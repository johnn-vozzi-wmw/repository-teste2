package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListUnidadeAlternativaWindow extends WmwWindow {

	private BaseGridEdit gridUnidadesProduto;
	private Pedido pedido;
	private ItemPedido itemPedido;
	public String cdUnidadeAlternativaSelecionada;

	public ListUnidadeAlternativaWindow(Pedido pedido, ItemPedido itemPedido) {
		super(Messages.LABEL_PRECO_UNIDADE);
		this.itemPedido = itemPedido;
		this.pedido = pedido;
		setDefaultRect();
	}

	public void initUI() {
	   try {
		super.initUI();
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.METAS_UNIDADE, -35, LEFT),
			new GridColDefinition(Messages.UNIDADE_LABEL_QT_ELEMENTAR, -25, RIGHT),
			new GridColDefinition(Messages.UNIDADE_LABEL_VL_ELEMENTAR, -25, RIGHT),
			new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -25, RIGHT),
			new GridColDefinition(Messages.UNIDADE_LABEL_FATOR, -25, RIGHT) };
		gridUnidadesProduto = UiUtil.createGridEdit(gridColDefiniton);
		UiUtil.add(this, gridUnidadesProduto, LEFT, TOP + HEIGHT_GAP, FILL, FILL);
		carregaGridUnidadesProduto();
	   } catch (Throwable ee) {ee.printStackTrace();}
	}

	private void carregaGridUnidadesProduto() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = new Vector(0);
		String[][] gridItems;
		try {
			gridUnidadesProduto.setItems(null);
			//--
			domainList = ProdutoUnidadeService.getInstance().findAllByProduto(itemPedido, itemPedido.getProduto(), itemPedido.getItemTabelaPreco().cdTabelaPreco);
			listSize = domainList.size();
			//--
			if (listSize > 0) {
				String[] item = getItemProdutoUnidade((ProdutoUnidade) domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItemProdutoUnidade((ProdutoUnidade) domainList.items[i]);
				}
				//--
				gridUnidadesProduto.setItems(gridItems);
				gridUnidadesProduto.qsort(3);
			}
		} finally {
			gridItems = null;
			domainList = null;
			msg.unpop();
		}
	}

	protected String[] getItemProdutoUnidade(ProdutoUnidade produtoUnidade) throws SQLException {
		itemPedido.cdUnidade = produtoUnidade.cdUnidade;
		PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
		//--
		permitePopUp();
		String[] item = { produtoUnidade.cdUnidade,
				produtoUnidade.toString(),
				StringUtil.getStringValueToInterface(ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade)),
				StringUtil.getStringValueToInterface(itemPedido.vlEmbalagemElementar),
				StringUtil.getStringValueToInterface(itemPedido.vlItemPedido),
				StringUtil.getStringValueToInterface(produtoUnidade.vlIndiceFinanceiro)};
		return item;
	}

	private void permitePopUp() {
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco && itemPedido.usandoPrecoUnidadePadraoConvertida) {
			throw new ValidationException(Messages.UA_SEM_PRECO);
		}
	}

	public void onEvent(Event event) {
	   try {
		super.onEvent(event);
		switch (event.type) {
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == gridUnidadesProduto) && (gridUnidadesProduto.getSelectedIndex() != -1)) {
					String[] item = gridUnidadesProduto.getSelectedItem();
					cdUnidadeAlternativaSelecionada = item[0];
					fecharWindow();
				}
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}

}
