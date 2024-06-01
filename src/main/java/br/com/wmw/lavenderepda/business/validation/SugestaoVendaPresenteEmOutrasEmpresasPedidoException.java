package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.exception.ValidationException;

public class SugestaoVendaPresenteEmOutrasEmpresasPedidoException extends ValidationException {

	private static final long serialVersionUID = 1L;
	public String flTipoSugestaoVenda = "";

	public SugestaoVendaPresenteEmOutrasEmpresasPedidoException(String message) {
		super(message);
	}

	public SugestaoVendaPresenteEmOutrasEmpresasPedidoException(String message, String flTipoSugestaoVenda) {
		super(message);
		this.flTipoSugestaoVenda = flTipoSugestaoVenda;
	}

	public String toString() {
		return "SugestaoVendaOutrasEmpresasComCadastroComQtdPedidoException";
	}

}
