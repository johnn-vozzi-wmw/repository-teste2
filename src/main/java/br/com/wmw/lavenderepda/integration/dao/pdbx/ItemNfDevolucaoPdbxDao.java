
package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemNfDevolucao;
import totalcross.sql.ResultSet;

public class ItemNfDevolucaoPdbxDao extends CrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemNfDevolucao();
	}

	private static ItemNfDevolucaoPdbxDao instance;
	
	public static ItemNfDevolucaoPdbxDao getInstance() {
		if (instance == null) {
			instance = new ItemNfDevolucaoPdbxDao();
		}
		return instance;
	}

	public ItemNfDevolucaoPdbxDao(String nomeTabela) {
		super(nomeTabela);
	}

	public ItemNfDevolucaoPdbxDao() {
		this(null);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemNfDevolucao itemNfDevolucao = new ItemNfDevolucao();
		itemNfDevolucao.cdEmpresa = rs.getString("CDEMPRESA");
		itemNfDevolucao.nuNfDevolucao = rs.getString("NUNFDEVOLUCAO");
		itemNfDevolucao.cdSerie = rs.getString("CDSERIE");
		itemNfDevolucao.cdProduto = rs.getString("CDPRODUTO");
		itemNfDevolucao.nuSequenciaItem = rs.getInt("NUSEQUENCIAITEM");
		itemNfDevolucao.cdUnidade = rs.getString("CDUNIDADE");
		itemNfDevolucao.qtItem = rs.getDouble("QTITEM");
		itemNfDevolucao.vlUnitItem = rs.getDouble("VLUNITARIO");
		itemNfDevolucao.vlTotalItem = rs.getDouble("VLTOTALITEM");
		itemNfDevolucao.cdRepresentante = rs.getString("CDREPRESENTANTE");
		itemNfDevolucao.nuPedido = rs.getString("NUPEDIDO");
		itemNfDevolucao.flOrigemPedido = rs.getString("FLORIGEMPEDIDO");
		return itemNfDevolucao;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append("rowkey, ").append("CDEMPRESA, ").append("NUNFDEVOLUCAO, ").append("CDSERIE, ").append("CDPRODUTO, ")
				.append("NUSEQUENCIAITEM, ").append("CDUNIDADE, ").append("QTITEM, ").append("VLUNITARIO, ")
				.append("VLTOTALITEM, ").append("NUPEDIDO, ").append("CDREPRESENTANTE, ").append("FLORIGEMPEDIDO");
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
		ItemNfDevolucao itemNfDevolucao = (ItemNfDevolucao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemNfDevolucao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemNfDevolucao.cdRepresentante);
		sqlWhereClause.addAndCondition("NUNFDEVOLUCAO = ", itemNfDevolucao.nuNfDevolucao);
		sqlWhereClause.addAndCondition("CDSERIE = ", itemNfDevolucao.cdSerie);
		sql.append(sqlWhereClause.getSql());

	}

}
