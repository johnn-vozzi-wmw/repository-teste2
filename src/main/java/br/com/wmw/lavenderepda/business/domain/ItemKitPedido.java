package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemKitPedido extends BaseDomain {

    public static String TABLE_NAME_ITEMKITPEDIDO = "TBLVPITEMKITPEDIDO";
    public static String TABLE_NAME_ITEMKITPEDIDOERP = "TBLVPITEMKITPEDIDOERP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdKit;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public double qtItemFisico;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemKitPedido) {
            ItemKitPedido itemKitPedido = (ItemKitPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemKitPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemKitPedido.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, itemKitPedido.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, itemKitPedido.nuPedido) &&
                ValueUtil.valueEquals(cdKit, itemKitPedido.cdKit) &&
                ValueUtil.valueEquals(cdProduto, itemKitPedido.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, itemKitPedido.flTipoItemPedido) &&
                (nuSeqProduto == itemKitPedido.nuSeqProduto);
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
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdKit);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        return primaryKey.toString();
    }

}