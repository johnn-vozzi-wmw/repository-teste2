package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationWarnException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelProdutosPendentesWindow extends WmwWindow {

	private GridListContainer listContainer;
	private Pedido pedido;
	private EditMemo edDescricao;
	private ButtonPopup btFecharPedido;
	private ButtonPopup btContinua;
	private String sortAtributte = null;
	private String sortAsc = ValueUtil.VALOR_SIM;
	private Vector itemList;
	public boolean disableBtFecharPedido = false;
	public CadPedidoForm cadPedidoForm;
	public CadItemPedidoForm cadItemPedidoForm;
	public boolean continuaFecharPedido = false;
	private static RelProdutosPendentesWindow newInstance;
	private HashMap<String,String> produtoTipoPedHash = new HashMap<String,String>();

	public boolean habiltaBtContinua = false;
	public boolean continua = false;
	public boolean fromVendaUnitaria;
	public boolean fromCapaPedido;

	private final String SORT_ATRIBUTTE_DEFAULT = "QTPEDIDOSCONTIDO";

	
	public static RelProdutosPendentesWindow getNewInstance(Pedido pedido, boolean disableBtFecharPedido, boolean habiltaBtContinua, Vector itemListPendentes, boolean fromCapaPedido) {
		newInstance = new RelProdutosPendentesWindow(pedido, disableBtFecharPedido, habiltaBtContinua, itemListPendentes, fromCapaPedido);
		return newInstance;
	}
	
	public static RelProdutosPendentesWindow getNewInstance(Pedido pedido, boolean disableBtFecharPedido, boolean habiltaBtContinua, Vector itemListPendentes) {
		return getNewInstance(pedido, disableBtFecharPedido, habiltaBtContinua, itemListPendentes, false);
	}

	public static RelProdutosPendentesWindow getNewInstance(Pedido pedido, boolean disableBtFecharPedido, boolean habiltaBtContinua, boolean fromCapaPedido) {
		return  getNewInstance(pedido, disableBtFecharPedido, habiltaBtContinua, null, fromCapaPedido);
	}
	
	public static RelProdutosPendentesWindow getNewInstance(Pedido pedido, boolean disableBtFecharPedido, boolean habiltaBtContinua) {
		return getNewInstance(pedido, disableBtFecharPedido, habiltaBtContinua, null);
	}

	public static void cleanInstance() {
		 newInstance = null;
	}

	private RelProdutosPendentesWindow(Pedido pedido, boolean disableBtFecharPedido, boolean habiltaBtContinua, Vector itemListPendentes, boolean fromCapaPedido) {
		super(Messages.PEDIDO_PRODUTOS_PENDENTES);
		this.pedido = pedido;
		this.disableBtFecharPedido = disableBtFecharPedido;
		this.habiltaBtContinua = habiltaBtContinua;
		this.fromCapaPedido = fromCapaPedido;
		edDescricao = new EditMemo("@@@@@@@@@@", 3, 255);
		edDescricao.setText(Messages.REL_PRODUTOS_PENDENTES_MSG_NAO_INSERIDOS_PEDIDO);
		edDescricao.setEditable(false);
		edDescricao.transparentBackground = true;
		edDescricao.drawDots = false;
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PED);
		btContinua = new ButtonPopup(Messages.REL_PRODUTOS_PENDENTES_LABEL_CONTINUAR);
		sortAtributte = SORT_ATRIBUTTE_DEFAULT;
		sortAsc = ValueUtil.VALOR_SIM;
		listContainer = new GridListContainer(6, 2, false, false);
		setColPosition();
		listContainer.setColsSort(new String[][] { { Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO" }, { Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO" }, { Messages.PEDIDO_LABEL_QTD_PEDIDOS, SORT_ATRIBUTTE_DEFAULT } });
		this.itemList = itemListPendentes;
		makeUnmovable();
		setDefaultRect();
	}

	public void setColPosition() {
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			setPositionRight(4);
		}
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
			setPositionRight(5);
		}
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			setPositionRight(6);
		}
	}

	private void setPositionRight(double qtItens) {
		if (qtItens % 2 == 0) {
			listContainer.setColPosition((int) qtItens - 1, RIGHT);
		}
	}
	
	public void initUI() {
	   try {
		super.initUI();
		if (listContainer != null) {
			listContainer.atributteSortSelected = sortAtributte;
			listContainer.sortAsc = sortAsc;
		}
		UiUtil.add(this, edDescricao, LEFT + WIDTH_GAP, TOP + WIDTH_GAP, FILL - WIDTH_GAP, PREFERRED + 20);
		UiUtil.add(this, listContainer, LEFT, AFTER + WIDTH_GAP, FILL, FILL);
		if (!disableBtFecharPedido) {
			addButtonPopup(btFecharPedido);
		}
		if (habiltaBtContinua) {
			addButtonPopup(btContinua);
		}
		addButtonPopup(btFechar);
		loadGrid();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			throw new ApplicationWarnException(e.getMessage());
		}
	}

	public void popup() {
		super.popup();
	}

	@Override
	public void unpop() {
		cleanInstance();
		super.unpop();
	}

	private void loadGrid() throws SQLException {
		try {
			int listSize = 0;
			listContainer.removeAllContainers();
			if(itemList == null){
				itemList = ItemPedidoService.getInstance().findProdutosPendentesByCliente(pedido, pedido.cdCliente, pedido.itemPedidoList, false);
			}
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
				produtoTipoPedHash = ProdutoService.getInstance().montaHashProdutosPendentes(ProdutoService.getInstance().getProdutoPendenteListByTipoPedido(pedido.getTipoPedido()));
			}
			listSize = itemList.size();
			Container all[] = new Container[listSize];
			if (listSize > 0) {
				//--
				BaseDomain domain = getDomainFilterSortable();
				ItemPedido.sortAttr = domain.sortAtributte;
				itemList.qsort();
				//--
				BaseListContainer.Item c;
				if (domain.sortAsc == null || domain.sortAsc.startsWith(ValueUtil.VALOR_SIM)) {
					for (int i = 0; i < listSize; i++) {
						domain = (BaseDomain) itemList.items[i];
						all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
						if ((i % 2) == 0) {
							c.setBackColor(Color.darker(c.getBackColor(), 10));
						}
						c.id = ((ItemPedido) domain).cdProduto;
						c.setItens(getItem(domain, pedido));
						c.setToolTip(getToolTip(domain));
						setPropertiesInRowList(c, domain);
						domain = null;
					}
				} else {
					int index = 0;
					for (int i = listSize - 1; i >= 0; i--) {
						domain = (BaseDomain) itemList.items[i];
						all[index] = c = new BaseListContainer.Item(listContainer.getLayout());
						if ((i % 2) == 0) {
							c.setBackColor(Color.darker(c.getBackColor(), 10));
						}
						c.id = ((ItemPedido) domain).cdProduto;
						c.setItens(getItem(domain, pedido));
						c.setToolTip(getToolTip(domain));
						setPropertiesInRowList(c, domain);
						domain = null;
						index++;
					}
				}
				listContainer.addContainers(all);
			} else {
				throw new ValidationException(Messages.PEDIDO_MSG_PEDIDO_SEM_PRODUTOS_PENDENTES);
			}
		} catch (ValidationException ex) {
			if (disableBtFecharPedido && !habiltaBtContinua) {
				throw ex;
			}
		}
	}

	private BaseDomain getDomainFilterSortable() {
		BaseDomain baseDomain = getDomainFilter();
		baseDomain.sortAtributte = sortAtributte;
		baseDomain.sortAsc = sortAsc;
		return baseDomain;
	}

	private BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	private String getToolTip(Object domain) throws SQLException {
		return ((ItemPedido) domain).getDsProduto();
	}
	
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		ItemPedido item = (ItemPedido) domain;
		int color = getGridRowColor(item);
		if (color != -1) {
			c.setBackColor(color);
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			if (produtoTipoPedHash.size() == 0 ) return;
			TipoPedido tipoPedido = pedido.getTipoPedido();
			if (!(produtoTipoPedHash.containsKey(item.cdProduto) ^ tipoPedido.isFlExcecaoProduto())) {
				c.setBackColor(Color.brighter(Color.RED, 230));
			}
		}
	}
	
	public int getGridRowColor(ItemPedido item) throws SQLException {
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && item.estoque.qtEstoque <= 0 && !item.getProduto().isIgnoraValidacao()) {
			return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK;
		}
		return -1;
	}

	protected String[] getItem(Object domain, Pedido pedido) throws java.sql.SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		Produto produto = itemPedido.getProduto();
		Vector vector = new Vector(5);
		vector.addElement(LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : StringUtil.getStringValue(itemPedido.cdProduto) + " - ");
		vector.addElement(StringUtil.getStringValue(itemPedido.getDsProduto()));
		vector.addElement(Messages.PEDIDO_LABEL_QTD_PEDIDOS + ": " + StringUtil.getStringValueToInterface(itemPedido.qtPedidosContido));
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() && produto != null) {
			vector.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsPrincipioAtivo), (int)(width * 0.6), listContainer.getFontSubItens()));
		} else {
			vector.addElement(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && produto != null) {
			if (itemPedido.pedido == null) {
				itemPedido.pedido = pedido;
			}
			String cdTabelaPreco = LavenderePdaConfig.ocultaTabelaPrecoPedido ? TabelaPreco.CDTABELAPRECO_VALOR_ZERO : pedido.cdTabelaPreco;
			vector.addElement(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false)));
		
		} else {
			vector.addElement(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() && produto != null) {
			String lbEstoque = LavenderePdaConfig.isUsaFlIgnoraValidaco() && ValueUtil.getBooleanValue(produto.flIgnoraValidacao) ? ValueUtil.VALOR_NI : EstoqueService.getInstance().getEstoqueToString(itemPedido.estoque.qtEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE;
			vector.addElement(lbEstoque);
		} else {
			vector.addElement(ValueUtil.VALOR_NI);
		}
		return (String[]) vector.toObjectArray();
	}

	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
					if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listContainer.reloadSortSettings();
						sortAtributte = listContainer.atributteSortSelected;
						sortAsc = listContainer.sortAsc;
						loadGrid();
					}
				}
			}
			case ControlEvent.PRESSED: {
				if (event.target == btFecharPedido) {
					continuaFecharPedido = true;
					cadPedidoForm.inItemNegotiationProdutosPendentes = false;
					fecharWindow();
				} else if (event.target == btFechar) {
					cadItemPedidoForm = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
					cadItemPedidoForm.isOcultaProdutoPendenteMenuOpcoes = false;
					cadPedidoForm.inItemNegotiationProdutosPendentes = false;
					cadPedidoForm.getPedido().isAdiconandoItemRelProdutosPendentes = false;
					fecharWindow();
				} else if (event.target == btContinua) {
					cadPedidoForm.inItemNegotiationProdutosPendentes = false;
					cadPedidoForm.getPedido().isAdiconandoItemRelProdutosPendentes = false;
					continua = true;
					fecharWindow();
				}
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listContainer.isEventoClickUnicoDisparado())) {
					CadItemPedidoForm cadItemPedido = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
					Produto produto = getSelectedItem();
					if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
						validateProdutoBloqueadoPorTipoPedido(produto, pedido.getTipoPedido());
					}
					cadItemPedido.fromProdutoPendenteGiroMultInsercao = fromVendaUnitaria;
					if (produto != null) {
						if (!disableBtFecharPedido || habiltaBtContinua) {
							cadItemPedido.add();
							cadItemPedido.flFromCadPedido = true;
							cadPedidoForm.inItemNegotiationProdutosPendentes = true;
							MainLavenderePda.getInstance().show(cadItemPedido);
						} else if (fromVendaUnitaria){
							ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
							listItemPedidoForm.novoClickFromRelProdutosPendentes();
						}
						cadItemPedido.fromRelProdutosPendentes = true;
						cadItemPedido.fromRelProdutosPendentesCapaPedido = this.fromCapaPedido;
						MainLavenderePda.getInstance().show(cadItemPedido);
						fecharWindow();
						cadItemPedido.produtoSelecionado = produto;
						cadItemPedido.gridClickAndRepaintScreen(!cadItemPedido.inVendaUnitariaMode);
						cadItemPedido.doEditOrRefreshItemPedido();
					}
				}
				break;
			}
		}
	}

	private void validateProdutoBloqueadoPorTipoPedido(Produto produto, TipoPedido tipoPedido) {
		if (produtoTipoPedHash.size() == 0) return;
		if (!(produtoTipoPedHash.containsKey(produto.cdProduto) ^ tipoPedido.isFlExcecaoProduto())) {
			throw new ValidationException(Messages.PRODUTO_BLOQUEADO_POR_TIPO_DE_PEDIDO);
		}
	}

	private Produto getSelectedItem() throws SQLException {
		return ProdutoService.getInstance().getProduto(listContainer.getSelectedId());
	}

	public boolean hasProdutosPendentes() {
		return (itemList != null) && (itemList.size() > 0);
	}

}
