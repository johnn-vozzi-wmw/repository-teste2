package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PrazoEntrega extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPPRAZOENTREGA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public double qtItem;
    public int nuDiasEntrega;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PrazoEntrega)) {
        	return false;
        }
        PrazoEntrega prazoEntrega = (PrazoEntrega) obj;
        return ValueUtil.valueEquals(cdEmpresa, prazoEntrega.cdEmpresa) 
        	&& ValueUtil.valueEquals(cdRepresentante, prazoEntrega.cdRepresentante) 
        	&& ValueUtil.valueEquals(qtItem, prazoEntrega.qtItem) 
        	&& ValueUtil.valueEquals(cdProduto, prazoEntrega.cdProduto);
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(qtItem);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}