package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class AplicacaoProduto extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPAPLICACAOPRODUTO";
	
	public String cdProduto;
	public String cdCultura;
	public String cdPraga;
	public String vlDose;
	
	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		primaryKey.append(cdCultura);
		primaryKey.append(";");
		primaryKey.append(cdPraga);
		return primaryKey.toString();
	}
	
	//Override
	public boolean equals(Object obj) {
		if (obj instanceof AplicacaoProduto) {
			AplicacaoProduto aplicacaoProduto = (AplicacaoProduto) obj;
			return
			ValueUtil.valueEquals(cdProduto, aplicacaoProduto.cdProduto) &&
			ValueUtil.valueEquals(cdCultura, aplicacaoProduto.cdCultura) &&
			ValueUtil.valueEquals(cdPraga, aplicacaoProduto.cdPraga);
		}
		return false;
	}
	
}
