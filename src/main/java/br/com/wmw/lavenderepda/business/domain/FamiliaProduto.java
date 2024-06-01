package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class FamiliaProduto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFAMILIAPRODUTO";

    public String cdEmpresa;
    public String cdFamilia;
    public String cdProduto;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FamiliaProduto) {
            FamiliaProduto familiaProduto = (FamiliaProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, familiaProduto.cdEmpresa) && 
                ValueUtil.valueEquals(cdFamilia, familiaProduto.cdFamilia) && 
                ValueUtil.valueEquals(cdProduto, familiaProduto.cdProduto);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdFamilia);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}