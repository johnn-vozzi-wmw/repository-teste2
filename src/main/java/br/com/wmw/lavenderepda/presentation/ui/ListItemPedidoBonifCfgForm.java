package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoBonifCfgService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItemPedidoBonifCfgForm extends LavendereCrudListForm {

	private final Pedido pedido;
	private BarContainer barBottomListContainerCustom;
	private LabelName lbTotalizerLeftCustom;
	private LabelName lbTotalizerRightCustom;
	double vlSumLeftTotalizer;
	double vlSumRightTotalizer;
	private BonifCfg bonifCfg;

	public ListItemPedidoBonifCfgForm(Pedido pedido, BonifCfg bonifCfg) {
		super(Messages.ITEMPEDIDOBONIFCFG_NOME_LISTA);
		this.pedido = pedido;
		this.bonifCfg = bonifCfg;
		constructorListContainer();
		if (bonifCfg.isTipoRegraQuantidade()) {
			listContainer.setColTotalizerRight(0, Messages.LABEL_SALDO);
		} else {
			listContainer.setColTotalizerRight(0, Messages.LABEL_SALDO + Messages.MOEDA);
		}
		configureCustomBottomComponents();
	}
	
	private void constructorListContainer() {
		GridListContainer gridListContainer = new GridListContainer(4, 2, false, false, false, true);
		gridListContainer.setColPosition(3, RIGHT);
		gridListContainer.setUseSortMenu(false);
		gridListContainer.setBarTopSimple();
		listContainer = gridListContainer;
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new ItemPedidoBonifCfg();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemPedidoBonifCfgService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		int barHeight = UiUtil.getControlPreferredHeight()/3*2;
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL - barHeight);
		UiUtil.add(this, barBottomListContainerCustom, LEFT, AFTER, FILL,  barHeight );
		barBottomListContainerCustom.add(lbTotalizerLeftCustom, LEFT + 3, CENTER);
		barBottomListContainerCustom.add(lbTotalizerRightCustom, RIGHT - 3, CENTER);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		//Esta tela nao precisa de evento ate agora.
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		vlSumRightTotalizer = 0;
		vlSumLeftTotalizer = 0;
		return ((ItemPedidoBonifCfgService)getCrudService()).findAllItensBonifCfgByPedido(pedido, this.bonifCfg.cdBonifCfg);
	}
	
	@Override
	protected String[] getItem(Object domain) {
		ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) domain;
		calculeTotalizer(itemPedBon);
		String leftMessage = getLeftItemMessage(itemPedBon);
		String rightMessage = getRightItemMessage(itemPedBon);
		return new String[] {
				LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : StringUtil.getStringValue(itemPedBon.cdProduto) + " - ",
				itemPedBon.getProduto().dsProduto,
				leftMessage,
				rightMessage
			};
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) domain;
		double totalizador = 0;
		BonifCfg bonifCfg = itemPedBon.getBonifCfg();
		if (bonifCfg.isTipoRegraValor()) {
			totalizador = itemPedBon.vlBonificacao;
		} else if (isBonifCfgQtd(bonifCfg) && !itemPedBon.isFlTipoRegistroBrinde()) {
			totalizador = itemPedBon.qtBonificacao;
		}
		totalizador *= (itemPedBon.isFlTipoRegistroDebito() ? -1 : 1);
		c.itemRightTotalizer = StringUtil.getStringValue(totalizador);
		if (itemPedBon.isFlTipoRegistroDebito()) {
			c.setBackColor(LavendereColorUtil.COR_PRODUTO_BONIFICACAO_BACK);
		} else if (itemPedBon.isFlTipoRegistroBrinde()) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE);
		}
		c.setToolTip(itemPedBon.getProduto().dsProduto);
	}
	
	private void configureCustomBottomComponents() {
		this.barBottomListContainerCustom = new BarContainer();
		this.barBottomListContainerCustom.setBackColor(ColorUtil.componentsBackColor);
		this.lbTotalizerLeftCustom = new LabelName(" ");
		this.lbTotalizerLeftCustom.setID("lbTotalizerLeftCustom");
		this.lbTotalizerRightCustom = new LabelName(" ");
		this.lbTotalizerRightCustom.setID("lbTotalizerRightCustom");
		this.lbTotalizerLeftCustom.setFont(UiUtil.fontVerySmall);
		this.lbTotalizerRightCustom.setFont(UiUtil.fontVerySmall);
		this.lbTotalizerLeftCustom.setForeColor(ColorUtil.componentsForeColor);
		this.lbTotalizerRightCustom.setForeColor(ColorUtil.componentsForeColor);
	}
	
	private void calculeTotalizer(ItemPedidoBonifCfg itemPedBon) {
		double valueToSum = isBonifCfgQtd(itemPedBon.getBonifCfg()) ? itemPedBon.isFlTipoRegistroBrinde() ? 0 : itemPedBon.qtBonificacao : itemPedBon.vlBonificacao;
		vlSumLeftTotalizer += itemPedBon.isFlTipoRegistroCredito() ? valueToSum : 0;
		vlSumRightTotalizer += itemPedBon.isFlTipoRegistroDebito() ? valueToSum : 0;
		configureBarBottomTotalizer(vlSumLeftTotalizer, vlSumRightTotalizer);
	}
	
	private void configureBarBottomTotalizer(double vlSumLeftTotalizer, double vlSumRightTotalizer) {
		lbTotalizerLeftCustom.setValue(Messages.LABEL_TOTAL_CREDITOS + (isBonifCfgQtd(bonifCfg) ? "" : Messages.MOEDA) + StringUtil.getStringValueToInterface(vlSumLeftTotalizer));
		lbTotalizerRightCustom.setValue(Messages.LABEL_TOTAL_DEBITOS + (isBonifCfgQtd(bonifCfg) ? "" : Messages.MOEDA) + StringUtil.getStringValueToInterface(vlSumRightTotalizer));
		lbTotalizerLeftCustom.reposition();
		lbTotalizerRightCustom.reposition();
	}

	protected boolean isBonifCfgQtd(BonifCfg bonifCfg) {
		return bonifCfg.isTipoRegraQuantidade() || bonifCfg.isTipoRegraContaCorrente();
	}
	
	private String getRightItemMessage(ItemPedidoBonifCfg itemPedBon) {
		String message = ValueUtil.VALOR_NI;
		BonifCfg bonifCfg = itemPedBon.getBonifCfg();
		if (bonifCfg.isTipoRegraValor()) {
			message = itemPedBon.isFlTipoRegistroCredito()
					? StringUtil.getStringValueToInterface(itemPedBon.getBonifCfg().vlPctSobreVenda) + Messages.LABEL_PERC_SOBRE_VENDA : ValueUtil.VALOR_NI;
		} else if (itemPedBon.isFlTipoRegistroBrinde()) {
			message = Messages.BONIFCFG_QTD_ITEM +  StringUtil.getStringValueToInterface(itemPedBon.qtBonificacao);
		} 
		return message;
	}
	
	private String getLeftItemMessage(ItemPedidoBonifCfg itemPedBon) {
		String message = ValueUtil.VALOR_NI;
		message = (itemPedBon.isFlTipoRegistroCredito() ? Messages.LABEL_CREDITO : Messages.LABEL_DEBITO);
		
		if (itemPedBon.getBonifCfg().isTipoRegraQuantidade() || itemPedBon.getBonifCfg().isTipoRegraContaCorrente()) {
			message += StringUtil.getStringValueToInterface(itemPedBon.isFlTipoRegistroBrinde() ? 0 : itemPedBon.qtBonificacao);
		} else {
			message += Messages.MOEDA + StringUtil.getStringValueToInterface(itemPedBon.vlBonificacao);
		}
		return message;
	}

}
