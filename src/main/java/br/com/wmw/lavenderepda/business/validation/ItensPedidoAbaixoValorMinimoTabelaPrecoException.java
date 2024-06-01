package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class ItensPedidoAbaixoValorMinimoTabelaPrecoException  extends ValidationException {
	
	private static final long serialVersionUID = 1L;
	
	public ItensPedidoAbaixoValorMinimoTabelaPrecoException(String message) {
		super(message);
	}
	
	public String toString() {
		return "ItensPedidoAbaixoValorMinimoTabelaPrecoException";
	}

}
