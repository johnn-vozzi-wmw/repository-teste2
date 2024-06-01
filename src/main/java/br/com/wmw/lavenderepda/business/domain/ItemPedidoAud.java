package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ItemPedidoAud extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPITEMPEDIDOAUD";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public double vlPctMargemItem;
    public double vlPctMargemRentabilidade;
    public double vlMargemItem;
    public double vlReceitaVirtual;
    public double vlReceitaLiquida;
    public double vlVerbaEmpresa;
    public double vlVerbaNecessaria;
    public double vlPctPis;
    public double vlPctIcms;
    public double vlItemPedidoNeutro;
    public Date dtInsercaoItem;
    public Date dtVerificacaoCusto;
    public String flGeraVerba;

    
    public ItemPedidoAud() {
		flGeraVerba = ValueUtil.VALOR_SIM;
	}
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedidoAud) {
            ItemPedidoAud itemPedidoAud = (ItemPedidoAud) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedidoAud.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemPedidoAud.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, itemPedidoAud.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, itemPedidoAud.flOrigemPedido) && 
                ValueUtil.valueEquals(cdProduto, itemPedidoAud.cdProduto) && 
                ValueUtil.valueEquals(flTipoItemPedido, itemPedidoAud.flTipoItemPedido) && 
                ValueUtil.valueEquals(nuSeqProduto, itemPedidoAud.nuSeqProduto);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        return primaryKey.toString();
    }

	public boolean isGeraVerba() {
		return ValueUtil.VALOR_SIM.equals(flGeraVerba);
	}

}