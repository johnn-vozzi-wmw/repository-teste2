package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeEstoqueDto;

public class ItemNfeEstoque extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPITEMNFEESTOQUE"; 
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String nuNotaRemessa;
	public String nuSerieRemessa;
	public String cdProduto;
	public String cdUnidade;
	public double qtItem;
	public String cdClassificFiscal;
	public double vlItem;
	public double vlTotalItem;
	public double vlTotalIcms;
	public double vlTotalSt;
	public double vlPctIcms;
	public double vlPctPis;
	public double vlPctCofins;
	public double qtPeso;
	
	//Nao persistente
	public String dsProduto;
	
	public ItemNfeEstoque() {}
	
	public ItemNfeEstoque(ItemNfeEstoqueDto itemNfeEstoqueDto) {
		try {
			FieldMapper.copy(itemNfeEstoqueDto, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemNfeEstoque) {
			ItemNfeEstoque itemNfeEstoque = (ItemNfeEstoque) obj;
			return ValueUtil.valueEquals(cdEmpresa, itemNfeEstoque.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, itemNfeEstoque.cdRepresentante) &&
					ValueUtil.valueEquals(nuSerieRemessa, itemNfeEstoque.nuSerieRemessa) &&
					ValueUtil.valueEquals(nuNotaRemessa, itemNfeEstoque.nuNotaRemessa) &&
					ValueUtil.valueEquals(cdProduto, itemNfeEstoque.cdProduto);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + nuSerieRemessa + ";" + nuNotaRemessa + ";" + cdProduto;
	}

	@Override
	public String getCdDomain() {
		return cdProduto;
	}

	@Override
	public String getDsDomain() {
		return dsProduto;
	}

}
