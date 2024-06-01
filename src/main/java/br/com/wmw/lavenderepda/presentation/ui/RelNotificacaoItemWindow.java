package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelNotificacaoItemWindow extends WmwListWindow {

	private ScrollTabbedContainer scrollTabbedContainer;
	private ButtonPopup btConferir;
	private Vector itemErroList;
	private Vector itemAdvertenciaList;
	protected boolean conferirInconsistencias;
	protected boolean usaConferirInconsistencias;
	private LabelValue lbDsInfo;
	private String dsInfo;
	private GridListContainer listContainerItensInseridos;
	private CadItemPedidoForm cadItemPedidoForm;
	private boolean isOpenInsercaoUnitaria;
	private boolean isUsaTabbedContainer;
	private boolean clearErrorLists;

	public RelNotificacaoItemWindow(Vector itemErroList, boolean usaConferirInconsistencias) {
		this(Messages.REL_NOTIFICACAO_ITENS, itemErroList, usaConferirInconsistencias);
	}

	public RelNotificacaoItemWindow(String titulo, Vector itemErroList, boolean usaConferirInconsistencias) {
		this(titulo, itemErroList, null, usaConferirInconsistencias, null, false);
	}

	public RelNotificacaoItemWindow(Vector itemErroList, Vector itemAdvertenciaList, boolean usaConferirInconsistencias) {
		this(Messages.REL_NOTIFICACAO_ITENS, itemErroList, itemAdvertenciaList, usaConferirInconsistencias, null, true);
	}

	public RelNotificacaoItemWindow(String titulo, Vector itemErroList, boolean usaConferirInconsistencias, String dsInfo) {
		this(titulo, itemErroList, null, usaConferirInconsistencias, dsInfo, false);
	}

	public RelNotificacaoItemWindow(String titulo, Vector itemErroList, Vector itemAdvertenciaList, boolean usaConferirInconsistencias, String dsInfo, boolean isUsaTabbedContainer) {
		super(titulo);
		this.itemErroList = itemErroList;
		this.itemAdvertenciaList = itemAdvertenciaList;
		this.isUsaTabbedContainer = isUsaTabbedContainer;
		scrollable = false;
		if (this.isUsaTabbedContainer) {
			Vector tabs = configureTabs();
			scrollTabbedContainer = new ScrollTabbedContainer((String[]) tabs.toObjectArray());
		}
		if (usaConferirInconsistencias) {
			btConferir = new ButtonPopup(Messages.REL_ERRO_ITEM_CONFERIR);
			this.usaConferirInconsistencias = true;
		}
		if (ValueUtil.isNotEmpty(dsInfo)) {
			lbDsInfo = new LabelValue();
			lbDsInfo.autoMultipleLines = true;
			this.dsInfo = dsInfo;
		}
		this.clearErrorLists = true;
		configureListContainer();
		setDefaultRect();
	}

	private Vector configureTabs() {
		Vector tabs = new Vector();
		if (isPossuiItemNaoInserido()) {
			tabs.addElement(Messages.REL_ITENS_NAO_INSERIDOS);
		}
		if (isPossuiItemInseridoComAdvertencia()) {
			listContainerItensInseridos = new GridListContainer(4, 1);
			listContainerItensInseridos.setBarTopSimple();
			tabs.addElement(Messages.REL_ITENS_ADVERTENCIAS);
		}
		return tabs;
	}

	private void loadListContainerInseridosComAdvertencia() throws SQLException {
		int size = itemAdvertenciaList.size();
		if (size > 0) {
			Container[] all = new Container[size];
			BaseListContainer.Item c;
			ProdutoErro itemAdvertencia;
			for (int i = 0; i < size; i++) {
				all[i] = c = new BaseListContainer.Item(listContainerItensInseridos.getLayout());
				if (i % 2 == 0) {
					c.setBackColor(Color.darker(c.getBackColor(), 10));
				}
				itemAdvertencia = (ProdutoErro) itemAdvertenciaList.items[i];
				c.setItens(getItemForListInseridos(itemAdvertencia));
				c.setToolTip(getToolTipoListItensInseridos(itemAdvertencia));
			}
			itemAdvertencia = null;
			listContainerItensInseridos.addContainers(all);
		}
	}

	private String getToolTipoListItensInseridos(ProdutoErro itemAdvertencia) {
		return itemAdvertencia.dsMensagemErro;
	}

	private String[] getItemForListInseridos(ProdutoErro itemAdvertencia) {
		Vector item = new Vector();
		String descricaoProduto = ValueUtil.isNotEmpty(itemAdvertencia.dsProduto) ?  itemAdvertencia.cdProduto + " - " + itemAdvertencia.dsProduto : itemAdvertencia.cdProduto;
		item.addElement(StringUtil.getStringValue(descricaoProduto));
		Vector lines = StringUtil.splitStringIntoLines(itemAdvertencia.dsMensagemErro, this.width - WIDTH_GAP_BIG, 3, UiUtil.defaultFontSmall);
		item.addElementsNotNull(lines.items);
		for (int i = 0; i < 3 - lines.size(); i++) {
			item.addElement(ValueUtil.VALOR_NI);
		}
		return (String[])item.toObjectArray();
	}

	protected void configureListContainer() {
		if (isPossuiItemNaoInserido()) {
			int itemCount = LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto5() ? 9 : 2;
			int itemsPerLine = LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto5() ? 3 : 1;
			listContainer = new GridListContainer(itemCount, itemsPerLine, false, true);
			if (LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto5()) {
				listContainer.setColPosition(3, LEFT);
				listContainer.setColPosition(4, CENTER);
				listContainer.setColPosition(5, RIGHT);
			}
			listContainer.setBarTopSimple();
			listContainer.indexWidthText = LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto5() ? 8 : 1;
		}
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	//@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return itemErroList;
	}

	//@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ProdutoErro produtoErro = (ProdutoErro) domain;
		Vector item = new Vector(0);
		String descricaoProduto = ValueUtil.isNotEmpty(produtoErro.dsProduto) ?  produtoErro.cdProduto + " - " + produtoErro.dsProduto : produtoErro.cdProduto;
		item.addElement(StringUtil.getStringValue(descricaoProduto));
		if (LavenderePdaConfig.isConfigGradeProduto() && !LavenderePdaConfig.usaGradeProduto5()) {
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
			item.addElement(ValueUtil.isNotEmpty(produtoErro.dsItemGrade1) ? produtoErro.dsItemGrade1 : "Sem grade");
			item.addElement(StringUtil.getStringValue(produtoErro.dsItemGrade2));
			item.addElement(StringUtil.getStringValue(produtoErro.dsItemGrade3));
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
		item.addElement(StringUtil.getStringValue(produtoErro.dsMensagemErro));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getToolTip(BaseDomain domain) {
		return ((ProdutoErro) domain).dsMensagemErro;
	}

	//@Override
	protected void onFormStart() {
		if (isUsaTabbedContainer) {
			int tab = 0;
			UiUtil.add(this, scrollTabbedContainer, getTop() + HEIGHT_GAP, footerH);
			if (isPossuiItemNaoInserido()) {
				Container tab1 = scrollTabbedContainer.getContainer(tab);
				if (lbDsInfo != null) {
					UiUtil.add(tab1, lbDsInfo, getLeft(), getNextY());
					lbDsInfo.setText(dsInfo);
				}
				UiUtil.add(tab1, listContainer, LEFT, getNextY(), FILL, FILL);
				tab++;
			}
			if (isPossuiItemInseridoComAdvertencia()) {
				Container tab2 = scrollTabbedContainer.getContainer(tab);
				UiUtil.add(tab2, listContainerItensInseridos, LEFT, TOP, FILL, FILL);
				try {
					loadListContainerInseridosComAdvertencia();
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		} else {
			if (lbDsInfo != null) {
				UiUtil.add(this, lbDsInfo, getLeft(), getNextY());
				lbDsInfo.setText(dsInfo);
			}
			if (isPossuiItemNaoInserido()) {
				UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - footerH);
			}
		}
	}


	protected void addButtons() {
		super.addButtons();
		if (!LavenderePdaConfig.isFiltraItensComErroNaInsercaoMultiplaItensRevisar()) {
			addButtonPopup(btFechar);
		}
		if (usaConferirInconsistencias) {
			addButtonPopup(btConferir);
		}
	}

	protected void addBtFechar() {
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	public void onEvent(Event event) {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btFechar) {
				if (clearErrorLists) {
					itemErroList.removeAllElements();
				}
				conferirInconsistencias = false;
				if (LavenderePdaConfig.isFiltraItensComErroNaInsercaoMultiplaItensRevisar()) {
					try {
						btFecharClick();
					} catch (SQLException e) {
						ExceptionUtil.handle(e);
					}
				}
			}
			if (event.target == btConferir) {
				conferirInconsistencias = true;
				unpop();
			}
			break;
		}
		}
		if (!LavenderePdaConfig.isFiltraItensComErroNaInsercaoMultiplaItensRevisar()) {
			super.onEvent(event);
		}
	}

	private boolean isPossuiItemInseridoComAdvertencia() {
		return ValueUtil.isNotEmpty(itemAdvertenciaList);
	}

	private boolean isPossuiItemNaoInserido() {
		return ValueUtil.isNotEmpty(itemErroList);
	}
	
	@Override
	protected void onUnpop() {
		super.onUnpop();
		if (clearErrorLists) {
			clearErrorLists();
		}
	}

	public void clearErrorLists() {
		if (listContainerItensInseridos != null) {
			listContainerItensInseridos.removeAllContainers();
			itemAdvertenciaList.removeAllElements();
		}
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) throws SQLException {
		this.cadItemPedidoForm = cadItemPedidoForm;
		singleClickOn = true;
		isOpenInsercaoUnitaria = true;
		this.cadItemPedidoForm.add();
		this.cadItemPedidoForm.show();
	}
	
	@Override
	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		if (isOpenInsercaoUnitaria) {
			this.cadItemPedidoForm.voltarClick();
		}
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		Produto produto = getSelectProduto();
		if (produto == null) {
			UiUtil.showErrorMessage(Messages.PRODUTO_MSG_NAO_ENCONTRADO);
			return;
		}
		ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByCdProduto(cadItemPedidoForm.pedido, produto.cdProduto);
		itemPedido.pedido = cadItemPedidoForm.pedido;
		cadItemPedidoForm.produtoSelecionado = produto;
		cadItemPedidoForm.edit(itemPedido);
		this.cadItemPedidoForm.fromRelNotificaoItemWindow = true;
		cadItemPedidoForm.gridClickAndRepaintScreen();
		unpop();
	}
	
	public Produto getSelectProduto() throws SQLException {
		return (Produto) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId());
	}

	public boolean isClearErrorLists() {
		return clearErrorLists;
	}

	public void setClearErrorLists(boolean clearErrorLists) {
		this.clearErrorLists = clearErrorLists;
	}

}
