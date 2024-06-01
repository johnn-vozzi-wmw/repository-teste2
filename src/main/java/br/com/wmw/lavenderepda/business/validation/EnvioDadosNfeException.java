package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class EnvioDadosNfeException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public EnvioDadosNfeException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return "EnvioDadosNfeException";
	}

}
