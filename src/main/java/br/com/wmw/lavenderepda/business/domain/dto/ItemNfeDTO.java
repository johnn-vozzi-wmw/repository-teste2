package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;

public class ItemNfeDTO {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public String cdItemGrade1;
	public String cdItemGrade2;
	public String cdItemGrade3;
	public String cdClassificFiscal;
	public String cdUnidade;
	public double qtItemFisico;
	public double vlBaseItemTabelaPreco;
	public double vlItemPedido;
	public double vlTotalIcmsItem;
	public double vlPctIcms;
	public double vlTotalItemPedido;
	public double vlTotalStItem;
	public String flTipoAlteracao;
	public String flCigarro;
    public String dsNcmProduto;
    public String nuCfopProduto;
    public double vlTotalBaseIcmsItem;
    public double vlTotalBaseStItem;
    public double vlDespesaAcessoria;
	public double qtPeso;
	public double vlPctPis;
	public double vlPctCofins;
	public String dsCestProduto;
	public double qtMultiploEmbalagem;
	public String cdOrigemMercadoria;
    public double vlPctReducaoBaseIcms;
    public double vlPctReducaoIcms;
    public double vlPctFecopRecolher;
    public String cdBeneficioFiscal;
	
	public ItemNfeDTO() {}
	
	public ItemNfeDTO(ItemNfe itemNfe) {
		try {
			FieldMapper.copy(itemNfe, this);
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
	
	public String getCdProduto() {
		return cdProduto;
	}
	
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}
	
	public String getFlTipoItemPedido() {
		return flTipoItemPedido;
	}
	
	public void setFlTipoItemPedido(String flTipoItemPedido) {
		this.flTipoItemPedido = flTipoItemPedido;
	}
	
	public int getNuSeqProduto() {
		return nuSeqProduto;
	}
	
	public void setNuSeqProduto(int nuSeqProduto) {
		this.nuSeqProduto = nuSeqProduto;
	}
	
	public String getCdItemGrade1() {
		return cdItemGrade1;
	}
	
	public void setCdItemGrade1(String cdItemGrade1) {
		this.cdItemGrade1 = cdItemGrade1;
	}
	
	public String getCdItemGrade2() {
		return cdItemGrade2;
	}
	
	public void setCdItemGrade2(String cdItemGrade2) {
		this.cdItemGrade2 = cdItemGrade2;
	}
	
	public String getCdItemGrade3() {
		return cdItemGrade3;
	}
	
	public void setCdItemGrade3(String cdItemGrade3) {
		this.cdItemGrade3 = cdItemGrade3;
	}
	
	public String getCdClassificFiscal() {
		return cdClassificFiscal;
	}
	
	public void setCdClassificFiscal(String cdClassificFiscal) {
		this.cdClassificFiscal = cdClassificFiscal;
	}
	
	public String getCdUnidade() {
		return cdUnidade;
	}
	
	public void setCdUnidade(String cdUnidade) {
		this.cdUnidade = cdUnidade;
	}
	
	public double getQtItemFisico() {
		return qtItemFisico;
	}
	
	public void setQtItemFisico(double qtItemFisico) {
		this.qtItemFisico = qtItemFisico;
	}
	
	public double getVlBaseItemTabelaPreco() {
		return vlBaseItemTabelaPreco;
	}
	
	public void setVlBaseItemTabelaPreco(double vlBaseItemTabelaPreco) {
		this.vlBaseItemTabelaPreco = vlBaseItemTabelaPreco;
	}
	
	public double getVlItemPedido() {
		return vlItemPedido;
	}
	
	public void setVlItemPedido(double vlItemPedido) {
		this.vlItemPedido = vlItemPedido;
	}
	
	public double getVlTotalIcmsItem() {
		return vlTotalIcmsItem;
	}
	
	public void setVlTotalIcmsItem(double vlTotalIcmsItem) {
		this.vlTotalIcmsItem = vlTotalIcmsItem;
	}
	
	public double getVlPctIcms() {
		return vlPctIcms;
	}
	
	public void setVlPctIcms(double vlPctIcms) {
		this.vlPctIcms = vlPctIcms;
	}
	
	public double getVlTotalItemPedido() {
		return vlTotalItemPedido;
	}
	
	public void setVlTotalItemPedido(double vlTotalItemPedido) {
		this.vlTotalItemPedido = vlTotalItemPedido;
	}
	
	public double getVlTotalStItem() {
		return vlTotalStItem;
	}
	
	public void setVlTotalStItem(double vlTotalStItem) {
		this.vlTotalStItem = vlTotalStItem;
	}
	
	public String getFlTipoAltercao() {
		return flTipoAlteracao;
	}
	
	public void getFlTipoAltercao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}

