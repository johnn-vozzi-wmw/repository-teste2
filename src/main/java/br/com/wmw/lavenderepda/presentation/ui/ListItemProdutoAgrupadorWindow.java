package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemComboAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemKitAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.KitService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.font.Font;
import totalcross.util.Vector;

public class ListItemProdutoAgrupadorWindow extends WmwListWindow {

	private final boolean fromKit;
	private final Produto produtoOriginal;
	private final ButtonPopup btSalvar;
	private final EditFiltro edFiltro;
	private final HashMap<String, Produto> produtoHash = new HashMap<>();
	private ItemPedido itemPedido;

	private final int column_key = 2;
	private final int column_qt = 1;
	private int valueOld = 0;
	
	public boolean saved;
	public boolean itensAutorizados;

	public ItemCombo itemCombo;
	public ItemKit itemKit;
	private LabelValue lbTotal;
	
	public ListItemProdutoAgrupadorWindow(ItemPedido itemPedido) throws SQLException {
		this(null, itemPedido.getProduto(), itemPedido);
		produtoOriginal.itensAutorizados = true;
		produtoOriginal.nuPedidoFilter = itemPedido.nuPedido;
		produtoOriginal.flOrigemPedidoFilter = itemPedido.flOrigemPedido;
		this.itemPedido = itemPedido;
		itensAutorizados = true;
		if (itemPedido.isAgrupadorSimilaridade()) loadProdutoHash();
		setDefaultRect();
	}

	public ListItemProdutoAgrupadorWindow(ItemCombo itemCombo, Produto produto) throws SQLException {
		this(null, produto, null);
		this.itemCombo = itemCombo;
		setDefaultRect();
	}

