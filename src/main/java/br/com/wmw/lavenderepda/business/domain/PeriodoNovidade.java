package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;

public class PeriodoNovidade extends LavendereBaseDomain {

	public static final int PERIODO_NOVIDADE_CDHOJE=3;
	public static final int PERIODO_NOVIDADE_CDPARAMETRO=2;
	public static final int PERIODO_NOVIDADE_CDTODOS=1;

	public static final String PERIODO_NOVIDADE_NMHOJE="Hoje";
	public static final String PERIODO_NOVIDADE_NMTODOS="Todos";

	public int cdPeriodoNovidade;
	public String dsPeriodoNovidade;

	//@Override
	public String getPrimaryKey() {
		return StringUtil.getStringValue(cdPeriodoNovidade);
	}

	public String getCdDomain() {
		return StringUtil.getStringValue(cdPeriodoNovidade);
	}

	public String getDsDomain() {
		return dsPeriodoNovidade;
	}

}
