package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class DescontoAcumuladoException extends ValidationException {
	
	private static final long serialVersionUID = 1L;
	
	public DescontoAcumuladoException(String message) {
		super(message);
	}

}
