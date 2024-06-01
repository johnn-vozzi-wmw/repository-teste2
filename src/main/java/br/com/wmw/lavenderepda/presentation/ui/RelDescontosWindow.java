package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescProgQtdService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;

public class RelDescontosWindow extends WmwWindow {
	
	private Pedido pedido;
	private LabelName lbDescProgressivoMixItens;
	private LabelValue lvDescProgressivoMixItens;
	private LabelName lbDescProgressivoItemFinalPedido;
	private LabelValue lvDescProgressivoItemFinalPedido;
	private LabelName lbIndiceFinanceiroCondComercial;
	private LabelValue lvIndiceFinanceiroCondComercial;
	private LabelName lbIndiceFinanceiroCondPagto;
	private LabelValue lvIndiceFinanceiroCondPagto;
	
	public RelDescontosWindow(Pedido pedido) {
		super(Messages.BOTAO_REL_DESCONTOS);
		this.pedido = pedido != null ? pedido : new Pedido();
		lbDescProgressivoMixItens = new LabelName(this.pedido.isPedidoAberto() ? Messages.REL_DESCONTOS_LABEL_DESC_PROGRESSIVO_MIX_ITENS_PREVISAO : Messages.REL_DESCONTOS_LABEL_DESC_PROGRESSIVO_MIX_ITENS);
		lvDescProgressivoMixItens = new LabelValue();
		lvDescProgressivoMixItens.usePercentValue = true;
		lbDescProgressivoItemFinalPedido = new LabelName(this.pedido.isPedidoAberto() ? Messages.REL_DESCONTOS_LABEL_DESC_PROGRESSIVO_ITEM_FINAL_PEDIDO_PREVISAO : Messages.REL_DESCONTOS_LABEL_DESC_PROGRESSIVO_ITEM_FINAL_PEDIDO);
		lvDescProgressivoItemFinalPedido = new LabelValue();
		lvDescProgressivoItemFinalPedido.usePercentValue = true;
		lbIndiceFinanceiroCondComercial = new LabelName();
		lvIndiceFinanceiroCondComercial = new LabelValue();
		lvIndiceFinanceiroCondComercial.usePercentValue = true;
		lbIndiceFinanceiroCondPagto = new LabelName();
		lvIndiceFinanceiroCondPagto = new LabelValue();
		lvIndiceFinanceiroCondPagto.usePercentValue = true;
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
	   try {
		super.initUI();
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			UiUtil.add(this, lbDescProgressivoMixItens, lvDescProgressivoMixItens, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido()) {
			UiUtil.add(this, lbDescProgressivoItemFinalPedido, lvDescProgressivoItemFinalPedido, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			lbIndiceFinanceiroCondComercial.setText(getLabelCondComercial());
			UiUtil.add(this, lbIndiceFinanceiroCondComercial, lvIndiceFinanceiroCondComercial, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			lbIndiceFinanceiroCondPagto.setText(getLabelCondPagto());
			UiUtil.add(this, lbIndiceFinanceiroCondPagto, lvIndiceFinanceiroCondPagto, getLeft(), getNextY());
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private String getLabelCondComercial() throws SQLException {
		if (pedido.getCondicaoComercial().vlIndiceFinanceiro > 1) {
			return Messages.REL_DESCONTOS_LABEL_ACRESC_COND_COMERCIAL;
		} else {
			return Messages.REL_DESCONTOS_LABEL_DESC_COND_COMERCIAL;
		}
	}
	
	private String getLabelCondPagto() throws SQLException {
		if (pedido.getCondicaoPagamento().vlIndiceFinanceiro > 1) {
			return Messages.REL_DESCONTOS_LABEL_ACRESC_COND_PAGTO;
		} else {
			return Messages.REL_DESCONTOS_LABEL_DESC_COND_PAGTO;
		}
	}
	
	@Override
	protected void onPopup() {
	   try {
		super.onPopup();
		loadDescProgressivoMixItens();
		loadDescProgressivoItemFinalPedido();
		loadIndiceFinanceiroCondComercial();
		loadIndiceFinanceiroCondPagto();
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private void loadDescProgressivoMixItens() throws SQLException {
		if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
			if (pedido.isPedidoAberto()) {
				int qtItensDiferentesInseridosNoPedido = ItemPedidoService.getInstance().getQtItensDiferentesInseridosNoPedido(pedido.itemPedidoList);
				double vlPctDescProgMix = DescProgQtdService.getInstance().getVlPctDescProgressivoMix(pedido.cdEmpresa, pedido.cdRepresentante, qtItensDiferentesInseridosNoPedido);
				lvDescProgressivoMixItens.setValue(vlPctDescProgMix);
			} else {
				lvDescProgressivoMixItens.setValue(pedido.vlPctDescProgressivoMix);
			}
		}
	}
	
	private void loadDescProgressivoItemFinalPedido() throws SQLException {
		if (LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido()) {
			if (pedido.isPedidoAberto()) {
				PedidoService.getInstance().isAplicaDescontoProgressivoNoPedido(pedido);
			} 
			lvDescProgressivoItemFinalPedido.setValue(pedido.vlPctDescProgressivo);
		}
	}
	
	private void loadIndiceFinanceiroCondComercial() throws SQLException {
		if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			double vlPctIndice = 0;
			if (!ValueUtil.valueEquals(pedido.getCondicaoComercial().vlIndiceFinanceiro, 0)) {
				vlPctIndice = (1 - pedido.getCondicaoComercial().vlIndiceFinanceiro) * 100;
			}
			lvIndiceFinanceiroCondComercial.setValue(Math.abs(vlPctIndice));
		}
	}
	
	private void loadIndiceFinanceiroCondPagto() throws SQLException {
		if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			double vlPctIndice = 0;
			if (!ValueUtil.valueEquals(pedido.getCondicaoPagamento().vlIndiceFinanceiro, 0)) {
				vlPctIndice = (1 - pedido.getCondicaoPagamento().vlIndiceFinanceiro) * 100;
			}
			lvIndiceFinanceiroCondPagto.setValue(Math.abs(vlPctIndice)); 
		}
	}

}
