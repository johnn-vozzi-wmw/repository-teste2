package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class AtributoOpcaoProd extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPATRIBUTOOPCAOPROD";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdAtributoProduto;
    public String cdAtributoOpcaoProduto;
    public String dsAtributoOpcaoProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AtributoOpcaoProd) {
            AtributoOpcaoProd atributoOpcaoProd = (AtributoOpcaoProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, atributoOpcaoProd.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, atributoOpcaoProd.cdRepresentante) &&
                ValueUtil.valueEquals(cdAtributoProduto, atributoOpcaoProd.cdAtributoProduto) &&
                ValueUtil.valueEquals(cdAtributoOpcaoProduto, atributoOpcaoProd.cdAtributoOpcaoProduto);
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
    	strBuffer.append(";");
    	strBuffer.append(cdAtributoOpcaoProduto);
        return  strBuffer.toString();
    }

	public String getCdDomain() {
		return cdAtributoOpcaoProduto;
	}

	public String getDsDomain() {
		return dsAtributoOpcaoProduto;
	}

}