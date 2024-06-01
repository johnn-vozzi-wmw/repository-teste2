package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class SugestaoVendaRep extends BaseDomain {

	public static String TABLE_NAME = "TBLVPSUGESTAOVENDAREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSugestaoVenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof SugestaoVendaRep) {
            SugestaoVendaRep sugestaoVendaRep = (SugestaoVendaRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, sugestaoVendaRep.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, sugestaoVendaRep.cdRepresentante) &&
                ValueUtil.valueEquals(cdSugestaoVenda, sugestaoVendaRep.cdSugestaoVenda);
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
        primaryKey.append(cdSugestaoVenda);
        return primaryKey.toString();
    }

}