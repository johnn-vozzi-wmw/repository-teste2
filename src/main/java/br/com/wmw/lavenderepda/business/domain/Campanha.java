package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Campanha extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPCAMPANHA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCampanha;
    public String dsCampanha;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Campanha) {
            Campanha campanha = (Campanha) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, campanha.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, campanha.cdRepresentante) &&
                ValueUtil.valueEquals(cdCampanha, campanha.cdCampanha);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCampanha);
        return  strBuffer.toString();
    }

	public String getCdDomain() {
		return cdCampanha;
	}

	public String getDsDomain() {
		return dsCampanha;
	}

}