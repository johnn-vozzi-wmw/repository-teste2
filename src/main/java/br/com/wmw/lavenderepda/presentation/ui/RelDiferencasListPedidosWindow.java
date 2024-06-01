package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.service.PedidoService;

public class RelDiferencasListPedidosWindow extends WmwWindow {

	int size = 0;

	public RelDiferencasListPedidosWindow() {
		super(Messages.PEDIDO_DIFERENCAS_RELATORIO);
		makeUnmovable();
		setDefaultRect();
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		ListPedidoForm listPedidoForm = new ListPedidoForm();
		listPedidoForm.inPedidosDifNaoLidos = true;
		UiUtil.add(this, listPedidoForm , LEFT, TOP, FILL, FILL);
		size = listPedidoForm.listSize;
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	//@Override
	public void popup() {
		if (size > 0) {
			super.popup();
		}
	}

	//@Override
	protected void onUnpop() {
		try {
			PedidoService.getInstance().updatePedidosDiferencasLido();
		} catch (Throwable e) {
			
		}
		super.onUnpop();
	}

}
