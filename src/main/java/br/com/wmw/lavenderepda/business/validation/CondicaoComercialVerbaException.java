package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class CondicaoComercialVerbaException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public CondicaoComercialVerbaException(String message) {
		super(message);
	}

	public CondicaoComercialVerbaException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		return "CondicaoComercialVerbaException";
	}

}
