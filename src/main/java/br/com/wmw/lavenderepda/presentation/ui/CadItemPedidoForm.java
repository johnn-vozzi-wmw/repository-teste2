package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseCrudForm;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.GridEditEvent;
import br.com.wmw.framework.presentation.ui.event.ImageCarrouselEvent;
import br.com.wmw.framework.presentation.ui.event.ItemContainerChangeEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.ValueChooser;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.Tree;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CestaPositProduto;
import br.com.wmw.lavenderepda.business.domain.CestaProduto;
import br.com.wmw.lavenderepda.business.domain.CestaPromocional;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.DescPromocionalGrade;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.DescVidaUtilGrupo;
import br.com.wmw.lavenderepda.business.domain.DescontoGrupo;
import br.com.wmw.lavenderepda.business.domain.DescontoPacote;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.HistoricoTrocaDevolucaoProd;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.LoteProduto;
import br.com.wmw.lavenderepda.business.domain.MetasPorGrupoProduto;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoIndustria;
import br.com.wmw.lavenderepda.business.domain.ProdutoRelacionado;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaCli;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoRegistro;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.AreaVendaClienteService;
import br.com.wmw.lavenderepda.business.service.AreaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.CalculaEmbalagensService;
import br.com.wmw.lavenderepda.business.service.CanalCliGrupoService;
import br.com.wmw.lavenderepda.business.service.CestaPromocionalService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ComboService;
import br.com.wmw.lavenderepda.business.service.ComiRentabilidadeService;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.DescComiFaixaService;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalGradeService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.DescQuantidadeService;
import br.com.wmw.lavenderepda.business.service.DescVidaUtilGrupoService;
import br.com.wmw.lavenderepda.business.service.DescontoGrupoService;
import br.com.wmw.lavenderepda.business.service.DescontoPacoteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoEmpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.HistoricoTrocaDevolucaoProdService;
import br.com.wmw.lavenderepda.business.service.ItemComboService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemKitPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoLogService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.KitService;
import br.com.wmw.lavenderepda.business.service.LocalService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.business.service.MenuCategoriaService;
import br.com.wmw.lavenderepda.business.service.MetasPorGrupoProdutoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoKitService;
import br.com.wmw.lavenderepda.business.service.ProdutoMenuCategoriaService;
import br.com.wmw.lavenderepda.business.service.ProdutoRelacionadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.RestricaoVendaCliService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.SugVendaPersonService;
import br.com.wmw.lavenderepda.business.service.TabPrecoLoteProdService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.TermoCorrecaoService;
import br.com.wmw.lavenderepda.business.service.TributacaoService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.business.service.UsuarioRelRepService;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaService;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.business.validation.ItemKitPedidoInseridoException;
import br.com.wmw.lavenderepda.business.validation.ValidateProdutoInPlataFormaVendaProdutoException;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoOpcaoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CestaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescProgFamiliaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescProgressivoConfigComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescPromocionalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.EstoqueDisponivelComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.LocalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.NaoPositivadoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PacoteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoDestaqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoUnidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoDescProgConfigFamComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ButtonMenuCategoria;
import br.com.wmw.lavenderepda.presentation.ui.ext.CadItemPedidoFormWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderProdutoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainerControlState;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.MenuCategoriaScrollContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.WindowUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.io.device.scanner.Scanner;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.MainWindow;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.event.ListContainerEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.ui.media.Sound;
import totalcross.util.IntVector;
import totalcross.util.Random;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

public class CadItemPedidoForm extends AbstractBaseCadItemPedidoForm {
	
	private static final String HASHKEY_PA = "PA";
	private static final String HASHKEY_AP = "AP";
	private static final String HASHKEY_DS = "DS";
	private static final String HASHKEY_CD = "CD";
		
	private static final String MOSTRA_TODOS_CAMPOS_GRID_LOTE = "0";
	private static final String CONFIG_COLUNA_LOCAL_GRID_LOTE = "1";
	private static final String CONFIG_COLUNA_DESCRICAO_GRID_LOTE = "2";
	private static final String CONFIG_COLUNA_DATA_VALIDADE_GRID_LOTE = "3";
	private static final String CONFIG_COLUNA_PERCENTUAL_VIDA_GRID_LOTE = "4";
	private static final String CONFIG_COLUNA_ESTOQUE_GRID_LOTE = "5";
	private static final String CONFIG_COLUNA_RESERVA_GRID_LOTE = "6";
	private static final String CONFIG_COLUNA_PERCENTUAL_DESCONTO_GRID_LOTE = "7";
	private static final String CONFIG_COLUNA_PRECO_LOTE = "8";
	private static final int COLUNA_LOTE = 0;
	private static final int COLUNA_DT_VALIDADE = 1;
	private static final int COLUNA_VL_PCTVIDAUTILPRODUTO = 2;
	private static final int COLUNA_ESTOQUE = 3;
	private static final int COLUNA_ESTOQUE_RESERVADO = 4;
	private static final int COLUNA_VL_PCT_DESCONTO = 5;
	private static final int COLUNA_VL_BASE_ITEM_STRING = 6;

	public static final String NU_FILTRO_FORNECEDOR = "1";
	public static final String NU_FILTRO_GRUPOPRODUTO = "2";
	public static final String NU_FILTRO_ATRIBUTO_PRODUTO = "3";
	public static final String NU_FILTRO_AVISO_PRE_ALTA = "4";
	public static final String NU_FILTRO_APENAS_KIT = "5";
	public static final String NU_FILTRO_KIT = "6";
	public static final String NU_FILTRO_CAMPANHA_VENDAS = "7";
	public static final String NU_FILTRO_PRODUTO_PROMOCIONAL = "8";
	public static final String NU_FILTRO_PRODUTO_DESC_PROMOCIONAL = "9";
	public static final String NU_FILTRO_PRODUTO_GRUPO_DESTAQUE = "10";
	public static final String NU_FILTRO_PRODUTO_OPORTUNIDADE = "11";
	public static final String NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE = "12";
	public static final String NU_FILTRO_GRUPO_DESCONTO_PRODUTO = "13";
	public static final String NU_FILTRO_TIPO_RANKING = "14";
	public static final String NU_FILTRO_COMISSAO = "15";
	public static final String NU_FILTRO_LOCAL = "16";
	public static final String NU_FILTRO_DESCMAX_PRODUTO_CLIENTE = "17";
	public static final String NU_FILTRO_PRODUTO_CBPACOTE = "18";
	public static final String NU_FILTRO_DESC_PROGRESSIVO_PERSONALIZADO = "19";
	public static final String NU_FILTRO_ESTOQUE_DISPONIVEL = "20";
	public static final String NU_FILTRO_PRODUTO_CBMARCADOR = "21";
	public static final String NU_FILTRO_ITENS_INSERIDOS = "22";
	public static final String NU_FILTRO_PRODUTO_COM_DESCQTD = "23";

	private static final String MSG_ERRO_ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO = "Desconto informado ultrapassou o permitido";

	public boolean fromProdutoRelacionadoWindowOnFechamento = false;
	public boolean fromProdutoRelacionadoWindow = false;
	public boolean fromWindowSugestaoProduto = false;
	public boolean fromWindowSugestaoVendaComQtde = false;
	public boolean fromWindowMultiplaSugestaoProduto = false;
	public boolean fromWindowMultiplaSugestaoNovoItemProduto = false;
	public boolean fromWindowSugestaoItensRentIdealOnFechamento = false;
	public boolean fromWindowItemPedidoAbaixoPesoMinimo = false;
	public boolean fromWindowItemPedidoAbaixoValorMinimo = false;
	public boolean fromWindowProdutoSimilar = false;
	public boolean fromWindowDesconto = false;
	public boolean showPopUpCredDesc = false;
	private boolean showListDescontoCredito = false;
	private boolean fromBtNovoItemOrFechamentoPed;
	public boolean fromSugestaoItemComboFechamento;
	public boolean fromSugestaoMultProdutos;
	public boolean fromProdutoPendenteWindow = false;

	public BaseCrudForm editingExternalForm;

	private ListProdutoSugestaoSemQtdeWindow listProdutoSugestaoSemQtdeWindow;
	private ListProdutoSugestaoComQtdeWindow listProdutoSugestaoComQtdeWindow;
	private ListProdutoSugestaoDifPedidoWindow listProdutoSugestaoDifPedidoWindow;
	private ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutosWindow;
	private ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutosNovoItemWindow;
	private ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutosVisualizacaoWindow;
	private ListItemPedidoAbaixoPesoMinimoWindow listItemPedidoAbaixoPesoMinimoWindow;
	private ListItemPedidoAbaixoValorMinimoWindow listItemPedidoAbaixoValorMinimoWindow;
	private ListSugestaoMultProdutosWindow listSugestaoPersonMultProdutosWindow;
	private boolean verificaProdutoGenerico;
	public boolean filterByPrincipioAtivo;
	public boolean filterByAplicacaoProduto;
	public boolean filterByCodigoProduto;
	
	public boolean ignoraTrocaTabsAuto = false;
	protected boolean listProdutoSimilarInicialized = false;
	private TabelaPreco tabelaPrecoSelecionada;
	private double valueUltimoDescontoNegociado;
	private double valueUltimoDesconto2Negociado;
	private double valueUltimoDesconto3Negociado;
	private double qtItemFisicoMultiplosItens;
	private double vlPctDescontoMultiplosItens;
	private boolean maiorDesc;
	public boolean selectDesconto;
	public boolean selectDescontoPacote;
	public boolean sugereCadastroProdutoDesejado;
	public Vector descontoQtdeGrupoProdutoList;
	private String[] limiteAviso;
	public boolean salvandoItemPelaListaComCdBarras;
	private ButtonAction btDescontos;
	private String ultimoCdProduto;
	private boolean usandoPopupDescQtdItemPedido;
	private boolean usandoPopupDescPacote;
	private boolean refreshCurrentItemMultiplaInsercaoList;

	protected PushButtonGroupBase btGroupTipoFiltros;
	protected PushButtonGroupBase btTipoPesquisaEdFiltro;
	protected BaseButton btFiltroAvancado;
	private ButtonAction btDescComi;
	private ButtonAction btBonificar;
	private ButtonAction btGiroProduto;
	public AtributoProdComboBox cbAtributoProd;
	public AtributoOpcaoProdComboBox cbAtributoOpcaoProd;
	public CheckBoolean ckPreAltaProduto;
	public CheckBoolean ckApenasKit;
	public CheckBoolean ckProdutoPromocional;
	public CheckBoolean ckProdutoOportunidade;
	private CheckBoolean ckApenasProdutoVendido;
	private CheckBoolean ckMaxDescProdCli;
	private CheckBoolean ckApenasItensAdicionados;
	private BaseButton btFornecedorAnterior;
	private BaseButton btFornecedorProximo;
	private BaseButton btGrupoProdutoAnterior;
	private BaseButton btGrupoProdutoProximo;
	public CestaComboBox cbCesta;
	public NaoPositivadoComboBox cbNaoPositivado;
	public ProdutoDestaqueComboBox cbProdutoGrupoDestaque;
	public PacoteComboBox cbPacote;
	public EstoqueDisponivelComboBox cbEstoqueDisponivel;
	private LabelName lbPacote;
	private LabelName lbAtributoProduto;
	private LabelName lbProdutoGrupoDestaque;
	public BaseGridEdit gridDescCond;
	public BaseGridEdit gridLoteProduto;
	public BaseGridEdit gridTabelaPreco;
	public BaseGridEdit gridHistTrocaDevProduto;
	private LabelName lbDescPromocional;
	public DescPromocionalComboBox cbDescPromocional;
	public LabelName lbTipoRanking;
	public Vector nmImageList;
	public LabelName lbComissaoMinima;
	public LabelName lbComissaoMaxima;
	public EditNumberFrac edComissaoMinima;
	public EditNumberFrac edComissaoMaxima;
	public LabelName lbComissaoItem;
	public LabelName lbVlPctComissaoPedidoIntervalo;
	public CadProdutoDynForm cadProdutoDynForm;
	
	private boolean conferirInconsistencias;
	private HashMap<String ,Item> itemContainerErroMap;
	private HashMap<String, Integer> posicoesbtGroupTipoFiltros = new HashMap<>();
	private ItemPedido itemPedidoInfoAdic;
	public boolean fromSimilar;
	
	private Map<String, Boolean> filtrosVisiveisMap = new HashMap<>();
	private Map<String, Boolean> filtrosNaoAutomaticosMap = new HashMap<>();
	private Map<String, Integer> hashIndexItensGridLote;

	private HashMap<String, ItemPedido> itensForTotalizadores;
	private HashMap<String, ItemPedido> itensListaForTotalizadores;
	
	private MenuCategoriaScrollContainer menuCategoriaScroll;
	private ButtonMenuCategoria dynamicButtonMenuCategoria;
	private Tree<ButtonMenuCategoria> buttonMenuCategoriaTree;
	private Tree<ButtonMenuCategoria> currentButtonMenuCategoriaTree;
	private LabelName lbCurrentCategoria;
	private LabelValue lvCurrentCategoria;
	private int qtButtonCategoriaPrevious;
	private boolean menuCategoriaHidden;
	private boolean menuCategoriaStarted;
	private BaseToolTip menuCategoriaAtualToolTip;

	private ProdutoIndustria produtoIndustriaFilter;
	private ButtonAction btLibPrecoSolAutorizacao;
	private ButtonAction btEditarKit;
	private MarcadorMultiComboBox cbMarcador;

	private DescProgressivoConfigComboBox cbDescProgressivoConfig;
	private TipoDescProgConfigFamComboBox cbDescProgConfigFam;
	private DescProgFamiliaComboBox cbDescProgFamilia;

	public ItemPedido itemPedidoAgrupadorSugPerson;
	private Vector itensErro;
	private boolean houveAlteracaoItemPorMultInsercao;
	public Vector itemPedidoListAposRecalculoDescProgressivo;
	public boolean atualizouDescProgressivo;

	public boolean possuiHistoricoTrocadevolucao;
	public boolean autoOpenDistSimilar;
	private boolean filtrandoItemEventoEmProgresso;
	private String filtroItemEventoEmProgresso;
	public boolean isOcultaProdutoPendenteMenuOpcoes = false;
	
	public static CadItemPedidoForm getInstance(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		if (instance == null || !(instance instanceof CadItemPedidoForm)) {
			instance = new CadItemPedidoForm(cadPedidoForm, pedido);
			listInicialized = true;
		} else { 
			CadItemPedidoForm cadItemPedidoForm = (CadItemPedidoForm) instance;
			cadItemPedidoForm.configure(cadPedidoForm, pedido);
			cadItemPedidoForm.limpaFiltros();
		}

		return (CadItemPedidoForm) instance;
	}

	public static CadItemPedidoForm getNewCadItemPedido(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		return new CadItemPedidoForm(cadPedidoForm, pedido);
	}

	public static void invalidateInstance() {
		instance = null;
	}
	
	private void configure(CadPedidoForm cadPedidoForm, Pedido pedido) {
		this.cadPedidoForm = cadPedidoForm;
		this.pedido = pedido;
		defineNuSeqItemPedidoAoIniciar(pedido);
	}

	private CadItemPedidoForm(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		super(pedido.isSugestaoPedidoGiroProduto() ? Messages.GIROPRODUTO_ITEM_PEDIDO_NOME_ENTIDADE : Messages.ITEMPEDIDO_NOME_ENTIDADE, pedido, cadPedidoForm);
		configure(cadPedidoForm, pedido);
		useScanner = true;
		valueUltimoDescontoNegociado = LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() ? pedido.vlPctDescItem : 0;
		selectDesconto = selectDescontoPacote = false;
		dsFiltro = "";
		//- CONTAINERS
		if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && !(LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.restringeItemPedidoPorLocal)) {
			fotosProdSugList = new Vector(LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto);
			for (int i = 0; i < LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto; i++) {
				ImageCarrousel imgCarrousel = new ImageCarrousel(Produto.getPathImg(), true);
				imgCarrousel.showArrowButtons = false;
				imgCarrousel.setFont(Font.getFont(MainWindow.getDefaultFont().name, false, (int)(BaseMainWindow.getDefaultFont().size * (VmUtil.isWinCEPocketPc() ? 0.65 : 0.6))));
				imgCarrousel.useGapTitle = true;
				imgCarrousel.showContagemPagina = false;
				fotosProdSugList.addElement(imgCarrousel);
			}
			nmImageList = new Vector(fotosProdSugList.size());
		}
		if (LavenderePdaConfig.usaCategoriaInsercaoItem()) {
			menuCategoriaScroll = new MenuCategoriaScrollContainer(false, true);
			dynamicButtonMenuCategoria = new ButtonMenuCategoria(true);
			dynamicButtonMenuCategoria.configuraMenuCategoriaPrincipal();
			if (dynamicButtonMenuCategoria.usaBtPrincipal) {
				dynamicButtonMenuCategoria.setTextImage(dynamicButtonMenuCategoria.principalText, dynamicButtonMenuCategoria.principalImage);
			} else {
				dynamicButtonMenuCategoria.setTextImage(Messages.MENU_CATEGORIA_LABEL, null);
			}
			lbCurrentCategoria = new LabelName(Messages.MENU_CATEGORIA_CATEGORIA_ATUAL);
			lvCurrentCategoria = new LabelValue();
			menuCategoriaAtualToolTip = new BaseToolTip(lvCurrentCategoria, "");
			menuCategoriaAtualToolTip.millisDisplay = 6000;
			lvCurrentCategoria.align = RIGHT;
		}
		//- BUTTONS
		btDescComi = new ButtonAction("");
		btBonificar = new ButtonAction(Messages.BOTAO_BONIFICAR, "images/bonificacao.png");
		if (LavenderePdaConfig.usaBotaoGiroProdutoItemPedido) { 
			btGiroProduto = new ButtonAction(""); 
		} 
        btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
        if (LavenderePdaConfig.usaFiltroAtributoProduto) {
            lbAtributoProduto = new LabelName(Messages.ITEMPEDIDO_LABEL_ATRIBUTO_OPCAO);
        }
    	ckPreAltaProduto = new CheckBoolean(Messages.PRODUTO_LABEL_PRE_ALTA_CUSTO);
    	ckApenasKit = new CheckBoolean(Messages.PRODUTOKIT_FILTRO);
    	//--
        ckProdutoPromocional = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_PROMOCIONAL);
        ckProdutoOportunidade = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_OPORTUNIDADE);
        ckApenasProdutoVendido = new CheckBoolean(Messages.PRODUTOVENDIDO_FILTRO);
        ckMaxDescProdCli = new CheckBoolean(Messages.DESCONTO_MAX_PRODCLI);
        ckApenasItensAdicionados = new CheckBoolean(Messages.FILTRO_APENAS_PRODUTOS_PEDIDO);
        ckApenasItensAdicionados.setID("ckApenasItensAdicionados");
        //--
		cbAtributoProd = new AtributoProdComboBox(LavenderePdaConfig.usaFiltroAtributoProduto ? Messages.ITEMPEDIDO_LABEL_ATRIBUTO : "");
		cbAtributoProd.load();
		cbAtributoOpcaoProd = new AtributoOpcaoProdComboBox(LavenderePdaConfig.usaFiltroAtributoProduto ? Messages.ITEMPEDIDO_LABEL_ATOPCAO : "");
		cbCesta = new CestaComboBox(lbCesta.getValue(), true, SessionLavenderePda.getCliente().cdCliente);
		cbCesta.setSelectedIndex(0);
		cbNaoPositivado = new NaoPositivadoComboBox();
		inicializaBtGroupTipoFiltros ();
    	lbProdutoGrupoDestaque = new LabelName(Messages.PRODUTO_LABEL_PRODUTO_DESTAQUE);
    	cbProdutoGrupoDestaque = new ProdutoDestaqueComboBox();
    	if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			cbPacote = new PacoteComboBox();
		}

    	lbPacote = new LabelName(Messages.DESCONTO_PACOTE_LABEL_COMBO);
		lbDescPromocional = new LabelName(Messages.PRODUTO_LABEL_DESC_PROMOCIONAL);
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox()) {
		cbDescPromocional = new DescPromocionalComboBox(pedido, null);
		}
		lbTipoRanking = new LabelName(Messages.TIPORANKING_ORDENACAO_RANKING);
		cbMarcador = new MarcadorMultiComboBox();
		btDescontos = new ButtonAction(Messages.BT_PRODUTOCREDITODESCONTO, "images/descontoCredito.png");
		if (exibeComboLocal()) {
			cbLocal = new LocalComboBox();
		}
		
		final String editNumberInicial = "9999999999";
		lbComissaoMinima = new LabelName(Messages.PRODUTO_LABEL_COMISSAO_MINIMA);
		lbComissaoMaxima = new LabelName(Messages.PRODUTO_LABEL_COMISSAO_MAXIMA);
		edComissaoMinima = new EditNumberFrac(editNumberInicial, 9);
		edComissaoMaxima = new EditNumberFrac(editNumberInicial, 9);
		lbComissaoItem = new LabelName(Messages.PRODUTO_LABEL_COMISSAO);
		
		lbVlPctComissaoPedido = new LabelName(Messages.PRODUTO_LABEL_PERCENTUAL_COMISSAO);
		lbVlPctComissaoPedidoIntervalo = new LabelName(Messages.PRODUTO_LABEL_PERCENTUAL_COMISSAO_INTERVALO);
		
		if (LavenderePdaConfig.exibeOpcoesNavegacaoNasCombos) {
			btFornecedorAnterior = new BaseButton("", UiUtil.getColorfulImage("images/previous.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
			btFornecedorProximo = new BaseButton("", UiUtil.getColorfulImage("images/next.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
			btGrupoProdutoAnterior = new BaseButton("", UiUtil.getColorfulImage("images/previous.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
			btGrupoProdutoProximo = new BaseButton("", UiUtil.getColorfulImage("images/next.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			cbDescProgressivoConfig = new DescProgressivoConfigComboBox(pedido.getCliente());
			cbDescProgConfigFam = new TipoDescProgConfigFamComboBox(true);
			cbDescProgFamilia = new DescProgFamiliaComboBox();
		}
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
			cbMarcador.load();
		}
		cbEstoqueDisponivel = new EstoqueDisponivelComboBox();
		criaGridLoteProduto();
		if (LavenderePdaConfig.isExibeHistoricoProduto()) {
			criaGridHistoricoTrocaDevolucaoProd();
	}
	}
	
	private void inicializaBtGroupTipoFiltros() {
		List<String> list = new ArrayList<String>();

		if(LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO);
		}
		if(LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO);
	}
		if(LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO);
		}
		list.add(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO);
		btGroupTipoFiltros = new PushButtonGroupBase(list.toArray(new String[0]), true, 1, -1, 1, 1, true, PushButtonGroup.NORMAL);
		setaPosicoesHashMapbtGroupTipoFiltros(list);

		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
			btTipoPesquisaEdFiltro = new PushButtonGroupBase(new String[] {Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_CONTEM, Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_INICIA}, true, 0, -1, 1, 1, true, PushButtonGroup.NORMAL);
		}
		btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
	}

	private void setaPosicoesHashMapbtGroupTipoFiltros (List<String> list){
		 for (int i = 0; i < list.size(); i++) {
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO)) {
					posicoesbtGroupTipoFiltros.put(HASHKEY_PA, i);
					continue;
				}
				else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_PA)) {
			posicoesbtGroupTipoFiltros.put(HASHKEY_PA, -1);
				}
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO)) {
					posicoesbtGroupTipoFiltros.put(HASHKEY_AP, i);
					continue;
				}
				else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_AP)) {
			posicoesbtGroupTipoFiltros.put(HASHKEY_AP, -1);
		}
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO)) {
					posicoesbtGroupTipoFiltros.put(HASHKEY_CD, i);
					continue;
	}
				else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_CD)) {
					posicoesbtGroupTipoFiltros.put(HASHKEY_CD, -1);
	}
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO)) {
					posicoesbtGroupTipoFiltros.put(HASHKEY_DS, i);
					continue;
				}
				else if(!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_DS)) {
					posicoesbtGroupTipoFiltros.put(HASHKEY_DS, -1);
				}
			}
			btGroupTipoFiltros.setSelectedIndex(list.size());
	}

	//-----------------------------------------------

	public void add() throws SQLException {
		super.add();
		verificaProdutoGenerico = true;
	}

	protected ItemPedido addNovoItem() throws SQLException {
		return addNovoItem(false);
	}

	protected ItemPedido addNovoItem(boolean fromMultInserc) throws SQLException {
		return addNovoItem(fromMultInserc, 0);
	}

	protected ItemPedido addNovoItem(boolean fromMultInserc, int nuSeqItemPedido) throws SQLException {
		ItemPedido itemPedido = super.addNovoItem(fromMultInserc, nuSeqItemPedido);
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		return itemPedido;
	}

	public void edit(BaseDomain domain) throws SQLException {
		//Necessário iniciar novamente os descontos para o mesmo não ficar na memória do item.
		descontoQuantidadeList = null;
		descontoQtdeGrupoProdutoList = null;
		//--
		ItemPedido itemPedido = (ItemPedido) domain;
		if (itemPedido.pedido == null) {
			itemPedido.pedido = pedido;
		}
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		itemPedido.getProduto();
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			ItemKitPedidoService.getInstance().findItemKitPedidoList(itemPedido);
		}
		if (LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto4()) {
			ItemPedidoGradeService.getInstance().findItemPedidoGradeList(itemPedido);
		}
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			if (OrigemPedido.FLORIGEMPEDIDO_ERP.equals(itemPedido.flOrigemPedido) && itemPedido.qtItemDesejado == 0) {
				itemPedido.qtItemDesejado = itemPedido.getQtItemFisico();
			}
   		}
		//--
		verificaCestaPromocional(itemPedido);
		//--
		if (!itemPedido.usaCestaPromo) {
			itemPedido.getItemTabelaPreco();
		}

		//--
		if (LavenderePdaConfig.pctMargemAgregada > 0) {
			itemPedido.pctMargemAgregada = LavenderePdaConfig.pctMargemAgregada;
			getItemPedidoService().calculateAndApplyVlAgregadoSugerido(itemPedido);
		}
		getItemPedidoService().removeDescontoCCPAoEditarItemPedido(itemPedido);
		//--
		internalSetEnabled(pedido.isPedidoAbertoEditavel() && !cadPedidoForm.inOnlyConsultaItens, false);
		if (LavenderePdaConfig.isUsaVendaRelacionada() && LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado) {
			internalSetEnabled(false, false);
		}
		if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
			itemPedido.vlPctVerba = ComiRentabilidadeService.getInstance().getComiRentabilidadeAtingida(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false)).vlPctVerba;
		}

		//--
		super.edit(itemPedido);
		//--
		if(LavenderePdaConfig.usaUnidadeAlternativa && pedido.isPedidoAberto() && pedido.itemPedidoList.contains(itemPedido)) {
			ProdutoUnidade produtoUnidadeNova = itemPedido.getProdutoUnidade();
			if(ItemPedidoService.getInstance().isUnidadeAtualComDivergencia(itemPedido, produtoUnidadeNova)) {
				UiUtil.showWarnMessage(Messages.ITEMPEDIDO_MUDANCA_UNIDADE_ALTERNATIVA);
				recalculaNovaUnidade(itemPedido, produtoUnidadeNova);
			}
		}
		
		if (!itemPedido.usaCestaPromo) {
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			if (itemTabelaPreco != null) {
				itemTabelaPreco.descontoQuantidadeList = getDescontoQuantidadeList(itemPedido);
				if (itemPedido.vlPctFaixaDescQtd != 0 || LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
				DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
			}
		}
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			getItemPedido().qtItemVendidoTroca = ItemPedidoService.getInstance().getQtItemVendidoTroca(getItemPedido());
		}
		//--
		addItensOnButtonMenu();
		getItemPedido().oldQtEstoqueConsumido = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(getItemPedido(), getItemPedido().getQtItemFisico(), false);
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			ItemPedidoLogService.getInstance().loadItemPedidoLog(getItemPedido());
		}
		if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata || LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			itemPedido.oldVlPctDesc = itemPedido.vlPctDesconto;
		}
		if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional) {
			DescPromocionalGrade filter = new DescPromocionalGrade();
			filter.cdEmpresa = itemPedido.cdEmpresa;
			filter.cdRepresentante = itemPedido.cdRepresentante;
			filter.descPromocional = itemPedido.getDescPromocional();
			itemPedido.descPromocionalGradeList = DescPromocionalGradeService.getInstance().findAllByExample(filter);
		}
	}

	private void recalculaNovaUnidade(ItemPedido itemPedido, ProdutoUnidade produtoUnidadeNova) throws SQLException {
		populateItemPedidoForReload(itemPedido, produtoUnidadeNova);
		changeUnidadeAlternativa(produtoUnidadeNova.cdUnidade);
	}

	private void populateItemPedidoForReload(ItemPedido itemPedidoAtual, ProdutoUnidade produtoUnidadeNova) throws SQLException {
		itemPedidoAtual.setProdutoUnidade(produtoUnidadeNova);
		itemPedidoAtual.nuConversaoUnidadePu = produtoUnidadeNova.nuConversaoUnidade;
		itemPedidoAtual.vlIndiceFinanceiroPu = produtoUnidadeNova.vlIndiceFinanceiro;
		itemPedidoAtual.flDivideMultiplicaPu = produtoUnidadeNova.flDivideMultiplica;
	}
	
	private void verificaCestaPromocional(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaCestaPromocional) {
			CestaPromocional cesta = CestaPromocionalService.getInstance().findCestaByClienteProduto(itemPedido.cdProduto, pedido.getCliente().cdCliente);
			if (cesta != null) {
				itemPedido.pctMaxDescCestaPromo = cesta.vlPctMaxDesconto;
				itemPedido.usaCestaPromo = true;
				edDsTabelaPreco.setValue(Messages.TABELAPRECO_ESPECIAL);
			}
		}
	}
	
	//@Override
	protected void insert(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && itemPedido.getProduto().isProdutoAgrupadorGrade())) {
			insereItemPedidoPorGradePreco(itemPedido);
		} else {
			insert(itemPedido);
		}
		cadPedidoForm.updateDataEntrega(pedido);
	}

	private void insereItemPedidoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		int size = itemPedidoPorGradePrecoList.size();
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			try {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
					if (itemPedidoPorGradePreco.qtItemFisico == 0) {
						continue;
					}
					itemPedidoPorGradePreco.itemPedidoPorGradePrecoList = new Vector(0);
					getPedidoService().atualizaNuSeqProduto(itemPedidoPorGradePreco);
					itemPedidoPorGradePreco.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedidoDatabase(itemPedidoPorGradePreco);
					marcaItemSugPerson(itemPedidoPorGradePreco);
					if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
						ajustaDescontoPeloAcrescimoCapaPedido(itemPedidoPorGradePreco);
				}
					getPedidoService().insertItemPedido(pedido, itemPedidoPorGradePreco, false);
					itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_SEM_EDICAO;
				}
			}catch (Throwable ex) {
				CrudDbxDao.getCurrentDriver().rollback();
				throw ex;
			} 
			getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
			CrudDbxDao.getCurrentDriver().commit();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}
	
	private void ajustaDescontoPeloAcrescimoCapaPedido(ItemPedido itemPedidoPorGradePreco) {
		if (itemPedidoPorGradePreco.vlPctAcrescimo > 0d) {
			itemPedidoPorGradePreco.vlPctDescPedido = 0d;
			try {
				PedidoService.getInstance().calculateItemPedido(pedido, itemPedidoPorGradePreco, true);
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	private boolean excluiItemPedidoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		if (UiUtil.showConfirmDeleteMessage(Messages.ITEM_GRADE_MSG_EXCLUSAO)) {
			Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoList.size();
			try {
				CrudDbxDao.getCurrentDriver().startTransaction();
				try {
					for (int i = 0; i < size; i++) {
						ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
						delete(itemPedidoPorGradePreco);
					}
				}catch (Throwable ex) {
					CrudDbxDao.getCurrentDriver().rollback();
					throw ex;
				} 
				CrudDbxDao.getCurrentDriver().commit();
			} finally {
				CrudDbxDao.getCurrentDriver().finishTransaction();
			}
			return true;
		}
		return false;
	}

	private void updateFlSugVendaPerson(final ItemPedido itemPedido) throws SQLException {
		marcaItemSugPerson(itemPedido);
	}

	private void marcaItemSugPerson(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			itemPedido.flSugVendaPerson = isProdutoSugPerson(itemPedido.cdProduto) ? ValueUtil.VALOR_SIM : null;
		}
	}

	private void insert(ItemPedido itemPedido) throws SQLException {
		updateFlSugVendaPerson(itemPedido);
		getPedidoService().insertItemPedido(pedido, itemPedido);
	}

	@Override
	protected void update(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido =  (ItemPedido) domain;

		updateFlSugVendaPerson(itemPedido);
		if (LavenderePdaConfig.usaGradeProduto4()) {
			updateItemPedidoPorGradePreco(itemPedido);
		} else if (LavenderePdaConfig.usaGradeProduto5() && isModoGrade()) {
			updateItemPedidoAgrupadorGrade(itemPedido);
		} else {
			getPedidoService().updateItemPedido(pedido, itemPedido);
		}
		cadPedidoForm.updateDataEntrega(pedido);
	}
	
	private void updateItemPedidoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
		Vector itemPedidoPorGradePrecoBancoList = ItemPedidoService.getInstance().findItemPedidoPorGradePreco(itemPedido);
		ItemPedidoService.getInstance().deletaItemPedidoPorGrade1(itemPedidoPorGradePrecoBancoList, itemPedido, pedido);
		Vector itemPedidoPorGradePrecoMemoriaList = itemPedido.itemPedidoPorGradePrecoList;
		int size = itemPedidoPorGradePrecoMemoriaList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePrecoMemoria = (ItemPedido) itemPedidoPorGradePrecoMemoriaList.items[i];
			getPedidoService().atualizaNuSeqProduto(itemPedidoPorGradePrecoMemoria);
			insert(itemPedidoPorGradePrecoMemoria);
		}
		excluiItemPedidoPorGradePreco(itemPedidoPorGradePrecoMemoriaList);
		pedido.itemPedidoList.addElementsNotNull(itemPedido.itemPedidoPorGradePrecoList.items);
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
			throw e;
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
	}
	}
	
	private void updateItemPedidoAgrupadorGrade(ItemPedido itemPedido) throws SQLException {
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			Vector itemPedidoPorGradePrecoMemoriaList = itemPedido.itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoMemoriaList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoPorGradePrecoMemoria = (ItemPedido) itemPedidoPorGradePrecoMemoriaList.items[i];
				if (itemPedidoPorGradePrecoMemoria.nuSeqItemPedido == -1) {
					itemPedidoPorGradePrecoMemoria.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedidoDatabase(itemPedidoPorGradePrecoMemoria);
				}
				itemPedidoPorGradePrecoMemoria.itemPedidoPorGradePrecoList = new Vector(0);
				crudItemPedidoGrade(itemPedidoPorGradePrecoMemoria);
				itemPedidoPorGradePrecoMemoria.flTipoEdicao = itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade = ItemPedido.ITEMPEDIDO_SEM_EDICAO;
			}
			getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}

	private void crudItemPedidoGrade(ItemPedido itemPedidoPorGradePrecoMemoria) throws SQLException {
		if (itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_INSERINDO) {
			getPedidoService().atualizaNuSeqProduto(itemPedidoPorGradePrecoMemoria);
			itemPedidoPorGradePrecoMemoria.nuSeqItemPedido = getItemPedidoService().getNextNuSeqItemPedidoDatabase(itemPedidoPorGradePrecoMemoria);
			insert(itemPedidoPorGradePrecoMemoria);
		} else if (itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_ATUALIZANDO) {
			ItemPedido itemPedidoBanco = (ItemPedido) ItemPedidoService.getInstance().findByPrimaryKey(itemPedidoPorGradePrecoMemoria);
			if (itemPedidoBanco != null && ValueUtil.round(itemPedidoBanco.getQtItemFisico()) == ValueUtil.round(itemPedidoPorGradePrecoMemoria.getQtItemFisico())
					&& itemPedidoBanco.vlItemPedido == itemPedidoPorGradePrecoMemoria.vlItemPedido && ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedidoBanco.cdItemGrade1)) {
				itemPedidoPorGradePrecoMemoria.cdItemGrade1 = itemPedidoBanco.cdItemGrade1;
				itemPedidoPorGradePrecoMemoria.cdItemGrade2 = itemPedidoBanco.cdItemGrade2;
				itemPedidoPorGradePrecoMemoria.cdItemGrade3 = itemPedidoBanco.cdItemGrade3;
			}
			getPedidoService().updateItemPedido(pedido, itemPedidoPorGradePrecoMemoria, false);
		} else if (itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_EXCLUINDO) {
			ItemPedido itemPedidoAux = getItemPedidoService().getItemPedidoByCdProduto(pedido, itemPedidoPorGradePrecoMemoria.getProduto().cdProduto);
			itemPedidoPorGradePrecoMemoria.setQtItemFisico(itemPedidoAux.getQtItemFisico());
			itemPedidoPorGradePrecoMemoria.qtItemFaturamento = itemPedidoAux.qtItemFaturamento;
			getPedidoService().deleteItemPedido(pedido, itemPedidoPorGradePrecoMemoria, false);
			if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) refreshCurrentItemMultiplaInsercaoList = true;
		}
	}

	private void excluiItemPedidoPorGradePreco(Vector itemPedidoPorGradePrecoBancoList) {
		int size = itemPedidoPorGradePrecoBancoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoExcluir = (ItemPedido) itemPedidoPorGradePrecoBancoList.items[i];
			pedido.itemPedidoList.removeElement(itemPedidoExcluir);
		}
	}

	public void addFromExternalForm(Produto produto, BaseCrudForm externalForm) throws SQLException {
		editOrAddFromExternalForm(null, externalForm, produto);
	}

	public void editFromExternalForm(ItemPedido itemPedido, BaseCrudForm externalForm) throws SQLException {
		editOrAddFromExternalForm(itemPedido, externalForm, itemPedido.getProduto());
	}

	private void editOrAddFromExternalForm(ItemPedido itemPedido, BaseCrudForm externalForm, Produto produto) throws SQLException {
		setBaseCrudListForm(null);
		flFromCadPedido = true;
		editingExternalForm = externalForm;
		produtoSelecionado = produto;
		if (itemPedido == null) {
			add();
			externalForm.show(this);
			gridClickAndRepaintScreen(true);
		} else {
			edit(itemPedido);
			externalForm.show(this);
		}
	}

	@Override
	protected void excluirClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		boolean excluiu = false;
		if ((LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && !fromListItemPedidoForm && itemPedido.getProduto().isProdutoAgrupadorGrade())) && btGradeItemPedido.isEnabled()) {
			excluiu = excluiItemPedidoPorGradePreco(itemPedido);
		} else if (itemPedido.isKitTipo3()) {
			excluiu = deletaKitTipo3(itemPedido);
		} else if (itemPedido.isFazParteKitFechado()) {
			excluiu = deletaKitFechado(itemPedido.cdKit);
		} else if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			excluiu = deleteCombo(itemPedido);
		} else {
            excluiu = deleteItemPedido();
		}
		if (excluiu) {
			if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
				PontuacaoConfigService.getInstance().reloadPontuacaoValorTotal(pedido, itemPedido);
			}
			if (LavenderePdaConfig.restringeItemPedidoPorLocal && ValueUtil.valueEquals(pedido.itemPedidoList.size(), 0)) {
				pedido.cdLocal = null;
			}
			if (inCarrouselMode && itemPedidoCarrouselList.size() > 1) {
				itemPedidoCarrouselList.remove(selectedItemPedidoCarrousel);
				configureCarrouselItemPedido(itemPedidoCarrouselList, carrouselWindow, carrouselType);
				edit(itemPedidoCarrouselList.get(selectedItemPedidoCarrousel));
				if (fromRelNotificaoItemWindow && ValueUtil.isNotEmpty(pedido.erroItensFechamentoPedido)) {
					pedido.erroItensFechamentoPedido.removeElement(itemPedido.getProduto());
				}
			} else {
				if (inCarrouselMode && itemPedidoCarrouselList.size() == 1) {
					itemPedidoCarrouselList.remove(selectedItemPedidoCarrousel);
				}
				if (fromRelNotificaoItemWindow && ValueUtil.isNotEmpty(pedido.erroItensFechamentoPedido)) {
					pedido.erroItensFechamentoPedido.removeElement(itemPedido.getProduto());
				}
				BaseCrudListForm listItemPedidoForm = getBaseCrudListForm();
				if (listItemPedidoForm instanceof ListItemPedidoForm) {
					voltarEListarMantendoScroll((ListItemPedidoForm)listItemPedidoForm);
				} else {
			voltarClick();
			list();
		}
			}
		}
		if (!isAddingFromArray) {
			pedido.atualizaLista = true;
		}
	}
	
	private void btDescProgressivoConfigClick(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		if (cliente != null) {
			DescProgressivoConfig descProgressivoConfigFilter = new DescProgressivoConfig();
			descProgressivoConfigFilter.cdEmpresa = cliente.cdEmpresa;
			descProgressivoConfigFilter.cdRepresentante = cliente.cdRepresentante;
			descProgressivoConfigFilter.cliente = cliente;
			int qtRegistros = DescProgressivoConfigService.getInstance().countByCliente(descProgressivoConfigFilter);
			if (qtRegistros >= 1) {
				show(new ListDescontoProgressivoForm(cliente, cadPedidoForm, true));
			} else {
				UiUtil.showWarnMessage(Messages.DESC_PROG_CONFIG_SEM_REGISTROS);
			}
		}
	}

	protected boolean deleteItemPedido() throws SQLException {
		boolean result = UiUtil.showConfirmDeleteMessage(getEntityDescription());
		if (result) {
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && getItemPedido().isAgrupadorSimilaridade()) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.MSG_AVISO_EXCLUSAO_ITEM_AUTORIZADO_SIMILAR)) return false;
				ItemPedidoAgrSimilarService.getInstance().deleteItensPedidoAgrSimilar(getItemPedido());
			}
			ItemPedido itemPedidoAux;
			int nuSeqProduto;
			if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido || LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
				nuSeqProduto = getItemPedido().nuSeqProduto;

			} else {
				nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
			}
			if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
				itemPedidoAux = getItemPedidoService().getItemPedidoByCdProduto(pedido, (getItemPedido().getProduto().cdProduto != null ? getItemPedido().getProduto().cdProduto : getItemPedido().cdProduto), getItemPedido().flTipoItemPedido, nuSeqProduto);
			} else {
				itemPedidoAux = getItemPedidoService().getItemPedidoByCdProduto(pedido, (getItemPedido().getProduto().cdProduto != null ? getItemPedido().getProduto().cdProduto : getItemPedido().cdProduto), nuSeqProduto);
			}
			itemPedidoAux.pedido = getItemPedido().pedido;
			ItemPedido itemPedido = getItemPedido();
			itemPedido.setQtItemFisico(itemPedidoAux.getQtItemFisico());
			itemPedido.qtItemFaturamento = itemPedidoAux.qtItemFaturamento;
			getPedidoService().deleteItemPedido(pedido, itemPedido);
			if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) refreshCurrentItemMultiplaInsercaoList = true;
		}
		return result;
	}

	protected void voltarEListarMantendoScroll(ListItemPedidoForm listItemPedidoForm) throws SQLException {
		GridListContainer listContainer = listItemPedidoForm.getListContainer();
		if (listContainer != null) {
			int lastIndex = listContainer.getSelectedIndex();
			voltarClick();
			list();
			if (lastIndex > 0) {
				Container previousContainer = listContainer.getContainer(lastIndex - 1);
				if (previousContainer == null && lastIndex > 1){
					previousContainer = listContainer.getContainer(lastIndex - 2);
				}
				if (previousContainer != null){
					listContainer.scrollToControl(previousContainer);
					listContainer.setScrollPos(previousContainer.getY());
				}
			}
		}
	}

	private boolean deletaKitTipo3(ItemPedido itemPedido) throws SQLException {
		if (UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(Messages.KIT_TIPO_3_EXCLUSAO, KitService.getInstance().getDsKit(itemPedido.cdKit, itemPedido.pedido)))) {
			try {
				UiUtil.showProcessingMessage();
				deleteItensKit(itemPedido.cdKit);
			} finally {
				UiUtil.unpopProcessingMessage();
			}
			return true;
		}
		return false;
	}

	private boolean deletaKitFechado(String cdKit) throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.KIT_MSG_KIT_EXCLUSAO_KIT_FECHADO)) {
			deleteItensKit(cdKit);
			return true;
		}
		return false;
	}

	private void deleteItensKit(String cdKit) throws SQLException {
			Vector itemPedidoList = pedido.itemPedidoList;
		Vector itemPedidoKitList = new Vector();
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoKit = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.valueEquals(cdKit, itemPedidoKit.cdKit)) {
				itemPedidoKitList.addElement(itemPedidoKit);
				}
			}
		size = itemPedidoKitList.size();
			for (int i = 0; i < size; i++) {
			delete((ItemPedido) itemPedidoKitList.items[i]);
			}
		}

	@Override
	protected void delete(final BaseDomain domain) throws SQLException {
		delete(domain, false);
	}

	private void delete(final BaseDomain domain, boolean background) throws SQLException {
		if (instance == null) {
			instance = getInstance(cadPedidoForm, pedido);
		}
		ItemPedido itemPedido = (ItemPedido) domain;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		if (!background) {
			msg.popupNonBlocking();
		}
		try {
			if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() && !LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.isItemBonificacao()) {
				if (!ItemPedidoService.getInstance().deleteItemBonificadoVinculado(itemPedido)) return;
			}
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && itemPedido.isAgrupadorSimilaridade()) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.MSG_AVISO_EXCLUSAO_ITEM_AUTORIZADO_SIMILAR)) return;
				ItemPedidoAgrSimilarService.getInstance().deleteItensPedidoAgrSimilar(itemPedido);
			}
		PedidoService.getInstance().validate(pedido);
		getPedidoService().deleteItemPedido(pedido, itemPedido);
			if (LavenderePdaConfig.isExibeComboMenuInferior() && itemPedido.isCombo()) {
				ItemPedidoService.getInstance().deleteItensCombo(itemPedido);
			}
		deleteProdutosRelacionados(itemPedido);
		if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
			recalculaPedidoAposAlterarDescPromocional(true);
		}
			GridListContainer listContainerInst = instance instanceof CadItemPedidoForm && instance.listContainer.size() == 0 || instance == null ? listContainer : instance.listContainer;
			int sizeListCon = listContainerInst.size();
		for (int k = 0; k < sizeListCon; k++) {
				BaseListContainer.Item c = (BaseListContainer.Item) listContainerInst.getContainer(k);
			if (c.id.equals(itemPedido.getProduto().getRowKey())) {
				if (c.rightControl instanceof ItemContainer) {
						ItemContainer item = (ItemContainer) c.rightControl;
					double vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescontoItemPedido(itemPedido);
					if (itemPedido.pedido.vlPctDescItem > vlPctMaxDesconto) {
						itemPedido.vlPctDesconto = vlPctMaxDesconto;
					} else if (itemPedido.pedido.vlPctDescItem > 0 && !LavenderePdaConfig.isAcumulaComDescDoItem()) {
						itemPedido.vlPctDesconto = itemPedido.pedido.vlPctDescItem;
						itemPedido.vlPctAcrescimo = 0;
					} else {
						itemPedido.vlPctAcrescimo = itemPedido.pedido.vlPctAcrescimoItem;
							itemPedido.vlPctDesconto = 0;
					}
					if (item != null) {
						item.itemPedido.setQtItemFisico(0);
						itemPedido.setQtItemFisico(0);
						item.itemPedido.vlPctDesconto = itemPedido.vlPctDesconto;
						item.itemPedido.vlPctAcrescimo = itemPedido.vlPctAcrescimo;
							if (item.vlItemOriginal > 0) {
								itemPedido.vlItemPedido = item.vlItemOriginal;
								item.itemPedido.vlItemPedido = item.vlItemOriginal;
							}
						item.clear(calculateItemForGrid(itemPedido));
						item.itemPedido = null;
							itemPedido.removeFromGrid = true;
							item.setControlsEnabled(true, true, true, true, true, true, true, true, true, true, true);
					}
					break;
				}
			}
		}
			if (LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && !PedidoService.getInstance().possuiCondicaoComercial(pedido)) {
				PedidoService.getInstance().aplicaDescQtdPorGrupoProdTodosItens(pedido, true, true);
			}
			atualizaItemPedidoNaLista(itemPedido);
		//--
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			ItemPedidoLogService.getInstance().saveItemPedidoLog(TipoRegistro.EXCLUSAO, itemPedido);
		}
			if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex() && pedido.isPedidoVenda()) {
			ItemPedidoService.getInstance().recalculaVlBaseFlexItens(pedido, true);
		}
		cadPedidoForm.updateDataEntrega(pedido);
		if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && !pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
		}
		} finally {
			if (!background) {
				msg.unpop();
	}
		}
	}

	protected void atualizaItemPedidoNaLista(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.destacaProdutosJaIncluidosAoPedido) {
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
				ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(itemPedido.cdProduto);
				produtoTabPreco.itemTabelaPreco = itemPedido.getItemTabelaPreco(); 
				produtoTabPreco.estoque = itemPedido.estoque;
				atualizaProdutoNaGrid(produtoTabPreco, itemPedido, false, false);
			} else {
				Produto produto = itemPedido.getProduto();
				if (produto != null) {
					produto.estoque = itemPedido.estoque;
					atualizaProdutoNaGrid(produto, itemPedido, false, false);
				}
			}
		}
	}

	private void deleteProdutosRelacionados(final ItemPedido itemPedidoPrincipal) throws SQLException {
		if (LavenderePdaConfig.isUsaVendaRelacionada() && LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado) {
			Vector produtoRelacionadoList = ProdutoRelacionadoService.getInstance().getProdutosRelacionados(itemPedidoPrincipal.getProduto());
			if (ValueUtil.isNotEmpty(produtoRelacionadoList)) {
				for (int i = 0; i < produtoRelacionadoList.size(); i++) {
					ProdutoRelacionado produtoRelacionado = (ProdutoRelacionado) produtoRelacionadoList.items[i];
					ItemPedido itemPedidoRelacionado = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, produtoRelacionado.cdProdutoRelacionado);
					itemPedidoRelacionado.pedido = pedido;
					if (pedido.itemPedidoList.indexOf(itemPedidoRelacionado) != -1) {
						getPedidoService().deleteItemPedido(pedido, itemPedidoRelacionado);
						atualizaItemPedidoNaLista(itemPedidoRelacionado);
					}
				}
			}
		}
	}
	
	protected void carregaInfosExtraDomainToScreen(ItemPedido itemPedido) throws SQLException {
		super.carregaInfosExtraDomainToScreen(itemPedido);
		if (LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido) {
			carregaGridPrecoCondicao();
		} else if ((LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.restringeItemPedidoPorLocal) && !pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			if (isEditing()) {
				carregaGridLoteProduto();
				if (!ValueUtil.isEmpty(itemPedido.cdLoteProduto)) {
					itemPedido.usaPctMaxDescLoteProduto = true;
					LoteProduto lote = LoteProdutoService.getInstance().findByItemPedido(itemPedido);
					if (lote != null && LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
						DescVidaUtilGrupo descVidaUtil = DescVidaUtilGrupoService.getInstance().getDescVidaUtilByLote(lote.vlPctvidautilproduto, itemPedido.getProduto().cdGrupoProduto1);
						if (descVidaUtil != null) {
							itemPedido.pctMaxDescLoteProdutoSelected = descVidaUtil.vlPctDesconto;
						} else {
							itemPedido.usaPctMaxDescLoteProduto = false;
						}
					}
				}
			} else {
				carregaGridLoteProduto();
				edVlPctDesconto.setEditable(isEnabled() && LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL));
			}
		} 
		if (LavenderePdaConfig.isExibeHistoricoProduto()) {
			carregaGridHistoricoTrocaDevolucaoProd();
	}
	}
	
	//@Override
	protected void validaCalcularClick(ItemPedido itemPedido) throws SQLException {
		super.validaCalcularClick(itemPedido);
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && !itemPedido.fromDescQtdWindow && !pedido.isPedidoBonificacao()) {
			if ((gridLoteProduto != null) && (gridLoteProduto.size() > 0 || LavenderePdaConfig.isUsaControleEstoquePorLoteProduto())) {
				if (ValueUtil.isEmpty(itemPedido.cdLoteProduto) && (!ValueUtil.VALOR_NAO.equals(itemPedido.getProduto().flLoteObrigatorio) || LavenderePdaConfig.isUsaControleEstoquePorLoteProduto())) {
					throw new ValidationException(Messages.LOTEPRODUTO_NAOSELECIONADO);
				}
			}
		}
		if (LavenderePdaConfig.usaSelecaoPorGrid() && !pedido.isPedidoBonificacao()) {
			if (gridTabelaPreco != null && gridTabelaPreco.size() > 0 && ValueUtil.isEmpty(itemPedido.cdTabelaPreco)) {
				throw new ValidationException(Messages.ITEMPEDIDO_TABELA_PRECO_NAO_SELECIONADA);
			}
		}
		ItemPedidoService.getInstance().validateItemBloqueadoRestrito(pedido, itemPedido);
	}

	// @Override
	protected void salvarClick() throws SQLException {
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		if (exibeAvisoLimite()) {
			return;
		}
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() && ItemPedidoService.getInstance().isVlItemPedidoMenorVlMin(getItemPedido())) {
			if (!confirmaValorMinimoItemPedido(getItemPedido())) {
				return;
			}
		}
		
		if (LavenderePdaConfig.validaQtMaxVendaPorGrade && ItemPedidoService.getInstance().avisoQuantidadeMaximaAtingidaItemPedido(getItemPedido())) return;
		getItemPedido().fromDescQtdWindow = false;
		pedido.updateByClickSalvarItemPedido = true;
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) SolAutorizacaoService.getInstance().updateFlVisualizadoByItemPedido(getItemPedido(), ValueUtil.VALOR_SIM);
		try {
		salvarItemUnitario(false);
		} catch (Throwable e) {
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) SolAutorizacaoService.getInstance().updateFlVisualizadoByItemPedido(getItemPedido(), ValueUtil.VALOR_NAO);
			throw e;
		}
		// --
		if (MainLavenderePda.getInstance().getActualForm().equals(this)) {
			setFocusInFiltro();
		}
		if (!isAddingFromArray) {
			pedido.atualizaLista = true;
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			ProdutoCreditoDescService.getInstance().loadCreditosPedido(pedido);
		}
	}

	@Override
	protected void salvarNovoClick() throws SQLException {
		getItemPedido().fromDescQtdWindow = false;
		validaProdutoGrade1();
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && exibeAvisoLimite()) {
			return;
		}
		if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() && ItemPedidoService.getInstance().isVlItemPedidoMenorVlMin(getItemPedido())) {
			if (!confirmaValorMinimoItemPedido(getItemPedido())) {
				return;
			}
		}
		if (LavenderePdaConfig.isUsaSugestaoComboAposInsercao()) {
			ultimoCdProduto = getItemPedido().cdProduto;
		}
		
		if (LavenderePdaConfig.validaQtMaxVendaPorGrade && ItemPedidoService.getInstance().avisoQuantidadeMaximaAtingidaItemPedido(getItemPedido()))
			return;
		
		pedido.updateByClickSalvarItemPedido = true;
		int size = getCheckedItensSize();
		if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && (size > 0) && !isEditing()) {
			if (!salvarMultiplosItens(true)) {
				return;
			}
		} else {
			if (!salvarItemUnitario(true)) {
				return;
			}
		}
		afterSalvarNovo();
	}

	public ItemPedido salvaItemPelaListaComCdBarras(Pedido pedido, String cdProduto, boolean itemTroca) throws SQLException {
		salvandoItemPelaListaComCdBarras = true;
		try {
			int nuSeqProduto =  LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido ? getItemPedido().nuSeqProduto : ItemPedido.NUSEQPRODUTO_UNICO;
			ItemPedido itemPedido = itemTroca ? ItemPedidoService.getInstance().getItemPedidoTrocaRecByCdProduto(pedido, cdProduto) : ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, cdProduto, nuSeqProduto);
			if (itemPedido == null) {
				add();
				setProdutoSelecionado(ProdutoService.getInstance().getProduto(cdProduto));
				inicializaItemParaVenda(getItemPedido());
				changeItemTabelaPreco(getItemPedido().pedido.cdTabelaPreco, true);
				edQtItemFisico.setValue(1);
			} else {
				setDomain(itemPedido);
				setProdutoSelecionado(ProdutoService.getInstance().getProduto(cdProduto));
				edit(itemPedido);
				itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + 1);
				edQtItemFisico.setValue(itemPedido.getQtItemFisicoOrg());
			}
			edQtItemFisicoValueChange(getItemPedido());
			bgItemTroca.setValueBoolean(itemTroca);
			bgItemTrocaClick();
			if (getItemPedido() != null) {
				domainToScreen(getItemPedido());
			}
			if (!salvarItemUnitario(false)) {
				throw new ValidationException(Messages.ITEMPEDIDO_LISTA_MSG_ITEM_NAO_INCLUSO);
			}
			cadPedidoForm.afterCrudItemPedido();
		} finally {
			salvandoItemPelaListaComCdBarras = false;
		}
		return getItemPedido();
	}

	private boolean salvarMultiplosItens(boolean bySalvarNovo) throws SQLException {
		if (!beforeSaveMultiplosItens()) {
			return false;
		}
		double vlPctDesconto = edVlPctDesconto.getValueDouble();
		double vlPctAcrescimo = edVlPctAcrescimo.getValueDouble();
		double qtItemFisico = edQtItemFisico.getValueDouble();
		double qtItemFaturamento = edQtItemFaturamento.getValueDouble();
		conferirInconsistencias = false;
		itensErro = new Vector();
		int countItensSuceso = 0;
		int[] checkedItens = new int[1];
		if (listContainer.checkedItens != null)
			checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		for (int i = 0; i < gridSize; i++) {
			try {
				listContainer.setSelectedIndex(checkedItens[i]);
				//--
				if (!bySalvarNovo) {
					setDomain(new ItemPedido());
					addNovoItem();
				}
				inicializaItemParaVenda(getItemPedido());
				changeItemTabelaPreco();
				if (!LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens()) {
					edVlPctDesconto.setValue(vlPctDesconto);
					edVlPctAcrescimo.setValue(vlPctAcrescimo);
					edQtItemFisico.setValue(qtItemFisico);
					edQtItemFaturamento.setValue(qtItemFaturamento);
				} else {
					if (isInsereMultiplosSemNegociacao()) {
						edQtItemFisico.setValue(getItemPedido().getQtItemFisico());
						edVlPctDesconto.setValue(getItemPedido().vlPctDesconto);
					} else {
						edQtItemFisico.setValue(1d);
					}
				}
				edQtItemFisicoValueChange(getItemPedido());
				salvarItemUnitario(bySalvarNovo);
				countItensSuceso++;
			} catch (Throwable e) {
				ItemPedido itemPedido = getItemPedido();
				ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, e.getMessage());
				itensErro.addElement(produtoErro);
			} finally {
				if (LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens()) {
					setProdutoSelecionado(null);
				}
			}
		}
		if (countItensSuceso > 0) {
			listContainer.uncheckAll();
		}
		Vector botoes = new Vector();
		if (itensErro.size() > 0) {
			botoes.addElement("	" + Messages.REL_NOTIFICACAO_ITENS + "	");
		}
		botoes.addElement(FrameworkMessages.BOTAO_OK);
		int result = UiUtil.showMessage(MessageUtil.getMessage(Messages.REL_INSERCAO_ITENS_RESUMO, new String[]{StringUtil.getStringValueToInterface(countItensSuceso), StringUtil.getStringValueToInterface(itensErro.size())}), TYPE_MESSAGE.INFO, (String[]) botoes.toObjectArray());
		if (itensErro.size() > 0 && result == 0) {
			RelNotificacaoItemWindow relInsercaoForm = new RelNotificacaoItemWindow(itensErro, false);
			relInsercaoForm.popup();
		}
		return true;
	}

	private boolean beforeSaveMultiplosItens() {
		int lenght = getCheckedItensSize();
		if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_INSERIR_MULTIPLOS, lenght)) == 0) {
			return false;
		}
		return true;
	}

	private boolean salvarItemUnitario(boolean bySalvarNovo) throws SQLException {
		return salvarItemUnitario(getItemPedido(), bySalvarNovo, true);
	}

	private boolean salvarItemUnitario(ItemPedido itemPedido, boolean bySalvarNovo, boolean showPopupNonBlocking) throws SQLException {
		produtoAnterior = itemPedido.getProduto();
		if (produtoAnterior == null) {
			if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				UiUtil.showErrorMessage(Messages.PRODUTO_NENHUM_SELECIONADO);
			}
			return false;
		}
		if (ValueUtil.isNotEmpty(itemPedido.getProduto().dsProduto)) {
			itemPedido.dsProduto = itemPedido.getProduto().dsProduto;
		}
		if (LavenderePdaConfig.isPropagaUltimoDescontoItemPedido()) {
			valueUltimoDescontoNegociado = edVlPctDesconto.getValueDouble();
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
				valueUltimoDesconto2Negociado = edVlPctDesconto2.getValueDouble();
				valueUltimoDesconto3Negociado = edVlPctDesconto3.getValueDouble();
			}
		}
		if (LavenderePdaConfig.usaSugestaoParaNovoPedidoGiroProduto) {
			itemPedido.isItemPedidoSugestaoGiro = false;
		}
		if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && !isEditing() && getCheckedItensSize() <= 0 && itemPedido.getQtItemLista() <= 0 && LavenderePdaConfig.naoPermiteInserirQtdDescMultipla()) {
			if(itemPedido.getQtItemLista() <= 0) {
				UiUtil.showErrorMessage(Messages.VALIDACAO_ITEMPEDIDO_QTD_CAMPO);
			} else {
				UiUtil.showErrorMessage(Messages.PRODUTO_NENHUM_SELECIONADO);
			}
			return false;
		}
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && itemPedido.getProduto().isProdutoAgrupadorGrade())) {
			calculaItensGrade(itemPedido);
		} else {
			if (calcularClick(true, itemPedido)) {
				refreshDomainToScreen(itemPedido);
			}
			}
		if (LavenderePdaConfig.usaRegistroProdutoFaltante && itemPedido.isRegistrouProdutoFalta) {
			itemPedido.isRegistrouProdutoFalta = false;
			return false;
		}
		if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
			pedido.recalculaPedidoMudancaFaixaDesc = itemPedido.oldVlPctDesc != itemPedido.vlPctDesconto || !isEditing();
			pedido.itensAtingiramFaixa = itemPedido.tipoDesc == 1;
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa == 1) {
				if (!isConfirmItemAbaixoRentMinima(itemPedido)) {
					return false;
				}
			} else if (LavenderePdaConfig.isAvisoRentabilidadeItemAbaixoEsperadoIncluindoItem()) {
				//Avisa rentabilidade abaixo de esperado
				if (LavenderePdaConfig.isAvisoRentabilidadeItemAbaixoMinimo() && ItemTabelaPrecoService.getInstance().isRentabalidadeMenorMinBaseadoItemTabelaPreco(itemPedido)) {
					UiUtil.showWarnMessage(Messages.ITEMPEDIDO_MSG_PCT_RENTABILIDADE_ABAIXO_MINIMO);
				} else if (LavenderePdaConfig.isAvisoRentabilidadeItemAbaixoEsperadoQualquerNivel() && itemPedido.getItemTabelaPreco().vlPctRentabilidadeEsp >= 0 && !ItemTabelaPrecoService.getInstance().isRentabalidadeMaiorEspBaseadoItemTabelaPreco(itemPedido)) {
					UiUtil.showWarnMessage(Messages.ITEMPEDIDO_MSG_PCT_RENTABILIDADE_ABAIXO_ESPERADO);
				}
			}
		}
		if (itemPedido != null && ItemPedidoService.getInstance().isItemBonificadoNoPedido(itemPedido)) {
			ItemPedidoService.getInstance().atualizaInfoNegociacaoItemBonificado(itemPedido);

			if (!itemPedido.isItemBonificacao()) {
				ItemPedidoService.getInstance().validaQtItemBonificadoVinculado(itemPedido, edQtItemFisico.getValueDouble());
			}
		}
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		if (showPopupNonBlocking) {
		msg.popupNonBlocking();
		}
		try {
			if (isInsereMultiplosSemNegociacao() && itemPedido.nuSeqItemPedido == -1) {
				itemPedido.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
			}
			if (bySalvarNovo) {
				salvarNovoClick(itemPedido);
			} else {
				salvarClick(itemPedido);
			}
		} catch (ValidateProdutoInPlataFormaVendaProdutoException ex) {
			UiUtil.showErrorMessage(ex.getMessage());
			voltarClick();
		} catch (ValidationException e) {
			if ((e.getMessage() != null) && e.getMessage().equals(ValidationException.EXCEPTION_ABORT_PROCESS)) {
				return false;
			}
			throw e;
		} finally {
			if (showPopupNonBlocking) {
			msg.unpop();
		}
		}
		return true;
	}

	private void calculaItensGrade(ItemPedido itemPedido) throws SQLException {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		if (!btGradeItemPedido.isEnabled() && !itemPedidoPorGradePrecoList.contains(itemPedido)) {
			itemPedidoPorGradePrecoList.addElement(itemPedido);
		}
		int size = itemPedidoPorGradePrecoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
			if (ValueUtil.isEmpty(itemPedidoPorGradePreco.dsProduto)) {
				itemPedidoPorGradePreco.dsProduto = itemPedidoPorGradePreco.getProduto().dsProduto;
			}
			if (itemPedidoPorGradePreco.pedido == null) {
				itemPedidoPorGradePreco.pedido = itemPedido.pedido;
			}
			calcularClick(true, itemPedidoPorGradePreco);
		}
		refreshDomainToScreen(getItemPedido());
	}

	private boolean isAlterouDescProgressivo(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.usaDescProgressivoPersonalizado && ValueUtil.isNotEmpty(itemPedido.getProduto().cdFamiliaDescProg) && !pedido.isPedidoMultiplaInsercao;
	}

	private void afterSalvarNovo() throws SQLException {
		if (pedido.itemPedidoList.size() == LavenderePdaConfig.maximoItensPorPedido) {
			UiUtil.showInfoMessage(Messages.PEDIDO_MSG_MAXIMO_ITENS_AVISO);
		}
		//--
		if (LavenderePdaConfig.atualizarEstoqueInterno && LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
			if (!pedido.isIgnoraControleEstoque() && produtoAnterior != null) {
				Estoque estoqueOfi = EstoqueService.getInstance().getEstoque(produtoAnterior.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
				if (estoqueOfi.qtEstoque <= 0) {
					setPropertiesInRowList(listContainer.getSelectedItem(), produtoAnterior);
				}
			}
		}
		if (LavenderePdaConfig.isUsaSugestaoComboFechamentoPedido() || LavenderePdaConfig.isUsaSugestaoComboAposInsercao()) {
			cadPedidoForm.salvouItemComboSugerido = true;
		}
		if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && !pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
		}
		if (inVendaUnitariaMode && (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || fromProdutoPendenteGiroMultInsercao || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao())) {
			voltarClick();
		}
		setFocusInFiltro();
	}

	protected Produto getProdutoSessaoVenda() throws SQLException {
		if ((produtoSelecionado == null) || (permiteInserirMultiplosItensPorVezComInterfaceNegociacao())) {
			produtoSelecionado = getSelectedProduto();
		}
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produto) throws SQLException {
		produtoSelecionado = produto;
	}

	@Override
	protected void afterSave() throws SQLException {
		afterSave(getItemPedido());
	}

	protected void afterSave(ItemPedido itemPedido) throws SQLException {
		try {
			if (LavenderePdaConfig.atualizarEstoqueInterno && LavenderePdaConfig.usaLocalEstoquePorTabelaPreco()) {
				EstoqueService.getInstance().recalculaEstoqueConsumido(itemPedido.getProduto().cdProduto);
			}
			getItemPedidoService().setEstoqueAtualizadoItemPedido(itemPedido);
			if (LavenderePdaConfig.emiteBeepAoInserirItemPedido && !VmUtil.isSimulador()) {
				try {
					Sound.setEnabled(true);
					Sound.beep();
				} catch (RuntimeException e) {
						VmUtil.debug(e.getMessage());
				}
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && itemPedido.vlDesconto3 > 0) {
				UiUtil.showInfoMessage(Messages.PEDIDO_PENDENTE_APROVACAO_ERP);
			}
			//--
			if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.isUsaDescontoQtdPorGrupo()) {
				if (getBaseCrudListForm() instanceof ListItemPedidoForm) {
					ListItemPedidoForm listItem = (ListItemPedidoForm)getBaseCrudListForm();
					if (listItem.showOnlyItensNaoConformeByDescComissao || listItem.showOnlyItensNaoConformeByDescontoGrupo) {
						listItem.list();
					}
				}
			}
			if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex() && pedido.isPedidoVenda() && pedido.descQuantidadePeso != null && !pedido.descQuantidadePeso.equals(pedido.descQuantidadePesoOld)) {
				ItemPedidoService.getInstance().recalculaVlBaseFlexItens(pedido, true);
			}
			if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
				PontuacaoConfigService.getInstance().reloadPontuacaoValorTotal(pedido, itemPedido);
			}
			if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlItemPedido() && pedido.isPedidoVenda() && pedido.descQuantidadePeso != null && !pedido.descQuantidadePeso.equals(pedido.descQuantidadePesoOld)) {
				ItemPedidoService.getInstance().recalculaVlItemPedido(pedido);
			}
			if (salvandoItemPelaListaComCdBarras) {
				return;
			}
			if (inVendaUnitariaMode && LavenderePdaConfig.usaGerenciaDeCreditoDesconto && ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(itemPedido.flTipoCadastroItem)) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PRODUTOCREDITODESCONTO_CREDITOS_GERADOS, StringUtil.getStringValueToInterface(itemPedido.qtdCreditoDesc)));
			}
			if (isAlterouDescProgressivo(itemPedido)) {
				pedido.recalculoDescontoProgressivoDTO = PedidoService.getInstance().atualizaDescProgressivoPedido(pedido);
				atualizouDescProgressivo = pedido.recalculoDescontoProgressivoDTO.atualizouDesconto;
				itemPedidoListAposRecalculoDescProgressivo = pedido.itemPedidoList;
			}
			if (LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && !PedidoService.getInstance().possuiCondicaoComercial(pedido)) {
				PedidoService.getInstance().aplicaDescQtdPorGrupoProdTodosItens(pedido, true, true);
			}
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && (itemPedido.alterouFaixa || (itemPedido.getProduto().isKit() && LavenderePdaConfig.isUsaKitBaseadoNoProduto()))) {
				aplicaDescQtdSimilaresAndUpdatePedido(itemPedido);
			}
			if (isEditing()) {
				if (inCarrouselMode && ajustouTodosItensCarrousel()) {
					removeFromCarrousel(selectedItemPedidoCarrousel);
				} else {
					voltarClick();
				}
			}
			//--
			if (MainLavenderePda.getInstance().getActualForm().equals(this) || fromListItemPedidoForm) {
				if (LavenderePdaConfig.usaFiltroFornecedorAutoAposSalvarNovo && !isPrevaleceFiltroAtributo()) {
					cbFornecedor.setValue(produtoAnterior.cdFornecedor);
					edFiltro.setValue("");
					filtrarProduto(null);
				}
				if (!itemPedido.isItemBonificacao() || !ItemPedidoService.isPedidoPossuiEsseItemNaoBonificado(itemPedido, itemPedido.pedido)) {
					if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
						ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(produtoAnterior.cdProduto);
						produtoTabPreco.itemTabelaPreco = produtoAnterior.itemTabelaPreco;
						produtoTabPreco.estoque = itemPedido.estoque;
						if (LavenderePdaConfig.usaGradeProduto5() && produtoAnterior.isProdutoAgrupadorGrade()) {
							produtoTabPreco.estoque = null;
						}
						atualizaProdutoNaGrid(produtoTabPreco, isInsereMultiplosSemNegociacao() ? itemPedido : null, true, LavenderePdaConfig.usaGradeProduto5());
					} else {
						produtoAnterior.estoque = itemPedido.estoque;
						atualizaProdutoNaGrid(produtoAnterior, isInsereMultiplosSemNegociacao() ? itemPedido : null, true, LavenderePdaConfig.usaGradeProduto5());
					}
				}
			}
			if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido && !pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
			}
			if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
				recalculaPedidoAposAlterarDescPromocional(false);
			}
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
				getItemPedidoService().enviaAtualizacao(itemPedido);
			}
		} finally {
			showPopUpRelDiferencasDescProgressivo(pedido);
		}
	}

	private void aplicaDescQtdSimilaresAndUpdatePedido(ItemPedido itemPedido) throws SQLException{
		try {
			ItemPedidoService.getInstance().aplicaDescQtdSimilares(itemPedido);
			PedidoService.getInstance().update(pedido);
			if (prevContainer instanceof ListItemPedidoForm && itemPedido.alterouFaixa) {
			itemPedido.alterouFaixa = false;
				((ListItemPedidoForm) prevContainer).list();
				return;
			}
			itemPedido.alterouFaixa = false;
			if (getBaseCrudListForm() instanceof ListItemPedidoForm && fromWindowProdutoSimilar) {
				ListItemPedidoForm listItem = (ListItemPedidoForm)getBaseCrudListForm();
				listItem.list();
				fromWindowProdutoSimilar = false;
			}
		} catch (ValidationException e) {
			return;
		}

	}

	private boolean ajustouTodosItensCarrousel() throws SQLException {
		if (carrouselType == CadItemPedidoFormWindow.CARROUSEL_TYPE_GONDOLA) {
			return !ajustouTodosItensSemQuantidade();
		} else if (carrouselType == CadItemPedidoFormWindow.CARROUSEL_TYPE_BLOQUEADO) {
			return !ajustouTodosItensBloqueados();
		} else if (carrouselType == CadItemPedidoFormWindow.CARROUSEL_TYPE_RESTRITO) {
			return !ajustouTodosItensProdutoRestrito();
		}
		return false;
	}

	private void removeFromCarrousel(int index) throws SQLException {
		itemPedidoCarrouselList.remove(index);
		selectedItemPedidoCarrousel = 0;
		edit(itemPedidoCarrouselList.get(selectedItemPedidoCarrousel));
		configureCarrouselItemPedido(itemPedidoCarrouselList, carrouselWindow, carrouselType);
	}

	private void recalculaPedidoAposAlterarDescPromocional(final boolean forceCalc) throws SQLException {
		int size = pedido.itemPedidoList.size();
		boolean pedidoFoiRecalculado = false;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoValue = (ItemPedido)pedido.itemPedidoList.items[i];
			if (!itemPedidoValue.recalculaDescPromo && !forceCalc) continue;
			pedidoFoiRecalculado = true;
			itemPedidoValue.recalculaDescPromo = false;
			if (forceCalc) {
				itemPedidoValue.loadDescPromocional(true);
			}
			getPedidoService().loadValorBaseItemPedido(pedido, itemPedidoValue);
			getItemPedidoService().calculate(itemPedidoValue, pedido);
			getItemPedidoService().update(itemPedidoValue);
		}
		if (pedidoFoiRecalculado) {
			getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
			cadPedidoForm.updateVlTotalPedido();
			}
		}

	protected void salvarClick(ItemPedido itemPedido) throws SQLException {
		beforeSave(itemPedido);
		onSave(itemPedido);
		afterSave(itemPedido);
	}
	
	protected void salvarNovoClick(ItemPedido itemPedido) throws SQLException {
		beforeSave(itemPedido);
		save(itemPedido);
		afterSave(itemPedido);
		//--
		if (!fromMenuCatalogForm) {
			add();
			}
	}

	protected void save(ItemPedido itemPedido) throws SQLException {
		insertOrUpdate(itemPedido);
		if (isEditing()) {
			updateCurrentRecordInList(itemPedido);
		} else {
			//list();
		}
	}

	protected void onSave(ItemPedido itemPedido) throws SQLException {
		save(itemPedido);
				}

	protected void onSave() throws SQLException {
		save();
			}


	protected void beforeSave() throws SQLException {
		beforeSave((ItemPedido) screenToDomain());
		}

	//@Override
	protected void beforeSave(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade())) {
			boolean hasItemPedidoPermiteEstNegativo = itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo || ItemGradeService.getInstance().validateEstoqueItemAgrGrade(itemPedido);
			if (hasItemPedidoPermiteEstNegativo && !itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo) {
				if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_GRADE_LIBERADO)){
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}
			addItemPedidoToListItemGrade(getItemPedido().itemPedidoPorGradePrecoList, itemPedido);

			Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoGrade = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
				if (hasItemPedidoPermiteEstNegativo) {
					itemPedidoGrade.isIgnoraMensagemEstoqueNegativo = true;
	}
				ItemPedidoService.getInstance().beforeSave(itemPedidoGrade, isEditing(), fromRelGiroProduto, fromRelProdutosPendentes);
			}
			itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo = false;
		} else {
			ItemPedidoService.getInstance().beforeSave(itemPedido, isEditing(), fromRelGiroProduto, fromRelProdutosPendentes);
		}
		super.beforeSave(itemPedido);
	}

	public static void validateItemPedidoUI(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		validateItemPedidoUI(itemPedido, pedido, false);
	}
	
	public boolean verificaExistenciaItemRestricao(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.getItemTabelaPreco().qtMaxVenda > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Image getBtTopRightImage() {
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			return UiUtil.getColorfulImage("images/itensOportunidade.png", UiUtil.getAppBarImageIconSize(), UiUtil.getAppBarImageIconSize());
		} else {
			return null;
		}
	}

	@Override
	protected void btTopRightClick() throws SQLException {
		super.btTopRightClick();
		cadPedidoForm.inRelatorioMode = true;
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE);
		listItemPedidoForm.show();
	}

	@Override
	protected String getBtTopRightTitle() {
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			return StringUtil.getStringValue(pedido.itemPedidoOportunidadeList.size());
		} else {
			return "";
		}
	}

	@Override
	public void updateVisibilityBtAlert() {
		super.updateVisibilityBtAlert();
		if (btAlert != null) {
			btAlert.setVisible(false);
		}
		updateVisibilityBtTopRight();
	}

	private void updateVisibilityBtTopRight() {
		try {
		if (LavenderePdaConfig.usaOportunidadeVenda && pedido.getTipoPedido() != null && btTopRight != null) {
				btTopRight.setVisible(!fromListItemPedidoForm && ValueUtil.isNotEmpty(pedido.itemPedidoOportunidadeList));
			repaintTitleAndButtons();
		}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
	}
	}

	@Override
	public void refreshDomainToScreen(ItemPedido itemPedido) throws SQLException {
		super.refreshDomainToScreen(itemPedido);
		refreshTicketMedioPedido();
		controlEstoque(itemPedido);
		bloqueiaAlteracaoPreco();
		enabledDesconto(itemPedido);
	}

	private void refreshTicketMedioPedido() {
		if (LavenderePdaConfig.geraApresentaTicketMedioDiario) {
			double sumQtItemFisico = 0.0;
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				sumQtItemFisico += ((ItemPedido) pedido.itemPedidoList.items[i]).getQtItemFisico();
			}
			double ticketMedioPedido = 0.0;
			if (sumQtItemFisico > 0.0) {
				ticketMedioPedido = pedido.vlTotalPedido / sumQtItemFisico;
			}
			edTicketMedioPedido.setValue(ticketMedioPedido);
		}
	}

	private void clearDomainAndScreen() throws SQLException {
		clearScreen();
		addNovoItem();
	}

	@Override
	protected void clearScreen() throws java.sql.SQLException {
		super.clearScreen();
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			cbUnidadeAlternativa.removeAll();
			if (gridUnidadeAlternativa != null) {
				gridUnidadeAlternativa.removeAll();
			}
		}
		refreshTicketMedioPedido();
		if (LavenderePdaConfig.isPropagaUltimoDescontoItemPedido() || LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			edVlPctDesconto.setValue(valueUltimoDescontoNegociado);
			if (LavenderePdaConfig.isPropagaUltimoDescontoItemPedido() && LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
				edVlPctDesconto2.setValue(valueUltimoDesconto2Negociado);
				edVlPctDesconto3.setValue(valueUltimoDesconto3Negociado);
			}
		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto) {
			//Quando botão de Desconto Comissao estiver disponivel na tela, teremos que esconder o botão de Excluir, pois estes ocupam a mesma posição na tela
			//Neste caso, o botão excluir passa para o menu Opções
			btExcluir.setVisible(false);
		}
	}

	protected void visibleState() throws SQLException {
		super.visibleState();
		if (inVendaUnitariaMode) { //Necessário if e else por conta da ordem das visibilidades
			containerGrid.setVisible(false);
			containerTotalizadoresMultIns.setVisible(false);
			containerVendaUnitaria.setVisible(true);
			if (isShowFotoProduto()) {
				imageCarrousel.setVisible(true);
			}
		} else {
			if (isShowFotoProduto()) {
				imageCarrousel.setVisible(false);
				if (!isEditing()) {
					imageCarrousel.setImgEmpty();
				}
			}
			containerVendaUnitaria.setVisible(false);
			if (!isEditing() && pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				containerVendaUnitaria.remove(containerInfosProduto);
			}
			containerTotalizadoresMultIns.setVisible(true);
			containerGrid.setVisible(true);
		}
		//--
		if (inVendaUnitariaMode && !LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) {
			stopScanner();
		} else {
			startScanner();
		}
		boolean editing = isEditing();
		ItemPedido itemPedido = getItemPedido();
		itemPedido.pedido = pedido;
		if (LavenderePdaConfig.isUsaVendaRelacionada() && LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado) {
			btExcluir.setVisible(editing && pedido.isPedidoAberto() && !cadPedidoForm.inOnlyConsultaItens);
		}
		btBonificar.setVisible((inVendaUnitariaMode || editing) &&
				ItemPedidoService.getInstance().isPermiteBonificarApenasProdutosInseridosNoPedido(itemPedido) &&
				ItemPedidoService.getInstance().isQtdPermiteBonificar(itemPedido) &&
				ItemPedidoService.getInstance().isPermiteBonificarItem(itemPedido) &&
				!getItemPedido().isItemBonificacao() && pedido.isPedidoAberto() &&
				!isModoGrade() && !itemPedido.isKitTipo3());
		btBonificar.setEnabled(ItemPedidoService.getInstance().isPedidoNaoPossuiEsseItemBonificado(itemPedido, itemPedido.pedido));

		btDescontos.setVisible(!inVendaUnitariaMode && LavenderePdaConfig.usaGerenciaDeCreditoDesconto && pedido.isPedidoAberto());

		if (btLibPrecoSolAutorizacao != null
				&& LavenderePdaConfig.liberaComSenhaPrecoProduto
				&& LavenderePdaConfig.usaSolicitacaoAutorizacaoPorNegociacaoDePreco()) {
			btLibPrecoSolAutorizacao.setVisible((isEditing() || inVendaUnitariaMode) && isPedidoPermiteLibPreco(itemPedido) && isMostraBtLiberarPreco(itemPedido));
		}
		btDescComi.setVisible(inVendaUnitariaMode && isEnabled());
		if (LavenderePdaConfig.usaBotaoGiroProdutoItemPedido) { 
			btGiroProduto.setVisible(!pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido() && pedido.isPedidoAberto() && !isEditing() && !cadPedidoForm.inItemNegotiationGiroProdutoPendente && !inVendaUnitariaMode && isEnabled());
		} 
		//--
		if ((gridLoteProduto != null) && (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil  || LavenderePdaConfig.restringeItemPedidoPorLocal) && !pedido.isPedidoBonificacao()) {
			gridLoteProduto.setVisible(pedido.isPedidoAberto());
		}
		if (LavenderePdaConfig.usaSelecaoPorGrid() && gridTabelaPreco != null && !pedido.isPedidoBonificacao()) {
			gridTabelaPreco.setVisible(!itemPedido.usaCestaPromo);
		}
		if (LavenderePdaConfig.isExibeHistoricoProduto()) {
			gridHistTrocaDevProduto.setVisible(possuiHistoricoTrocadevolucao);
		}
		//--
		if (LavenderePdaConfig.usaGradeProduto2()) {
			lbItemGrade1.setVisible(false);
			cbItemGrade1.setVisible(false);
		} else {
			lbItemGrade1.setVisible(cbItemGrade1.size() > 0);
			cbItemGrade1.setVisible(cbItemGrade1.size() > 0);
		}
		//--
		cbTabelaPrecoVisible();
		//--
		setInfoRentabilidadeVisible(SessionLavenderePda.isOcultoInfoRentabilidade);
		//--
		repaintTitleAndButtons();
		//--
		desabilitaCamposSeItemPedidoFazParteKitFechado();
		setEnableComponentsPossuemVariacao();
		//--
		enabledDesconto(getItemPedido());
	}

	protected boolean isPedidoPermiteLibPreco(ItemPedido itemPedido) throws SQLException {
		boolean pedidoPermiteLibPreco = !pedido.isPedidoBonificacao() && !pedido.isPedidoCritico() && pedido.isPedidoAbertoEditavel() && !itemPedido.isCombo() && !pedido.isTipoFreteFob() && !itemPedido.isKitTipo3() && !itemPedido.hasDescProgressivo();
		if (LavenderePdaConfig.isIgnoraValidacoesPedidoOrcamento()) {
			pedidoPermiteLibPreco &= (pedido.statusOrcamento == null || pedido.statusOrcamento.permiteFecharPedido());
		}
		return pedidoPermiteLibPreco;
	}

	protected boolean isMostraBtLiberarPreco(ItemPedido itemPedido) throws SQLException {
		boolean hasSolAutorizacaoItemPedido = itemPedido.solAutorizacaoItemPedidoCache.getHasSolAutorizacaoItemPedido(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
		boolean showBtLibPreco;
		if (!hasSolAutorizacaoItemPedido) {
			showBtLibPreco = true;
		} else {
			boolean isItemPedidoAutorizadoOuPendente = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizadoOuPendente(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
			showBtLibPreco = !isItemPedidoAutorizadoOuPendente;
		}
		return showBtLibPreco;
	}

	private void desabilitaCamposSeItemPedidoFazParteKitFechado() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if ((pedido.isPedidoAberto() || pedido.isPedidoReaberto) && itemPedido.isFazParteKitFechado()) {
			setEnabled(false);
			barTopContainer.setEnabled(true);
			btOpcoes.setEnabled(true);
			btEditarKit.setVisible(true);
			btEditarKit.setEnabled(true);
			containerIconsProduto.setEnabled(true);
			habilitaLabels(itemPedido);
			habilitaEditDesconto(itemPedido.cdKit);
		} else if (btEditarKit != null) {
			btEditarKit.setVisible(false);
			btEditarKit.setEnabled(false);
			habilitaDescontoKitFechado = false;
		} else {
			habilitaDescontoKitFechado = false;
		} 
	}
	
	private void habilitaEditDesconto(String cdKit) throws SQLException {
		Kit kit = KitService.getInstance().getKit(cdKit);
		if (kit != null && kit.isEditaDesconto()) {
			edVlPctDesconto.setEditable(true);
			edVlPctDesconto.setEnabled(true);
			edVlPctDesconto.drawBackgroundWhenDisabled = true;
			btSalvar.setVisible(true);
			btSalvar.setEnabled(true);
			habilitaDescontoKitFechado = true;
		}
	}

	private void habilitaLabels(ItemPedido itemPedido) {
	    for (Control child = children; child != null; child = child.getNext()) {
	    	if (child instanceof LabelName) {
	    		child.setEnabled(true);
	    	}
	    }
		int size = hashLabelsTemp.size();
		Vector hashLabelsList = hashLabelsTemp.getValues();
		for (int i = 0; i < size; i++) {
			LabelName label = (LabelName) hashLabelsList.items[i];
			if (label != null) {
				label.setEnabled(true);
			}
		}
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		desabilitaCamposSeItemPedidoFazParteKitFechado();
		super.refreshComponents();
		desabilitaCamposSeItemPedidoFazParteKitFechado();
		cbFornecedor.setEnabled(isEnabled());
		if (LavenderePdaConfig.usaSelecaoPorGrid() && gridTabelaPreco != null) {
			gridTabelaPreco.setEnabled((LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido && isEnabled());
		}
		validaExibicaoVlItemPedido();
		enabledDesconto(getItemPedido());
		populaEAtualizaTotalizadoresMultItensSemNegociacao(0);
	}


	protected String getBtVoltarTitle() {
		if (!inVendaUnitariaMode) {
			return getBtVoltarTitleOriginal();
		} else {
			return super.getBtVoltarTitle();
		}
	}

	private void cbTabelaPrecoVisible() throws SQLException {
		cbTabelaPreco.setVisible(!getItemPedido().usaCestaPromo && !LavenderePdaConfig.usaSelecaoPorGrid());
		edDsTabelaPreco.setVisible(getItemPedido().usaCestaPromo && !LavenderePdaConfig.usaSelecaoPorGrid());
		cbTabelaPreco.setID("cbTabelaPreco");
	}
	
	@Override
	public void onFormClose() throws SQLException {
		controlaEdicaoKitFechado();
		super.onFormClose();
		cadPedidoForm.solicitadoAcessoGiroProduto = false;
		cadPedidoForm.inItemNegotiationGiroProdutoPendente = false;
		if (pedido.isPedidoAberto()) {
			cadPedidoForm.afterCrudItemPedido();
		}
		if (listContainer != null && !fromListItemPedidoForm) {
			clearListContainerAndTotalizers();
		}
		reloadAtributoProd();
		edFiltro.setValue("");
		dsFiltro = "";
		listInicialized = false;
		flFromCadPedido = false;
		edComissaoMinima.setText("");
		edComissaoMaxima.setText("");
		cadPedidoForm.inRelatorioMode = prevContainer instanceof ListItemPedidoForm;
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) cbMarcador.unselectAll();
		if (fromMenuCatalogForm) {
			CadItemPedidoForm.invalidateInstance();
	}
	}

	//@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		//--
		LoadingBoxWindow msg = null;
		if (!fromMenuCatalogForm) {
			msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		}
		try {
			reloadComponents();
			if (LavenderePdaConfig.usaSelecaoPorGrid() && ValueUtil.isNotEmpty(getItemPedido().cdTabelaPreco)) {
				carregaGridTabelaPreco(getItemPedido().cdTabelaPreco);
			}
			if (!isEditing()) {
				refreshPedidoToScreen();
				inicializaCaches();
			}
			//--
			boolean recarregaInterface = pedido.isPermiteInserirMultiplosItensPorVezNoPedido();
			recarregaInterface |= LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples;
			recarregaInterface |= ValueUtil.isNotEmpty(LavenderePdaConfig.ordemCamposTelaItemPedido) && (LavenderePdaConfig.ordemCamposTelaItemPedido.indexOf("/") != -1);
			recarregaInterface |= LavenderePdaConfig.usaOportunidadeVenda;
			recarregaInterface |= LavenderePdaConfig.usaIgnoraControleVerbaNoTipoPedido;
			recarregaInterface |= LavenderePdaConfig.isUsaFlIgnoraValidaco();
			repaintScreen(recarregaInterface);
			//--
			if (isEditing() && isEnabled()) {
				boolean showDescComissaoWindow = true;
				if (LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
					Vector list = DescComiFaixaService.getInstance().findDescComiGrupoByItemPedido(pedido, getItemPedido());
					if (list.size() == 1) {
						getItemPedido().descComissaoGrupo = (DescComiFaixa) list.items[0];
						showDescComissaoWindow = false;
					}
				}
				if (LavenderePdaConfig.usaDescontoComissaoPorProduto && showDescComissaoWindow) {
					Vector list = DescComiFaixaService.getInstance().findDescComiProdByItemPedido(pedido, getItemPedido());
					if (list.size() == 1) {
						getItemPedido().descComissaoProd = (DescComiFaixa) list.items[0];
						showDescComissaoWindow = false;
					}
				}
				if (showDescComissaoWindow) {
					showDescComissaoList(false);
				}
			}
			if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
				if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
					cbCesta.load(true, SessionLavenderePda.getCliente().cdCliente, 0, false);
					cbCesta.setSelectedIndex(0);					
					cbCesta.popup();
				}
				cbCestaClick();
			}
			if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
				cbMarcador.load();
			}
			if (isCarregaProdutos()) {
				flListInicialized = true;
				carregaGridProdutos();
			}
			populaEAtualizaTotalizadoresMultItensSemNegociacao(0);
			// Necessario para girar a tela, já que uma intancia
			int dif = Settings.screenWidth - barBottomContainer.getWidth();
			if ((dif > 5) || (dif < -5)) {
				reposition();
			}
			//--
			if (isEditing()) {
				setFocusInQtde();
			} else {
				setFocusInFiltro();
			}
			if (showPopUpCredDesc) {
				btProdCreditoDescClick();
			}
			if (autoOpenDistSimilar) {
				autoOpenDistSimilar = false;
				btAgrupadorSimilaridadeClick();
			}
		} finally {
			if (msg != null)
				msg.unpop();
		}
	}

	private boolean isCarregaProdutos() {
		return (!isEditing() && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == 0) && !flListInicialized && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto != ValorParametro.PARAM_INT_VALOR_ZERO))
				|| (pedido.isSugestaoPedidoGiroProduto());
	}

	private void inicializaCaches() throws SQLException {
		if (LavenderePdaConfig.permiteAlterarValorItemComIPI || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			TributacaoService.getInstance().loadCacheTributacao(getItemPedido().pedido.getCliente());
		}
	}
	
	@Override
	protected Control getComponentToFocus() {
		return edFiltro;
	}

	@Override
	protected void onFormStart() throws SQLException {
		super.onFormStart();
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto) {
			UiUtil.add(barBottomContainer, btDescComi = new ButtonAction(Messages.DESCCOMI_NOME_BOTAO, "images/aplicadesc.png"), 2);
		} else if (LavenderePdaConfig.isUsaGiroProduto() && LavenderePdaConfig.usaBotaoGiroProdutoItemPedido) { 
			int posicao = LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido ? 2 : 1;
			UiUtil.add(barBottomContainer, btGiroProduto = new ButtonAction(Messages.GIROPRODUTO_NOME_ENTIDADE, "images/giroproduto.png"), posicao);
		} 
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			int posi = LavenderePdaConfig.isUsaGiroProduto() && LavenderePdaConfig.usaBotaoGiroProdutoItemPedido ? 4 : 1;
			UiUtil.add(barBottomContainer, btDescontos, posi);
		}
		if (isUsaBotaoBonificarItemPedido()) {
			UiUtil.add(barBottomContainer, btBonificar, 2);
		} else if (isUsaBotaoLibPrecoSolAutorizacao(pedido)) {
			UiUtil.add(barBottomContainer, btLibPrecoSolAutorizacao = new ButtonAction(Messages.BOTAO_SOL_AUTORIZACAO_LIB_PRECO, SolAutorizacaoService.IMAGES_SOLICITACAO_AUTORIZACAO_PNG), 2);
		}

		if(LavenderePdaConfig.isUsaKitProdutoFechado()) {
			UiUtil.add(barBottomContainer, btEditarKit = new ButtonAction(Messages.CAD_ITEM_PEDIDO_BOTAO_EDITAR_KIT, "images/edicao.png").setID("btEditarKit"), 1);
		}
		//--
		if (pedido.isSugestaoPedidoGiroProduto()) resetSetPositions();
		UiUtil.add(this, containerVendaUnitaria, SAME, SAME, FILL, FILL - barBottomContainer.getHeight() + 1);
		populateHashEditsAndLabels(LavenderePdaConfig.ordemCamposTelaItemPedido);
		//--
		addComponentsInScreen();
		populaEAtualizaTotalizadoresMultItensSemNegociacao(0);
		if (LavenderePdaConfig.usaCategoriaInsercaoItem()) {
			if (menuCategoriaStarted) {
				refreshContainerHeightForMenuCategoria();
			} else {
				menuCategoriaStarted = true;
				toggleVisibilityMenuCategoria();
		}
	}
	}
	
	protected boolean isUsaBotaoLibPrecoSolAutorizacao(final Pedido pedido) throws SQLException {
		return LavenderePdaConfig.isUsaSolicitacaoAutorizacao()
				&& LavenderePdaConfig.liberaComSenhaPrecoProduto
				&& LavenderePdaConfig.usaSolicitacaoAutorizacaoPorNegociacaoDePreco()
				&& !pedido.isPedidoCritico()
				&& !pedido.isPedidoBonificacao();
	}

	protected boolean isUsaBotaoBonificarItemPedido() {
		return LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao || LavenderePdaConfig.usaConfigBonificacaoItemPedido() || (LavenderePdaConfig.isUsaPoliticaBonificacao() && !LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade());
	}

	@Override
	public void reposition() {
		super.reposition();
		if ((LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil  || LavenderePdaConfig.restringeItemPedidoPorLocal) && imageCarrousel != null && imageCarrousel.isDisplayed()) {
			int spaceReserved = getHeightCbItemGrade1() + getReservedHeightCbItemGrade2() + (isModoPaisagem() ? UiUtil.getControlPreferredHeight() * 3 : UiUtil.getControlPreferredHeight() * 4);
			imageCarrousel.setRect(KEEP, KEEP, KEEP, FILL - spaceReserved);
		}
		if (LavenderePdaConfig.usaCategoriaInsercaoItem()) {
			if (!listMaximized) {
				refreshContainerHeightForMenuCategoria();
			} else {
				menuCategoriaScroll.canReposition = true;
		}
			if (!menuCategoriaHidden) {
				updateWidthButtonsMenuCategoria();
				addScrollSpacerButtonsMenuCategoria();
				updateCurrenteCategoriaText();
	}
		}
	}

	protected void btListaItensClick() throws SQLException {
		if (isInserindoItems()) {
			nuAcaoThreadMultInsercao = NU_ACAO_BTLISTAITENSCLICK;
			}
		boolean insereMultiplosSemNegociacao = isInsereMultiplosSemNegociacao();
		if (insereMultiplosSemNegociacao) {
			if (!insereUltimoItemMultInsercao()) return;
		}

		ListItemPedidoForm listItemPedidoForm;
		cadPedidoForm.inRelatorioMode = true;

		if (flFromCadPedido) {
			if (pedido.isPedidoTroca()) {
				listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
			} else if (pedido.isPedidoBonificacao()) {
				listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
			} else {
				listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			}
		} else {
			if (pedido.isPedidoTroca()) {
				listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
			} else if (pedido.isPedidoBonificacao()) {
				listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
			} else {
				listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			}
		}
		listItemPedidoForm.show();

		if (LavenderePdaConfig.usaCategoriaInsercaoItem()) {
			menuCategoriaScroll.fromItensClick = true;
		}
	}
	
	private boolean isUsaFiltroPorProdutoComDescontoPromocional() { 
		return LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() || (!LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional());
	}
	
	private void checkProdutoPromocionalANDProdutoDescPromocional(CheckBoolean check) {
		if (isUsaFiltroPorProdutoComDescontoPromocional() && LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido()) {
			if (check == ckProdutoPromocional) {
				if (ckProdutoPromocional.isChecked()) {
					ckProdutoDescPromocional.setChecked(!ckProdutoPromocional.isChecked());
					afterCkProdDescPromocionalValueChange(false);
				}	
			} else if (check == ckProdutoDescPromocional) {
				if (ckProdutoDescPromocional.isChecked()) {
					ckProdutoPromocional.setChecked(!ckProdutoDescPromocional.isChecked());
				}
			}
		}
	}
	
	public void afterCkProdDescPromocionalValueChange(boolean checked) {
		if (exibeComboLocal()) {
			if (checked) {
				cbLocal.setEnabled(true);
				cbLocal.setSelectedIndex(0);
			} else {
				cbLocal.setValue(null);
				cbLocal.setEnabled(false);
			}
		}
	}

	@Override
	protected void onFormEvent(final Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btDescComi) {
					showDescComissaoList(true);
				} else if (event.target == btBonificar) {
					btBonificarClick();
				} else if (event.target == btGiroProduto)  { 
					btGiroProdutoClick(false); 
				} else if (event.target == bgGeraVerba.bgBoolean) { 
					bgGeraVerbaChange();
				} else if (event.target == bgItemTroca.bgBoolean) {
					bgItemTrocaClick();
				} else if (event.target == bgOportunidade.bgBoolean) {
					bgOportunidadeClick();
				} else if (event.target == btLibPrecoSolAutorizacao) {
					btLibPrecoSolAutorizacaoClick();
				}
				if (event.target == cbAtributoProd) {
					cbAtributoProdChange();
					if (ValueUtil.isEmpty(cbAtributoProd.getValue())) {
						filtrarClick(event.target, NU_FILTRO_ATRIBUTO_PRODUTO);
					}
				} else if (event.target == btDescontos) { 
					btDescontosClick();
				} else if (event.target == cbAtributoOpcaoProd) {
					filtrarClick(event.target, NU_FILTRO_ATRIBUTO_PRODUTO);
					changeAtributoProdEnable();
				} else if (event.target == ckPreAltaProduto) {
					filtrarClick(event.target, NU_FILTRO_AVISO_PRE_ALTA);
				} else if (event.target == ckApenasKit) {
					filtrarClick(event.target, NU_FILTRO_APENAS_KIT);
				} else if (event.target == ckProdutoPromocional) {
					checkProdutoPromocionalANDProdutoDescPromocional(ckProdutoPromocional);
					filtrarClick(event.target, NU_FILTRO_PRODUTO_PROMOCIONAL);
				} else if (event.target == cbLocal) {
					filtrarClick(event.target, NU_FILTRO_LOCAL);
				} else if (event.target == ckProdutoDescPromocional) {
					afterCkProdDescPromocionalValueChange(ckProdutoDescPromocional.isChecked());
					checkProdutoPromocionalANDProdutoDescPromocional(ckProdutoDescPromocional);
					filtrarClick(event.target, NU_FILTRO_PRODUTO_DESC_PROMOCIONAL);
				} else if (event.target == ckProdutoOportunidade) {
					filtrarClick(event.target, NU_FILTRO_PRODUTO_OPORTUNIDADE);
				}  else if (event.target == ckApenasProdutoVendido) {
					filtrarClick(event.target, NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE);
				} else if (event.target == cbCesta) {
					cbCestaClick();
					filtrarClick(event.target, null);
				} else if (event.target == cbNaoPositivado) {
					filtrarClick(event.target, null);
				} else if (event.target == cbKit) {
					filtrarClick(event.target, NU_FILTRO_KIT);
				} else if (event.target == cbFornecedor) {
					loadGrupoProduto1ByFornecedor();
					filtrarClick(event.target, NU_FILTRO_FORNECEDOR);
				} else if (event.target == cbGrupoDescProd) {
					filtrarClick(event.target, NU_FILTRO_GRUPO_DESCONTO_PRODUTO);
				} else if (event.target == cbGrupoProduto1) {
					cbGrupoProduto1Change();
					filtrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
				} else if (event.target == cbGrupoProduto2) {
					cbGrupoProduto2Change();
					filtrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
				} else if (event.target == cbGrupoProduto3) {
					cbGrupoProduto3Change();
					filtrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
				} else if (event.target == cbGrupoProduto4) {
					filtrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
				} else if (event.target == cbProdutoGrupoDestaque) {
					filtrarClick(event.target, NU_FILTRO_PRODUTO_GRUPO_DESTAQUE);
				} else if (event.target == cbPacote) {
					filtrarClick(event.target, NU_FILTRO_PRODUTO_CBPACOTE);
				} else if (event.target == ckMaxDescProdCli) {
					filtrarClick(event.target, NU_FILTRO_DESCMAX_PRODUTO_CLIENTE);
				} else if (event.target == btFiltroAvancado) {
					verificaProdutoGenerico = true;
					btFiltroAvancadoClick();
				} else if (event.target == btFornecedorAnterior) {
					if (cbFornecedor.getSelectedIndex() > 0) {
						cbFornecedor.setSelectedIndex(cbFornecedor.getSelectedIndex() - 1);
						loadGrupoProduto1ByFornecedor();
						filtrarClick();
					}
				} else if (event.target == btFornecedorProximo) {
					cbFornecedor.setSelectedIndex(cbFornecedor.getSelectedIndex() + 1);
					loadGrupoProduto1ByFornecedor();
					filtrarClick();
				} else if (event.target == btGrupoProdutoAnterior) {
					if (cbGrupoProduto1.getSelectedIndex() > 0) {
						cbGrupoProduto1.setSelectedIndex(cbGrupoProduto1.getSelectedIndex() - 1);
						cbGrupoProduto1Change();
						filtrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
					}
				} else if (event.target == btGrupoProdutoProximo) {
					cbGrupoProduto1.setSelectedIndex(cbGrupoProduto1.getSelectedIndex() + 1);
					cbGrupoProduto1Change();
					filtrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
				} else if (event.target == btGroupTipoFiltros) {
					btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
				} else if (event.target == btIconeVerba) {
					if (isUtilizaVerba(getItemPedido())) {
						verbaClick();
					} else {
						UiUtil.showInfoMessage(Messages.NAO_UTILIZA_VERBA);
					}
				} else if (event.target == btIconeComissaoItemPedido) {
					openComissaoPedidoRepPopUp();
				} else if (event.target == btIconeBonificacao) {
					if (getItemPedido().isItemBonificacao()) {
						UiUtil.showInfoMessage(Messages.BONIFICACAO_MSG_ITEM_BONIFICADO);
					} else {
						UiUtil.showInfoMessage(Messages.BONIFICACAO_MSG_NAO_E_BONIFICADO);
					}
				} else if (event.target == btIconeKit) {
					if (getItemPedido().getProduto() != null && getItemPedido().getProduto().isKit()) {
						UiUtil.showInfoMessage(Messages.PRODUTOKIT_MSG_PRODUTO_KIT);
					} else {
						UiUtil.showInfoMessage(Messages.PRODUTOKIT_MSG_PRODUTO_NAO_KIT);
					}
				} else if (event.target == btIconeProdCreditoDesc) {
					btProdCreditoDescClick();
				} else if (event.target == btIconeOportunidade) {
					if ((getItemPedido().pedido != null && !getItemPedido().pedido.isOportunidade()) && getItemPedido().isItemPermiteOportunidade()) {
						UiUtil.showInfoMessage(Messages.OPORTUNIDADE_MSG_POSSUI_OPORTUNIDADE);
					}
				} else if (event.target == btIconeGrupoDestaque) {
					if (ValueUtil.isNotEmpty(msgGrupoDestaque)) {
						UiUtil.showInfoMessage(msgGrupoDestaque);
					}
				} else if (event.target == btIconeRentabilidade) {
					if (!LavenderePdaConfig.usaConfigMargemRentabilidade() && LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
					RelRentabilidadeFaixaWindow relRentabilidadeFaixa = new RelRentabilidadeFaixaWindow(getItemPedido(), null);
					relRentabilidadeFaixa.popup();
					} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
						RelMargemRentabFaixaWindow relMargemRentabFaixaWindow = new RelMargemRentabFaixaWindow(null, getItemPedido(), getItemPedido().vlPctMargemRentab);
						relMargemRentabFaixaWindow.popup();
					}
				} else if (event.target == btIconeOcultaInfoRentabilidade && LavenderePdaConfig.isOcultaInfoRentabilidadeManualmente()) {
					SessionLavenderePda.isOcultoInfoRentabilidade = !SessionLavenderePda.isOcultoInfoRentabilidade;
					btIconeOcultaInfoRentabilidade.setImage(getImageIconOcultaInfoRentabilidade(SessionLavenderePda.isOcultoInfoRentabilidade));
					setInfoRentabilidadeVisible(SessionLavenderePda.isOcultoInfoRentabilidade);
					if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco || LavenderePdaConfig.indiceRentabilidadePedido > 0) {
						list();
					}
				} else if (event.target == cbDescPromocional) {
					filtrarClick(event.target, NU_FILTRO_PRODUTO_DESC_PROMOCIONAL);
				} else if (event.target == btSugestao) {
					btSugestaoClick();
				} else if (event.target == btEditarKit) {
					btEditarKitClick();
				} else if (event.target instanceof ButtonMenuCategoria) {
					menuCategoriaClick((ButtonMenuCategoria) event.target);
				} else if (event.target != null && event.target instanceof ProdutoUnidadeComboBox && ((Control) event.target).getParent() instanceof ItemContainer) {
					BaseListContainer.Item item = (BaseListContainer.Item) listContainer.getSelectedItem();
					if (item != null) {
						ItemContainer itemContainer = (ItemContainer) item.rightControl;
						processaThreadInsercao(item, itemContainer, (Control) event.target);
						itemContainer.setColorsEditsByConversaoUnidade();
							}
				} else if (event.target != null && event.target == btVlPesoProdutoPontuacao) {
					tipPontuacaoBase.hide();
				} else if (event.target != null && event.target == btIconeAutorizacao) {
					openListAutorizacaoWindow();
				} else if (event.target != null && event.target == cbDescProgressivoConfig) {
					cbDescProgressivoConfigChange();
				} else if (event.target == cbDescProgConfigFam) {
					cbDescProgConfigFamChange();
				} else if (event.target == ckProdutoDescQtd) {
					if(ckProdutoDescQtd.isChecked()) {
						filtrarClick(ckProdutoDescQtd, null);
						} else {
						clearListContainerAndTotalizers();
							}
				} else if (event.target == cbMarcador) {
					filtrarClick(cbMarcador, NU_FILTRO_PRODUTO_CBMARCADOR);
				} else if (event.target == cbEstoqueDisponivel) {
					filtrarClick(event.target, NU_FILTRO_ESTOQUE_DISPONIVEL);
				} else if (event.target == ckApenasItensAdicionados) {
					filtrarClick(event.target, NU_FILTRO_ITENS_INSERIDOS);
				} else if (event.target == btIconeSimilar) {
					btAgrupadorSimilaridadeClick();
				} else if (event.target == btIconeInfoEstoqueUnidades){
					openListEstoqueFiliaisWindow();
						}
				break;
						}
			case ItemContainerChangeEvent.ITEM_CHANGE_EVENT: {
				try {
					ItemContainerChangeEvent itemEvent = (ItemContainerChangeEvent) event;
					BaseListContainer.Item item = (BaseListContainer.Item) itemEvent.targetOld;
					if (item != null) {
						ItemContainer itemContainer = (ItemContainer)item.rightControl;
						if (itemContainer != null && itemContainer.changed()) {
							setPropertiesInRowList(item, itemContainer.produto);
					}
				}
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == btOpcoes) {
					if (btOpcoes.selectedItem.equals(Messages.BOTAO_BONIFICAR)) {
						btBonificarClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTO_TITULO_CADASTRO)) {
						detalhesClick();
					} else if (btOpcoes.selectedItem.equals(Messages.SALDOBONIFICACAO_LABEL)) {
						bonificacaoSaldoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.VERBA_NOME_ENTIDADE)) {
						verbaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.RENTABILIDADE_NOME_ENTIDADE)) {
						rentabilidadeClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_HISTORICO)) {
						btHistoricoItemClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMKIT_TITULO_CADASTRO)) {
						kitProdutosClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTOKIT_TITULO_CADASTRO)) {
						btItensKitClick();
					} else if (btOpcoes.selectedItem.equals(Messages.CLIENTE_ULTIMOS_PEDIDOS)) {
						btUltimosPedidosClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_UTILITARIO_CALCULADORA)) {
						Calculator calculator = new Calculator();
						calculator.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_SLIDEFOTOS)) {
						btSlideFotosClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PESQUISAMERCADO_NOME_ENTIDADE)) {
						btPesquisaMercadoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE)) {
						btPesquisaMercadoProdutoConcorrenteClick();
					} else if (btOpcoes.selectedItem.equals(Messages.GIROPRODUTO_NOME_ENTIDADE)) {
						btGiroProdutoClick(false);
					} else if (btOpcoes.selectedItem.equals(Messages.PARAMETRO_LIBERACOMSENHAPRECOVENDA)) {
						btLiberarPrecoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_LIBERACOMSENHAVENDAPRODUTO)) {
						btLiberarPrecoProdutoBloqueadoClick(getItemPedido(), pedido);
					} else if (btOpcoes.selectedItem.equals(Messages.PARAMETRO_LIBERACOMSENHAPRECOBASEADOPERCENTUALUSUARIOESCOLHIDO)) {
						btLiberarPrecoBaseadoPercentualUsuarioEscolhido();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_REL_COMISSAO_PRECO)) {
						if (!showRelComissaoPorTabelaPreco()) {
							UiUtil.showErrorMessage(Messages.REL_COMISSAO_PRECO_TABPRECO_NAO_ENCONTRADA);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.LIMITEOPORTUNIDADE_SALDO_OPORTUNIDADE)) {
						RelLimiteOportunidadeWindow relLimiteOportunidadeWindow = new RelLimiteOportunidadeWindow(pedido);
						relLimiteOportunidadeWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE)) {
						btDescQtdClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEM_PRECOS_POR_CONDICAO)) {
						btPrecoCondicaoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTO_LABEL_PRECO_CONDCOMERCIAL)) {
						btCondComercialClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEM_DESCONTO_POR_TABELA)) {
						showDescontoPorTabela();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_RELVENPRODPORCLI)) {
						btRelVendasProdCliClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_DSOBSERVACAO)) {
						btObservacaoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.DESCONTO_PACOTE_TITULO)) {
						showDescontoPacoteList();
					} else if (btOpcoes.selectedItem.equals(Messages.DESCONTOGRUPO_NOME_ENTIDADE)) {
						showDescontoGrupoList();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_RELNOVIDADEPRODUTO)) {
						show(new ListRelNovidadeForm());
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_REGISTRAR_FALTA_PRODUTO)) {
						CadProdutoFaltaWindow cadProdutoFaltaWindow = new CadProdutoFaltaWindow(getItemPedido(), null, true, isEditing());
						cadProdutoFaltaWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_PREVISAO_DESCONTOS)) {
						previsaoDescontoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PEDIDO_ITENS_NAO_INSERIDOS)) {
						RelItemPedidoDivergenciaWindow relItemPedidoErroWindow = new RelItemPedidoDivergenciaWindow(Messages.PEDIDO_ITENS_NAO_INSERIDOS, pedido, true);
						relItemPedidoErroWindow.popup();
						if (ValueUtil.isNotEmpty(relItemPedidoErroWindow.cdProdutoSelected)) {
							produtoSelecionado = ProdutoService.getInstance().getProduto(relItemPedidoErroWindow.cdProdutoSelected);
							if (produtoSelecionado.cdProduto == null) {
								UiUtil.showErrorMessage(Messages.PRODUTO_MSG_NAO_ENCONTRADO);
								break;
							}
							gridClickAndRepaintScreen();
						}
					} else if (btOpcoes.selectedItem.equals(Messages.PEDIDO_LABEL_SUGESTAO_VENDAS)) {
						ListProdutoSugestaoSemQtdeWindow listProdutoSugestaoWindow = getListProdutoSugestaoSemQtdeWindow(false, false, true);
						if (listProdutoSugestaoWindow.hasSugestaoVenda()) {
							listProdutoSugestaoWindow.popup();
						} else {
							UiUtil.showInfoMessage(Messages.SUGESTAO_MSG_NENHUMA_VIGENTE);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.PEDIDO_LABEL_SUGESTAO_VENDAS_COM_QTDE)) {
						ListProdutoSugestaoComQtdeWindow produtoSugestaoComQtdeWindow = getListProdutoSugestaoComQtdeWindow(false, false, true);
						if ((!LavenderePdaConfig.isUsaRamoAtividadeSugestaoComQtdMinima() || pedido.getCliente().isPossuiRamoAtividade()) && produtoSugestaoComQtdeWindow.hasSugestaoVenda()) {
							produtoSugestaoComQtdeWindow.popup();
						} else {
							UiUtil.showInfoMessage(Messages.SUGESTAO_MSG_NENHUMA_COM_QTDE_VIGENTE);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.PEDIDO_LABEL_SUGESTAO_DIF_ULT_PEDIDOS)) {
						ListProdutoSugestaoDifPedidoWindow listProdutoSugestaoWindow = getListProdutoSugestaoDifPedidoWindow(false, false, true);
						if (listProdutoSugestaoWindow.hasSugestaoVendaProd()) {
							listProdutoSugestaoWindow.popup();
						} else {
							UiUtil.showInfoMessage(Messages.SUGESTAO_MSG_NENHUMA);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA)) {
						getPedidoService().calculateItemPedido(pedido, (ItemPedido)screenToDomain(), false);
						btSubstituicaoTributariaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTO_LABEL_PRINCIPIOATIVO)) {
						if (getItemPedido().getProduto() != null) {
							if (btPrincipioAtivoClick(getItemPedido(), true)) {
								gridClickAndRepaintScreen(!inVendaUnitariaMode);
							}
						} else {
							UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
						}
					} else if (btOpcoes.selectedItem.equals(FrameworkMessages.BOTAO_EXCLUIR)) {
						excluirClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_DESBLOQUEAR_LIMITADOR)) {
						btDesbloquearLimitador(pedido);
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_SIMILARES)) {
						btProdutosSimilares(getItemPedido());
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_GRUPOS_PENDENTES)) {
						RelGrupoProdutoNaoInseridoPedidoWindow relGrupoProdutoNaoInseridoPedidoWindow = new RelGrupoProdutoNaoInseridoPedidoWindow(super.title, false, pedido);
						relGrupoProdutoNaoInseridoPedidoWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_PRODUTOS_PENDENTES)) {
						showPopupRelProdutoPendente(true);
					} else if (btOpcoes.selectedItem.equals(Messages.DESC_PROMOCIONAL_POR_QTDE)) {
						btDescPromocionalPorQtdeClick();
					} else if (btOpcoes.selectedItem.equals(Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA)) {
						if (pedido.isPedidoAberto()) {
						if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && getSelectedProduto() != null) {
							if (getSelectedProduto() != null) {
								calculateItemPedidoQtUnitario(getSelectedProduto());
							}	
						}
						getPedidoService().calculateItemPedido(pedido, getItemPedido(), false);
						}
						RelInfoTributariaDoItemPedidoWindow relInfoTributariaDoItemPedidoWindow = new RelInfoTributariaDoItemPedidoWindow(getItemPedido());
						relInfoTributariaDoItemPedidoWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_INFORMACOES_EXTRAS)) {
						btInfoExtraItemPedidoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.REL_RENTABILIDADE_COMISSAO_TITULO)) {
						RelRentabilidadeComissaoWindow relRentabilidadeComissao = new RelRentabilidadeComissaoWindow(getItemPedido());
						relRentabilidadeComissao.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_DETALHES_VARIAVEIS_DE_CALCULO)) {
						if (getItemPedido().variavelCalculoList != null) {
							RelVariaveisCalculoWindow relVariaveisCalculoWindow = new RelVariaveisCalculoWindow(getItemPedido().variavelCalculoList);
							relVariaveisCalculoWindow.popup();
						} else {
							UiUtil.showInfoMessage(Messages.MSG_LISTA_VARIAVEIS_VAZIA);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_DETALHES_CALCULOS)) {
						boolean funcionalidadesDisponiveis = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || LavenderePdaConfig.isUsaRentabilidadeNoPedido() || LavenderePdaConfig.isUsaCalculaStItemPedido();
						if (funcionalidadesDisponiveis) {
							if (getItemPedido().getProduto() != null) {
								RelDetalhesCalculosItemWindow relDetalhesCalculos = new RelDetalhesCalculosItemWindow(pedido, getItemPedido());
								relDetalhesCalculos.popup();
							} else {
								UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
							}
						} else {
							UiUtil.showInfoMessage(Messages.REL_DETALHES_CALCULOS_NENHUMA_FUNC_DISPONIVEL);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.PEDIDO_TITULO_SUGESTAO_RENTABILIDADE_IDEAL)) {
						ListSugestaoItensRentabilidadeIdealWindow listSugestaoItensRentabilidadeIdealWindow = new ListSugestaoItensRentabilidadeIdealWindow(pedido, true, true);
						listSugestaoItensRentabilidadeIdealWindow.cadPedidoForm = cadPedidoForm;
						listSugestaoItensRentabilidadeIdealWindow.singleClickOn = pedido.isPedidoAberto();
						listSugestaoItensRentabilidadeIdealWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEM_PRODUTO_LOTE)) {
						menuProdutoLoteClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_RECALCULAR_ESTOQUE)) {
						if (!getItemPedido().pedido.isIgnoraControleEstoque(getItemPedido()) && LavenderePdaConfig.atualizarEstoqueInterno && !LavenderePdaConfig.ocultaRecalculaEstoque) {
							if (ValueUtil.isNotEmpty(getItemPedido().cdProduto)) {
								EstoqueService.getInstance().recalculaEstoqueConsumido(getItemPedido().cdProduto);
								controlEstoque(getItemPedido());
							} else {
								EstoqueService.getInstance().recalculaEstoqueConsumido("");
							}
							UiUtil.showInfoMessage(Messages.ITEMPEDIDO_MSG_ESTOQUE_RECALCULADO);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.USUARIODESC_NOME_ENTIDADE)) {
						btConfiguracaoDescontoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.REL_DIF_ITEMPEDIDO_LABEL_DIFERENCAS)) {
						btDiferencaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_REL_DESCONTOS)) {
						btRelDescontosClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTORETIRADA_NOME_BT)) {
						btProdutosRetiradaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.REL_LIBERACOES_SENHA_TITULO)) {
						btRelLiberacoesSenhaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.BT_DESCONTO_FAIXA_PESO)) {
						btFaixasDescPesoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTODESEJADO_LABEL_BOTAO)) {
						btProdutoDesejadoClick(false);
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_DESCONTO_VENDAS_MES)) {
						if (LavenderePdaConfig.isConfigColunasDescontoVolumeVendaMensalDesligado()) {
							UiUtil.showErrorMessage(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_CONFIG_COLUNAS_EXCEPTION);
						} else {
							new ListDescontoVendaVolumeMensal(pedido).popup();
						}
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_HISTLISTATABPRECO)) {
						TabelaPreco tabPreco = pedido.getTabelaPreco();
						if (tabPreco != null && ValueUtil.isNotEmpty(tabPreco.cdTabelaPreco)) {
							Produto produto = getItemPedido().getProduto();
							new RelHistListaTabPrecoWindow(pedido, tabPreco.cdListaTabelaPreco, tabPreco.cdColunaTabelaPreco, produto != null ? produto.cdGrupoProduto1 : null, produto != null ? getItemPedido() : null).popup();
						} else {
							UiUtil.showWarnMessage(Messages.PEDIDO_HISTORICO_TABPRECO_VAZIO);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_CALCULA_DESC_QTD_PARA_EMB_COMPLETA)) {
						btCalculaDescQtdEmbalagemCompletaClick(getItemPedido());
					} else if (btOpcoes.selectedItem.equals(Messages.LABEL_INFOS_COMPLEMENTARES)) {
						(new CadInfoComplementarItemPedidoWindow(getItemPedido(), pedido.isPedidoAberto())).popup();
						if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto() || LavenderePdaConfig.calculaPrecoPorVolumeProduto) {
							calcularItemEspecifico(getItemPedido(), getItemPedido());
							refreshDomainToScreen(getItemPedido());
						}
					} else if (btOpcoes.selectedItem.equals(Messages.PEDIDO_TITULO_SUGESTAO_MULTIPLOS_PRODUTOS)) {
						btSugestaoMultProdutosClick();
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_COMISSAO_ITEMPEDIDO_REP_WINDOW)) {
						openComissaoPedidoRepPopUp();
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_ULTIMOS_PRECOS_REDE)) {
						if (ValueUtil.isEmpty(pedido.getCliente().cdRede)) {
							UiUtil.showWarnMessage(Messages.REDECLIENTE_REDE_INVALIDA);
						} else if (ClienteService.getInstance().isPossuiUltimosPrecosRede(getItemPedido(), pedido.getCliente().cdRede)) {
							new RelUtimosPrecosRede(getItemPedido(), pedido.getCliente().cdRede).popup();
						} else {
							UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_SEM_ULTIMOS_PRECOS_REDE, LavenderePdaConfig.nuDiasConsideraPedido));
						}
					} else if (btOpcoes.selectedItem.equals(Messages.OBSERVACAO_CLIENTE)) {
						exibePopupObservacaoCliente();
					} else if (btOpcoes.selectedItem.equals(Messages.SUGESTAO_COMBO_PRODUTO)) {
						openListItemComboSugestaoWindow();
					} else if (btOpcoes.selectedItem.equals(Messages.SOL_AUTORIZACAO_ITEM_PEDIDO)) {
						openListAutorizacaoWindow();
					} else if (btOpcoes.selectedItem.equals(Messages.DESC_PROG_PEDIDO_BUTTON)) {
						btDescProgressivoConfigClick(pedido);
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_AGRUPADOR_SIMILARIDADE)) {
						btAgrupadorSimilaridadeClick();
					} else if (btOpcoes.selectedItem.equals(Messages.TITULO_POLITICAS_BONIFICACAO)) {
						new ListBonifCfgWindow(pedido).popup();
					} else if (btOpcoes.selectedItem.equals(Messages.ESTOQUE_FILIAIS)) {
						openListEstoqueFiliaisWindow();
					} else if (btOpcoes.selectedItem.equals(Messages.GERAR_CATALOGO)) {
						btGerarCatalogoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_SUGESTOES)) {
						btSugestaoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ATENDIMENTOHIST_TITLE)) {
						WindowUtil.btAtendimentoHistClick(this, getItemPedido().pedido.cdEmpresa, getItemPedido().pedido.cdCliente);
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_SOL_AUTORIZACAO_LIB_PRECO)) {
						btLibPrecoSolAutorizacaoClick();
					}
				}
				break;
			}
			case ImageCarrouselEvent.IMAGE_CLICKED: {
				ImageCarrousel imCarrousel = (ImageCarrousel)event.target;
				if (!imCarrousel.equals(imageCarrousel)) {
					if (imCarrousel.imgList.size() > 0) {
						String[] imagemSelected = imCarrousel.getSelectedImage();
						String cdProduto = null;
						for (int i = 0; i < nmImageList.size(); i++) {
							String[] info = (String[]) nmImageList.elementAt(i);
							if (info[0].equals(imagemSelected[0])) {
								cdProduto = info[1];
								break;
							}
						}
						produtoSelecionado = ProdutoService.getInstance().getProduto(cdProduto);
						gridClickAndRepaintScreen(false);
					}
				} else {
					showFotosProdutoWindow();
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == gridLoteProduto) && (gridLoteProduto.getSelectedIndex() != -1)) {
					if (gridLoteProduto.getSelectedIndex() != -1) {
						gridLoteProdutoClick();
					}
				}
				if (event.target == gridTabelaPreco && gridTabelaPreco.getSelectedIndex() != -1) {
					gridTabelaPrecoClick();
				}
				if ((listContainer.getSelectedIndex() != -1) && pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
					vlPctDescontoMultiplosItens = edVlPctDesconto.getValueDouble();
					qtItemFisicoMultiplosItens = edQtItemFisico.getValueDouble();
				}
				break;
			}
			case ListContainerEvent.RIGHT_IMAGE_CLICKED_EVENT: {
				if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
					vlPctDescontoMultiplosItens = edVlPctDesconto.getValueDouble();
					qtItemFisicoMultiplosItens = edQtItemFisico.getValueDouble();
				}
				break;
			}
			case ListContainerEvent.LEFT_IMAGE_CLICKED_EVENT: {
				if (LavenderePdaConfig.isLoadImagesOnProdutoList()) {
				ImageSliderProdutoWindow imageSliderProdutoWindow = new ImageSliderProdutoWindow(getSelectedProduto());
				imageSliderProdutoWindow.popup();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == edComissaoMinima || event.target == edComissaoMaxima) {
					filtrarClick(event.target, null);
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (isInsereMultiplosSemNegociacao() && event.target != null) {
					Control parent = ((Control)event.target).getParent();
					if (parent instanceof ValueChooser || parent instanceof ItemContainer) {
						if (event.target instanceof EditNumberFrac) {
							BaseListContainer.Item item = null;
							Container parente = ((EditNumberFrac) event.target).getParent();
							if (parente instanceof ItemContainer) {
								ItemContainer ic = (ItemContainer) ((EditNumberFrac) event.target).getParent();
								if (ic.index >= 0) {
									item = (BaseListContainer.Item) listContainer.getContainer(ic.index);
								}
							} else {
								parente = parent.getParent();
								if (parente instanceof ItemContainer) {
									ItemContainer ic = (ItemContainer) parente;
									if (ic.index >= 0) {
										item = (BaseListContainer.Item) listContainer.getContainer(ic.index);
									}
								}
							}
								if (item != null && item.rightControl != null) {
								ItemContainer itemContainer = (ItemContainer)item.rightControl;
								itemContainer.changedValue = event.target == itemContainer.edVlItemPedido;
								processaThreadInsercao(item, itemContainer, parent);
								itemContainer.setColorsEditsByConversaoUnidade();
								ValueChooser chooserDesc = itemContainer.chooserDescAcresc;
								if (chooserDesc != null && chooserDesc.isValueMaxReached() && !LavenderePdaConfig.isPermiteEditarDescontoAcrescimoMultiplaInsercao()) {
									chooserDesc.disableBtMais();
							}
						}
					}
				}
				} else if (inCarrouselMode && event.target == edQtItemFisico && edQtItemFisico.isEditable()) {
					if (!firstCarrouselValidation) {
						firstCarrouselValidation = true;
				break;
			}
					double qtItemFisico = edQtItemFisico.getValueDouble();
					try {
						if (validaAlterarQtdItem(getItemPedido())) {
							if (getItemPedido().itemChanged) refreshDomainToScreen(getItemPedido());
							edQtItemFisico.setValue(qtItemFisico);
							edQtItemFisicoValueChange(getItemPedido());
							calcularClick();
						} else {
							edQtItemFisico.setValue(getItemPedido().getQtItemFisico());
						}
					} catch (Throwable e) {
						edQtItemFisico.setValue(getItemPedido().getQtItemFisico());
						throw e;
					}
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if (isInsereMultiplosSemNegociacao() && !isModoGrade(false)) {
					//-- Acesso ao detalhe do item pela tela de multipla inserção
					if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() && houveAlteracaoItemPorMultInsercao && (event.target instanceof ItemContainer || event.target instanceof BaseListContainer.Item)) {
						if (isInserindoItems()) {
							nuAcaoThreadMultInsercao = NU_ACAO_DETALHE;
							return;
								}

						showDetalheItemJaInseridoMultInsercao = true;
						ItemContainer itemContainer = getSelectedItemContainer();
						if (itemContainer != null) {
							listContainerProdutoClick();
							}
						houveAlteracaoItemPorMultInsercao = false;
					}

				}
				break;
			}
			case GridEditEvent.COLUMN_PRESSED: {
				if (event.target == gridLoteProduto) {
					int coluna = ((GridEditEvent) event).nuColumn;
					if (coluna == COLUNA_LOTE) {
						sortGridLote(COLUNA_LOTE, getItemPedido().produtoLoteList);
					} else if (coluna == COLUNA_DT_VALIDADE) {
						sortGridLote(COLUNA_DT_VALIDADE, getItemPedido().produtoLoteList);
					} else if (coluna == COLUNA_VL_PCTVIDAUTILPRODUTO) {
						sortGridLote(COLUNA_VL_PCTVIDAUTILPRODUTO, getItemPedido().produtoLoteList);
					} else if (coluna == COLUNA_ESTOQUE) {
						sortGridLote(COLUNA_ESTOQUE, getItemPedido().produtoLoteList);
					} else if (coluna == COLUNA_ESTOQUE_RESERVADO) {
						sortGridLote(COLUNA_ESTOQUE_RESERVADO, getItemPedido().produtoLoteList);
					} else if (coluna == COLUNA_VL_PCT_DESCONTO) {
						sortGridLote(COLUNA_VL_PCT_DESCONTO, getItemPedido().produtoLoteList);
					} else if (coluna == COLUNA_VL_BASE_ITEM_STRING) {
						sortGridLote(COLUNA_VL_BASE_ITEM_STRING, getItemPedido().produtoLoteList);
					}
				}
			}
			case BaseGridEdit.AFTER: {
				setaCorLoteProduto(getItemPedido().produtoLoteList);
			}
		}
	}
	
	private void showPopupRelProdutoPendente(boolean ocultaProdutoPendenteMenuOpcoes) {
		try {
			isOcultaProdutoPendenteMenuOpcoes = ocultaProdutoPendenteMenuOpcoes;
			RelProdutosPendentesWindow relProdutosPendentesWindow = RelProdutosPendentesWindow.getNewInstance(pedido, true, false);
			relProdutosPendentesWindow.cadPedidoForm = cadPedidoForm;
			relProdutosPendentesWindow.fromVendaUnitaria = inVendaUnitariaMode;
			relProdutosPendentesWindow.popup();
		} catch (ValidationException ex) {
			UiUtil.showWarnMessage(ex.getMessage());
		} finally {
			RelProdutosPendentesWindow.cleanInstance();
		}
	}

	private void btSugestaoClick() throws SQLException {
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			if (listSugestaoPersonMultProdutosWindow == null || LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
				loadSugVendaPersonWindow();
				if (listSugestaoPersonMultProdutosWindow == null) {
					UiUtil.showWarnMessage(Messages.MULTIPLASSUGESTOES_ERRO_SEM_SUGESTOES);
				}
						} else {
				listSugestaoPersonMultProdutosWindow.list();
				if (listSugestaoPersonMultProdutosWindow.hasSugestoes()) {
					listSugestaoPersonMultProdutosWindow.popup();
				} else {
					UiUtil.showWarnMessage(Messages.MULTIPLASSUGESTOES_ERRO_SEM_SUGESTOES);
							}
						}

		} else {
			listMultiplasSugestoesProdutosNovoItemWindow = getListMultiplasSugestoesProdutosNovoItemWindow(false);
			if (listMultiplasSugestoesProdutosNovoItemWindow.hasSugestoes()) {
				listMultiplasSugestoesProdutosNovoItemWindow.popup();
				afterShowMultSugesProdutosWindow(listMultiplasSugestoesProdutosNovoItemWindow);
			} else {
				UiUtil.showWarnMessage(Messages.MULTIPLASSUGESTOES_ERRO_SEM_SUGESTOES);
					}
				}
	}

	private void openListEstoqueFiliaisWindow() throws SQLException {
		Produto produto = getItemPedido().getProduto();
		produto = produto == null ? getSelectedProduto() : produto;
		if (produto == null) {
			UiUtil.showErrorMessage(Messages.PRODUTO_NENHUM_SELECIONADO);
			return;
		}
		ListEstoqueFiliaisWindow listEstoqueFiliaisWindow =  new ListEstoqueFiliaisWindow(produto);
		show(listEstoqueFiliaisWindow);

	}

	private void openListItemComboSugestaoWindow() throws SQLException {
		new ListItemComboSugestaoWindow(pedido, null, null, null, false, getCdTabelaPreco()).popup();
	}

	private void alterarTipoTeclado() {
		if (filterByCodigoProduto && LavenderePdaConfig.isExibeBotaoParaFiltroCodigoTecladoNumerico()) {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_INT);
		} else {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_TEXT);
		}
	}

	public void processaThreadInsercao(BaseListContainer.Item itemControl, ItemContainer itemContainer, Control ctrlParent) {
		initThread(itemControl, itemContainer, ctrlParent);
	}

	private void initThread(Item itemControl, ItemContainer itemContainer, Control ctrlParent) {
		ThreadItem threadItem = new ThreadItem(itemContainer.produto.cdProduto, itemControl, itemContainer, ctrlParent);
		if (filaMultiplaInsercao == null) filaMultiplaInsercao = new LinkedList<>();
		synchronized (lock) {
			if (filaMultiplaInsercao.contains(threadItem)) {
				filaMultiplaInsercao.remove(threadItem);
			}
			filaMultiplaInsercao.add(threadItem);
		}
		if (insercaoMultiplaThread == null || ! insercaoMultiplaThread.running) {
			insercaoMultiplaThread = new InsercaoMultiplaThread();
			insercaoMultiplaThread.start();
		}
	}

	private void processaNuAcaoThreadMultInsercao() {
		boolean popupInserindoItensAberta = MainLavenderePda.getInstance().verificaPopupAbertaEmAlgumNivel(popupInserindoItens);
		if (popupInserindoItensAberta && ! isInserindoItems()) {
			try {
				int count = 0;
				while (popupInserindoItensAberta && MainWindow.getPopupCount() > 0) {
					Vm.safeSleep(1000);
					if (ValueUtil.valueEquals(popupInserindoItens, MainWindow.getTopMost())) {
						popupInserindoItens.unpop();
				break;
			}
					count++;
					if (count > 5) {
						popupInserindoItensAberta = MainLavenderePda.getInstance().verificaPopupAbertaEmAlgumNivel(popupInserindoItens);
						count = 0;
				}				
				}
				switch (nuAcaoThreadMultInsercao) {
					case NU_ACAO_VOLTARCLICK:
						voltarClick();
				break;
					case NU_ACAO_BTLISTAITENSCLICK:
						btListaItensClick();
						break;
					case NU_ACAO_FILTRAR_PRODUTO:
						if (targetFiltroThread != null) {
							filtrarProduto(targetFiltroThread);
			}
						break;
					case NU_ACAO_DETALHE:
						break;
					default:
						break;
		}
				nuAcaoThreadMultInsercao = 0;
			} catch (SQLException ex) {
				ExceptionUtil.handle(ex);
	}
		}
	}

	private void btEditarKitClick() throws SQLException {
		Kit kit = (Kit) KitService.getInstance().findByPrimaryKey(new Kit(pedido.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante , getItemPedido().cdKit));
		if (kit == null) kit = new Kit(getItemPedido().cdKit);
		ListItemKitForm listItemKitForm = new ListItemKitForm(kit, pedido);
		listItemKitForm.setCadItemPedidoForm(this);
		show(listItemKitForm);
	}

	private void btLibPrecoSolAutorizacaoClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.setQtItemFisico(edQtItemFisico.getValueDouble());
		if (itemPedido.getQtItemFisico() == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
		}
		if (!isItemPedidoAlterado(itemPedido)) {
			throw new ValidationException(Messages.ERRO_SOLICITACAO_PRECO_SEM_ALTERACOES);
		}
		double vlMinAutorizacaoPreco = itemPedido.getProduto().vlMinAutorizacaoPreco;
		if (itemPedido.vlTotalItemPedido < vlMinAutorizacaoPreco) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_VALIDACAO_MIN, new String[] {StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido), StringUtil.getStringValueToInterface(vlMinAutorizacaoPreco)}));
			return;
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			boolean hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido = !LavenderePdaConfig.isIgnoraAgrupadorSimilaridade() && itemPedido.solAutorizacaoItemPedidoCache.getHasSolAutorizacaoPendenteOuAutorizadaSimilarPedido(pedido, null, itemPedido.getProduto().cdAgrupadorSimilaridade);
			if (hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido) {
				UiUtil.showErrorMessage(Messages.ERRO_PEDIDO_POSSUI_OUTRO_SIMILAR_AUTORIZADO);
				return;
			}
		}
		if (LavenderePdaConfig.liberaNegociacaoPrecoApenasDesconto && itemPedido.vlTotalItemPedido >= itemPedido.vlTotalBrutoItemPedido) {
			UiUtil.showErrorMessage(Messages.ERRO_BLOQUEIA_SOLICITACAO_ACRESCIMO);
			return;
		}
		itemPedido.pedido = pedido;
		if (!UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_CONFIRM_NEGOCIACAO, StringUtil.getStringValueToInterface(itemPedido.vlItemPedido)))) {
			return;
		}
		itemPedido.auxiliarVariaveis.isItemEmSolicitacaoPreco = true;
		itemPedido.ignoraValidacaoDesconto = true;
		itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_SIM;
		itemPedido.qtItemMinAfterLibPreco = itemPedido.getQtItemFisico();
		itemPedido.vlItemMinAfterLibPreco = itemPedido.vlItemPedido;
		try {
			if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
				VerbaService.getInstance().resetVerba(pedido, itemPedido);
			}
			boolean itemPossuiaDescQtd = itemPedido.isRecebeuDescontoPorQuantidade();
			salvarClick();
			SolAutorizacaoService.getInstance().insertAutorizacaoByItemPedido(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO, true, false);
			PedidoService.getInstance().findItemPedidoList(pedido, true);
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && itemPossuiaDescQtd) {
				ItemPedidoService.getInstance().aplicaDescQtdSimilares(itemPedido);
				PedidoService.getInstance().update(pedido);
			}
			UiUtil.showSucessMessage(Messages.SOL_AUTORIZACAO_NEGOCIACAO_SUCESSO);
			voltarClick();
		} catch (Throwable e) {
			itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_NAO;
			throw e;
		} finally {
			itemPedido.ignoraValidacaoDesconto = false;
			itemPedido.auxiliarVariaveis.isItemEmSolicitacaoPreco = false;
		}
	}

	public boolean isItemPedidoAlterado(ItemPedido itemPedido) {
		return (itemPedido.vlPctDesconto > 0 && itemPedido.vlPctDesconto != itemPedido.oldVlPctDesc)
				|| (itemPedido.vlItemPedido != itemPedido.vlBaseItemPedido)
				|| itemPedido.vlPctAcrescimo > 0;
	}

	private void openListAutorizacaoWindow() throws SQLException {
		new ListSolAutorizacaoWindow(null, getItemPedido()).popup();
	}

	public void afterShowMultSugesProdutosWindow(ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutosNovoItemWindow) throws SQLException {
		if (listMultiplasSugestoesProdutosNovoItemWindow == null) return;
		if (listMultiplasSugestoesProdutosNovoItemWindow.produtoIndustriaSelected != null) {
			produtoIndustriaFilter = listMultiplasSugestoesProdutosNovoItemWindow.produtoIndustriaSelected;
			filtrarClick();
			produtoIndustriaFilter = listMultiplasSugestoesProdutosNovoItemWindow.produtoIndustriaSelected = null;
		}
	}

	private void exibePopupObservacaoCliente() throws SQLException {
		if (!LavenderePdaConfig.mostraObservacaoCliente() || !pedido.isPedidoAberto() || ValueUtil.isEmpty(pedido.getCliente().dsObservacao)) return;

		ObservacaoClienteWindow observacaoClienteWindow = new ObservacaoClienteWindow(pedido.getCliente().dsObservacao );
		observacaoClienteWindow.popup();
	}

	private void openComissaoPedidoRepPopUp() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.cdComissaoPedidoRep == 0 || itemPedido.comissaoPedidoRep == null) {
			UiUtil.showInfoMessage(Messages.NENHUMA_COMISSAO_ATINGIDA);
		} else {
			new RelComissaoPedidoRepWindow(null, itemPedido).popup();
		}
	}

	private void loadGrupoProduto1ByFornecedor() throws SQLException {
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
			Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
			cbGrupoProduto1.loadGrupoProduto1(pedido, fornecedorSelecionado);
		}
	}
	
	private void bonificacaoSaldoClick() throws SQLException {
		RelBonificacaoSaldoWindow relBonificacaoSaldoWindow = new RelBonificacaoSaldoWindow();
		relBonificacaoSaldoWindow.popup();
	}

	private void btProdCreditoDescClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		boolean credAplicado = ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem) && itemPedido.qtdCreditoDesc > 0;
		ItemPedido itemPedidoComDesconto = (ItemPedido) itemPedido.clone();
		RelCreditoDescontoWindow relCreditoDescontoWindow = new RelCreditoDescontoWindow(pedido, itemPedidoComDesconto, credAplicado);
		relCreditoDescontoWindow.popup();
		if (relCreditoDescontoWindow.qtdCredito > 0) {
			validaAplicacaoCreditoDesconto(itemPedidoComDesconto, relCreditoDescontoWindow);
			itemPedido.qtdCreditoDescOld = itemPedidoComDesconto.qtdCreditoDescOld;
			itemPedido.cdProdutoCreditoDesc = itemPedidoComDesconto.cdProdutoCreditoDesc;
			itemPedido.produtoCreditoDesc = itemPedidoComDesconto.produtoCreditoDesc;
			pedido.qtdCreditoDescontoConsumido += relCreditoDescontoWindow.qtdCredito - itemPedido.qtdCreditoDescOld;
			itemPedido.qtdCreditoDesc = relCreditoDescontoWindow.qtdCredito;
			itemPedido.flTipoCadastroItem = ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO;
			ItemPedidoService.getInstance().aplicaDescontoPorCredito(pedido, itemPedido);
			pedido.itemPedidoList.removeElement(itemPedido);
			itemPedido.itemChanged = true;
			pedido.atualizaLista = true;
			refreshDomainToScreen(itemPedido);
			calcularClick();
			pedido.itemPedidoList.addElement((ItemPedido) itemPedido.clone());
			ItemPedidoService.getInstance().updateItemSimples(itemPedido);
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
			if (prevContainer instanceof ListItemPedidoForm) {
				((ListItemPedidoForm)prevContainer).updateCurrentRecord(itemPedido);
			}
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PRODUTOCREDITODESCONTO_QTCREDITOS_CONSUMIDOS, relCreditoDescontoWindow.qtdCredito));
		}
	}

	private void validaAplicacaoCreditoDesconto(ItemPedido itemPedidoComDesconto, RelCreditoDescontoWindow relCreditoDescontoWindow) throws SQLException {
		itemPedidoComDesconto.qtdCreditoDesc = relCreditoDescontoWindow.qtdCredito;
		itemPedidoComDesconto.flTipoCadastroItem = ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO;
		ItemPedidoService.getInstance().aplicaDescontoPorCredito(pedido, itemPedidoComDesconto);
		validateItemPedidoAplicaCreditoDescUI(itemPedidoComDesconto, pedido, true);
	}

	private void btFaixasDescPesoClick() {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		ListDescQuantidadePesoWindow listDescQuantidadePesoWindow = null;
		try {
			if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
				listDescQuantidadePesoWindow = new ListDescQuantidadePesoWindow(getItemPedido());
			} else {
			listDescQuantidadePesoWindow = new ListDescQuantidadePesoWindow();
			}
			listDescQuantidadePesoWindow.setDefaultRect();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		} finally {
			mb.unpop();
		}
		listDescQuantidadePesoWindow.popup();
	}

	private void bgItemTrocaClick() throws SQLException {
		if (bgItemTroca.getValueBoolean()) {
			try {
				ItemPedidoService.getInstance().validaItemTroca(getItemPedido());
				getItemPedido().flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC;
			} catch (Throwable e) {
				bgItemTroca.setValueBoolean(false);
				throw e;
			}
		} else {
			getItemPedido().flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		}
	}

	private void bgGeraVerbaChange() throws SQLException {
		getItemPedido().getItemPedidoAud().flGeraVerba = bgGeraVerba.getValue();
		VerbaService.getInstance().calculaVerbaComImpostoERentabilidade(getItemPedido());
		ItemPedidoService.getInstance().calculaIndiceRentabilidadeItemSemTributos(getItemPedido());
		edVlVerbaNecessaria.setValue(getItemPedido().getItemPedidoAud().vlVerbaNecessaria);
		edVlIndiceRentabItem.setValue(getItemPedido().vlRentabilidade);
	}

	private void bgOportunidadeClick() throws SQLException {
		getItemPedido().flOportunidade = bgOportunidade.getValue();
		getItemPedido().vlPctAcrescimo = 0;
		getItemPedido().vlPctDesconto = 0;
		//-
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			String cdProdutoUnidadeInicial = getItemPedido().cdUnidade;
			cbUnidadeAlternativa.load(getItemPedido());
			if (LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 2) {
				carregaGridUnidadesProduto(getItemPedido(), cbUnidadeAlternativa.size() > 0 ? new Vector(cbUnidadeAlternativa.getItems()) : new Vector());
				selectUnidadeNaGrid(cdProdutoUnidadeInicial);
				if (gridUnidadeAlternativa.getSelectedIndex() != -1) {
					cdProdutoUnidadeInicial = gridUnidadeAlternativa.getSelectedItem()[0];
				}
			}
			cbUnidadeAlternativa.setValue(cdProdutoUnidadeInicial);
			if (cbUnidadeAlternativa.getSelectedIndex() == -1) {
				cbUnidadeAlternativa.setSelectedIndex(0);
			}
			getItemPedido().cdUnidade = cbUnidadeAlternativa.getValue();
		}
		//--
		PedidoService.getInstance().loadValorBaseItemPedido(pedido, getItemPedido());
		setEnableComponentsPossuemVariacao();
		refreshDomainToScreen(getItemPedido());
	}

	private void btDescontosClick() throws SQLException {
		ListProdutoCreditoDescontoWindow listProdutoCreditoDescontoWindow = new ListProdutoCreditoDescontoWindow(pedido, getCdTabelaPreco());
		listProdutoCreditoDescontoWindow.popup();
		if (listProdutoCreditoDescontoWindow.cdProduto != null) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.cdProduto.equals(listProdutoCreditoDescontoWindow.cdProduto)) {
					disabledBtNextPrev = true;
					edit(itemPedido);
					showDescontoClick(this);
					btProdCreditoDescClick();
					fromWindowDesconto = true;
					showListDescontoCredito = true;
					break;
				}
			}
		}
	}

	private String getCdTabelaPreco() {
		if (LavenderePdaConfig.ocultaTabelaPrecoPedido) {
			return cbTabelaPreco.getValue();
		}
		return pedido.cdTabelaPreco;
	}
	
	public void showDescontoClick(final BaseContainer form) throws SQLException {
		if (this.useScanner) {
			stopScanner();
		}
		if (form instanceof BaseUIForm && ((BaseUIForm) form).useScanner && !BaseMainWindow.getBaseMainWindowInstance().useBarCodeDriverLess) {
					ScannerUtil.start(useScanner);
					if (ScannerUtil.isStarted()) {
						Scanner.listener = this;
					}
				}
		MainWindow.getMainWindow().swap(this);
		onFormShow();
	}
	
	private void cbGrupoProduto2Change() throws SQLException {
			cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
		cbGrupoProduto4.setSelectedIndex(0);
	}
	
	private void cbGrupoProduto3Change() throws SQLException {
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	@Override
	protected void voltarClick() throws SQLException {
		if (fromMenuCatalogForm) {
			close();
			return;
		}
		disabledBtNextPrev = false;
		showPopUpCredDesc = false;
		fromWindowProdutoSimilar = false;
		pedido.setSugestaoPedidoGiroProduto(false);
		if (isInsereMultiplosSemNegociacao() && !isEditing() && !fromRelGiroProduto) {
			if (isInserindoItems()) {
				nuAcaoThreadMultInsercao = NU_ACAO_VOLTARCLICK;
			}
			if (!insereUltimoItemMultInsercao()) {
				return;
			}
		}
		if (LavenderePdaConfig.isUsaVendaRelacionada() && fromProdutoRelacionadoWindowOnFechamento) {
			fromProdutoRelacionadoWindowOnFechamento = false;
			super.voltarClick();
			return;
		}
		if (isInsereMultiplosSemNegociacao() || fromProdutoPendenteGiroMultInsercao) {
			fromProdutoPendenteGiroMultInsercao = false;
			if (LavenderePdaConfig.isUsaVlUnitGiroProduto()) {
				getItemPedido().giroProduto = null;
		}
		}
		//--
		if (editingExternalForm != null) {
			if (inCarrouselMode) {
				for (ItemPedido itemPedido : itemPedidoCarrouselList) {
					itemPedido.inCarrousel = false;
				}
			}
			close();
			if (editingExternalForm instanceof BaseCrudListForm) {
				BaseCrudListForm externalFormWindow = (BaseCrudListForm)editingExternalForm;
				externalFormWindow.list();
			}
		}
		if ((inVendaUnitariaMode && isEditing()) && !fromWindowSugestaoVendaComQtde && !fromProdutoRelacionadoWindow && !fromWindowMultiplaSugestaoProduto && !fromWindowMultiplaSugestaoNovoItemProduto && !fromWindowItemPedidoAbaixoPesoMinimo && !fromWindowItemPedidoAbaixoValorMinimo && !fromWindowDesconto && !fromItemPedidoInseridoPedido && !showDetalheItemJaInseridoMultInsercao) {
			if (LavenderePdaConfig.usaGradeProduto5()) {
				fromListItemPedidoForm = false;
			}
			super.voltarClick();
		} else if (!inVendaUnitariaMode) {
			super.voltarClick();
		} else if (fromRelGiroProduto && (!inVendaUnitariaMode || onFechamentoPedido)) {
			fromRelGiroProduto = false;
			onFechamentoPedido = false;
			super.voltarClick();
		} else if (LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedido() && fromRelProdutosPendentes) {
			fromRelProdutosPendentes = false;
			isOcultaProdutoPendenteMenuOpcoes = false;
			fromProdutoPendenteWindow = true;
			backToListAndClearDadosItemPedido();
			setFocusInFiltro();
		} else if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0 && fromWindowSugestaoItensRentIdealOnFechamento) {
			fromWindowSugestaoItensRentIdealOnFechamento = false;
			super.voltarClick();
		} else if (LavenderePdaConfig.isUsaSugestaoComboFechamentoPedido() && fromSugestaoItemComboFechamento && onFechamentoPedido) {
			fromSugestaoItemComboFechamento = false;
			onFechamentoPedido = false;
			super.voltarClick();
		} else if (fromListItemPedidoForm && fromSimilar) {
			fromSimilar = false;
			fromListItemPedidoForm = false;
			super.voltarClick();
		} else {
			if (!isFiltrandoAgrupadorGrade()) {
			atualizaProdutoMultInsercaoNaLista();
			}
			if (inVendaUnitariaMode && isEditing()) {
				add();
			}
			if (inVendaUnitariaMode && LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && LavenderePdaConfig.usaInsercaoItensDiferentesLeituraCodigoBarras
					&& usaCameraParaLeituraCdBarras()) {
				UiUtil.add(barBottomContainer, btLeitorCamera, 2);
			} else if (inVendaUnitariaMode && LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && usaCameraParaLeituraCdBarras()) {
				UiUtil.add(barBottomContainer, btLeitorCamera, 5);
			}
			fromWindowDesconto = false;
			backToListAndClearDadosItemPedido();
			setFocusInFiltro();
			if (!showPopupSugestaoVendaOnVoltarClick()) {
				showPopupProdutoRelacionado();
			}
			if (LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido() && fromWindowMultiplaSugestaoNovoItemProduto) {
				showPopupMultiplaSugestaoNovoItemOnVoltarClick();
			} else if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido) && fromWindowMultiplaSugestaoProduto) {
				showPopupMultiplaSugestaoOnVoltarClick();
			}
			if (fromWindowItemPedidoAbaixoPesoMinimo) {
				showPopupItemPedidoAbaixoPesoMinimoOnVoltarClick();
			}
			if (fromWindowItemPedidoAbaixoValorMinimo) {
				showPopupItemPedidoAbaixoValorMinimoOnVoltarClick();
			}
			if (showListDescontoCredito) {
				showListDescontoCredito = false;
				ProdutoCreditoDesc domainFilter = new ProdutoCreditoDesc();
				domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				domainFilter.flTipoCadastroProduto = ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO;
				if (ValueUtil.isNotEmpty(ProdutoCreditoDescService.getInstance().findAllByItemPedidoList(pedido, domainFilter))) {
					btDescontosClick();
				}
			}
			if (fromRelGiroProduto) {
				fromRelGiroProduto = false;
				fromBtNovoItemOrFechamentoPed = true;
				btGiroProdutoClick(fromBtNovoItemOrFechamentoPed);
			}
			fromListItemPedidoForm = false;
			fromItemPedidoInseridoPedido = false;
			showDetalheItemJaInseridoMultInsercao = false;
			if (LavenderePdaConfig.isUsaSugestaoComboAposInsercao() && !isEditing() && !onFechamentoPedido && !cadPedidoForm.mostraSugestaoItemComboOnExibition && cadPedidoForm.salvouItemComboSugerido) {
				cadPedidoForm.salvouItemComboSugerido = false;
				sugereItemCombo();
			}
			getItemPedido().selecionadoTabelaComMaiorDesconto = false;
		}
		if (fromRelNotificaoItemWindow) {
			showPopUpRelNotificacaoWindow();
		}
		if (pedido.isAdiconandoItemRelProdutosPendentes && LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedidoAoFecharPedido()) {
			super.voltarClick();
		}
		if (fromProdutoPendenteWindow && !fromRelProdutosPendentesCapaPedido) {
			showPopupRelProdutoPendente(false);
			fromProdutoPendenteWindow = false;
		}
		showPopUpRelDiferencasDescProgressivo(pedido);
	}
			
	private boolean insereUltimoItemMultInsercao() throws SQLException {
		if (isInserindoItems()) {
			popupInserindoItens.popupNonBlocking();
			return false;
		}

		if (conferirInconsistencias) {
			reorganizarLista();
		} else if (itemContainerErroMap != null) {
			itemContainerErroMap.clear();
	}
		isAddingFromArray = false;
		houveAlteracaoItemPorMultInsercao = false;
		return true;
	}

	private void showPopUpRelNotificacaoWindow() throws SQLException {
		if (!pedido.erroItensFechamentoPedido.isEmpty()) {
			RelNotificacaoItemWindow relNotificacaoItemWindow = new RelNotificacaoItemWindow(Messages.TITULO_REL_ERROS_PEDIDO_NAO_FECHADO, pedido.erroItensFechamentoPedido, false);
			relNotificacaoItemWindow.setCadItemPedidoForm(this);
			this.fromRelNotificaoItemWindow = false;
			relNotificacaoItemWindow.popup();
		}
	}

	private void controlaEdicaoKitFechado() {
		if (LavenderePdaConfig.isUsaKitProdutoFechado() && (pedido.isPedidoAberto() || pedido.isPedidoReaberto) && !isEnabled()) {
			setEnabled(true);
		}
	}

	private void atualizaProdutoMultInsercaoNaLista() throws SQLException {
		if (fromListDetalheInsecaoMult || refreshCurrentItemMultiplaInsercaoList) {
			ItemPedido itemPedido = (ItemPedido)ItemPedidoService.getInstance().findByPrimaryKey(getItemPedido());
			if (itemPedido == null) {
				itemPedido = getItemPedido();
				itemPedido.setQtItemFisico(0);
				itemPedido.vlDesconto = 0;
				itemPedido.removeFromGrid = true;
				if (itemPedido.getProduto() == null) return;
				if (itemPedido.vlPctDesconto > 0) {
					itemPedido.vlPctDesconto = 0d;
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
				}
				PedidoService.getInstance().calculateItemPedido(itemPedido.pedido, itemPedido, false);
			} else {
				itemPedido.pedido = pedido;
			}
			ProdutoBase produto = itemPedido.getProduto();
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
				ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(produto.cdProduto);
				produtoTabPreco.itemTabelaPreco = produto.itemTabelaPreco;
				produtoTabPreco.estoque = itemPedido.estoque;
				atualizaProdutoNaGrid(produtoTabPreco, isInsereMultiplosSemNegociacao() ? itemPedido : null, true, false);
			} else {
				produto.estoque = itemPedido.estoque;
				atualizaProdutoNaGrid(produto, isInsereMultiplosSemNegociacao() ? itemPedido : null, true, false);
			}
			refreshCurrentItemMultiplaInsercaoList = false;
		}
	}
	
	@Override
	protected int getPreviousItemIndex(int currentIndex) {
		if (!inCarrouselMode && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			GridListContainer listContainer = getBaseCrudListForm().getListContainer();
			return findFormerSimilarIndex(currentIndex, listContainer);
		}

		return super.getPreviousItemIndex(currentIndex);
			}

	private int findFormerSimilarIndex(int currentIndex, GridListContainer listContainer) {
		Item container;
		ItemPedido itemPedido;
		for (int i = (currentIndex - 1); i >= 0; i--) {
			container = (Item)listContainer.getContainer(i);
			itemPedido = (ItemPedido)container.domain;
			if (!itemPedido.isItemSimilar) {
				return i;
			}
		}
			return -1;
		}

	@Override
	protected int getNextItemIndex(int currentIndex) {
		if (!inCarrouselMode && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			GridListContainer listContainer = getBaseCrudListForm().getListContainer();
			return findNextSimilarIndex(currentIndex, listContainer);
		}

		return super.getNextItemIndex(currentIndex);
			}

	private int findNextSimilarIndex(int currentIndex, GridListContainer listContainer) {
		Item container;
		ItemPedido itemPedido;
		for (int i = (currentIndex + 1); i < listContainer.size(); i++) {
			container = (Item)listContainer.getContainer(i);
			itemPedido = (ItemPedido)container.domain;
			if (!itemPedido.isItemSimilar) {
				return i;
			}
		}
			return -1;
		}

	@Override
	protected void btPreviousItemClick() throws SQLException {
		controlaEdicaoKitFechado();
		super.btPreviousItemClick();
		updateNuFracaoFornecedor();
		recarregaGridHistDevBtAnteriorProximoClick();
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) onFormShow();
	}
	
	@Override
	protected void btNextItemClick() throws SQLException {
		controlaEdicaoKitFechado();
		super.btNextItemClick();
		updateNuFracaoFornecedor();
		recarregaGridHistDevBtAnteriorProximoClick();
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) onFormShow();
	}
	@Override
	protected void loadLbItemByItem() throws SQLException {
		if (!inCarrouselMode && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			ListItemPedidoForm listItemPedidoForm = (ListItemPedidoForm) getBaseCrudListForm();
			if (listItemPedidoForm != null && listItemPedidoForm.getListContainer() != null) {
				Vector list = listItemPedidoForm.getDomainListSelection();
				lbItemByItem.setValue(list.indexOf(getDomain()) + 1 + "/" + list.size());
			}
		} else {
			super.loadLbItemByItem();
		}
	}
	
	@Override
	protected void controlaVisibilidadeBotoesPreviousNext(GridListContainer gridListContainer) throws SQLException {
		if (!inCarrouselMode && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			ListItemPedidoForm listItemPedidoForm = (ListItemPedidoForm) getBaseCrudListForm();
			if (listItemPedidoForm != null && listItemPedidoForm.getListContainer() != null) {
				Vector list = listItemPedidoForm.getDomainListSelection();
				int i = list.indexOf(getDomain());
				btPreviousItem.setEnabled(i != 0);
				btNextItem.setEnabled(i + 1 != list.size());
			}
		} else {
			super.controlaVisibilidadeBotoesPreviousNext(gridListContainer);
		}
	}

	public void recarregaGridHistDevBtAnteriorProximoClick() throws SQLException {
		if (!LavenderePdaConfig.isExibeHistoricoProduto()) return;
		addFotoProduto();
		addGridHistTrocaDevProduto();
	}

	public void showPopupProdutoRelacionado() throws SQLException {
		if ("2".equals(LavenderePdaConfig.usaVendaRelacionada)) {
			ProdutoRelacionadoService.getInstance().loadProdutosRelacionadosNaoContemplados(getItemPedido().pedido);
			if (ValueUtil.isNotEmpty(getItemPedido().pedido.prodRelacionadosNaoContempladosList)) {
				String descricao = "";
				if (LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado) {
					descricao = Messages.PRODUTO_RELACIONADO_INSERCAO_ITENS_AUTO;
				} else {
					descricao = Messages.PRODUTO_RELACIONADO_INSERCAO_ITENS;
				}
				ListProdutoRelacionadoWindow listProdutoRelacionadoWindow = new ListProdutoRelacionadoWindow(descricao, getItemPedido().pedido, true);
				listProdutoRelacionadoWindow.cadPedidoForm = cadPedidoForm;
				listProdutoRelacionadoWindow.popup();
			} else {
				cadPedidoForm.inItemNegotiationProdutosRelacionados = false;
		    	fromProdutoRelacionadoWindow = false;
		    	fromProdutoRelacionadoWindowOnFechamento = false;
			}
		}
	}

	private boolean showPopupSugestaoVendaOnVoltarClick() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro()) {
				if (fromWindowSugestaoProduto && !listProdutoSugestaoSemQtdeWindow.onAcessoManual) {
					fromWindowSugestaoProduto = false;
					listProdutoSugestaoSemQtdeWindow = getListProdutoSugestaoSemQtdeWindow(listProdutoSugestaoSemQtdeWindow.onFechamentoPedido, true, listProdutoSugestaoSemQtdeWindow.onAcessoManual);
					if (listProdutoSugestaoSemQtdeWindow.onFechamentoPedido || listProdutoSugestaoSemQtdeWindow.hasSugestaoVenda()) {
						listProdutoSugestaoSemQtdeWindow.popup();
						return true;
					} else if (!listProdutoSugestaoSemQtdeWindow.onAcessoManual && !listProdutoSugestaoSemQtdeWindow.hasSugestaoVenda()) {
						listProdutoSugestaoComQtdeWindow = getListProdutoSugestaoComQtdeWindow(false, false);
						if (listProdutoSugestaoComQtdeWindow.hasSugestaoVenda()) {
							listProdutoSugestaoComQtdeWindow.popup();
							return true;
						}
					}
				} else if (fromWindowSugestaoVendaComQtde && !listProdutoSugestaoComQtdeWindow.onAcessoManual) {
					fromWindowSugestaoVendaComQtde = false;
					listProdutoSugestaoComQtdeWindow = getListProdutoSugestaoComQtdeWindow(listProdutoSugestaoComQtdeWindow.onFechamentoPedido, true, listProdutoSugestaoComQtdeWindow.onAcessoManual);
					if (listProdutoSugestaoComQtdeWindow.onFechamentoPedido || listProdutoSugestaoComQtdeWindow.hasSugestaoVenda()) {
						listProdutoSugestaoComQtdeWindow.popup();
						return true;
					}
				}
			} else if (LavenderePdaConfig.usaSugestaoVendaBaseadaDifPedidos > 0 && fromWindowSugestaoProduto) {
				fromWindowSugestaoProduto = false;
				listProdutoSugestaoDifPedidoWindow = getListProdutoSugestaoDifPedidoWindow(listProdutoSugestaoDifPedidoWindow.onFechamentoPedido, true, listProdutoSugestaoDifPedidoWindow.onAcessoManual);
				if (!listProdutoSugestaoDifPedidoWindow.onAcessoManual && (listProdutoSugestaoDifPedidoWindow.onFechamentoPedido || listProdutoSugestaoDifPedidoWindow.hasSugestaoVendaProd())) {
					listProdutoSugestaoDifPedidoWindow.popup();
					return true;
				}
			}
		}
		return false;
	}

	private void showPopupMultiplaSugestaoOnVoltarClick() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			fromWindowMultiplaSugestaoProduto = false;
			listMultiplasSugestoesProdutosWindow = getListMultiplasSugestoesProdutosWindow(true);
			if (listMultiplasSugestoesProdutosWindow.hasSugestoes()) {
				listMultiplasSugestoesProdutosWindow.popup();
				afterShowMultSugesProdutosWindow(listMultiplasSugestoesProdutosWindow);
			}
		}
	}
	
	private void showPopupMultiplaSugestaoNovoItemOnVoltarClick() throws SQLException {
		if (pedido.isPedidoVenda()) {
			fromWindowMultiplaSugestaoNovoItemProduto = false;
			listMultiplasSugestoesProdutosNovoItemWindow = getListMultiplasSugestoesProdutosNovoItemWindow(true);
			if (!listMultiplasSugestoesProdutosNovoItemWindow.onFechamentoPedido || listMultiplasSugestoesProdutosNovoItemWindow.hasSugestoes()) {
				listMultiplasSugestoesProdutosNovoItemWindow.popup();
				afterShowMultSugesProdutosWindow(listMultiplasSugestoesProdutosNovoItemWindow);
			}
		}
	}
	
	private void showPopupItemPedidoAbaixoPesoMinimoOnVoltarClick() throws SQLException {
		fromWindowItemPedidoAbaixoPesoMinimo = false;
		listItemPedidoAbaixoPesoMinimoWindow = getListItemPedidoAbaixoPesoMinimoWindow(true, true);
		if (listItemPedidoAbaixoPesoMinimoWindow.hasItemPedidoAbaixoPeso()) {
			listItemPedidoAbaixoPesoMinimoWindow.popup();
		} else {
			voltarClick();
		}
	}

	private void showPopupItemPedidoAbaixoValorMinimoOnVoltarClick() throws SQLException {
		fromWindowItemPedidoAbaixoValorMinimo = false;
		listItemPedidoAbaixoValorMinimoWindow = getListItemPedidoAbaixoValorMinimoWindow(true, true);
		if (listItemPedidoAbaixoValorMinimoWindow.hasItemPedidoAbaixoPeso()) {
			listItemPedidoAbaixoValorMinimoWindow.popup();
		} else {
			voltarClick();
		}
	}

	protected void backToListAndClearDadosItemPedido() throws SQLException {
		reloadTabPrecoOnBackToList();
		repaintScreen();
		clearProdutoVendaAtual();
		backToListMenuCategoria();
		cleanInfoComplementares();
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			addItensOnButtonMenu();
		}
		if (hideAttributesOld != hideAttributes) {
			mudaVisibilidadeAttrMultInserOuAgrupadorGrade();
			listContainer.hideDiscontField = hideAttributes;
			listContainer.refreshBtShowHideImage();
	}
	}
	
	public void cleanInfoComplementares() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.dtEntrega = null;
		itemPedido.vlAltura = 0;
		itemPedido.vlLargura = 0;
		itemPedido.vlComprimento = 0;
		itemPedido.vlPosVinco1 = 0;
		itemPedido.vlPosVinco2 = 0;
		itemPedido.vlPosVinco3 = 0;
		itemPedido.vlPosVinco4 = 0;
		itemPedido.vlPosVinco5 = 0;
		itemPedido.vlPosVinco6 = 0;
		itemPedido.vlPosVinco7 = 0;
		itemPedido.vlPosVinco8 = 0;
		itemPedido.vlPosVinco9 = 0;
		itemPedido.vlPosVinco10 = 0;
	}
	
	protected void btFiltrarClick() throws SQLException {
		verificaProdutoGenerico = true;
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
			if (!LavenderePdaConfig.filtraSomenteCesta()) {
			cbCesta.setSelectedIndex(0);
		}
			cbCestaClick(true);
		}
		//--
		filtrarClick();
	}

	protected boolean filtrarClick() throws SQLException {
		boolean filtrarClick = filtrarClick(null, null);
		sugereCadastroProdutoForaCatalogo();
		return filtrarClick;
	}

	protected boolean filtrarClick(final Object target, String targetId) throws SQLException {
	    if (deveFiltrarAutomaticamente(targetId)) {
	        return false;
	    }
	    if (filtrandoItemEventoEmProgresso) {
	    	edFiltro.setValue(filtroItemEventoEmProgresso);
	    }
	    
		dsFiltro = edFiltro.getText();
	    filtrarProduto(target);

	    if (deveSelecionarProdutoAutomaticamente()) {
	    	selecionarUnicoProdutoNaLista(buscaProdutoUnidadePorCodigoBarras());
	    }
	    if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
	    	edFiltro.setValue("");
	    }
	    if (deveIncrementarQuantidadePorLeituraCodigoBarras()) {
	        incrementarQuantidadePorLeituraCodigoBarras();
	    }

	    return listContainer.size() == 1;
	}

	private boolean deveFiltrarAutomaticamente(String targetId) {
	    return targetId != null && filtrosNaoAutomaticosMap.get(targetId) != null;
	}

	private boolean deveSelecionarProdutoAutomaticamente() {
	    return LavenderePdaConfig.isSelecionaProdutoAutomaticamente() && listContainer.size() == 1;
	}
	
	private ProdutoUnidade buscaProdutoUnidadePorCodigoBarras() throws SQLException {
		ProdutoUnidade produtoUnidade = null;
		
		if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(dsFiltro)) {
			produtoUnidade = ProdutoUnidadeService.getInstance().getProdutoUnidadeByCdBarras(getItemPedido(), dsFiltro);
			
			if (produtoUnidade != null) {
	            edFiltro.setText(produtoUnidade.cdProduto);
			}
		}
		
		return produtoUnidade;
	}

	private void selecionarUnicoProdutoNaLista(ProdutoUnidade produtoUnidade) throws SQLException {
			listContainer.setSelectedIndex(0);
			produtoSelecionado = null;

	    if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && autoClickMenuCategoria()) {
				itemSelecionado = true;
				gridClickAndRepaintScreen();
	        if (produtoUnidade != null && getItemPedido().getProduto() != null) {
	        	aplicarUnidadeAlternativa(produtoUnidade);
			}
		}
		}
	
	private void aplicarUnidadeAlternativa(ProdutoUnidade produtoUnidade) throws SQLException {
        String cdUnidadeAlternativa = produtoUnidade.cdUnidade;
        cbUnidadeAlternativa.setValue(cdUnidadeAlternativa);
        getItemPedido().cdUnidade = cdUnidadeAlternativa;
        changeUnidadeAlternativa(cdUnidadeAlternativa);
        selectUnidadeNaGrid(cdUnidadeAlternativa);
		}

	private boolean deveIncrementarQuantidadePorLeituraCodigoBarras() {
	    return LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && !pedido.isGondola();
		}

	private void incrementarQuantidadePorLeituraCodigoBarras() throws SQLException {
	    edQtItemFisico.setValue(1);
	    getItemPedido().setQtItemFisico(1);
	}

	private void filtrarProduto(final Object target) throws SQLException {
		if (isInserindoItems()) {
			nuAcaoThreadMultInsercao = NU_ACAO_FILTRAR_PRODUTO;
			filtrandoItemEventoEmProgresso = true;
			filtroItemEventoEmProgresso = edFiltro.getText();
		}
		targetFiltroThread = target;
		dsFiltro = edFiltro.getText();
		if (isInsereMultiplosSemNegociacao()) {
			if (!insereUltimoItemMultInsercao()) return;
			}
		sugereCadastroProdutoDesejado = false;
		if (validateFiltro()) {
			if (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == ValorParametro.PARAM_INT_VALOR_ZERO && !isAlgumFiltroEspecialSelecionado() && isOrigemFiltrosEspeciais(target)) {
				clearListContainerAndTotalizers();
				flListInicialized = false;
			} else {
				flListInicialized = true;
				carregaGridProdutos();
				clearDomainAndScreen();
			}
		} else {
			if (!isOrigemFiltrosEspeciais(target)) {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
			}
			clearListContainerAndTotalizers();
		}
	}
	
	private boolean isOrigemFiltrosEspeciais(final Object target) {
		return target == cbFornecedor || target == cbTabelaPreco || target == cbGrupoProduto1 || target == cbKit || target == cbAtributoProd || target == cbAtributoOpcaoProd || target == ckApenasKit || target == ckProdutoPromocional || target == ckProdutoDescPromocional || target == cbProdutoGrupoDestaque || (LavenderePdaConfig.usaDescQuantidadePorPacote && target == cbPacote) || target == ckProdutoOportunidade || target == ckApenasProdutoVendido || (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && target == cbDescPromocional) /*|| target == cbTipoRanking*/ || target == ckMaxDescProdCli || target == ckProdutoDescQtd || target == cbMarcador;
	}

	private void sugereCadastroProdutoForaCatalogo() throws SQLException {
		if (sugereCadastroProdutoDesejado && UiUtil.showConfirmYesNoMessage(Messages.PRODUTODESEJADO_MSG_SUGESTAO)) {
			btProdutoDesejadoClick(true);
		}
	}

	private boolean validateFiltro() {
		lvEstoque.setForeColor(ColorUtil.componentsForeColor);
		tipEstoque.setVisible(false);
		String filtro = dsFiltro;
		if (LavenderePdaConfig.usaPesquisaInicioString && dsFiltro.startsWith("*")) {
			filtro = dsFiltro.substring(1);
		}
    	if ((filtro == null) || ((filtro.length() < LavenderePdaConfig.qtMinimaCaracteresFiltroProduto) && !isAlgumFiltroEspecialSelecionado())) {
			return false;
		}
		return true;
	}

    private boolean isAlgumFiltroEspecialSelecionado() {
		return ckApenasKit.isChecked() ||
				ckPreAltaProduto.isChecked() ||
				!cbFornecedor.isValueSelectedEmpty() ||
				!cbAtributoOpcaoProd.isValueSelectedEmpty() ||
				!cbGrupoProduto1.isValueSelectedEmpty() ||
				(!cbGrupoProduto2.isValueSelectedEmpty() && LavenderePdaConfig.ocultaGrupoProduto1) ||
				!cbKit.isValueSelectedEmpty() ||
				ckProdutoPromocional.isChecked() ||
				ckProdutoDescPromocional.isChecked() ||
				ckProdutoDescQtd.isChecked() ||
				!cbProdutoGrupoDestaque.isValueSelectedEmpty() ||
				(LavenderePdaConfig.usaDescQuantidadePorPacote && !cbPacote.isValueSelectedEmpty()) ||
				ckProdutoOportunidade.isChecked() ||
				ckApenasProdutoVendido.isChecked() ||
				ckMaxDescProdCli.isChecked() ||
				(LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && !cbDescPromocional.isValueSelectedEmpty()) ||
				!cbEstoqueDisponivel.isValueSelectedEmpty() ||
				ValueUtil.isNotEmpty(cbMarcador.getSelected()) ||
				ckApenasItensAdicionados.isChecked();
    }

    protected String getSelectedRowKey() {
		return listContainer.getSelectedId();
    }

    protected boolean isComboTabPrecoVisible() throws SQLException {
    	return (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() || LavenderePdaConfig.usaListaColunaPorTabelaPreco || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido;
    }

	private void cbTabelaPrecoChangeManual() throws SQLException {
		if (getItemPedido().usaCestaPromo) {
			return;
		}
		//--
		showMessageTrocaTabPreco(true);
		//
		double vlPctDescontoSAVE = edVlPctDesconto.getValueDouble();
		double vlPctAcrescimoSAVE = edVlPctAcrescimo.getValueDouble();
		double qtItemFisicoSAVE = edQtItemFisico.getValueDouble();
		double qtItemFaturamentoSAVE = edQtItemFaturamento.getValueDouble();
		String dsObservacaoSAVE = getItemPedido().dsObservacao;
		//--
		String rowKeyProduto = getSelectedRowKey();
		tabelaPrecoChange();
		if (!ValueUtil.isEmpty(rowKeyProduto)) {
			int size = listContainer.size();
			for (int i = 0; i < size; i++) {
				if (listContainer.getId(i).equals(rowKeyProduto)) {
					listContainer.setSelectedIndex(i);
					if (inVendaUnitariaMode) {
						ignoraTrocaTabsAuto = true;
						try {
							gridClick();
						} finally {
							ignoraTrocaTabsAuto = false;
						}
					}
					break;
				}
			}
			if (listContainer.getSelectedIndex() == -1) {
				clearProdutoVendaAtual();
			}
		}
		//--
		if (!selectDesconto && !(LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && getItemPedido().getTabelaPreco() != null && getItemPedido().getTabelaPreco().isAplicaDescQtdeAuto())) {
			if (!LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco && !LavenderePdaConfig.isDesconsideraAcrescimoDescontoTrocaTabPreco()) {
				if(!ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL)) {
					vlPctDescontoSAVE = LavenderePdaConfig.selecionaTabPrecoMaiorDescPromo() && !isEditing() ? getItemPedido().vlPctDesconto : vlPctDescontoSAVE;
				getItemPedido().vlPctDesconto = vlPctDescontoSAVE;
				if (getItemPedido().vlPctDesconto > 0) {
					edVlPctDescontoValueChange(getItemPedido());
				}
				edVlPctDesconto.setValue(vlPctDescontoSAVE);
				getItemPedido().vlPctAcrescimo = vlPctAcrescimoSAVE;
				if (getItemPedido().vlPctAcrescimo > 0) {
					edVlPctAcrescimoValueChange(getItemPedido());
				}
				edVlPctAcrescimo.setValue(vlPctAcrescimoSAVE);
			}
			}
			//--
			getItemPedido().setQtItemFisico(qtItemFisicoSAVE);
			getItemPedido().qtItemFaturamento = qtItemFaturamentoSAVE;
			getItemPedido().dsObservacao = dsObservacaoSAVE;
			updateQtItens(getItemPedido());
			if (qtItemFisicoSAVE > 0 || qtItemFaturamentoSAVE > 0) {
				if (qtItemFisicoSAVE > 0) {
					edQtItemFisicoValueChange(getItemPedido());
				} else if (qtItemFaturamentoSAVE > 0) {
					edQtItemFaturamentoValueChange();
				}
				houveAlteracaoQtdItem = true;
				calcularClick();
			}
		}
		selectDesconto = selectDescontoPacote = false;
		//--
		edBaseNumpad = new EditText("", 0);
		//--
		setaCorLoteProduto(getItemPedido().produtoLoteList);
		refreshComponents();
	}

	protected void tabelaPrecoChange() throws SQLException {
		SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
		cbTabelaPreco.setListTabelaPrecoListaColuna(cbTabelaPreco.getValue());
		//Obrigatório invalidar os descontos quantidade na troca da tabela de preço
		descontoQuantidadeList = null;
		descontoQtdeGrupoProdutoList = null;
		ItemPedido itemPedido = getItemPedido();
		itemPedido.descQuantidade = null;
		itemPedido.cdTabelaPrecoOld = itemPedido.cdTabelaPreco;
		//--
		if (LavenderePdaConfig.isUsaKitProduto()) {
			cbKit.loadKit1e2(pedido);
			cbKit.setSelectedIndex(0);
			if (!LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && !isEditing() && !inVendaUnitariaMode) {
				filtrarProduto(cbKit);
			}
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox()) {
			cbDescPromocional.loadDescPromocional(pedido, cbTabelaPreco.getValue());
			cbDescPromocional.setSelectedIndex(0);
			if (!LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && !isEditing() && !inVendaUnitariaMode) {
				filtrarProduto(cbDescPromocional);
			}
		}
		//--
		if ((ProdutoService.getInstance().isParametrizadoJoinComItemTabelaPreco() || LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) && !isEditing() && !inVendaUnitariaMode) {
			//mantem o produto para reseleciona-lo na grid depois, pois no filtrarClick() ele é apagado!
			Produto prodTmp = getItemPedido().getProduto();
			filtrarProduto(cbTabelaPreco);
			getItemPedido().setProduto(prodTmp);
			getItemPedido().cdTabelaPreco = cbTabelaPreco.getValue();
		} else {
			if (itemPedido.getProduto() != null) {
				getItemPedidoService().clearDadosItemPedido(itemPedido);
				if (LavenderePdaConfig.isConfigGradeProduto()) {
					loadDadosGradeProduto(itemPedido);
				}
				changeItemTabelaPreco();
			}
		}
		ultimaTabelaPrecoSelecionada = cbTabelaPreco.getValue();
		if ((LavenderePdaConfig.mantemTabPrecoSelecionadaListaProduto || LavenderePdaConfig.usaSelecaoPorGrid()) && (!inVendaUnitariaMode)) {
			ultimaTabelaPrecoSelecionadaListaItens = cbTabelaPreco.getValue();
		}
		repaintNow();
	}

	private void reloadAtributoProd() {
		if (LavenderePdaConfig.usaFiltroAtributoProduto) {
			cbAtributoProd.setSelectedIndex(0);
			cbAtributoOpcaoProd.setSelectedIndex(0);
			changeAtributoProdEnable();
		}
	}

	protected void changeItemTabelaPreco() throws SQLException {
		changeItemTabelaPreco(null);
	}

	protected void changeItemTabelaPreco(String cdTabelaPreco) throws SQLException {
		changeItemTabelaPreco(cdTabelaPreco, true);
	}
	
	protected void changeItemTabelaPreco(String cdTabelaPreco, boolean refreshDomainToScreen) throws SQLException {
		changeItemTabelaPreco(getItemPedido(), cdTabelaPreco, refreshDomainToScreen, null);
	}
	
	@Override
	protected void changeItemTabelaPreco(String cdTabelaPreco, boolean refreshDomainToScreen, ItemTabelaPreco itemTabelaPreco) throws SQLException {
		changeItemTabelaPreco(getItemPedido(), cdTabelaPreco, refreshDomainToScreen, null);
	}

	protected void changeItemTabelaPreco(ItemPedido itemPedido, String cdTabelaPreco, boolean refreshDomainToScreen, ItemTabelaPreco itemTabelaPreco) throws SQLException {
		itemPedido.cdTabelaPreco = ValueUtil.isNotEmpty(cdTabelaPreco) ? cdTabelaPreco : cbTabelaPreco.getValue();
		Produto itemProduto = itemPedido.getProduto();
		if (itemProduto != null) {
			if (itemTabelaPreco == null) {
				itemTabelaPreco = itemPedido.getItemTabelaPreco();
			}
			itemPedido.setItemTabelaPreco(itemTabelaPreco);
			itemPedido.getProduto().itemTabelaPreco = itemTabelaPreco;
			if (itemTabelaPreco != null) {
				ItemPedidoService.getInstance().setCdUnidadeItemPedido(itemPedido, pedido, itemProduto, LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() ? cbUnidade.getValue() : cbUnidadeAlternativa.getValue());
				itemPedido.setItemTabelaPreco(itemTabelaPreco);
				itemTabelaPreco.descontoQuantidadeList = getDescontoQuantidadeList(itemPedido);
				descontoQuantidadeList = itemTabelaPreco.descontoQuantidadeList;
				if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
					itemTabelaPreco.vlManualBrutoItem = itemPedido.vlManualBrutoItem;
				}
				if (fromProdutoPendenteGiroMultInsercao) {
					itemTabelaPreco.vlManualBrutoItem = -1;
				}
				getPedidoService().resetDadosItemPedido(pedido, itemPedido);
			}
			if (refreshDomainToScreen) {
				refreshDomainToScreen(itemPedido);
			}
		}
		enabledDesconto(itemPedido);
		validaExibicaoVlItemPedido();
	}

	private TabelaPreco getTabelaPrecoSelecionada() throws SQLException {
		String cdTabelaPrecoSelecionada = cbTabelaPreco.getValue();
		if (!ValueUtil.isEmpty(cdTabelaPrecoSelecionada) && ((tabelaPrecoSelecionada == null) || (!cdTabelaPrecoSelecionada.equals(tabelaPrecoSelecionada.cdTabelaPreco)))) {
			if ((pedido.getTabelaPreco() == null) || (!cdTabelaPrecoSelecionada.equals(pedido.getTabelaPreco().cdTabelaPreco))) {
				tabelaPrecoSelecionada = TabelaPrecoService.getInstance().getTabelaPreco(cdTabelaPrecoSelecionada);
			} else {
				tabelaPrecoSelecionada = pedido.getTabelaPreco();
			}
		}
		return tabelaPrecoSelecionada;
	}

	private void enabledDesconto(ItemPedido itemPedido) throws SQLException {
		if (!pedido.isPedidoAberto() && !pedido.isPedidoReaberto) {
			edVlPctDesconto.setEditable(false);
			edVlItemPedido.setEditable(false);
		} else {
		TabelaPreco tabelaPreco = getTabelaPrecoSelecionada();
		tabelaPreco = tabelaPreco == null ? pedido.getTabelaPreco() : tabelaPreco;
		if (LavenderePdaConfig.isAplicaDescontoCategoria() && tabelaPreco != null && !tabelaPreco.isPermiteDesconto()) {
			edVlPctDesconto.setEditable(false);
			edVlItemPedido.setEditable(false);
		} else if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			tabelaPreco = getTabelaPrecoSelecionada();
			if (tabelaPreco != null) {
					if (tabelaPreco.isAplicaDescQtdeAuto() && itemPedido.getProduto() != null && DescQuantidadeService.getInstance().hasDescontoQuantidade(itemPedido) && !LavenderePdaConfig.liberaDescAcrescManualAuto) {
					edVlPctDesconto.setEditable(false);
					edVlPctAcrescimo.setEditable(false);
					edVlItemPedido.setEditable(false);
				} else {
						edVlPctDesconto.setEditable(isEdVlPctDescontoEditable());
					edVlPctAcrescimo.setEditable(isEnabled());
						edVlItemPedido.setEditable(isEnabled() && LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() && (ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList) || ValueUtil.isEmpty(pedido.getCliente().dsAcrescimoBloqueadoList)));
				}
			}
		}
			if (tabelaPreco != null && tabelaPreco.isBloqueiaCampoNegociacao()) {
			edVlPctDesconto.setEditable(false);
				edVlPctAcrescimo.setEditable(false);
			edVlItemPedido.setEditable(false);
			}
			boolean naoPermiteEditarDescAcresEVlItem = !LavenderePdaConfig.tipoPedidoOcultoNoPedido;
			naoPermiteEditarDescAcresEVlItem &= LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido() && pedido.getTipoPedido().vlPctMaxDesconto == 0 || LavenderePdaConfig.isUsaAcrescimoMaximoPorTipoPedido() && pedido.getTipoPedido().vlPctMaxAcrescimo == 0;
			naoPermiteEditarDescAcresEVlItem |= LavenderePdaConfig.usaConfigBonificacaoItemPedido() && getItemPedido() != null && getItemPedido().isItemBonificacao();
			if (naoPermiteEditarDescAcresEVlItem) {
				edVlPctDesconto.setEditable(false);
				edVlItemPedido.setEditable(false);
			edVlPctAcrescimo.setEditable(false);
		}
			if (itemPedido == null) return;

			if (itemPedido.hasDescProgressivo()) {
				edVlPctDesconto.setEditable(false);
				edVlItemPedido.setEditable(false);
				edVlPctAcrescimo.setEditable(false);
				edVlVerbaManual.setEditable(false);
				edVlUnidadeEmbalagem.setEditable(false);
	}
			if ((LavenderePdaConfig.isExibeComboMenuInferior() && itemPedido.isCombo()) || itemPedido.isKitTipo3()) {
				edVlPctAcrescimo.setEditable(false);
				edVlPctDesconto.setEditable(false);
				edVlItemPedido.setEditable(false);
				edQtItemFisico.setEditable(false);
			}
			if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && !LavenderePdaConfig.permiteNegociacaoItemPromocional()
					&& (itemPedido.isFazPartePromocao() || itemPedido.isItemTabelaPrecoPromocao())) {
				edVlPctAcrescimo.setEditable(false);
				edVlPctDesconto.setEditable(false);
				edVlItemPedido.setEditable(false);
			} else if (!itemPedido.isCombo() && !itemPedido.isKitTipo3() && ((LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && LavenderePdaConfig.permiteNegociacaoItemPromocional())
					|| (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && !itemPedido.isFazPartePromocao() && !itemPedido.isItemTabelaPrecoPromocao()))) {
				edVlPctDesconto.setEditable(true);
			}
		}
	}


	public Vector getDescontoQtdeGrupoProdutoList() throws SQLException {
		if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && (descontoQtdeGrupoProdutoList == null)) {
			descontoQtdeGrupoProdutoList = DescontoGrupoService.getInstance().findAllByGrupoProduto(getItemPedido());
		}
		return descontoQtdeGrupoProdutoList;
	}

	public void cbCestaClick() throws SQLException {
		cbCestaClick(false);
		}

	public void cbCestaClick(boolean fromFiltraClick) throws SQLException {
		if (!fromFiltraClick && LavenderePdaConfig.filtraSomenteCesta()) {
		setCbNaoPositivadoEnable();
		} else if (!LavenderePdaConfig.filtraSomenteCesta()) {
			setCbNaoPositivadoEnable();
	}
	}

	private void setCbNaoPositivadoEnable() {
		if (ValueUtil.isEmpty(cbCesta.getValue())) {
			cbNaoPositivado.setEnabled(false);
			cbNaoPositivado.setValue(Messages.OPCAO_TODOS);
		} else {
			cbNaoPositivado.setValue(Messages.NAO_POSITIVADOS);
			cbNaoPositivado.setEnabled(true);
		}
	}

	@Override
	protected void listContainerProdutoClick() throws SQLException {
		super.listContainerProdutoClick();
		gridClickAndRepaintScreen();
	}

	@Override
	public void gridClickAndRepaintScreen() throws SQLException {
		carregaDadosModoEdicaoOuGrade5();
		gridClickAndRepaintScreen(true);
		doEditOrRefreshItemPedido();
		openGridEditModeIfNecessary(fromWindowProdutoSimilar);
	}

	public void doEditOrRefreshItemPedido() throws SQLException {
		if (fromItemPedidoInseridoPedido) {
			if (!LavenderePdaConfig.usaGradeProduto5()) {
				getItemPedidoService().calculate(getItemPedido(), pedido);
			}
			edit(getItemPedido());
		} else if (isModoGrade()) {
			refreshDomainToScreen(getItemPedido());
		}
	}

	public void carregaDadosModoEdicaoOuGrade5() throws SQLException {
		if (LavenderePdaConfig.usaAcessoItemInseridoModoEdicao || LavenderePdaConfig.usaGradeProduto5()) {
			ItemPedido itemPedido = getItemPedido();
			itemPedido.naoComparaSeqItem = true;
			itemPedido.cdProduto = getProdutoSessaoVenda().cdProduto;
			int index = pedido.itemPedidoList.indexOf(itemPedido);
			if (index >= 0 && LavenderePdaConfig.usaAcessoItemInseridoModoEdicao && !isModoGrade(true)) {
				if (LavenderePdaConfig.usaGradeProduto4()) {
					ItemPedido itemPedidoGradeProduto = new ItemPedido();
					itemPedidoGradeProduto.pedido = pedido;
					itemPedidoGradeProduto.dsAgrupadorGradeFilter = itemPedido.getProduto().getDsAgrupadorGrade();
					ItemPedidoService.getInstance().carregaItensGradePreco(itemPedidoGradeProduto);
					if (ValueUtil.isNotEmpty(itemPedidoGradeProduto.itemPedidoPorGradePrecoList)) {
						fromItemPedidoInseridoPedido = false;
					}
				} else {
					itemPedido = (ItemPedido)pedido.itemPedidoList.items[index];
					setItemPedido((ItemPedido)itemPedido.clone());
					fromItemPedidoInseridoPedido = true;
				}
			} else if (isModoGrade(true)) {
				carregaItemAgrupadoEdicao(itemPedido);
				prepareAndLoadImageCarouselSliderByItemPedido(getItemPedido());
			} else {
				itemPedido.naoComparaSeqItem = false;
			}
		}
		}

	private void carregaItemAgrupadoEdicao(ItemPedido itemPedido) throws SQLException {
		int index = findIndexItemPedido(pedido.itemPedidoList, itemPedido);
		if (index >= 0) {
			itemPedido = (ItemPedido)pedido.itemPedidoList.items[index];
			itemPedido.nuSeqItemPedido = getItemPedido().nuSeqItemPedido;
			setItemPedido((ItemPedido)itemPedido.clone());
			fromItemPedidoInseridoPedido = !LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade();
			getItemPedido().dsAgrupadorGradeFilter = itemPedido.getProduto().getDsAgrupadorGrade();
			ItemPedidoService.getInstance().carregaItensAgrupadorGrade(getItemPedido());
		} else {
			inicializaItemParaVenda(itemPedido, false);
		}
	}

	private int findIndexItemPedido(Vector itemPedidoList, ItemPedido itemPedido) throws SQLException {
		int size = itemPedidoList.size();
		final Produto produto = itemPedido.getProduto();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) itemPedidoList.items[i];
			if (item.isItemVendaNormal() && ProdutoService.getInstance().isSameAgrupadorGrade(produto, item)) {
				return i;
			}
		}
		return -1;
	}

	public void gridClickAndRepaintScreen(boolean repaintScreen) throws SQLException {
		verificaProdutoGenerico = true;
		if (repaintScreen && LavenderePdaConfig.usaCestaPromocional) {
			repaintScreen();
		}
		if (gridClick()) {
			if (((LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() || !LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido() || !pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) && !LavenderePdaConfig.usaCestaPromocional) || fromProdutoPendenteGiroMultInsercao) {
				if (repaintScreen) {
					repaintScreen(true);
				} else {
					setEnableComponentsPossuemVariacao();
				}
			}
			showDescontoPorTabela();
			showRelComissaoPorTabelaPreco();
			setFocusInQtde();
		} else {
			if (!inVendaUnitariaMode && !usandoPopupDescPacote) {
				clearProdutoVendaAtual();
			}
		}
	}

	private boolean verificaProdutoSemEstoque(ItemPedido itemPedido) throws SQLException {
		if (!pedido.isIgnoraControleEstoque() && LavenderePdaConfig.usaRegistroProdutoFaltante && LavenderePdaConfig.bloquearVendaProdutoSemEstoque && !LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
			controlEstoque(itemPedido);
			if (itemPedido.estoque.qtEstoque <= 0.0 || itemPedido.estoque.qtEstoque < itemPedido.nuConversaoUnidade) {
				CadProdutoFaltaWindow cadProdutoFaltaWindow = new CadProdutoFaltaWindow(getItemPedido(), null, false, isEditing());
				cadProdutoFaltaWindow.popup();
					return true;
				}
			}
		return false;
	}

	public void validaAreaVendaAutomatica(ItemPedido itemPedido) throws SQLException {
		String[] cdsAreasCliente = AreaVendaClienteService.getInstance().findCdsAreasVendaCliente(pedido.cdCliente);
		String[] cdsAreasProduto = AreaVendaProdutoService.getInstance().findCdsAreasVendaProduto(itemPedido.cdProduto);
		int size = cdsAreasCliente.length;
		boolean foundArea = false;
		for (int i = 0; i < size; i++) {
			int sizeAreasProduto = cdsAreasProduto.length;
			for (int j = 0; j < sizeAreasProduto; j++) {
				if (cdsAreasCliente[i].equalsIgnoreCase(cdsAreasProduto[j])) {
					foundArea = true;
					break;
				}
			}
			if (foundArea) {
				break;
			}
		}
		if (!foundArea) {
			throw new ValidationException(Messages.PEDIDO_MSG_ADD_PRODUTO_AREAVENDA_DIF);
		}
	}

	private void validateProdutoClicado(final ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProduto() == null) {
			throw new ValidationException(Messages.PRODUTO_MSG_NAO_ENCONTRADO);
		}
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido()) {
			ProdutoBloqueadoService.getInstance().validateProdutoBloqueado(itemPedido, cbTabelaPreco);
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorCliente(RestricaoVendaCli.RESTRICAO_PEDIDO_VENDA)) {
			RestricaoVendaCliService.getInstance().validaBloqueiaVendaProdutoSelecionado(itemPedido.getProduto(), SessionLavenderePda.getCliente());
		}
		if (LavenderePdaConfig.usaAreaVendaAutoNoPedido) {
			validaAreaVendaAutomatica(itemPedido);
		}
		if (LavenderePdaConfig.usaAreaVendas) {
			AreaVendaProdutoService.getInstance().validadeProdutoAreaVenda(pedido.cdAreaVenda, itemPedido.cdProduto, false);
		}
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			ClienteService.getInstance().validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, pedido.getCliente());
		}
		if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorItemTabPreco() && ItemTabelaPrecoService.getInstance().countItemTabelaPrecoQualquerGrade(itemPedido.cdProduto, cbTabelaPreco.getCdsTabelaPreco()) <= 0) {
			throw new ValidationException(Messages.ITEMPEDIDO_ERRO_TODAS_GRADE_SEM_PRECO);
	}
		if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorEstoque() && EstoqueService.getInstance().getSumEstoqueGradeProduto(itemPedido.cdProduto, null) <= 0) {
			throw new ValidationException(Messages.ITEMPEDIDO_ERRO_TODAS_GRADE_SEM_ESTOQUE);
		}
	}
	
	public boolean gridClick() throws SQLException {
		if (!fromMenuCatalogForm) {
		UiUtil.showProcessingMessage();
		}
		boolean gridClickItemInseridoMultInsercao = showDetalheItemJaInseridoMultInsercao;

		try {
			if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
				gridClickItemInseridoMultInsercao = clickAcessaDetalheFromMultInsercao(gridClickItemInseridoMultInsercao);
			}
			ItemPedido itemPedido = getItemPedido();
			itemPedido.descontoPacote = null;
			itemPedido.descontoGrupoProduto = null;
			
			if (!gridClickItemInseridoMultInsercao && !fromRelNotificaoItemWindow) {
				if (!LavenderePdaConfig.usaAcessoItemInseridoModoEdicao && !LavenderePdaConfig.usaGradeProduto5() || (LavenderePdaConfig.usaAcessoItemInseridoModoEdicao && !pedido.itemPedidoList.contains(itemPedido)) || (LavenderePdaConfig.usaGradeProduto5() && !isProdutoPresenteItemPedido(itemPedido.getProduto()))) {
					inicializaItemParaVenda(itemPedido);
				} else if (LavenderePdaConfig.usaGradeProduto5() && !isModoGrade()) {
					if (isDescontoPedidoAplicadoPorItem()) {
						aplicaDescPedido(itemPedido);
				}
					if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(), Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
						itemPedido.vlPctAcrescimo = ItemPedidoService.getInstance().getVlPctAcrescimoPoliticaComercial(pedido.vlPctAcrescimoItem, itemPedido);
			}
				}
			}
			//--
			showAvisoProdutoPendenteRetirada();
			if (isPedidoComProdutoSimilaridadeAutorizado(itemPedido)) {
				return false;
			}
			if (verificaProdutoSimilar(itemPedido)) {
				return false;
			}
			if (isBloqueiaInsercaoDoProdutoRestrito(itemPedido.getProduto())) {
				return false;
			}
			//--
			if (!LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() && pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && ((getCheckedItensSize() > 0) || LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens())) {
				itemPedido.vlPctDesconto = vlPctDescontoMultiplosItens;
				itemPedido.setQtItemFisico(qtItemFisicoMultiplosItens);
			}
			//--
			showHistoricoItem();
			//--
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()  && !ignoraTrocaTabsAuto) {
				ultimaTabelaPrecoSelecionada = cbTabelaPreco.getValue();
				ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(itemPedido.cdProduto);
				reloadComboTabelaPreco(pedido, produtoTabPreco.dsTabPrecoList);
				if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisParaVenda()) {
					Vector tabelaPrecoList = TabelaPrecoService.getInstance().loadTabelaPrecoFiltrandoPorListaDeCodigos(cbTabelaPreco.getCdsTabelaPreco());
					cbTabelaPreco.removeAll();
					for (int i = 0; i < tabelaPrecoList.size(); i++) {
						TabelaPreco tabelaPreco = (TabelaPreco) tabelaPrecoList.items[i];
						if (existeItemTabelaPrecoQualquerGrade(tabelaPreco)) {
							cbTabelaPreco.add(tabelaPreco);
						}
					}
				}
				setaValorComboTabelaPreco();
				cbTabelaPreco.setValue(ultimaTabelaPrecoSelecionada);
				if (ValueUtil.isEmpty(cbTabelaPreco.getValue())) {
					cbTabelaPreco.setSelectedIndex(0);
					if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoSelecionada()) {
						tabelaPrecoChange();
					}
					atualizaProdutoNaGrid(produtoTabPreco, true);
				}
				if (ValueUtil.isNotEmpty(cbTabelaPreco.getValue())) {
					itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
				}
			}
			if (LavenderePdaConfig.selecionaTabelaPromocionalAoClicarNoProduto && !ignoraTrocaTabsAuto) {
				if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3()) {
					loadTabelaPromocionalAoClicarNoProdutoSemProdutoTabPreco(itemPedido);
				} else {
				String dsTabPrecoPromoList = ProdutoTabPrecoService.getInstance().getProdutoTabPrecoColumn(itemPedido.cdProduto, "DSTABPRECOPROMOLIST");
				String cdTabPromo = ItemPedidoService.getInstance().getCdTabelaProdPromocional(cbTabelaPreco.getCdsTabelaPreco(), dsTabPrecoPromoList);
				if (ValueUtil.isNotEmpty(cdTabPromo) && (!cdTabPromo.equals(cbTabelaPreco.getValue()))) {
					itemPedido.cdTabelaPreco = cdTabPromo;
					cbTabelaPreco.setValue(cdTabPromo);
				}
			}
			}
			//--
			if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
				itemPedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
			}
			//--
			if (!gridClickCestaPromocional(itemPedido)) {
				afterGridClick(itemPedido);
				return false;
			}
			//--
			if (LavenderePdaConfig.isUsaGradeProduto1A4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade())) {
				loadDadosGradeProduto(itemPedido);
			}

			if (!gridClickUnidadeAlternativa(itemPedido, gridClickItemInseridoMultInsercao)) {
				return false;
			}
			//--
			//--
			ItemPedidoService.getInstance().aplicaDescontoComissaoPadrao(itemPedido, pedido);
			//--
			if ((!LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() && !gridClickItemInseridoMultInsercao)) {
				changeItemTabelaPreco();
			}
			//--
			if (LavenderePdaConfig.usaDescQuantidadePorPacote && DescontoPacoteService.getInstance().hasDescPacoteInItemPedido(itemPedido, itemPedido.cdPacote, cbTabelaPreco.getValue())) {
				gridClickDescontoPacote(itemPedido, true, true);
			}
			//--
			if (itemPedido.descontoPacote == null && itemPedido.permiteAplicarDesconto() &&  DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido)) {
				if (!LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido && !gridClickDescontoQtdeGrupoProduto(itemPedido, true, true)) {
						return false;
					}
			} else {
				if (!LavenderePdaConfig.ocultaDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto() && !gridClickDescontoQuantidade(itemPedido, true, true)) {
					fromItemPedidoInseridoPedido = false;
					return false;
				}
				if (itemPedido.permiteAplicarDesconto() && !gridClickDescPromocionalPorQtde(true,true)) {
					return false;
				}
			}				//--
			String dsProduto = LavenderePdaConfig.usaGradeProduto5() && isModoGrade() && ! LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()? itemPedido.getProduto().getDsAgrupadorGrade() : itemPedido.getDsProdutoWithKey(itemPedido);
			changeLbDsProdutoValue(dsProduto);
			refreshProdutoToScreen(itemPedido);
			//--
			carregaUltimoPrecoItemPedido(itemPedido);
			//--
			refreshComponents();
			//--
			
			if ((itemPedido.estoque != null && ValueUtil.round(itemPedido.estoque.qtEstoque) > 0) || verificaLoteEstoque(itemPedido)) {
				itemPedido.fromProdutoFaltaWindow = false;
			}
			
			if (verificaSugestaoProdutosPorPrincipioAtivo(itemPedido)) {
				gridClickAndRepaintScreen(!inVendaUnitariaMode);
				fechouTelaDescontoQuantidade = true;
				return false;
			}
			//--
			if (!afterGridClick(itemPedido)) {
				return false;
			}
			if (itemPedido.getProduto() != null && !itemPedido.getProduto().isPermiteEstoqueNegativo() && verificaProdutoSemEstoque(itemPedido) && (LavenderePdaConfig.bloquearVendaProdutoSemEstoque || isUsaProdutoFaltanteEpermiteItemSemEstoque())) {
				return false;
			}
			if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || fromProdutoPendenteGiroMultInsercao || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
				addItensOnButtonMenu(itemPedido);
			}
			if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
				ItemPedidoLogService.getInstance().loadItemPedidoLog(itemPedido);
			}
			if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
				if (LavenderePdaConfig.usaConversaoUnidadesMedida && !LavenderePdaConfig.ocultaQtItemFaturamento) {
					itemPedido.qtItemFaturamento = 1;
					edQtItemFaturamento.setValue(1);
					edQtItemFaturamentoValueChange();
				} else {
					itemPedido.setQtItemFisico(1);
					edQtItemFisico.setValue(1);
					edQtItemFisicoValueChange(getItemPedido());
				}
					calcularClick();
				}
		} finally {
			if (!fromMenuCatalogForm) {
			UiUtil.unpopProcessingMessage();
		}
			getItemPedido().fromDescQtdWindow = false;
		}
		return true;
	}

	private boolean verificaLoteEstoque(ItemPedido itemPedido) throws SQLException {
		LoteProduto lote = new LoteProduto();
		String cdLocal = getCdLocal();
		Vector loteList = LoteProdutoService.getInstance().findAllByItemPedido(itemPedido, cdLocal);
		itemPedido.produtoLoteList = loteList;
		for (int i = 0; i < itemPedido.produtoLoteList.size(); i++) {
			lote = (LoteProduto) itemPedido.produtoLoteList.items[i];
			if (lote.qtEstoque > 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isUsaProdutoFaltanteEpermiteItemSemEstoque() {
		return LavenderePdaConfig.usaRegistroProdutoFaltante && !LavenderePdaConfig.bloquearVendaProdutoSemEstoque;
	}

	private void loadTabelaPromocionalAoClicarNoProdutoSemProdutoTabPreco(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdEmpresa = itemPedido.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = itemPedido.cdRepresentante;
		itemTabelaPrecoFilter.cdProduto = itemPedido.cdProduto;
		itemTabelaPrecoFilter.cdUf = itemPedido.cdUfClientePedido;
		itemTabelaPrecoFilter.cdPrazoPagtoPreco = itemPedido.cdPrazoPagtoPreco;
		itemTabelaPrecoFilter.cdItemGrade1 = itemPedido.cdItemGrade1;
		itemTabelaPrecoFilter.flPromocao = ValueUtil.VALOR_SIM;
		itemTabelaPrecoFilter.limit = 1;
		itemTabelaPrecoFilter.sortAtributte = ItemTabelaPreco.NOMECOLUNA_VLUNITARIO;
		itemTabelaPrecoFilter.sortAsc = ValueUtil.VALOR_SIM;
		if (LavenderePdaConfig.usaSegmentoNoPedido && LavenderePdaConfig.usaTabelaPrecoPorSegmento) {
			itemTabelaPrecoFilter.cdSegmentoFilter = itemPedido.pedido.cdSegmento;
			itemTabelaPrecoFilter.cdClienteSegmentoFilter = itemPedido.pedido.cdCliente;
		}
		String cdTabelaPrecoPromocional = ItemTabelaPrecoService.getInstance().findPrimeiroCdTabelaPrecoPromocional(itemTabelaPrecoFilter);
		String cdTabPromo = ItemPedidoService.getInstance().getCdTabelaProdPromocional(cbTabelaPreco.getCdsTabelaPreco(), cdTabelaPrecoPromocional);
		if (ValueUtil.isNotEmpty(cdTabPromo) && (!cdTabPromo.equals(cbTabelaPreco.getValue()))) {
			itemPedido.cdTabelaPreco = cdTabPromo;
			cbTabelaPreco.setValue(cdTabPromo);
		}
	}

	private void setaValorComboTabelaPreco() throws SQLException {
		if(!LavenderePdaConfig.selecionaTabPrecoMaiorDescPromo() || cbTabelaPreco.size() < 2) return;

		ItemPedido itemPedido = getItemPedido();
		if (itemPedido == null) return;

		String[] cdsTabelasPreco = cbTabelaPreco.getCdsTabelaPreco();
		String cdTabelaPrecoComMaiorDescontoPromocional = ItemTabelaPrecoService.getInstance().findCdTabelaPrecoComMaiorDescontoPromocional(cdsTabelasPreco, itemPedido.cdProduto);
		if (ValueUtil.isEmpty(cdTabelaPrecoComMaiorDescontoPromocional)) return;

		ultimaTabelaPrecoSelecionada = cdTabelaPrecoComMaiorDescontoPromocional;
		itemPedido.selecionadoTabelaComMaiorDesconto = true;

	}

	private boolean clickAcessaDetalheFromMultInsercao(boolean detailFromMultInsecao) throws SQLException {
		ItemPedido itemPedido = null;
		Produto produtoSelecionado = getProdutoSessaoVenda();
		if (showDetalheItemJaInseridoMultInsercao) {
			itemPedido = getItemPedido();
			itemPedido.naoComparaSeqItem = true;
		}
		if (isInsereMultiplosSemNegociacao() && getSelectedItemContainer() != null) {
			fromGridClick = true;
			insereUltimoItemMultInsercao();
			fromGridClick = false;
			setProdutoSelecionado(produtoSelecionado);
		}
		ItemPedido itemPedidoEdit = null;
		if (itemPedido != null && ValueUtil.isNotEmpty(itemPedido.cdProduto)) {
			itemPedidoEdit = (ItemPedido) ItemPedidoService.getInstance().findByPrimaryKey(itemPedido);
		}
		if (itemPedido != null && ValueUtil.isNotEmpty(itemPedido.nuPedido) && itemPedidoEdit != null) {
			itemPedido = itemPedidoEdit;
			setItemPedido(itemPedidoEdit);
			itemPedidoEdit.cdItemGrade1 = itemPedidoEdit.cdItemGrade2 = itemPedidoEdit.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			edit(itemPedidoEdit);
			disabledBtNextPrev = true;
		} else {
			if (detailFromMultInsecao) {
				int size = pedido.itemPedidoList.size();
				ItemPedido item = null;
				for (int i = 0; i < size; i++) {
					item = (ItemPedido) pedido.itemPedidoList.items[i];
					if (item.cdProduto.equals(produtoSelecionado.cdProduto)) {
						item = (ItemPedido) item.clone();
						setItemPedido(item);
						item.cdItemGrade1 = item.cdItemGrade2 = item.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						edit(item);
						disabledBtNextPrev = true;
						break;
					}
					item = null;
				}
				if (item == null) {
					add();
					detailFromMultInsecao = false;
				}
			}
		}
		return detailFromMultInsecao;
	}

	private void carregaUltimoPrecoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedido() && !fromRelGiroProduto) {
			ItemPedido itemPedidoUltimoPedidoCliente = itemPedido.getItemPedidoUltimoPedidoCliente();
			if (itemPedidoUltimoPedidoCliente == null) return;
			//--
			double ultimoPrecoPraticado = itemPedidoUltimoPedidoCliente.vlItemPedido;
			if (ultimoPrecoPraticado == 0) return;
			//--
			String cdUnidadeUltimoPedido = itemPedidoUltimoPedidoCliente.cdUnidade;
			String cdUnidadeAtual = itemPedido.cdUnidade;
			if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(cdUnidadeUltimoPedido)
					&& !StringUtil.getStringValue(itemPedido.cdUnidade).equals(cdUnidadeUltimoPedido)) {
				itemPedido.cdUnidade = cdUnidadeUltimoPedido;
				ProdutoUnidade ultimaProdutoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
				if (ultimaProdutoUnidade != null) {
					changeUnidadeAlternativa(cdUnidadeUltimoPedido);
				if (cbUnidadeAlternativa.getSelectedIndex() == -1) {
						cbUnidadeAlternativa.setValue(cdUnidadeAtual);
					} else {
						aplicaUltimoPrecoPraticado(itemPedido, ultimoPrecoPraticado);
				}
				} else {
					itemPedido.cdUnidade = cdUnidadeAtual;
			}
			} else {
				aplicaUltimoPrecoPraticado(itemPedido, ultimoPrecoPraticado);
			}
		} else {
			edVlItemPedido.setForeColor(ColorUtil.componentsForeColor);
		}
	}

	private void aplicaUltimoPrecoPraticado(ItemPedido itemPedido, double vlItemPedido) throws SQLException {
				itemPedido.vlItemPedido = vlItemPedido;
				edVlItemPedido.setValue(itemPedido.vlItemPedido);
				edVlItemPedido.setForeColor(LavendereColorUtil.COR_PRODUTO_ULTIMO_PRECO_PRATICADO_CLIENTE);
			calculoRapidoValorItem();
		}
	
	private boolean afterGridClick(ItemPedido itemPedido) throws SQLException {
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || fromProdutoPendenteGiroMultInsercao || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
			if (itemPedido.nuSeqItemPedido == 0) itemPedido.nuSeqItemPedido = -1;
			if (LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido) {
				carregaGridPrecoCondicao();
			} else if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.restringeItemPedidoPorLocal) {
				carregaGridLoteProduto();
				edVlPctDesconto.setEditable(isEnabled() && LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL));
			} else if (LavenderePdaConfig.usaSelecaoPorGrid()) {
				carregaGridTabelaPreco(null);
			} else if (LavenderePdaConfig.isExibeHistoricoProduto()) {
				carregaGridHistoricoTrocaDevolucaoProd();
			}
			if (LavenderePdaConfig.isExibeFotoTelaItemPedido()) {
				if (isShowFotoProduto()) {
					repaintNow();
					getFotoByItemPedido(itemPedido);
				}
			}
		}
		//--
		if (itemPedido.getQtItemFisico() != 0 && !pedido.itemPedidoList.contains(itemPedido)) {
			itemPedido.flTipoEdicao = LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() && itemPedido.vlPctAcrescimo > 0 ? ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT :  ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			try {
				calcularClick();
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(ex);
			}
		}
		//Produto bloqueado para a tabela de preço
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido()) {
				if (ProdutoBloqueadoService.getInstance().isBloqueadoForTabelaPreco(itemPedido)) {
			throw new ValidationException(Messages.ITEMTABELAPRECO_MSG_BLOQUEADO);
		}
			} else if (!LavenderePdaConfig.usaUnidadeAlternativa && itemPedido.getItemTabelaPreco().isFlBloqueadoBoolean()) {
				throw new ValidationException(Messages.ITEMTABELAPRECO_MSG_BLOQUEADO);
			}
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			if ((pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao())
					&& LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() && !LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens()) {
				ItemContainer itemContainer = getSelectedItemContainer();
				if (itemContainer != null && itemContainer.cbUnidadeAlternativa != null && ValueUtil.isEmpty(itemContainer.cbUnidadeAlternativa.getItems())) {
					throw new ValidationException(Messages.PEDIDO_TODAS_UN_RESTRITAS);
				}				
			}
		}
		
		if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && descontoQtdeGrupoProdutoList != null && descontoQtdeGrupoProdutoList.size() > 0 && selectDesconto && DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido)) {
			calculoRapidoDescontos(getItemPedido());
		}
		
		if (LavenderePdaConfig.usaDescQuantidadePorPacote && selectDescontoPacote && DescontoPacoteService.getInstance().hasDescPacoteInItemPedido(itemPedido, itemPedido.cdPacote, itemPedido.cdTabelaPreco)) {
			calculoRapidoDescontos(getItemPedido());
		}

		if (itemPedido.getProduto() != null && itemPedido.getProduto().isApresentaInfoComplCalculoPrecoPorVolumePorProduto()) {
			CadInfoComplementarItemPedidoWindow cadInfoComplementarItemPedidoWindow = new CadInfoComplementarItemPedidoWindow(getItemPedido(), pedido.isPedidoAberto());
			cadInfoComplementarItemPedidoWindow.popup();
			if (cadInfoComplementarItemPedidoWindow.canceladoInsercaoInfoComplementares) return false;
			if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto() || LavenderePdaConfig.calculaPrecoPorVolumeProduto)
				refreshDomainToScreen(getItemPedido());
		}
		
		return true;
	}

	private ItemContainer getSelectedItemContainer() {
		BaseListContainer.Item item = (BaseListContainer.Item) listContainer.getSelectedItem();
		if (item == null) return null;
		return (ItemContainer) item.rightControl;
	}
	
	public void carregaGridLoteProduto() throws SQLException {
		if (pedido.isPedidoBonificacao()) return;
		gridLoteProduto.setItems(null);
		gridLoteProduto.gridController.clearColors();
		ItemPedido itemPedido = getItemPedido();
		String cdLocal = getCdLocal();
		String cdLocalEmpresa = EmpresaService.getInstance().getEmpresa(SessionLavenderePda.cdEmpresa).cdLocal;
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			itemPedido.cdLoteProduto = null;
			itemPedido.cdLocal = null;
		}
		Vector loteList = LoteProdutoService.getInstance().findAllByItemPedido(itemPedido, cdLocal);
		itemPedido.produtoLoteList = loteList;
		if (ValueUtil.isEmpty(loteList)) return;
		int size = loteList.size();
		LoteProduto lote;
		String descString = DescVidaUtilGrupo.DESCVIDAUTILNAOENCONTRADO;
		String precoString = DescVidaUtilGrupo.DESCVIDAUTILNAOENCONTRADO;
		DescVidaUtilGrupo descVidaUtil;
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			sortGridLoteByColumn(CONFIG_COLUNA_DESCRICAO_GRID_LOTE);
		} else {
			sortGridLoteByColumn(CONFIG_COLUNA_DATA_VALIDADE_GRID_LOTE);
		}
		for (int i = 0; i < size; i++) {
			lote = (LoteProduto) loteList.items[i];
			double qtEstoque = LoteProdutoService.getInstance().getQtEstoqueLote(lote);
			if (!isEditing() && qtEstoque <= 0 && LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) continue;
			double desc = 0.0;
			double preco = itemPedido.vlBaseItemPedido;
			descVidaUtil = DescVidaUtilGrupoService.getInstance().getDescVidaUtilByLote(lote.vlPctvidautilproduto, itemPedido.getProduto().cdGrupoProduto1);
			if (descVidaUtil != null) {
				desc = descVidaUtil.vlPctDesconto;
				preco = ValueUtil.round(preco - ((preco * desc) / 100));
				descString = StringUtil.getStringValue(desc);
				precoString = StringUtil.getStringValue(preco);
			}
			lote.qtEstoque = qtEstoque;
			lote.vlPctDesconto = descString;
			lote.vlBaseItemString = precoString;
			String[] item = createItemGridLoteProduto(lote);
			gridLoteProduto.add(item);
			setaCorLoteProduto(lote, gridLoteProduto.size() - 1);
			if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
				if (!ValueUtil.isEmpty(pedido.cdLocal) && ValueUtil.valueEquals(lote.cdLocal, pedido.cdLocal)) {
					gridLoteProduto.setSelectedIndex(i);
					itemPedido.cdLoteProduto = lote.cdLoteproduto;
					itemPedido.cdLocal = lote.cdLocal;
				} else {
					if (ValueUtil.valueEquals(pedido.itemPedidoList.size(), 0) && !ValueUtil.isEmpty(cdLocalEmpresa) && ValueUtil.valueEquals(lote.cdLocal, cdLocalEmpresa)) {
						gridLoteProduto.setSelectedIndex(i);
						itemPedido.cdLoteProduto = lote.cdLoteproduto;
						itemPedido.cdLocal = lote.cdLocal;
		}
	}
			}
		}
	
	}

	public void carregaGridHistoricoTrocaDevolucaoProd() throws SQLException {
		gridHistTrocaDevProduto.clear();
		HistoricoTrocaDevolucaoProd historicoTrocaDevolucaoProd = HistoricoTrocaDevolucaoProdService.getInstance().findByPrimaryKey(SessionLavenderePda.getCliente().cdCliente, getItemPedido().cdProduto);
		String[][] gridItems = new String[2][5];
		gridItems[0][0] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.diasSemCompras);
		gridItems[0][1] = StringUtil.getStringValue(Messages.HISTORICO_VENDAS);
		gridItems[0][2] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.qtVendaSeisMeses);
		gridItems[0][3] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.qtVendaTresMeses);
		gridItems[0][4] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.qtVendaTrintaDias);
		gridItems[1][1] = StringUtil.getStringValue(Messages.HISTORICO_TROCAS_DEV);
		gridItems[1][2] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.percTrocaDevSeisMeses);
		gridItems[1][3] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.percTrocaDevTresMeses);
		gridItems[1][4] = StringUtil.getStringValue(historicoTrocaDevolucaoProd.percTrocaDevTrintaDias);
		gridHistTrocaDevProduto.add(gridItems);
		gridHistTrocaDevProduto.gridController.setColBackColor(Color.BRIGHT, 0);
		possuiHistoricoTrocadevolucao = historicoTrocaDevolucaoProd.possuiHistorico;
	}


	private void sortGridLoteByColumn(String column) {
		Integer dtValidadeGridColumnIndex = hashIndexItensGridLote.get(column);
		dtValidadeGridColumnIndex = dtValidadeGridColumnIndex == null ? 0 : dtValidadeGridColumnIndex;
		gridLoteProduto.qsort(dtValidadeGridColumnIndex, true);
	}

	private void sortGridLote(int column, Vector loteList) {
			if (COLUNA_LOTE == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_CD_LOTE_PRODUTO;
				SortUtil.qsortString(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			} else if (COLUNA_DT_VALIDADE == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_DT_VALIDADE;
				SortUtil.qsortInt(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			} else if (COLUNA_VL_PCTVIDAUTILPRODUTO == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_VL_PCVIDAUTILPRODUTO;
				SortUtil.qsortDouble(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			} else if (COLUNA_ESTOQUE == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_QTD_ESTOQUE;
				SortUtil.qsortDouble(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			} else if (COLUNA_ESTOQUE_RESERVADO == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_QTD_ESTOQUE_RESERVADO;
				SortUtil.qsortDouble(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			} else if (COLUNA_VL_PCT_DESCONTO == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_VL_PCTDESCONTO;
				SortUtil.qsortDouble(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			} else if (COLUNA_VL_BASE_ITEM_STRING == column) {
				LoteProduto.sortColumn = LoteProduto.SORT_COLUMN_VL_BASEITEMSTRING;
				SortUtil.qsortDouble(loteList.items, 0, loteList.size() - 1, gridLoteProduto.isAscending());
			}
	}

	private void setaCorLoteProduto(Vector produtoLoteList) {
		if (ValueUtil.isEmpty(produtoLoteList)) return;
		
		int size = produtoLoteList.size();
		for (int i = 0; i < size; i++) {
			setaCorLoteProduto((LoteProduto) produtoLoteList.items[i], i);
		}

	}

	private void setaCorLoteProduto(LoteProduto lote, int posicao) {
		if (LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco() && lote.qtTabPrecoLoteProd == 0) {
			gridLoteProduto.gridController.setRowForeColor(LavendereColorUtil.COR_FUNDO_GRID_LOTE_PRODUTO_VINCULADO_TABELA_PRECO, posicao);
		} else if (LavenderePdaConfig.pctVidaUtilLoteProdutoCritico > lote.vlPctvidautilproduto) {
			gridLoteProduto.gridController.setRowForeColor(LavendereColorUtil.COR_FONTE_QUE_CONTEM_LOTE_PERCENTUAL_DE_VIDA_CRITICO, posicao);
		} else {
			gridLoteProduto.gridController.setRowForeColor(-1, posicao);
		}
	}
	
	private String[] createItemGridLoteProduto(LoteProduto lote) {
		if (MOSTRA_TODOS_CAMPOS_GRID_LOTE.equals(LavenderePdaConfig.configColunasGridLoteProduto)) {
			return new String[] {lote.getLocal().toString(), lote.cdLoteproduto, StringUtil.getStringValue(lote.dtValidade), StringUtil.getStringValue(lote.vlPctvidautilproduto), StringUtil.getStringValue(lote.qtEstoque), StringUtil.getStringValue(lote.qtEstoquereservado), lote.vlPctDesconto, lote.vlBaseItemString};
		} else {
			return createArrayItemGridLoteProduto(lote);
		}
	}
	
	private String[] createArrayItemGridLoteProduto(LoteProduto lote) {
		List<String> list = new ArrayList<String>();
		String[] configList = LavenderePdaConfig.configColunasGridLoteProduto.split(";");
		for (String config : configList) {
			String field = createFieldItemByVlParameter(config, lote);
			if (field == null) continue;
			;
			list.add(field);
		}
		return list.toArray(new String[list.size()]);
	}

	private String createFieldItemByVlParameter(String config, LoteProduto lote) {
		switch (config) {
		case "1":
			String str = lote.getLocal().toString();
			return str != null ? str : "";
		case "2":
			return lote.cdLoteproduto;
		case "3":
			return StringUtil.getStringValue(lote.dtValidade);
		case "4":
			return StringUtil.getStringValueToInterface(lote.vlPctvidautilproduto);
		case "5":
			return StringUtil.getStringValueToInterface(lote.qtEstoque);
		case "6":
			return StringUtil.getStringValueToInterface(lote.qtEstoquereservado);
		case "7":
			return lote.vlPctDesconto;
		case "8":
			return lote.vlBaseItemString;
		}
		return null;
	}

	private void carregaGridTabelaPreco(String cdTabelaPrecoSelecionada) throws SQLException {
		if (!pedido.isPedidoBonificacao() && (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao())) {
			cdTabelaPrecoSelecionada = ValueUtil.isEmpty(cdTabelaPrecoSelecionada) ? cbTabelaPreco.getValue() : cdTabelaPrecoSelecionada;
			if (ValueUtil.isNotEmpty(cdTabelaPrecoSelecionada)) { 
				gridTabelaPreco.setItems(null);
				gridTabelaPreco.gridController.clearColors();
				Vector tabelaPrecoList = TabelaPrecoService.getInstance().loadTabelaPrecoFiltrandoPorListaDeCodigos(cbTabelaPreco.getCdsTabelaPreco());
				for (int i = 0; i < tabelaPrecoList.size(); i++) {
					TabelaPreco tabelaPreco = (TabelaPreco) tabelaPrecoList.items[i];
					if (existeItemTabelaPrecoQualquerGrade(tabelaPreco)) {
						String[] item = {tabelaPreco.cdTabelaPreco, StringUtil.getStringValue(tabelaPreco.dsTabelaPreco)};
						gridTabelaPreco.add(item);
					}
				}
				tabelaPrecoList =  gridTabelaPreco.getItemsVector();
				for (int i = 0; i < tabelaPrecoList.size(); i++) {
					String[] tabelaPreco = (String[]) tabelaPrecoList.items[i];
					if (ValueUtil.valueEquals(cdTabelaPrecoSelecionada, tabelaPreco[0])) {
						gridTabelaPreco.setSelectedIndex(i);
						if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisParaVenda()) {
							getItemPedido().cdTabelaPreco = cdTabelaPrecoSelecionada;
						}
						break;
					}
				}
			}
			if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisParaVenda()) {
				loadDadosGradeProduto(getItemPedido());
		}
	}
	}

	private boolean existeItemTabelaPrecoQualquerGrade(TabelaPreco tabelaPreco) throws SQLException {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		ItemPedido itemPedido = getItemPedido();
		itemTabelaPrecoFilter.cdEmpresa = itemPedido.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = tabelaPreco.cdRepresentante != null ? tabelaPreco.cdRepresentante : itemPedido.cdRepresentante;
		itemTabelaPrecoFilter.cdProduto = itemPedido.cdProduto;
		itemTabelaPrecoFilter.cdTabelaPreco = tabelaPreco.cdTabelaPreco;
		Vector itemTabPrecoList = ItemTabelaPrecoService.getInstance().findAllByExample(itemTabelaPrecoFilter);
		if (ValueUtil.isEmpty(itemTabPrecoList)) {
			return false;
		}
		if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorEstoque()) {
			return EstoqueService.getInstance().getSumQtEstoqueGradeNivel1PorLocalPorTabPreco(getItemPedido().cdProduto, tabelaPreco.cdLocalEstoque, itemTabPrecoList) > 0;
		}
		return true;
	}

	public void carregaGridPrecoCondicao() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			CondicaoPagamento condFilter = new CondicaoPagamento();
			condFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
			Vector list = CondicaoPagamentoService.getInstance().findAllByExampleOrderByQtDiasMedio(condFilter);
			IntVector listValidation = new IntVector();
			//--
			ItemPedido itemBase = getItemPedido();
			ItemPedido itemPedidoTest = new ItemPedido();
			itemPedidoTest.cdProduto = itemBase.cdProduto;
			itemPedidoTest.vlBaseItemPedido = itemBase.vlBaseItemPedido;
			itemPedidoTest.vlItemPedido = itemBase.vlItemPedido;
			itemPedidoTest.vlBaseItemTabelaPreco = itemBase.vlBaseItemTabelaPreco;
			//--
			gridDescCond.setItems(null);
			if (!ValueUtil.isEmpty(list)) {
				int size = list.size();
				for (int i = 0; i < size; i++) {
					int qtDias = ((CondicaoPagamento)list.items[i]).qtDiasMediosPagamento;
					if (listValidation.indexOf(qtDias) == -1) {
						ItemPedidoService.getInstance().getAndApplyIndiceFinanceiroLinhaProdutoNoItemPedido(itemPedidoTest, ((CondicaoPagamento)list.items[i]).cdCondicaoPagamento, pedido.getCliente());
						String[] item = {qtDias + Messages.DIAS, Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(itemPedidoTest.vlBaseItemPedido)};
						gridDescCond.add(item);
						listValidation.addElement(qtDias);
					}
				}
			}
			list = null;
			listValidation = null;
		}
	}


	private void loadFotosSugestaoVenda() throws SQLException {
		if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && containerFotosProdutos.isDisplayed() && !isEditing() && !(LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil  || LavenderePdaConfig.restringeItemPedidoPorLocal)) {
			Vector produtos = getProdutoList(false);
			Random r = new Random();
			//--
			nmImageList.removeAllElements();
			for (int i = 0; i < fotosProdSugList.size(); i++) {
				int index = r.between(0, produtos.size() - 1);
				ImageCarrousel imControl = (ImageCarrousel)fotosProdSugList.items[i];
				imControl.setBackForeColors(Color.darker(getBackColor(), 20), ColorUtil.componentsForeColor);
				if (index <= 0 && produtos.size() == 0) {
					imControl.setImgEmpty();
					imControl.updateDescricao(new String[] {" ", Messages.FOTO_PRODUTO_SEM_PRODUTO});
					continue;
				}
				if (produtos.size() == 1) {
					index = 0;
				}
				ProdutoBase produto = (ProdutoBase)produtos.items[index];
				int countTentativas = 0;
				boolean stopLoad = false;
				while (isProdutoInvalidoSugestao(produto)) {
					produtos.removeElementAt(index);
					if (produtos.size() == 0) {
						imControl.setImgEmpty();
						imControl.updateDescricao(new String[] {" ", Messages.FOTO_PRODUTO_SEM_PRODUTO});
						stopLoad = true;
						break;
					}
					index = r.between(0, produtos.size() - 1);
					if (produtos.size() == 1) {
						index = 0;
					}
					produto = (ProdutoBase)produtos.items[index];
					countTentativas++;
					if (countTentativas == 200) {
						stopLoad = true;
						break;
					}
				}
				if (stopLoad) {
					continue;
				}
				Vector v = new Vector(1);
				v = geraListaFotoProdutoToCarroussel(produto);
				imControl.setImgList(v);
				int color = getGridRowColor(produto);
				if (color != -1) {
					imControl.setBackForeColors(color, Color.getRGB(20, 20, 20));
				}
				produtos.removeElementAt(index);
			}
			containerFotosProdutos.scrollToPage(0);
		}
	}

	private Vector geraListaFotoProdutoToCarroussel(ProdutoBase produto) throws SQLException {
		Vector imgList = new Vector();
		String nmFoto = "";
		Vector fotoProdutoList = null;
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			FotoProdutoEmp filter = new FotoProdutoEmp();
			filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			filter.cdProduto = produto.cdProduto;
			fotoProdutoList = FotoProdutoEmpService.getInstance().findAllByExample(filter);
		} else {
			FotoProduto fotoProdutoFilter = new FotoProduto();
			fotoProdutoFilter.cdProduto = produto.cdProduto;
			fotoProdutoList = FotoProdutoService.getInstance().findAllByExample(fotoProdutoFilter);
		}
		if (ValueUtil.isEmpty(fotoProdutoList)) {
			String nmExtensao = FotoProdutoService.getInstance().getNmExtensaoFotoProduto(produto.cdProduto);
			nmFoto = produto.cdProduto;
			imgList.addElement(new String[] {nmFoto, StringUtil.getStringValue(produto.dsProduto), nmExtensao});	
		} else {
			FotoProduto fotoProduto = (FotoProduto) fotoProdutoList.items[0];
			String nmExtensao = fotoProduto.nmFoto.substring(fotoProduto.nmFoto.lastIndexOf("."), fotoProduto.nmFoto.length());
			nmFoto = fotoProduto.nmFoto.substring(0, fotoProduto.nmFoto.lastIndexOf(".")); // para não pegar o .jpg
			imgList.addElement(new String[] {nmFoto, StringUtil.getStringValue(produto.dsProduto), nmExtensao});	
			
		}
		String[] info = {nmFoto,produto.getCdProdutoOrDsReferenciaForFotoProduto()};
		nmImageList.addElement(info);
		return imgList;
	}

	private boolean isProdutoInvalidoSugestao(ProdutoBase produto) throws SQLException {
		if (produto.cdProduto.equals(getItemPedido().cdProduto)) {
			return true;
		}
		int itemPedidoSize = pedido.itemPedidoList.size();
		for (int i = 0; i < itemPedidoSize; i++) {
			if (produto.cdProduto.equals(((ItemPedido)pedido.itemPedidoList.items[i]).cdProduto)) {
				return true;
			}
		}
		return false;
	}

	protected void inicializaItemParaVenda(ItemPedido itemPedido) throws SQLException {
		inicializaItemParaVenda(itemPedido, false);
	}

	protected void inicializaItemParaVenda(ItemPedido itemPedido, boolean inserindoMultiInsercaoSemNegociacao) throws SQLException {
		bgItemTroca.setValueBoolean(false);
		getItemPedidoService().clearDadosItemPedido(pedido, itemPedido);
		itemPedido.pedido = pedido;
		itemPedido.flTipoEdicao = 0;
		if (!inserindoMultiInsercaoSemNegociacao || ValueUtil.isEmpty(itemPedido.cdProduto)) {
		itemPedido.setProduto(getProdutoSessaoVenda());
		}
		if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
			setInfosAdicionaisEscolhaItemPedido(itemPedido);
			}
		validateProdutoClicado(itemPedido);
		//Tabela de preço
		if ((!inVendaUnitariaMode && !isEditing()) || houveAlteracaoItemPorMultInsercao) {
			if (ValueUtil.isEmpty(itemPedido.cdTabelaPreco)) {
				itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
				if (cbTabelaPreco.getSelectedIndex() != -1) {
					itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
				}
			}
		}
		itemPedido.vlPctTotalMargemItem = 0;
		itemPedido.cdUnidade = LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() ? cbUnidade.getValue() : itemPedido.getProduto().cdUnidade;
		if (LavenderePdaConfig.usaProdutoRestrito && pedido.getCliente().isPossuiDescontoExtraProdutoRestrito() && itemPedido.getProduto().isFlProdutoRestrito()) {
			itemPedido.vlPctDescProdutoRestrito = pedido.getCliente().vlPctDescProdutoRestrito;
		}
//		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
//			DescProgressivoConfigService.getInstance().applyDescProgressivoInItemPedido(itemPedido);
//		}
		if (LavenderePdaConfig.isPropagaUltimoDescontoItemPedido()) {
			itemPedido.vlPctDesconto = valueUltimoDescontoNegociado;
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
				itemPedido.vlPctDesconto2 = valueUltimoDesconto2Negociado;
                if (getItemPedido().getItemTabelaPreco() != null && ValueUtil.VALOR_SIM.equals(getItemPedido().getItemTabelaPreco().flUsaDesconto3)) {
                	itemPedido.vlPctDesconto3 = valueUltimoDesconto3Negociado;
                } else {
                	itemPedido.vlPctDesconto3 = 0;
                }
			}
		} else if (isDescontoPedidoAplicadoPorItem()) {
			if (LavenderePdaConfig.isAcumulaComDescDoItem()) {
				itemPedido.vlPctDescPedido = pedido.vlPctDescItem;
			} else {
				aplicaDescPedido(itemPedido);
		}
		}
		if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(), Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
			itemPedido.vlPctAcrescimo = ItemPedidoService.getInstance().getVlPctAcrescimoPoliticaComercial(pedido.vlPctAcrescimoItem, itemPedido);
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli) {
			itemPedido.vlPctDescontoCanal = getVlPctDescontoCanal(itemPedido);
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
			itemPedido.vlRetornoProduto = ProdutoClienteService.getInstance().getVlRetornoProduto(itemPedido.cdProduto, SessionLavenderePda.getCliente().cdCliente);
		}
		if (LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			itemPedido.vlPctDescontoCanal = getVlPctDescontoCanal(itemPedido);
			itemPedido.vlPctContratoCli = itemPedido.pedido.getCliente().vlPctContratoCli;
		}
		if (LavenderePdaConfig.isApresentaQtdPreCarregadaDeVendaNoItemDoPedido() && !pedido.isGondola()) {
			itemPedido.setQtItemFisico(LavenderePdaConfig.apresentaQtdPreCarregadaDeVendaNoItemDoPedido);
		}
		boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem = LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem();
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
			if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				itemPedido.vlPctDescCliente = pedido.vlPctDescCliente;
			}
			if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				itemPedido.vlPctDescontoCondicao = pedido.vlPctDescontoCondicao;
			}
			if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				itemPedido.vlPctDescFrete = pedido.vlPctDescFrete;
			}
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			if (itemPedido.getProduto().isKit()) {
				ProdutoKitService.getInstance().loadItemKitPedidoFromProdutoKit(itemPedido);
			} else {
				itemPedido.itemKitPedidoList = new Vector(0);
			}
		}
		if ((exibeComboLocal()) && ckProdutoDescPromocional.isChecked()) {
			itemPedido.cdLocal = cbLocal.getValue();
		}
		if (LavenderePdaConfig.isPropagaUltimoDescontoAplicadoNoProdutoNesteCliente()) {
			double vlPctDescontoErp = ItemPedidoService.getInstance().getVlPctUltimoDescontoAplicadoNoProdutoNesteCliente(pedido, itemPedido.cdProduto);
			itemPedido.vlPctDesconto = vlPctDescontoErp > -1 ? vlPctDescontoErp : itemPedido.vlPctDesconto;
	}
	}
	
	private void setInfosAdicionaisEscolhaItemPedido(ItemPedido itemPedido) throws SQLException {
		if (fromSimilar) {
			fromSimilar = false;
			populaInfosAdicionaisProdutoSimilar(itemPedido);
		} else if (itemPedido.getProduto().complementar) {
			ItemPedidoService.getInstance().loadInfosAdicionaisItemComplementar(itemPedido);
		} else if (ValueUtil.isEmpty(itemPedido.flOrigemEscolhaItemPedido)) {
			setFlOrigemEscolhaItemPedido(itemPedido, ItemPedido.FLORIGEMESCOLHA_PADRAO);
		}
	}

	private void populaInfosAdicionaisProdutoSimilar(ItemPedido itemPedido) {
		setFlOrigemEscolhaItemPedido(itemPedido, ItemPedido.FLORIGEMESCOLHA_SIMILARES);
		itemPedido.cdProdutoOrigem = itemPedidoInfoAdic.cdProduto;
		itemPedido.vlItemPedidoOrigem = itemPedidoInfoAdic.vlItemPedido;
	}

	private void setFlOrigemEscolhaItemPedido(ItemPedido itemPedido, String flOrigemEscolhaItemPedido) {
		itemPedido.flOrigemEscolhaItemPedido = flOrigemEscolhaItemPedido;
	}

	private boolean isDescontoPedidoAplicadoPorItem() throws SQLException {
		return LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && !LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL);
	}

	private void aplicaDescPedido(ItemPedido itemPedido) throws SQLException {
		double vlPctMaxDesconto = 0;
		if (LavenderePdaConfig.selecionaTabPrecoMaiorDescPromo() && !itemPedido.selecionadoTabelaComMaiorDesconto) {
			vlPctMaxDesconto = ItemTabelaPrecoService.getInstance().findVlTabelaPrecoComMaiorDescontoPromocional(cbTabelaPreco.getCdsTabelaPreco(), itemPedido.cdProduto);
		} else {
			if (ValueUtil.isEmpty(itemPedido.cdTabelaPreco)) {
				itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
				if (cbTabelaPreco.getSelectedIndex() != -1) {
					itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
				}
			}
			vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescontoItemPedido(itemPedido);
		}
		if (pedido.vlPctDescItem > vlPctMaxDesconto) {
			itemPedido.vlPctDesconto = vlPctMaxDesconto;
			if (!inicializaItemFromGrid && !isInsereMultiplosSemNegociacao()) UiUtil.showConfirmMessage(MessageUtil.getMessage(Messages.DESCONTO_PEDIDO_NAO_APLICADO_ITEM_PEDIDO, vlPctMaxDesconto));
		} else {
			itemPedido.vlPctDesconto = pedido.vlPctDescItem;
		}
	}

	private double getVlPctDescontoCanal(ItemPedido itemPedido) throws SQLException {
		if (CanalCliGrupoService.getInstance().isProdutoPossuiDescontoCanalCliGrupoEspecifico(itemPedido.getProduto(), itemPedido.pedido.getCliente())) {
			return CanalCliGrupoService.getInstance().getDescontoCanalCliGrupo(itemPedido, pedido.getCliente());
		} else {
			return LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() ? itemPedido.pedido.vlPctDescItem : pedido.vlDesconto;
		}
	}

	private boolean gridClickCestaPromocional(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaCestaPromocional) {
			CestaPromocional cesta = CestaPromocionalService.getInstance().findCestaByClienteProduto(itemPedido.cdProduto, pedido.getCliente().cdCliente);
			if (cesta != null) {
				selecionaCestaPromocional(itemPedido, cesta);
				return false;
			}
			if (pedido.getTabelaPreco() != null) {
				edDsTabelaPreco.setText(StringUtil.getStringValue(pedido.getTabelaPreco().dsTabelaPreco));
			}
			cbTabelaPrecoVisible();
		}
		return true;
	}

	protected boolean gridClickDescontoQuantidade(final ItemPedido itemPedido, boolean inGridClick, boolean showBtSemDesconto) throws SQLException {
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && ValueUtil.isNotEmpty(cbTabelaPreco.getItems()) && !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido) && !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido)) {
			Vector tabelaPrecoList = new Vector(cbTabelaPreco.getItems());
			String cdTabelaPrecoAnterior = cbTabelaPreco.getValue();
			//Obrigatório invalidar o desconto quantidade no clique da grid
			descontoQuantidadeList = null;
			if (tabelaPrecoList.size() > 1) {
				// setar se vai selecionar a tabela com maior desconto na menor quantidade ou se vai utilizar a tabela atual selecionada
				if (!LavenderePdaConfig.ignoraEscolhaAutoMelhorTabPrecoDescPorQtd && !ignoraTrocaTabsAuto) {
					descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(DescQuantidadeService.getInstance().getDescontoQuantidadeListTabelaMaiorDesconto(tabelaPrecoList, itemPedido), getItemPedido());
					maiorDesc = true;
				} else {
					itemPedido.cdTabelaPreco = cdTabelaPrecoAnterior;
					descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto), getItemPedido());
					maiorDesc = false;
				}
				//--
				if (ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco) && (!itemPedido.cdTabelaPreco.equals(cdTabelaPrecoAnterior))) {
					cbTabelaPreco.setValue(itemPedido.cdTabelaPreco);
				}
			} else {
				itemPedido.cdTabelaPreco = cdTabelaPrecoAnterior;
				ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
				Vector descontoQuantidadeListItemPedido = itemTabelaPreco != null ? itemTabelaPreco.descontoQuantidadeList : null;
				if (ValueUtil.isNotEmpty(descontoQuantidadeListItemPedido) && itemPedido.getQtItemFisico() != 0) {
					descontoQuantidadeList = descontoQuantidadeListItemPedido;
				} else if (inGridClick && descontoQuantidadeListItemPedido != null) {
					descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(descontoQuantidadeListItemPedido, itemPedido);
				} else {
					descontoQuantidadeList = getDescontoQuantidadeList(itemPedido);
			}
			}
			//Verifica se existe desconto quantidade para o produto e se existir exibe o mesmo
			return showFormDescontoQtde(itemPedido, cdTabelaPrecoAnterior, inGridClick, showBtSemDesconto);
		}
		return true;
	}
	
	protected boolean gridClickDescontoPacote(final ItemPedido itemPedido, final boolean showBtSemDesconto, final boolean inGridClick) throws SQLException {
		itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
		return showDescontoPacoteWindow(itemPedido, showBtSemDesconto, inGridClick);
	}

	protected boolean gridClickDescontoQtdeGrupoProduto(final ItemPedido itemPedido, boolean inGridClick, boolean showBtSemDesconto) throws SQLException {
		if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && ValueUtil.isNotEmpty(cbTabelaPreco.getItems())) {
			String cdTabelaPreco = cbTabelaPreco.getValue();
			//Obrigatório invalidar o desconto quantidade por grupo no clique da grid
			descontoQtdeGrupoProdutoList = null;
			itemPedido.cdTabelaPreco = cdTabelaPreco;
			descontoQtdeGrupoProdutoList = DescontoGrupoService.getInstance().calcDescQtdeGrupoUnidadeAlternativa(getDescontoQtdeGrupoProdutoList(), getItemPedido());
			//Verifica se existe desconto quantidade para o grupo do produto e se existir exibe o mesmo
			return showDescQtdeGrupoProdutoWindow(itemPedido, inGridClick, showBtSemDesconto);
		}
		return true;
	}
	
	protected boolean gridClickDescPromocionalPorQtde(boolean inGridClick, boolean showBtCancelApenas) throws SQLException {
		if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(getItemPedido()) && ValueUtil.isNotEmpty(cbTabelaPreco.getItems()) && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL)) {
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				getItemPedido().descPromocional = null;
				getItemPedido().descPromocionalComQtdList = null;
			}
			if (ValueUtil.isNotEmpty(getItemPedido().getDescPromocionalComQtdList())) {
				return showDescPromocionalPorQtdeWindow(inGridClick, showBtCancelApenas);
			}
		}
		return true;
	}
	
	public void gridLoteProdutoClick() throws SQLException {
		validaLotePorTabelaPreco();
		screenToDomain();
		String[] itemSelected = gridLoteProduto.getSelectedItem();
		ItemPedido itemBase = getItemPedido();
		itemBase.cdLoteProduto = itemSelected[hashIndexItensGridLote.get(CONFIG_COLUNA_DESCRICAO_GRID_LOTE)];
		itemBase.vlPctDesconto = 0;
		itemBase.vlPctAcrescimo = 0;
		itemBase.usaPctMaxDescLoteProduto = false;
		itemBase.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
		Integer indexDescVidaUtil = hashIndexItensGridLote.get(CONFIG_COLUNA_PERCENTUAL_DESCONTO_GRID_LOTE);
		if (indexDescVidaUtil != null && !itemSelected[indexDescVidaUtil].equals(DescVidaUtilGrupo.DESCVIDAUTILNAOENCONTRADO)) {
			itemBase.vlPctDesconto = ValueUtil.getDoubleValue(itemSelected[indexDescVidaUtil]);
			itemBase.usaPctMaxDescLoteProduto = true;
			itemBase.pctMaxDescLoteProdutoSelected = ValueUtil.getDoubleValue(itemSelected[indexDescVidaUtil]);
		}
		refreshDomainToScreen(itemBase);
		if (itemBase.getQtItemFisico() > 0) {
			calcularClick();
		}
		edVlPctDesconto.setEditable(isEnabled() && LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL));
		if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			LoteProduto loteProduto = LoteProdutoService.getInstance().findByItemPedido(itemBase);
			if (loteProduto != null) {
				getItemPedido().cdLocal = loteProduto.cdLocal;
				if (!ValueUtil.isEmpty(pedido.cdLocal) && !ValueUtil.valueEquals(loteProduto.cdLocal, pedido.cdLocal)) {
					String dsLocalPadraoPedido = LocalService.getInstance().getDsLocal(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdLocal);
					UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MSG_VALIDACAO_LOCAL_DIFERENTE, new Object[] {dsLocalPadraoPedido}));
	}
			}
		}
	}
	
	private void validaLotePorTabelaPreco() throws SQLException {
		if (!LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) return;
		
		ItemPedido itemPedido = getItemPedido();
		String cdLoteProdutoSelecionado = gridLoteProduto.getSelectedItem()[hashIndexItensGridLote.get(CONFIG_COLUNA_DESCRICAO_GRID_LOTE)];
		String cdTabelaPrecoSelecionada = ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco) ? itemPedido.cdTabelaPreco : pedido.cdTabelaPreco;
		if (TabPrecoLoteProdService.getInstance().existeLoteProdutoParaTabelaPreco(itemPedido, cdLoteProdutoSelecionado, cdTabelaPrecoSelecionada)) return;
		
		itemPedido.cdLoteProduto = null;
		throw new ValidationException(Messages.TABPRECOLOTEPROD_VENDA_NAO_PERMITIDA);
	}

	private void gridTabelaPrecoClick() throws SQLException {
		String[] itemSelected = gridTabelaPreco.getSelectedItem();
		if (getItemPedido().cdTabelaPreco.equals(itemSelected[0])) return;
		
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.ITEMPEDIDO_TABELA_PRECO_AGUARDE_RECARREGANDO);
		mb.popupNonBlocking();
		try {
			validateGradeChange(itemSelected);
			cbTabelaPreco.setValue(itemSelected[0]);
			cbTabelaPrecoClick();
		} finally {
			mb.unpop();
		}
	}

	private void validateGradeChange(String[] itemSelected) throws SQLException {
		if (!isEditing() || !LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisParaVenda()) return;
			
		ItemPedido itemPedidoClone = (ItemPedido) getItemPedido().clone();
		itemPedidoClone.cdTabelaPreco = itemSelected[0];
		ItemTabelaPreco itemTabPreco = itemPedidoClone.getItemTabelaPreco();
		if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorItemTabPreco() && itemTabPreco.cdTabelaPreco == null) {
			setUltimaTabelaSelecionadaGrid();
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_GRADE_SEM_PRECO, itemPedidoClone.getTabelaPreco().toString()));
		}
		if(!LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorEstoque()) return;
		
		double qtEstoque = EstoqueService.getInstance().getQtEstoqueErp(itemPedidoClone.cdProduto, itemPedidoClone.cdItemGrade1, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, TabelaPrecoService.getInstance().getTabelaPreco(itemTabPreco.cdTabelaPreco).cdLocalEstoque);
		if (qtEstoque > 0) return;
		
		setUltimaTabelaSelecionadaGrid();
		throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_ERRO_GRADE_SEM_ESTOQUE, itemPedidoClone.getTabelaPreco().toString()));
	}

	private void setUltimaTabelaSelecionadaGrid() throws SQLException {
		Vector tabelaPrecoList =  gridTabelaPreco.getItemsVector();
		for (int i = 0; i < tabelaPrecoList.size(); i++) {
			String[] tabelaPreco = (String[]) tabelaPrecoList.items[i];
			if (!ValueUtil.valueEquals(getItemPedido().cdTabelaPreco, tabelaPreco[0])) continue;
			
			gridTabelaPreco.setSelectedIndex(i);
			break;
		}
	}

	private void limpaFiltros() throws SQLException {
		edFiltro.setValue("");
		filterByPrincipioAtivo = false;
		btGroupTipoFiltros.setSelectedIndex(btGroupTipoFiltros.getSelectedIndex());
		if (LavenderePdaConfig.usaFiltroFornecedor) {
			cbFornecedor.load();
		}
		cbFornecedor.setSelectedIndex(0);
		cbAtributoProd.setSelectedIndex(0);
		cbAtributoOpcaoProd.setSelectedIndex(0);
		resetCombosGrupoProduto();
		ckProdutoDescQtd.setChecked(false);
		ckApenasKit.setChecked(false);
		ckProdutoPromocional.setChecked(false);
		ckProdutoDescPromocional.setChecked(LavenderePdaConfig.usaDescPromo);
		ckPreAltaProduto.setChecked(false);
		ckProdutoOportunidade.setChecked(false);
		ckApenasProdutoVendido.setChecked(false);
		ckMaxDescProdCli.setChecked(false);
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox()) {
			cbDescPromocional.loadDescPromocional(pedido, null);
			cbDescPromocional.setSelectedIndex(0);
		}
		if (exibeComboLocal()) {
			cbLocal.setSelectedIndex(0);
			cbLocal.setEnabled(true);
		}
		zeraValueUltimoDesconto();
		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null) {
			btTipoPesquisaEdFiltro.setSelectedIndex(0);
	}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			cbDescProgressivoConfig.load(pedido.getCliente());
			cbDescProgressivoConfig.setSelectedIndex(0);
			cbDescProgConfigFam.setSelectedIndex(1);
	}
	}

	public void limpaDadosAoSairPedido() {
		ultimaTabelaPrecoSelecionada = "";
		ultimaTabelaPrecoSelecionadaListaItens = "";
		zeraValueUltimoDesconto();
	}

	private void zeraValueUltimoDesconto() {
		if (!LavenderePdaConfig.isPropagaUltimoDescontoItemNoPedido()) return; 
		
		valueUltimoDescontoNegociado = 0;
		valueUltimoDesconto2Negociado = 0;
		valueUltimoDesconto3Negociado = 0;
	}

	private boolean btPrincipioAtivoClick(ItemPedido itemPedido, boolean chamadaPeloMenu) throws SQLException {
		String[] principioAtivos = StringUtil.split(StringUtil.getStringValue(itemPedido.getProduto().dsPrincipioAtivo), ';');
		if (principioAtivos.length > 0) {
			if (chamadaPeloMenu || UiUtil.showConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_MOSTRAR_PRINCIPIOATIVO)) {
				verificaProdutoGenerico = false;
				ListProdPrincipioAtivoForm listProdutoPrincipioAtivoForm = new ListProdPrincipioAtivoForm(principioAtivos, itemPedido.cdProduto, cbTabelaPreco.getValue());
				listProdutoPrincipioAtivoForm.popup();
				produtoSelecionado = listProdutoPrincipioAtivoForm.produto;
				return produtoSelecionado != null;
			}
		} else if (chamadaPeloMenu) {
			UiUtil.showInfoMessage(Messages.PRODUTO_MSG_SEM_PRINCIPIO_ATIVO);
		}
		return false;
	}

	private boolean verificaSugestaoProdutosPorPrincipioAtivo(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.usaSugestaoVendaProdutoMesmoPrincipioAtivo || !verificaProdutoGenerico) return false;
		
		if (itemPedido.estoque.flErroEstoqueOnline || itemPedido.estoque.qtEstoque > 0) return false;
		
		return btPrincipioAtivoClick(itemPedido, false);
	}

	private void btBonificarClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.isBonificandoItemPeloBotao = true;
		try {
			if (itemPedido.getProduto() == null) {
				throw new ValidationException(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID, Messages.PRODUTO_NOME_ENTIDADE));
			}
				itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
			if (!ItemPedidoService.getInstance().validaQtItemBonificado(itemPedido)) {
				itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
				return;
			}
			if (!LavenderePdaConfig.isValidaPercMaxValorPedidoBonificadoNoFechamento()) {
				validateBonificacaoPedidoUI(itemPedido, pedido);
			}
			if (LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao || LavenderePdaConfig.isPermiteBonificarQualquerProduto() || itemPedido.getProduto().isPermiteBonificacao()
					|| LavenderePdaConfig.isUsaPoliticaBonificacao()) {
				itemPedido.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
				salvarNovoClick();
			} else {
					UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_NAO_PERMITE_BONIFICAR, StringUtil.getStringValue(itemPedido.getProduto().dsProduto)));
				}
		} catch (Throwable e) {
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			if (!ValidationException.EXCEPTION_ABORT_PROCESS.equals(e.getMessage())) {
				throw e;
		}
		} finally {
			itemPedido.isBonificandoItemPeloBotao = false;
	}
	}

	private boolean showFormDescontoQtde(ItemPedido itemPedido, String cdTabelaPrecoAnterior, boolean inGridClick, boolean showBtSemDesconto) throws SQLException {
		DescQuantidade descontoQuantidade = null;
		if (descontoQuantidadeList.size() > 0 && (!LavenderePdaConfig.usaSomenteQtdDescQtdSimilar || DescQuantidadeService.getInstance().hasDescontoQuantidade(itemPedido))) {
				getItemPedidoService().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
				double nuConversaoUnidade = 1;
				ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(getItemPedido());
				if (LavenderePdaConfig.usaUnidadeAlternativa &&  produtoUnidade != null) {
					nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(getItemPedido().getItemTabelaPreco(), produtoUnidade);
				}
				PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
				String dsUnidade = UnidadeService.getInstance().getDsUnidade(itemPedido.cdUnidade) + "/" + StringUtil.getStringValueToInterface(nuConversaoUnidade);
				boolean canceled = false;
			if (LavenderePdaConfig.mostraVolumeVendasMensalNoDescontoQuantidade) {
				try {
					ListDescQuantidadeVolVendasMensalWindow listDescQuantidadeVolVendasMensalWindow = new ListDescQuantidadeVolVendasMensalWindow(itemPedido, descontoQuantidadeList);
					listDescQuantidadeVolVendasMensalWindow.popup();
					canceled = listDescQuantidadeVolVendasMensalWindow.isCanceled;
					if (!canceled) {
						isAddingFromListDescQtd = true;
						descontoQuantidade = listDescQuantidadeVolVendasMensalWindow.descontoQuantidadeSelected;
					}
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			} else {
				ListDescQuantidadeWindow listDescontoQuantidadeWindow = new ListDescQuantidadeWindow(itemPedido.getProduto(), itemPedido.cdTabelaPreco, descontoQuantidadeList, maiorDesc, itemPedido.vlBaseItemPedido, dsUnidade, showBtSemDesconto, itemPedido);
				listDescontoQuantidadeWindow.popup();
				itemPedido.flIgnoraDescQtd = StringUtil.getStringValue(listDescontoQuantidadeWindow.isIgnorar);
				if (LavenderePdaConfig.aplicaApenasDescQtdOuIndiceCondPagto) {
					itemPedido.isAplicaIndiceCondPagtoPorDescQtdIgnorado = listDescontoQuantidadeWindow.isIgnorar;
				}
				usandoPopupDescQtdItemPedido = true;
				canceled = listDescontoQuantidadeWindow.isCanceled;
				if (!canceled) {
				descontoQuantidade = listDescontoQuantidadeWindow.descontoQuantidadeSelected;
				} else if (itemPedido.descQuantidade != null) {
					itemPedido.cancelouDescQtd = true;
				}
				itemPedido.editandoDescQtd = !listDescontoQuantidadeWindow.isIgnorar && !canceled;
			}
				itemPedido.oldQtItemFisicoDescQtd = 0; //Para dar suporte ao parametro 640-apresentaQtdPreCarregadaDeVendaNoItemDoPedido
				//--
			if (!canceled) {
					if (descontoQuantidade != null) {
						selectDesconto = true;
					if (!LavenderePdaConfig.ignoraEscolhaAutoMelhorTabPrecoDescPorQtd && ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco) && !itemPedido.cdTabelaPreco.equals(cdTabelaPrecoAnterior)) {
							tabelaPrecoChange();
							selectProdutoAutoAndRefresh(itemPedido, descontoQuantidade);
						if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_QNT_ITEM)) { // Se escolheu um desconto Por Quantidade, o valor base para calculo do flex assume o valor com desconto quantidade
								aplicaDescontoVlBaseItemTabelaPreco(itemPedido,	descontoQuantidade);
							}
						} else {
						if (!LavenderePdaConfig.mostraVolumeVendasMensalNoDescontoQuantidade) {
							itemPedido.setQtItemFisico(descontoQuantidade.qtItem);
						}
							if (!DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido)) {
								if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
									itemPedido.vlPctDesconto =  ValueUtil.round((1 - ((itemPedido.vlBaseItemPedido - descontoQuantidade.vlDesconto) / itemPedido.vlBaseItemPedido)) * 100);
								itemPedido.vlPctAcrescimo = 0;
								} else {
								if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta) {
										CalculaEmbalagensService.getInstance().calculaDescQtdEmbalagemCompleta(itemPedido, false);
										if (itemPedido.gerouEmbalagemCompleta) {
											itemPedido.vlPctFaixaDescQtd = descontoQuantidade.vlPctDesconto;
										}
								} else if (LavenderePdaConfig.aplicaDescontoQuantidadeVlBase) {
									if (itemPedido.oldVlPctFaixaDescQtd > 0) {
										ItemPedidoService.getInstance().reverteDescQtd(itemPedido);
									}
									itemPedido.vlBaseItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (descontoQuantidade.vlPctDesconto / 100)));
									itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
									itemPedido.vlBaseCalculoDescPromocional = itemPedido.vlBaseItemPedido;
									itemPedido.vlUnidadePadrao  = ValueUtil.round(itemPedido.vlUnidadePadrao * (1 - (descontoQuantidade.vlPctDesconto / 100)));
									itemPedido.vlPctFaixaDescQtd = descontoQuantidade.vlPctDesconto;
									itemPedido.oldVlPctFaixaDescQtd = itemPedido.vlPctFaixaDescQtd;
									} else {
										itemPedido.vlPctFaixaDescQtd = descontoQuantidade.vlPctDesconto;
									}
								}
							} else {
								itemPedido.vlPctDesconto = 0d;
							}
							itemPedido.flEditandoQtItemFaturamento = false;
							flFocoQtFaturamento = false;
							refreshDomainToScreen(itemPedido);
						if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex) { // Se escolheu um desconto Por Quantidade, o valor base para calculo do flex assume o valor com desconto quantidade
								aplicaDescontoVlBaseItemTabelaPreco(itemPedido,	descontoQuantidade);
							}
						}
						repaint();
						return true;
					} else {
					selectDesconto = selectDescontoPacote = false;
						itemPedido.cdTabelaPreco = cdTabelaPrecoAnterior;
						cbTabelaPreco.setValue(cdTabelaPrecoAnterior);
						if (!LavenderePdaConfig.ignoraEscolhaAutoMelhorTabPrecoDescPorQtd) {
							descontoQuantidadeList = null;
						descontoQuantidadeList = getDescontoQuantidadeList(itemPedido);
						}
					}
				} else {
					return false;
				}
			} else if (!inGridClick) {
				UiUtil.showWarnMessage(Messages.PRODUTO_MSG_NAO_POSSUI_DESC_X_QTDADE);
			}
		if (LavenderePdaConfig.aplicaApenasDescQtdOuIndiceCondPagto && ValueUtil.isEmpty(descontoQuantidadeList)) {
			itemPedido.isAplicaIndiceCondPagtoPorDescQtdIgnorado = true;
		}
		return true;
	}

	private void aplicaDescontoVlBaseItemTabelaPreco(ItemPedido itemPedido, DescQuantidade descontoQuantidade) throws SQLException {
		if (LavenderePdaConfig.usaDescontoPorQuantidadeValor && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_QNT_ITEM)) {
			itemPedido.getItemTabelaPreco().vlBase = ValueUtil.round(itemPedido.vlBaseItemTabelaPreco - descontoQuantidade.vlDesconto);
		} else {
			itemPedido.getItemTabelaPreco().vlBase = ValueUtil.round(itemPedido.vlBaseItemTabelaPreco * (1 - (itemPedido.vlPctDesconto / 100)));
		}
	}

	private boolean showDescontoPacoteWindow(ItemPedido itemPedido, final boolean showBtSemDesconto, final boolean inGridClick) throws SQLException {
		DescontoPacote descPacote = null;
		if (!DescontoPacoteService.getInstance().hasDescPacoteInItemPedido(itemPedido, itemPedido.cdPacote, itemPedido.cdTabelaPreco) && !inGridClick) {
			UiUtil.showWarnMessage(Messages.DESCONTOPACOTE_MSG_NAO_POSSUI_DESC_X_QTDE);
			return false;
		}
		ListDescontoPacoteWindow listDescontoPacoteWindow = new ListDescontoPacoteWindow(itemPedido, loadDsUnidadeForPopUpDesconto(itemPedido), showBtSemDesconto, inGridClick);
		listDescontoPacoteWindow.popup();
		descPacote = itemPedido.descontoPacote = listDescontoPacoteWindow.descontoPacote;
		usandoPopupDescPacote = true;
		selectDescontoPacote = false;
		if (listDescontoPacoteWindow.isCanceled || descPacote == null) {
			itemPedido.cdPacote = null;
			return false;
		}
		itemPedido.descontoGrupoProduto = null;
		itemPedido.cdPacote = descPacote.pacote.cdPacote;
		selectDescontoPacote = true;
		itemPedido.vlPctDesconto = descPacote.vlPctDesconto;
		itemPedido.setQtItemFisico(descPacote.qtItem);
		itemPedido.flEditandoQtItemFaturamento = flFocoQtFaturamento = false;
		refreshDomainToScreen(itemPedido);
		repaint();
		return true;
	}

	private boolean showDescQtdeGrupoProdutoWindow(ItemPedido itemPedido, boolean inGridClick, boolean showBtSemDesconto) throws SQLException {
		DescontoGrupo descGrupoProduto = null;
		if (!LavenderePdaConfig.ocultaPopupDescQtdeGrupoItemPedido && !PedidoService.getInstance().possuiCondicaoComercial(pedido)) {
			if (descontoQtdeGrupoProdutoList.size() > 0) {
				ListDescQtdeGrupoProdutoWindow listDescontoQtdeGrupoWindow = new ListDescQtdeGrupoProdutoWindow(itemPedido, descontoQtdeGrupoProdutoList, loadDsUnidadeForPopUpDesconto(itemPedido), showBtSemDesconto);
				listDescontoQtdeGrupoWindow.popup();
				descGrupoProduto = listDescontoQtdeGrupoWindow.descontoGrupoProduto;
				usandoPopupDescQtdItemPedido =  true;
				//--
				if (!listDescontoQtdeGrupoWindow.isCanceled) {
					listDescontoQtdeGrupoWindow = null;
					if (descGrupoProduto != null) {
						selectDesconto = true;
						if (DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido)) {
							itemPedido.vlPctDesconto = descGrupoProduto.vlPctDesconto;
							itemPedido.setQtItemFisico(descGrupoProduto.qtItem);
						} else {
							itemPedido.vlPctDesconto = 0d;
						}
						itemPedido.flEditandoQtItemFaturamento = false;
						flFocoQtFaturamento = false;
						refreshDomainToScreen(itemPedido);
						repaint();
						itemPedido.descontoGrupoProduto = descGrupoProduto;
						itemPedido.descontoPacote = null;
						itemPedido.cdPacote = null;
						return true;
					} else {
						selectDesconto = false;
					}
				} else {
					listDescontoQtdeGrupoWindow = null;
					return false;
				}
			} else if (!inGridClick) {
				UiUtil.showWarnMessage(Messages.DESCONTOGRUPO_MSG_NAO_POSSUI_DESC_X_QTDE);
			}
		}
		return true;
	}
	
	private String loadDsUnidadeForPopUpDesconto(ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
		double nuConversaoUnidade = 1;
		ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(getItemPedido());
		if (LavenderePdaConfig.usaUnidadeAlternativa &&  produtoUnidade != null) {
			nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(getItemPedido().getItemTabelaPreco(), produtoUnidade);
		}
		PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
		return UnidadeService.getInstance().getDsUnidade(itemPedido.cdUnidade) + "/" + StringUtil.getStringValueToInterface(nuConversaoUnidade);
	}

	private boolean showDescPromocionalPorQtdeWindow(boolean inGridClick, boolean showBtCancelApenas) throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (!LavenderePdaConfig.ocultaDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto()) {
			if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido)) {
				boolean estavaIgnorandoDesconto = itemPedido.isIgnoraDescQtdPro();
				ListDescPromocionalPorQtdeWindow listDescPromocionalPorQtdeWindow = new ListDescPromocionalPorQtdeWindow(showBtCancelApenas, itemPedido);
				listDescPromocionalPorQtdeWindow.popup();
				itemPedido.flIgnoraDescQtdPro = StringUtil.getStringValue(listDescPromocionalPorQtdeWindow.isIgnorar);
				if (!listDescPromocionalPorQtdeWindow.isCanceled) {
					if (listDescPromocionalPorQtdeWindow.isIgnorar) {
						itemPedido.flIgnoraDescQtdPro = StringUtil.getStringValue(true);
						getPedidoService().resetDadosItemPedido(pedido, itemPedido);
					} else {
						if (estavaIgnorandoDesconto) {
							itemPedido.oldQtItemFisicoDescPromocionalQtd = 0;
						}
						itemPedido.flIgnoraDescQtdPro = StringUtil.getStringValue(false);
					}
				}
				if (listDescPromocionalPorQtdeWindow.isCanceled) {
					itemPedido.descPromocional = null;
					itemPedido.descPromocionalComQtdList = null;
					return false;
				} else {
					DescPromocional descPromocional = listDescPromocionalPorQtdeWindow.descPromocional;
					if (descPromocional != null) {
						itemPedido.descPromocional = descPromocional;
						itemPedido.setQtItemFisico(descPromocional.qtItem);
						edQtItemFisico.setValue(descPromocional.qtItem);
						if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
							itemPedido.flPromocional = ValueUtil.VALOR_SIM;
						}
						edQtItemFisicoValueChange(getItemPedido());
						itemPedido.fromDescQtdWindow = true;
							calcularClick();
						itemPedido.flEditandoQtItemFaturamento = false;
						}
					return true;
				}
			} else if (!inGridClick) {
				UiUtil.showWarnMessage(Messages.DESC_PROMOCIONAL_POR_QTDE_MSG_NAO_POSSUI);
			}
		}
		return true;
	}

	public ListProdutoSugestaoDifPedidoWindow getListProdutoSugestaoDifPedidoWindow(boolean onFechamentoPedido, boolean onVoltarClick) throws SQLException {
		return getListProdutoSugestaoDifPedidoWindow(onFechamentoPedido, onVoltarClick, false);
	}

	public ListProdutoSugestaoDifPedidoWindow getListProdutoSugestaoDifPedidoWindow(boolean onFechamentoPedido, boolean onVoltarClick, boolean onAcessoManual) throws SQLException {
		if (listProdutoSugestaoDifPedidoWindow == null || (onFechamentoPedido != listProdutoSugestaoDifPedidoWindow.onFechamentoPedido) || (!pedido.equals(listProdutoSugestaoDifPedidoWindow.pedido))) {
			listProdutoSugestaoDifPedidoWindow = new ListProdutoSugestaoDifPedidoWindow(pedido, onFechamentoPedido);
			listProdutoSugestaoDifPedidoWindow.setCadItemPedidoForm(this);
		} else {
			listProdutoSugestaoDifPedidoWindow.list();
		}
		listProdutoSugestaoDifPedidoWindow.onAcessoManual = onAcessoManual;
		return listProdutoSugestaoDifPedidoWindow;
	}

	public ListProdutoSugestaoSemQtdeWindow getListProdutoSugestaoSemQtdeWindow(boolean onFechamentoPedido, boolean onVoltarClick) throws SQLException {
		return getListProdutoSugestaoSemQtdeWindow(onFechamentoPedido, onVoltarClick, false);
	}

	public ListProdutoSugestaoSemQtdeWindow getListProdutoSugestaoSemQtdeWindow(boolean onFechamentoPedido, boolean onVoltarClick, boolean onAcessoManual) throws SQLException {
		if (listProdutoSugestaoSemQtdeWindow == null || (onFechamentoPedido != listProdutoSugestaoSemQtdeWindow.onFechamentoPedido) || (!pedido.equals(listProdutoSugestaoSemQtdeWindow.pedido))) {
			listProdutoSugestaoSemQtdeWindow = new ListProdutoSugestaoSemQtdeWindow(pedido, onFechamentoPedido);
			listProdutoSugestaoSemQtdeWindow.setCadItemPedidoForm(this);
		} else {
			if (!onVoltarClick) {
				listProdutoSugestaoSemQtdeWindow.loadComboSugestaoVendasAndSelectTodos();
			}
			listProdutoSugestaoSemQtdeWindow.list(pedido);
		}
		if (onVoltarClick && !listProdutoSugestaoSemQtdeWindow.hasSugestaoVenda() && !listProdutoSugestaoSemQtdeWindow.isComboAllSelected()) {
			listProdutoSugestaoSemQtdeWindow.loadComboSugestaoVendasAndSelectTodos();
			listProdutoSugestaoSemQtdeWindow.list(pedido);
		}
		listProdutoSugestaoSemQtdeWindow.onAcessoManual = onAcessoManual;
		return listProdutoSugestaoSemQtdeWindow;
	}

	public ListProdutoSugestaoComQtdeWindow getListProdutoSugestaoComQtdeWindow(boolean onFechamentoPedido, boolean onVoltarClick) throws SQLException {
		return getListProdutoSugestaoComQtdeWindow(onFechamentoPedido, onVoltarClick, false);
	}

	public ListProdutoSugestaoComQtdeWindow getListProdutoSugestaoComQtdeWindow(boolean onFechamentoPedido, boolean onVoltarClick, boolean onAcessoManual) throws SQLException {
		if (listProdutoSugestaoComQtdeWindow == null || (onFechamentoPedido != listProdutoSugestaoComQtdeWindow.onFechamentoPedido) || (!pedido.equals(listProdutoSugestaoComQtdeWindow.pedido))) {
			listProdutoSugestaoComQtdeWindow = new ListProdutoSugestaoComQtdeWindow(pedido, onFechamentoPedido);
			listProdutoSugestaoComQtdeWindow.setCadItemPedidoForm(this);
		} else {
			if (!onVoltarClick) {
				listProdutoSugestaoComQtdeWindow.loadComboSugestaoVendasAndSelectFirst();
			}
			listProdutoSugestaoComQtdeWindow.list(pedido);
		}
		if (onVoltarClick && !listProdutoSugestaoComQtdeWindow.hasSugestaoVenda()) {
			listProdutoSugestaoComQtdeWindow.loadComboSugestaoVendasAndSelectFirst();
			listProdutoSugestaoComQtdeWindow.list(pedido);
		}
		listProdutoSugestaoComQtdeWindow.onAcessoManual = onAcessoManual;
		return listProdutoSugestaoComQtdeWindow;
	}

	public ListMultiplasSugestoesProdutosWindow getListMultiplasSugestoesProdutosWindow(boolean onVoltarClick) throws SQLException {
		if (onVoltarClick || listMultiplasSugestoesProdutosWindow == null || (!pedido.equals(listMultiplasSugestoesProdutosWindow.pedido))) {
			listMultiplasSugestoesProdutosWindow = new ListMultiplasSugestoesProdutosWindow(pedido, true);
			listMultiplasSugestoesProdutosWindow.setCadItemPedidoForm(this);
		} else {
			listMultiplasSugestoesProdutosWindow.loadComboSugestaoVendasAndSelectTodos();
		}
		listMultiplasSugestoesProdutosWindow.list(pedido);
		return listMultiplasSugestoesProdutosWindow;
	}
	
	public ListMultiplasSugestoesProdutosWindow getListMultiplasSugestoesProdutosNovoItemWindow(boolean onVoltarClick) throws SQLException {
		if (listMultiplasSugestoesProdutosNovoItemWindow == null || (!pedido.equals(listMultiplasSugestoesProdutosNovoItemWindow.pedido))) {
			listMultiplasSugestoesProdutosNovoItemWindow = new ListMultiplasSugestoesProdutosWindow(pedido, false);
			listMultiplasSugestoesProdutosNovoItemWindow.setCadItemPedidoForm(this);
		} else {
			if (onVoltarClick || listMultiplasSugestoesProdutosNovoItemWindow.produtoIndustriaSelected != null) {
				listMultiplasSugestoesProdutosNovoItemWindow.acaoAntesReabrirPopupAposEdicao();
			} else {
				listMultiplasSugestoesProdutosNovoItemWindow.list(pedido);
			}
		}
		return listMultiplasSugestoesProdutosNovoItemWindow;
	}
	
	private boolean isDeveCarregarVisualizacaoMultiplasSugestoesProdutos(Produto produto) {
		return listMultiplasSugestoesProdutosVisualizacaoWindow == null || !ValueUtil.valueEquals(produto, listMultiplasSugestoesProdutosVisualizacaoWindow.produto);
	}

	public ListItemPedidoAbaixoPesoMinimoWindow getListItemPedidoAbaixoPesoMinimoWindow(boolean onFechamentoPedido, boolean onVoltarClick) throws SQLException {
		if (listItemPedidoAbaixoPesoMinimoWindow == null || (!pedido.equals(listItemPedidoAbaixoPesoMinimoWindow.pedido))) {
			listItemPedidoAbaixoPesoMinimoWindow = new ListItemPedidoAbaixoPesoMinimoWindow(pedido);
			listItemPedidoAbaixoPesoMinimoWindow.setCadItemPedidoForm(this);
		} else {
			listItemPedidoAbaixoPesoMinimoWindow.setPedido(pedido);
			listItemPedidoAbaixoPesoMinimoWindow.list(pedido);
		}
		if (onVoltarClick && !listItemPedidoAbaixoPesoMinimoWindow.hasItemPedidoAbaixoPeso()) {
			listItemPedidoAbaixoPesoMinimoWindow.list(pedido);
		}
		return listItemPedidoAbaixoPesoMinimoWindow;
	}

	public ListItemPedidoAbaixoValorMinimoWindow getListItemPedidoAbaixoValorMinimoWindow(boolean onFechamentoPedido, boolean onVoltarClick) throws SQLException {
		if (listItemPedidoAbaixoValorMinimoWindow == null || (!pedido.equals(listItemPedidoAbaixoValorMinimoWindow.pedido))) {
			listItemPedidoAbaixoValorMinimoWindow = new ListItemPedidoAbaixoValorMinimoWindow(pedido);
			listItemPedidoAbaixoValorMinimoWindow.setCadItemPedidoForm(this);
		} else {
			listItemPedidoAbaixoValorMinimoWindow.setPedido(pedido);
			listItemPedidoAbaixoValorMinimoWindow.list(pedido);
		}
		if (onVoltarClick && !listItemPedidoAbaixoValorMinimoWindow.hasItemPedidoAbaixoPeso()) {
			listItemPedidoAbaixoValorMinimoWindow.list(pedido);
		}
		return listItemPedidoAbaixoValorMinimoWindow;
	}

	private void selectProdutoAutoAndRefresh(ItemPedido itemPedido, DescQuantidade descontoQuantidade) throws SQLException {
		int size = listContainer.size();
		String cellText;
		for (int i = 0; i < size; i++) {
			cellText = listContainer.getId(i);
			if (getItemPedido().getProduto().getRowKey().equals(cellText)) {
				itemPedido.setQtItemFisico(descontoQuantidade.qtItem);
				if (LavenderePdaConfig.usaDescontoPorQuantidadeValor && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_QNT_ITEM)) {
					itemPedido.vlPctDesconto = descontoQuantidade.vlDesconto;
				} else {
					itemPedido.vlPctDesconto = descontoQuantidade.vlPctDesconto;
					itemPedido.vlPctFaixaDescQtd = descontoQuantidade.vlPctDesconto;
				}
				refreshDomainToScreen(itemPedido);
				refreshProdutoToScreen(itemPedido);
				listContainer.setSelectedIndex(i);
				i = size;
			}
		}
	}

	private void selecionaCestaPromocional(final ItemPedido itemPedido, final CestaPromocional cesta) throws SQLException {
		SessionLavenderePda.cdTabelaPreco = cesta.cdTabelaPreco;
		itemPedido.cdTabelaPreco = cesta.cdTabelaPreco;
		edDsTabelaPreco.setValue(Messages.TABELAPRECO_ESPECIAL);
		itemPedido.vlBaseItemTabelaPreco = cesta.vlUnitario;
		itemPedido.vlBaseItemPedido = cesta.vlUnitario;
		itemPedido.vlItemPedido = cesta.vlUnitario;
		itemPedido.pctMaxDescCestaPromo = cesta.vlPctMaxDesconto;
		itemPedido.usaCestaPromo = true;
		refreshProdutoToScreen(itemPedido);
		refreshDomainToScreen(itemPedido);
		cbTabelaPrecoVisible();
	}


	// ********************* Métodos de controle da grid de produtos ********************************
	// **********************************************************************************************
	// **********************************************************************************************

	@Override
	protected Vector getProdutoList() throws SQLException {
		Vector produtoList = getProdutoList(true);
		sugereCadastroProdutoDesejado = LavenderePdaConfig.isUsaCadastroProdutoDesejadosForaCatalogoSugerindoCadastro() && ValueUtil.isEmpty(produtoList);
		return produtoList;
	}
	
	private Vector getProdutoList(boolean includeInterfaceFiltros) throws SQLException {
		ProdutoBase produtoFilter;
		Vector listProduto;
		try {
			produtoFilter = getProdutoFilter(includeInterfaceFiltros);
			listProduto = getListProduto(includeInterfaceFiltros, produtoFilter);
		afterList(listProduto, produtoFilter, includeInterfaceFiltros);
		} catch (FilterNotInformedException e ) {
			listProduto = new Vector();
		}
		return listProduto;
	}
	
	private ProdutoBase getProdutoFilter(boolean includeInterfaceFiltros) throws SQLException {
		ProdutoBase produtoFilter = super.getProdutoBaseFilter();
		produtoFilter.cdOrigemSetor = pedido.cdOrigemSetor;
		produtoFilter.produtoIndustriaFilter = produtoIndustriaFilter;
		produtoFilter.flCritico = pedido.flCritico;
		if (includeInterfaceFiltros) {
			getProdutoFilterInterface(produtoFilter);
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			produtoFilter.flExcecaoProduto = ValueUtil.isNotEmpty(pedido.getTipoPedido().flExcecaoProduto) ? pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
			produtoFilter.cdTipoPedidoFilter = pedido.cdTipoPedido;
		}
		if (!pedido.getCliente().isNovoClienteDefaultParaNovoPedido() && !pedido.getCliente().isClienteDefaultParaNovoPedido()) {
			prepareProdutoClienteFilter(produtoFilter);
			prepareClienteProdutoFilter(produtoFilter);
			prepareProdutoCondPagtoFilter(produtoFilter);
		}
		prepareGiroProdutoFilter(produtoFilter);

		if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
			produtoFilter.unidade = new Unidade();
			produtoFilter.unidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoFilter.unidade.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Unidade.class);
			
		}
		return produtoFilter;
	}

	private void getProdutoFilterInterface(ProdutoBase produtoFilter) throws SQLException {
		produtoFilter.filterToItemPedido = true;
		produtoFilter.itemTabelaPreco = new ItemTabelaPreco();
		produtoFilter.itemTabelaPreco.cdTabelaPreco = pedido.cdTabelaPreco;
		produtoFilter.flPreAltaCusto = StringUtil.getStringValue(ckPreAltaProduto.isChecked());
		produtoFilter.flKit = StringUtil.getStringValue(ckApenasKit.isChecked());
		
		if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido()) {
			produtoFilter.flFiltraProdutoPromocional = StringUtil.getStringValue(ckProdutoPromocional.isChecked());
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() && !LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
			((ProdutoTabPreco)produtoFilter).flFiltraProdutoDescPromocional = StringUtil.getStringValue(ckProdutoDescPromocional.isChecked());
			if (exibeComboLocal()) {
				produtoFilter.cdLocalFilter = cbLocal.getValue();
			}
		}
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			produtoFilter.cdGrupoDestaque = cbProdutoGrupoDestaque.getValue();
		}
		if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			produtoFilter.pacoteFilter = cbPacote.getValue();
			if (LavenderePdaConfig.usaTabPrecoDescQuantidadePorPacote && produtoFilter.pacoteFilter != null) {
				produtoFilter.pacoteFilter.cdTabelaPrecoFilter = cbTabelaPreco.getValue();
			}
		}

		if (LavenderePdaConfig.usaOportunidadeVenda && produtoFilter instanceof ProdutoTabPreco) {
			if (pedido.isOportunidade()) {
				((ProdutoTabPreco)produtoFilter).flFiltraProdutoOportunidade = ValueUtil.VALOR_SIM;
			} else {
				((ProdutoTabPreco)produtoFilter).flFiltraProdutoOportunidade = StringUtil.getStringValue(ckProdutoOportunidade.isChecked());
			}
		}
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
			if (pedido != null && pedido.isPedidoEmitidoNoMesCorrente() && !pedido.isPedidoBonificacao()
					&& !pedido.isPedidoTroca() && !pedido.isOportunidade() && !pedido.isPedidoComplementar()) {
				pedido.isPedidoNoMesCorrente = true;
			}
			produtoFilter.flVendido = ckApenasProdutoVendido.getValue();
		}
		produtoFilter.sortAsc = listContainer.sortAsc;
			produtoFilter.sortAtributte = listContainer.atributteSortSelected;
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(pedido.getCliente().cdGrupoPermProd)) {
			produtoFilter.cdGrupoPermProd = pedido.getCliente().cdGrupoPermProd;
		}
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
			produtoFilter.dsPositivados = cbNaoPositivado.getValue();
			produtoFilter.cdCesta = cbCesta.getValue();
			produtoFilter.cdCliente = pedido.cdCliente;
		}
		if (LavenderePdaConfig.usaGradeProduto5()) {
			produtoFilter.cdCliente = pedido.cdCliente;
		}
		if ((LavenderePdaConfig.isUsaKitProduto()) && (cbKit.getValue() != null)) {
			produtoFilter.cdKit = cbKit.getValue();
		}
		if (LavenderePdaConfig.usaFiltroAtributoProduto) {
			if (ValueUtil.isNotEmpty(cbAtributoProd.getValue()) && ValueUtil.isNotEmpty(cbAtributoOpcaoProd.getValue())) {
				produtoFilter.cdAtributoProduto = cbAtributoProd.getValue(); 
				produtoFilter.cdAtributoOpcaoProduto = cbAtributoOpcaoProd.getValue();
			}
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto != 0 && (ValueUtil.isNotEmpty(cbGrupoProduto1.getValue()) || LavenderePdaConfig.ocultaGrupoProduto1)) {
			produtoFilter.cdGrupoProduto1 = cbGrupoProduto1.getValue();
			if (ValueUtil.isNotEmpty(cbGrupoProduto2.getValue())) {
				produtoFilter.cdGrupoProduto2 = cbGrupoProduto2.getValue();
			}
			if (ValueUtil.isNotEmpty(cbGrupoProduto3.getValue())) {
				produtoFilter.cdGrupoProduto3 = cbGrupoProduto3.getValue();
			}
			if (ValueUtil.isNotEmpty(cbGrupoProduto4.getValue())) {
				produtoFilter.cdGrupoProduto4 = cbGrupoProduto4.getValue();
			}
		}
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() && ValueUtil.isNotEmpty(cbGrupoDescProd.getValue())) {
			produtoFilter.cdGrupoDescProd = cbGrupoDescProd.getValue();
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			produtoFilter.produtoUnidadeFilter = new ProdutoUnidade();
			produtoFilter.produtoUnidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoFilter.produtoUnidadeFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoUnidade.class);
			produtoFilter.produtoUnidadeFilter.cdUnidade = cbUnidade.getValue();
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoDisponivel()) {
			produtoFilter.cdTabelaPreco = cbTabelaPreco.getValue();
		}
		if ((LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && LavenderePdaConfig.permiteAlterarValorItemComIPI) || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			produtoFilter.cdTributacaoClienteFilter = pedido.getCliente().cdTributacaoCliente;
			produtoFilter.cdTributacaoClienteFilter2 = pedido.getCliente().cdTributacaoCliente2;
			produtoFilter.cdClienteFilter = pedido.getCliente().cdCliente;
			produtoFilter.cdTipoPedidoFilter = pedido.cdTipoPedido;
			produtoFilter.cdUFFilter = ValueUtil.isNotEmpty(pedido.getCliente().cdEstadoComercial) ? pedido.getCliente().cdEstadoComercial : ValueUtil.VALOR_ZERO;
			produtoFilter.fromListItemPedido = true;
		}
		if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla()) {
			produtoFilter.cdCliente = pedido.cdCliente;
		}
		Estoque estoque = new Estoque();
		if (LavenderePdaConfig.isUsaLocalEstoque()) {
			estoque.cdLocalEstoque = pedido.getCdLocalEstoque();
		}
		produtoFilter.estoque = estoque;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			produtoFilter.itemTabelaPreco.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
		}
		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && ckProdutoDescPromocional.isChecked()) {
			produtoFilter.joinDescPromocional = true;
			produtoFilter.cdCliente = pedido.cdCliente;
			produtoFilter.cdGrupoDescCliFilter = pedido.getCliente().cdGrupoDescCli;
			produtoFilter.cdCondicaoComercialFilter = pedido.cdCondicaoComercial;
			if (LavenderePdaConfig.usaDescPromo) {
				produtoFilter.cdLocalFilter = cbLocal.getValue();
			}
		}
		if (LavenderePdaConfig.usaDescMaxProdCli && ckMaxDescProdCli.isChecked()) {
			produtoFilter.filtraDescMaxProdCli = true;
			produtoFilter.cdCliente = pedido.cdCliente;
		}
		if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto()  && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			produtoFilter.cdCentroCustoProdFilter = pedido.cdCentroCusto;
	}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
			produtoFilter.cdPlataformaVendaFilter = pedido.cdPlataformaVenda;
		}
		if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial) {
			produtoFilter.cdCondicaoComercialFilter = pedido.cdCondicaoComercial;
		}
		if (LavenderePdaConfig.isConfigValorMinimoDescPromocional()) {
			produtoFilter.cdTabelaPrecoVlMinDescPromoFilter = cbTabelaPreco.getValue();
			produtoFilter.cdClienteVlMinDescPromoFilter = pedido.cdCliente;
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			produtoFilter.descProgressivoConfigFilter = (DescProgressivoConfig) cbDescProgressivoConfig.getSelectedItem();
			if (produtoFilter.descProgressivoConfigFilter != null) {
				produtoFilter.descProgressivoConfigFilter.cliente = pedido.getCliente();
				produtoFilter.descProgressivoConfigFilter.pedidoFilter = pedido;
				produtoFilter.descProgConfigFamFilter = new DescProgConfigFam();
				cbDescProgConfigFam.applyValue(produtoFilter.descProgConfigFamFilter);
				produtoFilter.descProgConfigFamFilter.cdFamiliaDescProg = cbDescProgFamilia.getValue();
		}
		}
		if (LavenderePdaConfig.apresentaFiltroDescQtd) {
			produtoFilter.filtraProdutoDescQtd = ckProdutoDescQtd.isChecked();
			}
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
			produtoFilter.cdMarcadores = cbMarcador.getSelected();
			}
		if (LavenderePdaConfig.isUsaFiltroEstoqueDisponivelTelaItemPedido()) {
			produtoFilter.cdStatusEstoque = cbEstoqueDisponivel.getValue();
				} 
		if (ckApenasItensAdicionados.isChecked()) {
			produtoFilter.filtraProdutosInseridos = true;
			produtoFilter.nuPedidoFilter = pedido.nuPedido;
			produtoFilter.flOrigemPedidoFilter = pedido.flOrigemPedido;
			}
		prepareCestaProdutoFilter(produtoFilter);
		prepareGrupoProdTipoPedFilter(produtoFilter);
		prepareProdutoMenuCategoriaFilter(produtoFilter);
		if (LavenderePdaConfig.usaFiltroComissao) {
			if (edComissaoMinima.getValueDouble() != 0 && edComissaoMaxima.getValueDouble() != 0 && edComissaoMinima.getValueDouble() > edComissaoMaxima.getValueDouble()) {
				throw new ValidationException(Messages.PRODUTO_LABEL_PERCENTUAL_MIN_SUPERIOR_MAX);
		}
			if (edComissaoMaxima.getValueDouble() > 0) {
				produtoFilter.itemTabelaPreco.filterByVlPctComissaoMaxFilter = true;
				produtoFilter.itemTabelaPreco.vlPctComissaoMaxFilter = edComissaoMaxima.getValueDouble();
		}
			if (edComissaoMinima.getValueDouble() > 0) {
				produtoFilter.itemTabelaPreco.filterByVlPctComissaoMinFilter = true;
				produtoFilter.itemTabelaPreco.vlPctComissaoMinFilter = edComissaoMinima.getValueDouble();
			}
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox()) {
			DescPromocional descPromo = cbDescPromocional.getDescPromocional();
			if (descPromo != null) {
				produtoFilter.nuPromocaoFilter = descPromo.nuPromocao;
				produtoFilter.filterByNuPromocao = true;
				produtoFilter.joinDescPromocional = true;
			} else if (cbDescPromocional.isOpcaoTodosSelecionado()) {
				produtoFilter.joinDescPromocional = true;
			}
		}
		if (ckProdutoPromocional.isChecked() && LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional) {
			produtoFilter.excetoDescPromocional = true;
		}
		if (LavenderePdaConfig.usaPrecoPorUf) {
			produtoFilter.cdUFFilter = pedido.getCliente().dsUfPreco;
		}
			}

	private Vector getListProduto(boolean includeInterfaceFiltros, ProdutoBase produto) throws SQLException {
		Vector listProduto;
		boolean onlyStartString = LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null && btTipoPesquisaEdFiltro.getSelectedIndex() == 1;
		if (includeInterfaceFiltros) {
			listProduto = ProdutoService.getInstance().findProdutos(TermoCorrecaoService.getInstance().getDsTermoCorrigido(dsFiltro), cbTabelaPreco.getValue(), cbFornecedor.getValue(), filterByPrincipioAtivo, filterByAplicacaoProduto, produto, onlyStartString, filterByCodigoProduto);
		} else {
			listProduto = ProdutoService.getInstance().findProdutos("", cbTabelaPreco.getValue(), null, false, false, produto, getItemPedido(), onlyStartString, false);
			}
		return listProduto;
	}

	private void afterList(Vector listProduto, ProdutoBase produtoFilter, boolean includeInterfaceFiltros) throws SQLException {
		if (includeInterfaceFiltros || LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
			loadVlProdutoToList(listProduto);
		}
		loadVlPctComissaoList(listProduto);
		if (LavenderePdaConfig.usaGradeProduto5()) {
			SessionLavenderePda.setUltimoProdutoFilter(produtoFilter);
	}
		}

	// ************ Métodos de controle da dinâmicidade da interface ********************************
	//********** Define se tal campo deve aparecer ou não na interface ******************************
	//******* Tal como ordenação de campos e visibilidades de conjunto de campos ******************************

	private void repaintScreen() throws SQLException {
		repaintScreen(false);
	}

	@Override
	protected void repaintScreen(boolean recarregaInterface) throws SQLException {
		super.repaintScreen(recarregaInterface);
		visibleState();
		updateNuFracaoFornecedor();
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			updateVisibilityBtAlert();
		}
		if (recarregaInterface) {
			removeComponentsInScreen();
			populateHashEditsAndLabels(LavenderePdaConfig.ordemCamposTelaItemPedido);
		}
		changeVisibleValueRentabilidade();
		reposition();
	}

	private void updateNuFracaoFornecedor() throws SQLException {
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			int nuFracaoFornecedor = (getItemPedido() != null && getItemPedido().getProduto() != null) ? getItemPedido().getProduto().nuFracaoFornecedor : 0;
			if (nuFracaoFornecedor > 0) {
				btIconeFracaoFornecedor.setText(MessageUtil.getMessage(Messages.PRODUTO_LABEL_FRACAO_FORNECEDOR, nuFracaoFornecedor));
			} else {
				btIconeFracaoFornecedor.setVisible(false);
			}
		}
		}

	protected void removeComponentsInScreen() throws SQLException {
		// Container Fileds
		if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid() && LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 0) {
			containerInfosProduto.remove(gridUnidadeAlternativa);
		}
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			if ((gridDescCond != null) && LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido) {
				containerInfosProduto.remove(gridDescCond);
			} else if ((gridLoteProduto != null) && (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil  || LavenderePdaConfig.restringeItemPedidoPorLocal)) {
				containerInfosProduto.remove(gridLoteProduto);
			} else if (LavenderePdaConfig.usaSelecaoPorGrid() && gridTabelaPreco != null) {
				containerInfosProduto.remove(gridTabelaPreco);
			} else if (LavenderePdaConfig.isExibeHistoricoProduto()) {
				containerInfosProduto.remove(gridHistTrocaDevProduto);
			}
			if (isShowFotoProduto()) {
				containerInfosProduto.remove(imageCarrousel);
				if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && !(LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil  || LavenderePdaConfig.restringeItemPedidoPorLocal)) {
					containerInfosProduto.remove(containerFotosProdutos);
				}
			}
		}
		Container container = permiteInserirMultiplosItensPorVezComInterfaceNegociacao() && !isItemPedidoGrade() ? containerGrid : containerFields2;
		for (int i = 1; i <= hashEdits.size(); i++) {
			container.remove((Control) hashEdits.get(i));
			container.remove((Control) hashLabels.get(i));
		}
		container.remove(lbDsProduto);
		container.remove(numericPad);
		container.remove(btSep3);
		if (btGradeItemPedido != null) container.remove(btGradeItemPedido);
		if (LavenderePdaConfig.usaGradeProduto5()) container.remove(edVlItemPedido);
	}

	public void addComponentsInScreen() throws SQLException {
		// Container Fileds
		addFields();
		// Container Grid
		if (repaintFilter && !pedido.isSugestaoPedidoGiroProduto()) {
			addFiltros();
		}
		addGridAndFields();
		// InfoCentral
		addInfosExtras();
	}

	@Override
	protected void addInfosExtras() throws SQLException {
		if (LavenderePdaConfig.isExibeFotoTelaItemPedido() && !pedido.isPedidoBonificacao() || fromProdutoPendenteGiroMultInsercao) {
			addFotoProduto();
			if (isShowFotoProduto() && inVendaUnitariaMode) {
				loadFotosSugestaoVenda();
			}
		}
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() || fromProdutoPendenteGiroMultInsercao) {
			if (LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido) {
				addGridDescCond();
			} else if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.restringeItemPedidoPorLocal) {
				addGridDescLoteProduto();
			} else if (LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 2 && !LavenderePdaConfig.isUsaSelecaoUnidadePorGrid()) {
				addGridUnidadeAlternativa();
			} else if (LavenderePdaConfig.usaSelecaoPorGrid()) {
				addGridTabelaPreco();
			} else if (LavenderePdaConfig.isExibeHistoricoProduto()) {
				addGridHistTrocaDevProduto();
			}
		} else if (LavenderePdaConfig.restringeItemPedidoPorLocal) {
			addGridDescLoteProduto();
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid() && LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 0) {
			addGridUnidadeAlternativa();
	}
		UiUtil.add(sessaoTotalizadores, lvQtProdutos, LEFT + listContainer.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
	}

	private void reloadComponents() {
		if (LavenderePdaConfig.isAplicaDescontoFimDoPedido()) {
			if (pedido.isPedidoAberto()) {
				tipPreviaDesc.setText(Messages.TOOLTIP_LABEL_PREVIADESCONTO);
			} else {
				tipPreviaDesc.setText(Messages.TOOLTIP_LABEL_DESCONTO_APLICADO);
			}
		}
		}

	public void addGridDescLoteProduto() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			criaGridLoteProduto();
			if (!isShowFotoProduto()) {
				UiUtil.add(containerInfosProduto, gridLoteProduto, LEFT, containerIconsProduto.getY2(), FILL, FILL - getHeightCbItemGrade1() - getReservedHeightCbItemGrade2());
			} else {
				UiUtil.add(containerInfosProduto, gridLoteProduto, LEFT, imageCarrousel.getY2(), FILL, FILL - getHeightCbItemGrade1() - getReservedHeightCbItemGrade2());
			}
		}
	}

	private void criaGridLoteProduto() {
		if (hashIndexItensGridLote == null) 
			hashIndexItensGridLote = new HashMap<String, Integer>();
		if (gridLoteProduto == null) {
			GridColDefinition[] gridColDefiniton;
			if (MOSTRA_TODOS_CAMPOS_GRID_LOTE.equals(LavenderePdaConfig.configColunasGridLoteProduto)) {
				gridColDefiniton = returnAllFieldsGridLoteProduto();
				setHashIndexGridAllFields();
			} else {
				gridColDefiniton = returnFieldsGridLoteProduto();
			}
			gridLoteProduto = UiUtil.createGridEdit(gridColDefiniton, false);
			gridLoteProduto.setGridControllable();
			gridLoteProduto.disableSort = false;
			gridLoteProduto.setID("gridLoteProduto");

		}
	}

	private void criaGridHistoricoTrocaDevolucaoProd() {
		if (gridHistTrocaDevProduto == null) {
			GridColDefinition[] gridColDefiniton;
			gridColDefiniton = new GridColDefinition[]{
					new GridColDefinition(Messages.HISTORICO_DIAS_SEM_COMPRAS, -10, Container.CENTER),
					new GridColDefinition(Messages.HISTORICO_PERIODO, -30, Container.CENTER),
					new GridColDefinition(Messages.HISTORICO_SEISMESES, -20, Container.CENTER),
					new GridColDefinition(Messages.HISTORICO_TRESMESES, -20, Container.CENTER),
					new GridColDefinition(Messages.HISTORICO_TRINTADIAS, -20, Container.CENTER)};
			gridHistTrocaDevProduto = UiUtil.createGridEdit(gridColDefiniton, false, false);
			gridHistTrocaDevProduto.setGridControllable();
		}
	}

	private void setHashIndexGridAllFields() {
		hashIndexItensGridLote.put(CONFIG_COLUNA_LOCAL_GRID_LOTE, 0);
		hashIndexItensGridLote.put(CONFIG_COLUNA_DESCRICAO_GRID_LOTE, 1);
		hashIndexItensGridLote.put(CONFIG_COLUNA_DATA_VALIDADE_GRID_LOTE, 2);
		hashIndexItensGridLote.put(CONFIG_COLUNA_PERCENTUAL_VIDA_GRID_LOTE, 3);
		hashIndexItensGridLote.put(CONFIG_COLUNA_ESTOQUE_GRID_LOTE, 4);
		hashIndexItensGridLote.put(CONFIG_COLUNA_RESERVA_GRID_LOTE, 5);
		hashIndexItensGridLote.put(CONFIG_COLUNA_PERCENTUAL_DESCONTO_GRID_LOTE, 6);
		hashIndexItensGridLote.put(CONFIG_COLUNA_PRECO_LOTE, 7);
	}

	private GridColDefinition[] returnFieldsGridLoteProduto() {
		List<GridColDefinition> list = new ArrayList<GridColDefinition>();
		String[] configList = LavenderePdaConfig.configColunasGridLoteProduto.split(";");
		int index = 0;
		for (String config : configList) {
			GridColDefinition field = createFieldByVlParameter(config, index);
			if (field == null) continue;
			list.add(field);
			index++;
		}
		return list.toArray(new GridColDefinition[list.size()]);
	}

	private GridColDefinition createFieldByVlParameter(String config, int index) {
		switch (config) {
		case "1":
			hashIndexItensGridLote.put(CONFIG_COLUNA_LOCAL_GRID_LOTE, index);
			return new GridColDefinition(Messages.LABEL_LOCAL, -25 , Container.LEFT);
		case "2":
			hashIndexItensGridLote.put(CONFIG_COLUNA_DESCRICAO_GRID_LOTE, index);
			return new GridColDefinition(Messages.LOTEPRODUTO_LOTE, -25, Container.LEFT);
		case "3":
			hashIndexItensGridLote.put(CONFIG_COLUNA_DATA_VALIDADE_GRID_LOTE, index);
			return new GridColDefinition(Messages.LOTEPRODUTO_VALIDADE, -35, Container.CENTER);
		case "4":
			hashIndexItensGridLote.put(CONFIG_COLUNA_PERCENTUAL_VIDA_GRID_LOTE, index);
			return new GridColDefinition(Messages.LOTEPRODUTO_VIDA, -20, Container.CENTER);
		case "5":
			hashIndexItensGridLote.put(CONFIG_COLUNA_ESTOQUE_GRID_LOTE, index);
			return new GridColDefinition(Messages.ESTOQUE_NOME_ENTIDADE, -20, Container.RIGHT);
		case "6":
			hashIndexItensGridLote.put(CONFIG_COLUNA_RESERVA_GRID_LOTE, index);
			return new GridColDefinition(Messages.LOTEPRODUTO_RESERVA, -20, Container.RIGHT);
		case "7":
			hashIndexItensGridLote.put(CONFIG_COLUNA_PERCENTUAL_DESCONTO_GRID_LOTE, index);
			return new GridColDefinition(Messages.LOTEPRODUTO_DESCONTO, -20, Container.CENTER);
		case "8":
			hashIndexItensGridLote.put(CONFIG_COLUNA_PRECO_LOTE, index);
			return new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -20, Container.RIGHT);
		}
		return null;
	}

	private GridColDefinition[] returnAllFieldsGridLoteProduto() {
		return new GridColDefinition[] {
				new GridColDefinition(Messages.LABEL_LOCAL, LavenderePdaConfig.usaDescPromo ? -25 : 0, Container.LEFT),
				new GridColDefinition(Messages.LOTEPRODUTO_LOTE, -25, Container.LEFT),
				new GridColDefinition(Messages.LOTEPRODUTO_VALIDADE, -35, Container.CENTER),
				new GridColDefinition(Messages.LOTEPRODUTO_VIDA, -20, Container.CENTER),
				new GridColDefinition(Messages.ESTOQUE_NOME_ENTIDADE, -20, Container.RIGHT),
				new GridColDefinition(Messages.LOTEPRODUTO_RESERVA, -20, Container.RIGHT),
				new GridColDefinition(Messages.LOTEPRODUTO_DESCONTO, -20, Container.CENTER),
				new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -20, Container.RIGHT) };
	}
	
	private void addGridTabelaPreco() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			if (gridTabelaPreco == null) {
				GridColDefinition[] gridColDefiniton = {
					new GridColDefinition(Messages.CODIGO, -15, Container.LEFT),	
					new GridColDefinition(Messages.TABELAPRECO_NOME_TABELA, -70, Container.LEFT)};	
				gridTabelaPreco = UiUtil.createGridEdit(gridColDefiniton, false);
				gridTabelaPreco.setGridControllable();
			}
			int height = containerInfosProduto.getHeight();
			if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid() && LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 0) {
				height /= 2;
		}
			UiUtil.add(containerInfosProduto, gridTabelaPreco, LEFT, containerIconsProduto.getY2(), FILL, height - getHeightCbItemGrade1() - getReservedHeightCbItemGrade2());
	}
	}

	private void addGridHistTrocaDevProduto() throws SQLException {
		if (!pedido.isPedidoBonificacao() && possuiHistoricoTrocadevolucao) {
			containerInfosProduto.remove(gridHistTrocaDevProduto);
			int spaceReserved = getHeightCbItemGrade1() + getReservedHeightCbItemGrade2();
			int y2 = isShowFotoProduto() && imageCarrousel.isDisplayed() ? imageCarrousel.getY2() : containerIconsProduto.getY2();
			UiUtil.add(containerInfosProduto, gridHistTrocaDevProduto, LEFT, y2, FILL, FILL - spaceReserved);
		}
	}

	public void addGridDescCond() {
		int spaceReserved = getHeightCbItemGrade1() + getReservedHeightCbItemGrade2();
		if (gridDescCond == null) {
			GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO, -70, Container.CENTER),
				new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -30, Container.CENTER) };
			gridDescCond = UiUtil.createGridEdit(gridColDefiniton, false);
		}
		UiUtil.add(containerInfosProduto, gridDescCond, LEFT, containerIconsProduto.getY2(), FILL, FILL - spaceReserved);
		gridDescCond.reposition();
		gridDescCond.setRect(LEFT, containerIconsProduto.getY2(), FILL, FILL - spaceReserved);
	}

	public void addGridUnidadeAlternativa() throws SQLException {
		if (containerInfosProduto.getHeight() - containerIconsProduto.getY2() > UiUtil.getControlPreferredHeight() * 2) {
			try {
				int spaceReserved = getHeightCbItemGrade1() + getReservedHeightCbItemGrade2();
				int y2 = isShowFotoProduto() && imageCarrousel.isDisplayed() ? imageCarrousel.getY2() : containerIconsProduto.getY2();
				y2 = y2 < containerIconsProduto.getY2() ? containerIconsProduto.getY2() : y2;
				UiUtil.add(containerInfosProduto, gridUnidadeAlternativa, LEFT, y2, FILL, FILL - spaceReserved);
				gridUnidadeAlternativa.reposition();
				gridUnidadeAlternativa.setRect(LEFT, y2, FILL, FILL - spaceReserved);
			} catch (RuntimeException runEx) {
				// A princípio remove a grid por não haver espaço o suficiente na tela
				containerInfosProduto.remove(gridUnidadeAlternativa);
			}
		} else {
			if (gridUnidadeAlternativa != null) {
				containerInfosProduto.remove(gridUnidadeAlternativa);
			}
		}
	}

	public void addFotoProduto() throws SQLException {
		int spaceReserved = getHeightCbItemGrade1() + getReservedHeightCbItemGrade2() + getHeightLbFichaTecnicaItemPedido() + getHeightLbInfosPersonalizadasItemPedido();
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			spaceReserved +=  isModoPaisagem() ? UiUtil.getControlPreferredHeight() * 3 : UiUtil.getControlPreferredHeight() * 4;
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid()) {
			spaceReserved += (LavenderePdaConfig.getUsaSelecaoUnidadePorGrid() + 1) * UiUtil.getControlPreferredHeight();
		}
		if (LavenderePdaConfig.isExibeHistoricoProduto() && gridHistTrocaDevProduto.isVisible()) {
			spaceReserved += 3 * UiUtil.getControlPreferredHeight();
		}
		if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && !(LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.restringeItemPedidoPorLocal)) {
			int heightSugestaoFotos = containerInfosProduto.getHeight() / 3;
			if (heightSugestaoFotos > UiUtil.getLabelPreferredHeight() * 2 && !isEditing()) {
				UiUtil.add(containerInfosProduto, containerFotosProdutos, LEFT, BOTTOM - spaceReserved, FILL, heightSugestaoFotos);
				int gapContainer = containerFotosProdutos.getHeight() / 13;
				int widthContainer = containerInfosProduto.getWidth() / 3 - (gapContainer * 2 / 3);
				if (fotosProdSugList.size() < 3) {
					widthContainer = containerInfosProduto.getWidth() / fotosProdSugList.size() - (gapContainer * 2 / 3);
				}
				for (int i = 0; i < fotosProdSugList.size(); i++) {
					UiUtil.add(containerFotosProdutos, (ImageCarrousel)fotosProdSugList.items[i], i == 0 ? LEFT : AFTER + gapContainer, CENTER, widthContainer, containerFotosProdutos.getHeight() - (gapContainer * 2));
				}
				spaceReserved += containerFotosProdutos.getHeight();
			} else {
				containerInfosProduto.remove(containerFotosProdutos);
			}
		}
		if (isShowFotoProdutoSlider()) {
			spaceReserved += getHeightFotoProdutoSlider();
		}
		if (imageCarrousel == null) {
			createImageCarrousel();
		}
		try {
			addImageCarrouselContainers(spaceReserved);
		} catch (Throwable ex) {
					containerInfosProduto.remove(imageCarrousel);
				}
			}
	
	private void addFiltros() throws SQLException {
		int yFiltrosTop = TOP + HEIGHT_GAP;
		//--
		boolean showAllFiltros = LavenderePdaConfig.isTodosFiltrosFixosTelaItemPedido();
		boolean exibeFiltroProdutoPromocional = false;
    	if (!LavenderePdaConfig.isTodosFiltrosNaTelaAvancadoItemPedido()) {
    		LavenderePdaConfig.carregaMapFiltrosProdutos(filtrosVisiveisMap, filtrosNaoAutomaticosMap, 1);
    		//--
    		boolean exibeFiltroFornecedor = LavenderePdaConfig.usaFiltroFornecedor && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_FORNECEDOR) != null);
    		boolean exibeFiltroGrupoProduto = LavenderePdaConfig.usaFiltroGrupoProduto != 0 && (showAllFiltros || (filtrosVisiveisMap.get(NU_FILTRO_GRUPOPRODUTO) != null));
    		boolean exibeFiltroAtributoProduto = LavenderePdaConfig.usaFiltroAtributoProduto && (showAllFiltros || (filtrosVisiveisMap.get(NU_FILTRO_ATRIBUTO_PRODUTO) != null));
    		boolean exibeFiltroAvisoPreAlta = LavenderePdaConfig.usaAvisoPreAlta && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_AVISO_PRE_ALTA) != null);
    		boolean exibeFiltroApenasKit = LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_APENAS_KIT) != null);
    		boolean exibeFiltroKitProduto = LavenderePdaConfig.isUsaKitProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_KIT) != null);
    		boolean exibeFiltroCampanhaVendas = LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_CAMPANHA_VENDAS) != null);
    		exibeFiltroProdutoPromocional = LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_PROMOCIONAL) != null);
    		exibeFiltroProdutoPromocional &= !LavenderePdaConfig.usaDescPromocionalRegraPoliticaDesconto();
    		boolean exibeFiltroProdutoDescPromocional = LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() || (!LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_DESC_PROMOCIONAL) != null);
    		exibeFiltroProdutoDescPromocional &= !LavenderePdaConfig.usaDescPromocionalRegraPoliticaDesconto();
    		boolean exibeFiltroProdutoGrupoDestaque = LavenderePdaConfig.isUsaGrupoDestaqueProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_GRUPO_DESTAQUE) != null);
    		boolean exibeFiltroPacote = LavenderePdaConfig.usaDescQuantidadePorPacote && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_CBPACOTE) != null);
    		boolean exibeFiltroProdutoOportunidade = LavenderePdaConfig.usaOportunidadeVenda && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_OPORTUNIDADE) != null && !pedido.isOportunidade());
    		boolean exibeFiltroApenasProdutoVendidoMesCorrente = LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE) != null);
    		boolean exibeFiltroGrupoDescProd = LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_GRUPO_DESCONTO_PRODUTO) != null);
    		boolean exibeFiltroDescPromocional = LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_DESC_PROMOCIONAL) != null); 
    		boolean exibeFiltroTipoRanking = LavenderePdaConfig.isUsaOrdenacaoRankingProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_TIPO_RANKING) != null);
    		boolean exibeFiltroComissao = LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_COMISSAO) != null);
    		boolean exibeFiltroLocal = exibeComboLocal() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_LOCAL) != null);
    		boolean exibeFiltroDescMaxProdCli = LavenderePdaConfig.usaDescMaxProdCli && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_DESCMAX_PRODUTO_CLIENTE) != null);
		    boolean exibeFiltroDescProgressivoPersonalizado = LavenderePdaConfig.usaDescProgressivoPersonalizado && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_DESC_PROGRESSIVO_PERSONALIZADO) != null);
		    boolean exibeFiltroEstoqueDisponivel = LavenderePdaConfig.isUsaFiltroEstoqueDisponivelTelaItemPedido() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_ESTOQUE_DISPONIVEL) != null);
		    boolean exibeFiltroItensInseridos = showAllFiltros ||  filtrosVisiveisMap.get(NU_FILTRO_ITENS_INSERIDOS) != null;
			boolean exibeFiltroMarcador = LavenderePdaConfig.apresentaMarcadorProdutoInsercao && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_CBMARCADOR) != null);
			boolean exibeFiltroProdutoComDescQtd = LavenderePdaConfig.apresentaFiltroDescQtd && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_COM_DESCQTD) != null);
    		//--
    		if (exibeFiltroFornecedor) {
				if (LavenderePdaConfig.exibeOpcoesNavegacaoNasCombos) {
					UiUtil.add(containerGrid, lbFornecedor, getLeft(), yFiltrosTop - HEIGHT_GAP_BIG, FILL, UiUtil.getControlPreferredHeight());
					UiUtil.add(containerGrid, btFornecedorAnterior, getLeft(), SAME + lbFornecedor.getPreferredHeight() + HEIGHT_GAP_BIG, PREFERRED);
					UiUtil.add(containerGrid, btFornecedorProximo, getRight(), SAME, PREFERRED);
					UiUtil.add(containerGrid, cbFornecedor, btFornecedorAnterior.getX2() + WIDTH_GAP, SAME, FIT - WIDTH_GAP, UiUtil.getControlPreferredHeight());
				} else {
					UiUtil.add(containerGrid, lbFornecedor, cbFornecedor, getLeft(), yFiltrosTop);
				}
				if (ValueUtil.isNotEmpty(pedido.cdFornecedorDefault)) {
					cbFornecedor.setValue(pedido.cdFornecedorDefault);
					pedido.cdFornecedorDefault = null;
				} else if (cbFornecedor.getSelectedIndex() == -1) {
					cbFornecedor.setSelectedIndex(0);
				}
       			yFiltrosTop = AFTER + HEIGHT_GAP;
			}
    		
    		if (exibeFiltroGrupoProduto) {
				loadGrupoProdutos(pedido);
            	if (LavenderePdaConfig.exibeOpcoesNavegacaoNasCombos && !LavenderePdaConfig.ocultaGrupoProduto1) {
            		UiUtil.add(containerGrid, lbGrupoProduto, getLeft(), yFiltrosTop - HEIGHT_GAP_BIG, FILL, UiUtil.getControlPreferredHeight());
					UiUtil.add(containerGrid, btGrupoProdutoAnterior, getLeft(), SAME + lbGrupoProduto.getPreferredHeight() + HEIGHT_GAP_BIG, PREFERRED);
					UiUtil.add(containerGrid, btGrupoProdutoProximo, getRight(), SAME, PREFERRED);
					UiUtil.add(containerGrid, cbGrupoProduto1, btGrupoProdutoAnterior.getX2() + WIDTH_GAP, SAME, FIT - WIDTH_GAP, UiUtil.getControlPreferredHeight());
            	} else {
            		UiUtil.add(containerGrid, lbGrupoProduto, getLeft(), yFiltrosTop);
            		if (!LavenderePdaConfig.ocultaGrupoProduto1) {
           			UiUtil.add(containerGrid, cbGrupoProduto1, getLeft(), AFTER);
            	}
				}
            	if (cbGrupoProduto1.getSelectedIndex() == -1) {
            		cbGrupoProduto1.setSelectedIndex(0);
				}
       			yFiltrosTop = AFTER + HEIGHT_GAP;
       			//--
    			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
           			UiUtil.add(containerGrid, cbGrupoProduto2, getLeft(), yFiltrosTop);
                	if (cbGrupoProduto2.getSelectedIndex() == -1) {
                		cbGrupoProduto2.setSelectedIndex(0);
    				}
    			}
    			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
           			UiUtil.add(containerGrid, cbGrupoProduto3, getLeft(), yFiltrosTop);
                	if (cbGrupoProduto3.getSelectedIndex() == -1) {
                		cbGrupoProduto3.setSelectedIndex(0);
    				}
    			}
    			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
    				UiUtil.add(containerGrid, cbGrupoProduto4, getLeft(), yFiltrosTop);
                	if (cbGrupoProduto4.getSelectedIndex() == -1) {
                		cbGrupoProduto4.setSelectedIndex(0);
    				}
    			}
    		}
			
        	if (exibeFiltroGrupoDescProd) {
        		UiUtil.add(containerGrid, lbGrupoDescProd, getLeft(), yFiltrosTop);
        		UiUtil.add(containerGrid, cbGrupoDescProd, getLeft(), AFTER);
        		yFiltrosTop = AFTER + HEIGHT_GAP;
        	}
			if (exibeFiltroKitProduto) {
				UiUtil.add(containerGrid, lbKit, cbKit, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroAtributoProduto) {
    			UiUtil.add(containerGrid, lbAtributoProduto, getLeft(), yFiltrosTop, PREFERRED, PREFERRED);
    			UiUtil.add(containerGrid, cbAtributoProd, getLeft(), AFTER, width / 2);
				UiUtil.add(containerGrid, cbAtributoOpcaoProd, AFTER + WIDTH_GAP, SAME, getWFill());
				reloadAtributoProd();
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroCampanhaVendas) {
				UiUtil.add(containerGrid, lbCesta, getLeft(), yFiltrosTop, PREFERRED, PREFERRED);
				UiUtil.add(containerGrid, cbCesta, getLeft(), AFTER, width / 2);
				UiUtil.add(containerGrid, cbNaoPositivado, AFTER + WIDTH_GAP, SAME, getWFill());
				setCbNaoPositivadoEnable();
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroProdutoGrupoDestaque) {
				UiUtil.add(containerGrid, lbProdutoGrupoDestaque, cbProdutoGrupoDestaque, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroPacote) {
				UiUtil.add(containerGrid, lbPacote, cbPacote, getLeft(), yFiltrosTop);
				cbPacote.setSelectedIndex(0);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroDescPromocional) {
				UiUtil.add(containerGrid, lbDescPromocional, cbDescPromocional, getLeft(), yFiltrosTop);
				cbDescPromocional.setSelectedIndex(0);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroAvisoPreAlta) {
				UiUtil.add(containerGrid, ckPreAltaProduto, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroApenasKit) {
				UiUtil.add(containerGrid, ckApenasKit, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroProdutoPromocional && !LavenderePdaConfig.isUsaFiltraProdPromoAposCampoBusca()) {
				UiUtil.add(containerGrid, ckProdutoPromocional, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroProdutoDescPromocional) {
				UiUtil.add(containerGrid, ckProdutoDescPromocional, getLeft(), yFiltrosTop, getWFill());
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroLocal) {
				UiUtil.add(containerGrid, new LabelName(Messages.LABEL_LOCAL), cbLocal, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroProdutoOportunidade) {
				UiUtil.add(containerGrid, ckProdutoOportunidade, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroApenasProdutoVendidoMesCorrente) {
				UiUtil.add(containerGrid, ckApenasProdutoVendido, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroDescMaxProdCli) {
				UiUtil.add(containerGrid, ckMaxDescProdCli, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroComissao) {
				UiUtil.add(containerGrid, lbComissaoMinima, getLeft(), yFiltrosTop);
				UiUtil.add(containerGrid, lbComissaoMaxima, AFTER + lbComissaoMinima.getWidth() + WIDTH_GAP, SAME);
				yFiltrosTop = AFTER + HEIGHT_GAP;
				UiUtil.add(containerGrid, edComissaoMinima, getLeft(), yFiltrosTop - HEIGHT_GAP, PREFERRED + lbComissaoMinima.getWidth() - WIDTH_GAP);
				UiUtil.add(containerGrid, edComissaoMaxima, AFTER + WIDTH_GAP , SAME);
			}
			if (exibeFiltroDescProgressivoPersonalizado) {
				UiUtil.add(containerGrid, new LabelName(Messages.DESC_PROG_CONFIG_NM_ENTIDADE), cbDescProgressivoConfig, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
				UiUtil.add(containerGrid, new LabelName(Messages.DESC_PROG_CONFIG_TIPOCONFIGFAM), cbDescProgConfigFam, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
				UiUtil.add(containerGrid, new LabelName(Messages.DESC_PROG_FAMILIA_PRODUTOS_TITLE), cbDescProgFamilia, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroProdutoComDescQtd) {
				UiUtil.add(containerGrid, ckProdutoDescQtd, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
		}
			if (exibeFiltroEstoqueDisponivel) {
				UiUtil.add(containerGrid, new LabelName(Messages.ESTOQUE_COMBO_TITULO), cbEstoqueDisponivel, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroItensInseridos) {
				UiUtil.add(containerGrid, ckApenasItensAdicionados, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}
			if (exibeFiltroMarcador) {
				UiUtil.add(containerGrid, new LabelName(Messages.MARCADOR_MARCADORES), cbMarcador, getLeft(), yFiltrosTop);
				yFiltrosTop = AFTER + HEIGHT_GAP;
			}

		}
    	//--
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			UiUtil.add(containerGrid, lbUnidade, cbUnidade, getLeft(), yFiltrosTop);
			yFiltrosTop = AFTER + HEIGHT_GAP;
		}
		if (LavenderePdaConfig.usaCategoriaInsercaoItem() && !menuCategoriaStarted) {
			int widthBotaoVoltar = ButtonMenuCategoria.FIXED_BT_VOLTAR_HEIGHT + fm.stringWidth(FrameworkMessages.BOTAO_VOLTAR) + WIDTH_GAP_BIG;
			UiUtil.add(containerGrid, dynamicButtonMenuCategoria, getLeft(), AFTER + HEIGHT_GAP, getWFill(), ButtonMenuCategoria.FIXED_BT_VOLTAR_HEIGHT);
			UiUtil.add(containerGrid, lbCurrentCategoria, RIGHT - WIDTH_GAP_BIG, SAME);
			lbCurrentCategoria.setVisible(false);
			lvCurrentCategoria.setVisible(false);
			UiUtil.add(containerGrid, lvCurrentCategoria, widthBotaoVoltar + WIDTH_GAP_BIG + WIDTH_GAP, AFTER - HEIGHT_GAP, getWFill() - WIDTH_GAP);
			UiUtil.add(containerGrid, menuCategoriaScroll, getLeft() - WIDTH_GAP_BIG, lvCurrentCategoria.getY2(), getWFill(), ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * LavenderePdaConfig.getQtdCategoriaPadrao());
			dynamicButtonMenuCategoria.defaultBtVoltarWidth = widthBotaoVoltar;
			if (buttonMenuCategoriaTree == null) {
				loadMenuCategoriaInicial();
			}
			yFiltrosTop = AFTER + HEIGHT_GAP;
		}
		int filtroEndGap = UiUtil.getFillRightSpace();
    	if (!showAllFiltros) {
			UiUtil.add(containerGrid, btFiltroAvancado, RIGHT - UiUtil.getFillRightSpace(), yFiltrosTop, PREFERRED);
			filtroEndGap += UiUtil.getControlPreferredHeight() + WIDTH_GAP;
			yFiltrosTop = SAME;
    	}
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado() || LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			UiUtil.add(containerGrid, btGroupTipoFiltros, getLeft(), yFiltrosTop, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerGrid, edFiltro, AFTER + WIDTH_GAP, SAME, FILL - filtroEndGap, UiUtil.getControlPreferredHeight());
		} else if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null) {
			UiUtil.add(containerGrid, btTipoPesquisaEdFiltro, getLeft(), yFiltrosTop, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerGrid, edFiltro, AFTER + WIDTH_GAP, SAME, FILL - filtroEndGap, UiUtil.getControlPreferredHeight());
		} else {
			UiUtil.add(containerGrid, edFiltro, getLeft(), yFiltrosTop, FILL - filtroEndGap, UiUtil.getControlPreferredHeight());
		}
		if (exibeFiltroProdutoPromocional && LavenderePdaConfig.isUsaFiltraProdPromoAposCampoBusca()) {
			yFiltrosTop = AFTER + HEIGHT_GAP;
			UiUtil.add(containerGrid, ckProdutoPromocional, getLeft(), yFiltrosTop);
		}
	}

	private boolean exibeComboLocal() {
		return LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal;
	}

	@Override
	public void addGridAndFields() throws SQLException {
		Vector filtrosVisiveis = new Vector(StringUtil.split(LavenderePdaConfig.filtrosFixoTelaItemPedido, ';'));
		boolean exibeFiltroProdutoPromocional = !LavenderePdaConfig.isTodosFiltrosNaTelaAvancadoItemPedido() && LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido() && (LavenderePdaConfig.isTodosFiltrosFixosTelaItemPedido() || (filtrosVisiveis.indexOf(NU_FILTRO_PRODUTO_PROMOCIONAL) != -1)) && LavenderePdaConfig.isUsaFiltraProdPromoAposCampoBusca();
		int y = exibeFiltroProdutoPromocional ? ckProdutoPromocional.getY2() : edFiltro.getY2();
		if (!inVendaUnitariaMode) {
			if (permiteInserirMultiplosItensPorVezComInterfaceNegociacao()) {
				addEditsVenda(containerGrid, BOTTOM - HEIGHT_GAP);
				UiUtil.add(containerGrid, listContainer, LEFT, BEFORE - HEIGHT_GAP, FILL, btSep3.getY() - (y + (HEIGHT_GAP * 2)));
			} else {
				UiUtil.add(containerGrid, listContainer, LEFT, y + (HEIGHT_GAP * 2), FILL, FILL - containerTotalizadoresMultIns.getHeight());
			}
		}
	}
	
	protected void addIconsProduto() throws SQLException {
		int xIcons = LEFT + WIDTH_GAP_BIG;
		if (!containerInfosProduto.isDisplayed()) {
			return;
		}
		containerIconsProduto.removeAll();
		UiUtil.add(containerInfosProduto, containerIconsProduto, LEFT, TOP, FILL, heigthContainerIcons);
		if (LavenderePdaConfig.isMostraFotoProduto() && btIconeFoto.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeFoto, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}

		if (LavenderePdaConfig.isUsaConfigVideosProdutos() || LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			UiUtil.add(containerIconsProduto, btIconeVideos, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}

		if ((LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex || LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) && btIconeVerba.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeVerba, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}

		if (LavenderePdaConfig.isMostraConfigTelaInfoProduto() && (!isModoGrade(true) || LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade())) {
			UiUtil.add(containerIconsProduto, btIconeDetalheProduto, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}

		if (LavenderePdaConfig.mostraInfoEstoqueUnidades) {
			UiUtil.add(containerIconsProduto, btIconeInfoEstoqueUnidades, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}

		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem() && pedido.isStatusPedidoNaoOcultaValoresComissao() && btIconeComissaoItemPedido.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeComissaoItemPedido, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if ((LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) && btIconeBonificacao.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeBonificacao, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && btIconeKit.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeKit, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto() && btIconeGrupoDestaque.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeGrupoDestaque, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (isPermiteUsarOportunidade() && btIconeOportunidade.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeOportunidade, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			UiUtil.add(containerIconsProduto, btIconeProdCreditoDesc, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.isOcultaInfoRentabilidadeManualmente()) {
			UiUtil.add(containerIconsProduto, btIconeOcultaInfoRentabilidade, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() && isShowFotoProdutoSlider()) {
			UiUtil.add(containerIconsProduto, btIconeCarrosselPopup, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}

		if (!isFiltrandoAgrupadorGrade() || LavenderePdaConfig.usaCarrosselProdutosGradeDetalheItem) {
			if (!LavenderePdaConfig.usaConfigMargemRentabilidade() && LavenderePdaConfig.isUsaRentabilidadeNoPedido()
					&& (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco)) {
			UiUtil.add(containerIconsProduto, btIconeRentabilidade, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
				xIcons = AFTER + WIDTH_GAP_BIG;
			} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
				UiUtil.add(containerIconsProduto, btIconeRentabilidade, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
				xIcons = AFTER + WIDTH_GAP_BIG;
			} else if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto
					&& LavenderePdaConfig.isUsaSolicitacaoAutorizacao()
					&& !LavenderePdaConfig.isIgnoraAgrupadorSimilaridade()) {
				UiUtil.add(containerIconsProduto, btIconeSimilar, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
				xIcons = AFTER + WIDTH_GAP_BIG;
		}
		}
		if (LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade() && isModoGrade()) {
			UiUtil.add(containerIconsProduto, btOcultaMostraEditLabels, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		//Deve ser mantido no final, pois seu posicionamento é a direita da tela, Usar xPosRightIcon para manter a ordem dos botões à direita!
		int xPosNextRightIcon = RIGHT - WIDTH_GAP_BIG;
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && btIconeAutorizacao.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeAutorizacao, xPosNextRightIcon, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xPosNextRightIcon = BEFORE - WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.mostraSeloPontuacaoItem) {
			btVlPesoProdutoPontuacao.setVisible(getItemPedidoService().possuiPontuacaoProduto(getItemPedido()));
			if(btVlPesoProdutoPontuacao.isVisible()) {
				tipPontuacaoBase = new BaseToolTip(btVlPesoProdutoPontuacao, Messages.PESO_ITEM_PONTUACAO_DESTAQUE);
				UiUtil.add(containerIconsProduto, btVlPesoProdutoPontuacao, xPosNextRightIcon, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
				xPosNextRightIcon = BEFORE - WIDTH_GAP_BIG;
			}
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			UiUtil.add(containerIconsProduto, btIconeFracaoFornecedor, xPosNextRightIcon, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xPosNextRightIcon = BEFORE - WIDTH_GAP_BIG;
		}
	}

	private boolean isConfirmItemAbaixoRentMinima(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) < RentabilidadeFaixaService.getInstance().getVlPctRentabilidadeFaixaMinima(itemPedido.pedido.getRentabilidadeFaixaList())) {
			return UiUtil.showConfirmYesNoMessage(Messages.RENTABILIDADEFAIXA_MSG_ITEM_RENTABILIDADE_MINIMA);
		}
		return true;
	}
	
	private void menuProdutoLoteClick() throws SQLException {
		Produto produto = getItemPedido().getProduto();
		produto = produto == null ? getSelectedProduto() : produto;
		if (produto == null) {
			UiUtil.showErrorMessage(Messages.PRODUTO_NENHUM_SELECIONADO);
			return;
		}
		if (!ProdutoService.getInstance().isExistsProduto(getItemPedido())) {
			UiUtil.showErrorMessage(Messages.PRODUTO_ERRO_NAO_DISPONIVEL);
			return;
		}
		String cdLocal = getCdLocal();
		RelProdutoLotesWindow relCondicaoWindow = new RelProdutoLotesWindow(produto, cdLocal, false);
		relCondicaoWindow.popup();
	}

	private String getCdLocal() throws SQLException {
		if (exibeComboLocal()) {
			if (isEditing()) {
				return getItemPedido().cdLocal;
			}
			return cbLocal.getValue();
		}
		return null;
	}
	
	public void rentabilidadeClick() {
		RelRentabilidadeSaldoWindow verbaForm = new RelRentabilidadeSaldoWindow(pedido);
		verbaForm.popup();
	}

	public void verbaClick() throws SQLException {
		if (pedido.isPedidoCritico()) {
			UiUtil.showWarnMessage(Messages.PEDIDOCRITICO_AVISO_VERBA_GRUPO);
			return;
		}
		if (pedido.isTipoFreteFob() && LavenderePdaConfig.usaValidaConversaoFOB()) {
			UiUtil.showWarnMessage(Messages.PEDIDO_FOB_AVISO_VERBA_GRUPO);
			return;
		}
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			new RelVerbaPersonalizadaWindow(getItemPedido()).popup();
		} else if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			RelVerbaFornecedorSaldoWindow relVerbaFornecedorSaldoWindow = new RelVerbaFornecedorSaldoWindow(pedido, getItemPedido());
			relVerbaFornecedorSaldoWindow.popup();
		} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
			RelVerbaUsuarioSaldoWindow verbaUsuarioSaldoWindow = new RelVerbaUsuarioSaldoWindow(pedido, getItemPedido());
			verbaUsuarioSaldoWindow.popup();
		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			RelVerbaGrupoSaldoWindow verbaGrupoSaldoWindow = new RelVerbaGrupoSaldoWindow(pedido, getItemPedido());
			verbaGrupoSaldoWindow.popup();
		} else {
			if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
				RelVerbaSaldoWindow verbaForm = new RelVerbaSaldoWindow(pedido);
				verbaForm.popup();
			} else {
				ListVerbaSaldoForm listVerbaSaldoForm = new ListVerbaSaldoForm(true);
				show(listVerbaSaldoForm);
			}
		}
	}

	public void btHistoricoItemClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			UiUtil.showProcessingMessage();
			try {
				RelHistoricoItensPedidoWindow historicoItensPedidoForm = new RelHistoricoItensPedidoWindow((ItemPedido)screenToDomain(), pedido);
				historicoItensPedidoForm.popup();
			} catch (ValidationException e) {
				UiUtil.showInfoMessage(e.getMessage());
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}
	
	public void btUltimosPedidosClick() throws SQLException {
		SessionLavenderePda.pedidoConsultaUltimosPedidos = pedido;
		SessionLavenderePda.cadItemPedidoFormConsultaUltimosPedidosInstance = this;
		ListPedidoForm listPedidoForm = new ListPedidoForm(pedido);
		listPedidoForm.inConsultaUltimosPedidos = true;
		show(listPedidoForm);
	}

	public void kitProdutosClick() throws SQLException {
		if (!isEditing() || LavenderePdaConfig.isUsaKitTipo3()) {
			ListItemKitForm listItemKitForm = new ListItemKitForm(pedido);
			listItemKitForm.setCadItemPedidoForm(this);
			listItemKitForm.editing = isEditing();
			show(listItemKitForm);
		} else {
			UiUtil.showErrorMessage(Messages.KIT_MSG_ERRO);
		}
	}
	
	public void btGiroProdutoClick(boolean fromBtNovoItemOrFechamentoPed) throws SQLException {
		if (LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido() && !inVendaUnitariaMode && !fromBtNovoItemOrFechamentoPed) {
			voltarClick();
			cadPedidoForm.btGiroProdutoClick();
		} else {
			this.fromBtNovoItemOrFechamentoPed = fromBtNovoItemOrFechamentoPed;
			ListGiroProdutoWindow listGiroProdutoWindow = new ListGiroProdutoWindow(this, cadPedidoForm, pedido, fromBtNovoItemOrFechamentoPed, false, false);
			if (!fromBtNovoItemOrFechamentoPed || listGiroProdutoWindow.hasGiroProduto) {
				listGiroProdutoWindow.popup();
			}
		}
	}

	protected void btRelVendasProdCliClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			RelVendasProdutoPorClienteWindow rel = new RelVendasProdutoPorClienteWindow(getItemPedido().cdProduto);
			rel.popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	protected void btEstoqueGradeClick() throws SQLException {
		ListEstoqueGradeWindow listProdutoSimilarWindow = new ListEstoqueGradeWindow(getItemPedido(), new Vector(), false, null);
		listProdutoSimilarWindow.popup();
	}
	
	protected void btDescQtdClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.getProduto() != null) {
			if (!gridClickDescontoQuantidade(itemPedido, false, false)) {
				return;
			}
			changeItemTabelaPreco();
			itemPedido.oldVlPctFaixaDescQtd = 0;
			if (!LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			refreshProdutoToScreen(itemPedido);
			}
			//--
			if (itemPedido.getQtItemFisico() != 0) {
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
				calcularClick();
			}
			//--
			setFocusInQtde();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}
	
	protected void btDescPromocionalPorQtdeClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.getProduto() != null) {
			if (!gridClickDescPromocionalPorQtde(false, false)) {
				return;
			}
			refreshProdutoToScreen(itemPedido);
			if (itemPedido.getQtItemFisico() != 0) {
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
				calcularClick();
			}
			setFocusInQtde();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	public void btPrecoCondicaoClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			RelPrecosPorCondicaoWindow relCondicaoForm = new RelPrecosPorCondicaoWindow(getItemPedido(), pedido.getCliente(), null, false);
			relCondicaoForm.popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(getEntityDescription());
		}
	}
	
	protected void btCondComercialClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			RelPrecosPorCondicaoComercialWindow relCondicaoForm = new RelPrecosPorCondicaoComercialWindow(getItemPedido().getProduto(), pedido.getCliente());
			relCondicaoForm.popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(getEntityDescription());
		}
	}
	
	public void showDescontoPorTabela() throws SQLException {
		if (LavenderePdaConfig.isOcultaDescTabela()) return;
	
		if (LavenderePdaConfig.naoPermiteInserirQtdDescMultipla() || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
		if (getItemPedido().getProduto() != null) {
			if (LavenderePdaConfig.mostraDescontosPorTabelaPreco) {
				try {
					RelDescontoPorTabelaPrecoWindow relDescontoTabelaForm = new RelDescontoPorTabelaPrecoWindow(getItemPedido().getProduto(), cbTabelaPreco.getItems(), pedido);
					relDescontoTabelaForm.popup();
					//--
					ItemTabelaPreco itemTabelaPreco = relDescontoTabelaForm.itemTabelaPreco;
					if (itemTabelaPreco != null) {
						cbTabelaPreco.setValue(itemTabelaPreco.cdTabelaPreco);
						tabelaPrecoChange();
						//--
						ItemPedido itemPedido = getItemPedido();
						itemPedido.vlPctDesconto = itemTabelaPreco.getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
						edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
						calculoRapidoDescontos(itemPedido);
					}
					} catch (Throwable ex) {
					UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.MSG_VALOR_TELADESCONTOPORTABELAPRECO_MENOR_QUE_PERMITIDO, LavenderePdaConfig.percAlturaTelaDescontoTabelaPreco));
					if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && !isEditing() && !inVendaUnitariaMode) {
							ItemPedido itemPedido = getItemPedido();
							refreshDomainToScreen(itemPedido);
							controlEstoque(getItemPedido());
					}
				}
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(getEntityDescription());
		}
	}
	}

	public boolean showRelComissaoPorTabelaPreco() throws SQLException {
		if (!pedido.isPedidoBonificacao() && !pedido.isPedidoTroca() && LavenderePdaConfig.exibeRelatorioComissaoAoSelecionarItem && !pedido.tipoPedidoIgnoraCalculoComissao()) {
			Vector tabelaPrecoList = TabelaPrecoService.getInstance().loadTabelasPrecos(pedido, true);
			if (ValueUtil.isEmpty(tabelaPrecoList) || tabelaPrecoList.size() == 1) {
				return false;
			}
			RelTabelaPrecoComissaoWindow relTabelaPrecoWindow = new RelTabelaPrecoComissaoWindow(tabelaPrecoList, getItemPedido().getProduto(), pedido);
			relTabelaPrecoWindow.popup();
			if (ValueUtil.isNotEmpty(relTabelaPrecoWindow.cdTabelaPreco) && !relTabelaPrecoWindow.cdTabelaPreco.equals(pedido.cdTabelaPreco)) {
				changeItemTabelaPreco(relTabelaPrecoWindow.cdTabelaPreco);
			}
			return true;
		}
		return false;
	}

	protected void showDescComissaoList(boolean chamadaPeloMenu) throws SQLException {
		boolean showDescComiGrupo = false;
		if (getItemPedido().getProduto() != null) {
			if (LavenderePdaConfig.usaDescontoComissaoPorGrupo) {
				ListDescComiGrupoWindow list = new ListDescComiGrupoWindow(pedido, getItemPedido());
				if (list.gridSize > 0) {
					list.lvNuItensGrupo.setValue(GrupoProduto1Service.getInstance().getQtItensByGrupoProdutoNoPedido(pedido, getItemPedido().getProduto().cdGrupoProduto1));
					list.popup();
					showDescComiGrupo = true;
				}
				DescComiFaixaService.getInstance().aplicaDescComiGrupoNoItemPedido(getItemPedido(), list.descComiGrupo, list.selecionouItemGrid);
				edVlPctDesconto.setValue(getItemPedido().vlPctDesconto);
				edVlPctComissao.setValue(getItemPedido().vlPctComissao);
				calculoRapidoDescontos(getItemPedido());
			}
			if (LavenderePdaConfig.usaDescontoComissaoPorProduto && !showDescComiGrupo) {
				ListDescComiProdWindow list = new ListDescComiProdWindow(pedido, getItemPedido());
				if (list.gridSize > 0) {
					list.popup();
					showDescComiGrupo = true;
				}
				DescComiFaixaService.getInstance().aplicaDescComiProdNoItemPedido(getItemPedido(), list.descComiProd, list.selecionouItemGrid);
				edVlPctDesconto.setValue(getItemPedido().vlPctDesconto);
				edVlPctComissao.setValue(getItemPedido().vlPctComissao);
				calculoRapidoDescontos(getItemPedido());
			}
			if (chamadaPeloMenu && !showDescComiGrupo && (LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.usaDescontoComissaoPorGrupo)) {
				UiUtil.showInfoMessage(Messages.DESCCOMIPROD_SEM_DESC);
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	protected void showDescontoGrupoList() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.getProduto() == null) {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
				return;
			}
		if (!gridClickDescontoQtdeGrupoProduto(itemPedido, false, false) || !selectDesconto || !DescontoGrupoService.getInstance().hasDescontoGrupoProduto(itemPedido)) return;
		refreshValuesAfterSelectDesconto(itemPedido);
	}

	protected void showDescontoPacoteList() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.getProduto() == null) {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
			return;
		}
		if (!gridClickDescontoPacote(itemPedido, false, false) || !selectDescontoPacote || !DescontoPacoteService.getInstance().hasDescPacoteInItemPedido(itemPedido, itemPedido.cdPacote, cbTabelaPreco.getValue())) return;
		refreshValuesAfterSelectDesconto(itemPedido);
	}

	private void refreshValuesAfterSelectDesconto(ItemPedido itemPedido) throws SQLException {
				changeItemTabelaPreco();
				refreshProdutoToScreen(itemPedido);
				calculoRapidoDescontos(itemPedido);
				setFocusInQtde();
			}

	protected void btDesbloquearLimitador(Pedido pedido) throws SQLException {
		AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
		senhaForm.setMensagem(Messages.PEDIDO_MSG_DESBLOQUEIA_LIMITADOR);
		senhaForm.setCdCliente(pedido.getCliente().cdCliente);
		senhaForm.setChaveSemente(SenhaDinamica.SENHA_DESBLOQUEAR_LIMITADOR);
		if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			pedido.flMaxVendaLiberadoSenha = ValueUtil.VALOR_SIM;
		}
	}

	protected void btLiberarPrecoClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			ItemPedido itemPedido = getItemPedido();
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(Messages.PEDIDO_MSG_LIBERA_PRECO);
			senhaForm.setCdCliente(pedido.getCliente().cdCliente);
			senhaForm.setCdProduto(itemPedido.cdProduto);
			senhaForm.setQtdeProduto(edQtItemFisico.getValueDouble());
			if (isPermiteAlterarVlItemNaUnidadeElementar()) {
				senhaForm.setVlProduto(edPrecoEmbPrimaria.getValueDouble());
			} else {
				senhaForm.setVlProduto(edVlItemPedido.getValueDouble());
			}
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_PRECO_PRODUTO);
			if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				UiUtil.showMessage(Messages.PEDIDO_MSG_LIBERA_PRECO_SUCESSO,
						  Messages.ITEMPEDIDO_LABEL_QTITEMFISICO + ".: " + StringUtil.getStringValueToInterface(senhaForm.edQtdProduto.getValueDouble()) + " , " +
						  Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO + ": " + StringUtil.getStringValueToInterface(senhaForm.edValorProduto.getValueDouble()), TYPE_MESSAGE.SUCCESS);
				itemPedido.qtItemMinAfterLibPreco = senhaForm.edQtdProduto.getValueDouble();
				itemPedido.vlItemMinAfterLibPreco = senhaForm.edValorProduto.getValueDouble();
				itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_SIM;
				if (senhaForm.editouValoresProduto) {
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
				}
				edQtItemFisico.setValue(senhaForm.edQtdProduto.getValueDouble());
				if (isPermiteAlterarVlItemNaUnidadeElementar()) {
					edPrecoEmbPrimaria.setValue(senhaForm.edValorProduto.getValueDouble());
				} else {
					edVlItemPedido.setValue(senhaForm.edValorProduto.getValueDouble());
				}
				calcularClick();
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}
	
	protected void btLiberarPrecoBaseadoPercentualUsuarioEscolhido() throws SQLException {
		if (getItemPedido().getProduto() != null && isPermiteLiberarPrecoItemPedido(getItemPedido())) {
			ItemPedido itemPedido = getItemPedido();
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(Messages.PEDIDO_MSG_LIBERA_PRECO);
			senhaForm.setCdCliente(pedido.getCliente().cdCliente);
			senhaForm.setCdProduto(itemPedido.cdProduto);
			senhaForm.setQtdeProduto(edQtItemFisico.getValueDouble());
			senhaForm.setVlProduto(edVlItemPedido.getValueDouble());
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_BASEADO_PERCENTUAL_USUARIO_ESCOLHIDO);
			if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				if (isItemPedidoPromocional(itemPedido) && senhaForm.edValorProduto.getValueDouble() < itemPedido.vlBaseItemPedido) {
					UiUtil.showErrorMessage(itemPedido.getProduto().isEspecial() ? Messages.PEDIDO_MSG_LIBERACAO_PRECO_PRODUTO_ESPECIAL_NAO_PERMITIDO_USUARIO : Messages.PEDIDO_MSG_LIBERACAO_PRECO_PRODUTO_PROMOCIONAL_NAO_PERMITIDO_USUARIO);
				} else if (UsuarioRelRepService.getInstance().isDescontoPermitidoParaUsuarioSelecionado(itemPedido, senhaForm.cdUsuarioLiberado, senhaForm.edValorProduto.getValueDouble())) {
					UiUtil.showSucessMessage(Messages.PEDIDO_MSG_LIBERA_PRECO_SUCESSO + "\n" + 
									Messages.ITEMPEDIDO_LABEL_QTITEMFISICO + ".: " + StringUtil.getStringValueToInterface(senhaForm.edQtdProduto.getValueDouble()) + " , " +
									Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO + ": " + StringUtil.getStringValueToInterface(senhaForm.edValorProduto.getValueDouble()));
					itemPedido.qtItemAfterLibPreco = senhaForm.edQtdProduto.getValueDouble();
					itemPedido.vlItemAfterLibPreco = senhaForm.edValorProduto.getValueDouble();
					itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_SIM;
					itemPedido.cdUsuarioLiberacao = senhaForm.cdUsuarioLiberado;
					edQtItemFisico.setValue(senhaForm.edQtdProduto.getValueDouble());
					edVlItemPedido.setValue(senhaForm.edValorProduto.getValueDouble());
					itemPedido.flEditandoQtItemFaturamento = false;
					calcularClick();
				} else {
					UiUtil.showErrorMessage(Messages.PEDIDO_MSG_LIBERACAO_PRECO_NAO_PERMITIDO_USUARIO);
				}
			}
		}
	}

	private boolean isPermiteLiberarPrecoItemPedido(ItemPedido itemPedido) throws SQLException {
		boolean retorno = true;
		if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido && itemPedido.vlPctDesconto != 0 && itemPedido.vlPctDesconto > 0) {
			retorno = !isItemPedidoPromocional(itemPedido);
		}
		if (!retorno) {
			UiUtil.showErrorMessage(itemPedido.getProduto().isEspecial() ? Messages.PEDIDO_MSG_LIBERACAO_PRECO_PRODUTO_ESPECIAL_NAO_PERMITIDO_USUARIO : Messages.PEDIDO_MSG_LIBERACAO_PRECO_PRODUTO_PROMOCIONAL_NAO_PERMITIDO_USUARIO);
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && itemPedido.qtdCreditoDesc > 0 && ValueUtil.isNotEmpty(itemPedido.flTipoCadastroItem)) {
			retorno = false;
			UiUtil.showErrorMessage(ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(itemPedido.flTipoCadastroItem) ? Messages.PRODUTOCREDITODESCONTO_ERRO_LIB_SENHA_QTD : Messages.PRODUTOCREDITODESCONTO_ERRO_LIB_SENHA_DESC);
		}
		return retorno;
	}
	
	private boolean isItemPedidoPromocional(ItemPedido itemPedido) throws SQLException {
		itemPedido.loadDescPromocional(false);
		return DescPromocionalService.getInstance().isDescPromocionalEncontrado(itemPedido) || (itemPedido.getProduto() != null && itemPedido.getProduto().isEspecial());
	}

	public void btSubstituicaoTributariaClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			new RelSubstituicaoTributariaWindow(pedido, getItemPedido(), isEditing()).popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}
	
	public void btPesquisaMercadoClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido != null && ValueUtil.isNotEmpty(itemPedido.cdProduto)) {
			CadPesquisaMercadoForm cadPesquisaForm = new CadPesquisaMercadoForm(itemPedido.cdProduto, pedido.nuPedido);
			cadPesquisaForm.add();
			show(cadPesquisaForm);
		} else {
			ListPesquisaMercadoForm listPesquisaForm = new ListPesquisaMercadoForm(pedido.nuPedido);
			show(listPesquisaForm);
		}
	}

	public void btPesquisaMercadoProdutoConcorrenteClick() throws SQLException {
		ListPesquisaMercadoConfigForm listPesquisaMercadoConfigForm = new ListPesquisaMercadoConfigForm(pedido.nuPedido);
		show(listPesquisaMercadoConfigForm);
	}

	public void showHistoricoItem() throws SQLException {
		//Verifica se existe historico do produto nos ultimos pedidos do cliente e se existir exibe o mesmo
		if (LavenderePdaConfig.usaHistoricoItemPedido && !LavenderePdaConfig.ocultaHistoricoItemPedidoAutomatico) {
			RelHistoricoItensPedidoWindow historicoItensPedidoForm = new RelHistoricoItensPedidoWindow(getItemPedido(), pedido);
			try {
				historicoItensPedidoForm.popup();
			} catch (ValidationException e) {
				// não faz nada, apenas não exibe a mensagem
			}
		}
	}


	public void cbAtributoProdChange() throws SQLException {
		cbAtributoOpcaoProd.load(cbAtributoProd.getValue());
		cbAtributoOpcaoProd.setSelectedIndex(0);
		//--
		changeAtributoProdEnable();
	}

	public void changeAtributoProdEnable() {
		cbAtributoOpcaoProd.setEnabled(false);
		edFiltro.setEnabled(true);
		btFiltroAvancado.setEnabled(true);
		if (ValueUtil.isNotEmpty(cbAtributoProd.getValue())) {
			cbAtributoOpcaoProd.setEnabled(true);
			if (ValueUtil.isEmpty(cbAtributoOpcaoProd.getValue())) {
				edFiltro.setEnabled(false);
				btFiltroAvancado.setEnabled(false);
			}
		}
		if (edFiltro.isEnabled()) {
			setFocusInFiltro();
		}
	}

	protected boolean verificaProdutoSimilar(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaSimilarVendaProduto && !listProdutoSimilarInicialized) {
			double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
			if (qtEstoque <= 0) {
				String uf = SessionLavenderePda.getCliente().dsUfPreco;
				Produto produto = itemPedido.getProduto();
				ListProdutoSimilarWindow listProdutoSimilarWindow = new ListProdutoSimilarWindow(produto, false, cbTabelaPreco.getValue(), uf, itemPedido);
				if (listProdutoSimilarWindow.possuiProdutoSimilares() && (!LavenderePdaConfig.usaAgrupadorProdutoSimilar || ValueUtil.isNotEmpty(produto.cdAgrupProdSimilar))) {
					if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
						itemPedidoInfoAdic = (ItemPedido)itemPedido.clone();
					}
					listProdutoSimilarWindow.setCadItemPedidoForm(this);
					listProdutoSimilarWindow.popup();
				}
				return listProdutoSimilarWindow.isSelectedItem();
			}
		}
		return false;
	}
	
	protected void btInfoExtraItemPedidoClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			InfoExtraItemPedidoWindow infoExtraItemPedidoForm = new InfoExtraItemPedidoWindow(getItemPedido(), pedido, cadPedidoForm);
			infoExtraItemPedidoForm.popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	protected void btProdutosSimilares(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaSimilarVendaProduto && !listProdutoSimilarInicialized) {
			Produto produto = itemPedido.getProduto();
			if (produto != null) {
				String uf = SessionLavenderePda.getCliente().dsUfPreco;
				ListProdutoSimilarWindow listProdutoSimilarWindow = new ListProdutoSimilarWindow(produto, false, cbTabelaPreco.getValue(), uf, itemPedido);
				if (listProdutoSimilarWindow.possuiProdutoSimilares() && (!LavenderePdaConfig.usaAgrupadorProdutoSimilar || ValueUtil.isNotEmpty(produto.cdAgrupProdSimilar))) {
					firstCarrouselValidation = false;
					if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
						itemPedidoInfoAdic = (ItemPedido)itemPedido.clone();
					}
					listProdutoSimilarWindow.setCadItemPedidoForm(this);
					listProdutoSimilarWindow.popup();
				} else {
					UiUtil.showInfoMessage(Messages.PRODUTO_MSG_NAO_POSSUI_PRODUTOS_SIMILARES);
				}
			} else {
				UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
			}
		}
	}

	public void btFiltroAvancadoClick() throws SQLException {
		FiltroProdutoAvancadoWindow filtroForm = new FiltroProdutoAvancadoWindow(pedido, cbTabelaPreco.getValue());
		//-- Transfere dados da tela principal para o filtro avancado
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao && ValueUtil.isNotEmpty(cbMarcador.getSelectedMarcador())) {
			filtroForm.cbMarcador.setSelectItens(cbMarcador.getSelectedMarcador());
		}
		if (LavenderePdaConfig.usaFiltroFornecedor && (!cbFornecedor.isValueSelectedEmpty())) {
			filtroForm.cbFornecedor.setValue(cbFornecedor.getValue());
		}
		if (LavenderePdaConfig.usaFiltroAtributoProduto && (ValueUtil.isNotEmpty(cbAtributoProd.getValue()))) {
			filtroForm.cbAtributoProd.setValue(cbAtributoProd.getValue());
			filtroForm.cbAtributoOpcaoProd.load(cbAtributoProd.getValue());
			filtroForm.cbAtributoOpcaoProd.setValue(cbAtributoOpcaoProd.getValue());
		}
		if (filterByPrincipioAtivo && LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			filtroForm.filterByPrincipioAtivo = true;
			filtroForm.filterByAplicacaoProduto = false;
			filtroForm.filterByCodigoProduto = false;
			filtroForm.btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_PA));
		} else if (filterByAplicacaoProduto && LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
			filtroForm.filterByPrincipioAtivo = false;
			filtroForm.filterByAplicacaoProduto = true;
			filtroForm.filterByCodigoProduto = false;
			filtroForm.btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_AP));
		} else if(LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			filtroForm.filterByPrincipioAtivo = false;
			filtroForm.filterByAplicacaoProduto = false;
			filtroForm.filterByCodigoProduto = true;
			filtroForm.btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_CD));
		} else {
			filtroForm.filterByPrincipioAtivo = false;
			filtroForm.filterByAplicacaoProduto = false;
			filtroForm.filterByCodigoProduto = false;
			filtroForm.btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_DS));
		}
		filtroForm.alterarTipoTeclado();
		if (LavenderePdaConfig.usaFiltroGrupoProduto != 0) {
			if (ValueUtil.isNotEmpty(cbGrupoProduto1.getValue())) {
				filtroForm.cbGrupoProduto1.setValue(cbGrupoProduto1.getValue());
				filtroForm.cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
				filtroForm.cbGrupoProduto2.setSelectedIndex(0);
			} else {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor && cbFornecedor.getSelectedItem() != null) {
						Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
						cbGrupoProduto1.loadGrupoProduto1(pedido, fornecedorSelecionado);
					} else {
						cbGrupoProduto1.loadGrupoProduto1(pedido);
			}
					cbGrupoProduto1.setSelectedIndex(0);
				}
			}
			if (ValueUtil.isNotEmpty(cbGrupoProduto2.getValue())) {
				filtroForm.cbGrupoProduto2.setValue(cbGrupoProduto2.getValue());
				filtroForm.cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
				filtroForm.cbGrupoProduto3.setSelectedIndex(0);
			}
			if (ValueUtil.isNotEmpty(cbGrupoProduto3.getValue())) {
				filtroForm.cbGrupoProduto3.setValue(cbGrupoProduto3.getValue());
				filtroForm.cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
				filtroForm.cbGrupoProduto4.setSelectedIndex(0);
			}
			if (ValueUtil.isNotEmpty(cbGrupoProduto4.getValue())) {
				filtroForm.cbGrupoProduto4.setValue(cbGrupoProduto4.getValue());
			}
		}
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
			filtroForm.cbGrupoProduto1.loadGrupoProduto1(pedido, (Fornecedor) cbFornecedor.getSelectedItem());
			if (cbGrupoProduto1.getSelectedItem() != null) {
				filtroForm.cbGrupoProduto1.setSelectedItem(cbGrupoProduto1.getSelectedItem());
			} else {
				filtroForm.cbGrupoProduto1.setSelectedIndex(0);
			}
		}
		if (LavenderePdaConfig.isUsaKitProduto() && (cbKit.getValue() != null)) {
			filtroForm.cbKit.setValue(cbKit.getKit());
		}
		if (LavenderePdaConfig.usaAvisoPreAlta) {
			filtroForm.ckPreAltaProduto.setChecked(ckPreAltaProduto.isChecked());
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			filtroForm.ckProdutoKit.setChecked(ckApenasKit.isChecked());
		}
		if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido()) {
			filtroForm.ckProdutoPromocional.setChecked(ckProdutoPromocional.isChecked());
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() || (!LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional())) {
			filtroForm.ckProdutoDescPromocional.setChecked(ckProdutoDescPromocional.isChecked());
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			filtroForm.ckProdutoOportunidade.setChecked(ckProdutoOportunidade.isChecked());
		}
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
			filtroForm.cbCesta.setValue(cbCesta.getValue());
			filtroForm.setCbNaoPositivadoEnable();
			filtroForm.cbNaoPositivado.setValue(cbNaoPositivado.getValue());
		}
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			filtroForm.cbProdutoGrupoDestaque.setSelectItens(cbProdutoGrupoDestaque.getValue());
		}
		if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			filtroForm.cbPacote.setValue(cbPacote.getValue());
		}
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
			filtroForm.ckProdutoVendido.setChecked(ckApenasProdutoVendido.isChecked());
		}
		if (exibeComboLocal()) {
			filtroForm.cbLocal.setEnabled(cbLocal.isEnabled());
			filtroForm.cbLocal.setSelectedIndex(cbLocal.getSelectedIndex());
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox()) {
			if (cbDescPromocional.getDescPromocional() != null) {
				filtroForm.cbDescPromocional.setValue(cbDescPromocional.getDescPromocional());
			} else if (FrameworkMessages.OPCAO_TODOS.equalsIgnoreCase(String.valueOf(cbDescPromocional.getSelectedItem()))) {
				filtroForm.cbDescPromocional.setSelectedIndex(1);
			}
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			filtroForm.cbDescProgressivoConfig.setSelectedIndex(cbDescProgressivoConfig.getSelectedIndex());
			if (filtroForm.cbDescProgressivoConfig.getValue() != null) {
				filtroForm.cbDescProgConfigFam.load(true);
				filtroForm.cbDescProgConfigFam.setSelectedIndex(cbDescProgConfigFam.getSelectedIndex());
				if (filtroForm.cbDescProgConfigFam.getSelectedItem() != null) {
					boolean tipoConsome = ValueUtil.valueEquals(filtroForm.cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_CONSOME);
					boolean tipoProduz = ValueUtil.valueEquals(filtroForm.cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_PRODUZ);
					boolean tipoAcumula = ValueUtil.valueEquals(filtroForm.cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_ACUMULA);
					boolean tipoAcumulaMax = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_ACUMULAMAX);
					filtroForm.cbDescProgFamilia.load(filtroForm.cbDescProgressivoConfig.getValue(), tipoConsome, tipoProduz, tipoAcumula, tipoAcumulaMax);
				} else {
					filtroForm.cbDescProgFamilia.load(filtroForm.cbDescProgressivoConfig.getValue(), true, true, true, true);
		}
				filtroForm.cbDescProgFamilia.setSelectedIndex(cbDescProgFamilia.getSelectedIndex());
			} else {
				filtroForm.cbDescProgConfigFam.removeAll();
				filtroForm.cbDescProgFamilia.removeAll();
			}
		}
		if (LavenderePdaConfig.isUsaFiltroEstoqueDisponivelTelaItemPedido()) {
			filtroForm.cbEstoqueDisponivel.setSelectItens(cbEstoqueDisponivel.getValue());
		}
		if (LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			if (edComissaoMinima.getValueDouble() != 0) {
				filtroForm.edComissaoMinima.setValue(edComissaoMinima.getValueDouble());
			}
			if (edComissaoMaxima.getValueDouble() != 0) {
				filtroForm.edComissaoMaxima.setValue(edComissaoMaxima.getValueDouble());
			}
		}
		
		filtroForm.ckApenasItensAdicionados.setChecked(ckApenasItensAdicionados.isChecked());
		filtroForm.ckDescMaxProdCli.setChecked(LavenderePdaConfig.usaDescMaxProdCli && ckMaxDescProdCli.isChecked());
		filtroForm.ckProdutoDescQtd.setChecked(ckProdutoDescQtd.isChecked());
		filtroForm.edFiltro.setValue(edFiltro.getValue());
		//-- Exibe janela de filtro avançado
		filtroForm.popup();
		//-- Transfere dados do filtro avancado para a janela principal
		if (filtroForm.filtroRealizado) {
			edFiltro.setValue(filtroForm.edFiltro.getValue());
			if (LavenderePdaConfig.usaFiltroFornecedor) {
				if (!filtroForm.cbFornecedor.isValueSelectedEmpty()) {
					cbFornecedor.setValue(filtroForm.cbFornecedor.getValue());
				} else {
					cbFornecedor.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.usaFiltroAtributoProduto) {
				if (ValueUtil.isNotEmpty(filtroForm.cbAtributoProd.getValue())) {
					cbAtributoProd.setValue(filtroForm.cbAtributoProd.getValue());
					if (ValueUtil.isNotEmpty(cbAtributoProd.getValue())) {
						cbAtributoOpcaoProd.load(cbAtributoProd.getValue());
						cbAtributoOpcaoProd.setValue(filtroForm.cbAtributoOpcaoProd.getValue());
					}
				} else {
					cbAtributoProd.setSelectedIndex(0);
					cbAtributoOpcaoProd.setSelectedIndex(0);
				}
				changeAtributoProdEnable();
			}
			if (filtroForm.filterByPrincipioAtivo && LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
				filterByPrincipioAtivo = true;
				filterByAplicacaoProduto = false;
				filterByCodigoProduto = false;
				btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_PA));
			} else if (filtroForm.filterByAplicacaoProduto && LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()) {
				filterByPrincipioAtivo = false;
				filterByAplicacaoProduto = true;
				filterByCodigoProduto = false;
				btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_AP));
			} else if (filtroForm.filterByCodigoProduto && LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
				filterByPrincipioAtivo = false;
				filterByAplicacaoProduto = false;
				filterByCodigoProduto = true;
				btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_CD));
			} else {
				filterByPrincipioAtivo = false;
				filterByAplicacaoProduto = false;
				filterByCodigoProduto = false;
				btGroupTipoFiltros.setSelectedIndex(posicoesbtGroupTipoFiltros.get(HASHKEY_DS));
			}
			alterarTipoTeclado();
			if (LavenderePdaConfig.usaFiltroGrupoProduto != 0) {
				if (ValueUtil.isNotEmpty(filtroForm.cbGrupoProduto1.getValue())) {
					cbGrupoProduto1.setValue(filtroForm.cbGrupoProduto1.getValue());
				} else {
					cbGrupoProduto1.setSelectedIndex(0);
				}
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
				if (ValueUtil.isNotEmpty(filtroForm.cbGrupoProduto2.getValue())) {
					cbGrupoProduto2.setValue(filtroForm.cbGrupoProduto2.getValue());
				} else {
					cbGrupoProduto2.setSelectedIndex(0);
				}
				cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
				if (ValueUtil.isNotEmpty(filtroForm.cbGrupoProduto3.getValue())) {
					cbGrupoProduto3.setValue(filtroForm.cbGrupoProduto3.getValue());
				} else {
					cbGrupoProduto3.setSelectedIndex(0);
				}
				cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
				if (ValueUtil.isNotEmpty(filtroForm.cbGrupoProduto4.getValue())) {
					cbGrupoProduto4.setValue(filtroForm.cbGrupoProduto4.getValue());
				} else {
					cbGrupoProduto4.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.isUsaKitProduto()) {
				if (filtroForm.cbKit.getValue() != null) {
					cbKit.setValue(filtroForm.cbKit.getKit());
				} else {
					cbKit.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.usaAvisoPreAlta) {
				ckPreAltaProduto.setChecked(filtroForm.ckPreAltaProduto.isChecked());
			}
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
				ckApenasKit.setChecked(filtroForm.ckProdutoKit.isChecked());
			}
			if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido()) {
				ckProdutoPromocional.setChecked(filtroForm.ckProdutoPromocional.isChecked());
			}
			if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() || LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
				ckProdutoDescPromocional.setChecked(filtroForm.ckProdutoDescPromocional.isChecked());
			}
			if (LavenderePdaConfig.usaDescPromo) {
				cbLocal.setSelectedIndex(filtroForm.cbLocal.getSelectedIndex());
				cbLocal.setEnabled(filtroForm.cbLocal.isEnabled());
			}
			if (LavenderePdaConfig.usaOportunidadeVenda) {
				ckProdutoOportunidade.setChecked(filtroForm.ckProdutoOportunidade.isChecked());
			}
			if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
				ckApenasProdutoVendido.setChecked(filtroForm.ckProdutoVendido.isChecked());
			}
			if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
				if (ValueUtil.isNotEmpty(filtroForm.cbCesta.getValue())) {
					cbCesta.setValue(filtroForm.cbCesta.getValue());
					cbCestaClick();
					cbNaoPositivado.setValue(filtroForm.cbNaoPositivado.getValue());
				} else {
					cbCesta.setSelectedIndex(0);
					cbCestaClick();
					cbNaoPositivado.setSelectedIndex(0);
				}
			}

			if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
				if (filtroForm.cbProdutoGrupoDestaque != null) {
					cbProdutoGrupoDestaque.setSelectItens(filtroForm.cbProdutoGrupoDestaque.getValue());
				}
			}
			if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
				if (filtroForm.cbPacote != null && filtroForm.cbPacote.getValue() != null) {
					cbPacote.setValue(filtroForm.cbPacote.getValue());
				} else {
					cbPacote.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox()) {
				if (filtroForm.cbDescPromocional.getDescPromocional() != null) {
					cbDescPromocional.setValue(filtroForm.cbDescPromocional.getDescPromocional());
				} else if (FrameworkMessages.OPCAO_TODOS.equalsIgnoreCase(String.valueOf(filtroForm.cbDescPromocional.getSelectedItem()))) {
					cbDescPromocional.setSelectedIndex(1);
				} else {
					cbDescPromocional.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
				edComissaoMinima.setValue(filtroForm.edComissaoMinima.getValueDouble());
				edComissaoMaxima.setValue(filtroForm.edComissaoMaxima.getValueDouble());
				}
			if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
				cbGrupoDescProd.setValue(filtroForm.cbGrupoDescProd.getValue());
			}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
				cbDescProgressivoConfig.setSelectedIndex(filtroForm.cbDescProgressivoConfig.getSelectedIndex());
				cbDescProgConfigFam.load(true);
				cbDescProgConfigFam.setSelectedIndex(filtroForm.cbDescProgConfigFam.getSelectedIndex());
				boolean tipoConsome = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_CONSOME);
				boolean tipoProduz = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_PRODUZ);
				boolean tipoAcumula = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_ACUMULA);
				boolean tipoAcumulaMax = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), TipoDescProgConfigFamComboBox.FL_ACUMULAMAX);
				cbDescProgFamilia.load(cbDescProgressivoConfig.getValue(), tipoConsome, tipoProduz, tipoAcumula, tipoAcumulaMax);
				cbDescProgFamilia.setSelectedIndex(filtroForm.cbDescProgFamilia.getSelectedIndex());
			}
			ckMaxDescProdCli.setChecked(LavenderePdaConfig.usaDescMaxProdCli && filtroForm.ckDescMaxProdCli.isChecked());
			if (LavenderePdaConfig.isUsaFiltroEstoqueDisponivelTelaItemPedido()) {
				cbEstoqueDisponivel.setSelectItens(filtroForm.cbEstoqueDisponivel.getValue());
			}
			ckApenasItensAdicionados.setChecked(filtroForm.ckApenasItensAdicionados.isChecked());
			if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
				cbMarcador.setSelectItens(filtroForm.cbMarcador.getSelectedMarcador());
			}
			if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
				cbGrupoDescProd.setSelectedItem(filtroForm.cbGrupoDescProd.getSelectedItem());
			}
			if (LavenderePdaConfig.apresentaFiltroDescQtd) {
				ckProdutoDescQtd.setChecked(filtroForm.ckProdutoDescQtd.isChecked());
			}
			filtrarClick(btFiltroAvancado, null);
		}
		btFiltroAvancado.setImage(UiUtil.getColorfulImage(destacaBotaoFiltro() ? "images/maisfiltrosativo.png" : "images/maisfiltros.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
	}

	private boolean destacaBotaoFiltro() {
		return ckApenasKit.isChecked() ||
		ckPreAltaProduto.isChecked() ||
		ckProdutoPromocional.isChecked() ||
		ckProdutoDescPromocional.isChecked() ||
		ckProdutoOportunidade.isChecked() ||
		ckApenasProdutoVendido.isChecked() ||
		ckProdutoDescQtd.isChecked() ||
		ValueUtil.isNotEmpty(edFiltro.getValue()) ||
		ValueUtil.isNotEmpty(cbKit.getValue()) ||
		ValueUtil.isNotEmpty(cbCesta.getValue()) ||
		ValueUtil.isNotEmpty(cbNaoPositivado.getValue()) ||
		ValueUtil.isNotEmpty(cbGrupoProduto1.getValue()) ||
		ValueUtil.isNotEmpty(cbGrupoProduto2.getValue()) ||
		ValueUtil.isNotEmpty(cbGrupoProduto3.getValue()) ||
		ValueUtil.isNotEmpty(cbGrupoProduto4.getValue()) ||
		ValueUtil.isNotEmpty(cbFornecedor.getValue()) ||
		ValueUtil.isNotEmpty(cbAtributoProd.getValue()) ||
		ValueUtil.isNotEmpty(cbAtributoOpcaoProd.getValue()) ||
		(LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && cbDescPromocional.getDescPromocional() != null);
				//|| cbTipoRanking.getValue() != 0;
	}

	protected boolean isPrevaleceFiltroAtributo() {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.ignoraFiltroAutoFornecedorPorAtributoProduto)) {
			String[] params = StringUtil.split(LavenderePdaConfig.ignoraFiltroAutoFornecedorPorAtributoProduto, ';');
			String cdAtributo;
			for (int i = 0; i < params.length; i++) {
				cdAtributo = params[i];
				if (cdAtributo.equals(StringUtil.getStringValue(cbAtributoProd.getValue()))) {
					return true;
				}
			}
		}
		cbAtributoProd.setSelectedIndex(0);
		cbAtributoOpcaoProd.setSelectedIndex(0);
		return false;
	}

	public void btLiberarPrecoProdutoBloqueadoClick(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		MetasPorGrupoProduto metasPorGrupoProdutoFilter = new MetasPorGrupoProduto();
		metasPorGrupoProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		metasPorGrupoProdutoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		metasPorGrupoProdutoFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		Vector metaList = MetasPorGrupoProdutoService.getInstance().findAllMetasVigentes(metasPorGrupoProdutoFilter);
		metaList = ItemPedidoService.getInstance().removeMetasQueNaoSeAplicam(metaList, itemPedido.getProduto());
		if (ValueUtil.isNotEmpty(metaList)) {
			int size = metaList.size();
			int nuNivelMetaGrupoProduto;
			int nuNivelGrupoProdutoNoProduto = ItemPedidoService.getInstance().nuNivelGrupoProdutoNoProduto(itemPedido.getProduto());
			int count = 0;
			for (int i = 0; i < size; i++) {
				MetasPorGrupoProduto metasPorGrupoProdutoRemove = (MetasPorGrupoProduto) metaList.items[count];
				count++;
				nuNivelMetaGrupoProduto = ItemPedidoService.getInstance().nuNivelMetaGrupoProduto(metasPorGrupoProdutoRemove);
				if (nuNivelMetaGrupoProduto > nuNivelGrupoProdutoNoProduto) {
					metaList.removeElement(metasPorGrupoProdutoRemove);
					count--;
				}
			}
			MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) metaList.items[0];
			size = metaList.size();
			for (int i = 0; i < size; i++) {
				MetasPorGrupoProduto metasPorGrupoProdutoAux = (MetasPorGrupoProduto) metaList.items[i];
				if (metasPorGrupoProdutoAux.qtUnidadeMeta < metasPorGrupoProduto.qtUnidadeMeta) {
					metasPorGrupoProduto = metasPorGrupoProdutoAux;
				}
			}
			if (pedido.metaPorGrupoProdHash != null && pedido.metaPorGrupoProdHash.get(metasPorGrupoProduto.getRowKey()) != null) {
				metasPorGrupoProduto = (MetasPorGrupoProduto) pedido.metaPorGrupoProdHash.get(metasPorGrupoProduto.getRowKey());
			}
			PedidoService.getInstance().findItemPedidoList(pedido);
			Vector itemPedidoList = new Vector();
			int sizeAux = pedido.itemPedidoList.size();
			for (int i = 0; i < sizeAux; i++) {
				itemPedidoList.addElement(pedido.itemPedidoList.items[i]);
			}
			itemPedidoList.removeElement(itemPedido);
			//--
			double vlRealizadoItensPedido = ItemPedidoService.getInstance().calculaRealizadoItensPedido(metasPorGrupoProduto, ItemPedidoService.getInstance().nuNivelMetaGrupoProduto(metasPorGrupoProduto), itemPedidoList);
			double qtPesoExcedente = ItemPedidoService.getInstance().calculaRealizadoOutrasMetas(pedido.metaPorGrupoProdHash, metasPorGrupoProduto) + (metasPorGrupoProduto.vlRealizadoPedidosPda + metasPorGrupoProduto.vlRealizado +
							(ItemPedidoService.getInstance().getPesoItemPedido(itemPedido)) + vlRealizadoItensPedido - (metasPorGrupoProduto.qtUnidadeMeta + metasPorGrupoProduto.vlSaldoLiberadoSenha));
			if (qtPesoExcedente < 0) {
				qtPesoExcedente = 0;
			}
			//--
			if (metasPorGrupoProduto != null) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(Messages.SENHADINAMICA_TITULO_PRODUTO_BLOQ);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_PRODUTO_BLOQUEADO_VENDA);
				senhaForm.setCdGrupoProduto1(metasPorGrupoProduto.cdGrupoProduto1);
				senhaForm.setCdGrupoProduto2(metasPorGrupoProduto.cdGrupoProduto2);
				senhaForm.setCdGrupoProduto3(metasPorGrupoProduto.cdGrupoProduto3);
				senhaForm.setPesoExcedente(qtPesoExcedente);
				if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					metasPorGrupoProduto.vlSaldoLiberadoSenha = senhaForm.vlPesoLiberado;
					pedido.metaPorGrupoProdHash.put(metasPorGrupoProduto.getRowKey(), metasPorGrupoProduto);
					itemPedido.flMetaGrupoProdLiberadoSenha = ValueUtil.VALOR_SIM;
				}
			}
		} else {
			ItemPedidoService.getInstance().validaBloqueioGrupos(itemPedido);
		}
	}
	
	protected void addItensOnButtonMenu() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.pedido = pedido;
		addItensOnButtonMenu(itemPedido);
	}
	
	private boolean usaMultiplasOuPersonalizavelSugestaoInicioPedido() {
		return LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido() || LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido();
	}

	private boolean usaBotoesPos1Pos4Pos5() throws SQLException {
		return LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido && renderBotaoCalcular() && isUtilizaBtLeitorCameraPos5();
	}

	private boolean mostraSugestoesMenuOpcoes() throws SQLException {
		return  usaMultiplasOuPersonalizavelSugestaoInicioPedido() && usaBotoesPos1Pos4Pos5() && !renderBotaoInfosComplementares() && !inVendaUnitariaMode;
	}

	protected void addItensOnButtonMenu(ItemPedido itemPedido) throws SQLException {
		boolean inPedidoAberto = pedido.isPedidoAbertoEditavel();
		boolean isEditing = isEditing();
		btOpcoes.removeAll();
		// --
		if (getItemPedido().getProduto() != null || pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			// --Info Produto
			if (!isModoGrade()) {
			btOpcoes.addItemAt(Messages.PRODUTO_TITULO_CADASTRO, 0);
			}
			// --Principio ativo
			if (LavenderePdaConfig.usaSugestaoVendaProdutoMesmoPrincipioAtivo && inPedidoAberto && !isEditing) {
				btOpcoes.addItemAt(Messages.PRODUTO_LABEL_PRINCIPIOATIVO, 1);
			}
			// --Bonificação
			if (ItemPedidoService.getInstance().isPermiteBonificarApenasProdutosInseridosNoPedido(itemPedido) &&
				ItemPedidoService.getInstance().isQtdPermiteBonificar(itemPedido) &&
				ItemPedidoService.getInstance().isPermiteBonificarItem(itemPedido) &&
				!getItemPedido().isItemBonificacao() && pedido.isPedidoAberto() && pedido.isPedidoBonificacao() &&
				!isModoGrade()) {
				btOpcoes.addItemAt(Messages.BOTAO_BONIFICAR, 2);
			}
			// --Historico
			if (LavenderePdaConfig.usaHistoricoItemPedido && !isFiltrandoAgrupadorGrade()) {
				btOpcoes.addItemAt(Messages.ITEMPEDIDO_LABEL_HISTORICO, 4);
			}
			// --Kit
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
				btOpcoes.addItemAt(Messages.PRODUTOKIT_TITULO_CADASTRO, 5);
			}
			// --Libera com senha preço produto
			if (LavenderePdaConfig.liberaComSenhaPrecoProduto && !LavenderePdaConfig.usaSolicitacaoAutorizacaoPorNegociacaoDePreco() && inPedidoAberto) {
				btOpcoes.addItemAt(Messages.PARAMETRO_LIBERACOMSENHAPRECOVENDA, 6);
			}
			// --Preço por condição pagamento
			if (LavenderePdaConfig.isMostraPrecosPorCondicaoPagamento() && inPedidoAberto) {
				btOpcoes.addItemAt(Messages.ITEM_PRECOS_POR_CONDICAO, 7);
			}
			// --Preço por condição comercial
			if (LavenderePdaConfig.usaCondicaoComercialPedido && inPedidoAberto && !LavenderePdaConfig.isOcultaPrecoCondComercial()) {
				btOpcoes.addItemAt(Messages.PRODUTO_LABEL_PRECO_CONDCOMERCIAL, 8);
			}
			// --Relatorio vendas produto
			if (LavenderePdaConfig.relVendasProdutoPorCliente && !isModoGrade()) {
				btOpcoes.addItemAt(Messages.ITEMPEDIDO_LABEL_RELVENPRODPORCLI, 8);
			}
			// --Desconto quantidade
			if ((LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido || LavenderePdaConfig.mostraVolumeVendasMensalNoDescontoQuantidade) && inPedidoAberto && getItemPedido().permiteAplicarDesconto() && !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(getItemPedido()) && !isItemAutorizacao(getItemPedido())) {
				btOpcoes.addItemAt(Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE, 9);
			}
			// --Desconto pacote
			if (LavenderePdaConfig.usaDescQuantidadePorPacote && inPedidoAberto) {
				btOpcoes.addItemAt(Messages.DESCONTO_PACOTE_TITULO, 10);
			}
			// --Desconto quantidade grupo
			if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && inPedidoAberto && !PedidoService.getInstance().possuiCondicaoComercial(pedido)) {
				btOpcoes.addItemAt(Messages.DESCONTOGRUPO_NOME_ENTIDADE, 10);
			}
			// --Observaçoes
			if (LavenderePdaConfig.usaObservacaoPorItemPedido && !isFiltrandoAgrupadorGrade()) {
				btOpcoes.addItemAt(Messages.ITEMPEDIDO_LABEL_DSOBSERVACAO, 11);
			}
			// --Similares
			if (LavenderePdaConfig.usaSimilarVendaProduto && !isModoGrade()) {
				btOpcoes.addItemAt(Messages.MENU_OPCAO_SIMILARES, 12);
			}
			// --Desconto tabela preço
			if (LavenderePdaConfig.mostraDescontosPorTabelaPreco && inPedidoAberto && !LavenderePdaConfig.isOcultaDescTabela()) {
				btOpcoes.addItemAt(Messages.ITEM_DESCONTO_POR_TABELA, 13);
			}
			// --Substituição tributária
			if (LavenderePdaConfig.usaRelSubstituicaoTributaria && LavenderePdaConfig.isUsaCalculoStItemPedido()) {
				btOpcoes.addItemAt(Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA, 14);
			}
			// --Libera com senha produto bloqueado por metaPorGrupoProd
			if (LavenderePdaConfig.liberaComSenhaVendaProdutoBloqueado && inPedidoAberto) {
				btOpcoes.addItemAt(Messages.MENU_LIBERACOMSENHAVENDAPRODUTO, 15);
			}
			// --Saldo de Oportunidade
			if (LavenderePdaConfig.usaControleSaldoOportunidade) {
				btOpcoes.addItemAt(Messages.LIMITEOPORTUNIDADE_SALDO_OPORTUNIDADE, 16);
			}
			// --Rel.Comissão Tabela Preço
			if (LavenderePdaConfig.exibeRelatorioComissaoAoSelecionarItem && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca() && !pedido.tipoPedidoIgnoraCalculoComissao()) {
				btOpcoes.addItemAt(Messages.MENU_REL_COMISSAO_PRECO, 17);
			}
			// -- Desconto Promocional por Qtde
			if (!LavenderePdaConfig.ocultaDescontoPorQuantidadeItemPedido && LavenderePdaConfig.usaDescontoQtdeNoGrupoDescPromocional && getItemPedido().permiteAplicarDesconto() && DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(getItemPedido()) && inPedidoAberto && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL)) {
				btOpcoes.addItemAt(Messages.DESC_PROMOCIONAL_POR_QTDE, 18);
			}
			// --Rel.Informações Tributárias
			if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido()) {
				btOpcoes.addItemAt(Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA, 19);
			}
			// -- Rel. Rentabilidade x Comissão
			if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
				btOpcoes.addItemAt(Messages.REL_RENTABILIDADE_COMISSAO_TITULO, 20);
			}
			// --Informações Extras do Item do Pedido
			if (LavenderePdaConfig.isUsaOrdemCamposTelaInfoExtra && !isFiltrandoAgrupadorGrade() && !pedido.isPedidoBonificacao()) {
				btOpcoes.addItemAt(Messages.ITEMPEDIDO_INFORMACOES_EXTRAS, 21);
			}
			if (!isModoGrade() && (Session.isModoSuporte || VmUtil.isJava())) {
				btOpcoes.addItemAt(Messages.MENU_OPCAO_DETALHES_CALCULOS, 22);
			}
			// --Libera Preço por Produto de acordo com o usuário
			if (LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido && inPedidoAberto) {
				btOpcoes.addItemAt(Messages.PARAMETRO_LIBERACOMSENHAPRECOBASEADOPERCENTUALUSUARIOESCOLHIDO, 23);
			}
			if ((LavenderePdaConfig.usaRegistroProdutoFaltante || LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) && pedido.isPedidoAbertoEditavel()) {
				btOpcoes.addItemAt(Messages.MENU_REGISTRAR_FALTA_PRODUTO, 23);
			}
			// --Diferenças
			if (LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif && pedido.isFlOrigemPedidoErp()) {
				btOpcoes.addItemAt(Messages.REL_DIF_ITEMPEDIDO_LABEL_DIFERENCAS, 24);
			}
			//-- Giro Produto
			if (!pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido() && LavenderePdaConfig.usaBotaoGiroProdutoItemPedido && LavenderePdaConfig.isUsaGiroProduto() && inPedidoAberto && !isEditing() && !cadPedidoForm.inItemNegotiationGiroProdutoPendente) {
				btOpcoes.addItem(Messages.GIROPRODUTO_NOME_ENTIDADE);
			}
			if (LavenderePdaConfig.usaDescQuantidadeApenasEmbalagemCompleta && inPedidoAberto && ValueUtil.isNotEmpty(itemPedido.cdProduto)) {
				btOpcoes.addItem(Messages.BOTAO_CALCULA_DESC_QTD_PARA_EMB_COMPLETA);
			}
			if (LavenderePdaConfig.calculaPrecoPorVolumeProduto && LavenderePdaConfig.usaInfoComplementarItemPedido()) {
				if (itemPedido != null && itemPedido.getProduto().isApresentaInfoComplCalculoPrecoPorVolumePorProduto()) {
					btOpcoes.addItem(Messages.LABEL_INFOS_COMPLEMENTARES);
				}
			} else if (LavenderePdaConfig.usaInfoComplementarItemPedido()) {
				btOpcoes.addItem(Messages.LABEL_INFOS_COMPLEMENTARES);
			}
			if (!isModoGrade() && LavenderePdaConfig.apresentaMultiplasSugestoesLocaisAdicionais(SugestaoVenda.LOCAL_MENU_INFERIOR) && LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento() && !pedido.isPedidoAbertoNaoEditavel() ) {
				btOpcoes.addItem(Messages.PEDIDO_TITULO_SUGESTAO_MULTIPLOS_PRODUTOS);
			}
			if (LavenderePdaConfig.apresentaValorVendaItemPedidoRedeCliente && pedido.isPedidoAberto()) {
				btOpcoes.addItem(Messages.BOTAO_ULTIMOS_PRECOS_REDE);
			}
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && itemPedido.isAgrupadorSimilaridade()) {
				btOpcoes.addItem(Messages.BOTAO_AGRUPADOR_SIMILARIDADE);
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !pedido.isPedidoTroca() && !pedido.isPedidoBonificacao()) {
				btOpcoes.addItem(Messages.TITULO_POLITICAS_BONIFICACAO);
			}

		} else {
			if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido()) {
				if (LavenderePdaConfig.isUsaSugestaoVendaComCadastro() && inPedidoAberto && !pedido.isPedidoBonificacao()) {
					btOpcoes.addItem(Messages.PEDIDO_LABEL_SUGESTAO_VENDAS);
					btOpcoes.addItem(Messages.PEDIDO_LABEL_SUGESTAO_VENDAS_COM_QTDE);
				}
				if (LavenderePdaConfig.usaSugestaoVendaBaseadaDifPedidos > 0 && inPedidoAberto && !pedido.isPedidoBonificacao()) {
					btOpcoes.addItem(Messages.PEDIDO_LABEL_SUGESTAO_DIF_ULT_PEDIDOS);
				}
			}
			if (mostraSugestoesMenuOpcoes()) {
				btOpcoes.addItem(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_SUGESTOES);
		}
		}
		if (pedido.hasErrosInsertSugestaoPedido()) {
			btOpcoes.addItem(Messages.PEDIDO_ITENS_NAO_INSERIDOS);
		}
		if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 && LavenderePdaConfig.qtdItensRentabilidadeIdealSugeridos > 0) {
			btOpcoes.addItem(Messages.PEDIDO_TITULO_SUGESTAO_RENTABILIDADE_IDEAL);
		}
		if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
			btOpcoes.addItem(Messages.PRODUTORETIRADA_NOME_BT);
		}
		// --
		btOpcoes.addItem(Messages.MENU_UTILITARIO_CALCULADORA);
		// --
		if (cadPedidoForm != null && !cadPedidoForm.inOnlyConsultaItens && !LavenderePdaConfig.isOcultaAcessosRelUltimosPedidosMenuInferior()) {
			btOpcoes.addItem(Messages.CLIENTE_ULTIMOS_PEDIDOS);
		}
		if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco ||
				LavenderePdaConfig.informaVerbaManual ||
				LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() ||
				LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao ||
				LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto ||
				(LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco && pedido.isPedidoAberto()) ||
				(LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada() && getItemPedido().getProduto() != null)) &&
				!LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex &&
				!pedido.isIgnoraControleVerba()) {
			if (!LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada() && !LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
			btOpcoes.addItem(Messages.VERBA_NOME_ENTIDADE);
			} else if (getItemPedido().getProduto() != null && pedido.isPedidoAberto()) {
				btOpcoes.addItem(Messages.VERBA_NOME_ENTIDADE);
		}
		}
		if (LavenderePdaConfig.validaSaldoPedidoBonificacao) {
			btOpcoes.addItem(Messages.SALDOBONIFICACAO_LABEL);
		}
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade()) {
			btOpcoes.addItem(Messages.RENTABILIDADE_NOME_ENTIDADE);
		}
		if ((LavenderePdaConfig.isUsaKitProduto() || LavenderePdaConfig.isUsaKitTipo3()) && inPedidoAberto && !pedido.isPedidoTroca() && !pedido.isPedidoBonificacao()) {
			btOpcoes.addItem(Messages.ITEMKIT_TITULO_CADASTRO);
		}
		if (!pedido.isSugestaoPedidoGiroProduto() && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido() && LavenderePdaConfig.isUsaGiroProduto() && inPedidoAberto && !isEditing() && cadPedidoForm != null && !cadPedidoForm.inItemNegotiationGiroProdutoPendente && !LavenderePdaConfig.usaBotaoGiroProdutoItemPedido) {
			btOpcoes.addItem(Messages.GIROPRODUTO_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.isMostraFotoProduto() && !isEditing() && inPedidoAberto && isListandoSemGradeOuEditandoGrade()) {
			btOpcoes.addItem(Messages.ITEMPEDIDO_LABEL_SLIDEFOTOS);
		}
		if (LavenderePdaConfig.usaPesquisaMercado && inPedidoAberto) {
			btOpcoes.addItem(Messages.PESQUISAMERCADO_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.usaPesquisaItemPedidoPesquisaMercado() && inPedidoAberto) {
			btOpcoes.addItem(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && inPedidoAberto) {
			btOpcoes.addItem(Messages.BOTAO_PREVISAO_DESCONTOS);
		}
		if (LavenderePdaConfig.usaRelNovidadeProduto) {
			btOpcoes.addItem(Messages.MENU_OPCAO_RELNOVIDADEPRODUTO);
		}
		if ((LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto) && ValueUtil.isNotEmpty(pedido.itemPedidoList) && isEditing && inPedidoAberto) {
			// Quando botão de Desconto Comissao estiver disponivel na tela, o botão excluir
			// passa para o menu Opções, pois estes ocupam a mesma posição na tela
			btOpcoes.addItem(FrameworkMessages.BOTAO_EXCLUIR);
		}
		if (LavenderePdaConfig.liberaSenhaQuantidadeMaximaVendaProduto) {
			btOpcoes.addItem(Messages.MENU_OPCAO_DESBLOQUEAR_LIMITADOR);
		}
		if (LavenderePdaConfig.mostraRelatorioGrupoProdutoNaoInseridosPedido) {
			btOpcoes.addItem(Messages.MENU_OPCAO_GRUPOS_PENDENTES);
		}
		if (permiteMenuProdutosPendentes(inPedidoAberto)) {
			btOpcoes.addItem(Messages.MENU_PRODUTOS_PENDENTES);
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.pctVidaUtilLoteProdutoCritico > 0) {
			btOpcoes.addItem(Messages.ITEM_PRODUTO_LOTE);
		}
		if (LavenderePdaConfig.atualizarEstoqueInterno && !LavenderePdaConfig.ocultaRecalculaEstoque && getItemPedido().getProduto() != null) {
			btOpcoes.addItem(Messages.MENU_RECALCULAR_ESTOQUE);
		}
		if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada) {
			btOpcoes.addItem(Messages.USUARIODESC_NOME_ENTIDADE);
		}
		if (LavenderePdaConfig.usaRelDescontosAplicadosNoItemPedidoPorFuncionalidade) {
			btOpcoes.addItem(Messages.BOTAO_REL_DESCONTOS);
		}
		if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica) {
			btOpcoes.addItem(Messages.REL_LIBERACOES_SENHA_TITULO);
		}
		if (LavenderePdaConfig.isUsaCadastroProdutoDesejadosForaCatalogo()) {
			btOpcoes.addItem(Messages.PRODUTODESEJADO_LABEL_BOTAO);
		}
		if (LavenderePdaConfig.usaDescQuantidadePeso()) {
			btOpcoes.addItem(Messages.BT_DESCONTO_FAIXA_PESO);
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() && inPedidoAberto) {
			btOpcoes.addItem(Messages.BOTAO_DESCONTO_VENDAS_MES);
		}
		if (LavenderePdaConfig.usaHistoricoVendasPorListaColunaTabelaPreco) {
			btOpcoes.addItem(Messages.BOTAO_HISTLISTATABPRECO);
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem() && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			btOpcoes.addItem(Messages.BOTAO_COMISSAO_ITEMPEDIDO_REP_WINDOW);
		}
		if (LavenderePdaConfig.mostraObservacaoCliente() && pedido.isPedidoAberto()) {
			btOpcoes.addItem(Messages.OBSERVACAO_CLIENTE);
		}
		if (!isModoGrade() && (Session.isModoSuporte && LavenderePdaConfig.usaConfigMargemRentabilidade())) {
			btOpcoes.addItem(Messages.MENU_OPCAO_DETALHES_VARIAVEIS_DE_CALCULO);
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior() && pedido.isPedidoAberto() && !pedido.isPedidoCritico() && !pedido.isTipoFreteFob() && !pedido.isPedidoBonificacao()) {
			btOpcoes.addItem(Messages.SUGESTAO_COMBO_PRODUTO);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			btOpcoes.addItem(Messages.SOL_AUTORIZACAO_ITEM_PEDIDO);
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && !pedido.isPedidoBonificacao() && pedido.isPedidoAberto() && !isEditingFromRelDescontoProgressivo()) {
			btOpcoes.addItem(Messages.DESC_PROG_PEDIDO_BUTTON);
		}
		if (LavenderePdaConfig.mostraInfoEstoqueUnidades) {
			btOpcoes.addItem(Messages.ESTOQUE_FILIAIS);
		}
		if (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() && LavenderePdaConfig.isUsaRelCatalogoCapaItemPedido()) {
			btOpcoes.addItem(Messages.GERAR_CATALOGO);
		}
		if (LavenderePdaConfig.usaHistoricoAtendimentoUnificado) {
			btOpcoes.addItem(Messages.ATENDIMENTOHIST_TITLE);
		}
		if (isUsaBotaoBonificarItemPedido() && isUsaBotaoLibPrecoSolAutorizacao(pedido)) {
			if (isPedidoPermiteLibPreco(itemPedido) && isMostraBtLiberarPreco(itemPedido) && itemPedido.getProduto() != null) {
				btOpcoes.addItem(Messages.BOTAO_SOL_AUTORIZACAO_LIB_PRECO);
			}
		}
	}

	private boolean permiteMenuProdutosPendentes(boolean inPedidoAberto) throws SQLException {
		return LavenderePdaConfig.isMostraRelProdutosNaoInseridosPedido() && inPedidoAberto && !pedido.isOportunidade() && !cadPedidoForm.inItemNegotiationProdutosPendentes && !isOcultaProdutoPendenteMenuOpcoes
				&& !pedido.getCliente().isClienteDefaultParaNovoPedido() && !pedido.getCliente().isNovoClienteDefaultParaNovoPedido();
	}
	
	private boolean isEditingFromRelDescontoProgressivo() {
		return editingExternalForm instanceof RelDescontoProgFamiliaProdForm;
	}

	private boolean isItemAutorizacao(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) return false;
		return itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, null);
	}

	@Override
	protected String[] getDefaultEditLabels() {
		if (LavenderePdaConfig.usaGradeProduto5() && isModoGrade()) {
			return LavenderePdaConfig.getCamposTelaItemPedidoGrade();
		}
		return new String[] { "1", "3", "4", "6", "7", "13" };
	}

	@Override
	protected void cbTabelaPrecoClick() throws SQLException {
		cbTabelaPrecoChangeManual();
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
			int index = listContainer.getScrollPos();
			list();
			listContainer.setScrollPos(index);
		}
		if (ValueUtil.isEmpty(getSelectedRowKey())) {
			if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.isNotEmpty(cbItemGrade1.getValue())) {
				getItemPedido().cdItemGrade1 = cbItemGrade1.getValue();
			}
			changeItemTabelaPreco();
			refreshProdutoToScreen(getItemPedido());
			if (getItemPedido().getProduto() != null) {
				gridClickUnidadeAlternativa(getItemPedido(), false);
		}
		}
		reloadCbGrupoProdutosFromTabelaPrecoChange();
		repaint();
	}

	private void reloadCbGrupoProdutosFromTabelaPrecoChange() throws SQLException {
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 0 && ValueUtil.isNotEmpty(cbTabelaPreco.getValue())) {
			String oldCdGrupoProduto1 = cbGrupoProduto1.getValue();
			String oldCdGrupoProduto2 = null;
			String oldCdGrupoProduto3 = null;
			String oldCdGrupoProduto4 = null;
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				oldCdGrupoProduto2 = cbGrupoProduto2.getValue();
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				oldCdGrupoProduto3 = cbGrupoProduto3.getValue();
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				oldCdGrupoProduto4 = cbGrupoProduto4.getValue();
			}
			Pedido filter = (Pedido) pedido.clone();
			filter.cdTabelaPreco = cbTabelaPreco.getValue();
			loadGrupoProduto1(filter);
			cbGrupoProduto1.setValue(oldCdGrupoProduto1);
			if (cbGrupoProduto1.getSelectedIndex() == -1) {
				cbGrupoProduto1.setSelectedIndex(0);
		}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				loadGrupoProduto2(pedido);
				cbGrupoProduto2.setValue(oldCdGrupoProduto2);
				if (cbGrupoProduto2.getSelectedIndex() == -1) {
					cbGrupoProduto2.setSelectedIndex(0);
	}
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				loadGrupoProduto3();
				cbGrupoProduto3.setValue(oldCdGrupoProduto3);
				if (cbGrupoProduto3.getSelectedIndex() == -1) {
					cbGrupoProduto3.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				loadGrupoProduto4();
				cbGrupoProduto4.setValue(oldCdGrupoProduto4);
				if (cbGrupoProduto4.getSelectedIndex() == -1) {
					cbGrupoProduto4.setSelectedIndex(0);
				}
			}
		}
	}
	
	private void btConfiguracaoDescontoClick() throws SQLException {
		RelUsuarioDescWindow relUsuarioDescWindow = new RelUsuarioDescWindow();
		relUsuarioDescWindow.popup();
	}
			
	private void btRelDescontosClick() {
		RelDescontosWindow relDescontosWindow = new RelDescontosWindow((Pedido) pedido.clone());
		relDescontosWindow.popup();
	}
	
	private void btRelLiberacoesSenhaClick() throws SQLException {
		RelLiberacoesSenhaWindow relLiberacoesSenhaWindow = new RelLiberacoesSenhaWindow(pedido.getCliente().cdCliente);
		relLiberacoesSenhaWindow.popup();
	}
	
	private void setInfoRentabilidadeVisible(boolean isOculto) throws SQLException {
		/* 30 */
		lbRentabilidadeItem.setVisible(!isOculto);
		edRentabilidadeItem.setVisible(!isOculto);
		/* 31 */
		lbRentabilidadePedido.setVisible(!isOculto);
		edRentabilidadePedido.setVisible(!isOculto);
		/* 45 */
		lbVlIndiceRentabItem.setVisible(!isOculto);
		edVlIndiceRentabItem.setVisible(!isOculto);
		/* 46 */
		lbVlIndiceRentabPedido.setVisible(!isOculto);
		edVlIndiceRentabPedido.setVisible(!isOculto);
		/* 80 */
		lbFaixaComissaoRentabilidade.setVisible(!isOculto);
		edFaixaComissaoRentabilidade.setVisible(!isOculto);
		/* 81 */
		lbFaixaComissaoRentabilidadeMinima.setVisible(!isOculto);
		edFaixaComissaoRentabilidadeMinima.setVisible(!isOculto);
		//--
		cadPedidoForm.setInfoRentabilidadeVisible(isOculto);

		if (btIconeRentabilidade != null) {
			if (isFiltrandoAgrupadorGrade() && !LavenderePdaConfig.usaCarrosselProdutosGradeDetalheItem) {
				btIconeRentabilidade.setVisible(false);
		} else {
				if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
					btIconeRentabilidade.setVisible(true);
				} else if (isExibeIconeRentabilidadePedido()) {
					btIconeRentabilidade.setVisible(!isOculto);
		}
	}
		}
	}
	
	private boolean exibeAvisoLimite() throws SQLException {
		Vector itemPedidoList = new Vector();
		if (ValueUtil.isEmpty(limiteAviso)) {
			limiteAviso = LavenderePdaConfig.getLimitesAvisoControleItemPedido();
			if (limiteAviso == null) {
				return false;
			}
		}
		if (limiteAviso.length > 0) {
			ItemPedido itemPedido = getItemPedido();
			ProdutoErro produtoErro = null;
			double unidadesItemPedido = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
			if (!ValueUtil.isEmpty(limiteAviso[0])) {
				if (unidadesItemPedido > ValueUtil.getDoubleValue(limiteAviso[0])) {
					produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, Messages.ITEMPEDIDO_EXCEDEU_LIMITE_QTDE);
					produtoErro.dsVlLimite = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_LIMITE_QUANT, StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(limiteAviso[0])));
					produtoErro.dsVlAtual = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_QUANT_ATUAL, StringUtil.getStringValueToInterface(unidadesItemPedido));
					itemPedidoList.addElement(produtoErro);
				}
			}
			if (!ValueUtil.isEmpty(limiteAviso[1]) && itemPedido.vlTotalItemPedido > ValueUtil.getDoubleValue(limiteAviso[1])) {
				produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, Messages.ITEMPEDIDO_EXCEDEU_LIMITE_VALOR);
				produtoErro.dsVlLimite = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_LIMITE_VALOR, StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(limiteAviso[1])));
				produtoErro.dsVlAtual = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_VALOR_ATUAL, StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
				itemPedidoList.addElement(produtoErro);
			}
			double qtPeso = ItemPedidoService.getInstance().getPesoItemPedido(itemPedido);
			if (!ValueUtil.isEmpty(limiteAviso[2]) && qtPeso > ValueUtil.getDoubleValue(limiteAviso[2])) {
				produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, Messages.ITEMPEDIDO_EXCEDEU_LIMITE_PESO);
				produtoErro.dsVlLimite = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_ATINGIU_LIMITE_PESO, StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(limiteAviso[2])));
				produtoErro.dsVlAtual = MessageUtil.getMessage(Messages.ITEMPEDIDO_AVISO_ITEM_PESO_ATUAL, StringUtil.getStringValueToInterface(qtPeso));
				itemPedidoList.addElement(produtoErro);
			}
			if (itemPedidoList.size() > 0) {
				ListLimiteAvisoWindow avisoLimiteWindow = new ListLimiteAvisoWindow(itemPedidoList);
				avisoLimiteWindow.popup();
				return !avisoLimiteWindow.continuar;
			}
		}
		return false;
	}
	
	private void btProdutoDesejadoClick(boolean fromSugestao) throws SQLException {
		if (fromSugestao) {
			show(new CadProdutoDesejadoForm(pedido, TermoCorrecaoService.getInstance().getDsTermoCorrigido(dsFiltro)));
		} else {
			show(new ListProdutoDesejadoForm(getItemPedido().pedido));
		}
	}
	
	
	@Override
	protected void insertOrUpdate(BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (isInsereMultiplosSemNegociacao() && !(LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade()) || LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() && isFiltrandoAgrupadorGrade()) {
			try {
				ItemPedidoService.getInstance().validateDuplicated(domain);
				try {
					insert(domain);
				} catch (Throwable e) {
					throw new RuntimeException(e.getMessage());
				}
			} catch (ItemKitPedidoInseridoException e) {
				throw e;
			} catch (ValidationException e) {
				update(domain);
			}
		} else if ((LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao()) && itemPedido.isItemBonificacao() && itemPedido.isBonificandoItemPeloBotao) {
			insert(domain);
		} else {
			super.insertOrUpdate(domain);
		}
	}
	
	public ItemPedido iniciaItemVendaMultInsercao(ItemContainerControlState itemContainerState, ItemContainer itemContainer, boolean changeUnidadeAlternativa, boolean changeQtd, boolean changeValue, boolean changeDesc, boolean forceRefresh) throws SQLException {
		if (itemContainer == null) {
			return null;
		}
		boolean acessaInsercaoFromSugestoes = LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao();
		ItemPedido itemPedido;
		if (acessaInsercaoFromSugestoes && itemContainer.itemPedido != null) {
			itemPedido = itemContainer.itemPedido;
		} else {
			itemPedido = addNovoItem(true, -1);
		}
		itemPedido.flTipoEdicao = 0;
		itemPedido.cdProduto = itemContainer.produto.cdProduto;
		if (itemPedido.nuSeqItemPedido == 0) {
			itemPedido.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
		}
		boolean usaAlteracaoValorTabelaInsercaoMultiplaSemNegociacao = LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao();
		double oldVlItemPedido = 0;
		double oldQtItemFisico = 0;
		double oldVlPctAcrescDesc = 0;
		boolean hasModificacoes = false;
		boolean acrescimoAlterado = false;
		boolean itemPedidoExiste = false;
		String oldCdUnidade;
		ItemPedido itemFromPedidoList = null;
		try {
			if (!acessaInsercaoFromSugestoes || itemContainer.itemPedido == null) {
				if (!ValueUtil.valueEquals(((Item)itemContainer.getParent()).id, listContainer.getSelectedId())) {
			}
				inicializaItemParaVenda(itemPedido, true);
			} else if (itemContainer.itemPedido != null) {
				ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, pedido);
			}
			if (itemContainer.itemPedido == null) {
				itemContainer.itemPedido = itemPedido;
			}
			itemPedido.naoComparaSeqItem = true;
			if (usaAlteracaoValorTabelaInsercaoMultiplaSemNegociacao) {
				double value = itemContainer.edVlBrutoItem.getValueDouble();
				itemPedido.vlManualBrutoItem = ValueUtil.isNotEmpty(itemContainer.edVlBrutoItem.getText()) ? value : -1;
				itemPedido.flValorTabelaAlterado = itemPedido.vlManualBrutoItem > 0 ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
				itemContainer.setVlBrutoItem(value);
			}
			changeItemTabelaPreco(itemPedido, null, false, null);
			try {
				if (changeQtd) {
					if (!validaAlterarQtdItem(itemPedido)) {
						itemContainer.setChooserQtdValue(itemPedido.getQtItemFisico());
						itemContainer.itemPedido = itemPedido;
						return itemPedido;
				}
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_QTD;
				}
			} catch (Throwable e) {
				itemContainer.setChooserQtdValue(itemPedido.getQtItemFisico());
				throw e;
			}
			double qtItemFisico = itemContainer.chooserQtd.getValue();
			double vlPctDescontoAcrescimo = itemContainer.chooserDescAcresc != null ? itemContainer.chooserDescAcresc.getValue() : 0;
			double qtEstoqueCliente = itemContainer.chooserEstoqueCliente != null ? itemContainer.chooserEstoqueCliente.getValue() : 0;
			acrescimoAlterado = vlPctDescontoAcrescimo < 0;

			int indexOf = pedido.itemPedidoList.indexOf(itemPedido);
			itemFromPedidoList = indexOf >= 0 ? (ItemPedido) pedido.itemPedidoList.items[indexOf] : null;
			itemPedidoExiste = itemFromPedidoList != null;
			if (isAtribuiOldVlPctAcrescimo(itemPedido)) {
				oldVlPctAcrescDesc = itemPedidoExiste ? itemFromPedidoList.vlPctAcrescimo * -1 : itemPedido.vlPctAcrescimo * -1;
			} else {
				oldVlPctAcrescDesc = itemPedidoExiste ? itemFromPedidoList.vlPctDesconto : itemPedido.vlPctDesconto;
				}
			oldVlItemPedido = itemPedidoExiste ? itemFromPedidoList.vlItemPedido : itemPedido.vlItemPedido;
			oldQtItemFisico = itemPedidoExiste ? itemFromPedidoList.getQtItemFisico(): itemPedido.getQtItemFisico();
			oldCdUnidade = itemPedidoExiste ? itemFromPedidoList.cdUnidade : itemPedido.cdUnidade;
			if (ValueUtil.isEmpty(oldCdUnidade)) {
				oldCdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
				}
			boolean houveAlteracaoVlItemPedido = itemContainer.edVlItemPedido != null && itemContainer.edVlItemPedido.getValueDouble() != oldVlItemPedido;
			boolean houveMudancaDescItemChooser = itemContainer.chooserDescAcresc != null && ValueUtil.round(oldVlPctAcrescDesc) != itemContainer.chooserDescAcresc.getValue();
			boolean houveMudancaQuantidade = itemContainer.chooserQtd != null && oldQtItemFisico != itemContainer.chooserQtd.getValue();
			boolean houveAlteracaoUnidade = itemContainer.cbUnidadeAlternativa != null && !ValueUtil.valueEquals(oldCdUnidade, itemContainer.cbUnidadeAlternativa.getValue());
			hasModificacoes = (indexOf == -1 && qtItemFisico > 0) || houveAlteracaoVlItemPedido || houveMudancaDescItemChooser || houveMudancaQuantidade || houveAlteracaoUnidade;
			if (!hasModificacoes) {
				return null;
			}
			if (houveAlteracaoVlItemPedido && houveMudancaDescItemChooser) {
				if (!changeDesc && !changeValue) {
					changeValue = true;
					changeDesc = false;
				}
			} else if (houveAlteracaoVlItemPedido && !changeValue) {
				changeValue = true;
				changeDesc = false;
			} else if (houveMudancaDescItemChooser && !changeDesc) {
				changeValue = false;
				changeDesc = true;
			}
			if (qtItemFisico > 0) {
			if (isInsereMultiplosSemNegociacao()) {
				itemPedido.qtItemFisico = qtItemFisico;
					if (itemContainer.chooserDescAcresc != null) {
						itemContainer.refreshChooserDescAcresc(itemPedido);
						if (acrescimoAlterado) {
							itemPedido.vlPctAcrescimo = Math.abs(vlPctDescontoAcrescimo);
						} else {
							itemPedido.vlPctDesconto = vlPctDescontoAcrescimo;
				}
				}
					if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
						itemPedido.vlItemPedido = itemContainer.edVlItemPedido.getValueDouble();
					}
				itemPedido.qtEstoqueCliente = qtEstoqueCliente;
				}
				if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
					itemPedido.vlItemPedido = itemContainer.edVlItemPedido.getValueDouble();
					edVlItemPedido.setValue(itemContainer.edVlItemPedido.getValue());
				}
				itemPedido.qtEstoqueCliente = qtEstoqueCliente;
				edQtItemFisico.setValue(itemPedido.getQtItemFisico());
				edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
				edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
				if (!ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL)) {
					edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
				}
				if (ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL)) {
					edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
				}
			} else {			
				edQtItemFisico.setValue(1d);
			}
			if (itemContainer.chooserDescAcresc != null && changeDesc) {
				setItemPedido(itemPedido);
				if (itemContainer.chooserDescAcresc != null && houveMudancaDescItemChooser) {
					try {
						if (validaAlterarValorItem(itemPedido)) {
							if (acrescimoAlterado) {
								itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
								edVlPctAcrescimoValueChange(itemPedido);
						} else {
								itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
								edVlPctDescontoValueChange(itemPedido, true);
						}
						} else {
							itemContainer.setChooserDesc(oldVlPctAcrescDesc);
							itemContainer.itemPedido = itemPedido;
							return itemPedido;
						}
					} catch (Throwable e) {
						itemContainer.setChooserDesc(oldVlPctAcrescDesc);
						throw e;
					}
				}
			}
			if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
				if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
					ItemPedidoService.getInstance().changeUnidadeAlternativa(itemPedido, itemContainer.cbUnidadeAlternativa.getValue());
				}
				if ((changeValue && oldVlItemPedido != itemContainer.edVlItemPedido.getValueDouble()) && !changeDesc && !changeUnidadeAlternativa) {
					setItemPedido(itemPedido);
					itemPedido.vlItemPedido = itemContainer.edVlItemPedido.getValueDouble();
					edVlItemPedido.setValue(itemContainer.edVlItemPedido.getValueDouble());
					if (validaAlterarValorItem(itemPedido, edVlItemPedido, oldVlItemPedido)) {
						edVlItemPedidoValueChange();
					} else {
						itemContainer.itemPedido = itemPedido;
					}
					if (oldVlItemPedido != itemContainer.edVlItemPedido.getValueDouble() && ! changeDesc && ! houveMudancaDescItemChooser) {
						itemPedido.vlItemPedido = itemContainer.edVlItemPedido.getValueDouble();
						edVlItemPedidoValueChange(itemPedido, true);
				}
			}
			}
			int flTipoEdicao = itemPedido.flTipoEdicao;
			edQtItemFisicoValueChange(itemPedido);
			if (flTipoEdicao > 0) itemPedido.flTipoEdicao = flTipoEdicao;
			if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				itemPedido.setQtItemFisico(itemContainer.chooserQtd.getValue());
			}			
			if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
				if (changeUnidadeAlternativa) {
					try {
						if (!validaTrocarCbUnidade(itemPedido, itemContainer.cbUnidadeAlternativa.getValue())) {
							itemContainer.cbUnidadeAlternativa.setValue(itemPedido.cdUnidade, itemPedido.cdProduto, itemPedido.cdItemGrade1);
							atualizaItemPedidoNaLista(itemPedido);
							itemContainer.itemPedido = itemPedido;
							return itemPedido;
						}
					} catch (Throwable e) {
						itemContainer.cbUnidadeAlternativa.setValue(itemPedido.cdUnidade, itemPedido.cdProduto, itemPedido.cdItemGrade1);
						itemContainer.itemPedido = itemPedido;
						throw e;
					}
					if (itemContainer.cbUnidadeAlternativa != null) {
						itemPedido.cdUnidade = itemContainer.cbUnidadeAlternativa.getValue();
					}
					itemPedido.clearProdutoUnidadeInfo();
				}
				if (vlPctDescontoAcrescimo > 0) {
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
				} else if (vlPctDescontoAcrescimo < 0) {
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
				}
				if (ValueUtil.isNotEmpty(itemContainer.cbUnidadeAlternativa.getItems())) {
					ItemPedidoService.getInstance().changeUnidadeAlternativa(itemPedido, itemContainer.cbUnidadeAlternativa.getValue());
				} else {
					itemContainer.setChooserQtdValue(0);
					itemContainer.itemPedido = itemPedido;
					throw new ValidationException(Messages.PEDIDO_TODAS_UN_RESTRITAS);
				}
			} else {
				if (itemPedido.getQtItemFisico() == 0) {
					itemPedido.setQtItemFisico(itemContainer.chooserQtd.getValue());
				}
				}
			if (usaAlteracaoValorTabelaInsercaoMultiplaSemNegociacao) {
				if (itemPedido.oldVlBruto <= 0) {
					itemPedido.oldVlBruto = itemPedido.getItemTabelaPreco().vlUnitario;
				}
				if (ItemPedidoService.getInstance().isVlItemPedidoMenorVlMin(itemPedido)) {
					if (!confirmaValorMinimoItemPedido(itemPedido)) {
						inicializaItemParaVenda(itemPedido, true);
						itemPedido.vlManualBrutoItem = itemPedido.oldVlBruto;
						changeItemTabelaPreco(itemPedido, null, true, null);
						itemPedido.setQtItemFisico(qtItemFisico);
						itemPedido.vlPctDesconto = itemPedido.oldPctDescSemNegoc;
						if (itemContainer.chooserDescAcresc != null) {
							itemContainer.setChooserDesc(itemPedido.oldPctDescSemNegoc);
						}
						if (itemPedido.vlPctDesconto > 0) {
							itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
						}
						calculateItemForGrid(itemPedido);
						itemContainer.setVlBrutoItem(itemPedido.vlBaseItemTabelaPreco);
						itemPedido.flValorTabelaAlterado = ValueUtil.VALOR_NAO;
					} else {
						itemPedido.oldVlBruto = itemPedido.vlBaseItemTabelaPreco;
					}
				} else {
					itemPedido.oldVlBruto = itemPedido.vlBaseItemTabelaPreco;
				}
			}
			
			if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() && !ValueUtil.isEmpty(itemPedido.vlItemPedido)) {
				if (itemContainer.chooserDescAcresc != null) {
					itemContainer.setChooserDesc(LavenderePdaConfig.isPermiteEditarDescontoAcrescimoMultiplaInsercao() && isAtribuiOldVlPctAcrescimo(itemPedido) ? itemPedido.vlPctAcrescimo * -1 : itemPedido.vlPctDesconto);
				}
				itemContainer.setEdVlItemPedido(itemPedido.vlItemPedido);
			}
			calcularClick(true, itemPedido);
			itemContainer.setlbVl(Messages.PRODUTO_LABEL_RS +StringUtil.getStringValueToInterface(itemPedido.vlItemPedido));
			itemContainer.setTotalItem(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
			itemContainer.setQe(Messages.ITEMPEDIDO_LABEL_CAIXAPADRAO + " " + itemContainer.itemPedido.getQtEmbalagemElementarToInterface());
			itemContainer.setVep(Messages.ITEMPEDIDO_LABEL_PRECOEMBPRIMARIA + " " + StringUtil.getStringValueToInterface(itemPedido.vlEmbalagemElementar));
			itemContainer.setVue(Messages.ITEMPEDIDO_LABEL_VL_UNIDADE_RENTABILIDADE + " " + StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().calculaVlUnidadePorEmbalagemWhenProdutoBase(itemPedido, itemPedido.pedido, itemContainer.produto, null)));//veio do chooser desc //veio do chooser qtd
			double saldoEstoque;
			if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
				saldoEstoque = RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(itemPedido);
			} else {
				double qtEstoque;
				qtEstoque = EstoqueService.getInstance().getQtEstoque(itemPedido.cdProduto, itemPedido.cdLocalEstoque);
				if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
					qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPdaComParcialPrevisto(itemPedido);
				}
				if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
					ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
					qtEstoque = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade, qtEstoque);
					itemContainer.setQtHist(produtoUnidade);
					itemContainer.setVlHist(produtoUnidade);
				}
				saldoEstoque = EstoqueService.getInstance().calcSaldoEstoque(itemPedido, itemPedido.getQtItemFisico() - itemPedido.getOldQtItemFisico(), qtEstoque);
			}
			itemContainer.setQtEstoque(EstoqueService.getInstance().getEstoqueToString(saldoEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
			
			itemContainer.itemPedido = itemPedido;
			if (LavenderePdaConfig.isFiltraItensComErroNaInsercaoMultiplaItens()) {
				if (itemContainerErroMap == null) {
					itemContainerErroMap = new HashMap<>(20);
				}
				Item container = (Item) itemContainer.getParent();
				itemContainerErroMap.put(container.id, container);
			}
			if (qtItemFisico == 0) {
				if (itemContainer.chooserDescAcresc != null) {
					itemContainer.setChooserDesc(0);
				}
			}
			itemContainer.reposition();
		} catch (ValidationException e) {
			if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() || LavenderePdaConfig.isPermiteEditarDescontoMultiplaInsercao() || LavenderePdaConfig.isPermiteEditarDescontoAcrescimoMultiplaInsercao()) {
				if (itemContainer.edVlItemPedido != null) {
					itemContainer.setEdVlItemPedido(oldVlItemPedido);
				}
				itemPedido.qtItemFisico = oldQtItemFisico;
				itemPedido.vlItemPedido = oldVlItemPedido;
				if (oldVlPctAcrescDesc > 0) {
					itemPedido.vlPctDesconto = oldVlPctAcrescDesc;
					itemPedido.vlPctAcrescimo = 0;
				} else {
					itemPedido.vlPctAcrescimo = oldVlPctAcrescDesc * -1;
					itemPedido.vlPctDesconto = 0;
			}
				if (itemContainer.chooserQtd != null) {
					itemContainer.setChooserQtdValue(oldQtItemFisico);
				}
				if (itemContainer.chooserDescAcresc != null) {
					if (acrescimoAlterado) {
						itemPedido.vlPctAcrescimo = oldVlPctAcrescDesc * -1;
					} else {
						itemPedido.vlPctDesconto = oldVlPctAcrescDesc;
					}
					itemContainer.setChooserDesc(oldVlPctAcrescDesc);
					itemContainer.refreshEnableState(itemPedido);
					if (itemContainerState != null && itemPedido.qtItemFisico == 0) {
						itemContainerState.edVlItemPedidoEnabled = false;
						itemContainerState.edVlBrutoItemPedidoEnabled = false;
					}
				}
			} else if (e.getMessage().contains(MSG_ERRO_ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO)) {
				if (itemFromPedidoList != null) {
					itemContainer.itemPedido.vlPctDesconto = oldVlPctAcrescDesc;
				}
			}
			if (changeValue || changeDesc) {
				clearScreen();
			}
			throw e;
		} finally {
//			setProdutoSelecionado(null);
//			clearProdutoVendaAtual();
//			getItemPedidoService().clearDadosItemPedido(itemPedido);
//			edQtItemFisico.setText(ValueUtil.VALOR_NI);
//			edVlPctDesconto.setText(ValueUtil.VALOR_NI);
//			edVlPctAcrescimo.setText(ValueUtil.VALOR_NI);
//			if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
//				edVlItemPedido.setText(ValueUtil.VALOR_NI);
//			}
			}
		itemContainer.itemPedido = itemPedido;
		return hasModificacoes ? itemPedido : null;
		}

	private boolean isAtribuiOldVlPctAcrescimo(ItemPedido itemPedido) {
		return itemPedido.vlPctAcrescimo > 0 && LavenderePdaConfig.isPermiteEditarDescontoAcrescimoMultiplaInsercao();
	}
	
	private void atualizaTotalizadoresMultiplosItensSemNegociacao() {
		String[] valuesItensPedido = PedidoService.getInstance().getValuesItensPedidoForTotalizadores(itensForTotalizadores);
		String[] valuesItensPedidoLista = PedidoService.getInstance().getValuesItensPedidoForTotalizadores(itensListaForTotalizadores);
		lvQtTotalItensInseridos.setValue(Messages.ITEMPEDIDO_LABEL_TOTALIZADOR_TOTALITENS + " " + valuesItensPedido[0]);
		lvQtTotalItensInseridosLista.setValue(Messages.ITEMPEDIDO_LABEL_TOTALIZADOR_TOTALITENS_LISTAGEM + " " + valuesItensPedidoLista[0]);
		lvQtTotalItensInseridos.reposition();
		lvQtTotalItensInseridosLista.reposition();
		lvVlTotalPedido.setValue(Messages.ITEMPEDIDO_LABEL_TOTALIZADOR_TOTALPEDIDO + " " + valuesItensPedido[1]);
		lvVlTotalPedidoLista.setValue(Messages.ITEMPEDIDO_LABEL_TOTALIZADOR_TOTALPEDIDO_LISTAGEM + " " + valuesItensPedidoLista[1]);
		lvVlTotalPedido.reposition();
		lvVlTotalPedidoLista.reposition();
	}

	private void populaItensForTotalizadores(int startIndex) throws SQLException {
		if (itensForTotalizadores == null) {
			itensForTotalizadores = new HashMap<>();
		}
		if (itensListaForTotalizadores == null) {
			itensListaForTotalizadores = new HashMap<>();
	}
		if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
			if (startIndex == 0) {
				itensListaForTotalizadores.clear();
			}
			int sizeListContainer = listContainer.size();
			int sizeListItemPedidoList = pedido.itemPedidoList.size();
			for (int i = 0; i < sizeListItemPedidoList; i++) {
				ItemPedido item = (ItemPedido) pedido.itemPedidoList.items[i];
				itensForTotalizadores.put(item.cdProduto, item);
				for (int j = startIndex; j < sizeListContainer; j++) {
					ProdutoBase produto = getProdutoFromListContainerByIndex(j);
					if (ValueUtil.valueEqualsIfNotNull(produto, item.getProduto())) {
						itensListaForTotalizadores.put(item.cdProduto, item);
			}
		}
	}
		}
	}
	
	private void insereItemItensForTotalizadores(ItemPedido itemPedido) {
		if (itemPedido.getQtItemFisico() != 0 && itemPedido.cdProduto != null) {
			itensForTotalizadores.put(itemPedido.cdProduto, itemPedido);
			itensListaForTotalizadores.put(itemPedido.cdProduto, itemPedido);
		} else {
			itensForTotalizadores.remove(itemPedido.cdProduto);
			itensListaForTotalizadores.remove(itemPedido.cdProduto);
		}
	}

	private boolean confirmaValorMinimoItemPedido(ItemPedido itemPedido) throws SQLException {
		return UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(Messages.ITEMTABELAPRECO_MSG_VALOR_ABAIXO_MINIMO, itemPedido.getItemTabelaPreco().vlMinItemPedido));
	}

	protected boolean saveItemMultInsercao(BaseListContainer.Item itemControl, ItemContainer itemContainer, ItemPedido itemPedido, boolean fromLastAction, boolean fromSugestao, boolean inserindoErro, boolean showLoadingBox) throws SQLException {
		boolean necessitaAtualizarItem = false;
		boolean deleteItem = false;
		ItemPedido itemPedidoErro = null;
		Vector itensErro = new Vector(1);
		itemPedidoAgrupadorSugPerson = null;
		int creditosGerados = 0;
		int qtItensInseridos = 0;
		double vlInserido = 0;
		conferirInconsistencias = false;

		if (itemPedido == null) return false;
		LoadingBoxWindow msg = null;
		if (showLoadingBox) {
			msg = UiUtil.createProcessingMessage(Messages.SAVE_ITEMS_FROM_ARRAY_MSG);
			msg.popupNonBlocking();
		}
		pedido.isPedidoMultiplaInsercao = true;
		try {
					isAddingFromArray = true;
					itemPedido.naoComparaSeqItem = true;
					if (pedido.itemPedidoList.contains(itemPedido)) {
				if (itemPedido.getQtItemFisico() <= 0) {
							itemPedido.itemChanged = true;
							itemPedido = (ItemPedido)pedido.itemPedidoList.items[pedido.itemPedidoList.indexOf(itemPedido)];
							itemPedido.naoComparaSeqItem = false;
					itemPedido.flTipoEdicao = 0;
					delete(itemPedido, !showLoadingBox);
					deleteItem = true;
					pedido.itemPedidoList.removeElement(itemPedido);
							pedido.atualizaLista = true;
					necessitaAtualizarItem = true;
					itemPedido.getProduto().estoque = itemPedido.estoque;
						} else {
							ItemPedido itemUpdate = (ItemPedido)pedido.itemPedidoList.items[pedido.itemPedidoList.indexOf(itemPedido)];
							itemUpdate = (ItemPedido) itemUpdate.clone();
					double qtItemFisico = itemUpdate.getQtItemFisico();
							double vlPctDesconto = itemUpdate.vlPctDesconto;
					double vlPctAcrescimo = itemUpdate.vlPctAcrescimo;
							double qtEstoqueCliente = itemUpdate.qtEstoqueCliente;
					String cdUnidade = itemUpdate.cdUnidade;
							itemUpdate.setOldQtItemFisico(qtItemFisico);
					itemUpdate.setQtItemFisico(itemPedido.getQtItemFisico());
							itemUpdate.vlPctDesconto = itemPedido.vlPctDesconto;
							itemUpdate.qtEstoqueCliente = itemPedido.qtEstoqueCliente;
					itemUpdate.vlPctAcrescimo = itemPedido.vlPctAcrescimo;
					itemUpdate.cdItemGrade1 = itemUpdate.cdItemGrade2 = itemUpdate.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
					itemUpdate.flTipoEdicao = itemPedido.flTipoEdicao;
					if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
								itemUpdate.clearProdutoUnidadeInfo();
								ItemPedidoService.getInstance().changeUnidadeAlternativa(itemUpdate, itemPedido.cdUnidade);
							}
					if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
								itemUpdate.vlBaseItemTabelaPreco = itemPedido.vlBaseItemTabelaPreco;
								itemUpdate.vlBaseItemPedido  = itemPedido.vlBaseItemPedido;
								itemUpdate.vlBaseCalculoDescPromocional = itemPedido.vlBaseCalculoDescPromocional;
								itemUpdate.vlItemPedido = itemPedido.vlBaseItemPedido;
								itemUpdate.vlUnidadePadrao = itemPedido.vlUnidadePadrao;
								itemUpdate.vlItemPedidoFrete = itemPedido.vlItemPedidoFrete;
								itemUpdate.cdLinha = itemPedido.cdLinha;
								itemUpdate.flValorTabelaAlterado = itemPedido.getItemTabelaPreco().vlUnitario != itemUpdate.vlBaseItemTabelaPreco ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
					} else if (LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
								itemUpdate.vlItemPedido = itemPedido.vlItemPedido;
							}
							if (itemUpdate.qtEmbalagemElementar <= 0) {
								itemUpdate.qtEmbalagemElementar = itemPedido.qtEmbalagemElementar;
							}
							itemUpdate.naoComparaSeqItem = false;
							itemUpdate.itemUpdated = true;
							editingFromList = true;
							ItemPedidoService.getInstance().aplicaQtdElementarItemPedido(itemUpdate, itemPedido.getProdutoUnidade());
							try {
						salvarItemUnitario(itemUpdate, true, false);
						itemPedido = itemUpdate;
					} catch (Throwable e) {
						itemPedido.setQtItemFisico(qtItemFisico);
								itemPedido.vlPctDesconto = vlPctDesconto;
						itemPedido.vlPctAcrescimo = vlPctAcrescimo;
								itemPedido.qtEstoqueCliente = qtEstoqueCliente;
						itemPedido.itemUpdated = itemUpdate.itemUpdated;
						if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
							itemPedido.clearProdutoUnidadeInfo();
							ItemPedidoService.getInstance().changeUnidadeAlternativa(itemPedido, cdUnidade);
						}
								throw e;
							}
					necessitaAtualizarItem = true;
						}
					} else {
				if (itemPedido.getQtItemFisico() > 0) {
							itemPedido.flValorTabelaAlterado = itemPedido.getItemTabelaPreco().vlUnitario != itemPedido.vlBaseItemTabelaPreco ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
							itemPedido.itemChanged = true;
							itemPedido.naoComparaSeqItem = false;
					pedido.atualizaLista = fromLastAction;
					salvarItemUnitario(itemPedido, true, false);
							if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(itemPedido.flTipoCadastroItem)) {
								creditosGerados += itemPedido.qtdCreditoDesc;
							}
				}
			}
							qtItensInseridos++;
							vlInserido += itemPedido.vlTotalItemPedido;
			necessitaAtualizarItem = true;
			itemContainer.setChooserQtdValue(deleteItem ? 0d : itemPedido.qtItemFisico);
			if (itemContainer.chooserDescAcresc != null) {
				itemContainer.setChooserDesc(deleteItem ? 0d : itemPedido.vlPctDesconto);
						}
			if (itemContainer.edVlBrutoItem != null) {
				itemContainer.setVlBrutoItem(deleteItem ? itemContainer.vlBrutoItemOriginal : itemContainer.edVlBrutoItem.getValueDouble());
					}
			if (itemContainer.edVlItemPedido != null) {
				itemContainer.setEdVlItemPedido(deleteItem ? itemContainer.vlItemOriginal : itemContainer.edVlItemPedido.getValueDouble());
				if (deleteItem) {
					itemPedido.vlItemPedido = itemContainer.vlItemOriginal;
				}
			}
		} catch (Throwable e) {
			if (itemPedido.getProduto() != null) {
				itensErro.addElement(new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, e.getMessage()));
			}
					if (LavenderePdaConfig.isFiltraItensComErroNaInsercaoMultiplaItens()) {
				itemPedidoErro = (ItemPedido) itemPedido.clone();
			} else if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
				itemPedidoErro = itemPedido;
					}
			if (!itemPedido.itemUpdated) {
				itemPedido.setQtItemFisico(0);
				itemPedido.vlPctDesconto = pedido.vlPctDescItem;
				itemPedido.vlPctAcrescimo = pedido.vlPctAcrescimoItem;
					}
					itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			itemPedido = calculateItemForGrid(itemPedido);
		} finally {
			if (msg != null) {
				msg.unpop();
			}
			editingFromList = false;
		}

		if (itensErro.size() + pedido.itemPedidoInseridosAdvertenciaList.size() > 0) {
			removeItensComAdvertenciaItensErroList(itensErro, pedido.itemPedidoInseridosAdvertenciaList);
			RelNotificacaoItemWindow relInsercaoForm = new RelNotificacaoItemWindow(itensErro, pedido.itemPedidoInseridosAdvertenciaList, (isInsereMultiplosSemNegociacao() && LavenderePdaConfig.isFiltraItensComErroNaInsercaoMultiplaItens()));
			relInsercaoForm.popup();
			conferirInconsistencias = relInsercaoForm.conferirInconsistencias;
			if (conferirInconsistencias) {
				saveItemMultInsercao(itemControl, itemContainer, itemPedidoErro, false, false, true, false);
			}
			if (LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
				atualizaItemNaListaMultInsercao(itemControl, itemContainer, itemPedido, fromLastAction, false, fromSugestao);
			}
		}
		if (!fromSugestao) {
			showMessageItensInseridos(false, creditosGerados, qtItensInseridos, vlInserido);
		} else if (itemPedidoAgrupadorSugPerson != null) {
			itemPedidoAgrupadorSugPerson.sugPersonCreditosGerados += creditosGerados;
			itemPedidoAgrupadorSugPerson.sugPersonQtItensInseridos += qtItensInseridos;
		}
		if (LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
			VerbaSaldoService.getInstance().enviaVerbaSaldoPedidosAbertos();
		}
		pedido.isPedidoMultiplaInsercao = false;
		if (necessitaAtualizarItem) {
			itemPedido.cdProduto = itemContainer.produto.cdProduto;
			atualizaItemNaListaMultInsercao(itemControl, itemContainer, itemPedido, fromLastAction, false, fromSugestao);
		}
		if (LavenderePdaConfig.isExibeTotalizadoresMultiplaInsercao()) {
			insereItemItensForTotalizadores((ItemPedido) itemPedido.clone());
			atualizaTotalizadoresMultiplosItensSemNegociacao();
		}

		return necessitaAtualizarItem;
	}

	public void showMessageItensInseridos(boolean fromSugPerson, int creditosGerados, int qtItensInseridos, double vlInserido) {
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() && qtItensInseridos > 0 && fromSugPerson) {
			String msgParteFinal = ".";
			if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
				msgParteFinal = " " + MessageUtil.getMessage(Messages.MULTIPLASSUGESTOES_MSG_PARCIAL_CRED_DESC, creditosGerados);
			}
			String message = fromSugPerson ? Messages.MULTIPLASSUGESTOES_MSG_INSERCAO_SUGPERSON : Messages.ITEMPEDIDO_MSG_INSERCAO_MULTIPLA;
			UiUtil.showWarnMessage(MessageUtil.getMessage(message, new String[] {String.valueOf(qtItensInseridos), StringUtil.getStringValueToInterface(vlInserido) , msgParteFinal}));
		}
		}

	private void removeItensComAdvertenciaItensErroList(Vector itensErro, Vector itensAdvertencia) {
		int size = itensAdvertencia.size();
		for (int i = 0; i < size; i++) {
			if (itensErro.contains(itensAdvertencia.items[i])) {
				itensAdvertencia.removeElement(itensAdvertencia.items[i]);
	}
		}
	}
	
	@Override
	public void onFormExibition() throws SQLException {
		if (!dontRefreshItemsOnNextExhibition) {
			nuAcaoThreadMultInsercao = 0;
			defineNuSeqItemPedidoAoIniciar(pedido);
		if (fromSugestaoMultProdutos) {
			fromSugestaoMultProdutos = false;
			btSugestaoMultProdutosClick();
		}
		if (!isAddingFromArray && pedido.atualizaLista) {
				atualizaListaItensPedidoAlteracaoExclusao();
		}
			populaEAtualizaTotalizadoresMultItensSemNegociacao(0);
		} else {
			dontRefreshItemsOnNextExhibition = false;
		}
		super.onFormExibition();
	}

	private void atualizaItemNaListaMultInsercao(BaseListContainer.Item containerRefresh, ItemContainer itemContainer, ItemPedido itemPedido, boolean refreshListInstance, boolean showLoadingMessage, boolean fromSugestao) throws SQLException {
		if (containerRefresh != null) {
			if (!fromSugestao) {
				setPropertiesInRowList(containerRefresh, itemPedido.getProduto());
			}
			if (itemContainer != null) {
				ItemPedido itemPedidoRefresh = itemContainer.itemPedido != null ? itemContainer.itemPedido : itemPedido;
				itemContainer.setValuesOnList(itemPedidoRefresh);
				if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
					itemContainer.cbUnidadeAlternativa.setValue(itemPedidoRefresh.cdUnidade, itemPedidoRefresh.cdProduto, itemPedidoRefresh.cdItemGrade1);
				}
			}
		} else {
			int size = pedido.itemPedidoList.size();
			int sizeListCon = refreshListInstance && instance != null && instance.listContainer != null ? instance.listContainer.size() : listContainer.size();
			LoadingBoxWindow msg = null;
		try {
				if (showLoadingMessage) {
					msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
				}
			for (int i = 0; i < size; i++) {
					for (int k = 0; k < sizeListCon; k++) {
						BaseListContainer.Item c = refreshListInstance && instance != null && instance.listContainer != null ? (BaseListContainer.Item) instance.listContainer.getContainer(k) : (BaseListContainer.Item) listContainer.getContainer(k);
						if (!c.id.equals(itemPedido.getProduto().getRowKey())) continue;
						if (!fromSugestao) {
							try {
								setPropertiesInRowList(c, itemPedido.getProduto());
							} catch (Throwable e) {
								ExceptionUtil.handle(e);
							}
						}
					if (itemPedido.itemChanged) {
							ItemContainer item = (ItemContainer)c.rightControl;
						if (item == null) continue;
								item.setValuesOnList(itemPedido);
								if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
									item.cbUnidadeAlternativa.setValue(itemPedido.cdUnidade, itemPedido.cdProduto, itemPedido.cdItemGrade1);
								}
							}
						}
				itemPedido.itemChanged = false;
					}
		} finally {
				if (msg != null) {
			msg.unpop();
		}
			}
		}
		pedido.atualizaLista = false;
	}
	
	private int getCheckedItensSize() {
		return listContainer.checkedItens != null ? listContainer.getCheckedItens().length : 0;
	}
	
	public void loadSugVendaPersonWindow() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				pedido.cdTabPrecoFilterSug = cbTabelaPreco.getValue();
				if (ValueUtil.isEmpty(listSugVendaPerson) || LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
					listSugVendaPerson = SugVendaPersonService.getInstance().findProdutosSugVendaPerson(pedido, listSugVendaPerson);
				}
				if (listSugVendaPerson.size() > 0) {
					ListSugestaoMultProdutosWindow listSugestaoMultProdutosWindow = new ListSugestaoMultProdutosWindow(pedido, listSugVendaPerson);
					listSugestaoMultProdutosWindow.setCadItemPedidoForm(this);
					this.listSugestaoPersonMultProdutosWindow = listSugestaoMultProdutosWindow;
					listSugestaoMultProdutosWindow.popup();
				}
			} finally {
				msg.unpop();
			}
		}
	}
	
	public void invalidateListSugPerson() {
		listSugestaoPersonMultProdutosWindow = null;
		listSugVendaPerson = null;
	}
	
	private void sugereItemCombo() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (ItemComboService.getInstance().isItemPedidoPertenceCombo(itemPedido, ultimoCdProduto, null, null)) {
			Vector domainList = ItemComboService.getInstance().findProdutosSugeridosByCombo(itemPedido.pedido, ultimoCdProduto, null, null, false);
			if (domainList.size() > 0) {
				ItemCombo itemCombo = ItemComboService.getInstance().getItemComboComCdProduto(pedido.cdEmpresa, pedido.cdRepresentante, ultimoCdProduto);
				new ListItemComboSugestaoWindow(pedido, itemCombo, this, domainList, false).popup();
			}
		}
	}

	private void reorganizarLista() throws SQLException {
		Container[] vetor = new Container[itensErro.size()];
		int pos = 0;
		Produto prodFilter = new Produto();
		prodFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		prodFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		for (int j = 0; j < itensErro.size(); j++) {
			prodFilter.cdProduto = ((ProdutoErro) itensErro.items[j]).cdProduto;
			listContainer.setSelectedItemById(prodFilter.getRowKey());
				vetor[pos++] = listContainer.getSelectedItem();
			}
		listContainer.removeAllContainers();
		listContainer.addContainers(vetor);
	}
 	
	private void btSugestaoMultProdutosClick() throws SQLException {
		Produto produto = getItemPedido().getProduto();
		produto = produto == null ? getSelectedProduto() : produto;
		if (isDeveCarregarVisualizacaoMultiplasSugestoesProdutos(produto)) {
			if (produto == null) {
				UiUtil.showInfoMessage(Messages.PRODUTO_NENHUM_SELECIONADO);
				return;
					}
			listMultiplasSugestoesProdutosVisualizacaoWindow = new ListMultiplasSugestoesProdutosWindow(pedido, produto, getTabelaPrecoSelecionada(), true);
			listMultiplasSugestoesProdutosVisualizacaoWindow.setCadItemPedidoForm(this);
		} else {
			listMultiplasSugestoesProdutosVisualizacaoWindow.list();
		}
		if (listMultiplasSugestoesProdutosVisualizacaoWindow.hasSugestoes()) {
			listMultiplasSugestoesProdutosVisualizacaoWindow.popup();
			afterShowMultSugesProdutosWindow(listMultiplasSugestoesProdutosVisualizacaoWindow);
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTO_SEM_SUGESTAO_MULTIPLOS);
			invalidateListMultSugestoesProd();
		}
	}
	
	protected void btInfoProdutosClick(BaseDomain produto) throws SQLException {
		if (cadProdutoDynForm == null) {
			cadProdutoDynForm = new CadProdutoDynForm();
		}
		cadProdutoDynForm.edit(produto);
		show(cadProdutoDynForm);
	}
	
	public void invalidateListMultSugestoesProd() {
		listMultiplasSugestoesProdutosVisualizacaoWindow = null;
	}
	
	private void updateHeightContainersForCategoria(int delta) {
		Control[] controls = containerGrid.getChildren();
		changeControlsYForMenuCategoria(controls, delta);
		if (listContainer.getY() + delta < Settings.screenHeight) {
			listContainer.setRect(0, listContainer.getY() + delta, FILL, (containerTotalizadoresMultIns.isVisible() ? FILL - containerTotalizadoresMultIns.getHeight() : FILL));
}
		try {
			listContainer.initUI();
		} catch (Throwable e) {
			// não coloca os totalizadores na tela pois não tem espaço
		}
		listContainer.repaintContainers();
	}

	private void refreshContainerHeightForMenuCategoria() {
		Control[] controls = menuCategoriaScroll.getBagChildren();
		int qtButtonCategoriaCurrent = controls != null ? controls.length : 0;
		if (qtButtonCategoriaCurrent > LavenderePdaConfig.getQtdCategoriaPadrao()) {
			qtButtonCategoriaCurrent = LavenderePdaConfig.getQtdCategoriaPadrao();
		}
		if (this.menuCategoriaHidden) {
			qtButtonCategoriaCurrent = 0;
		}
		menuCategoriaScroll.wasLandscape = Settings.isLandscape();
		int delta =  -(ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * (LavenderePdaConfig.getQtdCategoriaPadrao() - qtButtonCategoriaCurrent));
		updateHeightContainersForCategoria(delta);
	}

	private void toggleVisibilityMenuCategoria() {
		menuCategoriaScroll.setVisible(menuCategoriaHidden);
		menuCategoriaScroll.setEnabled(menuCategoriaHidden);
		int current = menuCategoriaScroll.getBagChildren() != null ? menuCategoriaScroll.getBagChildren().length : 0;
		int buttonHeight;
		if (current < LavenderePdaConfig.getQtdCategoriaPadrao()) {
			buttonHeight = ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * current;
		} else {
			buttonHeight = ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * LavenderePdaConfig.getQtdCategoriaPadrao();
		}
		if (!menuCategoriaHidden) {
			buttonHeight = -buttonHeight;
		}
		updateHeightContainersForCategoria(buttonHeight);
		menuCategoriaHidden = !menuCategoriaHidden;
	}

	protected void adjustContainerHeightOnClick() {
		Control[] controls = containerGrid.getChildren();
		int currentQtButtonCategoria = menuCategoriaScroll.getBagChildren() != null ? menuCategoriaScroll.getBagChildren().length : 0;
		int delta;
		int currentQt = Math.min(currentQtButtonCategoria, LavenderePdaConfig.getQtdCategoriaPadrao());
		int previousQt = Math.min(qtButtonCategoriaPrevious, LavenderePdaConfig.getQtdCategoriaPadrao());
		if (previousQt < currentQt) {
			delta = ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * (currentQt - previousQt);
		} else if (previousQt > currentQt) {
			delta = -(ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * (previousQt - currentQt));
		} else {
			return;
		}
		changeControlsYForMenuCategoria(controls, delta);
		if (menuCategoriaHidden) {
			delta = -(ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * LavenderePdaConfig.getQtdCategoriaPadrao());
		}
		updateListContainerRectMenuCategoria(delta);
	}

	private void updateListContainerRectMenuCategoria(int delta) {
		if (listContainer.getY() + delta < Settings.screenHeight) {
			listContainer.setRect(0, listContainer.getY() + delta, FILL, (containerTotalizadoresMultIns.isVisible() ? FILL - containerTotalizadoresMultIns.getHeight() : FILL));
		}
		listContainer.initUI();
		listContainer.repaintContainers();
	}

	@Override
	protected void adjustContainerHeightForMenuCategoria() {
		Control[] controls = containerGrid.getChildren();
		int currentQtButtonCategoria = menuCategoriaScroll.getBagChildren() != null ? menuCategoriaScroll.getBagChildren().length : 0;
		int delta;
		int currentQt = Math.min(currentQtButtonCategoria, LavenderePdaConfig.getQtdCategoriaPadrao());
		if (menuCategoriaHidden) {
			delta = -(ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * LavenderePdaConfig.getQtdCategoriaPadrao());
		} else {
			delta = -(ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * (LavenderePdaConfig.getQtdCategoriaPadrao() - currentQt));
		}
		if (menuCategoriaScroll.wasLandscape != Settings.isLandscape() && !menuCategoriaScroll.backToList) {
			menuCategoriaScroll.wasLandscape = !menuCategoriaScroll.wasLandscape;
			changeControlsYForMenuCategoria(controls, delta);
		}
		if (menuCategoriaScroll.backToList) {
			changeControlsYForMenuCategoria(controls, delta);
		}
		boolean land = Settings.isLandscape() && menuCategoriaScroll.orientation == MenuCategoriaScrollContainer.LANDSCAPE;
		boolean port = !Settings.isLandscape() && menuCategoriaScroll.orientation == MenuCategoriaScrollContainer.PORTRAIT;
		if ((land || port) && !menuCategoriaScroll.backToList && !menuCategoriaScroll.fromItensClick && menuCategoriaScroll.canReposition) {
			changeControlsYForMenuCategoria(controls, delta);
			menuCategoriaScroll.canReposition = false;
		}
		menuCategoriaScroll.orientation = 0;
		menuCategoriaScroll.fromItensClick = false;
		menuCategoriaScroll.backToList = false;
		updateListContainerRectMenuCategoria(delta);
		if (edFiltro.getY() > listContainer.getY() && delta < 0) {
			changeControlsYForMenuCategoria(controls, delta);
		}
		if (edFiltro.getY() < menuCategoriaScroll.getY() && delta < 0) {
			changeControlsYForMenuCategoria(controls, -delta);
		}
	}

	private void changeControlsYForMenuCategoria(Control[] controls, int delta) {
		if (controls != null) {
			for (int i = 0; i < controls.length; i++) {
				Control control = controls[i];
				if (control == listContainer) {
					continue;
				}
				if (control == menuCategoriaScroll) {
					break;
				}
				control.setRect(control.getX(), control.getY() + delta, control.getWidth(), control.getHeight());
			}
		}
	}

	@Override
	public void resizeListToFullScreen() {
		super.resizeListToFullScreen();
		if (LavenderePdaConfig.usaCategoriaInsercaoItem() && menuCategoriaScroll.orientation == 0) {
			menuCategoriaScroll.orientation = Settings.isLandscape() ? MenuCategoriaScrollContainer.LANDSCAPE : MenuCategoriaScrollContainer.PORTRAIT;
		}
	}

	private void backToListMenuCategoria() {
		if (LavenderePdaConfig.usaCategoriaInsercaoItem() && listMaximized) {
			menuCategoriaScroll.backToList = true;
		}
	}

	private void loadMenuCategoriaInicial() throws SQLException {
		ButtonMenuCategoria rootTreeNodeCategoria = new ButtonMenuCategoria();
		buttonMenuCategoriaTree = new Tree<>(rootTreeNodeCategoria);
		currentButtonMenuCategoriaTree = buttonMenuCategoriaTree;
		MenuCategoriaService.getInstance().populateTree(buttonMenuCategoriaTree);
		List<Tree<ButtonMenuCategoria>> subTrees = buttonMenuCategoriaTree.getSubTrees();
		loadListIntoMenuCategoriaScroll(subTrees);
		updateWidthButtonsMenuCategoria();
	}

	private void loadMenuCategoriaOnClick(ButtonMenuCategoria clickedButton) {
		List<Tree<ButtonMenuCategoria>> subTrees = buttonMenuCategoriaTree.getTree(clickedButton).getSubTrees();
		currentButtonMenuCategoriaTree = buttonMenuCategoriaTree.getTree(clickedButton);
		if (currentButtonMenuCategoriaTree.getParent() == null) {
			currentButtonMenuCategoriaTree = null;
		}
		qtButtonCategoriaPrevious = menuCategoriaScroll.getBagChildren() != null ? menuCategoriaScroll.getBagChildren().length : 0;
		menuCategoriaScroll.scrollToPage(0);
		menuCategoriaScroll.clear();
		menuCategoriaScroll.removeAll();
		updateCurrenteCategoriaText();
		loadListIntoMenuCategoriaScroll(subTrees);
	}

	private void updateCurrenteCategoriaText() {
		List<String> categoriaTextList = MenuCategoriaService.getInstance().getCategoriaAtualText(currentButtonMenuCategoriaTree);
		String fullPath = MenuCategoriaService.getInstance().getFullTextPath(categoriaTextList);
		lvCurrentCategoria.setValue(MenuCategoriaService.getInstance().getCategoriaAtualShortText(categoriaTextList, lvCurrentCategoria.getWidth(), fm));
		menuCategoriaAtualToolTip.setText(fullPath);
		boolean visible = ValueUtil.isNotEmpty(fullPath);
		lbCurrentCategoria.setVisible(visible);
		lvCurrentCategoria.setVisible(visible);
	}

	private void loadListIntoMenuCategoriaScroll(List<Tree<ButtonMenuCategoria>> subTrees) {
		int size = subTrees.size();
		for (int i = size - 1; i >= 0; i--) {
			ButtonMenuCategoria container = subTrees.get(i).head;
			menuCategoriaScroll.add(container);
		}
	}

	private void menuCategoriaClick(ButtonMenuCategoria clicked) throws SQLException {
		if (clicked == dynamicButtonMenuCategoria) {
			if (menuCategoriaHidden) {
				openButtonsCategoria();
			} else {
				if (currentButtonMenuCategoriaTree != null && currentButtonMenuCategoriaTree.getParent() != null) {
					nextButtonCategoria(currentButtonMenuCategoriaTree.getParent().head);
				} else {
					closeButtonsCategoria();
				}
			}
		} else {
			nextButtonCategoria(clicked);
		}
	}

	private void nextButtonCategoria(ButtonMenuCategoria button) throws SQLException {
		loadMenuCategoriaOnClick(button);
		adjustContainerHeightOnClick();
		updateWidthButtonsMenuCategoria();
		addScrollSpacerButtonsMenuCategoria();
		if (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == ValorParametro.PARAM_INT_VALOR_ZERO) {
			btFiltrarClick();
		}
	}

	private void addScrollSpacerButtonsMenuCategoria() {
		Control[] controls = menuCategoriaScroll.getBagChildren();
		if (controls != null) {
			int spacer = 0;
			if (controls.length > LavenderePdaConfig.getQtdCategoriaPadrao()) {
				spacer = (int) -(UiUtil.getButtonPreferredHeight() * 0.35);
				menuCategoriaScroll.canPaintScrollBar = true;
			} else {
				menuCategoriaScroll.canPaintScrollBar = false;
			}
			menuCategoriaScroll.reposition();
			for (int i = 0; i < controls.length; i++) {
				ButtonMenuCategoria button = (ButtonMenuCategoria) controls[i];
				button.setRect(button.getX(), button.getY(), Control.FILL + spacer, button.getHeight());
			}
		} else {
			menuCategoriaScroll.canPaintScrollBar = false;
		}
	}

	private void closeButtonsCategoria() {
		toggleVisibilityMenuCategoria();
		toggleSizeDynamicButtonMenuCategoria();
		updateCurrenteCategoriaText();
	}

	private void openButtonsCategoria() throws SQLException {
		toggleVisibilityMenuCategoria();
		updateCurrenteCategoriaText();
		updateWidthButtonsMenuCategoria();
		addScrollSpacerButtonsMenuCategoria();
		toggleSizeDynamicButtonMenuCategoria();
		if (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == ValorParametro.PARAM_INT_VALOR_ZERO) {
			btFiltrarClick();
		}
	}

	private void toggleSizeDynamicButtonMenuCategoria() {
		if (dynamicButtonMenuCategoria.isBtVoltar) {
			if (dynamicButtonMenuCategoria.usaBtPrincipal) {
				dynamicButtonMenuCategoria.setTextImage(dynamicButtonMenuCategoria.principalText, dynamicButtonMenuCategoria.principalImage);
			} else {
				dynamicButtonMenuCategoria.setTextImage(Messages.MENU_CATEGORIA_LABEL, null);
			}
		} else {
			int imageSize = UiUtil.getLabelPreferredHeight();
			dynamicButtonMenuCategoria.setTextImage(FrameworkMessages.BOTAO_VOLTAR, UiUtil.getColorfulImage("images/back.png", imageSize, imageSize));
		}
		dynamicButtonMenuCategoria.setRect(dynamicButtonMenuCategoria.getX(), dynamicButtonMenuCategoria.getY(), dynamicButtonMenuCategoria.isBtVoltar ? getWFill() : dynamicButtonMenuCategoria.defaultBtVoltarWidth, ButtonMenuCategoria.FIXED_BT_VOLTAR_HEIGHT);
		dynamicButtonMenuCategoria.isBtVoltar = !dynamicButtonMenuCategoria.isBtVoltar;
	}

	private boolean autoClickMenuCategoria() {
		if (LavenderePdaConfig.usaCategoriaInsercaoItem()) {
			Control[] controls = menuCategoriaScroll.getBagChildren();
			return (controls == null || controls.length == 0) && !menuCategoriaHidden;
		} else {
			return true;
		}
	}

	private void updateWidthButtonsMenuCategoria() {
		if (!menuCategoriaHidden) {
			dynamicButtonMenuCategoria.setRect(dynamicButtonMenuCategoria.getX(), dynamicButtonMenuCategoria.getY(), dynamicButtonMenuCategoria.defaultBtVoltarWidth, ButtonMenuCategoria.FIXED_BT_VOLTAR_HEIGHT);
		}
		setRectButtonMenuCategoria();
	}

	private void setRectButtonMenuCategoria() {
		Control[] controls = menuCategoriaScroll.getBagChildren();
		if (controls != null) {
			for (int i = 0; i < controls.length; i++) {
				ButtonMenuCategoria buttonMenuCategoria = (ButtonMenuCategoria) controls[i];
				buttonMenuCategoria.setRect(getLeft(), (i * ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT), FILL, ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT_SPACER);
			}
		}
	}

	private boolean showConfirmMessageDeleteItemCombo(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isCombo()) {
			Combo combo = ComboService.getInstance().getComboByItemPedido(itemPedido);
			boolean deletaCombo = ItemComboService.getInstance().isItemPedidoPertenceCombo(itemPedido, itemPedido.cdProduto, null, itemPedido.cdCombo);
			if (combo != null && deletaCombo) {
				return UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(Messages.MSG_EXCLUSAO_COMBO, combo.toString()));
			}
		}
		return true;
	}

	private boolean deleteCombo(ItemPedido itemPedido) throws SQLException {
		if (!itemPedido.isCombo()) {
			return delete();
		} else if (!showConfirmMessageDeleteItemCombo(itemPedido)) {
			return false;
		} else {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			try {
				msg.popupNonBlocking();
				delete(screenToDomain());
			} finally {
				msg.unpop();
			}
			return true;
		}
	}

	private void cbDescProgressivoConfigChange() {
		cbDescProgFamilia.removeAll();
		if (cbDescProgressivoConfig.getSelectedIndex() == 0) {
			cbDescProgConfigFam.removeAll();
		} else {
			cbDescProgConfigFam.load(true);
			cbDescProgConfigFam.setSelectedIndex(1);
		}
	}

	private void cbDescProgConfigFamChange() throws SQLException {
		if (cbDescProgConfigFam.getSelectedIndex() == 0) {
			cbDescProgFamilia.removeAll();
		} else {
			boolean tipoConsome = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_CONSOME);
			boolean tipoProduz = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_PRODUZ);
			boolean tipoAcumula = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_ACUMULA);
			boolean tipoAcumulaMax = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_ACUMULA_MAX);
			cbDescProgFamilia.load(cbDescProgressivoConfig.getValue(), tipoConsome, tipoProduz, tipoAcumula, tipoAcumulaMax);
			cbDescProgFamilia.setSelectedIndex(0);
		}
	}

	private void btAgrupadorSimilaridadeClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.getProduto().similaresList = null;
		Produto produto = itemPedido.getProduto();
		if (produto.itemTabelaPreco == null) {
			produto.itemTabelaPreco = new ItemTabelaPreco();
			produto.itemTabelaPreco.cdTabelaPreco = pedido.cdTabelaPreco;
		}
		ListItemProdutoAgrupadorWindow window = new ListItemProdutoAgrupadorWindow(itemPedido);
		window.popup();
		if (window.saved) {
			ItemPedidoAgrSimilarService.getInstance().updateItensPedidoAgrSimilar(itemPedido);
			updateQtItens(itemPedido, true, false);
			ListItemPedidoForm listItemPedidoForm = (ListItemPedidoForm) getBaseCrudListForm();
			if (listItemPedidoForm != null) {
				pedido.itemPedidoList.removeElement(itemPedido);
				pedido.itemPedidoList.addElement(itemPedido);
				listItemPedidoForm.list();
				listItemPedidoForm.updateCurrentRecord(itemPedido);
			}
		}
	}

	private boolean isPedidoComProdutoSimilaridadeAutorizado(ItemPedido itemPedido) throws SQLException {
		Produto produto = itemPedido.getProduto();
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() &&
				ValueUtil.isNotEmpty(produto.cdAgrupadorSimilaridade) && ItemPedidoAgrSimilarService.getInstance().isItemPedidoInseridoSimilarAutorizado(itemPedido)) {
			UiUtil.showErrorMessage(Messages.ERRO_PEDIDO_POSSUI_PRODUTO_AGRUPADOR_AUTORIZADO);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void btAgrupadorGradeClick() throws SQLException {
		if (isInserindoItems()) {
			UiUtil.showInfoMessage(Messages.AGUARDE_PROCESSANDO_ITENS, UiUtil.DEFAULT_MESSAGETIME_BIG);
		} else {
			super.btAgrupadorGradeClick();
		}
	}

//	protected void edQtItemFisicoValueChange() throws SQLException {
//		ItemPedido itemPedido = getItemPedido();
//		if (LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && !PedidoService.getInstance().possuiCondicaoComercial(pedido)) {
//			processaDescGrupoProdQtItemFisicoChange(itemPedido);
//		}
//	}

	private void processaDescGrupoProdQtItemFisicoChange(ItemPedido itemPedido) throws SQLException {
		double oldDesc = itemPedido.vlPctDesconto;
		DescontoGrupoService.getInstance().aplicaDescontoDescGrupoProduto(itemPedido, pedido);
		edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
		edVlPctDescontoValueChange(itemPedido, true);
		if (oldDesc != itemPedido.vlPctDesconto) {
			descontoQtdeGrupoProdutoList = DescontoGrupoService.getInstance().calcDescQtdeGrupoUnidadeAlternativa(getDescontoQtdeGrupoProdutoList(), getItemPedido());
			showDescQtdeGrupoProdutoWindow(itemPedido, true, false);
		}
	}

	@Override
	protected boolean isInserindoItems() {
		return (insercaoMultiplaThread != null && insercaoMultiplaThread.isRunning())
				|| (filaMultiplaInsercao != null && ! filaMultiplaInsercao.isEmpty());
	}

	private boolean isListandoSemGradeOuEditandoGrade() {
		return !isFiltrandoAgrupadorGrade();
	}

	private LinkedList<ThreadItem> filaMultiplaInsercao;
	private InsercaoMultiplaThread insercaoMultiplaThread;
	private static Lock lock = new Lock();

	private class InsercaoMultiplaThread extends Thread {

		private boolean running;

		public boolean isRunning() {
			return running;
		}

		@Override
		public void run() {
			running = true;
			Vm.sleep(1000);
			try {
				ThreadItem threadItem;
				synchronized (lock) {
					threadItem = filaMultiplaInsercao.poll();
				}
				if (threadItem != null && !(threadItem.ctrlParent instanceof ProdutoUnidadeComboBox)) {
					Vm.sleep(1000);
				}
				while (threadItem != null && verificaMesmoItemNaFila(threadItem)) {
					threadItem = substituiItemMaisAtual(threadItem);
					Vm.sleep(1500);
				}
				while (threadItem != null) {
					String key = threadItem.key;
					Item itemControl = threadItem.itemControl;
					ItemContainer itemContainer = threadItem.itemContainer;
					Control ctrlParent = threadItem.ctrlParent;
					while (verificaTecladoAberto()) {
						Vm.sleep(1000);
					}
					if (verificaEditContainerAtualComFoco(itemContainer)) {
						threadItem = preparaProximaIteracao(key);
						continue;
					}
					if (VmUtil.isWin32()) {
						boolean[] wait = {true};
						MainLavenderePda.getInstance().runOnMainThread(new Runnable() {

							@Override
							public void run() {
								iniciaESalvaItemVenda(key, itemContainer, itemControl, ctrlParent, wait);
							}

						}, false);
						int timeout = 0;
						while (wait[0] && timeout < 1000) {
							timeout++;
							Vm.sleep(20);
						}
					} else {
						boolean[] wait = {false};
						iniciaESalvaItemVenda(key, itemContainer, itemControl, ctrlParent, wait);
					}
					threadItem = preparaProximaIteracao(key);
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				showErrorMsg("Erro inesperado. " + StringUtil.getStringValue(e.getMessage()));
			} finally {
				try {
					running = false;
					boolean[] wait = {true};
					MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
						@Override
						public void run() {
							try {
								processaNuAcaoThreadMultInsercao();
								if (filtrandoItemEventoEmProgresso) {
									try {
										filtrarClick();
									} catch (SQLException e) {
										ExceptionUtil.handle(e);
									}
									filtrandoItemEventoEmProgresso = false;
								}
							} finally {
								wait[0] = false;
							}
						}
					});
					int timeout = 0;
					while (wait[0] && timeout < 200) {
						timeout++;
						Vm.sleep(20);
					}
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}

		private void iniciaESalvaItemVenda(String key, ItemContainer itemContainer, Item itemControl, Control ctrlParent, boolean[] wait) {
			LoadingBoxWindow msg = null;
			if (wait[0]) {
				msg = UiUtil.createProcessingMessage();
				msg.popupNonBlocking();
			}
			ItemContainerControlState itemContainerState = new ItemContainerControlState(itemContainer);
			try {
				itemContainer.disableAllControls();
				ItemPedido itemPedido = iniciaItemVendaMultInsercao(itemContainerState, itemContainer, ctrlParent instanceof ProdutoUnidadeComboBox, ctrlParent == itemContainer.chooserQtd, itemContainer.changedValue, ctrlParent == itemContainer.chooserDescAcresc, false);
				if (itemPedido != null) {
					houveAlteracaoItemPorMultInsercao = saveItemMultInsercao(itemControl, itemContainer, itemContainer.itemPedido, false, false, false, false);
				}
				itemContainer.changedValue = false;
				if (itemContainer.chooserQtd.getValue() == 0) {
					itemContainer.itemPedido = null;
					if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
						defineNuSeqItemPedidoAoIniciar(pedido);
					}
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				String msgErro = StringUtil.getStringValue(e.getMessage());
				if (e instanceof ValidationException && e.getMessage().equals(Messages.PRODUTO_MSG_BLOQUEADO)) {
					if (itemContainer.chooserQtd.getValue() > 0) {
						showErrorMsg(msgErro);
					}
				} else {
					String dsProdutoErro = key;
					try {
						Produto produto = itemContainer.itemPedido.getProduto();
						dsProdutoErro = produto.toString();
					} catch (SQLException e1) {
						ExceptionUtil.handle(e1);
					}
					showErrorMsg("Erro ao inserir o produto " + dsProdutoErro + ". " + msgErro);
				}
			} finally {
				try {
					itemContainer.setControlsEnabledByState(itemContainerState);
					ItemPedido itemPedido = itemContainer.itemPedido;
					if (itemPedido != null) {
						itemContainer.refreshChooserDescAcresc(itemPedido);
					}

					if (filaMultiplaInsercao.isEmpty()) {
						itemContainer.repaintNow();
					}
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				} finally {
					if (wait[0]) {
						msg.unpop();
						wait[0] = false;
					}
				}
			}
		}

		private ThreadItem preparaProximaIteracao(String key) {
			ThreadItem threadItem;
			synchronized (lock) {
				threadItem = filaMultiplaInsercao.poll();
			}
			if (threadItem != null && key.equals(threadItem.key)) {
				do {
					Vm.sleep(1500);
					threadItem = substituiItemMaisAtual(threadItem);
				} while (threadItem != null && verificaMesmoItemNaFila(threadItem));
			}
			return threadItem;
		}

		private boolean verificaEditContainerAtualComFoco(ItemContainer itemContainer) {
			if (getParentWindow() == null) {
				return false;
			}
			Control f = getParentWindow().getFocus();
			return f != null && (f == itemContainer.edVlItemPedido
						|| (itemContainer.chooserQtd != null && f == itemContainer.chooserQtd.edF)
						|| (itemContainer.chooserDescAcresc != null && f == itemContainer.chooserDescAcresc.edF));
		}

		private boolean verificaTecladoAberto() {
			return MainLavenderePda.getInstance().verificaTecladoAberto();
		}

		private boolean verificaMesmoItemNaFila(ThreadItem threadItem) {
			int indexOf = filaMultiplaInsercao.indexOf(threadItem);
			return indexOf != -1;
		}

		private ThreadItem substituiItemMaisAtual(ThreadItem threadItem) {
			synchronized (lock) {
				int indexOf = filaMultiplaInsercao.indexOf(threadItem);
				if (indexOf != -1) {
					ThreadItem threadItemNew = filaMultiplaInsercao.get(indexOf);
					if (threadItemNew != null) {
						threadItem = threadItemNew;
						filaMultiplaInsercao.remove(indexOf);
					}
				}
			}
			return threadItem;
		}

		private void showErrorMsg(final String msg) {
			MainLavenderePda.getInstance().runOnMainThread(new Runnable() {
				@Override
				public void run() {
					UiUtil.showErrorMessage(msg);
				}
			});
		}

	}

	private class ThreadItem {

		protected String key;
		protected Item itemControl;
		protected ItemContainer itemContainer;
		protected Control ctrlParent;

		public ThreadItem(String key, Item itemControl, ItemContainer itemContainer, Control ctrlParent) {
			this.key = key;
			this.itemControl = itemControl;
			this.itemContainer = itemContainer;
			this.ctrlParent = ctrlParent;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null) {
				ThreadItem objT = (ThreadItem) obj;
				return ValueUtil.valueEquals(objT.key, this.key);
			}
			return false;
		}
	}

	private void btGerarCatalogoClick() throws SQLException {
		(new CatalogoProdutoWindow()).popup();
	}

	private void showPopUpRelDiferencasDescProgressivo(Pedido pedido) {
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			if (pedido.recalculoDescontoProgressivoDTO != null && ValueUtil.isNotEmpty(pedido.recalculoDescontoProgressivoDTO.listItemDescontoDTO)) {
				new RelDiferencasDescontoProgressivoWindow(pedido.recalculoDescontoProgressivoDTO).popup();
			}
		}
	}

	private void populaEAtualizaTotalizadoresMultItensSemNegociacao(int startIndex) throws SQLException {
		if (LavenderePdaConfig.isExibeTotalizadoresMultiplaInsercao()) {
			populaItensForTotalizadores(startIndex);
			atualizaTotalizadoresMultiplosItensSemNegociacao();
		}
	}

	private void btGroupTipoFiltrosClick(int indexSelected) {
		filterByPrincipioAtivo = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_PA);
		filterByAplicacaoProduto = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_AP);
		if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			filterByCodigoProduto = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_CD);
		}
		alterarTipoTeclado();
	}

	private void prepareCestaProdutoFilter(ProdutoBase produtoFilter) {
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos() && ValueUtil.isNotEmpty(cbCesta.getValue())) {
			produtoFilter.cestaProdutoFilter = new CestaProduto();
			produtoFilter.cestaProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoFilter.cestaProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CestaProduto.class);
			produtoFilter.cestaProdutoFilter.cdCesta = cbCesta.getValue();
			
			if (Messages.NAO_POSITIVADOS.equals(produtoFilter.dsPositivados)) {
				produtoFilter.cestaPositProdutoFilter = new CestaPositProduto();
				produtoFilter.cestaPositProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				produtoFilter.cestaPositProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CestaPositProduto.class);
				produtoFilter.cestaPositProdutoFilter.cdCesta = cbCesta.getValue();
				produtoFilter.cestaPositProdutoFilter.cdCliente = pedido.cdCliente;
			}
		}
	}
	
	protected void prepareProdutoMenuCategoriaFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.usaCategoriaInsercaoItem() && !menuCategoriaHidden) {
			produtoFilter.produtoMenuCategoriaFilter = ProdutoMenuCategoriaService.getInstance().getProdutoMenuCategoriaFilter(currentButtonMenuCategoriaTree);
			if (ValueUtil.isEmpty(produtoFilter.produtoMenuCategoriaFilter.cdMenuListFilter)) {
				throw new FilterNotInformedException();
			}
		}
	}
	
	@Override
	protected void aposCarregarMaisProdutos(Vector produtoList) {
		super.aposCarregarMaisProdutos(produtoList);
		try {
			populaEAtualizaTotalizadoresMultItensSemNegociacao(offset);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}
}
