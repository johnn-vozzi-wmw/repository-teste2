package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class IemPedidoDuplicadoSemOrdemException extends ValidationException {

	public IemPedidoDuplicadoSemOrdemException() {
		super("");
	}

	public IemPedidoDuplicadoSemOrdemException(String message) {
		super(message);
	}
}
