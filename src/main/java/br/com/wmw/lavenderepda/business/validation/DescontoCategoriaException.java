package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class DescontoCategoriaException extends ValidationException {

	private static final long serialVersionUID = 1L;
	public String tipoCategoria;

	public DescontoCategoriaException(String message, String tipoCategoria) {
		super(message);
		this.tipoCategoria = tipoCategoria;
	}
	
	@Override
	public String toString() {
		return "DescontoCategoriaException";
	}

}
