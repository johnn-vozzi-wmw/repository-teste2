package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class PontuacaoFaixaDias extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPPONTUACAOFAIXADIAS";
	public static final String TABLE_NAME_WEB = "TBLVWPONTUACAOFAIXADIAS";
	
	public static final String FL_FATOR_CORRECAO_CONDICAO_PAGTO = "C";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPontuacaoConfig;
	public String flTipoFatorCorrecao;
	public int qtDiasMedios;
	public double vlTotalPedido;
	public double vlFatorCorrecao;
	
	public PontuacaoFaixaDias() {
		this(TABLE_NAME);
	}

	public PontuacaoFaixaDias(String tableName) {
		super(tableName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PontuacaoFaixaDias)) return false;
		PontuacaoFaixaDias pontuacaoFaixaDias = (PontuacaoFaixaDias) obj;
		return ValueUtil.valueEquals(cdEmpresa, pontuacaoFaixaDias.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, pontuacaoFaixaDias.cdRepresentante) &&
			   ValueUtil.valueEquals(cdPontuacaoConfig, pontuacaoFaixaDias.cdPontuacaoConfig) &&
			   ValueUtil.valueEquals(flTipoFatorCorrecao, pontuacaoFaixaDias.flTipoFatorCorrecao) &&
			   ValueUtil.valueEquals(qtDiasMedios, pontuacaoFaixaDias.qtDiasMedios) &&
			   ValueUtil.valueEquals(vlTotalPedido, pontuacaoFaixaDias.vlTotalPedido);
	}

	public String getCdDomain() {
		return cdPontuacaoConfig;
	}

	public String getDsDomain() {
		return cdPontuacaoConfig + "-" + flTipoFatorCorrecao + "-" + qtDiasMedios + "-" + vlTotalPedido;
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
    	strBuffer.append(qtDiasMedios);
	    strBuffer.append(";");
	    strBuffer.append(vlTotalPedido);
        return strBuffer.toString();
    }
    
    public boolean isFatorCorrecaoCondicaoPagto() {
    	return FL_FATOR_CORRECAO_CONDICAO_PAGTO.equalsIgnoreCase(flTipoFatorCorrecao);
    }
	
}