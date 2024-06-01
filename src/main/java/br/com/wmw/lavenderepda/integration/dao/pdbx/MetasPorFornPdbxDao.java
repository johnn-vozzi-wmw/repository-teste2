package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorForn;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MetasPorFornPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetasPorForn();
	}

    private static MetasPorFornPdbxDao instance;

    public MetasPorFornPdbxDao() {
        super(MetasPorForn.TABLE_NAME);
    }

    public static MetasPorFornPdbxDao getInstance() {
        if (instance == null) {
            instance = new MetasPorFornPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetasPorForn metasporfornecedor = new MetasPorForn();
        metasporfornecedor.rowKey = rs.getString("rowkey");
        metasporfornecedor.cdEmpresa = rs.getString("cdEmpresa");
        metasporfornecedor.cdRepresentante = rs.getString("cdRepresentante");
        metasporfornecedor.dsPeriodo = rs.getString("dsPeriodo");
        metasporfornecedor.cdFornecedor = rs.getString("cdFornecedor");
        metasporfornecedor.dsFornecedor = rs.getString("trim(f.nmRazaoSocial)");
        metasporfornecedor.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        metasporfornecedor.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	metasporfornecedor.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidademeta"));
        	metasporfornecedor.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixameta"));
        	metasporfornecedor.qtMixClienteMeta = ValueUtil.round(rs.getDouble("qtMixclientemeta"));
        	metasporfornecedor.qtMixProdutoMeta = ValueUtil.round(rs.getDouble("qtMixprodutometa"));
        }
        return metasporfornecedor;
	}

	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.DSPERIODO,");
        sql.append(" tb.cdFornecedor,");
        sql.append(" trim(f.nmRazaoSocial),");
        sql.append(" tb.VlMeta,");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	sql.append(" tb.qtUnidadeMeta,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtMixClienteMeta,");
        	sql.append(" tb.qtMixProdutoMeta,");
        }
        sql.append(" tb.VlRealizado");
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.DSPERIODO,");
        sql.append(" tb.cdFornecedor,");
        sql.append(" tb.VlMeta,");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	sql.append(" tb.qtUnidadeMeta,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtMixClienteMeta,");
        	sql.append(" tb.qtMixProdutoMeta,");
        }
        sql.append(" tb.VlRealizado");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetasPorForn metasporfornecedor = new MetasPorForn();
        metasporfornecedor.rowKey = rs.getString("rowkey");
        metasporfornecedor.cdEmpresa = rs.getString("cdEmpresa");
        metasporfornecedor.cdRepresentante = rs.getString("cdRepresentante");
        metasporfornecedor.dsPeriodo = rs.getString("dsPeriodo");
        metasporfornecedor.cdFornecedor = rs.getString("cdFornecedor");
        metasporfornecedor.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        metasporfornecedor.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	metasporfornecedor.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidademeta"));
        	metasporfornecedor.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixameta"));
        	metasporfornecedor.qtMixClienteMeta = ValueUtil.round(rs.getDouble("qtMixclientemeta"));
        	metasporfornecedor.qtMixProdutoMeta = ValueUtil.round(rs.getDouble("qtMixprodutometa"));
        }
        return metasporfornecedor;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetasPorForn metasporfornecedor = (MetasPorForn) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", metasporfornecedor.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", metasporfornecedor.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.DSPERIODO = ", metasporfornecedor.dsPeriodo);
		sqlWhereClause.addAndCondition("tb.cdFornecedor = ", metasporfornecedor.cdFornecedor);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addSelectGridColumns(StringBuffer sql, BaseDomain domain) {
        sql.append(" tb.rowkey,");
        sql.append(" f.NMRAZAOSOCIAL,");
        sql.append(" tb.VlMeta,");
        sql.append(" tb.VlRealizado");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
            sql.append(" , tb.qtUnidadeMeta,");
        	sql.append(" tb.qtCaixameta,");
        	sql.append(" tb.qtMixclientemeta,");
        	sql.append(" tb.qtMixprodutometa");
        }
    }

    //@Override
    public Vector findAllDistinctPeriodo(MetasPorForn metasPorFornFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select DISTINCT DSPERIODO from ");
        sql.append(tableName);
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", metasPorFornFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", metasPorFornFilter.cdRepresentante);
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
    
    @Override
    protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
    	sql.append("left join TBLVPFORNECEDOR f on tb.cdFornecedor = f.cdFornecedor and  tb.cdEmpresa = f.cdEmpresa ");
    }

}