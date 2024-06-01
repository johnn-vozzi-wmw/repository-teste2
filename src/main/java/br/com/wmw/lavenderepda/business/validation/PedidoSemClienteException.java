package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class PedidoSemClienteException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public PedidoSemClienteException(String message) {
		super(message);
	}

}
