package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ProdutoIndustria;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class ProdutoIndustriaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoIndustria();
	}

	private static ProdutoIndustriaPdbxDao instance;

		public ProdutoIndustriaPdbxDao() {
		super(ProdutoIndustria.TABLE_NAME);
	}

	public static ProdutoIndustriaPdbxDao getInstance() {
		return instance == null ? instance = new ProdutoIndustriaPdbxDao() : instance;
	}

	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDPRODINDUSTRIA,");
		sql.append(" tb.CDREFPRODINDUSTRIA,");
		sql.append(" tb.CDSUGESTAOVENDA,");
		sql.append(" tb.DSPRODINDUSTRIA,");
		sql.append(" tb.DSREFPRODINDUSTRIA");
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sql.append(" ,tb.VLLITROSUGESTAO");
		}
	}

	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ProdutoIndustria produtoIndustria = new ProdutoIndustria();
		produtoIndustria.rowKey = rs.getString("rowkey");
		produtoIndustria.cdEmpresa = rs.getString("cdEmpresa");
		produtoIndustria.cdRepresentante = rs.getString("cdRepresentante");
		produtoIndustria.cdProduto = rs.getString("cdProduto");
		produtoIndustria.cdProdIndustria = rs.getString("cdProdIndustria");
		produtoIndustria.cdRefProdIndustria = rs.getString("cdRefProdIndustria");
		produtoIndustria.cdSugestaoVenda = rs.getString("cdSugestaoVenda");
		produtoIndustria.dsProdIndustria = rs.getString("dsProdIndustria");
		produtoIndustria.dsRefProdIndustria = rs.getString("dsRefProdIndustria");
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			produtoIndustria.vlLitroSugestao = rs.getDouble("vlLitroSugestao");
		}
		return produtoIndustria;
	}

	protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		ProdutoIndustria produtoIndustriaFilter = (ProdutoIndustria) domainFilter;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", produtoIndustriaFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", produtoIndustriaFilter.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.CDPRODUTO", produtoIndustriaFilter.cdProduto);
		sqlWhereClause.addAndConditionEquals("tb.CDPRODINDUSTRIA", produtoIndustriaFilter.cdProdIndustria);
		sqlWhereClause.addAndConditionEquals("tb.CDREFPRODINDUSTRIA", produtoIndustriaFilter.cdRefProdIndustria);
		sqlWhereClause.addAndConditionEquals("tb.CDSUGESTAOVENDA", produtoIndustriaFilter.cdSugestaoVenda);
		sqlWhereClause.addAndConditionIn("tb.CDSUGESTAOVENDA ", produtoIndustriaFilter.cdSugestaoVendaIn);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addGroupBy(StringBuffer sql) throws SQLException {
		sql.append(" GROUP BY tb.CDREFPRODINDUSTRIA ");
	}
	
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
	
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}
	
	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {}
	
	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}
	
	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

}