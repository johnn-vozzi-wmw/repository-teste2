package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class RotaEntrega extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPROTAENTREGA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRotaEntrega;
    public String dsRotaEntrega;
    public String dsDiasEntrega;
    
    public RotaEntrega() { }
    
    public RotaEntrega(String cdEmpresa, String cdRepresentante, String cdRotaEntrega, String dsRotaEntrega, String dsDiasEntrega) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdRotaEntrega = cdRotaEntrega;
		this.dsRotaEntrega = dsRotaEntrega;
		this.dsDiasEntrega = dsDiasEntrega;
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RotaEntrega) {
            RotaEntrega rotaEntrega = (RotaEntrega) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, rotaEntrega.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, rotaEntrega.cdRepresentante) &&
                ValueUtil.valueEquals(cdRotaEntrega, rotaEntrega.cdRotaEntrega);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdRotaEntrega);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdRotaEntrega;
	}

	public String getDsDomain() {
		return dsRotaEntrega;
	}
}