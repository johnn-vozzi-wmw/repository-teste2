package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RelInadimpCli extends BaseDomain {

    public static String TABLE_NAME = "TBLVPRELINADIMPCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public int qtTitulos;
    public double vlTitulos;
    public int qtDiasMaior;
    //--
    public String nmRazaoSocial;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RelInadimpCli) {
            RelInadimpCli relInadimpCli = (RelInadimpCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, relInadimpCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, relInadimpCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, relInadimpCli.cdCliente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
        return strBuffer.toString();
    }

    //@Override
    public String toString() {
    	return getDescription();
    }
}