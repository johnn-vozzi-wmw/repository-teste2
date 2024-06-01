
package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class TipoPagamentoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoPagamento();
	}

    private static TipoPagamentoPdbxDao instance;

    public TipoPagamentoPdbxDao() {
        super(TipoPagamento.TABLE_NAME);
    }

    public static TipoPagamentoPdbxDao getInstance() {
        if (instance == null) {
            instance = new TipoPagamentoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null; 
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	TipoPagamento tipoPagamento = new TipoPagamento();
        tipoPagamento.rowKey = rs.getString("rowkey");
        tipoPagamento.cdEmpresa = rs.getString("cdEmpresa");
        tipoPagamento.cdRepresentante = rs.getString("cdRepresentante");
        tipoPagamento.cdTipoPagamento = rs.getString("cdTipoPagamento");
        tipoPagamento.dsTipoPagamento = rs.getString("dsTipoPagamento");
        tipoPagamento.flPagamentoAVista = rs.getString("flPagamentoAVista");
        tipoPagamento.flPermiteMultiplosPagamentos = rs.getString("flPermiteMultiplosPagamentos");
        tipoPagamento.flBoleto = rs.getString("flBoleto");
        tipoPagamento.cdBoletoConfig = rs.getString("cdBoletoConfig");
        tipoPagamento.flPermiteUsoModuloPagamento = rs.getString("flPermiteUsoModuloPagamento");
        tipoPagamento.flIgnoraLimiteCredito = rs.getString("flIgnoraLimiteCredito");
        tipoPagamento.flOcultoParaNovoCliente = rs.getString("flOcultoParaNovoCliente");
		if (LavenderePdaConfig.bloqueiaTipoPagamentoNivelSuperior) {
			tipoPagamento.nuNivel = rs.getInt("nuNivel");
		}
        if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento) {
        	tipoPagamento.qtMinValor = rs.getDouble("qtMinValor");
        }
        if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial) {
        	tipoPagamento.flEspecial = rs.getString("flEspecial");
        }
        if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
        	tipoPagamento.flCheque = rs.getString("flCheque");
        }
        if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
        	tipoPagamento.flUsaVencimento = rs.getString("flUsaVencimento");
        }
        if (LavenderePdaConfig.usaTipoPagamentoRestritoVenda) {
        	tipoPagamento.flRestrito = rs.getString("flRestrito");
        }
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	tipoPagamento.vlIndicePagamento = rs.getDouble("vlIndicePagamento");
        }
        if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
        	tipoPagamento.vlPctJuros = rs.getDouble("vlPctJuros");
        	tipoPagamento.vlPctMultaDiario = rs.getDouble("vlPctMultaDiario");
        	tipoPagamento.vlPctMulta = rs.getDouble("vlPctMulta");
        	tipoPagamento.nuDiasProtesto = rs.getInt("nuDiasProtesto");
        	tipoPagamento.nuDiasMulta = rs.getInt("nuDiasMulta");
        	tipoPagamento.nuDiasMaxPagamento = rs.getInt("nuDiasMaxPagamento");
        }
        return tipoPagamento;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTIPOPAGAMENTO,");
        sql.append(" tb.FLPERMITEMULTIPLOSPAGAMENTOS,");
        if (LavenderePdaConfig.bloqueiaTipoPagamentoNivelSuperior) {
        	sql.append(" tb.NUNIVEL,");
        }
        if (LavenderePdaConfig.valorMinimoParaPedidoPorTipoPagamento) {
        	sql.append(" tb.QTMINVALOR,");
        }
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial) {
			sql.append(" tb.FLESPECIAL,");
		}
		if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
			sql.append(" tb.FLCHEQUE,");
		}
		if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente ) {
			sql.append(" tb.FLUSAVENCIMENTO,");
		}
		if (LavenderePdaConfig.usaTipoPagamentoRestritoVenda) {
			sql.append(" tb.FLRESTRITO,");
		}
		if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
			sql.append(" VLINDICEPAGAMENTO,");
		}
		sql.append(" tb.FLPAGAMENTOAVISTA,");
		sql.append(" tb.FLBOLETO,");
		sql.append(" tb.CDBOLETOCONFIG,");		if (LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
			sql.append(" tb.VLPCTJUROS,")
			.append(" tb.VLPCTMULTADIARIO,")
			.append(" tb.VLPCTMULTA,")
			.append(" tb.NUDIASPROTESTO,")
			.append(" tb.NUDIASMULTA,")
			.append(" tb.NUDIASMAXPAGAMENTO,");
		}
        sql.append(" tb.DSTIPOPAGAMENTO,");
        sql.append(" tb.FLPERMITEUSOMODULOPAGAMENTO,");
        sql.append(" tb.FLIGNORALIMITECREDITO,");
        sql.append(" tb.FLOCULTOPARANOVOCLIENTE");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPagamento tipoPagamento = (TipoPagamento) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndConditionEquals("TB.CDEMPRESA", tipoPagamento.cdEmpresa);
        sqlWhereClause.addAndConditionEquals("TB.CDREPRESENTANTE", tipoPagamento.cdRepresentante);
    	sqlWhereClause.addAndConditionEquals("TB.FLPAGAMENTOAVISTA", tipoPagamento.flPagamentoAVista);
    	sqlWhereClause.addAndConditionNotEquals("COALESCE(TB.FLIGNORALIMITECREDITO, 'N')", tipoPagamento.notFlIgnoraLimiteCredito);
    	sqlWhereClause.addAndConditionNotEquals("COALESCE(TB.FLOCULTOPARANOVOCLIENTE, 'N')", tipoPagamento.notFlOcultoParaNovoCliente);
        if (LavenderePdaConfig.usaTipoPagamentoRestritoVenda && ValueUtil.isNotEmpty(tipoPagamento.notFlRestrito)) {
        	sqlWhereClause.addStartAndMultipleCondition();
        	sqlWhereClause.addAndCondition("COALESCE(TB.FLRESTRITO, 'N') != " + Sql.getValue(tipoPagamento.notFlRestrito));
       		sqlWhereClause.addOrCondition("TB.CDTIPOPAGAMENTO = " + Sql.getValue(tipoPagamento.cdTipoPagamento));
       		sqlWhereClause.addEndMultipleCondition();
        } else {
        	sqlWhereClause.addAndConditionEquals("TB.CDTIPOPAGAMENTO", tipoPagamento.cdTipoPagamento);
        }
        if (ValueUtil.isNotEmpty(tipoPagamento.notFlPermiteUsoModuloPagamento)) {
        	sqlWhereClause.addStartAndMultipleCondition();
        	sqlWhereClause.addAndCondition("TB.FLPERMITEUSOMODULOPAGAMENTO != " + Sql.getValue(tipoPagamento.notFlPermiteUsoModuloPagamento));
        	sqlWhereClause.addOrCondition("TB.FLPERMITEUSOMODULOPAGAMENTO IS NULL");
        	sqlWhereClause.addEndMultipleCondition();
        }
        sql.append(sqlWhereClause.getSql());
        
        if (tipoPagamento.condTipoPagtoFilter != null && LavenderePdaConfig.usaTipoPagamentoPorCondicaoPagamento()) {
        	DaoUtil.addExistsCondTipoPagto(sql, tipoPagamento.condTipoPagtoFilter, false);
        }
        if (tipoPagamento.tipoCondPagtoCliFilter != null && (LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente() || LavenderePdaConfig.usaCondPagtoPorTipoPagtoECondPagtoPorCliente())) {
        	DaoUtil.addExistsTipoCondPagtoCli(sql, tipoPagamento.tipoCondPagtoCliFilter, false);
        }
        if (tipoPagamento.tipoPagtoCliFilter != null && LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(ValueUtil.VALOR_NAO)) {
        	DaoUtil.addExistsTipoPagtoCli(sql, tipoPagamento.tipoPagtoCliFilter);
        }
        if (tipoPagamento.tipoPagtoTabPrecoFilter != null && LavenderePdaConfig.isUsaTipoPagamentoPorTabelaPreco()) {
        	DaoUtil.addExistsTipoPagtoTabPreco(sql, tipoPagamento.tipoPagtoTabPrecoFilter);
        }
        if (tipoPagamento.filtraQuandoPossuiCondPagto) {
        	DaoUtil.addTipoPagtoQuePossuiCondPagtoCondition(sql, tipoPagamento);
        }
    }

}