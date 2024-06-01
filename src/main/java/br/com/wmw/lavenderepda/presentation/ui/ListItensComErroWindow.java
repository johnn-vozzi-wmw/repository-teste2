package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItensComErroWindow extends WmwListWindow {
	
	private LabelValue lbDescricao;
	private Map<ItemPedido,ValidationException> itensPedidoComErroAndExceptionMap;
	
	public ListItensComErroWindow(Pedido pedido) throws SQLException {
		super(Messages.ITEMPEDIDO_ERRO_TITULO);
		this.itensPedidoComErroAndExceptionMap = pedido.itensComErroAndExceptionMap;
		lbDescricao = new LabelValue(Messages.ITEMPEDIDO_ERRO_DESCRICAO, CENTER);
		constructorListContainer();
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(3, 2);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector itensDoPedidoComErro = new Vector(0);
		itensDoPedidoComErro.addElements(itensPedidoComErroAndExceptionMap.keySet().toArray());
		return itensDoPedidoComErro;
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
	protected String[] getItem(Object domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		String[] item = {
			LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : StringUtil.getStringValue(itemPedido.cdProduto) + " - ",
			StringUtil.getStringValue(itemPedido.getDsProduto()),
			StringUtil.getStringAbreviada(itensPedidoComErroAndExceptionMap.get(itemPedido).getMessage(), (int) (getWidth() * 0.75), listContainer.getFontSubItens()),
		};
		return item;
	}
	
	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}
	
	@Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
    	return itensPedidoComErroAndExceptionMap.get(itemPedido).getMessage();
    }

	@Override
	protected void onFormStart() throws SQLException {
		lbDescricao.setText(MessageUtil.quebraLinhas(Messages.ITEMPEDIDO_ERRO_DESCRICAO, width - 20));
    	UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		// Sem Evento
	}
}
