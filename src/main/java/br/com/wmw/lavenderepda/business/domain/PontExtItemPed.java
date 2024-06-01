package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;

public class PontExtItemPed extends LavendereBasePersonDomain {
	
	public static final String TABLE_NAME = "TBLVPPONTEXTITEMPED";
	public static final String TABLE_NAME_WEB = "TBLVWPONTEXTITEMPED";
	
	public static final String TIPO_LANCAMENTO_NOVO_PEDIDO = "N";
	public static final String TIPO_LANCAMENTO_ESTORNO = "E";
	public static final String TIPO_LANCAMENTO_DEVOLUCAO = "D";
	public static final String TIPO_LANCAMENTO_CANCELAMENTO = "C";
	
	public static final String NM_COLUNA_VL_REALIZADO = "VLPONTUACAOREALIZADO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public String flTipoLancamento;
	public String dsTipoLancamento;
	public double qtItemFisico;
	public String cdUnidade;
	public double nuConversaoUnidade;
	public double qtItemFaturamento;
	public double vlTotalItemPedido;	
	public double vlPontuacaoBase;
	public double vlPontuacaoRealizado;
    public double vlPesoPontuacao;
    public double vlFatorCorrecaoFaixaPreco;
    public double vlFatorCorrecaoFaixaDias;
    public double vlPctFaixaPrecoPontuacao;
    public double vlBasePontuacao;
	public Date dtEmissao;
	public String hrEmissao;
	
	//-- Não Persistente
	public Date dtEmissaoInicialFilter;
	public Date dtEmissaoFinalFilter;
	public Produto produto;
	public double vlSaldoParcial;
	
	public PontExtItemPed() {
		this(TABLE_NAME);
	}

	public PontExtItemPed(String tableName) {
		super(tableName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PontExtItemPed)) return false;
		PontExtItemPed pontExtItemPed = (PontExtItemPed) obj;
		return ValueUtil.valueEquals(cdEmpresa, pontExtItemPed.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, pontExtItemPed.cdRepresentante) &&
			   ValueUtil.valueEquals(flOrigemPedido, pontExtItemPed.flOrigemPedido) &&
			   ValueUtil.valueEquals(nuPedido, pontExtItemPed.nuPedido) &&
			   ValueUtil.valueEquals(cdProduto, pontExtItemPed.cdProduto) &&
			   ValueUtil.valueEquals(flTipoItemPedido, pontExtItemPed.flTipoItemPedido) &&
			   ValueUtil.valueEquals(nuSeqProduto, pontExtItemPed.nuSeqProduto) &&
			   ValueUtil.valueEquals(flTipoLancamento, pontExtItemPed.flTipoLancamento);
	}

	public String getCdDomain() {
		return cdProduto;
	}

	public String getDsDomain() {
		return dsTipoLancamento;
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
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(flTipoItemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuSeqProduto);
    	strBuffer.append(";");
    	strBuffer.append(flTipoLancamento);
        return strBuffer.toString();
    }
    
    public boolean isTipoLancamentoNovoPedido() {
    	return TIPO_LANCAMENTO_NOVO_PEDIDO.equals(flTipoLancamento);
    }
    
    public boolean isTipoLancamentoEstorno() {
    	return TIPO_LANCAMENTO_ESTORNO.equals(flTipoLancamento);
    }
    
    public boolean isTipoLancamentoDevolucao() {
    	return TIPO_LANCAMENTO_DEVOLUCAO.equals(flTipoLancamento);
    }
    
    public boolean isTipoLancamentoCancelamento() {
    	return TIPO_LANCAMENTO_CANCELAMENTO.equals(flTipoLancamento);
    }
    
    public String getDsTipoLancamento() {
    	if (isTipoLancamentoNovoPedido()) return Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_NOVO_PED;
    	if (isTipoLancamentoEstorno()) return Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_ESTORNO;
    	if (isTipoLancamentoDevolucao()) return Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_DEVOLUCAO;
    	if (isTipoLancamentoCancelamento()) return Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO_CANCELAMENTO;
    	return null;
    }
    
}