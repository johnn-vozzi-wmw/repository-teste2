package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaTabelaPreco extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVERBATABELAPRECO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdVerba;
    public String cdTabelaPreco;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaTabelaPreco) {
            VerbaTabelaPreco verbaTabelaPreco = (VerbaTabelaPreco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaTabelaPreco.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaTabelaPreco.cdRepresentante) &&
                ValueUtil.valueEquals(cdVerba, verbaTabelaPreco.cdVerba) &&
                ValueUtil.valueEquals(cdTabelaPreco, verbaTabelaPreco.cdTabelaPreco);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdVerba);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
        return strBuffer.toString();
    }

}