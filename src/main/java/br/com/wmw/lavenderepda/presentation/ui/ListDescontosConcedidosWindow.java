package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoDescErp;
import br.com.wmw.lavenderepda.business.service.PedidoDescErpService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDescontosConcedidosWindow extends WmwListWindow {
	
	private Pedido pedido;

	public ListDescontosConcedidosWindow(Pedido pedido) {
		super(LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() || LavenderePdaConfig.isUsaMotivoPendencia() ? Messages.PEDIDODESCERP_PEDIDO_PENDENTE : Messages.PEDIDODESCERP_NOME_ENTIDADE);
		this.pedido = pedido;
		int itemCount = !LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() && !LavenderePdaConfig.isUsaMotivoPendencia() ? 6 : 4;
		listContainer = new GridListContainer(itemCount, 2);
		listContainer.setBarTopSimple();
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		if (!LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() && !LavenderePdaConfig.isUsaMotivoPendencia()) {
			listContainer.setColPosition(5, RIGHT);
		}
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return PedidoDescErpService.getInstance().getPedidoDescErpList(pedido, OrigemPedido.FLORIGEMPEDIDO_ERP);
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		Vector itens = new Vector(0);
		PedidoDescErp pedidoDescErp = (PedidoDescErp) domain;
		itens.addElement(Messages.PEDIDODESCERP_LABEL_NUSEQUENCIA + ": " + StringUtil.getStringValue(pedidoDescErp.nuSequencia));
		itens.addElement(StringUtil.getStringValue(pedidoDescErp.nmUsuario));
		if (!LavenderePdaConfig.isUsaMultiplasLiberacoesParaPedidoPendente() && !LavenderePdaConfig.isUsaMotivoPendencia()) {
			itens.addElement(Messages.PEDIDODESCERP_LABEL_VLPCTDESCONTOLIBERADO + ": " + StringUtil.getStringValueToInterface(pedidoDescErp.vlPctDescontoLiberado));
			itens.addElement(Messages.PEDIDODESCERP_LABEL_VLDESCONTOLIBERADO + ": " + StringUtil.getStringValueToInterface(pedidoDescErp.vlDescontoLiberado));
		}
		itens.addElement(Messages.PEDIDODESCERP_LABEL_DTLIBERACAO + ": " + StringUtil.getStringValue(pedidoDescErp.dtLiberacao));
		itens.addElement(Messages.PEDIDODESCERP_LABEL_HRLIBERACAO + ": " + StringUtil.getStringValue(pedidoDescErp.hrLiberacao));
		return (String[]) itens.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return PedidoDescErpService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new PedidoDescErp();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		//--
	}

}
