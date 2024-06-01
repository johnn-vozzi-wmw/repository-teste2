package br.com.wmw.lavenderepda.business.domain;

import totalcross.util.Vector;

public class ProdutoCondPagto extends ProdutoTipoRelacaoBase {

	public static String TABLE_NAME = "TBLVPPRODUTOCONDPAGTO";
	public static final String NMCOLUNA_CDCONDICAOPAGAMENTO = "CDCONDICAOPAGAMENTO";
	
	public String cdCondicaoPagamento;
	
	//Nao persistentes
	public Vector cdProdutoFilterList;
	
	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		primaryKey.append(cdCondicaoPagamento);
		primaryKey.append(";");
		primaryKey.append(flTipoRelacao);
		return primaryKey.toString();
	}

	@Override
    public String getCdDomainEntidade() {
    	return this.cdCondicaoPagamento;
    }
	
	@Override
    public String getCdDomainEntidadeNomeColuna() {
    	return NMCOLUNA_CDCONDICAOPAGAMENTO;
    }
}
