package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondComCondPagto extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONDCOMCONDPAGTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoComercial;
    public String cdCondicaoPagamento;
    public double vlIndiceFinanceiro;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondComCondPagto) {
            CondComCondPagto condComCondPagto = (CondComCondPagto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condComCondPagto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condComCondPagto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoComercial, condComCondPagto.cdCondicaoComercial) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condComCondPagto.cdCondicaoPagamento);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoComercial);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoPagamento);
        return primaryKey.toString();
    }

}