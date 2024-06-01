package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class PontuacaoFaixaPreco extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPPONTUACAOFAIXAPRECO";
	public static final String TABLE_NAME_WEB = "TBLVWPONTUACAOFAIXAPRECO";
	
	public static final String FL_FATOR_CORRECAO_DESC = "D";
	public static final String FL_FATOR_CORRECAO_ACRESC = "A";
	public static final String FL_FATOR_CORRECAO_NULO = "N";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPontuacaoConfig;
	public String flTipoFatorCorrecao;
	public double vlPctFaixa;
	public double vlFatorCorrecao;
	
	public PontuacaoFaixaPreco() {
		this(TABLE_NAME);
	}

	public PontuacaoFaixaPreco(String tableName) {
		super(tableName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PontuacaoFaixaPreco)) return false;
		PontuacaoFaixaPreco pontuacaoFaixaPreco = (PontuacaoFaixaPreco) obj;
		return ValueUtil.valueEquals(cdEmpresa, pontuacaoFaixaPreco.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, pontuacaoFaixaPreco.cdRepresentante) &&
			   ValueUtil.valueEquals(cdPontuacaoConfig, pontuacaoFaixaPreco.cdPontuacaoConfig) &&
			   ValueUtil.valueEquals(flTipoFatorCorrecao, pontuacaoFaixaPreco.flTipoFatorCorrecao) &&
			   ValueUtil.valueEquals(vlPctFaixa, pontuacaoFaixaPreco.vlPctFaixa);
	}

	public String getCdDomain() {
		return cdPontuacaoConfig;
	}

	public String getDsDomain() {
		return cdPontuacaoConfig + "-" + flTipoFatorCorrecao + "-" + vlPctFaixa;
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdPontuacaoConfig);
    	strBuffer.append(";");
    	strBuffer.append(flTipoFatorCorrecao);
    	strBuffer.append(";");
    	strBuffer.append(vlPctFaixa);
        return strBuffer.toString();
    }
    
    public boolean isFatorCorrecaoDesconto() {
    	return FL_FATOR_CORRECAO_DESC.equalsIgnoreCase(flTipoFatorCorrecao);
    }
    
    public boolean isFatorCorrecaoAcrescimo() {
    	return FL_FATOR_CORRECAO_ACRESC.equalsIgnoreCase(flTipoFatorCorrecao);
    }
    
    public boolean isFatorCorrecaoNulo() {
    	return FL_FATOR_CORRECAO_NULO.equalsIgnoreCase(flTipoFatorCorrecao);
    }
	
}