package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MarcadorCliente extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPMARCADORCLIENTE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdMarcador;
    
    public MarcadorCliente() { }

    public MarcadorCliente(String cdEmpresa, String cdRepresentante, String cdCliente) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdCliente = cdCliente;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof MarcadorCliente) {
            MarcadorCliente marcadorcliente = (MarcadorCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, marcadorcliente.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, marcadorcliente.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, marcadorcliente.cdCliente) && 
                ValueUtil.valueEquals(cdMarcador, marcadorcliente.cdMarcador);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdMarcador);
        return primaryKey.toString();
    }

}