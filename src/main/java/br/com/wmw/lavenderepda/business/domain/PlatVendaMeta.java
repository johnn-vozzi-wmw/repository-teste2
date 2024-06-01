package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PlatVendaMeta extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPPLATVENDAMETA";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdPlatVendaMeta;
    public Date dtMeta;
    public String cdCliente;
    public String cdCentroCusto;
    public String cdPlataformaVenda;
    public double vlMetaContratada;
    public double vlMetaPlanejadaRep;
    public double vlMetaPlanejadaSup;
    public String flPlanejado;
    public String flEncerrado;
    public String cdRede;
    
    //Não persistente
    public String dsCidade;
    public String cdPeriodo;
    public boolean edited;
    public String dsStatus;
    public boolean bloqueada;
    
    public PlatVendaMeta() {
	}
    
	public boolean equals(Object obj) {
        if (obj instanceof PlatVendaMeta) {
        	PlatVendaMeta platVendaMeta = (PlatVendaMeta) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, platVendaMeta.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, platVendaMeta.cdRepresentante) &&
                ValueUtil.valueEquals(cdPlatVendaMeta, platVendaMeta.cdPlatVendaMeta) &&
                ValueUtil.valueEquals(cdCliente, platVendaMeta.cdCliente) &&
                ValueUtil.valueEquals(cdCentroCusto, platVendaMeta.cdCentroCusto) &&
                ValueUtil.valueEquals(cdPlataformaVenda, platVendaMeta.cdPlataformaVenda);
        }
        return false;
	}
	
	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdPlatVendaMeta);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdCentroCusto);
        primaryKey.append(";");
        primaryKey.append(cdPlataformaVenda);
        return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return cdPlatVendaMeta;
	}

	@Override
	public String getDsDomain() {
		return null;
	}

}
