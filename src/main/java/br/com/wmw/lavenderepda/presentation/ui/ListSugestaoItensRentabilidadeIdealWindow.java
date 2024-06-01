package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListSugestaoItensRentabilidadeIdealWindow extends WmwListWindow {
	
	private Pedido pedido;
	public CadPedidoForm cadPedidoForm;
	private boolean fromCadItemPedido;
	private boolean openManual;
	private ButtonPopup btFecharPedido;

	public ListSugestaoItensRentabilidadeIdealWindow(final Pedido pedido, boolean fromCadItemPedido, boolean openManual) {
		super(Messages.PEDIDO_TITULO_SUGESTAO_RENTABILIDADE_IDEAL);
		this.pedido = pedido;
		this.fromCadItemPedido = fromCadItemPedido;
		this.openManual = openManual;
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
		listContainer = new GridListContainer(2, 1);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    	setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return ItemTabelaPrecoService.getInstance().getItemSugestaoRentabilidadeIdealList(pedido);
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) domain;
		Produto produto = ProdutoService.getInstance().getProduto(itemTabelaPreco.cdProduto);
		String[] item = {
        		StringUtil.getStringValue(produto),
    			StringUtil.getStringValue(Messages.PEDIDO_LABEL_QTPEDIDO + ": " + getQtdItemVendido(produto))};
        return item;
	}
	
	private String getQtdItemVendido(Produto produto) throws SQLException {
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.cdProduto.equals(produto.cdProduto)) {
					return StringUtil.getStringValueToInterface(itemPedido.getQtItemLista()) + " " + ItemPedidoService.getInstance().getTipoDescQtdListaItemPedido(itemPedido);
				} 
			}
		}
		return StringUtil.getStringValueToInterface(0.0);
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(c, domain);
		ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) domain;
		if (!ValueUtil.isEmpty(pedido.itemPedidoList)) {
			int size = pedido.itemPedidoList.size();
			for (int j = 0; j < size; j++) {
				ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[j];
				if (item.cdProduto.equals(itemTabelaPreco.cdProduto)) {
					c.setBackColor(LavendereColorUtil.COR_PRODUTO_INSERIDO_PEDIDO_BACK);
				}
			}
		}
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemTabelaPrecoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		ItemTabelaPreco domainFilter = new ItemTabelaPreco();
		return domainFilter;
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP + HEIGHT_GAP, FILL, FILL);
		if (!openManual) {
			addButtonPopup(btFecharPedido);
		}
		addBtFechar();
	}
	
	@Override
	protected void addButtons() {
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFechar) {
					cadPedidoForm.inItemNegotiationSugestaoRentabilidadeIdeal = false;
				} else if (event.target == btFecharPedido) {
					fecharWindow();
					cadPedidoForm.inItemNegotiationSugestaoRentabilidadeIdeal = false;
					if (MainLavenderePda.getInstance().getActualForm() instanceof CadPedidoForm) {
						CadPedidoForm cadPedidoForm = (CadPedidoForm)MainLavenderePda.getInstance().getActualForm();
						cadPedidoForm.getPedido().ignoraValidacaoSugestaoItensRentabilidadeIdeal = true;
						cadPedidoForm.showMessageConfirmClosePedido = false;
						cadPedidoForm.fecharPedido((fromCadItemPedido == true || openManual == true));
					}
				}
			}
		}
	}
	
	@Override
	public void singleClickInList() throws SQLException {
		if (pedido.isPedidoAberto()) {
			super.singleClickInList();
			if (listContainer.getSelectedIndex() != -1) {
				ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco) getCrudService().findByRowKey(listContainer.getSelectedId());
				CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
				Produto produto = ProdutoService.getInstance().getProduto(itemTabelaPreco.cdProduto);
				if (produto != null) {
					cadPedidoForm.inItemNegotiationSugestaoRentabilidadeIdeal = !fromCadItemPedido;
					cadItemPedidoForm.flFromCadPedido = true;
					cadItemPedidoForm.fromWindowSugestaoItensRentIdealOnFechamento = !fromCadItemPedido;
					ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, produto.cdProduto);
					if (itemPedido == null) {
						cadItemPedidoForm.add();
						if (!fromCadItemPedido) {
							MainLavenderePda.getInstance().show(cadItemPedidoForm);
						}
						cadItemPedidoForm.produtoSelecionado = produto;
						cadItemPedidoForm.gridClickAndRepaintScreen(!cadItemPedidoForm.inVendaUnitariaMode);
					} else {
						cadItemPedidoForm.edit(itemPedido);
						if (!fromCadItemPedido) {
							MainLavenderePda.getInstance().show(cadItemPedidoForm);
						} else {
							cadItemPedidoForm.onFormShow();
						}
						cadItemPedidoForm.produtoSelecionado = produto;
					}
					fecharWindow();
					cadItemPedidoForm.setFocusInQtde();
				}
			}
		}
	}

}
