package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondComercialExcec extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONDCOMERCIALEXCEC";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdCondicaoComercial;
    public String cdItemGrade1;
    public double vlUnitario;
    public double vlBase;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondComercialExcec) {
        	CondComercialExcec condComercialExcec = (CondComercialExcec) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condComercialExcec.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condComercialExcec.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, condComercialExcec.cdProduto) &&
                ValueUtil.valueEquals(cdCondicaoComercial, condComercialExcec.cdCondicaoComercial) &&
            	ValueUtil.valueEquals(cdItemGrade1, condComercialExcec.cdItemGrade1);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoComercial);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade1);
        return primaryKey.toString();
    }

}