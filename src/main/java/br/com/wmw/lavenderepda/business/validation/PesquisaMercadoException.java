package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class PesquisaMercadoException extends ValidationException {
	
	private static final long serialVersionUID = 1L;
	public boolean isCoordenada;
	public boolean isFotos;

	public PesquisaMercadoException(String message) {
		super(message);
	}
	
}
