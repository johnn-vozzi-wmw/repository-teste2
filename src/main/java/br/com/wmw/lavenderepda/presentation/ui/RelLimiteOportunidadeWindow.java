package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LimiteOportunidade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.LimiteOportunidadeService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class RelLimiteOportunidadeWindow extends WmwWindow {

	private LabelValue lvSaldoErp;
	private LabelName lbSaldoPda;
	private LabelValue lvSaldoPda;
	private LabelValue lvSaldoDisponivel;
	private ButtonPopup btRecalcular;
	private Pedido pedido;

	public RelLimiteOportunidadeWindow(Pedido pedido) throws SQLException {
		super(Messages.LIMITEOPORTUNIDADE_LABEL);
		this.pedido = pedido;
		lvSaldoErp = new LabelValue();
		lbSaldoPda = new LabelName(Messages.LIMITEOPORTUNIDADE_SALDO_CONSUMIDO);
		lvSaldoPda = new LabelValue();
		lvSaldoDisponivel = new LabelValue();
		lvSaldoDisponivel.setForeColor(ColorUtil.softGreen);
		btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
		loadValores();
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		int width = lbSaldoPda.getPreferredWidth();
		UiUtil.add(this, new LabelName(Messages.LIMITEOPORTUNIDADE_SALDO_REPRESENTANTE), CENTER + WIDTH_GAP, TOP);
		UiUtil.add(this, new LabelName(Messages.LIMITEOPORTUNIDADE_SALDO_ORIGINAL, RIGHT), LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP_BIG, width);
		UiUtil.add(this, lvSaldoErp, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, new LabelName(Messages.LIMITEOPORTUNIDADE_SALDO_CONSUMIDO, RIGHT), LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, width);
		UiUtil.add(this, lvSaldoPda, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, new LabelName(Messages.LIMITEOPORTUNIDADE_SALDO_DISPONIVEL, RIGHT), LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, width);
		UiUtil.add(this, lvSaldoDisponivel, AFTER + WIDTH_GAP_BIG, SAME);
		addButtonPopup(btRecalcular);
		addButtonPopup(btFechar);
	}

	public void loadValores() throws SQLException {
		LimiteOportunidade limiteOportunidadeErp = LimiteOportunidadeService.getInstance().getLimiteOportunidadeErp();
		LimiteOportunidade limiteOportunidadePda = LimiteOportunidadeService.getInstance().getLimiteOportunidadePda();
    	double vlSaldoPda = 0;
		double vlSaldoErpInicial = 0;
		if (limiteOportunidadeErp != null) {
			vlSaldoErpInicial = limiteOportunidadeErp.vlSaldo;
		}
		if (limiteOportunidadePda != null) {
			vlSaldoPda = limiteOportunidadePda.vlSaldo;
		}
		vlSaldoPda = vlSaldoPda * -1;
		int size2 = pedido.itemPedidoOportunidadeList.size();
		for (int i = 0; i < size2; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoOportunidadeList.items[i];
			vlSaldoPda += itemPedido.vlTotalItemPedido;
		}
		lvSaldoErp.setValue(vlSaldoErpInicial);
		lvSaldoPda.setValue(vlSaldoPda);
		lvSaldoDisponivel.setValue(vlSaldoErpInicial - vlSaldoPda);
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRecalcular) {
					LoadingBoxWindow mb = UiUtil.createProcessingMessage();
			    	mb.popupNonBlocking();
			    	try {
			    		LimiteOportunidadeService.getInstance().recalculateAndUpdateLimiteOportunidadePda();
			    	} finally {
			    		mb.unpop();
			    	}
					UiUtil.showSucessMessage(Messages.LIMITEOPORTUNIDADE_RECALCULO_SUCESSO);
					loadValores();
				}
				break;
			}
		}
	}

}
