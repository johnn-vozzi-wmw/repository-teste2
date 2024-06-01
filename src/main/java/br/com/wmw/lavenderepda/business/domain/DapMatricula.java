package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class DapMatricula extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDAPMATRICULA";
	
	public String cdEmpresa;
	public String cdCliente;
	public String cdDapMatricula;
	public String cdSafra;
	public Date dtEmissao;
	public Date dtValidade;
	public String dsCidade;
	public String dsLocalidade;
	public String cdUf;
	public int nuMaxSeqLaudo;
	
	//Não persistente
	public Cliente cliente;
	public String dsSafra;
	
	@Override
	public String getCdDomain() {
		return cdDapMatricula;
	}

	@Override
	public String getDsDomain() {
		return dsCidade;
	}
	
	public String getDsLocal() {
		return (ValueUtil.isNotEmpty(dsCidade) ? dsCidade + " - " : "") + (ValueUtil.isNotEmpty(cdUf) ? cdUf + " - ": "") + (ValueUtil.isNotEmpty(dsLocalidade) ? dsLocalidade : "");
	}

	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdSafra);
        primaryKey.append(";");
        primaryKey.append(cdDapMatricula);
        return primaryKey.toString();
	}

}
