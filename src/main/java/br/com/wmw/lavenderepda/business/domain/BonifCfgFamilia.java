package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class BonifCfgFamilia extends BaseDomain {
public static String TABLE_NAME = "TBLVPBONIFCFGFAMILIA";
	
	public String cdEmpresa;
    public String cdBonifCfg;
    public String cdFamilia;
    public String flGeraBonificacao;
    public String flPermiteBonificar;
    
    //Nao persistentes
    private Produto produtoFilter;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdBonifCfg);
        primaryKey.append(";");
        primaryKey.append(cdFamilia);
        return primaryKey.toString();
	}

	public Produto getProdutoFilter() {
		return produtoFilter;
	}

	public void setProdutoFilter(Produto produtoFilter) {
		this.produtoFilter = produtoFilter;
	}
}
