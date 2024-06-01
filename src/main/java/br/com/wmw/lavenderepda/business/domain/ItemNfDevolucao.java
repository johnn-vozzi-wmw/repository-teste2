package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemNfDevolucao extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPITEMNFDEVOLUCAO";
	
	public String cdEmpresa;
	public String nuNfDevolucao;
	public String cdSerie;
	public String cdProduto;
	public int nuSequenciaItem;
	public String cdUnidade;
	public double qtItem;
	public double vlUnitItem;
	public double vlTotalItem;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemNfDevolucao) {
			ItemNfDevolucao itemNotaDevolucao = (ItemNfDevolucao) obj;
			return ValueUtil.valueEquals(cdEmpresa, itemNotaDevolucao.cdEmpresa)
					&& ValueUtil.valueEquals(nuNfDevolucao, itemNotaDevolucao.cdRepresentante)
					&& ValueUtil.valueEquals(nuNfDevolucao, itemNotaDevolucao.nuNfDevolucao)
					&& ValueUtil.valueEquals(cdSerie, itemNotaDevolucao.cdSerie)
					&& ValueUtil.valueEquals(cdProduto, itemNotaDevolucao.cdProduto);
		}
		return false;
	}
	
	@Override
	public String getPrimaryKey() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(cdEmpresa);
		strBuilder.append(";");
		strBuilder.append(cdRepresentante);
		strBuilder.append(";");
		strBuilder.append(nuNfDevolucao);
		strBuilder.append(";");
		strBuilder.append(cdSerie);
        return strBuilder.toString();
	}
}
