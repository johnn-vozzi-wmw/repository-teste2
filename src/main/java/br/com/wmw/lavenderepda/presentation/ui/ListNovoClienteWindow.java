package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class ListNovoClienteWindow extends WmwWindow {
	
	public ListNovoClienteForm listNovoClienteForm;
	private ButtonPopup btNovoCliente;
	Pedido pedido;
	private boolean onPesquisaMercOrCadCoordenadaSemCli;

	public static String PESQUISAMERCSEMCLI = "1";
	public static String CADCOORDENADASEMCLI = "2";

	public ListNovoClienteWindow(String cdTipoCadastro) throws SQLException {
		super(Messages.NOVO_PEDIDO_SELECIONAR_CLIENTE);
		listNovoClienteForm = new ListNovoClienteForm(cdTipoCadastro);
		btNovoCliente = new ButtonPopup(Messages.MENU_OPCAO_NOVO_CLIENTE);
		this.onPesquisaMercOrCadCoordenadaSemCli = true;
		setDefaultRect();
	}
	
	public ListNovoClienteWindow(boolean onPedidoSemCliente, Pedido pedido) throws SQLException {
		super(Messages.NOVO_PEDIDO_SELECIONAR_CLIENTE);
		listNovoClienteForm = new ListNovoClienteForm(onPedidoSemCliente, pedido);
		btNovoCliente = new ButtonPopup(Messages.MENU_OPCAO_NOVO_CLIENTE);
		this.pedido = pedido;
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		listNovoClienteForm.setNovoClienteForm(this);
		UiUtil.add(this, listNovoClienteForm, LEFT, getTop(), FILL, FILL);
		if (LavenderePdaConfig.isUsaCadastroNovoCliente()) {
			addButtonPopup(btNovoCliente);
		}
	}
	
	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btNovoCliente) {
						CadNovoClienteWindow cadNovoClienteWindow;
						if (onPesquisaMercOrCadCoordenadaSemCli) {
							cadNovoClienteWindow = new CadNovoClienteWindow();
						} else {
							cadNovoClienteWindow = new CadNovoClienteWindow(pedido);
						}
						cadNovoClienteWindow.cadNovoClienteForm.add();
						cadNovoClienteWindow.popup();
						unpop();
					}
					break;
				}
			}
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
			if (ee instanceof ValidationException) {
				UiUtil.showErrorMessage(ee);
			}
		}
	}
}
