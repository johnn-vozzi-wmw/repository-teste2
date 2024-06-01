package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VariacaoProdReg extends BaseDomain {

	public static String TABLE_NAME = "TBLVPVARIACAOPRODREG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdRegiao;
    public double vlPctVariacao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VariacaoProdReg) {
            VariacaoProdReg variacaoProdReg = (VariacaoProdReg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, variacaoProdReg.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, variacaoProdReg.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, variacaoProdReg.cdProduto) &&
                ValueUtil.valueEquals(cdRegiao, variacaoProdReg.cdRegiao);
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
        return primaryKey.toString();
    }

}