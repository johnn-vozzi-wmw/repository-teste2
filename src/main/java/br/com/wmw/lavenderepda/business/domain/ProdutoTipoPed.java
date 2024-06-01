package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoTipoPed extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTOTIPOPED";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoPedido;
    public String cdProduto;
    public String nuCfopProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoTipoPed) {
            ProdutoTipoPed produtoTipoPed = (ProdutoTipoPed) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoTipoPed.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtoTipoPed.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoPedido, produtoTipoPed.cdTipoPedido) &&
                ValueUtil.valueEquals(cdProduto, produtoTipoPed.cdProduto);
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
        primaryKey.append(cdTipoPedido);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}