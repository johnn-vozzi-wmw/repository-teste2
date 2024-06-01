package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondPagtoProd extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCONDPAGTOPROD";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoPagamento;
    public String cdProduto;
    public double vlIndiceFinanceiro;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondPagtoProd) {
            CondPagtoProd condPagtoProd = (CondPagtoProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condPagtoProd.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, condPagtoProd.cdRepresentante) && 
                ValueUtil.valueEquals(cdCondicaoPagamento, condPagtoProd.cdCondicaoPagamento) && 
                ValueUtil.valueEquals(cdProduto, condPagtoProd.cdProduto);
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
        primaryKey.append(cdCondicaoPagamento);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}