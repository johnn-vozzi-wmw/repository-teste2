package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class NaoVendaProdPedidoException extends ValidationException {

	private static final long serialVersionUID = 1L;
	
	public NaoVendaProdPedidoException() {
		super("NaoVendaProdPedidoException");
	}
	
	@Override
	public String toString() {
		return "NaoVendaProdPedidoException";
	}

}
