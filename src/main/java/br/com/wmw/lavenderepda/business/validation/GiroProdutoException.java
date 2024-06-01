package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class GiroProdutoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public GiroProdutoException() {
		super("GiroProdutoException");
	}

	public String toString() {
		return "GiroProdutoException";
	}
	
}
