package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class IndiceGrupoProd extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPINDICEGRUPOPROD";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdGrupoProduto1;
	public String cdGrupoProduto2;
	public String cdGrupoProduto3;
	public String cdGrupoProduto4;
	public String cdGrupoProduto5;
	public String cdTabelaPreco;
	public String cdCondicaoPagamento;
	public double vlIndiceFinanceiro;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdGrupoProduto1 + ";" + cdGrupoProduto2 + ";" + cdGrupoProduto3 + ";" + cdGrupoProduto4 + ";" + cdGrupoProduto5 + ";" + cdTabelaPreco + ";" + cdCondicaoPagamento;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IndiceGrupoProd)) {
			return false;
		}
		IndiceGrupoProd indiceGrupoProd = (IndiceGrupoProd) obj;
		return cdEmpresa.equals(indiceGrupoProd.cdEmpresa) &&
				cdRepresentante.equals(indiceGrupoProd.cdRepresentante) &&
				cdGrupoProduto1.equals(indiceGrupoProd.cdGrupoProduto1) &&
				cdGrupoProduto2.equals(indiceGrupoProd.cdGrupoProduto2) &&
				cdGrupoProduto3.equals(indiceGrupoProd.cdGrupoProduto3) &&
				cdGrupoProduto4.equals(indiceGrupoProd.cdGrupoProduto4) &&
				cdGrupoProduto5.equals(indiceGrupoProd.cdGrupoProduto5) &&
				cdTabelaPreco.equals(indiceGrupoProd.cdTabelaPreco);
	}

}
