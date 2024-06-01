package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ColunaTabelaPreco;
import totalcross.sql.ResultSet;

public class ColunaTabelaPrecoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ColunaTabelaPreco();
	}

	private static ColunaTabelaPrecoDao instance;

	public ColunaTabelaPrecoDao() {
		super(ColunaTabelaPreco.TABLE_NAME);
	}
	
	public static ColunaTabelaPrecoDao getInstance() {
		if (instance == null) {
			instance = new ColunaTabelaPrecoDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ColunaTabelaPreco colunaTabelaPreco = new ColunaTabelaPreco();
		colunaTabelaPreco.rowKey = rs.getString("rowKey");
		colunaTabelaPreco.cdEmpresa = rs.getString("cdEmpresa");
		colunaTabelaPreco.cdRepresentante = rs.getString("cdRepresentante");
		colunaTabelaPreco.cdColunaTabelaPreco = rs.getInt("cdColunaTabelaPreco");
		colunaTabelaPreco.dsColunaTabelaPreco = rs.getString("dsColunaTabelaPreco");
		return colunaTabelaPreco;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,")
		.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" CDCOLUNATABELAPRECO,")
		.append(" DSCOLUNATABELAPRECO");
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
		ColunaTabelaPreco colunaTabelaPreco = new ColunaTabelaPreco();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", colunaTabelaPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", colunaTabelaPreco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCOLUNATABELAPRECO = ", colunaTabelaPreco.cdColunaTabelaPreco);
	}

}