	public ListItemProdutoAgrupadorWindow(ItemKit itemKit, Produto produtoOriginal, ItemPedido itemPedido) throws SQLException {
		super(produtoOriginal.toString());
		this.produtoOriginal = produtoOriginal;
		this.fromKit = itemKit != null;
		this.itemKit = itemKit;
		this.itemPedido = itemPedido;
		loadProdutoHash(produtoOriginal);
		edFiltro = new EditFiltro("999999999", 50);
		btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_SALVAR);
		constructorListContainer();
		if (fromKit) {
			setDefaultRect();
		}
	}

	private void loadProdutoHash(Produto produtoOriginal) throws SQLException {
		produtoOriginal.itemComboAgrSimilarFilter = ItemComboAgrSimilarService.getInstance().getItemComboAgrSimilarFilterByItemCombo(itemCombo);
		produtoOriginal.itemKitAgrSimilarFilter = ItemKitAgrSimilarService.getInstance().getItemKitAgrSimilarFilterByItemKit(itemKit);
		Vector produtoList = ProdutoService.getInstance().findProdutosAgrupador(produtoOriginal, fromKit, itemPedido != null ? itemPedido.pedido : null, isConsideraEstoqueKit());
		int size = produtoList.size();
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto) produtoList.items[i];
			produtoHash.put(produto.getPrimaryKey(), produto);
		}
		if (ValueUtil.isEmpty(produtoOriginal.similaresList)) {
			produtoOriginal.similaresList = ProdutoService.getInstance().findProdutosAgrupador(produtoOriginal, fromKit, itemPedido != null ? itemPedido.pedido : null, isConsideraEstoqueKit()); //roda findProdutosAgrupador novamente para pegar outras refêrencias de memória
		} else {
			size = produtoOriginal.similaresList.size();
			for (int i = 0; i < size; i++) {
				Produto produto = (Produto) produtoOriginal.similaresList.items[i];
				Produto prod = produtoHash.get(produto.getPrimaryKey());
				if (prod != null) prod.qtItemPedido = produto.qtItemPedido;
			}
		}
	}
	
	private void loadProdutoHash() throws SQLException {
		Vector itemPedidoAgrSimilarList = ItemPedidoAgrSimilarService.getInstance().findItemPedidoAgrSimilarList(itemPedido);
		int size = itemPedidoAgrSimilarList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoAgrSimilar item = (ItemPedidoAgrSimilar) itemPedidoAgrSimilarList.items[i];
			produtoHash.get(item.getProdutoKey()).qtItemPedido = item.qtItemFisico;
		}
		produtoHash.get(itemPedido.getProduto().getPrimaryKey()).qtItemPedido = itemPedido.getQtItemFisicoOrg();
		produtoOriginal.qtItemPedido = itemPedido.getQtItemFisico();
	}

	private void constructorListContainer() {
		GridColDefinition[] definition =  {new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, -80, LEFT),
				new GridColDefinition(Messages.ITEMCOMBO_LABEL_QTITENS, -20, RIGHT),
				new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT)};
		grid = UiUtil.createGridEdit(definition, false);
		grid.setFont(Font.getFont(grid.getFont().name, false, (int) (grid.getFont().size * 0.90)));
		if (itemPedido == null || !pedidoFechadoOuTransmitido()) {
			grid.setColumnEditableInt(column_qt, true, 9, true);
		}
		grid.setID("grid");
	}

	private boolean pedidoFechadoOuTransmitido() {
		return itemPedido.pedido.isPedidoFechado() || itemPedido.pedido.isPedidoTransmitido();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		produtoOriginal.itemComboAgrSimilarFilter = ItemComboAgrSimilarService.getInstance().getItemComboAgrSimilarFilterByItemCombo(itemCombo);
		produtoOriginal.itemKitAgrSimilarFilter = ItemKitAgrSimilarService.getInstance().getItemKitAgrSimilarFilterByItemKit(itemKit);
		return ProdutoService.getInstance().findProdutosAgrupador(produtoOriginal, fromKit, edFiltro.getValue(), itemPedido != null ? itemPedido.pedido : null, isConsideraEstoqueKit());
	}

	private boolean isConsideraEstoqueKit() throws SQLException {
		if (fromKit) {
			Kit kit = KitService.getInstance().getKit(itemKit);
			return kit.isValidaEstoque();
		}
		return true;
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		String[] item = new String[3];
		Produto produto = (Produto) domain;
		item[0] = ProdutoService.getInstance().getDescricaoProdutoReferenciaConsideraCodigo(produto);
		item[column_qt] = StringUtil.getStringValue(ValueUtil.getIntegerValue(produtoHash.get(produto.getPrimaryKey()).qtItemPedido));
		item[column_key] = produto.getPrimaryKey();
		return item;
	}

	@Override
	protected String getSelectedRowKey() {
		return grid.getSelectedItem()[column_key];
	}


	@Override
	protected CrudService getCrudService() {
		return ProdutoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new Produto();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, new LabelValue(MessageUtil.getMessage(Messages.ITEMCOMBO_MSG_QTD_DISTRIBUIR, ValueUtil.getIntegerValue(produtoOriginal.qtItemPedido))), LEFT + WIDTH_GAP_BIG, TOP + HEIGHT_GAP);
		lbTotal = new LabelValue(MessageUtil.getMessage(Messages.ITEMCOMBO_MSG_QTD_DISTRIBUIDA, ValueUtil.getIntegerValue(0)));
		UiUtil.add(this, lbTotal, LEFT + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP);
		
		UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, grid, getLeft(), AFTER + HEIGHT_GAP, FILL, FILL);
	}
	
	@Override
	public void list() throws SQLException {
		super.list();
		setQtdTotalDistribuida(-1);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					saveQtdSimilares();
					saved = true;
					btFecharClick();
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					list();
					edFiltro.setText(ValueUtil.VALOR_NI);
				}
				break;
			}
			case GridEvent.TEXT_CHANGED_EVENT: {
				gridToHash();
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (grid.getLastShownControl() != null) {
					String qtd = ((EditNumberInt) grid.getLastShownControl()).getText();
					int qtdInt = ValueUtil.getIntegerValue(qtd);
					setQtdTotalDistribuida(qtdInt);
					if (!VmUtil.isSimulador() && event.target != edFiltro) {
						grid.setCellText(grid.getSelectedIndex(), column_qt, qtd, true);
					}
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				setQtdTotalDistribuida(-1);
				break;
			}
			case ControlEvent.FOCUS_IN: {
				if (grid.getLastShownControl() != null) {
					String qtd = ((EditNumberInt) grid.getLastShownControl()).getText();
					if (qtd != null) {
						valueOld = ValueUtil.getIntegerValue(qtd);
					}
				}
				break;
			}
			case GridEvent.SELECTED_EVENT:
				GridEvent selectedEvent = (GridEvent)event;
				
				if (selectedEvent.row >= 0
						&& selectedEvent.col != 1) {
					grid.exibeControleGrid(selectedEvent.row, 1);
					
					final Control control = grid.getLastShownControl();
					if (control != null && control instanceof BaseEdit) {
						final BaseEdit edit = (BaseEdit)control;
						edit.disableClipboardMenu = true;
					}
					
				}
				
				selectedEvent.consumed = true;
				
				break;
		}

	}

	private void gridToHash() {
		int size = grid.size();
		for (int i = 0; i < size; i++) {
			produtoHash.get(grid.getCellText(i, column_key)).qtItemPedido = getIntegerFromGrid(i);
		}
	}

	private int getIntegerFromGrid(int i) {
		return ValueUtil.getIntegerValue(grid.getItem(i)[column_qt].replace(".", ""));
	}
	
	private void setQtdTotalDistribuida(int value) {
		int size = grid.size();
		int qtdTotal = 0;
		for (int i = 0; i < size; i++) {
			int qtd = getIntegerFromGrid(i);
			produtoHash.get(grid.getItem(i)[column_key]).qtItemPedido = qtd;
		}
		for (Produto produto : produtoHash.values()) {
			qtdTotal += produto.qtItemPedido;
		}
		if (value >= 0) {
			qtdTotal -= valueOld;
			qtdTotal += value;
		}
		if (qtdTotal > ValueUtil.getIntegerValue(produtoOriginal.qtItemPedido) && grid.getLastShownControl() != null) {
			((EditNumberInt) grid.getLastShownControl()).setValue(valueOld);
			throw new ValidationException(Messages.ITEMCOMBO_MSG_ERRO_QTD_SIMILAR_EXTRAPOLADA);
		}
		valueOld = 0;
		lbTotal.setValue(MessageUtil.getMessage(Messages.ITEMCOMBO_MSG_QTD_DISTRIBUIDA, ValueUtil.getIntegerValue(qtdTotal)));
		repaintNow();
	}

	private void saveQtdSimilares() {
		int size = grid.size();
		int qtdDistribuida = 0;
		for (int i = 0; i < size; i++) {
			int qtd = getIntegerFromGrid(i);
			produtoHash.get(grid.getItem(i)[column_key]).qtItemPedido = qtd;
		}
		for (Produto produto : produtoHash.values()) {
			qtdDistribuida += produto.qtItemPedido;
		}
		if (qtdDistribuida != ValueUtil.getIntegerValue(produtoOriginal.qtItemPedido)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMCOMBO_MSG_ERRO_QTD_SIMILAR, ValueUtil.getIntegerValue(produtoOriginal.qtItemPedido)));
		} else {
			size = this.produtoOriginal.similaresList.size();
			int sizeHash = produtoHash.size();
			int qtDistribuido = 0;
			for (int i = 0; i < size; i++) {
				Produto produto = (Produto) this.produtoOriginal.similaresList.items[i];
				Produto produtoInHash = produtoHash.get(produto.getPrimaryKey());
				if (produtoInHash != null) {
					qtDistribuido++;
					produto.qtItemPedido = produtoInHash.qtItemPedido;
					if (qtDistribuido == sizeHash) break;
				}
			}
		}
	}

	@Override
	protected void addButtons() {
		addButtonPopup(btSalvar);
		super.addButtons();
	}

}
