package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import javax.xml.stream.events.NotationDeclaration;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadNfDevolucaoWindow extends WmwWindow {

	private CadNfDevolucaoDynForm cadNfDevolucao;
	private ButtonPopup btAprovar;
	private ButtonPopup btReprovar;
	private NfDevolucao nfDevolucao;

	public CadNfDevolucaoWindow(NfDevolucao nfDevolucao, boolean onPopUp) throws SQLException {
		super(Messages.NFDEVOLUCAO_NOME_ENTIDADE);
		cadNfDevolucao = new CadNfDevolucaoDynForm(nfDevolucao, onPopUp);
		cadNfDevolucao.edit(nfDevolucao);
		this.nfDevolucao = nfDevolucao;
		btAprovar = new ButtonPopup(Messages.NFDEVOLUCAO_APROVAR);
		btReprovar = new ButtonPopup(Messages.NFDEVOLUCAO_REPROVAR);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadNfDevolucao, LEFT, getTop(), FILL, FILL);
		if (ValueUtil.isEmpty(nfDevolucao.flAprovacao) || NfDevolucao.NFDEVOLUCAO_PENDENTE.equals(nfDevolucao.flAprovacao)) {
			addButtonPopup(btAprovar);
			addButtonPopup(btReprovar);
		}
		
	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {

		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btAprovar) {
				cadNfDevolucao.btAprovarNfDevolucaoClick();
				finalizouAprovacaoReprovacao();
			} else if (event.target == btReprovar) {
				cadNfDevolucao.btReprovarNfDevolucaoClick();
				finalizouAprovacaoReprovacao();
			}
			break;
		}
		}
	}
	
	
	protected void finalizouAprovacaoReprovacao() throws SQLException {
		if (!ValueUtil.valueEquals(nfDevolucao.flAprovacao, NfDevolucao.NFDEVOLUCAO_PENDENTE)) {
			super.btFecharClick();
		}
	}
}
