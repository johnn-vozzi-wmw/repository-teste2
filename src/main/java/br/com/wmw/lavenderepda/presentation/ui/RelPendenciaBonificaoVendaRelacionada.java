package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.MotivoPendenciaService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.sys.Convert;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelPendenciaBonificaoVendaRelacionada extends LavendereWmwListWindow {
	
	private Pedido pedido;
	private LabelName lbDescricao;
	
	public RelPendenciaBonificaoVendaRelacionada(Pedido pedido) {
		super(Messages.REL_ITENS_PENDENTES_TITULO);
		this.pedido = pedido;
		lbDescricao = new LabelName();
		lbDescricao.setText(Messages.REL_PENDENCIA_BONIFICACAO_VENDA_RELACIONADA_COLUNA_DESCRICAO);
		constructorListContainer();
		setDefaultRect();
	}
	private void constructorListContainer() {
		listContainer = new GridListContainer(2, 1);
		listContainer.setBarTopSimple();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return ((PedidoService) getCrudService()).getPedidoBonificacaoListByPedidoVenda(pedido);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Pedido pedidoRelacionado = (Pedido) domain;
		Vector item = new Vector(0);
		item.addElement(Messages.REL_PENDENCIA_BONIFICACAO_VENDA_RELACIONADA_MSG_NUMERO_PEDIDO + " " + StringUtil.getStringValue(pedidoRelacionado.nuPedido));
		item.addElement(Messages.REL_PENDENCIA_BONIFICACAO_VENDA_RELACIONADA_MSG_MOTIVO_PENDENCIA + " " + StringUtil.getStringValue(MotivoPendenciaService.getInstance().findMotivoPendenciaPrincipalPedidoErp(pedidoRelacionado)));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new Pedido();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbDescricao, getLeft(), getNextY());
		UiUtil.add(this, listContainer, getLeft(), getNextY(), FILL, FILL);
		reposition();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}
}
