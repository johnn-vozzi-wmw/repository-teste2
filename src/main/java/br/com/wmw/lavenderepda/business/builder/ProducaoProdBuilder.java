package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.business.domain.ProducaoProd;

public class ProducaoProdBuilder {
	
	public String cdEmpresa;
	public String cdProduto;
	
	
	
	public ProducaoProdBuilder(String cdEmpresa, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdProduto = cdProduto;
	}


	public ProducaoProd build() {
		return new ProducaoProd(this);
	}
}
