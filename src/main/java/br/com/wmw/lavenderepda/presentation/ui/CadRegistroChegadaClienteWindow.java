package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ForceColetaGpsService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.StatusGpsService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotRegistroVisitaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.FechamentoDiarioUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.sync.async.SincronizacaoApp2WebRunnable;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.util.Date;

public class CadRegistroChegadaClienteWindow extends WmwWindow {

	private LabelName lbData;
	private LabelName lbHora;
	private LabelName lbObs;
	private LabelName lbMotivo;
	private LabelValue vlData;
	private LabelValue vlHora;
	private LabelValue lbRegistroJaSalvo;
	private LabelValue lbObrigaRegistroChegada;
	private EditMemo edObs;
	private ButtonPopup btRegistrarAtualizarChegada;
	private ButtonPopup btFoto;
	private Cliente cliente;
	public Visita visita;
	public MotRegistroVisitaComboBox cbMotivo;
	public AgendaVisita agendaVisita;
	public boolean isVisitaAndamento;
	public boolean registrouChegada;
	public Date dtAgendaAtual;


	public CadRegistroChegadaClienteWindow(Cliente clienteSelecionado, AgendaVisita agendaVisita, Visita visitaEmAndamento, Date dtAgendaAtual, boolean isFlAgendada) throws SQLException {
		this(clienteSelecionado, agendaVisita, visitaEmAndamento, dtAgendaAtual);
		if (isFlAgendada && !LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			visita.flVisitaAgendada = ValueUtil.VALOR_SIM;
		}
	}
	
	public CadRegistroChegadaClienteWindow(Cliente clienteSelecionado, AgendaVisita agendaVisita, Visita visitaEmAndamento, Date dtAgendaAtual) throws SQLException {
		super(clienteSelecionado.isNovoCliente() ? Messages.REGISTRAR_CHEGADA_NOVO_CLIENTE_LABEL : Messages.REGISTRAR_CHEGADA_CLIENTE_LABEL);
		this.cliente = clienteSelecionado;
		this.agendaVisita = agendaVisita;
		this.dtAgendaAtual = dtAgendaAtual;
		isVisitaAndamento = visitaEmAndamento != null;
		this.visita = isVisitaAndamento ? visitaEmAndamento : new Visita();
		if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
			this.visita.flLiberadoSenha = SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada;
		}
		lbData = new LabelName(Messages.DATA_LABEL_DATA);
		lbHora = new LabelName(Messages.DATA_LABEL_HORA);
		lbObs = new LabelName(Messages.OBSERVACAO_LABEL);
		lbMotivo = new LabelName(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
		lbRegistroJaSalvo = new LabelValue(cliente.isNovoCliente() ? Messages.REGISTRAR_CHEGADA_NOVO_CLIENTE_AVISO_CHEGADA_REGISTRADA : Messages.REGISTRAR_CHEGADA_CLIENTE_AVISO_CHEGADA_REGISTRADA);
    	lbRegistroJaSalvo.setFont(Font.getFont(true, lbRegistroJaSalvo.getFont().size));
		edObs = new EditMemo("@@@", 6, 255).setID("edObs");
		vlData = new LabelValue(StringUtil.getStringValue(DateUtil.getCurrentDate()));
		vlData.setID("vlData");
		vlHora = new LabelValue(StringUtil.getStringValue(TimeUtil.getCurrentTimeHHMMSS()));
		vlHora.setID("vlHora");
		btRegistrarAtualizarChegada = new ButtonPopup(isVisitaAndamento ? Messages.REGISTRAR_CHEGADA_CLIENTE_BOTAO_ATUALIZAR : Messages.REGISTRAR_CHEGADA_CLIENTE_BOTAO_REGISTRAR).setID("btRegistrarAtualizarChegada");
		btFoto = new ButtonPopup(Messages.LABEL_FOTO);
		lbObrigaRegistroChegada = new LabelValue(" - ", CENTER);
		lbObrigaRegistroChegada.autoMultipleLines = true;
		cbMotivo = new MotRegistroVisitaComboBox().setID("cbMotivo");
		if (!LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia) {
			cbMotivo.load(ValueUtil.VALOR_SIM);
		}
		setDefaultRect();
		domainToScreen();
	}

	private void domainToScreen() {
		if (ValueUtil.isNotEmpty(visita.cdMotivoRegistroVisita)) {
			cbMotivo.setValue(visita.cdMotivoRegistroVisita);
		}
		if (isVisitaAndamento) {
			vlData.setValue(visita.dtChegadaVisita);
			vlHora.setValue(visita.hrChegadaVisita);
			edObs.setValue(visita.dsObservacaoChegada);
		}
	}
	
