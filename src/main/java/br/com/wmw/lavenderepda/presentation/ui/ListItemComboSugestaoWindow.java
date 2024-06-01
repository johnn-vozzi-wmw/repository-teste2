package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.service.ComboService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemComboService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ComboComboBox;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.ListContainerEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListItemComboSugestaoWindow extends WmwListWindow {
	
	private Pedido pedido;
	private CadItemPedidoForm cadItemPedidoForm;
	private ButtonPopup btFecharPedido;
	private ButtonPopup btInserirItens;
	private EditNumberInt edQtdItem;
	public boolean fecharPedidoPressed;
	private Vector domainList;
	private ComboComboBox cbCombo;
	private String cdProdutoSugestao;
	private GridListContainer listContainerSecundario;
	private String cdTabelaPreco;
	private double vlTotalListaPrimaria;
	private int indexQtd;
	private int indexValor;
	private Image iconSimilar;
	private Vector itemComboPrincipalList;
	private Vector itemComboSecundarioList;

	public ListItemComboSugestaoWindow(Pedido pedido, ItemCombo itemCombo, CadItemPedidoForm cadItemPedidoForm, Vector domainList, boolean fromFechamento) throws SQLException {
		super(Messages.COMBO_SUGESTAO_TITLE);
		this.pedido = pedido;
		this.cadItemPedidoForm = cadItemPedidoForm;
		this.domainList = domainList;
		cbCombo = new ComboComboBox();
		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			cbCombo.load(itemCombo, pedido.nuPedido, pedido.flOrigemPedido);
			cdProdutoSugestao = itemCombo.cdProduto;
		}
		listContainer = constructorListContainer(Messages.ITEMCOMBO_PRIMARIO_TITLE);
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			listContainerSecundario = constructorListContainer(Messages.ITEMCOMBO_SECUNDARIO_TITLE);
			listContainerSecundario.setCheckable(true);
			listContainerSecundario.ckCheckAll.setVisible(false);
			listContainerSecundario.setOnlyCheckOne(true);
			listContainerSecundario.setUseTotalizerRight(true);
			listContainer.setColTotalizerRight(4, Messages.ITEMCOMBO_LABEL_TOTAL_COMBO);
			edQtdItem = new EditNumberInt("", 9);
			edQtdItem.setEnabled(true);
			edQtdItem.setValue(1);
		}
		singleClickOn = true;
		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			if (fromFechamento) {
				this.cadItemPedidoForm.fromSugestaoItemComboFechamento = true;
				btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PED);
				this.cadItemPedidoForm.flFromCadPedido = true;
			} else {
				this.cadItemPedidoForm.fromSugestaoItemComboFechamento = false;
			}
			setDefaultRect();
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			useLeftTopIcons = true;
			int size = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
			iconSimilar = UiUtil.getColorfulImage("images/similaridade.png", size, size);
		}
	}
	
	public ListItemComboSugestaoWindow(Pedido pedido, ItemCombo itemCombo, CadItemPedidoForm cadItemPedidoForm, Vector domainList, boolean fromFechamento, String cdTabelaPreco) throws SQLException {
		this(pedido, itemCombo, cadItemPedidoForm, domainList, fromFechamento);
		this.cdTabelaPreco = cdTabelaPreco;
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			cbCombo.load(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.nuPedido, cdTabelaPreco);
		}
		btInserirItens = new ButtonPopup(Messages.BOTAO_INSERIR);
		setDefaultWideRect();
	}

	@Override
	public void popup() {
		if (validateOpen()) {
			super.popup();
		}
	}

	private boolean validateOpen() {
		if (cbCombo != null && cbCombo.size() > 0) {
			return true;
		}
		UiUtil.showWarnMessage(Messages.MSG_SEM_COMBO_DISPONIVEL);
		try {
			this.fecharWindow();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		return false;
	}

	private GridListContainer constructorListContainer(String title) {
		int qtItens = 3;
		qtItens += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
		qtItens += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
		qtItens = LavenderePdaConfig.isExibeComboMenuInferior() ? 6 : qtItens;
		
		GridListContainer listContainer = new GridListContainer(qtItens, 2, false, false, false, true);
		if (qtItens > 3) {
			listContainer.setColPosition(3, RIGHT);
		}
		if (qtItens > 5) {
			listContainer.setColPosition(5, RIGHT);
		}
		listContainer.resizeable = false;
		if (ValueUtil.isNotEmpty(title)) {
			listContainer.setTitle(title);
			listContainer.setTitleFont(UiUtil.defaultFontSmall);
		} else {
			listContainer.setBarTopSimple();
		}
		return listContainer;
	}
	
	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProdutoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemCombo();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		if (cbCombo.getSelectedIndex() > 0 || LavenderePdaConfig.isExibeComboMenuInferior()) {
			String flTipoItemCombo = LavenderePdaConfig.isExibeComboMenuInferior() ? ItemCombo.TIPOITEMCOMBO_PRIMARIO : null;
			if (ValueUtil.isNotEmpty(cbCombo.getValue())) {
				return itemComboPrincipalList = ItemComboService.getInstance().findProdutosSugeridosByComboSummary(pedido, cdProdutoSugestao, cbCombo.getValue(), flTipoItemCombo);
			} else {
				return new Vector();
			}
		}
		return domainList;
	}
	
	@Override
	protected Image[] getIconsLegend(BaseDomain domain) {
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			Produto produto = (Produto) domain;
			if (ValueUtil.getBooleanValue(produto.flAgrupadorSimilaridade)) {
				return new Image[] {iconSimilar};
			}
		}
		return super.getIconsLegend(domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Produto produto = (Produto) domain;
		boolean mostraEstoque = LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido();
		boolean mostraPreco = LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido();
		int qtItens = 3;
		qtItens += mostraPreco ? 1 : 0;
		qtItens += mostraEstoque ? 1 : 0;
		qtItens = LavenderePdaConfig.isExibeComboMenuInferior() ? 6 : qtItens;
		String[] item = new String[qtItens];
		String dsReferencia = StringUtil.getStringValue(produto.dsReferencia);
        final String dsProduto = StringUtil.getStringValue(produto.dsProduto);
        int index = 0;
		if (!LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	item[index++] = produto.cdProduto + " - ";
        	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
        		item[index++] = "[" + dsReferencia + "] " + dsProduto;
        	} else {
        		item[index++] = dsProduto + " [" + dsReferencia + "]";
        	}
        } else if (!LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	item[index++] = produto.cdProduto + " - ";
        	item[index++] = dsProduto;
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
        		item[index++] = "[" + dsReferencia + "] ";
        		item[index++] = dsProduto;
        	} else {
        		item[index++] = dsProduto;
        		item[index++] = " [" + dsReferencia + "]";
        	}
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	item[index++] = produto.toString();
        	item[index++] = "";
        }
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			getItemComboEspecial(produto, item, index);
		} else {
			getItemComboPadrao(produto, mostraEstoque, mostraPreco, item, index);
		}
		return item;
	}

	private void getItemComboPadrao(Produto produto, boolean mostraEstoque, boolean mostraPreco, String[] item, int index) throws SQLException {
		if (mostraEstoque) {
			item[index++] = EstoqueService.getInstance().getEstoqueToString(produto.estoque.qtEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE;
		}
		if (mostraPreco) {
			double vlProduto = ProdutoService.getInstance().getPrecoProduto(produto, pedido.getCondicaoComercial(), pedido.cdTabelaPreco, null); 
			item[index++] = Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlProduto);
		}
		item[index++] = StringUtil.getStringValue(produto.combo);
	}
	
	private void getItemComboEspecial(Produto produto, String[] item, int index) throws SQLException {
		item[index++] = Messages.ITEMCOMBO_LABEL_VALOR + StringUtil.getStringValueToInterface(produto.vlProduto);
		item[indexQtd = index++] = MessageUtil.getMessage(Messages.ITEMCOMBO_LABEL_ITENS, ValueUtil.getIntegerValue(produto.qtItemPedido));
		item[indexValor = index++] = Messages.ITEMCOMBO_LABEL_VLTOTAL + StringUtil.getStringValueToInterface(produto.vlProduto * produto.qtItemPedido);
		double nuConversaoUnidadeMedida = produto.nuConversaoUnidadesMedida <= 0 ? 1 : produto.nuConversaoUnidadesMedida;
		item[index++] = Messages.ITEMCOMBO_VLUNIT_EMB + StringUtil.getStringValueToInterface(produto.vlProduto / nuConversaoUnidadeMedida);
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		Item item = (Item)listContainer.getSelectedItem();
		return item.id;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btFecharPedido) {
				fecharPedidoPressed = true;
				btFecharClick();
			} else if (event.target == cbCombo) {
				list();
				if (LavenderePdaConfig.isExibeComboMenuInferior() && edQtdItem != null) {
					edQtdItem.setValue(1);
				}
			} else if (event.target == btInserirItens) {
				btInserirItensClick();
			}
			break;
		case ListContainerEvent.RIGHT_IMAGE_CLICKED_EVENT:
			setTotalizadorListSecundaria(edQtdItem.getValueInt());
			break;
		case ValueChangeEvent.VALUE_CHANGE:
			if (event.target == edQtdItem) {
				atualizaQtdItens();
			}
			break;
		case PenEvent.PEN_UP:
			if (listContainerSecundario != null && (event.target instanceof BaseListContainer.Item) && singleClickOn && (listContainerSecundario.isEventoClickUnicoDisparado())) {
				listContainerSecundario.getBaseListContainer().dispararAcaoClickUnico = false;
				clickItemSecundario();
			}
			break;
		}
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			montaTelaComboPrimariaSecundaria();
		} else {
			UiUtil.add(this, new LabelName(Messages.COMBO_NOME_ENTIDADE), cbCombo, getLeft(), getTop() + HEIGHT_GAP);
			UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		}
		if (btFecharPedido != null) {
			addButtonPopup(btFecharPedido);
		}
		if (btInserirItens != null) {
			addButtonPopup(btInserirItens);
		} 
		addBtFechar();
	}
	
	private void montaTelaComboPrimariaSecundaria() {
		UiUtil.add(this, new LabelName(Messages.COMBO_NOME_ENTIDADE), cbCombo, getLeft(), getTop() + HEIGHT_GAP);
		int widthEd = UiUtil.getButtonPreferredHeight() * 3;
		UiUtil.add(this, edQtdItem, RIGHT - WIDTH_GAP, BOTTOM - WIDTH_GAP, widthEd);
		UiUtil.add(this, new LabelName(Messages.ITEMCOMBO_LABEL_QTITENS), BEFORE, CENTER_OF);
		UiUtil.add(this, listContainerSecundario, LEFT, BEFORE - HEIGHT_GAP, FILL, SCREENSIZE - 3, edQtdItem);
		UiUtil.add(this, listContainer, LEFT, TOP + UiUtil.getButtonPreferredHeight() + HEIGHT_GAP + titleGap + UiUtil.defaultFontSmall.fm.height, FILL, FIT);
	}
	
	@Override
	public void reposition() {
		super.reposition();
		setTotalizadorListSecundaria(edQtdItem.getValueInt());
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			cadItemPedidoForm.cadPedidoForm.mostraSugestaoItemComboOnExibition = false;
		}
		super.btFecharClick();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			Produto produto = (Produto) getSelectedDomain();
			editProduto(produto);
		} else if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			Produto produto = (Produto)((Item)listContainer.getSelectedItem()).domain;
			if (ValueUtil.getBooleanValue(produto.flAgrupadorSimilaridade)) {
				produto.findListCombo = true;
				produto.itemTabelaPreco = new ItemTabelaPreco();
				produto.itemTabelaPreco.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
				produto.cdTabelaPreco = produto.itemTabelaPreco.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(pedido);
				new ListItemProdutoAgrupadorWindow(getItemComboByProdutoAndCombo(produto, (Combo) cbCombo.getSelectedItem(), ItemCombo.TIPOITEMCOMBO_PRIMARIO), produto).popup();
			}
		}
	}

	public ItemCombo getItemComboByProdutoAndCombo(Produto produto, Combo combo, String tipoItemCombo) {
		ItemCombo itemCombo = new ItemCombo();
		itemCombo.cdEmpresa = combo.cdEmpresa;
		itemCombo.cdRepresentante = combo.cdRepresentante;
		itemCombo.cdCombo = combo.cdCombo;
		itemCombo.cdProduto = produto.cdProduto;
		itemCombo.cdTabelaPreco = produto.cdTabelaPreco;
		itemCombo.flTipoItemCombo = tipoItemCombo;
		return itemCombo;
	}
	
	private void editProduto(Produto produto) throws SQLException {
		if (btFecharPedido != null) {
			this.cadItemPedidoForm.cadPedidoForm.mostraSugestaoItemComboOnExibition = true;
			this.cadItemPedidoForm.onFechamentoPedido = true;
			cadItemPedidoForm.add();
			MainLavenderePda.getInstance().show(cadItemPedidoForm);
		}
		cadItemPedidoForm.produtoSelecionado = produto;
		cadItemPedidoForm.gridClickAndRepaintScreen();
		cadItemPedidoForm.setFocusInQtde();
		unpop();
	}
	
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return domain.toString();
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		if (ProdutoService.getInstance().isGrifaProdutoSemEstoqueNaLista((ProdutoBase)domain, null)) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_ITEM_COMBO_ESTOQUE);
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			Produto produto = (Produto)domain;
			if (!ValueUtil.valueEquals(produto.flTipoItemCombo, ItemCombo.TIPOITEMCOMBO_SECUNDARIO)) {
				vlTotalListaPrimaria += produto.vlProduto * produto.qtItemPedido;
			}
			c.itemRightTotalizer = StringUtil.getStringValueToInterface(produto.vlProduto * produto.qtItemPedido);
		}
	}
	
	private void listItemComboSecundario() throws SQLException  {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = null;
		try {
			if (ValueUtil.isNotEmpty(cbCombo.getValue()) && listContainerSecundario != null) {
				listContainerSecundario.removeAllContainers();
				//---
				domainList = itemComboSecundarioList = ItemComboService.getInstance().findProdutosSugeridosByComboSummary(pedido, null, cbCombo.getValue(), ItemCombo.TIPOITEMCOMBO_SECUNDARIO);
				listSize = domainList.size();
				Container all[] = new Container[listSize];
				//--
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
						all[i] = c = new BaseListContainer.Item(listContainerSecundario.getLayout());
						domain = (BaseDomain)domainList.items[i];
						c.id = domain.getRowKey();
						c.domain = domain;
						c.setItens(getItem(domain));
						c.setToolTip(getToolTip(domain));
						c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend, useLeftTopIcons);
						setPropertiesInRowList(c, domain);
						domain = null;
					}
					listContainerSecundario.addContainers(all);
					setTotalizadorListSecundaria(1);
				}
			}
		} finally {
			domainList = null;
			msg.unpop();
		}
	}
	
	@Override
	public void list() throws SQLException {
		vlTotalListaPrimaria = 0d;
		super.list();
		listItemComboSecundario();
	}
	
	private void btInserirItensClick() throws SQLException {
		if (edQtdItem.getValueInt() < 1) {
			throw new ValidationException(Messages.MSG_ERRO_QTD_COMBO);
		}
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			Vector listItensInsercaoSimilares = LavenderePdaConfig.usaAgrupadorSimilaridadeProduto ? getItensInsercaoSimilares() : null;
			Vector listItensSecundariosSelecionados = getListItensSecundarioSelecionados();
			int tipo = ItemPedidoService.getInstance().isPedidoPossuiItemComboAvulso(pedido, cbCombo.getValue(), listItensSecundariosSelecionados, listItensInsercaoSimilares);
			if (tipo != ItemCombo.TIPO_SEM_AVULSO) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.MSG_ERRO_ITEMCOMBO_AVULSO, tipo == ItemCombo.TIPO_ENCONTROU_OUTRA_COMBO ? Messages.MSG_FORMA_OUTRA_COMBO_ITEMCOMBO : Messages.MSG_FORMA_AVULSA_ITEMCOMBO));
				return;
			}
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
				tipo = ItemPedidoAgrSimilarService.getInstance().isPedidoPossuiItensComboAvulsos(pedido, cbCombo.getValue(), listItensSecundariosSelecionados, listItensInsercaoSimilares);
				if (tipo != ItemCombo.TIPO_SEM_AVULSO) {
					UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.MSG_ERRO_ITEMCOMBO_AVULSO, tipo == ItemCombo.TIPO_ENCONTROU_OUTRA_COMBO ? Messages.MSG_FORMA_OUTRA_COMBO_ITEMCOMBO : Messages.MSG_FORMA_AVULSA_ITEMCOMBO));
					return;
				}
			}
			if (ValueUtil.isEmpty(listItensSecundariosSelecionados)) {
				UiUtil.showErrorMessage(Messages.MSG_ERRO_NENHUM_ITEMCOMBO_SECUNDARIO);
				return;
			}
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
				ComboService.getInstance().insertItensComboSimilares(listItensInsercaoSimilares, pedido, cdTabelaPreco, cbCombo.getValue(), edQtdItem.getValueInt());
			} else {
				ComboService.getInstance().insertItensCombo(listItensSecundariosSelecionados, pedido, cdTabelaPreco, cbCombo.getValue(), edQtdItem.getValueInt());
			}
			UiUtil.showSucessMessage(Messages.MSG_COMBO_SELECIONADA_SALVA, UiUtil.DEFAULT_MESSAGETIME_SHORT);
			unpop();
		} finally {
			msg.unpop();
		}
	}
	
	private Vector getListItensSecundarioSelecionados() {
		Vector listSelecionados = new Vector(listContainerSecundario.size());
		int[] indexes = listContainerSecundario.getCheckedItens();
		for (int i : indexes) {
			ItemCombo itemCombo = new ItemCombo();
			itemCombo.cdEmpresa = pedido.cdEmpresa;
			itemCombo.cdRepresentante = pedido.cdRepresentante;
			itemCombo.cdCombo = cbCombo.getValue();
			Item item = (Item)listContainerSecundario.getContainer(i);
			Produto produto = (Produto)item.domain;
			itemCombo.cdProduto = produto.cdProduto;
			itemCombo.vlUnitarioCombo = produto.vlProduto;
			itemCombo.qtItemCombo = ValueUtil.getIntegerValue(produto.qtItemPedido);
			listSelecionados.addElement(itemCombo);
		}
		return listSelecionados;
	}
	
	private void setTotalizadorListSecundaria(int qtd) {
		qtd = qtd < 0 ? 0 : qtd;
		Item selectedItem = (Item)listContainerSecundario.getSelectedItem();
		double vlItemSelecionado = 0d;
		if (selectedItem != null && !listContainerSecundario.isChecked(listContainerSecundario.getSelectedIndex())) {
			Produto produto = (Produto)selectedItem.domain;
			vlItemSelecionado = produto.vlProduto * produto.qtItemPedido;
		}
		listContainerSecundario.setCustomRightTotalizer(Messages.ITEMCOMBO_LABEL_TOTAL_COMBO + StringUtil.getStringValueToInterface((vlTotalListaPrimaria + vlItemSelecionado) * qtd));
	}
	
	private void atualizaQtdItens() {
		int vlQtd = edQtdItem.getValueInt();
		atualizaListContainer(vlQtd, listContainer);
		atualizaListContainer(vlQtd, listContainerSecundario);
		listContainer.updateTotalizerRight();
		setTotalizadorListSecundaria(vlQtd);
	}

	private void atualizaListContainer(int vlQtd, GridListContainer listContainer) {
		int size = listContainer.size();
		for (int i = 0; i < size; i++) {
			Item item = (Item) listContainer.getContainer(i);
			Produto produto = (Produto) item.domain;
			item.items[indexQtd] = MessageUtil.getMessage(Messages.ITEMCOMBO_LABEL_ITENS, ValueUtil.getIntegerValue(produto.qtItemPedido * vlQtd));
			item.items[indexValor] = Messages.ITEMCOMBO_LABEL_VLTOTAL + StringUtil.getStringValueToInterface(produto.vlProduto * produto.qtItemPedido * vlQtd);
			item.itemRightTotalizer = StringUtil.getStringValueToInterface(produto.vlProduto * produto.qtItemPedido * vlQtd);
		}
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			return domain;
		}
		return super.getDomain(domain);
	}
	
	private void clickItemSecundario() throws SQLException {
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			Produto produto = (Produto)((Item)listContainerSecundario.getSelectedItem()).domain;
			if (ValueUtil.getBooleanValue(produto.flAgrupadorSimilaridade)) {
				produto.findListCombo = true;
				produto.itemTabelaPreco = new ItemTabelaPreco();
				produto.itemTabelaPreco.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
				produto.cdTabelaPreco = produto.itemTabelaPreco.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(pedido);
				new ListItemProdutoAgrupadorWindow(getItemComboByProdutoAndCombo(produto, (Combo) cbCombo.getSelectedItem(), ItemCombo.TIPOITEMCOMBO_SECUNDARIO), produto).popup();
			}
		}
	}
	
	private Vector getItensInsercaoSimilares() {
		Vector similaresList = new Vector();
		int size = itemComboPrincipalList.size();
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto) itemComboPrincipalList.items[i];
			if (!ValueUtil.getBooleanValue(produto.flAgrupadorSimilaridade) || ValueUtil.isEmpty(produto.similaresList) || !addSimilares(similaresList, produto.similaresList, produto)) {
				similaresList.addElement(produto);
			}
		}
		Vector itemComboSelecionadosList = getListProdutosSecundarioSelecionados();
		size = itemComboSelecionadosList.size();
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto) itemComboSelecionadosList.items[i];
			if (!ValueUtil.getBooleanValue(produto.flAgrupadorSimilaridade) || ValueUtil.isEmpty(produto.similaresList) || !addSimilares(similaresList, produto.similaresList, produto)) {
				similaresList.addElement(produto);
			}
		}
		return similaresList;
	}
	
	private Vector getListProdutosSecundarioSelecionados() {
		Vector listSelecionados = new Vector(listContainerSecundario.size());
		int[] indexes = listContainerSecundario.getCheckedItens();
		for (int i : indexes) {
			listSelecionados.addElement(itemComboSecundarioList.items[i]);
		}
		return listSelecionados;
	}
	
	private boolean addSimilares(Vector similaresList, Vector similaresProduto, Produto produtoCombo) {
		int size = similaresProduto.size();
		boolean inseriu = false;
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto) similaresProduto.items[i];
			if (produto.qtItemPedido > 0d) {
				inseriu = true;
				produto.vlProduto = produtoCombo.vlProduto;
				similaresList.addElement(produto);
			}
		}
		return inseriu;
	}
}
