package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;

public class CadTotalizadoresPedidoWindow extends WmwWindow {


	private TotalizadoresPedidoForm totalizadoresPedidoForm;
	private LabelValue lbValorItem;
	private LabelValue lbUnidadesVendidas;
	private LabelValue lbValorTotal;
	private LabelValue lbTotalUnidade;
	private LabelValue lbTotaisMix;
	private LabelName lbTotaisItens;
	private LabelName lbTotaisPedido;
	private SessionContainer containerTotaisItens;
	private SessionContainer containerTotaisPedido;

	public CadTotalizadoresPedidoWindow(Pedido pedido, ItemPedido item) throws SQLException {
		super(Messages.GRADE_LABEL_BOTAO_TOTALIZADORES);
		
		totalizadoresPedidoForm = new TotalizadoresPedidoForm();
		totalizadoresPedidoForm.setPedido(pedido);
		totalizadoresPedidoForm.setItemPedido(item);
		totalizadoresPedidoForm.isWindow = true;
		totalizadoresPedidoForm.domainToScreen();
		
		inicializaCampos();
		scrollable = true;
		scBase.showScrollBarH = true;
		domainToScreen(item, pedido);
		setDefaultRect();
	}

	private void inicializaCampos() {
		lbValorItem = new LabelValue();
		lbUnidadesVendidas = new LabelValue();
		lbValorTotal = new LabelValue();
		lbTotalUnidade = new LabelValue();
		containerTotaisItens = new SessionContainer();
		containerTotaisItens.setBackColor(ColorUtil.componentsBackColorDark);
		containerTotaisPedido = new SessionContainer();
		containerTotaisPedido.setBackColor(ColorUtil.componentsBackColorDark);
		lbTotaisItens = new LabelName(Messages.GRADE_LABEL_TOTAIS_ITENS);
		lbTotaisPedido = new LabelName(Messages.GRADE_LABEL_TOTAIS_PEDIDO);
		lbTotaisMix = new LabelValue();
	}

	public void initUI() {
		try {
			super.initUI();
			totalizadoresPedidoForm.cadTotalizadoresPedidoWindow = this;
			UiUtil.add(this, totalizadoresPedidoForm, LEFT , getTop() , FILL , FILL);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, containerTotaisItens, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerTotaisItens, lbTotaisItens, CENTER, CENTER);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, new LabelName(Messages.GRADE_LABEL_VALOR_ITEM), lbValorItem, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, new LabelName(Messages.GRADE_LABEL_UNIDADES_VENDIDAS), lbUnidadesVendidas, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, containerTotaisPedido, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerTotaisPedido, lbTotaisPedido, CENTER, CENTER);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, new LabelName(Messages.GRADE_LABEL_VALOR_TOTAL), lbValorTotal, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, new LabelName(Messages.GRADE_LABEL_TOTAIS_MIX_ITEM), lbTotaisMix, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(totalizadoresPedidoForm.scroolContainer, new LabelName(Messages.GRADE_LABEL_TOTAL_UNIDADES), lbTotalUnidade, getLeft(), AFTER + HEIGHT_GAP);
			
		} catch (Throwable e) {e.printStackTrace();}
	}

	private void domainToScreen(ItemPedido item, Pedido pedido) throws SQLException {
		double valorItem = item.vlBaseItemPedido * item.getQtItemFisico();
		double valorTotal = getValorTotal(pedido, item);
		double valorTotalGradesJaLancadas = getValorTotalGradesJaLancadas(pedido, item);
		double totalUnidade = valorTotal == 0 ? item.getQtItemFisico() : totalizadoresPedidoForm.getQtItensLista(pedido, item) + item.getQtItemFisico();
		lbValorItem.setValue(valorItem);
		lbUnidadesVendidas.setValue(StringUtil.getStringValueToInterface(item.getQtItemFisico() , LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
		lbValorTotal.setValue((valorTotal == 0 ? valorItem : valorTotal + valorItem - valorTotalGradesJaLancadas));
		lbTotalUnidade.setValue(StringUtil.getStringValueToInterface(totalUnidade , LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
		lbTotaisMix.setValue(getItensSize(pedido, valorItem, valorTotal, item));
	}

	private int getItensSize(Pedido pedido, double valorItem, double valorBase, ItemPedido itemPedido) throws SQLException {
		int itensSize = pedido.itemPedidoList != null ? pedido.itemPedidoList.size() : 0;
		if (valorBase == 0 && valorItem == 0 && pedido.itemPedidoList == null) {
			return 0;
		}
		boolean itemJaLancado = ItemPedidoService.getInstance().isItemJaInseridoMesmaUnidade(pedido, itemPedido);
		itensSize = valorItem == 0 && itemJaLancado ? itensSize - 1 : itensSize;
		itensSize = valorItem != 0 && !itemJaLancado ? itensSize + 1 : itensSize;
		return  itensSize;
	}
	
	private double getValorTotal(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		return ItemPedidoService.getInstance().getValorTotalTotalizadoresPedido(pedido, itemPedido);
	}
	
	private double getValorTotalGradesJaLancadas(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		return ItemPedidoService.getInstance().getValorTotalGradesJaLancadasPedido(pedido, itemPedido);
	}
	
}
