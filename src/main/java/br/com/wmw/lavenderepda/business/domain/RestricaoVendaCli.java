package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RestricaoVendaCli extends BaseDomain {

	public static String TABLE_NAME = "TBLVPRESTRICAOVENDACLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRestricaoVendaProd;
    public String cdRestricaoVendaCli;
    public String flBloqueiaVenda;
    public String flBloqueiaAlteracaoPreco;
    
    public static final String RESTRICAO_PEDIDO_VENDA = "1";
    public static final String RESTRICAO_PEDIDO_BONIFICACAO = "2";

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RestricaoVendaCli) {
            RestricaoVendaCli restricaoVendaCli = (RestricaoVendaCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, restricaoVendaCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, restricaoVendaCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdRestricaoVendaProd, restricaoVendaCli.cdRestricaoVendaProd) &&
                ValueUtil.valueEquals(cdRestricaoVendaCli, restricaoVendaCli.cdRestricaoVendaCli);
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
        primaryKey.append(cdRestricaoVendaProd);
        primaryKey.append(";");
        primaryKey.append(cdRestricaoVendaCli);
        return primaryKey.toString();
    }
    
    public boolean isBloqueiaVenda() {
    	return ValueUtil.getBooleanValue(this.flBloqueiaVenda);
    }
    
    public boolean isBloqueiaAlteracaoPreco() {
    	return ValueUtil.getBooleanValue(this.flBloqueiaAlteracaoPreco);
    }

}