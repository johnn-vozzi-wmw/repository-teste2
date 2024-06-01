package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class HistoricoPedido extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPHISTORICOPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String cdStatus;
    public String flOrigemPedido;
    public Date dtAtualizacao;
    public String hrAtualizacao;
    //Nao persistentes
    public boolean ultimoHistorico;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof HistoricoPedido) {
            HistoricoPedido historicoPedido = (HistoricoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, historicoPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, historicoPedido.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, historicoPedido.nuPedido) && 
                ValueUtil.valueEquals(cdStatus, historicoPedido.cdStatus) && 
                ValueUtil.valueEquals(flOrigemPedido, historicoPedido.flOrigemPedido);
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
        primaryKey.append(cdStatus);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        return primaryKey.toString();
    }

}