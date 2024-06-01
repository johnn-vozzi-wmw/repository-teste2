package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Bandeira;
import totalcross.sql.ResultSet;

public class BandeiraDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Bandeira();
	}

	private static BandeiraDao instance;

    public BandeiraDao() {
        super(Bandeira.TABLE_NAME);
    }

    public static BandeiraDao getInstance() {
        if (instance == null) {
            instance = new BandeiraDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	Bandeira bandeira = new Bandeira();
        bandeira.rowKey = rs.getString("rowkey");
        bandeira.cdEmpresa = rs.getString("cdEmpresa");
        bandeira.cdBandeira = rs.getString("cdBandeira");
        bandeira.dsBandeira = rs.getString("dsBandeira");
        return bandeira;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDBANDEIRA,");
        sql.append(" DSBANDEIRA");
    }

	//@Override
	protected void addInsertColumns(StringBuffer arg0) { }

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) { }

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) { }

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	Bandeira bandeira = new Bandeira();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", bandeira.cdEmpresa);
		sqlWhereClause.addAndCondition("CDBANDEIRA = ", bandeira.cdBandeira);
		sql.append(sqlWhereClause.getSql());
	}

}