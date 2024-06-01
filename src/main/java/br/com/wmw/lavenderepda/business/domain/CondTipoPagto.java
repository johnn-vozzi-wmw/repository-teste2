package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondTipoPagto extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCONDTIPOPAGTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoPagamento;
    public String cdTipoPagamento;
    public double qtMinValorParcela;
    
    public CondTipoPagto() {
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CondTipoPagto) {
            CondTipoPagto condpagtotipopagto = (CondTipoPagto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condpagtotipopagto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condpagtotipopagto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condpagtotipopagto.cdCondicaoPagamento) &&
                ValueUtil.valueEquals(cdTipoPagamento, condpagtotipopagto.cdTipoPagamento);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCondicaoPagamento);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoPagamento);
        return strBuffer.toString();
    }

}