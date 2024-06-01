package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelSubtitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ClassificFiscalService;
import totalcross.sys.Convert;

public class RelSubstituicaoTributariaWindow extends WmwWindow {

	private LabelName lbVlSubstituicaoTributaria;
	private LabelName lbVlNCM;
	private LabelName lbVlItemSemST;
	private LabelName lbVlTotalItemSemST;
	private LabelName lbVlTotalPedidoSemST;
	private LabelName lbVlItemComST;
	private LabelName lbVlTotalItemComST;
	private LabelName lbVlTotalPedidoComST;
	//--
	private LabelValue lbQtSubstituicaoTributaria;
	private LabelValue lbDsNCM;
	private LabelValue lbQtItemSemST;
	private LabelValue lbQtTotalItemSemST;
	private LabelValue lbQtTotalPedidoSemST;
	private LabelValue lbQtItemComST;
	private LabelValue lbQtTotalItemComST;
	private LabelValue lbQtTotalPedidoComST;

	private boolean editingItemPedido;
	private boolean filtraCamposItemPedido;
	private ItemPedido itemPedido;

	public RelSubstituicaoTributariaWindow(Pedido pedido) throws SQLException {
		this(pedido, null, false);
	}

	public RelSubstituicaoTributariaWindow(Pedido pedido, ItemPedido itemPedido, boolean editingItem) throws SQLException {
		super(Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA);
		this.itemPedido = itemPedido;
		editingItemPedido = editingItem;
		filtraCamposItemPedido = itemPedido == null;
		if (!filtraCamposItemPedido) {
			lbVlNCM = new LabelName(Messages.REL_LABEL_NCM, RIGHT);
			lbVlItemSemST = new LabelName(Messages.REL_LABEL_VALOR_ITEM_SEM_ST);
			lbVlTotalItemSemST = new LabelName(Messages.REL_LABEL_VALOR_TOTAL_ITEM_SEM_ST);
			lbVlItemComST = new LabelName(Messages.REL_LABEL_VALOR_ITEM_COM_ST);
			lbVlTotalItemComST = new LabelName(Messages.REL_LABEL_VALOR_TOTAL_ITEM_COM_ST);
			lbQtItemSemST = new LabelValue("999999999");
			lbQtTotalItemSemST = new LabelValue("999999999");
			lbQtItemComST = new LabelValue("999999999");
			lbQtTotalItemComST = new LabelValue("999999999");
		}
		lbVlSubstituicaoTributaria = new LabelName(Messages.REL_LABEL_VALOR_ST);
		lbVlTotalPedidoSemST = new LabelName(Messages.REL_LABEL_VALOR_TOTAL_PEDIDO_SEM_ST);
		lbVlTotalPedidoComST = new LabelName(Messages.REL_LABEL_VALOR_TOTAL_PEDIDO_COM_ST);
		lbQtSubstituicaoTributaria = new LabelValue("999999999");
		lbDsNCM = new LabelValue("999999999");
		lbQtTotalPedidoSemST = new LabelValue("999999999");
		lbQtTotalPedidoComST = new LabelValue("999999999");
		domainToScreen(itemPedido, pedido);
		setRect(10, 10);
	}

	public void domainToScreen(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double vlTotalPedidoSemST = pedido.getVlEfetivo();
		double vlTotalPedidoComSt = pedido.getVlEfetivoComSt();
		if (!filtraCamposItemPedido) {
			double vlST = itemPedido.vlSt;
			double vlItemSemST = itemPedido.vlItemPedido;
			double vlTotalItemSemST = itemPedido.vlTotalItemPedido;
			double vlItemComST = itemPedido.vlItemPedido + itemPedido.vlSt;
			double vlTotalItemComSt = itemPedido.getVlTotalComST();
			lbQtSubstituicaoTributaria.setValue(vlST);
			lbQtItemSemST.setValue(vlItemSemST);
			lbQtTotalItemSemST.setValue(vlTotalItemSemST);
			lbQtItemComST.setValue(vlItemComST);
			lbQtTotalItemComST.setValue(vlTotalItemComSt);
			if (!editingItemPedido) {
				vlTotalPedidoSemST += itemPedido.getVlEfetivoTotal();
				vlTotalPedidoComSt += itemPedido.getVlEfetivoTotalComSt();
			} else {
				ItemPedido itemPedidoOrg = (ItemPedido)pedido.itemPedidoList.items[pedido.itemPedidoList.indexOf(itemPedido)];
				vlTotalPedidoSemST += itemPedido.getVlEfetivoTotal() - itemPedidoOrg.getVlEfetivoTotal();
				vlTotalPedidoComSt += itemPedido.getVlEfetivoTotalComSt() - itemPedidoOrg.getVlEfetivoTotalComSt();
			}
		}
		lbQtTotalPedidoSemST.setValue(vlTotalPedidoSemST);
		lbQtTotalPedidoComST.setValue(vlTotalPedidoComSt);
	}

