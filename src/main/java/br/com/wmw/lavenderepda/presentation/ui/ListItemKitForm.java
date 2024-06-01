package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.KitService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.KitComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListItemKitForm extends BaseCrudListForm {

	private KitComboBox cbKit;
	private ButtonAction btSalvar;
	private int qtItensKit;
	private int qtKit;
	private EditNumberInt edQtItensKit;
	private EditNumberInt edQtKit;
	private Pedido pedido;
	private TabelaPrecoComboBox cbTabelaPreco;

	private Kit kit;
	private int indexQtd, indexValor;

	private CadItemPedidoForm cadItemPedidoForm;
	public boolean editing;
	private CadPedidoForm cadPedidoForm;

	private Image iconSimilar;

	private HashMap<String, Vector> produtoSimilarHash;

	public ListItemKitForm(Pedido pedido) throws SQLException {
		super(Messages.ITEMKIT_TITULO_CADASTRO);
		this.pedido = pedido;
		criaComponentes();
		if (LavenderePdaConfig.isUsaKitTipo3() && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			singleClickOn = true;
			useLeftTopIcons = true;
			int size = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
			iconSimilar = UiUtil.getColorfulImage("images/similaridade.png", size, size);
		}
		if (!LavenderePdaConfig.isUsaKitTipo3() && pedido.cdTabelaPreco != null) cbKit.loadKit1e2(pedido);
	}

	private void criaComponentes() throws SQLException {
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			cbKit = new KitComboBox(pedido);
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
				produtoSimilarHash = new HashMap<>();
			}
			indexQtd = 3;
			indexValor = 5;
		} else {
			cbKit = new KitComboBox(pedido);
		}
		cbKit.setID("cbKit");
		btSalvar = new ButtonAction(FrameworkMessages.BOTAO_SALVAR, "images/ok.png");
		btSalvar.setID("btSalvar");
		edQtItensKit = new EditNumberInt("99999", 4);
		edQtItensKit.setID("edQtItensKit");
		edQtKit = new EditNumberInt("99999", 4);
		edQtKit.setID("edQtKit");
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbTabelaPreco.setID("cbTabelaPreco");
		btExcluir = new ButtonAction(FrameworkMessages.BOTAO_EXCLUIR, "images/delete.png", ColorUtil.buttonExcluirForeColor);
		btExcluir.setID("btExcluir");
		createGrid();
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			constructorListContainer();
			edQtKit.setValue(1);
		}
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		int itemCount = 6;
		if (LavenderePdaConfig.isApresentaPrecoTabela()) {
			itemCount++;
		}
		if (LavenderePdaConfig.isPermiteItemBonificado()) {
			itemCount++;
		}
		listContainer = new GridListContainer(itemCount, 2, false, false, false, true);
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(5, RIGHT);
		if (LavenderePdaConfig.isPermiteItemBonificado()) {
			listContainer.setColPosition(itemCount - 1, RIGHT);
		}
		listContainer.setTitle(Messages.KIT_TIPO_3_LISTA_ITENS_TITLE);
		listContainer.setColTotalizerRight(3, Messages.KIT_TIPO_3_LISTA_TOTALIZADOR_TT_KIT);
		listContainer.setUseTotalizerRight(true);
		listContainer.sortAsc = sortAsc;
		listResizeable = false;
		ScrollPosition.AUTO_HIDE = true;
	}

	public ListItemKitForm(Kit kit, Pedido pedido) throws SQLException {
		super(Messages.CAD_ITEM_PEDIDO_BOTAO_EDITAR_KIT);
		this.kit = kit;
		this.pedido = pedido;
		criaComponentes();
		if (pedido.cdTabelaPreco != null) cbKit.loadKitByTabPreco(pedido.cdTabelaPreco);
		alteraKitEDesabilitaCombo();
		if (kit.kitNaoExiste) UiUtil.showInfoMessage(Messages.KIT_MSG_ERRO_KIT_INEXISTENTE);
	}

	private void alteraKitEDesabilitaCombo() throws SQLException {
		cbKit.setSelectedItem(kit);
		cbKitChange();
		cbKit.setEnabled(false);
		cbKit.drawBackgroundWhenDisabled = false;
	}
	
	//@Override
	protected CrudService getCrudService() {
		return ItemKitService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
    	return new ItemKit();
    }

	@Override
	protected Vector getDomainList() throws java.sql.SQLException {
		ItemKit itemKitFilter = new ItemKit();
		itemKitFilter.cdKit = cbKit.getValue();
		if (itemKitFilter.cdKit == null) {
			return new Vector();
		}
		itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemKitFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector list;
		if (isEditing()) {
			itemKitFilter.nuPedido = pedido.nuPedido;
			list = ((ItemKitService)getCrudService()).findAllKitByItemPedido(itemKitFilter);
			if (list != null) {
				ItemKit itemKit = (ItemKit) list.items[0];
				qtKit = ValueUtil.getIntegerValue(LavenderePdaConfig.usaConversaoUnidadesMedida ?  itemKit.qtItemFaturamentoItemPedido / itemKit.qtMinItem : itemKit.qtItemFisicoItemPedido / itemKit.qtMinItem);
			}
		} else {
			list = getCrudService().findAllByExample(itemKitFilter);
		}
		qtItensKit = list.size();
		filterForEstoque(list);
		return list;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		ItemKit itemKitFilter = new ItemKit();
		itemKitFilter.cdKit = cbKit.getValue();
		if (itemKitFilter.cdKit == null) {
			return new Vector();
		}
		itemKitFilter.cdEmpresa = pedido.cdEmpresa;
		itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemKitFilter.cdTabelaPreco = pedido.cdTabelaPreco;
		itemKitFilter.cdCliente = pedido.cdCliente;
		Vector list = ItemKitService.getInstance().findItemKitJoinProduto(itemKitFilter);
		filterForEstoque(list);
		return list;
	}
	
	@Override
	public void loadDefaultFilters() throws SQLException {
		super.loadDefaultFilters();
		if (kit == null) {
			cbKit.setSelectedIndex(0);
		}
	}

	private Vector filterForEstoque(Vector list) throws SQLException {
		int size = list.size();
		String cdLocalEstoque = LavenderePdaConfig.isUsaLocalEstoque() ? pedido.getCdLocalEstoque() : null;
		for (int i = 0; i < size; i++) {
			ItemKit itemKit = (ItemKit) list.items[i];
			if (ProdutoService.getInstance().produtoSemEstoque(ProdutoService.getInstance().getProduto(itemKit.cdProduto), cdLocalEstoque)) {
				itemKit.semEstoque = true;
			}
		}
		return list;
	}

	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}

	@Override
	public void detalhesClick() throws SQLException {
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			ItemKit itemKit = (ItemKit) getSelectedDomain();
			Produto produto = ProdutoService.getInstance().getProduto(itemKit.cdProduto);
			if (ValueUtil.getBooleanValue(itemKit.flAgrupadorSimilaridade)) {
				produto.qtItemPedido = itemKit.qtItemKit;
				Vector similaresList = produtoSimilarHash.get(itemKit.rowKey);
				if (similaresList == null) {
					produto.findListCombo = true;
					produto.itemTabelaPreco = new ItemTabelaPreco();
					produto.itemTabelaPreco.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
					produto.cdTabelaPreco = produto.itemTabelaPreco.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(pedido);
					similaresList = ProdutoService.getInstance().findProdutosAgrupador(produto, true, null, isConsideraEstoqueKit(itemKit));
				}
				produtoSimilarHash.put(itemKit.rowKey, similaresList);
				produto.similaresList = similaresList;
				new ListItemProdutoAgrupadorWindow(itemKit, produto, null).popup();
				produtoSimilarHash.put(itemKit.rowKey, produto.similaresList);
			}
		} else {
			super.detalhesClick();
		}
	}
	
	private boolean isConsideraEstoqueKit(ItemKit itemKit) throws SQLException {
		Kit kit = KitService.getInstance().getKit(itemKit);
		return kit.isValidaEstoque();
	}

	@Override
	protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
		if (ValueUtil.getBooleanValue(((ItemKit) domain).flAgrupadorSimilaridade) && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			return new Image[] {iconSimilar};
		}
		return super.getIconsLegend(domain);
	}

	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemKit itemKit = (ItemKit) domain;
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			return getItemTipo3(itemKit);
		}
		return LavenderePdaConfig.isUsaKitProdutoFechado() ? getItemFechado(itemKit) : getItemAberto(itemKit);
	}

	private String[] getItemTipo3(ItemKit itemKit) throws SQLException {
		Vector item = new Vector(5);
		ProdutoService.getInstance().addDescricaoProdutoLista(itemKit.cdProduto, item);
		item.addElement(MessageUtil.getMessage(Messages.ITEMKIT_VL_EMB_KIT, StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit)));
		item.addElement(MessageUtil.getMessage(Messages.ITEMKIT_QT_ITEM_KIT, StringUtil.getStringValueToInterface(itemKit.qtItemKit)));
		if (itemKit.nuConversaoUnidadesMedida > 0) {
			item.addElement(MessageUtil.getMessage(Messages.ITEMKIT_VL_UNIT_EMB_KIT, StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit / itemKit.nuConversaoUnidadesMedida)));		
		} else {
			item.addElement(MessageUtil.getMessage(Messages.ITEMKIT_VL_UNIT_EMB_KIT, StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit)));	
		}
		item.addElement(MessageUtil.getMessage(Messages.ITEMKIT_VL_TOTAL, StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit * itemKit.qtItemKit)));
		if (LavenderePdaConfig.isApresentaPrecoTabela()) {
			item.addElement(MessageUtil.getMessage(Messages.ITEMKIT_VLUNITARIO_TABPRECO, StringUtil.getStringValueToInterface(itemKit.vlUnitario)));
		}
		if (LavenderePdaConfig.isPermiteItemBonificado()) {
			item.addElement(itemKit.isBonificado() ? Messages.ITEMKIT_BONIFICADO : ValueUtil.VALOR_NI);
		}
		return (String[]) item.toObjectArray();
	}

	@Override
	protected void setPropertiesInRowList(BaseListContainer.Item c, BaseDomain domain) {
		Kit kit = cbKit.getKit();
		ItemKit itemKit = (ItemKit) domain;
		if (itemKit.isBonificado()) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_ITEMKIT_BONIFICADO);
		}
		if ((kit == null || kit.isValidaEstoque()) && itemKit.semEstoque) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_KIT_SEM_ESTOQUE);
		}
		if (!itemKit.isBonificado()) {
			c.itemRightTotalizer = StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit * itemKit.qtItemKit);
		}
	}
	
	
	private String[] getItemAberto(ItemKit itemKit) throws SQLException {
		return new String[]{
				StringUtil.getStringValue(itemKit.rowKey),
				StringUtil.getStringValue(itemKit.cdProduto),
				StringUtil.getStringValue(ProdutoService.getInstance().getDescriptionWithId(itemKit.cdProduto)),
				StringUtil.getStringValueToInterface(itemKit.qtMinItem)};
	}

	private String[] getItemFechado(ItemKit itemKit) throws SQLException {
		if(LavenderePdaConfig.isUsaUnidadeAlternativaKitProduto()) {
			return new String[]{
					StringUtil.getStringValue(itemKit.rowKey),
					StringUtil.getStringValue(itemKit.cdProduto),
					StringUtil.getStringValue(ProdutoService.getInstance().getDescriptionWithId(itemKit.cdProduto)),
					StringUtil.getStringValueToInterface(itemKit.qtMinItem),
					StringUtil.getStringValue(itemKit.cdUnidade),
					StringUtil.getStringValueToInterface(itemKit.vlPctDesconto)};
		}
		return new String[]{
				StringUtil.getStringValue(itemKit.rowKey),
				StringUtil.getStringValue(itemKit.cdProduto),
				StringUtil.getStringValue(ProdutoService.getInstance().getDescriptionWithId(itemKit.cdProduto)),
				StringUtil.getStringValueToInterface(itemKit.qtMinItem),
				StringUtil.getStringValueToInterface(itemKit.vlPctDesconto)};
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			return super.getSelectedRowKey();
		} else {
			String[] item = gridEdit.getSelectedItem();
			return item[0];
		}
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(barBottomContainer, btSalvar, 5);
		UiUtil.add(this, new LabelName(Messages.KIT_LABEL_CDKIT),  cbKit, getLeft(), getTop() + HEIGHT_GAP);
		if (isComboTabPrecoVisible())  {
			UiUtil.add(this, new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE), LEFT + WIDTH_GAP, AFTER  + HEIGHT_GAP);
			UiUtil.add(this, cbTabelaPreco, AFTER, SAME);
		}
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - UiUtil.getControlPreferredHeight() - barBottomContainer.getHeight() - HEIGHT_GAP * 2);
		} else {
			UiUtil.add(this, new LabelName(Messages.ITEMKIT_NOME_ENTIDADE), LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			UiUtil.add(this, gridEdit);
			gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL - UiUtil.getControlPreferredHeight() - barBottomContainer.getHeight() - HEIGHT_GAP * 2);			UiUtil.add(this, edQtItensKit, RIGHT - WIDTH_GAP, barBottomContainer.getY() - UiUtil.getControlPreferredHeight() - HEIGHT_GAP, PREFERRED);
			edQtItensKit.setEditable(false);
			UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_QTITENS), BEFORE - WIDTH_GAP, SAME + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isUsaKitProdutoFechado() || LavenderePdaConfig.isUsaKitTipo3()) {
			if (LavenderePdaConfig.isUsaKitTipo3()) {
				UiUtil.add(this, edQtKit, RIGHT - WIDTH_GAP, AFTER + HEIGHT_GAP, PREFERRED);
			} else {
				UiUtil.add(this, edQtKit, BEFORE - WIDTH_GAP, barBottomContainer.getY() - UiUtil.getControlPreferredHeight() - HEIGHT_GAP, PREFERRED);
				if (isEditing()) {
					UiUtil.add(barBottomContainer, btExcluir, 1);
				}
			}
			UiUtil.add(this, new LabelName(Messages.ITEMPEDIDO_LABEL_QTKIT), BEFORE - WIDTH_GAP, CENTER_OF);
		}
	}

	private void createGrid() {
        int oneChar = fm.charWidth('A');
		gridEdit = UiUtil.createGridEdit(getGridColDefinition(oneChar));
		gridEdit.setID("gridEdit");
		gridEdit.setGridControllable();
	}

	private GridColDefinition[] getGridColDefinition(int oneChar) {
		return LavenderePdaConfig.isUsaKitProdutoFechado() ? getGridColDefinitionKitFechado(oneChar) : getGridColDefinitionKitAberto(oneChar);
	}

	private GridColDefinition[] getGridColDefinitionKitAberto(int oneChar) {
		return new GridColDefinition[]{
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, oneChar * 8, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_DSPRODUTO, oneChar * 25, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, oneChar * 3, LEFT)};
	}
	
	private GridColDefinition[] getGridColDefinitionKitFechado(int oneChar) {
		if(LavenderePdaConfig.isUsaUnidadeAlternativaKitProduto()) {
			return new GridColDefinition[]{
					new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
					new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, oneChar * 8, LEFT),
					new GridColDefinition(Messages.PRODUTO_LABEL_DSPRODUTO, oneChar * 23, LEFT),
					new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, oneChar * 5, LEFT),
					new GridColDefinition(Messages.ITEMPEDIDO_LABEL_UNIDADE_ALTERNATIVA, oneChar * 5, LEFT),
				    new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO, oneChar * 4, LEFT)};
		}
		return new GridColDefinition[]{
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, oneChar * 8, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_DSPRODUTO, oneChar * 23, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, oneChar * 5, LEFT),
		    new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO, oneChar * 4, LEFT)};
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbKit) {
					cbKitChange();
				} else if (event.target == btSalvar) {
					adicionarKitClick();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE:
				if (event.target == edQtKit && LavenderePdaConfig.isUsaKitTipo3()) {
					atualizaValoresListContainer();
				}
				break;
		}
	}

	public void cbKitChange() throws SQLException {
		list();
		edQtItensKit.setValue(qtItensKit);
		if (!LavenderePdaConfig.isUsaKitTipo3()) {
			grifaProdutoSemEstoque();
			edQtKit.setValue(qtKit);
		}
		carregaComboTabelaPreco();
	}
	
	private void grifaProdutoSemEstoque() throws SQLException {
		Vector list = getDomainList();
		int size = gridEdit.size();
		for (int i = 0; i < size; i++) {
			gridEdit.getItem(i);
			ItemKit itemKit = (ItemKit) list.elementAt(i);
			if (itemKit.semEstoque) {
				gridEdit.gridController.setRowBackColor(LavendereColorUtil.COR_FUNDO_KIT_SEM_ESTOQUE, i);
			}

		}
	}

	private void carregaComboTabelaPreco() throws SQLException {
		if (isComboTabPrecoVisible()) {
			cbTabelaPreco.loadTabelaPrecoPorKit(cbKit.getValue());
			cbTabelaPreco.setSelectedIndex(0);
		}
		
	}

	public void adicionarKitClick() throws SQLException {
		if (cbKit.getValue() == null) {
			throw new ValidationException(Messages.KIT_MSG_CDKIT);
		}
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			adicionaKitTipo3();
		} else if (LavenderePdaConfig.isUsaKitProdutoFechado() && edQtKit.getValueInt() == 0) {
			throw new ValidationException(Messages.KIT_MSG_QTKIT);
		} else if (isEditing()) {
			ItemPedidoService.getInstance().updateItemPedidoByItemKit(pedido, gridEdit.getItemsVector(), cbKit.getKit(), edQtKit.getValueInt());
			PedidoService.getInstance().atualizaPedido(pedido);
			voltarParaListaItemPedido();
		} else {
			ItemPedidoService.getInstance().validaSeKitJaFoiAdicionadoAoPedido(pedido, cbKit.getValue());
			PedidoService.getInstance().addItemKitList(pedido, gridEdit.getItemsVector(), cbTabelaPreco.getValue(), edQtKit.getValueInt(), cbKit.getKit());
			UiUtil.showSucessMessage(Messages.KIT_MSG_SUCESSO, UiUtil.DEFAULT_MESSAGETIME_SHORT);
			close();
			cadItemPedidoForm.clearScreen();
		}
	}

	private void adicionaKitTipo3() throws SQLException {
		try {
			UiUtil.showProcessingMessage();
			Vector itemKitList = getDomainList(new ItemKit());
			if (edQtKit.getValueInt() == 0) {
				throw new ValidationException(Messages.KIT_MSG_QTKIT);
			}
			String cdKit = cbKit.getValue();
			Kit kit = KitService.getInstance().getKit(cdKit, pedido);
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
				applyProdutoSimilarHash(itemKitList);
				if (kit.isValidaEstoque()) {
					ItemKitService.getInstance().validateEstoque(itemKitList);
				}
				Vector itemKitFinal = ItemKitService.getInstance().validateItemKitAtreladoJaInseridoSimilares(pedido, itemKitList, cdKit);
				ItemKitService.getInstance().insertOrUpdateItemKitSimilares(pedido, itemKitFinal, edQtKit.getValueInt());
			} else {
				if (kit.isValidaEstoque()) {
					ItemKitService.getInstance().validateEstoque(itemKitList);
				}
				ItemKitService.getInstance().validateItemKitAtreladoJaInserido(pedido, itemKitList, cdKit);
				if (KitService.getInstance().kitJaFoiAdicionado(pedido, cdKit)) {
					PedidoService.getInstance().addQtAdicionalKitTipo3(pedido, itemKitList, edQtKit.getValueInt());
				} else {
					PedidoService.getInstance().addItemKitTipo3(pedido, itemKitList, edQtKit.getValueInt());
				}
			}
			UiUtil.showSucessMessage(Messages.KIT_MSG_SUCESSO, UiUtil.DEFAULT_MESSAGETIME_SHORT);
			close();
			if (cadPedidoForm != null) {
				cadPedidoForm.updateVlTotalPedido();
				cadPedidoForm.refreshComponents();
			}
			if (cadItemPedidoForm != null && editing) {
				cadItemPedidoForm.voltarClick();
			}
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	private void applyProdutoSimilarHash(Vector itemKitList) {
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			int size = itemKitList.size();
			for (int i = 0; i < size; i++) {
				ItemKit itemKit = (ItemKit) itemKitList.items[i];
				itemKit.similaresList = produtoSimilarHash.get(itemKit.rowKey);
			}
		}
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	public void setCadPedidoForm(CadPedidoForm cadPedidoForm) {
		this.cadPedidoForm = cadPedidoForm;
	}

	private boolean isComboTabPrecoVisible() {
	  	return !LavenderePdaConfig.usaSelecaoPorGrid() && (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() || LavenderePdaConfig.usaListaColunaPorTabelaPreco || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido;
	}

	private boolean isEditing() {
		return kit != null;
	}

	@Override
	protected void excluirClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.LIST_ITEM_KIT_EXCLUIR_KIT_PEDIDO)) {
			Vector itemPedidoList = pedido.itemPedidoList;
			Vector itemPedidoKitFechadoList = new Vector();
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				if (ValueUtil.valueEquals(kit.cdKit, itemPedido.cdKit)) {
					itemPedidoKitFechadoList.addElement(itemPedido);
				}
			}
			size = itemPedidoKitFechadoList.size();
			for (int i = 0; i < size; i++) {
				cadItemPedidoForm.delete((ItemPedido) itemPedidoKitFechadoList.items[i]);
			}
			voltarParaListaItemPedido();
		}
	}

	private void atualizaValoresListContainer() {
		int qtItem = edQtKit.getValueInt();
		qtItem = qtItem <= 0 ? 1 : qtItem;
		int size = listContainer.size();
		for (int i = 0; i < size; i++) {
			Item item = (Item) listContainer.getContainer(i);
			ItemKit itemKit = (ItemKit) item.domain;
			item.items[indexQtd] = MessageUtil.getMessage(Messages.ITEMKIT_QT_ITEM_KIT, StringUtil.getStringValueToInterface(itemKit.qtItemKit * qtItem));
			item.items[indexValor] = MessageUtil.getMessage(Messages.ITEMKIT_VL_TOTAL, StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit * itemKit.qtItemKit * qtItem));
			if (!itemKit.isBonificado()) {
				item.itemRightTotalizer = StringUtil.getStringValueToInterface(itemKit.vlUnitarioKit * itemKit.qtItemKit * qtItem);
			}
		}
		listContainer.updateTotalizerRight();
	}

	private void voltarParaListaItemPedido() throws SQLException {
		voltarClick();
		cadItemPedidoForm.voltarEListarMantendoScroll((ListItemPedidoForm) cadItemPedidoForm.getBaseCrudListForm());
	}



}
