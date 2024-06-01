package br.com.wmw.lavenderepda.business.validation;

public class PedidoNaoFechadoException  extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public PedidoNaoFechadoException(String message) {
		super(message);
	}

}
