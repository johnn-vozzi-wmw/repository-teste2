package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VisitaPedido extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPVISITAPEDIDO";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdVisita;
    public String nuPedido;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VisitaPedido) {
            VisitaPedido visitaPedido = (VisitaPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, visitaPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, visitaPedido.cdRepresentante) && 
            	ValueUtil.valueEquals(nuPedido, visitaPedido.nuPedido) &&
            ValueUtil.valueEquals(cdVisita, visitaPedido.cdVisita);
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
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdVisita);
        return primaryKey.toString();
    }

}