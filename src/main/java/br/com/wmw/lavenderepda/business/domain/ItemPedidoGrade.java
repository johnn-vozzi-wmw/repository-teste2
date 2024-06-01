package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoGradeDTO;

public class ItemPedidoGrade extends BaseDomain {

	public static String TABLE_NAME_ITEMPEDIDOGRADE = "TBLVPITEMPEDIDOGRADE";
	public static String TABLE_NAME_ITEMPEDIDOGRADEERP = "TBLVPITEMPEDIDOGRADEERP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public double qtItemFisico;
    
    //Nao persistente
    public ItemGrade itemGrade1;
    public ItemGrade itemGrade2;
    public ItemGrade itemGrade3;
    public String dsMotivoGradeNaoInserida;
	public boolean filtraItemPedGradeNivel3;
	public ProdutoGrade produtoGrade;
    
    public ItemPedidoGrade() {
    }
    
    public ItemPedidoGrade(ItemPedidoGradeDTO itemPedidoGradeDTO) {
    	this();
    	try {
    		FieldMapper.copy(itemPedidoGradeDTO, this);
    	} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedidoGrade) {
            ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedidoGrade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemPedidoGrade.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, itemPedidoGrade.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, itemPedidoGrade.nuPedido) &&
                ValueUtil.valueEquals(cdProduto, itemPedidoGrade.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, itemPedidoGrade.flTipoItemPedido) &&
                ValueUtil.valueEquals(nuSeqProduto, itemPedidoGrade.nuSeqProduto) &&
                ValueUtil.valueEquals(cdItemGrade1, itemPedidoGrade.cdItemGrade1) &&
                ValueUtil.valueEquals(cdItemGrade2, itemPedidoGrade.cdItemGrade2) &&
                ValueUtil.valueEquals(cdItemGrade3, itemPedidoGrade.cdItemGrade3);
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
        primaryKey.append(cdItemGrade1);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade2);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade3);
        return primaryKey.toString();
    }

}