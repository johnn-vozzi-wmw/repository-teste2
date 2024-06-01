package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TranspTipoPed extends BaseDomain {

	public static String TABLE_NAME = "TBLVPTRANSPTIPOPED";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoPedido;
    public String cdTransportadora;
    public double vlMinPedidoFreteGratis;
    public double vlFreteMinimo;
    public String flCobraFreteAdicionalPorProd;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TranspTipoPed) {
            TranspTipoPed transpTipoPed = (TranspTipoPed) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, transpTipoPed.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, transpTipoPed.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoPedido, transpTipoPed.cdTipoPedido) &&
                ValueUtil.valueEquals(cdTransportadora, transpTipoPed.cdTransportadora);
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
        primaryKey.append(cdTipoPedido);
        primaryKey.append(";");
        primaryKey.append(cdTransportadora);
        return primaryKey.toString();
    }
    
    public boolean isFlCobraFreteAdicionalPorProd() {
    	return ValueUtil.VALOR_SIM.equalsIgnoreCase(flCobraFreteAdicionalPorProd);
    }

}