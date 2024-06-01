package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FotoPedidoErp extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFOTOPEDIDOERP";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String nmFoto;
    public String flEnviadoServidor;
    public int nuTamanho;
    public Date dtModificacao;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoPedidoErp) {
            FotoPedidoErp fotoPedidoErp = (FotoPedidoErp) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fotoPedidoErp.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, fotoPedidoErp.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, fotoPedidoErp.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, fotoPedidoErp.flOrigemPedido) && 
                ValueUtil.valueEquals(nmFoto, fotoPedidoErp.nmFoto);
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
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nmFoto);
        return primaryKey.toString();
    }

}