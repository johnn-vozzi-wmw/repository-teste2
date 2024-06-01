package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class EstoqueException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public EstoqueException(String message, Object[] newParams) {
		super(message);
		this.params = newParams;
	}

	public String toString() {
		return "EstoqueException";
	}

}
