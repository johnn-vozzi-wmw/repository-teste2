package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class KitCliente extends BaseDomain {

public static final String TABLE_NAME = "TBLVPKITCLIENTE";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdKit;
	public String cdTabelaPreco;
	public String cdCliente;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdKit + ";" + cdTabelaPreco + ";" + cdCliente;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KitCliente) {
			KitCliente kitCliente = (KitCliente) obj;
			return ValueUtil.valueEquals(cdEmpresa, kitCliente.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, kitCliente.cdRepresentante) &&
					ValueUtil.valueEquals(cdKit, kitCliente.cdKit) &&
					ValueUtil.valueEquals(cdTabelaPreco, kitCliente.cdTabelaPreco) &&
					ValueUtil.valueEquals(cdCliente, kitCliente.cdCliente);
		}
		return false;
	}

}
