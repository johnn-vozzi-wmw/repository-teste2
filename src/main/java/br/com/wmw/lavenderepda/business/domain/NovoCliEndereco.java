package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class NovoCliEndereco extends BasePersonDomain {
	
	public static String TABLE_NAME = "TBLVPNOVOCLIENDERECO";
	
	public static final String NMCOLUNA_DSTIPOLOGRADOURO = "DSTIPOLOGRADOURO";
	public static final String NMCOLUNA_DSLOGRADOURO = "DSLOGRADOURO";
	public static final String NMCOLUNA_DSBAIRRO = "DSBAIRRO";
	public static final String NMCOLUNA_DSCIDADE = "DSCIDADE";
	public static final String NMCOLUNA_CDUF = "CDUF";
	public static final String NMCOLUNA_DSCEP = "DSCEP";
	
	public static final String NMCOLUNA_DSLOGRADOUROCOMERCIAL = "DSLOGRADOUROCOMERCIAL";
	public static final String NMCOLUNA_DSBAIRROCOMERCIAL = "DSBAIRROCOMERCIAL";
	public static final String NMCOLUNA_DSCIDADECOMERCIAL = "DSCIDADECOMERCIAL";
	public static final String NMCOLUNA_CDCIDADECOMERCIAL = "CDCIDADECOMERCIAL";
	public static final String NMCOLUNA_DSCEPCOMERCIAL = "DSCEPCOMERCIAL";
	public static final String NMCOLUNA_CDUFCOMERCIAL = "CDUFCOMERCIAL";
	public static final String NMCOLUNA_CDESTADOCOMERCIAL = "CDESTADOCOMERCIAL";
	public static final String NMCOLUNA_DSTIPOLOGRADOUROCOMERCIAL = "DSTIPOLOGRADOUROCOMERCIAL";
	public static final String NMCOLUNA_DSDIAENTREGA = "DSDIAENTREGA";
	public static final String NMCOLUNA_DSDIAENTREGAEMPRESA = "DSDIAENTREGAEMPRESA";
	public static final String NMCOLUNA_DSDIAABERTURA = "DSDIAABERTURA";
	public static final String NMCOLUNA_FLENTREGA = "FLENTREGA";
	public static final String NMCOLUNA_FLENTREGAPADRAO = "FLENTREGAPADRAO";
	public static final String NMCOLUNA_FLCOMERCIAL = "FLCOMERCIAL";
	public static final String NMCOLUNA_FLTIPOPESSOA = "FLTIPOPESSOA";
	public static final String NMCOLUNA_FLCOBRANCA = "FLCOBRANCA";
	public static final String NMCOLUNA_FLCOBRANCAPADRAO = "FLCOBRANCAPADRAO";
	
	public NovoCliEndereco() {
        super(TABLE_NAME);
    }

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdNovoCliente;
    public String flOrigemNovoCliente;
    public String cdEndereco;
    public String dsLogradouro;
    public String dsComplemento;
    public String dsBairro;
    public String dsCep;
    public String dsCidade;
    public String cdUf;
    public String dsPais;
    public String dsPontoReferencia;
    public String dsObservacao;
    public String flComercial;
    public String flEntrega;
    public String flEntregaPadrao;
    public String cdPeriodoEntrega;
    public String nuLogradouro;
    
    //Não persistente
    public static String sortAttr;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof NovoCliEndereco) {
        	NovoCliEndereco novoCliEndereco = (NovoCliEndereco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, novoCliEndereco.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, novoCliEndereco.cdRepresentante) && 
                ValueUtil.valueEquals(cdNovoCliente, novoCliEndereco.cdNovoCliente) && 
                ValueUtil.valueEquals(flOrigemNovoCliente, novoCliEndereco.flOrigemNovoCliente) && 
                ValueUtil.valueEquals(cdEndereco, novoCliEndereco.cdEndereco);
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
        primaryKey.append(cdNovoCliente);
        primaryKey.append(";");
        primaryKey.append(flOrigemNovoCliente);
        primaryKey.append(";");
        primaryKey.append(cdEndereco);
        return primaryKey.toString();
    }
    
    @Override
    public String toString() {
    	if (NMCOLUNA_DSLOGRADOURO.equals(sortAsc)) {
    		return StringUtil.getStringValue(getHashValuesDinamicos().get(NMCOLUNA_DSLOGRADOURO));
    	} else if (NMCOLUNA_DSBAIRRO.equals(sortAsc)) {
    		return StringUtil.getStringValue(getHashValuesDinamicos().get(NMCOLUNA_DSBAIRRO));
    	} else if (NMCOLUNA_DSCIDADE.equals(sortAsc)) {
    		return StringUtil.getStringValue(getHashValuesDinamicos().get(NMCOLUNA_DSCIDADE));
    	} else if (NMCOLUNA_CDUF.equals(sortAsc)) {
    		return StringUtil.getStringValue(getHashValuesDinamicos().get(NMCOLUNA_CDUF));
    	}
    	return super.toString();
    }

}