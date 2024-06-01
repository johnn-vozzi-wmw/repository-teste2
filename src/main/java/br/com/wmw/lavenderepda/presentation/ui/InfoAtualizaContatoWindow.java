package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class InfoAtualizaContatoWindow extends WmwWindow {

	public EditMemo edMessage;
	public ButtonPopup btNovoPedido;
	public ButtonPopup btCancelar;
	public ButtonPopup btVerContatos;
	public int options;
	
	public InfoAtualizaContatoWindow() {
		super(Messages.CONTATO_ATUALIZA_CONTATOS);
		edMessage  = new EditMemo("@@@@@@@@@@", 3, 255);
		edMessage.setText(Messages.CONTATO_MSG_ATUALIZA_CONTATOS);
		edMessage.setEditable(false);
		edMessage.transparentBackground = true;
		edMessage.drawDots = false;
		btNovoPedido = new ButtonPopup(Messages.CLIENTE_ATRASADO_BT_NOVO_PED_RESUMIDO);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btVerContatos = new ButtonPopup(Messages.CONTATO_NOME_CONTATOS);
		btFechar.setVisible(false);
		int windowHeight = titleFont.fm.height + footerH +  edMessage.getPreferredHeight() + HEIGHT_GAP_BIG * 2;
		setRect(LEFT + WIDTH_GAP_BIG, CENTER, FILL - WIDTH_GAP_BIG, windowHeight);
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, edMessage, getLeft(), getTop());
		addButtonPopup(btNovoPedido);
		addButtonPopup(btVerContatos);
		addButtonPopup(btCancelar);
	}

	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btVerContatos) {
					options = 1;
					unpop();
				} else if (event.target == btNovoPedido) {
					options = 0;
					unpop();
				} else if (event.target == btCancelar) {
					unpop();
					options = 2;
				}
				break;
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}
	
}
