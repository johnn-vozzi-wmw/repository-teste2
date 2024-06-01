package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import totalcross.util.Date;

public class NfceDTO {
	
	public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdTipoEmissao;
    public double qtTotalItem;
    public double vlTotalNfce;
    public double vlTotalDesconto;
    public double vlTotalLiquidoNfce;
    public String dsFormaPagamento;
    public double vlTotalPago;
    public double vlTroco;
    public String nuChaveAcesso;
    public String nuNfce;
    public String nuSerie;
    public Date dtEmissao;
    public String nuVersaoQrCode;
    public String nuAmbienteNfce;
    public String dsDigestValueNfce;
    public String cdIdentificacaoCsc;
    public String dsUrlQrCode;
    public String hrEmissao;
    public String nuProtocoloAutorizacao;
    public Date dtAutorizacao;
    public String hrAutorizacao;
    public double vlTotalTributos;
    public double vlPctTributosFederais;
    public double vlPctTributosEstaduais;
    public double vlPctTributosMunicipais;
    public ItemNfceDTO[] itemNfceList;
    public String cdMensagemRetorno;
    public String dsMensagemRetorno;
    
    public NfceDTO() {}
    
    public NfceDTO (Nfce nfce) {
    	try {
			FieldMapper.copy(nfce, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
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

	public String getCdTipoEmissao() {
		return cdTipoEmissao;
	}

	public void setCdTipoEmissao(String cdTipoEmissao) {
		this.cdTipoEmissao = cdTipoEmissao;
	}

	public double getQtTotalItem() {
		return qtTotalItem;
	}

	public void setQtTotalItem(double qtTotalItem) {
		this.qtTotalItem = qtTotalItem;
	}

	public double getVlTotalNfce() {
		return vlTotalNfce;
	}

	public void setVlTotalNfce(double vlTotalNfce) {
		this.vlTotalNfce = vlTotalNfce;
	}

	public double getVlTotalDesconto() {
		return vlTotalDesconto;
	}

	public void setVlTotalDesconto(double vlTotalDesconto) {
		this.vlTotalDesconto = vlTotalDesconto;
	}

	public double getVlTotalLiquidoNfce() {
		return vlTotalLiquidoNfce;
	}

	public void setVlTotalLiquidoNfce(double vlTotalLiquidoNfce) {
		this.vlTotalLiquidoNfce = vlTotalLiquidoNfce;
	}

	public String getDsFormaPagamento() {
		return dsFormaPagamento;
	}

	public void setDsFormaPagamento(String dsFormaPagamento) {
		this.dsFormaPagamento = dsFormaPagamento;
	}

	public double getVlTotalPago() {
		return vlTotalPago;
	}

	public void setVlTotalPago(double vlTotalPago) {
		this.vlTotalPago = vlTotalPago;
	}

	public double getVlTroco() {
		return vlTroco;
	}

	public void setVlTroco(double vlTroco) {
		this.vlTroco = vlTroco;
	}

	public String getNuChaveAcesso() {
		return nuChaveAcesso;
	}

	public void setNuChaveAcesso(String nuChaveAcesso) {
		this.nuChaveAcesso = nuChaveAcesso;
	}

	public String getNuNfce() {
		return nuNfce;
	}

	public void setNuNfce(String nuNfce) {
		this.nuNfce = nuNfce;
	}

	public String getNuSerie() {
		return nuSerie;
	}

	public void setNuSerie(String nuSerie) {
		this.nuSerie = nuSerie;
	}

	public String getDtEmissao() {
		return dtEmissao == null ? null : DateUtil.formatDateDDMMYYYY(dtEmissao);
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public String getNuVersaoQrCode() {
		return nuVersaoQrCode;
	}

	public void setNuVersaoQrCode(String nuVersaoQrCode) {
		this.nuVersaoQrCode = nuVersaoQrCode;
	}

	public String getNuAmbienteNfce() {
		return nuAmbienteNfce;
	}

	public void setNuAmbienteNfce(String nuAmbienteNfce) {
		this.nuAmbienteNfce = nuAmbienteNfce;
	}

	public String getDsDigestValueNfce() {
		return dsDigestValueNfce;
	}

	public void setDsDigestValueNfce(String dsDigestValueNfce) {
		this.dsDigestValueNfce = dsDigestValueNfce;
	}

	public String getCdIdentificacaoCsc() {
		return cdIdentificacaoCsc;
	}

	public void setCdIdentificacaoCsc(String cdIdentificacaoCsc) {
		this.cdIdentificacaoCsc = cdIdentificacaoCsc;
	}

	public String getDsUrlQrCode() {
		return dsUrlQrCode;
	}

	public void setDsUrlQrCode(String dsUrlQrCode) {
		this.dsUrlQrCode = dsUrlQrCode;
	}

	public String getHrEmissao() {
		return hrEmissao;
	}

	public void setHrEmissao(String hrEmissao) {
		this.hrEmissao = hrEmissao;
	}

	public String getNuProtocoloAutorizacao() {
		return nuProtocoloAutorizacao;
	}

	public void setNuProtocoloAutorizacao(String nuProtocoloAutorizacao) {
		this.nuProtocoloAutorizacao = nuProtocoloAutorizacao;
	}

	public String getDtAutorizacao() {
		return dtAutorizacao == null ? null : DateUtil.formatDateDDMMYYYY(dtAutorizacao);
	}

	public void setDtAutorizacao(Date dtAutorizacao) {
		this.dtAutorizacao = dtAutorizacao;
	}

	public String getHrAutorizacao() {
		return hrAutorizacao;
	}

	public void setHrAutorizacao(String hrAutorizacao) {
		this.hrAutorizacao = hrAutorizacao;
	}

	public double getVlTotalTributos() {
		return vlTotalTributos;
	}

	public void setVlTotalTributos(double vlTotalTributos) {
		this.vlTotalTributos = vlTotalTributos;
	}

	public double getVlPctTributosFederais() {
		return vlPctTributosFederais;
	}

	public void setVlPctTributosFederais(double vlPctTributosFederais) {
		this.vlPctTributosFederais = vlPctTributosFederais;
	}

	public double getVlPctTributosEstaduais() {
		return vlPctTributosEstaduais;
	}

	public void setVlPctTributosEstaduais(double vlPctTributosEstaduais) {
		this.vlPctTributosEstaduais = vlPctTributosEstaduais;
	}

	public double getVlPctTributosMunicipais() {
		return vlPctTributosMunicipais;
	}

	public void setVlPctTributosMunicipais(double vlPctTributosMunicipais) {
		this.vlPctTributosMunicipais = vlPctTributosMunicipais;
	}

	public ItemNfceDTO[] getItemNfceList() {
		return itemNfceList;
	}

	public void setItemNfceList(ItemNfceDTO[] itemNfceList) {
		this.itemNfceList = itemNfceList;
	}

	public String getCdMensagemRetorno() {
		return cdMensagemRetorno;
	}

	public void setCdMensagemRetorno(String cdMensagemRetorno) {
		this.cdMensagemRetorno = cdMensagemRetorno;
	}

	public String getDsMensagemRetorno() {
		return dsMensagemRetorno;
	}

	public void setDsMensagemRetorno(String dsMensagemRetorno) {
		this.dsMensagemRetorno = dsMensagemRetorno;
	}
    
}
