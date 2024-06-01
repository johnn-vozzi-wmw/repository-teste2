package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.GridEditNew;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.service.AreaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.sys.Convert;
import totalcross.sys.SpecialKeys;
import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.Window;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.image.ImageException;
import totalcross.util.IntVector;
import totalcross.util.Vector;

public class CadItemPedidoDesktopForm extends BaseCrudCadForm {

	private int COL_CDPRODUTO_EDIT = 0;
	private int COL_BUSCA_PRODUTO_EDIT = 1;
	private int COL_QTDADE_EDIT = 4;
	private int COL_VLITEM_EDIT = 5;
	private int COL_VLPCTDESCONTO_EDIT = 6;

	private int COL_CDPRODUTO = 1;
	private int COL_BUSCA_PRODUTO = 2;
	private int COL_QTDADE = 5;
	private int COL_VLITEM = 6;
	private int COL_VLPCTDESCONTO = 7;
	private int COL_VLTOTALITEM = 8;
	private int COL_NUSEQPRODUTO = 9;

	private String APPID_COL_PRODUTO;
	private String APPID_COL_QTDADE;
	private String APPID_COL_VLITEM;
	private String APPID_COL_VLPCTDESCONTO;

	private Vector listAppID;

	private final String searchIconEnable = "enable";
	private final String searchIconDisable = "disable";

	private int ultColSelected = -1;
	private int lastSelectedRow = -1;
	private double valueUltimoDescontoNegociado;
	private boolean inLimiteCursorPosEdits = false;
	private boolean clickNoDrag = true;
	private boolean isProdutoEncontrado = true;
	private boolean clickInInsert = false;
	private boolean controlFocus = true;
	private boolean forceFocusProduto = false;
	private boolean mustDeleteAndInsertItem = false;
	private boolean penEventConsumed = false;
	private boolean isPedidoAberto;
	private boolean isSavedOk;
	private boolean inserindoItens;
	private String[] lastCdProdutoAndRow = new String[] { "", "" };

	private Pedido pedido;
	private ItemPedido itemPedidoBkp;

	private CadPedidoForm cadPedidoForm;

	private LabelName lbVlTotal;
	private LabelName lbVlTotalItensBonificados;
	private LabelValue lbDsProduto;
	private LabelValue lbVlTotalItensPedido;
	private LabelValue lbVlTotalItensPedidoBonificados;
	private ButtonAction btSalvarEnviar;
	private BaseGridEdit grid;
	private SessionContainer containerDados;

