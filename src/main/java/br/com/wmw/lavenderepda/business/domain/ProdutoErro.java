package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoErro extends BaseDomain {
	
	public String cdProduto;
	public String dsProduto;
	public String dsMensagemErro;
	public String dsItemGrade1;
	public String dsItemGrade2;
	public String dsItemGrade3;
	
	public boolean isItemGrade;
	public String dsVlLimite;
	public String dsVlAtual;
	public Produto produto;
	public ItemPedido itemPedidoErro;
	
	public ProdutoErro(Produto produto, String cdProdutoItemPedido, String erro) {
		setCodigoEDescricaoProdutoErro(produto, cdProdutoItemPedido);
		this.produto = produto;
		this.dsProduto = produto.dsProduto;
		this.dsMensagemErro = erro;
	}

	public ProdutoErro(Produto produto, String cdProdutoItemPedido,  String erro, String dsItemGrade1, String dsItemGrade2, String dsItemGrade3) {
		setCodigoEDescricaoProdutoErro(produto, cdProdutoItemPedido);
		this.produto = produto;
		this.dsMensagemErro = erro;
		this.dsItemGrade1 = dsItemGrade1;
		this.dsItemGrade2 = dsItemGrade2;
		this.dsItemGrade3 = dsItemGrade3;
	}

	@Override
	public String getPrimaryKey() {
        return produto.getPrimaryKey();
	}
	
	private void setCodigoEDescricaoProdutoErro(Produto produto, String cdProdutoItemPedido) {
		if (produto != null && ValueUtil.isNotEmpty(produto.cdProduto)) {
			this.cdProduto = produto.cdProduto;
			this.dsProduto = produto.dsProduto;
		} else {
			this.cdProduto = cdProdutoItemPedido;
			this.dsProduto = "";
		}
		
	}

}
