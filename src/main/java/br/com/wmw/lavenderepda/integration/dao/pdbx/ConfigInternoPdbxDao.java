package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import totalcross.sql.ResultSet;

public class ConfigInternoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ConfigInterno();
	}

    private static ConfigInternoPdbxDao instance;

    public ConfigInternoPdbxDao() {
        super(ConfigInterno.TABLE_NAME);
    }

    public static ConfigInternoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ConfigInternoPdbxDao();
        }
        return instance;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDCONFIGINTERNO,");
        sql.append(" VLCHAVE,");
        sql.append(" VLCONFIGINTERNO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ConfigInterno configInterno = new ConfigInterno();
        configInterno.rowKey = rs.getString("rowkey");
        configInterno.cdEmpresa = rs.getString("cdEmpresa");
        configInterno.cdConfigInterno = rs.getInt("cdConfigInterno");
        configInterno.vlChave = rs.getString("vlChave");
        configInterno.vlConfigInterno = rs.getString("vlConfigInterno");
        return configInterno;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDCONFIGINTERNO,");
        sql.append(" VLCHAVE,");
        sql.append(" nuCarimbo,");
        sql.append(" VLCONFIGINTERNO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ConfigInterno configInterno = (ConfigInterno) domain;
        sql.append(Sql.getValue(configInterno.cdEmpresa)).append(",");
        sql.append(Sql.getValue(configInterno.cdConfigInterno)).append(",");
        sql.append(Sql.getValue(configInterno.vlChave)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(configInterno.vlConfigInterno));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ConfigInterno configInterno = (ConfigInterno) domain;
        sql.append(" VLCONFIGINTERNO = ").append(Sql.getValue(configInterno.vlConfigInterno));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ConfigInterno configInterno = (ConfigInterno) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", configInterno.cdEmpresa);
		sqlWhereClause.addAndCondition("CDCONFIGINTERNO = ", configInterno.cdConfigInterno);
		sqlWhereClause.addAndCondition("VLCHAVE = ", configInterno.vlChave);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}