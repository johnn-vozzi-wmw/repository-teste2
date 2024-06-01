package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FechamentoDiarioLan extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPFECHAMENTODIARIOLAN";
	public String cdEmpresa;
	public String cdRepresentante;
	public Date dtFechamentoDiario;
	public String cdTipoLancamento;
	public double vlTotalLancamento;
	public String hrAlteracao;
	public Date dtAlteracao;

	//Não persistentes

	public String dsTipoLancamento;

	@Override
	public String getCdDomain() {
		return cdTipoLancamento;
	}

	@Override
	public String getDsDomain() {
		return dtFechamentoDiario.toString();
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + dtFechamentoDiario + ";" + cdTipoLancamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FechamentoDiarioLan)) return false;
		FechamentoDiarioLan that = (FechamentoDiarioLan) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante) &&
				ValueUtil.valueEquals(dtFechamentoDiario, that.dtFechamentoDiario) &&
				ValueUtil.valueEquals(cdTipoLancamento, that.cdTipoLancamento);
	}

}
