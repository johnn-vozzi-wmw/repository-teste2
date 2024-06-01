package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.StatusCargaPedido;
import totalcross.util.Vector;

public class StatusCargaPedidoComboBox extends BaseComboBox {

	public StatusCargaPedidoComboBox() {
		super(Messages.CARGAPEDIDO_STATUS);
		carregaStatus();
	}

	public StatusCargaPedido getStatusCargaPedido() {
		return (StatusCargaPedido) getSelectedItem();
	}

	public String getValue() {
		StatusCargaPedido statusCargaPedido = (StatusCargaPedido) getSelectedItem();
		if (statusCargaPedido != null) {
			return statusCargaPedido.cdStatusCargaPedido;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}
	public String getDescription() {
		StatusCargaPedido statusCargaPedido = (StatusCargaPedido) getSelectedItem();
		if (statusCargaPedido != null) {
			return statusCargaPedido.dsStatusCargaPedido;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String value) {
		StatusCargaPedido statusCargaPedido = new StatusCargaPedido();
		statusCargaPedido.cdStatusCargaPedido = value;
		select(statusCargaPedido);
	}

	private void carregaStatus() {
		Vector statusCargaPedidoList = new Vector();
		//--
		StatusCargaPedido statusCargaPedido = new StatusCargaPedido();
		statusCargaPedido.cdStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_ABERTO;
		statusCargaPedido.dsStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_DSABERTO;
		statusCargaPedidoList.addElement(statusCargaPedido);
		//--
		statusCargaPedido = new StatusCargaPedido();
		statusCargaPedido.cdStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_FECHADO;
		statusCargaPedido.dsStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_DSFECHADO;
		statusCargaPedidoList.addElement(statusCargaPedido);
		//--
		statusCargaPedido = new StatusCargaPedido();
		statusCargaPedido.cdStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_TRANSMITIDO;
		statusCargaPedido.dsStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_DSTRANSMITIDO;
		statusCargaPedidoList.addElement(statusCargaPedido);
		//--
		statusCargaPedido = new StatusCargaPedido();
		statusCargaPedido.cdStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_VENCIDO;
		statusCargaPedido.dsStatusCargaPedido = StatusCargaPedido.STATUS_CARGAPEDIDO_DSVENCIDO;
		statusCargaPedidoList.addElement(statusCargaPedido);
		add(statusCargaPedidoList);
		setSelectedIndex(0);
	}

}
