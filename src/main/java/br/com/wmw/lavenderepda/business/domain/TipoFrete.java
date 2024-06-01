package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class TipoFrete extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPTIPOFRETE";

    public static final String CD_ESTADO_PADRAO = "0";
    public static final String NMCOLUNA_FLTIPOFRETECIF = "flTipoFreteCif";
    
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoFrete;
    public String cdUf;
    public String dsTipoFrete;
    public String flFreteFob;
    public double vlPctFrete;
    public double vlPesoMinimo;
    public String flUsaInfoAdicional;
    public String flDefault;
    public double vlPctMaxDesconto;
    public double vlMinimo;
    public String flTipoFreteCif;
    public String flSemFrete;
    public String flCalculaFrete;
    public String flCalculaFreteItem;
    public String flSugereSeguro;
    public String cdMotivoPendencia;
    public int nuOrdemLiberacao;
    
    //Nao persistentes
    public TipoFreteCli tipoFreteCliFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoFrete) {
            TipoFrete tipofrete = (TipoFrete) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipofrete.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipofrete.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoFrete, tipofrete.cdTipoFrete) &&
            	ValueUtil.valueEquals(cdUf, tipofrete.cdUf);
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
    	strBuffer.append(cdTipoFrete);
    	strBuffer.append(";");
    	strBuffer.append(cdUf);
        return strBuffer.toString();
    }

    @Override
	public String getCdDomain() {
		return cdTipoFrete;
	}

    @Override
	public String getDsDomain() {
		return dsTipoFrete;
	}

	public boolean isDefault() {
		return ValueUtil.VALOR_SIM.equals(flDefault);
	}

	public boolean isCalculaFrete() {
		return ValueUtil.VALOR_SIM.equals(flCalculaFrete);
	}

	public boolean isTipoFreteCif() {
		return ValueUtil.VALOR_SIM.equals(flTipoFreteCif);
	}

	public boolean isTipoFreteFob() {
		return ValueUtil.VALOR_SIM.equals(flFreteFob);
	}
	
	public boolean isTipoFreteSemFrete() {
		return ValueUtil.VALOR_SIM.equals(flSemFrete);
	}
	
	public boolean isUsaInfoAdicional() {
		return ValueUtil.VALOR_SIM.equals(this.flUsaInfoAdicional);
	}
	
	public boolean isCalculaFreteItem() {
		return ValueUtil.VALOR_SIM.equals(flCalculaFreteItem);
	}
	
	public boolean isSugereSeguro() {
		return ValueUtil.getBooleanValue(flSugereSeguro);
	}
	
	public boolean isPossuiMotivoPendencia() {
		return ValueUtil.isNotEmpty(cdMotivoPendencia);
	}
	
	public boolean isMarcaPedidoPendentePorTipoFrete() {
		return !isDefault() && isPossuiMotivoPendencia();
	}
}