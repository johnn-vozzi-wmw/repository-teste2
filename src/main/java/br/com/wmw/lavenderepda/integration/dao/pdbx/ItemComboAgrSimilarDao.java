package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemComboAgrSimilar;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class ItemComboAgrSimilarDao extends CrudDbxDao {

	private static ItemComboAgrSimilarDao instance;

	public ItemComboAgrSimilarDao() { super(ItemComboAgrSimilar.TABLE_NAME); }
	
	public static ItemComboAgrSimilarDao getInstance() {
		return instance == null ? instance = new ItemComboAgrSimilarDao() : instance;
	}

	@Override protected void addInsertColumns(StringBuffer sql) throws SQLException {}
	@Override protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}
	@Override protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemComboAgrSimilar itemComboAgrSimilar = new ItemComboAgrSimilar();
		itemComboAgrSimilar.rowKey = rs.getString("rowkey");
		itemComboAgrSimilar.cdEmpresa = rs.getString("cdEmpresa");
		itemComboAgrSimilar.cdRepresentante = rs.getString("cdRepresentante");
		itemComboAgrSimilar.cdCombo = rs.getString("cdCombo");
		itemComboAgrSimilar.cdProduto = rs.getString("cdProduto");
		itemComboAgrSimilar.cdTabelaPreco = rs.getString("cdTabelaPreco");
		itemComboAgrSimilar.cdProdutoSimilar = rs.getString("cdProdutoSimilar");
		itemComboAgrSimilar.flTipoItemCombo = rs.getString("flTipoItemCombo");
		return itemComboAgrSimilar;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDCOMBO,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.CDPRODUTOSIMILAR,");
		sql.append(" tb.FLTIPOITEMCOMBO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemComboAgrSimilar itemComboAgrSimilar = (ItemComboAgrSimilar) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemComboAgrSimilar.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemComboAgrSimilar.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCOMBO = ", itemComboAgrSimilar.cdCombo);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemComboAgrSimilar.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", itemComboAgrSimilar.cdTabelaPreco);
		sqlWhereClause.addAndCondition("tb.CDPRODUTOSIMILAR = ", itemComboAgrSimilar.cdProdutoSimilar);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMCOMBO = ", itemComboAgrSimilar.flTipoItemCombo);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemComboAgrSimilar();
	}

}