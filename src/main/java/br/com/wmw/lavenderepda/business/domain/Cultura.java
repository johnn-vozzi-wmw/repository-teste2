package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Cultura extends LavendereBaseDomain {
	
    public static String TABLE_NAME = "TBLVPCULTURA";
	
	public String cdCultura;
	public String dsCultura;
	public String dsNomeCientCultura;
	public String abrevCultura;
	
	//Não persistentes
	public String cdProduto;
	public String dsFiltro;
	
	@Override
	public String getCdDomain() {
		return cdCultura;
	}

	@Override
	public String getDsDomain() {
		return dsCultura;
	}

	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdCultura);
        return primaryKey.toString();
	}

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof Cultura) {
			Cultura cultura = (Cultura) obj;
			return ValueUtil.valueEquals(cdCultura, cultura.cdCultura);
		}
		return false;
	}
	
}
