
package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class EmpresaFilialImp extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPEMPRESAFILIALIMP";
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdFilialImp;
	public String dsRazaoSocialFilial;
	public String nuTelefone;
	public String nuCelular;
	public String hrAlteracao;
	public Date dtAlteracao;

	@Override
	public String getCdDomain() {
		return cdFilialImp;
	}

	@Override
	public String getDsDomain() {
		return dsRazaoSocialFilial;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdFilialImp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EmpresaFilialImp)) return false;
		EmpresaFilialImp that = (EmpresaFilialImp) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante) &&
				ValueUtil.valueEquals(cdFilialImp, that.cdFilialImp);
	}

}
