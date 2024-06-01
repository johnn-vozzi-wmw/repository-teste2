package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValorMaximoParcelaException extends ValidationException {

	private static final long serialVersionUID = 1L;


	public ValorMaximoParcelaException(String msg) {
		super(msg);
	}


	public String toString() {
		return "ValorMaximoParcelaException";
	}

}
