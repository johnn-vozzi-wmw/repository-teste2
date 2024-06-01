package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class LimiteOportunidade extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPLIMITEOPORTUNIDADE";

	public static final String LIMITE_PORTUNIDADE_PDA = "P";
	public static final String LIMITE_PORTUNIDADE_ERP = "E";

    public String cdEmpresa;
    public String cdRepresentante;
	public String flOrigemSaldo;
    public double vlLimite;
    public String flTipoLimite;
    public double vlSaldo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof LimiteOportunidade) {
            LimiteOportunidade limiteOportunidade = (LimiteOportunidade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, limiteOportunidade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, limiteOportunidade.cdRepresentante) &&
            ValueUtil.valueEquals(flOrigemSaldo, limiteOportunidade.flOrigemSaldo);
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
        primaryKey.append(flOrigemSaldo);
        return primaryKey.toString();
    }

}