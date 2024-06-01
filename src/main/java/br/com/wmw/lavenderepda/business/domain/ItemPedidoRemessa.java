package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ItemPedidoRemessa extends BaseDomain {

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdLocalEstoque;
    public String nuNotaRemessa;
    public String nuSerieRemessa;
    public double qtEstoqueConsumido;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedidoRemessa) {
            ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedidoRemessa.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemPedidoRemessa.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, itemPedidoRemessa.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, itemPedidoRemessa.nuPedido) && 
                ValueUtil.valueEquals(cdProduto, itemPedidoRemessa.cdProduto) && 
                ValueUtil.valueEquals(flTipoItemPedido, itemPedidoRemessa.flTipoItemPedido) && 
                ValueUtil.valueEquals(nuSeqProduto, itemPedidoRemessa.nuSeqProduto) && 
                ValueUtil.valueEquals(cdLocalEstoque, itemPedidoRemessa.cdLocalEstoque) && 
                ValueUtil.valueEquals(nuNotaRemessa, itemPedidoRemessa.nuNotaRemessa) && 
                ValueUtil.valueEquals(nuSerieRemessa, itemPedidoRemessa.nuSerieRemessa);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        primaryKey.append(";");
        primaryKey.append(cdLocalEstoque);
        primaryKey.append(";");
        primaryKey.append(nuNotaRemessa);
        primaryKey.append(";");
        primaryKey.append(nuSerieRemessa);
        return primaryKey.toString();
    }

}