package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondTipoPedido extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONDTIPOPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoPagamento;
    public String cdTipoPedido;
    
    //Nao Persistentes
    public boolean excecaoCondPagtoFilter;
    
    public CondTipoPedido() {
    }
    
    public CondTipoPedido(String cdEmpresa, String cdRepresentante, String cdTipoPedido) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdTipoPedido = cdTipoPedido;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondTipoPedido) {
            CondTipoPedido condTipoPedido = (CondTipoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condTipoPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condTipoPedido.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condTipoPedido.cdCondicaoPagamento) &&
                ValueUtil.valueEquals(cdTipoPedido, condTipoPedido.cdTipoPedido);
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
        primaryKey.append(cdCondicaoPagamento);
        primaryKey.append(";");
        primaryKey.append(cdTipoPedido);
        return primaryKey.toString();
    }

}