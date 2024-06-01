package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class ProdutoBloqueado extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTOBLOQUEADO";

	public static final String CDTABELAPRECO_DEFAULT = "0";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdProduto;
	public String cdTabelaPreco;
	public String cdUsuarioBloqueio;
	
	//Não persistente
	public String[] cdTabelaPrecoInFilter;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdProduto + ";" + cdTabelaPreco;
	}


}
