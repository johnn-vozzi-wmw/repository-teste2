package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonMenu;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class RelTrocaGerenciaWindow extends WmwWindow {

	private Pedido pedido;
	private Container scPrincipal;
	private ButtonMenu btMenuTrocaEnt;
	private ButtonMenu btMenuTrocaRec;
	private LabelName lbTrocaPctLimite;
	private LabelName lbVlTrocaTotalPedido;
	private LabelName lbVlTotalTrocaReceber;
	private LabelName lbVlTotalTrocaEntregar;
	private LabelName lbVlTrocaSaldo;
    private LabelValue lbVlTotalPedido;
    private LabelValue lbVlTotalTrocaRec;
    private LabelValue lbVlTotalTrocaEnt;
    private LabelValue lbVlSaldo;
    private LabelValue lbVlPctLimite;
    private CadPedidoForm cadPedidoForm;

	public RelTrocaGerenciaWindow(CadPedidoForm cadPedidoForm, Pedido pedido) {
		super(Messages.TROCA_TITULO_GERENCIAMENTO);
		this.cadPedidoForm = cadPedidoForm;
		this.pedido = pedido;
		lbTrocaPctLimite = new LabelName(Messages.TROCA_PERCENTUAL_LIMITE);
		lbVlTrocaTotalPedido = new LabelName(Messages.TROCA_VL_TOTAL_PEDIDO);
		lbVlTotalTrocaReceber = new LabelName(Messages.TROCA_VL_TOTAL_TROCA_REC);
		lbVlTotalTrocaEntregar = new LabelName(Messages.TROCA_VL_TOTAL_TROCA_ENT);
		lbVlTrocaSaldo = new LabelName(Messages.TROCA_SALDO);
        lbVlTotalPedido = new LabelValue("@@@@@@@@");
        lbVlTotalPedido.useCurrencyValue = true;
        lbVlTotalTrocaEnt = new LabelValue("@@@@@@@@");
        lbVlTotalTrocaEnt.useCurrencyValue = true;
        lbVlTotalTrocaRec = new LabelValue("@@@@@@@@");
        lbVlTotalTrocaRec.useCurrencyValue = true;
        lbVlSaldo = new LabelValue("@@@@@@@@");
        lbVlSaldo.useCurrencyValue = true;
        lbVlPctLimite = new LabelValue("@@@@@@@@");
        lbVlSaldo.usePercentValue = true;
        scPrincipal = new Container();
		scPrincipal.setBackColor(getBackColor());
        btMenuTrocaEnt = new ButtonMenu("Troca Entregar");
        btMenuTrocaRec = new ButtonMenu("Troca Recolher");
        setDefaultRect();
	}

	private void domainToScreen() {
		lbVlPctLimite.setValue(LavenderePdaConfig.percentualLimiteTrocaNoPedido);
		lbVlTotalPedido.setValue(pedido.vlTotalPedido);
		lbVlTotalTrocaEnt.setValue(pedido.vlTrocaEntregar);
		lbVlTotalTrocaRec.setValue(pedido.vlTrocaRecolher);
		lbVlSaldo.setValue(pedido.vlTrocaRecolher - pedido.vlTrocaEntregar);
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, scPrincipal, LEFT, TOP, FILL, FILL);
		addItensOnButtonMenu();
		addItensOnScreen();
		reposition();
		domainToScreen();
	}

	private void addItensOnScreen() {
		int yComponents = TOP + HEIGHT_GAP_BIG;
		if (LavenderePdaConfig.percentualLimiteTrocaNoPedido > 0) {
			UiUtil.add(scPrincipal, lbTrocaPctLimite, BaseUIForm.CENTEREDLABEL, getTop());
			UiUtil.add(scPrincipal, lbVlPctLimite, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED);
			yComponents = AFTER + HEIGHT_GAP;
		}
		UiUtil.add(scPrincipal, lbVlTrocaTotalPedido, BaseUIForm.CENTEREDLABEL, yComponents);
		UiUtil.add(scPrincipal, lbVlTotalPedido, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED);
		UiUtil.add(scPrincipal, lbVlTotalTrocaReceber, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(scPrincipal, lbVlTotalTrocaRec, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED);
		UiUtil.add(scPrincipal, lbVlTotalTrocaEntregar, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(scPrincipal, lbVlTotalTrocaEnt, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED);
		UiUtil.add(scPrincipal, lbVlTrocaSaldo, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(scPrincipal, lbVlSaldo, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED);
		UiUtil.add(scPrincipal, btMenuTrocaRec, CENTER - (btMenuTrocaEnt.getPreferredWidth() / 2) - WIDTH_GAP, BOTTOM);
		UiUtil.add(scPrincipal, btMenuTrocaEnt, CENTER + (btMenuTrocaRec.getPreferredWidth() / 2) + WIDTH_GAP, BOTTOM);
	}

	private void addItensOnButtonMenu() {
		btMenuTrocaEnt.removeAll();
		btMenuTrocaRec.removeAll();
		if (pedido.isPedidoAberto()) {
			btMenuTrocaEnt.addItem(Messages.TROCA_ENTREGAR_NOVA);
			btMenuTrocaRec.addItem(Messages.TROCA_RECOLHER_NOVA);
		}
		btMenuTrocaEnt.addItem(Messages.TROCA_ENTREGAR_LIST);
		btMenuTrocaRec.addItem(Messages.TROCA_RECOLHER_LIST);
	}

	public void onEvent(Event event) {
	   try {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btMenuTrocaEnt.listMenu) {
					if (btMenuTrocaEnt.listMenu.getSelectedItem().equals(Messages.TROCA_ENTREGAR_NOVA)) {
						CadItemTrocaForm cadItemTrocaForm = new CadItemTrocaForm(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT);
						unpop();
						cadItemTrocaForm.add();
						MainLavenderePda.getInstance().show(cadItemTrocaForm);
    				} else if (btMenuTrocaEnt.listMenu.getSelectedItem().equals(Messages.TROCA_ENTREGAR_LIST)) {
    					ListItemPedidoForm itemPedidoListForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT);
    					unpop();
    					itemPedidoListForm.show();
    				}
				} else if (event.target == btMenuTrocaRec.listMenu) {
					if (btMenuTrocaRec.listMenu.getSelectedItem().equals(Messages.TROCA_RECOLHER_LIST)) {
						ListItemPedidoForm itemPedidoListForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
						unpop();
						itemPedidoListForm.show();
					} else if (btMenuTrocaRec.listMenu.getSelectedItem().equals(Messages.TROCA_RECOLHER_NOVA)) {
						CadItemTrocaForm cadItemTrocaForm = new CadItemTrocaForm(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
						unpop();
						cadItemTrocaForm.add();
						MainLavenderePda.getInstance().show(cadItemTrocaForm);
					}
				}
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void reposition() {
		super.reposition();
		addItensOnScreen();
	}

}
