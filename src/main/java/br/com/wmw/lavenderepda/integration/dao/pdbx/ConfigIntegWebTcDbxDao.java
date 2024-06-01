package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import totalcross.sql.ResultSet;

public class ConfigIntegWebTcDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ConfigIntegWebTc();
	}

    private static ConfigIntegWebTcDbxDao instance;

    public ConfigIntegWebTcDbxDao() {
        super(ConfigIntegWebTc.TABLE_NAME);
    }

    public static ConfigIntegWebTcDbxDao getInstance() {
        if (instance == null) {
            instance = new ConfigIntegWebTcDbxDao();
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
        sql.append(" rowkey,");
        sql.append(" dsTabela,");
        sql.append(" dsChave,");
        sql.append(" flRemessa,");
        sql.append(" flRetorno,");
        sql.append(" flAtivo");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ConfigIntegWebTc configIntegWebTc = new ConfigIntegWebTc();
        configIntegWebTc.rowKey = rs.getString("rowkey");
        configIntegWebTc.dsTabela = rs.getString("dsTabela");
        configIntegWebTc.dsChave = rs.getString("dsChave");
        configIntegWebTc.flRemessa = rs.getString("flRemessa");
        configIntegWebTc.flRetorno = rs.getString("flRetorno");
        configIntegWebTc.flAtivo = rs.getString("flAtivo");
        return configIntegWebTc;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ConfigIntegWebTc configIntegWebTc = (ConfigIntegWebTc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("dsTabela = ", configIntegWebTc.dsTabela);
		sqlWhereClause.addAndCondition("FLRETORNO = ", configIntegWebTc.flRetorno);
		sqlWhereClause.addAndCondition("FLREMESSA = ", configIntegWebTc.flRemessa);
		sqlWhereClause.addAndCondition("FLATIVO = ", configIntegWebTc.flAtivo);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}