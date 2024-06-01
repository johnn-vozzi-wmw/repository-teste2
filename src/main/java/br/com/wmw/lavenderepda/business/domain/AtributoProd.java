package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class AtributoProd extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPATRIBUTOPROD";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdAtributoProduto;
    public String dsAtributoProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AtributoProd) {
            AtributoProd atributoProd = (AtributoProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, atributoProd.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, atributoProd.cdRepresentante) &&
                ValueUtil.valueEquals(cdAtributoProduto, atributoProd.cdAtributoProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdAtributoProduto);
        return  strBuffer.toString();
    }

	public String getCdDomain() {
		return cdAtributoProduto;
	}

	public String getDsDomain() {
		return dsAtributoProduto;
	}

}