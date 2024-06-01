package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ItemPedidoBloqueadoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public ItemPedidoBloqueadoException(String msg) {
		super(msg);
	}
}
