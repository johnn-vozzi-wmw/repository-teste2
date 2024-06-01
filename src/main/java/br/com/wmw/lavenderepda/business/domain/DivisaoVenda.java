package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class DivisaoVenda extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPDIVISAOVENDA";
    public static final String NMCOLUNA_DSDIVISAOVENDA = "dsDivisaoVenda";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdDivisaoVenda;
    public String dsDivisaoVenda;
    public String flIgnoraVerbaGrpProd;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DivisaoVenda) {
            DivisaoVenda divisaoVenda = (DivisaoVenda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, divisaoVenda.cdEmpresa) 
                && ValueUtil.valueEquals(cdRepresentante, divisaoVenda.cdRepresentante)
                && ValueUtil.valueEquals(cdDivisaoVenda, divisaoVenda.cdDivisaoVenda);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdDivisaoVenda);
        return  strBuffer.toString();
    }
    
    @Override
	public String getCdDomain() {
		return cdDivisaoVenda;
	}
    
    @Override
	public String getDsDomain() {
		return dsDivisaoVenda;
	}
    
}