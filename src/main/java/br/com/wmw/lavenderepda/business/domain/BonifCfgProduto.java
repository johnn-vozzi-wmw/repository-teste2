package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class BonifCfgProduto extends BaseDomain {

	public static String TABLE_NAME = "TBLVPBONIFCFGPRODUTO";

	public String cdEmpresa;
	public String cdBonifCfg;
	public String cdProduto;
	public String flGeraBonificacao;
	public String flPermiteBonificar;

	//Nao persistente
	public String dsProduto;
	public String cdRepresentanteFilter;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdBonifCfg);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		return primaryKey.toString();
	}
}
