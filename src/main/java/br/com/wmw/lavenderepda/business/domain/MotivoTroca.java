package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotivoTroca extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPMOTIVOTROCA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdMotivotroca;
    public String dsMotivotroca;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoTroca) {
            MotivoTroca motivotroca = (MotivoTroca) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, motivotroca.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, motivotroca.cdRepresentante) &&
                ValueUtil.valueEquals(cdMotivotroca, motivotroca.cdMotivotroca);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdMotivotroca);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdMotivotroca;
	}

	public String getDsDomain() {
		return dsMotivotroca;
	}

}