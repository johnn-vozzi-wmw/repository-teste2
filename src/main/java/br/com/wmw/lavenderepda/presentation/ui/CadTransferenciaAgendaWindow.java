package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
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
import br.com.wmw.lavenderepda.business.service.AgendaAtendimentoService;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoAgendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UsuarioSupervisorComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadTransferenciaAgendaWindow extends WmwWindow {

	private AgendaVisita agendaVisita;
	private LabelName lbDtAgenda;
	private EditDate edDtAgenda;
	private LabelName lbMotivoAgenda;
	private MotivoAgendaComboBox cbMotivoAgenda;
	private LabelName lbHoraAgenda;
	private EditTimeMask edHoraAgenda;
	private LabelValue lbInfoDtLimite;
	private Date dtLimReagend;
	private ButtonPopup btConfirmar;
	private LabelName lbDsOBservacao;
	private EditMemo edObs;
	private LabelName lbUsuarioSupevisor;
	private UsuarioSupervisorComboBox cbUSup;
	public boolean transferiuAgenda;
	public Vector agendaList;
	public Date dtAgendaA;
	
	
	public CadTransferenciaAgendaWindow(Vector agendaList, AgendaVisita agendaVisita, Date dtAgendaAtual) throws SQLException {
		super(Messages.BT_TRANSFERIR_AGENDA);
		this.dtAgendaA = dtAgendaAtual;
		if (ValueUtil.isNotEmpty(agendaList)) {
			this.agendaList = agendaList;
			this.agendaVisita = (AgendaVisita) agendaList.items[0];
		} else {
			this.agendaVisita = agendaVisita;
		}
		lbDtAgenda = new LabelName(Messages.LABEL_DT_TRASNFERENCIA);
		edDtAgenda = new EditDate();
		lbMotivoAgenda = new LabelName(Messages.LABEL_MOTIVO_TRANSFERENCIA);
		cbMotivoAgenda = new MotivoAgendaComboBox();
		cbMotivoAgenda.loadMotivoTransferenciaAgendaVista();
		cbUSup = new UsuarioSupervisorComboBox();
		cbUSup.loadUsuarioSupervisor();
		lbUsuarioSupevisor = new LabelName(Messages.TRANSFERENCIA_USUARIO);
		lbHoraAgenda = new LabelName(Messages.LABEL_HR_TRANSFERENCIA);
		edHoraAgenda = new EditTimeMask(EditTimeMask.FORMATO_HHMMSS); 
		lbInfoDtLimite = new LabelValue(" - ", CENTER);
		lbInfoDtLimite.autoMultipleLines = true;
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia != 0) {
			dtLimReagend = AgendaVisitaService.getInstance().getDtLimiteReagendamento(this.agendaVisita.dtAgendaAtual);
		}
		lbDsOBservacao = new LabelName(Messages.OBSERVACAO_LABEL);
		edObs = new EditMemo("@@@", 6, 255);
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		btFechar.setText(Messages.BT_CANCELAR);
		setDefaultRect();
	}

	
	@Override
	public void initUI() {
		super.initUI();
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia != 0) {
			UiUtil.add(this, lbInfoDtLimite, getLeft(), getNextY(), FILL, PREFERRED);
			if (dtLimReagend.isAfter(DateUtil.getCurrentDate())) {
				lbInfoDtLimite.setText(MessageUtil.getMessage(Messages.LABEL_INFO_DTLIMITE_TRANSFERENCIA, dtLimReagend));
			} else {
				lbInfoDtLimite.setText(Messages.LABEL_INFO_TRANSFERENCIA_NAO_PERMITIDO);
				edDtAgenda.setEnabled(false);
				edHoraAgenda.setEnabled(false);
				cbMotivoAgenda.setEnabled(false);
				edObs.setEnabled(false);
			}
		}
		UiUtil.add(this, lbDtAgenda, edDtAgenda, getLeft(), getNextY());
		UiUtil.add(this, lbHoraAgenda, edHoraAgenda, getLeft(), getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
		UiUtil.add(this, lbUsuarioSupevisor, cbUSup, getLeft(), getNextY());
		if (LavenderePdaConfig.usaMotivoReagendamentoTransferenciaAgenda) {
			UiUtil.add(this, lbMotivoAgenda, cbMotivoAgenda, getLeft(), getNextY());
		}
		UiUtil.add(this, lbDsOBservacao, edObs, getLeft(), getNextY(), getWFill(), FILL - HEIGHT_GAP_BIG);
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia == 0 || dtLimReagend.isAfter(DateUtil.getCurrentDate())) {
			addButtonPopup(btConfirmar);
		}
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btConfirmar) {
					btConfirmarClick();
				}
				break;
		}
	}


	private void btConfirmarClick() throws SQLException {
		AgendaUtil agendaUtil = new AgendaUtil();
		agendaUtil.setDsMotivoAgenda(cbMotivoAgenda.getValue());
		agendaUtil.setDsHoraAgenda(edHoraAgenda.getValue());
		agendaUtil.setDtAgenda(edDtAgenda.getValue());
		agendaUtil.setDtLimiteReagendamento(dtLimReagend);
		agendaUtil.setDsObs(edObs.getValue());
		agendaUtil.setCdSupervisor(cbUSup.getValue());
		agendaUtil.setDtAgendaA(dtAgendaA);
		if (ValueUtil.isNotEmpty(agendaList)) {
			int size = agendaList.size();
			for (int i = 0; i < size; i++) {
				agendaUtil.setAgendaVisitaTrasnf((AgendaVisita) agendaList.items[i]);
				AgendaAtendimentoService.getInstance().transferirAgendaVisita(agendaUtil);
			}
		} else {
			agendaUtil.setAgendaVisitaTrasnf(agendaVisita);
			AgendaAtendimentoService.getInstance().transferirAgendaVisita(agendaUtil);
		}
		UiUtil.showInfoMessage(Messages.TRANSFERENCIA_SUCESSO);
		transferiuAgenda = true;
		fecharWindow();
	}
	
