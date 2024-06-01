package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class BonifCfgCategoria extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPBONIFCFGCATEGORIA";
	
	public String cdEmpresa;
    public String cdBonifCfg;
    public String cdCategoria;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdBonifCfg);
        primaryKey.append(";");
        primaryKey.append(cdCategoria);
        return primaryKey.toString();
	}

}
