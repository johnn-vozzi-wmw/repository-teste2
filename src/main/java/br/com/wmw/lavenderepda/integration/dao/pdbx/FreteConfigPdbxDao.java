package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import totalcross.sql.ResultSet;

public class FreteConfigPdbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FreteConfig();
	}

    private static FreteConfigPdbxDao instance;

    public FreteConfigPdbxDao() {
        super(FreteConfig.TABLE_NAME);
    }

    public static FreteConfigPdbxDao getInstance() {
        return (instance == null) ? instance = new FreteConfigPdbxDao() : instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	FreteConfig freteConfig = new FreteConfig(rs.getString("cdEmpresa"), rs.getString("cdRepresentante"), rs.getString("cdTransportadora"));
        freteConfig.rowKey = rs.getString("rowkey");
        freteConfig.cdFreteConfig = rs.getString("cdFreteConfig");
        freteConfig.dsFreteConfig = rs.getString("dsFreteConfig");
        freteConfig.cdTipoFrete = rs.getString("cdTipoFrete");
        freteConfig.flTipoTabFrete = rs.getString("flTipoTabFrete");
        freteConfig.cdRegiao = rs.getString("cdRegiao");
        freteConfig.nuCepInicio = rs.getInt("nuCepInicio");
        freteConfig.nuCepFim = rs.getInt("nuCepFim");
        return freteConfig;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTRANSPORTADORA,");
        sql.append(" t.NMTRANSPORTADORA,");
        sql.append(" tb.CDFRETECONFIG,");
        sql.append(" tb.DSFRETECONFIG,");
        sql.append(" tb.CDTIPOFRETE,");
        sql.append(" tb.CDREGIAO,");
        sql.append(" tb.NUCEPINICIO,");
        sql.append(" tb.NUCEPFIM,");
        sql.append(" tb.FLTIPOTABFRETE");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FreteConfig freteConfigFilter = (FreteConfig) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", freteConfigFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", freteConfigFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDTRANSPORTADORA = ", freteConfigFilter.cdTransportadora);
		sqlWhereClause.addAndCondition("tb.CDFRETECONFIG = ", freteConfigFilter.cdFreteConfig);
		sqlWhereClause.addAndCondition("tb.DSFRETECONFIG = ", freteConfigFilter.dsFreteConfig);
		sqlWhereClause.addAndCondition("tb.CDTIPOFRETE = ", freteConfigFilter.cdTipoFrete);
		sqlWhereClause.addAndCondition("tb.FLTIPOTABFRETE = ", freteConfigFilter.flTipoTabFrete);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	if (domain.sortAtributte.equalsIgnoreCase("VLPRECOFRETECALCULADO")) return;
    	super.addOrderByWithoutAlias(sql, domain);
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	sql.append("LEFT JOIN TBLVPTRANSPORTADORA t ");
    	sql.append("on t.CDEMPRESA = tb.CDEMPRESA ");
    	sql.append("and t.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
    	sql.append("and t.CDTRANSPORTADORA = tb.CDTRANSPORTADORA ");
    }

}