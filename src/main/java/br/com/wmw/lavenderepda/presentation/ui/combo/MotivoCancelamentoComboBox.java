package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotCancelPedido;
import br.com.wmw.lavenderepda.business.service.MotCancelPedidoService;
import totalcross.util.Vector;

public class MotivoCancelamentoComboBox extends BaseComboBox {
	
	public MotivoCancelamentoComboBox() throws SQLException {
		super(Messages.PEDIDO_LABEL_MOTIVO_CANCELAMENTO_COMBO);
		this.defaultItemType = BaseComboBox.DefaultItemType_SELECT_ONE_ITEM;
		load();
	}

	public MotCancelPedido getMotCancelPedido() {
		return (MotCancelPedido)getSelectedItem();
	}

	public String getValue() {
		MotCancelPedido motCancelPedido = (MotCancelPedido)getSelectedItem();
		if (motCancelPedido != null) {
			return motCancelPedido.cdMotivoCancelamento;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String value) {
		MotCancelPedido motCancelPedido = new MotCancelPedido();
		motCancelPedido.cdMotivoCancelamento = value;
		select(motCancelPedido);
	}

	private void load() throws SQLException {
		Vector list = MotCancelPedidoService.getInstance().findAll();
		list.qsort();
		add(list);
	}

}
