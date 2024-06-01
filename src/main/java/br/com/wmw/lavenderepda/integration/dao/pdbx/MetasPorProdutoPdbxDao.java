package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MetasPorProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetasPorProduto();
	}

    private static MetasPorProdutoPdbxDao instance;

    public MetasPorProdutoPdbxDao() {
        super(MetasPorProduto.TABLE_NAME);
    }

    public static MetasPorProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new MetasPorProdutoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MetasPorProduto metasPorProduto = new MetasPorProduto();
        metasPorProduto.rowKey = rs.getString("rowkey");
        if (LavenderePdaConfig.usaRelMetasProdutoIndependenteDoCadDeProdutos) {
	        metasPorProduto.cdProduto = rs.getString("CDPRODUTO");
	        metasPorProduto.dsProduto = rs.getString("DSPRODUTO");
    	} else {
    		metasPorProduto.cdProduto = rs.getString("CDPRODUTO");
    		metasPorProduto.dsProduto = rs.getString("DSPRODUTO");
    	}
        metasPorProduto.vlMeta = rs.getDouble("vlMeta");
        metasPorProduto.vlRealizado = rs.getDouble("VlRealizado");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
            metasPorProduto.qtUnidadeMeta = rs.getDouble("qtUnidadeMeta");
            metasPorProduto.qtUnidadeRealizado = rs.getDouble("qtUnidadeRealizado");
        	metasPorProduto.qtCaixaMeta = rs.getDouble("qtCaixaMeta");
        	metasPorProduto.qtCaixaRealizado = rs.getDouble("qtCaixaRealizado");
        	metasPorProduto.qtMixClienteMeta = rs.getDouble("qtMixClienteMeta");
        	metasPorProduto.qtMixClienteRealizado = rs.getDouble("qtMixClienteRealizado");
        }
        return metasPorProduto;
	}

	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        if (LavenderePdaConfig.usaRelMetasProdutoIndependenteDoCadDeProdutos) {
        	sql.append(" tb.CDPRODUTO,");
    		sql.append(" tb.DSPRODUTO,");
    	} else {
    		sql.append(" p.CDPRODUTO,");
    		sql.append(" p.DSPRODUTO,");
    	}
        sql.append(" tb.VlMeta,");
        sql.append(" tb.VlRealizado");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
            sql.append(" , tb.qtUnidadeMeta,");
            sql.append(" tb.qtUnidadeRealizado,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtCaixaRealizado,");
        	sql.append(" tb.qtMixClienteMeta,");
        	sql.append(" tb.qtMixClienteRealizado");
        }
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" VlMeta,");
        sql.append(" VlRealizado,");
        sql.append(" qtUnidadeMeta,");
        sql.append(" qtUnidadeRealizado,");
        sql.append(" DSPRODUTO,");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
        	sql.append(" qtCaixaMeta,");
        	sql.append(" qtCaixaRealizado,");
        	sql.append(" qtMixClienteMeta,");
        	sql.append(" qtMixClienteRealizado,");
        }
        sql.append(" DSPERIODO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MetasPorProduto metasporproduto = new MetasPorProduto();
        metasporproduto.rowKey = rs.getString("rowkey");
        metasporproduto.cdEmpresa = rs.getString("cdEmpresa");
        metasporproduto.cdRepresentante = rs.getString("cdRepresentante");
        metasporproduto.cdProduto = rs.getString("cdProduto");
        metasporproduto.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        metasporproduto.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        metasporproduto.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidademeta"));
        metasporproduto.qtUnidadeRealizado = ValueUtil.round(rs.getDouble("qtUnidadeRealizado"));
        metasporproduto.dsProduto = rs.getString("dsProduto");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
        	metasporproduto.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixameta"));
        	metasporproduto.qtCaixaRealizado = ValueUtil.round(rs.getDouble("qtCaixaRealizado"));
        	metasporproduto.qtMixClienteMeta = ValueUtil.round(rs.getDouble("qtMixclientemeta"));
        	metasporproduto.qtMixClienteRealizado = ValueUtil.round(rs.getDouble("qtMixClienteRealizado"));
        }
        metasporproduto.dsPeriodo = rs.getString("dsPeriodo");
        return metasporproduto;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetasPorProduto metasporproduto = (MetasPorProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (metasporproduto.findDescProduto && !LavenderePdaConfig.usaRelMetasProdutoIndependenteDoCadDeProdutos) {
			sqlWhereClause.addJoin(", TBLVPPRODUTO p where tb.cdProduto = p.cdProduto and  tb.cdEmpresa = p.cdEmpresa and  p.cdRepresentante = " + Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
		}
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", metasporproduto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", metasporproduto.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", metasporproduto.cdProduto);
		sqlWhereClause.addAndCondition("tb.DSPERIODO = ", metasporproduto.dsPeriodo);
		sql.append(sqlWhereClause.getSql());
		//--
       	if (!ValueUtil.isEmpty(metasporproduto.cdProdutoLike)) {
			String prefixo = LavenderePdaConfig.usaRelMetasProdutoIndependenteDoCadDeProdutos ? "tb." : "p.";
			sql.append(" and ( ").append(prefixo).append("cdProduto LIKE ");
			sql.append(Sql.getValue(metasporproduto.cdProdutoLike));
			sql.append(" OR ").append(prefixo).append("DSPRODUTO LIKE ");
			sql.append(Sql.getValue(metasporproduto.cdProdutoLike));
			sql.append(" ) ");
       	}
    }

    protected void addSelectGridColumns(StringBuffer sql, BaseDomain domain) {
        sql.append(" tb.rowkey,");
        if (LavenderePdaConfig.usaRelMetasProdutoIndependenteDoCadDeProdutos) {
        	sql.append(" tb.CDPRODUTO,");
    		sql.append(" tb.DSPRODUTO,");
    	} else {
    		sql.append(" p.CDPRODUTO,");
    		sql.append(" p.DSPRODUTO,");
    	}
        sql.append(" tb.VlMeta,");
        sql.append(" tb.VlRealizado");
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
            sql.append(" , tb.qtUnidadeMeta,");
            sql.append(" tb.qtUnidadeRealizado,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtCaixaRealizado,");
        	sql.append(" tb.qtMixClienteMeta,");
        	sql.append(" tb.qtMixClienteRealizado");
        }
    }

    protected void addOrderByGrid(StringBuffer sql) {
    	String orderBy = LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? "CDPRODUTO" : "DSPRODUTO";
    	if (LavenderePdaConfig.usaRelMetasProdutoIndependenteDoCadDeProdutos) {
    		sql.append(" order by tb.").append(orderBy);
    	} else {
    		sql.append(" order by p.").append(orderBy);
    	}
    }

    //@Override
    public Vector findAllDistinctPeriodo(MetasPorProduto metasPorProdutoFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select DISTINCT DSPERIODO from ");
        sql.append(tableName);
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", metasPorProdutoFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", metasPorProdutoFilter.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
        sql.append(" order by NUSEQUENCIA");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
		        result.addElement(rs.getString("DSPERIODO"));
			}
			return result;
		}
    }

}