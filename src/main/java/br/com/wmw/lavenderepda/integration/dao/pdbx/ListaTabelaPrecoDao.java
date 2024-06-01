package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ListaTabelaPreco;
import totalcross.sql.ResultSet;

public class ListaTabelaPrecoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ListaTabelaPreco();
	}

	private static ListaTabelaPrecoDao instance;
	
	public ListaTabelaPrecoDao() {
		super(ListaTabelaPreco.TABLE_NAME);
	}
	
	public static ListaTabelaPrecoDao getInstance() {
		if (instance == null) {
			instance = new ListaTabelaPrecoDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ListaTabelaPreco listaTabelaPreco = new ListaTabelaPreco();
		listaTabelaPreco.rowKey = rs.getString("rowkey");
		listaTabelaPreco.cdEmpresa = rs.getString("cdEmpresa");
		listaTabelaPreco.cdRepresentante = rs.getString("cdRepresentante");
		listaTabelaPreco.cdListaTabelaPreco = rs.getInt("cdListaTabelaPreco");
		listaTabelaPreco.dsListaTabelaPreco = rs.getString("dsListaTabelaPreco");
		return listaTabelaPreco;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,")
		.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" CDLISTATABELAPRECO,")
		.append(" DSLISTATABELAPRECO");
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
		ListaTabelaPreco listaTabelaPreco = (ListaTabelaPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", listaTabelaPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", listaTabelaPreco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDLISTATABELAPRECO = ", listaTabelaPreco.cdListaTabelaPreco);
		sql.append(sqlWhereClause.getSql());
	}

}
