package br.com.wmw.lavenderepda.business.domain;

public class ProdutoTabPreco extends ProdutoBase {

    public static String TABLE_NAME = "TBLVPPRODUTOTABPRECO";

	public static final char SEPARADOR_CAMPOS = '|';

    public ProdutoTabPreco(String tableName) {
		super(tableName);
	}

    public ProdutoTabPreco() {
    	super(null);
	}

    public String dsTabPrecoPromoList;
    public String dsTabPrecoDescPromocionalList;
    public String dsTabPrecoOportunidadeList;

    //Não persistente
    public String flFiltraProdutoDescPromocional;
    public String flFiltraProdutoOportunidade;

    public double nuMultiploEspecialProduto;
    
}