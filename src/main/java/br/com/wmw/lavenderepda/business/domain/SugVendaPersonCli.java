package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class SugVendaPersonCli extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPSUGVENDAPERSONCLI";
	
	public String cdEmpresa;
	public String cdCliente;
	public String cdSugVendaPerson;
	

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdCliente + ";" + cdSugVendaPerson;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SugVendaPersonCli) {
			SugVendaPersonCli sugVendaPersonCli = (SugVendaPersonCli) obj;
			return ValueUtil.valueEquals(cdSugVendaPerson, sugVendaPersonCli.cdSugVendaPerson) &&
					ValueUtil.valueEquals(cdEmpresa, sugVendaPersonCli.cdEmpresa) &&
					ValueUtil.valueEquals(cdCliente, sugVendaPersonCli.cdCliente);
			
		}
		return false;
	}

}
