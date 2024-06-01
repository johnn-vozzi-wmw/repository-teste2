package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import totalcross.util.Date;

public class NfeDTO {
	
    public ItemNfeDTO[] itemNfeList;
    public ItemNfeReferenciaDTO[] itemNfeReferenciaList;
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdMensagemRetorno;
    public String flAmbiente;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdStatusNfe;
    public String dsNaturezaOperacao;
    public String vlChaveAcesso;
    public Date dtSolicitacao;
    public String dsSerieNfe;
    public Date dtResposta;
    public String dsTipoEmissao;
    public int nuLote;
    public String dsObservacao;
    public int nuNfe;
    public String cdTipoOperacaoNfe;
    public Date dtEmissao;
    public Date dtSaida;
    public String hrSaida;
    public double vlIbpt;
    public double vlTotalNfe;
    public double vlTotalIcms;
    public double vlTotalSt;
    public double vlTotalIpi;
    public double vlTotalFrete;
    public double vlTotalSeguro;
    public double vlTotalDesconto;
    public double vlTotalProdutosNfe;
    public String flTipoAlteracao;
    public String cdTransportadora;
    public String dsMensagemRetorno;
    public String cdCliente;
    public String cdTipoPedido;
    public String hrEmissao;
    public String hrResposta;
    public String nuProtocolo;
    public double vlTotalDescontoFin;
    public String dsMensagemNotaCredito;
    
    public NfeDTO() {}
    
    public NfeDTO(Nfe nfe) {
    	try {
    		FieldMapper.copy(nfe, this);
    	} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
	public String getCdEmpresa() {
		return cdEmpresa;
	}
	
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	
	public String getCdRepresentante() {
		return cdRepresentante;
	}
	
	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}
	
