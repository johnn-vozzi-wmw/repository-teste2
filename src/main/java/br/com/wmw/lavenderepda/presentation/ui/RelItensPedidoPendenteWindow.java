package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelItensPedidoPendenteWindow extends WmwListWindow {
	
	private Vector itemPedidoPendenteList;

	public RelItensPedidoPendenteWindow(Vector itemPedidoPendenteList) {
		super(Messages.REL_ITEMPEDIDO_PENDENTES);
		this.itemPedidoPendenteList = itemPedidoPendenteList;
		listContainer = new GridListContainer(5, 2);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		colocaAtributosImparesNaDireita(listContainer.getItemCount());
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return this.itemPedidoPendenteList;
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		String[] item = {
			LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : StringUtil.getStringValue(itemPedido.cdProduto) + " - ",
			StringUtil.getStringValue(itemPedido.getDsProduto()),
			StringUtil.getStringAbreviada(getDsMotivoItemPedidoPendente(itemPedido), (int) (getWidth() * 0.75), listContainer.getFontSubItens()),
			ValueUtil.VALOR_NI,
			getVlPctAcrDescExtrapolado(itemPedido)};
		return item;
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemPedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, getTop() + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		//Sem Evento
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(c, domain);
		ItemPedido itemPedido = (ItemPedido) domain;
		c.setToolTip(StringUtil.getStringValue(itemPedido.dsMotivoItemPendentePedido) + ": " + getVlPctAcrDescExtrapolado(itemPedido));
	}

	private String getVlPctAcrDescExtrapolado(ItemPedido itemPedido) {
		if (itemPedido.vlPctDesconto > 0) {
			return StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto) + Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO;
		}
		if (itemPedido.vlPctAcrescimo > 0) {
			return StringUtil.getStringValueToInterface(itemPedido.vlPctAcrescimo) + Messages.ITEMPEDIDO_LABEL_VLPCTACRESCIMO;
		}
		return ValueUtil.VALOR_NI;
	}
	
	private String getDsMotivoItemPedidoPendente(ItemPedido itemPedido) {
		if (ValueUtil.isEmpty(itemPedido.dsMotivoItemPendentePedido)) {
			return Messages.MSG_ITEMPEDIDO_FICARA_PENDENTE;
		}
		return StringUtil.getStringValue(itemPedido.dsMotivoItemPendentePedido);
	}
	
	private void colocaAtributosImparesNaDireita(int nuAtributosInGrid) {
		for (int i = 3; i < nuAtributosInGrid; i += 2) {
			listContainer.setColPosition(i, RIGHT);
		}
	}
}
