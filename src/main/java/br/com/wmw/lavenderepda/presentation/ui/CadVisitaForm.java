package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotRegistroVisitaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.FechamentoDiarioUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadVisitaForm extends BaseCrudCadForm {

    private EditText edFlTipoFrequencia;
    public MotRegistroVisitaComboBox cbMotivo;
    private EditDate edDtVisita;
    private EditText edHrVisita;
    private EditMemo edDsObservacao;
    private LabelContainer clienteContainer;
    private ButtonAction btFoto;
    
    private int tipoVisita;
    private boolean editingMotivo;

    public CadVisitaForm(int tipoVisita) throws SQLException {
        super("");
        this.tipoVisita = tipoVisita;
        edFlTipoFrequencia = new EditText("@@@@@@@@@@", 1);
        edDtVisita = new EditDate();
        edHrVisita = new EditText("@@@@@@", 5);
        edDsObservacao = new EditMemo("@@@@@@@@@@", 6, 254);
        cbMotivo = new MotRegistroVisitaComboBox();
        clienteContainer = new LabelContainer("");
        btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
        loadInfoByTipoVisita();
    }

    private void loadInfoByTipoVisita() throws SQLException {
		if (Visita.CD_VISITA_POSITIVADA == tipoVisita) {
			this.title = Messages.VISITA_MOTIVOPEDIDO_FORA_ORDEM;
			cbMotivo.load(Visita.FL_VISITA_POSITIVADA);
			btSalvar = new ButtonAction(Messages.BOTAO_NOVO_PEDIDO, "images/novopedido.png");
		} else if (Visita.CD_VISITA_NAO_POSITIVADA == tipoVisita) {
			this.title = Messages.VISITA_MOTIVOPEDIDO_NAO_REALIZADO;
			cbMotivo.load(Visita.FL_VISITA_NAOPOSITIVADA);
		} else if (Visita.CD_VISITA_TODOS == tipoVisita) {
			this.title = Messages.VISITA_LABEL_VISITA_MOTIVO;
			cbMotivo.load();
		}
    }

    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return VisitaService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Visita();
    }

    private Visita getVisita() throws SQLException {
    	return (Visita)getDomain();
    }

    //@Override
    public void edit(BaseDomain domain) throws java.sql.SQLException {
    	AgendaVisita agendaVisita = (AgendaVisita) domain;
    	Visita visita = getVisita();
    	if (Visita.CD_VISITA_NAO_POSITIVADA == tipoVisita) {
    		Visita visitaFilter = new Visita();
    		visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		visitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    		visitaFilter.cdCliente = agendaVisita.cdCliente;
    		visitaFilter.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
    		visitaFilter.dtVisita = agendaVisita.dtAgendaAtual;
    		visitaFilter.dtChegadaVisita = DateUtil.getCurrentDate();
    		visitaFilter.nuSequencia = agendaVisita.nuSequencia;
    		visitaFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
    		Vector visitas = VisitaService.getInstance().findAllByExample(visitaFilter);
    		if (visitas.size() > 0) {
    			editingMotivo = true;
    			visita = (Visita) visitas.items[0];
    		}
    	}
    	visita.cdCliente = agendaVisita.cdCliente;
    	visita.cdEmpresa = agendaVisita.cdEmpresa;
    	visita.cdRepresentante = agendaVisita.cdRepresentante;
    	visita.dsFrequenciaClienteVisita = agendaVisita.getDescricaoTipoFrequencia(agendaVisita.flTipoFrequencia);
    	visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	visita.dtVisita = agendaVisita.dtAgendaAtual;
    	visita.dtAgendaVisita = agendaVisita.dtAgendaAtual;
    	visita.dtChegadaVisita = DateUtil.getCurrentDate();
    	visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.nuSequencia = agendaVisita.nuSequencia;
    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
    		visita.cdTipoAgenda = agendaVisita.cdTipoAgenda;
    	}
    	if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
    		visita.cdRepOriginal = agendaVisita.cdRepOriginal;
    	}
    	super.edit(visita);
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        Visita visita = getVisita();
    	visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.cdMotivoRegistroVisita = cbMotivo.getValue();
    	if (Visita.CD_VISITA_NAO_POSITIVADA == tipoVisita) {
    		visita.flVisitaPositivada = Visita.FL_VISITA_NAOPOSITIVADA;
    	} else if (Visita.CD_VISITA_POSITIVADA == tipoVisita) {
    		visita.flVisitaPositivada = Visita.FL_VISITA_POSITIVADA;
    	} else {
    		MotRegistroVisita motRegistroVisita = (MotRegistroVisita) cbMotivo.getSelectedItem();
    		if (motRegistroVisita != null) {
    			visita.flVisitaPositivada = motRegistroVisita.flVisitaPositivada;
    		}
    	}
    	visita.dsObservacao = edDsObservacao.getValue();
        return visita;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        Visita visitaFilter = (Visita) domain;
        clienteContainer.setDescricao(ClienteService.getInstance().getDescriptionWithKey(visitaFilter.cdEmpresa, visitaFilter.cdRepresentante, visitaFilter.cdCliente));
        edFlTipoFrequencia.setValue(visitaFilter.dsFrequenciaClienteVisita);
        cbMotivo.setValue(visitaFilter.cdMotivoRegistroVisita);
        edDsObservacao.setValue(visitaFilter.dsObservacao);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edDtVisita.setText("");
        edHrVisita.setText("");
        edDsObservacao.setText("");
    }

    public void setEnabled(boolean enabled) {
        edDtVisita.setEditable(enabled);
        edHrVisita.setEditable(enabled);
        edDsObservacao.setEditable(enabled);
        cbMotivo.setEditable(enabled);
    }

    //@Override
    protected void visibleState() throws SQLException {
    	edFlTipoFrequencia.setEditable(false);
        edDtVisita.setEditable(false);
        edHrVisita.setEditable(false);
        btSalvar.setVisible(!editingMotivo);
        cbMotivo.setEnabled(!editingMotivo);
        edDsObservacao.setEnabled(!editingMotivo);
        //--
        boolean isVisibleBtExcluir = editingMotivo && !getDomain().isEnviadoServidor() && getVisita().isVisitaEnviadaServidor();
        btExcluir.setVisible(isVisibleBtExcluir);
    }

	//@Override
    protected void onFormStart() throws SQLException {
    	if (tipoVisita == Visita.CD_VISITA_POSITIVADA) {
    		UiUtil.add(barBottomContainer, btSalvar, 5);
    	}
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	if (!LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
    		UiUtil.add(this, new LabelName(Messages.AGENDAVISITA_LABEL_FLTIPOFREQUENCIA), edFlTipoFrequencia, getLeft(), AFTER + HEIGHT_GAP);
    	}
        UiUtil.add(this, new LabelName(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA), cbMotivo, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - barBottomContainer.getHeight() - HEIGHT_GAP);
        if (LavenderePdaConfig.usaVisitaFoto) {
        	UiUtil.add(barBottomContainer, btFoto, 1);
        }
    }

    //@Override
    protected void salvarClick() throws SQLException {
    	if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) return;
    	
    	if (tipoVisita == Visita.CD_VISITA_NAO_POSITIVADA || Visita.CD_VISITA_TODOS == tipoVisita) {
    		insertOrUpdateVisita();
    		enviarDadosBackground((Visita) getDomain());
    		if (LavenderePdaConfig.isUsaMotivoAgendaNaoPositivadaMultiplasEmpresas()) {
    			replicaVisitaNaoPositivadaEmTodasEmpresas();
    		}
    		close();
    	} else {
    		if (ValueUtil.isEmpty(cbMotivo.getValue())) {
    			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
    		}
    		close();
    		Visita visita = (Visita) getDomain();
    		visita.cdMotivoRegistroVisita = cbMotivo.getValue();
    		visita.dsObservacao = edDsObservacao.getValue();
    		((CadClienteMenuForm)getBaseCrudCadMenuForm()).setVisitaPositivadaTemp(visita);
    		((CadClienteMenuForm)getBaseCrudCadMenuForm()).novoPedido();
    	}
    }

	private void replicaVisitaNaoPositivadaEmTodasEmpresas() throws SQLException {
		Visita visita = (Visita) screenToDomain();
		Vector visitasReplicadasList = AgendaVisitaService.getInstance().replicaVisitaNaoPositivadaEmTodasEmpresas(visita, cbMotivo.getValue());
		int sizeVisitasReplicadas = visitasReplicadasList.size();
		for (int i = 0; i < sizeVisitasReplicadas; i++) {
			enviarDadosBackground((Visita) visitasReplicadasList.items[i]);
		}
	}

    private void insertOrUpdateVisita() throws SQLException {
    	Visita visita = (Visita)screenToDomain();
    	if (!editingMotivo) {
    		if (ValueUtil.isEmpty(visita.cdVisita)) {
    			visita.cdVisita = VisitaService.getInstance().generateIdGlobal();
    			visita.cdEmpresa = SessionLavenderePda.getCliente().cdEmpresa;
    			visita.cdRepresentante = SessionLavenderePda.getCliente().cdRepresentante;
    			visita.flVisitaManual = ValueUtil.VALOR_SIM;
    		}
    		getCrudService().insert(visita);
    	} else {
    		((VisitaService)getCrudService()).updateByMotivoNaoPositivado(screenToDomain());
    	}
    }

    protected void enviarDadosBackground(Visita visita) throws SQLException {
    	VisitaService.getInstance().enviaDadosBackgroundVisita(visita);
    }

    protected void voltarClick() throws SQLException {
    	if (LavenderePdaConfig.usaVisitaFoto) {
    		Visita visita = (Visita) getDomain();
    		VisitaFotoService.getInstance().cancelaAlteracoesFotosFisicamente(visita);
    	}
    	super.voltarClick();
    	if ((LavenderePdaConfig.usaEscolhaEmpresaPedido) && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld)) {
			EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
			SessionLavenderePda.cdEmpresaOld = null;
		}
    }
    
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFoto) {
					btFotoClick();
				}
				break;
			}
		}
	}
	
	private void btFotoClick() throws SQLException {
		Visita visita = (Visita) getDomain();
		CadVisitaFotoWindow cadVisitaFotoForm = new CadVisitaFotoWindow(visita);
		cadVisitaFotoForm.popup();
	}
	
}