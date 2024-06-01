package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class Conjunto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCONJUNTO";

    public String cdEmpresa;
    public String cdConjunto;
    public String dsConjunto;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Conjunto) {
            Conjunto conjunto = (Conjunto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, conjunto.cdEmpresa) && 
                ValueUtil.valueEquals(cdConjunto, conjunto.cdConjunto);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdConjunto);
        return primaryKey.toString();
    }

}