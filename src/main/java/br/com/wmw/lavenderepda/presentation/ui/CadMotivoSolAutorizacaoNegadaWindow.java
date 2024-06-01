package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadMotivoSolAutorizacaoNegadaWindow extends WmwWindow {

	private EditMemo edMotivoNegacao;
	private LabelValue lvMessage;
	private ButtonPopup btOk;
	private ButtonPopup btCancelar;

	public boolean confirmadoNegacao;
	public String motivoNegacao = null;
	public boolean fechadoPeloBtCancelar;

	public CadMotivoSolAutorizacaoNegadaWindow() {
		super(Messages.SOL_AUTORIZACAO_MOTIVO_WINDOW_TITLE);
		edMotivoNegacao = new EditMemo("@@@", 3, 1000);
		lvMessage = new LabelValue();
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btCancelar = new ButtonPopup(Messages.BT_CANCELAR);
		btFechar.setVisible(false);
		int windowHeight = titleFont.fm.height + footerH + HEIGHT_GAP_BIG * 20;
		setRect(LEFT + WIDTH_GAP_BIG, CENTER, FILL - WIDTH_GAP_BIG, windowHeight);
	}

	@Override
	public void popup() {
		motivoNegacao = null;
		confirmadoNegacao = fechadoPeloBtCancelar = false;
		super.popup();
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, lvMessage, getLeft(), getTop() + HEIGHT_GAP_BIG);
		lvMessage.setMultipleLinesText(Messages.SOL_AUTORIZACAO_MOTIVO_WINDOW_LABEL);
		UiUtil.add(this, new LabelName(Messages.SOL_AUTORIZACAO_MOTIVO_WINDOW_MOTIVO_LABEL), edMotivoNegacao, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - (HEIGHT_GAP_BIG * 4));
		addButtonPopup(btOk);
		addButtonPopup(btCancelar);
	}


	public void onEvent(Event event) {
		try {
			switch (event.type) {
				case ControlEvent.PRESSED:
					if (event.target == btOk) {
						confirmadoNegacao = true;
						motivoNegacao = edMotivoNegacao.getValue();
						unpop();
					} else if (event.target == btCancelar) {
						fechadoPeloBtCancelar = true;
						unpop();
					}
				break;
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}
	
}