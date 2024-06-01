package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class LoginFailureControlException extends ValidationException {

	public LoginFailureControlException(String message) {
		super(message);
	}

	public LoginFailureControlException(String message, String newParams) {
		super(message, newParams);
	}
}
