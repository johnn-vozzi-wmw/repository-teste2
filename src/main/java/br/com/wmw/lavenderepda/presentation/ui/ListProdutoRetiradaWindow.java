package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ProdutoRetirada;
import br.com.wmw.lavenderepda.business.service.ProdutoRetiradaService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoRetiradaWindow extends WmwListWindow {

	private Cliente cliente;
	
	public ListProdutoRetiradaWindow(Cliente cliente) {
		super(Messages.PRODUTORETIRADA_TITULO_LISTA);
		this.cliente = cliente;
		constructorListContainer();
		setDefaultRect();
	} 

	
	private void constructorListContainer() {
		listContainer = new GridListContainer(10, 2);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColPosition(5, RIGHT);
    	listContainer.setColPosition(7, RIGHT);
    }
	
	
	@Override
	protected CrudService getCrudService() {
		return ProdutoRetiradaService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ProdutoRetirada();
	}

	@Override
	protected Vector getDomainList(BaseDomain baseDomain) throws SQLException {
		ProdutoRetirada produtoRetiradaFilter = (ProdutoRetirada) baseDomain;
		produtoRetiradaFilter.cdEmpresa = cliente.cdEmpresa;
		produtoRetiradaFilter.cdRepresentante = cliente.cdRepresentante;
		produtoRetiradaFilter.cdCliente = cliente.cdCliente;
		produtoRetiradaFilter.sortAtributte = "DTCOMPRA";
		produtoRetiradaFilter.sortAsc = ValueUtil.VALOR_SIM;
		return getCrudService().findAllByExample(produtoRetiradaFilter);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ProdutoRetirada produtoRetirada = (ProdutoRetirada) domain;
		String[] item = {
			produtoRetirada.toString(),
			"",
			Messages.PRODUTORETIRADA_LABEL_NUNOTA + " " + StringUtil.getStringValueToInterface(produtoRetirada.nuNota),
			Messages.PRODUTORETIRADA_LABEL_QTDCOMPRA + " " + StringUtil.getStringValueToInterface(produtoRetirada.qtCompra),
			Messages.PRODUTORETIRADA_LABEL_NUCONTRATO + " " + StringUtil.getStringValueToInterface(produtoRetirada.nuContrato),
			Messages.PRODUTORETIRADA_LABEL_QTDRESTANTE + " " + StringUtil.getStringValueToInterface(produtoRetirada.qtRestante),
			Messages.PRODUTORETIRADA_LABEL_DTCOMPRA + " " + StringUtil.getStringValue(produtoRetirada.dtCompra),
			"",
			Messages.PRODUTORETIRADA_LABEL_DTMAXRET + " " + StringUtil.getStringValue(produtoRetirada.dtMaxRetirada),
			"",
		};
		return item;
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) {
		ProdutoRetirada produtoRetirada = (ProdutoRetirada) domain;
		if (produtoRetirada.dtMaxRetirada != null && produtoRetirada.dtMaxRetirada.isBefore(DateUtil.getCurrentDate())) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_PENDENTE_RETIRADA);
		}
	}
	
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return ((ProdutoRetirada) domain).toString();
	}

	@Override
	protected String getSelectedRowKey() {
		return null;
	}

	@Override
	protected void onFormEvent(Event arg0) {
		
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

}
