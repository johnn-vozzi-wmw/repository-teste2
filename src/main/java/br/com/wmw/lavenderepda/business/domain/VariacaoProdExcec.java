package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VariacaoProdExcec extends BaseDomain {

	public static String TABLE_NAME = "TBLVPVARIACAOPRODEXCEC";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdRegiao;
    public String cdCategoria;
    public double vlPctVariacao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VariacaoProdExcec) {
            VariacaoProdExcec variacaoProdExcec = (VariacaoProdExcec) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, variacaoProdExcec.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, variacaoProdExcec.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, variacaoProdExcec.cdProduto) &&
                ValueUtil.valueEquals(cdRegiao, variacaoProdExcec.cdRegiao) &&
                ValueUtil.valueEquals(cdCategoria, variacaoProdExcec.cdCategoria);
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
        primaryKey.append(cdRegiao);
        primaryKey.append(";");
        primaryKey.append(cdCategoria);
        return primaryKey.toString();
    }

}