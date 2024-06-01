package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RelNovidadeProdService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Settings;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListEstoqueGradeWindow extends WmwWindow {
	
	public int posYGrade;
	public int posYList;
	public int heightGrade;
	public int heightList;

	private BaseGridEdit gradeGrid;
	private BaseGridEdit gradeList;
	
	private EditFiltro edFiltro;
	protected ButtonAction btLeitorCamera;
	private String dsFiltro;
	private CheckBoolean ckInserido;
	private CheckBoolean ckPrevisto;
	
	private Produto produto;
	private LabelValue lbQtTotalEstoque;
	private LabelValue lbQtTotalEstoqueGrade;
	private LabelValue lbQtTotalEstoquePrevisto;
	private LabelName lbItemGrade1;
	private LabelName lbQtTotalEstoqueName;
	private LabelName lbQtTotalEstoqueGradeName;
	private LabelName lbQtTotalEstoquePrevistoName;
	private ItemGradeComboBox cbItemGrade1;
	private Vector produtoGradeList;
	
	private Vector itensGrade1Grade;
	private Vector itensGrade2Grade;
	private Vector itensGrade3Grade;
	
	private Vector itensGrade3List;
	private Vector itensGrade2List;
	
	private String cdItemGrade1Default;
	private LabelValue lbDsProdutoContainer;
	
	public ButtonAction btAtualizaEstoqueOnline;
	public ButtonPopup btProduto;
	
	public PushButtonGroupBase btTabOptions;
	public boolean btTabOptionsVisible;
	
	public String cdTabelaPreco;
	
	public BaseScrollContainer containerGrid;
	public BaseScrollContainer containerList;
	
	public boolean isLoaded = false;
	public int activeTabContainer = 0;
	
	protected int[] precoGrid;
	protected String[] precoList;
	protected boolean permiteEventoClickNaGrade;
	private Vector itemPedidoList;
	private boolean semItensInformados;
	protected boolean somenteNovidades;
	protected RelNovidadeProd relNovidadeProdFilter;
	protected boolean showMenuProduto;
	private String cdLocalEstoque;
	private Map<String, Produto> hashProdutoGrade;
	
	public ListEstoqueGradeWindow(ItemPedido itemPedido, Vector itemPedidoList, boolean permiteEventoClickNaGrade, String cdLocalEstoque) throws SQLException {
		this(itemPedido.getProduto(),  itemPedido.cdItemGrade1, itemPedido.getCdTabelaPreco(), itemPedido.vlTotalItemPedido, itemPedidoList, permiteEventoClickNaGrade, cdLocalEstoque);
	}
	
	public ListEstoqueGradeWindow(Produto produto, String cdItemGrade1Default, String cdTabelaPreco, double vlTotalItemPedido, Vector itemPedidoList, boolean permiteEventoClickNaGrade, String cdLocalEstoque) throws SQLException {
		this(produto, cdItemGrade1Default, cdTabelaPreco, vlTotalItemPedido, itemPedidoList, permiteEventoClickNaGrade, null, cdLocalEstoque);
	}
	
	public ListEstoqueGradeWindow(Produto produto, String cdItemGrade1Default, String cdTabelaPreco, double vlTotalItemPedido, Vector itemPedidoList, boolean permiteEventoClickNaGrade, RelNovidadeProd relNovidadeProdFilter, String cdLocalEstoque) throws SQLException {
		super(Messages.ESTOQUE_NOME_ENTIDADE);
		this.permiteEventoClickNaGrade = permiteEventoClickNaGrade;
		this.somenteNovidades = relNovidadeProdFilter != null;
		this.relNovidadeProdFilter = relNovidadeProdFilter;
		this.cdLocalEstoque = cdLocalEstoque;
		setThisVariables(produto, cdItemGrade1Default, cdTabelaPreco, vlTotalItemPedido);
		configureLabels();
		cbItemGrade1 = new ItemGradeComboBox();
		cbItemGrade1.setEnabled(LavenderePdaConfig.getConfigGradeProduto() != 4);
		if (somenteNovidades && LavenderePdaConfig.permiteAcessoAoMenuProdutoAtravesRelNovidade) {
			btProduto = new ButtonPopup(Messages.PRODUTO);
		}
		ckInserido = new CheckBoolean(Messages.ESTOQUE_GRADE_INSERIDO);
		ckPrevisto = new CheckBoolean(Messages.ESTOQUE_GRADE_PREVISTO);
		this.itemPedidoList = itemPedidoList;
		configureBtEstoqueOnline();
		inicializaEditsFiltro();
		generateTabsContainers();
		configureBtTabOptions();
		loadDadosGradeProduto();
		setDefaultRect();
		loadDefaultModeList();
		habilitaOrdenacao();
	}

	private void habilitaOrdenacao() {
		if (gradeGrid != null) {
			gradeGrid.disableSort = permiteEventoClickNaGrade;
		}
		if (gradeList != null) {
			gradeList.disableSort = permiteEventoClickNaGrade;
		}
	}

	private void inicializaEditsFiltro() {
		edFiltro = new EditFiltro("999999999", 50);
		if (LavenderePdaConfig.isUsaFiltroGradeProdutoPorCamera() && LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			btLeitorCamera = new ButtonAction("  " + Messages.CAMERA + "  ", "images/barcode.png");
		}	
	}

	private void loadDefaultModeList() {
		if(!(LavenderePdaConfig.isGradeProdutoModoLista() && isShowListTabPanel())) {
			return;
		}
		controlaTabList();
		paintList();
	}

	private void setThisVariables(Produto produto, String cdItemGrade1Default, String cdTabelaPreco, double vlTotalItemPedido) {
		this.produto = produto;
		this.cdTabelaPreco = cdTabelaPreco;
		this.cdItemGrade1Default = cdItemGrade1Default;
		this.semItensInformados = vlTotalItemPedido == 0;
	}

	private double getQtEstoqueProduto(Produto produto) throws SQLException {
		if (somenteNovidades) return RelNovidadeProdService.getInstance().getSumQtEstoque(produto, null, null, null, relNovidadeProdFilter);
		else if (LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade()) return EstoqueService.getInstance().getSumEstoqueAgrupadorGrade(produto.getDsAgrupadorGrade(), cdItemGrade1Default, cdLocalEstoque, true);
		return EstoqueService.getInstance().getSumEstoqueGradeProduto(produto.cdProduto, null);
	}

	private void configureBtEstoqueOnline() {
		if (!LavenderePdaConfig.usaEstoqueOnline) {
			return;
		}
		btAtualizaEstoqueOnline = new ButtonAction(UiUtil.getColorfulImage("images/reload.png", (UiUtil.getControlPreferredHeight() / 3) * 2, (UiUtil.getControlPreferredHeight() / 3) * 2));
		btAtualizaEstoqueOnline.transparentBackground = true;
		btAtualizaEstoqueOnline.useBorder = false;
		btAtualizaEstoqueOnline.setEnabled(true);
	}

	private void configureLabels() throws SQLException {
		double qtEstoqueProduto = getQtEstoqueProduto(produto);
		String dsProduto = LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? produto.getDsAgrupadorGrade() : produto.toString();
		lbDsProdutoContainer = new LabelValue(dsProduto);
		new BaseToolTip(lbDsProdutoContainer, dsProduto);
		lbQtTotalEstoque = new LabelValue(StringUtil.getStringValueToInterface(qtEstoqueProduto, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface));
		lbQtTotalEstoqueGrade = new LabelValue();
		lbItemGrade1 = new LabelName(" ");
		lbQtTotalEstoqueGradeName = new LabelName(Messages.ESTOQUE_TOTAL_ESTOQUE_GRADE);
		if (LavenderePdaConfig.usaGradeProduto5()) {
			double qtEstoquePrevisto = EstoqueService.getInstance().getSumEstoquePrevistoAgrupadorGrade(produto.getDsAgrupadorGrade(), cdItemGrade1Default, cdLocalEstoque); 
			lbQtTotalEstoquePrevistoName = new LabelName(Messages.ESTOQUE_LABEL_PREVISTO);
			lbQtTotalEstoquePrevisto = new LabelValue(StringUtil.getStringValueToInterface(qtEstoquePrevisto, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface));
		}
	}
	
	private void generateTabsContainers() {
		containerList = new BaseScrollContainer(false, false);
		containerGrid = new BaseScrollContainer(false, false);
	}

	private void configureBtTabOptions() {
		btTabOptions = new PushButtonGroupBase(new String[]{Messages.GRADE_LABEL_TAB_MODO_GRADE, Messages.GRADE_LABEL_TAB_MODO_LISTA}, true, 0, -1, 8, 1, true, PushButtonGroupBase.BUTTON);
		btTabOptions.setFont(UiUtil.defaultFontSmall);
		btTabOptions.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.baseForeColorSystem);
		btTabOptionsVisible = false;
	}
	
	private void controlaTabList() {
		getSecondContainer().setVisible(true);
		getFirstContainer().setVisible(false);
		btTabOptions.setSelectedIndex(1);
		activeTabContainer = 1;
		isLoaded = true;
	}

	private void controlaTabGrid() {
		getSecondContainer().setVisible(false);
		getFirstContainer().setVisible(true);
		btTabOptions.setSelectedIndex(0);
		activeTabContainer = 0;
		isLoaded = true;
	}

	private void loadDadosGradeProduto() throws SQLException {
		String cdItemGrade1 = permiteEventoClickNaGrade ? cdItemGrade1Default : null;
		produtoGradeList = getProdutoGradeList(cdItemGrade1);
		if (ValueUtil.isNotEmpty(produtoGradeList) && LavenderePdaConfig.getConfigGradeProduto() != 2) {
			int size = produtoGradeList.size();
			if (size > 0) {
				ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[0];
				lbItemGrade1.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtGrade.cdTipoItemGrade1));
				cbItemGrade1.popupTitle = lbItemGrade1.getValue();
			}
			populaItemGrade1(size);
			cbItemGrade1.add(itensGrade1Grade);
			if (size > 0) {
				ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[0];
				cbItemGrade1.setValue(produtGrade.cdTipoItemGrade1, cdItemGrade1Default);
			}
			if (cbItemGrade1.getValue() == null) {
				cbItemGrade1.setSelectedIndex(0);
			}
		}
		if (cbItemGrade1.size() > 1) {
			atualizaEstoqueGrade();
		}
		montaGridGradeEstoque();
	}


	private Vector getProdutoGradeList(String cdItemGrade1) throws SQLException {
		return LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? ProdutoGradeService.getInstance().findAllProdutoGradeAgrupadorGrade(cdTabelaPreco, produto.getDsAgrupadorGrade()) : ProdutoGradeService.getInstance().findProdutoGradeList(produto.cdProduto, cdTabelaPreco, cdItemGrade1, relNovidadeProdFilter);
	}

	private void populaItemGrade1(int size) throws SQLException {
		itensGrade1Grade = new Vector();
		for (int i = 0; i < size; i++) {
			ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[i];
			ItemGrade itemGrade1 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade1, produtGrade.cdItemGrade1);
			if (itemGrade1 != null) {
				if (itensGrade1Grade.indexOf(itemGrade1) == -1) {
					if(ValueUtil.isEmpty(dsFiltro) || !LavenderePdaConfig.isUsaFiltroGradeProduto()) {
						itensGrade1Grade.addElement(itemGrade1);
					} else {
						if (verificaFiltroGrade(itemGrade1, produtGrade)) {
							itensGrade1Grade.addElement(itemGrade1);
						}
					}
				}
			}
		}
		itensGrade1Grade.qsort();
	}
	
	
	private void montaFiltroGrade() {
		boolean filtroPorCodigoBarras = LavenderePdaConfig.isUsaFiltroGradeProdutoPorCamera() && LavenderePdaConfig.usaCameraParaLeituraCdBarras();
		if (LavenderePdaConfig.isUsaFiltroGradeProduto()) {
			int espacoComSemBotao = (filtroPorCodigoBarras ? UiUtil.getButtonPreferredHeight() + 60 : 0);
			UiUtil.add(this, edFiltro, getLeft(), getNextY(), getWFill() - espacoComSemBotao);
			if (filtroPorCodigoBarras) {
				UiUtil.add(this, btLeitorCamera, AFTER + 7, SAME, UiUtil.getButtonPreferredHeight() + 50, UiUtil.getButtonPreferredHeight());
			}
		}	
	}
	
	private void btFiltrarClick(String filter) throws SQLException {
		if (!ValueUtil.valueEquals(dsFiltro, filter)) {
			dsFiltro = filter;
		}
		gradeGrid.removeAllElements();
		populaItemGrade1(produtoGradeList.size());
		montaGridGradeEstoque();
		reloadAtualTabContainer();
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			dsFiltro = null;
			edFiltro.clear();
		}
	}
	
	protected void realizaLeituraCamera() throws SQLException {
		StringBuffer dsInfo = new StringBuffer("");
		dsFiltro = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, dsInfo.toString());
		if(dsFiltro != null) {
			btFiltrarClick(dsFiltro);
		}
	}

	private boolean verificaFiltroGrade(ItemGrade item, ProdutoGrade produto) {
		return ValueUtil.isNotEmpty(dsFiltro)
				 && (item.dsItemGrade.toUpperCase().contains(dsFiltro.toUpperCase())
				 ||(produto.nuCodigoBarras != null ? produto.nuCodigoBarras.toUpperCase().contains(dsFiltro.toUpperCase()) : false));
	}

	private void atualizaEstoqueGrade() throws SQLException {
		if (cbItemGrade1.getValue() != null) {
			lbQtTotalEstoqueGrade.setValue(EstoqueService.getInstance().getSumEstoqueGradeProdutoNivel1(produto.cdProduto, cbItemGrade1.getValue(), null));
		}
	}

	private void montaGridGradeEstoque() throws SQLException {
		resetGrades();
		if (ValueUtil.isEmpty(produtoGradeList)) {
			return;
		}
		String cdItemGrade1 = cbItemGrade1.getValue();
		for (int i = 0; i < produtoGradeList.size(); i++) {
			ProdutoGrade produtoGrade = (ProdutoGrade)produtoGradeList.items[i];
			if (LavenderePdaConfig.usaGradeProduto2()) {
				controlaItemGrade1(produtoGrade);
			}
			if (!ValueUtil.isEmpty(produtoGrade.cdItemGrade2) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdItemGrade2) && (!LavenderePdaConfig.usaGradeProduto5() || ValueUtil.valueEquals(cdItemGrade1, produtoGrade.cdItemGrade1))) {
				controlaItemGrade2(produtoGrade);
			}
			if (!ValueUtil.isEmpty(produtoGrade.cdItemGrade3) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdItemGrade3) && (!LavenderePdaConfig.usaGradeProduto5() || ValueUtil.valueEquals(cdItemGrade1, produtoGrade.cdItemGrade1))) {
				controlaItemGrade3(produtoGrade);
			}
		}
		sortItensGrade();
		callLoadListGridMethods();
	}

	private void sortItensGrade() {
		if (LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto || LavenderePdaConfig.usaGradeProduto5()) {
			SortUtil.qsortInt(itensGrade1Grade.items, 0, itensGrade1Grade.size() - 1, true);
			SortUtil.qsortInt(itensGrade2Grade.items, 0, itensGrade2Grade.size() - 1, true);
			SortUtil.qsortInt(itensGrade3Grade.items, 0, itensGrade3Grade.size() - 1, true);
		} else {
			itensGrade1Grade.qsort();
			itensGrade2Grade.qsort();
			itensGrade3Grade.qsort();
		}
	}

	private void resetGrades() {
		itensGrade1Grade = new Vector(0);
		itensGrade2Grade = new Vector(0);
		itensGrade3Grade = new Vector(0);
		itensGrade2List = new Vector(0);
		itensGrade3List = new Vector(0);
	}

	private void callLoadListGridMethods() throws SQLException {
		if (itensGrade3Grade.size() > 0) {
			carregaListGradeEstoque3();
			checkListOrGrid(3);
		} else if (itensGrade2Grade.size() > 0) {
			if (LavenderePdaConfig.usaGradeProduto2()) {
				carregaListGradeEstoque2();
			}
			checkListOrGrid(2);
		} else if (itensGrade1Grade.size() > 0) {
			checkListOrGrid(1);
		}
	}

	private void controlaItemGrade3(ProdutoGrade produtoGrade) throws SQLException {
		ItemGrade itemGrade3 = ItemGradeService.getInstance().getItemGrade(produtoGrade.cdTipoItemGrade3, produtoGrade.cdItemGrade3);
		if ((itemGrade3 != null) && (itensGrade3Grade.indexOf(itemGrade3) == -1)) {
			itemGrade3.nuOrdemColuna = produtoGrade.nuOrdemColuna;
			itensGrade3Grade.addElement(itemGrade3);
		}
	}

	private void controlaItemGrade2(ProdutoGrade produtoGrade) throws SQLException {
		ItemGrade itemGrade2 = ItemGradeService.getInstance().getItemGrade(produtoGrade.cdTipoItemGrade2, produtoGrade.cdItemGrade2);
		if ((itemGrade2 != null) && (itensGrade2Grade.indexOf(itemGrade2) == -1)) {
			itemGrade2.nuOrdemLinha = produtoGrade.nuOrdemLinha;
			itensGrade2Grade.addElement(itemGrade2);
		}
	}

	private void controlaItemGrade1(ProdutoGrade produtoGrade) throws SQLException {
		if (!ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtoGrade.cdItemGrade1)) {
			ItemGrade itemGrade1 = ItemGradeService.getInstance().getItemGrade(produtoGrade.cdTipoItemGrade1, produtoGrade.cdItemGrade1);
			if ((itemGrade1 != null) && (itensGrade1Grade.indexOf(itemGrade1) == -1)) {
				if(ValueUtil.isEmpty(dsFiltro) || !LavenderePdaConfig.isUsaFiltroGradeProduto()) {
					itensGrade1Grade.addElement(itemGrade1);
				} else {
					if (verificaFiltroGrade(itemGrade1, produtoGrade)) {
						itensGrade1Grade.addElement(itemGrade1);
					}
				}
			}
		}
	}
	
	private void carregaListGradeEstoque2() throws SQLException {
		Vector itemGrade2 = ItemGradeService.getInstance().findGradeEstoqueForList(2, produto.cdProduto, null, cdTabelaPreco);
		for (int i = 0; i < itemGrade2.size(); i++) {
			ItemGrade itemGrade = (ItemGrade) itemGrade2.items[i];
			itensGrade2List.addElement(itemGrade);
		}
	}
	
	private void carregaListGradeEstoque3() throws SQLException {
		Vector itemGrade3 = ItemGradeService.getInstance().findGradeEstoqueForList(3, produto.cdProduto, cbItemGrade1.getValue(), cdTabelaPreco);
		for (int i = 0; i < itemGrade3.size(); i++) {
			ItemGrade itemGrade = (ItemGrade) itemGrade3.items[i];
			itensGrade3List.addElement(itemGrade);
		}
	}

	private void checkListOrGrid(int nuGrade) throws SQLException {
		if(nuGrade < 1 || nuGrade > 3) {
			return;
		}
		if(nuGrade == 1) {
			if (LavenderePdaConfig.usaGradeProduto4() && gradeGrid != null ) return;
			controlGridGrade1();
		} else if(nuGrade == 2) {
			controlListGrade2();
			controlGridGrade2();
		} else if(nuGrade == 3) {
			controlListGrade3();
			controlGridGrade3();
		}
	}

	private void controlGridGrade1() throws SQLException {
		montaGridGrade1();
		carregaGridGrade1();
	}
	
	private void controlListGrade2() throws SQLException {
		if (LavenderePdaConfig.getConfigGradeProduto() != 2) {
			return;
		}
		montaListGrade2();
		carregaListGrade2();					
	}
	
	private void controlGridGrade2() throws SQLException {
		montaGridGrade2();
		carregaGridGrade2();
	}
	
	private void controlListGrade3() throws SQLException {
		montaListGrade3();
		carregaListGrade3();
		if(LavenderePdaConfig.isGradeProdutoModoLista()) {
			gradeList.qsort(2, true);
		}
	}

	private void controlGridGrade3() throws SQLException {
		montaGridGrade3();
		carregaGridGrade3();
	}

	//@Override
	public void initUI() {
	   try {
			super.initUI();
			int widthLabels = fm.stringWidth("999.999.999,");
			UiUtil.add(this, lbDsProdutoContainer, getLeft(), getTop(), FILL, LabelContainer.getStaticHeight());
			montaFiltroGrade();
		   if (LavenderePdaConfig.getConfigGradeProduto() != 2) {
				UiUtil.add(this, lbItemGrade1, SAME, AFTER);
				UiUtil.add(this, cbItemGrade1, SAME, AFTER);
			}
			
			if (!LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
				lbQtTotalEstoqueName = new LabelName(Messages.ESTOQUE_TOTAL_ESTOQUE);
				UiUtil.add(this, lbQtTotalEstoqueName, lbQtTotalEstoque, getLeft(), AFTER + HEIGHT_GAP, widthLabels, PREFERRED);
				UiUtil.add(this, new BaseToolTip(lbQtTotalEstoque, lbQtTotalEstoque.getText()));
			} else {
				UiUtil.add(this, lbQtTotalEstoque, getLeft(), AFTER + HEIGHT_GAP, widthLabels, PREFERRED);
				lbQtTotalEstoque.setVisible(false);
			}
			if (LavenderePdaConfig.usaGradeProduto5()) {
				UiUtil.add(this, lbQtTotalEstoquePrevistoName, lbQtTotalEstoquePrevisto, calculaCentroPosition(lbQtTotalEstoquePrevistoName), lbQtTotalEstoqueName.getY(), widthLabels, PREFERRED);
				new BaseToolTip(lbQtTotalEstoquePrevisto, lbQtTotalEstoquePrevisto.getText());
			} else if (isApresentaEstoqueGrade()) {
				UiUtil.add(this, lbQtTotalEstoqueGradeName, lbQtTotalEstoqueGrade, calculaCentroPosition(lbQtTotalEstoqueGradeName) , BEFORE, widthLabels, PREFERRED);
				UiUtil.add(this, new BaseToolTip(lbQtTotalEstoqueGrade, lbQtTotalEstoqueGrade.getText()));
			}
			
			if (LavenderePdaConfig.usaEstoqueOnline && !somenteNovidades) {
				UiUtil.add(this, btAtualizaEstoqueOnline, RIGHT - WIDTH_GAP_BIG, lbQtTotalEstoque.getY() - HEIGHT_GAP, UiUtil.getControlPreferredHeight(), UiUtil.getControlPreferredHeight());
			}
			if (somenteNovidades && LavenderePdaConfig.permiteAcessoAoMenuProdutoAtravesRelNovidade) {
				addButtonPopup(btProduto);
				addBtFechar();
			}
			if (LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade()) {
				UiUtil.add(this, ckInserido, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(this, ckPrevisto, calculaCentroPosition(lbQtTotalEstoquePrevistoName), SAME);
			}
		   if (isShowListTabPanel() || (isShowListGrade2TabPanel() && LavenderePdaConfig.usaGradeProduto2())) {
				UiUtil.add(this, btTabOptions, CENTER, AFTER + HEIGHT_GAP_BIG, UiUtil.getControlPreferredHeight() * 3, UiUtil.getControlPreferredHeight());
				btTabOptionsVisible = true;
				if (gradeGrid != null && btTabOptions.getSelectedIndex() == 0) {
					paintGrid(false);
				}
				if (gradeList != null && btTabOptions.getSelectedIndex() == 1) {
					paintList();
				}
			} else {
				if (gradeGrid != null) {
					paintGrid(false);
				}
			}
			
			carregaGrid();
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	private void paintGrid(boolean fillScreen) {
		remove(getFirstContainer());
		getFirstContainer().removeAll();
		int posYGrid = (btTabOptionsVisible) ? btTabOptions.getY() + btTabOptions.getHeight() + HEIGHT_GAP : lbQtTotalEstoque.getY() + lbQtTotalEstoque.getHeight() + HEIGHT_GAP;
		if (LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade()) posYGrid = ckInserido.getY2() + HEIGHT_GAP;
		UiUtil.add(this, getFirstContainer(), LEFT, posYGrid, FILL, FILL);
		UiUtil.add(getFirstContainer(), gradeGrid, LEFT, TOP + HEIGHT_GAP, FILL, fillScreen ? FILL : getGridHeight());
	}
	
	private int getGridHeight() {
		Vector items = getGridCurrentItemList();
		int gradeHeight = items == null ? 0 : (items.size() + 2) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		int remainingWindowSpace = getFirstContainer().getHeight() - cFundoFooter.getHeight();
		return gradeHeight > remainingWindowSpace ? FILL : gradeHeight;
	}
	
	private Vector getGridCurrentItemList() {
		if (LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			return itensGrade2Grade;
		} else if (LavenderePdaConfig.usaGradeProduto2() || LavenderePdaConfig.isGradeProdutoModoLista()) {
			return itensGrade1Grade;
		}
		return null;
	}

	private void paintList() {
		remove(getSecondContainer());
		getSecondContainer().remove(gradeList);
		int posYGrid = (btTabOptionsVisible) ? btTabOptions.getY() + btTabOptions.getHeight() + HEIGHT_GAP : lbQtTotalEstoque.getY() + lbQtTotalEstoque.getHeight() + HEIGHT_GAP;
		UiUtil.add(this, getSecondContainer(), LEFT, posYGrid, FILL, FILL);
		UiUtil.add(getSecondContainer(), gradeList, LEFT, TOP + HEIGHT_GAP , FILL, FILL);
	}
	
	@Override
	public void reposition() {
		if (isApresentaEstoqueGrade()) {
			lbQtTotalEstoqueGradeName.setSet(calculaCentroPosition(null) , BEFORE);
			lbQtTotalEstoqueGrade.setSet(calculaCentroPosition(null) , AFTER);
		}
		if(btTabOptions.getSelectedIndex() == 0 && btTabOptionsVisible) {
			remove(containerGrid);
			paintGrid(false);
		}
		if(btTabOptions.getSelectedIndex() == 1 && btTabOptionsVisible) {
			remove(containerList);
			paintList();
		}
		super.reposition();
	}

	private void carregaGrid() throws SQLException {
    	if (itensGrade3Grade.size() > 0) {
    		checkListOrGrid(3);
    	} else if (itensGrade2Grade.size() > 0) {
    		checkListOrGrid(2);
    	} else if (itensGrade1Grade.size() > 0) {
    		checkListOrGrid(1);
    	} 
	}

	//@Override
	public void popup() {
	   try {
			if (ValueUtil.isNotEmpty(produtoGradeList)) {
				super.popup();
			} else {
				if (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO)) {
					UiUtil.showInfoMessage(Messages.INFO_ESTOQUE, MessageUtil.getMessage(Messages.ESTOQUE, ValueUtil.getIntegerValue(EstoqueService.getInstance().getQtEstoque(produto.cdProduto, Estoque.CD_LOCAL_ESTOQUE_PADRAO))));
				} else {
					UiUtil.showInfoMessage(Messages.INFO_ESTOQUE, MessageUtil.getMessage(Messages.ESTOQUE, EstoqueService.getInstance().getQtEstoque(produto.cdProduto, Estoque.CD_LOCAL_ESTOQUE_PADRAO)));	
				}
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		if (gradeGrid != null) {
			gradeGrid.setItems(null);
		}
		if (gradeList != null) {
			gradeList.setItems(null);
		}
	}

	@Override
	public void onEvent(Event event) {
	   try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btLeitorCamera){
						realizaLeituraCamera();
					} else if (event.target == btProduto) {
						showMenuProduto = true;
						btFecharClick();
					} else if (event.target == ckInserido || event.target == ckPrevisto) {
						reloadAtualTabContainer();
					} else {
						controlTabEvent(event);
						controlComboGrade1Event(event);
						controlAtualizaEstoqueEvent(event);
					}
					break;
				}
				case EditIconEvent.PRESSED: {
					if (event.target == edFiltro) {
						btFiltrarClick(edFiltro.getText());
					}
					break;
				}
				case KeyEvent.SPECIAL_KEY_PRESS: {
		    		KeyEvent ke = (KeyEvent) event; 
		    		if (ke.isActionKey() && (ke.target instanceof EditFiltro)) {
		    			btFiltrarClick(edFiltro.getText());
		    		}
		    		break;
		    	}
				case KeyboardEvent.KEYBOARD_PRESS: {
					if (event.target == edFiltro) {
						btFiltrarClick(edFiltro.getText());
					} 
					break;
		    	}
				case PenEvent.PEN_UP: {
					eventoClickNaGrade(event);
				}
			}
    	} catch (Throwable ex) {
    		ExceptionUtil.handle(ex);
		}
	}

	private void eventoClickNaGrade(Event event) throws SQLException {
		if (permiteEventoClickNaGrade) {
			if (event.target == gradeGrid) {
				gradeGridSelecionado();
			} else if (event.target == gradeList) {
				gradeListSelecionado();
			}
		}
	}
	
	private void gradeGridSelecionado() throws SQLException {
		precoGrid = gradeGrid.getSelectedXYIndex();
		int qtdColunas =  ValueUtil.isNotEmpty(itensGrade3List) ? gradeGrid.captions.length  - 2 : gradeGrid.captions.length - 1; 
		if (precoGrid[0] >= 0 && precoGrid[0] < gradeGrid.size() && precoGrid[1] > 0 && precoGrid[1] <= qtdColunas) {
			precoList = null;
			fecharWindow();
		}
	}
	
	private void gradeListSelecionado() throws SQLException {
		int[]posicaoSelecionada = gradeList.getSelectedXYIndex();
		if (posicaoSelecionada[0] >= 0 && posicaoSelecionada[1] >= 0) {
			String dsGrade1 = gradeList.getCellText(gradeList.getSelectedXYIndex()[0], 0);
			String dsGrade2 = gradeList.getCellText(gradeList.getSelectedXYIndex()[0], 1);
			precoList = new String[] {dsGrade1, dsGrade2};
			precoGrid = null;
			fecharWindow();
		}
	}

	private void controlAtualizaEstoqueEvent(Event event) throws SQLException {
		if (event.target == btAtualizaEstoqueOnline) {
			atualizadoInfoEstoque();
			reloadAtualTabContainer();
		}
	}

	private void controlComboGrade1Event(Event event) throws SQLException {
		if (event.target == cbItemGrade1) {
			if (LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade()) {
				double qtEstoqueProduto = EstoqueService.getInstance().getSumEstoqueAgrupadorGrade(produto.getDsAgrupadorGrade(), cbItemGrade1.getValue(), cdLocalEstoque, true);
				double qtEstoqueProdutoPrevisto = EstoqueService.getInstance().getSumEstoquePrevistoAgrupadorGrade(produto.getDsAgrupadorGrade(), cbItemGrade1.getValue(), cdLocalEstoque);
				lbQtTotalEstoque.setValue(EstoqueService.getInstance().getEstoqueToString(qtEstoqueProduto));
				lbQtTotalEstoquePrevisto.setValue(EstoqueService.getInstance().getEstoquePrevistoToString(qtEstoqueProdutoPrevisto));
				setHashProdutoGrade(null);
			}
			reloadAtualTabContainer();
		}
	}

	private void reloadAtualTabContainer() throws SQLException {
		if (gradeGrid != null) {
			remove(getContainerAtualTab());
			montaGridGradeEstoque();
			remove(getFirstContainer());
			
			if (btTabOptions.getSelectedIndex() == 0) {
				paintGrid(false);
			}
			if (btTabOptions.getSelectedIndex() == 1) {
				paintList();
			}
			carregaGrid();
		}
		atualizaEstoqueGrade();
	}

	private void controlTabEvent(Event event) {
		if (event.target == btTabOptions) {
			if (btTabOptions.getSelectedIndex() == 0) {
				remove(getSecondContainer());
				controlaTabGrid();
				paintGrid(false);
			}
			if (btTabOptions.getSelectedIndex() == 1) {
				remove(getFirstContainer());
				controlaTabList();
				paintList();
			}
		}
	}
	
	private void montaGridGrade1() throws SQLException {
		String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemGrade)itensGrade1Grade.items[0]).cdTipoItemGrade);
		GridColDefinition[] gridColDefiniton = { new GridColDefinition(dsTipoItemGrade, -75, LEFT), new GridColDefinition(Messages.GRADE_LABEL_QTESTOQUE, -20, LEFT)};
		configurateGradeGrid(gridColDefiniton, false);
		int itensGrade1GradeSize = itensGrade1Grade.size();
		gradeGrid.gridController.setRowBackColor(ColorUtil.componentsBorderColor, itensGrade1GradeSize);
		for (int i = 0; i < itensGrade1GradeSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade1Grade.items[i];
			gradeGrid.add(new String[]{StringUtil.getStringValue(itemGrade.dsItemGrade), ""});
		}
		gradeGrid.drawHighlight = false;
	}
	
	private void montaGridGrade2() throws SQLException {
		int itensGrade2GradeSize = itensGrade2Grade.size();
		if (LavenderePdaConfig.usaGradeProduto2()) {
			int widthCelulas = fm.stringWidth("00000,00");
			GridColDefinition[] gridColDefinition = new GridColDefinition[itensGrade2GradeSize + 2];
			gridColDefinition[0] = new GridColDefinition(" ", -30, LEFT);
			gridColDefinition[gridColDefinition.length - 1] = new GridColDefinition(Messages.LABEL_TOTAL_GRIDEDIT, widthCelulas, LEFT);
			for (int i = 0; i < itensGrade2GradeSize; i++) {
				ItemGrade itemGrade = (ItemGrade)itensGrade2Grade.items[i];
				gridColDefinition[i + 1] = new GridColDefinition(StringUtil.getStringValue(itemGrade.dsItemGrade), widthCelulas, LEFT);
			}
			configurateGradeGrid(gridColDefinition, true);
			gradeGrid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
			int itemGrade1GradeSize = itensGrade1Grade.size();
			for (int i = 0; i < itemGrade1GradeSize; i++) {
				ItemGrade itemGrade = (ItemGrade)itensGrade1Grade.items[i];
				String[] valueBase = new String[itensGrade2GradeSize + 2];
				valueBase[0] = StringUtil.getStringValue(itemGrade.dsItemGrade);
				gradeGrid.add(valueBase);
			}
			gradeGrid.drawHighlight = false;
		} else {
			String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemGrade)itensGrade2Grade.items[0]).cdTipoItemGrade);
			GridColDefinition[] gridColDefiniton = { new GridColDefinition(dsTipoItemGrade, -75, LEFT), new GridColDefinition(Messages.GRADE_LABEL_QTESTOQUE, -20, LEFT)};
			configurateGradeGrid(gridColDefiniton, true);
			gradeGrid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
			for (int i = 0; i < itensGrade2GradeSize; i++) {
				ItemGrade itemGrade = (ItemGrade)itensGrade2Grade.items[i];
				gradeGrid.add(new String[]{StringUtil.getStringValue(itemGrade.dsItemGrade), ""});
			}
			gradeGrid.drawHighlight = false;
		}
	}
	
	private void montaGridGrade3() {
		int widthCelulas = fm.stringWidth("00000,00");
		int itensGrade3GradeSize = itensGrade3Grade.size();
		GridColDefinition[] gridColDefinition = new GridColDefinition[itensGrade3GradeSize + 2];
		gridColDefinition[0] = new GridColDefinition(" ", -30, LEFT);
		gridColDefinition[gridColDefinition.length - 1] = new GridColDefinition(Messages.LABEL_TOTAL_GRIDEDIT, widthCelulas, RIGHT);
		for (int i = 0; i < itensGrade3GradeSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade3Grade.items[i];
			gridColDefinition[i + 1] = new GridColDefinition(StringUtil.getStringValue(itemGrade.dsItemGrade), widthCelulas, RIGHT);
		}
		configurateGradeGrid(gridColDefinition, true);
		int itensGrade2GradeSize = itensGrade2Grade.size();
		gradeGrid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
		gradeGrid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), gridColDefinition.length - 1);
		gradeGrid.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade2GradeSize);
		for (int i = 0; i < itensGrade2GradeSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade2Grade.items[i];
			String[] valueBase = new String[itensGrade3GradeSize + 2];
			valueBase[0] = StringUtil.getStringValue(itemGrade.dsItemGrade);
			gradeGrid.add(valueBase);
		}
		gradeGrid.drawHighlight = false;
	}
	
	private void carregaGridGrade1() throws SQLException {
		double qtTtEstoque = 0;
		String[] itemTotais = new String[2];
        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
        gradeGrid.totalizadorLabel = itemTotais[0];
		int itensGrade1GradeSize = itensGrade1Grade.size();
		int cor = buscaCor();
        for (int i = 0; i < itensGrade1GradeSize; i++) {
        	String[] item = gradeGrid.getItem(i);
        	ItemGrade itemGrade1 = (ItemGrade)itensGrade1Grade.items[i];
        	double qtEstoque = somenteNovidades ? RelNovidadeProdService.getInstance().getQtEstoqueNivel1(produto, itemGrade1.cdItemGrade) : EstoqueService.getInstance().getQtEstoqueNivel1(produto.cdProduto, itemGrade1.cdItemGrade);
        	grifaGrade1(i, qtEstoque);
        	qtTtEstoque += qtEstoque;
        	item[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        	ItemPedido itemPedido =  ItemPedidoService.getInstance().buscaItemPedidoPorGrade(itemGrade1.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, itemPedidoList);
        	if (naoPintaCelula(cor, itemPedido)) {
        		continue;
        	}
        	gradeGrid.gridController.setRowBackColor(cor, i);
        }
        if (LavenderePdaConfig.usaOrdenacaoEstoqueNaGradeProduto) {
        	gradeGrid.qsort(1);
        }
        if (!LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
        	itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTtEstoque) : qtTtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        	gradeGrid.add(itemTotais);
        }
	}
	
	private void carregaGridGrade2() throws SQLException {
		double qtTtEstoque = 0;
		int gradeGridSize = gradeGrid.size();
		int itensGrade2GradeSize = itensGrade2Grade.size();
		if (LavenderePdaConfig.usaGradeProduto2()) {
	        for (int i = 0; i < gradeGridSize; i++) {
	        	String[] item = gradeGrid.getItem(i);
	        	ItemGrade itemGrade1 = (ItemGrade)itensGrade1Grade.items[i];
	        	double ttItemGrade1 = 0;
	        	for (int j = 0; j < itensGrade2GradeSize; j++) {
	        		ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[j];
	        		double qtEstoque = somenteNovidades ? RelNovidadeProdService.getInstance().getQtEstoqueNivel2(produto, itemGrade1.cdItemGrade1, itemGrade2.cdItemGrade2) : EstoqueService.getInstance().getQtEstoqueNivel2(produto.cdProduto, itemGrade1.cdItemGrade, itemGrade2.cdItemGrade);
	        		item[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
	        		qtTtEstoque += qtEstoque;
	        		ttItemGrade1 += qtEstoque;
	        	}
	        	item[item.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(ttItemGrade1) : ttItemGrade1, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
	        }
	        //--
	        String[] itemTotais = new String[itensGrade2GradeSize + 2];
	        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
	        gradeGrid.totalizadorLabel = itemTotais[0];
	        for (int i = 0; i < itensGrade2GradeSize; i++) {
	    		ItemGrade itemGrade2 = (ItemGrade) itensGrade2Grade.items[i];
	        	Estoque estoqueExample = new Estoque();
	        	estoqueExample.cdEmpresa = SessionLavenderePda.cdEmpresa;
	        	estoqueExample.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
	        	estoqueExample.cdProduto = produto.cdProduto;
	        	estoqueExample.cdItemGrade2 = itemGrade2.cdItemGrade;
	        	double qtEstoque = somenteNovidades ? RelNovidadeProdService.getInstance().getSumQtEstoque(produto, null, itemGrade2.cdItemGrade2, null, relNovidadeProdFilter) : EstoqueService.getInstance().sumByExample(estoqueExample, "QTESTOQUE");
	        	itemTotais[i + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
			}
	        if (!LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
	        	itemTotais[itemTotais.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTtEstoque) : qtTtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
	        	gradeGrid.add(itemTotais);
	        }
		} else {
			String[] itemTotais = new String[2];
			itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
			gradeGrid.totalizadorLabel = itemTotais[0];
			int cor = buscaCor();
			for (int i = 0; i < itensGrade2GradeSize; i++) {
				String[] item = gradeGrid.getItem(i);
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[i];
				double qtEstoque = somenteNovidades ? RelNovidadeProdService.getInstance().getQtEstoqueNivel2(produto, cbItemGrade1.getValue(), itemGrade2.cdItemGrade2) : EstoqueService.getInstance().getQtEstoqueNivel2(produto.cdProduto, cbItemGrade1.getValue(), itemGrade2.cdItemGrade);
				qtTtEstoque += qtEstoque;
				item[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
				ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO, itemPedidoList); 
				if (naoPintaCelula(cor, itemPedido)) {
					continue;
				}
        		gradeGrid.gridController.setRowBackColor(cor, i); 
			}
			if (LavenderePdaConfig.usaOrdenacaoEstoqueNaGradeProduto) {
	        	gradeGrid.qsort(1);
	        }
			if (!LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
				itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTtEstoque) : qtTtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
				gradeGrid.add(itemTotais);
			}
		}
	}
	
	private int buscaCor() throws SQLException {
		TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
		CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(tema.cdEsquemaCor, 168);
		if (corSistema != null) {
			return Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB);
		}
		return 0;
	}
	
	private boolean naoPintaCelula(int cor, ItemPedido itemPedido) {
		return semItensInformados || itemPedido.getQtItemFisico() == 0 || cor == 0;
	}
	
	private void carregaGridGrade3() throws SQLException {
		double qtTtEstoque = 0;
		int gradeGridSize = gradeGrid.size();
		int itemGrade3GradeSize = itensGrade3Grade.size();
		int cor = buscaCor();
		Map<String, ProdutoGrade> hashProdutoGrade = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGradeService.getInstance().montaHashProdutoGradeAgrupador(cdTabelaPreco, cbItemGrade1.getValue(), produto.getDsAgrupadorGrade()) : null;
		Map<String, Estoque> hashEstoqueGrade = LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? EstoqueService.getInstance().montaHashEstoqueAgrupadorGrade(produto.getDsAgrupadorGrade(), cbItemGrade1.getValue(), cdLocalEstoque) : null;
		double[] totalItemGrade3 = new double[itemGrade3GradeSize];
		Map<String, Image> hashMarcadores = new HashMap<>();
		if (LavenderePdaConfig.apresentaMarcadorGrade) {
			ProdutoService.getInstance().loadImagesMarcadores(hashMarcadores, UiUtil.getControlPreferredHeight() / 3);
		}
		Vector listMarcadores = new Vector();
        for (int i = 0; i < gradeGridSize; i++) {
        	String[] item = gradeGrid.getItem(i);
        	ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[i];
        	double ttItemGrade2 = 0;
        	Image[] cellImages = LavenderePdaConfig.apresentaMarcadorGrade ? new Image[gradeGrid.captions.length] : null;
        	for (int j = 0; j < itemGrade3GradeSize; j++) {
        		ItemGrade itemGrade3 = (ItemGrade)itensGrade3Grade.items[j];
        		Double qtEstoque = getQtEstoqueGrade(itemGrade2, itemGrade3, hashEstoqueGrade);
        		if (qtEstoque == null) {
        			item[j + 1] = ValueUtil.VALOR_NI;
        			if (LavenderePdaConfig.usaGradeProduto5()) {
        				gradeGrid.gridController.setCelBackColor(ColorUtil.componentsBackColorDark, i , j + 1);
        			}
        			qtEstoque = 0d;
        		} else {
        			item[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        		}
        		qtTtEstoque += qtEstoque;
        		ttItemGrade2 += qtEstoque;
        		totalItemGrade3[j] += qtEstoque;
        		if (!LavenderePdaConfig.usaGradeProduto5()) {
        			ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedidoList);
        			if (naoPintaCelula(cor, itemPedido)) continue;
        			gradeGrid.gridController.setCelBackColor(cor, i , j + 1); 
        		}
        		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.apresentaMarcadorGrade) {
        			ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedidoList);
        			ProdutoGrade produto = hashProdutoGrade.get(ProdutoGrade.getGradeKey(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade));
        			if (itemPedido != null && produto != null) {
        				setImageMarcador(cellImages, hashMarcadores, produto, j + 1);
        			}
        		}
        		
        	}
        	if (LavenderePdaConfig.apresentaMarcadorGrade) {
        		listMarcadores.addElement(cellImages);
        	}
        	item[item.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(ttItemGrade2) : ttItemGrade2, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        }
        //--
        if (LavenderePdaConfig.apresentaMarcadorGrade) {
        	gradeGrid.setListCellImages(listMarcadores);
        	gradeGrid.keepImageAndText = true;
		}
        String[] itemTotais = new String[itemGrade3GradeSize + 2];
        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
        gradeGrid.totalizadorLabel = itemTotais[0];
        for (int i = 0; i < itemGrade3GradeSize; i++) {
    		double qtEstoque = LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? totalItemGrade3[i] : getQtEstoqueGrade3((ItemGrade)itensGrade3Grade.items[i]);
        	itemTotais[i + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
		}
        if (!LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
        	itemTotais[itemTotais.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTtEstoque) : qtTtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        	gradeGrid.add(itemTotais);
        }
	}
	
	private void setImageMarcador(Image[] cellImages, Map<String, Image> marcadores, ProdutoGrade produto, int j) {
		if (ValueUtil.isNotEmpty(produto.cdMarcador)) {
			cellImages[j] = marcadores.get(produto.cdMarcador);
		}
	}
	
	private double getQtEstoqueGrade3(ItemGrade itemGrade3) throws SQLException {
		Estoque estoqueExample = new Estoque();
		estoqueExample.cdEmpresa = SessionLavenderePda.cdEmpresa;
		estoqueExample.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		estoqueExample.cdProduto = produto.cdProduto;
		estoqueExample.cdItemGrade1 = cbItemGrade1.getValue();
		estoqueExample.cdItemGrade3 = itemGrade3.cdItemGrade;
		estoqueExample.flOrigemEstoque = "E";
		if (somenteNovidades) {
			return RelNovidadeProdService.getInstance().getSumQtEstoque(produto, cbItemGrade1.getValue(), null, itemGrade3.cdItemGrade, relNovidadeProdFilter);
		}
		return EstoqueService.getInstance().sumByExample(estoqueExample, "QTESTOQUE");
	}

	private Double getQtEstoqueGrade(ItemGrade itemGrade2, ItemGrade itemGrade3, Map<String, Estoque> hashEstoqueGrade) throws SQLException {
		if (somenteNovidades) {
			return RelNovidadeProdService.getInstance().getQtEstoqueNivel3(produto, cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
		} else if (LavenderePdaConfig.usaGradeProduto5()) {
			Estoque estoque = hashEstoqueGrade.get(Estoque.getKeyItemGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade));
			if (ckInserido.isChecked() && (estoque == null || !isProdutoInPedido(estoque.cdProduto))) {
				return null;
			}
			if (ckPrevisto.isChecked() && estoque != null) {
				return estoque.qtEstoquePrevisto;
			}
			if (estoque != null) {
				if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
					EstoqueService.getInstance().setEstoqueItemComParcialPrevisto(estoque);
				}
				return estoque.qtEstoque;
			} else {
				return null;
			}
		}
		return EstoqueService.getInstance().getQtEstoqueNivel3(produto.cdProduto, cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
	}
	
	private boolean isProdutoInPedido(String cdProduto) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (cdProduto.equals(itemPedido.cdProduto) && itemPedido.qtItemFisico > 0) {
				return true;
			}
		}
		return false;
	}
	
	private void montaListGrade2() throws SQLException {
		configurateGradeList(setGradeList2ColDefinition());
		int itensGrade2GradeSize = itensGrade2List.size();
		gradeList.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade2GradeSize);
		for (int i = 0; i < itensGrade2GradeSize; i++) {
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			String[] valueBase = new String[itensGrade2GradeSize + 2];
			valueBase[0] = StringUtil.getStringValue(itemGrade2.dsItemGrade1);
			valueBase[1] = StringUtil.getStringValue(itemGrade2.dsItemGrade2);
			gradeList.add(valueBase);
		}
		gradeList.drawHighlight = false;		
	}

	private void montaListGrade3() throws SQLException {
		configurateGradeList(setGradeList3ColDefinition());
		int itensGrade3ListSize = itensGrade3List.size();
		gradeList.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade3ListSize);
		for (int i = 0; i < itensGrade3ListSize; i++) {
			ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[i];
			String[] valueBase = new String[itensGrade3ListSize + 2];
			valueBase[0] = StringUtil.getStringValue(itemGrade3.dsItemGrade2);
			valueBase[1] = StringUtil.getStringValue(itemGrade3.dsItemGrade3);
			gradeList.add(valueBase);
		}
		gradeList.drawHighlight = false;
	}

	private GridColDefinition[] setGradeList3ColDefinition() throws SQLException {
		int widthCelulas = fm.stringWidth("000000000,00000");
		GridColDefinition[] gridColDefinition = new GridColDefinition[3];
		gridColDefinition[0] = new GridColDefinition(getItemGrade2ListTitle(), widthCelulas, LEFT);
		gridColDefinition[1] = new GridColDefinition(getItemGrade3ListTitle(), widthCelulas, LEFT);
		gridColDefinition[2] = new GridColDefinition(Messages.GRADE_LABEL_QTESTOQUE, widthCelulas, RIGHT);
		return gridColDefinition;
	}
	
	private GridColDefinition[] setGradeList2ColDefinition() throws SQLException {
		int widthCelulas = fm.stringWidth("000000000,00000");
		GridColDefinition[] gridColDefinition = new GridColDefinition[3];
		gridColDefinition[0] = new GridColDefinition(getItemGrade1ListTitle(), widthCelulas, LEFT);
		gridColDefinition[1] = new GridColDefinition(getItemGrade2ListTitle(), widthCelulas, LEFT);
		gridColDefinition[2] = new GridColDefinition(Messages.GRADE_LABEL_QTESTOQUE, widthCelulas, RIGHT);
		return gridColDefinition;
	}

	private String getItemGrade3ListTitle() throws SQLException {
		ItemGrade itemGradeTitle = (ItemGrade)itensGrade3Grade.items[0];
		return TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGradeTitle.cdTipoItemGrade);
	}
	
	private String getItemGrade2ListTitle() throws SQLException {
		ItemGrade itemGradeTitle = (ItemGrade)itensGrade2Grade.items[0];
		return TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGradeTitle.cdTipoItemGrade);
	}
	
	private String getItemGrade1ListTitle() throws SQLException {
		ItemGrade itemGradeTitle = (ItemGrade)itensGrade1Grade.items[0];
		return TipoItemGradeService.getInstance().getDsTipoItemGrade(itemGradeTitle.cdTipoItemGrade);
	}
	
	private void carregaListGrade3() throws SQLException {
		int gradeListSize = gradeList.size();
		int itensGrade3ListSize = itensGrade3List.size();
		int cor = buscaCor();
        for (int i = 0; i < gradeListSize; i++) {
        	ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[i];
    		double qtEstoque = somenteNovidades ? RelNovidadeProdService.getInstance().getQtEstoqueNivel3(produto, cbItemGrade1.getValue(), itemGrade3.cdItemGrade2, itemGrade3.cdItemGrade3) : EstoqueService.getInstance().getQtEstoqueNivel3(produto.cdProduto, cbItemGrade1.getValue(), itemGrade3.cdItemGrade2, itemGrade3.cdItemGrade3);
    		itemGrade3.qtEstoqueItem = qtEstoque;
    		gradeList.getItem(i)[2] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
    		ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade3.cdItemGrade2, itemGrade3.cdItemGrade3, itemPedidoList);
    		if (naoPintaCelula(cor, itemPedido)) {
    			continue;
    		}
    		gradeList.gridController.setRowBackColor(cor, i);
        }
        String[] itemTotal = new String[3];
        double qtTotal = 0;
        for (int i = 0; i < itensGrade3ListSize; i++) {
    		ItemGrade itemGradeTotal = (ItemGrade) itensGrade3List.items[i];
    		qtTotal += itemGradeTotal.qtEstoqueItem;
		}
        itemTotal[0] = Messages.LABEL_TOTAL_GRIDEDIT;
        gradeList.totalizadorLabel = itemTotal[0];
		itemTotal[itemTotal.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTotal) : qtTotal, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        gradeList.add(itemTotal);
	}
	
	private void carregaListGrade2() throws SQLException {
		int gradeListSize = gradeList.size();
		int itensGrade2ListSize = itensGrade2List.size();
		for (int i = 0; i < gradeListSize; i++) {
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			double qtEstoque = somenteNovidades ? RelNovidadeProdService.getInstance().getQtEstoqueNivel2(produto, itemGrade2.cdItemGrade1, itemGrade2.cdItemGrade2) : EstoqueService.getInstance().getQtEstoqueNivel2(produto.cdProduto, itemGrade2.cdItemGrade1, itemGrade2.cdItemGrade2);
			itemGrade2.qtEstoqueItem = qtEstoque;
			gradeList.getItem(i)[2] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
		}
		String[] itemTotal = new String[3];
		double qtTotal = 0;
		for (int i = 0; i < itensGrade2ListSize; i++) {
			ItemGrade itemGradeTotal = (ItemGrade) itensGrade2List.items[i];
			qtTotal += itemGradeTotal.qtEstoqueItem;
		}
		itemTotal[0] = Messages.LABEL_TOTAL_GRIDEDIT;
		gradeList.totalizadorLabel = itemTotal[0];
		itemTotal[itemTotal.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTotal) : qtTotal, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
		gradeList.add(itemTotal);
	}
	
	private void atualizadoInfoEstoque() {
		try {
			ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
			ps.nuMaxTentativas = 1;
			LavendereWeb2Tc lwWeb2Tc = new LavendereWeb2Tc(ps);
			Vector estoqueList = getEstoqueGradeList();
			int estoqueListSize = estoqueList.size();
			for (int i = 0; i < estoqueListSize; i++) {
				Estoque estoque = (Estoque) estoqueList.items[i];
				lwWeb2Tc.getEstoqueServidorAndUpdatePda(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, produto.cdProduto, estoque.cdItemGrade1, estoque.cdItemGrade2, estoque.cdItemGrade3, estoque.cdLocalEstoque);
			}
			double qtdEstoque = EstoqueService.getInstance().getSumEstoqueGradeProduto(produto.cdProduto, null);
			lbQtTotalEstoque.setValue(EstoqueService.getInstance().getEstoqueToString(qtdEstoque));
		} catch (ValidationException e) {
			throw new ValidationException(e.getMessage());
		} catch (Throwable e) {
			LogPdaService.getInstance().logSyncError("Erro ao atualizar o estoque. " + e.getMessage());
			throw new ValidationException("Erro ao atualizar o estoque. " + e.getMessage());
		}
	}
	
	private Vector getEstoqueGradeList() throws SQLException {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		estoqueFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		estoqueFilter.cdProduto = produto.cdProduto;
		estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		return EstoqueService.getInstance().findAllByExample(estoqueFilter);
	}
	
	private boolean isShowListTabPanel() {
		return itensGrade3Grade.size() > 0 && itensGrade2Grade.size() > 0 && !LavenderePdaConfig.usaGradeProduto5();
	}
	
	private boolean isShowListGrade2TabPanel() {
		return (itensGrade2Grade.size() > 0);
	}

	private void configurateGradeList(GridColDefinition[] gridColDefinition) {
		gradeList = UiUtil.createGridEdit(gridColDefinition, false);
		gradeList.captionsBackColor = ColorUtil.gridCaptionsBackColor;
		gradeList.setGridControllable();
		gradeList.disableSort = false;
	}
	
	private void configurateGradeGrid(GridColDefinition[] gridColDefinition, boolean disableSort) {
		gradeGrid = UiUtil.createGridEdit(gridColDefinition, false);
		gradeGrid.setGridControllable();
		gradeGrid.disableSort = disableSort;
		gradeGrid.drawRects = LavenderePdaConfig.usaGradeProduto5();
	}
	
	private BaseScrollContainer getContainerAtualTab() {
		return btTabOptions.getSelectedIndex() == 0 ? getFirstContainer() : getSecondContainer();
	}
	
	private BaseScrollContainer getFirstContainer() {
		return containerGrid;
	}
	
	private BaseScrollContainer getSecondContainer() {
		return containerList;
	}

	private boolean isApresentaEstoqueGrade() {
		return cbItemGrade1.size() > 1;
	}

	private int calculaCentroPosition(LabelName label) {
		return (Settings.screenWidth / 2) - label.fm.stringWidth(label.getText()) / 2;
	}
	
	private void grifaGrade1(int linha, double qtEstoque) throws SQLException {
		if (LavenderePdaConfig.getConfigGradeProduto() != ItemGrade.GRADE_NIVEL_2) {
			return;
		}
		if (qtEstoque > 0) {
			return;
		}
		gradeGrid.gridController.setRowBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK, linha);
	}
	
	public void setHashProdutoGrade(Map<String, Produto> hashProdutoGrade) {
		this.hashProdutoGrade = hashProdutoGrade;
	}

}
