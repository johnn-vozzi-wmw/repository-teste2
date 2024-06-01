package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ListTransportadoraRegException  extends ValidationException {

	private static final long serialVersionUID = 1L;
	public static final String EXCEPTION = "ListTransportadoraRegException";

	public ListTransportadoraRegException() {
		super("ListTransportadoraRegException");
	}

	public String toString() {
		return "ListTransportadoraRegException";
	}

}