public class AgendaUtil {
	
	private AgendaVisita agendaVisitaTrasnf;
	private String dsMotivoAgenda, dsHoraAgenda, dsObs, cdSupervisor;
	private Date dtAgenda, dtLimiteReagendamento, dtAgendaA;
	
	public AgendaVisita getAgendaVisitaTrasnf() {
		return agendaVisitaTrasnf;
	}

	public void setAgendaVisitaTrasnf(AgendaVisita agendaVisitaTrasnf) {
		this.agendaVisitaTrasnf = agendaVisitaTrasnf;
	}

	public String getCdSupervisor() {
		return cdSupervisor;
	}

	public void setCdSupervisor(String cdSupervisor) {
		this.cdSupervisor = cdSupervisor;
	}

	public Date getDtAgendaA() {
		return dtAgendaA;
	}

	public void setDtAgendaA(Date dtAgendaA) {
		this.dtAgendaA = dtAgendaA;
	}

	public Date getDtLimiteReagendamento() {
		return dtLimiteReagendamento;
	}

	public void setDtLimiteReagendamento(Date dtLimiteReagendamento) {
		this.dtLimiteReagendamento = dtLimiteReagendamento;
	}

	public Date getDtAgenda() {
		return dtAgenda;
	}

	public void setDtAgenda(Date dtAgenda) {
		this.dtAgenda = dtAgenda;
	}

	public String getDsMotivoAgenda() {
		return dsMotivoAgenda;
	}

	public void setDsMotivoAgenda(String dsMotivoAgenda) {
		this.dsMotivoAgenda = dsMotivoAgenda;
	}

	public String getDsObs() {
		return dsObs;
	}

	public void setDsObs(String dsObs) {
		this.dsObs = dsObs;
	}

	public String getDsHoraAgenda() {
		return dsHoraAgenda;
	}

	public void setDsHoraAgenda(String dsHoraAgenda) {
		this.dsHoraAgenda = dsHoraAgenda;
	}
}
	
	
}
