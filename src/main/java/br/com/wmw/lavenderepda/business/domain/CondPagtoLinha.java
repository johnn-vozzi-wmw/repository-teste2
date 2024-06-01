package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondPagtoLinha extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCONDPAGTOLINHA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoPagamento;
    public String cdLinha;
    public String cdRegiao;
    public int nuDiasPrazo;
    public double vlIndiceFinanceiro;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondPagtoLinha) {
            CondPagtoLinha condPagtoLinha = (CondPagtoLinha) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condPagtoLinha.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condPagtoLinha.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condPagtoLinha.cdCondicaoPagamento) &&
                ValueUtil.valueEquals(cdRegiao, condPagtoLinha.cdRegiao) &&
                ValueUtil.valueEquals(cdLinha, condPagtoLinha.cdLinha);
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
    	strBuffer.append(";");
    	strBuffer.append(cdLinha);
    	strBuffer.append(";");
    	strBuffer.append(cdRegiao);
        return strBuffer.toString();
    }

}