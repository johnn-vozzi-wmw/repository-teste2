package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class LogLogin extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPLOGLOGIN";

    public String dsEquipamento;
    public String cdUsuarioLogin;
    public String flAmbiente;
    public String dsStatus;
    public String dsMotivo;
    public Date dtLogin;
    public String hrLogin;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogLogin) {
            LogLogin logLogin = (LogLogin) obj;
            return
                ValueUtil.valueEquals(dsEquipamento, logLogin.dsEquipamento) 
                && ValueUtil.valueEquals(cdUsuarioLogin, logLogin.cdUsuarioLogin) 
                && ValueUtil.valueEquals(dtLogin, logLogin.dtLogin) 
                && ValueUtil.valueEquals(hrLogin, logLogin.hrLogin);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(dsEquipamento);
        primaryKey.append(";");
        primaryKey.append(cdUsuarioLogin);
        primaryKey.append(";");
        primaryKey.append(dtLogin);
        primaryKey.append(";");
        primaryKey.append(hrLogin);
        return primaryKey.toString();
    }  

	public String getCdDomain() {
		return getPrimaryKey();
	}

	public String getDsDomain() {
		return getPrimaryKey();
	}    

}