	public void initUI() {
		super.initUI();
		if (LavenderePdaConfig.isBloqueiaNovoPedidoClienteSemRegistroChegada()) {
			UiUtil.add(this, lbObrigaRegistroChegada, getLeft(), getNextY(), FILL, PREFERRED);
			lbObrigaRegistroChegada.setText(Messages.MSG_OBRIGATORIO_REGISTRO_CHEGADA_CLIENTE);
		}
		UiUtil.add(this, lbData, vlData, getLeft(), getNextY());
		if (!LavenderePdaConfig.ocultaHoraRegistroChegadaSaida) {
			UiUtil.add(this, lbHora, vlHora, SAME, AFTER + HEIGHT_GAP);
		}
		if (!LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia && agendaVisita != null && isAgendaVisitaForaSequencia()) {
			UiUtil.add(this, lbMotivo, cbMotivo, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (isVisitaAndamento) {
			UiUtil.add(this, lbRegistroJaSalvo, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(this, lbObs, edObs, SAME, AFTER + HEIGHT_GAP, getWFill(), FILL - HEIGHT_GAP_BIG);
		if (edObs.getHeight() < UiUtil.getControlPreferredHeight() * 4) {
			edObs.resetSetPositions();
			edObs.setRect(edObs.getX(), edObs.getY(), getWFill(), UiUtil.getControlPreferredHeight() * 3);
		}
		//--
		addButtonPopup(btRegistrarAtualizarChegada);
		if (LavenderePdaConfig.usaVisitaFoto) {
			addButtonPopup(btFoto);
		}
		addButtonPopup(btFechar);
	}

	//@Override
	protected void addBtFechar() {
	}

	public void onWindowEvent(final Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRegistrarAtualizarChegada) {
					if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
						return;
					}
					if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) {
						return;
					}
					btRegistrarChegadaClick();
				} else if (event.target == btFoto) {
					btFotoClick();
				}
				break;
			}
		}
	}

	public void btRegistrarChegadaClick() throws SQLException {
		String dsObservacaoChegada =  edObs.getValue().trim().replaceAll("_+$", "$1");
		if (!isVisitaAndamento) {
			visita.cdVisita = ValueUtil.isNotEmpty(visita.cdVisita) ? visita.cdVisita : VisitaService.getInstance().generateIdGlobal();
			visita.cdEmpresa = SessionLavenderePda.cdEmpresa;
			visita.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
			visita.dsObservacaoChegada = dsObservacaoChegada;
			visita.cdCliente = cliente.cdCliente;
			visita.nuSequencia = agendaVisita.nuSequencia;
			visita.dtVisita = DateUtil.getCurrentDate();
			visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
			visita.dtChegadaVisita = DateUtil.getCurrentDate();
			visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
			visita.cdTipoAgenda = agendaVisita.cdTipoAgenda;
			visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			visita.dtAgendaVisita = dtAgendaAtual;
			if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
	    		visita.cdRepOriginal = agendaVisita.cdRepOriginal;
	    	}
			if (!LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia) {
				visita.cdMotivoRegistroVisita = cbMotivo.getValue();
			}
			if (LavenderePdaConfig.isPositivaVisitaAutomaticamente() || LavenderePdaConfig.isPositivaVisitaAutomaticamenteECarregaMotivoVisitaPositiva()) {
				visita.flVisitaPositivada = ValueUtil.VALOR_SIM;
			}
	    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
	    		visita.cdTipoAgenda = agendaVisita.cdTipoAgenda;
	    	}
			if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
				ForceColetaGpsService.getInstance().start();
			}
			if (LavenderePdaConfig.isUsaColetaGpsAppExterno() && ! StatusGpsService.getInstance().isWGPSActiveOnDevice()) {
				PontoGpsService.getInstance().initializeWgps();
			}
			if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
				visita.cdChaveAgendaOrigem = agendaVisita.getRowKey().contains("null") ? null : agendaVisita.getRowKey();
			}
			validateRegistroChegada();
			VisitaService.getInstance().insert(visita, SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado());
			registrouChegada = true;
		} else {
			visita.dsObservacaoChegada = dsObservacaoChegada;
			if (!LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia) {
				visita.cdMotivoRegistroVisita = cbMotivo.getValue();
			}
			validateRegistroChegada();
			VisitaService.getInstance().update(visita, SincronizacaoApp2WebRunnable.getInstance().isSyncAutomaticoLigado());
			registrouChegada = true;
		}
		SessionLavenderePda.visitaAndamento = visita;
		unpop();
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		super.btFecharClick();
		if (!registrouChegada && isVisitaAndamento) {
			VisitaFotoService.getInstance().cancelaAlteracoesFotosFisicamente(visita);
		}
	}

	private void btFotoClick() {
		CadVisitaFotoWindow visitaFoto = new CadVisitaFotoWindow(visita, true);
		visitaFoto.popup();
		visita = visitaFoto.visita;
	}

	private void validateRegistroChegada() {
		if (!LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia && ValueUtil.isEmpty(visita.cdMotivoRegistroVisita) && isAgendaVisitaForaSequencia()) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
		}
		
		if (LavenderePdaConfig.isValorMinCaractersParaObservacaoMotivoVisita()) {
			if (ValueUtil.isEmpty(visita.dsObservacaoChegada) || visita.dsObservacaoChegada.length() < LavenderePdaConfig.nuMinCaracterObservacaoMotivoVisita) {
				throw new ValidationException(MessageUtil.getMessage(Messages.VISITA_MSG_QTDADE_MIN_OBS, new String[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.nuMinCaracterObservacaoMotivoVisita)}));
			}
		}
	}
	
	private boolean isAgendaVisitaForaSequencia() {
		if (agendaVisita != null) {
			StringBuilder rowkeyAgendaVisitaSelecionada = new StringBuilder(); 
			if (ValueUtil.isNotEmpty(agendaVisita.rowKey)) {
				rowkeyAgendaVisitaSelecionada.append(agendaVisita.rowKey);
			} else {
				rowkeyAgendaVisitaSelecionada.append(agendaVisita.toString()).append(";");
			}
			return !rowkeyAgendaVisitaSelecionada.toString().equals(agendaVisita.rowkeyDaProximaSequencia);
		}
		return false;
	}

}
