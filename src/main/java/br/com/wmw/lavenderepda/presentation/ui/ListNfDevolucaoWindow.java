package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class ListNfDevolucaoWindow extends WmwWindow {

	private ListNfDevolucaoForm listNfDevolucaoForm;
	private ButtonPopup btAprovar;
	private ButtonPopup btReprovar;
	
	public ListNfDevolucaoWindow(NfDevolucao nfDevolucao) throws SQLException {
		super(Messages.NFDEVOLUCAO_NOME_ENTIDADE);
		try {
			listNfDevolucaoForm = new ListNfDevolucaoForm(nfDevolucao, true);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		btAprovar = new ButtonPopup(Messages.NFDEVOLUCAO_APROVAR);
		btReprovar = new ButtonPopup(Messages.NFDEVOLUCAO_REPROVAR);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listNfDevolucaoForm, LEFT, getTop(), FILL, FILL);
		addButtonPopup(btAprovar);
		addButtonPopup(btReprovar);
	}
	
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btAprovar) {
				listNfDevolucaoForm.btAprovarClick();
			} else if (event.target == btReprovar) {
				listNfDevolucaoForm.btReprovarClick();
			}
			break;
		}
		}
	}

}
