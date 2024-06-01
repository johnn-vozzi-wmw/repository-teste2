package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class CondPagtoTabPreco extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCONDPAGTOTABPRECO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String cdCondicaoPagamento;
    public Date dtInicial;
    public Date dtFinal;
    public double qtMinValor;
    public int qtMinProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondPagtoTabPreco) {
            CondPagtoTabPreco condPagtoTabPreco = (CondPagtoTabPreco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condPagtoTabPreco.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condPagtoTabPreco.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, condPagtoTabPreco.cdTabelaPreco) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condPagtoTabPreco.cdCondicaoPagamento);
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
    	strBuffer.append(cdCondicaoPagamento);
        return strBuffer.toString();
    }

}