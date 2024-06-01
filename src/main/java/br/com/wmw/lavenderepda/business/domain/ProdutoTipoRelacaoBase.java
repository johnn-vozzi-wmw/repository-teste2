package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public abstract class ProdutoTipoRelacaoBase extends BaseDomain {
	
	public static final String RELACAOEXCLUSIVA = "X";
	public static final String RELACAOEXCECAO = "E";
	public static final String RELACAORESTRICAO = "R";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
	public String flTipoRelacao; 
    
    //Nao persistentes
    public String[] flTipoRelacaoList;
    public boolean validandoCount;
    
    public abstract String getCdDomainEntidade();
    public abstract String getCdDomainEntidadeNomeColuna();
}
