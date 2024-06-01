package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.sys.Convert;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItemPedidoAbaixoPesoMinimoWindow extends LavendereWmwListWindow {
	
	private CadItemPedidoForm cadItemPedidoForm;
	private LabelValue lbDescricao;
	private LabelValue lbPesoPedido;
	public Pedido pedido;
	private Vector tabelaPrecoPesoInvalidoList;
	
	public ListItemPedidoAbaixoPesoMinimoWindow(Pedido pedido) {
		super(Messages.ITEMPEDIDO_PESO_MINIMO_TITULO_ITENS_ABAIXO_MINIMO);
		this.pedido = pedido;
		lbDescricao = new LabelValue();
		lbDescricao.setText(Convert.insertLineBreak(width - 6, lbDescricao.fm, Messages.ITEMPEDIDO_PESOMINIMO_NAO_ATINGIDO_TABELA_PRECO));
		lbPesoPedido = new LabelValue();
		singleClickOn = true;
		constructorListContainer();
		setDefaultRect();
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

    private void constructorListContainer() {
		listContainer = new GridListContainer(9, 3);
		listContainer.setColPosition(6, LEFT);
		listContainer.setColPosition(7, CENTER);
		listContainer.setColPosition(8, RIGHT);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }
    
	//@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		Vector itemPedidoPesoInvalidoList = new Vector();
		tabelaPrecoPesoInvalidoList = new Vector();
		boolean validaPorItemTabela = LavenderePdaConfig.isValidaTabelaPrecoFechamentoItemPedido();
		for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[j];
			if(validaPorItemTabela) {
				recalculaPeso();
				if (itemPedido.getTabelaPreco().qtPesoAcumuladoAtual < itemPedido.getTabelaPreco().qtPesoMin) {
					if (!tabelaPrecoPesoInvalidoList.contains(itemPedido.getTabelaPreco())) {
						tabelaPrecoPesoInvalidoList.addElement(itemPedido.getTabelaPreco());
					}
					itemPedidoPesoInvalidoList.addElement(itemPedido);
				}
			}
			else {
				if (pedido.qtPeso < itemPedido.getTabelaPreco().qtPesoMin) {
					if (!tabelaPrecoPesoInvalidoList.contains(itemPedido.getTabelaPreco())) {
						tabelaPrecoPesoInvalidoList.addElement(itemPedido.getTabelaPreco());
					}
					itemPedidoPesoInvalidoList.addElement(itemPedido);
				}
			}
		}
		return ordernaItensPorTabelaPreco(itemPedidoPesoInvalidoList);
	}
	
	private void recalculaPeso() {
		try {
			PedidoService.getInstance().validaQtMinPesoTabPreco(pedido);
		} catch (Throwable e) {
		}		
	}
	
	private Vector ordernaItensPorTabelaPreco(Vector itemPedidoPesoInvalidoList) {
		Vector newItemPedidoPesoInvalidoList = new Vector();
		for (int i = 0; i < tabelaPrecoPesoInvalidoList.size(); i++) {
			TabelaPreco tabelaPreco = (TabelaPreco) tabelaPrecoPesoInvalidoList.items[i];
			for (int j = 0; j < itemPedidoPesoInvalidoList.size(); j++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoPesoInvalidoList.items[j];
				if (itemPedido.cdTabelaPreco.equals(tabelaPreco.cdTabelaPreco)) {
					newItemPedidoPesoInvalidoList.addElement(itemPedido);
				}
			}
		}
		return newItemPedidoPesoInvalidoList;
	}

	//@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		StringBuilder str = new StringBuilder();
		ItemPedido itemPedido = (ItemPedido) domain;
		Vector item = new Vector();
		boolean validaPorItemTabela = LavenderePdaConfig.isValidaTabelaPrecoFechamentoItemPedido();

		item.addElement(StringUtil.getStringValue(itemPedido.getProduto()));
		item.addElement("");
		item.addElement("");

		str.setLength(0);
		str.append(itemPedido.getTabelaPreco().toString()).append(" ");
		item.addElement(StringUtil.getStringValue(str.toString()));
		item.addElement("");
		item.addElement("");
		
		str.setLength(0);
		str.append(Messages.PRODUTO_LABEL_RS).append(" ").append(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido)).append(" ");
		item.addElement(StringUtil.getStringValue(str.toString()));
    	
		str.setLength(0);
		if(validaPorItemTabela) {
			str.append(Messages.ITEMPEDIDO_PESO_MINIMO_LABEL_PESO_ATUAL_ITEM).append(" ").append(StringUtil.getStringValueToInterface(itemPedido.getTabelaPreco().qtPesoAcumuladoAtual));
			item.addElement(StringUtil.getStringValue(str.toString()));
		}
		else {
			item.addElement("");
		}
		
		str.setLength(0);
		str.append(Messages.ITEMPEDIDO_PESO_MINIMO_LABEL_PESO_MINIMO_PEDIDO).append(" ").append(StringUtil.getStringValueToInterface(itemPedido.getTabelaPreco().qtPesoMin));
		item.addElement(StringUtil.getStringValue(str.toString()));
		
    	return (String[]) item.toObjectArray();
	}

	//@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoService.getInstance();
	}

	//@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	//@Override
	protected void onFormStart() {
		UiUtil.add(this, lbDescricao, LEFT + HEIGHT_GAP, getTop() + HEIGHT_GAP);
		UiUtil.add(this, lbPesoPedido, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		reposition();
	}
	
	//@Override
	public void reposition() {
		super.reposition();
		lbDescricao.setText(Convert.insertLineBreak(width - 6, lbDescricao.fm, Messages.ITEMPEDIDO_PESOMINIMO_NAO_ATINGIDO_TABELA_PRECO));
		lbDescricao.setRect(LEFT + HEIGHT_GAP, getTop() + HEIGHT_GAP, PREFERRED, PREFERRED);
		lbPesoPedido.setRect(LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
		listContainer.reposition();
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
	   try {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFechar) {
					fecharWindow();
				}
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	public void list(Pedido pedidoBase) throws SQLException {
		this.pedido = pedidoBase;
		list();
	}
	
	public boolean hasItemPedidoAbaixoPeso() {
		return listContainer != null && listContainer.size() > 0;
	}
    
    private ItemPedido getSelectedItemPedido() throws SQLException {
    	ItemPedido itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(listContainer.getSelectedId());
    	return itemPedido;
    }
    
	public void detalhesClick() throws SQLException {
		cadItemPedidoForm.edit(getSelectedItemPedido());
		cadItemPedidoForm.onFormShow();
		unpop();
		cadItemPedidoForm.fromWindowItemPedidoAbaixoPesoMinimo = true;
		cadItemPedidoForm.setFocusInQtde();
	}

	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		cadItemPedidoForm.voltarClick();
	}
	
	//@Override
	protected void onPopup() {
		super.onPopup();
		lbPesoPedido.setText(new StringBuffer(Messages.ITEMPEDIDO_PESO_PEDIDO_ATUAL_LABEL).append(": ").append(StringUtil.getStringValueToInterface(this.pedido.qtPeso)).toString());
	}

}
