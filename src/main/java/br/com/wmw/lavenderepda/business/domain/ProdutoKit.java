package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoKit extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTOKIT";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdKit;
    public String cdProduto;
    public double qtItemFisico;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoKit) {
            ProdutoKit produtoKit = (ProdutoKit) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoKit.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtoKit.cdRepresentante) &&
                ValueUtil.valueEquals(cdKit, produtoKit.cdKit) &&
                ValueUtil.valueEquals(cdProduto, produtoKit.cdProduto);
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
        primaryKey.append(cdKit);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}