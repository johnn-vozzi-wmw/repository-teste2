package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class SugVendaPersonProd extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPSUGVENDAPERSONPROD";
	
	public String cdEmpresa;
	public String cdProduto;
	public String cdSugVendaPerson;
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdProduto + ";" + cdSugVendaPerson;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SugVendaPersonProd) {
			SugVendaPersonProd sugVendaPersonProd = (SugVendaPersonProd) obj;
			return ValueUtil.valueEquals(cdSugVendaPerson, sugVendaPersonProd.cdSugVendaPerson) &&
					ValueUtil.valueEquals(cdEmpresa, sugVendaPersonProd.cdEmpresa) &&
					ValueUtil.valueEquals(cdProduto, sugVendaPersonProd.cdProduto);
		}
		return super.equals(obj);
	}

}
