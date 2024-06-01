package br.com.wmw.lavenderepda.business.domain.dto;

import totalcross.json.JSONObject;

public class CepDTO {
	
    public String dsCep;
    public String cdLogradouro;
    public String cdBairro;
    public String cdCidade;
    public String dsLogradouro;
    public String dsCidade;
    public String dsBairro;
    public String dsUf;
    
    public CepDTO(JSONObject jsonObject) {
    	dsUf = jsonObject.optString("uf");
    	dsCidade = jsonObject.optString("localidade");
    	dsBairro = jsonObject.optString("bairro");
    	dsLogradouro = jsonObject.optString("logradouro");
		dsCep = jsonObject.optString("cep");
	}
    
	public String getDsCep() {
		return dsCep;
	}
	public void setDsCep(String dsCep) {
		this.dsCep = dsCep;
	}
	public String getCdLogradouro() {
		return cdLogradouro;
	}
	public void setCdLogradouro(String cdLogradouro) {
		this.cdLogradouro = cdLogradouro;
	}
	public String getCdBairro() {
		return cdBairro;
	}
	public void setCdBairro(String cdBairro) {
		this.cdBairro = cdBairro;
	}
	public String getCdCidade() {
		return cdCidade;
	}
	public void setCdCidade(String cdCidade) {
		this.cdCidade = cdCidade;
	}
	public String getDsLogradouro() {
		return dsLogradouro;
	}
	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}
	public String getDsCidade() {
		return dsCidade;
	}
	public void setDsCidade(String dsCidade) {
		this.dsCidade = dsCidade;
	}
	public String getDsBairro() {
		return dsBairro;
	}
	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}
	public String getDsUf() {
		return dsUf;
	}
	public void setDsUf(String dsUf) {
		this.dsUf = dsUf;
	}    
}