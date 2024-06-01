package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class FechamentoDiarioEst extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPFECHAMENTODIARIOEST";
	public String cdEmpresa;
	public String cdRepresentante;
	public Date dtMovimentacao;
	public String cdProduto;
	public double qtRemessa;
	public double qtRetorno;
	public double qtVendido;
	public double vlSaldo;
	public String hrAlteracao;
	public Date dtAlteracao;

	@Override
	public String getCdDomain() {
		return dtMovimentacao.toString();
	}

	@Override
	public String getDsDomain() {
		return cdProduto;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + dtMovimentacao + ";" + cdProduto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FechamentoDiarioEst)) return false;
		FechamentoDiarioEst that = (FechamentoDiarioEst) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante) &&
				ValueUtil.valueEquals(dtMovimentacao, that.dtMovimentacao) &&
				ValueUtil.valueEquals(cdProduto, that.cdProduto);
	}

}
