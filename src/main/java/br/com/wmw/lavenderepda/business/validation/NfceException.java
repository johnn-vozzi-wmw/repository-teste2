package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class NfceException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public NfceException() {
		super("NFCE_EXCEPTION");
	}
}
