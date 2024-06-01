package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class NfeException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public NfeException() {
		super("NFE_EXCEPTION");
	}

	public NfeException(String msg) {
		super(msg);
	}
	
}
