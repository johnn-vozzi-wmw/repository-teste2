package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemTituloFinan extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPITEMTITULOFINAN";
	
	public String cdRepresentante;
	public String cdEmpresa;
	public String cdCliente;
	public String cdItem;
	public String nuNF;
	public String cdTitulo;
	public double qtdItem;
	public double vlUnitItem;
	public double vlTotalItem;
	
	public TituloFinanceiro TituloFinanceiro;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemTituloFinan) {
			ItemTituloFinan itemTituloFinanceiro = (ItemTituloFinan) obj;
			return ValueUtil.valueEquals(cdEmpresa, itemTituloFinanceiro.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, itemTituloFinanceiro.cdRepresentante) &&
					ValueUtil.valueEquals(cdCliente, itemTituloFinanceiro.cdCliente) &&
					ValueUtil.valueEquals(cdTitulo, itemTituloFinanceiro.cdTitulo) &&
					ValueUtil.valueEquals(nuNF, itemTituloFinanceiro.nuNF) &&
					ValueUtil.valueEquals(cdItem, itemTituloFinanceiro.cdItem);
		}
		return false;
	}
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdCliente + ";" + cdTitulo + ";" + "nuNF" + cdItem;
	}

}
