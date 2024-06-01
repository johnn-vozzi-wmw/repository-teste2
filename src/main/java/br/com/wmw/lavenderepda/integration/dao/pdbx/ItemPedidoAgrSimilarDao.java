package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemPedidoAgrSimilarDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoAgrSimilar();
	}

	private static ItemPedidoAgrSimilarDao instance;

	public ItemPedidoAgrSimilarDao() {
		super(ItemPedidoAgrSimilar.TABLE_NAME);
	}
	
	public static ItemPedidoAgrSimilarDao getInstance() {
		if (instance == null) {
			instance = new ItemPedidoAgrSimilarDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemPedidoAgrSimilar itemPedidoAgrSimilar = new ItemPedidoAgrSimilar();
		itemPedidoAgrSimilar.rowKey = rs.getString("rowkey");
		itemPedidoAgrSimilar.cdEmpresa = rs.getString("cdEmpresa");
		itemPedidoAgrSimilar.cdRepresentante = rs.getString("cdRepresentante");
		itemPedidoAgrSimilar.flOrigemPedido = rs.getString("flOrigemPedido");
		itemPedidoAgrSimilar.nuPedido = rs.getString("nuPedido");
		itemPedidoAgrSimilar.cdProduto = rs.getString("cdProduto");
		itemPedidoAgrSimilar.cdProdutoSimilar = rs.getString("cdProdutoSimilar");
		itemPedidoAgrSimilar.flTipoItemPedido = rs.getString("flTipoItemPedido");
		itemPedidoAgrSimilar.nuSeqProduto = rs.getInt("nuSeqProduto");
		itemPedidoAgrSimilar.cdAgrupadorSimilaridade = rs.getString("cdAgrupadorSimilaridade");
		itemPedidoAgrSimilar.qtItemFisico = rs.getDouble("qtItemFisico");
		itemPedidoAgrSimilar.vlItemPedido = rs.getDouble("vlItemPedido");
		itemPedidoAgrSimilar.vlTotalItemPedido = rs.getDouble("vlTotalItemPedido");
		itemPedidoAgrSimilar.dsProdutoSimilar = rs.getString("dsProdutoSimilar");
		return itemPedidoAgrSimilar;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("tb.rowKey,")
		.append("tb.CDEMPRESA,")
		.append("tb.CDREPRESENTANTE,")
		.append("tb.FLORIGEMPEDIDO,")
		.append("tb.NUPEDIDO,")
		.append("tb.CDPRODUTO,")
		.append("tb.CDPRODUTOSIMILAR,")
		.append("tb.FLTIPOITEMPEDIDO,")
		.append("tb.NUSEQPRODUTO,")
		.append("tb.CDAGRUPADORSIMILARIDADE,")
		.append("tb.QTITEMFISICO,")
		.append("item.VLITEMPEDIDO,")
		.append("item.VLITEMPEDIDO * tb.QTITEMFISICO AS VLTOTALITEMPEDIDO,")
		.append("produto.DSPRODUTO AS DSPRODUTOSIMILAR");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append("CDEMPRESA,")
		.append("CDREPRESENTANTE,")
		.append("FLORIGEMPEDIDO,")
		.append("NUPEDIDO,")
		.append("CDPRODUTO,")
		.append("CDPRODUTOSIMILAR,")
		.append("FLTIPOITEMPEDIDO,")
		.append("NUSEQPRODUTO,")
		.append("CDAGRUPADORSIMILARIDADE,")
		.append("QTITEMFISICO,")
		.append("NUCARIMBO,")
		.append("FLTIPOALTERACAO,")
		.append("CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) domain;
		sql.append(Sql.getValue(item.cdEmpresa)).append(",")
		.append(Sql.getValue(item.cdRepresentante)).append(",")
		.append(Sql.getValue(item.flOrigemPedido)).append(",")
		.append(Sql.getValue(item.nuPedido)).append(",")
		.append(Sql.getValue(item.cdProduto)).append(",")
		.append(Sql.getValue(item.cdProdutoSimilar)).append(",")
		.append(Sql.getValue(item.flTipoItemPedido)).append(",")
		.append(Sql.getValue(item.nuSeqProduto)).append(",")
		.append(Sql.getValue(item.cdAgrupadorSimilaridade)).append(",")
		.append(Sql.getValue(item.qtItemFisico)).append(",")
		.append(Sql.getValue(item.nuCarimbo)).append(",")
		.append(Sql.getValue(item.flTipoAlteracao)).append(",")
		.append(Sql.getValue(item.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) domain;
		sql.append(" CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(item.cdAgrupadorSimilaridade)).append(",")
		.append(" QTITEMFISICO = ").append(Sql.getValue(item.qtItemFisico)).append(",")
		.append(" NUCARIMBO = ").append(Sql.getValue(item.nuCarimbo)).append(",")
		.append(" FLTIPOALTERACAO = ").append(Sql.getValue(item.flTipoAlteracao)).append(",")
		.append(" CDUSUARIO = ").append(Sql.getValue(item.cdUsuario));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", item.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", item.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", item.flOrigemPedido);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", item.nuPedido);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", item.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDPRODUTOSIMILAR = ", item.cdProdutoSimilar);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMPEDIDO = ", item.flTipoItemPedido);
		sqlWhereClause.addAndCondition("tb.NUSEQPRODUTO = ", item.nuSeqProduto);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPITEMPEDIDO item ON ")
		.append(" item.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" item.FLORIGEMPEDIDO = tb.FLORIGEMPEDIDO AND ")
		.append(" item.NUPEDIDO = tb.NUPEDIDO AND ")
		.append(" item.CDPRODUTO = tb.CDPRODUTO AND ")
		.append(" item.FLTIPOITEMPEDIDO = tb.FLTIPOITEMPEDIDO AND ")
		.append(" item.NUSEQPRODUTO = tb.NUSEQPRODUTO")
		.append(" JOIN TBLVPPRODUTO produto ON ")
		.append(" produto.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" produto.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" produto.CDPRODUTO = tb.CDPRODUTOSIMILAR");
	}
	
	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", item.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", item.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", item.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", item.nuPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", item.cdProduto);
		sqlWhereClause.addAndCondition("CDPRODUTOSIMILAR = ", item.cdProdutoSimilar);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", item.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", item.nuSeqProduto);
		sql.append(sqlWhereClause.getSql());
	}
	
	public int isPedidoPossuiItensComboAvulsos(ItemPedidoAgrSimilar itemPedido, String cdCombo, boolean beforeDelete, Vector listItensSecundarioSelecionados, Vector listItensSimilares) throws SQLException {
		StringBuffer sql = getSqlItemComboNoPedido(itemPedido, cdCombo, true, beforeDelete, listItensSecundarioSelecionados, LavenderePdaConfig.usaAgrupadorSimilaridadeProduto);
		if (ValueUtil.isNotEmpty(listItensSimilares)) {
			addNotInSimilares(sql, listItensSimilares);
		}
		int tipoRetorno = ItemCombo.TIPO_SEM_AVULSO;
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next() && ValueUtil.isNotEmpty(rs.getString(1))) {
				tipoRetorno = ItemCombo.TIPO_ENCONTROU_AVULSO;
			}
		}
		return tipoRetorno;
	}
	
	private StringBuffer getSqlItemComboNoPedido(ItemPedidoAgrSimilar itemPedido, String cdCombo, boolean avulso, boolean beforeDelete, Vector listItensSecundarioSelecionados, boolean usaItemComboSimilar) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ").append("tb.CDPRODUTO").append(" FROM TBLVPITEMPEDIDOAGRSIMILAR tb");
		String cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		if (usaItemComboSimilar) {
			sql.append(" LEFT JOIN TBLVPITEMCOMBOAGRSIMILAR itcs ON ")
			.append(" ITCS.CDEMPRESA = tb.CDEMPRESA ")
			.append(" AND ITCS.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
			if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
				sql.append(" AND ITCS.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
			}
			sql.append(" AND ITCS.CDPRODUTOSIMILAR = tb.CDPRODUTOSIMILAR ")
			.append(" AND ITCS.CDCOMBO = ").append(Sql.getValue(cdCombo));
		}
		sql.append(" JOIN TBLVPITEMCOMBO ITC ON")
		.append(" ITC.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" ITC.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
		.append(" (ITC.CDPRODUTO = tb.CDPRODUTOSIMILAR OR ITCS.CDPRODUTO = ITC.CDPRODUTO)");
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			sql.append(" AND ITC.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
		}
		if (!avulso) {
			sql.append(" AND ITC.FLTIPOITEMCOMBO = 'S'");
		}
		sql.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND")
		.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND")
		.append(" tb.NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND")
		.append(" tb.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND")
		.append(" ITC.CDCOMBO = ").append(Sql.getValue(cdCombo)).append(" AND")
		.append(" (ITC.FLTIPOITEMCOMBO = 'P' ");
		if (ValueUtil.isNotEmpty(listItensSecundarioSelecionados)) {
			sql.append(" OR (ITC.FLTIPOITEMCOMBO = 'S' AND ITC.CDPRODUTO = ").append(Sql.getValue(((ItemCombo)listItensSecundarioSelecionados.items[0]).cdProduto)).append(")");
		}
		sql.append(")");
		if (beforeDelete) {
			sql.append(" AND tb.CDPRODUTO <> ").append(Sql.getValue(itemPedido.cdProduto));
		}
		return sql;
	}
	
	private void addNotInSimilares(StringBuffer sql, Vector listItensSimilares) {
		sql.append(" AND tb.CDPRODUTOSIMILAR IN (");
		int size = listItensSimilares.size();
		for (int i = 0; i < size; i++) {
			sql.append(Sql.getValue(((Produto) listItensSimilares.items[i]).cdProduto)).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
	}

}
