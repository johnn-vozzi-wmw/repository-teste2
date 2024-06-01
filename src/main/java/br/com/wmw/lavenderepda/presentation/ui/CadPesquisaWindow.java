package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadPesquisaWindow extends WmwWindow {

	private static CadPesquisaWindow cadPesquisaWindow;
	private CadPesquisaForm cadPesquisaForm;
	public static boolean isPesquisaFinalizada;
	ButtonPopup btFinalizar;

	@SuppressWarnings("unused")
	private boolean onSelectPesquisaWindow;

	public CadPesquisaWindow(boolean onSelectPesquisaWindow, Questionario questionario) throws SQLException {
		super(Messages.PESQUISAS_CLIENTE_PERGUNTA);
		cadPesquisaForm = new CadPesquisaForm(true, questionario);
		this.onSelectPesquisaWindow = onSelectPesquisaWindow;
		btFinalizar = new ButtonPopup(Messages.PESQUISAS_CLIENTE_BT_FINALIZAR);
		btFechar.setVisible(false);
		setDefaultRect();
		
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadPesquisaForm, LEFT, getTop(), FILL, FILL);
		try {
			cadPesquisaForm.montaForm();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		addButtonPopup(btFinalizar);
		btFinalizar.setEnabled(cadPesquisaForm.isUltimaPergunta());
	}

	public static CadPesquisaWindow getInstance(Questionario questionario) throws SQLException {
		if (cadPesquisaWindow == null) {
			return new CadPesquisaWindow(true, questionario);
		} else {
			return cadPesquisaWindow;
		}
	}
	
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btFinalizar) {
				btfinalizarClick();
				if (cadPesquisaForm.finalizado) {
					try {
						this.btFecharClick();
					} catch (SQLException e) {
						ExceptionUtil.handle(e);
					}
				}
			} else if (event.target == cadPesquisaForm.btProxima || event.target == cadPesquisaForm.btAnterior) {
				btFinalizar.setEnabled(cadPesquisaForm.isUltimaPergunta());
			}
			break;

		default:
			break;
		}
	}
	
	private void btfinalizarClick(){
		try {
			cadPesquisaForm.btFinalizarClick();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	protected void btFecharClick() throws SQLException {
		super.btFecharClick();
	}

}
