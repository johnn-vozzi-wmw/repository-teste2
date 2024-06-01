package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class RamoAtividade extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPRAMOATIVIDADE";
	
	public String cdEmpresa;
	public String cdRepresentante;
    public String cdRamoAtividade;
    public String dsRamoAtividade;
    
    public boolean equals(Object obj) {
        if (obj instanceof RamoAtividade) {
        	RamoAtividade classificFiscal = (RamoAtividade) obj;
            return
            ValueUtil.valueEquals(cdEmpresa, classificFiscal.cdEmpresa) &&
            ValueUtil.valueEquals(cdRepresentante, classificFiscal.cdRepresentante) &&
            ValueUtil.valueEquals(cdRamoAtividade, classificFiscal.cdRamoAtividade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdRamoAtividade);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdRamoAtividade;
	}

	public String getDsDomain() {
		return dsRamoAtividade;
	}


}
