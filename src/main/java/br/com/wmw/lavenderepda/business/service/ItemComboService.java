package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemComboDao;
import totalcross.util.Vector;

public class ItemComboService extends CrudService {
	
	private static ItemComboService instance;

	@Override
	public void validate(BaseDomain domain) throws SQLException {}

	@Override
	protected CrudDao getCrudDao() {
		return ItemComboDao.getInstance();
	}
	
	public static ItemComboService getInstance() {
		if (instance == null) {
			instance = new ItemComboService();
		}
		return instance;
	}
	
	public Vector findProdutosSugeridosByCombo(Pedido pedido, String cdProdutoSugestaoCombo, String cdCombo, String flTipoItemCombo, boolean isFechandoPedidoSugestaoCombo) throws SQLException {
		Produto filter = prepareProdutoFilterForCombo(pedido);
		ItemCombo itemCombo = getItemComboComCdProduto(pedido.cdEmpresa, pedido.cdRepresentante, cdProdutoSugestaoCombo);
		return ProdutoService.getInstance().findProdutosSugeridosCombo(filter, itemCombo, pedido.nuPedido, pedido.cdCliente, pedido.flOrigemPedido, cdCombo, flTipoItemCombo, isFechandoPedidoSugestaoCombo);
	}

	public Vector findProdutosSugeridosByComboSummary(Pedido pedido, String cdProdutoSugestaoCombo, String cdCombo, String flTipoItemCombo) throws SQLException {
		Produto filter = prepareProdutoFilterForCombo(pedido);
		ItemCombo itemCombo = getItemComboComCdProduto(pedido.cdEmpresa, pedido.cdRepresentante, cdProdutoSugestaoCombo);
		return ProdutoService.getInstance().findProdutosSugeridosComboSummary(filter, itemCombo, pedido.nuPedido, pedido.cdCliente, pedido.flOrigemPedido, cdCombo, flTipoItemCombo);
	}


	private Produto prepareProdutoFilterForCombo(Pedido pedido) throws SQLException {
		Produto filter = new Produto();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() || LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
			filter.estoque = new Estoque();
			filter.estoque.cdLocalEstoque = pedido.getCdLocalEstoque();
		}
		if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
			filter.itemTabelaPreco = new ItemTabelaPreco();
			filter.itemTabelaPreco.cdTabelaPreco = pedido.cdTabelaPreco;
			if (LavenderePdaConfig.usaPrecoPorUf) {
				filter.itemTabelaPreco.cdUf = pedido.getCliente().dsUfPreco;
			} else {
				filter.itemTabelaPreco.cdUf = ValueUtil.VALOR_ZERO;
			}
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			filter.findListCombo = true;
			filter.itemTabelaPreco = new ItemTabelaPreco();
			filter.itemTabelaPreco.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
			filter.cdTabelaPreco = filter.itemTabelaPreco.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(pedido);
		}
		return filter;
	}

	public ItemCombo getItemComboComCdProduto(String cdEmpresa, String cdRepresentante, String cdProdutoSugestaoCombo) {
		ItemCombo itemCombo = new ItemCombo();
		itemCombo.cdEmpresa = cdEmpresa;
		itemCombo.cdRepresentante = cdRepresentante;
		itemCombo.cdProduto = cdProdutoSugestaoCombo;
		return itemCombo;
	}
	
	public boolean isItemPedidoPertenceCombo(ItemPedido itemPedido, String cdProduto, String flTipoItemCombo, String cdCombo) throws SQLException {
		itemPedido.cdProduto = ValueUtil.isEmpty(itemPedido.cdProduto) && LavenderePdaConfig.isUsaSugestaoComboAposInsercao() ? cdProduto : itemPedido.cdProduto;
		ItemCombo filter = getItemComboFilter(itemPedido);
		filter.flTipoItemCombo = flTipoItemCombo;
		boolean pertenceCombo = ItemComboDao.getInstance().countItemComboVigente(filter) > 0;
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && !pertenceCombo) {
			pertenceCombo = ItemComboDao.getInstance().countItemComboSimilarVigente(filter) > 0;
		}
		return pertenceCombo;
	}
	
	public boolean isItemComboPossuiVlUnitario(ItemPedido itemPedido) throws SQLException {
		ItemCombo filter = getItemComboFilter(itemPedido);
		ItemCombo itemCombo = (ItemCombo) findByPrimaryKey(filter);
		return ItemCombo.TIPOITEMCOMBO_PRIMARIO.equals(itemCombo.flTipoItemCombo) && itemCombo.vlUnitarioCombo > 0d;
	}

	public ItemCombo getItemComboFilter(ItemPedido itemPedido) throws SQLException {
		ItemCombo filter = new ItemCombo();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdRepresentante = itemPedido.cdRepresentante;
		filter.cdProduto = itemPedido.cdProduto;
		filter.cdCombo = itemPedido.cdCombo;
		if (LavenderePdaConfig.isUsaSugestaoComboAposInsercaoEFechamento()) {
			filter.cdTabelaPreco = ValueUtil.VALOR_ZERO;
		} else {
			filter.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		}
		filter.cdAgrupadorSimilaridade = itemPedido.getProduto().cdAgrupadorSimilaridade;
		return filter;
	}
	
	public Vector findItemComboVencidoList(Pedido pedido) throws SQLException {
		ItemCombo filter = new ItemCombo();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(pedido);
		filter.nuPedido = pedido.nuPedido;
		filter.flOrigemPedido = pedido.flOrigemPedido;
		return ItemComboDao.getInstance().findItemComboVencidoList(filter);
	}

	public ItemCombo findByItemPedido(ItemPedido itemPedido) throws SQLException {
		ItemCombo filter = getItemComboFilter(itemPedido);
		ItemCombo itemCombo = (ItemCombo) findByPrimaryKey(filter);
		if (itemCombo == null && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			filter.buscandoSimilar = true;
			itemCombo = ItemComboDao.getInstance().findItemComboSimilar(filter);
		}
		return itemCombo;
	}
	
}
