package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoErpDifDTO;
import totalcross.util.Date;

public class ItemPedidoErpDif extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPITEMPEDIDOERPDIF";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public int nuSeqItemPedido;
    public double qtItemfisicoOrg;
    public double qtItemFisicoErp;
    public String dsObservacaoOrg;
    public String dsObservacaoErp;
    public String cdUnidade;
    public Produto produto;

    //Não Persistente
    public Date dtEmissaoPedido;
    
    public ItemPedidoErpDif() {
	}
    
    public ItemPedidoErpDif(ItemPedidoErpDifDTO itemPedidoErpDifDTO) {
    	this();
    	try {
    		FieldMapper.copy(itemPedidoErpDifDTO, this);
    	} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    public ItemPedidoErpDif(ItemPedido itemPedido) {
		this.cdEmpresa = itemPedido.cdEmpresa;
		this.cdRepresentante = itemPedido.cdRepresentante;
		this.flOrigemPedido = itemPedido.flOrigemPedido;
		this.nuPedido = itemPedido.nuPedido;
		this.cdProduto = itemPedido.cdProduto;
		this.flTipoItemPedido = itemPedido.flTipoItemPedido;
		this.nuSeqProduto = itemPedido.nuSeqProduto;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedidoErpDif) {
            ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedidoErpDif.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemPedidoErpDif.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, itemPedidoErpDif.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, itemPedidoErpDif.nuPedido) &&
                ValueUtil.valueEquals(cdProduto, itemPedidoErpDif.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, itemPedidoErpDif.flTipoItemPedido) &&
                (nuSeqProduto == itemPedidoErpDif.nuSeqProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(flTipoItemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuSeqProduto);
        return strBuffer.toString();
    }

}