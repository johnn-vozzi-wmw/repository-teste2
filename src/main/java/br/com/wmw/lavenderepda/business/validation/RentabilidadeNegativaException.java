package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class RentabilidadeNegativaException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public RentabilidadeNegativaException() {
		super("RentabilidadeNegativaException");
	}

	public String toString() {
		return "RentabilidadeNegativaException";
	}
	
}
