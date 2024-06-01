package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoClienteCod extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPPRODUTOCLIENTECOD";
	
	public static final String FLACAO_INSERCAO = "I";
	public static final String FLACAO_ATUALIZACAO = "A";
	public static final String FLACAO_EXCLUSAO = "E";
	 
	public String cdRepresentante;
	public String cdEmpresa;
	public String cdCliente;
	public String cdProduto;
	public String cdProdutoCliente;
	
	//Não persistente
	public String dsProduto;
	public String dsPrincipioAtivo;
	public String dsRazaoSocialCliente;
	public boolean pedidoFechado;
	public String nuPedido;
	public boolean realizaBuscaItemPedidoErp;
	
	public ProdutoClienteCod() {
		
	}
	
	public ProdutoClienteCod(Cliente cliente) {
		cdEmpresa = cliente.cdEmpresa;
		cdRepresentante = cliente.cdRepresentante;
		cdCliente = cliente.cdCliente;
	}
	
	public ProdutoClienteCod(Cliente cliente, Produto produto) {
		this(cliente);
		this.dsRazaoSocialCliente = cliente.nmRazaoSocial;
		if (produto != null) {
			this.cdProduto = produto.cdProduto;
			this.dsProduto = produto.dsProduto;
			this.dsPrincipioAtivo = produto.dsPrincipioAtivo;
		}
	}
	
	public ProdutoClienteCod(String cdCliente, String nuPedido) {
		this.cdCliente = cdCliente;
		this.nuPedido = nuPedido;
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
		return primaryKey.toString();
	}
	
	public boolean isPossuiCodigoProdutoCliente() {
		return ValueUtil.isNotEmpty(cdProdutoCliente);
	}

}
