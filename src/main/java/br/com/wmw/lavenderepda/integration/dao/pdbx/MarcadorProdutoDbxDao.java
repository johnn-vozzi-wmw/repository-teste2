package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.MarcadorProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MarcadorProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MarcadorProduto();
	}

    private static MarcadorProdutoDbxDao instance;

    public MarcadorProdutoDbxDao() {
        super(MarcadorProduto.TABLE_NAME); 
    }
    
    public static MarcadorProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new MarcadorProdutoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        MarcadorProduto marcadorProduto = new MarcadorProduto();
        marcadorProduto.rowKey = rs.getString("rowkey");
        marcadorProduto.cdEmpresa = rs.getString("cdEmpresa");
        marcadorProduto.cdRepresentante = rs.getString("cdRepresentante");
        marcadorProduto.cdProduto = rs.getString("cdProduto");
        marcadorProduto.cdMarcador = rs.getString("cdMarcador");
        marcadorProduto.cdCliente = rs.getString("cdCliente");
        marcadorProduto.cdUsuario = rs.getString("cdUsuario");
        marcadorProduto.nuCarimbo = rs.getInt("nuCarimbo");
        marcadorProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return marcadorProduto;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDMARCADOR,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDMARCADOR,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        MarcadorProduto marcadorProduto = (MarcadorProduto) domain;
        sql.append(Sql.getValue(marcadorProduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(marcadorProduto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(marcadorProduto.cdProduto)).append(",");
        sql.append(Sql.getValue(marcadorProduto.cdMarcador)).append(",");
        sql.append(Sql.getValue(marcadorProduto.cdCliente)).append(",");
        sql.append(Sql.getValue(marcadorProduto.cdUsuario)).append(",");
        sql.append(Sql.getValue(marcadorProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(marcadorProduto.flTipoAlteracao)).append(",");
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        MarcadorProduto marcadorProduto = (MarcadorProduto) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(marcadorProduto.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(marcadorProduto.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(marcadorProduto.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MarcadorProduto marcadorProduto = (MarcadorProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", marcadorProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", marcadorProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", marcadorProduto.cdProduto);
		sqlWhereClause.addAndCondition("CDMARCADOR = ", marcadorProduto.cdMarcador);
		sqlWhereClause.addAndCondition("(CDCLIENTE = " + Sql.getValue(marcadorProduto.cdCliente) + " OR CDCLIENTE = '0')");
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public Vector findAllByProdutoFilter(MarcadorProduto filter, boolean showFlAgrupadorGrade, Vector cdMarcadores) throws SQLException{
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(mountSqlByProdutoFilter(filter, showFlAgrupadorGrade))) {
    		Vector marcadores = new Vector();
    		while (rs.next()) {
    			Marcador marcador = new Marcador();
    			marcador.cdMarcador = rs.getString("CDMARCADOR");
				if (showFlAgrupadorGrade) {
					marcador.flAgrupadorGrade = rs.getString("FLAGRUPADORGRADE");
				}
    			marcadores.addElement(marcador);
    			cdMarcadores.addElement(marcador.cdMarcador);
    		}
    		return marcadores;
    	}
    }
    
    public Vector findMarcadoresByProduto(MarcadorProduto filter) throws SQLException {
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(mountSqlByProdutoFilter(filter, false))) {
            Vector vector = new Vector();
            while (rs.next()) {
                vector.addElement(rs.getString(1));
            }
            return vector;
        }
    }
    
    private String mountSqlByProdutoFilter(MarcadorProduto filter, boolean showFlAgrupadorGrade) {
    	StringBuffer sql = getSqlBuffer();
        sql.append("SELECT ");
        getProdutoFilterSelectColumns(sql, showFlAgrupadorGrade);
        sql.append(" FROM ")
        .append(tableName).append(" TB ")
        .append(" JOIN TBLVPMARCADOR MARCADOR ON ")
        .append(" MARCADOR.CDMARCADOR = TB.CDMARCADOR ")
        .append(" AND (COALESCE(MARCADOR.DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(MARCADOR.DTTERMINOVIGENCIA, DATE()) >= DATE()) ");
        addWhereByExample(filter, sql);
        sql.append(" ORDER BY FLAGRUPADORGRADE DESC, MARCADOR.NUSEQUENCIA, MARCADOR.DSMARCADOR");
        
        return sql.toString();
    }
    
    private void getProdutoFilterSelectColumns(StringBuffer sql, boolean showFlAgrupadorGrade) {
    	sql.append(" TB.CDMARCADOR ");
    	if (showFlAgrupadorGrade) sql.append(", IFNULL(MARCADOR.FLAGRUPADORGRADE, 'N') FLAGRUPADORGRADE ");
    }

}