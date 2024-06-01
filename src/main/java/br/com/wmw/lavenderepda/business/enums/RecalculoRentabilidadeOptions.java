package br.com.wmw.lavenderepda.business.enums;

public enum RecalculoRentabilidadeOptions {

	RECALCULO_RENTABILIDADE_SALVAMENTO_PEDIDO("1"),
	RECALCULO_RENTABILIDADE_REACESSO_PEDIDO("2"),
	RECALCULO_RENTABILIDADE_FECHAMENTO_PEDIDO("3"),
	RECALCULO_RENTABILIDADE_PEDIDO_REABERTO("4"),
	RECALCULO_RENTABILIDADE_FECHAMENTO_LOTE("5"),
	RECALCULO_RENTABILIDADE_ACESSO_LISTA_ITENS("6"),
	RECALCULO_RENTABILIDADE_INSERIR_ITEMPEDIDO("7");
	
	private final String option;
	
	RecalculoRentabilidadeOptions(String option) {
		this.option = option;
	}
	
	public String getOption() {
		return option;
	}
	
}
