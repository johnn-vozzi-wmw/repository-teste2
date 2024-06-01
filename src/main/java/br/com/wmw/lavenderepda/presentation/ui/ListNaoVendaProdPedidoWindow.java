package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NaoVendaProdPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.NaoVendaProdPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListNaoVendaProdPedidoWindow extends LavendereWmwListWindow  {
	
	private Pedido pedido;
	private LabelValue lbMensagem;

	public ListNaoVendaProdPedidoWindow(Pedido pedido) {
		super(Messages.PRODUTOS_SUGERIDOS_NAO_VENDIDOS);
		this.pedido = pedido;
		listContainer = new GridListContainer(1, 1);
		lbMensagem = new LabelValue("", CENTER);
		lbMensagem.autoSplit = true;
		lbMensagem.setText(Messages.SELECIONE_PRODUTO_MOTIVO_NAO_VENDA);
		setDefaultRect();
		btFechar.setText(Messages.BOTAO_CANCELAR);
		singleClickOn = true;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return pedido.produtosNaoInseridos;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Produto produto = (Produto) domain;
		String[] item = {StringUtil.getStringValue(produto)};
		return item;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		Produto produtoSelecionado = (Produto) ProdutoService.getInstance().findByRowKey(getSelectedRowKey());
		CadNaoVendaProdPedidoWindow cadNaoVendaProdPedidoWindow = new CadNaoVendaProdPedidoWindow(pedido, produtoSelecionado);
		cadNaoVendaProdPedidoWindow.setBaseListWindow(this);
		cadNaoVendaProdPedidoWindow.add();
		cadNaoVendaProdPedidoWindow.popup();
		if (cadNaoVendaProdPedidoWindow.closedByBtFechar == true) return;
		
		pedido.produtosNaoInseridos.removeElement(produtoSelecionado);
		if (ValueUtil.isNotEmpty(pedido.produtosNaoInseridos)) {
			list();
			return;
		}
		fecharWindow();
	}
	

	@Override
	protected CrudService getCrudService() throws SQLException {
		return NaoVendaProdPedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new NaoVendaProdPedido();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbMensagem , LEFT, getTop() + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
		
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}
	

}
