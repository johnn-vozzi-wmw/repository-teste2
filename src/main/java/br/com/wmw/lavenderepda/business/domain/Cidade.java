package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Cidade extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCIDADE";
	
    public String cdCidade;
    public String nmCidade;
    public String cdUf;
    public String flCapital;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Cidade) {
            Cidade cidade = (Cidade) obj;
            return
                ValueUtil.valueEquals(cdCidade, cidade.cdCidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdCidade);
        return primaryKey.toString();
    }

}