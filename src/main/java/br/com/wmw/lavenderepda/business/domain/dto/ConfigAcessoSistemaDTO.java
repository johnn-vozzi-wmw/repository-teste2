package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.business.domain.BaseDomain;

public class ConfigAcessoSistemaDTO extends BaseDomain {
	
	public String cdFuncao;
    public int nuDiaSemana;
    public String hrInicio;
    public String hrFim;

    public String getCdFuncao() {
		return cdFuncao;
	}
	public void setCdFuncao(String cdFuncao) {
		this.cdFuncao = cdFuncao;
	}
	public int getNuDiaSemana() {
		return nuDiaSemana;
	}
	public void setNuDiaSemana(int nuDiaSemana) {
		this.nuDiaSemana = nuDiaSemana;
	}
	public String getHrInicio() {
		return hrInicio;
	}
	public void setHrInicio(String hrInicio) {
		this.hrInicio = hrInicio;
	}
	public String getHrFim() {
		return hrFim;
	}
	public void setHrFim(String hrFim) {
		this.hrFim = hrFim;
	}
	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdFuncao);
        primaryKey.append(";");
        primaryKey.append(nuDiaSemana);
        primaryKey.append(";");
        primaryKey.append(hrInicio);
        return primaryKey.toString();
	}
}
