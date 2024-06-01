package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class VerbaSaldoSupervisor extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPVERBASALDOSUPERVISOR";
	
	public String cdEmpresa;
	public String cdSupervisor;
	public String cdContaCorrente;
	public double vlSaldo;
	public double vlSaldoInicial;
	public Date dtSaldo;
	public Date dtVigenciaInicial;
	public Date dtVigenciaFinal;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdContaCorrente;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VerbaSaldoSupervisor) {
			VerbaSaldoSupervisor verba = (VerbaSaldoSupervisor) obj;
			return ValueUtil.valueEquals(cdEmpresa, verba.cdEmpresa) &&
					ValueUtil.valueEquals(cdContaCorrente, verba.cdContaCorrente);
		} 
		return false;
	}

}
