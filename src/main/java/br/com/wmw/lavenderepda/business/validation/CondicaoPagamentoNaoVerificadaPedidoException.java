package br.com.wmw.lavenderepda.business.validation;

public class CondicaoPagamentoNaoVerificadaPedidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public CondicaoPagamentoNaoVerificadaPedidoException(String message) {
		super(message);
	}

	public CondicaoPagamentoNaoVerificadaPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public CondicaoPagamentoNaoVerificadaPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "CondicaoPagamentoNaoVerificadaPedidoException";
	}

}
