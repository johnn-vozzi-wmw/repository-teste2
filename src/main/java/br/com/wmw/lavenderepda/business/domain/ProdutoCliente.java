package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Vector;

public class ProdutoCliente extends ProdutoTipoRelacaoBase {

	public static String TABLE_NAME = "TBLVPPRODUTOCLIENTE";

	public static final String NMCOLUNA_CDCLIENTE = "CDCLIENTE";
	public static final String NMCOLUNA_VLPCTFIDELIDADE = "VLPCTFIDELIDADE";
	public static final String NMCOLUNA_VLRETORNOPRODUTO = "VLRETORNOPRODUTO";
	public static final String NMCOLUNA_VLPCTROYALT = "VLPCTROYALT";

	public String cdCliente;
    public double vlRetornoProduto;
    public double vlPctFidelidade;
    public double vlPctRoyalt;
    
    //Não persistente
    public Vector cdProdutoFilterList;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoCliente) {
            ProdutoCliente produtoCliente = (ProdutoCliente) obj;
            return ValueUtil.valueEquals(cdEmpresa, produtoCliente.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, produtoCliente.cdRepresentante)
					&& ValueUtil.valueEquals(cdProduto, produtoCliente.cdProduto)
					&& ValueUtil.valueEquals(cdCliente, produtoCliente.cdCliente);
        }
        return false;
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
        primaryKey.append(flTipoRelacao);
        return primaryKey.toString();
    }

    @Override
    public String getCdDomainEntidade() {
    	return this.cdCliente;
    }
    
    @Override
    public String getCdDomainEntidadeNomeColuna() {
    	return NMCOLUNA_CDCLIENTE;
    }
}