	public void initUI() {
	   try {
		super.initUI();
		int yComponents = TOP;
		//DADOS DO PRODUTO
		if (!filtraCamposItemPedido) {
			SessionContainer containerLabelProduto = new SessionContainer();
			UiUtil.add(this, containerLabelProduto, LEFT, TOP, FILL, PREFERRED);
			UiUtil.add(containerLabelProduto, new LabelSubtitle(Messages.REL_LABEL_DADOS_PRODUTO), LEFT + 20, CENTER, PREFERRED, PREFERRED);
			LabelName lbProduto = new LabelName(Messages.ITEMPEDIDO_LABEL_PRODUTO);
			UiUtil.add(this, lbProduto, LEFT + HEIGHT_GAP, AFTER + HEIGHT_GAP, PREFERRED);
			UiUtil.add(this, new LabelValue(Convert.insertLineBreak(width - (lbProduto.getX2() + WIDTH_GAP_BIG), lbProduto.fm, itemPedido.getProduto().toString())), AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
			UiUtil.add(this, lbVlNCM,  LEFT+HEIGHT_GAP, AFTER, lbProduto.getWidth(),PREFERRED);
			if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdClassificFiscal)) {
				lbDsNCM.setValue(ClassificFiscalService.getInstance().getDsClassificacaoFiscal(itemPedido.getProduto().cdClassificFiscal));
				UiUtil.add(this, lbVlNCM, lbDsNCM, getLeft(), AFTER + HEIGHT_GAP);
			}
			yComponents = AFTER + HEIGHT_GAP_BIG;
		}
		//RESUMO ST
		LabelName lbResumoSt = new LabelName(Messages.REL_LABEL_RESUMO_ST);
		lbResumoSt.setForeColor(ColorUtil.labelNameForeColor);
		UiUtil.add(this, lbResumoSt, CENTER, yComponents, PREFERRED, PREFERRED);
		addLabelsValoresSt();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void addLabelsValoresSt() {
		int yComponents = AFTER + HEIGHT_GAP;
		if (!filtraCamposItemPedido) {
			UiUtil.add(this, lbVlSubstituicaoTributaria,  BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
			UiUtil.add(this, lbQtSubstituicaoTributaria, AFTER + WIDTH_GAP_BIG, SAME);
			UiUtil.add(this, lbVlItemSemST, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbQtItemSemST, AFTER + WIDTH_GAP_BIG, SAME);
			UiUtil.add(this, lbVlTotalItemSemST, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbQtTotalItemSemST, AFTER + WIDTH_GAP_BIG, SAME);
			UiUtil.add(this, lbVlItemComST, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbQtItemComST, AFTER + WIDTH_GAP_BIG, SAME);
			UiUtil.add(this, lbVlTotalItemComST, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbQtTotalItemComST, AFTER + WIDTH_GAP_BIG, SAME);
			yComponents = AFTER;
		}
		UiUtil.add(this, lbVlTotalPedidoSemST, BaseUIForm.CENTEREDLABEL, yComponents);
		UiUtil.add(this, lbQtTotalPedidoSemST, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbVlTotalPedidoComST, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbQtTotalPedidoComST, AFTER + WIDTH_GAP_BIG, SAME);
	}

}