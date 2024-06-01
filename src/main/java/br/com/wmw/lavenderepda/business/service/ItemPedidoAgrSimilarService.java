package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoAgrSimilarDao;
import totalcross.util.Vector;

public class ItemPedidoAgrSimilarService extends CrudService {
	
	private static ItemPedidoAgrSimilarService instance;
	
	public static ItemPedidoAgrSimilarService getInstance() {
		if (instance == null) {
			instance = new ItemPedidoAgrSimilarService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return ItemPedidoAgrSimilarDao.getInstance();
	}
	
	public void insereItensPedidoAgrSimilar(ItemPedido itemPedido) throws SQLException {
		Vector itemAgrSimilarList = beforeInsertOrUpdateItemPedidoAgrSimilar(itemPedido, false);
		int size = itemAgrSimilarList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) itemAgrSimilarList.items[i];
			insert(item);
		}
		if (itemPedido.pedido != null) {
			itemPedido.pedido.setItemPedidoAgrSimilares(null);
		}
	}
	
	public void updateItensPedidoAgrSimilar(ItemPedido itemPedido) throws SQLException {
		Vector itemAgrSimilarList = beforeInsertOrUpdateItemPedidoAgrSimilar(itemPedido, true);
		int size = itemAgrSimilarList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) itemAgrSimilarList.items[i];
			if (countByExample(item) > 0) {
				if (item.qtItemFisico == 0d) delete(item);
				else update(item);
			} else if (item.qtItemFisico > 0d) {
				insert(item);
			}
		}
	}

	private Vector beforeInsertOrUpdateItemPedidoAgrSimilar(ItemPedido itemPedido, boolean updating) throws SQLException {
		Vector similaresList = itemPedido.getProduto().similaresList;
		int size = similaresList.size();
		Vector itemAgrSimilarList = new Vector(size);
		double oldQtItemFisico = itemPedido.getQtItemFisico();
		Produto produtoItemPedido = null;
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto) similaresList.items[i];
			if (ValueUtil.valueEquals(itemPedido.cdProduto, produto.cdProduto)) {
				produtoItemPedido = produto;
			} else if (produto.qtItemPedido > 0 || updating) {
				ItemPedidoAgrSimilar item = getItemPedidoAgrSimilar(itemPedido);
				item.cdProdutoSimilar = produto.cdProduto;
				item.qtItemFisico = produto.qtItemPedido;
				item.flTipoAlteracao = !updating ? BaseDomain.FLTIPOALTERACAO_INSERIDO : BaseDomain.FLTIPOALTERACAO_ALTERADO;
				validateEstoqueItemSimilar(item, itemPedido.pedido);
				itemAgrSimilarList.addElement(item);
			}
		}
		if (produtoItemPedido != null) {
			itemPedido.setQtItemFisico(produtoItemPedido.qtItemPedido);
			try {
				EstoqueService.getInstance().validateEstoque(itemPedido.pedido, itemPedido, true);
				ItemPedidoService.getInstance().updateQtItemFisicoSimilar(itemPedido, produtoItemPedido.qtItemPedido);
			} catch (ValidationException e) {
				itemPedido.setQtItemFisico(oldQtItemFisico);
				throw e;
			} finally {
				if (itemPedido.pedido != null) {
					itemPedido.pedido.setItemPedidoAgrSimilares(null);
				}
			}
		}
		return itemAgrSimilarList;
	}

	private ItemPedidoAgrSimilar getItemPedidoAgrSimilar(ItemPedido itemPedido) throws SQLException {
		ItemPedidoAgrSimilar item = new ItemPedidoAgrSimilar();
		item.cdEmpresa = itemPedido.cdEmpresa;
		item.cdRepresentante = itemPedido.cdRepresentante;
		item.flOrigemPedido = itemPedido.flOrigemPedido;
		item.nuPedido = itemPedido.nuPedido;
		item.cdProduto = itemPedido.cdProduto;
		item.flTipoItemPedido = itemPedido.flTipoItemPedido;
		item.nuSeqProduto = itemPedido.nuSeqProduto;
		item.cdAgrupadorSimilaridade = itemPedido.getProduto().cdAgrupadorSimilaridade;
		return item;
	}
	
	public void deleteItensPedidoAgrSimilar(ItemPedido itemPedido) throws SQLException {
		deleteAllByExample(getItemPedidoAgrSimilar(itemPedido));
	}
	
	public void deleteItensPedidoAgrSimilar(Pedido pedido) throws SQLException {
		deleteAllByExample(getItemPedidoAgrSimilar(pedido));
	}

	private ItemPedidoAgrSimilar getItemPedidoAgrSimilar(Pedido pedido) {
		ItemPedidoAgrSimilar item = new ItemPedidoAgrSimilar();
		if (pedido == null) return item;
		item.cdEmpresa = pedido.cdEmpresa;
		item.cdRepresentante = pedido.cdRepresentante;
		item.flOrigemPedido = pedido.flOrigemPedido;
		item.nuPedido = pedido.nuPedido;
		return item;
	}
	
	private void validateEstoqueItemSimilar(ItemPedidoAgrSimilar itemPedidoAgrSimilar, Pedido pedido) throws SQLException {
		ItemPedido itemPedido = getItemPedidoFromSimilar(itemPedidoAgrSimilar, pedido);
		itemPedido.setQtItemFisico(itemPedidoAgrSimilar.qtItemFisico);
		EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
	}

	private ItemPedido getItemPedidoFromSimilar(ItemPedidoAgrSimilar itemPedidoAgrSimilar, Pedido pedido) {
		ItemPedido itemPedido = new ItemPedido(pedido);
		itemPedido.cdProduto = itemPedidoAgrSimilar.cdProdutoSimilar;
		itemPedido.flTipoItemPedido = itemPedidoAgrSimilar.flTipoItemPedido;
		itemPedido.pedido = pedido;
		return itemPedido;
	}
	
	public Vector findItemPedidoAgrSimilarList(ItemPedido itemPedido) throws SQLException {
		return findAllByExample(getItemPedidoAgrSimilar(itemPedido));
	}
	
	public Vector findItemPedidoAgrSimilarList(Pedido pedido) throws SQLException {
		return findAllByExample(getItemPedidoAgrSimilar(pedido));
	}
	
	public Vector findItensAgrSimilarForItemPedidoList(Pedido pedido, Vector itemPedidoList) throws SQLException {
		Vector itensAgrSimiarList = findItemPedidoAgrSimilarList(pedido);
		int size = itensAgrSimiarList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoAgrSimilar itemSimilar = (ItemPedidoAgrSimilar) itensAgrSimiarList.items[i];
			ItemPedido itemPedido = getItemPedidoFromSimilar(itemSimilar, pedido);
			itemPedido.setQtItemFisico(itemSimilar.qtItemFisico);
			itemPedido.cdProdutoSimilarOrg = itemSimilar.cdProduto;
			itemPedido.isItemSimilar = true;
			itemPedido.nuSeqProduto = itemSimilar.nuSeqProduto;
			itemPedidoList.addElement(itemPedido);
		}
		return itemPedidoList;
	}
	
	public boolean isItemPedidoInseridoSimilarAutorizado(ItemPedido itemPedido) throws SQLException {
		ItemPedidoAgrSimilar filter = getItemPedidoAgrSimilar(itemPedido);
		filter.cdProduto = null;
		filter.cdProdutoSimilar = itemPedido.cdProduto;
		return countByExample(filter) > 0;
	}
	
	public Vector filtraProdutosSimilaresInseridosAgrupadorSimilaridade(Vector listProdutoSimilar, Pedido pedido) throws SQLException {
		Vector listItemPedidoAgrSimilar = findItemPedidoAgrSimilarList(pedido);
		int size = listProdutoSimilar.size();
		int sizeAgrSimilar = listItemPedidoAgrSimilar.size();
		Vector listProdutoSimilarFiltrada = new Vector(size);
		for (int i = 0; i < size; i++) {
			ProdutoSimilar produtoSimilar = (ProdutoSimilar) listProdutoSimilar.items[i];
			if (!isItemAgrSimilarContidoProdutoSimilarList(listItemPedidoAgrSimilar, produtoSimilar.cdProdutoSimilar, sizeAgrSimilar)) {
				listProdutoSimilarFiltrada.addElement(produtoSimilar);
			}
		}
		return listProdutoSimilarFiltrada;
	}
	
	private boolean isItemAgrSimilarContidoProdutoSimilarList(Vector listItemPedidoAgrSimilar, String cdProduto, int size) {
		for (int i = 0; i < size; i++) {
			ItemPedidoAgrSimilar itemPedidoAgrSimilar = (ItemPedidoAgrSimilar) listItemPedidoAgrSimilar.items[i]; 
			if (ValueUtil.valueEquals(cdProduto, itemPedidoAgrSimilar.cdProdutoSimilar)) {
				return true;
			}
		}
		return false;
	}
	
	public int isPedidoPossuiItensComboAvulsos(Pedido pedido, String cdCombo, Vector listItensSecundarioSelecionados, Vector listItensSelecionados) throws SQLException {
		ItemPedidoAgrSimilar filter = getItemPedidoAgrSimilar(pedido);
		filter.pedido = pedido;
		return ItemPedidoAgrSimilarDao.getInstance().isPedidoPossuiItensComboAvulsos(filter, cdCombo, false, listItensSecundarioSelecionados, listItensSelecionados);
	}

}
