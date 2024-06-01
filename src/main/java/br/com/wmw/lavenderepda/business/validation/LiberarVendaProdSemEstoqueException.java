package br.com.wmw.lavenderepda.business.validation;

public class LiberarVendaProdSemEstoqueException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public LiberarVendaProdSemEstoqueException() {
		super();
	}


	public LiberarVendaProdSemEstoqueException(String message) {
		super(message);
	}


	public String toString() {
		return "LiberarVendaProdSemEstoqueException";
	}

}
