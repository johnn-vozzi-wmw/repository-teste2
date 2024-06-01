package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import totalcross.util.Date;

public class EstoqueBuilder {

	// Parametros Obrigatórios
	public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdLocalEstoque;
    public double qtEstoque;
    public double qtEstoquePrevisto;
    public double qtEstoqueMin;
    public Date dtEstoquePrevisto;
    public String flOrigemEstoque;
    public String flModoEstoque;
    
	public EstoqueBuilder(String cdEmpresa, String cdRepresentante, String cdProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
	}
	
	public EstoqueBuilder(ProdutoBase produtoBase) {
		this(produtoBase.cdEmpresa, produtoBase.cdRepresentante, produtoBase.cdProduto);
	}
	
	public EstoqueBuilder(ProdutoBase produtoBase, boolean todosProdutos) {
		this(produtoBase.cdEmpresa, produtoBase.cdRepresentante, !todosProdutos ? produtoBase.cdProduto : null);
	}
	
	public EstoqueBuilder(ProdutoGrade produtoGrade) {
		this.cdEmpresa = produtoGrade.cdEmpresa;
		this.cdRepresentante = produtoGrade.cdRepresentante;
		this.cdProduto = produtoGrade.cdProduto;
		this.cdItemGrade1 = ValueUtil.isEmpty(produtoGrade.cdItemGrade1) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : produtoGrade.cdItemGrade1;
		this.cdItemGrade2 = ValueUtil.isEmpty(produtoGrade.cdItemGrade2) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : produtoGrade.cdItemGrade2;
		this.cdItemGrade3 = ValueUtil.isEmpty(produtoGrade.cdItemGrade3) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : produtoGrade.cdItemGrade3;
	}

	public EstoqueBuilder comQtEstoque(double newQtEstoque) {
		this.qtEstoque = newQtEstoque;
		return this;
	}
	
	public Estoque build() {
		return new Estoque(this, true);
	}

}
