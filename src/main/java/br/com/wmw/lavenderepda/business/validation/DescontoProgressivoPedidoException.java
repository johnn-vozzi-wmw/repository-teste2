package br.com.wmw.lavenderepda.business.validation;

public class DescontoProgressivoPedidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public DescontoProgressivoPedidoException(String message) {
		super(message);
	}

	public DescontoProgressivoPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public DescontoProgressivoPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}


	public String toString() {
		//return this.getClass().getSimpleName();
		return "DescontoProgressivoPedidoException";
	}

}
