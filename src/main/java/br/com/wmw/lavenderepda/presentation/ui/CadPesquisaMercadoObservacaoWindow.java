package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadPesquisaMercadoObservacaoWindow extends WmwWindow {

	private LabelName lbObs;
	public EditMemo edObs;
	private ButtonPopup btOk;
	public boolean closedByOk;
	
	public CadPesquisaMercadoObservacaoWindow(String dsObservacao) {
		super(Messages.PESQUISA_MERCADO_TITULO_OBSERVACAO);
		edObs = new EditMemo("@@@@@@@@@@", 8, 255);
		edObs.setText(dsObservacao);
		lbObs = new LabelName(Messages.PESQUISA_MERCADO_TITULO_OBSERVACAO);
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_SALVAR);
		int windowHeight = titleFont.fm.height + lbObs.getPreferredHeight() + footerH + edObs.getPreferredHeight() + (HEIGHT_GAP_BIG * 3);
		setRect(LEFT + (WIDTH_GAP_BIG * 3), CENTER, FILL - (WIDTH_GAP_BIG * 3), windowHeight);
	}

	@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btOk);
		addButtonPopup(btFechar);
		UiUtil.add(this, lbObs, edObs, getLeft(), getNextY());
		edObs.requestFocus();
	}
	
	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btOk) {
					closedByBtFechar = false;
					fecharWindow();
				}
				break;
			}
		}
	}
	
}
