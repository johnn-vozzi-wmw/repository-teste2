package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ProdutoMargem extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPPRODUTOMARGEM";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdRamoAtividade;
    public double vlPctMargemDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoMargem) {
            ProdutoMargem produtoMargem = (ProdutoMargem) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoMargem.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, produtoMargem.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, produtoMargem.cdProduto) && 
                ValueUtil.valueEquals(cdRamoAtividade, produtoMargem.cdRamoAtividade);
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
        primaryKey.append(cdRamoAtividade);
        return primaryKey.toString();
    }

}