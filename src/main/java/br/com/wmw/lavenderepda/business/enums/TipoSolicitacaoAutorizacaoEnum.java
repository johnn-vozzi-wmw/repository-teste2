package br.com.wmw.lavenderepda.business.enums;

import br.com.wmw.lavenderepda.Messages;

public enum TipoSolicitacaoAutorizacaoEnum {

	RESERVED(""),
	NEGOCIACAO_PRECO(Messages.TIPO_SOLICITACAO_AUTORIZACAO_NEGOCIACAO_PRECO),
	BONIFICACAO(Messages.TIPO_SOLICITACAO_AUTORIZACAO_BONIFICACAO),
	VENDA_CRITICA(Messages.TIPO_SOLICITACAO_AUTORIZACAO_VENDA_CRITICA),
	PARCELA_MIN_MAX(Messages.TIPO_SOLICITACAO_PARCELA_MIN_MAX);

	TipoSolicitacaoAutorizacaoEnum(String title) { this.title = title; }
	private String title;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

}