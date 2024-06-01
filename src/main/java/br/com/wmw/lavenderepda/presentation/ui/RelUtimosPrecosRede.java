package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelUtimosPrecosRede extends WmwListWindow {
	
	private ItemPedido itemPedido;
	private String cdRede;

	public RelUtimosPrecosRede(ItemPedido itemPedido, String cdRede) throws SQLException {
		super(itemPedido.getProduto().toString());
		this.itemPedido = itemPedido;
		this.cdRede = cdRede;
		constructorListContainer();
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(3, RIGHT);
		listContainer.atributteSortSelected = sortAtributte = Pedido.NMCOLUNA_DTEMISSAO;
		listContainer.sortAsc = sortAsc = ValueUtil.VALOR_NAO;
		listContainer.setColsSort(new String[][] {{Messages.PEDIDO_LABEL_DTEMISSAO, Pedido.NMCOLUNA_DTEMISSAO}, {Messages.CODIGO, "CLI.CDCLIENTE"}, {Messages.CLIENTE_LABEL_NMFANTASIA_LISTA, "CLI.NMFANTASIA"}});
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return ClienteService.getInstance().findUltimosPrecosClienteRede((ItemPedido)domain, cdRede);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Cliente cliente = (Cliente) domain;
		StringBuffer buf = new StringBuffer();
		String[] item = new String[4];
		item[0] = buf.append(cliente.cdCliente).append(" - ").toString();
		item[1] = cliente.nmFantasia;
		buf.setLength(0);
		item[2] = buf.append(Messages.PEDIDO_LABEL_DTEMISSAO).append(" ").append(DateUtil.formatDateDDMMYYYY(cliente.dtEmissao)).toString();
		buf.setLength(0);
		item[3] = buf.append(FrameworkMessages.LABEL_RS).append(" ").append(StringUtil.getStringValueToInterface(cliente.vlUltimoPreco)).toString();
		return item;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ClienteService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return itemPedido;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

}
