package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoRelacionado extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTORELACIONADO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdProdutoRelacionado;
    public double vlPctVendaMin;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoRelacionado) {
            ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoRelacionado.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtoRelacionado.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, produtoRelacionado.cdProduto) &&
                ValueUtil.valueEquals(cdProdutoRelacionado, produtoRelacionado.cdProdutoRelacionado);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdProdutoRelacionado);
        return primaryKey.toString();
    }

}