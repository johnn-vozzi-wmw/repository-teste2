package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class ProdutoClienteCodAtua extends BaseDomain  {
	
	public static String TABLE_NAME = "TBLVPPRODUTOCLIENTECODATUA";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public String cdProduto;
	public String cdProdutoClienteAtua;
	public String cdProdutoCliente;
	public String flAcao;
	
	public ProdutoClienteCodAtua() {
	}
	
	public ProdutoClienteCodAtua(ProdutoClienteCod produtoClienteCod, String cdProdutoClienteAtua) {
		this.cdEmpresa = produtoClienteCod.cdEmpresa;
		this.cdRepresentante = produtoClienteCod.cdRepresentante;
		this.cdCliente = produtoClienteCod.cdCliente;
		this.cdProduto = produtoClienteCod.cdProduto;
		this.cdProdutoCliente = produtoClienteCod.cdProdutoCliente;
		this.cdProdutoClienteAtua = cdProdutoClienteAtua;
	}
	
	public ProdutoClienteCodAtua(ProdutoClienteCod produtoClienteCod) {
		this.cdEmpresa = produtoClienteCod.cdEmpresa;
		this.cdRepresentante = produtoClienteCod.cdRepresentante;
		this.cdCliente = produtoClienteCod.cdCliente;
		this.cdProduto = produtoClienteCod.cdProduto;
		this.cdProdutoCliente = produtoClienteCod.cdProdutoCliente;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		primaryKey.append(cdCliente);
		primaryKey.append(";");
		primaryKey.append(cdProdutoClienteAtua);
		return primaryKey.toString();
	}

}
