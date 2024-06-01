package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class TipoEntrega extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPTIPOENTREGA";

    public String cdEmpresa;
    public String cdTipoentrega;
    public String dsTipoentrega;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoEntrega) {
            TipoEntrega tipoentrega = (TipoEntrega) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoentrega.cdEmpresa) &&
                ValueUtil.valueEquals(cdTipoentrega, tipoentrega.cdTipoentrega);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoentrega);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdTipoentrega;
	}

	public String getDsDomain() {
		return dsTipoentrega;
	}

}