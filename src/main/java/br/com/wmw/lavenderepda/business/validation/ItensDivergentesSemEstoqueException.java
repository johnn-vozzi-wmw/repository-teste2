package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Vector;

public class ItensDivergentesSemEstoqueException extends ValidationException {

	private static final long serialVersionUID = 1L;
	private Vector list;
	
	public ItensDivergentesSemEstoqueException(Vector list) {
		super(Messages.MSG_ITENS_ESTOQUE_NEGATIVO);
		this.list = list;
	}

	public Vector getList() {
		return list;
	}
}
