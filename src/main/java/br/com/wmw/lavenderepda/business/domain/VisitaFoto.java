package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;

public class VisitaFoto extends BaseDomain {

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemVisita;
    public String cdVisita;
    public int cdFoto;
    public String imFoto;
    public String flEnviadoServidor;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VisitaFoto) {
            VisitaFoto visitaFoto = (VisitaFoto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, visitaFoto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, visitaFoto.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemVisita, visitaFoto.flOrigemVisita) &&
                ValueUtil.valueEquals(cdVisita, visitaFoto.cdVisita) &&
                ValueUtil.valueEquals(cdFoto, visitaFoto.cdFoto);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(flOrigemVisita);
        primaryKey.append(";");
        primaryKey.append(cdVisita);
        primaryKey.append(";");
        primaryKey.append(cdFoto);
        return primaryKey.toString();
    }
 
	public static String getPathImg() {
		return FotoUtil.getPathImg(VisitaFoto.class);
	}
	
	public boolean isVisitaFotoEnviadaServidor() {
    	return ValueUtil.VALOR_SIM.equals(flEnviadoServidor);
	}

}