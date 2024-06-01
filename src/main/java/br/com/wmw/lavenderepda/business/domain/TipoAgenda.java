package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class TipoAgenda extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPTIPOAGENDA";
	
    public String cdTipoAgenda;
    public String dsTipoAgenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoAgenda) {
            TipoAgenda tipoAgenda = (TipoAgenda) obj;
            return
                ValueUtil.valueEquals(cdTipoAgenda, tipoAgenda.cdTipoAgenda);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdTipoAgenda);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdTipoAgenda;
	}

	@Override
	public String getDsDomain() {
		return dsTipoAgenda;
	}

}