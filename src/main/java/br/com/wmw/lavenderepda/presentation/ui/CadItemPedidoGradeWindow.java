package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.GridEditEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.BaseTooltipLabelValue;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridActionShowControl;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.KeyEditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocionalGrade;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.enums.ItemGradeCellForeColor;
import br.com.wmw.lavenderepda.business.service.ConversaoUnidadeService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalGradeService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.business.validation.EstoqueException;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeMultiComboBox;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Settings;
import totalcross.sys.SpecialKeys;
import totalcross.sys.Vm;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.KeyListener;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class CadItemPedidoGradeWindow extends WmwWindow {
	
	private static final String FIRST_COLUMN = "0";
	private static final String COLUMN_ESTOQUE = "1";
	private static final int GRADE3_OFFSET = 1;

	private BaseGridEdit grid;
	private ItemPedido itemPedido;
	private LabelName lbItemGrade1;
	private EditFiltro edFiltro;
	protected ButtonAction btLeitorCamera;
	private String dsFiltro;
	private BaseTooltipLabelValue lbVlItemGrade1;
	private LabelName lbItemGrade2;
	private BaseTooltipLabelValue lbVlItemGrade2;
	private LabelValue lbDsProduto;
	private EditNumberFrac edControlGrid;
	private ButtonPopup btOk;
	private ButtonPopup btLimpar;
	private ButtonPopup btTotalizadores;
	private ButtonPopup btPrecos;
	private ButtonPopup btEstoque;
	private PushButtonGroupBase numericPad;
	private BaseEdit edBaseNumPad;
	private ItemGradeMultiComboBox cbItemGrade2;
	private ItemGradeMultiComboBox cbItemGrade3;
	private EditNumberInt edQtItem;
	private BaseButton btReplicar;
	private boolean permiteEdicao = true;
	private Vector itensGradeComProblemaList;
	private StringBuffer itensComProblemas;
	public boolean windowClosed;
	private boolean enterConfirmaInsercao;
	private int numPadHeight = 0;
	private String dsGradeFechada;

	private Vector produtoGradeList;
	private Vector itensGrade1List;
	private Vector itensGrade1ListAuxiliar;
	private Vector itensGrade2List;
	private Vector itensGrade3List;
	private BaseScrollContainer scrollContainer;
	private LabelValue lbMultip, lbTitleMult;
	private Vector itemPedidoGrade3List = new Vector();
	private Vector itemPedidoGrade2List = new Vector();
	private Vector itemPedidoGrade1List = new Vector();
	private Vector descPromocionalGradeList = new Vector();
	private Vector itemPedidoPrecoGradeList = new Vector();
	private Vector itemPedidoPrecoGradeOriginalList = new Vector();
	private HashMap<String, ProdutoGrade> produtoGradeHash = new HashMap<String, ProdutoGrade>();
	private boolean ascending;
	private int lastSortCol;
	private Map<String, Produto> hashProdutoGrade;
	
	private final AbstractBaseCadItemPedidoForm cadItemPedidoForm;
	
	public CadItemPedidoGradeWindow(ItemPedido itemPedido, boolean permiteEdicao, AbstractBaseCadItemPedidoForm cadItemPedidoForm) throws SQLException {
		super(Messages.ITEMPEDIDO_LABEL_QTITENS);
		this.itemPedido = itemPedido;
		armazenaItemPedidoPrecoGradeOriginal();
		this.permiteEdicao = permiteEdicao;
		this.cadItemPedidoForm = cadItemPedidoForm;
		
		lbItemGrade1 = new LabelName(" ");
		lbVlItemGrade1 = new BaseTooltipLabelValue(" ");
		lbItemGrade2 = new LabelName(" ");
		lbVlItemGrade2 = new BaseTooltipLabelValue(" ");
		scrollable = false;
		edControlGrid = new EditNumberFrac("00000", 9, BaseEdit.EDIT_TYPE_ONLY_NUMBER_DEC);
		btOk = new ButtonPopup("  " + FrameworkMessages.BOTAO_OK + "  ");
		btLimpar = new ButtonPopup("  " + FrameworkMessages.BOTAO_LIMPAR + "  ");
		btTotalizadores = new ButtonPopup("  " + Messages.GRADE_LABEL_BOTAO_TOTALIZADORES + "  ");
		btPrecos = new ButtonPopup("  " + Messages.BOTAO_PRECOS + "  ");
		btEstoque = new ButtonPopup("  " + Messages.ESTOQUE_NOME_ENTIDADE + "  ");
		edFiltro = new EditFiltro("999999999", 50);
		if ( LavenderePdaConfig.isUsaFiltroGradeProdutoPorCamera() && LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
			btLeitorCamera = new ButtonAction("  "+Messages.CAMERA+"  ","images/barcode.png");
		}
		if (permiteEdicao) {
			btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
		}
		if (LavenderePdaConfig.usaGradeProduto5()) {
			cbItemGrade2 = new ItemGradeMultiComboBox();
			cbItemGrade3 = new ItemGradeMultiComboBox();
			edQtItem = new EditNumberInt("", 9);
			btReplicar = new BaseButton(Messages.BOTAO_REPLICAR_GRADE);
		}
		lbMultip = new LabelValue();
		lbTitleMult = new LabelValue();
        loadDadosGradeProduto();

        if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido && itemPedido.pedido.isPedidoAberto()) {
			numericPad = new PushButtonGroupBase(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", StringUtil.getStringValue(Settings.decimalSeparator), "<" }, false, -1, -1, UiUtil.defaultGap * 2, 2, true, PushButtonGroup.NORMAL);
			numericPad.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
			numericPad.borderLarge = true;
			numericPad.borderColor = ColorUtil.componentsBorderColor;
			numericPad.setFocusLess(true);
        }
        if (grid != null) grid.disableSort = LavenderePdaConfig.usaGradeProduto5();
        setDefaultRect();
        enterConfirmaInsercao = LavenderePdaConfig.isUsaTeclaEnterComoConfirmacaoItemPedido();
        lastSortCol = -1;
	}

	private void armazenaItemPedidoPrecoGradeOriginal() {
		int size = this.itemPedido.itemPedidoPorGradePrecoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) this.itemPedido.itemPedidoPorGradePrecoList.items[i];
			ItemPedido itemPedidoPrecoGradeOriginal = (ItemPedido) itemPedidoPorGradePreco.clone();
			itemPedidoPrecoGradeOriginal.pedido = itemPedidoPorGradePreco.pedido;
			itemPedidoPrecoGradeOriginalList.addElement(itemPedidoPrecoGradeOriginal);
		}
	}
	
	public CadItemPedidoGradeWindow(ItemPedido itemPedido, AbstractBaseCadItemPedidoForm cadItemPedidoForm) throws SQLException {
		super(Messages.ITEMPEDIDO_LABEL_QTITENS);
		this.itemPedido = itemPedido;
		this.cadItemPedidoForm = cadItemPedidoForm;
		
		produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(itemPedido.cdProduto, itemPedido.getCdTabelaPreco());
		montaGridGradeProduto();
	}

	private void loadDadosGradeProduto() throws SQLException {
		Pedido pedido = itemPedido.pedido;
		if (pedido == null) {
			pedido = PedidoService.getInstance().findPedidoByItemPedido(itemPedido);
			itemPedido.pedido = pedido;
		}
		if (pedido.isFlOrigemPedidoErp()) {
			produtoGradeList = ProdutoGradeService.getInstance().findAllProdutoGradeErpByItemPedido(itemPedido);
		} else if (pedido.isPedidoFechado() || pedido.isPedidoTransmitido()) {
			produtoGradeList = ProdutoGradeService.getInstance().findAllProdutoGradeInseridosByItemPedido(itemPedido);
		} else {	
			produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(itemPedido);						
		}
		
		if (ValueUtil.isNotEmpty(produtoGradeList) && LavenderePdaConfig.getConfigGradeProduto() != 2) {
			int size = produtoGradeList.size();
			if (size > 0) {
				ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[0];
				lbItemGrade1.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtGrade.cdTipoItemGrade1));
				lbVlItemGrade1.setValue(ItemGradeService.getInstance().getDsItemGrade(produtGrade.cdTipoItemGrade1, itemPedido.cdItemGrade1));
				if (LavenderePdaConfig.isGradeProdutoModoLista()) {
					lbItemGrade2.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtGrade.cdTipoItemGrade2));
					lbVlItemGrade2.setValue(ItemGradeService.getInstance().getDsItemGrade(produtGrade.cdTipoItemGrade2, itemPedido.cdItemGrade2));
				}
			}
		}
		montaGridGradeProduto();
	}

	private void montaGridGradeProduto() throws SQLException {
		if (itemPedido.cdItemGrade1 != null && ValueUtil.isNotEmpty(produtoGradeList)) {
			carregaItensGradeList();
			iniciaListaGradeAuxiliar();
			if (LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto) {
				SortUtil.qsortInt(itensGrade1List.items, 0, itensGrade1List.size() - 1, true);
			} else {
				itensGrade1List.qsort();
			}
			
			SortUtil.qsortInt(itensGrade2List.items, 0, itensGrade2List.size() - 1, true);
			SortUtil.qsortInt(itensGrade3List.items, 0, itensGrade3List.size() - 1, true);
			
			if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional) {
				montaGradeFechadaVectors();
			}
			if (LavenderePdaConfig.isGradeProdutoModoLista()) {
				itensGrade1List.addElementsNotNull(itensGrade3List.items);
				itensGrade3List = new Vector();
			}
			if (LavenderePdaConfig.usaGradeProduto5()) {
				loadItemGradeComboBox();
			}
			if (itensGrade3List.size() > 0) {
				montaGridGrade3();
			} else if (itensGrade2List.size() > 0) {
				montaGridGrade2();
			} else if (itensGrade1List.size() > 0) {
				montaGridGrade1();
			}
		}
	}

	private void criaItensGradeList() {
		itensGrade1List = new Vector(0);
		itensGrade2List = new Vector(0);
		itensGrade3List = new Vector(0);
	}
	
	private void loadItemGradeComboBox() {
		cbItemGrade2.add(itensGrade2List);
		cbItemGrade3.add(itensGrade3List);
	}

	private void carregaItensGradeList() throws SQLException {
		if (itemPedido.cdItemGrade1 != null && ValueUtil.isNotEmpty(produtoGradeList)) {
			criaItensGradeList();
			boolean stopList1 = false;
			int size = produtoGradeList.size();
			for (int i = 0; i < size; i++) {
				ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[i];
				if (LavenderePdaConfig.usaGradeProduto2() && !stopList1) {
					if (!ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtGrade.cdItemGrade1)) {
						ItemGrade itemGrade1;
						itemGrade1 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade1, produtGrade.cdItemGrade1);
						if ((itemGrade1 != null) && (itensGrade1List.indexOf(itemGrade1) == -1)) {
							if(ValueUtil.isEmpty(dsFiltro) || !LavenderePdaConfig.isUsaFiltroGradeProduto()) {
								itensGrade1List.addElement(itemGrade1);
							} else {
								if (verificaFiltroGrade(itemGrade1,produtGrade)) {
									itensGrade1List.addElement(itemGrade1);
								} else if (ValueUtil.isEmpty(dsFiltro) && !ValueUtil.isEmpty(itensGrade1ListAuxiliar)) {
									itensGrade1List = new Vector(itensGrade1ListAuxiliar.items) ;
									itensGrade1List.setSize(itensGrade1ListAuxiliar.size());
									stopList1 = true;
								} 
							}
						} 
					}
				}
				if (itemPedido.cdItemGrade1.equals(produtGrade.cdItemGrade1) || LavenderePdaConfig.usaGradeProduto2()) {
					if (!ValueUtil.isEmpty(produtGrade.cdItemGrade2) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtGrade.cdItemGrade2) && !LavenderePdaConfig.isGradeProdutoModoLista()) {
						ItemGrade itemGrade2 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade2, produtGrade.cdItemGrade2);
						if ((itemGrade2 != null) && (itensGrade2List.indexOf(itemGrade2) == -1)) {
							itemGrade2.nuOrdemLinha = produtGrade.nuOrdemLinha;
							itensGrade2List.addElement(itemGrade2);
						}
					}
					if (!ValueUtil.isEmpty(produtGrade.cdItemGrade3) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtGrade.cdItemGrade3)) {
						if (isGradeTresValida(produtGrade) || LavenderePdaConfig.getConfigGradeProduto() != 3) {
							ItemGrade itemGrade3 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade3, produtGrade.cdItemGrade3);
							if ((itemGrade3 != null) && (itensGrade3List.indexOf(itemGrade3) == -1)) {
								itemGrade3.nuOrdemColuna = produtGrade.nuOrdemColuna;
								itensGrade3List.addElement(itemGrade3);
							}
						}
					}
				}
			}
		}
	}

	private boolean isGradeTresValida(ProdutoGrade produtGrade) {
		return LavenderePdaConfig.isGradeProdutoModoLista()
			&& produtGrade != null 
			&& ValueUtil.valueEquals(produtGrade.cdItemGrade1, itemPedido.cdItemGrade1)
			&& ValueUtil.valueEquals(produtGrade.cdItemGrade2, itemPedido.cdItemGrade2);
	}

	@Override
	public void initUI() {
        try {
			super.initUI();
			if (permiteEdicao) {
				addButtonPopup(btOk);
			}
			
			if (LavenderePdaConfig.exibeAbaTotalizadoresPedidoGrade() && !LavenderePdaConfig.usaGradeProduto5()) {
				addButtonPopup(btTotalizadores);
			}
			if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
				addButtonPopup(btPrecos);
				addButtonPopup(btEstoque);
			}
			if (permiteEdicao) {
				addButtonPopup(btLimpar);
			}		
						
			addButtonPopup(btFechar);
			int remainingSize = 0;
			if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido && itemPedido.pedido.isPedidoAberto()) {
				numPadHeight = UiUtil.getControlPreferredHeight() * 2;
				UiUtil.add(this, numericPad, LEFT, BOTTOM - cFundoFooter.getHeight(), FILL + 4, numPadHeight);
				remainingSize += numPadHeight;
			}
			String dsProduto = LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade() ? itemPedido.getProduto().getDsAgrupadorGrade() : ProdutoService.getInstance().getDescricaoProdutoReferenciaConsideraCodigo(itemPedido.getProduto());
			lbDsProduto = new LabelValue(dsProduto, LEFT);
			new BaseToolTip(lbDsProduto, dsProduto);
			UiUtil.add(this, lbDsProduto, getLeft(), getNextY());
			montaFiltroGrade();
			
			if (LavenderePdaConfig.getConfigGradeProduto() != 2) {
				UiUtil.add(this, lbItemGrade1, lbVlItemGrade1, SAME, AFTER + HEIGHT_GAP);
			}
			remainingSize += LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional ? descPromocionalGradeList.size() * fmH : 0;
			if (LavenderePdaConfig.isGradeProdutoModoLista()) {
				UiUtil.add(this, lbItemGrade2, lbVlItemGrade2, getLeft(), getLeft());
			}
			if (LavenderePdaConfig.usaGradeProduto5()) {
				int width = SCREENSIZE + 24;
				UiUtil.add(this, cbItemGrade2, getLeft(), getNextY(), width);
				UiUtil.add(this, cbItemGrade3, AFTER + WIDTH_GAP, SAME, width);
				UiUtil.add(this, edQtItem, AFTER + WIDTH_GAP, SAME, width);
				UiUtil.add(this, btReplicar, AFTER + WIDTH_GAP, SAME);
			}
			if (grid != null) {
				UiUtil.add(this, grid, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP, FILL, getGridHeight(remainingSize));
			}
			
			carregaGradeFechada();
			montaGradeFechada();
			carregaGrid();
			updateQtInserido();
			if (LavendereConfig.getInstance().usaFocoCampoBuscaAutomaticamente && permiteEdicao) {
				grid.setSelectedIndex(0);
				grid.exibeControleGrid(0, LavenderePdaConfig.isOcultaEstoque() ? 1 : 2);
			}
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	}

	private void carregaGradeFechada() {
		if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional) {
			int size = descPromocionalGradeList.size();
			StringBuffer msg = new StringBuffer();
			Vector list = new Vector(itensGrade2List.size());
			list.addElementsNotNull(itensGrade2List.items);
			for (int i = 0; i < size; i++) {
				DescPromocionalGrade descPromocionalGrade = (DescPromocionalGrade) descPromocionalGradeList.items[i];
				for (int j = 0, size2 = list.size(); j < size2; j++) {
					ItemGrade itemGrade = (ItemGrade)list.items[j];
					if (descPromocionalGrade.cdItemGrade2.equals(itemGrade.cdItemGrade)) {
						if (i > 0) {
							msg.append("; ");
						}
						try {
							msg.append(ItemGradeService.getInstance().getDsItemGrade(itemGrade.cdTipoItemGrade, itemGrade.cdItemGrade)).append(" = ").append(descPromocionalGrade.nuMultiploEspecial);
						} catch (SQLException e) {
							ExceptionUtil.handle(e);
						}
						list.removeElementAt(j);
						size2--;
						j--;
						break;
					}
				}
			}
			dsGradeFechada = msg.toString();
		}
	}

	private void montaGradeFechada() {
		if (ValueUtil.isNotEmpty(descPromocionalGradeList)) {
			scrollContainer = new BaseScrollContainer(false, true);
			lbTitleMult.setText(Messages.GRADE_RESTRICAO_PROD_PROMOCIONAL);
			UiUtil.add(this, scrollContainer, LEFT, AFTER, FILL, FILL - footerH - numPadHeight);
			UiUtil.add(scrollContainer, lbTitleMult, LEFT, TOP);
			UiUtil.add(scrollContainer, lbMultip, LEFT, AFTER, scrollContainer.getWidth(), PREFERRED);
			lbMultip.setMultipleLinesText(dsGradeFechada);
		}
	}

	private int getGridHeight(int remainingSize) {
		int baseGridHeight = remainingSize == 0 ? FILL - cFundoFooter.getHeight() - lbDsProduto.getHeight() : FILL - cFundoFooter.getHeight() - remainingSize - HEIGHT_GAP;
		int gradeHeight = 0;
		if (LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			gradeHeight = (2 + itensGrade2List.size()) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		} else if (LavenderePdaConfig.usaGradeProduto2()) {
			gradeHeight = (2 + itensGrade1List.size()) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		} else if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			gradeHeight = (2 + itensGrade1List.size()) * UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		}
		int baseWindowHeight = this.height - cFundoFooter.getHeight() - remainingSize;
		baseWindowHeight -= LavenderePdaConfig.usaGradeProduto5() ? btReplicar.getY2() : lbDsProduto.getY2();
		boolean gradeHeightTooBig = gradeHeight * 1.2 > baseWindowHeight;
		return gradeHeight > baseWindowHeight || gradeHeightTooBig ? baseGridHeight : gradeHeight;
	}
	
	@Override
	public void reposition() {
		if (scrollContainer != null) {
			remove(scrollContainer);
		}
		super.reposition();
		grid.setRect(KEEP, KEEP, FILL, getGridHeight(numPadHeight + (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional ? descPromocionalGradeList.size() * fmH : 0)));
		grid.scrollTo(-1);
		montaGradeFechada();
	}

	@Override
	protected void addBtFechar() {
	}

	public void carregaGrid() throws SQLException {
    	if (itensGrade3List.size() > 0) {
    		carregaGridGrade3();
		} else if (itensGrade2List.size() > 0) {
			carregaGridGrade2();
    	} else if (itensGrade1List.size() > 0) {
    		carregaGridGrade1();
    	}
    	updateTotaisGradeProduto();
	}

	private double getQtItemByItemPedido(String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) {
		ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
		itemPedidoGrade.cdEmpresa = this.itemPedido.cdEmpresa;
		itemPedidoGrade.cdRepresentante = this.itemPedido.cdRepresentante;
		itemPedidoGrade.flOrigemPedido = this.itemPedido.flOrigemPedido;
		itemPedidoGrade.nuPedido = this.itemPedido.nuPedido;
		itemPedidoGrade.flTipoItemPedido = this.itemPedido.flTipoItemPedido;
		itemPedidoGrade.nuSeqProduto = this.itemPedido.nuSeqProduto;
		itemPedidoGrade.cdProduto = this.itemPedido.cdProduto;
		itemPedidoGrade.cdItemGrade1 = cdItemGrade1;
		itemPedidoGrade.cdItemGrade2 = cdItemGrade2;
		itemPedidoGrade.cdItemGrade3 = cdItemGrade3;
		int indexOf = this.itemPedido.itemPedidoGradeList.indexOf(itemPedidoGrade);
		if (indexOf < 0) {
			itemPedidoGrade.nuSeqProduto = 1;
			indexOf = this.itemPedido.itemPedidoGradeList.indexOf(itemPedidoGrade);
		}
		if (indexOf != -1) {
			ItemPedidoGrade itemPedidoGradeOfic = (ItemPedidoGrade)this.itemPedido.itemPedidoGradeList.items[indexOf];
			return itemPedidoGradeOfic.qtItemFisico;
		}
		return 0;
	}

	//@Override
	public void popup() {
		if (ValueUtil.isNotEmpty(produtoGradeList)) {
			super.popup();
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTO_SEM_ESTOQUE_GRADE);
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		if (grid != null) {
			grid.setItems(null);
		}
	}
	
	

	public void onEvent(Event event) {
		try {
		    super.onEvent(event);
		    switch (event.type) {
		    	case ControlEvent.PRESSED: {
		    		if (event.target == btOk) {
		    			btOkClick();
		    		} else if (event.target == btLimpar) {
		    			btLimparClick();
		    		} else if (event.target == btTotalizadores) {
		    			btTotalizadoresClick();
		    		} else if (event.target == numericPad) {
		    			KeyEvent ke = new KeyEvent();
		    			String s = numericPad.getSelectedItem();
		    			if (s != null) {
		    				if (s.equals("<")) {
		    					ke.key = SpecialKeys.BACKSPACE;
		    				} else {
		    					ke.key = s.charAt(0);
		    				}
		    				if (edBaseNumPad != null) {
		    					ke.target = edBaseNumPad;
		    					edBaseNumPad.onEvent(ke);
		    				}
		    			}
		    			numericPad.setSelectedIndex(-1);
		    		} else if(event.target == btLeitorCamera) {
		    			realizaLeituraCamera();
		    		} else if (event.target == btPrecos){
		    			btPrecosClick(!LavenderePdaConfig.usaGradeProduto5(), false);
		    		} else if (event.target == btEstoque){
		    			btEstoqueClick();
		    		} else if (event.target == btReplicar) {
		    			btReplicarClick();
		    		}
		    		break;
		    	}
		    	case EditIconEvent.PRESSED: {
		    		if (event.target == edFiltro) {
		    			btFiltrarClick(edFiltro.getText());
		    		}
		    		break;
		    	}
		    	case PenEvent.PEN_UP: {
					if (LavenderePdaConfig.usaGradeProduto4() && event.target instanceof BaseGridEdit && grid.getSelectedXYIndex()[1] >= 0) {
						if (((BaseGridEdit) event.target).getLastShownControl() == null) {
							int numMaxLinhas = grid.getItemsVector().size() - 1;
							int numMaxColunas = ((String[]) grid.getItemsVector().items[0]).length - 1;
							if (grid.getSelectedXYIndex()[0] >= 0 && grid.getSelectedXYIndex()[0] < numMaxLinhas && grid.getSelectedXYIndex()[1] >= 1 && grid.getSelectedXYIndex()[1] < numMaxColunas) {
								UiUtil.showInfoMessage(Messages.PRODUTO_GRADE_INSERIDO_SEM_PRECO_UNITARIO);
							}
						} else if (((BaseGridEdit) event.target).getLastShownControl() instanceof EditNumberFrac) {
							EditNumberFrac lastShownControl = (EditNumberFrac) ((BaseGridEdit) event.target).getLastShownControl();
							if (! abrePopupInfosComplementares(lastShownControl, grid.getSelectedXYIndex()) && lastShownControl != null) {
								lastShownControl.popupKCC();
							}
						}
					}
		    		break;
		    	}
		    	case ControlEvent.FOCUS_IN: {
		    		if (event.target instanceof BaseEdit && ((BaseEdit)event.target).isEditable()) {
		    			edBaseNumPad = (BaseEdit) event.target;
		    		} else {
		    			edBaseNumPad = new EditText("", 0);
		    		}
		    		break;
		    	}
		    	case ControlEvent.FOCUS_OUT: {
		    		if (event.target instanceof EditNumberFrac) {
		    			updateTotaisGradeProduto();
		    		}
		    		if ((!LavenderePdaConfig.usaTecladoVirtual || LavenderePdaConfig.usaTecladoFixoTelaItemPedido) && itemPedido.pedido.isPedidoAberto()) {
		    			edBaseNumPad = new EditText("", 0);
		    		}
		    		break;
		    	}
		    	case KeyEvent.SPECIAL_KEY_PRESS: {
		    		KeyEvent ke = (KeyEvent) event; 
		    		if (ke.isActionKey()) {
		    			btEnterClick(ke);
		    		}
		    		break;
		    	}
		    	case KeyEvent.KEY_PRESS: {
		    		KeyEvent ke = (KeyEvent) event;
		    		if (ke.key == 32 && !(ke.target instanceof EditFiltro)) {
		    			btTotalizadoresClick();
		    		}
		    		break;
		    	}
		    	case KeyboardEvent.KEYBOARD_PRESS: {
					if (event.target == edFiltro) {
						btFiltrarClick(edFiltro.getText());
					} 
					break;
		    	}
		    	case ValueChangeEvent.VALUE_CHANGE: {
					if (event.target instanceof BaseEdit) {
						onGridValueChange();
					}
					break;
				}
		    	case ValueChangeEvent.VALUE_CHANGE_GRID: {
		    		ValueChangeEvent ve = (ValueChangeEvent) event;
		    		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isUsaMultiploEspecialProduto() && ve.target instanceof EditNumberFrac) {
		    			controlaMultiploEspecial(ve.row, ve.col, ((EditNumberFrac)ve.target).getText());
		    		}
		    		break;
		    	}
		    	case GridEditEvent.COLUMN_PRESSED: {
		    		if (ValueUtil.isEmpty(itensGrade2List) && ValueUtil.isEmpty(itensGrade3List)) {
		    			int col = ((GridEditEvent)event).nuColumn;
		    			sortListGrade(col);
		    			repaintCelsByEstoque();
		    		}
		    		break;
		    	}
		    	case GridEvent.SELECTED_EVENT: {
		    		if (LavenderePdaConfig.usaGradeProduto5()) {
		    			GridEvent ge = (GridEvent) event;
		    			if ((ge.col == 0 || ge.row == -2) && ge.row != grid.size() - 1 && ge.row != -1) {
		    				btReplicarClick(ge.row, ge.col);
		    			}
					}
		    		break;
		    	}
	    	}
		    
	   	} catch (ValidationException e) {
	   		UiUtil.showErrorMessage(e);
    	} catch (Throwable ee) {
    		UiUtil.showErrorMessage(ee);
		}
	}

	private void btEnterClick(KeyEvent ke) throws SQLException {
		if (ke.target == edQtItem) {
			btReplicarClick();
		} else if (enterConfirmaInsercao && !(ke.target instanceof EditFiltro)) {
			grid.hideControl();
			ControlEvent ce = new ControlEvent(ControlEvent.PRESSED, btOk);
			postEvent(ce);
		} else if (ke.target instanceof EditFiltro) {
			btFiltrarClick(edFiltro.getText());
		}
	}

	private void onGridValueChange() {
		grid.hideControl();
		updateTotaisGradeProduto();
		if (!LavenderePdaConfig.usaTecladoVirtual || LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
			edBaseNumPad = new EditText("", 0);
		}
	}
	
	private void controlaMultiploEspecial(int row, int col, String value) {
		String cellText = LavenderePdaConfig.isUsaMultiploEspecialProduto() ? getGradeCellText(row, col - GRADE3_OFFSET, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro(), value, row, col) : value;
		grid.setCellText(row, col, cellText);
	}

	private void btReplicarClick(int row, int col) {
		if (row == -2 && (col > itensGrade3List.size() || col == 0)) return;
		WmwInputBox inputBox = new WmwInputBox(Messages.BOTAO_REPLICAR_GRADE, ValueUtil.VALOR_NI, 0);
		inputBox.getEdit().setText("");
		addListenerOnInput(inputBox);
		inputBox.popup();
		if (inputBox.getPressedButtonIndex() == 0) return;
		String qtdReplicar = inputBox.getValue();
		boolean usaQtdInteiro = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro();
		if (col == 0) {
			int size = itensGrade3List.size();
			for (int i = 1; i <= size; i++) {
				if (grid.gridController.isRowColEnabled(row, i)) {
					String cellText = LavenderePdaConfig.isUsaMultiploEspecialProduto() ? getGradeCellText(row, i - 1, usaQtdInteiro, qtdReplicar, row, i) : qtdReplicar;
					grid.setCellText(row, i, cellText);
				}
			}
		} else if (row == -2) {
			int size = itensGrade2List.size();
			for (int i = 0; i < size; i++) {
				if (grid.gridController.isRowColEnabled(i, col)) {
					String cellText = LavenderePdaConfig.isUsaMultiploEspecialProduto() ? getGradeCellText(i, col - 1, usaQtdInteiro, qtdReplicar, i, col) : qtdReplicar;
					grid.setCellText(i, col, cellText);
				}
			}
		}
		updateTotaisGradeProduto();
		grid.repaintNow();
	}

	private void addListenerOnInput(WmwInputBox inputBox) {
		inputBox.getEdit().addKeyListener(new KeyListener() {
			@Override
			public void specialkeyPressed(KeyEvent ke) {
				if (ke.isActionKey()) {
					inputBox.unpop();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
			@Override
			public void actionkeyPressed(KeyEvent ke) {
			}
		});
	}

	private int getItemGradeRow(ItemPedido itemPedido) {
		for (int row = 0; row < itensGrade2List.size(); row++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade2List.items[row];
			if (ValueUtil.valueEqualsIfNotNull(itemPedido.cdItemGrade2, itemGrade.cdItemGrade)) {
				return row;
			}
		}
		return -1;
	}
	
	private int getItemGradeColumn(ItemPedido itemPedido) {
		for (int column = 0; column < itensGrade3List.size(); column++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade3List.items[column];
			if (ValueUtil.valueEqualsIfNotNull(itemPedido.cdItemGrade3, itemGrade.cdItemGrade)) {
				return column;
			}
		}
		return -1;
	}
	
	private int[] getItemGradePosition(ItemPedido itemPedido) {
		final int row = getItemGradeRow(itemPedido);
		if (row != -1) {
			final int column = getItemGradeColumn(itemPedido);
			if (column != -1) {
				return new int[] {row, column + 1};
			}
		}
		return null;
	}
	
	private String getGradeCellText(int row, int col, boolean usaQtdInteiro, String qtdReplicar, int rRow, int rCol) {
		ItemGrade itemGrade2 = (ItemGrade) itensGrade2List.items[row];
		ItemGrade itemGrade3 = (ItemGrade) itensGrade3List.items[col];
		double qtdPermitida = ProdutoService.getInstance().getMaxMultiploEspecialPermitido(ValueUtil.getDoubleSimpleValue(qtdReplicar), hashProdutoGrade.get(ProdutoGrade.getGradeKey(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade)));
		if (LavenderePdaConfig.informaQuantidadePrimeiroMultiploNaoAtingido && qtdPermitida == 0d) {
			grid.gridController.setCelForeColor(LavendereColorUtil.COR_FUNDO_ITEM_SEM_MULTIPLO, rRow, rCol);
			return StringUtil.getStringValueToInterface(usaQtdInteiro ? ValueUtil.getIntegerValue(qtdReplicar) : ValueUtil.getDoubleSimpleValue(qtdReplicar), usaQtdInteiro ? 0 : ValueUtil.doublePrecisionInterface);
		}
		grid.gridController.setCelForeColor(-1, rRow, rCol);
		return StringUtil.getStringValueToInterface(usaQtdInteiro ? ValueUtil.getIntegerValue(qtdPermitida) : qtdPermitida, usaQtdInteiro ? 0 : ValueUtil.doublePrecisionInterface);
	}
	
	private void btReplicarClick() {
		int[] rows = UiUtil.getSelectedIndexesOrEverything(cbItemGrade2);
		int[] cols = UiUtil.getSelectedIndexesOrEverything(cbItemGrade3);
		if (edQtItem.getValueInt() == 0 || ValueUtil.isEmpty(rows) || ValueUtil.isEmpty(cols)) {
			throw new ValidationException(Messages.MSG_ERRO_REPLICAR_GRADE_QUANTIDADE);
		}
		String value = StringUtil.getStringValue(edQtItem.getValueInt());
		boolean usaQtdInteiro = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro();
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				if (grid.gridController.isRowColEnabled(rows[i], cols[j] + 1)) {
					String cellText = LavenderePdaConfig.isUsaMultiploEspecialProduto() ? getGradeCellText(rows[i], cols[j], usaQtdInteiro, value, rows[i], cols[j] + 1) : value;
					grid.setCellText(rows[i], cols[j] + 1, cellText);
				}
			}
		}
		updateTotaisGradeProduto();
		grid.repaintNow();
	}

	private void repaintCelsByEstoque() throws SQLException {
		boolean corAtualZebrado = true;
		int rowSize = itensGrade1List.size();
		for (int i = 0; i < rowSize; i++) {
			String[] itens = grid.getItem(i);
			double qtEstoque = ValueUtil.getDoubleValue(itens[1]);
			reGrifaGrade1(i, qtEstoque, corAtualZebrado);
			corAtualZebrado = !corAtualZebrado;
		}
	}
	
	private void sortListGrade(int column) {
		ascending = column == lastSortCol ? !ascending : true;
		lastSortCol = column;
		switch (column) {
		case 0:
			SortUtil.qsortString(itensGrade1List.items, 0, itensGrade1List.size() - 1, ascending);
			SortUtil.qsortString(itensGrade1ListAuxiliar.items, 0, itensGrade1ListAuxiliar.size() - 1, ascending);
			break;
		case 1:
			ItemGrade.sortAttr = LavenderePdaConfig.isOcultaEstoque() ? ItemGrade.NMCOLUMN_QTITEMGRADE : ItemGrade.NMCOLUMN_QTESTOQUEITEM;
			SortUtil.qsortDouble(itensGrade1List.items, 0, itensGrade1List.size() - 1, ascending);
			SortUtil.qsortDouble(itensGrade1ListAuxiliar.items, 0, itensGrade1ListAuxiliar.size() - 1, ascending);
			break;
		case 2:
			ItemGrade.sortAttr = ItemGrade.NMCOLUMN_QTITEMGRADE;
			SortUtil.qsortDouble(itensGrade1List.items, 0, itensGrade1List.size() - 1, ascending);
			SortUtil.qsortDouble(itensGrade1ListAuxiliar.items, 0, itensGrade1ListAuxiliar.size() - 1, ascending);
			break;
		}
		grid.qsort(column, ascending);
	}
	
	private boolean abrePopupInfosComplementares(EditNumberFrac control, int[] posXY) {
		if (LavenderePdaConfig.usaGradeProduto4()) {
			if (posXY == null) return false;
			
			ItemGrade itemGrade2 = (ItemGrade) itensGrade2List.items[posXY[0]];
			ItemGrade itemGrade3 = ValueUtil.isNotEmpty(itensGrade3List) ? (ItemGrade) itensGrade3List.items[posXY[1] - 1] : null;
			itemGrade3 = itemGrade3 != null ? itemGrade3 : new ItemGrade();
			itemGrade2 = itemGrade2 != null ? itemGrade2 : new ItemGrade();
			ProdutoGrade produtoGradeFilter = ProdutoGradeService.getInstance().getProdutoGradeFilter(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, itemPedido.cdProduto, itemPedido.cdTabelaPreco);
			try {
				ProdutoGrade produtoGrade = produtoGradeHash.get(produtoGradeFilter.getRowKey());
				if (produtoGrade == null) {
					produtoGrade = (ProdutoGrade) ProdutoGradeService.getInstance().findByPrimaryKey(produtoGradeFilter);
					if (produtoGrade == null) return false;
					
					ItemPedido itemPedidoPorGrade = buscaItemPedidoPorGrade(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
					if (itemPedidoPorGrade != null) {
						produtoGrade.qtItemFisico = (int) itemPedidoPorGrade.getQtItemFisico();
						produtoGrade.vlAltura = itemPedidoPorGrade.vlAltura;
						produtoGrade.vlLargura = itemPedidoPorGrade.vlLargura;
						produtoGrade.vlComprimento = itemPedidoPorGrade.vlComprimento;
					}
				}
				if (produtoGrade.showPopUpInfosComplementares()) {
					String[] rowGrid = grid.getItem(posXY[0]);
					double qtInserida = ValueUtil.getDoubleValue(rowGrid[posXY[1]].replace(".", ""));
					CadInfoComplProdutoGradeWindow cadInfoComplProdutoGradeWindow = new CadInfoComplProdutoGradeWindow(itemPedido.pedido, produtoGrade, itemPedido.cdTabelaPreco, itemPedido.cdUfClientePedido, itemPedido.pedido.isPedidoAberto(), qtInserida);
					cadInfoComplProdutoGradeWindow.popup();
					if (cadInfoComplProdutoGradeWindow.removeuItem) {
						control.setValue(0);
						produtoGradeHash.remove(produtoGrade.getRowKey());
						requestFocus();
					} else if (cadInfoComplProdutoGradeWindow.salvouItem) {
						produtoGradeHash.put(produtoGrade.getRowKey(), produtoGrade);
						control.setValue(produtoGrade.qtItemFisico);
						requestFocus();
					}
					return true;
				}
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
		return false;
	}

	private ItemPedido buscaItemPedidoPorGrade(String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) {
		return buscaItemPedidoPorGrade(null, cdItemGrade1, cdItemGrade2, cdItemGrade3);
	}
	
	private ItemPedido buscaItemPedidoPorGrade(Map<String, ProdutoGrade> hashProdutoGrade, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) {
		cdItemGrade2 = cdItemGrade2 != null ? cdItemGrade2 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		cdItemGrade3 = cdItemGrade3 != null ? cdItemGrade3 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		int size = itemPedidoPrecoGradeList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPrecoGradeList.items[i];
			if ((ValueUtil.valueEquals(cdItemGrade1, itemPedido.cdItemGrade1) && ValueUtil.valueEquals(cdItemGrade2, itemPedido.cdItemGrade2) && ValueUtil.valueEquals(cdItemGrade3, itemPedido.cdItemGrade3))) {
				return itemPedido;
			} else if (hashProdutoGrade != null) {
				ProdutoGrade produtoGrade = hashProdutoGrade.get(ProdutoGrade.getGradeKey(cdItemGrade1, cdItemGrade2, cdItemGrade3));
				if (produtoGrade != null && ValueUtil.valueEqualsIfNotNull(produtoGrade.cdProduto, itemPedido.cdProduto)) return itemPedido;
			}
		}
		return null;
	}
		
	private void setaItemPedidoPorGradePreco3() throws SQLException  {
		int sizeGrade2 = itensGrade2List.size();
		if (!LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			itemPedido.setQtItemFisico(0);
		}
		for (int i = 0; i < sizeGrade2; i++) {
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			String[] rowGrid = grid.getItem(i);
			int sizeGrade3 = itensGrade3List.size();
			for (int j = 0; j < sizeGrade3; j++) {
				double qtInserida = ValueUtil.getDoubleValue(rowGrid[j + 1].replace(".", ""));
				if (qtInserida == 0 && !LavenderePdaConfig.usaGradeProduto5()) continue;
				ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[j];
				ItemPedido itemPedidoPorGradePreco = buscaItemPedidoPorGrade(null, itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
				if (itemPedidoPorGradePreco == null) continue;
				if (LavenderePdaConfig.usaGradeProduto5()) setFlEdicaoItemPedidoGrade(itemPedidoPorGradePreco, qtInserida);
				itemPedidoPorGradePreco.setQtItemFisico(qtInserida);
				if (LavenderePdaConfig.usaGradeProduto4()) {
					itemPedidoPorGradePreco.vlPctDesconto = itemPedido.vlPctDesconto;
					itemPedidoPorGradePreco.vlPctAcrescimo = itemPedido.vlPctAcrescimo;
					itemPedidoPorGradePreco.setProduto(itemPedido.getProduto());
				}
				setInfosAdicionaisItemPedido(itemPedidoPorGradePreco, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
				itemPedido.itemPedidoPorGradePrecoList.removeElement(itemPedidoPorGradePreco);
				if (!isConversaoUnidadeCompleta(qtInserida)) {
					if (ValueUtil.isNotEmpty(itensComProblemas.toString())) {
						itensComProblemas.append(", ");
					} 
					itensComProblemas.append(StringUtil.getStringValue(itemGrade2.dsItemGrade)).append(" - ").append(StringUtil.getStringValue(itemGrade3.dsItemGrade));
					itensGradeComProblemaList.addElement(itemPedidoPorGradePreco);
					continue;
				}
				if (qtInserida > 0 || itemPedidoPorGradePreco.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_EXCLUINDO) {
					ItemPedidoService.getInstance().loadPoliticaComercial(itemPedidoPorGradePreco, itemPedido.pedido);
					setaVlItemDescAcresCapaPedido(itemPedidoPorGradePreco);
					totalizaEAgrupaItemPedido(qtInserida, itemPedidoPorGradePreco);
				}
			}
		}
	}
	
	private void validaMultiploEspecial() {
		int sizeGrade2 = itensGrade2List.size();
		itemPedido.setQtItemFisico(0);
		for (int i = 0; i < sizeGrade2; i++) {
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			String[] rowGrid = grid.getItem(i);
			int sizeGrade3 = itensGrade3List.size();
			for (int j = 0; j < sizeGrade3; j++) {
				double qtInserida = ValueUtil.getDoubleValue(rowGrid[j + 1].replace(".", ""));
				if (qtInserida == 0) continue;
				ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[j];
				double qtdPermitida = ProdutoService.getInstance().getMaxMultiploEspecialPermitido(qtInserida, hashProdutoGrade.get(ProdutoGrade.getGradeKey(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade)));
				if (LavenderePdaConfig.informaQuantidadePrimeiroMultiploNaoAtingido && qtdPermitida == 0d) {
					throw new ValidationException(Messages.MSG_ERRO_MULTIPLO_GRADE);
				}
			}
		}
	}

	private void setaVlItemDescAcresCapaPedido(ItemPedido itemPedidoPorGradePreco) throws SQLException {
		boolean aplicaDescAcresCapaPedido = itemPedidoPorGradePreco.vlPctDesconto == 0d && itemPedidoPorGradePreco.vlPctAcrescimo == 0d;	
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && itemPedido.pedido.vlPctDescItem > 0d && aplicaDescAcresCapaPedido) {
			if (LavenderePdaConfig.isAcumulaComDescDoItem()) {
				itemPedidoPorGradePreco.vlPctDescPedido = itemPedido.pedido.vlPctDescItem;
			} else {
				itemPedidoPorGradePreco.vlPctDesconto = itemPedido.pedido.vlPctDescItem;
			}
			double vlItemGrade = ValueUtil.round(itemPedidoPorGradePreco.vlBaseItemPedido * (1 - itemPedido.pedido.vlPctDescItem / 100));
			itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			ItemPedidoService.getInstance().setaVlItemGradeAgrupador(false, vlItemGrade, itemPedidoPorGradePreco);
		} else if (LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() && itemPedido.pedido.vlPctAcrescimoItem > 0d && aplicaDescAcresCapaPedido) {
			itemPedidoPorGradePreco.vlPctAcrescimo = itemPedido.pedido.vlPctAcrescimoItem;
			double vlItemGrade = ValueUtil.round(itemPedidoPorGradePreco.vlBaseItemPedido * (1 + itemPedido.pedido.vlPctAcrescimoItem / 100));
			itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
			ItemPedidoService.getInstance().setaVlItemGradeAgrupador(false, vlItemGrade, itemPedidoPorGradePreco);
		}
	}
	
	private void setFlEdicaoItemPedidoGrade(ItemPedido itemPedidoPorGradePreco, double qtInserida) {
		if (itemPedidoPorGradePreco.flEdicaoItemPedidoGrade == ItemPedido.ITEMPEDIDO_SEM_EDICAO && (cadItemPedidoForm.isEditing() || LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade())) {
			if (itemPedidoPorGradePreco.getQtItemFisico() == 0d && qtInserida > 0d) itemPedidoPorGradePreco.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_INSERINDO;
			else if (itemPedidoPorGradePreco.getQtItemFisico() > 0d && qtInserida > 0d) itemPedidoPorGradePreco.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_ATUALIZANDO;
			else if (itemPedidoPorGradePreco.getQtItemFisico() > 0d && qtInserida == 0d) itemPedidoPorGradePreco.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_EXCLUINDO;
		}
	}

	private void setInfosAdicionaisItemPedido(ItemPedido itemPedido, String cdItemGrade2, String cdItemGrade3) {
		ProdutoGrade produtoGradeFilter = ProdutoGradeService.getInstance().getProdutoGradeFilter(itemPedido.cdItemGrade1, cdItemGrade2, cdItemGrade3, itemPedido.cdProduto, itemPedido.cdTabelaPreco);
		ProdutoGrade produtoGrade = produtoGradeHash.get(produtoGradeFilter.getRowKey());
		if (produtoGrade != null) {
			itemPedido.vlAltura = produtoGrade.vlAltura;
			itemPedido.vlLargura = produtoGrade.vlLargura;
			itemPedido.vlComprimento = produtoGrade.vlComprimento;
			itemPedido.vlIndiceVolume = produtoGrade.vlIndiceVolume;
			if (produtoGrade.vlBaseItemTabelaPreco > 0) {
				itemPedido.vlBaseItemTabelaPreco = produtoGrade.vlBaseItemTabelaPreco; 		
				itemPedido.vlBaseItemPedido = produtoGrade.vlBaseItemPedido;		
				itemPedido.vlItemPedido = produtoGrade.vlItemPedido;		
				itemPedido.vlUnidadePadrao = produtoGrade.vlUnidadePadrao;
			}
		}
	}

	private void setaItemPedidoPorGradePreco2() throws SQLException  {
		int sizeGrade2 = itensGrade2List.size();
		itemPedido.setQtItemFisico(0);
		for (int i = 0; i < sizeGrade2; i++) {
			String[] rowGrid = grid.getItem(i);
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			double qtInserida = ValueUtil.getDoubleValue(rowGrid[1].replace(".", ""));
			ItemPedido itemPedidoPorGradePreco = buscaItemPedidoPorGrade(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
			if (itemPedidoPorGradePreco == null) continue;
			itemPedidoPorGradePreco.setQtItemFisico(qtInserida);
			itemPedidoPorGradePreco.vlPctDesconto = itemPedido.vlPctDesconto;
			itemPedidoPorGradePreco.vlPctAcrescimo = itemPedido.vlPctAcrescimo;
			itemPedidoPorGradePreco.setProduto(itemPedido.getProduto());
			setInfosAdicionaisItemPedido(itemPedidoPorGradePreco, itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
			if (qtInserida == 0) continue; 
			totalizaEAgrupaItemPedido(qtInserida, itemPedidoPorGradePreco);
		}
	}
	

	private void totalizaEAgrupaItemPedido(double qtInserida, ItemPedido itemPedidoPorGradePreco) {
		itemPedidoPorGradePreco.vlTotalItemPedido = itemPedidoPorGradePreco.vlItemPedido * qtInserida;
		if (!LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + qtInserida);
			itemPedido.vlTotalItemPedido += itemPedidoPorGradePreco.vlTotalItemPedido;
		} else if (itemPedido.equals(itemPedidoPorGradePreco)) {
			Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
			itemPedido = itemPedidoPorGradePreco;
			itemPedido.itemPedidoPorGradePrecoList = itemPedidoPorGradePrecoList;
		}
		if (LavenderePdaConfig.usaGradeProduto5()) {
			if (itemPedidoPorGradePreco.flTipoEdicao == ItemPedido.ITEMPEDIDO_SEM_EDICAO) {
				itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
			}
			itemPedidoPorGradePreco.naoComparaSeqItem = true;
			itemPedido.itemPedidoPorGradePrecoList.removeElement(itemPedidoPorGradePreco);
			itemPedidoPorGradePreco.naoComparaSeqItem = false;
		}
		itemPedido.itemPedidoPorGradePrecoList.addElement(itemPedidoPorGradePreco);
	}
	
	private void setaItemPedidoPorGradePreco() throws SQLException {
		itensGradeComProblemaList = new Vector();
		itensComProblemas = new StringBuffer();
		if (LavenderePdaConfig.usaGradeProduto4()) {
			itemPedido.itemPedidoPorGradePrecoList.removeAllElements();
		}
		itemPedido.setQtItemFisico(itemPedido.getQtItemFisicoAtualizaEstoque());
		itemPedido.qtItemFaturamento = itemPedido.oldQtItemFaturamento;
		itemPedido.vlTotalItemPedido = 0;
		if (ValueUtil.isNotEmpty(itensGrade3List)) {
			setaItemPedidoPorGradePreco3();
		} else if (ValueUtil.isNotEmpty(itensGrade2List)) {  
			setaItemPedidoPorGradePreco2();
		} 
	}
	
	private boolean validaGradeProduto5(Vector itemPedidoPorGradePrecoList) throws SQLException {
		int size = itemPedidoPorGradePrecoList.size();
		
		Map<int[], ItemGradeCellForeColor> itemGradeCellColors = new HashMap<>();
		
		boolean isValid = true;
		boolean hasEstoqueNegativoPermitido = false, hasEstoqueNegativoNaoPermitido = false;
		
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)itemPedidoPorGradePrecoList.items[i];
			if (itemPedido.pedido == null) {
				itemPedido.pedido = this.itemPedido.pedido;
			}
			boolean isCurrentItemValid;
			
			int[] gridLocation = getItemGradePosition(itemPedido);
			if (gridLocation == null) continue;
			try {
				isCurrentItemValid = validaProdutoGrade(itemPedido, false, true);
			} catch (EstoqueException e) {
				
				if (permiteOuIgnoraEstoqueNegativo(itemPedido)) {
					itemGradeCellColors.put(gridLocation, ItemGradeCellForeColor.ESTOQUE_NEGATIVO_PERMITIDO);
					hasEstoqueNegativoPermitido = true;
				} else {
					itemGradeCellColors.put(gridLocation, ItemGradeCellForeColor.ESTOQUE_NEGATIVO_NAO_PERMITIDO);
					hasEstoqueNegativoNaoPermitido = true;
				}
				
				isCurrentItemValid = false;
			} catch (Throwable e) {
				isCurrentItemValid = false;
			}
			
			grid.gridController.setCelForeColor(ColorUtil.componentsForeColor, gridLocation[0], gridLocation[1]);
			
			isValid &= isCurrentItemValid;
		}
		
		if (hasEstoqueNegativoPermitido && hasEstoqueNegativoNaoPermitido) {
			UiUtil.showErrorMessage(Messages.ESTOQUE_MSG_GRADE_PRODUTOS_ESTOQUE_NEGATIVO_E_SEMESTOQUE);
		} else if (hasEstoqueNegativoNaoPermitido) {
			UiUtil.showErrorMessage(Messages.ESTOQUE_MSG_GRADE_PRODUTOS_ESTOQUE_NEGATIVO);
		} else if (hasEstoqueNegativoPermitido){
			if (UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_GRADE_LIBERADO)) {
				marcaIgnoraSegundaValidacao(size, itemPedidoPorGradePrecoList, true);
				return true;
			} else {
				marcaIgnoraSegundaValidacao(size, itemPedidoPorGradePrecoList, false);
			}
		}
		
		refreshGradeItemForeColors(itemGradeCellColors);
		
		return isValid;
	}
	
	private void marcaIgnoraSegundaValidacao(int size, Vector itemPedidoPorGradePrecoList, boolean ignore) {
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)itemPedidoPorGradePrecoList.items[i];
			itemPedido.isIgnoraMensagemEstoqueNegativo = ignore;
		}
		this.itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo = true;
	}
	
	private void refreshGradeItemForeColors(Map<int[], ItemGradeCellForeColor> itemGradeCellForeColors) {
		for (int[] gridLocation : itemGradeCellForeColors.keySet()) {
			ItemGradeCellForeColor cellForeColor = itemGradeCellForeColors.get(gridLocation);
			grid.gridController.setCelForeColor(cellForeColor.getColor(), gridLocation[0], gridLocation[1]);
		}
	}
	
	private boolean validacoesGradeProduto4(Vector itemPedidoPorGradePrecoList) throws Exception {
		int size = itemPedidoPorGradePrecoList.size();
		
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
			if (!validaProdutoGrade(itemPedido, true, false)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validaGrade(Vector itemPedidoPorGradePrecoList) throws Exception {
		if (LavenderePdaConfig.usaGradeProduto5()) {
			return validaGradeProduto5(itemPedido.itemPedidoPorGradePrecoList);
		} else {
			return validacoesGradeProduto4(itemPedido.itemPedidoPorGradePrecoList);
		}
	}
	
	private boolean validaProdutoGrade(ItemPedido itemPedido, boolean showMessage, boolean alwaysThrowEstoqueException) throws Exception {
		ItemPedidoService.getInstance().validateMinGradesAndQtMin(itemPedido);
		if (ItemPedidoService.getInstance().avisaControleItemPedido(itemPedido)) {
			return false;
		}
		if (!validaEstoque(itemPedido, showMessage, alwaysThrowEstoqueException)) {
			acionaRelatorioComEstoqueInsuficiente(itemPedido);
			return false;
		}
		if (!LavenderePdaConfig.usaGradeProduto5()) {
			validaPreco(itemPedido);
		}
		return true;
	}

	private void btOkClick() throws Exception {
		UiUtil.showProcessingMessage();
		try {
			atualizaListaGradeAuxiliar();
			if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
				setaItemPedidoPorGradePreco();
				if (LavenderePdaConfig.usaGradeProduto5()) {
					if (LavenderePdaConfig.informaQuantidadePrimeiroMultiploNaoAtingido) {
						validaMultiploEspecial();
					}
					if (!LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
						totalizaItemPedidoEdicao();
					}
				}
				if (validaGrade(itemPedido.itemPedidoPorGradePrecoList)) {
					Vm.safeSleep(500);
					unpop();
				}
				return;
			}
			setPedidoItemGrade();
			boolean closePopup = ValueUtil.isEmpty(itensComProblemas.toString()) || isResolveProblemasArredondamento() ;
			if (!closePopup) return;
			
			itemPedido.setQtItemFisico(0);
			itemPedido.qtItemFaturamento = 0;
			setItemPedidoGradeNoItemPedido();
			if (LavenderePdaConfig.usaMultiploEspecialPorGradeProdutoPromocional && itemPedidoGrade2List.size() > 0) {
				itemPedido.itemGradeList = itensGrade2List;
				itemPedido.descPromocionalGradeList = descPromocionalGradeList;
			}
			ItemPedidoService.getInstance().validateMinGradesAndQtMin(itemPedido);
			if (ItemPedidoService.getInstance().avisaControleItemPedido(itemPedido)) return;
			
			if (validaEstoque(itemPedido, true, false)) { 
				unpop();
			} else {
				acionaRelatorioComEstoqueInsuficiente(itemPedido);
			}
			if (LavenderePdaConfig.usaGradeProduto4()) {
				removeInfosAdicionaisItem();
			}
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	private void totalizaItemPedidoEdicao() {
		int size = itemPedido.itemPedidoPorGradePrecoList.size();
		itemPedido.setQtItemFisico(0);
		itemPedido.vlTotalItemPedido = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedido.itemPedidoPorGradePrecoList.items[i];
			itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + itemPedidoPorGradePreco.getQtItemFisico());
			itemPedido.vlTotalItemPedido += itemPedidoPorGradePreco.vlTotalItemPedido;
		}
	}

	private void atuallizaQuantidadeItemGrade() {
		int size = grid.size() -1;
		int j = 0, indexOfList = 0;
		for (int i = 0; i < size; i++) {
			String[] item = grid.getItem(i);
			ItemGrade itemGrade1 = (ItemGrade) itensGrade1ListAuxiliar.items[i];
			if (!isItemGradeComProblemaEstoque(itemGrade1)) continue; 
			for (int k = 0; k < itemPedido.itemPedidoGradeList.size(); k++) {
				ItemPedidoGrade itemPedidoGradeOfic = (ItemPedidoGrade)this.itemPedido.itemPedidoGradeList.items[j];
				if (ValueUtil.valueEquals(itemGrade1.dsItemGrade, itemPedidoGradeOfic.itemGrade1.dsItemGrade)) {
					indexOfList = j;
					break;
				}
				j++;
			}
			ItemPedidoGrade itemPedidoGradeOfic = (ItemPedidoGrade)this.itemPedido.itemPedidoGradeList.items[indexOfList];
			itemPedidoGradeOfic.qtItemFisico =  ValueUtil.getDoubleValueTruncated(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(itemGrade1.qtItemGrade) : itemGrade1.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			item[2] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(itemGrade1.qtItemGrade) : itemGrade1.qtItemGrade, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		}
		updateTotaisGradeProduto();
		grid.repaintNow();
	}
	
	private boolean isItemGradeComProblemaEstoque(ItemGrade itemGradeFilter) {
		List<ItemPedidoGrade> itemPedidoGradeList = itemPedido.getItemPedidoGradeErroList();
		for (ItemPedidoGrade itemPedidoGrade : itemPedidoGradeList) {
			if (ValueUtil.valueEquals(itemGradeFilter, itemPedidoGrade.itemGrade1)) return true;
		}
		return false;
	}
	
	private boolean validaEstoque(ItemPedido itemPedido, boolean showConfirmation, boolean alwaysCreateEstoqueException) throws Exception {
		if (!LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
			return true;
		}
		try {
			EstoqueService.getInstance().validateEstoque(itemPedido.pedido, itemPedido, false);
		} catch (ValidationException e) {
			if (alwaysCreateEstoqueException) {
				throw createEstoqueException(e);
			}
			if (permiteOuIgnoraEstoqueNegativo(itemPedido) && showConfirmation) {
				boolean ignore = UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_GRADE_LIBERADO);
				itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo = ignore;
				if (ignore) {
					return true;
				}
				throw e;
			} else {
				if (isLancaExcecao()) {
					throw e;
				}
				UiUtil.showErrorMessage(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	private EstoqueException createEstoqueException(ValidationException e) {
		if (e instanceof EstoqueException) {
			return (EstoqueException)e;
		}
		return new EstoqueException(e.getMessage(), null);
	}
	
	private boolean permiteOuIgnoraEstoqueNegativo(ItemPedido itemPedido) throws SQLException {
		Produto produto = ProdutoService.getInstance().getProduto(itemPedido.cdProduto);
		return produto != null && produto.isPermiteEstoqueNegativo();
	}
	
	private void validaPreco(ItemPedido itemPedido) throws SQLException {
		String cdItemGrade1 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade1;
		String cdItemGrade2 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade2;
		String cdItemGrade3 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade3;
		ItemTabelaPreco itemTabelaPrecoGrade = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(itemPedido.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido, cdItemGrade1, cdItemGrade2, cdItemGrade3);
		if (itemTabelaPrecoGrade != null && itemTabelaPrecoGrade.cdTabelaPreco != null) return;
		
		throw new ValidationException(Messages.PRODUTO_GRADE_INSERIDO_SEM_PRECO);
	}

	private boolean isLancaExcecao() {
		return itemPedido.getItemPedidoGradeErroList().size() == 0 || LavenderePdaConfig.getConfigGradeProduto() != ItemGrade.GRADE_NIVEL_2 || LavenderePdaConfig.isNaoMostraListaEstoqueGradeFaltante();
	}

	private void acionaRelatorioComEstoqueInsuficiente(ItemPedido itemPedido) throws SQLException {
		ListEstoqueGradeFaltanteWindow listEstoqueGradeFaltanteWindows = new ListEstoqueGradeFaltanteWindow(itemPedido);
		listEstoqueGradeFaltanteWindows.popup();
		if (!listEstoqueGradeFaltanteWindows.isConfirmouAlteracao()) return;
		
		atuallizaQuantidadeItemGrade();
	}

	private void btLimparClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.GRADE_BT_LIMPAR)) {
			produtoGradeHash =  new HashMap<String, ProdutoGrade>();
			if (itensGrade3List.size() > 0) {
				for (int i = 0; i < itensGrade2List.size(); i++) {
					String[] rowGrid = grid.getItem(i);
					for (int j = 0; j < itensGrade3List.size(); j++) {
						if (!LavenderePdaConfig.usaGradeProduto5() || grid.gridController.isRowColEnabled(i, j + 1)) {
							rowGrid[j + 1] = "0";
						}
					}
					updateTotaisGrade3();
				}
			} else if (itensGrade2List.size() > 0) {
				if (LavenderePdaConfig.usaGradeProduto2()) {
					for (int i = 0; i < itensGrade1List.size(); i++) {
						String[] rowGrid = grid.getItem(i);
						for (int j = 0; j < itensGrade2List.size(); j++) {
							rowGrid[j + 1] = "0";
						}
					}
				} else {
					for (int i = 0; i < itensGrade2List.size(); i++) {
						String[] rowGrid = grid.getItem(i);
						rowGrid[1] = "0";
					}
				}
				updateTotaisGrade2();
			} else if (itensGrade1List.size() > 0) {
				for (int i = 0; i < itensGrade1List.size(); i++) {
		        	String[] rowGrid = grid.getItem(i);
		        	rowGrid[LavenderePdaConfig.isOcultaEstoque() ? 1 : 2] = "0";
				}
				updateQtInserido();
				updateTotaisGrade1();
				if (LavenderePdaConfig.isUsaFiltroGradeProduto() && !isGradeNivel2()) {
					btFiltrarClick("");
				}
			}
			repaintNow();
		}
	}

	private void removeInfosAdicionaisItem() throws SQLException {
		int size = itemPedidoPrecoGradeList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPrecoGradeList.items[i];
			if (itemPedido.vlAltura > 0) {
				itemPedido.setQtItemFisico(0);
				itemPedido.vlAltura = 0;
				itemPedido.vlLargura = 0;
				itemPedido.vlComprimento = 0;
				itemPedido.vlIndiceVolume = 0;
				PedidoService.getInstance().resetDadosItemPedido(itemPedido.pedido, itemPedido);
			}
		}
	}
	
	private void btTotalizadoresClick() throws SQLException {
		Pedido pedido = PedidoService.getInstance().findPedidoByItemPedido(itemPedido);
		verificaItemJaLancado(pedido);
		itemPedido.setQtItemFisico(0);
		setItemPedidoGradeNoItemPedido();
		CadTotalizadoresPedidoWindow cadTotalizadoresPedido = new CadTotalizadoresPedidoWindow(pedido, itemPedido);
		cadTotalizadoresPedido.popup();
	}

	private void verificaItemJaLancado(Pedido pedido) throws SQLException {
		Pedido pedidoClone = (Pedido) pedido.clone();
		PedidoService.getInstance().findItemPedidoList(pedidoClone);
		boolean itemJaSalvo = ItemPedidoService.getInstance().isItemJaInseridoMesmaUnidade(pedidoClone, itemPedido);
		pedido.itemPedidoList = null;
		setPedidoItemGrade();
		if (itemJaSalvo) {
			itemPedido.setQtItemFisico(0);
		}
	}

	private void setItemPedidoGradeNoItemPedido() {
		setItemPedidoGradeNoItemPedido(itemPedidoGrade3List);
		setItemPedidoGradeNoItemPedido(itemPedidoGrade2List);
		setItemPedidoGradeNoItemPedido(itemPedidoGrade1List);
	}

	private void setPedidoItemGrade() throws SQLException {
		itensGradeComProblemaList = new Vector();
		itensComProblemas = new StringBuffer();
		itemPedido.itemPedidoGradeList.removeAllElements();
		itemPedido.setQtItemFisico(itemPedido.getQtItemFisicoAtualizaEstoque());
		itemPedido.qtItemFaturamento = itemPedido.oldQtItemFaturamento;
		if (ValueUtil.isNotEmpty(itensGrade3List)) {
			setItemPedidoGrade3List();
		} else if (ValueUtil.isNotEmpty(itensGrade2List)) {
			setItemPedidoGrade2List();
		} else if (ValueUtil.isNotEmpty(itensGrade1List)) {
			setItemPedidoGrade1List();
		}
	}
	
	private void setItemPedidoGradeNoItemPedido(Vector itemPedidoGradeList) {
		for (int i = 0; i < itemPedidoGradeList.size(); i++) {
			ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedidoGradeList.items[i];
			itemPedido.itemPedidoGradeList.addElement(itemPedidoGrade);
			itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + itemPedidoGrade.qtItemFisico);
		}
	}

	private void updateTotaisGradeProduto() {
		if (itensGrade3List.size() > 0) {
    		updateTotaisGrade3();
		} else if (itensGrade2List.size() > 0) {
			updateTotaisGrade2();
		} else if (itensGrade1List.size() > 0) {
			updateTotaisGrade1();
		}
	}
	
	private void updateTotaisGrade1() {
		double qtTtItemFisico = 0;
		double qtTtEstoque = 0;
		int sizeDifference = LavenderePdaConfig.isOcultaEstoque() ? 1 : 0;
		String[] itemTotais = new String[3 - sizeDifference];
        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
		int size = grid.size();
        for (int i = 0; i < (size - 1); i++) {
        	double qtItemFisico = ValueUtil.getDoubleValue(grid.getItem(i)[2 - sizeDifference].replace(".", "")); 
    		qtTtItemFisico += qtItemFisico;
    		qtTtEstoque += ValueUtil.getDoubleValue(grid.getItem(i)[1].replace(".", ""));
    		((ItemGrade)itensGrade1List.items[i]).qtItemGrade = qtItemFisico;
        }
        if (!LavenderePdaConfig.isOcultaEstoque() && !LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
        	itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTtEstoque) : qtTtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
        }
        itemTotais[2 - sizeDifference] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        grid.setItens(itemTotais, grid.size() - 1);
        atualizaListaGradeAuxiliar();
	}
	
	private void updateTotaisGrade2() {
		double qtTtItemFisico = 0;
		if (LavenderePdaConfig.usaGradeProduto2()) {
			String[] itemTotais = new String[itensGrade2List.size() + 2];
            itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
    		int size = grid.size();
            for (int i = 0; i < (size - 1); i++) {
            	String[] item = grid.getItem(i);
            	double ttItemGrade1 = 0;
            	for (int j = 0; j < itensGrade2List.size(); j++) {
            		double qtItemFisico = ValueUtil.getDoubleValue(item[j + 1].replace(".", ""));
            		qtTtItemFisico += qtItemFisico;
            		ttItemGrade1 += qtItemFisico;
            		itemTotais[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(itemTotais[j + 1]) + ValueUtil.getIntegerValue(qtItemFisico) : ValueUtil.getDoubleValue(itemTotais[j + 1]) + qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            	}
            	item[item.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(ttItemGrade1) : ttItemGrade1, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            }
            // Coluna de Totais
            for (int i = 1; i <= itensGrade2List.size(); i++) {
            	double ttItemGrade2 = 0;
            	for (int j = 0; j < size - 1; j++) {
            		ttItemGrade2 += ValueUtil.getDoubleValue(grid.getItem(j)[i].replace(".", ""));
            	}
            	itemTotais[i] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(ttItemGrade2) : ttItemGrade2, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
    		}
            itemTotais[itemTotais.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            grid.setItens(itemTotais, grid.size() - 1);
		} else {
			String[] itemTotais = new String[2];
			itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
			int size = grid.size();
			for (int i = 0; i < (size - 1); i++) {
				qtTtItemFisico += ValueUtil.getDoubleValue(grid.getItem(i)[1].replace(".", ""));
			}
			itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			grid.setItens(itemTotais, grid.size() - 1);
		}		
	}
	
	private void updateTotaisGrade3() {
		double qtTtItemFisico = 0;
		String[] itemTotais = new String[itensGrade3List.size() + 2];
        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
		int size = grid.size();
        for (int i = 0; i < (size - 1); i++) {
        	String[] item = grid.getItem(i);
        	double ttItemGrade2 = 0;
        	for (int j = 0; j < itensGrade3List.size(); j++) {
				double qtItemFisico = ValueUtil.getDoubleValue(item[j + 1].replace(".", ""));
        		qtTtItemFisico += qtItemFisico;
        		ttItemGrade2 += qtItemFisico;
        		itemTotais[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(itemTotais[j + 1]) + ValueUtil.getIntegerValue(qtItemFisico) : ValueUtil.getDoubleValue(itemTotais[j + 1]) + qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        	}
        	item[item.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(ttItemGrade2) : ttItemGrade2, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        }
        // Coluna de Totais
        for (int i = 1; i <= itensGrade3List.size(); i++) {
        	double ttItemGrade3 = 0;
        	for (int j = 0; j < size - 1; j++) {
				double vlItem = ValueUtil.getDoubleValue(grid.getItem(j)[i].replace(".", ""));
        		ttItemGrade3 += vlItem;
        	}
        	itemTotais[i] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(ttItemGrade3) : ttItemGrade3, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		}
        itemTotais[itemTotais.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        grid.setItens(itemTotais, grid.size() - 1);
	}
	
	private void montaGridGrade1() throws SQLException {
		boolean flagCorAtualZebrado = true;
		if (grid == null) {
			String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemGrade) itensGrade1List.items[0]).cdTipoItemGrade);
			GridColDefinition gridColDefinitionDescricao = new GridColDefinition(dsTipoItemGrade, -55, LEFT);
			GridColDefinition gridColDefinitionEstoque = new GridColDefinition(Messages.ESTOQUE_TOTAL_ESTOQUE, -20,	RIGHT);
			GridColDefinition gridColDefinitionQuantidade = new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -20, RIGHT);
			GridColDefinition[] gridColDefinition;
		if (LavenderePdaConfig.isOcultaEstoque()) {
				gridColDefinition = new GridColDefinition[2];
				gridColDefinition[0] = gridColDefinitionDescricao;
				gridColDefinition[1] = gridColDefinitionQuantidade;
			} else {
				gridColDefinition = new GridColDefinition[3];
				gridColDefinition[0] = gridColDefinitionDescricao;
				gridColDefinition[1] = gridColDefinitionEstoque;
				gridColDefinition[2] = gridColDefinitionQuantidade;
			}
			grid = createGrid(gridColDefinition);
			if (permiteEdicao) {
				edControlGrid = grid.setColumnEditableDoubleNoCalc(LavenderePdaConfig.isOcultaEstoque() ? 1 : 2, true, 9,
						LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface, 0);
				edControlGrid.autoSelect = true;
				edControlGrid.enableTeclado = !LavenderePdaConfig.usaTecladoFixoTelaItemPedido;
				edControlGrid.alignment = CENTER;
			}
		}
		
		grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
		setGridAlignment();
		grid.setGridControllable();
		grid.disableSort = false;
		grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
		if (!LavenderePdaConfig.isOcultaEstoque()) {
			grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 1);
		}
		grid.gridController.setRowBackColor(ColorUtil.componentsBorderColor, itensGrade1List.size());
		
		int rowSize = itensGrade1List.size();
		for (int i = 0; i < rowSize; i++) {
			ItemGrade itemGrade = (ItemGrade) itensGrade1List.items[i];
			grid.add(new String[] { StringUtil.getStringValue(itemGrade.dsItemGrade), "", "" });
			grid.gridController.setRowBackColor(Color.brighter(ColorUtil.componentsBackColorDark,(flagCorAtualZebrado ? 0 : 20)), i);
			if (!LavenderePdaConfig.isOcultaEstoque()) {
				grid.gridController.setRowBackColor(Color.brighter(ColorUtil.componentsBackColorDark,(flagCorAtualZebrado ? 0 : 20)), i);
			}
			grid.gridController.setCelBackColor(Color.brighter(ColorUtil.componentsBackColorDark,(flagCorAtualZebrado ? 20 : 30)), i, 2);
			flagCorAtualZebrado = !flagCorAtualZebrado;
		}
	
		grid.drawHighlight = false;
		grid.gridController.colDisableList.addElement(FIRST_COLUMN);
		if (!LavenderePdaConfig.isOcultaEstoque()) {
			grid.gridController.colDisableList.addElement(COLUMN_ESTOQUE);
		}
		grid.gridController.setRowDisable(grid.size());
		grid.repaintNow();
	}

	private BaseGridEdit createGrid(GridColDefinition[] gridColDefinition) {
		BaseGridEdit createGridEdit = UiUtil.createGridEdit(gridColDefinition);
		createGridEdit.disableKeyboard = LavenderePdaConfig.usaGradeProduto4();
		return createGridEdit;
	}
	
	private void setGridAlignment() {
		grid.aligns[0] = LEFT;
		grid.aligns[1] = CENTER;
		if (!LavenderePdaConfig.isOcultaEstoque()) {
			grid.aligns[2] = CENTER;	
		}
	}

	private void montaGridGrade2() throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto2()) {
			int widthCelulas = fm.stringWidth("00000,00");
			GridColDefinition[] gridColDefinition = new GridColDefinition[itensGrade2List.size() + 2];
			gridColDefinition[0] = new GridColDefinition(" ", -30, LEFT);
			gridColDefinition[gridColDefinition.length - 1] = new GridColDefinition(Messages.LABEL_TOTAL_GRIDEDIT, widthCelulas, RIGHT);
			for (int i = 0; i < itensGrade2List.size(); i++) {
				ItemGrade itemGrade = (ItemGrade) itensGrade2List.items[i];
				gridColDefinition[i + 1] = new GridColDefinition(StringUtil.getStringValue(itemGrade.dsItemGrade), widthCelulas, RIGHT);
			}
			grid = createGrid(gridColDefinition);
			if (permiteEdicao) {	
				for (int i = 0; i < itensGrade2List.size(); i++) {
					edControlGrid = grid.setColumnEditableDoubleNoCalc(i + 1, true, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0	: ValueUtil.doublePrecisionInterface, 0);
					edControlGrid.autoSelect = true;
					edControlGrid.enableTeclado = !LavenderePdaConfig.usaTecladoFixoTelaItemPedido;
				}
			}
			grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
			grid.setGridControllable();
//			grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
//			grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), gridColDefinition.length - 1 );
//			grid.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade1List.size());
			int rowSize = itensGrade1List.size();

			for (int i = 0; i < rowSize; i++) {
				ItemGrade itemGrade = (ItemGrade) itensGrade1List.items[i];
				String[] valueBase = new String[itensGrade2List.size() + 2];
				valueBase[0] = StringUtil.getStringValue(itemGrade.dsItemGrade);
				grid.add(valueBase);
				desabilitaCelulasGradesFaltantes(itemGrade, i);
			}
			grid.drawHighlight = false;
			grid.gridController.colDisableList.addElement(FIRST_COLUMN);
			grid.gridController.colDisableList.addElement(StringUtil.getStringValue(itensGrade2List.size() + 1));
			grid.gridController.setRowDisable(grid.size());
		} else {
			String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(((ItemGrade)itensGrade2List.items[0]).cdTipoItemGrade);
			GridColDefinition[] gridColDefiniton = { new GridColDefinition(dsTipoItemGrade, -75, LEFT), new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -20, RIGHT)};
			grid = createGrid(gridColDefiniton);
			if (permiteEdicao) {
				edControlGrid = grid.setColumnEditableDoubleNoCalc(1, true, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface, 0);
				edControlGrid.autoSelect = true;
				edControlGrid.enableTeclado = !LavenderePdaConfig.usaTecladoFixoTelaItemPedido;
			}
			grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
			grid.setGridControllable();
