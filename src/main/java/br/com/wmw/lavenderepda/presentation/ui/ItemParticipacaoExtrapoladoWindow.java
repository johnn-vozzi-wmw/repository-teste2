package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ItemParticipacaoExtrapoladoWindow  extends WmwListWindow {
	
	public ItemPedido itemVendaRelacionado;
	private CadItemPedidoBonificacaoForm cadItemPedidoBonificacaoForm;
	public Pedido pedido;

	public ItemParticipacaoExtrapoladoWindow(Pedido pedido) {
		super(Messages.ITEMPEDIDO_LABEL_PARTICIPACAO_EXTRAPOLADO);
		this.pedido = pedido;
		makeUnmovable();
		singleClickOn = true;
		constructorListContainer();
		setDefaultRect();
	}

	private void constructorListContainer() {
		listContainer = new GridListContainer(2, 1);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	return pedido.itemPedidoParticipacaoExtrapoladaList;
	}

	//@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        String[] item = {
        	StringUtil.getStringValue(itemPedido.cdProduto) + " - " + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
            "% atual: " + StringUtil.getStringValueToInterface(itemPedido.getVlAtualParticipacao()) + " - % permitido: " +  StringUtil.getStringValueToInterface(itemPedido.getItemTabelaPreco().vlPctMaxParticipacao) 
        };
        return item;
    }

	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}
	
	public void setCadItemPedidoBonificacaoForm(CadItemPedidoBonificacaoForm cadItemPedidoBonificacaoForm) {
		this.cadItemPedidoBonificacaoForm = cadItemPedidoBonificacaoForm;
	}

	protected void onFormStart() {
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	public void screenResized() {
		super.screenResized();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {}

	public void singleClickInList() throws SQLException {
		itemVendaRelacionado = (ItemPedido) getSelectedDomain();
		itemVendaRelacionado.setPedido(pedido);
		cadItemPedidoBonificacaoForm.produtoSelecionado = itemVendaRelacionado.getProduto();
 		cadItemPedidoBonificacaoForm.domainToScreen(itemVendaRelacionado);
 		cadItemPedidoBonificacaoForm.edit(itemVendaRelacionado);
		cadItemPedidoBonificacaoForm.repaintScreen();
		unpop();
		cadItemPedidoBonificacaoForm.setFocusInQtde();
	}
	
	public void list(Pedido pedidoRef) throws SQLException {
		this.pedido = pedidoRef;
		list();
	}

	public void list() throws java.sql.SQLException {
		super.list();  
	}
	
	public boolean hasVlItemPedidoExtrapolado() {
		return pedido.itemPedidoParticipacaoExtrapoladaList.size() > 0;
	}

}
