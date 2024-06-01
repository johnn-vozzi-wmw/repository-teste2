package br.com.wmw.lavenderepda.business.validation;

public class SugestaoVendaDifPedidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public SugestaoVendaDifPedidoException(String message) {
		super(message);
	}

	public SugestaoVendaDifPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public SugestaoVendaDifPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "SugestaoVendaDifPedidoException";
	}

}
