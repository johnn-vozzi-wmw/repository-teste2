package br.com.wmw.lavenderepda.business.domain;

public class DapCultura extends LavendereBaseDomain  {
	
	public static final String TABLE_NAME = "TBLVPDAPCULTURA";
	
	public String cdEmpresa;
	public String cdCliente;
	public String cdSafra;
	public String cdDapMatricula;
	public String cdDapCultura;
	public String dsDapCultura;
	public double qtArea;
	
	@Override
	public String getCdDomain() {
		return cdDapCultura;
	}
	
	@Override
	public String getDsDomain() {
		return dsDapCultura;
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
        primaryKey.append(";");
        primaryKey.append(cdDapCultura);
        return primaryKey.toString();
	}

}
