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
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ForceColetaGpsService;
import br.com.wmw.lavenderepda.business.service.MotRegistroVisitaService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.StatusGpsService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaPedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotRegistroVisitaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.util.Vector;

public class CadRegistroSaidaClienteWindow extends WmwWindow {

	private LabelName lbData;
	private LabelName lbHora;
	private LabelName lbObs;
	private LabelName lbMotivo;
	private LabelValue vlData;
	private LabelValue vlHora;
	private LabelValue lbRegistroJaSalvo;
	private EditMemo edObs;
	private ButtonPopup btRegistrarAtualizarSaida;
	private ButtonPopup btFoto;
	private Visita visita;
	public MotRegistroVisitaComboBox cbMotivo;
	public boolean registrouSaida;
	public boolean isEdicaoRegistroSaida;
	public boolean visitaPositivada;
	public boolean registraAutomatico;
	
	public Vector visitasReplicadasList;

	public CadRegistroSaidaClienteWindow(Visita visita, boolean registraAutomatico, boolean novoCliente) throws SQLException {
		super(novoCliente ? Messages.REGISTRAR_SAIDA_NOVO_CLIENTE_LABEL : Messages.REGISTRAR_SAIDA_CLIENTE_LABEL);
		this.visita = visita;
		this.registraAutomatico = registraAutomatico;
		isEdicaoRegistroSaida = ValueUtil.isNotEmpty(visita.hrSaidaVisita);
		if (LavenderePdaConfig.usaVisitaFoto) {
			this.visita.setVisitaFotoList(VisitaFotoService.getInstance().findAllVisitaFotoByVisita(visita));
		}
		this.visitaPositivada = ValueUtil.getBooleanValue(visita.flVisitaPositivada);
		lbData = new LabelName(Messages.DATA_LABEL_DATA);
		lbHora = new LabelName(Messages.DATA_LABEL_HORA);
		lbObs = new LabelName(Messages.OBSERVACAO_LABEL);
		lbMotivo = new LabelName(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
        edObs = new EditMemo("@@@", 6, 255).setID("edObs");
        lbRegistroJaSalvo = new LabelValue(novoCliente ? Messages.REGISTRAR_CHEGADA_NOVO_CLIENTE_AVISO_SAIDA_REGISTRADA : Messages.REGISTRAR_CHEGADA_CLIENTE_AVISO_SAIDA_REGISTRADA);
    	lbRegistroJaSalvo.setFont(Font.getFont(true, lbRegistroJaSalvo.getFont().size));
        vlData = new LabelValue(StringUtil.getStringValue(DateUtil.getCurrentDate()));
        vlData.setID("vlData");
        vlHora = new LabelValue(StringUtil.getStringValue(TimeUtil.getCurrentTimeHHMMSS()));
        vlHora.setID("vlHora");
        btRegistrarAtualizarSaida = new ButtonPopup(isEdicaoRegistroSaida ? Messages.REGISTRAR_CHEGADA_CLIENTE_BOTAO_ATUALIZAR : Messages.REGISTRAR_CHEGADA_CLIENTE_BOTAO_REGISTRAR).setID("btRegistrarAtualizarSaida");
        btFoto = new ButtonPopup(Messages.LABEL_FOTO);
        cbMotivo = new MotRegistroVisitaComboBox().setID("cbMotivo");
        if (LavenderePdaConfig.isPositivaVisitaAutomaticamenteECarregaMotivoVisitaPositiva()) {
			cbMotivo.load(ValueUtil.VALOR_SIM);
		} else {
			cbMotivo.loadToRegistroSaidaCliente();
		}
        visitasReplicadasList = new Vector();
        setDefaultRect();
        domainToScreen();
	}

	private void domainToScreen() {
		if (ValueUtil.isNotEmpty(visita.cdMotivoRegistroVisita)) {
			cbMotivo.setValue(visita.cdMotivoRegistroVisita);
		}
		if (isEdicaoRegistroSaida) {
			vlData.setValue(visita.dtSaidaVisita);
			vlHora.setValue(visita.hrSaidaVisita);
			edObs.setValue(visita.dsObservacaoSaida);
		}
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			UiUtil.add(this, lbData, vlData, getLeft(), getNextY());
			if (!LavenderePdaConfig.ocultaHoraRegistroChegadaSaida) {
				UiUtil.add(this, lbHora, vlHora, SAME, getNextY());
			}
			if (LavenderePdaConfig.usaPositivacaoAgendaVisitaSemPedido() || !isVisitaPositivada()) {
				UiUtil.add(this, lbMotivo, cbMotivo, getLeft(), getNextY());
			}
			if (isEdicaoRegistroSaida) {
				UiUtil.add(this, lbRegistroJaSalvo, getLeft(), getNextY());
			}
			UiUtil.add(this, lbObs, edObs, getLeft(), getNextY());
			if (edObs.getHeight() < UiUtil.getControlPreferredHeight() * 7) {
				edObs.resetSetPositions();
				edObs.setRect(edObs.getX(), edObs.getY(), getWFill(), UiUtil.getControlPreferredHeight() * 5);
			} else {
				edObs.setRect(edObs.getX(), edObs.getY(), getWFill(), FILL - HEIGHT_GAP_BIG);
			}
			addButtonPopup(btRegistrarAtualizarSaida);
			if (LavenderePdaConfig.usaVisitaFoto) {
				addButtonPopup(btFoto);
			}
			addButtonPopup(btFechar);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private boolean isVisitaPositivada() throws SQLException {
		return VisitaPedidoService.getInstance().isVisitaPossuiPedido(visita) && visita.isVisitaPositivada();
	}
	
	public void onWindowEvent(final Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRegistrarAtualizarSaida) {
					if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
						return;
					}
					btRegistrarSaidaClick();
				} else if (event.target == btFoto) {
					btFotoClick();
				}
				break;
			}
		}
	} 

	private void btRegistrarSaidaClick() throws SQLException {
		if (visita != null) {
			visita.dsObservacaoSaida = edObs.getValue().trim().replaceAll("_+$", "$1");
			visita.cdMotivoRegistroVisita = ValueUtil.isEmpty(cbMotivo.getValue()) && isVisitaPositivada() ? visita.cdMotivoRegistroVisita : cbMotivo.getValue();
			MotRegistroVisita motRegistroVisita = new MotRegistroVisita();
	    	motRegistroVisita.cdMotivoRegistroVisita = cbMotivo.getValue();
	    	motRegistroVisita.cdEmpresa = visita.cdEmpresa;
	    	String flVisitaPositivada = ValueUtil.VALOR_NI;
	    	if (ValueUtil.isNotEmpty(motRegistroVisita.cdMotivoRegistroVisita)) {
	    		flVisitaPositivada = StringUtil.getStringValue(MotRegistroVisitaService.getInstance().findColumnByRowKey(motRegistroVisita.getRowKey(), "FLVISITAPOSITIVADA"));
	    	}
	    	if (!ValueUtil.VALOR_NI.equals(flVisitaPositivada)) {
	        	visita.flVisitaPositivada = flVisitaPositivada;
	    	}
	    	validateRegistrarSaida();
			if (!isEdicaoRegistroSaida) {
				visita.dtSaidaVisita = DateUtil.getCurrentDate();
				visita.hrSaidaVisita = TimeUtil.getCurrentTimeHHMMSS();
			}
	    	visita.flVisitaManual = ValueUtil.VALOR_SIM;
			VisitaService.getInstance().update(visita);
			if (LavenderePdaConfig.isUsaMotivoAgendaNaoPositivadaMultiplasEmpresas()) {
				visitasReplicadasList = AgendaVisitaService.getInstance().replicaVisitaNaoPositivadaEmTodasEmpresas(visita, cbMotivo.getValue());
			}
			registrouSaida = true;
			if (!isEdicaoRegistroSaida) {
				if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
					ForceColetaGpsService.getInstance().start();
				}
				if (LavenderePdaConfig.isUsaColetaGpsAppExterno() && ! StatusGpsService.getInstance().isWGPSActiveOnDevice()) {
					PontoGpsService.getInstance().initializeWgps();
				}
			}
		}
		SessionLavenderePda.visitaAndamento = null;
		unpop();
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		super.btFecharClick();
		if (!registrouSaida && visita != null) {
			VisitaFotoService.getInstance().cancelaAlteracoesFotosFisicamente(visita);
		}
	}

	private void btFotoClick() {
		CadVisitaFotoWindow visitaFoto = new CadVisitaFotoWindow(visita, true);
		visitaFoto.popup();
	}

	protected void onPopup() {
		super.onPopup();
		registrouSaida = false;
	}
	
	private void validateRegistrarSaida() {
		if (!Visita.FL_VISITA_POSITIVADA.equals(visita.flVisitaPositivada) || (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && !registraAutomatico && LavenderePdaConfig.usaPositivacaoAgendaVisitaSemPedido())) {
	        if (ValueUtil.isEmpty(visita.cdMotivoRegistroVisita)) {
	            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
	        }
        }
		if (LavenderePdaConfig.isValorMinCaractersParaObservacaoMotivoVisita() && !registraAutomatico) {
			if (ValueUtil.isEmpty(visita.dsObservacaoSaida) || visita.dsObservacaoSaida.length() < LavenderePdaConfig.nuMinCaracterObservacaoMotivoVisita) {
				throw new ValidationException(MessageUtil.getMessage(Messages.VISITA_MSG_QTDADE_MIN_OBS, new String[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.nuMinCaracterObservacaoMotivoVisita)}));
			}
		}
	}

	@Override
	public void popup() {
		if (registraAutomatico) try {
			btRegistrarSaidaClick();
		} catch (Throwable ee) {ee.printStackTrace();} else {
			super.popup();
		}
	}

}
