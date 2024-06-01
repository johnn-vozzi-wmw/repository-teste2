package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaUsuario extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPVERBAUSUARIO";
    
    public String flOrigemSaldo;
    public double vlSaldo;
    public double vlSaldoOrg;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaUsuario) {
            VerbaUsuario verbaUsuario = (VerbaUsuario) obj;
            return
                ValueUtil.valueEquals(flOrigemSaldo, verbaUsuario.flOrigemSaldo) && 
                ValueUtil.valueEquals(cdUsuario, verbaUsuario.cdUsuario);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(flOrigemSaldo);
        primaryKey.append(";");
        return primaryKey.toString();
    }

}