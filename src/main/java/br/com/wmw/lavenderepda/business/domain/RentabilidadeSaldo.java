package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RentabilidadeSaldo extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPRENTABILIDADESALDO";

	public static String RENTABILIDADESALDO_PDA = "P";
	public static String RENTABILIDADESALDO_ERP = "E";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemSaldo;
    public double vlTotalRentabilidade;
    public double vlTotalItens;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RentabilidadeSaldo) {
            RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, rentabilidadeSaldo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, rentabilidadeSaldo.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemSaldo, rentabilidadeSaldo.flOrigemSaldo);
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