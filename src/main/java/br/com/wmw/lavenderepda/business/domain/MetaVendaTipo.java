package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MetaVendaTipo extends BaseDomain {

	public static String TABLE_NAME = "TBLVPMETAVENDATIPO";

    public String cdEmpresa;
    public String cdMetaVenda;
    public String cdFamilia;
    public double vlMetaVenda;
    public double vlRealizadoVenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaVendaTipo) {
            MetaVendaTipo metaVendaTipo = (MetaVendaTipo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, metaVendaTipo.cdEmpresa) &&
                ValueUtil.valueEquals(cdMetaVenda, metaVendaTipo.cdMetaVenda) &&
                ValueUtil.valueEquals(cdFamilia, metaVendaTipo.cdFamilia);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMetaVenda);
        primaryKey.append(";");
        primaryKey.append(cdFamilia);
        return primaryKey.toString();
    }

}
