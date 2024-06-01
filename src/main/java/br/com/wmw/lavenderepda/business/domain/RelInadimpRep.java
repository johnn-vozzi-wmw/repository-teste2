package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class RelInadimpRep extends BaseDomain {

    public static String TABLE_NAME = "TBLVPRELINADIMPREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public int qtClientes;
    public int qtTitulos;
    public double vlTitulos;
    //Não Persistente
    public String nmRepresentante;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RelInadimpRep) {
            RelInadimpRep relInadimpRep = (RelInadimpRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, relInadimpRep.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, relInadimpRep.cdRepresentante);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
        return strBuffer.toString();
    }

    //@Override
    public String toString() {
    	return StringUtil.getStringValue(cdRepresentante);
    }
}