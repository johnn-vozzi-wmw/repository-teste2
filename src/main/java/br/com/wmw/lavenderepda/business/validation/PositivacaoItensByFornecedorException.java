package br.com.wmw.lavenderepda.business.validation;

public class PositivacaoItensByFornecedorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public PositivacaoItensByFornecedorException(String message) {
		super(message);
	}

	public PositivacaoItensByFornecedorException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public PositivacaoItensByFornecedorException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "PositivacaoItensByFornecedorException";
	}

}
