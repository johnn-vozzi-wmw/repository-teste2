package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.MotRegistroVisitaService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotRegistroVisitaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.FechamentoDiarioUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadVisitaManualForm extends BaseCrudCadForm {

    private LabelContainer clienteContainer;
    private EditText edCdMotivoregistrovisita;
    public MotRegistroVisitaComboBox cbMotivo;
    private ButtonAction btFoto;

    private EditMemo edDsObservacao;

    public CadVisitaManualForm() throws SQLException {
        super(Messages.VISITA_LABEL_VISITA_MOTIVO);
        clienteContainer = new LabelContainer("");
        edCdMotivoregistrovisita = new EditText("@@@@@@@@@@", 20);
        edDsObservacao = new EditMemo("@@@@@@@@@@", 4, 254);
        cbMotivo = new MotRegistroVisitaComboBox();
        cbMotivo.load();
        btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
    }

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

    //@Override
    public void add() throws java.sql.SQLException {
    	super.add();
    	//--
        Visita visita = (Visita)getDomain();
        visita.cdEmpresa = SessionLavenderePda.cdEmpresa;
        visita.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	visita.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	visita.cdVisita = getCrudService().generateIdGlobal();
    	visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	visita.dtVisita = DateUtil.getCurrentDate();
    	visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.dtChegadaVisita = DateUtil.getCurrentDate();
    	visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.flVisitaManual = ValueUtil.VALOR_SIM;
    	if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
    		visita.flLiberadoSenha = SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada;
    	}
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        Visita visita = (Visita)getDomain();
        visita.cdMotivoRegistroVisita = cbMotivo.getValue();
    	visita.dsObservacao = edDsObservacao.getValue();
    	//--
    	MotRegistroVisita motRegistroVisita = new MotRegistroVisita();
    	motRegistroVisita.cdMotivoRegistroVisita = cbMotivo.getValue();
    	motRegistroVisita.cdEmpresa = visita.cdEmpresa;
    	String flVisitaPositivada = StringUtil.getStringValue(MotRegistroVisitaService.getInstance().findColumnByRowKey(motRegistroVisita.getRowKey(), "FLVISITAPOSITIVADA"));
    	if (!ValueUtil.VALOR_NI.equals(flVisitaPositivada)) {
        	visita.flVisitaPositivada = flVisitaPositivada;
    	}
        return visita;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        Visita visita = (Visita) domain;
        clienteContainer.setDescricao(ClienteService.getInstance().getDescriptionWithKey(visita.cdEmpresa, visita.cdRepresentante, visita.cdCliente));
		cbMotivo.setValue(visita.cdMotivoRegistroVisita);
		edDsObservacao.setValue(visita.dsObservacao);
    }
    
    //@Override
    public void edit(BaseDomain domain) throws java.sql.SQLException {
    	super.edit(domain);
    	Visita visita = (Visita) domain;
    	if (LavenderePdaConfig.usaVisitaFoto) {
			visita.setVisitaFotoList(VisitaFotoService.getInstance().findAllVisitaFotoByVisita(visita));
		}
    	if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
    		visita.flLiberadoSenha = SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada;
    	}
    }


    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        clienteContainer.setDescricao(SessionLavenderePda.getCliente().toString());
        edCdMotivoregistrovisita.setText("");
        edDsObservacao.setText("");
    }
    
    
    public void setEnabled(boolean enabled) {
        edDsObservacao.setEditable(enabled);
        cbMotivo.setEditable(enabled);
    }

    //@Override
    protected void visibleState() throws SQLException {
		cbMotivo.setEnabled(true);
		edDsObservacao.setEditable(true);
		btExcluir.setVisible(false);
    }

    protected void voltarClick() throws SQLException {
    	if (LavenderePdaConfig.usaVisitaFoto) {
    		Visita visita = (Visita) getDomain();
    		VisitaFotoService.getInstance().cancelaAlteracoesFotosFisicamente(visita);
    	}
		super.voltarClick();
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
        UiUtil.add(this, new LabelName(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA), cbMotivo, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - barBottomContainer.getHeight() - HEIGHT_GAP);
        if (LavenderePdaConfig.usaVisitaFoto) {
        	UiUtil.add(barBottomContainer, btFoto, 1);
        }
    }

	//@Override
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
	
	protected void salvarClick() throws SQLException {
		if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn())  return;
		
		if (LavenderePdaConfig.isUsaRegistroManualDeVisitaSemAgendaObrigatorio()) {
			if (ValueUtil.isEmpty(cbMotivo.getValue())) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
			}
		}
		Visita visita = (Visita) getDomain();
		visita.ignoraValidacaoObservacao = LavenderePdaConfig.isUsaRegistroManualDeVisitaSemAgendaNaoObrigatorio();
		for (int i = 0; i < visita.getVisitaFotoList().size(); i++) {
			VisitaFoto visitaFoto = (VisitaFoto) visita.getVisitaFotoList().items[i];
			VisitaFoto.setDadosAlteracao(visitaFoto);
		}

		if (LavenderePdaConfig.isUsaMotivoAgendaNaoPositivadaMultiplasEmpresas()) {
			Visita visitaAReplicar = (Visita) screenToDomain();
			AgendaVisitaService.getInstance().replicaVisitaNaoPositivadaEmTodasEmpresas(visitaAReplicar, cbMotivo.getValue());
		}
		super.salvarClick();
	}
	
}
