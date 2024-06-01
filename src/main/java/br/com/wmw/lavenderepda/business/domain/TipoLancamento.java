package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class TipoLancamento extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPTIPOLANCAMENTO";
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTipoLancamento;
	public String dsTipoLancamento;
	public String hrAlteracao;
	public Date dtAlteracao;

	public TipoLancamento(String cdTipoLancamento, String dsTipoLancamento) {
		this.cdTipoLancamento = cdTipoLancamento;
		this.dsTipoLancamento = dsTipoLancamento;
	}

	public TipoLancamento() {

	}

	@Override
	public String getCdDomain() {
		return cdTipoLancamento;
	}

	@Override
	public String getDsDomain() {
		return dsTipoLancamento;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdTipoLancamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TipoLancamento)) return false;
		TipoLancamento that = (TipoLancamento) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante) &&
				ValueUtil.valueEquals(cdTipoLancamento, that.cdTipoLancamento);
	}

}
