package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ValidacaoIemPedidoNuOrdemDuplicadaWindow extends WmwWindow {

	public BaseGridEdit grid;
	private EditMemo edDescricao;
	public boolean possuiItensDuplicadosComOrdensDuplicadas;
	Vector itemPedidoList;
	Pedido pedido;

	public ValidacaoIemPedidoNuOrdemDuplicadaWindow(Pedido pedido) {
		super(Messages.TITULO_TELA_ORDEMCOMPRA);
		this.pedido = pedido;
		edDescricao = new EditMemo("@@@@@@@@@@", 4, 155);
		edDescricao.setText(Messages.ITEMPEDIDO_DUPLICADO_ORDEMCOMPRA_DUPLICADA);
		edDescricao.setEditable(false);
		edDescricao.transparentBackground = true;
		edDescricao.drawDots = false;
		setDefaultRect();
	}

	public void montaGrid() {
		UiUtil.add(this, edDescricao, getLeft(), getNextY(), getWFill());
		GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_CDPRODUTO, -50, LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_NU_ORDEM_COMPRA, -50, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, getLeft(), getNextY(), FILL, FILL);
	}

	@Override
	public void initUI() {
		super.initUI();
		montaGrid();
		addBtFechar();
		try {
			carregaGrid();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	public void carregaGrid() throws SQLException {
		grid.clear();
		itemPedidoList = ItemPedidoService.getInstance().getItemPedidoDuplicadosOrdemCompraDuplicadaByPedido(pedido);
		int size = itemPedidoList.size();
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) itemPedidoList.items[i];
			String[] item = {
					StringUtil.getStringValue(itemPedido.cdProduto),
					StringUtil.getStringValue(itemPedido.nuOrdemCompraCliente),
			};
			grid.add(item);
		}
		possuiItensDuplicadosComOrdensDuplicadas = grid.size() > 0;
	}

	@Override
	public void popup() {
		if (possuiItensDuplicadosComOrdensDuplicadas) {
			super.popup();
		}
	}

}