package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Familia extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPFAMILIA";

    public String cdEmpresa;
    public String cdFamilia;
    public String dsFamilia;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Familia) {
            Familia familia = (Familia) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, familia.cdEmpresa) &&
                ValueUtil.valueEquals(cdFamilia, familia.cdFamilia);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdFamilia);
        return primaryKey.toString();
    }

	public String getCdDomain() {
		return cdFamilia;
	}

	public String getDsDomain() {
		return dsFamilia;
	}

}