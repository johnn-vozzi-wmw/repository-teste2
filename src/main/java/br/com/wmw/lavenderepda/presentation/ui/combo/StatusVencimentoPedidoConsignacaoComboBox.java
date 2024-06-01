package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class StatusVencimentoPedidoConsignacaoComboBox extends BaseComboBox {
	
	public final String statusVencimentoTodosDesc = "Todos";
	public final String statusVencimentoAVencer = "2";
	public final String statusVencimentoAVencerDesc = "A Vencer";
	public final String statusVencimentoVencida = "3";
	public final String statusVencimentoVencidaDesc = "Vencidas";
	
	public StatusVencimentoPedidoConsignacaoComboBox() {
		super(Messages.PEDIDO_CONSIGNACAO_STATUS_VENCIMENTO);
		load();
		setSelectedIndex(0);
	}
	
	private String getValue() {
		String select = (String)getSelectedItem();
		if (select.equals(statusVencimentoAVencerDesc)) {
			return statusVencimentoAVencer;
		} else if (select.equals(statusVencimentoVencidaDesc)) {
			return statusVencimentoVencida;
		} 
		return null;
	}

	private void load() {
		removeAll();
		add(statusVencimentoTodosDesc);
		add(statusVencimentoAVencerDesc);
		add(statusVencimentoVencidaDesc);
		
	}
	
	public boolean isStatusVencimento() {
		return ValueUtil.valueEquals(statusVencimentoVencida, getValue());
	}
	
	public boolean isStatusAVencer() {
		return ValueUtil.valueEquals(statusVencimentoAVencer, getValue());
	}

}
