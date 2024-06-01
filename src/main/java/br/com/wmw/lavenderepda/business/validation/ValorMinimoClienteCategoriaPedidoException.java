package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValorMinimoClienteCategoriaPedidoException extends ValidationException {

	private static final long serialVersionUID = 1L;
	public Object[] params = null;
	
	public ValorMinimoClienteCategoriaPedidoException(String message) {
		super(message);
	}
	
	public ValorMinimoClienteCategoriaPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		return "ValorMinimoClienteCategoriaPedidoException";
	}

}
