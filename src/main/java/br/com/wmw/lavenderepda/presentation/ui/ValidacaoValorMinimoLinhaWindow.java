package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ConfigValorMinimoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigValorMinimoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;

import java.sql.SQLException;
import java.util.HashMap;

public class ValidacaoValorMinimoLinhaWindow extends WmwWindow {

	public BaseGridEdit grid;
	public boolean possuiLinhaAbaixoVlMinimo;
	HashMap<String, String> linhaPedidoUsaAgrupamentoHash;
	Pedido pedido;

	public ValidacaoValorMinimoLinhaWindow(Pedido pedido) {
		super(Messages.TITULO_TELA_LINHA);
		this.pedido = pedido;
		setDefaultRect();
	}

	public void montaGrid() {
		UiUtil.add(this, new LabelValue(Messages.PEDIDO_VL_MINIMO_LINHA_ERRO), AFTER + WIDTH_GAP, SAME);
		GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(Messages.LABEL_LINHA, -40, CENTER),
				new GridColDefinition(Messages.LINHA_VLMINIMO_PEDIDO, -30, CENTER),
				new GridColDefinition(Messages.PEDIDO_VLITEMS_LINHA, -30, CENTER)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
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
		linhaPedidoUsaAgrupamentoHash = ConfigValorMinimoDao.getInstance().findLinhaUsaFlAgrupamento(pedido);
		for (String cdLinha : linhaPedidoUsaAgrupamentoHash.keySet()) {
			String dsLinha = linhaPedidoUsaAgrupamentoHash.get(cdLinha);
			double vlMinPedidoLinha = ValueUtil.round(ConfigValorMinimoService.getInstance().findValorMinimoPedidoPorLinha(pedido, cdLinha));
			double vlTotalItemsLinhaPedido = ValueUtil.round(ItemPedidoPdbxDao.getInstance().sumVlTotalItemsPedidoPorLinha(cdLinha, pedido));
			if (vlTotalItemsLinhaPedido < vlMinPedidoLinha) {
				String[] item = {
						StringUtil.getStringValue(StringUtil.resume(dsLinha, 10)),
						StringUtil.getStringValue(vlMinPedidoLinha),
						StringUtil.getStringValue(vlTotalItemsLinhaPedido),
				};
				grid.add(item);
			}
		}
		possuiLinhaAbaixoVlMinimo = grid.size() > 0;
	}

	@Override
	public void popup() {
		if (possuiLinhaAbaixoVlMinimo) {
			super.popup();
		}
	}

}
