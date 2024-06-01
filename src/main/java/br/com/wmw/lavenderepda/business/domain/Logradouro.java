package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Logradouro extends BaseDomain {

	public static String TABLE_NAME = "TBLVPLOGRADOURO";
	
    public String cdLogradouro;
    public String dsLogradouro;
    public String dsTipoLogradouro;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Logradouro) {
            Logradouro logradouro = (Logradouro) obj;
            return
                ValueUtil.valueEquals(cdLogradouro, logradouro.cdLogradouro);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdLogradouro);
        return primaryKey.toString();
    }
    
	public String getCdDomain() {
		return cdLogradouro;
	}

	public String getDsDomain() {
		return dsLogradouro;
	}

}