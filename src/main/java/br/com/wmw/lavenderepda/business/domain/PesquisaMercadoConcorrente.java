package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PesquisaMercadoConcorrente extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISAMERCCONC";

	public String cdEmpresa;
	public String cdPesquisaMercadoConfig;
	public String cdConcorrente;

	//não persistentes
	public String dsConcorrente;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdPesquisaMercadoConfig + ";" + cdConcorrente;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaMercadoConcorrente) {
			PesquisaMercadoConcorrente other = (PesquisaMercadoConcorrente) obj;
			return ValueUtil.valueEquals(cdEmpresa, other.cdEmpresa)
					&& ValueUtil.valueEquals(cdPesquisaMercadoConfig, other.cdPesquisaMercadoConfig)
					&& ValueUtil.valueEquals(cdConcorrente, other.cdConcorrente);
		} else {
			return false;
		}
	}

}
