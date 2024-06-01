package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class TipoNotificacao extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPTIPONOTIFICACAO";

	public String cdTipoNotificacao;
	public String dsTipoNotificacao;
	public String flFixo;
	public Date dtAlteracao;
	public String hrAlteracao;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TipoNotificacao) {
			TipoNotificacao tipoNotificacao = (TipoNotificacao) obj;
			return
					ValueUtil.valueEquals(cdTipoNotificacao, tipoNotificacao.cdTipoNotificacao);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdTipoNotificacao);
		return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return cdTipoNotificacao;
	}

	@Override
	public String getDsDomain() {
		return dsTipoNotificacao;
	}
}