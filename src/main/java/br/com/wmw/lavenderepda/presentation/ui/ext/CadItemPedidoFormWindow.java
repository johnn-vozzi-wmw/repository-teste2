package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.lavenderepda.presentation.ui.CadItemPedidoForm;
import totalcross.ui.Window;
import totalcross.ui.event.Event;

public class CadItemPedidoFormWindow extends Window {

	private CadItemPedidoForm cadItemPedidoForm;

	public static final int CARROUSEL_TYPE_GONDOLA = 1;
	public static final int CARROUSEL_TYPE_BLOQUEADO = 2;
	public static final int CARROUSEL_TYPE_RESTRITO = 3;
	public static final int CARROUSEL_TYPE_PRODUTOCLIENTE = 4;

	public CadItemPedidoFormWindow(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	public void showWindow() throws SQLException {
		setRect(LEFT, TOP, FILL, FILL);
		add(cadItemPedidoForm);
		cadItemPedidoForm.show();
		swap(cadItemPedidoForm);
		popup();
	}
	
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
		case ButtonOptionsEvent.OPTION_PRESS: {
			if (event.target == cadItemPedidoForm.btOpcoes) {
				cadItemPedidoForm.onEvent(event);
			}
			break;
		}
		default: break;
		}
	}
}
