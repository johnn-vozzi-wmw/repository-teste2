package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemKitAgrSimilar;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class ItemKitAgrSimilarDao extends CrudDbxDao {

	private static ItemKitAgrSimilarDao instance;

	public ItemKitAgrSimilarDao() { super(ItemKitAgrSimilar.TABLE_NAME); }
	
	public static ItemKitAgrSimilarDao getInstance() {
		return instance == null ? instance = new ItemKitAgrSimilarDao() : instance;
	}

	@Override protected void addInsertColumns(StringBuffer sql) throws SQLException {}
	@Override protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}
	@Override protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemKitAgrSimilar itemKitAgrSimilar = new ItemKitAgrSimilar();
		itemKitAgrSimilar.rowKey = rs.getString("rowkey");
		itemKitAgrSimilar.cdEmpresa = rs.getString("cdEmpresa");
		itemKitAgrSimilar.cdRepresentante = rs.getString("cdRepresentante");
		itemKitAgrSimilar.cdKit = rs.getString("cdKit");
		itemKitAgrSimilar.cdProduto = rs.getString("cdProduto");
		itemKitAgrSimilar.cdTabelaPreco = rs.getString("cdTabelaPreco");
		itemKitAgrSimilar.cdProdutoSimilar = rs.getString("cdProdutoSimilar");
		return itemKitAgrSimilar;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDKIT,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.CDPRODUTOSIMILAR");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemKitAgrSimilar itemKitAgrSimilar = (ItemKitAgrSimilar) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemKitAgrSimilar.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemKitAgrSimilar.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDKIT = ", itemKitAgrSimilar.cdKit);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemKitAgrSimilar.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", itemKitAgrSimilar.cdTabelaPreco);
		sqlWhereClause.addAndCondition("tb.CDPRODUTOSIMILAR = ", itemKitAgrSimilar.cdProdutoSimilar);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemKitAgrSimilar();
	}

}