	public CadItemPedidoDesktopForm(CadPedidoForm cadPedidoForm, Pedido pedido) {
		super(Messages.ITEMPEDIDO_NOME_ENTIDADE);
		//--
		this.cadPedidoForm = cadPedidoForm;
		this.pedido = pedido;
		lbDsProduto = new LabelValue();
		lbDsProduto.setFont(UiUtil.defaultFontSmall);
		containerDados = new SessionContainer();
		//--
		lbVlTotalItensPedido = new LabelValue("9999,999");
		lbVlTotalItensPedidoBonificados = new LabelValue("9999,999");
		lbVlTotal = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTAL);
		lbVlTotalItensBonificados = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTAL_BONIF);
		//--
		lbVlTotal.setFont(UiUtil.defaultFontSmall);
		lbVlTotalItensPedido.setFont(UiUtil.defaultFontSmall);
		lbVlTotalItensPedidoBonificados.setFont(UiUtil.defaultFontSmall);
		lbVlTotalItensBonificados.setFont(UiUtil.defaultFontSmall);
		//--
		btExcluir.setText(Messages.ITEMPEDIDO_LABEL_EXCLUIR_ITEM);
		btSalvarEnviar = new ButtonAction(Messages.BOTAO_FECHAR_ENVIAR, "images/fecharenviarpedido.png");
		//--
		isPedidoAberto = pedido.isPedidoAberto();
		listAppID = new Vector();
	}

	protected BaseDomain screenToDomain() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		int row = lastSelectedRow;
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			itemPedido.cdProduto = grid.getCellText(row, COL_CDPRODUTO);
			itemPedido.vlItemPedido = ValueUtil.getDoubleValue(grid.getCellText(row, COL_VLITEM));
			itemPedido.vlPctDesconto = ValueUtil.getDoubleValue(grid.getCellText(row, COL_VLPCTDESCONTO));
			itemPedido.vlTotalItemPedido = ValueUtil.getDoubleValue(grid.getCellText(row, COL_VLTOTALITEM));
			itemPedido.flEditandoQtItemFaturamento = false;
		}
		itemPedido.setQtItemFisico(ValueUtil.getDoubleValue(grid.getCellText(row, COL_QTDADE)));
		//--
		return itemPedido;
	}

	@Override
	public void edit(BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		itemPedido.pedido = pedido;
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		itemPedido.getProduto();
		//--
		if (!itemPedido.usaCestaPromo) {
			itemPedido.getItemTabelaPreco();
		}
		penEventConsumed = false;
		super.edit(itemPedido);
	}

	@Override
	public void add() throws SQLException {
		super.add();
		addNovoItem();
	}

	@Override
	public void onFormShow() throws SQLException {
		carregaGrid();
	}

	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		if (isPedidoAberto) {
			cadPedidoForm.afterCrudItemPedido();
		}
		Util.resetImages();
	}

	@Override
	protected void visibleState() throws SQLException {
		btSalvar.setVisible(false);
		btExcluir.setVisible(isPedidoAberto && !LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop);
		btSalvarEnviar.setVisible(isPedidoAberto);
		containerDados.setVisible(!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop || !LavenderePdaConfig.ocultaInfosValoresPedido);
	}

	private void addNovoItem() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.pedido = pedido;
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		itemPedido.nuSeqItemPedido = getItemPedidoService().getNextNuSeqItemPedido(pedido);
		itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			itemPedido.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
		}
		itemPedido.setProduto(new Produto());
		setDomain(itemPedido);
	}

	public ItemPedido getDomainInGrid() throws SQLException {
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
		itemPedidoFilter.nuPedido = pedido.nuPedido;
		itemPedidoFilter.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		String tipoItemPedido;
		if (penEventConsumed) {
			tipoItemPedido = !grid.isChecked(grid.getSelectedIndex()) ? TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO : TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		} else {
			tipoItemPedido = grid.isChecked(grid.getSelectedIndex()) ? TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO : TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		}
		itemPedidoFilter.flTipoItemPedido = tipoItemPedido;
		itemPedidoFilter.nuSeqProduto = ValueUtil.getIntegerValue(grid.getCellText(grid.getSelectedIndex(), COL_NUSEQPRODUTO));
		itemPedidoFilter.cdProduto = grid.getCellText(grid.getSelectedIndex(), COL_CDPRODUTO);
		//--
		ItemPedido itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoFilter.getRowKey());
		if (itemPedido == null) return itemPedidoFilter;
		itemPedido.pedido = pedido;
		setDomain(itemPedido);
		return itemPedido;
	}

	protected void domainToScreen(BaseDomain domain) throws SQLException {
	}

	protected void clearScreen() throws java.sql.SQLException {
	}

	protected BaseDomain createDomain() throws SQLException {
		return new ItemPedido();
	}

	protected String getEntityDescription() {
		return title;
	}

	protected CrudService getCrudService() throws SQLException {
		return getItemPedidoService();
	}

	private ItemPedidoService getItemPedidoService() {
		return ItemPedidoService.getInstance();
	}

	private PedidoService getPedidoService() {
		return PedidoService.getInstance();
	}

	public ItemPedido getItemPedido() throws SQLException {
		return (ItemPedido) getDomain();
	}

	private void createGrid() {
		int ww2 = width / 8;
        String colExpand = "     ";
        String colCdProdutoRotulo = (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop && LavenderePdaConfig.ocultaColunaCdProduto) ? FrameworkMessages.CAMPO_ID : Messages.PRODUTO_NOME_ENTIDADE;
        //Define colunas da Grid
        Vector gridColDefinitonVector = new Vector();
        gridColDefinitonVector.addElement(new GridColDefinition(colCdProdutoRotulo, ww2, LEFT));
        if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
        	int tamColProd = LavenderePdaConfig.ocultaColunaCdProduto ? 4 : 3;
        	if (LavenderePdaConfig.ocultaInfosValoresPedido) {
        		tamColProd += 2;
        	}
        	gridColDefinitonVector.addElement(new GridColDefinition(LavenderePdaConfig.ocultaColunaCdProduto ? Messages.PRODUTO_NOME_ENTIDADE : Messages.PRODUTO_LABEL_DSPRODUTO, ww2 * tamColProd, LEFT)); //Coluna de descrição do produto
        } else {
        	gridColDefinitonVector.addElement(new GridColDefinition(isPedidoAberto ? colExpand : FrameworkMessages.CAMPO_ID, fm.stringWidth(colExpand), LEFT)); //Coluna de search de produto
        }
        if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
        	gridColDefinitonVector.addElement(new GridColDefinition(Messages.ITEMPEDIDO_LABEL_UN_MEDIDA, ww2, CENTER));
        	gridColDefinitonVector.addElement(new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTD_MULTIPLA, ww2, CENTER));
        }
		gridColDefinitonVector.addElement(new GridColDefinition(LavenderePdaConfig.usaConversaoUnidadesMedida ? Messages.ITEMPEDIDO_LABEL_QTITEMFISICOUN : Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, ww2, CENTER));
        if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
        	gridColDefinitonVector.addElement(new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, ww2, CENTER));
        	gridColDefinitonVector.addElement(new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO, ww2, CENTER));
        	gridColDefinitonVector.addElement(new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO, ww2, CENTER));
        }
		gridColDefinitonVector.addElement(new GridColDefinition(FrameworkMessages.CAMPO_ID, ww2, CENTER));
		//Converte para arrays de colunas da grid
		GridColDefinition[] gridColDefiniton = new GridColDefinition[gridColDefinitonVector.size()];
		Vm.arrayCopy(gridColDefinitonVector.toObjectArray(), 0, gridColDefiniton, 0, gridColDefinitonVector.size());
		//--
		GridEditNew.setCkeckCaptionText(Messages.ITEMPEDIDO_LABEL_BON);
    	grid = UiUtil.createGridEdit(gridColDefiniton, LavenderePdaConfig.isPermiteBonificarProduto());
    	GridEditNew.setCkeckCaptionText(" ");
    	grid.useKeyPress2ScrollInGrid = false;
    	grid.canClickSelectAll = false;
		//--
		int limiteGridHeight = barBottomContainer.getHeight();
		if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			limiteGridHeight += UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		} else if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			limiteGridHeight += UiUtil.getControlPreferredHeight() + HEIGHT_GAP;
		}
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - limiteGridHeight);
		//--
		grid.setGridControllable();
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			grid.useZeroAsEmpty = false;
		}
		changeValues();
		try {
			grid.setImage(searchIconEnable, UiUtil.getColorfulImage("images/add.png", fmH, fmH), false);
			grid.setImage(searchIconDisable, UiUtil.getColorfulImage("images/add.png", fmH, fmH, ColorUtil.labelNameForeColor), false);
		} catch (ImageException ie) {
		}
		//--
		if (isPedidoAberto) {
			setColumnsEditableInGrid();
		}
	}

	private void changeValues() {
		if (!grid.checkable) {
			COL_CDPRODUTO -= 1;
			COL_BUSCA_PRODUTO -= 1;
			COL_QTDADE -= 1;
			COL_VLITEM -= 1;
			COL_VLPCTDESCONTO -= 1;
			COL_VLTOTALITEM -= 1;
			COL_NUSEQPRODUTO -= 1;
		}
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			COL_BUSCA_PRODUTO = -1; //Seta para -1 para sair dos controles de evento
		}
		if (LavenderePdaConfig.ocultaInfosValoresPedido) {
			COL_QTDADE -= 2; //Anda duas posições para frente porque são dois as colunas escondidas antes desta
			COL_QTDADE_EDIT -= 2;
			COL_NUSEQPRODUTO -= 5; //Anda cinco posições para frente porque são quatro as colunas escondidas antes desta e a anterior que mudou
			COL_VLITEM = -1; //Seta para -1 para sair dos controles de evento
			COL_VLPCTDESCONTO = -1; //Seta para -1 para sair dos controles de evento
			COL_VLTOTALITEM = -1; //Seta para -1 para sair dos controles de evento
		}
		APPID_COL_PRODUTO = StringUtil.getStringValue(COL_CDPRODUTO);
		APPID_COL_QTDADE = StringUtil.getStringValue(COL_QTDADE);
		APPID_COL_VLITEM = StringUtil.getStringValue(COL_VLITEM);
		APPID_COL_VLPCTDESCONTO = StringUtil.getStringValue(COL_VLPCTDESCONTO);
		listAppID.addElement(APPID_COL_PRODUTO);
		listAppID.addElement(APPID_COL_QTDADE);
		listAppID.addElement(APPID_COL_VLITEM);
		listAppID.addElement(APPID_COL_VLPCTDESCONTO);
	}

	private void setColumnsEditableInGrid() {
		if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			EditText edProduto =  grid.setColumnEditable(COL_CDPRODUTO_EDIT, true);
			edProduto.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
			edProduto.autoSelect = true;
			edProduto.appObj = APPID_COL_PRODUTO;
		}
    	//--
    	EditNumberFrac edQtItem = grid.setColumnEditableDouble(COL_QTDADE_EDIT, true, 9);
    	edQtItem.autoSelect = true;
    	edQtItem.appObj = APPID_COL_QTDADE;
    	//--
    	if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
    		EditNumberFrac edVlItem = grid.setColumnEditableDouble(COL_VLITEM_EDIT, true, 9);
    		edVlItem.autoSelect = true;
    		edVlItem.appObj = APPID_COL_VLITEM;
    		//--
    		EditNumberFrac edVlPctDesc = grid.setColumnEditableDouble(COL_VLPCTDESCONTO_EDIT, true, 9);
    		edVlPctDesc.autoSelect = true;
    		edVlPctDesc.appObj = APPID_COL_VLPCTDESCONTO;
    	}
	}

	protected String getBtVoltarTitle() {
		return super.getBtVoltarTitleOriginal();
	}

	public void initCabecalhoRodape() throws SQLException {
		super.initCabecalhoRodape();
		UiUtil.add(barBottomContainer, btSalvarEnviar, 5);
	}

	protected void onFormStart() throws SQLException {
		LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
		UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, UiUtil.getControlPreferredHeight());
		createGrid();
		UiUtil.add(this, containerDados, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight());
		int posY = LavenderePdaConfig.ocultaInfosValoresPedido ? CENTER : TOP;
		if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			UiUtil.add(containerDados, lbDsProduto, LEFT + WIDTH_GAP, posY, FILL);
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			UiUtil.add(containerDados, lbVlTotalItensPedido, RIGHT - WIDTH_GAP, BOTTOM);
			UiUtil.add(containerDados, lbVlTotal, BEFORE - WIDTH_GAP_BIG, BOTTOM);
			if (LavenderePdaConfig.isPermiteBonificarProduto()) {
				UiUtil.add(containerDados, lbVlTotalItensPedidoBonificados, BEFORE - WIDTH_GAP, BOTTOM);
				UiUtil.add(containerDados, lbVlTotalItensBonificados, BEFORE - WIDTH_GAP_BIG, BOTTOM);
			}
		}
	}

	private void refreshPedidoToScreen(ItemPedido itemPedido) throws SQLException {
		String desconto = "";
		String vlItemPedido = StringUtil.getStringValueToInterface(itemPedido.vlItemPedido);
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			desconto = StringUtil.getStringValueToInterface(pedido.vlPctDescItem);
			vlItemPedido = StringUtil.getStringValueToInterface(ValueUtil.round(itemPedido.vlItemPedido - ((itemPedido.vlItemPedido * pedido.vlPctDescItem) / 100)));
		}
		int nuMaxPrecision = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface;
		//Monta Item
		Vector itemVector = new Vector();
		itemVector.addElement(itemPedido.getProduto().cdProduto); /*1 Código*/
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			itemVector.addElement(itemPedido.getDsProduto()); /*2 Descrição*/
		} else {
			itemVector.addElement(searchIconEnable); /*2 imagem*/
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			itemVector.addElement(StringUtil.getStringValue(itemPedido.getProduto().dsUnidadeFaturamento)); /*3 Un.Medida*/
			itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.getProduto().nuConversaoUnidadesMedida, nuMaxPrecision)); /*4 Qtdade*/
		}
		itemVector.addElement(""); /*5 Qtdade*/
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			itemVector.addElement(vlItemPedido); /*6 Vl*/
			itemVector.addElement(desconto); /*7 Estoque*/
			itemVector.addElement(""); /*8 VlTot*/
		}
		itemVector.addElement(StringUtil.getStringValue(itemPedido.nuSeqProduto)); /*9 SeqProd*/
		String[] item = new String[itemVector.size()];
		Vm.arrayCopy(itemVector.toObjectArray(), 0, item, 0, itemVector.size());
		//--
		grid.setItens(item, grid.getSelectedIndex());
		grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
	}

	private void selectProdutoClick(Produto produto) throws SQLException {
		inicializaItemParaVenda(produto);
		ItemPedido itemPedido = getItemPedido();
		//--
		getPedidoService().resetDadosItemPedido(pedido, itemPedido);
		//--
		refreshPedidoToScreen(itemPedido);
	}

	private void inicializaItemParaVenda(Produto produto) throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		getItemPedidoService().clearDadosItemPedido(pedido, itemPedido);
		if (produto != null) {
			itemPedido.setProduto(produto);
		}
		itemPedido.flTipoEdicao = 0;
		itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
		if (ValueUtil.isEmpty(itemPedido.cdTabelaPreco) && ValueUtil.isNotEmpty(SessionLavenderePda.cdTabelaPreco)) {
			itemPedido.cdTabelaPreco = SessionLavenderePda.cdTabelaPreco;
		}
		if (!isEditing()) {
			getNextNuSeqProduto(itemPedido);
		}
		if (LavenderePdaConfig.isPropagaUltimoDescontoItemPedido() || LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			itemPedido.vlPctDesconto = valueUltimoDescontoNegociado;
		}
		validateProdutoClicado(itemPedido);
	}

	private void validateProdutoClicado(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido()) {
			ProdutoBloqueadoService.getInstance().validateProdutoBloqueado(itemPedido, cadPedidoForm.cbTabelaPreco);
		}
		//--
		if (LavenderePdaConfig.usaAreaVendas) {
			AreaVendaProdutoService.getInstance().validadeProdutoAreaVenda(pedido.cdAreaVenda, itemPedido.cdProduto, false);
		}
		//--
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
			ClienteService.getInstance().validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, pedido.getCliente());
		}
	}

	private void addNewLineInGrid() {
		Vector newLine = new Vector();
		newLine.addElement(""); //Produto
		if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			newLine.addElement(searchIconEnable); //Search de Produto
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			newLine.addElement(""); //Unidade Medida
			newLine.addElement(""); //Conversao Unidade
		}
	    newLine.addElement(""); //Quantidade
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			newLine.addElement(""); //Valor
			newLine.addElement(""); //Desconto
			newLine.addElement(""); //Total Item
		}
		newLine.addElement(""); //NuSequencia
		//Converte para array de String
		String[] newLineString = new String[newLine.size()];
		Vm.arrayCopy(newLine.toObjectArray(), 0, newLineString, 0, newLine.size());
		//--
		grid.add(newLineString);
		grid.scrollTo(lastSelectedRow);
	}

	private void carregaGrid() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = null;
		String[][] gridItems;
		grid.clear();
		int col = 0;
		try {
			grid.setItems(null);
			//---
			domainList = getItemPedidoList();
			listSize = domainList.size();
			//--
			if (listSize > 0) {
				gridItems = new String[listSize][getItemPedidoToGrid(domainList.items[0]).length];
				IntVector rowCheckedList = new IntVector(0);
				for (int i = 0; i < listSize; i++) {
					if (grid.checkable) {
						ItemPedido itemPedido = (ItemPedido) domainList.items[i];
						if (TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(itemPedido.flTipoItemPedido)) {
							rowCheckedList.addElement(i);
						}
					}
					gridItems[i] = getItemPedidoToGrid(domainList.items[i]);
					if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
						gridItems[i][COL_BUSCA_PRODUTO_EDIT] = searchIconDisable;
					}
					gridRowColEnableProdutoAndCheck(i, false);
				}
				//--
				grid.setItems(gridItems);
				if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
					grid.qsort(1);
				}
				//--
				int size = rowCheckedList.size();
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						grid.setChecked(rowCheckedList.items[i], true);
					}
					rowCheckedList = null;
				}
				//--
				lastSelectedRow = LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop ? 0 : grid.size() - 1;
				grid.setSelectedIndex(lastSelectedRow);
				if (isPedidoAberto) {
					edit(getDomainInGrid());
					getDsProdutoSelected();
					col = COL_QTDADE;
				} else {
					getDsProdutoSelected();
				}
			} else if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
				inserindoItens = true;
				addAllItemPedidoInGrid();
			} else {
				addNewLineInGrid();
				lastSelectedRow =  LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop ? 0 : grid.size() - 1;
				grid.setSelectedIndex(lastSelectedRow);
				gridRowColEnableProdutoAndCheck(lastSelectedRow, true);
				if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
					col = COL_CDPRODUTO;
				} else {
					col = COL_QTDADE_EDIT;
				}
				add();
			}
		} finally {
			gridItems = null;
			domainList = null;
			msg.unpop();
		}
		if (isPedidoAberto) {
			if (grid.getItemsVector().size() > 0) {
				grid.exibeControleGrid(grid.getSelectedIndex(), col);
			}
			makeItemPedidoTmp();
		} else {
			grid.requestFocus();
		}
	}

	private String[] getItemPedidoToGrid(final Object domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		itemPedido.cdProduto = itemPedido.cdProduto == null ? "" : itemPedido.cdProduto;
		int nuMaxPrecision = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface;
		//Monta Item
		Vector itemVector = new Vector();
		itemVector.addElement(itemPedido.cdProduto); /*1 Código*/
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			itemVector.addElement(itemPedido.getDsProduto()); /*2 Descrição Produto*/
		} else {
			itemVector.addElement(isEditing() ? searchIconDisable : searchIconEnable); /*2 imagem*/
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			itemVector.addElement(StringUtil.getStringValue(itemPedido.getProduto().dsUnidadeFaturamento)); /*3 UN*/
			itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.getProduto().nuConversaoUnidadesMedida , nuMaxPrecision)); /*4 QtMul*/
		}
		if (LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
			String qtd = itemPedido.getQtItemFisico() == 0 && inserindoItens ? "" : StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico() , nuMaxPrecision);
			itemVector.addElement(qtd); /*5 Qtdade*/
		} else {
			itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico() , nuMaxPrecision)); /*5 Qtdade*/
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido)); /*6 Vl*/
			itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto)); /*7 Estoque*/
			itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido)); /*8 VlTot*/
		}
		itemVector.addElement(StringUtil.getStringValueToInterface(itemPedido.nuSeqProduto)); /*9 SeqProd*/
		String[] item = new String[itemVector.size()];
		Vm.arrayCopy(itemVector.toObjectArray(), 0, item, 0, itemVector.size());
		//--
		return item;
	}

	private boolean isItemEmpty(int row) {
		boolean retorno = true;
		if (ValueUtil.isNotEmpty(grid.getCellText(row, COL_CDPRODUTO))) {
			retorno = false;
		} else if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop && ValueUtil.getDoubleValue(grid.getCellText(row, COL_QTDADE))  != 0) {
			retorno = false;
		} else if (!LavenderePdaConfig.ocultaInfosValoresPedido && ValueUtil.getDoubleValue(grid.getCellText(row, COL_VLITEM)) != 0) {
			retorno = false;
		}
		return ValueUtil.isEmpty(grid.getCellText(row, COL_CDPRODUTO)) || retorno;
	}

	private Vector getItemPedidoList() {
		Vector list = PedidoService.getInstance().getItensListByTipo(TipoItemPedido.TIPOITEMPEDIDO_NORMAL, pedido);
		updateLabelsContainerDados();
		return list;
	}

	private void preparaProduto(Produto produto) throws SQLException {
		addNovoItem();
		inicializaItemParaVenda(produto);
		ItemPedido itemPedido = getItemPedido();
		//--
		getPedidoService().resetDadosItemPedido(pedido, itemPedido);
	}

	/**
	 * Converte todos os produtos em itens e salva no pedido
	 * @throws SQLException 
	 */
	private void addAllItemPedidoInGrid() throws SQLException {
		Vector list = getProdutoList();
		int size = list.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ProdutoBase produto = (ProdutoBase) list.items[i];
				if (produto instanceof Produto) {
					preparaProduto((Produto)produto);
				} else {
					preparaProduto(ProdutoService.getInstance().getProduto(produto.cdProduto));
				}
				//--
				getCrudService().insert(getItemPedido());
			}
			PedidoService.getInstance().findItemPedidoList(pedido);
			carregaGrid();
		}
	}

	private Vector getProdutoList() throws SQLException {
		ProdutoBase produto;
		try {
			produto = getProdutoFilter();
		} catch (FilterNotInformedException e) {
			return new Vector(0);
		}
		
		return ProdutoService.getInstance().findProdutos("", pedido.cdTabelaPreco, null, false, produto, false, false);
	}

	public void updateLabelsContainerDados() {
		lbVlTotalItensPedido.setValue(pedido.vlTotalPedido);
		if (LavenderePdaConfig.isPermiteBonificarProduto()) {
			lbVlTotalItensPedidoBonificados.setValue(pedido.vlBonificacaoPedido);
		}
	}

	private void newItemInGrid() throws SQLException {
		addNewLineInGrid();
		//--
		int row = grid.size() - 1;
		gridRowColEnableProdutoAndCheck(row, true);
		//--
		grid.setSelectedIndex(row);
		add();
		makeItemPedidoTmp();
		getDsProdutoSelected();
		grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
	}

	private void gridRowColEnableProdutoAndCheck(int row, boolean enable) {
		grid.gridController.setRowColDisable(row, COL_CDPRODUTO, !enable);
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case PenEvent.PEN_DOWN: {
				controlFocus = true;
				if (event.target == grid) {
					if (forceFocusProduto) {
						if (grid.getSelectedIndex() != -1) {
							grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
						}
					}
					clickNoDrag = true;
				}
				forceFocusProduto = false;
				break;
			}
			case GridEvent.CHECK_CHANGED_EVENT: {
				if (event.target == grid) {
					penEventConsumed = false;
					if (isPedidoAberto) {
						if (grid.isChecked(grid.getSelectedIndex())) {
							if (lastSelectedRow != grid.getSelectedIndex()) {
								validateNewItemWhenGridCheckedEvent();
							} else {
								toggleBonificacaoWhenItemIsNotBonificado();
							}
						} else {
							if (isEditing() || (lastSelectedRow != grid.getSelectedIndex())) {
								boolean result = UiUtil.showConfirmYesNoMessage(Messages.ITEM_MSG_CONFIRM_DESBONIFICACAO);
								if (result) {
									validateDuplicatedAndPrepareNewItem(TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
								} else {
									grid.setChecked(grid.getSelectedIndex(), !result);
									GridEditNew.repaint();
								}
							}
							getItemPedido().flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
							getNextNuSeqProduto(getItemPedido());
						}
						lastSelectedRow = grid.getSelectedIndex();
						grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
						penEventConsumed = false;
					}
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if (event.target == grid) {
					ultColSelected = ((GridEvent) event).col;
					if (!isPedidoAberto && (lastSelectedRow != grid.getSelectedIndex())) {
						lastSelectedRow = grid.getSelectedIndex();
						getDsProdutoSelected();
					}
				}
				break;
			}
			case PenEvent.PEN_DRAG_START: {
				if (event.target == grid) {
					clickNoDrag = false;
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target == grid) && isPedidoAberto) {
					boolean isItemSavedOk = true;
					isItemSavedOk = saveAndMakeItemPedidoTmp();
					int line = grid.getSelectedIndex();
					if (isItemSavedOk) {
						if (clickNoDrag) {
							if (line != -1) {
								if ((ultColSelected == COL_BUSCA_PRODUTO) && !isEditing()) {
									showListProduto();
								}
							} else if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
								int gridSize = grid.size();
								gridSize = gridSize == 0 ? 1 : gridSize + 1;
								int lineClicked = grid.getLineByPenDown(((PenEvent) event).y);
								if (gridSize == (lineClicked + 1)) {
									grid.hideControl();
									newItemInGrid();
								}
							}
						}
						if (grid.getSelectedIndex() != lastSelectedRow) {
							if (ValueUtil.isNotEmpty(grid.getSelectedItem())) {
								showControlInGrid((String) ((Control) event.target).appObj);
							}
							getDsProdutoSelected();
							lastSelectedRow = grid.getSelectedIndex();
						}
						ultColSelected = -1;
					}
				} else {
					int line = grid.getSelectedIndex();
					if (!((event.target instanceof Button) || (event.target instanceof BaseEdit)) && isPedidoAberto) {
						validaAndSalvaItemInGridFocusOut(line);
					}
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				controlFocus = true;
				KeyEvent ke = (KeyEvent) event;
				int key = ke.key;
				if (key == SpecialKeys.ESCAPE) {
					if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
						if ((grid.getSelectedIndex() != -1) && isPedidoAberto) {
							grid.hideControl();
							//--
							ItemPedido itemPedido;
							if (mustDeleteAndInsertItem) {
								edit(itemPedidoBkp);
								itemPedido = getItemPedido();
							} else {
								itemPedido = itemPedidoBkp;
							}
							grid.setItens(getItemPedidoToGrid(itemPedido), grid.getSelectedIndex());
							if (grid.checkable) {
								if (!isEditing() && grid.isChecked(grid.getSelectedIndex())) {
									getItemPedido().flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
								}
								grid.setChecked(grid.getSelectedIndex(), TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(itemPedido.flTipoItemPedido));
							}
							grid.exibeControleGrid(grid.getSelectedIndex(), isEditing() ? COL_QTDADE : COL_CDPRODUTO);
							//--
							if (mustDeleteAndInsertItem) {
								makeItemPedidoTmp();
							}
							//--
							penEventConsumed = false;
							mustDeleteAndInsertItem = false;
							lastCdProdutoAndRow = new String[] { "", "" };
						}
						event.consumed = true;
					}
				}
				if ((key == SpecialKeys.TAB)) {
					if (event.target instanceof Edit) {
						Edit ed = (Edit) event.target;
						rightKeyClick(ed);
					}
				} else if (key == SpecialKeys.ENTER || (key == SpecialKeys.ACTION)) {
					if (event.target instanceof Edit) {
						Edit ed = (Edit) event.target;
						enterKeyClick(ed);
					}
				} else if (key == SpecialKeys.RIGHT) {
					if (event.target instanceof Edit) {
						Edit ed = (Edit) event.target;
						if (ed.getCursorPos()[1] == ed.getLength()) {
							if (inLimiteCursorPosEdits || (ed.getLength() == 0)) {
								rightKeyClick(ed);
								inLimiteCursorPosEdits = false;
							} else {
								inLimiteCursorPosEdits = true;
							}
						} else {
							inLimiteCursorPosEdits = false;
						}
					}
				} else if (key == SpecialKeys.LEFT) {
					if (event.target instanceof Edit) {
						Edit ed = (Edit) event.target;
						if (ed.getCursorPos()[1] == 0) {
							if (inLimiteCursorPosEdits || (ed.getLength() == 0) || APPID_COL_QTDADE.equals(ed.appObj)) {
								leftKeyClick(ed);
							} else {
								inLimiteCursorPosEdits = true;
							}
						} else {
							inLimiteCursorPosEdits = false;
						}
					}
				} else if (ke.isDownKey()) {
					if (isPedidoAberto) {
						if (grid.getSelectedIndex() != -1) {
							downKeyClick(event);
							showControlInGrid((String) ((Control) event.target).appObj);
						}
					} else {
						if (grid.getSelectedIndex() != (grid.size() - 1)) {
							lastSelectedRow = grid.getSelectedIndex();
							grid.setSelectedIndex(grid.getSelectedIndex() + 1);
							getDsProdutoSelected();
						}
					}
				} else if (ke.isUpKey()) {
					if (grid.getSelectedIndex() != -1) {
						if (isPedidoAberto) {
							upKeyClick(event);
							showControlInGrid((String) ((Control) event.target).appObj);
						} else {
							lastSelectedRow = grid.getSelectedIndex();
							grid.setSelectedIndex(grid.getSelectedIndex() - 1);
							getDsProdutoSelected();
						}
					}
				} else if (key == SpecialKeys.INSERT) {
					if (grid.getSelectedIndex() != -1) {
						if (!isEditing() && isPedidoAberto) {
							clickInInsert = true;
							showListProduto();
							clickInInsert = false;
						}
					}
				}
				Window.needsPaint = true;
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (grid.getSelectedIndex() != lastSelectedRow) break;
				Object obj = event.target;
				if (obj instanceof BaseEdit) {
					BaseEdit ed = (BaseEdit) obj;
					if ((ed.appObj.equals(APPID_COL_PRODUTO)) && !clickInInsert && controlFocus && !ed.isKeyboardShown()) {
						controlFocus = false;
						String cdProduto = grid.getCellText(grid.getSelectedIndex(), COL_CDPRODUTO);
						if (!lastCdProdutoAndRow[0].equals(cdProduto) || !lastCdProdutoAndRow[1].equals(StringUtil.getStringValue(grid.getSelectedIndex()))) {
							if (!ValueUtil.isEmpty(cdProduto)) {
								Produto produto;
								if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
									produto = ProdutoService.getInstance().getProduto(cdProduto);
								} else {
									produto = ProdutoTabPrecoService.getInstance().findByCdProdutoAndCdTabelaPreco(cdProduto, pedido.cdTabelaPreco);
								}
								if (!ValueUtil.isEmpty(produto.rowKey) || (produto != null)) {
									if (produto.equals(new Produto())) {
										grid.setCellText(grid.getSelectedIndex(), COL_CDPRODUTO, "");
										throw new ValidationException(Messages.PRODUTO_NAO_ENCONTRADO);
									} else {
										isProdutoEncontrado = true;
										selectProdutoClick(produto);
										getDsProdutoSelected();
										//--
										lastCdProdutoAndRow[0] = cdProduto;
										lastCdProdutoAndRow[1] = StringUtil.getStringValue(grid.getSelectedIndex());
									}
								}
							}
						}
					} else if (ed.appObj.equals(APPID_COL_QTDADE)) {
						calculoRapidoQtdade(grid.getSelectedIndex());
					} else if (ed.appObj.equals(APPID_COL_VLITEM)) {
						calculoRapidoValorItem(grid.getSelectedIndex());
					} else if (ed.appObj.equals(APPID_COL_VLPCTDESCONTO)) {
						calculoRapidoDesconto(grid.getSelectedIndex());
					}
				}
				break;
			}
			case KeyboardEvent.KEYBOARD_PRESS: {
				Object obj = event.target;
				if (obj instanceof BaseEdit) {
					BaseEdit ed = (BaseEdit) obj;
					if (ed.appObj.equals(APPID_COL_PRODUTO)) {
						grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
					}
				}
			}
			case ControlEvent.PRESSED: {
				if (event.target == btSalvarEnviar) {
					btSalvarEnviarClick();
				} 
				break;
			}
		}
	}

	private void toggleBonificacaoWhenItemIsNotBonificado() throws SQLException {
		boolean result = UiUtil.showConfirmYesNoMessage(Messages.ITEM_MSG_CONFIRM_BONIFICACAO);
		if (result) {
			if (isEditing()) {
				validateDuplicatedAndPrepareNewItem(TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
			} else {
				validateOldItemAndPrepareItem(TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
			}
		} else {
			getItemPedido().flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			grid.setChecked(grid.getSelectedIndex(), result);
			GridEditNew.repaint();
		}
	}

	private void validateNewItemWhenGridCheckedEvent() throws SQLException {
		isSavedOk = true;
		//--
		int lastSelectedLineTmp = grid.getSelectedIndex();
		grid.setChecked(lastSelectedLineTmp, false);
		//--
		PenEvent pen = new PenEvent();
		pen.type = PenEvent.PEN_UP;
		Control ctr = grid;
		pen.target = ctr;
		pen.consumed = false;
		onEvent(pen);
		//--
		if (isSavedOk) {
			grid.setSelectedIndex(lastSelectedLineTmp);
			grid.setChecked(lastSelectedLineTmp, true);
			toggleBonificacaoWhenItemIsNotBonificado();
		}
	}

	private void validateOldItemAndPrepareItem(String tipoItemPedido) throws SQLException {
		boolean isNewItemEmpty = isItemEmpty(lastSelectedRow);
		if (isNewItemEmpty || isEditing()) {
			int line = grid.getSelectedIndex();
			if (isNewItemEmpty) {
				if (line == lastSelectedRow) {
					ItemPedido itemPedido = getItemPedido();
					itemPedido.flTipoItemPedido = tipoItemPedido;
					return;
				} else {
					grid.del(lastSelectedRow);
					//--
					if (lastSelectedRow == grid.size()) {
						grid.hideControl();
						if ((ultColSelected != -1) && (isEditableColumn(ultColSelected))) {
							grid.exibeControleGrid(line, ultColSelected);
						}
					}
				}
			} else {
				salvarItem();
			}
			if (line != -1) {
				penEventConsumed = true;
				checkItemAndPrepareToEdition(line);
				//--
				ItemPedido itemPedido = getItemPedido();
				super.add();
				itemPedido.vlTotalItemPedidoOld = 0; // Se não limpar ele não valida a bonificação.
				itemPedido.flTipoItemPedido = tipoItemPedido;
				setDomain(itemPedido);
				mustDeleteAndInsertItem = true;
				if (!LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
					ItemPedidoService.getInstance().validateDuplicated(getItemPedido());
				}
			}
		} else {
			getItemPedido().flTipoItemPedido = tipoItemPedido;
			getNextNuSeqProduto(getItemPedido());
		}
	}

	public void checkItemAndPrepareToEdition(int line) throws SQLException {
		String cdProduto = grid.getCellText(line, COL_CDPRODUTO);
		if (!ValueUtil.isEmpty(cdProduto)) {
			edit(getDomainInGrid());
		}
		makeItemPedidoTmp();
	}

	private void validateDuplicatedAndPrepareNewItem(String tipoItemPedido) throws SQLException {
		penEventConsumed = true;
		saveAndMakeItemPedidoTmp();
		//--
		ItemPedido itemPedido = getItemPedido();
		super.add();
		itemPedido.vlTotalItemPedidoOld = 0; // Se não limpar ele não valida a bonificação.
		itemPedido.flTipoItemPedido = tipoItemPedido;
		setDomain(itemPedido);
		mustDeleteAndInsertItem = true;
		if (!LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
			ItemPedidoService.getInstance().validateDuplicated(getItemPedido());
		}
	}

	private boolean saveAndMakeItemPedidoTmp() throws SQLException {
		int line = grid.getSelectedIndex();
		if ((lastSelectedRow != line) && (lastSelectedRow != -1)) {
			return validaAndSalvaItemInGridFocusOut(line);
		} else if ((lastSelectedRow == -1) && (line != -1)) {
			checkItemAndPrepareToEdition(line);
		}
		return true;
	}

	private void btSalvarEnviarClick() throws SQLException {
		int line = grid.getSelectedIndex();
		boolean cancelaPedido = false;
		if (isPedidoAberto) {
			if (TipoPedidoService.getInstance().isTipoPedidoObrigaQtdProdutos(pedido.cdTipoPedido) && LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
				cancelaPedido = isCancelaPedido();
			}
			if (!cancelaPedido) {
				if (validaAndSalvaItemInGridFocusOut(line)) {
					if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_ENVIAR_PEDIDOS_AGORA)) { //Salvar, FecharPedido, enviar p/ web e fechar tela;
						cadPedidoForm.fecharEnviarPedido(true);
					}
				}
			} else {
				return;
			}
		}
	}

	protected void voltarClick() throws SQLException {
		int line = grid.getSelectedIndex();
		boolean cancelaPedido = false;
		if (isPedidoAberto) {
			if (TipoPedidoService.getInstance().isTipoPedidoObrigaQtdProdutos(pedido.cdTipoPedido) && LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
				cancelaPedido = isCancelaPedido();
			}
			if (!cancelaPedido) {
				validaAndSalvaItemInGridFocusOut(line);
			} else {
				return;
			}
		}
		super.voltarClick();
	}

	private boolean isCancelaPedido() throws SQLException {
		int size = grid.size();
		boolean allFieldsOk = true;
		for (int i = 0; i < size; i++) {
			if (ValueUtil.isEmpty(grid.getCellText(i, COL_QTDADE))) {
				allFieldsOk = false;
				break;
			}
		}
		if (!allFieldsOk) {
			if (UiUtil.showConfirmYesNoMessage(Messages.ITEMPEDIDO_MSG_SEM_QUANTIDADE_ITEM)) {
				super.voltarClick();
				cadPedidoForm.delete(pedido);
				cadPedidoForm.close();
			}
			return true;
		}
		return false;
	}

	private boolean validaAndSalvaItemInGridFocusOut(int line) throws SQLException {
		if (isItemEmpty(lastSelectedRow)) {
			grid.del(lastSelectedRow);
			//--
			if (lastSelectedRow == grid.size()) {
				grid.hideControl();
				if ((ultColSelected != -1) && (isEditableColumn(ultColSelected))) {
					grid.exibeControleGrid(line, ultColSelected);
				}
			}
		} else {
			grid.setSelectedIndex(lastSelectedRow);
			if (!salvarItem()) return false;
			//--
			grid.setSelectedIndex(line);
		}
		if (line != -1) {
			checkItemAndPrepareToEdition(line);
		}
		return true;
	}

	private boolean isEditableColumn(int ultColSelected) {
		return (ultColSelected == COL_QTDADE_EDIT) && (ultColSelected == COL_VLITEM) && (ultColSelected == COL_VLPCTDESCONTO);
	}

	public void showListProduto() throws SQLException {
		ListProdutoWindow listProduto = new ListProdutoWindow(pedido.cdTabelaPreco, true);
		listProduto.popup();
		if (listProduto.produto != null) {
			selectProdutoClick(listProduto.produto);
			getDsProdutoSelected();
		} else {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
		}
	}

	private void upKeyClick(Event event) throws SQLException {
		if (!(grid.getSelectedIndex() < 0) && grid.getSelectedIndex() != lastSelectedRow) {
			grid.hideControl();
			if (isItemEmpty(lastSelectedRow)) {
				grid.del(lastSelectedRow);
				//--
				grid.setSelectedIndex(grid.size() - 1);
			} else {
				if (!isPreviousItemEmpty()) {
					BaseEdit ed = (BaseEdit) event.target;
					if (ed.appObj.equals(APPID_COL_QTDADE)) {
						calculoRapidoQtdade(lastSelectedRow);
					} else if (ed.appObj.equals(APPID_COL_VLITEM)) {
						calculoRapidoValorItem(lastSelectedRow);
					} else if (ed.appObj.equals(APPID_COL_VLPCTDESCONTO)) {
						calculoRapidoDesconto(lastSelectedRow);
					}
					if (!salvarItem()) return;
				}
			}
			lastSelectedRow = grid.getSelectedIndex();
			edit(getDomainInGrid());
			makeItemPedidoTmp();
			//--
			getDsProdutoSelected();
		}
	}
	
	private boolean isPreviousItemEmpty() {
		return isItemEmpty(grid.getSelectedIndex() + 1);
	}

	private void downKeyClick(Event event) throws SQLException {
		grid.hideControl();
		if (!isItemEmpty(lastSelectedRow)) {
			BaseEdit ed = (BaseEdit) event.target;
			if (ed.appObj.equals(APPID_COL_QTDADE)) {
				calculoRapidoQtdade(lastSelectedRow);
			} else if (ed.appObj.equals(APPID_COL_VLITEM)) {
				calculoRapidoValorItem(lastSelectedRow);
			} else if (ed.appObj.equals(APPID_COL_VLPCTDESCONTO)) {
				calculoRapidoDesconto(lastSelectedRow);
			}
			if (!salvarItem()) return;
			if (grid.size() == (lastSelectedRow + 1)) {
				if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
					addNewLineInGrid();
					grid.setSelectedIndex(grid.size() - 1);
					gridRowColEnableProdutoAndCheck(grid.getSelectedIndex(), true);
					add();
				}
			} else {
				if (ValueUtil.isEmpty(grid.getCellText(grid.getSelectedIndex(), COL_CDPRODUTO))) {
					add();
				} else {
					edit(getDomainInGrid());
				}
			}
			makeItemPedidoTmp();
			lastSelectedRow = grid.getSelectedIndex();
			getDsProdutoSelected();
		}
	}

	public boolean salvarItem() throws SQLException {
		try {
			salvarClick();
		} catch (ValidationException e) {
			if ((e.getMessage() != null) && e.getMessage().equals(ValidationException.EXCEPTION_ABORT_PROCESS)) {
				afterExceptionThrowedOnEvent(e);
				return false;
			}
			throw e;
		}
		return true;
	}

	private void rightKeyClick(Edit edOrg) throws SQLException {
		if (edOrg.appObj.equals(APPID_COL_PRODUTO)) {
			if (isProdutoEncontrado) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
			} else {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
			}
		} else if (edOrg.appObj.equals(APPID_COL_QTDADE) && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLITEM);
		} else if (edOrg.appObj.equals(APPID_COL_VLITEM) && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLPCTDESCONTO);
		} else {
			int col = isEditing() || LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop ? COL_QTDADE : COL_CDPRODUTO;
			grid.exibeControleGrid(grid.getSelectedIndex(), col);
		}
	}

	private void enterKeyClick(Edit edOrg) throws SQLException {
		if (edOrg.appObj.equals(APPID_COL_PRODUTO)) {
			if (isProdutoEncontrado) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
			} else {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
			}
		} else if (edOrg.appObj.equals(APPID_COL_QTDADE) && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLITEM);
		} else if (edOrg.appObj.equals(APPID_COL_VLITEM) && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLPCTDESCONTO);
		} else {
			enterKeyClickDown();
			int col = isEditing() || LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop ? COL_QTDADE : COL_CDPRODUTO;
			grid.exibeControleGrid(grid.getSelectedIndex(), col);
		}
	}
	
	private void enterKeyClickDown() throws SQLException {
		grid.hideControl();
		if (!isItemEmpty(lastSelectedRow)) {
			if (!salvarItem()) return;
			if (grid.size() == (lastSelectedRow + 1)) {
				if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
					addNewLineInGrid();
					grid.setSelectedIndex(grid.size() - 1);
					gridRowColEnableProdutoAndCheck(grid.getSelectedIndex(), true);
					add();
				}
			} else {
				if (ValueUtil.isEmpty(grid.getCellText(grid.getSelectedIndex(), COL_CDPRODUTO))) {
					add();
				} else {
					grid.setSelectedIndex(lastSelectedRow + 1);
					edit(getDomainInGrid());
				}
			}
			makeItemPedidoTmp();
			lastSelectedRow = grid.getSelectedIndex();
			getDsProdutoSelected();
		}
	}

	private void leftKeyClick(Edit edOrg) throws SQLException {
		if (edOrg.appObj.equals(APPID_COL_PRODUTO)) {
			int col = LavenderePdaConfig.ocultaInfosValoresPedido ? COL_QTDADE : COL_VLPCTDESCONTO;
			grid.exibeControleGrid(grid.getSelectedIndex(), col);
		} else if (edOrg.appObj.equals(APPID_COL_QTDADE)) {
			if (grid.gridController.isEnabled(grid.getSelectedIndex(), COL_CDPRODUTO) && !LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
			} else {
				int col = LavenderePdaConfig.ocultaInfosValoresPedido ? COL_QTDADE : COL_VLPCTDESCONTO;
				grid.exibeControleGrid(grid.getSelectedIndex(), col);
			}
		} else if (edOrg.appObj.equals(APPID_COL_VLITEM)) {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
		} else if (edOrg.appObj.equals(APPID_COL_VLPCTDESCONTO)) {
			grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLITEM);
		}
	}

	private boolean itemPedidoEquals(ItemPedido itemPedido) {
		itemPedidoBkp.cdProduto = itemPedidoBkp.cdProduto == null ? "" : itemPedidoBkp.cdProduto;
		return (!ValueUtil.isEmpty(itemPedido.cdProduto) || !ValueUtil.isEmpty(itemPedidoBkp.cdProduto)) &&
				(ValueUtil.round(itemPedidoBkp.vlItemPedido) == ValueUtil.round(itemPedido.vlItemPedido)) &&
				(ValueUtil.round(itemPedidoBkp.vlPctDesconto) == ValueUtil.round(itemPedido.vlPctDesconto)) &&
				(ValueUtil.round(itemPedidoBkp.getQtItemFisico()) == ValueUtil.round(itemPedido.getQtItemFisico())) &&
				(itemPedidoBkp.flTipoItemPedido.equals(itemPedido.flTipoItemPedido)) &&
				(ValueUtil.round(itemPedidoBkp.nuSeqProduto) == ValueUtil.round(itemPedido.nuSeqProduto)) &&
				(ValueUtil.round(itemPedidoBkp.vlTotalItemPedido) == ValueUtil.round(itemPedido.vlTotalItemPedido)) &&
				(itemPedidoBkp.cdProduto.equals(itemPedido.cdProduto));
	}

	private void showControlInGrid(String appObj) {
		int idObj = COL_CDPRODUTO;
		if (isItemEmpty(grid.getSelectedIndex()) || ValueUtil.isEmpty(appObj)) {
			idObj = COL_CDPRODUTO;
		} else {
			idObj = ValueUtil.getIntegerValue(appObj);
		}
		if (idObj == COL_CDPRODUTO) {
			if (!grid.gridController.isEnabled(grid.getSelectedIndex(), idObj)) {
				idObj = COL_QTDADE;
			}
		}
		grid.exibeControleGrid(grid.getSelectedIndex(), idObj);
	}

	private void getDsProdutoSelected() throws SQLException {
		String attrProduto = grid.getCellText(grid.getSelectedIndex(), COL_CDPRODUTO);
		if (!ValueUtil.isEmpty(attrProduto)) {
			attrProduto = ProdutoService.getInstance().getProduto(attrProduto).toString();
			if (!ValueUtil.isEmpty(attrProduto)) {
				lbDsProduto.setText(attrProduto);
			}
		} else {
			lbDsProduto.setText("");
		}
	}

	private void calculoRapidoDesconto(int selectedIndex) throws SQLException {
		double vlItemPedido = getItemPedido().vlBaseItemPedido;
		String desconto = grid.getCellText(selectedIndex, COL_VLPCTDESCONTO);
		double vlPctDesc = ValueUtil.round(ValueUtil.getDoubleValue(ValueUtil.isEmpty(desconto) ? "0" : desconto));
		double vlItem = ValueUtil.round(vlItemPedido - ((vlItemPedido * vlPctDesc) / 100));
		if (vlPctDesc != 0 || (vlItemPedido < vlItem)) {
			grid.setCellText(selectedIndex, COL_VLITEM, StringUtil.getStringValueToInterface(vlItem));
			grid.setCellText(selectedIndex, COL_VLTOTALITEM,
					StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(grid.getCellText(selectedIndex, COL_QTDADE)) * vlItem));
		} else {
			double vlItemGrid = ValueUtil.getDoubleValue(grid.getCellText(selectedIndex, COL_VLITEM));
			String cdProduto = grid.getCellText(selectedIndex, COL_CDPRODUTO);
			if (vlItemGrid < vlItemPedido && ValueUtil.isNotEmpty(cdProduto)) {
				grid.setCellText(selectedIndex, COL_VLITEM, StringUtil.getStringValueToInterface(vlItem));
				grid.setCellText(selectedIndex, COL_VLTOTALITEM,
						StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(grid.getCellText(selectedIndex, COL_QTDADE)) * vlItem));
			}
		}
	}

	private void calculoRapidoValorItem(int selectedIndex) throws SQLException {
		String vlItem = grid.getCellText(selectedIndex, COL_VLITEM);
		String cdProduto = grid.getCellText(selectedIndex, COL_CDPRODUTO);
		if (!ValueUtil.isEmpty(cdProduto)) {
			ItemPedido itemPedido = getItemPedido();
			double vlBaseItemPedido = itemPedido.vlBaseItemPedido;
			if (vlBaseItemPedido != 0) {
				double vlItemPedido = ValueUtil.getDoubleValue(vlItem);
				double vlPctDesconto = ValueUtil.round((1 - (vlItemPedido / vlBaseItemPedido)) * 100);
				if (vlPctDesconto < 0) {
					vlPctDesconto = 0;
				}
				grid.setCellText(selectedIndex, COL_VLPCTDESCONTO, StringUtil.getStringValueToInterface(vlPctDesconto));
				grid.setCellText(selectedIndex, COL_VLTOTALITEM,
						StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(grid.getCellText(selectedIndex, COL_QTDADE)) * vlItemPedido));
			}
		}
	}

	private void calculoRapidoQtdade(int selectedIndex) {
		String qtItem = grid.getCellText(selectedIndex, COL_QTDADE);
		double vlItemPedido = ValueUtil.getDoubleValue(grid.getCellText(selectedIndex, COL_VLITEM));
		if (vlItemPedido != 0) {
			grid.setCellText(selectedIndex, COL_VLTOTALITEM, StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(qtItem) * vlItemPedido));
		}
	}

	private void makeItemPedidoTmp() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedidoBkp = new ItemPedido();
		itemPedidoBkp.cdProduto = itemPedido.cdProduto;
		itemPedidoBkp.vlItemPedido = itemPedido.vlItemPedido;
		itemPedidoBkp.vlPctDesconto = itemPedido.vlPctDesconto;
		itemPedidoBkp.cdTabelaPreco = itemPedido.cdTabelaPreco;
		itemPedidoBkp.setQtItemFisico(itemPedido.getQtItemFisico());
		itemPedidoBkp.vlTotalItemPedido = itemPedido.vlTotalItemPedido;
		itemPedidoBkp.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoBkp.nuSeqProduto = itemPedido.nuSeqProduto;
		itemPedidoBkp.flOrigemPedido = itemPedido.flOrigemPedido;
		itemPedidoBkp.nuPedido = itemPedido.nuPedido;
		itemPedidoBkp.cdEmpresa = itemPedido.cdEmpresa;
		itemPedidoBkp.cdRepresentante = itemPedido.cdRepresentante;
		//Produto
		if (itemPedido.getProduto() == null) return;
		Produto prod = new Produto();
		prod.cdProduto = itemPedido.getProduto().cdProduto;
		prod.dsUnidadeFaturamento = itemPedido.getProduto().dsUnidadeFaturamento;
		prod.nuConversaoUnidadesMedida = itemPedido.getProduto().nuConversaoUnidadesMedida;
		//--
		itemPedidoBkp.setProduto(prod);
	}

	//@Override
	protected void salvarClick() throws SQLException {
		if (!itemPedidoEquals((ItemPedido) screenToDomain()) && (grid.getSelectedIndex() != -1)) {
			pedido.updateByClickSalvarItemPedido = true;
			if (TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(getItemPedido().flTipoItemPedido)) {
				if (LavenderePdaConfig.isUsaPrecoBaseItemBonificado()) {
					double vlBase = 0; 
					if (LavenderePdaConfig.isUsaPrecoBaseItemPedidoPrecoBonificado()) {
						vlBase = getItemPedido().vlBaseItemPedido; 
					} else if (LavenderePdaConfig.isUsaPrecoBasePorRedutorCliente()) {
						vlBase = getItemPedido().vlBaseFlex;
					} else {
						vlBase = getItemPedido().getItemTabelaPreco().vlBase;
					}
					if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_BONIFICACAO_PRECO, Convert.toString(vlBase, 2))) == 0) {
						return;
					}
				}
			}
			salvarItemUnitario();
		}
	}

	private void salvarItemUnitario() throws SQLException {
		calcularClick();
		//--
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			super.salvarClick();
		} finally {
			msg.unpop();
		}
	}

	private void calcularClick() throws SQLException {
		ItemPedido itemPedido = (ItemPedido) screenToDomain();
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
		}
		getPedidoService().calculateItemPedido(pedido, itemPedido, true);
		//--
		if (LavenderePdaConfig.isAvisaUsuarioSobreConsumoVerba() && (itemPedido.vlVerbaItem < 0) && ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && itemPedido.getProduto().isUtilizaVerba())) {
    		if (!pedido.isSimulaControleVerba() && !pedido.isIgnoraControleVerba()) {
    			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_CONSUMIDA, itemPedido.vlVerbaItem));
    		}
		}
		if (LavenderePdaConfig.usaControleNoDescontoPromocional && itemPedido.flAbaixoPtReferenciaDesc && itemPedido.permiteAplicarDesconto()) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PCT_DESCPROMOCIONAL, new Object[] { StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), StringUtil.getStringValueToInterface(itemPedido.vlPtReferencialDesc) }));
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			if (itemPedido.pontosMinimoItemNaoAlcancado) {
				UiUtil.showWarnMessage(Messages.MSG_NAO_ALCANCOU_VL_MINS_PONTOS);
			} else if (itemPedido.isVlPrecoCustoNull) {
				UiUtil.showWarnMessage(Messages.MSG_PRODUTO_NAO_POSSUI_VL_CUSTO);
			}
		}
		setDomain(itemPedido);
	}

	//@Override
	protected void beforeSave() throws SQLException {
		ItemPedido itemPedido = (ItemPedido) screenToDomain();
		//--
		CadItemPedidoForm.validateItemPedidoUI(itemPedido, pedido, isEditing());
	}

	//@Override
	protected void onSave() throws SQLException {
		save();
	}

	//@Override
	protected void insertOrUpdate(BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (isEditing()) {
			update(itemPedido);
		} else {
			if (mustDeleteAndInsertItem) {
				itemPedidoBkp = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoBkp.getRowKey());
				if (itemPedidoBkp != null) {
					getPedidoService().validate(pedido);
					getPedidoService().deleteItemPedido(pedido, itemPedidoBkp);
				}
				//--
				getNextNuSeqProduto(itemPedido);
				grid.setItens(getItemPedidoToGrid(itemPedido), grid.getSelectedIndex());
			}
			insert(itemPedido);
		}
	}

	protected void getNextNuSeqProduto(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
			if (LavenderePdaConfig.usaSequenciaInsercaoNuSeqProduto) {
				itemPedido.nuSeqProduto = itemPedido.nuSeqItemPedido;
			} else {
				itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedido, "NUSEQPRODUTO") + 1;
			}
		}
	}

	//@Override
	protected void insert(final BaseDomain domain) throws SQLException {
		getPedidoService().insertItemPedido(pedido, (ItemPedido) domain);
		//--
	}

	//@Override
	protected void update(final BaseDomain domain) throws SQLException {
		getPedidoService().updateItemPedido(pedido, (ItemPedido) domain);
		//--
	}

	//@Override
	protected void afterSave() throws java.sql.SQLException {
		if (grid.gridController.isEnabled(grid.getSelectedIndex(), COL_CDPRODUTO)) {
			gridRowColEnableProdutoAndCheck(grid.getSelectedIndex(), false);
			if (!LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
				grid.setCellText(grid.getSelectedIndex(), COL_BUSCA_PRODUTO, searchIconDisable);
			}
		}
		updateLabelsContainerDados();
	}

	//@Override
	protected void excluirClick() throws SQLException {
		if (grid.getSelectedIndex() != -1) {
			if (delete()) {
				carregaGrid();
				updateLabelsContainerDados();
			}
		} else {
			UiUtil.showInfoMessage(Messages.ITEM_MSG_NENHUM_PEDIDO_SELECIONADO);
			return;
		}
	}

	//@Override
	protected void delete(BaseDomain domain) throws SQLException {
		if (isEditing()) {
			getPedidoService().validate(pedido);
			getPedidoService().deleteItemPedido(pedido, (ItemPedido) domain);
		} else {
			grid.del(grid.getSelectedIndex());
			//--
			if (grid.size() == 0) {
				newItemInGrid();
				lastSelectedRow = grid.getSelectedIndex();
			} else {
				edit(getDomainInGrid());
				grid.setSelectedIndex(grid.size() - 1);
				lastSelectedRow = grid.getSelectedIndex();
				makeItemPedidoTmp();
				//--
				getDsProdutoSelected();
			}
		}
		lastCdProdutoAndRow = new String[] { "", "" };
		//--
	}

	//@Override
	public void afterExceptionThrowedOnEvent(Throwable ex) {
	   try {
		super.afterExceptionThrowedOnEvent(ex);
		if (ex instanceof ValidationException) {
			if (ex.getMessage().equals(ValidationException.EXCEPTION_ABORT_PROCESS) && !LavenderePdaConfig.isAvisaVendaProdutoSemEstoque() && !LavenderePdaConfig.carregaProdutosAutoTelaItemPedidoEstiloDesktop) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLPCTDESCONTO);
				isSavedOk = false;
			} else {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE_EDIT);
				isSavedOk = false;
			}
			if (((ValidationException) ex).params != null) {
				if (((ValidationException) ex).params.equalsIgnoreCase(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO)) {
					grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
				} else if (((ValidationException) ex).params.equalsIgnoreCase(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO)) {
					grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLITEM);
				} else if (((ValidationException) ex).params.equalsIgnoreCase(Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO)) {
					grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLTOTALITEM);
				}
			}
			if (ex.getMessage().equals(Messages.PRODUTO_NAO_ENCONTRADO)) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_CDPRODUTO);
				isProdutoEncontrado = false;
				forceFocusProduto = true;
			} else if (ex.getMessage().indexOf(Messages.ITEMPEDIDO_MSG_CONVERSAO_UNIDADE.substring(0, 30)) != -1) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_QTDADE);
			} else if (ex.getMessage().indexOf(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO.substring(0, 20)) != -1) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLPCTDESCONTO);
			} else if (ex.getMessage().indexOf(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PERMITIDO.substring(0, 20)) != -1) {
				grid.exibeControleGrid(grid.getSelectedIndex(), COL_VLITEM);
			} else if ((ex.getMessage().indexOf(Messages.ITEMPEDIDO_MSG_PRODUTO_DUPLICADO.substring(0, 10)) != -1) && mustDeleteAndInsertItem) {
				if (grid.checkable) {
					grid.setChecked(grid.getSelectedIndex(), TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(itemPedidoBkp.flTipoItemPedido));
				}
				edit(itemPedidoBkp);
				grid.exibeControleGrid(grid.getSelectedIndex(), isEditing() ? COL_QTDADE : COL_CDPRODUTO);
				getDsProdutoSelected();
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	public ProdutoBase getProdutoFilter() throws SQLException {
		ProdutoBase produto;
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			produto = new ProdutoTabPreco();
		} else {
			produto = new Produto();
		}
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			produto.flExcecaoProduto = ValueUtil.isNotEmpty(pedido.getTipoPedido().flExcecaoProduto) ? pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
			produto.cdTipoPedidoFilter = pedido.cdTipoPedido;
		}
		prepareGrupoProdTipoPedFilter(produto);
		return produto;
	}

	private void prepareGrupoProdTipoPedFilter(ProdutoBase produto) throws SQLException {
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
			if (pedido.getTipoPedido() == null) {
				throw new FilterNotInformedException();
			}
			produto.grupoProdTipoPedFilter = new GrupoProdTipoPed();
			produto.grupoProdTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produto.grupoProdTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProdTipoPed.class);
			produto.grupoProdTipoPedFilter.cdTipoPedido = pedido.cdTipoPedido;
			produto.grupoProdTipoPedFilter.excecaoGrupoProdutoFilter = pedido.getTipoPedido().isFlExcecaoGrupoProduto();
		}
	}
}