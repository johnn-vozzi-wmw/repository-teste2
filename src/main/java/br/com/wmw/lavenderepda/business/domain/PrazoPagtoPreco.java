package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PrazoPagtoPreco extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPRAZOPAGTOPRECO";

    public String cdEmpresa;
    public String cdRepresentante;
    public int cdPrazoPagtoPreco;
    public int nuDiasPrazoPagtoPreco;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PrazoPagtoPreco) {
            PrazoPagtoPreco prazoPagtoPreco = (PrazoPagtoPreco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, prazoPagtoPreco.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, prazoPagtoPreco.cdRepresentante) &&
                ValueUtil.valueEquals(cdPrazoPagtoPreco, prazoPagtoPreco.cdPrazoPagtoPreco);
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
        primaryKey.append(cdPrazoPagtoPreco);
        return primaryKey.toString();
    }

}