//			grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
//			grid.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade2List.size());
			int rowSize = itensGrade2List.size();
			for (int i = 0; i < rowSize; i++) {
				ItemGrade itemGrade = (ItemGrade)itensGrade2List.items[i];
				grid.add(new String[]{StringUtil.getStringValue(itemGrade.dsItemGrade), ""});
			}
			grid.drawHighlight = false;
			grid.gridController.colDisableList.addElement(FIRST_COLUMN);
			grid.gridController.setRowDisable(grid.size());
			carregaItemPedidoPrecoPorGrade2();
		}
	}
	
	private void desabilitaCelulasGradesFaltantes(ItemGrade itemGrade1, int row) throws SQLException {
		int size = itensGrade2List.size();
		int disabledColor = Color.brighter(Color.RED, 230);
		ProdutoGrade filter = new ProdutoGrade();		
		filter.cdEmpresa = itemGrade1.cdEmpresa;
		filter.cdRepresentante = itemPedido.cdRepresentante;
		filter.cdItemGrade1 = itemGrade1.cdItemGrade;
		filter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		filter.cdProduto = itemPedido.cdProduto;
		filter.cdTabelaPreco = LavenderePdaConfig.usaGradeProdutoPorTabelaPreco ? itemPedido.cdTabelaPreco : ProdutoGrade.CDTABELAPRECO_PADRAO;
		for (int col = 0; col < size; col++) {
			ItemGrade itemGrade2 = (ItemGrade) itensGrade2List.items[col];
			filter.cdItemGrade2 = itemGrade2.cdItemGrade;
			if (!produtoGradeList.contains(filter)) {
				grid.gridController.setRowColDisable(row, col + 1 );
				grid.gridController.setCelBackColor(disabledColor, row, col + 1);
			}
		}
	}

	private void montaGridGrade3() throws SQLException {
		int widthCelulas = fm.stringWidth("00000,00");
		GridColDefinition[] gridColDefinition = new GridColDefinition[itensGrade3List.size() + 2];
		gridColDefinition[0] = new GridColDefinition(" ", -30, LEFT);
		gridColDefinition[gridColDefinition.length - 1] = new GridColDefinition(Messages.LABEL_TOTAL_GRIDEDIT, widthCelulas, RIGHT);
		for (int i = 0; i < itensGrade3List.size(); i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade3List.items[i];
			gridColDefinition[i + 1] = new GridColDefinition(StringUtil.getStringValue(itemGrade.dsItemGrade), widthCelulas, RIGHT);
		}
		grid = createGrid(gridColDefinition);
		if (LavenderePdaConfig.usaGradeProduto5()) grid.drawRects = true;
		if (permiteEdicao) {
			for (int i = 0; i < itensGrade3List.size(); i++) {
				edControlGrid = grid.setColumnEditableDoubleNoCalc(i + 1, true, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface, LavenderePdaConfig.isConfigGradeProduto() ? 1 : 0);
				edControlGrid.autoSelect = true;
				edControlGrid.enableTeclado = !LavenderePdaConfig.usaTecladoFixoTelaItemPedido;
			}
		}
		grid.captionsBackColor = ColorUtil.gridCaptionsBackColor;
		grid.setGridControllable();
//		grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), 0);
//		grid.gridController.setColBackColor(Color.brighter(Color.DARK, 90), gridColDefinition.length - 1);
//		grid.gridController.setRowBackColor(Color.brighter(Color.DARK, 70), itensGrade2List.size());
		int rowSize = itensGrade2List.size();
		for (int i = 0; i < rowSize; i++) {
			ItemGrade itemGrade = (ItemGrade)itensGrade2List.items[i];
			String[] valueBase = new String[itensGrade3List.size() + 2];
			valueBase[0] = StringUtil.getStringValue(itemGrade.dsItemGrade);
			grid.add(valueBase);
		}
		grid.drawHighlight = false;
		grid.gridController.colDisableList.addElement(FIRST_COLUMN);
		grid.gridController.colDisableList.addElement(StringUtil.getStringValue(itensGrade3List.size() + 1));
		grid.gridController.setRowDisable(grid.size());
		carregaItemPedidoPrecoPorGrade3();
		if (LavenderePdaConfig.usaGradeProduto5()) {
			carregaItemPedidoPrecoAgrupadorGrade();
		}
	}
	
	private void carregaItemPedidoPrecoPorGrade3() throws SQLException {
		if (LavenderePdaConfig.getConfigGradeProduto() != 4) return;
		
		itemPedidoPrecoGradeList.addElementsNotNull(itemPedido.itemPedidoPorGradePrecoList.items);
		int sizeItemGrade3 = itensGrade3List.size();
		for (int i = 0; i < sizeItemGrade3; i++) {
			ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[i];
			int sizeItemGrade2 = itensGrade2List.size();
			for (int j = 0; j < sizeItemGrade2; j++) {
				carregaItemPedidoPrecoPorGrade3(itemGrade3, j);
			}
		}
	}

	private void carregaItemPedidoPrecoPorGrade3(ItemGrade itemGrade3, int j) throws SQLException {
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[j];
				ItemTabelaPreco itemTabelaPrecoGrade = getItemTabelaPreco(itemGrade2, itemGrade3);
				itemTabelaPrecoGrade = itemTabelaPrecoGrade != null ? itemTabelaPrecoGrade : new ItemTabelaPreco();
				ItemTabelaPreco itemTabelaPrecoProdutoOriginal = itemPedido.getProduto().itemTabelaPreco; 
				itemPedido.getProduto().itemTabelaPreco = itemTabelaPrecoGrade;
				ItemPedido novoItemPedido = ItemTabelaPrecoService.getInstance().getItemPedidoGradeComValores(itemPedido.pedido, itemPedido.getProduto(),  itemPedido.cdTabelaPreco, false, null);
				novoItemPedido.naoComparaSeqItem = true;
		if (itemPedidoPrecoGradeList.contains(novoItemPedido)) {
			return;
		}
				setaGrades(novoItemPedido, itemGrade2, itemGrade3);
				setaValoresItemPedidoPrecoGrade(itemTabelaPrecoGrade, itemTabelaPrecoProdutoOriginal, novoItemPedido);
			}
	
	private void carregaItemPedidoPrecoAgrupadorGrade() throws SQLException {
		itemPedidoPrecoGradeList.addElementsNotNull(itemPedido.itemPedidoPorGradePrecoList.items);
		int sizeItemGrade3 = itensGrade3List.size();
		hashProdutoGrade = ProdutoService.getInstance().montaHashProduto(itemPedido.cdEmpresa, itemPedido.cdItemGrade1, itemPedido.getProduto().getDsAgrupadorGrade(), itemPedido.pedido.cdTabelaPreco);
		for (int i = 0; i < sizeItemGrade3; i++) {
			ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[i];
			int sizeItemGrade2 = itensGrade2List.size();
			for (int j = 0; j < sizeItemGrade2; j++) {
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[j];
				Produto produto = hashProdutoGrade.get(ProdutoGrade.getGradeKey(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade));
				if (produto == null) continue;
				ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(itemPedido.cdTabelaPreco, produto.cdProduto, itemPedido.cdUfClientePedido, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
				produto.itemTabelaPreco = itemTabelaPreco;
				ItemPedido novoItemPedido = ItemTabelaPrecoService.getInstance().getItemPedidoGradeComValores(itemPedido.pedido, produto,  itemPedido.cdTabelaPreco, false, null);
				novoItemPedido.dsProduto = produto.dsProduto;
				novoItemPedido.naoComparaSeqItem = true;
				int index = itemPedidoPrecoGradeList.indexOf(novoItemPedido);
				if (index >= 0) {
					ItemPedido itemPedidoPorGrade = (ItemPedido)itemPedidoPrecoGradeList.items[index];
					setaGrades(itemPedidoPorGrade, itemGrade2, itemGrade3);
					continue;
				}
				setaGrades(novoItemPedido, itemGrade2, itemGrade3);
				setaValoresItemPedidoPrecoGrade(itemTabelaPreco, itemTabelaPreco, novoItemPedido);
			}
		}
		setupShowControlAction(hashProdutoGrade);
	}

	private void setupShowControlAction(Map<String, Produto> hashProduto) {
		GridActionShowControl action = new GridActionShowControl() {
			
			@Override
			public void apply(int row, int col, BaseEdit ed) {
				if (!(ed instanceof KeyEditNumberFrac)) return;
				ItemGrade itemGrade2 = (ItemGrade) itensGrade2List.items[row];
				ItemGrade itemGrade3 = (ItemGrade) itensGrade3List.items[col - GRADE3_OFFSET];
				Produto produto = hashProduto.get(ProdutoGrade.getGradeKey(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade));
				if (produto != null) {
					double nuMultiploEspecial = produto.nuMultiploEspecial > 0 ? produto.nuMultiploEspecial : 1;
					KeyEditNumberFrac keyEdit = (KeyEditNumberFrac) ed;
					keyEdit.setStep(nuMultiploEspecial);
				}
				
			}
		};
		grid.setShowControlAction(action);
	}

	private void setaValoresItemPedidoPrecoGrade(ItemTabelaPreco itemTabelaPrecoGrade, ItemTabelaPreco itemTabelaPrecoProdutoOriginal, ItemPedido novoItemPedido) throws SQLException {
		novoItemPedido.isIgnoraMensagemEstoqueNegativo = itemPedido.isIgnoraMensagemEstoqueNegativo;
		novoItemPedido.flTipoItemPedido = itemPedido.flTipoItemPedido;
		novoItemPedido.nuSeqItemPedido = -1;
		itemTabelaPrecoGrade.vlUnitario = novoItemPedido.vlBaseItemTabelaPreco;
		itemPedido.getProduto().itemTabelaPreco = itemTabelaPrecoProdutoOriginal; 
		itemPedidoPrecoGradeList.addElement(novoItemPedido);
	}
	
	private void carregaItemPedidoPrecoPorGrade2() throws SQLException  {
		if (LavenderePdaConfig.getConfigGradeProduto() != 4 || ValueUtil.isEmpty(itensGrade2List)) return;
		itemPedidoPrecoGradeList.addElementsNotNull(itemPedido.itemPedidoPorGradePrecoList.items);
		
		int sizeItemGrade2 = itensGrade2List.size();
		for (int i = 0; i < sizeItemGrade2; i++) {
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			ItemTabelaPreco itemTabelaPrecoGrade = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(itemPedido.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido, itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
			ItemTabelaPreco itemTabelaPrecoProdutoOriginal = itemPedido.getProduto().itemTabelaPreco; 
			itemPedido.getProduto().itemTabelaPreco = itemTabelaPrecoGrade;
			ItemPedido novoItemPedido = ItemTabelaPrecoService.getInstance().getItemPedidoGradeComValores(itemPedido.pedido, itemPedido.getProduto(),  itemPedido.cdTabelaPreco, false, null);
			if (itemPedidoPrecoGradeList.contains(novoItemPedido)) continue;
			setaGrades(novoItemPedido, itemGrade2, null);
			setaValoresItemPedidoPrecoGrade(itemTabelaPrecoGrade, itemTabelaPrecoProdutoOriginal, novoItemPedido);
		}
		
	}
	
	private void setaGrades(ItemPedido novoItemPedido, ItemGrade itemGrade2, ItemGrade itemGrade3) {
		novoItemPedido.itemGrade1 = itemPedido.itemGrade1;
		novoItemPedido.cdItemGrade1 = itemPedido.cdItemGrade1;
		if (itemGrade2!= null) {
			novoItemPedido.itemGrade2 = itemGrade2;
			novoItemPedido.cdItemGrade2 = itemGrade2.cdItemGrade;
		}
		if (itemGrade3!= null) {
			novoItemPedido.cdItemGrade3 = itemGrade3.cdItemGrade;
		} else {
			novoItemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		}
	}
	
	protected void btPrecosClick(boolean permiteEventoClickNaGrade, boolean fromCadItemPedido) throws SQLException {
		if (!fromCadItemPedido) {
			setaItemPedidoPorGradePreco();
		} else {
			loadPoliticaComercial();
		}
		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.informaQuantidadePrimeiroMultiploNaoAtingido) {
			validaMultiploEspecial();
		}
		ListPrecoGradeWindow listPrecoGradeWindow = new ListPrecoGradeWindow(itemPedido, itensGrade2List, itensGrade3List, itemPedidoPrecoGradeList, permiteEventoClickNaGrade, hashProdutoGrade);
		UiUtil.unpopProcessingMessage();
		listPrecoGradeWindow.popup();
		if (permiteEventoClickNaGrade && !listPrecoGradeWindow.closedByBtFechar) {
			setaPosicaoDeAcordocomPrecoSelecionado(listPrecoGradeWindow.precoGrid, listPrecoGradeWindow.precoList);
		}
	}
	
	private void loadPoliticaComercial() throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto5()) {
			int size = itemPedidoPrecoGradeList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPrecoGradeList.items[i];
				if (itemPedidoPorGradePreco.politicaComercial == null && itemPedidoPorGradePreco.qtItemFisico > 0d) {
					ItemPedidoService.getInstance().loadPoliticaComercial(itemPedidoPorGradePreco, itemPedido.pedido);
				}
			}
		}
	}
	
	private void btEstoqueClick() throws SQLException {
		String cdLocalEstoque = null;
		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.informaQuantidadePrimeiroMultiploNaoAtingido) {
			validaMultiploEspecial();
		}
		setaItemPedidoPorGradePreco();
		if (LavenderePdaConfig.usaLocalEstoquePorCentroCusto() && itemPedido.pedido.getCentroCusto() != null) {
			cdLocalEstoque = itemPedido.pedido.getCentroCusto().cdLocalEstoque;
		}
		ListEstoqueGradeWindow listEstoqueGradeWindow = new ListEstoqueGradeWindow(itemPedido, itemPedidoPrecoGradeList, !LavenderePdaConfig.usaGradeProduto5(), cdLocalEstoque);
		if (LavenderePdaConfig.usaGradeProduto5()) {
			listEstoqueGradeWindow.setHashProdutoGrade(hashProdutoGrade);
		}
		listEstoqueGradeWindow.popup();
		if (!listEstoqueGradeWindow.closedByBtFechar) {
			setaPosicaoDeAcordocomPrecoSelecionado(listEstoqueGradeWindow.precoGrid, listEstoqueGradeWindow.precoList);
		}
	}

	private void setaPosicaoDeAcordocomPrecoSelecionado(int[] precoGrid, String[]precoList) {
		if (precoGrid != null && precoGrid[0] >= 0 && precoGrid[1] >= 0){
			grid.setSelectedIndex(precoGrid[0]);
			grid.exibeControleGrid(precoGrid[0], precoGrid[1]);
			EditNumberFrac lastShownControl = (EditNumberFrac) grid.getLastShownControl();
			if (! abrePopupInfosComplementares(lastShownControl, precoGrid) && lastShownControl != null) {
				lastShownControl.popupKCC();
			}
			return;
		} 
		if (precoList == null) return;
		int sizeGrade3 = itensGrade3List.size();
		String dsGrade2Selecionada = precoList[0];
		String dsGrade3Selecionada = precoList[1];
		for (int i = 0; i < sizeGrade3; i++) {
			ItemGrade itemGrade3 = (ItemGrade) itensGrade3List.items[i];
			if (ValueUtil.valueEquals(dsGrade3Selecionada, itemGrade3.dsItemGrade)) {
				int sizeGrade2 = itensGrade2List.size();
				for (int j = 0; j < sizeGrade2; j++) {
					ItemGrade itemGrade2 = (ItemGrade) itensGrade2List.items[j];
					if (ValueUtil.valueEquals(dsGrade2Selecionada, itemGrade2.dsItemGrade)) {
						grid.setSelectedIndex(j);
						grid.exibeControleGrid(j,i+1);
						EditNumberFrac lastShownControl = (EditNumberFrac) grid.getLastShownControl();
						if (! abrePopupInfosComplementares(lastShownControl, new int[] {j, i + 1}) && lastShownControl != null) {
							lastShownControl.popupKCC();
						}
						return;
					}
				}
			}
		}
	}
	
	private void carregaGridGrade1() throws SQLException {
		boolean corAtualZebrado = true;
		double qtTtItemFisico = 0;
		double qtTotalEstoque = 0;
		int posQtd = LavenderePdaConfig.isOcultaEstoque() ? 1 : 2;
		String[] itemTotais = new String[3];
		itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
		grid.totalizadorLabel = itemTotais[0];
		int size = grid.size();
		for (int i = 0; i < size; i++) {
			String[] item = grid.getItem(i);
			ItemGrade itemGrade1 = (ItemGrade)itensGrade1List.items[i];
			double qtEstoque = 0d;
			grifaGrade1(i, itemGrade1.cdItemGrade, corAtualZebrado);
			corAtualZebrado = !corAtualZebrado;
			if (!LavenderePdaConfig.isOcultaEstoque()) {
				if (LavenderePdaConfig.isGradeProdutoModoLista()) {
					qtEstoque = EstoqueService.getInstance().getQtEstoqueNivel3(itemPedido.cdProduto, itemPedido.cdItemGrade1, itemPedido.cdItemGrade2, itemGrade1.cdItemGrade);
				} else {
					qtEstoque = EstoqueService.getInstance().getQtEstoqueNivel1(itemPedido.cdProduto, itemGrade1.cdItemGrade);
				}
				qtTotalEstoque += qtEstoque;
				itemGrade1.qtEstoqueItem = qtEstoque; 
				item[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtEstoque) : qtEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
				if (LavenderePdaConfig.usaOrdenacaoEstoqueNaGradeProduto) {
					SortUtil.qsortDouble(itensGrade1List.items, 0, itensGrade1List.size() - 1, true);
				}
			}
			double qtItemFisico = 0d;
			if (LavenderePdaConfig.isGradeProdutoModoLista()) {
				qtItemFisico = getQtItemByItemPedido(itemPedido.cdItemGrade1, itemPedido.cdItemGrade2, itemGrade1.cdItemGrade);
			} else if (LavenderePdaConfig.usaGradeProduto4()){
				ItemPedido itemPedidoPorGrade = buscaItemPedidoPorGrade(itemGrade1.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
				qtItemFisico = itemPedidoPorGrade != null ? itemPedidoPorGrade.getQtItemFisico() : 0;
			} else {
				qtItemFisico = getQtItemByItemPedido(itemGrade1.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
			}
			qtTtItemFisico += qtItemFisico;
			if (qtItemFisico == 0) {
				item[posQtd] = "";
			} else {
				item[posQtd] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtItemFisico) : qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			}
		}
		if (!LavenderePdaConfig.isOcultaEstoque() && !LavenderePdaConfig.isOcultaTotalizadoresDeEstoque()) {
			itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? ValueUtil.getIntegerValue(qtTotalEstoque) : qtTotalEstoque, LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? 0 : ValueUtil.doublePrecisionInterface);
		}
		itemTotais[posQtd] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		grid.add(itemTotais);
		atualizaListaGradeAuxiliar();
	}

	private void grifaGrade1(int linha, String cdItemGrade1, boolean corAtualZebrado) throws SQLException {
		if (LavenderePdaConfig.getConfigGradeProduto() != ItemGrade.GRADE_NIVEL_2) return;
		double qtdEstoque = EstoqueService.getInstance().getQtEstoqueNivel1(itemPedido.cdProduto, cdItemGrade1);
		grid.gridController.setCelBackColor(defineCelColorByEstoque(corAtualZebrado, qtdEstoque), linha, 0);
		if (!LavenderePdaConfig.isOcultaEstoque()) {
			grid.gridController.setCelBackColor(defineCelColorByEstoque(corAtualZebrado, qtdEstoque), linha, 1);
		}
		
	}
	
	private void reGrifaGrade1(int linha, double qtdEstoque, boolean corAtualZebrado) throws SQLException {
		if (LavenderePdaConfig.getConfigGradeProduto() != ItemGrade.GRADE_NIVEL_2) return;
		grid.gridController.setCelBackColor(defineCelColorByEstoque(corAtualZebrado, qtdEstoque), linha, 0);
		if (!LavenderePdaConfig.isOcultaEstoque()) {
			grid.gridController.setCelBackColor(defineCelColorByEstoque(corAtualZebrado, qtdEstoque), linha, 1);
		}
		
	}

	private int defineCelColorByEstoque(boolean corAtualZebrado, double qtdEstoque) {
		if (qtdEstoque > 0) {
			return corAtualZebrado ? Color.darker(LavendereColorUtil.componentsBackColorDark, 30) : LavendereColorUtil.componentsBackColorDark;
		} else {
			return corAtualZebrado ? Color.darker(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK, 30) : LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK;
		}
	}
	
	private void carregaGridGrade2() {
		double qtTtItemFisico = 0;
		if (LavenderePdaConfig.usaGradeProduto2()) {
			String[] itemTotais = new String[itensGrade2List.size() + 2];
            itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
            grid.totalizadorLabel = itemTotais[0];
    		int size = grid.size();
            for (int i = 0; i < size; i++) {
            	String[] item = grid.getItem(i);
            	ItemGrade itemGrade1 = (ItemGrade)itensGrade1List.items[i];
            	double ttItemGrade1 = 0;
            	for (int j = 0; j < itensGrade2List.size(); j++) {
            		ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[j];
            		double qtItemFisico = getQtItemByItemPedido(itemGrade1.cdItemGrade, itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
            		if (qtItemFisico == 0) {
            			item[j + 1] = "";
            		} else {
            			item[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtItemFisico) : qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            		}
            		qtTtItemFisico += qtItemFisico;
            		ttItemGrade1 += qtItemFisico;
            		if (qtItemFisico == 0) {
            			itemTotais[j + 1] = "";
            		} else {
            			itemTotais[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(itemTotais[j + 1]) + ValueUtil.getIntegerValue(qtItemFisico) : ValueUtil.getDoubleValue(itemTotais[j + 1]) + qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            		}
            	}
            	item[item.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(ttItemGrade1) : ttItemGrade1, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            }
            // Coluna de Totais
            itemTotais[itemTotais.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
            grid.add(itemTotais);
		} else {
			String[] itemTotais = new String[2];
			itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
			grid.totalizadorLabel = itemTotais[0];
			int size = grid.size();
			for (int i = 0; i < size; i++) {
				String[] item = grid.getItem(i);
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
				double qtItemFisico = 0;
				if (LavenderePdaConfig.usaGradeProduto4()) {
					ItemPedido itemPedidoPorGrade = buscaItemPedidoPorGrade(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
					qtItemFisico = itemPedidoPorGrade.getQtItemFisico();
				} else {
					qtItemFisico = getQtItemByItemPedido(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
				}
				qtTtItemFisico += qtItemFisico;
				item[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtItemFisico) : qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			}
			itemTotais[1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			grid.add(itemTotais);
		}
	}
	
	private void carregaGridGrade3() throws SQLException {
		double qtTtItemFisico = 0;
		String[] itemTotais = new String[itensGrade3List.size() + 2];
        itemTotais[0] = Messages.LABEL_TOTAL_GRIDEDIT;
        grid.totalizadorLabel = itemTotais[0];
		int size = grid.size();
		Map<String, ProdutoGrade> hashProdutoGrade = LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade() ? ProdutoGradeService.getInstance().montaHashProdutoGradeAgrupador(itemPedido.cdTabelaPreco, itemPedido.cdItemGrade1, itemPedido.getProduto().getDsAgrupadorGrade()) : null;
		Map<String, Estoque> hashEstoqueGrade = LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade() && LavenderePdaConfig.bloquearVendaProdutoSemEstoque ? EstoqueService.getInstance().montaHashEstoqueAgrupadorGrade(itemPedido.getProduto().getDsAgrupadorGrade(), itemPedido.cdItemGrade1, itemPedido.pedido.getCdLocalEstoque()) : null;
		Map<String, Image> hashMarcadores = new HashMap<>();
		if (LavenderePdaConfig.apresentaMarcadorGrade) ProdutoService.getInstance().loadImagesMarcadores(hashMarcadores, UiUtil.getControlPreferredHeight() / 3);
		ItemPedido itemPedidoPorGrade = null;
		Vector listMarcadores = new Vector();
		for (int i = 0; i < size; i++) {
        	String[] item = grid.getItem(i);
        	ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
        	double ttItemGrade2 = 0;
        	Image[] cellImages = LavenderePdaConfig.apresentaMarcadorGrade ? new Image[grid.captions.length] : null;
        	for (int j = 0; j < itensGrade3List.size(); j++) {
        		ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[j];
        		double qtItemFisico = 0;
        		if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
        			itemPedidoPorGrade = buscaItemPedidoPorGrade(hashProdutoGrade, itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
        			qtItemFisico = itemPedidoPorGrade != null ? itemPedidoPorGrade.getQtItemFisico() : 0;
        		} else {
        			qtItemFisico = getQtItemByItemPedido(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
        		}
        		if (qtItemFisico == 0) {
        			item[j + 1] = "";
        		} else {
        			item[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtItemFisico) : qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        		}
        		qtTtItemFisico += qtItemFisico;
        		ttItemGrade2 += qtItemFisico;
        		if (qtItemFisico == 0) {
        			itemTotais[j + 1] = "";
        		} else {
        			itemTotais[j + 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(itemTotais[j + 1]) + ValueUtil.getIntegerValue(qtItemFisico) : ValueUtil.getDoubleValue(itemTotais[j + 1]) + qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        		}
        		ItemTabelaPreco itemTabelaPrecoGrade = getItemTabelaPreco(itemGrade2, itemGrade3);
        		if (itemTabelaPrecoGrade == null || itemTabelaPrecoGrade.vlUnitario <= 0) {
        			int color = i % 2 == 1 ? Color.darker(LavendereColorUtil.COR_FUNDO_CELULA_GRADE_SEM_PRECO, 20) : LavendereColorUtil.COR_FUNDO_CELULA_GRADE_SEM_PRECO;
        			grid.gridController.setRowColDisable(i, j+1, true);
        			grid.gridController.setCelBackColor(color, i , j + 1);
        		}
        		if (LavenderePdaConfig.usaGradeProduto5()) {
        			disableCellProdutoSemGrade(hashProdutoGrade.keySet(), hashEstoqueGrade, itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade, i, j);
        			if (itemPedidoPorGrade != null) {
        				if (itemPedidoPorGrade.isKitTipo3()) {
        					grid.gridController.setRowColDisable(i, j+1, true);
        				}
        				if (LavenderePdaConfig.apresentaMarcadorGrade) {
        					ProdutoGrade produto = hashProdutoGrade.get(ProdutoGrade.getGradeKey(itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade));
        					setImageMarcador(cellImages, hashMarcadores, produto, j + 1);
        				}
        			}
        		}
        	}
        	if (LavenderePdaConfig.apresentaMarcadorGrade) {
        		listMarcadores.addElement(cellImages);
        	}
        	item[item.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(ttItemGrade2) : ttItemGrade2, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        }
		if (LavenderePdaConfig.apresentaMarcadorGrade) {
			grid.setListCellImages(listMarcadores);
			grid.keepImageAndText = true;
		}
        // Coluna de Totais
        itemTotais[itemTotais.length - 1] = StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(qtTtItemFisico) : qtTtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        grid.add(itemTotais);
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

	private ItemTabelaPreco getItemTabelaPreco(ItemGrade itemGrade2, ItemGrade itemGrade3) throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto5()) {
			return ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(itemPedido.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
		}
		return ItemTabelaPrecoService.getInstance().getItemTabelaPrecoComGrade(itemPedido.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido, itemPedido.cdItemGrade1, itemGrade2.cdItemGrade, itemGrade3.cdItemGrade);
	}
	
	private void disableCellProdutoSemGrade(Set<String> setProdutoGrade, Map<String, Estoque> mapEstoqueGrade, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, int i, int j) {
		String gradeKey = ProdutoGrade.getGradeKey(cdItemGrade1, cdItemGrade2, cdItemGrade3);
		boolean produtoSemEstoque = false;
		if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
			Estoque estoque = mapEstoqueGrade.get(gradeKey);
			produtoSemEstoque = estoque == null || estoque.qtEstoque <= 0d;
			Produto produto = this.hashProdutoGrade.get(gradeKey);
			if (produto != null) produtoSemEstoque &= !produto.isPermiteEstoqueNegativo();
		}
		int color = i % 2 == 1 ? Color.darker(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK, 20) : LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK;
		if (!setProdutoGrade.contains(gradeKey) || !hashProdutoGrade.containsKey(gradeKey) || produtoSemEstoque) {
			grid.gridController.setRowColDisable(i, j+1, true);
			if (produtoSemEstoque) grid.gridController.setCelBackColor(color, i, j+1);
			else grid.gridController.setCelBackColor(ColorUtil.componentsBackColorDark, i, j+1);
		}
	}
	
	private void setItemPedidoGrade3List() throws SQLException {
		Vector itemPedidoGradeList = new Vector();
		for (int i = 0; i < itensGrade2List.size(); i++) {
			ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
			String[] rowGrid = grid.getItem(i);
			for (int j = 0; j < itensGrade3List.size(); j++) {
				double qtInserida = ValueUtil.getDoubleValue(rowGrid[j + 1].replace(".", ""));
				if (qtInserida > 0) {
					ItemGrade itemGrade3 = (ItemGrade)itensGrade3List.items[j];
					ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
					itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
					itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
					itemPedidoGrade.flOrigemPedido = itemPedido.flOrigemPedido;
					itemPedidoGrade.nuPedido = itemPedido.nuPedido;
					itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
					itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
					itemPedidoGrade.cdProduto = itemPedido.cdProduto;
					itemPedidoGrade.cdItemGrade1 = itemPedido.cdItemGrade1;
					itemPedidoGrade.cdItemGrade2 = itemGrade2.cdItemGrade;
					itemPedidoGrade.cdItemGrade3 = itemGrade3.cdItemGrade;
					itemPedidoGrade.qtItemFisico = qtInserida;
					itemPedidoGrade.itemGrade3 = itemGrade3;
					itemPedidoGrade.itemGrade2 = itemGrade2;
					itemPedidoGradeList.addElement(itemPedidoGrade);
					if (!isConversaoUnidadeCompleta(qtInserida)) {
						if (ValueUtil.isNotEmpty(itensComProblemas.toString())) {
							itensComProblemas.append(", ");
						}
						itensComProblemas.append(StringUtil.getStringValue(itemGrade2.dsItemGrade)).append(" - ").append(StringUtil.getStringValue(itemGrade3.dsItemGrade));
						itensGradeComProblemaList.addElement(itemPedidoGrade);
					}
				}
			}
		}
		itemPedidoGrade3List = itemPedidoGradeList;
	}
	
	private void setItemPedidoGrade2List() throws SQLException {
		Vector itemPedidoGradeList = new Vector();
		if (LavenderePdaConfig.usaGradeProduto2()) {
			for (int i = 0; i < itensGrade1List.size(); i++) {
				ItemGrade itemGrade1 = (ItemGrade)itensGrade1List.items[i];
				String[] rowGrid = grid.getItem(i);
				for (int j = 0; j < itensGrade2List.size(); j++) {
					double qtInserida = ValueUtil.getDoubleValue(rowGrid[j + 1].replace(".", ""));
					if (qtInserida > 0) {
						ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[j];	
						ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
						itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
						itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
						itemPedidoGrade.flOrigemPedido = itemPedido.flOrigemPedido;
						itemPedidoGrade.nuPedido = itemPedido.nuPedido;
						itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
						itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
						itemPedidoGrade.cdProduto = itemPedido.cdProduto;
						itemPedidoGrade.cdItemGrade1 = itemGrade1.cdItemGrade;
						itemPedidoGrade.cdItemGrade2 = itemGrade2.cdItemGrade;
						itemPedidoGrade.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						itemPedidoGrade.qtItemFisico = qtInserida;
						itemPedidoGrade.itemGrade2 = itemGrade2;
						itemPedidoGrade.itemGrade1 = itemGrade1;
						itemPedidoGradeList.addElement(itemPedidoGrade);
						if (!isConversaoUnidadeCompleta(qtInserida)) {
							if (ValueUtil.isNotEmpty(itensComProblemas.toString())) {
								itensComProblemas.append(", ");
							}
							itensComProblemas.append(StringUtil.getStringValue(itemGrade1.dsItemGrade)).append(" - ").append(StringUtil.getStringValue(itemGrade2.dsItemGrade));
							itensGradeComProblemaList.addElement(itemPedidoGrade);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < itensGrade2List.size(); i++) {
				String[] rowGrid = grid.getItem(i);
				ItemGrade itemGrade2 = (ItemGrade)itensGrade2List.items[i];
				double qtInserida = ValueUtil.getDoubleValue(rowGrid[1].replace(".", ""));
				if (qtInserida > 0) {
					ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
					itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
					itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
					itemPedidoGrade.flOrigemPedido = itemPedido.flOrigemPedido;
					itemPedidoGrade.nuPedido = itemPedido.nuPedido;
					itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
					itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
					itemPedidoGrade.cdProduto = itemPedido.cdProduto;
					itemPedidoGrade.cdItemGrade1 = itemPedido.cdItemGrade1;
					itemPedidoGrade.qtItemFisico = qtInserida;
					itemPedidoGrade.cdItemGrade2 = itemGrade2.cdItemGrade;
					itemPedidoGrade.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
					itemPedidoGrade.itemGrade2 = itemGrade2;
					itemPedidoGradeList.addElement(itemPedidoGrade);
				}
			}
		}
		itemPedidoGrade2List = itemPedidoGradeList;
	}
	
	private void setItemPedidoGrade1List() throws SQLException {
		Vector itemPedidoGradeList = new Vector();
		for (int i = 0; i < itensGrade1ListAuxiliar.size(); i++) {
        	ItemGrade itemGrade1 = (ItemGrade)itensGrade1ListAuxiliar.items[i];
        	double qtInserida = itemGrade1.qtItemGrade;
        	if (qtInserida > 0) {
        		ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
				itemPedidoGrade.cdEmpresa = itemPedido.cdEmpresa;
				itemPedidoGrade.cdRepresentante = itemPedido.cdRepresentante;
				itemPedidoGrade.flOrigemPedido = itemPedido.flOrigemPedido;
				itemPedidoGrade.nuPedido = itemPedido.nuPedido;
				itemPedidoGrade.flTipoItemPedido = itemPedido.flTipoItemPedido;
				itemPedidoGrade.nuSeqProduto = itemPedido.nuSeqProduto;
				itemPedidoGrade.cdProduto = itemPedido.cdProduto;
				itemPedidoGrade.qtItemFisico = qtInserida;
				if (LavenderePdaConfig.isGradeProdutoModoLista()) {
					itemPedidoGrade.cdItemGrade1 = itemPedido.cdItemGrade1;
					itemPedidoGrade.cdItemGrade2 = itemPedido.cdItemGrade2;
					itemPedidoGrade.cdItemGrade3 = itemGrade1.cdItemGrade;
					itemPedidoGrade.itemGrade1 = itemPedido.itemGrade1;
					itemPedidoGrade.itemGrade2 = itemPedido.itemGrade2;
					itemPedidoGrade.itemGrade3 = itemGrade1;
				} else {
					itemPedidoGrade.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
					itemPedidoGrade.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
					itemPedidoGrade.cdItemGrade1 = itemGrade1.cdItemGrade;
					itemPedidoGrade.itemGrade1 = itemGrade1;
				}
				itemPedidoGradeList.addElement(itemPedidoGrade);
				if (!isConversaoUnidadeCompleta(qtInserida)) {
					if (ValueUtil.isNotEmpty(itensComProblemas.toString())) {
						itensComProblemas.append(", ");
					}
					itensComProblemas.append(StringUtil.getStringValue(itemGrade1.dsItemGrade));
					itensGradeComProblemaList.addElement(itemPedidoGrade);
				}
        	}
		}
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			itemPedidoGrade3List = itemPedidoGradeList;
		} else {
			itemPedidoGrade1List = itemPedidoGradeList;
		}
	}
	
	public boolean isConversaoUnidadeCompleta(double qtInserida) throws SQLException {
		if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.avisaConversaoUnidades) {
			Produto produtoItem = itemPedido.getProduto();
			if (produtoItem != null && (!(ValueUtil.VALOR_NAO.equals(produtoItem.flConsisteConversaoUnidade)))) {
				double nuConversaoUnidade = produtoItem.nuConversaoUnidadesMedida;
				if (nuConversaoUnidade != 0) {
					return ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuConversaoUnidade, qtInserida);
				}
			}
		}
		return true;
	}
	
	public void arredondaConversaoUnidade(ItemPedidoGrade itemPedidoGrade) throws SQLException {
		Produto produtoItem = itemPedido.getProduto();
		if (produtoItem != null && (!(ValueUtil.VALOR_NAO.equals(produtoItem.flConsisteConversaoUnidade)))) {
			double nuConversaoUnidade = produtoItem.nuConversaoUnidadesMedida;
			if (nuConversaoUnidade != 0) {
				if (!ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuConversaoUnidade, itemPedidoGrade.qtItemFisico)) {
					itemPedidoGrade.qtItemFisico = ConversaoUnidadeService.getInstance().arredondaObedecendoConversaoUnidade(nuConversaoUnidade, itemPedidoGrade.qtItemFisico);
				}
			}
		}
	}
	
	public boolean isResolveProblemasArredondamento() throws SQLException {
		int value = !LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.doublePrecisionInterface : 0;
		double nuConversaoUnidade = itemPedido.getProduto().nuConversaoUnidadesMedida;
		String param1 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade)), value);
		String param2 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(itemPedido.getQtItemFisico() / nuConversaoUnidade, 0)) * nuConversaoUnidade, value);
		String param3 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(itemPedido.getQtItemFisico() / nuConversaoUnidade, 0)) * nuConversaoUnidade + nuConversaoUnidade, value);
		if (ValueUtil.getIntegerValue(param2) == 0) {
			param2 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade)));
			param3 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade + nuConversaoUnidade)));
		}
		if (LavenderePdaConfig.isPermiteArrendondarCasoItemNaoAlcanceConversao()) {
			if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ITEMGRADE_ARREDONDADO,  new Object[] { itensComProblemas.toString(), param1, param2, param3}))) {
				for (int i = 0; i < itensGradeComProblemaList.size(); i++) {
					ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itensGradeComProblemaList.items[i];
					arredondaConversaoUnidade(itemPedidoGrade);
				}
				return true;
			} 
			return false;
		}
		UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_GRADE_COM_PROBLEMA,  new Object[] { itensComProblemas.toString(), param1, param2, param3}));
		return false;
	}
	
	private void montaGradeFechadaVectors() throws SQLException {
		DescPromocionalGrade filter = new DescPromocionalGrade();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdRepresentante = itemPedido.cdRepresentante;
		filter.descPromocional = itemPedido.getDescPromocional();
		filter.itemGradeList = itensGrade2List;
		descPromocionalGradeList = DescPromocionalGradeService.getInstance().findAllByExample(filter);
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		cadItemPedidoForm.dontRefreshItemsOnNextExhibition = true;
		super.btFecharClick();
		windowClosed = true;
		itemPedido.itemPedidoPorGradePrecoList.removeAllElements();
		itemPedido.itemPedidoPorGradePrecoList.addElementsNotNull(itemPedidoPrecoGradeOriginalList.items);
	}

	private void montaFiltroGrade() {
		boolean filtrarPorCodigoBarras = LavenderePdaConfig.isUsaFiltroGradeProdutoPorCamera() && LavenderePdaConfig.usaCameraParaLeituraCdBarras();
		if (LavenderePdaConfig.isUsaFiltroGradeProduto() && !isGradeNivel2()) {
			int espacoComSemBotao = (filtrarPorCodigoBarras ? UiUtil.getButtonPreferredHeight() + 30 : 0);
			UiUtil.add(this, edFiltro, getLeft(), getNextY(), getWFill() - espacoComSemBotao);
			if (filtrarPorCodigoBarras) {
				btLeitorCamera.setImage(UiUtil.getColorfulImage("images/barcode.png", UiUtil.getBarMenuPreferredHeight() / 9 * 4, UiUtil.getBarMenuPreferredHeight() / 9 * 4, ColorUtil.baseForeColorSystem));
				UiUtil.add(this, btLeitorCamera, RIGHT, SAME, UiUtil.getButtonPreferredHeight() + 30, edFiltro.getHeight());
			}
		}
	}

	private boolean isGradeNivel2() {
		for (int i = 0; i < produtoGradeList.size(); i++) {
			ProdutoGrade prod = (ProdutoGrade) produtoGradeList.items[i];
			if (!ValueUtil.isEmpty(prod.cdItemGrade2)) {
				if (!prod.cdItemGrade2.equals("0")) {
					return true;
				}
			}
		}
		return false;
	}

	private void btFiltrarClick(String filter) throws SQLException {
		if (dsFiltro != filter) {
			dsFiltro = filter;
		}
		updateQtInserido();
		grid.removeAllElements();
		montaGridGradeProduto();
		carregaGrid();
		updateTotaisGradeProduto();
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			dsFiltro = null;
			edFiltro.clear();
		}
	}

	private boolean verificaFiltroGrade(ItemGrade item, ProdutoGrade produto) {
		return ValueUtil.isNotEmpty(dsFiltro) && (item.dsItemGrade.toUpperCase().contains(dsFiltro.toUpperCase()) 
				|| (produto.nuCodigoBarras != null ? produto.nuCodigoBarras.startsWith(dsFiltro.toUpperCase()) : false));
	}

	private void updateQtInserido() {
		if (ValueUtil.isNotEmpty(itensGrade1List)) {
			int size = grid.size();
			int sizeDifference = LavenderePdaConfig.isOcultaEstoque() ? 1 : 0;
			for (int i = 0; i < (size - 1); i++) {
				((ItemGrade) itensGrade1List.items[i]).qtItemGrade = ValueUtil
						.getDoubleValue(grid.getItem(i)[2 - sizeDifference].replace(".", ""));
			}
		}
		atualizaListaGradeAuxiliar();
	}

	protected void realizaLeituraCamera() throws SQLException {
		dsFiltro = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, "");
		if (dsFiltro != null) {
			btFiltrarClick(dsFiltro);
		}
	}

	private void iniciaListaGradeAuxiliar() {
		if (itensGrade1ListAuxiliar == null) {
			itensGrade1ListAuxiliar = new Vector(itensGrade1List.items);
			itensGrade1ListAuxiliar.setSize(itensGrade1List.size());
		}
	}

	private void atualizaListaGradeAuxiliar() {
		boolean sameSize = itensGrade1List.size() == itensGrade1ListAuxiliar.size();
		for (int i = 0; i < itensGrade1List.size(); i++) {
			ItemGrade item1 = (ItemGrade) itensGrade1List.items[i];
			if (sameSize) {
				ItemGrade item2 = (ItemGrade) itensGrade1ListAuxiliar.items[i];
				if (item1.equals(item2)) {
					item2.qtItemGrade = item1.qtItemGrade;
					break;
				}
			} else {
				for (int j = 0; j < itensGrade1ListAuxiliar.size(); j++) {
					ItemGrade item2 = (ItemGrade) itensGrade1ListAuxiliar.items[j];
					if (item1.equals(item2)) {
						item2.qtItemGrade = item1.qtItemGrade;
						break;
					}
				}
			}
		}
	}
}
