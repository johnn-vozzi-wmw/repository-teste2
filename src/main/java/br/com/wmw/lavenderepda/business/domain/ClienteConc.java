package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ClienteConc extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPCLIENTECONC";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdConcorrente;
    
    public ClienteConc() {
		super();
    }

    
    public ClienteConc(String cdEmpresa, String cdRepresentante, String cdCliente) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdCliente = cdCliente;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteConc) {
            ClienteConc clienteConc = (ClienteConc) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clienteConc.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, clienteConc.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, clienteConc.cdCliente) && 
                ValueUtil.valueEquals(cdConcorrente, clienteConc.cdConcorrente);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdConcorrente);
        return primaryKey.toString();
    }

}