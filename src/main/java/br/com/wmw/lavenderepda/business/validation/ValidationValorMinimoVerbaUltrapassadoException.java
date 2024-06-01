package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValidationValorMinimoVerbaUltrapassadoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public ValidationValorMinimoVerbaUltrapassadoException(String message) {
		super(message);
	}

	public ValidationValorMinimoVerbaUltrapassadoException(String message, String newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		return "ValidationValorMinimoVerbaUltrapassadoException";
	}
	
}
