package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondPagtoRep extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCONDPAGTOREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoPagamento;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondPagtoRep) {
            CondPagtoRep condicaoPagamentoRep = (CondPagtoRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condicaoPagamentoRep.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condicaoPagamentoRep.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condicaoPagamentoRep.cdCondicaoPagamento);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCondicaoPagamento);
        return strBuffer.toString();
    }
}