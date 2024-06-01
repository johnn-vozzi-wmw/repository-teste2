package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.TipoFreteService;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class FreteConfig extends LavendereBaseDomain {

	public static final String FL_TIPO_TAB_FRETE_REGIAO = "R";
	public static final String FL_TIPO_TAB_FRETE_CEP = "C";
	public static final String CAMPOFILTROPRECO = "VLPRECOFRETECALCULADO";
	public static final String EXIBE_TRANSPORTADORA_CAPA = "1";
	public static final String EXIBE_TIPO_FRETE_CAPA = "2";
	public static final String EXIBE_VALOR_FRETE_CAPA = "3";
	
	
	
    public static String TABLE_NAME = "TBLVPFRETECONFIG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTransportadora;
    public String cdFreteConfig;
    public String dsFreteConfig;
    public String cdTipoFrete;
    public String flTipoTabFrete;
    public String cdRegiao;
    public int nuCepInicio;
    public int nuCepFim;
    
    //-- Não Persistente
    public BigDecimal vlPrecoFreteCalculado;
    public Vector listFreteCalculo;
    public Vector listFreteCalculoFormaFrete;
    public String nmTransportadoraDsFreteConfig;
    public TipoFrete tipoFrete;
    public String flCif;
    public String flFob;
    public Cliente clienteFilter;

    public FreteConfig() {
    	
    }
    
    public FreteConfig(final String cdEmpresa, final String cdRepresentante, final String cdTransportadora) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdTransportadora = cdTransportadora;
	}
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FreteConfig) {
            FreteConfig freteConfig = (FreteConfig) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, freteConfig.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, freteConfig.cdRepresentante) &&
                ValueUtil.valueEquals(cdTransportadora, freteConfig.cdTransportadora) &&
            	ValueUtil.valueEquals(cdFreteConfig, freteConfig.cdFreteConfig);
        }
        return false;
    }
    
    @Override
    public double getSortDoubleValue() {
    	return ValueUtil.getDoubleValue(vlPrecoFreteCalculado);
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
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdFreteConfig;
	}

	public String getDsDomain() {
		return dsFreteConfig;
	}
	
	public TipoFrete getTipoFrete() throws SQLException {
		if (tipoFrete == null && cdTipoFrete != null) {
			TipoFrete filter = new TipoFrete();
			filter.cdEmpresa = cdEmpresa;
			filter.cdRepresentante = cdRepresentante;
			filter.cdTipoFrete = cdTipoFrete;
			filter.cdUf = "0";
			filter = (TipoFrete) TipoFreteService.getInstance().findByPrimaryKey(filter);
			tipoFrete = filter != null ? filter : new TipoFrete();
		}
		return tipoFrete;
	}
	
	public boolean isCif() throws SQLException {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(getTipoFrete().flTipoFreteCif);
	}
	
	public boolean isFob() throws SQLException {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(getTipoFrete().flTipoFreteCif);
	}
	
	public boolean isTipoTabFreteRegiao() {
		return FL_TIPO_TAB_FRETE_REGIAO.equalsIgnoreCase(flTipoTabFrete);
	}
	
	public boolean isTipoTabFreteCep() {
		return FL_TIPO_TAB_FRETE_CEP.equalsIgnoreCase(flTipoTabFrete);
	}

}