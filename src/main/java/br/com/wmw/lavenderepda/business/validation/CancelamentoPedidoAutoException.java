package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;

public class CancelamentoPedidoAutoException extends Exception {

	private static final long serialVersionUID = 1L;

	public CancelamentoPedidoAutoException(String message) {
		super(message);
	}
	
	public CancelamentoPedidoAutoException(Pedido pedido, String message) {
		super(MessageUtil.getMessage(Messages.CANCELAMENTO_PEDIDO_ERROR, new String[] {pedido.nuPedido, message}));
	}

}
