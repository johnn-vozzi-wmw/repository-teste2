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
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;

public class RelInfoTributariaDoItemPedidoWindow extends WmwWindow {

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

	private LabelName lbDescricaoVlNcm;
	private LabelValue lbVlNcm;
	private LabelName lbDescricaoVlPctIcms;
	private LabelValue lbVlPctIcms;
	private LabelName lbDescricaoVlPctSt;
	private LabelValue lbVlPctSt;
	private LabelName lbDescricaoVlPctIpi;
	private LabelValue lbVlPctIpi;
	private LabelName lbDescricaoVlPctPis;
	private LabelValue lbVlPctPis;
	private LabelName lbDescricaoVlPctCofins;
	private LabelValue lbVlPctCofins;
	private LabelName lbDescricaoVlCst;
	private LabelValue lbVlCst;
	private LabelName lbDescricaoVlPctMva;
	private LabelValue lbVlPctMva;
	private LabelName lbDescricaoVlCfop;
	private LabelValue lbVlCfop;
	private LabelName lbDescricaoVlPisCofins;
	private LabelValue lbVlPisCofins;

	private ItemTabelaPreco itemTabPreco;

	public RelInfoTributariaDoItemPedidoWindow(ItemPedido itemPedido) throws SQLException {
		super(Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA_ITEMPEDIDO);
		lbDescricaoVlNcm = new LabelName(Messages.REL_LABEL_VL_NCM);
		lbVlNcm = new LabelValue("999999999").setID("lbVlNcm");
		lbDescricaoVlPctIcms = new LabelName(Messages.REL_LABEL_PCT_ICMS);
		lbVlPctIcms = new LabelValue("999999999").setID("lbVlPctIcms");
		lbDescricaoVlPctSt = new LabelName(Messages.REL_LABEL_PCT_ST);
		lbVlPctSt = new LabelValue("999999999").setID("lbVlPctSt");
		lbDescricaoVlPctIpi = new LabelName(Messages.REL_LABEL_PCT_IPI);
		lbVlPctIpi = new LabelValue("999999999").setID("lbVlPctIpi");
		lbDescricaoVlPctPis = new LabelName(Messages.REL_LABEL_PCT_PIS);
		lbVlPctPis = new LabelValue("999999999").setID("lbVlPctPis");
		lbDescricaoVlPctCofins = new LabelName(Messages.REL_LABEL_PCT_COFINS);
		lbVlPctCofins = new LabelValue("999999999").setID("lbVlPctCofins");
		lbDescricaoVlCst = new LabelName(Messages.REL_LABEL_VL_CST);
		lbVlCst = new LabelValue("999999999").setID("lbVlCst");
		lbDescricaoVlPctMva = new LabelName(Messages.REL_LABEL_PCT_MVA);
		lbVlPctMva = new LabelValue("999999999").setID("lbVlPctMva");
		lbDescricaoVlCfop = new LabelName(Messages.REL_LABEL_VL_CFOP);
		lbVlCfop = new LabelValue("999999999").setID("lbVlCfop");
		lbDescricaoVlPisCofins = new LabelName(Messages.REL_LABEL_PCT_PISCOFINS);
		lbVlPisCofins = new LabelValue("999999999");
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
		lbDescricaoFrete = new LabelName(Messages.REL_LABEL_VALOR_FRETE, RIGHT);
		lbValorFrete = new LabelValue("999999999");
		lbValorFrete.useCurrencyValue = true;
		lbDescricaoDespesaAcessoria = new LabelName(Messages.REL_LABEL_VALOR_DESPESA_ACESSORIA);
		lbValorDespesaAcessoria = new LabelValue("999999999");
		lbValorDespesaAcessoria.useCurrencyValue = true;

		domainToScreen(itemPedido);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		// IMPOSTOS
		int yPosition = TOP;
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.isUsaCalculoIpiItemPedido() || (LavenderePdaConfig.calculaFecopItemPedido) || LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco) {
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
		if ((LavenderePdaConfig.calculaImpostosAdicionaisItemPedido || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) && !LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco) {
			addLabelsValoresDeducoes();
			yPosition = AFTER;
		}
		//INFORMAÇÕES ADICIONAIS
		if(!LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco){
			SessionContainer sessionContainerInformacaoAdicional = new SessionContainer();
			sessionContainerInformacaoAdicional.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
			UiUtil.add(this, sessionContainerInformacaoAdicional, LEFT, yPosition, FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
			LabelSubtitle lbInfoAdicionais = new LabelSubtitle(Messages.REL_LABEL_INFORMACAO_ADICIONAL);
			lbInfoAdicionais.setForeColor(ColorUtil.componentsForeColor);
			UiUtil.add(sessionContainerInformacaoAdicional, lbInfoAdicionais, CENTER, CENTER, PREFERRED, PREFERRED);
			addLabelsValoresInformacaoAdicional();
		}

	}

	private void addLabelsValoresImpostos() {
		if (LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco) {
			addLabelImposto(lbDescricaoVlNcm, lbVlNcm, ValueUtil.getDoubleSimpleValue(itemTabPreco.vlNcm));
			addLabelImposto(lbDescricaoVlPctIcms, lbVlPctIcms, itemTabPreco.vlPctIcms);
			addLabelImposto(lbDescricaoVlPctSt, lbVlPctSt, itemTabPreco.vlPctSt);
			addLabelImposto(lbDescricaoVlPctIpi, lbVlPctIpi, itemTabPreco.vlPctIpi);
			addLabelImposto(lbDescricaoVlPctPis, lbVlPctPis, itemTabPreco.vlPctPis);
			addLabelImposto(lbDescricaoVlPctCofins, lbVlPctCofins, itemTabPreco.vlPctCofins);
			addLabelImposto(lbDescricaoVlCst, lbVlCst, ValueUtil.getDoubleSimpleValue(itemTabPreco.vlCst));
			addLabelImposto(lbDescricaoVlPctMva, lbVlPctMva, itemTabPreco.vlPctMva);
			addLabelImposto(lbDescricaoVlCfop, lbVlCfop, ValueUtil.getDoubleSimpleValue(itemTabPreco.vlCfop));
			addLabelImposto(lbDescricaoVlPisCofins, lbVlPisCofins, itemTabPreco.vlPctPisCofins);
		} else {
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
	}

	private void addLabelImposto(LabelName labelName, LabelValue labelValue, double value) {
		if (value >= 0d) {
			UiUtil.add(this, labelName, BaseUIForm.CENTEREDLABEL, AFTER);
			UiUtil.add(this, labelValue, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		}
	}

	private void addLabelsValoresDeducoes() {
		UiUtil.add(this, lbDescricaoPis, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorPis, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, lbDescricaoCofins, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorCofins, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
	}

	private void addLabelsValoresInformacaoAdicional() {
		UiUtil.add(this, lbDescricaoFrete,  BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorFrete, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, lbDescricaoDespesaAcessoria, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(this, lbValorDespesaAcessoria, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
	}

	private void domainToScreen(ItemPedido itemPedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (LavenderePdaConfig.mostraTributacaoItemPorItemTabelaPreco && itemTabelaPreco != null) {
			itemTabPreco = itemTabelaPreco;
			lbVlNcm.setValue(ValueUtil.isNotEmpty(itemTabelaPreco.vlNcm) ? itemTabelaPreco.vlNcm : ValueUtil.VALOR_ZERO);
			lbVlPctIcms.setValue(getValueConcat(itemTabelaPreco.vlPctIcms, "%"));
			lbVlPctSt.setValue(getValueConcat(itemTabelaPreco.vlPctSt, "%"));
			lbVlPctIpi.setValue(getValueConcat(itemTabelaPreco.vlPctIpi, "%"));
			lbVlPctPis.setValue(getValueConcat(itemTabelaPreco.vlPctPis, "%"));
			lbVlPctCofins.setValue(getValueConcat(itemTabelaPreco.vlPctCofins, "%"));
			lbVlCst.setValue(ValueUtil.isNotEmpty(itemTabelaPreco.vlCst) ? itemTabelaPreco.vlCst : ValueUtil.VALOR_ZERO);
			lbVlPctMva.setValue(getValueConcat(itemTabelaPreco.vlPctMva, "%"));
			lbVlCfop.setValue(ValueUtil.isNotEmpty(itemTabelaPreco.vlCfop) ? itemTabelaPreco.vlCfop : ValueUtil.VALOR_ZERO);
			lbVlPisCofins.setValue(getValueConcat(itemTabelaPreco.vlPctPisCofins, "%"));
		} else {
			if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
				lbValorIcms.setValue(getVlPisCofinsIcms(itemPedido, LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalIcmsItem : itemPedido.vlIcms));
				lbValorST.setValue(getVlStIpi(LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalStItem : itemPedido.vlSt));
			}
			if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				lbValorIPI.setValue(getVlStIpi(LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalIpiItem : itemPedido.vlIpiItem));
			}
			if (LavenderePdaConfig.calculaFecopItemPedido) {
				lbValorFecop.setValue(itemPedido.getVlFecop() > 0 ? StringUtil.getStringValueToInterface(itemPedido.getVlFecop()) : VALOR_NAO_APLICAVEL);
			}
			lbValorPis.setValue(getVlPisCofinsIcms(itemPedido, LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalPisItem : itemPedido.vlPis));
			lbValorCofins.setValue(getVlPisCofinsIcms(itemPedido, LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemPedido.vlTotalCofinsItem : itemPedido.vlCofins));
			lbValorFrete.setValue(getVlFrete(itemPedido));
			lbValorDespesaAcessoria.setValue(itemPedido.vlDespesaAcessoria > 0 ? StringUtil.getStringValueToInterface(itemPedido.vlDespesaAcessoria) : VALOR_NAO_APLICAVEL);
		}
	}

	private String getValueConcat(double valor , String concat){
		return StringUtil.getStringValueToInterface(ValueUtil.round(valor, LavenderePdaConfig.nuCasasDecimais)) + concat;
	}

	private String getVlFrete(ItemPedido itemPedido) {
		double vlFrete = 0;
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			vlFrete = itemPedido.vlTotalItemPedidoFrete;
		} else {
			vlFrete = itemPedido.getVlTotalFrete();
		}
		return vlFrete > 0 ? StringUtil.getStringValueToInterface(ValueUtil.round(vlFrete, LavenderePdaConfig.nuCasasDecimais)) : VALOR_NAO_APLICAVEL;
	}

	private String getVlPisCofinsIcms(ItemPedido itemPedido, double valor) throws SQLException {
		if (valor > 0) {
			valor = ValueUtil.round(valor, LavenderePdaConfig.nuCasasDecimais);
			TributacaoConfig tributacaoConfigItem = itemPedido.getTributacaoConfigItem();
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && tributacaoConfigItem != null && tributacaoConfigItem.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
				return StringUtil.getStringValueToInterface(valor * -1);
			} else {
				return StringUtil.getStringValueToInterface(valor);
			}
		}
		return VALOR_NAO_APLICAVEL;
	}

	private String getVlStIpi(double valor) {
		return valor > 0 ? StringUtil.getStringValueToInterface(ValueUtil.round(valor, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : VALOR_NAO_APLICAVEL;
	}

}
