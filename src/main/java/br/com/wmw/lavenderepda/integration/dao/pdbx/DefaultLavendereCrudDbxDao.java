package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import totalcross.sql.ResultSet;

public class DefaultLavendereCrudDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BaseDomain() {
			@Override
			public String getPrimaryKey() {
				return null;
			}
		};
	}

    private static DefaultLavendereCrudDbxDao instance;

	public static DefaultLavendereCrudDbxDao getInstance() {
        if (instance == null) {
            instance = new DefaultLavendereCrudDbxDao();
        }
        return instance;
    }

	public DefaultLavendereCrudDbxDao() {
		super("");
	}

	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException { }
	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	public int executeUpdate(String sql) throws java.sql.SQLException {
		return super.executeUpdate(sql);
	}
}
