package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.VerbaUsuarioService;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;

public class RelVerbaUsuarioSaldoWindow extends WmwWindow {

	private LabelValue lbVerbaDisponivel;
	private LabelValue lbVerbaConsumida;
	private LabelValue lbVerbaPositiva;
	private LabelValue lbVerbaSaldoFinal;
	private ButtonPopup btRecalcular;
	private Pedido pedido;
	private ItemPedido itemPedido;
	
	public RelVerbaUsuarioSaldoWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		super(Messages.VERBAUSUARIO_LABEL_TOTAL_GERAL);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		load();
	}
		
	private void load() throws SQLException {	
		setDefaultRect();
		Container base = this;
		btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
		addButtonPopup(btRecalcular);
		addButtonPopup(btFechar);
		double vlVerbaPedido = ValueUtil.round(pedido.vlVerbaPedido);
		double vlVerbaPositiva =  ValueUtil.round(pedido.vlVerbaPedidoPositivo);
		final int space = WIDTH_GAP * 5;
		int heighGap = HEIGHT_GAP * 4;
		double vlSaldoTotal = VerbaUsuarioService.getInstance().getVlSaldo();
		if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
			vlSaldoTotal += vlVerbaPedido + (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0);
		}
		if (vlSaldoTotal != 0) {
			UiUtil.add(base, new LabelName(Messages.VERBAUSUARIO_LABEL_VLVERBADISPONIVEL), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
			lbVerbaDisponivel = new LabelValue();
			UiUtil.add(base, lbVerbaDisponivel, AFTER + space, SAME);
			if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
				lbVerbaDisponivel.setValue(Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldoTotal));
			} else {
				lbVerbaDisponivel.setValue(Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldoTotal - vlVerbaPedido - (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0)));
			}
			UiUtil.add(base, new LabelName(Messages.VERBAUSUARIO_LABEL_VLVERBAPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
			lbVerbaConsumida = new LabelValue();
			UiUtil.add(base, lbVerbaConsumida, AFTER + space, SAME);
			lbVerbaConsumida.setValue(Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPedido));
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva) {
				UiUtil.add(base, new LabelName(Messages.VERBAUSUARIO_LABEL_VLVERBAGERADOPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
				lbVerbaPositiva = new LabelValue();
				UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
				lbVerbaPositiva.setValue(Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPositiva));
			}
			Button sep2 = new Button("");
			sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
			int oneChar = fm.charWidth('A');
			UiUtil.add(base, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - (oneChar * 4), 1);
			UiUtil.add(base, new LabelName(Messages.VERBAUSUARIO_LABEL_VLSALDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
			lbVerbaSaldoFinal = new LabelValue();
			UiUtil.add(base, lbVerbaSaldoFinal, AFTER + space, SAME);
			double saldoFinal = vlSaldoTotal;
			if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
				saldoFinal += vlVerbaPedido + (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0);
			}
			lbVerbaSaldoFinal.setValue(Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(saldoFinal));
			if (saldoFinal > 0) {
				lbVerbaSaldoFinal.setForeColor(ColorUtil.softGreen);
			} else {
				lbVerbaSaldoFinal.setForeColor(ColorUtil.softRed);
			}
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() && !LavenderePdaConfig.geraVerbaPositiva) {
				UiUtil.add(base, new LabelName(Messages.VERBAUSUARIO_LABEL_VLVERBAPEDIDOPOSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
				lbVerbaPositiva = new LabelValue();
				UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
				lbVerbaPositiva.setValue(Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPositiva));
				Button sep3 = new Button("");
				UiUtil.add(base, sep3, LEFT, AFTER + (HEIGHT_GAP * 10), FILL, 1);
				sep3.setForeColor(Color.getRGB(125, 125, 125));
			}
		} else {
			UiUtil.add(base, Messages.VERBAUSUARIO_MSG_NENHUM_SALDO_DISPONIVEL, CENTER, CENTER);
		}
	}
	
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRecalcular) {
					unpop();
					VerbaUsuarioService.getInstance().recalculateAndUpdateVerbaUsuarioPda();
					UiUtil.showSucessMessage(Messages.VERBAUSUARIO_MSG_SALDO_VERBA_RECALCULADO);
					RelVerbaUsuarioSaldoWindow relVerbaUsuarioSaldoWindow = new RelVerbaUsuarioSaldoWindow(pedido, itemPedido);
					relVerbaUsuarioSaldoWindow.popup();
				}
				break;
			}
		}
	}
	
}
