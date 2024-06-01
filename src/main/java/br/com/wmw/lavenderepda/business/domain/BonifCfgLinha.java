package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class BonifCfgLinha extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPBONIFCFGLINHA";
	
	public String cdEmpresa;
    public String cdBonifCfg;
    public String cdLinha;
    public String flGeraBonificacao;
    public String flPermiteBonificar;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdBonifCfg);
        primaryKey.append(";");
        primaryKey.append(cdLinha);
        return primaryKey.toString();
	}
}
