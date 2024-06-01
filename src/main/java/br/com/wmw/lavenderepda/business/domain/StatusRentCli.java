package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class StatusRentCli extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPSTATUSRENTCLI";

    public String cdEmpresa;
    public String cdStatusRentCli;
    public String dsStatusRentCli;
    public int vlRLista;
    public int vlGLista;
    public int vlBLista;
    public int vlRIcon;
    public int vlGIcon;
    public int vlBIcon;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusRentCli) {
            StatusRentCli statusRentCli = (StatusRentCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, statusRentCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdStatusRentCli, statusRentCli.cdStatusRentCli);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdStatusRentCli);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return this.cdStatusRentCli;
	}

	@Override
	public String getDsDomain() {
		return this.dsStatusRentCli;
	}

}