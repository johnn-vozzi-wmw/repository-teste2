package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FotoPedido extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPFOTOPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String nmFoto;
    public int nuTamanho;
    public Date dtModificacao;
    public String flEnviadoServidor;
    //--
    public boolean forceFilter;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FotoPedido) {
            FotoPedido fotoPedido = (FotoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fotoPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, fotoPedido.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, fotoPedido.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, fotoPedido.flOrigemPedido) && 
                ValueUtil.valueEquals(nmFoto, fotoPedido.nmFoto);
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
    
    public static String getPathImg() {
		return FotoUtil.getPathImg(FotoPedido.class);
	}
    
    public boolean isFotoPedidoEnviadaServidor() {
    	return ValueUtil.VALOR_SIM.equals(flEnviadoServidor);
	}

}