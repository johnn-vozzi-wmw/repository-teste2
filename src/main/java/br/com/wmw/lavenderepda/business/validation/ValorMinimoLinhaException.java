package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValorMinimoLinhaException extends ValidationException {

	public ValorMinimoLinhaException() {
		super("");
	}

	public ValorMinimoLinhaException(String message) {
		super(message);
	}

}
