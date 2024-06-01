package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RepresentanteEmp extends BaseDomain {

    public static String TABLE_NAME = "TBLVPREPRESENTANTEEMP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flDefault;


    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RepresentanteEmp) {
            RepresentanteEmp representanteEmp = (RepresentanteEmp) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, representanteEmp.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, representanteEmp.cdRepresentante);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
        return strBuffer.toString();
    }

    public boolean isDefault() {
    	return ValueUtil.VALOR_SIM.equals(flDefault);
    }

}