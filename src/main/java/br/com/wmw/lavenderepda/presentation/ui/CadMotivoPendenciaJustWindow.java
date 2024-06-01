package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.MotivoPendenciaJust;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoPendenciaJustComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadMotivoPendenciaJustWindow extends WmwWindow {
	
	private EditMemo edObservacao;
	private LabelValue lvMotivoPendencia;
	private ButtonPopup btFecharPedido;
	private ButtonPopup btCancelar;
	private MotivoPendenciaJustComboBox cbMotivoPendenciaJust;
	private MotivoPendencia motivoPendencia;
	private Pedido pedido;
	public boolean naoPossuiJustificativas;

	public CadMotivoPendenciaJustWindow(MotivoPendencia motivoPendencia, Pedido pedido) throws SQLException {
		super(Messages.MOTIVO_PENDENCIA_JUST_TITULO);
		edObservacao = new EditMemo("@@@", 3, 4000);
		edObservacao.drawBackgroundWhenDisabled = true;
		edObservacao.setValue(pedido.dsObsMotivoPendencia);
		lvMotivoPendencia = new LabelValue();
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PED);
		btCancelar = new ButtonPopup(Messages.BT_CANCELAR);
		btFechar.setVisible(false);
		cbMotivoPendenciaJust = new MotivoPendenciaJustComboBox(motivoPendencia);
		cbMotivoPendenciaJust.drawBackgroundWhenDisabled = true;
		selectedMotivoPendenciaJust(cbMotivoPendenciaJust, pedido);
		mudaVisibilidadeObservacao(defineVisibilidadeObservacao());
		this.motivoPendencia = motivoPendencia;
		this.pedido = pedido;
		setDefaultRect();
		naoPossuiJustificativas = cbMotivoPendenciaJust.size() <= 1;
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btCancelar) {
				unpop();
				closedByBtFechar = true;
			} else if (event.target == btFecharPedido) {
				btFecharPedido();
			}
			break;
		case ValueChangeEvent.VALUE_CHANGE:
			if (event.target == cbMotivoPendenciaJust) {
				motivoPendenciaJustComboChange();
			}
			break;
		}
	}
	
	
	private void btFecharPedido() {
		MotivoPendenciaJust motivoPendenciaPrincipalJust = (MotivoPendenciaJust)cbMotivoPendenciaJust.getSelectedItem();
		if (validaCampos(motivoPendenciaPrincipalJust)) {
			pedido.cdMotivoPendencia = motivoPendenciaPrincipalJust.cdMotivoPendencia;
			pedido.cdMotivoPendenciaJust = motivoPendenciaPrincipalJust.cdMotivoPendenciaJust;
			pedido.dsObsMotivoPendencia = edObservacao.getValue();
			unpop();
		}
	}

	private boolean validaCampos(MotivoPendenciaJust motivoPendenciaPrincipalJust) {
		if (motivoPendenciaPrincipalJust == null) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_MOTIVOPENDENCIAJUST_JUST_OBRIGATORIA);
			return false;
		} else if (motivoPendenciaPrincipalJust.isHabilitaObservacao()) {
			if (ValueUtil.isEmpty(edObservacao.getValue()) && motivoPendenciaPrincipalJust.isObservacaoObrigatoria()) {
				UiUtil.showErrorMessage(Messages.MSG_ERRO_MOTIVOPENDENCIAJUST_OBS_OBRIGATORIA);
				return false;
			} else if (ValueUtil.isNotEmpty(edObservacao.getValue()) && edObservacao.getValue().trim().length() < 10) {
				UiUtil.showErrorMessage(Messages.MSG_ERRO_MOTIVOPENDENCIAJUST_MIN_CARACTERES_OBRIGATORIO);
				return false;
			}
		}
		return true;
	}

	private void motivoPendenciaJustComboChange() {
		MotivoPendenciaJust motivoPendenciaJust = (MotivoPendenciaJust)cbMotivoPendenciaJust.getSelectedItem();
		if (motivoPendenciaJust != null) {
			mudaVisibilidadeObservacao(motivoPendenciaJust.isHabilitaObservacao());
			if (motivoPendenciaJust.isObservacaoNaoUtilizada()) {
				edObservacao.setValue(ValueUtil.VALOR_NI);
			}
		}
	}

	private void mudaVisibilidadeObservacao(boolean habilitaObservacao) {
		edObservacao.setEnabled(habilitaObservacao);
		edObservacao.setEditable(habilitaObservacao);
		if (!habilitaObservacao) {
			edObservacao.setValue(ValueUtil.VALOR_NI);
		}
	}

	private boolean defineVisibilidadeObservacao() {
		MotivoPendenciaJust motivoPendenciaJust = (MotivoPendenciaJust)cbMotivoPendenciaJust.getSelectedItem();
		return motivoPendenciaJust != null && motivoPendenciaJust.isHabilitaObservacao();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.MOTIVO_PENDENCIA_JUST_PRINCIPAL), lvMotivoPendencia, getLeft(), getNextY());
		lvMotivoPendencia.setMultipleLinesText(motivoPendencia.dsMotivoPendencia);
		UiUtil.add(this, new LabelName(Messages.MOTIVO_PENDENCIA_JUST_COMBO), cbMotivoPendenciaJust, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.MOTIVO_PENDENCIA_JUST_OBSERVACAO), edObservacao, getLeft(), getNextY(), getWFill(), getWFill());
		addButtonPopup(btFecharPedido);
		addButtonPopup(btCancelar);
	}
	
	private void selectedMotivoPendenciaJust(MotivoPendenciaJustComboBox cbMotivoPendenciaJust, Pedido pedido) {
		if(cbMotivoPendenciaJust.size() == 2) {
			cbMotivoPendenciaJust.setSelectedIndex(1);
		} else if (ValueUtil.isNotEmpty(pedido.cdMotivoPendenciaJust)){
			cbMotivoPendenciaJust.setSelectedItem(new MotivoPendenciaJust(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdMotivoPendencia, pedido.cdMotivoPendenciaJust));
		}
	}

}
