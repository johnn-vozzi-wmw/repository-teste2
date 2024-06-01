package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoAtributo extends BaseDomain {

    public static String TABLE_NAME = "TBLVPPRODUTOATRIBUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdAtributoProduto;
    public String cdAtributoOpcaoProduto;
    public String cdProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoAtributo) {
            ProdutoAtributo produtoAtributo = (ProdutoAtributo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoAtributo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtoAtributo.cdRepresentante) &&
                ValueUtil.valueEquals(cdAtributoProduto, produtoAtributo.cdAtributoProduto) &&
                ValueUtil.valueEquals(cdAtributoOpcaoProduto, produtoAtributo.cdAtributoOpcaoProduto) &&
                ValueUtil.valueEquals(cdProduto, produtoAtributo.cdProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdAtributoProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdAtributoOpcaoProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return strBuffer.toString();
    }

}