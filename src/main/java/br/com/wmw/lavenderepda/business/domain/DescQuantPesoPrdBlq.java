package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescQuantPesoPrdBlq extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDESCQUANTPESOPRDBLQ";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String cdProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescQuantPesoPrdBlq) {
            DescQuantPesoPrdBlq descQuantidadePeso = (DescQuantPesoPrdBlq) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descQuantidadePeso.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, descQuantidadePeso.cdRepresentante) && 
                ValueUtil.valueEquals(cdRepresentante, descQuantidadePeso.cdTabelaPreco) && 
                ValueUtil.valueEquals(cdRepresentante, descQuantidadePeso.cdProduto);
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
        primaryKey.append(cdTabelaPreco);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}