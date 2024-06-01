package br.com.wmw.lavenderepda.business.validation;

import totalcross.util.Vector;

public class GradeAbertaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public Vector itensParaGradeFechada;
	
	public GradeAbertaException() {
		super();
	}

	public GradeAbertaException(String message) {
		super(message);
	}
	
	public GradeAbertaException(String message, Vector itens) {
		this(message);
		this.itensParaGradeFechada = itens;
	}

	public String toString() {
		return "GradeAbertaException";
	}

}
