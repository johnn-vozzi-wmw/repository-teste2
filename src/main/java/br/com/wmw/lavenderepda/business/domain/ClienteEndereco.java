package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class ClienteEndereco extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPCLIENTEENDERECO";
	
	public static final String NMCOLUNA_CDENDERECO = "cdEndereco";
	public static final String NMCOLUNA_DSLOGRADOURO = "dsLogradouro";
	public static final String NMCOLUNA_CDTIPOENDERECO = "cdTipoEndereco";
	public static final String NMCOLUNA_NULOGRADOURO = "nuLogradouro";
	public static final String NMCOLUNA_DSCOMPLEMENTO = "dsComplemento";
	public static final String NMCOLUNA_DSBAIRRO = "dsBairro";
	public static final String NMCOLUNA_DSCEP = "dsCep";
	public static final String NMCOLUNA_DSCIDADE = "dsCidade";
	public static final String NMCOLUNA_DSESTADO = "dsEstado";
	public static final String NMCOLUNA_DSPAIS = "dsPais";
	public static final String NMCOLUNA_DSPONTOREFERENCIA = "dsPontoReferencia";
	public static final String NMCOLUNA_DSOBSERVACAO = "dsObservacao";
	public static final String NMCOLUNA_DSDIAENTREGA = "dsDiaEntrega";
	public static final String NMCOLUNA_DSPERIODOENTREGA = "dsPeriodoEntrega";
	public static final String NMCOLUNA_DSPERIODOENTREGAALTERNATIVO = "dsPeriodoEntregaAlternativo";
	public static final String NMCOLUNA_DSDIAABERTURA = "dsDiaAbertura";
	public static final String NMCOLUNA_DSPERIODOABERTURA = "dsPeriodoAbertura";
	public static final String NMCOLUNA_FLCOMERCIAL = "flComercial";
	public static final String NMCOLUNA_FLENTREGA = "flEntrega";
	public static final String NMCOLUNA_FLENTREGAPADRAO = "flEntregaPadrao";
	public static final String NMCOLUNA_FLENTREGAAGENDADA = "flEntregaAgendada";
	public static final String NMCOLUNA_CDPERIODOENTREGA = "cdPeriodoEntrega";
	public static final String NMCOLUNA_FLCOBRANCA = "flCobranca";
	public static final String NMCOLUNA_FLCOBRANCAPADRAO = "flCobrancaPadrao";
	public static final String NMCOLUNA_NUCNPJ = "nuCnpj";
	public static final String NMCOLUNA_FLTIPOPESSOA = "flTipoPessoa";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdEndereco;
    public String dsLogradouro;
    public String nuLogradouro;
    public String dsComplemento;
    public String dsBairro;
    public String dsCep;
    public String dsCidade;
    public String dsEstado;
    public String dsPais;
    public String dsPontoReferencia;
    public String dsObservacao;
    public String flComercial;
    public String flEntrega;
    public String flEntregaPadrao;
    public String dsDiaEntrega;
    public String dsPeriodoEntrega;
    public String dsPeriodoEntregaAlternativo;
    public String dsDiaAbertura;
    public String dsPeriodoAbertura;
    public String flEntregaAgendada;
    public String cdPeriodoEntrega;
    public String cdTipoEndereco;
    public String flCobranca;
    public String flCobrancaPadrao;
    public String nuCnpj;
    public String flTipoPessoa;
    public String cdRegistro;

    public ClienteEndereco() {
    	this(TABLE_NAME);
    }
    
    public ClienteEndereco(String dsTabela) {
    	super(dsTabela);
    }
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteEndereco) {
            ClienteEndereco clienteEndereco = (ClienteEndereco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clienteEndereco.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, clienteEndereco.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, clienteEndereco.cdCliente) && 
                ValueUtil.valueEquals(cdEndereco, clienteEndereco.cdEndereco);
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
        primaryKey.append(cdEndereco);
        return primaryKey.toString();
    }
    
    public boolean isComercial() {
    	return ValueUtil.VALOR_SIM.equals(flComercial);
    }
    
    public boolean isEntrega() {
    	return ValueUtil.VALOR_SIM.equals(flEntrega);
    }
    
    public boolean isEntregaPadrao() {
    	return ValueUtil.VALOR_SIM.equals(flEntregaPadrao);
    }
    
    public boolean isCobranca() {
    	return ValueUtil.VALOR_SIM.equals(flCobranca);
    }

    public boolean isCobrancaPadrao() {
    	return ValueUtil.VALOR_SIM.equals(flCobrancaPadrao);
    }
    
    public boolean isJuridica() {
    	return ValueUtil.valueEquals(Messages.TIPOPESSOA_FLAG_JURIDICA, flTipoPessoa);
    }
    
    public boolean isFisica() {
    	return ValueUtil.valueEquals(Messages.TIPOPESSOA_FLAG_FISICA, flTipoPessoa);
    }
    
	@Override
	public String getCdDomain() {
		return cdEndereco;
	}

	@Override
	public String getDsDomain() {
		return dsLogradouro;
	}
	

}