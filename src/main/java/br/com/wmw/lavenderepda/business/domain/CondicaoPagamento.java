package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.CondPagtoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.PrazoPagtoPrecoService;
import totalcross.util.Date;

public class CondicaoPagamento extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPCONDICAOPAGAMENTO";

    public static final int TIPOCONDPAGTO_MEDIO = 0;
	public static final int TIPOCONDPAGTO_VENCIMENTO = 1;
    public static final int TIPOCONDPAGTO_LINHA = 2;
    public static final int TIPOCONDPAGTO_OUTRO = 3;
    public static final int TIPOCONDPAGTO_RATEIO = 4;
    public static final int TIPOCONDPAGTO_PARCELADO = 5;
    public static final int TIPOCONDPAGTO_VENCIMENTO_UNICO = 7;
    public static final int TIPOCONDPAGTO_PRAZO_BASE = 8;
    public static final int TIPOCONDPAGTO_PARCELADO_USUARIO = 9;

    public static final int PERIODO_PRAZO_BASE_MENSAL = 0;
    public static final int PERIODO_PRAZO_BASE_SEMANAL = 1;
    public static final int PERIODO_PRAZO_BASE_NENHUM = -1;

    public static final int PRAZOBASE_SEMANAL_SABADO = 6;

    public static final String USA_TIPO_PAGAMENTO_ANTES_CONDICAO = "2";
    public static final char SEPARADOR_CAMPOS = '|';

    public static final String NM_COLUNA_QTVLMINVALOR = "qtMinValor";
    public static final String NM_COLUNA_VLPCTDESCONTOTOTALPEDIDO = "vlPctDescontoTotalPedido";
    public static final String NM_COLUNA_QTDIASMAXIMOPAGAMENTO = "QTDIASMAXIMOPAGAMENTO";
    public static final String NM_COLUNA_QTDIASMEDIOSPAGAMENTO = "QTDIASMEDIOSPAGAMENTO";
    public static final String NM_COLUNA_DSCONDICAOPAGAMENTO = "DSCONDICAOPAGAMENTO";
    
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoPagamento;
    public String dsCondicaoPagamento;
    public double vlIndiceFinanceiro;
    public int qtDiasMediosPagamento;
    public int qtDiasMaximoPagamento;
    public double vlPctDescontoTotalPedido;
    public String cdTipoCondPagto;
    public String cdTipoPagamento;
    public int cdPrazoPagtoPreco;
    public int nuParcelas;
    public int nuIntervaloEntrada;
    public int nuIntervaloParcelas;
	public int nuInterMaxEditEntrada;
	public int nuInterMaxEditParcelas;
	public Date dtPagamento;
    public int nuPrazoBase;
    public String flPeriodicidadePrazoBase;
    public String flEspecial;
    public String flIgnoraLimiteCredito;
    public String cdGrupoCondicao;
    public double vlMaxPeso;
    public double vlMinPeso;
    public double vlIndiceCredito;
    public String flInformaDados;
    public double vlPctCustoFinanceiro;
    private int qtMinProduto;
    private double qtMinValor;
    public double qtMaxValor;
    public String flAbertura;
    public String flFundacao;
    public double qtMinValorParcela;
    public double qtMaxValorParcela;
    public double vlPctMaxDesconto;
    public String flPermiteIgnorarRecalculo;
    public String flPermiteEditarParcelas;
    public String flParcelaUnicaVlMinParcela;
    public String flObrigaAnexoDocumento;
    public String cdGrupoCondPagtoPolitComerc;

    public boolean filterByQtDiasMedioPagto;
    public boolean filterByQtDiasMaximoPagto;
	private PrazoPagtoPreco prazoPagtoPreco;
	public int qtMinMixProduto;
	
	public int qtMixCotaItens;
	public double vlCotaItens;
	private boolean qtMinProdutoCondPagtoTabpreco;
	private boolean qtMinValorCondPagtoTabpreco;
	public boolean filtraQtMinProduto;
	public boolean filtraQtMinValor;
	public boolean filtraQtMinMixProduto;
	
	public String notCdTipoCondPagto;
	public CondTipoPedido condTipoPedidoFilter;
	public CondPagtoTabPreco condPagtoTabPrecoFilter;
	public CondPagtoCli condPagtoCliFilter;
	public TipoCondPagtoCli tipoCondPagtoCliFilter;
	public CondPagtoSeg condPagtoSegFilter;
	public CondComCondPagto condComCondPagtoFilter;
	public CondTipoPagto condTipoPagtoFilter;
	public CondPagtoRep condPagtoRepFilter;

	public CondicaoPagamento() { }
	
	public CondicaoPagamento(String cdEmpresa, String cdRepresentante, String cdCondicaoPagamento) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdCondicaoPagamento = cdCondicaoPagamento;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof CondicaoPagamento) {
            CondicaoPagamento condicaoPagamento = (CondicaoPagamento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condicaoPagamento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condicaoPagamento.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, condicaoPagamento.cdCondicaoPagamento);
        }
        return false;
    }

	public PrazoPagtoPreco getPrazoPagtoPreco() throws SQLException {
		if (cdPrazoPagtoPreco > 0 && ((prazoPagtoPreco == null) || (cdPrazoPagtoPreco != prazoPagtoPreco.cdPrazoPagtoPreco))) {
			prazoPagtoPreco = PrazoPagtoPrecoService.getInstance().getPrazoPagtoPreco(cdPrazoPagtoPreco);
		}
		return prazoPagtoPreco;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCondicaoPagamento);
        return strBuffer.toString();
    }

    public boolean isEspecial() {
    	return ValueUtil.VALOR_SIM.equals(flEspecial);
    }

    public boolean isIgnoraLimiteCredito() {
    	return ValueUtil.VALOR_SIM.equals(flIgnoraLimiteCredito);
    }

	@Override
	public String getCdDomain() {
		return cdCondicaoPagamento;
	}

	@Override
	public String getDsDomain() {
		return dsCondicaoPagamento;
	}
	
	@Override
	public int getSortIntValue() {
		return LavenderePdaConfig.isBloqueiaCondPagtoPorDiasMaximoCliente() ? qtDiasMaximoPagamento : qtDiasMediosPagamento;
	}
	
	public int getNuParcelas() {
		return this.nuParcelas > 0 ? nuParcelas : 1;
	}

	public int getQtMinProduto(String cdTabelaPreco) throws SQLException {
		if (LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco && !qtMinProdutoCondPagtoTabpreco) {
			qtMinProduto = CondPagtoTabPrecoService.getInstance().qtMinProdutoCondpagtoTabPreco(cdCondicaoPagamento, cdTabelaPreco);
			qtMinProdutoCondPagtoTabpreco = true;
		}
		return qtMinProduto;
	}
	
	public int getQtMinProduto() {
		return this.qtMinProduto;
	}

	public void setQtMinProduto(int qtMinProduto) {
		this.qtMinProduto = qtMinProduto;
	}

	public double getQtMinValor(String cdTabelaPreco) throws SQLException {
		if (LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco && !qtMinValorCondPagtoTabpreco) {
			qtMinValor = CondPagtoTabPrecoService.getInstance().qtMinValorCondpagtoTabPreco(cdCondicaoPagamento, cdTabelaPreco);
			qtMinValorCondPagtoTabpreco = true;
		}
		return qtMinValor;
	}

	public double getQtMinValor() {
		return this.qtMinValor;
	}
	
	public void setQtMinValor(double qtMinValor) {
		this.qtMinValor = qtMinValor;
	}

	public boolean isPermiteIgnorarRecalculo() {
		return ValueUtil.getBooleanValue(flPermiteIgnorarRecalculo);
	}
	
	public boolean isPermiteEditarParcelas() {
		return ValueUtil.getBooleanValue(flPermiteEditarParcelas);
	}
	
	public boolean isParcelaUnicaVlMinParcela() {
		return ValueUtil.getBooleanValue(flParcelaUnicaVlMinParcela);
	}
	
	public boolean isObrigaAnexoDocumento() {
		return ValueUtil.getBooleanValue(this.flObrigaAnexoDocumento);
	}
}