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
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoPerdaPedidoComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadMotivoPedidoPerdidoWindow  extends WmwWindow {
	
	private LabelValue lbDescricao;
	private ButtonPopup btOk;
	private MotivoPerdaPedidoComboBox cbMotivoPerda;
	public String cdMotivoPerda;
	private EditMemo emObservacao;
	public String dsObservacao;

	public CadMotivoPedidoPerdidoWindow() throws SQLException {
		super(Messages.PEDIDO_LABEL_MOTIVO_PEDIDO_PERDIDO_COMBO);
		cbMotivoPerda = new MotivoPerdaPedidoComboBox();
		cbMotivoPerda.setSelectedIndex(0);
		btOk = new ButtonPopup(Messages.PEDIDO_LABEL_BOTAO_PERDER_PEDIDO);
		btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
		btFechar.setVisible(true);
		lbDescricao = new LabelValue(Messages.PEDIDO_MSG_INFORME_MOTIVO_PEDIDO_PERDIDO);
		emObservacao = new EditMemo("", 3, 500);
		initUI();
	}
	
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbDescricao, getLeft() + WIDTH_GAP_BIG, getTop() + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_MOTIVO_PEDIDO_PERDIDO_COMBO), cbMotivoPerda, getLeft(), AFTER + HEIGHT_GAP_BIG);
		int preferredHeight = lbDescricao.getPreferredHeight() + cbMotivoPerda.getPreferredHeight() + btOk.getPreferredHeight();
		if (LavenderePdaConfig.isUsaEnvioRecadoNoCancelamentoPedido()) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_MOTIVO_PEDIDO_PERDIDO_JUSTIFICATIVA), emObservacao, getLeft(), AFTER + HEIGHT_GAP_BIG);
			preferredHeight += emObservacao.getPreferredHeight();
		}
		int windowHeight = titleFont.fm.height + footerH +  preferredHeight + btOk.getPreferredHeight() + HEIGHT_GAP_BIG * 5;
		setRect(LEFT + WIDTH_GAP_BIG, CENTER, FILL - WIDTH_GAP_BIG, windowHeight);
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
					if (ValueUtil.isEmpty(cbMotivoPerda.getValue())) {
						UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ERRO_MOTIVO_PEDIDO_PERDIDO);
					} else {
						cdMotivoPerda = cbMotivoPerda.getValue();
						dsObservacao = emObservacao.getValue();
						fecharWindow();
					}
				}
				if (event.target == btFechar) {
					cdMotivoPerda = null;
					dsObservacao = null;
					fecharWindow();
				}
				break;
			}
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}
	
}
