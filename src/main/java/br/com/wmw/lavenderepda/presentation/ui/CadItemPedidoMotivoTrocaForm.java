package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoTrocaComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadItemPedidoMotivoTrocaForm extends WmwWindow {

	public MotivoTrocaComboBox cbMotivoTroca;
	public EditMemo edObsTroca;
	private ButtonPopup btCancelar;
	public boolean result;

	private ItemPedido itemPedido;

	public CadItemPedidoMotivoTrocaForm(ItemPedido item) throws SQLException {
		super(Messages.ITEMPEDIDO_LABEL_MOTIVO_TROCA);
		cbMotivoTroca = new MotivoTrocaComboBox();
		cbMotivoTroca.setID("cbMotivoTroca");
		if (!ValueUtil.isEmpty(item.cdMotivoTroca)) {
			cbMotivoTroca.setValue(item.cdMotivoTroca);
		} else {
			cbMotivoTroca.setSelectedIndex(0);
		}
		edObsTroca = new EditMemo("", 10, 255);
		edObsTroca.setValue(item.dsObsMotivoTroca);
		edObsTroca.setID("edObsTroca");
    	btFechar = new ButtonPopup(FrameworkMessages.BOTAO_OK);
    	btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
    	itemPedido = item;
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_MOTTROCA), cbMotivoTroca, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edObsTroca, getLeft(), getNextY(), getWFill(), FILL - UiUtil.BASE_MARGIN_GAP);
		addButtonPopup(btCancelar);
	}

	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btFechar) {
					if (ValueUtil.isEmpty(cbMotivoTroca.getValue())) {
						UiUtil.showErrorMessage(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_MOTTROCA);
					} else {
						itemPedido.cdMotivoTroca = cbMotivoTroca.getValue();
						itemPedido.dsObsMotivoTroca = edObsTroca.getValue();
						result = true;
						unpop();
					}
				} else  if (event.target == btCancelar) {
					result = false;
					unpop();
				}
				break;
		}
	}
}
