package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemNfeEstoque;
import totalcross.sql.ResultSet;

public class ItemNfeEstoqueDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemNfeEstoque();
	}

	private static ItemNfeEstoqueDao instance;
	
	public static ItemNfeEstoqueDao getInstance() {
		if (instance == null) {
			instance = new ItemNfeEstoqueDao();
		}
		return instance;
	}

	public ItemNfeEstoqueDao() {
		super(ItemNfeEstoque.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemNfeEstoque itemNfeEstoque = new ItemNfeEstoque();
		itemNfeEstoque.cdEmpresa = rs.getString("cdEmpresa");
		itemNfeEstoque.cdRepresentante = rs.getString("cdRepresentante");
		itemNfeEstoque.nuNotaRemessa = rs.getString("nuNotaRemessa");
		itemNfeEstoque.nuSerieRemessa = rs.getString("nuSerieRemessa");
		itemNfeEstoque.cdProduto = rs.getString("cdProduto");
		itemNfeEstoque.cdUnidade = rs.getString("cdUnidade");
		itemNfeEstoque.qtItem = rs.getDouble("qtItem");
		itemNfeEstoque.cdClassificFiscal = rs.getString("cdClassificFiscal");
		itemNfeEstoque.vlItem = rs.getDouble("vlItem");
		itemNfeEstoque.vlTotalItem = rs.getDouble("vlTotalItem");
		itemNfeEstoque.vlTotalIcms = rs.getDouble("vlTotalIcms");
		itemNfeEstoque.vlTotalSt = rs.getDouble("vlTotalSt");
		itemNfeEstoque.vlPctIcms = rs.getDouble("vlPctIcms");
		itemNfeEstoque.vlPctPis = rs.getDouble("vlPctPis");
		itemNfeEstoque.vlPctCofins = rs.getDouble("vlPctCofins");
		itemNfeEstoque.qtPeso = rs.getDouble("qtPeso");
		itemNfeEstoque.dsProduto = rs.getString("dsProduto");
		return itemNfeEstoque;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.CDEMPRESA,")
		.append(" tb.CDREPRESENTANTE,")
		.append(" tb.NUNOTAREMESSA,")
		.append(" tb.NUSERIEREMESSA,")
		.append(" tb.CDPRODUTO,")
		.append(" tb.CDUNIDADE,")
		.append(" tb.QTITEM,")
		.append(" tb.CDCLASSIFICFISCAL,")
		.append(" tb.VLITEM,")
		.append(" tb.VLTOTALITEM,")
		.append(" tb.VLTOTALICMS,")
		.append(" tb.VLTOTALST,")
		.append(" tb.VLPCTICMS,")
		.append(" tb.VLPCTPIS,")
		.append(" tb.VLPCTCOFINS,")
		.append(" tb.QTPESO,")
		.append(" prod.DSPRODUTO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" NUNOTAREMESSA,")
		.append(" NUSERIEREMESSA,")
		.append(" CDPRODUTO,")
		.append(" CDUNIDADE,")
		.append(" QTITEM,")
		.append(" CDCLASSIFICFISCAL,")
		.append(" VLITEM,")
		.append(" VLTOTALITEM,")
		.append(" VLTOTALICMS,")
		.append(" VLTOTALST,")
		.append(" VLPCTICMS,")
		.append(" VLPCTPIS,")
		.append(" VLPCTCOFINS,")
		.append(" QTPESO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemNfeEstoque itemNfeEstoque = (ItemNfeEstoque) domain;
		sql.append(Sql.getValue(itemNfeEstoque.cdEmpresa)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.cdRepresentante)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.nuNotaRemessa)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.nuSerieRemessa)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.cdProduto)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.cdUnidade)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.qtItem)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.cdClassificFiscal)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlItem)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlTotalItem)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlTotalIcms)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlTotalSt)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlPctIcms)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlPctPis)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.vlPctCofins)).append(", ")
		.append(Sql.getValue(itemNfeEstoque.qtPeso));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemNfeEstoque itemNfeEstoque = (ItemNfeEstoque) domain;
		sql.append("CDUNIDADE = ").append(Sql.getValue(itemNfeEstoque.cdUnidade)).append(", ")
		.append("QTITEM = ").append(Sql.getValue(itemNfeEstoque.qtItem)).append(", ")
		.append("CDCLASSIFICFISCAL = ").append(Sql.getValue(itemNfeEstoque.cdClassificFiscal)).append(", ")
		.append("VLITEM = ").append(Sql.getValue(itemNfeEstoque.vlItem)).append(", ")
		.append("VLTOTALITEM = ").append(Sql.getValue(itemNfeEstoque.vlTotalItem)).append(", ")
		.append("VLTOTALICMS = ").append(Sql.getValue(itemNfeEstoque.vlTotalIcms)).append(", ")
		.append("VLTOTALST = ").append(Sql.getValue(itemNfeEstoque.vlTotalSt)).append(", ")
		.append("VLPCTICMS = ").append(Sql.getValue(itemNfeEstoque.vlPctIcms)).append(", ")
		.append("VLPCTPIS = ").append(Sql.getValue(itemNfeEstoque.vlPctPis)).append(", ")
		.append("VLPCTCOFINS = ").append(Sql.getValue(itemNfeEstoque.vlPctCofins)).append(", ")
		.append("QTPESO = ").append(Sql.getValue(itemNfeEstoque.qtPeso));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemNfeEstoque itemNfeEstoque = (ItemNfeEstoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemNfeEstoque.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemNfeEstoque.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUNOTAREMESSA = ", itemNfeEstoque.nuNotaRemessa);
		sqlWhereClause.addAndCondition("tb.NUSERIEREMESSA = ", itemNfeEstoque.nuSerieRemessa);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemNfeEstoque.cdProduto);
		sql.append(sqlWhereClause.getSql());
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPPRODUTO prod ON ")
		.append(" tb.CDEMPRESA = prod.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = prod.CDPRODUTO ");
	}


}
