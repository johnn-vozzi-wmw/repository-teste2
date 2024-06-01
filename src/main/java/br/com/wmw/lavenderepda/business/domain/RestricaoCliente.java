package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RestricaoCliente extends BaseDomain {

	public static String TABLE_NAME = "TBLVPRESTRICAOCLIENTE";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdRestricao;
	public String cdCliente;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdRestricao + ";" + cdCliente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RestricaoCliente)) {
			return false;
		}
		RestricaoCliente that = (RestricaoCliente) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante) &&
				ValueUtil.valueEquals(cdRestricao, that.cdRestricao) &&
				ValueUtil.valueEquals(cdCliente, that.cdCliente);
	}

}
