package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Praga extends LavendereBaseDomain {
	
    public static String TABLE_NAME = "TBLVPPRAGA";
	
	public String cdPraga;
	public String dsPraga;
	public String dsNomeCientPraga;
	
	//Não persisntentes
	public String cdProduto;
	public String cdCultura;
	public String dsFiltro;
	
	@Override
	public String getCdDomain() {
		return cdPraga;
	}

	@Override
	public String getDsDomain() {
		return dsNomeCientPraga;
	}

	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdPraga);
        return primaryKey.toString();
	}
	
	//Override
	public boolean equals(Object obj) {
		if (obj instanceof Praga) {
			Praga praga = (Praga) obj;
			return ValueUtil.valueEquals(cdPraga, praga.cdPraga);
		}
		return false;
	}

}
