package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class RequisicaoServTipo extends LavendereBaseDomain {
	
    public static String TABLE_NAME = "TBLVPREQUISICAOSERVTIPO";
    
    
    public static final char SEPARADOR_CAMPOS = '|';
	public static final String FLFILTRO_STATUS_CLIENTE_RESTRITO = "R";
	public static final String FLFILTRO_STATUS_CLIENTE_EXCECAO = "E";
	
	public String cdRequisicaoServTipo;
	public String dsRequisicaoServTipo;
	public String flObrigaCliente;
	public String flObrigaPedido;
	public String flFiltroStatusCliente;
	public String dsStatusClienteList;
	
	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdRequisicaoServTipo);
        return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return cdRequisicaoServTipo;
	}

	@Override
	public String getDsDomain() {
		return dsRequisicaoServTipo;
	}

	public boolean isObrigaPedido() {
		return ValueUtil.VALOR_SIM.equals(flObrigaPedido);
	}
	
	public boolean isObrigaCliente() {
		return ValueUtil.VALOR_SIM.equals(flObrigaCliente);
	}
	
}
