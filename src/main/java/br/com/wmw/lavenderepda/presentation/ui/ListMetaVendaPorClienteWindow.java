package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;

public class ListMetaVendaPorClienteWindow extends WmwWindow {

	public ListMetaVendaPorClienteForm listMetaVendaPorClienteForm;
	public Cliente cliente;

	public ListMetaVendaPorClienteWindow(Cliente cliente) throws SQLException {
		super(Messages.META_VENDA_CLIENTE);
		listMetaVendaPorClienteForm = new ListMetaVendaPorClienteForm(cliente, true);
		scrollable = false;
		setDefaultRect();
		
	}
	
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listMetaVendaPorClienteForm, LEFT, getTop(), FILL, FILL - footerH);
		listMetaVendaPorClienteForm.visibleState();
		listMetaVendaPorClienteForm.showRequestedFocus();
	}
	
}
