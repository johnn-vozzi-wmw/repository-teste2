package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.Session;
import totalcross.json.JSONObject;

public class ParametrosWGPS extends BaseDomain {
	
	public boolean isStart;
	public String cdEmpresa;
	public String cdRepresentante;
	public int nuIntervaloColetaGpsRepresentante;
	public int nuIntervaloEnvioPontoGpsBackground;
	public String dir;
	public String cdUsuario;
	public String horarioLimiteColetaGpsManual;
	public String cdSistema;
	public boolean isWMWSuporte;

	public ParametrosWGPS(boolean isStart, String cdEmpresa, String cdRepresentante, int nuIntervaloColetaGpsRepresentante, int nuIntervaloEnvioPontoGpsBackground, String cdUsuario, String dir, String horarioLimiteColetaGpsManual) {
		this.isStart = isStart;
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.nuIntervaloColetaGpsRepresentante = nuIntervaloColetaGpsRepresentante;
		this.nuIntervaloEnvioPontoGpsBackground = nuIntervaloEnvioPontoGpsBackground;
		this.cdUsuario = cdUsuario;
		this.dir = dir;
		this.horarioLimiteColetaGpsManual = horarioLimiteColetaGpsManual;
	}

	@Override
	public String getPrimaryKey() {
		return null;
	}
	
	public JSONObject getAsJson() {
		JSONObject parametersJson = new JSONObject();
		parametersJson.put("isStart", isStart);
		parametersJson.put("cdEmpresa", cdEmpresa);
		parametersJson.put("cdRepresentante", cdRepresentante);
		parametersJson.put("nuIntervaloColetaGpsRepresentante", nuIntervaloColetaGpsRepresentante * 1000);
		parametersJson.put("nuIntervaloEnvioPontoGpsBackground", nuIntervaloEnvioPontoGpsBackground * 1000);
		parametersJson.put("dir", dir);
		parametersJson.put("cdUsuario", cdUsuario);
		parametersJson.put("horarioLimiteColetaGpsManual", horarioLimiteColetaGpsManual);
		parametersJson.put("cdSistema", AppConfig.CDSISTEMA);
		parametersJson.put("isWMWSuporte", Session.isModoSuporte);
		return parametersJson;
	}

}
