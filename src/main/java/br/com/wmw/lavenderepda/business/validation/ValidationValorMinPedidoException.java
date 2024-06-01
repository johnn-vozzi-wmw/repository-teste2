package br.com.wmw.lavenderepda.business.validation;

public class ValidationValorMinPedidoException extends DescAcresMaximoException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public ValidationValorMinPedidoException(String message) {
		super(message);
	}
	
	public ValidationValorMinPedidoException(String message, double vlPermitrido) {
		super(message, vlPermitrido);
	}

	public ValidationValorMinPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public ValidationValorMinPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "ValidationValorMinPedidoException";
	}

}
