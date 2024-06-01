package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class BonifCfgRepresentante extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPBONIFCFGREPRESENTANTE";
	
	public String cdEmpresa;
    public String cdBonifCfg;
    public String cdRepresentante;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdBonifCfg);
        return primaryKey.toString();
	}

}
