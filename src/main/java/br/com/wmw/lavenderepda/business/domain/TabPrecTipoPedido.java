package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TabPrecTipoPedido extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPTABPRECTIPOPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String cdTipoPedido;
    
    //Nao persistentes
    public boolean excecaoTipoPedidoFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TabPrecTipoPedido) {
            TabPrecTipoPedido tabPrecTipoPedido = (TabPrecTipoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tabPrecTipoPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, tabPrecTipoPedido.cdRepresentante) && 
                ValueUtil.valueEquals(cdTabelaPreco, tabPrecTipoPedido.cdTabelaPreco) && 
                ValueUtil.valueEquals(cdTipoPedido, tabPrecTipoPedido.cdTipoPedido);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdTabelaPreco);
        primaryKey.append(";");
        primaryKey.append(cdTipoPedido);
        return primaryKey.toString();
    }

}