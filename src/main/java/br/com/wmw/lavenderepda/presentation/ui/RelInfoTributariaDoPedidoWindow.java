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
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.util.Vector;

public class RelInfoTributariaDoPedidoWindow extends WmwWindow {
	
	private static final String VALOR_NAO_APLICAVEL = "--";
	
	private LabelName lbDescricaoIcms;
	private LabelValue lbValorIcms;
	private LabelName lbDescricaoST;
	private LabelValue lbValorST;
	private LabelName lbDescricaoIPI;
	private LabelValue lbValorIPI;
	private LabelName lbDescricaoFecop;
	private LabelValue lbValorFecop;
	private LabelName lbDescricaoPis;
	private LabelValue lbValorPis;
	private LabelName lbDescricaoCofins;
	private LabelValue lbValorCofins;
	private LabelName lbDescricaoFrete;
	private LabelValue lbValorFrete;
	private LabelName lbDescricaoDespesaAcessoria;
	private LabelValue lbValorDespesaAcessoria;

	public RelInfoTributariaDoPedidoWindow(Pedido pedido) throws SQLException {
		super(Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA_PEDIDO);
		lbDescricaoIcms = new LabelName(Messages.REL_LABEL_VALOR_ICMS);
		lbValorIcms = new LabelValue("999999999");
		lbValorIcms.setID("lbValorIcms");
		lbValorIcms.useCurrencyValue = true;
		lbDescricaoST = new LabelName(Messages.REL_LABEL_VALOR_ST);
		lbValorST = new LabelValue("999999999");
		lbValorST.setID("lbValorST");
		lbValorST.useCurrencyValue = true;
		lbDescricaoIPI = new LabelName(Messages.REL_LABEL_VALOR_IPI);
		lbValorIPI = new LabelValue("999999999");
		lbValorIPI.setID("lbValorIPI");
		lbValorIPI.useCurrencyValue = true;
		lbDescricaoFecop = new LabelName(Messages.REL_LABEL_VALOR_FECOP);
		lbValorFecop = new LabelValue("999999999");
		lbValorFecop.setID("lbValorFecop");
		lbValorFecop.useCurrencyValue = true;
		lbDescricaoPis = new LabelName(Messages.REL_LABEL_VALOR_PIS);
		lbValorPis = new LabelValue("999999999");
		lbValorPis.setID("lbValorPis");
		lbValorPis.useCurrencyValue = true;
		lbDescricaoCofins = new LabelName(Messages.REL_LABEL_VALOR_COFINS);
		lbValorCofins = new LabelValue("999999999");
		lbValorCofins.setID("lbValorCofins");
		lbValorCofins.useCurrencyValue = true;
		lbDescricaoFrete = new LabelName(Messages.REL_LABEL_VALOR_FRETE);
		lbValorFrete = new LabelValue("999999999");
		lbValorFrete.setID("lbValorFrete");
		lbValorFrete.useCurrencyValue = true;
		lbDescricaoDespesaAcessoria = new LabelName(Messages.REL_LABEL_VALOR_DESPESA_ACESSORIA);
		lbValorDespesaAcessoria = new LabelValue("999999999");
		lbValorDespesaAcessoria.setID("lbValorDespesaAcessoria");
		lbValorDespesaAcessoria.useCurrencyValue = true;
		domainToScreen(pedido);
		setDefaultRect();
	}
	
