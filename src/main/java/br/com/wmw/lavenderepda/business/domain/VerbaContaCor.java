package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaContaCor extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVERBACONTACOR";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdContaCorrente;
    public String dsContaCorrente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaContaCor) {
            VerbaContaCor verbaContaCorrente = (VerbaContaCor) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaContaCorrente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaContaCorrente.cdRepresentante) &&
                ValueUtil.valueEquals(cdContaCorrente, verbaContaCorrente.cdContaCorrente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdContaCorrente);
        return strBuffer.toString();
    }

}