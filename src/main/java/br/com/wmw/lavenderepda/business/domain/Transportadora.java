package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Transportadora extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPTRANSPORTADORA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTransportadora;
    public String nmTransportadora;
    public String flSomaFrete;
    public String flMostraFrete;
    public String nuCnpj;
    public String nuPlaca;
    
  //Não persistente
    public TranspTipoPed transpTipoPedFilter;
    public String flDefault;
    public String cdRegiao;
    public int dsCepTratado;
    public boolean consideraFreteConfig;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transportadora) {
            Transportadora transportadora = (Transportadora) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, transportadora.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, transportadora.cdRepresentante) &&
                ValueUtil.valueEquals(cdTransportadora, transportadora.cdTransportadora);
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
    	strBuffer.append(cdTransportadora);
        return strBuffer.toString();
    }

    public boolean isFlDefault() {
    	return ValueUtil.VALOR_SIM.equals(flDefault);
    }

    public boolean isFlSomaFrete() {
    	return ValueUtil.VALOR_SIM.equals(flSomaFrete);
    }

    public boolean isFlMostraFrete() {
    	return ValueUtil.VALOR_SIM.equals(flMostraFrete);
    }

	public String getCdDomain() {
		return cdTransportadora;
	}

	public String getDsDomain() {
		return nmTransportadora;
	}
}