package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.json.JSONObject;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ConfigNotificacao extends BaseDomain {
	public static final String FLSISTEMA_WEB = "W";
	public static final String FLSISTEMA_APP = "P";

	public static String TABLE_NAME = "TBLVPCONFIGNOTIFICACAO";
	public String flSistema;
	public String cdConfigNotificacao;
	public String cdTipoNotificacao;
	public String nmEntidade;
	public String dsMensagem;
	public String dsSql;
	public int nuDiasLimpeza;
	public String dsConfigJson;
	public Date dtAlteracao;
	public String hrAlteracao;
	
	//não persistente
	private Vector nmEntidadeList;
	private JSONObject objectDsConfigJson;
	public TipoNotificacao tipoNotificacao;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConfigNotificacao) {
			ConfigNotificacao configNotificacao = (ConfigNotificacao) obj;
			return ValueUtil.valueEquals(cdConfigNotificacao, configNotificacao.cdConfigNotificacao);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdConfigNotificacao);
		return primaryKey.toString();
	}

	public Vector getNmEntidadeList() {
		return nmEntidadeList;
	}

	public void setNmEntidadeList(Vector nmEntidadeList) {
		this.nmEntidadeList = nmEntidadeList;
	}

	public String getNmEntidadeComPrefix() {
		if (ValueUtil.isNotEmpty(nmEntidade)) {
			return CrudDbxDao.PREFIXO_TABLE_APP_VENDAS + nmEntidade.toUpperCase();
		}
		return null;
	}

	public boolean isPropJsonLigado(String chave) {
		try {
			if (objectDsConfigJson == null) {
				objectDsConfigJson = new JSONObject(dsConfigJson);
			}
			return ValueUtil.getBooleanValue(objectDsConfigJson.getString(chave));
		} catch (Exception e) {
			return false;
		}
	}

	public void setFixo() {
		if (tipoNotificacao == null) {
			tipoNotificacao = new TipoNotificacao();
		}
		tipoNotificacao.flFixo = "S";
	}
	
	public void setDinamico() {
		if (tipoNotificacao == null) {
			tipoNotificacao = new TipoNotificacao();
		}
		tipoNotificacao.flFixo = "N";
	}
}