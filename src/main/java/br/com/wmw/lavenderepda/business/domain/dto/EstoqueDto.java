package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import totalcross.util.Date;

public class EstoqueDto {
	
	public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdLocalEstoque;
    public double qtEstoque;
    public double qtEstoquePrevisto;
    public double qtEstoqueMin;
    public double qtEstoqueRemessa;
	public Date dtEstoquePrevisto;
    public String flOrigemEstoque;
    public String flTipoAlteracao;
	public String nmEmpresaCurto;
	
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
	
	public String getCdProduto() {
		return cdProduto;
	}
	
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
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
	
	public String getCdLocalEstoque() {
		return cdLocalEstoque;
	}
	
	public void setCdLocalEstoque(String cdLocalEstoque) {
		this.cdLocalEstoque = cdLocalEstoque;
	}
	
	public double getQtEstoque() {
		return qtEstoque;
	}
	
	public void setQtEstoque(double qtEstoque) {
		this.qtEstoque = qtEstoque;
	}
	
	public double getQtEstoquePrevisto() {
		return qtEstoquePrevisto;
	}
	
	public void setQtEstoquePrevisto(double qtEstoquePrevisto) {
		this.qtEstoquePrevisto = qtEstoquePrevisto;
	}
	
	public double getQtEstoqueMin() {
		return qtEstoqueMin;
	}
	
	public void setQtEstoqueMin(double qtEstoqueMin) {
		this.qtEstoqueMin = qtEstoqueMin;
	}
	
	public String getDtEstoquePrevisto() {
		return dtEstoquePrevisto == null ? null : DateUtil.formatDateDDMMYYYY(dtEstoquePrevisto);
	}
	
	public void setDtEstoquePrevisto(Date dtEstoquePrevisto) {
		this.dtEstoquePrevisto = dtEstoquePrevisto;
	}
	
	public String getFlOrigemEstoque() {
		return flOrigemEstoque;
	}
	
	public void setFlOrigemEstoque(String flOrigemEstoque) {
		this.flOrigemEstoque = flOrigemEstoque;
	}
	
	public String getFlTipoAlteracao() {
		return flTipoAlteracao;
	}
	
	public void setFlTipoAlteracao(String flTipoAlteracao) {
		this.flTipoAlteracao = flTipoAlteracao;
	}
 
	public double getQtEstoqueRemessa() {
		return qtEstoqueRemessa;
	}

	public void setQtEstoqueRemessa(double qtEstoqueRemessa) {
		this.qtEstoqueRemessa = qtEstoqueRemessa;
	}

	public String getNmEmpresaCurto() {
		return nmEmpresaCurto;
	}

	public void setNmEmpresaCurto(String nmEmpresaCurto) {
		this.nmEmpresaCurto = nmEmpresaCurto;
	}
}