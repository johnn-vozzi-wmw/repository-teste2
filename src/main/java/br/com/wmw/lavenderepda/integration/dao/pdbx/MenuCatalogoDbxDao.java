package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Representante;
import totalcross.sql.ResultSet;
import totalcross.sql.ResultSetMetaData;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MenuCatalogoDbxDao extends LavendereCrudDbxDao {

    private static MenuCatalogoDbxDao instance;

    public static MenuCatalogoDbxDao getInstance() {
        if (instance == null) {
            instance = new MenuCatalogoDbxDao();
        }
        return instance;
    }

    public MenuCatalogoDbxDao() {
        super(MenuCatalogo.TABLE_NAME);
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MenuCatalogo menuCatalogo = new MenuCatalogo();
        menuCatalogo.cdFuncionalidade = rs.getString("cdFuncionalidade");
        menuCatalogo.nuNivel = rs.getInt("nuNivel");
        menuCatalogo.nmEntidade = rs.getString("nmEntidade");
        menuCatalogo.nmMenu = rs.getString("nmMenu");
        menuCatalogo.dsSql = rs.getString("dsSql");
        menuCatalogo.flIndividual = rs.getString("flIndividual");
        menuCatalogo.rowKey = rs.getString("rowKey");
        return menuCatalogo;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" CDFUNCIONALIDADE, ");
        sql.append(" NUNIVEL, ");
        sql.append(" NMENTIDADE, ");
        sql.append(" NMMENU, ");
        sql.append(" DSSQL, ");
        sql.append(" FLINDIVIDUAL, ");
        sql.append(" ROWKEY ");
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        MenuCatalogo filter = (MenuCatalogo) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndConditionEquals("CDFUNCIONALIDADE", filter.cdFuncionalidade);
        sqlWhereClause.addAndConditionEquals("NMENTIDADE", filter.nuNivel);
        sqlWhereClause.addAndConditionEquals("FLINDIVIDUAL", filter.flIndividual);
        sql.append(sqlWhereClause.getSql());
    }

    @Override
    protected BaseDomain getBaseDomainDefault() {
        return new MenuCatalogo();
    }

    public MenuCatalogo findEntidadePrimeiroNivel(String cdFuncionalidade) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(tableName);
        sql.append(" WHERE NUNIVEL = 1");
        sql.append(" AND CDFUNCIONALIDADE = ").append(Sql.getValue(cdFuncionalidade));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
            if (rs.next()) {
                return (MenuCatalogo) populate(null, rs);
            }
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
        return null;
    }

    public int countRegistrosEntidade(String sql) throws SQLException {
    	return getInt(sql);
    }

    public int countRegistrosEntidadeSql(MenuCatalogo menuCatalogo) throws SQLException {
        StringBuffer sql;
        String dsSqlWithQuotes = menuCatalogo.dsSql.replaceAll("&", "'");
        sql = transformSqlFilters(menuCatalogo, dsSqlWithQuotes);
        String sqlCount = " SELECT SUM(QTDE) AS sumQtde FROM (SELECT COUNT(1) qtde FROM (";
        String sqlFinal = sqlCount + sql + "))";
        return countRegistrosEntidade(sqlFinal);
    }

    public Vector findRegistrosEntidadeSql(MenuCatalogo menuCatalogo) throws SQLException {
        StringBuffer sqlFinal;
        String sqlConsulta = menuCatalogo.dsSql.replaceAll("&", "'");
        sqlFinal = transformSqlFilters(menuCatalogo, sqlConsulta);
        //--
        return executeSqlRegistrosEntidade(menuCatalogo, sqlFinal);
    }

    private Vector executeSqlRegistrosEntidade(MenuCatalogo menuCatalogo, StringBuffer sqlFinal) throws SQLException {
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sqlFinal.toString())) {
        	Vector resultList = new Vector(rs.getFetchSize());
            ResultSetMetaData rsmd = rs.getMetaData();
            int size = rsmd.getColumnCount();
            Vector resultColumns = new Vector(size);
            for (int i = 1; i <= size; i++) {
                resultColumns.addElement(rsmd.getColumnName(i));
            }
            menuCatalogo.nmColunaDescricao = (String) resultColumns.elementAt(resultColumns.size() - 1);
            while (rs.next()) {
                resultList.addElement(populatePersonLinkedMap(rs, resultColumns));
            }
            return resultList;
        } catch (Exception e) {
            ExceptionUtil.handle(e);
            return null;
        }
    }

    private StringBuffer transformSqlFilters(MenuCatalogo menuCatalogo, String sqlConsulta) {
        StringBuffer sqlFinal;
        int indexInicioParam = sqlConsulta.indexOf(":");
        String sqlConsultaPartial = "";
        boolean contailsUIFilter = ValueUtil.isNotEmpty(menuCatalogo.dsFiltro);
        if (indexInicioParam != -1) {
            sqlConsultaPartial = sqlConsulta.substring(indexInicioParam + 1);
            if (contailsUIFilter) {
                sqlFinal = new StringBuffer();
                sqlFinal.append(" SELECT * FROM ( ");
                sqlFinal.append(sqlConsulta, 0, indexInicioParam);
            } else {
                sqlFinal = new StringBuffer(sqlConsulta.substring(0, indexInicioParam));
            }
        } else {
            if (contailsUIFilter) {
                sqlFinal = new StringBuffer();
                sqlFinal.append(" SELECT * FROM ( ");
                sqlFinal.append(sqlConsulta);
            } else {
                sqlFinal = new StringBuffer(sqlConsulta);
            }
        }
        while (indexInicioParam != -1) {
            int indexFinalParam = sqlConsultaPartial.indexOf(" ");
            int indexFinalPercentual = sqlConsultaPartial.indexOf("%");
            if (indexFinalPercentual != -1 && (indexFinalPercentual < indexFinalParam || indexFinalParam == -1)) {
                indexFinalParam = indexFinalPercentual;
            }
            //--
            indexFinalParam = indexFinalParam == -1 ? sqlConsultaPartial.length() : indexFinalParam;
            String nmCampo = sqlConsultaPartial.substring(0, indexFinalParam).trim();
            String paramValue = StringUtil.getStringValue(menuCatalogo.valoresFiltroTelaPaiHash.get(nmCampo.toUpperCase()));
            if (Representante.NMCOLUNA_CDREPRESENTANTE.equalsIgnoreCase(nmCampo) && ValueUtil.isEmpty(paramValue)) {
                paramValue = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
            }
            if (Empresa.NMCOLUNA_CDEMPRESA.equalsIgnoreCase(nmCampo) && ValueUtil.isEmpty(paramValue)) {
                paramValue = SessionLavenderePda.cdEmpresa;
            }
            if (ValueUtil.isEmpty(paramValue) && menuCatalogo.pedidoJsonFilters != null) {
                paramValue = menuCatalogo.pedidoJsonFilters.getString(nmCampo);
            }
            sqlFinal.append(Sql.getValue(paramValue));
            sqlConsultaPartial = sqlConsultaPartial.substring(indexFinalParam);
            indexInicioParam = sqlConsultaPartial.indexOf(":");
            if (indexInicioParam != -1) {
                sqlFinal.append(sqlConsultaPartial, 0, indexInicioParam);
                sqlConsultaPartial = sqlConsultaPartial.substring(indexInicioParam + 1);
            } else {
                sqlFinal.append(sqlConsultaPartial);
            }
        }
        if (contailsUIFilter) {
            sqlFinal.append(" ) ");
            sqlFinal.append(" WHERE UPPER(").append(menuCatalogo.nmColunaDescricao).append(") LIKE UPPER('%").append(menuCatalogo.dsFiltro).append("%') ");
        }
        menuCatalogo.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
        addLimit(sqlFinal, menuCatalogo);
        return sqlFinal;
    }

    private boolean isDsSqlContainsGroupBy(String sqlConsulta) {
        return sqlConsulta.toUpperCase().contains("GROUP BY");
    }

    private boolean isDsSqlContainsOrderBy(String sqlFinal) {
        return sqlFinal.toUpperCase().contains("ORDER BY");
    }

    private void addDsFiltroTela(StringBuffer sqlFinal, MenuCatalogo menuCatalogo) {
        String sqlFinalAsString = sqlFinal.toString();
        if (sqlFinalAsString.toUpperCase().contains("WHERE")) {
            sqlFinal.append(" AND ").append(menuCatalogo.nmColunaDescricao).append(" LIKE '%").append(menuCatalogo.dsFiltro).append("%' ");
        } else {
            sqlFinal.append(" WHERE UPPER(").append(menuCatalogo.nmColunaDescricao).append(") LIKE UPPER('%").append(menuCatalogo.dsFiltro).append("%') ");
        }
    }

    public Vector findAllEntidadesAgrupadas() throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT NMENTIDADE FROM ").append(tableName);
        sql.append(" WHERE NMENTIDADE != ").append(Sql.getValue(Produto.class.getSimpleName()));
        sql.append(" GROUP BY NMENTIDADE");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	Vector menuCatalogoList = new Vector(rs.getFetchSize());
        	MenuCatalogo menuCatalogo;
            while (rs.next()) {
            	menuCatalogo = new MenuCatalogo();
            	menuCatalogo.nmEntidade = rs.getString(1);
                menuCatalogoList.addElement(menuCatalogo);
            }
            return menuCatalogoList;
        } catch (Exception e) {
            ExceptionUtil.handle(e);
            return null;
        }
    }

}
