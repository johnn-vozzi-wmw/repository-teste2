package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.util.BigDecimal;

public class RelComissaoPedidoWindow extends WmwWindow {

	public BaseGridEdit grid;
	private Pedido pedido;

	private LabelValue lvVlComissaoPedido;
	private LabelValue lvVlTotalPedido;

	public RelComissaoPedidoWindow(Pedido pedido) {
		super(Messages.RELCOMISSAOPEDIDO_NOME_ENTIDADE);
		this.pedido = pedido;
		scrollable = false;
		lvVlComissaoPedido = new LabelValue("9999999999999999");
		lvVlComissaoPedido.useCurrencyValue = true;
		lvVlTotalPedido = new LabelValue("9999999999999999");
		lvVlTotalPedido.useCurrencyValue = true;
		setDefaultRect();
	}

	private void domainToScreen() {
		String comissao = "";
		try {
			//Conversões para corrigir bug de ponto flutuante aonde alguns casos não arredonda o double corretamente somente com ValueUtil.Round
			comissao = StringUtil.getStringValueToInterface(BigDecimal.valueOf(ValueUtil.round(pedido.vlComissaoPedido)).setScale(LavenderePdaConfig.nuCasasDecimais, BigDecimal.ROUND_HALF_UP));
		} catch (Throwable e) {
			comissao = StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlComissaoPedido, LavenderePdaConfig.nuCasasDecimais));
		}
		String vlPctComissao = StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlPctComissaoPedido, LavenderePdaConfig.nuCasasDecimais));
		lvVlTotalPedido.setValue(ValueUtil.round(pedido.vlTotalPedido, LavenderePdaConfig.nuCasasDecimais));
		lvVlComissaoPedido.setValue(comissao + " ("+vlPctComissao+"%)");
	}

	public void initUI() {
	   try {
		super.initUI();

		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_VLTOTALPEDIDO), BaseUIForm.CENTEREDLABEL, BOTTOM - footerH);
		UiUtil.add(this, lvVlTotalPedido, AFTER + WIDTH_GAP, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_VLCOMISSAOPEDIDO), BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
		UiUtil.add(this, lvVlComissaoPedido, AFTER + WIDTH_GAP, SAME, FILL, PREFERRED);
        int oneChar = fm.charWidth('A');
		GridColDefinition[] gridColDefiniton;
		if (LavenderePdaConfig.mostraDescontoAcrescimoRelComissao()) {
			gridColDefiniton = new GridColDefinition[] {
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, oneChar * 25, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, oneChar * 5, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_ACRESCIMO_DESCONTO, oneChar * 2, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PCTCOMISSAO, oneChar * 2, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, (oneChar * 5) + WIDTH_GAP, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLCOMISSAOITEM, oneChar * (5 + LavenderePdaConfig.nuCasasDecimais), LEFT)};
		} else {
			gridColDefiniton = new GridColDefinition[] {
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, oneChar * 25, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, oneChar * 5, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PCTCOMISSAO, oneChar * 2, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, (oneChar * 5) + WIDTH_GAP, LEFT),
			   new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLCOMISSAOITEM, oneChar * (5 + LavenderePdaConfig.nuCasasDecimais), LEFT)};
		}
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, TOP, FILL , FILL - footerH - (lvVlComissaoPedido.getHeight() + lvVlTotalPedido.getHeight() + HEIGHT_GAP_BIG));
		carregaGrid();
		domainToScreen();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void carregaGrid() throws SQLException {
		grid.clear();
		if (pedido.itemPedidoList != null) {
			int size = pedido.itemPedidoList.size();
			ItemPedido itemPedido;
			for (int x = 0; x < size; x++) {
				itemPedido = (ItemPedido)pedido.itemPedidoList.items[x];
				String qtItemPrincipal = StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico(), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
				if (itemPedido != null) {
					String[] item;
					if (LavenderePdaConfig.mostraDescontoAcrescimoRelComissao()) {
						item = new String[]{
								StringUtil.getStringValue(itemPedido.getDsProdutoWithKey(itemPedido)), StringUtil.getStringValueToInterface(itemPedido.vlItemPedido),
								StringUtil.getStringValueToInterface(itemPedido.getVlPctDescontoAcrescimo()), StringUtil.getStringValueToInterface(itemPedido.vlPctComissao,
								LavenderePdaConfig.nuCasasDecimaisComissao), qtItemPrincipal, StringUtil.getStringValueToInterface(itemPedido.vlTotalComissao)};
					} else {
						item = new String[]{
								StringUtil.getStringValue(itemPedido.getDsProdutoWithKey(itemPedido)), StringUtil.getStringValueToInterface(itemPedido.vlItemPedido),
								StringUtil.getStringValueToInterface(itemPedido.vlPctComissao, LavenderePdaConfig.nuCasasDecimaisComissao), qtItemPrincipal,
								StringUtil.getStringValueToInterface(itemPedido.vlTotalComissao)};
					}
					grid.add(item);
				}
			}
		}
		grid.qsort(2, false);
	}

	public void popup() {
		if ((pedido.itemPedidoList != null) && (pedido.itemPedidoList.size() > 0)) {
			super.popup();
		} else {
			UiUtil.showInfoMessage(Messages.PEDIDO_MSG_SEM_ITEM_NO_PEDIDO);
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

}
