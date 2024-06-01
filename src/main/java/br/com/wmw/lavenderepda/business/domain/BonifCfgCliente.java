package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class BonifCfgCliente extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPBONIFCFGCLIENTE";

    public String cdEmpresa;
    public String cdBonifCfg;
    public String cdCliente;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BonifCfgCliente) {
            BonifCfgCliente bonifCfgCliente = (BonifCfgCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, bonifCfgCliente.cdEmpresa) && 
                ValueUtil.valueEquals(cdBonifCfg, bonifCfgCliente.cdBonifCfg) && 
                ValueUtil.valueEquals(cdCliente, bonifCfgCliente.cdCliente);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdBonifCfg);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}