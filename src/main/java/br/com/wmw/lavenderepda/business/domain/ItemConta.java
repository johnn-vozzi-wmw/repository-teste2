package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ItemConta extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPITEMCONTA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdItemConta;
    public String dsItemConta;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemConta) {
            ItemConta itemConta = (ItemConta) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemConta.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemConta.cdRepresentante) &&
                ValueUtil.valueEquals(cdItemConta, itemConta.cdItemConta);
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
        primaryKey.append(cdItemConta);
        return primaryKey.toString();
    }

	//@Override
	public String getCdDomain() {
		return cdItemConta;
	}

	//@Override
	public String getDsDomain() {
		return dsItemConta;
	}

}