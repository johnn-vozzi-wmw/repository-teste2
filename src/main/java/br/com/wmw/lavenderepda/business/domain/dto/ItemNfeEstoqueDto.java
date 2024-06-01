package br.com.wmw.lavenderepda.business.domain.dto;

public class ItemNfeEstoqueDto {

	public String cdEmpresa;
	public String cdRepresentante;
	public String nuNotaRemessa;
	public String nuSerieRemessa;
	public String cdProduto;
	public String cdUnidade;
	public double qtItem;
	public String cdClassificacaoFiscal;
	public double vlItem;
	public double vlTotalItem;
	public double vlTotalIcms;
	public double vlTotalSt;
	public double vlPctIcms;
	public double vlPctPis;
	public double vlPctCofins;
	public double qtPeso;
	
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
	
	public String getCdProduto() {
		return cdProduto;
	}
	
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}

	public String getCdUnidade() {
		return cdUnidade;
	}

	public void setCdUnidade(String cdUnidade) {
		this.cdUnidade = cdUnidade;
	}

	public double getQtItem() {
		return qtItem;
	}

	public void setQtItem(double qtItem) {
		this.qtItem = qtItem;
	}

	public String getCdClassificacaoFiscal() {
		return cdClassificacaoFiscal;
	}

	public void setCdClassificacaoFiscal(String cdClassificacaoFiscal) {
		this.cdClassificacaoFiscal = cdClassificacaoFiscal;
	}

	public double getVlItem() {
		return vlItem;
	}

	public void setVlItem(double vlItem) {
		this.vlItem = vlItem;
	}

	public double getVlTotalItem() {
		return vlTotalItem;
	}

	public void setVlTotalItem(double vlTotalItem) {
		this.vlTotalItem = vlTotalItem;
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

	public double getVlPctIcms() {
		return vlPctIcms;
	}

	public void setVlPctIcms(double vlPctIcms) {
		this.vlPctIcms = vlPctIcms;
	}

	public double getVlPctPis() {
		return vlPctPis;
	}

	public void setVlPctPis(double vlPctPis) {
		this.vlPctPis = vlPctPis;
	}

	public double getVlPctCofins() {
		return vlPctCofins;
	}

	public void setVlPctCofins(double vlPctCofins) {
		this.vlPctCofins = vlPctCofins;
	}

	public double getQtPeso() {
		return qtPeso;
	}

	public void setQtPeso(double qtPeso) {
		this.qtPeso = qtPeso;
	}
	
}