package br.com.wmw.lavenderepda.business.conf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditFoneMask;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.tmp.ComboBoxTc;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.FiltroCliente;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaCli;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.MargemRentabService;
import br.com.wmw.lavenderepda.business.service.NfceService;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.PedidoBoletoService;
import br.com.wmw.lavenderepda.business.service.StatusGpsService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.service.ValorParametroService;
import br.com.wmw.lavenderepda.presentation.ui.ext.ButtonMenuCategoria;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.gfx.Color;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public final class LavenderePdaConfig {

	public static String cdStatusPedidoAberto = "1";
	public static String cdStatusPedidoFechado = "2";
	public static final String cdStatusProcessandoNfeTxt = "1";
	public static final String cdStatusFinalizadoNfeTxt = "2";
	public static final String cdStatusResetNfeTxt = "9";
	public static String usaTipoFretePedido; //4
	public static String configTransportadoraPedido; //5
	public static boolean tipoPedidoOcultoNoPedido; //6
	public static String configTipoPagamento; //7
	private static String configCondicaoPagamentoNoPedido; //9
	public static int nuDiasPrevisaoEntrega; //10
	public static boolean previsaoEntregaOcultaNoPedido; //11
	public static int nuCasasDecimais; //12
	public static boolean condicaoPagamentoSemCadastro; //13
	public static char separadorCondicaoPagamentoSemCadastro; //14
	public static int maiorParcelaCondicaoPagamentoSemCadastro; //15
	private static String configRentabilidadeNoPedido; //16
	public static double valorMinimoParaPedido; //17
	public static boolean camposDsPagamentoFichaFinanceira; //18
	public static JSONObject configIndiceFinanceiroClienteVlItemPedido; //19
	public static int maximoItensPorPedido; //22
	public static JSONObject configIndiceFinanceiroCondPagto; //24
	public static boolean bloquearNovoPedidoClienteBloqueado; //25
	public static boolean usaRestricaoVendaClienteProduto; //26
	public static int nuDiaSemanaVigenciaSemanal; //26-2
	public static String cadastroNovoCliente; //27
	public static String enviarEmailPedidoAutoCliente; //30
	public static String permiteDescontoAcrescimoItemPedido; //38
	public static boolean controlarLimiteCreditoCliente; //39
	public static boolean bloquearLimiteCreditoCliente; //40
	public static boolean usaAgrupadorSimilaridadeProduto; //43-1
	public static boolean ocultaListaSimilaresDescQuantidade; //43-2
	public static boolean usaSomenteQtdDescQtdSimilar; //43-3
	public static boolean usaRegistroVisitaSupervisor; //45
	public static boolean usaDescProgressivoPersonalizado; //46-1
	public static boolean ignoraOutroPedidoAbertoDescProg; //46-2
	public static int exibeAlteracaoDescProgressivo; // 46-3
	public static boolean usaClienteSemPedidoFornecedor; //55-1
	public static int nuDiasClienteSemPedidoFornecedor; //55-2
	public static boolean usaWebserviceSankhyaComplementaPedido; //57-1
	public static int timeOutWebserviceSankhyaComplementaPedido; //57-2
	public static String consultaSqlWebserviceSankhyaComplementaPedido; //57-3
	public static boolean usaStatusClienteVinculado; //58
	private static int nuLayoutImpressaoPedidoViaBluetooth; //59
	public static String cdStatusNovoClienteFechado; //67
	public static boolean bloquearVendaProdutoSemEstoque; //79
	public static boolean usarDescontoPorQuantidadeItemPedido; //80
	public static boolean liberaDescAcrescManualAuto; //80-2
	public static boolean usaVigenciaDescQuantidade; //80-3
	public static boolean ignoraAcrescimo; //80-4
	public static boolean ocultaBotaoIgnorar; //80-5
	public static boolean apresentaFiltroDescQtd; //80-6
	public static boolean permiteVincularCliente; //80-7
	public static boolean ocultaPercentualDescontoListaFaixa; //80-8
	public static int qtMinimaCaracteresFiltroProduto; //81
	public static boolean usaTabelaPrecoPorCliente; //82
	public static boolean usarFlStatusClienteDaFichaFinanceira; //83
	public static int nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido; //84
	public static int sementeSenhaClienteAtrasadoNovoPedido; //85
	public static boolean atualizarEstoqueInterno; //87-1
	public static boolean ocultaRecalculaEstoque; //87-2
	public static boolean usarCondicaoPagtoPorTabelaPreco; //89
	public static String permiteTabPrecoItemDiferentePedido; //90
	public static boolean usaConversaoUnidadesMedida; //95
	public static boolean ocultaQtItemFisico; //96
	public static String cdStatusPedidoTransmitido = "3"; //97
	public static String ocultaEstoque; //99
	public static String ocultaFichaFinanceira; //100
	public static boolean permiteValorZeroPedidoEItem; //101
	public static boolean usaEstoqueOnline; //102
	public static boolean bloqueiaTabPrecoPadraoClienteNoPedido; //103
	public static boolean usaFiltroSomenteDescricaoProduto; //104
	public static String usaQtdItemPedidoInteiro; //106
	public static boolean usaCondicaoPagamentoPorCliente; //107
	public static String geraParcelasPorTipoCondPgto; //108
	public static boolean permiteEditarVecimentoParcela;
	public static boolean sugereAlteracaoProximosVencimentos;
	public static boolean permiteDescAdicionalPorTabPreco; //110
	public static boolean usaTabelaPrecoPorRepresentante; //113
	public static boolean usaMuralDeRecados; //114
	public static String naoConsisteDescontoAcrescimoMaximo; //115
	public static String configModuloRecado; //117
	public static String configProdutoControlado; //123
	public static String valorMinimoParaPedidoPorTabelaPreco; //124
	public static boolean geraVerbaSaldo; //125
	public static double controleNivelCriticoBateria; //128
	public static boolean persisteVerbaSaldoRep; //129
	public static boolean grifaClienteBloqueado; //131
	public static int usaFiltroGrupoProduto; //132
	public static JSONObject configInsercaoMultiplosItensPorVezNoPedido; //133
	public static int geraCargaQuandoAtualizacaoMuitoGrande; //135
	public static int sementeSenhaControleVencimentoCertificadoIos; //136
	private static String configAgendaDeVisitas; //140
	public static boolean bloquearExclusaoRecado; //141
	public static String consisteConversaoUnidades; //144
	public static boolean consisteConversaoUnidadesPorCliente; //145
	public static String configListaClientes; //149
	public static boolean ocultaDescontoPorQuantidadeItemPedido; //150
	public static boolean ocultaColunaCdProduto; //151
	public static boolean ignoraEscolhaAutoMelhorTabPrecoDescPorQtd; //152
	public static int avisaClienteAtrasadoNovoPedido; //154
	public static boolean usaPesquisaInicioString; //156
	private static String usaFiltroProdutoPorTabelaPreco; //157
	public static int nivelRegistroDeLogNoPda; //158
	public static int nuTruncamentoRegraDescontoVerba; //163
	public static String nuArredondamentoRegraInterpolacao; //164
	public static String configDescricaoEntidadesCliente; //165
	public static boolean usaHistoricoTabelaPrecoUsadasPorCliente; //167
	public static boolean usaHistoricoItemPedido; //173
	public static JSONObject configKitProduto; //174
	public static boolean usaFiltroFornecedor; //175
	public static String propagaUltimoDescontoItemPedido; //176
	public static boolean geraNovidadeProdutoParaEntradaNoEstoque; //177-1
	public static boolean ocultaQtdEstoqueNovidade; //177-2
	public static boolean usaRelNovidadeProduto; //178
	public static boolean usaCodigoRepresentanteErp; //187
	public static int sementeSenhaFechamentoDiario;
	public static int bloqueiaClienteAtrasadoNovoPedido; //188
	private static String nuMaxDiasPrevisaoEntregaDoPedido; //189
	private static String aplicaIndiceFinanceiroClientePorPedido; //190
	public static boolean usaListaColunaPorTabelaPreco; //191
	public static int usaRotaDeEntregaNoPedidoSemCadastro; //193
	public static boolean usaTipoDeEntregaNoPedido; //194
	public static int ignoraPrecoMinimoListaColunaPorTabelaPreco; //195
	public static boolean consistePrecoMaximoListaColunaPorTabelaPreco; //196
	public static boolean ocultaHistoricoItemPedidoAutomatico; //197
	public static boolean mostraVlPrecoFabrica; //198
	public static String configPrecoMaximoConsumidor; //199
	public static String configNuOrdemCompraClienteNoPedido; //201
	public static boolean permiteVenderProdutoMenorDescontoQuantidade; //205
	private static String configLiberacaoComSenhaLimiteCreditoCliente; //206
	public static int sementeSenhaLimiteCreditoCliente; //207
	public static boolean liberaComSenhaPrecoDeVenda; //208
	public static int sementeSenhaPrecoDeVenda; //209
	public static int sementeSenhaTipoPedido; //210
	public static boolean marcaRecadoComoLidoAutomaticamente; //212
	public static String bloqueiaCondPagtoPorDiasCliente; //213
	public static boolean permiteReabrirPedidoFechado; //216
	public static String ordemCamposTelaItemPedido; //220
	public static boolean isUsaOrdemCamposTelaItemPedido; //220-2
	public static String filtrosFixoTelaItemPedido; //221
	public static boolean mostraTodosCamposAutomaticamenteTelaItemPedido; //222
	public static boolean limpaFiltroProdutoAutomaticamente; //223
	private static JSONObject configUnificaBotoesEnviarReceberDados; //224
	public static boolean obrigaLeituraRecados; //225
	public static boolean aplicaIndiceFinanceiroClientePorItemFinalPedido;//226
	public static boolean aplicaDescontoProgressivoPorItemFinalPedido;//227
	public static int aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido; //228
	public static boolean aplicaDescontoCCPPorItemFinalPedido; //229
	public static boolean aplicaSomenteMaiorDescontoPorItemFinalPedido; //230
	public static boolean mostraEstoquePrevisto; //236
	public static boolean mostraPrecosPorPrazoMedioItemPedido; //237
	public static boolean usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil; //238
	public static double pctVidaUtilLoteProdutoCritico; //253
	public static boolean usaVerbaItemPedidoPorItemTabPreco; //254
	public static boolean usaFreteNoPedidoPorItemBaseadoNoItemTabPreco; //255
	public static String mostraColunaQtdNaListaDePedidos; //257
	public static boolean clienteComContratoExigeSetorPedido; //258
	public static boolean mostraFotoProduto; //259
	public static boolean mostraDetalhesAdicionaisNosRelatoriosDeMetas; //262
	public static String usaRegistroManualDeVisitaSemAgenda; //263
	public static int obrigaEnvioDePedidosPorTempoDecorrido; //264
	public static int obrigaEnvioDePedidosPorQtdPedidosPendentes; //265
	public static int sementeSenhaObrigaEnvioDePedidos; //266
	public static String bloqueiaProdutoBloqueadoNoPedido; //267
	public static boolean usaRegistroMotivoNaoVendaProdutoSugerido; //268
	public static String obrigaReceberDadosBloqueiaUsoSistema; //269
	public static int sementeSenhaObrigaReceberDadosBloqueiaUsoSistema; //270
	public static String obrigaReceberDadosBloqueiaNovoPedido; //271
	public static int sementeSenhaObrigaReceberDadosBloqueiaNovoPedido; //272
	public static double permiteDescontoEmValorPorPedido; //274
	public static String configRelGiroProduto; //275
	private static String configGeracaoSenhaSupervisor; //276
	private static String configPesquisaMercado; //278	
	public static String configModuloDeContatosDoCliente; //280
	public static int qtDiasPermanenciaPesquisaDeMercado; //284
	public static boolean mostraRelAniversariantesAposLogin; //285
	public static String configPedidoExclusivoTrocaRecolher; //287-1
	public static String usaPedidoExclusivoTrocaRecolher; //287-1
	public static boolean usaCestaPromocional; //288
	public static boolean usaCodigoClienteUnico; //292
	public static int usaNmFantasiaNoLugarDaRazaoSocialDoCliente; //293
	private static int nuCasasDecimaisArmazenamento; //295
	public static boolean usaAreaVendas; //296
	public static boolean ocultaTabelaPrecoPedido; //297
	public static boolean naoSolicitaMotivoVisitaClienteForaSequencia; //298
	public static int usaFiltroPrincipioAtivoNoProduto; //300
	public static boolean usaSugestaoVendaProdutoMesmoPrincipioAtivo; //301
	public static String configCalculaStItemPedido; //303
	public static String mostraColunaPrecoNaListaDeProdutos; //306-1
	public static boolean mostraPrecoUnidadeItem; //306-2
	public static boolean exibirNotasFiscaisPedido; //307
	public static boolean ocultaQtItemFaturamento; //308
	public static boolean mostraQtdPorEmbalagemProduto; //309
	public static boolean usaFiltroFornecedorAutoAposSalvarNovo; //310
	public static boolean excluiRecadoAutomaticamenteAposLeitura; //311
	public static boolean ocultaHoraRegistroChegadaSaida; //312
	public static boolean travaTipoPagtoPadraoClienteEspecial; //314
	public static boolean sugereEnvioAutomaticoPedido; //315
	public static String configPedidoProducao; //316
	private static String mostraFlexPositivoPedido; //317
	public static boolean aplicaDescontoPromocionalItemTabPreco; //318
	public static boolean restringeTabPrecoEspecialConformePadraoCliente; //319
	public static boolean restringeTabPrecoCondPagtoClienteEspecial; //320
	public static String configValorMinimoDescPromocional; //323
	public static double permiteDescontoPercentualPorPedido; //324
	public static String configCalculoPesoPedido; //326
	public static String configFiltrosListaPedidos; //327
	public static boolean usaTecladoVirtual; //329
	public static boolean usaModuloTrocaNoPedido; //333
	public static double percentualLimiteTrocaNoPedido; //334
	public static String percentualToleranciaTrocaNoPedido; //335
	public static boolean indiceFinanceiroCondPagtoPorDias; //336
	public static boolean anulaIndiceFinanceiroCondPagtoTabPrecoProm; //339
	public static boolean mostrarPercDescontoMaximo; //340
	private static String usaCampanhaDeVendasPorCestaDeProdutos; //344
	private static String mostraColunaEstoqueNaListaDeProdutos; //346
	public static boolean relResumoPedidos; //347
	public static double permiteDescValorPorPedidoConsumindoVerba; //348
	public static int qtMinimaItensParaLiberarVerbaPorPedido; //349
	public static boolean usaIndiceFinanceiroSupRep; //351
	public static boolean calculaStSimplificadaItemPedido; //352
	public static boolean usaFiltroAtributoProduto; //357
	public static String ignoraFiltroAutoFornecedorPorAtributoProduto; //358
	public static boolean sugereEnvioAutomaticoRecado; //365
	public static boolean usaObservacaoPorItemPedido; //369
	public static String labelGruposProduto; //372
	public static boolean indiceFinanceiroCondPagtoVlBase; //374
	public static String configFiltroProdutoCodigo; //375
	public static List<String> avisaUsuarioSobreConsumoVerba; //378
	public static boolean geraApresentaTicketMedioDiario; //384
	public static boolean mostraVlCotacaoDolarPedido; //385
	public static String configModuloRiscoChurn; //391
	private static String valorMinimoParaPedidoPorCondPagto; //386
	public static boolean valorMinimoParaPedidoPorTipoPagamento; //394
	private static String usaVigenciaNaVerbaPorItemTabPreco; //395
	public static boolean aplicaDescontoQuantidadeVlBaseFlex; //397
	public static boolean bloqueiaTipoPagamentoNivelSuperior; //398
	public static boolean grifaClienteAtrasado; //399
	public static int quantidadeMinimaItensPedido; //400
	public static double ticketMedioEmpresa; //401
	public static boolean aplicaMultiploEspecialAutoItemPedido; //403
	public static String consisteConversaoUnidadesMultiploEspecial; //406
	public static boolean informaQuantidadePrimeiroMultiploNaoAtingido; //406-5
	public static boolean avisaConversaoUnidades; //407
	public static String configMotivoTrocaPorItem; //408
	private static String avisaVendaProdutoSemEstoque; //409
	public static String horaLimiteParaEnvioPedidos; //410
	public static boolean selecionaTabelaPromocionalAoClicarNoProduto; //411
	public static int sementeSenhaHoraLimiteEnvioPedidos; //412
	public static JSONObject configBonificacaoItemPedido; //413
	public static int sementeSenhaBonificarProdutoPedido; //414
	public static boolean usaOrdenacaoPorCodigoListaProdutos; // 415
	public static String permitePedidoNovoCliente; //416
	public static boolean bloqueiaAlterarFreteDoItemPedido; //419
	public static boolean usaFreteApenasTipoFob; //420
	public static String tabelasDadosParaTodosRepSup; //421
	public static boolean liberaComSenhaPrecoProduto; //422-1
	public static boolean usaDescMaxPrecoLiberadoConsomeVerbaGrupoProduto; //422-2
	public static boolean liberaNegociacaoPrecoApenasDesconto; //422-3
	public static int sementeSenhaLiberaPrecoProduto; //423
	public static boolean usaDescontoProgressivoPorTabelaPreco; //424
	public static boolean bloqueiaDescontoProgressivoClientesEspeciais; //425
	public static boolean permiteBonificarQualquerProduto; //426
	public static boolean bloqueiaTipoPedidoPadraoClienteNoPedido; //427
	private static JSONObject configDescontoAcrescimoNoPedidoAplicadoPorItem; //428
	public static String usaPrecoBaseItemBonificado; //429
	public static String mostraPrecosPorCondicaoPagamento; //430
	public static boolean usaServicoBuscaEnderecoPorCep; //431
	public static String grifaProdutoSemEstoqueNaLista; //432
	public static boolean emiteBeepAoInserirItemPedido; //433
	public static boolean permiteEditarDescontoProgressivo; //434
	public static String usaTipoPagamentoPorCondicaoPagamento; //438
	public static boolean usaTabelaPrecoPorCondicaoPagamento; //441
	public static String configSugestaoParaNovoPedido; //442
	public static boolean usaDescontoComissaoPorProduto; //444
	public static boolean usaDescontoComissaoPorGrupo; //445
	public static boolean relVendasProdutoPorCliente; //450
	public static boolean aplicaIndiceFinanceiroEspClientePorItemFinalPedido; //452
	public static boolean mostraPrevisaoDescontoGridProdutos; //453
	public static String tamanhoColunaCdProduto; //455
	private static String usaPedidoBonificacao; //456
	public static String bloqueiaCondPagtoPadraoClienteNoPedido; //457
	public static String usaCondPagtoPorTipoPagtoPadraoCliente; //458
	public static boolean usaOrdemNumericaColunaCodigoProduto; //459
	public static String aplicaDeflatorCondPagtoValorTotalPedido; //461
	public static String configCodigosProdutosParaClientes; //463
	public static boolean confirmaNovoPedidoClienteBloqueado; //464
	public static String usaRotaDeEntregaNoPedidoComCadastro; //466
	public static int nuMinDiasPrevisaoEntregaDoPedido; //467
	public static boolean ocultaTelaGeracaoSenhaSupervisor; //469
	public static String mostraDetalhesAdicionaisRelMetasPorProduto; //470
	public static boolean bloqueiaItemTabelaPrecoParaVenda; //472
	public static boolean geraDiferencasPedidoPdaIntegracaoPorView; //476
	public static String tabelaPrecoParaTroca; //477
	public static boolean indiceFinanceiroCondPagtoClientePorDias; //478
	public static boolean anulaDescontoPessoaFisica; //479
	public static boolean destacaProdutosJaIncluidosAoPedido; //481
	public static boolean filtraTabelaPrecoPelaRegiaoDoCliente; //482
	public static boolean consisteMultiploEmbalagem; //484
	public static boolean mostraSaldoContaCorrenteCliente; //485
	public static boolean mostraRelatorioContaCorrenteCliente; //486
	public static boolean usaFatorCUMEspecialClienteCreditoAntecipado; //487
	public static boolean apenasAvisaDescontoAcrescimoMaximo; //488
	public static boolean gravaCpfCnpjNovoClienteSemSeparadores; //489
	public static boolean atualizaDataHoraServidorNoAcessoAoSistema; //494
	public static boolean filtraTabelaPrecoPelaListaDoCliente; //495
	public static String mostraProdutoPromocionalDestacado; //498
	public static int qtdMinItensParaPermitirItensPromocionais; //499
	public static boolean usaPrecoEspecialParaClienteEspecial; //500
	public static boolean destacaProdutoDeKitNaGrid; //501
	public static String mostraValorDaUnidadePrecoPorEmbalagem; //502
	public static boolean permiteAlterarValorDaUnidadePrecoPorEmbalagem; //503
	public static boolean aplicaReducaoPrecoItemClienteOptanteSimples; //506
	public static boolean usaLeadTimePadraoClienteNoPedido; //508
	public static boolean permiteAlterarTabelaPrecoPedido; //509
	public static boolean exibeTituloMaisAtrasadoNaFichaFinanceira; //511
	public static String cdStatusPedidoCancelado; //533
	public static boolean usaRegistroProdutoFaltante; //535
	public static boolean mostraEmbalagemCompraProduto; //541
	public static boolean permiteTodasTabelasPedidoBonificacao; //543
	public static boolean usaNovoPedidoParaMesmoCliente; //544
	public static boolean usaCCClientePorTipoPedido; //545
	public static String nuGrupoProdutoForaDaCCClientePorTipoPedido; //547
	public static boolean usaPesquisaProdutoPersonalizada; //550
	public static String configFaceamento; //553
	public static boolean verificaLimiteCreditoClienteEspecial; //558
	public static boolean mostraTotalDeClienteNaLista; //559
	public static boolean mostraRelNovidadeAposPrimeiroSync; //560
	public static boolean usaNovidadesRecentesNoRelNovidades; //561
	public static boolean retornaTabPrecoParaPadrao; //562
	public static boolean informaVerbaManual; //563
	public static boolean usaTecladoFixoTelaItemPedido; //564
	public static double permiteBonificarProdutoPedidoSemVerba; //565
	public static boolean usaControleNoDescontoPromocional; //566
	public static boolean usaAvisoPreAlta; //567
	public static int mostraFotoNaTelaItemPedido; //568
	public static boolean calculaPontuacaoDaRentabilidadeNoPedido; //570
	public static String ocultaPrecosNoMenuProduto; //571
	public static String avisaUsuarioPositivacaoFornecedor; //572
	public static boolean ignoraIndiceFinanceiroCondPagtoProdutoPromocional; //574
	public static String usaControleTimeZone; //575
	public static boolean usaTelaAdicionarItemAoPedidoEstiloDesktop; //577
	public static int nuMinCaracterObservacaoMotivoVisita; //582
	public static boolean usaPedidoBonificacaoUsandoVerbaCliente; //583
	public static boolean permiteEscolhaSaldoVerbaAConsumir; //583-2
	public static int valorTimeOutEstoqueOnline; //584
	public static boolean naoObrigaCompletarAgendaDiaAnterior; //588
	public static boolean aplicaDescontoQuantidadeVlBase; //589
	public static String dominioDataEntregaFinalSemana; //590
	public static boolean usaModuloConsignacao; //591
	public static boolean aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex; //592
	public static String usaModuloPagamento; //606
	public static boolean aplicaDescontoPedidoRepEspecial; //607
	public static boolean usaUnidadeAlternativa; //608
	private static String configDescontoQtdPorGrupo; //609
	public static String filtrosFixoTelaListaProduto; //610
	public static boolean usaCondicaoPagamentoPorTipoPedido; //611
	public static boolean usaSugestaoTabPrecoECondPagto; //612
	public static boolean filtraGrupoProdutoPorTipoPedido; //613
	public static String configUsaMultiplasSugestoesProdutoIndustria; //614
	public static boolean usaContratoForecast; //617
	public static boolean usaControleEstoqueGondola; //618
	public static int aplicaDescProgressivoPorQtdPorItemFinalPedido; //619
	public static boolean usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex; //622-1
	public static boolean aplicaBaseadoTabelaPreco; //622-2
	public static boolean permiteAlterarItemDePedidoBonificacao; //623
	public static boolean mostraValorParcelaPedido; //626
	public static boolean mostraDescontosPorTabelaPreco; //627
	public static boolean enviaInformacoesVisitaOnline; //631
	public static boolean usaPainelGerenciamentoVendas; //632
	public static int qtCaracteresColunaTabelaPreco; //634
	public static boolean apenasAvisaValorMinimoParaPedido; //635
	public static boolean emiteAlertaUsuarioVerificacaoCondPagto; //636
	public static int qtdPedidoSugestaoParaNovoPedido; //637
	public static int mostraUnidadesAlternativasAoSelecionarProduto; //638
	public static int apresentaQtdPreCarregadaDeVendaNoItemDoPedido; //640
	public static int quantidadeMinimaCaixasPedido; //641
	public static String usaKitBaseadoNoProduto; //644
	public static boolean mostraTodasTabelaPrecoPedidoNovoCliente; //645
	public static boolean validaDescontoCCPPorItem; //646
	public static String usaAgendaVisitaBaseadaNaSemanaDoMes; //649
	public static boolean usaFiltroMarcaProduto; //653
	public static boolean mostraValorTotalPedidoItensComEstoque; //655
	public static boolean usaOrdemNumericaColunaCodigoCliente; //656
	public static boolean usaApenasUmProdutoQuantidadeMaxVendaNoPedido; //657
	public static boolean usaRelSubstituicaoTributaria; //661
	public static boolean usaOrdenacaoDescricaoListaItemPedido; //662
	public static boolean destacaProdutoQtMinEstoqueLista; //663
	public static String mostraDetalhesAdicionaisRelMetasPorGrupoProduto; //664
	public static boolean usaBonificacaoPorGrupoBonificacao; //666
	public static int qtItemAVenderParaPermitirBonificarProduto; //667
	public static int qtPermitidaProdutoABonificarAposVenda; //668
	public static boolean usaRelMetasProdutoIndependenteDoCadDeProdutos; //678
	public static String mostraVlComTributosNasListasDePedidoEItens; //679
	public static boolean usaScroolLateralListasProdutos;
	public static int nuDiasSolicitaAtualizacaoCliente;
	private static int mostraDescricaoReferencia;
	public static String usaFiltroProdutoPromocional;
	public static int liberaComSenhaClienteRedeAtrasadoNovoPedido;
	public static int sementeSenhaClienteRedeAtrasadoNovoPedido;
	public static String configObservacaoPedido;
	public static int indiceRentabilidadePedido;
	public static int indiceMinimoRentabilidadePedido;
	public static boolean usaSugestaoParaNovoPedidoGiroProduto;
	public static int percAlturaTelaDescontoTabelaPreco;
	public static int qtMinimaItensParaPermitirItemRestricaoQuantidade;
	public static int sementeSenhaLiberacaoQtMaxVendaPorProduto;
	public static boolean filtraProdutoPorTipoPedido;
	public static boolean naoDescontaVerbaEmPedidoBonificacao;
	public static boolean usaTamanhoDinamicoTecladoFixoTelaItemPedido;
	public static boolean usaSimilarVendaProduto;
	public static boolean usaAgrupadorProdutoSimilar;
	public static String usaTipoPagtoPorCondPagtoECondPagtoPorCliente;
	public static boolean permiteIncluirMesmoProdutoNoPedido;
	public static boolean usaSequenciaInsercaoNuSeqProduto;
	public static boolean usaRelUltimosPedidosDoCliente;
	public static int nuDiasPedidoRecenteRelUltimosPedidos;
	public static String configGradeProduto;
	public static boolean usaEnvioPedidoBackground;
	public static boolean usaCondicaoPagamentoPedidoBonificacao;
	public static String mostraColunaQtdEmbalagensNaListaDePedidos;
	public static boolean usaSistemaModoSuporte;
	public static String usaPrecoPorUnidadeQuantidadePrazo;
	public static boolean permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido;
	public static boolean aplicaDescontoPromocionalAutomaticoItemTabPreco;
	public static String aplicaMaiorDescontoNoItemPedido;
	public static String configCarregaUltimoPrecoItemPedido;
	public static boolean mostraRelatorioGrupoProdutoNaoInseridosPedido;
	public static String mostraRelProdutosNaoInseridosPedido;
	public static String mostraProdutoDescPromocionalDestacado;
	public static String usaFiltroProdutoDescPromocional;
	public static boolean mostraPrecoProdutoTelaAutomaticaSelecaoUnidade;
	public static boolean mostraRelIndicadoresDeProdutividadeAposSync;
	public static boolean geraVerbaPositiva; //735
	public static boolean permiteAlterarVlItemNaUnidadeElementar;
	public static int nuIntervaloColetaGpsRepresentante;
	public static boolean usaColetaGpsPontosEspecificosSistema;
	public static int nuIntervaloErroColetaGpsPontosEspecificosSistema;
	public static int nuMaxTempoErroColetaGpsPontosEspecificosSistema;
	public static boolean usaSegmentoNoPedido;
	public static boolean usaSegmentoPorCliente;
	public static boolean usaTabelaPrecoPorSegmento;
	public static boolean usaCondicaoPagamentoPorSegmento;
	public static String ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao;
	public static boolean usaCondicaoComercialPedido;
	public static boolean usaCondicaoPagamentoPorCondicaoComercial;
	public static boolean usaCondicaoComercialPorSegmentoECliente;
	public static boolean usaRelInfoEntregaPedido;
	public static int nuDiasFiltroClienteSemPedido;
	private static String usaSugestaoVendaComCadastro;
	public static String usaIndicacaoMotivoDescPedido;
	public static String configTelaInfoProduto; //758
	public static String usaDiasUteisContabilizarDiasAtrazoCliente;
	public static boolean usaFretePedidoPorTranspTipoPedProd;
	public static int usaSugestaoVendaBaseadaDifPedidos;
	public static boolean usaRateioFreteRepresentanteCliente;
	public static String usaRestricaoVendaProdutoPorCliente;
	public static boolean usaBloqueioAlteracaoPrecoPedidoPorCliente;
	public static String usaMotivoAtrasoClienteAtrasado;
	public static boolean mantemPercAcrescDescProdutoAoTrocarTabPrecoPedido;
	public static boolean usaRestricaoVendaProdutoPorUnidade;
	public static boolean permiteAlterarCondicaoComercialPedido;
	public static int qtMinCaracteresMotivoAtrasoClienteAtrasado;
	public static boolean relMetasDeVendaUnificado;
	public static boolean exibeRelMetasDeVendaAposPrimeiroSync;
	public static boolean usaPesoGerarRealizado;
	public static boolean exibeRelClientesNaoAtendidosAposPrimeiroSync;
	public static boolean usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd;
	public static boolean liberaComSenhaVendaProdutoBloqueado;
	public static int sementeSenhaLiberaComSenhaVendaProdutoBloqueado;
	public static boolean usaItensBonificadoCalculoRealizado;
	public static boolean usaDsReferenciaNmFoto;
	public static int validaPctMaxDescPorRepresentante;
	public static boolean usaNuConversaoUnidadePorItemTabelaPreco;
	public static boolean realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido;
	public static boolean ocultaInfosValoresPedido;
	public static String usaGerenciamentoRentabilidade;
	public static boolean usaTipoPedidoComoOpcaoMenuCliente;
	public static boolean usaNuSequenciaNaChaveDaAgendaVisita;
	public static boolean acessaDiretamenteTelaAdicionarItemPedido;
	public static boolean bloqueiaFechamentoPedidoProdutoSemEstoque;
	public static boolean usaPositivacaoVisitaPorTipoPedido;
	public static boolean carregaProdutosAutoTelaItemPedidoEstiloDesktop;
	public static JSONObject configLocalEstoque;
	public static boolean bloqueiaSistemaGpsInativo;
	public static int sementeSenhaBloqueiaSistemaGpsInativo;
	public static boolean usaDescricaoCodigoNaVisualizacaoEntidades;
	public static String ocultaAcessoRelUltimosPedidos;
	public static boolean ocultaAcessoInfoCliente;
	public static int usaSugestaoVendaProdutosPorFoto;
	public static String qtdPedidosPermitidosManterAbertos;
	public static boolean usaVerbaUnificada;
	public static int qtCaracteresObservacoesPedidoItemPedido;
	public static boolean naoValidaSugestaoVendaAoFecharPedidoEmLote;
	public static boolean naoValidaSugestaoVendaDifPedidosAoFecharPedidoEmLote;
	public static boolean ignoraIndiceFinanceiroCondComercialProdutoPromocional;
	public static String naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote;
	public static boolean usaRegistroPagamentoParaClienteAtrasado;
	public static boolean usaSugestaoFechamentoAoSairPedido;
	public static boolean usaEnvioPedidoServidorSemConfirmacao;
	public static boolean usaEnvioRecadoServidorSemConfirmacao;
	public static double pctMargemAgregada;
	public static boolean naoAvisaClienteAtrasadoFecharPedidoEmLote;
	public static boolean usaDescontoQtdeGrupoPorTabelaPreco;
	public static boolean ocultaPopupDescQtdeGrupoItemPedido;
	public static boolean mostraPercDescMaxPedido;
	public static boolean filtraProdutosPorOrigemPedido;
	public static int usaImpressaoPedidoViaBluetooth;
	public static boolean mantemTabPrecoSelecionadaListaProduto;
	public static String configGrupoDestaqueProduto;
	public static boolean usaOportunidadeVenda;
	public static String valorMinimoParaPedidoPorTipoPedido;
	public static boolean usaTabelaPrecoPorClienteOuCondPagto;
	public static String validaDescMaxMesmoComDescQuantidadeEDescontoGrupo;
	public static boolean liberaSenhaSugestaoVendaObrigatoria;
	public static int sementeSenhaSugestaoVendaObrigatoria;
	public static int validaSugestaoVendaMultiplasEmpresas;
	public static String bloqueiaDescontoMaiorDescontoProgressivo;
	public static boolean usaControleSaldoOportunidade;
	public static boolean usaGrupoDestaqueItemTabPreco;
	public static boolean aplicaDescQtdPorGrupoProdFecharPedido;
	public static boolean filtraProdutoClienteRepresentante;
	public static boolean usaValorExcecaoNaCondicaoComercial;
	public static boolean destacaClienteAtendidoMes;
	public static boolean usaEscolhaEmpresaPedido;
	public static String usaVendaRelacionada;
	public static boolean usaSenhaVendaRelacionada;
	public static int sementeSenhaVendaRelacionada;
	public static String ocultaCamposCalculadosFichaFinanceira;
	public static int usaBackupAutomatico;
	public static int nuDiasPermanenciaResumoDia;
	public static String relacionaPedidoNaTrocaBonificacao;
	public static boolean usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente;
	public static int qtCopiasImpressaoPedido;
	public static int controlaVencimentoAlvaraCliente;
	private static JSONObject configVerbaSaldoPorGrupoProduto;
	public static boolean obrigaRespostaRecado;
	public static int controlaVencimentoAfeCliente;
	public static boolean usaItensLiberadosSenhaCalculoDescProgressivoPedido;
	public static boolean permiteEditarConexaoPda;
	public static String calculaIpiItemPedido;
	public static String tipoDescricaoQtdListaItemPedido;
	public static boolean usaPedidosTodasEmpresasSaldoCliente;
	public static int nuCasasDecimaisVlStVlIpi;
	public static int nuIntervaloEnvioPontoGpsBackground;
	public static boolean usaGrupoCondPagtoCli;
	public static String usaEstoqueDisponivelItemSugestao;
	public static boolean usaConfirmacaoVerbaPedidoSugestao;
	public static String configInfoComplementarPedido;
	public static boolean usaSenhaSqlExecutor;
	public static int sementeSenhaSqlExecutor;
	public static boolean obrigaInfoAdicionalFreteNoPedido;
	public static boolean usaPctFreteTipoFreteNoPedido;	
	public static boolean aplicaValoresProdPrincipalProdRelacionado;
	private static JSONObject aplicaDescontoNoProdutoPorGrupoDescPromocional; //908
	public static boolean validaVendaRelacionadaUnidadeFaturamento;
	public static String formatoMascaraFone;
	public static String avisaPedidoAbertoFechado;
	public static boolean exibeRelatorioComissaoAoSelecionarItem;
	public static boolean usaCondComercialPorCondPagto;
	public static boolean usaRegistroLogin;
	public static String configHoraLimiteDeEnvioParaDataDeEntregaDoPedido;
	public static String usaCadastroCoordenadasGeograficasCliente;
	public static boolean obrigaCadCoordenadasGeograficas;
	public static int sementeSenhaCancelamentoCadCoordenadas;
	public static boolean filtraClientePorProdutoRepresentante;
	public static boolean aplicaDescontoCCPAposInserirItem;
	private static boolean usaCameraParaLeituraCdBarras;
	public static String usaCondicaoComercialPorCliente;
	public static boolean usaVisitaFoto;
	public static boolean usaBotaoNovoPedidoNaListaPedidos;
	public static boolean usaEnvioBackgroundColetaGpsPontoEspecifico;
	public static boolean usaAuditoriaColetaGps;
	public static int vlTimeOutTentativaColetaGps;
	public static String usaDescontoMaximoItemPorCliente;
	public static String usaPctFretePorTipoPedidoTabPrecoEPeso;
	public static boolean usaControlePesoPedidoPorCondPagto;
	public static boolean usaControlePesoPedidoPorEmpresa;
	public static String usaDescontoAcrescimoMaximoEmValor;
	public static boolean usaArredondamentoIndividualCalculoTributacao;
	public static String usaDescontoPedidoPorCliente;
	public static boolean destacaProdutoComPrecoEmQueda;
	public static boolean calculaPesoGrupoProdutoNoPedido;
	public static boolean usaIndiceEspecialCondPagtoProd;
	public static String descontaComissaoRentabilidadePorItem;
	public static boolean usaRentabilidadeMinimaItemPedido;
	public static boolean usaWorkflowStatusPedido;
	public static JSONObject configDescontosEmCascata;
	public static String usaReplicacaoPedido;
	public static boolean bloqueiaSistemaEquipamentoInativo;
	public static boolean usaTipoPedidoPorCliente;
	public static String configDiasMedioCondPagto;
	public static boolean usaCalculoDeTributacaoPersonalizado;
	public static boolean mostraDescAcessoriaCapaPedido;
	public static String mostraPrecoItemSt;
	public static boolean usaFreteManualPedido;
	public static double vlPctToleranciaRentabilidadeMinima;
	public static boolean usaVerbaPorFaixaRentabilidadeComissao;
	public static boolean aplicaComissaoEspecialProdutoPromocional;
	public static boolean usaIndicacaoQuilometragemTempoNoPedido;
	public static String ordemCamposTelaInfoExtra;
	public static boolean isUsaOrdemCamposTelaInfoExtra;
	public static int nuDiasPermanenciaFotoCliente;
	public static int nuDiasPermanenciaCargaPedido;
	public static int nuDiasRestantesAvisoVencimentoCarga;
	public static int sementeSenhaFecharCargaVencida;
	public static int nuDiasValidadeCargaPedido;
	public static double qtdPesoMaximoCargaPedido;
	public static String usaCargaPedidoPorRotaEntregaDoCliente;
	public static double qtdPesoMinimoCargaPedido;
	public static int sementeSenhaFecharCargaPesoMenorMinimo;
	public static boolean usaControleEspecialEdicaoUnidades;
	public static int usaControleRentabilidadePorFaixa;
	public static int qtdItensRentabilidadeIdealSugeridos;
	public static boolean permiteFotoMenuCliente;//947-1
	public static boolean permiteFotoClientePedido;//947-2
	public static String permiteRegistrarExcluirFotoCliente;
	public static String usaControleDataEntregaPedidoPelaCarga;
	public static boolean permiteCadastrarCargaNaCapaDoPedido;
	public static boolean usaSinalizadorRentabilidadeBaseadoIndiceCalculado;
	public static double vlToleranciaIndiceMinimoRentabilidadePedido;
	public static int usaImpressaoNfeViaBluetooth;
	public static boolean usaCalculoVerbaComImpostoERentabilidade;
	public static int nuDiasValidadeCustoItem;
	public static boolean usaVerbaGrupoProdComToleranciaNoDesconto;
	public static int nuDiasRestantesAvisoSaldoVerbaGrupo;
	public static boolean usaDescontoQtdeNoGrupoDescPromocional; //1012
	public static boolean ocultaPercentualDesconto; //1012
	public static String permitePedidoAVistaClienteBloqueadoAtrasado;
	public static boolean liberaTipoCondPagtoPedidoDias;
	private static String usaRetornoAutomaticoDadosRelativosPedido;
	public static int valorTimeOutRetornoDadosRelativosPedido;
	public static String sugereImpressaoDocumentosPedidoAposEnvio;
	public static String configImpressaoNfeViaBluetooth;
	public static boolean validaClienteAtrasadoApenasAoFecharPedido;
	public static String ordemCamposTelaItemPedidoBonificacao;
	public static String permiteAtualizarManualmenteDadosCadastraisCliente;
	public static double vlIndiceSeguroCalculadoNoItemPedido;
	public static boolean aplicaVariacaoPrecoProdutoPorCliente;
	private static String configCadastroFotoNovoCliente;
	public static boolean aplicaReducaoSimplesAposCalculoValorItem;
	public static String usaPositivacaoAgendaVisitaSemPedido;
	public static String configClienteEmModoProspeccao;
	public static boolean usaClienteEmModoProspeccao;
	public static boolean permiteInativarClienteProspeccao;
	public static boolean liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido;
	public static int sementeSenhaPrecoBaseadoPercentualUsuarioEscolhido;	
	public static boolean validaPctMaxDescAcrescPorUsuario;
	public static boolean usaConfirmacaoEntregaPedidoDiaNaoUtil;
	private static String adicionaCodigoUsuarioAoNumeroPedido;
	public static boolean usaRegistroChegadaSaidaClienteVisita;
	public static String usaSugestaoRegistroChegadaNoCliente;
	public static int qtMinimaFotosCadastroNovoCliente;
	public static String configMultiplosEnderecosCliente;
	public static String configEnderecosPedido;
	public static String configMultiplosEnderecosCadastroNovoCliente; //1056
	public static String permiteDecidirModoRegistroFaltaEstoqueProduto;
	public static String usaSugestaoRegistroSaidaNoCliente;
	public static int usaImpressaoBoletoViaBluetooth;
	public static int usaImpressaoComprovanteBoleto;
	public static boolean usaOrigemPedidoPortal;
	public static String usaPrecoPadraoListaProdutos;
	public static boolean liberaComSenhaVendaProdutoSemEstoque;
	public static int sementeSenhaVendaProdutoSemEstoque;
	public static String usaListaSacCliente;
	public static boolean usaOcorrenciaSACComWorkflow;
	public static boolean ocultaContatoSAC;
	public static boolean naoPermiteExcluirAlterarSAC;
	public static String exibeRelatorioNovosSacs;
	public static int nuCasasDecimaisPisCofinsIcms;
	public static String pulaDataEntregaEmDiasDeFeriadoProxDia;
	private static String usaReservaEstoqueCentralizado;
	public static String aplicaAlteracoesCadastroClienteEnderecoAutomaticamente;
	public static int usaPedidoComplementar;
	public static boolean registraSaidaClienteAoFecharPedido;
	public static boolean usaClienteKeyAccount;
	public static boolean liberaSenhaQuantidadeMaximaVendaProduto;
	public static boolean destacaProdutoQuantidadeMaximaVenda;
	public static boolean usaDescontoPonderadoPedido;
	public static boolean restringeDescontoPedidoBaseadoMediaPonderada;
	public static String usaMultiplasLiberacoesDescontoNoPedido;
	public static boolean usaReagendamentoAgendaVisita;
	public static boolean usaMotivoReagendamentoTransferenciaAgenda;
	public static int nuDiasMaximoReagendamentoTransferencia;
	public static boolean usaTransferenciaAgendaVisitaParaAgendaAtendimento;
	public static String usaAvisoClienteSemPesquisaMercado;
	public static String usaPesquisaMercadoProdutosConcorrentes;
	public static boolean aplicaPercentualFreteCalculoPrecoItem;
	public static boolean usaSinalizadorRentabilidadeBaseadoItemTabelaPreco;
	public static String usaAvisoRentabilidadeItemAbaixoEsperado;
	public static boolean usaDestaqueStatusRentabilidadeCliente;
	public static boolean usaPctManualAcrescimoCustoNoPedido;
	public static boolean bloquearNovoPedidoClienteBloqueadoPorAtraso;
	public static boolean destacaClienteBloqueadoPorAtrasoNaLista;
	public static String usaMultiplasSugestoesProdutoFechamentoPedido;
	public static String qtLimiteItensSugestaoListasEspecificas;
	public static String qtLimiteItensMultiplasSugestoesListaGeral;
	public static boolean usaAtualizacaoEscolhaClienteEnvioEmail;
	public static String usaExpressaoRegularValidacaoSenhaUsuario;
	public static int qtMaxVendaProdutoMes;
	public static String dsTipoClienteQtMaxVendaProduto;
	public static boolean usaPctMaxParticipacaoItemBonificacao;
	public static boolean usaApenasItemPedidoOriginalNaBonificacaoTroca;
	public static String configPercentualQuantidadeOriginalBonificacaoTroca;
	public static boolean liberaSenhaQtdItemMaiorPedidoOriginal;
	public static int sementeSenhaQtdItemMaiorPedidoOriginal;
	public static boolean permitePesquisaMercadoNovoCliente;
	private static double resolucaoCameraRegistroFotos;
	public static int usaImpressaoComprovanteNfe;
	public static double vlPctLimiteSomaCanalContratoDecisaoCalculo;
	public static double vlPctLimiteContratoDecisaoCalculoDesc;
	public static boolean registraFusoHorarioVisita;
	public static String usaColetaGpsManual;
	public static int avisaColetaGpsParada;
	public static String usaAgendaVisitaFinalDeSemana;
	public static double nuMetrosToleranciaPrecisaoCoordenada;
	public static String qtMaxCaracteresOrdemCompraNoPedido;
	public static boolean exibeQuantidadeEstoqueFaltanteItemNaLista;
	public static boolean aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa;
	public static String usaCadastroCoordenadasGeograficasNovoCliente;
	public static boolean obrigaCadCoordenadasGeograficasNovoCliente;
	public static boolean restringeTabelaPrecoPorCondicaoComercial;
	public static boolean permiteCondComercialItemDiferentePedido;
	public static boolean usaTipoFretePorCliente;
	public static boolean usaCreditoIndiceCondicaoPagamentoNaBonificacao;
	public static boolean usaCreditoIndiceTipoFreteCliNaBonificacao;
	public static boolean usaValorMaximoBonificaoPorCreditoPedidoVenda;
	public static boolean usaEnvioPedidoPendenteParaAutorizacaoEquipamento;
	public static String cdStatusPedidoPendenteAprovacao;
	public static boolean usaMotivoParadaColetaGps;
	public static int nuDiasPermanenciaLogsWgps;
	public static boolean usaAplicativoExternoParaColetaGps;
	public static String usaPrecoItemPorPesoMinimoPedido;
	public static boolean usaVerbaSaldoPorFornecedor;
	public static boolean usaIncrementoQuantidadePorLeituraCodigoBarras;
	public static boolean bloqueiaSistemaColetaGpsManualDesligada;
	public static boolean usaDestaqueItensVendidosMesCorrente;
	public static String vlPctToleranciaVisitasForaAgenda;
	public static boolean liberaSenhaVisitaClienteForaAgenda;
	public static int sementeSenhaVisitaClienteForaAgenda;
	public static boolean usaGrupoDescPromocionalNoDescQtdPorGrupo;
	public static int marcaItemPedidoPendenteAprovacao;
	public static boolean permiteLiberacaoPedidoPendenteOutraOrdemLiberacao;
	public static String usaTotalizadoresEspecificosListaCliente;
	public static boolean usaControleOnlineUsuariosInativos;
	public static boolean usaAtualizacaoTipoPessoaCliente;
	public static boolean usaToleranciaVisitasForaDeAgendaPorClienteVisitado;
	public static boolean aplicaDescProgressivoPorMixPorItemFinalPedido;
	public static boolean ignoraDescontroProgressivoProdutoPromocional;
	public static boolean usaDescontoProgressivoPorCondComercial;
	public static boolean usaRelDescontosAplicadosNoItemPedidoPorFuncionalidade;
	public static String usaRamoAtividadeSugestaoProdutoComCadastro;
	public static boolean aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa;
	public static boolean geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente;
	public static boolean filtraProdutoPorGrupoCliente;
	public static boolean usaPrecoItemComValoresAdicionaisEmbutidos;
	public static boolean permiteReagendamentoAgendaParaDataIgualOriginal;
	private static String usaCalculoVolumeItemPedido;
	public static boolean usaDataLimiteValidadeAgendaVisita;
	public static boolean permiteIndicarAgendaVisitaValidaParaDataUnica;
	public static boolean usaCadastroAgendaVisita;
	public static String permiteRegistrarMotivoVisitaMultiplasVisitas;
	public static boolean usaTipoAgendaNaAgendaVisita;
	public static boolean usaTipoFretePorEstado;
	public static boolean usaDescontoPedidoPorTipoFrete;
	public static String usaDescontosAutoEmCascataNaCapaPedidoPorItem;
	public static int nuCasasDecimaisVlVolume;
	public static boolean usaCopiaAgendaRepresentanteParaSupervisor;
	public static String usaCaracteristicasClienteNoSistema;
	public static boolean usaValidacaoCamposDinamicosObrigatoriosAoFecharPedido;
	public static boolean usaTipoPessoaFisicaComoPadraoCadastroNovoCliente;
	public static boolean usaMultiplosPagamentosParaPedido;
	public static boolean usaPctMaxDescontoPagamentoPorCondPagto;
	public static boolean usaMultiplasLiberacoesParaClienteComSenhaUnica;
	public static boolean reagendaAgendaVisitaParaDiaPosteriorAnterior;
	public static String ocultaInfoRentabilidadeManualmente;
	public static String exibeAbaTotalizadoresPedido;
	public static String usaOrdenacaoRankingProduto;
	public static boolean permiteApresentarApenasVerbaUtilizadaPedido;
	public static String usaInformacoesAdicionaisPagamentoCheque;
	public static int enviaEstoqueRepParaServidor;
	public static boolean usaTextoPadraoReferenteCheque;
	public static int nuDiasMaximoVencimentoToleranciaPagamento;
	public static int usaContatoERPClienteNoPedido; 
	public static boolean usaCotaValorQuantidadeRetirarAcresCondPagto;
	public static String qtdMinimaProdutosRestricaoPromocionalPedido;
	public static boolean usaOrdenacaoEstoqueNaGradeProduto;
	public static String usaValidacaoAcrescDescMaxNoDescontoPromocional;
	public static String permiteInserirEmailAlternativoPedido;
	public static String usaHorarioLimiteColetaGpsManual;
	public static String liberaSenhaDiaEntregaPedido;
	public static int sementeSenhaDiaEntregaPedido;
	public static boolean usaDiaEntregaBairroParaCalculoDataEntrega;
	public static double usaDivisaoValorVerbaUsuarioEmpresa;
	public static double vlPctMargemDescontoItemPedido;
	public static int usaTrocaRecolherComoDescontoPagamentoPedido;
	public static double pctMaxTrocaRecolherPedido;
	public static int qtdDiasMaximoTrocaItemPedido;
	public static boolean usaNfePorReferencia;
	public static String usaValorPadraoSimFlEnviaEmail;
	public static boolean geraLogAcaoPedidoItemPedido;
	public static String nuResolucaoFotoCameraNativa; 
	public static boolean usaGradeProdutoPorTabelaPreco;
	public static boolean obrigaCampoRelacionadoCasoCampoEstejaPreenchido;
	public static String usaRateioProducaoPorRepresentante;
	public static boolean usaControleEstoqueProdutoRateioProducao;
	public static boolean apresentaDiasEntregaEmpresaCadastroEndereco;
	public static boolean liberaComSenhaCancelamentoConsignacao;
	public static int sementeSenhaLiberaComSenhaCancelamentoConsignacao;
	public static int usaImpressaoPedidoConsignacaoDevolucao;
	public static int usaImpressaoComprovanteConsignacaoDevolucao;
	public static int qtCopiasImpressaoPedidoConsignacaoDevolucao;
	public static int usaSugestaoImpressaoPedidoConsignacaoDevolucao;
	public static String nuDiasPermanenciaPedidoConsignacao;
	public static boolean usaPrecoPorUf;
    public static int nuDiasPermanenciaPedido;
    public static int nuDiasPermanenciaRecado;
    public static int nuDiasPermanenciaNovidadesProduto;
	public static boolean usaRelMetasGrupoProdIndepDoCadDeGrupoProd;
    public static String usaSenhaDoUsuarioPdaSensivelAoCase;
    public static boolean relDiferencasPedido;
	public static int nuDiasAlertaClienteSemPedido;
	public static int sementeSenhaClienteSemPedido;
	public static int nuDiasBloqueiaClienteSemPedido;
	public static boolean usaDescontoPorQuantidadeValor;
	public static boolean usaCancelamentoDePedido;//675-1
	public static boolean usaCancPedAbertoAuto;//675-2
	public static boolean usaCancPedFechadoAuto;//675-3
	public static int nuDiaSemCanPedAbertoFechadoAuto;//675-4
	public static boolean usaAreaVendaAutoNoPedido;
	public static boolean usaLoginAutomaticoSistema;
	public static boolean usaSistemaModoOffLine;
	public static int nuDiasPermanenciaMetas;
	public static String mostraQtVendasProdutoNoPeriodo;
	public static boolean usaValorBaseDoItemParaVerbaPositiva; //828-1
	public static boolean usaValorBaseDoItemParaVerbaNegativa; //828-2
	public static boolean usaValorBaseItemTabelaPrecoParaVerbaPositiva; //828-3
	public static boolean usaApenasUnidadesComPreco;
	public static String usaNomeSistemaPersonalizado;
	public static boolean calculaFecopItemPedido;
	public static boolean detalhaInfoTributariaPedidoEItemPedido;
	public static boolean mostraTributacaoItemPorItemTabelaPreco;
	public static boolean mostraVlBrutoCapaPedido;
	public static boolean calculaImpostosAdicionaisItemPedido;
	public static boolean usaConfigInfoFinanceiroDaRedeParaClientes;
	public static boolean exibeSaldoNegativo;
	public static boolean grifaProdutoSemEstoqueEmUmaGradeNaLista;
	public static String naoConsideraRegistroVisitaManualTolerancia;
	public static boolean apresentaDadosEntregaNaAbaPrincipalPedido;
	public static String usaAvisoControleInsercaoItemPedido;
	public static boolean usaDescItemPorCanalCliEGrupoProdEContratoCli;
	public static String usaNavegacaoGpsNoMapa;
	public static boolean gravaCodigoVerbaItemPedidoPorTabelaPreco;
	public static boolean usaMargemProdutoAplicadaNoValorBaseVerba;
	public static String usaLimpezaGradesNaoDisponiveisParaVenda;
	public static boolean usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli;
	public static int qtdCalculoRecursivoContratoCli;
	public static boolean usaDataCarregamentoPedido;
	public static boolean usaApresentacaoValorStCapaPedido;
	public static boolean usaAdicionaNuNotaGeralNaNotaFiscal;
	public static String usaRetornoAutomaticoDadosRelativosPedidoBackground;
	public static int nuIntervaloVerificaoConexaoEquipamento;
	public static String usaImpressaoContingenciaNfeViaBluetooth;
	public static int qtCopiasImpressaoContingenciaNfe;
	public static int usaGeracaoBoletoContingencia;
	public static String cdStatusPedidoConsignado;
	public static boolean usaDevolucaoPedidosEmConsignacao;
	public static String nuDiasValidadePedidoEmConsignacao;
	public static boolean apresentaListaConsignacoesPrimeiroLogin;
	public static boolean usaValorComStCalculoVerba;
	public static JSONObject configDescontoAcrescimoItemPedidoPorTipoPedido;  //1312
	public static boolean permiteSelecionarFotoArmazenadaCadastroFoto;
	public static boolean usaValorComImpostosControleValorMinimoPedido;
	public static boolean usaCalculoReversoNaST;
	public static String usaHoraFimAgendaVisita;
	public static boolean usaCamposDinamicosMontagemDetalhesCliente;
	public static boolean usaMotivoAgendaNaoPositivadaMultiplasEmpresas;
	public static boolean usaValorStCalculoMinimoItemPedidoPorItemTabelaPreco;
	public static boolean usaIndicacaoClienteEntregaPedido;
	public static String usaProvisionamentoConsumoVerbaSaldo;
	public static String configValorMinimoItemPedidoPorItemTabelaPreco; //1316
	public static int usaNumerosAleatoriosSalvosGeracaoSenha;
	public static String usaCadastroProdutoDesejadosForaCatalogo;
	public static int nuDiasPermanenciaRegistroProdutosDesejados;
	public static String usaSistemaIdiomaPersonalizado;
	public static int qtCaracteresMinimoDescricaoProdutoDesejado;
	public static boolean usaPedidoQualquerRepresentanteParaHistoricoCliente;  //1337
	public static boolean usaRegistroAtividadeRelacionadaPedido;
	public static String nuDiasPermanenciaAtividadePedido;
	public static String configDescQuantidadeVlBasePorPesoPedido;
	public static String usaInfoAdicionaisComprovanteNfe;
	public static boolean usaFotoPedidoNoSistema;
	public static String configDocumentosAnexo;
	private static String usaSelecaoDocumentosAnexo;
	public static int limiteMegabytes;
	public static String nuDiasPermanenciaDocumentoAnexo;
	public static String configTituloFinanceiro;
	public static boolean usaSenhaAdministracaoTabelas;
	public static int sementeSenhaAdministracaoTabelas;
	public static boolean usaFiltroTipoClienteRede;
	public static boolean usaFiltroPeriodoDataAgendaVisita;
	public static boolean usaIndicacaoDadosBancariosClienteNoPedido;
	private static String usaBloqueioEnvioPedidoProdutoRestrito;
	public static boolean usaRevalidacaoProdutoRestritosEnvioPedido;
	public static boolean geraFlSituacaoRestritoProdutoNoSync;
	public static boolean usaFretePedidoPorToneladaCliente;
	public static boolean usaPedidoComplementarMesmaConfPedidoOriginal;
	private static String naoPermiteFecharPedidoSemReservaDeEstoque;
	public static boolean usaApresentacaoProdutosPendentesRetirada;
	public static String usaAvisoVendaProdutosPendentesRetirada;
	public static boolean apresentaNovidadesClienteRelatorioNovidadeProduto;
	private static String nuDiasCancelamentoAutoPedidoCliente;
	private static String nuDiasCancelamentoAutoPedidoClienteKeyAccount;
	public static int usaInsercaoQtdDescInsercaoMultiplaSemNegociacao;
	public static boolean usaCondicaoNegociacaoNoPedido;
	public static boolean usaValidacaoEstoqueLocalEstCondNegociacao;
	public static boolean usaEstoqueInternoParcialmenteLocalEstoqueCondNeg;
	public static boolean usaLiberacaoSenhaValorAbaixoMinimoVerba;
	public static int sementeSenhaValorAbaixoMinimoVerba;
	public static boolean usaAplicacaoMaiorDescontoEmCascata;
	public static boolean naoConsideraProdutoDescPromocionalComoPromocional;
	public static boolean usaIndicacaoItemPedidoProdutoPromocional;
	private static boolean usaSelecaoUnidadeAlternativaCapaPedido;
	public static String usaDescontosEmCascataManuaisNaCapaPedidoPorItem;
	public static boolean realizaCadastroAgendaVisitaAoReagendar;
	public static boolean usaRelacionamentoProdutosSAC;
	public static boolean usaInsercaoItensDiferentesLeituraCodigoBarras;
	public static boolean usaAgrupamentoPedidoEmConsignacao;
	public static boolean usaIndiceGrupoProdutoTabPrecoCondPagto;
	public static boolean usaRelacionamentoProdutosSA;
	public static String nuDiasPermanenciaLogSyncBackground;
	public static boolean usaCalculoComissaoPorTabelaPrecoEGrupoProduto;
	public static boolean usaLimiteCreditoCompartilhadoEntreEmpresas;
	private static String dsOpcoesFaltaDescAcrescPorUsuario;
	public static String usaConfirmacaoCPFCNPJIgualNovoCliente;
	public static String usaMultiplasSugestoesProdutoInicioPedido;
	public static boolean usaDescQuantidadeApenasEmbalagemCompleta;
	public static boolean usaLimiteCreditoRedeCompartilhadoEmpresas;
	public static boolean usaTipoPagamentoRestritoVenda;
	public static boolean permiteAlterarValorItemComIPI;
	public static boolean validaPeriodoEntregaParaPedido;
	public static String configEscolhaTransportadoraNoPedido;
	public static String usaConfirmacaoSubstituicaoCadastroAgendaVisita;
	public static int sementeSenhaConfirmacaoSubstituicaoCadastroAgendaVisita;
	public static boolean usaEscolhaTransportadoraAoFecharPedido;
	public static boolean usaDestaqueTransportadoraCIFNaLista;
	public static boolean usaCalculoTransportadoraMaisRentavelPedido;
	public static boolean usaTransportadoraPreferencialCliente;
	public static boolean permiteCopiaDePedidoAberto;
	public static boolean usaTransportadoraAuxiliar;
	public static String usaDescontoPorVolumeVendaMensal;
	public static boolean usaGerenciaDeCreditoDesconto;
	public static double vlPctAvisaCreditoDesconto;
	public static boolean naoAplicaIndiceClienteNoVlBaseVerba;
	public static JSONObject configValorFreteManual;
	public static boolean mostraValorBruto;
	public static boolean mostraValorDesconto;
	public static int usaQtMinProdutoPorTabelaPreco;
	public static String localApresentacaoRelGiroProduto;
	public static boolean usaQuantidadeMinimaDescPromocional;
	public static boolean usaFiltroApenasGruposProdutosExistentesCargaRepresentante;
	public static boolean usaInsercaoQuantidadeDesejadaPedido;
	public static boolean grifaProdutoComVidaUtilLoteCriticoNaLista;
	public static boolean usaVerbaPositivaApenasPedidoCorrente;
	public static boolean marcaNovoClienteParaAnalise;
	public static String nuDiasPermanenciaAnaliseNovoCliente;
	public static boolean usaMultiploEspecialPorGradeProdutoPromocional;
	public static String usaQtdeMinimaProdutoPorCondPagamento;
	public static String formaValidacao;
	private static String configGeracaoPdfPedido;
	private static boolean usaFocoCampoBuscaAutomaticamente;
	private static boolean usaTeclaEnterComoConfirmacaoItemPedido;
	public static boolean usaBotaoIgnorarValidacoesPedido;
	private static boolean apresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido;
	public static boolean permiteItemPedidoComQuantidadeZero;
	public static int sementeSenhaLiberacaoRentabilidadeMinima;
	public static int usaImpressaoNotaPromissoriaViaBluetooth;
	public static boolean usaBotaoGiroProdutoItemPedido; 
	public static boolean usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco;
	public static boolean usaTabelaPrecoComVigencia;
	public static boolean usaCondPagtoTabPrecoComVigencia;
	public static String mostraColunaMarcaNaListaDeProdutos;
	public static boolean exibeCidadeEstadoNoMenuCliente;
	public static boolean apresentaListaSociosClienteNosDetalhes;
	public static boolean ocultaCamposDescontosImpressaoContingenciaNfe; 
	public static boolean usaFiltroGrupoProdutoListaItemPedido; 
	public static boolean usaSolicitacaoDadosPessoaisAcessoSistema;
	public static boolean usaEntregaPedidoBaseadaEmCadastro;
	public static boolean usaOrdenacaoNuSequenciaGradeProduto;
	public static String usaPreenchimentoCamposNovoClientePorCnpj;
	private static int mostraFotoProdutoNaListaProdutos;
	private static String mostraEstoquePrevistoNaListaProdutos;
	public static String usaCamposAdicionaisImpressaoLayoutNfe;
	private static int usaCalculoGastoTotalVariavelPorProduto;
	public static boolean usaProdutoSimilarProdutoInexistente;
	private static String configMargemContribuicao;
	public static boolean consomePrecoMinimoItemBonificado;
	public static boolean ignoraValidacaoValorMinPedidoConformeProdutoVendido;
	public static int nuTentativasLogin;
	public static int nuMinutosTentativasLogin;
	private static int usaDescontosCategoriaClienteEmCascataManuaisNaCapaPorPedido;
	public static boolean usaAcessoDiretoGradeProdutosItemPedido;
	public static boolean usaAcessoItemInseridoModoEdicao;
	public static boolean usaIndiceFinanceiroTipoPagamentoPagamentoPedido;
	public static String usaImpressaoNfceViaBluetooth;
	public static boolean usaApresentacaoFixaTicketMedioCapaPedido;
	public static boolean usaApresentacaoFixaTicketMedioListaAgendaVisita;
	public static String nuCamposExibidosSemQuantidadeFracionada;
	public static boolean validaSaldoPedidoBonificacao;
	public static boolean liberaComSenhaSaldoBonificacaoExtrapolado;
	public static int sementeSenhaSaldoBonificacaoExtrapolado;
	public static int nuLinhasRetornoBuscaSistema; //1157
	public static boolean apresentaMensagemLimiteNuLinhasRetornoBuscaSistema; //1157
	private static int usaSugestaoVendaPersonalizavelInicioPedido;
	public static String dsInformacoesComplementaresListaSugestaoVendaPerson;
	private static String dsOrdenacaoPadraoListaSugestaoVendaPerson;
	private static String qtLimiteProdutosExibidosListaSugestaoVendaPerson;
	public static int qtLimiteSugestaoVendaPersExibidasNaTela;
	public static boolean naoAplicaIndiceUnidadeAlternativaValorBaseRentabilidade;
	public static boolean filtraGrupoProdutoPorFornecedor;
	public static boolean exibeHistoricoEstoqueCliente;
	public static boolean exibeHistoricoQtdeVendaDoItem;
	public static boolean exibeOpcoesNavegacaoNasCombos;
	public static boolean calculaRentabilidadePedidoRetornado;
	public static int sementeSenhaControleDataHoraNoAcessoAoSistema;
	public static boolean utilizaApenasControleDeDataAcessoSistema;
	public static int nuPeriodoDiasClienteRecenteNaBase;
	private static String configUsoMarcadores;
	private static String localUsoMarcadoresProduto;
	public static String usaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso;
	public static boolean validaClienteAtrasadoPorValorTotalTitulosEmAtrasoApenasAoFecharPedido;
	public static boolean usaControleEstoquePorRemessa;
	public static int nuDiasPermanenciaRemessaEstoque;
	public static boolean usaInfoAdicionalImpressaoNfeVendaCigarros;
	public static boolean apresentaMultiplosPagamentosImpressaoNfe; //1535 (usa)
	public static String origemDescricaoPagamento; //1535 (origemDescricaoPagamento)
	public static boolean apresentaSecaoTransportadoraImpressaoNfe;
	public static int usaGeracaoNfePedidoAposFechamento;
	public static boolean listaChaveAcessoNotasRemessaImpressaNfe;
	public static String usaGeracaoNotaNfeContingenciaPedido;
	public static boolean permiteEditarPrimeiroDescontoEmCascataManualNaCapaDoPedido;
	public static boolean usaImpressaoReciboPagadorBoleto;
	public static String dsLinhasTextoResponsabilidadeCedenteBoletoContingencia;
	public static boolean usaApenasGiroProdutoPorTabelaPrecoPedido;
	public static boolean usaGeracaoTxtNfe;
	public static boolean usaRetornoAutomaticoDadosNfe;
	public static boolean usaRetornoAutomaticoDadosBoleto;
	public static boolean usaRetornoAutomaticoDadosErpDif;
	public static boolean usaRetornoAutomaticoDadosNfce;
	public static boolean usaRetornoAutomaticoValidacaoSEFAZ;
	public static boolean usaLinhaSeparadoraItensImpressao;
	public static int nuDiasPermanenciaRegistroValorizacaoProd;
	public static boolean calculaPrazoEntregaPorProduto;
	public static boolean usaFechamentoDeVendasPorPeriodo;
	public static boolean usaFiltroTituloFinanceiroPorTipoPagamento;
	public static String usaCamposAdicionaisImpressaoPedido;
	public static boolean isUsaCamposAdicionaisImpressaoPedido;
	public static boolean usaIndicacaoCNPJTransportadoraFreteFOB;
	public static String configPedidoAbertoComIndicacaoOrcamento;
	public static boolean usaPedidoAbertoComIndicacaoOrcamento;
	public static boolean enviaPedidoEmOrcamentoParaSupervisor;
	public static int modoSugestaoEnvioPedidoEmOrcamento;
	public static boolean usaModoControleEstoquePorTipoPedido;
	public static String configCadastroDescontoPromocional;
	public static boolean grifaMetaNaoAtingida;
	public static boolean consideraTotalRealizadoParaCalculoMeta;
	public static boolean apresentaInformacoesAdicionaisRelatorioMetaVendaCliente;
	public static String usaConfigFechamentoDiarioVendas;
	public static String usaImpressaoFechamentoDiarioVendas;
	public static String dsDadosAdicionaisImpressaoFechamentoDiarioVendas;
	public static String nuDiasPermanenciaFechamentoDiario;
	public static String usaImpressaoFinanceiroFechamentoDiario;
	public static int nuDiasPermanenciaRegistroNovoCliente;
	public static String nuDiasPermanenciaRegistroCargaProduto;
	public static int usaVerificacaoFotoPedidoPresentePedido;
	public static String ocultaRentabilidadePedido;
	public static boolean usaDevolucaoTotalEstoqueRemessaRepresentante;
	public static String usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante;
	public static String nuDiasPermanenciaDadosEstoqueRep;
	public static boolean exibeGrupoProdutoListaMaximizada;
	public static boolean usaHistoricoVendasPorListaColunaTabelaPreco;
	public static String configuracoesIgnoradasReplicacaoPedido;
	public static String aplicaTaxaAntecipacaoNoItem;
	public static String nuDiasExpiracaoSenha;
	public static int nuRegistrosControleRepeticaoSenhaUsuario;
	public static boolean usaListaRestricaoSenhaUsuario;
	public static String mensagemExpressaoRegularValidacaoSenhaUsuario;
	public static boolean permiteAlterarVencimentoConformeCondPagto;
	public static boolean usaCondicaoPagamentoRepresentante;
	public static boolean permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega;
	public static boolean emiteBeepDeErro;
	public static boolean geraNovoPedidoDiferencas;
	private static String configModoResumoDiario;
	public static String aplicarDescontosIndicesParaSaldoVerbaNegativo;
	public static String aplicaDescontosSequenciaisNoItemPedido;
	private static String configSugestaoVendaBaseadaEmComboProdutos;
	public static boolean usaNovidadeNovoClienteNaoIntegrado;
	public static String filtraItensComErroNaInsercaoMultiplaItens;
	public static double aplicaDescontoNaCapaDoPedido;
	public static boolean apresentaPopUpErroEnvioPedidoAoSairSistema;
	public static boolean apresentaPopUpErroEnvioPedidoNovoPedido;
	public static boolean apresentaPopUpErroEnvioPedidoAcessoTelaPorMenu;
	public static String nuOrdemLiberacaoPedidoPendente;
	public static boolean usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto;	
	public static boolean usaFotoProdutoPorEmpresa;
	public static boolean habilitaEquipamentoParaUsoPorPadrao;
	public static boolean permiteRentabilidadeNegativaPedido;
	public static boolean padronizaListaContatoEAniversariante;
	public static boolean usaGeracaoRemessaEstoqueNoEquipamento;
	public static int valorTimeOutRetornoDadosRelativosRemessaEstoque;
	public static boolean usaMelhorRotaSistema; //798-1
	public static boolean automatizaCalculoRota; //798-2
	public static String usaCancelamentoNfePedido; //1589
	public static String configEstoquePrevisto; //1691;
	public static int nuDiasObrigaFechamentoVendas; //1597
	public static boolean usaDevolucaoRemessaEstoqueNoEquipamento; //1630
	public static boolean apresentaEstoqueDaRemessaEmpresaNaListaProdutos; //1635
	public static boolean liberaPedidoPendenteComSenhaPorDescontoMaximo; //1638
	public static int sementeSenhaDescontoMaximoPedidoPendente; //1639
	public static boolean usaVlBaseVerbaEDescMaximoPorRedutorCliente; //1641
	private static String configControleEstoquePorLoteProduto; //1642
	public static boolean controlaExibicaoComboTipoPedidoPorPreferenciaFuncao; //1650
	public static boolean usaFiltroComissao; //1656
	private static String usaEnvioRecadoPedido; //1657
	public static boolean aplicaConversaoUnidadeNaValidacaoQtMaxVenda; //1659
	public static boolean apresentaUnidadeFracionadaDoProduto; //1660
	public static boolean permiteIncluirMesmoProdutoLoteDiferenteNoPedido; //1661
	public static String apresentaSaldoVerbaPedido; //1668
	private static int usaSelecaoUnidadePorGrid; //1669
	public static boolean usaDicionarioCorrecaoNoFiltroDeProduto; //1671
	public static boolean permiteIndicarRecebimentoSMSNoCadastroCliente; //1672
	public static boolean ocultaProdutoSemEstoqueListaSugestaoVendaPerson; //1673
	public static boolean usaRelatorioMetaVendaCliente; //1674
	public static String bloqueiaCondPagtoPorCliente;	
	public static boolean usaValidacaoCPFCNPJBaseWeb; //1676
	public static int usaImpressaoInventarioRemessaEstoque; //1677
	public static int vlTimeOutValidacaoCPFCNPJDuplicado; //1678
	public static String somaValorSTNoValorTotalNfe; //1683
	public static boolean apresentaPrecoCondComercialCli; //1684
	public static String configCatalogoProduto; //1685
	private static String usaInfoComplementarItemPedido; //1686
	public static boolean aplicaDescontoNoItemDeAcordoComICMSdoCliente; //1687
	public static boolean controlaDescontoUsandoVerbaAtravesQtMinItens; //1690
	public static boolean atualizaChaveNfeContingencia; //1693
	public static boolean validaRegraProdutoRestritoEPromocionalSeparadamente; //1694
	public static boolean naoConsomeVerbaAutomaticamenteAoFecharPedido; //1699
	public static boolean liberaComSenhaPedidoComSaldoVerbaExtrapolado; //1700
	public static int sementeSenhaPedidoComSaldoVerbaExtrapolado; //1701
	public static boolean permiteAcessoAoMenuProdutoAtravesRelNovidade; //1702
	public static boolean usaVencimentosAdicionaisBoleto; //1703
	public static boolean usaOrdenacaoVerbaItemPedido; //1704
	public static boolean apresentaConsumoVerbaDePedidoNaoTransmitido; //1706
	public static boolean usaDescPromo;//1707
	public static boolean ignoraLocalSemDescPro;//1707
	public static boolean restringeItemPedidoPorLocal;//1707
	public static boolean exibeOpcaoTodosFiltroLocal;//1707
	public static String mostraListaEstoqueGradeFaltante; //1708
	private static int modoDeIdentificarSemanaDoMesNaAgendaVisita; //1709
	public static String localApresentacaoMultiplasSugestoesProduto; //1710
	public static String usaFiltroGradeProduto; //1712
	public static int restauraBackupAutoAposAtualizarVersao;//1713
	public static String nivelLogSyncBackground; //1714
	public static boolean usaDescMaxProdCli; //1715
	public static boolean usaIndiceClienteGrupoProd; //1724
	public static boolean naoCalculaDadosResumoDiaPda; //1716
	private static String configOrigemDadosEscolhaTransportadoraNoPedido; //1711
	public static String usaFiltroAplicacaoDoProduto; //1717
	public static boolean usaFiltroCodigoAlternativoProduto; //1718 
	public static boolean usaQuebraLinhaDescricaoProdutoNaLista; //1720
	public static boolean usaColetaInfoAdicionaisEscolhaItemPedido; //1721
	public static boolean usaDescontoMultiplosPagamentosParaPedido;//1728
	public static boolean mantemPrecoNegociadoReplicacaoPedido;//1722
	public static String configFiltroListaProdutos; //1723
	public static boolean validaQtMaxVendaPorGrade; //1727
	public static boolean permiteAlternarEmpresaDuranteCadastroPedido; //1729
	public static boolean usaCorCadastroMarcador; //1731
	public static boolean usaVolumeVendaMensalRede; //1738
	public static String configColunasGridDescontoVolumeVendasMensal; //1739
	public static boolean mostraVolumeVendasMensalNoDescontoQuantidade; //1740
	public static String configColunasGridDescontoQuantidade; //1741
	public static boolean permiteAlterarVencimentoConformeCliente; //1743
	public static boolean habilitaAtualizacaoCadastroCliente; //1744
	public static int nuDiasAtualizaCadastroCliente; //1745
	public static String usaCalculoVpcItemPedido; //1746
	public static JSONObject configCalculoComissao;//1747
	public static String configCalculoComissaoPorFaixaDesconto;//1748
	public static boolean liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado; //1749
	public static int sementeSenhaPedidoBonificacaoComSaldoVerbaExtrapolado; //1750
	public static boolean usaProdutoRestrito; //1751
	public static String informaUfNaoPermiteInscEstadualVazio; //1752
	public static boolean usaValorRetornoProduto; //1753
	public static int nuDiaClienteAbertura; //1754
	public static int nuDiaClienteFundacao; //1755
	public static boolean apresentaValorTotalPedidoComTributosEFrete; //1756
	public static boolean usaTabelaPrecoPorTipoPedido; //1759
    public static boolean descontaIcmsRentabilidade; //1760
	public static boolean usaVerbaSaldoOnline; //1761
	public static boolean apresentaListaClienteRedeTituloFinanceiro; //1762 
	public static boolean usaFreteValorUnidade; //1763
	public static boolean usaInterpolacaoPrecoProduto; //1764
	public static String configNotaCreditoParaPedido;
	public static boolean ocultaValorFretePedido; //1771
	public static int controlaDescontoUsandoVerbaAtravesMixItens; //1772
	public static boolean enviaProtocoloPedidosEmailHtml; //1776
	public static boolean marcaClienteOcultoReplicaoNovoCliente; //1775 
	public static String marcaPedidoPendenteBaseadoLimiteCredito; //1769
	public static boolean gravaFotosGaleriaEquipamento; //1777
	public static String cdStatusPedidoPendenteAprovacaoLimCredito; //1773
	public static JSONObject configDescPromocionalRegraInterpolacaoPoliticaDesconto; //1774
	public static boolean exibeFracaoEmbalagemFornecedorItemPedido; //1779
	public static boolean usaRecalculoValoresDosPedidos;
	public static String configColunasGridLoteProduto; //1736
	public static int nuDiasAposEmissaoRecalValPed;
	public static int nuDiasAposUltimoRecalculoValPed;
	public static boolean usaRecalculoValoresDosPedidosInicioDoMes;
	public static boolean usaCriacaoPedidoErpCancelado; //1778
	public static boolean calculaPrecoPorMetroQuadradoUnidadeProduto; //1781
	public static String filtrosFixoTelaListaCliente; //1782
	public static String configOrdenacaoProdutoLista; //1799
	private static String configValoresMinimos; //1803
	public static boolean usaVerbaPositivaPorGrupoProdutoTabelaPreco; // 1804
	public static boolean usaVerbaPorFaixaMargemContribuicao; //1805
	
	public static int formulaCalculoRentabilidadeNoPedido; //16
	public static List<String> usaImpressaoModuloPagamento; //1688
	public static String configPersonalizadaParaDiferencasPedido; //1766
	public static boolean usaTabelaPrecoPorCanalAtendimento; //1780
	public static boolean usaValorMinimoParaPedidoPorFuncao; //1784
	public static boolean usaValorMinimoParaPedidoPorGrupoCliente; //1785	
	public static double valorMinimoPedidoPessoaFisica;
	public static double valorMinimoPedidoPessoaJuridica;
	public static boolean usaDestaqueClienteSemLimiteCredito; //1783
	public static String configIndiceFinanceiroCondPagamento; //1795
	public static boolean calculaPrecoPorVolumeProduto; //1802
	public static boolean apresentaValorVendaItemPedidoRedeCliente; //1806
	public static int nuDiasConsideraPedido; //1806-2
	public static boolean usaDescQuantidadePorPacote; //1808-1
	public static boolean usaTabPrecoDescQuantidadePorPacote; //1808-2
	public static boolean usaDescontoExtra;
	public static boolean usaDescontoMaximoPorCondicaoPagamento; //1810
	private static String configCadastroFotoProdutoNaPesquisaDeMercado; //1811
	private static String configCadastroCoordenadaNaPesquisaDeMercado; //1812
	private static String configPesquisaCliente; //1813
	private static String marcaPedidoPendenteComItemBonificado; //1814
	private static String liberaComSenhaRestauracaoBackup; //1816
	public static String configLiberacaoComSenhaCondPagamento; //1817
	public static String configCalculoFretePersonalizado; //1820
	public static boolean usaDivulgaInformacao; // 1821-1
	public static boolean obrigaVerTodosDivulgaInformacao; // 1821-2
	public static String dirImagemDivulgaInformacao; // 1821-3
	public static String nuMaxWidthHeightImagemDivulgaInformacao; // 1821-4
	public static int nuApresentaDivulgacao; // 1821-5
	public static int nuTimeOutImagemUrl; // 1821-6
	public static boolean baixaImagemLinkAoExibir; // 1821-7
	public static boolean permiteIgnorarRecalculoCondicaoPagamento; //1822
	public static boolean usaControlePontuacao; // 1826-1
	public static String mostraPontuacaoPedido; // 1826-2
	public static boolean mostraSeloPontuacaoItem; // 1826-3
	public static String mostraPontuacaoListaItem; // 1826-4
	public static boolean mostraExtratoPontuacao; // 1826-5
	public static String mostraPontuacaoResumoDia; // 1826-6
	public static int nuCasasDecimaisPontuacao; // 1826-7
	public static boolean ignoraExtratoApp; // 1826-8
	public static boolean ocultaValorPontuacaoBaseExtrato; // 1826-9
	public static int tipoCalculoPontuacao = 1; // 1826-10
	public static boolean usaValorTotalPedidoFaixaDias ; // 1826-11
	public static boolean usaConversaoUnidadeAlternativaPesoPontuacao ; // 1826-12
	private static int usaConfigProspect;// 1828
	public static boolean ignoraSeparadoresProspectCpfCnpj;// 1828
	public static String nmCampoValidaDataProspect; //1828
	public static int nuAnoValidaDataProspect;//1828
	public static int nuDiasPermanenciaRegistroProspect;//1828
	public static int permiteTirarFotoProspect;//1828
	public static boolean permiteInserirFotoCadastradaProspect; //1828
	public static boolean permiteCadastroCoordenada;//1828
	public static boolean permiteAnexarDocumentosProspect;//1828
	public static boolean permiteCarregarCamposPorCnpjViaWS;//1828
	public static String dsUrlWsParaCarregarCamposCnpj;//1828
	public static JSONObject configPoliticaComercial; //1831
	public static boolean usaGondolaPedido; //1832
	private static String configCategoriaProduto; // 1833;
	public static String configMotivoPendencia; // 1834
	public static boolean usaPedidoProdutoCritico; //1835-1
	public static boolean permiteApenasUmItemPedidoProdutoCritico; //1835-2
	public static boolean filtraProdutosPedidoNaoCritico; //1835-3
	private static String configPesquisaMercadoProdutoConcorrente; //1837
	public static JSONObject configMargemRentabilidade; //1838
	public static String configCadastroMetasPlataformaVenda; //1839
	public static boolean usaRadarRepresentante; //1840
	public static String configSolicitaAutorizacao; //1842
	public static boolean geraNovidadeAutorizacao; //1842-2
	public static boolean permiteInserirEmailAlternativoPedEmOrcamento; //1574-4
	public static String configHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica; //1846
	public static boolean usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica; //1846-1
	public static boolean usaGeracaoVisitaProspectNovoCliente; //1848
	public static boolean bloquearRegistroChegadaRepNaoPresente; //1847
	public static int nuMetrosToleranciaRepresentantePresenteCliente; //772
	public static boolean ocultaComissaoCapaPedidoConformeLayoutItemPedido; //1849
	public static boolean usaPedidoPerdido; //1850-1
	public static String cdStatusPedidoPerdido; //1850-2
	public static int nuDiasPermanenciaPedidoPerdido; //1850-3
	public static boolean usaAppExternoMenuCliente; //1851-1
	public static String nmPacoteAppExternoMenuClienteAndroid; //1851-2
	public static String nmAppExternoMenuClienteAndroid; //1851-3
	public static String urlAppExternoMenuClienteIOS; //1851-4
	public static boolean usaMarcadorPedido; //1852
	public static String configFiltroEstoqueDisponivel; //1853
	public static String configConversaoTipoPedido; //1856
	public static Set<String> syncWebAppEntidadeRepZero; //1857
	public static boolean usaPriorizacaoPesquisaItemPedido; //1858
	public static String configListaItensDoPedido; //1859
	public static boolean mostraRentabPraticadaSugerida; //1860
	public static boolean usaPercDescGrupoProdutoOuClienteVip; //1871
	public static String configListaNovoCliente; //1861
	public static boolean ocultaCondPagtoPorCondComercial; //1864
	public static String configHistoricoTrocaDevolucao; //1865
	public static boolean replicaDadosItemTabelaPreco0PorTabelaPreco; //1870
	public static boolean apresentaHistoricoTitulosAtrasados; //1866
	private static String marcaPedidoPendenteAprovacaoCondPagto; //1867
	public static String configLaudoDap; //1873
	public static boolean usaInversaoIndiceFinanceiroClienteCondPagto; //1874
	public static int nuCasasDecimaisBoleto; //1879
	public static String pedidoViaCampanhaPublicitaria; //1880
	public static String configListaProdutoAgrupadorGrade; //1882-1
	private static String permiteOcultarValoresItemAgrupadorGrade; //1882-2
	private static String configReservaEstoqueCorrente;//1891
	public static boolean usaCotacaoMoedaProduto; //1893
	public static boolean mostraInfoEstoqueUnidades; //1895
	public static String configPoliticaBonificacao; //1889
	public static String usaTecladoWMWJson;
	public static String gerarPdfBoletoNotaFiscal; //1900
	public static String configFotoProdutoGrade; //1901
	public static boolean usaCarrosselProdutosGradeDetalheItem; //1902
	public static String usaCatalogoExterno; //1903
	public static String configPrioridadeLiberacaoPendenciaPedido;//1904
	public static String configMenuCatalogo;//1905
	public static boolean usaSugestaoVendaPorDivisao; //1907
	public static boolean apresentaDsFichaTecnicaCapaItemPedido; //1909
	private static String configVideosProdutos; //1910
	public static boolean mostraBonusListaProduto; //1914
	public static String configCampoDinamicoIndicadorProdutividadeInterno; //1915
	public static String configStatusItemPedido; //1916
	public static boolean aplicaApenasDescQtdOuIndiceCondPagto; //1918
	public static boolean usaHistoricoAtendimentoUnificado; //1921
	public static boolean ocultaSementeTelasGeracaoSenha; //1923
	public static String configInterfaceSistema;//1925
	public static boolean usaEscolhaModoFeira; //1926
	public static boolean limpaQtItemFisicoMudancaUnidade; //1929;
	public static String configApresentacaoInfosPersonalizadasCapaItemPedido; //1931
	public static boolean apresentaFiltroRepresentanteListaProdutosCargaSupervisor; //1933
	public static String configNumeroRegistrosMostradosPorVezNaLista; //1936
	public static boolean usaNovoPedidoOrcamentoSemRegistroChegada; //1937
	private static String controleLoginUsuario; //1938
	public static boolean usaFiltroProdutoCondicaoPagamentoRepresentante; //1940
	public static boolean usaFiltroDeProdutoNaListaDePedido; //1941
	public static String configLeads; //1942
	public static boolean exibirNotasDevolucaoNaFichaFinanceira; //1945
	public static boolean permiteMultAgendasNoDiaMesmoCliente; //1946
	public static boolean permiteIniciarPedidoSemCliente; //1947

	public static int intervaloSyncAutomaticoApp2Web;
	public static int intervaloSyncAutomaticoWeb2App;
	
	public static boolean mostraEstoqueCorrente;
	public static boolean isConfigReservaEstoqueCorrente;
	private static boolean permiteDescCascataCategoria;
	public static boolean usaIgnoraControleVerbaNoTipoPedido;
	public static boolean mostraAbaNfeNoPedido;
	public static boolean mostraAbaBoletoNoPedido;
	public static boolean mostraAbaNfceNoPedido;
	public static String dsOrdenacaoListaSugPerson;
	public static String sentidoOrdenacaoListaSugPerson;
	public static int qtLimiteProdutosSug;
	public static boolean apresentaIndicadoresCompraHistoricoClienteListaAgendas;
	public static boolean apresentaIndicadoresCompraHistoricoClienteListaClientes;
	public static boolean apresentaIndicadoresClienteRiscoChurn;
	public static int nuArredondamentoRegraInterpolacaoUnit;
	public static int nuArredondamentoRegraInterpolacaoTotal;
	public static int nuCasasDecimaisComissao;
	public static boolean isNuCasasDecimaisComissaoLigado;
	public static boolean usaPesquisaMercado;
	public static boolean excluiPesquisaMercadoPedido;
	public static boolean isVerificaPedidoJaEnviadoReservaEstoqueComTrigger;
	public static String appExternoPesquisaMercado;
	public static String pacoteAppExternoPesquisaMercado;
	public static String pacoteAppExternoPesquisaMercadoUrl;
	public static boolean contabilizaAcessoExternoPesquisaMercado;
	public static boolean ocultaGrupoProduto1;
	public static boolean ignoraNovaFotoEmissaoPedido;
	public static boolean ocultaItens;
	public static boolean ocultaNotaFiscalPedidoNaoRetornado;
	public static boolean apresentaMarcadorProdutoInsercao;
	public static boolean apresentaMarcadorProdutoInseridos;
	public static boolean apresentaMarcadorProdutoLista;
	public static boolean apresentaMarcadorGrade;
	public static boolean usaImagemQrCode;
	public static boolean usaFiltroGrupoFaceamento;
	public static boolean usaFiltroAtivoFaceamento;
	public static int nuMinutosResetBloqueioUsuario;
	public static boolean usaConfigAcessoSistema;
	public static int nuMinutosAvisoBloqueioSistema;
	public static int sementeSenhaLiberacaoSisBloqueado;
	


	public static final String QTDESTOQUEPEDIDO = "13";
	public static final String QTDEMBALAGEM = "21";
	public static final String QTDDESEJADACOMPRA = "123";
	public static final String QTDESTOQUEVALIDAVENDIDANOPEDIDO = "62"; //Quantidade com Estoque Válido Vendida no Pedido
	public static final String QTDESTOQUEPREVISTO = "137"; //Quantidade do Estoque Previsto

	public static final int sleep_Time_Background_Thread = 3000;
	public static final int LIMITE_MAXIMO_VERIFICAO_FOTO_LOCAL_EXISTE = 50000;
	
	public static final String JSON_KEY_EXIBEINFOFRETECAPAPEDIDO = "exibeInformacoesFreteCapaPedido";
	
	private static Hashtable jsonParams;

	

	private LavenderePdaConfig() {
	}

	public static void loadParametros() throws SQLException {
		jsonParams = new Hashtable(0);
		loadValorParametros();
		loadParametrosExtras();
	}

	public static void loadParametrosExtras() throws SQLException {
		mostraAbaNfeNoPedido = usaGeracaoNfePedidoAposFechamento() || NfeService.getInstance().isExisteNfeParaPedidos();
		mostraAbaBoletoNoPedido = isApresentaAbaBoleto() || PedidoBoletoService.getInstance().isExisteBoletoParaPedidos();
		usaIgnoraControleVerbaNoTipoPedido = TipoPedidoService.getInstance().isPossuiTipoPedidoComFlIgnoraControleVerba();
		mostraAbaNfceNoPedido = isUsaImpressaoNfceViaBluetooth() && NfceService.getInstance().isExisteNfceParaPedidos();
	}

	public static void loadValorParametros() throws SQLException {
		Hashtable hashValorParametros = getHashValorParametros();
		/*1*/	cdStatusPedidoAberto = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CDSTATUSPEDIDOABERTO);
		/*2*/	cdStatusPedidoFechado = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CDSTATUSPEDIDOFECHADO);
		/*4*/	usaTipoFretePedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOFRETEPEDIDO);
		/*5*/	configTransportadoraPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGTRANSPORTADORAPEDIDO);
		/*6*/	tipoPedidoOcultoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.TIPOPEDIDOOCULTONOPEDIDO));
		/*7*/	configTipoPagamento = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGTIPOPAGAMENTO);
		/*9*/	configCondicaoPagamentoNoPedido	= getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONDICAOPAGAMENTOOCULTONOPEDIDO);
		/*10*/	nuDiasPrevisaoEntrega = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPREVISAOENTREGA));
		/*11*/	previsaoEntregaOcultaNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PREVISAOENTREGAOCULTANOPEDIDO));
		/*12*/	nuCasasDecimais = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUCASASDECIMAIS));
		if (ValueUtil.VALOR_NAO.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUCASASDECIMAIS))) {
			nuCasasDecimais = 2;
		}
		ValueUtil.doublePrecision = nuCasasDecimais;
		ValueUtil.doublePrecisionInterface = nuCasasDecimais;
		/*295*/ String vlParam = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUCASASDECIMAISARMAZENAMENTO);
		if (!ValueUtil.isEmpty(vlParam)) {
			nuCasasDecimaisArmazenamento = ValueUtil.getIntegerValue(vlParam);
			if (nuCasasDecimaisArmazenamento > 0) {
				ValueUtil.doublePrecision = nuCasasDecimaisArmazenamento;
			}
		}
		/*13*/  condicaoPagamentoSemCadastro = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONDICAOPAGAMENTOSEMCADASTRO));
		/*14*/  separadorCondicaoPagamentoSemCadastro = (ValueUtil.isEmpty(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEPARADORCONDICAOPAGAMENTOSEMCADASTRO))) ? 'N' : (getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEPARADORCONDICAOPAGAMENTOSEMCADASTRO)).toCharArray()[0];
		/*15*/  maiorParcelaCondicaoPagamentoSemCadastro = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MAIORPARCELACONDICAOPAGAMENTOSEMCADASTRO));
		/*16*/	configRentabilidadeNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGRENTABILIDADENOPEDIDO);
		formulaCalculoRentabilidadeNoPedido = getFormulaCalculo();
		/*17*/	String vlMinimoParaPedido = ValueUtil.isNotEmpty(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDO)) ? getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDO).replaceAll(",", ".") : "";
		loadVlMinimoPedido(vlMinimoParaPedido);
		/*18*/	camposDsPagamentoFichaFinanceira = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CAMPOSDSPAGAMENTOFICHAFINANCEIRA));
		/*19*/	configIndiceFinanceiroClienteVlItemPedido = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGINDICEFINANCEIROCLIENTEVLITEMPEDIDO), ValorParametro.CONFIGINDICEFINANCEIROCLIENTEVLITEMPEDIDO);
		/*22*/	maximoItensPorPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MAXIMOITENSPORPEDIDO));
		if (maximoItensPorPedido == 0) {
			maximoItensPorPedido = 500;
		}
		/*24*/	configIndiceFinanceiroCondPagto = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO), ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
		/*25*/	bloquearNovoPedidoClienteBloqueado	= ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEARNOVOPEDIDOCLIENTEBLOQUEADO));
		vlParam = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGRESTRICAOVENDACLIENTEPRODUTO);
		/*26*/  usaRestricaoVendaClienteProduto = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGRESTRICAOVENDACLIENTEPRODUTO);
		/*26-1*/nuDiaSemanaVigenciaSemanal = getVlParamAsInt(getValorAtributoJson(vlParam, "nuDiaSemanaVigenciaSemanal", ValorParametro.CONFIGRESTRICAOVENDACLIENTEPRODUTO));
		/*27*/  cadastroNovoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CADASTRONOVOCLIENTE);
		/*30*/	enviarEmailPedidoAutoCliente = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.ENVIAREMAILPEDIDOAUTOCLIENTE);
		/*38*/	permiteDescontoAcrescimoItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDESCONTOACRESCIMOITEMPEDIDO);
		/*39*/	controlarLimiteCreditoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLARLIMITECREDITOCLIENTE));
		/*40*/	bloquearLimiteCreditoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEARLIMITECREDITOCLIENTE));
		vlParam = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGAGRUPADORSIMILARIDADEPRODUTO);
		/*43-1*/ usaAgrupadorSimilaridadeProduto = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGAGRUPADORSIMILARIDADEPRODUTO);
		/*43-2*/ ocultaListaSimilaresDescQuantidade = isAtributoJsonLigado(vlParam, "ocultaListaSimilaresDescQuantidade", ValorParametro.CONFIGAGRUPADORSIMILARIDADEPRODUTO);
		/*43-3*/ usaSomenteQtdDescQtdSimilar = isAtributoJsonLigado(vlParam, "usaSomenteQtdDescQtdSimilar", ValorParametro.CONFIGAGRUPADORSIMILARIDADEPRODUTO);
		/*45*/	usaRegistroVisitaSupervisor = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREGISTROVISITASUPERVISOR));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCPROGRESSIVOPERSONALIZADO);
		/*46-1*/ usaDescProgressivoPersonalizado = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGDESCPROGRESSIVOPERSONALIZADO);
		/*46-2*/ ignoraOutroPedidoAbertoDescProg = isAtributoJsonLigado(vlParam, "ignoraOutroPedidoAbertoDescProg", ValorParametro.CONFIGDESCPROGRESSIVOPERSONALIZADO);
		/*46-3*/ exibeAlteracaoDescProgressivo = getVlParamAsInt(getValorAtributoJson(vlParam, "exibeAlteracaoDescProgressivo", ValorParametro.CONFIGDESCPROGRESSIVOPERSONALIZADO));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCLIENTESEMPEDIDOFORNECEDOR);
		/*55-1*/ usaClienteSemPedidoFornecedor = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGCLIENTESEMPEDIDOFORNECEDOR);
		/*55-2*/ nuDiasClienteSemPedidoFornecedor = getVlParamAsInt(getValorAtributoJson(vlParam, "nuDiasFiltro", ValorParametro.CONFIGCLIENTESEMPEDIDOFORNECEDOR));
		/*58*/  usaStatusClienteVinculado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASTATUSCLIENTEVINCULADO));
		/*59*/  nuLayoutImpressaoPedidoViaBluetooth = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NULAYOUTIMPRESSAOPEDIDOVIABLUETOOTH));
		vlParam = getValorParametroPorSistema(hashValorParametros, ValorParametro.USAWEBSERVICESANKHYACOMPLEMENTAPEDIDO);
		/*57-1*/ usaWebserviceSankhyaComplementaPedido = isAtributoJsonLigado(vlParam, "usa", ValorParametro.USAWEBSERVICESANKHYACOMPLEMENTAPEDIDO);
		/*57-2*/
		String timeOut = getValorAtributoJson(vlParam, "timeOut", ValorParametro.USAWEBSERVICESANKHYACOMPLEMENTAPEDIDO);
		timeOutWebserviceSankhyaComplementaPedido = timeOut.contains(";") ? ValueUtil.getIntegerValue(timeOut.split(";")[0]) : ValueUtil.getIntegerValue(timeOut);
		if (timeOutWebserviceSankhyaComplementaPedido > 0) {
			timeOutWebserviceSankhyaComplementaPedido *= 1000;
		} else {
			timeOutWebserviceSankhyaComplementaPedido = 10000;
		}
		/*57-3*/ consultaSqlWebserviceSankhyaComplementaPedido = getValorAtributoJson(vlParam, "consultaSql", ValorParametro.USAWEBSERVICESANKHYACOMPLEMENTAPEDIDO);
		/*58*/  usaStatusClienteVinculado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASTATUSCLIENTEVINCULADO));
		/*67*/	cdStatusNovoClienteFechado = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CDSTATUSNOVOCLIENTEFECHADO);
		/*79*/	bloquearVendaProdutoSemEstoque = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEARVENDAPRODUTOSEMESTOQUE));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80*/	usarDescontoPorQuantidadeItemPedido = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-2*/ liberaDescAcrescManualAuto = isAtributoJsonLigado(vlParam, "liberaDescAcrescManualAuto", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-3*/ usaVigenciaDescQuantidade = isAtributoJsonLigado(vlParam, "usaVigencia", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-4*/ ignoraAcrescimo = isAtributoJsonLigado(vlParam, "ignoraAcrescimo", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-5*/ ocultaBotaoIgnorar = isAtributoJsonLigado(vlParam, "ocultaBotaoIgnorar", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-6*/ apresentaFiltroDescQtd = isAtributoJsonLigado(vlParam, "apresentaFiltroDescQtd", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-7*/ permiteVincularCliente = isAtributoJsonLigado(vlParam, "permiteVincularCliente", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*80-8*/ ocultaPercentualDescontoListaFaixa = isAtributoJsonLigado(vlParam, "ocultaPercentualDescontoListaFaixa", ValorParametro.CONFIGDESCONTOPORQUANTIDADEITEMPEDIDO);
		/*81*/	String vlQtMinimaCaracteresFiltroProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMINIMACARACTERESFILTROPRODUTO);
		if ("0".equals(vlQtMinimaCaracteresFiltroProduto)) {
			vlQtMinimaCaracteresFiltroProduto = StringUtil.getStringValue(ValorParametro.PARAM_INT_VALOR_ZERO);
		}
		qtMinimaCaracteresFiltroProduto = ValueUtil.getIntegerValue(vlQtMinimaCaracteresFiltroProduto);
		/*82*/	usaTabelaPrecoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORCLIENTE));
		/*83*/	usarFlStatusClienteDaFichaFinanceira = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USARFLSTATUSCLIENTEDAFICHAFINANCEIRA));
		/*84*/	nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASLIBERACAOCOMSENHACLIENTEATRASADONOVOPEDIDO));
		/*85*/	sementeSenhaClienteAtrasadoNovoPedido	= ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHACLIENTEATRASADONOVOPEDIDO));
		/*87-1*/  atualizarEstoqueInterno = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ATUALIZARESTOQUEINTERNO), "usa", ValorParametro.ATUALIZARESTOQUEINTERNO);
		/*87-2*/  ocultaRecalculaEstoque = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ATUALIZARESTOQUEINTERNO), "ocultaRecalculaEstoque", ValorParametro.ATUALIZARESTOQUEINTERNO);
		/*89*/	usarCondicaoPagtoPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARCONDICAOPAGTOPORTABELAPRECO));
		/*90*/	permiteTabPrecoItemDiferentePedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITETABPRECOITEMDIFERENTEPEDIDO);
		/*91*/	usaPrecoPorUf = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAPRECOPORUF));
		/*95*/	usaConversaoUnidadesMedida = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONVERSAOUNIDADESMEDIDA));
		/*96*/	ocultaQtItemFisico = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAQTITEMFISICO));
		/*97*/	cdStatusPedidoTransmitido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CDSTATUSPEDIDOTRANSMITIDO);
		/*99*/	ocultaEstoque = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAESTOQUE);
		/*100*/	ocultaFichaFinanceira = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAFICHAFINANCEIRA);
		if (ValueUtil.VALOR_SIM.equals(ocultaFichaFinanceira)) {
			ocultaFichaFinanceira = "1";
		}
		/*101*/	permiteValorZeroPedidoEItem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEVALORZEROPEDIDOEITEM));
		/*102*/	usaEstoqueOnline = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAESTOQUEONLINE));
		/*103*/	bloqueiaTabPrecoPadraoClienteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIATABPRECOPADRAOCLIENTENOPEDIDO));
		/*104*/	usaFiltroSomenteDescricaoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROSOMENTEDESCRICAOPRODUTO));
		/*105*/	nuDiasPermanenciaPedido = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAPEDIDO));
		/*106*/	usaQtdItemPedidoInteiro = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAQTDITEMPEDIDOINTEIRO);
		/*107*/	usaCondicaoPagamentoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOPAGAMENTOPORCLIENTE));
		/*108*/	String jsonGeraParcelasPorTipoCondPgto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GERAPARCELASPORTIPOCONDPGTO);
			    geraParcelasPorTipoCondPgto = getValorAtributoJson(jsonGeraParcelasPorTipoCondPgto, "usa", ValorParametro.GERAPARCELASPORTIPOCONDPGTO);
				permiteEditarVecimentoParcela = isGeraParcelasPorTipoCondPgto() && !LavenderePdaConfig.isGeraParcelasEmPercentual() && isAtributoJsonLigado(jsonGeraParcelasPorTipoCondPgto, "permiteEditarVecimentoParcela", ValorParametro.GERAPARCELASPORTIPOCONDPGTO);
				sugereAlteracaoProximosVencimentos = isAtributoJsonLigado(jsonGeraParcelasPorTipoCondPgto, "sugereAlteracaoProximosVencimentos", ValorParametro.GERAPARCELASPORTIPOCONDPGTO);
		/*110*/	permiteDescAdicionalPorTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDESCADICIONALPORTABPRECO));
		/*113*/ usaTabelaPrecoPorRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORREPRESENTANTE));
		/*114*/	usaMuralDeRecados = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMURALDERECADOS));
		/*115*/	naoConsisteDescontoAcrescimoMaximo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOCONSISTEDESCONTOACRESCIMOMAXIMO);
		/*117*/	configModuloRecado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMODULORECADO);
		/*123*/	configProdutoControlado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIAPRODUTOCONTROLADOCLIENTESEMALVARA);
		/*124*/	valorMinimoParaPedidoPorTabelaPreco = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDOPORTABELAPRECO);
		/*125*/	geraVerbaSaldo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GERAVERBASALDO));
		/*128*/	controleNivelCriticoBateria = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLENIVELCRITICOBATERIA));
		/*129*/	persisteVerbaSaldoRep = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERSISTEVERBASALDOREP));
		/*131*/	grifaClienteBloqueado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRIFACLIENTEBLOQUEADO));
		String vlParamFiltroGrupoProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROGRUPOPRODUTO);
		/*132*/	usaFiltroGrupoProduto = getMaiorVlParam(getValorAtributoJson(vlParamFiltroGrupoProduto, "usa", ValorParametro.USAFILTROGRUPOPRODUTO));
		ocultaGrupoProduto1 = isAtributoJsonLigado(vlParamFiltroGrupoProduto, "ocultaGrupoProduto1", ValorParametro.USAFILTROGRUPOPRODUTO);
		usaFiltroGrupoFaceamento = isAtributoJsonLigado(vlParamFiltroGrupoProduto, "usaFiltroGrupoFaceamento", ValorParametro.USAFILTROGRUPOPRODUTO);
		/*133*/	configInsercaoMultiplosItensPorVezNoPedido = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO), ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
		/*135*/	geraCargaQuandoAtualizacaoMuitoGrande = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.GERACARGAQUANDOATUALIZACAOMUITOGRANDE));
		/*136*/	sementeSenhaControleVencimentoCertificadoIos = ValueUtil.getIntegerValue(getValorAtributoJson(getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGDIAWI), "sementeSenhaControleSeguranca", ValorParametro.CONFIGDIAWI));
		if (sementeSenhaControleVencimentoCertificadoIos == 0) {
			sementeSenhaControleVencimentoCertificadoIos = 5000;
		}
		/*140*/	configAgendaDeVisitas = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGAGENDADEVISITAS);
		/*141*/	bloquearExclusaoRecado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEAREXCLUSAORECADO));
		/*142*/	nuDiasPermanenciaRecado = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIARECADO));
		/*144*/	consisteConversaoUnidades = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSISTECONVERSAOUNIDADES);
		/*145*/	consisteConversaoUnidadesPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSISTECONVERSAOUNIDADESPORCLIENTE));
		/*149*/ configListaClientes = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLISTACLIENTES);
		/*150*/	ocultaDescontoPorQuantidadeItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTADESCONTOPORQUANTIDADEITEMPEDIDO));
		/*151*/	ocultaColunaCdProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.OCULTACOLUNACDPRODUTO));
		/*152*/	ignoraEscolhaAutoMelhorTabPrecoDescPorQtd = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAESCOLHAAUTOMELHORTABPRECODESCPORQTD));
		/*154*/	avisaClienteAtrasadoNovoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISACLIENTEATRASADONOVOPEDIDO));
		/*156*/	usaPesquisaInicioString = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPESQUISAINICIOSTRING));
		/*157*/	usaFiltroProdutoPorTabelaPreco = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROPRODUTOPORTABELAPRECO);
		/*158*/	nivelRegistroDeLogNoPda = LogPda.getCdNivelInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NIVELREGISTRODELOGNOPDA));
		/*163*/ nuTruncamentoRegraDescontoVerba = getVlParamAsInt(getValorParametroPorSistema(hashValorParametros, ValorParametro.USATRUNCAMENTOREGRAINTERPOLACAO));
		nuTruncamentoRegraDescontoVerba = nuTruncamentoRegraDescontoVerba <= 0 ? ValueUtil.doublePrecision : nuTruncamentoRegraDescontoVerba;
		/*164*/ nuArredondamentoRegraInterpolacao = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUARREDONDAMENTOREGRAINTERPOLACAOCAMPOINDIVIDUAL);
		String[] vlParams = nuArredondamentoRegraInterpolacao.split(";");
		nuArredondamentoRegraInterpolacaoTotal = getVlParamAsInt(vlParams[0]);
		nuArredondamentoRegraInterpolacaoTotal = nuArredondamentoRegraInterpolacaoTotal <= 0 ? ValueUtil.doublePrecision : nuArredondamentoRegraInterpolacaoTotal;
		if (vlParams.length > 1) {
			nuArredondamentoRegraInterpolacaoUnit = getVlParamAsInt(vlParams[1]);
			nuArredondamentoRegraInterpolacaoUnit = nuArredondamentoRegraInterpolacaoUnit <= 0 ? ValueUtil.doublePrecision : nuArredondamentoRegraInterpolacaoUnit;
		} else {
			nuArredondamentoRegraInterpolacaoUnit = nuArredondamentoRegraInterpolacaoTotal;
		}
		/*165*/	configDescricaoEntidadesCliente = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGDESCRICAOENTIDADESCLIENTE);
		/*167*/	usaHistoricoTabelaPrecoUsadasPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAHISTORICOTABELAPRECOUSADASPORCLIENTE));
		/*173*/	usaHistoricoItemPedido = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAHISTORICOITEMPEDIDO), "usa", ValorParametro.USAHISTORICOITEMPEDIDO);
		/*174*/	configKitProduto = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGKITPRODUTO), ValorParametro.CONFIGKITPRODUTO);
		/*175*/	usaFiltroFornecedor = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROFORNECEDOR));
		/*176*/	propagaUltimoDescontoItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PROPAGAULTIMODESCONTOITEMPEDIDO);
		if (ValueUtil.VALOR_SIM.equals(propagaUltimoDescontoItemPedido)) {
			propagaUltimoDescontoItemPedido = "1";
		}
		/*177-1*/ geraNovidadeProdutoParaEntradaNoEstoque = isAtributoJsonLigado(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.GERANOVIDADEPRODUTOPARAENTRADANOESTOQUE), "usa", ValorParametro.GERANOVIDADEPRODUTOPARAENTRADANOESTOQUE);
		/*177-2*/ ocultaQtdEstoqueNovidade = isAtributoJsonLigado(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.GERANOVIDADEPRODUTOPARAENTRADANOESTOQUE), "ocultaQtdEstoqueNovidade", ValorParametro.GERANOVIDADEPRODUTOPARAENTRADANOESTOQUE);
		/*178*/	usaRelNovidadeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELNOVIDADEPRODUTO));
		/*183*/ sementeSenhaFechamentoDiario = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAFECHAMENTODIARIO));
		/*186*/	nuDiasPermanenciaNovidadesProduto = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIANOVIDADESPRODUTO));
		/*187*/ usaCodigoRepresentanteErp = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACODIGOREPRESENTANTEERP));
		/*188*/	bloqueiaClienteAtrasadoNovoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIACLIENTEATRASADONOVOPEDIDO));
		/*189*/	nuMaxDiasPrevisaoEntregaDoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUMAXDIASPREVISAOENTREGADOPEDIDO);
		/*190*/	aplicaIndiceFinanceiroClientePorPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAINDICEFINANCEIROCLIENTEPORPEDIDO);
		/*191*/	usaListaColunaPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USALISTACOLUNAPORTABELAPRECO));
		/*193*/	usaRotaDeEntregaNoPedidoSemCadastro = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAROTADEENTREGANOPEDIDOSEMCADASTRO));
		/*194*/	usaTipoDeEntregaNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPODEENTREGANOPEDIDO));
		/*195*/	ignoraPrecoMinimoListaColunaPorTabelaPreco = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAPRECOMINIMOLISTACOLUNAPORTABELAPRECO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAPRECOMINIMOLISTACOLUNAPORTABELAPRECO))) {
			ignoraPrecoMinimoListaColunaPorTabelaPreco = 1;
		}
		/*196*/	consistePrecoMaximoListaColunaPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSISTEPRECOMAXIMOLISTACOLUNAPORTABELAPRECO));
		/*197*/	ocultaHistoricoItemPedidoAutomatico = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAHISTORICOITEMPEDIDOAUTOMATICO));
		/*198*/	mostraVlPrecoFabrica = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAVLPRECOFABRICA));
		/*199*/	configPrecoMaximoConsumidor = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPRECOMAXIMOCONSUMIDOR);
		/*201*/	configNuOrdemCompraClienteNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGNUORDEMCOMPRACLIENTENOPEDIDO);
		/*205*/	permiteVenderProdutoMenorDescontoQuantidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEVENDERPRODUTOMENORDESCONTOQUANTIDADE));
		/*206*/	configLiberacaoComSenhaLimiteCreditoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
		/*207*/	sementeSenhaLimiteCreditoCliente =  ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHALIMITECREDITOCLIENTE));
		/*208*/	liberaComSenhaPrecoDeVenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAPRECODEVENDA));
		/*209*/	sementeSenhaPrecoDeVenda = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHAPRECODEVENDA));
		/*210*/	sementeSenhaTipoPedido = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHATIPOPEDIDO));
		/*212*/	marcaRecadoComoLidoAutomaticamente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MARCARECADOCOMOLIDOAUTOMATICAMENTE));
		/*213*/	bloqueiaCondPagtoPorDiasCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIACONDPAGTOPORDIASCLIENTE);
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIACONDPAGTOPORDIASCLIENTE))) {
			bloqueiaCondPagtoPorDiasCliente = "1";
		}
		int param216 = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEREABRIRPEDIDOFECHADO));
		/*216*/	permiteReabrirPedidoFechado = param216 == 1 || param216 == 2;
		/*220*/	ordemCamposTelaItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ORDEMCAMPOSTELAITEMPEDIDO);
		isUsaOrdemCamposTelaItemPedido = isOrdemCamposValida(ordemCamposTelaItemPedido);
		/*221*/	filtrosFixoTelaItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTROSFIXOTELAITEMPEDIDO);
		/*222*/	mostraTodosCamposAutomaticamenteTelaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRATODOSCAMPOSAUTOMATICAMENTETELAITEMPEDIDO));
		/*223*/	limpaFiltroProdutoAutomaticamente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIMPAFILTROPRODUTOAUTOMATICAMENTE));
		/*224*/	configUnificaBotoesEnviarReceberDados = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGUNIFICABOTOESENVIARRECEBERDADOS), ValorParametro.CONFIGUNIFICABOTOESENVIARRECEBERDADOS);
		/*225*/	obrigaLeituraRecados = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGALEITURARECADOS));
		/*226*/	aplicaIndiceFinanceiroClientePorItemFinalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAINDICEFINANCEIROCLIENTEPORITEMFINALPEDIDO));
		/*227*/	aplicaDescontoProgressivoPorItemFinalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOPROGRESSIVOPORITEMFINALPEDIDO));
		/*228*/	aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAINDICEFINANCEIROCONDPAGTOLINHAPRODITEMPEDIDO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAINDICEFINANCEIROCONDPAGTOLINHAPRODITEMPEDIDO))) {
			aplicaIndiceFinanceiroCondPagtoLinhaProdItemPedido = 1;
		}
		/*229*/	aplicaDescontoCCPPorItemFinalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOCCPPORITEMFINALPEDIDO));
		/*230*/	aplicaSomenteMaiorDescontoPorItemFinalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICASOMENTEMAIORDESCONTOPORITEMFINALPEDIDO));
		/*236*/	mostraEstoquePrevisto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAESTOQUEPREVISTO));
		/*237*/	mostraPrecosPorPrazoMedioItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPRECOSPORPRAZOMEDIOITEMPEDIDO));
		/*238*/	usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALOTEPRODUTOCOMPOSSIBILIDADEDESCONTOPORPCTVIDAUTIL));
		/*253*/	pctVidaUtilLoteProdutoCritico = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PCTVIDAUTILLOTEPRODUTOCRITICO));
		/*254*/	usaVerbaItemPedidoPorItemTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBAITEMPEDIDOPORITEMTABPRECO));
		/*255*/	usaFreteNoPedidoPorItemBaseadoNoItemTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFRETENOPEDIDOPORITEMBASEADONOITEMTABPRECO));
		/*257*/	mostraColunaQtdNaListaDePedidos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRACOLUNAQTDNALISTADEPEDIDOS);
		if (ValueUtil.VALOR_SIM.equals(mostraColunaQtdNaListaDePedidos)) {
			mostraColunaQtdNaListaDePedidos = "itens";
		}
		/*258*/	clienteComContratoExigeSetorPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CLIENTECOMCONTRATOEXIGESETORPEDIDO));
		/*259*/	mostraFotoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAFOTOPRODUTO));
		/*262*/	mostraDetalhesAdicionaisNosRelatoriosDeMetas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRADETALHESADICIONAISNOSRELATORIOSDEMETAS));
		/*263*/	usaRegistroManualDeVisitaSemAgenda = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREGISTROMANUALDEVISITASEMAGENDA);
		/*264*/	obrigaEnvioDePedidosPorTempoDecorrido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGAENVIODEPEDIDOSPORTEMPODECORRIDO));
		/*265*/	obrigaEnvioDePedidosPorQtdPedidosPendentes = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGAENVIODEPEDIDOSPORQTDPEDIDOSPENDENTES));
		/*266*/	sementeSenhaObrigaEnvioDePedidos = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHAOBRIGAENVIODEPEDIDOS));
		/*267*/	bloqueiaProdutoBloqueadoNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIAPRODUTOBLOQUEADONOPEDIDO);
		/*268*/	usaRegistroMotivoNaoVendaProdutoSugerido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREGISTROMOTIVONAOVENDAPRODUTOSUGERIDO));
		/*269*/	obrigaReceberDadosBloqueiaUsoSistema = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGARECEBERDADOSBLOQUEIAUSOSISTEMA);
		/*270*/	sementeSenhaObrigaReceberDadosBloqueiaUsoSistema = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHAOBRIGARECEBERDADOSBLOQUEIAUSOSISTEMA));
		/*271*/	obrigaReceberDadosBloqueiaNovoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGARECEBERDADOSBLOQUEIANOVOPEDIDO);
		/*272*/	sementeSenhaObrigaReceberDadosBloqueiaNovoPedido = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHAOBRIGARECEBERDADOSBLOQUEIANOVOPEDIDO));
		/*274*/	permiteDescontoEmValorPorPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDESCONTOEMVALORPORPEDIDO));
		/*275*/	configRelGiroProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RELGIROPRODUTO);
		/*276*/	configGeracaoSenhaSupervisor = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGGERACAOSENHASUPERVISOR);
		/*278*/	configPesquisaMercado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPESQUISADEMERCADO);
		loadConfigsPesquisaMercado(configPesquisaMercado, ValorParametro.USAPESQUISADEMERCADO);
		/*280*/	configModuloDeContatosDoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
		/*284*/	qtDiasPermanenciaPesquisaDeMercado = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.QTDIASPERMANENCIAPESQUISADEMERCADO));
		/*285*/	mostraRelAniversariantesAposLogin = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RELANIVERSARIANTES));
		/*287*/ configPedidoExclusivoTrocaRecolher = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPEDIDOEXCLUSIVOTROCARECOLHER);
		/*287-1*/ usaPedidoExclusivoTrocaRecolher = getValorAtributoJson(configPedidoExclusivoTrocaRecolher, "usa", ValorParametro.CONFIGPEDIDOEXCLUSIVOTROCARECOLHER);
		if (ValueUtil.VALOR_NAO.equals(usaPedidoExclusivoTrocaRecolher)) {
			usaPedidoExclusivoTrocaRecolher = "";
		}
		/*288*/	usaCestaPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACESTAPROMOCIONAL));
		/*292*/	usaCodigoClienteUnico = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACODIGOCLIENTEUNICO));
		/*293*/ usaNmFantasiaNoLugarDaRazaoSocialDoCliente = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USANMFANTASIANOLUGARDARAZAOSOCIALDOCLIENTE));
		/*296*/	usaAreaVendas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAREAVENDAS));
		/*297*/	ocultaTabelaPrecoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTATABELAPRECOPEDIDO));
		/*298*/	naoSolicitaMotivoVisitaClienteForaSequencia = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOSOLICITAMOTIVOVISITACLIENTEFORASEQUENCIA));
		String vlParamFiltroPrincipioAtivoNoProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROPRINCIPIOATIVONOPRODUTO);
		/*300*/ usaFiltroPrincipioAtivoNoProduto = getVlParamAsInt(getValorAtributoJson(vlParamFiltroPrincipioAtivoNoProduto, "usa", ValorParametro.USAFILTROPRINCIPIOATIVONOPRODUTO));
		usaFiltroAtivoFaceamento = isAtributoJsonLigado(vlParamFiltroPrincipioAtivoNoProduto, "usaFiltroAtivoFaceamento", ValorParametro.USAFILTROPRINCIPIOATIVONOPRODUTO);
		/*303*/	configCalculaStItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULASTITEMPEDIDO);
		/*304*/	usaRelMetasProdutoIndependenteDoCadDeProdutos = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USARELMETASPRODUTOINDEPENDENTEDOCADDEPRODUTOS));
		/*306-1*/ mostraColunaPrecoNaListaDeProdutos = getValorAtributoJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRACOLUNAPRECONALISTADEPRODUTOS), "usa", ValorParametro.MOSTRACOLUNAPRECONALISTADEPRODUTOS);
		/*306-2*/ mostraPrecoUnidadeItem = isMostraColunaPrecoNaListaDeProdutos() && isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRACOLUNAPRECONALISTADEPRODUTOS), "mostraPrecoUnidadeItem", ValorParametro.MOSTRACOLUNAPRECONALISTADEPRODUTOS);
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBIRNOTASFISCAISPEDIDO);
		/*307*/	exibirNotasFiscaisPedido = isAtributoJsonLigado(vlParam, "usa", ValorParametro.EXIBIRNOTASFISCAISPEDIDO);
		ocultaItens = isAtributoJsonLigado(vlParam, "ocultaItens", ValorParametro.EXIBIRNOTASFISCAISPEDIDO);
		ocultaNotaFiscalPedidoNaoRetornado = isAtributoJsonLigado(vlParam, "ocultaNotaFiscalPedidoNaoRetornado", ValorParametro.EXIBIRNOTASFISCAISPEDIDO);
		/*308*/	ocultaQtItemFaturamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAQTITEMFATURAMENTO));
		/*309*/	mostraQtdPorEmbalagemProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAEMBALAGEMPRODUTO));
		/*310*/	usaFiltroFornecedorAutoAposSalvarNovo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROFORNECEDORAUTOAPOSSALVARNOVO));
		/*311*/	excluiRecadoAutomaticamenteAposLeitura = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXCLUIRECADOAUTOMATICAMENTEAPOSLEITURA));
		/*312*/	ocultaHoraRegistroChegadaSaida = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAHORAREGISTROCHEGADASAIDA));
		/*313*/	usaSenhaDoUsuarioPdaSensivelAoCase = getValorParametroPorSistema(hashValorParametros, ValorParametro.USASENHADOUSUARIOPDASENSIVELAOCASE);
		/*314*/	travaTipoPagtoPadraoClienteEspecial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.TRAVATIPOPAGTOPADRAOCLIENTEESPECIAL));
		/*315*/	sugereEnvioAutomaticoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SUGEREENVIOAUTOMATICOPEDIDO));
		/*316*/	configPedidoProducao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPEDIDOPRODUCAO);
		/*317*/	mostraFlexPositivoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAFLEXPOSITIVOPEDIDO);
		/*318*/	aplicaDescontoPromocionalItemTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOPROMOCIONALITEMTABPRECO));
		/*319*/	restringeTabPrecoEspecialConformePadraoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RESTRINGETABPRECOESPECIALCONFORMEPADRAOCLIENTE));
		/*320*/	restringeTabPrecoCondPagtoClienteEspecial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RESTRINGETABPRECOCONDPAGTOCLIENTEESPECIAL));
		/*323*/ configValorMinimoDescPromocional = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGVALORMINIMODESCPROMOCIONAL);
		/*324*/	permiteDescontoPercentualPorPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDESCONTOPERCENTUALPORPEDIDO));
		/*326*/	configCalculoPesoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULOPESOPEDIDO);
		/*327*/ configFiltrosListaPedidos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
		/*329*/	usaTecladoVirtual = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATECLADOVIRTUAL));
		BaseEdit.tecladoAwaysVisible = usaTecladoVirtual;
		EditMemo.tecladoAwaysVisible = usaTecladoVirtual;
		/*333*/	usaModuloTrocaNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMODULOTROCANOPEDIDO));
		/*334*/	percentualLimiteTrocaNoPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERCENTUALLIMITETROCANOPEDIDO));
		if (percentualLimiteTrocaNoPedido > 100) {
			percentualLimiteTrocaNoPedido = 100;
		}
		/*335*/	percentualToleranciaTrocaNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERCENTUALTOLERANCIATROCANOPEDIDO);
		/*336*/ indiceFinanceiroCondPagtoPorDias = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.INDICEFINANCEIROCONDPAGTOPORDIAS));
		/*339*/	anulaIndiceFinanceiroCondPagtoTabPrecoProm = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ANULAINDICEFINANCEIROCONDPAGTOTABPRECOPROM));
		/*340*/	mostrarPercDescontoMaximo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARPERCDESCONTOMAXIMO));
		String vlParamQtCaracteresObservacoesPedidoItemPedido = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.QTCARACTERESOBSERVACOESPEDIDOITEMPEDIDO);
		/*341*/ qtCaracteresObservacoesPedidoItemPedido = getQtCaracteresObservacoesPedidoItemPedido(vlParamQtCaracteresObservacoesPedidoItemPedido);
		/*344*/	usaCampanhaDeVendasPorCestaDeProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACAMPANHADEVENDASPORCESTADEPRODUTOS);
		/*346*/	mostraColunaEstoqueNaListaDeProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRACOLUNAESTOQUENALISTADEPRODUTOS);
		/*347*/	relResumoPedidos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RELRESUMOPEDIDOS));
		/*348*/	permiteDescValorPorPedidoConsumindoVerba = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDESCVALORPORPEDIDOCONSUMINDOVERBA));
		/*349*/	qtMinimaItensParaLiberarVerbaPorPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMINIMAITENSPARALIBERARVERBAPORPEDIDO));
		/*351*/ usaIndiceFinanceiroSupRep = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICEFINANCEIROSUPREP));
		/*352*/	calculaStSimplificadaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULASTSIMPLIFICADAITEMPEDIDO));
		/*357*/	usaFiltroAtributoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROATRIBUTOPRODUTO));
		/*358*/	ignoraFiltroAutoFornecedorPorAtributoProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAFILTROAUTOFORNECEDORPORATRIBUTOPRODUTO);
		/*365*/	sugereEnvioAutomaticoRecado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SUGEREENVIOAUTOMATICORECADO));
		/*369*/	usaObservacaoPorItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAOBSERVACAOPORITEMPEDIDO));
		/*372*/	labelGruposProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LABELGRUPOSPRODUTO);
		/*374*/	indiceFinanceiroCondPagtoVlBase = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.INDICEFINANCEIROCONDPAGTOVLBASE));
		/*375*/	configFiltroProdutoCodigo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGFILTROPRODUTOCODIGO);
		/*376*/	relDiferencasPedido = loadVlParamRelDifPedido(hashValorParametros);
		/*378*/
		String vlAvisaUsuarioSobreConsumoVerba = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISAUSUARIOSOBRECONSUMOVERBA);
		if (ValueUtil.isNotEmpty(vlAvisaUsuarioSobreConsumoVerba)) {
			avisaUsuarioSobreConsumoVerba = Arrays.asList(vlAvisaUsuarioSobreConsumoVerba.split(";"));
		}
		/*384*/	geraApresentaTicketMedioDiario = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GERAAPRESENTATICKETMEDIODIARIO));
		/*385*/	mostraVlCotacaoDolarPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAVLCOTACAODOLARPEDIDO));
		/*386*/	valorMinimoParaPedidoPorCondPagto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDOPORCONDPAGTO);
		/*389*/	setValorTimeoutReadConexaoPda(ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.VALORTIMEOUTREADCONEXAOPDA))); //Deve multiplicar por mil porque � milisegundos
		/*391*/	configModuloRiscoChurn = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGMODULORISCOCHURN);
		/*394*/ valorMinimoParaPedidoPorTipoPagamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDOPORTIPOPAGAMENTO));
		/*395*/ usaVigenciaNaVerbaPorItemTabPreco = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVIGENCIANAVERBAPORITEMTABPRECO);
		/*397*/ aplicaDescontoQuantidadeVlBaseFlex = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOQUANTIDADEVLBASEFLEX));
		/*398*/ bloqueiaTipoPagamentoNivelSuperior = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIATIPOPAGAMENTONIVELSUPERIOR));
		/*399*/ grifaClienteAtrasado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRIFACLIENTEATRASADO));
		/*400*/ quantidadeMinimaItensPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QUANTIDADEMINIMAITENSPEDIDO));
		/*401*/	ticketMedioEmpresa = ValueUtil.getDoubleValue(StringUtil.getStringValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.TICKETMEDIOEMPRESA)).replaceAll("N", "-1").replaceAll(",", "."));
		/*403*/ aplicaMultiploEspecialAutoItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAMULTIPLOESPECIALAUTOITEMPEDIDO));
		/*406*/ consisteConversaoUnidadesMultiploEspecial = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSISTECONVERSAOUNIDADESMULTIPLOESPECIAL);
		/*406-5*/ informaQuantidadePrimeiroMultiploNaoAtingido = isAtributoJsonLigado(consisteConversaoUnidadesMultiploEspecial, "informaQuantidadePrimeiroMultiploNaoAtingido", ValorParametro.CONSISTECONVERSAOUNIDADESMULTIPLOESPECIAL);
		/*407*/ avisaConversaoUnidades = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISACONVERSAOUNIDADES));
		/*408*/ configMotivoTrocaPorItem = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMOTIVOTROCAPORITEM);
		/*409*/ avisaVendaProdutoSemEstoque = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISAVENDAPRODUTOSEMESTOQUE);
		/*410*/ horaLimiteParaEnvioPedidos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.HORALIMITEPARAENVIOPEDIDOS);
		/*411*/ selecionaTabelaPromocionalAoClicarNoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SELECIONATABELAPROMOCIONALAOCLICARNOPRODUTO));
		/*412*/	sementeSenhaHoraLimiteEnvioPedidos = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHAHORALIMITEENVIOPEDIDOS));
		/*413*/	configBonificacaoItemPedido = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros , ValorParametro.CONFIGBONIFICACAOITEMPEDIDO), ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
		/*414*/	sementeSenhaBonificarProdutoPedido = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHABONIFICARPRODUTOPEDIDO));
		/*415*/ usaOrdenacaoPorCodigoListaProdutos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDENACAOPORCODIGOLISTAPRODUTOS));
		/*416*/ permitePedidoNovoCliente = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGPEDIDONOVOCLIENTE);
		/*419*/ bloqueiaAlterarFreteDoItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIAALTERARFRETEDOITEMPEDIDO));
		/*420*/ usaFreteApenasTipoFob = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFRETEAPENASTIPOFOB));
		/*421*/ tabelasDadosParaTodosRepSup = getValorParametroPorSistema(hashValorParametros, ValorParametro.TABELASDADOSPARATODOSREPSUP);
		if (ValueUtil.isNotEmpty(tabelasDadosParaTodosRepSup)) {
			tabelasDadosParaTodosRepSup = tabelasDadosParaTodosRepSup.toUpperCase();
			if (!tabelasDadosParaTodosRepSup.endsWith(";")) {
				tabelasDadosParaTodosRepSup += ";";
			}
			if (!tabelasDadosParaTodosRepSup.startsWith(";")) {
				tabelasDadosParaTodosRepSup = ";" + tabelasDadosParaTodosRepSup;
			}
		}
		/*422*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAPRECOPRODUTO);
		/*422-1*/ liberaComSenhaPrecoProduto = isAtributoJsonLigado(vlParam, "usa", ValorParametro.LIBERACOMSENHAPRECOPRODUTO);
		/*422-2*/ usaDescMaxPrecoLiberadoConsomeVerbaGrupoProduto = liberaComSenhaPrecoProduto && isAtributoJsonLigado(vlParam, "usaDescMaxPrecoLiberadoConsomeVerbaGrupoProduto", ValorParametro.LIBERACOMSENHAPRECOPRODUTO);
		/*422-3*/ liberaNegociacaoPrecoApenasDesconto = liberaComSenhaPrecoProduto && isAtributoJsonLigado(vlParam, "liberaNegociacaoPrecoApenasDesconto", ValorParametro.LIBERACOMSENHAPRECOPRODUTO);
		/*423*/	sementeSenhaLiberaPrecoProduto = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHALIBERAPRECOPRODUTO));
		/*424*/	usaDescontoProgressivoPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPROGRESSIVOPORTABELAPRECO));
		/*425*/	bloqueiaDescontoProgressivoClientesEspeciais = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIADESCONTOPROGRESSIVOCLIENTESESPECIAIS));
		/*427*/	bloqueiaTipoPedidoPadraoClienteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIATIPOPEDIDOPADRAOCLIENTENOPEDIDO));
		/*428*/	configDescontoAcrescimoNoPedidoAplicadoPorItem = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM), ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
		/*429*/	usaPrecoBaseItemBonificado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRECOBASEITEMBONIFICADO);
		/*430*/	mostraPrecosPorCondicaoPagamento = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPRECOSPORCONDICAOPAGAMENTO);
		/*431*/ usaServicoBuscaEnderecoPorCep = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASERVICOBUSCAENDERECOPORCEP));
		/*432*/	grifaProdutoSemEstoqueNaLista = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRIFAPRODUTOSEMESTOQUENALISTA);
		/*433*/	emiteBeepAoInserirItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EMITEBEEPAOINSERIRITEMPEDIDO));
		/*434*/	permiteEditarDescontoProgressivo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEEDITARDESCONTOPROGRESSIVO));
		/*438*/	usaTipoPagamentoPorCondicaoPagamento =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOPAGAMENTOPORCONDICAOPAGAMENTO);
		/*441*/	usaTabelaPrecoPorCondicaoPagamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORCONDICAOPAGAMENTO));
		/*442*/	configSugestaoParaNovoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGSUGESTAOPARANOVOPEDIDO);
		/*444*/	usaDescontoComissaoPorProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOCOMISSAOPORPRODUTO));
		/*445*/	usaDescontoComissaoPorGrupo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOCOMISSAOPORGRUPO));
		/*450*/ relVendasProdutoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RELVENDASPRODUTOPORCLIENTE));
		/*452*/ aplicaIndiceFinanceiroEspClientePorItemFinalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAINDICEFINANCEIROESPCLIENTEPORITEMFINALPEDIDO));
		/*453*/ mostraPrevisaoDescontoGridProdutos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPREVISAODESCONTOGRIDPRODUTOS));
		/*455*/ tamanhoColunaCdProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.TAMANHOCOLUNACDPRODUTO);
		tamanhoColunaCdProduto =  tamanhoColunaCdProduto == null ? "111" : tamanhoColunaCdProduto;
		/*456*/ usaPedidoBonificacao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPEDIDOBONIFICACAO);
		/*457*/ bloqueiaCondPagtoPadraoClienteNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIACONDPAGTOPADRAOCLIENTENOPEDIDO);
		/*458*/ usaCondPagtoPorTipoPagtoPadraoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDPAGTOPORTIPOPAGTOPADRAOCLIENTE);
		usaCondPagtoPorTipoPagtoPadraoCliente =  ValueUtil.VALOR_NAO.equals(usaCondPagtoPorTipoPagtoPadraoCliente) ? "" : usaCondPagtoPorTipoPagtoPadraoCliente;
		/*459*/ usaOrdemNumericaColunaCodigoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDEMNUMERICACOLUNACODIGOPRODUTO));
		/*461*/ aplicaDeflatorCondPagtoValorTotalPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADEFLATORCONDPAGTOVALORTOTALPEDIDO);
		/*463*/ configCodigosProdutosParaClientes = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCODIGOSPRODUTOSPARACLIENTES);
		/*464*/ confirmaNovoPedidoClienteBloqueado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIRMANOVOPEDIDOCLIENTEBLOQUEADO));
		/*466*/ usaRotaDeEntregaNoPedidoComCadastro = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAROTADEENTREGANOPEDIDOCOMCADASTRO);
		/*467*/	nuMinDiasPrevisaoEntregaDoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUMINDIASPREVISAOENTREGADOPEDIDO));
		/*469*/ ocultaTelaGeracaoSenhaSupervisor = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTATELAGERACAOSENHASUPERVISOR));
		/*470*/ mostraDetalhesAdicionaisRelMetasPorProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRADETALHESADICIONAISRELMETASPORPRODUTO);
		/*472*/ bloqueiaItemTabelaPrecoParaVenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIAITEMTABELAPRECOPARAVENDA));
		/*476*/ geraDiferencasPedidoPdaIntegracaoPorView = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.GERADIFERENCASPEDIDOPDAINTEGRACAOPORVIEW));
		/*477*/ tabelaPrecoParaTroca = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.TABELAPRECOPARATROCA);
		/*478*/ indiceFinanceiroCondPagtoClientePorDias = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.INDICEFINANCEIROCONDPAGTOCLIENTEPORDIAS));
		/*479*/ anulaDescontoPessoaFisica = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ANULADESCONTOPESSOAFISICA));
		/*481*/ destacaProdutosJaIncluidosAoPedido = isDestacaProdutosJaIncluidos(hashValorParametros);
		/*482*/ filtraTabelaPrecoPelaRegiaoDoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRATABELAPRECOPELAREGIAODOCLIENTE));
		/*484*/ consisteMultiploEmbalagem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSISTEMULTIPLOEMBALAGEM));
		/*485*/ mostraSaldoContaCorrenteCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRASALDOCONTACORRENTECLIENTE));
		/*486*/ mostraRelatorioContaCorrenteCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARELATORIOCONTACORRENTECLIENTE));
		/*487*/ usaFatorCUMEspecialClienteCreditoAntecipado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFATORCUMESPECIALCLIENTECREDITOANTECIPADO));
		/*488*/ apenasAvisaDescontoAcrescimoMaximo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APENASAVISADESCONTOACRESCIMOMAXIMO));
		/*489*/ gravaCpfCnpjNovoClienteSemSeparadores = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRAVACPFCNPJNOVOCLIENTESEMSEPARADORES));
		/*494*/	atualizaDataHoraServidorNoAcessoAoSistema = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.ATUALIZADATAHORASERVIDORNOACESSOAOSISTEMA));
		/*495*/ filtraTabelaPrecoPelaListaDoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.FILTRATABELAPRECOPELALISTADOCLIENTE));
		/*498*/ mostraProdutoPromocionalDestacado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPRODUTOPROMOCIONALDESTACADO);
		/*499*/ qtdMinItensParaPermitirItensPromocionais = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDMINITENSPARAPERMITIRITENSPROMOCIONAIS));
		/*500*/ usaPrecoEspecialParaClienteEspecial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRECOESPECIALPARACLIENTEESPECIAL));
		/*501*/ destacaProdutoDeKitNaGrid = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACAPRODUTODEKITNAGRID));
		/*502*/ mostraValorDaUnidadePrecoPorEmbalagem = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAVALORDAUNIDADEPRECOPOREMBALAGEM);
		/*503*/ permiteAlterarValorDaUnidadePrecoPorEmbalagem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARVALORDAUNIDADEPRECOPOREMBALAGEM));
		/*506*/ aplicaReducaoPrecoItemClienteOptanteSimples = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAREDUCAOPRECOITEMCLIENTEOPTANTESIMPLES));
		/*508*/ usaLeadTimePadraoClienteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALEADTIMEPADRAOCLIENTENOPEDIDO));
		/*509*/ permiteAlterarTabelaPrecoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARTABELAPRECOPEDIDO));
		/*511*/ exibeTituloMaisAtrasadoNaFichaFinanceira = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBETITULOMAISATRASADONAFICHAFINANCEIRA));
		/*533*/ cdStatusPedidoCancelado = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CDSTATUSPEDIDOCANCELADO);
		/*535*/ usaRegistroProdutoFaltante = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAREGISTROPRODUTOFALTANTE));
		/*541*/ mostraEmbalagemCompraProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAEMBALAGEMCOMPRAPRODUTO));
		/*543*/ permiteTodasTabelasPedidoBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITETODASTABELASPEDIDOBONIFICACAO));
		/*544*/ usaNovoPedidoParaMesmoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USANOVOPEDIDOPARAMESMOCLIENTE));
		/*545*/ usaCCClientePorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACCCLIENTEPORTIPOPEDIDO));
		/*547*/ nuGrupoProdutoForaDaCCClientePorTipoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUGRUPOPRODUTOFORADACCCLIENTEPORTIPOPEDIDO);
		/*553*/ configFaceamento = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGFACEAMENTO);
		/*550*/ usaPesquisaProdutoPersonalizada = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPESQUISAPRODUTOPERSONALIZADA));
		/*558*/ verificaLimiteCreditoClienteEspecial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VERIFICALIMITECREDIDOCLIENTEESPECIAL));
		/*559*/ mostraTotalDeClienteNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRATOTALDECLIENTENALISTA));
		/*560*/ mostraRelNovidadeAposPrimeiroSync = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARELNOVIDADEAPOSPRIMEIROSYNC));
		/*561*/ usaNovidadesRecentesNoRelNovidades = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USANOVIDADESRECENTESNORELNOVIDADES));
		/*562*/ retornaTabPrecoParaPadrao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RETORNATABPRECOPARAPADRAO));
		/*563*/ informaVerbaManual = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.INFORMAVERBAMANUAL));
		/*564*/ usaTecladoFixoTelaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATECLADOFIXOTELAITEMPEDIDO));
		/*566*/ usaControleNoDescontoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLENODESCONTOPROMOCIONAL));
		/*567*/ usaAvisoPreAlta = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAVISOPREALTA));
		/*568*/ mostraFotoNaTelaItemPedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAFOTONATELAITEMPEDIDO));
		/*570*/ calculaPontuacaoDaRentabilidadeNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAPONTUACAODARENTABILIDADENOPEDIDO));
		/*571*/ ocultaPrecosNoMenuProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAPRECOSNOMENUPRODUTO);
		/*572*/	avisaUsuarioPositivacaoFornecedor = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISAUSUARIOPOSITIVACAOFORNECEDOR);
		/*574*/ ignoraIndiceFinanceiroCondPagtoProdutoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAINDICEFINANCEIROCONDPAGTOPRODUTOPROMOCIONAL));
		/*575*/ usaControleTimeZone = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLETIMEZONE);
		/*577*/ usaTelaAdicionarItemAoPedidoEstiloDesktop = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATELAADICIONARITEMAOPEDIDOESTILODESKTOP));
		/*582*/ nuMinCaracterObservacaoMotivoVisita = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUMINCARACTEROBSERVACAOMOTIVOVISITA));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPEDIDOBONIFICACAOUSANDOVERBACLIENTE);
		/*583*/ usaPedidoBonificacaoUsandoVerbaCliente = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGPEDIDOBONIFICACAOUSANDOVERBACLIENTE);
		/*583-1*/ permiteEscolhaSaldoVerbaAConsumir = isAtributoJsonLigado(vlParam, "permiteEscolhaSaldoVerbaAConsumir", ValorParametro.CONFIGPEDIDOBONIFICACAOUSANDOVERBACLIENTE);
		/*584*/	valorTimeOutEstoqueOnline = (ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.VALORTIMEOUTESTOQUEONLINE))) * 1000; //Deve multiplicar por mil porque � milisegundos
		if (valorTimeOutEstoqueOnline == 0) {
			valorTimeOutEstoqueOnline = 20000;
		}
		/*588*/ naoObrigaCompletarAgendaDiaAnterior = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOOBRIGACOMPLETARAGENDADIAANTERIOR));
		/*589*/ aplicaDescontoQuantidadeVlBase = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOQUANTIDADEVLBASE));
		/*590*/	dominioDataEntregaFinalSemana = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DOMINIODATAENTREGAFINALSEMANA);
		/*591*/ usaModuloConsignacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMODULOCONSIGNACAO));
		/*592*/ aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCPROGRESSIVOPORITEMFINALPEDIDOCONSUMINDOFLEX));
		/*606*/ usaModuloPagamento = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMODULOPAGAMENTO);
		/*607*/	aplicaDescontoPedidoRepEspecial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOPEDIDOREPESPECIAL));
		/*608*/	usaUnidadeAlternativa = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAUNIDADEALTERNATIVA));
		/*609*/	configDescontoQtdPorGrupo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCONTOQTDPORGRUPO);
		/*610*/	filtrosFixoTelaListaProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTROSFIXOTELALISTAPRODUTO);
		/*611*/	usaCondicaoPagamentoPorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOPAGAMENTOPORTIPOPEDIDO));
		/*612*/	usaSugestaoTabPrecoECondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOTABPRECOECONDPAGTO));
		/*613*/	filtraGrupoProdutoPorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRAGRUPOPRODUTOPORTIPOPEDIDO));
		/*614*/ configUsaMultiplasSugestoesProdutoIndustria = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGUSAMULTIPLASSUGESTOESPRODUTOINDUSTRIA);
		/*617*/ usaContratoForecast = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTRATOFORECAST));
		/*618*/ usaControleEstoqueGondola = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLEESTOQUEGONDOLA));
		/*619*/	aplicaDescProgressivoPorQtdPorItemFinalPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCPROGRESSIVOPORQTDPORITEMFINALPEDIDO));
		/*620*/	nuDiasAlertaClienteSemPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASALERTACLIENTESEMPEDIDO));
		/*621*/	sementeSenhaClienteSemPedido = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHACLIENTESEMPEDIDO));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOMAXIMOBASEADONOVLBASEITEMPEDIDOQUANDOUSAFLEX);
		/*622-1*/ usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = isAtributoJsonLigado(vlParam, "usa", ValorParametro.USADESCONTOMAXIMOBASEADONOVLBASEITEMPEDIDOQUANDOUSAFLEX);
		/*622-2*/ aplicaBaseadoTabelaPreco = isAtributoJsonLigado(vlParam, "aplicaBaseadoTabelaPreco", ValorParametro.USADESCONTOMAXIMOBASEADONOVLBASEITEMPEDIDOQUANDOUSAFLEX);
		/*623*/ permiteAlterarItemDePedidoBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARITEMDEPEDIDOBONIFICACAO));
		/*626*/ mostraValorParcelaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAVALORPARCELAPEDIDO));
		/*627*/	mostraDescontosPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRADESCONTOSPORTABELAPRECO));
		/*631*/	enviaInformacoesVisitaOnline = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ENVIAINFORMACOESVISITAONLINE));
		/*632*/	usaPainelGerenciamentoVendas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPAINELGERENCIAMENTOVENDAS));
		/*634*/ qtCaracteresColunaTabelaPreco = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.QTCARACTERESCOLUNATABELAPRECO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.QTCARACTERESCOLUNATABELAPRECO))) {
			qtCaracteresColunaTabelaPreco = 2;
		}
		/*635*/ apenasAvisaValorMinimoParaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APENASAVISAVALORMINIMOPARAPEDIDO));
		/*636*/	emiteAlertaUsuarioVerificacaoCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EMITEALERTAUSUARIOVERIFICACAOCONDPAGTO));
		/*637*/	qtdPedidoSugestaoParaNovoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDPEDIDOSUGESTAOPARANOVOPEDIDO));
		/*638*/	mostraUnidadesAlternativasAoSelecionarProduto = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAUNIDADESALTERNATIVASAOSELECIONARPRODUTO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAUNIDADESALTERNATIVASAOSELECIONARPRODUTO))) {
			mostraUnidadesAlternativasAoSelecionarProduto = 1;
		}
		/*640*/ apresentaQtdPreCarregadaDeVendaNoItemDoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAQTDPRECARREGADADEVENDANOITEMDOPEDIDO));
		/*641*/ quantidadeMinimaCaixasPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QUANTIDADEMINIMACAIXASPEDIDO));
		/*644*/ usaKitBaseadoNoProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAKITBASEADONOPRODUTO);
		/*645*/	mostraTodasTabelaPrecoPedidoNovoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRATODASTABELAPRECOPEDIDONOVOCLIENTE));
		/*646*/ validaDescontoCCPPorItem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDADESCONTOCCPPORITEM));
		/*649*/ usaAgendaVisitaBaseadaNaSemanaDoMes = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAGENDAVISITABASEADANASEMANADOMES);
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAGENDAVISITABASEADANASEMANADOMES))) {
			usaAgendaVisitaBaseadaNaSemanaDoMes = "1";
		}
		/*653*/ usaFiltroMarcaProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROMARCAPRODUTO));
		/*655*/ mostraValorTotalPedidoItensComEstoque = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAVALORTOTALPEDIDOITENSCOMESTOQUE));
		/*656*/ usaOrdemNumericaColunaCodigoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDEMNUMERICACOLUNACODIGOCLIENTE));
		/*657*/ usaApenasUmProdutoQuantidadeMaxVendaNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAQUANTIDADEMAXPRODUTOPORTABELAPRECO));
		/*661*/ usaRelSubstituicaoTributaria = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELSUBSTITUICAOTRIBUTARIA));
		/*662*/ usaOrdenacaoDescricaoListaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDENACAODESCRICAOLISTAITEMPEDIDO));
		/*663*/ destacaProdutoQtMinEstoqueLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACAPRODUTOQTMINESTOQUELISTA));
		/*664*/ mostraDetalhesAdicionaisRelMetasPorGrupoProduto = getValorParametroPorSistema(hashValorParametros, ValorParametro.MOSTRADETALHESADICIONAISRELMETASPORGRUPOPRODUTO1);
		/*666*/ usaBonificacaoPorGrupoBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USABONIFICACAOPORGRUPOBONIFICACAO));
		/*667*/ qtItemAVenderParaPermitirBonificarProduto = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTITEMAVENDERPARAPERMITIRBONIFICARPRODUTO));
		/*668*/ qtPermitidaProdutoABonificarAposVenda = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTPERMITIDAPRODUTOABONIFICARAPOSVENDA));
		/*669*/	nuDiasBloqueiaClienteSemPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASBLOQUEIACLIENTESEMPEDIDO));
		/*672*/	usaOcorrenciaSACComWorkflow = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAOCORRENCIASACCOMWORKFLOW));
		/*674*/	usaDescontoPorQuantidadeValor = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPORQUANTIDADEVALOR));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACANCELAMENTODEPEDIDO);
		int param675 = getVlParamAsInt(getValorAtributoJson(vlParam, "usa", ValorParametro.USACANCELAMENTODEPEDIDO));
		/*675-1*/usaCancelamentoDePedido = param675 == 1 || param675 == 2;
		/*675-2*/usaCancPedAbertoAuto = isAtributoJsonLigado(vlParam, "usaCancPedAbertoAuto", ValorParametro.USACANCELAMENTODEPEDIDO);
		/*675-3*/usaCancPedFechadoAuto = isAtributoJsonLigado(vlParam, "usaCancPedFechadoAuto", ValorParametro.USACANCELAMENTODEPEDIDO);
		/*675-4*/nuDiaSemCanPedAbertoFechadoAuto = getVlParamAsInt(getValorAtributoJson(vlParam, "nuDiaSemCanPedAbertoFechadoAuto", ValorParametro.USACANCELAMENTODEPEDIDO));
		/*676*/	usaAreaVendaAutoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAREAVENDAAUTONOPEDIDO));
		/*678*/	usaRelMetasGrupoProdIndepDoCadDeGrupoProd = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELMETASGRUPOPRODINDEPDOCADDEGRUPOPROD));
		/*679*/	mostraVlComTributosNasListasDePedidoEItens = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMOSTRAVLTRIBUTOSNASLISTASDEPEDIDOEITENS);
		/*680*/	usaScroolLateralListasProdutos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASCROOLLATERALLISTASPRODUTOS));
		/*681*/	ocultaContatoSAC = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.OCULTACONTATOSAC));
		/*682*/	naoPermiteExcluirAlterarSAC = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NAOPERMITEEXCLUIRALTERARSAC));
		/*685*/	nuDiasSolicitaAtualizacaoCliente = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASSOLICITAATUALIZACAOCLIENTE));
		/*686*/	usaFiltroProdutoPromocional = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROPRODUTOPROMOCIONAL);
		/*687*/ mostraDescricaoReferencia = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRADESCRICAOREFERENCIA));
		/*688*/	liberaComSenhaClienteRedeAtrasadoNovoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHACLIENTEREDEATRASADONOVOPEDIDO));
		/*689*/	sementeSenhaClienteRedeAtrasadoNovoPedido = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHACLIENTEREDEATRASADONOVOPEDIDO));
		/*690*/	configObservacaoPedido = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGOBSERVACAOPEDIDO);
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASIMILARVENDAPRODUTO);
		/*693*/	usaSimilarVendaProduto = isAtributoJsonLigado(vlParam, "usa", ValorParametro.USASIMILARVENDAPRODUTO);
		/*693-1*/ usaAgrupadorProdutoSimilar = isAtributoJsonLigado(vlParam, "usaAgrupadorProduto", ValorParametro.USASIMILARVENDAPRODUTO);
		/*696*/	indiceRentabilidadePedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.INDICERENTABILIDADEPEDIDO));
		/*697*/	indiceMinimoRentabilidadePedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.INDICEMINIMORENTABILIDADEPEDIDO));
		/*698*/ usaSugestaoParaNovoPedidoGiroProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOPARANOVOPEDIDOGIROPRODUTO));
		/*699*/ percAlturaTelaDescontoTabelaPreco = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ALTURATELADESCONTOTABELAPRECO));
		/*700*/ qtMinimaItensParaPermitirItemRestricaoQuantidade = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMINIMAITENSNORMAISPEDIDO));
		/*701*/ sementeSenhaLiberacaoQtMaxVendaPorProduto = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHALIBERACAOQTMINIMAITENSNORMAISPEDIDO));
		/*702*/	filtraProdutoPorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRAPRODUTOPORTIPOPEDIDO));
		/*703*/	naoDescontaVerbaEmPedidoBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAODESCONTAVERBAEMPEDIDOBONIFICACAO));
		/*704*/	usaTamanhoDinamicoTecladoFixoTelaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATAMANHODINAMICOTECLADOFIXOTELAITEMPEDIDO));
		/*705*/	usaTipoPagtoPorCondPagtoECondPagtoPorCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOPAGTOPORCONDPAGTOECONDPAGTOPORCLIENTE);
		/*706*/	permiteIncluirMesmoProdutoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEINCLUIRMESMOPRODUTONOPEDIDO));
		/*707*/	usaSequenciaInsercaoNuSeqProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASEQUENCIAINSERCAONUSEQPRODUTO));
		/*708*/	usaRelUltimosPedidosDoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELULTIMOSPEDIDOSDOCLIENTE));
		/*709*/	nuDiasPedidoRecenteRelUltimosPedidos = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPEDIDORECENTERELULTIMOSPEDIDOS));
		/*710*/configGradeProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGGRADEPRODUTO);
		/*712*/ cdStatusPedidoPendenteAprovacao = getValorParametroPorSistema(hashValorParametros, ValorParametro.CDSTATUSPEDIDOPENDENTEAPROVACAO);
		/*713*/	usaEnvioPedidoBackground = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAENVIOPEDIDOBACKROUND));
		/*715*/	mostraColunaQtdEmbalagensNaListaDePedidos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRACOLUNAQTDEMBALAGENSNALISTADEPEDIDOS);
		if (ValueUtil.VALOR_SIM.equals(mostraColunaQtdEmbalagensNaListaDePedidos)) {
			mostraColunaQtdEmbalagensNaListaDePedidos = "CX";
		}
		/*714*/ usaCondicaoPagamentoPedidoBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOPAGAMENTOPEDIDOBONIFICACAO));
		/*717*/ usaSistemaModoSuporte = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USASISTEMAMODOSUPORTE));
		/*718*/ formatoMascaraFone = getValorParametroPorSistema(hashValorParametros, ValorParametro.FORMATOMASCARAFONE);
		if (!ValueUtil.VALOR_NAO.equals(formatoMascaraFone) && ValueUtil.isNotEmpty(formatoMascaraFone)) {
			EditFoneMask.mask = formatoMascaraFone.replaceAll("9", "#");
		}
		/*719*/ usaPrecoPorUnidadeQuantidadePrazo = getValorParametroPorSistema(hashValorParametros, ValorParametro.USAPRECOPORUNIDADEQUANTIDADEPRAZO);
		/*721*/ permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.PERMITEINCLUIRMESMOPRODUTOUNIDADEDIFERENTENOPEDIDO));
		/*723*/	aplicaDescontoPromocionalAutomaticoItemTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOPROMOCIONALAUTOMATICOITEMTABPRECO));
		/*724*/	aplicaMaiorDescontoNoItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAMAIORDESCONTONOITEMPEDIDO);
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAMAIORDESCONTONOITEMPEDIDO))) {
			aplicaMaiorDescontoNoItemPedido = "1";
		}
		/*725*/ configCarregaUltimoPrecoItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCARREGAULTIMOPRECOITEMPEDIDO);
		/*726*/ mostraRelatorioGrupoProdutoNaoInseridosPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARELATORIOGRUPOPRODUTONAOINSERIDOSPEDIDO));
		/*727*/ usaLoginAutomaticoSistema = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USALOGINAUTOMATICOSISTEMA));
		/*728*/ usaSistemaModoOffLine = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USASISTEMAMODOOFFLINE));
		/*729*/ mostraRelProdutosNaoInseridosPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARELPRODUTOSNAOINSERIDOSPEDIDO);
		/*730*/ mostraProdutoDescPromocionalDestacado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPRODUTODESCPROMOCIONALDESTACADO);
		/*731*/ usaFiltroProdutoDescPromocional = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROPRODUTODESCPROMOCIONAL);
		/*733*/ mostraPrecoProdutoTelaAutomaticaSelecaoUnidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPRECOPRODUTOTELAAUTOMATICASELECAOUNIDADE));
		/*734*/ mostraRelIndicadoresDeProdutividadeAposSync = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARELINDICADORESDEPRODUTIVIDADEAPOSSYNC));
		/*735*/ geraVerbaPositiva = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GERAVERBAPOSITIVA));
		/*736*/ permiteAlterarVlItemNaUnidadeElementar = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARVLITEMNAUNIDADEELEMENTAR));
		/*738*/ nuIntervaloColetaGpsRepresentante = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUINTERVALOCOLETAGPSREPRESENTANTE));
		/*739*/ usaColetaGpsPontosEspecificosSistema = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACOLETAGPSPONTOSESPECIFICOSSISTEMA));
		/*740*/ nuIntervaloErroColetaGpsPontosEspecificosSistema = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUINTERVALOERROCOLETAGPSPONTOSESPECIFICOSSISTEMA));
		/*741*/ nuMaxTempoErroColetaGpsPontosEspecificosSistema = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUMAXTEMPOERROCOLETAGPSPONTOSESPECIFICOSSISTEMA));
		/*745*/ nuDiasPermanenciaMetas = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAMETAS));
		/*746*/ usaSegmentoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASEGMENTONOPEDIDO));
		/*747*/ usaSegmentoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASEGMENTOPORCLIENTE));
		/*748*/ usaTabelaPrecoPorSegmento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORSEGMENTO));
		/*749*/ usaCondicaoPagamentoPorSegmento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOPAGAMENTOPORSEGMENTO));
		/*750*/ ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAINDICEFINANCEIROUNIDADEALTERNATIVAPRODPROMOCAO);
		/*751*/ mostraQtVendasProdutoNoPeriodo = getValorParametroPorSistema(hashValorParametros, ValorParametro.MOSTRAQTVENDASPRODUTONOPERIODO);
		/*752*/ usaCondicaoComercialPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOCOMERCIALPEDIDO));
		/*753*/ usaCondicaoPagamentoPorCondicaoComercial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOPAGAMENTOPORCONDICAOCOMERCIAL));
		/*754*/ usaCondicaoComercialPorSegmentoECliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOCOMERCIALPORSEGMENTOECLIENTE));
		/*755*/ usaRelInfoEntregaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELINFOENTREGAPEDIDO));
		/*756*/ nuDiasFiltroClienteSemPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASFILTROCLIENTESEMPEDIDO));
		/*758*/ configTelaInfoProduto = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGTELAINFOPRODUTO);
		/*762*/ usaSugestaoVendaComCadastro = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOVENDACOMCADASTRO);
		/*763*/ usaIndicacaoMotivoDescPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICACAOMOTIVODESCPEDIDO);
		/*766*/ usaDiasUteisContabilizarDiasAtrazoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADIASUTEISCONTABILIZARDIASATRAZOCLIENTE);
		/*767*/ usaFretePedidoPorTranspTipoPedProd = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFRETEPEDIDOPORTRANSPTIPOPEDPROD));
		/*768*/ usaSugestaoVendaBaseadaDifPedidos = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOVENDABASEADADIFPEDIDOS));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOVENDABASEADADIFPEDIDOS))) {
			usaSugestaoVendaBaseadaDifPedidos = 1;
		}
		/*769*/ usaRateioFreteRepresentanteCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARATEIOFRETEREPRESENTANTECLIENTE));
		/*770*/ usaRestricaoVendaProdutoPorCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARESTRICAOVENDAPRODUTOPORCLIENTE);
		/*771*/ usaBloqueioAlteracaoPrecoPedidoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USABLOQUEIOALTERACAOPRECOPEDIDOPORCLIENTE));
		/*772*/ nuMetrosToleranciaRepresentantePresenteCliente = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(ValorParametro.NUMETROSTOLERANCIAREPRESENTANTEPRESENTECLIENTE));
		/*774*/ usaMotivoAtrasoClienteAtrasado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMOTIVOATRASOCLIENTEATRASADO);
		/*775*/ mantemPercAcrescDescProdutoAoTrocarTabPrecoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MANTEMPERCACRESCDESCPRODUTOAOTROCARTABPRECOPEDIDO));
		/*776*/ usaRestricaoVendaProdutoPorUnidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARESTRICAOVENDAPRODUTOPORUNIDADE));
		/*779*/ permiteAlterarCondicaoComercialPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARCONDICAOCOMERCIALPEDIDO));
		/*780*/ usaItensBonificadoCalculoRealizado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAITENSBONIFICADOCALCULOREALIZADO));
		/*781*/ usaPesoGerarRealizado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPESOGERARREALIZADO));
		/*783*/ qtMinCaracteresMotivoAtrasoClienteAtrasado = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMINCARACTERESMOTIVOATRASOCLIENTEATRASADO));
		if (qtMinCaracteresMotivoAtrasoClienteAtrasado == 0) {
			qtMinCaracteresMotivoAtrasoClienteAtrasado = 1;
		}
		/*784*/ relMetasDeVendaUnificado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RELMETASDEVENDAUNIFICADO));
		/*785*/ exibeRelMetasDeVendaAposPrimeiroSync = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBERELMETASDEVENDAAPOSPRIMEIROSYNC));
		/*786*/ usaDsReferenciaNmFoto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADSREFERENCIANMFOTO));
		/*787*/ exibeRelClientesNaoAtendidosAposPrimeiroSync = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBERELCLIENTESNAOATENDIDOSAPOSPRIMEIROSYNC));
		/*788*/ liberaComSenhaVendaProdutoBloqueado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAVENDAPRODUTOBLOQUEADO));
		/*789*/	sementeSenhaLiberaComSenhaVendaProdutoBloqueado = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.SEMENTESENHALIBERACOMSENHAVENDAPRODUTOBLOQUEADO));
		/*790*/ usaBloqueioVendaProdutoBaseadoRealizadoMetaGrupoProd = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USABLOQUEIOVENDAPRODUTOBASEADOREALIZADOMETAGRUPOPROD));
		/*791*/ validaPctMaxDescPorRepresentante = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDAPCTMAXDESCPORREPRESENTANTE));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDAPCTMAXDESCPORREPRESENTANTE))) {
			validaPctMaxDescPorRepresentante = 1;
		}
		/*792*/ usaNuConversaoUnidadePorItemTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USANUCONVERSAOUNIDADEPORITEMTABELAPRECO));
		/*793*/ realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.REALIZACALCULOINDICESPRECOPRODUTOLISTAADICONARITENSPEDIDO));
		/*794*/ ocultaInfosValoresPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTATOTALIZADORESLISTASPEDIDOITENS));
		/*795*/ usaGerenciamentoRentabilidade = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGERENCIAMENTORENTABILIDADE);
		if (ValueUtil.VALOR_SIM.equals(usaGerenciamentoRentabilidade)) {
			usaGerenciamentoRentabilidade = "1";
		}
		/*796*/ usaTipoPedidoComoOpcaoMenuCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOPEDIDOCOMOOPCAOMENUCLIENTE));
		/*797*/ usaNuSequenciaNaChaveDaAgendaVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USANUSEQUENCIANACHAVEDAAGENDAVISITA));
		/*798-1*/ usaMelhorRotaSistema = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMELHORROTASISTEMA), "usa", ValorParametro.CONFIGMELHORROTASISTEMA);
		/*798-2*/ automatizaCalculoRota = usaMelhorRotaSistema && isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMELHORROTASISTEMA), "automatizaCalculoRota", ValorParametro.CONFIGMELHORROTASISTEMA);
		/*799*/ acessaDiretamenteTelaAdicionarItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ACESSADIRETAMENTETELAADICIONARITEMPEDIDO));
		/*800*/ bloqueiaFechamentoPedidoProdutoSemEstoque = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIAFECHAMENTOPEDIDOPRODUTOSEMESTOQUE));
		/*801*/ usaPositivacaoVisitaPorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPOSITIVACAOVISITAPORTIPOPEDIDO));
		/*802*/ carregaProdutosAutoTelaItemPedidoEstiloDesktop = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CARREGAPRODUTOSAUTOTELAITEMPEDIDOESTILODESKTOP));
		/*804*/ configLocalEstoque = getParametroAsJson(getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGLOCALESTOQUE), ValorParametro.CONFIGLOCALESTOQUE);
		/*805*/ bloqueiaSistemaGpsInativo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIASISTEMAGPSINATIVO));
		/*806*/	sementeSenhaBloqueiaSistemaGpsInativo = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHABLOQUEIASISTEMAGPSINATIVO));
		/*807*/ usaDescricaoCodigoNaVisualizacaoEntidades = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USADESCRICAOCODIGONAVISUALIZACAOENTIDADES));
		/*810*/ ocultaAcessoRelUltimosPedidos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAACESSORELULTIMOSPEDIDOS);
		/*811*/ ocultaAcessoInfoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAACESSOINFOCLIENTE));
		/*812*/ usaSugestaoVendaProdutosPorFoto = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOVENDAPRODUTOSPORFOTO));
		/*813*/ qtdPedidosPermitidosManterAbertos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOPERMITEMANTERPEDIDOSABERTOS);
		/*814*/ usaVerbaUnificada = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBAUNIFICADA));
		/*817*/ naoValidaSugestaoVendaAoFecharPedidoEmLote = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOVALIDASUGESTAOVENDAAOFECHARPEDIDOEMLOTE));
		/*818*/ naoValidaSugestaoVendaDifPedidosAoFecharPedidoEmLote = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOVALIDASUGESTAOVENDADIFPEDIDOSAOFECHARPEDIDOEMLOTE));
		/*819*/ ignoraIndiceFinanceiroCondComercialProdutoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAINDICEFINANCEIROCONDCOMERCIALPRODUTOPROMOCIONAL));
		/*820*/ naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOVALIDAPRODUTOSNAOINSERIDOSHISTORICOFECHARPEDIDO);
		/*821*/ usaRegistroPagamentoParaClienteAtrasado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREGISTROPAGAMENTOPARACLIENTEATRASADO));
		/*822*/ usaSugestaoFechamentoAoSairPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOFECHAMENTOAOSAIRPEDIDO));
		/*823*/ usaEnvioPedidoServidorSemConfirmacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAENVIOPEDIDOSERVIDORSEMCONFIRMACAO));
		/*824*/ usaEnvioRecadoServidorSemConfirmacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.REALIZAENVIORECADOAUTOMATICO));
		/*827*/ pctMargemAgregada = ValueUtil.getDoubleValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.PCTMARGEMAGREGADA));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORBASEDOITEMPARAVERBAPOSITIVA);
		/*828*/ usaValorBaseDoItemParaVerbaPositiva = isAtributoJsonLigado(vlParam, "usaValorBaseDoItemParaVerbaPositiva", ValorParametro.USAVALORBASEDOITEMPARAVERBAPOSITIVA);
		usaValorBaseItemTabelaPrecoParaVerbaPositiva = isAtributoJsonLigado(vlParam, "usaValorBaseItemTabelaPrecoParaVerbaPositiva", ValorParametro.USAVALORBASEDOITEMPARAVERBAPOSITIVA);
		usaValorBaseDoItemParaVerbaNegativa = isAtributoJsonLigado(vlParam, "usaValorBaseDoItemParaVerbaNegativa", ValorParametro.USAVALORBASEDOITEMPARAVERBAPOSITIVA);
		/*830*/ usaApenasUnidadesComPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPENASUNIDADESCOMPRECO));
		/*833*/ usaNomeSistemaPersonalizado = getValorParametroPorSistema(hashValorParametros, ValorParametro.USANOMESISTEMAPERSONALIZADO);
		/*835*/ naoAvisaClienteAtrasadoFecharPedidoEmLote = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOAVISACLIENTEATRASADOFECHARPEDIDOEMLOTE));
		/*837*/ usaDescontoQtdeGrupoPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOQTDEGRUPOPORTABELAPRECO));
		/*838*/ ocultaPopupDescQtdeGrupoItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAPOPUPDESCQTDEGRUPOITEMPEDIDO));
		/*841*/ mostraPercDescMaxPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAPERCDESCMAXPEDIDO));
		/*842*/ filtraProdutosPorOrigemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.FILTRAPRODUTOSPORORIGEMPEDIDO));
		/*843*/ usaImpressaoPedidoViaBluetooth = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOPEDIDOVIABLUETOOTH));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOPEDIDOVIABLUETOOTH))) {
			usaImpressaoPedidoViaBluetooth = 1;
		}
		/*844*/ mantemTabPrecoSelecionadaListaProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MANTEMTABPRECOSELECIONADALISTAPRODUTO));
		/*845*/ usaEstoqueDisponivelItemSugestao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAESTOQUEDISPONIVELITEMSUGESTAO);
		/*846*/ configGrupoDestaqueProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACAPRODUTOGRUPODESTAQUELISTA);
		/*847*/ usaOportunidadeVenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAOPORTUNIDADEVENDA));
		/*848*/ valorMinimoParaPedidoPorTipoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDOPORTIPOPEDIDO);
		/*849*/ liberaSenhaSugestaoVendaObrigatoria = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERASENHASUGESTAOVENDAOBRIGATORIA));
		/*850*/ sementeSenhaSugestaoVendaObrigatoria = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHASUGESTAOVENDAOBRIGATORIA));
		/*851*/ usaTabelaPrecoPorClienteOuCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORCLIENTEOUCONDPAGTO));
		/*853*/ validaSugestaoVendaMultiplasEmpresas = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDASUGESTAOVENDAMULTIPLASEMPRESAS));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDASUGESTAOVENDAMULTIPLASEMPRESAS))) {
			validaSugestaoVendaMultiplasEmpresas = 1;
		}
		if (validaSugestaoVendaMultiplasEmpresas > 2) {
			validaSugestaoVendaMultiplasEmpresas = 0;
		}
		/*855*/ validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDADESCMAXMESMOCOMDESCQUANTIDADEEDESCONTOGRUPO);
		if (ValueUtil.VALOR_SIM.equals(validaDescMaxMesmoComDescQuantidadeEDescontoGrupo)) {
			validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "1";
		}
		/*856*/ bloqueiaDescontoMaiorDescontoProgressivo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIADESCONTOMAIORDESCONTOPROGRESSIVO);
		if (ValueUtil.VALOR_SIM.equals(bloqueiaDescontoMaiorDescontoProgressivo)) {
			bloqueiaDescontoMaiorDescontoProgressivo = "1";
		}
		/*857*/ usaControleSaldoOportunidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLESALDOOPORTUNIDADE));
		/*858*/ usaGrupoDestaqueItemTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAGRUPODESTAQUEITEMTABPRECO));
		/*860*/ aplicaDescQtdPorGrupoProdFecharPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCQTDEPORGRUPOPRODUTOAOFECHARPEDIDO));
		/*861*/ filtraProdutoClienteRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRAPRODUTOCLIENTEREPRESENTANTE));
		/*862*/ usaValorExcecaoNaCondicaoComercial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALOREXCECAONACONDICAOCOMERCIAL));
		/*863*/ usaEscolhaEmpresaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAESCOLHAEMPRESAPEDIDO));
		/*866*/ destacaClienteAtendidoMes = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.DESTACACLIENTEATENDIDOMES));
		/*867*/ usaVendaRelacionada = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVENDARELACIONADA);
		/*868*/ usaSenhaVendaRelacionada = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASENHAVENDARELACIONADA));
		/*869*/	sementeSenhaVendaRelacionada = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAVENDARELACIONADA));
		/*870*/ ocultaCamposCalculadosFichaFinanceira = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTACAMPOSCALCULADOSFICHAFINANCEIRA);
		/*877*/ nuDiasPermanenciaResumoDia = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIARESUMODIA));
		if (nuDiasPermanenciaResumoDia == 0) {
			nuDiasPermanenciaResumoDia = 1;
		}
		/*879*/ relacionaPedidoNaTrocaBonificacao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RELACIONAPEDIDONABONIFICACAO);
		/*881*/ usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVARIACAOPRECOPRODUTOPORCATEGORIAEREGIAOCLIENTE));
		/*882*/ qtCopiasImpressaoPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTCOPIASIMPRESSAOPEDIDO));
		if (qtCopiasImpressaoPedido == 0) {
			qtCopiasImpressaoPedido = 1;
		}
		/*883*/ usaBackupAutomatico = getIntervaloBackup(hashValorParametros);
		/*885*/ controlaVencimentoAlvaraCliente = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLAVENCIMENTOALVARACLIENTE));
		/*886*/ configVerbaSaldoPorGrupoProduto = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGVERBASALDOPORGRUPOPRODUTO), ValorParametro.CONFIGVERBASALDOPORGRUPOPRODUTO);
		/*887*/ obrigaRespostaRecado = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.OBRIGARESPOSTARECADO));
		/*888*/ permiteEditarConexaoPda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEEDITARCONEXAOPDA));
		/*889*/ calculaIpiItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAIPIITEMPEDIDO);
		/*890*/ tipoDescricaoQtdListaItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.TIPODESCQTDLISTAITEMPEDIDO);
		/*891*/ nuIntervaloEnvioPontoGpsBackground = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUINTERVALOENVIOPONTOGPSBACKGROUND));
		/*892*/ controlaVencimentoAfeCliente = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLAVENCIMENTOAFECLIENTE));
		/*893*/ usaItensLiberadosSenhaCalculoDescProgressivoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAITENSLIBERADOSSENHACALCULODESCPROGRESSIVOPEDIDO));
		/*894*/ usaPedidosTodasEmpresasSaldoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPEDIDOSTODASEMPRESASSALDOCLIENTE));
		/*895*/ usaGrupoCondPagtoCli = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGRUPOCONDPAGTOCLI));
		/*896*/	nuCasasDecimaisVlStVlIpi = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUCASASDECIMAISVLSTVLIPI));
		if (nuCasasDecimaisVlStVlIpi == 0) {
			nuCasasDecimaisVlStVlIpi = 4;
		}
		/*899*/	configInfoComplementarPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO);
		/*900*/ obrigaInfoAdicionalFreteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGAINFOADICIONALFRETENOPEDIDO));
		/*901*/ usaPctFreteTipoFreteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPCTFRETETIPOFRETENOPEDIDO));
		/*902*/ usaConfirmacaoVerbaPedidoSugestao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONFIRMACAOVERBAPEDIDOSUGESTAO));
		/*903*/ usaVisitaFoto =  ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAVISITAFOTO));
		/*904*/ usaSenhaSqlExecutor = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USASENHASQLEXECUTOR));
		/*905*/	sementeSenhaSqlExecutor = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHASQLEXECUTOR));
		/*907*/ aplicaValoresProdPrincipalProdRelacionado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAVALORESPRODPRINCIPALPRODRELACIONADO));
		/*908*/	aplicaDescontoNoProdutoPorGrupoDescPromocional = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTONOPRODUTOPORGRUPODESCPROMOCIONAL), ValorParametro.APLICADESCONTONOPRODUTOPORGRUPODESCPROMOCIONAL);
		/*909*/ validaVendaRelacionadaUnidadeFaturamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDAVENDARELACIONAUNIDADEFATURAMENTO));
		/*912*/ exibeRelatorioComissaoAoSelecionarItem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBERELATORIOCOMISSAOAOSELECIONARITEM));
		/*913*/ usaCondComercialPorCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDCOMERCIALPORCONDPAGTO));
		/*914*/ usaRegistroLogin = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAREGISTROLOGIN));
		/*915*/ Session.saveLogErrorList = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADETALHAMENTOERRONOSYNC));
		/*916*/ usaCadastroCoordenadasGeograficasCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACADASTROCOORDENADASGEOGRAFICASCLIENTE);
		if (ValueUtil.VALOR_SIM.equals(usaCadastroCoordenadasGeograficasCliente)) {
			usaCadastroCoordenadasGeograficasCliente = "1";
		}
		/*917*/ obrigaCadCoordenadasGeograficas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGACADCOORDENADASGEOGRAFICAS));
		/*918*/	sementeSenhaCancelamentoCadCoordenadas = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHACANCELAMENTOCADCOORDENADAS));
		/*919*/ filtraClientePorProdutoRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRACLIENTEPORPRODUTOREPRESENTANTE));
		/*921*/	avisaPedidoAbertoFechado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISAPEDIDOABERTOFECHADO);
		if (ValueUtil.VALOR_SIM.equals(avisaPedidoAbertoFechado)) {
			avisaPedidoAbertoFechado = "1";
		}
		/*922*/ calculaFecopItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAFECOPITEMPEDIDO));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGINFOFINANCEIRODAREDEPARACLIENTES);
		/*923*/ usaConfigInfoFinanceiroDaRedeParaClientes = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGINFOFINANCEIRODAREDEPARACLIENTES);
				exibeSaldoNegativo = isAtributoJsonLigado(vlParam, "exibeSaldoNegativo", ValorParametro.CONFIGINFOFINANCEIRODAREDEPARACLIENTES);
		/*924*/ detalhaInfoTributariaPedidoEItemPedido = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DETALHAINFOTRIBUTARIAPEDIDOEITEMPEDIDO), "usa", ValorParametro.DETALHAINFOTRIBUTARIAPEDIDOEITEMPEDIDO);
		        mostraTributacaoItemPorItemTabelaPreco  = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DETALHAINFOTRIBUTARIAPEDIDOEITEMPEDIDO), "mostraTributacaoItemPorItemTabelaPreco", ValorParametro.DETALHAINFOTRIBUTARIAPEDIDOEITEMPEDIDO);
		        mostraVlBrutoCapaPedido = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DETALHAINFOTRIBUTARIAPEDIDOEITEMPEDIDO), "mostraVlBrutoCapaPedido", ValorParametro.DETALHAINFOTRIBUTARIAPEDIDOEITEMPEDIDO);
		/*925*/ calculaImpostosAdicionaisItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAIMPOSTOSADICIONAISITEMPEDIDO));
		/*926*/ usaCameraParaLeituraCdBarras = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACAMERAPARALEITURACDBARRAS));
		/*927*/	configHoraLimiteDeEnvioParaDataDeEntregaDoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO);
		/*930*/	aplicaDescontoCCPAposInserirItem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOCCPAPOSINSERIRITEM));
		/*931*/	usaPctFretePorTipoPedidoTabPrecoEPeso = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPCTFRETEPORTIPOPEDIDOTABPRECOEPESO);
		/*932*/ usaCondicaoComercialPorCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAOCOMERCIALPORCLIENTE);
		/*933*/ usaEnvioBackgroundColetaGpsPontoEspecifico = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAENVIOBACKGROUNDCOLETAGPSPONTOESPECIFICO));
		/*934*/ usaAuditoriaColetaGps = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAUDITORIACOLETAGPS));
		/*936*/ usaDescontoAcrescimoMaximoEmValor = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOACRESCIMOMAXIMOEMVALOR);
		if (ValueUtil.VALOR_SIM.equals(usaDescontoAcrescimoMaximoEmValor)) {
			usaDescontoAcrescimoMaximoEmValor = "1";
		}
		/*937*/ usaControlePesoPedidoPorCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLEPESOPEDIDOPORCONDPAGTO));
		/*938*/ usaControlePesoPedidoPorEmpresa = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLEPESOPEDIDOPOREMPRESA));
		/*939*/ usaBotaoNovoPedidoNaListaPedidos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USABOTAONOVOPEDIDONALISTAPEDIDOS));
		/*940*/	bloqueiaSistemaEquipamentoInativo = ValueUtil.getBooleanValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.BLOQUEIASISTEMAEQUIPAMENTOINATIVO));
		/*941*/	setValorTimeoutOpenConexaoPda(ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.VALORTIMEOUTOPENCONEXAOPDA)));
		/*943*/ destacaProdutoComPrecoEmQueda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACAPRODUTOCOMPRECOEMQUEDA));
		/*944*/ usaDescontoMaximoItemPorCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOMAXIMOITEMPORCLIENTE);
		if (ValueUtil.VALOR_SIM.equals(usaDescontoMaximoItemPorCliente)) {
			usaDescontoMaximoItemPorCliente = "1";
		}
		/*945*/ configDescontosEmCascata = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCONTOSEMCASCATA), ValorParametro.CONFIGDESCONTOSEMCASCATA);
		/*946*/ usaWorkflowStatusPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAWORKFLOWSTATUSPEDIDO));
		/*947*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGFOTOCLIENTE);
		/*947-1*/ permiteFotoMenuCliente = isAtributoJsonLigado(vlParam, "permiteFotoMenuCliente", ValorParametro.CONFIGFOTOCLIENTE);
		/*947-2*/ permiteFotoClientePedido = isAtributoJsonLigado(vlParam, "permiteFotoClientePedido", ValorParametro.CONFIGFOTOCLIENTE);
		/*948*/	vlTimeOutTentativaColetaGps = (ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.VLTIMEOUTTENTATIVACOLETAGPS))) * 1000; //Deve multiplicar por mil porque � milisegundos
		if (vlTimeOutTentativaColetaGps == 0) {
			vlTimeOutTentativaColetaGps = 10000;
		}
		/*949*/ usaArredondamentoIndividualCalculoTributacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAARREDONDAMENTOINDIVIDUALCALCULOTRIBUTACAO));
		/*950*/ calculaPesoGrupoProdutoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAPESOGRUPOPRODUTONOPEDIDO));
		/*951*/ usaDescontoPedidoPorCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPEDIDOPORCLIENTE);
		/*952*/ descontaComissaoRentabilidadePorItem = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESCONTACOMISSAORENTABILIDADEPORITEM);
		/*954*/ usaRentabilidadeMinimaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARENTABILIDADEMINIMAITEMPEDIDO));
		/*956*/ usaIndiceEspecialCondPagtoProd = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICEESPECIALCONDPAGTOPROD));
		/*957*/ usaReplicacaoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREPLICACAOPEDIDO);
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEREGISTRAREXCLUIRFOTOCLIENTE);
		/*958*/ permiteRegistrarExcluirFotoCliente = getValorAtributoJson(vlParam, "usa", ValorParametro.PERMITEREGISTRAREXCLUIRFOTOCLIENTE);
		ignoraNovaFotoEmissaoPedido = isAtributoJsonLigado(vlParam, "ignoraNovaFotoEmissaoPedido", ValorParametro.PERMITEREGISTRAREXCLUIRFOTOCLIENTE);
		/*959*/ nuDiasPermanenciaFotoCliente = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAFOTOCLIENTE));
		if (nuDiasPermanenciaFotoCliente == 0) {
			nuDiasPermanenciaFotoCliente = 180;
		}
		/*962*/ nuDiasPermanenciaCargaPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIACARGAPEDIDO));
		/*963*/ nuDiasRestantesAvisoVencimentoCarga = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASRESTANTESAVISOVENCIMENTOCARGA));
		/*964*/ sementeSenhaFecharCargaVencida = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHAFECHARCARGAVENCIDA));
		/*965*/ nuDiasValidadeCargaPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASVALIDADECARGAPEDIDO));
		/*966*/ qtdPesoMaximoCargaPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDPESOMAXIMOCARGAPEDIDO));
		/*967*/ usaCargaPedidoPorRotaEntregaDoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACARGAPEDIDOPORROTAENTREGADOCLIENTE);
		/*968*/ qtdPesoMinimoCargaPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDPESOMINIMOCARGAPEDIDO));
		/*969*/ sementeSenhaFecharCargaPesoMenorMinimo = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHAFECHARCARGAPESOMENORMINIMO));
		/*970*/ usaCalculoDeTributacaoPersonalizado = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO), "usa", ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO);
		/*970-2*/ mostraDescAcessoriaCapaPedido = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO), "mostraDescAcessoriaCapaPedido", ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO);
		/*970-4*/ mostraPrecoItemSt = getValorAtributoJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO), "mostraPrecoItemSt", ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO);
		/*970-3*/ usaFreteManualPedido = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO), "usaFreteManualPedido", ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO);
		/*971*/ vlPctToleranciaRentabilidadeMinima = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLPCTTOLERANCIARENTABILIDADEMINIMA));
		/*972*/ usaVerbaPorFaixaRentabilidadeComissao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBAPORFAIXARENTABILIDADECOMISSAO));
		/*973*/	ordemCamposTelaInfoExtra = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ORDEMCAMPOSTELAINFOEXTRA);
		isUsaOrdemCamposTelaInfoExtra = isOrdemCamposValida(ordemCamposTelaInfoExtra);
		/*974*/ usaTipoPedidoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOPEDIDOPORCLIENTE));
		/*975*/ aplicaComissaoEspecialProdutoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICACOMISSAOESPECIALPRODUTOPROMOCIONAL));
		/*977*/ usaIndicacaoQuilometragemTempoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICACAOQUILOMETRAGEMTEMPONOPEDIDO));
		/*978*/ configDiasMedioCondPagto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDIASMEDIOCONDPAGTO);
		/*979*/ usaControleRentabilidadePorFaixa = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLERENTABILIDADEPORFAIXA));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLERENTABILIDADEPORFAIXA))) {
			usaControleRentabilidadePorFaixa = 1;
		} else if (usaControleRentabilidadePorFaixa > 3) {
			usaControleRentabilidadePorFaixa = 0;
		}
		/*980*/ usaDescItemPorCanalCliEGrupoProdEContratoCli = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCITEMPORCANALCLIEGRUPOPRODECONTRATOCLI));
		/*982*/ qtdItensRentabilidadeIdealSugeridos = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDITENSRENTABILIDADEIDEALSUGERIDOS));
		/*983*/ usaControleEspecialEdicaoUnidades = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLEESPECIALEDICAOUNIDADES));
		/*984*/ usaSinalizadorRentabilidadeBaseadoIndiceCalculado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASINALIZADORRENTABILIDADEBASEADOINDICECALCULADO));
		/*985*/ vlToleranciaIndiceMinimoRentabilidadePedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLTOLERANCIAINDICEMINIMORENTABILIDADEPEDIDO));
		/*986*/ usaCalculoVerbaComImpostoERentabilidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOVERBACOMIMPOSTOERENTABILIDADE));
		/*987*/ nuDiasValidadeCustoItem = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASVALIDADECUSTOITEM));
		/*988*/ usaControleDataEntregaPedidoPelaCarga = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLEDATAENTREGAPEDIDOPELACARGA);
		/*989*/ permiteCadastrarCargaNaCapaDoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITECADASTRARCARGANACAPADOPEDIDO));
		/*991*/ usaVerbaGrupoProdComToleranciaNoDesconto = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAVERBAGRUPOPRODCOMTOLERANCIANODESCONTO));
		/*993*/ nuDiasRestantesAvisoSaldoVerbaGrupo = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASRESTANTESAVISOSALDOVERBAGRUPO));
		/*994*/	permitePedidoAVistaClienteBloqueadoAtrasado = getValorAtributoJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEPEDIDOAVISTACLIENTEBLOQUEADOATRASADO), "usa", ValorParametro.PERMITEPEDIDOAVISTACLIENTEBLOQUEADOATRASADO);
		if (ValueUtil.VALOR_SIM.equals(permitePedidoAVistaClienteBloqueadoAtrasado)) {
			permitePedidoAVistaClienteBloqueadoAtrasado = "1";
		}
		/*994-2*/ liberaTipoCondPagtoPedidoDias = isAtributoJsonLigado(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEPEDIDOAVISTACLIENTEBLOQUEADOATRASADO), "liberaTipoCondPagtoPedidoDias", ValorParametro.PERMITEPEDIDOAVISTACLIENTEBLOQUEADOATRASADO);
		/*996*/ usaRetornoAutomaticoDadosRelativosPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARETORNOAUTOMATICODADOSRELATIVOSPEDIDO);
		setValuesRetornoAutomaticoPedido();
		/*999*/ valorTimeOutRetornoDadosRelativosPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORTIMEOUTRETORNODADOSRELATIVOSPEDIDO)) * 1000;
		if (valorTimeOutRetornoDadosRelativosPedido == 0) {
			valorTimeOutRetornoDadosRelativosPedido = 20000;
		}
		/*1000*/ vlIndiceSeguroCalculadoNoItemPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLINDICESEGUROCALCULADONOITEMPEDIDO));
		/*1001*/ sugereImpressaoDocumentosPedidoAposEnvio = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SUGEREIMPRESSAODOCUMENTOSPEDIDOAPOSENVIO); 
		/*1002*/ configImpressaoNfeViaBluetooth = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH);
		/*1007*/ usaListaSacCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLISTASACCLIENTE);
		/*1008*/ aplicaVariacaoPrecoProdutoPorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAVARIACAOPRECOPRODUTOPORCLIENTE));
		/*1009*/ validaClienteAtrasadoApenasAoFecharPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISACLIENTEATRASADOAPENASAOFECHARPEDIDO));
	    /*1010*/ permiteAtualizarManualmenteDadosCadastraisCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEATUALIZARMANUALMENTEDADOSCADASTRAISCLIENTE);
		/*1011*/ configCadastroFotoNovoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCADASTROFOTONOVOCLIENTE);
		/*1012*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFDESCONTOPORQTDENOGRUPODESCPROMOCIONAL);
		/*1012*/ usaDescontoQtdeNoGrupoDescPromocional = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFDESCONTOPORQTDENOGRUPODESCPROMOCIONAL);
		/*1012*/ ocultaPercentualDesconto = isAtributoJsonLigado(vlParam, "ocultaPercentualDesconto", ValorParametro.CONFDESCONTOPORQTDENOGRUPODESCPROMOCIONAL);

		/*1013*/ usaNavegacaoGpsNoMapa = getValorParametroPorSistema(hashValorParametros, ValorParametro.USANAVEGACAOGPSNOMAPA);
		if (ValueUtil.VALOR_SIM.equals(usaNavegacaoGpsNoMapa)) {
			usaNavegacaoGpsNoMapa = "1";
		}
		/*1014*/ aplicaReducaoSimplesAposCalculoValorItem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAREDUCAOSIMPLESAPOSCALCULOVALORITEM));
		/*1018*/ ordemCamposTelaItemPedidoBonificacao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ORDEMCAMPOSTELAITEMPEDIDOBONIFICACAO);
		/*1020*/ usaPositivacaoAgendaVisitaSemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPOSITIVACAOAGENDAVISITASEMPEDIDO);
		if (ValueUtil.VALOR_SIM.equals(usaPositivacaoAgendaVisitaSemPedido)) {
			usaPositivacaoAgendaVisitaSemPedido = "1";
		}
		/*1022*/ permiteDecidirModoRegistroFaltaEstoqueProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDECIDIRMODOREGISTROFALTAESTOQUEPRODUTO);
		/*1021*/ resolucaoCameraRegistroFotos = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RESOLUCAOCAMERAREGISTROFOTOS));
		//ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDECIDIRMODOREGISTROFALTAESTOQUEPRODUTO));
//		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDECIDIRMODOREGISTROFALTAESTOQUEPRODUTO))) {
//			permiteDecidirModoRegistroFaltaEstoqueProduto = 1;
//		}
		/*1026*/
		configClienteEmModoProspeccao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCLIENTEEMMODOPROSPECCAO);
		usaClienteEmModoProspeccao = isAtributoJsonLigado(configClienteEmModoProspeccao, "usa", ValorParametro.CONFIGCLIENTEEMMODOPROSPECCAO);
		permiteInativarClienteProspeccao = isAtributoJsonLigado(configClienteEmModoProspeccao, "permiteInativarClienteProspeccao", ValorParametro.CONFIGCLIENTEEMMODOPROSPECCAO);
		/*1029*/ liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAPRECOBASEADOPERCENTUALUSUARIOESCOLHIDO));
		/*1030*/ sementeSenhaPrecoBaseadoPercentualUsuarioEscolhido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAPRECOBASEADOPERCENTUALUSUARIOESCOLHIDO));
		/*1031*/ usaConfirmacaoEntregaPedidoDiaNaoUtil = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONFIRMACAOENTREGAPEDIDODIANAOUTIL));
		/*1032*/ validaPctMaxDescAcrescPorUsuario = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDAPCTMAXDESCACRESCPORUSUARIO));
		/*1033*/ usaRegistroChegadaSaidaClienteVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREGISTROCHEGADASAIDACLIENTEVISITA));
		/*1034*/ usaSugestaoRegistroChegadaNoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOREGISTROCHEGADANOCLIENTE);
		if (ValueUtil.VALOR_SIM.equals(usaSugestaoRegistroChegadaNoCliente)) {
			usaSugestaoRegistroChegadaNoCliente = "1";
		}
		/*1041*/ exibeRelatorioNovosSacs = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBERELATORIONOVOSSACS);
		/*1042*/ qtMinimaFotosCadastroNovoCliente = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMINIMAFOTOSCADASTRONOVOCLIENTE));
		/*1049*/ configMultiplosEnderecosCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMULTIPLOSENDERECOSCLIENTE);
		/*1050*/ adicionaCodigoUsuarioAoNumeroPedido = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.ADICIONACODIGOUSUARIOAONUMEROPEDIDO);
		/*1053*/ nuCasasDecimaisPisCofinsIcms = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUCASASDECIMAISPISCOFINSICMS));
		if (nuCasasDecimaisPisCofinsIcms == 0) {
			nuCasasDecimaisPisCofinsIcms = nuCasasDecimais;
		}
		/*1054*/ configEnderecosPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGENDERECOSPEDIDO);
		/*1055*/ usaOrigemPedidoPortal = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAORIGEMPEDIDOPORTAL));
		/*1056*/ configMultiplosEnderecosCadastroNovoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEMULTIPLOSENDERECOSCADASTRONOVOCLIENTE);
		/*1058*/ usaSugestaoRegistroSaidaNoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOREGISTROSAIDANOCLIENTE);
		/*1061*/ usaImpressaoBoletoViaBluetooth = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOBOLETOVIABLUETOOTH));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOBOLETOVIABLUETOOTH))) {
			usaImpressaoBoletoViaBluetooth = 1;
		}
		/*1062*/ usaImpressaoComprovanteBoleto = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCOMPROVANTEBOLETO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCOMPROVANTEBOLETO))) {
			usaImpressaoComprovanteBoleto = 1;
		}
		/*1063*/ liberaComSenhaVendaProdutoSemEstoque = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAVENDAPRODUTOSEMESTOQUE));
		/*1064*/ sementeSenhaVendaProdutoSemEstoque = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHAVENDAPRODUTOSEMESTOQUE));
		/*1065*/ pulaDataEntregaEmDiasDeFeriadoProxDia = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PULADATAENTREGAEMDIASDEFERIADOPROXDIA);
		/*1069*/ usaPrecoPadraoListaProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRECOPADRAOLISTAPRODUTOS);
		/*1080*/ usaAgendaVisitaFinalDeSemana = getValorParametroPorSistema(hashValorParametros, ValorParametro.USAAGENDAVISITAFINALDESEMANA);
		/*1084*/ usaReservaEstoqueCentralizado = getValorParametroPorSistema(hashValorParametros, ValorParametro.USARESERVAESTOQUECENTRALIZADO);
		/*1085*/ usaPedidoComplementar = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAPEDIDOCOMPLEMENTAR));
		if (!ValueUtil.isValidNumber(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAPEDIDOCOMPLEMENTAR))) {
			usaPedidoComplementar = -1;
		}
		/*1092*/ usaClienteKeyAccount =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACLIENTEKEYACCOUNT));
		/*1093*/ registraSaidaClienteAoFecharPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.REGISTRASAIDACLIENTEAOFECHARPEDIDO));
		/*1087*/ registraFusoHorarioVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.REGISTRAFUSOHORARIOVISITA));
		/*1089*/ usaColetaGpsManual = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACOLETAGPSMANUAL);
		/*1090*/ avisaColetaGpsParada = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISACOLETAGPSPARADA));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.AVISACOLETAGPSPARADA))) {
			avisaColetaGpsParada = 1;
		}
		/*1097*/ liberaSenhaQuantidadeMaximaVendaProduto =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERASENHAQUANTIDADEMAXIMAVENDAPRODUTO));
		/*1098*/ destacaProdutoQuantidadeMaximaVenda =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACAPRODUTOQUANTIDADEMAXIMAVENDA));
		/*1099*/ aplicaAlteracoesCadastroClienteEnderecoAutomaticamente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAALTERACOESCADASTROCLIENTEENDERECOAUTOMATICAMENTE);
		/*1103*/ bloquearNovoPedidoClienteBloqueadoPorAtraso = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEARNOVOPEDIDOCLIENTEBLOQUEADOPORATRASO));
		/*1104*/ destacaClienteBloqueadoPorAtrasoNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACACLIENTEBLOQUEADOPORATRASONALISTA));
		/*1105*/ usaReagendamentoAgendaVisita =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREAGENDAMENTOAGENDAVISITA));
		/*1106*/ usaMotivoReagendamentoTransferenciaAgenda =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMOTIVOREAGENDAMENTOTRANSFERENCIAAGENDA));
		/*1107*/ nuDiasMaximoReagendamentoTransferencia =  ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASMAXIMOREAGENDAMENTOTRANSFERENCIA));
		/*1108*/ usaDescontoPonderadoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPONDERADOPEDIDO));
		/*1109*/ restringeDescontoPedidoBaseadoMediaPonderada = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RESTRINGEDESCONTOPEDIDOBASEADOMEDIAPONDERADA));
		/*1110*/ usaMultiplasLiberacoesDescontoNoPedido = getValorParametroPorSistema(hashValorParametros, ValorParametro.USAMULTIPLASLIBERACOESDESCONTONOPEDIDO);
		/*1111*/ usaSinalizadorRentabilidadeBaseadoItemTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASINALIZADORRENTABILIDADEBASEADOITEMTABELAPRECO));
		/*1112*/ usaAvisoRentabilidadeItemAbaixoEsperado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAVISORENTABILIDADEITEMABAIXOESPERADO);
		/*1113*/ usaDestaqueStatusRentabilidadeCliente =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESTAQUESTATUSRENTABILIDADECLIENTE));
		/*1117*/ usaTransferenciaAgendaVisitaParaAgendaAtendimento =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATRANSFERENCIAAGENDAVISITAPARAAGENDAATENDIMENTO));
		/*1119*/ usaPctManualAcrescimoCustoNoPedido =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPCTMANUALACRESCIMOCUSTONOPEDIDO));
		/*1121*/ usaPctMaxParticipacaoItemBonificacao =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPCTMAXPARTICIPACAOITEMBONIFICACAO));
		/*1122*/ usaApenasItemPedidoOriginalNaBonificacaoTroca =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPENASITEMPEDIDOORIGINALNABONIFICACAOTROCA));
		/*1123*/ configPercentualQuantidadeOriginalBonificacaoTroca = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPERCENTUALQUANTIDADEORIGINALBONIFICACAOTROCA);
		/*1124*/ liberaSenhaQtdItemMaiorPedidoOriginal =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERASENHAQTDITEMMAIORPEDIDOORIGINAL));
		/*1125*/ sementeSenhaQtdItemMaiorPedidoOriginal = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAQTDITEMMAIORPEDIDOORIGINAL));
		/*1127*/ usaAvisoClienteSemPesquisaMercado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAVISOCLIENTESEMPESQUISAMERCADO);
		/*1128*/ usaPesquisaMercadoProdutosConcorrentes = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPESQUISAMERCADOPRODUTOSCONCORRENTES);
		/*1132*/ usaMultiplasSugestoesProdutoFechamentoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMULTIPLASSUGESTOESPRODUTOFECHAMENTOPEDIDO);
		/*1133*/ qtLimiteItensSugestaoListasEspecificas =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTLIMITEITENSSUGESTAOLISTASESPECIFICAS);
		/*1134*/ qtLimiteItensMultiplasSugestoesListaGeral =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTLIMITEITENSMULTIPLASSUGESTOESLISTAGERAL);
		/*1140*/ aplicaPercentualFreteCalculoPrecoItem =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAPERCENTUALFRETECALCULOPRECOITEM));
		/*1142*/ usaAtualizacaoEscolhaClienteEnvioEmail =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAATUALIZACAOESCOLHACLIENTEENVIOEMAIL));
		/*1146*/ usaExpressaoRegularValidacaoSenhaUsuario =  getValorParametroPorSistema(hashValorParametros, ValorParametro.USAEXPRESSAOREGULARVALIDACAOSENHAUSUARIO);
		/*1150*/ permitePesquisaMercadoNovoCliente =  ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.PERMITEPESQUISAMERCADONOVOCLIENTE));		
		/*1151*/ nuMetrosToleranciaPrecisaoCoordenada = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUMETROSTOLERANCIAPRECISAOCOORDENADA));
		/*1152*/ usaImpressaoComprovanteNfe = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCOMPROVANTENFE));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCOMPROVANTENFE))) {
			usaImpressaoComprovanteNfe = 1;
		}
		/*1153*/ exibeQuantidadeEstoqueFaltanteItemNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBEQUANTIDADEESTOQUEFALTANTEITEMNALISTA));
		/*1154*/ usaEnvioPedidoPendenteParaAutorizacaoEquipamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAENVIOPEDIDOPENDENTEPARAAUTORIZACAOEQUIPAMENTO));
		/*1157*/ nuLinhasRetornoBuscaSistema = nuLinhasRetornoBuscaSistema(getValorParametroPorSistema(hashValorParametros, ValorParametro.NULINHASRETORNOBUSCASISTEMA));
		/*1159*/ qtMaxVendaProdutoMes =  ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMAXVENDAPRODUTOMES));
		/*1160*/ dsTipoClienteQtMaxVendaProduto =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DSTIPOCLIENTEQTMAXVENDAPRODUTO);
		/*1161*/ restringeTabelaPrecoPorCondicaoComercial =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RESTRINGETABELAPRECOPORCONDICAOCOMERCIAL));
		/*1162*/ usaTipoFretePorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOFRETEPORCLIENTE));
		/*1163*/ usaCreditoIndiceCondicaoPagamentoNaBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACREDITOINDICECONDICAOPAGAMENTONABONIFICACAO));
		/*1164*/ usaCreditoIndiceTipoFreteCliNaBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACREDITOINDICETIPOFRETECLINABONIFICACAO));
		/*1165*/ usaValorMaximoBonificaoPorCreditoPedidoVenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORMAXIMOBONIFICAOPORCREDITOPEDIDOVENDA));
		/*1167*/ vlPctLimiteSomaCanalContratoDecisaoCalculo = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLPCTLIMITESOMACANALCONTRATODECISAOCALCULO));
		/*1168*/ vlPctLimiteContratoDecisaoCalculoDesc = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLPCTLIMITECONTRATODECISAOCALCULODESC));
		/*1169*/ usaAplicativoExternoParaColetaGps =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPLICATIVOEXTERNOPARACOLETAGPS));
		/*1170*/ nuDiasPermanenciaLogsWgps =  ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIALOGSWGPS));
		/*1171*/ usaCadastroCoordenadasGeograficasNovoCliente =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACADASTROCOORDENADASGEOGRAFICASNOVOCLIENTE);
		/*1172*/ obrigaCadCoordenadasGeograficasNovoCliente =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OBRIGACADCOORDENADASGEOGRAFICASNOVOCLIENTE));
		/*1173*/ usaMotivoParadaColetaGps =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMOTIVOPARADACOLETAGPS));
		/*1175*/ permiteCondComercialItemDiferentePedido =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITECONDCOMERCIALITEMDIFERENTEPEDIDO));
		/*1176*/ aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICAINDICESNAUNIDADEPADRAOPARAUNIDADEALTERNATIVA));
		/*1177*/ bloqueiaSistemaColetaGpsManualDesligada =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIASISTEMACOLETAGPSMANUALDESLIGADA));
		/*1179*/ qtMaxCaracteresOrdemCompraNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTMAXCARACTERESORDEMCOMPRANOPEDIDO);
		/*1180*/ usaVerbaSaldoPorFornecedor =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBASALDOPORFORNECEDOR));
		/*1181*/ usaPrecoItemPorPesoMinimoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRECOITEMPORPESOMINIMOPEDIDO);
		/*1182*/ usaDestaqueItensVendidosMesCorrente =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESTAQUEITENSVENDIDOSMESCORRENTE));
		/*1184*/ usaIncrementoQuantidadePorLeituraCodigoBarras = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINCREMENTOQUANTIDADEPORLEITURACODIGOBARRAS));
		/*1185*/ vlPctToleranciaVisitasForaAgenda = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLPCTTOLERANCIAVISITASFORAAGENDA);
		/*1186*/ liberaSenhaVisitaClienteForaAgenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERASENHAVISITACLIENTEFORAAGENDA));
		/*1187*/ sementeSenhaVisitaClienteForaAgenda = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA));
		/*1188*/ marcaItemPedidoPendenteAprovacao = getVlParamAsInt(getValorParametroPorSistema(hashValorParametros, ValorParametro.MARCAITEMPEDIDOPENDENTEAPROVACAO));
		/*1191*/ usaGrupoDescPromocionalNoDescQtdPorGrupo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGRUPODESCPROMOCIONALNODESCQTDPORGRUPO));
		/*1190*/ permiteLiberacaoPedidoPendenteOutraOrdemLiberacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITELIBERACAOPEDIDOPENDENTEOUTRAORDEMLIBERACAO));
		/*1192*/ geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.GERAVERBASALDOUSUARIOATRAVESCONFIGURACAORECORRENTE));
		/*1194*/ usaTipoPessoaFisicaComoPadraoCadastroNovoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOPESSOAFISICACOMOPADRAOCADASTRONOVOCLIENTE));
		/*1195*/ aplicaDescProgressivoPorMixPorItemFinalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCPROGRESSIVOPORMIXPORITEMFINALPEDIDO));
		/*1196*/ ignoraDescontroProgressivoProdutoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORADESCONTROPROGRESSIVOPRODUTOPROMOCIONAL));
		/*1197*/ usaTotalizadoresEspecificosListaCliente = getValorParametroPorSistema(hashValorParametros, ValorParametro.USATOTALIZADORESESPECIFICOSLISTACLIENTE);
		/*1198*/ usaDescontoProgressivoPorCondComercial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPROGRESSIVOPORCONDCOMERCIAL));
		/*1200*/ usaRelDescontosAplicadosNoItemPedidoPorFuncionalidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELDESCONTOSAPLICADOSNOITEMPEDIDOPORFUNCIONALIDADE));
		/*1202*/ usaPrecoItemComValoresAdicionaisEmbutidos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRECOITEMCOMVALORESADICIONAISEMBUTIDOS));
		/*1203*/ usaControleOnlineUsuariosInativos = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACONTROLEONLINEUSUARIOSINATIVOS));
		/*1204*/ aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCACRESCNAUNIDADEPADRAOPARAUNIDADEALTERNATIVA));
		/*1205*/ usaRamoAtividadeSugestaoProdutoComCadastro = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USARAMOATIVIDADESUGESTAOPRODUTOCOMCADASTRO);
		/*1207*/ configCadastroDescontoPromocional = getValorParametroPorSistema(ValorParametro.CONFIGCADASTRODESCONTOPROMOCIONAL);
		/*1206*/ filtraProdutoPorGrupoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRAPRODUTOPORGRUPOCLIENTE));
		/*1208*/ usaAtualizacaoTipoPessoaCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAATUALIZACAOTIPOPESSOACLIENTE));
		/*1209*/ usaToleranciaVisitasForaDeAgendaPorClienteVisitado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATOLERANCIAVISITASFORADEAGENDAPORCLIENTEVISITADO));
		/*1210*/ usaDataLimiteValidadeAgendaVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USADATALIMITEVALIDADEAGENDAVISITA));
		/*1211*/ permiteIndicarAgendaVisitaValidaParaDataUnica = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.PERMITEINDICARAGENDAVISITAVALIDAPARADATAUNICA));
		/*1212*/ usaCadastroAgendaVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACADASTROAGENDAVISITA));
		/*1213*/ usaTipoFretePorEstado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOFRETEPORESTADO));
		/*1214*/ usaDescontoPedidoPorTipoFrete = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPEDIDOPORTIPOFRETE));
		/*1215*/ usaMultiplosPagamentosParaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMULTIPLOSPAGAMENTOSPARAPEDIDO));
		/*1217*/ usaCalculoVolumeItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOVOLUMEITEMPEDIDO);
		/*1219*/ permiteReagendamentoAgendaParaDataIgualOriginal = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEREAGENDAMENTOAGENDAPARADATAIGUALORIGINAL));
		/*1220*/ usaDescontosAutoEmCascataNaCapaPedidoPorItem = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOSAUTOEMCASCATANACAPAPEDIDOPORITEM);
		/*1224*/ usaPctMaxDescontoPagamentoPorCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPCTMAXDESCONTOPAGAMENTOPORCONDPAGTO));
		/*1225*/ permiteRegistrarMotivoVisitaMultiplasVisitas = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEREGISTRARMOTIVOVISITAMULTIPLASVISITAS);
		/*1227*/ usaCaracteristicasClienteNoSistema = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACARACTERISTICASCLIENTENOSISTEMA);
		/*1228*/ usaTipoAgendaNaAgendaVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOAGENDANAAGENDAVISITA));
		/*1229*/ usaCopiaAgendaRepresentanteParaSupervisor = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USACOPIAAGENDAREPRESENTANTEPARASUPERVISOR));
		/*1230*/ usaMultiplasLiberacoesParaClienteComSenhaUnica = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAMULTIPLASLIBERACOESPARACLIENTECOMSENHAUNICA));
		/*1231*/ nuCasasDecimaisVlVolume = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUCASASDECIMAISVLVOLUME));
		if (nuCasasDecimaisVlVolume == 0) {
			nuCasasDecimaisVlVolume = nuCasasDecimais;
		}
		/*1233*/ usaValidacaoCamposDinamicosObrigatoriosAoFecharPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALIDACAOCAMPOSDINAMICOSOBRIGATORIOSAOFECHARPEDIDO));
		/*1234*/ reagendaAgendaVisitaParaDiaPosteriorAnterior = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.REAGENDAAGENDAVISITAPARADIAPOSTERIORANTERIOR));
		/*1235*/ ocultaInfoRentabilidadeManualmente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAINFORENTABILIDADEMANUALMENTE);
		/*1236*/ exibeAbaTotalizadoresPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBEABATOTALIZADORESPEDIDO);
		/*1237*/ usaInformacoesAdicionaisPagamentoCheque = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINFORMACOESADICIONAISPAGAMENTOCHEQUE);
		/*1238*/ usaTextoPadraoReferenteCheque = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATEXTOPADRAOREFERENTECHEQUE));
		/*1239*/ nuDiasMaximoVencimentoToleranciaPagamento = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASMAXIMOVENCIMENTOTOLERANCIACHEQUE));
		/*1240*/ usaOrdenacaoRankingProduto = getValorParametroPorSistema(hashValorParametros, ValorParametro.USAORDENACAORANKINGPRODUTO);
		/*1241*/ permiteInserirEmailAlternativoPedido = getValorParametroPorSistema(hashValorParametros, ValorParametro.PERMITEINSERIREMAILALTERNATIVOPEDIDO);
		/*1243*/ permiteApresentarApenasVerbaUtilizadaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEAPRESENTARAPENASVERBAUTILIZADAPEDIDO));
		/*1245*/ liberaSenhaDiaEntregaPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERASENHADIAENTREGAPEDIDO);
		/*1246*/ sementeSenhaDiaEntregaPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHADIAENTREGAPEDIDO));
		/*1247*/ usaNfePorReferencia = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USANFEPORREFERENCIA));
		/*1248*/ qtdMinimaProdutosRestricaoPromocionalPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDMINIMAPRODUTOSRESTRICAOPROMOCIONALPEDIDO);
		/*1249*/ usaOrdenacaoEstoqueNaGradeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDENACAOESTOQUENAGRADEPRODUTO));
		/*1250*/ enviaEstoqueRepParaServidor = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ENVIAESTOQUEREPPARASERVIDOR));
		/*1251*/ usaValidacaoAcrescDescMaxNoDescontoPromocional = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALIDACAOACRESCDESCMAXNODESCONTOPROMOCIONAL);
		/*1252*/ usaTrocaRecolherComoDescontoPagamentoPedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATROCARECOLHERCOMODESCONTOPAGAMENTOPEDIDO));
		/*1253*/ pctMaxTrocaRecolherPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PCTMAXTROCARECOLHERPEDIDO));
		if (pctMaxTrocaRecolherPedido == 0d) {
			pctMaxTrocaRecolherPedido = 100;
		}
		/*1254*/ qtdDiasMaximoTrocaItemPedido = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDDIASMAXIMOTROCAITEMPEDIDO));
		/*1255*/ usaDiaEntregaBairroParaCalculoDataEntrega = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADIAENTREGABAIRROPARACALCULODATAENTREGA));
		/*1256*/ usaHorarioLimiteColetaGpsManual = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAHORARIOLIMITECOLETAGPSMANUAL);
		/*1257*/ usaDivisaoValorVerbaUsuarioEmpresa = getVlPctDivisaoVerbaUsuario(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USADIVISAOVALORVERBAUSUARIOEMPRESA));
		/*1259*/ vlPctMargemDescontoItemPedido = ValueUtil.getDoubleValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.VLPCTMARGEMDESCONTOITEMPEDIDO));
		/*1260*/ usaGradeProdutoPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGRADEPRODUTOPORTABELAPRECO));
		/*1261*/ usaValorPadraoSimFlEnviaEmail = getValorParametroPorSistema(hashValorParametros, ValorParametro.USAVALORPADRAOSIMFLENVIAEMAIL);
		/*1263*/ grifaProdutoSemEstoqueEmUmaGradeNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRIFAPRODUTOSEMESTOQUEEMUMAGRADENALISTA));
		/*1264*/ usaSolicitacaoDadosPessoaisAcessoSistema = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASOLICITACAODADOSPESSOAISACESSOSISTEMA));
		/*1265*/ gravaCodigoVerbaItemPedidoPorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRAVACODIGOVERBAITEMPEDIDOPORTABELAPRECO));
		/*1266*/ usaMargemProdutoAplicadaNoValorBaseVerba = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMARGEMPRODUTOAPLICADANOVALORBASEVERBA));
		/*1270*/ naoConsideraRegistroVisitaManualTolerancia = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOCONSIDERAREGISTROVISITAMANUALTOLERANCIA);
		/*1271*/ usaRateioProducaoPorRepresentante = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARATEIOPRODUCAOPORREPRESENTANTE);
		/*1272*/ usaControleEstoqueProdutoRateioProducao = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACONTROLEESTOQUEPRODUTORATEIOPRODUCAO));
		/*1276*/ nuResolucaoFotoCameraNativa = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NURESOLUCAOFOTOCAMERANATIVA);
		if (ValueUtil.VALOR_SIM.equals(nuResolucaoFotoCameraNativa)) {
			nuResolucaoFotoCameraNativa = "640";
		}
		/*1277*/ usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADECISAOPRECOBASEADOCANALCLIEGRUPOPRODECONTRATOCLI));
		/*1278*/ qtdCalculoRecursivoContratoCli = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTDCALCULORECURSIVOCONTRATOCLI));
		/*1280*/ usaCotaValorQuantidadeRetirarAcresCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACOTAVALORQUANTIDADERETIRARACRESCONDPAGTO));
		/*1281*/ apresentaDiasEntregaEmpresaCadastroEndereco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTADIASENTREGAEMPRESACADASTROENDERECO));
		/*1282*/ usaDataCarregamentoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADATACARREGAMENTOPEDIDO));
		/*1283*/ apresentaDadosEntregaNaAbaPrincipalPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTADADOSENTREGANAABAPRINCIPALPEDIDO));
		/*1284*/ usaApresentacaoValorStCapaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPRESENTACAOVALORSTCAPAPEDIDO));
		/*1285*/ usaAvisoControleInsercaoItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAVISOCONTROLEINSERCAOITEMPEDIDO);
		/*1286*/ usaRetornoAutomaticoDadosRelativosPedidoBackground = getValorParametroPorSistema(hashValorParametros, ValorParametro.USARETORNOAUTOMATICODADOSRELATIVOSPEDIDOBACKGROUND);
		/*1287*/ nuIntervaloVerificaoConexaoEquipamento = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUINTERVALOVERIFICAOCONEXAOEQUIPAMENTO));
		/*1288*/ usaImpressaoContingenciaNfeViaBluetooth = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCONTINGENCIANFEVIABLUETOOTH);
		/*1289*/ qtCopiasImpressaoContingenciaNfe = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTCOPIASIMPRESSAOCONTINGENCIANFE));
		qtCopiasImpressaoContingenciaNfe = qtCopiasImpressaoContingenciaNfe == 0 ? 1 : qtCopiasImpressaoContingenciaNfe;
		/*1290*/ cdStatusPedidoConsignado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CDSTATUSPEDIDOCONSIGNADO);
		/*1291*/ usaDevolucaoPedidosEmConsignacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADEVOLUCAOPEDIDOSEMCONSIGNACAO));
		/*1292*/ nuDiasValidadePedidoEmConsignacao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASVALIDADEPEDIDOEMCONSIGNACAO);
		/*1293*/ apresentaListaConsignacoesPrimeiroLogin = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTALISTACONSIGNACOESPRIMEIROLOGIN));
		/*1294*/ obrigaCampoRelacionadoCasoCampoEstejaPreenchido = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.OBRIGACAMPORELACIONADOCASOCAMPOESTEJAPREENCHIDO));
		/*1295*/ usaContatoERPClienteNoPedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTATOERPCLIENTENOPEDIDO));
		/*1296*/ geraLogAcaoPedidoItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.GERALOGACAOPEDIDOITEMPEDIDO));
		/*1298*/ usaValorComStCalculoVerba = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORCOMSTCALCULOVERBA));
		/*1299*/ usaIndicacaoClienteEntregaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICACAOCLIENTEENTREGAPEDIDO));
		/*1311*/ usaMotivoAgendaNaoPositivadaMultiplasEmpresas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMOTIVOAGENDANAOPOSITIVADAMULTIPLASEMPRESAS));
		/*1300*/ usaAdicionaNuNotaGeralNaNotaFiscal = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAADICIONANUNOTAGERALNANOTAFISCAL));
		/*1301*/ liberaComSenhaCancelamentoConsignacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHACANCELAMENTOCONSIGNACAO));
		/*1302*/ sementeSenhaLiberaComSenhaCancelamentoConsignacao = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHALIBERACOMSENHACANCELAMENTOCONSIGNACAO));
		/*1303*/ usaImpressaoPedidoConsignacaoDevolucao = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOPEDIDOCONSIGNACAODEVOLUCAO));
		/*1304*/ usaImpressaoComprovanteConsignacaoDevolucao = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCOMPROVANTECONSIGNACAODEVOLUCAO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOCOMPROVANTECONSIGNACAODEVOLUCAO))) {
			usaImpressaoComprovanteConsignacaoDevolucao = 1;
		}
		/*1305*/ qtCopiasImpressaoPedidoConsignacaoDevolucao = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTCOPIASIMPRESSAOPEDIDOCONSIGNACAODEVOLUCAO));
		if (qtCopiasImpressaoPedidoConsignacaoDevolucao == 0) {
			qtCopiasImpressaoPedidoConsignacaoDevolucao = 1;
		}
		/*1306*/ usaSugestaoImpressaoPedidoConsignacaoDevolucao = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOIMPRESSAOPEDIDOCONSIGNACAODEVOLUCAO));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOIMPRESSAOPEDIDOCONSIGNACAODEVOLUCAO))) {
			usaSugestaoImpressaoPedidoConsignacaoDevolucao = 1;
		}
		/*1307*/ nuDiasPermanenciaPedidoConsignacao = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAPEDIDOCONSIGNACAO);
		/*1308*/ usaGeracaoBoletoContingencia = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGERACAOIMPRESSAOBOLETOCONTINGENCIA));
		/*1312*/ configDescontoAcrescimoItemPedidoPorTipoPedido = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCONTOACRESCIMOPEDIDOPORTIPOPEDIDO), ValorParametro.CONFIGDESCONTOACRESCIMOPEDIDOPORTIPOPEDIDO);
		/*1313*/ usaProvisionamentoConsumoVerbaSaldo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPROVISIONAMENTOCONSUMOVERBASALDO);
		/*1316*/ configValorMinimoItemPedidoPorItemTabelaPreco = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGVALORMINIMOITEMPEDIDOPORITEMTABELAPRECO);
		/*1317*/ usaValorComImpostosControleValorMinimoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORCOMIMPOSTOSCONTROLEVALORMINIMOPEDIDO));
		/*1318*/ usaHoraFimAgendaVisita = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAHORAFIMAGENDAVISITA);
		/*1321*/ usaCamposDinamicosMontagemDetalhesCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACAMPOSDINAMICOSMONTAGEMDETALHESCLIENTE));
		/*1322*/ permiteSelecionarFotoArmazenadaCadastroFoto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITESELECIONARFOTOARMAZENADACADASTROFOTO));
		/*1324*/ usaValorStCalculoMinimoItemPedidoPorItemTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORSTCALCULOMINIMOITEMPEDIDOPORITEMTABELAPRECO));
		/*1327*/ usaCadastroProdutoDesejadosForaCatalogo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACADASTROPRODUTODESEJADOSFORACATALOGO);
		/*1328*/ nuDiasPermanenciaRegistroProdutosDesejados = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAREGISTROPRODUTOSDESEJADOS));
		if (ValueUtil.VALOR_NAO.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIAREGISTROPRODUTOSDESEJADOS))) {
			nuDiasPermanenciaRegistroProdutosDesejados = -1;
		}
		/*1329*/ usaSistemaIdiomaPersonalizado = getValorParametroPorSistema(hashValorParametros, ValorParametro.USASISTEMAIDIOMAPERSONALIZADO);
		/*1330*/ usaNumerosAleatoriosSalvosGeracaoSenha = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USANUMEROSALEATORIOSSALVOSGERACAOSENHA));
		/*1335*/ qtCaracteresMinimoDescricaoProdutoDesejado = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTCARACTERESMINIMODESCRICAOPRODUTODESEJADO));
		/*1337*/ usaPedidoQualquerRepresentanteParaHistoricoCliente  = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPEDIDOQUALQUERREPRESENTANTEPARAHISTORIOCLIENTE));
		/*1339*/ usaRegistroAtividadeRelacionadaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREGISTROATIVIDADERELACIONADAPEDIDO));
		/*1340*/ nuDiasPermanenciaAtividadePedido =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIAATIVIDADEPEDIDO);
		/*1341*/ usaFotoPedidoNoSistema = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFOTOPEDIDONOSISTEMA));
		/*1342*/ configDocumentosAnexo = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGDOCUMENTOSANEXO);
		/*1342-2*/ usaSelecaoDocumentosAnexo = getValorAtributoJson(configDocumentosAnexo, "usa", ValorParametro.CONFIGDOCUMENTOSANEXO);
		           limiteMegabytes = populeLimiteMegabytes();
		/*1343*/ nuDiasPermanenciaDocumentoAnexo = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIADOCUMENTOANEXO);
		/*1347*/ configTituloFinanceiro = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGTELATITULOFINANCEIRO);
		/*1348*/ usaCalculoReversoNaST = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOREVERSONAST));
		/*1353*/ usaPedidoComplementarMesmaConfPedidoOriginal = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPEDIDOCOMPLEMENTARMESMACONFPEDIDOORIGINAL));
		/*1354*/ usaIndicacaoDadosBancariosClienteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICACAODADOSBANCARIOSCLIENTENOPEDIDO));
		/*1356*/ usaFiltroTipoClienteRede = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAFILTROTIPOCLIENTEREDE));
		/*1358*/ usaFiltroPeriodoDataAgendaVisita = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROPERIODODATAAGENDAVISITA));
		/*1359*/ usaSenhaAdministracaoTabelas = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USASENHAADMINISTRACAOTABELAS));
		/*1360*/ sementeSenhaAdministracaoTabelas = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHAADMINISTRACAOTABELAS));
		/*1363*/ usaBloqueioEnvioPedidoProdutoRestrito = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USABLOQUEIOENVIOPEDIDOPRODUTORESTRITO);
		/*1364*/ usaRevalidacaoProdutoRestritosEnvioPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAREVALIDACAOPRODUTORESTRITOSENVIOPEDIDO));
		/*1365*/ geraFlSituacaoRestritoProdutoNoSync = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GERAFLSITUACAORESTRITOPRODUTONOSYNC));
		/*1366*/ usaLimpezaGradesNaoDisponiveisParaVenda = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALIMPEZAGRADESNAODISPONIVEISPARAVENDA);
		/*1368*/ usaFretePedidoPorToneladaCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFRETEPEDIDOPORTONELADACLIENTE));
		/*1372*/ apresentaNovidadesClienteRelatorioNovidadeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTANOVIDADESCLIENTERELATORIONOVIDADEPRODUTO));
		/*1373*/ naoPermiteFecharPedidoSemReservaDeEstoque = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOPERMITEFECHARPEDIDOOFFLINE);
		if (ValueUtil.VALOR_SIM.equals(naoPermiteFecharPedidoSemReservaDeEstoque)) {
			naoPermiteFecharPedidoSemReservaDeEstoque = "1";
		}
		/*1375*/ usaCondicaoNegociacaoNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONDICAONEGOCIACAONOPEDIDO));
		/*1376*/ usaApresentacaoProdutosPendentesRetirada = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAAPRESENTACAOPRODUTOSPENDENTESRETIRADA));
		/*1377*/ usaAvisoVendaProdutosPendentesRetirada = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAAVISOVENDAPRODUTOSPENDENTESRETIRADA);
		/*1378*/ usaValidacaoEstoqueLocalEstCondNegociacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALIDACAOESTOQUELOCALESTCONDNEGOCIACAO));
		/*1379*/ usaEstoqueInternoParcialmenteLocalEstoqueCondNeg = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAESTOQUEINTERNOPARCIALMENTELOCALESTOQUECONDNEG));
		/*1383*/ usaLiberacaoSenhaValorAbaixoMinimoVerba = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALIBERACAOSENHAVALORABAIXOMINIMOVERBA));
		/*1384*/ sementeSenhaValorAbaixoMinimoVerba = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAVALORABAIXOMINIMOVERBA));
		/*1385*/ configDescQuantidadeVlBasePorPesoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCQUANTIDADEVLBASEPORPESOPEDIDO);
		/*1386*/ usaSelecaoUnidadeAlternativaCapaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASELECAOUNIDADEALTERNATIVACAPAPEDIDO));
		/*1388*/ usaDescontosEmCascataManuaisNaCapaPedidoPorItem = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOSEMCASCATAMANUAISNACAPAPEDIDOPORITEM);
		/*1389*/ nuDiasCancelamentoAutoPedidoCliente = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUDIASCANCELAMENTOAUTOPEDIDOCLIENTE);
		/*1390*/ nuDiasCancelamentoAutoPedidoClienteKeyAccount = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUDIASCANCELAMENTOAUTOPEDIDOCLIENTEKEYACCOUNT);
		/*1391*/ naoConsideraProdutoDescPromocionalComoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOCONSIDERAPRODUTODESCPROMOCIONALCOMOPROMOCIONAL));
		/*1392*/ usaIndicacaoItemPedidoProdutoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICACAOITEMPEDIDOPRODUTOPROMOCIONAL));
		/*1393*/ usaAplicacaoMaiorDescontoEmCascata = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPLICACAOMAIORDESCONTOEMCASCATA)) && !aplicaSomenteMaiorDescontoPorItemFinalPedido && !isAplicaMaiorDescontoNoItemPedido();
		/*1394*/ usaInsercaoItensDiferentesLeituraCodigoBarras = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINSERCAOITENSDIFERENTESLEITURACODIGOBARRAS));
		/*1395*/ usaAgrupamentoPedidoEmConsignacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAGRUPAMENTOPEDIDOEMCONSIGNACAO));
		/*1398*/ usaInfoAdicionaisComprovanteNfe = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINFOADICIONAISCOMPROVANTENFE);
		/*1401*/ realizaCadastroAgendaVisitaAoReagendar = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.REALIZACADASTROAGENDAVISITAAOREAGENDAR));
		/*1405*/ usaRelacionamentoProdutosSAC = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELACIONAMENTOPRODUTOSSAC));
		/*1406*/ nuDiasPermanenciaLogSyncBackground = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIALOGSYNCBACKGROUND);
		/*1408*/ dsOpcoesFaltaDescAcrescPorUsuario = getValorParametroPorSistema(hashValorParametros, ValorParametro.DSOPCOESFALTADESCACRESCPORUSUARIO);
		/*1409*/ usaCalculoComissaoPorTabelaPrecoEGrupoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOCOMISSAOPORTABELAPRECOEGRUPOPRODUTO));
		/*1410*/ usaMultiplasSugestoesProdutoInicioPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMULTIPLASSUGESTOESPRODUTOINICIOPEDIDO);
		/*1411*/ usaConfirmacaoCPFCNPJIgualNovoCliente = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USACONFIRMACAOCPFCNPJIGUALNOVOCLIENTE);
		/*1414*/ usaIndiceGrupoProdutoTabPrecoCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICEGRUPOPRODUTOTABPRECOCONDPAGTO));
		/*1415*/ usaLimiteCreditoRedeCompartilhadoEmpresas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALIMITECREDITOCOMPARTILHADOENTREEMPRESAS));
		/*1417*/ configEscolhaTransportadoraNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGESCOLHATRANSPORTADORANOPEDIDO);    
		/*1418*/ usaDestaqueTransportadoraCIFNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESTAQUETRANSPORTADORACIFNALISTA));       
		/*1419*/ usaCalculoTransportadoraMaisRentavelPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOTRANSPORTADORAMAISRENTAVELPEDIDO));
		/*1420*/ usaTransportadoraPreferencialCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATRANSPORTADORAPREFERENCIALCLIENTE));
		/*1423*/ usaTipoPagamentoRestritoVenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATIPOPAGAMENTORESTRITOVENDA));
		/*1425*/ permiteAlterarValorItemComIPI = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARVALORITEMCOMIPI));
		/*1426*/ validaPeriodoEntregaParaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.VALIDAPERIODOENTREGAPARAPEDIDO));
		/*1427*/ usaConfirmacaoSubstituicaoCadastroAgendaVisita = getValorParametroPorSistema(hashValorParametros, ValorParametro.USACONFIRMACAOSUBSTITUICAOCADASTROAGENDAVISITA);
		/*1428*/ sementeSenhaConfirmacaoSubstituicaoCadastroAgendaVisita = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHACONFIRMACAOSUBSTITUICAOCADASTROAGENDAVISITA));
		/*1429*/ permiteCopiaDePedidoAberto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITECOPIADEPEDIDOABERTO));
		/*1430*/ usaDescQuantidadeApenasEmbalagemCompleta = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCQUANTIDADEAPENASEMBALAGEMCOMPLETA));
		/*1435*/ usaDescontoPorVolumeVendaMensal = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOPORVOLUMEVENDAMENSAL);
		/*1436*/ usaTransportadoraAuxiliar = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATRANSPORTADORAAUXILIAR));
		/*1437*/ configValorFreteManual = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGVALORFRETEMANUAL), ValorParametro.CONFIGVALORFRETEMANUAL);
		/*1439*/ usaGerenciaDeCreditoDesconto = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAGERENCIADECREDITODESCONTO));
		/*1440*/ vlPctAvisaCreditoDesconto = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VLPCTAVISACREDITODESCONTO));
		/*1441*/ naoAplicaIndiceClienteNoVlBaseVerba = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOAPLICAINDICECLIENTENOVLBASEVERBA));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAVALORBRUTOEDESCONTOCAPAPEDIDO);
		/*1443*/ mostraValorBruto = isAtributoJsonLigado(vlParam, "mostraValorBruto", ValorParametro.APRESENTAVALORBRUTOEDESCONTOCAPAPEDIDO);
		/*1443-2*/ mostraValorDesconto = isAtributoJsonLigado(vlParam, "mostraValorDesconto", ValorParametro.APRESENTAVALORBRUTOEDESCONTOCAPAPEDIDO);
		/*1444*/ usaQtMinProdutoPorTabelaPreco = getVlParamAsInt(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAQTMINPRODUTOPORTABELAPRECO));
		/*1445*/ usaVerbaPositivaApenasPedidoCorrente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBAPOSITIVAAPENASPEDIDOCORRENTE));
		/*1448*/ localApresentacaoRelGiroProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LOCALAPRESENTACAORELGIROPRODUTO);
		/*1449*/ grifaProdutoComVidaUtilLoteCriticoNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRIFAPRODUTOCOMVIDAUTILLOTECRITICONALISTA));
		/*1450*/ usaEntregaPedidoBaseadaEmCadastro = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAENTREGAPEDIDOBASEADAEMCADASTRO));
		/*1451*/ usaInsercaoQuantidadeDesejadaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINSERCAOQUANTIDADEDESEJADAPEDIDO));
		/*1452*/ usaQuantidadeMinimaDescPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAQUANTIDADEMINIMADESCPROMOCIONAL)) && aplicaDescontoNoProdutoPorGrupoDescPromocional();
		/*1453*/ usaFiltroApenasGruposProdutosExistentesCargaRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROAPENASGRUPOSPRODUTOSEXISTENTESCARGAREPRESENTANTES));
		/*1454*/ usaMultiploEspecialPorGradeProdutoPromocional = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMULTIPLOESPECIALPORGRADEPRODUTOPROMOCIONAL));
		/*1457*/ marcaNovoClienteParaAnalise = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MARCANOVOCLIENTEPARAANALISE));
		/*1458*/ nuDiasPermanenciaAnaliseNovoCliente = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAANALISENOVOCLIENTE);
		/*1459*/ usaOrdenacaoNuSequenciaGradeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDENACAONUSEQUENCIAGRADEPRODUTO)) && !usaOrdenacaoEstoqueNaGradeProduto;
		/*1460*/ usaFocoCampoBuscaAutomaticamente = VmUtil.isSimulador() && ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFOCOCAMPOBUSCAAUTOMATICAMENTE));
		configuraTecladoVirtual();
		/*1461*/ usaTeclaEnterComoConfirmacaoItemPedido = VmUtil.isSimulador() && ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATECLAENTERCOMOCONFIRMACAOITEMPEDIDO));
		/*1462*/ configGeracaoPdfPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGGERACAOPDFPEDIDO);
		/*1463*/ usaPreenchimentoCamposNovoClientePorCnpj = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPREENCHIMENTOCAMPOSNOVOCLIENTEPORCNPJ);
		/*1467*/ apresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTATOTALEMBALAGEMVENDIDANOPEDIDOELISTAITEMPEDIDO));
		/*1468*/ mostraFotoProdutoNaListaProdutos = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAFOTOPRODUTONALISTAPRODUTOS));
		/*1469*/ mostraEstoquePrevistoNaListaProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAESTOQUEPREVISTONALISTAPRODUTOS);
		/*1470*/ usaImpressaoNotaPromissoriaViaBluetooth = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAONOTAPROMISSORIAVIABLUETOOTH));
		/*1472*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAQTDEMINIMAPRODUTOPORCONDPAGAMENTO);
		         usaQtdeMinimaProdutoPorCondPagamento = getValorAtributoJson(vlParam, "usa", ValorParametro.USAQTDEMINIMAPRODUTOPORCONDPAGAMENTO);
				 formaValidacao = getValorAtributoJson(vlParam, "formaValidacao", ValorParametro.USAQTDEMINIMAPRODUTOPORCONDPAGAMENTO);
		/*1475*/ sementeSenhaLiberacaoRentabilidadeMinima = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHALIBERACAORENTABILIDADEMINIMA));
		/*1476*/ usaImpressaoNfceViaBluetooth = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAONFCEVIABLUETOOTH);
		/*1477*/ configMargemContribuicao = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGMARGEMCONTRIBUICAO);
		/*1478*/ permiteItemPedidoComQuantidadeZero = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEITEMPEDIDOCOMQUANTIDADEZERO)) && usaInsercaoQuantidadeDesejadaPedido;
		/*1480*/ usaBotaoIgnorarValidacoesPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USABOTAOIGNORARVALIDACOESPEDIDO));
		/*1487*/ ignoraValidacaoValorMinPedidoConformeProdutoVendido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.IGNORAVALIDACAOVALORMINPEDIDOCONFORMEPRODUTOVENDIDO));
		/*1490*/ usaBotaoGiroProdutoItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USABOTAOGIROPRODUTOITEMPEDIDO));
		/*1493*/ usaDescontosCategoriaClienteEmCascataManuaisNaCapaPorPedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOSCATEGORIACLIENTEEMCASCATAMANUAISNACAPAPORPEDIDO));
		/*1496*/ usaValidacaoMinimosCondPagtoPorCondPagtoTabPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALIDACAOMINIMOSCONDPAGTOTABPRECO));
		/*1497*/ usaTabelaPrecoComVigencia = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USATABELAPRECOCOMVIGENCIA));
		/*1498*/ usaCondPagtoTabPrecoComVigencia = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACONDPAGTOTABPRECOCOMVIGENCIA));
		/*1499*/ mostraColunaMarcaNaListaDeProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRACOLUNAMARCANALISTADEPRODUTOS);
		/*1500*/ usaCamposAdicionaisImpressaoLayoutNfe = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACAMPOSADICIONAISIMPRESSAOLAYOUTNFE);
		/*1501*/ apresentaListaSociosClienteNosDetalhes = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTALISTASOCIOSCLIENTENOSDETALHES));
		/*1502*/ exibeCidadeEstadoNoMenuCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBECIDADEESTADONOMENUCLIENTE));
		/*1503*/ usaCalculoGastoTotalVariavelPorProduto = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOGASTOTOTALVARIAVELPORPRODUTO));
		/*1504*/ ocultaCamposDescontosImpressaoContingenciaNfe = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTACAMPOSDESCONTOSIMPRESSAOCONTINGENCIANFE));
		/*1505*/ usaProdutoSimilarProdutoInexistente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRODUTOSIMILARPRODUTOINEXISTENTE)); 
		/*1506*/ usaAcessoDiretoGradeProdutosItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAACESSODIRETOGRADEPRODUTOSITEMPEDIDO)) && (usaGradeProduto2() || usaGradeProduto5());
		/*1507*/ usaAcessoItemInseridoModoEdicao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAACESSOITEMINSERIDOMODOEDICAO)) && !permiteIncluirMesmoProdutoNoPedido;
		/*1508*/ usaFiltroGrupoProdutoListaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAFILTROGRUPOPRODUTOLISTAITEMPEDIDO));
		/*1492*/ usaIndiceFinanceiroTipoPagamentoPagamentoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICEFINANCEIROTIPOPAGAMENTOPAGAMENTOPEDIDO));
		/*1509*/ validaSaldoPedidoBonificacao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDASALDOPEDIDOBONIFICACAO));
		/*1510*/ liberaComSenhaSaldoBonificacaoExtrapolado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHASALDOBONIFICACAOEXTRAPOLADO));
		/*1511*/ sementeSenhaSaldoBonificacaoExtrapolado = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.SEMENTESENHASALDOBONIFICACAOEXTRAPOLADO));
		/*1512*/ consomePrecoMinimoItemBonificado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSOMEPRECOMINIMOITEMBONIFICADO)); 
		permiteDescCascataCategoria = isPermiteDescCascataCategoria();
		/*1518*/ setUsaApresentacaoFixaTicketMedioPedidosCliente(hashValorParametros);
		/*1519*/ nuCamposExibidosSemQuantidadeFracionada = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUCAMPOSEXIBIDOSSEMQUANTIDADEFRACIONADA);
		/*1523*/ usaSugestaoVendaPersonalizavelInicioPedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOVENDAPERSONALIZAVELINICIOPEDIDO));
		/*1524*/ dsInformacoesComplementaresListaSugestaoVendaPerson = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DSINFORMACOESCOMPLEMENTARESLISTASUGESTAOVENDAPERSON);
		/*1525*/ dsOrdenacaoPadraoListaSugestaoVendaPerson = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DSORDENACAOPADRAOLISTASUGESTAOVENDAPERSON);
		String[] dsOrdenacaoItens = dsOrdenacaoPadraoListaSugestaoVendaPerson.split(";");
		dsOrdenacaoListaSugPerson = dsOrdenacaoItens[0];
		if (dsOrdenacaoItens.length > 1) {
			sentidoOrdenacaoListaSugPerson = dsOrdenacaoItens[1];
		}
		ajustaOrdemSugPerson();
		/*1526*/ qtLimiteProdutosExibidosListaSugestaoVendaPerson = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTLIMITEPRODUTOSEXIBIDOSLISTASUGESTAOVENDAPERSON);
		setQtLimiteProdutosSugPerson();
		/*1527*/ qtLimiteSugestaoVendaPersExibidasNaTela = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.QTLIMITESUGESTAOVENDAPERSEXIBIDASNATELA));
		qtLimiteSugestaoVendaPersExibidasNaTela = qtLimiteSugestaoVendaPersExibidasNaTela < 1 ? 3 : qtLimiteSugestaoVendaPersExibidasNaTela;
		/*1531*/ usaGeracaoNfePedidoAposFechamento = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGERACAONFEPEDIDOAPOSFECHAMENTO));
		/*1532*/ usaControleEstoquePorRemessa = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACONTROLEESTOQUEPORREMESSA));
		/*1533*/ usaInfoAdicionalImpressaoNfeVendaCigarros = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINFOADICIONALIMPRESSAONFEVENDACIGARROS));
		/*1534*/ naoAplicaIndiceUnidadeAlternativaValorBaseRentabilidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOAPLICAINDICEUNIDADEALTERNATIVAVALORBASERENTABILIDADE));
		/*1535*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMULTIPLOSPAGAMENTOSIMPRESSAONFE);
		apresentaMultiplosPagamentosImpressaoNfe =isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGMULTIPLOSPAGAMENTOSIMPRESSAONFE);
		origemDescricaoPagamento = getValorAtributoJson(vlParam, "origemDescricaoPagamento", ValorParametro.CONFIGMULTIPLOSPAGAMENTOSIMPRESSAONFE);
		/*1536*/ nuDiasPermanenciaRemessaEstoque = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIAREMESSAESTOQUE));
		if (nuDiasPermanenciaRemessaEstoque == 0) {
			nuDiasPermanenciaRemessaEstoque = 180;
		}
		/*1538*/ sementeSenhaControleDataHoraNoAcessoAoSistema = getVlParamAsInt(getValorParametroPorSistema(ValorParametro.SEMENTESENHACONTROLEDATAHORANOACESSOAOSISTEMA));
		/*1540*/ apresentaSecaoTransportadoraImpressaoNfe = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTASECAOTRANSPORTADORAIMPRESSAONFE));
		/*1541*/ listaChaveAcessoNotasRemessaImpressaNfe = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LISTACHAVEACESSONOTASREMESSAIMPRESSANFE));
		/*1542*/ usaGeracaoTxtNfe = !ValueUtil.VALOR_NAO.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DIRDESTINOARQUIVOSGERADOSNFE));
		/*1543*/ filtraGrupoProdutoPorFornecedor = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRAGRUPOPRODUTOPORFORNECEDOR));
		/*1544*/ exibeHistoricoEstoqueCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBEHISTORICOESTOQUECLIENTE));
		/*1545*/ exibeHistoricoQtdeVendaDoItem = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBEHISTORICOQTDEVENDADOITEM));
		/*1548*/ exibeOpcoesNavegacaoNasCombos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBEOPCOESNAVEGACAONASCOMBOS));
		/*1549*/ calculaRentabilidadePedidoRetornado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULARENTABILIDADEPEDIDORETORNADO));
		/*1551*/ nuPeriodoDiasClienteRecenteNaBase = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUPERIODODIASCLIENTERECENTENABASE));
		/*1552*/ configUsoMarcadores = getValorAtributoJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGUSOMARCADORES), "usaMarcadorCliente", ValorParametro.CONFIGUSOMARCADORES);
		localUsoMarcadoresProduto = getValorAtributoJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGUSOMARCADORES), "usaMarcadorProduto", ValorParametro.CONFIGUSOMARCADORES);
		setApresentaIndicadores(Cliente.APRESENTA_LISTACLI);
		setApresentaIndicadores(Cliente.APRESENTA_LISTAGENDA);
		setApresentaIndicadores(ClienteChurn.APRESENTA_INDICADOR);
		setApresentaMarcadoresProduto();
		/*1554*/ utilizaApenasControleDeDataAcessoSistema = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.UTILIZAAPENASCONTROLEDEDATAACESSOSISTEMA));
		/*1559*/ usaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALIDACAOCLIENTEATRASADOPORVALORTOTALTITULOSEMATRASO);
		/*1560*/ validaClienteAtrasadoPorValorTotalTitulosEmAtrasoApenasAoFecharPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDACLIENTEATRASADOPORVALORTOTALTITULOSEMATRASOAPENASAOFECHARPEDIDO));
		/*1562*/ nuDiasPermanenciaRegistroValorizacaoProd = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAREGISTROVALORIZACAOPROD));
		/*1563*/ usaFechamentoDeVendasPorPeriodo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFECHAMENTODEVENDASPORPERIODO));
		if (nuDiasPermanenciaResumoDia == 1 && usaFechamentoDeVendasPorPeriodo) {
			nuDiasPermanenciaResumoDia = 180;
		}
		/*1565*/ usaGeracaoNotaNfeContingenciaPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGERACAONOTANFECONTINGENCIAPEDIDO);
		/*1566*/ usaFiltroTituloFinanceiroPorTipoPagamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROTITULOFINANCEIROPORTIPOPAGAMENTO));
		/*1567*/ permiteEditarPrimeiroDescontoEmCascataManualNaCapaDoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEEDITARPRIMEIRODESCONTOEMCASCATAMANUALNACAPADOPEDIDO));
		/*1568*/ usaImpressaoReciboPagadorBoleto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAORECIBOSACADOBOLETO));
		/*1569*/ dsLinhasTextoResponsabilidadeCedenteBoletoContingencia = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DSLINHASTEXTORESPONSABILIDADECEDENTEBOLETOCONTINGENCIA);
		/*1570*/ calculaPrazoEntregaPorProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAPRAZOENTREGAPORPRODUTO));
		/*1571*/ usaApenasGiroProdutoPorTabelaPrecoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPENASGIROPRODUTOPORTABELAPRECOPEDIDO));
		/*1573*/ usaModoControleEstoquePorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMODOCONTROLEESTOQUEPORTIPOPEDIDO));
		/*1574*/ configPedidoAbertoComIndicacaoOrcamento = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPEDIDOABERTOCOMINDICACAOORCAMENTO);
		/*1574-1*/ usaPedidoAbertoComIndicacaoOrcamento = isAtributoJsonLigado(configPedidoAbertoComIndicacaoOrcamento, "usa", ValorParametro.CONFIGPEDIDOABERTOCOMINDICACAOORCAMENTO);
		/*1574-2*/ enviaPedidoEmOrcamentoParaSupervisor = isAtributoJsonLigado(configPedidoAbertoComIndicacaoOrcamento, "enviaPedidoEmOrcamentoParaSupervisor", ValorParametro.CONFIGPEDIDOABERTOCOMINDICACAOORCAMENTO);
		/*1574-3*/ modoSugestaoEnvioPedidoEmOrcamento = getVlParamAsInt(getValorAtributoJson(configPedidoAbertoComIndicacaoOrcamento, "modoSugestaoEnvioPedidoEmOrcamento", ValorParametro.CONFIGPEDIDOABERTOCOMINDICACAOORCAMENTO));
		/*1574-4*/ permiteInserirEmailAlternativoPedEmOrcamento = isAtributoJsonLigado(configPedidoAbertoComIndicacaoOrcamento, "permiteEmailAlternativoPedidoEmOrcamento", ValorParametro.CONFIGPEDIDOABERTOCOMINDICACAOORCAMENTO);
		/*1575*/ usaLinhaSeparadoraItensImpressao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALINHASEPARADORAITENSIMPRESSAO));
		/*1576*/ usaCamposAdicionaisImpressaoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACAMPOSADICIONAISIMPRESSAOPEDIDO);
		/*1577*/ grifaMetaNaoAtingida = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRIFAMETANAOATINGIDA));
		/*1578*/ consideraTotalRealizadoParaCalculoMeta = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONSIDERATOTALREALIZADOPARACALCULOMETA));
		isUsaCamposAdicionaisImpressaoPedido = isOrdemCamposValida(usaCamposAdicionaisImpressaoPedido);
		/*1580*/ apresentaInformacoesAdicionaisRelatorioMetaVendaCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAINFORMACOESADICIONAISRELATORIOMETAVENDACLIENTE));
		/*1587*/ usaIndicacaoCNPJTransportadoraFreteFOB = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICACAOCNPJTRANSPORTADORAFRETEFOB));
		/*1588*/ usaVerificacaoFotoPedidoPresentePedido = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERIFICACAOFOTOPEDIDOPRESENTEPEDIDO));
		/*1586*/ aplicaDescontosSequenciaisNoItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTOSSEQUENCIAISNOITEMPEDIDO);
		/*1590*/ nuDiasPermanenciaRegistroNovoCliente = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIAREGISTRONOVOCLIENTE));
		/*1591*/ configuracoesIgnoradasReplicacaoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGURACOESIGNORADASREPLICACAOPEDIDO);
		/*1592*/ usaConfigFechamentoDiarioVendas = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
		/*1593*/ usaImpressaoFechamentoDiarioVendas = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOFECHAMENTODIARIOVENDAS);
		/*1594*/ dsDadosAdicionaisImpressaoFechamentoDiarioVendas = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DSDADOSADICIONAISIMPRESSAOFECHAMENTODIARIOVENDAS);
		/*1596*/ nuDiasPermanenciaFechamentoDiario = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIAFECHAMENTODIARIO);
		/*1598*/ usaImpressaoFinanceiroFechamentoDiario = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOFINANCEIROFECHAMENTODIARIO);
		/*1595*/ nuDiasPermanenciaRegistroCargaProduto = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASPERMANENCIAREGISTROCARGAPRODUTO);
		/*1599*/ usaHistoricoVendasPorListaColunaTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USAHISTORICOVENDASPORLISTACOLUNATABELAPRECO));
		/*1601*/ ocultaRentabilidadePedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTARENTABILIDADECAPAPEDIDO);
		/*1602*/ configSugestaoVendaBaseadaEmComboProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGSUGESTAOVENDABASEADAEMCOMBOPRODUTOS);
		/*1604*/ usaCondicaoPagamentoRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACONDICAOPAGAMENTOPORREPRESENTANTE));
		if (!(isUsaKitTipo3() && isExibeComboMenuInferior())) {
			try {
				configKitProduto.put("usa", (isUsaSugestaoVendaBaseadaEmComboProdutos()) ? ValueUtil.VALOR_NAO : getValorAtributoJson(configKitProduto, "usa", ValorParametro.CONFIGKITPRODUTO));
			} catch(JSONException e) {
				ExceptionUtil.handle(e);
			}
		}
		/*1610*/ usaGeracaoRemessaEstoqueNoEquipamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAGERACAOREMESSAESTOQUENOEQUIPAMENTO));
		/*1611*/ nuDiasExpiracaoSenha = getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASEXPIRACAOSENHA);
		/*1612*/ nuRegistrosControleRepeticaoSenhaUsuario = ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUREGISTROSCONTROLEREPETICAOSENHAUSUARIO));
		/*1613*/ usaListaRestricaoSenhaUsuario = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USALISTARESTRICAOSENHAUSUARIO));
		/*1614*/ mensagemExpressaoRegularValidacaoSenhaUsuario = getValorParametroPorSistema(hashValorParametros, ValorParametro.MENSAGEMEXPRESSAOREGULARVALIDACAOSENHAUSUARIO);
		/*1615*/ filtraItensComErroNaInsercaoMultiplaItens = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTRAITENSCOMERRONAINSERCAOMULTIPLAITENS);
		/*1616*/ emiteBeepDeErro = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EMITEBEEPDEERRO));
		/*1617*/ aplicaTaxaAntecipacaoNoItem = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICATAXAANTECIPACAONOITEM);
		/*1619*/ permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.PERMITEINDICARDATAENTREGAMANUALQUANDOUSACADASTROENTREGA)) && previsaoEntregaOcultaNoPedido && usaEntregaPedidoBaseadaEmCadastro;
		/*1600*/ usaNovidadeNovoClienteNaoIntegrado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USANOVIDADENOVOCLIENTENAOINTEGRADO));
		/*1608*/ geraNovoPedidoDiferencas = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GERANOVOPEDIDODIFERENCAS));
		/*1626*/ configModoResumoDiario = getValorParametroPorEmpresa(ValorParametro.CONFIGMODORESUMODIARIO);
		/*1629*/ aplicarDescontosIndicesParaSaldoVerbaNegativo = getValorParametroPorEmpresa(ValorParametro.APLICARDESCONTOSINDICESPARASALDOFLEXNEGATIVO);
		/*1622*/ usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USALISTAMULTIPLAINSERCAOITENSNOPEDIDOPORGIROPRODUTO));
		/*1628*/ valorTimeOutRetornoDadosRelativosRemessaEstoque = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORTIMEOUTRETORNODADOSRELATIVOSREMESSAESTOQUE));
		valorTimeOutRetornoDadosRelativosRemessaEstoque = valorTimeOutRetornoDadosRelativosRemessaEstoque <= 0 ? 20 : valorTimeOutRetornoDadosRelativosRemessaEstoque;
		/*1625*/ aplicaDescontoNaCapaDoPedido = ValueUtil.getDoubleValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTONOPEDIDOCONSUMINDOVERBA));
		if (aplicaDescontoNaCapaDoPedido > 100) {
			aplicaDescontoNaCapaDoPedido = 100;
		}
		/*1630*/ usaDevolucaoRemessaEstoqueNoEquipamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADEVOLUCAOREMESSAESTOQUENOEQUIPAMENTO));
		/*1634*/ loadVlParamApresentaPedidosComErroEnvio(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAPEDIDOSCOMERRONOENVIO));
		/*1633*/ nuOrdemLiberacaoPedidoPendente = getValorParametroPorSistema(ValorParametro.USAMULTIPLASLIBERACOESPARAPEDIDOPENDENTE); 
		/*1638*/ liberaPedidoPendenteComSenhaPorDescontoMaximo = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERAPEDIDOPENDENTECOMSENHAPORDESCONTOMAXIMO));
		/*1639*/ sementeSenhaDescontoMaximoPedidoPendente = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHADESCONTOMAXIMOPEDIDOPENDENTE));
		/*1641*/ usaVlBaseVerbaEDescMaximoPorRedutorCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVLBASEVERBAEDESCMAXIMOPORREDUTORCLIENTE));
		/*1635*/ apresentaEstoqueDaRemessaEmpresaNaListaProdutos = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAESTOQUEDAREMESSAEMPRESANALISTAPRODUTOS));
		/*1646*/ usaFotoProdutoPorEmpresa = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFOTOPRODUTOPOREMPRESA));
		/*1647*/ habilitaEquipamentoParaUsoPorPadrao = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.HABILITAEQUIPAMENTOPARAUSOPORPADRAO));
		/*1597*/ nuDiasObrigaFechamentoVendas = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASOBRIGAFECHAMENTOVENDAS));
		/*1648*/ permiteRentabilidadeNegativaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITERENTABILIDADENEGATIVAPEDIDO));
		/*1649*/ padronizaListaContatoEAniversariante = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PADRONIZALISTACONTATOEANIVERSARIANTE));
		/*1650*/ controlaExibicaoComboTipoPedidoPorPreferenciaFuncao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLAEXIBICAOCOMBOTIPOPEDIDOPORPREFERENCIAFUNCAO));
		/*1642*/ configControleEstoquePorLoteProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCONTROLEESTOQUEPORLOTEPRODUTO);
		/*1654*/ ComboBoxTc.qtItensShowEditFilter = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUREGISTROSHABILITAPESQUISANACOMBO));
		/*1656*/ usaFiltroComissao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROCOMISSAO));
		/*1657*/ usaEnvioRecadoPedido = getValorParametroPorEmpresa(ValorParametro.USAENVIORECADOPEDIDO);
		/*1659*/ aplicaConversaoUnidadeNaValidacaoQtMaxVenda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICACONVERSAOUNIDADENAVALIDACAOQTMAXVENDA));
		/*1660*/ apresentaUnidadeFracionadaDoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAUNIDADEFRACIONADADOPRODUTO));
		/*1661*/ permiteIncluirMesmoProdutoLoteDiferenteNoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEINCLUIRMESMOPRODUTOLOTEDIFERENTENOPEDIDO));
		/*1668*/ apresentaSaldoVerbaPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTASALDOVERBAPEDIDO);
		/*1605*/ usaDevolucaoTotalEstoqueRemessaRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADEVOLUCAOTOTALESTOQUEREMESSAREPRESENTANTE));
		/*1606*/ usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAODEVOLUCAOTOTALESTOQUEREMESSAREPRESENTANTE);
		/*1607*/ nuDiasPermanenciaDadosEstoqueRep = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIASPERMANENCIADADOSESTOQUEREP);
		/*1589*/ usaCancelamentoNfePedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACANCELAMENTONFEPEDIDO);
		/*1609*/ exibeGrupoProdutoListaMaximizada = ValueUtil.getBooleanValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.EXIBEGRUPOPRODUTOLISTAMAXIMIZADA));
		/*1637*/ permiteAlterarVencimentoConformeCondPagto = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARVENCIMENTOCONFORMECONDPAGTO));
		/*1669*/ usaSelecaoUnidadePorGrid = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASELECAOUNIDADEPORGRID));
		/*1672*/ permiteIndicarRecebimentoSMSNoCadastroCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEINDICARRECEBIMENTOSMSNOCADASTROCLIENTE));
		/*1671*/ usaDicionarioCorrecaoNoFiltroDeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADICIONARIOCORRECAONOFILTRODEPRODUTO));
		/*1673*/ ocultaProdutoSemEstoqueListaSugestaoVendaPerson = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAPRODUTOSEMESTOQUELISTASUGESTAOVENDAPERSON));
		/*1674*/ usaRelatorioMetaVendaCliente = ValueUtil.getBooleanValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USARELATORIOMETAVENDACLIENTE));
		/*1675*/ EditDate.permiteDigitacaoCampoData = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEDIGITACAOCAMPODATA));
		/*1676*/ usaValidacaoCPFCNPJBaseWeb = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALIDACAOCPFCNPJBASEWEB));
		/*1677*/ usaImpressaoInventarioRemessaEstoque = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOMOVIMESALDOFISICOPRODUTOSREMESSAESTOQUE));
		if (ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOMOVIMESALDOFISICOPRODUTOSREMESSAESTOQUE))) {
			usaImpressaoPedidoViaBluetooth = 1;
		}
		/*1678*/ vlTimeOutValidacaoCPFCNPJDuplicado = getVlParamAsInt(getValorParametroPorSistema(hashValorParametros, ValorParametro.VLTIMEOUTVALIDACAOCPFCNPJDUPLICADO));
		vlTimeOutValidacaoCPFCNPJDuplicado = vlTimeOutValidacaoCPFCNPJDuplicado <= 0 ? 5 : vlTimeOutValidacaoCPFCNPJDuplicado;
		/*1681*/ MPTPrinter.setPrinterDensity(ValueUtil.getIntegerValue(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDENSIDADEIMPRESSORATERMICA)));
		/*1683*/ somaValorSTNoValorTotalNfe = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SOMAVALORSTNOVALORTOTALNFE);
		/*1684*/ apresentaPrecoCondComercialCli = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAPRECOCONDCOMERCIALCLI));
		/*1688*/ usaImpressaoModuloPagamento = getVlParametro(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMPRESSAOMODULOPAGAMENTO), ":");
		/*1686*/ usaInfoComplementarItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		/*1687*/ aplicaDescontoNoItemDeAcordoComICMSdoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APLICADESCONTONOITEMDEACORDOCOMICMSDOCLIENTE));
		/*1685*/ configCatalogoProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCATALOGOPRODUTO);
		/*1690*/ controlaDescontoUsandoVerbaAtravesQtMinItens = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLADESCONTOUSANDOVERBAATRAVESQTMINITENS));
		/*1691*/ configEstoquePrevisto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGESTOQUEPREVISTO);
		/*1693*/ atualizaChaveNfeContingencia = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ATUALIZACHAVENFECONTINGENCIA));
		/*1694*/ validaRegraProdutoRestritoEPromocionalSeparadamente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDAREGRAPRODUTORESTRITOEPROMOCIONALSEPARADAMENTE));
		/*1698*/ bloqueiaCondPagtoPorCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEIACONDPAGTOPORCLIENTE);
		if (ValueUtil.VALOR_SIM.equals(bloqueiaCondPagtoPorCliente)) {
			bloqueiaCondPagtoPorCliente = "1";
		}
		/*1699*/ naoConsomeVerbaAutomaticamenteAoFecharPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOCONSOMEVERBAAUTOMATICAMENTEAOFECHARPEDIDO));
		/*1700*/ liberaComSenhaPedidoComSaldoVerbaExtrapolado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAPEDIDOCOMSALDOVERBAEXTRAPOLADO));
		/*1701*/ sementeSenhaPedidoComSaldoVerbaExtrapolado = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAPEDIDOCOMSALDOVERBAEXTRAPOLADO));
		/*1702*/ permiteAcessoAoMenuProdutoAtravesRelNovidade =  ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEACESSOAOMENUPRODUTOATRAVESRELNOVIDADE));
		/*1703*/ usaVencimentosAdicionaisBoleto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVENCIMENTOSADICIONAISBOLETO));
		/*1704*/ usaOrdenacaoVerbaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAORDENACAOVERBAITEMPEDIDO));
		/*1706*/ apresentaConsumoVerbaDePedidoNaoTransmitido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTACONSUMOVERBADEPEDIDONAOTRANSMITIDO));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLOCALLOTEPRODUTO);
		/*1707*/ usaDescPromo = isAtributoJsonLigado(vlParam, "usaDescPromo", ValorParametro.CONFIGLOCALLOTEPRODUTO);
		ignoraLocalSemDescPro = isAtributoJsonLigado(vlParam, "ignoraLocalSemDescPro", ValorParametro.CONFIGLOCALLOTEPRODUTO);
		restringeItemPedidoPorLocal = isAtributoJsonLigado(vlParam, "restringeItemPedidoPorLocal", ValorParametro.CONFIGLOCALLOTEPRODUTO);
		exibeOpcaoTodosFiltroLocal = isAtributoJsonLigado(vlParam, "exibeOpcaoTodosFiltroLocal", ValorParametro.CONFIGLOCALLOTEPRODUTO);
		/*1708*/ mostraListaEstoqueGradeFaltante = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRALISTAESTOQUEGRADEFALTANTE);		
		/*1709*/ modoDeIdentificarSemanaDoMesNaAgendaVisita = getVlParamAsInt(getValorParametroPorSistema(hashValorParametros, ValorParametro.MODODEIDENTIFICARSEMANADOMESNAAGENDAVISITA));
		/*1710*/ localApresentacaoMultiplasSugestoesProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LOCALAPRESENTACAOMULTIPLASSUGESTOESPRODUTO); 
		/*1711*/ configOrigemDadosEscolhaTransportadoraNoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.ORIGEMDADOSESCOLHATRANSPORTADORANOPEDIDO);	
		/*1712*/ usaFiltroGradeProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROGRADEPRODUTO);
		/*1713*/ restauraBackupAutoAposAtualizarVersao = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.RESTAURABACKUPAUTOAPOSATUALIZARVERSAO));
		/*1714*/ nivelLogSyncBackground = loadNivelLogSyncBackground(hashValorParametros);
		/*1715*/ usaDescMaxProdCli = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCMAXPRODCLI));
		/*1716*/ naoCalculaDadosResumoDiaPda = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NAOCALCULADADOSRESUMODIAPDA));
		/*1717*/ usaFiltroAplicacaoDoProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROAPLICACAODOPRODUTO);
		/*1718*/ usaFiltroCodigoAlternativoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROCODIGOALTERNATIVOPRODUTO));
		/*1720*/ usaQuebraLinhaDescricaoProdutoNaLista = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAQUEBRALINHADESCRICAOPRODUTONALISTA));
		/*1721*/ usaColetaInfoAdicionaisEscolhaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACOLETAINFOADICIONAISESCOLHAITEMPEDIDO));
		/*1722*/ mantemPrecoNegociadoReplicacaoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MANTEMPRECONEGOCIADOREPLICACAOPEDIDO));
		/*1723*/ configFiltroListaProdutos = getValorParametroPorRepresentante(ValorParametro.CONFIGFILTROLISTAPRODUTOS);
		/*1724*/ usaIndiceClienteGrupoProd = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINDICECLIENTEGRUPOPROD));
		/*1725*/ loadParamRecalculoValoresDosPedidos();
		/*1727*/ validaQtMaxVendaPorGrade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALIDAQTMAXVENDAPORGRADE));
		/*1728*/ usaDescontoMultiplosPagamentosParaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOMULTIPLOSPAGAMENTOSPARAPEDIDO));
		/*1729*/ permiteAlternarEmpresaDuranteCadastroPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERNAREMPRESADURANTECADASTROPEDIDO));
		/*1731*/ usaCorCadastroMarcador = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACORCADASTROMARCADOR));	
		/*1738*/ usaVolumeVendaMensalRede = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVOLUMEVENDAMENSALREDE));
		/*1739*/ configColunasGridDescontoVolumeVendasMensal = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCOLUNASGRIDDESCONTOVOLUMEVENDASMENSAL);
		/*1740*/ mostraVolumeVendasMensalNoDescontoQuantidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRAVOLUMEVENDASMENSALNODESCONTOQUANTIDADE));
		/*1741*/ configColunasGridDescontoQuantidade = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCOLUNASGRIDDESCONTOQUANTIDADE);
		/*1743*/ permiteAlterarVencimentoConformeCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEALTERARVENCIMENTOCONFORMECLIENTE));
		/*1744*/ habilitaAtualizacaoCadastroCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.HABILITAATUALIZACAOCADASTROCLIENTE));
		/*1745*/ nuDiasAtualizaCadastroCliente = getVlParamAsInt(getValorParametroPorSistema(hashValorParametros, ValorParametro.NUDIASATUALIZACADASTROCLIENTE));
		/*1746*/ usaCalculoVpcItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACALCULOVPCITEMPEDIDO);
		/*1747*/ configCalculoComissao = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULOCOMISSAO), ValorParametro.CONFIGCALCULOCOMISSAO);
		loadNuCasasDecimaisComissao();
		/*1748*/ configCalculoComissaoPorFaixaDesconto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULOCOMISSAOPORFAIXADESCONTO);
		/*1749*/ liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHAPEDIDOBONIFICACAOCOMSALDOVERBAEXTRAPOLADO));
		/*1736*/ configColunasGridLoteProduto = returnValueConfigColunasGridLoteProduto(hashValorParametros);
		/*1750*/ sementeSenhaPedidoBonificacaoComSaldoVerbaExtrapolado = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.SEMENTESENHAPEDIDOBONIFICACAOCOMSALDOVERBAEXTRAPOLADO));
		/*1751*/ usaProdutoRestrito = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRODUTORESTRITO));
		/*1752*/ informaUfNaoPermiteInscEstadualVazio = getValorParametroPorSistema(hashValorParametros, ValorParametro.INFORMAUFNAOPERMITEINSCESTADUALVAZIO);
		/*1753*/ usaValorRetornoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORRETORNOPRODUTO));
		/*1754*/ nuDiaClienteAbertura = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIACLIENTEABERTURA));
		/*1755*/ nuDiaClienteFundacao = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NUDIACLIENTEFUNDACAO));
		/*1759*/ usaTabelaPrecoPorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORTIPOPEDIDO));
		/*1756*/ apresentaValorTotalPedidoComTributosEFrete = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAVALORTOTALPEDIDOCOMTRIBUTOSEFRETE));
		/*1759*/ usaTabelaPrecoPorTipoPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORTIPOPEDIDO));
		/*1760*/ descontaIcmsRentabilidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESCONTAICMSRENTABILIDADE));
		/*1761*/ usaVerbaSaldoOnline = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBASALDOONLINE));
		/*1762*/ apresentaListaClienteRedeTituloFinanceiro = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTALISTACLIENTEREDETITULOFINANCEIRO));
		/*1763*/ usaFreteValorUnidade = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFRETEVALORUNIDADE));
		/*1764*/ usaInterpolacaoPrecoProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USAINTERPOLACAOPRECOPRODUTO));
		/*1766*/ configPersonalizadaParaDiferencasPedido = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGPERSONALIZADAPARADIFERENCASPEDIDO);
		/*1768*/ configNotaCreditoParaPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGNOTACREDITOPARAPEDIDO);
		/*1771*/ ocultaValorFretePedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTAVALORFRETEPEDIDO));
		/*1778*/ usaCriacaoPedidoErpCancelado = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.USACRIACAOPEDIDOERPCANCELADO));
		/*1769*/ marcaPedidoPendenteBaseadoLimiteCredito = getValorParametroPorSistema(hashValorParametros, ValorParametro.MARCAPEDIDOPENDENTEBASEADOLIMITECREDITO);
		/*1772*/ controlaDescontoUsandoVerbaAtravesMixItens = ValueUtil.getIntegerValue(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONTROLADESCONTOUSANDOVERBAATRAVESMIXITENS));
		/*1773*/ cdStatusPedidoPendenteAprovacaoLimCredito = getValorParametroPorSistema(hashValorParametros, ValorParametro.CDSTATUSPEDIDOPENDENTEAPROVACAOLIMCREDITO);
		/*1774*/ configDescPromocionalRegraInterpolacaoPoliticaDesconto = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCPROMOCIONALREGRAINTERPOLACAOPOLITICADESCONTO), ValorParametro.USADESCPROMOCIONALREGRAINTERPOLACAOPOLITICADESCONTO);
		/*1776*/ enviaProtocoloPedidosEmailHtml = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(ValorParametro.ENVIAPROTOCOLOPEDIDOSEMAILHTML));
		/*1777*/ gravaFotosGaleriaEquipamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.GRAVAFOTOSGALERIAEQUIPAMENTO));
		/*1779*/ exibeFracaoEmbalagemFornecedorItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.EXIBEFRACAOEMBALAGEMFORNECEDORITEMPEDIDO));
		/*1775*/ marcaClienteOcultoReplicaoNovoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MARCACLIENTEOCULTOREPLICAONOVOCLIENTE));
		/*1780*/ usaTabelaPrecoPorCanalAtendimento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USATABELAPRECOPORCANALATENDIMENTO));
		/*1781*/ calculaPrecoPorMetroQuadradoUnidadeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(ValorParametro.CALCULAPRECOPORMETROQUADRADOUNIDADEPRODUTO));
		/*1782*/ filtrosFixoTelaListaCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.FILTROSFIXOTELALISTACLIENTE);
		/*1783*/ usaDestaqueClienteSemLimiteCredito = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESTAQUECLIENTESEMLIMITECREDITO));
		/*1784*/ usaValorMinimoParaPedidoPorFuncao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORMINIMOPARAPEDIDOPORFUNCAO));      
		/*1785*/ usaValorMinimoParaPedidoPorGrupoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVALORMINIMOPARAPEDIDOPORGRUPOCLIENTE));
		/*1786*/ loadVlMinPedidoTipoPessoa(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.VALORMINIMOPARAPEDIDOPORTIPOPESSOA));      
		/*1799*/ configOrdenacaoProdutoLista = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGORDENACAOPRODUTOLISTA);
		/*1795*/ configIndiceFinanceiroCondPagamento = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGINDICEFINANCEIROCONDPAGAMENTO);
		/*1801*/ usaDescontoExtra = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOEXTRA));
		/*1802*/ calculaPrecoPorVolumeProduto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CALCULAPRECOPORVOLUMEPRODUTO));
		/*1803*/ configValoresMinimos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGVALORESMINIMOS);
		/*1804*/ usaVerbaPositivaPorGrupoProdutoTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBAPOSITIVAPORGRUPOPRODUTOTABELAPRECO));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGAPRESENTAVALORVENDAITEMPEDIDOREDECLIENTE);
		/*1806*/ apresentaValorVendaItemPedidoRedeCliente = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGAPRESENTAVALORVENDAITEMPEDIDOREDECLIENTE);
		/*1806-2*/ nuDiasConsideraPedido = getVlParamAsInt(getValorAtributoJson(vlParam, "nuDiasConsideraPedido", ValorParametro.CONFIGAPRESENTAVALORVENDAITEMPEDIDOREDECLIENTE));
		apresentaValorVendaItemPedidoRedeCliente &= nuDiasConsideraPedido > 0;
		/*1805*/ usaVerbaPorFaixaMargemContribuicao = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAVERBAPORFAIXAMARGEMCONTRIBUICAO));
		vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDESCQUANTIDADEPORPACOTE);
		/*1808-1*/ usaDescQuantidadePorPacote = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGDESCQUANTIDADEPORPACOTE);
		/*1808-2*/ usaTabPrecoDescQuantidadePorPacote = isAtributoJsonLigado(vlParam, "usaTabPreco", ValorParametro.CONFIGDESCQUANTIDADEPORPACOTE);
		/*1810*/ usaDescontoMaximoPorCondicaoPagamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USADESCONTOMAXIMOPORCONDICAOPAGAMENTO));
		/*1811*/ configCadastroFotoProdutoNaPesquisaDeMercado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCADASTROFOTOPRODUTONAPESQUISADEMERCADO);
		/*1812*/ configCadastroCoordenadaNaPesquisaDeMercado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO);
		/*1813*/ configPesquisaCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPESQUISACLIENTE);
		/*1814*/ marcaPedidoPendenteComItemBonificado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MARCAPEDIDOPENDENTECOMITEMBONIFICADO);
		/*1816*/ liberaComSenhaRestauracaoBackup = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIBERACOMSENHARESTAURACAOBKP);
		/*1817*/ configLiberacaoComSenhaCondPagamento = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLIBERACAOCOMSENHACONDPAGAMENTO);

		/*1887*/ isVerificaPedidoJaEnviadoReservaEstoqueComTrigger = ValueUtil.VALOR_SIM.equalsIgnoreCase(getValorParametroPorSistema(hashValorParametros, ValorParametro.VERIFICAPEDIDOJAENVIADORESERVAESTOQUECOMTRIGGER));
		/*1820*/ configCalculoFretePersonalizado = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO);
		/*1821*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGDIVULGAINFORMACAO);
		/*1821-1*/ usaDivulgaInformacao = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGDIVULGAINFORMACAO);
		/*1821-2*/ obrigaVerTodosDivulgaInformacao = isAtributoJsonLigado(vlParam, "obrigaVerTodos", ValorParametro.CONFIGDIVULGAINFORMACAO);
		/*1821-3*/ dirImagemDivulgaInformacao = getValorAtributoJson(vlParam, "dirImagem", ValorParametro.CONFIGDIVULGAINFORMACAO);
		/*1821-4*/ nuMaxWidthHeightImagemDivulgaInformacao = getValorAtributoJson(vlParam, "nuMaxWidthHeightImagem", ValorParametro.CONFIGDIVULGAINFORMACAO);
		/*1821-5*/ nuApresentaDivulgacao = ValueUtil.getIntegerValue(getValorAtributoJson(vlParam, "nuApresentaDivulgacao", ValorParametro.CONFIGDIVULGAINFORMACAO));
		/*1821-6*/ nuTimeOutImagemUrl = ValueUtil.getIntegerValue(getValorAtributoJson(vlParam, "nuTimeOutImagemUrl", ValorParametro.CONFIGDIVULGAINFORMACAO));
		if (nuTimeOutImagemUrl <= 0) {
			nuTimeOutImagemUrl = 3;
	}
		/*1821-7*/ baixaImagemLinkAoExibir = isAtributoJsonLigado(vlParam, "baixaImagemLinkAoExibir", ValorParametro.CONFIGDIVULGAINFORMACAO);
		/*1822*/ permiteIgnorarRecalculoCondicaoPagamento = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEIGNORARRECALCULOCONDICAOPAGAMENTO));
		/*1826*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-1*/ usaControlePontuacao = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-2*/ mostraPontuacaoPedido = getValorAtributoJson(vlParam, "mostraPontuacaoPedido", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-3*/ mostraSeloPontuacaoItem = usaControlePontuacao && isAtributoJsonLigado(vlParam, "mostraSeloPontuacaoItem", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-4*/ mostraPontuacaoListaItem = getValorAtributoJson(vlParam, "mostraPontuacaoListaItem", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-5*/ mostraExtratoPontuacao = usaControlePontuacao && isAtributoJsonLigado(vlParam, "mostraExtratoPontuacao", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-6*/ mostraPontuacaoResumoDia = getValorAtributoJson(vlParam, "mostraPontuacaoResumoDia", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-7*/ nuCasasDecimaisPontuacao = ValueUtil.getIntegerValue(getValorAtributoJson(vlParam, "nuCasasDecimaisPontuacao", ValorParametro.CONFIGCONTROLEPONTUACAO));
		/*1826-8*/ ignoraExtratoApp = isAtributoJsonLigado(vlParam, "ignoraExtratoApp", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-9*/ ocultaValorPontuacaoBaseExtrato = isAtributoJsonLigado(vlParam, "ocultaValorPontuacaoBaseExtrato", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-10*/ tipoCalculoPontuacao = ValueUtil.getIntegerValue(getValorAtributoJson(vlParam, "tipoCalculoPontuacao", ValorParametro.CONFIGCONTROLEPONTUACAO));
		tipoCalculoPontuacao = tipoCalculoPontuacao < 1 || tipoCalculoPontuacao > 3 ? 1 : tipoCalculoPontuacao;
		/*1826-11*/ usaValorTotalPedidoFaixaDias = usaControlePontuacao && isAtributoJsonLigado(vlParam, "usaValorTotalPedidoFaixaDias", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1826-12*/ usaConversaoUnidadeAlternativaPesoPontuacao = usaControlePontuacao && isAtributoJsonLigado(vlParam, "usaConversaoUnidadeAlternativaPesoPontuacao", ValorParametro.CONFIGCONTROLEPONTUACAO);
		/*1828*/ carregaParametrosProspect(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPROSPECT), ValorParametro.CONFIGPROSPECT);
		/*1831*/ configPoliticaComercial = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPOLITICACOMERCIAL), ValorParametro.CONFIGPOLITICACOMERCIAL);
		/*1832*/ usaGondolaPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGGONDOLAPEDIDO));
		/*1833*/ configCategoriaProduto = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCATEGORIAPRODUTO);
		/*1834*/ configMotivoPendencia = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGMOTIVOPENDENCIA);
		/*1835*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPEDIDOPRODUTOCRITICO);
		/*1835-1*/ usaPedidoProdutoCritico = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGPEDIDOPRODUTOCRITICO);
		/*1835-2*/ permiteApenasUmItemPedidoProdutoCritico = usaPedidoProdutoCritico && isAtributoJsonLigado(vlParam, "permiteApenasUmItemPedido", ValorParametro.CONFIGPEDIDOPRODUTOCRITICO);
		/*1835-3*/ filtraProdutosPedidoNaoCritico = usaPedidoProdutoCritico && isAtributoJsonLigado(vlParam, "filtraProdutosPedidoNaoCritico", ValorParametro.CONFIGPEDIDOPRODUTOCRITICO);
		/*1837*/ configPesquisaMercadoProdutoConcorrente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
		/*1838*/ configMargemRentabilidade = getParametroAsJson(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMARGEMRENTABILIDADE), ValorParametro.CONFIGMARGEMRENTABILIDADE);
		/*1839*/ configCadastroMetasPlataformaVenda = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA);
		/*1840*/ usaRadarRepresentante = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(ValorParametro.USARADARREPRESENTANTE));
		/*1842-1*/ configSolicitaAutorizacao = getValorParametroPorSistema(ValorParametro.CONFIGSOLICITAAUTORIZACAO);
		/*1842-2*/ geraNovidadeAutorizacao = isAtributoJsonLigado(configSolicitaAutorizacao, "geraNovidadeAutorizacao", ValorParametro.CONFIGSOLICITAAUTORIZACAO);
		/*1846*/ configHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDOPESSOAFISICA);
		/*1846-1*/ usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica = isAtributoJsonLigado(configHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica, "usa", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDOPESSOAFISICA);
		/*1847*/ bloquearRegistroChegadaRepNaoPresente = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.BLOQUEARREGISTROCHEGADAREPNAOPRESENTE));

		/*1848*/ usaGeracaoVisitaProspectNovoCliente = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(ValorParametro.USAGERACAOVISITAPROSPECTNOVOCLIENTE));

		/*1849*/ ocultaComissaoCapaPedidoConformeLayoutItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTACOMISSAOCAPAPEDIDOCONFORMELAYOUTITEMPEDIDO));
		/*1850*/ vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMOTIVOPERDAPEDIDO);
		/*1850-1*/ usaPedidoPerdido = isAtributoJsonLigado(vlParam, "usa", ValorParametro.CONFIGMOTIVOPERDAPEDIDO);
		/*1850-2*/ cdStatusPedidoPerdido = getValorAtributoJson(vlParam, "cdStatusPedidoPerdido", ValorParametro.CONFIGMOTIVOPERDAPEDIDO);
		/*1850-3*/ nuDiasPermanenciaPedidoPerdido = ValueUtil.getIntOrDefaultValue(getValorAtributoJson(vlParam, "nuDiasPermanenciaPedidoPerdido", ValorParametro.CONFIGMOTIVOPERDAPEDIDO), 180);

		/*1851*/ carregaParametrosMaisParceiro(getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGAPPEXTERNOMENUCLIENTE));
		/*1852*/ usaMarcadorPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAMARCADORPEDIDO));
		/*1853*/ configFiltroEstoqueDisponivel = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGFILTROESTOQUEDISPONIVEL);
		/*1856*/ configConversaoTipoPedido =getValorParametroPorSistema(ValorParametro.CONFIGCONVERSAOTIPOPEDIDO);
		/*1871*/ usaPercDescGrupoProdutoOuClienteVip = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPERCDESCGRUPOPRODUTOOUCLIENTEVIP));

		/*1857*/ loadSyncWebAppEntidadeRepZero(getValorParametroPorSistema(hashValorParametros, ValorParametro.SYNCWEBAPPENTIDADEREPZERO));
		/*1858*/ usaPriorizacaoPesquisaItemPedido = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAPRIORIZACAOPESQUISAITEMPEDIDO));
		/*1859*/ configListaItensDoPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLISTAITENSDOPEDIDO);
		/*1860*/mostraRentabPraticadaSugerida = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRARENTABPRATICADASUGERIDA));

		/*1861*/ configListaNovoCliente = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLISTANOVOCLIENTE);

		/*1864*/ ocultaCondPagtoPorCondComercial = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTACONDPAGTOPORCONDCOMERCIAL));
		/*1865*/ configHistoricoTrocaDevolucao = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGHISTORICOTROCADEVOLUCAO);

		/*1866*/ apresentaHistoricoTitulosAtrasados = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTAHISTORICOTITULOSATRASADOS));
		/*1867*/ marcaPedidoPendenteAprovacaoCondPagto = getValorParametroPorSistema(ValorParametro.MARCAPEDIDOPENDENTEAPROVACAOCONDPAGTO);
		/*1870*/replicaDadosItemTabelaPreco0PorTabelaPreco = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(hashValorParametros, ValorParametro.REPLICADADOSITEMTABELAPRECO0PORTABELAPRECO));

		/*1873*/configLaudoDap = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLAUDODAP);
		/*1783*/ usaInversaoIndiceFinanceiroClienteCondPagto = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAINVERSAOINDICEFINANCEIROCLIENTECONDPAGTO));
		/*1879*/ nuCasasDecimaisBoleto = ValueUtil.getIntegerValue(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUCASASDECIMAISBOLETO));
		if (ValueUtil.VALOR_NAO.equals(getValorParametroPorEmpresa(hashValorParametros, ValorParametro.NUCASASDECIMAISBOLETO))) {
			nuCasasDecimaisBoleto = nuCasasDecimais;
		}
		/*1880*/pedidoViaCampanhaPublicitaria = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PEDIDOVIACAMPANHAPUBLICITARIA);
		/*1882-1*/ configListaProdutoAgrupadorGrade = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLISTAPRODUTOAGRUPADORGRADE);
		/*1882-2*/ permiteOcultarValoresItemAgrupadorGrade = getValorAtributoJson(configListaProdutoAgrupadorGrade, "permiteOcultarValoresItem", ValorParametro.CONFIGLISTAPRODUTOAGRUPADORGRADE);
		/*1889*/ configPoliticaBonificacao = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGPOLITICABONIFICACAO);
		/*1891*/ configReservaEstoqueCorrente =  getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGRESERVAESTOQUECORRENTE);
		isConfigReservaEstoqueCorrente = isUsaReservaEstoqueCorrente();
		mostraEstoqueCorrente = isAtributoJsonLigado(configReservaEstoqueCorrente, "mostraEstoqueCorrente", ValorParametro.CONFIGRESERVAESTOQUECORRENTE);
		/*1893*/usaCotacaoMoedaProduto = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorSistema(ValorParametro.USACOTACAOMOEDAPRODUTO));
		/*1895*/ mostraInfoEstoqueUnidades = ValueUtil.valueEquals(ValueUtil.VALOR_SIM , getValorParametroPorRepresentante(ValorParametro.MOSTRAINFOESTOQUEUNIDADES));
		/*1900*/ gerarPdfBoletoNotaFiscal = getValorParametroPorSistema(ValorParametro.GERARPDFBOLETONOTAFISCAL);
		/*1901*/ configFotoProdutoGrade = getValorParametroPorSistema(ValorParametro.CONFIGFOTOPRODUTOGRADE);
		/*1902*/ usaCarrosselProdutosGradeDetalheItem = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USACARROSSELPRODUTOSGRADEDETALHEITEM));
		/*1903*/ usaCatalogoExterno = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.USACATALOGOEXTERNO);
		/*1904*/ configPrioridadeLiberacaoPendenciaPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGPRIORIDADELIBERACAOPENDENCIAPEDIDO);
		/*1905*/ configMenuCatalogo = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGMENUCATALOGO);
		/*1906*/ usaImagemQrCode = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAIMAGEMQRCODE));
		/*1907*/ usaSugestaoVendaPorDivisao = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USASUGESTAOVENDAPORDIVISAO));
		/*1909*/ apresentaDsFichaTecnicaCapaItemPedido = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.APRESENTADSFICHATECNICACAPAITEMPEDIDO));
		/*1910*/ configVideosProdutos = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGVIDEOSPRODUTOS);
		/*1914*/ mostraBonusListaProduto = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.MOSTRABONUSLISTAPRODUTO));
		/*1915*/ configCampoDinamicoIndicadorProdutividadeInterno = getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGCAMPODINAMICOINDICADORPRODUTIVIDADEINTERNO);
		/*1916*/ configStatusItemPedido = getValorParametroPorEmpresa(hashValorParametros, ValorParametro.CONFIGSTATUSITEMPEDIDO);
		/*1918*/ aplicaApenasDescQtdOuIndiceCondPagto = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorEmpresa(hashValorParametros, ValorParametro.APLICAAPENASDESCQTDOUINDICECONDPAGTO));
		/*1921*/ usaHistoricoAtendimentoUnificado = ValueUtil.getBooleanValue(getValorAtributoJson(getValorParametroPorSistema(hashValorParametros, ValorParametro.CONFIGHISTORICOATENDIMENTOUNIFICADO), "usa", ValorParametro.CONFIGHISTORICOATENDIMENTOUNIFICADO));
		/*1923*/ ocultaSementeTelasGeracaoSenha = ValueUtil.VALOR_SIM.equals(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.OCULTASEMENTETELASGERACAOSENHA));
		/*1924*/ loadConfigSyncAutomatico();
		/*1925*/ configInterfaceSistema = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGINTERFACESISTEMA);
		/*1926*/ usaEscolhaModoFeira = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAESCOLHAMODOFEIRA));
		/*1929*/ limpaQtItemFisicoMudancaUnidade = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.LIMPAQTITEMFISICOMUDANCAUNIDADE));
		/*1931*/ configApresentacaoInfosPersonalizadasCapaItemPedido = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGAPRESENTACAOINFOSPERSONALIZADASCAPAITEMPEDIDO);
		/*1932*/ apresentaFiltroRepresentanteListaProdutosCargaSupervisor = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorEmpresa(hashValorParametros, ValorParametro.APRESENTAFILTROREPRESENTANTELISTAPRODUTOSCARGASUPERVISOR));
		/*1934*/ configNumeroRegistrosMostradosPorVezNaLista = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGNUMEROREGISTROSMOSTRADOSPORVEZNALISTA);
		/*1937*/ usaNovoPedidoOrcamentoSemRegistroChegada = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USANOVOPEDIDOORCAMENTOSEMREGISTROCHEGADA));
		/*1938*/ loadControleLoginUsuario();
		/*1940*/ usaFiltroProdutoCondicaoPagamentoRepresentante = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTROPRODUTOCONDICAOPAGAMENTOREPRESENTANTE));
		/*1941*/ usaFiltroDeProdutoNaListaDePedido = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAFILTRODEPRODUTONALISTADEPEDIDO));
		/*1942*/ configLeads = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGLEADS);
		/*1945*/ exibirNotasDevolucaoNaFichaFinanceira = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.EXIBIRNOTASDEVOLUCAONAFICHAFINANCEIRA));
		/*1946*/ permiteMultAgendasNoDiaMesmoCliente = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEMULTAGENDASNODIAMESMOCLIENTE));
		/*1947*/ permiteIniciarPedidoSemCliente = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getValorParametroPorRepresentante(hashValorParametros, ValorParametro.PERMITEINICIARPEDIDOSEMCLIENTE));

		afterLoadParameters();
	}

	static void loadConfigSyncAutomatico() {
		try {
			JSONObject json = LavenderePdaConfig.getParametroAsJson(getValorParametroPorRepresentante(ValorParametro.CONFIGSYNCAUTOMATICO), ValorParametro.CONFIGSYNCAUTOMATICO);
			intervaloSyncAutomaticoApp2Web = json.optInt("enviar", 0);
			intervaloSyncAutomaticoWeb2App = json.optInt("receber", 0);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private static void afterLoadParameters() {
		if (usaCategoriaInsercaoItem() || usaCategoriaMenuProdutos()) { /* 1833 */
			ButtonMenuCategoria.configureConstants();
		}
		configInterfaceProperties();
	}

	private static void configInterfaceProperties() {
		AppConfig.instance.useBottomButtonText = isAtributoJsonLigado(configInterfaceSistema, "usaLegendaBotoesInferiores", ValorParametro.CONFIGINTERFACESISTEMA);
		AppConfig.instance.usaCorFundoFotosApenasTemaEscuro = isAtributoJsonLigado(configInterfaceSistema, "usaCorFundoFotosApenasTemaEscuro", ValorParametro.CONFIGINTERFACESISTEMA);
		try {
			String dsCorValue = getValorAtributoJson(configInterfaceSistema, "dsCorFundoFotos", ValorParametro.CONFIGINTERFACESISTEMA);
			String[] dsCorValueArray = StringUtil.getStringValue(dsCorValue).split(",");
			if (dsCorValueArray.length == 1) {
				AppConfig.instance.nuCorFundoFotos =  ValueUtil.VALOR_SIM.equals(dsCorValueArray[0]) ? Color.WHITE : -1;
			} else {
				AppConfig.instance.nuCorFundoFotos = ColorUtil.getRgbColor(dsCorValueArray);
			}
		} catch (Exception e) {
			AppConfig.instance.nuCorFundoFotos = -1;
		}
	}

	@SuppressWarnings("unused")
	private static int getSementeSenhaParametro(final String vlParam, final String propriedade, int cdParametro) {
		try {
			int sementeSenha = ValueUtil.getIntegerValue(getValorAtributoJson(vlParam, propriedade, cdParametro));
			return sementeSenha > 0 ? sementeSenha : 15000;
		} catch (Throwable e) {
			return 15000;
		}
	}

	private static void loadVlMinPedidoTipoPessoa(String valorParametroPorRepresentante) {
		if (ValueUtil.isNotEmpty(valorParametroPorRepresentante)) {
			String[] values = valorParametroPorRepresentante.split(";");
			if (values.length > 0) {
				valorMinimoPedidoPessoaJuridica = ValueUtil.getDoubleSimpleValue(values[0]);
			}
			if (values.length > 1) {
				valorMinimoPedidoPessoaFisica = ValueUtil.getDoubleSimpleValue(values[1]);
			}
		}
	}

	private static Hashtable getHashValorParametros() throws SQLException {
		Vector valorParametroList = ValorParametroService.getInstance().findAll();
		if (valorParametroList.size() == 0) {
			throw new ValidationException(Messages.PARAMETRO_NENHUMREGISTROTABELA);
		}
		Hashtable hashValorParametros = new Hashtable((int)(valorParametroList.size() * 1.2));
		int size = valorParametroList.size();
		ValorParametro valorParametro;
		for (int i = 0; i < size; i++) {
			valorParametro = (ValorParametro)valorParametroList.items[i];
			hashValorParametros.put(valorParametro.getRowKey(), valorParametro);
		}
		return hashValorParametros;
	}

	public static ValorParametro getParametroPorSistema(int cdParametro) throws SQLException {
		return getValorParametro(null, cdParametro, ValorParametro.NMENTIDADE_SISTEMA, StringUtil.getStringValue(ValorParametro.CDSISTEMA));
	}
	
	public static ValorParametro getParametroPorEmpresa(int cdParametro) throws SQLException {
		return getParametroPorEmpresa(cdParametro, SessionLavenderePda.cdEmpresa);
	}

	public static ValorParametro getParametroPorEmpresa(int cdParametro, String cdEmpresa) throws SQLException {
		ValorParametro valorParametro = getValorParametro(null, cdParametro, ValorParametro.NMENTIDADE_EMPRESA, StringUtil.getStringValue(cdEmpresa));
		if (valorParametro == null) {
			return getParametroPorSistema(cdParametro);
		}
		return valorParametro;
	}
	
	public static ValorParametro getParametroPorRepresentante(int cdParametro) throws SQLException {
		return getParametroPorRepresentante(cdParametro, SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
	}

	public static ValorParametro getParametroPorRepresentante(int cdParametro, String cdEmpresa, String cdRepresentante) throws SQLException {
		ValorParametro valorParametro = getValorParametro(null, cdParametro, ValorParametro.NMENTIDADE_REPRESENTANTE, StringUtil.getStringValue(cdRepresentante));
		if (valorParametro == null) {
			return getParametroPorEmpresa(cdParametro, cdEmpresa);
		}
		return valorParametro;
	}

	public static String getValorParametroPorSistema(int cdParametro) throws SQLException {
		return getValorParametroPorSistema(null, cdParametro);
	}
	
	private static String getValorParametroPorSistema(Hashtable hashParametros, int cdParametro) throws SQLException {
		ValorParametro valorParametro = getValorParametro(hashParametros, cdParametro, ValorParametro.NMENTIDADE_SISTEMA, StringUtil.getStringValue(ValorParametro.CDSISTEMA));
		if (valorParametro == null) {
			return "";
		} else {
			return valorParametro.vlParametro;
		}
	}

	public static String getValorParametroPorEmpresa(int cdParametro) throws SQLException {
		return getValorParametroPorEmpresa(null, cdParametro);
	}
	
	private static String getValorParametroPorEmpresa(Hashtable hashParametros, int cdParametro) throws SQLException {
		ValorParametro valorParametro = getValorParametro(hashParametros, cdParametro, ValorParametro.NMENTIDADE_EMPRESA, StringUtil.getStringValue(SessionLavenderePda.cdEmpresa));
		if (valorParametro == null) {
			return getValorParametroPorSistema(hashParametros, cdParametro);
		} else {
			return valorParametro.vlParametro;
		}
	}

	public static String getValorParametroPorRepresentante(int cdParametro) throws SQLException {
		return getValorParametroPorRepresentante(null, cdParametro);
	}

	private static String getValorParametroPorRepresentante(Hashtable hashParametros, int cdParametro) throws SQLException {
		ValorParametro valorParametro = getValorParametro(hashParametros, cdParametro, ValorParametro.NMENTIDADE_REPRESENTANTE, StringUtil.getStringValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
		if (valorParametro == null) {
			return getValorParametroPorEmpresa(hashParametros, cdParametro);
		} else {
			return valorParametro.vlParametro;
		}
	}

	private static final boolean isDominioLigado(final String param, final String value) {
		if (param == null || value == null) return false;
		
		String[] vlParams = param.split(";");
		for (String vlParam : vlParams) {
			if (value.equals(vlParam)) {
				return true;
			}
		}
		return false;
	}

	protected static boolean isAtributoJsonLigado(final String vlParametroJson, final String chave, final int cdParametro) {
		return comparaValorAtributoJson(vlParametroJson, chave, ValueUtil.VALOR_SIM, cdParametro);
	}
	
	protected static boolean isAtributoJsonLigado(final JSONObject vlParametroJson, final String chave, final int cdParametro) {
		return comparaValorAtributoJson(vlParametroJson, chave, ValueUtil.VALOR_SIM, cdParametro);
	}

	protected static boolean isAtributoJsonDesligado(final String vlParametroJson, final String chave, final int cdParametro) {
		return comparaValorAtributoJson(vlParametroJson, chave, ValueUtil.VALOR_NAO, cdParametro);
	}
	
	protected static boolean comparaValorAtributoJson(final String vlParametroJson, final String chave, final String valorDesejado, final int cdParametro) {
		return ValueUtil.valueEquals(valorDesejado, getValorAtributoJson(vlParametroJson, chave, cdParametro));
	}

	protected static boolean comparaValorAtributoJson(final JSONObject vlParametroJson, final String chave, final String valorDesejado, final int cdParametro) {
		return ValueUtil.valueEquals(valorDesejado, getValorAtributoJson(vlParametroJson, chave, cdParametro));
	}
	
	private static boolean containsValorAtributoJson(final JSONObject vlParametroJson, final String chave, final String valorDesejado, final int cdParametro) {
		String value = getValorAtributoJson(vlParametroJson, chave, cdParametro);
		if (value != null && !value.equals(ValueUtil.VALOR_NAO)) {
			return Arrays.asList(value.split(";")).contains(valorDesejado);
		}
		return false;
	}
	
	protected static String getValorAtributoJson(final String vlParametroJson, final String chave, final int cdParametro) {
		return getValorAtributoJson(getParametroAsJson(vlParametroJson, cdParametro), chave, cdParametro);
	}

	private static String getValorAtributoJson(JSONObject json, String chave, int cdParametro) {
		try {
			return json.getString(chave);
		} catch (Throwable e) {
			if (Session.isModoDebug) {
				String msg = "Parametro: " + cdParametro + "\n Valor: " + json.toString() + "\n Erro: " + e.getMessage();
				ExceptionUtil.handle(e, msg);
		}
		}
		return ValueUtil.VALOR_NAO;
	}

	public static JSONObject getParametroAsJson(final String vlParametroJson, final int cdParametro) {
		try {
			JSONObject json;
			if (jsonParams != null) {
				json = (JSONObject) jsonParams.get(cdParametro);
				if (json == null) {
					json = new JSONObject(vlParametroJson);
					jsonParams.put(cdParametro, new JSONObject(vlParametroJson));
				}
			} else {
				json = new JSONObject(vlParametroJson);
			}
			return json;
		} catch (Throwable e) {
			if (Session.isModoDebug) {
				String msg = "Parametro: " + cdParametro + "\n Valor: " + vlParametroJson + "\n Erro: " + e.getMessage();
				ExceptionUtil.handle(e, msg);
			}
		}
		return new JSONObject();
	}

	private static int getVlParamAsInt(String vlParametro) {
		if (ValueUtil.VALOR_SIM.equals(vlParametro)) {
			return 1;
		}
		return ValueUtil.getIntegerValue(vlParametro);
		
	}

	private static int getMaiorVlParam(String vlParam) {
		try {
			String[] values = StringUtil.split(vlParam, ',');
			int size = values.length;
			int maxValue = 0;
			for (int i = 0; i < size; i++) {
				int value = ValueUtil.getIntegerValue(values[i]);
				if (value > maxValue) {
					maxValue = value;
				}
			}
			return maxValue;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return 0;
	}

	private static ValorParametro getValorParametro(Hashtable hashParametros, int cdParametro, String nmEntidade, String vlChave) throws SQLException {
		ValorParametro valorParametro = new ValorParametro();
		valorParametro.cdSistema = ValorParametro.CDSISTEMA;
		valorParametro.cdParametro = cdParametro;
		valorParametro.nmEntidade = nmEntidade;
		valorParametro.vlChave = "[" + vlChave + "]";
		if (hashParametros == null) {
			return (ValorParametro) ValorParametroService.getInstance().findByRowKey(valorParametro.getRowKey());
		} else {
			return (ValorParametro) hashParametros.get(valorParametro.getRowKey());
		}
	}
	
	public static boolean isParametroLigadoEmQualquerEntidade(int cdParametro) throws SQLException {
		ValorParametro valorParametro = new ValorParametro();
		valorParametro.cdSistema = ValorParametro.CDSISTEMA;
		valorParametro.cdParametro = cdParametro;
		valorParametro.vlParametro = ValueUtil.VALOR_NAO;
		return ValueUtil.isNotEmpty(ValorParametroService.getInstance().getValorParametroLigadoList(valorParametro));
	}
	
	public static String getPrimeiroValorValidoDoParametro(int cdParametro) throws SQLException {
		ValorParametro valorParametroFilter = new ValorParametro();
		valorParametroFilter.cdSistema = ValorParametro.CDSISTEMA;
		valorParametroFilter.cdParametro = cdParametro;
		valorParametroFilter.nmEntidade = ValorParametro.NMENTIDADE_REPRESENTANTE;
		valorParametroFilter.vlParametro = ValueUtil.VALOR_NAO;
		Vector valorParametroLigadoList = ValorParametroService.getInstance().getValorParametroLigadoList(valorParametroFilter);
		if (ValueUtil.isNotEmpty(valorParametroLigadoList)) {
			ValorParametro valorParametro = (ValorParametro) valorParametroLigadoList.items[0];
			return valorParametro.vlParametro;
		} else {
			return getValorParametroPorEmpresa(cdParametro);
		}
	}

	//-----------------------------------------------------------------------------------
	// M�todos complementares
	//-----------------------------------------------------------------------------------
	
	private static String returnValueConfigColunasGridLoteProduto(Hashtable hashValorParametros) throws SQLException {
		String value = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.CONFIGCOLUNASGRIDLOTEPRODUTO);
		if (ValueUtil.isEmpty(value)) return "2"; 
		if (!value.contains("2")) {
			value += value.charAt(value.length()-1) == ';' ? "2" : ";2";
		}
		return value;
	}
	

	private static boolean loadVlParamRelDifPedido(Hashtable hashValorParametros) throws SQLException {
		String vlParam = getValorParametroPorSistema(hashValorParametros, ValorParametro.RELDIFERENCASPEDIDO);
		return isDominioLigado(vlParam, ValueUtil.VALOR_SIM) || isDominioLigado(vlParam, "P");
	}
	
	public static void loadVlMinimoPedido(String vlMinimoParaPedido) {
		if (ValueUtil.isNotEmpty(vlMinimoParaPedido)) {
			String[] values = vlMinimoParaPedido.split(";");
			if (values.length > 0) {
				String value = values.length > 1 ? values[1] : values[0];
				valorMinimoParaPedido = ValueUtil.getDoubleSimpleValue(value);
			}
		}
	}

	private static void loadVlParamApresentaPedidosComErroEnvio(String vlParamApresentaPedidosComErroNoEnvio) {
		if (vlParamApresentaPedidosComErroNoEnvio != null) {
			List<String> values =  Arrays.asList(vlParamApresentaPedidosComErroNoEnvio.split(";"));
			apresentaPopUpErroEnvioPedidoNovoPedido = values.contains("1");
			apresentaPopUpErroEnvioPedidoAcessoTelaPorMenu = values.contains("2");
			apresentaPopUpErroEnvioPedidoAoSairSistema = values.contains("3");
		}
	}
	
	public static boolean isArredondaDescontosSequenciaisNoItemPedidoApenasNoFinal() {
		return getVlParamAsInt(aplicaDescontosSequenciaisNoItemPedido) == 2;
	}
	
	public static boolean isAplicaDescontosSequenciaisNoItemPedido() {
		int vlParam = getVlParamAsInt(aplicaDescontosSequenciaisNoItemPedido);
		return vlParam == 1 || vlParam == 2;
	}

	private static int getQtCaracteresObservacoesPedidoItemPedido(String vlParamQtCaracteresObservacoesPedidoItemPedido) {
		String[] values = vlParamQtCaracteresObservacoesPedidoItemPedido.split(";");
		int valor = ValueUtil.getIntegerValue(values[0]);
		if (valor > 4000 || valor <= 0) {
			valor = 4000;
		}
		return valor;
	}
	
	public static boolean isApresentaPedidosComErroNoEnvio() {
		return apresentaPopUpErroEnvioPedidoAoSairSistema || apresentaPopUpErroEnvioPedidoNovoPedido || apresentaPopUpErroEnvioPedidoAcessoTelaPorMenu;
	}
	
	public static int getPermiteDescontoAcrescimoItemPedido() {
		if (ValueUtil.isEmpty(permiteDescontoAcrescimoItemPedido)) {
			return 0;
		}
		String[] vlParametro = permiteDescontoAcrescimoItemPedido.split(";");
		if (vlParametro.length == 2) {
			return ValueUtil.getIntegerValue(vlParametro[1]);
		} else {
			return ValueUtil.getIntegerValue(vlParametro[0]);
		}
	}

	public static boolean isPermiteDescontoPercentualItemPedido() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return (value == ValorParametro.PARAM_DESCONTO_PCT) || (value == ValorParametro.PARAM_DESCONTO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_PCT)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTO_PCT_E_DESCONTOACRESCIMO_VALOR);
		} else {
			return false;
		}
	}

	public static boolean isPermiteAcrescimoPercentualItemPedido() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return (value == ValorParametro.PARAM_ACRESCIMO_PCT)
					|| (value == ValorParametro.PARAM_ACRESCIMO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_PCT)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_PCT_E_VALOR);
		} else {
			return false;
		}
	}

	public static boolean isPermiteApenasAcrescimoItemPedido() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return (value == ValorParametro.PARAM_ACRESCIMO_VALOR)
					|| (value == ValorParametro.PARAM_ACRESCIMO_PCT_E_VALOR);
		} else {
			return false;
		}
	}

	public static boolean isPermiteApenasDescontoItemPedido() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return (value == ValorParametro.PARAM_DESCONTO_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTO_PCT_E_VALOR);
		} else {
			return false;
		}
	}

	public static boolean isPermiteAlterarVlUnitarioItemPedidoDesconto() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return (value == ValorParametro.PARAM_DESCONTO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTO_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTO_PCT_E_DESCONTOACRESCIMO_VALOR);
		} else {
			return false;
		}
	}

	public static boolean isPermiteAlterarVlUnitarioItemPedidoAcrescimo() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return (value == ValorParametro.PARAM_ACRESCIMO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_ACRESCIMO_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_PCT_E_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTOACRESCIMO_VALOR)
					|| (value == ValorParametro.PARAM_DESCONTO_PCT_E_DESCONTOACRESCIMO_VALOR);
		} else {
			return false;
		}
	}
	
	public static boolean isPermiteAlterarVlUnitarioItemPedidoEApenasPercentualDesconto() {
		int value = getPermiteDescontoAcrescimoItemPedido();
		if (value != 0) {
			return value == ValorParametro.PARAM_DESCONTO_PCT_E_DESCONTOACRESCIMO_VALOR;
		} else {
			return false;
		}
	}

	public static boolean isPermiteAlterarVlUnitarioItemPedido() {
		return isPermiteAlterarVlUnitarioItemPedidoDesconto() || isPermiteAlterarVlUnitarioItemPedidoAcrescimo() || isPermiteAlterarVlUnitarioItemPedidoEApenasPercentualDesconto();
	}

	public static boolean isAplicaDescontoFimDoPedido() {
		return aplicaDescontoCCPPorItemFinalPedido || aplicaDescontoProgressivoPorItemFinalPedido
				|| isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida()
				|| aplicaIndiceFinanceiroClientePorItemFinalPedido
				|| aplicaIndiceFinanceiroEspClientePorItemFinalPedido;
	}

	public static boolean isUsaTabelaPrecoPedido() {
		return !(usaListaColunaPorTabelaPreco || ocultaTabelaPrecoPedido);
	}

	public static boolean isUsaRotaDeEntregaNoPedidoSemCadastroLigado() {
		return (usaRotaDeEntregaNoPedidoSemCadastro == 1) || (usaRotaDeEntregaNoPedidoSemCadastro == 2);
	}

	public static boolean isUsaRotaDeEntregaNoPedidoComCadastroLigado() {
		return ValueUtil.VALOR_SIM.equals(usaRotaDeEntregaNoPedidoComCadastro) || "1".equals(usaRotaDeEntregaNoPedidoComCadastro) || "2".equals(usaRotaDeEntregaNoPedidoComCadastro) || "3".equals(usaRotaDeEntregaNoPedidoComCadastro) || isUsaRotaDeEntregaPadraoDoCliente();
	}

	public static boolean isUsaRotaDeEntregaPadraoDoCliente() {
		return ValueUtil.valueEquals("4", usaRotaDeEntregaNoPedidoComCadastro);

	}
	
	public static boolean exibeAbaTotalizadoresPedidoCapaPedido() {
		return ValueUtil.VALOR_SIM.equals(exibeAbaTotalizadoresPedido) || "1".equals(exibeAbaTotalizadoresPedido) || exibeAbaTotalizadoresPedidoCapaPedidoGrade();
	}

	private static boolean exibeAbaTotalizadoresPedidoCapaPedidoGrade() {
		return "2".equals(exibeAbaTotalizadoresPedido);
	}
	
	public static boolean exibeAbaTotalizadoresPedidoGrade() {
		return "3".equals(exibeAbaTotalizadoresPedido) || exibeAbaTotalizadoresPedidoCapaPedidoGrade();
	}

	public static boolean isUsaRotaDeEntregaNoPedido() {
		return isUsaRotaDeEntregaNoPedidoComCadastroLigado() || isUsaRotaDeEntregaNoPedidoSemCadastroLigado();
	}

	public static boolean isUsaNuOrdemCompraClienteNoPedido() {
		return isUsaNuOrdemCompraClienteNoPedidoApenasNumeros() || isUsaNuOrdemCompraClienteNoPedidoCaracteresAlphanumericos();
	}

	public static boolean isUsaNuOrdemCompraClienteNoPedidoApenasNumeros() {
		return "1".equals(getValorAtributoJson(configNuOrdemCompraClienteNoPedido, "usa", ValorParametro.CONFIGNUORDEMCOMPRACLIENTENOPEDIDO));
	}

	public static boolean isUsaNuOrdemCompraClienteNoPedidoCaracteresAlphanumericos() {
		return "2".equals(getValorAtributoJson(configNuOrdemCompraClienteNoPedido, "usa", ValorParametro.CONFIGNUORDEMCOMPRACLIENTENOPEDIDO));
	}

	public static boolean usaNuOrdemCompraENuSeqClienteItemPedido() {
		return isUsaNuOrdemCompraClienteNoPedido() && isAtributoJsonLigado(configNuOrdemCompraClienteNoPedido, "usaNuOrdemCompraENuSeqClienteItemPedido", ValorParametro.CONFIGNUORDEMCOMPRACLIENTENOPEDIDO);
	}

	public static boolean isValorMinimoParaPedidoLigado() {
		return valorMinimoParaPedido != 0;
	}

	public static boolean isNuMinDiasPrevisaoEntregaDoPedidoLigado() {
		return nuMinDiasPrevisaoEntregaDoPedido != 0;
	}
	
	public static boolean isMostraDetalhesAdicionaisRelMetasPorProdutoLigado() {
		return (!ValueUtil.isEmpty(mostraDetalhesAdicionaisRelMetasPorProduto)) && (!ValueUtil.VALOR_NAO.equals(mostraDetalhesAdicionaisRelMetasPorProduto));
	}

	public static String[] getMostraDetalhesAdicionaisRelMetasPorProduto() {
		if (ValueUtil.VALOR_SIM.equals(mostraDetalhesAdicionaisRelMetasPorProduto)) {
			return new String[]{Messages.METAS_UNIDADE, Messages.METASPORPRODUTO_UNIDADE_REALIZADO, Messages.METAS_QTCAIXA, Messages.METASPORPRODUTO_CAIXA_REALIZADO, Messages.METAS_MIXCLIENTE, Messages.METASPORPRODUTO_MIXCLIENTE_REALIZADO};
		} else if (isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
			return StringUtil.split(mostraDetalhesAdicionaisRelMetasPorProduto, ';');
		}
		return new String[1];
	}

	public static boolean isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() {
		return !ValueUtil.isEmpty(mostraDetalhesAdicionaisRelMetasPorGrupoProduto) && !ValueUtil.VALOR_NAO.equals(mostraDetalhesAdicionaisRelMetasPorGrupoProduto);
	}

	public static String[] getMostraDetalhesAdicionaisRelMetasPorGrupoProduto1() {
		if (ValueUtil.VALOR_SIM.equals(mostraDetalhesAdicionaisRelMetasPorGrupoProduto)) {
			return new String[]
				{
					Messages.METAS_VLMETA,
					Messages.METAS_VLRESULTADO,
					Messages.METAS_PCTREALIZADO,
					Messages.METAS_UNIDADE,
					Messages.METASPORPRODUTO_UNIDADE_REALIZADO,
					Messages.METAS_PCTREALIZADO,
					Messages.METAS_QTCAIXA,
					Messages.METASPORPRODUTO_CAIXA_REALIZADO,
					Messages.METAS_PCTREALIZADO,
					Messages.METAS_MIXCLIENTE,
					Messages.METASPORPRODUTO_MIXCLIENTE_REALIZADO,
					Messages.METAS_PCTREALIZADO,
					Messages.METAS_DTINICIAL,
					Messages.METAS_DTFINAL
				};
		} else if (isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado()) {
			String[] columns = StringUtil.split(mostraDetalhesAdicionaisRelMetasPorGrupoProduto, ';');
			if (columns.length != 14) {
				UiUtil.showWarnMessage(Messages.META_ERRO_PARAMETRO);
				return new String[]{".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."};
			}
			return columns;
		} else {
			return new String[]{Messages.METAS_VLMETA, Messages.METAS_VLRESULTADO, Messages.METAS_PCTREALIZADO, ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."};
		}
	}

	public static boolean isObrigaReceberDadosBloqueiaNovoPedido() {
		if (obrigaReceberDadosBloqueiaNovoPedido != null) {
			int intervalo = ValueUtil.getIntegerValue(obrigaReceberDadosBloqueiaNovoPedido);
			return (intervalo > 0) || ((intervalo == 0) && (obrigaReceberDadosBloqueiaNovoPedido.indexOf(':') != -1));
		}
		return false;
	}

	public static boolean isObrigaReceberDadosBloqueiaUsoSistema() {
		if (obrigaReceberDadosBloqueiaUsoSistema != null) {
			int intervalo = ValueUtil.getIntegerValue(obrigaReceberDadosBloqueiaUsoSistema);
			return (intervalo > 0) || ((intervalo == 0) && (obrigaReceberDadosBloqueiaUsoSistema.indexOf(':') != -1));
		}
		return false;
	}


	public static boolean isOcultaInfoPreco() {
		return isAtributoJsonLigado(ocultaPrecosNoMenuProduto, "ocultaInfoPreco", ValorParametro.OCULTAPRECOSNOMENUPRODUTO);
	}

	public static boolean isOcultaPrecoCondComercial() {
		return isAtributoJsonLigado(ocultaPrecosNoMenuProduto, "ocultaPrecoCondComercial", ValorParametro.OCULTAPRECOSNOMENUPRODUTO);
	}

	public static boolean isOcultaDescTabela() {
		return isAtributoJsonLigado(ocultaPrecosNoMenuProduto, "ocultaDescTabela", ValorParametro.OCULTAPRECOSNOMENUPRODUTO);
	}

	public static boolean isAvisaUsuarioPositivacaoFornecedor() {
		String value = avisaUsuarioPositivacaoFornecedor;
		return !ValueUtil.isEmpty(value) && !value.equals(ValueUtil.VALOR_NAO);
	}
			
	public static boolean isValorMinCaractersParaObservacaoMotivoVisita() {
		return nuMinCaracterObservacaoMotivoVisita != 0;
	}

	public static boolean isUsaPedidoBonificacao() {
		return usaPedidoBonificacao();
	}

	public static boolean isAplicaDescontoProgressivoPorItemFinalPedido() {
		return aplicaDescontoProgressivoPorItemFinalPedido || aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex
				|| isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida();
	}

	public static boolean isUsaVerba() {
		return usaVerbaItemPedidoPorItemTabPreco || informaVerbaManual || isPermiteBonificarProdutoPedidoUsandoVerba()
				|| (usaPedidoBonificacao() && !naoDescontaVerbaEmPedidoBonificacao)
				|| aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex
				|| usaVerbaPorFaixaRentabilidadeComissao || usaVerbaGrupoProdComToleranciaNoDesconto || usaVerbaPositivaPorGrupoProdutoTabelaPreco || usaVerbaGrupoSaldoPersonalizada();
	}

	public static boolean isAplicaDescProgressivoPorQtdPorItemFinalPedidoLigado() {
		return 0 != aplicaDescProgressivoPorQtdPorItemFinalPedido;
	}

	public static boolean isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida() {
		return 2 == aplicaDescProgressivoPorQtdPorItemFinalPedido;
	}

	private static boolean isNaoConsisteDescontoAcrescimoMaximo(String paramValue) {
		return paramValue.equalsIgnoreCase("S") || ValorParametro.PARAM_NAOCONSISTEDESCONTOACRESCIMOMAXIMO.equals(paramValue);
	}

	public static boolean isNaoConsisteDescontoMaximo() {
		String paramValue = naoConsisteDescontoAcrescimoMaximo;
		return (paramValue != null) && (isNaoConsisteDescontoAcrescimoMaximo(paramValue) || ValorParametro.PARAM_NAOCONSISTEDESCONTOMAXIMO.equals(paramValue));
	}
	
	public static boolean isNaoConsisteAcrescimoMaximo() {
		String paramValue = naoConsisteDescontoAcrescimoMaximo;
		return (paramValue != null) && (isNaoConsisteDescontoAcrescimoMaximo(paramValue) || ValorParametro.PARAM_NAOCONSISTEACRESCIMOMAXIMO.equals(paramValue));
	}

	public static boolean isUsaAlertaBloqueioClienteSemPedido() {
		return nuDiasAlertaClienteSemPedido > 0 || nuDiasBloqueiaClienteSemPedido > 0 || nuDiasFiltroClienteSemPedido > 0;
	}

	public static int getNuDiasAlertaBloqueioClienteSemPedido() {
		int nuDias = nuDiasAlertaClienteSemPedido;
		if (nuDias == 0 || (nuDiasBloqueiaClienteSemPedido > 0 && nuDiasBloqueiaClienteSemPedido < nuDias)) {
			nuDias = nuDiasBloqueiaClienteSemPedido;
		}
		if (nuDias == 0 || (nuDiasFiltroClienteSemPedido > 0 && nuDiasFiltroClienteSemPedido < nuDias)) {
			nuDias = nuDiasFiltroClienteSemPedido;
		}
		return nuDias;
	}

	public static boolean isUsaDescontoMaximoBaseadoNoVlBaseFlex() {
		return usaVerbaItemPedidoPorItemTabPreco && !usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex;
	}

	public static boolean isApresentaQtdPreCarregadaDeVendaNoItemDoPedido() {
		return apresentaQtdPreCarregadaDeVendaNoItemDoPedido > 0;
	}

	public static boolean isTodosFiltrosFixosTelaListaProduto() {
		return ValueUtil.isEmpty(filtrosFixoTelaListaProduto) || ValueUtil.VALOR_NAO.equals(filtrosFixoTelaListaProduto);
	}

	public static boolean isTodosFiltrosNaTelaAvancadoListaProduto() {
		return "0".equals(filtrosFixoTelaListaProduto);
	}

	public static boolean isTodosFiltrosFixosTelaItemPedido() {
		return ValueUtil.isEmpty(filtrosFixoTelaItemPedido) || ValueUtil.VALOR_NAO.equals(filtrosFixoTelaItemPedido);
	}

	public static boolean isTodosFiltrosNaTelaAvancadoItemPedido() {
		return "0".equals(filtrosFixoTelaItemPedido);
	}
	
	public static boolean isUsaFiltroProdutoPorTabelaPrecoSelecionada() {
		return ValueUtil.VALOR_SIM.equals(usaFiltroProdutoPorTabelaPreco) || "1".equals(usaFiltroProdutoPorTabelaPreco);
	}
	
	public static boolean isUsaFiltroProdutoPorTabelaPrecoDisponivel() {
		return "2".equals(usaFiltroProdutoPorTabelaPreco);
	}
	
	public static boolean isUsaFiltroProdutoPorTabelaPreco() {
		return isUsaFiltroProdutoPorTabelaPrecoSelecionada() || isUsaFiltroProdutoPorTabelaPrecoDisponivel();
	}

	public static boolean isUsaFiltroProdutoPorTabelaPrecoTipo3() {
		return "3".equals(usaFiltroProdutoPorTabelaPreco);
	}

	public static boolean isUsaFiltraProdPromoAposCampoBusca() {
		return "3".equals(usaFiltroProdutoPromocional);
	}
	
	public static boolean isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido() {
		return ("2".equals(usaFiltroProdutoPromocional) || "3".equals(usaFiltroProdutoPromocional)) && (isUsaFiltroProdutoPorTabelaPreco() || isUsaFiltroProdutoPorTabelaPrecoTipo3());
	}

	public static boolean isUsaFiltraPorProdutosPromocionalItemPedido() {
		return isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido() || ((ValueUtil.getBooleanValue(usaFiltroProdutoPromocional) || "1".equals(usaFiltroProdutoPromocional) || "3".equals(usaFiltroProdutoPromocional)) && (isUsaFiltroProdutoPorTabelaPreco() || isUsaFiltroProdutoPorTabelaPrecoTipo3()));
	}

	public static boolean isUsaFiltroProdutoDescPromocionalTelaListaProduto() {
		return "2".equals(usaFiltroProdutoDescPromocional) && isUsaFiltroProdutoPorTabelaPreco();
	}
	
	public static boolean isUsaFiltroProdutoDescPromocionalTipoComboBox() {
		return "3".equals(usaFiltroProdutoDescPromocional);
	}

	public static boolean isUsaFiltroProdutoDescPromocional() {
		return ("2".equals(usaFiltroProdutoDescPromocional) || ValueUtil.getBooleanValue(usaFiltroProdutoDescPromocional) || "1".equals(usaFiltroProdutoDescPromocional)) && isUsaFiltroProdutoPorTabelaPreco();
	}

	public static boolean isMostraProdutoDescPromocionalDestacadoTelaListaProduto() {
		return "2".equals(mostraProdutoDescPromocionalDestacado) && isUsaFiltroProdutoPorTabelaPreco();
	}

	public static boolean isMostraProdutoDescPromocionalDestacadoTelaListaProdutoTipo3() {
		return "2".equals(mostraProdutoDescPromocionalDestacado) && isUsaFiltroProdutoPorTabelaPrecoTipo3();
	}

	public static boolean isMostraProdutoDescPromocionalDestacadoTelaItemPedido() {
		return ("2".equals(mostraProdutoDescPromocionalDestacado) || ValueUtil.getBooleanValue(mostraProdutoDescPromocionalDestacado) || "1".equals(mostraProdutoDescPromocionalDestacado)) && isUsaFiltroProdutoPorTabelaPreco();
	}

	public static boolean isMostraProdutoDescPromocionalDestacadoTelaItemPedidoTipo3() {
		return ("2".equals(mostraProdutoDescPromocionalDestacado) || ValueUtil.getBooleanValue(mostraProdutoDescPromocionalDestacado) || "1".equals(mostraProdutoDescPromocionalDestacado)) && isUsaFiltroProdutoPorTabelaPrecoTipo3();
	}


	public static boolean isMostraProdutoPromocionalDestacadoTelaItemPedido() {
		return (isDestaqueNaListaDeProdutosDaTelaDeAdicionarItemPedidoETelaEstiloDesktop() || isMostraProdutoPromocionalDestacadoTelaListaProduto() || isDestaqueNaListaDeItensInseridosNoPedido()) ;
	}
	
	public static boolean isDestaqueNaListaDeProdutosDaTelaDeAdicionarItemPedidoETelaEstiloDesktop() {
		return isMostraProdutoPromocionalDestacado("1")  ;
	}
	
	public static boolean isMostraProdutoPromocionalDestacadoTelaListaProduto() {
		return isMostraProdutoPromocionalDestacado("2")  ;
	}
	
	public static boolean isDestaqueNaListaDeItensInseridosNoPedido() {
		return isMostraProdutoPromocionalDestacado("3");
	}

	public static boolean isLiberaComSenhaClienteRedeAtrasadoNovoPedido() {
		return liberaComSenhaClienteRedeAtrasadoNovoPedido > 0;
	}

	public static boolean isMostraColunaQtdNaListaDePedidos() {
		return ValueUtil.isNotEmpty(mostraColunaQtdNaListaDePedidos) && !ValueUtil.VALOR_NAO.equals(mostraColunaQtdNaListaDePedidos);
	}

	public static boolean isMostraColunaQtdEmbalagensNaListaDePedidos() {
		return ValueUtil.isNotEmpty(mostraColunaQtdEmbalagensNaListaDePedidos) && !ValueUtil.VALOR_NAO.equals(mostraColunaQtdEmbalagensNaListaDePedidos);
	}

	public static boolean isPermiteAlterarVlItemNaUnidadeElementar() {
		return permiteAlterarVlItemNaUnidadeElementar && (usaUnidadeAlternativa || usaNuConversaoUnidadePorItemTabelaPreco);
	}

	public static boolean isColetaDadosGpsRepresentante() {
		return StatusGpsService.getInstance().isPlataformaSuportadaGps() && nuIntervaloColetaGpsRepresentante > 0 && ! isUsaColetaGpsAppExterno();
	}

	public static boolean isUsaColetaGpsPontosEspecificosSistema() {
		return StatusGpsService.getInstance().isPlataformaSuportadaGps() && usaColetaGpsPontosEspecificosSistema && ! isUsaColetaGpsAppExterno();
	}

	public static boolean usaIntervaloErroColetaGpsPontosEspecificosSistema() {
		return nuIntervaloErroColetaGpsPontosEspecificosSistema > 0;
	}
	
	public static boolean usaMaxTempoErroColetaGpsPontosEspecificosSistema() {
		return nuMaxTempoErroColetaGpsPontosEspecificosSistema > 0;
	}

	public static boolean usaNuDiasPermanenciaMetas() {
		return nuDiasPermanenciaMetas > 0;
	}

	public static boolean isUsaIndicacaoMotivoDescPedido() {
		return ValueUtil.VALOR_SIM.equals(usaIndicacaoMotivoDescPedido) || ValueUtil.getIntegerValue(usaIndicacaoMotivoDescPedido) > 0;
	}

	public static boolean isUsaDiasUteisContabilizarDiasAtrazoCliente() {
		return ValueUtil.VALOR_SIM.equals(usaDiasUteisContabilizarDiasAtrazoCliente) || ValueUtil.getIntegerValue(usaDiasUteisContabilizarDiasAtrazoCliente) > 0;
	}

	public static boolean isUsaMotivoAtrasoClienteAtrasado() {
		return !ValueUtil.VALOR_SIM.equals(usaMotivoAtrasoClienteAtrasado) && !ValueUtil.VALOR_NAO.equals(usaMotivoAtrasoClienteAtrasado) && ValueUtil.isNotEmpty(usaMotivoAtrasoClienteAtrasado);
	}
	
	public static boolean isConsisteConversaoUnidades() {
		return "S".equals(consisteConversaoUnidades) || "1".equals(consisteConversaoUnidades) || "2".equals(consisteConversaoUnidades);
	}

	public static boolean isPermiteArrendondarCasoItemNaoAlcanceConversao() {
		return "2".equals(consisteConversaoUnidades) && !usaFatorCUMEspecialClienteCreditoAntecipado && !consisteConversaoUnidadesMultiploEspecial();
	}
	
	public static boolean consisteConversaoUnidadesMultiploEspecial() {
		return isAtributoJsonLigado(consisteConversaoUnidadesMultiploEspecial, "usa", ValorParametro.CONSISTECONVERSAOUNIDADESMULTIPLOESPECIAL);
	}

	public static boolean consisteConversaoUnidadesIgnoraMultiploEspecial() {
		return consisteConversaoUnidadesMultiploEspecial() && isAtributoJsonLigado(consisteConversaoUnidadesMultiploEspecial, "ignoraMultiploEspecial", ValorParametro.CONSISTECONVERSAOUNIDADESMULTIPLOESPECIAL);
	}

	public static boolean consisteConversaoUnidadesIgnoraEstoqueQtdDesejada() {
		return consisteConversaoUnidadesMultiploEspecial() && isAtributoJsonLigado(consisteConversaoUnidadesMultiploEspecial, "ignoraEstoqueQtdDesejada", ValorParametro.CONSISTECONVERSAOUNIDADESMULTIPLOESPECIAL);
	}

	public static boolean consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa() {
		return consisteConversaoUnidadesMultiploEspecial() && isAtributoJsonLigado(consisteConversaoUnidadesMultiploEspecial, "multiploEspecialUnidadeAlternativa", ValorParametro.CONSISTECONVERSAOUNIDADESMULTIPLOESPECIAL);
	}
	
	public static boolean isUsaMultiploEspecialProduto() {
		return LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() || LavenderePdaConfig.aplicaMultiploEspecialAutoItemPedido || LavenderePdaConfig.usaControleEspecialEdicaoUnidades;
	}

	public static int getConfigDescricaoEntidadesCliente() {
		String paramValue = configDescricaoEntidadesCliente;
		if (ValueUtil.VALOR_NAO.equals(paramValue)) {
			return 0;
		}
		return ValueUtil.getIntegerValue(paramValue);
	}

	public static boolean isUsaRegistroPagamentoParaClienteAtrasado() {
		return isUsaModuloPagamento() && usaRegistroPagamentoParaClienteAtrasado;
	}

	public static boolean isUsaVendaRelacionada() {
		return ValueUtil.isNotEmpty(usaVendaRelacionada) && (ValueUtil.VALOR_SIM.equals(usaVendaRelacionada) || "1".equals(usaVendaRelacionada) || "2".equals(usaVendaRelacionada));
	}

	public static boolean isValidaProdNaoInseridosHistAoFecharPedEmLote() {
		return !ValueUtil.VALOR_SIM.equals(naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote)
				&& !"1".equals(naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote)
				&& !"3".equals(naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote);
	}

	public static boolean isValidaProdNaoInseridosHistAoFecharPedido() {
		return !"2".equals(naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote)
				&& !"3".equals(naoValidaProdutosNaoInseridosHistoricoFecharPedidoLote);
	}

	public static boolean isUsaEnvioPontoGpsBackground() {
		return nuIntervaloEnvioPontoGpsBackground > 0;
	}

	private static boolean isUsaEstoqueDisponivelItemPedido() {
		return ValueUtil.VALOR_SIM.equals(usaEstoqueDisponivelItemSugestao) || "1".equals(usaEstoqueDisponivelItemSugestao) || "3".equals(usaEstoqueDisponivelItemSugestao);
	}
	
	public static boolean isUsaSugestaoParaNovoPedido() {
		String vlParam = getValorAtributoJson(configSugestaoParaNovoPedido, "usa", ValorParametro.CONFIGSUGESTAOPARANOVOPEDIDO);
		return ValueUtil.VALOR_SIM.equals(vlParam) || "1".equals(vlParam) || "3".equals(vlParam);
	}
	
	public static boolean isUsaEstoqueDisponivelItemPedidoSugestao() {
		return isUsaSugestaoParaNovoPedido() && isUsaEstoqueDisponivelItemPedido();
	}
	
	public static boolean isIgnoraPedidoCanceladoSugestaoNovoPedido() {
		return isAtributoJsonLigado(configSugestaoParaNovoPedido, "ignoraPedidoCancelado", ValorParametro.CONFIGSUGESTAOPARANOVOPEDIDO);
	}
	
	public static boolean isUsaEstoqueDisponivelItemPedidoReplicacao() {
		return LavenderePdaConfig.isUsaReplicacaoPedido() && isUsaEstoqueDisponivelItemPedido();
	}

	public static boolean isUsaConfirmacaoVerbaPedidoSugestao() {
		return isUsaSugestaoParaNovoPedido() && usaConfirmacaoVerbaPedidoSugestao && (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao);
	}
	
	public static boolean isUsaConfirmacaoVerbaReplicacaoPedido() {
		return LavenderePdaConfig.isUsaReplicacaoPedido() && usaConfirmacaoVerbaPedidoSugestao && (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0);
	}

	public static boolean isAplicaMaiorDescontoNoItemPedido() {
		return ValueUtil.isNotEmpty(aplicaMaiorDescontoNoItemPedido) && ("1".equals(aplicaMaiorDescontoNoItemPedido) || "2".equals(aplicaMaiorDescontoNoItemPedido) || "3".equals(aplicaMaiorDescontoNoItemPedido) || "4".equals(aplicaMaiorDescontoNoItemPedido));
	}
	
	public static boolean isSemQuantidadeFracionada(String campoQtdConfig) {
		if (isUsaQtdInteiro()) {
			String[] camposSemQuantidadeFracionada = nuCamposExibidosSemQuantidadeFracionada.split(";");
			for (int i = 0; i < camposSemQuantidadeFracionada.length; i++) {
				if (campoQtdConfig.equals(camposSemQuantidadeFracionada[i])) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	
	public static boolean isUsaTodasInformacoesAdicionais() {
		return isUsaCentroCustoInformacoesAdicionais() && isUsaItemContaInformacoesAdicionais() && isUsaClasseValorInformacoesAdicionais() && isUsaModoFaturamentoInformacoesAdicionais();
	}

	public static boolean isUsaCentroCustoInformacoesAdicionais() {
		return getValorAtributoJson(configInfoComplementarPedido, "listaCampos", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO).contains("cdCentroCusto") || isUsaPlataformaVendaInformacoesAdicionais();
	}

	public static boolean isUsaPlataformaVendaInformacoesAdicionais() {
		return getValorAtributoJson(configInfoComplementarPedido, "listaCampos", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO).contains("cdPlataformaVenda");
	}

	public static boolean isUsaItemContaInformacoesAdicionais() {
		return getValorAtributoJson(configInfoComplementarPedido, "listaCampos", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO).contains("cdItemConta");
	}

	public static boolean isUsaClasseValorInformacoesAdicionais() {
		return getValorAtributoJson(configInfoComplementarPedido, "listaCampos", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO).contains("cdClasseValor");
	}
	
	public static boolean isUsaModoFaturamentoInformacoesAdicionais() {
		return getValorAtributoJson(configInfoComplementarPedido, "listaCampos", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO).contains("cdModoFaturamento");
	}

	public static boolean isUsaFiltroProdutosPorCentroCusto() {
		return isAtributoJsonLigado(configInfoComplementarPedido, "filtraProdutosPorCentroCusto", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO);
	}
	
	public static boolean isOcultaTodosAcessosRelUltimosPedidos() {
		return "S".equals(ocultaAcessoRelUltimosPedidos) || "1".equals(ocultaAcessoRelUltimosPedidos); 
	}
	
	public static boolean isOcultaAcessosRelUltimosPedidosMenuInferior() {
		return isOcultaTodosAcessosRelUltimosPedidos() || "2".equals(ocultaAcessoRelUltimosPedidos); 
	}

	public static boolean isUsaQtdItemPedidoFisicoInteiro() {
		return ValueUtil.VALOR_SIM.equals(usaQtdItemPedidoInteiro) || "1".equals(usaQtdItemPedidoInteiro);
	}

	public static boolean isUsaQtdItemPedidoFaturamentoInteiro() {
		return ValueUtil.VALOR_SIM.equals(usaQtdItemPedidoInteiro) || "2".equals(usaQtdItemPedidoInteiro);
	}
	
	public static boolean isUsaQtdInteiro() {
		return isUsaQtdItemPedidoFisicoInteiro() || isUsaQtdItemPedidoFaturamentoInteiro();
	}
	
	public static boolean isGeraParcelasPorTipoCondPgto() {
		return ValueUtil.isNotEmpty(geraParcelasPorTipoCondPgto) && !ValueUtil.VALOR_NAO.equalsIgnoreCase(geraParcelasPorTipoCondPgto);
	}

	public static boolean isGeraParcelasEmPercentual() {
		return "2".equalsIgnoreCase(LavenderePdaConfig.geraParcelasPorTipoCondPgto);
	}
	
	public static boolean isPermiteDecidirModoRegistroFaltaEstoqueProduto() {
    	return  !ValueUtil.VALOR_NAO.equals(permiteDecidirModoRegistroFaltaEstoqueProduto);
	}
	
	public static boolean isAvisaPedidoAbertoFechado() {
		return "1".equals(avisaPedidoAbertoFechado);
	}
	
	public static boolean isAvisaPedidoAbertoFechadoEntrarSairSync() {
		return "2".equals(avisaPedidoAbertoFechado) || isAvisaPedidoAbertoFechado();
	}
	
	public static boolean isAvisaPedidoAbertoFechadoEntrarSairSistema() {
		return "3".equals(avisaPedidoAbertoFechado) || isAvisaPedidoAbertoFechado();
	}
	
	public static boolean isAvisaPedidoAbertoFechadoFecharPedido() {
		return "4".equals(avisaPedidoAbertoFechado) || isAvisaPedidoAbertoFechado();
	}
	
	public static boolean isUsaPctFretePorTipoPedidoTabPrecoEPeso() {
		return "S".equals(usaPctFretePorTipoPedidoTabPrecoEPeso) || "1".equals(usaPctFretePorTipoPedidoTabPrecoEPeso) || "2".equals(usaPctFretePorTipoPedidoTabPrecoEPeso) || "3".equals(usaPctFretePorTipoPedidoTabPrecoEPeso); 
	}
	
	public static boolean usaCameraParaLeituraCdBarras() {
		return usaCameraParaLeituraCdBarras && (VmUtil.isAndroid() || VmUtil.isIOS());
	}

	public static boolean isUsaCalculoIpiItemPedido() {
		return usaCalculoDeTributacaoPersonalizado || ("S".equals(calculaIpiItemPedido) || "1".equals(calculaIpiItemPedido) || "2".equals(calculaIpiItemPedido)); 
	}
	public static boolean isPermiteRegistrarNovasFotosDeCliente() {
		return ValueUtil.VALOR_SIM.equals(permiteRegistrarExcluirFotoCliente) || "1".equals(permiteRegistrarExcluirFotoCliente) || "2".equals(permiteRegistrarExcluirFotoCliente);
	}
	public static boolean isPermiteExcluirFotosDeCliente() {
		return ValueUtil.VALOR_SIM.equals(permiteRegistrarExcluirFotoCliente) || "1".equals(permiteRegistrarExcluirFotoCliente) || "3".equals(permiteRegistrarExcluirFotoCliente);
	}
	
	public static boolean isUsaCalculoStItemPedido() {
		return usaCalculoDeTributacaoPersonalizado || isUsaCalculaStItemPedido();
	}
	
	public static boolean isUsaCadastroCoordenadasGeograficasCliente() {
		return isColetaAutomaticamenteCoordenadasGeograficasCliente() || isUsaCadastroCoordenadasGeograficasClienteSemColetaAutomatica() || isUsaCadastroCoordenadasGeograficasSomenteMenuCliente();
	}
	
	public static boolean isColetaAutomaticamenteCoordenadasGeograficasCliente() {
		return "1".equals(usaCadastroCoordenadasGeograficasCliente);
	}
	
	public static boolean isUsaCadastroCoordenadasGeograficasClienteSemColetaAutomatica() {
		return "2".equals(usaCadastroCoordenadasGeograficasCliente);
	}
	
	public static boolean isUsaCadastroCoordenadasGeograficasSomenteMenuCliente() {
		return "3".equals(usaCadastroCoordenadasGeograficasCliente);
	}
	
	public static boolean isUsaDescontoMaximoItemPorCliente() {
		return "1".equals(usaDescontoMaximoItemPorCliente) || "2".equals(usaDescontoMaximoItemPorCliente);
	}
	
	public static boolean isApresentaDescontoMaximoItemPorCliente() {
		return "1".equals(usaDescontoMaximoItemPorCliente);
	}
	
	private static boolean isUsaDescontoAcrescimoMaximoEmValor() {
		return "1".equals(usaDescontoAcrescimoMaximoEmValor) && !isUsaDescontoMaximoItemPorCliente();
	}
	
	public static boolean isUsaDescontoMaximoEmValor() {
		return "2".equals(usaDescontoAcrescimoMaximoEmValor) || isUsaDescontoAcrescimoMaximoEmValor();
	}
	
	public static boolean isUsaAcrescimoMaximoEmValor() {
		return "3".equals(usaDescontoAcrescimoMaximoEmValor) || isUsaDescontoAcrescimoMaximoEmValor();
	}
	
	public static boolean isMostraValorDaUnidadePrecoPorEmbalagem() {
		return ValueUtil.isNotEmpty(mostraValorDaUnidadePrecoPorEmbalagem) && !ValueUtil.VALOR_NAO.equals(mostraValorDaUnidadePrecoPorEmbalagem);
	}
	
	protected static boolean isOrdemCamposValida(String ordemCampos) {
		if (ValueUtil.isEmpty(ordemCampos)) {
			return false;
		}
		String[] str = ordemCampos.split(";");
		return ValueUtil.isNotEmpty(str) && ValueUtil.getIntegerValue(str[0]) > 0;
	}
	
	private static void setValorTimeoutOpenConexaoPda(int value) {
		if (value == 0) {
			value = 3;
		}
		Session.valorTimeoutOpenHttpConnection = value * 1000;
	}
	
	private static void setValorTimeoutReadConexaoPda(int value) {
		if (value == 0) {
			value = 25;
		}
		Session.valorTimeoutReadHttpConnection = value * 1000;
	}
	
	public static boolean usaConfigModuloRiscoChurn() {
		return isAtributoJsonLigado(configModuloRiscoChurn, "usa", ValorParametro.CONFIGMODULORISCOCHURN);
		}

	public static int nuDiasObrigarMotivoRiscoChurn() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configModuloRiscoChurn, "nuDiasObrigarMotivoRiscoChurn", ValorParametro.CONFIGMODULORISCOCHURN));
	}
	
	public static boolean isObrigaRelacionamentoEntreCargaEPedido() {
		return ValueUtil.VALOR_SIM.equals(usaCargaPedidoPorRotaEntregaDoCliente) || "1".equals(usaCargaPedidoPorRotaEntregaDoCliente);
	}
		
	public static boolean isUsaCargaPedidoPorRotaEntregaDoCliente() {
		return isObrigaRelacionamentoEntreCargaEPedido() || "2".equals(usaCargaPedidoPorRotaEntregaDoCliente);
	}
	
	public static boolean isValidaPesoMaximoCargaPedido() {
		return isConfigCalculoPesoPedido() && isUsaCargaPedidoPorRotaEntregaDoCliente() && qtdPesoMaximoCargaPedido > 0;
	}
	
	public static boolean isValidaPesoMinimoCargaPedido() {
		return isConfigCalculoPesoPedido() && isUsaCargaPedidoPorRotaEntregaDoCliente() && qtdPesoMinimoCargaPedido > 0;
	}
	
	public static boolean isOrdenaCondPagtoPedidoPorDiasMedioPagtoOnCombo() {
		return isAtributoJsonLigado(configDiasMedioCondPagto, "ordenaComboCondPagtoPedido", ValorParametro.CONFIGDIASMEDIOCONDPAGTO);
		}
	
	public static boolean isOrdenaCondPagtoPedidoPorDiasMedioPagtoOnRel() {
		return isAtributoJsonLigado(configDiasMedioCondPagto, "ordenaRelPrecoCondPagto", ValorParametro.CONFIGDIASMEDIOCONDPAGTO);
		}
	
	public static boolean isOrdenaCondPagtoPedidoPorDiasMedioPagtoExibeDiasMedio() {
		return isAtributoJsonLigado(configDiasMedioCondPagto, "exibeDiasMedioPagtoPedido", ValorParametro.CONFIGDIASMEDIOCONDPAGTO);
		}
	
	public static boolean isUsaControleRentabilidadePorFaixa() {
		return usaControleRentabilidadePorFaixa == 1 || usaControleRentabilidadePorFaixa == 2 || usaControleRentabilidadePorFaixa == 3;
	}
	
	public static boolean isBloqueiaFechamentoPedidoRentabilidadeMinima() {
		return usaControleRentabilidadePorFaixa == 3;
	}
	
	public static boolean isObrigaUsaControleDataEntregaPedidoPelaCarga() {
		return ValueUtil.VALOR_SIM.equals(usaControleDataEntregaPedidoPelaCarga) || "1".equals(usaControleDataEntregaPedidoPelaCarga);
	}
	
	public static boolean isUsaControleDataEntregaPedidoPelaCarga() {
		return isObrigaUsaControleDataEntregaPedidoPelaCarga() || "2".equals(usaControleDataEntregaPedidoPelaCarga);
	}

	public static boolean isValidaDescMaxMesmoComDescQuantidadeEDescontoGrupo() {
		return validaDescMaxMesmoComDescQuantidadeEDescontoGrupo.equals("1") || validaDescMaxMesmoComDescQuantidadeEDescontoGrupo.equals("2");
	}
	
	public static boolean isNivelGrupoProd2() {
    	return LavenderePdaConfig.usaFiltroGrupoProduto == 0 || LavenderePdaConfig.usaFiltroGrupoProduto > 1; 
    }

    public static boolean isNivelGrupoProd3() {
    	return LavenderePdaConfig.usaFiltroGrupoProduto == 0 || LavenderePdaConfig.usaFiltroGrupoProduto > 2; 
    }
    
    public static boolean isNivelGrupoProd4() {
    	return LavenderePdaConfig.usaFiltroGrupoProduto == 0 || LavenderePdaConfig.usaFiltroGrupoProduto > 3;
    }
    
	public static boolean isUsaFiltroPrincipioAtivoNoProduto() {
		return LavenderePdaConfig.usaFiltroPrincipioAtivoNoProduto == 1 || LavenderePdaConfig.usaFiltroPrincipioAtivoNoProduto == 2;
	}

    public static boolean isUsaDescontaComissaoRentabilidadePorItem() {
		return isUsaDescontaComissaoRentabilidadePorItemTipo1() || isUsaDescontaComissaoRentabilidadePorItemTipo2();
    }

	public static boolean isUsaDescontaComissaoRentabilidadePorItemTipo1() {
		return ValueUtil.VALOR_SIM.equals(descontaComissaoRentabilidadePorItem) || "1".equals(descontaComissaoRentabilidadePorItem);
	}

	public static boolean isUsaDescontaComissaoRentabilidadePorItemTipo2() {
		return "2".equals(descontaComissaoRentabilidadePorItem);
	}

	public static boolean isPermitePedidoAVistaClienteBloqueadoAtrasado() {
		String[] str = getPermitePedidoAVistaClienteBloqueadoAtrasado();
		for (String a: str) {
			if ("S".equals(a) || "1".equals(a)) {
				return true;
			}
		}
		return false;
	}

	private static String[] getPermitePedidoAVistaClienteBloqueadoAtrasado() {
		try {
			return permitePedidoAVistaClienteBloqueadoAtrasado.split(";");
		} catch (Throwable ex) {
			return new String[] {ValueUtil.VALOR_NAO};
		}
	}
	
	public static boolean isPermitePedidoAVistaClienteBloqueado() {
		if (isPermitePedidoAVistaClienteBloqueadoAtrasado()) {
			return true;
		}
		String[] str = getPermitePedidoAVistaClienteBloqueadoAtrasado();
		for (String a: str) {
			if ("2".equals(a)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPermitePedidoAVistaClienteAtrasado() {
		if (isPermitePedidoAVistaClienteBloqueadoAtrasado()) {
			return true;
		}
		String[] str = getPermitePedidoAVistaClienteBloqueadoAtrasado();
		for (String a: str) {
			if ("3".equals(a)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPermitePedidoAVistaClienteBloqueadoPorAtraso() {
		if (isPermitePedidoAVistaClienteBloqueadoAtrasado()) {
			return true;
		}
		String[] str = getPermitePedidoAVistaClienteBloqueadoAtrasado();
		for (String a: str) {
			if ("4".equals(a)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPermitePedidoAVistaApenasClienteBloqueadoPorAtraso() {
		String[] str = getPermitePedidoAVistaClienteBloqueadoAtrasado();
		for (String a: str) {
			if ("4".equals(a)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCalculaSeguroNoItemPedido() {
		return vlIndiceSeguroCalculadoNoItemPedido != 0;
	}
    
    public static boolean isUsaCadastroFotoNovoCliente() {
    	return ValueUtil.VALOR_SIM.equals(getUsaCadastroFotoNovoCliente()) || "1".equals(getUsaCadastroFotoNovoCliente()) || "2".equals(getUsaCadastroFotoNovoCliente());
    }
    
    public static String getUsaCadastroFotoNovoCliente() {
		return getValorAtributoJson(configCadastroFotoNovoCliente, "usa", ValorParametro.CONFIGCADASTROFOTONOVOCLIENTE);
    }
    
	public static boolean isUsaCadastroNovoCliente() {
		return isUsaCadastroNovoClientePessoaFisicaJuridica() || isUsaCadastroNovoClienteApenasPessoaFisica() || isUsaCadastroNovoClienteApenasPessoaJuridica();
	}

	public static boolean isUsaCadastroNovoClientePessoaFisicaJuridica() {
		String vlParam = getValorAtributoJson(cadastroNovoCliente, "usa", ValorParametro.CADASTRONOVOCLIENTE);
		return ValueUtil.VALOR_SIM.equals(vlParam) || "1".equals(vlParam);
		}
    
    public static boolean isUsaCadastroNovoClienteApenasPessoaJuridica() {
		String vlParam = getValorAtributoJson(cadastroNovoCliente, "usa", ValorParametro.CADASTRONOVOCLIENTE);
		return "2".equals(vlParam);
		}
    
    public static boolean isUsaCadastroNovoClienteApenasPessoaFisica() {
		String vlParam = getValorAtributoJson(cadastroNovoCliente, "usa", ValorParametro.CADASTRONOVOCLIENTE);
		return "3".equals(vlParam);
		}
	
	public static String getNmCampoValidaDataNovoCliente() {
		if (isUsaCadastroNovoCliente()) {
			return getValorAtributoJson(cadastroNovoCliente, "nmCampoValidaData", ValorParametro.CADASTRONOVOCLIENTE);
		}
		return ValueUtil.VALOR_NI;
	}
	
	public static int getNuDataAnoMinCadastroNovoCliente() {
		if (isUsaCadastroNovoCliente()) {
			return ValueUtil.getIntegerValue(getValorAtributoJson(cadastroNovoCliente, "nuDataAnoMin", ValorParametro.CADASTRONOVOCLIENTE));
		}
		return 0;
	}
    
    public static boolean sugereRegistroChegadaNoMenuCliente() {
    	return usaRegistroChegadaSaidaClienteVisita && ("1".equals(usaSugestaoRegistroChegadaNoCliente) || "2".equals(usaSugestaoRegistroChegadaNoCliente));
    }
    
    public static boolean sugereRegistroChegadaNoMenuPedido() {
    	return usaRegistroChegadaSaidaClienteVisita && ("1".equals(usaSugestaoRegistroChegadaNoCliente) || "3".equals(usaSugestaoRegistroChegadaNoCliente) || "4".equals(usaSugestaoRegistroChegadaNoCliente));
    }
    
    public static boolean sugereRegistroSaidaNoMenuCliente() {
    	Vector mensagensVisiveis = new Vector(StringUtil.split(LavenderePdaConfig.usaSugestaoRegistroSaidaNoCliente, ';'));
    	return (mensagensVisiveis.indexOf("1") != -1 || mensagensVisiveis.indexOf("S") != -1) && !registraSaidaClienteAoFecharPedido;
    }
    
    public static boolean sugereRegistroSaidaAoFecharPedido() {
    	Vector mensagensVisiveis = new Vector(StringUtil.split(LavenderePdaConfig.usaSugestaoRegistroSaidaNoCliente, ';'));
    	return (mensagensVisiveis.indexOf("2") != -1 || mensagensVisiveis.indexOf("S") != -1) && !registraSaidaClienteAoFecharPedido;
    }
    
    public static boolean isFiltraItemTabelaPrecoListaProduto() {
    	String vlParametro = getMostraColunaPrecoNaListaDeProdutos();
    	return ValueUtil.VALOR_SIM.equals(vlParametro) || "1".equals(vlParametro) && !usaPrecoPadraoProdutoParaSerExibidoNaLista();
    }
    
    public static String getMostraColunaPrecoNaListaDeProdutos() {
    	String vlParametro[] = mostraColunaPrecoNaListaDeProdutos.split(",");
    	return vlParametro[0];
    }
    
    public static boolean sugereRegistroSaidaNaListaDeClientes() {
    	Vector mensagensVisiveis = new Vector(StringUtil.split(LavenderePdaConfig.usaSugestaoRegistroSaidaNoCliente, ';'));
    	return (mensagensVisiveis.indexOf("3") != -1 || mensagensVisiveis.indexOf("S") != -1) && !registraSaidaClienteAoFecharPedido;
    }
    
    public static boolean usaPositivacaoAgendaVisitaSemPedido() {
    	return isCarregaMotivoVisitaPositiva() || isPositivaVisitaAutomaticamente() || isPositivaVisitaAutomaticamenteECarregaMotivoVisitaPositiva();
    }
    
    public static boolean isUsaNavegacaoGpsNoMapa() {
    	return "1".equals(usaNavegacaoGpsNoMapa) || "2".equals(usaNavegacaoGpsNoMapa);
    }
    
    public static boolean isCarregaMotivoVisitaPositiva() {
    	return "1".equals(usaPositivacaoAgendaVisitaSemPedido);
    }
    
    public static boolean isPositivaVisitaAutomaticamente() {
    	return "2".equals(usaPositivacaoAgendaVisitaSemPedido) && usaRegistroChegadaSaidaClienteVisita;
    }
    
    public static boolean isPositivaVisitaAutomaticamenteECarregaMotivoVisitaPositiva() {
    	return "3".equals(usaPositivacaoAgendaVisitaSemPedido) && usaRegistroChegadaSaidaClienteVisita;
    }
    
    public static boolean isQtMinEnderecosNovoClienteHabilitado() {
		return isUsaMultiplosEnderecosCadastroNovoCliente() && getQtMinEnderecosCadastroNovoCliente() != 0;
	}
    
    public static boolean isUsaRetornoAutomaticoDadosRelativosPedido() {
    	return !ValueUtil.VALOR_NAO.equals(usaRetornoAutomaticoDadosRelativosPedido);
    }
    
    public static boolean isUsaListaSacCliente() {
    	String valorConfigurado = getValorAtributoJson(usaListaSacCliente, "usa", ValorParametro.CONFIGLISTASACCLIENTE);
    	return ValueUtil.VALOR_SIM.equals(valorConfigurado) || "1".equals(valorConfigurado) || "2".equals(valorConfigurado)|| "3".equals(valorConfigurado);
    }

    public static boolean isPermiteGerirListaSacCliente() {
    	String valorConfigurado = getValorAtributoJson(usaListaSacCliente, "usa", ValorParametro.CONFIGLISTASACCLIENTE);
    	return "3".equals(valorConfigurado);
    }

    public static boolean isPermiteEncerrarOcorrencia() {
    	String valorConfigurado = getValorAtributoJson(usaListaSacCliente, "permiteEncerrarOcorrencia", ValorParametro.CONFIGLISTASACCLIENTE);
    	return ValueUtil.VALOR_SIM.equals(valorConfigurado);
    }

    public static boolean isSempreObrigaInclusaoEnderecoEntrega() {
    	return ValueUtil.VALOR_SIM.equals(getConfigObrigatoriedadeEnderecoEntregaPedido()) || "1".equals(getConfigObrigatoriedadeEnderecoEntregaPedido());
    }
    
    public static boolean isObrigaInclusaoEnderecoEntregaCasoExistaRegistroDeEnderecoEntrega() {
    	return  "2".equals(getConfigObrigatoriedadeEnderecoEntregaPedido());
    }
 
    public static boolean isAplicaAlteracoesCadastroClienteEnderecoAutomaticamente() { 
    	return ValueUtil.isNotEmpty(aplicaAlteracoesCadastroClienteEnderecoAutomaticamente) && !"N".equals(aplicaAlteracoesCadastroClienteEnderecoAutomaticamente);
	}
    public static boolean usaPrecoPadraoProdutoParaSerExibidoNaLista() {
    	return "2".equals(usaPrecoPadraoListaProdutos) || "3".equals(usaPrecoPadraoListaProdutos);
    }
    
    public static boolean isHabilitaOrdenacaoPrecoPadrao() {
    	return !ValueUtil.VALOR_NAO.equals(getMostraColunaPrecoNaListaDeProdutos()) && (ValueUtil.VALOR_SIM.equals(usaPrecoPadraoListaProdutos) || "1".equals(usaPrecoPadraoListaProdutos) || "3".equals(usaPrecoPadraoListaProdutos));
    }
    
    public static boolean isMostraColunaPrecoNaListaDeProdutosDentroPedido() {
    	return ValueUtil.VALOR_SIM.equals(getMostraColunaPrecoNaListaDeProdutos()) || "2".equals(getMostraColunaPrecoNaListaDeProdutos());
    }

	public static boolean isMostraColunaPrecoNaListaDeProdutos() {
		return ValueUtil.VALOR_SIM.equals(getMostraColunaPrecoNaListaDeProdutos()) || "1".equals(getMostraColunaPrecoNaListaDeProdutos());
	}


    public static boolean usaPedidoComplementar() {
    	return usaPedidoComplementar != -1;
    }
    
    public static boolean isObrigaRelacionarPedidoBonificacao() {
    	String valorConfigurado = getValorAtributoJson(relacionaPedidoNaTrocaBonificacao, "usa", ValorParametro.RELACIONAPEDIDONABONIFICACAO);
    	return ValueUtil.valueEquals(valorConfigurado, ValueUtil.VALOR_SIM) 
    		|| ValueUtil.valueEquals(valorConfigurado, "1") 
    		|| ValueUtil.valueEquals(valorConfigurado, "3");
    }
    
    public static boolean isObrigaRelacionarPedidoTroca() {
    	String valorConfigurado = getValorAtributoJson(relacionaPedidoNaTrocaBonificacao, "usa", ValorParametro.RELACIONAPEDIDONABONIFICACAO);
    	return ValueUtil.valueEquals(valorConfigurado, "2") || ValueUtil.valueEquals(valorConfigurado, "3");
    }
    
	public static boolean isPermiteRelacionarPedidoNaBonificacao() {
		return ValueUtil.valueEquals(getValorAtributoJson(relacionaPedidoNaTrocaBonificacao, "usa", ValorParametro.RELACIONAPEDIDONABONIFICACAO), "4");
	}

    public static boolean isUsaVariosPedidosBonificados() {
    	/*
    	 * Essa fun��o ser� refeita na ativiade LAV-21961
    	 * At� que isto ocorra, a pedido do analista Paulo, esta fun��o permanecera desabilitada para todos os clientes
    	 */
    	return false;//isAtributoJsonLigado(relacionaPedidoNaTrocaBonificacao, "usaVariosPedidosRel");
    }

	public static boolean isPermiteReabrirPedidoBonificacao() {
		return isAtributoJsonLigado(relacionaPedidoNaTrocaBonificacao, "permiteReabrirPedidoBonificacao", ValorParametro.RELACIONAPEDIDONABONIFICACAO);
	}

    public static boolean isUsaKitBaseadoNoProduto() {
    	return ValueUtil.VALOR_SIM.equals(usaKitBaseadoNoProduto) || "1".equals(usaKitBaseadoNoProduto) || "2".equals(usaKitBaseadoNoProduto);
    }

    public static boolean isUsaKitBaseadoNoProdutoOcultaQtAtual() {
    	return  "2".equals(usaKitBaseadoNoProduto);
    }
 
	private static String[] getUsaAvisoClienteSemPesquisaMercado() {
		try {
			return usaAvisoClienteSemPesquisaMercado.split(",");
		} catch (Throwable ex) {
			return new String[] {""};
		}
	}
	
	public static boolean isUsaAvisoClienteSemPesquisaMercado() {
		return isUsaAvisoClienteSemPesquisaMercadoFecharPedido() || isUsaAvisoClienteSemPesquisaMercadoCliente() || isUsaAvisoClienteSemPesquisaMercadoNovoPedido();
	}
	
	public static int getNuDiasAvisoClienteSemPesquisaMercado() {
		return ValueUtil.getIntegerValue(getUsaAvisoClienteSemPesquisaMercado()[0]);
	}
	
	public static boolean isUsaAvisoClienteSemPesquisaMercadoFecharPedido() {
    	try {
    		String vlParam = getUsaAvisoClienteSemPesquisaMercado()[1];
    		return isUsaPesquisaMercado() && ("S".equals(vlParam) || "1".equals(vlParam)); 
		} catch (Throwable e) {
			return false;
		}
    } 
	
    public static boolean isUsaAvisoClienteSemPesquisaMercadoCliente() {
    	try {
    		String vlParam = getUsaAvisoClienteSemPesquisaMercado()[1];
    		return isUsaPesquisaMercado() && "2".equals(vlParam); 
    	} catch (Throwable e) {
    		return false;
    	}
    }

    public static boolean isUsaAvisoClienteSemPesquisaMercadoNovoPedido() {
    	try {
    		String vlParam = getUsaAvisoClienteSemPesquisaMercado()[1];
    		return isUsaPesquisaMercado() && "3".equals(vlParam); 
    	} catch (Throwable e) {
    		return false;
    	}
    }
    
    public static boolean isUsaAvisoClienteSemPesquisaMercadoTipoValor() {
    	try {
    		String vlParam = getUsaAvisoClienteSemPesquisaMercado()[2];
    		return PesquisaMercado.CDTIPOPESQUISA_VALOR.equals(vlParam);
    	} catch (Throwable ex) {
    		return true;
    	}
    }
    
    public static boolean isUsaAvisoClienteSemPesquisaMercadoTipoGondola() {
    	try {
    		String vlParam = getUsaAvisoClienteSemPesquisaMercado()[2];
    		return PesquisaMercado.CDTIPOPESQUISA_GONDOLA.equals(vlParam);
    	} catch (Throwable ex) {
    		return true;
    	}
    }
    
	private static boolean isUsaPesquisaMercado() {
		return LavenderePdaConfig.usaPesquisaMercado || isUsaPesquisaMercadoProdutosConcorrentes();
	}
	
    public static boolean isDataEntregaValidaApenasSabado() {
		String[] str = getDominioDataEntregaFinalSemana();
		return ValueUtil.isNotEmpty(str) && "1".equals(str[0]);
	}

	private static String[] getDominioDataEntregaFinalSemana() {
		try {
			String[] str = dominioDataEntregaFinalSemana.split(";");
			return str;
		} catch (NullPointerException ex) {
			return new String[] {""};
		}
	}

    public static boolean isDataEntregaValidaApenasDomingo() {
		String[] str = getDominioDataEntregaFinalSemana();
		return ValueUtil.isNotEmpty(str) && "2".equals(str[0]);
	}

    public static boolean isDataEntregaContabilizandoApenasDiasUteis() {
		String[] str = getDominioDataEntregaFinalSemana();
		return ValueUtil.isNotEmpty(str) && "D".equals(str[0]);
	}

    public static boolean isDataEntregaAjustadaParaSegundaFeira() {
		String[] str = getDominioDataEntregaFinalSemana();
		return ValueUtil.isNotEmpty(str) && "N".equals(str[0]);
	}

    public static boolean isContabilizaDtEmissaoInvalidoNoCalculoEntrega() {
		String[] str = getDominioDataEntregaFinalSemana();
		return ValueUtil.isNotEmpty(str) && str.length > 1 && "A".equals(str[1]);
	}
    
    public static boolean isNaoContabilizaFinalSemanaNoCalculoEntrega() {
    	String[] str = getDominioDataEntregaFinalSemana();
    	return ValueUtil.isNotEmpty(str) && str.length > 1 && "B".equals(str[1]);
    }

    private static String[] getUsaAvisoRentabilidadeItemAbaixoEsperado() {
    	try {
    		return usaAvisoRentabilidadeItemAbaixoEsperado.split(";");
    	} catch (NullPointerException ex) {
    		return new String[] {""};
    	}
    }
    
    public static boolean isAvisoRentabilidadeItemAbaixoEsperadoIncluindoItem() {
    	String[] str = getUsaAvisoRentabilidadeItemAbaixoEsperado();
    	return "S".equals(str[0]) || "2".equals(str[0]) || "1".equals(str[0]);
    }
    
    public static boolean isAvisoRentabilidadeItemAbaixoEsperadoFechandoPedido() {
    	String[] str = getUsaAvisoRentabilidadeItemAbaixoEsperado();
    	return "S".equals(str[0]) || "3".equals(str[0]) || "1".equals(str[0]);
    }

    public static boolean isAvisoRentabilidadeItemAbaixoEsperadoQualquerNivel() {
    	String[] str = getUsaAvisoRentabilidadeItemAbaixoEsperado();
    	return str.length > 1 && "1".equals(str[1]);
    }

    public static boolean isAvisoRentabilidadeItemAbaixoMinimo() {
    	String[] str = getUsaAvisoRentabilidadeItemAbaixoEsperado();
    	return str.length > 1 && "2".equals(str[1]);
    }
    
    public static boolean isIndiceFinanceiroCondPagtoVlItemPedido2ouN() {
    	String aplicaIndiceVlItemPedido = getValorAtributoJson(configIndiceFinanceiroCondPagto, "aplicaIndiceVlItemPedido", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
    	return ValueUtil.valueEquals(ValueUtil.VALOR_NAO, aplicaIndiceVlItemPedido)  || ValueUtil.valueEquals("2", aplicaIndiceVlItemPedido);
    }    
    
    public static boolean isIndiceFinanceiroCondPagtoVlItemPedido() {
    	String aplicaIndiceVlItemPedido = getValorAtributoJson(configIndiceFinanceiroCondPagto, "aplicaIndiceVlItemPedido", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
    	return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, aplicaIndiceVlItemPedido)  || ValueUtil.valueEquals("1", aplicaIndiceVlItemPedido) || ValueUtil.valueEquals("2", aplicaIndiceVlItemPedido);
    } 
    
    public static boolean exibeDescontoAcrescimoIndice() {
    	String aplicaIndiceVlItemPedido = getValorAtributoJson(configIndiceFinanceiroCondPagto, "aplicaIndiceVlItemPedido", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
    	if  (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, aplicaIndiceVlItemPedido)) return false;

    	return isAtributoJsonLigado(configIndiceFinanceiroCondPagto, "exibeDescontoAcrescimoIndice", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
    }
    
    public static boolean usaIndiceCondPagtoClienteConformePrazoMedio() {
    	String aplicaIndiceVlItemPedido = getValorAtributoJson(configIndiceFinanceiroCondPagto, "aplicaIndiceVlItemPedido", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
    	if  (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, aplicaIndiceVlItemPedido)) return false;

    	return isAtributoJsonLigado(configIndiceFinanceiroCondPagto, "usaIndiceCondPagtoClienteConformePrazoMedio", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
    }
    
    public static int getNuCasasDecimaisCalcIndicFinanceiro() {
    	String valor = getValorAtributoJson(configIndiceFinanceiroCondPagto, "nuCasasDecimaisCalcIndicFinanceiro", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO);
		return ValueUtil.isEmpty(valor) ? 0 : ValueUtil.getIntegerValue(valor);
    }

    public static boolean isEnviarEmailPedidoAutoCliente() {
    	List<String> vlParametro = getEnviarEmailPedidoAutoCliente();
    	String param1 = vlParametro.get(0);
    	if (vlParametro.size() == 2) {
			String param2 = vlParametro.get(1);
			return ("S".equalsIgnoreCase(param1) || "1".equalsIgnoreCase(param1) || "2".equalsIgnoreCase(param1)) && OrigemPedido.FLORIGEMPEDIDO_PDA.equalsIgnoreCase(param2);
		} else if (vlParametro.size() == 1) {
			return "S".equalsIgnoreCase(param1) || "1".equalsIgnoreCase(param1) || "2".equalsIgnoreCase(param1);
		}
    	return false;
    }
    
    public static List<String> getEnviarEmailPedidoAutoCliente() {
		try {
			return Arrays.asList(enviarEmailPedidoAutoCliente.split(","));
		} catch (Throwable ex) {
			return Arrays.asList(ValueUtil.VALOR_NAO);
		}
	}

    public static boolean isIndiceFinanceiroCondPagtoMantendoVlItemPedido() {
    	return ValueUtil.valueEquals("2", getValorAtributoJson(configIndiceFinanceiroCondPagto, "aplicaIndiceVlItemPedido", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGTO));
    }
    
    public static boolean isHoraLimiteParaEnvioPedidos() {
    	String horaLimite = getHoraLimiteParaEnvioPedidos();
    	return ValueUtil.isNotEmpty(horaLimite) && !ValueUtil.VALOR_NAO.equals(horaLimite);
    }
    
    public static List<String> getHoraLimiteParaEnvioPedidosList() {
    	List<String> vlParametroList = Arrays.asList(getHoraLimiteParaEnvioPedidos().split(","));
    	List<String> horaLimiteList = new ArrayList<String>();
    	for (String vlParametro : vlParametroList) {
    		horaLimiteList.addAll(Arrays.asList(vlParametro.split("-")));
		}
    	return horaLimiteList;
    }
    
    protected static String getHoraLimiteParaEnvioPedidos() {
    	if (ValueUtil.isNotEmpty(horaLimiteParaEnvioPedidos)) {
    		if (horaLimiteParaEnvioPedidos.indexOf("|") == -1) {
    			return horaLimiteParaEnvioPedidos;
    		}
			return getHoraLimiteParaEnvioPedidosByDiaSemana(ConfigInternoService.getInstance().getDtServidor());
    	}
    	return ValueUtil.VALOR_NI;
    }
    
    public static boolean isVerificaHoraLimiteparaEnvioPedidos() {
    	return !ValueUtil.VALOR_NAO.equals(horaLimiteParaEnvioPedidos);
    }
    
    protected static String getHoraLimiteParaEnvioPedidosByDiaSemana(Date dataServidor) {
    	String horaLimite = "";
    	if (ValueUtil.isNotEmpty(dataServidor)) {
    		int dayOfWeek = dataServidor.getDayOfWeek();
    		String[] horaLimiteList = StringUtil.splitTurbo(horaLimiteParaEnvioPedidos, '|', 9);
			switch (dayOfWeek) {
			case DateUtil.DATA_SEMANA_DOMINGO:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_DOMINGO];
				break;
			case DateUtil.DATA_SEMANA_SEGUNDA:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_SEGUNDA];
				break;
			case DateUtil.DATA_SEMANA_TERCA:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_TERCA];
				break;
			case DateUtil.DATA_SEMANA_QUARTA:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_QUARTA];
				break;
			case DateUtil.DATA_SEMANA_QUINTA:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_QUINTA];
				break;
			case DateUtil.DATA_SEMANA_SEXTA:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_SEXTA];
				break;
			case DateUtil.DATA_SEMANA_SABADO:
				horaLimite = horaLimiteList[DateUtil.DATA_SEMANA_SABADO];
				break;
			}
		}
    	return StringUtil.getStringValue(horaLimite);
    }
    
    private static String usaMultiplasSugestoesProdutoFechamentoPedido() {
    	try {
    		String[] vlParamWebPda = usaMultiplasSugestoesProdutoFechamentoPedido.split(";");
    		return vlParamWebPda.length > 1 ? vlParamWebPda[1] : vlParamWebPda[0];
    	} catch (NullPointerException ex) {
    		return "";
    	}
    }
	   
	private static boolean usaMultiplasSugestoesProdutoFechamentoPedido(String tipo) {
		String[] param = usaMultiplasSugestoesProdutoFechamentoPedido().split(",");
		for (String e: param) {
			if (tipo.equals(e) || ValueUtil.VALOR_SIM.equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean usaMultiplasSugestoesProdutoFechamentoPedidoGiro() {
		return usaMultiplasSugestoesProdutoFechamentoPedido("1");
	}
	
	public static boolean usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento() {
		return usaMultiplasSugestoesProdutoFechamentoPedido("2");
	}
	
	public static boolean usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda() {
		return usaMultiplasSugestoesProdutoFechamentoPedido("3");
	}

	public static int getQtLimiteItensSugestaoListas(boolean geral, String tipo, boolean onFechamento) {
		String[] qtLimite;
		if (geral) {
			if (ValueUtil.isEmpty(qtLimiteItensMultiplasSugestoesListaGeral) || ValueUtil.VALOR_NAO.equals(qtLimiteItensMultiplasSugestoesListaGeral)) {
				return -1;
			}
			if (ValueUtil.VALOR_SIM.equals(qtLimiteItensMultiplasSugestoesListaGeral)) {
				return 0;
			}
			qtLimite = qtLimiteItensMultiplasSugestoesListaGeral.split(",");
		} else {
			if (ValueUtil.isEmpty(qtLimiteItensSugestaoListasEspecificas) || ValueUtil.VALOR_NAO.equals(qtLimiteItensSugestaoListasEspecificas)) {
				return -1;
			}
			if (ValueUtil.VALOR_SIM.equals(qtLimiteItensSugestaoListasEspecificas)) {
				return 0;
			}
			qtLimite = qtLimiteItensSugestaoListasEspecificas.split(",");
		}
		if (qtLimite.length == 1) {
			return ValueUtil.getIntegerValue(qtLimite[0]);
		} else {
			int posicaoQtLimitePorTipoSugestao = getPosicaoQtLimitePorTipoSugestao(tipo, onFechamento);
			if (posicaoQtLimitePorTipoSugestao < 0) {
				return 0;
			}
			try {
				return ValueUtil.getIntegerValue(qtLimite[posicaoQtLimitePorTipoSugestao]);
			} catch (Throwable e) {
				return 0;
			}
		}
	}

	private static int getPosicaoQtLimitePorTipoSugestao(String tipo, boolean onFechamento) {
		String vlParam = onFechamento ? usaMultiplasSugestoesProdutoFechamentoPedido() : usaMultiplasSugestoesProdutoInicioPedido;
		vlParam = ValueUtil.VALOR_SIM.equals(vlParam) ? "1,2,3" : vlParam;
		String[] str = vlParam.split(",");
		for (int i = 0; i < str.length; i++) {
			if (tipo.equals(str[i])) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isBloqueiaNovoPedidoClienteSemRegistroChegada() {
		return usaRegistroChegadaSaidaClienteVisita && "4".equals(LavenderePdaConfig.usaSugestaoRegistroChegadaNoCliente);
	}
    
    public static boolean isUsaPesquisaMercadoProdutosConcorrentes() {
    	return isUsaPesquisaMercadoProdutosConcorrentesTipoValor() || isUsaPesquisaMercadoProdutosConcorrentesTipoGondola();
    }
    
    public static boolean isUsaPesquisaMercadoProdutosConcorrentesTipoValor() {
    	return isTipoUsaPesquisaMercadoProdutosConcorrentes(PesquisaMercado.CDTIPOPESQUISA_VALOR);
    }

    public static boolean isUsaPesquisaMercadoProdutosConcorrentesTipoGondola() {
    	return isTipoUsaPesquisaMercadoProdutosConcorrentes(PesquisaMercado.CDTIPOPESQUISA_GONDOLA);
    }
    
	private static boolean isTipoUsaPesquisaMercadoProdutosConcorrentes(String tipoPesquisa) {
		try {
			String[] vlParams = usaPesquisaMercadoProdutosConcorrentes.split(",");
			for (String dominio : vlParams) {
				if (tipoPesquisa.equalsIgnoreCase(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
		return false;
	}
	
	public static boolean isUsaCadastroCoordenadasGeograficasNovoCliente() {
		return isShowBtCadastroCoordenadasGeograficasNovoCliente() || isCadastroCoordenadasGeograficasNovoClienteAutomatico();
	}

	public static double getResolucaoCameraRegistroFotos() {
		double value = ValueUtil.round(resolucaoCameraRegistroFotos, 1);
		if (value == 0) {
			value = 0.3;
	}
		return value;
	}
	
	public static boolean isShowBtCadastroCoordenadasGeograficasNovoCliente() {
		return "2".equals(usaCadastroCoordenadasGeograficasNovoCliente);
	}

	public static boolean isCadastroCoordenadasGeograficasNovoClienteAutomatico() {
		return "S".equals(usaCadastroCoordenadasGeograficasNovoCliente) || "1".equals(usaCadastroCoordenadasGeograficasNovoCliente);
	}
    
    
	public static boolean usaLocalEstoque() {
		return usaLocalEstoquePorCliente() || usaLocalEstoquePorTabelaPreco();
	}
    
    public static boolean isUsaColetaGpsManual() {
    	return "S".equals(usaColetaGpsManual) && (LavenderePdaConfig.isColetaDadosGpsRepresentante() || isUsaColetaGpsAppExterno());
    }
    
    public static boolean isUsaColetaGpsAppExterno() {
    	return usaAplicativoExternoParaColetaGps && VmUtil.isAndroid();
    }
    
    public static boolean isSistemaPermiteCadastroParaSabadoEDomingo() {
		return ValueUtil.VALOR_SIM.equals(usaAgendaVisitaFinalDeSemana) || "1".equals(usaAgendaVisitaFinalDeSemana);
	}
        
	public static boolean isSistemaPermiteCadastroApenasSabado() {
		return "2".equals(usaAgendaVisitaFinalDeSemana);
	}
	
	public static boolean isAgendaVisitaFinalDeSemanaNaoHabilitado() {
		return !LavenderePdaConfig.isSistemaPermiteCadastroParaSabadoEDomingo() && !LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && !LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo();
	}
	
	public static boolean isSistemaPermiteCadastroApenasDomingo() {
		return "3".equals(usaAgendaVisitaFinalDeSemana);
	}
	
	public static boolean isUsaAgendaVisitaBaseadaNaSemanaDoMes() {
		return "1".equals(usaAgendaVisitaBaseadaNaSemanaDoMes);
	}
	
	public static boolean isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes() {
		return "2".equals(usaAgendaVisitaBaseadaNaSemanaDoMes);
	}
	
	public static int getQtMaxCaracteresOrdemCompraNoPedido() {
		try {
			String[] vlParams = qtMaxCaracteresOrdemCompraNoPedido.split(",");
			int qtMaxCaracteres = vlParams.length > 1 ? ValueUtil.getIntegerValue(vlParams[1]) : ValueUtil.getIntegerValue(vlParams[0]);
			return qtMaxCaracteres < 20 ? qtMaxCaracteres : 20;
		} catch (Throwable ex) {
			return 20;
		}
	}
	
	public static boolean isCreditoIndiceTipoFreteCliNaBonificacao() {
		return usaCreditoIndiceTipoFreteCliNaBonificacao && usaTipoFretePorCliente && aplicaPercentualFreteCalculoPrecoItem;
	}
	
	public static boolean isCreditoIndiceCondicaoPagamentoNaBonificacao() {
		return usaCreditoIndiceCondicaoPagamentoNaBonificacao && isIndiceFinanceiroCondPagtoVlItemPedido();
	}
	
	public static boolean isGeraCreditoIndiceBonificacao() {
		return isCreditoIndiceCondicaoPagamentoNaBonificacao() || isCreditoIndiceTipoFreteCliNaBonificacao();
	}
	
	public static String getConfigValidacaoPrecoItemPorPesoMinimo() {
		return getValorAtributoJson(usaPrecoItemPorPesoMinimoPedido, "usa", ValorParametro.USAPRECOITEMPORPESOMINIMOPEDIDO);
	}

	public static boolean isOrdenaTabelasPrecoPorPesoMinimo() {
		return (isUsaPrecoItemPorPesoMinimoPedido() || isValidaTabelaPrecoFechamentoPedido() || isValidaTabelaPrecoFechamentoItemPedido())
				&& isAtributoJsonLigado(usaPrecoItemPorPesoMinimoPedido, "ordenaTabelasPrecoPorPesoMinimo", ValorParametro.USAPRECOITEMPORPESOMINIMOPEDIDO);
	}

	public static boolean isUsaPrecoItemPorPesoMinimoPedido() {
		return ("1".equals(getConfigValidacaoPrecoItemPorPesoMinimo()) || ValueUtil.VALOR_SIM.equals(getConfigValidacaoPrecoItemPorPesoMinimo())) && isConfigCalculoPesoPedido();
	} 
	
	public static boolean isValidaTabelaPrecoFechamentoPedido() {
		return "2".equals(getConfigValidacaoPrecoItemPorPesoMinimo()) && isConfigCalculoPesoPedido();
	}
	public static boolean isValidaTabelaPrecoFechamentoItemPedido() {
		return "3".equals(getConfigValidacaoPrecoItemPorPesoMinimo()) && isConfigCalculoPesoPedido();
	}
	
	public static boolean mostraVPesoCapaPedido() {
		return isConfigCalculoPesoPedido() && isAtributoJsonLigado(configCalculoPesoPedido, "mostraVPesoCapaPedido", ValorParametro.CONFIGCALCULOPESOPEDIDO);
	}
	
	public static boolean mostraVPesoListaItens() {
		return isConfigCalculoPesoPedido() && isAtributoJsonLigado(configCalculoPesoPedido, "mostraVPesoListaItens", ValorParametro.CONFIGCALCULOPESOPEDIDO);
	}
	
	public static boolean isUsaPrecoItemPorPesoMinimoItemPedido() {
		return isUsaPrecoItemPorPesoMinimoPedido() && (usaListaColunaPorTabelaPreco || permiteTabPrecoItemDiferentePedido());
	}
	
	public static boolean isUsaPrecoItemPorPesoMinimoItem() {
		return isValidaTabelaPrecoFechamentoItemPedido() && (usaListaColunaPorTabelaPreco || permiteTabPrecoItemDiferentePedido());
	}

	public static boolean isBloqueiaDescontoMaiorDescontoProgressivo() {
		return "1".equals(bloqueiaDescontoMaiorDescontoProgressivo) || isBloqueiaDescontoMaiorDescontoProgressivoComValorTruncado();
	}
		
	public static boolean isBloqueiaDescontoMaiorDescontoProgressivoComValorTruncado() {
		return "2".equals(bloqueiaDescontoMaiorDescontoProgressivo);
	}
	
	public static boolean isUsaVerbaSaldoPorFornecedor() {
		return usaVerbaSaldoPorFornecedor && usaVerbaItemPedidoPorItemTabPreco;
	}
	
	public static boolean isUsaGerenciamentoRentabilidade() {
		return "1".equals(usaGerenciamentoRentabilidade);
	}
	
	public static boolean isUsaGerenciamentoRentabilidadeComBaseNaRentabilidadeDoPedido() {
		return "2".equals(usaGerenciamentoRentabilidade);
	}
	
	public static boolean isBloqueiaCondPagtoPorDiasCliente() {
		return isBloqueiaCondPagtoPorDiasMediosCliente() || isBloqueiaCondPagtoPorDiasMaximoCliente();
	}
	
	public static boolean isBloqueiaCondPagtoPorDiasMediosCliente() {
		return "1".equals(bloqueiaCondPagtoPorDiasCliente);
	}
	
	public static boolean isBloqueiaCondPagtoPorDiasMaximoCliente() {
		return "2".equals(bloqueiaCondPagtoPorDiasCliente);
	}
	
	public static boolean isToleranciaVisitasForaAgenda() {
		return isUsaAgendaDeVisitas() && isUsaToleranciaVisitasForaAgenda();
	}
	
	public static boolean isAplicaDescontosEmCascataNoItemPedidoRegra1() {
		return isAplicaDescontosEmCascataComArredondamentoRegra1() || isAplicaDescontosEmCascataSemArredondamentoRegra1();
	}
	
	public static boolean isConfigDescontosEmCascata() {
		return isAplicaDescontosEmCascataComArredondamentoRegra1() || isAplicaDescontosEmCascataSemArredondamentoRegra1() || isAplicaDescontosEmCascataNoItemPedidoRegra2();
	}

	public static boolean isAplicaDescontosEmCascataComArredondamentoRegra1() {
		return comparaValorAtributoJson(configDescontosEmCascata, "tipoRegraDesconto", "1", ValorParametro.CONFIGDESCONTOSEMCASCATA) && comparaValorAtributoJson(configDescontosEmCascata, "modoArredondamento", "1", ValorParametro.CONFIGDESCONTOSEMCASCATA);
	}
	
	public static boolean isAplicaDescontosEmCascataSemArredondamentoRegra1() {
		return comparaValorAtributoJson(configDescontosEmCascata, "tipoRegraDesconto", "1", ValorParametro.CONFIGDESCONTOSEMCASCATA) && comparaValorAtributoJson(configDescontosEmCascata, "modoArredondamento", "2", ValorParametro.CONFIGDESCONTOSEMCASCATA);
	}
	
	public static boolean isAplicaDescontosEmCascataNoItemPedidoRegra2() {
		return comparaValorAtributoJson(configDescontosEmCascata, "tipoRegraDesconto", "2", ValorParametro.CONFIGDESCONTOSEMCASCATA);
	}
	
	public static boolean isArredondaDescontosEmCascataItemAItemRegra2() {
		return comparaValorAtributoJson(configDescontosEmCascata, "modoArredondamento", "1", ValorParametro.CONFIGDESCONTOSEMCASCATA);
	}
	
	public static boolean isArredondaDescontosEmCascataNoFinalRegra2() {
		return comparaValorAtributoJson(configDescontosEmCascata, "modoArredondamento", "2", ValorParametro.CONFIGDESCONTOSEMCASCATA);
	}
	
	public static double getVlPctHabilitaDesconto3() {
		return ValueUtil.getDoubleValue(getValorAtributoJson(configDescontosEmCascata, "vlPctHabilitaDesconto3", ValorParametro.CONFIGDESCONTOSEMCASCATA));
	}
	
	public static boolean isPropagaUltimoDescontoItemPedido() {
		return isPropagaUltimoDescontoItem() || isPropagaUltimoDescontoItemNoPedido();
	}
	
	public static boolean isPropagaUltimoDescontoItem() {
		return "1".equals(propagaUltimoDescontoItemPedido);
	}
	
	public static boolean isPropagaUltimoDescontoItemNoPedido() {
		return "2".equals(propagaUltimoDescontoItemPedido);
	}
	
	public static boolean isPropagaUltimoDescontoAplicadoNoProdutoNesteCliente() {
		return "3".equals(propagaUltimoDescontoItemPedido);
	}
	
	public static boolean isUsaDescontoQtdPorGrupo() {
		return isAtributoJsonLigado(configDescontoQtdPorGrupo, "usa", ValorParametro.CONFIGDESCONTOQTDPORGRUPO);
	}
	
	public static boolean isIgnoraPctMaxDescEmItemDescMaxProdCli() {
		return isUsaDescontoQtdPorGrupo() && isAtributoJsonLigado(configDescontoQtdPorGrupo, "ignoraPctMaxDescEmItemDescMaxProdCli", ValorParametro.CONFIGDESCONTOQTDPORGRUPO);
	}
	
	public static boolean isUsaGrupoDescPromocionalNoDescQtdPorGrupo() {
		return isUsaDescontoQtdPorGrupo() && usaGrupoDescPromocionalNoDescQtdPorGrupo;
	}

	public static boolean isUsaTotalizadoresEspecificosListaCliente() {
		try {
			if (ValueUtil.isNotEmpty(usaTotalizadoresEspecificosListaCliente)) {
				String[] vlParam = usaTotalizadoresEspecificosListaCliente.split(";");
				if (vlParam[0].equals("1") || vlParam[0].equals("2") || vlParam[1].equals("1") || vlParam[1].equals("2")) {
					return true;
				}
			}
		} catch (Throwable e) {
			//nao faz nada
		}
		return false;
	}
	
	public static boolean isUsaTotalizadorClienteAtrasado() {
		try {
			if (ValueUtil.isNotEmpty(usaTotalizadoresEspecificosListaCliente)) {
				String[] vlParam = usaTotalizadoresEspecificosListaCliente.split(";");
				if (vlParam[0].equals("1") || vlParam[1].equals("1")) {
					return true;
				}
			}
		} catch (Throwable e) {
			//nao faz nada
		}
		return false;
	}

	public static boolean isUsaTotalizadorClienteAtendido() {
		try {
			if (ValueUtil.isNotEmpty(usaTotalizadoresEspecificosListaCliente)) {
				String[] vlParam = usaTotalizadoresEspecificosListaCliente.split(";");
				if (vlParam[0].equals("2") || vlParam[1].equals("2")) {
					return true;
				}
			}
		} catch (Throwable e) {
			//nao faz nada
		}
		return false;
	}
	
	public static boolean isUsaRamoAtividadeSugestaoComESemQtdMinima() {
		return "S".equals(usaRamoAtividadeSugestaoProdutoComCadastro) || "1".equals(usaRamoAtividadeSugestaoProdutoComCadastro);
	}

	public static boolean isUsaRamoAtividadeSugestaoSemQtdMinima() {
		return "2".equals(usaRamoAtividadeSugestaoProdutoComCadastro) || isUsaRamoAtividadeSugestaoComESemQtdMinima();
	}
	
	public static boolean isUsaRamoAtividadeSugestaoComQtdMinima() {
		return "3".equals(usaRamoAtividadeSugestaoProdutoComCadastro) || isUsaRamoAtividadeSugestaoComESemQtdMinima();
	}
	
	public static boolean isUsaVerbaUsuario() {
		return usaVerbaItemPedidoPorItemTabPreco && geraVerbaSaldoUsuarioAtravesConfiguracaoRecorrente;
	}
	
	public static boolean isPermiteAtualizarManualmenteDadosCadastraisCliente() {
		return "S".equals(permiteAtualizarManualmenteDadosCadastraisCliente) || "1".equals(permiteAtualizarManualmenteDadosCadastraisCliente) || "2".equals(permiteAtualizarManualmenteDadosCadastraisCliente);
	}
	
	public static boolean isPermiteAtualizarManualmenteDadosCadastraisClienteSalvaDadosNaoAlterados() {
		return "S".equals(permiteAtualizarManualmenteDadosCadastraisCliente) || "1".equals(permiteAtualizarManualmenteDadosCadastraisCliente);
	}
	
	public static boolean isPermiteRegistrarMotivoVisitaMultiplasVisitas() {
		return isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtual() || isPermiteRegistrarMotivoVisitaMultuplasVisitasDiaAnterior() || isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtualDiaAnterior();
	}
	
	public static boolean isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtualDiaAnterior() {
		return "1".equals(permiteRegistrarMotivoVisitaMultiplasVisitas) || ValueUtil.VALOR_SIM.equals(permiteRegistrarMotivoVisitaMultiplasVisitas);
	}
	
	public static boolean isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtual() {
		return "2".equals(permiteRegistrarMotivoVisitaMultiplasVisitas);
	}
	
	public static boolean isPermiteRegistrarMotivoVisitaMultuplasVisitasDiaAnterior() {
		return "3".equals(permiteRegistrarMotivoVisitaMultiplasVisitas);
	}
	
	public static boolean isUsaCaracteristicasClienteNoSistema() {
		return isUsaRedeCaracteristicasClienteNoSistema() || isUsaCategoriaCaracteristicasClienteNoSistema();
	}
	
	public static boolean isUsaRedeCaracteristicasClienteNoSistema() {
		try {
			String[] vlParams = usaCaracteristicasClienteNoSistema.split(",");
			for (String dominio : vlParams) {
				if ("1".equalsIgnoreCase(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return false;
	}
	
	public static boolean isUsaCategoriaCaracteristicasClienteNoSistema() {
		try {
			String[] vlParams = usaCaracteristicasClienteNoSistema.split(",");
			for (String dominio : vlParams) {
				if ("2".equalsIgnoreCase(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return false;
	}
	
	public static boolean isUsaDescontosAutoEmCascataNaCapaPedidoPorItemArredondaNoFinal() {
		return "2".equals(usaDescontosAutoEmCascataNaCapaPedidoPorItem);
	}
	
	public static boolean isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() {
		return "S".equals(usaDescontosAutoEmCascataNaCapaPedidoPorItem) || "1".equals(usaDescontosAutoEmCascataNaCapaPedidoPorItem) || isUsaDescontosAutoEmCascataNaCapaPedidoPorItemArredondaNoFinal();
	}
	
	public static boolean isUsaDescontoPedidoPorClienteMaximo() {
		return "S".equals(usaDescontoPedidoPorCliente) || "1".equals(usaDescontoPedidoPorCliente) || isUsaDescontoPedidoPorClienteMinimoMaximo();
	}
	
	public static boolean isUsaDescontoPedidoPorClienteMinimoMaximo() {
		return "2".equals(usaDescontoPedidoPorCliente) && permiteDescontoPercentualPorPedido > 0;
	}
	
	public static boolean isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() {
		return "S".equals(aplicaDeflatorCondPagtoValorTotalPedido) || "1".equals(aplicaDeflatorCondPagtoValorTotalPedido);
	}
	
	public static boolean isAplicaDeflatorCondPagtoValorTotalPedidoManual() {
		return "2".equals(aplicaDeflatorCondPagtoValorTotalPedido);
	}
	
	public static boolean isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos() {
		return "3".equals(aplicaDeflatorCondPagtoValorTotalPedido);
	}

	public static boolean isNaoAplicaDeflatorCondPagtoValorTotalPedido() {
		return !isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() && !isAplicaDeflatorCondPagtoValorTotalPedidoManual() && !isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos();
	}
	
	public static boolean isOcultaInfoRentabilidadeManualmente() {
		return isOcultaInfoRentabilidadeManualmenteDefaultOff() || isOcultaInfoRentabilidadeManualmenteDefaultOn();
	}
	
	public static boolean isOcultaInfoRentabilidadeManualmenteDefaultOff() {
		return "S".equals(ocultaInfoRentabilidadeManualmente) || "1".equals(ocultaInfoRentabilidadeManualmente);
	}
	
	public static boolean isOcultaInfoRentabilidadeManualmenteDefaultOn() {
		return "2".equals(ocultaInfoRentabilidadeManualmente);
	}
	
	public static String getCarregaUltimoPrecoItemPedido() {
		return getValorAtributoJson(configCarregaUltimoPrecoItemPedido, "usa", ValorParametro.CONFIGCARREGAULTIMOPRECOITEMPEDIDO);
	}

	public static boolean isCarregaUltimoPrecoItemPedido() {
		return isCarregaUltimoPrecoItemPedidoSemValidarVlMinimo() || isCarregaUltimoPrecoItemPedidoValidandoVlMinimo();
	}
	
	public static boolean isCarregaUltimoPrecoItemPedidoSemValidarVlMinimo() {
		return ValueUtil.VALOR_SIM.equals(getCarregaUltimoPrecoItemPedido()) || "1".equals(getCarregaUltimoPrecoItemPedido());
	}
	
	public static boolean isCarregaUltimoPrecoItemPedidoValidandoVlMinimo() {
		return "2".equals(getCarregaUltimoPrecoItemPedido());
	}
	
	public static boolean isIgnoraAcrescimoDesconto() {
		return isCarregaUltimoPrecoItemPedido() && isAtributoJsonLigado(configCarregaUltimoPrecoItemPedido, "ignoraAcrescimoDesconto", ValorParametro.CONFIGCARREGAULTIMOPRECOITEMPEDIDO);
	}
	
	public static boolean isUsaFiltroCidadeUFBairroNaListaDeClientes() {
		return "S".equals(usaFiltroCidadeUFBairroListaClientes()) || "1".equals(usaFiltroCidadeUFBairroListaClientes()) || isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes();
	}
	
	public static boolean isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes() {
		return "2".equals(usaFiltroCidadeUFBairroListaClientes());
	}
	
	public static boolean isUsaOrdenacaoRankingProduto() {
		return isUsaOrdenacaoRankingProdutoEmpresa() || isUsaOrdenacaoRankingProdutoRepresentante() || isUsaOrdenacaoRankingProdutoSupervisao() || isUsaOrdenacaoRankingProdutoRegional();
	}
	
	public static boolean isUsaOrdenacaoRankingProdutoEmpresa() {
		try {
			String[] vlParams = usaOrdenacaoRankingProduto.split(",");
			for (String dominio : vlParams) {
				if ("1".equalsIgnoreCase(dominio) || ValueUtil.VALOR_SIM.equals(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return false;
	}
	
	public static boolean isUsaOrdenacaoRankingProdutoRepresentante() {
		try {
			String[] vlParams = usaOrdenacaoRankingProduto.split(",");
			for (String dominio : vlParams) {
				if ("2".equalsIgnoreCase(dominio) || ValueUtil.VALOR_SIM.equals(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return false;
	}
	
	public static boolean isUsaOrdenacaoRankingProdutoSupervisao() {
		try {
			String[] vlParams = usaOrdenacaoRankingProduto.split(",");
			for (String dominio : vlParams) {
				if ("3".equalsIgnoreCase(dominio) || ValueUtil.VALOR_SIM.equals(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return false;
	}
	
	public static boolean isUsaOrdenacaoRankingProdutoRegional() {
		try {
			String[] vlParams = usaOrdenacaoRankingProduto.split(",");
			for (String dominio : vlParams) {
				if ("4".equalsIgnoreCase(dominio) || ValueUtil.VALOR_SIM.equals(dominio)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return false;
	}
	
	public static String getHoraLimiteColetaGpsManual() {
		return TimeUtil.isValidTimeHHMM(usaHorarioLimiteColetaGpsManual) ? usaHorarioLimiteColetaGpsManual.concat(":00") : ValueUtil.VALOR_NAO;
	}
	
	public static boolean isUsaHorarioLimiteColetaGpsManual() {
		return !ValueUtil.VALOR_NAO.equals(usaHorarioLimiteColetaGpsManual) && TimeUtil.isValidTimeHHMM(usaHorarioLimiteColetaGpsManual);
	}
		
	private static boolean isUsaValidacaoAcrescDescMaxNoDescontoPromocional() {
		return ValueUtil.VALOR_SIM.equals(usaValidacaoAcrescDescMaxNoDescontoPromocional) || "1".equals(usaValidacaoAcrescDescMaxNoDescontoPromocional);
	}
	
	public static boolean isUsaValidacaoDescMaxNoDescontoPromocional() {
		return "2".equals(usaValidacaoAcrescDescMaxNoDescontoPromocional) || isUsaValidacaoAcrescDescMaxNoDescontoPromocional();
	}
	
	public static boolean isUsaValidacaoAcrescMaxNoDescontoPromocional() {
		return "3".equals(usaValidacaoAcrescDescMaxNoDescontoPromocional) || isUsaValidacaoAcrescDescMaxNoDescontoPromocional();
	}

	public static boolean isConfigGradeProduto() {
		return usaGradeProduto1() || usaGradeProduto2() || isGradeProdutoModoLista() || usaGradeProduto4() || usaGradeProduto5();
	}
	
	public static int getConfigGradeProduto() {
		String usaGradeProduto = getValorAtributoJson(configGradeProduto, "usa", ValorParametro.CONFIGGRADEPRODUTO);
		if (ValueUtil.VALOR_SIM.equals(usaGradeProduto)) {
			usaGradeProduto = "1";
		} else if (ValueUtil.VALOR_NAO.equals(usaGradeProduto)) {
			usaGradeProduto = "0";
		}
		int vlParam = ValueUtil.getIntegerValue(usaGradeProduto);
		return vlParam > 5 ? 0 : vlParam;
	}
	
	public static boolean isUsaGradeProduto1A4() {
		return getConfigGradeProduto() == 1 || getConfigGradeProduto() == 2 || getConfigGradeProduto() == 3 || usaGradeProduto4();
	}

	public static boolean usaGradeProduto1() {
		return getConfigGradeProduto() == 1;
	}

	public static boolean usaGradeProduto2() {
		return getConfigGradeProduto() == 2;
	}

	public static boolean isGradeProdutoModoLista() {
		return getConfigGradeProduto() == 3;
	}
	
	public static boolean usaGradeProduto4() {
		return getConfigGradeProduto() == 4;
	}
	
	public static boolean usaGradeProduto5() {
		return getConfigGradeProduto() == 5;
	}

	public static String[] getCamposTelaItemPedidoGrade() {
		String atributoJson = getValorAtributoJson(configGradeProduto, "camposTelaItemPedidoGrade", ValorParametro.CONFIGGRADEPRODUTO);
		return StringUtil.split(StringUtil.getStringValue(atributoJson), ';');
	}
	
	public static String[] getCamposTelaItemPedidoGradeOcultaveis() {
		String atributoJson = getValorAtributoJson(configGradeProduto, "camposTelaItemPedidoGradeOcultaveis", ValorParametro.CONFIGGRADEPRODUTO);
		return StringUtil.split(StringUtil.getStringValue(atributoJson), ';');
	}
	
	public static boolean isPermiteOcultarCamposItemPedidoGrade() {
		String[] value = getCamposTelaItemPedidoGradeOcultaveis();
		boolean atributoConfigurado = true;
		if (value.length == 1) {
			atributoConfigurado = ValueUtil.valueNotEqualsIfNotNull(value[0], ValueUtil.VALOR_NAO);
		}
		return isConfigGradeProduto() && atributoConfigurado;
	}

	public static boolean isOcultaComboItemGrade() {
		return isAtributoJsonLigado(configGradeProduto, "ocultaComboItemGrade", ValorParametro.CONFIGGRADEPRODUTO);
	}

	public static double getVlPctDivisaoVerbaUsuario(String vlParametro) {
		double vlPctVerbaUsuario = 100;
		try {
			if (!ValueUtil.VALOR_SIM.equals(vlParametro) && !ValueUtil.VALOR_NAO.equals(vlParametro)) {
				vlPctVerbaUsuario = ValueUtil.getDoubleValue(vlParametro);
				if (vlPctVerbaUsuario >= 0 && vlPctVerbaUsuario <= 100) {
					return vlPctVerbaUsuario;
				}
			}
		} catch (Throwable ex) {
			//--
		}
		return vlPctVerbaUsuario;
	}
	
	public static boolean isUsaDivisaoValorVerbaUsuarioEmpresa() {
		double vlPctVerbaUsuario = usaDivisaoValorVerbaUsuarioEmpresa;
		return !ValueUtil.valueEquals(vlPctVerbaUsuario, 100);
	}
	
	public static double getVlPctMargemDescontoItemPedido() {
		if (vlPctMargemDescontoItemPedido > 0 && vlPctMargemDescontoItemPedido <= 100) {
			return vlPctMargemDescontoItemPedido;
		}
		return 0;
	}
	
	public static boolean isPermiteInformarNovoClienteFechamentoPedido() {
		return isPermitePedidoApenasNovoClienteNaoEnviadoServidor() || isIgnoraBloqueioPedidoNovoCliente();
	}

	public static boolean isPermitePedidoNovoCliente() {
		return isPermitePedidoApenasNovoClienteNaoEnviadoServidor() || isIgnoraBloqueioPedidoNovoCliente() || isPermitePedidoParaNovoClienteApenasPelaTelaDetalhes();
	}

	public static boolean isPermitePedidoApenasNovoClienteNaoEnviadoServidor() {
		String vlParametroUsa = getValorAtributoJson(permitePedidoNovoCliente, "usa", ValorParametro.CONFIGPEDIDONOVOCLIENTE);
		return  ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlParametroUsa) || ValueUtil.valueEquals("1", vlParametroUsa);
	}
	
	public static boolean isIgnoraBloqueioPedidoNovoCliente() {
		String vlParametroUsa = getValorAtributoJson(permitePedidoNovoCliente, "usa", ValorParametro.CONFIGPEDIDONOVOCLIENTE);
		return ValueUtil.valueEquals("2", vlParametroUsa);
	}
	
	public static boolean isPermitePedidoParaNovoClienteApenasPelaTelaDetalhes() {
		String vlParametroUsa = getValorAtributoJson(permitePedidoNovoCliente, "usa", ValorParametro.CONFIGPEDIDONOVOCLIENTE);
		return ValueUtil.valueEquals("3", vlParametroUsa);
	}

	public static boolean isUsaMargemProdutoAplicadaNoValorBaseVerba() {
		return usaVerbaItemPedidoPorItemTabPreco && usaMargemProdutoAplicadaNoValorBaseVerba;
	}
	
	public static int getNuResolucaoFotoCameraNativa() {
		try {
			int vlParametro = ValueUtil.getIntegerValue(nuResolucaoFotoCameraNativa);
			return vlParametro;
		} catch (Throwable e) {
			return 0;
		}
	}
	
	public static boolean isUsaCameraNativa() {
		return getNuResolucaoFotoCameraNativa() > 0;
	}
	
	public static boolean isUsaLimpezaGradesNaoDisponiveisParaVenda() {
		return isUsaLimpezaGradesNaoDisponiveisPorItemTabPreco() || isUsaLimpezaGradesNaoDisponiveisPorEstoque();
	}
	
	private static boolean isUsaLimpezaGradesNaoDisponiveisParaVendaPorItemTabPrecoEEstoque() {
		return ValueUtil.VALOR_SIM.equals(usaLimpezaGradesNaoDisponiveisParaVenda) || "1".equals(usaLimpezaGradesNaoDisponiveisParaVenda);
	}

	public static boolean isUsaLimpezaGradesNaoDisponiveisPorItemTabPreco() {
		return ("2".equals(usaLimpezaGradesNaoDisponiveisParaVenda) || isUsaLimpezaGradesNaoDisponiveisParaVendaPorItemTabPrecoEEstoque()) && (usaGradeProduto1() || usaGradeProduto4()) && usaSelecaoPorGrid();
	}

	public static boolean isUsaLimpezaGradesNaoDisponiveisPorEstoque() {
		return ("3".equals(usaLimpezaGradesNaoDisponiveisParaVenda) || isUsaLimpezaGradesNaoDisponiveisParaVendaPorItemTabPrecoEEstoque()) && (usaGradeProduto1() || usaGradeProduto4()) && usaSelecaoPorGrid() && LavenderePdaConfig.usaLocalEstoquePorTabelaPreco();
	}
	

	public static boolean isNaoConsideraRegistroVisitaManualTolerancia() {
		return ValueUtil.VALOR_SIM.equals(naoConsideraRegistroVisitaManualTolerancia) || "1".equals(naoConsideraRegistroVisitaManualTolerancia);
	}
	
	public static boolean isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia() {
		return "2".equals(naoConsideraRegistroVisitaManualTolerancia);
	}
    
	public static boolean isUsaAdicionaNuNotaGeralNaNotaFiscal() { 
	   	return usaAdicionaNuNotaGeralNaNotaFiscal; 
	}
	

	public static List<String> getSugereImpressaoDocumentosPedidoAposEnvio() {
		return Arrays.asList(sugereImpressaoDocumentosPedidoAposEnvio.split(";"));
	}

	public static boolean isSugereImpressaoDocumentosPedidoAposEnvio() {
		final List<String> valor = getSugereImpressaoDocumentosPedidoAposEnvio();
		return valor != null && (valor.contains("1") || valor.contains("2") || 
				valor.contains("3") || valor.contains("4") || valor.contains("5") ||
				valor.contains("6") || valor.contains("7")); 
	}
	
	public static boolean isPermiteImpressaoPedido() {
		final List<String> valor = getSugereImpressaoDocumentosPedidoAposEnvio();
		if (valor != null && (valor.contains("2") || valor.contains("3") || valor.contains("4") || valor.contains("5"))) {
			return false;
		}
		return true;
	}

	public static boolean isSugereImpressaoDocumentosEContingenciaAposEnvio() {
		final List<String> valor = getSugereImpressaoDocumentosPedidoAposEnvio();
		if (valor != null && (valor.contains("3") || valor.contains("5") || valor.contains("7"))) {
			return false;
		}
		return true;
	}
    
    public static boolean isUsaRetornoAutomaticoDadosRelativosPedidoBackground() {
    	return isUsaRetornoAutomaticoDadosNfeEItemNfeBackground() || isUsaRetornoAutomaticoDadosPedidoBoletoBackground();
    }
   
	public static boolean isUsaRetornoAutomaticoDadosNfeEItemNfeBackground() {
		String[] vlParametro = usaRetornoAutomaticoDadosRelativosPedidoBackground.split(";");
		for (String valor : vlParametro) {
			if (ValueUtil.VALOR_SIM.equals(valor) || "1".equals(valor)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUsaRetornoAutomaticoDadosPedidoBoletoBackground() {
		String[] vlParametro = usaRetornoAutomaticoDadosRelativosPedidoBackground.split(";");
		for (String valor : vlParametro) {
			if ("2".equals(valor)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUsaCotaVlQtdRetirarAcresCondPgto() {
		return usaCotaValorQuantidadeRetirarAcresCondPagto && permiteDescontoEmValorPorPedido == 0 && permiteDescontoPercentualPorPedido == 0 && permiteDescValorPorPedidoConsumindoVerba == 0 && !isAplicaMaiorDescontoNoItemPedido();
	}
	
	public static boolean isLiberaSenhaDiaEntregaPedido() {
		return ValueUtil.VALOR_SIM.equals(liberaSenhaDiaEntregaPedido) || "1".equals(liberaSenhaDiaEntregaPedido) || "2".equals(liberaSenhaDiaEntregaPedido);
	}
	
	public static boolean isLiberaSenhaDiaEntregaPedidoPorUsuario() {
		return "2".equals(liberaSenhaDiaEntregaPedido);
	}
	
	public static String[] getLimitesAvisoControleItemPedido() {
		String value = LavenderePdaConfig.usaAvisoControleInsercaoItemPedido;
		if (ValueUtil.isNotEmpty(value) && !value.equals(ValueUtil.VALOR_NAO)) {
			String[] limiteAviso = StringUtil.splitTurbo(value, ';', 4);
			return limiteAviso;
		}
		return null;
	}

	public static boolean isUsaMotivoAgendaNaoPositivadaMultiplasEmpresas() {
		return usaMotivoAgendaNaoPositivadaMultiplasEmpresas && !usaPositivacaoAgendaVisitaSemPedido();
	}
	
	public static boolean isUsaImpressaoContingenciaNfeViaBluetooth() {
		return "3".equals(usaImpressaoContingenciaNfeViaBluetooth) || "4".equals(usaImpressaoContingenciaNfeViaBluetooth);
	}
	
	public static boolean isUsaGeracaoBoletoApenasContingencia() {
		return usaGeracaoBoletoContingencia == 1;
	}
	
	public static boolean isUsaGeracaoBoletoApenasSolicitado() {
		return usaGeracaoBoletoContingencia == 2;
	}
	
	public static boolean isUsaGeracaoImpressaoBoletoContingencia() {
		return isUsaGeracaoBoletoApenasContingencia() || isUsaGeracaoBoletoApenasSolicitado();
	}
	
	public static boolean isUsaPedidoEmConsignacao() {
		return !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, cdStatusPedidoConsignado);
	}
	
	public static int getNuDiasValidadePedidoEmConsignacao() {
		String valorParametro = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, nuDiasValidadePedidoEmConsignacao) ? "180" : nuDiasValidadePedidoEmConsignacao;
		return ValueUtil.getIntegerValue(valorParametro);
		
	}
	
	public static boolean isSugereImpressaoPedidoConsignacao() {
		return usaSugestaoImpressaoPedidoConsignacaoDevolucao == 1 || usaSugestaoImpressaoPedidoConsignacaoDevolucao == 2;
	}
	
	public static boolean isSugereImpressaoPedidoConsignacaoDevolucao() {
		return usaSugestaoImpressaoPedidoConsignacaoDevolucao == 1 || usaSugestaoImpressaoPedidoConsignacaoDevolucao == 3;
	}
	
	public static boolean isLigadoNuDiasPermanenciaPedidoConsignacao() {
		return !ValueUtil.VALOR_NAO.equals(nuDiasPermanenciaPedidoConsignacao);
	}

	public static int getNuDiasPermanenciaPedidoConsignacao() {
		if (ValueUtil.VALOR_SIM.equals(nuDiasPermanenciaPedidoConsignacao)) {
			return 180;
		}
		return ValueUtil.getIntegerValue(nuDiasPermanenciaPedidoConsignacao);
	}
	
	public static boolean isUsaContatoERPClienteNoPedido() {
		return isPermiteInformarContatoERPClienteNoPedido() || isObrigaInformarContatoERPClienteNoPedido();
	}
	
	public static boolean isPermiteInformarContatoERPClienteNoPedido() {
		return usaContatoERPClienteNoPedido == 1;
	}
	
	public static boolean isObrigaInformarContatoERPClienteNoPedido() {
		return usaContatoERPClienteNoPedido == 2;
	}

	private static String getUsaConfigDescontoAcrescimoMaxPorTipoPedido() {
		return getValorAtributoJson(configDescontoAcrescimoItemPedidoPorTipoPedido, "usa", ValorParametro.CONFIGDESCONTOACRESCIMOPEDIDOPORTIPOPEDIDO);
	}
	
	private static boolean isUsaConfigDescontoAcrescimoMaxPorTipoPedido() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getUsaConfigDescontoAcrescimoMaxPorTipoPedido()) || ValueUtil.valueEquals("1", getUsaConfigDescontoAcrescimoMaxPorTipoPedido());
	}
	
	public static boolean isUsaDescontoMaximoPorTipoPedido() {
		return isUsaConfigDescontoAcrescimoMaxPorTipoPedido() || ValueUtil.valueEquals("2", getUsaConfigDescontoAcrescimoMaxPorTipoPedido());
	}
	
	public static boolean isUsaAcrescimoMaximoPorTipoPedido() {
		return isUsaConfigDescontoAcrescimoMaxPorTipoPedido() || ValueUtil.valueEquals("3", getUsaConfigDescontoAcrescimoMaxPorTipoPedido());
	}

	public static boolean isUtilizaTipoPedProdEspecificoDescAcresMaximo() {
		return isAtributoJsonLigado(configDescontoAcrescimoItemPedidoPorTipoPedido, "utilizaTipoPedProdEspecifico", ValorParametro.CONFIGDESCONTOACRESCIMOPEDIDOPORTIPOPEDIDO);
	}

	public static boolean isUsaDescontoPorTipoPedidoEProduto() {
		return isUsaDescontoMaximoPorTipoPedido() && isUtilizaTipoPedProdEspecificoDescAcresMaximo();
	}
	
	public static boolean isSistemaIdiomaIngles() throws SQLException {
		String valor = getValorParametroPorSistema(ValorParametro.USASISTEMAIDIOMAPERSONALIZADO).toUpperCase();
		return isUsaSistemaIdiomaPersonalizado(FrameworkMessages.IDIOMA_INGLES, valor);
	}

	public static boolean isSistemaIdiomaEspanhol() throws SQLException {
		String valor = getValorParametroPorSistema(ValorParametro.USASISTEMAIDIOMAPERSONALIZADO).toUpperCase();
		return isUsaSistemaIdiomaPersonalizado(FrameworkMessages.IDIOMA_ESPANHOL, valor);
	}
	
	private static boolean isUsaSistemaIdiomaPersonalizado(String regiaoIdioma, String valor) {
		boolean result = ValueUtil.valueEquals(regiaoIdioma, valor);
		if (result) {
			AppConfig.regiaoIdioma = regiaoIdioma;
		}
		return result;
	}
	
	public static boolean isApenasHabilitaHoraFimAgendaVisita() {
		return ValueUtil.VALOR_SIM.equals(usaHoraFimAgendaVisita) || "1".equals(usaHoraFimAgendaVisita);
	}
	
	public static boolean isHabilitaEObrigaHoraFimAgendaVisita() {
		return "2".equals(usaHoraFimAgendaVisita);
	}
	
	public static boolean isUsaHoraFimAgendaVisita() {
		return isApenasHabilitaHoraFimAgendaVisita() || isHabilitaEObrigaHoraFimAgendaVisita();
	}
	
	public static boolean isMostraDescricaoReferencia() {
		return mostraDescricaoReferencia == 1 || isMostraDescricaoReferenciaAntesDsProduto();
	}
	
	public static boolean isMostraDescricaoReferenciaAposDsProduto() {
		return mostraDescricaoReferencia == 1;
	}
	
	public static boolean isMostraDescricaoReferenciaAntesDsProduto() {
		return mostraDescricaoReferencia == 2;
	}
	
	public static boolean isMostraRelProdutosNaoInseridosPedido() {
		return isMostraRelProdutosNaoInseridosPedidoAoFecharPedido() || isMostraRelProdutosNaoInseridosPedidoAoSalvarPedido() || isMostraRelProdutosNaoInseridosPedidoAoCancelar();
	}
	
	public static boolean isMostraRelProdutosNaoInseridosPedidoAoFecharPedido() {
		return mostraRelProdutosNaoInseridosPedido != null && mostraRelProdutosNaoInseridosPedido.contains("1");
	}
	
	public static boolean isMostraRelProdutosNaoInseridosPedidoAoSalvarPedido() {
		return mostraRelProdutosNaoInseridosPedido != null && mostraRelProdutosNaoInseridosPedido.contains("3");
	}
	
	public static boolean isMostraRelProdutosNaoInseridosPedidoAoCancelar() {
		return mostraRelProdutosNaoInseridosPedido != null && mostraRelProdutosNaoInseridosPedido.contains("2");
	}
	
	public static boolean isUsaCadastroProdutoDesejadosForaCatalogo() {
		return ValueUtil.VALOR_SIM.equals(usaCadastroProdutoDesejadosForaCatalogo) || "1".equals(usaCadastroProdutoDesejadosForaCatalogo) || isUsaCadastroProdutoDesejadosForaCatalogoSugerindoCadastro();
	}
	
	public static boolean isUsaCadastroProdutoDesejadosForaCatalogoSugerindoCadastro() {
		return "2".equals(usaCadastroProdutoDesejadosForaCatalogo);
	}
	
	public static boolean isAtualizaEstoqueComReservaCentralizada() {
		return atualizarEstoqueInterno && permiteReabrirPedidoFechado && isUsaReservaEstoqueCentralizado();
	}
	
	public static boolean isOcultaFichaFinanceira() {
		return "1".equals(ocultaFichaFinanceira) || "2".equals(ocultaFichaFinanceira);
	}
	
	public static int getNuDiasPermanenciaAtividadePedido() {
		try {
			List<String> valorParametroList = Arrays.asList(nuDiasPermanenciaAtividadePedido.split(";"));
			int vlParametro = ValueUtil.getIntegerValue(valorParametroList.get(valorParametroList.size() - 1));
			return vlParametro > 0 ? vlParametro : 180;
		} catch (Throwable ex) {
			return 180;
		}
	}
	
	public static boolean usaDescQuantidadePeso() {
		return usaDescQuantidadePesoAplicaDescNoVlBaseFlex() || usaDescQuantidadePesoAplicaDescNoVlItemPedido();
	}

	public static boolean usaDescQuantidadePesoAplicaDescNoVlBaseFlex() {
		return isAtributoJsonLigado(configDescQuantidadeVlBasePorPesoPedido, "aplicaDescQtdPesoVlBaseFlex", ValorParametro.CONFIGDESCQUANTIDADEVLBASEPORPESOPEDIDO);
	}

	public static boolean usaDescQuantidadePesoAplicaDescNoVlItemPedido() {
		return isAtributoJsonLigado(configDescQuantidadeVlBasePorPesoPedido, "aplicaDescQtdPesoVlBase", ValorParametro.CONFIGDESCQUANTIDADEVLBASEPORPESOPEDIDO);
	}

	public static boolean usaFaixaPesoPorTabelaPreco() {
		return isAtributoJsonLigado(configDescQuantidadeVlBasePorPesoPedido, "usaFaixaPesoPorTabelaPreco", ValorParametro.CONFIGDESCQUANTIDADEVLBASEPORPESOPEDIDO);
	}

	public static boolean usaBloqueioProdutosDescQtdPeso() {
		return isAtributoJsonLigado(configDescQuantidadeVlBasePorPesoPedido, "usaBloqueioProdutosDescQtdPeso", ValorParametro.CONFIGDESCQUANTIDADEVLBASEPORPESOPEDIDO);
	}

	public static boolean usaSelecaoDocAnexo() {
		return usaSelecaoDocAnexoNovoCli() || usaSelecaoDocAnexoPedido();
	}
	
	public static boolean usaSelecaoDocAnexoPedido() {
		if (usaSelecaoDocumentosAnexo != null) {
			String[] entidades = usaSelecaoDocumentosAnexo.split(";");
			for (String nmEntidade : entidades) {
				if ("PEDIDO".equalsIgnoreCase(nmEntidade)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean usaSelecaoDocAnexoNovoCli() {
		if (usaSelecaoDocumentosAnexo != null) {
			String[] entidades = usaSelecaoDocumentosAnexo.split(";");
			for (String nmEntidade : entidades) {
				if ("NOVOCLIENTE".equalsIgnoreCase(nmEntidade)) {
					return true;
				}
			}
		}
		return false;
	}

	
	public static int getNuDiasPermanenciaDocumentoAnexo() {
		try {
			String[] dias = nuDiasPermanenciaDocumentoAnexo.split(";");
			return dias.length == 2 ? ValueUtil.getIntegerValue(dias[1]) : ValueUtil.getIntegerValue(dias[0]); 
		} catch (Throwable e) {
			return 0;
		}
	}
	
	
	public static boolean isUsaIndicacaoDadosBancariosClienteNoPedido() {
		return usaIndicacaoDadosBancariosClienteNoPedido && !isOcultaSelecaoCondicaoPagamentoPedido() && !usaMultiplosPagamentosParaPedido;
	}

	public static boolean isUsaAvisoVendaProdutosPendentesRetirada() {
		return ValueUtil.VALOR_SIM.equals(usaAvisoVendaProdutosPendentesRetirada) || "1".equals(usaAvisoVendaProdutosPendentesRetirada);
	}
	
	public static boolean isNaoPermiteFecharPedidoSemReservaDeEstoque() {
		return "1".equals(naoPermiteFecharPedidoSemReservaDeEstoque) || "2".equals(naoPermiteFecharPedidoSemReservaDeEstoque);
	}
	
	public static boolean isAvisaPedidoAbertoAoEntrarNoSync() {
		return "2".equals(naoPermiteFecharPedidoSemReservaDeEstoque);
	}
	
	public static boolean isUsaAvisoVendaProdutosPendentesRetiradaSelecionarCliente() {
		return isUsaAvisoVendaProdutosPendentesRetirada() || "2".equals(usaAvisoVendaProdutosPendentesRetirada);
	}

	public static boolean isUsaAvisoVendaProdutosPendentesRetiradaSelecionarProduto() {
		return isUsaAvisoVendaProdutosPendentesRetirada() || "3".equals(usaAvisoVendaProdutosPendentesRetirada);
	}
	public static boolean isUsaBloqueioEnvioPedidoProdutoRestrito() {
		return isPermiteInserirProdutosRestritosNoPedidoSemAviso() || isApresentaAvisoAoUsuarioNaInsercaoDoProdutoRestrito();
	}

	public static boolean isPermiteInserirProdutosRestritosNoPedidoSemAviso() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, usaBloqueioEnvioPedidoProdutoRestrito) || ValueUtil.valueEquals("1", usaBloqueioEnvioPedidoProdutoRestrito);
	}

	public static boolean isApresentaAvisoAoUsuarioNaInsercaoDoProdutoRestrito() {
		return ValueUtil.valueEquals("2", usaBloqueioEnvioPedidoProdutoRestrito); 
	}

	public static boolean isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() {
		return ValueUtil.VALOR_SIM.equals(ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao) || "1".equals(ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao); 
	}

	public static boolean isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosDescPromocional() {
		return ValueUtil.VALOR_SIM.equals(ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao) || "1".equals(ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao) || "2".equals(ignoraIndiceFinanceiroUnidadeAlternativaProdPromocao); 
	}
	
	public static Date getDataBaseCancelamentoAutoPedidoCliente() {
		return getDataBaseCancelamentoAutoPedidoCliente(nuDiasCancelamentoAutoPedidoCliente);
	}
	
	private static Date getDataBaseCancelamentoAutoPedidoCliente(String vlParametro) {
		if (ValueUtil.isValidNumber(vlParametro)) {
			try {
				Date dataBaseCancelamentoAutoPedidoCliente = DateUtil.getCurrentDate();
				DateUtil.decDay(dataBaseCancelamentoAutoPedidoCliente, Convert.toInt(vlParametro)+1);
				return dataBaseCancelamentoAutoPedidoCliente;
			} catch (Throwable e) {
			}
		}
		return null;
	}
	
	public static Date getDataBaseCancelamentoAutoPedidoClienteKeyAccount() {
		Date dataBaseCancelamentoAutoPedidoCliente = getDataBaseCancelamentoAutoPedidoCliente(nuDiasCancelamentoAutoPedidoClienteKeyAccount);
		return ValueUtil.isEmpty(dataBaseCancelamentoAutoPedidoCliente) ? getDataBaseCancelamentoAutoPedidoCliente() : dataBaseCancelamentoAutoPedidoCliente;
	}
		
	public static boolean usaDataBaseCancelamentoAutoPedidoCliente() {
		return ValueUtil.isNotEmpty(getDataBaseCancelamentoAutoPedidoClienteKeyAccount());
	}
		
	public static boolean isTipoPagamentoOcultoAndNaoSetaPadrao() {
		return getConfigTipoPagamentoOcultoNoPedido() == 1;
	}
	
	public static boolean isTipoPagamentoOcultoAndSetaPadraoCliente() {
		return getConfigTipoPagamentoOcultoNoPedido() == 2;
	}
	
	public static boolean isExibeFotoTelaItemPedido() {
		return mostraFotoNaTelaItemPedido == 1 || mostraFotoNaTelaItemPedido == 2;
	}
	
	public static boolean isExibeFotoInsercaoMultiplos() {
		return mostraFotoNaTelaItemPedido == 2;
	}
	
	public static boolean isInsereQtdDescMultipla() {
		return (isPermiteEditarDescontoMultiplaInsercao() || isPermiteEditarDescontoAcrescimoMultiplaInsercao()) && isPermiteEditarQuantidadeMultiplaInsercao() && isOcultaInterfaceNegociacaoMultiplosItens();
	}
	
	public static boolean isInsereSomenteQtdMultipla() {
		return isPermiteEditarQuantidadeMultiplaInsercao() && !(isPermiteEditarDescontoMultiplaInsercao() || isPermiteEditarDescontoAcrescimoMultiplaInsercao()) && isOcultaInterfaceNegociacaoMultiplosItens();
	}
	
	public static boolean naoPermiteInserirQtdDescMultipla() {
		return !(isInsereQtdDescMultipla() || isInsereSomenteQtdMultipla());
	}
	
	public static boolean isUsaValidacaoEstoqueLocalEstCondNegociacao() {
		return usaValidacaoEstoqueLocalEstCondNegociacao && isAtributoJsonLigado(configLocalEstoque, "usaLocalEstoquePorCliente", ValorParametro.CONFIGLOCALESTOQUE);
	}
	
	public static boolean isUsaSelecaoUnidadeAlternativaCapaPedido() {
		return usaSelecaoUnidadeAlternativaCapaPedido && usaUnidadeAlternativa && isPermiteInserirMultiplosItensPorVezNoPedido();
	}
	
	public static boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItemArredondaPorDesconto() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, usaDescontosEmCascataManuaisNaCapaPedidoPorItem) || ValueUtil.valueEquals("1", usaDescontosEmCascataManuaisNaCapaPedidoPorItem);
	}
	
	public static boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItemArredondaNoFinal() {
		return ValueUtil.valueEquals("2", usaDescontosEmCascataManuaisNaCapaPedidoPorItem);
	}
	
	public static boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() {
		return isUsaDescontosEmCascataManuaisNaCapaPedidoPorItemArredondaPorDesconto() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItemArredondaNoFinal(); 
	}
	
	public static boolean isUsaTrocaRecolherComoDescontoPagamentoPedido() {
		return usaTrocaRecolherComoDescontoPagamentoPedido == 1 || usaTrocaRecolherComoDescontoPagamentoPedido == 2;
	}
	
	public static boolean isUsaTrocaRecolherComoDescontoPagamentoPedidoErp() {
		return usaTrocaRecolherComoDescontoPagamentoPedido == 2;
	}
	
	public static List<String> getUsaInfoAdicionaisComprovanteNfe() {
		try {
			return Arrays.asList(usaInfoAdicionaisComprovanteNfe.split(";"));
		} catch (Throwable ex) {
			return Arrays.asList(ValueUtil.VALOR_NAO);
		}
	}
		
	public static boolean isExibeValorNfeNaImpressaoComprovanteNfe() {
		return getUsaInfoAdicionaisComprovanteNfe().contains("1"); 
	}
	
	public static boolean isExibeCondicaoPagamentoNaImpressaoComprovanteNfe() {
		return getUsaInfoAdicionaisComprovanteNfe().contains("2"); 
	}

	public static boolean isExibeDataEmissaoNaImpressaoComprovanteNfe() {
		return getUsaInfoAdicionaisComprovanteNfe().contains("3"); 
	}

	public static boolean isExibeDadosClienteNaImpressaoComprovanteNfe() {
		return getUsaInfoAdicionaisComprovanteNfe().contains("4"); 
	}

	private static boolean usaMultiplasSugestoesProdutoInicioPedido(String tipo) {
		try {
			String[] param = usaMultiplasSugestoesProdutoInicioPedido.split(",");
			for (String e: param) {
				if (tipo.equals(e) || ValueUtil.VALOR_SIM.equals(e)) {
					return true;
				}
			}
		} catch (Throwable ex) {
			return false;
		}
		return false;
	}
	
	public static boolean usaMultiplasSugestoesProdutoInicioPedido() {
		return usaMultiplasSugestoesProdutoInicioPedidoGiro() || usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() || usaMultiplasSugestoesProdutoIndustria() || usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda();
	}
	
	public static boolean usaMultiplasSugestoesProdutoIndustria() {
		String vlParametro = getValorAtributoJson(configUsaMultiplasSugestoesProdutoIndustria, "usa", ValorParametro.CONFIGUSAMULTIPLASSUGESTOESPRODUTOINDUSTRIA);
		List<String> opcoesAtivasList = Arrays.asList(vlParametro.split(";"));
		return opcoesAtivasList.contains(ValueUtil.VALOR_SIM) || opcoesAtivasList.contains("2");
	}
	
	public static boolean isInformaQtdSugeridaProdutoIndustria() {
		if (!usaMultiplasSugestoesProdutoIndustria()) {
			return false;
		}
		return isAtributoJsonLigado(configUsaMultiplasSugestoesProdutoIndustria, "informaQtdSugeridaProdutoIndustria", ValorParametro.CONFIGUSAMULTIPLASSUGESTOESPRODUTOINDUSTRIA);
	}
	
	public static int getNuDiasPermanenciaLogSyncBackground() {
		try {
			String [] values = nuDiasPermanenciaLogSyncBackground.split(";");
			if (values != null && values.length > 1) {
				return ValueUtil.getIntegerValue(values[1]);
			}
			return 2;
		} catch (Throwable ex) {
			return 2;
		}
	}
	
	public static boolean isCalculaComissaoTabPrecoEGrupo() {
		return usaCalculoComissaoPorTabelaPrecoEGrupoProduto && !LavenderePdaConfig.usaConfigCalculoComissao();
	}
	
	public static boolean isDsOpcoesFaltaDescAcrescPorUsuario() {
		return ValueUtil.VALOR_SIM.equals(dsOpcoesFaltaDescAcrescPorUsuario) || "1".equals(dsOpcoesFaltaDescAcrescPorUsuario);
	}
	
	public static boolean isDsOpcoesFaltaDescAcrescPorUsuarioApenasAcrescimo() {
		return "2".equals(dsOpcoesFaltaDescAcrescPorUsuario);
	}
	
	public static boolean isDsOpcoesFaltaDescAcrescPorUsuarioApenasDesconto() {
		return "3".equals(dsOpcoesFaltaDescAcrescPorUsuario);
	}
	
	public static boolean usaConfirmacaoCPFCNPJIgualNovoCliente() {
		String vlParam = getUsaConfirmacaoCPFCNPJIgualNovoCliente();
		return ValueUtil.VALOR_SIM.equals(vlParam) || "1".equals(vlParam) || "2".equals(vlParam);
	}
	
	public static String getUsaConfirmacaoCPFCNPJIgualNovoCliente() {
		if (usaConfirmacaoCPFCNPJIgualNovoCliente != null && !ValueUtil.VALOR_NAO.equals(usaConfirmacaoCPFCNPJIgualNovoCliente)) {
			String[] valores =  usaConfirmacaoCPFCNPJIgualNovoCliente.split(",");
			return valores.length > 1 ? valores[1] : valores[0];
		}
		return ValueUtil.VALOR_NAO;
	}
	
	public static boolean usaConfirmacaoCPFCNPJNovoClienteSemEmpresa() {
		String vlParam = getUsaConfirmacaoCPFCNPJIgualNovoCliente();
		return ValueUtil.VALOR_SIM.equals(vlParam) || "1".equals(vlParam);
	}
	
	public static boolean usaMultiplasSugestoesProdutoInicioPedidoGiro() {
		return usaMultiplasSugestoesProdutoInicioPedido("1");
	}
	
	public static boolean usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() {
		return usaMultiplasSugestoesProdutoInicioPedido("2");
	}
	
	public static boolean usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda() {
		return usaMultiplasSugestoesProdutoInicioPedido("3");
	}
	
	public static boolean isPermiteInserirFreteManualEUsaTipoFrete() {
		return ValueUtil.valueEquals(getValorAtributoJson(configValorFreteManual, "usa", ValorParametro.CONFIGVALORFRETEMANUAL),"1") && isUsaTipoFretePedido();
	}

	public static boolean isSolicitaConfirmacaoSubstituirAgenda() {
		return usaConfirmacaoSubstituicaoCadastroAgendaVisita != null && (usaConfirmacaoSubstituicaoCadastroAgendaVisita.equals("S") || usaConfirmacaoSubstituicaoCadastroAgendaVisita.equals("1"));
	}

	public static boolean isSolicitaConfirmacaoSubstituirAgendaComSenha() {
		return usaConfirmacaoSubstituicaoCadastroAgendaVisita != null && usaConfirmacaoSubstituicaoCadastroAgendaVisita.equals("2");
	}

	public static boolean isPermiteInserirFreteManualENaoUsaTipoFrete() {
		return ValueUtil.valueEquals(getValorAtributoJson(configValorFreteManual, "usa", ValorParametro.CONFIGVALORFRETEMANUAL),"1") && !isUsaTipoFretePedido();
	}
	
	public static boolean isPermiteValorFreteManualMaiorQueValorPedido() {
		return isAtributoJsonLigado(configValorFreteManual, "permiteValorFreteManualMaiorQueValorPedido", ValorParametro.CONFIGVALORFRETEMANUAL);
	}
	
	public static boolean isPermiteInserirFreteManual() {
		return ValueUtil.valueEquals(getValorAtributoJson(configValorFreteManual, "usa", ValorParametro.CONFIGVALORFRETEMANUAL),"1");
	}
	
	public static boolean isPermiteInserirFreteManualItemPedido() {
		return "2".equals(getValorAtributoJson(configValorFreteManual, "usa", ValorParametro.CONFIGVALORFRETEMANUAL));
	}

	public static boolean isPermiteInserirVlFreteAdicionalPedido() {
		return "3".equals(getValorAtributoJson(configValorFreteManual, "usa", ValorParametro.CONFIGVALORFRETEMANUAL));
	}

	public static String getTipoCalculoFreteUnitario() {
		return getValorAtributoJson(configValorFreteManual, "tipoCalculoFreteUnitario", ValorParametro.CONFIGVALORFRETEMANUAL);
	}
	
	public static boolean isUsaRegistroManualDeVisitaSemAgenda() {
		return isUsaRegistroManualDeVisitaSemAgendaNaoObrigatorio() || isUsaRegistroManualDeVisitaSemAgendaObrigatorio();
	}
	
	public static boolean isUsaRegistroManualDeVisitaSemAgendaNaoObrigatorio() {
		return ValueUtil.VALOR_SIM.equals(usaRegistroManualDeVisitaSemAgenda) || "1".equals(usaRegistroManualDeVisitaSemAgenda);
	}
	
	public static boolean isUsaRegistroManualDeVisitaSemAgendaObrigatorio() {
		return "2".equals(usaRegistroManualDeVisitaSemAgenda);
	}
	
	public static boolean isAdicionaCodigoUsuarioAoNumeroPedido() {
		return ValueUtil.VALOR_SIM.equals(adicionaCodigoUsuarioAoNumeroPedido) || "1".equals(adicionaCodigoUsuarioAoNumeroPedido);
	}
	
	public static boolean isAdicionaCodigoUsuarioNumericoAoNumeroPedido() {
		return "2".equals(adicionaCodigoUsuarioAoNumeroPedido);
	}
	
	public static boolean isPermiteInserirEmailAlternativoPedido() {
		return  isPermiteEnviarEmailAlternativoComFlEnviaEmail() || isPermiteEnviarEmailIgnoraFlEnviaEmail();
	}
	
	public static boolean isPermiteEnviarEmailAlternativoComFlEnviaEmail() {
		return ValueUtil.VALOR_SIM.equals(permiteInserirEmailAlternativoPedido) || "1".equals(permiteInserirEmailAlternativoPedido);
	}

	public static boolean isPermiteEnviarEmailIgnoraFlEnviaEmail() {
		return "2".equals(permiteInserirEmailAlternativoPedido);
	}

	public static boolean isUsaEntregaPedidoBaseadaEmCadastro() {
		return previsaoEntregaOcultaNoPedido && usaEntregaPedidoBaseadaEmCadastro; 
	}
	
	public static boolean usaQtMinProdTabPreco() {
		return 1 == usaQtMinProdutoPorTabelaPreco || usaQtMinProdTabPrecoEGrupo() || usaQtMinProdTabPrecoEClasse();
	}
	
	public static boolean usaQtMinProdTabPrecoEGrupo() {
		return 2 == usaQtMinProdutoPorTabelaPreco;
	}
	
	public static boolean usaQtMinProdTabPrecoEClasse() {
		return 3 == usaQtMinProdutoPorTabelaPreco;
	}

	public static boolean isValorMinimoParaPedidoPorCondPagto() {
		return ValueUtil.valueEquals(valorMinimoParaPedidoPorCondPagto, ValueUtil.VALOR_SIM) || ValueUtil.valueEquals(valorMinimoParaPedidoPorCondPagto, "1") || ValueUtil.valueEquals(valorMinimoParaPedidoPorCondPagto, "2");
	}
	
	public static boolean isValorMinimoParaPedidoPorCondPagtoRetiraOpcoesInvalidasCombo() {
		return ValueUtil.valueEquals(valorMinimoParaPedidoPorCondPagto, "2");
	}
	
	public static boolean showGiroProdutoBtNovoItemClick() {
		return isUsaGiroProduto() && (ValueUtil.VALOR_SIM.equals(localApresentacaoRelGiroProduto) || "1".equals(localApresentacaoRelGiroProduto) || "2".equals(localApresentacaoRelGiroProduto));
	}

	public static boolean showGiroProdutoFechamentoPedido() {
		return isUsaGiroProduto() && isApresentaRelGiroProdutoFechamentoPedido();
	}

	public static boolean isApresentaRelGiroProdutoFechamentoPedido() {
		return ValueUtil.VALOR_SIM.equals(localApresentacaoRelGiroProduto) || "1".equals(localApresentacaoRelGiroProduto) || "3".equals(localApresentacaoRelGiroProduto);
	}

	public static boolean isUsaGiroProduto() {
		return !isAtributoJsonDesligado(configRelGiroProduto, "usaUltPreco", ValorParametro.RELGIROPRODUTO) || !isAtributoJsonDesligado(configRelGiroProduto, "usaMedia", ValorParametro.RELGIROPRODUTO)
			|| !isAtributoJsonDesligado(configRelGiroProduto, "usaMaiorCompra", ValorParametro.RELGIROPRODUTO) || !isAtributoJsonDesligado(configRelGiroProduto, "usaQtdMedia", ValorParametro.RELGIROPRODUTO)
			|| !isAtributoJsonDesligado(configRelGiroProduto, "usaVlUnit", ValorParametro.RELGIROPRODUTO) || !isAtributoJsonDesligado(configRelGiroProduto, "usaDia", ValorParametro.RELGIROPRODUTO)
			|| !isAtributoJsonDesligado(configRelGiroProduto, "usaObservacao", ValorParametro.RELGIROPRODUTO) || !isAtributoJsonDesligado(configRelGiroProduto, "usaGrade1", ValorParametro.RELGIROPRODUTO)
			|| !isAtributoJsonDesligado(configRelGiroProduto, "usaGrade2", ValorParametro.RELGIROPRODUTO) || !isAtributoJsonDesligado(configRelGiroProduto, "usaGrade3", ValorParametro.RELGIROPRODUTO);
	}

	public static boolean isUsaVlUnitGiroProduto() {
		return isAtributoJsonLigado(configRelGiroProduto, "usaVlUnit", ValorParametro.RELGIROPRODUTO);
	}
	
	public static boolean naoUsaUltPrecoRelGiroProduto() {
		return isAtributoJsonDesligado(configRelGiroProduto, "usaUltPreco", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaUltPreco() {
		return getValorAtributoJson(configRelGiroProduto, "usaUltPreco", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaMedia() {
		return getValorAtributoJson(configRelGiroProduto, "usaMedia", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaMaiorCompra() {
		return getValorAtributoJson(configRelGiroProduto, "usaMaiorCompra", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaQtdMedia() {
		return getValorAtributoJson(configRelGiroProduto, "usaQtdMedia", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaVlUnit() {
		return getValorAtributoJson(configRelGiroProduto, "usaVlUnit", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaDia() {
		return getValorAtributoJson(configRelGiroProduto, "usaDia", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaObservacao() {
		return getValorAtributoJson(configRelGiroProduto, "usaObservacao", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaGrade1() {
		return getValorAtributoJson(configRelGiroProduto, "usaGrade1", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaGrade2() {
		return getValorAtributoJson(configRelGiroProduto, "usaGrade2", ValorParametro.RELGIROPRODUTO);
	}
	
	public static String relGiroProdutoUsaGrade3() {
		return getValorAtributoJson(configRelGiroProduto, "usaGrade3", ValorParametro.RELGIROPRODUTO);
	}

	public static boolean usaTransportadoraPedido(){
		return isAtributoJsonLigado(configTransportadoraPedido, "usa", ValorParametro.CONFIGTRANSPORTADORAPEDIDO);
	}

	public static boolean isBloqueiaAlterarTransportadora(){
		return isAtributoJsonLigado(configTransportadoraPedido, "bloqueiaAlterarTransportadora", ValorParametro.CONFIGTRANSPORTADORAPEDIDO) && usaTransportadoraPedido();
	}

	public static boolean isUsaTransportadoraAuxiliar() {
		return  usaTransportadoraPedido() && usaTransportadoraAuxiliar;
	}
	
	public static int getNuDiasPermanenciaAnaliseNovoCliente() {
		if (ValueUtil.isNotEmpty(nuDiasPermanenciaAnaliseNovoCliente)) {
			String vlParamPda = nuDiasPermanenciaAnaliseNovoCliente.split(";")[0];
			if (ValueUtil.isValidNumber(vlParamPda)) {
				return ValueUtil.getIntegerValue(vlParamPda);
			}
		}
		return 0;
	}
	
	public static boolean isUsaCondicaoComercialCliPadraoVazio() {
		return "2".equals(usaCondicaoComercialPorCliente);
	}
	
	public static boolean isUsaCondicaoComercialCliComDefault() {
		return ValueUtil.VALOR_SIM.equals(usaCondicaoComercialPorCliente) || "1".equals(usaCondicaoComercialPorCliente);
	}
	
	public static boolean isUsaCondicaoComercialPorCliente() {
		return isUsaCondicaoComercialCliComDefault() || isUsaCondicaoComercialCliPadraoVazio();
	}
	
	public static boolean isUsaPreenchimentoCamposNovoClientePorCnpj() {
		return isAtributoJsonLigado(usaPreenchimentoCamposNovoClientePorCnpj, "usa" , ValorParametro.USAPREENCHIMENTOCAMPOSNOVOCLIENTEPORCNPJ);
	}
	
	public static boolean isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio() {
		return isAtributoJsonLigado(usaPreenchimentoCamposNovoClientePorCnpj, "bloqueiaEdicaoCampos" , ValorParametro.USAPREENCHIMENTOCAMPOSNOVOCLIENTEPORCNPJ);
	}
	
	public static boolean isUsaPreenchimentoCamposNovoClientePorCnpjNotificaFalhas() {
		return isAtributoJsonLigado(usaPreenchimentoCamposNovoClientePorCnpj, "notificaFalhas" , ValorParametro.USAPREENCHIMENTOCAMPOSNOVOCLIENTEPORCNPJ);
	}
	
	public static boolean isUsaPermitePreenchimentoCamposAtualizacaoClientePorCNPJ() {
		return isAtributoJsonLigado(usaPreenchimentoCamposNovoClientePorCnpj, "permitePreenchimentoCamposAtualizacaoClientePorCNPJ" , ValorParametro.USAPREENCHIMENTOCAMPOSNOVOCLIENTEPORCNPJ);
	}
	
	public static boolean isUsaTeclaEnterComoConfirmacaoItemPedido() {
		return usaTeclaEnterComoConfirmacaoItemPedido;
	}
	
	public static void configuraTecladoVirtual() {
		usaTecladoVirtual = usaTecladoVirtual && !usaFocoCampoBuscaAutomaticamente;
		LavendereConfig.getInstance().usaFocoCampoBuscaAutomaticamente = usaFocoCampoBuscaAutomaticamente; 
		BaseEdit.tecladoAwaysVisible = usaTecladoVirtual;
		EditMemo.tecladoAwaysVisible = usaTecladoVirtual;
	}
	public static boolean isUsaRateioProducaoPorRepresentante() {
		return ValueUtil.VALOR_SIM.equals(usaRateioProducaoPorRepresentante) || "1".equals(usaRateioProducaoPorRepresentante);
	}
	
	public static boolean isUsaRateioProducaoPorRepresentantePermiteEdicao() {
		return "2".equals(usaRateioProducaoPorRepresentante);
	}
	
	public static boolean isMostraFotoProdutoNaListaProdutosForaPedido() {
		return isMostraFotoProdutoNaListaProdutosDentroForaPedido() || mostraFotoProdutoNaListaProdutos == 2 || mostraFotoProdutoNaListaProdutos == 5;
	}
	
	public static boolean isMostraFotoProdutoNaListaProdutosDentroPedido() {
		return isMostraFotoProdutoNaListaProdutosDentroForaPedido() || mostraFotoProdutoNaListaProdutos == 3 || mostraFotoProdutoNaListaProdutos == 6;
	}
	
	private static boolean isMostraFotoProdutoNaListaProdutosDentroForaPedido() {
		return mostraFotoProdutoNaListaProdutos == 1 || mostraFotoProdutoNaListaProdutos == 7; 
	}
	
	public static boolean isMostraFotoProdutoListSugPerson() {
		return mostraFotoProdutoNaListaProdutos > 3 && mostraFotoProdutoNaListaProdutos < 8;
	}
	
	public static boolean isMostraEstoquePrevistoNaListaProdutosForaPedido() {
		return "2".equals(mostraEstoquePrevistoNaListaProdutos) || isMostraEstoquePrevistoNaListaProdutosDentroForaPedido();
	}
	
	public static boolean isMostraEstoquePrevistoNaListaProdutosDentroPedido() {
		return "3".equals(mostraEstoquePrevistoNaListaProdutos) || isMostraEstoquePrevistoNaListaProdutosDentroForaPedido();
	}
	
	private static boolean isMostraEstoquePrevistoNaListaProdutosDentroForaPedido() {
		return ValueUtil.VALOR_SIM.equals(mostraEstoquePrevistoNaListaProdutos) || "1".equals(mostraEstoquePrevistoNaListaProdutos);
	}
	
	public static boolean isConfigCalculoPesoPedido() {
		return !comparaValorAtributoJson(configCalculoPesoPedido, "formulaCalculo", ValueUtil.VALOR_NAO, ValorParametro.CONFIGCALCULOPESOPEDIDO);
	}
	
	public static boolean isConfigCalculoPesoPedidoConsideraGramatura() {
		return "2".equals(getValorAtributoJson(configCalculoPesoPedido, "formulaCalculo", ValorParametro.CONFIGCALCULOPESOPEDIDO));
	}
	
	public static boolean isConfigCalculoPesoPedidoNaoConsideraGramatura() {
		return "1".equals(getValorAtributoJson(configCalculoPesoPedido, "formulaCalculo", ValorParametro.CONFIGCALCULOPESOPEDIDO));
	}
	
	public static boolean isCalculaPesoTotalPedidoNoTotalizador() {
		return "1".equals(getValorAtributoJson(configCalculoPesoPedido, "formulaCalculo", ValorParametro.CONFIGCALCULOPESOPEDIDO)) || "2".equals(getValorAtributoJson(configCalculoPesoPedido, "formulaCalculo", ValorParametro.CONFIGCALCULOPESOPEDIDO)) || isCalculaPesoTotalPedidoMedio();
	}
	
	public static boolean isCalculaPesoTotalPedidoMedio() {
		return ValueUtil.valueEquals("3", getValorAtributoJson(configCalculoPesoPedido, "formulaCalculo", ValorParametro.CONFIGCALCULOPESOPEDIDO));
	}

	public static boolean isMostraRentabPraticadaSugerida() {
		return mostraRentabPraticadaSugerida && (isUsaFormulaRentabilidadePedido(9) || usaConfigMargemRentabilidade());
	}

	public static boolean isCalculaPesoTotalMostraPesoPorFaixa() {
		return isAtributoJsonLigado(configCalculoPesoPedido, "mostraPesoPorFaixa", ValorParametro.CONFIGCALCULOPESOPEDIDO);
	}

	public static boolean isUsaCalculoVolumeItemPedido() {
		return isUsaCalculoVolumeItemPedidoTotal() || isUsaCalculoVolumeItemPedidoLista() || isUsaCalculoVolumeItemPedidoTotalizador();
	}
	
	public static boolean isUsaCalculoVolumeItemPedidoTotal() {
		return ValueUtil.VALOR_SIM.equals(usaCalculoVolumeItemPedido) || "1".equals(usaCalculoVolumeItemPedido);
	}
	
	public static boolean isUsaCalculoVolumeItemPedidoTotalizador() {
		return "2".equals(usaCalculoVolumeItemPedido);
	}
	
	public static boolean isUsaCalculoVolumeItemPedidoLista() {
		return "3".equals(usaCalculoVolumeItemPedido);
	}
	
	public static boolean isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido() {
		return apresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido && usaConversaoUnidadesMedida;
	}
	
	private static String vlUsaSugestaoVendaComCadastro() {
		return getValorAtributoJson(usaSugestaoVendaComCadastro, "usa", ValorParametro.USASUGESTAOVENDACOMCADASTRO);
	}

	private static boolean usaSugestaoVendaComCadastro (final String vlUsaSugestaoVendaComCadastro) {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlUsaSugestaoVendaComCadastro) || ValueUtil.valueEquals("1", vlUsaSugestaoVendaComCadastro);
	}

	public static boolean isUsaSugestaoVendaComCadastro() {
		return isUsaSugestaoVendaComCadastroNovoItem() || isUsaSugestaoVendaComCadastroFecharPedido();
	}
	
	public static boolean isUsaSugestaoVendaComCadastroNovoItem() {
		String vlUsaSugestaoVendaComCadastro = vlUsaSugestaoVendaComCadastro();
		return usaSugestaoVendaComCadastro(vlUsaSugestaoVendaComCadastro) || ValueUtil.valueEquals("2", vlUsaSugestaoVendaComCadastro);
	}
	
	public static boolean isUsaSugestaoVendaComCadastroFecharPedido() {
		String vlUsaSugestaoVendaComCadastro = vlUsaSugestaoVendaComCadastro();
		return usaSugestaoVendaComCadastro(vlUsaSugestaoVendaComCadastro) || ValueUtil.valueEquals("3", vlUsaSugestaoVendaComCadastro);
	}

	public static boolean ignoraProdutoSemPreco() {
		return isUsaSugestaoVendaComCadastro() && isAtributoJsonLigado(usaSugestaoVendaComCadastro, "ignoraProdutoSemPreco", ValorParametro.USASUGESTAOVENDACOMCADASTRO);
	}
	
	public static boolean isUsaPrecoBaseItemPedidoPrecoBonificado() {
		return ValueUtil.VALOR_SIM.equals(usaPrecoBaseItemBonificado) || "1".equals(usaPrecoBaseItemBonificado);
	}
	
	public static boolean isUsaPrecoBaseItemTabPrecoBonificado() {
		return "2".equals(usaPrecoBaseItemBonificado);
	}

	public static boolean isUsaPrecoBasePorRedutorCliente() {
		return "3".equals(usaPrecoBaseItemBonificado);
	}
	
	public static boolean isUsaPrecoBaseItemBonificado() {
		return isUsaPrecoBaseItemPedidoPrecoBonificado() || isUsaPrecoBaseItemTabPrecoBonificado() || isUsaPrecoBasePorRedutorCliente();
	}
	
	private static String[] getUsaCamposAdicionaisImpressaoLayoutNfe() {
    	try {
    		return usaCamposAdicionaisImpressaoLayoutNfe.split(",");
    	} catch (NullPointerException ex) {
    		return new String[] {ValueUtil.VALOR_NAO};
    	}
    }
	
	public static boolean usaImpressaoNfeViaBluetoothComCamposAdicionais() {
		return isUsaLayout3ConfigImpressaoNfeViaBluetooth() || isUsaLayout4ConfigImpressaoNfeViaBluetooth() || isUsaLayout6ConfigImpressaoNfeViaBluetooth();
	}
    
	public static boolean isUsaLayout3ConfigImpressaoNfeViaBluetooth() {
		return getValorAtributoJson(configImpressaoNfeViaBluetooth, "usa", ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH).contains("3");
	}	
	
	public static boolean isUsaLayout4ConfigImpressaoNfeViaBluetooth() {
		return getValorAtributoJson(configImpressaoNfeViaBluetooth, "usa", ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH).contains("4");
	}
	
	public static boolean isUsaLayout6ConfigImpressaoNfeViaBluetooth() {
		return getValorAtributoJson(configImpressaoNfeViaBluetooth, "usa", ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH).contains("6");
	}
	
	public static int nrCopiasConfigImpressaoNfeViaBluetooth() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configImpressaoNfeViaBluetooth, "nrCopias", ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH));
	}
	
	public static boolean isAgrupadaItensInclusaoConfigImpressaoNfeViaBluetooth() {
		return getValorAtributoJson(configImpressaoNfeViaBluetooth, "ordenacaoItens", ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH).contains("G");
	}		
	
	public static int nuCasasDecimaisValoresMonetarios() {
		int nuCasasDecimaisValoresMonetarios = ValueUtil.getIntegerValue(getValorAtributoJson(configImpressaoNfeViaBluetooth, "nuCasasDecimaisValoresMonetarios", ValorParametro.CONFIGIMPRESSAONFEVIABLUETOOTH));
		return nuCasasDecimaisValoresMonetarios > 0 ? nuCasasDecimaisValoresMonetarios : ValueUtil.doublePrecisionInterface;
	}

    public static boolean isUsaCampoTotalStItemImpressaoLayoutNfe() {
    	if (!usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
    		return false;
    	}
    	
    	String[] dominios = getUsaCamposAdicionaisImpressaoLayoutNfe();
		for (String dominioParam: dominios) {
			if ("1".equals(dominioParam)) {
				return true;
			}
		}
		return false;
    }
    
    public static boolean isUsaCampoTotalProdutosNotaImpressaoLayoutNfe() {
    	if (!usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
    		return false;
    	}
    	
    	String[] dominios = getUsaCamposAdicionaisImpressaoLayoutNfe();
    	for (String dominioParam: dominios) {
    		if ("2".equals(dominioParam)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static boolean isUsaCampoBaseCalculoIcmsImpressaoLayoutNfe() {
    	if (!usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
    		return false;
    	}
    	
    	String[] dominios = getUsaCamposAdicionaisImpressaoLayoutNfe();
    	for (String dominioParam: dominios) {
    		if ("3".equals(dominioParam)) {
    			return true;
    		}
    	}
    	return false;
    }

    public static boolean isUsaCampoBaseCalculoSTImpressaoLayoutNfe() {
    	if (!usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
    		return false;
    	}
    	
    	String[] dominios = getUsaCamposAdicionaisImpressaoLayoutNfe();
    	for (String dominioParam: dominios) {
    		if ("4".equals(dominioParam)) {
    			return true;
    		}
    	}
    	return false;
    }

    public static boolean isUsaCampoOutrasDespesasImpressaoLayoutNfe() {
    	if (!usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
    		return false;
    	}
    	
    	String[] dominios = getUsaCamposAdicionaisImpressaoLayoutNfe();
    	for (String dominioParam: dominios) {
    		if ("5".equals(dominioParam)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static boolean isUsaCampoNcmProdutoImpressaoLayoutNfe() {
    	if (!usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
    		return false;
    	}
    	
    	String[] dominios = getUsaCamposAdicionaisImpressaoLayoutNfe();
    	for (String dominioParam: dominios) {
    		if ("6".equals(dominioParam)) {
    			return true;
    		}
    	}
    	return false;
    }
	
	public static boolean isEnviaEstoqueRepParaServidor() {
		return enviaEstoqueRepParaServidor == 1 || isEnviaEstoqueRepParaServidorDevolveEstoqueAtual();
	}
	
	public static boolean isEnviaEstoqueRepParaServidorDevolveEstoqueAtual() {
		return enviaEstoqueRepParaServidor == 2;
	}
	
	private static String[] mostraColunaEstoqueNasListasNoPedido() {
		try {
			return mostraColunaEstoqueNaListaDeProdutos.split(";");
		} catch (Throwable ex) {
			return new String[] {ValueUtil.VALOR_NAO};
		}
	}
	
	public static boolean mostraColunaEstoqueNaListaProdutoForaPedido() {
		String[] dominios = mostraColunaEstoqueNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "1".equals(dominio)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean mostraColunaEstoqueNaListaProdutoDentroPedido() {
		String[] dominios = mostraColunaEstoqueNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "2".equals(dominio)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean mostraColunaEstoqueNaListaItensInseridosNoPedido() {
		String[] dominios = mostraColunaEstoqueNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "3".equals(dominio)) {
				return true;
			}
		}
		return false;
	}
	
	private static String[] mostraColunaMarcaNasListasNoPedido() {
		try {
			return mostraColunaMarcaNaListaDeProdutos.split(";");
		} catch (Throwable ex) {
			return new String[] {ValueUtil.VALOR_NAO};
		}
	}
	
	public static boolean apresentaMarcaNasListasNoPedido() {
		return mostraColunaMarcaNaListaProdutoForaPedido() || mostraColunaMarcaNaListaProdutoDentroPedido() || mostraColunaMarcaNaListaItensInseridosNoPedido();
	}
	
	public static boolean mostraColunaMarcaNaListaProdutoForaPedido() {
		String[] dominios = mostraColunaMarcaNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "1".equals(dominio)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean mostraColunaMarcaNaListaProdutoDentroPedido() {
		String[] dominios = mostraColunaMarcaNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "2".equals(dominio)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean mostraColunaMarcaNaListaItensInseridosNoPedido() {
		String[] dominios = mostraColunaMarcaNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "3".equals(dominio)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUsaImpressaoNfceViaBluetooth() {
		return "3".equals(usaImpressaoNfceViaBluetooth);
	}

	public static boolean isUsaRetornoAutomaticoDadosNfce() {
		return usaRetornoAutomaticoDadosRelativosPedido.indexOf("4") != -1;
	}


	public static boolean mostraColunaMarcaNaSugestaoDeProdutos() {
		String[] dominios = mostraColunaMarcaNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "4".equals(dominio)) {
				return true;
			}
		}
		return false;
	}

	public static boolean mostraColunaMarcaNaListaNovidadesDeProdutos() {
		String[] dominios = mostraColunaMarcaNasListasNoPedido();
		for (String dominio: dominios) {
			if (ValueUtil.VALOR_SIM.equals(dominio) || "5".equals(dominio)) {
				return true;
			}
		}
		return false;
	}

	public static Integer nuLinhasRetornoBuscaSistema(String vlParametro) {
		int nuLinhas = 0;
		String chave;
		try {
			if (ValueUtil.isEmpty(vlParametro) || ValueUtil.VALOR_NAO.equals(vlParametro)) {
				nuLinhas = 0;
			} else {
				String[] dominio = vlParametro.split(";");
				if (dominio.length > 1) {
					chave = dominio[1];	
				} else {
					chave = dominio[0];
				}
				String[] vlChave = chave.split(":");
				if (vlChave.length > 1) {
					apresentaMensagemLimiteNuLinhasRetornoBuscaSistema = ValueUtil.VALOR_SIM.equals(vlChave[1]);
				} else if (vlChave.length == 1) {
					apresentaMensagemLimiteNuLinhasRetornoBuscaSistema = true;
				}
				nuLinhas = ValueUtil.getIntegerValue(vlChave[0]);
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
		return nuLinhas;
	}
	
	private static void setUsaApresentacaoFixaTicketMedioPedidosCliente(Hashtable hashValorParametros) {
		try {
			List<String> asList = Arrays.asList(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.USAAPRESENTACAOFIXATICKETMEDIOPEDIDOSCLIENTE).split(";"));
			usaApresentacaoFixaTicketMedioCapaPedido = asList.contains("1");
			usaApresentacaoFixaTicketMedioListaAgendaVisita = asList.contains("3");
		} catch (Throwable ex) {
		}
	}
	
	public static int[] getDominioInformacoesSugVendaPerson() {
		String[] dominios = dsInformacoesComplementaresListaSugestaoVendaPerson.split(";");
		int size = dominios.length;
		Convert.qsort(dominios, 0, size - 1, Convert.SORT_INT);
		int vlParametro = 0;
		int[] retorno = new int[size];
		int indexRetorno = 0;
		for (int i = 0; i < size && i < 5; i++) {
			if ((vlParametro = ValueUtil.getIntegerValue(dominios[i])) > 0) {
				retorno[indexRetorno++] = vlParametro;
			}
		}
		return retorno;
	}
	
	public static boolean isUsaSugestaoVendaPersonalizavelInicioPedido() {
		return usaSugestaoVendaPersonalizavelInicioPedido == 1 || usaSugestaoVendaPersonalizavelInicioPedido == 3;
	}
	
	private static void ajustaOrdemSugPerson() {
		if (ValueUtil.getIntegerValue(dsOrdenacaoListaSugPerson) > 2) { 
			if (dsInformacoesComplementaresListaSugestaoVendaPerson.indexOf(SugVendaPerson.ORDEMVLHIST) == -1 && dsInformacoesComplementaresListaSugestaoVendaPerson.indexOf(SugVendaPerson.ORDEMQTDHIST) == -1 && !mostraColunaEstoqueNaListaProdutoDentroPedido()) {
				dsOrdenacaoListaSugPerson = "1";
				sentidoOrdenacaoListaSugPerson = "A";
			}
		}
	}
	
	private static void setQtLimiteProdutosSugPerson() {
		String[] qtdProdutosLimite = qtLimiteProdutosExibidosListaSugestaoVendaPerson.split(";");
		if (qtdProdutosLimite.length < 2) {
			qtLimiteProdutosSug = ValueUtil.getIntegerValue(qtLimiteProdutosExibidosListaSugestaoVendaPerson);
		} else {
			qtLimiteProdutosSug = ValueUtil.getIntegerValue(qtdProdutosLimite[1]);
		}
		qtLimiteProdutosSug = qtLimiteProdutosSug < 1 ? 100 : qtLimiteProdutosSug;
	}
	
	public static boolean isUsaCalculoGastoTotalVariavelPorProdutoSemICMS() {
		return usaCalculoGastoTotalVariavelPorProduto == 1;
	}
	
	public static boolean isUsaCalculoGastoTotalVariavelPorProdutoComICMS() {
		return usaCalculoGastoTotalVariavelPorProduto == 2;
	}
	
	public static boolean isUsaCalculoGastoTotalVariavelPorProduto() {
		return isUsaCalculoGastoTotalVariavelPorProdutoSemICMS() || isUsaCalculoGastoTotalVariavelPorProdutoComICMS();
	}
	
	private static void setApresentaIndicadores(String param) {
		if (ValueUtil.isNotEmpty(configUsoMarcadores)) {
			String[] params = configUsoMarcadores.split(";");
			String paramPda = params.length > 1 ? params[1] : params[0];
			boolean valor = Arrays.asList(paramPda.split(",")).contains(param);
			if (Cliente.APRESENTA_LISTACLI.equals(param)) {
				apresentaIndicadoresCompraHistoricoClienteListaClientes = valor;
				return;
			}
			if (Cliente.APRESENTA_LISTAGENDA.equals(param)) {
				apresentaIndicadoresCompraHistoricoClienteListaAgendas = valor;
				return;
			}
			apresentaIndicadoresClienteRiscoChurn = valor;
		}
	} 
	
	private static void setApresentaMarcadoresProduto() {
		apresentaMarcadorProdutoInsercao = apresentaMarcadorProdutoInseridos = apresentaMarcadorProdutoLista = apresentaMarcadorGrade = false;
		if (ValueUtil.isNotEmpty(localUsoMarcadoresProduto)) {
			String[] paramGeral = localUsoMarcadoresProduto.split(";");
			String[] params = (paramGeral.length > 1 ? paramGeral[1] : paramGeral[0]).split(",");
			for (int i = 0; i < params.length; i++) {
				apresentaMarcadorProdutoInsercao |= "1".equals(params[i]);
				apresentaMarcadorProdutoInseridos |= "2".equals(params[i]);
				apresentaMarcadorProdutoLista |= "3".equals(params[i]);
				apresentaMarcadorGrade |= "4".equals(params[i]);
			}
			apresentaMarcadorGrade &= usaGradeProduto5();
		}
	}

	public static boolean apresentaIndicadoreDetelhesCliente() {
		return (apresentaIndicadoresCompraHistoricoClienteListaAgendas || apresentaIndicadoresCompraHistoricoClienteListaClientes);
	}
	
	public static boolean apresentaMarcadoresProduto() {
		return apresentaMarcadorProdutoInsercao || apresentaMarcadorProdutoInseridos || apresentaMarcadorProdutoLista;
	}

	public static boolean isUsaPrecoPorUnidadeQuantidadePrazo() {
		return ValueUtil.VALOR_SIM.equals(usaPrecoPorUnidadeQuantidadePrazo) || "1".equals(usaPrecoPorUnidadeQuantidadePrazo) || "2".equals(usaPrecoPorUnidadeQuantidadePrazo);
	}
	
	public static boolean isNaoPermiteManterPedidosAbertos() {
		return getQtdPedidosPermitidosManterAbertos() == 0;
	}

	public static int getQtdPedidosPermitidosManterAbertos() {
		try {
			return Convert.toInt(qtdPedidosPermitidosManterAbertos);
		} catch (Throwable e) {
			return -1;
		}
	}
	
	public static boolean isUsaToleranciaVisitasForaAgenda() {
		return !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, vlPctToleranciaVisitasForaAgenda);
	}
	
	public static double getVlPctToleranciaVisitasForaAgenda() {
		return ValueUtil.getDoubleValue(vlPctToleranciaVisitasForaAgenda);
	}

	public static boolean isUsaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso() {
		return isAvisaClienteAtrasadoPorValorTotalTitulosEmAtraso() || isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso();
	}

	public static boolean isAvisaClienteAtrasadoPorValorTotalTitulosEmAtraso() {
		return "A".equals(usaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso);
	}

	public static boolean isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso() {
		return "B".equals(usaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso);
	}
	
	
	public static boolean usaGeracaoNfePedidoAposFechamento() {
		return usaGeracaoNfePedidoAposFechamentoPrd() || usaGeracaoNfePedidoAposFechamentoHom();
	}
	
	public static boolean usaGeracaoNfePedidoAposFechamentoPrd() {
		return usaGeracaoNfePedidoAposFechamento == 1;
	}
	
	public static boolean usaGeracaoNfePedidoAposFechamentoHom() {
		return usaGeracaoNfePedidoAposFechamento == 2;
	}
	
	public static boolean isUsaGeracaoNotaNfeContingenciaPedidoSemConexao() {
		String[] vlParam = usaGeracaoNotaNfeContingenciaPedido.split(";");
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlParam[0]) || ValueUtil.valueEquals("1", vlParam[0]);
	}

	public static boolean isUsaSomenteGeracaoNotaNfeContingenciaPedido() {
		String[] vlParam = usaGeracaoNotaNfeContingenciaPedido.split(";");
		return ValueUtil.valueEquals("2", vlParam[0]);
	}
	
	private static String getVlSegundoDominioParaUsaGeracaoNotaNfeContingenciaPedido() {
		String[] vlParam = usaGeracaoNotaNfeContingenciaPedido.split(";");
		if (vlParam.length == 1) {
			return ValueUtil.VALOR_SIM;
		}
		if (!ValueUtil.valueEquals("1", vlParam[1]) && !ValueUtil.valueEquals("2", vlParam[1])) {
			return ValueUtil.VALOR_SIM;
		}
		return vlParam[1];
	}
	
	public static boolean isNecessitaQueRepresentantePossuaSerieExclusivaDeContingencia() {
		String vlParam = getVlSegundoDominioParaUsaGeracaoNotaNfeContingenciaPedido();
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlParam) || ValueUtil.valueEquals("1", vlParam);
	}
	
	private static void setValuesRetornoAutomaticoPedido() {
		if (ValueUtil.isNotEmpty(usaRetornoAutomaticoDadosRelativosPedido)) {
			String[] vlParam = usaRetornoAutomaticoDadosRelativosPedido.split(";");
			if (vlParam.length > 0) {
				loop: for (int i = 0; i < vlParam.length; i++) {
					switch (getVlParamAsInt(vlParam[i])) {
						case 1:
							usaRetornoAutomaticoDadosNfe = true;
							break;
						case 2:
							usaRetornoAutomaticoDadosBoleto = true;
							break;
						case 3:
							usaRetornoAutomaticoDadosErpDif = true;
							usaRetornoAutomaticoDadosNfe = usaRetornoAutomaticoDadosBoleto = usaRetornoAutomaticoValidacaoSEFAZ = false;
							break loop;
						case 4:
							usaRetornoAutomaticoDadosNfce = true;
							break;
						case 5:
							usaRetornoAutomaticoValidacaoSEFAZ = true;
							usaRetornoAutomaticoDadosNfe = usaRetornoAutomaticoDadosBoleto = usaRetornoAutomaticoDadosErpDif = false;
							break loop;
					}
				}
			}
		}
	}
	
	public static boolean isUsaInformacoesAdicionaisPagamentoCheque() {
		return isUsaDominioPagamentoCheque("1") || isUsaDominioPagamentoCheque("2") || isUsaDominioPagamentoCheque("3") || isUsaDominioPagamentoCheque("4") || isUsaDominioPagamentoCheque("5") || isUsaDominioPagamentoCheque("6") || isUsaDominioPagamentoCheque("7") || isUsaDominioPagamentoCheque("8") || isUsaDominioPagamentoCheque("9") || isUsaDominioPagamentoCheque("10");
	}
	
	public static boolean isUsaDominioPagamentoCheque(String vlDominio) {
		return StringUtil.contains(usaInformacoesAdicionaisPagamentoCheque, vlDominio);
	}
	
	public static boolean isAplicaDescontoCategoria() {
		return (isAplicaDescontoCategoriaArredondandoCadaAplicacao() || isAplicaDescontoCategoriaArredondaSoResultado()) && LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido();
	}
	
	public static boolean isAplicaDescontoCategoriaArredondandoCadaAplicacao() {
		return usaDescontosCategoriaClienteEmCascataManuaisNaCapaPorPedido == 1 && permiteDescCascataCategoria;
	}
	
	public static boolean isAplicaDescontoCategoriaArredondaSoResultado() {
		return usaDescontosCategoriaClienteEmCascataManuaisNaCapaPorPedido == 2 && permiteDescCascataCategoria;
	}
	
	private static boolean isPermiteDescCascataCategoria() {
		return !isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() && !(permiteDescontoPercentualPorPedido > 0) && !isUsaDescontoPedidoPorClienteMaximo() &&
				isNaoAplicaDeflatorCondPagtoValorTotalPedido() && !aplicaIndiceFinanceiroClientePorPedido() && permiteDescontoEmValorPorPedido == 0 && !isUsaDescontoNoPedidoAplicadoPorItem() && !aplicaDescontoPedidoRepEspecial &&
				!usaDescontoPedidoPorTipoFrete && !usaDescontoPonderadoPedido && !usaValorMaximoBonificaoPorCreditoPedidoVenda && !isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem();
	}
	
	public static boolean isUsaDescontoNoPedidoAplicadoPorItem() {
		return isAtributoJsonLigado(configDescontoAcrescimoNoPedidoAplicadoPorItem, "usaDesconto", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
	}

	public static boolean isUsaAcrescimoNoPedidoAplicadoPorItem() {
		return isAtributoJsonLigado(configDescontoAcrescimoNoPedidoAplicadoPorItem, "usaAcrescimo", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
	}

	public static boolean isUsaDescontoOuAcrescimoAplicadoPorItem() {
		return isUsaDescontoNoPedidoAplicadoPorItem() || isUsaAcrescimoNoPedidoAplicadoPorItem();
	}

	public static boolean replicaDescontoAcrescimoDaCapaNosItens() {
		String valor = getValorAtributoJson(configDescontoAcrescimoNoPedidoAplicadoPorItem, "ocultaConfirmacaoDescAcresItens", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
		return valor.contains("1");
	}

	public static boolean nuncaReplicaDescontoAcrescimoDaCapaNosItens() {
		String valor = getValorAtributoJson(configDescontoAcrescimoNoPedidoAplicadoPorItem, "ocultaConfirmacaoDescAcresItens", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
		return valor.contains("2");
	}

	public static boolean ocultaConfirmacaoDescAcresItens() {
		if (!isUsaDescontoOuAcrescimoAplicadoPorItem()) return false;
		return replicaDescontoAcrescimoDaCapaNosItens() || nuncaReplicaDescontoAcrescimoDaCapaNosItens();
	}
	
	public static double getVlPctMaxDesconto() {
		if (!isUsaDescontoNoPedidoAplicadoPorItem()) return 0;
		
		return ValueUtil.getDoubleValue(getValorAtributoJson(configDescontoAcrescimoNoPedidoAplicadoPorItem, "vlPctMaxDesconto", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM));
	}
	
	public static boolean isAcumulaComDescDoItem() {
		return isUsaDescontoNoPedidoAplicadoPorItem() && isAtributoJsonLigado(configDescontoAcrescimoNoPedidoAplicadoPorItem, "acumulaComDescDoItem", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
	}
	
	public static boolean aplicaSomenteItemSemDesconto() {
		return isUsaDescontoNoPedidoAplicadoPorItem() && isAtributoJsonLigado(configDescontoAcrescimoNoPedidoAplicadoPorItem, "aplicaSomenteItemSemDesconto", ValorParametro.CONFIGDESCONTOACRESCIMONOPEDIDOAPLICADOPORITEM);
	}
	
	public static boolean selecionaTabPrecoMaiorDescPromo() {
		return permiteTabPrecoItemDiferentePedido() && isAtributoJsonLigado(permiteTabPrecoItemDiferentePedido, "selecionaTabPrecoMaiorDescPromo", ValorParametro.PERMITETABPRECOITEMDIFERENTEPEDIDO);
	}
	
	public static boolean isInverteNomeFantasiaRazaoSocialCliente() {
		return 1 == usaNmFantasiaNoLugarDaRazaoSocialDoCliente;
	}
	public static boolean sugereEnvioOrcamentoParaEmpresaECliente() {
		return modoSugestaoEnvioPedidoEmOrcamento == 1;
	}
	
	public static boolean sugereEnvioOrcamentoParaEmpresa() {
		return modoSugestaoEnvioPedidoEmOrcamento == 2;
	}
	
	public static boolean sugereEnvioOrcamentoParaCliente() {
		return modoSugestaoEnvioPedidoEmOrcamento == 3;
	}
	
	public static boolean naoSugereEnvioOrcamento() {
		return modoSugestaoEnvioPedidoEmOrcamento < 1 || modoSugestaoEnvioPedidoEmOrcamento > 3;
	}
	
	private static int getIntervaloBackup(Hashtable hashValorParametros) throws SQLException {
		String vlParametro = getValorParametroPorSistema(hashValorParametros, ValorParametro.USABACKUPAUTOMATICO);
		if (ValueUtil.VALOR_SIM.equals(vlParametro)) {
			return 1440;
		}
		if (ValueUtil.isValidNumber(vlParametro)) {
			return ValueUtil.getIntegerValue(vlParametro);
		}
		return -1;
	}
	
	
	public static boolean isUsaImpressaoFechamentoDiarioVendas() {
		return ValueUtil.valueEquals("3", usaImpressaoFechamentoDiarioVendas) || ValueUtil.valueEquals("4", usaImpressaoFechamentoDiarioVendas);
	}

	public static boolean isUsaImpressaoFinanceiroFechamentoDiario() {
		return ValueUtil.valueEquals("3", usaImpressaoFinanceiroFechamentoDiario) || ValueUtil.valueEquals("4", usaImpressaoFinanceiroFechamentoDiario);
	}
	
	public static int getNuDiasPermanenciaFechamentoDiario() {
		try {
			return Convert.toInt(nuDiasPermanenciaFechamentoDiario);
		} catch (Throwable e) {
			return 180;
		}
	}

	public static boolean isFilterFlModoEstoque() {
		return LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.usaModoControleEstoquePorTipoPedido;
	}
	
	public static boolean isUsaGeracaoPdfPedido() {
		return isUsaGeracaoPdfOnline() || isUsaGeracaoPdfOffline() || isSugereGeracaoPdfNoFechamento() || isGeraPdfOfflineAuto();
	}
	
	public static boolean isUsaExpressaoRegularValidacaoSenhaUsuario() {
		return !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, usaExpressaoRegularValidacaoSenhaUsuario);
	}
	
	public static boolean isQtdDiasExpiracaoSenhaLigado() {
		try {
			String[] vlParametro = nuDiasExpiracaoSenha.split(";");
			Convert.toInt(vlParametro[0]);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}
	
	public static int getQtdDiasExpiracaoSenha() {
		String[] vlParametro = nuDiasExpiracaoSenha.split(";");
		return ValueUtil.getIntegerValue(vlParametro[0]);
	}
	
	public static boolean isQtdDiasRestantesParaExpiracaoSenhaLigado() {
		try {
			String[] vlParametro = nuDiasExpiracaoSenha.split(";");
			if (ValueUtil.isNotEmpty(vlParametro) && vlParametro.length > 1) {
				Convert.toInt(vlParametro[1]);
				return true;
			} else {
				return false;
			}
		} catch (Throwable ex) {
			return false;
		}
	}
	
	public static int getQtdDiasRestantesParaExpiracaoSenha() {
		String[] vlParametro = nuDiasExpiracaoSenha.split(";");
		return ValueUtil.getIntegerValue(vlParametro[1]);
	}
	
	public static boolean isUsaSugestaoComboAposInsercao() {
		return verificaRegraDeSugestaoVendaBaseadaEmComboProdutos(Combo.SUGERE_APOS_INSERCAO);
	}
	
	public static boolean isUsaSugestaoComboFechamentoPedido() {
		return verificaRegraDeSugestaoVendaBaseadaEmComboProdutos(Combo.SUGERE_AO_FECHAR_PEDIDO);
	}

	public static boolean isUsaSugestaoComboAposInsercaoEFechamento() {
		 return isUsaSugestaoComboFechamentoPedido() && isUsaSugestaoComboAposInsercao();
	}
	
	public static boolean isExibeComboMenuInferior() {
		return verificaRegraDeSugestaoVendaBaseadaEmComboProdutos(Combo.OPCAO_MENU_INFERIOR, true);
	}

	public static int getNuDiasPermanenciaDadosEstoqueRep() {
		try {
			return Convert.toInt(nuDiasPermanenciaDadosEstoqueRep);
		} catch (Throwable e) {
			return 180;
		}
	}
	public static boolean isIgnoraConfiguracoesReplicacao(String conf) {
		String[] vlParams = configuracoesIgnoradasReplicacaoPedido.split(";");
		for (int i = 0; i < vlParams.length; i++) {
			if (conf.equals(vlParams[i])) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isModoResumoDiario(String dominio) {
		String usaModoResumoDiario = getValorAtributoJson(configModoResumoDiario, "usaModoResumoDiario", ValorParametro.CONFIGMODORESUMODIARIO);
		if (usaModoResumoDiario != null) {
			return Arrays.asList(usaModoResumoDiario.split(";")).contains(dominio);
		}
		return false;
	}

	public static boolean isModoResumoDiario() { return isModoResumoDiario("1"); }

	public static boolean isModoIndicadores() { return isModoResumoDiario("2"); }
	
	public static boolean isApresentaAbaBoleto() {
		return isUsaRetornoAutomaticoDadosPedidoBoletoBackground() || isUsaGeracaoImpressaoBoletoContingencia();
	}
	
	private static boolean isDestacaProdutosJaIncluidos(Hashtable hashValorParametros) throws SQLException {
		int vlParam = getVlParamAsInt(getValorParametroPorRepresentante(hashValorParametros, ValorParametro.DESTACAPRODUTOSJAINCLUIDOSAOPEDIDO));
		return vlParam == 1 || vlParam == 3;
	}
	
	public static boolean mostraVlPrecoMaximoConsumidorProduto() {
		return "1".equals(getValorAtributoJson(configPrecoMaximoConsumidor, "usa", ValorParametro.CONFIGPRECOMAXIMOCONSUMIDOR));
	}
	
	public static boolean mostraVlPrecoMaximoConsumidorItemTabPreco() {
		return "2".equals(getValorAtributoJson(configPrecoMaximoConsumidor, "usa", ValorParametro.CONFIGPRECOMAXIMOCONSUMIDOR));
	}
	
	public static boolean limitaPrecoMaximoConsumidor() {
		return ValueUtil.VALOR_SIM.equals(getValorAtributoJson(configPrecoMaximoConsumidor, "limitaPrecoMaximoConsumidor", ValorParametro.CONFIGPRECOMAXIMOCONSUMIDOR));
	}
	
	public static boolean isGeraCargaQuandoAtualizacaoMuitoGrande() {
		return geraCargaQuandoAtualizacaoMuitoGrande > 0;
	}
	
	public static boolean isSomaVlStNoVlTotalNfe(String dsTipoEmissaoNfe) {
		List<String> vlParams = Arrays.asList(somaValorSTNoValorTotalNfe.split(";"));
		if (Nfe.EMISSAO_NORMAL.equals(dsTipoEmissaoNfe)) {
			return vlParams.contains("1");
		} else if (Nfe.EMISSAO_CONTINGENCIA.equals(dsTipoEmissaoNfe)) {
			return vlParams.contains("2");
		}
		return false;
	}

	public static boolean isAplicaDescEmValorOuPctCapaPedido() {
		return aplicaDescontoNaCapaDoPedido > 0;
	}
	
	private static List<String> getConfigCadastroDescontoPromocional() {
		String[] vlParametro = configCadastroDescontoPromocional.split(";");
		return Arrays.asList(vlParametro);
	}
	
	public static boolean isHabilitaUsoGruposClientesCadastro() {
		return getConfigCadastroDescontoPromocional().contains("1");
	}
	
	public static boolean isUsaReservaEstoqueCentralizado() {
		return ValueUtil.VALOR_SIM.equals(usaReservaEstoqueCentralizado) || "1".equals(usaReservaEstoqueCentralizado) || "3".equals(usaReservaEstoqueCentralizado);
	}
	
	public static boolean isUsaReservaEstoqueCentralizadoAtomico() {
		return "2".equals(usaReservaEstoqueCentralizado) && !usaEnvioPedidoBackground;
	}
	
	public static boolean isUsaReservaEstoqueCentralizadoCapaPedido() {
		return "3".equals(usaReservaEstoqueCentralizado);
	}

	public static boolean isMostraFotoProduto() {
		return LavenderePdaConfig.mostraFotoProduto || LavenderePdaConfig.usaFotoProdutoPorEmpresa;
	}

	public static boolean isLoadImagesOnProdutoList() {
		return (isExibeFotoInsercaoMultiplos() &&  isOcultaInterfaceNegociacaoMultiplosItens()) || isMostraFotoProdutoNaListaProdutosDentroPedido();
	}

	public static boolean isPermiteInserirMultiplosItensPorVezNoPedido(Pedido pedido) throws SQLException {
		boolean isPermiteInserirMultiplos = LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido() && !pedido.isPedidoBonificacao();
		if (isPermiteInserirMultiplos && pedido.getTipoPedido() != null) {
			isPermiteInserirMultiplos = pedido.getTipoPedido().isInsereLote() && !pedido.getTipoPedido().isIgnoraInsercaoMultiplosItens();
		}
		return isPermiteInserirMultiplos;
	}

	public static boolean isUsaKitProduto() {
		return isUsaKitProdutoAberto() || isUsaKitProdutoFechado();
	}

	public static boolean isUsaKitProdutoAberto() {
		return ValueUtil.valueEquals("1", getValorAtributoJson(configKitProduto, "usa", ValorParametro.CONFIGKITPRODUTO));
	}

	public static boolean isUsaKitTipo3() {
		return ValueUtil.valueEquals("3", getValorAtributoJson(configKitProduto, "usa", ValorParametro.CONFIGKITPRODUTO));
	}

	public static boolean isUsaKitProdutoFechado() {
		return ValueUtil.valueEquals("2", getValorAtributoJson(configKitProduto, "usa", ValorParametro.CONFIGKITPRODUTO));
	}
	
	public static boolean isUsaUnidadeAlternativaKitProduto() {
		return isUsaKitProdutoFechado() && isAtributoJsonLigado(configKitProduto, "usaUnidadeAlternativa", ValorParametro.CONFIGKITPRODUTO);
	}
	
	public static boolean isApresentaPrecoTabela() {
		return isUsaKitTipo3() && isAtributoJsonLigado(configKitProduto, "apresentaPrecoTabela", ValorParametro.CONFIGKITPRODUTO);
	}
	
	public static boolean isPermiteItemBonificado() {
		return isUsaKitTipo3() && isAtributoJsonLigado(configKitProduto, "permiteItemBonificado", ValorParametro.CONFIGKITPRODUTO);
	}
	
	public static boolean isUsaAgrupadorKitPolitica() {
		return isUsaKitTipo3() && isAtributoJsonLigado(configKitProduto, "usaAgrupadorKitPolitica", ValorParametro.CONFIGKITPRODUTO);
	}

	public static boolean isUsaValidaEstoqueKit() {
		return isUsaKitTipo3() && isAtributoJsonLigado(configKitProduto, "usaValidaEstoque", ValorParametro.CONFIGKITPRODUTO);
	}
	
	public static boolean isMantemValorKitNaReplicacao() {
		return isUsaKitTipo3() && isAtributoJsonLigado(configKitProduto, "mantemValorKitNaReplicacao", ValorParametro.CONFIGKITPRODUTO);
	}
	
	public static boolean isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento() {
		return getAplicarDescontosIndicesParaSaldoFlexNegativo().contains("1");
	}
	
	public static boolean isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido() {
		return getAplicarDescontosIndicesParaSaldoFlexNegativo().contains("2");
	}
	
	public static boolean isAplicarDescontosIndicesParaSaldoFlexNegativo() {
		return isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento() || isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido();
	}
	
	private static List<String> getAplicarDescontosIndicesParaSaldoFlexNegativo() {
		try {
			return Arrays.asList(aplicarDescontosIndicesParaSaldoVerbaNegativo.split(";"));
		} catch (Throwable ex) {
			return Arrays.asList(ValueUtil.VALOR_NAO);
		}
	}
	
	public static boolean isUsaMultiplasLiberacoesParaPedidoPendente() {
		return getNuOrdemLiberacaoPedidoPendente() > 0;
	}
	
	public static int getNuOrdemLiberacaoPedidoPendente() {
		return ValueUtil.getIntegerValue(nuOrdemLiberacaoPedidoPendente);
	}
	
	public static boolean isHabilitaUsoCondicaoComercialCadastro() {
		return getConfigCadastroDescontoPromocional().contains("2");
	}

	public static boolean isAplicaIndiceCondicaoComercialNoPedido() {
		return usaCondicaoComercialPedido && 
				isHabilitaUsoCondicaoComercialCadastro();
	}
	
	private static boolean isControlaEstoquePorLoteProduto() {
		return isAtributoJsonLigado(configControleEstoquePorLoteProduto, "usa", ValorParametro.CONFIGCONTROLEESTOQUEPORLOTEPRODUTO);
	}
	
	public static boolean isUsaControleEstoquePorLoteProduto() {
		return isControlaEstoquePorLoteProduto() && usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil;
	}
	
	public static boolean isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco() {
		if (!isUsaControleEstoquePorLoteProduto()) return false;
		
		return isAtributoJsonLigado(configControleEstoquePorLoteProduto, "usaTabPrecoLoteProd", ValorParametro.CONFIGCONTROLEESTOQUEPORLOTEPRODUTO);
	}
	
	public static boolean isPermitePedidoTributacaoNovoCliente() {
		return LavenderePdaConfig.isPermitePedidoNovoCliente() && LavenderePdaConfig.isUsaCalculaStItemPedido();
	}
	public static boolean isUsaSelecaoUnidadePorGrid() {
		return usaSelecaoUnidadePorGrid > 0;
	}

	public static int getUsaSelecaoUnidadePorGrid() {
		return usaSelecaoUnidadePorGrid;
	}
	

	public static boolean isApresentaSaldoVerbaPedidoInTotais() {
		String vlParametro[] = apresentaSaldoVerbaPedido.split(";");
		for (int i = 0; i < vlParametro.length; i++) {
			if (ValueUtil.valueEquals(vlParametro[i], "2")) return true;
		}
		return false;
	}

	public static boolean isApresentaSaldoVerbaPedidoInRelVerba() {
		String vlParametro[] = apresentaSaldoVerbaPedido.split(";");
		for (int i = 0; i < vlParametro.length; i++) {
			if (ValueUtil.valueEquals(vlParametro[i], "1")) return true;
		}
		return false;
	}
	
	private static List<String> getCondicaoPagamentoOcultoNoPedido() {
		try {
			String valorAtributo = getValorAtributoJson(configCondicaoPagamentoNoPedido, "ocultaCondicaoPagamento", ValorParametro.CONDICAOPAGAMENTOOCULTONOPEDIDO);
			return Arrays.asList(valorAtributo.split(";"));
		} catch (Throwable ex) {
			return Arrays.asList(ValueUtil.VALOR_NAO);
		}
	}
		
	public static boolean isOcultaSelecaoCondicaoPagamentoPedido() {
		return getCondicaoPagamentoOcultoNoPedido().contains("1"); 
	} 
	
	public static boolean isOcultaSelecaoCondicaoPagamentoPagamentoPedido() {
		return getCondicaoPagamentoOcultoNoPedido().contains("2"); 
	}
	
	public static boolean isOcultaSelecaoCondicaoPagamento() {
		return  isOcultaSelecaoCondicaoPagamentoPedido() || isOcultaSelecaoCondicaoPagamentoPagamentoPedido();
	}
	
	public static boolean isNuParcelasNoPedido() {
		if (isOcultaSelecaoCondicaoPagamento()) return false;
	
		return isAtributoJsonLigado(configCondicaoPagamentoNoPedido, "nuParcelasNoPedido", ValorParametro.CONDICAOPAGAMENTOOCULTONOPEDIDO);
	}

	public static boolean isUsaEnvioRecadoNoCancelamentoPedido() {
		return isUsaEnvioRecadoPedido("1");
	}
	
	private static boolean isUsaEnvioRecadoPedido(String vlDominio) {
		if (ValueUtil.isEmpty(usaEnvioRecadoPedido) || ValueUtil.valueEquals(ValueUtil.VALOR_NAO, usaEnvioRecadoPedido)) return false;
		
		List<String> vlParametroList = Arrays.asList(usaEnvioRecadoPedido.split(";"));
		return vlParametroList.contains(vlDominio);
	}
			
	private static boolean isMostraProdutoPromocionalDestacado(String vlDominio) {
		if (!isUsaFiltroProdutoPorTabelaPreco() && !isUsaFiltroProdutoPorTabelaPrecoTipo3()) return false;
		
		if (ValueUtil.isEmpty(mostraProdutoPromocionalDestacado) || ValueUtil.valueEquals(ValueUtil.VALOR_NAO, mostraProdutoPromocionalDestacado)) return false;
		
		List<String> vlParametroList = Arrays.asList(mostraProdutoPromocionalDestacado.split(";"));
		return vlParametroList.contains(vlDominio);
		
	}
	
	public static int[] getDominiosInformacoesComplementaresInsercMultipla() {
		String[] values = getListaCamposInformacoesComplementaresMultiplosItensSemNegociacao().split(";");
		int length = values.length;
		Convert.qsort(values, 0, length - 1, Convert.SORT_INT, true);
		int[] intValues = new int[length];
		for (int i = 0; i < length; i++) {
			intValues[i] = ValueUtil.getIntegerValue(values[i]);
		}
		return intValues;
	}
	
	public static boolean isMostraCampoValorUnitarioEmbalagemListaProdutosDentroPedido() {
		int[] parametros = getDominiosInformacoesComplementaresInsercMultipla();
		for (int i = 0; i < parametros.length; i++) {
			if (parametros[i] == 5) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean showInformacoesComplementaresInsercMultipla() {
		return !ValueUtil.VALOR_NAO.equals(getListaCamposInformacoesComplementaresMultiplosItensSemNegociacao());
	}
	
	public static boolean isApresentaPrecoCondComercialCli() {
		return apresentaPrecoCondComercialCli && isUsaCondicaoComercialCliComDefault();
	}
	
	public static boolean isMarcaItemPedidoPendenteAprovacao() {
		return marcaItemPedidoPendenteAprovacao == 1 || marcaItemPedidoPendenteAprovacao == 2 || isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow();
	}
	
	public static boolean isMarcaItemPedidoPendenteAprovacaoMaxDescItem() {
		return marcaItemPedidoPendenteAprovacao == 1;
	}
	
	public static boolean isMarcaItemPedidoPendenteAprovacaoMaxDescUsuario() {
		return marcaItemPedidoPendenteAprovacao == 2;
	}
	
	public static boolean isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow() {
		return marcaItemPedidoPendenteAprovacao == 3 && "2".equals(usaMultiplasLiberacoesDescontoNoPedido) || isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDesconto();
	}
	
	public static boolean usaMultiplasLiberacoesDescontoNoPedido() {
		return ValueUtil.VALOR_SIM.equals(usaMultiplasLiberacoesDescontoNoPedido) || "2".equals(usaMultiplasLiberacoesDescontoNoPedido);
	}

	public static int getValorTimeOutCatalogoProduto() {
		int valorTimeOutCatalogo = valorTimeOutGeracaoCatalogoProduto();
		return valorTimeOutCatalogo <= 0 ? 90000 : valorTimeOutCatalogo * 1000;
	}
	
	public static int getQtdMinimaProdutosRestritos() {
		String[] vlParams = qtdMinimaProdutosRestricaoPromocionalPedido.split(";");
		return ValueUtil.getIntegerValue(vlParams[0]);
	}
	
	public static int getQtdMinimaProdutosPromocionais() {
		String[] vlParams = qtdMinimaProdutosRestricaoPromocionalPedido.split(";");
		if (vlParams.length > 1) {
			return ValueUtil.getIntegerValue(vlParams[1]);
		}
		return ValueUtil.getIntegerValue(vlParams[0]);
	}
	
	public static boolean usaQtdMinimaProdutosRestritosPromocionais() {
		return getQtdMinimaProdutosRestritos() > 0 || getQtdMinimaProdutosPromocionais() > 0;
	}
	
	public static boolean isValidaRegraProdutoRestritoEPromocionalSeparadamente() {
		return validaRegraProdutoRestritoEPromocionalSeparadamente && qtdMinimaProdutosRestricaoPromocionalPedido.contains(";");
	}

	public static List<String> getDsDadosAdicionaisImpressaoFechamentoDiarioVendas() {
		return Arrays.asList(dsDadosAdicionaisImpressaoFechamentoDiarioVendas.split(";"));
	}
	
	public static boolean utilizaEscolhaTransportadoraNoFechamentoPedido() {
		return ValueUtil.valueEquals("1", getValorAtributoJson(configEscolhaTransportadoraNoPedido, "usa", ValorParametro.CONFIGESCOLHATRANSPORTADORANOPEDIDO));
	}
	
	public static boolean utilizaEscolhaTransportadoraNoInicioPedido() {
		return ValueUtil.valueEquals("2", getValorAtributoJson(configEscolhaTransportadoraNoPedido, "usa", ValorParametro.CONFIGESCOLHATRANSPORTADORANOPEDIDO));
	}
	
	public static boolean isExibeInformacoesFreteCapaPedidoEscolhaTransportadora() {
		return utilizaEscolhaTransportadora() && !isAtributoJsonDesligado(configEscolhaTransportadoraNoPedido, JSON_KEY_EXIBEINFOFRETECAPAPEDIDO, ValorParametro.CONFIGESCOLHATRANSPORTADORANOPEDIDO);
	}
	
	public static List<String> getDominiosInformacoesFreteCapaPedidoEscolhaTransportadora() {
		return Arrays.asList(getValorAtributoJson(configEscolhaTransportadoraNoPedido, JSON_KEY_EXIBEINFOFRETECAPAPEDIDO, ValorParametro.CONFIGESCOLHATRANSPORTADORANOPEDIDO).split(";"));
	}
	
	public static boolean utilizaEscolhaTransportadora() {
		return utilizaEscolhaTransportadoraNoInicioPedido() || utilizaEscolhaTransportadoraNoFechamentoPedido();
	}
	
	public static boolean escolhaTransportadoraPedidoPorRegiao() {
		return ValueUtil.valueEquals("1", getValorAtributoJson(configOrigemDadosEscolhaTransportadoraNoPedido, "usa", ValorParametro.ORIGEMDADOSESCOLHATRANSPORTADORANOPEDIDO));
	}
	
	public static boolean escolhaTransportadoraPedidoPorCep() {
		return ValueUtil.valueEquals("2", getValorAtributoJson(configOrigemDadosEscolhaTransportadoraNoPedido, "usa", ValorParametro.ORIGEMDADOSESCOLHATRANSPORTADORANOPEDIDO));
	}
	
	public static boolean isOcultaComboTransportadoraPorRegiao() {
		return escolhaTransportadoraPedidoPorRegiao() && isAtributoJsonLigado(configOrigemDadosEscolhaTransportadoraNoPedido, "ocultaComboRegiao", ValorParametro.ORIGEMDADOSESCOLHATRANSPORTADORANOPEDIDO); 
	}
	
	public static boolean usaEscolhaTransportadoraPedido() {
		return escolhaTransportadoraPedidoPorRegiao() || escolhaTransportadoraPedidoPorCep(); 
	}
	
	public static boolean isIdentificaSemanaAgendaVisitaConformeSemanaMes() {
		return modoDeIdentificarSemanaDoMesNaAgendaVisita == 2;
	}

	public static boolean isOcultaEstoque() {
		String vlParametroPrimeiraPosicao = getVlParametro(ocultaEstoque, ";").get(0); 
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlParametroPrimeiraPosicao);
	}
	
	public static boolean isOcultaTotalizadoresDeEstoque() {
		List<String> vlParametroList = getVlParametro(ocultaEstoque, ";");
		if (vlParametroList.size() == 1) return false;
		
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, vlParametroList.get(1));
	}
	
	private static List<String> getVlParametro(String vlParametro, String separador) {
		try {
			return Arrays.asList(vlParametro.split(separador));
		} catch (Throwable e) {
			return Arrays.asList(ValueUtil.VALOR_NAO);
		}
	}
	
	public static boolean isNaoMostraListaEstoqueGradeFaltante() {
		return !isExibeColunaComEstoqueDisponivelParaProdutoGrade() && !isExibeColunaComQuantidadeExcedenteParaProdutoGrade() ;
	}
	
	public static boolean isExibeColunaComEstoqueDisponivelParaProdutoGrade() {
		return  ValueUtil.valueEquals("1", mostraListaEstoqueGradeFaltante);
	}
	
	public static boolean isExibeColunaComQuantidadeExcedenteParaProdutoGrade() {
		return  ValueUtil.valueEquals("2", mostraListaEstoqueGradeFaltante);
	}
	
	public static boolean isFiltraItensComErroNaInsercaoMultiplaItens() {
		return ValueUtil.VALOR_SIM.equals(filtraItensComErroNaInsercaoMultiplaItens) || ("1").equals(filtraItensComErroNaInsercaoMultiplaItens) || ("2").equals(filtraItensComErroNaInsercaoMultiplaItens);
	}

	public static boolean isFiltraItensComErroNaInsercaoMultiplaItensRevisar() {
		return ("2").equals(filtraItensComErroNaInsercaoMultiplaItens);
	}
	
	public static boolean isMostraPrecosPorCondicaoPagamento() {
		return ValueUtil.VALOR_SIM.equals(mostraPrecosPorCondicaoPagamento) || ("1").equals(mostraPrecosPorCondicaoPagamento) || ("2").equals(mostraPrecosPorCondicaoPagamento);
	}

	public static boolean isMostraPrecosPorCondicaoPagamentoPercentual() {
		return ("2").equals(mostraPrecosPorCondicaoPagamento);
	}
		
	public static boolean apresentaMultiplasSugestoesLocaisAdicionais(String compareValue) {
		String[] vlParams = localApresentacaoMultiplasSugestoesProduto.split(";");
		for (String vlParam : vlParams) {
			if (compareValue.equals(vlParam)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isUsaFiltroAplicacaoDoProduto() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(usaFiltroAplicacaoDoProduto) || "1".equals(usaFiltroAplicacaoDoProduto) ||isUsaFiltroAplicacaoDoProdutoSeparado();
	}
	
	public static boolean isUsaFiltroAplicacaoDoProdutoSeparado() {
		return "2".equals(usaFiltroAplicacaoDoProduto);
	}
	
	public static boolean isUsaFiltroGradeProduto() {
		return ("1").equals(usaFiltroGradeProduto) || ("2").equals(usaFiltroGradeProduto); 
	}
	
	public static boolean isUsaFiltroGradeProdutoPorCamera() {
		return ("2").equals(usaFiltroGradeProduto); 
	}
	
	public static boolean isBloqueiaCondPagtoPorCliente() {
		return "1".equals(bloqueiaCondPagtoPorCliente) || "2".equals(bloqueiaCondPagtoPorCliente);
	}
	
	public static boolean isBloqueiaCondPagtoPorClienteAddItemPedidoFimPedido() {
		return "1".equals(bloqueiaCondPagtoPorCliente);
	}
	
	public static boolean isBloqueiaCondPagtoPorClienteNoCombo() {
		return "2".equals(bloqueiaCondPagtoPorCliente);
	}
	
	public static void carregaMapFiltrosProdutos(Map<String, Boolean> filtrosVisiveisMap, Map<String, Boolean> filtrosNaoAutomaticosMap, int position) {
		Vector filtrosVisiveis = new Vector(StringUtil.split(position == 0 ? filtrosFixoTelaListaProduto : filtrosFixoTelaItemPedido, ';'));
		for (int i = 0; i < filtrosVisiveis.size(); i++) {
			filtrosVisiveisMap.put((String) filtrosVisiveis.elementAt(i), true);
		}
		if (filtrosNaoAutomaticosMap == null) return;
		Vector vlParam = new Vector(StringUtil.split(getValorAtributoJson(configFiltroListaProdutos, "naoRealizaFiltroAutomatico", ValorParametro.CONFIGFILTROLISTAPRODUTOS), '|'));
		if (vlParam != null && vlParam.size() > 1) {
			Vector filtrosNaoAutomaticosDentroPedido = new Vector(StringUtil.split((String) vlParam.elementAt(position), ';'));
			for (int i = 0; i < filtrosNaoAutomaticosDentroPedido.size(); i++) {
				filtrosNaoAutomaticosMap.put((String) filtrosNaoAutomaticosDentroPedido.elementAt(i), true);
			}
		}
	}
	
	public static String getMessageCustomLeftTotalizer(int size) {
		String registrosMsg;
		StringBuffer msg = new StringBuffer();
		msg.append(size);
		if (size >= LavenderePdaConfig.nuLinhasRetornoBuscaSistema && LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0) {
			msg.append(" ");
			registrosMsg = Messages.LABEL_PRIMEIROS_REGISTROS;
		} else {
			registrosMsg = size > 1 ? FrameworkMessages.REGISTROS_PLURAL : FrameworkMessages.REGISTRO_SINGULAR;
		}
		msg.append(registrosMsg);
		return msg.toString();
	}
	
	private static String loadNivelLogSyncBackground(Hashtable hashValorParametros) throws SQLException {
		String vlParam = getValorParametroPorRepresentante(hashValorParametros, ValorParametro.NIVELLOGSYNCBACKGROUND);
		LavendereConfig.getInstance().nivelLogSyncBackground = vlParam;
		return vlParam;
	}
	
	public static boolean isLogSyncBackgroundLigado() {
		return !ValueUtil.VALOR_NAO.equals(nivelLogSyncBackground);
	}
	
	public static boolean isRestauraBackupVersaoQuestionandoUsuario() {
		return restauraBackupAutoAposAtualizarVersao == 1;
	}
	
	public static boolean isRestauraBackupVersaoSemQuestionarUsuario() {
		return restauraBackupAutoAposAtualizarVersao == 2 || restauraBackupAutoAposAtualizarVersao == 3;
	}
		
	public static boolean isRestauraBackupAutomaticoPermitindoAtualizarVersaoComPedidoAberto() {
		return restauraBackupAutoAposAtualizarVersao == 3;
	}

	public static boolean isUsaObrigaInfoComplementarItemPedido() {
		return getInfoComplementarItemPedidoListaCamposObrigatorios().size() > 0;
	}
	
	public static Map<String, String> loadCamposObrigatoriosInfoComplementarItemPedido() {
		List<String> list = getInfoComplementarItemPedidoListaCamposObrigatorios();
		Map<String, String> result = new HashMap<String, String>();
		for (String obrigatorio : list) {
			switch (obrigatorio) {
			case "dtEntrega":
				result.put(Messages.LABEL_DTENTREGA, Messages.LABEL_DTENTREGA);
				break;
			case "vlAltura":
				result.put(Messages.LABEL_ALTURA, Messages.LABEL_ALTURA);
				break;
			case "vlLargura":
				result.put(Messages.LABEL_LARGURA, Messages.LABEL_LARGURA);
				break;
			case "vlComprimento":
				result.put(Messages.LABEL_VLCOMPRIMENTO, Messages.LABEL_VLCOMPRIMENTO);
				break;
			case "vlPosVinco1":
				result.put(Messages.LABEL_VLPOSVINCO1, Messages.LABEL_VLPOSVINCO1);
				break;
			case "vlPosVinco2":
				result.put(Messages.LABEL_VLPOSVINCO2, Messages.LABEL_VLPOSVINCO2);
				break;
			case "vlPosVinco3":
				result.put(Messages.LABEL_VLPOSVINCO3, Messages.LABEL_VLPOSVINCO3);
				break;
			case "vlPosVinco4":
				result.put(Messages.LABEL_VLPOSVINCO4, Messages.LABEL_VLPOSVINCO4);
				break;
			case "vlPosVinco5":
				result.put(Messages.LABEL_VLPOSVINCO5, Messages.LABEL_VLPOSVINCO5);
				break;
			case "vlPosVinco6":
				result.put(Messages.LABEL_VLPOSVINCO6, Messages.LABEL_VLPOSVINCO6);
				break;
			case "vlPosVinco7":
				result.put(Messages.LABEL_VLPOSVINCO7, Messages.LABEL_VLPOSVINCO7);
				break;
			case "vlPosVinco8":
				result.put(Messages.LABEL_VLPOSVINCO8, Messages.LABEL_VLPOSVINCO8);
				break;
			case "vlPosVinco9":
				result.put(Messages.LABEL_VLPOSVINCO9, Messages.LABEL_VLPOSVINCO9);
				break;
			case "vlPosVinco10":
				result.put(Messages.LABEL_VLPOSVINCO10, Messages.LABEL_VLPOSVINCO10);
				break;
			}
		}
		return result;
	}
		
	public static boolean usaValorRentabilidadeSemCalculo() {
		return formulaCalculoRentabilidadeNoPedido == 5 || formulaCalculoRentabilidadeNoPedido == 6 || formulaCalculoRentabilidadeNoPedido == 7 || formulaCalculoRentabilidadeNoPedido == 9;
	}
		
	public static boolean isUsaRentabilidadeNoPedido() {
		return isAtributoJsonLigado(configRentabilidadeNoPedido, "usa", ValorParametro.CONFIGRENTABILIDADENOPEDIDO);
	}
	
	public static boolean isUsaFormulaRentabilidadePedido(int formula) {
		return isUsaRentabilidadeNoPedido() && formulaCalculoRentabilidadeNoPedido == formula;
	}
	
	private static int getFormulaCalculo() {
		int vlParam = ValueUtil.getIntegerValue(getValorAtributoJson(configRentabilidadeNoPedido, "formulaCalculo", ValorParametro.CONFIGRENTABILIDADENOPEDIDO));
		return !isUsaRentabilidadeNoPedido() || vlParam > 9 || vlParam < 1 ? 0 : vlParam;
	}
	
	public static boolean isApresentaRentabilidadeNaListaPedido() {
		return isAtributoJsonLigado(configRentabilidadeNoPedido, "apresentaNaListaPedido", ValorParametro.CONFIGRENTABILIDADENOPEDIDO);
	}
	
	public static boolean isIndiceFinanceiroClienteVlItemPedido() {
		String valorAtributoJson = getValorAtributoJson(configIndiceFinanceiroClienteVlItemPedido, "usa", ValorParametro.CONFIGINDICEFINANCEIROCLIENTEVLITEMPEDIDO);
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, valorAtributoJson) || ValueUtil.valueEquals("1", valorAtributoJson);
	}

	public static int getNuCasasDecimaisCalcIndicFinanceiroCliente() {
		String valorAtributoJson = getValorAtributoJson(configIndiceFinanceiroClienteVlItemPedido, "nuCasasDecimaisCalcIndicFinanceiroCliente", ValorParametro.CONFIGINDICEFINANCEIROCLIENTEVLITEMPEDIDO);
		return ValueUtil.isEmpty(valorAtributoJson) ? 0 : ValueUtil.getIntegerValue(valorAtributoJson);
	}

	public static boolean isMostraQtVendasProdutoNoPeriodo() {
		return ValueUtil.VALOR_SIM.equals(mostraQtVendasProdutoNoPeriodo) || "1".equals(mostraQtVendasProdutoNoPeriodo) || "2".equals(mostraQtVendasProdutoNoPeriodo);
	}
	
	public static boolean usaRestricaoVendaProdutoPorCliente(String compareValue) {
		return isDominioLigado(usaRestricaoVendaProdutoPorCliente, compareValue);
	}
	
	public static boolean usaRestricaoVendaProdutoPorCliente() {
		return usaRestricaoVendaProdutoPorCliente(RestricaoVendaCli.RESTRICAO_PEDIDO_VENDA) || usaRestricaoVendaProdutoPorCliente(RestricaoVendaCli.RESTRICAO_PEDIDO_BONIFICACAO);
	}
	
	public static boolean isConfigColunasDescontoVolumeVendaMensalDesligado() {
		return ValueUtil.VALOR_NAO.equals(configColunasGridDescontoVolumeVendasMensal);
	}
	
	public static boolean isTodasColunasVisiveisGridDescontoVolumeVendaMensal() {
		return ValueUtil.VALOR_ZERO.equals(configColunasGridDescontoVolumeVendasMensal);
	}
	
	public static boolean isColunaFaixaVisivelGridDescontoVolumeVendaMensal() {
		return isDominioLigado(configColunasGridDescontoVolumeVendasMensal, "1");
	}
	
	public static boolean isColunaPctDescontoVisivelGridDescontoVolumeVendaMensal() {
		return isDominioLigado(configColunasGridDescontoVolumeVendasMensal, "2");
	}
	
	public static boolean isColunaVlPedidoVisivelGridDescontoVolumeVendaMensal() {
		return isDominioLigado(configColunasGridDescontoVolumeVendasMensal, "3");
	}
	
	public static boolean isConfigColunasGridDescontoQuantidadeDesligado() {
		return ValueUtil.VALOR_NAO.equals(configColunasGridDescontoQuantidade);
	}
	
	public static boolean isTodasColunasVisiveisGridDescontoQuantidade() {
		return ValueUtil.VALOR_ZERO.equals(configColunasGridDescontoQuantidade);
	}
	
	public static boolean isColunaQuantidadeVisivelGridDescontoQuantidade() {
		return isDominioLigado(configColunasGridDescontoQuantidade, "1");
	}
	
	public static boolean isColunaPctDescontoVisivelGridDescontoQuantidade() {
		return isDominioLigado(configColunasGridDescontoQuantidade, "2");
	}
	
	public static boolean isColunaVlItemVisivelGridDescontoQuantidade() {
		return isDominioLigado(configColunasGridDescontoQuantidade, "3");
	}
	
	public static boolean isUsaDescontoPorVolumeVendaMensal() {
		return aplicaFaixaDescontoVolumeVendaMensalPorItem() || aplicaFaixaDescontoVolumeVendaMensalPorPedido();
	}
	
	public static boolean aplicaFaixaDescontoVolumeVendaMensalPorItem() {
		return "1".equals(usaDescontoPorVolumeVendaMensal);
	}
	
	public static boolean aplicaFaixaDescontoVolumeVendaMensalPorPedido() {
		return "2".equals(usaDescontoPorVolumeVendaMensal);
	}
	
	public static boolean usaConfigCalculoComissao() {
		return !ValueUtil.VALOR_NAO.equalsIgnoreCase(getValorAtributoJson(configCalculoComissao, "usa", ValorParametro.CONFIGCALCULOCOMISSAO));
		}
	
	public static double getNuMultiplicadorComissao() {
		return ValueUtil.getDoubleValue(getValorAtributoJson(configCalculoComissao, "nuMultiplicadorComissao", ValorParametro.CONFIGCALCULOCOMISSAO));
	}
	
	public static int getNuCasasDecimaisCalculoComissao() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configCalculoComissao, "nuCasasDecimaisCalculoComissao", ValorParametro.CONFIGCALCULOCOMISSAO));
	}

	public static boolean usaConfigCalculoComissaoPorFaixaDesconto() {
		return !ValueUtil.VALOR_NAO.equals(configCalculoComissaoPorFaixaDesconto);
	}
	
	public static String[] getConfigCalculoComissao() {
		try {
			return getValorAtributoJson(configCalculoComissao, "usa", ValorParametro.CONFIGCALCULOCOMISSAO).split(";");
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return new String[] {ValueUtil.VALOR_NAO};
		}
	}
	
	public static boolean ocultaValoresComissao() {
		return isAtributoJsonLigado(configCalculoComissao, "ocultaValoresComissao", ValorParametro.CONFIGCALCULOCOMISSAO);
	}

	public static boolean mostraDescontoAcrescimoRelComissao() {
		return isAtributoJsonLigado(configCalculoComissao, "mostraDescontoAcrescimoRelComissao", ValorParametro.CONFIGCALCULOCOMISSAO);
	}

	public static String[] getConfigCalculoComissaoPorFaixaDesconto() {
		return configCalculoComissaoPorFaixaDesconto.split(";");
	}	

	public static boolean isUsaModuloPagamento() {
		return ValueUtil.valueEquals("1", usaModuloPagamento)  || isUsaModuloPagamentoComAdicional(); 
	}
	
	public static boolean isUsaModuloPagamentoComAdicional() {
		return ValueUtil.valueEquals("2", usaModuloPagamento);
	}
	
	public static int qtdCopiasImpressaoModuloPagamento() { 
		if (usaImpressaoModuloPagamento.size() == 1) return 1;
		
		try {
			 return Convert.toInt(usaImpressaoModuloPagamento.get(1));
		} catch (Throwable e) {
			return 1;
		}
	}
	
	public static boolean isUsaSistemaIdiomaEspanhol() {
		return ValueUtil.valueEquals(FrameworkMessages.IDIOMA_ESPANHOL, usaSistemaIdiomaPersonalizado);
	}

	public static boolean isUsaSistemaIdiomaIngles() {
		return ValueUtil.valueEquals(FrameworkMessages.IDIOMA_INGLES, usaSistemaIdiomaPersonalizado);
	}

	private static void loadParamRecalculoValoresDosPedidos() throws SQLException {
		List<String> paramValues = getVlParametro(getValorParametroPorRepresentante(ValorParametro.USARECALCULOVALORESDOSPEDIDOS), ";");
		if (paramValues.size() == 3) {
			nuDiasAposEmissaoRecalValPed = ValueUtil.getIntegerValue(paramValues.get(0));
			nuDiasAposUltimoRecalculoValPed = ValueUtil.getIntegerValue(paramValues.get(1));
			usaRecalculoValoresDosPedidosInicioDoMes = ValueUtil.VALOR_SIM.equals(paramValues.get(2));
			usaRecalculoValoresDosPedidos = nuDiasAposEmissaoRecalValPed > 0 || nuDiasAposUltimoRecalculoValPed > 0 || usaRecalculoValoresDosPedidosInicioDoMes;
		}
	}
	
	public static boolean isMostraFlexPositivoPedido() throws SQLException {
		return isMostraFlexPositivoPedido(null);
	}
	
	public static boolean isMostraFlexPositivoPedido(String origem) throws SQLException {
		if (ValueUtil.isEmpty(mostraFlexPositivoPedido)) return false;
		String[] parametro = mostraFlexPositivoPedido.split(";");
		if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, parametro[0])) {
			if (parametro.length == 1 || ValueUtil.isEmpty(origem)) return true;
			List<String> origensPermitidas = getVlParametro(parametro[1], ",");
			return origensPermitidas.contains(origem);
		}
		return false;
	}
	
	public static boolean usaCalculoVpcItemPedido() {
		return "1".equals(usaCalculoVpcItemPedido) || "2".equals(usaCalculoVpcItemPedido);
	}
	
	public static boolean usaCalculoVpcItemPedidoParaTodosClientes() {
		return "2".equals(usaCalculoVpcItemPedido);
	}
	
	public static boolean usaCalculoVpcItemPedidoSomenteClientesVpcInformado() {
		return "1".equals(usaCalculoVpcItemPedido);
	}

	public static boolean isUsaReplicacaoPedido() {
		return ValueUtil.VALOR_SIM.equals(usaReplicacaoPedido) || "P".equals(usaReplicacaoPedido);
	}
	
	private static List<String> getVlConfigNotaCreditoParaPedido() {
		return getVlParametro(configNotaCreditoParaPedido, ";"); 
	}
	
	private static boolean utilizaNotasCreditoMasNaoObrigaSelecaoDeNotaCredito() {
		return ValueUtil.valueEquals("1", getVlConfigNotaCreditoParaPedido().get(0));
	}
	
	public static boolean utilizaNotasCreditoEObrigaSelecaoDeNotaCredito() {
		return ValueUtil.valueEquals("2", getVlConfigNotaCreditoParaPedido().get(0));
	}
	
	public static boolean utilizaNotasCredito() {
		return utilizaNotasCreditoMasNaoObrigaSelecaoDeNotaCredito() || utilizaNotasCreditoEObrigaSelecaoDeNotaCredito();
	}
	
	public static double getVlAdicionalNotaCredito() {
		List<String> vlConfigNotaCreditoParaPedido = getVlConfigNotaCreditoParaPedido();
		if (vlConfigNotaCreditoParaPedido.size() == 1) return 0;
		
		return ValueUtil.getDoubleSimpleValue(getVlConfigNotaCreditoParaPedido().get(1));
	}
	
	public static boolean apresentaCampoPercentualDescontoCapaPedido() {
		return ValueUtil.valueEquals("1", aplicaIndiceFinanceiroClientePorPedido);
	}
	
	public static boolean apresentaMensagemClientePossuiIndiceFinanceiroConfigurado() {
		return ValueUtil.valueEquals("2", aplicaIndiceFinanceiroClientePorPedido);
	}
	
	public static boolean aplicaIndiceFinanceiroClientePorPedido() {
		return apresentaCampoPercentualDescontoCapaPedido() || apresentaMensagemClientePossuiIndiceFinanceiroConfigurado();
	}
	
	public static boolean isControlaDescontoUsandoVerbaAtravesMixItens() {
		return controlaDescontoUsandoVerbaAtravesMixItens > 0;
	}
	
	public static int getNuMaxDiasPrevisaoEntregaDoPedido() {
		int maxDias = ValueUtil.getIntegerValue(nuMaxDiasPrevisaoEntregaDoPedido.split(";")[0]);
		return maxDias;
	}
	
	public static boolean isNuMaxDiasPrevisaoEntregaDoPedidoConsideraDiasCorridos() {
		String[] parametro = nuMaxDiasPrevisaoEntregaDoPedido.split(";");
		return parametro.length > 1 && ValueUtil.valueEquals(parametro[1], "C");
	}
	
	public static boolean isShowDiferencaPedidoStatusPedido() {
		return isDominioLigado(configPersonalizadaParaDiferencasPedido, "1") || ValueUtil.VALOR_NAO.equals(configPersonalizadaParaDiferencasPedido);
	}

	public static boolean isShowDiferencaPedidoObsPedido() {
		return isDominioLigado(configPersonalizadaParaDiferencasPedido, "2") || ValueUtil.VALOR_NAO.equals(configPersonalizadaParaDiferencasPedido);
	}

	public static boolean isShowDiferencaPedidoQtdItemPedido() {
		return isDominioLigado(configPersonalizadaParaDiferencasPedido, "3") || ValueUtil.VALOR_NAO.equals(configPersonalizadaParaDiferencasPedido);
	}

	public static boolean isShowDiferencaPedidoObsItemPedido() {
		return isDominioLigado(configPersonalizadaParaDiferencasPedido, "4") || ValueUtil.VALOR_NAO.equals(configPersonalizadaParaDiferencasPedido);
	}

	public static boolean isShowDiferencaPedidoInclusaoItem() {
		return isDominioLigado(configPersonalizadaParaDiferencasPedido, "5") || ValueUtil.VALOR_NAO.equals(configPersonalizadaParaDiferencasPedido);
	}
	
	public static boolean isShowDiferencaPedidoItemPedidoGrade() {
		return isDominioLigado(configPersonalizadaParaDiferencasPedido, "6");
	}
	
	public static boolean isShowDifPedido() {
		return isShowDiferencaPedidoStatusPedido() || isShowDiferencaPedidoObsPedido();
	}
	
	public static boolean isShowDifItemPedido() {
		return isShowDiferencaPedidoQtdItemPedido() || isShowDiferencaPedidoObsItemPedido() || isShowDiferencaPedidoInclusaoItem();
	}
	
	public static boolean liberaComSenhaClienteAtrasadoNovoPedido() {
		return nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido > 0;
	}

	public static boolean isApresentaPopUPPedidoAVista() {
		return LavenderePdaConfig.isPermitePedidoAVistaApenasClienteBloqueadoPorAtraso() && !liberaComSenhaClienteAtrasadoNovoPedido();
	}
	
	public static boolean isApresentaPopUPPedidoAVistaSenha() {
		return !ValueUtil.VALOR_NAO.equals(permitePedidoAVistaClienteBloqueadoAtrasado) && liberaComSenhaClienteAtrasadoNovoPedido();
	}
	
	
	public static boolean usaDescPromocionalRegraInterpolacaoPoliticaDesconto() {
		try {
			return usaDescPromocionalRegraPoliticaDesconto() && aplicaDescontoNoProdutoPorGrupoDescPromocional();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

	public static boolean usaDescPromocionalRegraPoliticaDesconto() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(getDescPromoRegraInterpolacaoPoliticaConfig("usa"));
	}
	
	public static boolean usaDescPromocionalRegraInterpolacaoUnidadeProduto() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(getDescPromoRegraInterpolacaoPoliticaConfig("usaUnidadeProduto"));
	}

	private static String getDescPromoRegraInterpolacaoPoliticaConfig(final String opDominio) {
		return getValorAtributoJson(configDescPromocionalRegraInterpolacaoPoliticaDesconto, opDominio, ValorParametro.USADESCPROMOCIONALREGRAINTERPOLACAOPOLITICADESCONTO);
	}

	public static boolean isCalculaPrecoPorMetroQuadradoUnidadeProduto() {
		return calculaPrecoPorMetroQuadradoUnidadeProduto && usaInfoComplementarItemPedido();
	}
	
	public static boolean isFiltrosFixoTelaListaClienteLigado() {
		return ValueUtil.isNotEmpty(filtrosFixoTelaListaCliente) && (ValueUtil.VALOR_SIM.equalsIgnoreCase(filtrosFixoTelaListaCliente) || ValueUtil.valueEquals("0", filtrosFixoTelaListaCliente));
	}
	
	public static boolean isFiltrosFixoTelaListaClienteDesligado() {
		return ValueUtil.isEmpty(filtrosFixoTelaListaCliente) || ValueUtil.VALOR_NAO.equalsIgnoreCase(filtrosFixoTelaListaCliente);
	}
	
	public static boolean isStatusClienteFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.STATUS_CLIENTE);
	}
	
	public static boolean isCaracteristicaClienteFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.CARACTERISTICA_CLIENTE);
	}
	
	public static boolean isMarcadoresFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.MARCADORES);
	}
	
	public static boolean isTipoClienteFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.TIPO_CLIENTE);
	}

	public static boolean isTipoCadastroFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.TIPO_CADASTRO);
	}
	
	public static boolean isClienteAtendidoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.CLIENTE_ATENDIDO);
	}
	
	public static boolean isClienteSemPedidoFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.CLIENTE_SEM_PEDIDO);
	}
	
	public static boolean isRepresentanteFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.REPRESENTANTE);
	}

	public static boolean isDescProgressivoPersonalizadoFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.DESC_PROGRESSIVO_PERSONALIZADO);
	}

	public static boolean isFornecedorFixoTelaListaCliente() {
		return isTipoFiltroFixoTelaListaClienteLigado(FiltroCliente.FORNECEDOR);
	}

	public static boolean isFiltrosFixoTelaListaClienteDominioConfigurado() {
		return !isStatusClienteFixoTelaListaCliente() || !isCaracteristicaClienteFixoTelaListaCliente()
			|| !isMarcadoresFixoTelaListaCliente() || !isTipoClienteFixoTelaListaCliente()
			|| !isClienteAtendidoTelaListaCliente() || !isClienteSemPedidoFixoTelaListaCliente()
			|| !isRepresentanteFixoTelaListaCliente();
	}
	
	private static boolean isTipoFiltroFixoTelaListaClienteLigado(final String cdTipoFiltro) {
		boolean isParamDesligado = isFiltrosFixoTelaListaClienteDesligado();
		return isParamDesligado || isDominioLigado(filtrosFixoTelaListaCliente, cdTipoFiltro);
	}
	
	public static boolean usaConfigFechamentoDiarioVendas() {
		return isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "usa", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}
	
	public static boolean isBloqueiaFechamentoDiarioPedidosNaoTransmitidos() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "bloqueiaFechamentoPedidoNaoTransmitido", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static boolean isLiberaFechamentoDiarioComSenha() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "liberaFechamentoDiarioComSenha", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}
	
	public static boolean usaPermiteInformarVeiculoFechamento() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "permiteInformarVeiculoFechamento", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static boolean usaObrigaInformarVeiculoFechamento() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "obrigaInformarVeiculoFechamento", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static boolean usaRelatorioEstoqueProduto() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "usaRelatorioEstoqueProduto", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static boolean usaTipoLancamentoDinamico() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "usaTipoLancamentoDinamico", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static boolean consideraValorPedidoAtualRetornado() {
		return usaConfigFechamentoDiarioVendas() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "consideraValorPedidoAtualRetornado", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static boolean usaRelatorioClienteTipoPagamento() {
		return consideraValorPedidoAtualRetornado() && isAtributoJsonLigado(usaConfigFechamentoDiarioVendas, "usaRelatorioClienteTipoPagamento", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS);
	}

	public static int nuDiasConsideraValorPedidoAtualRetornadoPagamentos() {
		if (!consideraValorPedidoAtualRetornado()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(usaConfigFechamentoDiarioVendas, "nuDiasConsideraValorPedidoAtualRetornadoPagamentos", ValorParametro.CONFIGFECHAMENTODIARIOVENDAS));
	}

	public static boolean isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() {
		return isAtributoJsonLigado(configLiberacaoComSenhaLimiteCreditoCliente, "usa", ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
	}
	
	public static boolean isUsaLiberacaoPorUsuarioEAlcada() {
		return isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && isAtributoJsonLigado(configLiberacaoComSenhaLimiteCreditoCliente, "usaLiberacaoPorUsuarioEAlcada", ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
	}
	
	public static boolean isMarcaPedidoPendenteLimiteCredito() {
		return isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && isUsaLiberacaoPorUsuarioEAlcada() && isAtributoJsonLigado(configLiberacaoComSenhaLimiteCreditoCliente, "marcaPedidoPendente", ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
	}
	
	public static boolean isUsaDtAberturaEFundacao() {
		return (nuDiaClienteAbertura > 0 || nuDiaClienteFundacao > 0);
	}

	public static boolean mostraFaixaComissao() {
		return !ValueUtil.VALOR_NAO.equalsIgnoreCase(getValorAtributoJson(configCalculoComissao, "mostraFaixaComissao", ValorParametro.CONFIGCALCULOCOMISSAO));
		}
	
	public static boolean mostraFaixaComissaoPedidoEItem() {
		return usaConfigCalculoComissao() && mostraFaixaComissao();
	}
	
	public static boolean isValorMinimoParaPedidoPorTabelaPreco() {
		return ValueUtil.valueEquals(valorMinimoParaPedidoPorTabelaPreco, "1") || ValueUtil.valueEquals(valorMinimoParaPedidoPorTabelaPreco, "S");
					}
	public static boolean isValorMinimoParaPedidoPorTabelaPrecoAgrupadoPorItem() {
		return ValueUtil.valueEquals(valorMinimoParaPedidoPorTabelaPreco, "2");
				}

	public static boolean isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() {
		return isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && isAtributoJsonLigado(configLiberacaoComSenhaLimiteCreditoCliente, "bloquearNoFechamentoDoPedido", ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
			}
	
	public static boolean isIncluiDescQtdeGrupoProdValidacaoLimiteCreditoFechamentoPedido() {
		return isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && isAtributoJsonLigado(configLiberacaoComSenhaLimiteCreditoCliente, "incluiDescQtdeGrupoProdValidacao", ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
		}

	public static boolean isAplicaIndiceCondPagtoVlBaseIPI() {
		return isAtributoJsonLigado(configIndiceFinanceiroCondPagamento, "aplicaVlBaseIPI", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGAMENTO);
	}
	
	public static boolean usaInfoComplementarItemPedido() {
		String value = getValorAtributoJson(usaInfoComplementarItemPedido, "listaCampos", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		return ValueUtil.isNotEmpty(value) && !ValueUtil.VALOR_NAO.equals(value);
	}
	
	public static boolean usaValidaPosicaoVincoLargura() {
		String value = getValorAtributoJson(usaInfoComplementarItemPedido, "validaPosicaoVincoLargura", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		return ValueUtil.isNotEmpty(value) && !ValueUtil.VALOR_NAO.equals(value);
	}
	
	public static boolean usaValorInteiroLargura() {
		String value = getValorAtributoJson(usaInfoComplementarItemPedido, "usaValorInteiroLargura", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		return ValueUtil.isNotEmpty(value) && !ValueUtil.VALOR_NAO.equals(value);
	}

	public static boolean usaValorInteiroComprimento() {
		String value = getValorAtributoJson(usaInfoComplementarItemPedido, "usaValorInteiroComprimento", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		return ValueUtil.isNotEmpty(value) && !ValueUtil.VALOR_NAO.equals(value);
	}
	
	public static List<String> getInfoComplementarItemPedidoListaCampos() {
		return getInfoComplementarItemPedido("listaCampos");
	}
	
	public static List<String> getInfoComplementarItemPedidoListaCamposObrigatorios() {
		return getInfoComplementarItemPedido("listaCamposObrigatorios");
	}
	
	public static List<String> getInfoComplementarItemPedidoListaCamposParaDescricaoItemPedido() {
		return getInfoComplementarItemPedido("listaCamposParaDescricaoItemPedido");
	}
	
	public static int getInfoComplementarItemPedidoListaNuCasasDecimais() {
		int nCasas = ValueUtil.getIntegerValue(getValorAtributoJson(usaInfoComplementarItemPedido, "nuCasasDecimais", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO));
		return nCasas > 0 ? nCasas : 3;
	}

	private static List<String> getInfoComplementarItemPedido(String listaEspecifica) {
		String campos = getValorAtributoJson(usaInfoComplementarItemPedido, listaEspecifica, ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		return (ValueUtil.isNotEmpty(campos)) ? Arrays.asList(campos.split(";")) : new ArrayList<String>();
	}
	
	private static boolean getConfigGeracaoSenhaSupervisor(String listaEspecifica) {
		String campo = getValorAtributoJson(configGeracaoSenhaSupervisor, listaEspecifica, ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO);
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(campo); 
	}
	
	public static boolean getOcultaLiberacaoClienteAtrasado() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoClienteAtrasado");
	}
	
	public static boolean getOcultaLiberacaoLimiteCreditoCliente() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoLimiteCreditoCliente");
	}
	
	public static boolean getOcultaLiberacaoPrecoVendaPedido() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoPrecoVendaPedido");
	}
	
	public static boolean getOcultaLiberacaoTipoPedido() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoTipoPedido");
	}
	
	public static boolean getOcultaLiberacaoSemEnvioDados() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoSemEnvioDados");
	}
	
	public static boolean getOcultaLiberacaoPedidoSemRecebimento() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoPedidoSemRecebimento");
	}
	
	public static boolean getOcultaLiberacaoSistemaSemRecebimento() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoSistemaSemRecebimento");
	}
	
	public static boolean getOcultaLiberacaoClientesSemPedidos() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoClientesSemPedidos");
	}
	
	public static boolean getOcultaLiberacaoClienteRedeAtrasado() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoClienteRedeAtrasado");
	}
	
	public static boolean getOcultaLiberacaoVendaProdutoBloqueado() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoVendaProdutoBloqueado");
	}
	
	public static boolean getOcultaLiberacaoSistemaGPSInativo() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoSistemaGPSInativo");
	}
	
	public static boolean getOcultaLiberacaoHoraLimiteEnvio() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoHoraLimiteEnvio");
	}
	
	public static boolean getOcultaLiberacaoPrecoVendaProdutoCliente() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoPrecoVendaProdutoCliente");
	}
	
	public static boolean getOcultaLiberacaoQtdeItemMaiorPedidoOrig() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoQtdeItemMaiorPedidoOrig");
	}
	
	public static boolean getOcultaLiberacaoDiaEntregaPedido() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoDiaEntregaPedido");
	}
	
	public static boolean getOcultaLiberacaoPedidoRentabilidadeMinima() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoPedidoRentabilidadeMinima");
	}
	
	public static boolean getOcultaLiberacaoPedidoSaldoBonifInsuficiente() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoPedidoSaldoBonifInsuficiente");
	}
	
	public static boolean getOcultaLiberacaoVisitaClienteForaAgenda() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberacaoVisitaClienteForaAgenda");
	}
	
	public static boolean getOcultaLiberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido() {
		return getConfigGeracaoSenhaSupervisor("ocultaLiberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido");
	}
	
	public static boolean isAplicaIndiceCondPagtoVlBasePIS() {
		return isAtributoJsonLigado(configIndiceFinanceiroCondPagamento, "aplicaVlBasePIS", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGAMENTO);
	}

	public static boolean isAplicaIndiceCondPagtoVlBaseCOFINS() {
		return isAtributoJsonLigado(configIndiceFinanceiroCondPagamento, "aplicaVlBaseCOFINS", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGAMENTO);
	}

	public static boolean isAplicaIndiceCondPagtoVlBaseICMS() {
		return isAtributoJsonLigado(configIndiceFinanceiroCondPagamento, "aplicaVlBaseICMS", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGAMENTO);
	}
	
	public static boolean isAplicaIndiceCondPagtoVlBaseST() {
		return isAtributoJsonLigado(configIndiceFinanceiroCondPagamento, "aplicaVlBaseST", ValorParametro.CONFIGINDICEFINANCEIROCONDPAGAMENTO);
	}
	
	public static void loadNuCasasDecimaisComissao() {
		try {
			String vlPropriedade = getValorAtributoJson(configCalculoComissao, "nuCasasDecimaisComissao", ValorParametro.CONFIGCALCULOCOMISSAO);
			if (ValueUtil.isEmpty(vlPropriedade) || ValueUtil.VALOR_NAO.equalsIgnoreCase(vlPropriedade)) {
				nuCasasDecimaisComissao = ValueUtil.doublePrecisionInterface;
				isNuCasasDecimaisComissaoLigado = false;
				return;
			}
			int valorArredondamentoInteiro = ValueUtil.getIntegerValue(vlPropriedade);
			if (valorArredondamentoInteiro < 0) {
				isNuCasasDecimaisComissaoLigado = false;
				nuCasasDecimaisComissao = ValueUtil.doublePrecisionInterface;
				return;
			} else if (valorArredondamentoInteiro > 7) {
				isNuCasasDecimaisComissaoLigado = true;
				nuCasasDecimaisComissao = 7;
				return;
			}
			isNuCasasDecimaisComissaoLigado = true;
			nuCasasDecimaisComissao = valorArredondamentoInteiro;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			isNuCasasDecimaisComissaoLigado = false;
			nuCasasDecimaisComissao = ValueUtil.doublePrecisionInterface;
		}
	}
	
	public static boolean isOcultaOrdenacaoCodigoProduto() {
		return isAtributoJsonLigado(configOrdenacaoProdutoLista, "ocultaOrdenacaoCodigoProduto", ValorParametro.CONFIGORDENACAOPRODUTOLISTA);
	}
	
	public static boolean isOcultaOrdenacaoDescricaoProduto() {
		return isAtributoJsonLigado(configOrdenacaoProdutoLista, "ocultaOrdenacaoDescricaoProduto", ValorParametro.CONFIGORDENACAOPRODUTOLISTA);
	}
	
	public static boolean isPossuiOrdenacaoListaProduto() {
		return !isOcultaOrdenacaoCodigoProduto() || !isOcultaOrdenacaoDescricaoProduto()
				|| isMostraDescricaoReferencia() || isHabilitaOrdenacaoPrecoPadrao() || isUsaOrdenacaoRankingProduto();
	}
	
	public static boolean isPossuiOrdenacaoInsercaoProdutoNoPedido() {
		return isPossuiOrdenacaoListaProduto() || usaFiltroComissao;
	}
	
	private static void loadConfigsPesquisaMercado(String jsonParam, int cdParametro) {
		usaPesquisaMercado = isAtributoJsonLigado(jsonParam, "usa", cdParametro);
		excluiPesquisaMercadoPedido = isAtributoJsonLigado(jsonParam, "excluiPesquisaMercadoPedido", cdParametro);

		String appParam = getValorAtributoJson(jsonParam, "nmAppExternoPesquisaMercadoAndroid", cdParametro);
		String[] appConf = (appParam.equals(ValueUtil.VALOR_NAO) || ValueUtil.isEmpty(appParam)) && usaPesquisaMercado ? null : getValorAtributoJson(jsonParam, "nmAppExternoPesquisaMercadoAndroid", cdParametro).split(";");
		try {
			pacoteAppExternoPesquisaMercado = ValueUtil.isEmpty(appConf) ? ValueUtil.VALOR_NAO : appConf[0];
			appExternoPesquisaMercado = ValueUtil.isEmpty(appConf) ? ValueUtil.VALOR_NAO : appConf[1];
			pacoteAppExternoPesquisaMercadoUrl = (appParam.equals(ValueUtil.VALOR_NAO) || ValueUtil.isEmpty(appParam)) && usaPesquisaMercado ? null : getValorAtributoJson(jsonParam, "nmAppExternoPesquisaMercadoUrlIos", cdParametro);
			contabilizaAcessoExternoPesquisaMercado = isAtributoJsonLigado(jsonParam, "contabilizaAcessoExterno", cdParametro);
		} catch (Throwable e) { //par�metro configurado errado.
			pacoteAppExternoPesquisaMercado = ValueUtil.VALOR_NAO;
			appExternoPesquisaMercado = ValueUtil.VALOR_NAO;
			pacoteAppExternoPesquisaMercadoUrl = ValueUtil.VALOR_NAO;
	}
	}

	public static boolean usaAppExternoPesquisaMercado() {
		return usaPesquisaMercadoProdutosConcorrentes.equals(ValueUtil.VALOR_NAO) && !appExternoPesquisaMercado.equals(ValueUtil.VALOR_NAO) && !pacoteAppExternoPesquisaMercado.equals(ValueUtil.VALOR_NAO);
	}

	public static boolean usaConfigMargemContribuicao() {
		return usaConfigMargemContribuicaoRegra1() || usaConfigMargemContribuicaoRegra2();
	}
	
	public static boolean usaTabelaConfigMinParcela() {
		return isAtributoJsonLigado(configValoresMinimos, "usaTabelaConfigMinParcela", ValorParametro.CONFIGVALORESMINIMOS);
	}

	public static double valorMinimoParcela() {
		String valorMinimoParcela = getValorAtributoJson(configValoresMinimos, "valorMinimoParcela", ValorParametro.CONFIGVALORESMINIMOS);
		return ValueUtil.getDoubleValue(valorMinimoParcela);
	}

	public static int[] configValorMinimoUnicoParaParcela() {
		String values[]  = getValorAtributoJson(configValoresMinimos, "configValorMinimoUnicoParaParcela", ValorParametro.CONFIGVALORESMINIMOS).split(";");
		int[] configValorMinimoUnicoParaParcela = new int[4];
		for (int i = 0; i < values.length; i++) {
			int value = ValueUtil.getIntegerValue(values[i]);
			if (value > 4) continue;

			configValorMinimoUnicoParaParcela[i] = value;
		}
		return configValorMinimoUnicoParaParcela;
	}

	public static boolean isConfigLiberacaoComSenhaVlMinParcela() {
		return isLiberaComSenhaVlMinParcela() && getSementeSenhaVlMinParcela() > 0;
	}
	
	public static boolean isLiberaComSenhaVlMinParcela() {
		return isAtributoJsonLigado(configValoresMinimos, "liberaComSenhaVlMinParcela", ValorParametro.CONFIGVALORESMINIMOS);
	}
	
	public static int getSementeSenhaVlMinParcela() {
		String valorConfigurado = getValorAtributoJson(configValoresMinimos, "sementeSenhaVlMinParcela", ValorParametro.CONFIGVALORESMINIMOS);
		return ValueUtil.getIntegerValue(valorConfigurado);
	}

	public static int[] configValorMinimoUnicoParaPedido() {
		String values[]  = getValorAtributoJson(configValoresMinimos, "configValorMinimoUnicoParaPedido", ValorParametro.CONFIGVALORESMINIMOS).split(";");
		int[] configValorMinimoUnicoParaPedido = new int[5];
		for (int i = 0; i < values.length; i++) {
			int value = ValueUtil.getIntegerValue(values[i]);
			if (value == 1 || value == 2 || value == 3 || value == 4 || value == 5) {
				configValorMinimoUnicoParaPedido[i] = value;
			}
		}
		return configValorMinimoUnicoParaPedido;
	}

	public static boolean isConfigValorMinimoUnicoParaParcela() {
		int[] configValorMinimoUnicoParaParcela = configValorMinimoUnicoParaParcela();
		return !(ValueUtil.valueEquals(0, configValorMinimoUnicoParaParcela[0]));
	}

	public static boolean isConfigValorMinimoUnicoParaPedido() {
		int[] configValorMinimoUnicoParaPedido = configValorMinimoUnicoParaPedido();
		return !(ValueUtil.valueEquals(0, configValorMinimoUnicoParaPedido[0]));
	}

	public static List<String> listaOrdenacaoTabelaConfigMin() {
		String listaOrdenacaoTabelaConfigMin = getValorAtributoJson(configValoresMinimos, "listaOrdenacaoTabelaConfigMin", ValorParametro.CONFIGVALORESMINIMOS);
		return Arrays.asList(listaOrdenacaoTabelaConfigMin.split(";"));
	}

	public static boolean usaTabelaConfigMinPedido() {
		return isAtributoJsonLigado(configValoresMinimos, "usaTabelaConfigMinPedido", ValorParametro.CONFIGVALORESMINIMOS);
	}


	public static boolean usaTecladoWMW() throws SQLException {
		return VmUtil.isSimulador() || isTecladoWMW() || ConfigInternoService.getInstance().usaTecladoWmw();
	}

	public static boolean isTecladoWMW() {
		return isAtributoJsonLigado(usaTecladoWMWJson, "usaTecladoWMW", ValorParametro.CONFIGTECLADO);
	}

	public static boolean usaCalculadoraWMWCampoNumerico() {
		if (VmUtil.isSimulador()) return true;
		return isAtributoJsonLigado(usaTecladoWMWJson, "usaCalculadoraWMWCampoNumerico", ValorParametro.CONFIGTECLADO);
	}

	public static void carregaParametrosTecladoAntesDeLogar() throws SQLException {
		usaTecladoVirtual = ValueUtil.VALOR_SIM.equals(getValorParametroPorSistema(ValorParametro.USATECLADOVIRTUAL));
		usaTecladoWMWJson = getValorParametroPorSistema(ValorParametro.CONFIGTECLADO);
		carregaParametrosTecladoWmw();
		configuraTecladoVirtual();
	}

	public static void carregaParametrosTecladoWmw() throws SQLException {
		LavendereConfig.getInstance().usaTecladoWMW = usaTecladoWMW();
		Settings.enableVirtualKeyboard = ! LavendereConfig.getInstance().usaTecladoWMW;
		LavendereConfig.getInstance().usaCalculadoraWMWCampoNumerico = usaCalculadoraWMWCampoNumerico();
	}

	public static boolean isAvisaVendaProdutoSemEstoque() {
		return isAvisaVendaProdutoSemEstoqueSemDetalhes() || isAvisaVendaProdutoSemEstoqueComDetalhes();
	}
	
	public static boolean isAvisaVendaProdutoSemEstoqueSemDetalhes() {
		return ValueUtil.valueEquals("1", avisaVendaProdutoSemEstoque);
	}
	
	public static boolean isAvisaVendaProdutoSemEstoqueComDetalhes() {
		return ValueUtil.valueEquals("2", avisaVendaProdutoSemEstoque);
	}
	
	public static boolean isQtdeMinimaProdutoPorCondPagamento() {
		return ValueUtil.isNotEmpty(usaQtdeMinimaProdutoPorCondPagamento) && !"N".equals(usaQtdeMinimaProdutoPorCondPagamento);
	}

	public static boolean usaQtdeMinimaProdutoPorCondPagamentoEQtProduto() {
		return ValueUtil.getIntegerValue(usaQtdeMinimaProdutoPorCondPagamento) == 1;  
	}
	
	public static boolean usaQtdeMinimaProdutoPorCondPagamentoEQtMixProduto() {
		return ValueUtil.getIntegerValue(usaQtdeMinimaProdutoPorCondPagamento) == 2;  
	}
	
	public static boolean isQtdeMinimaProdutoPorCondPagamentoFormaValidacaoPadrao() {
		return ValueUtil.isEmpty(formaValidacao) || ValueUtil.getIntegerValue(formaValidacao) == 1;
	}

	public static boolean usaValorMinimoParaPedidoPorTipoPedido() {
		return isAtributoJsonLigado(valorMinimoParaPedidoPorTipoPedido,"usa", ValorParametro.VALORMINIMOPARAPEDIDOPORTIPOPEDIDO);
	}
	
	public static boolean usaValorMinTipoPedidoPrioridade() {
		return isAtributoJsonLigado(valorMinimoParaPedidoPorTipoPedido,"usaValorMinTipoPedidoPrioridade", ValorParametro.VALORMINIMOPARAPEDIDOPORTIPOPEDIDO);
	}
	
	public static boolean isAplicaTaxaAntecipacaoNoItem() {
		return ValueUtil.valueEquals("1", aplicaTaxaAntecipacaoNoItem) || ValueUtil.valueEquals("2", aplicaTaxaAntecipacaoNoItem) ;
	}	
	
	public static boolean isAplicaTaxaAntecipacaoIntegralNoItem() {
		return ValueUtil.valueEquals("1", aplicaTaxaAntecipacaoNoItem);
	}
	
	public static boolean isAplicaTaxaAntecipacaoMensalNoItem() {
		return ValueUtil.valueEquals("2", aplicaTaxaAntecipacaoNoItem);
	}
	
	public static boolean isPermiteMultiplasTrocasBonificacoes() {
		String permiteMultiplasTrocasBonificacoes = getValorAtributoJson(relacionaPedidoNaTrocaBonificacao, "permiteMultiplasTrocasBonificacoes", ValorParametro.RELACIONAPEDIDONABONIFICACAO);
		return (isObrigaRelacionarPedidoBonificacao() || isObrigaRelacionarPedidoTroca()) && ValueUtil.getBooleanValue(permiteMultiplasTrocasBonificacoes);
	}
	
	public static int getQtdeMultiplasTrocasBonificacoes() {
		String qtdeMultiplasTrocasBonificacoes = getValorAtributoJson(relacionaPedidoNaTrocaBonificacao, "qtdeMultiplasTrocasBonificacoes", ValorParametro.RELACIONAPEDIDONABONIFICACAO);
		return ValueUtil.getIntegerValue(qtdeMultiplasTrocasBonificacoes);
	}
	
	public static int getDiasLimiteMultiplasTrocasBonificacoes() {
		String diasLimiteMultiplasTrocasBonificacoes = getValorAtributoJson(relacionaPedidoNaTrocaBonificacao, "diasLimiteMultiplasTrocasBonificacoes", ValorParametro.RELACIONAPEDIDONABONIFICACAO);
		return ValueUtil.getIntegerValue(diasLimiteMultiplasTrocasBonificacoes);
	}
	
	public static boolean isUsaAgendaDeVisitas() {
		return isAtributoJsonLigado(configAgendaDeVisitas,"usa", ValorParametro.CONFIGAGENDADEVISITAS);
	}

	public static boolean isTodosFiltrosNaTelaPrincipalAgendaDeVisitas() {
		List<String> valor = configFiltrosFixos();
		return isUsaAgendaDeVisitas() && ValueUtil.valueEquals(ValueUtil.VALOR_NAO, valor.get(0)) ;
	}

	private static boolean isTodosFiltrosNaTelaAvancadaAgendaDeVisitas() {
		List<String> valor = configFiltrosFixos();
		return isUsaAgendaDeVisitas() && (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, valor.get(0)) || ValueUtil.valueEquals(ValueUtil.VALOR_ZERO, valor.get(0))) ;
	}

	public static boolean isExibeFiltroAgendaVisitaNaTelaPrincipal(String codigoFiltro) {
		if (isTodosFiltrosNaTelaAvancadaAgendaDeVisitas()) return false;

		return isTodosFiltrosNaTelaPrincipalAgendaDeVisitas() || configFiltrosFixos().contains(codigoFiltro);
	}

	private static List<String> configFiltrosFixos() {
		String valor = getValorAtributoJson(configAgendaDeVisitas, "configFiltrosFixos", ValorParametro.CONFIGAGENDADEVISITAS);
		valor = ValueUtil.isNotEmpty(valor) ? valor : ValueUtil.VALOR_NI;
		return Arrays.asList(valor.split(";")) ;

	}

	public static boolean mostraObservacaoCliente() {
		return isAtributoJsonLigado(configObservacaoPedido, "mostraObservacaoCliente", ValorParametro.CONFIGOBSERVACAOPEDIDO);
	}	

	public static boolean isUsaLiberacaoComSenhaRestauracaoBackup() {
		return isAtributoJsonLigado(liberaComSenhaRestauracaoBackup, "usa", ValorParametro.LIBERACOMSENHARESTAURACAOBKP);
	}

	public static int getSementeSenhaLiberaComSenhaRestauracaoBackup() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(liberaComSenhaRestauracaoBackup, "sementeSenha", ValorParametro.LIBERACOMSENHARESTAURACAOBKP));
	}
	
	public static boolean usaConfigMargemContribuicaoRegra1() {
		return isDominioLigado(configMargemContribuicao, "1");
	}

	public static boolean usaConfigMargemContribuicaoRegra2() {
		return isDominioLigado(configMargemContribuicao, "2");
	}
	
	public static boolean usaCampanhaDeVendasPorCestaDeProdutos() {
		return isAtributoJsonLigado(usaCampanhaDeVendasPorCestaDeProdutos, "usa", ValorParametro.USACAMPANHADEVENDASPORCESTADEPRODUTOS);
	}
	
	public static boolean filtraSomenteCesta() {
		return isAtributoJsonLigado(usaCampanhaDeVendasPorCestaDeProdutos, "filtraSomenteCesta", ValorParametro.USACAMPANHADEVENDASPORCESTADEPRODUTOS) && usaCampanhaDeVendasPorCestaDeProdutos();
	}
	
	public static boolean isLiberaComSenhaCondPagamento() {
		return isAtributoJsonLigado(configLiberacaoComSenhaCondPagamento, "liberaComSenhaCondPagamento", ValorParametro.CONFIGLIBERACAOCOMSENHACONDPAGAMENTO);
	}

	public static int getSementeSenhaCondPagamento() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configLiberacaoComSenhaCondPagamento, "sementeSenhaCondPagamento", ValorParametro.CONFIGLIBERACAOCOMSENHACONDPAGAMENTO));
	}
	
	public static boolean isUsaCancelamentoNfePedido() {
		return ValueUtil.VALOR_SIM.equals(usaCancelamentoNfePedido) || ValueUtil.getIntegerValue(usaCancelamentoNfePedido) == 1;
	}
	
	public static boolean isUsaCancelamentoNfePedidoTransmitido() {
		return ValueUtil.getIntegerValue(usaCancelamentoNfePedido) == 2;
	}

	public static boolean usaMarcaPedidoPendenteComItemBonificado() {
		return isAtributoJsonLigado(marcaPedidoPendenteComItemBonificado, "usa", ValorParametro.MARCAPEDIDOPENDENTECOMITEMBONIFICADO);
	}

	public static boolean obrigaMotivoBonificacao() {
		return isAtributoJsonLigado(marcaPedidoPendenteComItemBonificado, "obrigaMotivoBonificacao", ValorParametro.MARCAPEDIDOPENDENTECOMITEMBONIFICADO) && usaMarcaPedidoPendenteComItemBonificado();
	}

	public static int nuMinCaracteresMotivoBonificacao() {
		if (!obrigaMotivoBonificacao()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(marcaPedidoPendenteComItemBonificado, "nuMinCaracteresMotivoBonificacao", ValorParametro.MARCAPEDIDOPENDENTECOMITEMBONIFICADO));
	}
	
	public static boolean usaCalculoFretePersonalizado() {
		return ValueUtil.getBooleanValue(getValorAtributoJson(configCalculoFretePersonalizado, "usa", ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO));
	}
	
	public static boolean exibeDetalhesCalculoFretePersonalizado() {
		return ValueUtil.getBooleanValue(getValorAtributoJson(configCalculoFretePersonalizado, "exibeDetalhesCalculo", ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO));
	}
	
	public static boolean usaFiltroTodosPadrao() {
		return ValueUtil.getBooleanValue(getValorAtributoJson(configCalculoFretePersonalizado, "filtroTodosPadrao", ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO));
	}
	
	public static boolean permitirFreteManual() {
		return ValueUtil.getBooleanValue(getValorAtributoJson(configCalculoFretePersonalizado, "permiteFreteManual", ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO));
	}
	
	public static boolean exibirInformacoesFreteCapaPedido() {
		String value = getValorAtributoJson(configCalculoFretePersonalizado, JSON_KEY_EXIBEINFOFRETECAPAPEDIDO, ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO);
		return !value.contains(ValueUtil.VALOR_NAO);
	}
	
	public static String[] informacoesParaSeremExibidasFreteCapaPedido() {
		String value = getValorAtributoJson(configCalculoFretePersonalizado, JSON_KEY_EXIBEINFOFRETECAPAPEDIDO, ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO);
		return value.split(";");
	}
	
	public static boolean exibePopupFreteInicioPedido() {
		return usaCalculoFretePersonalizado() && ValueUtil.getBooleanValue(getValorAtributoJson(configCalculoFretePersonalizado, "exibePopupInicioPedido", ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO));
	}
	
	public static boolean exibePopupFreteFechamentoPedido() {
		return usaCalculoFretePersonalizado() && ValueUtil.getBooleanValue(getValorAtributoJson(configCalculoFretePersonalizado, "exibePopupFechamentoPedido", ValorParametro.CONFIGCALCULOFRETEPERSONALIZADO));
	}

	public static boolean usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedido() {
		return isAtributoJsonLigado(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "usa", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO);
	}
	
	public static int nuDiasAdicionarHoraLimiteDataEntrega(int diaSemana, boolean pessoaFisica) {
		String vlConfiguracao = getConfigDiaSemana(diaSemana, pessoaFisica);
		if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, vlConfiguracao)) return 0;
		
		return ValueUtil.getIntegerValue(Arrays.asList(vlConfiguracao.split(";")).get(1));
	}
	
	public static String horaLimiteDeEnvioParaDataDeEntregaDoPedido(int diaSemana, boolean pessoaFisica) {
		String vlConfiguracao = getConfigDiaSemana(diaSemana, pessoaFisica);
		if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, vlConfiguracao)) return ValueUtil.VALOR_NAO;
		
		return Arrays.asList(vlConfiguracao.split(";")).get(0);
	}
	
	private static String getConfigDiaSemana(int diaSemana, boolean pessoaFisica) {
		if (!usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedido()) return ValueUtil.VALOR_NAO;
		
		boolean pf = pessoaFisica && usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica;

		String config = pf ? configHoraLimiteDeEnvioParaDataDeEntregaDoPedidoPessoaFisica : configHoraLimiteDeEnvioParaDataDeEntregaDoPedido;
		int cdParametro = pf ? ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDOPESSOAFISICA : ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO;

		switch (diaSemana) {
			case DateUtil.DATA_SEMANA_SEGUNDA:
				return getValorAtributoJson(config, "configSegunda", cdParametro);
			case DateUtil.DATA_SEMANA_TERCA:
				return getValorAtributoJson(config, "configTerca", cdParametro);
			case DateUtil.DATA_SEMANA_QUARTA:
				return getValorAtributoJson(config, "configQuarta", cdParametro);
			case DateUtil.DATA_SEMANA_QUINTA:
				return getValorAtributoJson(config, "configQuinta", cdParametro);
			case DateUtil.DATA_SEMANA_SEXTA:
				return getValorAtributoJson(config, "configSexta", cdParametro);
			case DateUtil.DATA_SEMANA_SABADO:
				return getValorAtributoJson(config, "configSabado", cdParametro);
			case DateUtil.DATA_SEMANA_DOMINGO:
				return getValorAtributoJson(config, "configDomingo", cdParametro);
			default:
				return ValueUtil.VALOR_NAO;
		}
	}
	
	public static boolean isPossuiConfiguracaoDeHoraLimite(int diaSemana) {
		if (!usaConfigHoraLimiteDeEnvioParaDataDeEntregaDoPedido()) return false;
		
		switch (diaSemana) {
			case DateUtil.DATA_SEMANA_SEGUNDA:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configSegunda", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			case DateUtil.DATA_SEMANA_TERCA:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configTerca", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			case DateUtil.DATA_SEMANA_QUARTA:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configQuarta", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			case DateUtil.DATA_SEMANA_QUINTA:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configQuinta", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			case DateUtil.DATA_SEMANA_SEXTA:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configSexta", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			case DateUtil.DATA_SEMANA_SABADO:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configSabado", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			case DateUtil.DATA_SEMANA_DOMINGO:
				return !ValueUtil.valueEquals(ValueUtil.FL_NAO, getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configDomingo", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO));
			default:
				return false;
		}
	}
	
	public static boolean isPossuiConfiguracaoSabado() {
		String vlConfiguracaoSabado = getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configSabado", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO);
		return !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, vlConfiguracaoSabado);
	}
	
	public static boolean isPossuiConfiguracaoDomingo() {
		String vlConfiguracaoDomingo = getValorAtributoJson(configHoraLimiteDeEnvioParaDataDeEntregaDoPedido, "configDomingo", ValorParametro.CONFIGHORALIMITEDEENVIOPARADATADEENTREGADOPEDIDO);
		return !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, vlConfiguracaoDomingo);
	}
	
	public static boolean isUsaMultiplosEnderecosCadastroNovoCliente() {
		return isAtributoJsonLigado(configMultiplosEnderecosCadastroNovoCliente, "usa", ValorParametro.PERMITEMULTIPLOSENDERECOSCADASTRONOVOCLIENTE);
	}
	
	public static int getQtMinEnderecosCadastroNovoCliente() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configMultiplosEnderecosCadastroNovoCliente, "qtMinEnderecos", ValorParametro.PERMITEMULTIPLOSENDERECOSCADASTRONOVOCLIENTE));
	}
	
	public static boolean isUsaControleFlagsEnderecoNovoCliente() {
		return isAtributoJsonLigado(configMultiplosEnderecosCadastroNovoCliente, "usaControleFlagsEndereco", ValorParametro.PERMITEMULTIPLOSENDERECOSCADASTRONOVOCLIENTE);
	}

	public static boolean isObrigaCnpjParaEnderecosEntregaNovoCliente() {
		return isAtributoJsonLigado(configMultiplosEnderecosCadastroNovoCliente, "obrigaCnpjParaEnderecosEntrega", ValorParametro.PERMITEMULTIPLOSENDERECOSCADASTRONOVOCLIENTE);
	}

	public static boolean exibeItensConfigTituloFinanceiro() {
		return ValueUtil.VALOR_SIM.equals(getValorAtributoJson(configTituloFinanceiro, "exibeItens", ValorParametro.CONFIGTELATITULOFINANCEIRO));
	}

	public static int nuDiasPermanenciaTituloFinanceiroPagoConfigTituloFinanceiro() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configTituloFinanceiro, "nuDiasPermanenciaTituloFinanceiroPago", ValorParametro.CONFIGTELATITULOFINANCEIRO));
	}

	public static boolean filtroPeriodoVencimentoConfigTituloFinanceiro() {
		return getValorAtributoJson(configTituloFinanceiro, "filtros", ValorParametro.CONFIGTELATITULOFINANCEIRO).contains("1");
	}

	public static boolean filtroPeriodoPagamentoConfigTituloFinanceiro() {
		return getValorAtributoJson(configTituloFinanceiro, "filtros", ValorParametro.CONFIGTELATITULOFINANCEIRO).contains("2");
	}

	public static boolean filtroTituloPagoConfigTituloFinanceiro() {
		return getValorAtributoJson(configTituloFinanceiro, "filtros", ValorParametro.CONFIGTELATITULOFINANCEIRO).contains("3");
	}

	public static boolean usaCadastroCoordenadaNaPesquisaDeMercado() {
		return isAtributoJsonLigado(configCadastroCoordenadaNaPesquisaDeMercado, "usa", ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO);
	}
	
	public static boolean usaCadastroAutomaticoCoordenadaPesquisaDeMercado() {
		return isAtributoJsonLigado(configCadastroCoordenadaNaPesquisaDeMercado, "cadastroAutomatico", ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO);
	}
	
	public static int getTimeOutCoordenadaPesquisaDeMercado() {
		int n = ValueUtil.getIntegerValue(getValorAtributoJson(configCadastroCoordenadaNaPesquisaDeMercado, "timeOut", ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO));
		return (n == 0) ? 10000 : n * 1000;
	}
	
	public static boolean obrigaCadastroCoordenadaNaPesquisaDeMercado() {
		return isAtributoJsonLigado(configCadastroCoordenadaNaPesquisaDeMercado, "obriga", ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO) && usaCadastroCoordenadaNaPesquisaDeMercado();
	}
	
	public static int getSementeSenhaCadastroCoordenadaNaPesquisaDeMercado() {
		String valorAtributo = getValorAtributoJson(configCadastroCoordenadaNaPesquisaDeMercado, "sementeSenha", ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO);
		if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, valorAtributo)) {
			return 0;
		}
		return ValueUtil.getIntegerValue(valorAtributo);
	}
	
	public static boolean usaCadastroFotoProdutoNaPesquisaDeMercado() {
		return isAtributoJsonLigado(configCadastroFotoProdutoNaPesquisaDeMercado, "usa", ValorParametro.CONFIGCADASTROFOTOPRODUTONAPESQUISADEMERCADO);
	}
	
	public static boolean obrigaCadastroFotoProdutoNaPesquisaDeMercado() {
		return usaCadastroFotoProdutoNaPesquisaDeMercado() && isAtributoJsonLigado(configCadastroFotoProdutoNaPesquisaDeMercado, "obriga", ValorParametro.CONFIGCADASTROFOTOPRODUTONAPESQUISADEMERCADO);
	}
	
	public static int getSementeSenhaCadastroFotoProdutoNaPesquisaDeMercado() {
		String valorAtributo = getValorAtributoJson(configCadastroFotoProdutoNaPesquisaDeMercado, "sementeSenha", ValorParametro.CONFIGCADASTROFOTOPRODUTONAPESQUISADEMERCADO);
		if (ValueUtil.valueEquals(valorAtributo, ValueUtil.VALOR_NAO) || !obrigaCadastroFotoProdutoNaPesquisaDeMercado()) {
			return 0;
		} else {
			return ValueUtil.getIntegerValue(valorAtributo);
		}
	}
		
	public static boolean isDirImagemDivulgaInformacaoConfigurado() {
		return !ValueUtil.VALOR_NAO.equalsIgnoreCase(dirImagemDivulgaInformacao);
	}
	
	public static int getNuMaxWidthHeightImagemDivulgaInformacao() {
		try {
			if (ValueUtil.VALOR_NAO.equalsIgnoreCase(nuMaxWidthHeightImagemDivulgaInformacao) || ValueUtil.VALOR_SIM.equalsIgnoreCase(nuMaxWidthHeightImagemDivulgaInformacao)) return 500;
			return ValueUtil.getIntegerValue(nuMaxWidthHeightImagemDivulgaInformacao);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return 500;
		}
	}
	public static boolean usaVigenciaNaVerbaPorItemTabPreco() {
		return isAtributoJsonLigado(usaVigenciaNaVerbaPorItemTabPreco, "usa", ValorParametro.USAVIGENCIANAVERBAPORITEMTABPRECO);
	}

	public static boolean permiteHistoricoSaldoAntigo() {
		return usaVigenciaNaVerbaPorItemTabPreco() && isAtributoJsonLigado(usaVigenciaNaVerbaPorItemTabPreco, "permiteHistoricoSaldoAntigo", ValorParametro.USAVIGENCIANAVERBAPORITEMTABPRECO);
	}

	public static boolean aplicaDescontoNoProdutoPorGrupoDescPromocional() {
		return isAtributoJsonLigado(aplicaDescontoNoProdutoPorGrupoDescPromocional, "usa", ValorParametro.APLICADESCONTONOPRODUTOPORGRUPODESCPROMOCIONAL);
	}

	public static boolean aplicaIndiceCondPgtoVlProdutoFinal() {
		return aplicaDescontoNoProdutoPorGrupoDescPromocional() && isAtributoJsonLigado(aplicaDescontoNoProdutoPorGrupoDescPromocional, "aplicaIndiceCondPgtoVlProdutoFinal", ValorParametro.APLICADESCONTONOPRODUTOPORGRUPODESCPROMOCIONAL);
	}
	
	public static boolean habilitaRegrasAdicionaisDescPromocional() {
		return aplicaDescontoNoProdutoPorGrupoDescPromocional() && isAtributoJsonLigado(aplicaDescontoNoProdutoPorGrupoDescPromocional, "habilitaRegrasAdicionais", ValorParametro.APLICADESCONTONOPRODUTOPORGRUPODESCPROMOCIONAL);
	}

	public static boolean permiteTabPrecoItemDiferentePedido() {
		return isAtributoJsonLigado(permiteTabPrecoItemDiferentePedido, "usa", ValorParametro.PERMITETABPRECOITEMDIFERENTEPEDIDO);
	}
	
		public static boolean usaBloqueiaCondPagtoPadraoClienteNoPedido() {
		return isAtributoJsonLigado(bloqueiaCondPagtoPadraoClienteNoPedido, "usa", ValorParametro.BLOQUEIACONDPAGTOPADRAOCLIENTENOPEDIDO);
	}
	
	public static boolean usaPermiteCondPagtoPadraoCliTipoPed() {
		return usaBloqueiaCondPagtoPadraoClienteNoPedido() && isAtributoJsonLigado(bloqueiaCondPagtoPadraoClienteNoPedido, "permiteCondPagtoPadraoCliTipoPed", ValorParametro.BLOQUEIACONDPAGTOPADRAOCLIENTENOPEDIDO);
	}
	public static boolean usaSelecaoPorGrid() {
		return permiteTabPrecoItemDiferentePedido() && isAtributoJsonLigado(permiteTabPrecoItemDiferentePedido, "usaSelecaoPorGrid", ValorParametro.PERMITETABPRECOITEMDIFERENTEPEDIDO);
	}
	
	public static boolean usaTabelaPrincipal() {
		return permiteTabPrecoItemDiferentePedido() && isAtributoJsonLigado(permiteTabPrecoItemDiferentePedido, "usaTabelaPrincipal", ValorParametro.PERMITETABPRECOITEMDIFERENTEPEDIDO);
	}
		
	public static boolean usaFiltroOrigemPedidoListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroOrigemPedido", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}
	
	public static boolean usaFiltroEmpresaListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroEmpresa", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}
	
	public static boolean usaFiltroNfeListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroNfe", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}
	
	public static boolean usaFiltroSelecaoTipoDataListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroSelecaoTipoData", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}
	
	public static boolean usaFiltroNumeroPedidoListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroNumeroPedido", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}
	
	public static boolean usaFiltroOrdemCompraClienteListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroOrdemCompraCliente", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}
	
	public static boolean usaFiltroRedeClienteListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroRedeCliente", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}

	public static boolean usaFiltroMarcadorListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroMarcador", ValorParametro.CONFIGFILTROSLISTAPEDIDOS)
				&& usaMarcadorPedido;
	}

	public static boolean usaProvisionamentoConsumoVerbaSaldo() {
		return isAtributoJsonLigado(usaProvisionamentoConsumoVerbaSaldo,"usa", ValorParametro.USAPROVISIONAMENTOCONSUMOVERBASALDO);
	}

	public static void carregaParametrosProspect(String jsonStr, int cdParametro) {
		JSONObject json = getParametroAsJson(jsonStr, cdParametro);
		usaConfigProspect = getVlParamAsInt(getValorAtributoJson(json, "usa", cdParametro));
		ignoraSeparadoresProspectCpfCnpj = ValueUtil.getBooleanValue(getValorAtributoJson(json, "ignoraSeparadoresProspectCpfCnpj", cdParametro));
		nmCampoValidaDataProspect = getValorAtributoJson(json, "nmCampoValidaData", cdParametro);
		nuAnoValidaDataProspect = getVlParamAsInt(getValorAtributoJson(json, "nuAnoValidaData", cdParametro));
		nuDiasPermanenciaRegistroProspect = getVlParamAsInt(getValorAtributoJson(json, "nuDiasPermanenciaRegistroProspect", cdParametro));
		permiteTirarFotoProspect = getVlParamAsInt(getValorAtributoJson(json, "permiteTirarFoto", cdParametro));
		permiteInserirFotoCadastradaProspect = ValueUtil.getBooleanValue(getValorAtributoJson(json, "permiteInserirFotoCadastrada", cdParametro));
		permiteCadastroCoordenada = ValueUtil.getBooleanValue(getValorAtributoJson(json, "permiteCadastroCoordenada", cdParametro));
		permiteAnexarDocumentosProspect = ValueUtil.getBooleanValue(getValorAtributoJson(json, "permiteAnexarDocumentos", cdParametro));
		permiteCarregarCamposPorCnpjViaWS = ValueUtil.getBooleanValue(getValorAtributoJson(json, "permiteCarregarCamposPorCnpjViaWS", cdParametro));
		dsUrlWsParaCarregarCamposCnpj = getValorAtributoJson(json, "dsUrlWsParaCarregarCamposCnpj", cdParametro);
	}

	public static boolean usaCadastroProspectPF() {
		return usaConfigProspect == 1 || usaConfigProspect == 3;
	}

	public static boolean usaCadastroProspectPJ() {
		return usaConfigProspect == 1 || usaConfigProspect == 2;
	}

	public static boolean permiteTirarFotoProspect() {
		return permiteTirarFotoProspect == 1 || permiteTirarFotoProspect == 2;
	}

	public static boolean obrigaTirarFotoProspect() {
		return permiteTirarFotoProspect == 2;
	}

	public static boolean usaConfigPedidoProducao() {
		return isAtributoJsonLigado(configPedidoProducao, "usa", ValorParametro.CONFIGPEDIDOPRODUCAO);
	}

	public static boolean isPermiteMultiplosRelacionamentosPedidoProducao() {
		if (!usaConfigPedidoProducao()) return false;
		return isAtributoJsonLigado(configPedidoProducao, "permiteMultiplosRelacionamentos", ValorParametro.CONFIGPEDIDOPRODUCAO);
	}

	public static int getSementeSenhaPedidoProducao() {
		if (!usaConfigPedidoProducao()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(configPedidoProducao, "sementeSenha", ValorParametro.CONFIGPEDIDOPRODUCAO));
	}

	public static boolean usaFiltroProdutosPorPlataformaVenda() {
		return isAtributoJsonLigado(configInfoComplementarPedido, "filtraProdutosPorPlataformaVenda", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO);
	}

	public static int getNuMinimoCaracteresObsModoFaturamento() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configInfoComplementarPedido, "nuMinimoCaracteresObsModoFaturamento", ValorParametro.CONFIGINFOCOMPLEMENTARPEDIDO));
	}

	public static boolean isCampoVisivelTelaItemPedido(String idCampo) {
		List<String> parametro = Arrays.asList(ordemCamposTelaItemPedido.split(";"));
		return parametro.contains(idCampo);
	}

	public static boolean isConfigValorMinimoDescPromocional() {
		return isAtributoJsonLigado(configValorMinimoDescPromocional, "usa", ValorParametro.CONFIGVALORMINIMODESCPROMOCIONAL) && aplicaDescontoNoProdutoPorGrupoDescPromocional();
	}

	public static boolean isMostraValorMinimoCapaPedido() {
		return isConfigValorMinimoDescPromocional() && isAtributoJsonLigado(configValorMinimoDescPromocional, "mostraValorMinimoCapaPedido", ValorParametro.CONFIGVALORMINIMODESCPROMOCIONAL);
	}

	public static double getVlPercentualValorMinimoPromocional() {
		try {
			return ValueUtil.getDoubleValue(getValorAtributoJson(configValorMinimoDescPromocional, "percentualValorMinimo", ValorParametro.CONFIGVALORMINIMODESCPROMOCIONAL)) / 100;
		} catch (Throwable e) {
			return 0;
		}
	}

	public static boolean mostraPontuacaoPedidoBase() {
		return apresentaBasePontuacao(mostraPontuacaoPedido);
	}

	public static boolean mostraPontuacaoPedidoRealizado() {
		return apresentaRealizadoPontuacao(mostraPontuacaoPedido);
	}

	public static boolean mostraPontuacaoListaItemBase() {
		return apresentaBasePontuacao(mostraPontuacaoListaItem);
	}

	public static boolean mostraPontuacaoListaItemRealizado() {
		return apresentaRealizadoPontuacao(mostraPontuacaoListaItem);
	}

	public static boolean mostraPontuacaoResumoDiaBase() {
		return apresentaBasePontuacao(mostraPontuacaoResumoDia);
	}

	public static boolean mostraPontuacaoResumoDiaRealizado() {
		return apresentaRealizadoPontuacao(mostraPontuacaoResumoDia);
	}

	public static boolean apresentaBasePontuacao(final String vlParam) {
		if (ValueUtil.isEmpty(vlParam) || ValueUtil.VALOR_NAO.equalsIgnoreCase(vlParam)) return false;
		return usaControlePontuacao && ("1".equalsIgnoreCase(vlParam) || "3".equalsIgnoreCase(vlParam));
	}

	public static boolean apresentaRealizadoPontuacao(final String vlParam) {
		if (ValueUtil.isEmpty(vlParam) || ValueUtil.VALOR_NAO.equalsIgnoreCase(vlParam)) return false;
		return usaControlePontuacao && ("2".equalsIgnoreCase(vlParam) || "3".equalsIgnoreCase(vlParam));
	}

	public static boolean usaCategoriaInsercaoItem() {
		return isAtributoJsonLigado(configCategoriaProduto, "usaCategoriaInsercaoItem", ValorParametro.CONFIGCATEGORIAPRODUTO);
	}

	public static boolean usaCategoriaMenuProdutos() {
		return isAtributoJsonLigado(configCategoriaProduto, "usaCategoriaMenuProdutos", ValorParametro.CONFIGCATEGORIAPRODUTO);
	}

	public static int getQtdCategoriaPadrao() {
		int qt = ValueUtil.getIntegerValue(getValorAtributoJson(configCategoriaProduto, "qtdCategoriaPadrao", ValorParametro.CONFIGCATEGORIAPRODUTO));
		return qt <= 0 ? 3 : qt;
	}

	public static int getNuTamanhoPadrao() {
		int nu = ValueUtil.getIntegerValue(getValorAtributoJson(configCategoriaProduto, "nuTamanhoPadrao", ValorParametro.CONFIGCATEGORIAPRODUTO));
		return nu <= 0 ? 5 : nu;
	}


	public static boolean usaPoliticaComercial() {
		return isAtributoJsonLigado(configPoliticaComercial, "usa", ValorParametro.CONFIGPOLITICACOMERCIAL);
	}

	public static boolean usaFamiliaPadraoPoliticaComercial() {
		return isAtributoJsonLigado(configPoliticaComercial, "usaFamiliaPadraoPoliticaComercial", ValorParametro.CONFIGPOLITICACOMERCIAL);
	}

	public static List<String> getListaOrdenacaoPoliticaComercial() {
		if (!usaPoliticaComercial()) return new ArrayList<String>();
		String campos = getValorAtributoJson(configPoliticaComercial, "listaOrdenacaoPoliticaComercial", ValorParametro.CONFIGPOLITICACOMERCIAL);
		boolean valorN = ValueUtil.VALOR_NAO.equalsIgnoreCase(campos);
		return (ValueUtil.isNotEmpty(campos) && !valorN) ? Arrays.asList(campos.split(";")) : new ArrayList<String>();
	}

	public static int getConfigUsaEnderecoEntregaPedido() {
		String param = getValorAtributoJson(configEnderecosPedido, "usaEnderecoEntrega", ValorParametro.CONFIGENDERECOSPEDIDO);
		if ("S".equals(param)) return 1;
		int intParam = ValueUtil.getIntegerValue(param);
		if ("N".equals(param) || intParam > 2) return 0;
		return intParam;
	}

	public static int getConfigUsaEnderecoCobrancaPedido() {
		String param = getValorAtributoJson(configEnderecosPedido, "usaEnderecoCobranca", ValorParametro.CONFIGENDERECOSPEDIDO);
		if ("S".equals(param)) return 1;
		int intParam = ValueUtil.getIntegerValue(param);
		if ("N".equals(param) || intParam > 2) return 0;
		return intParam;
	}

	public static String getConfigObrigatoriedadeEnderecoEntregaPedido() {
		return getValorAtributoJson(configEnderecosPedido, "obrigaEnderecoEntrega", ValorParametro.CONFIGENDERECOSPEDIDO);
	}

	public static String getConfigObrigatoriedadeEnderecoCobrancaPedido() {
		return getValorAtributoJson(configEnderecosPedido, "obrigaEnderecoCobranca", ValorParametro.CONFIGENDERECOSPEDIDO);
	}

	public static boolean isApresentaEnderecoAtualizadoEntrega() {
		return "3".equals(getValorAtributoJson(configEnderecosPedido, "apresentaEnderecoAtualizado", ValorParametro.CONFIGENDERECOSPEDIDO)) 
				|| "1".equals(getValorAtributoJson(configEnderecosPedido, "apresentaEnderecoAtualizado", ValorParametro.CONFIGENDERECOSPEDIDO)) ;
	}
	
	public static boolean isApresentaEnderecoAtualizadoCobranca() {
		return "2".equals(getValorAtributoJson(configEnderecosPedido, "apresentaEnderecoAtualizado", ValorParametro.CONFIGENDERECOSPEDIDO))
				|| "1".equals(getValorAtributoJson(configEnderecosPedido, "apresentaEnderecoAtualizado", ValorParametro.CONFIGENDERECOSPEDIDO));
	}
	
	public static boolean isApresentaEnderecoNovoCliente() {
		return "1".equals(getValorAtributoJson(configEnderecosPedido, "apresentaNovoClienteEndereco", ValorParametro.CONFIGENDERECOSPEDIDO));
	}
	
    public static boolean isSempreObrigaInclusaoEnderecoCobranca() {
    	return ValueUtil.VALOR_SIM.equals(getConfigObrigatoriedadeEnderecoCobrancaPedido()) || "1".equals(getConfigObrigatoriedadeEnderecoCobrancaPedido());
    }

    public static boolean isObrigaInclusaoEnderecoCobrancaCasoExistaRegistroDeEnderecoCobranca() {
    	return  "2".equals(getConfigObrigatoriedadeEnderecoCobrancaPedido());
    }

	public static boolean isUsaCulturaInformacoesAdicionaisItemPedido() {
		return getValorAtributoJson(usaInfoComplementarItemPedido, "listaCampos", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO).contains("cdCultura");
	}

	public static boolean isUsaPragaInformacoesAdicionaisItemPedido() {
		return getValorAtributoJson(usaInfoComplementarItemPedido, "listaCampos", ValorParametro.USAINFOCOMPLEMENTARITEMPEDIDO).contains("cdPraga");
	}

	public static int getConfigTipoPagamentoOcultoNoPedido() {
		return getVlParamAsInt(getValorAtributoJson(configTipoPagamento, "tipoPagamentoOcultoNoPedido", ValorParametro.CONFIGTIPOPAGAMENTO).substring(0, 1));
	}

	public static boolean isBloqueiaTipoPagamentoPadraoClienteNoPedido() {
		return isAtributoJsonLigado(configTipoPagamento, "bloqueiaTipoPagamentoPadraoClienteNoPedido", ValorParametro.CONFIGTIPOPAGAMENTO);
	}

	public static boolean isUsaTipoPagamentoPorTabelaPreco() {
		return isAtributoJsonLigado(configTipoPagamento, "usaTipoPagamentoPorTabelaPreco", ValorParametro.CONFIGTIPOPAGAMENTO);
	}

	public static boolean isBloqueiaTipoPagtoIgnoraLimiteCreditoPorFuncao() {
		return isAtributoJsonLigado(configTipoPagamento, "bloqueiaTipoPagtoIgnoraLimiteCreditoPorFuncao", ValorParametro.CONFIGTIPOPAGAMENTO);
	}

	public static int getQtMinimaCaracteresListaClientes() {
		String qtMinCaracteres = getValorAtributoJson(configListaClientes, "qtMinimaCaracteres", ValorParametro.CONFIGLISTACLIENTES);
		if ("0".equals(qtMinCaracteres)) return ValorParametro.PARAM_INT_VALOR_ZERO;
		return ValueUtil.getIntegerValue(qtMinCaracteres);
	}

	public static String naoRealizaFiltroAutomaticoListaClientes() {
		return getValorAtributoJson(configListaClientes, "naoRealizaFiltroAutomatico", ValorParametro.CONFIGLISTACLIENTES);
	}

	public static boolean exibeNomeFantasiaListaClientes() {
		return isAtributoJsonLigado(configListaClientes, "exibeNomeFantasia", ValorParametro.CONFIGLISTACLIENTES);
	}

	public static String usaFiltroCidadeUFBairroListaClientes() {
		return getValorAtributoJson(configListaClientes, "usaFiltroCidadeUFBairro", ValorParametro.CONFIGLISTACLIENTES);
	}

	public static boolean usaFiltroCnpjClienteListaClientes() {
		return isAtributoJsonLigado(configListaClientes, "usaFiltroCnpjCliente", ValorParametro.CONFIGLISTACLIENTES);
	}

	public static boolean usaFiltroContatoListaClientes() {
		return isAtributoJsonLigado(configListaClientes, "usaFiltroContato", ValorParametro.CONFIGLISTACLIENTES);
	}

	public static boolean usaFotoCliente() {
		return LavenderePdaConfig.permiteFotoMenuCliente || LavenderePdaConfig.permiteFotoClientePedido;
	}


	public static boolean usaLocalEstoquePorCliente() {
		return isAtributoJsonLigado(configLocalEstoque, "usaLocalEstoquePorCliente", ValorParametro.CONFIGLOCALESTOQUE);
	}

	public static boolean usaCancelamentoAutomaticoPedidoAbertoFechado() {
		return LavenderePdaConfig.usaCancPedFechadoAuto || LavenderePdaConfig.usaCancPedAbertoAuto;
	}

	public static boolean usaLocalEstoquePorTabelaPreco() {
		return isAtributoJsonLigado(configLocalEstoque, "usaLocalEstoquePorTabelaPreco", ValorParametro.CONFIGLOCALESTOQUE);
	}

	public static boolean usaLocalEstoquePorCentroCusto() {
		return isAtributoJsonLigado(configLocalEstoque, "usaLocalEstoquePorCentroCusto", ValorParametro.CONFIGLOCALESTOQUE);
	}

	public static boolean usaLocalEstoquePorTipoPedido() {
		return isAtributoJsonLigado(configLocalEstoque, "usaLocalEstoquePorTipoPedido", ValorParametro.CONFIGLOCALESTOQUE);
	}

	public static boolean isUsaLocalEstoque() {
		return usaLocalEstoquePorTabelaPreco() || usaLocalEstoquePorCliente() || usaLocalEstoquePorCentroCusto() ||	usaLocalEstoquePorTipoPedido() || usaFiltroLocalEstoqueListaProduto() || isUsaSelecaoLocalEstoqueCatalogoProduto();
	}


	public static boolean usaFiltroLocalEstoqueListaProduto() {
		return isAtributoJsonLigado(configLocalEstoque, "usaFiltroLocalEstoqueListaProduto", ValorParametro.CONFIGLOCALESTOQUE);
	}

	public static boolean isUsaSelecaoLocalEstoqueCatalogoProduto() {
		return isAtributoJsonLigado(configLocalEstoque, "usaSelecaoLocalEstoqueCatalogoProduto", ValorParametro.CONFIGLOCALESTOQUE);
	}

	public static boolean usaControleEstoquePorEstoquePrevisto() {
		return isAtributoJsonLigado(configEstoquePrevisto, "usaControleEstoquePorEstoquePrevisto", ValorParametro.CONFIGESTOQUEPREVISTO);
	}

	public static boolean exibeGridEstoquePrevisto() {
		return isAtributoJsonLigado(configEstoquePrevisto, "exibeGridEstoquePrevisto", ValorParametro.CONFIGESTOQUEPREVISTO);
	}

	public static boolean usaControleEstoquePrevistoParcial() {
		return isAtributoJsonLigado(configEstoquePrevisto, "usaControleEstoquePrevistoParcial", ValorParametro.CONFIGESTOQUEPREVISTO);
	}

	public static boolean isValidaCadastroDuasEtapas() {
		return isAtributoJsonLigado(cadastroNovoCliente, "validaCadastroDuasEtapas", ValorParametro.CADASTRONOVOCLIENTE);
	}

	public static boolean isGeraNovidadeClientePrimeiraEtapa() {
		return isAtributoJsonLigado(cadastroNovoCliente, "geraNovidadeClientePrimeiraEtapa", ValorParametro.CADASTRONOVOCLIENTE);
	}

	public static boolean usaMultiplosEnderecosCliente() {
		return isAtributoJsonLigado(configMultiplosEnderecosCliente, "usa", ValorParametro.CONFIGMULTIPLOSENDERECOSCLIENTE);
	}

	public static boolean isPermiteGerenciarEnderecosCliente() {
		String valorAtributo = getValorAtributoJson(configMultiplosEnderecosCliente, "permiteGerenciarEnderecos", ValorParametro.CONFIGMULTIPLOSENDERECOSCLIENTE);
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) || "1".equals(valorAtributo);
	}

	public static boolean usaIndicacaoTipoEnderecoCliente() {
		return isAtributoJsonLigado(configMultiplosEnderecosCliente, "usaIndicacaoTipoEndereco", ValorParametro.CONFIGMULTIPLOSENDERECOSCLIENTE);
	}

	public static int nuDiasPermanenciaAtualizacaoEnderecoCliente() {
		int nuDias = ValueUtil.getIntegerValue(getValorAtributoJson(configMultiplosEnderecosCliente, "nuDiasPermanenciaAtualizacaoEndereco", ValorParametro.CONFIGMULTIPLOSENDERECOSCLIENTE));
		return nuDias == 0 ? 1 : nuDias;
	}

	public static boolean isObrigaCnpjParaEnderecosEntregaCliente() {
		return isAtributoJsonLigado(configMultiplosEnderecosCliente, "obrigaCnpjParaEnderecosEntrega", ValorParametro.CONFIGMULTIPLOSENDERECOSCLIENTE);
	}

	public static boolean usaTipoPagamentoPorCondicaoPagamento() {
		return usaTipoPagamentoPorCondicaoPagamento.equals(ValueUtil.VALOR_SIM) || usaTipoPagamentoPorCondicaoPagamento.equals("1");
	}

	public static boolean usaCondicaoPagamentoPorTipoPagamento() {
		return usaTipoPagamentoPorCondicaoPagamento.equals(CondicaoPagamento.USA_TIPO_PAGAMENTO_ANTES_CONDICAO);
	}

	public static boolean usaTipoPagtoPorCondPagtoECondPagtoPorCliente() {
		return usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(ValueUtil.VALOR_SIM) || usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals("1");
	}

	public static boolean usaCondPagtoPorTipoPagtoECondPagtoPorCliente() {
		return usaTipoPagtoPorCondPagtoECondPagtoPorCliente.equals(CondicaoPagamento.USA_TIPO_PAGAMENTO_ANTES_CONDICAO);
	}

	public static boolean usaConfigMargemRentabilidade() {
		return containsValorAtributoJson(configMargemRentabilidade, "usa", "1", ValorParametro.CONFIGMARGEMRENTABILIDADE);
	}

	public static boolean apenasCalculaMargem() {
		return isAtributoJsonLigado(configMargemRentabilidade, "apenasCalculaMargem", ValorParametro.CONFIGMARGEMRENTABILIDADE);
	}

	public static List<String> defineRecalculoRentabilidade(){
		String value = getValorAtributoJson(configMargemRentabilidade, "defineRecalculoRentabilidade", ValorParametro.CONFIGMARGEMRENTABILIDADE);
		if (ValueUtil.isNotEmpty(value) && !ValueUtil.VALOR_NAO.equals(value)) {
			return Arrays.asList(value.split(";"));
		}
		return new ArrayList<>(0);
	}

	public static boolean usaValidacaoPercentualMargemRentab() {
		return validaPercentualMinimoMargemRentab() || validaPercentualMaximoMargemRentab() || validaPercentualMinimoMaximoMargemRentab();
	}

	public static boolean validaPercentualMinimoMargemRentab() {
		return 1 == ValueUtil.getIntegerValue(getValorAtributoJson(configMargemRentabilidade, "validaPctMargemRentabilidade", ValorParametro.CONFIGMARGEMRENTABILIDADE))
				|| validaPercentualMinimoMaximoMargemRentab();
	}

	public static boolean validaPercentualMaximoMargemRentab() {
		return 2 == ValueUtil.getIntegerValue(getValorAtributoJson(configMargemRentabilidade, "validaPctMargemRentabilidade", ValorParametro.CONFIGMARGEMRENTABILIDADE))
				|| validaPercentualMinimoMaximoMargemRentab();
	}

	public static boolean validaPercentualMinimoMaximoMargemRentab() {
		return 3 == ValueUtil.getIntegerValue(getValorAtributoJson(configMargemRentabilidade, "validaPctMargemRentabilidade", ValorParametro.CONFIGMARGEMRENTABILIDADE));
	}

	public static String getFormulaCalculoItemPedido() {
		if (!usaConfigMargemRentabilidade()) return null;
		String valorAtributo = getValorAtributoJson(configMargemRentabilidade, "formulaCalculoItemPedido", ValorParametro.CONFIGMARGEMRENTABILIDADE);
		return ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? null : valorAtributo;
	}

	public static String getFormulaCalculoPedido() {
		if (!usaConfigMargemRentabilidade()) return null;
		String valorAtributo =  getValorAtributoJson(configMargemRentabilidade, "formulaCalculoPedido", ValorParametro.CONFIGMARGEMRENTABILIDADE);
		return ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? null : valorAtributo;
	}

	public static List<String> getListaOrdenacaoMargemRentabilidade() {
		if (!usaConfigMargemRentabilidade()) return null;
		String campos = getValorAtributoJson(configMargemRentabilidade, "listaOrdenacaoMargemRentabilidade", ValorParametro.CONFIGMARGEMRENTABILIDADE);
		boolean valorN = ValueUtil.VALOR_NAO.equalsIgnoreCase(campos);
		return (ValueUtil.isNotEmpty(campos) && !valorN) ? Arrays.asList(campos.split(";")) : null;
	}

	public static boolean usaMotivoPendenciaAgrupado() {
		return isAtributoJsonLigado(configMargemRentabilidade, "usaMotivoPendenciaAgrupado", ValorParametro.CONFIGMARGEMRENTABILIDADE);
	}

	public static String getFormulaCalculoItemPedidoAgrupado() {
		if (!usaConfigMargemRentabilidade()) return null;

		String valorAtributo = getValorAtributoJson(configMargemRentabilidade, "formulaCalculoItemPedidoAgrupado", ValorParametro.CONFIGMARGEMRENTABILIDADE);
		return ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? null : valorAtributo;
	}

	public static boolean usaConfigCadastroMetasPlataformaVenda() {
		return isAtributoJsonLigado(configCadastroMetasPlataformaVenda, "usa", ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA);
	}

	public static int getDiasAlertaBloqueioMetaRepCadastroMetasPlataformaVenda() {
		if (!usaConfigCadastroMetasPlataformaVenda()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(configCadastroMetasPlataformaVenda, "diasAlertaBloqueioMetaRep", ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA));
	}

	public static int getDiasAlertaBloqueioMetaSupCadastroMetasPlataformaVenda() {
		if (!usaConfigCadastroMetasPlataformaVenda()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(configCadastroMetasPlataformaVenda, "diasAlertaBloqueioMetaSup", ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA));
	}

	public static int getDiasBloqueioMetaRepCadastroMetasPlataformaVenda() {
		if (!usaConfigCadastroMetasPlataformaVenda()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(configCadastroMetasPlataformaVenda, "diasBloqueioMetaRep", ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA));
	}

	public static int getDiasBloqueioMetaSupCadastroMetasPlataformaVenda() {
		if (!usaConfigCadastroMetasPlataformaVenda()) return 0;
		return ValueUtil.getIntegerValue(getValorAtributoJson(configCadastroMetasPlataformaVenda, "diasBloqueioMetaSup", ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA));
	}

	public static double getPercValorMinimoMetaRepCadastroMetasPlataformaVenda() {
		if (!usaConfigCadastroMetasPlataformaVenda()) return 0d;
		double percVlMinimo = ValueUtil.getDoubleValue(getValorAtributoJson(configCadastroMetasPlataformaVenda, "percValorMinimoMetaRep", ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA));
		return percVlMinimo > 100 ? 100 : percVlMinimo;
	}


	public static boolean exibeBotaoClientesNoMapa() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "exibeBotaoClientesNoMapa", ValorParametro.CONFIGAGENDADEVISITAS);
	}

	public static boolean exibeBotaoRotaParaClientes() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "exibeBotaoRotaParaClientes", ValorParametro.CONFIGAGENDADEVISITAS);
	}

	public static boolean exibeSequenciaAgenda() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "exibeSequenciaAgenda", ValorParametro.CONFIGAGENDADEVISITAS);
	}

	public static boolean consideraAgendaPendenteCargaInicial() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "consideraAgendaPendenteCargaInicial", ValorParametro.CONFIGAGENDADEVISITAS);
	}

	public static boolean usaOrdenacaoPersonalizada() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "usaOrdenacaoPersonalizada", ValorParametro.CONFIGAGENDADEVISITAS);
	}
	
	public static boolean isContabVariosPedidoRota() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "contabVariosPedidoRota", ValorParametro.CONFIGAGENDADEVISITAS);
	}
	
	public static boolean isRegistraAgendaCliHist() {
		return isAtributoJsonLigado(configAgendaDeVisitas, "registraAgendaCliHist", ValorParametro.CONFIGAGENDADEVISITAS);
	}
	
	public static boolean isUsaPesquisaCliente() {
		return isAtributoJsonLigado(configPesquisaCliente, "usa", ValorParametro.CONFIGPESQUISACLIENTE);
	}

	public static boolean isUsaPesquisaNovoCliente() {
		return isAtributoJsonLigado(configPesquisaCliente, "usaNovoCliente", ValorParametro.CONFIGPESQUISACLIENTE);
	}

	public static int nuDiasRespPesq() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configPesquisaCliente, "nuDiasRespPesq", ValorParametro.CONFIGPESQUISACLIENTE));
	}
	
	public static boolean validaPesquisasPorCliente() {
		return isAtributoJsonLigado(configPesquisaCliente, "validaPesquisasPorCliente", ValorParametro.CONFIGPESQUISACLIENTE);
	}
	
	public static boolean isObrigaRespostaRegistroChegada() {
		return isAtributoJsonLigado(configPesquisaCliente, "obrigaRespostaRegistroChegada", ValorParametro.CONFIGPESQUISACLIENTE);
	}
	
	public static boolean obrigaAprovacaoNotasDeDevolucao() {
		return isAtributoJsonLigado(configPesquisaCliente, "obrigaAprovacaoNotasDeDevolucao", ValorParametro.CONFIGPESQUISACLIENTE);
	}

	public static boolean isPermiteEmailAlternativoPedOrcamento() {
		return (permiteInserirEmailAlternativoPedEmOrcamento && usaPedidoAbertoComIndicacaoOrcamento);
	}

	public static boolean usaConfigPesquisaMercadoProdutoConcorrente() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "usa", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaIgnoraClientePesquisaMercado() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "ignoraCliente", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean permiteInserirNovoExcluirItemPesquisaMercado() {
		return usaConfigPesquisaMercadoProdutoConcorrente() && isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "permiteInserirNovoExcluirItem", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaPesquisaPedidoPesquisaMercado() {
		return usaConfigPesquisaMercadoProdutoConcorrente() && isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "usaPesquisaPedido", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaPesquisaItemPedidoPesquisaMercado() {
		return usaConfigPesquisaMercadoProdutoConcorrente() && isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "usaPesquisaItemPedido", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaInclusaoFotoPesquisaMercado() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "usaInclusaoFotoPesquisa", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaInclusaoFotoProdPesquisaMercado() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "usaInclusaoFotoProdPesquisa", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean obrigaInclusaoFotoPesquisaMercado() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "obrigaInclusaoFoto", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaCadastroCoordenadaPesquisaMercado() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "usaCadastroCoordenada", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean permiteCoordenadaAutoPesquisaMercado() {
		return usaCadastroCoordenadaPesquisaMercado() && isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "permiteCoordenadaAuto", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static int timeOutCoordenadaAutoPesquisaMercado() {
		int n = ValueUtil.getIntegerValue(getValorAtributoJson(configCadastroCoordenadaNaPesquisaDeMercado, "timeOutCoordenadaAuto", ValorParametro.CONFIGCADASTROCOORDENADANAPESQUISADEMERCADO));
		return (n <= 0) ? 10000 : n * 1000;
	}

	public static boolean excluiPesquisaPedidoPesquisaMercado() {
		return usaConfigPesquisaMercadoProdutoConcorrente() && isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "excluiPesquisaPedido", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean geraNovidadePesquisaMercado() {
		return isAtributoJsonLigado(configPesquisaMercadoProdutoConcorrente, "geraNovidadePesquisa", ValorParametro.CONFIGPESQUISAMERCADOPRODUTOCONCORRENTE);
	}

	public static boolean usaValorMinimoItemPedidoPorItemTabelaPreco() {
		return isAtributoJsonLigado(configValorMinimoItemPedidoPorItemTabelaPreco, "usa", ValorParametro.CONFIGVALORMINIMOITEMPEDIDOPORITEMTABELAPRECO);
	}

	public static boolean ignoraVlMinDescontoMaximoMenor() {
		return usaValorMinimoItemPedidoPorItemTabelaPreco() && isAtributoJsonLigado(configValorMinimoItemPedidoPorItemTabelaPreco, "ignoraVlMinDescontoMaximoMenor", ValorParametro.CONFIGVALORMINIMOITEMPEDIDOPORITEMTABELAPRECO);
	}

	public static boolean isUsaTipoFretePedido() {
		return isAtributoJsonLigado(usaTipoFretePedido, "usa", ValorParametro.USATIPOFRETEPEDIDO);
	}

	public static boolean configFreteEmbutidoDestacadoCliente() {
		return isAtributoJsonLigado(usaTipoFretePedido, "configFreteEmbutidoDestacadoCliente", ValorParametro.USATIPOFRETEPEDIDO);
	}

	public static boolean usaValidaConversaoFOB() {
		return isUsaTipoFretePedido() && isAtributoJsonLigado(usaTipoFretePedido, "validaConversaoFOB", ValorParametro.USATIPOFRETEPEDIDO);
	}

	public static String getCdStatusPedidoFOBPendenteAprovacao() {
		return getValorAtributoJson(usaTipoFretePedido, "cdStatusPedidoFOBPendenteAprovacao", ValorParametro.USATIPOFRETEPEDIDO);
	}

	public static boolean usaPedidoBonificacao() {
		return isAtributoJsonLigado(usaPedidoBonificacao, "usa", ValorParametro.USAPEDIDOBONIFICACAO);
	}

	public static boolean usaPedidoBonificacaoConsomeVerbaGrupoProduto() {
		return isAtributoJsonLigado(usaPedidoBonificacao, "consomeVerbaGrupoProduto", ValorParametro.USAPEDIDOBONIFICACAO);
	}

	public static Double usaPedidoBonificacaoPercentualLimiteBonificacao() {
		double percentualLimiteBonificacao = ValueUtil.getDoubleValue(getValorAtributoJson(usaPedidoBonificacao, "percentualLimiteBonificacao", ValorParametro.USAPEDIDOBONIFICACAO));
		return (percentualLimiteBonificacao > 0 && percentualLimiteBonificacao <= 100) ? percentualLimiteBonificacao : 0;
	}

	public static String getVlValidaPctPoliticaComercial() {
		return getValorAtributoJson(configPoliticaComercial, "validaPctPoliticaComercial", ValorParametro.CONFIGPOLITICACOMERCIAL);
	}

	public static boolean isValidaPctPoliticaComercial() {
		return isValidaPctMinPoliticaComercial() || isValidaPctMaxPoliticaComercial() || isValidaPctMinMaxPoliticaComercial();
	}

	public static boolean isValidaPctMinPoliticaComercial() {
		return usaPoliticaComercial() && ValueUtil.valueEquals(getVlValidaPctPoliticaComercial(), "1");
	}

	public static boolean isValidaPctMaxPoliticaComercial() {
		return usaPoliticaComercial() && ValueUtil.valueEquals(getVlValidaPctPoliticaComercial(), "2");
	}

	public static boolean isValidaPctMinMaxPoliticaComercial() {
		return usaPoliticaComercial() && ValueUtil.valueEquals(getVlValidaPctPoliticaComercial(), "3");
	}

	public static boolean isOcultaRentabilidadePedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "1");
	}

	public static boolean isOcultaEscalaRentabilidadeCapaPedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "2");
	}

	public static boolean isOcultaFaixaRentabilidadeCapaPedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "3");
	}

	public static boolean isOcultaRentabilidadeItemPedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "4");
	}

	public static boolean isOcultaRentabilidadeTelaFaixaRentabilidade() {
		return isDominioLigado(ocultaRentabilidadePedido, "5");
	}

	public static boolean isOcultaValorRentabilidadeSePositivaDoPedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "6");
	}

	public static boolean isOcultaValorRentabilidadeSePositivaDoItemPedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "7");
	}

	public static boolean isOcultaPercentualFaixaRentabilidadeSePositiva() {
		return isDominioLigado(ocultaRentabilidadePedido, "8");
	}

	public static boolean isOcultaPercentualMargemRentabilidadeListaPedido() {
		return isDominioLigado(ocultaRentabilidadePedido, "9");
	}

	public static boolean isOcultaValorRentabilidadeSePositivaDoPedido(final Pedido pedido) throws SQLException {
		return isOcultaValorRentabilidadeSePositivaDoPedido() && pedido.vlPctMargemRentab >= 0 && !MargemRentabService.isPedidoPendenteAprovacao(pedido);
	}

	public static boolean isOcultaValorRentabilidadeSePositivaDoItemPedido(Double VlPctRentabilidade) {
		return isOcultaValorRentabilidadeSePositivaDoItemPedido() && VlPctRentabilidade >= 0;
	}

	public static boolean isOcultaPercentualFaixaRentabilidadeSePositiva(final MargemRentabFaixa margemRentab) {
		return isOcultaPercentualFaixaRentabilidadeSePositiva() && margemRentab.vlPctMargemRentab >= 0 && (isUsaMotivosPendenciaMargemOuRentabilidade() ? margemRentab.cdMotivoPendencia == null : true);
	}

    public static boolean isDesconsideraAcrescimoDescontoTrocaTabPreco() {
    	return permiteTabPrecoItemDiferentePedido() && isAtributoJsonLigado(permiteTabPrecoItemDiferentePedido, "desconsideraAcrescimoDescontoTrocaTabelaPreco", ValorParametro.PERMITETABPRECOITEMDIFERENTEPEDIDO);
    }

	public static boolean usaCodigosProdutosParaClientes() {
		return isAtributoJsonLigado(configCodigosProdutosParaClientes, "usa", ValorParametro.CONFIGCODIGOSPRODUTOSPARACLIENTES);
	}

	public static boolean exibeFlagCdProdutoClienteNoPedido() {
		return usaCodigosProdutosParaClientes() && isAtributoJsonLigado(configCodigosProdutosParaClientes, "exibeFlagNoPedido", ValorParametro.CONFIGCODIGOSPRODUTOSPARACLIENTES);
	}

	public static boolean isUsaModuloContatoCliente() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "usa", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static boolean isHabilitaCadastroModuloContatoCliente() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "habilitaCadastro", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static boolean isHabilitaEdicaoModuloContatoCliente() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "habilitaEdicao", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static boolean isHabilitaExclusaoModuloContatoCliente() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "habilitaExclusao", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static boolean isUsaContatosNovoCliente() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "usaContatosNovoCliente", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static boolean permiteContatosNovoClienteTransmitido() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "permiteContatosNovoClienteTransmitido", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static int qtMinContatosNovoCliente() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configModuloDeContatosDoCliente, "qtMinContatosNovoCliente", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE));
	}
	
	public static boolean permiteAtualizacaoContatoNaAtualizacaoDeClientePorPeriodo() {
		return isAtributoJsonLigado(configModuloDeContatosDoCliente, "permiteAtualizacaoContatoNaAtualizacaoDeClientePorPeriodo", ValorParametro.CONFIGMODULODECONTATOSDOCLIENTE);
	}

	public static boolean isUsaGeracaoCatalogoProduto() {
		return isAtributoJsonLigado(configCatalogoProduto, "usa", ValorParametro.CONFIGCATALOGOPRODUTO);
	}

	private static void carregaParametrosMaisParceiro(String vlParametro) {
		/*1851-1*/ usaAppExternoMenuCliente = isAtributoJsonLigado(vlParametro, "usa", ValorParametro.CONFIGAPPEXTERNOMENUCLIENTE);
		String vlParam = getValorAtributoJson(vlParametro, "nmAppExternoAndroid", ValorParametro.CONFIGAPPEXTERNOMENUCLIENTE);
		String[] appConfig = vlParam.split(";");
		/*1851-2*/ nmPacoteAppExternoMenuClienteAndroid = appConfig.length > 1 ? appConfig[0] : ValueUtil.VALOR_NAO;
		/*1851-3*/ nmAppExternoMenuClienteAndroid = appConfig.length > 1 ? appConfig[1] : ValueUtil.VALOR_NAO;
		/*1851-4*/ urlAppExternoMenuClienteIOS = getValorAtributoJson(vlParametro, "urlAppExternoIOs", ValorParametro.CONFIGAPPEXTERNOMENUCLIENTE);
	}
	public static int valorTimeOutGeracaoCatalogoProduto() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configCatalogoProduto, "timeOut", ValorParametro.CONFIGCATALOGOPRODUTO));
	}

	public static boolean isUsaTabelaPrecoSomenteCatalogo() {
		return isUsaGeracaoCatalogoProduto() && isAtributoJsonLigado(configCatalogoProduto, "usaTabelaPrecoSomenteCatalogo", ValorParametro.CONFIGCATALOGOPRODUTO);
	}

	public static List<String> getListaFiltrosOpcionaisCatalogo() {
		if (!isUsaGeracaoCatalogoProduto()) return new ArrayList<String>();
		String campos = getValorAtributoJson(configCatalogoProduto, "listaFiltrosOpcionais", ValorParametro.CONFIGCATALOGOPRODUTO);
		boolean valorN = ValueUtil.VALOR_NAO.equalsIgnoreCase(campos);
		return (ValueUtil.isNotEmpty(campos) && !valorN) ? Arrays.asList(campos.split(";")) : new ArrayList<String>();
	}

	public static boolean isPulaDataEntregaEmDiasDeFeriadoProxDia() {
		return isPulaDataEntregaEmDiasDeFeriadoProxDiaExclusiveAtual() || isPulaDataEntregaEmDiasDeFeriadoProxDiaInclusiveAtual();
	}

	public static boolean isPulaDataEntregaEmDiasDeFeriadoProxDiaExclusiveAtual() {
		return "1".equals(pulaDataEntregaEmDiasDeFeriadoProxDia);
	}

	public static boolean isPulaDataEntregaEmDiasDeFeriadoProxDiaInclusiveAtual() {
		return "2".equals(pulaDataEntregaEmDiasDeFeriadoProxDia);
	}

	public static boolean isUsaMotivoPendencia() {
		return !isAtributoJsonDesligado(configMotivoPendencia, "usa", ValorParametro.CONFIGMOTIVOPENDENCIA);
	}

	private static boolean verificaSeMotivoPendenciaEstaConfigurado(String regra) {
		String vlParam = getValorAtributoJson(configMotivoPendencia, "usa", ValorParametro.CONFIGMOTIVOPENDENCIA);
		List<String> regras = Arrays.asList(vlParam.split(";"));
		return regras.contains(regra) || regras.contains(ValueUtil.VALOR_SIM);
	}

	public static boolean isUsaMotivosPendenciaPedidosBonificacao() {
		return verificaSeMotivoPendenciaEstaConfigurado("1");
	}

	public static boolean isUsaMotivosPendenciaVendaComBonificacao() {
		return verificaSeMotivoPendenciaEstaConfigurado("2");
	}

	public static boolean isUsaMotivosPendenciaPoliticaComercial() {
		return verificaSeMotivoPendenciaEstaConfigurado("3");
	}

	public static boolean isUsaMotivosPendenciaMargemOuRentabilidade() {
		return verificaSeMotivoPendenciaEstaConfigurado("4");
	}
	
	public static boolean isUsaMotivosPendenciaVerbaDeGrupoExtrapolada() {
		return verificaSeMotivoPendenciaEstaConfigurado("5");
	}
	
	public static boolean isUsaMotivosPendenciaTipoFreteDiferentePadrao() {
		return verificaSeMotivoPendenciaEstaConfigurado("7");
	}

	public static boolean isUsaMotivosPendenciaPedidosTroca() {
		return verificaSeMotivoPendenciaEstaConfigurado("6");
	}

	public static boolean isUsaJustificativaMotivoPendenciaBaseadoNoMaiorNivelLiberacao() {
		return isUsaMotivoPendencia() && ValueUtil.getIntegerValue(getValorAtributoJson(configMotivoPendencia, "modoJustificativa", ValorParametro.CONFIGMOTIVOPENDENCIA)) == 1;
	}

	public static boolean isUsaFiltroEstoqueDisponivelListaProduto() {
		return isAtributoJsonLigado(configFiltroEstoqueDisponivel, "usaListaProduto", ValorParametro.CONFIGFILTROESTOQUEDISPONIVEL);
	}

	public static boolean isUsaFiltroEstoqueDisponivelTelaItemPedido() {
		return isAtributoJsonLigado(configFiltroEstoqueDisponivel, "usaTelaItemPedido", ValorParametro.CONFIGFILTROESTOQUEDISPONIVEL);
	}

	public static boolean isUsaFiltroEstoqueDisponivel() {
		return isUsaFiltroEstoqueDisponivelListaProduto() || isUsaFiltroEstoqueDisponivelTelaItemPedido();
	}

	public static String getOpcaoDefaultFiltroEstoqueDisponivel() {
		return getValorAtributoJson(configFiltroEstoqueDisponivel, "opcaoPadrao", ValorParametro.CONFIGFILTROESTOQUEDISPONIVEL);
	}

	public static boolean isUsaOpcaoDefaultFiltroEstoqueDisponivel() {
		String opcaoDefault = getOpcaoDefaultFiltroEstoqueDisponivel();
		return opcaoDefault != null && !ValueUtil.valueEquals(opcaoDefault, "N");
	}
	public static boolean isUsaCatalogoProdutoEstoqueDisponivel() {
		return isAtributoJsonLigado(configFiltroEstoqueDisponivel, "usaCatalogoProduto", ValorParametro.CONFIGFILTROESTOQUEDISPONIVEL);
	}

	public static boolean usaConversaoTipoPedido() {
		return isAtributoJsonLigado(configConversaoTipoPedido, "usa", ValorParametro.CONFIGCONVERSAOTIPOPEDIDO);
	}

	public static int getNuDiasPermanenciaRegistroAppConversao() {
		int valor = ValueUtil.getIntegerValue(getValorAtributoJson(configConversaoTipoPedido, "nuDiasPermanenciaRegistroApp", ValorParametro.CONFIGCONVERSAOTIPOPEDIDO));
		return valor > 0 ? valor : 180;
	}

	public static boolean isPermiteInserirMultiplosItensPorVezNoPedido() {
		String valorAtributoJson = getValorAtributoJson(configInsercaoMultiplosItensPorVezNoPedido, "usa", ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
		return valorAtributoJson != null && (ValueUtil.valueEquals(valorAtributoJson, "1") || ValueUtil.valueEquals(valorAtributoJson, "2"));
	}

	public static boolean isOcultaInterfaceNegociacaoMultiplosItens() {
		return isAtributoJsonParamMultInsercaoLigado("ocultaInterfaceNegociacao");
	}

	public static boolean isPermiteAcessoTelaPadraoMultiplaInsercao() {
		return isAtributoJsonParamMultInsercaoLigado("permiteAcessoTelaPadrao");
	}

	public static boolean isExibeTotalizadoresMultiplaInsercao() {
		return isAtributoJsonParamMultInsercaoLigado("exibeTotalizadores");
	}

	public static boolean isPermiteEditarDescontoMultiplaInsercao() {
		return isAtributoJsonParamMultInsercaoLigado("permiteEditarDesconto");
	}

	public static boolean isPermiteEditarQuantidadeMultiplaInsercao() {
		return isAtributoJsonParamMultInsercaoLigado("permiteEditarQuantidade");
	}

	public static boolean isPermiteEditarDescontoOuQuantidade() {
		return isPermiteEditarQuantidadeMultiplaInsercao() || isPermiteEditarDescontoMultiplaInsercao();
	}

	public static boolean isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() {
		String vlAtributoJson = getValorAtributoJson(configInsercaoMultiplosItensPorVezNoPedido, "permiteEditarValor", ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
		return vlAtributoJson != null && ValueUtil.valueEquals(vlAtributoJson, "1") && !(aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa || usaValorMinimoItemPedidoPorItemTabelaPreco());
	}

	public static boolean isPermiteEditarValorUnitarioMultiplaInsercao() {
		String vlAtributoJson = getValorAtributoJson(configInsercaoMultiplosItensPorVezNoPedido, "permiteEditarValor", ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
		return vlAtributoJson != null && ValueUtil.valueEquals(vlAtributoJson, "2") && !(aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa || usaValorMinimoItemPedidoPorItemTabelaPreco());
	}

	public static String getListaCamposInformacoesComplementaresMultiplosItensSemNegociacao() {
		return getValorAtributoJson(configInsercaoMultiplosItensPorVezNoPedido, "listaCamposInformacoesComplementares", ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
	}

	public static boolean isPermiteSelecaoUnidadeAlternativa() {
		return isAtributoJsonParamMultInsercaoLigado("permiteSelecaoUnidadeAlternativa");
	}

	public static boolean isPermiteEditarDescontoAcrescimoMultiplaInsercao() {
		return isAtributoJsonParamMultInsercaoLigado("permiteEditarDescontoAcrescimo");
	}

	public static boolean isPermiteOcultarDescontoAcrescimoMultiplaInsercao() {
		return isPermiteOcultarValoresMultiplaInsercaoConfigurado("1") && isOcultaInterfaceNegociacaoMultiplosItens() && (isPermiteEditarDescontoAcrescimoMultiplaInsercao() || isPermiteEditarDescontoMultiplaInsercao());
	}

	public static boolean isUsaModoExibicaoCompactoMultiplaInsercao() {
		return isAtributoJsonParamMultInsercaoLigado("usaModoExibicaoCompacto") && isOcultaInterfaceNegociacaoMultiplosItens();
	}

	private static boolean isAtributoJsonParamMultInsercaoLigado(String vlParamJsonMultInsercao) {
		return isPermiteInserirMultiplosItensPorVezNoPedido() && isAtributoJsonLigado(configInsercaoMultiplosItensPorVezNoPedido, vlParamJsonMultInsercao, ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
	}
	
	public static void loadSyncWebAppEntidadeRepZero(String value) {
		syncWebAppEntidadeRepZero = null;
		if (value != null) {
			syncWebAppEntidadeRepZero = new HashSet<>(Arrays.asList(value.toLowerCase().split(";")));
		}
	}

	public static boolean isEntidadeSyncWebAppRepZero(String nmEntidade) {
		if (syncWebAppEntidadeRepZero == null || nmEntidade == null) {
			return false;
		}
		return syncWebAppEntidadeRepZero.contains(nmEntidade.toLowerCase());
	}

	public static boolean listaStatusPedidoRelComissao(Pedido pedido) {
		String[] vlPropriedade = StringUtil.split(getValorAtributoJson(configCalculoComissao, "listaStatusPedidoRelComissao", ValorParametro.CONFIGCALCULOCOMISSAO), ';');
		String aceitaTodos = vlPropriedade[0];
		if (ValueUtil.VALOR_SIM.equals(aceitaTodos)) return true;
		for (String cdStatusPedido : vlPropriedade) {
			if (cdStatusPedido.equals(pedido.cdStatusPedido)) {
				return true;
			}
		}
		return false;
	}

	public static boolean usaQtColunasSublistItemPedidoPersonalizado() {
		return qtColunasSublistItemPedido() != 2;
	}

	public static int qtColunasSublistItemPedido() {
		String qtColunasDetalhes = getValorAtributoJson(configListaItensDoPedido, "qtColunasDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
		int value = ValueUtil.getIntegerValue(qtColunasDetalhes);
		value = value < 2 ? 2 : value;
		return value > 5 ? 5 : value;
	}

	public static boolean mostraSequencialNaDescricaoItemPedido() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraSequencialNaDescricao", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean mostraPrincipioAtivoNaSublistItemPedido() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraPrincipioAtivoNosDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean mostraDescAcrescNaSublistItemPedido() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraDescAcrescNosDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean mostraPercComissaoNaSublistItemPedido() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraPercComissaoNosDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean permiteExclusaoNaListaDeItensDoPedido() {
		return isAtributoJsonLigado(configListaItensDoPedido, "permiteExclusaoItens", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean mostraTodosPercentuaisMediosNosTotalizadoresDaListaItemPedido() {
		return mostraDescAcrescNaSublistItemPedido() && mostraPercComissaoNaSublistItemPedido();
	}

	public static boolean mostraDimensoesNosDetalhes() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraDimensoesNosDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean mostraColecaoNosDetalhes() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraColecaoNosDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean mostraStatusColecaoNosDetalhes() {
		return isAtributoJsonLigado(configListaItensDoPedido, "mostraStatusColecaoNosDetalhes", ValorParametro.CONFIGLISTAITENSDOPEDIDO);
	}

	public static boolean usaLayoutImpressaoPedidoViaBluetooth1() {
		return nuLayoutImpressaoPedidoViaBluetooth == 1;
	}

	public static boolean novoClienteExibeRazaoSocial() {
		return isAtributoJsonLigado(configListaNovoCliente, "exibeRazaoSocial", ValorParametro.CONFIGLISTANOVOCLIENTE);
	}

	public static boolean novoClienteExibeNomeFantasia() {
		return isAtributoJsonLigado(configListaNovoCliente, "exibeNomeFantasia", ValorParametro.CONFIGLISTANOVOCLIENTE);
	}

	public static boolean novoClienteExibeDataCadastro() {
		return isAtributoJsonLigado(configListaNovoCliente, "exibeDataCadastro", ValorParametro.CONFIGLISTANOVOCLIENTE);
	}

	public static boolean isExibeHistoricoCliente() {
		return isAtributoJsonLigado(configHistoricoTrocaDevolucao, "exibeHistoricoCliente", ValorParametro.CONFIGHISTORICOTROCADEVOLUCAO);
	}

	public static boolean isExibeHistoricoProduto() {
		return isAtributoJsonLigado(configHistoricoTrocaDevolucao, "exibeHistoricoProduto", ValorParametro.CONFIGHISTORICOTROCADEVOLUCAO);
	}

	public static String modoTotalizacaoHistoricoCliente() {
		return getValorAtributoJson(configHistoricoTrocaDevolucao, "modoTotalizacaoHistoricoCliente", ValorParametro.CONFIGHISTORICOTROCADEVOLUCAO);
	}

	public static boolean modoTotalizacaoHistoricoClienteTipo1() {
		return modoTotalizacaoHistoricoCliente().equals("1");
	}

	public static boolean modoTotalizacaoHistoricoClienteTipo2() {
		return modoTotalizacaoHistoricoCliente().equals("2");
	}

	public static boolean modoTotalizacaoHistoricoClienteTipo3() {
		return modoTotalizacaoHistoricoCliente().equals("3");
	}

	public static boolean permiteNegociacaoItemPromocional() {
		return isAtributoJsonLigado(aplicaDescontoNoProdutoPorGrupoDescPromocional, "permiteNegociacaoItemPromocional", ValorParametro.APLICADESCONTONOPRODUTOPORGRUPODESCPROMOCIONAL) && aplicaDescontoNoProdutoPorGrupoDescPromocional();
	}

	public static boolean usaConfigBonificacaoItemPedido() {
		return isAtributoJsonLigado(configBonificacaoItemPedido, "usa", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
	}

	public static boolean isPermiteBonificarProdutoPedidoUsandoVerba() {
		return usaConfigBonificacaoItemPedido() && isAtributoJsonLigado(configBonificacaoItemPedido, "consomeVerba", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
	}

	public static boolean isPermiteBonificarQualquerProduto() {
		return usaConfigBonificacaoItemPedido() && isAtributoJsonLigado(configBonificacaoItemPedido, "permiteBonificarQualquerProduto", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
	}
	public static double getPercMaxQuantidadeBonificada() {
		return ValueUtil.getDoubleValue(getValorAtributoJson(configBonificacaoItemPedido, "percMaxQuantidadeBonificada", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO));
	}

	public static String getPercMaxValorPedidoBonificado() {
		return getValorAtributoJson(configBonificacaoItemPedido, "percMaxValorPedidoBonificado", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
	}

	public static boolean isPermiteBonificarProduto() {
		return usaConfigBonificacaoItemPedido();
	}

	public static double getPercMaxValorPedidoBonificadoDouble() {
		String percMaxValorPedidoBonificado = getPercMaxValorPedidoBonificado();
		if (ValueUtil.VALOR_NAO.equals(percMaxValorPedidoBonificado)) {
			return 0;
		}
		if (ValueUtil.VALOR_SIM.equals(percMaxValorPedidoBonificado)) {
			return 100;
		}
		double percMaxValorPedidoBonificadoDouble = Double.parseDouble(percMaxValorPedidoBonificado);
		if (percMaxValorPedidoBonificadoDouble > 100) {
			percMaxValorPedidoBonificadoDouble = 0;
		}
		return percMaxValorPedidoBonificadoDouble;
	}

	public static boolean isPermiteBonificarProdutoSemLimitesItensNoPedido() {
		return getPercMaxValorPedidoBonificadoDouble() == 0;
	}

	public static boolean isValidaPercMaxValorPedidoBonificadoNoFechamento() {
		return usaConfigBonificacaoItemPedido() && isAtributoJsonLigado(configBonificacaoItemPedido, "validaPercMaxValorPedidoBonificadoNoFechamento", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
	}

	public static boolean isPermiteBonificarApenasProdutosInseridosNoPedido() {
		return usaConfigBonificacaoItemPedido() && isAtributoJsonLigado(configBonificacaoItemPedido, "permiteBonificarApenasProdutosInseridosNoPedido", ValorParametro.CONFIGBONIFICACAOITEMPEDIDO);
	}

	public static boolean usaMarcaPedidoPendenteBaseadoLimiteCredito() {
		return isAtributoJsonLigado(marcaPedidoPendenteBaseadoLimiteCredito, "usa", ValorParametro.MARCAPEDIDOPENDENTEBASEADOLIMITECREDITO);
	}

	public static String getCdStatusLiberaRepLimCred() {
		return getValorAtributoJson(marcaPedidoPendenteBaseadoLimiteCredito, "cdStatusLiberaRepLimCred", ValorParametro.MARCAPEDIDOPENDENTEBASEADOLIMITECREDITO);
	}

	public static boolean usaMarcaPedidoPendenteAprovacaoCondPagto() {
		return isAtributoJsonLigado(marcaPedidoPendenteAprovacaoCondPagto, "usa", ValorParametro.MARCAPEDIDOPENDENTEAPROVACAOCONDPAGTO);
	}
	
	public static boolean usaMarcaPedidoPendenteAprovacaoCondPagtoDiferentePadrao() {
		return usaMarcaPedidoPendenteAprovacaoCondPagto() && !usaMarcaPedidoPendenteAprovacaoQtdDiasCondPagto();
	}
	
	public static boolean usaMarcaPedidoPendenteAprovacaoQtdDiasCondPagto() {
		return usaMarcaPedidoPendenteAprovacaoCondPagto() && isAtributoJsonLigado(marcaPedidoPendenteAprovacaoCondPagto, "marcaPedidoPendentePorQtDiasCondPagto", ValorParametro.MARCAPEDIDOPENDENTEAPROVACAOCONDPAGTO);
	}

	public static String getCdStatusPendenteCondPagto() {
		return getValorAtributoJson(marcaPedidoPendenteAprovacaoCondPagto, "cdStatusPendenteCondPagto", ValorParametro.MARCAPEDIDOPENDENTEAPROVACAOCONDPAGTO);
	}

	public static String getCdStatusLiberaRepCondPagto() {
		return getValorAtributoJson(marcaPedidoPendenteAprovacaoCondPagto, "cdStatusLiberaRepCondPagto", ValorParametro.MARCAPEDIDOPENDENTEAPROVACAOCONDPAGTO);
	}

	public static boolean usaLiberacaoProprioRep() {
		return !ValueUtil.valueEquals(getCdStatusLiberaRepCondPagto(), ValueUtil.VALOR_NAO) || !ValueUtil.valueEquals(getCdStatusLiberaRepLimCred(), ValueUtil.VALOR_NAO);
	}

	public static boolean usaPedidoPendenteRetornoRepLimCred() {
		return !ValueUtil.valueEquals(getCdStatusLiberaRepLimCred(), ValueUtil.VALOR_NAO);
	}

	public static boolean isUsaLaudoDap() {
		return isAtributoJsonLigado(configLaudoDap, "usa", ValorParametro.CONFIGLAUDODAP);
	}

	public static boolean usaAgrupadorSimilaridadeComSolicitacaoAutorizacao() {
		return isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto;
	}

	public static boolean isUsaGrupoDestaqueProduto() {
		return isAtributoJsonLigado(configGrupoDestaqueProduto, "usaListaProduto", ValorParametro.DESTACAPRODUTOGRUPODESTAQUELISTA);
	}
	public static boolean isUsaGrupoDestaqueProdutoCatalogo() {
		return isAtributoJsonLigado(configGrupoDestaqueProduto, "usaCatalogoProduto", ValorParametro.DESTACAPRODUTOGRUPODESTAQUELISTA);
	}

	public static boolean usaPedidoViaCampanhaPublicitaria() {
		return isAtributoJsonLigado(pedidoViaCampanhaPublicitaria, "usa", ValorParametro.PEDIDOVIACAMPANHAPUBLICITARIA);
	}

	public static boolean isObrigaVincularCampanhaPublicitariaPedido() {
		return usaPedidoViaCampanhaPublicitaria() && isAtributoJsonLigado(pedidoViaCampanhaPublicitaria, "obrigaVincularCampanhaPublicitaria", ValorParametro.PEDIDOVIACAMPANHAPUBLICITARIA);
	}

	public static boolean isPermiteUsuarioNormalEnviarRecado() {
		return isAtributoJsonLigado(configModuloRecado, "permiteUsuarioNormalEnviarRecado", ValorParametro.CONFIGMODULORECADO);
	}

	public static boolean isUsaRecadoPorFuncionalidade() {
		String vlParam = getValorAtributoJson(configModuloRecado, "permiteRecadoPorFuncionalidade", ValorParametro.CONFIGMODULORECADO);
		List<String> configParam = Arrays.asList(vlParam.split(";"));
		return configParam.contains("1")||configParam.contains("2");
	}

	public static boolean isPermiteAplicarFiltros() {
		String vlParam = getValorAtributoJson(configModuloRecado, "permiteAplicarFiltros", ValorParametro.CONFIGMODULORECADO);
		return vlParam.equals("1");
	}

	public static boolean usaFaceamento() {
		return isAtributoJsonLigado(configFaceamento, "usa", ValorParametro.CONFIGFACEAMENTO);
	}

	public static boolean usaEdicaoSugestaoVenda() {
		return isAtributoJsonLigado(configFaceamento, "editarSugestaoVenda", ValorParametro.CONFIGFACEAMENTO);
	}

	public static boolean isUsaFlIgnoraValidaco() {
		return isAvisaVendaProdutoSemEstoque() || usaGrifaProdutoSemEstoqueNaLista() || isUsaReservaEstoqueCentralizado() || isUsaReservaEstoqueCentralizadoAtomico() ||
				mostraColunaEstoqueNaListaProdutoForaPedido() || mostraColunaEstoqueNaListaProdutoDentroPedido() || mostraColunaEstoqueNaListaItensInseridosNoPedido();
	}

	public static boolean usaGrifaProdutoSemEstoqueNaLista() {
		return usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() || usaGrifaProdutoSemEstoqueNaListaItensAdicionados();
	}

	public static boolean usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() {
		String[] vlParametro = grifaProdutoSemEstoqueNaLista.split(";");
		for (String valor: vlParametro) {
			if (ValueUtil.VALOR_SIM.equals(valor) || "1".equals(valor)) {
				return true;
			}
		}
		return false;
	}

	public static boolean usaGrifaProdutoSemEstoqueNaListaItensAdicionados() {
		return isDominioLigado(grifaProdutoSemEstoqueNaLista, "2");
	}


	public static boolean usaFiltraProdutoCodigoExato() {
		return isAtributoJsonLigado(configFiltroProdutoCodigo, "filtraProdutoCodigoExato", ValorParametro.CONFIGFILTROPRODUTOCODIGO);
	}

	public static boolean isExibeBotaoParaFiltroCodigo() {
		String vlParam = getValorAtributoJson(configFiltroProdutoCodigo, "exibeBotaoParaFiltroCodigo", ValorParametro.CONFIGFILTROPRODUTOCODIGO);
		return vlParam.equals("1")||vlParam.equals("2");
	}

	public static boolean isExibeBotaoParaFiltroCodigoTecladoAlfaNumerico() {
		String vlParam = getValorAtributoJson(configFiltroProdutoCodigo, "exibeBotaoParaFiltroCodigo", ValorParametro.CONFIGFILTROPRODUTOCODIGO);
		return vlParam.equals("1");
	}

	public static boolean isExibeBotaoParaFiltroCodigoTecladoNumerico() {
		String vlParam = getValorAtributoJson(configFiltroProdutoCodigo, "exibeBotaoParaFiltroCodigo", ValorParametro.CONFIGFILTROPRODUTOCODIGO);
		return vlParam.equals("2");
	}

	public static boolean isSelecionaProdutoAutomaticamente() {
		String vlParam = getValorAtributoJson(configFiltroListaProdutos, "selecionaProdutoAutomaticamente", ValorParametro.CONFIGFILTROLISTAPRODUTOS);
		return vlParam.equals("S");
	}

	public static boolean isMostraOrdenacaoRelevanciaProduto() {
		return isAtributoJsonLigado(configOrdenacaoProdutoLista, "mostraOrdenacaoRelevanciaProduto", ValorParametro.CONFIGORDENACAOPRODUTOLISTA);
	}
	
	public static boolean isMostraOrdenacaoEstoqueProduto() {
		return isAtributoJsonLigado(configOrdenacaoProdutoLista, "mostraOrdenacaoEstoqueProduto", ValorParametro.CONFIGORDENACAOPRODUTOLISTA);
	}

	public static boolean isCadastroDescontoPromocionalDesabilitado() {
		return ValueUtil.valueEquals(configCadastroDescontoPromocional, ValueUtil.VALOR_NAO);
	}

	public static boolean isMostraConfigTelaInfoProduto() {
		String vlParam = getValorAtributoJson(configTelaInfoProduto, "usaItemPedido", ValorParametro.CONFIGTELAINFOPRODUTO);
		return !vlParam.equals("N");
	}	
	
	public static String getConfigTelaInfoProdutoValue() {
		return  getValorAtributoJson(configTelaInfoProduto, "usaItemPedido", ValorParametro.CONFIGTELAINFOPRODUTO);
	}

	public static boolean isBloqueiaClienteSemAlvaraProdutoControlado() {
		return isAtributoJsonLigado(configProdutoControlado, "bloqueiaClienteSemAlvara", ValorParametro.BLOQUEIAPRODUTOCONTROLADOCLIENTESEMALVARA);
	}

	public static boolean isBloqueiaClienteSemLicencaProdutoControlado() {
		return isAtributoJsonLigado(configProdutoControlado, "bloqueiaClienteSemLicenca", ValorParametro.BLOQUEIAPRODUTOCONTROLADOCLIENTESEMALVARA);
	}
	public static boolean exibeQualquerAlteracaoDescontoProgressivo() {
		return exibeAlteracaoDescProgressivo == 3;
	}

	public static boolean exibeAlteracaoGanhoDescontoProgressivo() {
		return exibeQualquerAlteracaoDescontoProgressivo() || exibeAlteracaoDescProgressivo == 2;
	}

	public static boolean exibeAlteracaoPercaDescontoProgressivo() {
		return exibeQualquerAlteracaoDescontoProgressivo() || exibeAlteracaoDescProgressivo == 1;
	}

	public static boolean isUsaSolicitacaoAutorizacao() {
		return !isAtributoJsonDesligado(configSolicitaAutorizacao, "usa", ValorParametro.CONFIGSOLICITAAUTORIZACAO);
	}

	private static boolean verificaRegraDeSolicitacaoAutorizacaoConfigurada(String regra) {
		String vlParam = getValorAtributoJson(configSolicitaAutorizacao, "usa", ValorParametro.CONFIGSOLICITAAUTORIZACAO);
		List<String> regras = Arrays.asList(vlParam.split(","));
		return regras.contains(regra);
	}

	public static boolean usaSolicitacaoAutorizacaoPorNegociacaoDePreco() {
		return verificaRegraDeSolicitacaoAutorizacaoConfigurada("1");
	}

	public static boolean usaSolicitacaoAutorizacaoPorBonificacao() {
		return verificaRegraDeSolicitacaoAutorizacaoConfigurada("2");
	}

	public static boolean usaSolicitacaoAutorizacaoPorVendaCritica() {
		return verificaRegraDeSolicitacaoAutorizacaoConfigurada("3");
	}

	public static boolean usaSolicitacaoAutorizacaoPorParcelaMinMax() {
		return verificaRegraDeSolicitacaoAutorizacaoConfigurada("4");
	}

	public static boolean isIgnoraValidacoesPedidoOrcamento() {
		return isUsaSolicitacaoAutorizacao() && isAtributoJsonLigado(configSolicitaAutorizacao, "ignoraValidacoesPedidoOrcamento", ValorParametro.CONFIGSOLICITAAUTORIZACAO);
	}

	public static boolean isIgnoraValidacoesVlMinItemPedPorItemTabPreco() {
		return isUsaSolicitacaoAutorizacao() && isAtributoJsonLigado(configSolicitaAutorizacao, "ignoraValidacoesVlMinItemPedPorItemTabPreco", ValorParametro.CONFIGSOLICITAAUTORIZACAO);
	}

	public static boolean isIgnoraAgrupadorSimilaridade() {
		return isUsaSolicitacaoAutorizacao() && isAtributoJsonLigado(configSolicitaAutorizacao, "ignoraAgrupadorSimilaridade", ValorParametro.CONFIGSOLICITAAUTORIZACAO);
	}

	public static boolean isObrigaAnexoDocCondicaoPagamento() {
		return usaSelecaoDocAnexoPedido() && isAtributoJsonLigado(configDocumentosAnexo, "obrigaPorCondicaoPagamentoNoPedido", ValorParametro.CONFIGDOCUMENTOSANEXO);
	}

	private static int populeLimiteMegabytes() {
		if (ValueUtil.isNotEmpty(usaSelecaoDocumentosAnexo) && !ValueUtil.VALOR_NAO.equals(usaSelecaoDocumentosAnexo)) {
			try {
				return Integer.parseInt(getValorAtributoJson(configDocumentosAnexo, "limiteMegabytes", ValorParametro.CONFIGDOCUMENTOSANEXO));
			} catch (Exception e) {
				return 10;
			}
		}
		return 0;
	}

	public static boolean isUsaReservaEstoqueCorrente() {
		return isAtributoJsonLigado(configReservaEstoqueCorrente, "usa", ValorParametro.CONFIGRESERVAESTOQUECORRENTE);
	}
	
	public static boolean usaReservaEstoqueCorrenteR3() {
		return "2".equals(getValorAtributoJson(configReservaEstoqueCorrente, "usa", ValorParametro.CONFIGRESERVAESTOQUECORRENTE));
	}
	
	public static boolean isObrigaAnexoDocClienteLimiteCredExtrapolado() {
		return usaSelecaoDocAnexoPedido() && isAtributoJsonLigado(configDocumentosAnexo, "obrigaPorLimiteCreditoExtrapoladoNoPedido", ValorParametro.CONFIGDOCUMENTOSANEXO);
	}

	public static boolean usaFiltroStatusExcecaoListaPedidos() {
		return isAtributoJsonLigado(configFiltrosListaPedidos, "usaFiltroStatusExcecao", ValorParametro.CONFIGFILTROSLISTAPEDIDOS);
	}

	public static boolean usaFiltroStatusExcecaoResumoDia() {
		return isAtributoJsonLigado(configModoResumoDiario, "usaFiltroStatusExcecao", ValorParametro.CONFIGMODORESUMODIARIO);
	}

	public static boolean isUsaGeracaoPdfOnline() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "usaGeracaoPdfOnline", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isUsaGeracaoPdfOffline() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "usaGeracaoPdfOffline", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isSugereGeracaoPdfNoFechamento() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "sugereGeracaoPdfNoFechamento", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isGeraPdfOfflineAuto() {
		return isUsaGeracaoPdfOffline() && isAtributoJsonLigado(configGeracaoPdfPedido, "geraPdfOfflineAuto", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isGeraPdfOnlinePorPadrao() {
		return isUsaGeracaoPdfOnline() && isAtributoJsonLigado(configGeracaoPdfPedido, "geraPdfOnlinePorPadrao", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isAbrePdfGeradoAuto() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "abrePdfGeradoAuto", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}


	public static boolean isGeraRelPedidoItensViaClientePdf() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "geraRelPedidoItensViaClientePdf", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isIgnoraVlMaximoDesconto() {
		return usaValorMinimoItemPedidoPorItemTabelaPreco() && isAtributoJsonLigado(configValorMinimoItemPedidoPorItemTabelaPreco, "ignoraVlMaximoDesconto", ValorParametro.CONFIGVALORMINIMOITEMPEDIDOPORITEMTABELAPRECO);
	}


	public static boolean isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDesconto() {
		return "3".equals(usaMultiplasLiberacoesDescontoNoPedido) || isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDescontoPonderada();
	}

	public static boolean isUsaMultiplasLiberacoesRespeitandoHierarquiaPercentualDescontoPonderada() {
		return "4".equals(usaMultiplasLiberacoesDescontoNoPedido);
	}

	public static boolean isUsaPoliticaBonificacao() {
		return isAtributoJsonLigado(configPoliticaBonificacao, "usa", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaPoliticaBonificacaoRepresentante() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaRepresentante", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaPoliticaBonificacaoFamilia() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaFamiliasProdutos", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaPoliticaBonificacaoLinha() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaLinhasProdutos", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaPoliticaBonificacaoCategoria() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaCategoriasClientes", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaConsumoVerbaSupervisor() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaConsumoVerbaSupervisor", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaPoliticaBonificacaoProduto() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaProdutos", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}
	
	public static boolean isUsaPoliticaBonificacaoClientes() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaClientes", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}
	
	public static boolean isUsaRegraValorVenda() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaRegraValorVenda", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}
	
	public static boolean isUsaRegraFaixaQuantidade() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaRegraFaixaQuantidade", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}
	
	public static boolean isUsaRegraContaCorrenteQuantidade() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaRegraContaCorrenteQuantidade", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}
	
	public static boolean permiteFiltroCestaEmConjuntoOutrosFiltros() {
		return isAtributoJsonLigado(usaCampanhaDeVendasPorCestaDeProdutos, "permiteFiltroCestaEmConjuntoOutrosFiltros", ValorParametro.USACAMPANHADEVENDASPORCESTADEPRODUTOS);
	}

	public static boolean isMostraVlComTributosNasListasDePedidoEItens() {
		return isAtributoJsonLigado(mostraVlComTributosNasListasDePedidoEItens, "usa", ValorParametro.CONFIGMOSTRAVLTRIBUTOSNASLISTASDEPEDIDOEITENS);
	}

	public static boolean isMostraPrecoComSTNaListaDeItensPedido() {
		return isAtributoJsonLigado(mostraVlComTributosNasListasDePedidoEItens, "apresentaPrecoComST", ValorParametro.CONFIGMOSTRAVLTRIBUTOSNASLISTASDEPEDIDOEITENS);
	}

	public static boolean isUsaPoliticaBonificacaoComBrindes() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaBrindes", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaCalculaStItemPedido() {
		return isAtributoJsonLigado(configCalculaStItemPedido, "usa", ValorParametro.CONFIGCALCULASTITEMPEDIDO);
	}

	public static boolean isPMPFSobreVlBaseRetido() {
		return isUsaCalculaStItemPedido() && isAtributoJsonLigado(configCalculaStItemPedido, "PMPFSobrevlBaseRetido", ValorParametro.CONFIGCALCULASTITEMPEDIDO);
	}

	public static boolean isUsaIndiceFinaceiroPorClienteOrCondicaoPagamento() {
		return isIndiceFinanceiroClienteVlItemPedido() || isIndiceFinanceiroCondPagtoVlItemPedido();
	}

	public static boolean isAplicaDeflatorCondPagtoValorItensFormulaJuros() {
		return "4".equals(aplicaDeflatorCondPagtoValorTotalPedido);
	}

	public static boolean isGeraPdfPedidoBoleto() {
		return isAtributoJsonLigado(gerarPdfBoletoNotaFiscal, "geraPdfBoleto", ValorParametro.GERARPDFBOLETONOTAFISCAL);
	}

	public static boolean isUsaPoliticaBonificacaoConjuntos() {
		return isUsaPoliticaBonificacao() && isAtributoJsonLigado(configPoliticaBonificacao, "usaConjuntos", ValorParametro.CONFIGPOLITICABONIFICACAO);
	}

	public static boolean isUsaImpressaoNfePDF() {
		return isAtributoJsonLigado(gerarPdfBoletoNotaFiscal, "geraPdfNotaFiscal", ValorParametro.GERARPDFBOLETONOTAFISCAL);
	}

	public static boolean isGeraCatalogoItensPedidoOnline() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "usaGeracaoCatalogoItensPedidoOnline", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isGeraHistoricoCatalogoItensPedidoOnline() {
		return isAtributoJsonLigado(configGeracaoPdfPedido, "geraHistoricoGeracaoCatalogoItensPedidoOnline", ValorParametro.CONFIGGERACAOPDFPEDIDO);
	}

	public static boolean isUsaFotoProdutoGrade() {
		return isAtributoJsonLigado(configFotoProdutoGrade, "usa", ValorParametro.CONFIGFOTOPRODUTOGRADE);
	}

	public static boolean isUsaListaProdutoAgrupadorGrade() {
		return isAtributoJsonLigado(configListaProdutoAgrupadorGrade, "usa", ValorParametro.CONFIGLISTAPRODUTOAGRUPADORGRADE);
	}

	public static boolean isPermiteOcultarValoresItemAgrupadorGrade() {
		return isUsaListaProdutoAgrupadorGrade() && !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, permiteOcultarValoresItemAgrupadorGrade);
	}
	public static boolean isPermiteOcultarValoresEstoqueItemAgrupadorGrade() {
		return isPermiteOcultarValoresAgrupadorGradeConfigurado("1");
	}

	public static boolean isPermiteOcultarValoresTotaisItemAgrupadorGrade() {
		return isPermiteOcultarValoresAgrupadorGradeConfigurado("2");
	}

	private static boolean isPermiteOcultarValoresAgrupadorGradeConfigurado(String expectedValue) {
		if (!isUsaListaProdutoAgrupadorGrade()) {
			return false;
		}
		String[] valuesJson = StringUtil.split(permiteOcultarValoresItemAgrupadorGrade, ';');
		for (int i = 0; i < valuesJson.length; i++) {
			if (ValueUtil.valueEquals(expectedValue, valuesJson[i])) {
				return true;
			}
		}
		return false;
	}

	private static boolean isPermiteOcultarValoresMultiplaInsercaoConfigurado(String expectedValue) {
		if (!isPermiteInserirMultiplosItensPorVezNoPedido()) {
			return false;
		}
		String atributoJson = getValorAtributoJson(configInsercaoMultiplosItensPorVezNoPedido, "permiteOcultarValoresItem", ValorParametro.CONFIGINSERCAOMULTIPLOSITENSPORVEZNOPEDIDO);
		String[] valuesJson = StringUtil.split(StringUtil.getStringValue(atributoJson), ';');
		for (int i = 0; i < valuesJson.length; i++) {
			if (ValueUtil.valueEquals(expectedValue, valuesJson[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPermiteOcultarValoresEstoqueItemMultiplaInsercao() {
		return isPermiteOcultarValoresMultiplaInsercaoConfigurado("2");
	}

	public static boolean isPermiteOcultarValoresTotaisItemMultiplaInsercao() {
		return isPermiteOcultarValoresMultiplaInsercaoConfigurado("3");
	}

	public static boolean isPermiteOcultarValoresItemMultiplaInsercao() {
		return isPermiteOcultarValoresEstoqueItemMultiplaInsercao()
				|| isPermiteOcultarValoresTotaisItemMultiplaInsercao()
				|| isPermiteOcultarDescontoAcrescimoMultiplaInsercao();
	}

	public static boolean isUsaCarrosselImagemProdutosComAgrupadorGrade() {
		return usaCarrosselProdutosGradeDetalheItem && isUsaListaProdutoAgrupadorGrade();
	}

	public static String getVlParamUsaRelCatalogoPedido() {
		return getValorAtributoJson(configCatalogoProduto, "usaRelCatalogoPedido", ValorParametro.CONFIGCATALOGOPRODUTO);
	}
	
	public static String getVlPrecoItemSt() {
		return getValorAtributoJson(mostraPrecoItemSt, "mostraPrecoItemSt", ValorParametro.CONFIGCALCULODETRIBUTACAOPERSONALIZADO);
	}
	
	public static boolean isMostraPrecoItemStNaListaProduto() {
		return getVlPrecoItemSt().contains("1");
	}

	public static boolean isMostraPrecoItemStNaListaProdutoDoPedido() {
		return getVlPrecoItemSt().contains("2");
	}

	public static boolean isMostraPrecoItemStNoDetalheDoProduto() {
		return getVlPrecoItemSt().contains("3");
	}

	public static boolean isUsaRelCatalogoCapaPedido() {
		return getVlParamUsaRelCatalogoPedido().contains("1");
	}

	public static boolean isUsaRelCatalogoCapaItemPedido() {
		return getVlParamUsaRelCatalogoPedido().contains("2");
	}

	public static boolean isUsaRelCatalogoListaItemPedido() {
		return getVlParamUsaRelCatalogoPedido().contains("3");
	}

	public static boolean isUsaArquivoCatalogoExternoCapaPedido() {
		return usaCatalogoExterno != null && (usaCatalogoExterno.contains("1") || usaCatalogoExterno.contains("S"));
	}

	public static boolean isUsaArquivoCatalogoExternoListaProdutos() {
		return usaCatalogoExterno != null && (usaCatalogoExterno.contains("2") || usaCatalogoExterno.contains("S"));
	}

	public static boolean isUsaBloqueiaProdutoBloqueadoNoPedido() {
		return isAtributoJsonLigado(bloqueiaProdutoBloqueadoNoPedido, "usa", ValorParametro.BLOQUEIAPRODUTOBLOQUEADONOPEDIDO);
	}
	
	public static boolean isUsaDestacaProdutoBloqueado() {
		return isUsaBloqueiaProdutoBloqueadoNoPedido() && isAtributoJsonLigado(bloqueiaProdutoBloqueadoNoPedido, "destacaProdutoBloqueado", ValorParametro.BLOQUEIAPRODUTOBLOQUEADONOPEDIDO);
	}
	
	public static boolean isUsaSugestaoVendaBaseadaEmComboProdutos() {
		return !isAtributoJsonDesligado(configSugestaoVendaBaseadaEmComboProdutos, "usa", ValorParametro.CONFIGSUGESTAOVENDABASEADAEMCOMBOPRODUTOS);
	}
	
	private static boolean verificaRegraDeSugestaoVendaBaseadaEmComboProdutos(String regra) {
		return verificaRegraDeSugestaoVendaBaseadaEmComboProdutos(regra, false);
	}
	
	private static boolean verificaRegraDeSugestaoVendaBaseadaEmComboProdutos(String regra, boolean isIndividual) {
		String vlParam = getValorAtributoJson(configSugestaoVendaBaseadaEmComboProdutos, "usa", ValorParametro.CONFIGSUGESTAOVENDABASEADAEMCOMBOPRODUTOS);
		List<String> regras = Arrays.asList(vlParam.split(","));
		return regras.contains(regra) || (!isIndividual && regras.contains(ValueUtil.VALOR_SIM));
	}
	public static boolean usaKitBonificadoEPoliticaBonificacao() {
		return permiteIncluirMesmoProdutoNoPedido && isPermiteItemBonificado() && isUsaPoliticaBonificacao();
	}

	public static boolean usaMotivoTrocaPorItemPedido() {
		return isAtributoJsonLigado(configMotivoTrocaPorItem, "usa", ValorParametro.CONFIGMOTIVOTROCAPORITEM);
	}

	public static boolean isUsaConfigMenuCatalogo() {
		return isAtributoJsonLigado(configMenuCatalogo, "usa", ValorParametro.CONFIGMENUCATALOGO);
	}

	public static int getQuantidadeItensPorLinhaMenuCatalogo() {
		int number;
		try {
			number = Integer.parseInt(getValorAtributoJson(configMenuCatalogo, "quantidadeItensPorLinha", ValorParametro.CONFIGMENUCATALOGO));
		} catch (Exception e) {
			return 2;
		}
		return number;
	}

	public static boolean isApresentaTextoPadraoMenuCatalogo() {
		return isUsaConfigMenuCatalogo() && isAtributoJsonLigado(configMenuCatalogo, "apresentaTextoPadrao", ValorParametro.CONFIGMENUCATALOGO);
	}

	public static boolean isUsaConfigVideosProdutos() {
		return isAtributoJsonLigado(configVideosProdutos, "usaConfigVideosProdutos", ValorParametro.CONFIGVIDEOSPRODUTOS);
	}

	public static boolean isUsaConfigVideosProdutoAgrupadorGrade() {
		return isAtributoJsonLigado(configVideosProdutos, "usaConfigVideosProdutoAgrupadorGrade", ValorParametro.CONFIGVIDEOSPRODUTOS);
	}

	public static boolean isUsaConfigCampoDinamicoIndicadorProdutividadeInterno() {
		return isAtributoJsonLigado(configCampoDinamicoIndicadorProdutividadeInterno, "usa", ValorParametro.CONFIGCAMPODINAMICOINDICADORPRODUTIVIDADEINTERNO);
	}

	public static List<String> getDefineCampoDinamicoGrafico() {
		if (!isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
			return null;
		}
		String vlAtributo = getValorAtributoJson(configCampoDinamicoIndicadorProdutividadeInterno, "defineCampoDinamicoGrafico", ValorParametro.CONFIGCAMPODINAMICOINDICADORPRODUTIVIDADEINTERNO);
		String[] vlParam = StringUtil.getStringValue(vlAtributo).split(",");
		return Arrays.asList(vlParam);
	}

	public static String[] getDefineCorRGBCampoDinamicoGrafico() {
		if (!isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
			return null;
		}
		String vlAtributo = getValorAtributoJson(configCampoDinamicoIndicadorProdutividadeInterno, "defineCorRGBCampoDinamicoGrafico", ValorParametro.CONFIGCAMPODINAMICOINDICADORPRODUTIVIDADEINTERNO);
		return StringUtil.getStringValue(vlAtributo).split(",");
	}

	public static List<String> getDefineSubtitulosDinamicos() {
		if (!isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
			return null;
		}
		String vlAtributo = getValorAtributoJson(configCampoDinamicoIndicadorProdutividadeInterno, "defineSubtitulosDinamicos", ValorParametro.CONFIGCAMPODINAMICOINDICADORPRODUTIVIDADEINTERNO);
		String[] vlParam = StringUtil.getStringValue(vlAtributo).toUpperCase().split(",");
		return Arrays.asList(vlParam);
	}

	public static boolean isGraficoIndicadorSubtitulo(String valor) {
		List<String> subtitulos = getDefineSubtitulosDinamicos();
		if (subtitulos == null) {
			return false;
		}
		return subtitulos.contains(valor);
	}


	public static boolean isAvisaUsuarioSobreConsumoVerba() {
		return isAvisaUsuarioSobreConsumoVerba("1");
	}


	private static boolean isAvisaUsuarioSobreConsumoVerba(String option) {
		if (ValueUtil.isNotEmpty(avisaUsuarioSobreConsumoVerba)) {
			return avisaUsuarioSobreConsumoVerba.contains(option);
		}
		return false;
	}

	public static boolean isConfigStatusItemPedido() {
		return isAtributoJsonLigado(configStatusItemPedido, "usa", ValorParametro.CONFIGSTATUSITEMPEDIDO);
	}

	public static boolean isConfigStatusItemPedidoUsaCampoDiferenca() {
		return isAtributoJsonLigado(configStatusItemPedido, "usaCampoDiferenca", ValorParametro.CONFIGSTATUSITEMPEDIDO);
	}
	
	public static boolean isExibeRelatorioNovosSacsLogin() {
		return "2".equals(exibeRelatorioNovosSacs);
	}
	
	public static boolean isExibeRelatorioNovosSacsSync() {
		return "S".equals(exibeRelatorioNovosSacs) || "1".equals(exibeRelatorioNovosSacs);
	}
	
	public static boolean isExibeRelatorioNovosSacsPrimeiroSync() {
		return "3".equals(exibeRelatorioNovosSacs);
	}
	
	public static boolean isLigadoAlgumaRegraVerba() {
		return usaVerbaItemPedidoPorItemTabPreco || informaVerbaManual || isPermiteBonificarProdutoPedidoUsandoVerba()
				|| aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex
				|| (usaPedidoBonificacao() && !naoDescontaVerbaEmPedidoBonificacao)
				|| usaVerbaPorFaixaRentabilidadeComissao
				|| usaVerbaGrupoProdComToleranciaNoDesconto
				|| usaVerbaPositivaPorGrupoProdutoTabelaPreco;
	}
	
	public static boolean isMostraRelNovidadeAposPrimeiroSync(boolean isPrimeiroSyncDia) {
		return isPrimeiroSyncDia && (LavenderePdaConfig.mostraRelNovidadeAposPrimeiroSync
				&& (LavenderePdaConfig.usaRelNovidadeProduto || LavenderePdaConfig.usaNovidadeNovoClienteNaoIntegrado
						|| LavenderePdaConfig.isGeraNovidadeClientePrimeiraEtapa()
						|| LavenderePdaConfig.geraNovidadePesquisaMercado()
						|| LavenderePdaConfig.geraNovidadeAutorizacao));
	}
	
	public static boolean isUnificaBotoesEnviarReceberDados() {
		return isAtributoJsonLigado(configUnificaBotoesEnviarReceberDados, "usa", ValorParametro.CONFIGUNIFICABOTOESENVIARRECEBERDADOS);
	}
	
	public static int getIntervaloEnviarReceberDados() {
		if (isUnificaBotoesEnviarReceberDados()) {
			String valor = getValorAtributoJson(configUnificaBotoesEnviarReceberDados, "nuIntervaloEspera", ValorParametro.CONFIGUNIFICABOTOESENVIARRECEBERDADOS);
			return ValueUtil.getIntegerValue(valor);
		}
		return 0;
	}
	
	public static boolean usaConfigVerbaSaldoPorGrupoProduto() {
	    return isAtributoJsonLigado(configVerbaSaldoPorGrupoProduto, "usa", ValorParametro.CONFIGVERBASALDOPORGRUPOPRODUTO);
	}
	
    public static boolean isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal() {
		return isUsaPercQuantidadeDoItemOriginalBonificacaoTroca() || isUsaPercQuantidadeDosItensPedidoOriginalBonificacaoTroca();
    }
    
    public static boolean isUsaPercQuantidadeDoItemOriginalBonificacaoTroca() {
    	return ValueUtil.valueEquals("1", getValorAtributoJson(configPercentualQuantidadeOriginalBonificacaoTroca, "usa", ValorParametro.CONFIGPERCENTUALQUANTIDADEORIGINALBONIFICACAOTROCA));
    }
    
    public static boolean isUsaPercQuantidadeDosItensPedidoOriginalBonificacaoTroca() {
    	return ValueUtil.valueEquals("2", getValorAtributoJson(configPercentualQuantidadeOriginalBonificacaoTroca, "usa", ValorParametro.CONFIGPERCENTUALQUANTIDADEORIGINALBONIFICACAOTROCA));
    }
    
    public static double getPercentualDoItemOuPedidoOriginalNaTroca() {
    	if (!isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal()) {
    		return -1;
    	}
    	return ValueUtil.round(ValueUtil.getDoubleValue(getValorAtributoJson(configPercentualQuantidadeOriginalBonificacaoTroca, "percentualQuantidadeTroca", ValorParametro.CONFIGPERCENTUALQUANTIDADEORIGINALBONIFICACAOTROCA)));
    }
    
    public static double getPercentualDoItemOuPedidoOriginalNaBonificacao() {
    	if (!isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal()) {
    		return -1;
    	}
    	return ValueUtil.round(ValueUtil.getDoubleValue(getValorAtributoJson(configPercentualQuantidadeOriginalBonificacaoTroca, "percentualQuantidadeBonificacao", ValorParametro.CONFIGPERCENTUALQUANTIDADEORIGINALBONIFICACAOTROCA)));
    }
	
	public static boolean isObrigaFotoItemTroca() {
		return ValueUtil.isNotEmpty(usaPedidoExclusivoTrocaRecolher) && isAtributoJsonLigado(configPedidoExclusivoTrocaRecolher, "obrigaFotoItemTroca", ValorParametro.CONFIGPEDIDOEXCLUSIVOTROCARECOLHER);
	}
	public static boolean usaVerbaGrupoSaldoPersonalizada() {
	    return usaConfigVerbaSaldoPorGrupoProduto() && isAtributoJsonLigado(configVerbaSaldoPorGrupoProduto, "verbaGrupoSaldoPersonalizada", ValorParametro.CONFIGVERBASALDOPORGRUPOPRODUTO);
	}

	public static boolean usaUnificaVerbaGrupoSaldoPorEmpresa() {
	    return usaVerbaGrupoSaldoPersonalizada() && usaVerbaUnificada;
	}

	public static boolean isConfigApresentacaoInfosPersonalizadasCapaItemPedido() {
		return isAtributoJsonLigado(configApresentacaoInfosPersonalizadasCapaItemPedido, "usa", ValorParametro.CONFIGAPRESENTACAOINFOSPERSONALIZADASCAPAITEMPEDIDO) && !apresentaDsFichaTecnicaCapaItemPedido;
	}

	public static String getConsultaSqlApresentacaoInfosPersonalizadasCapaItemPedido() {
		return getValorAtributoJson(configApresentacaoInfosPersonalizadasCapaItemPedido, "consultaSql", ValorParametro.CONFIGAPRESENTACAOINFOSPERSONALIZADASCAPAITEMPEDIDO);
	}
	
	public static boolean usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente() {
		return usaVerbaItemPedidoPorItemTabPreco && usaVerbaPositivaApenasPedidoCorrente && permiteDescValorPorPedidoConsumindoVerba > 0;
	}

	public static void loadControleLoginUsuario() throws SQLException {
		controleLoginUsuario = getValorParametroPorSistema(ValorParametro.CONTROLELOGINUSUARIO);
		nuTentativasLogin = getNuTentativasLogin();
		nuMinutosTentativasLogin = getNuMinutosTentativasLogin();
		nuMinutosResetBloqueioUsuario = getNuMinutosResetBloqueioUsuario();
		nivelRegistroDeLogNoPda = LogPda.getCdNivelInt(getValorParametroPorSistema(ValorParametro.NIVELREGISTRODELOGNOPDA));
		usaConfigAcessoSistema = !ValueUtil.VALOR_NAO.equals(getValorAtributoJson(controleLoginUsuario, "usaConfigAcessoSistema", ValorParametro.CONTROLELOGINUSUARIO));
		nuMinutosAvisoBloqueioSistema = getNuMinutosAvisoBloqueioSistema();
		sementeSenhaLiberacaoSisBloqueado = ValueUtil.getIntegerValue(getValorAtributoJson(controleLoginUsuario, "sementeSenhaLiberacaoSisBloqueado", ValorParametro.CONTROLELOGINUSUARIO));
	}


	private static int getNuMinutosAvisoBloqueioSistema() {
		try {
			String vlParametro = getValorAtributoJson(controleLoginUsuario, "nuMinutosAvisoBloqueioSistema", ValorParametro.CONTROLELOGINUSUARIO);
			if (ValueUtil.VALOR_SIM.equals(vlParametro)) {
				return 5;
			} else {
				int nuMinutosAviso = ValueUtil.getIntegerValue(vlParametro);
				return nuMinutosAviso > 5 ? nuMinutosAviso : 5;
			}
		} catch (Exception e) {
			return 5;
		}
	}

	private static Integer getNuTentativasLogin() {
		try {
			String vlParametro = getValorAtributoJson(controleLoginUsuario, "qtdPermitidaLoginInvalido", ValorParametro.CONTROLELOGINUSUARIO);
			if (ValueUtil.VALOR_SIM.equals(vlParametro)) {
				return 1;
			} else {
				return ValueUtil.getIntegerValue(vlParametro);
			}
		} catch (Exception e) {
			return 0;
		}
	}

	private static Integer getNuMinutosTentativasLogin() {
		int minutos24Hora = 1440;
		try {
			int intParam = ValueUtil.getIntegerValue(getValorAtributoJson(controleLoginUsuario, "nuMinutosResetCacheLoginsErradosUsuario", ValorParametro.CONTROLELOGINUSUARIO));
			return intParam <= 0 ? minutos24Hora : intParam;
		} catch (Exception e) {
			return minutos24Hora;
		}
	}

	private static Integer getNuMinutosResetBloqueioUsuario(){
		try {
			String vlParametro = getValorAtributoJson(controleLoginUsuario, "nuMinutosResetBloqueioUsuario", ValorParametro.CONTROLELOGINUSUARIO);
			if (ValueUtil.VALOR_SIM.equals(vlParametro)) {
				return 1;
			} else {
				return ValueUtil.getIntegerValue(vlParametro);
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int getNuRegistrosProdutosMostradosPorVezNaLista() {
		return getNuRegistrosMostradosPorVezNaLista("nuRegistrosListaProdutos");
	}
	
	public static int getNuRegistrosAddItemPedidoMostradosPorVezNaLista() {
		return getNuRegistrosMostradosPorVezNaLista("nuRegistrosListaInserirItem");
	}
	
	public static int getNuRegistrosClientesMostradosPorVezNaLista() {
		return getNuRegistrosMostradosPorVezNaLista("nuRegistrosListaCliente");
	}
	
	private static int getNuRegistrosMostradosPorVezNaLista(String atributoJson) {
		int qtRegistros = ValueUtil.getIntegerValue(getValorAtributoJson(configNumeroRegistrosMostradosPorVezNaLista, atributoJson, ValorParametro.CONFIGNUMEROREGISTROSMOSTRADOSPORVEZNALISTA));
		if (qtRegistros < 20 && qtRegistros != 0) {
			qtRegistros = 20;
		}
		return qtRegistros;
	}
	
	public static boolean getNuDiasMaxFiltroDataListaLeadsLigado() {
		return !ValueUtil.VALOR_NAO.equals(getValorAtributoJson(configLeads, "nuDiasMaxFiltroDataListaLeads", ValorParametro.CONFIGLEADS));
	}

	public static boolean isApresentaTelefoneListaLeadsLigado() {
		return !ValueUtil.VALOR_NAO.equals(getValorAtributoJson(configLeads, "apresentaTelefone", ValorParametro.CONFIGLEADS));
	}
	
	public static boolean isApresentaSiteListaLeadsLigado() {
		return !ValueUtil.VALOR_NAO.equals(getValorAtributoJson(configLeads, "apresentaSite", ValorParametro.CONFIGLEADS));
	}

	public static boolean isApresentaNotaListaLeadsLigado() {
		return !ValueUtil.VALOR_NAO.equals(getValorAtributoJson(configLeads, "apresentaNota", ValorParametro.CONFIGLEADS));
	}
	
	public static int getNuDiasMaxFiltroDataListaLeads() {
		return ValueUtil.getIntegerValue(getValorAtributoJson(configLeads, "nuDiasMaxFiltroDataListaLeads", ValorParametro.CONFIGLEADS));
	}

	private static boolean isUsaPrioridadeLiberacaoPendenciaPedido() {
		return isAtributoJsonLigado(configPrioridadeLiberacaoPendenciaPedido, "usa", ValorParametro.CONFIGPRIORIDADELIBERACAOPENDENCIAPEDIDO);
	}
	
	public static boolean isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido() {
		return isUsaPrioridadeLiberacaoPendenciaPedido() && (usaMarcaPedidoPendenteAprovacaoCondPagto() || usaMarcaPedidoPendenteBaseadoLimiteCredito());
	}

	public static boolean isUsaCategoriaLeads() {
		return !ValueUtil.VALOR_NAO.equals(getValorAtributoJson(configLeads, "usaCategoria", ValorParametro.CONFIGLEADS));
	}
	
	private static boolean isUsaPermiteNaoUtilizarRentabilidade() {
		return isAtributoJsonLigado(configMargemRentabilidade, "permiteNaoUtilizarRentabilidade", ValorParametro.CONFIGMARGEMRENTABILIDADE);
	}
	public static boolean isPermiteNaoUtilizarRentabilidade() {
		return usaConfigMargemRentabilidade() && isUsaPermiteNaoUtilizarRentabilidade();
	}
}
