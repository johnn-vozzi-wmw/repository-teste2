package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Bairro extends BaseDomain {

	public static String TABLE_NAME = "TBLVPBAIRRO";
	
	public String cdBairro;
    public String dsBairro;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Bairro) {
            Bairro bairro = (Bairro) obj;
            return
                ValueUtil.valueEquals(cdBairro, bairro.cdBairro);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdBairro);
        return primaryKey.toString();
    }

}