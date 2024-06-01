package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldoVigencia;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoVigenciaService;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelVerbaSaldoWindow extends WmwWindow {

	private LabelValue lbVerbaDisponivel;
	private LabelValue lbVlVerbaDisponivelMesPassado;
	private LabelValue lbVerbaConsumida;
	private LabelValue lbVerbaPositiva;
	private LabelValue lbVlVerbaSaldoPedido;
	private LabelValue lbVerbaSaldoFinal;
	private LabelValue lbVerbaMinimo;
	
	private LabelValue lbVerbaSaldoConsumidoPedido;
	private LabelValue lbVerbaSaldoPositivoPedido;
	private LabelValue lbVerbaAtualPedido;
	
	private ScrollTabbedContainer tbSaldo;
	private ButtonPopup btRecalcular;
	public BaseButton btVerbaSaldoOnline;
	
	private Pedido pedido;

	public RelVerbaSaldoWindow(Pedido pedido) throws SQLException {
		super(Messages.VERBASALDO_NOME_ENTIDADE);
		this.pedido = pedido;
		double vlVerbaPedido = ValueUtil.round(pedido.vlVerbaPedido); // Verba do pedido sempre vem com valor negativo
		
		if (LavenderePdaConfig.usaPedidoBonificacao() && pedido.isPedidoBonificacao()) {
		}
		double vlVerbaPositiva =  ValueUtil.round(pedido.vlVerbaPedidoPositivo);
		setDefaultRect();
		btRecalcular = new ButtonPopup(Messages.BOTAO_RECALCULAR);
		addButtonPopup(btRecalcular);
		addButtonPopup(btFechar);
		add(new LabelName(Messages.VERBASALDO_VERBAREPRESENTANTE), CENTER, getTop() + HEIGHT_GAP);
		Container base = this;
		tbSaldo = new ScrollTabbedContainer(new String[] {Messages.VERBASALDO_LABEL_VLSALDO, Messages.TITULOFINANCEIRO_LABEL_DSHISTORICO});
		if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
			UiUtil.add(base, tbSaldo, AFTER, 1);
			base = tbSaldo.getContainer(0);
		}
		if (!LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			Vector list =  VerbaSaldoService.getInstance().findAllGrupoByContaC();
			if (ValueUtil.isNotEmpty(list)) {
				VerbaSaldo verbaSaldo = VerbaSaldoService.getInstance().getVerbaSaldo(list);
				if (verbaSaldo != null) {
					final int space = WIDTH_GAP_BIG;
					int heighGap = HEIGHT_GAP_BIG;
					//--
					if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
						if (verbaSaldo.dtVigenciaInicial.isAfter(pedido.dtEmissao) || verbaSaldo.dtVigenciaFinal.isBefore(pedido.dtEmissao)) {
							vlVerbaPedido = 0;
						}
						UiUtil.add(base, new LabelValue(StringUtil.getStringValue(verbaSaldo.dtVigenciaInicial) + " à " + StringUtil.getStringValue(verbaSaldo.dtVigenciaFinal)), CENTER - 3, SAME + heighGap);
						UiUtil.addSeparatorCenter(base, AFTER + HEIGHT_GAP);
						UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLSALDOINICIAL), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
						UiUtil.add(base, new LabelValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(verbaSaldo.vlSaldoInicial)), AFTER + space, SAME);
						heighGap = HEIGHT_GAP;
					}
					UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLVERBADISPONIVEL), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
					lbVerbaDisponivel = new LabelValue();
					lbVerbaDisponivel.setID("lbVerbaDisponivel");
					lbVerbaDisponivel.useCurrencyValue = true;
					UiUtil.add(base, lbVerbaDisponivel, AFTER + WIDTH_GAP_BIG, SAME);
					double vlVerbaDisponivel = VerbaSaldoService.getInstance().getVlSaldoDisponivelRelatorio(pedido);
					lbVerbaDisponivel.setValue(vlVerbaDisponivel);
					
					if (LavenderePdaConfig.usaVerbaSaldoOnline) {
						btVerbaSaldoOnline = new BaseButton(UiUtil.getColorfulImage("images/reload.png", UiUtil.getLabelPreferredHeight() - WIDTH_GAP, UiUtil.getLabelPreferredHeight() - WIDTH_GAP));
						btVerbaSaldoOnline.transparentBackground = true;
						btVerbaSaldoOnline.useBorder = false;
						btVerbaSaldoOnline.setVisible(true);
						UiUtil.add(base, btVerbaSaldoOnline, SAME + (WIDTH_GAP_BIG * 3) + fm.stringWidth(StringUtil.getStringValue(vlVerbaDisponivel)), SAME - (WIDTH_GAP / 2), UiUtil.getLabelPreferredHeight() + WIDTH_GAP, UiUtil.getLabelPreferredHeight() + WIDTH_GAP);	
					}
					
					UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
					lbVerbaConsumida = new LabelValue();
					lbVerbaConsumida.setID("lbVerbaConsumida");
					lbVerbaConsumida.useCurrencyValue = true;
					UiUtil.add(base, lbVerbaConsumida, AFTER + space, SAME);
					
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
						double vlVerbaPedidoConsumida = LavenderePdaConfig.geraVerbaPositiva ? pedido.vlVerbaPedido : pedido.getVlVerbaPedidoDisponivel();
						if (vlVerbaPedidoConsumida < 0) {
							lbVerbaConsumida.setValue(StringUtil.getStringValueToInterface(vlVerbaPedidoConsumida));
						} else {
							lbVerbaConsumida.setValue(StringUtil.getStringValueToInterface(0d));
						}
						
					} else {
						lbVerbaConsumida.setValue(StringUtil.getStringValueToInterface(vlVerbaPedido));
					}
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.geraVerbaPositiva) {
						UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAGERADOPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
						lbVerbaPositiva = new LabelValue();
						lbVerbaPositiva.setID("lbVerbaPositiva");
						lbVerbaPositiva.useCurrencyValue = true;
						UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
						double verbaGeradaPedido = (LavenderePdaConfig.isApresentaSaldoVerbaPedidoInRelVerba()) ? pedido.getVlVerbaPedidoDisponivel() : pedido.vlVerbaPedidoPositivo;
						lbVerbaPositiva.setValue(StringUtil.getStringValueToInterface((verbaGeradaPedido) > 0 ? verbaGeradaPedido : 0d));
					}
					
					if (LavenderePdaConfig.isApresentaSaldoVerbaPedidoInRelVerba()) {
						UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLVERBASALDOPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + heighGap);
						lbVlVerbaSaldoPedido = new LabelValue();
						lbVlVerbaSaldoPedido.useCurrencyValue = true;
						UiUtil.add(base, lbVlVerbaSaldoPedido, AFTER + space, SAME);
						double verbaSaldoPedido = pedido.getVlVerbaPedidoDisponivel();
						lbVlVerbaSaldoPedido.setValue(StringUtil.getStringValueToInterface(verbaSaldoPedido));
					}
					
					Button sep2 = new Button("");
					sep2.setBackForeColors(ColorUtil.componentsForeColor, ColorUtil.componentsForeColor);
					int oneChar = fm.charWidth('A');
					UiUtil.add(base, sep2, SAME - WIDTH_GAP, AFTER + WIDTH_GAP, FILL - (oneChar * 4), 1);
					//--
					UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLSALDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
					lbVerbaSaldoFinal = new LabelValue();
					lbVerbaSaldoFinal.setID("lbVerbaSaldoFinal");
					lbVerbaSaldoFinal.useCurrencyValue = true;
					UiUtil.add(base, lbVerbaSaldoFinal, AFTER + space, SAME);
					lbVerbaSaldoFinal.setValue(VerbaSaldoService.getInstance().getDsSaldoFinal(pedido, vlVerbaDisponivel, vlVerbaPedido, vlVerbaPositiva));
					lbVerbaSaldoFinal.setForeColor(ValueUtil.getDoubleValue(lbVerbaSaldoFinal.getValue()) > 0 ? ColorUtil.softGreen : ColorUtil.softRed);
					if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba) {
						UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLMINVERBA), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
						lbVerbaMinimo = new LabelValue();
						lbVerbaMinimo.useCurrencyValue = true;
						lbVerbaMinimo.setValue(verbaSaldo.vlMinVerba);
						UiUtil.add(base, lbVerbaMinimo, AFTER + space, SAME);
					}
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && !LavenderePdaConfig.geraVerbaPositiva && !LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
						Button sep3 = new Button("");
						UiUtil.add(base, sep3, LEFT, AFTER + (HEIGHT_GAP * 10), FILL, 1);
						sep3.setForeColor(Color.getRGB(125, 125, 125));
						//--
						UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDOPOSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
						lbVerbaPositiva = new LabelValue();
						lbVerbaPositiva.useCurrencyValue = true;
						lbVerbaPositiva.setValue(StringUtil.getStringValueToInterface(pedido.vlVerbaPedidoPositivo));
						UiUtil.add(base, lbVerbaPositiva, AFTER + space, SAME);
					}
					
					if (LavenderePdaConfig.isMostraFlexPositivoPedido() && LavenderePdaConfig.usaVerbaPositivaApenasPedidoCorrente) {
						if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
							vlVerbaPedido -= pedido.vlDesconto;
						}
						Button sep4 = new Button("");
						UiUtil.add(base, sep4, LEFT, AFTER + (HEIGHT_GAP * 10), FILL, 1);
						sep4.setForeColor(Color.getRGB(125, 125, 125));
						//--
						add(new LabelName(Messages.VERBASALDO_VERBAPEDIDO), CENTER, AFTER + (HEIGHT_GAP * 2));
						
						UiUtil.add(base, new LabelName(Messages.VERBA_VERBAPEDIDO_SALDO_POSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
						lbVerbaSaldoPositivoPedido = new LabelValue();
						lbVerbaSaldoPositivoPedido.setID("lbVerbaSaldoPositivoPedido");
						lbVerbaSaldoPositivoPedido.useCurrencyValue = true;
						UiUtil.add(base, lbVerbaSaldoPositivoPedido, AFTER + space, SAME);
						lbVerbaSaldoPositivoPedido.setValue(StringUtil.getStringValueToInterface(vlVerbaPositiva));
						
						UiUtil.add(base, new LabelName(Messages.VERBA_VERBAPEDIDO_SALDO_CONSUMIDO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
						lbVerbaSaldoConsumidoPedido = new LabelValue();
						lbVerbaSaldoConsumidoPedido.setID("lbVerbaSaldoConsumidoPedido");
						lbVerbaSaldoConsumidoPedido.useCurrencyValue = true;
						UiUtil.add(base, lbVerbaSaldoConsumidoPedido, AFTER + space, SAME);
						lbVerbaSaldoConsumidoPedido.setValue(StringUtil.getStringValueToInterface(vlVerbaPedido));
						
						UiUtil.add(base, new LabelName(Messages.VERBA_VERBAPEDIDO_SALDO), BaseUIForm.CENTEREDLABEL, AFTER + (HEIGHT_GAP * 2));
						lbVerbaAtualPedido = new LabelValue();
						lbVerbaAtualPedido.setID("lbVerbaAtualPedido");
						lbVerbaAtualPedido.useCurrencyValue = true;
						UiUtil.add(base, lbVerbaAtualPedido, AFTER + space, SAME);
						lbVerbaAtualPedido.setValue(StringUtil.getStringValueToInterface(pedido.getVlVerbaPedidoDisponivel()));
						lbVerbaAtualPedido.setForeColor(ValueUtil.getDoubleValue(lbVerbaAtualPedido.getValue()) > 0 ? ColorUtil.softGreen : ColorUtil.softRed);
					}
				} else {
					UiUtil.add(base, "Nenhum Saldo Disponível", CENTER, CENTER);
				}
				if (LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco()) {
					tbSaldo.setContainer(1, new ListVerbaSaldoForm(false));
				}
			} else {
				UiUtil.add(base, "Nenhum Saldo Disponível", CENTER, CENTER);
			}
			list = null;
		} else {
			Vector verbaList = VerbaSaldoVigenciaService.getInstance().getVerbaSaldoVigentes();
			if (ValueUtil.isNotEmpty(verbaList) || pedido.vlVerbaPedido != 0 || pedido.vlVerbaPedidoPositivo != 0) {
				if (ValueUtil.isNotEmpty(verbaList)) addComponentsVerbaVigencia(base, verbaList, pedido);
				else addEmptyComponentsVerbaVigencia(base);
				UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLVERBAPEDIDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
				lbVerbaConsumida = new LabelValue();
				lbVerbaConsumida.setID("lbVerbaConsumida");
				lbVerbaConsumida.useCurrencyValue = true;
				UiUtil.add(base, lbVerbaConsumida, AFTER + WIDTH_GAP_BIG, SAME);
				lbVerbaConsumida.setValue(vlVerbaPedido);
				UiUtil.add(base, new LabelName(Messages.VERBA_VERBAPEDIDO_SALDO_POSITIVO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
				lbVerbaPositiva = new LabelValue();
				lbVerbaPositiva.useCurrencyValue = true;
				UiUtil.add(base, lbVerbaPositiva, AFTER + WIDTH_GAP_BIG, SAME);
				lbVerbaPositiva.setValue(pedido.vlVerbaPedidoPositivo);
				UiUtil.addSeparatorCenter(base, AFTER + HEIGHT_GAP);
				UiUtil.add(base, new LabelName(Messages.VERBASALDO_LABEL_VLSALDO), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
				lbVerbaSaldoFinal = new LabelValue();
				lbVerbaSaldoFinal.setID("lbVerbaSaldoFinal");
				lbVerbaSaldoFinal.useCurrencyValue = true;
				UiUtil.add(base, lbVerbaSaldoFinal, AFTER + WIDTH_GAP_BIG, SAME);
				lbVerbaSaldoFinal.setValue(VerbaSaldoVigenciaService.getInstance().getVlFinalVerba(pedido, verbaList));
				lbVerbaSaldoFinal.setForeColor(ValueUtil.getDoubleValue(lbVerbaSaldoFinal.getValue()) > 0 ? ColorUtil.softGreen : ColorUtil.softRed);
			} else {
				UiUtil.add(base, "Nenhum Saldo Disponível", CENTER, CENTER);
			}
		}
	}

	private void addComponentsVerbaVigencia(Container base, Vector verbaList, Pedido pedido) throws SQLException {
		VerbaSaldoVigencia verba = (VerbaSaldoVigencia)verbaList.items[0];
		boolean possuiMesAnterior = verba.cdMesSaldo != DateUtil.getCurrentDate().getMonth();
		int cdMes = possuiMesAnterior ? verba.cdMesSaldo : verba.cdMesSaldo - 1;
		String label = MessageUtil.getMessage(Messages.VERBASALDOVIGENCIA_SALDO_MES, Date.getMonthName(cdMes));
		UiUtil.add(base, new LabelName(label), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		lbVlVerbaDisponivelMesPassado = new LabelValue();
		lbVlVerbaDisponivelMesPassado.useCurrencyValue = true;
		UiUtil.add(base, lbVlVerbaDisponivelMesPassado, AFTER + WIDTH_GAP_BIG, SAME);
		double vlSaldo = possuiMesAnterior ? verba.vlSaldo : 0d;
		lbVlVerbaDisponivelMesPassado.setValue(vlSaldo);
		verba = verbaList.size() > 1 ? (VerbaSaldoVigencia)verbaList.items[1] : !possuiMesAnterior ? verba : null;
		cdMes = verba != null ? verba.cdMesSaldo : DateUtil.getCurrentDate().getMonth();
		UiUtil.add(base, new LabelName(MessageUtil.getMessage(Messages.VERBASALDOVIGENCIA_SALDO_MES, Date.getMonthName(cdMes))), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		lbVerbaDisponivel = new LabelValue();
		lbVerbaDisponivel.useCurrencyValue = true;
		UiUtil.add(base, lbVerbaDisponivel, AFTER + WIDTH_GAP_BIG, SAME);
		vlSaldo = verba != null ? verba.vlSaldo : 0d;
		lbVerbaDisponivel.setValue(vlSaldo);
	}
	
	private void addEmptyComponentsVerbaVigencia(Container base) {
		String label = MessageUtil.getMessage(Messages.VERBASALDOVIGENCIA_SALDO_MES, Date.getMonthName(DateUtil.getCurrentDate().getMonth() - 1));
		UiUtil.add(base, new LabelName(label), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		lbVlVerbaDisponivelMesPassado = new LabelValue();
		lbVlVerbaDisponivelMesPassado.useCurrencyValue = true;
		UiUtil.add(base, lbVlVerbaDisponivelMesPassado, AFTER + WIDTH_GAP_BIG, SAME);
		lbVlVerbaDisponivelMesPassado.setValue(0d);
		UiUtil.add(base, new LabelName(MessageUtil.getMessage(Messages.VERBASALDOVIGENCIA_SALDO_MES, Date.getMonthName(DateUtil.getCurrentDate().getMonth()))), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
		lbVerbaDisponivel = new LabelValue();
		lbVerbaDisponivel.useCurrencyValue = true;
		UiUtil.add(base, lbVerbaDisponivel, AFTER + WIDTH_GAP_BIG, SAME);
		lbVerbaDisponivel.setValue(0d);
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btRecalcular) {
					recalcularVerba(true);
				} else if (event.target == btVerbaSaldoOnline) {
					syncronizeVerbaSaldo();
				}
			break;
		}
	}

	private void recalcularVerba(boolean showMessage) throws SQLException {
		unpop();
		processaRecalculaVerba();
		if (showMessage) {
			UiUtil.showSucessMessage(Messages.VERBASALDO_MSG_SALDO_VERBA_RECALCULADO);
		}
		RelVerbaSaldoWindow verbaSaldoForm = new RelVerbaSaldoWindow(pedido);
		verbaSaldoForm.popup();
	}

	private void processaRecalculaVerba() throws SQLException {
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
			VerbaSaldoService.getInstance().recalculateVerbaSaldoPdaForPedidos();
		} else {
			VerbaSaldoService.getInstance().recalculateAndUpdateVerbaSaldoPda();
		}
	}

	private void syncronizeVerbaSaldo() throws SQLException {
		try {
			ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
			ps.nuMaxTentativas = 1;
			LavendereWeb2Tc lwWeb2Tc = new LavendereWeb2Tc(ps);
			lwWeb2Tc.ignoreAntesEDepoisReceberDados = true;
			lwWeb2Tc.importaVerbaSaldoERPAndUpdatePDA(pedido.cdEmpresa, pedido.cdRepresentante);
			recalcularVerba(false);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.VERBA_SALDO_ERRO_ATUALIZAR, ex.getMessage()));
		}
	}

}
