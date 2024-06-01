package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import totalcross.util.Date;

public class ClienteChurn extends BasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPCLIENTECHURN";
	public static final String APRESENTA_INDICADOR = "5";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdChurn;
    public String cdCliente;
    public Date dtEntradaChurn;
    public String cdMotivoChurn;
    public String dsObsChurn;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Não persistente
    public String dsFiltroCliente;
    private Cliente cliente;
    private MotivoChurn motivoChurn;
    
    public ClienteChurn() { 
    	super(TABLE_NAME);
    	this.cliente = new Cliente();
    	this.motivoChurn = new MotivoChurn();
    }
    
    public ClienteChurn(String cdRepresentante) {
    	this();
    	this.cdEmpresa = SessionLavenderePda.cdEmpresa;
		this.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cdRepresentante : SessionLavenderePda.getCdRepresentanteFiltroDados(this.getClass());
    }
    
    
    public ClienteChurn(String cdRepresentante, String cdMotivoChurn, String dsFiltroCliente) {
    	this(cdRepresentante);
		this.cdMotivoChurn = cdMotivoChurn;
		this.dsFiltroCliente = dsFiltroCliente;
		this.motivoChurn = new MotivoChurn(cdMotivoChurn);
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteChurn) {
            ClienteChurn clientechurn = (ClienteChurn) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clientechurn.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, clientechurn.cdRepresentante) && 
                ValueUtil.valueEquals(cdChurn, clientechurn.cdChurn);
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
        primaryKey.append(cdChurn);
        return primaryKey.toString();
    }
    
    public MotivoChurn getMotivoChurn() {
		return motivoChurn;
	}

	public void setMotivoChurn(String cdMotivoChurn) {
		this.cdMotivoChurn = cdMotivoChurn;
		this.motivoChurn.cdMotivoChurn = cdMotivoChurn;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		this.cdCliente = cliente.cdCliente;
		
	}


}