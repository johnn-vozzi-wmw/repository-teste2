package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Bandeira extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPBANDEIRA";
	
	public String cdEmpresa;
    public String cdBandeira;
    public String dsBandeira;
    
    public boolean equals(Object obj) {
        if (obj instanceof Bandeira) {
        	Bandeira classificFiscal = (Bandeira) obj;
            return
            ValueUtil.valueEquals(cdEmpresa, classificFiscal.cdEmpresa) &&
            ValueUtil.valueEquals(cdBandeira, classificFiscal.cdBandeira);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdBandeira);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdBandeira;
	}

	public String getDsDomain() {
		return dsBandeira;
	}


}
