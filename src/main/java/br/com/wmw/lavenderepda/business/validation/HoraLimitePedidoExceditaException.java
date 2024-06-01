package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.Messages;

public class HoraLimitePedidoExceditaException extends ValidationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HoraLimitePedidoExceditaException() {
		super(Messages.TC2WEB_ERRO.concat(Messages.PEDIDO_MSG_HORALIMITE_EXCEDIDA));
	}
}
