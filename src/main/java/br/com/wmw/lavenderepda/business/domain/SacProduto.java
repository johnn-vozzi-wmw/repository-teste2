package br.com.wmw.lavenderepda.business.domain;

import java.util.Objects;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class SacProduto extends BaseDomain {
	
	public SacProduto() {
		super();
	}

	public static String TABLE_NAME = "TBLVPSACPRODUTO";

    public String cdEmpresa;
    public String cdSac;
    public String cdProduto;
   
    public Produto produto;

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdSac);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SacProduto) {
			SacProduto sacProduto = (SacProduto) obj;
			return ValueUtil.valueEquals(cdEmpresa, sacProduto.cdEmpresa)
					&& ValueUtil.valueEquals(cdSac, sacProduto.cdSac)
					&& ValueUtil.valueEquals(cdProduto, sacProduto.cdProduto);
		}
		return false;
	}

}