package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.sys.Convert;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelProdutosRentabilidadeSemAlcadaWindow extends WmwListWindow {
	
	private LabelValue lbMensagem;
	
	private Vector itemPedidoList;

	public RelProdutosRentabilidadeSemAlcadaWindow(String mensagem, Vector itemPedidoList) {
		super(Messages.MARGEMRENTABFAIXA_REL_TITULO_TELA);
		lbMensagem = new LabelValue(mensagem, CENTER);
		this.itemPedidoList = itemPedidoList;
		constructorListContainer();
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		if (LavenderePdaConfig.usaMotivoPendenciaAgrupado()) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[0];
			ItemPedido itemPedidoFilter = new ItemPedido(itemPedido.pedido);
			itemPedido.cdMargemRentab = itemPedido.cdMargemRentab;
			return getCrudService().findAllByExample(itemPedidoFilter);
			
		}
		
		return  itemPedidoList;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		Produto produto = itemPedido.getProduto();
		String[] item = { produto.toString() };
		return item;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return null;
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
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbMensagem, LEFT, getNextY());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		ajustaComponentes();
	}
	
	@Override
	public void reposition() {
		super.reposition();
		ajustaComponentes();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException { }
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(1, 1);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }
	
	private void ajustaComponentes() {
		lbMensagem.setText(Convert.insertLineBreak(width - 6, lbMensagem.fm, lbMensagem.getText()));
		lbMensagem.setRect(CENTER, getTop() + HEIGHT_GAP, width, PREFERRED);
		listContainer.reposition();
	}

}
