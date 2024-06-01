package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class GrupoProduto5 extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPGRUPOPRODUTO5";
	
	public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String cdGrupoProduto4;
    public String cdGrupoProduto5;
    public String dsGrupoProduto5;

	@Override
	public String getCdDomain() {
		return cdGrupoProduto5;
	}

	@Override
	public String getDsDomain() {
		return dsGrupoProduto5;
	}

	@Override
	public String getPrimaryKey() {
		return cdGrupoProduto1 + ";" + cdGrupoProduto2 + ";" + cdGrupoProduto3 + ";" + cdGrupoProduto4 + ";" + cdGrupoProduto5;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GrupoProduto5) {
			GrupoProduto5 grupoProduto5 = (GrupoProduto5) obj;
			return ValueUtil.valueEquals(cdGrupoProduto1, grupoProduto5.cdGrupoProduto1) &&
					ValueUtil.valueEquals(cdGrupoProduto2, grupoProduto5.cdGrupoProduto2) &&
					ValueUtil.valueEquals(cdGrupoProduto3, grupoProduto5.cdGrupoProduto3) &&
					ValueUtil.valueEquals(cdGrupoProduto4, grupoProduto5.cdGrupoProduto4) &&
					ValueUtil.valueEquals(cdGrupoProduto5, grupoProduto5.cdGrupoProduto5);
		}
		return false;
	}

}
