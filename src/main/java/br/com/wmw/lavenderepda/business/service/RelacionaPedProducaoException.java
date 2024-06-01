package br.com.wmw.lavenderepda.business.service;

public class RelacionaPedProducaoException extends RuntimeException {

	public static final String EXCEPTION_ABORT_PROCESS = "AbortProcess";

	private static final long serialVersionUID = 1L;

	public String params;

	public RelacionaPedProducaoException() {
		super();
	}
	
	public RelacionaPedProducaoException(String message) {
		super(message);
	}

	public RelacionaPedProducaoException(String message, String newParams) {
		this(message);
		this.params = newParams;
	}

}
