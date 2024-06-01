package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.CepDTO;

public class Cep extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCEP";
	
    public String dsCep;
    public String cdLogradouro;
    public String cdBairro;
    public String cdCidade;
    
    public String dsLogradouro;
    public String dsCidade;
    public String dsBairro;
    public String dsUf;    
    public static String sortAttr;
    
    
    public Cep() { }
    
    public Cep(String dsCep) {
    	this.dsCep = dsCep;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Cep) {
            Cep cep = (Cep) obj;
            return
                ValueUtil.valueEquals(dsCep, cep.dsCep) && 
                ValueUtil.valueEquals(cdLogradouro, cep.cdLogradouro) && 
                ValueUtil.valueEquals(cdBairro, cep.cdBairro) && 
                ValueUtil.valueEquals(cdCidade, cep.cdCidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(dsCep);
        primaryKey.append(";");
        primaryKey.append(cdLogradouro);
        primaryKey.append(";");
        primaryKey.append(cdBairro);
        primaryKey.append(";");
        primaryKey.append(cdCidade);
        return primaryKey.toString();
    }
    
    @Override
    public String getSortStringValue() {
    	if ("LOGRADOURO".equals(sortAttr)) {
    		return dsLogradouro;
    	} else if ("BAIRRO".equals(sortAttr)) {
			return dsBairro;
		}
    	return dsCep;
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

	public void setEndereco(CepDTO cepDto) {
		this.dsUf = cepDto.getDsUf();
		this.dsCidade = cepDto.getDsCidade();
		this.dsBairro = cepDto.getDsBairro();
		this.dsLogradouro = cepDto.getDsLogradouro();
	}

}