package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoPedidoCli extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPTIPOPEDIDOCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoPedido;
    public String cdCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoPedidoCli) {
            TipoPedidoCli tipoPedidoCli = (TipoPedidoCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoPedidoCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, tipoPedidoCli.cdRepresentante) && 
                ValueUtil.valueEquals(cdTipoPedido, tipoPedidoCli.cdTipoPedido) && 
                ValueUtil.valueEquals(cdCliente, tipoPedidoCli.cdCliente);
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
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}