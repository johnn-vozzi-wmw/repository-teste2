package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ValidationGrupoProdutoNaoInseridoPedidoException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;

	public ValidationGrupoProdutoNaoInseridoPedidoException(String message) {
		super(message);
	}

	public ValidationGrupoProdutoNaoInseridoPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public ValidationGrupoProdutoNaoInseridoPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "ValidationGrupoProdutoNaoInseridoPedidoException";
	}

}
