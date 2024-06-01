package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Questionario extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPQUESTIONARIO";

	public String cdEmpresa;
	public String cdQuestionario;
	public String nmQuestionario;
	public Date dtCadastro;
	public String flTipoQuestionario;
	
	public static final String NMCOLUNA_NMQUESTIONARIO = "NMQUESTIONARIO";

	//Não Persistentes
	public String cdCliente;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Questionario) {
			Questionario questionario = (Questionario) obj;
			return ValueUtil.valueEquals(cdEmpresa, questionario.cdEmpresa) && ValueUtil.valueEquals(cdQuestionario, questionario.cdQuestionario);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdQuestionario);
		return primaryKey.toString();
	}
}
