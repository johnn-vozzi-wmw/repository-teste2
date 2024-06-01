package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class BonifCfgBrinde extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPBONIFCFGBRINDE";
	
	public String cdEmpresa;
	public String cdBonifCfg;
	public String cdProduto;
	public double qtBrinde;
	public String flOpcional;
	
	//Nao persistentes
	private Produto produto;

	@Override
	public String getCdDomain() {
		return cdProduto;
	}

	@Override
	public String getDsDomain() {
		return "[" + cdEmpresa + "]" + " - " + cdBonifCfg + " - " + cdProduto;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdBonifCfg);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		return primaryKey.toString();
	}
	
	@Override
	public String toString() {
		return getDsDomain();
	}
	
	public boolean isOpcional() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, this.flOpcional);
	}

	public Produto getProduto() {
		if (produto == null) {
			produto = new Produto();
		}
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
