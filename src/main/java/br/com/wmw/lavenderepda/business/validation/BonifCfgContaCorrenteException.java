package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class BonifCfgContaCorrenteException extends ValidationException {

	private static final long serialVersionUID = 1L;
	
	public double vlSaldo;
	public double vlBonificado;

	public BonifCfgContaCorrenteException(String message, double vlSaldo, double vlBonificado) {
		super(message);
		this.vlSaldo = vlSaldo;
		this.vlBonificado = vlBonificado;
	}

}
