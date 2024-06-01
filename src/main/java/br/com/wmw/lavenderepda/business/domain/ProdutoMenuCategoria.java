package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class ProdutoMenuCategoria extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTOMENUCATEGORIA";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdMenu;
	public String cdProduto;
	public String dtAlteracao;
	public String hrAlteracao;
	
	//Nao persistentes
	public String[] cdMenuListFilter;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdMenu + ";" + cdProduto + ";" + cdUsuario;
	}

}
