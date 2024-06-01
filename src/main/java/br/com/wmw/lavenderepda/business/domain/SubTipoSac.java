package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class SubTipoSac extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPSUBTIPOSAC";

    public String cdEmpresa;
    public String cdTipoSac;
    public String cdSubTipoSac;
    public String dsSubTipoSac;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof SubTipoSac) {
            SubTipoSac subTipoSac = (SubTipoSac) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, subTipoSac.cdEmpresa) && 
                ValueUtil.valueEquals(cdTipoSac, subTipoSac.cdTipoSac) && 
                ValueUtil.valueEquals(cdSubTipoSac, subTipoSac.cdSubTipoSac);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdTipoSac);
        primaryKey.append(";");
        primaryKey.append(cdSubTipoSac);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdSubTipoSac;
	}

	@Override
	public String getDsDomain() {
		return dsSubTipoSac;
	}

}