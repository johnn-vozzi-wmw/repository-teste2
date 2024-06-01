package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Regiao extends LavendereBaseDomain {

public static String TABLE_NAME = "TBLVPREGIAO";
public static String COLUMN_NMREGIAO = "NMREGIAO";
public static String COLUMN_CDREGIAO = "CDREGIAO";
	
	public String cdRegiao;
    public String nmRegiao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Regiao) {
        	Regiao regiao = (Regiao) obj;
            return
                ValueUtil.valueEquals(cdRegiao, regiao.cdRegiao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdRegiao);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return this.cdRegiao;
	}

	@Override
	public String getDsDomain() {
		return this.nmRegiao;
	}
    
}
