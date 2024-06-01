package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class BonifCfgConjunto extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPBONIFCFGCONJUNTO";
	public String cdEmpresa;
	public String cdBonifCfg;
	public String cdConjunto;
	public String flGeraBonificacao;
	public String flPermiteBonificar;

	@Override
	public String getPrimaryKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.cdEmpresa)
			.append(";")
			.append(this.cdBonifCfg)
			.append(";")
			.append(this.cdConjunto);
		return sb.toString();
	}
	
	public boolean isGeraBonificacao() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, this.flGeraBonificacao);
	}
	
	public boolean isPermiteBonificar() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, this.flPermiteBonificar);
	}

}
