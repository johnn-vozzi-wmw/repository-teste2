package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoCancelamentoComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadMotivoCancelamentoPedidoWindow  extends WmwWindow {
	
	private LabelValue lbDescricao;
	private ButtonPopup btOk;
	private MotivoCancelamentoComboBox cbMotivoCancelamento;
	public String cdMotivoCancelamento;
	private EditMemo emJustificativaCancelamento;
	public String dsJustificativaCancelamento;

	public CadMotivoCancelamentoPedidoWindow() throws SQLException {
		super(Messages.PEDIDO_LABEL_MOTIVO_CANCELAMENTO_COMBO);
		cbMotivoCancelamento = new MotivoCancelamentoComboBox();
		cbMotivoCancelamento.setSelectedIndex(0);
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btFechar.setVisible(true);
		lbDescricao = new LabelValue(Messages.PEDIDO_MSG_INFORME_MOTIVO_CANCELAMENTO);
		emJustificativaCancelamento = new EditMemo("", 3, 500);
		int windowHeight = titleFont.fm.height + footerH + UiUtil.getControlPreferredHeight() * 3 + HEIGHT_GAP_BIG * 5;
		if (LavenderePdaConfig.isUsaEnvioRecadoNoCancelamentoPedido()) {
			windowHeight += UiUtil.getControlPreferredHeight();
		}
		setRect(CENTER, CENTER, SCREENSIZE + 92, windowHeight);
	}
	
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbDescricao, CENTER, getTop() + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_MOTIVO_CANCELAMENTO_COMBO), cbMotivoCancelamento, getLeft(), AFTER + HEIGHT_GAP_BIG);
		if (LavenderePdaConfig.isUsaEnvioRecadoNoCancelamentoPedido()) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_MOTIVO_CANCELAMENTO_JUSTIFICATIVA), emJustificativaCancelamento, getLeft(), AFTER + HEIGHT_GAP_BIG);
		}
	}
	
	@Override
	protected void addBtFechar() {
		addButtonPopup(btOk);
		addButtonPopup(btFechar);
	}
	
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btOk) {
					if (ValueUtil.isEmpty(cbMotivoCancelamento.getValue())) {
						UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ERRO_MOTIVO_CANCELAMENTO);
					} else {
						cdMotivoCancelamento = cbMotivoCancelamento.getValue();
						if (LavenderePdaConfig.isUsaEnvioRecadoNoCancelamentoPedido()) {
							dsJustificativaCancelamento = emJustificativaCancelamento.getValue();
						}
						fecharWindow();
					}
				}
				if (event.target == btFechar) {
					cdMotivoCancelamento = null;
					dsJustificativaCancelamento = null;
					fecharWindow();
				}
				break;
			}
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

}
