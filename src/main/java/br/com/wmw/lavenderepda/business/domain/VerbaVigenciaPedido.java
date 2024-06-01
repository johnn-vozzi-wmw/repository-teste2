package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaVigenciaPedido extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPVERBAVIGENCIAPEDIDO";
	public static final String NMCOLUNA_VLSALDO = "VLSALDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public int cdMesSaldo;
    public String nuPedido;
    public double vlSaldo;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaVigenciaPedido) {
            VerbaVigenciaPedido verbaVigenciaPedido = (VerbaVigenciaPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaVigenciaPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, verbaVigenciaPedido.cdRepresentante) && 
                ValueUtil.valueEquals(cdMesSaldo, verbaVigenciaPedido.cdMesSaldo) && 
                ValueUtil.valueEquals(nuPedido, verbaVigenciaPedido.nuPedido);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdMesSaldo);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        return primaryKey.toString();
    }

}