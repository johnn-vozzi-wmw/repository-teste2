package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class DescAcresMaximoException extends ValidationException {

	private static final long serialVersionUID = 1L;
	
	public double vlPermitido;

	public DescAcresMaximoException(String message) {
		super(message);
	}
	
	public DescAcresMaximoException(String message, double vlPermitido) {
		super(message);
		this.vlPermitido = vlPermitido;
	}

}
