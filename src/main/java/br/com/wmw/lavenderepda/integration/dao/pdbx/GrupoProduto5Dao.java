package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto5;
import totalcross.sql.ResultSet;

public class GrupoProduto5Dao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProduto5();
	}

	private static GrupoProduto5Dao instance;
	
	public GrupoProduto5Dao() {
		super(GrupoProduto5.TABLE_NAME);
	}
	
	public static GrupoProduto5Dao getInstance() {
		if (instance == null) {
			instance = new GrupoProduto5Dao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		GrupoProduto5 grupoProduto5 = new GrupoProduto5();
		grupoProduto5.rowKey = rs.getString("rowkey");
		grupoProduto5.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
		grupoProduto5.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
		grupoProduto5.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
		grupoProduto5.cdGrupoProduto4 = rs.getString("cdGrupoProduto4");
		grupoProduto5.cdGrupoProduto5 = rs.getString("cdGrupoProduto5");
		grupoProduto5.dsGrupoProduto5 = rs.getString("dsGrupoProduto5");
		return grupoProduto5;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("rowkey,")
		.append("CDGRUPOPRODUTO1,")
		.append("CDGRUPOPRODUTO2,")
		.append("CDGRUPOPRODUTO3,")
		.append("CDGRUPOPRODUTO4,")
		.append("CDGRUPOPRODUTO5,")
		.append("DSGRUPOPRODUTO5");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		GrupoProduto5 grupoProduto5 = (GrupoProduto5) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", grupoProduto5.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", grupoProduto5.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", grupoProduto5.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO4 = ", grupoProduto5.cdGrupoProduto4);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO5 = ", grupoProduto5.cdGrupoProduto5);
	}

}
