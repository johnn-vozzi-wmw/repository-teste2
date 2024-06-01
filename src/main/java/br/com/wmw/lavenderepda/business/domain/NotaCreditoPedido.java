package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class NotaCreditoPedido extends BaseDomain {
    
	public static final String TABLE_NAME = "TBLVPNOTACREDITOPEDIDO";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdNotaCredito;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public NotaCreditoPedido() {

    }
    
    public NotaCreditoPedido(Pedido pedido, String cdNotaCredito) {
		cdEmpresa = pedido.cdEmpresa;
		cdRepresentante = pedido.cdRepresentante;
		nuPedido = pedido.nuPedido;
		flOrigemPedido = pedido.flOrigemPedido;
		this.cdNotaCredito = cdNotaCredito;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof NotaCreditoPedido) {
            NotaCreditoPedido notacreditopedido = (NotaCreditoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, notacreditopedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, notacreditopedido.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, notacreditopedido.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, notacreditopedido.nuPedido) && 
                ValueUtil.valueEquals(cdNotaCredito, notacreditopedido.cdNotaCredito);
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
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdNotaCredito);
        return primaryKey.toString();
    }

}