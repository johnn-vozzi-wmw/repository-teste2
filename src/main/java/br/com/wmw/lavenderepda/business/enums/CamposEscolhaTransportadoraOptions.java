package br.com.wmw.lavenderepda.business.enums;

public enum CamposEscolhaTransportadoraOptions {
	
	CAMPO_NOME_TRANSPORTADORA("1"),
	CAMPO_DESCRICAO_TIPOFRETE("2"),
	CAMPO_VALORFRETE("3");
	
	private final String option;
	
	CamposEscolhaTransportadoraOptions(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}
}
