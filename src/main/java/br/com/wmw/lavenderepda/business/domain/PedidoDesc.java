package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PedidoDesc extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPEDIDODESC";
	
	public static String NMCOLUNA_NUSEQUENCIA = "nuSequencia";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdUsuarioLiberacao;
    public String nmUsuario;
    public int nuSequencia;
    public double vlPctDescontoLiberado;
    public double vlDescontoLiberado;
    public Date dtLiberacao;
    public String hrLiberacao;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PedidoDesc) {
            PedidoDesc pedidoDesc = (PedidoDesc) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pedidoDesc.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, pedidoDesc.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, pedidoDesc.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, pedidoDesc.nuPedido) &&
                ValueUtil.valueEquals(cdUsuarioLiberacao, pedidoDesc.cdUsuarioLiberacao);
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
        primaryKey.append(cdUsuarioLiberacao);
        return primaryKey.toString();
    }

}