package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class FreteBaseCalculo extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPFRETEBASECALCULO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTransportadora;
    public String cdFreteConfig;
    public int cdFreteCalculo;
    public int cdFreteCalculoBC;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FreteBaseCalculo) {
            FreteBaseCalculo freteBaseCalculo = (FreteBaseCalculo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, freteBaseCalculo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, freteBaseCalculo.cdRepresentante) &&
                ValueUtil.valueEquals(cdTransportadora, freteBaseCalculo.cdTransportadora) &&
                ValueUtil.valueEquals(cdFreteConfig, freteBaseCalculo.cdFreteConfig) &&
                ValueUtil.valueEquals(cdFreteCalculo, freteBaseCalculo.cdFreteCalculo) &&
            	ValueUtil.valueEquals(cdFreteCalculoBC, freteBaseCalculo.cdFreteCalculoBC);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTransportadora);
    	strBuffer.append(";");
    	strBuffer.append(cdFreteConfig);
    	strBuffer.append(";");
    	strBuffer.append(cdFreteCalculo);
    	strBuffer.append(";");
    	strBuffer.append(cdFreteCalculoBC);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdFreteConfig + "-" + cdFreteCalculo + "-" + cdFreteCalculoBC;
	}

	public String getDsDomain() {
		return getCdDomain();
	}
	
}