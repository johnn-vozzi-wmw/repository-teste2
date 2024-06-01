package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemTituloFinan;
import totalcross.sql.ResultSet;

public class ItemTituloFinanPdbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemTituloFinan();
	}
	
	private static ItemTituloFinanPdbxDao instance;
	
	public static ItemTituloFinanPdbxDao getInstance() {
		if (instance == null) {
			instance = new ItemTituloFinanPdbxDao();
		}
		return instance;
	}
	
	public ItemTituloFinanPdbxDao(String newTableName) {
		super(newTableName);
	}
	
	public ItemTituloFinanPdbxDao() {
		this(null);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemTituloFinan itemTituloFinanceiro = new ItemTituloFinan();
		itemTituloFinanceiro.cdEmpresa = rs.getString("cdEmpresa");
		itemTituloFinanceiro.cdRepresentante = rs.getString("cdRepresentante");
		itemTituloFinanceiro.cdCliente = rs.getString("cdCliente");
		itemTituloFinanceiro.cdTitulo = rs.getString("cdTitulo");
		itemTituloFinanceiro.nuNF = rs.getString("nuNF");
		itemTituloFinanceiro.cdItem = rs.getString("cdItem");
		itemTituloFinanceiro.vlUnitItem = rs.getDouble("vlUnitItem");
		itemTituloFinanceiro.qtdItem = rs.getDouble("qtdItem");
		itemTituloFinanceiro.vlTotalItem = rs.getDouble("vlTotalItem");
		return itemTituloFinanceiro;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append("rowkey, ")
		.append("CDEMPRESA, ")
		.append("CDREPRESENTANTE, ")
		.append("CDCLIENTE, ")
		.append("CDTITULO, ")
		.append("NUNF, ")
		.append("CDITEM, ")
		.append("VLUNITITEM, ")
		.append("QTDITEM, ")
		.append("VLTOTALITEM");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		ItemTituloFinan itemTituloFinanceiro = (ItemTituloFinan) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemTituloFinanceiro.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemTituloFinanceiro.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", itemTituloFinanceiro.cdCliente);
		sqlWhereClause.addAndCondition("CDTITULO = ", itemTituloFinanceiro.cdTitulo);
		sqlWhereClause.addAndCondition("NUNF = ", itemTituloFinanceiro.nuNF);
		sqlWhereClause.addAndCondition("CDITEM = ", itemTituloFinanceiro.cdItem);
		sql.append(sqlWhereClause.getSql());
	}

}
