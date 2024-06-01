package br.com.wmw.lavenderepda.business.validation;

public class ClienteBloqueadoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public Object[] params = null;
	
	public ClienteBloqueadoException(String message) {
		super(message);
	}
	
	public String toString() {
		return "ClienteBloqueadoException";
	}
	
}