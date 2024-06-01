package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.lavenderepda.business.domain.PedidoErpDif;

public class PedidoErpDifDTO extends PedidoErpDif {
	
	public ItemPedidoErpDifDTO[] itemPedidoErpDifList;
	
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
	
	public String getCdStatusPedido() {
		return cdStatusPedido;
	}
	
	public void setCdStatusPedido(String cdStatusPedido) {
		this.cdStatusPedido = cdStatusPedido;
	}
	
	public String getDsObservacaoOrg() {
		return dsObservacaoorg;
	}
	
	public void setDsObservacaoOrg(String dsObservacaoorg) {
		this.dsObservacaoorg = dsObservacaoorg;
	}
	
	public String getDsObservacaoErp() {
		return dsObservacaoerp;
	}
	
	public void setDsObservacaoErp(String dsObservacaoerp) {
		this.dsObservacaoerp = dsObservacaoerp;
	}

	public ItemPedidoErpDifDTO[] getItemPedidoErpDifList() {
		return itemPedidoErpDifList;
	}

	public void setItemPedidoErpDifList(ItemPedidoErpDifDTO[] itemPedidoErpDifList) {
		this.itemPedidoErpDifList = itemPedidoErpDifList;
	}
	
}
