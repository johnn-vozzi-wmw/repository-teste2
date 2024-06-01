package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CondComCliente extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCONDCOMCLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoComercial;
    public String cdCliente;
    public String flDefault;
    
    //Nao Persistentes
    public boolean comparaClientesRepresentante;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CondComCliente) {
            CondComCliente condComCliente = (CondComCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condComCliente.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, condComCliente.cdRepresentante) && 
                ValueUtil.valueEquals(cdCondicaoComercial, condComCliente.cdCondicaoComercial) && 
                ValueUtil.valueEquals(cdCliente, condComCliente.cdCliente);
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
        primaryKey.append(cdCondicaoComercial);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}