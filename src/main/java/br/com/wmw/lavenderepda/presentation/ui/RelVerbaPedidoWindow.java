package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelVerbaPedidoWindow extends WmwWindow {

	private LabelName lbSaldoVerba;
	private LabelValue lbSaldoVerbaValue;
	private LabelName lbTotalPedido;
	private LabelValue lbTotalPedidoValue;
	private LabelName lbDesconto;
	private LabelValue lbDescontoValue;
	private LabelName lbValorFinal;
	private LabelValue lbValorFinalValue;
	private LabelName lbMotivoDesconto;
	private LabelName lbPercMaxDesconto;
	private LabelValue lvPercMaxDesconto;
	private EditNumberFrac edVlFinal;
	private EditNumberFrac edvlDesc;
	private ButtonPopup btSalvar;
	private ButtonPopup btCalcular;
	private EditMemo edMotivoDesconto;

	private Pedido pedido;

	public RelVerbaPedidoWindow(Pedido pedido) throws SQLException {
		super(Messages.DESCONTO_PEDIDO_LABEL);
		this.pedido = pedido;
		Vector list =  VerbaSaldoService.getInstance().findAllGrupoByContaC();
		if (list.size() == 0) {
			throw new ValidationException(Messages.VERBASALDO_MSG_CLIENTE_SEM_VERBA_SALDO);
		}
		VerbaSaldo verbaSaldo = (VerbaSaldo) list.items[0];
		//--
		lbSaldoVerba = new LabelName(Messages.DESCONTO_PEDIDO_SALDO_VERBA, RIGHT);
		double vlVerbaDisponivel = LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente() ? verbaSaldo.vlSaldo : verbaSaldo.vlSaldo + pedido.vlDescontoOld;
		lbSaldoVerbaValue = new LabelValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerbaDisponivel));
		lbTotalPedido = new LabelName(Messages.PEDIDO_LABEL_TOTALPEDIDO, RIGHT);
		lbTotalPedidoValue = new LabelValue(Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(getVlTotalItens()));
		lbDesconto = new LabelName(Messages.PEDIDO_LABEL_DESCONTO_VALOR, RIGHT);
		lbDescontoValue = new LabelValue(Messages.PRODUTO_LABEL_RS);
		lbValorFinal = new LabelName(Messages.PEDIDO_LABEL_VALORFINAL, RIGHT);
		lbValorFinalValue = new LabelValue(Messages.PRODUTO_LABEL_RS);
		edvlDesc = new EditNumberFrac("0000000", 8);
		edvlDesc.setValue(ValueUtil.round(pedido.vlDesconto));
		edvlDesc.setEnabled(pedido.isPedidoAberto());
		edvlDesc.autoSelect = true;
		edVlFinal = new EditNumberFrac("0000000", 8);
		edVlFinal.setValue(ValueUtil.round(getVlTotalItens() - (pedido.vlDesconto)));
		edVlFinal.setEditable(false);
		edVlFinal.setForeColor(verbaSaldo.vlSaldo > 0 ? ColorUtil.softGreen : ColorUtil.softRed);
		lbMotivoDesconto = new LabelName(Messages.LABEL_MOTIVO_DESC_PEDIDO);
		edMotivoDesconto = new EditMemo("@@@", 4, 4000);
		edMotivoDesconto.drawBackgroundWhenDisabled = true;
		edMotivoDesconto.setEnabled(false);
		edMotivoDesconto.setValue(pedido.dsMotivoDesconto);
		haveValue();
		lbPercMaxDesconto = new LabelName(Messages.PEDIDO_LABEL_PERC_MAX_DESCONTO);
		if (LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0) {
			double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba;
			double vlMaxDesc =  pedido.vlTotalItens * (pctMaxDesconto / 100);
			lvPercMaxDesconto = new LabelValue(" " + Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(vlMaxDesc) + " (" + StringUtil.getStringValueToInterface(pctMaxDesconto) + " %) ");
		} else {
			lvPercMaxDesconto = new LabelValue(ValueUtil.VALOR_ZERO + " % ");
		}
		btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btSalvar.setVisible(pedido.isPedidoAberto());
		btCalcular = new ButtonPopup(Messages.BOTAO_CALCULAR);
		btCalcular.setVisible(pedido.isPedidoAberto());
		btFechar.setText(FrameworkMessages.BOTAO_CANCELAR);
		setRect(CENTER, CENTER, (int) (UiUtil.getScreenWidthOrigin() * 0.8), 500);
	}

	@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btSalvar);
		addButtonPopup(btCalcular);
		addButtonPopup(btFechar);
		addComponentsCentralizados();
		//--
		resetSetPositions();
		if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido()) {
			setRect(CENTER, CENTER, KEEP, edMotivoDesconto.getY2() + footerH + UiUtil.getControlPreferredHeight() * 2 + HEIGHT_GAP_BIG * 3);
		} else {
			setRect(CENTER, CENTER, KEEP, edVlFinal.getY2() + footerH + UiUtil.getControlPreferredHeight() * 2 + HEIGHT_GAP_BIG  * 2);
		}
		super.initUI();	
	}

	private void addComponentsCentralizados() {
		UiUtil.add(this, lbSaldoVerba, BaseUIForm.CENTEREDLABEL, getNextY() + HEIGHT_GAP);
		UiUtil.add(this, lbSaldoVerbaValue, AFTER + WIDTH_GAP_BIG, SAME);
		if (LavenderePdaConfig.mostraPercDescMaxPedido) {
			UiUtil.add(this, lbPercMaxDesconto, BaseUIForm.CENTEREDLABEL, getNextY());
			UiUtil.add(this, lvPercMaxDesconto, AFTER + WIDTH_GAP_BIG, SAME);
		}
		UiUtil.add(this, lbTotalPedido, BaseUIForm.CENTEREDLABEL, getNextY());
		UiUtil.add(this, lbTotalPedidoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, lbDesconto, BaseUIForm.CENTEREDLABEL, getNextY());
		UiUtil.add(this, lbDescontoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, edvlDesc, AFTER, SAME, PREFERRED, PREFERRED);
		UiUtil.add(this, lbValorFinal, BaseUIForm.CENTEREDLABEL, getNextY());
		UiUtil.add(this, lbValorFinalValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, edVlFinal, AFTER, SAME, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido()) {
			UiUtil.add(this, lbMotivoDesconto, edMotivoDesconto, getLeft(), getNextY() + HEIGHT_GAP_BIG * 2);
		}
	}

	public void btCalcularClick() throws SQLException {
		boolean aplicaDesconto = true;
		double newVlTotalPedido = getVlTotalItens() - (edvlDesc.getValueDouble());
		if (pedido.itemPedidoList.size() < LavenderePdaConfig.qtMinimaItensParaLiberarVerbaPorPedido && !pedido.isSimulaControleVerba()) {
			throw new ValidationException(Messages.VERBAPEDIDO_QT_MINIMA_ITENS);
		}
		if (getVlTotalItens() != 0) {
			double pctDesconto = 100 - ((newVlTotalPedido * 100) / getVlTotalItens());
			double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? ValueUtil.round(pedido.getCliente().vlPctMaxDesconto) : ValueUtil.round(LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba);
			if (pctMaxDesconto > 0 && pctDesconto > pctMaxDesconto) {
				edvlDesc.setValue(pedido.vlDesconto);
				edVlFinal.setValue(ValueUtil.round(pedido.vlTotalPedido));
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, StringUtil.getStringValueToInterface(pctMaxDesconto)));
				aplicaDesconto = false;
			}
		} else {
			if (edvlDesc.getValueDouble() > 0) {
				edvlDesc.setValue(0);
				UiUtil.showWarnMessage(Messages.MSG_NAO_POSSUI_ITEM_PEDIDO);
				aplicaDesconto = false;
			}
		}
		double vlDesconto = ValueUtil.round(edvlDesc.getValueDouble() * -1);
		double vlDescontoOld = ValueUtil.round(pedido.vlDescontoOld * -1);
		if (vlDesconto < 0) {
    		double vlSaldo = ValueUtil.round(VerbaSaldoService.getInstance().getVlSaldo("0"));
    		double tot = ValueUtil.round((vlSaldo + vlDesconto) - vlDescontoOld);
    		if (tot < 0) {
    			tot = ValueUtil.round(tot + SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba);
    			if (tot < 0  && !pedido.isSimulaControleVerba()) {
        			String[] args = {StringUtil.getStringValueToInterface(vlDesconto * -1), StringUtil.getStringValueToInterface(vlSaldo - vlDescontoOld)};
        			throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_INDISPONIVEL, args));
    			}
    		}
		}
		if (aplicaDesconto) {
			edVlFinal.setValue(ValueUtil.round(getVlTotalItens() - (edvlDesc.getValueDouble())));
		}
	}

	public double getVlTotalItens() {
		return pedido.vlTotalItens;
	}

	public double findToleranciaVerbaNegativaRep() throws SQLException {
		Representante representanteFilter = new Representante();
		representanteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector representanteList = RepresentanteService.getInstance().findAllByExample(representanteFilter);
		Representante representante = (Representante) representanteList.items[0];
		return representante.vlToleranciaVerba;
	}

	private boolean validateQtdMinimaCaracteres() {
		String dsMotivo = edMotivoDesconto.getText();
		String qtdMinFiltro = LavenderePdaConfig.usaIndicacaoMotivoDescPedido;
		int qtdMinMotivo = 0;
		if (qtdMinFiltro.equals("S") && dsMotivo.length() < 1) {
			qtdMinMotivo = 1;
		} else if (ValueUtil.getIntegerValue(qtdMinFiltro) > 0 && dsMotivo.length() < ValueUtil.getIntegerValue(qtdMinFiltro)) {
			qtdMinMotivo = ValueUtil.getIntegerValue(qtdMinFiltro);
		}
		if (qtdMinMotivo > 0) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.MSG_MOTIVO_DESC_NAO_INFORMADO, StringUtil.getStringValueToInterface(qtdMinMotivo)));
			return false;
		}
		return true;
	}

	public void haveValue() {
		if (ValueUtil.getDoubleValue(edvlDesc.getText()) > 0) {
			edMotivoDesconto.setEnabled(true);
		} else {
			edMotivoDesconto.setEnabled(false);
			edMotivoDesconto.setText("");
		}
	}

	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.FOCUS_OUT: {
				if (event.target == edvlDesc) {
					btCalcularClick();
					haveValue();
				}
				break;
			}
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					btCalcularClick();
					if (LavenderePdaConfig.isUsaIndicacaoMotivoDescPedido() && ValueUtil.getDoubleValue(edvlDesc.getText()) > 0) {
						if (!validateQtdMinimaCaracteres()) {
							return;
						}
					}
					boolean result = VerbaSaldoService.getInstance().validateSaldo(edvlDesc.getValueDouble() * -1, pedido.vlDescontoOld * -1, pedido.vlVerbaPedidoPositivo, pedido.vlVerbaPedido);
					if (result) {
						pedido.dsMotivoDesconto = edMotivoDesconto.getText();
						pedido.vlDesconto = edvlDesc.getValueDouble();
						pedido.vlTotalPedido = edVlFinal.getValueDouble();
						unpop();
					}
				}
				if (event.target == edvlDesc) {
					haveValue();
				}
				if (event.target == btCalcular) {
					btCalcularClick();
				}
				break;
			}
			case KeyboardEvent.KEYBOARD_PRESS: {
				if (event.target == edvlDesc) {
					btCalcularClick();
				}
				break;
			}
		}
	}

}
