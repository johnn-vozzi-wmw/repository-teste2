package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescProgressivo extends BaseDomain {

    public static String TABLE_NAME = "TBLVPDESCPROGRESSIVO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String cdCondicaoComercial;
    public double vlInicioFaixa;
    public double vlFinalFaixa;
    public double vlPctDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescProgressivo) {
            DescProgressivo descontoProgressivo = (DescProgressivo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descontoProgressivo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, descontoProgressivo.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, descontoProgressivo.cdTabelaPreco) &&
                ValueUtil.valueEquals(cdCondicaoComercial, descontoProgressivo.cdCondicaoComercial) &&
                (vlInicioFaixa == descontoProgressivo.vlInicioFaixa);
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
    	strBuffer.append(cdCondicaoComercial);
    	strBuffer.append(";");
    	strBuffer.append(vlInicioFaixa);
        return strBuffer.toString();
    }

}