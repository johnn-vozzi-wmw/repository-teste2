package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.RentabilidadeSaldoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class RelRentabilidadeSaldoWindow extends WmwWindow {

	private LabelName lbRentabilidadeFinal;
	private LabelName lbRentabilidadePedido;
	private LabelName lbRentabilidadeAtual;
	private LabelValue lbRentabilidadeFinalValue;
	private LabelValue lbRentabilidadePedidoValue;
	private LabelValue lbRentabilidadeAtualValue;
	private Pedido pedido;
	private ButtonPopup btRecalcular;

	public RelRentabilidadeSaldoWindow(Pedido pedido) {
		super(Messages.RENTABILIDADE_REPRESENTANTE);
		this.pedido = pedido;
		lbRentabilidadeAtual = new LabelName(Messages.RENTABILIDADE_ATUAL);
		lbRentabilidadeFinal = new LabelName(Messages.RENTABILIDADE_FINAL);
		lbRentabilidadePedido = new LabelName(Messages.RENTABILIDADE_PEDIDO);
		lbRentabilidadeAtualValue = new LabelValue();
		lbRentabilidadePedidoValue = new LabelValue();
		lbRentabilidadeFinalValue = new LabelValue();
		btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
		setDefaultRect();
	}

	public void initUI() {
		try {
			super.initUI();
			double vlPctRentabilidadePedido = ValueUtil.round(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
			double vlPctRentabilidadeFinal = RentabilidadeSaldoService.getInstance().getPctRentabilidadeFinal();
			double vlVendaFinal = RentabilidadeSaldoService.getInstance().getVlVendaFinal();
			double vlPctRentabilidadeAtual = 0;
			if (vlVendaFinal - pedido.vlTotalItens != 0) {
				vlPctRentabilidadeAtual = (RentabilidadeSaldoService.getInstance().getVlRentabilidadeFinal() - pedido.vlRentabilidade) / (vlVendaFinal - pedido.vlTotalItens) * 100;
			}
			lbRentabilidadeAtualValue.setValue(StringUtil.getStringValueToInterface(vlPctRentabilidadeAtual) + "%");
			lbRentabilidadePedidoValue.setValue(StringUtil.getStringValueToInterface(vlPctRentabilidadePedido) + "%");
			lbRentabilidadeFinalValue.setValue(StringUtil.getStringValueToInterface(vlPctRentabilidadeFinal) + "%");
			addComponentsScreen();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private void addComponentsScreen() {
		UiUtil.add(this, lbRentabilidadeAtual, BaseUIForm.CENTEREDLABEL, TOP + HEIGHT_GAP);
		UiUtil.add(this, lbRentabilidadeAtualValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbRentabilidadePedido, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbRentabilidadePedidoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbRentabilidadeFinal, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(this, lbRentabilidadeFinalValue, AFTER + WIDTH_GAP_BIG, SAME);
		addButtonPopup(btRecalcular);
		addButtonPopup(btFechar);
	}

	public void reposition() {
		super.reposition();
		addComponentsScreen();
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btRecalcular) {
					unpop();
					LoadingBoxWindow mb = UiUtil.createProcessingMessage();
			    	mb.popupNonBlocking();
			    	try {
			    		RentabilidadeSaldoService.getInstance().recalculateAndUpdateRentabilidadeSaldoPda();
			    	} finally {
			    		mb.unpop();
			    	}
					UiUtil.showSucessMessage(Messages.RENTABILIDADESALDO_MSG_SALDO_RECALCULADO);
					RelRentabilidadeSaldoWindow relRentabilidadeSaldoWindow = new RelRentabilidadeSaldoWindow(pedido);
					relRentabilidadeSaldoWindow.popup();
				}
				break;
		}
	}

}
