package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class EstoquePrevistoException extends ValidationException {
	
	private static final long serialVersionUID = 1L;
	
	public EstoquePrevistoException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "EstoquePrevistoException";
	}

}
