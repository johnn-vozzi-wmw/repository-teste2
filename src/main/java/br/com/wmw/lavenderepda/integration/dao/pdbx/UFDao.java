package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.UF;
import totalcross.sql.ResultSet;

public class UFDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UF();
	}

	private static UFDao instance;

    public UFDao() {
        super(UF.TABLE_NAME);
    }

    public static UFDao getInstance() {
        if (instance == null) {
            instance = new UFDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	UF uf = new UF();
        uf.rowKey = rs.getString("rowkey");
        uf.cdUf = rs.getString("cdUf");
        uf.dsUf = rs.getString("dsUf");
        uf.cdUfIbge = rs.getString("cdUfIbge");
        uf.dsTimeZoneUf = rs.getString("dsTimeZoneUf");
        return uf;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDUF,");
        sql.append(" DSUF,");
        sql.append(" CDUFIBGE,");
        sql.append(" DSTIMEZONEUF");
    }

	//@Override
	protected void addInsertColumns(StringBuffer arg0) {
	}

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	UF uf = (UF) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDUF = ", uf.cdUf);
		sqlWhereClause.addAndCondition("DSUF = ", uf.dsUf);
		sql.append(sqlWhereClause.getSql());
	}

}