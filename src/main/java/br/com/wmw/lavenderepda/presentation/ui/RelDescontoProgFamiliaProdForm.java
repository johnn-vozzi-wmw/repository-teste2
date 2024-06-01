package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelDescontoProgFamiliaProdForm extends LavendereCrudListForm {

	private DescProgConfigFam fam;
	private Pedido pedido;
	
	private CadPedidoForm cadPedidoForm;
	private CadItemPedidoForm cadItemPedidoForm;
	
	public RelDescontoProgFamiliaProdForm(DescProgConfigFam fam, CadPedidoForm cadPedidoForm) throws SQLException {
		super(fam != null ? fam.toString() : Messages.DESC_PROG_FAMILIA_PRODUTOS_TITLE);
		this.fam = fam;
		this.cadPedidoForm = cadPedidoForm;
		pedido = cadPedidoForm != null ? cadPedidoForm.getPedido() : null;
		
		singleClickOn = true;
		constructListContainer();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		final Produto produto = getSelectedDomain();
		if (pedido == null || cadPedidoForm == null || isApenasConsulta()) return;
		
		LoadingBoxWindow lbWindow = UiUtil.createProcessingMessage();
		try {
			lbWindow.popupNonBlocking();
			if (isProdutoSelecionadoRestritoProdutoCliente()) {
				throw new ValidationException(Messages.MSG_PRODUTO_DESC_PROGRESSIVO_RESTRITO_CLIENTE);
			}
			super.detalhesClick(produto);
			ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, produto.cdProduto);

			loadCadItemPedidoForm();
			if (itemPedido == null) {
				cadItemPedidoForm.addFromExternalForm(produto, this);
			} else {
				cadItemPedidoForm.editFromExternalForm(itemPedido, this);
			}
		} finally {
			lbWindow.unpop();
		}
	}

	private boolean isProdutoSelecionadoRestritoProdutoCliente() {
		Produto produto = (Produto) getSelectedDomain2();
		return LavenderePdaConfig.filtraProdutoClienteRepresentante && produto.restritoClienteProduto;
	}

	private void setupCadItemPedidoForm() {
		cadItemPedidoForm.flFromCadPedido = true;
		cadItemPedidoForm.disabledBtNextPrev = true;
	}
	
	private void loadCadItemPedidoForm() throws SQLException {
		if (cadItemPedidoForm == null) {
			cadItemPedidoForm = CadItemPedidoForm.getNewCadItemPedido(cadPedidoForm, pedido);
			cadItemPedidoForm.setBaseCrudListForm(null);
		}
		setupCadItemPedidoForm();
	}
	
	@Override
	public void list() throws SQLException {
		final int lastScroll = listContainer.getScrollPos();
		super.list();;
		listContainer.getBaseListContainer().scrollContent(0, lastScroll, true);
	}
	
	@Override
	protected void setPropertiesInRowList(Item container, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(container, domain);
		setPropertiesInRowItem(container, domain);
	}
	
	@Override
	protected void updatePropertiesInRowItem(Item container, BaseDomain domain, int indexAtual) throws SQLException {
		super.updatePropertiesInRowItem(container, domain, indexAtual);
		setPropertiesInRowItem(container, domain);
	}
	
	private void setPropertiesInRowItem(Item container, BaseDomain domain) {
		if (pedido != null) {
			Produto produto = (Produto)domain;
			int color = getItemColor(produto, pedido);
			container.setBackColor(color == -1 ? ColorUtil.baseBackColorSystem : color);
		}
	}
	
	public int getItemColor(Produto produto, Pedido pedido) {
		if (LavenderePdaConfig.destacaProdutosJaIncluidosAoPedido && isProdutoAlreadyPresent(produto, pedido)) {
			return LavendereColorUtil.COR_PRODUTO_INSERIDO_DESC_PROGRESSIVO_BACK;
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante && produto.restritoClienteProduto) {
			return LavendereColorUtil.COR_FUNDO_ITEM_PRODUTO_RESTRITO;
		}
		return -1;
	}
	
	private boolean isProdutoAlreadyPresent(final Produto produto, final Pedido pedido) {
		if (!ValueUtil.isEmpty(pedido.itemPedidoList)) {
			for (int i = 0, size = pedido.itemPedidoList.size(); i < size; i++) {
				ItemPedido item = (ItemPedido)pedido.itemPedidoList.items[i];
				if (ValueUtil.valueEquals(item.cdProduto, produto.cdProduto)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isApenasConsulta() throws SQLException {
		if (cadPedidoForm.isPedidoApenasConsulta()) {
			return !isProdutoAlreadyPresent(getSelectedDomain(), pedido);
		}
		return false;
	}
	
	private void constructListContainer() {
		configListContainer("DSPRODUTO");
		listContainer = new GridListContainer(1, 1);
		
		Util.setListSort(listContainer,
				new String[] {"Descrição", "DSPRODUTO"},
				new String[] {"Código", "CDPRODUTO"}
				);
	}
	
	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		Produto filter = new Produto();
		filter.cdEmpresa = fam.cdEmpresa;
		filter.cdRepresentante = fam.cdRepresentante;
		filter.cdFamiliaDescProg = fam.cdFamiliaDescProg;
		
		return filter;
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Produto produto = (Produto)domain;
		String dsProduto = produto.dsProduto;
		
		if (!LavenderePdaConfig.ocultaColunaCdProduto && ValueUtil.isNotEmpty(produto.cdProduto)) {
			dsProduto = produto.cdProduto + (ValueUtil.isNotEmpty(dsProduto) ? (" - " + dsProduto) : "");
		}
		
		return new String[] {dsProduto};
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Produto filter = (Produto)domain;
		prepareProdutoClienteFilter(filter);
		Vector listProduto = ((ProdutoService)getCrudService()).findAllProdutoByFamiliaProd(filter);
		realizaOrdenacaoListaItens(listProduto, sortAtributte, sortAsc);
		return listProduto;
	}

	public void realizaOrdenacaoListaItens(Vector list, String sortAtributte, String sortAsc) {
		ProdutoBase.sortAttr = sortAtributte;
		if (ProdutoBase.SORT_COLUMN_DSPRODUTO.equalsIgnoreCase(sortAtributte)) {
			SortUtil.qsortString(list.items, 0, list.size() - 1, true);
		} else if (ProdutoBase.SORT_COLUMN_CDPRODUTO.equalsIgnoreCase(sortAtributte)) {
			SortUtil.qsortString(list.items, 0, list.size() - 1, true);
		} else {
			list.qsort();
		}
		//Ordenação desc
		if (sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			list.reverse();
		}
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getNextY() - HEIGHT_GAP, FILL, FILL);
	}
	
	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProdutoService.getInstance();
	}
	
	@Override
	public Produto getSelectedDomain() throws SQLException {
		BaseDomain domain = super.getSelectedDomain();
		return domain != null ? (Produto)domain : null;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
	protected void prepareProdutoClienteFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			produtoFilter.produtoClienteFilter = ProdutoClienteService.getInstance().getProdutoClienteFilter(pedido == null ? fam.cliente.cdCliente : pedido.cdCliente);
		}
	}
}
