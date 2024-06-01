package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.ValueChooser;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CorSistemaLavendere;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.business.validation.DescAcresMaximoException;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListPrecoGradeWindow extends WmwWindow {

	public int posYGrade;
	public int posYList;
	public int heightGrade;
	public int heightList;

	private BaseGridEdit gradeGrid;
	private BaseGridEdit gradeList;

	private EditFiltro edFiltro;
	protected ButtonAction btLeitorCamera;
	private String dsFiltro;
	private ItemGradeMultiComboBox cbItemGrade2;
	private ItemGradeMultiComboBox cbItemGrade3;
	private EditNumberFrac edVlItem;
	private ValueChooser chooserPctDesc;
	private BaseButton btReplicar;
	private ButtonPopup btResetarValores;
	private CheckBoolean ckInserido;

	private Produto produto;
	private LabelName lbItemGrade1;
	private ItemGradeComboBox cbItemGrade1;
	private Vector produtoGradeList;

	private Vector itensGrade1Grade;
	private Vector itensGrade2Grade;
	private Vector itensGrade3Grade;

	private Vector itensGrade3List;

	private Vector itemPedidoList;

	private String cdItemGrade1Default;
	private LabelValue lbDsProdutoContainer;

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
	private boolean semItensInformados;
	private Pedido pedido;
	private ItemPedido itemPedido;
	private Map<String, Produto> hashProdutoGrade;

	public ListPrecoGradeWindow(ItemPedido itemPedido, Vector itensGrade2Grade, Vector itensGrade3Grade, Vector itemPedidoList, boolean permiteEventoClickNaGrade, Map<String, Produto> hashProdutoGrade) throws SQLException {
		super(Messages.BOTAO_PRECOS);
		this.hashProdutoGrade = hashProdutoGrade;
		this.permiteEventoClickNaGrade = permiteEventoClickNaGrade;
		setThisVariables(itemPedido);
		configureLabels();
		cbItemGrade1 = new ItemGradeComboBox();
		cbItemGrade1.setEnabled(false);
		this.itensGrade3Grade = itensGrade3Grade;
		this.itensGrade2Grade = itensGrade2Grade;
		this.itemPedidoList = itemPedidoList;
		this.itemPedido = itemPedido;
		if (LavenderePdaConfig.usaGradeProduto5()) {
			createComponentsGrade5(itensGrade2Grade, itensGrade3Grade);
		}
		inicializaEditsFiltro();
		generateTabsContainers();
		configureBtTabOptions();
		loadDadosGradeProduto();
		selectCkInserido();
		montaGridGradePreco();
		setDefaultRect();
		loadDefaultModeList();
		habilitaOrdenacao();
	}

	public ListPrecoGradeWindow(ItemPedido itemPedido, Vector itensGrade2Grade, Vector itensGrade3Grade, Vector itemPedidoList, boolean permiteEventoClickNaGrade) throws SQLException {
		this(itemPedido, itensGrade2Grade, itensGrade3Grade, itemPedidoList, permiteEventoClickNaGrade, null);
	}

	private void createComponentsGrade5(Vector itensGrade2Grade, Vector itensGrade3Grade) {
		cbItemGrade2 = new ItemGradeMultiComboBox();
		cbItemGrade3 = new ItemGradeMultiComboBox();
		cbItemGrade2.add(ItemGradeService.getInstance().filtraItemGradeListPorItemPedido(itensGrade2Grade, itemPedidoList, false));
		cbItemGrade3.add(ItemGradeService.getInstance().filtraItemGradeListPorItemPedido(itensGrade3Grade, itemPedidoList, true));
		cbItemGrade2.drawBackgroundWhenDisabled = true;
		cbItemGrade3.drawBackgroundWhenDisabled = true;
		edVlItem = new EditNumberFrac("", 9);
		edVlItem.setValue(0d);
		chooserPctDesc = new ValueChooser(-100d, 0.1, 100d, ValueUtil.doublePrecisionInterface, false);
		chooserPctDesc.setID("chooserPctDesc");
		btReplicar = new BaseButton(Messages.BOTAO_REPLICAR_GRADE);
		btResetarValores = new ButtonPopup(Messages.BOTAO_RESETAR_VALORES);
		cbItemGrade2.setEnabled(false);
		cbItemGrade3.setEnabled(false);
		edVlItem.setEnabled(false);
		chooserPctDesc.disableAll();
		btReplicar.setEnabled(false);
		btResetarValores.setEnabled(true);
		ckInserido = new CheckBoolean(Messages.ESTOQUE_GRADE_INSERIDO);
	}

	private void habilitaOrdenacao() {
		if (gradeGrid != null && !LavenderePdaConfig.usaGradeProduto5()) {
			gradeGrid.disableSort = permiteEventoClickNaGrade;
		}
		if (gradeList != null) {
			gradeList.disableSort = permiteEventoClickNaGrade;
		}
	}


	private int getGridHeight() {
		Vector items = getGridCurrentItemList();
		int gradeHeight = items == null ? 0 : (items.size() + 1) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
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

	private void setThisVariables(ItemPedido itemPedido) throws SQLException {
		this.produto = itemPedido.getProduto();
		this.cdTabelaPreco = itemPedido.getCdTabelaPreco();
		this.cdItemGrade1Default = itemPedido.cdItemGrade1;
		this.semItensInformados = itemPedido.vlTotalItemPedido == 0;
		this.pedido = itemPedido.pedido;
	}

	private void configureLabels() throws SQLException {
		String dsProduto = LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? produto.getDsAgrupadorGrade() : this.produto.toString();
		lbDsProdutoContainer = new LabelValue(dsProduto);
		new BaseToolTip(lbDsProdutoContainer, dsProduto);
		lbItemGrade1 = new LabelName(" ");
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
	}

	private Vector getProdutoGradeList(String cdItemGrade1) throws SQLException {
		return LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? ProdutoGradeService.getInstance().findAllProdutoGradeAgrupadorGrade(cdTabelaPreco, produto.getDsAgrupadorGrade()) : ProdutoGradeService.getInstance().findProdutoGradeList(produto.cdProduto, cdTabelaPreco, cdItemGrade1, null);
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
			int espacoComSemBotao = filtroPorCodigoBarras ? UiUtil.getButtonPreferredHeight() + 60 : 0;
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
		montaGridGradePreco();
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


	private void montaGridGradePreco() throws SQLException {
		resetGrades();
		if (ValueUtil.isEmpty(produtoGradeList)) {
			return;
		}
		callLoadListGridMethods();
	}


	private void resetGrades() {
		itensGrade1Grade = new Vector(0);
		itensGrade3List = new Vector(0);
	}

	private void callLoadListGridMethods() throws SQLException {
		if (itensGrade3Grade.size() > 0) {
			carregaListGradePreco3();
			checkListOrGrid(3);
		} else if (itensGrade2Grade.size() > 0) {
			checkListOrGrid(2);
		} else if (itensGrade1Grade.size() > 0) {
			checkListOrGrid(1);
		}
	}

	private void carregaListGradePreco3() throws SQLException {
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
		if(nuGrade == 2) {
			if (gradeGrid != null) return;
			controlGridGrade2();
		} else if(nuGrade == 3) {
			if (gradeList == null) {
				controlListGrade3();
			}
			if (gradeGrid == null || LavenderePdaConfig.usaGradeProduto5()) {
				controlGridGrade3();
			}
		}
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
	
	@Override
	protected int getLeft() {
		return LEFT + WIDTH_GAP_BIG;
	}

	@Override
	public void initUI() {
	   try {
			super.initUI();
			UiUtil.add(this, lbDsProdutoContainer, getLeft(), getTop(), FILL, LabelContainer.getStaticHeight());
			montaFiltroGrade();
		   if (LavenderePdaConfig.getConfigGradeProduto() != 2) {
				UiUtil.add(this, lbItemGrade1, getLeft(), AFTER);
				UiUtil.add(this, cbItemGrade1, SAME, AFTER);
			}
			if (LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade()) {
				int height = UiUtil.getControlPreferredHeight();
				addButtonPopup(btResetarValores);
				UiUtil.add(this, ckInserido, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(this, cbItemGrade2, AFTER, SAME, SCREENSIZE - 4);
				UiUtil.add(this, cbItemGrade3, AFTER + WIDTH_GAP * 2, SAME, SCREENSIZE - 4);
				UiUtil.add(this, new LabelName(Messages.VALOR), getLeft(), AFTER);
				UiUtil.add(this, edVlItem, SAME, AFTER, SCREENSIZE - 3);
				UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_ACRESCIMO_DESCONTO), AFTER + WIDTH_GAP * 2, BEFORE);
				UiUtil.add(this, chooserPctDesc, SAME, AFTER, SCREENSIZE - 3);
				UiUtil.add(this, btReplicar, AFTER + WIDTH_GAP * 2, SAME, SCREENSIZE - 7);
				if (ckInserido.isChecked()) {
					chooserPctDesc.enableAll();
				} else {
					chooserPctDesc.disableAll();
				}
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

		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	private void paintGrid(boolean fillScreen) {
		getFirstContainer().remove(gradeGrid);
		int posYGrid = (btTabOptionsVisible) ? btTabOptions.getY() + btTabOptions.getHeight() + HEIGHT_GAP :  HEIGHT_GAP_BIG*2 + cbItemGrade1.getHeight() + lbItemGrade1.getHeight();
		posYGrid = LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade() ? btReplicar.getY2() + HEIGHT_GAP : posYGrid;
		UiUtil.add(this, getFirstContainer(), LEFT, posYGrid, FILL, FILL);
		UiUtil.add(getFirstContainer(), gradeGrid, LEFT, TOP + HEIGHT_GAP, FILL, fillScreen ? FILL : getGridHeight());
	}

	private void paintList() {
		getSecondContainer().remove(gradeList);
		int posYGrid = (btTabOptionsVisible) ? btTabOptions.getY() + btTabOptions.getHeight() + HEIGHT_GAP : HEIGHT_GAP;
		UiUtil.add(this, getSecondContainer(), LEFT, posYGrid, FILL, FILL);
		UiUtil.add(getSecondContainer(), gradeList, LEFT, TOP + HEIGHT_GAP , FILL, FILL);
	}

	@Override
	public void reposition() {
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


	protected void onUnpop() {
		super.onUnpop();
		if (gradeGrid != null) {
			gradeGrid.setItems(null);
		}
		if (gradeList != null) {
			gradeList.setItems(null);
		}
	}

	public void onEvent(Event event) {
	   try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btLeitorCamera){
						realizaLeituraCamera();
					} else if (event.target == ckInserido) {
						ckInseridoClick();
					} else if (event.target == btReplicar) {
						btReplicarClick();
		    		} else if (event.target == btResetarValores) {
		    			btResetarValoresClick();
					} else {
						controlTabEvent(event);
						controlComboGrade1Event(event);
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
				case GridEvent.SELECTED_EVENT: {
					if (ckInserido.isChecked()) {
						onGridSelect(event);
					}
		    		break;
		    	}
				case PenEvent.PEN_UP: {
					eventoClickNaGrade(event);
					break;
				}
				case ValueChangeEvent.VALUE_CHANGE: {
					if (event.target == edVlItem || event.target == chooserPctDesc.edF) {
						EditNumberFrac ed = (EditNumberFrac) event.target;
						ed.setValue(ed.getValueDouble());
						if (event.target == edVlItem) chooserPctDesc.setValue(0d);
						else edVlItem.setValue(0d);
					}
					break;
				}
			}
    	} catch (Throwable ex) {
    		ExceptionUtil.handle(ex);
		}
	}

	private void btResetarValoresClick() throws SQLException {
		carregaGridGrade3(true);
		gradeGrid.repaintNow();
	}

	private void onGridSelect(Event event) {
		GridEvent ge = (GridEvent) event;
		if ((ge.col == 0 || ge.row == -2) && ge.row != -1) {
			btReplicarClick(ge.row, ge.col);
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

	private void ckInseridoClick() throws SQLException {
		boolean enabled = ckInserido.isChecked();
		cbItemGrade2.setEnabled(enabled);
		cbItemGrade3.setEnabled(enabled);
		edVlItem.setEnabled(enabled);
		btReplicar.setEnabled(enabled);
		btResetarValores.setEnabled(enabled);
		if (enabled) {
			chooserPctDesc.enableAll();
		} else {
			chooserPctDesc.disableAll();
		}
		reloadAtualTabContainer();
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
		if (posicaoSelecionada[0] >= 0 && posicaoSelecionada[1] >= 0 ) {
			String dsGrade1 = gradeList.getCellText(gradeList.getSelectedXYIndex()[0], 0);
			String dsGrade2 = gradeList.getCellText(gradeList.getSelectedXYIndex()[0], 1);
			precoList = new String[] {dsGrade1, dsGrade2};
			precoGrid = null;
			fecharWindow();
		}
	}

	private void controlComboGrade1Event(Event event) throws SQLException {
		if (event.target == cbItemGrade1) {
			reloadAtualTabContainer();
		}
	}

	private void reloadAtualTabContainer() throws SQLException {
		if (gradeGrid != null) {
			remove(getContainerAtualTab());
			montaGridGradePreco();
			if(btTabOptions.getSelectedIndex() == 0) {
				paintGrid(false);
			}
			if(btTabOptions.getSelectedIndex() == 1) {
				paintList();
			}
//			carregaGrid();
		}
	}

	private void controlTabEvent(Event event) {
		if (event.target == btTabOptions) {
			if(btTabOptions.getSelectedIndex() == 0) {
				remove(getSecondContainer());
				controlaTabGrid();
				paintGrid(false);
			}
			if(btTabOptions.getSelectedIndex() == 1) {
				remove(getFirstContainer());
				controlaTabList();
				paintList();
			}
		}
	}

	private void btReplicarClick() {
		Vector selectedGrades2 = UiUtil.getSelectedItemsOrEverything(cbItemGrade2);
		Vector selectedGrades3 = UiUtil.getSelectedItemsOrEverything(cbItemGrade3);
		if ((edVlItem.getValueDouble() == 0d && chooserPctDesc.getValue() == 0d) || ValueUtil.isEmpty(selectedGrades2) || ValueUtil.isEmpty(selectedGrades3)) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_REPLICAR_GRADE_VALOR);
			return;
		}
		boolean isReplicandoValor = edVlItem.getValueDouble() > 0d;
		if (!isReplicandoValor && chooserPctDesc.getValue() >= 100d) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_REPLICAR_DESCONTO);
			return;
		}
		double value = isReplicandoValor ? edVlItem.getValueDouble() : chooserPctDesc.getValue();
		int size1 = selectedGrades2.size();
		int size2 = selectedGrades3.size();
		for (int i = 0; i < size1; i++) {
			ItemGrade itemGrade2 = (ItemGrade) selectedGrades2.items[i];
			int row = itensGrade2Grade.indexOf(itemGrade2);
			for (int j = 0; j < size2; j++) {
				ItemGrade itemGrade3 = (ItemGrade) selectedGrades3.items[j];
				int col = itensGrade3Grade.indexOf(itemGrade3);
				if (gradeGrid.gridController.isRowColEnabled(row, col + 1)) {
					ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedidoList);

					if (isReplicandoValor) {
						if (edVlItem.getValueDouble() == 0d) {
							value = itemPedido.vlBaseItemPedido;
						}
					} else {
						itemPedido.flTipoEdicao = getItemPedidoGradeFlTipoEdicao(value);
					}

					setVlPrecoReplicado(isReplicandoValor, row, col + 1, value, itemPedido);
				}
			}
		}
		gradeGrid.repaintNow();
	}

	private int getItemPedidoGradeFlTipoEdicao(double vlPctDesconto) {
		return vlPctDesconto < 0 ? ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT : ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
	}

	private void setVlPrecoReplicado(boolean isReplicandoValor, int row, int col, double value, ItemPedido itemPedido) {
		if (isReplicandoValor) {
			gradeGrid.setCellText(row, col, StringUtil.getStringValueToInterface(value));
		} else {
			itemPedido.vlPctDescontoReplicacaoGrade = value;
			if (value < 0) {
				itemPedido.vlPctDescPedido = 0d;
			} else if (value > 0) {
				itemPedido.vlPctAcrescimo = 0d;
			}
			value = itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDescontoReplicacaoGrade + itemPedido.vlPctDescPedido) / 100);
			gradeGrid.setCellText(row, col, StringUtil.getStringValueToInterface(value));
		}
	}

	private void btReplicarClick(int row, int col) {
		if (row == -2 && (col > itensGrade3Grade.size() || col == 0)) return;
		WmwInputBox inputBox = new WmwInputBox(Messages.BOTAO_REPLICAR_GRADE, ValueUtil.VALOR_NI, 0d);
		inputBox.getEdit().setText("");
		inputBox.popup();
		if (inputBox.getPressedButtonIndex() == 0) return;
		String qtdReplicar = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(inputBox.getValue()), ValueUtil.doublePrecisionInterface);
		if (col == 0) {
			replicaGradePorColuna(row, qtdReplicar);
		} else if (row == -2) {
			replicaGradePorLinha(col, qtdReplicar);
		}
		gradeGrid.repaintNow();
	}

	private void replicaGradePorLinha(int col, String qtdReplicar) {
		int size = itensGrade2Grade.size();
		for (int i = 0; i < size; i++) {
			if (gradeGrid.gridController.isRowColEnabled(i, col)) {
				gradeGrid.setCellText(i, col, qtdReplicar);
			}
		}
	}

	private void replicaGradePorColuna(int row, String qtdReplicar) {
		int size = itensGrade3Grade.size();
		for (int i = 1; i <= size; i++) {
			if (gradeGrid.gridController.isRowColEnabled(row, i)) {
				gradeGrid.setCellText(row, i, qtdReplicar);
			}
		}
	}

	private void montaGridGrade2() throws SQLException {
		int itensGrade2GradeSize = itensGrade2Grade.size();
		String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemGrade)itensGrade2Grade.items[0]).cdTipoItemGrade);
		GridColDefinition[] gridColDefiniton = { new GridColDefinition(dsTipoItemGrade, -75, LEFT), new GridColDefinition(Messages.GRADE_LABEL_PRECO, -20, LEFT)};
		configurateGradeGrid(gridColDefiniton);
		gradeGrid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
		for (int i = 0; i < itensGrade2GradeSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade2Grade.items[i];
			gradeGrid.add(new String[]{StringUtil.getStringValue(itemGrade.dsItemGrade), ""});
		}
		gradeGrid.drawHighlight = false;
	}

	private void montaGridGrade3() {
		int widthCelulas = fm.stringWidth("00000,00");
		int itensGrade3GradeSize = itensGrade3Grade.size();
		GridColDefinition[] gridColDefinition = new GridColDefinition[itensGrade3GradeSize + 2];
		gridColDefinition[0] = new GridColDefinition(" ",-30, LEFT);
		gridColDefinition[gridColDefinition.length - 1] = new GridColDefinition("", widthCelulas, RIGHT);
		for (int i = 0; i < itensGrade3GradeSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade3Grade.items[i];
			gridColDefinition[i + 1] = new GridColDefinition(StringUtil.getStringValue(itemGrade.dsItemGrade), widthCelulas, RIGHT);
		}
		configurateGradeGrid(gridColDefinition);
		if (LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade()) {
			configurateGridGradeProduto5(itensGrade3GradeSize);
				}
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

	private void configurateGridGradeProduto5(int itensGrade3GradeSize) {
		for (int i = 0; i < itensGrade3GradeSize; i++) {
			EditNumberFrac edControlGrid = gradeGrid.setColumnEditableDoubleNoCalc(i + 1, ckInserido.isChecked(), 9, ValueUtil.doublePrecisionInterface, 0);
			if (ckInserido.isChecked()) {
				edControlGrid.autoSelect = true;
			}
		}
	}


	private void carregaGridGrade2() throws SQLException {
		int itensGrade2GradeSize = itensGrade2Grade.size();
		if (itensGrade2GradeSize == 0) return;
		int cor = buscaCor();
		for (int i = 0; i < itensGrade2GradeSize; i++) {
			String[] item = gradeGrid.getItem(i);
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[i];
			ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO, itemPedidoList);
			item[1] = StringUtil.getStringValueToInterface(itemPedido.vlItemPedido,  ValueUtil.doublePrecisionInterface);
			if (naoPintaCelula(i % 2 == 0 ? cor :  Color.darker(cor, 20), itemPedido)) continue;
			gradeGrid.gridController.setRowBackColor(i % 2 == 0 ? cor :  Color.darker(cor, 20), i);
		}
	}

	private void carregaGridGrade3(boolean resetaValores) throws SQLException {
		int gradeGridSize = gradeGrid.size();
		if (gradeGridSize == 0) return;
		int itemGrade3GradeSize = itensGrade3Grade.size();
		int cor = buscaCor();
		boolean usaGradeProduto5 = LavenderePdaConfig.usaGradeProduto5() && produto.isProdutoAgrupadorGrade();
		Map<String, ProdutoGrade> hashProdutoGrade = usaGradeProduto5 ? ProdutoGradeService.getInstance().montaHashProdutoGradeAgrupador(cdTabelaPreco, cbItemGrade1.getValue(), produto.getDsAgrupadorGrade()) : null;
		if (LavenderePdaConfig.usaGradeProduto5()) {
			gradeGrid.gridController.rowColBackColorList = new Vector(50);
		}
		ItemPedido itemPedido = null;
		Map<String, Image> hashMarcadores = new HashMap<>();
		if (LavenderePdaConfig.apresentaMarcadorGrade) ProdutoService.getInstance().loadImagesMarcadores(hashMarcadores, UiUtil.getControlPreferredHeight() / 3);
		Vector listMarcadores = new Vector();
        for (int i = 0; i < gradeGridSize; i++) {
        	String[] item = gradeGrid.getItem(i);
        	ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[i];
        	Image[] cellImages = LavenderePdaConfig.apresentaMarcadorGrade ? new Image[gradeGrid.captions.length] : null;
        	for (int j = 0; j < itemGrade3GradeSize; j++) {
        		ItemGrade itemGrade3 = (ItemGrade)itensGrade3Grade.items[j];
        		itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedidoList);
        		boolean isProdutoInPedido = isProdutoInPedido(itemPedido.cdProduto);
        		if (ckInserido.isChecked()) {
        			item[j + 1] =  isProdutoInPedido ? StringUtil.getStringValueToInterface(usaGradeProduto5 && !resetaValores ? itemPedido.vlItemPedido : itemPedido.vlBaseItemPedido,  ValueUtil.doublePrecisionInterface) : ValueUtil.VALOR_NI;
        		} else {
        			item[j + 1] = ValueUtil.isNotEmpty(itemPedido.cdProduto) ? StringUtil.getStringValueToInterface(usaGradeProduto5 && !resetaValores ? itemPedido.vlItemPedido : itemPedido.vlBaseItemPedido,  ValueUtil.doublePrecisionInterface) : ValueUtil.VALOR_NI;
        		}
        		if (LavenderePdaConfig.usaGradeProduto5()) {
        			disableCellProdutoSemGrade(hashProdutoGrade.keySet(), cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, i, j, !isProdutoInPedido);
        			ProdutoGrade produto = hashProdutoGrade.get(ProdutoGrade.getGradeKey(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade));
        			if (LavenderePdaConfig.apresentaMarcadorGrade && itemPedido != null && produto != null) {
        				setImageMarcador(cellImages, hashMarcadores, produto, j + 1);
        		}
        		}
        		if (naoPintaCelula(i % 2 == 0 ? cor :  Color.darker(cor, 20), itemPedido)) continue;
        		gradeGrid.gridController.setCelBackColor(i % 2 == 0 ? cor :  Color.darker(cor, 20), i , j + 1);
        	}
        	if (LavenderePdaConfig.apresentaMarcadorGrade) {
        		listMarcadores.addElement(cellImages);
        }
	}
        if (LavenderePdaConfig.apresentaMarcadorGrade) {
        	gradeGrid.setListCellImages(listMarcadores);
        	gradeGrid.keepImageAndText = true;
		}
	}

	private void setImageMarcador(Image[] cellImages, Map<String, Image> marcadores, ProdutoGrade produto, int j) {
		if (ValueUtil.isNotEmpty(produto.cdMarcador)) {
			cellImages[j] = marcadores.get(produto.cdMarcador);
		}
	}

	private String findAgrupadorGradeCdMarcador(Produto produto) {
		for (int i = 0; i < produto.marcadores.size(); i++) {
			Object object = produto.marcadores.items[i];
			if (object != null) {
				Marcador marcador = (Marcador)object;
				if (ValueUtil.valueEqualsIfNotNull(marcador.flAgrupadorGrade, ValueUtil.VALOR_SIM)) {
					return marcador.cdMarcador;
				}
			}
		}
		return null;
	}

	private void carregaGridGrade3() throws SQLException {
		carregaGridGrade3(false);
	}

	private void setPrecosAgrupadorGrade() throws SQLException {
		int sizeGrade2 = itensGrade2Grade.size();
		boolean hasProdutoGradeComVlPermitido = false;
		UiUtil.showProcessingMessage();
		try {
			for (int i = 0; i < sizeGrade2; i++) {
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[i];
				String[] rowGrid = gradeGrid.getItem(i);
				int sizeGrade3 = itensGrade3Grade.size();
				for (int j = 0; j < sizeGrade3; j++) {
					double vlItemGrade = ValueUtil.getDoubleValue(rowGrid[j + 1].replace(".", ""));
					if (vlItemGrade == 0) continue;
					ItemGrade itemGrade3 = (ItemGrade)itensGrade3Grade.items[j];
					ItemPedido itemPedidoPorGradePreco = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedidoList);
					if (itemPedidoPorGradePreco == null) continue;
					itemPedidoPorGradePreco.vlItemPedido = vlItemGrade;
					if (itemPedidoPorGradePreco.flEdicaoItemPedidoGrade == ItemPedido.ITEMPEDIDO_SEM_EDICAO) itemPedidoPorGradePreco.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_ATUALIZANDO;
					if (itemPedidoPorGradePreco.vlPctDescontoReplicacaoGrade < 0) {
						itemPedidoPorGradePreco.vlPctDesconto = 0;
						itemPedidoPorGradePreco.vlPctDescPedido = 0d;
						itemPedidoPorGradePreco.vlPctAcrescimo = -itemPedidoPorGradePreco.vlPctDescontoReplicacaoGrade;
					} else if (itemPedidoPorGradePreco.vlPctDescontoReplicacaoGrade > 0) {
						itemPedidoPorGradePreco.vlPctDesconto = itemPedidoPorGradePreco.vlPctDescontoReplicacaoGrade;
						itemPedidoPorGradePreco.vlPctAcrescimo = 0;
					} else if (itemPedidoPorGradePreco.vlPctDescPedido == 0d) {
						ItemPedidoService.getInstance().calculaItemPedidoGradeDescontoAcrescimo(itemPedidoPorGradePreco);
						itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
					} else if (itemPedidoPorGradePreco.vlPctDescPedido > 0d) {
						ItemPedidoService.getInstance().calculaItemPedidoGradeDescontoAcrescimoComDescCapa(itemPedidoPorGradePreco);
						itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
					}
					itemPedidoPorGradePreco.vlPctDescontoReplicacaoGrade = 0d;
					if (itemPedidoPorGradePreco.pedido == null) {
						itemPedidoPorGradePreco.pedido = itemPedido.pedido;
					}
					try {
						ItemPedidoService.getInstance().validateDescAcresMax(itemPedidoPorGradePreco);
						ItemPedidoService.getInstance().garantirDescontoAcrescimentoNaoNegativo(itemPedidoPorGradePreco);
					} catch (DescAcresMaximoException e) {
						hasProdutoGradeComVlPermitido = true;
						itemPedidoPorGradePreco.vlItemPedido = e.vlPermitido;
						ItemPedidoService.getInstance().calculaItemPedidoGradeDescontoAcrescimo(itemPedidoPorGradePreco);
						rowGrid[j + 1] = StringUtil.getStringValueToInterface(itemPedidoPorGradePreco.vlItemPedido, ValueUtil.doublePrecisionInterface);
					}
				}
			}
		} finally {
			UiUtil.unpopProcessingMessage();
		}
		if (hasProdutoGradeComVlPermitido) UiUtil.showWarnMessage(Messages.MSG_AVISO_GRADE_COM_VALOR_PERMITIDO);
	}

	private void totalizaValoresItemPedido() {
		int size = itemPedido.itemPedidoPorGradePrecoList.size();
		itemPedido.vlTotalItemPedido = 0d;
		UiUtil.showProcessingMessage();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedido.itemPedidoPorGradePrecoList.items[i];
			if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
				if (itemPedido.equals(itemPedidoPorGradePreco)) {
					Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
					itemPedido = itemPedidoPorGradePreco;
					itemPedidoPorGradePreco.itemPedidoPorGradePrecoList = itemPedidoPorGradePrecoList;
					break;
				}
			} else if (itemPedidoPorGradePreco.qtItemFisico > 0d) {
				itemPedido.vlTotalItemPedido += itemPedidoPorGradePreco.vlItemPedido * itemPedidoPorGradePreco.qtItemFisico;
			}
		}
		UiUtil.unpopProcessingMessage();
	}

	private void disableCellProdutoSemGrade(Set<String> hashProdutoGrade, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, int i, int j, boolean produtoForaPedido) {
		boolean containsProdutoGrade = hashProdutoGrade.contains(ProdutoGrade.getGradeKey(cdItemGrade1, cdItemGrade2, cdItemGrade3));
		if (produtoForaPedido || !containsProdutoGrade) {
			gradeGrid.gridController.setRowColDisable(i, j+1, true);
			if (!containsProdutoGrade || ckInserido.isChecked()) gradeGrid.gridController.setCelBackColor(ColorUtil.componentsBackColorDark, i, j+1);
		}
	}

	private boolean isProdutoInPedido(String cdProduto) {
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.valueEquals(cdProduto, itemPedido.cdProduto) && itemPedido.qtItemFisico > 0d) return true;
		}
		return false;
	}

	private int buscaCor() throws SQLException {
		if (permiteEventoClickNaGrade) {
			TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
			CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(tema.cdEsquemaCor, CorSistemaLavendere.COR_FUNDO_CELULA_GRADE_COM_QUANTIDADE);
			if (corSistema != null) {
				return Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB);
			}
		}
		return 0;
	}

	private boolean naoPintaCelula(int cor, ItemPedido itemPedido) {
		return semItensInformados || itemPedido.getQtItemFisico() == 0 || cor == 0;
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
		gridColDefinition[2] = new GridColDefinition(Messages.GRADE_LABEL_PRECO, widthCelulas, RIGHT);
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

	private void carregaListGrade3() throws SQLException {
		int gradeListSize = gradeList.size();
		if (gradeListSize == 0) return;
		int cor = buscaCor();
        for (int i = 0; i < gradeListSize; i++) {
        	ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[i];
        	ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade3.cdItemGrade2, itemGrade3.cdItemGrade3, itemPedidoList);
    		gradeList.getItem(i)[2] = StringUtil.getStringValueToInterface(itemPedido.vlItemPedido,  ValueUtil.doublePrecisionInterface);
    		if (naoPintaCelula(i % 2 == 0 ? cor :  Color.darker(cor, 20), itemPedido)) continue;
    		gradeList.gridController.setRowBackColor(i % 2 == 0 ? cor :  Color.darker(cor, 20), i);
        }
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

	private void configurateGradeGrid(GridColDefinition[] gridColDefinition) {
		gradeGrid = UiUtil.createGridEdit(gridColDefinition, false);
		gradeGrid.setGridControllable();
		gradeGrid.disableSort = true;
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

	@Override
	protected void btFecharClick() throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto5() && ckInserido.isChecked()) {
			setPrecosAgrupadorGrade();
			totalizaValoresItemPedido();
		}
		super.btFecharClick();
	}

	public void setHashProdutoGrade(Map<String, Produto> hashProdutoGrade) {
		this.hashProdutoGrade = hashProdutoGrade;
	}

	private void selectCkInserido() {
		if (LavenderePdaConfig.usaGradeProduto5()) {
			int gradeGridSize = itensGrade2Grade.size();
			if (gradeGridSize == 0) return;
			int itemGrade3GradeSize = itensGrade3Grade.size();
			for (int i = 0; i < gradeGridSize; i++) {
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2Grade.items[i];
				for (int j = 0; j < itemGrade3GradeSize; j++) {
					ItemGrade itemGrade3 = (ItemGrade)itensGrade3Grade.items[j];
					ItemPedido itemPedido = ItemPedidoService.getInstance().buscaItemPedidoPorGrade(cbItemGrade1.getValue(), itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedidoList);
					if (isProdutoInPedido(itemPedido.cdProduto)) {
						ckInserido.setChecked(true);
						cbItemGrade2.setEnabled(true);
						cbItemGrade3.setEnabled(true);
						edVlItem.setEnabled(true);
						chooserPctDesc.enableAll();
						btReplicar.setEnabled(true);
						btResetarValores.setEnabled(true);
						return;
					}
				}
			}
		}
	}

}
