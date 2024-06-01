package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PlatVendaMetaPerid extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPPLATVENDAMETAPERID";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdPeriodo;
    public String dsPeriodo;
    public Date dtInicial;
    public Date dtFinal;

    public boolean equals(Object obj) {
        if (obj instanceof PlatVendaMetaPerid) {
        	PlatVendaMetaPerid platVendaMetaPerid = (PlatVendaMetaPerid) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, platVendaMetaPerid.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, platVendaMetaPerid.cdRepresentante) &&
                ValueUtil.valueEquals(cdPeriodo, platVendaMetaPerid.cdPeriodo);
        }
        return false;
    }

	
	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdPeriodo);
        return primaryKey.toString();
	}

	//@Override
	public String getCdDomain() {
		return cdPeriodo;
	}

	//@Override
	public String getDsDomain() {
		return dsPeriodo;
	}
}
