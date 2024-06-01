package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Rede;

public class ClienteInadimplenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Object[] params = null;
	public int TIPO_CLIENTE_INADIMPLENTE = 0;
	
	public int qtClientesRedeAtraso;
	public Rede redeAtraso;

	public ClienteInadimplenteException(String message) {
		super(message);
	}

	public ClienteInadimplenteException(String message, String newParam, int tipoClienteInadimplente) {
		this(message);
		Object[] newParams = {newParam};
		this.params = newParams;
		this.TIPO_CLIENTE_INADIMPLENTE = tipoClienteInadimplente;
	}

	public ClienteInadimplenteException(String message, Object[] newParams, int tipoClienteInadimplente) {
		this(message);
		this.params = newParams;
		this.TIPO_CLIENTE_INADIMPLENTE = tipoClienteInadimplente;
	}

	public ClienteInadimplenteException(String message, int tipoClienteInadimplente) {
		super(message);
		this.TIPO_CLIENTE_INADIMPLENTE = tipoClienteInadimplente;
	}

	public String toString() {
		return "ClienteInadimplenteException";
	}
	
	public boolean isSomenteRedeAtraso() {
		return this.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_REDE;
	}

}
