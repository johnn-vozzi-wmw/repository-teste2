package br.com.wmw.lavenderepda.business.validation;

public class ProdutosRelacionadosNaoAtendidosException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public ProdutosRelacionadosNaoAtendidosException(String message) {
		super(message);
	}

	public ProdutosRelacionadosNaoAtendidosException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public ProdutosRelacionadosNaoAtendidosException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		return "ProdutosRelacionadosNaoAtendidosException";
	}

}
