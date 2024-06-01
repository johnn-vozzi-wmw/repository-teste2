package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RestricaoVendaUn extends BaseDomain {

	public static String TABLE_NAME = "TBLVPRESTRICAOVENDAUN";

	public static final String CDTIPOPEDIDO_VALOR_PADRAO = "0";
	public static final String CDTABELAPRECO_VALOR_PADRAO = "0";
	public static final String CDCLIENTE_VALOR_PADRAO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdUnidade;
    public String cdProduto;
    public String cdRestricaoVendaUn;
    public String cdCliente;
    public String cdTipoPedido;
    public String cdTabelaPreco;
    public String flBloqueiaVenda;

	public String cdTipoPedidoOrFilter;
	public String cdTabelaPrecoOrFilter;
	public String cdClienteOrFilter;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RestricaoVendaUn) {
            RestricaoVendaUn restricaoVendaUn = (RestricaoVendaUn) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, restricaoVendaUn.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, restricaoVendaUn.cdRepresentante) &&
                ValueUtil.valueEquals(cdUnidade, restricaoVendaUn.cdUnidade) &&
                ValueUtil.valueEquals(cdProduto, restricaoVendaUn.cdProduto) &&
                ValueUtil.valueEquals(cdRestricaoVendaUn, restricaoVendaUn.cdRestricaoVendaUn) &&
                ValueUtil.valueEquals(cdCliente, restricaoVendaUn.cdCliente);
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
        primaryKey.append(cdUnidade);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdRestricaoVendaUn);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }
    
    public boolean isBloqueiaVenda() {
    	return ValueUtil.getBooleanValue(flBloqueiaVenda);
    }

}