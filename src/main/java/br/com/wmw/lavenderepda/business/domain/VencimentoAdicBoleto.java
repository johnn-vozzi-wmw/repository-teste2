package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class VencimentoAdicBoleto extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPVENCIMENTOADICBOLETO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdVencimentoAdicBoleto;
	public String dsVencimentoAdicBoleto;
	public Integer nuMaxVencimentos;
	public Integer nuIntervaloVencimentos;
	
	@Override
	public String getPrimaryKey() {
		StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdVencimentoAdicBoleto);
        return strBuffer.toString();
	}
	
	public String getCdDomain() {
		return cdVencimentoAdicBoleto;
	}

	public String getDsDomain() {
		return dsVencimentoAdicBoleto;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VencimentoAdicBoleto) {
			VencimentoAdicBoleto vencimentoAdicBoleto = (VencimentoAdicBoleto) obj;
			return ValueUtil.valueEquals(cdEmpresa, vencimentoAdicBoleto.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, vencimentoAdicBoleto.cdRepresentante) &&
					ValueUtil.valueEquals(cdVencimentoAdicBoleto, vencimentoAdicBoleto.cdVencimentoAdicBoleto);
		}
		return false;
	}

}