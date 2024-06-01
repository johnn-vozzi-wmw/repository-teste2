package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;

public class OrigemPedidoComboBox extends BaseComboBox {

	public OrigemPedidoComboBox() {
		super(LavenderePdaConfig.usaFiltroOrigemPedidoListaPedidos() ? Messages.PEDIDO_LABEL_FLORIGEM : "");
		load();
	}

	public void setValue(String flOrigem) {
		if (flOrigem.equals(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
			select(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_PDA));
		} else if (flOrigem.equals(OrigemPedido.FLORIGEMPEDIDO_WEB)) {
			select(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_WEB));
		} else if (flOrigem.equals(OrigemPedido.FLORIGEMPEDIDO_ERP)) {
			select(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_ERP));
		} else if (flOrigem.equals(OrigemPedido.FLORIGEMPEDIDO_PORTAL)) {
			select(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_PORTAL));
		} else {
			select(null);
		}
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (select != null) {
			if (select.equals(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_PDA))) {
				return OrigemPedido.FLORIGEMPEDIDO_PDA;
			} else if (select.equals(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_WEB))) {
				return OrigemPedido.FLORIGEMPEDIDO_WEB;
			} else if (select.equals(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_ERP))) {
				return OrigemPedido.FLORIGEMPEDIDO_ERP;
			} else if (select.equals(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_PORTAL))) {
				return OrigemPedido.FLORIGEMPEDIDO_PORTAL;
			} else {
				return ValueUtil.VALOR_NI;
			}
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public String getValueString() {
		return (String)getSelectedItem();
	}

	private void load() {
		removeAll();
		add(FrameworkMessages.OPCAO_TODOS);
		add(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_PDA));
		add(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_ERP));
		add(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_WEB));
		if (LavenderePdaConfig.usaOrigemPedidoPortal) {
			add(OrigemPedido.getDsOrigemPedidoMessage(OrigemPedido.FLORIGEMPEDIDO_PORTAL));
		}
	}

}
