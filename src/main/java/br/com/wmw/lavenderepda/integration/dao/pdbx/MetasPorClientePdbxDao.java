package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorCliente;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MetasPorClientePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetasPorCliente();
	}

    private static MetasPorClientePdbxDao instance;

    public MetasPorClientePdbxDao() {
        super(MetasPorCliente.TABLE_NAME);
    }

    public static MetasPorClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new MetasPorClientePdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		MetasPorCliente metasporcliente = new MetasPorCliente();
		metasporcliente.rowKey = rs.getString("rowkey");
		metasporcliente.cdEmpresa = rs.getString("cdEmpresa");
		metasporcliente.cdRepresentante = rs.getString("cdRepresentante");
		metasporcliente.dsPeriodo = rs.getString("dsPeriodo");
		metasporcliente.cdCliente = rs.getString("cdCliente");
		metasporcliente.dsCliente = rs.getString("NMRAZAOSOCIAL");
		metasporcliente.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
		metasporcliente.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
		if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
			metasporcliente.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidademeta"));
			metasporcliente.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixameta"));
			metasporcliente.qtMixProdutoMeta = ValueUtil.round(rs.getDouble("qtMixprodutometa"));
		}
		return metasporcliente;
	}

	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.DSPERIODO,");
        sql.append(" tb.cdCliente,");
        sql.append(" c.NMRAZAOSOCIAL,");
        sql.append(" tb.VlMeta,");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	sql.append(" tb.qtUnidadeMeta,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtMixProdutoMeta,");
        }
        sql.append(" tb.VlRealizado");
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MetasPorCliente metasporcliente = new MetasPorCliente();
        metasporcliente.rowKey = rs.getString("rowkey");
        metasporcliente.cdEmpresa = rs.getString("cdEmpresa");
        metasporcliente.cdRepresentante = rs.getString("cdRepresentante");
        metasporcliente.dsPeriodo = rs.getString("dsPeriodo");
        metasporcliente.cdCliente = rs.getString("cdCliente");
        metasporcliente.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        metasporcliente.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	metasporcliente.qtUnidadeMeta = ValueUtil.round(rs.getDouble("qtUnidademeta"));
        	metasporcliente.qtCaixaMeta = ValueUtil.round(rs.getDouble("qtCaixameta"));
        	metasporcliente.qtMixProdutoMeta = ValueUtil.round(rs.getDouble("qtMixprodutometa"));
        }

        return metasporcliente;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.DSPERIODO,");
        sql.append(" tb.cdCliente,");
        sql.append(" tb.VlMeta,");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	sql.append(" tb.qtUnidadeMeta,");
        	sql.append(" tb.qtCaixaMeta,");
        	sql.append(" tb.qtMixProdutoMeta,");
        }
        sql.append(" tb.VlRealizado");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetasPorCliente metasporcliente = (MetasPorCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addJoin(", TBLVPCLIENTE c where tb.cdCliente = c.cdCliente and tb.cdEmpresa = c.cdEmpresa and tb.cdRepresentante = c.cdRepresentante");
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", metasporcliente.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", metasporcliente.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.DSPERIODO = ", metasporcliente.dsPeriodo);
		sqlWhereClause.addAndCondition("tb.cdCliente = ", metasporcliente.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addSelectGridColumns(StringBuffer sql, BaseDomain domain) {
        sql.append(" tb.rowkey,");
        sql.append(" c.NMRAZAOSOCIAL,");
        sql.append(" tb.VlMeta,");
        sql.append(" tb.VlRealizado");
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
            sql.append(" , tb.qtUnidadeMeta,");
        	sql.append(" tb.qtCaixameta,");
        	sql.append(" tb.qtMixProdutoMeta");
        }
    }

    //@Override
    public Vector findAllDistinctPeriodo(MetasPorCliente metasPorClienteFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select DISTINCT DSPERIODO from ");
        sql.append(tableName);
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", metasPorClienteFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", metasPorClienteFilter.cdRepresentante);
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