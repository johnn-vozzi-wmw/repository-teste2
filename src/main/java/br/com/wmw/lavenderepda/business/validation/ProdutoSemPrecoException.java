package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ProdutoSemPrecoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public ProdutoSemPrecoException(String message) {
		super(message);
	}

}
