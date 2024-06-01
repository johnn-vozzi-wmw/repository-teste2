package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;

public class RelPreviaDescontosWindow extends WmwWindow {

	private BaseGridEdit grid;
	private Pedido pedido;
	private LabelName lbVlBruto;
	private LabelValue edVlBruto;
	private LabelName lbPctDescProg;
	private LabelValue edPctDescProg;
	private LabelName lbPctDescCliente;
	private LabelValue edPctDescCliente;
	private LabelName lbVlLiquido;
	private LabelValue edVlLiquido;

	public RelPreviaDescontosWindow(Pedido pedido) {
		super(Messages.PEDIDO_PREVISAODESCONTOS_RELATORIO);
		lbVlBruto = new LabelName(Messages.PEDIDO_LABEL_VLTOTALPEDIDO);
		edVlBruto = new LabelValue("999999999");
		lbPctDescProg = new LabelName(Messages.PEDIDO_LABEL_PREVDESCPROGRESSIVO);
		edPctDescProg = new LabelValue("999999999");
		lbPctDescCliente = new LabelName(Messages.PEDIDO_LABEL_PREVDESCCLIENTE);
		edPctDescCliente = new LabelValue("999999999");
		lbVlLiquido = new LabelName(Messages.PEDIDO_LABEL_TOTALLIQUIDO);
        edVlLiquido = new LabelValue("999999999");
		edVlLiquido.setForeColor(ColorUtil.softGreen);
        this.pedido = pedido;
		makeUnmovable();
		setDefaultRect();
	}

	private void domainToScreen() throws SQLException {
		edVlBruto.setValue(pedido.vlTotalPedido);
		if (pedido.vlTotalPedido > 0) {
			double vlPctDescProgressivo = pedido.getVlPctDescProgressivo();
			edPctDescProg.setValue(vlPctDescProgressivo);
			if (LavenderePdaConfig.permiteEditarDescontoProgressivo && pedido.vlPctDescProgressivo <= vlPctDescProgressivo) {
				edPctDescProg.setValue(pedido.vlPctDescProgressivo);
			} else {
				pedido.vlPctDescProgressivo = vlPctDescProgressivo;
			}
    		Cliente cliente = pedido.getCliente();
    		if (cliente.vlIndiceFinanceiro == 0) {
    			edPctDescCliente.setValue(0);
    		} else {
    			edPctDescCliente.setValue(ValueUtil.round((1 - cliente.vlIndiceFinanceiro) * 100));
    		}
			aplicaDescontos();
			edVlLiquido.setValue(pedido.vlTotalPedido);
		}
		carregaGrid();
		PedidoService.getInstance().retiraDescontosDoFimDoPedido(pedido);
		PedidoService.getInstance().calculate(pedido);
		int size = pedido.itemPedidoList.size();
        for (int i = 0; i < size; i++) {
			String[] itemPedido = getItem(pedido.itemPedidoList.items[i]);
			grid.setCellText(i, 5, itemPedido[7]);
			grid.setCellText(i, 8, itemPedido[9]);
		}
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
        int oneChar = fm.charWidth('A');
        int tamColQtItemFaturamento = 0;
        if (LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida()) {
        	tamColQtItemFaturamento = oneChar * 8;
        }
		GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, oneChar * 8, LEFT),
            new GridColDefinition(Messages.PRODUTO_LABEL_DSPRODUTO, oneChar * 20, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFATURAMENTO, tamColQtItemFaturamento, RIGHT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, oneChar * 8, RIGHT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, oneChar * 8, RIGHT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO_PROG, oneChar * 6, RIGHT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO_PREV, oneChar * 8, RIGHT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO, oneChar * 8, RIGHT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO_PREV, oneChar * 8, RIGHT)};
        grid = UiUtil.createGridEdit(gridColDefiniton, false);
		addComponentsCentralizados();
		domainToScreen();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void addComponentsCentralizados() {
		UiUtil.add(this, lbVlBruto, BaseUIForm.CENTEREDLABEL, TOP + HEIGHT_GAP);
		UiUtil.add(this, edVlBruto, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbVlLiquido, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(this, edVlLiquido, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbPctDescProg, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(this, edPctDescProg, AFTER + WIDTH_GAP_BIG, SAME);
		if (LavenderePdaConfig.aplicaIndiceFinanceiroClientePorItemFinalPedido || LavenderePdaConfig.aplicaIndiceFinanceiroEspClientePorItemFinalPedido) {
			UiUtil.add(this, lbPctDescCliente, BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(this, edPctDescCliente, AFTER + WIDTH_GAP_BIG, SAME);
		}
		UiUtil.add(this, grid, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP  , FILL - WIDTH_GAP, FILL);
	}

	public void reposition() {
		super.reposition();
		addComponentsCentralizados();
	}

	public void carregaGrid() throws SQLException {
		int size = pedido.itemPedidoList.size();
        for (int i = 0; i < size; i++) {
			grid.add(getItem(pedido.itemPedidoList.items[i]));
		}
	}

	private void aplicaDescontos() throws SQLException {
		if (LavenderePdaConfig.aplicaSomenteMaiorDescontoPorItemFinalPedido) {
    		PedidoService.getInstance().aplicaMaiorDescontoFimPedidoNoPedido(pedido);
    	} else {
    		if (LavenderePdaConfig.isAplicaDescontoFimDoPedido() || LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
    			PedidoService.getInstance().isAplicaIndiceFinanceiroClienteNoPedido(pedido);
    			PedidoService.getInstance().isAplicaDescontoCCPNoPedido(pedido);
    			PedidoService.getInstance().isAplicaDescontoProgressivoNoPedido(pedido);
    		}
    	}
		int size = pedido.itemPedidoList.size();
        for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			ItemPedidoService.getInstance().calculateVlTotalItemPedido(pedido, itemPedido);
		}
		PedidoService.getInstance().calculate(pedido);
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        String[] item = {
                StringUtil.getStringValue(itemPedido.rowKey),
                StringUtil.getStringValue(itemPedido.cdProduto),
                StringUtil.getStringValue(itemPedido.getDsProduto()),
                StringUtil.getStringValueToInterface(itemPedido.qtItemFaturamento),
                StringUtil.getStringValueToInterface(itemPedido.getQtItemLista()),
                StringUtil.getStringValue(""), //Valor é setado depois
                StringUtil.getStringValueToInterface(itemPedido.vlPctDescPrev),
                StringUtil.getStringValueToInterface(itemPedido.vlItemPedido),
                StringUtil.getStringValue(""), //Valor é setado depois
                StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido)};
        return item;
    }

	//@Override
	public void popup() {
		if (pedido.vlTotalItens > 0) {
			super.popup();
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_FECHARPEDIDO_QTD_ITENS, pedido.nuPedido));
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

}
