package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TransportadoraCep extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPTRANSPCEP";

	public String cdEmpresa;
	public String cdTransportadora;
	public String nmTransportadora;
	public String cdTipoServicoFrete;
	public String nuFaixaCepInicial;
	public String nuFaixaCepFinal;
	public int nuDiasEntrega;
	public double vlPctFrete;
	public double vlMinFrete;
	public double vlTaxaColeta;
	
	
	@Override
	public boolean equals(Object obj) {
        if (obj instanceof TransportadoraCep ) {
        	TransportadoraCep transportadoraCep= (TransportadoraCep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, transportadoraCep.cdEmpresa) &&
                ValueUtil.valueEquals(cdTransportadora, transportadoraCep.cdTransportadora) &&
                ValueUtil.valueEquals(cdTipoServicoFrete, transportadoraCep.cdTipoServicoFrete) &&
                ValueUtil.valueEquals(nuFaixaCepInicial, transportadoraCep.nuFaixaCepInicial) &&
                ValueUtil.valueEquals(nuFaixaCepFinal, transportadoraCep.nuFaixaCepFinal);
        }
        return false;
    }

	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdTransportadora);
		primaryKey.append(";");
		primaryKey.append(cdTipoServicoFrete);
		primaryKey.append(";");
		primaryKey.append(nuFaixaCepInicial);
		primaryKey.append(";");
		primaryKey.append(nuFaixaCepFinal);
		primaryKey.append(";");
		return primaryKey.toString();
	}

	
	

}
