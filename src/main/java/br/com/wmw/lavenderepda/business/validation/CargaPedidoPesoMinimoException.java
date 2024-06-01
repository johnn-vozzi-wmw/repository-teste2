package br.com.wmw.lavenderepda.business.validation;

public class CargaPedidoPesoMinimoException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public CargaPedidoPesoMinimoException() {
		super();
	}

	public CargaPedidoPesoMinimoException(String message) {
		super(message);
	}

	public String toString() {
		return "CargaPedidoPesoMinimoException";
	}

}
