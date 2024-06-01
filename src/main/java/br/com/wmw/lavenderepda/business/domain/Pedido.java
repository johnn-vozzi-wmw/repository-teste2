package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.PedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.cache.SolAutorizacaoPedidoCache;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoDTO;
import br.com.wmw.lavenderepda.business.domain.dto.RecalculoDescontoProgressivoDTO;
import br.com.wmw.lavenderepda.business.service.AtividadePedidoService;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.CentroCustoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.CondComCondPagtoService;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.DescProgQtdService;
import br.com.wmw.lavenderepda.business.service.DescProgressivoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.FreteConfigService;
import br.com.wmw.lavenderepda.business.service.InfoFretePedidoService;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import br.com.wmw.lavenderepda.business.service.ItemLiberacaoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoBonifCfgService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.KitService;
import br.com.wmw.lavenderepda.business.service.ModoFaturamentoService;
import br.com.wmw.lavenderepda.business.service.MotivoPendenciaService;
import br.com.wmw.lavenderepda.business.service.NfceService;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.NotaFiscalService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoBoletoService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.TipoFreteService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.service.TranspTipoPedService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import totalcross.sys.Settings;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class Pedido extends BasePersonDomain {

	public static final String TABLE_NAME_PEDIDO = "TBLVPPEDIDO";
	public static final String TABLE_NAME_PEDIDOERP = "TBLVPPEDIDOERP";
	public static final String TABLE_NAME_PEDIDOWEB = "TBLVWPEDIDO";

	public static final String FLTIPOALTERACAO_PROCESSANDO_ENVIO = "P";
	public static final String FLTIPOALTERACAO_ALTERADO_ORCAMENTO = "O";

	public static final String SIGLE_VMP_EXCEPTION = "VMP";

	public static final int MAX_LENGTH_DS_OBSERVACAO = 4000;

	public static final int DATA_ENTREGA_SABADO = 1;
	public static final int DATA_ENTREGA_DOMINGO = 2;
	public static final int DATA_ENTREGA_SABADO_OU_DOMINGO = 3;

	public static final int SUGESTAO_PEDIDO_BASEADO_ULTIMO_PEDIDO = 0;
	public static final int SUGESTAO_PEDIDO_BASEADO_GIRO = 1;

	public static final String DSRESPOSTA_PEDIDO_ENVIADO_SERVIDOR = "ENVIADO";
	public static final String DSRESPOSTA_PEDIDO_NAO_ENVIADO_SERVIDOR = "NAO_ENVIADO";
	
	public static final String NMCOLUNA_VLPCTACRESCIMOCUSTO = "VLPCTACRESCIMOCUSTO";
	public static final String NMCOLUNA_CDMOTIVOACRESCIMOCUSTO = "CDMOTIVOACRESCIMOCUSTO";
	public static final String NMCOLUNA_FLENVIAEMAIL = "FLENVIAEMAIL";
	public static final String NMCOLUNA_DSEMAILSDESTINO = "DSEMAILSDESTINO";
	public static final String NMCOLUNA_DSOBSERVACAO = "DSOBSERVACAO";
	public static final String NMCOLUNA_CDCLIENTEENTREGA = "CDCLIENTEENTREGA";
	public static final String NMCOLUNA_FLRESTRITO = "FLRESTRITO";
	public static final String NMCOLUNA_VLVERBAPEDIDO = "vlVerbaPedido";
	public static final String NMTAB_PEDIDO = "Pedido";
	public static final String NMCOLUNA_CDSTATUSPEDIDO = "CDSTATUSPEDIDO";
	public static final String NMCOLUNA_CDCONDICAOPAGAMENTO = "CDCONDICAOPAGAMENTO";
	public static final String NMCOLUNA_CDENTREGA = "CDENTREGA";
	public static final String NMCOLUNA_FLNFEIMPRESSA = "FLNFEIMPRESSA";
	public static final String NMCOLUNA_VLTOTALNOTACREDITO = "VLTOTALNOTACREDITO";
	public static final String NMCOLUNA_DTEMISSAO = "DTEMISSAO";
	public static final String NMCOLUNA_FLPROCESSANDONFETXT = "FLPROCESSANDONFETXT";
	public static final String NMCOLUNA_FLCREDITOCLIENTELIBERADOSENHA = "FLCREDITOCLIENTELIBERADOSENHA";
	public static final String NMCOLUNA_ENDPADRAO = "ENDPADRAO";
	public static final String NMCOLUNA_CDPRODUTO = "CDPRODUTO";
	public static final String NMCOLUNA_DSPRODUTO = "DSPRODUTO";
	public static final String NMCOLUNA_NUSEQITEMPEDIDO = "NUSEQITEMPEDIDO";
	public static final String NMCOLUNA_VLFRETEADICIONAL = "VLFRETEADICIONAL";
	public static final String NMCOLUNA_VLTOTALPEDIDO = "VLTOTALPEDIDO";
	public static final String NMCOLUNA_VLFRETE = "VLFRETE";

	public static final String CAMPOS_ADICIONAIS_IMPRESSAO_DATA = "1";
	public static final String IGNORA_CONDPAGTO_REPLICACAO = "1";
	public static final String IGNORA_SELECAO_CLIENTE_REPLICACAO = "2";

	public String cdEmpresa;
	public String cdRepresentante;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdCliente;
	public String nuPedidoRelacionado;
	public String flOrigemPedidoRelacionado;
	public String cdStatusPedido;
	public Date dtEmissao;
	public String hrEmissao;
	public Date dtEntrega;
	public double vlTotalItens;
	public double vlTotalPedido;
	public String cdCondicaoPagamento;
	public String cdTabelaPreco;
	public String cdTipoPagamento;
	public String cdTipoPedido;
	public String cdSegmento;
	public String cdCondicaoComercial;
	public String cdTransportadora;
	public String cdTipoFrete;
	public double vlFrete;
	public String cdSupervisor;
	public double vlVerbaPedido;
	public double vlVerbaPedidoPositivo;
	public String hrFimEmissao;
	public String flPrecoLiberadoSenha;
	public String nuOrdemCompraCliente;
	public String cdRotaEntrega;
	public String flCreditoClienteLiberadoSenha;
	public String flPossuiDiferenca;
	public String flPedidoDiferenca;
	public String nuPedidoDiferenca;
	public String cdSetor;
	public String cdOrigemSetor;
	public String cdTipoEntrega;
	public String nuVersaoSistemaOrigem;
	public String dsUrlEnvioServidor;
	public double vlDesconto;
	public double qtPeso;
	public String cdAreaVenda;
	public Date dtFechamento;
	public String hrFechamento;
	public Date dtTransmissaoPda;
	public String hrTransmissaoPda;
	public double vlTrocaRecolher;
	public double vlTrocaEntregar;
	public double vlPctDesconto;
	public double vlPctDescProgressivo;
	public double vlPctDescItem;
	public double vlPctAcrescimoItem;
	public double vlBonificacaoPedido;
	public double vlRentabilidade;
	public String flPedidoNovoCliente;
	public String dsCondicaoPagamentoSemCadastro;
	public int qtPontosPedido;
	public String flBloqueadoEdicao;
	public double vlTotalBrutoItens;
	public double vlTotalBaseItens;
	public String flMaxVendaLiberadoSenha;
	public String flClienteAtrasadoLiberadoSenha;
	public String dsMotivoDesconto;
	public double vlPctFreteRepresentante;
	public double vlFreteRepresentante;
	public double vlFreteCliente;
	public String flImpresso;
	public String nuPedidoRelBonificacao;
	public String nuPedidoSugestao;
	public String flOrigemPedidoSugestao;
	public double vlTotalPedidoEstoquePositivo;
	public String cdCentroCusto;
	public String cdPlataformaVenda;
	public String cdItemConta;
	public String cdClasseValor;
	public String cdModoFaturamento;
	public String dsModoFaturamento;
	public String flPedidoReplicado;
	public String nuPedidoOriginal;
	public String flSugestaoVendaLiberadoSenha;
	public String cdCargaPedido;
	public boolean flAbaixoRentabilidadeMinima;
	public double vlPctMargemMin;
	public String flEtapaVerba;
	public double vlPctComissao;
	public String flPagamentoAVista;
	public String flGeraNfe;
	public int nuKmInicial;
	public int nuKmFinal;
	public String hrInicialIndicado;
	public String hrFinalIndicado;
	public String flNfeImpressa;
	public String flLiberadoEntrega;
	public String cdEnderecoCliente;
	public String flGeraBoleto;
	public String flBoletoImpresso;
	public String flSituacaoReservaEst;
	public String nuPedidoRelTroca;
	public String nuPedidoComplementado;
	public String flKeyAccount;
	public String flPendente;
	public double vlTotalCreditoCondicao;
	public double vlTotalCreditoFrete;
	public String flGeraCreditoCondicao;
	public String flGeraCreditoFrete;
	public String cdMotivoCancelamento;
	public String flItemPendente;
	public double vlPctDescProgressivoMix;
	public double vlVolumePedido;
	public double vlPctDescFrete;
	public double vlPctDescCliente;
	public double vlPctDescontoCondicao;
	public Date dtEntregaLiberada;
	public double vlTotalTrocaPedido;
	public Date dtCarregamento;
	public String flCotaCondPagto;
	public String cdUsuarioLibEntrega;
	public String flNfeContImpressa;
	public Date dtConsignacao;
	public double vlPedidoOriginal;
	public double vlTotalDevolucoes;
	public String flConsignacaoImpressa;
	public String cdPedidosAgrupados;
	public String cdTransportadoraAux;
	public String cdEntrega;
	public String cdRegiao;
	public String flRentabilidadeLiberada;
	public String flGeraNfce;
	public String flNfceImpressa;
	public String flSaldoBoniLiberadoSenha;
	public double vlPctDesc2;
	public double vlPctDesc3;
	public String cdStatusOrcamento;
	public String dsObsOrcamento;
	public String dsJustificativaCancelamento;
	public String flDescontoLiberadoSenha;
	public double vlPctFrete;
	public String cdContato;
	public String nuPedCompRelacionado;
	public String flRestrito;
	public String cdCondNegociacao;
	public String oldCdCondicaoNegociacao;
	public String flMinVerbaLiberado;
	public String cdUnidade;
	public String flPromissoriaImpressa;
	public String nuCnpjTransportadora;
	public String flModoEstoque;
	public Date dtPagamento;
	public double vlPctIndiceFinCondPagto;
	public double vlPctDescQuantidadePeso;
	public String cdTributacaoCliente;
	public String flProcessandoNfeTxt;
	public String cdTabPrecoFilterSug;
	public String flAguardaPedidoComplementar;
	public Date dtUltimoRecalculoValores;
	public String dsMotivoCancLimCredito;
	public String cdUsuarioCancLimCred;
	public String cdUsuarioLiberacaoLimCred;
	public String flPendenteLimCred;
	public double vlPctDescontoAutoEfetivo;
	public double vlTotalDescontoAuto;
	public double vlDescontoTotalAutoDesc;
	public String nuLoteProtocolo;
	public String flEnviadoProtocolo;
	public double vlTotalNotaCredito;
	public double vlDescontoIndiceFinanCliente;
	public double vlSeguroPedido;
	public String flValorMinParcelaLiberadoSenha;
	public double vlPctTotalMargem;
	public String dsObservacaoCliente;
	public int qtDiasCPgtoLibSenha;
	public double vlTotalPedidoLiberado;
	public String cdFreteConfig;
	public String flCalculaSeguro;
	public int nuParcelas;
	public double vlVerbaFornecedor;
	public double vlVerbaGrupo;
	public double vlPctDescHistoricoVendas;
	public String dsMotivoBonificacao;
	public String cdRepresentanteOrg;
	public String cdContaCorrente;
	public String cdLocal;
	public String dsObsModoFaturamento;
	public double vlTotalPontuacaoBase;
	public double vlTotalPontuacaoRealizado;
	public double vlBaseMargemRentab;
	public double vlCustoMargemRentab;
	public String cdMargemRentab;
	public double vlPctMargemRentab;
	
	public String cdEnderecoCobranca;
	public String flGondola;
	public String flCritico;
	public String flPendenteFob;
	public String cdUsuarioCancFob;
	public String cdUsuarioLiberacaoFob;
	public double vlRentabilidadeTotal;
	public double vlRentabTotalItens;
	public double vlPctComissaoTotal;
	public double vlFreteTotal;
	public String flCodigoInternoCliente;
	public String flUtilizaRentabilidade;
	public double vlRentabilidadeSug;
	public double vlRentabSugItens;
	
	public String cdMotivoPerda;
	public String cdMotivoPendencia;
	public String cdMotivoPendenciaJust;
	public String dsObsMotivoPendencia;
	public String oldCdMotivoPendencia;
	public double vlFinalPedidoDescTribFrete;
	public String flPendenteCondPagto;
	public String dsMotivoCancCondPagto;
	public String cdUsuarioCancCondPagto;
	public String cdUsuarioLiberacaoCondPagto;
	public String flCalculaPontuacao;
	public Date dtFaturamento;
	public String flVinculaCampanhaPublicitaria;
	public String cdCampanhaPublicitaria;
	public String flEdicaoBloqueada;
	public double vlFreteAdicional;
	public Date dtSugestaoCliente;
	public String dsQrCodePix;
	public String cdDivisaoVenda;

	//Dinâmicos
	public String dsObservacao;
	public double vlCotacaoDolar;
	public String dsMensagemEntrega;
	public String dsDestino;
	public String dsEmailsDestino;
	public String cdClienteEntrega;

	//Não persistentes
	public Vector cdProdutoDistinctList;
	public ArrayList<String> itensBonificados;
	public boolean isDtSugeridaDiferenteDtEntrega;
	public boolean onResumoDiario;
	public boolean isTrocaDataPrevista;
	public String cdMarcadores;
	public boolean onFechamentoPedido;
	public Vector itemPedidoList;
	public Vector itemPedidoListCalculoTributacao;
	public Vector itemPedidoOportunidadeList;
	public Vector itemPedidoTrocaList;
	public Vector parcelaPedidoList;
	public Vector condicaoPagamentoDisponiveis;
	public String oldCdCondicaoPagamento;
	public String oldCdCondicaoComercial;
	public String oldCdTipoFrete;
	public double oldVlTotalItens = -1.0;
	public String oldCdTipoPedido;
	public Hashtable verbaProdutoHash;
	public Vector verbaProdutoMixList;
	public Vector verbaProdutoQuebraMixList;
	public Vector rentabilidadeFaixaList;
	public Vector itemPedidoNaoInseridoSugestaoPedList;
	public Vector itemPedidoDivergenciaEstoqueSugestaoPedList;
	public Vector itemPedidoDivergenciaDescontoSugestaoPedList;
	public Vector itemPedidoConsumoVerbaSugestaoPedList;
	public Vector itemPedidoEstoquePrevistoExcepList;
	public Vector itemPedidoInseridosAdvertenciaList;
	public Vector itemPedidoAConfirmarInsercaoList;
	public boolean inserindoFromSugestaoPedido;
	public boolean preencheuCdTransportadoraSankhya;
	public String qtDiasVctoParcelas;
    public Cliente cliente;
	public boolean refreshListItemPedidoFromPoliticaBonificacaoAtualizada;
	protected Empresa empresa;
	private CargaPedido cargaPedido;
	private CondicaoComercial condicaoComercial;
	private CondicaoNegociacao condicaoNegociacao;
	private CondComCondPagto condComCondPagto;
	private CondicaoPagamento condicaoPagamento;
	private TipoPagamento tipoPagamento;
	public TipoPedido tipoPedido;
	public Transportadora transportadora;
	private TranspTipoPed transpTipoPed;
	protected TipoFrete tipoFrete;
	private TabelaPreco tabelaPreco;
	private DescProgressivo descProgressivo;
	private DescProgQtd descProgQtd;
	private InfoFretePedido infoFretePedido;
	private ItemLiberacao itemLiberacao;
	public String flHouveAlteracao;
	public Nfe nfe;
	public Vector pedidoBoletoList;
	public Vector notaFiscalList;
	public Vector atividadePedidoList;
	public Vector pedidoConsignacaoList;
	public Vector pedidoConsignacaoDevolucaoList;
	public Vector pedidoBoletoContList;
	public boolean updateByClickNovoItemInPedido;
	public boolean updateByClickSalvarItemPedido;
	public boolean showMessageLimiteCredito = true;
	public boolean showMessagePontuacao = true;
	public boolean validaDataEntrega = true;
	public boolean deletadoPelaIntefacePedido;
	public double vlComissaoPedido;
	public double vlPctComissaoPedido;
	public double vlTtPedidoComSt;
	public double vlTtPedidoComIpi;
	public double vlTtPedidoComTributos;

	public double vlVerbaPedidoOld;
	public double vlDescontoOld;
	public boolean bonificacaoLiberada;
	public double qtItensTotalUnidade;
	public double qtItensTotalCaixa;
	public String cdFornecedorDefault;
	public boolean fecharPedidoComVerbaNeg;
	public static String sortAttr;
	public double vlFreteItensPedidoOld;
	public double vlFreteOld;
	public double vlTotalPedidoOld;
	public Hashtable metaPorGrupoProdHash;
	public int nuSequenciaAgenda;
	public int qtItensPedidoEnvioServidor;
	public boolean flDescQtdGrupoAplicadoAuto;
	public Vector prodRelacionadosNaoContempladosList;
	public boolean isFiltraPedidoNuPedidoRelTrocaEmpty;
	public boolean usandoTabelaPrecoPorCondicaoPagamento;
	public boolean ignoraValidacaoSugestaoDifProdutos;
	public boolean ignoraValidacaoSugestaoProdutosSemQtde;
	public boolean ignoraValidacaoSugestaoItensRentabilidadeIdeal;
	public boolean ignoraValidacaoSugestaoProdutosComQtde;
	public boolean ignoraValidacaoProdutosPendentes;
	public boolean ignoraGiroProdutoPendente;
	public boolean ignoraValidacaoAtrasoCliente;
	public boolean ignoraAvisoSugestaoVendaSemQtdOutrasEmpresas;
	public boolean ignoraAvisoSugestaoVendaComQtdOutrasEmpresas;
	public boolean ignoraValidacaoMultiplosSugestaoProdutos;
	public boolean ignoraValidacaoLimiteCreditoCliente;
	public Date dtEmissaoInicialFilter;
	public Date dtEmissaoFinalFilter;
	public Date dtFechamentoInicialFilter;
	public Date dtFechamentoFinalFilter;
	public boolean flFiltraPedidosFechadosTransmitidos;
	public boolean flFiltraPedidosDifAbertosCancelados;
	public boolean flFiltraPedidosDifFechamentoDiario;
	public boolean flFiltraPedidosDif;
	public String cdStatusPedidoDif;
	public boolean atualizouCustoItem;
	public double vlMargemPercentual;
	public double vlMargemEspecificaPonderada;
	public double vlMargemValor;
	public double vlReceitaVirtual;
	public int countItensSuceso;
	public int countItensErro;
	public Vector itensErros;
	public Map<ItemPedido, ValidationException> itensComErroAndExceptionMap;
	private Visita visita;
	public boolean insertVisita;
	public boolean ignoraInativacaoReservaEstoque;
	public boolean ignoraGeracaoReservaEstoque;
	public Vector itemPedidoProblemaReservaEstoqueList;
	public Vector itemPedidoComReservaEstoqueCorrenteList;
	public boolean isMotivoAcrescimoCustoVazio;
	public boolean isVlAcrescimoCustoVazio;
	public Vector itemPedidoParticipacaoExtrapoladaList;
	public Pedido pedidoRelacionado;
	public double vlAtualPedido;
	public double vlCreditoDisponivelConsumo;
	public TabelaPreco tabelaPrecoListaProdutosFilter;
	public boolean isPedidoNoMesCorrente;
	public boolean isPedidoBaseadoGiro;
	public boolean isPedidoReaberto;
	public boolean flUIChange;
	public boolean ignoraValidacaoEstoque;
	public String dsClienteEntrega;
	public boolean isDeletePedidosPdaByPedidosErp;
	public String flSituacaoReservaEstReabrePedido;
	public String[] ultimoConcorrenteProdutoDesejado;
	public boolean ultimoConcorrenteProdutoDesejadoFlag;
	public String pedidoNfeBoletoLabel;
	public boolean filtraDocNaoImpressos;
	public boolean pedidoConsignadoVencido;
	public boolean pedidoConsignadoAVencer;
	public BigDecimal nuDocumento;
	public Date dtVencimento;
	public boolean fechandoPedidoConsignado;
	private PagamentoPedido debitoBancario;
	public Vector descQuantidadePesoList;
	public DescQuantidadePeso descQuantidadePeso;
	public DescQuantidadePeso descQuantidadePesoOld;
	public boolean isRecalculandoVlBaseFlexItens;
	public boolean isRecalculandoVlItemPedidoItens;
	public boolean isResetandoItemPedido;
	public boolean inFecharPedidosEmLote;
	public boolean recalculaPedidoMudancaFaixaDesc;
	public boolean itensAtingiramFaixa;
	public String cdGrupoRecalc;
	public String flKeyAccountFilter;
	public String[] cdStatusPedidoFilter;
	public boolean atualizaLista;
	public Vector docAnexoList;
	public Vector fotoList;
	public Date dtTransmissaoPdaInicialFilter;
	public Date dtTransmissaoPdaFinalFilter;
	public int qtdCreditoDescontoGerado;
	public int qtdCreditoDescontoConsumido;
	public Vector itemPedidoCredDescList;
	public TransportadoraReg transportadoraReg;
	public TransportadoraCep transportadoraCep;
	public double saldoVerbaPedido;
	public double vlTotalMargem;
	public Nfce nfce;
	public List<SugVendaPerson> sugVendaPersonList;
	public boolean cdCondicaoComercialChanged;
	public Vector pagamentoPedidoList;
	public boolean aptoImpressao;
	public double oldVlPctCat1;
	public double oldVlPctCat2;
	public double oldVlPctCat3;
	public boolean usaFiltroTipoAlteracao;
	public boolean onReplicacao;
	public boolean verificadoSePosuiFoto;
	public boolean geraPdfOfflineFechamentoLote;
	public String msgProblemaGeracaoPdfOffline;
	public boolean pedidoSimulacao;
	private boolean sugestaoPedidoGiroProduto;
	public double vlPedidoBeforeCota;
	public boolean deveValidarConsumoVerbaPedido;
	public boolean consumoVerbaSaldoLiberadoSenha;
	public Vector erroItensFechamentoPedido;
	public Vector itemPedidoPrecoNegociadoList;
	public String dsClienteFilter;
	public double vlPctVpc;
	public boolean isReplicandoPedido;
	public boolean isReplicandoPedidoAvisoCapaPedido;
	public Vector notaCreditoPedidoList;
	public boolean filtraApenasPedidosComDescontoFinanceiro;
	public String flCreditoClienteAberto;
	public boolean desconsideraPedidoPendente;
	public Vector itemPedidoInseridoDivergList;
	public int cdComissaoPedidoRep;
	public ComissaoPedidoRep comissaoPedidoRep;
	public Vector produtosNaoInseridos;
	public double vlTotalPedidoFreteTributos;
	public double vlTotalFretePedido;
	public FreteConfig freteConfig;
	public boolean ignoraRecalculoItens;
	public boolean showMessageDtVencimentoPrecoItemTabelaPreco = true;
	public double vlVerbaFaltanteSimulada;
	public String lastValidationVerbaSimulada;
	public boolean solicitaSenhaLiberaPrecoProdutoReplicacao;
	public StatusPedidoPda statusPedidoPda;
	public StatusOrcamento statusOrcamento;
	public Vector pedidoRelacionadoList;
	public CentroCusto centroCusto;
	private boolean filtraTipoPagamentoQueNaoIgnoraLimiteCredito;
	public boolean isDeletandoAutomaticamente;
	public PesoFaixa pesoFaixa;
	public boolean isPedidoMultiplaInsercao;
	public boolean filtraPedidoPerdido;
	public boolean houveAtualizacaoDescProgressivoFechamento;
	public double vlTotalDinheiro;
	public double vlTotalBoleto;
	public double vlTotalCheque;
	public String flConvertePedidoReplicacao;

	public boolean removendoItem;
	public boolean ignoreSolAutorizacaoItemExcluido;
	private Vector itemPedidoAgrSimilares;

	public HashMap<String, Object> variavelCalculoList;
	public double vlComissaoPedidoTotal;
	public String cdMarcadorFilter;
	public boolean ignoraTipoPedidoIgnoraEnvioErp;
	public boolean filtraApenasTipoPedidoIgnoraEnvioErp;
	public boolean flPedidoErp;
	public String cdRedeCliente;
	public Date dtEntregaInicialFilter;
	public Date dtEntregaFinalFilter;
	public int nuSolAutorizacaoPendenteOuNaoAutorizada;
	public boolean flSugestao;
	public boolean isAdiconandoItemRelProdutosPendentes;
	public boolean cdCondicaoPagamentoChanged;
	public String nmRepresentante;
	public boolean isPedidoReplicadoConvertidoTipoPedido;
	public boolean isTipoPedidoChanged;
	public boolean isTipoFreteChanged;
	public boolean parcelaMinMaxPendenteOuAutorizada;
	public String[] cdStatusExcecaoList;
	private Vector bonifCfgPedidoList;
	public boolean forceRefreshBonifCfgPedidoList;
	public boolean sortByTipoItemPedido;
	public boolean addJoinTipoPedidoOnCadastroSac;
	public boolean filterByMinSumQtItemFisico;
	public double minSumQtItemFisico;
	public StatusOrcamento statusOrcamentoFilter;
	public ItemPedido itemPedidoFilter;
	public String notFlTipoAlteracao;
	public boolean onlyPedidoComNfe;
	public String nuOrdemCompraClienteLikeFilter;
	public String nuPedidoLikeFilter;
	public boolean filterByMeusPedidos;
	public int nuOrdemLiberacaoFilter;
	public SupervisorRep supervisorRepFilter;

	public boolean isGeraPdfOfflineAuto;
	public Map<String, Double> qtSaldoBonifCfgFaixaQtde;
	public Map<String, Double> qtConsumirBonifCfgFaixaQtde;
	public Map<String, BonifCfgFaixaQtde> bonifCfgFaixaQtdeAtualMap;
	public Map<String, BonifCfgFaixaQtde> bonifCfgFaixaQtdeOldMap;
	public Map<String, BonifCfgFaixaQtde> bonifCfgFaixaQtdeDeletedMap;
	public boolean isDeletandoItemAutomaticamente;
	private Vector itemBrindeBonifCfgList;
	public boolean aplicouBonifContaCorrente;
	public boolean totalmenteConvertidoPedidoBonificacao;
	public Vector itemTotalmenteConvertidoBonifList;
	public RecalculoDescontoProgressivoDTO recalculoDescontoProgressivoDTO;
	public boolean flFiltraPedidosDifCancelados;
	public boolean alterouPoliticaComercialItem;

	//-- Controle de cache solAutorizacao
	public SolAutorizacaoPedidoCache solAutorizacaoPedidoCache = new SolAutorizacaoPedidoCache();

	//--PDF Online
	public double qtEncomenda;
	public double vlTotalEncomenda;

	//para controle do reload da condicao de pagamento
	public boolean loadedPoliticaComercialOnCondicaoPagtoChange;
	public boolean forceReloadPoliticaComercialSync;
	public boolean fromPedidoSemCliente;

	public Pedido() {
		this(TABLE_NAME_PEDIDO);
	}

	public Pedido(String tableName) {
		super(tableName);
		itemPedidoList = new Vector();
		itemPedidoTrocaList = new Vector();
		parcelaPedidoList = new Vector(0);
		verbaProdutoQuebraMixList = new Vector();
		metaPorGrupoProdHash = new Hashtable("");
		itemPedidoOportunidadeList = new Vector();
		prodRelacionadosNaoContempladosList = new Vector();
		itemPedidoNaoInseridoSugestaoPedList = new Vector(0);
		itemPedidoDivergenciaEstoqueSugestaoPedList = new Vector(0);
		itemPedidoConsumoVerbaSugestaoPedList = new Vector(0);
		itemPedidoEstoquePrevistoExcepList = new Vector(0);
		rentabilidadeFaixaList = new Vector(0);
		itemPedidoParticipacaoExtrapoladaList = new Vector(0);
		itemPedidoListCalculoTributacao = new Vector(0);
		itemPedidoDivergenciaDescontoSugestaoPedList = new Vector(0);
		descQuantidadePesoList = new Vector(0);
		docAnexoList = new Vector(0);
		fotoList = new Vector(0);
		pagamentoPedidoList = new Vector(0);
		notaCreditoPedidoList = new Vector(0);
		itemPedidoInseridosAdvertenciaList = new Vector(0);
		verificadoSePosuiFoto = false;
		statusPedidoPda = new StatusPedidoPda();
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			statusOrcamento = new StatusOrcamento();
		}
		if (LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao()) {
			pedidoRelacionadoList = new Vector();
		}
	}
	
	public Pedido(PedidoBuilder pedidoBuilder) {
		this();
		this.cdEmpresa = pedidoBuilder.cdEmpresa;
		this.cdRepresentante = pedidoBuilder.cdRepresentante;
		this.nuPedido = pedidoBuilder.nuPedido;
		this.flOrigemPedido = pedidoBuilder.flOrigemPedido;
		this.itemPedidoList = new Vector();
		this.itemPedidoList.addElement(pedidoBuilder.itemPedidoList);
	}
	
	public Pedido(PedidoDTO pedidoDTO) {
		this();
		try {
			FieldMapper.copy(pedidoDTO, this);
			if (pedidoDTO.itensPedidoErp != null && pedidoDTO.itensPedidoErp.length > 0) {
				int size = pedidoDTO.itensPedidoErp.length;
				for (int i = 0; i < size; i++) {
					itemPedidoList.addElement(new ItemPedido(pedidoDTO.itensPedidoErp[i]));
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof Pedido) {
			Pedido pedido = (Pedido) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, pedido.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, pedido.cdRepresentante) &&
			ValueUtil.valueEquals(nuPedido, pedido.nuPedido) &&
			ValueUtil.valueEquals(flOrigemPedido, pedido.flOrigemPedido);
		}
		return false;
	}

	public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
        return strBuffer.toString();
    }

	public String getDsStatusPedido() throws SQLException {
		return StatusPedidoPdaService.getInstance().getDsStatusPedido(cdStatusPedido);
	}
	
	public boolean isStatusPedidoComplementavel() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, statusPedidoPda.flComplementavel);
	}

	public String getDsOrigemPedido() {
		return OrigemPedido.getDsOrigemPedidoMessage(flOrigemPedido);
	}

	public double getVlPctDescProgressivo() throws SQLException {
		if (LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida()) {
			return getVlPctDescProgQtd();
		} else {
			if (vlTotalPedido != 0 && ((descProgressivo == null) || (!((descProgressivo.vlFinalFaixa > vlTotalPedido) && (descProgressivo.vlInicioFaixa < vlTotalPedido))))) {
				boolean updateVlPctDescProgressivo = !((descProgressivo == null) && vlPctDescProgressivo != 0);
				descProgressivo = DescProgressivoService.getInstance().getDescProgressivoPedido(this);
				if (updateVlPctDescProgressivo) {
					vlPctDescProgressivo = descProgressivo != null ? descProgressivo.vlPctDesconto : 0;
				}
			}
			if (descProgressivo == null) {
				return 0.0;
			} else {
				return descProgressivo.vlPctDesconto;
			}
		}
	}

	private double getVlPctDescProgQtd() throws SQLException {
		if (LavenderePdaConfig.anulaDescontoPessoaFisica && this.getCliente().isPessoaFisica()) {
			return 0.0;
		}
		if (vlTotalItens != oldVlTotalItens) {
	    	descProgQtd = DescProgQtdService.getInstance().getDescProgQtdPedido(getQtItensValidosAplicarDescProgressivo());
	    	if (descProgQtd == null) {
	    		descProgQtd = new DescProgQtd();
	    		descProgQtd.vlPctDesconto = 0.0;
	    	}
	    	oldVlTotalItens = vlTotalItens;
		}
		return descProgQtd.vlPctDesconto;
	}

	public double getQtItensValidosAplicarDescProgressivo() {
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			double sumQtItem = 0.0;
			int size = itemPedidoList != null ? itemPedidoList.size() : 0;
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)itemPedidoList.items[i];
				if (LavenderePdaConfig.isBloqueiaDescontoMaiorDescontoProgressivo()) {
					if (!itemPedido.isFlPrecoLiberadoSenha() || LavenderePdaConfig.usaItensLiberadosSenhaCalculoDescProgressivoPedido) {
						sumQtItem += itemPedido.qtItemFaturamento;
					}
				} else {
					if ((itemPedido.vlPctAcrescimo <= 0) && ((itemPedido.vlPctDesconto <= 0) && LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida())) {
						sumQtItem += itemPedido.qtItemFaturamento;
					}
				}
			}
			return sumQtItem;
		} else {
			return 0;
		}
	}

	public double getQtItensFaturamento() {
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			double sumQtItemFaturamento = 0.0;
			int size = itemPedidoList != null ? itemPedidoList.size() : 0;
			for (int i = 0; i < size; i++) {
				sumQtItemFaturamento += ((ItemPedido)itemPedidoList.items[i]).qtItemFaturamento;
			}
			return sumQtItemFaturamento;
		} else {
			return 0;
		}
	}
	
	public double getQtItensLista() {
		return getQtItensLista(itemPedidoList);
	}

	public double getQtItensLista(Vector itemPedidoList) {
		if (itemPedidoList == null) {
			itemPedidoList = this.itemPedidoList;
		}
		double sumQtItens = 0.0;
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			sumQtItens = getQtItensFromListaNormalOuTroca(itemPedidoList);
		} else if (ValueUtil.isNotEmpty(itemPedidoTrocaList)) {
			sumQtItens = getQtItensFromListaNormalOuTroca(itemPedidoTrocaList);
		} 
		return sumQtItens;
	}

	private double getQtItensFromListaNormalOuTroca(Vector itensList) {
		double sumQtItens = 0.0;
		int size = itensList != null ? itensList.size() : 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itensList.items[i];
			sumQtItens += itemPedido.getQtItemLista();
		}
		return sumQtItens;
	}

	public double getQtItensTrocaLista() {
		if (ValueUtil.isNotEmpty(itemPedidoTrocaList)) {
			double sumQtItens = 0.0;
			int size = itemPedidoTrocaList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoTrocaList.items[i];
				sumQtItens += itemPedido.getQtItemLista();
			}
			return sumQtItens;
		} else {
			return 0;
		}
	}

	public double getVlPctRentabilidade(boolean ignoreProdutoNeutro) throws SQLException {
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			return getVlPctRentabilidadeByConfigRentabilidadeNoPedido(ignoreProdutoNeutro);
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			return vlPctMargemRentab;
		}
		return 0d;
	}

	public double getVlPctRentabilidadeByConfigRentabilidadeNoPedido(boolean ignoreProdutoNeutro) throws SQLException {
		double vlRentabilidadePedido = ignoreProdutoNeutro ? vlRentabilidadeTotal : vlRentabilidade;
		if (LavenderePdaConfig.usaValorRentabilidadeSemCalculo()) {
			return vlRentabilidadePedido;
		}
		double vlTotalBaseItem = 0;
		ItemPedido itemPedido;
		int size = itemPedidoList != null ? itemPedidoList.size() : 0;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalBaseItem += getVlBaseItemRentabilidade(itemPedido, ignoreProdutoNeutro);
		}
		if (vlTotalBaseItem > 0) {
			return ValueUtil.round((vlRentabilidadePedido / vlTotalBaseItem) * 100);
		} 
		return 0;
	}
	
	private double getVlBaseItemRentabilidade(ItemPedido itemPedido, boolean ignoreProdutoNeutro) throws SQLException {
		if ((!ignoreProdutoNeutro && itemPedido.getProduto().isNeutro()) || itemPedido.isFazParteKitFechado()) return 0d;
		switch (LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido) {
			case 1:
				return ValueUtil.round(itemPedido.vlBaseItemPedido * itemPedido.getQtItemFisico());
			case 2:
				return ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
			case 3:
				return itemPedido.getVlTotalItemComTributos();
			case 4:
				return ItemPedidoService.getInstance().getVlPrecoTotalCustoItem(this, itemPedido);
			case 8:
				return ValueUtil.round(itemPedido.vlTotalItemPedido);
			default:
				return 0d;
		}
	}

	public Visita getVisita() {
		if (visita == null) {
			visita = new Visita();
		}
		return visita;
	}

	public void setVisita(Visita visita) {
		this.visita = visita;
	}

	private boolean isPedidoTrocaRecolher() {
		if (LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher != null) {
			if (LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher.indexOf(";") == -1) {
				return LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher.equals(cdTipoPedido);
			} else {
				String[] value = StringUtil.split(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher, ';');
				if (ValueUtil.isNotEmpty(value)) {
					for (int i = 0; i < value.length; i++) {
						String cdTipoPedidoFilter = value[i];
						if (cdTipoPedidoFilter.equals(cdTipoPedido)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isPedidoAberto() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoAberto);
	}

	public boolean isPedidoFechado() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoFechado);
	}

	public boolean isPedidoTransmitido() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoTransmitido);
	}
	
	public boolean isPedidoPendente() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoPendenteAprovacao);
	}
	
	public boolean isPedidoPendenteLimCredito() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoPendenteAprovacaoLimCredito);
	}
	
	public boolean isPedidoCancelado() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoCancelado);
	}
	
	public boolean isPedidoConsignado() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.cdStatusPedidoConsignado);
	}
	
	public boolean isPedidoPermiteConsignacao() throws SQLException {
		return StatusPedidoPdaService.getInstance().isPossuiStatusConsignacao() && getTipoPedido() != null && getTipoPedido().isPermiteConsignacao() && (getCliente().isPermiteConsignacao() || getTipoPedido().isObrigaConsignacao());
	}
	
	public boolean isPedidoConsignadoVencido() {
		if (ValueUtil.isNotEmpty(dtConsignacao)) {
			int dias = DateUtil.getDaysBetween(DateUtil.getCurrentDate(), dtConsignacao);
			return dias > LavenderePdaConfig.getNuDiasValidadePedidoEmConsignacao();
		}
		return false;
	}
	
	public double getVlPercentualDevolucaoAtingido(double vlDevolucoes) {
		return ValueUtil.round(vlDevolucoes * 100 / vlPedidoOriginal);
	}
	
	public boolean isPercentualDevolucaoClienteUltrapassada(double vlDevolucoes) throws SQLException {
		if (getCliente().vlPctDevolucaoConsig == 0) {
			return true;
		}
		if (vlPedidoOriginal > 0) {
			return getVlPercentualDevolucaoAtingido(vlDevolucoes) > getCliente().vlPctDevolucaoConsig;
		}
		return false;
	}
	
	public Date getDataVencimentoConsignacao() {
		Date dataVencimentoConsignacao = dtConsignacao;
		if (dataVencimentoConsignacao != null) {
			dataVencimentoConsignacao.advance(LavenderePdaConfig.getNuDiasValidadePedidoEmConsignacao());
		}
		return dataVencimentoConsignacao;
	}

	public boolean isPedidoTroca() throws SQLException {
		return !ValueUtil.isEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher) && getTipoPedido() != null && isPedidoTrocaRecolher();
	}

	public boolean isPedidoBonificacao() throws SQLException {
		return LavenderePdaConfig.isUsaPedidoBonificacao() && isTipoPedidoBonificacao();
	}

	public boolean isTipoPedidoBonificacao() throws SQLException {
		return getTipoPedido() != null &&  getTipoPedido().isBonificacao();
	}

	public boolean isPedidoValidaSaldoBonificacao() throws SQLException {
		return LavenderePdaConfig.validaSaldoPedidoBonificacao && getTipoPedido() != null &&  getTipoPedido().isBonificacao();
	}

	public boolean isPedidoIniciadoProcessoEnvio() {
		return FLTIPOALTERACAO_PROCESSANDO_ENVIO.equals(flTipoAlteracao);
	}

	public boolean isPedidoReplicado() {
		return ValueUtil.getBooleanValue(flPedidoReplicado);
	}

	public boolean isPedidoRelacionadoOrigemErp() {
		return OrigemPedido.FLORIGEMPEDIDO_ERP.equals(flOrigemPedidoRelacionado);
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		if (cliente != null) {
			cdCliente = cliente.cdCliente;
			flClienteAtrasadoLiberadoSenha = StringUtil.getStringValue(cliente.flClienteAtrasadoLiberadoSenha);
			flCreditoClienteLiberadoSenha = cliente.flCreditoClienteLiberadoSenha;
		}
	}

	public Cliente getCliente() throws SQLException {
		if (!ValueUtil.isEmpty(cdCliente) && ((cliente == null) || (!ValueUtil.valueEquals(cdCliente, cliente.cdCliente)))) {
			cliente = ClienteService.getInstance().getCliente(cdEmpresa, cdRepresentante, cdCliente);
		}
		return cliente;
	}
	
	public CentroCusto getCentroCusto() throws SQLException {
		if (ValueUtil.isNotEmpty(cdCentroCusto) && (centroCusto == null || centroCusto.equals(new CentroCusto()))) {
			centroCusto = CentroCustoService.getInstance().findCentroCusto(cdEmpresa, cdRepresentante, cdCentroCusto);
		}
		return centroCusto;
	}	

	public TabelaPreco getTabelaPreco() throws SQLException {
		if (!ValueUtil.isEmpty(cdTabelaPreco) && ((tabelaPreco == null) || (!ValueUtil.valueEquals(cdTabelaPreco, tabelaPreco.cdTabelaPreco)))) {
			tabelaPreco = TabelaPrecoService.getInstance().getTabelaPreco(cdTabelaPreco);
			if (LavenderePdaConfig.usarCondicaoPagtoPorTabelaPreco && (condicaoPagamentoDisponiveis != null)) {
				condicaoPagamentoDisponiveis = CondicaoPagamentoService.getInstance().loadCondicoesPagamento(this);
			}
		}
		return tabelaPreco;
	}
	
	public TabelaPreco getTabelaPrecoListaProdutosFilter(String cdTabPreco) throws SQLException {
		if (ValueUtil.isNotEmpty(cdTabPreco) && ((tabelaPrecoListaProdutosFilter == null) || (!cdTabPreco.equals(tabelaPrecoListaProdutosFilter.cdTabelaPreco)))) {
			tabelaPrecoListaProdutosFilter = TabelaPrecoService.getInstance().getTabelaPreco(cdTabPreco);
		}
		if (tabelaPrecoListaProdutosFilter == null) {
			tabelaPrecoListaProdutosFilter = new TabelaPreco();
			tabelaPrecoListaProdutosFilter.cdTabelaPreco = cdTabPreco;
		}
		return tabelaPrecoListaProdutosFilter;
	}

	public CondicaoPagamento getCondicaoPagamento() throws SQLException {
		if (ValueUtil.isNotEmpty(cdCondicaoPagamento) && ((condicaoPagamento == null) || (!cdCondicaoPagamento.equals(condicaoPagamento.cdCondicaoPagamento)) || ValueUtil.isEmpty(condicaoPagamento.dsCondicaoPagamento))) {
			condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(cdCondicaoPagamento);
		}
		if (condicaoPagamento == null || condicaoPagamento.cdEmpresa == null || (!LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao && isPedidoBonificacao())) {
			condicaoPagamento = new CondicaoPagamento();
			condicaoPagamento.cdCondicaoPagamento = cdCondicaoPagamento;
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public Vector getCondicoesPagamentoDisponiveis() throws SQLException {
		if (condicaoPagamentoDisponiveis == null) {
			condicaoPagamentoDisponiveis = CondicaoPagamentoService.getInstance().loadCondicoesPagamento(this);
		}
		return condicaoPagamentoDisponiveis;
	}

	public CondicaoComercial getCondicaoComercial() throws SQLException {
		if (!ValueUtil.isEmpty(cdCondicaoComercial) && ((condicaoComercial == null) || (!cdCondicaoComercial.equals(condicaoComercial.cdCondicaoComercial)))) {
			condicaoComercial = CondicaoComercialService.getInstance().getCondicaoComercial(cdCondicaoComercial);
			if (condicaoComercial == null) {
				condicaoComercial = new CondicaoComercial();
				condicaoComercial.cdCondicaoComercial = cdCondicaoComercial;
			}
		}
		if (condicaoComercial == null) {
			condicaoComercial = new CondicaoComercial();
		}
		return condicaoComercial;
	}
	
	public void setCondicaoComercial(CondicaoComercial condicaoComercial) {
		this.condicaoComercial = condicaoComercial;
	}

	public CondicaoNegociacao getCondicaoNegociacao() {
		return condicaoNegociacao;
	}

	public void setCondicaoNegociacao(CondicaoNegociacao condicaoNegociacao) {
		this.condicaoNegociacao = condicaoNegociacao != null ? condicaoNegociacao : new CondicaoNegociacao();
		this.cdCondNegociacao = this.condicaoNegociacao.cdCondicaoNegociacao;
	}

	public CondComCondPagto getCondComCondPagto() throws SQLException {
		if (!ValueUtil.isEmpty(cdCondicaoComercial) && !ValueUtil.isEmpty(cdCondicaoPagamento) &&
					 ((condComCondPagto == null) || (!cdCondicaoComercial.equals(condComCondPagto.cdCondicaoComercial)) || (!cdCondicaoPagamento.equals(condComCondPagto.cdCondicaoPagamento)))) {
			condComCondPagto = CondComCondPagtoService.getInstance().getCondComCondPagto(cdCondicaoComercial, cdCondicaoPagamento);
			if (condComCondPagto == null) {
				condComCondPagto = new CondComCondPagto();
				condComCondPagto.cdCondicaoComercial = cdCondicaoComercial;
				condComCondPagto.cdCondicaoPagamento = cdCondicaoPagamento;
			}
		}
		return condComCondPagto;
	}

	public TipoPagamento getTipoPagamento() throws SQLException {
		if (!ValueUtil.isEmpty(cdTipoPagamento) && ((tipoPagamento == null) || (!cdTipoPagamento.equals(tipoPagamento.cdTipoPagamento)))) {
			tipoPagamento = TipoPagamentoService.getInstance().getTipoPagamento(cdTipoPagamento);
		}
		if (ValueUtil.isEmpty(cdTipoPagamento) || (tipoPagamento == null)) {
			tipoPagamento = new TipoPagamento();
			tipoPagamento.cdTipoPagamento = cdTipoPagamento;
		}
		return tipoPagamento;
	}

	public TipoPedido getTipoPedido() throws SQLException {
		if (!ValueUtil.isEmpty(cdTipoPedido) && ((tipoPedido == null) || (!cdTipoPedido.equals(tipoPedido.cdTipoPedido)))) {
			tipoPedido = TipoPedidoService.getInstance().getTipoPedido(cdTipoPedido);
			if (LavenderePdaConfig.usaCondicaoPagamentoPorTipoPedido && (condicaoPagamentoDisponiveis != null)) {
				condicaoPagamentoDisponiveis = CondicaoPagamentoService.getInstance().loadCondicoesPagamento(this);
			}
		}
		return tipoPedido;
	}

	public boolean isOportunidade() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isOportunidade() && !getTipoPedido().isBonificacao();
	}

	public Transportadora getTransportadora() throws SQLException {
		if (!ValueUtil.isEmpty(cdTransportadora) && ((transportadora == null) || (!cdTransportadora.equals(transportadora.cdTransportadora)))) {
			transportadora = TransportadoraService.getInstance().getTransportadora(cdTransportadora);
		}
		return transportadora;
	}

	public TranspTipoPed getTranspTipoPed() throws SQLException {
		if (!ValueUtil.isEmpty(cdTransportadora) && ((transportadora == null) || (!cdTransportadora.equals(transportadora.cdTransportadora))) ||
					 !ValueUtil.isEmpty(cdTipoPedido) && ((tipoPedido == null) || (!cdTipoPedido.equals(tipoPedido.cdTipoPedido)))) {
			transpTipoPed = TranspTipoPedService.getInstance().getTranspTipoPed(cdTransportadora, cdTipoPedido);
		}
		return transpTipoPed;
	}

	public TipoFrete getTipoFrete() throws SQLException {
		if (!ValueUtil.isEmpty(cdTipoFrete) && ((tipoFrete == null) || (!cdTipoFrete.equals(tipoFrete.cdTipoFrete)))) {
			tipoFrete = TipoFreteService.getInstance().getTipoFrete(cdTipoFrete, getCliente().cdEstadoComercial);
		}
		return tipoFrete;
	}

	public InfoFretePedido getInfoFretePedido() throws SQLException {
		if (infoFretePedido == null) {
			infoFretePedido = InfoFretePedidoService.getInstance().getInfoFrete(cdEmpresa, cdRepresentante, nuPedido, flOrigemPedido);
		}
		if (infoFretePedido == null) {
			infoFretePedido = new InfoFretePedido();
		}
		return infoFretePedido;
	}
	
	public ItemLiberacao getItemLiberacao() throws SQLException {
		if (itemLiberacao == null) {
			itemLiberacao =  ItemLiberacaoService.getInstance().getItemLiberacaoByPedido(this);
		}
		if (itemLiberacao == null) {
			itemLiberacao = new ItemLiberacao();
		}
		return itemLiberacao;
	}
	
	public void setItemLiberacao(ItemLiberacao itemLiberacao) {
		this.itemLiberacao = itemLiberacao;
	}

	public Nfe getInfoNfe() throws SQLException {
		if ((!isPedidoAberto() || !isPedidoFechado())) {
			nfe = NfeService.getInstance().getNfe(cdEmpresa, cdRepresentante, nuPedido, flOrigemPedido);
			if (nfe == null) {
				nfe = NfeService.getInstance().getNfe(cdEmpresa, cdRepresentante, nuPedidoRelacionado, flOrigemPedidoRelacionado);
		}
		}
		return nfe == null ? new Nfe() : nfe;
	}

	public Nfce getInfoNfce() throws SQLException {
		if ((!isPedidoAberto() || !isPedidoFechado())) {
			nfce = NfceService.getInstance().getNfce(cdEmpresa, cdRepresentante, nuPedido, flOrigemPedido);
		}
		return nfce == null ? new Nfce() : nfce;
	}

	public Nfce getNfce() throws SQLException {
		if ((!isPedidoAberto() || !isPedidoFechado()) && nfce == null && nuPedido != null) {
			nfce = NfceService.getInstance().getNfce(cdEmpresa, cdRepresentante, nuPedido, flOrigemPedido);
		}
		return nfce == null ? new Nfce() : nfce;
	}

	public Vector getPedidoBoletoList() throws SQLException {
		if (!(isPedidoTransmitido() || isPedidoFechado() || isFlOrigemPedidoErp() || aptoImpressao)) {
			return new Vector();
		}
		if (ValueUtil.isEmpty(pedidoBoletoList)) {
			pedidoBoletoList = PedidoBoletoService.getInstance().getPedidoBoletoList(this);
		}
		return pedidoBoletoList;
	}
	
	public Vector getNotaFiscalList() throws SQLException {
		if (!isFlOrigemPedidoErp()) {
			return new Vector();
		}
		if (ValueUtil.isEmpty(notaFiscalList)) {
			notaFiscalList = NotaFiscalService.getInstance().findNotaFiscalList(cdEmpresa, cdRepresentante, nuPedido);
		}
		return notaFiscalList;
	}
	
	public Vector getAtividadePedidoList() throws SQLException {
		if (!isFlOrigemPedidoErp()) {
			return new Vector();
		}
		if (ValueUtil.isEmpty(atividadePedidoList)) {
			atividadePedidoList = AtividadePedidoService.getInstance().getAtividadePedidoList(this);
		}
		return atividadePedidoList;
	}
	
	public Vector getPedidoConsignacaoList() throws SQLException {
		if (!isPedidoConsignado()) {
			return new Vector();
		}
		if (ValueUtil.isEmpty(pedidoConsignacaoList)) {
			pedidoConsignacaoList = PedidoConsignacaoService.getInstance().findPedidoConsignacaoTipoInclusaoList(this);
		}
		return pedidoConsignacaoList;
	}
	
	public Vector getPedidoConsignacaoDevolucaoList() throws SQLException {
		if (!isPedidoConsignado()) {
			return new Vector();
		}
		if (ValueUtil.isEmpty(pedidoConsignacaoDevolucaoList)) {
			Vector pedidoConsignacaoDevList = PedidoConsignacaoService.getInstance().findItensDevolvidosPorPedido(this);
			pedidoConsignacaoDevolucaoList = PedidoConsignacaoService.getInstance().getListaAgrupadaPorProdutosEQuePossuamQtDevolvida(pedidoConsignacaoDevList);
		}
		return pedidoConsignacaoDevolucaoList;
	}
	
	public void setPedidoConsignacaoDevolucaoList(Vector pedidoConsignacaoDevolucaoList) {
		this.pedidoConsignacaoDevolucaoList = pedidoConsignacaoDevolucaoList;
	}

	public void setInfoFretePedido(String flTaxaEntrega, double vlTaxaEntrega, String flAjudante, int qtAjudante, String flAntecipaEntrega, String flAgendamento, String cdTipoVeiculo) {
		InfoFretePedido infoFretePedidoNew = new InfoFretePedido();
		infoFretePedidoNew.cdEmpresa = cdEmpresa;
		infoFretePedidoNew.cdRepresentante = cdRepresentante;
		infoFretePedidoNew.nuPedido = nuPedido;
		infoFretePedidoNew.flOrigemPedido = flOrigemPedido;
		infoFretePedidoNew.flTaxaEntrega = flTaxaEntrega;
		infoFretePedidoNew.vlTaxaEntrega = vlTaxaEntrega;
		infoFretePedidoNew.flAjudante = flAjudante;
		infoFretePedidoNew.qtAjudante = qtAjudante;
		infoFretePedidoNew.flAntecipaEntrega = flAntecipaEntrega;
		infoFretePedidoNew.flAgendamento = flAgendamento;
		infoFretePedidoNew.cdTipoVeiculo = cdTipoVeiculo;
		this.infoFretePedido = infoFretePedidoNew;
	}

	public boolean isTipoFreteFob() throws SQLException {
		TipoFrete tipoFrete = getTipoFrete();
		return tipoFrete != null && tipoFrete.isTipoFreteFob();
	}

	public boolean isFlPrecoLiberadoSenha() {
		return ValueUtil.VALOR_SIM.equals(flPrecoLiberadoSenha);
	}

	public boolean isFlCreditoClienteLiberadoSenha() {
		return ValueUtil.VALOR_SIM.equals(flCreditoClienteLiberadoSenha);
	}

	public double getVlEfetivo() throws SQLException {
		int size = itemPedidoList != null ? itemPedidoList.size() : 0;
		ItemPedido item;
		double vlEfetivo = 0;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido)itemPedidoList.items[i];
			vlEfetivo += item.getVlEfetivoTotal();
		}
		return vlEfetivo;
	}

	public double getVlEfetivoComSt() throws SQLException {
		int size = itemPedidoList != null ? itemPedidoList.size() : 0;
		ItemPedido item;
		double vlEfetivoComSt = 0;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido)itemPedidoList.items[i];
			vlEfetivoComSt += item.getVlEfetivoTotalComSt();
		}
		return vlEfetivoComSt;
	}
	
	public double getVlTotalPedidoGeral() {
		return vlTtPedidoComTributos == 0 ? vlTotalPedido : vlTtPedidoComTributos + vlFrete;
	}
	
	public double getVlTotalPedidoGeral(ItemPedido itemPedido) {
		return getVlTotalPedidoGeral() - ItemPedidoService.getInstance().getVlTotalItemsTroca(this, itemPedido);
	}
	
	public double getVlTotalBrutoItensComDesconto() {
		return vlTotalBrutoItens - vlDesconto;
	}

	public void removerItemPedido(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && isPedidoVenda() && itemPedido.isItemTrocaRecolher()) {
			itemPedidoTrocaList.removeElement(itemPedido);
		} else if (isPedidoTrocaRecolher() && itemPedido.isItemTrocaRecolher()) {
			itemPedidoTrocaList.removeElement(itemPedido);
		} else {
			itemPedidoList.removeElement(itemPedido);
		}
		itemPedidoAgrSimilares = null;
	}

    public int getQtItensValidacaoItensPromocionais() throws SQLException {
    	// Para um item do pedido ser considerado na validação de itens promocionais, ele não pode estar incluso em um kit válido.
    	Vector kitValidos = KitService.getInstance().findAllByTabPreco(cdTabelaPreco);
		int sizeKits = kitValidos.size();
		//--
		int size = itemPedidoList != null ? itemPedidoList.size() : 0;
		ItemPedido item;
		int qtItensValidos = 0;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido)itemPedidoList.items[i];
			if (item.getItemTabelaPreco().qtMaxVenda > 0 && !ValueUtil.VALOR_SIM.equals(flMaxVendaLiberadoSenha)) {
				continue;
			}
			//-- Verifica se o item não pertence a um kit
			boolean isPertenceKit = false;
			if (LavenderePdaConfig.isUsaKitProduto()) {
				for (int j = 0; j < sizeKits; j++) {
					ItemKit itemKitFilter = new ItemKit();
					Kit kit = (Kit)kitValidos.items[j];
					itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
					itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
					itemKitFilter.cdProduto = item.cdProduto;
					itemKitFilter.cdKit = kit.cdKit;
					itemKitFilter.cdTabelaPreco = ItemKit.CD_TABELA_PRECO_ZERO;
					itemKitFilter = (ItemKit) ItemKitService.getInstance().findByRowKey(itemKitFilter.getRowKey());
					if (itemKitFilter != null) {
						isPertenceKit = true;
						break;
					}
				}
			}
			//== Fim da verificação do item no kit
			if (!isPertenceKit) {
				qtItensValidos++;
			}
		}
		return qtItensValidos;
    }

    public boolean isWithItensPromocionais() throws SQLException {
    	// Para um item do pedido ser considerado na validação de itens promocionais, ele não pode estar incluso em um kit válido.
    	Vector kitValidos = KitService.getInstance().findAllByTabPreco(cdTabelaPreco);
		int sizeKits = kitValidos.size();
    	//--
		int size = itemPedidoList != null ? itemPedidoList.size() : 0;
		ItemPedido item;
		ItemTabelaPreco itemTabPreco;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido)itemPedidoList.items[i];
			itemTabPreco = item.getItemTabelaPreco();
			if (ValueUtil.VALOR_SIM.equals(itemTabPreco.flPromocao)) {
				if (itemTabPreco.qtMaxVenda > 0 && !ValueUtil.VALOR_SIM.equals(flMaxVendaLiberadoSenha)) {
					continue;
				}
				//-- Verifica se o item não pertence a um kit
				boolean isPertenceKit = false;
				if (LavenderePdaConfig.isUsaKitProduto()) {
					for (int j = 0; j < sizeKits; j++) {
						Kit kit = (Kit)kitValidos.items[j];
						ItemKit itemKitFilter = new ItemKit();
						itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
						itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
						itemKitFilter.cdProduto = item.cdProduto;
						itemKitFilter.cdKit = kit.cdKit;
						itemKitFilter = (ItemKit)ItemKitService.getInstance().findByRowKey(itemKitFilter.getRowKey());
						if (itemKitFilter != null) {
							isPertenceKit = true;
							break;
						}
					}
				}
				//== Fim da verificação do item no kit
				if (!isPertenceKit) {
					return true;
				}
			}
		}
		return false;
    }

    public boolean isFlOrigemPedidoPda() {
    	return OrigemPedido.FLORIGEMPEDIDO_PDA.equals(flOrigemPedido);
    }
    
    public boolean isFlOrigemPedidoErp() {
    	return OrigemPedido.FLORIGEMPEDIDO_ERP.equals(flOrigemPedido);
    }

	public String toString() {
    	if ("dtEmissao".equalsIgnoreCase(sortAttr)) {
    		return StringUtil.getStringValue(dtEmissao);
    	} else if ("hrEmissao".equalsIgnoreCase(sortAttr)) {
    		return StringUtil.getStringValue(hrEmissao);
    	}
    	return super.toString();
    }
	
	@Override
	public double getSortDoubleValue() {
		try {
			return getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return 0;
		}
	}
	
	@Override
	public int getSortIntValue() {
		return dtEmissao.getDateInt();
	}

	public boolean isIgnoraControleVerba() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isIgnoraControleVerba() || isPedidoCriticoOuConversaoFob() || VerbaGrupoSaldoService.getInstance().ignoraValidacaoVerbaSaldoPorGrupoProduto(this);
	}

	public boolean isSimulaControleVerba() throws SQLException {
		return ((LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex() || LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) && isPedidoVenda() && isPedidoAberto()) || (getTipoPedido() != null && getTipoPedido().isSimulaControleVerba());
	}

	public boolean hasErrosInsertSugestaoPedido() {
		return ValueUtil.isNotEmpty(itemPedidoNaoInseridoSugestaoPedList) || ValueUtil.isNotEmpty(itemPedidoDivergenciaEstoqueSugestaoPedList) || ValueUtil.isNotEmpty(itemPedidoConsumoVerbaSugestaoPedList) || ValueUtil.isNotEmpty(itemPedidoDivergenciaDescontoSugestaoPedList) || ValueUtil.isNotEmpty(itemPedidoInseridoDivergList);
	}

	public boolean isAtingiuPesoMinimo() throws SQLException {
		TipoFrete tipoFrete = getTipoFrete();
		TipoPedido tipoPedido = getTipoPedido();
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() && tipoFrete != null && tipoFrete.vlPesoMinimo != 0 && (tipoPedido != null && !tipoPedido.isIgnoraPesoMinFrete() || tipoPedido == null)) {
			return qtPeso >= getTipoFrete().vlPesoMinimo;
		}
		return true;
	}
	
	public CargaPedido getCargaPedido() throws SQLException {
		if (ValueUtil.isNotEmpty(cdCargaPedido) && ((cargaPedido == null) || (!cdCargaPedido.equals(cargaPedido.cdCargaPedido)))) {
			cargaPedido = CargaPedidoService.getInstance().getCargaPedido(cdCargaPedido);
		}
		if (cargaPedido == null) {
			return new CargaPedido();
		}
		return cargaPedido;
	}
	
	public Empresa getEmpresa() throws SQLException {
		if (ValueUtil.isNotEmpty(cdEmpresa) && ((empresa == null) || (!cdEmpresa.equals(empresa.cdEmpresa)))) {
			empresa = EmpresaService.getInstance().getEmpresa(cdEmpresa);
		}
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Vector getRentabilidadeFaixaList() throws SQLException {
		if (ValueUtil.isEmpty(rentabilidadeFaixaList)) {
			rentabilidadeFaixaList = RentabilidadeFaixaService.getInstance().getRentabilidadeFaixaList();
		}
		return rentabilidadeFaixaList;
	}

	public boolean isPagamentoAVista() {
		return ValueUtil.VALOR_SIM.equals(flPagamentoAVista);
	}
	
	public void setPagamentoAVista() throws SQLException {
		this.flPagamentoAVista = (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, getTipoPagamento().flPagamentoAVista)
				&& (cliente.isStatusBloqueado() || cliente.isStatusBloqueadoPorAtraso() || cliente.isAtrasado())) ?
				ValueUtil.VALOR_SIM : getCliente().flClienteLiberadoPedidoAVista;
	}

	public boolean isLiberadoEntrega() {
		return ValueUtil.VALOR_SIM.equals(flLiberadoEntrega);
	}
	
	public boolean isNfeImpressa() {
		return ValueUtil.VALOR_SIM.equals(flNfeImpressa);
	}
	
	public boolean isNfeContImpressa() {
		return ValueUtil.VALOR_SIM.equals(flNfeContImpressa);
	}
	
	public boolean isNfceImpressa() {
		return ValueUtil.VALOR_SIM.equals(flNfceImpressa);
	}

	public boolean isBoletoImpresso() {
		return ValueUtil.VALOR_SIM.equals(flBoletoImpresso);
	}
	
	public boolean isPedidoComplementar() throws SQLException {
		return LavenderePdaConfig.usaPedidoComplementar() && getTipoPedido() != null &&  getTipoPedido().isComplementar();
	}
	
	public boolean isPedidoConsignacaoImpresso() {
		return ValueUtil.VALOR_SIM.equals(flConsignacaoImpressa);
	}
	
	public String getCdLocalEstoque() throws SQLException {
		String cdLocalEstoque;
		if (LavenderePdaConfig.usaLocalEstoquePorTabelaPreco() && getTabelaPreco() != null) {
			cdLocalEstoque = getTabelaPreco().cdLocalEstoque;
		} else if (LavenderePdaConfig.usaLocalEstoquePorTipoPedido() && getTipoPedido() != null) {
			cdLocalEstoque = getTipoPedido().cdLocalEstoque;
		} else if (LavenderePdaConfig.usaLocalEstoquePorCliente() && getCliente() != null) {
			cdLocalEstoque = getCliente().cdLocalEstoque;
		} else if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto() && ValueUtil.isNotEmpty(cdCentroCusto)) {
			cdLocalEstoque = getCentroCusto().cdLocalEstoque;
		} else if (utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			Empresa empresa = new Empresa();
			empresa.cdEmpresa = cdEmpresa;
			cdLocalEstoque = EmpresaService.getInstance().getLocalEstoqueEmpresa();
		} else {
			cdLocalEstoque = Estoque.CD_LOCAL_ESTOQUE_PADRAO;
		}
		return cdLocalEstoque;
	}

	public boolean isPedidoVenda() throws SQLException {
		return !isPedidoBonificacao() && !isPedidoTroca() && !isPedidoComplementar() && !isOportunidade() && !isPedidoProducao();
	}
	
	public boolean isGeraCreditoCondicao() {
		return ValueUtil.VALOR_SIM.equals(flGeraCreditoCondicao);
	}
	
	public boolean isGeraCreditoFrete() {
		return ValueUtil.VALOR_SIM.equals(flGeraCreditoFrete);
	}
	
	public boolean isPedidoEmitidoNoMesCorrente() {
		return DateUtil.isAfterOrEquals(dtEmissao, DateUtil.getFirstUtilDayOfMonth(DateUtil.getCurrentDate())) && DateUtil.isBeforeOrEquals(dtEmissao, DateUtil.getLastDayOfMonth(DateUtil.getCurrentDate()));
	}
	
	public boolean isPedidoItemPendente() {
		return ValueUtil.getBooleanValue(flItemPendente);
	}
	
	public boolean isFlCotaCondPagto() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(this.flCotaCondPagto);
	}
	
	public double getValorTotalValidadoComImpostos() {
		if (LavenderePdaConfig.usaValorComImpostosControleValorMinimoPedido) {
			return this.vlTtPedidoComSt; 
		} 
		
		return this.vlTotalPedido;
	}
	public boolean isPedidoSemReservaEstoque() {
		return ValueUtil.VALOR_NAO.equals(flSituacaoReservaEst) || ValueUtil.isEmpty(flSituacaoReservaEst);
	}
	
	public boolean isPedidoComReservaEstoque() {
		return !isPedidoSemReservaEstoque();
	}
	
	public boolean isPendente() {
		return ValueUtil.getBooleanValue(flPendente);
	}
	
	public boolean isPendenteLimCred() {
		return ValueUtil.VALOR_SIM.equals(flPendenteLimCred);
	}
	
	public PagamentoPedido getPagamentoPedido() throws SQLException {
		if (debitoBancario == null) {
			debitoBancario = PagamentoPedidoService.getInstance().getPagamentoPedido(this);
		}
		return debitoBancario;
	}
	
	public void setPagamentoPedido(String nuBanco, String nuAgencia, String nuConta) throws SQLException {
		debitoBancario = getPagamentoPedido();
		debitoBancario.cdEmpresa = cdEmpresa;
		debitoBancario.cdRepresentante = cdRepresentante;
		debitoBancario.nuPedido = nuPedido;
		debitoBancario.flOrigemPedido = flOrigemPedido;
		debitoBancario.nuBanco = nuBanco;
		debitoBancario.nuAgencia = nuAgencia;
		debitoBancario.nuConta = nuConta;
		debitoBancario.cdCondicaoPagamento = cdCondicaoPagamento;
		debitoBancario.cdUsuario = cdUsuario;
	}
	
	public void setPagamentoPedidoClear() {
		debitoBancario = null;
	}
	
	public boolean isPedidoRestrito() {
		return ValueUtil.getBooleanValue(flRestrito);
	}
	
	public boolean isIgnoraControleEstoque() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isIgnoraControleEstoque();
	}
	
	public boolean isIgnoraControleEstoque(ItemPedido itemPedido) throws SQLException {
		return isIgnoraControleEstoque() || TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPedido.flTipoItemPedido);
	}
	
	public double getVlVerbaPedidoDisponivel() {
		if (LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
			return this.vlVerbaPedidoPositivo + this.vlVerbaPedido - this.vlDesconto;
		}
		return this.vlVerbaPedidoPositivo + this.vlVerbaPedido;
	}
	
	
	public String getNomeArquivoPdf() throws SQLException {
		String dtEmissaoStr = null;
		if (dtEmissao == null) {
			dtEmissaoStr = DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate());
		} else {
			dtEmissaoStr = DateUtil.formatDateDDMMYYYY(dtEmissao);
		}
		String isAuto = (isGeraPdfOfflineAuto ? "_auto" : "");
		isGeraPdfOfflineAuto = false;
		return nuPedido + "_" + dtEmissaoStr.replace("/", "-") + "_" + StringUtil.changeStringAccented(getCliente().nmRazaoSocial).replaceAll("[\\ \\/]", "_") + isAuto;
	}
	
	public String getDsFilePathPdfPedido() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfPedido/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}

	public String getDsFilePathPdfPedidoBoleto() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfPedidoBoleto/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}

	public boolean isPromissoriaImpressa() {
		return ValueUtil.getBooleanValue(flPromissoriaImpressa);
	}

	public boolean isIgnoraEstoqueReplicacao() throws SQLException {
		if (this.getTipoPedido() != null) {
			return this.getTipoPedido().isIgnoraEstoqueReplicacao();
		}
		return false;
	}

	public boolean isFlTipoCreditoAlcada() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isFlTipoCreditoAlcada();
	}

	public boolean isUsaBloqueioReplicacaoPedido() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isUsaBloqueioReplicacaoPedido();
	}
	
	public boolean isIgnoraVlMinPedidoTipoPedido() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isIgnoraVlMinPedido();
	}

	public String getFlModoEstoque() throws SQLException {
		if(LavenderePdaConfig.isFilterFlModoEstoque()) {
			return (getTipoPedido() == null) ? TipoPedido.TIPO_PEDIDO_REMESSA_ESTOQUE : getTipoPedido().getFlModoEstoque();
		}
		return null;
	}

	public boolean isPedidoAlteradoOrcamento() {
		return FLTIPOALTERACAO_ALTERADO_ORCAMENTO.equals(flTipoAlteracao);
	}
	

	public static boolean isUtilizaLocalEstoqueDaEmpresa(String modoEstoque) {
		return TipoPedido.TIPO_PEDIDO_EMPRESA_ESTOQUE.equals(modoEstoque);
	}

	public boolean utilizaEstoquePorLocalEstoqueDaEmpresa() {
		return LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.usaModoControleEstoquePorTipoPedido && isUtilizaLocalEstoqueDaEmpresa(flModoEstoque);
	}
	
	public boolean isPermiteInserirMultiplosItensPorVezNoPedido() throws SQLException {
		if (flSugestao) return true;
		boolean isPermiteInserirMultiplos = LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido(this) && !isPedidoBonificacao();
		if (isPermiteInserirMultiplos && getTipoPedido() != null) {
			isPermiteInserirMultiplos = getTipoPedido().isInsereLote() && !getTipoPedido().isIgnoraInsercaoMultiplosItens();
		}
		return isPermiteInserirMultiplos;
	}

	public boolean isConsignacaoObrigatoria() throws SQLException {
		if (getTipoPedido() != null) return LavenderePdaConfig.isUsaPedidoEmConsignacao() && ValueUtil.valueEquals("2", this.getTipoPedido().flConsignaPedido);
		return false;
	}

	public boolean isPossuiDiferenca() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flPossuiDiferenca);
	}
	
	public boolean isPossuiPedidoDiferenca() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flPedidoDiferenca);
	}
	public boolean isGeradoDiferenca() {
		return ValueUtil.isNotEmpty(nuPedidoDiferenca);
	}

	public void setSugestaoPedidoGiroProduto(boolean sugestaoPedidoGiroProduto) {
		this.sugestaoPedidoGiroProduto = sugestaoPedidoGiroProduto;
	}
	
	public boolean isSugestaoPedidoGiroProduto() {
		return LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto && sugestaoPedidoGiroProduto;
	}
	
	public boolean isPedidoNovoCliente() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flPedidoNovoCliente);
	}

	public String getcdTributacaoCliente() {
		return cdTributacaoCliente;
	}

	public void setcdTributacaoCliente(String cdTributacaoCliente) {
		this.cdTributacaoCliente = cdTributacaoCliente;
	}

	public boolean isDescontoLiberadoSenha() {
		return ValueUtil.getBooleanValue(flDescontoLiberadoSenha);
	}
	
	public boolean isValorMinParcelaLiberadoSenha() {
		return ValueUtil.getBooleanValue(flValorMinParcelaLiberadoSenha);
	}
	
	public boolean isGeraBoleto() {
		return ValueUtil.getBooleanValue(flGeraBoleto);
	}
	
	public boolean isGeraNfe() {
		return ValueUtil.getBooleanValue(flGeraNfe);
	}
	
	public String dsPedidoPrefixo() {
		return "Pedido " + nuPedido + " ";
	}

	public boolean isPossuiItensQueMantiveramPrecoNegociado() {
		return ValueUtil.isNotEmpty(this.itemPedidoPrecoNegociadoList);
	}

	public void setTipoPedido(TipoPedido tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public double getVlTotalDesconto() {
		if (itemPedidoList == null) return 0;
		double vlTotalDesconto = 0;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			double vlDesconto = itemPedido.vlBaseItemPedido - itemPedido.vlItemPedido;
			vlTotalDesconto += vlDesconto <= 0 ? vlDesconto : 0;
		}
		return vlTotalDesconto;
	}
	
	public Vector getCdLinhaProdutoList() throws SQLException {
		Vector cdLinhaProdutoList = new Vector();
		if (ValueUtil.isEmpty(this.itemPedidoList)) return cdLinhaProdutoList;
		
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			Produto produto = itemPedido.getProduto();
			if (produto == null) continue;
			
			String cdLinha = ValueUtil.isNotEmpty(produto.cdLinha) ? produto.cdLinha : ValueUtil.VALOR_ZERO;
			if (cdLinhaProdutoList.contains(cdLinha)) continue;
			
			cdLinhaProdutoList.addElement(cdLinha);
		}
		return cdLinhaProdutoList;
	}
	
	public double getVlTotalPromocional() {
		if (ValueUtil.isEmpty(itemPedidoList)) return 0;
		double vlTotalPromo = 0;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!ValueUtil.getBooleanValue(itemPedido.flPromocional)) continue;
			vlTotalPromo += itemPedido.vlTotalBrutoItemPedido >= 0 ? itemPedido.vlTotalBrutoItemPedido : 0;
		}
		return vlTotalPromo;
	}
	
	public FreteConfig getFreteConfig() throws SQLException {
		return getFreteConfig(true);
	}
	
	public FreteConfig getFreteConfig(boolean doLoadCalculoList) throws SQLException {
		if (freteConfig == null && cdFreteConfig != null) {
			FreteConfig filter = new FreteConfig(this.cdEmpresa, this.cdRepresentante, this.cdTransportadora);
			filter.cdFreteConfig = this.cdFreteConfig;
			freteConfig = FreteConfigService.getInstance().findFreteConfigByPrimaryKey(filter, doLoadCalculoList);
		}
		return freteConfig;
	}
	
	public boolean pedidoComFretePersonalizadoManual() {
		return LavenderePdaConfig.permitirFreteManual() && ValueUtil.isNotEmpty(this.cdTransportadora) && ValueUtil.isNotEmpty(this.cdTipoFrete) && ValueUtil.isEmpty(this.cdFreteConfig);
	}
	
	public boolean isCalculaSeguro() {
		return ValueUtil.getBooleanValue(flCalculaSeguro) || ValueUtil.isEmpty(flCalculaSeguro);
	}

	public String getCdContaCorrente() throws SQLException {
		if (LavenderePdaConfig.permiteHistoricoSaldoAntigo()) {
			if (ValueUtil.isEmpty(cdContaCorrente)) {
				VerbaSaldo verbaSaldoFilter = new VerbaSaldo();
				verbaSaldoFilter.setCdEmpresa(cdEmpresa);
				if (ValueUtil.isEmpty(cdSupervisor)) {
					verbaSaldoFilter.cdRepresentante = cdRepresentante;
				} else {
					verbaSaldoFilter.cdRepresentante = cdSupervisor;
				}
				verbaSaldoFilter.dtVigenciaInicial = DateUtil.getCurrentDate();
				cdContaCorrente = VerbaSaldoService.getInstance().maxByExample(verbaSaldoFilter, "cdContaCorrente");
			}
			return cdContaCorrente;
		} else {
			return VerbaSaldo.CDCONTACORRENTE_PADRAO;
		}
	}

	public boolean permiteLiberacaoPedidoBonificado() throws SQLException {
		return LavenderePdaConfig.isUsaMotivoPendencia() || !isPedidoBonificacao();
	}
	
	public ModoFaturamento getModoFaturamento() throws SQLException {
		if (ValueUtil.isEmpty(cdModoFaturamento)) return null;
		return ModoFaturamentoService.getInstance().findModoFaturamentoByCdModoFaturamento(cdModoFaturamento);
	}
	
	public boolean isPedidoVendaProducao() throws SQLException {
		return LavenderePdaConfig.usaConfigPedidoProducao() && isTipoPedidoVendaProducao();
	}
	
	public boolean isTipoPedidoVendaProducao() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isVendaProducao();
	}
	
	public boolean isPedidoProducao() throws SQLException {
		return LavenderePdaConfig.usaConfigPedidoProducao() && isTipoPedidoProducao();
	}
	
	public boolean isTipoPedidoProducao() throws SQLException {
		return getTipoPedido() != null && getTipoPedido().isProducao();
	}

	public void marcaFlPendente(ItemPedido itemPedido) {
		if (itemPedido.isPendente()) {
			this.flPendente = ValueUtil.VALOR_SIM;
			return;
		}
		int size = this.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoAdicionado = (ItemPedido) itemPedidoList.items[i];
			if (!itemPedidoAdicionado.isPendente() || ValueUtil.valueEquals(itemPedido, itemPedidoAdicionado)) continue;
				
			this.flPendente = ValueUtil.VALOR_SIM;
			return;
		}
		this.flPendente = ValueUtil.VALOR_NAO;
	}

	public boolean isExigeLiberacaoSomenteNivelAprovacao() throws SQLException {
		ItemPedido itemPedido = (ItemPedido) this.itemPedidoList.items[0];
		if (itemPedido == null) return false;
		
		itemPedido.motivoPendencia = MotivoPendenciaService.getInstance().findByMotivoPendencia(itemPedido.cdMotivoPendencia);
		return itemPedido.motivoPendencia.isExigeLiberacaoSomenteNivelAprovacao();
	}

	public boolean isGondola() {
		return LavenderePdaConfig.usaGondolaPedido && ValueUtil.getBooleanValue(flGondola);
	}

	public boolean isPedidoCritico() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flCritico);
	}

	public boolean isPendenteFob() {
		return ValueUtil.VALOR_SIM.equals(flPendenteFob);
	}

	public String[] getCdGrupoProduto1Array() throws SQLException {
		if (ValueUtil.isEmpty(itemPedidoList)) {
			return null;
		}
		int size = itemPedidoList.size();
		String[] cdGrupoProdutoArray = new String[size];
		for (int i = 0; i < size; i++) {
			cdGrupoProdutoArray[i] = ((ItemPedido) itemPedidoList.items[i]).getProduto().cdGrupoProduto1;
		}
		return cdGrupoProdutoArray;
	}

	public boolean isPedidoCriticoOuConversaoFob() throws SQLException {
		return isPedidoCritico() || (isTipoFreteFob() && LavenderePdaConfig.usaValidaConversaoFOB());
	}

	public double getVlTotalPedidoNaoNeutro() throws SQLException {
		double vlTotal = 0;
		ItemPedido item;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			item = (ItemPedido) itemPedidoList.items[i];
			if (item.getProduto().isNeutro()) continue;
			vlTotal += item.vlTotalItemPedido;
		}
		return vlTotal;
	}

	public boolean isFiltraTipoPagamentoQueNaoIgnoraLimiteCredito() {
		return filtraTipoPagamentoQueNaoIgnoraLimiteCredito;
	}

	public void setFiltraTipoPagamentoQueNaoIgnoraLimiteCredito(boolean filtraTipoPagamentoQueNaoIgnoraLimiteCredito) {
		this.filtraTipoPagamentoQueNaoIgnoraLimiteCredito = filtraTipoPagamentoQueNaoIgnoraLimiteCredito;
	}

	public boolean isUsaCodigoInternoCliente() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flCodigoInternoCliente);
	}
	
	private boolean isUtilizaRentabilidade() {
		return ValueUtil.isEmpty(flUtilizaRentabilidade) || ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flUtilizaRentabilidade);
	}
	
	public boolean isPermiteUtilizarRentabilidade() {
		return isUtilizaRentabilidade() || !LavenderePdaConfig.isPermiteNaoUtilizarRentabilidade();
	}

	public boolean isPedidoPerdido() {
		return ValueUtil.valueEquals(LavenderePdaConfig.cdStatusPedidoPerdido, cdStatusPedido);
	}

	public boolean isPedidoPossuiJustificativa() {
		return ValueUtil.isNotEmpty(cdMotivoPendenciaJust);
	}

	public boolean isPedidoPossuiMotivoPendencia() {
		return ValueUtil.isNotEmpty(cdMotivoPendencia);
	}

	public void setItemPedidoAgrSimilares(Vector itemPedidoAgrSimilares) {
		this.itemPedidoAgrSimilares = itemPedidoAgrSimilares;
	}

	public Vector getItemPedidoAgrSimilares() {
		if (itemPedidoAgrSimilares == null) {
			itemPedidoAgrSimilares = new Vector();
			try {
				ItemPedidoAgrSimilarService.getInstance().findItensAgrSimilarForItemPedidoList(this, itemPedidoAgrSimilares);
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
		return itemPedidoAgrSimilares;
	}

	public Vector getProdutoListInseridos() throws SQLException {
		int size = itemPedidoList.size();
		Vector produtoBaseList = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			Produto produto = itemPedido.getProduto();
			if (produto != null) {
				produtoBaseList.addElement(produto);
			}
		}
		return produtoBaseList;
	}

	public boolean isTipoPedidoPermiteComissao() throws SQLException {
		TipoPedido tipoPedido = getTipoPedido();
		return tipoPedido != null &&  !tipoPedido.isIgnoraCalculoComissao();
	}

	public boolean isTipoPedidoGeraNfeOuNfce() throws SQLException {
		TipoPedido tipoPedido = getTipoPedido();
		return tipoPedido != null && (tipoPedido.isGeraNfce() || tipoPedido.isGeraNfe());
	}

	public boolean tipoPedidoIgnoraCalculoComissao() throws SQLException {
		if (getTipoPedido() == null) return false;
		return getTipoPedido().isIgnoraCalculoComissao();
	}

	public boolean isPendenteCondPagto() {
		return ValueUtil.VALOR_SIM.equals(flPendenteCondPagto);
	}

	public boolean isPedidoPendenteLibRep() {
		return ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.getCdStatusLiberaRepCondPagto()) || ValueUtil.valueEquals(cdStatusPedido, LavenderePdaConfig.getCdStatusLiberaRepLimCred());
	}

	public boolean geraParcelasPorVencimento() throws SQLException {
		return ValueUtil.valueEquals(ValueUtil.getIntegerValue(getCondicaoPagamento().cdTipoCondPagto), CondicaoPagamento.TIPOCONDPAGTO_VENCIMENTO);
	}

	public boolean necessitaRecalculoPontuacao() {
		return ValueUtil.getBooleanValue(flCalculaPontuacao);
	}


	public boolean isConverteTipoPedidoReplicacao() throws SQLException {
		if (flConvertePedidoReplicacao == null) {
			flConvertePedidoReplicacao = StatusPedidoPdaService.getInstance().isConverteTipoPedidoReplicacao(cdStatusPedido);
		}
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flConvertePedidoReplicacao);
	}

	public boolean isEdicaoBloqueada() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, this.flEdicaoBloqueada);
	}

	public boolean isPedidoAbertoEditavel() {
		return isPedidoAberto() && !(isEdicaoBloqueada() || isFlOrigemPedidoErp() || parcelaMinMaxPendenteOuAutorizada);
	}

	public boolean isPedidoAbertoNaoEditavel() {
		return isPedidoAberto() && (isEdicaoBloqueada() || isFlOrigemPedidoErp() || parcelaMinMaxPendenteOuAutorizada);
	}

	public boolean isStatusPedidoNaoOcultaValoresComissao() throws SQLException {
		return (!LavenderePdaConfig.ocultaValoresComissao() && this.statusPedidoPda != null
					&& (ValueUtil.VALOR_NAO.equalsIgnoreCase(this.statusPedidoPda.flOcultaValoresComissao) || ValueUtil.isEmpty(this.statusPedidoPda.flOcultaValoresComissao) ))
				&& (getTipoPedido() != null && !getTipoPedido().isIgnoraCalculoComissao() && !isPedidoTroca());
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Vector getBonifCfgPedidoList() throws SQLException {
		if (bonifCfgPedidoList == null || forceRefreshBonifCfgPedidoList) {
			bonifCfgPedidoList = ItemPedidoBonifCfgService.getInstance().getSumVlBonicacaoByBonifCfgPedido(this);
			forceRefreshBonifCfgPedidoList = false;
		}
		return bonifCfgPedidoList;
	}

	/**
	 * Método usado somente em TestCase
	 * @param <tt>bonifCfgPedidoList</tt>
	 * **/
	public void setBonifCfgPedidoList(Vector bonifCfgPedidoList) {
		this.bonifCfgPedidoList = bonifCfgPedidoList;
	}

	public Vector getItemBrindeBonifCfgList() {
		return itemBrindeBonifCfgList;
	}

	public void setItemBrindeBonifCfgList(Vector itemBrindeBonifCfgList) {
		this.itemBrindeBonifCfgList = itemBrindeBonifCfgList;
	}

	public boolean isPedidoPendenteBonificacao() throws SQLException {
		return isPedidoBonificacao() && LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao();
	}

	public String getNomeArquivoCatalogoPedidoPdf() throws SQLException {
		String dtEmissaoStr = null;
		if (dtEmissao == null) {
			dtEmissaoStr = DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate());
		} else {
			dtEmissaoStr = DateUtil.formatDateDDMMYYYY(dtEmissao);
		}
		return nuPedido + "_" + dtEmissaoStr.replace("/", "-") + "_" + StringUtil.changeStringAccented(getCliente().nmRazaoSocial).replaceAll("[\\ \\/]", "_");
	}

	public String getDsFilePathCatalogoPedidoPdf() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "PdfCatalogoPedido/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Settings.appPath + "/" + filePath;
		}
	}
	
	public Vector getOnlyItemPedidoPendenteList() {
		Vector itemPedidoPendenteList = new Vector(0);
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (itemPedido.isPendente()) {
				itemPedidoPendenteList.addElement(itemPedido);
			}
		}
		return itemPedidoPendenteList;
	}
	
	public Vector getOnlyItemPedidoDescontoMaximoUltrapassadoList() throws SQLException {
		Vector itemPedidoDescontoMaximoUltrapassadoList = new Vector(0);
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			double vlPctMaxDescontoItem = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			if (vlPctMaxDescontoItem != 0 && itemPedido.vlPctDesconto > vlPctMaxDescontoItem) {
				itemPedidoDescontoMaximoUltrapassadoList.addElement(itemPedido);
			}
		}
		return itemPedidoDescontoMaximoUltrapassadoList;
	}
	
}
