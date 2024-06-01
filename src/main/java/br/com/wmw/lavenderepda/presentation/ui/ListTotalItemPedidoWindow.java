package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListTotalItemPedidoWindow extends WmwListWindow {
	
	private ListItemPedidoForm listItemPedidoForm;
	private Vector itemPedidoList;

	
    public ListTotalItemPedidoWindow(ListItemPedidoForm listItemPedidoForm,  Vector itemPedidoList) {
    	super(Messages.ITEMPEDIDO_NOME_ENTIDADE);
    	this.listItemPedidoForm = listItemPedidoForm;
    	this.itemPedidoList = itemPedidoList;
		listContainer = listItemPedidoForm.constructorListContainer(false);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		singleClickOn = false;
		setDefaultRect();
	}
    
	protected CrudService getCrudService() throws java.sql.SQLException {
        return ItemPedidoService.getInstance();
    }

    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	return itemPedidoList;
    }

    protected String[] getItem(Object domain) throws java.sql.SQLException {
		return listItemPedidoForm.getItem(domain);
	}

    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {
	}

	public void updateCurrentRecord(BaseDomain domain) throws java.sql.SQLException {
		list();
	}
	
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return listItemPedidoForm.getToolTip(domain);
	}
	

}