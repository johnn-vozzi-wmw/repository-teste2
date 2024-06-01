package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;

public class PedidoRelacionado extends BasePersonDomain {

	public static final String TABLE_NAME_PEDIDORELACIONADO = "TBLVPPEDIDORELACIONADO";
	public static final String TABLE_NAME_PEDIDORELACIONADOWEB = "TBLVWPEDIDORELACIONADO";
	
	public static final String FLTIPORELACIONAMENTO_PEDIDO_PRODUCAO = "1";
	
	public PedidoRelacionado() {
		this(TABLE_NAME_PEDIDORELACIONADO);
	}
	
	public PedidoRelacionado(String dsTabela) {
		super(dsTabela);
	}

	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdEmpresaRel;
	public String cdRepresentanteRel;
	public String flOrigemPedidoRel;
	public String nuPedidoRel;
	public String flTipoRelacionamento;
	
	@Override
	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdEmpresaRel);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentanteRel);
    	strBuffer.append(";");
    	strBuffer.append(nuPedidoRel);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedidoRel);
    	strBuffer.append(";");
    	strBuffer.append(flTipoRelacionamento);
        return strBuffer.toString();
	}

}
