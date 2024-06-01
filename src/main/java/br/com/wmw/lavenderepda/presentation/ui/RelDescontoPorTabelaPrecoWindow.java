package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.sys.Settings;
import totalcross.ui.Grid;
import totalcross.ui.Window;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelDescontoPorTabelaPrecoWindow extends WmwListWindow {

	private Produto produto;
	private ButtonPopup btSelecionar;
	public ItemTabelaPreco itemTabelaPreco;
	private boolean inItemPedido = false;
	private Vector listTabelaPreco;
	private Pedido pedido;
	private LabelContainer lbDsProdutoContainer;

	public RelDescontoPorTabelaPrecoWindow(Produto produto) throws SQLException {
		this(produto, TabelaPrecoService.getInstance().findAllByEmpAndRep().items, null);
	}

	public RelDescontoPorTabelaPrecoWindow(Produto produto, Object[] listTabelaPreco, Pedido pedido) {
		super(Messages.PEDIDO_DESCONTOPORTABPRECO_RELATORIO);
		this.produto = produto;
		btSelecionar = new ButtonPopup(Messages.BOTAO_SELECIONAR);
		this.listTabelaPreco = new Vector(0);
		lbDsProdutoContainer = new LabelContainer(this.produto.toString());
		if (ValueUtil.isNotEmpty(listTabelaPreco)) {
			this.listTabelaPreco = new Vector(listTabelaPreco);
		}
		if (pedido != null) {
			this.pedido = pedido;
			this.inItemPedido = true;
		}
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -33, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLDESCONTO, -33, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_PRECO_COM_DESCONTO, -33, CENTER)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		calculaTamanhoTela();
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return TabelaPrecoService.getInstance();
	}

	protected void calculaTamanhoTela() {
		if ((LavenderePdaConfig.percAlturaTelaDescontoTabelaPreco > 0) && (LavenderePdaConfig.percAlturaTelaDescontoTabelaPreco <= 100)) {
			int alturaTela = (int) ((LavenderePdaConfig.percAlturaTelaDescontoTabelaPreco / 100.0) * Settings.screenHeight);
			int x = (Settings.screenWidth * (10 / 2)) / 100;
			setRect(LEFT + x, CENTER, FILL - x, alturaTela);
		} else {
			setDefaultRect();
		}
	}

	@Override
	public void screenResized() {
		super.screenResized();
		try {
			calculaTamanhoTela();
			initUI();
		} catch (Throwable ex) {
			setDefaultRect();
			initUI();
		}
		reposition();
	}

	@Override
	public void popup() {
		if (produto.cdProduto != null) {
			super.popup();
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID, Messages.PRODUTO_LABEL_CODIGO));
		}
	}

	@Override
	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

	protected BaseDomain getDomainFilter() {
		return new ItemTabelaPreco();
	}

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
		itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemTabelaPrecoFilter.cdProduto = produto.cdProduto;
		itemTabelaPrecoFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemTabelaPrecoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemTabelaPrecoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemTabelaPrecoFilter.cdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
		itemTabelaPrecoFilter.qtItem = ItemTabelaPreco.QTITEM_VALOR_PADRAO;
		itemTabelaPrecoFilter.cdPrazoPagtoPreco = LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() ? pedido.getCondicaoPagamento().cdPrazoPagtoPreco : ItemTabelaPreco.CDPRAZOPAGTOPRECO_VALOR_PADRAO;
		Vector listFinal = new Vector(0);
		if (ValueUtil.isNotEmpty(listTabelaPreco)) {
			int size = this.listTabelaPreco.size();
			for (int i = 0; i < size; i++) {
				if ((TabelaPreco)listTabelaPreco.items[i] == null) continue;
				itemTabelaPrecoFilter.cdTabelaPreco = ((TabelaPreco)listTabelaPreco.items[i]).cdTabelaPreco;
				ItemTabelaPreco itemTabPreco = (ItemTabelaPreco) ItemTabelaPrecoService.getInstance().findByRowKey(itemTabelaPrecoFilter.getRowKey());
				if (itemTabPreco != null) {
					if (inItemPedido) {
						ItemPedido itemPedidoFilter = new ItemPedido();
						itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
						itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
						itemPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
						itemPedidoFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						itemPedidoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						itemPedidoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						itemPedidoFilter.cdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
						itemPedidoFilter.cdUfClientePedido = pedido.getCliente().dsUfPreco;
						itemPedidoFilter.pedido = pedido;
						itemPedidoFilter.setProduto(produto);
						itemPedidoFilter.setItemTabelaPreco(itemTabPreco);
						itemPedidoFilter.cdTabelaPreco = itemTabPreco.cdTabelaPreco;
						itemPedidoFilter.nuPedido = pedido.nuPedido;
						PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedidoFilter);
						listFinal.addElement(itemPedidoFilter);
					} else {
						listFinal.addElement(itemTabPreco);
					}
				}
			}
		}
		return listFinal;
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		if (inItemPedido) {
			ItemPedido itemPedido = (ItemPedido)domain;
			double vlPctMaxDesconto = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			double vlPrecoComDesconto = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * vlPctMaxDesconto) / 100);
			String[] item = {
					itemPedido.getItemTabelaPreco().getRowKey(),
					StringUtil.getStringValueToInterface(itemPedido.vlBaseItemPedido),
					StringUtil.getStringValueToInterface(vlPctMaxDesconto) + "%",
					StringUtil.getStringValueToInterface(vlPrecoComDesconto)
			};
			return item;
		} else {
			ItemTabelaPreco itemTabPreco = (ItemTabelaPreco) domain;
			double vlPctMaxDesconto = itemTabPreco.getVlPctMaxDescontoItemTabelaPreco(null);
			double vlPrecoComDesconto = itemTabPreco.vlBase - ((itemTabPreco.vlBase * vlPctMaxDesconto) / 100);
			String[] item = {
					itemTabPreco.getRowKey(),
					StringUtil.getStringValueToInterface(itemTabPreco.vlUnitario),
					StringUtil.getStringValueToInterface(vlPctMaxDesconto) + "%",
					StringUtil.getStringValueToInterface(vlPrecoComDesconto)
			};
			return item;
		}
	}

	protected String getSelectedRowKey() {
		return grid.getSelectedItem()[0];
	}

	protected void onFormStart() {
		UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		if (inItemPedido) {
			addButtonPopup(btSelecionar);
		}
		addButtonPopup(btFechar);
	}
	
	@Override
	protected void addBtFechar() {
		// Não utilizado nesta tela
	}

	@Override
	public void doubleClick() throws SQLException {
		if (inItemPedido) {
			detalhesClick();
		}
	}

	@Override
	public void detalhesClick() throws SQLException {
		int indexSelected = grid.getSelectedIndex();
		if (indexSelected >= 0) {
			itemTabelaPreco = (ItemTabelaPreco) ItemTabelaPrecoService.getInstance().findByRowKey(getSelectedRowKey());
			fecharWindow();
		} else {
			UiUtil.showInfoMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO);
			Window.repaintActiveWindows();

		}
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSelecionar) {
					detalhesClick();
				}
				break;
			}
		}
	}

	@Override
	public void reposition() {
		super.reposition();
		for (int i = 1; i <= 3; i++) {
			grid.setColumnWidth(i, -33);
		}
		Grid.repaint();
	}

}
