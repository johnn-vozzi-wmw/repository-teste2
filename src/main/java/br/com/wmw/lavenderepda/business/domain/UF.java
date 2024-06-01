package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class UF extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPUF";

    public String cdUf;
    public String dsUf;
    public String cdUfIbge;
    public String dsTimeZoneUf;
    
    public UF() {  }
    
    public UF(String cdUf) { 
    	this.cdUf = cdUf;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UF) {
            UF unidadeFederal = (UF) obj;
            return ValueUtil.valueEquals(cdUf, unidadeFederal.cdUf);
        }
        return false;
    }

	public String getPrimaryKey() {
		return cdUf;
	}

	public String getCdDomain() {
		return cdUf;
	}

	public String getDsDomain() {
		return dsUf;
	}
}