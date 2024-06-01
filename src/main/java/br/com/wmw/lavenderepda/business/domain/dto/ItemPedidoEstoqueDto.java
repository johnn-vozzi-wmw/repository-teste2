package br.com.wmw.lavenderepda.business.domain.dto;

public class ItemPedidoEstoqueDto {
	
	public static final String TABLE_NAME = "TBLVPITEMPEDIDOESTOQUE"; 
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPedidoEstoque;
	public String cdProduto;
	public double qtRemessa;

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

	public String getCdPedidoEstoque() {
		return cdPedidoEstoque;
	}

	public void setCdPedidoEstoque(String cdPedidoEstoque) {
		this.cdPedidoEstoque = cdPedidoEstoque;
	}

	public String getCdProduto() {
		return cdProduto;
	}

	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}

	public double getQtRemessa() {
		return qtRemessa;
	}

	public void setQtRemessa(double qtRemessa) {
		this.qtRemessa = qtRemessa;
	}

}
