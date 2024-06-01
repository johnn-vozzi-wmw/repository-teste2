package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;

public class StatusCargaPedido extends BaseDomain {

	public static final String STATUS_CARGAPEDIDO_TODOS = "0";
	public static final String STATUS_CARGAPEDIDO_TRANSMITIDO = "1";
	public static final String STATUS_CARGAPEDIDO_FECHADO = "2";
	public static final String STATUS_CARGAPEDIDO_VENCIDO = "3";
	public static final String STATUS_CARGAPEDIDO_ABERTO = "4";

	public static final String STATUS_CARGAPEDIDO_DSTODOS = "---Todos---";
	public static final String STATUS_CARGAPEDIDO_DSTRANSMITIDO = "Transmitido";
	public static final String STATUS_CARGAPEDIDO_DSFECHADO = "Fechado";
	public static final String STATUS_CARGAPEDIDO_DSVENCIDO = "Vencido";
	public static final String STATUS_CARGAPEDIDO_DSABERTO = "Aberto";

	public String cdStatusCargaPedido;
	public String dsStatusCargaPedido;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusCargaPedido) {
            StatusCargaPedido statusCargaPedido = (StatusCargaPedido) obj;
            return (cdStatusCargaPedido.equals(statusCargaPedido.cdStatusCargaPedido));
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdStatusCargaPedido);
    }

    @Override
    public String toString() {
    	return dsStatusCargaPedido;
    }

    public static String getDsStatusCargaPedido(String cdStatusCargaPedido){
    	if (cdStatusCargaPedido.equals(STATUS_CARGAPEDIDO_TODOS)){
    		return STATUS_CARGAPEDIDO_DSTODOS;
    	} else if (cdStatusCargaPedido.equals(STATUS_CARGAPEDIDO_TRANSMITIDO)){
    		return STATUS_CARGAPEDIDO_DSTRANSMITIDO;
    	} else if (cdStatusCargaPedido.equals(STATUS_CARGAPEDIDO_FECHADO)){
    		return STATUS_CARGAPEDIDO_DSFECHADO;
    	} else if (cdStatusCargaPedido.equals(STATUS_CARGAPEDIDO_VENCIDO)){
    		return STATUS_CARGAPEDIDO_DSVENCIDO;
    	} else if (cdStatusCargaPedido.equals(STATUS_CARGAPEDIDO_ABERTO)){
    		return STATUS_CARGAPEDIDO_DSABERTO;
    	} else {
    		return "";
    	}
    }
}
