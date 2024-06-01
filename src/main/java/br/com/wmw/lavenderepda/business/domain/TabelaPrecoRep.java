package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class TabelaPrecoRep extends BaseDomain {

    public static String TABLE_NAME = "TBLVPTABELAPRECOREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public double vlPctComissao;


    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TabelaPrecoRep) {
            TabelaPrecoRep tabelaPrecoRep = (TabelaPrecoRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tabelaPrecoRep.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tabelaPrecoRep.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, tabelaPrecoRep.cdTabelaPreco);
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
        return strBuffer.toString();
    }

}