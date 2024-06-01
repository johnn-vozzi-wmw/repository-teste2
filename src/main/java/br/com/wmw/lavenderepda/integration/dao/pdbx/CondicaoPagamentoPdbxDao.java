package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class CondicaoPagamentoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondicaoPagamento();
	}

    private static CondicaoPagamentoPdbxDao instance;

    public CondicaoPagamentoPdbxDao() {
        super(CondicaoPagamento.TABLE_NAME);
    }

    public static CondicaoPagamentoPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondicaoPagamentoPdbxDao();
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
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" DSCONDICAOPAGAMENTO,");
        sql.append(" FLIGNORALIMITECREDITO,");
        sql.append(" FLABERTURA,");
        sql.append(" FLFUNDACAO,");
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto() || LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			sql.append(" CDTIPOCONDPAGTO,");
		}
		sql.append(" NUPARCELAS,");
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto() || LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque() || LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0 || LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
			sql.append(" NUINTERVALOPARCELAS,");
			sql.append(" NUINTERVALOENTRADA,");
			sql.append(" NUINTERMAXEDITENTRADA,");
			sql.append(" NUINTERMAXEDITPARCELAS,");
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			sql.append(" DTPAGAMENTO,");
			sql.append(" NUPRAZOBASE,");
			sql.append(" FLPERIODICIDADEPRAZOBASE,");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente)) {
			sql.append(" CDTIPOPAGAMENTO,");
		}
		sql.append(" VLINDICEFINANCEIRO,");
		sql.append(" QTDIASMEDIOSPAGAMENTO,");
		sql.append(" QTDIASMAXIMOPAGAMENTO,");
		sql.append(" QTMINVALORPARCELA,");
		sql.append(" QTMAXVALORPARCELA,");
		sql.append(" QTMAXVALOR,");
		sql.append(" FLOBRIGAANEXODOCUMENTO,");
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			sql.append(" VLPCTDESCONTOTOTALPEDIDO,");
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto()) {
			sql.append(" QTMINVALOR,");
		}
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" FLESPECIAL,");
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			sql.append(" CDPRAZOPAGTOPRECO,");
		}
		if (LavenderePdaConfig.usaGrupoCondPagtoCli) {
			sql.append(" CDGRUPOCONDICAO,");
		}
		if (LavenderePdaConfig.usaControlePesoPedidoPorCondPagto) {
			sql.append(" VLMAXPESO,");
			sql.append(" VLMINPESO,");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			sql.append(" VLINDICECREDITO,");
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			sql.append(" QTMIXCOTAITENS,");
			sql.append(" VLCOTAITENS,");
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			sql.append(" FLINFORMADADOS,");
		} 
		if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto()) {
			sql.append(" QTMINPRODUTO,");
		}
		if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtMixProduto()) {
			sql.append(" QTMINMIXPRODUTO,");
		}
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLPCTCUSTOFINANCEIRO,");
		}
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			sql.append(" VLPCTMAXDESCONTO,");	
		}
		if (LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento) {
			sql.append(" FLPERMITEIGNORARRECALCULO,");	
		}
		sql.append(" FLPERMITEEDITARPARCELAS,");	
		sql.append(" FLPARCELAUNICAVLMINPARCELA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDGRUPOCONDPAGTOPOLITCOMERC");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
        condicaoPagamento.rowKey = rs.getString("rowkey");
        condicaoPagamento.cdEmpresa = rs.getString("cdEmpresa");
        condicaoPagamento.cdRepresentante = rs.getString("cdRepresentante");
        condicaoPagamento.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condicaoPagamento.dsCondicaoPagamento = rs.getString("dsCondicaoPagamento");
        condicaoPagamento.flIgnoraLimiteCredito = rs.getString("flIgnoraLimiteCredito");
        condicaoPagamento.flAbertura = rs.getString("flAbertura");
        condicaoPagamento.flFundacao = rs.getString("flFundacao");
        condicaoPagamento.qtMaxValor = rs.getDouble("qtMaxValor");
        condicaoPagamento.flObrigaAnexoDocumento = rs.getString("flObrigaAnexoDocumento");
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto() || LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			condicaoPagamento.cdTipoCondPagto = rs.getString("cdTipoCondPagto");
		}
		condicaoPagamento.nuParcelas = rs.getInt("nuParcelas");
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto() || LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque() || LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0 || LavenderePdaConfig.isUsaGeracaoImpressaoBoletoContingencia()) {
			condicaoPagamento.nuIntervaloParcelas = rs.getInt("nuIntervaloParcelas");
			condicaoPagamento.nuIntervaloEntrada = rs.getInt("nuIntervaloEntrada");
			condicaoPagamento.nuInterMaxEditEntrada = rs.getInt("nuInterMaxEditEntrada");
			condicaoPagamento.nuInterMaxEditParcelas = rs.getInt("nuInterMaxEditParcelas");
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			condicaoPagamento.dtPagamento = rs.getDate("dtPagamento");
			condicaoPagamento.nuPrazoBase = rs.getInt("nuPrazoBase");
			condicaoPagamento.flPeriodicidadePrazoBase = rs.getString("flPeriodicidadePrazoBase");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente)) {
			condicaoPagamento.cdTipoPagamento = rs.getString("cdTipoPagamento");
		}
		condicaoPagamento.vlIndiceFinanceiro = LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento ? 1 : rs.getDouble("vlIndiceFinanceiro");
		condicaoPagamento.qtDiasMediosPagamento = rs.getInt("qtDiasMediosPagamento");
		condicaoPagamento.qtDiasMaximoPagamento = rs.getInt("qtDiasMaximoPagamento");
		condicaoPagamento.qtMinValorParcela = rs.getDouble("qtMinValorParcela");
		condicaoPagamento.qtMaxValorParcela = rs.getDouble("qtMaxValorParcela");
		condicaoPagamento.flObrigaAnexoDocumento = rs.getString("flObrigaAnexoDocumento");
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
			condicaoPagamento.vlPctDescontoTotalPedido = ValueUtil.round(rs.getDouble("vlPctDescontoTotalPedido"));
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto()) {
			condicaoPagamento.setQtMinValor(rs.getDouble("qtMinValor"));
		}
		if (LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			condicaoPagamento.flEspecial = rs.getString("flEspecial");
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			condicaoPagamento.cdPrazoPagtoPreco = rs.getInt("cdPrazoPagtoPreco");
		}
		if (LavenderePdaConfig.usaGrupoCondPagtoCli) {
			condicaoPagamento.cdGrupoCondicao = rs.getString("cdGrupoCondicao");
		}
		if (LavenderePdaConfig.usaControlePesoPedidoPorCondPagto) {
			condicaoPagamento.vlMaxPeso = rs.getDouble("vlMaxPeso");
			condicaoPagamento.vlMinPeso = rs.getDouble("vlMinPeso");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			condicaoPagamento.vlIndiceCredito = rs.getDouble("vlIndiceCredito");
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			condicaoPagamento.qtMixCotaItens = rs.getInt("qtMixCotaItens");
			condicaoPagamento.vlCotaItens = rs.getDouble("vlCotaItens");
		}
		if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
			condicaoPagamento.flInformaDados = rs.getString("flInformaDados");
		}
		if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto()) {
			condicaoPagamento.setQtMinProduto(rs.getInt("qtMinProduto"));
		}
		if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtMixProduto()) {
			condicaoPagamento.qtMinMixProduto = rs.getInt("qtMinMixProduto");
		}
		if (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			condicaoPagamento.vlPctCustoFinanceiro = rs.getDouble("vlPctCustoFinanceiro");
		}
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			condicaoPagamento.vlPctMaxDesconto = rs.getDouble("vlPctMaxDesconto");	
		}
		if (LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento) {
			condicaoPagamento.flPermiteIgnorarRecalculo = rs.getString("flPermiteIgnorarRecalculo");
		}
		condicaoPagamento.flPermiteEditarParcelas = rs.getString("flPermiteEditarParcelas");
		condicaoPagamento.flParcelaUnicaVlMinParcela = rs.getString("flParcelaUnicaVlMinParcela");
        condicaoPagamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condicaoPagamento.cdGrupoCondPagtoPolitComerc = rs.getString("cdGrupoCondPagtoPolitComerc");
        return condicaoPagamento;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondicaoPagamento condicaoPagamento = (CondicaoPagamento) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", condicaoPagamento.cdEmpresa);
       	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condicaoPagamento.cdRepresentante);
       	sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condicaoPagamento.cdCondicaoPagamento);
       	sqlWhereClause.addAndCondition("FLABERTURA = ", condicaoPagamento.flAbertura);
       	sqlWhereClause.addAndCondition("FLFUNDACAO = ", condicaoPagamento.flFundacao);
       	sqlWhereClause.addAndCondition("CDGRUPOCONDPAGTOPOLITCOMERC = ", condicaoPagamento.cdGrupoCondPagtoPolitComerc);
       	if (condicaoPagamento.filterByQtDiasMedioPagto) {
       		sqlWhereClause.addAndConditionForced("QTDIASMEDIOSPAGAMENTO <= ", condicaoPagamento.qtDiasMediosPagamento);
       	} else if (condicaoPagamento.filterByQtDiasMaximoPagto) {
       		sqlWhereClause.addAndConditionForced("QTDIASMAXIMOPAGAMENTO <= ", condicaoPagamento.qtDiasMaximoPagamento);
       	}
       	//--
       	if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente)) {
       		String[] cdTipoPagtoPadrao;
       		if (ValueUtil.isNotEmpty(condicaoPagamento.cdTipoPagamento)) {
       			cdTipoPagtoPadrao = new String[]{LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente, condicaoPagamento.cdTipoPagamento};
       		} else {
       			cdTipoPagtoPadrao = new String[]{LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente};
       		}
        	sqlWhereClause.addAndConditionIn("COALESCE(CDTIPOPAGAMENTO," + Sql.getValue(LavenderePdaConfig.usaCondPagtoPorTipoPagtoPadraoCliente) + ")", cdTipoPagtoPadrao);
        }

       	if (LavenderePdaConfig.clienteComContratoExigeSetorPedido && ValueUtil.isNotEmpty(condicaoPagamento.notCdTipoCondPagto)) {
       		sqlWhereClause.addStartAndMultipleCondition();
       		sqlWhereClause.addAndCondition("CDTIPOCONDPAGTO != " + Sql.getValue(condicaoPagamento.notCdTipoCondPagto));
       		sqlWhereClause.addOrCondition("CDTIPOCONDPAGTO IS NULL");
       		sqlWhereClause.addEndMultipleCondition();
       	}
       	if (!LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco && LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagtoRetiraOpcoesInvalidasCombo() && condicaoPagamento.filtraQtMinValor) {
       		sqlWhereClause.addAndConditionForced("QTMINVALOR <= ", condicaoPagamento.getQtMinValor());
       	}
       	if (!LavenderePdaConfig.usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco && LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() && condicaoPagamento.filtraQtMinProduto) {
       		sqlWhereClause.addAndConditionForced("QTMINPRODUTO <= ", condicaoPagamento.getQtMinProduto());
       	}
       	if (LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamentoEQtMixProduto() && LavenderePdaConfig.isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() && condicaoPagamento.filtraQtMinMixProduto) {
       		sqlWhereClause.addAndConditionForced("QTMINMIXPRODUTO <= ", condicaoPagamento.qtMinMixProduto);
       	}
       	if (LavenderePdaConfig.usaGrupoCondPagtoCli && ValueUtil.isNotEmpty(condicaoPagamento.cdGrupoCondicao)) {
       		DaoUtil.addAndMultipleOrLikeCondition(sqlWhereClause, StringUtil.split(condicaoPagamento.cdGrupoCondicao, CondicaoPagamento.SEPARADOR_CAMPOS), "CDGRUPOCONDICAO", StringUtil.getStringValue(CondicaoPagamento.SEPARADOR_CAMPOS));
       	}
        
       	sql.append(sqlWhereClause.getSql());
        
        if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido && condicaoPagamento.condTipoPedidoFilter != null) {
       		DaoUtil.addExistsCondTipoPedido(sql, condicaoPagamento.condTipoPedidoFilter);
       	}
        if (LavenderePdaConfig.usarCondicaoPagtoPorTabelaPreco && condicaoPagamento.condPagtoTabPrecoFilter != null) {
        	DaoUtil.addExistsCondPagtoTabPreco(sql, condicaoPagamento.condPagtoTabPrecoFilter, true);
        }
        if (LavenderePdaConfig.usaCondicaoPagamentoPorCliente && condicaoPagamento.condPagtoCliFilter != null) {
        	DaoUtil.addExistsCondPagtoCli(sql, condicaoPagamento.condPagtoCliFilter);
        }
        if (!LavenderePdaConfig.usaCondicaoPagamentoPorCliente && !LavenderePdaConfig.usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(ValueUtil.VALOR_NAO) && condicaoPagamento.tipoCondPagtoCliFilter != null) {
        	DaoUtil.addExistsTipoCondPagtoCli(sql, condicaoPagamento.tipoCondPagtoCliFilter, true);
        }
        if (LavenderePdaConfig.usaSegmentoNoPedido && LavenderePdaConfig.usaCondicaoPagamentoPorSegmento && condicaoPagamento.condPagtoSegFilter != null) {
        	DaoUtil.addExistsCondPagtoSeg(sql, condicaoPagamento.condPagtoSegFilter);
        }
        if (LavenderePdaConfig.usaCondicaoPagamentoPorCondicaoComercial && condicaoPagamento.condComCondPagtoFilter != null) {
        	DaoUtil.addExistsCondComCondPagto(sql, condicaoPagamento.condComCondPagtoFilter, true);
        }
        if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPagamento() && condicaoPagamento.condTipoPagtoFilter != null) {
        	DaoUtil.addExistsCondTipoPagto(sql, condicaoPagamento.condTipoPagtoFilter, true);
        }
        if (LavenderePdaConfig.usaCondicaoPagamentoRepresentante && condicaoPagamento.condPagtoRepFilter != null) {
        	DaoUtil.addExistsCondPagtoRep(sql, condicaoPagamento.condPagtoRepFilter);
        }
    }

    public Vector findAllByExampleOrderByQtDiasMedio(BaseDomain domain) throws SQLException {
	    StringBuffer sql = getSqlBuffer();
	    sql.append(" select ");
	    addSelectColumns(domain, sql);
	    sql.append(" from ");
	    sql.append(tableName);
	    addWhereByExample(domain, sql);
	    sql.append(" order by qtDiasMediosPagamento ");
	    return findAll(domain, sql.toString());
    }
    
}