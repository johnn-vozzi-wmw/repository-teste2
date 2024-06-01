package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.CondicaoNegociacaoService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.validation.ImpossivelCancelarPedidoException;
import br.com.wmw.lavenderepda.business.validation.PedidoNaoFechadoException;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PedidoPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pedido();
	}

	private static PedidoPdbxDao instance;
	private static PedidoPdbxDao instanceErp;

	private boolean isDelete;

	public PedidoPdbxDao() {
		super(Pedido.TABLE_NAME_PEDIDO);
	}

	public PedidoPdbxDao(String newTableName) {
		super(newTableName);
	}
	
	public static PedidoPdbxDao getInstance() {
		if (PedidoPdbxDao.instance == null) {
			PedidoPdbxDao.instance = new PedidoPdbxDao(Pedido.TABLE_NAME_PEDIDO);
		}
		return PedidoPdbxDao.instance;
	}

	public static PedidoPdbxDao getInstanceErp() {
		if (PedidoPdbxDao.instanceErp == null) {
			PedidoPdbxDao.instanceErp = new PedidoPdbxDao(Pedido.TABLE_NAME_PEDIDOERP);
		}
		return PedidoPdbxDao.instanceErp;
	}

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		Pedido pedidoFilter = (Pedido) domainFilter;
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.NUPEDIDO,");
		sql.append(" tb.FLORIGEMPEDIDO,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(" tb.NUPEDIDORELACIONADO,");
		sql.append(" tb.FLORIGEMPEDIDORELACIONADO,");
		sql.append(" tb.CDSTATUSPEDIDO,");
		sql.append(" tb.DTEMISSAO,");
		sql.append(" tb.HREMISSAO,");
		sql.append(" tb.HRFIMEMISSAO,");
		sql.append(" tb.DTFECHAMENTO,");
		sql.append(" tb.HRFECHAMENTO,");
		sql.append(" tb.VLTOTALITENS,");
		sql.append(" tb.VLTOTALPEDIDO,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.VLTOTALBRUTOITENS,");
		sql.append(" tb.VLTOTALBASEITENS,");
		sql.append(" tb.NUVERSAOSISTEMAORIGEM,");
		sql.append(" tb.DSURLENVIOSERVIDOR,");
		sql.append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".DSSTATUSPEDIDO DSSTATUSPEDIDO,");
		sql.append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".FLCOMPLEMENTAVEL FLCOMPLEMENTAVEL,");
		if (pedidoFilter.onResumoDiario){
			sql.append(DaoUtil.ALIAS_TIPOPEDIDO).append(".CDTIPOPEDIDO CDTIPOPEDIDOJOIN,");
			sql.append(DaoUtil.ALIAS_TIPOPEDIDO).append(".DSTIPOPEDIDO DSTIPOPEDIDO,");
			sql.append(DaoUtil.ALIAS_TIPOPEDIDO).append(".FLDEFAULT FLDEFAULT,");
			sql.append(DaoUtil.ALIAS_TIPOPEDIDO).append(".FLBONIFICACAO FLBONIFICACAO,");
			sql.append(DaoUtil.ALIAS_TIPOPEDIDO).append(".FLOPORTUNIDADE FLOPORTUNIDADE,");
			sql.append(DaoUtil.ALIAS_CLIENTE).append(".DSUFPRECO DSUFPRECO,");
		}
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 || LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais()) {
			sql.append(" tb.FLMAXVENDALIBERADOSENHA,");
		}
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaPedidoComplementar() || LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			sql.append(" tb.DTENTREGA,");
		}
		if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			sql.append(" tb.CDCONDICAOPAGAMENTO,");
		}
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			sql.append(" tb.CDTIPOPAGAMENTO,");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			sql.append(" tb.CDTIPOPEDIDO,");
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" tb.CDTIPOFRETE,");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaFretePedidoPorToneladaCliente || LavenderePdaConfig.isPermiteInserirFreteManual() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" tb.VLFRETE,");
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(" tb.CDSUPERVISOR,");
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			sql.append(" tb.VLVERBAPEDIDO,");
		}
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
			sql.append(" tb.VLVERBAPEDIDOPOSITIVO,");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			sql.append(" tb.VLPCTINDICEFINCONDPAGTO,");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			sql.append(" tb.VLPCTDESCQUANTIDADEPESO,");
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda) {
			sql.append(" tb.FLPRECOLIBERADOSENHA,");
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedido()) {
			sql.append(" tb.NUORDEMCOMPRACLIENTE,");
		}
		if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() || LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
			sql.append(" tb.FLCREDITOCLIENTELIBERADOSENHA,");
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedido()) {
			sql.append(" tb.CDROTAENTREGA,");
		}
		if (LavenderePdaConfig.relDiferencasPedido) {
			sql.append(" tb.FLPOSSUIDIFERENCA,");
			sql.append(" tb.FLPEDIDODIFERENCA,");
		}
		if (LavenderePdaConfig.geraNovoPedidoDiferencas) {
			sql.append(" tb.NUPEDIDODIFERENCA,");
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			sql.append(" tb.CDSETOR,");
			sql.append(" tb.CDORIGEMSETOR,");
		}
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			sql.append(" tb.CDTIPOENTREGA,");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) || (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) || LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			sql.append(" tb.VLDESCONTO,");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			sql.append(" tb.QTPESO,");
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			sql.append(" tb.CDAREAVENDA,");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.usaModuloTrocaNoPedido) {
			sql.append(" tb.VLTROCARECOLHER,");
			sql.append(" tb.VLTROCAENTREGAR,");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido) {
			sql.append(" tb.VLPCTDESCONTO,");
			if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
				sql.append(" tb.VLDESCONTOINDICEFINANCLIENTE,");
			}
		}
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido() || LavenderePdaConfig.permiteEditarDescontoProgressivo) {
			sql.append(" tb.VLPCTDESCPROGRESSIVO,");
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			sql.append(" tb.VLPCTDESCITEM,");
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			sql.append(" tb.VLPCTACRESCIMOITEM,");
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			sql.append(" tb.VLBONIFICACAOPEDIDO,");
		}
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido()) || (LavenderePdaConfig.indiceRentabilidadePedido > 0)) {
			sql.append(" tb.VLRENTABILIDADE,");
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			sql.append(" tb.FLPEDIDONOVOCLIENTE,");
		}
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			sql.append(" tb.DSCONDICAOPAGAMENTOSEMCADASTRO,");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			sql.append(" tb.QTPONTOSPEDIDO,");
		}
		if (LavenderePdaConfig.usaContratoForecast || LavenderePdaConfig.usaControleEstoqueGondola) {
			sql.append(" tb.FLBLOQUEADOEDICAO,");
		}
		if (LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
			sql.append(" tb.FLCLIENTEATRASADOLIBERADOSENHA,");
		}
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			sql.append(" tb.CDSEGMENTO,");
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			sql.append(" tb.CDCONDICAOCOMERCIAL,");
		}
		if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido()) {
			sql.append(" tb.DSMOTIVODESCONTO,");
		}
		if ((LavenderePdaConfig.usaTransportadoraPedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido()) || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" tb.CDTRANSPORTADORA,");
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente) {
			sql.append(" tb.VLPCTFRETEREPRESENTANTE,");
			sql.append(" tb.VLFRETEREPRESENTANTE,");
			sql.append(" tb.VLFRETECLIENTE,");
		}
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0 || LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			sql.append(" tb.FLIMPRESSO,");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) {
			sql.append(" tb.NUPEDIDORELBONIFICACAO,");
		}
		if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido()) {
			sql.append(" tb.NUPEDIDOSUGESTAO,");
			sql.append(" tb.FLORIGEMPEDIDOSUGESTAO,");
		}
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			sql.append(" tb.CDCENTROCUSTO,");
			sql.append(" tb.CDPLATAFORMAVENDA,");
			sql.append(" tb.CDITEMCONTA,");
			sql.append(" tb.CDCLASSEVALOR,");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			sql.append(" tb.VLTOTALPEDIDOESTOQUEPOSITIVO,");
		}
		if (LavenderePdaConfig.isUsaReplicacaoPedido()) {
			sql.append(" tb.FLPEDIDOREPLICADO,");
			sql.append(" tb.NUPEDIDOORIGINAL,");
		}
		if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
			sql.append(" tb.FLSUGESTAOVENDALIBERADOSENHA,");
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			sql.append(" tb.CDCARGAPEDIDO,");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
			sql.append(" tb.FLABAIXORENTABILIDADEMINIMA,");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" tb.VLPCTMARGEMMIN,");
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			sql.append(" tb.FLETAPAVERBA,");
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.mostraFaixaComissao()) {
			sql.append(" tb.VLPCTCOMISSAO,");
		}
		if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() || !LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() || !LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			sql.append(" tb.FLPAGAMENTOAVISTA,");
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() || LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
				sql.append(" tb.FLGERANFE,");
			}
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto || LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
				sql.append(" tb.FLGERABOLETO,");
			}
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			sql.append(" tb.NUKMINICIAL,");
			sql.append(" tb.NUKMFINAL,");
			sql.append(" tb.HRINICIALINDICADO,");
			sql.append(" tb.HRFINALINDICADO,");
		}
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			sql.append(" tb.FLNFEIMPRESSA,");
		}
		if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil) {
			sql.append(" tb.FLLIBERADOENTREGA,");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			sql.append(" tb.CDENDERECOCLIENTE,");
		}
		if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0) {
			sql.append(" tb.FLBOLETOIMPRESSO,");
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			sql.append(" tb.FLSITUACAORESERVAEST,");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) {
			sql.append(" tb.NUPEDIDORELTROCA,");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			sql.append(" tb.NUPEDIDOCOMPLEMENTADO,");
		}
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			sql.append(" tb.FLKEYACCOUNT,");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() || LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() || LavenderePdaConfig.usaBotaoNovoPedidoNaListaPedidos || LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado() || LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(" tb.FLPENDENTE,");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() || LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
			sql.append(" tb.FLPENDENTELIMCRED,");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			sql.append(" tb.FLGERACREDITOCONDICAO,");
			sql.append(" tb.VLTOTALCREDITOCONDICAO,");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			sql.append(" tb.FLGERACREDITOFRETE,");
			sql.append(" tb.VLTOTALCREDITOFRETE,");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			sql.append(" tb.FLITEMPENDENTE,");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			sql.append(" tb.VLPCTDESCPROGRESSIVOMIX,");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			sql.append(" tb.VLVOLUMEPEDIDO,");
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			sql.append(" tb.VLPCTDESCFRETE,");
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" tb.VLPCTDESCCLIENTE,");
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()
				|| LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()
				|| LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()
				|| (LavenderePdaConfig.isAplicaDescontoCategoria() && LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido())) {
			sql.append(" tb.VLPCTDESCONTOCONDICAO,");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
			sql.append(" tb.DTENTREGALIBERADA,");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedidoPorUsuario()) {
			sql.append(" tb.CDUSUARIOLIBENTREGA,");
		}
		if (LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada()) {
			sql.append(" tb.CDUSUARIOLIBERACAOLIMCRED ,");
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
			sql.append(" tb.VLTOTALTROCAPEDIDO,");
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			sql.append(" tb.DTCARREGAMENTO,");
		}
		if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			sql.append(" tb.FLNFECONTIMPRESSA,");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			sql.append(" tb.DTCONSIGNACAO,");
			if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
				sql.append(" tb.VLPEDIDOORIGINAL,");
				sql.append(" tb.VLTOTALDEVOLUCOES,");
			}
		}
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
			sql.append(" tb.FLCONSIGNACAOIMPRESSA,");
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			sql.append(" tb.FLCOTACONDPAGTO,");
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			sql.append(" tb.CDCONTATO,");
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			sql.append(" tb.CDCLIENTEENTREGA,");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			sql.append(" tb.NUPEDCOMPRELACIONADO,");
		}
		if (LavenderePdaConfig.usaAgrupamentoPedidoEmConsignacao) {
			sql.append(" tb.CDPEDIDOSAGRUPADOS,");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			sql.append(" tb.FLRESTRITO,");
		}
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			sql.append(" tb.CDCONDNEGOCIACAO,");
		}
		if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
			sql.append(" tb.FLMINVERBALIBERADO,");
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			sql.append(" tb.CDUNIDADE,");
		}
		if (LavenderePdaConfig.usaTransportadoraAuxiliar && LavenderePdaConfig.usaTransportadoraPedido()) {
			sql.append(" tb.CDTRANSPORTADORAAUX,");
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
			sql.append(" tb.CDENTREGA,");
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			sql.append(" tb.CDREGIAO,");
		}
		if (LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0) {
			sql.append(" tb.FLPROMISSORIAIMPRESSA, ");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" tb.VLTOTALMARGEM, ");
		}
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
			sql.append(" FLGERANFCE,");
			sql.append(" FLNFCEIMPRESSA,");
		}
		if (LavenderePdaConfig.validaSaldoPedidoBonificacao) {
			sql.append(" tb.FLSALDOBONILIBERADOSENHA, ");
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" tb.VLPCTDESC2,");
			sql.append(" tb.VLPCTDESC3,");
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
			sql.append(" tb.NUCNPJTRANSPORTADORA,");
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			sql.append(" tb.CDSTATUSORCAMENTO,")
					.append(" tb.DSOBSORCAMENTO,");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			sql.append(" tb.DTPAGAMENTO,");
		}
		if (LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente()) {
			sql.append(" tb.CDTRIBUTACAOCLIENTE,");
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			sql.append(" tb.FLDESCONTOLIBERADOSENHA,");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			sql.append(" tb.DTULTIMORECALCULOVALORES,");
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			sql.append(" tb.VLTOTALNOTACREDITO,");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			sql.append(" tb.VLPCTVPC,");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			sql.append(" tb.VLPCTDESCONTOAUTOEFETIVO,")
					.append(" tb.VLTOTALDESCONTOAUTO,")
					.append(" tb.VLDESCONTOTOTALAUTODESC,");
		}
		if (LavenderePdaConfig.enviaProtocoloPedidosEmailHtml) {
			sql.append(" tb.NULOTEPROTOCOLO,");
			sql.append(" tb.FLENVIADOPROTOCOLO,");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			sql.append(" tb.CDCOMISSAOPEDIDOREP,");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			sql.append(" tb.VLSEGUROPEDIDO,");
		}
		if (LavenderePdaConfig.isConfigLiberacaoComSenhaVlMinParcela()) {
			sql.append(" tb.FLVALORMINPARCELALIBERADOSENHA,");
		}
		if (LavenderePdaConfig.mostraObservacaoCliente()) {
			sql.append(" tb.DSOBSERVACAOCLIENTE,");
		}
		if (LavenderePdaConfig.isLiberaComSenhaCondPagamento()) {
			sql.append(" tb.QTDIASCPGTOLIBSENHA,");
			sql.append(" tb.VLTOTALPEDIDOLIBERADO,");
		}

		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" tb.CDFRETECONFIG,");
		}
		sql.append(" tb.FLCALCULASEGURO,");
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			sql.append(" tb.VLVERBAGRUPO,");
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" tb.VLPCTDESCHISTORICOVENDAS,");
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sql.append(" tb.CDLOCAL,");
		}
		if (LavenderePdaConfig.obrigaMotivoBonificacao()) {
			sql.append(" tb.DSMOTIVOBONIFICACAO,");
		}
		if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente) {
			sql.append(" tb.CDREPRESENTANTEORG,");
		}
		if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
			sql.append(" tb.CDMODOFATURAMENTO,");
			sql.append(" tb.DSOBSMODOFATURAMENTO,");
		}
		sql.append(" tb.CDCONTACORRENTE,");
		sql.append(" tb.FLAGUARDAPEDIDOCOMPLEMENTAR,");
		sql.append(" tb.FLPROCESSANDONFETXT,");
		sql.append(" tb.FLMODOESTOQUE,");
		sql.append(" tb.VLPCTTOTALMARGEM,");
		if (LavenderePdaConfig.usaControlePontuacao) {
			sql.append(" tb.VLTOTALPONTUACAOBASE,");
			sql.append(" tb.VLTOTALPONTUACAOREALIZADO,");
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			sql.append(" tb.NUPARCELAS,");
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			sql.append(" tb.FLGONDOLA,");
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			sql.append(" tb.FLCRITICO,");
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			sql.append(" tb.FLPENDENTEFOB,");
			sql.append(" tb.CDUSUARIOCANCFOB,");
			sql.append(" tb.CDUSUARIOLIBERACAOFOB,");
		}
		sql.append(" tb.FLTIPOALTERACAO,");
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" tb.VLBASEMARGEMRENTAB,");
			sql.append(" tb.VLCUSTOMARGEMRENTAB,");
			sql.append(" tb.VLPCTMARGEMRENTAB,");
			sql.append(" tb.CDMARGEMRENTAB,");
		}
		sql.append(" tb.CDUSUARIO");
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			sql.append(", CDENDERECOCOBRANCA");
		}
		sql.append(", tb.VLPCTCOMISSAOTOTAL");
		sql.append(", tb.VLRENTABILIDADETOTAL");
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			sql.append(", tb.VLRENTABTOTALITENS").append(", tb.VLRENTABSUGITENS");
		}
		sql.append(", tb.VLFRETETOTAL");
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			sql.append(", tb.FLCODIGOINTERNOCLIENTE");	
		}
		if (LavenderePdaConfig.usaPedidoPerdido) {
			sql.append(", tb.CDMOTIVOPERDA");	
		}
		if (LavenderePdaConfig.isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao()) {
			sql.append(", tb.CDMOTIVOPENDENCIA");
			sql.append(", tb.CDMOTIVOPENDENCIAJUST");
			sql.append(", tb.DSOBSMOTIVOPENDENCIA");
		}
		sql.append(", tb.VLFINALPEDIDODESCTRIBFRETE");
		sql.append(", tb.QTENCOMENDA");
		sql.append(", tb.VLTOTALENCOMENDA");
		if(LavenderePdaConfig.isMostraRentabPraticadaSugerida()){
			sql.append(", tb.VLRENTABILIDADESUG");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			sql.append(", tb.FLPENDENTECONDPAGTO");
			sql.append(", tb.DSMOTIVOCANCCONDPAGTO");
			sql.append(", tb.CDUSUARIOCANCCONDPAGTO");
			sql.append(", tb.CDUSUARIOLIBERACAOCONDPAGTO");
		}
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
			sql.append(", tb.FLCALCULAPONTUACAO");
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			sql.append(", tb.FLVINCULACAMPANHAPUBLICITARIA");
			sql.append(", tb.CDCAMPANHAPUBLICITARIA");
		}
		sql.append(", tb.QTDIASVCTOPARCELAS");
		sql.append(", tb.FLEDICAOBLOQUEADA");
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			sql.append(", tb.DTSUGESTAOCLIENTE");
		}
		sql.append(", tb.DSOBSERVACAO");
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			sql.append(", tb.VLFRETEADICIONAL");
		}
		if (LavenderePdaConfig.usaImagemQrCode) {
			sql.append(", tb.DSQRCODEPIX");
		}
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			sql.append(", tb.CDDIVISAOVENDA");
		}
		if (LavenderePdaConfig.usaDataBaseCancelamentoAutoPedidoCliente()) {
			sql.append(", tb.CDMOTIVOCANCELAMENTO");
		}
		if (pedidoFilter.addJoinTipoPedidoOnCadastroSac) {
			sql.append(", TIPOPED.DSTIPOPEDIDO");
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			sql.append(", tb.FLUTILIZARENTABILIDADE");	
		}
	}


	//@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws java.sql.SQLException {
		Pedido pedidoFilter = (Pedido) domainFilter;
		Pedido pedido = new Pedido();
		pedido.rowKey = resultSet.getString("rowkey");
		pedido.cdEmpresa = resultSet.getString("cdEmpresa");
		pedido.cdRepresentante = resultSet.getString("cdRepresentante");
		pedido.nuPedido = resultSet.getString("nuPedido");
		pedido.flOrigemPedido = resultSet.getString("flOrigemPedido");
		pedido.cdCliente = resultSet.getString("cdCliente");
		pedido.nuPedidoRelacionado = resultSet.getString("nuPedidoRelacionado");
		pedido.flOrigemPedidoRelacionado = resultSet.getString("flOrigemPedidoRelacionado");
		pedido.cdStatusPedido = resultSet.getString("cdStatusPedido");
		pedido.dtEmissao = resultSet.getDate("dtEmissao");
		pedido.hrEmissao = resultSet.getString("hrEmissao");
		pedido.hrFimEmissao =  resultSet.getString("hrFimEmissao");
		pedido.dtFechamento = resultSet.getDate("dtFechamento");
		pedido.hrFechamento =  resultSet.getString("hrFechamento");
		pedido.vlTotalItens = ValueUtil.round(resultSet.getDouble("vlTotalItens"));
		pedido.vlTotalPedido = ValueUtil.round(resultSet.getDouble("vlTotalPedido"));
		pedido.cdTabelaPreco = resultSet.getString("cdTabelaPreco");
		pedido.nuVersaoSistemaOrigem = resultSet.getString("nuVersaoSistemaOrigem");
		pedido.dsUrlEnvioServidor = resultSet.getString("dsUrlEnvioServidor");
		pedido.vlTotalBrutoItens = resultSet.getDouble("vlTotalBrutoItens");
		pedido.vlTotalBaseItens = resultSet.getDouble("vlTotalBaseItens");
		pedido.statusPedidoPda.cdStatusPedido = pedido.cdStatusPedido;
		pedido.statusPedidoPda.dsStatusPedido = resultSet.getString("DSSTATUSPEDIDO");
		pedido.statusPedidoPda.flComplementavel = resultSet.getString("FLCOMPLEMENTAVEL");
		if (pedidoFilter.onResumoDiario){
			populateResumoDiario(resultSet, pedido);
		}
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 || LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais()) {
			pedido.flMaxVendaLiberadoSenha = resultSet.getString("flMaxVendaLiberadoSenha");
		}
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaPedidoComplementar() || LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			pedido.dtEntrega = resultSet.getDate("dtEntrega");
		}
		if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			pedido.cdCondicaoPagamento = resultSet.getString("cdCondicaoPagamento");
			pedido.oldCdCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			pedido.cdTipoPagamento = resultSet.getString("cdTipoPagamento");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			pedido.cdTipoPedido = resultSet.getString("cdTipoPedido");
			pedido.oldCdTipoPedido = pedido.cdTipoPedido;
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.cdTipoFrete = resultSet.getString("cdTipoFrete");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaFretePedidoPorToneladaCliente || LavenderePdaConfig.isPermiteInserirFreteManual() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.vlFrete =  resultSet.getDouble("vlFrete");
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			pedido.cdSupervisor = resultSet.getString("cdSupervisor");
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			pedido.vlVerbaPedido = ValueUtil.round(resultSet.getDouble("vlVerbaPedido"));
			pedido.vlVerbaPedidoOld = pedido.vlVerbaPedido;
		}
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 || LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			pedido.vlDesconto = ValueUtil.round(resultSet.getDouble("vlDesconto"));
			pedido.vlDescontoOld = pedido.vlDesconto;
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			pedido.vlPctIndiceFinCondPagto = ValueUtil.round(resultSet.getDouble("vlPctIndiceFinCondPagto"));
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			pedido.vlPctDescQuantidadePeso = ValueUtil.round(resultSet.getDouble("vlPctDescQuantidadePeso"));
		}
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
			pedido.vlVerbaPedidoPositivo = ValueUtil.round(resultSet.getDouble("vlVerbaPedidoPositivo"));
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda) {
			pedido.flPrecoLiberadoSenha = resultSet.getString("flPrecoLiberadoSenha");
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedido()) {
			pedido.nuOrdemCompraCliente = resultSet.getString("nuOrdemCompraCliente");
		}
		if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() || LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
			pedido.flCreditoClienteLiberadoSenha = resultSet.getString("flCreditoClienteLiberadoSenha");
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedido()) {
			pedido.cdRotaEntrega = resultSet.getString("cdRotaEntrega");
		}
        if (LavenderePdaConfig.relDiferencasPedido) {
        	pedido.flPossuiDiferenca = resultSet.getString("flPossuiDiferenca");
        	pedido.flPedidoDiferenca = resultSet.getString("flPedidoDiferenca");
        }
        if (LavenderePdaConfig.geraNovoPedidoDiferencas) {
        	pedido.nuPedidoDiferenca = resultSet.getString("nuPedidoDiferenca");
        }
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			pedido.cdSetor = resultSet.getString("cdSetor");
			pedido.cdOrigemSetor = resultSet.getString("cdOrigemSetor");
		}
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			pedido.cdTipoEntrega = resultSet.getString("cdTipoEntrega");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) || (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) || LavenderePdaConfig.usaDescontoPonderadoPedido) {
			pedido.vlDesconto = resultSet.getDouble("vlDesconto");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			pedido.qtPeso = resultSet.getDouble("qtPeso");
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			pedido.cdAreaVenda = resultSet.getString("cdAreaVenda");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.usaModuloTrocaNoPedido) {
			pedido.vlTrocaRecolher =  resultSet.getDouble("vlTrocaRecolher");
			pedido.vlTrocaEntregar =  resultSet.getDouble("vlTrocaEntregar");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos() || LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido) {
			pedido.vlPctDesconto = resultSet.getDouble("vlPctDesconto");
			if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
				pedido.vlDescontoIndiceFinanCliente = resultSet.getDouble("vlDescontoIndiceFinanCliente");
			}
		}
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido() || LavenderePdaConfig.permiteEditarDescontoProgressivo) {
			pedido.vlPctDescProgressivo = resultSet.getDouble("vlPctDescProgressivo");
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			pedido.vlPctDescItem = resultSet.getDouble("vlPctDescItem");
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			pedido.vlPctAcrescimoItem = resultSet.getDouble("vlPctAcrescimoItem");
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao()  || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			pedido.vlBonificacaoPedido = resultSet.getDouble("vlbonificacaopedido");
		}
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido()) || (LavenderePdaConfig.indiceRentabilidadePedido > 0)) {
			pedido.vlRentabilidade = resultSet.getDouble("vlRentabilidade");
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			pedido.flPedidoNovoCliente = resultSet.getString("flPedidoNovoCliente");
		}
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			pedido.dsCondicaoPagamentoSemCadastro = resultSet.getString("dsCondicaoPagamentoSemCadastro");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			pedido.qtPontosPedido = resultSet.getInt("qtPontosPedido");
		}
		if (LavenderePdaConfig.usaContratoForecast || LavenderePdaConfig.usaControleEstoqueGondola) {
			pedido.flBloqueadoEdicao = resultSet.getString("flBloqueadoEdicao");
		}
		if (LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
			pedido.flClienteAtrasadoLiberadoSenha = resultSet.getString("flClienteAtrasadoLiberadoSenha");
		}
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			pedido.cdSegmento = resultSet.getString("cdSegmento");
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			pedido.cdCondicaoComercial = resultSet.getString("cdCondicaoComercial");
		}
		if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido()) {
			pedido.dsMotivoDesconto = resultSet.getString("dsMotivoDesconto");
		}
		if (LavenderePdaConfig.usaTransportadoraPedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.cdTransportadora = resultSet.getString("cdTransportadora");
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente) {
			pedido.vlPctFreteRepresentante =  resultSet.getDouble("vlPctFreteRepresentante");
			pedido.vlFreteRepresentante =  resultSet.getDouble("vlFreteRepresentante");
			pedido.vlFreteCliente =  resultSet.getDouble("vlFreteCliente");
		}
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0 || LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			pedido.flImpresso = resultSet.getString("flImpresso");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) {
			pedido.nuPedidoRelBonificacao = resultSet.getString("nuPedidoRelBonificacao");
		}
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			pedido.cdCentroCusto = resultSet.getString("cdCentroCusto");
			pedido.cdPlataformaVenda = resultSet.getString("cdPlataformaVenda");
			pedido.cdItemConta = resultSet.getString("cdItemConta");
			pedido.cdClasseValor = resultSet.getString("cdClasseValor");
		}
		if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido()) {
			pedido.nuPedidoSugestao = resultSet.getString("nuPedidoSugestao");
			pedido.flOrigemPedidoSugestao = resultSet.getString("flOrigemPedidoSugestao");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			pedido.vlTotalPedidoEstoquePositivo = resultSet.getDouble("vlTotalPedidoEstoquePositivo");
		}
		if (LavenderePdaConfig.isUsaReplicacaoPedido()) {
			pedido.flPedidoReplicado = resultSet.getString("flPedidoReplicado");
			pedido.nuPedidoOriginal = resultSet.getString("nuPedidoOriginal");
		}
		if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
			pedido.flSugestaoVendaLiberadoSenha = resultSet.getString("flSugestaoVendaLiberadoSenha");
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			pedido.cdCargaPedido = resultSet.getString("cdCargaPedido");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
			pedido.flAbaixoRentabilidadeMinima = ValueUtil.getBooleanValue(resultSet.getString("flAbaixoRentabilidadeMinima"));
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			pedido.vlPctMargemMin = resultSet.getDouble("vlPctMargemMin");
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			pedido.flEtapaVerba = resultSet.getString("flEtapaVerba"); 
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.mostraFaixaComissao()) {
			pedido.vlPctComissao = resultSet.getDouble("vlPctComissao");
		}
		if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() || !LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() || !LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			pedido.flPagamentoAVista = resultSet.getString("flPagamentoAVista");
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() || LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
				pedido.flGeraNfe = resultSet.getString("flGeraNfe");
			}
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto || LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
				pedido.flGeraBoleto = resultSet.getString("flGeraBoleto");
			}
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			pedido.nuKmInicial = resultSet.getInt("nuKmInicial");
			pedido.nuKmFinal = resultSet.getInt("nuKmFinal");
			pedido.hrInicialIndicado = resultSet.getString("hrInicialIndicado");
			pedido.hrFinalIndicado = resultSet.getString("hrFinalIndicado");
		}
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			pedido.flNfeImpressa = resultSet.getString("flNfeImpressa");
		}
		if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil) {
			pedido.flLiberadoEntrega = resultSet.getString("flLiberadoEntrega");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			pedido.cdEnderecoCliente = resultSet.getString("cdEnderecoCliente");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			pedido.cdEnderecoCobranca = resultSet.getString("cdEnderecoCobranca");
		}
		if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0) {
			pedido.flBoletoImpresso = resultSet.getString("flBoletoImpresso");
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			pedido.flSituacaoReservaEst = resultSet.getString("flSituacaoReservaEst");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) {
			pedido.nuPedidoRelTroca = resultSet.getString("nuPedidoRelTroca");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			pedido.nuPedidoComplementado = resultSet.getString("nuPedidoComplementado");
		}
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			pedido.flKeyAccount = resultSet.getString("flKeyAccount");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() || LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() || LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido || LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado() || LavenderePdaConfig.isUsaMotivoPendencia()) {
			pedido.flPendente = resultSet.getString("flPendente");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() || LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
			pedido.flPendenteLimCred = resultSet.getString("flPendenteLimCred");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			pedido.flGeraCreditoCondicao = resultSet.getString("FLGERACREDITOCONDICAO");
			pedido.vlTotalCreditoCondicao = resultSet.getDouble("VLTOTALCREDITOCONDICAO");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			pedido.flGeraCreditoFrete = resultSet.getString("FLGERACREDITOFRETE");
			pedido.vlTotalCreditoFrete = resultSet.getDouble("VLTOTALCREDITOFRETE");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			pedido.flItemPendente = resultSet.getString("FLITEMPENDENTE");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			pedido.vlPctDescProgressivoMix = resultSet.getDouble("VLPCTDESCPROGRESSIVOMIX");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			pedido.vlVolumePedido = resultSet.getDouble("VLVOLUMEPEDIDO");
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			pedido.vlPctDescFrete = resultSet.getDouble("VLPCTDESCFRETE");
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			pedido.oldVlPctCat1 = pedido.vlPctDescCliente = resultSet.getDouble("VLPCTDESCCLIENTE");
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() 
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()
				|| LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() 
				|| LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()
				|| (LavenderePdaConfig.isAplicaDescontoCategoria() && LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido())) {
			pedido.vlPctDescontoCondicao = resultSet.getDouble("VLPCTDESCONTOCONDICAO");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
			pedido.dtEntregaLiberada = resultSet.getDate("DTENTREGALIBERADA");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedidoPorUsuario()) {
			pedido.cdUsuarioLibEntrega = resultSet.getString("CDUSUARIOLIBENTREGA");
		}
		if (LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada()) {
			pedido.cdUsuarioLiberacaoLimCred = resultSet.getString("CDUSUARIOLIBERACAOLIMCRED");
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
			pedido.vlTotalTrocaPedido = resultSet.getDouble("vlTotalTrocaPedido");
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			pedido.dtCarregamento = resultSet.getDate("DTCARREGAMENTO");
		}
		if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			pedido.flNfeContImpressa = resultSet.getString("FLNFECONTIMPRESSA");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			pedido.dtConsignacao = resultSet.getDate("DTCONSIGNACAO");
			if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
				pedido.vlPedidoOriginal = resultSet.getDouble("VLPEDIDOORIGINAL");
				pedido.vlTotalDevolucoes = resultSet.getDouble("VLTOTALDEVOLUCOES");
			}
		}
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
			pedido.flConsignacaoImpressa = resultSet.getString("FLCONSIGNACAOIMPRESSA");
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			pedido.flCotaCondPagto = resultSet.getString("flCotaCondPagto");
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			pedido.cdContato = resultSet.getString("CDCONTATO");
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			pedido.cdClienteEntrega = resultSet.getString("cdClienteEntrega");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			pedido.nuPedCompRelacionado = resultSet.getString("NUPEDCOMPRELACIONADO");
		}
		if (LavenderePdaConfig.usaAgrupamentoPedidoEmConsignacao) {
			pedido.cdPedidosAgrupados = resultSet.getString("CDPEDIDOSAGRUPADOS");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			pedido.flRestrito = resultSet.getString("flRestrito");
		}
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			String cdCondNegociacao = resultSet.getString("cdCondNegociacao");
			setCondicaoNegociacaoNoPedido(pedido, cdCondNegociacao);
			pedido.oldCdCondicaoNegociacao = cdCondNegociacao;
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			pedido.cdUnidade = resultSet.getString("cdUnidade");
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
			pedido.cdEntrega = resultSet.getString("cdEntrega");
		}
		pedido.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
			pedido.flMinVerbaLiberado = resultSet.getString("FLMINVERBALIBERADO");
		}
		if (LavenderePdaConfig.usaTransportadoraAuxiliar && LavenderePdaConfig.usaTransportadoraPedido()) {
			pedido.cdTransportadoraAux = resultSet.getString("CDTRANSPORTADORAAUX");
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			pedido.cdRegiao = resultSet.getString("cdRegiao");
		}
		if (LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0) {
			pedido.flPromissoriaImpressa = resultSet.getString("flPromissoriaImpressa");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			pedido.vlTotalMargem = resultSet.getDouble("vlTotalMargem");
		}
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
			pedido.flGeraNfce = resultSet.getString("flGeraNfce");
			pedido.flNfceImpressa = resultSet.getString("flNfceImpressa");
		}
		if (LavenderePdaConfig.validaSaldoPedidoBonificacao) {
			pedido.flSaldoBoniLiberadoSenha = resultSet.getString("flSaldoBoniLiberadoSenha");
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			pedido.oldVlPctCat2 = pedido.vlPctDesc2 = resultSet.getDouble("vlPctDesc2");
			pedido.oldVlPctCat3 = pedido.vlPctDesc3 = resultSet.getDouble("vlPctDesc3");
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
			pedido.nuCnpjTransportadora = resultSet.getString("nuCnpjTransportadora");
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			pedido.cdStatusOrcamento = resultSet.getString("cdStatusOrcamento");
			pedido.dsObsOrcamento = resultSet.getString("dsObsOrcamento");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			pedido.dtPagamento = resultSet.getDate("dtPagamento");
		}
		if (LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente()) {
			pedido.cdTributacaoCliente = resultSet.getString("cdTributacaoCliente");
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			pedido.flDescontoLiberadoSenha = resultSet.getString("flDescontoLiberadoSenha");
		}
		if (LavenderePdaConfig.isConfigLiberacaoComSenhaVlMinParcela()) {
			pedido. flValorMinParcelaLiberadoSenha = resultSet.getString("flValorMinParcelaLiberadoSenha");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			pedido.dtUltimoRecalculoValores = resultSet.getDate("DTULTIMORECALCULOVALORES");
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			pedido.vlTotalNotaCredito = resultSet.getDouble("VLTOTALNOTACREDITO");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			pedido.vlPctVpc = resultSet.getDouble("vlPctVpc");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			pedido.vlPctDescontoAutoEfetivo = resultSet.getDouble("vlPctDescontoAutoEfetivo");
			pedido.vlTotalDescontoAuto = resultSet.getDouble("vlTotalDescontoAuto");
			pedido.vlDescontoTotalAutoDesc = resultSet.getDouble("vlDescontoTotalAutoDesc");
		}
		if (LavenderePdaConfig.enviaProtocoloPedidosEmailHtml) {
			pedido.nuLoteProtocolo = resultSet.getString("nuLoteProtocolo");
			pedido.flEnviadoProtocolo = resultSet.getString("flEnviadoProtocolo");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			pedido.cdComissaoPedidoRep = resultSet.getInt("cdComissaoPedidoRep");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			pedido.vlSeguroPedido = resultSet.getDouble("vlSeguroPedido");
		}
		if (LavenderePdaConfig.mostraObservacaoCliente()) {
			pedido.dsObservacaoCliente = resultSet.getString("dsObservacaoCliente");
		}
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			pedido.vlVerbaGrupo = resultSet.getDouble("vlVerbaGrupo");
		}
		if (LavenderePdaConfig.obrigaMotivoBonificacao()) {
			pedido.dsMotivoBonificacao = resultSet.getString("dsMotivoBonificacao");
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			pedido.cdLocal = resultSet.getString("cdLocal");
		}
		pedido.vlPctTotalMargem = resultSet.getDouble("vlPctTotalMargem");
		if (LavenderePdaConfig.isLiberaComSenhaCondPagamento()) {
			pedido.qtDiasCPgtoLibSenha = resultSet.getInt("qtDiasCPgtoLibSenha");
			pedido.vlTotalPedidoLiberado = resultSet.getInt("vlTotalPedidoLiberado");
		}
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.cdFreteConfig = resultSet.getString("cdFreteConfig");
		}
		pedido.flCalculaSeguro = resultSet.getString("flCalculaSeguro");
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			pedido.vlPctDescHistoricoVendas = resultSet.getDouble("vlPctDescHistoricoVendas");
		}
		if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
			pedido.cdModoFaturamento = resultSet.getString("cdModoFaturamento");
			pedido.dsObsModoFaturamento = resultSet.getString("dsObsModoFaturamento");
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			pedido.nuParcelas = resultSet.getInt("nuParcelas");
		}
		pedido.cdContaCorrente = resultSet.getString("cdContaCorrente");
		pedido.flAguardaPedidoComplementar = resultSet.getString("flAguardaPedidoComplementar");
		pedido.flProcessandoNfeTxt = resultSet.getString("flProcessandoNfeTxt");
		pedido.flModoEstoque = resultSet.getString("flModoEstoque");
		if (LavenderePdaConfig.usaControlePontuacao) {
			pedido.vlTotalPontuacaoBase = resultSet.getDouble("vlTotalPontuacaoBase");
			pedido.vlTotalPontuacaoRealizado = resultSet.getDouble("vlTotalPontuacaoRealizado");
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			pedido.flGondola = resultSet.getString("flGondola");
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			pedido.flCritico = resultSet.getString("flCritico");
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			pedido.flPendenteFob = resultSet.getString("flPendenteFob");
			pedido.cdUsuarioCancFob = resultSet.getString("cdUsuarioCancFob");
			pedido.cdUsuarioLiberacaoFob = resultSet.getString("cdUsuarioLiberacaoFob");
		}
		pedido.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			pedido.vlBaseMargemRentab = resultSet.getDouble("vlBaseMargemRentab");
			pedido.vlCustoMargemRentab = resultSet.getDouble("vlCustoMargemRentab");
			pedido.vlPctMargemRentab = resultSet.getDouble("vlPctMargemRentab");
			pedido.cdMargemRentab = resultSet.getString("cdMargemRentab");
		}
		pedido.cdUsuario = resultSet.getString("cdUsuario");
		pedido.vlPctComissaoTotal = resultSet.getDouble("vlPctComissaoTotal");
		pedido.vlRentabilidadeTotal = resultSet.getDouble("vlRentabilidadeTotal");
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			pedido.vlRentabTotalItens = resultSet.getDouble("vlRentabTotalItens");
			pedido.vlRentabSugItens = resultSet.getDouble("vlRentabSugItens");
		}
		pedido.vlFreteTotal = resultSet.getDouble("vlFreteTotal");
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			pedido.flCodigoInternoCliente = resultSet.getString("flCodigoInternoCliente");
		}
		if (LavenderePdaConfig.usaPedidoPerdido) {
			pedido.cdMotivoPerda = resultSet.getString("cdMotivoPerda");
		}
		if (LavenderePdaConfig.isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao()) {
			pedido.cdMotivoPendencia = resultSet.getString("cdMotivoPendencia");
			pedido.cdMotivoPendenciaJust = resultSet.getString("cdMotivoPendenciaJust");
			pedido.dsObsMotivoPendencia = resultSet.getString("dsObsMotivoPendencia");
		}
		pedido.vlFinalPedidoDescTribFrete = resultSet.getDouble("VLFINALPEDIDODESCTRIBFRETE");
		pedido.qtEncomenda = resultSet.getDouble("QTENCOMENDA");
		pedido.vlTotalEncomenda = resultSet.getDouble("VLTOTALENCOMENDA");
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			pedido.vlRentabilidadeSug = resultSet.getDouble("vlRentabilidadeSug");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			pedido.flPendenteCondPagto = resultSet.getString("flPendenteCondPagto");
			pedido.dsMotivoCancCondPagto = resultSet.getString("dsMotivoCancCondPagto");
			pedido.cdUsuarioCancCondPagto = resultSet.getString("cdUsuarioCancCondPagto");
			pedido.cdUsuarioLiberacaoCondPagto = resultSet.getString("cdUsuarioLiberacaoCondPagto");
		}
		
		pedido.qtDiasVctoParcelas = resultSet.getString("qtDiasVctoParcelas");
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
			pedido.flCalculaPontuacao = resultSet.getString("flCalculaPontuacao");
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			pedido.flVinculaCampanhaPublicitaria = resultSet.getString("flVinculaCampanhaPublicitaria");
			pedido.cdCampanhaPublicitaria = resultSet.getString("cdCampanhaPublicitaria");
		}
		pedido.flEdicaoBloqueada = resultSet.getString("FLEDICAOBLOQUEADA");
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			pedido.dtSugestaoCliente = resultSet.getDate("DTSUGESTAOCLIENTE");
		}
		pedido.dsObservacao = resultSet.getString("DSOBSERVACAO");
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			pedido.vlFreteAdicional = resultSet.getDouble("VLFRETEADICIONAL");
		}
		if (LavenderePdaConfig.usaImagemQrCode) {
			pedido.dsQrCodePix = resultSet.getString("DSQRCODEPIX");
		}
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			pedido.cdDivisaoVenda = resultSet.getString("CDDIVISAOVENDA");
		}
		if (LavenderePdaConfig.usaDataBaseCancelamentoAutoPedidoCliente()) {
			pedido.cdMotivoCancelamento = resultSet.getString("CDMOTIVOCANCELAMENTO");
		}
		if (pedidoFilter.addJoinTipoPedidoOnCadastroSac) {
			pedido.tipoPedido = new TipoPedido();
			pedido.tipoPedido.cdTipoPedido = resultSet.getString("CDTIPOPEDIDO");
			pedido.tipoPedido.dsTipoPedido = resultSet.getString("DSTIPOPEDIDO");
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			pedido.flUtilizaRentabilidade = resultSet.getString("flUtilizaRentabilidade");
		}
		return pedido;
	}

	private static void populateResumoDiario(ResultSet resultSet, Pedido pedido) throws SQLException {
		pedido.cliente = new Cliente();
		pedido.tipoPedido = new TipoPedido();
		pedido.tipoPedido.cdEmpresa = resultSet.getString("cdEmpresa");
		pedido.tipoPedido.cdRepresentante = resultSet.getString("cdRepresentante");
		pedido.tipoPedido.cdTipoPedido = resultSet.getString("cdTipoPedidoJoin");
		pedido.tipoPedido.dsTipoPedido = resultSet.getString("dsTipoPedido");
		pedido.tipoPedido.flDefault = resultSet.getString("flDefault");
		pedido.tipoPedido.flBonificacao = resultSet.getString("flBonificacao");
		pedido.tipoPedido.flOportunidade = resultSet.getString("flOportunidade");
		pedido.cliente.cdEmpresa = resultSet.getString("cdEmpresa");
		pedido.cliente.cdRepresentante = resultSet.getString("cdRepresentante");
		pedido.cliente.cdCliente = resultSet.getString("cdCliente");
		pedido.cliente.dsUfPreco = resultSet.getString("dsUfPreco");
	}

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.NUPEDIDO,");
		sql.append(" tb.FLORIGEMPEDIDO,");
		sql.append(" tb.CDSTATUSPEDIDO,");
		sql.append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".DSSTATUSPEDIDO DSSTATUSPEDIDO,");
		sql.append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".CDCORFUNDO CDCORFUNDO,");
		sql.append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".FLCOMPLEMENTAVEL FLCOMPLEMENTAVEL,");
		sql.append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".FLIGNORAHISTORICOITEM FLIGNORAHISTORICOITEM,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(DaoUtil.ALIAS_CLIENTE).append(".NMRAZAOSOCIAL NMRAZAOSOCIAL,");
		sql.append(" tb.DTEMISSAO,");
		sql.append(" tb.HREMISSAO,");
		sql.append(" tb.HRFIMEMISSAO,");
		sql.append(" tb.VLTOTALITENS,");
		sql.append(" tb.VLTOTALBASEITENS,");
		sql.append(" tb.VLTOTALPEDIDO,");
		sql.append(" tb.VLFRETE,");
		sql.append(" tb.FLPROCESSANDONFETXT,");
		sql.append(" tb.NUPEDIDORELACIONADO,");
		sql.append(" tb.FLAGUARDAPEDIDOCOMPLEMENTAR,");
		sql.append(" tb.DSOBSORCAMENTO");
		if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			sql.append(", tb.VLTROCARECOLHER");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			sql.append(", tb.CDTIPOPEDIDO");
			if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.isUsaPedidoBonificacao()) {
				sql.append(", ").append(DaoUtil.ALIAS_TIPOPEDIDO).append(".FLBONIFICACAO FLBONIFICACAO");
				sql.append(", ").append(DaoUtil.ALIAS_TIPOPEDIDO).append(".DSTIPOPEDIDO DSTIPOPEDIDO");
			}
		}
		if (LavenderePdaConfig.mostraVlCotacaoDolarPedido) {
			sql.append(", tb.VLCOTACAODOLAR");
		}
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 || LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais()) {
			sql.append(", tb.FLMAXVENDALIBERADOSENHA");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			sql.append(", tb.QTPESO");
		}
		if (LavenderePdaConfig.relResumoPedidos) {
			sql.append(", tb.CDCONDICAOPAGAMENTO");
		}
		if (LavenderePdaConfig.relDiferencasPedido) {
			sql.append(", tb.FLPOSSUIDIFERENCA");
			sql.append(", tb.FLPEDIDODIFERENCA");
			sql.append(", tb.FLTIPOALTERACAO");
		}
		if (LavenderePdaConfig.geraNovoPedidoDiferencas) {
			sql.append(", tb.NUPEDIDODIFERENCA");
		}
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaPedidoComplementar()) {
			sql.append(", tb.DTENTREGA");
		}
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade() || LavenderePdaConfig.isUsaGerenciamentoRentabilidadeComBaseNaRentabilidadeDoPedido() || LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
			sql.append(", tb.VLRENTABILIDADE");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			sql.append(", tb.FLITEMPENDENTE");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() || LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento || LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(", tb.FLPENDENTE");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			sql.append(", tb.VLVOLUMEPEDIDO");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			sql.append(", tb.NUPEDCOMPRELACIONADO");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			sql.append(", tb.DTCONSIGNACAO");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			sql.append(", tb.FLRESTRITO,");
		}
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			sql.append(", tb.CDCONDNEGOCIACAO");
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			sql.append(", tb.CDSTATUSORCAMENTO");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			sql.append(", tb.DTULTIMORECALCULOVALORES");
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			sql.append(", tb.VLTOTALNOTACREDITO");
			sql.append(", tb.VLTOTALBRUTOITENS");
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sql.append(", tb.CDLOCAL");
		}
		if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
			sql.append(", tb.NUORDEMCOMPRACLIENTE");
		}
		if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente) {
			sql.append(", tb.CDREPRESENTANTEORG");
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			sql.append(", ").append(DaoUtil.ALIAS_STATUSORCAMENTO).append(".DSSTATUSORCAMENTO");
		}
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			sql.append(", tb.VLRENTABILIDADESUG")
				.append(", tb.VLRENTABTOTALITENS")
				.append(", tb.VLRENTABSUGITENS");
		}
		sql.append(", tb.QTDIASVCTOPARCELAS");
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(", tb.VLPCTMARGEMRENTAB");
		}
		if (LavenderePdaConfig.usaMarcadorPedido) {
			addSelectMarcadores(sql);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			sql.append(", (SELECT COUNT(1) as qtde FROM ( " +
					"SELECT MAX(solAut.CDSOLAUTORIZACAO), solAut.FLAUTORIZADO FROM TBLVPSOLAUTORIZACAO solAut  " +
					"where solAut.CDEMPRESA = tb.CDEMPRESA" +
					" and solAut.CDREPRESENTANTE = tb.CDREPRESENTANTE " +
					" and solAut.FLORIGEMPEDIDO = tb.FLORIGEMPEDIDO " +
					" and solAut.NUPEDIDO = tb.NUPEDIDO " +
					" and solAut.CDCLIENTE = tb.CDCLIENTE" +
					" and solAut.FLVISUALIZADO <> 'S'" +
					" and (solAut.FLEXCLUIDO <> 'S' OR solAut.FLEXCLUIDO IS NULL) " +
					"GROUP BY solAut.CDEMPRESA, solAut.CDREPRESENTANTE, solAut.FLORIGEMPEDIDO, solAut.NUPEDIDO, solAut.CDCLIENTE, solAut.CDPRODUTO, solAut.FLTIPOITEMPEDIDO, solAut.CDTIPOSOLAUTORIZACAO ) " +
					"TA WHERE TA.FLAUTORIZADO <> 'S') NUSOLAUTORIZACAOPENDENTEOUNAOAUTORIZADA");
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			sql.append(", tb.FLVINCULACAMPANHAPUBLICITARIA");
			sql.append(", tb.CDCAMPANHAPUBLICITARIA");
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(", REP.NMREPRESENTANTE NMREPRESENTANTE");
		}
		
		sql.append(", tb.FLEDICAOBLOQUEADA");
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			sql.append(", tb.DTSUGESTAOCLIENTE");
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			sql.append(", tb.VLFRETEADICIONAL");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			sql.append(", tb.FLPENDENTECONDPAGTO");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()) {
			sql.append(", tb.FLPENDENTELIMCRED");
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			sql.append(", tb.VLBONIFICACAOPEDIDO");
		}
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		Pedido pedido = new Pedido();
		pedido.rowKey = resultSet.getString("rowkey");
		pedido.cdEmpresa = resultSet.getString("cdEmpresa");
		pedido.cdRepresentante = resultSet.getString("cdRepresentante");
		pedido.nuPedido = resultSet.getString("nuPedido");
		pedido.flOrigemPedido = resultSet.getString("flOrigemPedido");
		pedido.cdStatusPedido = resultSet.getString("cdStatusPedido");
		populateStatusPedidoPda(resultSet, pedido);
		pedido.cdCliente = resultSet.getString("cdCliente");
		populateSummaryCliente(pedido, resultSet);
		pedido.dtEmissao = resultSet.getDate("dtEmissao");
		pedido.hrEmissao = resultSet.getString("hrEmissao");
		pedido.vlTotalItens = resultSet.getDouble("vlTotalItens");
		pedido.vlFrete = resultSet.getDouble("vlFrete");
		pedido.vlTotalPedido = resultSet.getDouble("vlTotalPedido");
		pedido.vlTotalBaseItens = resultSet.getDouble("vlTotalBaseItens");
		pedido.nuPedidoRelacionado = resultSet.getString("nuPedidoRelacionado");
		pedido.flProcessandoNfeTxt = resultSet.getString("flProcessandoNfeTxt");
		pedido.flAguardaPedidoComplementar = resultSet.getString("flAguardaPedidoComplementar");
		pedido.dsObsOrcamento = resultSet.getString("dsObsOrcamento");
		if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			pedido.vlTrocaRecolher = ValueUtil.round(resultSet.getDouble("vlTrocaRecolher"));
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			pedido.cdTipoPedido = resultSet.getString("cdTipoPedido");
			if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.isUsaPedidoBonificacao()) {
				populateSummaryTipoPedido(pedido, resultSet);
			}
		}
		if (LavenderePdaConfig.mostraVlCotacaoDolarPedido) {
			pedido.vlCotacaoDolar = ValueUtil.round(resultSet.getDouble("vlCotacaoDolar"));
		}
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 || LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais()) {
			pedido.flMaxVendaLiberadoSenha = resultSet.getString("flMaxVendaLiberadoSenha");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			pedido.qtPeso = ValueUtil.round(resultSet.getDouble("qtPeso"));
		}
		if (LavenderePdaConfig.relResumoPedidos) {
			pedido.cdCondicaoPagamento = resultSet.getString("cdCondicaoPagamento");
		}
		if (LavenderePdaConfig.relDiferencasPedido) {
			pedido.flPossuiDiferenca = resultSet.getString("flPossuiDiferenca");
			pedido.flPedidoDiferenca = resultSet.getString("flPedidoDiferenca");
			pedido.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		}
		if (LavenderePdaConfig.geraNovoPedidoDiferencas) {
        	pedido.nuPedidoDiferenca = resultSet.getString("nuPedidoDiferenca");
        }
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaPedidoComplementar()) {
			pedido.dtEntrega = resultSet.getDate("dtEntrega");
		}
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade() || LavenderePdaConfig.isUsaGerenciamentoRentabilidadeComBaseNaRentabilidadeDoPedido() || LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
			pedido.vlRentabilidade = ValueUtil.round(resultSet.getDouble("vlRentabilidade"));
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			pedido.flItemPendente = resultSet.getString("flItemPendente");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() || LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento || LavenderePdaConfig.isUsaMotivoPendencia()) {
			pedido.flPendente = resultSet.getString("flPendente");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			pedido.vlVolumePedido = resultSet.getDouble("vlVolumePedido");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			pedido.nuPedCompRelacionado = resultSet.getString("nuPedCompRelacionado");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			pedido.dtConsignacao = resultSet.getDate("DTCONSIGNACAO");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			pedido.flRestrito = resultSet.getString("flRestrito");
		}
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			setCondicaoNegociacaoNoPedido(pedido, resultSet.getString("cdCondNegociacao"));
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			pedido.cdStatusOrcamento = resultSet.getString("cdStatusOrcamento");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			pedido.dtUltimoRecalculoValores = resultSet.getDate("DTULTIMORECALCULOVALORES");
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			pedido.vlTotalNotaCredito = resultSet.getDouble("VLTOTALNOTACREDITO");
			pedido.vlTotalBrutoItens = resultSet.getDouble("VLTOTALBRUTOITENS");
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			pedido.cdLocal = resultSet.getString("CDLOCAL");
		}
		if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
			pedido.nuOrdemCompraCliente = resultSet.getString("NUORDEMCOMPRACLIENTE");
		}
		if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente) {
			pedido.cdRepresentanteOrg = resultSet.getString("cdRepresentanteOrg"); 
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			pedido.statusOrcamento.dsStatusOrcamento = resultSet.getString("dsStatusOrcamento");
		}
		if (LavenderePdaConfig.usaMarcadorPedido) {
			pedido.cdMarcadores = resultSet.getString("cdMarcadores");
		}
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			pedido.vlRentabTotalItens = resultSet.getDouble("VLRENTABTOTALITENS");
			pedido.vlRentabSugItens = resultSet.getDouble("VLRENTABSUGITENS");
			pedido.vlRentabilidadeSug = resultSet.getDouble("vlRentabilidadeSug");
		}
		pedido.qtDiasVctoParcelas = resultSet.getString("qtDiasVctoParcelas");
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			pedido.nuSolAutorizacaoPendenteOuNaoAutorizada = resultSet.getInt("nuSolAutorizacaoPendenteOuNaoAutorizada");
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			pedido.flVinculaCampanhaPublicitaria = resultSet.getString("flVinculaCampanhaPublicitaria");
			pedido.cdCampanhaPublicitaria = resultSet.getString("cdCampanhaPublicitaria");
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			pedido.nmRepresentante = resultSet.getString("NMREPRESENTANTE");
		}
		pedido.flEdicaoBloqueada = resultSet.getString("flEdicaoBloqueada");
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			pedido.dtSugestaoCliente = resultSet.getDate("DTSUGESTAOCLIENTE");
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			pedido.vlPctMargemRentab = resultSet.getDouble("vlPctMargemRentab");
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			pedido.vlFreteAdicional = resultSet.getDouble("VLFRETEADICIONAL");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			pedido.flPendenteCondPagto = resultSet.getString("FLPENDENTECONDPAGTO");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()) {
			pedido.flPendenteLimCred = resultSet.getString("FLPENDENTELIMCRED");
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			pedido.vlBonificacaoPedido = resultSet.getDouble("VLBONIFICACAOPEDIDO");
		}
		return pedido;
	}

	private void populateSummaryTipoPedido(Pedido pedido, ResultSet resultSet) throws SQLException {
		TipoPedido tipoPedido = new TipoPedido();
		tipoPedido.cdEmpresa = pedido.cdEmpresa;
		tipoPedido.cdRepresentante = pedido.cdRepresentante;
		tipoPedido.cdTipoPedido = pedido.cdTipoPedido;
		tipoPedido.dsTipoPedido = resultSet.getString("DSTIPOPEDIDO");
		tipoPedido.flBonificacao = resultSet.getString("FLBONIFICACAO");
		pedido.setTipoPedido(tipoPedido);
	}

	private void populateSummaryCliente(Pedido pedido, ResultSet resultSet) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = pedido.cdEmpresa;
		cliente.cdRepresentante = pedido.cdRepresentante;
		cliente.cdCliente = pedido.cdCliente;
		cliente.nmRazaoSocial = resultSet.getString("nmRazaoSocial");
		pedido.setCliente(cliente);
	}

	private void populateStatusPedidoPda(ResultSet resultSet, Pedido pedido) throws SQLException {
		pedido.statusPedidoPda.cdStatusPedido = pedido.cdStatusPedido;
		pedido.statusPedidoPda.dsStatusPedido = resultSet.getString("DSSTATUSPEDIDO");
		pedido.statusPedidoPda.cdCorFundo = resultSet.getInt("cdCorFundo");
		pedido.statusPedidoPda.flComplementavel = resultSet.getString("flComplementavel");
		pedido.statusPedidoPda.flIgnoraHistoricoItem = resultSet.getString("flIgnoraHistoricoItem");
	}

	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		Pedido pedido = (Pedido) domain;
		if ("ROWKEY".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.getRowKey());
		}
		if ("CDEMPRESA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdEmpresa);
		}
		if ("CDREPRESENTANTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdRepresentante);
		}
		if ("NUPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuPedido);
		}
		if ("FLORIGEMPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flOrigemPedido);
		}
		if ("CDCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdCliente);
		}
		if ("NUPEDIDORELACIONADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuPedidoRelacionado);
		}
		if ("FLORIGEMPEDIDORELACIONADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flOrigemPedidoRelacionado);
		}
		if ("CDSTATUSPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdStatusPedido);
		}
		if ("DTEMISSAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtEmissao);
		}
		if ("HREMISSAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.hrEmissao);
		}
		if ("HRFIMEMISSAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.hrFimEmissao);
		}
		if ("DTFECHAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtFechamento);
		}
		if ("HRFECHAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.hrFechamento);
		}
		if ("VLTOTALITENS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalItens);
		}
		if ("VLTOTALPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalPedido);
		}
		if ("CDTABELAPRECO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTabelaPreco);
		}
		if ("VLTOTALBRUTOITENS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalBrutoItens);
		}
		if ("VLTOTALBASEITENS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalBaseItens);
		}
		if ("NUVERSAOSISTEMAORIGEM".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuVersaoSistemaOrigem);
		}
		if ("DSURLENVIOSERVIDOR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsUrlEnvioServidor);
		}
		if ("DTENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtEntrega);
		}
		if ("CDCONDICAOPAGAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdCondicaoPagamento);
		}
		if ("CDTIPOPAGAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTipoPagamento);
		}
		if ("CDTIPOPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTipoPedido);
		}
		if ("CDTIPOFRETE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTipoFrete);
		}
		if ("VLFRETE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlFrete);
		}
		if ("CDSUPERVISOR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdSupervisor);
		}
		if ("VLVERBAPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlVerbaPedido);
		}
		if ("VLVERBAPEDIDOPOSITIVO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlVerbaPedidoPositivo);
		}
		if ("FLPRECOLIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPrecoLiberadoSenha);
		}
		if ("NUORDEMCOMPRACLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuOrdemCompraCliente);
		}
		if ("FLCREDITOCLIENTELIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flCreditoClienteLiberadoSenha);
		}
		if ("CDROTAENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdRotaEntrega);
		}
		if ("FLPOSSUIDIFERENCA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPossuiDiferenca);
		}
		if ("NUPEDIDODIFERENCA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuPedidoDiferenca);
		}
		if ("CDSETOR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdSetor);
		}
		if ("CDORIGEMSETOR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdOrigemSetor);
		}
		if ("CDTIPOENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTipoEntrega);
		}
		if ("VLDESCONTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlDesconto);
		}
		if ("QTPESO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.qtPeso);
		}
		if ("CDAREAVENDA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdAreaVenda);
		}
		if ("VLTROCARECOLHER".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTrocaRecolher);
		}
		if ("VLTROCAENTREGAR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTrocaEntregar);
		}
		if ("VLPCTDESCONTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDesconto);
		}
		if ("VLPCTDESCPROGRESSIVO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescProgressivo);
		}
		if ("VLPCTDESCITEM".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescItem);
		}
		if ("VLBONIFICACAOPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlBonificacaoPedido);
		}
		if ("FLPEDIDONOVOCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPedidoNovoCliente);
		}
		if ("DSCONDICAOPAGAMENTOSEMCADASTRO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsCondicaoPagamentoSemCadastro);
		}
		if ("QTPONTOSPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.qtPontosPedido);
		}
		if ("VLRENTABILIDADE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlRentabilidade);
		}
		if ("FLBLOQUEADOEDICAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flBloqueadoEdicao);
		}
		if ("NUCARIMBO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if ("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flTipoAlteracao);
		}
		if ("FLMAXVENDALIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flMaxVendaLiberadoSenha);
		}
		if ("FLCLIENTEATRASADOLIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flClienteAtrasadoLiberadoSenha);
		}
		if ("CDUSUARIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuario);
		}
		if ("CDSEGMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdSegmento);
		}
		if ("CDCONDICAOCOMERCIAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdCondicaoComercial);
		}
		if ("DSMOTIVODESCONTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsMotivoDesconto);
		}
		if ("CDTRANSPORTADORA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTransportadora);
		}
		if ("VLPCTFRETEREPRESENTANTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctFreteRepresentante);
		}
		if ("VLFRETEREPRESENTANTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlFreteRepresentante);
		}
		if ("VLFRETECLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlFreteCliente);
		}
		if ("NUPEDIDORELBONIFICACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuPedidoRelBonificacao);
		}
		if ("VLTOTALPEDIDOESTOQUEPOSITIVO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalPedidoEstoquePositivo);
		}
		if ("CDCENTROCUSTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdCentroCusto);
		}
		if ("CDPLATAFORMAVENDA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdPlataformaVenda);
		}
		if ("CDITEMCONTA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdItemConta);
		}
		if ("CDCLASSEVALOR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdClasseValor);
		}
		if ("FLSUGESTAOVENDALIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flSugestaoVendaLiberadoSenha);
		}
		if ("CDCARGAPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdCargaPedido);
		}
		if ("FLABAIXORENTABILIDADEMINIMA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(StringUtil.getStringValue(pedido.flAbaixoRentabilidadeMinima));
		}
		if ("VLPCTMARGEMMIN".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctMargemMin);
		}
		if ("FLETAPAVERBA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flEtapaVerba);
		}
		if ("VLPCTCOMISSAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctComissao);
		}
		if ("FLPAGAMENTOAVISTA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPagamentoAVista);
		}
		if ("FLGERANFE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flGeraNfe);
		}
		if ("FLGERABOLETO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flGeraBoleto);
		}
		if ("NUKMINICIAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuKmInicial);
		}
		if ("NUKMFINAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuKmFinal);
		}
		if ("HRINICIALINDICADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.hrInicialIndicado);
		}
		if ("HRFINALINDICADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.hrFinalIndicado);
		}
		if ("FLNFEIMPRESSA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flNfeImpressa);
		}
		if ("FLLIBERADOENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flLiberadoEntrega);
		}
		if ("CDENDERECOCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdEnderecoCliente);
		}
		if ("CDENDERECOCOBRANCA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdEnderecoCobranca);
		}
		if ("FLBOLETOIMPRESSO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flBoletoImpresso);
		}
		if ("FLSITUACAORESERVAEST".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flSituacaoReservaEst);
		}
		if ("NUPEDIDORELTROCA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuPedidoRelTroca);
		}
		if ("NUPEDIDOCOMPLEMENTADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuPedidoComplementado);
		}
		if ("FLKEYACCOUNT".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flKeyAccount);
		}
		if ("FLPENDENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPendente);
		}
		if ("FLPENDENTELIMCRED".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPendenteLimCred);
		}
		if ("FLGERACREDITOCONDICAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flGeraCreditoCondicao);
		}
		if ("VLTOTALCREDITOCONDICAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalCreditoCondicao);
		}
		if ("FLGERACREDITOFRETE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flGeraCreditoFrete);
		}
		if ("VLTOTALCREDITOFRETE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalCreditoFrete);
		}
		if ("FLITEMPENDENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flItemPendente);
		}
		if ("VLPCTDESCPROGRESSIVOMIX".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescProgressivoMix);
		}
		if ("VLVOLUMEPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlVolumePedido);
		}
		if ("VLPCTDESCFRETE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescFrete);
		}
		if ("VLPCTDESCCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescCliente);
		}
		if ("VLPCTDESCONTOCONDICAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescontoCondicao);
		}
		if ("DTENTREGALIBERADA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtEntregaLiberada);
		}
		if ("CDUSUARIOLIBENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuarioLibEntrega);
		}
		if ("CDUSUARIOLIBERACAOLIMCRED".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuarioLiberacaoLimCred);
		}
		if ("VLTOTALTROCAPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalTrocaPedido);
		}
		if ("DTCARREGAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtCarregamento);
		}
		if ("FLNFECONTIMPRESSA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flNfeContImpressa);
		}
		if ("FLCONSIGNACAOIMPRESSA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flConsignacaoImpressa);
		}
		if ("FLCOTACONDPAGTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flCotaCondPagto);
		}
		if ("CDCONTATO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdContato);
		}
		if ("CDCLIENTEENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdClienteEntrega);
		}
		if ("CDPEDIDOSAGRUPADOS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdPedidosAgrupados);
		}
		if ("FLRESTRITO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flRestrito);
		}
		if ("CDCONDNEGOCIACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdCondNegociacao);
		}
		if ("FLMINVERBALIBERADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flMinVerbaLiberado);
		}
		if ("CDUNIDADE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUnidade);
		}
		if ("CDTRANSPORTADORAAUX".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdTransportadoraAux);
		}
		if ("CDENTREGA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdEntrega);
		}
		if ("FLRENTABILIDADELIBERADA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flRentabilidadeLiberada);
		}
		if ("FLGERANFCE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flGeraNfce);
		}
		if ("FLNFCEIMPRESSA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flNfceImpressa);
		}
		if (LavenderePdaConfig.isPermiteEnviarEmailIgnoraFlEnviaEmail() && "FLENVIAEMAIL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(ValueUtil.VALOR_SIM);
		}
		if ("VLTOTALMARGEM".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalMargem);
		}
		if ("FLSALDOBONILIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flSaldoBoniLiberadoSenha);
		}
		if ("VLPCTDESC2".equalsIgnoreCase(columnName)) { 
			return Sql.getValue(pedido.vlPctDesc2);
		}
		if ("VLPCTDESC3".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDesc3);
		}
		if ("NUCNPJTRANSPORTADORA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuCnpjTransportadora);
		}
		if ("CDSTATUSORCAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdStatusOrcamento);
		}
		if ("DSOBSORCAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsObsOrcamento);
		}
		if ("FLMODOESTOQUE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flModoEstoque);
		}
		if ("DTPAGAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtPagamento);
		}
		if ("FLDESCONTOLIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flDescontoLiberadoSenha);
		}
		if ("FLVALORMINPARCELALIBERADOSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flValorMinParcelaLiberadoSenha);
		}
		if ("FLAGUARDAPEDIDOCOMPLEMENTAR".equalsIgnoreCase(columnName)){
			return Sql.getValue(pedido.flAguardaPedidoComplementar);
		}
		if ("DTULTIMORECALCULOVALORES".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtUltimoRecalculoValores);
		}
		if ("VLTOTALNOTACREDITO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalNotaCredito);
		}
		if ("VLPCTVPC".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctVpc);
		}
		if ("VLPCTDESCONTOAUTOEFETIVO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescontoAutoEfetivo);
		}
		if ("VLTOTALDESCONTOAUTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalDescontoAuto);
		}
		if ("VLDESCONTOTOTALAUTODESC".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlDescontoTotalAutoDesc);
		}
		if ("NULOTEPROTOCOLO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuLoteProtocolo);
		}
		if ("FLENVIADOPROTOCOLO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flEnviadoProtocolo);
		}
		if ("CDCOMISSAOPEDIDOREP".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdComissaoPedidoRep);
		}
		if ("VLSEGUROPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlSeguroPedido);
		}
		if ("DSOBSERVACAOCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsObservacaoCliente);
		}
		if ("VLPCTTOTALMARGEM".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctTotalMargem);
		}
		if ("CDLOCAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdLocal);
		}
		if ("QTDIASCPGTOLIBSENHA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.qtDiasCPgtoLibSenha);
		}
		if ("VLTOTALPEDIDOLIBERADO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlTotalPedidoLiberado);
		}
		if ("CDFRETECONFIG".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdFreteConfig);
		}
		if ("FLCALCULASEGURO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flCalculaSeguro);
		}
		if ("VLVERBAFORNECEDOR".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlVerbaFornecedor);
		}
		if ("VLVERBAGRUPO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlVerbaGrupo);
		}
		if ("VLPCTDESCHISTORICOVENDAS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctDescHistoricoVendas);
		}
		if ("DSMOTIVOBONIFICACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsMotivoBonificacao);
		}
		if ("CDMODOFATURAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdModoFaturamento);
		}
		if ("DSOBSMODOFATURAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsObsModoFaturamento);
		}
		if ("NUPARCELAS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.nuParcelas);
		}
		if ("CDCONTACORRENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdContaCorrente);
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			if ("VLTOTALPONTUACAOBASE".equalsIgnoreCase(columnName)) {
				return Sql.getValue(pedido.vlTotalPontuacaoBase);
			}
			if ("VLTOTALPONTUACAOREALIZADO".equalsIgnoreCase(columnName)) {
				return Sql.getValue(pedido.vlTotalPontuacaoRealizado);
			}
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			if ("VLBASEMARGEMRENTAB".equalsIgnoreCase(columnName)) {
				return Sql.getValue(pedido.vlBaseMargemRentab);
			}
			if ("VLCUSTOMARGEMRENTAB".equalsIgnoreCase(columnName)) {
				return Sql.getValue(pedido.vlCustoMargemRentab);
			}
			if ("VLPCTMARGEMRENTAB".equalsIgnoreCase(columnName)) {
				return Sql.getValue(pedido.vlPctMargemRentab);
			}
			if ("CDMARGEMRENTAB".equalsIgnoreCase(columnName)) {
				return Sql.getValue(pedido.cdMargemRentab);
			}
		}
		if ("CDENDERECOCOBRANCA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdEnderecoCobranca);
		}
		if ("FLGONDOLA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flGondola);
		}
		if ("FLCRITICO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flCritico);
		}
		if ("FLPENDENTEFOB".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPendenteFob);
		}
		if ("CDUSUARIOCANCFOB".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuarioCancFob);
		}
		if ("CDUSUARIOLIBERACAOFOB".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuarioLiberacaoFob);
		}
		if ("VLPCTCOMISSAOTOTAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlPctComissaoTotal);
		}
		if ("VLRENTABILIDADETOTAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlRentabilidadeTotal);
		}
		if ("VLFRETETOTAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlFreteTotal);
		}
		if ("FLCODIGOINTERNOCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flCodigoInternoCliente);
		}
		if ("CDMOTIVOPERDA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdMotivoPerda);
		}
		if ("VLFINALPEDIDODESCTRIBFRETE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlFinalPedidoDescTribFrete);
		}
		if ("VLRENTABILIDADESUG".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlRentabilidadeSug);
		}
		if ("QTDIASVCTOPARCELAS".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.qtDiasVctoParcelas);
		}
		if ("FLPENDENTECONDPAGTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flPendenteCondPagto);
		}
		if ("DSMOTIVOCANCCONDPAGTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsMotivoCancCondPagto);
		}
		if ("CDUSUARIOCANCCONDPAGTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuarioCancCondPagto);
		}
		if ("CDUSUARIOLIBERACAOCONDPAGTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdUsuarioLiberacaoCondPagto);
		}
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias && "FLCALCULAPONTUACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flCalculaPontuacao);
		}
		if ("FLEDICAOBLOQUEADA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flEdicaoBloqueada);
		}
		if ("DTSUGESTAOCLIENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dtSugestaoCliente);
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido() && "VLFRETEADICIONAL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.vlFreteAdicional);
		}
		if ("DSOBSERVACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.dsObservacao);
		}
		if ("CDDIVISAOVENDA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.cdDivisaoVenda);
		}
		if ("FLUTILIZARENTABILIDADE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedido.flUtilizaRentabilidade);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}
 
	//@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		Pedido pedido = (Pedido) domain;
		addUpdateValuesPerson(pedido, sql);
		//--
		sql.append(" CDSTATUSPEDIDO = ").append(Sql.getValue(pedido.cdStatusPedido)).append(",");
		sql.append(" DTEMISSAO = ").append(Sql.getValue(pedido.dtEmissao)).append(",");		
		sql.append(" HREMISSAO = ").append(Sql.getValue(pedido.hrEmissao)).append(",");
		sql.append(" HRFIMEMISSAO = ").append(Sql.getValue(pedido.hrFimEmissao)).append(",");
		sql.append(" DTFECHAMENTO = ").append(Sql.getValue(pedido.dtFechamento)).append(",");
		sql.append(" HRFECHAMENTO = ").append(Sql.getValue(pedido.hrFechamento)).append(",");
		sql.append(" VLTOTALITENS = ").append(Sql.getValue(pedido.vlTotalItens)).append(",");
		sql.append(" VLTOTALPEDIDO = ").append(Sql.getValue(pedido.vlTotalPedido)).append(",");
		sql.append(" CDTABELAPRECO = ").append(Sql.getValue(pedido.cdTabelaPreco)).append(",");
		sql.append(" VLTOTALBRUTOITENS = ").append(Sql.getValue(pedido.vlTotalBrutoItens)).append(",");
		sql.append(" VLTOTALBASEITENS = ").append(Sql.getValue(pedido.vlTotalBaseItens)).append(",");
		sql.append(" NUVERSAOSISTEMAORIGEM = ").append(Sql.getValue(pedido.nuVersaoSistemaOrigem)).append(",");
		sql.append(" DSURLENVIOSERVIDOR = ").append(Sql.getValue(pedido.dsUrlEnvioServidor)).append(",");
		if (LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			sql.append(" CDCLIENTE = ").append(Sql.getValue(pedido.cdCliente)).append(",");
		}
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 || LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais()) {
			sql.append(" FLMAXVENDALIBERADOSENHA = ").append(Sql.getValue(pedido.flMaxVendaLiberadoSenha)).append(",");
		}
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaPedidoComplementar() || LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			sql.append(" DTENTREGA = ").append(Sql.getValue(pedido.dtEntrega)).append(",");
		}
		if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			sql.append(" CDCONDICAOPAGAMENTO = ").append(Sql.getValue(pedido.cdCondicaoPagamento)).append(",");
		}
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			sql.append(" CDTIPOPAGAMENTO = ").append(Sql.getValue(pedido.cdTipoPagamento)).append(",");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			sql.append(" CDTIPOPEDIDO = ").append(Sql.getValue(pedido.cdTipoPedido)).append(",");
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" CDTIPOFRETE = ").append(Sql.getValue(pedido.cdTipoFrete)).append(",");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaFretePedidoPorToneladaCliente || LavenderePdaConfig.isPermiteInserirFreteManual() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" VLFRETE = ").append(Sql.getValue(pedido.vlFrete)).append(",");
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(" CDSUPERVISOR = ").append(Sql.getValue(pedido.cdSupervisor)).append(",");
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			sql.append(" VLVERBAPEDIDO = ").append(Sql.getValue(pedido.vlVerbaPedido)).append(",");
		}
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
			sql.append(" VLVERBAPEDIDOPOSITIVO = ").append(Sql.getValue(pedido.vlVerbaPedidoPositivo)).append(",");
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda) {
			sql.append(" FLPRECOLIBERADOSENHA = ").append(Sql.getValue(pedido.flPrecoLiberadoSenha)).append(",");
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedido()) {
			sql.append(" NUORDEMCOMPRACLIENTE = ").append(Sql.getValue(pedido.nuOrdemCompraCliente)).append(",");
		}
		if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() || LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
			sql.append(" FLCREDITOCLIENTELIBERADOSENHA = ").append(Sql.getValue(pedido.flCreditoClienteLiberadoSenha)).append(",");
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedido()) {
			sql.append(" CDROTAENTREGA = ").append(Sql.getValue(pedido.cdRotaEntrega)).append(",");
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			sql.append(" CDSETOR = ").append(Sql.getValue(pedido.cdSetor)).append(",");
			sql.append(" CDORIGEMSETOR = ").append(Sql.getValue(pedido.cdOrigemSetor)).append(",");
		}
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			sql.append(" CDTIPOENTREGA = ").append(Sql.getValue(pedido.cdTipoEntrega)).append(",");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) || (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) || LavenderePdaConfig.usaDescontoPonderadoPedido || LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			sql.append(" VLDESCONTO = ").append(Sql.getValue(pedido.vlDesconto)).append(",");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			sql.append(" QTPESO = ").append(Sql.getValue(pedido.qtPeso)).append(",");
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			sql.append(" CDAREAVENDA = ").append(Sql.getValue(pedido.cdAreaVenda)).append(",");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.usaModuloTrocaNoPedido) {
			sql.append(" VLTROCARECOLHER = ").append(Sql.getValue(pedido.vlTrocaRecolher)).append(",");
			sql.append(" VLTROCAENTREGAR = ").append(Sql.getValue(pedido.vlTrocaEntregar)).append(",");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos() || LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido) {
			sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(pedido.vlPctDesconto)).append(",");
			if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
				sql.append(" VLDESCONTOINDICEFINANCLIENTE = ").append(Sql.getValue(pedido.vlDescontoIndiceFinanCliente)).append(",");
			}
		}
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido() || LavenderePdaConfig.permiteEditarDescontoProgressivo) {
			sql.append(" VLPCTDESCPROGRESSIVO = ").append(Sql.getValue(pedido.vlPctDescProgressivo)).append(",");
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			sql.append(" VLPCTDESCITEM = ").append(Sql.getValue(pedido.vlPctDescItem)).append(",");
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			sql.append(" VLPCTACRESCIMOITEM = ").append(Sql.getValue(pedido.vlPctAcrescimoItem)).append(",");
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao()  || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			sql.append(" VLBONIFICACAOPEDIDO = ").append(Sql.getValue(pedido.vlBonificacaoPedido)).append(",");
		}
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido()) || (LavenderePdaConfig.indiceRentabilidadePedido > 0)) {
			sql.append(" VLRENTABILIDADE = ").append(Sql.getValue(pedido.vlRentabilidade)).append(",");
		}
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			sql.append(" DSCONDICAOPAGAMENTOSEMCADASTRO = ").append(Sql.getValue(pedido.dsCondicaoPagamentoSemCadastro)).append(",");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			sql.append(" QTPONTOSPEDIDO = ").append(Sql.getValue(pedido.qtPontosPedido)).append(",");
		}
		if (LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
			sql.append(" FLCLIENTEATRASADOLIBERADOSENHA = ").append(Sql.getValue(pedido.flClienteAtrasadoLiberadoSenha)).append(",");
		}
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			sql.append(" CDSEGMENTO = ").append(Sql.getValue(pedido.cdSegmento)).append(",");
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			sql.append(" CDCONDICAOCOMERCIAL = ").append(Sql.getValue(pedido.cdCondicaoComercial)).append(",");
		}
		if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido()) {
			sql.append(" DSMOTIVODESCONTO = ").append(Sql.getValue(pedido.dsMotivoDesconto)).append(",");
		}
		if (LavenderePdaConfig.usaTransportadoraPedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" CDTRANSPORTADORA = ").append(Sql.getValue(pedido.cdTransportadora)).append(",");
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && pedido.transportadoraReg != null) {
			sql.append(" VLMINPEDIDOTRANSPREG = ").append(Sql.getValue(pedido.transportadoraReg.vlMinPedido)).append(",");
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente) {
			sql.append(" VLPCTFRETEREPRESENTANTE = ").append(Sql.getValue(pedido.vlPctFreteRepresentante)).append(",");
			sql.append(" VLFRETEREPRESENTANTE = ").append(Sql.getValue(pedido.vlFreteRepresentante)).append(",");
			sql.append(" VLFRETECLIENTE = ").append(Sql.getValue(pedido.vlFreteCliente)).append(",");
		}
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0 || LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			sql.append(" FLIMPRESSO = ").append(Sql.getValue(pedido.flImpresso)).append(",");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) {
			sql.append(" NUPEDIDORELBONIFICACAO = ").append(Sql.getValue(pedido.nuPedidoRelBonificacao)).append(",");
		}
		if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido()) {
			sql.append(" NUPEDIDOSUGESTAO = ").append(Sql.getValue(pedido.nuPedidoSugestao)).append(",");
			sql.append(" FLORIGEMPEDIDOSUGESTAO = ").append(Sql.getValue(pedido.flOrigemPedidoSugestao)).append(",");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			sql.append(" VLTOTALPEDIDOESTOQUEPOSITIVO = ").append(Sql.getValue(pedido.vlTotalPedidoEstoquePositivo)).append(",");
		}
		sql.append(" FLBLOQUEADOEDICAO = ").append(Sql.getValue(pedido.flBloqueadoEdicao)).append(",");
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			sql.append(" CDCENTROCUSTO = ").append(Sql.getValue(pedido.cdCentroCusto)).append(",");
			sql.append(" CDPLATAFORMAVENDA = ").append(Sql.getValue(pedido.cdPlataformaVenda)).append(",");
			sql.append(" CDITEMCONTA = ").append(Sql.getValue(pedido.cdItemConta)).append(",");
			sql.append(" CDCLASSEVALOR = ").append(Sql.getValue(pedido.cdClasseValor)).append(",");
		}
		if (LavenderePdaConfig.isUsaReplicacaoPedido()) {
			sql.append(" FLPEDIDOREPLICADO = ").append(Sql.getValue(pedido.flPedidoReplicado)).append(",");
			sql.append(" NUPEDIDOORIGINAL = ").append(Sql.getValue(pedido.nuPedidoOriginal)).append(",");
		}
		if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
			sql.append(" FLSUGESTAOVENDALIBERADOSENHA = ").append(Sql.getValue(pedido.flSugestaoVendaLiberadoSenha)).append(",");
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			sql.append(" CDCARGAPEDIDO = ").append(Sql.getValue(pedido.cdCargaPedido)).append(",");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
			sql.append(" FLABAIXORENTABILIDADEMINIMA = ").append(Sql.getValue(StringUtil.getStringValue(pedido.flAbaixoRentabilidadeMinima))).append(",");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" VLPCTMARGEMMIN = ").append(Sql.getValue(pedido.vlPctMargemMin)).append(",");
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.mostraFaixaComissao()) {
			sql.append(" VLPCTCOMISSAO = ").append(Sql.getValue(pedido.vlPctComissao)).append(",");
		}
		if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() || LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() || !LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			sql.append(" FLPAGAMENTOAVISTA = ").append(Sql.getValue(pedido.flPagamentoAVista)).append(",");
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() || LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
				sql.append(" FLGERANFE = ").append(Sql.getValue(pedido.flGeraNfe)).append(",");
			}
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto || LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
				sql.append(" FLGERABOLETO = ").append(Sql.getValue(pedido.flGeraBoleto)).append(",");
			}
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			sql.append(" NUKMINICIAL = ").append(Sql.getValue(pedido.nuKmInicial)).append(",");
			sql.append(" NUKMFINAL = ").append(Sql.getValue(pedido.nuKmFinal)).append(",");
			sql.append(" HRINICIALINDICADO = ").append(Sql.getValue(pedido.hrInicialIndicado)).append(",");
			sql.append(" HRFINALINDICADO = ").append(Sql.getValue(pedido.hrFinalIndicado)).append(",");
		}
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			sql.append(" FLNFEIMPRESSA = ").append(Sql.getValue(pedido.flNfeImpressa)).append(",");
		}
		if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil) {
			sql.append(" FLLIBERADOENTREGA = ").append(Sql.getValue(pedido.flLiberadoEntrega)).append(",");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			sql.append(" CDENDERECOCLIENTE = ").append(Sql.getValue(pedido.cdEnderecoCliente)).append(",");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			sql.append(" CDENDERECOCOBRANCA = ").append(Sql.getValue(pedido.cdEnderecoCobranca)).append(",");
		}
		if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0) {
			sql.append(" FLBOLETOIMPRESSO = ").append(Sql.getValue(pedido.flBoletoImpresso)).append(",");
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizado() || LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			sql.append(" FLSITUACAORESERVAEST = ").append(Sql.getValue(pedido.flSituacaoReservaEst)).append(",");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) {
			sql.append(" NUPEDIDORELTROCA = ").append(Sql.getValue(pedido.nuPedidoRelTroca)).append(",");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			sql.append(" NUPEDIDOCOMPLEMENTADO = ").append(Sql.getValue(pedido.nuPedidoComplementado)).append(",");
		}
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			sql.append(" FLKEYACCOUNT = ").append(Sql.getValue(pedido.flKeyAccount)).append(",");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() ||  LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() || LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido || LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado() || LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(" FLPENDENTE = ").append(Sql.getValue(pedido.flPendente)).append(",");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() || LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
			sql.append(" FLPENDENTELIMCRED = ").append(Sql.getValue(pedido.flPendenteLimCred)).append(",");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			sql.append(" FLGERACREDITOCONDICAO = ").append(Sql.getValue(pedido.flGeraCreditoCondicao)).append(",");
			sql.append(" VLTOTALCREDITOCONDICAO = ").append(Sql.getValue(pedido.vlTotalCreditoCondicao)).append(",");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			sql.append(" FLGERACREDITOFRETE = ").append(Sql.getValue(pedido.flGeraCreditoFrete)).append(",");
			sql.append(" VLTOTALCREDITOFRETE = ").append(Sql.getValue(pedido.vlTotalCreditoFrete)).append(",");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			sql.append(" FLITEMPENDENTE = ").append(Sql.getValue(pedido.flItemPendente)).append(",");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			sql.append(" VLPCTDESCPROGRESSIVOMIX = ").append(Sql.getValue(pedido.vlPctDescProgressivoMix)).append(",");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			sql.append(" VLVOLUMEPEDIDO = ").append(Sql.getValue(pedido.vlVolumePedido)).append(",");
		}
		if (LavenderePdaConfig.geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			sql.append(" FLETAPAVERBA = ").append(Sql.getValue(pedido.flEtapaVerba)).append(",");
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			sql.append(" VLPCTDESCFRETE = ").append(Sql.getValue(pedido.vlPctDescFrete)).append(",");
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" VLPCTDESCCLIENTE = ").append(Sql.getValue(pedido.vlPctDescCliente)).append(",");
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()
				|| LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()
				|| LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()
				|| (LavenderePdaConfig.isAplicaDescontoCategoria() && LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido())) {
			sql.append(" VLPCTDESCONTOCONDICAO = ").append(Sql.getValue(pedido.vlPctDescontoCondicao)).append(",");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
			sql.append(" DTENTREGALIBERADA = ").append(Sql.getValue(pedido.dtEntregaLiberada)).append(",");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedidoPorUsuario()) {
			sql.append(" CDUSUARIOLIBENTREGA = ").append(Sql.getValue(pedido.cdUsuarioLibEntrega)).append(",");
		}
		if (LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada()) {
			sql.append(" CDUSUARIOLIBERACAOLIMCRED = ").append(Sql.getValue(pedido.cdUsuarioLiberacaoLimCred)).append(",");
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
			sql.append(" VLTOTALTROCAPEDIDO = ").append(Sql.getValue(pedido.vlTotalTrocaPedido)).append(",");
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			sql.append(" DTCARREGAMENTO = ").append(Sql.getValue(pedido.dtCarregamento)).append(",");
		}
		if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			sql.append(" FLNFECONTIMPRESSA = ").append(Sql.getValue(pedido.flNfeContImpressa)).append(",");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			sql.append(" DTCONSIGNACAO = ").append(Sql.getValue(pedido.dtConsignacao)).append(",");
			if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
				sql.append(" VLPEDIDOORIGINAL = ").append(Sql.getValue(pedido.vlPedidoOriginal)).append(",");
				sql.append(" VLTOTALDEVOLUCOES  = ").append(Sql.getValue(pedido.vlTotalDevolucoes)).append(",");
			}
		}
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
			sql.append(" FLCONSIGNACAOIMPRESSA = ").append(Sql.getValue(pedido.flConsignacaoImpressa)).append(",");
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			sql.append(" FLCOTACONDPAGTO = ").append(Sql.getValue(pedido.flCotaCondPagto)).append(",");
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			sql.append(" CDCONTATO = ").append(Sql.getValue(pedido.cdContato)).append(",");
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			sql.append(" CDCLIENTEENTREGA = ").append(Sql.getValue(pedido.cdClienteEntrega)).append(",");
		}
		if (LavenderePdaConfig.usaAgrupamentoPedidoEmConsignacao) {
			sql.append(" CDPEDIDOSAGRUPADOS = ").append(Sql.getValue(pedido.cdPedidosAgrupados)).append(",");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			sql.append(" FLRESTRITO = ").append(Sql.getValue(pedido.flRestrito)).append(",");
		}
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			sql.append(" CDCONDNEGOCIACAO = ").append(Sql.getValue(pedido.cdCondNegociacao)).append(",");
		}
		if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
			sql.append(" FLMINVERBALIBERADO = ").append(Sql.getValue(pedido.flMinVerbaLiberado)).append(",");
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			sql.append(" CDUNIDADE = ").append(Sql.getValue(pedido.cdUnidade)).append(",");
		}
		if (LavenderePdaConfig.usaTransportadoraAuxiliar && LavenderePdaConfig.usaTransportadoraPedido()) {
			sql.append(" CDTRANSPORTADORAAUX = ").append(Sql.getValue(pedido.cdTransportadoraAux)).append(",");
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
			sql.append(" CDENTREGA = ").append(Sql.getValue(pedido.cdEntrega)).append(",");
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			sql.append(" CDREGIAO = ").append(Sql.getValue(pedido.cdRegiao)).append(",");
		}
		if (LavenderePdaConfig.isBloqueiaFechamentoPedidoRentabilidadeMinima()) {
			sql.append(" FLRENTABILIDADELIBERADA = ").append(Sql.getValue(pedido.flRentabilidadeLiberada)).append(",");
		}
		if (LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0) {
			sql.append(" FLPROMISSORIAIMPRESSA = ").append(Sql.getValue(pedido.flPromissoriaImpressa)).append(",");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLTOTALMARGEM = ").append(Sql.getValue(pedido.vlTotalMargem)).append(",");
		}
		if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
			sql.append(" FLGERANFCE = ").append(Sql.getValue(pedido.flGeraNfce)).append(",");
			sql.append(" FLNFCEIMPRESSA = ").append(Sql.getValue(pedido.flNfceImpressa)).append(",");
		}
		if (LavenderePdaConfig.validaSaldoPedidoBonificacao) {
			sql.append(" FLSALDOBONILIBERADOSENHA = ").append(Sql.getValue(pedido.flSaldoBoniLiberadoSenha)).append(",");
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			sql.append(" VLPCTDESC2 = ").append(Sql.getValue(pedido.vlPctDesc2)).append(",");
			sql.append(" VLPCTDESC3 = ").append(Sql.getValue(pedido.vlPctDesc3)).append(",");
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
			sql.append(" NUCNPJTRANSPORTADORA = ").append(Sql.getValue(pedido.nuCnpjTransportadora)).append(",");
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			sql.append(" CDSTATUSORCAMENTO = ").append(Sql.getValue(pedido.cdStatusOrcamento)).append(",")
			.append(" DSOBSORCAMENTO = ").append(Sql.getValue(pedido.dsObsOrcamento)).append(",");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			sql.append(" DTPAGAMENTO = ").append(Sql.getValue(pedido.dtPagamento)).append(",");
		}
		if (LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente()) {
			sql.append(" cdTributacaoCliente = ").append(Sql.getValue(pedido.cdTributacaoCliente)).append(",");
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			sql.append(" flDescontoLiberadoSenha = ").append(Sql.getValue(pedido.flDescontoLiberadoSenha)).append(",");
			sql.append(" flItemPendente = ").append(Sql.getValue(pedido.flItemPendente)).append(",");
		}
		if (LavenderePdaConfig.isConfigLiberacaoComSenhaVlMinParcela()) {
			sql.append(" flValorMinParcelaLiberadoSenha = ").append(Sql.getValue(pedido.flValorMinParcelaLiberadoSenha )).append(",");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			sql.append(" DTULTIMORECALCULOVALORES = ").append(Sql.getValue(pedido.dtUltimoRecalculoValores)).append(",");
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			sql.append(" VLTOTALNOTACREDITO = ").append(Sql.getValue(pedido.vlTotalNotaCredito)).append(",");
		} 
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			sql.append(" VLPCTVPC = ").append(Sql.getValue(pedido.vlPctVpc)).append(",");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			sql.append(" VLPCTDESCONTOAUTOEFETIVO = ").append(Sql.getValue(pedido.vlPctDescontoAutoEfetivo)).append(",")
			.append(" VLTOTALDESCONTOAUTO = ").append(Sql.getValue(pedido.vlTotalDescontoAuto)).append(",")
			.append(" VLDESCONTOTOTALAUTODESC = ").append(Sql.getValue(pedido.vlDescontoTotalAutoDesc)).append(",");
		}
		if (LavenderePdaConfig.enviaProtocoloPedidosEmailHtml) {
			sql.append(" NULOTEPROTOCOLO = ").append(Sql.getValue(pedido.nuLoteProtocolo)).append(",");
			sql.append(" FLENVIADOPROTOCOLO = ").append(Sql.getValue(pedido.flEnviadoProtocolo)).append(",");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			sql.append(" CDCOMISSAOPEDIDOREP = ").append(Sql.getValue(pedido.cdComissaoPedidoRep)).append(",");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			sql.append(" VLSEGUROPEDIDO = ").append(Sql.getValue(pedido.vlSeguroPedido)).append(",");
		}
		if (LavenderePdaConfig.mostraObservacaoCliente()) {
			sql.append(" DSOBSERVACAOCLIENTE = ").append(Sql.getValue(pedido.dsObservacaoCliente)).append(",");
		}
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			sql.append(" VLVERBAGRUPO = ").append(Sql.getValue(pedido.vlVerbaGrupo)).append(",");
		}
		if (LavenderePdaConfig.obrigaMotivoBonificacao()) {
			sql.append(" DSMOTIVOBONIFICACAO = ").append(Sql.getValue(pedido.dsMotivoBonificacao)).append(",");
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sql.append(" CDLOCAL = ").append(Sql.getValue(pedido.cdLocal)).append(",");
		}
		sql.append(" VLPCTTOTALMARGEM = ").append(Sql.getValue(pedido.vlPctTotalMargem)).append(",");
		if (LavenderePdaConfig.isLiberaComSenhaCondPagamento()) {
			sql.append(" QTDIASCPGTOLIBSENHA = ").append(Sql.getValue(pedido.qtDiasCPgtoLibSenha)).append(",");
			sql.append(" VLTOTALPEDIDOLIBERADO = ").append(Sql.getValue(pedido.vlTotalPedidoLiberado)).append(",");
		}
		
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			sql.append(" CDFRETECONFIG = ").append(Sql.getValue(pedido.cdFreteConfig)).append(",");
		}
		
		sql.append(" FLCALCULASEGURO = ").append(Sql.getValue(pedido.flCalculaSeguro)).append(",");
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" VLPCTDESCHISTORICOVENDAS = ").append(Sql.getValue(pedido.vlPctDescHistoricoVendas)).append(",");
		}
		if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
			sql.append(" CDMODOFATURAMENTO = ").append(Sql.getValue(pedido.cdModoFaturamento)).append(",");
			sql.append(" DSOBSMODOFATURAMENTO = ").append(Sql.getValue(pedido.dsObsModoFaturamento)).append(",");
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			sql.append(" NUPARCELAS = ").append(Sql.getValue(pedido.nuParcelas)).append(",");
		}
		sql.append(" CDCONTACORRENTE = ").append(Sql.getValue(pedido.cdContaCorrente)).append(",");
		sql.append(" FLAGUARDAPEDIDOCOMPLEMENTAR = ").append(Sql.getValue(pedido.flAguardaPedidoComplementar)).append(",");
		sql.append(" FLPROCESSANDONFETXT = ").append(Sql.getValue(pedido.flProcessandoNfeTxt)).append(",");
		sql.append(" FLMODOESTOQUE = ").append(Sql.getValue(pedido.flModoEstoque)).append(",");
		if (LavenderePdaConfig.usaControlePontuacao) {
			sql.append(" VLTOTALPONTUACAOBASE = ").append(Sql.getValue(pedido.vlTotalPontuacaoBase)).append(",");
			sql.append(" VLTOTALPONTUACAOREALIZADO = ").append(Sql.getValue(pedido.vlTotalPontuacaoRealizado)).append(",");
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			sql.append(" FLGONDOLA = ").append(Sql.getValue(pedido.flGondola)).append(",");
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			sql.append(" FLCRITICO = ").append(Sql.getValue(pedido.flCritico)).append(",");
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			sql.append(" FLPENDENTEFOB = ").append(Sql.getValue(pedido.flPendenteFob)).append(",");
		}
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedido.flTipoAlteracao)).append(",");
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLBASEMARGEMRENTAB = ").append(Sql.getValue(pedido.vlBaseMargemRentab)).append(",");
			sql.append(" VLCUSTOMARGEMRENTAB = ").append(Sql.getValue(pedido.vlCustoMargemRentab)).append(",");
			sql.append(" VLPCTMARGEMRENTAB = ").append(Sql.getValue(pedido.vlPctMargemRentab)).append(",");
			sql.append(" CDMARGEMRENTAB = ").append(Sql.getValue(pedido.cdMargemRentab)).append(",");
		}
		sql.append(" CDUSUARIO = ").append(Sql.getValue(pedido.cdUsuario));
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			sql.append(", CDENDERECOCOBRANCA = ").append(Sql.getValue(pedido.cdEnderecoCobranca));
		}
		sql.append(", VLPCTCOMISSAOTOTAL = ").append(Sql.getValue(pedido.vlPctComissaoTotal));
		sql.append(", VLRENTABILIDADETOTAL = ").append(Sql.getValue(pedido.vlRentabilidadeTotal));
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			sql.append(", VLRENTABTOTALITENS = ").append(Sql.getValue(pedido.vlRentabTotalItens));
			sql.append(", VLRENTABSUGITENS = ").append(Sql.getValue(pedido.vlRentabSugItens));
		}
		sql.append(", VLFRETETOTAL = ").append(Sql.getValue(pedido.vlFreteTotal));
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			sql.append(", FLCODIGOINTERNOCLIENTE = ").append(Sql.getValue(pedido.flCodigoInternoCliente));	
		}
		if (LavenderePdaConfig.usaPedidoPerdido) {
			sql.append(", CDMOTIVOPERDA = ").append(Sql.getValue(pedido.cdMotivoPerda));	
		}
		if (LavenderePdaConfig.isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao()) {
			sql.append(", CDMOTIVOPENDENCIA = ").append(Sql.getValue(pedido.cdMotivoPendencia));
			sql.append(", CDMOTIVOPENDENCIAJUST = ").append(Sql.getValue(pedido.cdMotivoPendenciaJust));
			sql.append(", DSOBSMOTIVOPENDENCIA = ").append(Sql.getValue(pedido.dsObsMotivoPendencia));
		}
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			sql.append(", VLRENTABILIDADESUG = ").append(Sql.getValue(pedido.vlRentabilidadeSug));
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			sql.append(", FLPENDENTECONDPAGTO = ").append(Sql.getValue(pedido.flPendenteCondPagto));
		}
		sql.append(",").append(" VLFINALPEDIDODESCTRIBFRETE = ").append(Sql.getValue(pedido.vlFinalPedidoDescTribFrete));
		sql.append(",").append(" QTDIASVCTOPARCELAS = ").append(Sql.getValue(pedido.qtDiasVctoParcelas));
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			sql.append(",").append(" DTSUGESTAOCLIENTE = ").append(Sql.getValue(pedido.dtSugestaoCliente));
		}
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
			sql.append(",").append(" FLCALCULAPONTUACAO = ").append(Sql.getValue(pedido.flCalculaPontuacao));
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			sql.append(",").append(" FLVINCULACAMPANHAPUBLICITARIA = ").append(Sql.getValue(pedido.flVinculaCampanhaPublicitaria));
			sql.append(",").append(" CDCAMPANHAPUBLICITARIA = ").append(Sql.getValue(pedido.cdCampanhaPublicitaria));
		}
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			sql.append(",").append(" VLFRETEADICIONAL = ").append(Sql.getValue(pedido.vlFreteAdicional));
		}
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			sql.append(",").append(" CDDIVISAOVENDA = ").append(Sql.getValue(pedido.cdDivisaoVenda));
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			sql.append(",").append(" FLUTILIZARENTABILIDADE = ").append(Sql.getValue(pedido.flUtilizaRentabilidade));	
		}
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		isDelete = true;
		super.addWhereDeleteByExample(domain, sql);
		isDelete = false;
	}

	private String getAliasIfNotDelete(String column) {
		return isDelete ? column : "tb." + column;
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		Pedido pedido = (Pedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("CDEMPRESA"), pedido.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("CDREPRESENTANTE"), pedido.cdRepresentante);
		if (pedido.flFiltraPedidosDif) {
			sqlWhereClause.addAndConditionNotEquals(getAliasIfNotDelete("NUPEDIDO"), pedido.nuPedido);
		} else {
			sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("NUPEDIDO"), pedido.nuPedido);
		}
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("FLORIGEMPEDIDO"), pedido.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("CDCLIENTE"), pedido.cdCliente);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("CDSTATUSPEDIDO"), pedido.cdStatusPedido);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete(" CDSTATUSPEDIDO != "), pedido.cdStatusPedidoDif);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("DTEMISSAO"), pedido.dtEmissao);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("FLPRECOLIBERADOSENHA"), pedido.flPrecoLiberadoSenha);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("FLCREDITOCLIENTELIBERADOSENHA"), pedido.flCreditoClienteLiberadoSenha);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTEMISSAO >= "), pedido.dtEmissaoInicialFilter);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTEMISSAO <= "), pedido.dtEmissaoFinalFilter);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTFECHAMENTO >= "), pedido.dtFechamentoInicialFilter);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete("DTFECHAMENTO <= "), pedido.dtFechamentoFinalFilter);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTENTREGA >= "), pedido.dtEntregaInicialFilter);
		sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTENTREGA <= "), pedido.dtEntregaFinalFilter);
		sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("FLPOSSUIDIFERENCA"), pedido.flPossuiDiferenca);
		sqlWhereClause.addAndConditionNotEquals(getAliasIfNotDelete("FLTIPOALTERACAO"), pedido.notFlTipoAlteracao);
		sqlWhereClause.addAndLikeCondition(getAliasIfNotDelete("NUORDEMCOMPRACLIENTE"), pedido.nuOrdemCompraClienteLikeFilter);
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("NUPEDIDORELBONIFICACAO = "), pedido.nuPedidoRelBonificacao);
		}
		if (pedido.flFiltraPedidosFechadosTransmitidos) {
			sqlWhereClause.addAndCondition(" (" + getAliasIfNotDelete("CDSTATUSPEDIDO = "), LavenderePdaConfig.cdStatusPedidoFechado);
			sqlWhereClause.addOrCondition(getAliasIfNotDelete(" CDSTATUSPEDIDO = "), LavenderePdaConfig.cdStatusPedidoTransmitido);
			sqlWhereClause.addEndMultipleCondition();
		}
		if (pedido.flFiltraPedidosDifAbertosCancelados) {
			sqlWhereClause.addAndCondition(" (" + getAliasIfNotDelete("CDSTATUSPEDIDO != "), LavenderePdaConfig.cdStatusPedidoAberto);
			sqlWhereClause.addAndCondition(getAliasIfNotDelete(" CDSTATUSPEDIDO != "), LavenderePdaConfig.cdStatusPedidoCancelado);
			sqlWhereClause.addEndMultipleCondition();
		}
		if (pedido.flFiltraPedidosDifCancelados) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete(" CDSTATUSPEDIDO != "), LavenderePdaConfig.cdStatusPedidoCancelado);
		}
		if (pedido.flFiltraPedidosDifFechamentoDiario) {
			if (pedido.dtFechamento != null) {
				sqlWhereClause.addAndCondition(" (" + getAliasIfNotDelete("DTFECHAMENTO > "), pedido.dtFechamento);
				sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTFECHAMENTO < "), DateUtil.getCurrentDate());
				sqlWhereClause.addEndMultipleCondition();
			} else {
				sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTFECHAMENTO > "), FechamentoDiarioService.getInstance().getDataConsideraValorPedidoAtualRetornadoPagamentos(DateUtil.getCurrentDate()));
				sqlWhereClause.addAndCondition(getAliasIfNotDelete(" DTFECHAMENTO < "), DateUtil.getCurrentDate());
			}
		}		
		if (pedido.filtraApenasPedidosComDescontoFinanceiro) {
			sqlWhereClause.addAndConditionForced(getAliasIfNotDelete(" VLDESCONTOINDICEFINANCLIENTE > "), 0);
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("CDCARGAPEDIDO"), pedido.cdCargaPedido);
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete(" NUPEDIDORELTROCA = "), pedido.nuPedidoRelTroca);
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("NUPEDIDOCOMPLEMENTADO"), pedido.nuPedidoComplementado);
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("CDCLIENTEENTREGA = "), pedido.cdClienteEntrega);
		}
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			if (ValueUtil.VALOR_NAO.equals(pedido.flKeyAccountFilter)) {
				sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("FLKEYACCOUNT = ")) + Sql.getValue(pedido.flKeyAccountFilter) + " OR " + (getAliasIfNotDelete("FLKEYACCOUNT ")) + "IS NULL OR " + (getAliasIfNotDelete("FLKEYACCOUNT = '')")));
			} else {
				sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("FLKEYACCOUNT"), pedido.flKeyAccountFilter);
			}
		}
		if (ValueUtil.isNotEmpty(pedido.cdStatusPedidoFilter)) {
			sqlWhereClause.addAndCondition(" (" + getAliasIfNotDelete("CDSTATUSPEDIDO = "), pedido.cdStatusPedidoFilter[0]);
			for (int i = 1; i < pedido.cdStatusPedidoFilter.length; i++) {
				sqlWhereClause.addOrCondition(getAliasIfNotDelete(" CDSTATUSPEDIDO = "), pedido.cdStatusPedidoFilter[i]);
			}
			sqlWhereClause.addEndMultipleCondition();
		}
		if (pedido.filtraDocNaoImpressos) {
			sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("FLNFEIMPRESSA ")) + "<> 'S' OR " + (getAliasIfNotDelete("FLBOLETOIMPRESSO ")) + "<> 'S')");
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("CDREGIAO = "), pedido.cdRegiao);
		}
		if (LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo() && LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			if (ValueUtil.isNotEmpty(pedido.dtTransmissaoPdaInicialFilter)) {
				sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("DTTRANSMISSAOPDA ")) + "is null or " + (getAliasIfNotDelete("DTTRANSMISSAOPDA >=")), pedido.dtTransmissaoPdaInicialFilter);
				sqlWhereClause.addEndMultipleCondition();
			}
			if (ValueUtil.isNotEmpty(pedido.dtTransmissaoPdaFinalFilter)) {
				sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("DTTRANSMISSAOPDA ")) + "is null or " + (getAliasIfNotDelete("DTTRANSMISSAOPDA <=")), pedido.dtTransmissaoPdaFinalFilter);
				sqlWhereClause.addEndMultipleCondition();
			}
		}
		if (LavenderePdaConfig.enviaProtocoloPedidosEmailHtml) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("NULOTEPROTOCOLO ="), pedido.nuLoteProtocolo);
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("FLENVIADOPROTOCOLO ="), pedido.flEnviadoProtocolo);
		}
		if (pedido.desconsideraPedidoPendente) {
			sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("FLPENDENTELIMCRED ")) + "= 'N' OR " + (getAliasIfNotDelete("FLPENDENTELIMCRED ")) + "IS NULL OR " + (getAliasIfNotDelete("FLPENDENTELIMCRED = '')")));
		}
		if (ValueUtil.getBooleanValue(pedido.flPendenteLimCred)) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("FLPENDENTELIMCRED ="), ValueUtil.VALOR_SIM);
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("CDSTATUSORCAMENTO"), pedido.cdStatusOrcamento);
			if (pedido.usaFiltroTipoAlteracao) {
				sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("FLTIPOALTERACAO")) + " <> '' OR " + (getAliasIfNotDelete("FLTIPOALTERACAO")) + " IS NULL)");
			}
		}
		if (LavenderePdaConfig.usaDataBaseCancelamentoAutoPedidoCliente() && pedido.usaFiltroTipoAlteracao) {
			sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("FLTIPOALTERACAO")) + " <> '' OR " + (getAliasIfNotDelete("FLTIPOALTERACAO")) + " IS NULL)");
		}
		sqlWhereClause.addAndConditionEquals(DaoUtil.ALIAS_STATUSPEDIDOPDA + ".FLCREDITOCLIENTEABERTO", pedido.flCreditoClienteAberto);

		if (pedido.isFiltraTipoPagamentoQueNaoIgnoraLimiteCredito()) {
			sqlWhereClause.addAndCondition("(" + DaoUtil.ALIAS_TIPOPAGAMETO + ".FLIGNORALIMITECREDITO is NULL OR " + DaoUtil.ALIAS_TIPOPAGAMETO +".FLIGNORALIMITECREDITO != 'S')");
		}
		if (ValueUtil.isNotEmpty(pedido.cdTipoPedido)) {
			sqlWhereClause.addAndConditionIn(getAliasIfNotDelete("CDTIPOPEDIDO"), pedido.cdTipoPedido.split(","));
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sqlWhereClause.addAndCondition(getAliasIfNotDelete("CDLOCAL"), pedido.cdLocal);
		}
		if (LavenderePdaConfig.usaPedidoPerdido && pedido.filtraPedidoPerdido) {
			sqlWhereClause.addAndCondition(("(" + getAliasIfNotDelete("CDMOTIVOPERDA")) + " IS NOT NULL AND " + (getAliasIfNotDelete("CDMOTIVOPERDA")) + " <> '')");
		}
		if (LavenderePdaConfig.usaMarcadorPedido && !ValueUtil.isEmpty(pedido.cdMarcadorFilter)) {
			sqlWhereClause.addAndCondition("EXISTS (SELECT CDMARCADOR FROM TBLVPMARCADORPEDIDO WHERE NUPEDIDO = tb.NUPEDIDO AND FLORIGEMPEDIDO = tb.FLORIGEMPEDIDO AND CDMARCADOR = '"+pedido.cdMarcadorFilter+"')");
		}
		if (pedido.ignoraTipoPedidoIgnoraEnvioErp) {
			sqlWhereClause.addAndCondition("NOT EXISTS(SELECT CDTIPOPEDIDO FROM TBLVPTIPOPEDIDO TP WHERE tb.CDTIPOPEDIDO = TP.CDTIPOPEDIDO AND TP.FLIGNORAENVIOERP = 'S')");
		}
		if (pedido.filtraApenasTipoPedidoIgnoraEnvioErp) {
			sqlWhereClause.addAndCondition("EXISTS(SELECT CDTIPOPEDIDO FROM TBLVPTIPOPEDIDO TP WHERE CDTIPOPEDIDO = TP.CDTIPOPEDIDO AND TP.FLIGNORAENVIOERP = 'S')");
		}
		if (LavenderePdaConfig.enviaPedidoEmOrcamentoParaSupervisor && pedido.flPedidoErp) {
			sqlWhereClause.addAndCondition(DaoUtil.getNotExistsPedidoPdaAbertoByCopiaPedidoErp());
		}
		if (LavenderePdaConfig.usaFiltroStatusExcecaoListaPedidos() || LavenderePdaConfig.usaFiltroStatusExcecaoResumoDia()) {
			sqlWhereClause.addAndConditionNotIn("tb.CDSTATUSPEDIDO ", pedido.cdStatusExcecaoList);
		}
		if (LavenderePdaConfig.isUsaPercQuantidadeDosItensPedidoOriginalBonificacaoTroca() && pedido.filterByMinSumQtItemFisico) {
			sqlWhereClause.addAndCondition(DaoUtil.getQtMinimaItemPedidoCondition(pedido.minSumQtItemFisico, ValueUtil.valueEquals(Pedido.TABLE_NAME_PEDIDOERP, tableName)));
		}
		if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && pedido.statusOrcamentoFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsStatusOrcamentoCondition(pedido.statusOrcamentoFilter));
		}
		if (LavenderePdaConfig.usaFiltroDeProdutoNaListaDePedido && pedido.itemPedidoFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsInItemPedido(pedido.itemPedidoFilter, ValueUtil.valueEquals(Pedido.TABLE_NAME_PEDIDOERP, tableName)));
		}
		if (pedido.onlyPedidoComNfe) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsNfeCondition());
		}
		if (pedido.supervisorRepFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsSupervisorRepCondition(pedido.supervisorRepFilter));
		}
		if (ValueUtil.isNotEmpty(pedido.nuPedidoLikeFilter)) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addOrLikeCondition("tb.NUPEDIDO", pedido.nuPedidoLikeFilter);
			sqlWhereClause.addOrLikeCondition("tb.NUPEDIDORELACIONADO", pedido.nuPedidoLikeFilter);
			sqlWhereClause.addEndMultipleCondition();
		}
		if (pedido.filterByMeusPedidos) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addOrCondition("tb.CDREPRESENTANTE = tb.CDREPRESENTANTEORG");
			sqlWhereClause.addOrCondition("COALESCE(tb.CDREPRESENTANTEORG, '') = ''");
			sqlWhereClause.addEndMultipleCondition();
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && pedido.nuOrdemLiberacaoFilter != 0) {
			sqlWhereClause.addAndCondition(getExistsPedidosByNivelLiberacaoPorRegraLiberacao(pedido.nuOrdemLiberacaoFilter));
		}
		if (ValueUtil.isNotEmpty(pedido.flPendenteCondPagto)) {
			sqlWhereClause.addAndConditionEquals(getAliasIfNotDelete("FLPENDENTECONDPAGTO"), pedido.flPendenteCondPagto);
		}
		//--
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			super.addOrderBy(sql, domain);
		} else {
			sql.append(" order by DTEMISSAO desc, HREMISSAO desc");
		}
	}

	public void updatePedidoTramsmitidoComSucesso(String rowKey, String dsUrlTransmissao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido));
		sql.append(" ,FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" ,DSURLENVIOSERVIDOR = ").append(Sql.getValue(dsUrlTransmissao));
		sql.append(" where ROWKEY = ").append(Sql.getValue(rowKey));
		//--
		executeUpdate(sql.toString());
	}
	
	public void updatePedidoOrcamentoTransmitido(String rowKey, String dsUrlTransmissao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" ,FLENVIAEMAIL = ").append(Sql.getValue(ValueUtil.VALOR_NAO));
		sql.append(" ,DSURLENVIOSERVIDOR = ").append(Sql.getValue(dsUrlTransmissao));
		sql.append(" where ROWKEY = ").append(Sql.getValue(rowKey));
		executeUpdate(sql.toString());
	}
	
	public void updatePedidoCanceladoTransmitido(String rowKey, String dsUrlTransmissao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" ,DSURLENVIOSERVIDOR = ").append(Sql.getValue(dsUrlTransmissao));
		sql.append(" where ROWKEY = ").append(Sql.getValue(rowKey));
		//--
		executeUpdate(sql.toString());
	}
	
	public void updatePedidoCancelado(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" CDSTATUSPEDIDO = ").append(Sql.getValue(pedido.cdStatusPedido));
		sql.append(" ,CDMOTIVOCANCELAMENTO = ").append(Sql.getValue(pedido.cdMotivoCancelamento));
		sql.append(" ,FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		 if (LavenderePdaConfig.isUsaEnvioRecadoNoCancelamentoPedido()) {
			 sql.append(" ,DSJUSTIFICATIVACANCELAMENTO = ").append(Sql.getValue(pedido.dsJustificativaCancelamento));
		 }
		
		sql.append(" where ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
		executeUpdate(sql.toString());
	}

	public int updateDtHrPedidosTramsmitidos(Pedido pedido) {
		try {
			StringBuffer sql = getSqlBuffer();
			sql.append(" update ").append(tableName).append(" set");
			sql.append(" DTTRANSMISSAOPDA = ").append(Sql.getValue(DateUtil.getCurrentDate())).append(",");
			sql.append(" HRTRANSMISSAOPDA = ").append(Sql.getValue(TimeUtil.getCurrentTimeHHMM()));
			
			sql.append(" where nupedido = ").append(Sql.getValue(pedido.getRowKey()));
			
			/*sql.append(" where CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado));
			if (LavenderePdaConfig.usaCancelamentoAutomaticoPedidoAbertoFechado() || LavenderePdaConfig.getDataBaseCancelamentoAutoPedidoCliente() != null || LavenderePdaConfig.getDataBaseCancelamentoAutoPedidoClienteKeyAccount() != null) {
				sql.append(" OR CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
			}
			if (LavenderePdaConfig.usaPedidoPerdido) {
				sql.append(" OR CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoPerdido));
			}
			//--*/
			return executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return 0;
		}
	}

	public void updatePedidosDiferencasLido() {
		//PEDIDOERP
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(Pedido.TABLE_NAME_PEDIDOERP).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		sql.append(" where (FLTIPOALTERACAO != ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		sql.append(" or FLTIPOALTERACAO is null)");
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			// Não lança excessão para executar o próximo update
			ExceptionUtil.handle(e);
		}
		//PEDIDO
		sql = getSqlBuffer();
		sql.append(" update ").append(Pedido.TABLE_NAME_PEDIDO).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		sql.append(" where (FLTIPOALTERACAO != ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		sql.append(" or FLTIPOALTERACAO is null)");
		sql.append(" and CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido));
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			// Não lança excessão para executar o próximo update
		}
	}
	
	public void updatePedidoOrcamento(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedido.flTipoAlteracao));
		sql.append(" ,FLENVIAEMAIL = ").append(Sql.getValue(pedido.getHashValuesDinamicos().get(Pedido.NMCOLUNA_FLENVIAEMAIL)));
		sql.append(" where ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
		executeUpdate(sql.toString());
	}
	
	public void updatePedidoDiferenca(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(Pedido.TABLE_NAME_PEDIDOERP).append(" SET");
		sql.append(" FLPEDIDODIFERENCA = ").append(Sql.getValue(pedido.flPedidoDiferenca));
		sql.append(" where ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
		executeUpdate(sql.toString());
	}
	
	public void updateIndicesFinanceirosPedido(final double vlTotalVerbaPedido, final Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(tableName).append(" SET");
		sql.append(" VLVERBAPEDIDO = ").append(Sql.getValue(vlTotalVerbaPedido));
		sql.append(" ,VLPCTINDICEFINCONDPAGTO = ").append(Sql.getValue(pedido.vlPctIndiceFinCondPagto));
		sql.append(" ,VLPCTDESCQUANTIDADEPESO  = ").append(Sql.getValue(pedido.vlPctDescQuantidadePeso));
		sql.append(" where ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
		executeUpdate(sql.toString());
	}

	public int updatePedidoProcessandoEnvio(Pedido pedido) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO)).append(",");
		sql.append(" DTTRANSMISSAOPDA = ").append(Sql.getValue(DateUtil.getCurrentDate())).append(",");
		sql.append(" HRTRANSMISSAOPDA = ").append(Sql.getValue(TimeUtil.getCurrentTimeHHMM()));
		sql.append(" where ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
		sql.append(" and CDSTATUSPEDIDO = ").append(Sql.getValue(pedido.cdStatusPedido));
		if (! pedido.isPedidoAlteradoOrcamento()) {
			sql.append(" and CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		}
		try {
			return executeUpdate(sql.toString());
		} catch (Throwable e) {
			return 0;
		}
	}
	
	public int updateStatusReabrirPedido(String rowkey, String cdStatusAtualizacao, boolean addFlagEnvioFilter) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" CDSTATUSPEDIDO = ").append(Sql.getValue(cdStatusAtualizacao));
		sql.append(" where ROWKEY = ").append(Sql.getValue(rowkey));
		if (addFlagEnvioFilter) {
			sql.append(" and FLTIPOALTERACAO <> ").append(Sql.getValue(Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO));
			sql.append(" and FLTIPOALTERACAO <> ").append(Sql.getValue(Pedido.FLTIPOALTERACAO_ORIGINAL));
		}
		try {
			return executeUpdate(sql.toString());
		} catch (Throwable e) {
			return 0;
		}
	}

	public Pedido findUltimoPedidoNaoAbertoByExample(BaseDomain domain) throws SQLException {
		Pedido pedido = findUltimoPedidoNaoAbertoByExampleSep(domain);
		if (pedido == null) {
			pedido = getInstanceErp().findUltimoPedidoNaoAbertoByExampleSep(domain);
		}
		return pedido;
	}

	public Pedido findUltimoPedidoByExample(BaseDomain domain) throws SQLException {
		Pedido pedido = findUltimoPedidoByExampleSep(domain);
		if (pedido == null) {
			pedido = getInstanceErp().findUltimoPedidoByExampleSep(domain);
		}
		return pedido;
	}

	private Pedido findUltimoPedidoNaoAbertoByExampleSep(BaseDomain domain) throws SQLException {
		Pedido pedidofilter = (Pedido) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT * ");
		sql.append(" FROM ").append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedidofilter.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedidofilter.cdRepresentante));
		sql.append(" and CDCLIENTE = ").append(Sql.getValue(pedidofilter.cdCliente));
		sql.append(" and CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		sql.append(" order by DTEMISSAO desc, HREMISSAO desc limit 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (Pedido) populateDyn(rs);
			}
		}
		return null;
	}

	private Pedido findUltimoPedidoByExampleSep(BaseDomain domain) throws SQLException {
		Pedido pedidofilter = (Pedido) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT");
		addSelectColumns(domain, sql);
		sql.append(" FROM ").append(tableName).append(" tb ");
		DaoUtil.addJoinStatusPedidoPda(sql);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedidofilter.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedidofilter.cdRepresentante));
		sql.append(" and CDCLIENTE = ").append(Sql.getValue(pedidofilter.cdCliente));
		sql.append(" order by DTEMISSAO desc, HREMISSAO desc limit 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (Pedido)populate(domain, rs);
			}
		}
		return null;
	}

	public Vector findDatasUltimosPedidosNaoTransmitidosByExample(BaseDomain domain) throws SQLException {
		Pedido pedidofilter = (Pedido) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DTEMISSAO");
		sql.append(" FROM ").append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedidofilter.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedidofilter.cdRepresentante));
		sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedidofilter.flOrigemPedido));
		sql.append(" and DTEMISSAO < ").append(Sql.getValue(DateUtil.getCurrentDate()));
		sql.append(" and CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido));
		sql.append(" order by DTEMISSAO desc, HREMISSAO desc");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector dataList = new Vector();
			Date data;
			while (rs.next()) {
				data = rs.getDate("dtEmissao");
				if (dataList.indexOf(data) == -1) {
					dataList.addElement(data);
				}
			}
			return dataList;
		}
	}

	public int findCountPedidosAbertos() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT count(*) as qtdePed");
		sql.append(" FROM ").append(tableName);
		sql.append(" where CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido));
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			sql.append(" AND CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		}
		if ((LavenderePdaConfig.usaCancelamentoDePedido || LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) && ValueUtil.isNotEmpty(LavenderePdaConfig.cdStatusPedidoCancelado)) {
			sql.append(" AND CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
		}
		return getInt(sql.toString());
	}

	public Vector findAllPedidosPdaResumido(Pedido pedidoFilter) throws SQLException {
		return findAllPedidosPdaResumido(pedidoFilter, false);
	}
	
	public Vector findAllPedidosPdaResumido(Pedido pedidoFilter, boolean addJoin) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDSTATUSPEDIDO, tb.NUPEDIDO, tb.FLORIGEMPEDIDO, tb.CDCLIENTE, ")
				.append("tb.DTEMISSAO, tb.HREMISSAO, tb.CDTIPOPEDIDO, tb.CDTIPOPAGAMENTO, tb.VLVERBAPEDIDO, tb.VLVERBAPEDIDOPOSITIVO, ")
				.append("tb.VLTOTALITENS, tb.VLDESCONTO, tb.VLTOTALPEDIDO, tb.VLTROCAENTREGAR, tb.VLTROCARECOLHER, tb.DTTRANSMISSAOPDA, ")
				.append("tb.FLPAGAMENTOAVISTA, tb.FLCRITICO, tb.ROWKEY");
		sql.append(" FROM ").append(tableName).append(" tb ");
		if (addJoin) {
			DaoUtil.addJoinStatusPedidoPda(sql);
			DaoUtil.addJoinTipoPagamento(sql);
		}
		addWhereByExample(pedidoFilter, sql);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listPedido = new Vector();
			Pedido pedido;
			while (rs.next()) {
				pedido = new Pedido();
				pedido.cdEmpresa = rs.getString("cdEmpresa");
				pedido.cdRepresentante = rs.getString("cdRepresentante");
				pedido.nuPedido = rs.getString("nuPedido");
				pedido.cdStatusPedido = rs.getString("cdStatusPedido");
				pedido.flOrigemPedido = rs.getString("flOrigemPedido");
				pedido.cdCliente = rs.getString("cdCliente");
				pedido.dtEmissao = rs.getDate("dtEmissao");
				pedido.hrEmissao = rs.getString("hrEmissao");
				pedido.cdTipoPedido = rs.getString("cdTipoPedido");
				pedido.cdTipoPagamento = rs.getString("cdTipoPagamento");
				pedido.vlTotalItens = rs.getDouble("vlTotalItens");
				pedido.vlVerbaPedido = rs.getDouble("vlVerbaPedido");
				pedido.vlVerbaPedidoPositivo = rs.getDouble("vlVerbaPedidoPositivo");
				pedido.vlDesconto = rs.getDouble("vlDesconto");
				pedido.vlTotalPedido = rs.getDouble("vlTotalPedido");
				pedido.vlTrocaEntregar = rs.getDouble("vlTrocaEntregar");
				pedido.vlTrocaRecolher = rs.getDouble("vlTrocaRecolher");
				pedido.dtTransmissaoPda = rs.getDate("DTTRANSMISSAOPDA");
				pedido.flPagamentoAVista = rs.getString("FLPAGAMENTOAVISTA");
				pedido.flCritico = rs.getString("FLCRITICO");
				pedido.rowKey = rs.getString("rowKey");
				listPedido.addElement(pedido);
			}
			return listPedido;
		}
	}
	
	public Date findDtOldestPedidoCargaPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select min(dtEmissao) as data");
		sql.append(" from ").append(tableName);
		sql.append(" where cdCargaPedido = ").append(Sql.getValue(pedido.cdCargaPedido));
		sql.append(" and cdEmpresa = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		sql.append(" and cdRepresentante = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
		sql.append(" and flOrigemPedido = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_PDA));
		return getDate(sql.toString());
	}
	
	public Vector findPedidosConsignadosOrdenadosPorVencimento(Pedido pedidoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT * ");
		sql.append(" FROM ").append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante));
		sql.append(" and CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoConsignado));
		if (pedidoFilter.pedidoConsignadoVencido) {
			sql.append(" AND DTCONSIGNACAO < ").append(Sql.getValue(DateUtil.getCurrentDate()));
		} else if (pedidoFilter.pedidoConsignadoAVencer) {
			sql.append(" and DTCONSIGNACAO >= ").append(Sql.getValue(DateUtil.getCurrentDate()));
		}
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector pedidoList = new Vector();
			//--
			Pedido pedidoResult;
			while (rs.next()) {
				pedidoResult = new Pedido();
				pedidoResult.cdEmpresa = SessionLavenderePda.cdEmpresa;
				pedidoResult.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
				pedidoResult.flOrigemPedido = rs.getString("flOrigemPedido");
				pedidoResult.nuPedido = rs.getString("nuPedido");
				pedidoResult.cdCliente = rs.getString("cdCliente");
				pedidoResult.vlTotalPedido = rs.getDouble("vlTotalPedido");
				pedidoResult.dtConsignacao = rs.getDate("dtConsignacao");
				pedidoList.addElement(pedidoResult);
			}
			return pedidoList;
		}
	}

	public Vector findAllPedidosSugestao(Pedido pedido) throws SQLException {
		return VectorUtil.concatVectors(findAllPedidosPdaResumido(pedido),
				PedidoPdbxDao.getInstanceErp().findAllPedidosPdaResumido(pedido));
	}

	public Vector findClientePedidoList(Pedido domain) throws SQLException {
		domain.setFiltraTipoPagamentoQueNaoIgnoraLimiteCredito(true);
		return VectorUtil.concatVectors(findAllPedidosPdaResumido(domain, true),
				PedidoPdbxDao.getInstanceErp().findAllPedidosPdaResumido(domain, true));
	}

	//@Override
	public BaseDomain findByRowKey(String rowKey) throws java.sql.SQLException {
		StringBuffer strBuffer = getSqlBuffer();
		strBuffer.append(";").append(OrigemPedido.FLORIGEMPEDIDO_PDA).append(";");
		if (rowKey.indexOf(strBuffer.toString()) == -1) {
			return PedidoPdbxDao.getInstanceErp().findByRowKeyErp(rowKey);
		}
		return super.findByRowKey(rowKey);
	}

	private BaseDomain findByRowKeyErp(String rowKey) throws java.sql.SQLException {
		return super.findByRowKey(rowKey);
	}

	public BasePersonDomain findByRowKeyDyn(String rowKey) throws SQLException {
		if (rowKey.indexOf(";" + OrigemPedido.FLORIGEMPEDIDO_PDA + ";") == -1) {
			return PedidoPdbxDao.getInstanceErp().findByRowKeyDynErp(rowKey);
		}
		return super.findByRowKeyDyn(rowKey);
	}

	private BasePersonDomain findByRowKeyDynErp(String rowKey) throws SQLException {
		return super.findByRowKeyDyn(rowKey);
	}

	//@Override
	public Vector findAll() throws java.sql.SQLException {
		return VectorUtil.concatVectors(findAllSimple(),
				PedidoPdbxDao.getInstanceErp().findAllSimple());
	}

	public Vector findAllSimple() throws java.sql.SQLException {
		return super.findAll();
	}

	//@Override
	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExample(domain),
				PedidoPdbxDao.getInstanceErp().findAllByExampleErp(domain));
	}

	//@Override
	public Vector findAllByExampleDyn(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleDyn(domain),
				PedidoPdbxDao.getInstanceErp().findAllByExampleDynErp(domain));
	}

	private Vector findAllByExampleErp(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExample(domain);
	}

	private Vector findAllByExampleDynErp(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleDyn(domain);
	}

	public Vector findAllByExampleOnlyPda(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExample(domain);
	}

	//@Override
	public Vector findAllByExampleSummary(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleSummary(domain),
				PedidoPdbxDao.getInstanceErp().findAllByExampleSummaryErp(domain));
	}

	private Vector findAllByExampleSummaryErp(BaseDomain domain) throws java.sql.SQLException {
		if (LavenderePdaConfig.enviaPedidoEmOrcamentoParaSupervisor) ((Pedido) domain).flPedidoErp = true;
		return super.findAllByExampleSummary(domain);
	}

	//@Override
	public Vector findAllInCache() throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllInCache(),
				PedidoPdbxDao.getInstanceErp().findAllInCacheErp());
	}

	private Vector findAllInCacheErp() throws java.sql.SQLException {
		return super.findAllInCache();
	}

	//@Override
	public Vector findAllByExampleInCache(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleInCache(domain),
				PedidoPdbxDao.getInstanceErp().findAllByExampleInCacheErp(domain));
	}

	private Vector findAllByExampleInCacheErp(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleInCache(domain);
	}

	//@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		Pedido pedido = (Pedido) domain;
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedido.flOrigemPedido)) {
			super.delete(domain);
		} else {
			PedidoPdbxDao.getInstanceErp().deleteErp(domain);
		}
	}
	
	@Override
	public void update(BaseDomain domain) throws java.sql.SQLException {
		Pedido pedido = (Pedido) domain;
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedido.flOrigemPedido)) {
			super.update(domain);
		} else {
			PedidoPdbxDao.getInstanceErp().updateErp(domain);
		}
	}

	private void deleteErp(BaseDomain domain) throws java.sql.SQLException {
		super.delete(domain);
	}
	
	private void updateErp(BaseDomain domain) throws java.sql.SQLException {
		super.update(domain);
	}

	@Override
	protected BasePersonDomain populateColumnsDynFixas(ResultSet resultSet) throws SQLException {
		Pedido pedido = new Pedido();
		pedido.rowKey = resultSet.getString("rowkey");
		pedido.cdEmpresa = resultSet.getString("cdEmpresa");
		pedido.cdRepresentante = resultSet.getString("cdRepresentante");
		pedido.nuPedido = resultSet.getString("nuPedido");
		pedido.flOrigemPedido = resultSet.getString("flOrigemPedido");
		pedido.cdCliente = resultSet.getString("cdCliente");
		pedido.nuPedidoRelacionado = resultSet.getString("nuPedidoRelacionado");
		pedido.flOrigemPedidoRelacionado = resultSet.getString("flOrigemPedidoRelacionado");
		pedido.cdStatusPedido = resultSet.getString("cdStatusPedido");
		pedido.dtEmissao = resultSet.getDate("dtEmissao");
		pedido.hrEmissao = resultSet.getString("hrEmissao");
		pedido.hrFimEmissao =  resultSet.getString("hrFimEmissao");
		pedido.dtFechamento = resultSet.getDate("dtFechamento");
		pedido.hrFechamento =  resultSet.getString("hrFechamento");
		pedido.vlTotalItens = ValueUtil.round(resultSet.getDouble("vlTotalItens"));
		pedido.vlTotalPedido = ValueUtil.round(resultSet.getDouble("vlTotalPedido"));
		pedido.cdTabelaPreco = resultSet.getString("cdTabelaPreco");
		pedido.nuVersaoSistemaOrigem = resultSet.getString("nuVersaoSistemaOrigem");
		pedido.dsUrlEnvioServidor = resultSet.getString("dsUrlEnvioServidor");
		pedido.vlTotalBrutoItens = resultSet.getDouble("vlTotalBrutoItens");
		pedido.vlTotalBaseItens = resultSet.getDouble("vlTotalBaseItens");
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 || LavenderePdaConfig.usaQtdMinimaProdutosRestritosPromocionais()) {
			pedido.flMaxVendaLiberadoSenha = resultSet.getString("flMaxVendaLiberadoSenha");
		}
		if (!LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaPedidoComplementar() || LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega || LavenderePdaConfig.isConfigReservaEstoqueCorrente) {
			pedido.dtEntrega = resultSet.getDate("dtEntrega");
		}
		if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			pedido.cdCondicaoPagamento = resultSet.getString("cdCondicaoPagamento");
			pedido.oldCdCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			pedido.cdTipoPagamento = resultSet.getString("cdTipoPagamento");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			pedido.cdTipoPedido = resultSet.getString("cdTipoPedido");
			pedido.oldCdTipoPedido = pedido.cdTipoPedido;
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido()  || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.cdTipoFrete = resultSet.getString("cdTipoFrete");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.usaFretePedidoPorToneladaCliente || LavenderePdaConfig.isPermiteInserirFreteManual() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.vlFrete =  resultSet.getDouble("vlFrete");
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			pedido.cdSupervisor = resultSet.getString("cdSupervisor");
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			pedido.vlVerbaPedido = ValueUtil.round(resultSet.getDouble("vlVerbaPedido"));
			pedido.vlVerbaPedidoOld = pedido.vlVerbaPedido;
		}
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 || LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			pedido.vlDesconto = ValueUtil.round(resultSet.getDouble("vlDesconto"));
			pedido.vlDescontoOld = pedido.vlDesconto;
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			pedido.vlPctIndiceFinCondPagto = ValueUtil.round(resultSet.getDouble("vlPctIndiceFinCondPagto"));
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			pedido.vlPctDescQuantidadePeso = ValueUtil.round(resultSet.getDouble("vlPctDescQuantidadePeso"));
		}
		if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
			pedido.vlVerbaPedidoPositivo = ValueUtil.round(resultSet.getDouble("vlVerbaPedidoPositivo"));
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda) {
			pedido.flPrecoLiberadoSenha = resultSet.getString("flPrecoLiberadoSenha");
		}
		if (LavenderePdaConfig.isUsaNuOrdemCompraClienteNoPedido()) {
			pedido.nuOrdemCompraCliente = resultSet.getString("nuOrdemCompraCliente");
		}
		if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() || LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
			pedido.flCreditoClienteLiberadoSenha = resultSet.getString("flCreditoClienteLiberadoSenha");
		}
		if (LavenderePdaConfig.isUsaRotaDeEntregaNoPedido()) {
			pedido.cdRotaEntrega = resultSet.getString("cdRotaEntrega");
		}
		if (LavenderePdaConfig.relDiferencasPedido) {
			pedido.flPossuiDiferenca = resultSet.getString("flPossuiDiferenca");
			pedido.flPedidoDiferenca = resultSet.getString("flPedidoDiferenca");
		}
		if (LavenderePdaConfig.geraNovoPedidoDiferencas) {
			pedido.nuPedidoDiferenca = resultSet.getString("nuPedidoDiferenca");
		}
		if (LavenderePdaConfig.clienteComContratoExigeSetorPedido) {
			pedido.cdSetor = resultSet.getString("cdSetor");
			pedido.cdOrigemSetor = resultSet.getString("cdOrigemSetor");
		}
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			pedido.cdTipoEntrega = resultSet.getString("cdTipoEntrega");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || (LavenderePdaConfig.permiteDescontoPercentualPorPedido > 0) || (LavenderePdaConfig.permiteDescontoEmValorPorPedido > 0) || (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) || LavenderePdaConfig.usaDescontoPonderadoPedido) {
			pedido.vlDesconto = resultSet.getDouble("vlDesconto");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			pedido.qtPeso = resultSet.getDouble("qtPeso");
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			pedido.cdAreaVenda = resultSet.getString("cdAreaVenda");
		}
		if (!ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.usaModuloTrocaNoPedido) {
			pedido.vlTrocaRecolher =  resultSet.getDouble("vlTrocaRecolher");
			pedido.vlTrocaEntregar =  resultSet.getDouble("vlTrocaEntregar");
		}
		if (LavenderePdaConfig.isAplicaDescEmValorOuPctCapaPedido() || LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos() || LavenderePdaConfig.aplicaDescontoPedidoRepEspecial || LavenderePdaConfig.usaDescontoPonderadoPedido) {
			pedido.vlPctDesconto = resultSet.getDouble("vlPctDesconto");
			if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorPedido()) {
				pedido.vlDescontoIndiceFinanCliente = resultSet.getDouble("vlDescontoIndiceFinanCliente");
			}
		}
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido() || LavenderePdaConfig.permiteEditarDescontoProgressivo) {
			pedido.vlPctDescProgressivo = resultSet.getDouble("vlPctDescProgressivo");
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			pedido.vlPctDescItem = resultSet.getDouble("vlPctDescItem");
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem()) {
			pedido.vlPctAcrescimoItem = resultSet.getDouble("vlPctAcrescimoItem");
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.isUsaPedidoBonificacao()  || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			pedido.vlBonificacaoPedido = resultSet.getDouble("vlbonificacaopedido");
		}
		if ((LavenderePdaConfig.isUsaRentabilidadeNoPedido()) || (LavenderePdaConfig.indiceRentabilidadePedido > 0)) {
			pedido.vlRentabilidade = resultSet.getDouble("vlRentabilidade");
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			pedido.flPedidoNovoCliente = resultSet.getString("flPedidoNovoCliente");
		}
		if (LavenderePdaConfig.condicaoPagamentoSemCadastro) {
			pedido.dsCondicaoPagamentoSemCadastro = resultSet.getString("dsCondicaoPagamentoSemCadastro");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			pedido.qtPontosPedido = resultSet.getInt("qtPontosPedido");
		}
		if (LavenderePdaConfig.usaContratoForecast || LavenderePdaConfig.usaControleEstoqueGondola) {
			pedido.flBloqueadoEdicao = resultSet.getString("flBloqueadoEdicao");
		}
		if (LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
			pedido.flClienteAtrasadoLiberadoSenha = resultSet.getString("flClienteAtrasadoLiberadoSenha");
		}
		if (LavenderePdaConfig.usaSegmentoNoPedido) {
			pedido.cdSegmento = resultSet.getString("cdSegmento");
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			pedido.cdCondicaoComercial = resultSet.getString("cdCondicaoComercial");
		}
		if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido()) {
			pedido.dsMotivoDesconto = resultSet.getString("dsMotivoDesconto");
		}
		if (LavenderePdaConfig.usaTransportadoraPedido() || LavenderePdaConfig.utilizaEscolhaTransportadora() && LavenderePdaConfig.usaEscolhaTransportadoraPedido() || LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.cdTransportadora = resultSet.getString("cdTransportadora");
		}
		if (LavenderePdaConfig.usaRateioFreteRepresentanteCliente) {
			pedido.vlPctFreteRepresentante =  resultSet.getDouble("vlPctFreteRepresentante");
			pedido.vlFreteRepresentante =  resultSet.getDouble("vlFreteRepresentante");
			pedido.vlFreteCliente =  resultSet.getDouble("vlFreteCliente");
		}
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0 || LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			pedido.flImpresso = resultSet.getString("flImpresso");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) {
			pedido.nuPedidoRelBonificacao = resultSet.getString("nuPedidoRelBonificacao");
		}
		if (LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			pedido.cdCentroCusto = resultSet.getString("cdCentroCusto");
			pedido.cdPlataformaVenda = resultSet.getString("cdPlataformaVenda");
			pedido.cdItemConta = resultSet.getString("cdItemConta");
			pedido.cdClasseValor = resultSet.getString("cdClasseValor");
		}
		if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido()) {
			pedido.nuPedidoSugestao = resultSet.getString("nuPedidoSugestao");
			pedido.flOrigemPedidoSugestao = resultSet.getString("flOrigemPedidoSugestao");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			pedido.vlTotalPedidoEstoquePositivo = resultSet.getDouble("vlTotalPedidoEstoquePositivo");
		}
		if (LavenderePdaConfig.isUsaReplicacaoPedido()) {
			pedido.flPedidoReplicado = resultSet.getString("flPedidoReplicado");
			pedido.nuPedidoOriginal = resultSet.getString("nuPedidoOriginal");
		}
		if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
			pedido.flSugestaoVendaLiberadoSenha = resultSet.getString("flSugestaoVendaLiberadoSenha");
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			pedido.cdCargaPedido = resultSet.getString("cdCargaPedido");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
			pedido.flAbaixoRentabilidadeMinima = ValueUtil.getBooleanValue(resultSet.getString("flAbaixoRentabilidadeMinima"));
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			pedido.vlPctMargemMin = resultSet.getDouble("vlPctMargemMin");
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			pedido.flEtapaVerba = resultSet.getString("flEtapaVerba");
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.mostraFaixaComissao()) {
			pedido.vlPctComissao = resultSet.getDouble("vlPctComissao");
		}
		if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() || !LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente() || !LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			pedido.flPagamentoAVista = resultSet.getString("flPagamentoAVista");
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() || LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
				pedido.flGeraNfe = resultSet.getString("flGeraNfe");
			}
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosBoleto || LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
				pedido.flGeraBoleto = resultSet.getString("flGeraBoleto");
			}
		}
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			pedido.nuKmInicial = resultSet.getInt("nuKmInicial");
			pedido.nuKmFinal = resultSet.getInt("nuKmFinal");
			pedido.hrInicialIndicado = resultSet.getString("hrInicialIndicado");
			pedido.hrFinalIndicado = resultSet.getString("hrFinalIndicado");
		}
		if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
			pedido.flNfeImpressa = resultSet.getString("flNfeImpressa");
		}
		if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil) {
			pedido.flLiberadoEntrega = resultSet.getString("flLiberadoEntrega");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
			pedido.cdEnderecoCliente = resultSet.getString("cdEnderecoCliente");
		}
		if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0) {
			pedido.flBoletoImpresso = resultSet.getString("flBoletoImpresso");
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCentralizado()  || LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			pedido.flSituacaoReservaEst = resultSet.getString("flSituacaoReservaEst");
		}
		if (LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) {
			pedido.nuPedidoRelTroca = resultSet.getString("nuPedidoRelTroca");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			pedido.nuPedidoComplementado = resultSet.getString("nuPedidoComplementado");
		}
		if (LavenderePdaConfig.usaClienteKeyAccount) {
			pedido.flKeyAccount = resultSet.getString("flKeyAccount");
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativo() || LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() || LavenderePdaConfig.usaBotaoIgnorarValidacoesPedido || LavenderePdaConfig.usaMarcaPedidoPendenteComItemBonificado() || LavenderePdaConfig.isUsaMotivoPendencia()) {
			pedido.flPendente = resultSet.getString("flPendente");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito() || LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
			pedido.flPendenteLimCred = resultSet.getString("flPendenteLimCred");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			pedido.flGeraCreditoCondicao = resultSet.getString("FLGERACREDITOCONDICAO");
			pedido.vlTotalCreditoCondicao = resultSet.getDouble("VLTOTALCREDITOCONDICAO");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			pedido.flGeraCreditoFrete = resultSet.getString("FLGERACREDITOFRETE");
			pedido.vlTotalCreditoFrete = resultSet.getDouble("VLTOTALCREDITOFRETE");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			pedido.flItemPendente = resultSet.getString("FLITEMPENDENTE");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			pedido.vlPctDescProgressivoMix = resultSet.getDouble("VLPCTDESCPROGRESSIVOMIX");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			pedido.vlVolumePedido = resultSet.getDouble("VLVOLUMEPEDIDO");
		}
		if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			pedido.vlPctDescFrete = resultSet.getDouble("VLPCTDESCFRETE");
		}
		if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			pedido.oldVlPctCat1 = pedido.vlPctDescCliente = resultSet.getDouble("VLPCTDESCCLIENTE");
		}
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()
				|| LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()
				|| LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()
				|| LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()
				|| (LavenderePdaConfig.isAplicaDescontoCategoria() && LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido())) {
			pedido.vlPctDescontoCondicao = resultSet.getDouble("VLPCTDESCONTOCONDICAO");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
			pedido.dtEntregaLiberada = resultSet.getDate("DTENTREGALIBERADA");
		}
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedidoPorUsuario()) {
			pedido.cdUsuarioLibEntrega = resultSet.getString("CDUSUARIOLIBENTREGA");
		}
		if (LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada()) {
			pedido.cdUsuarioLiberacaoLimCred = resultSet.getString("CDUSUARIOLIBERACAOLIMCRED");
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
			pedido.vlTotalTrocaPedido = resultSet.getDouble("vlTotalTrocaPedido");
		}
		if (LavenderePdaConfig.usaDataCarregamentoPedido) {
			pedido.dtCarregamento = resultSet.getDate("DTCARREGAMENTO");
		}
		if (LavenderePdaConfig.isUsaImpressaoContingenciaNfeViaBluetooth()) {
			pedido.flNfeContImpressa = resultSet.getString("FLNFECONTIMPRESSA");
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			pedido.dtConsignacao = resultSet.getDate("DTCONSIGNACAO");
			if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
				pedido.vlPedidoOriginal = resultSet.getDouble("VLPEDIDOORIGINAL");
				pedido.vlTotalDevolucoes = resultSet.getDouble("VLTOTALDEVOLUCOES");
			}
		}
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
			pedido.flConsignacaoImpressa = resultSet.getString("FLCONSIGNACAOIMPRESSA");
		}
		if (LavenderePdaConfig.isUsaCotaVlQtdRetirarAcresCondPgto()) {
			pedido.flCotaCondPagto = resultSet.getString("flCotaCondPagto");
		}
		if (LavenderePdaConfig.isUsaContatoERPClienteNoPedido()) {
			pedido.cdContato = resultSet.getString("CDCONTATO");
		}
		if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido) {
			pedido.cdClienteEntrega = resultSet.getString("cdClienteEntrega");
		}
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			pedido.nuPedCompRelacionado = resultSet.getString("NUPEDCOMPRELACIONADO");
		}
		if (LavenderePdaConfig.usaAgrupamentoPedidoEmConsignacao) {
			pedido.cdPedidosAgrupados = resultSet.getString("CDPEDIDOSAGRUPADOS");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			pedido.flRestrito = resultSet.getString("flRestrito");
		}
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			String cdCondNegociacao = resultSet.getString("cdCondNegociacao");
			setCondicaoNegociacaoNoPedido(pedido, cdCondNegociacao);
			pedido.oldCdCondicaoNegociacao = cdCondNegociacao;
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			pedido.cdUnidade = resultSet.getString("cdUnidade");
		}
		if (LavenderePdaConfig.isUsaEntregaPedidoBaseadaEmCadastro()) {
			pedido.cdEntrega = resultSet.getString("cdEntrega");
		}
		pedido.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
			pedido.flMinVerbaLiberado = resultSet.getString("FLMINVERBALIBERADO");
		}
		if (LavenderePdaConfig.usaTransportadoraAuxiliar && LavenderePdaConfig.usaTransportadoraPedido()) {
			pedido.cdTransportadoraAux = resultSet.getString("CDTRANSPORTADORAAUX");
		}
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
			pedido.cdRegiao = resultSet.getString("cdRegiao");
		}
		if (LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0) {
			pedido.flPromissoriaImpressa = resultSet.getString("flPromissoriaImpressa");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			pedido.vlTotalMargem = resultSet.getDouble("vlTotalMargem");
		}
		if (LavenderePdaConfig.validaSaldoPedidoBonificacao) {
			pedido.flSaldoBoniLiberadoSenha = resultSet.getString("flSaldoBoniLiberadoSenha");
		}
		if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			pedido.oldVlPctCat2 = pedido.vlPctDesc2 = resultSet.getDouble("vlPctDesc2");
			pedido.oldVlPctCat3 = pedido.vlPctDesc3 = resultSet.getDouble("vlPctDesc3");
		}
		if (LavenderePdaConfig.isUsaTipoFretePedido() && LavenderePdaConfig.usaIndicacaoCNPJTransportadoraFreteFOB) {
			pedido.nuCnpjTransportadora = resultSet.getString("nuCnpjTransportadora");
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			pedido.cdStatusOrcamento = resultSet.getString("cdStatusOrcamento");
			pedido.dsObsOrcamento = resultSet.getString("dsObsOrcamento");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			pedido.dtPagamento = resultSet.getDate("dtPagamento");
		}
		if (LavenderePdaConfig.isPermitePedidoTributacaoNovoCliente()) {
			pedido.cdTributacaoCliente = resultSet.getString("cdTributacaoCliente");
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			pedido.flDescontoLiberadoSenha = resultSet.getString("flDescontoLiberadoSenha");
		}
		if (LavenderePdaConfig.isConfigLiberacaoComSenhaVlMinParcela()) {
			pedido. flValorMinParcelaLiberadoSenha = resultSet.getString("flValorMinParcelaLiberadoSenha");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			pedido.dtUltimoRecalculoValores = resultSet.getDate("DTULTIMORECALCULOVALORES");
		}
		if (LavenderePdaConfig.utilizaNotasCredito()) {
			pedido.vlTotalNotaCredito = resultSet.getDouble("VLTOTALNOTACREDITO");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			pedido.vlPctVpc = resultSet.getDouble("vlPctVpc");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			pedido.vlPctDescontoAutoEfetivo = resultSet.getDouble("vlPctDescontoAutoEfetivo");
			pedido.vlTotalDescontoAuto = resultSet.getDouble("vlTotalDescontoAuto");
			pedido.vlDescontoTotalAutoDesc = resultSet.getDouble("vlDescontoTotalAutoDesc");
		}
		if (LavenderePdaConfig.enviaProtocoloPedidosEmailHtml) {
			pedido.nuLoteProtocolo = resultSet.getString("nuLoteProtocolo");
			pedido.flEnviadoProtocolo = resultSet.getString("flEnviadoProtocolo");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			pedido.cdComissaoPedidoRep = resultSet.getInt("cdComissaoPedidoRep");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			pedido.vlSeguroPedido = resultSet.getDouble("vlSeguroPedido");
		}
		if (LavenderePdaConfig.mostraObservacaoCliente()) {
			pedido.dsObservacaoCliente = resultSet.getString("dsObservacaoCliente");
		}
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			pedido.vlVerbaGrupo = resultSet.getDouble("vlVerbaGrupo");
		}
		if (LavenderePdaConfig.obrigaMotivoBonificacao()) {
			pedido.dsMotivoBonificacao = resultSet.getString("dsMotivoBonificacao");
		}
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			pedido.cdLocal = resultSet.getString("cdLocal");
		}
		pedido.vlPctTotalMargem = resultSet.getDouble("vlPctTotalMargem");
		if (LavenderePdaConfig.isLiberaComSenhaCondPagamento()) {
			pedido.qtDiasCPgtoLibSenha = resultSet.getInt("qtDiasCPgtoLibSenha");
			pedido.vlTotalPedidoLiberado = resultSet.getInt("vlTotalPedidoLiberado");
		}
		if (LavenderePdaConfig.usaCalculoFretePersonalizado()) {
			pedido.cdFreteConfig = resultSet.getString("cdFreteConfig");
		}
		pedido.flCalculaSeguro = resultSet.getString("flCalculaSeguro");
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			pedido.vlPctDescHistoricoVendas = resultSet.getDouble("vlPctDescHistoricoVendas");
		}
		if (LavenderePdaConfig.isUsaModoFaturamentoInformacoesAdicionais()) {
			pedido.cdModoFaturamento = resultSet.getString("cdModoFaturamento");
			pedido.dsObsModoFaturamento = resultSet.getString("dsObsModoFaturamento");
		}
		pedido.cdContaCorrente = resultSet.getString("cdContaCorrente");
		pedido.flAguardaPedidoComplementar = resultSet.getString("flAguardaPedidoComplementar");
		pedido.flProcessandoNfeTxt = resultSet.getString("flProcessandoNfeTxt");
		pedido.flModoEstoque = resultSet.getString("flModoEstoque");
		if (LavenderePdaConfig.usaControlePontuacao) {
			pedido.vlTotalPontuacaoBase = resultSet.getDouble("vlTotalPontuacaoBase");
			pedido.vlTotalPontuacaoRealizado = resultSet.getDouble("vlTotalPontuacaoRealizado");
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			pedido.flGondola = resultSet.getString("flGondola");
		}
		if (LavenderePdaConfig.usaValidaConversaoFOB()) {
			pedido.flPendenteFob = resultSet.getString("flPendenteFob");
			pedido.cdUsuarioCancFob = resultSet.getString("cdUsuarioCancFob");
			pedido.cdUsuarioLiberacaoFob = resultSet.getString("cdUsuarioLiberacaoFob");
		}
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() > 0) {
			pedido.cdEnderecoCobranca = resultSet.getString("CDENDERECOCOBRANCA");
		}
		if (LavenderePdaConfig.isNuParcelasNoPedido()) {
			pedido.nuParcelas = resultSet.getInt("nuParcelas");
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			pedido.vlBaseMargemRentab = resultSet.getDouble("vlBaseMargemRentab");
			pedido.vlCustoMargemRentab = resultSet.getDouble("vlCustoMargemRentab");
			pedido.vlPctMargemRentab = resultSet.getDouble("vlPctMargemRentab");
			pedido.cdMargemRentab = resultSet.getString("cdMargemRentab");
		}
		if (LavenderePdaConfig.usaPedidoProdutoCritico) {
			pedido.flCritico = resultSet.getString("flCritico");
		}
		if (LavenderePdaConfig.exibeFlagCdProdutoClienteNoPedido()) {
			pedido.flCodigoInternoCliente = resultSet.getString("flCodigoInternoCliente");
		}
		if (LavenderePdaConfig.usaPedidoPerdido) {
			pedido.cdMotivoPerda = resultSet.getString("cdMotivoPerda");
		}
		pedido.vlPctComissaoTotal = resultSet.getDouble("vlPctComissaoTotal");
		pedido.vlRentabilidadeTotal = resultSet.getDouble("vlRentabilidadeTotal");
		pedido.vlFreteTotal = resultSet.getDouble("vlFreteTotal");
		pedido.flTipoAlteracao = resultSet.getString("flTipoAlteracao");
		if (LavenderePdaConfig.isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao()) {
			pedido.cdMotivoPendencia = resultSet.getString("cdMotivoPendencia");
			pedido.cdMotivoPendenciaJust = resultSet.getString("cdMotivoPendenciaJust");
			pedido.dsObsMotivoPendencia = resultSet.getString("dsObsMotivoPendencia");
		}
		pedido.cdUsuario = resultSet.getString("cdUsuario");
		pedido.qtEncomenda = resultSet.getDouble("QTENCOMENDA");
		pedido.vlTotalEncomenda = resultSet.getDouble("VLTOTALENCOMENDA");
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			pedido.vlRentabilidadeSug = resultSet.getDouble("VLRENTABILIDADESUG");
			pedido.vlRentabTotalItens = resultSet.getDouble("VLRENTABTOTALITENS");
			pedido.vlRentabSugItens = resultSet.getDouble("VLRENTABSUGITENS");
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto()) {
			pedido.flPendenteCondPagto = resultSet.getString("flPendenteCondPagto");
			pedido.dsMotivoCancCondPagto = resultSet.getString("dsMotivoCancCondPagto");
			pedido.cdUsuarioCancCondPagto = resultSet.getString("cdUsuarioCancCondPagto");
			pedido.cdUsuarioLiberacaoCondPagto = resultSet.getString("cdUsuarioLiberacaoCondPagto");
		}
		pedido.qtDiasVctoParcelas = resultSet.getString("qtDiasVctoParcelas");
		if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
			pedido.flCalculaPontuacao = resultSet.getString("flCalculaPontuacao");
		}
		if (LavenderePdaConfig.usaPedidoViaCampanhaPublicitaria()) {
			pedido.flVinculaCampanhaPublicitaria = resultSet.getString("flVinculaCampanhaPublicitaria");
			pedido.cdCampanhaPublicitaria = resultSet.getString("cdCampanhaPublicitaria");
		}
		pedido.dtFaturamento = resultSet.getDate("dtFaturamento");
		pedido.flEdicaoBloqueada = resultSet.getString("flEdicaoBloqueada");
		if (LavenderePdaConfig.isPermiteInserirVlFreteAdicionalPedido()) {
			pedido.vlFreteAdicional = resultSet.getDouble("vlFreteAdicional");
		}
		if (LavenderePdaConfig.isUsaReservaEstoqueCorrente()){
			pedido.dtSugestaoCliente = resultSet.getDate("dtSugestaoCliente");
		}
		if (LavenderePdaConfig.usaImagemQrCode) {
			pedido.dsQrCodePix = resultSet.getString("DSQRCODEPIX");
		}
		if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
			pedido.cdDivisaoVenda = resultSet.getString("CDDIVISAOVENDA");
		}
		if (LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade()) {
			pedido.flUtilizaRentabilidade = resultSet.getString("FLUTILIZARENTABILIDADE");
		}
		return pedido;
	}

	protected BasePersonDomain populateDyn(ResultSet resultSet) throws java.sql.SQLException {
		BasePersonDomain domain = populateColumnsDynFixas(resultSet);
		domain.initAtributosDyn(Pedido.TABLE_NAME_PEDIDO);
		populatePerson(resultSet, domain);
		return domain;
	}

	public Vector findAllPedidoClienteFechadoOrTransmitidoNoMes(Pedido pedido) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA, CDREPRESENTANTE, NUPEDIDO, FLORIGEMPEDIDO, CDTIPOPEDIDO");
		sql.append(" FROM ").append(tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND CDCLIENTE = ").append(Sql.getValue(pedido.cdCliente));
		sql.append(" AND (DTEMISSAO >= ").append(Sql.getValue(DateUtil.getFirstUtilDayOfMonth(pedido.dtEmissao)));
		sql.append(" AND DTEMISSAO <= ").append(Sql.getValue(DateUtil.getLastDayOfMonth(pedido.dtEmissao))).append(")");
		sql.append(" AND (CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado));
		sql.append(" OR CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido)).append(")");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector pedidoList = new Vector();
			Pedido pedidoResult;
			while (rs.next()) {
				pedidoResult = new Pedido();
				pedidoResult.cdEmpresa = rs.getString("cdEmpresa");
				pedidoResult.cdRepresentante = rs.getString("cdRepresentante");
				pedidoResult.nuPedido = rs.getString("nuPedido");
				pedidoResult.flOrigemPedido = rs.getString("flOrigemPedido");
				pedidoResult.cdTipoPedido = rs.getString("cdTipoPedido");
				pedidoList.addElement(pedidoResult);
			}
			return pedidoList;
		}
	}
	
	public int countPedidoClienteFechadoOrTransmitidoNoMes(Pedido pedido) throws java.sql.SQLException {
		Vector list = findAllPedidoClienteFechadoOrTransmitidoNoMes(pedido);
		Vector listFinal = new Vector();
		if (ValueUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				Pedido ped = (Pedido) list.items[i];
				TipoPedido tipoPedido = TipoPedidoService.getInstance().getTipoPedido(ped.cdTipoPedido);
				if (tipoPedido != null && tipoPedido.isGeraAtendimento()) {
					listFinal.addElement(ped);
					break;
				}
			}
		}
		return listFinal.size();
	}

	public Vector findAllPedidosByFrequenciaSugestaoVenda(Pedido pedido, boolean onDeletePedido) throws java.sql.SQLException {
		if (onDeletePedido) {
			return findAllPedidosFechadosPdaResumido(pedido);
		}
		return VectorUtil.concatVectors(findAllPedidosPdaResumido(pedido), PedidoPdbxDao.getInstanceErp().findAllPedidosPdaResumido(pedido));
	}
	
	private Vector findAllPedidosFechadosPdaResumido(Pedido pedido) throws SQLException {
		Vector list = findAllPedidosPdaResumido(pedido);
		Vector listFinal = new Vector();
		if (ValueUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				Pedido ped = (Pedido) list.items[i];
				if (ped.isPedidoFechado()) {
					listFinal.addElement(ped);
				}
			}
		}
		return listFinal;
	}
	
	public Vector findAllInCurrentMonth(ItemPedido itemPedido, String tableName) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA, CDREPRESENTANTE, NUPEDIDO, FLORIGEMPEDIDO");
		sql.append(" FROM ").append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa));
		sql.append(" and CDCLIENTE = ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente));
		Date dtInicial = DateUtil.getDateValue(1, DateUtil.getCurrentDate().getMonth(), DateUtil.getCurrentDate().getYear());
		Date dtFinal = DateUtil.getLastDayOfMonth(DateUtil.getCurrentDate());
		sql.append(" and DTEMISSAO >= ").append(Sql.getValue(dtInicial));
		sql.append(" and DTEMISSAO <= ").append(Sql.getValue(dtFinal));
		sql.append(" and CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector pedidoList = new Vector();
			Pedido pedido;
			while (rs.next()) {
				pedido = new Pedido();
				pedido.cdEmpresa = rs.getString("cdEmpresa");
				pedido.cdRepresentante = rs.getString("cdRepresentante");
				pedido.nuPedido = rs.getString("nuPedido");
				pedido.flOrigemPedido = rs.getString("flOrigemPedido");
				pedidoList.addElement(pedido);
			}
			return pedidoList;
		}
	}

	public void marcaPedidoPendente(Pedido pedido) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET FLPENDENTE = ").append(Sql.getValue(ValueUtil.VALOR_SIM)).append(", ");
		sql.append(" VLDESCONTO = ").append(Sql.getValue(pedido.vlDesconto)).append(", ");
		sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(pedido.vlPctDesconto)).append(" ");
		addWhereByPrimaryKey(sql, pedido);
		executeUpdate(sql.toString());
	}

	public void marcaPedidoPendenteLimCred(Pedido pedido) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET FLPENDENTELIMCRED = ").append(Sql.getValue(ValueUtil.VALOR_SIM)).append(", ");
		addWhereByPrimaryKey(sql, pedido);
		executeUpdate(sql.toString());
	}
	
	public void atualizaDescontoPedido(Pedido pedido) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET VLDESCONTO = ").append(Sql.getValue(pedido.vlDesconto)).append(", ");
		sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(pedido.vlPctDesconto)).append(" ");
		addWhereByPrimaryKey(sql, pedido);
		executeUpdate(sql.toString());
	}
	
	public void updateColumnErp(String rowkey, String dsColumn, String value, int type) throws java.sql.SQLException {
		PedidoPdbxDao.getInstanceErp().updateColumn(rowkey, dsColumn, value, type);
	}
	
	public void updateColumnErp(String rowkey, String dsColumn, Object value, int type) throws java.sql.SQLException {
		PedidoPdbxDao.getInstanceErp().updateColumn(rowkey, dsColumn, value, type);
	}
	
	public boolean isPossuiPedidosSemFlReserva(Pedido filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select ");
		sql.append(" count(*) as qtPedidoFlReserva");
		sql.append(" from ").append(tableName).append(" tb ");
		addWhereByExample(filter, sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (LavenderePdaConfig.isAtualizaEstoqueComReservaCentralizada() ||  LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
			sqlWhereClause.addAndCondition("FLSITUACAORESERVAEST != ", filter.flSituacaoReservaEstReabrePedido);
		}
		return getInt(sql.toString()) > 0;
	}
	
	private void setCondicaoNegociacaoNoPedido(Pedido pedido, String cdCondNegociacao) throws SQLException {
		if (ValueUtil.isNotEmpty(cdCondNegociacao)) {
			pedido.setCondicaoNegociacao(CondicaoNegociacaoService.getInstance().findCondicaoNegociacao(cdCondNegociacao));
		} else {
			pedido.setCondicaoNegociacao(new CondicaoNegociacao());
		}
	}

	public Vector findAllPedidoOriginalByPedidoErp() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT");
		if (LavenderePdaConfig.usaPedidoComplementar()) {
			sql.append(" PEDERP.NUPEDCOMPRELACIONADO AS NUPEDCOMPRELACIONADOERP,");
		}
		sql.append(" PED.*, STP.DSSTATUSPEDIDO, STP.FLCOMPLEMENTAVEL");
		sql.append(" FROM ").append(tableName).append(" PED ");
		sql.append(" JOIN TBLVPSTATUSPEDIDOPDA STP ");
		sql.append(" ON PED.CDSTATUSPEDIDO = STP.CDSTATUSPEDIDO");
		sql.append(" JOIN TBLVPPEDIDOERP PEDERP ");
		sql.append(" ON PED.CDEMPRESA = PEDERP.CDEMPRESA");
		sql.append(" AND PED.CDREPRESENTANTE = PEDERP.CDREPRESENTANTE");
		sql.append(" AND PED.NUPEDIDO = PEDERP.NUPEDIDORELACIONADO");
		sql.append(" AND PED.FLORIGEMPEDIDO = PEDERP.FLORIGEMPEDIDORELACIONADO");
		Vector pedidoList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				Pedido pedido = (Pedido) populate(getBaseDomainDefault(), rs);
				if (LavenderePdaConfig.usaPedidoComplementar()) {
					pedido.nuPedCompRelacionado = rs.getString("NUPEDCOMPRELACIONADOERP");
				}
				pedidoList.addElement(pedido);
			}
		}
		return pedidoList;
	}
	
	public void updateReabrirPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set ");
		addUpdateValues(pedido, sql);
		sql.append(" where rowkey = ").append(Sql.getValue(pedido.getRowKey()));
		sql.append(" and FLTIPOALTERACAO <> ").append(Sql.getValue(Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO));
		sql.append(" and CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado));
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			throw new PedidoNaoFechadoException(Messages.PEDIDO_MSG_ERRO_REABRIR_PROCESSADO_OUTRO_SERVICO);
		}
	}
	
	public void updateCancelarPedidoFechadoOffline(final Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set ");
		sql.append(" CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
		sql.append(", CDMOTIVOCANCELAMENTO = ").append(Sql.getValue(pedido.cdMotivoCancelamento));
		sql.append(", DSJUSTIFICATIVACANCELAMENTO = ").append(Sql.getValue(pedido.dsJustificativaCancelamento));
		sql.append(" where rowkey = ").append(Sql.getValue(pedido.getRowKey()));
		sql.append(" and FLTIPOALTERACAO = ").append(Sql.getValue(Pedido.FLTIPOALTERACAO_ALTERADO));
		sql.append(" and CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado));
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			throw new ImpossivelCancelarPedidoException(Messages.PEDIDO_MSG_ERRO_CANCELAR_PROCESSADO_OUTRO_SERVICO);
		}
	}
	
	public double sumVlTotalPedidosConsideraVolumeVendaMensalPorStatus(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(VLTOTALITENS) VLTOTALITENSVOLVENDA FROM ").append(tableName).append(" TB ");
		DaoUtil.addJoinCliente(sql);
		DaoUtil.addJoinStatusPedidoPda(sql);
		sql.append(" WHERE ").append(DaoUtil.ALIAS_CLIENTE).append(".CDREDE = ").append(Sql.getValue(pedido.getCliente().cdRede));
		sql.append(" AND ").append(DaoUtil.ALIAS_STATUSPEDIDOPDA).append(".FLCONSIDERAVOLUMEVENDAMENSAL = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append(" AND TB.NUPEDIDO != ").append(Sql.getValue(pedido.nuPedido));
		return getDouble(sql.toString());
	}
	
	public Vector findAllPedidosDaRede(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT TB.* FROM ").append(tableName).append(" TB");
		DaoUtil.addJoinCliente(sql);
		DaoUtil.addJoinRedeClientes(sql, DaoUtil.ALIAS_CLIENTE);
		sql.append(" WHERE ").append(DaoUtil.ALIAS_REDECLIENTE).append(".CDREDE = ").append(Sql.getValue(pedido.getCliente().cdRede));
		sql.append(" AND TB.CDSTATUSPEDIDO = ").append(LavenderePdaConfig.cdStatusPedidoFechado);
		sql.append(" AND TB.NUPEDIDO != ").append(Sql.getValue(pedido.nuPedido));
		Vector pedidoList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	        while (rs.next()) {
	        	pedidoList.addElement(populate(pedido, rs));
	        } 
		}
		return pedidoList;
	}

	public void updateProtocolo(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set ");
		sql.append(" NULOTEPROTOCOLO = '").append(pedido.nuLoteProtocolo).append("',");
		sql.append(" FLENVIADOPROTOCOLO = '").append(pedido.flEnviadoProtocolo).append("'");
		sql.append( "WHERE rowkey = '").append(pedido.rowKey).append("'");
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public int countPedidosNaoTransmitidosFechamentoDiario(Pedido pedidoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT COUNT(1) FROM ").append(tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedidoFilter.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedidoFilter.cdRepresentante));
		sql.append(" AND CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		sql.append(" AND CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
		sql.append(" AND CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido));
		return getInt(sql.toString());
	}

	public void resetFlProcessandoNfeTxt() {
		try {
			String sql = getSqlBuffer()
				.append("UPDATE ").append(tableName).append(" SET ")
				.append(Pedido.NMCOLUNA_FLPROCESSANDONFETXT).append(" = ").append(Sql.getValue(LavenderePdaConfig.cdStatusResetNfeTxt))
				.append(" WHERE ")
				.append(Pedido.NMCOLUNA_FLPROCESSANDONFETXT).append(" = ").append(Sql.getValue(LavenderePdaConfig.cdStatusProcessandoNfeTxt)).toString();
			executeUpdate(sql);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		Pedido pedidoFilter = (Pedido) domainFilter;
		DaoUtil.addJoinStatusPedidoPda(sql);
		if (pedidoFilter.onResumoDiario || pedidoFilter.addJoinTipoPedidoOnCadastroSac) {
			DaoUtil.addJoinTipoPedido(sql);
		}
		if (pedidoFilter.onResumoDiario) {
			DaoUtil.addLeftJoinCliente(sql);
		}
	}

	@Override
	protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
		Pedido pedido = (Pedido) domainFilter;
		DaoUtil.addJoinStatusPedidoPda(sql);
		DaoUtil.addJoinCliente(sql, pedido.cliente);
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) || LavenderePdaConfig.isUsaPedidoBonificacao())) {
			DaoUtil.addJoinTipoPedido(sql);
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			DaoUtil.addJoinStatusOrcamento(sql);
		}
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			DaoUtil.addJoinRepresentante(sql, "REP");
		}
	}

	public void updatePontuacaoPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(tableName)
           .append(" SET VLTOTALPONTUACAOBASE = ").append(Sql.getValue(pedido.vlTotalPontuacaoBase))
           .append(", VLTOTALPONTUACAOREALIZADO = ").append(Sql.getValue(pedido.vlTotalPontuacaoRealizado))
		   .append(", FLCALCULAPONTUACAO = ").append(Sql.getValue(pedido.flCalculaPontuacao))
           .append(" WHERE ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
        executeUpdate(sql.toString());
	}

	private void addWhereByPrimaryKey(StringBuffer sql, Pedido pedido) {
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa))
				.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante))
				.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido))
				.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(ValueUtil.VALOR_EMBRANCO);
	}

	public List<Pedido> findPedidosFechadosSemNfe(boolean notNull) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TB.*, SPDA.DSSTATUSPEDIDO FROM TBLVPPEDIDO TB ")
			.append(" JOIN TBLVPSTATUSPEDIDOPDA SPDA ON TB.CDSTATUSPEDIDO = SPDA.CDSTATUSPEDIDO ")
			.append(" JOIN TBLVPTIPOPEDIDO TIPO ON TB.CDEMPRESA = TIPO.CDEMPRESA AND TB.CDREPRESENTANTE = TIPO.CDREPRESENTANTE ")
				.append(" AND TB.CDTIPOPEDIDO = TIPO.CDTIPOPEDIDO ")
			.append(" LEFT JOIN TBLVPNFE NFE ON TB.CDEMPRESA = NFE.CDEMPRESA AND TB.CDREPRESENTANTE = NFE.CDREPRESENTANTE")
				.append(" AND TB.FLORIGEMPEDIDO = NFE.FLORIGEMPEDIDO AND TB.NUPEDIDO = NFE.NUPEDIDO ");
		sql.append(" WHERE TB.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado))
			.append(" AND TB.FLPROCESSANDONFETXT = ").append(Sql.getValue(LavenderePdaConfig.cdStatusResetNfeTxt))
			.append(" AND TIPO.FLGERANFE = ").append(Sql.getValue(ValueUtil.VALOR_SIM))
			.append(" AND NFE.CDEMPRESA IS ");
		if (notNull) {
			sql.append(" NOT ");
		}
		sql.append(" NULL ");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			List<Pedido> pedidoSemNfeList = new ArrayList<>();
			while (rs.next()) {
				Pedido pedido = (Pedido) populate(getBaseDomainDefault(), rs);
				pedidoSemNfeList.add(pedido);
			}
			return pedidoSemNfeList;
		}
	}
	
	public Vector findPedidoRelacionadoList(Pedido pedidoFilter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TB.*, SPDA.DSSTATUSPEDIDO, SPDA.FLCOMPLEMENTAVEL FROM ").append(tableName).append(" TB ")
		.append(" JOIN TBLVPSTATUSPEDIDOPDA SPDA ON TB.CDSTATUSPEDIDO = SPDA.CDSTATUSPEDIDO ")
		.append(" JOIN TBLVPTIPOPEDIDO TIPO ON TB.CDEMPRESA = TIPO.CDEMPRESA AND TB.CDREPRESENTANTE = TIPO.CDREPRESENTANTE AND TB.CDTIPOPEDIDO = TIPO.CDTIPOPEDIDO ");
		addJoinPedidoRelacionado(sql);
		addWhereByExample(pedidoFilter, sql);
		sql.append(" AND TIPO.FLPRODUCAO = '").append(TipoPedido.TIPO_PEDIDO_PRODUCAO).append("' ");
		sql.append(" AND SPDA.FLRELACIONAPEDIDO = '").append(ValueUtil.VALOR_SIM).append("' ");
		sql.append(" AND (TB.CDSTATUSPEDIDO = '").append(LavenderePdaConfig.cdStatusPedidoTransmitido).append("' ");
		sql.append(" OR (TB.FLORIGEMPEDIDO = '").append(OrigemPedido.FLORIGEMPEDIDO_ERP).append("' ");
		sql.append(" AND TB.CDSTATUSPEDIDO != '").append(LavenderePdaConfig.cdStatusPedidoCancelado).append("')) ");
		addWhereByFiltroPedidoRelacionado(pedidoFilter, sql);
		addOrderByPedidoRelacionado(sql, pedidoFilter);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector pedidoList = new Vector();
			while (rs.next()) {
				Pedido pedido = (Pedido) populate(pedidoFilter, rs);
				pedidoList.addElement(pedido);
			}
			return pedidoList;
		}
		
	}

	public Pedido findPedidoRelBonificacao(Pedido pedido) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDUSUARIO, NUPEDIDO, VLTOTALPEDIDO FROM  ").append(tableName);
		sql.append(" WHERE  CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND CDUSUARIO = ").append(Sql.getValue(pedido.cdUsuario));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				pedido.cdEmpresa = rs.getString("cdEmpresa");
				pedido.cdRepresentante = rs.getString("cdRepresentante");
				pedido.cdEmpresa = rs.getString("cdEmpresa");
				pedido.cdUsuario = rs.getString("cdUsuario");
				pedido.nuPedido = rs.getString("nuPedido");
				pedido.vlTotalPedido = rs.getDouble("vlTotalPedido");
				return pedido;
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return pedido;
	}
	
	public void updatePedidoPerdido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(tableName)
           .append(" SET CDMOTIVOPERDA = ").append(Sql.getValue(pedido.cdMotivoPerda))
           .append(", CDSTATUSPEDIDO = ").append(Sql.getValue(pedido.cdStatusPedido))
           .append(", DSOBSERVACAO = ").append(Sql.getValue(pedido.dsObservacao))
           .append(", FLPENDENTE = ").append(Sql.getValue(pedido.flPendente))
           .append(" WHERE ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
        executeUpdate(sql.toString());
	}

	private String getExistsPedidosByNivelLiberacaoPorRegraLiberacao(int nuOrdemLiberacaoFilter) {
		StringBuffer sql = new StringBuffer();
		if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isPermiteRelacionarPedidoNaBonificacao()) {
			sql.append("EXISTS (SELECT 1 FROM TBLVPPEDIDOERP TBERP ")
					.append(" JOIN TBLVPSTATUSPEDIDOPDA SPDA ON TBERP.CDSTATUSPEDIDO = SPDA.CDSTATUSPEDIDO")
					.append(" JOIN TBLVPTIPOPEDIDO TPPEDIDO ON TBERP.CDEMPRESA = TPPEDIDO.CDEMPRESA AND TPPEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class)))
					.append(" AND TBERP.CDTIPOPEDIDO = TPPEDIDO.CDTIPOPEDIDO AND TBERP.CDUSUARIO = TPPEDIDO.CDUSUARIO")
					.append(" WHERE TBERP.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoPendenteAprovacao)).append(" AND EXISTS (")
					.append(" SELECT 1 FROM ( SELECT * FROM (")
					.append(" SELECT AUX.CDEMPRESA, AUX.CDREPRESENTANTE, AUX.FLORIGEMPEDIDO, AUX.NUPEDIDO, AUX.CDMOTIVOPENDENCIA, MAX(AUX.NUORDEMLIBERACAO) AS MAX_NUORDEMLIBERACAO")
					.append(" FROM TBLVPITEMPEDIDOERP AUX")
					.append(" WHERE AUX.CDEMPRESA = TBERP.CDEMPRESA AND AUX.CDREPRESENTANTE = TBERP.CDREPRESENTANTE AND AUX.FLORIGEMPEDIDO = TBERP.FLORIGEMPEDIDO")
					.append(" AND AUX.NUPEDIDO = TBERP.NUPEDIDO GROUP BY AUX.CDEMPRESA, AUX.CDREPRESENTANTE, AUX.FLORIGEMPEDIDO, AUX.NUPEDIDO, AUX.CDMOTIVOPENDENCIA) ITE")
					.append(" LEFT JOIN TBLVPMOTIVOPENDENCIA MOT ON MOT.CDEMPRESA = ITE.CDEMPRESA")
					.append(" AND MOT.CDMOTIVOPENDENCIA = ITE.CDMOTIVOPENDENCIA")
					.append(" LEFT JOIN TBLVPPEDIDODESC PEDDESC ON PEDDESC.CDEMPRESA = ITE.CDEMPRESA AND PEDDESC.CDREPRESENTANTE = ITE.CDREPRESENTANTE")
					.append(" AND PEDDESC.FLORIGEMPEDIDO = ITE.FLORIGEMPEDIDO AND PEDDESC.NUPEDIDO = ITE.NUPEDIDO AND PEDDESC.NUSEQUENCIA = ").append(nuOrdemLiberacaoFilter)
					.append(" WHERE PEDDESC.CDEMPRESA IS NULL AND ((MOT.FLREGRALIBERACAO = '1' AND ITE.MAX_NUORDEMLIBERACAO >= ").append(nuOrdemLiberacaoFilter)
					.append(" OR  (MOT.FLREGRALIBERACAO = '2' AND ITE.MAX_NUORDEMLIBERACAO = ").append(nuOrdemLiberacaoFilter).append(")")
					.append(" OR (TBERP.NUPEDIDORELBONIFICACAO IS NOT NULL AND (TPPEDIDO.FLBONIFICACAO = 'N' OR TPPEDIDO.FLBONIFICACAO IS NULL))").append("))) nivel)");
			sql.append(" AND TB.CDEMPRESA = TBERP.CDEMPRESA AND TB.CDREPRESENTANTE = TBERP.CDREPRESENTANTE AND TB.NUPEDIDO = TBERP.NUPEDIDO AND TB.FLORIGEMPEDIDO = TBERP.FLORIGEMPEDIDO)");
		} else {
			sql.append("EXISTS (SELECT 1 FROM TBLVPPEDIDOERP TBERP ")
					.append(" JOIN TBLVPSTATUSPEDIDOPDA SPDA ON TBERP.CDSTATUSPEDIDO = SPDA.CDSTATUSPEDIDO")
					.append(" WHERE TBERP.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoPendenteAprovacao)).append(" AND EXISTS (")
					.append(" SELECT 1 FROM ( SELECT * FROM (")
					.append(" SELECT AUX.CDEMPRESA, AUX.CDREPRESENTANTE, AUX.FLORIGEMPEDIDO, AUX.NUPEDIDO, AUX.CDMOTIVOPENDENCIA, MAX(AUX.NUORDEMLIBERACAO) AS MAX_NUORDEMLIBERACAO")
					.append(" FROM TBLVPITEMPEDIDOERP AUX")
					.append(" WHERE AUX.CDEMPRESA = TBERP.CDEMPRESA AND AUX.CDREPRESENTANTE = TBERP.CDREPRESENTANTE AND AUX.FLORIGEMPEDIDO = TBERP.FLORIGEMPEDIDO")
					.append(" AND AUX.NUPEDIDO = TBERP.NUPEDIDO GROUP BY AUX.CDEMPRESA, AUX.CDREPRESENTANTE, AUX.FLORIGEMPEDIDO, AUX.NUPEDIDO, AUX.CDMOTIVOPENDENCIA) ITE")
					.append(" JOIN TBLVPMOTIVOPENDENCIA MOT ON MOT.CDEMPRESA = ITE.CDEMPRESA")
					.append(" AND MOT.CDMOTIVOPENDENCIA = ITE.CDMOTIVOPENDENCIA")
					.append(" LEFT JOIN TBLVPPEDIDODESC PEDDESC ON PEDDESC.CDEMPRESA = ITE.CDEMPRESA AND PEDDESC.CDREPRESENTANTE = ITE.CDREPRESENTANTE")
					.append(" AND PEDDESC.FLORIGEMPEDIDO = ITE.FLORIGEMPEDIDO AND PEDDESC.NUPEDIDO = ITE.NUPEDIDO AND PEDDESC.NUSEQUENCIA = ").append(nuOrdemLiberacaoFilter)
					.append(" WHERE PEDDESC.CDEMPRESA IS NULL AND ((MOT.FLREGRALIBERACAO = '1' AND ITE.MAX_NUORDEMLIBERACAO >= ").append(nuOrdemLiberacaoFilter)
					.append(" OR  (MOT.FLREGRALIBERACAO = '2' AND ITE.MAX_NUORDEMLIBERACAO = ").append(nuOrdemLiberacaoFilter).append(" ))) ")
					.append(") nivel)");
			sql.append(" AND TB.CDEMPRESA = TBERP.CDEMPRESA AND TB.CDREPRESENTANTE = TBERP.CDREPRESENTANTE AND TB.NUPEDIDO = TBERP.NUPEDIDO AND TB.FLORIGEMPEDIDO = TBERP.FLORIGEMPEDIDO)");	
		}
		return sql.toString();
	}

	private void addSelectMarcadores(StringBuffer sql) {
		sql.append(", (select GROUP_CONCAT(DISTINCT m.cdmarcador) marcadores ");
		sql.append(" FROM TBLVPMARCADOR m INNER JOIN TBLVPMARCADORPEDIDO ml on (ml.cdEmpresa = tb.cdEmpresa and ml.cdRepresentante = tb.cdRepresentante");
		sql.append(" and ml.flOrigemPedido = tb.flOrigemPedido and ml.nuPedido = tb.nuPedido and ml.cdMarcador = m.CDMARCADOR)");
		sql.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE()) ORDER BY m.NUSEQUENCIA, m.DSMARCADOR ");
		sql.append(" ) AS cdMarcadores ");
	}

	public void updatePedidoMotivoPendente(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(tableName)
				.append(" SET CDMOTIVOPENDENCIA = ").append(Sql.getValue(pedido.cdMotivoPendencia))
				.append(", CDMOTIVOPENDENCIAJUST = ").append(Sql.getValue(pedido.cdMotivoPendenciaJust))
				.append(", DSOBSMOTIVOPENDENCIA = ").append(Sql.getValue(pedido.dsObsMotivoPendencia))
				.append(" WHERE ROWKEY = ").append(Sql.getValue(pedido.getRowKey()));
		executeUpdate(sql.toString());
	}
	
	public Pedido sumVlTotalPedidosFechamentoDiario(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("WITH PEDIDO (CDEMPRESA, CDREPRESENTANTE, CDTIPOPEDIDO, CDTIPOPAGAMENTO, VLTOTALPEDIDO) AS (")
		.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDTIPOPEDIDO, CDTIPOPAGAMENTO, VLTOTALPEDIDO FROM ").append(tableName).append(" tb");
		addWhereByExample(pedido, sql);
		sql.append(" UNION ALL ")
		.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDTIPOPEDIDO, CDTIPOPAGAMENTO, VLTOTALPEDIDO FROM TBLVPPEDIDOERP tb");
		addWhereByExample(pedido, sql);
		sql.append(")");
		sql.append("SELECT SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' THEN VLTOTALPEDIDO ELSE 0 END), SUM(CASE WHEN tpp.FLBONIFICACAO = 'S' THEN VLTOTALPEDIDO ELSE 0 END),")
		.append("SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' AND COALESCE(tp.FLCHEQUE, 'N') = 'S' AND tp.CDTIPOPAGAMENTO IS NOT NULL THEN VLTOTALPEDIDO ELSE 0 END) VLTOTALCHEQUE,")
		.append("SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' AND COALESCE(tp.FLBOLETO, 'N') = 'S' AND tp.CDTIPOPAGAMENTO IS NOT NULL THEN VLTOTALPEDIDO ELSE 0 END) VLTOTALBOLETO,")
		.append("SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' AND COALESCE(tp.FLBOLETO, 'N') = 'N' AND COALESCE(tp.FLCHEQUE, 'N') = 'N' AND tp.CDTIPOPAGAMENTO IS NOT NULL THEN VLTOTALPEDIDO ELSE 0 END) VLTOTALDINHEIRO")
		.append(" FROM PEDIDO").append(" tb")
		.append(" LEFT JOIN TBLVPTIPOPAGAMENTO tp ON tb.CDEMPRESA = tp.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = tp.CDREPRESENTANTE AND ")
		.append(" tb.CDTIPOPAGAMENTO = tp.CDTIPOPAGAMENTO ")
		.append(" LEFT JOIN TBLVPTIPOPEDIDO tpp ON tpp.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" tpp.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" tpp.CDTIPOPEDIDO = tb.CDTIPOPEDIDO");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				Pedido pedidoValue = new Pedido();
				pedidoValue.vlTotalPedido = rs.getDouble(1);
				pedidoValue.vlBonificacaoPedido = rs.getDouble(2);
				pedidoValue.vlTotalCheque = rs.getDouble(3);
				pedidoValue.vlTotalBoleto = rs.getDouble(4);
				pedidoValue.vlTotalDinheiro = rs.getDouble(5);
				return pedidoValue;
			}
			return null;
		}
	}
	
	public Vector findSumTotalPedidoFechamentoDiarioCliente(Pedido filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("WITH PEDIDO (CDEMPRESA, CDREPRESENTANTE, CDCLIENTE, CDTIPOPEDIDO, CDTIPOPAGAMENTO, VLTOTALPEDIDO) AS (")
		.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDCLIENTE, CDTIPOPEDIDO, CDTIPOPAGAMENTO, VLTOTALPEDIDO FROM ").append(tableName).append(" tb");
		addWhereByExample(filter, sql);
		sql.append(" UNION ALL ")
		.append("SELECT CDEMPRESA, CDREPRESENTANTE, CDCLIENTE, CDTIPOPEDIDO, CDTIPOPAGAMENTO, VLTOTALPEDIDO FROM TBLVPPEDIDOERP tb");
		addWhereByExample(filter, sql);
		sql.append(")")
		.append("SELECT SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' AND COALESCE(tp.FLCHEQUE, 'N') = 'S' AND tp.CDTIPOPAGAMENTO IS NOT NULL THEN VLTOTALPEDIDO ELSE 0 END) VLTOTALCHEQUE,")
		.append("SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' AND COALESCE(tp.FLBOLETO, 'N') = 'S' AND tp.CDTIPOPAGAMENTO IS NOT NULL THEN VLTOTALPEDIDO ELSE 0 END) VLTOTALBOLETO,")
		.append("SUM(CASE WHEN COALESCE(tpp.FLBONIFICACAO, 'N') = 'N' AND COALESCE(tp.FLBOLETO, 'N') = 'N' AND COALESCE(tp.FLCHEQUE, 'N') = 'N' AND tp.CDTIPOPAGAMENTO IS NOT NULL THEN VLTOTALPEDIDO ELSE 0 END) VLTOTALDINHEIRO,")
		.append(" tb.CDCLIENTE, cli.NMRAZAOSOCIAL")
		.append(" FROM PEDIDO").append(" tb")
		.append(" JOIN TBLVPCLIENTE cli ON cli.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" cli.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" cli.CDCLIENTE = tb.CDCLIENTE ")
		.append(" LEFT JOIN TBLVPTIPOPAGAMENTO tp ON tb.CDEMPRESA = tp.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = tp.CDREPRESENTANTE AND ")
		.append(" tb.CDTIPOPAGAMENTO = tp.CDTIPOPAGAMENTO ")
		.append(" LEFT JOIN TBLVPTIPOPEDIDO tpp ON tpp.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" tpp.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" tpp.CDTIPOPEDIDO = tb.CDTIPOPEDIDO")
		.append(" GROUP BY tb.CDCLIENTE");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			while (rs.next()) {
				Pedido pedido = new Pedido();
				pedido.vlTotalCheque = rs.getDouble(1);
				pedido.vlTotalBoleto = rs.getDouble(2);
				pedido.vlTotalDinheiro = rs.getDouble(3);
				Cliente cliente = new Cliente();
				cliente.cdCliente = rs.getString(4);
				cliente.nmRazaoSocial = rs.getString(5);
				pedido.setCliente(cliente);
				list.addElement(pedido);
			}
			return list;
		}
	}

	private void addWhereByFiltroPedidoRelacionado(Pedido pedidoFilter, StringBuffer sql) {
		if (ValueUtil.isNotEmpty(pedidoFilter.cdRedeCliente)) {
			sql.append(" AND CLI.CDREDE = '").append(pedidoFilter.cdRedeCliente).append("'");
		}
		if (ValueUtil.isNotEmpty(pedidoFilter.dsClienteFilter)) {
			sql.append(" AND CLI.NMRAZAOSOCIAL LIKE '%").append(pedidoFilter.dsClienteFilter).append("%'");
		}
	}

	private void addJoinPedidoRelacionado(StringBuffer sql) {
		DaoUtil.addJoinCliente(sql);
	}
	
	private void addOrderByPedidoRelacionado(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			sql.append(" order by ");
			String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
			String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
			for (int i = 0; i < sortAtributtes.length; i++) {
				sql.append(sortAtributtes[i]);
				sql.append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
				if (!(i == (sortAtributtes.length - 1))) {
					sql.append(" , ");
				}
			}
		}
	}

	public int updateFlPendentePedidoRelacionado(Pedido pedidoFilter, String flPendente) {
		try {
			StringBuffer sql = getSqlBuffer();
			sql.append(" update ").append(tableName).append(" set");
			sql.append(" FLPENDENTE = ").append(Sql.getValue(flPendente));
			sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedidoFilter.cdEmpresa));
			sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedidoFilter.cdRepresentante));
			sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedidoFilter.flOrigemPedido));
			sql.append(" and NUPEDIDORELBONIFICACAO = ").append(Sql.getValue(pedidoFilter.nuPedidoRelBonificacao));
			return executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return 0;
		}
	}

}
