package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ProdutoClienteRelacionadoException extends ValidationException  {

	private static final long serialVersionUID = 1L;

	public ProdutoClienteRelacionadoException(String message) {
		super(message);
	}

}
