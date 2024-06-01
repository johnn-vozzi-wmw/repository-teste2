package br.com.wmw.lavenderepda.business.validation;

public class ClienteAtrasadoVlTitulosException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public double param = 0d;

	public ClienteAtrasadoVlTitulosException(String message) {
		super(message);
	}

	public ClienteAtrasadoVlTitulosException(String message, double param) {
		this(message);
		this.param = param;
	}


	public String toString() {
		return "ClienteAtrasadoVlTitulosException";
	}

}
