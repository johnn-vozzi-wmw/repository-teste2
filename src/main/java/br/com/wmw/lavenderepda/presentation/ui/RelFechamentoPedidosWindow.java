package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelFechamentoPedidosWindow extends WmwWindow {

	private BaseGridEdit grid;
	private LabelValue lbDescricao1;
	private LabelValue lbDescricao2;
	private String pedidosNaoFechados;
	private ButtonPopup btForcarFechamentoPedidos;

	public RelFechamentoPedidosWindow(String pedNaoFechados) {
		super(Messages.PEDIDO_FECHAMENTO_RELATORIO);
		lbDescricao1 = new LabelValue(Messages.PEDIDO_MSG_FECHARPEDIDOS_ERRO_PART1, CENTER);
		lbDescricao2 = new LabelValue(Messages.PEDIDO_MSG_FECHARPEDIDOS_ERRO_PART2, CENTER);
		pedidosNaoFechados = pedNaoFechados;
		makeUnmovable();
		btForcarFechamentoPedidos = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDOS_ABR);
		scrollable = false;
		setDefaultRect();
	}

	//@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbDescricao1, CENTER + WIDTH_GAP, TOP);
		UiUtil.add(this, lbDescricao2, CENTER + WIDTH_GAP, AFTER);
        int oneChar = fm.charWidth('A');
        StringBuffer dsErro = new StringBuffer();
        dsErro.append(FrameworkMessages.TITULO_MSG_ERRO);
    	int widthDsErro = oneChar * 40;
    	while (fm.stringWidth(dsErro.toString()) < widthDsErro) {
    		dsErro.append("   ");
    	}
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
												new GridColDefinition(Messages.PEDIDO_LABEL_NUPEDIDO, oneChar * 15, LEFT),
												new GridColDefinition(dsErro.toString(), 25, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, FILL - WIDTH_GAP, FILL - footerH);
		carregaGrid();
		addButtonPopup(btForcarFechamentoPedidos);
		addButtonPopup(btFechar);
	}

	public void carregaGrid() {
		String[] pedidosErros = StringUtil.split(pedidosNaoFechados, '#');
		boolean podeForcarFechamentoPedidos = true;
		for (int i = 0; i < pedidosErros.length; i++) {
			String[] dados = StringUtil.split(pedidosErros[i], '*');
			String messageErro = dados[3].replace('|', ' ');
			if (ValueUtil.valueEquals(messageErro, Messages.PEDIDO_MSG_ERRO_RESERVA_ESTOQUE_SEM_ESTOQUE)) {
				messageErro = Messages.PEDIDO_MSG_VALIDACAO_ERRO_RESERVA_CORRENTE_REL_FECHAMENTO;
			}
			String[] pedidoErro = {dados[0], dados[1], messageErro};
			if (!dados[2].equalsIgnoreCase("ValidationValorMinPedidoException")) {
				podeForcarFechamentoPedidos = false;
			}
			grid.add(pedidoErro);
		}
		if (!podeForcarFechamentoPedidos || !LavenderePdaConfig.apenasAvisaValorMinimoParaPedido) {
			btForcarFechamentoPedidos.setVisible(true);
		}
		ajustaTamanhoBotoes();
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btForcarFechamentoPedidos) {
					btForcarFechamentoPedidosClick();
				}
				break;
			}
		}
	}

	private void btForcarFechamentoPedidosClick() {
		try {
			//Fecha os pedido selecionados da lista
			Vector pedidosGrid = grid.getItemsVector();
			Vector pedidoList = new Vector();
			Pedido pedido = null;
			for (int i = 0; i < pedidosGrid.size(); i++) {
				String[] row = (String[]) pedidosGrid.items[i];
				pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(row[0]);
				if (pedido != null) {
					pedido.ignoraValidacaoSugestaoItensRentabilidadeIdeal = true;
					pedido.ignoraValidacaoProdutosPendentes = true;
					pedido.ignoraValidacaoAtrasoCliente = true;
					pedidoList.addElement(pedido);
				}
			}
			pedidosNaoFechados = PedidoService.getInstance().fecharPedidos(pedidoList, false, true, true);
			if (ValueUtil.isEmpty(pedidosNaoFechados)) {
				UiUtil.showConfirmMessage(Messages.PEDIDO_MSG_FECHARPEDIDOS_SUCESSO);
				this.unpop();
			} else {
				UiUtil.showErrorMessage("Problema ao fechar alguns pedidos.");
			}
			PedidoUiUtil.mostraAvisosAposFechamentoPedidoPelaLista(pedidoList);
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
	}

}
