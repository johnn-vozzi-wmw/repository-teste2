package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class FuncaoConfig extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFUNCAOCONFIG";

    public String cdFuncao;
    public double vlMinPedido;
    public String flBloqSistemaFeriado;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FuncaoConfig) {
            FuncaoConfig funcaoConfig = (FuncaoConfig) obj;
            return
                ValueUtil.valueEquals(cdFuncao, funcaoConfig.cdFuncao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdFuncao);
        return primaryKey.toString();
    }

}