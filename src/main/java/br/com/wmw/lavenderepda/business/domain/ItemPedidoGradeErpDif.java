package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemPedidoGradeErpDif extends BaseDomain {

	public static String TABLE_NAME = "TBLVPITEMPEDIDOGRADEERPDIF";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdItemGradeOrg1;
    public String cdItemGradeOrg2;
    public String cdItemGradeOrg3;
    public double qtItemFisicoOrg;
    public String cdItemGradeErp1;
    public String cdItemGradeErp2;
    public String cdItemGradeErp3;
    public double qtItemFisicoErp;
    public String flEmailEnviado;
    public String dsObservacaoOrg;
    public String dsObservacaoErp;
    public String cdUnidade;
    
    public ProdutoGrade produtoGrade;
    public ItemGrade itemGrade1;
    public ItemGrade itemGrade2;
    public ItemGrade itemGrade3;
    
    
    public ItemPedidoGradeErpDif() {}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedidoGradeErpDif) {
            ItemPedidoGradeErpDif itemPedidoGrade = (ItemPedidoGradeErpDif) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedidoGrade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemPedidoGrade.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, itemPedidoGrade.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, itemPedidoGrade.nuPedido) &&
                ValueUtil.valueEquals(cdProduto, itemPedidoGrade.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, itemPedidoGrade.flTipoItemPedido) &&
                ValueUtil.valueEquals(nuSeqProduto, itemPedidoGrade.nuSeqProduto) &&
                ValueUtil.valueEquals(cdItemGradeOrg1, itemPedidoGrade.cdItemGradeOrg1) &&
                ValueUtil.valueEquals(cdItemGradeOrg2, itemPedidoGrade.cdItemGradeOrg2) &&
                ValueUtil.valueEquals(cdItemGradeOrg3, itemPedidoGrade.cdItemGradeOrg3) &&
	            ValueUtil.valueEquals(cdItemGradeErp1, itemPedidoGrade.cdItemGradeErp1) &&
	            ValueUtil.valueEquals(cdItemGradeErp2, itemPedidoGrade.cdItemGradeErp2) &&
	            ValueUtil.valueEquals(cdItemGradeErp3, itemPedidoGrade.cdItemGradeErp3);
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
        primaryKey.append(cdItemGradeOrg1);
        primaryKey.append(";");
        primaryKey.append(cdItemGradeOrg2);
        primaryKey.append(";");
        primaryKey.append(cdItemGradeOrg3);
        primaryKey.append(";");
        primaryKey.append(cdItemGradeErp1);
        primaryKey.append(";");
        primaryKey.append(cdItemGradeErp2);
        primaryKey.append(";");
        primaryKey.append(cdItemGradeErp3);
        return primaryKey.toString();
    }

}