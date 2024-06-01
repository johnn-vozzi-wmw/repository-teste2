package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditTime;
import br.com.wmw.framework.presentation.ui.ext.EditTimeMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.VisitaSupervisor;
import br.com.wmw.lavenderepda.business.service.VisitaSupervisorService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadVisitaSupervisorForm extends BaseCrudCadForm {

	private RepresentanteSupervComboBox cbRepresentantes;
	private ClienteComboBox cbClientes;
    private EditDate edDtVisita;
    private EditTime edHrVisita;
    private EditMemo edDsObservacao;
    private EditTimeMask edQtTempoAtendimento;
    private CheckBoolean chFlRepresentantePresente;
    
    public static final String MASK_FORMATO_HMM_VALUE = "__:__";

    public CadVisitaSupervisorForm() throws SQLException {
        super(Messages.VISITA_TITULO_CADASTRO);
        cbRepresentantes = new RepresentanteSupervComboBox();
        cbClientes = new ClienteComboBox(Messages.CLIENTE_NOME_ENTIDADE);
        edDtVisita = new EditDate();
        edHrVisita = new EditTime(EditTime.FORMATO_HHMM);
        edDsObservacao = new EditMemo("@@@@@@@@@@", 4, 255);
        edQtTempoAtendimento = new EditTimeMask(EditTimeMask.FORMATO_HHMM);
        chFlRepresentantePresente = new CheckBoolean();
    }

    //@Override
    protected String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return VisitaSupervisorService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new VisitaSupervisor();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        VisitaSupervisor visita = ((VisitaSupervisor) getDomain());
        visita.cdRepresentante = cbRepresentantes.getValue();
        visita.cdCliente = cbClientes.getValue();
        visita.dtVisita = edDtVisita.getValue();
        visita.hrVisita = edHrVisita.getValue();
        visita.dsObservacao = edDsObservacao.getValue();
        if(!edQtTempoAtendimento.getText().equalsIgnoreCase(MASK_FORMATO_HMM_VALUE)) {
        	visita.qtTempoAtendimento = edQtTempoAtendimento.getText();
        }
        visita.flRepresentantePresente = StringUtil.getStringValue(chFlRepresentantePresente.getValue());
        return visita;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        VisitaSupervisor visita = (VisitaSupervisor) domain;
        cbRepresentantes.setValue(visita.cdRepresentante);
        if (isEditing()) {
        	cbClientes.recarregaClientes();
        }
        cbClientes.setValue(visita.cdCliente);
        edDtVisita.setValue(visita.dtVisita);
        edHrVisita.setValue(visita.hrVisita);
        edDsObservacao.setValue(visita.dsObservacao);
		if (ValueUtil.isNotEmpty(visita.qtTempoAtendimento)) {
			edQtTempoAtendimento.setValue(visita.qtTempoAtendimento);
		}
        chFlRepresentantePresente.setValue(visita.flRepresentantePresente);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
    	cbRepresentantes.setSelectedIndex(-1);
        cbClientes.setSelectedIndex(-1);
    	cbClientes.removeAll();
        edDtVisita.setValue(DateUtil.getCurrentDate());
        edHrVisita.setValue(TimeUtil.getCurrentTimeHHMM());
        edDsObservacao.setText("");
        edQtTempoAtendimento.setText(edQtTempoAtendimento.maskToEmptyText(edQtTempoAtendimento.getMask()));
        chFlRepresentantePresente.setValue(ValueUtil.VALOR_NI);
    }

    public void setEnabled(boolean enabled) {
    	cbRepresentantes.setEditable(enabled);
        cbClientes.setEditable(enabled);
        edDtVisita.setEditable(enabled);
        edHrVisita.setEditable(enabled);
        edDsObservacao.setEditable(enabled);
        edQtTempoAtendimento.setEditable(enabled);
        chFlRepresentantePresente.setEditable(enabled);
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentantes, getLeft(), getTop());
        UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), cbClientes, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.DATA_LABEL_DATA), edDtVisita, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.DATA_LABEL_HORA), edHrVisita, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.VISITA_LABEL_QTTEMPOATENDIMENTO), edQtTempoAtendimento, getLeft(), AFTER + HEIGHT_GAP);
        //--
        UiUtil.add(this, new LabelName(Messages.VISITA_LABEL_FLREPRESENTANTEPRESENTE), chFlRepresentantePresente, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - barBottomContainer.getHeight() - HEIGHT_GAP_BIG);
    }

    //@Override
    public void onFormShow() throws SQLException {
		boolean editing = isEditing();
       	if (editing && !getDomain().isAlteradoPalm()) {
       		setReadOnly();
       		btSalvar.setVisible(false);
       		btExcluir.setVisible(false);
       	} else {
       		btSalvar.setVisible(true);
       		btExcluir.setVisible(editing);
       	}
       	//--
       	edDtVisita.setEnabled(false);
       	edHrVisita.setEnabled(false);
       	//--
    	super.onFormShow();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		case ControlEvent.PRESSED: {
    			if (event.target == cbRepresentantes) {
    				setRepresentanteSelecionado();
    				cbClientes.recarregaClientes();
    			}
    			break;
    		}

    	}
    }

    private void setRepresentanteSelecionado() throws SQLException {
    	if (cbRepresentantes.getSelectedItem() != null) {
    		SupervisorRep supervisorRep = (SupervisorRep)cbRepresentantes.getSelectedItem();
        	SessionLavenderePda.setRepresentante(supervisorRep.cdRepresentante, supervisorRep.representante.nmRepresentante);
    	}
    }

}
