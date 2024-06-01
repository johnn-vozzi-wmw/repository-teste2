package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import totalcross.util.Date;

public class AtendimentoHist extends BaseDomain {
	
	private String cdEmpresa;
	private String flOrigem;
	private String cdCliente;
	
	private String dsAtendimento;
	private String cdAtendimento;
	private String chatId;
	private Date dtAtendimento;
	private String hrAtendimento;
	private String cdServico;
	private String dsServico;
	
	private String cdUsuario;
	private String nmUsuario;

	private String cdRepresentante;
	private String nmRepresentante;

	private String nuPedido;
	private String obsAtendimento;
	private String obsPedido;
	private String obsEntrega;
	private String obsNf;
	private String obsNaoVenda;
	private String cdMotivoCancelamento;
	private String dsMotivoCancelamento;
	private String detalhes1;
	private String detalhes2;

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getFlOrigem() {
		return flOrigem;
	}

	public void setFlOrigem(String flOrigem) {
		this.flOrigem = flOrigem;
	}

	public String getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getNuPedido() {
		return nuPedido;
	}

	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getDsAtendimento() {
		return dsAtendimento;
	}

	public void setDsAtendimento(String dsAtendimento) {
		this.dsAtendimento = dsAtendimento;
	}

	public Date getDtAtendimento() {
		return dtAtendimento;
	}

	public void setDtAtendimento(Date dtAtendimento) {
		this.dtAtendimento = dtAtendimento;
	}

	public String getHrAtendimento() {
		return hrAtendimento;
	}

	public void setHrAtendimento(String hrAtendimento) {
		this.hrAtendimento = hrAtendimento;
	}

	public boolean isPossuiChat() {
		return chatId != null;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}
	
	public String getDsAtendimentoResumido() {
		return StringUtil.clearEnter(StringUtil.resume(getDsAtendimento(), 150)).replaceAll("=", "");
	}

	public String getObsAtendimento() {
		return obsAtendimento;
	}

	public void setObsAtendimento(String obsAtendimento) {
		this.obsAtendimento = obsAtendimento;
	}

	public String getObsPedido() {
		return obsPedido;
	}

	public void setObsPedido(String obsPedido) {
		this.obsPedido = obsPedido;
	}

	public String getObsEntrega() {
		return obsEntrega;
	}

	public void setObsEntrega(String obsEntrega) {
		this.obsEntrega = obsEntrega;
	}

	public String getObsNf() {
		return obsNf;
	}

	public void setObsNf(String obsNf) {
		this.obsNf = obsNf;
	}

	public String getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(String cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public String getNmRepresentante() {
		return nmRepresentante;
	}

	public void setNmRepresentante(String nmRepresentante) {
		this.nmRepresentante = nmRepresentante;
	}

	public String getCdAtendimento() {
		return cdAtendimento;
	}

	public void setCdAtendimento(String cdAtendimento) {
		this.cdAtendimento = cdAtendimento;
	}

	public String getDetalhes1() {
		return detalhes1;
	}

	public void setDetalhes1(String detalhes1) {
		this.detalhes1 = detalhes1;
	}

	public String getDetalhes2() {
		return detalhes2;
	}

	public void setDetalhes2(String detalhes2) {
		this.detalhes2 = detalhes2;
	}

	public String getCdServico() {
		return cdServico;
	}

	public void setCdServico(String cdServico) {
		this.cdServico = cdServico;
	}

	public String getDsServico() {
		return dsServico;
	}

	public void setDsServico(String dsServico) {
		this.dsServico = dsServico;
	}

	public String getObsNaoVenda() {
		return obsNaoVenda;
	}

	public void setObsNaoVenda(String obsNaoVenda) {
		this.obsNaoVenda = obsNaoVenda;
	}

	public String getCdMotivoCancelamento() {
		return cdMotivoCancelamento;
	}

	public void setCdMotivoCancelamento(String cdMotivoCancelamento) {
		this.cdMotivoCancelamento = cdMotivoCancelamento;
	}

	public String getDsMotivoCancelamento() {
		return dsMotivoCancelamento;
	}

	public void setDsMotivoCancelamento(String dsMotivoCancelamento) {
		this.dsMotivoCancelamento = dsMotivoCancelamento;
	}

	@Override
	public String getPrimaryKey() {
		return "";
	}

}
