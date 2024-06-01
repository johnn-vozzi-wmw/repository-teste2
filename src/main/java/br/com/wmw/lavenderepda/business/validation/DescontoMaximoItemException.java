package br.com.wmw.lavenderepda.business.validation;

public class DescontoMaximoItemException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public DescontoMaximoItemException(String msg) {
		super(msg);
	}


	public String toString() {
		return "DescontoMaximoItemException";
	}

}
