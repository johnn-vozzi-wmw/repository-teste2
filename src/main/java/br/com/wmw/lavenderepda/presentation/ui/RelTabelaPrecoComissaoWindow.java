package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelTabelaPrecoComissaoWindow extends LavendereWmwListWindow {

	private Produto produto;
	private Vector tabelaPrecoList;
	private Pedido pedido;
	public String cdTabelaPreco;
	private LabelContainer lbDsProdutoContainer;
	
	public RelTabelaPrecoComissaoWindow(Vector tabelaPrecoList, Produto produto, Pedido pedido) {
		super(Messages.MENU_REL_COMISSAO_PRECO);
		this.produto = produto;
		this.pedido = pedido;
		lbDsProdutoContainer = new LabelContainer(this.produto.toString());
		this.tabelaPrecoList = tabelaPrecoList;
		singleClickOn = true;
		constructorListContainer();
		setDefaultRect();
	}
	
    public void constructorListContainer() {
    	listContainer = new GridListContainer(4, 2);
    	listContainer.setColPosition(2, LEFT);
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }
    
	protected void onFormStart() {
		UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}
	
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		tabelaPrecoList.qsort();
		return tabelaPrecoList;
	}
	
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		TabelaPreco tabelaPreco = (TabelaPreco) domain;
		Vector item = new Vector();
		item.addElement(tabelaPreco.toString());
		item.addElement(StringUtil.getStringValue(""));
		item.addElement(Messages.REL_COMISSAO_PRECO_COMISSAO + " " + StringUtil.getStringValueToInterface(tabelaPreco.vlPctComissao) + "%");
		item.addElement(Messages.REL_COMISSAO_PRECO + StringUtil.getStringValueToInterface(ItemTabelaPrecoService.getInstance().getVlVendaProdutoToGrid(pedido, produto, tabelaPreco.cdTabelaPreco, false)));
		return (String[]) item.toObjectArray();
	}

	 //@Override
    public void detalhesClick() throws SQLException {
    	super.detalhesClick();
		cdTabelaPreco = ((TabelaPreco) getSelectedDomain()).cdTabelaPreco;
		unpop();
    }
	
	 //@Override
    protected String getSelectedRowKey() {
        String item = listContainer.getSelectedId();
        return item;
    }
	protected CrudService getCrudService() throws java.sql.SQLException {
		return TabelaPrecoService.getInstance();
	}


	protected BaseDomain getDomainFilter() {
		return new TabelaPreco();
	}

	protected void onFormEvent(Event event) throws SQLException {
	}

}
