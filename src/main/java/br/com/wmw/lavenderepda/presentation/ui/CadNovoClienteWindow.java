package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadNovoClienteWindow extends WmwWindow {
	
	public CadNovoClienteForm cadNovoClienteForm;
	private ButtonPopup btOk;

	public CadNovoClienteWindow(Pedido pedido) throws SQLException {
		super(Messages.NOVOCLIENTE_NOME_ENTIDADE);
		cadNovoClienteForm = new CadNovoClienteForm(true, pedido, null);
		cadNovoClienteForm.onWindow = true;
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		setDefaultRect();
	}

	public CadNovoClienteWindow() throws SQLException {
		super(Messages.NOVOCLIENTE_NOME_ENTIDADE);
		cadNovoClienteForm = new CadNovoClienteForm(true);
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		setDefaultRect();
	}
	
	//@Override
	public void initUI() {
		super.initUI();
		cadNovoClienteForm.setCadNovoClienteForm(this);
		UiUtil.add(this, cadNovoClienteForm, LEFT, TOP, FILL, FILL);
		addButtonPopup(btOk);
	}
	
	//@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btOk) {
					if (salvar()) {
						unpop();
					}
				}
				break;
			}
		}
	}
	
	private boolean salvar() {
		try {
			cadNovoClienteForm.salvarClick();
			return true;
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
		return false;
	}
	
	//@Override
	protected void btFecharClick() throws SQLException {
		int result = UiUtil.showConfirmYesNoCancelMessage(Messages.NOVOCLIENTE_MSG_FECHAR_WINDOW);
		switch (result) {
		case 0:
			return;
		case 1:
			super.btFecharClick();
			break;
		case 2:
			if (salvar()) {
				super.btFecharClick();
				break;
			} else {
				return;
			}
		}
	}

}
