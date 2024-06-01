package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class FreteEvento extends LavendereBaseDomain {

	public static final int TIPO_BASE_CALCULO_SEM_BASE_CALCULO = 0;
	public static final int TIPO_BASE_CALCULO_VALOR_PEDIDO = 1;
	public static final int TIPO_BASE_CALCULO_PESO_PEDIDO = 2;
	public static final int TIPO_BASE_CALCULO_VOLUME = 3;
	public static final int TIPO_BASE_CALCULO_CALCULADO_MAIOR_VALOR = 4;
	public static final int TIPO_BASE_CALCULO_CALCULADO_SOMATORIO = 5;
	public static final int TIPO_BASE_CALCULO_FAIXA_PESO = 6;
	
    public static String TABLE_NAME = "TBLVPFRETEEVENTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFreteEvento;
    public String dsFreteEvento;
    public int nuTipoBaseCalculo;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FreteEvento) {
            FreteEvento freteEvento = (FreteEvento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, freteEvento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, freteEvento.cdRepresentante) &&
            	ValueUtil.valueEquals(cdFreteEvento, freteEvento.cdFreteEvento);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdFreteEvento);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdFreteEvento;
	}

	public String getDsDomain() {
		return dsFreteEvento;
	}
	
	public boolean isTipoBaseCalculoSemBaseCalculo() {
		return TIPO_BASE_CALCULO_SEM_BASE_CALCULO == nuTipoBaseCalculo;
	}
	
	public boolean isTipoBaseCalculoValorPedido() {
		return TIPO_BASE_CALCULO_VALOR_PEDIDO == nuTipoBaseCalculo;
	}

	public boolean isTipoBaseCalculoPesoPedido() {
		return TIPO_BASE_CALCULO_PESO_PEDIDO == nuTipoBaseCalculo;
	}

	public boolean isTipoBaseCalculoVolume() {
		return TIPO_BASE_CALCULO_VOLUME == nuTipoBaseCalculo;
	}

	public boolean isTipoBaseCalculoCalculadoMaiorValor() {
		return TIPO_BASE_CALCULO_CALCULADO_MAIOR_VALOR == nuTipoBaseCalculo;
	}

	public boolean isTipoBaseCalculoCalculadoSomatorio() {
		return TIPO_BASE_CALCULO_CALCULADO_SOMATORIO == nuTipoBaseCalculo;
	}
	
	public boolean isTipoBaseCalculoFaixaPeso() {
		return TIPO_BASE_CALCULO_FAIXA_PESO == nuTipoBaseCalculo;
	}
	
	public boolean isTipoBaseCalculoCalculado() {
		return isTipoBaseCalculoCalculadoMaiorValor() || isTipoBaseCalculoCalculadoSomatorio();
	}

}