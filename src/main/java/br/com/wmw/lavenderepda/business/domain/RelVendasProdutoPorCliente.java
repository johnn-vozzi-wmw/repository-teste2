package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BaseDomain;
import totalcross.util.Date;

public class RelVendasProdutoPorCliente extends BaseDomain {

	public String nmRazaoSocial;
	public Date dtEmissao;
	public double qtItemFisico;
	public String cdProduto;

	public RelVendasProdutoPorCliente() {

	}

	public String getPrimaryKey() {
		throw new RuntimeException("M�todo n�o implementado");
	}
}
