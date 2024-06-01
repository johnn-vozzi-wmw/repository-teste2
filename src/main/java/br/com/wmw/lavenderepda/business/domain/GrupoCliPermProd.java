package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class GrupoCliPermProd extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPGRUPOCLIPERMPROD";

    public String cdGrupoCliente;
    public String cdProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoCliPermProd) {
            GrupoCliPermProd grupoCliPermProd = (GrupoCliPermProd) obj;
            return
                ValueUtil.valueEquals(cdGrupoCliente, grupoCliPermProd.cdGrupoCliente) && 
                ValueUtil.valueEquals(cdProduto, grupoCliPermProd.cdProduto);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdGrupoCliente);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}