package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class MarcadorPedido extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPMARCADORPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdMarcador;
    
    public MarcadorPedido() { }

    public MarcadorPedido(String cdEmpresa, String cdRepresentante, String flOrigemPedido, String nuPedido) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.flOrigemPedido = flOrigemPedido;
		this.nuPedido = nuPedido;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof MarcadorPedido) {
            MarcadorPedido marcadorPedido = (MarcadorPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, marcadorPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, marcadorPedido.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, marcadorPedido.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, marcadorPedido.nuPedido) && 
                ValueUtil.valueEquals(cdMarcador, marcadorPedido.cdMarcador);
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
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdMarcador);
        return primaryKey.toString();
    }

}