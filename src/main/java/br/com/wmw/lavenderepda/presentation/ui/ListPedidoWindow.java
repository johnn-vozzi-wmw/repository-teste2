package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;

public class ListPedidoWindow extends WmwWindow {

	public ListPedidoForm listPedidoForm;
	public int size = 0;
	
	public ListPedidoWindow() {
		super(Messages.MENU_OPCAO_PEDIDO);
		makeUnmovable();
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
	   try {
		super.initUI();
		listPedidoForm = new ListPedidoForm();
		listPedidoForm.inConsultaPedidoReqServ = true;
		UiUtil.add(this, listPedidoForm , LEFT, TOP, FILL, FILL);
		size = listPedidoForm.listSize;
		} catch (Throwable ee) {
			ee.printStackTrace();
	   }
	}
	
	@Override
	public void popup() {
		if (size > 0) {
			super.popup();
		}
	}

}
