package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.business.domain.Produto;

public class ProdutoBuilder {

	// Parametros Obrigatórios
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    
    public String cdFornecedor;
    
	public ProdutoBuilder(String cdEmpresa, String cdRepresentante, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
	}
	
	public ProdutoBuilder padrao(String newCdEmpresa, String newCdRepresentante, String newCdProduto) {
		return new ProdutoBuilder(newCdEmpresa, newCdRepresentante, newCdProduto);
	}

	public ProdutoBuilder comCdFornecedor(String valor) {
		cdFornecedor = valor;
		return this;
	}
	
	public Produto build() {
		return new Produto(this);
	}

}
