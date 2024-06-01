package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;

public class ListSacWindow extends WmwWindow {

	public ListSacForm listSacForm;
	public Cliente cliente;


	public ListSacWindow(Cliente cliente) throws SQLException {
		super("SACs");
		this.cliente = cliente;
		listSacForm = new ListSacForm(cliente, true);
		scrollable = false;
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		listSacForm.listSacWindow = this;
		UiUtil.add(this, listSacForm , LEFT , getTop() , FILL , FILL - footerH);
		listSacForm.visibleState();
	}

	public void edit(BaseDomain baseDomain) throws java.sql.SQLException {
		CadSacWindow cadSacWindow = new CadSacWindow();
		cadSacWindow.edit(baseDomain);
		cadSacWindow.popup();
	}

	
}
