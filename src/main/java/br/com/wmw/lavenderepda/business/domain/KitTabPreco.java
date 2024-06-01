package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class KitTabPreco extends BaseDomain {

    public static String TABLE_NAME = "TBLVPKITTABPRECO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdKit;
    public String cdTabelaPreco;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof KitTabPreco) {
            KitTabPreco kitTabPreco = (KitTabPreco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, kitTabPreco.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, kitTabPreco.cdRepresentante) &&
                ValueUtil.valueEquals(cdKit, kitTabPreco.cdKit) &&
                ValueUtil.valueEquals(cdTabelaPreco, kitTabPreco.cdTabelaPreco);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdKit);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
        return strBuffer.toString();
    }

}