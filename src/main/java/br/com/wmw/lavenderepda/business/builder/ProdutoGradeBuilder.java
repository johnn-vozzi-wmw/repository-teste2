package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;

public class ProdutoGradeBuilder {

	// Parametros Obrigatórios
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdTabelaPreco;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdTipoItemGrade1;
    public String cdTipoItemGrade2;
    public String cdTipoItemGrade3;
    
	public ProdutoGradeBuilder(String cdEmpresa, String cdRepresentante, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
	}
	
	public ProdutoGradeBuilder(ProdutoBase produtoBase) {
		this(produtoBase, false);
	}
	
	public ProdutoGradeBuilder(ProdutoBase produtoBase, boolean todosProdutos) {
		this.cdEmpresa = produtoBase.cdEmpresa;
		this.cdRepresentante = produtoBase.cdRepresentante;
		if (!todosProdutos) {
			this.cdProduto = produtoBase.cdProduto;
		}
	}
	
	public ProdutoGradeBuilder() {
		this.cdEmpresa = SessionLavenderePda.cdEmpresa;
		this.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
	}

	public ProdutoGrade build() {
		return new ProdutoGrade(this);
	}
	
	public ProdutoGradeBuilder(ItemPedido itemPedido) {
    	this.cdEmpresa = itemPedido.cdEmpresa;
    	this.cdRepresentante = itemPedido.cdRepresentante;
    	this.cdProduto = itemPedido.cdProduto;
    	this.cdTabelaPreco = itemPedido.cdTabelaPreco;
    	this.cdItemGrade1 = itemPedido.cdItemGrade1;
    }

}
