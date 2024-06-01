package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValidationBonificacaoSaldoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public ValidationBonificacaoSaldoException(String message) {
		super(message);
	}

	public String toString() {
		return "ValidationBonificacaoSaldoException";
	}

}
