package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ClienteSeg extends BaseDomain {

	public static String TABLE_NAME = "TBLVPCLIENTESEG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSegmento;
    public String cdCliente;
    public String flDefault;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteSeg) {
            ClienteSeg clienteSeg = (ClienteSeg) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clienteSeg.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, clienteSeg.cdRepresentante) &&
                ValueUtil.valueEquals(cdSegmento, clienteSeg.cdSegmento) &&
                ValueUtil.valueEquals(cdCliente, clienteSeg.cdCliente);
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
        primaryKey.append(cdSegmento);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}