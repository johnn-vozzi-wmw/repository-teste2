package br.com.wmw.lavenderepda.business.enums;

import br.com.wmw.lavenderepda.Messages;

public enum StatusSolicitacaoAutorizacaoEnum {

	P(Messages.STATUS_SOLICITACAO_AUTORIZACAO_PENDENTE),
	N(Messages.STATUS_SOLICITACAO_AUTORIZACAO_NEGADO),
	S(Messages.STATUS_SOLICITACAO_AUTORIZACAO_AUTORIZADO);

	StatusSolicitacaoAutorizacaoEnum(String title) { this.title = title; }
	private String title;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

}