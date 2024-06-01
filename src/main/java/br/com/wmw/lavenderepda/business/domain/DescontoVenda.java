package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;

public class DescontoVenda extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPDESCONTOVENDA";
	public static String NM_COLUNA_VLVENDA = "VLVENDA";

    public String cdEmpresa;
    public double vlVenda;
	public String cdUf;
    public double vlPctDesconto;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Não persistente
    public double vlVendaFilter;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescontoVenda) {
            DescontoVenda descontoVenda = (DescontoVenda) obj;
            return ValueUtil.valueEquals(cdEmpresa, descontoVenda.cdEmpresa) && ValueUtil.valueEquals(vlVenda, descontoVenda.vlVenda) && ValueUtil.valueEquals(cdUf, descontoVenda.cdUf);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(vlVenda);
        primaryKey.append(";");
        primaryKey.append(cdUf);
        return primaryKey.toString();
    }

    @Override
    public String toString() {
    	return Messages.ITEMPEDIDO_LABEL_TOTALVENDA + " " + StringUtil.getStringValueToInterface(vlVenda) + " | " + StringUtil.getStringValueToInterface(vlPctDesconto) + " " + Messages.DESCONTO_LABEL_VLPCTDESCONTO;
    }

}