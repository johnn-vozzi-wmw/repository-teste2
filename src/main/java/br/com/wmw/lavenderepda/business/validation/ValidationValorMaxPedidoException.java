package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValidationValorMaxPedidoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public ValidationValorMaxPedidoException(String message) {
		super(message);
	}

	public ValidationValorMaxPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public ValidationValorMaxPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "ValidationValorMaxPedidoException";
	}

}
