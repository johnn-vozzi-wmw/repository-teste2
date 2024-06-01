package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PlatVendaMetaHist extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPPLATVENDAMETAHIST";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdPlatVendaMeta;
    public Date dtMeta;
    public String cdCliente;
    public String cdCentroCusto;
    public String cdPlataformaVenda;
    public double vlVendaRealizada;

	public boolean equals(Object obj) {
        if (obj instanceof PlatVendaMetaHist) {
        	PlatVendaMetaHist platVendaMetaHist = (PlatVendaMetaHist) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, platVendaMetaHist.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, platVendaMetaHist.cdRepresentante) &&
                ValueUtil.valueEquals(cdPlatVendaMeta, platVendaMetaHist.cdPlatVendaMeta) &&
                ValueUtil.valueEquals(cdCliente, platVendaMetaHist.cdCliente) &&
                ValueUtil.valueEquals(cdCentroCusto, platVendaMetaHist.cdCentroCusto) &&
                ValueUtil.valueEquals(cdPlataformaVenda, platVendaMetaHist.cdPlataformaVenda);
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
