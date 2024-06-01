package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class FreteCalculo extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPFRETECALCULO";
    
    public static final String TIPO_TAXA_SEM_TAXA = "S";
    public static final String TIPO_TAXA_PERCENTUAL = "P";
    public static final String TIPO_TAXA_INDICE = "I";
    public static final String TIPO_TAXA_FIXO = "F";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTransportadora;
    public String cdFreteConfig;
	public int cdFreteCalculo;
    public int nuOrdemCalculo;
    public String cdFreteEvento;
    public String flTipoTaxa;
    public double vlTaxa;
    public String flFormaPrecoFrete;
    public double vlMinimo;
    public double vlFaixaBC;
    public String flTipoTaxaExcedendeFaixaBC;
    public double vlTaxaExcedenteFaixaBC;
    
    //-- Não persistentes
    public BigDecimal valorCalculado;
    public FreteEvento freteEvento;
    public FreteConfig freteConfig;
    public Vector listFreteBaseCalculo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FreteCalculo) {
            FreteCalculo freteCalculo = (FreteCalculo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, freteCalculo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, freteCalculo.cdRepresentante) &&
                ValueUtil.valueEquals(cdTransportadora, freteCalculo.cdTransportadora) &&
                ValueUtil.valueEquals(cdFreteConfig, freteCalculo.cdFreteConfig) &&
                ValueUtil.valueEquals(cdFreteCalculo, freteCalculo.cdFreteCalculo);
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
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdFreteConfig + "-" + nuOrdemCalculo + "-" + cdFreteEvento;
	}

	public String getDsDomain() {
		return (freteEvento != null) ? freteEvento.dsFreteEvento : getCdDomain();
	}
	
	public boolean isFormaPrecoFrete() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flFormaPrecoFrete);
	}
	
	public boolean isTipoTaxaSemTaxa() {
		return TIPO_TAXA_SEM_TAXA.equalsIgnoreCase(flTipoTaxa);
	}
	
	public boolean isTipoTaxaPercentual() {
		return TIPO_TAXA_PERCENTUAL.equalsIgnoreCase(flTipoTaxa);
	}
	
	public boolean isTipoTaxaIndice() {
		return TIPO_TAXA_INDICE.equalsIgnoreCase(flTipoTaxa);
	}
	
	public boolean isTipoTaxaFixo() {
		return TIPO_TAXA_FIXO.equalsIgnoreCase(flTipoTaxa);
	}
	
	public boolean isTipoTaxaExcedenteFaixaBCPercentual() {
		return TIPO_TAXA_PERCENTUAL.equalsIgnoreCase(flTipoTaxaExcedendeFaixaBC);
	}
	
	public boolean isTipoTaxaExcedenteFaixaBCIndice() {
		return TIPO_TAXA_INDICE.equalsIgnoreCase(flTipoTaxaExcedendeFaixaBC);
	}
	
	public boolean isTipoTaxaExcedenteFaixaBCFixo() {
		return TIPO_TAXA_FIXO.equalsIgnoreCase(flTipoTaxaExcedendeFaixaBC);
	}
	
}