	public String getNuPedido() {
		return nuPedido;
	}
	
	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}
	
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	
	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}
	
	public String getCdStatusNfe() {
		return cdStatusNfe;
	}
	
	public void setCdStatusNfe(String cdStatusNfe) {
		this.cdStatusNfe = cdStatusNfe;
	}
	
	public String getDsNaturezaOperacao() {
		return dsNaturezaOperacao;
	}
	
	public void setDsNaturezaOperacao(String dsNaturezaOperacao) {
		this.dsNaturezaOperacao = dsNaturezaOperacao;
	}
	
	public String getVlChaveAcesso() {
		return vlChaveAcesso;
	}
	
	public void setVlChaveAcesso(String vlChaveAcesso) {
		this.vlChaveAcesso = vlChaveAcesso;
	}
	
	public String getDtSolicitacao() {
		return dtSolicitacao == null ? null : DateUtil.formatDateDDMMYYYY(dtSolicitacao);
	}
	
	public void setDtSolicitacao(Date dtSolicitacao) {
		this.dtSolicitacao = dtSolicitacao;
	}
	
	public String getDsSerieNfe() {
		return dsSerieNfe;
	}
	
	public void setDsSerieNfe(String dsSerieNfe) {
		this.dsSerieNfe = dsSerieNfe;
	}
	
	public String getDtResposta() {
		return dtResposta == null ? null : DateUtil.formatDateDDMMYYYY(dtResposta);
	}
	
	public void setDtResposta(Date dtResposta) {
		this.dtResposta = dtResposta;
	}
	
	public int getNuLote() {
		return nuLote;
	}
	
	public void setNuLote(int nuLote) {
		this.nuLote = nuLote;
	}
	
	public String getDsTipoEmissao() {
		return dsTipoEmissao;
	}
	
	public void setDsTipoEmissao(String dsTipoEmissao) {
		this.dsTipoEmissao = dsTipoEmissao;
	}
	
	public String getDsObservacao() {
		return dsObservacao;
	}
	
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
	public int getNuNfe() {
		return nuNfe;
	}
	
	public void setNuNfe(int nuNfe) {
		this.nuNfe = nuNfe;
	}
	
	public String getCdTipoOperacaoNfe() {
		return cdTipoOperacaoNfe;
	}
	
	public void setCdTipoOperacaoNfe(String cdTipoOperacaoNfe) {
		this.cdTipoOperacaoNfe = cdTipoOperacaoNfe;
	}
	
	public String getDtEmissao() {
		return dtEmissao == null ? null : DateUtil.formatDateDDMMYYYY(dtEmissao);
	}
	
	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}
	
	public String getDtSaida() {
		return dtSaida == null ? null : DateUtil.formatDateDDMMYYYY(dtSaida);
	}
	
	public void setDtSaida(Date dtSaida) {
		this.dtSaida = dtSaida;
	}
	
	public String getHrSaida() {
		return hrSaida;
	}
	
	public void setHrSaida(String hrSaida) {
		this.hrSaida = hrSaida;
	}
	
	public double getVlIbpt() {
		return vlIbpt;
	}
	
	public void setVlIbpt(double vlIbpt) {
		this.vlIbpt = vlIbpt;
	}
	
	public double getVlTotalNfe() {
		return vlTotalNfe;
	}
	
	public void setVlTotalNfe(double vlTotalNfe) {
		this.vlTotalNfe = vlTotalNfe;
	}
	
	public double getVlTotalIcms() {
		return vlTotalIcms;
	}
	
	public void setVlTotalIcms(double vlTotalIcms) {
		this.vlTotalIcms = vlTotalIcms;
	}
	
	public double getVlTotalSt() {
		return vlTotalSt;
	}
	
	public void setVlTotalSt(double vlTotalSt) {
		this.vlTotalSt = vlTotalSt;
	}
	
	public double getVlTotalIpi() {
		return vlTotalIpi;
	}
	
	public void setVlTotalIpi(double vlTotalIpi) {
		this.vlTotalIpi = vlTotalIpi;
	}
	
	public double getVlTotalFrete() {
		return vlTotalFrete;
	}
	
	public void setVlTotalFrete(double vlTotalFrete) {
		this.vlTotalFrete = vlTotalFrete;
	}
	
	public double getVlTotalSeguro() {
		return vlTotalSeguro;
	}
	
	public void setVlTotalSeguro(double vlTotalSeguro) {
		this.vlTotalSeguro = vlTotalSeguro;
	}
	
	public double getVlTotalDesconto() {
		return vlTotalDesconto;
	}
	
	public void setVlTotalDesconto(double vlTotalDesconto) {
		this.vlTotalDesconto = vlTotalDesconto;
	}
	
	public double getVlTotalProdutosNfe() {
		return vlTotalProdutosNfe;
	}
	
	public void setVlTotalProdutosNfe(double vlTotalProdutosNfe) {
		this.vlTotalProdutosNfe = vlTotalProdutosNfe;
	}
	
	public String getFlTipoAltercao() {
		return flTipoAlteracao;
	}
	
	public void getFlTipoAltercao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}

	public ItemNfeDTO[] getItemNfeList() {
		return itemNfeList;
	}
	
	public String getCdTransportadora() {
		return this.cdTransportadora;
	}
	
	public void setCdTransportadora(String cdTransportadora) {
		this.cdTransportadora = cdTransportadora;
	}
	
	public String getFlAmbiente() {
		return this.flAmbiente;
	}
	
	public void setCdAmbiente(String flAmbiente) {
		this.flAmbiente = flAmbiente;
	}
	
	public String getCdMensagemRetorno() {
		return this.cdMensagemRetorno;
	}
	
	public void setCdMensagemRetorno(String cdMensagemRetorno) {
		this.cdMensagemRetorno = cdMensagemRetorno;
	}
	
	public String getDsMensagemRetorno() {
		return this.dsMensagemRetorno;
	}
	
	public void setDsMensagemRetorno(String dsMensagemRetorno) {
		this.dsMensagemRetorno = dsMensagemRetorno;
	}

	public void setItemNfeList(ItemNfeDTO[] itemNfeList) {
		this.itemNfeList = itemNfeList;
	}

	public ItemNfeReferenciaDTO[] getItemNfeReferenciaList() {
		return itemNfeReferenciaList;
	}

	public void setItemNfeReferenciaList(ItemNfeReferenciaDTO[] itemNfeReferenciaList) {
		this.itemNfeReferenciaList = itemNfeReferenciaList;
	}

	public String getFlTipoAlteracao() {
		return flTipoAlteracao;
	}

	public void setFlTipoAlteracao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}

	public String getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(String cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getCdTipoPedido() {
		return cdTipoPedido;
	}

	public void setCdTipoPedido(String cdTipoPedido) {
		this.cdTipoPedido = cdTipoPedido;
	}

	public void setFlAmbiente(String flAmbiente) {
		this.flAmbiente = flAmbiente;
	}

	public String getHrEmissao() {
		return hrEmissao;
	}

	public void setHrEmissao(String hrEmissao) {
		this.hrEmissao = hrEmissao;
	}

	public String getHrResposta() {
		return hrResposta;
	}

	public void setHrResposta(String hrResposta) {
		this.hrResposta = hrResposta;
	}

	public String getNuProtocolo() {
		return nuProtocolo;
	}

	public void setNuProtocolo(String nuProtocolo) {
		this.nuProtocolo = nuProtocolo;
	}
	
	public double getVlTotalDescontoFin() {
		return vlTotalDescontoFin;
	}
	
	public void setVlTotalDescontoFin(double vlTotalDescontoFin) {
		this.vlTotalDescontoFin = vlTotalDescontoFin;
	}

	public String getDsMensagemNotaCredito() {
		return dsMensagemNotaCredito;
	}

	public void setDsMensagemNotaCredito(String dsMensagemNotaCredito) {
		this.dsMensagemNotaCredito = dsMensagemNotaCredito;
	}

}
