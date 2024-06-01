package br.com.wmw.lavenderepda.business.domain;

public class EstoqueDisponivel extends LavendereBaseDomain  {
	
	public static final String ESTOQUE_DISPONIVEL_COMBO_OPCAO_1 = "1";
	public static final String ESTOQUE_DISPONIVEL_COMBO_OPCAO_2 = "2";
	public static final String ESTOQUE_DISPONIVEL_COMBO_OPCAO_3 = "3";
	
	public String cdEstoqueDisponivel;
	public String dsStatusEstoque;
	
	public EstoqueDisponivel(String cdEstoqueDisponivel) {
		this.cdEstoqueDisponivel = cdEstoqueDisponivel;
	}

	public EstoqueDisponivel(String cdEstoqueDisponivel, String dsStatusEstoque) {
		this.cdEstoqueDisponivel = cdEstoqueDisponivel;
		this.dsStatusEstoque = dsStatusEstoque;
	}
	

	@Override
	public String getCdDomain() {
		return cdEstoqueDisponivel;
	}

	@Override
	public String getDsDomain() {
		return dsStatusEstoque;
	}

	@Override
	public String getPrimaryKey() {
		return cdEstoqueDisponivel;
	}

}
