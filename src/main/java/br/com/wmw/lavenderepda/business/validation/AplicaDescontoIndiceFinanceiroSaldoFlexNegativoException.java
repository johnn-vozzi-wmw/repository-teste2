package br.com.wmw.lavenderepda.business.validation;

public class AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException(String message) {
		super(message);
	}
	
	public String toString() {
		return "AplicaDescontoIndiceFinanceiroSaldoFlexNegativoException";
	}

}
