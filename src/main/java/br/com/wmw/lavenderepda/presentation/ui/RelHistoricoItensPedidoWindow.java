package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.HistoricoItem;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.util.Vector;

public class RelHistoricoItensPedidoWindow extends WmwWindow {

	private BaseGridEdit grid;
	private Pedido pedido;
	private ItemPedido itemPedido;
	private LabelContainer lbDsProdutoContainer;

	public RelHistoricoItensPedidoWindow(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		super(Messages.ITEMPEDIDO_LABEL_HISTORICO);
		this.itemPedido = itemPedido;
		this.pedido = pedido;
		lbDsProdutoContainer = new LabelContainer(itemPedido.getDsProdutoWithKey(itemPedido));
		scrollable = false;
		setDefaultRect();
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, lbDsProdutoContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
		int oneChar = fm.stringWidth("A");
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_HISTORICO_GRID_DATA, oneChar * 10, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_HISTORICO_GRID_QUANTIDADE, oneChar * 8, RIGHT),
			new GridColDefinition(Messages.ITEMPEDIDO_HISTORICO_GRID_DESCONTO, oneChar * 6, RIGHT),
			new GridColDefinition(Messages.ITEMPEDIDO_HISTORICO_GRID_VALOR, oneChar * 10, RIGHT),
			new GridColDefinition(Messages.ITEMPEDIDO_HISTORICO_GRID_DOLAR, LavenderePdaConfig.mostraVlCotacaoDolarPedido ?  oneChar * 4 : 0, RIGHT),
			new GridColDefinition(Messages.ITEMPEDIDO_HISTORICO_GRID_VLITEMST, mostraValorItemComSt() ?  oneChar * 10 : 0, RIGHT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - footerH);
		carregaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	//@Override
	public void popup() {
		if (grid.size() == 0) {
			throw new ValidationException(Messages.ITEMPEDIDO_MSG_HISTORICO_ITEM);
		} else {
			super.popup();
		}
	}

	public void carregaGrid() throws SQLException {
		grid.setItems(null);
		Vector itensList = ItemPedidoService.getInstance().findHistoricoProdutosByCliente(pedido.cdCliente, itemPedido.cdProduto);
		if (itensList != null) {
			int size = itensList.size();
			HistoricoItem item;
			for (int i = 0; i < size; i++) {
				item = (HistoricoItem)itensList.items[i];
				String qtItemPrincipal;
				if (LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar) {
					qtItemPrincipal = StringUtil.getStringValueToInterface(item.qtItemPedidoUnElementar, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
				} else {
					qtItemPrincipal = StringUtil.getStringValueToInterface(item.qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
				}
				String[] itemGrid = {"",
					StringUtil.getStringValue(item.dtEmissao),
					qtItemPrincipal,
					StringUtil.getStringValueToInterface(item.vlPctDesconto),
					LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar ? StringUtil.getStringValueToInterface(item.vlItemPedidoUnElementar) : StringUtil.getStringValueToInterface(item.vlItemPedido),
					StringUtil.getStringValueToInterface(item.vlCotacaoDolar),
					LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? StringUtil.getStringValueToInterface(item.vlItemPedido + (item.qtItemFisico > 0 ? (item.vlStItem / item.qtItemFisico) : 0)) : StringUtil.getStringValueToInterface(item.vlItemPedido + item.vlStItem)};
				grid.add(itemGrid);
			}
			grid.qsort(1);
		}
		itensList = null;
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}
	
	private boolean mostraValorItemComSt() {
		return !LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar && (LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || LavenderePdaConfig.calculaStSimplificadaItemPedido);
	}

}
