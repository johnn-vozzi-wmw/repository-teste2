package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.validation.RestricaoValidationException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ComboDao;
import totalcross.util.Vector;

public class ComboService extends CrudService {
	
	private static ComboService instance;
	
	public static ComboService getInstance() {
		if (instance == null) {
			instance = new ComboService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {}

	@Override
	protected CrudDao getCrudDao() {
		return ComboDao.getInstance();
	}
	
	public Vector findListComboByItemCombo(ItemCombo itemCombo, String nuPedido, String flOrigemPedido) throws SQLException {
		return ComboDao.getInstance().getListCombos(itemCombo, nuPedido, flOrigemPedido);
	}
	
	public Vector findListComboVigente(String cdEmpresa, String cdRepresentante, String cdCliente, String nuPedido, String cdTabelaPreco) throws SQLException {
		Combo filter = new Combo();
		filter.cdEmpresa = cdEmpresa;
		filter.cdRepresentante = cdRepresentante;
		filter.cdTabelaPreco = cdTabelaPreco;
		filter.dtVigenciaInicial = filter.dtVigenciaFinal = DateUtil.getCurrentDate();
		filter.nuPedido = nuPedido;
		Vector comboList = ComboDao.getInstance().findComboVigenteComEstoque(filter, cdCliente);
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			Vector comboListFinal = new Vector();
			int size = comboList.size();
			for (int i = 0; i < size; i++) {
				Combo combo = (Combo) comboList.items[i];
				if (!ComboDao.getInstance().containsGruposSemEstoque(combo)) {
					comboListFinal.addElement(combo);
				}
			}
			return comboListFinal;
		}
		return comboList;
	}
	
	public void insertItensCombo(Vector listItemComboSecundario, Pedido pedido, String cdTabelaPreco, String cdCombo, int qtdItem) throws SQLException {
		ItemCombo filterPrimario = getItemComboFilter(pedido, cdTabelaPreco, cdCombo);
		Vector listItensCombo = ItemComboService.getInstance().findAllByExample(filterPrimario);
		listItensCombo.addElementsNotNull(listItemComboSecundario.items);
		int size = listItensCombo.size();
		ArrayList<ItemPedido> itemPedidoList = new ArrayList<>(size);
		Vector itemPedidoListOld = new Vector();
		itemPedidoListOld.addElementsNotNull(pedido.itemPedidoList.items);
		String cdProduto = null;
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			for (int i = 0; i < size; i++) {
				ItemCombo itemCombo = (ItemCombo)listItensCombo.items[i];
				Produto filter = new Produto();
				cdProduto = itemCombo.cdProduto;
				ItemPedido itemPedido = getItemPedidoFromItemCombo(pedido, cdTabelaPreco, cdCombo, itemCombo, filter, qtdItem);
				itemPedidoList.add(itemPedido);
				EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
			}
			for (ItemPedido itemPedido : itemPedidoList) {
				ItemPedidoService.getInstance().saveItemSugestaoCombo(itemPedido, pedido);
			}
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
			pedido.itemPedidoList = itemPedidoListOld;
			resetQtItemFisico(itemPedidoList);
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
			if (e instanceof RestricaoValidationException) {
				throw new ValidationException(Messages.COMBO_RESTRICAO_VENDA_EXCEPTION);
			} else if (e instanceof ValidationException) {
				throw new ValidationException(e.getMessage() + " " + MessageUtil.getMessage(Messages.MSG_MOTIVO, ProdutoService.getInstance().getDsProduto(cdProduto)));
			}
			throw e;
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}
	
	public void insertItensComboSimilares(Vector itensInsercao, Pedido pedido, String cdTabelaPreco, String cdCombo, int qtdItem) throws SQLException {
		if (itensInsercao == null) return;
		int size = itensInsercao.size();
		ArrayList<ItemPedido> itemPedidoList = new ArrayList<>(size);
		Vector itemPedidoListOld = new Vector();
		itemPedidoListOld.addElementsNotNull(pedido.itemPedidoList.items);
		String cdProduto = null;
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			for (int i = 0; i < size; i++) {
				Produto produto = (Produto) itensInsercao.items[i];
				cdProduto = produto.cdProduto;
				ItemPedido itemPedido = getItemPedidoFromItemComboSimilar(pedido, cdTabelaPreco, cdCombo, produto, qtdItem);
				itemPedidoList.add(itemPedido);
				EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
			}
			for (ItemPedido itemPedido : itemPedidoList) {
				ItemPedidoService.getInstance().saveItemSugestaoCombo(itemPedido, pedido);
			}
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
			pedido.itemPedidoList = itemPedidoListOld;
			resetQtItemFisico(itemPedidoList);
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
			if (e instanceof RestricaoValidationException) {
				throw new ValidationException(Messages.COMBO_RESTRICAO_VENDA_EXCEPTION);
			} else if (e instanceof ValidationException) {
				throw new ValidationException(e.getMessage() + " " + MessageUtil.getMessage(Messages.MSG_MOTIVO, ProdutoService.getInstance().getDsProduto(cdProduto)));
			}
			throw e;
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}

	private void resetQtItemFisico(ArrayList<ItemPedido> itemPedidoList) throws SQLException {
		for (ItemPedido itemPedido : itemPedidoList) {
			itemPedido.setQtItemFisico(ValueUtil.getDoubleSimpleValue(ItemPedidoService.getInstance().findColumnByRowKey(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_QTITEMFISICO)));
			itemPedido.itemComboInserido = false;
		}
	}

	private ItemPedido getItemPedidoFromItemCombo(Pedido pedido, String cdTabelaPreco, String cdCombo, ItemCombo itemCombo, Produto filter, int qtdItem) throws SQLException {
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.cdProduto = itemCombo.cdProduto;
		return ItemPedidoService.getInstance().inicializaItemPedidoVenda(pedido, (Produto) ProdutoService.getInstance().findByPrimaryKey(filter), cdTabelaPreco, itemCombo.qtItemCombo * qtdItem, cdCombo);
	}

	public void loadValorItemByItemCombo(Pedido pedido, ItemPedido itemPedido, ItemCombo itemCombo) throws SQLException {
		if (itemCombo == null) return;
		itemPedido.vlBaseItemPedido = itemCombo.vlUnitarioCombo;
		itemPedido.vlItemPedido = itemCombo.vlUnitarioCombo;
		ItemPedidoService.getInstance().applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		ItemPedidoService.getInstance().aplicaIndiceFinanceiroSupRep(itemPedido);
	}
	
	private ItemPedido getItemPedidoFromItemComboSimilar(Pedido pedido, String cdTabelaPreco, String cdCombo, Produto produto, int qtdItem) throws SQLException {
		ItemPedido itemPedido = ItemPedidoService.getInstance().inicializaItemPedidoVenda(pedido, (Produto)ProdutoService.getInstance().findByPrimaryKey(produto), cdTabelaPreco, produto.qtItemPedido * qtdItem, cdCombo);
		itemPedido.cdCombo = cdCombo;
		return itemPedido;
	}

	private ItemCombo getItemComboFilter(Pedido pedido, String cdTabelaPreco, String cdCombo) {
		ItemCombo filterPrimario = new ItemCombo();
		filterPrimario.cdEmpresa = pedido.cdEmpresa;
		filterPrimario.cdRepresentante = pedido.cdRepresentante;
		filterPrimario.cdCombo = cdCombo;
		filterPrimario.cdTabelaPreco = cdTabelaPreco;
		filterPrimario.flTipoItemCombo = ItemCombo.TIPOITEMCOMBO_PRIMARIO;
		filterPrimario.nuPedido = pedido.nuPedido;
		filterPrimario.flOrigemPedido = pedido.flOrigemPedido;
		return filterPrimario;
	}
	
	public boolean isPedidoComComboForaVigencia(Pedido pedido) throws SQLException {
		return ComboDao.getInstance().isPedidoComComboForaVigencia(pedido);
	}
	
	public boolean isPedidoComItemCombo(Vector itemPedidoList) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			if (((ItemPedido) itemPedidoList.items[i]).isCombo()) return true;
		}
		return false;
	}
	
	public Combo getComboByItemPedido(ItemPedido itemPedido) throws SQLException {
		Combo combo = new Combo();
		combo.cdEmpresa = itemPedido.cdEmpresa;
		combo.cdRepresentante = itemPedido.cdRepresentante;
		combo.cdCombo = itemPedido.cdCombo;
		combo.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		return (Combo) findByPrimaryKey(combo);
	}
	
	public Vector ordernaListaItens(Vector itemPedidoList, boolean asc) {
		int size = itemPedidoList.size();
		ItemPedido.sortAttr = "DSPRODUTO";
		itemPedidoList.qsort();
		Vector itensPedidoAvulsos = new Vector(size);
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!itemPedido.isCombo()) {
				itensPedidoAvulsos.addElement(itemPedido);
				itemPedidoList.removeElementAt(i);
				i--;
				size--;
			}
		}
		if (asc) {
			size = itensPedidoAvulsos.size();
			for (int i = 0; i < size; i++) {
				itemPedidoList.insertElementAt(itensPedidoAvulsos.items[i], 0);
			}
		} else {
			itemPedidoList.addElementsNotNull(itensPedidoAvulsos.items);
		}
		ItemPedido.sortAttr = ItemPedido.DS_COLUNA_CDCOMBO;
		return itemPedidoList;
	}
	
}
