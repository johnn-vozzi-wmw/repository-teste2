package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondPagtoCli extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONDPAGTOCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdCondicaoPagamento;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondPagtoCli) {
            CondPagtoCli condpagtocli = (CondPagtoCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condpagtocli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condpagtocli.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, condpagtocli.cdCliente) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condpagtocli.cdCondicaoPagamento);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdCondicaoPagamento);
        return strBuffer.toString();
    }

}