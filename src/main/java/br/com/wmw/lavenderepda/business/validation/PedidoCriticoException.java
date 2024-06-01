package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class PedidoCriticoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public PedidoCriticoException(String message) {
		super(message);
	}

}
