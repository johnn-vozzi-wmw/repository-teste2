package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class OpcaoPagamentoAVistaWindow extends WmwWindow {
	
	public ButtonPopup btAVista;
	public ButtonPopup btCancelar;
	public ButtonPopup btLiberarComSenha;
	public int options;
	public EditMemo edMessage;
	public String message;
	public boolean showBtLiberarComSenha;

	public OpcaoPagamentoAVistaWindow(String message, boolean showBtLiberarComSenha) {
		super(Messages.PEDIDO_TITULO_PEDIDO_A_VISTA);
		this.message = message;
		this.showBtLiberarComSenha = showBtLiberarComSenha;
		int rowCount = contaLinha(message);
		edMessage  = new EditMemo("@@@@@@@@@@", rowCount, 255);
		edMessage.setText(message);
		edMessage.setEditable(false);
		edMessage.transparentBackground = true;
		edMessage.drawDots = false;
		btAVista = new ButtonPopup(Messages.PEDIDO_LABEL_PEDIDO_A_VISTA);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btLiberarComSenha = new ButtonPopup(Messages.SENHADINAMICA_LABEL_SENHA);
		btLiberarComSenha.setVisible(showBtLiberarComSenha);
		btFechar.setVisible(false);
		int windowHeight = titleFont.fm.height + titleGap + footerH +  edMessage.getPreferredHeight() + HEIGHT_GAP_BIG * 3;
		setRect(LEFT + WIDTH_GAP_BIG, CENTER, FILL - WIDTH_GAP_BIG, windowHeight);
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, edMessage, getLeft(), getTop());
		addButtonPopup(btAVista);
		addButtonPopup(btLiberarComSenha);
		addButtonPopup(btCancelar);
	}

	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					options = 0;
					unpop();
				} else if (event.target == btLiberarComSenha) {
					options = 1;
					unpop();
				} else if (event.target == btAVista) {
					unpop();
					options = 2;
				}
				break;
			}
		}
	}
	
	private int contaLinha(String descricao) {
		descricao = MessageUtil.quebraLinhas(descricao, getWidth() - WIDTH_GAP_BIG * 2);
		String[] descricaoMultiLinhas = StringUtil.split(descricao, '\n');
		return descricaoMultiLinhas.length;
	}
}
