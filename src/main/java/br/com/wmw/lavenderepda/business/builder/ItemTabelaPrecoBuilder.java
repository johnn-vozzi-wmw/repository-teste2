package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;

public class ItemTabelaPrecoBuilder {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdProduto;
	public String cdUf;
	
    
	public ItemTabelaPrecoBuilder(String cdEmpresa, String cdRepresentante, String cdTabelaPreco, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdTabelaPreco = cdTabelaPreco;
		this.cdProduto = cdProduto;
	}
	
	public ItemTabelaPrecoBuilder comCdUf(String newCdUf) {
		this.cdUf = newCdUf;
		return this;
	}
	
	public ItemTabelaPreco build() {
		return new ItemTabelaPreco(this);
	}

}
