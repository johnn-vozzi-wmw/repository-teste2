package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import totalcross.util.Date;

public class NfeEstoqueDto {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String nuNotaRemessa;
	public String nuSerieRemessa;
	public String cdPedidoEstoque;
	public String cdStatusNfe;
	public String dsNaturezaOperacao;
	public String nuCfop;
	public Date dtSolicitacao;
	public String vlChaveAcesso;
	public String dsTipoEmissao;
	public String dsObservacao;
	public String cdTipoOperacaoNfe;
	public Date dtEmissao;
	public String hrEmissao;
	public Date dtResposta;
	public String hrResposta;
	public Date dtSaida;
	public String hrSaida;
	public double vlTotalNfe;
	public double vlTotalIcms;
	public double vlTotalSt;
	public double vlTotalIpi;
	public double vlTotalFrete;
	public double vlTotalSeguro;
	public double vlIbp;
	public String cdTransportadora;
	public String flTipoNfe;
	public ItemNfeEstoqueDto[] itemNfeEstoqueList;
	
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
	
	public String getNuNotaRemessa() {
		return nuNotaRemessa;
	}
	
	public void setNuNotaRemessa(String nuNotaRemessa) {
		this.nuNotaRemessa = nuNotaRemessa;
	}
	
	public String getNuSerieRemessa() {
		return nuSerieRemessa;
	}
	
	public void setNuSerieRemessa(String nuSerieRemessa) {
		this.nuSerieRemessa = nuSerieRemessa;
	}

	public ItemNfeEstoqueDto[] getItemNfeEstoqueList() {
		return itemNfeEstoqueList;
	}

	public void setItemNfeEstoqueList(ItemNfeEstoqueDto[] itemNfeEstoqueList) {
		this.itemNfeEstoqueList = itemNfeEstoqueList;
	}

	public String getCdPedidoEstoque() {
		return cdPedidoEstoque;
	}

	public void setCdPedidoEstoque(String cdPedidoEstoque) {
		this.cdPedidoEstoque = cdPedidoEstoque;
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

	public String getNuCfop() {
		return nuCfop;
	}

	public void setNuCfop(String nuCfop) {
		this.nuCfop = nuCfop;
	}

	public String getDtSolicitacao() {
		return dtSolicitacao == null ? null : DateUtil.formatDateDDMMYYYY(dtSolicitacao);
	}

	public void setDtSolicitacao(Date dtSolicitacao) {
		this.dtSolicitacao = dtSolicitacao;
	}

	public String getVlChaveAcesso() {
		return vlChaveAcesso;
	}

	public void setVlChaveAcesso(String vlChaveAcesso) {
		this.vlChaveAcesso = vlChaveAcesso;
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

	public String getHrEmissao() {
		return hrEmissao;
	}

	public void setHrEmissao(String hrEmissao) {
		this.hrEmissao = hrEmissao;
	}

	public String getDtResposta() {
		return dtResposta == null ? null : DateUtil.formatDateDDMMYYYY(dtResposta);
	}

	public void setDtResposta(Date dtResposta) {
		this.dtResposta = dtResposta;
	}

	public String getHrResposta() {
		return hrResposta;
	}

	public void setHrResposta(String hrResposta) {
		this.hrResposta = hrResposta;
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

	public double getVlIbp() {
		return vlIbp;
	}

	public void setVlIbp(double vlIbp) {
		this.vlIbp = vlIbp;
	}

	public String getCdTransportadora() {
		return cdTransportadora;
	}

	public void setCdTransportadora(String cdTransportadora) {
		this.cdTransportadora = cdTransportadora;
	}

	public String getFlTipoNfe() {
		return flTipoNfe;
	}

	public void setFlTipoNfe(String flTipoNfe) {
		this.flTipoNfe = flTipoNfe;
	}
	
}