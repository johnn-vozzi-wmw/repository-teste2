package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondComSegCli extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCONDCOMSEGCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoComercial;
    public String cdSegmento;
    public String cdCliente;
    public String flDefault;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondComSegCli) {
            CondComSegCli condComSegCli = (CondComSegCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condComSegCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condComSegCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoComercial, condComSegCli.cdCondicaoComercial) &&
                ValueUtil.valueEquals(cdSegmento, condComSegCli.cdSegmento) &&
                ValueUtil.valueEquals(cdCliente, condComSegCli.cdCliente);
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
        primaryKey.append(cdCondicaoComercial);
        primaryKey.append(";");
        primaryKey.append(cdSegmento);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}