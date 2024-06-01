package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelItemPedidoDivergenciaWindow extends WmwListWindow {
	
	private enum TipoItemList {
		DIVERGENCIA_ESTOQUE,
		CONSUMO_VERBA,
		DIVERGENCIA_DESCONTO,
		PRECO_NEOGOCIADO,
		ITEM_INSERIDO,
		ITEM_COM_ADVERTENCIA
	}

	private Pedido pedido;
	private ScrollTabbedContainer scrollTabbedContainer;
	private GridListContainer listDivergenciaEstoqueContainer;
	private GridListContainer listConsumoVerbaItemContainer;
	private GridListContainer listDivergenciaDescontoContainer;
	private GridListContainer listPrecoNegociadoContainer;
	private GridListContainer listInseridosContainer;
	private GridListContainer listItensDivergenciaContainer;
	private ButtonPopup btSelecionar;
	public boolean canSelectProduto;
	public String cdProdutoSelected;

	public RelItemPedidoDivergenciaWindow(String title, Pedido pedido) {
		this(title, pedido, false);
	}

	public RelItemPedidoDivergenciaWindow(String title, Pedido pedido, boolean canSelectProduto) {
		super(title);
		this.canSelectProduto = canSelectProduto;
		this.pedido = pedido;
		scrollable = false;
		Vector tabs = configureTabs();
		scrollTabbedContainer = new ScrollTabbedContainer((String[]) tabs.toObjectArray());
		if (LavenderePdaConfig.getConfigGradeProduto() <= 0) {
			listContainer = new GridListContainer(5, 2);
			listContainer.setColPosition(3, RIGHT);
			listContainer.setUseSortMenu(false);
			listContainer.setBarTopSimple();
		}
		btSelecionar = new ButtonPopup(FrameworkMessages.BOTAO_SELECIONAR);
		setDefaultRect();
	}

	private Vector configureTabs() {
		Vector tabs = new Vector();
		if (addTabNaoInseridos()) {
			tabs.addElement(Messages.REL_ITENS_NAO_INSERIDOS);
		}
		if (addTabDivergenciaEstoque()) {
			listDivergenciaEstoqueContainer = createGridListContainer();
			tabs.addElement(Messages.REL_DIVERGENCIA_ESTOQUE);
		}
		if (addTabVerba()) {
			listConsumoVerbaItemContainer = createGridListContainer();
			tabs.addElement(Messages.REL_DIVERGENCIA_VERBA);
		}
		if (addTabDivergenciaDesconto()) {
			listDivergenciaDescontoContainer = createGridListContainer();
			tabs.addElement(Messages.REL_DIVERGENCIA_DESCONTO);
		}
		if (addTabItensQueMantiveramPrecoNegociado()) {
			listPrecoNegociadoContainer = createGridListContainer();
			tabs.addElement(Messages.REL_DESCACRESC);
		}
		if (addTabItensInseridos()) {
			listInseridosContainer = createGridListContainer();
			listInseridosContainer.usaScroolHorizontal = LavenderePdaConfig.usaScroolLateralListasProdutos;
			tabs.addElement(Messages.REL_ITENS_INSERIDOS);
		}
		if (addTabItensComAdvertencia()) {
			listItensDivergenciaContainer = createGridListContainer();
			listItensDivergenciaContainer.usaScroolHorizontal = true;
			tabs.addElement(Messages.PEDIDO_ITENS_COM_ADVERTENCIA);
		}
		return tabs;
	}
	
	private GridListContainer createGridListContainer() {
		GridListContainer gridListContainer = new GridListContainer(4, 2);
		gridListContainer.setColPosition(3, RIGHT);
		gridListContainer.setUseSortMenu(false);
		gridListContainer.setBarTopSimple();
		return gridListContainer;
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	//@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return pedido.itemPedidoNaoInseridoSugestaoPedList;
	}
	
	@Override
	public void list() throws SQLException {
		if (addTabNaoInseridos()) {
			if (LavenderePdaConfig.getConfigGradeProduto() <= 0) {
				super.list();
			}
		}
	}
	protected String[] getItemAdvertencia(ItemPedido itemPedido) throws SQLException {
		String[] item = {
			StringUtil.getStringValue(itemPedido.cdProduto),
			"-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
			itemPedido.dsMotivoAdvertencia, ""
		};
		return item;
	}
	

	//@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ItemPedido itemPedido = (ItemPedido)domain;
		String[] item = {
			StringUtil.getStringValue(itemPedido.cdProduto),
			"-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
			Messages.ITEMPEDIDO_LABEL_QTITEMFISICO + " " + Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico()),
			Messages.ITEMPEDIDO_LABEL_VLTOTAL + " " + Messages.MOEDA + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido),
			Messages.REL_INSERCAO_ERRO + ": " + StringUtil.getStringValue(itemPedido.dsMotivoItemNaoInseridoSugestaoPedido)
		};
		return item;
	}
	//@Override
	protected String getSelectedRowKey() {
		return listContainer.getValueFromContainer(0, 0);
	}

	//--------------------------------------------------------------

	//@Override
	protected void onFormStart() throws SQLException {
		int tabIndex = 0;
		UiUtil.add(this, scrollTabbedContainer, getTop() + HEIGHT_GAP, footerH);
		if (addTabNaoInseridos()) {
			Container tab1 = scrollTabbedContainer.getContainer(tabIndex);
			if (LavenderePdaConfig.isConfigGradeProduto()) {
				configureItemPedidoGrade(tab1);
			} else {
				UiUtil.add(tab1, listContainer, LEFT, AFTER+HEIGHT_GAP, FILL, FILL);
			}
			tabIndex++;
		}
		if (addTabDivergenciaEstoque()) {
			adicionaContainerNaTela(tabIndex++, listDivergenciaEstoqueContainer);
			loadListContainer(pedido.itemPedidoDivergenciaEstoqueSugestaoPedList, listDivergenciaEstoqueContainer, TipoItemList.DIVERGENCIA_ESTOQUE);
		}
		if (addTabVerba()) {
			adicionaContainerNaTela(tabIndex++, listConsumoVerbaItemContainer);
			loadListContainer(pedido.itemPedidoConsumoVerbaSugestaoPedList, listConsumoVerbaItemContainer, TipoItemList.CONSUMO_VERBA);
		}
		if (addTabDivergenciaDesconto()) {
			adicionaContainerNaTela(tabIndex++, listDivergenciaDescontoContainer);
			loadListContainer(pedido.itemPedidoDivergenciaDescontoSugestaoPedList, listDivergenciaDescontoContainer, TipoItemList.DIVERGENCIA_DESCONTO);
		}
		if (addTabItensQueMantiveramPrecoNegociado()) {
			adicionaContainerNaTela(tabIndex++, listPrecoNegociadoContainer);
			loadListContainer(pedido.itemPedidoPrecoNegociadoList, listPrecoNegociadoContainer, TipoItemList.PRECO_NEOGOCIADO);
		}
		if (addTabItensInseridos()) {
			adicionaContainerNaTela(tabIndex++, listInseridosContainer);
			loadListContainer(pedido.itemPedidoInseridoDivergList, listInseridosContainer, TipoItemList.ITEM_INSERIDO);
		}
		if (addTabItensComAdvertencia()) {
			adicionaContainerNaTela(tabIndex++, listItensDivergenciaContainer);
			loadListContainer(pedido.itemPedidoInseridosAdvertenciaList, listItensDivergenciaContainer, TipoItemList.ITEM_COM_ADVERTENCIA);
		}
		setActiveTab();
	}
	
	private void adicionaContainerNaTela(int tabIndex, GridListContainer gridListContainer) {
		Container tab = scrollTabbedContainer.getContainer(tabIndex);
		UiUtil.add(tab, gridListContainer, LEFT, TOP, FILL, FILL);
	}

	//----------------------------------------------------------------------------
	//Configuração para quando utiliza grade.
	
	private void configureItemPedidoGrade(Container tab1) throws SQLException {
		int size = pedido.itemPedidoNaoInseridoSugestaoPedList.size();
		listContainer = new GridListContainer(4, 2, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
		UiUtil.add(tab1, listContainer, LEFT, getTop(), FILL, FILL);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		Container[] produtos = new Container[size];
		if (size > 0) {
			BaseListContainer.Item c;
			ItemPedido itemPedido;
			for (int i = 0; i < size; i++) {
				itemPedido = (ItemPedido) pedido.itemPedidoNaoInseridoSugestaoPedList.items[i];
				produtos[i] = c = new BaseListContainer.Item(listContainer.getLayout());
				c.setItens(getItem(itemPedido, false));
				setPropertiesGradeInRowList(itemPedido, c, listContainer);
				c.id = itemPedido.getRowKey();
				c.setToolTip(itemPedido.dsMotivoItemNaoInseridoSugestaoPedido);
			}
			listContainer.addContainers(produtos);
		}
	}

	private void setPropertiesGradeInRowList(ItemPedido itemPedido, Item c, GridListContainer listContainer) throws SQLException {
		int errosSize = 0;
		if (!itemPedido.getItemPedidoGradeErroList().isEmpty()) {
			errosSize = itemPedido.getItemPedidoGradeErroList().size();
		}
		if (errosSize > 0) {
			for (int j = 0; j < errosSize; j++) {
				c.addSublistItem(getItem(itemPedido.getItemPedidoGradeErroList().get(j), listContainer));
			}
		} else {
			c.addSublistItem(getItem(itemPedido, true));
		}
	}

	private String[] getItem(ItemPedido itemPedido, boolean isSublist) throws SQLException {
		if (isSublist) {
			return new String[]{
				itemPedido.dsMotivoItemNaoInseridoSugestaoPedido
			};
		} else {
			String dsProduto = itemPedido.getProduto().dsProduto;
			return new String[]{
					StringUtil.getStringValue(itemPedido.cdProduto),
					"-" + StringUtil.getStringValue(dsProduto == null ? itemPedido.dsProduto : dsProduto),
					Messages.ITEMPEDIDO_LABEL_QTITEMFISICO + " " + Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico()),
					Messages.ITEMPEDIDO_LABEL_VLTOTAL + " " + Messages.MOEDA + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido)
			};
		}
	}
	
	private String[] getItem(ItemPedidoGrade itemPedidoGrade, GridListContainer listContainer) throws SQLException {
		String[] item = {
				ItemPedidoGradeService.getInstance().getDescricaoGradeResumida(itemPedidoGrade), itemPedidoGrade.dsMotivoGradeNaoInserida
		};
		if (listContainer.usaScroolHorizontal) {
            int stringWidth = fm.stringWidth((String)item[0]);
            if (stringWidth > listContainer.widthFullItens) {
                listContainer.widthFullItens = stringWidth + 5;
            }
        }
		return item;
	}
	
	//----------------------------------------------------------------------------
	
	private void setActiveTab() {
		scrollTabbedContainer.setActiveTab(0);
	}
	
	private boolean addTabNaoInseridos() {
		return pedido.itemPedidoNaoInseridoSugestaoPedList.size() > 0;
	}

	private boolean addTabDivergenciaEstoque() {
		return (LavenderePdaConfig.isUsaEstoqueDisponivelItemPedidoSugestao() || LavenderePdaConfig.isUsaEstoqueDisponivelItemPedidoReplicacao()) && pedido.itemPedidoDivergenciaEstoqueSugestaoPedList.size() > 0;
	}

	private boolean addTabVerba() {
		return (LavenderePdaConfig.isUsaConfirmacaoVerbaPedidoSugestao() || LavenderePdaConfig.isUsaConfirmacaoVerbaReplicacaoPedido()) && pedido.itemPedidoConsumoVerbaSugestaoPedList.size() > 0;
	}
	
	private boolean addTabItensComAdvertencia() {
		return LavenderePdaConfig.isIgnoraValidacoesPedidoOrcamento() && !pedido.itemPedidoInseridosAdvertenciaList.isEmpty();
	}
	
	private boolean addTabDivergenciaDesconto() {
		return pedido.itemPedidoDivergenciaDescontoSugestaoPedList.size() > 0;
	}
	
	private boolean addTabItensQueMantiveramPrecoNegociado() {
		return this.pedido.isPossuiItensQueMantiveramPrecoNegociado();
	}
	
	private boolean addTabItensInseridos() {
		return ValueUtil.isNotEmpty(pedido.itemPedidoInseridoDivergList);
	}

	protected void addButtons() {
		super.addButtons();
		if (canSelectProduto) {
			addButtonPopup(btSelecionar);
		}
		addButtonPopup(btFechar);
	}
	
	protected void addBtFechar() {
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSelecionar) {
					unpop();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if (canSelectProduto) {
					if (pedido.itemPedidoDivergenciaEstoqueSugestaoPedList.size() > 0 && event.target instanceof BaseListContainer.Item && ((BaseListContainer.Item)event.target).layout == listDivergenciaEstoqueContainer.getLayout() && listDivergenciaEstoqueContainer.isEventoClickUnicoDisparado()) {
						setCdProdutoSelected(listDivergenciaEstoqueContainer);
					} else if (pedido.itemPedidoConsumoVerbaSugestaoPedList.size() > 0 && listConsumoVerbaItemContainer != null && event.target instanceof BaseListContainer.Item && ((BaseListContainer.Item)event.target).layout == listConsumoVerbaItemContainer.getLayout() && listConsumoVerbaItemContainer.isEventoClickUnicoDisparado()) {
						setCdProdutoSelected(listConsumoVerbaItemContainer);
					} else if (pedido.itemPedidoNaoInseridoSugestaoPedList.size() > 0 && event.target instanceof BaseListContainer.Item && ((BaseListContainer.Item)event.target).layout == listContainer.getLayout() && listContainer.isEventoClickUnicoDisparado()) {
						setCdProdutoSelected(listContainer);
					}
				}
				break;
			}
		}
	}

	private void setCdProdutoSelected(GridListContainer listContainer) {
		if (listContainer.getSelectedIndex() != -1) {
			BaseListContainer.Item c = (Item) listContainer.getSelectedItem();
			cdProdutoSelected = c.getItem(0);
		}
	}

	private void loadListContainer(Vector itemPedidoList, GridListContainer listContainer, TipoItemList tipo) throws SQLException {
		listContainer.removeAllContainers();
		int size = itemPedidoList.size();
		if (size > 0) {
			Container[] all = new Container[size];
			BaseListContainer.Item c;
			ItemPedido itemPedido;
			for (int i = 0; i < size; i++) {
				all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
				if (i % 2 == 0) {
					c.setBackColor(Color.darker(c.getBackColor(), 10));
				}
				itemPedido = (ItemPedido) itemPedidoList.items[i];
				c.setItens(getItemForList(itemPedido, tipo));
				if (tipo == TipoItemList.ITEM_COM_ADVERTENCIA) {
					c.setToolTip(itemPedido.dsMotivoAdvertencia);
				}
				if (tipo == TipoItemList.ITEM_INSERIDO && LavenderePdaConfig.isConfigGradeProduto()) {
					setPropertiesGradeInRowList(itemPedido, c, listContainer);
				}
			}
			itemPedido = null;
			listContainer.addContainers(all);
		}
	}

	private String[] getDivergenciaEstoqueItem(ItemPedido itemPedido) throws SQLException {
		String[] item = {
			StringUtil.getStringValue(itemPedido.cdProduto),
			"-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
			Messages.PRODUTOKIT_LABEL_QTITEMFISICO_ORIG + " " + Util.getQtItemPedidoFormatted(itemPedido.qtItemFisicoNaoInseridoSugestaoPedido),
			Messages.PRODUTOKIT_LABEL_QTITEMFISICO_ATUAL + " " + Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico())
		};
		return item;
	}
	
	private String[] getDivergenciaDescontoItem(ItemPedido itemPedido) throws SQLException {
		String[] item = {
				StringUtil.getStringValue(itemPedido.cdProduto),
				"-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
				Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO_ORIG + " " + StringUtil.getStringValueToInterface(itemPedido.vlPctDescontoNaoInseridoSugestaoPedido),
				Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO_ATUAL + " " + StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto)
			};
			return item;
	}
	
	private String[] precoNegociadoItem(ItemPedido itemPedido) throws SQLException {
		String[] item = {
				StringUtil.getStringValue(itemPedido.cdProduto),
				"-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
				descricaoOrigem(itemPedido.vlPctDescInicial, itemPedido.vlPctAcrescInicial),
				descricaoAtual(itemPedido.vlPctDesconto, itemPedido.vlPctAcrescimo)
			};
			return item;
	}
	
	private String descricaoOrigem(double vlPctDescOrigem, double vlPctAcresOrigem) {
		if (vlPctDescOrigem > 0) {
			return Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO_ORIG + " " + StringUtil.getStringValueToInterface(vlPctDescOrigem);
		}
		if (vlPctAcresOrigem > 0) {
			return Messages.ITEMPEDIDO_LABEL_VLPCTACRESCIMO_ORIG + " " + StringUtil.getStringValueToInterface(vlPctAcresOrigem);
		}
		return Messages.ITEMPEDIDO_LABEL_SEM_DESCONTO_ACRESCIMO_ORIG;
	}
	
	private String descricaoAtual(double vlPctDescAtual, double vlPctAcresAtual) {
		if (vlPctDescAtual > 0) {
			return Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO_ATUAL + " " + StringUtil.getStringValueToInterface(vlPctDescAtual);
		}
		
		return Messages.ITEMPEDIDO_LABEL_VLPCTACRESCIMO_ATUAL + " " + StringUtil.getStringValueToInterface(vlPctAcresAtual);
	}

	private String[] getConsumoVerbaItem(ItemPedido itemPedido) throws SQLException {
		String[] item = {
			StringUtil.getStringValue(itemPedido.cdProduto),
			"-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
			Messages.REL_DIVERGENCIA_VERBA_ORIG + " " + StringUtil.getStringValuePositivo(itemPedido.vlVerbaItemSugestaoPedido),
			Messages.REL_DIVERGENCIA_VERBA_ATUAL + " " + StringUtil.getStringValuePositivo(itemPedido.vlVerbaItem)
		};
		return item;
	}

	//@Override
	protected String getToolTip(BaseDomain domain) throws java.sql.SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		return StringUtil.getStringValue(itemPedido.dsMotivoItemNaoInseridoSugestaoPedido);
	}
	
	@Override
	protected void onUnpop() {
		if (addTabNaoInseridos()) {
			super.onUnpop();
		}
	}
	
	private String[] getItemForList(ItemPedido itemPedido, TipoItemList tipo) throws SQLException {
		String[] item = null;
		switch (tipo) {
			case DIVERGENCIA_ESTOQUE:
				item = getDivergenciaEstoqueItem(itemPedido);
				break;
			case CONSUMO_VERBA:
				item = getConsumoVerbaItem(itemPedido);
				break;
			case DIVERGENCIA_DESCONTO:
				item = getDivergenciaDescontoItem(itemPedido);
				break;
			case PRECO_NEOGOCIADO:
				item = precoNegociadoItem(itemPedido);
				break;
			case ITEM_INSERIDO:
				item = getItem(itemPedido);
				break;
			case ITEM_COM_ADVERTENCIA:
				item = getItemAdvertencia(itemPedido);
				break;
		}
		return item;
	}
	
}
