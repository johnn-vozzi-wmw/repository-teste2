package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class CreditoDisponivelPedidoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public CreditoDisponivelPedidoException() {
		super("");
	}

	public String toString() {
		return "CreditoDisponivelPedidoException";
	}
	
}
