package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RotaEntregaCli extends BaseDomain {

	public static String TABLE_NAME = "TBLVPROTAENTREGACLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRotaEntrega;
    public String cdCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RotaEntregaCli) {
            RotaEntregaCli rotaEntregaCli = (RotaEntregaCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, rotaEntregaCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, rotaEntregaCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdRotaEntrega, rotaEntregaCli.cdRotaEntrega) &&
                ValueUtil.valueEquals(cdCliente, rotaEntregaCli.cdCliente);
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
        primaryKey.append(cdRotaEntrega);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}