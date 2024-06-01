package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class ConfigIntegWebTc extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCONFIGINTEGWEBTC";

    public String dsTabela;
    public String dsChave;
    public String flRemessa;
    public String flRetorno;
    public String flAtivo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigIntegWebTc) {
            ConfigIntegWebTc configIntegWebTc = (ConfigIntegWebTc) obj;
            return
                ValueUtil.valueEquals(dsTabela, configIntegWebTc.dsTabela);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return  dsTabela;
    }

    public boolean isAtivo() {
    	return ValueUtil.VALOR_SIM.equals(flAtivo);
    }

    public boolean isRemessa() {
    	return ValueUtil.VALOR_SIM.equals(flRemessa);
    }

    public boolean isRetorno() {
    	return ValueUtil.VALOR_SIM.equals(flRetorno);
    }

    public String[] getChaves() {
    	return StringUtil.split(StringUtil.getStringValue(dsChave), ';');
    }
}