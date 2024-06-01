package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.HistoricoItem;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.service.ItemComboAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemComboService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.json.JSONObject;
import totalcross.sql.ResultSet;
import totalcross.sql.ResultSetMetaData;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ItemPedidoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedido();
	}

    private static ItemPedidoPdbxDao instance;
    private static ItemPedidoPdbxDao instanceErp;

    public ItemPedidoPdbxDao() {
        super(ItemPedido.TABLE_NAME_ITEMPEDIDO);
    }

    public ItemPedidoPdbxDao(String newTableName) {
    	super(newTableName);
    }

    public static ItemPedidoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ItemPedidoPdbxDao(ItemPedido.TABLE_NAME_ITEMPEDIDO);
        }
        return instance;
    }

    public static ItemPedidoPdbxDao getInstanceErp() {
        if (instanceErp == null) {
            instanceErp = new ItemPedidoPdbxDao(ItemPedido.TABLE_NAME_ITEMPEDIDOERP);
        }
        return instanceErp;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		ItemPedido itemPedidoFilter = (ItemPedido) domainFilter;
        sql.append(" TB.rowKey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.FLORIGEMPEDIDO,");
        sql.append(" TB.NUPEDIDO,");
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.FLTIPOITEMPEDIDO,");
        sql.append(" TB.NUSEQPRODUTO,");
        sql.append(" TB.CDITEMGRADE1,");
        sql.append(" TB.CDITEMGRADE2,");
        sql.append(" TB.CDITEMGRADE3,");
        sql.append(" TB.NUSEQITEMPEDIDO,");
        sql.append(" TB.CDTABELAPRECO,");
        sql.append(" TB.QTITEMFISICO,");
        sql.append(" TB.VLITEMPEDIDO,");
        sql.append(" TB.VLBASEITEMTABELAPRECO,");
        sql.append(" TB.VLBASECALCULODESCPROMOCIONAL,");
        sql.append(" TB.VLBASEITEMPEDIDO,");
        sql.append(" TB.VLTOTALITEMPEDIDO,");
        sql.append(" TB.VLPCTDESCONTO,");
        sql.append(" TB.VLPCTACRESCIMO,");
        sql.append(" TB.VLTOTALBRUTOITEMPEDIDO,");
	    sql.append(" TB.VLUNIDADEPADRAO,");
		if (needAddVlUnidadePadraoAndBaseEmbElementar()) {
			sql.append(" TB.VLBASEEMBALAGEMELEMENTAR,");
		}
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			sql.append(" TB.CDLINHA,");
		}
		if (LavenderePdaConfig.usaConversaoUnidadesMedida) {
			sql.append(" TB.QTITEMFATURAMENTO,");
		}
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual) {
			sql.append(" TB.VLBASEFLEX,");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() || LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" TB.VLRENTABILIDADE,");
			sql.append(" TB.VLRENTABILIDADESUG,");
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo() || LavenderePdaConfig.aplicaComissaoEspecialProdutoPromocional) {
			sql.append(" TB.VLTOTALCOMISSAO,");
			sql.append(" TB.VLPCTCOMISSAO,");
			sql.append(" TB.VLPCTCOMISSAOTOTAL,");
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			sql.append(" TB.VLVERBAITEM,");
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
				sql.append(" TB.VLVERBAITEMPOSITIVO,");
			}
			sql.append(" TB.CDCONTACORRENTE,");
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
			sql.append(" TB.VLRETORNOPRODUTO,");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			sql.append(" TB.VLITEMPEDIDOFRETE,");
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			sql.append(" TB.CDLOTEPRODUTO,");
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto || LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido) {
			sql.append(" TB.FLPRECOLIBERADOSENHA,");
			if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido)  {
				sql.append(" TB.CDUSUARIOLIBERACAO ,");
			}
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			sql.append(" TB.VLST,");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			sql.append(" TB.VLREDUCAOOPTANTESIMPLES,");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			sql.append(" TB.QTPESO,");
		}
		if (LavenderePdaConfig.usaMotivoTrocaPorItemPedido()) {
			sql.append(" TB.CDMOTIVOTROCA,");
			sql.append(" TB.DSOBSMOTIVOTROCA,");
		}
		if (LavenderePdaConfig.usaObservacaoPorItemPedido) {
			sql.append(" TB.DSOBSERVACAO,");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			sql.append(" TB.QTPONTOSITEM,");
		}
		if (LavenderePdaConfig.quantidadeMinimaCaixasPedido > 0) {
			sql.append(" TB.FLVENDIDOQTMINIMA,");
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			sql.append(" TB.CDPRAZOPAGTOPRECO,");
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar) {
			sql.append(" TB.VLITEMPEDIDOUNELEMENTAR,");
			sql.append(" TB.QTITEMPEDIDOUNELEMENTAR,");
		}
		if (LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" TB.VLFRETE,");
		}
		if (LavenderePdaConfig.liberaComSenhaVendaProdutoBloqueado) {
			sql.append(" TB.FLMETAGRUPOPRODLIBERADOSENHA,");
		}
		if (LavenderePdaConfig.usaSenhaVendaRelacionada) {
			sql.append(" TB.FLLIBERADOVENDARELACIONADA,");
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			sql.append(" TB.VLIPIITEM,");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			sql.append(" TB.QTITEMESTOQUEPOSITIVO,");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" TB.DTINICIOPROMOCAO,");
		}
		if (LavenderePdaConfig.usaIndicacaoItemPedidoProdutoPromocional) {
			sql.append(" TB.FLPROMOCAO, ");
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" TB.NUPROMOCAO,");
			sql.append(" TB.VLDESCONTOPROMO,");
			sql.append(" TB.FLIGNORADESCQTDPRO,");
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido() || LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" TB.VLFINALPROMO,");
			sql.append(" TB.VLPCTDESCONTOPROMO,");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			sql.append(" TB.VLPCTFAIXADESCQTD,");
			sql.append(" TB.FLIGNORADESCQTD,");
		}

		addSelectJoinDescQT(domainFilter, sql);

		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			sql.append(" TB.VLDESCONTOCCP,");
			sql.append(" TB.VLPCTDESCONTOCCP,");
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			sql.append(" TB.VLFECOP,");
			sql.append(" TB.VLTOTALFECOPITEM,");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() || LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido()) {
			sql.append(" TB.CDSUGESTAOVENDA,");
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			sql.append(" TB.CDTRIBUTACAOCONFIG,");
			sql.append(" TB.VLTOTALSTITEM,");
			sql.append(" TB.VLTOTALIPIITEM,");
			sql.append(" TB.VLTOTALICMSITEM,");
			sql.append(" TB.VLTOTALPISITEM,");
			sql.append(" TB.VLTOTALCOFINSITEM,");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			sql.append(" TB.VLSEGUROITEMPEDIDO,");
		}
		sql.append(" TB.VLICMS,");
		sql.append(" TB.VLPIS,");
		sql.append(" TB.VLCOFINS,");
		sql.append(" TB.VLDESPESAACESSORIA,");
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1() || LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" TB.VLDESCONTO,");
			sql.append(" TB.VLPCTDESCONTO2,");
			sql.append(" TB.VLDESCONTO2,");
			sql.append(" TB.VLPCTDESCONTO3,");
			sql.append(" TB.VLDESCONTO3,");
		} else if (LavenderePdaConfig.usaDescontoExtra) {
			sql.append(" TB.VLPCTDESCONTO2,");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso()  || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaFreteManualPedido) {
			sql.append(" TB.VLTOTALITEMPEDIDOFRETE,");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			sql.append(" TB.VLPCTDESCONTOCANAL,");
		}
		sql.append(" TB.CDUNIDADE,");
		if (LavenderePdaConfig.liberaComSenhaVendaProdutoSemEstoque) {
			sql.append(" TB.FLESTOQUELIBERADO,");
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			sql.append(" TB.CDVERBAGRUPO,");
		}
		if (LavenderePdaConfig.liberaSenhaQtdItemMaiorPedidoOriginal) {
			sql.append(" TB.FLQUANTIDADELIBERADA,");
		}
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
			sql.append(" TB.CDCONDICAOCOMERCIAL,");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			sql.append(" TB.VLCREDITOCONDICAO,");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			sql.append(" TB.VLCREDITOFRETE,");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo || LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(" TB.FLPENDENTE,");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			sql.append(" TB.VLPCTDESCPROGRESSIVOMIX,");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			sql.append(" TB.VLVOLUMEITEM,");
		}
		boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem = LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem();
		boolean usaDescontoPorVolumeVendaMensal = LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal();
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || usaDescontoPorVolumeVendaMensal || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				sql.append(" TB.VLPCTDESCFRETE,");
			}
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || usaDescontoPorVolumeVendaMensal || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				sql.append(" TB.VLPCTDESCCLIENTE,");
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				sql.append(" TB.VLPCTDESCONTOCONDICAO,");
			}
		}
		if (LavenderePdaConfig.isUsaDivisaoValorVerbaUsuarioEmpresa()) {
			sql.append(" TB.VLPCTVERBARATEIO,");
		}
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			sql.append(" TB.VLPCTMARGEMPRODUTO,");
		}
		if (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco) {
			sql.append(" TB.CDVERBA,");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			sql.append(" TB.VLPCTCONTRATOCLI,");
			sql.append(" TB.FLDECISAOCALCULO,");
		}
		if (LavenderePdaConfig.usaCalculoReversoNaST) {
			sql.append(" TB.VLITEMPEDIDOSTREVERSO,");
			sql.append(" TB.VLPCTDESCONTOSTREVERSO,");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			sql.append(" TB.FLRESTRITO,");
		}
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
			sql.append(" TB.FLVALORTABELAALTERADO,");
		}
		if (LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
			sql.append(" TB.VLINDICEGRUPOPROD,");
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			sql.append(" TB.QTDCREDITODESC,");
			sql.append(" TB.FLTIPOCADASTROITEM,");
			sql.append(" TB.CDPRODUTOCREDITODESC,");
		}
		if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			sql.append(" TB.VLITEMIPI,");
			sql.append(" TB.VLBASEITEMIPI,");
		}
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			sql.append(" TB.QTITEMDESEJADO,");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" TB.VLTOTALMARGEMITEM,");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			sql.append(" TB.FLSUGVENDAPERSON,");
		}
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
			sql.append(" TB.QTESTOQUECLIENTE,");
		}
		if (LavenderePdaConfig.calculaRentabilidadePedidoRetornado
				|| LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" TB.VLTOTALPRECOCUSTO,");
		}
		if (LavenderePdaConfig.calculaPrazoEntregaPorProduto) {
			sql.append(" TB.NUDIASPRAZOENTREGA,");
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			sql.append(" TB.NUCONVERSAOUNIDADEPU,");
			sql.append(" TB.FLDIVIDEMULTIPLICAPU,");
			sql.append(" TB.VLINDICEFINANCEIROPU,");
		}
		if (LavenderePdaConfig.isUsaKitProdutoFechado() || LavenderePdaConfig.isUsaKitTipo3()) {
			sql.append(" TB.CDKIT,");
		}

		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			sql.append(" TB.VLPCTACRESCIMOCONDICAO,");
			sql.append(" TB.VLPCTACRESCCLIENTE,");
			sql.append(" TB.VLPCTDESCALCADA,");
		}
		if (LavenderePdaConfig.usaInfoComplementarItemPedido() || LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.isConfigReservaEstoqueCorrente) {
			sql.append(" TB.DTENTREGA,");
		}
		if (LavenderePdaConfig.usaInfoComplementarItemPedido() || LavenderePdaConfig.usaGradeProduto4()) {
			sql.append(" TB.VLALTURA,");
			sql.append(" TB.VLLARGURA,");
			sql.append(" TB.VLCOMPRIMENTO,");
			sql.append(" TB.VLPOSVINCO1,");
			sql.append(" TB.VLPOSVINCO2,");
			sql.append(" TB.VLPOSVINCO3,");
			sql.append(" TB.VLPOSVINCO4,");
			sql.append(" TB.VLPOSVINCO5,");
			sql.append(" TB.VLPOSVINCO6,");
			sql.append(" TB.VLPOSVINCO7,");
			sql.append(" TB.VLPOSVINCO8,");
			sql.append(" TB.VLPOSVINCO9,");
			sql.append(" TB.VLPOSVINCO10,");
			sql.append(" TB.CDCULTURA,");
			sql.append(" TB.CDPRAGA,");
			sql.append(" TB.VLDOSE,");
		}
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			sql.append(" TB.VLPCTACRESCIMOICMS,")
			.append(" TB.VLPCTDESCONTOICMS,");
		}
		if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sql.append(" TB.CDLOCAL,");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			sql.append(" TB.FLERRORECALCULO,");
		}
		if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
			sql.append(" TB.FLORIGEMESCOLHAITEMPEDIDO,")
			.append(" TB.CDPRODUTOORIGEM,")
			.append(" TB.VLITEMPEDIDOORIGEM,")
			.append(" TB.DTINCLUSAOITEMPEDIDO,")
			.append(" TB.HRINCLUSAOITEMPEDIDO,");
		}
		if (LavenderePdaConfig.usaIndiceClienteGrupoProd) {
			sql.append(" TB.VLINDICECLIENTEGRUPOPROD,");
		}
		if (LavenderePdaConfig.usaProdutoRestrito) {
			sql.append(" TB.VLPCTDESCPRODUTORESTRITO,");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			sql.append(" TB.VLVPC,");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			sql.append(" TB.VLPCTDESCONTOAUTO,")
			.append(" TB.VLPCTDESCONTOEFETIVO,")
			.append(" TB.VLPCTDESCONTOAUTOEFETIVO,")
			.append(" TB.VLDESCONTOAUTO,")
			.append(" TB.VLTOTALDESCONTOAUTO,")
			.append(" TB.VLPRECOEFETIVOUNITARIO,")
			.append(" TB.VLPRECOEFETIVOUNITARIODESC,")
			.append(" TB.VLEFETIVOTOTALITEM,")
			.append(" TB.VLEFETIVOTOTALITEMDESC,")
			.append(" TB.VLDESCONTOTOTALAUTODESC,")
			.append(" TB.VLBASEINTERPOLACAOPRODUTO,");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			sql.append(" TB.CDCOMISSAOPEDIDOREP,");
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			sql.append(" TB.VLITEMFRETETRIBUTACAO,")
			.append(" TB.VLTOTALITEMFRETETRIBUTACAO,");
		}
		sql.append(" TB.VLDESCONTOCONDICAO,");
		if (LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueComDetalhes()) {
			sql.append(" TB.QTESTOQUEPREVISTO,");
			sql.append(" TB.DTESTOQUEPREVISTO,");
		}
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			sql.append(" TB.VLPCTDESCCONDPAGTO,");
		}
		if (LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto()) {
			sql.append(" TB.VLVERBAGRUPOITEM,");
			sql.append(" TB.VLTOLERANCIAVERGRUSALDO,");
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
			sql.append(" TB.VLPCTDESCPEDIDO,");
		}
		sql.append(" TB.VLTOTALBASESTITEM,");
		sql.append(" TB.VLTOTALBASEICMSITEM,");
		sql.append(" TB.VLINDICEVOLUME,");
		sql.append(" TB.CDGRUPODESCCLI,");
		sql.append(" TB.CDGRUPODESCPROD,");
		sql.append(" TB.VLPCTTOTALMARGEMITEM,");
		if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			sql.append(" TB.CDPACOTE,");
		}
		if (LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento) {
			sql.append(" TB.VLINDICECONDICAOPAGTO,");
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(" TB.CDPOLITICACOMERCIAL,");
			sql.append(" TB.VLPCTPOLITICACOMERCIAL,");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			sql.append(" TB.VLBASEANTECIPACAO,");
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			sql.append(" TB.VLPONTUACAOBASEITEM,");
			sql.append(" TB.VLPONTUACAOREALIZADOITEM,");
			sql.append(" TB.VLPESOPONTUACAO,");
			sql.append(" TB.VLFATORCORRECAOFAIXAPRECO,");
			sql.append(" TB.VLFATORCORRECAOFAIXADIAS,");
			sql.append(" TB.VLPCTFAIXAPRECOPONTUACAO,");
			sql.append(" TB.VLBASEPONTUACAO,");
			sql.append(" TB.CDPONTUACAOCONFIG,");
			sql.append(" TB.QTDIASFAIXAPONTUACAO,");
		}
		if (LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(" TB.CDMOTIVOPENDENCIA,");
			sql.append(" TB.NUORDEMLIBERACAO,");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" TB.VLTOTALMAODEOBRA,");
			sql.append(" TB.VLTOTALIMPOSTOS,");
			sql.append(" TB.VLTOTALCUSTOCOMERCIAL,");
			sql.append(" TB.VLTOTALCUSTOFINANCEIRO,");
			sql.append(" TB.VLDESCPRODUTORESTRITO,");
			sql.append(" TB.VLTOTALDESCPRODUTORESTRITO,");
		}
        if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
        	sql.append(" TB.FLPROMOCIONAL,");
        }
		if (LavenderePdaConfig.usaGondolaPedido) {
			sql.append(" TB.QTITEMGONDOLA,");
		}
        if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" TB.CDMARGEMRENTAB,");
		}
        sql.append(" TB.FLTIPOALTERACAO,");
        if(LavenderePdaConfig.usaConfigMargemRentabilidade()) {
        	sql.append(" TB.VLBASEMARGEMRENTAB,");
        	sql.append(" TB.VLCUSTOMARGEMRENTAB,");
        	sql.append(" TB.VLPCTMARGEMRENTAB,");
        }
        if (LavenderePdaConfig.isExibeComboMenuInferior()) {
        	sql.append(" TB.CDCOMBO,");
        }
        if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
	        sql.append(" TB.CDDESCPROGRESSIVO,");
	        sql.append(" TB.VLPCTDESCPROG,");
        }
		if (LavenderePdaConfig.mostraColecaoNosDetalhes()) {
			sql.append(" ").append(DaoUtil.ALIAS_COLECAO).append(".DSCOLECAO,");
		}
		if (LavenderePdaConfig.mostraStatusColecaoNosDetalhes()) {
			sql.append(" ").append(DaoUtil.ALIAS_COLECAO_STATUS).append(".DSSTATUSCOLECAO,");
		}
		if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
			sql.append(" ").append(DaoUtil.ALIAS_PRODUTO).append(".DSDIMENSOES,");
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			sql.append(" TB.FLAUTORIZADO,");
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
				sql.append(" TB.FLAGRUPADORSIMILARIDADE,")
				.append(" TB.CDAGRUPADORSIMILARIDADE,");
			}
		}
		if (LavenderePdaConfig.usaDescQuantidadePeso()) {
			sql.append(" TB.VLPCTFAIXADESCQTDPESO,");
		}
		if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
			sql.append(" TB.CDVERBASALDOCLIENTE, ");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			sql.append(" TB.VLCOTACAOMOEDAPRODUTO,");
			sql.append(" TB.DSMOEDAVENDAPRODUTO,");
		}
		if (((ItemPedido) domainFilter).isBuscaFlKitProduto) {
			sql.append(" ").append(DaoUtil.ALIAS_PRODUTO).append(".FLKIT FLKITPRODUTO, ");
		}
		if (LavenderePdaConfig.isConfigStatusItemPedido()) {
			sql.append(" TB.CDSTATUSITEMPEDIDO,");
			sql.append(" ").append(DaoUtil.ALIAS_STATUSITEMPEDIDO).append(".DSSTATUSITEMPEDIDO, ");
		}
		if (LavenderePdaConfig.relDiferencasPedido && itemPedidoFilter.isOrigemErp()) {
			sql.append(" ").append(DaoUtil.ALIAS_ITEMPEDIDOERPDIF).append(".CDEMPRESA AS CDEMPRESADIF, ");
		}
        sql.append(" TB.DSPRODUTO,");
        sql.append(" TB.CDPRODUTOCLIENTE,");
        sql.append(" TB.CDUSUARIO");
	    sql.append(", TB.QTENCOMENDA");
	    sql.append(", TB.VLTOTALENCOMENDA");
	    sql.append(", TB.CDLOCALESTOQUE");
		if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
			sql.append(", TB.NUORDEMCOMPRACLIENTE");
			sql.append(", TB.NUSEQORDEMCOMPRACLIENTE");
		}
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		ItemPedido itemPedidoFilter = (ItemPedido) domainFilter; 
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.rowKey = rs.getString("rowkey");
        itemPedido.cdEmpresa = rs.getString("cdEmpresa");
        itemPedido.cdRepresentante = rs.getString("cdRepresentante");
        itemPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        itemPedido.nuPedido = rs.getString("nuPedido");
        itemPedido.cdProduto = rs.getString("cdProduto");
        itemPedido.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemPedido.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemPedido.cdItemGrade1 = rs.getString("cdItemGrade1");
        itemPedido.cdItemGrade2 = rs.getString("cdItemGrade2");
        itemPedido.cdItemGrade3 = rs.getString("cdItemGrade3");
        itemPedido.nuSeqItemPedido = rs.getInt("nuSeqItemPedido");
        itemPedido.cdTabelaPreco = rs.getString("cdTabelaPreco");
        itemPedido.setQtItemFisico(ValueUtil.round(rs.getDouble("qtItemFisico")));
        itemPedido.vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
        itemPedido.vlBaseItemTabelaPreco = ValueUtil.round(rs.getDouble("vlBaseItemTabelaPreco"));
        itemPedido.vlBaseCalculoDescPromocional = ValueUtil.round(rs.getDouble("vlBaseCalculoDescPromocional"));
        itemPedido.vlBaseItemPedido = ValueUtil.round(rs.getDouble("vlBaseItemPedido"));
        itemPedido.vlTotalItemPedido = ValueUtil.round(rs.getDouble("vlTotalItemPedido"));
        itemPedido.vlTotalItemPedidoOld = itemPedido.vlTotalItemPedido;
        itemPedido.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        itemPedido.vlPctAcrescimo = ValueUtil.round(rs.getDouble("vlPctAcrescimo"));
        itemPedido.vlTotalBrutoItemPedido = ValueUtil.round(rs.getDouble("vlTotalBrutoItemPedido"));
        itemPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemPedido.cdUsuario = rs.getString("cdUsuario");
	    itemPedido.vlUnidadePadrao = ValueUtil.round(rs.getDouble("vlUnidadePadrao"));
        if (needAddVlUnidadePadraoAndBaseEmbElementar()) {
        	itemPedido.vlBaseEmbalagemElementar = ValueUtil.round(rs.getDouble("vlBaseEmbalagemElementar"));
        }
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			itemPedido.cdLinha = rs.getString("cdLinha");
		}
		if (LavenderePdaConfig.usaConversaoUnidadesMedida) {
			itemPedido.qtItemFaturamento = ValueUtil.round(rs.getDouble("qtItemFaturamento"));
		}
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual) {
			itemPedido.vlBaseFlex = ValueUtil.round(rs.getDouble("vlBaseFlex"));
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() || LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.vlRentabilidade = ValueUtil.round(rs.getDouble("vlRentabilidade"));
			itemPedido.vlRentabilidadeSug = ValueUtil.round(rs.getDouble("vlRentabilidadeSug"));
			itemPedido.vlRentabilidadeOld = itemPedido.vlRentabilidade;
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo() || LavenderePdaConfig.aplicaComissaoEspecialProdutoPromocional) {
			if (!LavenderePdaConfig.isNuCasasDecimaisComissaoLigado) {
				itemPedido.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
				itemPedido.vlPctComissaoTotal = ValueUtil.round(rs.getDouble("vlPctComissaoTotal"));
			} else {
				itemPedido.vlPctComissao = ValueUtil.getDoubleValueTruncated(rs.getDouble("vlPctComissao"), LavenderePdaConfig.nuCasasDecimaisComissao);
				itemPedido.vlPctComissaoTotal = ValueUtil.getDoubleValueTruncated(rs.getDouble("vlPctComissaoTotal"), LavenderePdaConfig.nuCasasDecimaisComissao);
			}
			itemPedido.vlTotalComissao = ValueUtil.round(rs.getDouble("vlTotalComissao"));
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			itemPedido.vlVerbaItem = ValueUtil.round(rs.getDouble("vlVerbaItem"));
			itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
				itemPedido.vlVerbaItemPositivo = ValueUtil.round(rs.getDouble("vlVerbaItemPositivo"));
				itemPedido.vlVerbaItemPositivoOld = itemPedido.vlVerbaItemPositivo;
			}
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			itemPedido.vlItemPedidoFrete = ValueUtil.round(rs.getDouble("vlItemPedidoFrete"));
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			itemPedido.cdLoteProduto = rs.getString("cdLoteProduto");
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto) {
			itemPedido.flPrecoLiberadoSenha = rs.getString("flPrecoLiberadoSenha");
			itemPedido.qtItemMinAfterLibPreco = itemPedido.getQtItemFisico();
			itemPedido.vlItemMinAfterLibPreco = itemPedido.vlItemPedido;
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido) {
			itemPedido.flPrecoLiberadoSenha = rs.getString("flPrecoLiberadoSenha");
			itemPedido.cdUsuarioLiberacao = rs.getString("cdUsuarioLiberacao");
			itemPedido.qtItemAfterLibPreco = itemPedido.getQtItemFisico();
			itemPedido.vlItemAfterLibPreco = itemPedido.vlItemPedido;
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
			itemPedido.vlRetornoProduto = rs.getDouble("vlRetornoProduto");
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			itemPedido.vlSt = rs.getDouble("vlSt");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			itemPedido.vlReducaoOptanteSimples = rs.getDouble("vlReducaoOptanteSimples");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			itemPedido.qtPeso = rs.getDouble("qtPeso");
		}
		if (LavenderePdaConfig.usaMotivoTrocaPorItemPedido()) {
			itemPedido.cdMotivoTroca = rs.getString("cdMotivoTroca");
			itemPedido.dsObsMotivoTroca = rs.getString("dsObsMotivoTroca");
		}
		if (LavenderePdaConfig.usaObservacaoPorItemPedido) {
			itemPedido.dsObservacao = rs.getString("dsObservacao");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			itemPedido.qtPontosItem = rs.getInt("qtPontosItem");
		}
		if (LavenderePdaConfig.quantidadeMinimaCaixasPedido > 0) {
			itemPedido.flVendidoQtMinima = rs.getString("flVendidoQtMinima");
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			itemPedido.cdPrazoPagtoPreco = rs.getInt("cdPrazoPagtoPreco");
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar) {
			itemPedido.vlItemPedidoUnElementar = rs.getDouble("vlItemPedidoUnElementar");
			itemPedido.qtItemPedidoUnElementar = rs.getDouble("qtItemPedidoUnElementar");
		}
		if (LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.vlFrete = rs.getDouble("vlFrete");
		}
		if (LavenderePdaConfig.liberaComSenhaVendaProdutoBloqueado) {
			itemPedido.flMetaGrupoProdLiberadoSenha = rs.getString("flMetaGrupoProdLiberadoSenha");
		}
		if (LavenderePdaConfig.usaControleSaldoOportunidade) {
			itemPedido.vlOportunidadeOld = itemPedido.vlTotalItemPedido;
		}
		if (LavenderePdaConfig.usaSenhaVendaRelacionada) {
			itemPedido.flLiberadoVendaRelacionada = rs.getString("flLiberadoVendaRelacionada");
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			itemPedido.vlIpiItem = rs.getDouble("vlIpiItem");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			itemPedido.qtItemEstoquePositivo = rs.getDouble("qtItemEstoquePositivo");
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			itemPedido.vlDescontoCCP = rs.getDouble("vlDescontoCCP");
			itemPedido.vlPctDescontoCCP = rs.getDouble("vlPctDescontoCCP");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			itemPedido.dtInicioPromocao = rs.getDate("dtInicioPromocao");
		}
		if (LavenderePdaConfig.usaIndicacaoItemPedidoProdutoPromocional) {
			itemPedido.flPromocao = rs.getString("flPromocao");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
			itemPedido.nuPromocao = rs.getInt("nuPromocao");
			itemPedido.vlDescontoPromo = rs.getDouble("vlDescontoPromo");
			itemPedido.flIgnoraDescQtdPro = rs.getString("flIgnoraDescQtdPro");
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido() || LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			itemPedido.vlFinalPromo = rs.getDouble("vlFinalPromo");
			itemPedido.vlPctDescontoPromo = rs.getDouble("vlPctDescontoPromo");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			itemPedido.vlPctFaixaDescQtd = rs.getDouble("vlPctFaixaDescQtd");
			itemPedido.oldVlPctFaixaDescQtd = itemPedido.vlPctFaixaDescQtd;
			itemPedido.flIgnoraDescQtd = rs.getString("flIgnoraDescQtd");

			if (domainFilter != null && ((ItemPedido) domainFilter).isJoinDescQt) {
				int descqtitem = rs.getInt("DQ_DESCQTITEM");
				if (descqtitem > 0) {
					DescQuantidade descQuantidade = new DescQuantidade();
					descQuantidade.cdEmpresa = itemPedido.cdEmpresa;
					descQuantidade.cdRepresentante = itemPedido.cdRepresentante;
					descQuantidade.cdTabelaPreco = itemPedido.cdTabelaPreco;
					descQuantidade.cdProduto = itemPedido.cdProduto;
					descQuantidade.qtItem = descqtitem;
					descQuantidade.vlPctDesconto = rs.getDouble("DQ_VLPCTDESCONTO");
					descQuantidade.vlDesconto = rs.getDouble("DQ_VLDESCONTO");
					descQuantidade.cdDesconto = rs.getString("DQ_CDDESCONTO");
					itemPedido.descQuantidade = descQuantidade;
				}
			}
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			itemPedido.vlFecop = rs.getDouble("vlFecop");
			itemPedido.vlTotalFecopItem = rs.getDouble("vlTotalFecopItem");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() || LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido()) {
			itemPedido.cdSugestaoVenda = rs.getString("cdSugestaoVenda");
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			itemPedido.cdTributacaoConfig = rs.getString("cdTributacaoConfig");
			itemPedido.vlTotalStItem = rs.getDouble("vlTotalStItem");
			itemPedido.vlTotalIpiItem = rs.getDouble("vlTotalIpiItem");
			itemPedido.vlTotalIcmsItem = rs.getDouble("vlTotalIcmsItem");
			itemPedido.vlTotalPisItem = rs.getDouble("vlTotalPisItem");
			itemPedido.vlTotalCofinsItem = rs.getDouble("vlTotalCofinsItem");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			itemPedido.vlSeguroItemPedido = rs.getDouble("vlSeguroItemPedido");
		}
		itemPedido.vlIcms = rs.getDouble("vlIcms");
		itemPedido.vlPis = rs.getDouble("vlPis");
		itemPedido.vlCofins = rs.getDouble("vlCofins");
		itemPedido.vlDespesaAcessoria = rs.getDouble("vlDespesaAcessoria");
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1() || LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			itemPedido.vlDesconto = rs.getDouble("vlDesconto");
			itemPedido.vlPctDesconto2 = rs.getDouble("vlPctDesconto2");
			itemPedido.vlDesconto2 = rs.getDouble("vlDesconto2");
			itemPedido.vlPctDesconto3 = rs.getDouble("vlPctDesconto3");
			itemPedido.vlDesconto3 = rs.getDouble("vlDesconto3");
		}
		if (LavenderePdaConfig.usaDescontoExtra) {
			itemPedido.vlPctDesconto2 = rs.getDouble("vlPctDesconto2");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			itemPedido.vlPctDescontoCanal = rs.getDouble("vlPctDescontoCanal");
		}
		itemPedido.cdUnidade = rs.getString("cdUnidade");
		if (LavenderePdaConfig.liberaComSenhaVendaProdutoSemEstoque) {
			itemPedido.flEstoqueLiberado = rs.getString("flEstoqueLiberado");
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			itemPedido.cdVerbaGrupo = rs.getInt("cdVerbaGrupo");
		}
        itemPedido.setOldQtItemFisico(itemPedido.getQtItemFisicoOrg());
        itemPedido.oldQtEstoqueConsumido = itemPedido.getQtItemFisicoOrg();
        itemPedido.oldQtItemFisicoDescQtd = itemPedido.getQtItemFisicoOrg();
        itemPedido.oldQtItemFisicoDescPromocionalQtd = itemPedido.getQtItemFisicoOrg();
        itemPedido.oldQtItemFaturamento = itemPedido.qtItemFaturamento;
        itemPedido.oldQtPeso = itemPedido.qtPeso;
        if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaFreteManualPedido) {
			itemPedido.vlTotalItemPedidoFrete = ValueUtil.round(rs.getDouble("vlTotalItemPedidoFrete"));
		}
		if (LavenderePdaConfig.liberaSenhaQtdItemMaiorPedidoOriginal) {
			itemPedido.flQuantidadeLiberada = rs.getString("flQuantidadeLiberada");
		}
        if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
        	itemPedido.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        }
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			itemPedido.vlCreditoCondicao = rs.getDouble("vlCreditoCondicao");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			itemPedido.vlCreditoFrete = rs.getDouble("vlCreditoFrete");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo || LavenderePdaConfig.isUsaMotivoPendencia()) {
			itemPedido.flPendente = rs.getString("flPendente");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			itemPedido.vlPctDescProgressivoMix = rs.getDouble("vlPctDescProgressivoMix");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			itemPedido.vlVolumeItem = rs.getDouble("vlVolumeItem");
		}
		boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem = LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem();
		boolean usaDescontoPorVolumeVendaMensal = LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal();
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || usaDescontoPorVolumeVendaMensal || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				itemPedido.vlPctDescFrete = rs.getDouble("vlPctDescFrete");
			}
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || usaDescontoPorVolumeVendaMensal || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				itemPedido.vlPctDescCliente = rs.getDouble("vlPctDescCliente");
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				itemPedido.vlPctDescontoCondicao = rs.getDouble("vlPctDescontoCondicao");
			}
		}
		if (LavenderePdaConfig.isUsaDivisaoValorVerbaUsuarioEmpresa()) {
			itemPedido.vlPctVerbaRateio = rs.getDouble("vlPctVerbaRateio");
		}
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			itemPedido.vlPctMargemProduto = rs.getDouble("vlPctMargemProduto");
		}
		if (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco) {
			itemPedido.cdVerba = rs.getString("cdVerba");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			itemPedido.vlPctContratoCli = rs.getDouble("vlPctContratoCli");
			itemPedido.flDecisaoCalculo = rs.getString("flDecisaoCalculo");
		}
		if (LavenderePdaConfig.usaCalculoReversoNaST) {
			itemPedido.vlItemPedidoStReverso = rs.getDouble("vlItemPedidoStReverso");
			itemPedido.vlPctDescontoStReverso = rs.getDouble("vlPctDescontoStReverso");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			itemPedido.flRestrito = rs.getString("flRestrito");
		}
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
			itemPedido.flValorTabelaAlterado = rs.getString("flValorTabelaAlterado");
		}
		if (LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
			itemPedido.vlIndiceGrupoProd = rs.getDouble("vlIndiceGrupoProd");
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			itemPedido.flTipoCadastroItem = rs.getString("flTipoCadastroItem");
			itemPedido.qtdCreditoDesc = rs.getInt("qtdCreditoDesc");
			itemPedido.cdProdutoCreditoDesc = rs.getString("cdProdutoCreditoDesc");
		}
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			itemPedido.qtItemDesejado = rs.getDouble("qtItemDesejado");
		}
		if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			itemPedido.vlItemIpi = rs.getDouble("vlItemIpi");
			itemPedido.vlBaseItemIpi = rs.getDouble("vlBaseItemIpi");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.vlTotalMargemItem = rs.getDouble("vlTotalMargemItem");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			itemPedido.flSugVendaPerson = rs.getString("flSugVendaPerson");
		}
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
			itemPedido.qtEstoqueCliente = rs.getDouble("qtEstoqueCliente");
		}
		if (LavenderePdaConfig.calculaRentabilidadePedidoRetornado
				|| LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.vlTotalPrecoCusto = rs.getDouble("vlTotalPrecoCusto");
		}
		if (LavenderePdaConfig.calculaPrazoEntregaPorProduto) {
			itemPedido.nuDiasPrazoEntrega = rs.getInt("nuDiasPrazoEntrega");
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			itemPedido.nuConversaoUnidadePu = rs.getDouble("nuConversaoUnidadePu");
			itemPedido.flDivideMultiplicaPu = rs.getString("flDivideMultiplicaPu");
			itemPedido.vlIndiceFinanceiroPu = rs.getDouble("vlIndiceFinanceiroPu");
		}
		if (LavenderePdaConfig.isUsaKitProdutoFechado() || LavenderePdaConfig.isUsaKitTipo3()) {
			itemPedido.cdKit = rs.getString("cdKit");
		}
		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			itemPedido.vlPctAcrescimoCondicao = rs.getDouble("vlPctAcrescimoCondicao");
			itemPedido.vlPctAcrescCliente = rs.getDouble("vlPctAcrescCliente");
			itemPedido.vlPctDescAlcada = rs.getDouble("vlPctDescAlcada");
		}
		if (LavenderePdaConfig.usaInfoComplementarItemPedido() || LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.isConfigReservaEstoqueCorrente) {
			itemPedido.dtEntrega = rs.getDate("dtEntrega");
		}
		if (LavenderePdaConfig.usaInfoComplementarItemPedido() || LavenderePdaConfig.usaGradeProduto4()) {
			itemPedido.vlAltura = rs.getDouble("vlAltura");
			itemPedido.vlLargura = rs.getDouble("vlLargura");
			itemPedido.vlComprimento = rs.getDouble("vlComprimento");
			itemPedido.vlPosVinco1 = rs.getInt("vlPosVinco1");
			itemPedido.vlPosVinco2 = rs.getInt("vlPosVinco2");
			itemPedido.vlPosVinco3 = rs.getInt("vlPosVinco3");
			itemPedido.vlPosVinco4 = rs.getInt("vlPosVinco4");
			itemPedido.vlPosVinco5 = rs.getInt("vlPosVinco5");
			itemPedido.vlPosVinco6 = rs.getInt("vlPosVinco6");
			itemPedido.vlPosVinco7 = rs.getInt("vlPosVinco7");
			itemPedido.vlPosVinco8 = rs.getInt("vlPosVinco8");
			itemPedido.vlPosVinco9 = rs.getInt("vlPosVinco9");
			itemPedido.vlPosVinco10 = rs.getInt("vlPosVinco10");
			itemPedido.cdCultura = rs.getString("cdCultura");
			itemPedido.cdPraga = rs.getString("cdPraga");
			itemPedido.vlDose = rs.getString("vlDose");
		}
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			itemPedido.vlPctAcrescimoIcms = rs.getDouble("vlPctAcrescimoIcms");
			itemPedido.vlPctDescontoIcms = rs.getDouble("vlPctDescontoIcms");
		}
		if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			itemPedido.cdLocal = rs.getString("cdLocal");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			itemPedido.flErroRecalculo = rs.getString("flErroRecalculo");
		}
		if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
			itemPedido.flOrigemEscolhaItemPedido = rs.getString("flOrigemEscolhaItemPedido");
			itemPedido.cdProdutoOrigem = rs.getString("cdProdutoOrigem");
			itemPedido.vlItemPedidoOrigem = rs.getDouble("vlItemPedidoOrigem");
			itemPedido.dtInclusaoItemPedido = rs.getDate("dtInclusaoItemPedido");
			itemPedido.hrInclusaoItemPedido = rs.getString("hrInclusaoItemPedido");
		}
		if (LavenderePdaConfig.usaIndiceClienteGrupoProd) {
			itemPedido.vlIndiceClienteGrupoProd = rs.getDouble("vlIndiceClienteGrupoProd");
		}
		if (LavenderePdaConfig.usaProdutoRestrito) {
			itemPedido.vlPctDescProdutoRestrito = rs.getDouble("vlPctDescProdutoRestrito");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			itemPedido.vlPctDescontoAuto = rs.getDouble("vlPctDescontoAuto");
			itemPedido.vlPctDescontoEfetivo = rs.getDouble("vlPctDescontoEfetivo");
			itemPedido.vlPctDescontoAutoEfetivo = rs.getDouble("vlPctDescontoAutoEfetivo");
			itemPedido.vlDescontoAuto = rs.getDouble("vlDescontoAuto");
			itemPedido.vlTotalDescontoAuto = rs.getDouble("vlTotalDescontoAuto");
			itemPedido.vlPrecoEfetivoUnitario = rs.getDouble("vlPrecoEfetivoUnitario");
			itemPedido.vlPrecoEfetivoUnitarioDesc = rs.getDouble("vlPrecoEfetivoUnitarioDesc");
			itemPedido.vlEfetivoTotalItem = rs.getDouble("vlEfetivoTotalItem");
			itemPedido.vlEfetivoTotalItemDesc = rs.getDouble("vlEfetivoTotalItemDesc");
			itemPedido.vlDescontoTotalAutoDesc = rs.getDouble("vlDescontoTotalAutoDesc");
			itemPedido.vlBaseInterpolacaoProduto = rs.getDouble("vlBaseInterpolacaoProduto");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			itemPedido.cdComissaoPedidoRep = rs.getInt("cdComissaoPedidoRep");
		}
		if (LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueComDetalhes()) {
			itemPedido.qtEstoquePrevisto = rs.getDouble("qtEstoquePrevisto");
			itemPedido.dtEstoquePrevisto = rs.getDate("dtEstoquePrevisto");
		}
		itemPedido.vlTotalBaseStItem = rs.getDouble("vlTotalBaseStItem");
		itemPedido.vlTotalBaseIcmsItem = rs.getDouble("vlTotalBaseIcmsItem");
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			itemPedido.vlVpc = rs.getDouble("vlVpc");
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			itemPedido.vlItemFreteTributacao = rs.getDouble("vlItemFreteTributacao");
			itemPedido.vlTotalItemFreteTributacao = rs.getDouble("vlTotalItemFreteTributacao");
		}
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			itemPedido.vlPctDescCondPagto = rs.getDouble("vlPctDescCondPagto");
		}
		if (LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto()) {
			itemPedido.vlVerbaGrupoItem = rs.getDouble("vlVerbaGrupoItem");
			itemPedido.vlVerbaGrupoOld = itemPedido.vlVerbaGrupoItem;
			itemPedido.vlToleranciaVerGruSaldo = rs.getDouble("vlToleranciaVerGruSaldo");
			itemPedido.vlToleranciaVerGruSaldoOld = itemPedido.vlToleranciaVerGruSaldo;
			
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
			itemPedido.vlPctDescPedido = rs.getDouble("vlPctDescPedido");
		}
		itemPedido.vlDescontoCondicao = rs.getDouble("vlDescontoCondicao");
		itemPedido.cdGrupoDescCli = rs.getString("cdGrupoDescCli");
		itemPedido.cdGrupoDescProd = rs.getString("cdGrupoDescProd");
		if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			itemPedido.cdPacote = rs.getString("cdPacote");
		}
		if (LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento) {
			itemPedido.vlIndiceCondicaoPagto = rs.getDouble("vlIndiceCondicaoPagto");
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			itemPedido.cdPoliticaComercial = rs.getString("cdPoliticaComercial");
			itemPedido.vlPctPoliticaComercial = rs.getDouble("vlPctPoliticaComercial");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			itemPedido.vlBaseAntecipacao = rs.getDouble("vlBaseAntecipacao");
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			itemPedido.vlPontuacaoBaseItem = rs.getDouble("vlPontuacaoBaseItem");
			itemPedido.vlPontuacaoRealizadoItem = rs.getDouble("vlPontuacaoRealizadoItem");
			itemPedido.vlPesoPontuacao = rs.getDouble("vlPesoPontuacao");
			itemPedido.vlFatorCorrecaoFaixaPreco = rs.getDouble("vlFatorCorrecaoFaixaPreco");
			itemPedido.vlFatorCorrecaoFaixaDias = rs.getDouble("vlFatorCorrecaoFaixaDias");
			itemPedido.vlPctFaixaPrecoPontuacao = rs.getDouble("vlPctFaixaPrecoPontuacao");
			itemPedido.vlBasePontuacao = rs.getDouble("vlBasePontuacao");
			itemPedido.cdPontuacaoConfig = rs.getString("cdPontuacaoConfig");
			itemPedido.qtDiasFaixaPontuacao = rs.getInt("qtDiasFaixaPontuacao");
		}
		if (LavenderePdaConfig.isUsaMotivoPendencia()) {
			itemPedido.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
			itemPedido.nuOrdemLiberacao = rs.getInt("nuOrdemLiberacao");
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.cdMargemRentab = rs.getString("cdMargemRentab");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			itemPedido.vlTotalMaoDeObra = rs.getDouble("vlTotalMaoDeObra");
			itemPedido.vlTotalImpostos = rs.getDouble("vlTotalImpostos");
			itemPedido.vlTotalCustoComercial = rs.getDouble("vlTotalCustoComercial");
			itemPedido.vlTotalCustoFinanceiro = rs.getDouble("vlTotalCustoFinanceiro");
			itemPedido.vlDescProdutoRestrito = rs.getDouble("vlDescProdutoRestrito");
			itemPedido.vlTotalDescProdutoRestrito = rs.getDouble("vlTotalDescProdutoRestrito");
		}
		itemPedido.vlIndiceVolume = rs.getDouble("vlIndiceVolume");
		itemPedido.vlPctTotalMargemItem = rs.getDouble("vlPctTotalMargemItem");
		if(LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
			itemPedido.flPromocional = rs.getString("flPromocional");
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			itemPedido.qtItemGondola = rs.getInt("qtItemGondola");
		}
        if(LavenderePdaConfig.usaConfigMargemRentabilidade()) {
        	itemPedido.vlBaseMargemRentab = rs.getDouble("vlBaseMargemRentab");
        	itemPedido.vlCustoMargemRentab = rs.getDouble("vlCustoMargemRentab");
        	itemPedido.vlPctMargemRentab = rs.getDouble("vlPctMargemRentab");
        }
        if (LavenderePdaConfig.isExibeComboMenuInferior()) {
        	itemPedido.cdCombo = rs.getString("cdCombo");
        }
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
	    	itemPedido.cdDescProgressivo = rs.getString("cdDescProgressivo");
		    itemPedido.vlPctDescProg = rs.getDouble("vlPctDescProg");
		    itemPedido.auxiliarVariaveis.oldCdDescProgressivo = itemPedido.cdDescProgressivo;
		    itemPedido.auxiliarVariaveis.oldVlPctDescProg = itemPedido.vlPctDescProg;
	    }
		if (LavenderePdaConfig.mostraColecaoNosDetalhes()) {
			itemPedido.dsColecao = rs.getString("dsColecao");
		}
		if (LavenderePdaConfig.mostraStatusColecaoNosDetalhes()) {
			itemPedido.dsStatusColecao = rs.getString("dsStatusColecao");
		}
		if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
			itemPedido.dsDimensoes = rs.getString("dsDimensoes");
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			itemPedido.flAutorizado = rs.getString("flAutorizado");
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
				itemPedido.flAgrupadorSimilaridade = rs.getString("flAgrupadorSimilaridade");
				itemPedido.cdAgrupadorSimilaridade = rs.getString("cdAgrupadorSimilaridade");
			}
		}
		if (LavenderePdaConfig.usaDescQuantidadePeso()) {
			itemPedido.vlPctFaixaDescQtdPeso = rs.getDouble("VLPCTFAIXADESCQTDPESO");
		}
		if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
			itemPedido.cdVerbaSaldoCliente = rs.getString("cdVerbaSaldoCliente");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			itemPedido.vlCotacaoMoedaProduto = rs.getDouble("VLCOTACAOMOEDAPRODUTO");
			itemPedido.dsMoedaVendaProduto = rs.getString("DSMOEDAVENDAPRODUTO");
		}
		if (domainFilter != null && ((ItemPedido) domainFilter).isBuscaFlKitProduto) {
			itemPedido.flProdutoKitFromBusca = rs.getString("FLKITPRODUTO");
		}
		if (LavenderePdaConfig.isConfigStatusItemPedido()) {
			itemPedido.cdStatusItemPedido = rs.getString("CDSTATUSITEMPEDIDO");
			itemPedido.dsStatusItemPedido = rs.getString("DSSTATUSITEMPEDIDO");
		}
	    if (LavenderePdaConfig.relDiferencasPedido && itemPedidoFilter != null && itemPedidoFilter.isOrigemErp()) {
			String cdEmpresaDif = rs.getString("CDEMPRESADIF");
		    itemPedido.possuiDiferenca = cdEmpresaDif != null;
	    }
	    itemPedido.dsProduto = rs.getString("dsProduto");
	    itemPedido.cdProdutoCliente = rs.getString("cdProdutoCliente");
	    itemPedido.qtEncomenda = rs.getDouble("QTENCOMENDA");
	    itemPedido.vlTotalEncomenda = rs.getDouble("VLTOTALENCOMENDA");
	    itemPedido.cdLocalEstoque = rs.getString("CDLOCALESTOQUE");
		if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
			itemPedido.nuOrdemCompraCliente = rs.getString("NUORDEMCOMPRACLIENTE");
			itemPedido.nuSeqOrdemCompraCliente = rs.getInt("NUSEQORDEMCOMPRACLIENTE");
		}
	    return itemPedido;
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" NUSEQPRODUTO, ");
        sql.append(" VLTOTALITEMPEDIDO, ");
        sql.append(" CDUNIDADE, ");
        sql.append(" QTITEMFISICO");
        if (LavenderePdaConfig.usaCriacaoPedidoErpCancelado) {
        	sql.append(", VLVERBAITEM");
        }
	}

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.rowKey = rs.getString("rowkey");
        itemPedido.cdEmpresa = rs.getString("cdEmpresa");
        itemPedido.cdRepresentante = rs.getString("cdRepresentante");
        itemPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        itemPedido.nuPedido = rs.getString("nuPedido");
        itemPedido.cdProduto = rs.getString("cdProduto");
        itemPedido.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemPedido.cdItemGrade1 = rs.getString("cdItemGrade1");
        itemPedido.cdItemGrade2 = rs.getString("cdItemGrade2");
        itemPedido.cdItemGrade3 = rs.getString("cdItemGrade3");
        itemPedido.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemPedido.vlTotalItemPedido = rs.getDouble("vlTotalItemPedido");
        itemPedido.cdUnidade = rs.getString("cdUnidade");
        itemPedido.setQtItemFisico(rs.getDouble("qtItemFisico"));
        if (LavenderePdaConfig.usaCriacaoPedidoErpCancelado) {
        	itemPedido.vlVerbaItem = rs.getDouble("vlVerbaItem");
        	itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
        }
        return itemPedido;
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" NUSEQITEMPEDIDO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" VLITEMPEDIDO,");
        sql.append(" VLBASEITEMTABELAPRECO,");
        sql.append(" VLBASECALCULODESCPROMOCIONAL,");
        sql.append(" VLBASEITEMPEDIDO,");
		sql.append(" VLTOTALITEMPEDIDO,");
		sql.append(" VLPCTDESCONTO,");
		sql.append(" VLPCTACRESCIMO,");
		sql.append(" VLTOTALBRUTOITEMPEDIDO,");
		sql.append(" VLUNIDADEPADRAO,");
		sql.append(" VLBASEEMBALAGEMELEMENTAR,");
		sql.append(" CDLINHA,");
		sql.append(" QTITEMFATURAMENTO,");
		sql.append(" VLBASEFLEX,");
		sql.append(" VLRENTABILIDADE,");
		sql.append(" VLRENTABILIDADESUG,");
		sql.append(" VLTOTALCOMISSAO,");
		sql.append(" VLPCTCOMISSAO,");
	    sql.append(" VLPCTCOMISSAOTOTAL,");
		sql.append(" VLVERBAITEM,");
		sql.append(" VLVERBAITEMPOSITIVO,");
		sql.append(" CDCONTACORRENTE,");
		sql.append(" VLRETORNOPRODUTO,");
		sql.append(" VLITEMPEDIDOFRETE,");
		sql.append(" CDLOTEPRODUTO,");
		sql.append(" FLPRECOLIBERADOSENHA,");
		sql.append(" QTPESO,");
		sql.append(" VLST,");
		sql.append(" VLREDUCAOOPTANTESIMPLES,");
		sql.append(" CDMOTIVOTROCA,");
		sql.append(" DSOBSMOTIVOTROCA,");
		sql.append(" DSOBSERVACAO,");
		sql.append(" QTPONTOSITEM,");
		sql.append(" CDUNIDADE,");
		sql.append(" FLVENDIDOQTMINIMA,");
		sql.append(" CDPRAZOPAGTOPRECO,");
		sql.append(" VLITEMPEDIDOUNELEMENTAR,");
		sql.append(" QTITEMPEDIDOUNELEMENTAR,");
		sql.append(" VLFRETE,");
		sql.append(" FLMETAGRUPOPRODLIBERADOSENHA,");
		sql.append(" FLLIBERADOVENDARELACIONADA,");
		sql.append(" VLIPIITEM,");
		sql.append(" QTITEMESTOQUEPOSITIVO,");
		sql.append(" VLDESCONTOCCP,");
		sql.append(" VLPCTDESCONTOCCP,");
		sql.append(" NUPROMOCAO,");
		sql.append(" DTINICIOPROMOCAO,");
		sql.append(" VLFINALPROMO,");
		sql.append(" VLDESCONTOPROMO,");
		sql.append(" VLPCTDESCONTOPROMO,");
		sql.append(" VLPCTFAIXADESCQTD,");
		sql.append(" VLFECOP,");
		sql.append(" VLTOTALFECOPITEM,");
		sql.append(" VLICMS,");
		sql.append(" VLPIS,");
		sql.append(" VLCOFINS,");
		sql.append(" VLDESPESAACESSORIA,");
		sql.append(" CDSUGESTAOVENDA,");
		sql.append(" CDTRIBUTACAOCONFIG,");
		sql.append(" VLTOTALSTITEM,");
		sql.append(" VLTOTALIPIITEM,");
		sql.append(" VLTOTALICMSITEM,");
		sql.append(" VLTOTALPISITEM,");
		sql.append(" VLTOTALCOFINSITEM,");
		sql.append(" VLSEGUROITEMPEDIDO,");
		sql.append(" VLDESCONTO,");
		sql.append(" VLPCTDESCONTO2,");
		sql.append(" VLDESCONTO2,");
		sql.append(" VLPCTDESCONTO3,");
		sql.append(" VLDESCONTO3,");
		sql.append(" VLTOTALITEMPEDIDOFRETE,");
		sql.append(" VLPCTDESCONTOCANAL,");
		sql.append(" CDVERBAGRUPO,");
		sql.append(" CDUSUARIOLIBERACAO,");
		sql.append(" FLESTOQUELIBERADO,");
		sql.append(" FLQUANTIDADELIBERADA,");
		sql.append(" CDCONDICAOCOMERCIAL,");
		sql.append(" VLCREDITOCONDICAO,");
		sql.append(" VLCREDITOFRETE,");
		sql.append(" FLPENDENTE,");
	    sql.append(" VLPCTDESCPROGRESSIVOMIX,");
	    sql.append(" VLVOLUMEITEM,");
	    sql.append(" VLPCTDESCFRETE,");
	    sql.append(" VLPCTDESCCLIENTE,");
	    sql.append(" VLPCTDESCONTOCONDICAO,");
	    sql.append(" VLPCTVERBARATEIO,");
	    sql.append(" VLPCTMARGEMPRODUTO,");
	    sql.append(" CDVERBA,");
	    sql.append(" VLPCTCONTRATOCLI,");
	    sql.append(" FLDECISAOCALCULO,");
	    sql.append(" VLITEMPEDIDOSTREVERSO,");
		sql.append(" VLPCTDESCONTOSTREVERSO,");
		sql.append(" FLRESTRITO,");
		sql.append(" VLINDICEGRUPOPROD,");
		sql.append(" VLITEMIPI,");
		sql.append(" VLBASEITEMIPI,");
		sql.append(" FLVALORTABELAALTERADO,");
		sql.append(" FLPROMOCAO,");
		sql.append(" FLTIPOCADASTROITEM,");
		sql.append(" QTDCREDITODESC,");
		sql.append(" CDPRODUTOCREDITODESC,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" QTITEMDESEJADO,");
		sql.append(" VLTOTALMARGEMITEM,");
		sql.append(" FLSUGVENDAPERSON,");
		sql.append(" QTESTOQUECLIENTE,");
		sql.append(" VLTOTALPRECOCUSTO,");
		sql.append(" VLTOTALBASESTITEM,");
		sql.append(" VLTOTALBASEICMSITEM,");
		sql.append(" NUDIASPRAZOENTREGA,");
		sql.append(" NUCONVERSAOUNIDADEPU,");
		sql.append(" FLDIVIDEMULTIPLICAPU,");
		sql.append(" VLINDICEFINANCEIROPU,");
		sql.append(" CDKIT,");
		sql.append(" VLPCTMARGEMAGREGADA,");
		sql.append(" VLPCTACRESCIMOCONDICAO,");
		sql.append(" VLPCTACRESCCLIENTE,");
		sql.append(" VLPCTDESCALCADA,");
		sql.append(" VLPCTACRESCIMOICMS,");
		sql.append(" VLPCTDESCONTOICMS,");
		sql.append(" CDLOCAL,");
		sql.append(" DTENTREGA,");
		sql.append(" VLALTURA,");
		sql.append(" VLLARGURA,");
		sql.append(" VLCOMPRIMENTO,");
		sql.append(" VLPOSVINCO1,");
		sql.append(" VLPOSVINCO2,");
		sql.append(" VLPOSVINCO3,");
		sql.append(" VLPOSVINCO4,");
		sql.append(" VLPOSVINCO5,");
		sql.append(" VLPOSVINCO6,");
		sql.append(" VLPOSVINCO7,");
		sql.append(" VLPOSVINCO8,");
		sql.append(" VLPOSVINCO9,");
		sql.append(" VLPOSVINCO10,");
		sql.append(" FLORIGEMESCOLHAITEMPEDIDO,");
		sql.append(" CDPRODUTOORIGEM,");
		sql.append(" VLITEMPEDIDOORIGEM,");
		sql.append(" VLVPC,");
		sql.append(" DTINCLUSAOITEMPEDIDO,");
		sql.append(" HRINCLUSAOITEMPEDIDO,");
		sql.append(" VLINDICEVOLUME,");
		sql.append(" VLINDICECLIENTEGRUPOPROD,");
		sql.append(" VLPCTDESCPRODUTORESTRITO,")
		.append(" VLPCTDESCONTOAUTO,")
		.append(" VLPCTDESCONTOEFETIVO,")
		.append(" VLPCTDESCONTOAUTOEFETIVO,")
		.append(" VLDESCONTOAUTO,")
		.append(" VLTOTALDESCONTOAUTO,")
		.append(" VLPRECOEFETIVOUNITARIO,")
		.append(" VLPRECOEFETIVOUNITARIODESC,")
		.append(" VLEFETIVOTOTALITEM,")
		.append(" VLEFETIVOTOTALITEMDESC,")
		.append(" VLDESCONTOTOTALAUTODESC,")
		.append(" VLBASEINTERPOLACAOPRODUTO,")
		.append(" CDGRUPODESCPROD,")
		.append(" CDGRUPODESCCLI,")
		.append(" CDCOMISSAOPEDIDOREP,")
		.append(" VLDESCONTOCONDICAO,")
		.append(" QTESTOQUEPREVISTO,")
		.append(" DTESTOQUEPREVISTO,")
		.append(" VLITEMFRETETRIBUTACAO,")
		.append(" VLTOTALITEMFRETETRIBUTACAO,")
		.append(" VLPCTDESCPEDIDO,")
		.append(" VLPCTTOTALMARGEMITEM,")
		.append(" VLPCTDESCCONDPAGTO,")
		.append(" VLVERBAGRUPOITEM,")
		.append(" VLTOLERANCIAVERGRUSALDO,")
		.append(" CDPACOTE,")
		.append(" VLINDICECONDICAOPAGTO,")
		.append(" CDMOTIVOPENDENCIA,")
		.append(" NUORDEMLIBERACAO,")
		.append(" VLBASEANTECIPACAO,")
		.append(" VLPONTUACAOBASEITEM,")
		.append(" VLPONTUACAOREALIZADOITEM,")
		.append(" VLPESOPONTUACAO,")
		.append(" VLFATORCORRECAOFAIXAPRECO,")
		.append(" VLFATORCORRECAOFAIXADIAS,")
		.append(" VLPCTFAIXAPRECOPONTUACAO,")
		.append(" VLBASEPONTUACAO,")
		.append(" CDPONTUACAOCONFIG,")
		.append(" QTDIASFAIXAPONTUACAO,")
		.append(" CDPOLITICACOMERCIAL,")
		.append(" VLPCTPOLITICACOMERCIAL,")
		.append(" VLTOTALMAODEOBRA,")
		.append(" VLTOTALIMPOSTOS,")
		.append(" VLTOTALCUSTOCOMERCIAL,")
		.append(" VLTOTALCUSTOFINANCEIRO,")
		.append(" CDCULTURA,")
		.append(" CDPRAGA,")
		.append(" VLDOSE,")
		.append(" FLPROMOCIONAL,")
		.append(" CDMARGEMRENTAB,")
		.append(" VLBASEMARGEMRENTAB,")
		.append(" VLCUSTOMARGEMRENTAB,")
		.append(" VLPCTMARGEMRENTAB,")
		.append(" QTITEMGONDOLA,")
		.append(" VLDESCPRODUTORESTRITO,")
		.append(" VLTOTALDESCPRODUTORESTRITO,")
		.append(" CDCOMBO,")
		.append(" VLINDICEPLATAFORMAVENDACLIFIN,")
		.append(" CDDESCPROGRESSIVO,")
		.append(" VLPCTDESCPROG,")
		.append(" FLIGNORADESCQTD,")
		.append(" FLIGNORADESCQTDPRO,")
		.append(" DSPRODUTO,")
		.append(" FLAUTORIZADO,")
		.append(" NUCONVERSAOUNIDADENEGOCIADO,")
		.append(" VLPCTFAIXADESCQTDPESO,")
		.append(" CDAGRUPADORSIMILARIDADE,")
		.append(" CDVERBASALDOCLIENTE,")
		.append(" VLCOTACAOMOEDAPRODUTO,")
		.append(" DSMOEDAVENDAPRODUTO,")
		.append(" CDLOCALESTOQUE,")
		.append(" NUORDEMCOMPRACLIENTE,")
		.append(" NUSEQORDEMCOMPRACLIENTE,")
		.append(" INFOSPERSONALIZADAS,")
		.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        sql.append(Sql.getValue(itemPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(itemPedido.cdProduto)).append(",");
        sql.append(Sql.getValue(itemPedido.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(itemPedido.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemPedido.cdItemGrade1)).append(",");
        sql.append(Sql.getValue(itemPedido.cdItemGrade2)).append(",");
        sql.append(Sql.getValue(itemPedido.cdItemGrade3)).append(",");
        sql.append(Sql.getValue(itemPedido.nuSeqItemPedido)).append(",");
        sql.append(Sql.getValue(itemPedido.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(itemPedido.getQtItemFisicoOrg())).append(",");
        sql.append(Sql.getValue(itemPedido.vlItemPedido)).append(",");
        sql.append(Sql.getValue(itemPedido.vlBaseItemTabelaPreco)).append(",");
        sql.append(Sql.getValue(itemPedido.vlBaseCalculoDescPromocional)).append(",");
        sql.append(Sql.getValue(itemPedido.vlBaseItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDesconto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctAcrescimo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalBrutoItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedido.vlUnidadePadrao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBaseEmbalagemElementar)).append(",");
		sql.append(Sql.getValue(itemPedido.cdLinha)).append(",");
		sql.append(Sql.getValue(itemPedido.qtItemFaturamento)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBaseFlex)).append(",");
		sql.append(Sql.getValue(itemPedido.vlRentabilidade)).append(",");
		sql.append(Sql.getValue(itemPedido.vlRentabilidadeSug)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalComissao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctComissao)).append(",");
	    sql.append(Sql.getValue(itemPedido.vlPctComissaoTotal)).append(",");
		sql.append(Sql.getValue(itemPedido.vlVerbaItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlVerbaItemPositivo)).append(",");
		sql.append(Sql.getValue(itemPedido.cdContaCorrente)).append(",");
		sql.append(Sql.getValue(itemPedido.vlRetornoProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlItemPedidoFrete)).append(",");
		sql.append(Sql.getValue(itemPedido.cdLoteProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.flPrecoLiberadoSenha)).append(",");
		sql.append(Sql.getValue(itemPedido.qtPeso)).append(",");
		sql.append(Sql.getValue(itemPedido.vlSt)).append(",");
		sql.append(Sql.getValue(itemPedido.vlReducaoOptanteSimples)).append(",");
		sql.append(Sql.getValue(itemPedido.cdMotivoTroca)).append(",");
		sql.append(Sql.getValue(itemPedido.dsObsMotivoTroca)).append(",");
		sql.append(Sql.getValue(itemPedido.dsObservacao)).append(",");
		sql.append(Sql.getValue(itemPedido.qtPontosItem)).append(",");
		sql.append(Sql.getValue(itemPedido.cdUnidade)).append(",");
		sql.append(Sql.getValue(itemPedido.flVendidoQtMinima)).append(",");
		sql.append(Sql.getValue(itemPedido.cdPrazoPagtoPreco)).append(",");
		sql.append(Sql.getValue(itemPedido.vlItemPedidoUnElementar)).append(",");
		sql.append(Sql.getValue(itemPedido.qtItemPedidoUnElementar)).append(",");
		sql.append(Sql.getValue(itemPedido.vlFrete)).append(",");
		sql.append(Sql.getValue(itemPedido.flMetaGrupoProdLiberadoSenha)).append(",");
		sql.append(Sql.getValue(itemPedido.flLiberadoVendaRelacionada)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIpiItem)).append(",");
		sql.append(Sql.getValue(itemPedido.qtItemEstoquePositivo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDescontoCCP)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoCCP)).append(",");
		sql.append(Sql.getValue(itemPedido.nuPromocao)).append(",");
		sql.append(Sql.getValue(itemPedido.dtInicioPromocao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlFinalPromo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDescontoPromo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoPromo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctFaixaDescQtd)).append(",");
		sql.append(Sql.getValue(itemPedido.vlFecop)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalFecopItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIcms)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPis)).append(",");
		sql.append(Sql.getValue(itemPedido.vlCofins)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDespesaAcessoria)).append(",");
		sql.append(Sql.getValue(itemPedido.cdSugestaoVenda)).append(",");
		sql.append(Sql.getValue(itemPedido.cdTributacaoConfig)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalStItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalIpiItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalIcmsItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalPisItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalCofinsItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlSeguroItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDesconto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDesconto2)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDesconto2)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDesconto3)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDesconto3)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalItemPedidoFrete)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoCanal)).append(",");
		sql.append(Sql.getValue(itemPedido.cdVerbaGrupo)).append(",");
		sql.append(Sql.getValue(itemPedido.cdUsuarioLiberacao)).append(",");
		sql.append(Sql.getValue(itemPedido.flEstoqueLiberado)).append(",");
		sql.append(Sql.getValue(itemPedido.flQuantidadeLiberada)).append(",");
		sql.append(Sql.getValue(itemPedido.cdCondicaoComercial)).append(",");
		sql.append(Sql.getValue(itemPedido.vlCreditoCondicao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlCreditoFrete)).append(",");
		sql.append(Sql.getValue(itemPedido.flPendente)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescProgressivoMix)).append(",");
		sql.append(Sql.getValue(itemPedido.vlVolumeItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescFrete)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescCliente)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoCondicao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctVerbaRateio)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctMargemProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.cdVerba)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctContratoCli)).append(",");
		sql.append(Sql.getValue(itemPedido.flDecisaoCalculo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlItemPedidoStReverso)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoStReverso)).append(",");
		sql.append(Sql.getValue(itemPedido.flRestrito)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIndiceGrupoProd)).append(",");
		sql.append(Sql.getValue(itemPedido.vlItemIpi)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBaseItemIpi)).append(",");
		sql.append(Sql.getValue(itemPedido.flValorTabelaAlterado)).append(",");
		sql.append(Sql.getValue(itemPedido.flPromocao)).append(",");
		sql.append(Sql.getValue(itemPedido.flTipoCadastroItem)).append(",");
		sql.append(Sql.getValue(itemPedido.qtdCreditoDesc)).append(",");
		sql.append(Sql.getValue(itemPedido.cdProdutoCreditoDesc)).append(",");
		sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
		sql.append(Sql.getValue(itemPedido.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(itemPedido.qtItemDesejado)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalMargemItem)).append(",");
		sql.append(Sql.getValue(itemPedido.flSugVendaPerson)).append(",");
		sql.append(Sql.getValue(itemPedido.qtEstoqueCliente)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalPrecoCusto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalBaseStItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalBaseIcmsItem)).append(",");
		sql.append(Sql.getValue(itemPedido.nuDiasPrazoEntrega)).append(",");
		sql.append(Sql.getValue(itemPedido.nuConversaoUnidadePu)).append(",");
		sql.append(Sql.getValue(itemPedido.flDivideMultiplicaPu)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIndiceFinanceiroPu)).append(",");
		sql.append(Sql.getValue(itemPedido.cdKit)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctMargemAgregada)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctAcrescimoCondicao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctAcrescCliente)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescAlcada)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctAcrescimoIcms)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoIcms)).append(",");
		sql.append(Sql.getValue(itemPedido.cdLocal)).append(",");
		sql.append(Sql.getValue(itemPedido.dtEntrega)).append(",");
		sql.append(Sql.getValue(itemPedido.vlAltura)).append(",");
		sql.append(Sql.getValue(itemPedido.vlLargura)).append(",");
		sql.append(Sql.getValue(itemPedido.vlComprimento)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco1)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco2)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco3)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco4)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco5)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco6)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco7)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco8)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco9)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPosVinco10)).append(",");
		sql.append(Sql.getValue(itemPedido.flOrigemEscolhaItemPedido)).append(",");
		sql.append(Sql.getValue(itemPedido.cdProdutoOrigem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlItemPedidoOrigem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlVpc)).append(",");
		sql.append(Sql.getValue(DateUtil.getCurrentDate())).append(",");
		sql.append(Sql.getValue(TimeUtil.getCurrentTimeHHMMSS())).append(",");
		sql.append(Sql.getValue(itemPedido.vlIndiceVolume)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIndiceClienteGrupoProd)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescProdutoRestrito)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoAuto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoEfetivo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescontoAutoEfetivo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDescontoAuto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalDescontoAuto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPrecoEfetivoUnitario)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPrecoEfetivoUnitarioDesc)).append(",");
		sql.append(Sql.getValue(itemPedido.vlEfetivoTotalItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlEfetivoTotalItemDesc)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDescontoTotalAutoDesc)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBaseInterpolacaoProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.cdGrupoDescProd)).append(",");
		sql.append(Sql.getValue(itemPedido.cdGrupoDescCli)).append(",");
		sql.append(Sql.getValue(itemPedido.cdComissaoPedidoRep)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDescontoCondicao)).append(",");
		sql.append(Sql.getValue(itemPedido.qtEstoquePrevisto)).append(",");
		sql.append(Sql.getValue(itemPedido.dtEstoquePrevisto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlItemFreteTributacao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalItemFreteTributacao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescPedido)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctTotalMargemItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctDescCondPagto)).append(",");
		sql.append(Sql.getValue(itemPedido.vlVerbaGrupoItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlToleranciaVerGruSaldo)).append(",");
		sql.append(Sql.getValue(itemPedido.cdPacote)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIndiceCondicaoPagto)).append(",");
		sql.append(Sql.getValue(itemPedido.cdMotivoPendencia)).append(",");
		sql.append(Sql.getValue(itemPedido.nuOrdemLiberacao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBaseAntecipacao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPontuacaoBaseItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPontuacaoRealizadoItem)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPesoPontuacao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlFatorCorrecaoFaixaPreco)).append(",");
		sql.append(Sql.getValue(itemPedido.vlFatorCorrecaoFaixaDias)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctFaixaPrecoPontuacao)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBasePontuacao)).append(",");
	    sql.append(Sql.getValue(itemPedido.cdPontuacaoConfig)).append(",");
	    sql.append(Sql.getValue(itemPedido.qtDiasFaixaPontuacao)).append(",");
		sql.append(Sql.getValue(itemPedido.cdPoliticaComercial)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctPoliticaComercial)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalMaoDeObra)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalImpostos)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalCustoComercial)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalCustoFinanceiro)).append(",");
		sql.append(Sql.getValue(itemPedido.cdCultura)).append(",");
		sql.append(Sql.getValue(itemPedido.cdPraga)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDose)).append(",");
		sql.append(Sql.getValue(itemPedido.flPromocional)).append(",");
		sql.append(Sql.getValue(itemPedido.cdMargemRentab)).append(",");
		sql.append(Sql.getValue(itemPedido.vlBaseMargemRentab)).append(",");
		sql.append(Sql.getValue(itemPedido.vlCustoMargemRentab)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctMargemRentab)).append(",");
		sql.append(Sql.getValue(itemPedido.qtItemGondola)).append(",");
		sql.append(Sql.getValue(itemPedido.vlDescProdutoRestrito)).append(",");
		sql.append(Sql.getValue(itemPedido.vlTotalDescProdutoRestrito)).append(",");
		sql.append(Sql.getValue(itemPedido.cdCombo)).append(",");
		sql.append(Sql.getValue(itemPedido.vlIndicePlataformaVendaCliFin)).append(",");
	    sql.append(Sql.getValue(itemPedido.cdDescProgressivo)).append(",");
	    sql.append(Sql.getValue(itemPedido.vlPctDescProg)).append(",");
		sql.append(Sql.getValue(itemPedido.flIgnoraDescQtd)).append(",");
		sql.append(Sql.getValue(itemPedido.flIgnoraDescQtdPro)).append(",");
		sql.append(Sql.getValue(itemPedido.dsProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.flAutorizado)).append(",");
	    sql.append(Sql.getValue(itemPedido.nuConversaoUnidade)).append(",");
		sql.append(Sql.getValue(itemPedido.vlPctFaixaDescQtdPeso)).append(",");
		sql.append(Sql.getValue(itemPedido.cdAgrupadorSimilaridade)).append(",");
		sql.append(Sql.getValue(itemPedido.cdVerbaSaldoCliente)).append(",");
		sql.append(Sql.getValue(itemPedido.vlCotacaoMoedaProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.dsMoedaVendaProduto)).append(",");
		sql.append(Sql.getValue(itemPedido.cdLocalEstoque)).append(",");
		sql.append(Sql.getValue(itemPedido.nuOrdemCompraCliente)).append(",");
		sql.append(Sql.getValue(itemPedido.nuSeqOrdemCompraCliente > 0 ? itemPedido.nuSeqOrdemCompraCliente : ValueUtil.VALOR_NI)).append(",");
		sql.append(Sql.getValue(itemPedido.infosPersonalizadas)).append(",");
		sql.append(Sql.getValue(itemPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        sql.append(" NUSEQITEMPEDIDO = ").append(Sql.getValue(itemPedido.nuSeqItemPedido)).append(",");
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(itemPedido.cdTabelaPreco)).append(",");
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(itemPedido.getQtItemFisicoOrg())).append(",");
        sql.append(" VLITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlItemPedido)).append(",");
        sql.append(" VLBASEITEMTABELAPRECO = ").append(Sql.getValue(itemPedido.vlBaseItemTabelaPreco)).append(",");
        sql.append(" VLBASECALCULODESCPROMOCIONAL = ").append(Sql.getValue(itemPedido.vlBaseCalculoDescPromocional)).append(",");
        sql.append(" VLBASEITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlBaseItemPedido)).append(",");
        sql.append(" VLTOTALITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlTotalItemPedido)).append(",");
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(itemPedido.vlPctDesconto)).append(",");
        sql.append(" VLPCTACRESCIMO = ").append(Sql.getValue(itemPedido.vlPctAcrescimo)).append(",");
        sql.append(" VLTOTALBRUTOITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlTotalBrutoItemPedido)).append(",");
        if (needAddVlUnidadePadraoAndBaseEmbElementar()) {
        	sql.append(" VLUNIDADEPADRAO = ").append(Sql.getValue(itemPedido.vlUnidadePadrao)).append(",");
        	sql.append(" VLBASEEMBALAGEMELEMENTAR = ").append(Sql.getValue(itemPedido.vlBaseEmbalagemElementar)).append(",");
        }
		if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
			sql.append(" CDLINHA = ").append(Sql.getValue(itemPedido.cdLinha)).append(",");
		}
		if (LavenderePdaConfig.usaConversaoUnidadesMedida) {
			sql.append(" QTITEMFATURAMENTO = ").append(Sql.getValue(itemPedido.qtItemFaturamento)).append(",");
		}
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual) {
			sql.append(" VLBASEFLEX = ").append(Sql.getValue(itemPedido.vlBaseFlex)).append(",");
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() || LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLRENTABILIDADE = ").append(Sql.getValue(itemPedido.vlRentabilidade)).append(",");
			sql.append(" VLRENTABILIDADESUG = ").append(Sql.getValue(itemPedido.vlRentabilidadeSug)).append(",");
		}
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo() || LavenderePdaConfig.aplicaComissaoEspecialProdutoPromocional) {
			sql.append(" VLTOTALCOMISSAO = ").append(Sql.getValue(itemPedido.vlTotalComissao)).append(",");
			sql.append(" VLPCTCOMISSAO = ").append(Sql.getValue(itemPedido.vlPctComissao)).append(",");
			sql.append(" VLPCTCOMISSAOTOTAL = ").append(Sql.getValue(itemPedido.vlPctComissaoTotal)).append(",");
		}
		if (LavenderePdaConfig.isUsaVerba() || LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			sql.append(" VLVERBAITEM = ").append(Sql.getValue(itemPedido.vlVerbaItem)).append(",");
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
				sql.append(" VLVERBAITEMPOSITIVO = ").append(Sql.getValue(itemPedido.vlVerbaItemPositivo)).append(",");
			}
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			sql.append(" VLITEMPEDIDOFRETE = ").append(Sql.getValue(itemPedido.vlItemPedidoFrete)).append(",");
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			sql.append(" CDLOTEPRODUTO = ").append(Sql.getValue(itemPedido.cdLoteProduto)).append(",");
		}
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto || LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido) {
			sql.append(" FLPRECOLIBERADOSENHA = ").append(Sql.getValue(itemPedido.flPrecoLiberadoSenha)).append(",");
			if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido) {
				sql.append(" CDUSUARIOLIBERACAO  = ").append(Sql.getValue(itemPedido.cdUsuarioLiberacao)).append(",");
			}
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
		sql.append(" VLRETORNOPRODUTO = ").append(Sql.getValue(itemPedido.vlRetornoProduto)).append(",");
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			sql.append(" QTPESO = ").append(Sql.getValue(itemPedido.qtPeso)).append(",");
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			sql.append(" VLST = ").append(Sql.getValue(itemPedido.vlSt)).append(",");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			sql.append(" VLREDUCAOOPTANTESIMPLES = ").append(Sql.getValue(itemPedido.vlReducaoOptanteSimples)).append(",");
		}
		if (LavenderePdaConfig.usaMotivoTrocaPorItemPedido()) {
			sql.append(" CDMOTIVOTROCA = ").append(Sql.getValue(itemPedido.cdMotivoTroca)).append(",");
			sql.append(" DSOBSMOTIVOTROCA = ").append(Sql.getValue(itemPedido.dsObsMotivoTroca)).append(",");
		}
		if (LavenderePdaConfig.usaObservacaoPorItemPedido) {
			sql.append(" DSOBSERVACAO = ").append(Sql.getValue(itemPedido.dsObservacao)).append(",");
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			sql.append(" QTPONTOSITEM = ").append(Sql.getValue(itemPedido.qtPontosItem)).append(",");
		}
		if (LavenderePdaConfig.quantidadeMinimaCaixasPedido > 0) {
			sql.append(" FLVENDIDOQTMINIMA = ").append(Sql.getValue(itemPedido.flVendidoQtMinima)).append(",");
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			sql.append(" CDPRAZOPAGTOPRECO = ").append(Sql.getValue(itemPedido.cdPrazoPagtoPreco)).append(",");
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			sql.append(" NUCONVERSAOUNIDADEPU = ").append(Sql.getValue(itemPedido.nuConversaoUnidadePu)).append(",");
			sql.append(" FLDIVIDEMULTIPLICAPU = ").append(Sql.getValue(itemPedido.flDivideMultiplicaPu)).append(",");
			sql.append(" VLINDICEFINANCEIROPU = ").append(Sql.getValue(itemPedido.vlIndiceFinanceiroPu)).append(",");
			if (LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar) {
				sql.append(" QTITEMPEDIDOUNELEMENTAR = ").append(Sql.getValue(itemPedido.qtItemPedidoUnElementar)).append(",");
				sql.append(" VLITEMPEDIDOUNELEMENTAR = ").append(Sql.getValue(itemPedido.vlItemPedidoUnElementar)).append(",");
			}
		}
		if (LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLFRETE = ").append(Sql.getValue(itemPedido.vlFrete)).append(",");
		}
		if (LavenderePdaConfig.liberaComSenhaVendaProdutoBloqueado) {
			sql.append(" FLMETAGRUPOPRODLIBERADOSENHA = ").append(Sql.getValue(itemPedido.flMetaGrupoProdLiberadoSenha)).append(",");
		}
		if (LavenderePdaConfig.usaSenhaVendaRelacionada) {
			sql.append(" FLLIBERADOVENDARELACIONADA = ").append(Sql.getValue(itemPedido.flLiberadoVendaRelacionada)).append(",");
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			sql.append(" VLIPIITEM = ").append(Sql.getValue(itemPedido.vlIpiItem)).append(",");
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			sql.append(" QTITEMESTOQUEPOSITIVO = ").append(Sql.getValue(itemPedido.qtItemEstoquePositivo)).append(",");
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			sql.append(" VLDESCONTOCCP = ").append(Sql.getValue(itemPedido.vlDescontoCCP)).append(",");
			sql.append(" VLPCTDESCONTOCCP = ").append(Sql.getValue(itemPedido.vlPctDescontoCCP)).append(",");
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" DTINICIOPROMOCAO = ").append(Sql.getValue(itemPedido.dtInicioPromocao)).append(",");
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			sql.append(" NUPROMOCAO = ").append(Sql.getValue(itemPedido.nuPromocao)).append(",");
			sql.append(" VLDESCONTOPROMO = ").append(Sql.getValue(itemPedido.vlDescontoPromo)).append(",");
			sql.append(" FLIGNORADESCQTDPRO = ").append(Sql.getValue(itemPedido.flIgnoraDescQtdPro)).append(",");
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido() || LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" VLFINALPROMO = ").append(Sql.getValue(itemPedido.vlFinalPromo)).append(",");
			sql.append(" VLPCTDESCONTOPROMO = ").append(Sql.getValue(itemPedido.vlPctDescontoPromo)).append(",");
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			sql.append(" VLPCTFAIXADESCQTD = ").append(Sql.getValue(itemPedido.vlPctFaixaDescQtd)).append(",");
			sql.append(" FLIGNORADESCQTD = ").append(Sql.getValue(itemPedido.flIgnoraDescQtd)).append(",");
			
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			sql.append(" VLFECOP = ").append(Sql.getValue(itemPedido.vlFecop)).append(",");
			sql.append(" VLTOTALFECOPITEM = ").append(Sql.getValue(itemPedido.vlTotalFecopItem)).append(",");
		}
		sql.append(" VLICMS = ").append(Sql.getValue(itemPedido.vlIcms)).append(",");
		sql.append(" VLPIS = ").append(Sql.getValue(itemPedido.vlPis)).append(",");
		sql.append(" VLCOFINS = ").append(Sql.getValue(itemPedido.vlCofins)).append(",");
		sql.append(" VLDESPESAACESSORIA = ").append(Sql.getValue(itemPedido.vlDespesaAcessoria)).append(",");
		if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() || LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido()) {
			sql.append(" CDSUGESTAOVENDA = ").append(Sql.getValue(itemPedido.cdSugestaoVenda)).append(",");
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			sql.append(" CDTRIBUTACAOCONFIG = ").append(Sql.getValue(itemPedido.cdTributacaoConfig)).append(",");
			sql.append(" VLTOTALSTITEM = ").append(Sql.getValue(itemPedido.vlTotalStItem)).append(",");
			sql.append(" VLTOTALIPIITEM = ").append(Sql.getValue(itemPedido.vlTotalIpiItem)).append(",");
			sql.append(" VLTOTALICMSITEM = ").append(Sql.getValue(itemPedido.vlTotalIcmsItem)).append(",");
			sql.append(" VLTOTALPISITEM = ").append(Sql.getValue(itemPedido.vlTotalPisItem)).append(",");
			sql.append(" VLTOTALCOFINSITEM = ").append(Sql.getValue(itemPedido.vlTotalCofinsItem)).append(",");
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			sql.append(" VLSEGUROITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlSeguroItemPedido)).append(",");
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1() || LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			sql.append(" VLDESCONTO = ").append(Sql.getValue(itemPedido.vlDesconto)).append(",");
			sql.append(" VLPCTDESCONTO2 = ").append(Sql.getValue(itemPedido.vlPctDesconto2)).append(",");
			sql.append(" VLDESCONTO2 = ").append(Sql.getValue(itemPedido.vlDesconto2)).append(",");
			sql.append(" VLPCTDESCONTO3 = ").append(Sql.getValue(itemPedido.vlPctDesconto3)).append(",");
			sql.append(" VLDESCONTO3 = ").append(Sql.getValue(itemPedido.vlDesconto3)).append(",");
		} else if (LavenderePdaConfig.usaDescontoExtra) {
			sql.append(" VLPCTDESCONTO2 = ").append(Sql.getValue(itemPedido.vlPctDesconto2)).append(",");
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco || LavenderePdaConfig.usaFreteApenasTipoFob || LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido() || LavenderePdaConfig.usaFreteManualPedido) {
			sql.append(" VLTOTALITEMPEDIDOFRETE = ").append(Sql.getValue(itemPedido.vlTotalItemPedidoFrete)).append(",");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			sql.append(" VLPCTDESCONTOCANAL = ").append(Sql.getValue(itemPedido.vlPctDescontoCanal)).append(",");
		}
		sql.append(" CDUNIDADE = ").append(Sql.getValue(itemPedido.cdUnidade)).append(",");
		if (LavenderePdaConfig.liberaComSenhaVendaProdutoSemEstoque) {
			sql.append(" flEstoqueLiberado = ").append(Sql.getValue(itemPedido.flEstoqueLiberado)).append(",");
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			sql.append(" CDVERBAGRUPO = ").append(Sql.getValue(itemPedido.cdVerbaGrupo)).append(",");
		}
		if (LavenderePdaConfig.liberaSenhaQtdItemMaiorPedidoOriginal) {
			sql.append(" FLQUANTIDADELIBERADA = ").append(Sql.getValue(itemPedido.flQuantidadeLiberada)).append(",");
		}
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
			sql.append(" CDCONDICAOCOMERCIAL = ").append(Sql.getValue(itemPedido.cdCondicaoComercial)).append(",");
		}
		if (LavenderePdaConfig.isCreditoIndiceCondicaoPagamentoNaBonificacao()) {
			sql.append(" VLCREDITOCONDICAO = ").append(Sql.getValue(itemPedido.vlCreditoCondicao)).append(",");
		}
		if (LavenderePdaConfig.isCreditoIndiceTipoFreteCliNaBonificacao()) {
			sql.append(" VLCREDITOFRETE = ").append(Sql.getValue(itemPedido.vlCreditoFrete)).append(",");
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(" FLPENDENTE = ").append(Sql.getValue(itemPedido.flPendente)).append(",");
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			sql.append(" VLPCTDESCPROGRESSIVOMIX = ").append(Sql.getValue(itemPedido.vlPctDescProgressivoMix)).append(",");
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			sql.append(" VLVOLUMEITEM = ").append(Sql.getValue(itemPedido.vlVolumeItem)).append(",");
		}
		boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem = LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem();
		boolean usaDescontoPorVolumeVendaMensal = LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal();
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || usaDescontoPorVolumeVendaMensal || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				sql.append(" VLPCTDESCFRETE = ").append(Sql.getValue(itemPedido.vlPctDescFrete)).append(",");
			}
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || usaDescontoPorVolumeVendaMensal || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				sql.append(" VLPCTDESCCLIENTE = ").append(Sql.getValue(itemPedido.vlPctDescCliente)).append(",");
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem || LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
				sql.append(" VLPCTDESCONTOCONDICAO = ").append(Sql.getValue(itemPedido.vlPctDescontoCondicao)).append(",");
			}
		}
		if (LavenderePdaConfig.isUsaDivisaoValorVerbaUsuarioEmpresa()) {
			sql.append(" VLPCTVERBARATEIO = ").append(Sql.getValue(itemPedido.vlPctVerbaRateio)).append(",");
		}
		if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba()) {
			sql.append(" VLPCTMARGEMPRODUTO = ").append(Sql.getValue(itemPedido.vlPctMargemProduto)).append(",");
		}
		if (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco) {
			sql.append(" CDVERBA = ").append(Sql.getValue(itemPedido.cdVerba)).append(",");
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			sql.append(" VLPCTCONTRATOCLI = ").append(Sql.getValue(itemPedido.vlPctContratoCli)).append(",");
			sql.append(" FLDECISAOCALCULO = ").append(Sql.getValue(itemPedido.flDecisaoCalculo)).append(",");
		}
		if (LavenderePdaConfig.usaCalculoReversoNaST) {
			sql.append(" VLITEMPEDIDOSTREVERSO = ").append(Sql.getValue(itemPedido.vlItemPedidoStReverso)).append(",");
			sql.append(" VLPCTDESCONTOSTREVERSO = ").append(Sql.getValue(itemPedido.vlPctDescontoStReverso)).append(",");
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito()) {
			sql.append(" FLRESTRITO = ").append(Sql.getValue(itemPedido.flRestrito)).append(",");
		}
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
			sql.append(" FLVALORTABELAALTERADO = ").append(Sql.getValue(itemPedido.flValorTabelaAlterado)).append(",");
		}
		if (LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
			sql.append(" VLINDICEGRUPOPROD = ").append(Sql.getValue(itemPedido.vlIndiceGrupoProd)).append(",");
		}
		if (LavenderePdaConfig.usaIndicacaoItemPedidoProdutoPromocional) {
			sql.append(" FLPROMOCAO = ").append(Sql.getValue(itemPedido.flPromocao)).append(",");
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			sql.append(" FLTIPOCADASTROITEM = ").append(Sql.getValue(itemPedido.flTipoCadastroItem)).append(",");
			sql.append(" QTDCREDITODESC = ").append(Sql.getValue(itemPedido.qtdCreditoDesc)).append(",");
			sql.append(" CDPRODUTOCREDITODESC = ").append(Sql.getValue(itemPedido.cdProdutoCreditoDesc)).append(",");
		}
		if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			sql.append(" VLITEMIPI = ").append(Sql.getValue(itemPedido.vlItemIpi)).append(",");
			sql.append(" VLBASEITEMIPI = ").append(Sql.getValue(itemPedido.vlItemIpi)).append(",");
		}
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			sql.append(" QTITEMDESEJADO = ").append(Sql.getValue(itemPedido.qtItemDesejado)).append(",");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLTOTALMARGEMITEM = ").append(Sql.getValue(itemPedido.vlTotalMargemItem)).append(",");
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			sql.append(" FLSUGVENDAPERSON = ").append(Sql.getValue(itemPedido.flSugVendaPerson)).append(",");
		}
		if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
			sql.append(" QTESTOQUECLIENTE = ").append(Sql.getValue(itemPedido.qtEstoqueCliente)).append(",");
		}
		if (LavenderePdaConfig.calculaRentabilidadePedidoRetornado
				|| LavenderePdaConfig.indiceFinanceiroCondPagtoVlBase
				|| LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido == 8
				|| LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()
				|| LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLTOTALPRECOCUSTO = ").append(Sql.getValue(itemPedido.vlTotalPrecoCusto)).append(",");
		}
		if (LavenderePdaConfig.calculaPrazoEntregaPorProduto) {
			sql.append(" NUDIASPRAZOENTREGA = ").append(Sql.getValue(itemPedido.nuDiasPrazoEntrega)).append(",");
		}
		sql.append(" VLPCTMARGEMAGREGADA = ").append(Sql.getValue(itemPedido.vlPctMargemAgregada)).append(",");
		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			sql.append(" VLPCTACRESCIMOCONDICAO = ").append(Sql.getValue(itemPedido.vlPctAcrescimoCondicao)).append(",");
			sql.append(" VLPCTACRESCCLIENTE = ").append(Sql.getValue(itemPedido.vlPctAcrescCliente)).append(",");
			sql.append(" VLPCTDESCALCADA = ").append(Sql.getValue(itemPedido.vlPctDescAlcada)).append(",");
		}
		if (LavenderePdaConfig.usaInfoComplementarItemPedido() || LavenderePdaConfig.usaGradeProduto4()) {
			sql.append(" DTENTREGA = ").append(Sql.getValue(itemPedido.dtEntrega)).append(",");
			sql.append(" VLALTURA = ").append(Sql.getValue(itemPedido.vlAltura)).append(",");
			sql.append(" VLLARGURA = ").append(Sql.getValue(itemPedido.vlLargura)).append(",");
			sql.append(" VLCOMPRIMENTO = ").append(Sql.getValue(itemPedido.vlComprimento)).append(",");
			sql.append(" VLPOSVINCO1 = ").append(Sql.getValue(itemPedido.vlPosVinco1)).append(",");
			sql.append(" VLPOSVINCO2 = ").append(Sql.getValue(itemPedido.vlPosVinco2)).append(",");
			sql.append(" VLPOSVINCO3 = ").append(Sql.getValue(itemPedido.vlPosVinco3)).append(",");
			sql.append(" VLPOSVINCO4 = ").append(Sql.getValue(itemPedido.vlPosVinco4)).append(",");
			sql.append(" VLPOSVINCO5 = ").append(Sql.getValue(itemPedido.vlPosVinco5)).append(",");
			sql.append(" VLPOSVINCO6 = ").append(Sql.getValue(itemPedido.vlPosVinco6)).append(",");
			sql.append(" VLPOSVINCO7 = ").append(Sql.getValue(itemPedido.vlPosVinco7)).append(",");
			sql.append(" VLPOSVINCO8 = ").append(Sql.getValue(itemPedido.vlPosVinco8)).append(",");
			sql.append(" VLPOSVINCO9 = ").append(Sql.getValue(itemPedido.vlPosVinco9)).append(",");
			sql.append(" VLPOSVINCO10 = ").append(Sql.getValue(itemPedido.vlPosVinco10)).append(",");
			sql.append(" CDCULTURA = ").append(Sql.getValue(itemPedido.cdCultura)).append(",");
			sql.append(" CDPRAGA = ").append(Sql.getValue(itemPedido.cdPraga)).append(",");
			sql.append(" VLDOSE = ").append(Sql.getValue(itemPedido.vlDose)).append(",");
		}
		if (LavenderePdaConfig.aplicaDescontoNoItemDeAcordoComICMSdoCliente) {
			sql.append(" VLPCTACRESCIMOICMS = ").append(Sql.getValue(itemPedido.vlPctAcrescimoIcms)).append(",");
			sql.append(" VLPCTDESCONTOICMS = ").append(Sql.getValue(itemPedido.vlPctDescontoIcms)).append(",");
		}
		if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sql.append(" CDLOCAL = ").append(Sql.getValue(itemPedido.cdLocal)).append(",");
		}
		if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
			sql.append(" FLORIGEMESCOLHAITEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemEscolhaItemPedido)).append(",");
			sql.append(" CDPRODUTOORIGEM = ").append(Sql.getValue(itemPedido.cdProdutoOrigem)).append(",");
			sql.append(" VLITEMPEDIDOORIGEM = ").append(Sql.getValue(itemPedido.vlItemPedidoOrigem)).append(",");
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
			sql.append(" FLERRORECALCULO = ").append(Sql.getValue(itemPedido.flErroRecalculo)).append(",");
		}
		if (LavenderePdaConfig.usaCalculoVpcItemPedido()) {
			sql.append(" VLVPC = ").append(Sql.getValue(itemPedido.vlVpc)).append(",");
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			sql.append(" VLPCTDESCONTOAUTO = ").append(Sql.getValue(itemPedido.vlPctDescontoAuto)).append(",")
			.append(" VLPCTDESCONTOEFETIVO = ").append(Sql.getValue(itemPedido.vlPctDescontoEfetivo)).append(",")
			.append(" VLPCTDESCONTOAUTOEFETIVO = ").append(Sql.getValue(itemPedido.vlPctDescontoAutoEfetivo)).append(",")
			.append(" VLDESCONTOAUTO = ").append(Sql.getValue(itemPedido.vlDescontoAuto)).append(",")
			.append(" VLTOTALDESCONTOAUTO = ").append(Sql.getValue(itemPedido.vlTotalDescontoAuto)).append(",")
			.append(" VLPRECOEFETIVOUNITARIO = ").append(Sql.getValue(itemPedido.vlPrecoEfetivoUnitario)).append(",")
			.append(" VLPRECOEFETIVOUNITARIODESC = ").append(Sql.getValue(itemPedido.vlPrecoEfetivoUnitarioDesc)).append(",")
			.append(" VLEFETIVOTOTALITEM = ").append(Sql.getValue(itemPedido.vlEfetivoTotalItem)).append(",")
			.append(" VLEFETIVOTOTALITEMDESC = ").append(Sql.getValue(itemPedido.vlEfetivoTotalItemDesc)).append(",")
			.append(" VLDESCONTOTOTALAUTODESC = ").append(Sql.getValue(itemPedido.vlDescontoTotalAutoDesc)).append(",")
			.append(" VLBASEINTERPOLACAOPRODUTO = ").append(Sql.getValue(itemPedido.vlBaseInterpolacaoProduto)).append(",");
			if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
				sql.append(" CDGRUPODESCPROD = ").append(Sql.getValue(itemPedido.cdGrupoDescProd)).append(",");
				sql.append(" CDGRUPODESCCLI = ").append(Sql.getValue(itemPedido.cdGrupoDescCli)).append(",");
			}
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			sql.append(" CDCOMISSAOPEDIDOREP = ").append(Sql.getValue(itemPedido.cdComissaoPedidoRep)).append(",");
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			sql.append(" VLITEMFRETETRIBUTACAO = ").append(Sql.getValue(itemPedido.vlItemFreteTributacao)).append(",");
			sql.append(" VLTOTALITEMFRETETRIBUTACAO = ").append(Sql.getValue(itemPedido.vlTotalItemFreteTributacao)).append(",");
		}
		sql.append(" VLDESCONTOCONDICAO = ").append(Sql.getValue(itemPedido.vlDescontoCondicao)).append(",");
		if (LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueComDetalhes()) {
			sql.append(" QTESTOQUEPREVISTO = ").append(Sql.getValue(itemPedido.qtEstoquePrevisto)).append(",");
			sql.append(" DTESTOQUEPREVISTO = ").append(Sql.getValue(itemPedido.dtEstoquePrevisto)).append(",");
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
			sql.append(" VLPCTDESCPEDIDO = ").append(Sql.getValue(itemPedido.vlPctDescPedido)).append(",");
		}
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			sql.append(" VLPCTDESCCONDPAGTO = ").append(Sql.getValue(itemPedido.vlPctDescCondPagto)).append(",");
		}
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			sql.append(" VLVERBAGRUPOITEM = ").append(Sql.getValue(itemPedido.vlVerbaGrupoItem)).append(",");
			sql.append(" VLTOLERANCIAVERGRUSALDO = ").append(Sql.getValue(itemPedido.vlToleranciaVerGruSaldo)).append(",");
		}
		sql.append(" VLTOTALBASESTITEM = ").append(Sql.getValue(itemPedido.vlTotalBaseStItem)).append(",");
		sql.append(" VLTOTALBASEICMSITEM = ").append(Sql.getValue(itemPedido.vlTotalBaseIcmsItem)).append(",");
		sql.append(" VLINDICEVOLUME = ").append(Sql.getValue(itemPedido.vlIndiceVolume)).append(",");
		sql.append(" VLPCTTOTALMARGEMITEM = ").append(Sql.getValue(itemPedido.vlPctTotalMargemItem)).append(",");
		if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			sql.append(" CDPACOTE = ").append(Sql.getValue(itemPedido.cdPacote)).append(",");
		}
		if (LavenderePdaConfig.permiteIgnorarRecalculoCondicaoPagamento) {
			sql.append(" VLINDICECONDICAOPAGTO = ").append(Sql.getValue(itemPedido.vlIndiceCondicaoPagto)).append(",");
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			sql.append(" CDPOLITICACOMERCIAL = ").append(Sql.getValue(itemPedido.cdPoliticaComercial)).append(",");
			sql.append(" VLPCTPOLITICACOMERCIAL = ").append(Sql.getValue(itemPedido.vlPctPoliticaComercial)).append(",");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			sql.append(" VLBASEANTECIPACAO = ").append(Sql.getValue(itemPedido.vlBaseAntecipacao)).append(",");
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			sql.append(" VLPONTUACAOBASEITEM = ").append(Sql.getValue(itemPedido.vlPontuacaoBaseItem)).append(",");
			sql.append(" VLPONTUACAOREALIZADOITEM = ").append(Sql.getValue(itemPedido.vlPontuacaoRealizadoItem)).append(",");
			sql.append(" VLPESOPONTUACAO = ").append(Sql.getValue(itemPedido.vlPesoPontuacao)).append(",");
			sql.append(" VLFATORCORRECAOFAIXAPRECO = ").append(Sql.getValue(itemPedido.vlFatorCorrecaoFaixaPreco)).append(",");
			sql.append(" VLFATORCORRECAOFAIXADIAS = ").append(Sql.getValue(itemPedido.vlFatorCorrecaoFaixaDias)).append(",");
			sql.append(" VLPCTFAIXAPRECOPONTUACAO = ").append(Sql.getValue(itemPedido.vlPctFaixaPrecoPontuacao)).append(",");
			sql.append(" VLBASEPONTUACAO = ").append(Sql.getValue(itemPedido.vlBasePontuacao)).append(",");
			sql.append(" CDPONTUACAOCONFIG = ").append(Sql.getValue(itemPedido.cdPontuacaoConfig)).append(",");
			sql.append(" QTDIASFAIXAPONTUACAO = ").append(Sql.getValue(itemPedido.qtDiasFaixaPontuacao)).append(",");
		}
		if (LavenderePdaConfig.isUsaMotivoPendencia()) {
			sql.append(" CDMOTIVOPENDENCIA = ").append(Sql.getValue(itemPedido.cdMotivoPendencia)).append(",");
			sql.append(" NUORDEMLIBERACAO = ").append(Sql.getValue(itemPedido.nuOrdemLiberacao)).append(",");
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2() || LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLTOTALMAODEOBRA = ").append(Sql.getValue(itemPedido.vlTotalMaoDeObra)).append(",");
			sql.append(" VLTOTALIMPOSTOS = ").append(Sql.getValue(itemPedido.vlTotalImpostos)).append(",");
			sql.append(" VLTOTALCUSTOCOMERCIAL = ").append(Sql.getValue(itemPedido.vlTotalCustoComercial)).append(",");
			sql.append(" VLTOTALCUSTOFINANCEIRO = ").append(Sql.getValue(itemPedido.vlTotalCustoFinanceiro)).append(",");
			sql.append(" VLDESCPRODUTORESTRITO = ").append(Sql.getValue(itemPedido.vlDescProdutoRestrito)).append(",");
			sql.append(" VLTOTALDESCPRODUTORESTRITO = ").append(Sql.getValue(itemPedido.vlTotalDescProdutoRestrito)).append(",");
		}
        if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
        	sql.append(" FLPROMOCIONAL = ").append(Sql.getValue(itemPedido.flPromocional)).append(",");
        }
        if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" CDMARGEMRENTAB = ").append(Sql.getValue(itemPedido.cdMargemRentab)).append(",");
		}
		if (LavenderePdaConfig.usaGondolaPedido) {
			sql.append(" QTITEMGONDOLA = ").append(Sql.getValue(itemPedido.qtItemGondola)).append(",");
		}
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedido.flTipoAlteracao)).append(",");
		if(LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			sql.append(" VLBASEMARGEMRENTAB = ").append(Sql.getValue(itemPedido.vlBaseMargemRentab)).append(",");
			sql.append(" VLCUSTOMARGEMRENTAB = ").append(Sql.getValue(itemPedido.vlCustoMargemRentab)).append(",");
			sql.append(" VLPCTMARGEMRENTAB = ").append(Sql.getValue(itemPedido.vlPctMargemRentab)).append(",");
        }
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			sql.append(" CDCOMBO = ").append(Sql.getValue(itemPedido.cdCombo)).append(",");
		}
	    if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
		    sql.append(" CDDESCPROGRESSIVO = ").append(Sql.getValue(itemPedido.cdDescProgressivo)).append(",");
		    sql.append(" VLPCTDESCPROG = ").append(Sql.getValue(itemPedido.vlPctDescProg)).append(",");
	    }
	    if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
	    	sql.append(" FLAUTORIZADO = ").append(Sql.getValue(itemPedido.flAutorizado)).append(",");
	    }
		if (LavenderePdaConfig.usaDescQuantidadePeso()) {
			sql.append(" VLPCTFAIXADESCQTDPESO =").append(Sql.getValue(itemPedido.vlPctFaixaDescQtdPeso)).append(",");
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			sql.append(" CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(itemPedido.cdAgrupadorSimilaridade)).append(",");
		}
		if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
			sql.append(" CDVERBASALDOCLIENTE = ").append(Sql.getValue(itemPedido.cdVerbaSaldoCliente)).append(",");
		}
		if (LavenderePdaConfig.usaGradeProduto5()) {
			sql.append(" CDITEMGRADE1 = ").append(Sql.getValue(itemPedido.cdItemGrade1)).append(",")
			.append(" CDITEMGRADE2 = ").append(Sql.getValue(itemPedido.cdItemGrade2)).append(",")
			.append(" CDITEMGRADE3 = ").append(Sql.getValue(itemPedido.cdItemGrade3)).append(",");
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			sql.append(" VLCOTACAOMOEDAPRODUTO = ").append(Sql.getValue(itemPedido.vlCotacaoMoedaProduto)).append(",");
			sql.append(" DSMOEDAVENDAPRODUTO = ").append(Sql.getValue(itemPedido.dsMoedaVendaProduto)).append(", ");
		}
		sql.append(" CDLOCALESTOQUE = ").append(Sql.getValue(itemPedido.cdLocalEstoque)).append(",");
		sql.append(" FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemPedido.flTipoItemPedido)).append(",");
	    sql.append(" DSPRODUTO = ").append(Sql.getValue(itemPedido.dsProduto)).append(",");
	    sql.append(" NUCONVERSAOUNIDADENEGOCIADO = ").append(Sql.getValue(itemPedido.nuConversaoUnidade)).append(",");
		if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
			sql.append(" NUORDEMCOMPRACLIENTE = ").append(Sql.getValue(itemPedido.nuOrdemCompraCliente)).append(",");
			if (itemPedido.nuSeqOrdemCompraCliente > 0) {
				sql.append(" NUSEQORDEMCOMPRACLIENTE = ").append(Sql.getValue(itemPedido.nuSeqOrdemCompraCliente)).append(",");
			} else {
				sql.append(" NUSEQORDEMCOMPRACLIENTE = ").append(Sql.getValue(ValueUtil.VALOR_NI)).append(",");
			}
		}
		if (LavenderePdaConfig.isConfigApresentacaoInfosPersonalizadasCapaItemPedido()) {
			sql.append(" INFOSPERSONALIZADAS = ").append(Sql.getValue(itemPedido.infosPersonalizadas)).append(",");
		}
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemPedido.cdUsuario));
    }

    protected void addWhereMaxKey(BaseDomain domain, StringBuffer sql) {
        ItemPedido itemPedido = (ItemPedido) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido));
        sql.append(" and CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto));
        sql.append(" and FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemPedido.flTipoItemPedido));
        if (ValueUtil.isNotEmpty(itemPedido.cdItemGrade1)) {
        	sql.append(" and CDITEMGRADE1 = ").append(Sql.getValue(itemPedido.cdItemGrade1));
        }
        if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(itemPedido.cdUnidade)) {
        	sql.append(" and CDUNIDADE = ").append(Sql.getValue(itemPedido.cdUnidade));
        }
        if (ValueUtil.isNotEmpty(itemPedido.cdLoteProduto)) {
        	sql.append(" and CDLOTEPRODUTO = ").append(Sql.getValue(itemPedido.cdLoteProduto));
        }
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        boolean buscarItensConsumindoVerba = itemPedido.buscarItensConsumindoVerba && ValueUtil.isNotEmpty(itemPedido.cdGrupoProduto1);
        
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", itemPedido.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", itemPedido.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", itemPedido.flOrigemPedido);
		if (buscarItensConsumindoVerba) {
			sqlWhereClause.addAndConditionNotEquals("tb.NUPEDIDO", itemPedido.nuPedido);
		} else {
			sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", itemPedido.nuPedido);
		}
		sqlWhereClause.addAndConditionEquals("tb.CDPRODUTO", itemPedido.cdProduto);
		sqlWhereClause.addAndConditionEquals("tb.FLTIPOITEMPEDIDO", itemPedido.flTipoItemPedido);
		sqlWhereClause.addAndConditionEquals("FLSUGVENDAPERSON", itemPedido.flSugVendaPerson);
		sqlWhereClause.addAndConditionEquals("CDITEMGRADE1", itemPedido.cdItemGrade1);
		sqlWhereClause.addAndConditionEquals("CDMARGEMRENTAB", itemPedido.cdMargemRentab);
		if (ValueUtil.isNotEmpty(itemPedido.dsAgrupadorGradeFilter)) {
			sqlWhereClause.addAndConditionEquals(DaoUtil.ALIAS_PRODUTO + ".DSAGRUPADORGRADE", itemPedido.dsAgrupadorGradeFilter);
			sqlWhereClause.addAndCondition("PRODUTOGRADE.CDPRODUTO IS NOT NULL");
		}
		if (buscarItensConsumindoVerba) {
			sqlWhereClause.addAndConditionEquals("prod.CDGRUPOPRODUTO1", itemPedido.cdGrupoProduto1);
		}
        sql.append(sqlWhereClause.getSql());
        if (buscarItensConsumindoVerba) {
        	sql.append(" AND tb.VLVERBAGRUPOITEM < 0 ");
        }
    }

	public int getNextNuSeqItemPedido(ItemPedido itemPedido) throws SQLException {
		int maxNuSeqItemPedido = 0;
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        sql.append(" max(nuSeqItemPedido) as maxNuSeqItemPedido from ");
        sql.append(tableName);
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido));
        return getInt(sql.toString()) + 1;
    }

	public double getVlTotalPedidoComSt(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT vlItemPedido, vlSt, qtitemfisico FROM ");
		sql.append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
    		double vlTotalPedidoComSt = 0;
    		double vlItemPedido = 0;
    		double vlSt = 0;
    		double qtitemfisico = 0;
    		while (rs.next()) {
    			vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
    			vlSt = rs.getDouble("vlSt");
    			qtitemfisico = rs.getDouble("qtitemfisico");
    			vlTotalPedidoComSt += (vlItemPedido + vlSt) * qtitemfisico;
    		}
    		return vlTotalPedidoComSt;
    	}
	}

	public double getVlTotalPedidoComIpi(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT vlItemPedido, vlIpiItem, qtitemfisico FROM ");
		sql.append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
    		double vlTotalPedidoComIpi = 0;
    		double vlItemPedido = 0;
    		double vlIpi = 0;
    		double qtitemfisico = 0;
    		while (rs.next()) {
    			vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
    			vlIpi = rs.getDouble("vlIpiItem");
    			qtitemfisico = rs.getDouble("qtitemfisico");
    			vlTotalPedidoComIpi += (vlItemPedido + vlIpi) * qtitemfisico;
    		}
    		return vlTotalPedidoComIpi;
    	}
	}

	public double getVlTotalPedidoComTributos(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT vlItemPedido, vlSt, vlIpiItem, vlFecop, qtItemFisico FROM ");
		sql.append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
    		double vlTotalPedidoComTributos = 0;
    		double vlItemPedido = 0;
    		double vlSt = 0;
    		double vlIpi = 0;
    		double vlFecop = 0;
    		double qtItemFisico = 0;
    		while (rs.next()) {
    			vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
    			vlSt = rs.getDouble("vlSt");
    			vlIpi = rs.getDouble("vlIpiItem");
    			if (LavenderePdaConfig.calculaFecopItemPedido) {
    				vlFecop = rs.getDouble("vlFecop");
    			}
    			qtItemFisico = rs.getDouble("qtItemFisico");
    			vlTotalPedidoComTributos += (vlItemPedido + vlSt + vlIpi + vlFecop) * qtItemFisico;
    		}
    		return vlTotalPedidoComTributos;
    	}
	}

	public Vector getItemPedidoListParaCalculoTributacao(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT vlItemPedido, vlSt, vlIpiItem, vlFecop, qtItemFisico, " +
					 "vlTotalPisItem, vlTotalCofinsItem, vlTotalIcmsItem, vlTotalStItem, " +
					 "vlTotalIpiItem, flTipoItemPedido, vlDespesaAcessoria, vlSeguroItemPedido, " +
					 "cdEmpresa, cdRepresentante, cdProduto");
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append(" and NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector itemPedidoList = new Vector();
			while (rs.next()) {
				ItemPedido item = new ItemPedido();
				item.cdEmpresa = rs.getString("cdEmpresa");
				item.cdRepresentante = rs.getString("cdRepresentante");
				item.cdProduto = rs.getString("cdProduto");
				item.vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
				item.vlSt = rs.getDouble("vlSt");
				item.vlIpiItem = rs.getDouble("vlIpiItem");
				item.vlFecop = rs.getDouble("vlFecop");
				item.setQtItemFisico(rs.getDouble("qtItemFisico"));
				item.vlTotalPisItem = rs.getDouble("vlTotalPisItem");
				item.vlTotalCofinsItem = rs.getDouble("vlTotalCofinsItem");
				item.vlTotalIcmsItem = rs.getDouble("vlTotalIcmsItem");
				item.vlTotalStItem = rs.getDouble("vlTotalStItem");
				item.vlTotalIpiItem = rs.getDouble("vlTotalIpiItem");
				item.flTipoItemPedido = rs.getString("flTipoItemPedido");
				item.vlDespesaAcessoria = rs.getDouble("vlDespesaAcessoria");
				item.vlSeguroItemPedido = rs.getDouble("vlSeguroItemPedido");
				item.pedido = pedido;
				itemPedidoList.addElement(item);
			}
			return itemPedidoList;
		}
	}

	public double countItensPedido(ItemPedido itemPedido) throws SQLException {
        StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT sum(qtitemfisico) as qtdeItens FROM ");
    	sql.append(tableName).append(" tb");
        addWhereByExample(itemPedido, sql);
        return getDouble(sql.toString());
	}

	public int findCountPedidosByCliente(String nuPedidoCorrente, String cdCliente) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append("select count(distinct tb.NUPEDIDO)");
		sql.append("  from ").append(tableName).append(" tb, ");
		sql.append(getTableNamePedido()).append(" pedido ");
    	sql.append(" where tb.CDEMPRESA = pedido.CDEMPRESA");
		sql.append("   and tb.NUPEDIDO = pedido.NUPEDIDO");
		sql.append("   and tb.FLORIGEMPEDIDO = pedido.FLORIGEMPEDIDO");
		sql.append("   and tb.CDREPRESENTANTE = pedido.CDREPRESENTANTE");
		sql.append("   and tb.FLTIPOITEMPEDIDO = ").append(Sql.getValue(TipoItemPedido.TIPOITEMPEDIDO_NORMAL));
		sql.append("   and pedido.CDCLIENTE = ").append(Sql.getValue(cdCliente));
		sql.append("   and pedido.NUPEDIDO <> ").append(Sql.getValue(nuPedidoCorrente));
		return getInt(sql.toString());
	}

	public ItemPedido findItemPedidoUltimoPedidoCliente(String cdCliente, String cdProduto, String nuPedidoDif) throws SQLException {
		Hashtable valuesPedido = findVlItemUltimoPedido(cdCliente, cdProduto, nuPedidoDif, ItemPedido.TABLE_NAME_ITEMPEDIDO);
		Hashtable valuesPedidoErp = findVlItemUltimoPedido(cdCliente, cdProduto, nuPedidoDif, ItemPedido.TABLE_NAME_ITEMPEDIDOERP);
		//--
		Date dtEmissaoPedido = null;
		String hrEmissaoPedido = null;
		//--
		Date dtEmissaoPedidoErp = null;
		String hrEmissaoPedidoErp = null;
		ItemPedido itemPedido = new ItemPedido();
		ItemPedido itemPedidoErp = new ItemPedido();
		if (valuesPedido != null && valuesPedido.size() > 0) {
			dtEmissaoPedido = (Date) valuesPedido.get("dtEmissao");
			hrEmissaoPedido = (String) valuesPedido.get("hrEmissao");
			itemPedido.cdCliente = StringUtil.getStringValue(valuesPedido.get("cdCliente"));
			itemPedido.cdProduto = StringUtil.getStringValue(valuesPedido.get("cdProduto"));
			itemPedido.cdUnidade = StringUtil.getStringValue(valuesPedido.get("cdUnidade"));
			itemPedido.nuPedido = StringUtil.getStringValue(valuesPedido.get("nuPedido"));
			itemPedido.vlPctDesconto = ValueUtil.getDoubleValue((String) valuesPedido.get("vlPctDesconto"));
			itemPedido.vlPctAcrescimo = ValueUtil.getDoubleValue((String) valuesPedido.get("vlPctAcrescimo"));
			itemPedido.vlItemPedido = ValueUtil.getDoubleValue((String) valuesPedido.get("vlItemPedido"));
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				itemPedido.setQtItemFisico((double) valuesPedido.get("qtItemFisico"));
				itemPedido.vlTotalIpiItem = (double) valuesPedido.get("vlTotalIpiItem");
			}
			if (LavenderePdaConfig.isIgnoraAcrescimoDesconto()) {
				itemPedido.vlBaseItemPedido = ValueUtil.getDoubleValue((String) valuesPedido.get("vlBaseItemPedido"));
			}

		}
		if (valuesPedidoErp != null && valuesPedidoErp.size() > 0) {
			dtEmissaoPedidoErp = (Date) valuesPedidoErp.get("dtEmissao");
			hrEmissaoPedidoErp = (String) valuesPedidoErp.get("hrEmissao");
			itemPedidoErp.cdCliente = StringUtil.getStringValue(valuesPedidoErp.get("cdCliente"));
			itemPedidoErp.cdProduto = StringUtil.getStringValue(valuesPedidoErp.get("cdProduto"));
			itemPedidoErp.cdUnidade = StringUtil.getStringValue(valuesPedidoErp.get("cdUnidade"));
			itemPedidoErp.nuPedido = StringUtil.getStringValue(valuesPedidoErp.get("nuPedido"));
			itemPedidoErp.vlPctDesconto = ValueUtil.getDoubleValue((String) valuesPedidoErp.get("vlPctDesconto"));
			itemPedidoErp.vlPctAcrescimo = ValueUtil.getDoubleValue((String) valuesPedidoErp.get("vlPctAcrescimo"));
			itemPedidoErp.vlItemPedido = ValueUtil.getDoubleValue((String) valuesPedidoErp.get("vlItemPedido"));
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				itemPedidoErp.setQtItemFisico((double) valuesPedidoErp.get("qtItemFisico"));
				itemPedidoErp.vlTotalIpiItem = (double) valuesPedidoErp.get("vlTotalIpiItem");
			}
			if (LavenderePdaConfig.isIgnoraAcrescimoDesconto()) {
				itemPedido.vlBaseItemPedido = ValueUtil.getDoubleValue((String) valuesPedido.get("vlBaseItemPedido"));
		}
		}

		if (ValueUtil.isNotEmpty(dtEmissaoPedidoErp) && ValueUtil.isNotEmpty(dtEmissaoPedido)) {
			if (dtEmissaoPedido.isAfter(dtEmissaoPedidoErp)) {
				return itemPedido;
			} else if (dtEmissaoPedido.equals(dtEmissaoPedidoErp)) {
				if (ValueUtil.isEmpty(hrEmissaoPedidoErp)) {
					return itemPedido;
				} else if (ValueUtil.isNotEmpty(hrEmissaoPedido)) {
					String[] hora = StringUtil.split(hrEmissaoPedido, ':');
					String[] horaErp = StringUtil.split(hrEmissaoPedidoErp, ':');
					int time = ValueUtil.getIntegerValue(hora[0] + hora[1]);
					int timeErp = ValueUtil.getIntegerValue(horaErp[0] + horaErp[1]);
					if (time > timeErp) {
						return itemPedido;
					} else {
						return itemPedidoErp;
					}
				}
			} else {
				return itemPedidoErp;
			}
		} else if (ValueUtil.isNotEmpty(dtEmissaoPedido)) {
			return itemPedido;
		} else if (ValueUtil.isNotEmpty(dtEmissaoPedidoErp)) {
			return itemPedidoErp;
		}
		return new ItemPedido();
	}

	private Hashtable findVlItemUltimoPedido(String cdCliente, String cdProduto, String nuPedidoDif, String tableItem) throws SQLException {
		ItemPedido itemPedidoFilter = new ItemPedido();
		Hashtable values = new Hashtable(0);
		itemPedidoFilter.cdCliente = cdCliente;
		itemPedidoFilter.cdProduto = cdProduto;
		itemPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector itemPedidoList;
		if (ItemPedido.TABLE_NAME_ITEMPEDIDO.equals(tableItem)) {
			itemPedidoList = getInstance().findVlItemPedidosList(itemPedidoFilter, nuPedidoDif, Pedido.TABLE_NAME_PEDIDO);
		} else {
			itemPedidoList = getInstanceErp().findVlItemPedidosList(itemPedidoFilter, nuPedidoDif, Pedido.TABLE_NAME_PEDIDOERP);
		}
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
			if (itemPedido.vlItemPedido > 0) {
				values.put("dtEmissao", DateUtil.getDateValue(itemPedido.pedido.dtEmissao));
				values.put("hrEmissao", StringUtil.getStringValue(itemPedido.pedido.hrEmissao));
				values.put("cdCliente", StringUtil.getStringValue(itemPedidoFilter.cdCliente));
				values.put("nuPedido", StringUtil.getStringValue(itemPedido.nuPedido));
				values.put("cdProduto", StringUtil.getStringValue(itemPedidoFilter.cdProduto));
				values.put("vlItemPedido", StringUtil.getStringValue(itemPedido.vlItemPedido));
				values.put("cdUnidade", StringUtil.getStringValue(itemPedido.cdUnidade));
				values.put("vlPctDesconto", StringUtil.getStringValue(itemPedido.vlPctDesconto));
				values.put("vlPctAcrescimo", StringUtil.getStringValue(itemPedido.vlPctAcrescimo));
				if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
					values.put("qtItemFisico", itemPedido.getQtItemFisicoOrg());
					values.put("vlTotalIpiItem", itemPedido.vlTotalIpiItem);
				}
				if (LavenderePdaConfig.isIgnoraAcrescimoDesconto()) {
					values.put("vlBaseItemPedido", StringUtil.getStringValue(itemPedido.vlBaseItemPedido));
				}
				break;
			}
		}
	return values;
}

	private Vector findVlItemPedidosList(BaseDomain domain, String nuPedidoDif, String tabela) throws SQLException {
		ItemPedido itemPedidoFilter = (ItemPedido) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("select cdProduto, vlItemPedido, i.cdUnidade, i.vlPctDesconto, i.vlPctAcrescimo, p.nuPedido, p.dtEmissao, p.hrEmissao, p.cdRepresentante");
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			sql.append(", vlTotalIpiItem, qtItemFisico");
		}
		if (LavenderePdaConfig.isIgnoraAcrescimoDesconto()) {
			sql.append(" ,vlBaseItemPedido");
		}
		sql.append(" from ").append(tableName).append(" i ");
		sql.append(" JOIN ").append(tabela).append(" p ").append(" ON ")
			.append(" p.cdEmpresa = i.cdEmpresa ")
			.append(" and p.cdRepresentante = i.cdRepresentante")
			.append(" and p.nuPedido = i.nuPedido")
			.append(" and p.flOrigemPedido = i.flOrigemPedido");
		SqlWhereClause sq = new SqlWhereClause();
		sq.addAndConditionEquals(" cdProduto", itemPedidoFilter.cdProduto);
		sq.addAndConditionEquals(" p.cdCliente", itemPedidoFilter.cdCliente);
		sq.addAndConditionEquals(" p.cdEmpresa", itemPedidoFilter.cdEmpresa);
		sq.addAndConditionEquals(" p.cdRepresentante", itemPedidoFilter.cdRepresentante);
		sq.addAndConditionNotEquals(" p.nuPedido", nuPedidoDif);
		sql.append(sq.getSql());
		sql.append(" order by nuSeqProduto DESC ");
		Vector itemPedidos = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			ItemPedido itemPedido;
			while (rs.next()) {
				itemPedido = new ItemPedido();
				itemPedido.pedido = new Pedido();
				itemPedido.cdProduto = rs.getString("cdProduto");
				itemPedido.vlItemPedido = rs.getDouble("vlItemPedido");
				itemPedido.cdUnidade = rs.getString("cdUnidade");
				itemPedido.vlPctDesconto = rs.getDouble("vlPctDesconto");
				itemPedido.vlPctAcrescimo = rs.getDouble("vlPctAcrescimo");
				itemPedido.nuPedido = rs.getString("nuPedido");
				itemPedido.pedido.dtEmissao = rs.getDate("dtEmissao");
				itemPedido.pedido.hrEmissao = rs.getString("hrEmissao");
				if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
					itemPedido.setQtItemFisico(rs.getDouble("qtItemFisico"));
					itemPedido.vlTotalIpiItem = rs.getDouble("vlTotalIpiItem");
				}
				if (LavenderePdaConfig.isIgnoraAcrescimoDesconto()) {
					itemPedido.vlBaseItemPedido = rs.getDouble("vlBaseItemPedido");
				}
				itemPedidos.addElement(itemPedido);
			}
		}
		return itemPedidos;
	}

	public Vector findUltimosPedidos(String cdCliente, String nuPedidoDif, String tablePedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select p.cdEmpresa, p.cdRepresentante, p.dtEmissao, p.hrEmissao, p.nuPedido, p.cdCliente from ").append(tablePedido).append(" p ").append(" where ");
		sql.append("p.cdcliente = '");
		sql.append(cdCliente);
		sql.append("' and p.nupedido != '");
		sql.append(nuPedidoDif);
		sql.append("' order by p.dtEmissao DESC, p.hrEmissao DESC, p.nuPedido DESC ");
		Vector pedidos = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Pedido pedido;
			while (rs.next()) {
				pedido = new Pedido();
				pedido.cdEmpresa = rs.getString("cdEmpresa");
				pedido.cdRepresentante = rs.getString("cdRepresentante");
				pedido.nuPedido = rs.getString("nuPedido");
				pedido.cdCliente = rs.getString("cdCliente");
				pedido.dtEmissao = rs.getDate("dtEmissao");
				pedido.hrEmissao = rs.getString("hrEmissao");
				pedidos.addElement(pedido);
			}
		}
		return pedidos;
	}

	public Vector findItemPedidoByCliente(String nuPedido, String cdCliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDPRODUTO, tb.CDTABELAPRECO, tb.CDUNIDADE, TB.CDPRAZOPAGTOPRECO, COUNT(*) as QTDPEDIDO");
		sql.append("  from ").append(tableName).append(" tb, ");
		sql.append(getTableNamePedido()).append(" pedido ");
		sql.append(" where tb.CDEMPRESA = pedido.CDEMPRESA");
		sql.append("   and tb.NUPEDIDO = pedido.NUPEDIDO");
		sql.append("   and tb.FLORIGEMPEDIDO = pedido.FLORIGEMPEDIDO");
		sql.append("   and tb.CDREPRESENTANTE = pedido.CDREPRESENTANTE");
		sql.append("   and tb.FLTIPOITEMPEDIDO = ").append(Sql.getValue(TipoItemPedido.TIPOITEMPEDIDO_NORMAL));
		sql.append("   and pedido.CDCLIENTE = ").append(Sql.getValue(cdCliente));
		sql.append("   and pedido.NUPEDIDO <> ").append(Sql.getValue(nuPedido));
		sql.append(" group by tb.CDPRODUTO, tb.CDUNIDADE, TB.CDPRAZOPAGTOPRECO");
    	Vector itens = new Vector();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		ItemPedido itemPedido;
	    	while (rs.next()) {
	    		itemPedido = new ItemPedido();
	    		itemPedido.cdEmpresa = rs.getString("cdempresa");
	    		itemPedido.cdRepresentante = rs.getString("cdrepresentante");
	    		itemPedido.cdProduto = rs.getString("cdproduto");
	    		itemPedido.cdUnidade = rs.getString("cdUnidade");
	    		itemPedido.cdTabelaPreco = rs.getString("cdTabelaPreco");
	    		itemPedido.cdPrazoPagtoPreco = rs.getInt("cdPrazoPagtoPreco");
	    		itemPedido.qtPedidosContido = rs.getInt("qtdpedido");
	    		itens.addElement(itemPedido);
	    	}
    	}
    	return itens;
	}

	private String getTableNamePedido() {
		if (ItemPedido.TABLE_NAME_ITEMPEDIDO.equalsIgnoreCase(tableName)) {
			return Pedido.TABLE_NAME_PEDIDO;
		} else {
			return Pedido.TABLE_NAME_PEDIDOERP;
		}
	}



    //-----------------------------------------------------------------------------------------
    // Todos os métodos de busca sobrescritos, para pegar info das tabelas ItemPedido e ItemPedidoErp
    //-----------------------------------------------------------------------------------------

	//@Override
	public BaseDomain findByRowKey(String rowKey) throws java.sql.SQLException {
		StringBuffer strBuffer = getSqlBuffer();
		strBuffer.append(";").append(OrigemPedido.FLORIGEMPEDIDO_PDA).append(";");
    	if (rowKey.indexOf(strBuffer.toString()) == -1) {
    		return ItemPedidoPdbxDao.getInstanceErp().findByRowKeyErp(rowKey);
    	}
		return super.findByRowKey(rowKey);
	}

	private BaseDomain findByRowKeyErp(String rowKey) throws java.sql.SQLException {
		return super.findByRowKey(rowKey);
	}

	//@Override
	public Vector findAll() throws java.sql.SQLException {
		return VectorUtil.concatVectors(findAllSimple(),
				ItemPedidoPdbxDao.getInstanceErp().findAllSimple());
	}

	public Vector findAllSimple() throws java.sql.SQLException {
		return super.findAll();
	}

	//@Override
	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExample(domain),
				ItemPedidoPdbxDao.getInstanceErp().findAllByExampleUnique(domain));
	}

	public Vector findAllByExampleUnique(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExample(domain);
	}

	//@Override
	public Vector findAllByExampleSummary(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleSummary(domain),
				ItemPedidoPdbxDao.getInstanceErp().findAllByExampleSummaryUnique(domain));
	}

	public Vector findAllByExampleSummaryUnique(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleSummary(domain);
	}

	//@Override
	public Vector findAllInCache() throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllInCache(),
				ItemPedidoPdbxDao.getInstanceErp().findAllInCacheErp());
	}

	private Vector findAllInCacheErp() throws SQLException {
		return super.findAllInCache();
	}

	//@Override
	public Vector findAllByExampleInCache(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleInCache(domain),
				ItemPedidoPdbxDao.getInstanceErp().findAllByExampleInCacheErp(domain));
	}

	private Vector findAllByExampleInCacheErp(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleInCache(domain);
	}

	@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
    	if (!OrigemPedido.FLORIGEMPEDIDO_PDA.equals(itemPedido.flOrigemPedido)) {
    		ItemPedidoPdbxDao.getInstanceErp().deleteErp(domain);
    	} else {
        	super.delete(domain);
    	}
	}

	private void deleteErp(BaseDomain domain) throws SQLException {
    	super.delete(domain);
    }

	private Vector findQtItemFisicoByExample(BaseDomain domain) throws SQLException {
		ItemPedido itemPedidoFilter = (ItemPedido) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT QTITEMFISICO FROM ");
		sql.append(tableName);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemPedidoFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemPedidoFilter.cdRepresentante);
		sqlWhereClause.addAndConditionOr("NUPEDIDO = ", StringUtil.split(itemPedidoFilter.nuPedido, ','));
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemPedidoFilter.cdProduto);
		sql.append(sqlWhereClause.getSql());
		//--
		Vector qtTotal = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	    	while (rs.next()) {
	    		ItemPedido itemPedido = new ItemPedido();
	    		itemPedido.setQtItemFisico(ValueUtil.round(rs.getDouble("qtItemFisico")));
	    		qtTotal.addElement(itemPedido);
	    	}
		}
		return qtTotal;
	}

	public Vector findAllQtItemFisicoByExample(BaseDomain domain) throws SQLException {
		return VectorUtil.concatVectors(findQtItemFisicoByExample(domain),
				ItemPedidoPdbxDao.getInstanceErp().findQtItemFisicoByExample(domain));
	}


	public double findQtProdutoVendido(Pedido pedido, String tableName) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select sum(coalesce(QTITEMFISICO, 0) qt");
		if (LavenderePdaConfig.isConsisteConversaoUnidades()) {
			sql.setLength(0);
			sql.append("select sum(coalesce(QTITEMFATURAMENTO,0) qt");
		}
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		return getDouble(sql.toString());
	}

	public Vector findProdutoAbaixoMin(Pedido pedido) throws SQLException {
		Vector v = new Vector();
		if (pedido == null || pedido.getTabelaPreco() == null) {
			return v;
		}
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO, P.DSPRODUTO FROM ").append(tableName).append(" tb ")
		.append("JOIN TBLVPPRODUTO P ON tb.CDEMPRESA = P.CDEMPRESA AND tb.CDPRODUTO = P.CDPRODUTO ")
		.append(" AND tb.CDREPRESENTANTE = P.CDREPRESENTANTE ")
		.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante))
		.append(" AND tb.nuPedido = ").append(Sql.getValue(pedido.nuPedido))
		.append(" AND tb.QTITEMFISICO < ").append(Sql.getValue(pedido.getTabelaPreco().qtMinProduto));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Produto produto;
			while (rs.next()) {
				produto = new Produto();
				produto.cdProduto = rs.getString(1);
				produto.dsProduto = rs.getString(2);
				v.addElement(produto);
			}
		}
		return v;
	}

	public Vector findHistoricoEstoque(String cdEmpresa, String cdCliente, String cdRepresentante, final String cdProduto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.QTESTOQUECLIENTE, tb.QTITEMFISICO FROM TBLVPITEMPEDIDO tb ")
			.append("JOIN TBLVPPEDIDO ped ON tb.CDEMPRESA = ped.CDEMPRESA AND tb.CDREPRESENTANTE = ped.CDREPRESENTANTE AND tb.NUPEDIDO = ped.NUPEDIDO AND tb.FLORIGEMPEDIDO = ped.FLORIGEMPEDIDO ")
			.append("WHERE ped.CDEMPRESA = ").append(Sql.getValue(cdEmpresa))
			.append(" AND ped.CDCLIENTE = ").append(Sql.getValue(cdCliente))
			.append(" AND ped.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante))
			.append(" AND tb.CDPRODUTO = ").append(Sql.getValue(cdProduto))
			.append(" AND ped.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto))
			.append(" ORDER BY DTEMISSAO DESC, HREMISSAO DESC LIMIT 2");
		Vector itensPedidoHistoricoEstoque = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			ItemPedido itemPedidoHistorico;
			while (rs.next()) {
				itemPedidoHistorico = new ItemPedido();
				itemPedidoHistorico.setQtItemFisico(rs.getDouble("qtItemFisico"));
				itemPedidoHistorico.qtEstoqueCliente = rs.getDouble("qtEstoqueCliente");
				itensPedidoHistoricoEstoque.addElement(itemPedidoHistorico);
			}
		}
		return itensPedidoHistoricoEstoque;
	}

	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}

	public double findVlTotalItemPedidoMeta(final String cdGrupoProdutoMeta, final String cdCliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM(ITEMPEDIDO.VLTOTALITEMPEDIDO) AS VLTOTALITEMPEDIDO FROM TBLVPITEMPEDIDO ITEMPEDIDO")
		   .append(" JOIN TBLVPPEDIDO PEDIDO")
		   .append(" ON PEDIDO.CDEMPRESA = ITEMPEDIDO.CDEMPRESA")
		   .append(" AND PEDIDO.CDREPRESENTANTE = ITEMPEDIDO.CDREPRESENTANTE")
		   .append(" AND PEDIDO.NUPEDIDO = ITEMPEDIDO.NUPEDIDO")
		   .append(" AND PEDIDO.FLORIGEMPEDIDO = ITEMPEDIDO.FLORIGEMPEDIDO")
		   .append(" AND PEDIDO.CDCLIENTE = ").append(Sql.getValue(cdCliente))
		   .append(" AND (PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado))
		   .append(" OR PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoTransmitido))
		   .append(" ) AND (ITEMPEDIDO.FLTIPOITEMPEDIDO = ").append(Sql.getValue(TipoItemPedido.TIPOITEMPEDIDO_NORMAL))
		   .append(" OR ITEMPEDIDO.FLTIPOITEMPEDIDO = ").append(Sql.getValue(TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO))
		   .append(" ) JOIN TBLVPPRODUTO PRODUTO")
		   .append(" ON PRODUTO.CDEMPRESA = ITEMPEDIDO.CDEMPRESA")
		   .append(" AND PRODUTO.CDREPRESENTANTE = ITEMPEDIDO.CDREPRESENTANTE")
		   .append(" AND PRODUTO.CDPRODUTO = ITEMPEDIDO.CDPRODUTO")
		   .append(" AND PRODUTO.CDGRUPOPRODUTO1 = ").append(Sql.getValue(cdGrupoProdutoMeta));
		return getDouble(sql.toString());
	}

	public int countItemPedidoPertencemAoKit(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(CDKIT) AS QTKIT");
		sql.append(" FROM ").append(tableName).append(" tb ");
		addWhereByExample(itemPedido, sql);
		sql.append(" AND CDKIT = ").append(Sql.getValue(itemPedido.cdKit));
		if (ValueUtil.isNotEmpty(itemPedido.cdProduto)) {
			sql.append(" AND CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto));
		}
		return getInt(sql.toString());
	}

	public int countItemPedidoSemKit(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(CDKIT) AS QTKIT");
		sql.append(" FROM ").append(tableName).append( " tb ");
		addWhereByExample(itemPedido, sql);
		sql.append(" AND (CDKIT IS NULL OR CDKIT = '') ");
		return getInt(sql.toString());
	}

	public int countItensPertencentesCombo(ItemPedido filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPITEMPEDIDO tb")
		.append(" JOIN TBLVPITEMCOMBO itemcombo ON")
		.append(" tb.CDEMPRESA = itemcombo.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = itemcombo.CDREPRESENTANTE AND")
		.append(" tb.CDPRODUTO = itemcombo.CDPRODUTO")
		.append(" JOIN TBLVPCOMBO combo ON")
		.append(" tb.CDEMPRESA = combo.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = combo.CDREPRESENTANTE AND")
		.append(" itemcombo.CDCOMBO = combo.CDCOMBO");
		addWhereByExample(filter, sql);
		sql.append(" AND ((combo.DTVIGENCIAINICIAL <= date('now', 'localtime') OR (combo.DTVIGENCIAINICIAL IS NULL OR combo.DTVIGENCIAINICIAL = ''))")
		.append(" AND (combo.DTVIGENCIAFINAL >= date('now', 'localtime') OR (combo.DTVIGENCIAFINAL IS NULL OR combo.DTVIGENCIAFINAL = '')))");
		return getInt(sql.toString());
	}

	private boolean needAddVlUnidadePadraoAndBaseEmbElementar() {
		return LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa
			|| LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()
			|| (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && LavenderePdaConfig.usaUnidadeAlternativa)
			|| (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() && LavenderePdaConfig.usaUnidadeAlternativa);
	}

	public Vector findItensByComplementar(ItemPedido complementar) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO, VLITEMPEDIDO FROM TBLVPITEMPEDIDO tb ")
		.append(" JOIN TBLVPPRODUTOCOMPLEMENTO comp ON ")
		.append(" tb.CDEMPRESA = comp.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = comp.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = comp.CDPRODUTO")
		.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(complementar.cdEmpresa)).append(" AND ")
		.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(complementar.cdRepresentante)).append(" AND ")
		.append(" tb.NUPEDIDO = ").append(Sql.getValue(complementar.nuPedido)).append(" AND ")
		.append(" tb.FLORIGEMPEDIDO = ").append(Sql.getValue(complementar.flOrigemPedido)).append(" AND ")
		.append(" comp.CDPRODUTOCOMPLEMENTO = ").append(Sql.getValue(complementar.cdProduto));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector(10);
			ItemPedido itemPedido;
			while (rs.next()) {
				itemPedido = new ItemPedido();
				itemPedido.cdProduto = rs.getString(1);
				itemPedido.vlItemPedido = rs.getDouble(2);
				list.addElement(itemPedido);
			}
			return list;
		}
	}

	public void updateVlVpcItensPedido(Pedido pedido, double vlPctVpc) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName);
		sql.append(" SET VLVPC = (VLTOTALITEMPEDIDO * ").append(Sql.getValue(vlPctVpc / 100));
		sql.append(" ) WHERE NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" AND ");
		sql.append("CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND ");
		sql.append("CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		executeUpdate(sql.toString());
	}

	public Vector findCdProdutoComPedidoNaoEnviadoList() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT (ITEMPEDIDO.CDPRODUTO) CDPRODUTO FROM TBLVPITEMPEDIDO ITEMPEDIDO ");
		sql.append("JOIN TBLVPPEDIDO PEDIDO ON ");
		sql.append("PEDIDO.CDEMPRESA = ITEMPEDIDO.CDEMPRESA AND ");
		sql.append("PEDIDO.CDREPRESENTANTE = ITEMPEDIDO.CDREPRESENTANTE AND ");
		sql.append("PEDIDO.NUPEDIDO = ITEMPEDIDO.NUPEDIDO AND ");
		sql.append("PEDIDO.FLORIGEMPEDIDO = ITEMPEDIDO.FLORIGEMPEDIDO ");
		sql.append("AND (PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		sql.append(" OR PEDIDO.CDSTATUSPEDIDO = ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoFechado)).append(")");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			while (rs.next()) {
				list.addElement(rs.getString(1));
			}
			return list;
		}
	}

	public int countItemComPrecoLiberadoSenha(Pedido pedido) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT COUNT(1) AS QTITEMLIBERADOSENHA FROM TBLVPITEMPEDIDO ");
		sql.append("WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append("AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append("AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append("AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append("AND FLPRECOLIBERADOSENHA = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		return getInt(sql.toString());
	}

	public void updateValuesVerba(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE TBLVPITEMPEDIDO SET VLVERBAITEM = ").append(Sql.getValue(itemPedido.vlVerbaItem))
		.append(", VLVERBAGRUPOITEM = ").append(Sql.getValue(itemPedido.vlVerbaGrupoItem))
		.append(", VLTOLERANCIAVERGRUSALDO = ").append(Sql.getValue(itemPedido.vlToleranciaVerGruSaldo))
		.append(" WHERE CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND ")
		.append("CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ")
		.append("NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND ")
		.append("FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND ")
		.append("CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto)).append(" AND ")
		.append("FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemPedido.flTipoItemPedido)).append(" AND ")
		.append("NUSEQPRODUTO = ").append(Sql.getValue(itemPedido.nuSeqProduto));;
		executeUpdate(sql.toString());
	}

	public void updatePontuacaoItemPedido(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(tableName)
           .append(" SET VLPONTUACAOBASEITEM = ").append(Sql.getValue(itemPedido.vlPontuacaoBaseItem))
           .append(", VLPONTUACAOREALIZADOITEM = ").append(Sql.getValue(itemPedido.vlPontuacaoRealizadoItem))
           .append(", VLPESOPONTUACAO = ").append(Sql.getValue(itemPedido.vlPesoPontuacao))
           .append(", VLFATORCORRECAOFAIXAPRECO = ").append(Sql.getValue(itemPedido.vlFatorCorrecaoFaixaPreco))
           .append(", VLFATORCORRECAOFAIXADIAS = ").append(Sql.getValue(itemPedido.vlFatorCorrecaoFaixaDias))
           .append(", VLPCTFAIXAPRECOPONTUACAO = ").append(Sql.getValue(itemPedido.vlPctFaixaPrecoPontuacao))
           .append(", VLBASEPONTUACAO = ").append(Sql.getValue(itemPedido.vlBasePontuacao))
		   .append(", CDPONTUACAOCONFIG = ").append(Sql.getValue(itemPedido.cdPontuacaoConfig))
		   .append(", QTDIASFAIXAPONTUACAO = ").append(Sql.getValue(itemPedido.qtDiasFaixaPontuacao))
           .append(" WHERE ROWKEY = ").append(Sql.getValue(itemPedido.getRowKey()));
        executeUpdate(sql.toString());
	}
	
	public double getVlPctUltimoDescontoAplicadoNoProdutoNesteCliente(Pedido pedido, String cdProduto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ITEMPEDIDOERP.VLPCTDESCONTO FROM TBLVPITEMPEDIDOERP ITEMPEDIDOERP")
		.append(" JOIN TBLVPPEDIDOERP PEDIDOERP ON ITEMPEDIDOERP.CDEMPRESA = PEDIDOERP.CDEMPRESA")
		.append(" AND ITEMPEDIDOERP.CDREPRESENTANTE = PEDIDOERP.CDREPRESENTANTE AND ITEMPEDIDOERP.FLORIGEMPEDIDO = PEDIDOERP.FLORIGEMPEDIDO AND ITEMPEDIDOERP.NUPEDIDO = PEDIDOERP.NUPEDIDO")
		.append(" WHERE ITEMPEDIDOERP.CDPRODUTO = ").append(Sql.getValue(cdProduto))
		.append(" AND PEDIDOERP.CDCLIENTE = ").append(Sql.getValue(pedido.cdCliente))
		.append(" AND ITEMPEDIDOERP.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante))
		.append(" AND ITEMPEDIDOERP.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa))
		.append(" ORDER BY PEDIDOERP.DTEMISSAO DESC LIMIT 1");
		return getDouble(sql.toString());
	}

	public Vector findItemPedidoPendenteList(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ")
		.append(" ITEMPEDIDO.NUORDEMLIBERACAO, ")
		.append("  MOTIVOPENDENCIA.FLREGRALIBERACAO ")
		.append(" FROM ").append(tableName).append(" ITEMPEDIDO ")
		.append(" LEFT JOIN TBLVPMOTIVOPENDENCIA MOTIVOPENDENCIA ")
		.append(" ON MOTIVOPENDENCIA.CDEMPRESA = ITEMPEDIDO.CDEMPRESA ")
		.append(" AND MOTIVOPENDENCIA.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(MotivoPendencia.class))) 
		.append(" AND MOTIVOPENDENCIA.CDMOTIVOPENDENCIA = ITEMPEDIDO.CDMOTIVOPENDENCIA ") 
		.append(" WHERE ITEMPEDIDO.CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa))
		.append(" AND ITEMPEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante))
		.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido))
		.append(" AND ITEMPEDIDO.NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido))
		.append(" AND ITEMPEDIDO.FLPENDENTE = ").append(Sql.getValue(ValueUtil.VALOR_SIM)) 
		.append(" GROUP BY ITEMPEDIDO.NUORDEMLIBERACAO, MOTIVOPENDENCIA.FLREGRALIBERACAO ")
		.append(" ORDER BY ITEMPEDIDO.NUORDEMLIBERACAO, MOTIVOPENDENCIA.FLREGRALIBERACAO ");
	
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector itemPedidoPendenteList = new Vector();
			while (rs.next()) {
				itemPedidoPendenteList.addElement(new ItemPedido(rs.getInt(1), rs.getString(2)));
			}
			return itemPedidoPendenteList;
		}
	}
	
	public Vector findProdutosPendentesByItemPedido(ItemPedido itemPedidoFilter) throws SQLException {
		boolean isSupervisor = SessionLavenderePda.isUsuarioSupervisor();
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ")
		.append(" ITEMPEDIDO.CDEMPRESA,")
		.append(" ITEMPEDIDO.CDREPRESENTANTE,")
		.append(" ITEMPEDIDO.FLORIGEMPEDIDO,")
		.append(" ITEMPEDIDO.NUPEDIDO,")
		.append(" ITEMPEDIDO.FLTIPOITEMPEDIDO,")
		.append(" ITEMPEDIDO.NUSEQPRODUTO,")
		.append(" PRODUTO.CDPRODUTO, ")
		.append(" PRODUTO.DSPRODUTO, ")
		.append(" MOTIVOPENDENCIA.CDMOTIVOPENDENCIA,")
		.append(" MOTIVOPENDENCIA.DSMOTIVOPENDENCIA");
		if (isSupervisor) {
			sql.append(" FROM TBLVPITEMPEDIDOERP ITEMPEDIDO ");
		} else {
			sql.append(" FROM ").append(tableName).append(" ITEMPEDIDO ");
		}
		sql.append(" LEFT JOIN TBLVPMOTIVOPENDENCIA MOTIVOPENDENCIA")
		.append(" ON MOTIVOPENDENCIA.CDEMPRESA = ITEMPEDIDO.CDEMPRESA");
		if (isSupervisor) {
			sql.append(" AND MOTIVOPENDENCIA.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(getClass())));
		} else {
			sql.append(" AND MOTIVOPENDENCIA.CDREPRESENTANTE = ITEMPEDIDO.CDREPRESENTANTE");			
		}
		sql.append(" AND MOTIVOPENDENCIA.CDMOTIVOPENDENCIA = ITEMPEDIDO.CDMOTIVOPENDENCIA ") 
		.append(" JOIN TBLVPPRODUTO PRODUTO ")
		.append(" ON ITEMPEDIDO.CDEMPRESA = PRODUTO.CDEMPRESA ");
		if (isSupervisor) {
			sql.append(" AND PRODUTO.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(getClass())));
		} else {
			sql.append(" AND ITEMPEDIDO.CDREPRESENTANTE = PRODUTO.CDREPRESENTANTE ");
		}
		sql.append(" AND ITEMPEDIDO.CDPRODUTO = PRODUTO.CDPRODUTO")
		.append(" WHERE ITEMPEDIDO.CDEMPRESA = ").append(Sql.getValue(itemPedidoFilter.cdEmpresa))
		.append(" AND ITEMPEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(itemPedidoFilter.cdRepresentante))
		.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedidoFilter.flOrigemPedido))
		.append(" AND ITEMPEDIDO.NUPEDIDO = ").append(Sql.getValue(itemPedidoFilter.nuPedido))
		.append(" AND (ITEMPEDIDO.CDMOTIVOPENDENCIA IS NOT NULL AND ITEMPEDIDO.CDMOTIVOPENDENCIA <> '')");
		addOrderBy(sql, itemPedidoFilter);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector itemPedidoPendenteList = new Vector();
			Produto produto;
			ItemPedido itemPedido;
			MotivoPendencia motivoPendenciaAtual;
			while (rs.next()) {
				produto = new Produto();
				itemPedido = new ItemPedido();
				produto.cdProduto = rs.getString("cdProduto");
				produto.dsProduto = rs.getString("dsProduto");
				itemPedido.setProduto(produto);
				itemPedido.cdEmpresa = rs.getString("cdEmpresa");
				itemPedido.cdRepresentante = rs.getString("cdRepresentante");
				itemPedido.flOrigemPedido = rs.getString("flOrigemPedido");
				itemPedido.nuPedido = rs.getString("nuPedido");
				itemPedido.flTipoItemPedido = rs.getString("flTipoItemPedido");
				itemPedido.nuSeqProduto = rs.getInt("nuSeqProduto");
				motivoPendenciaAtual = new MotivoPendencia();
				motivoPendenciaAtual.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
				motivoPendenciaAtual.dsMotivoPendencia = rs.getString("dsMotivoPendencia");
				itemPedido.motivoPendencia = motivoPendenciaAtual;
				itemPedidoPendenteList.addElement(itemPedido);
			}
			return itemPedidoPendenteList;
		}
	}

	public void liberaItemPedidoPorOrdemLiberacao(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE TBLVPITEMPEDIDOERP ")
		.append( " SET FLPENDENTE = ").append(Sql.getValue(ValueUtil.VALOR_NAO))
		.append( " WHERE CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa))
		.append( " AND FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido))
		.append( " AND CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante))
		.append( " AND NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido))
		.append( " AND NUORDEMLIBERACAO = ").append(Sql.getValue(itemPedido.nuOrdemLiberacao));
		executeUpdate(sql.toString());
	}

	public int countItensPedidoPendenteLiberacao(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT COUNT(NUPEDIDO) QTD ")
		.append(" FROM TBLVPITEMPEDIDOERP ")
		.append( " WHERE CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa))
		.append( " AND FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido))
		.append( " AND CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante))
		.append( " AND NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido))
		.append( " AND FLPENDENTE = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		return getInt(sql.toString());
	}

	public Vector findItemPedidoComMargemRentabilidade(ItemPedido itemPedidoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT  DISTINCT(CDMARGEMRENTAB) AS CDMARGEMRENTAB ");
		sql.append(" FROM ").append(ItemPedido.TABLE_NAME_ITEMPEDIDO)
		.append(" WHERE CDEMPRESA = ").append(Sql.getValue(itemPedidoFilter.cdEmpresa))
		.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(itemPedidoFilter.cdRepresentante))
		.append(" AND NUPEDIDO = ").append(Sql.getValue(itemPedidoFilter.nuPedido))
		.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedidoFilter.flOrigemPedido))
		.append(" AND CDMARGEMRENTAB IS NOT NULL AND CDMARGEMRENTAB <> '' ");
		Vector itemPedidoList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoFilter.clone();
				itemPedido.cdMargemRentab = rs.getString("CDMARGEMRENTAB");
				itemPedidoList.addElement(itemPedido);
			}
		}
		return itemPedidoList;
	}
	
	public void marcaItemPedidoPorRentabilidadeAgrupado(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE TBLVPITEMPEDIDO ")
		.append( " SET FLPENDENTE = ").append(Sql.getValue(itemPedido.flPendente))
		.append( " , CDMOTIVOPENDENCIA = ").append(Sql.getValue(itemPedido.cdMotivoPendencia))
		.append( " , NUORDEMLIBERACAO = ").append(Sql.getValue(itemPedido.nuOrdemLiberacao))
		.append( " WHERE CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa))
		.append( " AND CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante))
		.append( " AND NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido))
		.append( " AND FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido))
		.append( " AND CDMARGEMRENTAB = ").append(Sql.getValue(itemPedido.cdMargemRentab));
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public Vector findCdClasseListByPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sql.append("SELECT distinct prod.CDCLASSE FROM TBLVPITEMPEDIDO tb");
		sql.append(" JOIN TBLVPPRODUTO prod ON tb.CDEMPRESA = prod.CDEMPRESA AND tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND tb.CDPRODUTO = prod.CDPRODUTO");
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", pedido.nuPedido);
		sql.append(sqlWhereClause.getSql());
		Vector cdClasseList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				String cdClasseCdTabPreco = rs.getString(1);
				cdClasseList.addElement(cdClasseCdTabPreco);
			}
			return cdClasseList;
		}
	}

	public HashMap<String, Double> getQtItensByCdClasseNoPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM(tb.QTITEMFISICO) as QTITEMFISICO, prod.CDCLASSE FROM TBLVPITEMPEDIDO tb");
		DaoUtil.addJoinProduto(sql, "prod", false);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", pedido.nuPedido);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", pedido.flOrigemPedido);
		sql.append(sqlWhereClause.getSql());
		sql.append(" GROUP BY prod.CDCLASSE");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			HashMap<String, Double> cdClasseSumQtItembyClasseHash = new HashMap<>();
			while (rs.next()) {
				double sumQtItem = rs.getDouble(1);
				String cdClasse = rs.getString(2);
				cdClasseSumQtItembyClasseHash.put(cdClasse, sumQtItem);
			}
			return cdClasseSumQtItembyClasseHash;
		}
	}

	public boolean isPedidoPossuiItensComboSecundario(ItemPedido itemPedido, String cdCombo, boolean avulso, boolean beforeDelete) throws SQLException {
		return getInt(getSqlItemComboNoPedido(itemPedido, cdCombo, avulso, beforeDelete, null, false).toString()) > 0;
	}

	private StringBuffer getSqlItemComboNoPedido(ItemPedido itemPedido, String cdCombo, boolean avulso, boolean beforeDelete, Vector listItensSecundarioSelecionados, boolean usaItemComboSimilar) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ").append(avulso ? "tb.CDCOMBO" : "COUNT(1)").append(" FROM TBLVPITEMPEDIDO tb");
		String cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		if (usaItemComboSimilar) {
			sql.append(" LEFT JOIN TBLVPITEMCOMBOAGRSIMILAR itcs ON ")
			.append(" ITCS.CDEMPRESA = tb.CDEMPRESA ")
			.append(" AND ITCS.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
			if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
				sql.append(" AND ITCS.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
			}
			sql.append(" AND ITCS.CDPRODUTOSIMILAR = tb.CDPRODUTO ")
			.append(" AND ITCS.CDCOMBO = ").append(Sql.getValue(cdCombo));
		}
		sql.append(" JOIN TBLVPITEMCOMBO ITC ON")
		.append(" ITC.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" ITC.CDREPRESENTANTE = tb.CDREPRESENTANTE AND");
		if (usaItemComboSimilar) {
			sql.append(" (ITC.CDPRODUTO = tb.CDPRODUTO OR ITCS.CDPRODUTO = ITC.CDPRODUTO)");	
		} else {
			sql.append(" (ITC.CDPRODUTO = tb.CDPRODUTO)");
		}
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			sql.append(" AND ITC.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
		}
		if (!avulso) {
			sql.append(" AND ITC.FLTIPOITEMCOMBO = 'S'");
		}
		sql.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND")
		.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND")
		.append(" tb.NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND")
		.append(" tb.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND")
		.append(" ITC.CDCOMBO = ").append(Sql.getValue(cdCombo)).append(" AND")
		.append(" (ITC.FLTIPOITEMCOMBO = 'P' ");
		if (ValueUtil.isNotEmpty(listItensSecundarioSelecionados)) {
			sql.append(" OR (ITC.FLTIPOITEMCOMBO = 'S' AND ITC.CDPRODUTO = ").append(Sql.getValue(((ItemCombo)listItensSecundarioSelecionados.items[0]).cdProduto)).append(")");
		}
		sql.append(")");
		if (avulso) {
			sql.append(" AND").append(" (COALESCE(tb.CDCOMBO, '') <> ITC.CDCOMBO)");
		} else { 
			sql.append(" AND").append(" (COALESCE(tb.CDCOMBO, '') = ITC.CDCOMBO)");
		}
		if (beforeDelete) {
			sql.append(" AND tb.CDPRODUTO <> ").append(Sql.getValue(itemPedido.cdProduto));
		}
		return sql;
	}
	
	public int isPedidoPossuiItensComboAvulsos(ItemPedido itemPedido, String cdCombo, boolean beforeDelete, Vector listItensSecundarioSelecionados, Vector listItensSimilares) throws SQLException {
		StringBuffer sql = getSqlItemComboNoPedido(itemPedido, cdCombo, true, beforeDelete, listItensSecundarioSelecionados, LavenderePdaConfig.usaAgrupadorSimilaridadeProduto);
		if (ValueUtil.isNotEmpty(listItensSimilares)) {
			addNotInSimilares(sql, listItensSimilares);
		}
		int tipoRetorno = ItemCombo.TIPO_SEM_AVULSO;
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				if (ValueUtil.isNotEmpty(rs.getString(1))) {
					return ItemCombo.TIPO_ENCONTROU_OUTRA_COMBO;
				}
				tipoRetorno = ItemCombo.TIPO_ENCONTROU_AVULSO;
			}
		}
		return tipoRetorno;
	}
	
	private void addNotInSimilares(StringBuffer sql, Vector listItensSimilares) {
		sql.append(" AND tb.CDPRODUTO IN (");
		int size = listItensSimilares.size();
		for (int i = 0; i < size; i++) {
			sql.append(Sql.getValue(((Produto) listItensSimilares.items[i]).cdProduto)).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
	}

	public boolean isPedidoPossuiItemSimilarSecundario(ItemPedido itemPedido, String cdCombo, boolean beforeDelete) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPPRODUTO tb")
		.append(" JOIN TBLVPITEMPEDIDO itemP ON")
		.append(" itemP.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" itemP.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
		.append(" itemP.CDPRODUTO = tb.CDPRODUTO")
		.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND")
		.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND")
		.append(" itemP.NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND")
		.append(" itemP.FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND")
		.append(" tb.CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(itemPedido.getProduto().cdAgrupadorSimilaridade));
		if (beforeDelete) {
			sql.append(" AND tb.CDPRODUTO <> ").append(Sql.getValue(itemPedido.cdProduto));
		}
		sql.append(" AND EXISTS (SELECT 1 FROM TBLVPITEMCOMBO itemC")
		.append(" JOIN TBLVPPRODUTO prod ON")
		.append(" prod.CDEMPRESA = itemC.CDEMPRESA AND")
		.append(" prod.CDREPRESENTANTE = itemC.CDREPRESENTANTE AND")
		.append(" prod.CDPRODUTO = itemC.CDPRODUTO AND")
		.append(" prod.CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(itemPedido.getProduto().cdAgrupadorSimilaridade));
		ItemCombo itemComboFilter = ItemComboService.getInstance().getItemComboFilter(itemPedido);
		itemComboFilter.flTipoItemCombo = ItemCombo.TIPOITEMCOMBO_SECUNDARIO;
		DaoUtil.addJoinItemComboAgrSimilar(sql, ItemComboAgrSimilarService.getInstance().getItemComboAgrSimilarFilterByItemCombo(itemComboFilter));
		sql.append(" WHERE itemC.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" itemC.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
		.append(" itemC.CDCOMBO = ").append(Sql.getValue(cdCombo)).append(" AND");
		String cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			sql.append(" itemC.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco)).append(" AND");
		}
		sql.append(" itemC.FLTIPOITEMCOMBO = ").append(Sql.getValue(ItemCombo.TIPOITEMCOMBO_SECUNDARIO)).append(")");
		return getInt(sql.toString()) > 0;
	}

	public void updateCdProdutoClienteItensPedido(String cdCliente, ItemPedido itemPedidoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE TBLVPITEMPEDIDO")
		.append(" SET CDPRODUTOCLIENTE = (SELECT prodclicod.CDPRODUTOCLIENTE")
		.append(" FROM TBLVPPEDIDO ped, TBLVPPRODUTOCLIENTECOD prodclicod")
		.append(" WHERE ped.CDEMPRESA = TBLVPITEMPEDIDO.CDEMPRESA")
		.append(" AND ped.CDREPRESENTANTE = TBLVPITEMPEDIDO.CDREPRESENTANTE")
		.append(" AND ped.NUPEDIDO = TBLVPITEMPEDIDO.NUPEDIDO")
		.append(" AND ped.FLORIGEMPEDIDO = TBLVPITEMPEDIDO.FLORIGEMPEDIDO")
		.append(" AND prodclicod.CDEMPRESA = TBLVPITEMPEDIDO.CDEMPRESA")
		.append(" AND prodclicod.CDREPRESENTANTE = TBLVPITEMPEDIDO.CDREPRESENTANTE")
		.append(" AND prodclicod.CDPRODUTO = TBLVPITEMPEDIDO.CDPRODUTO")
		.append(" AND prodclicod.CDCLIENTE =").append(Sql.getValue(cdCliente))
		.append(" AND ped.NUPEDIDO =").append(Sql.getValue(itemPedidoFilter.nuPedido))
		.append(" AND ped.CDEMPRESA =").append(Sql.getValue(itemPedidoFilter.cdEmpresa))
		.append(" AND ped.CDREPRESENTANTE =").append(Sql.getValue(itemPedidoFilter.cdRepresentante))
		.append(" AND ped.FLORIGEMPEDIDO =").append(Sql.getValue(itemPedidoFilter.flOrigemPedido)).append(")");
		sql.append(" WHERE EXISTS (SELECT prodclicod.CDPRODUTOCLIENTE")
		.append(" FROM TBLVPPEDIDO ped, TBLVPPRODUTOCLIENTECOD prodclicod")
		.append(" WHERE ped.CDEMPRESA = TBLVPITEMPEDIDO.CDEMPRESA")
		.append(" AND ped.CDREPRESENTANTE = TBLVPITEMPEDIDO.CDREPRESENTANTE")
		.append(" AND ped.NUPEDIDO = TBLVPITEMPEDIDO.NUPEDIDO")
		.append(" AND ped.FLORIGEMPEDIDO = TBLVPITEMPEDIDO.FLORIGEMPEDIDO")
		.append(" AND prodclicod.CDEMPRESA = TBLVPITEMPEDIDO.CDEMPRESA")
		.append(" AND prodclicod.CDREPRESENTANTE = TBLVPITEMPEDIDO.CDREPRESENTANTE")
		.append(" AND prodclicod.CDPRODUTO = TBLVPITEMPEDIDO.CDPRODUTO")
		.append(" AND prodclicod.CDCLIENTE =").append(Sql.getValue(cdCliente))
		.append(" AND ped.NUPEDIDO =").append(Sql.getValue(itemPedidoFilter.nuPedido))
		.append(" AND ped.CDEMPRESA =").append(Sql.getValue(itemPedidoFilter.cdEmpresa))
		.append(" AND ped.CDREPRESENTANTE =").append(Sql.getValue(itemPedidoFilter.cdRepresentante))
		.append(" AND ped.FLORIGEMPEDIDO =").append(Sql.getValue(itemPedidoFilter.flOrigemPedido)).append(")");
		try {
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void getSqlDescProgressivoPersonalizadoExtrapolados(Pedido pedido, StringBuffer sql) {
		sql.append(DescProgressivoConfig.TABLE_NAME).append(" descProg ")
				.append(" ON tb.CDEMPRESA = descProg.CDEMPRESA")
				.append(" AND tb.CDREPRESENTANTE = descProg.CDREPRESENTANTE")
				.append(" AND tb.CDDESCPROGRESSIVO = descProg.CDDESCPROGRESSIVO")
				.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa))
				.append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante))
				.append(" AND tb.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido))
				.append(" AND tb.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido))
				.append(" AND descProg.DTFIMVIGENCIA < ").append(Sql.getValue(DateUtil.getCurrentDate()));
	}

	public boolean isPossuiItensDescProgressivoPersonalizadoExtrapolados(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM ").append(ItemPedido.TABLE_NAME_ITEMPEDIDO).append(" tb JOIN ");
		getSqlDescProgressivoPersonalizadoExtrapolados(pedido, sql);
		return getInt(sql.toString()) > 0;
	}

	public Vector findCdProdutosDescProgressivoPersonalizadoExtrapolados(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO FROM ").append(ItemPedido.TABLE_NAME_ITEMPEDIDO).append(" tb JOIN ");
		getSqlDescProgressivoPersonalizadoExtrapolados(pedido, sql);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(rs.getString(1));
			}
			return list;
		}
	}
	private void getSqlDescQtdVencidos(Pedido pedido, StringBuffer sql) throws SQLException {
		sql.append(" TBLVPDESCQUANTIDADE dq ON tb.CDEMPRESA = dq.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = dq.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = dq.CDPRODUTO AND ")
		.append(" dq.CDTABELAPRECO = ").append(Sql.getValue(TabelaPrecoService.getInstance().getCdTabelaPreco(pedido))).append(" AND ")
		.append(" tb.QTITEMFISICO >= dq.QTITEM ")
		.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND ")
		.append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante)).append(" AND ")
		.append(" tb.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" AND ")
		.append(" tb.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido)).append(" AND ")
		.append(" tb.VLPCTFAIXADESCQTD > 0 AND ")
		.append(" dq.DTFIMVIGENCIA < ").append(Sql.getValue(DateUtil.getCurrentDate()));
	}

	public int getMaxNuOrdemLiberacaoItemPendente() throws SQLException {
		return getInt("SELECT MAX(NUORDEMLIBERACAO) AS MAXNUORDEMLIBERACAO FROM TBLVPITEMPEDIDOERP WHERE FLPENDENTE = 'S' AND CDMOTIVOPENDENCIA IS NOT NULL");
	}
	
	public boolean isPossuiProdutosDescQtdVencidos(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPITEMPEDIDO tb JOIN ");
		getSqlDescQtdVencidos(pedido, sql);
		return getInt(sql.toString()) > 0;
	}

	public Vector findCdProdutosDescQtdVencidos(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO FROM TBLVPITEMPEDIDO tb JOIN ");
		getSqlDescQtdVencidos(pedido, sql);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(rs.getString(1));
			}
			return list;
		}
	}
	
	public void updateFlPedidoPerdidoByPedido(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName);
		sql.append(" SET FLPEDIDOPERDIDO = ").append(Sql.getValue(itemPedido.flPedidoPerdido));
		sql.append(" WHERE NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND ");
		sql.append("CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND ");
		sql.append("CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ");
		sql.append("FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido));
		executeUpdate(sql.toString());
	}
	
	public Vector findItemPedidoConsomeDescProgList(Pedido pedido, String cdDescProgressivo) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO FROM TBLVPITEMPEDIDO tb ")
		.append("JOIN TBLVPPRODUTO prod ON ")
		.append(" tb.CDEMPRESA = prod.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = prod.CDPRODUTO ")
		.append(" JOIN TBLVPDESCPROGCONFIGFAM dpcf ON ")
		.append(" dpcf.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" dpcf.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" dpcf.CDFAMILIADESCPROG = prod.CDFAMILIADESCPROG AND ")
		.append(" dpcf.CDDESCPROGRESSIVO = ").append(Sql.getValue(cdDescProgressivo))
		.append(" AND dpcf.FLTIPOFAMILIACON = 'S'");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", pedido.nuPedido);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", pedido.flOrigemPedido);
		sql.append(sqlWhereClause.getSql())
		.append(DescProgressivoConfigDbxDao.addItemPedidoRequirements("tb"));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			while (rs.next()) {
				list.addElement(rs.getString(1));
			}
			return list;
		}
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		ItemPedido filter = (ItemPedido) domainFilter;
		if (LavenderePdaConfig.mostraColecaoNosDetalhes() || LavenderePdaConfig.mostraStatusColecaoNosDetalhes()
				|| LavenderePdaConfig.mostraDimensoesNosDetalhes() || LavenderePdaConfig.usaGradeProduto5() || filter.isBuscaFlKitProduto) {
			sql.append(" LEFT JOIN TBLVPPRODUTO ").append(DaoUtil.ALIAS_PRODUTO)
					.append(" ON TB.CDEMPRESA = ").append(DaoUtil.ALIAS_PRODUTO).append(".CDEMPRESA")
					.append(" AND TB.CDREPRESENTANTE = ").append(DaoUtil.ALIAS_PRODUTO).append(".CDREPRESENTANTE")
					.append(" AND TB.CDPRODUTO = ").append(DaoUtil.ALIAS_PRODUTO).append(".CDPRODUTO");
			if (LavenderePdaConfig.mostraColecaoNosDetalhes()) {
				sql.append(" LEFT JOIN TBLVPCOLECAO ").append(DaoUtil.ALIAS_COLECAO)
						.append(" ON PROD.CDEMPRESA = ").append(DaoUtil.ALIAS_COLECAO).append(".CDEMPRESA")
						.append(" AND PROD.CDMARCA = ").append(DaoUtil.ALIAS_COLECAO).append(".CDMARCA")
						.append(" AND PROD.CDCOLECAO = ").append(DaoUtil.ALIAS_COLECAO).append(".CDCOLECAO");
			}
			if (LavenderePdaConfig.mostraStatusColecaoNosDetalhes()) {
				sql.append(" LEFT JOIN TBLVPCOLECAOSTATUS ").append(DaoUtil.ALIAS_COLECAO_STATUS)
						.append(" ON PROD.CDEMPRESA = ").append(DaoUtil.ALIAS_COLECAO_STATUS).append(".CDEMPRESA")
						.append(" AND PROD.CDSTATUSCOLECAO = ").append(DaoUtil.ALIAS_COLECAO_STATUS).append(".CDSTATUSCOLECAO");
			}
		}
		if (filter.buscarItensConsumindoVerba && ValueUtil.isNotEmpty(filter.cdGrupoProduto1)) {
			DaoUtil.addJoinProduto(sql, "prod", false);
			DaoUtil.addJoinPedidoByStatus(sql, filter, LavenderePdaConfig.cdStatusPedidoAberto);
		}
		if (LavenderePdaConfig.isConfigGradeProduto() && filter != null && ValueUtil.isNotEmpty(filter.dsAgrupadorGradeFilter)) {
			DaoUtil.addJoinProdutoGrade(sql);
		}
		if (LavenderePdaConfig.relDiferencasPedido && filter != null && filter.isOrigemErp()) {
			DaoUtil.addJoinItemPedidoErpDif(sql);
		}
		addJoinDescQT(domainFilter, sql);
		if (LavenderePdaConfig.isConfigStatusItemPedido()) {
			DaoUtil.addJoinStatusItemPedido(sql);
		}
	}
	
	public void updateLimpaFlAutorizadoItemPedido(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
		.append(" SET FLAUTORIZADO = ").append(Sql.getValue(itemPedido.flAutorizado));
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemPedido.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemPedido.flOrigemPedido);
		sql.append(sqlWhereClause.getSql());
		executeUpdate(sql.toString());
	}

	public void updateItemPendentePoliticaComercialReloadCondicaoPagamento(ItemPedido itemPedido) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("UPDATE ").append(tableName);
    	sql.append(" SET FLPENDENTE = ").append(Sql.getValue(itemPedido.flPendente));
    	sql.append(" , CDPOLITICACOMERCIAL = ").append(Sql.getValue(itemPedido.cdPoliticaComercial));
    	sql.append(" , VLPCTCOMISSAO = ").append(Sql.getValue(itemPedido.vlPctComissao));
    	sql.append(" , VLPCTPOLITICACOMERCIAL = ").append(Sql.getValue(itemPedido.vlPctPoliticaComercial));
    	sql.append(" , VLPCTDESCONTO = ").append(Sql.getValue(itemPedido.vlPctDesconto));
    	sql.append(" , VLPCTACRESCIMO = ").append(Sql.getValue(itemPedido.vlPctAcrescimo));
    	sql.append(" , VLITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlItemPedido));
    	sql.append(" , VLTOTALITEMPEDIDO = ").append(Sql.getValue(itemPedido.vlTotalItemPedido));
		sql.append(" WHERE NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND ");
		sql.append("CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND ");
		sql.append("CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND ");
		sql.append("FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND ");
		sql.append("CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto)).append(" AND ");
		sql.append("FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemPedido.flTipoItemPedido)).append(" AND ");
		sql.append("NUSEQPRODUTO = ").append(itemPedido.nuSeqProduto);
		executeUpdate(sql.toString());
	}

	public Vector findAllByExamplePedidoComBonificacao(BaseDomain domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(domain, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(domain, sql);
		addWhereByExample(domain, sql);
		addGroupBy(sql);
		sql.append("ORDER BY tb.FLTIPOITEMPEDIDO = 'V' DESC");
		addLimit(sql, domain);
		return findAll(domain, sql.toString());
	}

	private void addJoinDescQT(BaseDomain domainFilter, StringBuffer sql) {
		if (domainFilter != null && ((ItemPedido) domainFilter).isJoinDescQt && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			sql.append(" LEFT JOIN ");
			sql.append(" TBLVPDESCQUANTIDADE DQ ON");
			sql.append(" DQ.CDEMPRESA = TB.CDEMPRESA");
			sql.append(" AND DQ.CDREPRESENTANTE = TB.CDREPRESENTANTE");
			sql.append(" AND DQ.CDPRODUTO = TB.CDPRODUTO");
			sql.append(" AND DQ.CDTABELAPRECO = TB.CDTABELAPRECO");
			sql.append(" AND DQ.QTITEM  = DQ_DESCQTITEM");
			sql.append(" AND DQ.CDUSUARIO = TB.CDUSUARIO");
		}
	}

	private void addSelectJoinDescQT(BaseDomain domainFilter, StringBuffer sql) {
		if (domainFilter!=null && ((ItemPedido)domainFilter).isJoinDescQt && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			sql.append(" DQ.VLPCTDESCONTO AS DQ_VLPCTDESCONTO,");
			sql.append(" DQ.VLDESCONTO AS DQ_VLDESCONTO,");
			sql.append(" DQ.CDDESCONTO AS DQ_CDDESCONTO,");
			sql.append(" (SELECT QTITEM FROM TBLVPDESCQUANTIDADE D");
			sql.append(" where D.CDEMPRESA = TB.CDEMPRESA");
			sql.append(" AND D.CDREPRESENTANTE = TB.CDREPRESENTANTE");
			sql.append(" AND D.CDPRODUTO = TB.CDPRODUTO");
			sql.append(" AND D.CDTABELAPRECO = TB.CDTABELAPRECO");
			sql.append(" AND D.QTITEM <= TB.QTITEMFISICO");
			sql.append("  ORDER BY D.QTITEM DESC LIMIT 1");
			sql.append(" ) AS DQ_DESCQTITEM,");
		}
	}

	public BaseDomain findByPrimaryKeyWhereNotTipoPedidoBonificado(BaseDomain domainFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domainFilter, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addJoin(domainFilter, sql);
        addWhereNotTipoPedidoBonificado(domainFilter, sql);
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
	        if (rs.next()) {
	            return populate(domainFilter, rs);
	        }
        }
        return null;
    }

	private void addWhereNotTipoPedidoBonificado(BaseDomain domainFilter, StringBuffer sql) {
        ItemPedido itemPedido = (ItemPedido) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", itemPedido.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", itemPedido.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", itemPedido.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", itemPedido.nuPedido);
		sqlWhereClause.addAndConditionEquals("tb.CDPRODUTO", itemPedido.cdProduto);
		sqlWhereClause.addAndConditionNotEquals("tb.FLTIPOITEMPEDIDO", itemPedido.flTipoItemPedido);
		sql.append(sqlWhereClause.getSql());
	}

	public void updateItemAutorizadoAgrupador(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE TBLVPITEMPEDIDO SET FLAGRUPADORSIMILARIDADE = ").append(Sql.getValue(itemPedido.flAgrupadorSimilaridade))
		.append(", CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(itemPedido.cdAgrupadorSimilaridade))
		.append(" WHERE rowkey = ").append(Sql.getValue(itemPedido.getRowKey()));
		executeUpdate(sql.toString());
	}

	public Vector findHistoricoProdutosByCliente(ItemPedido itemPedidoFilter) throws SQLException {
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT P.DTEMISSAO DTEMISSAO, ");
		sql.append("       P.VLCOTACAODOLAR VLCOTACAODOLAR, ");
		sql.append("       ITEM.QTITEMFISICO QTITEMFISICO, ");
		sql.append("       ITEM.VLPCTDESCONTO VLPCTDESCONTO, ");
		sql.append("       ITEM.VLITEMPEDIDO VLITEMPEDIDO, ");
		sql.append("       ITEM.VLITEMPEDIDOUNELEMENTAR VLITEMPEDIDOUNELEMENTAR, ");
		sql.append("       ITEM.QTITEMPEDIDOUNELEMENTAR QTITEMPEDIDOUNELEMENTAR, ");
		sql.append("	   ITEM.VLTOTALSTITEM VLTOTALSTITEM, ");
		sql.append("       ITEM.VLST VLST ");
		sql.append("FROM TBLVPITEMPEDIDO ITEM ");
		sql.append("         JOIN TBLVPPEDIDO P ");
		sql.append("              ON (ITEM.CDEMPRESA = P.CDEMPRESA ");
		sql.append("                  AND ITEM.CDREPRESENTANTE = P.CDREPRESENTANTE ");
		sql.append("                  AND ITEM.NUPEDIDO = P.NUPEDIDO ");
		sql.append("                  AND ITEM.FLORIGEMPEDIDO = P.FLORIGEMPEDIDO) ");
		sql.append("                 JOIN TBLVPSTATUSPEDIDOPDA SP on (sp.CDSTATUSPEDIDO = P.CDSTATUSPEDIDO) ");
		sql.append(" WHERE ");
		sql.append(" (SP.FLIGNORAHISTORICOITEM IS NULL ");
		sql.append(" OR SP.FLIGNORAHISTORICOITEM <> 'S') ");

		if(itemPedidoFilter.cdCliente != null){
			sql.append(	"AND P.CDCLIENTE = '"+itemPedidoFilter.cdCliente+"' ");

		}
		if(itemPedidoFilter.cdProduto != null){
			sql.append(" AND ITEM.CDPRODUTO = '"+ itemPedidoFilter.cdProduto +"' ");
		}

		sql.append("UNION ");

		sql.append("SELECT P.DTEMISSAO DTEMISSAO, ");
		sql.append("       P.VLCOTACAODOLAR VLCOTACAODOLAR, ");
		sql.append("       ITEM.QTITEMFISICO QTITEMFISICO, ");
		sql.append("       ITEM.VLPCTDESCONTO VLPCTDESCONTO, ");
		sql.append("       ITEM.VLITEMPEDIDO VLITEMPEDIDO, ");
		sql.append("       ITEM.VLITEMPEDIDOUNELEMENTAR VLITEMPEDIDOUNELEMENTAR, ");
		sql.append("       ITEM.QTITEMPEDIDOUNELEMENTAR QTITEMPEDIDOUNELEMENTAR, ");
		sql.append("	   ITEM.VLTOTALSTITEM VLTOTALSTITEM, ");
		sql.append("       ITEM.VLST VLST ");
		sql.append("FROM TBLVPITEMPEDIDOERP ITEM ");
		sql.append("         JOIN TBLVPPEDIDOERP P ");
		sql.append("              ON (P.CDEMPRESA = ITEM.CDEMPRESA AND ");
		sql.append("                  P.CDREPRESENTANTE = ITEM.CDREPRESENTANTE AND ");
		sql.append("                  P.NUPEDIDO = ITEM.NUPEDIDO AND ");
		sql.append("                  P.FLORIGEMPEDIDO = ITEM.FLORIGEMPEDIDO) ");
		sql.append("                 JOIN TBLVPSTATUSPEDIDOPDA SP on (sp.CDSTATUSPEDIDO = P.CDSTATUSPEDIDO) ");
		sql.append(" WHERE ");
		sql.append(" (SP.FLIGNORAHISTORICOITEM IS NULL" );
		sql.append(" OR SP.FLIGNORAHISTORICOITEM <> 'S') ");

		if(itemPedidoFilter.cdCliente != null){
			sql.append(	" AND P.CDCLIENTE = '" + itemPedidoFilter.cdCliente + "' ");
		}

		if(itemPedidoFilter.cdProduto != null){
			sql.append(" AND ITEM.CDPRODUTO = '" + itemPedidoFilter.cdProduto + "' ");
		}
		Vector itemPedidoList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			HistoricoItem histItem;
			while (rs.next()) {
				histItem = new HistoricoItem();
				histItem.dtEmissao = rs.getDate("DTEMISSAO");
				histItem.vlCotacaoDolar = rs.getDouble("VLCOTACAODOLAR");
				histItem.qtItemFisico = rs.getDouble("QTITEMFISICO");
				histItem.vlPctDesconto = rs.getDouble("VLPCTDESCONTO");
				histItem.vlItemPedido = rs.getDouble("VLITEMPEDIDO");
				histItem.vlItemPedidoUnElementar = rs.getDouble("VLITEMPEDIDOUNELEMENTAR");
				histItem.qtItemPedidoUnElementar = rs.getDouble("QTITEMPEDIDOUNELEMENTAR");
				histItem.vlStItem = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? rs.getDouble("VLTOTALSTITEM") : rs.getDouble("VLST");
				itemPedidoList.addElement(histItem);
			}
		}
		return itemPedidoList;
	}

	public double sumVlTotalItemAgrupadorGrade(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM(VLTOTALITEMPEDIDO) FROM TBLVPITEMPEDIDO tb ")
		.append(" JOIN TBLVPPRODUTO prod ON ")
		.append(" tb.CDEMPRESA = prod.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
		.append(" tb.CDPRODUTO = prod.CDPRODUTO ");
		DaoUtil.addJoinProdutoGrade(sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", itemPedido.nuPedido);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", itemPedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("prod.DSAGRUPADORGRADE = ", itemPedido.dsAgrupadorGradeFilter);
		sqlWhereClause.addAndCondition("PRODUTOGRADE.CDPRODUTO IS NOT NULL");
		sql.append(sqlWhereClause.getSql());
		return getDouble(sql.toString());
	}
	
	public int countItemPedidoAgrupadorGrade(Pedido pedido, String dsAgrupadorGrade) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPITEMPEDIDO tb");
		DaoUtil.addJoinProdutoGrade(sql);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", pedido.nuPedido);
		sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", pedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("prod.DSAGRUPADORGRADE = ", dsAgrupadorGrade);
		sqlWhereClause.addAndCondition("PRODUTOGRADE.CDPRODUTO IS NOT NULL");
		return getInt(sql.toString());
	}

	public void updateColumnsPendencia(ItemPedido itemPedido) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" UPDATE ").append(tableName).append(" SET ");
    	sql.append(" FLPENDENTE = ").append(Sql.getValue(itemPedido.flPendente));
    	sql.append(" , CDMOTIVOPENDENCIA = ").append(Sql.getValue(itemPedido.cdMotivoPendencia));
    	sql.append(" , NUORDEMLIBERACAO = ").append(Sql.getValue(itemPedido.nuOrdemLiberacao));
    	sql.append(" WHERE rowkey = ").append(Sql.getValue(itemPedido.getRowKey()));
    	executeUpdate(sql.toString());
	}
	
	public void updateFlPendenteItems(String flPendente, Vector rowkeyList) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName);
		sql.append(" SET FLPENDENTE = '").append(flPendente);
		sql.append("' WHERE CDEMPRESA = '").append(SessionLavenderePda.cdEmpresa).append("' ");
		addLoopInSql(sql, rowkeyList, "ROWKEY", 400);
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
	}

	public double sumVlTotalItemsPedidoPorLinha(String cdLinha, Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(VLTOTALITEMPEDIDO) VLTOTALITEMPEDIDO FROM ").append(tableName).append(" TB");
		sql.append(" JOIN TBLVPPRODUTO ").append(DaoUtil.ALIAS_PRODUTO).append(" ON ");
		sql.append(" PROD.CDPRODUTO = TB.CDPRODUTO");
		sql.append(" AND PROD.CDEMPRESA = TB.CDEMPRESA");
		sql.append(" AND PROD.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(" WHERE PROD.CDLINHA = ").append(Sql.getValue(cdLinha));
		sql.append(" AND TB.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" AND TB.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND TB.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND TB.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		return getDouble(sql.toString());
	}

	public BaseDomain findByPrimaryKeyAndCdKit(BaseDomain domainFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(domainFilter, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(domainFilter, sql);
		addWhereCdKit(domainFilter, sql);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return populate(domainFilter, rs);
			}
		}
		return null;
	}
	private void addWhereCdKit(BaseDomain domainFilter, StringBuffer sql) {
        ItemPedido itemPedido = (ItemPedido) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", itemPedido.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", itemPedido.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", itemPedido.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", itemPedido.nuPedido);
		sqlWhereClause.addAndConditionEquals("tb.CDPRODUTO", itemPedido.cdProduto);
		sqlWhereClause.addAndConditionEquals("tb.FLTIPOITEMPEDIDO", itemPedido.flTipoItemPedido);
		sqlWhereClause.addAndConditionEquals("tb.CDKIT", itemPedido.cdKit);
		sql.append(sqlWhereClause.getSql());
	}

	public HashMap<String, String> getInfosPersonalizadasItemPedido(JSONObject itemPedidoJsonFilters) throws SQLException {
		StringBuffer sqlFinal;
		String sqlConsulta = LavenderePdaConfig.getConsultaSqlApresentacaoInfosPersonalizadasCapaItemPedido().replaceAll("//n", "'\n'");
		sqlFinal = transformSqlFilters(itemPedidoJsonFilters, sqlConsulta);
		return executeSqlInfosPersonalizadas(sqlFinal);
	}

	private StringBuffer transformSqlFilters(JSONObject itemPedidoJsonFilters, String sqlConsulta) {
		StringBuffer sqlFinal;
		int indexInicioParam = sqlConsulta.indexOf(":");
		String sqlConsultaPartial = "";
		if (indexInicioParam != -1) {
			sqlConsultaPartial = sqlConsulta.substring(indexInicioParam + 1);
			sqlFinal = new StringBuffer(sqlConsulta.substring(0, indexInicioParam));
		} else {
			sqlFinal = new StringBuffer(sqlConsulta);
		}
		while (indexInicioParam != -1) {
			int indexFinalParam = sqlConsultaPartial.indexOf(" ");
			int indexFinalPercentual = sqlConsultaPartial.indexOf("%");
			if (indexFinalPercentual != -1 && (indexFinalPercentual < indexFinalParam || indexFinalParam == -1)) {
				indexFinalParam = indexFinalPercentual;
			}
			//--
			indexFinalParam = indexFinalParam == -1 ? sqlConsultaPartial.length() : indexFinalParam;
			String nmCampo = sqlConsultaPartial.substring(0, indexFinalParam).trim();
			String paramValue = "";
			if (Representante.NMCOLUNA_CDREPRESENTANTE.equalsIgnoreCase(nmCampo) && ValueUtil.isEmpty(paramValue)) {
				paramValue = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
			}
			if (Empresa.NMCOLUNA_CDEMPRESA.equalsIgnoreCase(nmCampo) && ValueUtil.isEmpty(paramValue)) {
				paramValue = SessionLavenderePda.cdEmpresa;
			}
			if (ValueUtil.isEmpty(paramValue) && itemPedidoJsonFilters != null) {
				paramValue = itemPedidoJsonFilters.getString(nmCampo);
			}
			sqlFinal.append(Sql.getValue(paramValue));
			sqlConsultaPartial = sqlConsultaPartial.substring(indexFinalParam);
			indexInicioParam = sqlConsultaPartial.indexOf(":");
			if (indexInicioParam != -1) {
				sqlFinal.append(sqlConsultaPartial, 0, indexInicioParam);
				sqlConsultaPartial = sqlConsultaPartial.substring(indexInicioParam + 1);
			} else {
				sqlFinal.append(sqlConsultaPartial);
			}
		}
		return sqlFinal;
	}

	private HashMap<String, String> executeSqlInfosPersonalizadas(StringBuffer sqlFinal) {
		try (Statement st = getCurrentDriver().getStatement();
			 ResultSet rs = st.executeQuery(sqlFinal.toString())) {
			ResultSetMetaData rsmd = rs.getMetaData();
			HashMap<String, String> resultMap = new HashMap<>();
			int size = rsmd.getColumnCount();
			Vector resultColumns = new Vector(size);
			for (int i = 1; i <= size; i++) {
				resultColumns.addElement(rsmd.getColumnName(i));
			}
			while (rs.next()) {
				resultMap = populatePersonLinkedMap(rs, resultColumns);
			}
			return resultMap;
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return null;
		}
	}

	public void updateNuOrdemCompraClienteItemPedidoByNuOrdemCompraPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" UPDATE ").append(tableName).append(" SET ");
		sql.append(" NUORDEMCOMPRACLIENTE = ").append(Sql.getValue(pedido.nuOrdemCompraCliente));
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append(" AND (NUORDEMCOMPRACLIENTE IS NULL OR NUORDEMCOMPRACLIENTE = '')");
		try {
			executeUpdate(sql.toString());
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	public Vector getItemPedidoDuplicadosOrdemCompraDuplicadaByPedido(Pedido pedido) throws SQLException {
		Vector itemPedidoList = new Vector();
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDPRODUTO, NUORDEMCOMPRACLIENTE, COUNT(*) FROM ").append(tableName);
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append("GROUP BY CDPRODUTO, NUORDEMCOMPRACLIENTE HAVING COUNT(*) > 1");
		try (Statement st = getCurrentDriver().getStatement();
			 ResultSet rs = st.executeQuery(sql.toString())) {
			ItemPedido itemPedido = new ItemPedido();
			while (rs.next()) {
				itemPedido.cdProduto = rs.getString(1);
				itemPedido.nuOrdemCompraCliente = rs.getString(2);
				itemPedidoList.addElement(itemPedido);
			}
		}
		return itemPedidoList;
	}
	
	public void updateValoresPoliticaComercial(ItemPedido itemPedido) {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE TBLVPITEMPEDIDO SET CDPOLITICACOMERCIAL = ").append(Sql.getValue(itemPedido.cdPoliticaComercial))
		.append(", VLPCTPOLITICACOMERCIAL = ").append(Sql.getValue(itemPedido.vlPctPoliticaComercial))
		.append(", VLPCTCOMISSAO = ").append(Sql.getValue(itemPedido.vlPctComissao))
		.append(", VLTOTALCOMISSAO = ").append(Sql.getValue(itemPedido.vlTotalComissao))
		.append(" WHERE CDEMPRESA = ").append(Sql.getValue(itemPedido.cdEmpresa)).append(" AND")
		.append(" CDREPRESENTANTE = ").append(Sql.getValue(itemPedido.cdRepresentante)).append(" AND")
		.append(" NUPEDIDO = ").append(Sql.getValue(itemPedido.nuPedido)).append(" AND")
		.append(" FLORIGEMPEDIDO = ").append(Sql.getValue(itemPedido.flOrigemPedido)).append(" AND")
		.append(" CDPRODUTO = ").append(Sql.getValue(itemPedido.cdProduto)).append(" AND")
		.append(" FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemPedido.flTipoItemPedido)).append(" AND")
		.append(" NUSEQPRODUTO = ").append(Sql.getValue(itemPedido.nuSeqProduto));
		try {
			executeUpdate(sql.toString());
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

}
