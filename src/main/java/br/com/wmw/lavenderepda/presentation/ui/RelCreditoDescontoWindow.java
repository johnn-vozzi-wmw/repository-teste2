package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class RelCreditoDescontoWindow extends WmwWindow {

	private Pedido pedido;
	private ItemPedido itemPedido;
	private LabelName lbNovoSaldo;
	private LabelValue lvNovoSaldo;
	private LabelName lbCreditoDisponivel;
	private LabelValue lvCreditoDisponivel;
	private LabelName lbCreditoNecessario;
	private LabelValue lvQtdCredito;
	private ButtonPopup btConfirmar;
	public int qtdCredito;
	private boolean credAplicado;
	
	public RelCreditoDescontoWindow(Pedido pedido, ItemPedido itemPedido, boolean credAplicado) throws SQLException {
		super(credAplicado ? Messages.PRODUTOCREDITODESCONTO_DESC_APLICADO : Messages.PRODUTOCREDITODESCONTO_NOME_ENTIDADE);
		this.credAplicado = credAplicado;
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		lbCreditoDisponivel = new LabelName(Messages.PRODUTOCREDITODESCONTO_DISPONIVEIS);
		lvCreditoDisponivel = new LabelValue(StringUtil.getStringValueToInterface(pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido));
		lbCreditoNecessario = new LabelName(credAplicado ? Messages.PRODUTOCREDITODESCONTO_CRED_CONSUMIDOS : Messages.PRODUTOCREDITODESCONTO_QTD_NECESSARIA);
		int qtCredNecessario = ProdutoCreditoDescService.getInstance().getQtCreditosNecessario(itemPedido);
		lvQtdCredito = new LabelValue(qtCredNecessario);
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		if (!credAplicado) {
			lbNovoSaldo = new LabelName(Messages.PRODUTOCREDITODESCONTO_NOVO_SALDO); 
			btFechar.setText(Messages.BT_CANCELAR);
		}
		lvNovoSaldo = new LabelValue(StringUtil.getStringValueToInterface(pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido - qtCredNecessario));
		setDefaultRect();
	}
	
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbCreditoDisponivel, lvCreditoDisponivel, getLeft(), getNextY());
		UiUtil.add(this, lbCreditoNecessario, lvQtdCredito, getLeft(), getNextY());
		if (!credAplicado) {
			UiUtil.add(this, lbNovoSaldo, lvNovoSaldo, getLeft(), getNextY());
			addButtonPopup(btConfirmar);
		}
		addButtonPopup(btFechar);
	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btConfirmar) {
					brConfirmarClick();
				}
				break;
			}
		}
	}


	private void brConfirmarClick() throws SQLException {
		itemPedido.qtdCreditoDescOld = itemPedido.qtdCreditoDesc;
		qtdCredito = ProdutoCreditoDescService.getInstance().getQtCreditosAplicar(pedido, itemPedido, lvQtdCredito.getIntegerValue());
		fecharWindow();
	}
	
}
