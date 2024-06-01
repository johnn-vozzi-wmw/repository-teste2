package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.event.OpenningLastPhotoListEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.BaseLayoutListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.Tree;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.LocalEstoqueService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.business.service.MenuCategoriaService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoDestaqueService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoMenuCategoriaService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.TermoCorrecaoService;
import br.com.wmw.lavenderepda.business.service.TributacaoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoOpcaoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoComercialComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.EstoqueDisponivelComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto2ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto3ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto4ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.LocalEstoqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PacoteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoDestaqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ButtonMenuCategoria;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderProdutoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.presentation.ui.ext.MenuCategoriaScrollContainer;
import br.com.wmw.lavenderepda.thread.FotoProdutoThread;
import br.com.wmw.lavenderepda.util.FotoProdutoLazyLoadUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.io.device.scanner.ScanEvent;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.ListContainerEvent;
import totalcross.ui.event.ScrollEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListProdutoForm extends LavendereCrudListForm {
	
	private StringBuffer strTooltip = new StringBuffer(50);
    private StringBuffer strGetItem = new StringBuffer(50);
    
    private Vector itens = new Vector(0);
	
	public static int GRID_POSITION_ROWKEY = 0;
	public static int GRID_POSITION_CD_PRODUTO = 1;
	public static int GRID_POSITION_DS_PRODUTO = 2;
	public static int GRID_POSITION_PRINCIPIOATIVO = 3;
	public static int GRID_POSITION_PRECO = 4;
	public static int GRID_POSITION_ESTOQUE = 5;
	public static int GRID_POSITION_PREALTA = 6;
	public static String NU_FILTRO_FORNECEDOR = "1";
	public static String NU_FILTRO_GRUPOPRODUTO = "2";
	public static String NU_FILTRO_ATRIBUTO_PRODUTO = "3";
	public static String NU_FILTRO_AVISO_PRE_ALTA = "4";
	public static String NU_FILTRO_APENAS_KIT = "5";
	public static String NU_FILTRO_PRODUTO_PROMOCIONAL = "6";
	public static String NU_FILTRO_PRODUTO_GRUPO_DESTAQUE = "7";
	public static String NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE = "8";
	public static String NU_FILTRO_TIPO_RANKING = "9";
	public static String NU_FILTRO_PRODUTO_DESC_PROMOCIONAL = "10";
	public static String NU_FILTRO_PRODUTO_LOCAL_ESTOQUE = "11";
	public static String NU_FILTRO_ESTOQUE_DISPONIVEL = "12";
	public static String NU_FILTRO_PRODUTO_CBPACOTE = "18";
	public static String NU_FILTRO_PRODUTO_CBMARCADOR = "21";
	public static String NU_FILTRO_PRODUTO_COM_DESCQTD = "23";
	private static final String HASHKEY_PA = "PA";
	private static final String HASHKEY_AP = "AP";
	private static final String HASHKEY_DS = "DS";
	private static final String HASHKEY_CD = "CD";

	private boolean flListInicialized;
	private String dsFiltro = "";
	private boolean filterByPrincipioAtivo;
	private boolean filterByAplicacaoProduto;
	private boolean filterByCodigoProduto;
	private String cdTabelaPreco = "";
	public boolean inWindowSelectProduto;
	private ListProdutoWindow listProdutoWindow;
	public boolean isFromItemPedidoDesktop;

	private BaseButton btFiltroAvancado;
	private TabelaPrecoComboBox cbTabelaPreco;
	private CondicaoComercialComboBox cbCondicaoComercial;
	private LabelName lbFornecedor;
	private FornecedorComboBox cbFornecedor;
	private GrupoProduto1ComboBox cbGrupoProduto1;
	private GrupoProduto2ComboBox cbGrupoProduto2;
	private GrupoProduto3ComboBox cbGrupoProduto3;
	private GrupoProduto4ComboBox cbGrupoProduto4;
	private LabelName lbGrupoProduto1;
	@SuppressWarnings("unused")
	private LabelName lbGrupoProduto2;
	@SuppressWarnings("unused")
	private LabelName lbGrupoProduto3;
	@SuppressWarnings("unused")
	private LabelName lbGrupoProduto2e3;
	@SuppressWarnings("unused")
	private LabelName lbGrupoProduto1e2;
	@SuppressWarnings("unused")
	private LabelName lbGrupoProduto3e4;
	private UnidadeFederalComboBox cbUf;
	private PushButtonGroupBase btGroupTipoFiltros;
	private AtributoProdComboBox cbAtributoProd;
	private AtributoOpcaoProdComboBox cbAtributoOpcaoProd;
	private CheckBoolean ckApenasKit;
	private CheckBoolean ckApenasProdutoVendido;
	private CheckBoolean ckPreAltaProduto;
	private CheckBoolean ckProdutoPromocional;
	private CheckBoolean ckProdutoDescPromocional;
	private CheckBoolean ckProdutoDescQtd;
	private LabelName lbProdutoGrupoDestaque;
	private LabelName lbPacote;
	private LabelName lbLocalEstoque;
	private ProdutoDestaqueComboBox cbProdutoGrupoDestaque;
	private PacoteComboBox cbPacote;
	private ButtonAction btSlideFoto, btLeitorCamera, btGerarCatalogo, btCatalogoExterno;
	private int imageSize;
	private boolean buscaLimitaProduto;
	private LocalEstoqueComboBox cbLocalEstoque;
	private MarcadorMultiComboBox cbMarcador;
	private EstoqueDisponivelComboBox cbEstoqueDisponivel;
	protected SessionTotalizerContainer sessaoTotalizadores;
	protected LabelTotalizador lvQtProdutos;
	protected ImageSliderProdutoWindow imageSliderProdutoList; 
	
	private String textoMaximoPadraoComQuebraLinha;
	private int tamanhoMaximoCaracteresLinha;
	
	public ItemTabelaPreco itemTabelaPrecoFiltroLista;
	public CondicaoComercial condicaoComercialFiltroLista;
	private HashMap<String, Integer> posicoesbtGroupTipoFiltros = new HashMap<String,Integer>(); 
	private Map<String, Boolean> filtrosVisiveisMap = new HashMap<String, Boolean>();
	private Map<String, Boolean> filtrosNaoAutomaticosMap = new HashMap<String, Boolean>();
	private Map<String, Image> mapIconsMarcadores;

	private MenuCategoriaScrollContainer menuCategoriaScroll;
	private ButtonMenuCategoria dynamicButtonMenuCategoria;
	private Tree<ButtonMenuCategoria> buttonMenuCategoriaTree;
	private Tree<ButtonMenuCategoria> currentButtonMenuCategoriaTree;
	private LabelName lbCurrentCategoria;
	private LabelValue lvCurrentCategoria;
	private int qtButtonCategoriaPrevious;
	private BaseToolTip menuCategoriaAtualToolTip;
	private boolean menuCategoriaHidden;
	private boolean menuCategoriaStarted;
	private boolean onReposition;
	private boolean usaLazyLoading;

	protected PushButtonGroupBase btTipoPesquisaEdFiltro;

	private final RepresentanteSupervComboBox cbRepresentante;

	public ListProdutoForm(String cdTabelaPreco, boolean isFromItemPedidoDesktop) throws SQLException {
		this();
		this.cdTabelaPreco = cdTabelaPreco;
		this.isFromItemPedidoDesktop = isFromItemPedidoDesktop;
	}

    public ListProdutoForm() throws SQLException {
        super(Messages.PRODUTO_TITULO_LISTA);
        setBaseCrudCadMenuForm(new CadProdutoMenuForm());
        singleClickOn = true;
        sessaoTotalizadores = new SessionTotalizerContainer();
		lvQtProdutos = new LabelTotalizador("0" + FrameworkMessages.REGISTRO_SINGULAR);
		constructorListContainer();
        flListInicialized = false;
        useScanner = true;
        btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		edFiltro.setMaxLength(100);
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
        if (LavenderePdaConfig.isMostraFotoProduto()) {
    		btSlideFoto = new ButtonAction(Messages.ITEMPEDIDO_LABEL_SLIDEFOTOS, "images/slidefotos.png");
        }
        //--
        cbFornecedor = new FornecedorComboBox();
        lbFornecedor = new LabelName(Messages.FORNECEDOR_NOME_ENTIDADE);
        cbTabelaPreco = new TabelaPrecoComboBox();
        cbTabelaPreco.setID("cbTabelaPreco");
        cbCondicaoComercial = new CondicaoComercialComboBox();
        cbUf = new UnidadeFederalComboBox();
        //--
        ckApenasKit = new CheckBoolean(Messages.PRODUTOKIT_FILTRO);
        ckApenasProdutoVendido = new CheckBoolean(Messages.PRODUTOVENDIDO_FILTRO);
        ckPreAltaProduto = new CheckBoolean(Messages.PRODUTO_LABEL_PRE_ALTA_CUSTO);
        ckProdutoPromocional = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_PROMOCIONAL);
        ckProdutoDescPromocional = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_DESC_PROMOCIONAL);
		ckProdutoDescPromocional.setID("ckProdutoDescPromocional");
		ckProdutoDescQtd = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_DESC_QTD);
		ckProdutoDescQtd.setID("ckProdutoDescQtd");
        //
    	lbProdutoGrupoDestaque = new LabelName(Messages.PRODUTO_LABEL_PRODUTO_DESTAQUE);
    	cbProdutoGrupoDestaque = new ProdutoDestaqueComboBox();
    	//--
    	lbPacote = new LabelName(Messages.DESCONTO_PACOTE_LABEL_COMBO);
    	cbPacote = new PacoteComboBox();
    	//--
        cbGrupoProduto1 = new GrupoProduto1ComboBox(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
        cbGrupoProduto2 = new GrupoProduto2ComboBox(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
        cbGrupoProduto3 = new GrupoProduto3ComboBox(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
        cbGrupoProduto4 = new GrupoProduto4ComboBox(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
        if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
        	inicializaFiltrosGrupoProduto();
    	}
    	cbAtributoProd = new AtributoProdComboBox(LavenderePdaConfig.usaFiltroAtributoProduto ? Messages.ITEMPEDIDO_LABEL_ATRIBUTO : "");
    	cbAtributoOpcaoProd = new AtributoOpcaoProdComboBox(LavenderePdaConfig.usaFiltroAtributoProduto ? Messages.ITEMPEDIDO_LABEL_ATOPCAO : "");
    	cbAtributoOpcaoProd.drawBackgroundWhenDisabled = true;	
        cbAtributoProd.load();
        inicializaBtGroupTipoFiltros();           
    	if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
    		btLeitorCamera = new ButtonAction(Messages.CAMERA, "images/barcode.png");
    	}
    	if (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() && (VmUtil.isAndroid() || VmUtil.isSimulador() || VmUtil.isIOS())) {
    		btGerarCatalogo = new ButtonAction(Messages.CATALOGO, "images/catalogogerar.png");
    	}
		if (LavenderePdaConfig.isUsaArquivoCatalogoExternoListaProdutos() && (VmUtil.isAndroid() || VmUtil.isSimulador() || VmUtil.isIOS())) {
			btCatalogoExterno = new ButtonAction(Messages.CATALOGO_EXTERNO, "images/catalogo.png");
		}
    	cbLocalEstoque = new LocalEstoqueComboBox();
    	cbMarcador = new MarcadorMultiComboBox();
    	if (LavenderePdaConfig.apresentaMarcadorProdutoLista) {
		    cbMarcador.load();
	    }
    	lbLocalEstoque = new LabelName(Messages.PRODUTO_LABEL_FILTRO_LOCAL_ESTOQUE);
	    if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
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
	    cbEstoqueDisponivel = new EstoqueDisponivelComboBox();
		cbRepresentante = new RepresentanteSupervComboBox();
		usaLazyLoading = getMaxResult() > 0;
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

    private void inicializaFiltrosGrupoProduto() throws SQLException {
		lbGrupoProduto1 = new LabelName(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
		StringBuffer sb = new StringBuffer();
		lbGrupoProduto1 = new LabelName();
		if (!LavenderePdaConfig.ocultaGrupoProduto1) {
			sb.append(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
			cbGrupoProduto1.popupTitle = lbGrupoProduto1.getValue();
			if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
				Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
				cbGrupoProduto1.loadGrupoProduto1(null, fornecedorSelecionado);
			} else {
				cbGrupoProduto1.loadGrupoProduto1(null);
			}
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
			sb.append(" / ").append(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
			cbGrupoProduto2.popupTitle = Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2;
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
	        	cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
	        }
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
			sb.append(" / ").append(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
			cbGrupoProduto3.popupTitle = Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3;
			lbGrupoProduto3 = new LabelName(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
        	cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
			sb.append(" / ").append(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
			cbGrupoProduto4.popupTitle = Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4;
			cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());

		}
		String txtGruposProduto = sb.toString();
		loadLabelGruposProdutos(sb);
		if (sb.length() == 0) sb.append(txtGruposProduto);
		if (sb.indexOf(" / ") == 0) sb.delete(0, 2);
		lbGrupoProduto1.setText(sb.toString());
    }

	public void setListProdutoWindow(ListProdutoWindow listProdutoWindow) {
    	this.listProdutoWindow = listProdutoWindow;
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

    private void constructorListContainer() {
    	ScrollPosition.AUTO_HIDE = false;
    	int nuAtributosInGrid = getItemCount();
		//--
		int nuItemsPerLine = 2;
		listContainer = new GridListContainer(nuAtributosInGrid, nuItemsPerLine, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
		listContainer.setTotalizerQtdeVisible(false);
		listContainer.addSingleLineBarBottomContainer(sessaoTotalizadores);
		
		int size = 0;
		size = !LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto() ? size + 1 : size;
		size = !LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto() ? size + 1 : size;
		size = LavenderePdaConfig.isMostraDescricaoReferencia() ? size + 1 : size;
		size = LavenderePdaConfig.isHabilitaOrdenacaoPrecoPadrao() ? size + 1 : size;
	    size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoEmpresa() ? size + 1 : size;
	    size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRepresentante() ? size + 1 : size;
	    size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoSupervisao() ? size + 1 : size;
	    size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRegional() ? size + 1 : size;
	    size = LavenderePdaConfig.isMostraOrdenacaoRelevanciaProduto() ? size + 1 : size;
	    size = LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto() ? size + 1 : size;

		int position = 0;
		String[][] matriz = new String[size][2];
		if (!LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_CODIGO;
			matriz[position++][1] = "CDPRODUTO";
		}
		if (!LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSPRODUTO;
			matriz[position++][1] = "DSPRODUTO";
		}
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSREFERENCIA;
			matriz[position][1] = "DSREFERENCIA";
			position++;
		}
		if (LavenderePdaConfig.isHabilitaOrdenacaoPrecoPadrao()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_PRECO_PADRAO;
			matriz[position][1] = "VLPRECOPADRAO";
		}
	    if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoEmpresa()) {
		    matriz[position][0] = Messages.TIPORANKING_EMPRESA;
		    matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGEMP;
		    position++;
	    }
	    if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRepresentante()) {
		    matriz[position][0] = Messages.TIPORANKING_REPRESENTANTE;
		    matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGREP;
		    position++;
	    }
	    if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoSupervisao()) {
		    matriz[position][0] = Messages.TIPORANKING_SUPERVISAO;
		    matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGSUP;
		    position++;
	    }
	    if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRegional()) {
		    matriz[position][0] = Messages.TIPORANKING_REGIAO;
		    matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGREG;
		    position++;
	    }
	    if (LavenderePdaConfig.isMostraOrdenacaoRelevanciaProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_NURELEVANCIA;
			matriz[position][1] = "NURELEVANCIA";
			position++;
		}
	    if (LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_QTESTOQUE;
			matriz[position][1] = ProdutoBase.SORT_COLUMN_QTESTOQUE;
		}
	    if (LavenderePdaConfig.isPossuiOrdenacaoListaProduto()) {
			listContainer.setColsSort(matriz);
			setDefaultSortAttribute(matriz);
		} else {
			sortAtributte = "DSPRODUTO";
			sortAsc = ValueUtil.VALOR_SIM;
		}
		colocaAtributosImparesNaDireita(nuAtributosInGrid);
		ScrollPosition.AUTO_HIDE = true;
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			BaseLayoutListContainer layout = (BaseLayoutListContainer) listContainer.getLayout(); 
			if (layout.relativeFontSizes.length > 2) {
				layout.relativeFontSizes[2] = layout.relativeFontSizes[0]; 
			}
			if (layout.defaultItemColors.length > 2) {
				layout.defaultItemColors[2] = layout.defaultItemColors[0]; 
			}
			layout.lineGap = 15;
			layout.lineBreak = true;
		}
	}
    
	private void setDefaultSortAttribute(String[][] matriz) {
		String sortAscDefault = ListContainerConfig.getDefautOrder(getConfigClassName());
		if (ValueUtil.isEmpty(sortAscDefault)) {
			sortAscDefault = ValueUtil.VALOR_SIM;
		}
		boolean validSortAttribute = false;
		String sortAtributteDefault = ListContainerConfig.getDefautSortColumn(getConfigClassName());
		if (ValueUtil.isNotEmpty(sortAtributteDefault)) {
			for (int i = 0; i < matriz.length; i++) {
				String sortColumn = matriz[i][1];
				if (sortColumn.equalsIgnoreCase(sortAtributteDefault)) {
					validSortAttribute = true;
					break;
				}
			}
		}
		if (!validSortAttribute) {
			sortAscDefault = ValueUtil.VALOR_SIM;
			if (!LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos && !LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto()) {
				sortAtributteDefault = "DSPRODUTO";
			} else if (LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos && !LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto()) {
				sortAtributteDefault = "CDPRODUTO";
			} else {
				sortAtributteDefault = matriz[0][1];
			}
		}
		sortAtributte = sortAtributteDefault;
		sortAsc = sortAscDefault;
	}
    
	private int getItemCount() {
		int nuAtributosInGrid = 2;
		nuAtributosInGrid = LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos() ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.isMostraPrecoItemStNaListaProduto() ? nuAtributosInGrid + 2 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.mostraPrecoUnidadeItem ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() && !LavenderePdaConfig.usaModoControleEstoquePorTipoPedido ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.mostraColunaMarcaNaListaProdutoForaPedido() ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo() ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido() ? nuAtributosInGrid + 1 : nuAtributosInGrid;
		nuAtributosInGrid = LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista ? nuAtributosInGrid + 2 : nuAtributosInGrid;
		if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido()) {
			while (nuAtributosInGrid < 9) {
				++nuAtributosInGrid;
			}
		}
		return nuAtributosInGrid;
	}

    @Override
    protected CrudService getCrudService() throws SQLException {
        return ProdutoService.getInstance();
    }

    @Override
    protected BaseDomain getDomainFilter() throws SQLException {
    	ProdutoBase produtoFilter = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? new ProdutoTabPreco() : new Produto();
    	getBaseFilters(produtoFilter);
    	return produtoFilter;
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vm.gc();
	    boolean onlyStartString = LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null && btTipoPesquisaEdFiltro.getSelectedIndex() == 1;
		return ((ProdutoService)getCrudService()).findProdutos(TermoCorrecaoService.getInstance().getDsTermoCorrigido(dsFiltro), cdTabelaPreco, cbFornecedor.getValue(), filterByPrincipioAtivo, filterByAplicacaoProduto, domain, onlyStartString, filterByCodigoProduto);
    }
    
	private void getBaseFilters(ProdutoBase produto) {
		if (usaLazyLoading) {
			produto.limit = getLimitDomainFilter();
			produto.contaProdutosListados = true;
		} else {
			produto.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		}
		produto.filterToItemPedido = false;
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
    	produto.flPreAltaCusto = StringUtil.getStringValue(ckPreAltaProduto.isChecked());
    	produto.flKit = StringUtil.getStringValue(ckApenasKit.isChecked());
    	if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente) {
    		produto.flVendido = StringUtil.getStringValue(ckApenasProdutoVendido.isChecked());
    	}
    	if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido()) {
    		produto.flFiltraProdutoPromocional = StringUtil.getStringValue(ckProdutoPromocional.isChecked());
    	}
    	if (LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTelaListaProduto()) {
    		((ProdutoTabPreco)produto).flFiltraProdutoDescPromocional = StringUtil.getStringValue(ckProdutoDescPromocional.isChecked());
    	}
    	if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
    		produto.cdGrupoDestaque = cbProdutoGrupoDestaque.getValue();
    	}
    	if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
    		produto.pacoteFilter = cbPacote.getValue();
			if (LavenderePdaConfig.usaTabPrecoDescQuantidadePorPacote && produto.pacoteFilter != null) {
				produto.pacoteFilter.cdTabelaPrecoFilter = cbTabelaPreco.getValue();
			}
    	}
    	if (LavenderePdaConfig.usaFiltroAtributoProduto && ValueUtil.isNotEmpty(cbAtributoProd.getValue()) && ValueUtil.isNotEmpty(cbAtributoOpcaoProd.getValue())) {
    		produto.cdAtributoProduto = cbAtributoProd.getValue();
    		produto.cdAtributoOpcaoProduto = cbAtributoOpcaoProd.getValue();
    	}
    	if (LavenderePdaConfig.usaFiltroGrupoProduto > 0 && (ValueUtil.isNotEmpty(cbGrupoProduto1.getValue()) || LavenderePdaConfig.ocultaGrupoProduto1)) {
    		produto.cdGrupoProduto1 = cbGrupoProduto1.getValue();
    		produto.cdGrupoProduto2 = cbGrupoProduto2.getValue();
    		produto.cdGrupoProduto3 = cbGrupoProduto3.getValue();
    		produto.cdGrupoProduto4 = cbGrupoProduto4.getValue();
    	}
    	if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || isFromItemPedidoDesktop || LavenderePdaConfig.isMostraPrecoItemStNaListaProduto() || (LavenderePdaConfig.usaListaColunaPorTabelaPreco && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3()) || LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido()) {
    		produto.itemTabelaPreco = ItemTabelaPrecoService.getInstance().getTabelaPrecoFilterListaProdutos(cbTabelaPreco.getValue(), cbUf.getValue(), LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() ? 1 : 0, 0);
    		cdTabelaPreco = cbTabelaPreco.getValue();
    	}
    	
    	produto.estoque = new Estoque();
    	produto.estoque.cdLocalEstoque = ValueUtil.VALOR_NI;
    	if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto()) {
    		produto.estoque.cdLocalEstoque = cbLocalEstoque.getValue();	
    	}
    	produto.fromListItemPedido = false;
    	produto.fromListProduto = true;
    	if (LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial && LavenderePdaConfig.usaCondicaoComercialPedido) {
    		produto.cdCondicaoComercialFilter = cbCondicaoComercial.getValue();
	    }
    	if (LavenderePdaConfig.apresentaMarcadorProdutoLista) {
    		produto.cdMarcadores = cbMarcador.getSelected();
    	}
		if (LavenderePdaConfig.isUsaFiltroEstoqueDisponivelListaProduto()) {
			produto.cdStatusEstoque = cbEstoqueDisponivel.getValue();
		}
		if (LavenderePdaConfig.apresentaFiltroDescQtd) {
			produto.filtraProdutoDescQtd = ckProdutoDescQtd.isChecked();
		}
		if (LavenderePdaConfig.usaCategoriaMenuProdutos() && !menuCategoriaHidden) {
			produto.produtoMenuCategoriaFilter = ProdutoMenuCategoriaService.getInstance().getProdutoMenuCategoriaFilter(currentButtonMenuCategoriaTree);
		}
		if (ckProdutoPromocional.isChecked() && LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional) {
			produto.excetoDescPromocional = true;
		}
    }
    
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		ProdutoBase produtoBase = (ProdutoBase) domain;
		int color = getGridRowColor(produtoBase);
		if (color != -1) {
			c.setBackColor(color);
		}
		if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido()) {
			loadImageSize();
			Util.prepareDefaultImage(imageSize);
			c.setImage(Util.getDefaultLoadingImage(imageSize));
		}
	}
	
	private void loadImageSize() throws SQLException {
			int itemCount = getItemCount();
			int nuItensPerLine = 2;
			imageSize = ValueUtil.getIntegerValue(((itemCount / nuItensPerLine) * UiUtil.getLabelPreferredHeight())) - WIDTH_GAP;
	}
	
	@Override
	protected void updatePropertiesInRowItem(BaseListContainer.Item containerItem, BaseDomain domain, int indexAtual) throws SQLException {
		if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido() && domain != null) {
			ProdutoBase produtoBase = (ProdutoBase) domain;
			if (produtoBase.imageProduto != null) {
				ImageControl ic = ((ImageControl) containerItem.leftControl);
				ic.centerImage = true;
				ic.setImage(produtoBase.imageProduto);
				containerItem.repaintNow();
			}
		}
	}
	
	protected void loadLazyImages(Vector domainList) {
		if (usaThreadFotoProduto()) {
			if (usaLazyLoading) {
				FotoProdutoLazyLoadUtil.startThreadFotoProduto(this, domainList, imageSize, offset);
			} else {
				FotoProdutoThread.start(this, domainList, imageSize);
			}
			UiUtil.unpopProcessingMessage();
		}
	}
	
	private boolean usaThreadFotoProduto() {
		return LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido();
	}
	
    private int getGridRowColor(ProdutoBase produtoBase) throws SQLException {
		// PRODUTOS BLOQUEADOS
    	if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda && LavenderePdaConfig.isUsaDestacaProdutoBloqueado() && ProdutoBloqueadoService.getInstance().isProdutoBloqueado(produtoBase, cbTabelaPreco.getValue())) {
			return LavendereColorUtil.COR_FUNDO_GRID_PRODUTO_BLOQUEADO;
		}
    	// PRODUTOS VENDIDOS NO MÊS CORRENTE
    	if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && produtoBase.isVendido()) {
    		return LavendereColorUtil.COR_PRODUTO_VENDIDO_MES_CORRENTE;
    	}
		// PRODUTOS SEM ESTOQUE EM VERMELHO
		if ((LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produtoBase.isIgnoraValidacao()) || LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista) {
			if (LavenderePdaConfig.isConfigGradeProduto() && ProdutoGradeService.getInstance().isProdutoPossuiGrade(produtoBase)) {
				if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && produtoBase.sumEstoqueGrades == 0) {
					return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK;
				} else if (LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista && produtoBase.isAlgumaGradeSemEstoque) {
					return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK;
				} 
			} else if (!(LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.usaModoControleEstoquePorTipoPedido) && ProdutoService.getInstance().isGrifaProdutoSemEstoqueNaLista(produtoBase, null)) {
				return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK;
			}
		}
		// PRODUTOS COM ESTOQUE ABAIXO DO MINIMO
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista && produtoBase.estoque.qtEstoque <= produtoBase.estoque.qtEstoqueMin) {
			return LavendereColorUtil.COR_PRODUTO_ESTOQUE_MINIMO;
		}
		// PRODUTOS COM PRE-ALTA DE CUSTO EM VERMELHO
		if (LavenderePdaConfig.usaAvisoPreAlta && ValueUtil.getBooleanValue(produtoBase.flPreAltaCusto)) {
			return LavendereColorUtil.COR_PRODUTO_COM_AVISO_PRE_ALTA;
		}
		//MOSTRA PRODUTO PROMOCIONAL DESTACADO
		if ((!LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional || !ProdutoService.getInstance().isProdutoHaveDescPromocional(produtoBase, cbTabelaPreco.getValue()))
				&& (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaListaProduto() || (inWindowSelectProduto && LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido()))
				&& (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() || LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3())) {
			int color = destacaProdutoPromocionalItemTabPreco(produtoBase);
			if (color != -1) {
				return color;
			}
		}
		//MOSTRA PRODUTO COM DESCONTO PROMOCIONAL DESTACADO (tipo 1 e 2)
		if ((LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaListaProduto() || (inWindowSelectProduto && LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido())) && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			int color = destacaProdutoDescPromocional(produtoBase);
			if (color != -1) {
				return color;
			}
		}
		//MOSTRA PRODUTO COM DESCONTO PROMOCIONAL DESTACADO (tipo 3)
		if ((LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaListaProdutoTipo3() || (inWindowSelectProduto && LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedidoTipo3()))
				&& LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3()) {
			if (produtoBase.itemTabelaPreco != null && produtoBase.itemTabelaPreco.vlPctDescPromocional > 0d) {
				return LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK;
			}
		}
		//GRUPO DE DESTAQUE
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto() && ValueUtil.isNotEmpty(produtoBase.cdGrupoDestaque)) {
			int color = destacaProdutoPorGrupoDestaque(produtoBase);
			if (color != -1) {
				return color; 
			}
		}
		// PRODUTOS RESTRITOS
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito() && produtoBase.isProdutoRestrito()) {
			return LavendereColorUtil.COR_PRODUTO_RESTRITO;
		}
		// PRODUTO COM LOTE EM VIDA UTIL CRITICA
		if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista && ValueUtil.VALOR_SIM.equals(produtoBase.flLoteProdutoCritico)) {
    		return LavendereColorUtil.COR_FONTE_PRODUTO_LOTE_VIDA_UTIL_CRITICA;
    	}
		return -1;
	}

	private int destacaProdutoPorGrupoDestaque(ProdutoBase produtoBase) throws SQLException {
		if (produtoBase.produtoDestaque != null && produtoBase.produtoDestaque.corSistema != null && produtoBase.produtoDestaque.corSistema.cdEsquemaCor > 0) {
			CorSistema corSistema = produtoBase.produtoDestaque.corSistema;
			return Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB);
		}
		return -1;
	}

	private int destacaProdutoPromocionalItemTabPreco(ProdutoBase produtoBase) {
		if (produtoBase instanceof ProdutoTabPreco) {
			ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) produtoBase;
			String[] tabelasPromocionais = StringUtil.split(StringUtil.getStringValue(produtoTabPreco.dsTabPrecoPromoList), '|');
			Vector vector = new Vector(tabelasPromocionais);
			int index = vector.indexOf(cdTabelaPreco);
			if ((index != -1) || (ValueUtil.isEmpty(cdTabelaPreco) && (tabelasPromocionais.length > 0))) {
				return LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK;
			}
		} else if (produtoBase.isProdutoPromocao()) {
			return LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK;
		}
		return -1;
	}
	
	private int destacaProdutoDescPromocional(ProdutoBase produto) {
		if (produto instanceof ProdutoTabPreco) {
			ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) produto;
			String[] tabelasPromocionais = StringUtil.split(StringUtil.getStringValue(produtoTabPreco.dsTabPrecoDescPromocionalList), ProdutoTabPreco.SEPARADOR_CAMPOS);
			Vector vector = new Vector(tabelasPromocionais);
			int index = vector.indexOf(cdTabelaPreco);
			if ((index != -1) || (ValueUtil.isEmpty(cdTabelaPreco) && (tabelasPromocionais.length > 0))) {
				return LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK;
			}
		}
		return -1;
	}

    private boolean validateFiltro() {
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
    			ckApenasProdutoVendido.isChecked()  ||
    			ckPreAltaProduto.isChecked() ||
    			!cbFornecedor.isValueSelectedEmpty() ||
    			!cbAtributoOpcaoProd.isValueSelectedEmpty() ||
    			!cbGrupoProduto1.isValueSelectedEmpty() ||
			    (!cbGrupoProduto2.isValueSelectedEmpty() && LavenderePdaConfig.ocultaGrupoProduto1) ||
    			ckProdutoPromocional.isChecked() ||
    			!cbProdutoGrupoDestaque.isValueSelectedEmpty() ||
    			!cbPacote.isValueSelectedEmpty() ||
    			!cbEstoqueDisponivel.isValueSelectedEmpty() ||
    			ValueUtil.isNotEmpty(cbMarcador.getSelected());
    }
    
    @Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	ProdutoBase produto = (ProdutoBase) domain;
    	strTooltip.setLength(0);
    	String dsReferencia = StringUtil.getStringValue(produto.dsReferencia);
    	final String dsProduto = StringUtil.getStringValue(produto.dsProduto);
    	if (!LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
    		strTooltip.append(produto.cdProduto).append(" - ");
         	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
         		strTooltip.append("[").append(dsReferencia).append("] ").append(dsProduto).toString();
        	} else {
        		strTooltip.append(dsProduto).append(" [").append(dsReferencia).append("]");
        	}
    	} else if (!LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
    		strTooltip.append(produto.cdProduto).append(" - ").append(dsProduto);
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
        		strTooltip.append("[").append(dsReferencia).append("] ").append(dsProduto);
 
        	} else {
        		strTooltip.append(dsProduto).append(" [").append(dsReferencia).append("]");
        	}
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	strTooltip.append(produto.toString()).append("");
        	
        }	
    	return strTooltip.toString();
    }
    
    private String getEstoqueProduto(final ProdutoBase produto) throws SQLException {
    	if (ProdutoService.getInstance().isIgnoraValidacao(produto)) {
    		return ValueUtil.VALOR_EMBRANCO;
    	}
    	final double qtEstoque = LavenderePdaConfig.isUsaControleEstoquePorLoteProduto() ? LoteProdutoService.getInstance().getQtEstoqueLoteDisponivel(produto.cdProduto) : produto.estoque.qtEstoque;
    	return EstoqueService.getInstance().getEstoqueToString(qtEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE;
    }
    
    @Override
    protected String[] getItem(Object domain) throws SQLException {
        ProdutoBase produto = (ProdutoBase) domain;
        strGetItem.setLength(0);
        itens.setSize(0);
        addDescricaoProduto(produto);
        if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
        	itens.addElements(new String[]{"",""});
        }
        strGetItem.setLength(0);
        //--
        if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
        	double multiplier = LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido() ? 0.75 * 0.6 : 0.6;
        	itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsPrincipioAtivo), (int)(width * multiplier), listContainer.getFontSubItens()));
        }
        //--
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() && !LavenderePdaConfig.usaModoControleEstoquePorTipoPedido) {
			itens.addElement(getEstoqueProduto(produto));
        }
        if (LavenderePdaConfig.mostraColunaMarcaNaListaProdutoForaPedido()) {
        	itens.addElement(StringUtil.getStringValue(produto.dsMarca));
        }
        if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
        	strGetItem.setLength(0);
        	if (produto.qtVendasPeriodo > 0) {
        		itens.addElement(strGetItem.append(produto.qtVendasPeriodo).append(" vend.").toString());
        	} else {
        		itens.addElement("");
        	}
        }
        if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos()) {
        	strGetItem.setLength(0);
        	double precoProduto = getPrecoProdutoNaLista(produto);
        	if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
		        itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_RS, StringUtil.getStringValueToInterface(precoProduto)));
		        double nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
		        if (nuConversaoUnidadesMedida == 0) {
			        nuConversaoUnidadesMedida = 1;
		        }
		        itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_UNID_RS, StringUtil.getStringValueToInterface(precoProduto / nuConversaoUnidadesMedida)));
	        } else {
		        itens.addElement(strGetItem.append(Messages.PRODUTO_LABEL_RS).append(StringUtil.getStringValueToInterface(precoProduto)).toString());
	        }
        }
        
        if (LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()) {
        	double valorUnitarioUnidadeST;
			if (produto.itemTabelaPreco != null) {
				if (produto.nuConversaoUnidadesMedida == 0.0) {
					valorUnitarioUnidadeST = produto.itemTabelaPreco.vlEmbalagemSt;
				} else {
					valorUnitarioUnidadeST = produto.itemTabelaPreco.vlEmbalagemSt / produto.nuConversaoUnidadesMedida;
				}
				itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_VALOR_ST, Messages.MOEDA + " " + StringUtil.getStringValueToInterface(produto.itemTabelaPreco.vlEmbalagemSt)));
			} else {
        		valorUnitarioUnidadeST = 0;
        		itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_VALOR_ST, Messages.MOEDA + " " + StringUtil.getStringValueToInterface(0)));
        	}
        	itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_VALOR_UNIDADE_ST,  Messages.MOEDA + " " + StringUtil.getStringValueToInterface(valorUnitarioUnidadeST)));
        }
        
		itemTabelaPrecoFiltroLista = new ItemTabelaPreco();
		itemTabelaPrecoFiltroLista.cdTabelaPreco = cbTabelaPreco.getValue() != null ? cbTabelaPreco.getValue() : "0";
		itemTabelaPrecoFiltroLista.cdUf = cbUf.getValue();
		condicaoComercialFiltroLista = cbCondicaoComercial.getCondicaoComercial();
		
        if (LavenderePdaConfig.mostraBonusListaProduto) {
        	double valorBonusProduto = ProdutoService.getInstance().getValorBonusProdutoNaLista(produto.itemTabelaPreco);
        	itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_BONUS, Messages.MOEDA + " " + StringUtil.getStringValueToInterface(valorBonusProduto)));
        }
		
        if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosForaPedido()) {
        	itens.addElement(EstoqueService.getInstance().getEstoquePrevistoToString(produto.estoque.qtEstoquePrevisto) + Messages.PRODUTO_LABEL_ESTOQUE_PREVISTO);
        }
        if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido()) {
        	while (itens.size() < 9) {
				itens.addElement("");
			}
        }
        return (String[])itens.toObjectArray();
    }
    
    private void addDescricaoProduto(ProdutoBase produto) {
    	String dsReferencia = StringUtil.getStringValue(produto.dsReferencia);
    	final String dsProduto = StringUtil.getStringValue(produto.dsProduto);
    	if (!LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	itens.addElement(strGetItem.append(produto.cdProduto).append(" - ").toString());
        	strGetItem.setLength(0);
        	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
        		itens.addElement(strGetItem.append("[").append(dsReferencia).append("] ").append(dsProduto).toString());
        	} else {
        		itens.addElement(strGetItem.append(dsProduto).append(" [").append(dsReferencia).append("]").toString());
        	}
        } else if (!LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	itens.addElement(strGetItem.append(produto.cdProduto).append(" - ").toString());
        	itens.addElement(dsProduto);
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
        		itens.addElement(strGetItem.append("[").append(dsReferencia).append("] ").toString());
        		itens.addElement(dsProduto);
        	} else {
        		itens.addElement(dsProduto);
        		itens.addElement(strGetItem.append(" [").append(dsReferencia).append("]").toString());
        	}
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	itens.addElement(produto.dsProduto);
        	itens.addElement("");
        }
	}

	@Override
    protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
    }

    @Override
	public void initUI() {
		try {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				if ((LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == 0) && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto != ValorParametro.PARAM_INT_VALOR_ZERO)) {
					flListInicialized = true;
				}
			} finally {
				msg.unpop();
			}

			super.initUI();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	setFocusInListContainer();
    	showMessageFiltroQuantidadeResultados();
    }

	private void showMessageFiltroQuantidadeResultados() {
		if (apresentaMensagemNuLimiteRegistrosBuscaSistema()) {
    		UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_QUANTIDADE_RESULTADOS, LavenderePdaConfig.nuLinhasRetornoBuscaSistema));
    	}
	}

	@Override
    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
    	setFocusInListContainer();
    }
    
    @Override
    protected Control getComponentToFocus() {
    	return edFiltro;
    }

    public void setFocusInListContainer() {
    	listContainer.setFocus();
    	showRequestedFocus();
    }

    @Override
    public void loadDefaultFilters() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
		}
    	//Fornecedor
        if (LavenderePdaConfig.usaFiltroFornecedor) {
	        cbFornecedor.setSelectedIndex(0);
    	}
        //GrupoProduto
        if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
        	cbGrupoProduto1.setSelectedIndex(0);
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				cbGrupoProduto2.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				cbGrupoProduto3.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				cbGrupoProduto4.setSelectedIndex(0);
			}
        }
        //PrecoUf
        if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos() && LavenderePdaConfig.usaPrecoPorUf) {
            cbUf.carregaUf();
        	cbUf.setSelectedIndex(0);
        }
        //TabPreco
        if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos() || (LavenderePdaConfig.usaListaColunaPorTabelaPreco && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3())) {
        	cbTabelaPreco.loadForListProduto();
        	cbTabelaPreco.setSelectedIndex(0);
        }
        //Condição Comercial
        if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos() && LavenderePdaConfig.usaCondicaoComercialPedido) {
        	cbCondicaoComercial.carregaCondicoesComerciaisForListProduto();
        	cbCondicaoComercial.setSelectedIndex(0);
        }
        //Atributo Produto
        if (LavenderePdaConfig.usaFiltroAtributoProduto) {
        	cbAtributoProd.setSelectedIndex(0);
        	cbAtributoOpcaoProd.setSelectedIndex(0);
        	cbAtributoOpcaoProd.setEnabled(false);
        }
        //Pacote
        if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
        	cbPacote.setSelectedIndex(0);
        }
        if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto()) {
        	cbLocalEstoque.setSelectedIndex(0);
        }
    }

    @Override
    protected void onFormStart() throws SQLException {
    	//-- Filtros
		if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.apresentaFiltroRepresentanteListaProdutosCargaSupervisor) {
			UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
		}
    	if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos() || (LavenderePdaConfig.usaListaColunaPorTabelaPreco && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3())) {
        	if (LavenderePdaConfig.usaPrecoPorUf) {
        		UiUtil.add(this, new LabelName(Messages.TABELAPRECO_NOME_TABPRECO + " / " + Messages.PRODUTO_LABEL_UF_TABELA_PRECO), getLeft(), getNextY());
        		UiUtil.add(this, cbTabelaPreco, getLeft(), AFTER, FILL - fm.stringWidth("UF: SCWI"));
        		UiUtil.add(this, cbUf, AFTER + WIDTH_GAP, SAME);
        	} else {
            	UiUtil.add(this, new LabelName(Messages.TABELAPRECO_NOME_TABPRECO), cbTabelaPreco, getLeft(), getNextY());
            	if (LavenderePdaConfig.usaCondicaoComercialPedido) {
            		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL), cbCondicaoComercial, getLeft(), getNextY());
            	}
        	}
    	}
    	boolean showAllFiltros = LavenderePdaConfig.isTodosFiltrosFixosTelaListaProduto();
    	boolean exibeFiltroProdutoPromocional = false;
    	if (!LavenderePdaConfig.isTodosFiltrosNaTelaAvancadoListaProduto()) {
    		LavenderePdaConfig.carregaMapFiltrosProdutos(filtrosVisiveisMap, filtrosNaoAutomaticosMap, 0);
    		boolean exibeFiltroFornecedor = LavenderePdaConfig.usaFiltroFornecedor && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_FORNECEDOR) != null);
    		boolean exibeFiltroGrupoProduto = LavenderePdaConfig.usaFiltroGrupoProduto > 0 && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_GRUPOPRODUTO) != null);
    		boolean exibeFiltroAtributoProduto = LavenderePdaConfig.usaFiltroAtributoProduto && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_ATRIBUTO_PRODUTO) != null);
    		boolean exibeFiltroAvisoPreAlta = LavenderePdaConfig.usaAvisoPreAlta && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_AVISO_PRE_ALTA) != null);
    		boolean exibeFiltroApenasKit = LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_APENAS_KIT) != null);
    		boolean exibeFiltroApenasProdutoVendidoMesCorrente = LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE) != null);
    		exibeFiltroProdutoPromocional = LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_PROMOCIONAL) != null);
    		boolean exibeFiltroProdutoDescPromocional = LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTelaListaProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_DESC_PROMOCIONAL) != null);
    		boolean exibeFiltroProdutoGrupoDestaque = LavenderePdaConfig.isUsaGrupoDestaqueProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_GRUPO_DESTAQUE) != null);
    		boolean exibeFiltroPacote = LavenderePdaConfig.usaDescQuantidadePorPacote && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_CBPACOTE) != null);
    		boolean exibeFiltroTipoRanking = LavenderePdaConfig.isUsaOrdenacaoRankingProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_TIPO_RANKING) != null);
    		boolean exibeFiltroLocalEstoque = LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto() && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_LOCAL_ESTOQUE) != null);
    		boolean exibeFiltroEstoqueDisponivel = LavenderePdaConfig.isUsaFiltroEstoqueDisponivelListaProduto() && (showAllFiltros || filtrosVisiveisMap.get(ListProdutoForm.NU_FILTRO_ESTOQUE_DISPONIVEL) != null);
			boolean exibeFiltroMarcador = LavenderePdaConfig.apresentaMarcadorProdutoLista && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_CBMARCADOR) != null);
			boolean exibeFiltroProdutoComDescQtd = LavenderePdaConfig.apresentaFiltroDescQtd && (showAllFiltros || filtrosVisiveisMap.get(NU_FILTRO_PRODUTO_COM_DESCQTD) != null);
    		//--
    		if (exibeFiltroFornecedor) {
           		UiUtil.add(this, lbFornecedor, cbFornecedor, getLeft(), getNextY());
        	}
    		if (exibeFiltroGrupoProduto) {
    			addFiltrosGrupoProduto();
    		}
    		if (exibeFiltroAtributoProduto) {
    			UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_ATRIBUTO_OPCAO), cbAtributoProd, getLeft(), AFTER, FILL - (width / 2));
    			UiUtil.add(this, cbAtributoOpcaoProd, AFTER + WIDTH_GAP, SAME);
    		}
            if (exibeFiltroAvisoPreAlta) {
            	UiUtil.add(this, ckPreAltaProduto, getLeft(), getNextY());
            }
            if (exibeFiltroApenasKit) {
            	UiUtil.add(this, ckApenasKit, getLeft(), getNextY());
            }
            if (exibeFiltroProdutoPromocional && !LavenderePdaConfig.isUsaFiltraProdPromoAposCampoBusca()) {
            	UiUtil.add(this, ckProdutoPromocional, getLeft(), getNextY());
            }
            if (exibeFiltroProdutoDescPromocional) {
            	UiUtil.add(this, ckProdutoDescPromocional, getLeft(), getNextY(), getWFill());
            }
            if (exibeFiltroApenasProdutoVendidoMesCorrente) {
            	UiUtil.add(this, ckApenasProdutoVendido, getLeft(), getNextY());
            }
            if (exibeFiltroProdutoGrupoDestaque) {
            	UiUtil.add(this, lbProdutoGrupoDestaque, cbProdutoGrupoDestaque, getLeft(), getNextY());
            }
            if (exibeFiltroPacote) {
            	UiUtil.add(this, lbPacote, cbPacote, getLeft(), getNextY());
            }
            if (exibeFiltroLocalEstoque) {
            	UiUtil.add(this, lbLocalEstoque, cbLocalEstoque, getLeft(), getNextY());
            }
            if (exibeFiltroEstoqueDisponivel) {
            	UiUtil.add(this, new LabelName(Messages.ESTOQUE_COMBO_TITULO), cbEstoqueDisponivel, getLeft(), getNextY());
            }
			if (exibeFiltroMarcador) {
				UiUtil.add(this, new LabelName(Messages.MARCADOR_MARCADORES), cbMarcador, getLeft(), getNextY());
			}
			if (exibeFiltroProdutoComDescQtd) {
				UiUtil.add(this, ckProdutoDescQtd, getLeft(), getNextY());
			}
    	}
	    if (LavenderePdaConfig.usaCategoriaMenuProdutos() && !menuCategoriaStarted) {
		    int widthBotaoVoltar = ButtonMenuCategoria.FIXED_BT_VOLTAR_HEIGHT + fm.stringWidth(FrameworkMessages.BOTAO_VOLTAR) + WIDTH_GAP_BIG;
		    int yPos = getNextY();
		    UiUtil.add(this, lbCurrentCategoria, RIGHT - WIDTH_GAP_BIG, yPos + HEIGHT_GAP);
		    UiUtil.add(this, lvCurrentCategoria, widthBotaoVoltar + WIDTH_GAP_BIG + WIDTH_GAP, AFTER - HEIGHT_GAP, getWFill() - WIDTH_GAP);
		    UiUtil.add(this, dynamicButtonMenuCategoria, getLeft(), lbCurrentCategoria.getY(), getWFill(), ButtonMenuCategoria.FIXED_BT_VOLTAR_HEIGHT);
		    UiUtil.add(this, menuCategoriaScroll, getLeft() - WIDTH_GAP_BIG, lvCurrentCategoria.getY2(), getWFill(), ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * LavenderePdaConfig.getQtdCategoriaPadrao());
		    dynamicButtonMenuCategoria.defaultBtVoltarWidth = widthBotaoVoltar;
		    if (buttonMenuCategoriaTree == null) {
			    loadMenuCategoriaInicial();
		    }
	    }
	    int largEdFiltrar = getWFill();
    	if (!showAllFiltros) {
    		UiUtil.add(this, btFiltroAvancado, RIGHT - UiUtil.getFillRightSpace(), getNextY() + HEIGHT_GAP, UiUtil.getControlPreferredHeight());
    		largEdFiltrar -= UiUtil.getControlPreferredHeight();
    	}
    	int posY = !showAllFiltros ? SAME : getNextY();
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
			UiUtil.add(this, btGroupTipoFiltros, getLeft(), posY, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME, largEdFiltrar, UiUtil.getControlPreferredHeight());
		} else if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
			UiUtil.add(this, btTipoPesquisaEdFiltro, getLeft(), posY, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME, largEdFiltrar, UiUtil.getControlPreferredHeight());
		} else if(LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			UiUtil.add(this, btGroupTipoFiltros, getLeft(), posY, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME, largEdFiltrar, UiUtil.getControlPreferredHeight());
		} else {
			UiUtil.add(this, edFiltro, getLeft(), posY, largEdFiltrar, UiUtil.getControlPreferredHeight());
		}
    	if (exibeFiltroProdutoPromocional && LavenderePdaConfig.isUsaFiltraProdPromoAposCampoBusca()) {
    		UiUtil.add(this, ckProdutoPromocional, getLeft(), getNextY());
    	}
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			UiUtil.add(barBottomContainer, btLeitorCamera, 1);
		}
		if (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() && (VmUtil.isAndroid() || VmUtil.isSimulador() || VmUtil.isIOS())) {
			UiUtil.add(barBottomContainer, btGerarCatalogo, 2);
		}
		if (LavenderePdaConfig.isUsaArquivoCatalogoExternoListaProdutos() && (VmUtil.isAndroid() || VmUtil.isSimulador() || VmUtil.isIOS())) {
			UiUtil.add(barBottomContainer, btCatalogoExterno, 3);
		}
        //-- Botão slide foto
        if (LavenderePdaConfig.isMostraFotoProduto()) {
            UiUtil.add(barBottomContainer, btSlideFoto, 5);
        }
        //-- Grid
	    if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
		    int alt = isBarBottomContainerVisible() && !inWindowSelectProduto ? FILL - (barBottomContainer.getHeight() + 1) : FILL;
			UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, alt + menuCategoriaScroll.getHeight());
		    if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
			    if (menuCategoriaStarted) {
				    refreshContainerHeightForMenuCategoria();
			    } else {
				    startMenuCategoria();
			    }
		    }
	    } else {
		    int alt = isBarBottomContainerVisible() && !inWindowSelectProduto ? FILL - (barBottomContainer.getHeight() + 1) : FILL;
		    UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, alt);
	    }
	    UiUtil.add(sessaoTotalizadores, lvQtProdutos, LEFT + listContainer.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
    }
    
    private void addFiltrosGrupoProduto() {
    	if (LavenderePdaConfig.usaFiltroGrupoProduto >= 1) {
    		UiUtil.add(this, lbGrupoProduto1, getLeft(), getNextY(), PREFERRED, PREFERRED);
    	}
    	if (LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY(), FILL - (width / 2));
				UiUtil.add(this, cbGrupoProduto2, AFTER + WIDTH_GAP, SAME);
			} else {
				UiUtil.add(this, cbGrupoProduto2, getLeft(), getNextY());
			}
			UiUtil.add(this, cbGrupoProduto3, getLeft(), getNextY(), FILL - (width / 2));
			UiUtil.add(this, cbGrupoProduto4, AFTER + WIDTH_GAP, SAME);
		} else if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY());
				UiUtil.add(this, cbGrupoProduto2, getLeft(), AFTER, FILL - (width / 2));
				UiUtil.add(this, cbGrupoProduto3, AFTER + WIDTH_GAP, SAME);
			} else {
				UiUtil.add(this, cbGrupoProduto2, getLeft(), getNextY());
				UiUtil.add(this, cbGrupoProduto3, getLeft(), getNextY());
			}
		} else if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY());
			}
			UiUtil.add(this, cbGrupoProduto2, getLeft(), getNextY());
		} else if (LavenderePdaConfig.usaFiltroGrupoProduto == 1 && !LavenderePdaConfig.ocultaGrupoProduto1) {
			UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY());
		}
    }

	private void startMenuCategoria() {
		menuCategoriaStarted = true;
		toggleVisibilityMenuCategoria();
		int currentQt = menuCategoriaScroll.getBagChildren() == null ? 0 : menuCategoriaScroll.getBagChildren().length;
		int qt = LavenderePdaConfig.getQtdCategoriaPadrao() - Math.min(LavenderePdaConfig.getQtdCategoriaPadrao(), currentQt);
		int delta = -(ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT * qt);
		updateHeightContainersForCategoria(delta);
	}

	@Override
	protected int getTop() {
    	if (inWindowSelectProduto) {
    		return TOP + HEIGHT_GAP;
    	} else {
    		return super.getTop();
    	}
    }
    
    private boolean isBarBottomContainerVisible() {
    	return !inWindowSelectProduto && (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() || LavenderePdaConfig.isMostraFotoProduto() || LavenderePdaConfig.usaCameraParaLeituraCdBarras());
    }

    @Override
    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(isBarBottomContainerVisible());
    	barTopContainer.setVisible(!inWindowSelectProduto);
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == cbFornecedor) {
	    			loadGrupoProduto1ByFornecedor();
	    			btFiltrarClick(event.target, NU_FILTRO_FORNECEDOR);
	    		} else if (event.target == cbGrupoProduto1) {
	    			cbGrupoProduto1Change();
	    			btFiltrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
	    		} else if (event.target == cbGrupoProduto2) {
	    			cbGrupoProduto2Change();
	    			btFiltrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
	    		} else if (event.target == cbGrupoProduto3) {
	    			cbGrupoProduto3Change();
	    			btFiltrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
	    		} else if (event.target == cbGrupoProduto4) {
	    			btFiltrarClick(event.target, NU_FILTRO_GRUPOPRODUTO);
	    		} else if (event.target == btFiltroAvancado) {
	    			btFiltroAvancadoClick(event.target);
	    		} else if (event.target == btSlideFoto) {
	    			slideFotosClick();
	    		} else if (event.target == cbAtributoProd) {
	    			cbAtributoOpcaoProd.load(cbAtributoProd.getValue());
	    			cbAtributoOpcaoProd.setSelectedIndex(0);
	    			changeAtributoProdEnable();
	    			if (ValueUtil.isEmpty(cbAtributoProd.getValue())) {
	    				btFiltrarClick(event.target, NU_FILTRO_ATRIBUTO_PRODUTO);
	    			}
	    		} else if (event.target == cbAtributoOpcaoProd) {
	    			btFiltrarClick(event.target, NU_FILTRO_ATRIBUTO_PRODUTO);
	    			changeAtributoProdEnable();
	    		} else if ((event.target == cbTabelaPreco) || (event.target == cbUf)) {
	    			if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos()) {
	    				atualizaLista();
	    			}
	    		} else if (event.target == ckApenasKit) {
	    			btFiltrarClick(event.target, NU_FILTRO_APENAS_KIT);
	    		} else if (event.target == ckPreAltaProduto) {
	    			btFiltrarClick(event.target, NU_FILTRO_AVISO_PRE_ALTA);
	    		} else if (event.target == ckProdutoPromocional) {
	    			btFiltrarClick(event.target, NU_FILTRO_PRODUTO_PROMOCIONAL);
	    		} else if (event.target == ckProdutoDescPromocional) {
	    			btFiltrarClick(event.target, NU_FILTRO_PRODUTO_DESC_PROMOCIONAL);
	    		} else if (event.target == cbProdutoGrupoDestaque) {
	    			btFiltrarClick(event.target, NU_FILTRO_PRODUTO_GRUPO_DESTAQUE);
	    		} else if (event.target == cbPacote) {
	    			btFiltrarClick(event.target, NU_FILTRO_PRODUTO_CBPACOTE);
	    		} else if (event.target == ckApenasProdutoVendido) {
	    			btFiltrarClick(event.target, NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE);
	    		} else if (event.target == cbCondicaoComercial) {
	    			atualizaLista();
	    		} else if (event.target == btGerarCatalogo) {
	    			btGerarCatalogoClick();
	    		} else if (event.target == btLeitorCamera) {
	    			dsFiltro = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, dsFiltro);
	    			findProdutoByLeituraCdBarras();
				} else if (event.target == btGroupTipoFiltros) {
					btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
				} else if (event.target == cbLocalEstoque) {
	     			btFiltrarClick(event.target, NU_FILTRO_PRODUTO_LOCAL_ESTOQUE);
	     		} else if (event.target instanceof ButtonMenuCategoria) {
				    menuCategoriaClick((ButtonMenuCategoria) event.target);
			    } else if (event.target == cbMarcador) {
			    	btFiltrarClick(cbMarcador, NU_FILTRO_PRODUTO_CBMARCADOR);
			    } else if (event.target == cbEstoqueDisponivel) {
	    			btFiltrarClick(event.target, NU_FILTRO_ESTOQUE_DISPONIVEL);
				} else if (event.target == btCatalogoExterno) {
					btCatalogoExternoClick();
				} else if (event.target == ckProdutoDescQtd) {
	    			btFiltrarClick(event.target, NU_FILTRO_PRODUTO_COM_DESCQTD);
	    		} else if (event.target == cbRepresentante) {
					cbRepresentanteChange();
				}
	    		break;
	    		
	    	}
	    	case ListContainerEvent.LEFT_IMAGE_CLICKED_EVENT: {
	    		ProdutoBase produto = getProdutoFromListContainerByIndex(listContainer.getSelectedIndex());
	    		ImageSliderProdutoWindow imageSliderProdutoWindow = new ImageSliderProdutoWindow(produto);
	    		imageSliderProdutoWindow.popup();
	    		break;
	    	}
	    	case ScrollEvent.SCROLL_END: {
				if (usaLazyLoading && flListInicialized && listContainer.isEndScroll()) {
					listScroll();
				}
				break;
			}
	    	case OpenningLastPhotoListEvent.OPENNING_LAST_PHOTO_LIST_EVENT: {
	    		if (usaLazyLoading && flListInicialized && !listContainer.scrollFinished) {
					listScroll();
				}
				break;
	    	}
    	}
	}

	private void alterarTipoTeclado() {
		if (filterByCodigoProduto && LavenderePdaConfig.isExibeBotaoParaFiltroCodigoTecladoNumerico()) {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_INT);
		} else {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_TEXT);
		}
	}
    
	private void cbGrupoProduto3Change() throws SQLException {
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void cbGrupoProduto2Change() throws SQLException {
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void cbGrupoProduto1Change() throws SQLException {
		cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
		cbGrupoProduto4.setSelectedIndex(0);
	}
    
    private void btGerarCatalogoClick() throws SQLException {
    	new CatalogoProdutoWindow().popup();
	}

	private void btCatalogoExternoClick() {
		new CatalogoExternoWindow().popup();
	}

    @Override
    protected void resizeListToFullScreen() {
		listContainer.setRect(0, LavenderePdaConfig.exibeGrupoProdutoListaMaximizada && cbGrupoProduto1.isDisplayed() ? cbGrupoProduto1.getY2() + HEIGHT_GAP : getTop(), FILL, barBottomContainer.isVisible() ? FILL - barBottomContainer.getHeight() : FILL);
		listContainer.initUI();
		listContainer.repaintContainers();
	    if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
	    	menuCategoriaScroll.qtRepositions++;
		    menuCategoriaScroll.orientation = Settings.isLandscape() ? MenuCategoriaScrollContainer.LANDSCAPE : MenuCategoriaScrollContainer.PORTRAIT;
	    }
    }
    
    private void loadGrupoProduto1ByFornecedor() throws SQLException {
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
			Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
			cbGrupoProduto1.loadGrupoProduto1(null, fornecedorSelecionado);
		}
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
			edFiltro.requestFocus();
		}
	}

	private void btFiltroAvancadoClick(Object target) throws SQLException {
    	FiltroProdutoAvancadoWindow filtroForm = new FiltroProdutoAvancadoWindow();
    	//--
		if (LavenderePdaConfig.apresentaMarcadorProdutoLista && ValueUtil.isNotEmpty(cbMarcador.getSelectedMarcador())) {
			filtroForm.cbMarcador.setSelectItens(cbMarcador.getSelectedMarcador());
		}
    	if (LavenderePdaConfig.usaFiltroFornecedor && (ValueUtil.isNotEmpty(cbFornecedor.getValue()))) {
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
		} else if(filterByAplicacaoProduto && LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto()){
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
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
			if (ValueUtil.isNotEmpty(cbGrupoProduto1.getValue())) {
				filtroForm.cbGrupoProduto1.setValue(cbGrupoProduto1.getValue());
				filtroForm.cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
				filtroForm.cbGrupoProduto2.setSelectedIndex(0);
			}
			if (ValueUtil.isNotEmpty(cbGrupoProduto2.getValue())) {
				filtroForm.cbGrupoProduto2.setValue(cbGrupoProduto2.getValue());
				filtroForm.cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
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
			filtroForm.cbGrupoProduto1.loadGrupoProduto1(null, (Fornecedor) cbFornecedor.getSelectedItem());
			if (cbGrupoProduto1.getSelectedItem() != null) {
				filtroForm.cbGrupoProduto1.setSelectedItem(cbGrupoProduto1.getSelectedItem());
			} else {
				filtroForm.cbGrupoProduto1.setSelectedIndex(0);
			}
		}
		filtroForm.ckPreAltaProduto.setChecked(ckPreAltaProduto.isChecked());
		filtroForm.ckProdutoPromocional.setChecked(ckProdutoPromocional.isChecked());
		filtroForm.ckProdutoDescPromocional.setChecked(ckProdutoDescPromocional.isChecked());
		filtroForm.ckProdutoKit.setChecked(ckApenasKit.isChecked());
		filtroForm.ckProdutoVendido.setChecked(ckApenasProdutoVendido.isChecked());
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
    		filtroForm.cbProdutoGrupoDestaque.setSelectItens(cbProdutoGrupoDestaque.getValue());
    	}
		if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
			filtroForm.cbPacote.setValue(cbPacote.getValue());
		}
		if (LavenderePdaConfig.isUsaFiltroEstoqueDisponivelListaProduto()) {
			filtroForm.cbEstoqueDisponivel.setSelectItens(cbEstoqueDisponivel.getValue());
		}
		if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto() && ValueUtil.isNotEmpty(cbLocalEstoque.getValue())) {
			filtroForm.cbLocalEstoque.setValue(cbLocalEstoque.getValue());
		}
		filtroForm.edFiltro.setValue(edFiltro.getValue());
		//--
		filtroForm.popup();
		//--
		if (filtroForm.filtroRealizado) {
			edFiltro.setValue(filtroForm.edFiltro.getValue());
			if (LavenderePdaConfig.usaFiltroFornecedor) {
				if (ValueUtil.isNotEmpty(filtroForm.cbFornecedor.getValue())) {
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
			if ((LavenderePdaConfig.isUsaOrdenacaoRankingProduto() /*&& cbTipoRanking.getValue() > 0*/) || LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
				if (ValueUtil.isNotEmpty(filtroForm.cbGrupoProduto1.getValue())) {
					cbGrupoProduto1.setValue(filtroForm.cbGrupoProduto1.getValue());
				} else {
					cbGrupoProduto1.setSelectedIndex(0);
				}
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
				if (ValueUtil.isNotEmpty(filtroForm.cbGrupoProduto2.getValue())) {
					cbGrupoProduto2.setValue(filtroForm.cbGrupoProduto2.getValue());
				} else {
					cbGrupoProduto2.setSelectedIndex(0);
				}
				cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
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
			ckPreAltaProduto.setChecked(filtroForm.ckPreAltaProduto.isChecked());
			ckApenasKit.setChecked(filtroForm.ckProdutoKit.isChecked());
			ckApenasProdutoVendido.setChecked(filtroForm.ckProdutoVendido.isChecked());
			ckProdutoPromocional.setChecked(filtroForm.ckProdutoPromocional.isChecked());
			ckProdutoDescPromocional.setChecked(filtroForm.ckProdutoDescPromocional.isChecked());
			ckProdutoDescQtd.setChecked(filtroForm.ckProdutoDescQtd.isChecked());
			if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
				cbProdutoGrupoDestaque.setSelectItens(filtroForm.cbProdutoGrupoDestaque.getValue());
			}
			if (LavenderePdaConfig.usaDescQuantidadePorPacote) {
				if (filtroForm.cbPacote.getValue() != null) {
					cbPacote.setValue(filtroForm.cbPacote.getValue());
				} else {
					cbPacote.setSelectedIndex(0);
				}
			}
			if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto()) {
				cbLocalEstoque.setValue(filtroForm.cbLocalEstoque.getValue());
			}
			if (LavenderePdaConfig.isUsaFiltroEstoqueDisponivelListaProduto()) {
				cbEstoqueDisponivel.setSelectItens(filtroForm.cbEstoqueDisponivel.getValue());
			}
			if (LavenderePdaConfig.apresentaMarcadorProdutoLista) {
				cbMarcador.setSelectItens(filtroForm.cbMarcador.getSelectedMarcador());
			}
			//--
			btFiltrarClick(target, null);
		}
		btFiltroAvancado.setImage(UiUtil.getColorfulImage(destacaBotaoFiltro() ? "images/maisfiltrosativo.png" : "images/maisfiltros.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
    }
	
	private boolean destacaBotaoFiltro() {
		return ckApenasKit.isChecked() ||
				ckPreAltaProduto.isChecked() ||
				ckProdutoPromocional.isChecked() ||
				ckProdutoDescPromocional.isChecked() ||
				ckApenasProdutoVendido.isChecked() ||
				ValueUtil.isNotEmpty(edFiltro.getValue()) ||
				ValueUtil.isNotEmpty(cbGrupoProduto1.getValue()) ||
				ValueUtil.isNotEmpty(cbGrupoProduto2.getValue()) ||
				ValueUtil.isNotEmpty(cbGrupoProduto3.getValue()) ||
				ValueUtil.isNotEmpty(cbGrupoProduto4.getValue()) ||
				ValueUtil.isNotEmpty(cbFornecedor.getValue()) ||
				ValueUtil.isNotEmpty(cbAtributoProd.getValue()) ||
				ValueUtil.isNotEmpty(cbAtributoOpcaoProd.getValue());
	}

    private void slideFotosClick() throws SQLException {
    	if (listContainer.size() > 0) {
    		Vector produtoList = getDomainListByListContainer(0);
			UiUtil.showProcessingMessage();
			imageSliderProdutoList = new ImageSliderProdutoWindow(produtoList, listContainer.getSelectedIndex(), getQtTotalRegistros(produtoList));
			UiUtil.unpopProcessingMessage();
			imageSliderProdutoList.popup();
			selectImage(listContainer.size(), imageSliderProdutoList);
			imageSliderProdutoList = null;
    	} else {
    		UiUtil.showWarnMessage(Messages.SLIDE_SEM_FOTO);
    	}
    }

	private void selectImage(int size, ImageSliderProdutoWindow imageSliderProdutoWindow) {
		if (imageSliderProdutoWindow.imageSelected != null) {
			String[] imageSelected = imageSliderProdutoWindow.imageSelected;
			for (int i = 0; i < size; i++) {
				ProdutoBase produtoBase = (ProdutoBase) getProdutoFromListContainerByIndex(i);
				if (imageSelected[1].equals(ProdutoService.getInstance().getDsProdutoSlide(produtoBase))) {
					listContainer.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	@Override
    public void list() throws SQLException {
    	configureConstantesQuebraLinha();
    	FotoProdutoLazyLoadUtil.stopAllThreadsFotoProdutoLazyLoad();
    	if (LavenderePdaConfig.apresentaMarcadorProdutoLista && (mapIconsMarcadores == null || mapIconsMarcadores.size() == 0)) {
    		useLeftTopIcons = true;
    		mapIconsMarcadores = new HashMap<>();
    		int iconSize = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
    		ProdutoService.getInstance().loadImagesMarcadores(mapIconsMarcadores, iconSize);
    	}
    	if (flListInicialized) {
        	super.list();
    	}
    }
    
    @Override
    protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
    	if (LavenderePdaConfig.apresentaMarcadorProdutoLista) {
    		ProdutoBase produto = (ProdutoBase) domain;
    		return ProdutoService.getInstance().getIconsMarcadores(produto, mapIconsMarcadores);
    	}
    	return super.getIconsLegend(domain);
    }
    
    @Override
    public void updateCurrentRecord(BaseDomain domain) throws SQLException {
    	super.updateCurrentRecord(domain);
    	if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido()) {
    		list();
    	}
    }

    @Override
    public void detalhesClick() throws SQLException {
		if (inWindowSelectProduto) {
			listProdutoWindow.produto = (Produto) getSelectedDomain();
			listProdutoWindow.fecharWindow();
		} else {
			CadProdutoMenuForm cadProduto = ((CadProdutoMenuForm)getBaseCrudCadMenuForm());
			if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutos() || (LavenderePdaConfig.usaListaColunaPorTabelaPreco && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3())) {
				cadProduto.cdTabPreco = cbTabelaPreco.getValue();
			}
			if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto() && ValueUtil.isNotEmpty(cbLocalEstoque.getValue())) {
				String dsLocalEstoque = LocalEstoqueService.getInstance().findDsLocalEstoque(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cbLocalEstoque.getValue()).toString();
				cadProduto.cdLocalEstoque = cbLocalEstoque.getValue();
				cadProduto.dsLocalEstoque = StringUtil.getStringValue(dsLocalEstoque);
			}
			super.detalhesClick();
		}
		if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
			menuCategoriaScroll.backToList = true;
		}
    }

    @Override
    protected void filtrarClick() throws SQLException {
    	btFiltrarClick(edFiltro, null);
    }

    private void btFiltrarClick(Object target, String targetId) throws SQLException {
    	if (targetId != null && filtrosNaoAutomaticosMap.get(targetId) != null) return;
    	try {
    		dsFiltro = edFiltro.getText();
    		ProdutoUnidade produtoUnidade = null;
    		if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(dsFiltro)) {
    			produtoUnidade = ProdutoUnidadeService.getInstance().getProdutoUnidadeByCdBarras(dsFiltro);
    			if (produtoUnidade != null) {
    				dsFiltro = produtoUnidade.cdProduto;
    			}
    		}
    		edFiltro.setText(dsFiltro);
    		if (validateFiltro()) {
    			if (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == ValorParametro.PARAM_INT_VALOR_ZERO && !isAlgumFiltroEspecialSelecionado() && isOrigemFiltrosEspeciais(target)) {
    				clearGrid();
    			} else {
    				flListInicialized = true;
    				list();
    				showMessageFiltroQuantidadeResultados();
    				if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
    					edFiltro.setValue("");
    				}
    			}
    		} else {
    			if (!isOrigemFiltrosEspeciais(target)) {
    				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
    			}
    			clearGrid();
    		}
    	} finally {
    		setFocusInListContainer();
    	}
    }
    
    private boolean isOrigemFiltrosEspeciais(final Object target) {
    	return target == cbFornecedor || target == cbGrupoProduto1 || target == ckApenasKit || target == cbAtributoProd || target == cbAtributoOpcaoProd || target == ckProdutoPromocional || target == ckProdutoDescPromocional || target == ckApenasProdutoVendido || target == cbProdutoGrupoDestaque || target == cbPacote || target == cbMarcador;
    }

    @Override
    public void onFormClose() throws SQLException {
		TributacaoService.getInstance().clearCache();
		FotoProdutoLazyLoadUtil.stopAllThreadsFotoProdutoLazyLoad();
		FotoProdutoThread.setToStop();
		Util.resetImages();
    	super.onFormClose();
    }
    
    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	loadLazyImages(domainList);
    	strGetItem.setLength(0);
    	strTooltip.setLength(0);
    	itens.removeAllElements();
    	itens.setSize(0);
    	int qtTotalRegistros = getQtTotalRegistros(domainList);
    	setValueQtProdutosTotalizador(qtTotalRegistros);
    	if (listContainer.size() == qtTotalRegistros) {
    		listContainer.scrollFinished = true;
    	}
    	if (imageSliderProdutoList != null) {
    		imageSliderProdutoList.carregaMaisProdutosNoImageCarrousel(getDomainListByListContainer(offset));
    	}
    }

	public int getQtTotalRegistros(Vector domainList) {
		int qtTotalRegistros = listContainer.size();
		buscaLimitaProduto = false;
		if (usaLazyLoading && ValueUtil.isNotEmpty(domainList)) {
			qtTotalRegistros = ((ProdutoBase) domainList.elementAt(0)).qtProdutosListados; 
		}
		if (LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && LavenderePdaConfig.nuLinhasRetornoBuscaSistema <= qtTotalRegistros) {
			qtTotalRegistros = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
			buscaLimitaProduto = true;
		}
		return qtTotalRegistros;
	}
    
	private boolean apresentaMensagemNuLimiteRegistrosBuscaSistema() {
		return LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && buscaLimitaProduto && LavenderePdaConfig.apresentaMensagemLimiteNuLinhasRetornoBuscaSistema;
	}

	private void loadLabelGruposProdutos(StringBuffer sb) {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			sb.setLength(0);
			if (temp.length > 0 && lbGrupoProduto1 != null) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					sb.append(temp[0]);
					cbGrupoProduto1.popupTitle = temp[0];
				}
				if (temp.length > 1 && LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
					sb.append(" / ").append(temp[1]);
					cbGrupoProduto2.popupTitle = temp[1];
					if (temp.length > 2 && LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
						sb.append(" / ").append(temp[2]);
						cbGrupoProduto3.popupTitle = temp[2];
						if (temp.length > 3 && LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
							sb.append(" / ").append(temp[3]);
							cbGrupoProduto4.popupTitle = temp[3];
						}
					}
				}
			}
		}
	}
	
	private void atualizaLista() throws SQLException {
    	int index = listContainer.getScrollPos();
		list();
		listContainer.setScrollPos(index);
		repaint();
    }

	@Override
	protected void processaLeitura(ScanEvent event) throws SQLException {
		dsFiltro = event.data;
		String errorCode = "NR";
		if (!errorCode.equals(dsFiltro)) {
			super.processaLeitura(event);
			findProdutoByLeituraCdBarras();
		}
	}

	protected void findProdutoByLeituraCdBarras() throws SQLException {
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			edFiltro.setText(dsFiltro);
			filtrarClick();
			if (listContainer.size() == 1) {
				listContainer.setSelectedIndex(0);
				detalhesClick();
			}
		}
	}
	
	@Override
	public void reposition() {
		this.onReposition = true;
		super.reposition();
		if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
			if (!isListMaximized()) {
				refreshContainerHeightForMenuCategoria();
			}
			if (!menuCategoriaHidden) {
				updateWidthButtonsMenuCategoria();
				addScrollSpacerButtonsMenuCategoria();
				updateCurrenteCategoriaText();
			}
		}
		this.onReposition = false;
	}

	private void configureConstantesQuebraLinha() {
		double proporcaoImagens = 0;
		if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosForaPedido()) {
			proporcaoImagens = (width * (22.0 /100)) + HEIGHT_GAP;
		}
		textoMaximoPadraoComQuebraLinha = Convert.insertLineBreak((int) (width - proporcaoImagens), fm, Messages.MAXIMO_CARACTERES_PADRAO_TELA);
		tamanhoMaximoCaracteresLinha = Convert.tokenizeString(textoMaximoPadraoComQuebraLinha, '\n')[0].length();
	}
	
	private void updateHeightContainersForCategoria(int delta) {
		Control[] controls = this.getChildren();
		changeControlsYForMenuCategoria(controls, delta);
		if (listContainer.getY() + delta < Settings.screenHeight) {
			listContainer.setRect(0, listContainer.getY() + delta, FILL, isBarBottomContainerVisible() && !inWindowSelectProduto ? FILL - (barBottomContainer.getHeight() + 1) : FILL);
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
		Control[] controls = this.getChildren();
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

	protected void adjustContainerHeightForMenuCategoria() {
		Control[] controls = this.getChildren();
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
		} else {
			if (menuCategoriaScroll.backToList && menuCategoriaScroll.qtRepositions > 2 && ((menuCategoriaScroll.orientation == MenuCategoriaScrollContainer.PORTRAIT && !Settings.isLandscape()) || (menuCategoriaScroll.orientation == MenuCategoriaScrollContainer.LANDSCAPE && Settings.isLandscape()))) {
				changeControlsYForMenuCategoria(controls, delta);
			} else if (menuCategoriaScroll.qtRepositions > 1 && ((menuCategoriaScroll.qtRepositions % 2 != 0 && !menuCategoriaScroll.backToList) || (menuCategoriaScroll.qtRepositions % 2 == 0 && menuCategoriaScroll.backToList))) {
				changeControlsYForMenuCategoria(controls, delta);
			}
		}
		menuCategoriaScroll.qtRepositions = 0;
		menuCategoriaScroll.backToList = false;
		updateListContainerRectMenuCategoria(delta);
	}

	private void updateListContainerRectMenuCategoria(int delta) {
		if (listContainer.getY() + delta < Settings.screenHeight) {
			listContainer.setRect(0, listContainer.getY() + delta, FILL, isBarBottomContainerVisible() && !inWindowSelectProduto ? FILL - (barBottomContainer.getHeight() + 1) : FILL);
		}
		listContainer.initUI();
		listContainer.repaintContainers();
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

	private void loadMenuCategoriaInicial() throws SQLException {
		ButtonMenuCategoria rootTreeNodeCategoria = new ButtonMenuCategoria();
		buttonMenuCategoriaTree = new Tree<>(rootTreeNodeCategoria);
		currentButtonMenuCategoriaTree = buttonMenuCategoriaTree;
		MenuCategoriaService.getInstance().populateTree(buttonMenuCategoriaTree);
		List<Tree<ButtonMenuCategoria>> subTrees = buttonMenuCategoriaTree.getSubTrees();
		loadListIntoMenuCategoriaScroll(subTrees);
		setRectButtonMenuCategoria();
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
		lbCurrentCategoria.setVisible(ValueUtil.isNotEmpty(fullPath));
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
			filtrarClick();
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
				button.setRect(button.getX(), button.getY(), FILL + spacer, button.getHeight());
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
			filtrarClick();
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
				buttonMenuCategoria.setRect(getLeft(), i * ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT, FILL, ButtonMenuCategoria.DEFAULT_BUTTON_HEIGHT_SPACER);
			}
		}
	}

	@Override
	protected void afterResizeListToOriginalSize() {
		if (LavenderePdaConfig.usaCategoriaMenuProdutos()) {
			adjustContainerHeightForMenuCategoria();
		}
	}
	
	private double getPrecoProdutoNaLista(ProdutoBase produto) throws SQLException {
		double precoProduto;
		if (onReposition) {
			if (LavenderePdaConfig.usaPrecoPadraoProdutoParaSerExibidoNaLista()) {
				precoProduto = produto.vlPrecoPadrao;
			} else {
				precoProduto = produto.itemTabelaPreco != null ? produto.itemTabelaPreco.vlUnitario : 0; 
			}
		} else {
			precoProduto = ProdutoService.getInstance().getPrecoProduto(produto, cbCondicaoComercial.getCondicaoComercial(), cbTabelaPreco.getValue(), cbUf.getValue());
		}
		return precoProduto;
	}
	
	private void colocaAtributosImparesNaDireita(int nuAtributosInGrid) {
		for (int i = 3; i < nuAtributosInGrid; i += 2) {
			listContainer.setColPosition(i, RIGHT);
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

	private void cbRepresentanteChange() throws SQLException {
		if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
		} else {
			SessionLavenderePda.setRepresentante(null);
		}
		loadDefaultFilters();
		filtrarClick();
	}
	
	protected void setValueQtProdutosTotalizador(int qtTotalProdutos) {
		lvQtProdutos.setText(LavenderePdaConfig.getMessageCustomLeftTotalizer(qtTotalProdutos));
		lvQtProdutos.reposition();
	}
	
	protected ProdutoBase getProdutoFromListContainerByIndex(int index) {
		return (ProdutoBase) ((BaseListContainer.Item) listContainer.getContainer(index)).domain;
	}
	
	protected Vector getDomainListByListContainer(int startIndex) {
		int size = listContainer.size();
		Vector produtoList = new Vector(size - startIndex);
		for (int i = startIndex; i < size; i++) {
			produtoList.addElement(getProdutoFromListContainerByIndex(i));
		}
		return produtoList;
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
	@Override
	protected int getMaxResult() {
		return LavenderePdaConfig.getNuRegistrosProdutosMostradosPorVezNaLista();
	}
	
	protected int getLimitDomainFilter() {
		int qtLimit = getMaxResult();
		if (LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && offset + qtLimit > LavenderePdaConfig.nuLinhasRetornoBuscaSistema) {
			qtLimit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema - offset;
		}
		return qtLimit;
	}
	
	@Override
	public void clearGrid() {
		super.clearGrid();
		setValueQtProdutosTotalizador(0);
	}
}
