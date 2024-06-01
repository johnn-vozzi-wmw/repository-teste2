package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VerbaFornecedor;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;

public class RelVerbaPersonalizadaWindow extends WmwWindow {
	
	private ItemPedido currentItemPedido;
	
	private LabelValue lbVlGrupoProdDisp;
	private LabelValue lvVlConsumoItemGrupo;
	private LabelValue lbVlGrupoLiquido;
	private BaseScrollContainer containerGeral;
	private BaseScrollContainer containerGrupo;
	private ScrollTabbedContainer tabs;
	List<String> tabsString;
	
	HashMap<String, VerbaFornecedor> hashFornecedor;
	HashMap<String, VerbaGrupoSaldo> hashGrupo;

	public RelVerbaPersonalizadaWindow(ItemPedido itemPedido) throws SQLException {
		super(Messages.REL_VERBA_PERSONALIZADA_TITULO);
		this.currentItemPedido = itemPedido;
		tabsString = new ArrayList<String>();
		tabsString.add(Messages.REL_VERBA_SALDO_PERSONALIZADO_GERAL);
		lbVlGrupoProdDisp = new LabelValue(0);
		lbVlGrupoProdDisp.setID("lbVlGrupoProdDisp");
		lvVlConsumoItemGrupo = new LabelValue(0);
		lvVlConsumoItemGrupo.setID("lvVlConsumoItemGrupo");
		lbVlGrupoLiquido = new LabelValue(0);
		lbVlGrupoLiquido.setID("lbVlGrupoLiquido");
		tabsString.add(Messages.GRUPO_PLURAL);
		containerGeral = new BaseScrollContainer(false, true);
		containerGrupo = new BaseScrollContainer(false, true);
		tabs = new ScrollTabbedContainer(tabsString.toArray(new String[tabsString.size()]));
		containerGeral.setRect(getLeft(), getNextY(), getWFill(), FILL - BaseContainer.HEIGHT_GAP);
		containerGrupo.setRect(containerGeral.getRect());
		setValues(itemPedido);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, tabs, getTop() + HEIGHT_GAP, footerH);
		try {
			addComponentsContainerGeral();
			if (lbVlGrupoProdDisp != null) {
				addComponentsContainerGrupo();
			}
		} catch (SQLException e) {
			UiUtil.showErrorMessage(e);
		}
	}

	private void addComponentsContainerGrupo() throws SQLException {
		int y = getTop();
		LabelValue lvVerbaOficial;
		LabelValue lvVerbaGeradaConsumida;
		LabelValue lvVerbaLiquida;
		LabelValue lvTolerancia;
		LabelName lbTolerancia;
		VerbaGrupoSaldo filter = new VerbaGrupoSaldo();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		HashMap<String, VerbaGrupoSaldo> hashAll = new HashMap<String, VerbaGrupoSaldo>();
		hashAll.putAll(hashGrupo);
		VerbaGrupoSaldoService.getInstance().mesclaHashComVerbaGruposNaoExistentes(filter, hashAll);
		for (Map.Entry<String, VerbaGrupoSaldo> grupo : hashAll.entrySet()) {
			VerbaGrupoSaldo verba = grupo.getValue();
			if (verba.getGrupoProduto1() == null) continue;
			lbTolerancia = new LabelName(Messages.VERBAGRUPOSALDO_VLTOLERANCIA);
			lbTolerancia.setID(getIdGrupo(grupo.getKey(), "lbTolerancia"));
			
			lvVerbaGeradaConsumida = new LabelValue(0);
			lvVerbaGeradaConsumida.setID(getIdGrupo(grupo.getKey(), "lvVerbaGeradaConsumida"));
			lvVerbaLiquida = new LabelValue(0);
			lvVerbaLiquida.setID(getIdGrupo(grupo.getKey(), "lvVerbaLiquida"));
			lvTolerancia = new LabelValue(0);
			lvTolerancia.setID(getIdGrupo(grupo.getKey(), "lvTolerancia"));
			lvVerbaOficial = new LabelValue(0);
			lvVerbaOficial.setID(getIdGrupo(grupo.getKey(), "lvVerbaOficial"));

			lvTolerancia.setFont(UiUtil.fontVerySmall);
			lbTolerancia.setFont(UiUtil.fontVerySmall);
			
			updateVerbaGrupoSaldoValues(verba, lvVerbaOficial, lvVerbaGeradaConsumida, lvVerbaLiquida, lvTolerancia);
			UiUtil.add(containerGrupo, new LabelTitle(verba.getGrupoProduto1().toString()), CENTER, y);
			y = AFTER + HEIGHT_GAP_BIG;
			UiUtil.add(containerGrupo, new LabelName(Messages.VERBAGRUPOSALDO_NOME_ENTIDADE), BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGrupo, lvVerbaOficial, AFTER + WIDTH_GAP, SAME);
			y = AFTER + HEIGHT_GAP_BIG;
			UiUtil.add(containerGrupo, lbTolerancia, BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGrupo, lvTolerancia, AFTER + WIDTH_GAP, SAME + 4);
			y = AFTER + HEIGHT_GAP_BIG;
			UiUtil.add(containerGrupo, new LabelName(Messages.VERBASALDO_LABEL_CONSUMO_ITEM_GRUPO), BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGrupo, lvVerbaGeradaConsumida, AFTER + WIDTH_GAP, SAME);
			y = AFTER + HEIGHT_GAP_BIG;
			UiUtil.add(containerGrupo, new LabelName(Messages.VERBASALDO_LABEL_CONSUMO_LIQUIDO), BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGrupo, lvVerbaLiquida, AFTER + WIDTH_GAP, SAME);
		}
		tabs.setContainer(tabsString.indexOf(Messages.GRUPO_PLURAL), containerGrupo);
	}

	private String getIdGrupo(String keyGrupo, String id) {
		return keyGrupo + "_" + id;
	}
	
	private void updateVerbaGrupoSaldoValues(final VerbaGrupoSaldo verba, final LabelValue lvSaldo, final LabelValue lvVerbaConsumida, final LabelValue lvVerbaLiquida, final LabelValue lvTolerancia) throws SQLException {
		final boolean containsKey = hashGrupo.containsKey(verba.cdGrupoProduto1);
		final double vlSaldoOficial = VerbaGrupoSaldoService.getInstance().getVlSaldo(verba.cdEmpresa, verba.cdRepresentante, verba.cdGrupoProduto1);
		final double[] consumoSaldoTolerancia = VerbaGrupoSaldoService.getInstance().getConsumoVerbaGrupoPedidosAbertos(currentItemPedido, verba, lvTolerancia != null);
		final double saldo = containsKey ? VerbaGrupoSaldoService.getInstance().getVlTotalGrupoSaldo(currentItemPedido, verba) - vlSaldoOficial + consumoSaldoTolerancia[0] : consumoSaldoTolerancia[0];
		if (lvTolerancia != null) {
			double vlToleranciaOficial = VerbaGrupoSaldoService.getInstance().getVlToleranciaDisponivel(currentItemPedido, verba, vlSaldoOficial);
			vlToleranciaOficial += consumoSaldoTolerancia[1];
			double vlSaldoErp = VerbaGrupoSaldoService.getInstance().getVlSaldoErpRelatorio(verba.cdEmpresa, verba.cdRepresentante, verba.cdGrupoProduto1);
			if (vlSaldoErp < 0) {
				vlToleranciaOficial += vlSaldoErp;
			}
			lvTolerancia.setValue(vlToleranciaOficial);
		}
		final double vlLiquido = vlSaldoOficial + saldo;
		lvSaldo.setValue(vlSaldoOficial);
		lvVerbaConsumida.setValue(saldo);
		lvVerbaLiquida.setValue(vlLiquido);
		lvVerbaLiquida.setForeColor(vlLiquido < 0 ? ColorUtil.softRed : ColorUtil.softGreen);
	}
	

	private void addComponentsContainerGeral() {
		int y = AFTER + HEIGHT_GAP_BIG;
		if (lbVlGrupoProdDisp != null) {
			UiUtil.addSeparatorCenter(containerGeral, AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(containerGeral, new LabelName(Messages.VERBAGRUPOSALDO_NOME_ENTIDADE), BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGeral, lbVlGrupoProdDisp, AFTER + WIDTH_GAP, SAME);
			y = AFTER + HEIGHT_GAP_BIG;
			UiUtil.add(containerGeral, new LabelName(Messages.VERBASALDO_LABEL_CONSUMO_ITEM_GRUPO), BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGeral, lvVlConsumoItemGrupo, AFTER + WIDTH_GAP, SAME);
			y = AFTER + HEIGHT_GAP_BIG;
			UiUtil.add(containerGeral, new LabelName(Messages.VERBASALDO_LABEL_CONSUMO_LIQUIDO), BaseUIForm.CENTEREDLABEL, y);
			UiUtil.add(containerGeral, lbVlGrupoLiquido, AFTER + WIDTH_GAP, SAME);
		}
		tabs.setContainer(0, containerGeral);
	}
	
	private void setValues(ItemPedido itemPedido) throws SQLException {
		hashGrupo = new HashMap<String, VerbaGrupoSaldo>();
		ItemPedido itemClone = (ItemPedido) itemPedido.clone();
		itemClone.vlToleranciaVerGruSaldo = itemClone.vlVerbaGrupoItem < 0 ? itemClone.vlToleranciaVerGruSaldo : 0d;
		VerbaGrupoSaldo verba = new VerbaGrupoSaldo();
		verba.cdEmpresa = itemPedido.cdEmpresa;
		verba.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
		verba.cdRepresentante = itemPedido.cdRepresentante;
		double vlSaldoGrupo = VerbaGrupoSaldoService.getInstance().getVlVerbaDisponivel(itemPedido, true);
		double vlToleranciaDisp = VerbaGrupoSaldoService.getInstance().getVlToleranciaDisponivel(itemPedido);
		verba.vlSaldo = vlSaldoGrupo + itemClone.vlVerbaGrupoItem;
		verba.vlTolerancia = vlToleranciaDisp + (itemClone.vlToleranciaVerGruSaldo > 0 ? itemClone.vlToleranciaVerGruSaldo : 0);
		hashGrupo.put(itemClone.getProduto().cdGrupoProduto1, verba);
		if (lbVlGrupoProdDisp != null) {
			updateVerbaGrupoSaldoValues(verba, lbVlGrupoProdDisp, lvVlConsumoItemGrupo, lbVlGrupoLiquido, null);
		}
	}

}
