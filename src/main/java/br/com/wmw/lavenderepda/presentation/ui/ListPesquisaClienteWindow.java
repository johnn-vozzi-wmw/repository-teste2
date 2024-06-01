package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.service.PesquisaAppService;
import br.com.wmw.lavenderepda.business.service.VisitaService;

public class ListPesquisaClienteWindow extends WmwWindow {

	private ListPesquisaClienteForm listPesquisaClienteForm;
	private static ListPesquisaClienteWindow listPesquisaClienteWindow;

	public ListPesquisaClienteWindow (Cliente cliente) throws SQLException {
		super(Messages.PESQUISAS_CLIENTE_TITULO);
		listPesquisaClienteForm = new ListPesquisaClienteForm(true, cliente);
		setDefaultRect();
	}


	@Override
	protected void onWindowExibition() {
		try {
			listPesquisaClienteForm.onFormExibition();
			super.onWindowExibition();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listPesquisaClienteForm, LEFT, getTop(), FILL, FILL);
		listPesquisaClienteForm.visibleState();
		listPesquisaClienteForm.showRequestedFocus();
		addButtonPopup(btFechar);
		btFechar.setEnabled(true);
	}


	public static ListPesquisaClienteWindow getInstance(Cliente cliente) throws SQLException {
		if (listPesquisaClienteWindow == null ){
			return new ListPesquisaClienteWindow(cliente);
		} else {
			return listPesquisaClienteWindow;
		}
	}


	@Override
	protected void btFecharClick() throws SQLException {
		ListPesquisaClienteForm listPesquisaClienteForm = new ListPesquisaClienteForm(true, SessionLavenderePda.getCliente());
		if (listPesquisaClienteForm.possuiPesquisaPendente()) {
			if (UiUtil.showWarnConfirmYesNoMessage(Messages.OBRIGA_RESPONDER_PESQUISA_FINALIZAR_CHEGADA)) {
				PesquisaAppService.getInstance().deletePesquisasByVisitaAndamento();
				VisitaService.getInstance().desfazVisitaEmAndamento(SessionLavenderePda.visitaAndamento);
				super.btFecharClick();
			}
		} else {
			super.btFecharClick();
		}
	}
	
}
