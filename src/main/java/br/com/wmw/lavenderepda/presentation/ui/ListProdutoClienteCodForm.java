package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteCodService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoClienteCodForm extends LavendereCrudListForm  {
	
	private Cliente cliente;
	private Pedido pedido;
	private HashMap<String, ProdutoClienteCod> produtoClienteCodHash;

	public ListProdutoClienteCodForm(Cliente cliente, Pedido pedido) throws SQLException {
		super(Messages.CAD_PROD_CLI_COD_ITENS);
		this.cliente = cliente;
		this.pedido = pedido;
		singleClickOn = !isFromPedido() || isPedidoAberto();
		produtoClienteCodHash = new HashMap<String, ProdutoClienteCod>();
		constructorListContainer();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		ProdutoClienteCod produtoClienteCod = new ProdutoClienteCod();
		produtoClienteCod.cdEmpresa = cliente.cdEmpresa;
		produtoClienteCod.cdRepresentante = cliente.cdRepresentante;
		produtoClienteCod.cdCliente = cliente.cdCliente;
		if (isFromPedido()) {
			produtoClienteCod.nuPedido = pedido.nuPedido;
			produtoClienteCod.realizaBuscaItemPedidoErp = OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedido.flOrigemPedido);
			produtoClienteCod.pedidoFechado = !isPedidoAberto();
		}
		return produtoClienteCod;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProdutoClienteCodService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}
	
	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		if (isFromPedido()) {
			return produtoClienteCodHash.get(getSelectedRowKey());
		} else {
			return super.getSelectedDomain();
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector produtoClienteCodList;
		if (isFromPedido()) {
			produtoClienteCodList = ((ProdutoClienteCodService) getCrudService()).findAllProdutoClienteCodFromFechamentoPedido(domain);
			addProdutoClienteCodListToHash(produtoClienteCodList);
		} else {
			produtoClienteCodList = getCrudService().findAllByExample(domain);
		}
		return produtoClienteCodList;
	}
	
	private void addProdutoClienteCodListToHash(Vector produtoClienteList) {
		int size = produtoClienteList.size();
		for (int i = 0; i < size; i++) {
			ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) produtoClienteList.items[i];
			produtoClienteCodHash.put(produtoClienteCod.getRowKey(), produtoClienteCod);
		}
		
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		Vector item = new Vector();
		item.addElement(LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : StringUtil.getStringValue(produtoClienteCod.cdProduto) + " - ");
		item.addElement(StringUtil.getStringValue(produtoClienteCod.dsProduto));
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			item.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produtoClienteCod.dsPrincipioAtivo), (int)(width * 0.6), listContainer.getFontSubItens()));
		}
		if (ValueUtil.isEmpty(produtoClienteCod.cdProdutoCliente)) {
			item.addElement(Messages.CAD_PROD_CLI_COD_SEMCODIGO_MSG);
		} else {
			item.addElement(StringUtil.getStringValue(produtoClienteCod.cdProdutoCliente));
		}
		return (String[]) item.toObjectArray();
	}
	
	private int getItemCount() {
		int itemCount = LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 4 : 3;
		return itemCount;
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(getItemCount(), 2);
		String[][] colsSort = getColsSort();
		listContainer.setColsSort(colsSort);
		listContainer.btResize.setVisible(false);
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			listContainer.setColPosition(3, RIGHT);
		}
		sortAtributte = "prod.DSPRODUTO";
		sortAsc = "S";
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
	}

	private String[][] getColsSort() {
		String[][] colsSort = new String[3][2];
		colsSort[0][0] = Messages.CAD_PROD_CLI_COD_SORT_CODPRODCLI;
		colsSort[0][1] = "tb.CDPRODUTOCLIENTE";
		colsSort[1][0] = Messages.CAD_PROD_CLI_COD_SORT_DESC;
		colsSort[1][1] = "prod.DSPRODUTO";
		colsSort[2][0] = Messages.CAD_PROD_CLI_COD_SORT_CDPROD;
		colsSort[2][1] = "prod.CDPRODUTO";
		return colsSort;
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		BaseDomain domain = getSelectedDomain();
		CadProdutoClienteCodForm cadProdutoClienteCod = new CadProdutoClienteCodForm(isFromPedido());
		CadProdutoClienteCodFormWindow cadProdutoClienteCodFormWindow = new CadProdutoClienteCodFormWindow(cadProdutoClienteCod);
		cadProdutoClienteCod.setCadProdutoClienteCodFormWindow(cadProdutoClienteCodFormWindow);
		setBaseCrudCadForm(cadProdutoClienteCod);
		cadProdutoClienteCodFormWindow.showWindow((ProdutoClienteCod) domain);
	}
	
	private boolean isFromPedido() {
		return pedido != null;
	}
	
	private boolean isPedidoAberto() {
		return isFromPedido() && pedido.isPedidoAberto();
	}

	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
		containerItem.setToolTip(((ProdutoClienteCod) domain).dsProduto);
	}
}
