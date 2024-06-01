package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ItemPedidoProdutoRestritoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public ItemPedidoProdutoRestritoException(String message) {
		super(message);
	}
}