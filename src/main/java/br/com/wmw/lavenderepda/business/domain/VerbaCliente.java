package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaCliente extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPVERBACLIENTE";

	public static String VERBASALDO_PDA = "P";
	public static String VERBASALDO_ERP = "E";
	
	public static final String CDVERBASALDOCLIENTE_DEFAULT = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemSaldo;
    public String cdCliente;
    public double vlSaldo;
    public String dsControleatualizacao;
    public String cdVerbaSaldoCliente;
    public String cdGrupoProduto1;
    public int nuCarimbo;
    
    // Nao persistente
    public GrupoProduto1 grupoProduto1;
    
    public VerbaCliente() {
    	super();
    }
    
    public VerbaCliente(Cliente cliente) {
    	this.cdEmpresa = cliente.cdEmpresa;
    	this.cdRepresentante = cliente.cdRepresentante;
    	this.cdCliente = cliente.cdCliente;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaCliente) {
            VerbaCliente verbaCliente = (VerbaCliente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaCliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaCliente.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemSaldo, verbaCliente.flOrigemSaldo) &&
                ValueUtil.valueEquals(cdCliente, verbaCliente.cdCliente) &&
                ValueUtil.valueEquals(cdVerbaSaldoCliente, verbaCliente.cdVerbaSaldoCliente) &&
                ValueUtil.valueEquals(cdGrupoProduto1, verbaCliente.cdGrupoProduto1);
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
        primaryKey.append(flOrigemSaldo);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdVerbaSaldoCliente);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto1);
        return primaryKey.toString();
    }

}