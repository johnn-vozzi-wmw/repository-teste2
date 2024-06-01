package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.cache.SolAutorizacaoItemPedidoCache;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoDTO;
import br.com.wmw.lavenderepda.business.service.BonifCfgService;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.DescMaxProdCliService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoEmpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import br.com.wmw.lavenderepda.business.service.ItemComboService;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAudService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PoliticaComercialFaixaService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoRepService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.TipoFreteTabPrecoService;
import br.com.wmw.lavenderepda.business.service.TributacaoConfigService;
import br.com.wmw.lavenderepda.business.service.TributacaoService;
import br.com.wmw.lavenderepda.business.service.TributacaoVlBaseService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.business.service.VerbaService;
import br.com.wmw.lavenderepda.business.service.VerbaTabelaPrecoService;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ItemPedido extends BaseDomain {

    public static String TABLE_NAME_ITEMPEDIDO = "TBLVPITEMPEDIDO";
    public static String TABLE_NAME_ITEMPEDIDOERP = "TBLVPITEMPEDIDOERP";
    public static final String SIMPLE_NAME = "ItemPedido";

    public static String ITEMPEDIDO_FLORIGEMERP = "E";

    public static final int ITEMPEDIDO_SEM_EDICAO = 0;
	public static final int ITEMPEDIDO_EDITANDO_DESCONTOPCT = 1;
	public static final int ITEMPEDIDO_EDITANDO_ACRESCIMOPCT = 2;
	public static final int ITEMPEDIDO_EDITANDO_QTD = 3;
	public static final int ITEMPEDIDO_EDITANDO_VLITEM = 4;
	public static final int ITEMPEDIDO_EDITANDO_QTD_FATURAMENTO = 5;
	public static final int ITEMPEDIDO_EDITANDO_VLELEMENTAR = 6;
	public static final int ITEMPEDIDO_EDITANDO_VLITEMST = 7;
	public static final int ITEMPEDIDO_EDITANDO_VLDESCONTOST = 8;
	public static final int ITEMPEDIDO_EDITANDO_VLITEMIPI = 9;
	public static final int ITEMPEDIDO_EDITANTO_RENTABILIDADE = 10;

	public static final String FLORIGEMESCOLHA_PADRAO = "1";
	public static final String FLORIGEMESCOLHA_SIMILARES = "2";
	public static final String FLORIGEMESCOLHA_GIROPRODUTO = "3";
	public static final String FLORIGEMESCOLHA_MULTIPLOS_COMPLEMENTARES = "4";
	public static final String FLORIGEMESCOLHA_NAO_INSERIDOS = "5";

	public static final int NUSEQPRODUTO_UNICO = 1;
	
	public static final String CALCULO_DECISAO_CANAL_PRECO_FORMULA_A = "A";
	public static final String CALCULO_DECISAO_CANAL_PRECO_FORMULA_B = "B";

	public static final String DS_COLUNA_VLBASEFLEX = "vlBaseFlex";
	public static final String DS_COLUNA_VERBA_ITEM = "vlVerbaItem";
	public static final String DS_COLUNA_VERBA_ITEM_POSITIVO = "vlVerbaItemPositivo";
	public static final String DS_FLTIPOCADASTROITEM = "flTipoCadastroItem";
	public static final String DS_COLUNA_QTITEMGONDOLA = "qtItemGondola";
	public static final String DS_COLUNA_QTITEMFISICO = "qtItemFisico";
	public static final String DS_COLUNA_CDCOMBO = "cdCombo";
	public static final String DS_COLUNA_FLAUTORIZADO = "flAutorizado";
	public static final String DS_COLUNA_FLAGRUPADORSIMILARIDADE = "flAgrupadorSimilaridade";
	public static final String DS_COLUNA_CDAGRUPADORSIMILARIDADE = "cdAgrupadorSimilaridade";
	public static final String DS_COLUNA_VLTOTALITEMPEDIDOFRETE = "vlTotalItemPedidoFrete";
	public static final String DS_COLUNA_NUORDEMCOMPRACLIENTE = "nuOrdemCompraCliente";

	public static final int FLEDICAOITEMGRADE_INSERINDO = 1;
	public static final int FLEDICAOITEMGRADE_ATUALIZANDO = 2;
	public static final int FLEDICAOITEMGRADE_EXCLUINDO = 3;

	public static boolean ignoreRoundDecisaoCanalCli;

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
	public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public int nuSeqItemPedido;
    public String cdTabelaPreco;
    public double qtItemFisico;
    public double qtItemFaturamento;
    public double qtPeso;
    public double vlItemPedido;
    public double vlBaseItemTabelaPreco;
    public double vlBaseCalculoDescPromocional;
    public double vlBaseItemPedido;
    public double vlBaseFlex;
    public double vlTotalItemPedido;
    public double vlPctDesconto;
    public double vlPctAcrescimo;
    public double vlRentabilidade;
    public double vlPctComissao;
    public String cdLinha;
    public double vlVerbaItem;
    public double vlVerbaItemPositivo;
    public double vlItemPedidoFrete;
    public double vlTotalItemPedidoFrete;
    public String cdContaCorrente;
    public String cdLoteProduto;
    public String flPrecoLiberadoSenha;
    public double vlSt;
    public double vlReducaoOptanteSimples;
    public String cdMotivoTroca;
    public String dsObsMotivoTroca;
    public String dsObservacao;
    public int qtPontosItem;
    public String cdUnidade;
    public int cdPrazoPagtoPreco;
    public String flVendidoQtMinima;
    public double vlTotalBrutoItemPedido;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public double vlUnidadePadrao;
    public double vlItemIpi;
    public double vlBaseItemIpi;
    public double vlBaseEmbalagemElementar;
    public double vlItemPedidoUnElementar;
    public double qtItemPedidoUnElementar;
    public double vlFrete;
    public double vlFreteOld;
    public String flMetaGrupoProdLiberadoSenha;
    public String flLiberadoVendaRelacionada;
    public double vlIpiItem;
    public double qtItemEstoquePositivo;
    public double vlDescontoCCP;
    public double vlPctDescontoCCP;
    public double vlFinalPromo;
    public double vlPctDescontoPromo;
    public double vlPctFaixaDescQtd;
    public double vlFecop;
    public double vlIcms;
    public double vlPis;
    public double vlCofins;
    public double vlDespesaAcessoria;
    public String cdSugestaoVenda;
    public String cdTributacaoConfig;
    public double vlPctDescontoCanal;
    public double vlSeguroItemPedido;
    public double vlDescontoPromo;
    public Date dtInicioPromocao;
    public int nuPromocao;
    public String flEstoqueLiberado;
    public String cdCondicaoComercial;
    public double vlCreditoFrete;
    public double vlCreditoCondicao;
    public double vlDesconto;
    public double vlPctDesconto2;
    public double vlDesconto2;
    public double vlPctDesconto3;
    public double vlDesconto3;
    public int cdVerbaGrupo;
    public double vlTotalStItem;
    public double vlTotalIpiItem;
    public double vlTotalIcmsItem;
    public double vlTotalPisItem;
    public double vlTotalCofinsItem;
    public String cdUsuarioLiberacao;
    public String flQuantidadeLiberada;
    public String flPendente;
    public double vlPctDescProgressivoMix;
    public double vlVolumeItem;
	public double vlPctDescFrete;
	public double vlPctDescCliente;
	public double vlPctAcrescCliente;
	public double vlPctDescontoCondicao;
	public double vlPctAcrescimoCondicao;
	public double vlPctVerbaRateio;
	public double vlPctMargemProduto;
	public String cdVerba;
	public double vlPctContratoCli;
	public String flDecisaoCalculo;
	public double vlItemPedidoStReverso;
    public double vlPctDescontoStReverso;
    public String flRestrito;
    public String flPromocao;
    public String flValorTabelaAlterado;
    public double vlIndiceGrupoProd;
    public int qtdCreditoDesc;
    public String flTipoCadastroItem;
    public String cdProdutoCreditoDesc;
    public double qtItemDesejado;
    public double vlTotalMargemItem;
    public String flSugVendaPerson;
    public double qtEstoqueCliente;
    public double vlTotalPrecoCusto;
    public double vlTotalBaseStItem;
    public double vlTotalBaseIcmsItem;
    public int nuDiasPrazoEntrega;
    public double nuConversaoUnidadePu;
    public double vlIndiceFinanceiroPu;
    public String flDivideMultiplicaPu;
    public String cdKit;
    public double vlPctMargemAgregada;
    public double vlPctDescAlcada;
    public double vlPctAcrescimoIcms;
    public double vlPctDescontoIcms;
    public String cdLocal;
    public Date dtEntrega;
    public double vlAltura;
    public double vlLargura;
    public double vlComprimento;
    public int vlPosVinco1;
    public int vlPosVinco2;
    public int vlPosVinco3;
    public int vlPosVinco4;
    public int vlPosVinco5;
    public int vlPosVinco6;
    public int vlPosVinco7;
    public int vlPosVinco8;
    public int vlPosVinco9;
    public int vlPosVinco10;
    public String flOrigemEscolhaItemPedido;
    public String cdProdutoOrigem;
    public double vlItemPedidoOrigem;
    public Date dtInclusaoItemPedido;
    public String hrInclusaoItemPedido;
    public double vlIndiceClienteGrupoProd;
    public double vlTotalFecopItem;
    public String flErroRecalculo;
    public double vlPctDescProdutoRestrito;
    public double vlVpc;
    public double vlIndiceVolume;
    public double vlRetornoProduto;
    public double vlPctDescontoAuto;
    public double vlPctDescontoAutoEfetivo;
    public double vlDescontoAuto;
    public double vlTotalDescontoAuto;
    public double vlPrecoEfetivoUnitario;
    public double vlEfetivoTotalItem;
    public double vlEfetivoTotalItemDesc;
    public double vlDescontoTotalAutoDesc;
    public double vlPctDescontoEfetivo;
    public double vlPrecoEfetivoUnitarioDesc;
    public double vlBaseInterpolacaoProduto;
    public String cdGrupoDescProd;
    public String cdGrupoDescCli;
    public String cdUnidadeDescPromocionalFilter;
    public double vlDescontoCondicao;
    public double qtEstoquePrevisto;
    public Date dtEstoquePrevisto;
    public double vlItemFreteTributacao;
    public double vlTotalItemFreteTributacao;
    public double vlPctTotalMargemItem;
    public double vlPctDescCondPagto;
    public double vlVerbaGrupoItem;
    public String cdPacote;
    public double vlIndiceCondicaoPagto;
    public double vlBaseAntecipacao;
    public String cdMotivoPendencia;
    public int nuOrdemLiberacao;
    public String flPromocional;
    public String cdPoliticaComercial;
    public double vlPctPoliticaComercial;
    public int qtItemGondola;
    public double vlPontuacaoBaseItem;
    public double vlPontuacaoRealizadoItem;
    public double vlPesoPontuacao;
    public double vlFatorCorrecaoFaixaPreco;
    public double vlFatorCorrecaoFaixaDias;
    public double vlPctFaixaPrecoPontuacao;
    public double vlBasePontuacao;
    public double vlTotalMaoDeObra;
    public double vlTotalImpostos;
    public double vlTotalCustoComercial;
    public double vlTotalCustoFinanceiro;
    public double vlTotalComissao;
	public String cdCultura;
	public String cdPraga;
	public String vlDose;
	public String cdMargemRentab;
	public double vlBaseMargemRentab;
	public double vlCustoMargemRentab;
	public double vlPctMargemRentab;
	public double vlDescProdutoRestrito;
	public double vlTotalDescProdutoRestrito;
	public String cdCombo;
	public double vlIndicePlataformaVendaCliFin;
	public double vlPctComissaoTotal;
	public String cdProdutoClienteCod;
	public String dsProduto;
	public String flAutorizado;

	public String cdDescProgressivo;
	public double vlPctDescProg;
	public String flPedidoPerdido;
	public String cdProdutoCliente;

	public double vlRentabilidadeSug;

	public String cdPontuacaoConfig;
	public int qtDiasFaixaPontuacao;
	public String flAgrupadorSimilaridade;
	public String cdAgrupadorSimilaridade;
	public double vlPctFaixaDescQtdPeso;
	public String cdVerbaSaldoCliente;

	public double vlCotacaoMoedaProduto;
	public String dsMoedaVendaProduto;
	public String cdStatusItemPedido;
	public String nuOrdemCompraCliente;
	public int nuSeqOrdemCompraCliente;
	public String infosPersonalizadas;

	//Não persistentes
    public Pedido pedido;
    public FaceamentoEstoque faceamentoEstoque;
    public Estoque estoque;
	public String cdTabelaPrecoFilter;
	public ItemPedidoAuxVariavel auxiliarVariaveis = new ItemPedidoAuxVariavel();
	private Produto produto;
    private ProdutoBase produtoBase;
    private ItemTabelaPreco itemTabelaPreco;
    private ItemTabelaPreco itemTabelaPrecoOld;
    public int cdPrazoPagtoPrecoOld;
    private ProdutoUnidade produtoUnidade;
    public int flTipoEdicao;
    public boolean flEditandoQtItemFaturamento;
    public String cdUfClientePedido;
    public double qtItemPedidoMinimo;
    private double oldQtItemFisico;
    public double oldQtItemFisicoDescQtd;
    public double oldQtItemFisicoDescPromocionalQtd;
    public double oldQtItemFaturamento;
    public double oldQtPeso;
    public Date dtEmissaoPedido;
    public DescQuantidade descQuantidade;
    public DescComiFaixa descComissaoProd;
    public DescComiFaixa descComissaoGrupo;
    public DescPromocional descPromocional;
    public Vector descPromocionalComQtdList;
    public TributacaoConfig tributacaoConfig;
    private Tributacao tributacao;
    private TributacaoVlBase tributacaoVlBase;
    public double vlTotalItemPedidoOld;
    public double vlRentabilidadeOld;
    public double vlVerbaItemOld;
    public double vlVerbaItemPositivoOld;
    public double vlPctVerba;
    public double vlVerbaManual;
    public double vlPctDescPrev;
    public Vector verbaItemPedidoList;
    public Vector itemKitPedidoList;
    public Vector itemPedidoGradeList;
    private List<ItemPedidoGrade> itemPedidoGradeErroList;
    public boolean ignoraValidacaoDesconto;
	public boolean ignoraValidacaoDescontoOld;
    public double pctMaxDescLoteProdutoSelected;
    public double pctMaxDescCestaPromo;
    public boolean usaPctMaxDescLoteProduto;
    public boolean usaCestaPromo;
    public boolean forcaReservaEstoque;
    public boolean precoMinimoListaColunaExtrapolado;
    public boolean pontosMinimoItemNaoAlcancado;
    public boolean isVlPrecoCustoNull;
    public Vector verbaTabelaPrecoList;
    public double qtItemMinAfterLibPreco;
    public double vlItemMinAfterLibPreco;
    public double qtItemAfterLibPreco;
    public double vlItemAfterLibPreco;
    public double qtItemMinDescComissao;
    public double qtItemMesmoGrupo;
    public double vlPctMaxDescGrupo;
    public double nuConversaoUnidade;
    public double qtEmbalagemElementar;
    public double vlEmbalagemElementar;
    public boolean isItemPedidoSugestaoGiro;
    public boolean usandoPrecoUnidadePadraoConvertida;
    public boolean flDescQtdGrupoAplicadoAuto;
    public String flOportunidade;
    public double qtItemFisicoNaoInseridoSugestaoPedido;
    public double vlVerbaItemSugestaoPedido;
    public double vlPctDescontoNaoInseridoSugestaoPedido;
    public String dsMotivoItemNaoInseridoSugestaoPedido;
    public String dsMotivoItemPendentePedido;
    private TipoFreteTabPreco tipoFreteTabPreco;
    public double qtPesoTipoFreteTabPrecoAtual = -1;
    public double qtPesoTipoFreteTabPrecoPosterior = -1;
    private ItemPedidoAud itemPedidoAud;
    public boolean atualizouCusto;
    public boolean isIgnoraMensagemEstoqueNegativo;
	private CondicaoComercial condicaoComercial;
	private TabelaPreco tabelaPreco;
	private TabelaPrecoRep tabelaPrecoRep;
	private TabelaPreco tabelaPrecoOld;
    public boolean possuiDiferenca;
    public boolean pendenteMaxAcresc;
    public boolean pendenteMaxDesc;
    public boolean retiraDescItem;
    public double qtItemVendidoTroca;
    public double oldQtEstoqueConsumido;
    public String cdTabelaPrecoOld;
    public boolean usaCdTributacaoCliente2;
    public double vlDecisaoCanalFormulaA;
    public ItemPedido itemPedidoUltimoPedidoCliente;
    public String cdCliente;
    public boolean naoComparaSeqItem;
    public double qtDevolvida;
    public double qtDevolvidaOld;
    public double vlManualBrutoItem;
    public double oldPctDescSemNegoc;
    public double oldVlBruto;
	public ProdutoCreditoDesc produtoCreditoDesc;
    public Vector itemGradeList; 
    public Vector descPromocionalGradeList;
    public boolean gradeAbertaValidated;
    public boolean itemChanged;
    public boolean itemUpdated;
    public Vector unidadesListSugPerson;
    public boolean flInvisivelSug;
    public ItemGrade itemGrade1;
    public ItemGrade itemGrade2;
    public double vlPctDescKitFechado;
    public double vlBaseFlexAnterior;
    public boolean removeFromGrid;
    public double vlTotalItemTabelaPreco;
    public DescMaxProdCli descMaxProdCli;
    public Map<String,Object> hashValoresInfoComplementares;
    public Vector itemPedidoPorGradePrecoList;
    public boolean ignoraValidacaoVerbaTemp;
    public boolean ignoraSegundaValidacaoGradeEstoqueNegativo;
    public double vlVerbaGrupoOld;
    public Vector produtoLoteList;
    public double vlPctDescPedido;
    public Date dtPagamento;
    public double vlBaseTaxaAntecipacao;
    public boolean selecionadoTabelaComMaiorDesconto;  
    public MotivoPendencia motivoPendencia;
    public PoliticaComercialFaixa politicaComercialFaixa;
    public PoliticaComercial politicaComercial;
    public HashMap<String, Object> variavelCalculoList;
    public boolean itemComboInserido;
    public boolean zerouDescAcresByPoliticaComercial;
    public double vlToleranciaVerGruSaldo;
    public double vlToleranciaVerGruSaldoOld;
    public String cdStatusPedidoFilter;
	public RestricaoProduto restricaoProduto;
	private ItemKit itemKit;
	public double vlBaseIcmsCalcFecop;
	public boolean unidadeAlternativaChanged;
	public String dsMotivoAdvertencia;
	public int flEdicaoItemPedidoGrade;
	public boolean flUIChange;
	public double vlPctDescontoReplicacaoGrade;
    public boolean reservado;
    public boolean consumiuPrev;
    public double qtReservaPrev;
    public double qtEstoqueDisponivel;
	private Vector bonifCfgList;
	public boolean forceRefreshBonifCfgList;
	public double vlBonificado;
	public double qtBonificadoBonifCfg;
	public Date dtSugestaoCliente;
	public String cdLocalEstoque;
    public String cdCentroCusto;
	public String dsStatusItemPedido;
	public boolean isAplicaIndiceCondPagtoPorDescQtdIgnorado;
	
	public GiroProduto giroProduto;
    
	//Não Alterar
    public boolean isItemAgrupador = false;

    //-- Controles para ignora/considerar o desc quantidade prm(80/908/1012)
    public String flIgnoraDescQtd;
    public String flIgnoraDescQtdPro;
    
	//-- usaControleNoDescontoPromocional
    public boolean flAbaixoPtReferenciaDesc;
    public double vlPtReferencialDesc;
    public static String sortAttr;
    public int qtPedidosContido;
    public double vlAgregadoSugerido;
    public double pctMargemAgregada;
	public double vlOportunidadeOld;
	public double vlItemTabPrecoVariacaoPreco;
	public boolean descontoProgressivoAplicado;
	public double vlItemPedidoStBase;
	public boolean usaDescontoCascata;
	public int tipoDesc;
	public double oldVlPctDesc;
	public boolean gerouEmbalagemCompleta;
	public int qtdCreditoDescOld;
	public int qtdItemGerarCred;
	public boolean bloqueiaAplicaDescPromocional;
	public double vlNegociado;
	public double vlPctDescInicial;
	public double vlPctAcrescInicial;
	public boolean recalculaDescPromo;
	public int cdComissaoPedidoRep;
	public ComissaoPedidoRep comissaoPedidoRep;
	public DescontoPacote descontoPacote;
    public double qtItemMesmoPacote;
    public double vlPctMaxDescPacote;
	public DescontoGrupo descontoGrupoProduto;
	public boolean isPedidoSemPrazoCadastrado;
	//-- controles Verba personalizada
	public boolean consumiuVerbaFornecedor;
	public boolean consumiuVerbaGrupoSaldo;
	public boolean consumiuVerbaItem;
	public boolean isReabrindoPedido;
	public PontuacaoProduto pontuacaoProduto;
	public PesoFaixa pesoFaixa;
	public int sugPersonCreditosGerados;
	public int sugPersonQtItensInseridos;
	public double sugPersonVlInserido;
	public String dsColecao;
	public String dsStatusColecao;
	public String dsDimensoes;

	public boolean inCarrousel;
	
	public boolean fromDescQtdWindow;
	public boolean fromProdutoFaltaWindow;
	public boolean fromProdutoFaltaMsg;
	public boolean isRegistrouProdutoFalta;

	private ItemCombo itemCombo;
	public double oldVlPctFaixaDescQtd;
	public double oldVlDescontoFaixaDescQtd;
	public boolean cancelouDescQtd;
	public boolean editandoDescQtd;
	public SolAutorizacao solAutorizacao;
	public String cdProdutoSimilarOrg;
	public boolean isItemSimilar;

	public boolean atualizandoDesc;
	public boolean alterouFaixa;
	public String dsAgrupadorGradeFilter;
	public boolean isItemBrinde;
	public double qtItemBrinde;
	public boolean isIgnoraControleBonifCfgBrinde;
	private boolean isLoadedDescPromocional;
	private boolean isLoadedProdutoUnidade;
	public String[] nuPedidoListFilter;

	//--PDF Online
	public double qtEncomenda;
	public double vlTotalEncomenda;
	public double vlItemPedidoOriginal;

    //-- Controle de cache solAutorizacao
    public SolAutorizacaoItemPedidoCache solAutorizacaoItemPedidoCache = new SolAutorizacaoItemPedidoCache();

	//--controle de join por desconto por quantidade
	public boolean isJoinDescQt;

	//controle para botao bonificar item
	public boolean isBonificandoItemPeloBotao;

    public Vector estoquePrevistoList;

    public boolean isBonificacaoAutomatica;

	public boolean isBuscaFlKitProduto;
	public String flProdutoKitFromBusca;
	
	public boolean isInserindoItemPoliticaBonificacao;
	
	public boolean buscarItensConsumindoVerba;
	public String cdGrupoProduto1;
	
	public Vector fotoItemTrocaList;

    public ItemPedido() {
    	verbaItemPedidoList = new Vector(0);
    	itemKitPedidoList = new Vector(0);
    	itemPedidoGradeList = new Vector(0);
    	descPromocionalComQtdList = new Vector(0);
    	itemPedidoPorGradePrecoList = new Vector(0);
    	qtItemGondola = -1;
    	createHashItensInfoComplementar();
    }
	
	public ItemPedido(Pedido pedido) {
		this();
		this.cdEmpresa = pedido.cdEmpresa;
		this.cdRepresentante = pedido.cdRepresentante;
		this.nuPedido = pedido.nuPedido;
		this.flOrigemPedido = pedido.flOrigemPedido;
	}
	
	private void createHashItensInfoComplementar() {
		hashValoresInfoComplementares = new HashMap<String, Object>();
		hashValoresInfoComplementares.put(Messages.LABEL_ALTURA, null);
		hashValoresInfoComplementares.put(Messages.LABEL_LARGURA, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLCOMPRIMENTO, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO1, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO2, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO3, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO4, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO5, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO6, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO7, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO8, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO9, null);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO10, null);
		hashValoresInfoComplementares.put(Messages.LABEL_DTENTREGA, null);
	}
	
	public void loadHashValoresInfoComplementares() {
		hashValoresInfoComplementares.put(Messages.LABEL_ALTURA, vlAltura);
		hashValoresInfoComplementares.put(Messages.LABEL_LARGURA,vlLargura);
		hashValoresInfoComplementares.put(Messages.LABEL_VLCOMPRIMENTO, vlComprimento);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO1, (double) vlPosVinco1);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO2, (double) vlPosVinco2);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO3, (double) vlPosVinco3);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO4, (double) vlPosVinco4);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO5, (double) vlPosVinco5);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO6, (double) vlPosVinco6);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO7, (double) vlPosVinco7);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO8, (double) vlPosVinco8);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO9, (double) vlPosVinco9);
		hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO10, (double) vlPosVinco10);
		hashValoresInfoComplementares.put(Messages.LABEL_DTENTREGA, dtEntrega);
	}
	
	public ItemPedido(ItemPedidoBuilder itemPedidoBuilder) {
		this.cdEmpresa = itemPedidoBuilder.cdEmpresa;
		this.cdRepresentante = itemPedidoBuilder.cdRepresentante;
		this.flOrigemPedido = itemPedidoBuilder.flOrigemPedido;
		this.nuPedido = itemPedidoBuilder.nuPedido;
		this.cdProduto = itemPedidoBuilder.cdProduto;
		this.tabelaPreco = itemPedidoBuilder.tabelaPreco;
		this.flTipoItemPedido = itemPedidoBuilder.flTipoItemPedido;
		this.itemTabelaPreco = itemPedidoBuilder.itemTabelaPreco;
		this.nuSeqProduto = itemPedidoBuilder.nuSeqProduto;
		this.cdTabelaPreco = itemPedidoBuilder.cdTabelaPreco;
		this.pedido = itemPedidoBuilder.pedido;
	}
	
	public ItemPedido(ItemPedidoDTO itemPedidoDTO) {
		this();
		try {
			FieldMapper.copy(itemPedidoDTO, this);
			if (itemPedidoDTO.itemPedidoGradeErpList != null && itemPedidoDTO.itemPedidoGradeErpList.length > 0) {
				int size = itemPedidoDTO.itemPedidoGradeErpList.length;
				itemPedidoGradeList = new Vector(size);
				for (int i = 0; i < size; i++) {
					itemPedidoGradeList.addElement(new ItemPedidoGrade(itemPedidoDTO.itemPedidoGradeErpList[i]));
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public ItemPedido(int nuOrdemLiberacao, String flRegraLiberacao) {
		this();
		this.nuOrdemLiberacao = nuOrdemLiberacao;
		this.motivoPendencia = new MotivoPendencia(flRegraLiberacao);
	}

	public ItemPedido(Pedido pedido, int nuOrdemLiberacao) {
		this(pedido);
		this.nuOrdemLiberacao = nuOrdemLiberacao;
	}
	
	public ItemPedido(ItemPedidoBonifCfg itemPedBonifCfg) {
		this();
		this.cdEmpresa = itemPedBonifCfg.cdEmpresa;
		this.cdRepresentante = itemPedBonifCfg.cdRepresentante;
		this.nuPedido = itemPedBonifCfg.nuPedido;
		this.flOrigemPedido = itemPedBonifCfg.flOrigemPedido;
		this.cdProduto = itemPedBonifCfg.cdProduto;
		this.flTipoItemPedido = itemPedBonifCfg.flTipoItemPedido;
		this.nuSeqProduto = itemPedBonifCfg.nuSeqProduto;
	}

	//Usado para ordenação em Vetores de ItemPedido
    //Override
    public String toString() {
    	if ("CDPRODUTO".equals(sortAttr)) {
    		return cdProduto;
    	} else if ("DSPRODUTO".equals(sortAttr)) {
    		try {
				return getProduto().dsProduto;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	} else if ("NUSEQITEMPEDIDO".equals(sortAttr)) {
    		return StringUtil.getStringValue(nuSeqItemPedido);
    	} else if ("QTPEDIDOSCONTIDO".equals(sortAttr)) {
    		return StringUtil.getStringValue(qtPedidosContido);
    	}
    	return super.toString();
    }

    //Override
    public int getSortIntValue() {
    	if (sortAttr.equals("NUSEQITEMPEDIDO")) {
    		return ValueUtil.getIntegerValue(nuSeqItemPedido);
    	}
    	if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() && produto != null) {
			return produto.getSortIntValue();
    	}
    	return ValueUtil.getIntegerValue(cdProduto);
    }
    
    @Override
    public double getSortDoubleValue() {
    	if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() && produto != null 
    			&& (ProdutoBase.SORT_COLUMN_PRECO.equalsIgnoreCase(sortAttr) || ProdutoBase.SORT_COLUMN_VLPRECOPADRAO.equalsIgnoreCase(sortAttr) || ProdutoBase.SORT_COLUMN_VLMEDIOHIST.equalsIgnoreCase(sortAttr))) {
			return produto.getSortDoubleValue();
    	} else if (LavenderePdaConfig.usaOrdenacaoVerbaItemPedido && DS_COLUNA_VERBA_ITEM.equalsIgnoreCase(sortAttr)) {
    		if (DS_COLUNA_VERBA_ITEM.equalsIgnoreCase(sortAttr)) return vlVerbaItem + vlVerbaItemPositivo;
    	} else if (isOrdenaEstoque()) {
    		return getEstoqueItem();
    	}
    	return super.getSortDoubleValue();
    }
    
    @Override
    public String getSortStringValue() {
    	if (LavenderePdaConfig.isUsaPoliticaBonificacao() && pedido.sortByTipoItemPedido) {
	    	return this.flTipoItemPedido + nuSeqItemPedido;
	    }
	    if ((LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() || ProdutoBase.SORT_COLUMN_DSPRODUTO.equalsIgnoreCase(sortAttr)) && produto != null) {
	    	return (ValueUtil.isEmpty(produto.toString())) ? StringUtil.getStringValue(this.dsProduto) : produto.toString();
    	}
	    if (pedido.isGondola() && DS_COLUNA_QTITEMGONDOLA.equals(sortAttr) && produto != null) {
		    return produto.dsProduto;
	    }
	    if (LavenderePdaConfig.isExibeComboMenuInferior() && DS_COLUNA_CDCOMBO.equals(sortAttr)) {
	    	return StringUtil.getStringValue(cdCombo);
	    }
    	return super.getSortStringValue();
    }
    
    public boolean fazParteDaGradePreco(ItemPedido itemPedido) {
    	return
            ValueUtil.valueEquals(cdEmpresa, itemPedido.cdEmpresa) &&
            ValueUtil.valueEquals(cdRepresentante, itemPedido.cdRepresentante) &&
            ValueUtil.valueEquals(flOrigemPedido, itemPedido.flOrigemPedido) &&
            ValueUtil.valueEquals(nuPedido, itemPedido.nuPedido) &&
            ValueUtil.valueEquals(cdProduto, itemPedido.cdProduto) &&
            ValueUtil.valueEquals(flTipoItemPedido, itemPedido.flTipoItemPedido) &&
            ValueUtil.valueEquals(cdItemGrade1, itemPedido.cdItemGrade1);
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemPedido) {
            ItemPedido itemPedido = (ItemPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemPedido.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, itemPedido.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, itemPedido.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, itemPedido.nuPedido) &&
                ValueUtil.valueEquals(cdProduto, itemPedido.cdProduto) &&
                ValueUtil.valueEquals(flTipoItemPedido, itemPedido.flTipoItemPedido) &&
                (nuSeqProduto == itemPedido.nuSeqProduto || itemPedido.naoComparaSeqItem || this.naoComparaSeqItem);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
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
        return strBuffer.toString();
    }

    public String getDsProduto() throws SQLException {
    	return getDsProduto(null);
    }

    public String getDsProduto(ItemPedido itemPedido) throws SQLException {
    	return ProdutoService.getInstance().getDsProduto(getProduto(), itemPedido);
    }

    public String getDsProdutoWithKey() throws SQLException {
    	return getDsProdutoWithKey(null);
    }
    public String getDsProdutoWithKey(ItemPedido itemPedido) throws SQLException {
    	if (produto != null && ValueUtil.isEmpty(produto.dsProduto)) {
    		getProduto(true);
    	}
    	return ProdutoService.getInstance().getDescriptionWithId(produto, this.cdProduto, itemPedido);
    }

    public boolean isEditandoVlItemIpi() {
    	return ITEMPEDIDO_EDITANDO_VLITEMIPI == flTipoEdicao;
    }
    
    public boolean isEditandoDescontoPct() {
    	return ITEMPEDIDO_EDITANDO_DESCONTOPCT == flTipoEdicao;
    }

    public boolean isEditandoAcrescimoPct() {
    	return ITEMPEDIDO_EDITANDO_ACRESCIMOPCT == flTipoEdicao;
    }

    public boolean isEditandoValorItem() {
    	return ITEMPEDIDO_EDITANDO_VLITEM == flTipoEdicao;
    }

    public boolean isEditandoValorElementar() {
    	return ITEMPEDIDO_EDITANDO_VLELEMENTAR == flTipoEdicao;
    }

    public boolean isEditandoQtd() {
    	return (ITEMPEDIDO_EDITANDO_QTD == flTipoEdicao) || (ITEMPEDIDO_EDITANDO_QTD_FATURAMENTO == flTipoEdicao);
    }
    
    public boolean isEditandoDescontoST() {
    	return ITEMPEDIDO_EDITANDO_VLDESCONTOST == flTipoEdicao;
    }
    
    public boolean isEditandoValorItemST() {
    	return ITEMPEDIDO_EDITANDO_VLITEMST == flTipoEdicao;
    }
    
    public double getQtItemLista() {
    	if (LavenderePdaConfig.usaConversaoUnidadesMedida && LavenderePdaConfig.ocultaQtItemFisico) {
    		return LavenderePdaConfig.isUsaQtdItemPedidoFaturamentoInteiro() ? (int) qtItemFaturamento : qtItemFaturamento;
    	} else {
    		return LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? (int) getQtItemFisico() : getQtItemFisico();
    	}
    }

    public ItemTabelaPreco getItemTabelaPreco() throws SQLException {
    	return getItemTabelaPreco(null);
    }
    
    public ItemTabelaPreco reloadItemTabelaPreco() throws SQLException {
    	itemTabelaPreco = null;
    	return getItemTabelaPreco(null);
    }
    
    public ItemTabelaPreco getItemTabelaPreco(String cdPrazoPagtoPrecoParam) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdPrazoPagtoPrecoParam)) {
    		cdPrazoPagtoPreco = ValueUtil.getIntegerValue(cdPrazoPagtoPrecoParam);
    	}
    	if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
    		String cdUnidadePreco = (LavenderePdaConfig.usaUnidadeAlternativa) ? StringUtil.getStringValue(cdUnidade) : ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
			if ((!ValueUtil.isEmpty(cdTabelaPreco)) && ((itemTabelaPreco == null) || (!cdTabelaPreco.equals(itemTabelaPreco.cdTabelaPreco)) || (cdProduto != null && !cdProduto.equals(itemTabelaPreco.cdProduto)) || (!cdUnidadePreco.equals(itemTabelaPreco.cdUnidade) || usandoPrecoUnidadePadraoConvertida) || (ValueUtil.getIntegerValue(getQtItemFisico()) != itemTabelaPreco.qtItemFisico))) {
				itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUfClientePedido, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdUnidadePreco, ValueUtil.getIntegerValueTruncated(getQtItemFisico() > 0 && getQtItemFisico() < 0.5 ? 1 : getQtItemFisico()), cdPrazoPagtoPreco);
				usandoPrecoUnidadePadraoConvertida = false;
    			Produto produto = getProduto();
    			if (LavenderePdaConfig.usaUnidadeAlternativa && produto != null && ValueUtil.isEmpty(itemTabelaPreco.cdProduto) && ValueUtil.isNotEmpty(produto.cdUnidade) && !isCdUnidadeIgualCdUnidadeProduto()) {
    				itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cdTabelaPreco, cdProduto, cdUfClientePedido, cdItemGrade1, cdItemGrade2, cdItemGrade3, produto.cdUnidade, ValueUtil.getIntegerValueTruncated(getQtItemFisico() > 0 && getQtItemFisico() < 0.5 ? 1 : getQtItemFisico()), cdPrazoPagtoPreco);
    				usandoPrecoUnidadePadraoConvertida = ValueUtil.isNotEmpty(itemTabelaPreco.cdProduto);
    			}
    		}
    	} else {
    		String cdItemGrade1 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : this.cdItemGrade1;
    		String cdItemGrade2 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : this.cdItemGrade2;
    		String cdItemGrade3 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : this.cdItemGrade3;
    		if (cdProduto != null && ((ValueUtil.isNotEmpty(cdTabelaPreco)) && ((itemTabelaPreco == null) || (!cdTabelaPreco.equals(itemTabelaPreco.cdTabelaPreco)) || (!cdProduto.equals(itemTabelaPreco.cdProduto)) || (ValueUtil.isNotEmpty(cdItemGrade1) && verificaCdItemGrade1DiferenteItemTabelaPreco())))) {
    			itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(cdTabelaPreco, cdProduto, cdUfClientePedido, cdItemGrade1, cdItemGrade2, cdItemGrade3);
    		}
    	}
    	if (itemTabelaPreco == null && produto != null && pedido != null && ValueUtil.isNotEmpty(cdTabelaPreco)) {
    		itemTabelaPreco = ItemTabelaPrecoService.getInstance().getTabelaPrecoFilter(cdTabelaPreco, produto.cdProduto, pedido.getCliente().dsUfPreco, cdPrazoPagtoPrecoParam, cdPrazoPagtoPreco, 0);
    		if (itemTabelaPreco.cdEmpresa != null && itemTabelaPreco.cdRepresentante != null && itemTabelaPreco.cdTabelaPreco != null && itemTabelaPreco.cdUf != null && itemTabelaPreco.cdProduto != null && itemTabelaPreco.cdUnidade != null) {
    			itemTabelaPreco = (ItemTabelaPreco) ItemTabelaPrecoService.getInstance().findByPrimaryKey(itemTabelaPreco);
    		}
    	}
    	if (itemTabelaPreco != null && itemTabelaPreco.isChaveVazia()) {
			populaDadosItemTabelaPreco();
	    }
    	return itemTabelaPreco;
    }

    private void populaDadosItemTabelaPreco() {
		itemTabelaPreco.cdEmpresa = cdEmpresa == null ? pedido.cdEmpresa : cdEmpresa;
		itemTabelaPreco.cdRepresentante = cdRepresentante == null ? pedido.cdRepresentante : cdRepresentante;
		itemTabelaPreco.cdTabelaPreco = cdTabelaPreco;
		itemTabelaPreco.cdProduto = cdProduto;
		itemTabelaPreco.cdUf = cdUfClientePedido;
		itemTabelaPreco.cdItemGrade1 = cdItemGrade1 == null ? "0" : cdItemGrade1;
		itemTabelaPreco.cdItemGrade2 = cdItemGrade2 == null ? "0" : cdItemGrade2;
		itemTabelaPreco.cdItemGrade3 = cdItemGrade3 == null ? "0" : cdItemGrade3;
		itemTabelaPreco.cdUnidade = cdUnidade == null ? "0" : cdUnidade;
    }

	private boolean verificaCdItemGrade1DiferenteItemTabelaPreco() {
		return (LavenderePdaConfig.isUsaGradeProduto1A4() || LavenderePdaConfig.usaGradeProduto5()) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equalsIgnoreCase(itemTabelaPreco.cdItemGrade1) 
				&& (LavenderePdaConfig.getConfigGradeProduto() != 2 && !ValueUtil.valueEquals(cdItemGrade1, itemTabelaPreco.cdItemGrade1) 
						|| LavenderePdaConfig.usaGradeProduto2() && !ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO.equals(itemTabelaPreco.cdItemGrade1));
	}
	
	public ProdutoUnidade getProdutoUnidade() throws SQLException {
		return getProdutoUnidade(false);
	}

	public ProdutoUnidade getProdutoUnidade(final boolean forceLoadUnAlternativaInterpolacaoDesc) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa || forceLoadUnAlternativaInterpolacaoDesc) {
			if (isLoadedProdutoUnidade && produtoUnidade == null) {
				return null;
			}
			if (produtoUnidade == null || (ValueUtil.isNotEmpty(cdUnidade) && (!cdUnidade.equals(produtoUnidade.cdUnidade) || (produtoUnidade.cdProduto != null && !produtoUnidade.cdProduto.equals(cdProduto))))) {
				setProdutoUnidade(ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(this));
			}
			if (produtoUnidade == null) {
				produtoUnidade = new ProdutoUnidade();
				produtoUnidade.nuConversaoUnidade = 1;
				produtoUnidade.vlIndiceFinanceiro = 1;
				produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
				produtoUnidade.cdUnidade = cdUnidade;
				produtoUnidade.cdProduto = cdProduto;
			} else {
				if (produtoUnidade.vlIndiceFinanceiro == 0) {
					produtoUnidade.vlIndiceFinanceiro = 1;
				}
				if (produtoUnidade.nuConversaoUnidade == 0) {
					produtoUnidade.nuConversaoUnidade = 1;
				}
			}
		}
		isLoadedProdutoUnidade = true;
		return produtoUnidade;
	}

    public void setProdutoUnidade(ProdutoUnidade produtoUnidade) {
    	this.produtoUnidade = produtoUnidade;
    }

    public double getQtUnidadeEmbalagemElementar() throws SQLException {
    	if (LavenderePdaConfig.usaUnidadeAlternativa) {
    		if (qtEmbalagemElementar == 0) {
    			return 0;
    		} else if (getProdutoUnidade().isMultiplica()) {
    			return ValueUtil.round(getQtItemFisico() * qtEmbalagemElementar);
    		} else {
    			return ValueUtil.round(getQtItemFisico() / qtEmbalagemElementar);
    		}
    	}
    	return getQtItemFisico();
    }
    
	public String getQtEmbalagemElementarToInterface() {
		boolean naoUsaQtdFracionada = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() && LavenderePdaConfig.isSemQuantidadeFracionada(Produto.CDCAMPO_QT_EMBALAGEM_ELEMENTAR);
		return StringUtil.getStringValueToInterface(this.qtEmbalagemElementar, naoUsaQtdFracionada ? 0 : ValueUtil.doublePrecisionInterface);
	}

    public boolean possuiItemTabelaPreco() throws SQLException {
    	ItemTabelaPreco itemTabPreco = getItemTabelaPreco();
    	return (itemTabPreco != null) && !(ValueUtil.isEmpty(itemTabPreco.cdProduto) && ValueUtil.isEmpty(itemTabPreco.cdTabelaPreco));
    }

    public void setItemTabelaPreco(ItemTabelaPreco itemTabelaPreco) {
    	this.itemTabelaPreco = itemTabelaPreco;
    }

    public ItemTabelaPreco getItemTabelaPrecoAtual() {
    	return itemTabelaPreco;
    }

    public Vector getVerbaTabelaPrecoList() throws SQLException {
    	if (!ValueUtil.isEmpty(cdTabelaPreco)) {
    		VerbaTabelaPreco verbaPreco = null;
    		if ((verbaTabelaPrecoList != null) && (verbaTabelaPrecoList.size() > 0)) {
    			verbaPreco = (VerbaTabelaPreco)verbaTabelaPrecoList.items[0];
    		}
    		if ((verbaTabelaPrecoList == null) || (verbaTabelaPrecoList.size() == 0) || ((verbaPreco != null) && (!verbaPreco.cdTabelaPreco.equals(cdTabelaPreco)))) {
    	    	Vector verbas = VerbaService.getInstance().findAll();
    	    	if (verbas.size() == 1) {
        			verbaTabelaPrecoList = new Vector();
        			VerbaTabelaPreco verbaTabPreco = VerbaTabelaPrecoService.getInstance().getVerbaTabelaPreco(cdTabelaPreco, cdEmpresa, cdRepresentante, ((Verba)verbas.items[0]).cdVerba);
        			if (verbaTabPreco != null) {
        				verbaTabelaPrecoList.addElement(verbaTabPreco);
        			}
        			verbas = null;
    	    	} else {
        			verbaTabelaPrecoList = VerbaTabelaPrecoService.getInstance().getVerbaTabelaPrecoList(cdTabelaPreco, cdEmpresa, cdRepresentante);
    	    	}
    		}
    	}
    	return verbaTabelaPrecoList;
    }

	public double getVlPctRentabilidade(boolean ignoreProdutoNeutro) throws SQLException {
		if ((!ignoreProdutoNeutro && getProduto().isNeutro()) || isFazParteKitFechado()) {
			return 0;
		} else if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			return getVlPctRentabilidadeByConfigRentabilidadeNoPedido(ignoreProdutoNeutro);
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			return vlPctMargemRentab;
		}
		return 0d;
	}

	public double getVlPctRentabilidadeByConfigRentabilidadeNoPedido(boolean ignoreProdutoNeutro) throws SQLException {
    	if ((!ignoreProdutoNeutro && getProduto().isNeutro()) || isFazParteKitFechado()) {
    		return 0;
    	}
    	RentabilidadeFaixaService.getInstance().calculaRentabilidadeItemPedidoRetornado(this);
    	double vlBaseCalculoRentabilidade = 0;
    	switch (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido) {
    	case 1:
    		vlBaseCalculoRentabilidade = ValueUtil.round(this.vlBaseItemPedido * getQtItemFisico());
    		break;
    	case 2:
    		vlBaseCalculoRentabilidade = ValueUtil.round(this.vlItemPedido * getQtItemFisico());
    		break;
    	case 3:
    		vlBaseCalculoRentabilidade = ValueUtil.round(getVlTotalItemComTributos());
    		break;
    	case 4:
    		vlBaseCalculoRentabilidade =  ItemPedidoService.getInstance().getVlPrecoTotalCustoItem(pedido, this);
    		break;
    	case 8:
    		vlBaseCalculoRentabilidade = vlTotalItemPedido;
    	default:
    		if (LavenderePdaConfig.usaValorRentabilidadeSemCalculo()) {
    			return vlRentabilidade;
    		}
    	}
    	if (vlBaseCalculoRentabilidade > 0 ) {
    		return (vlRentabilidade / vlBaseCalculoRentabilidade) * 100;
    	}
    	return 0;
    }

	public double getVlPctRentabilidadeLiquida() throws SQLException {
    	return getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) - vlPctComissao;
    }
    
    public boolean isFlPrecoLiberadoSenha() {
    	return ValueUtil.VALOR_SIM.equals(flPrecoLiberadoSenha);
    }

    public boolean isItemBonificacao() {
    	return TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(flTipoItemPedido);
    }
    
    public boolean isItemVendaNormal() {
    	return TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(flTipoItemPedido);
    }
    
    public boolean isItemTroca() {
    	return (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(flTipoItemPedido)) || (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(flTipoItemPedido));
    }

    public boolean isItemTrocaRecolher() {
    	return TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(flTipoItemPedido);
    }

    public boolean isOportunidade() throws SQLException {
		return ValueUtil.VALOR_SIM.equals(flOportunidade) && (pedido == null || !pedido.isPedidoBonificacao());
	}

	public Produto getProduto() throws SQLException {
		return getProduto(false);
	}

    public Produto getProduto(boolean forced) throws SQLException {
    	if (isObjetoProdutoVazio() || forced) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto, LavenderePdaConfig.apresentaMarcadorProdutoInseridos);
    	}
    	return produto;
    }

	public boolean isObjetoProdutoVazio() {
		return ValueUtil.isNotEmpty(cdProduto) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)));
	}

	public boolean isProdutoVazio() {
    	return produto == null;
    }
    
    public Produto getProdutoComTributacao(boolean fromSugestaoPedido) throws SQLException {
    	if ((!ValueUtil.isEmpty(cdProduto)) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
    		produto = new Produto();
    		produto.cdProduto = cdProduto;
    		produto.estoque = new Estoque();
    		produto.estoque.cdLocalEstoque = getCdLocalEstoque();
    		produto = ProdutoService.getInstance().getProdutoComTributacao
    							(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, produto, fromSugestaoPedido, pedido.getCliente().cdTributacaoCliente,
    									pedido.getCliente().cdTributacaoCliente2, pedido.getCliente().cdCliente, pedido.cdTipoPedido, 
    									ValueUtil.isNotEmpty(pedido.getCliente().cdEstadoComercial) ? pedido.getCliente().cdEstadoComercial : ValueUtil.VALOR_ZERO
    							);
    	}
    	return produto;
    }

    public void setProduto(Produto produto) {
    	this.produto = produto;
    	cdProduto = null;
    	if (produto != null) {
    		cdProduto = produto.cdProduto;
    	}
    }
    
    public void setProdutoBase(ProdutoBase produtoBase) {
    	this.produtoBase = produtoBase;
    }
    
    public ProdutoBase getProdutoBase() {
    	return produtoBase;
    }

    public void setPedido(Pedido pedido) {
    	this.pedido = pedido;
    	nuPedido = null;
    	if (pedido != null) {
    		nuPedido = pedido.nuPedido;
    	}
    }
    public Tributacao getTributacaoItem() throws SQLException {
    	if (pedido.getCliente() == null) {
    		return null;
    	}
    	return getTributacaoItem(pedido.getCliente());
    }

    public Tributacao getTributacaoItem(Cliente cliente) throws SQLException {
    	if (tributacao == null || ValueUtil.isEmpty(tributacao.cdTributacaoProduto) || !ValueUtil.valueEquals(tributacao.cdTributacaoProduto, getProduto().cdTributacaoProduto) || !tributacao.cdTributacaoCliente.equals(getCdTributacaoCliente())) {
    		if (ValueUtil.isNotEmpty(getProduto().cdTributacaoProduto) && ValueUtil.isNotEmpty(cliente.cdTributacaoCliente)) {
    			tributacao = TributacaoService.getInstance().getTributacao(cliente, getProduto().cdTributacaoProduto, pedido.getTipoPedido(), pedido.getCliente().cdEstadoComercial, tributacaoConfig);
    			if (tributacao != null) {
    				usaCdTributacaoCliente2 = tributacao.cdTributacaoCliente.equals(pedido.getCliente().cdTributacaoCliente2);
    			}
    		} else {
    			tributacao = null;
    		}
    	}
    	return tributacao;
    }
    
	private String getCdTributacaoCliente() throws SQLException {
		if (usaCdTributacaoCliente2) {
			return pedido.getCliente().cdTributacaoCliente2;
		}
		return pedido.getCliente().cdTributacaoCliente;
	}
    
    public TributacaoConfig getTributacaoConfigItem(boolean tribConfigUpdated) throws SQLException {
    	if (tributacaoConfig == null || isEditandoValorItemST() || tribConfigUpdated ) {
	    	tributacaoConfig = TributacaoConfigService.getInstance().getTributacaoConfig(pedido.getCliente(), getProduto(), pedido.getTipoPedido());
    	}
    	cdTributacaoConfig = tributacaoConfig != null ? tributacaoConfig.cdTributacaoConfig : null;
    	return tributacaoConfig;
    }
    
    public TributacaoConfig getTributacaoConfigItem() throws SQLException {
    	return getTributacaoConfigItem(false);
    }

    public TributacaoVlBase getVlBaseTributacaoItem() throws SQLException {
    	if (ValueUtil.isNotEmpty(cdProduto) && tributacao != null && ((tributacaoVlBase == null) || (!cdProduto.equals(tributacaoVlBase.cdProduto)))) {
    		tributacaoVlBase = TributacaoVlBaseService.getInstance().getTributacaoVlBase(tributacao.cdTributacaoCliente, tributacao.cdTributacaoProduto, cdProduto, pedido.getCliente().cdEstadoComercial);
    	}
    	if (tributacaoVlBase == null) {
			tributacaoVlBase = new TributacaoVlBase();
			tributacaoVlBase.cdProduto = cdProduto;
		}
    	return tributacaoVlBase;
    }

	public void loadDescPromocional(final boolean reloadDescPromocional) throws SQLException {
		isLoadedDescPromocional = descPromocional != null && isLoadedDescPromocional;
		if (reloadDescPromocional || (!isLoadedDescPromocional && !DescPromocionalService.getInstance().isDescPromocionalEncontrado(this))) {
			DescPromocionalService.getInstance().loadDescPromocional(this);
			if (produtoBase != null) {
				produtoBase.descPromocionalCarregado = true;
			}
			if (LavenderePdaConfig.usaDescontoQtdeNoGrupoDescPromocional && descPromocionalComQtdList.size() > 0) {
				SortUtil.qsortInt(descPromocionalComQtdList.items, 0, descPromocionalComQtdList.size() - 1, true);
				DescPromocionalService.getInstance().loadMaiorFaixaDescPromocionalPorQuantidadeItemPedido(this);
			}
			isLoadedDescPromocional = true;
		}
	}
	
	public DescPromocional getDescPromocional() throws SQLException {
		boolean reloadDescPromocional = LavenderePdaConfig.habilitaRegrasAdicionaisDescPromocional() && pedido != null && (pedido.cdCondicaoPagamentoChanged || pedido.cdCondicaoComercialChanged || pedido.isTipoFreteChanged);
		loadDescPromocional(reloadDescPromocional);
		return descPromocional;
	}
	 
	public Vector getDescPromocionalComQtdList() throws SQLException {
		loadDescPromocional(false);
		return descPromocionalComQtdList;
	}
    
    public boolean isIndiceFinanceiroEspecialCliente() throws SQLException {
    	boolean especial = false;
    	if (LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
    		String flIndiceEspecial = ValueUtil.VALOR_NI;
        	if (produto == null) {
        		Produto prod = new Produto();
        		prod.cdEmpresa = SessionLavenderePda.cdEmpresa;
        		prod.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
        		prod.cdProduto = cdProduto;
        		flIndiceEspecial = StringUtil.getStringValue(ProdutoService.getInstance().findColumnByRowKey(prod.getRowKey(), "FLINDICEFINANCEIROESPECIAL"));
        		prod = null;
        	} else {
        		flIndiceEspecial = produto.flIndiceFinanceiroEspecial;
        	}
    		especial = ValueUtil.VALOR_SIM.equals(flIndiceEspecial);
    	}
    	return especial;
    }

    public double getQtItemEstoquePositivo() throws SQLException {
    	return getQtItemEstoquePositivo(EstoqueService.getInstance().getQtEstoqueErpPda(this, this.getCdLocalEstoque()));
    }
    public double getQtItemEstoquePositivo(double qtEstoque) {
    	if (qtEstoque > 0) {
			if (getQtItemFisico() <= qtEstoque) {
				return getQtItemFisico();
			} else {
				return qtEstoque;
			}
    	}
    	return 0;
    }

    public double getVlEfetivoTotal() throws SQLException {
		return getVlEfetivoTotal(getQtItemEstoquePositivo());
    }

    public double getVlEfetivoTotal(double qtEstoque) {
    	return vlItemPedido * getQtItemEstoquePositivo(qtEstoque);
	}

    public double getVlEfetivoTotalComSt() throws SQLException {
    	return (vlItemPedido + vlSt) * getQtItemEstoquePositivo();
    }

    public double getVlTotalComST() {
		return LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? getVlTotalItem() + getVlTotalST() : getVlItemComSt() * getQtItemFisico();
    }

    public double getVlTotalST() {
    	return LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? vlTotalStItem : getQtItemFisico() * vlSt;
    }
    
    public double getVlTotalSTItem() {
    	double qtd = getQtItemFisico() > 0 ? getQtItemFisico() : 1;
    	return ValueUtil.round(vlTotalStItem / qtd, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
    }

    public double getVlTotalItem() {
    	return vlItemPedido * getQtItemFisico();
    }
    
    public double getVlTotalItemBruto() {
    	return vlBaseItemTabelaPreco * getQtItemFisico();
    }

    public double getVlItemComSt() {
    	return vlItemPedido + (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? getVlTotalSTItem() : vlSt);
    }

    public double getVlItemComIpi() {
    	return vlItemPedido + vlIpiItem;
    }

    public double getVlTotalIpi() {
    	return  LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? vlTotalIpiItem : getQtItemFisico() * vlIpiItem;
    }
    
    public double getVlTotalIpiItem() {
    	double qtd = getQtItemFisico() > 0 ? getQtItemFisico() : 1;
    	return ValueUtil.round(vlTotalIpiItem / qtd, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
    }

    public double getVlTotalItemComIpi() {
    	return (vlItemPedido + vlIpiItem) * getQtItemFisico();
    }
    
    public double getVlTotalSeguro() {
    	return vlSeguroItemPedido * getQtItemFisico();
    }
    
    
    public double getVlTotalItemPedidoComSeguro(boolean aplicaIndiceCondPagto, double vlIndice) {
    	return getVlTotalItemPedidoCalcTributos(aplicaIndiceCondPagto, vlIndice) + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : getVlTotalSeguro());
    }
    
    private double getVlTotalItemPedidoCalcTributos(boolean aplicaIndiceCondPagto, double vlIndice) {
		if (vlIndice == 0) {
			vlIndice = 1;
		}
		try {
			if (tributacaoConfig!=null && tributacaoConfig.isCalculaEDebitaPisCofins() && this.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
				return vlTotalItemPedido;
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		return aplicaIndiceCondPagto ? vlTotalItemPedido * vlIndice : vlTotalItemPedido;
	}

    public double getVlTributos()  {
    	if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
    		return vlTotalStItem + vlTotalIpiItem + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : getVlTotalSeguro() + (LavenderePdaConfig.calculaFecopItemPedido ? getVlFecop() : 0));
    	}
    	double vlTributos = vlSt + vlIpiItem;
    	if (LavenderePdaConfig.calculaFecopItemPedido) {
    		vlTributos += getVlFecop();
    	}
    	return vlTributos + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : getVlTotalSeguro());
    }
    
    public double getVlDeducoes() throws SQLException {
    	double vlDeducoes = 0;
    	if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
    		if (getProduto().isDebitaPisCofinsZonaFranca()) {
    			vlDeducoes += vlTotalIcmsItem;
    		}
    		if (getTributacaoConfigItem() != null && tributacaoConfig.isCalculaEDebitaPisCofins()) {
    			vlDeducoes += vlTotalPisItem + vlTotalCofinsItem;
    		}
    	}
    	return vlDeducoes;
    }
    
    public double getVlAdicionais() throws SQLException {
    	double vlAdicionais = vlDespesaAcessoria;
    	if (!(LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco && (!LavenderePdaConfig.usaFreteApenasTipoFob || pedido.isTipoFreteFob()))) {
    		vlAdicionais += vlItemPedidoFrete; 
    	}
    	return vlAdicionais;
    }
    
    public double getVlItemComFrete() {
    	return vlItemPedido + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : vlItemPedidoFrete);
    }
    
    public double getVlTotalFrete() {
    	if (LavenderePdaConfig.usaTransportadoraPedido() && LavenderePdaConfig.usaFretePedidoPorTranspTipoPedProd) {
    		return vlFrete;
    	} else if (LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
    		return vlTotalItemPedidoFrete;
    	} else {
    		return getQtItemFisico() * vlItemPedidoFrete;
    	}
    }
    
    public double getVlTotalFreteItem() {
    	double qtd = getQtItemFisico() > 0 ? getQtItemFisico() : 1;
    	return ValueUtil.round(getVlTotalFrete() / qtd);
    }
    
    public double getVlTributosComVlAdicionais() throws SQLException {
    	return (!TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(flTipoItemPedido)) ? getVlTributos() + getVlAdicionais() : getVlAdicionais();
    }
    
    public double getVlTotalTributos() {
    	return getQtItemFisico() * getVlTributos();
    }

    public double getVlItemComTributos() {
    	if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && getQtItemFisico() > 0) {
			return vlItemPedido + (getVlTributos() / getQtItemFisico());
		}
    	return vlItemPedido + getVlTributos();
    }

    public double getVlTotalItemComTributos() {
    	double vlItemPedido = LavenderePdaConfig.descontaIcmsRentabilidade ? (this.vlItemPedido - getVlTotalIcms()) : this.vlItemPedido;
    	if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
    		return (vlItemPedido * getQtItemFisico()) + getVlTributos();
    	} else {
    		return (vlItemPedido + getVlTributos()) * getQtItemFisico();
    	}
    }
    
	public double getVlTotalPis() throws SQLException {
		double vlPctPis = pedido.getEmpresa().vlPctPis;
		if (vlPctPis == 0) {
			Tributacao tributacao = getTributacaoItem(pedido.getCliente());
			vlPctPis = tributacao != null ? tributacao.vlPctPis : 0;
			if (vlPctPis == 0) {
				vlPctPis = getProduto().vlPctPis;
			}
		}
		return getVlTotalItem() * (vlPctPis / 100);
	}
    
	public double getVlTotalCofins() throws SQLException {
		double vlPctCofins = pedido.getEmpresa().vlPctCofins;
		if (vlPctCofins == 0) {
			Tributacao tributacao = getTributacaoItem(pedido.getCliente());
			vlPctCofins = tributacao != null ? tributacao.vlPctCofins : 0;
			if (vlPctCofins == 0) {
				vlPctCofins = getProduto().vlPctCofins;
			}
		}
		return getVlTotalItem() * (vlPctCofins / 100);
	}
    
    public double getVlTotalIrpj() throws SQLException {
    	double vlPctIrpj = pedido.getEmpresa().vlPctIrpj;
    	return getVlTotalItemComTributos() * (vlPctIrpj / 100);
    }
    
    public double getVlTotalCustoVariavel() throws SQLException {
    	double vlPctCustoVariavel = pedido.getEmpresa().vlPctCustoVariavel;
    	return getVlTotalItemComTributos() * (vlPctCustoVariavel / 100);
    }
    
    public double getVlTotalCustoFixo() throws SQLException {
    	double vlPctCustoFixo = pedido.getEmpresa().vlPctCustoFixo;
    	return getVlTotalItemComTributos() * (vlPctCustoFixo / 100);
    }
    
    public double getVlTotalIcms() {
    	return LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? vlTotalIcmsItem : getQtItemFisico() * vlIcms;
    }
    
    public double getVlGastoTotalVariavel() throws SQLException {
    	double vlGastoTotalVariavel = getVlTotalPis() + getVlTotalCofins() + getVlTotalST() + getVlTotalIpi() + getVlTotalIrpj() + getVlTotalCSLL() + getVlTotalCPP() + getVlTotalCustoVariavel();
    	if (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProduto()) {
    		if (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProdutoSemICMS()) {
    			vlGastoTotalVariavel += getVlTotalCustoVariavelProduto();
    		} else if (LavenderePdaConfig.isUsaCalculoGastoTotalVariavelPorProdutoComICMS()) {
    			vlGastoTotalVariavel += getVlTotalIcms() + getVlTotalCustoVariavelProduto();
    		}
    	} else {
			vlGastoTotalVariavel += getVlTotalIcms();
		}
		return vlGastoTotalVariavel;
    }
    
    public String getCdProdutoOrDsReferenciaForFotoProduto() {
    	return produto.getCdProdutoOrDsReferenciaForFotoProduto();
    }
    
	public boolean hasFoto(boolean isApresentaFotoGrade) throws SQLException {
		int fotoProdutoCount = 0;
		if (isApresentaFotoGrade && LavenderePdaConfig.isUsaFotoProdutoGrade() && LavenderePdaConfig.usaGradeProduto5() && getProduto().isProdutoAgrupadorGrade()) {
			FotoProdutoGrade filter = new FotoProdutoGrade();
			filter.dsAgrupadorGrade = getProduto().getDsAgrupadorGrade();
			fotoProdutoCount = FotoProdutoGradeService.getInstance().countByExample(filter);
		} else if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			FotoProdutoEmp filter = new FotoProdutoEmp();
			filter.cdEmpresa = cdEmpresa;
			if (isApresentaFotoGrade && LavenderePdaConfig.usaGradeProduto5() && getProduto().isProdutoAgrupadorGrade()) {
				filter.dsAgrupadorGrade = getProduto().getDsAgrupadorGrade();
				fotoProdutoCount = FotoProdutoEmpService.getInstance().countFotoProdutoByAgrupadorGrade(filter);
			} else {
				filter.cdProduto = cdProduto;
				fotoProdutoCount = FotoProdutoEmpService.getInstance().countByExample(filter);
			}
		} else {
			FotoProduto fotoProdutoFilter = new FotoProduto();
			if (isApresentaFotoGrade && LavenderePdaConfig.usaGradeProduto5() && getProduto().isProdutoAgrupadorGrade()) {
				fotoProdutoFilter.dsAgrupadorGrade = getProduto().getDsAgrupadorGrade();
				fotoProdutoCount = FotoProdutoService.getInstance().countFotoProdutoByAgrupadorGrade(fotoProdutoFilter);
			} else {
				fotoProdutoFilter.cdProduto = cdProduto;
				fotoProdutoCount = FotoProdutoService.getInstance().countByExample(fotoProdutoFilter);
			}
		}
		return fotoProdutoCount != 0;
	}

	public ItemTabelaPreco getItemTabelaPrecoOld() {
		return itemTabelaPrecoOld;
	}

	public void setItemTabelaPrecoOld(ItemTabelaPreco itemTabelaPrecoOld) {
		this.itemTabelaPrecoOld = itemTabelaPrecoOld;
	}

	public boolean isItemPermiteOportunidade() throws SQLException {
		return getItemTabelaPreco() != null && getItemTabelaPreco().vlOportunidade > 0;
		
	}
	
	public TipoFreteTabPreco getTipoFreteTabPreco() throws SQLException {
		TipoFrete tipoFrete = pedido.getTipoFrete();
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if (tipoFrete == null || tipoPedido == null || tipoPedido.isIgnoraCalculoFrete()) return null;
		String cdTipoFrete = tipoFrete.cdTipoFrete;
		double qtPesoPedido = ValueUtil.round(pedido.qtPeso + ItemPedidoService.getInstance().getPesoItemPedido(this));
		if (pedido.itemPedidoList.contains(this)) {
			qtPesoPedido = ValueUtil.round(qtPesoPedido - ItemPedidoService.getInstance().getPesoItemPedido(this, oldQtItemFisico));
		}
		//--
		if (ValueUtil.isNotEmpty(cdTipoFrete) && ValueUtil.isNotEmpty(cdTabelaPreco) && 
						(tipoFreteTabPreco == null || (!cdProduto.equals(tipoFreteTabPreco.cdProduto)) || 
						(!cdTabelaPreco.equals(tipoFreteTabPreco.cdTabelaPreco)) || 
						(!cdTipoFrete.equals(tipoFreteTabPreco.cdTipoFrete)) ||
						(qtPesoPedido != tipoFreteTabPreco.qtPesoPedido && !LavenderePdaConfig.usaFreteValorUnidade) || 
						(LavenderePdaConfig.usaFreteValorUnidade && !ValueUtil.valueEquals(cdUnidade, tipoFreteTabPreco.cdUnidade)))) {
			tipoFreteTabPreco = TipoFreteTabPrecoService.getInstance().getTipoFreteTabPreco(this, cdTipoFrete, qtPesoPedido);
		}
		return tipoFreteTabPreco;
	}
	
	public TipoFreteTabPreco getTipoFreteTabPrecoPesoZero() throws SQLException {
		TipoFrete tipoFrete = pedido.getTipoFrete();
		if (tipoFrete == null) return null;
		return TipoFreteTabPrecoService.getInstance().getTipoFreteTabPreco(this, tipoFrete.cdTipoFrete);
	}

	public ItemPedidoAud getItemPedidoAud() throws SQLException {
		if (itemPedidoAud == null) {
			itemPedidoAud = ItemPedidoAudService.getInstance().getItemPedidoAud(this);
			if (itemPedidoAud == null) {
				return itemPedidoAud = new ItemPedidoAud();
			}
		}
		return itemPedidoAud;
	}

	public void setItemPedidoAud(ItemPedidoAud itemPedidoAud) {
		this.itemPedidoAud = itemPedidoAud;
	}
	
	public double getVlVerbaManual() {
		double verbaManual = 0;
		if (LavenderePdaConfig.informaVerbaManual) {
			if (vlVerbaManual == 0) {
				if (vlVerbaItem != 0) {
					verbaManual = vlVerbaItem / getQtItemFisico();
				}
			} else {
				verbaManual = vlVerbaManual;
			}
			verbaManual = verbaManual < 0 ? verbaManual * -1 : verbaManual;
		}
		return verbaManual;
	}
	
	public double getVlTotalFreteESeguro() {
		return vlTotalItemPedido + getVlTotalFrete() + getVlTotalSeguro();
	}
	
	public double getVlFreteESeguro() {
		return vlItemPedido + ((getVlTotalFrete() + getVlTotalSeguro()) / (getQtItemFisico() > 0 ? getQtItemFisico() : 1));
	}
	
	public CondicaoComercial getCondicaoComercial() throws SQLException {
		if (!ValueUtil.isEmpty(cdCondicaoComercial) && ((condicaoComercial == null) || (!cdCondicaoComercial.equals(condicaoComercial.cdCondicaoComercial)))) {
			condicaoComercial = CondicaoComercialService.getInstance().getCondicaoComercial(cdCondicaoComercial);
			if (condicaoComercial == null) {
				condicaoComercial = new CondicaoComercial();
				condicaoComercial.cdCondicaoComercial = cdCondicaoComercial;
			}
		}
		return condicaoComercial;
	}
	
	public double getVlAtualParticipacao() {
		if (vlTotalItemPedido > 0 && pedido.vlTotalPedido > 0) {
			return ValueUtil.round((vlTotalItemPedido / pedido.vlTotalPedido) * 100);
		}
		return 0;
	}
	
	public TabelaPreco getTabelaPreco() throws SQLException {
		if (ValueUtil.isNotEmpty(cdTabelaPreco) && ((tabelaPreco == null) || (!cdTabelaPreco.equals(tabelaPreco.cdTabelaPreco)))) {
			tabelaPreco = TabelaPrecoService.getInstance().getTabelaPreco(cdTabelaPreco);
		}
		return tabelaPreco;
	}
	
	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public TabelaPrecoRep getTabelaPrecoRep() throws SQLException {
		if (ValueUtil.isNotEmpty(cdTabelaPreco) && ((tabelaPrecoRep == null) || (!cdTabelaPreco.equals(tabelaPrecoRep.cdTabelaPreco)))) {
			tabelaPrecoRep = TabelaPrecoRepService.getInstance().getTabelaPrecoRep(cdTabelaPreco);
		}
		return tabelaPrecoRep;
	}

	public void setTabelaPrecoRep(TabelaPrecoRep tabelaPrecoRep) {
		this.tabelaPrecoRep = tabelaPrecoRep;
	}

	public String getCdLocalEstoque() throws SQLException {
		String local = getCdLocalEstoquePadrao();
		if (local == null && pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			Empresa empresa = new Empresa();
			empresa.cdEmpresa = pedido.cdEmpresa;
			local = EmpresaService.getInstance().getLocalEstoqueEmpresa();
		}
		return local;
	}
	
	private String getCdLocalEstoquePadrao() throws SQLException {
		if ((LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() || LavenderePdaConfig.permiteCondComercialItemDiferentePedido) && LavenderePdaConfig.usaLocalEstoquePorTabelaPreco() && getTabelaPreco() != null) {
			return getTabelaPreco().cdLocalEstoque;
		}
		return pedido.getCdLocalEstoque();
	}
	
	private boolean isOrdenaEstoque() {
		return LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto() && ProdutoBase.SORT_COLUMN_QTESTOQUE.equalsIgnoreCase(ItemPedido.sortAttr);
	}
	
	private double getEstoqueItem() {
		try {
			return EstoqueService.getInstance().getQtEstoque(this.cdProduto, getCdLocalEstoque());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0d;
	}
	
	public String getCdTabelaPreco() throws SQLException {
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			return getTabelaPreco() != null ? getTabelaPreco().cdTabelaPreco : null;
		} else {
			return pedido.cdTabelaPreco;
		}
	}
	
	public double getVlCreditoCondicao() throws SQLException {
		return vlCreditoCondicao * (getQtItemFisico() / getProduto().nuConversaoUnidadesMedida);
	}
	
	public double getVlCreditoFrete() throws SQLException {
		return vlCreditoFrete * (getQtItemFisico() / getProduto().nuConversaoUnidadesMedida);
	}

	public double getVlTotalItemPedidoFrete() {
		return LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : vlTotalItemPedidoFrete;
	}

	public boolean isPendente() {
		return ValueUtil.getBooleanValue(flPendente);
	}
	
	public boolean isRestrito() {
		return ValueUtil.getBooleanValue(flRestrito);
	}
	
	public boolean isIgnoraControleVerba() throws SQLException {
		return (LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco && getTabelaPreco() != null && getTabelaPreco().isAplicaDescQtdeAuto())
				|| isKitTipo3()
				|| hasDescProgressivo();
	}
	
	public boolean isTabelaPrecoOldIgnoraControleVerba() throws SQLException {
		return LavenderePdaConfig.gravaCodigoVerbaItemPedidoPorTabelaPreco && getTabelaPrecoOld() != null && getTabelaPreco().isAplicaDescQtdeAuto();
	}

	public TabelaPreco getTabelaPrecoOld() throws SQLException {
		if (ValueUtil.isNotEmpty(cdTabelaPrecoOld) && ((tabelaPrecoOld == null) || (!cdTabelaPrecoOld.equals(tabelaPrecoOld.cdTabelaPreco)))) {
			tabelaPrecoOld = TabelaPrecoService.getInstance().getTabelaPreco(cdTabelaPrecoOld);
			tabelaPrecoOld = ValueUtil.isEmpty(tabelaPrecoOld.cdTabelaPreco) ? null : tabelaPrecoOld;
		}
		return tabelaPrecoOld;
	}
	public double getQtItemFisicoAtualizaEstoque() {
		return oldQtItemFisico == 0 ? oldQtItemFisico = getQtItemFisico() : oldQtItemFisico;
	}

	public void setOldQtItemFisico(double oldQtItemFisico) {
		this.oldQtItemFisico = oldQtItemFisico;
	}
	
	public double getOldQtItemFisico() {
		return oldQtItemFisico;
	}
	
	public ItemPedido getItemPedidoUltimoPedidoCliente() throws SQLException {
		if (cdProduto != null && pedido != null) {
			if (itemPedidoUltimoPedidoCliente == null || (!ValueUtil.valueEquals(itemPedidoUltimoPedidoCliente.cdCliente, pedido.cdCliente) || !ValueUtil.valueEquals(itemPedidoUltimoPedidoCliente.cdProduto, cdProduto) && !ValueUtil.valueEquals(itemPedidoUltimoPedidoCliente.nuPedido, pedido.nuPedido))) {
				itemPedidoUltimoPedidoCliente = ItemPedidoService.getInstance().findItemPedidoUltimoPedidoCliente(pedido.cdCliente, cdProduto, pedido.nuPedido);
			}
		}
		return itemPedidoUltimoPedidoCliente;
	}
	
	public int getFltipoEdicao() {
		if (vlPctDesconto > 0) {
			return ITEMPEDIDO_EDITANDO_DESCONTOPCT;
		}
		if (vlPctAcrescimo > 0) {
			return ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
		}
		return flTipoEdicao;
	}
	
	public double getVlItemComIpiCalcPersonalizado() {
		return vlItemPedido + (getQtItemFisico() > 0 ? (vlTotalIpiItem / getQtItemFisico()) : vlTotalIpiItem);
	}
	
	public double getVlTotalItemComIpiCalcPersonalizado() {
		return (vlItemPedido * getQtItemFisico()) + vlTotalIpiItem;
	}
	
	public double getPctDifStEIpi() {
		double vlTotalItemComIpiCalcPersonalizado = getVlTotalItemComIpiCalcPersonalizado();
		return vlTotalItemComIpiCalcPersonalizado > 0 ? (((getVlItemComTributos() * getQtItemFisico()) / vlTotalItemComIpiCalcPersonalizado) - 1) * 100 : 0;
	}
	
	public double getVlVolumeArrendondado() throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			if (getProdutoUnidade().isMultiplica()) {
				if (LavenderePdaConfig.calculaPrecoPorVolumeProduto && (getProduto().flCalculaPrecoVolume != null) && getProduto().flCalculaPrecoVolume.equals(ValueUtil.VALOR_SIM)){
					return ValueUtil.round(vlIndiceVolume * getProdutoUnidade().nuConversaoUnidade, LavenderePdaConfig.nuCasasDecimaisVlVolume);
				}
				return ValueUtil.round(getProduto().vlVolume * getProdutoUnidade().nuConversaoUnidade, LavenderePdaConfig.nuCasasDecimaisVlVolume);
			} else {
				if (LavenderePdaConfig.calculaPrecoPorVolumeProduto && (getProduto().flCalculaPrecoVolume != null) && getProduto().flCalculaPrecoVolume.equals(ValueUtil.VALOR_SIM)){
					return ValueUtil.round(vlIndiceVolume  / getProdutoUnidade().nuConversaoUnidade, LavenderePdaConfig.nuCasasDecimaisVlVolume);
				}				
				return ValueUtil.round(getProduto().vlVolume / getProdutoUnidade().nuConversaoUnidade, LavenderePdaConfig.nuCasasDecimaisVlVolume);
			}
		}
		if (LavenderePdaConfig.calculaPrecoPorVolumeProduto && (getProduto().flCalculaPrecoVolume != null) && getProduto().flCalculaPrecoVolume.equals(ValueUtil.VALOR_SIM)){
			return ValueUtil.round(vlIndiceVolume, LavenderePdaConfig.nuCasasDecimaisVlVolume);
		}
		return getProduto().getVlVolumeArrendondado();
	}
	
	public boolean isFlTipoCadastroDesconto() {
		return ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(flTipoCadastroItem);
	}
	
	public boolean isFlTipoCadastroQuantidade() {
		return ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(flTipoCadastroItem);
	}

	public ProdutoCreditoDesc getProdutoCreditoDesc() throws SQLException {
		if (ValueUtil.isNotEmpty(cdProdutoCreditoDesc) && (produtoCreditoDesc == null || !cdProdutoCreditoDesc.equals(produtoCreditoDesc.cdProdutoCreditoDesc))  ) {
			produtoCreditoDesc = ProdutoCreditoDescService.getInstance().getProdutoCreditoDescontoDoItem(cdEmpresa, cdProduto, cdProdutoCreditoDesc);
		}
		return produtoCreditoDesc;
	}
	
	public boolean isCdUnidadeIgualCdUnidadeProduto() throws SQLException {
		return ValueUtil.valueEquals(cdUnidade, getProduto().cdUnidade);
	}
	
	public String getQtItemFaturamentoToInterface() {
		if (LavenderePdaConfig.isUsaQtdInteiro()) {
			return StringUtil.getStringValueToInterface((int)this.qtItemFaturamento);
		} else {
			return StringUtil.getStringValueToInterface((double)this.qtItemFaturamento);
		}
	}
	
	public double getVlTotalCSLL() throws SQLException {
		return getVlTotalItemComTributos() * (pedido.getEmpresa().vlPctCsll / 100);
	}
	
	public double getVlTotalCPP() throws SQLException {
		return getVlTotalItemComTributos() * (pedido.getEmpresa().vlPctCpp / 100);
	}
	
	public double getVlTotalCustoVariavelProduto() throws SQLException {
		return getVlTotalItemComTributos() * (getProduto().vlPctCustoVariavel / 100);
	}

	public List<ItemPedidoGrade> getItemPedidoGradeErroList() {
		if (itemPedidoGradeErroList == null) {
			itemPedidoGradeErroList = new ArrayList<ItemPedidoGrade>();
		}
		return itemPedidoGradeErroList;
	}
	
	public boolean isSugVendaPerson() {
		return ValueUtil.getBooleanValue(flSugVendaPerson);
	}
	
	public boolean isFlTipoCreditoAlcada() throws SQLException {
		return pedido != null && pedido.isFlTipoCreditoAlcada();
	}
	
	public void clearProdutoUnidadeInfo() {
		this.nuConversaoUnidadePu = 0;
		this.flDivideMultiplicaPu = ValueUtil.VALOR_NI;
		this.vlIndiceFinanceiroPu = 0;
	}
	
	public boolean isFazParteKitFechado() {
		return ValueUtil.isNotEmpty(cdKit) && !LavenderePdaConfig.isUsaKitTipo3();
	}
	
	public boolean isRecebeuDescontoPorQuantidade() {
		return  vlPctFaixaDescQtd > 0;
	}
	
	public double getVlPrecoCusto() throws SQLException {
		ItemTabelaPreco itemTabelaPreco = getItemTabelaPrecoAtual();
		itemTabelaPreco = itemTabelaPreco == null ? getItemTabelaPreco() : itemTabelaPreco;
		if (itemTabelaPreco.vlPrecoCusto > 0) {
			return itemTabelaPreco.vlPrecoCusto; 
		}
		if (getProduto() != null) {
			return getProduto().vlPrecoCusto;
		}
		return 0;
	}
	
	public double getVlPctDescKitFechado() throws SQLException {
		String flBonificado = StringUtil.getStringValue(isItemBonificacao()); 
		if (vlPctDescKitFechado == 0) {
			vlPctDescKitFechado = ItemKitService.getInstance().findVlPctDesconto(cdKit, cdProduto, flBonificado);
		}
		return vlPctDescKitFechado;
	}
	
	public boolean isFazPartePromocao() {
		return ValueUtil.getBooleanValue(flPromocao) || nuPromocao > 0;
	}

	public boolean isItemTabelaPrecoPromocao() {
		return itemTabelaPreco != null && ValueUtil.getBooleanValue(itemTabelaPreco.flPromocao);
	}

	public DescMaxProdCli getDescMaxProdCli() throws SQLException {
		if (LavenderePdaConfig.usaDescMaxProdCli) {
			if (descMaxProdCli == null) {
				DescMaxProdCliService.getInstance().loadDescMaxProdCli(this);
			}
			return descMaxProdCli;
		}
		return null;
	}
	
	public boolean possuiDescMaxProdCli() throws SQLException {
		return getDescMaxProdCli() != null;
	}
	
	public double getVlFecop() {
    	return LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? vlTotalFecopItem : vlFecop;
    }
	
	public boolean isErroRecalculo() {
		return ValueUtil.VALOR_SIM.equals(flErroRecalculo);
	}
	
	public double getSomaValoresTribPersonalizada() {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			return vlTotalStItem + vlTotalIpiItem + vlTotalIcmsItem + vlTotalPisItem + vlTotalCofinsItem + vlTotalFecopItem;
		}
		return 0;
	}
	
	public double getValoresTribRound(boolean unitario) {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			try {
				return ValueUtil.getDoubleValue(new BigDecimal(String.valueOf(vlTotalStItem))
						.add(new BigDecimal(String.valueOf(vlTotalIpiItem)))
						.divide(new BigDecimal(String.valueOf(unitario && getQtItemFisico() > 0 ? getQtItemFisico() : 1)), ValueUtil.doublePrecision, BigDecimal.ROUND_HALF_UP).toPlainString());
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
		return 0;
	}
	
	public double getPesoGrade() throws SQLException {
		if (!LavenderePdaConfig.usaGradeProduto4()) return getProduto().qtPeso;
		
		ProdutoGrade produtoGrade = ProdutoGradeService.getInstance().getProdutoGradeFilter(cdItemGrade1, cdItemGrade2, cdItemGrade3, cdProduto, cdTabelaPreco);
		produtoGrade = (ProdutoGrade) ProdutoGradeService.getInstance().findByPrimaryKey(produtoGrade);
		return (produtoGrade != null && produtoGrade.qtPeso != 0.0) ? produtoGrade.qtPeso : getProduto().qtPeso;
	}
	
	public double getPesoItemPedido() throws SQLException {
		return getPesoGrade() * getQtItemFisico();
	}
	
	public boolean unidadeBaseIgnoraMultEspecial() throws SQLException {
		Produto produto = getProduto();
		if (produto == null || produto.cdUnidade == null) return false;
		Unidade filter = new Unidade(this.cdEmpresa, this.cdRepresentante, produto.cdUnidade);
		String flIgnoraMultEspecial = UnidadeService.getInstance().findColumnByRowKey(filter.getRowKey(), "FLIGNORAMULTESPECIAL");
		return ValueUtil.VALOR_SIM.equals(flIgnoraMultEspecial);
	}
	
	public double getVlItemPedidoUnitarioTributosFreteSeguro() {
		return vlItemPedido + getVlTotalSTItem() + getVlTotalIpiItem() + vlSeguroItemPedido + getVlTotalFreteItem();
	}
	
	public double getVlTotalItemPedidoTributosFreteSeguro() {
		return vlTotalItemPedido + getVlTotalST() + getVlTotalIpi() + getVlTotalSeguro() + getVlTotalFrete();
	}

	public double getPctComissaoByPoliticaComercialFaixa() throws SQLException {
		return PoliticaComercialFaixaService.getInstance().findPctComissaoByPoliticaComercialItemPedido(this);
	}

	public boolean isFlSituacaoBloqueado() throws SQLException {
		return getProduto().flSituacao == Produto.FL_SITUACAO_PRODUTO_BLOQUEADO;
	}

	public boolean isGondola() {
		return pedido.isGondola() && qtItemGondola >= 0;
	}

	public boolean isCombo() {
		return ValueUtil.isNotEmpty(cdCombo);
	}

	public double getVlEmbalagemElementar() throws SQLException {
		if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem() && LavenderePdaConfig.usaUnidadeAlternativa && !LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar) {
			return ItemPedidoService.getInstance().getVlUnidadeEmbalagem(this);
		}
		if (vlEmbalagemElementar > 0) {
			return vlEmbalagemElementar;
		}
		if (produto == null || produto.nuConversaoUnidadesMedida <= 0) {
			return 0;
		}
		int nuFracao = produto.nuFracao <= 0 || !LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto ? 1 : produto.nuFracao;
		return vlItemPedido / (produto.nuConversaoUnidadesMedida * nuFracao);
	}

	public ItemCombo getItemCombo() throws SQLException {
		return itemCombo == null && isCombo() ? itemCombo = ItemComboService.getInstance().findByItemPedido(this) : itemCombo;
	}

	public boolean isKitTipo3() {
		return LavenderePdaConfig.isUsaKitTipo3() && ValueUtil.isNotEmpty(cdKit);
	}

	public ItemKit getItemKit() throws SQLException {
		return itemKit == null ? itemKit = ItemKitService.getInstance().findItemKitByItemPedido(this) : itemKit;
	}

	public void setItemKit(ItemKit itemKit) {
		this.itemKit = itemKit;
	}

	public boolean permiteUtilizarVlBaseFlexComoVlBaseCalculo() throws SQLException {
		return !LavenderePdaConfig.aplicaBaseadoTabelaPreco || !getTabelaPreco().isUsaDescontoMaximoPorVlBaseItemPedido();
	}
	public double getNuConversaoUnidade() throws SQLException {
		Produto produtoItem = getProduto();
		if (produtoItem == null) return 0;
		
		double nuConversaoUnidade = 0;
		nuConversaoUnidade = LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() ? produtoItem.nuMultiploEspecial : produtoItem.nuConversaoUnidadesMedida;
		ProdutoUnidade prodUnidade = this.getProdutoUnidade();
		nuConversaoUnidade = LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa() && prodUnidade != null && prodUnidade.nuMultiploEspecial != 0 ? prodUnidade.nuMultiploEspecial : nuConversaoUnidade;
		return nuConversaoUnidade;
	}

	public boolean hasDescProgressivo() {
		return LavenderePdaConfig.usaDescProgressivoPersonalizado && ValueUtil.isNotEmpty(cdDescProgressivo) && !auxiliarVariaveis.insertingVerba;
	}

	public boolean permiteAplicarDesconto() {
		return !hasDescProgressivo() && !isKitTipo3() && !(LavenderePdaConfig.isExibeComboMenuInferior() && isCombo());
	}

	public boolean isIgnoraDescQtd() {
		return ValueUtil.getBooleanValue(flIgnoraDescQtd);
	}
	
	public boolean isIgnoraDescQtdPro() {
		return ValueUtil.getBooleanValue(flIgnoraDescQtdPro);
	}

	public boolean permiteAplicarDescProgressivoPersonalizado() throws SQLException {
		return !isCombo() && ValueUtil.isEmpty(cdKit) && !isItemAutorizadoOuPendenteSolAutorizacao();
	}

	private boolean isItemAutorizadoOuPendenteSolAutorizacao() throws SQLException {
		return LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && this.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizadoOuPendente(this, null);
	}


	public double getVlPctDescontoAcrescimo() {
		return vlPctDesconto > 0 || vlPctAcrescimo == 0 ? vlPctDesconto : -vlPctAcrescimo;
	}

	public double getVlPctDescontoAcrescimoDefaultItem() {
		if (pedido == null) return 0d;
		if (pedido.vlPctDescItem > 0 || pedido.vlPctAcrescimoItem == 0) {
			return LavenderePdaConfig.isAcumulaComDescDoItem() ? 0 : pedido.vlPctDescItem;
		}
		return -pedido.vlPctAcrescimoItem;
	}

	public boolean isAgrupadorSimilaridade() {
		return ValueUtil.getBooleanValue(flAgrupadorSimilaridade);
	}

	public SolAutorizacao getSolAutorizacao() {
		if (solAutorizacao == null) {
			try {
				solAutorizacao = SolAutorizacaoService.getInstance().getSolAutorizacaoSimilares(this, null);
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
		return solAutorizacao;
	}

	public double getQtItemFisico() {
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && isAgrupadorSimilaridade() && getSolAutorizacao() != null) {
			return getSolAutorizacao().qtItemFisico;
		}
		return qtItemFisico;
	}

	public void setQtItemFisico(double qtItemFisico) {
		this.qtItemFisico = qtItemFisico;
	}

	public double getQtItemFisicoOrg() {
		return qtItemFisico;
	}


	public Vector getBonifCfgList() throws SQLException {
		if (bonifCfgList == null || forceRefreshBonifCfgList) {
			bonifCfgList = BonifCfgService.getInstance().getBonifCfgAplicaveis(this);
			forceRefreshBonifCfgList = false;
		}
		return bonifCfgList;
	}
	
	public boolean isOrigemErp() {
		return ITEMPEDIDO_FLORIGEMERP.equalsIgnoreCase(flOrigemPedido);
	}

	public Boolean isInseridoNoPedido() {
		return pedido.itemPedidoList != null && pedido.itemPedidoList.contains(this);
	}
	
	public String getKeyItemPedidoMargemRentab() {
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && ValueUtil.isNotEmpty(cdTabelaPreco)) {
			return this.getRowKey() + cdTabelaPreco;
		} else {
			return this.getRowKey();
		}
	}
	
	public boolean isMantemValorKitNaReplicacao() {
		return LavenderePdaConfig.isMantemValorKitNaReplicacao() && isKitTipo3(); 
	}
   
}
