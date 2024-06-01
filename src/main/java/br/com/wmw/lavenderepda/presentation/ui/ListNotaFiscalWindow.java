package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.NotaFiscalService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListNotaFiscalWindow extends LavendereWmwListWindow {

	private Pedido pedido;

	public ListNotaFiscalWindow(Pedido pedido) {
		super(Messages.NOTA_FISCAL);
		this.pedido = pedido;
		setDefaultRect();
	}

	@Override protected Vector getDomainList(BaseDomain domain) throws SQLException { return pedido.getNotaFiscalList(); }

	@Override protected String[] getItem(Object domain) throws SQLException { return null; }

	@Override protected String getSelectedRowKey() throws SQLException { return listContainer.getId(listContainer.getSelectedIndex()); }

	@Override protected CrudService getCrudService() throws SQLException { return NotaFiscalService.getInstance(); }

	@Override protected BaseDomain getDomainFilter() {	return new NotaFiscal(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido); }

	@Override protected void onFormEvent(Event event) throws SQLException {}

	@Override protected void onFormStart() throws SQLException { 
		UiUtil.add(this, new ListNotaFiscalForm(pedido, true), LEFT, TOP, FILL, FILL); 
	}

}
