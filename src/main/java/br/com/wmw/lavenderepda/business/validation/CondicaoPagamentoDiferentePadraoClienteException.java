package br.com.wmw.lavenderepda.business.validation;

public class CondicaoPagamentoDiferentePadraoClienteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public CondicaoPagamentoDiferentePadraoClienteException(String message) {
		super(message);
	}

	public CondicaoPagamentoDiferentePadraoClienteException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public CondicaoPagamentoDiferentePadraoClienteException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
