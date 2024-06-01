package br.com.wmw.lavenderepda.business.validation;

import totalcross.util.Vector;

public class RelProdutosPendentesPedidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;
	public Vector itemListPendentes = null;


	public RelProdutosPendentesPedidoException(String message) {
		super(message);
	}

	public RelProdutosPendentesPedidoException(String message, String newParam) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
	}

	public RelProdutosPendentesPedidoException(String message, Object[] newParams) {
		this(message);
		this.params = newParams;
	}

	public RelProdutosPendentesPedidoException(String message, Vector itemListPendentes) {
		this(message);
		this.itemListPendentes = itemListPendentes;
	}

	public String toString() {
		//return this.getClass().getSimpleName();
		return "RelProdutosPendentesPedidoException";
	}

}
