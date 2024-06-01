package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;

public class ItemNfceDTO {
	
	public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdProduto;
    public int nuSeqProduto;
    public double qtItemFisico;
    public String cdUnidade;
    public double vlUnitario;
    public double vlTotalItem;
    
    public ItemNfceDTO () {}
    
    public ItemNfceDTO(ItemNfce itemNfce) {
    	try {
			FieldMapper.copy(itemNfce, this);
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

	public String getCdProduto() {
		return cdProduto;
	}

	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}

	public int getNuSeqProduto() {
		return nuSeqProduto;
	}

	public void setNuSeqProduto(int nuSeqProduto) {
		this.nuSeqProduto = nuSeqProduto;
	}

	public double getQtItemFisico() {
		return qtItemFisico;
	}

	public void setQtItemFisico(double qtItemFisico) {
		this.qtItemFisico = qtItemFisico;
	}

	public String getCdUnidade() {
		return cdUnidade;
	}

	public void setCdUnidade(String cdUnidade) {
		this.cdUnidade = cdUnidade;
	}

	public double getVlUnitario() {
		return vlUnitario;
	}

	public void setVlUnitario(double vlUnitario) {
		this.vlUnitario = vlUnitario;
	}

	public double getVlTotalItem() {
		return vlTotalItem;
	}

	public void setVlTotalItem(double vlTotalItem) {
		this.vlTotalItem = vlTotalItem;
	}
    
}
