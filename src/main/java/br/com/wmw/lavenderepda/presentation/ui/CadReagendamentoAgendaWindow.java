package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditTimeMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.vo.AgendaVisitaReagendamentoParams;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoAgendaComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class CadReagendamentoAgendaWindow extends WmwWindow {

	private LabelName lbDtAgenda;
	private EditDate edDtAgenda;
	private LabelName lbMotivoAgenda;
	private MotivoAgendaComboBox cbMotivoAgenda;
	private LabelName lbHoraAgenda;
	private LabelName lbHoraAgendaFim;
	private EditTimeMask edHoraAgenda;
	private EditTimeMask edHoraAgendaFim;
	private LabelValue lbInfoDtLimite;
	private Date dtLimiteReagendamento;
	private ButtonPopup btConfirmar;
	private AgendaVisita agendaVisita;
	private Date dataAgendaOriginal;
	private LabelName lbDsOBservacao;
	private EditMemo edObs;
	public boolean reagendouAgenda;
	public Date dtAgendaAtual;
	
	public CadReagendamentoAgendaWindow(AgendaVisita agendaVisita, Date dtAgendaAtual) throws SQLException {
		super(Messages.BT_REAGENDAR_AGENDA);
		this.agendaVisita = agendaVisita;
		this.dtAgendaAtual = dtAgendaAtual;
		lbDtAgenda = new LabelName(Messages.LABEL_DT_REAGENDAMENTO);
		edDtAgenda = new EditDate();
		edDtAgenda.setID("edDtAgenda");
		lbMotivoAgenda = new LabelName(Messages.LABEL_MOTIVO_AGENDA);
		cbMotivoAgenda = new MotivoAgendaComboBox();
		cbMotivoAgenda.setID("cbMotivoAgenda");
		cbMotivoAgenda.loadMotivoReagendamentoAgendaVista();
		lbHoraAgenda = new LabelName(LavenderePdaConfig.isUsaHoraFimAgendaVisita() ? Messages.LABEL_HR_INICIO_REAGENDAMENTO : Messages.LABEL_HR_REAGENDAMENTO);
		lbHoraAgendaFim = new LabelName(Messages.LABEL_HR_REAGENDAMENTO_FIM);
		edHoraAgenda = new EditTimeMask(EditTimeMask.FORMATO_HHMM);
		edHoraAgenda.setID("edHoraAgenda");
		edHoraAgendaFim = new EditTimeMask(EditTimeMask.FORMATO_HHMM);
		edHoraAgendaFim.setID("edHoraAgendaFim");
		lbInfoDtLimite = new LabelValue(" - ", CENTER);
		lbInfoDtLimite.autoMultipleLines = true;
		dataAgendaOriginal = ValueUtil.isNotEmpty(agendaVisita.dtAgendaOriginal) ? agendaVisita.dtAgendaOriginal : agendaVisita.dtAgendaAtual; 
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia != 0) {
			dtLimiteReagendamento = AgendaVisitaService.getInstance().getDtLimiteReagendamento(dataAgendaOriginal);
		}
		lbDsOBservacao = new LabelName(Messages.OBSERVACAO_LABEL);
		edObs = new EditMemo("@@@", 6, 255);
		edObs.setID("edObs");
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		btFechar.setText(Messages.BT_CANCELAR);
		setDefaultRect();
	}

	
	@Override
	public void initUI() {
		super.initUI();
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia != 0) {
			UiUtil.add(this, lbInfoDtLimite, getLeft(), getNextY(), FILL, PREFERRED);
			if (dtLimiteReagendamento.isAfter(DateUtil.getCurrentDate())) {
				lbInfoDtLimite.setText(MessageUtil.getMessage(Messages.LABEL_INFO_DTLIMITE, dtLimiteReagendamento));
			} else {
				lbInfoDtLimite.setText(Messages.LABEL_INFO_REAGENDAMENTO_NAO_PERMITIDO);
				edDtAgenda.setEnabled(false);
				edHoraAgenda.setEnabled(false);
				edHoraAgendaFim.setEnabled(false);
				cbMotivoAgenda.setEnabled(false);
				edObs.setEnabled(false);
			}
		}
		UiUtil.add(this, lbDtAgenda, edDtAgenda, getLeft(), getNextY());
		UiUtil.add(this, lbHoraAgenda, edHoraAgenda, getLeft(), getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
		if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
			UiUtil.add(this, lbHoraAgendaFim, edHoraAgendaFim, getLeft(), getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
		}
		if (LavenderePdaConfig.usaMotivoReagendamentoTransferenciaAgenda) {
			UiUtil.add(this, lbMotivoAgenda, cbMotivoAgenda, getLeft(), getNextY());
		}
		UiUtil.add(this, lbDsOBservacao, edObs, getLeft(), getNextY(), getWFill(), FILL - HEIGHT_GAP_BIG);
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia == 0 || dtLimiteReagendamento.isAfter(DateUtil.getCurrentDate())) {
			addButtonPopup(btConfirmar);
		}
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btConfirmar) {
					btConfirmarClick();
				} else if (event.target instanceof CalendarWmw) {
					edDtAgendaChange();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target instanceof EditDate) {
					edDtAgendaChange();
				}
			}
		}
	}
	

	private void btConfirmarClick() throws SQLException {
		AgendaVisitaReagendamentoParams agendaVisitaReagendamentoParams = new AgendaVisitaReagendamentoParams();
		agendaVisitaReagendamentoParams.cdMotivoReagendamento = cbMotivoAgenda.getValue();
		agendaVisitaReagendamentoParams.hrReagendamento = edHoraAgenda.getValue();
		agendaVisitaReagendamentoParams.dsObservacao = edObs.getValue();
		agendaVisitaReagendamentoParams.hrReagendamentoFim = edHoraAgendaFim.getValue();
		agendaVisitaReagendamentoParams.dtReagendamento = edDtAgenda.getValue();
		agendaVisitaReagendamentoParams.dtLimiteReagendamento = dtLimiteReagendamento;
		agendaVisitaReagendamentoParams.dataAgendaOriginal = dataAgendaOriginal;
		agendaVisitaReagendamentoParams.dtAgendaAtual = dtAgendaAtual;
		AgendaVisitaService.getInstance().reagendarAgendaVisita(agendaVisita, agendaVisitaReagendamentoParams);
		UiUtil.showInfoMessage(Messages.REAGENDAMENTO_SUCESSO);
		reagendouAgenda = true;
		fecharWindow();
	}
	
	private void edDtAgendaChange() {
		if (ValueUtil.isNotEmpty(edDtAgenda.getText())) {
			Date datAgenda  = DateUtil.getDateValue(edDtAgenda.getText());
			cbMotivoAgenda.setSelectedIndex(0);
			edObs.setText("");
			if (LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal && datAgenda.equals(this.agendaVisita.dtAgendaAtual)) {
				cbMotivoAgenda.setEditable(false);
				edObs.setEditable(false);
			} else {
				cbMotivoAgenda.setEditable(true);
				edObs.setEditable(true);
			}
		}
	}
	
}
