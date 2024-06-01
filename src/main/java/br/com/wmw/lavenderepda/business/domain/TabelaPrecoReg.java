package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TabelaPrecoReg extends BaseDomain {

    public static String TABLE_NAME = "TBLVPTABELAPRECOREG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String cdRegiao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TabelaPrecoReg) {
            TabelaPrecoReg tabelaPrecoReg = (TabelaPrecoReg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tabelaPrecoReg.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tabelaPrecoReg.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, tabelaPrecoReg.cdTabelaPreco) &&
                ValueUtil.valueEquals(cdRegiao, tabelaPrecoReg.cdRegiao);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	strBuffer.append(cdRegiao);
        return strBuffer.toString();
    }

}