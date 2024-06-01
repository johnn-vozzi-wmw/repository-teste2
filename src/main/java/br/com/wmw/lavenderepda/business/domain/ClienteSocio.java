package br.com.wmw.lavenderepda.business.domain;

import totalcross.util.Date;

public class ClienteSocio extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPCLIENTESOCIO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public String cdSocio;
	public String nuCnpj;
	public String nmRazaoSocial;
	public Date dtEntradaSocio;
	public double vlPctParticipacao;

	@Override
	public String getCdDomain() {
		return cdSocio;
	}

	@Override
	public String getDsDomain() {
		return nmRazaoSocial;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdCliente + ";" + cdSocio;
	}

}
