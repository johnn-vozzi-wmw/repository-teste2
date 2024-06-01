package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadMotivoItemBonificadoWindow extends WmwWindow {

	public EditMemo edMotivoItemBonificado;
	private LabelValue lvMessage;
	private ButtonPopup btOk;
	private ButtonPopup btCancelar;
	public boolean fechadoPeloBtCancelar;

	public CadMotivoItemBonificadoWindow() {
		super(Messages.MOTIVO_ITEM_BONIFICADO_WINDOW);
		edMotivoItemBonificado = new EditMemo("@@@", 10, 4000);
		lvMessage = new LabelValue();
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btCancelar = new ButtonPopup(Messages.BT_CANCELAR);
		btFechar.setVisible(false);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lvMessage, getLeft(), getNextY());
		lvMessage.setMultipleLinesText(Messages.PEDIDO_MARCA_PENDENTE_ITEM_BONIFICADO);
		UiUtil.add(this, new LabelName(Messages.MOTIVO_ITEM_BONIFICADO_WINDOW_LABEL), edMotivoItemBonificado, getLeft(), getNextY(), getWFill(), FILL - UiUtil.BASE_MARGIN_GAP);
		addButtonPopup(btOk);
		addButtonPopup(btCancelar);
	}

	@Override
	public void onEvent(Event event) {
		try {
			switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btOk) {
					if (LavenderePdaConfig.nuMinCaracteresMotivoBonificacao() > 0 && !checkQuantidadeCaracteres()) {
						return;
					} else {
						unpop();
					}
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

	private boolean checkQuantidadeCaracteres() {
		int chars = edMotivoItemBonificado.getText().trim().length();
		if (chars >= LavenderePdaConfig.nuMinCaracteresMotivoBonificacao()) return true;
		String[] args = {
				StringUtil.getStringValueToInterface(LavenderePdaConfig.nuMinCaracteresMotivoBonificacao()),
				StringUtil.getStringValue(chars)};
		UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.MOTIVO_ITEM_BONIFICADO_WINDOW_MIN_CARACTERES, args));
		return false;
	}
	
}