	public void initUI() {
		super.initUI();
		// IMPOSTOS
		int yPosition = TOP;
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.isUsaCalculoIpiItemPedido() || LavenderePdaConfig.calculaFecopItemPedido) {
			SessionContainer sessionContainerImposto = new SessionContainer();
			sessionContainerImposto.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
			UiUtil.add(this, sessionContainerImposto, LEFT, yPosition, FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
			LabelSubtitle lbImpostos = new LabelSubtitle(Messages.REL_LABEL_IMPOSTO);
			lbImpostos.setForeColor(ColorUtil.componentsForeColor);
			UiUtil.add(sessionContainerImposto, lbImpostos, CENTER, CENTER, PREFERRED, PREFERRED);
			addLabelsValoresImpostos();
			yPosition = AFTER;
		}
		//DEDUÇÕES
		if (LavenderePdaConfig.calculaImpostosAdicionaisItemPedido || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			addLabelsValoresDeducoes();
			yPosition = AFTER;
		}
		//INFORMAÇÕES ADICIONAIS
		SessionContainer sessionContainerInformacaoAdicional = new SessionContainer();
		sessionContainerInformacaoAdicional.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
		UiUtil.add(this, sessionContainerInformacaoAdicional, LEFT, yPosition, FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
		LabelSubtitle lbInfoAdicionais = new LabelSubtitle(Messages.REL_LABEL_INFORMACAO_ADICIONAL);
		lbInfoAdicionais.setForeColor(ColorUtil.componentsForeColor);
		UiUtil.add(sessionContainerInformacaoAdicional, lbInfoAdicionais, CENTER, CENTER, PREFERRED, PREFERRED);
		addLabelsValoresInformacaoAdicional();
	}

	private void addLabelsValoresImpostos() {
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			UiUtil.add(this, lbDescricaoIcms, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbValorIcms, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
			UiUtil.add(this, lbDescricaoST, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbValorST, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			UiUtil.add(this, lbDescricaoIPI, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbValorIPI, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			UiUtil.add(this, lbDescricaoFecop, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, lbValorFecop, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		}
	}
	
	private void addLabelsValoresDeducoes() {
		UiUtil.add(this, lbDescricaoPis, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorPis, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, lbDescricaoCofins, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorCofins, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
	}
	
	private void addLabelsValoresInformacaoAdicional() {
		UiUtil.add(this, lbDescricaoFrete, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorFrete, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, lbDescricaoDespesaAcessoria, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorDespesaAcessoria, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
	}
	
	private void domainToScreen(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			lbValorIcms.setValue(getTotalIcmsDoPedido(pedido.itemPedidoList)); 
			lbValorST.setValue(getTotalSTDoPedido(pedido.itemPedidoList));
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			lbValorIPI.setValue(getTotalIpiDoPedido(pedido.itemPedidoList));
		}
		if (LavenderePdaConfig.calculaFecopItemPedido) {
			lbValorFecop.setValue(getTotalFecopDoPedido(pedido.itemPedidoList));
		}
		lbValorPis.setValue(getTotalPisDoPedido(pedido.itemPedidoList));
		lbValorCofins.setValue(getTotalCofinsDoPedido(pedido.itemPedidoList));
		//--
		lbValorFrete.setValue(getTotalFrete(pedido.itemPedidoList, pedido.isTipoFreteFob()));
		lbValorDespesaAcessoria.setValue(getTotalDespesaAcessoria(pedido.itemPedidoList));
	}

	private String getTotalIcmsDoPedido(Vector itemPedidoList) {
		double vlTotalIcms = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalIcms += LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalIcmsItem : itemPedido.vlIcms * itemPedido.getQtItemFisico();
		}
		return vlTotalIcms > 0 ? StringUtil.getStringValueToInterface(vlTotalIcms) : VALOR_NAO_APLICAVEL;
	}
	
	private String getTotalSTDoPedido(Vector itemPedidoList) {
		double vlTotalST = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalST += LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalStItem : itemPedido.vlSt * itemPedido.getQtItemFisico();
		}
		return vlTotalST > 0 ? StringUtil.getStringValueToInterface(vlTotalST) : VALOR_NAO_APLICAVEL;
	}
	
	private String getTotalIpiDoPedido(Vector itemPedidoList) {
		double vlTotalIPI = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalIPI += LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalIpiItem : itemPedido.vlIpiItem * itemPedido.getQtItemFisico();
		}
		return vlTotalIPI > 0 ? StringUtil.getStringValueToInterface(vlTotalIPI) : VALOR_NAO_APLICAVEL;
	}
	
	private String getTotalFecopDoPedido(Vector itemPedidoList) {
		double vlTotalFecop = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalFecop += LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalFecopItem : itemPedido.vlFecop * itemPedido.getQtItemFisico();
		}
		return vlTotalFecop > 0 ? StringUtil.getStringValueToInterface(vlTotalFecop) : VALOR_NAO_APLICAVEL;
	}
	
	private String getTotalPisDoPedido(Vector itemPedidoList) {
		double vlTotalPis = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalPis +=  LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalPisItem : itemPedido.vlPis * itemPedido.getQtItemFisico();
		}
		return vlTotalPis > 0 ? StringUtil.getStringValueToInterface(vlTotalPis) : VALOR_NAO_APLICAVEL;
	}
	
	private String getTotalCofinsDoPedido(Vector itemPedidoList) {
		double vlTotalCofins = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalCofins += LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalCofinsItem : (itemPedido.vlCofins * itemPedido.getQtItemFisico());
		}
		return vlTotalCofins > 0 ? StringUtil.getStringValueToInterface(vlTotalCofins) : VALOR_NAO_APLICAVEL;
	}
		
	private String getTotalFrete(Vector itemPedidoList, boolean isTipoFreteFob) {
		double vlTotalFrete = 0;
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			vlTotalFrete += itemPedido.getVlTotalFrete();
		}
		return vlTotalFrete > 0 ? StringUtil.getStringValueToInterface(vlTotalFrete) : VALOR_NAO_APLICAVEL;
	}
		
	private String getTotalDespesaAcessoria(Vector itemPedidoList) {
		double vlTotalDespesaAcessoria = PedidoService.getInstance().getVlTotalDespesaAcessoria(itemPedidoList);
		return vlTotalDespesaAcessoria != 0d ? StringUtil.getStringValueToInterface(vlTotalDespesaAcessoria) : VALOR_NAO_APLICAVEL;
	}
	

}
