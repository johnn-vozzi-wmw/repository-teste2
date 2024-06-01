package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ComboCliente extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPCOMBOCLIENTE";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCombo;
	public String cdTabelaPreco;
	public String cdCliente;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdCombo + ";" + cdTabelaPreco + ";" + cdCliente;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComboCliente) {
			ComboCliente comboCliente = (ComboCliente) obj;
			return ValueUtil.valueEquals(cdEmpresa, comboCliente.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, comboCliente.cdRepresentante) &&
					ValueUtil.valueEquals(cdCombo, comboCliente.cdCombo) &&
					ValueUtil.valueEquals(cdTabelaPreco, comboCliente.cdTabelaPreco) &&
					ValueUtil.valueEquals(cdCliente, comboCliente.cdCliente);
		}
		return false;
	}

}
