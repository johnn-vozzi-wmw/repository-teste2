package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAgrSimilar;

public class ItemPedidoAgrSimilarDTO {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdProduto;
	public String cdProdutoSimilar;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public String cdAgrupadorSimilaridade;
	public double qtItemFisico;
	public double vlItemPedido;
	public double vlTotalItemPedido;
	public String dsProdutoSimilar;
	
	public ItemPedidoAgrSimilarDTO(ItemPedidoAgrSimilar itemPedidoAgrSimilar) {
		try {
			FieldMapper.copy(itemPedidoAgrSimilar, this);
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
	
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	
	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}
	
	public String getNuPedido() {
		return nuPedido;
	}
	
	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}
	
	public String getCdProduto() {
		return cdProduto;
	}
	
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}
	
	public String getCdProdutoSimilar() {
		return cdProdutoSimilar;
	}
	
	public void setCdProdutoSimilar(String cdProdutoSimilar) {
		this.cdProdutoSimilar = cdProdutoSimilar;
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
	
	public String getCdAgrupadorSimilaridade() {
		return cdAgrupadorSimilaridade;
	}
	
	public void setCdAgrupadorSimilaridade(String cdAgrupadorSimilaridade) {
		this.cdAgrupadorSimilaridade = cdAgrupadorSimilaridade;
	}
	
	public double getQtItemFisico() {
		return qtItemFisico;
	}
	
	public void setQtItemFisico(double qtItemFisico) {
		this.qtItemFisico = qtItemFisico;
	}

	public double getVlItemPedido() {
		return vlItemPedido;
	}

	public void setVlItemPedido(double vlItemPedido) {
		this.vlItemPedido = vlItemPedido;
	}

	public double getVlTotalItemPedido() {
		return vlTotalItemPedido;
	}

	public void setVlTotalItemPedido(double vlTotalItemPedido) {
		this.vlTotalItemPedido = vlTotalItemPedido;
	}

	public String getDsProdutoSimilar() {
		return dsProdutoSimilar;
	}

	public void setDsProdutoSimilar(String dsProdutoSimilar) {
		this.dsProdutoSimilar = dsProdutoSimilar;
	}
	
}
