package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class PontuacaoProduto extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPPONTUACAOPRODUTO";
	public static final String TABLE_NAME_WEB = "TBLVWPONTUACAOPRODUTO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPontuacaoConfig;
	public String cdProduto;
	public double vlPesoPontuacao;
	
	public PontuacaoProduto() {
		this(TABLE_NAME);
	}

	public PontuacaoProduto(String tableName) {
		super(tableName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PontuacaoProduto)) return false;
		PontuacaoProduto pontuacaoProduto = (PontuacaoProduto) obj;
		return ValueUtil.valueEquals(cdEmpresa, pontuacaoProduto.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, pontuacaoProduto.cdRepresentante) &&
				ValueUtil.valueEquals(cdPontuacaoConfig, pontuacaoProduto.cdPontuacaoConfig) &&
				ValueUtil.valueEquals(cdProduto, pontuacaoProduto.cdProduto);
	}

	public String getCdDomain() {
		return cdPontuacaoConfig;
	}

	public String getDsDomain() {
		return cdPontuacaoConfig + "-" + cdProduto;
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdPontuacaoConfig);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return strBuffer.toString();
    }
    
}