	public String getFlTipoAlteracao() {
		return flTipoAlteracao;
	}

	public void setFlTipoAlteracao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}

	public String getFlCigarro() {
		return flCigarro;
	}

	public void setFlCigarro(String flCigarro) {
		this.flCigarro = flCigarro;
	}

	public String getDsNcmProduto() {
		return dsNcmProduto;
	}

	public void setDsNcmProduto(String dsNcmProduto) {
		this.dsNcmProduto = dsNcmProduto;
	}

	public String getNuCfopProduto() {
		return nuCfopProduto;
	}

	public void setNuCfopProduto(String nuCfopProduto) {
		this.nuCfopProduto = nuCfopProduto;
	}

	public double getVlTotalBaseIcmsItem() {
		return vlTotalBaseIcmsItem;
	}

	public void setVlTotalBaseIcmsItem(double vlTotalBaseIcmsItem) {
		this.vlTotalBaseIcmsItem = vlTotalBaseIcmsItem;
	}

	public double getVlTotalBaseStItem() {
		return vlTotalBaseStItem;
	}

	public void setVlTotalBaseStItem(double vlTotalBaseStItem) {
		this.vlTotalBaseStItem = vlTotalBaseStItem;
	}

	public double getVlDespesaAcessoria() {
		return vlDespesaAcessoria;
	}

	public void setVlDespesaAcessoria(double vlDespesaAcessoria) {
		this.vlDespesaAcessoria = vlDespesaAcessoria;
	}

	public double getQtPeso() {
		return qtPeso;
	}

	public void setQtPeso(double qtPeso) {
		this.qtPeso = qtPeso;
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

	public String getDsCestProduto() {
		return dsCestProduto;
	}

	public void setDsCestProduto(String dsCestProduto) {
		this.dsCestProduto = dsCestProduto;
	}
	
	public double getQtMultiploEmbalagem() {
		return qtMultiploEmbalagem;
	}
	
	public void setQtMultiploEmbalagem(double qtMultiploEmbalagem) {
		this.qtMultiploEmbalagem = qtMultiploEmbalagem;
	}
	
	public String getCdOrigemMercadoria() {
		return cdOrigemMercadoria;
	}

	public void setCdOrigemMercadoria(String cdOrigemMercadoria) {
		this.cdOrigemMercadoria = cdOrigemMercadoria;
	}

	public double getVlPctReducaoBaseIcms() {
		return vlPctReducaoBaseIcms;
	}

	public void setVlPctReducaoBaseIcms(double vlPctReducaoBaseIcms) {
		this.vlPctReducaoBaseIcms = vlPctReducaoBaseIcms;
	}

	public double getVlPctReducaoIcms() {
		return vlPctReducaoIcms;
	}

	public void setVlPctReducaoIcms(double vlPctReducaoIcms) {
		this.vlPctReducaoIcms = vlPctReducaoIcms;
	}
	
	public double getVlPctFecopRecolher() {
		return vlPctFecopRecolher;
	}
	
	public void setVlPctFecopRecolher(double vlPctFecopRecolher) {
		this.vlPctFecopRecolher = vlPctFecopRecolher;
	}
	
	public String getCdBeneficioFiscal() {
		return cdBeneficioFiscal;
	}
	
	public void setCdBeneficioFiscal(String cdBeneficioFiscal) {
		this.cdBeneficioFiscal = cdBeneficioFiscal;
	}
	
}
