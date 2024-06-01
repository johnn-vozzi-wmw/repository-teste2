package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ItensComProblemaPedidoSemClienteException extends ValidationException {

	public ItensComProblemaPedidoSemClienteException(String message, String newParams) {
		super(message, newParams);
	}
}
