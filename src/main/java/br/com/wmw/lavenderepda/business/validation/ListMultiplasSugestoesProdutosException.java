package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ListMultiplasSugestoesProdutosException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public ListMultiplasSugestoesProdutosException(String message) {
		super(message);
	}

	public String toString() {
		return "ListMultiplasSugestoesProdutosException";
	}
}
