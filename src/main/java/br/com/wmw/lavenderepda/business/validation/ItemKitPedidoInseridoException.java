package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ItemKitPedidoInseridoException extends ValidationException {
	
	private static final long seriaVersionUID = 1L;
	
	public Object[] params = null;

	public ItemKitPedidoInseridoException(String message) {
		super(message);
	}
	
	public ItemKitPedidoInseridoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}
}
