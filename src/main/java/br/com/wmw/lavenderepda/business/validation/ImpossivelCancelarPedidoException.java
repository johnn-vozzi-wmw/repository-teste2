package br.com.wmw.lavenderepda.business.validation;

public class ImpossivelCancelarPedidoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ImpossivelCancelarPedidoException(String message) {
		super(message);
	}

}
