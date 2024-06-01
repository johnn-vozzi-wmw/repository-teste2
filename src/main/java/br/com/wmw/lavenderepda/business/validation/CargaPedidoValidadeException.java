package br.com.wmw.lavenderepda.business.validation;

public class CargaPedidoValidadeException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public CargaPedidoValidadeException() {
		super();
	}


	public CargaPedidoValidadeException(String message) {
		super(message);
	}


	public String toString() {
		return "CargaPedidoValidadeException";
	}

}
