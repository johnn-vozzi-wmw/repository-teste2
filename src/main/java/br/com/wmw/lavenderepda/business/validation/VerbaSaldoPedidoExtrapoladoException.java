package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class VerbaSaldoPedidoExtrapoladoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public VerbaSaldoPedidoExtrapoladoException(String message) {
		super(message);
	}

}
