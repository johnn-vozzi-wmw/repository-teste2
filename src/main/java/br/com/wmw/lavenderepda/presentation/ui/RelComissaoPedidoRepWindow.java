package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ComissaoPedidoRep;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ComissaoPedidoRepService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelComissaoPedidoRepWindow extends WmwListWindow {

	private Pedido pedido;
	private ItemPedido itemPedido;
	private ComissaoPedidoRep comissaoPedidoRepSelecionada;
	private Vector items;

	public RelComissaoPedidoRepWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException, ValidationException {
		super((pedido == null && itemPedido != null) ? Messages.COMISSAO_ITEMPEDIDO_REP_WINDOW : Messages.COMISSAO_PEDIDO_REP_WINDOW);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		setDefaultRect();
	}

	private ComissaoPedidoRep createComissaoPedidoRepFilter() {
		ComissaoPedidoRep comissaoPedidoRepFilter = new ComissaoPedidoRep();
		if (pedido == null && itemPedido != null) {
			populateFilter(comissaoPedidoRepFilter, itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdTabelaPreco);
			comissaoPedidoRepSelecionada = itemPedido.comissaoPedidoRep;
		} else if (pedido != null && itemPedido == null) {
			populateFilter(comissaoPedidoRepFilter, pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdTabelaPreco);
			comissaoPedidoRepSelecionada = pedido.comissaoPedidoRep;
		}
		return comissaoPedidoRepFilter;
	}

	private void populateFilter(ComissaoPedidoRep comissaoPedidoRepFilter, final String cdEmpresa, final String cdRepresentante, final String cdTabelaPreco) {
		comissaoPedidoRepFilter.cdEmpresa = cdEmpresa;
		comissaoPedidoRepFilter.cdRepresentante = cdRepresentante;
		comissaoPedidoRepFilter.cdTabelaPreco = cdTabelaPreco;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return items = ComissaoPedidoRepService.getInstance().findAllByExample(createComissaoPedidoRepFilter());
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		String[] item = new String[3];
		try {
			ComissaoPedidoRep comissaoPedidoRep = (ComissaoPedidoRep) domain;
			item = new String[] {
				StringUtil.getStringValue(comissaoPedidoRep.dsComissaoPedidoRep),
				comissaoPedidoRep.toString()
			};
	        int corFaixa = ComissaoPedidoRepService.getInstance().getCorIconeComissao(comissaoPedidoRep);
			grid.setImage(comissaoPedidoRep.toString(), UiUtil.getIconButtonAction(ComissaoPedidoRepService.IMAGES_ICONE_COMISSAO_PNG, corFaixa, true), false);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return item;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return null;
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ComissaoPedidoRepService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ComissaoPedidoRep();
	}

	@Override
	public void list() throws SQLException {
		super.list();
		afterList();
	}
	
	private void afterList() {
		int listSize = items.size();
		int selectedRow = 0;
		for (int i = 0; i < listSize; i++) {
			ComissaoPedidoRep comissaoPedidoRep = (ComissaoPedidoRep) items.items[i];
			if (comissaoPedidoRep.equals(comissaoPedidoRepSelecionada)) {
				selectedRow = i;
				break;
			}
		}
		grid.setSelectedIndex(selectedRow);
	}

	@Override
	protected void onFormStart() throws SQLException {
		int nextY = AFTER;
		if (pedido == null && itemPedido != null) {
			SessionContainer containerGrid = new SessionContainer();
			UiUtil.add(this, containerGrid, LEFT, getNextY(), FILL, UiUtil.getControlPreferredHeight());
			LabelValue labelDsProduto = new LabelValue(itemPedido.getProduto().toString().toUpperCase());
			labelDsProduto.setForeColor(ColorUtil.sessionContainerForeColor);
			UiUtil.add(containerGrid, labelDsProduto, CENTER, CENTER);
			nextY = getNextY();
		}
    	GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.COMISSAO_DESCRICAO_WINDOW, -75, LEFT),
			new GridColDefinition(Messages.COMISSAO_COR_WINDOW, -25, CENTER)
    	};
    	grid = UiUtil.createGridEdit(gridColDefiniton);
    	grid.setGridControllable();
        UiUtil.add(this, grid);
        grid.setRect(LEFT, nextY, FILL, FILL);
	}
	
}
