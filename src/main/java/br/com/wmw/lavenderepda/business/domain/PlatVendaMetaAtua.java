package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PlatVendaMetaAtua extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPPLATVENDAMETAATUA";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdPlatVendaMeta;
    public String cdCliente;
    public String cdCentroCusto;
    public String cdPlataformaVenda;
    public Date dtMeta;
    public double vlMetaPlanejadaRep;
    public double vlMetaPlanejadaSup;
    public String flPlanejado;
    public String flEncerrado;

    public PlatVendaMetaAtua() {
    	//
    }
    
    public PlatVendaMetaAtua(PlatVendaMeta platVendaMeta) {
    	cdEmpresa = platVendaMeta.cdEmpresa;
    	cdRepresentante = platVendaMeta.cdRepresentante;
    	cdPlatVendaMeta = platVendaMeta.cdPlatVendaMeta;
    	dtMeta = platVendaMeta.dtMeta;
    	cdCliente = platVendaMeta.cdCliente;
    	cdCentroCusto = platVendaMeta.cdCentroCusto;
    	cdPlataformaVenda = platVendaMeta.cdPlataformaVenda;
    	vlMetaPlanejadaRep = platVendaMeta.vlMetaPlanejadaRep;
    	vlMetaPlanejadaSup = platVendaMeta.vlMetaPlanejadaSup;
    	flPlanejado = platVendaMeta.flPlanejado;
    	flEncerrado = platVendaMeta.flEncerrado;
	}
    
	public boolean equals(Object obj) {
        if (obj instanceof PlatVendaMetaAtua) {
        	PlatVendaMetaAtua platVendaMetaAtua = (PlatVendaMetaAtua) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, platVendaMetaAtua.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, platVendaMetaAtua.cdRepresentante) &&
                ValueUtil.valueEquals(cdPlatVendaMeta, platVendaMetaAtua.cdPlatVendaMeta) &&
                ValueUtil.valueEquals(cdCliente, platVendaMetaAtua.cdCliente) &&
                ValueUtil.valueEquals(cdCentroCusto, platVendaMetaAtua.cdCentroCusto) &&
                ValueUtil.valueEquals(cdPlataformaVenda, platVendaMetaAtua.cdPlataformaVenda);
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
		return null;
	}

	@Override
	public String getDsDomain() {
		return null;
	}

}
