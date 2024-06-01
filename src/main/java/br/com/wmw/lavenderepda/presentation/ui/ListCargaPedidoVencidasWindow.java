package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListCargaPedidoVencidasWindow extends WmwListWindow {
	
	public ListCargaPedidoVencidasWindow(String title) {
		super(title);
		listContainer = new GridListContainer(2, 1);
		listContainer.setColsSort(new String[][] {{Messages.CARGAPEDIDO_CARGA_PEDIDO, "CARGA"}, {Messages.CARGAPEDIDO_LABEL_VALIDADE, "VALIDADE"}});
		listContainer.setBarTopSimple();
		setDefaultRect();
	}
		
	//@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return Messages.CARGAPEDIDO_TITULO_LISTA_VENCIDOS == title ? CargaPedidoService.getInstance().findAllCargasPedidoVencidas() : CargaPedidoService.getInstance().findAllCargasPedidoProximoVencimento();
	}

	//@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		CargaPedido cargaPedido = (CargaPedido) domain;
		Date dtVencimento = CargaPedidoService.getInstance().getDtOldestPedidoCargaPedido(cargaPedido);
		if (ValueUtil.isNotEmpty(dtVencimento)) {
			DateUtil.addDay(dtVencimento, LavenderePdaConfig.nuDiasValidadeCargaPedido);
		}
		Vector itens = new Vector(0);
		itens.addElement(cargaPedido.toString());
		itens.addElement(Messages.CARGAPEDIDO_LABEL_VALIDADE + ": " + StringUtil.getStringValue(dtVencimento));
		return (String[]) itens.toObjectArray();
	}

	//@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return CargaPedidoService.getInstance();
	}

	//@Override
	protected BaseDomain getDomainFilter() {
		return new CargaPedido();
	}

	//@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}
	
}
