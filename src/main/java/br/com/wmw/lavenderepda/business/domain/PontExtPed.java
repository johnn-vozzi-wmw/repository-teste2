package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PontExtPed extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPPONTEXTPED";
	public static final String TABLE_NAME_WEB = "TBLVWPONTEXTPED";
	
	public static final String SORT_DATEHR = "DTHREMISSAO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdSegmento;
	public String cdCanal;
	public String dsTipoLancamento;
	public double vlPontuacaoBase;
	public double vlPontuacaoRealizado;
	public double vlTotalPedido;
	public int qtDiasMedios;
	public String flPedidoCancelado;
	public Date dtEmissao;
	public String hrEmissao;
	
	//-- Não Persistente
	public Date dtEmissaoInicialFilter;
	public Date dtEmissaoFinalFilter;
	public Cliente cliente;
	public double vlPontuacaoBaseReal;
	public double vlPontuacaoRealizadoReal;
	public boolean hasRetornoDiferencaErp;
	
	
	public PontExtPed() {
		this(TABLE_NAME);
	}

	public PontExtPed(String tableName) {
		super(tableName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PontExtPed)) return false;
		PontExtPed pontExtPed = (PontExtPed) obj;
		return ValueUtil.valueEquals(cdEmpresa, pontExtPed.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, pontExtPed.cdRepresentante) &&
			   ValueUtil.valueEquals(flOrigemPedido, pontExtPed.flOrigemPedido) &&
			   ValueUtil.valueEquals(nuPedido, pontExtPed.nuPedido);
	}

	public String getCdDomain() {
		return nuPedido;
	}

	public String getDsDomain() {
		return flOrigemPedido + "-" + nuPedido;
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
        return strBuffer.toString();
    }
    
    public boolean isPedidoCancelado() {
    	return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flPedidoCancelado);
    }
    
}