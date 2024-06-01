package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoItemGrade extends BaseDomain {

	public static final String GRADE_1 = "1";
	public static final String GRADE_2 = "2";
	public static final String GRADE_3 = "3";
	
	public static String TABLE_NAME = "TBLVPTIPOITEMGRADE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoItemGrade;
    public String dsTipoItemGrade;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoItemGrade) {
            TipoItemGrade tipoItemGrade = (TipoItemGrade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoItemGrade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipoItemGrade.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoItemGrade, tipoItemGrade.cdTipoItemGrade);
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
        primaryKey.append(cdTipoItemGrade);
        return primaryKey.toString();
    }
    
    @Override
    public String toString() {
    	return dsTipoItemGrade;
    }

}