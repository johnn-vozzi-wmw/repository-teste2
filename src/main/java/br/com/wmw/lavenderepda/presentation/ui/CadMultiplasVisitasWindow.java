package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.ForceColetaGpsService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.StatusGpsService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotRegistroVisitaComboBox;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class CadMultiplasVisitasWindow extends WmwCadWindow {
	
    private MotRegistroVisitaComboBox cbMotivo;
    private EditMemo edDsObservacao;
    
	private Date dtAgendaAtual;
	private LabelValue lvDtAgendaAtual;
	private Vector agendaVisitaList;
	public boolean registroSalvo;

	public CadMultiplasVisitasWindow(Vector agendaVisitaList, Date dtAgendaAtual) throws SQLException {
		super("");
		cloneDataAgendaAtual(dtAgendaAtual);
		lvDtAgendaAtual = new LabelValue(StringUtil.getStringValue(dtAgendaAtual));
		this.agendaVisitaList = agendaVisitaList;
        edDsObservacao = new EditMemo("@@@@@@@@@@", 6, 254);
        cbMotivo = new MotRegistroVisitaComboBox();
        btSalvar.setText(FrameworkMessages.BOTAO_SALVAR);
		loadInfoByTipoVisita();
		setDefaultRect();
	}

	private void cloneDataAgendaAtual(Date dtAgendaAtual) {
		try {
			this.dtAgendaAtual = new Date(dtAgendaAtual.getDay(), dtAgendaAtual.getMonth(), dtAgendaAtual.getYear());
		} catch (InvalidDateException e) {
			ExceptionUtil.handle(e);
			this.dtAgendaAtual = dtAgendaAtual;
		}
	}
	
	private void loadInfoByTipoVisita() throws SQLException {
		this.title = Messages.VISITA_MOTIVOPEDIDO_NAO_REALIZADO;
		cbMotivo.load(Visita.FL_VISITA_NAOPOSITIVADA);
    }
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.DATA_LABEL_DATA), lvDtAgendaAtual, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA), cbMotivo, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - HEIGHT_GAP_BIG);
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		Visita visita = getVisita();
    	visita.cdMotivoRegistroVisita = cbMotivo.getValue();
    	visita.flVisitaPositivada = Visita.FL_VISITA_NAOPOSITIVADA;
    	if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
    		visita.dsObservacaoChegada = edDsObservacao.getValue();
    		visita.dsObservacaoSaida = edDsObservacao.getValue();
    	} else {
    		visita.dsObservacao = edDsObservacao.getValue();
    	}
    	visita.flVisitaManual = ValueUtil.VALOR_SIM;
        return visita;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		Visita visitaFilter = (Visita) domain;
        cbMotivo.setValue(visitaFilter.cdMotivoRegistroVisita);
        edDsObservacao.setValue(visitaFilter.dsObservacao);
	}
	
	@Override
	public void edit(BaseDomain domain) throws java.sql.SQLException {
		super.edit(domain);
	}

	@Override
	protected void clearScreen() throws java.sql.SQLException {
        edDsObservacao.setText("");
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new Visita();
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return VisitaService.getInstance();
	}
	
	private Visita getVisita() throws SQLException {
    	return (Visita)getDomain();
    }
	
	@Override
	protected void visibleState() throws java.sql.SQLException {
		super.visibleState();
	}
	
	@Override
	protected void salvarClick() throws SQLException {
		if (ValueUtil.isNotEmpty(agendaVisitaList)) {
			if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.VISITA_MSG_CONFIRMA_REGISTRO_MULTIPLAS_VISITAS, dtAgendaAtual))) {
				Vector visitaList = new Vector();
				UiUtil.showProcessingMessage();
				try {
					updateVisitaEmAndamento(visitaList);
					for (int i = 0; i < agendaVisitaList.size(); i++) {
						AgendaVisita agendaVisita = (AgendaVisita) agendaVisitaList.items[i];
						Visita visita = (Visita)screenToDomain();
						int cdVisita = ValueUtil.getIntegerValue(VisitaService.getInstance().generateIdGlobal());
						visita.cdVisita = StringUtil.getStringValue(cdVisita);
						visita.cdCliente = agendaVisita.cdCliente;
						visita.cdEmpresa = agendaVisita.cdEmpresa;
						visita.cdRepresentante = agendaVisita.cdRepresentante;
						visita.dsFrequenciaClienteVisita = agendaVisita.getDescricaoTipoFrequencia(agendaVisita.flTipoFrequencia);
						visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
						visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
						visita.dtVisita = new Date(dtAgendaAtual.getDay(), dtAgendaAtual.getMonth(), dtAgendaAtual.getYear());
						visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
						visita.dtChegadaVisita = DateUtil.getCurrentDate();
						visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
						if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
							visita.dtSaidaVisita = DateUtil.getCurrentDate();
							visita.hrSaidaVisita = TimeUtil.getCurrentTimeHHMMSS();
						}
						visita.nuSequencia = agendaVisita.nuSequencia;
						visita.dtAgendaVisita = new Date(dtAgendaAtual.getDay(), dtAgendaAtual.getMonth(), dtAgendaAtual.getYear());
				    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
				    		visita.cdTipoAgenda = agendaVisita.cdTipoAgenda;
				    	}
						getCrudService().insert(visita);
			    		if (LavenderePdaConfig.isUsaMotivoAgendaNaoPositivadaMultiplasEmpresas()) {
			    			Vector visitasReplicadasList = AgendaVisitaService.getInstance().replicaVisitaNaoPositivadaEmTodasEmpresas(visita, cbMotivo.getValue());
			    			if (ValueUtil.isNotEmpty(visitasReplicadasList)) {
			    				visitaList.addElements(visitasReplicadasList.toObjectArray());
			    			}
			    		}
						visitaList.addElement((Visita)visita.clone());
					}
				
					afterSaveAll(visitaList);
					registroSalvo = true;
					fecharWindow();
				} catch (Throwable ex) {
					UiUtil.showErrorMessage(ex);
				} finally {
					UiUtil.unpopProcessingMessage();
				}
			}
		}
	}
	
	private void updateVisitaEmAndamento(Vector visitaList) throws SQLException {
		if (SessionLavenderePda.visitaAndamento != null) {
			Vector agendaVisitaNaoAndamentoList = new Vector();
			boolean visitaDaAgenda;
			for (int i = 0; i < agendaVisitaList.size(); i++) {
				AgendaVisita agendaVisita = (AgendaVisita) agendaVisitaList.items[i];
				if (SessionLavenderePda.visitaAndamento == null) {
					visitaDaAgenda = false;
				} else {
					visitaDaAgenda = agendaVisita.cdCliente.equals(SessionLavenderePda.visitaAndamento.cdCliente) && agendaVisita.cdEmpresa.equals(SessionLavenderePda.visitaAndamento.cdEmpresa) && agendaVisita.cdRepresentante.equals(SessionLavenderePda.visitaAndamento.cdRepresentante);
				}
				if (visitaDaAgenda) {
					if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
						SessionLavenderePda.visitaAndamento.dtSaidaVisita = DateUtil.getCurrentDate();
						SessionLavenderePda.visitaAndamento.hrSaidaVisita = TimeUtil.getCurrentTimeHHMMSS();
					}
					SessionLavenderePda.visitaAndamento.flVisitaPositivada = Visita.FL_VISITA_NAOPOSITIVADA;
					SessionLavenderePda.visitaAndamento.nuSequencia = agendaVisita.nuSequencia;
					String nmRazaoSocial = ClienteService.getInstance().getNmRazaoSocial(SessionLavenderePda.visitaAndamento.cdEmpresa, SessionLavenderePda.visitaAndamento.cdRepresentante, SessionLavenderePda.visitaAndamento.cdCliente);
					if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.VISITA_MSG_FECHAR_VISITA_EM_ANDAMENTO, SessionLavenderePda.visitaAndamento.cdCliente + " - " + nmRazaoSocial))) {
						getCrudService().update(SessionLavenderePda.visitaAndamento);
						SessionLavenderePda.visitaAndamento = null;
						agendaVisitaNaoAndamentoList.insertElementAt(agendaVisita, i);
					}
				} else {
					agendaVisitaNaoAndamentoList.insertElementAt(agendaVisita, i);
				}
			}
			agendaVisitaList = agendaVisitaNaoAndamentoList;
		}
	}
	
	protected void afterSaveAll(Vector visitaList) throws SQLException {
		if (ValueUtil.isNotEmpty(visitaList)) {
			if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
					ForceColetaGpsService.getInstance().start();
				}
				if (LavenderePdaConfig.isUsaColetaGpsAppExterno() && ! StatusGpsService.getInstance().isWGPSActiveOnDevice()) {
					PontoGpsService.getInstance().initializeWgps();
				}
			} else {
				if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema()) {
					PontoGpsService.getInstance().startColetaGpsPontoEspecificoSistema();
				}
			}
			for (int i = 0; i < visitaList.size(); i++) {
				Visita visita = (Visita) visitaList.items[i];
				if (LavenderePdaConfig.enviaInformacoesVisitaOnline && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
					String cdSessao = PedidoService.getInstance().generateIdGlobal();
					EnviaDadosThread.getInstance().enviaVisita(cdSessao, visita);
				}
				VisitaService.getInstance().fechaVisita(visita, false);
			}
		}
	}
	
	@Override
	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		if ((LavenderePdaConfig.usaEscolhaEmpresaPedido) && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld)) {
			EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
			SessionLavenderePda.cdEmpresaOld = null;
		}
	}

}