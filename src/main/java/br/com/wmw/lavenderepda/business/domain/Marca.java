package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Marca extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPMARCA";

    public String cdEmpresa;
    public String cdMarca;
    public String dsMarca;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Marca) {
            Marca marca = (Marca) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, marca.cdEmpresa) && 
                ValueUtil.valueEquals(cdMarca, marca.cdMarca);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMarca);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdMarca;
	}

	@Override
	public String getDsDomain() {
		return dsMarca;
	}
}