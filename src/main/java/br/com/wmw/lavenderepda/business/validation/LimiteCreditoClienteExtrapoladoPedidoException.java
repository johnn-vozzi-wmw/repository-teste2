package br.com.wmw.lavenderepda.business.validation;

public class LimiteCreditoClienteExtrapoladoPedidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public LimiteCreditoClienteExtrapoladoPedidoException(String message) {
		super(message);
	}

	public LimiteCreditoClienteExtrapoladoPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public LimiteCreditoClienteExtrapoladoPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "LimiteCreditoClienteExtrapoladoPedidoException";
	}

}
