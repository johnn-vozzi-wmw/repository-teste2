package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotRegistroVisita extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPMOTREGISTROVISITA";

    public String cdEmpresa;
    public String cdMotivoRegistroVisita;
    public String dsMotivoRegistroVisita;
    public String flVisitaPositivada;
    public String flTipoCadastroCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotRegistroVisita) {
            MotRegistroVisita motregistrovisita = (MotRegistroVisita) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, motregistrovisita.cdEmpresa) &&
                ValueUtil.valueEquals(cdMotivoRegistroVisita, motregistrovisita.cdMotivoRegistroVisita);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdMotivoRegistroVisita);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdMotivoRegistroVisita;
	}

	public String getDsDomain() {
		return dsMotivoRegistroVisita;
	}

}