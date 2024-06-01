package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.BaseWindow;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaFornecedor;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.FornecedorService;
import br.com.wmw.lavenderepda.business.service.VerbaFornecedorService;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelVerbaFornecedorSaldoWindow extends WmwWindow {

	private LabelValue lbVerbaDisponivel;
	private LabelValue lbVerbaConsumida;
	private LabelValue lbVerbaPositiva;
	private LabelValue lbVerbaSaldoFinal;
	private LabelValue lbApenasFornecedorPedido;
	private ButtonPopup btRecalcular;
	private Pedido pedido;
	private ItemPedido itemPedido;
	private CheckBoolean ckApenasFornecedorPedido;
	private Vector verbaFornecedorSaldoList;
	private Vector fornecedorList;

	public RelVerbaFornecedorSaldoWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		super(Messages.VERBAFORNECEDOR_LABEL_TOTAL_GERAL);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		ckApenasFornecedorPedido = new CheckBoolean();
		ckApenasFornecedorPedido.setID("ckApenasFornecedorPedido");
		ckApenasFornecedorPedido.setValue(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.apresentaApenasVerbaPresentePedido));
		lbApenasFornecedorPedido = new LabelValue(Messages.VERBAFORNECEDOR_LABEL_CHECK_FORNECEDOR_PEDIDO);
		lbApenasFornecedorPedido.setID("lbApenasFornecedorPedido");
		lbApenasFornecedorPedido.setText(MessageUtil.quebraLinhas(Messages.VERBAFORNECEDOR_LABEL_CHECK_FORNECEDOR_PEDIDO, width - (WIDTH_GAP_BIG * 2) - ckApenasFornecedorPedido.getWidth()));
		load();
	}
	
	//@Override
	public void screenResized() {
		super.screenResized();
		lbApenasFornecedorPedido.setText(MessageUtil.quebraLinhas(Messages.VERBAFORNECEDOR_LABEL_CHECK_FORNECEDOR_PEDIDO, width - (WIDTH_GAP_BIG * 2) - ckApenasFornecedorPedido.getWidth()));
		reposition();
	}
	
	private void load() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			double vlSaldoTotal = 0;
			double vlVerbaPedido = ValueUtil.round(pedido.vlVerbaPedido);
			double vlVerbaPositiva =  ValueUtil.round(pedido.vlVerbaPedidoPositivo);
			setDefaultRect();
			btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
			btRecalcular.setID("btRecalcular");
			addButtonPopup(btRecalcular);
			addButtonPopup(btFechar);
			loadinfoFornecedor();
			this.setID("base");
			BaseWindow base = this;
			Container checkBase = new Container();
			final int space = WIDTH_GAP * 5;
			int heighGap = HEIGHT_GAP * 4;
			Vector verbaFornecedorErpList = VerbaFornecedorService.getInstance().findAllVerbaFornecedorErp();
			if (ValueUtil.isNotEmpty(verbaFornecedorErpList)) {
				if (LavenderePdaConfig.permiteApresentarApenasVerbaUtilizadaPedido) {
					UiUtil.add(base, checkBase, LEFT , TOP, FILL, HEIGHT_GAP_BIG * 7);
					UiUtil.add(checkBase, ckApenasFornecedorPedido, LEFT + WIDTH_GAP, CENTER);
					lbApenasFornecedorPedido.setText(MessageUtil.quebraLinhas(Messages.VERBAFORNECEDOR_LABEL_CHECK_FORNECEDOR_PEDIDO, width - (WIDTH_GAP_BIG * 2) - ckApenasFornecedorPedido.getWidth()));
					UiUtil.add(checkBase, lbApenasFornecedorPedido, AFTER + WIDTH_GAP, CENTER);
					if (ckApenasFornecedorPedido.isChecked()) {
						verbaFornecedorErpList = VerbaFornecedorService.getInstance().filtraFornecedoresNoPedido(verbaFornecedorErpList, pedido.itemPedidoList);
					}
				}
				for (int i = 0; i < verbaFornecedorErpList.size(); i++) {
					VerbaFornecedor verbaFornecedor = (VerbaFornecedor) verbaFornecedorErpList.items[i];
					double vlSaldo = getSaldoFornecedor(verbaFornecedor.cdFornecedor);
					UiUtil.add(base, new LabelTitle(getDsFornecedor(verbaFornecedor.cdFornecedor)) , CENTER, AFTER + HEIGHT_GAP_BIG);
					UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBADISPONIVEL), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
					lbVerbaDisponivel = new LabelValue();
					ADD_ID.addIDComposed(base, lbVerbaDisponivel, i, "lbVerbaDisponivel");
					UiUtil.add(base, lbVerbaDisponivel, AFTER + space, SAME);
					if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
						lbVerbaDisponivel.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldo));
					} else {
						double vlVerbaItemPedido = 0;
						double vlVerbaItemPositivoPedido = 0;
						for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
							ItemPedido itemPedidoFornecedor = (ItemPedido) pedido.itemPedidoList.items[j];
							if (ValueUtil.valueEqualsIfNotNull(itemPedidoFornecedor.getProduto().cdFornecedor, verbaFornecedor.cdFornecedor)) {
								vlVerbaItemPedido += itemPedidoFornecedor.vlVerbaItem;
								vlVerbaItemPositivoPedido += itemPedidoFornecedor.vlVerbaItemPositivo;
							}
						}
						lbVerbaDisponivel.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldo - vlVerbaItemPedido - (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaItemPositivoPedido : 0)));
						UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBAPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
						lbVerbaConsumida = new LabelValue();
						ADD_ID.addIDComposed(base, lbVerbaConsumida, i, "lbVerbaConsumida");
						UiUtil.add(base, lbVerbaConsumida, AFTER + space, SAME);
						lbVerbaConsumida.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaItemPedido));
						if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva) {
							UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBAGERADOPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
							lbVerbaPositiva = new LabelValue();
							ADD_ID.addIDComposed(base, lbVerbaPositiva, i, "lbVerbaPositiva");
							UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
							lbVerbaPositiva.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaItemPositivoPedido));
						}
						Button sep2 = new Button("");
						sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
						int oneChar = fm.charWidth('A');
						UiUtil.add(base, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - (oneChar * 4), 1);
						UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLSALDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
						lbVerbaSaldoFinal = new LabelValue();
						ADD_ID.addIDComposed(base, lbVerbaSaldoFinal, i, "lbVerbaSaldoFinal");
						UiUtil.add(base, lbVerbaSaldoFinal, AFTER + space, SAME);
						double saldoFinal = vlSaldo;
						if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
							saldoFinal += vlVerbaPedido + (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0);
						}
						lbVerbaSaldoFinal.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(saldoFinal));
						if (saldoFinal > 0) {
							lbVerbaSaldoFinal.setForeColor(ColorUtil.softGreen);
						} else {
							lbVerbaSaldoFinal.setForeColor(ColorUtil.softRed);
						}
						if (LavenderePdaConfig.isMostraFlexPositivoPedido() && !LavenderePdaConfig.geraVerbaPositiva) {
							//--
							UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBAPEDIDOPOSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
							lbVerbaPositiva = new LabelValue();
							ADD_ID.addIDComposed(base, lbVerbaPositiva, i, "lbVerbaPositiva");
							UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
							lbVerbaPositiva.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaItemPositivoPedido));
							Button sep3 = new Button("");
							UiUtil.add(base, sep3, LEFT, AFTER + (HEIGHT_GAP * 10), FILL, 1);
							sep3.setForeColor(Color.getRGB(125, 125, 125));
						}
						vlSaldoTotal += saldoFinal;
					}
				}
			}
	
			//Total Geral
			if (!LavenderePdaConfig.permiteApresentarApenasVerbaUtilizadaPedido || !ckApenasFornecedorPedido.isChecked()) {
				if (vlSaldoTotal != 0) {
					UiUtil.add(base, new LabelTitle(Messages.VERBAGRUPOSALDO_LABEL_TOTAL_GERAL), CENTER, AFTER + HEIGHT_GAP);
					UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBADISPONIVEL), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
					lbVerbaDisponivel = new LabelValue();
					ADD_ID.addIDComposed(base, lbVerbaDisponivel, "geral", "lbVerbaDisponivel");
					UiUtil.add(base, lbVerbaDisponivel, AFTER + space, SAME);
					if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
						lbVerbaDisponivel.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldoTotal));
					} else {
						lbVerbaDisponivel.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlSaldoTotal - vlVerbaPedido - (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0)));
					}
					UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBAPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
					lbVerbaConsumida = new LabelValue();
					ADD_ID.addIDComposed(base, lbVerbaConsumida, "geral", "lbVerbaConsumida");
					UiUtil.add(base, lbVerbaConsumida, AFTER + space, SAME);
					lbVerbaConsumida.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPedido));
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva) {
						UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBAGERADOPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
						lbVerbaPositiva = new LabelValue();
						ADD_ID.addIDComposed(base, lbVerbaPositiva, "geral", "lbVerbaPositiva");
						UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
						lbVerbaPositiva.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPositiva));
					}
					Button sep2 = new Button("");
					sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
					int oneChar = fm.charWidth('A');
					UiUtil.add(base, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - (oneChar * 4), 1);
					UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLSALDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
					lbVerbaSaldoFinal = new LabelValue();
					ADD_ID.addIDComposed(base, lbVerbaSaldoFinal, "geral", "lbVerbaSaldoFinal");
					UiUtil.add(base, lbVerbaSaldoFinal, AFTER + space, SAME);
					double saldoFinal = vlSaldoTotal;
					if (pedido.getTipoPedido() != null && pedido.isSimulaControleVerba()) {
						saldoFinal += vlVerbaPedido + (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva ? vlVerbaPositiva : 0);
					}
					lbVerbaSaldoFinal.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(saldoFinal));
					if (saldoFinal > 0) {
						lbVerbaSaldoFinal.setForeColor(ColorUtil.softGreen);
					} else {
						lbVerbaSaldoFinal.setForeColor(ColorUtil.softRed);
					}
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && !LavenderePdaConfig.geraVerbaPositiva) {
						UiUtil.add(base, new LabelName(Messages.VERBAFORNECEDOR_LABEL_VLVERBAPEDIDOPOSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
						lbVerbaPositiva = new LabelValue();
						ADD_ID.addIDComposed(base, lbVerbaPositiva, "geral", "lbVerbaPositiva");
						UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
						lbVerbaPositiva.setValue(Messages.VERBAFORNECEDOR_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaPositiva));
						Button sep3 = new Button("");
						UiUtil.add(base, sep3, LEFT, AFTER + (HEIGHT_GAP * 10), FILL, 1);
						sep3.setForeColor(Color.getRGB(125, 125, 125));
					}
				} else {
					UiUtil.add(base, Messages.VERBAFORNECEDOR_MSG_NENHUM_SALDO_DISPONIVEL, CENTER, AFTER + HEIGHT_GAP_BIG);
				}
			}
		} finally {
			msg.unpop();
		}
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRecalcular) {
					unpop();
					VerbaFornecedorService.getInstance().recalculateAndUpdateVerbaFornecedorPda();
					UiUtil.showSucessMessage(Messages.VERBAFORNECEDOR_MSG_SALDO_VERBA_RECALCULADO);
					RelVerbaFornecedorSaldoWindow relVerbaGrupoSaldoWindow = new RelVerbaFornecedorSaldoWindow(pedido, itemPedido);
					relVerbaGrupoSaldoWindow.popup();
				}
				if (event.target == ckApenasFornecedorPedido) {
					ConfigInternoService.getInstance().addValue(ConfigInterno.apresentaApenasVerbaPresentePedido, ConfigInterno.VLCHAVEDEFAULT, ckApenasFornecedorPedido.isChecked() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO);
					RelVerbaFornecedorSaldoWindow relVerbaGrupoSaldoWindow = new RelVerbaFornecedorSaldoWindow(pedido, itemPedido);
					unpop();
					relVerbaGrupoSaldoWindow.popup();
				}
				break;
			}
		}
	}
	
	private void loadinfoFornecedor() throws SQLException {
		VerbaFornecedor verbaFornecedorFilter = new VerbaFornecedor();
		verbaFornecedorFilter.cdEmpresa = pedido.cdEmpresa;
		verbaFornecedorFilter.cdRepresentante = pedido.cdRepresentante;
		verbaFornecedorSaldoList = VerbaFornecedorService.getInstance().findAllByExample(verbaFornecedorFilter);
		Fornecedor fornecedorFilter = new Fornecedor();
		fornecedorFilter.cdEmpresa = pedido.cdEmpresa;
		fornecedorList = FornecedorService.getInstance().findAllByExample(fornecedorFilter);
	}
	
	private double getSaldoFornecedor(String cdFornecedor) throws SQLException {
		for (int i = 0; i < verbaFornecedorSaldoList.size(); i++) {
			VerbaFornecedor verbaFornecedor = (VerbaFornecedor) verbaFornecedorSaldoList.items[i];
			if (verbaFornecedor.cdFornecedor.equals(cdFornecedor)) {
				return VerbaFornecedorService.getInstance().getVlSaldo(verbaFornecedor.cdEmpresa, verbaFornecedor.cdRepresentante, verbaFornecedor.cdFornecedor);
			}
		}
		return 0d;
	}
	
	private String getDsFornecedor(String cdFornecedor) {
		for (int i = 0; i < fornecedorList.size(); i++) {
			Fornecedor fornecedor = (Fornecedor) fornecedorList.items[i];
			if (fornecedor.cdFornecedor.equals(cdFornecedor)) {
				return fornecedor != null ? fornecedor.toString() : cdFornecedor;
			}
		}
		return cdFornecedor;
	}

}
