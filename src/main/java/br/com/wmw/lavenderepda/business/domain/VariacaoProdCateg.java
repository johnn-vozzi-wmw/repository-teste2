package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VariacaoProdCateg extends BaseDomain {

	public static String TABLE_NAME = "TBLVPVARIACAOPRODCATEG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdCategoria;
    public double vlPctVariacao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VariacaoProdCateg) {
            VariacaoProdCateg variacaoProdCategoria = (VariacaoProdCateg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, variacaoProdCategoria.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, variacaoProdCategoria.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, variacaoProdCategoria.cdProduto) &&
                ValueUtil.valueEquals(cdCategoria, variacaoProdCategoria.cdCategoria);
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
        primaryKey.append(cdCategoria);
        return primaryKey.toString();
    }

}