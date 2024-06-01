package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Meta;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class MetaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Meta();
	}

    private static MetaPdbxDao instance;

    public MetaPdbxDao() {
        super(Meta.TABLE_NAME);
    }

    public static MetaPdbxDao getInstance() {
        if (instance == null) {
            instance = new MetaPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" QTPEDIDOS,");
        sql.append(" VLREALIZADOVENDAS,");
        sql.append(" VLMETAVENDAS,");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	sql.append(" qtUnidadeMeta,");
        	sql.append(" qtCaixaMeta,");
        	sql.append(" qtMixClienteMeta,");
        	sql.append(" qtMixProdutoMeta,");
        }
        sql.append(" DSPERIODO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" nuSequencia");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Meta meta = new Meta();
        meta.rowKey = rs.getString("rowkey");
        meta.cdEmpresa = rs.getString("cdEmpresa");
        meta.cdRepresentante = rs.getString("cdRepresentante");
        meta.qtPedidos = rs.getInt("qtPedidos");
        meta.vlRealizadoVendas = ValueUtil.round(rs.getDouble("vlRealizadoVendas"));
        meta.vlMetaVendas = ValueUtil.round(rs.getDouble("vlMetaVendas"));
        meta.dsPeriodo = rs.getString("dsPeriodo");
        meta.dtInicial = rs.getDate("dtInicial");
        meta.dtFinal = rs.getDate("dtFinal");
        meta.nuSequencia = rs.getString("nuSequencia");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	meta.qtMixClienteMeta = ValueUtil.round(rs.getDouble("qtMixClienteMeta"));
        	meta.qtMixProdutoMeta = ValueUtil.round(rs.getDouble("qtMixProdutoMeta"));
        	meta.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidadeMeta"));
        	meta.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixaMeta"));
        }
        return meta;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Meta produtividade = (Meta) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", produtividade.cdEmpresa);
       	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtividade.cdRepresentante);
       	if (LavenderePdaConfig.usaSistemaModoOffLine) {
       		sql.append(sqlWhereClause.getSql());
       		//--
        	Date dataAtual = DateUtil.getCurrentDate();
        	sql.append(" AND ((");
        	sql.append(" dtInicial <= " + Sql.getValue(dataAtual)).append(" AND ");
        	if (LavenderePdaConfig.usaNuDiasPermanenciaMetas()) {
        		DateUtil.decDay(dataAtual, LavenderePdaConfig.nuDiasPermanenciaMetas);
        	}
        	sql.append(" dtFinal >= " + Sql.getValue(dataAtual));
        	sql.append(")");
        	sql.append(" OR (");
        	sql.append(" dtFinal  = " + Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL)).append(" OR ");
        	sql.append(" dtInicial  = " + Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL));
        	sql.append(")) ");
       	} else {
       		sqlWhereClause.addAndCondition("DTINICIAL <= ", produtividade.dtInicial);
       		sqlWhereClause.addAndCondition("DTFINAL >= ", produtividade.dtFinal);
       		//--
       		sql.append(sqlWhereClause.getSql());
       	}
    }

    public Vector findAllMetasGroupByRepresentante(String cdEmpresa) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	sql.append(" tb.CDREPRESENTANTE,");
    	sql.append(" r.NMREPRESENTANTE,");
    	sql.append(" sum(tb.QTPEDIDOS) as qttotal,");
    	sql.append(" sum(tb.VLREALIZADOVENDAS) as vlRealizadoVendas,");
    	sql.append(" sum(tb.VLMETAVENDAS) as vlMetaVendas");
    	if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
    		sql.append(", sum(tb.qtUnidadeMeta) as qtUnidadeMeta,");
    		sql.append(" sum(tb.qtCaixaMeta) as qtCaixaMeta,");
    		sql.append(" sum(tb.qtMixProdutoMeta) as qtMixProdutoMeta,");
    		sql.append(" sum(tb.qtMixClienteMeta) as qtMixClienteMeta");
    	}
    	//--
    	sql.append(" from ").append(tableName).append(" tb,");
    	sql.append(" TBLVPREPRESENTANTE").append(" r where tb.cdRepresentante = r.cdRepresentante");
    	sql.append(" and tb.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
        if (LavenderePdaConfig.usaSistemaModoOffLine) {
        	Date dataAtual = DateUtil.getCurrentDate();
        	sql.append(" AND ((");
        	sql.append(" tb.dtInicial <= " + Sql.getValue(dataAtual)).append(" AND ");
        	if (LavenderePdaConfig.usaNuDiasPermanenciaMetas()) {
        		DateUtil.decDay(dataAtual, LavenderePdaConfig.nuDiasPermanenciaMetas);
        	}
        	sql.append(" tb.dtFinal >= " + Sql.getValue(dataAtual));
        	sql.append(")");
        	sql.append(" OR (");
        	sql.append(" tb.dtFinal  = " + Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL)).append(" OR ");
        	sql.append(" tb.dtInicial  = " + Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL));
        	sql.append(")) ");
        }
    	//--
    	sql.append(" group by tb.CDREPRESENTANTE");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
		        Meta meta = new Meta();
		        meta.cdRepresentante = rs.getString("cdRepresentante");
		        meta.nmRepresentante = rs.getString("NMREPRESENTANTE");
		        meta.qtPedidos = ValueUtil.getIntegerValue(rs.getDouble("qttotal"));
		        meta.vlRealizadoVendas = ValueUtil.round(rs.getDouble("vlRealizadoVendas"));
		        meta.vlMetaVendas = ValueUtil.round(rs.getDouble("vlMetaVendas"));
		        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
			        meta.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidadeMeta"));
			        meta.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixaMeta"));
			        meta.qtMixClienteMeta = ValueUtil.round(rs.getDouble("qtMixClienteMeta"));
			        meta.qtMixProdutoMeta = ValueUtil.round(rs.getDouble("qtMixProdutoMeta"));
		        }
				result.addElement(meta);
			}
			return result;
		}
    }

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	super.addOrderBy(sql, domain);
    	if (sql.toString().contains("order by")) {
    		sql.append(", nuSequencia");
    	} else {
    		sql.append(" order by nuSequencia");
    	}
    }

    //@Override
    protected void addSelectGridColumns(StringBuffer sql) {
        sql.append(" rowKey,");
        sql.append(" DSPERIODO,");
        sql.append(" QTPEDIDOS,");
        sql.append(" VLREALIZADOVENDAS,");
        sql.append(" VLMETAVENDAS,");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	sql.append(" qtUnidadeMeta,");
        	sql.append(" qtCaixaMeta,");
        	sql.append(" qtMixClienteMeta,");
        	sql.append(" qtMixProdutoMeta,");
        }
        sql.append(" nuSequencia");
    }

    //@Override
    protected void addOrderByGrid(StringBuffer sql) {
    	sql.append(" order by nuSequencia");
    }


}