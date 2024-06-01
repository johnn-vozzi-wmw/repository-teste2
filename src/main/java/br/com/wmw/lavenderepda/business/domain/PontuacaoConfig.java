package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PontuacaoConfig extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPPONTUACAOCONFIG";
	public static final String TABLE_NAME_WEB = "TBLVWPONTUACAOCONFIG";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPontuacaoConfig;
	public String dsPontuacaoConfig;
	public String cdSegmento;
	public String cdCanal;
	public String dsCidadeComercial;
	public Date dtInicioVigencia;
	public Date dtFimVigencia;
	
	// Não persistentes
	public PontuacaoFaixaDias pontuacaoFaixaDiaCondicaoPagto;
	public PontuacaoFaixaPreco pontuacaoFaixaPreco;
	public PontuacaoProduto pontuacaoProduto;
	
	public PontuacaoConfig() {
		this(TABLE_NAME);
	}

	public PontuacaoConfig(String tableName) {
		super(tableName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PontuacaoConfig)) return false;
		PontuacaoConfig pontuacaoConfig = (PontuacaoConfig) obj;
		return ValueUtil.valueEquals(cdEmpresa, pontuacaoConfig.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, pontuacaoConfig.cdRepresentante) &&
			   ValueUtil.valueEquals(cdPontuacaoConfig, pontuacaoConfig.cdPontuacaoConfig);
	}

	public String getCdDomain() {
		return cdPontuacaoConfig;
	}

	public String getDsDomain() {
		return dsPontuacaoConfig;
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdPontuacaoConfig);
        return strBuffer.toString();
    }
	
}