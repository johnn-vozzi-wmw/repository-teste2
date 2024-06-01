package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemPedidoAgrSimilar extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPITEMPEDIDOAGRSIMILAR";

	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdProduto;
	public String cdProdutoSimilar;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public String cdAgrupadorSimilaridade;
	public double qtItemFisico;
	
	//Nao persistente
	public double vlItemPedido;
	public double vlTotalItemPedido;
	public String dsProdutoSimilar;
	public Pedido pedido;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + flOrigemPedido + ";" + nuPedido + ";" + cdProduto + ";" + cdProdutoSimilar + ";" + flTipoItemPedido + ";" + nuSeqProduto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemPedidoAgrSimilar) {
			ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) obj;
			return ValueUtil.valueEquals(cdEmpresa, item.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, item.cdRepresentante) &&
					ValueUtil.valueEquals(flOrigemPedido, item.flOrigemPedido) &&
					ValueUtil.valueEquals(nuPedido, item.nuPedido) &&
					ValueUtil.valueEquals(cdProduto, item.cdProduto) &&
					ValueUtil.valueEquals(cdProdutoSimilar, item.cdProdutoSimilar) &&
					ValueUtil.valueEquals(flTipoItemPedido, item.flTipoItemPedido) &&
					ValueUtil.valueEquals(nuSeqProduto, item.nuSeqProduto);
		}
		return false;
	}
	
	public String getProdutoKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdProdutoSimilar;
	}
	
}
