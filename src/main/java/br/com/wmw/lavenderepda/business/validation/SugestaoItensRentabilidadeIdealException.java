package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class SugestaoItensRentabilidadeIdealException extends ValidationException {
	
	private static final long serialVersionUID = 1L;
	
	public SugestaoItensRentabilidadeIdealException(String message) {
		super(message);
	}
	
	public String toString() {
		return "SugestaoItensRentabilidadeIdealException";
	}

}
