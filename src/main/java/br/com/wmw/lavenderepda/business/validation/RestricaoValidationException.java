package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class RestricaoValidationException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public RestricaoValidationException(String message) {
		super(message);
	}
}
