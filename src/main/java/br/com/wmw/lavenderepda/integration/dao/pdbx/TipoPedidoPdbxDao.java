package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class TipoPedidoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoPedido();
	}

    private static TipoPedidoPdbxDao instance;
    private boolean useDistinctTipoPedido;

    public TipoPedidoPdbxDao() {
        super(TipoPedido.TABLE_NAME);
    }

    public static TipoPedidoPdbxDao getInstance() {
        if (instance == null) {
            instance = new TipoPedidoPdbxDao();
        }
        return instance;
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException {
    	//Não faz nada
    }
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
    	//Não faz nada
    }
    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
    	//Não faz nada
    }
    @Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		//Não faz nada
	}
    @Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet resultSet) {
		return null;
	}

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
    	TipoPedido tipoPedido = new TipoPedido();
    	if (!useDistinctTipoPedido) {
    		tipoPedido.rowKey = resultSet.getString("rowkey");
    	}
        tipoPedido.cdEmpresa = resultSet.getString("cdEmpresa");
    	if (!useDistinctTipoPedido) {
    		tipoPedido.cdRepresentante = resultSet.getString("cdRepresentante");
    	}
        tipoPedido.cdTipoPedido = resultSet.getString("cdTipoPedido");
        tipoPedido.dsTipoPedido = resultSet.getString("dsTipoPedido");
        tipoPedido.flDefault = resultSet.getString("flDefault");
    	tipoPedido.flBonificacao = resultSet.getString("flBonificacao");
		if (LavenderePdaConfig.isIndiceFinanceiroClienteVlItemPedido()) {
			tipoPedido.flIgnorarIndiceFinanCli = resultSet.getString("flIgnorarIndiceFinanCli");
		}
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido) {
			tipoPedido.flExcecaoCondPagto = resultSet.getString("flExcecaoCondPagto");
		}
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
			tipoPedido.flExcecaoGrupoProduto = resultSet.getString("flExcecaoGrupoProduto");
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			tipoPedido.flExcecaoProduto = resultSet.getString("flExcecaoProduto");
		}
		if (LavenderePdaConfig.usaPositivacaoVisitaPorTipoPedido) {
			tipoPedido.flVisitaPositivada = resultSet.getString("flVisitaPositivada");
		}
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			tipoPedido.flObrigaQtdProdutos = resultSet.getString("flObrigaQtdProdutos");
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			tipoPedido.flOportunidade = resultSet.getString("flOportunidade");
		}
		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido()) {
			tipoPedido.qtMinValor = resultSet.getDouble("qtMinValor");
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			tipoPedido.flIndicaKmTempo = resultSet.getString("flIndicaKmTempo");
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes) {
			tipoPedido.flGeraAtendimento = resultSet.getString("flGeraAtendimento");
		}
		if (LavenderePdaConfig.usaLocalEstoquePorTipoPedido()) {
			tipoPedido.cdLocalEstoque = resultSet.getString("cdLocalEstoque");
		}
		tipoPedido.flSimulaControleVerba = resultSet.getString("flSimulaControleVerba");
		tipoPedido.flIgnoraControleVerba = resultSet.getString("flIgnoraControleVerba");
		tipoPedido.flIgnoraControleEstoque = resultSet.getString("flIgnoraControleEstoque");
		tipoPedido.flIgnoraLimiteCreditoCliente = resultSet.getString("flIgnoraLimiteCreditoCliente");
		tipoPedido.flIgnoraClienteAtrasado = resultSet.getString("flIgnoraClienteAtrasado");
		tipoPedido.qtMinValorParcela = resultSet.getDouble("qtMinValorParcela");
		tipoPedido.flIgnoraValorMinimoPedido = resultSet.getString("flIgnoraValorMinimoPedido");
		tipoPedido.flConsisteConversaoUnidade = resultSet.getString("flConsisteConversaoUnidade");
		tipoPedido.flExigeSenha = resultSet.getString("flExigeSenha");
		tipoPedido.flObrigaInfoComplementar = resultSet.getString("flObrigaInfoComplementar");
		tipoPedido.flGeraNfe = resultSet.getString("flGeraNfe");
		tipoPedido.flGeraBoleto = resultSet.getString("flGeraBoleto");
		tipoPedido.flRelacaoPedidoErp = resultSet.getString("flRelacaoPedidoErp");
	    tipoPedido.flBloqueiaMultRelacaoPedErp = resultSet.getString("flBloqueiaMultRelacaoPedErp");
	    tipoPedido.flObrigaObservacao = resultSet.getString("flObrigaObservacao");
		if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
			tipoPedido.flTipoCredito = resultSet.getString("flTipoCredito");
		}
		if (LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			tipoPedido.flInsereLote = resultSet.getString("flInsereLote");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			tipoPedido.flConsignaPedido = resultSet.getString("flConsignaPedido");
		}
		if (LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido()) {
			tipoPedido.vlPctMaxDesconto = resultSet.getDouble("vlPctMaxDesconto");
		}
		if (LavenderePdaConfig.isUsaAcrescimoMaximoPorTipoPedido()) {
			tipoPedido.vlPctMaxAcrescimo = resultSet.getDouble("vlPctMaxAcrescimo");
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
        	tipoPedido.flIndicaClienteEntrega = resultSet.getString("flIndicaClienteEntrega");
        }
		if (!LavenderePdaConfig.naoPermiteInserirQtdDescMultipla()) {
			tipoPedido.flIgnoraInsercMultItens = resultSet.getString("flIgnoraInsercMultItens");
		}
		tipoPedido.flComplementar = resultSet.getString("flComplementar");
		tipoPedido.flEnviaEmail = resultSet.getString("flEnviaEmail");
		tipoPedido.flIgnoraEstoqueReplicacao = resultSet.getString("flIgnoraEstoqueReplicacao");
		tipoPedido.flRevalidaEstoqueFechamento = resultSet.getString("flRevalidaEstoqueFechamento");
		if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
			tipoPedido.dsNaturezaOperacao = resultSet.getString("dsNaturezaOperacao");
		}
		if (LavenderePdaConfig.usaTabelaPrecoPorTipoPedido) {
			tipoPedido.flExcecaoTabPreco = resultSet.getString("flExcecaoTabPreco"); 
		}
		if (LavenderePdaConfig.isUsaMotivoPendencia()) {
			tipoPedido.cdMotivoPendenciaBonif = resultSet.getString("cdMotivoPendenciaBonif");
			tipoPedido.nuOrdemLiberacaoBonif = resultSet.getInt("nuOrdemLiberacaoBonif");
		}
		tipoPedido.flTributacaoGeral = resultSet.getString("flTributacaoGeral");
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
			tipoPedido.flGeraNfce = resultSet.getString("flGeraNfce");
		}
		tipoPedido.flBloqueiaReplicarPedido = resultSet.getString("flBloqueiaReplicarPedido");
		tipoPedido.setFlModoEstoque(resultSet.getString("flModoEstoque"));
		tipoPedido.flIgnoraDescontoItem = resultSet.getString("flIgnoraDescontoItem");
		tipoPedido.flIgnoraQuantidadeItem = resultSet.getString("flIgnoraQuantidadeItem");
		tipoPedido.flIndicaAguardaComplemento = resultSet.getString("flIndicaAguardaComplemento");
		tipoPedido.flNaoRelacionaPedNaTrocaBonif = resultSet.getString("flNaoRelacionaPedNaTrocaBonif");
		tipoPedido.flIgnoraCalculoFrete = resultSet.getString("flIgnoraCalculoFrete");
		tipoPedido.flIgnoraMultEspecial = resultSet.getString("flIgnoraMultEspecial");
		tipoPedido.flIgnoraCalculoComissao = resultSet.getString("flIgnoraCalculoComissao");
		tipoPedido.flIgnoraPesoMinFrete = resultSet.getString("flIgnoraPesoMinFrete");
		tipoPedido.flUtilizaCondPgtoPadraoCli = resultSet.getString("flUtilizaCondPgtoPadraoCli");
		tipoPedido.flIgnoraEnvioErp = resultSet.getString("flIgnoraEnvioErp");
		if (LavenderePdaConfig.usaConfigPedidoProducao()) {
			tipoPedido.flProducao = resultSet.getString("flProducao");
		}
		if (LavenderePdaConfig.usaTabelaConfigMinParcela()) {
			tipoPedido.flIgnoraValorMinimoParcela = resultSet.getString("flIgnoraValorMinimoParcela");
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			tipoPedido.nuMinConversaoFob = resultSet.getInt("nuMinConversaoFob");
		}
		if (LavenderePdaConfig.usaEscolhaModoFeira) {
			tipoPedido.flFeira = resultSet.getString("flFeira");
		}
		tipoPedido.flBonificacaoContaCorrente = resultSet.getString("flBonificacaoContaCorrente");
		tipoPedido.flIgnoraPoliticaBonificacao = resultSet.getString("flIgnoraPoliticaBonificacao");
		tipoPedido.flIgnoraVlMinParcelaCondPagto = resultSet.getString("flIgnoraVlMinParcelaCondPagto");
		tipoPedido.cdMotivoPendenciaTroca = resultSet.getString("cdMotivoPendenciaTroca");
		return tipoPedido;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
    	if (!useDistinctTipoPedido) {
    		sql.append(" rowKey,");
    	}
        sql.append(" CDEMPRESA,");
        if (!useDistinctTipoPedido) {
        	sql.append(" CDREPRESENTANTE,");
        }
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" DSTIPOPEDIDO,");
        sql.append(" FLDEFAULT,");
    	sql.append(" FLBONIFICACAO,");
        if (LavenderePdaConfig.isIndiceFinanceiroClienteVlItemPedido()) {
        	sql.append(" flIgnorarIndiceFinanCli,");
        }
		if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido) {
			sql.append(" FLEXCECAOCONDPAGTO,");
		}
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
			sql.append(" FLEXCECAOGRUPOPRODUTO,");
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			sql.append(" FLEXCECAOPRODUTO,");
		}
		if (LavenderePdaConfig.usaPositivacaoVisitaPorTipoPedido) {
			sql.append(" FLVISITAPOSITIVADA,");
		}
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			sql.append(" FLOBRIGAQTDPRODUTOS,");
		}
		if (LavenderePdaConfig.usaValorMinimoParaPedidoPorTipoPedido()) {
			sql.append(" QTMINVALOR,");
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			sql.append(" FLOPORTUNIDADE,");
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			sql.append(" FLINDICAKMTEMPO,");
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes) {
			sql.append(" FLGERAATENDIMENTO,");
		}
		sql.append(" FLSIMULACONTROLEVERBA,");
		sql.append(" FLIGNORACONTROLEVERBA,");
		sql.append(" FLIGNORACONTROLEESTOQUE,");
		sql.append(" FLIGNORALIMITECREDITOCLIENTE,");
		sql.append(" FLIGNORACLIENTEATRASADO,");
		sql.append(" FLIGNORAVALORMINIMOPEDIDO,");
		sql.append(" QTMINVALORPARCELA,");
		sql.append(" FLCONSISTECONVERSAOUNIDADE,");
        sql.append(" FLEXIGESENHA,");
        sql.append(" FLOBRIGAINFOCOMPLEMENTAR,");
        sql.append(" FLGERANFE,");
        sql.append(" FLGERABOLETO,");
        sql.append(" FLRELACAOPEDIDOERP,");
	    sql.append("FLBLOQUEIAMULTRELACAOPEDERP,");
	    sql.append("FLOBRIGAOBSERVACAO,");
        if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
        	sql.append(" FLTIPOCREDITO,");
        }
        if (LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
        	sql.append(" FLINSERELOTE,");
        }
        if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
        	sql.append(" FLCONSIGNAPEDIDO,");
        }
        if (LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido()) {
        	sql.append(" VLPCTMAXDESCONTO,");
        }
        if (LavenderePdaConfig.isUsaAcrescimoMaximoPorTipoPedido()) {
        	sql.append(" VLPCTMAXACRESCIMO,");
        }
        if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
        	sql.append(" FLINDICACLIENTEENTREGA,");
        }
        if (!LavenderePdaConfig.naoPermiteInserirQtdDescMultipla()) {
        	sql.append(" FLIGNORAINSERCMULTITENS,");
        }
        sql.append(" FLCOMPLEMENTAR,");
        sql.append(" FLENVIAEMAIL,");
        sql.append(" FLIGNORAESTOQUEREPLICACAO,");
        sql.append(" FLREVALIDAESTOQUEFECHAMENTO,");
        if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
        	sql.append(" FLGERANFCE,");
        }
        if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
        	sql.append(" DSNATUREZAOPERACAO,");
        }
        if (LavenderePdaConfig.usaTabelaPrecoPorTipoPedido) {
        	sql.append(" FLEXCECAOTABPRECO,");
        }
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
        	sql.append(" CDMOTIVOPENDENCIABONIF,");
            sql.append(" NUORDEMLIBERACAOBONIF,");
        }
        sql.append(" FLTRIBUTACAOGERAL,");
        sql.append(" FLMODOESTOQUE,");
        sql.append(" FLBLOQUEIAREPLICARPEDIDO,");
        sql.append(" FLIGNORADESCONTOITEM,");
        sql.append(" FLIGNORAQUANTIDADEITEM,");
        sql.append(" FLINDICAAGUARDACOMPLEMENTO,");
        sql.append(" FLNAORELACIONAPEDNATROCABONIF,");
        sql.append(" FLIGNORACALCULOFRETE,");
        sql.append(" FLIGNORAMULTESPECIAL,");
        sql.append(" FLIGNORACALCULOCOMISSAO,");
        sql.append(" FLIGNORAPESOMINFRETE,");
        sql.append(" FLUTILIZACONDPGTOPADRAOCLI,");
        sql.append(" FLIGNORAENVIOERP");
        if (LavenderePdaConfig.usaConfigPedidoProducao()) {
        	sql.append(", FLPRODUCAO");
        }
        if (LavenderePdaConfig.usaTabelaConfigMinParcela()) {
        	sql.append(", FLIGNORAVALORMINIMOPARCELA");
        }
        if (LavenderePdaConfig.usaValidaConversaoFOB()) {
        	sql.append(", NUMINCONVERSAOFOB");
        }
		if (LavenderePdaConfig.usaLocalEstoquePorTipoPedido()) {
			sql.append(", CDLOCALESTOQUE");
		}
		if (LavenderePdaConfig.usaEscolhaModoFeira) {
			sql.append(", FLFEIRA");
		}
		sql.append(", FLBONIFICACAOCONTACORRENTE");
		sql.append(", FLIGNORAPOLITICABONIFICACAO");
		sql.append(", FLIGNORAVLMINPARCELACONDPAGTO");
		sql.append(", CDMOTIVOPENDENCIATROCA ");
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPedido tipoPedido = (TipoPedido) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoPedido.cdEmpresa);
       	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoPedido.cdRepresentante);
       	if (ValueUtil.isNotEmpty(tipoPedido.cdTipoPedidoFilter)) {
       		sqlWhereClause.addStartAndMultipleCondition();
       	}
       	sqlWhereClause.addAndLikeCondition("TB.DSTIPOPEDIDO", tipoPedido.dsTipoPedido);
   		sqlWhereClause.addOrLikeCondition("TB.CDTIPOPEDIDO", tipoPedido.cdTipoPedidoFilter);
   		if (ValueUtil.isNotEmpty(tipoPedido.cdTipoPedidoFilter)) {
   			sqlWhereClause.addEndMultipleCondition();
   		}
       	sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", tipoPedido.cdTipoPedido);
       	sqlWhereClause.addAndCondition("FLDEFAULT = ", tipoPedido.flDefault);
       	sqlWhereClause.addAndCondition("FLOPORTUNIDADE = ", tipoPedido.flOportunidade);
       	sqlWhereClause.addAndCondition("FLRELACAOPEDIDOERP = ", tipoPedido.flRelacaoPedidoErp);
       	sqlWhereClause.addAndCondition("FLIGNORACONTROLEVERBA = ", tipoPedido.flIgnoraControleVerba);
       	sqlWhereClause.addAndCondition("FLTIPOCREDITO = ", tipoPedido.flTipoCredito);
       	sqlWhereClause.addAndCondition("FLGERAATENDIMENTO = ", tipoPedido.flGeraAtendimento);
       	sqlWhereClause.addAndCondition("FLENVIAEMAIL = ", tipoPedido.flEnviaEmail);
       	sqlWhereClause.addAndCondition("FLINDICACLIENTEENTREGA = ", tipoPedido.flIndicaClienteEntrega);
       	sqlWhereClause.addAndCondition("FLGERANFCE = ", tipoPedido.flGeraNfce);
       	sqlWhereClause.addAndCondition("FLIGNORACALCULOFRETE = ", tipoPedido.flIgnoraCalculoFrete);
       	sqlWhereClause.addAndCondition("FLUTILIZACONDPGTOPADRAOCLI = ", tipoPedido.flUtilizaCondPgtoPadraoCli);
       	sqlWhereClause.addAndCondition("FLIGNORAENVIOERP = ", tipoPedido.flIgnoraEnvioErp);
       	sqlWhereClause.addAndCondition("FLBONIFICACAOCONTACORRENTE = ", tipoPedido.flBonificacaoContaCorrente);
       	sqlWhereClause.addAndCondition("COALESCE(FLBONIFICACAOCONTACORRENTE, 'N') <> ", tipoPedido.flBonificacaoContaCorrenteDif);
        sql.append(sqlWhereClause.getSql());
        
        if (LavenderePdaConfig.usaTipoPedidoPorCliente && tipoPedido.tipoPedidoCliFilter != null) {
        	DaoUtil.addExistsTipoPedidoCli(sql, tipoPedido.tipoPedidoCliFilter);
		}
	}
    
	public int countTipoPedidosBonificacaoCarga(TipoPedido filter) throws SQLException {
		if (filter == null) {
			return 0;
		}
    	StringBuffer sql = getSqlBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sql.append("SELECT ");
		sql.append(" COUNT(1) ");
		sql.append(" FROM ");
		sql.append(TipoPedido.TABLE_NAME);
		sqlWhereClause.addAndCondition("FLBONIFICACAO = 'S'");
		sqlWhereClause.addAndCondition("CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", filter.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString());
    }
    
	public Vector findAllDistinctTipoPedido(BaseDomain domain) throws SQLException {
		useDistinctTipoPedido = true;
		StringBuffer sql = getSqlBuffer();
		sql.append(" select distinct ");
		addSelectColumns(domain, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(domain, sql);
		addWhereByExample(domain, sql);
		addGroupBy(sql);
		addOrderBy(sql, domain);
		addLimit(sql, domain);
		Vector listTipoPedido = new Vector();
		listTipoPedido = findAll(domain, sql.toString());
		useDistinctTipoPedido = false;
		return listTipoPedido;
	}

}
