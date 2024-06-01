package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class FeriadoEmpresa extends BaseDomain {

	public static String TABLE_NAME = "TBLVPFERIADOEMPRESA";
	
    public String cdEmpresa;
    public int nuDia;
    public int nuMes;
    public int nuAno;
    public String dsFeriado;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof FeriadoEmpresa) {
            FeriadoEmpresa feriadoEmpresa = (FeriadoEmpresa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, feriadoEmpresa.cdEmpresa) && 
                ValueUtil.valueEquals(nuDia, feriadoEmpresa.nuDia) && 
                ValueUtil.valueEquals(nuMes, feriadoEmpresa.nuMes) && 
                ValueUtil.valueEquals(nuAno, feriadoEmpresa.nuAno);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(nuDia);
        primaryKey.append(";");
        primaryKey.append(nuMes);
        primaryKey.append(";");
        primaryKey.append(nuAno);
        return primaryKey.toString();
    }

}