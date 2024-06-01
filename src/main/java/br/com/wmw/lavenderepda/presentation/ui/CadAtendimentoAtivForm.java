package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
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
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.service.AtendimentoAtivService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TipoSacAtividadeService;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class CadAtendimentoAtivForm extends BaseCrudCadForm {

	private LabelContainer listDetailContainer;
	private BaseScrollContainer scroolContainer;
    private LabelValue lbUsuarioAtendimento;
    private LabelValue lbAtividadeSac;
    private LabelValue lbStatus;
    private EditMemo edDescricao;
    private LabelValue lbDtProgramacao;
    private LabelValue lbCliente;
    private LabelValue lbTipoAtendimento;
    private BaseToolTip tipDsTitulo;
	private ButtonAction btEditar;
	private boolean liberadoEdicao;
	private ButtonAction btFinalizarAtendimento;


    public boolean onCadAtendimentoWindow;
    
    public CadAtendimentoAtivForm(boolean onCadAtendimentoWindow) {
        super(Messages.SAC_DETALHES_ATENDIMENTO_TITULO);
        this.onCadAtendimentoWindow = onCadAtendimentoWindow;
        barBottomContainer.setVisible(true);
        barBottomContainer.setEnabled(true);
        scroolContainer = new BaseScrollContainer(false, true);
        scroolContainer.setBackColor(LavendereColorUtil.formsBackColor);
        listDetailContainer = new LabelContainer("");
        lbUsuarioAtendimento = new LabelValue();
        lbAtividadeSac = new LabelValue();
        lbStatus = new LabelValue();
        edDescricao = new EditMemo("@@@@@@@@@@@@", 3, 1, 255).setID("edDescricao");
        lbDtProgramacao = new LabelValue();
        lbCliente = new LabelValue();
        lbTipoAtendimento = new LabelValue();
        tipDsTitulo = new BaseToolTip(lbAtividadeSac, "");
        btEditar = new ButtonAction(Messages.LABEL_BOTAO_EDITAR, "images/editar.png");
        btFinalizarAtendimento = new ButtonAction(Messages.BUTTON_LABEL_ENCERRAR,"images/ok.png", true);
        if (!LavenderePdaConfig.isPermiteGerirListaSacCliente()) {
        	setReadOnly();
        }
    }

    @Override
    public String getEntityDescription() {
    	return Messages.SAC_DETALHES_ATENDIMENTO_TITULO;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return AtendimentoAtivService.getInstance();
    }
    
    @Override
    protected BaseDomain createDomain() throws SQLException {
        return new AtendimentoAtiv();
    }
    
    @Override
    protected BaseDomain screenToDomain() throws SQLException {
    	AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) getDomain();
    	atendimentoAtiv.dsObservacao = edDescricao.getValue();
    	setDomain(atendimentoAtiv);
		return atendimentoAtiv;
    }
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) domain;
        String dsAtividade = TipoSacAtividadeService.getInstance().getDsAtividade(atendimentoAtiv);
		if (ValueUtil.isEmpty(dsAtividade)) {
			dsAtividade = StringUtil.getStringAbreviada(atendimentoAtiv.dsObservacao, getWidth() - HEIGHT_GAP);
		}
        listDetailContainer.setDescricao(Messages.SAC_PROTOCOLO + " - " + atendimentoAtiv.cdSac);
        lbAtividadeSac.setValue(atendimentoAtiv.cdAtendimentoAtividade);
        tipDsTitulo.setText(MessageUtil.quebraLinhas(dsAtividade));
        lbUsuarioAtendimento.setValue(StringUtil.getStringValue(UsuarioWebService.getInstance().getNmUsuario(atendimentoAtiv.cdUsuarioAtendimento)));
		if (ValueUtil.valueEquals(lbUsuarioAtendimento.getValue(), "")) {
			lbUsuarioAtendimento.setValue(RepresentanteService.getInstance().findColumnByRowKey(atendimentoAtiv.cdUsuarioAtendimento  + ";", "NMREPRESENTANTE"));
		}
        lbDtProgramacao.setValue(atendimentoAtiv.dtAtendimento);
		Cliente cliente =  ClienteService.getInstance().getCliente(atendimentoAtiv.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(Cliente.class), atendimentoAtiv.cdCliente);
        lbCliente.setValue(cliente.nmRazaoSocial);
        lbTipoAtendimento.setValue(atendimentoAtiv.getDsTipoAtendimento(atendimentoAtiv.flTipoAtendimento));
        lbStatus.setValue(atendimentoAtiv.getDsStatusAtendimento(atendimentoAtiv.cdStatusAtendimento));
        edDescricao.setValue(atendimentoAtiv.dsObservacao);
     
    }
    
    @Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, listDetailContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	UiUtil.add(this, scroolContainer, LEFT, getNextY(), FILL, onCadAtendimentoWindow ? FILL : FILL - UiUtil.getBottomBarPreferredHeight());
        AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) getDomain();
		if (!ValueUtil.valueEquals(atendimentoAtiv.cdStatusAtendimento, AtendimentoAtiv.CDSTATUSATENDIMENTO_CONCLUIDO)) {
			UiUtil.add(barBottomContainer, btEditar, 4);
			UiUtil.add(barBottomContainer, btFinalizarAtendimento, 5);
		}
    	UiUtil.add(scroolContainer, new LabelName(Messages.SAC_LABEL_ATIVIDADE), lbAtividadeSac, getLeft(), getNextY() + HEIGHT_GAP_BIG * 2, PREFERRED);
    	UiUtil.add(scroolContainer, new LabelName(Messages.SAC_LABEL_ATENDENTE), lbUsuarioAtendimento, getLeft(), getNextY(), PREFERRED);
    	UiUtil.add(scroolContainer, new LabelName(Messages.SAC_LABEL_CLIENTE), lbCliente, getLeft(), getNextY(), PREFERRED);
    	UiUtil.add(scroolContainer, new LabelName(Messages.SAC_LABEL_DATA_ATENDIMENTO), lbDtProgramacao, getLeft(), getNextY(), PREFERRED);
    	if (LavenderePdaConfig.usaOcorrenciaSACComWorkflow) {
    		UiUtil.add(scroolContainer, new LabelName(Messages.SAC_LABEL_TIPO_ATENDIMENTO), lbTipoAtendimento, getLeft(), getNextY(), PREFERRED);
			
		}
		UiUtil.add(scroolContainer, new LabelName(Messages.SAC_LABEL_STATUS), lbStatus, getLeft(), getNextY(), PREFERRED);
		UiUtil.add(scroolContainer, new LabelName(Messages.SAC_DETALHES_ATENDIMENTO_TITULO + "*"), edDescricao, getLeft(), getNextY());
    }

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btEditar) {
				btEditarClick();
			}  else if (event.target == btFinalizarAtendimento) {
				btFinalizarAtendimentoClick();
			} 
			break;
		}
		}
	}

	private void btFinalizarAtendimentoClick() throws SQLException {
		AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) getDomain();
		atendimentoAtiv.cdStatusAtendimento = AtendimentoAtiv.CDSTATUSATENDIMENTO_CONCLUIDO;
		salvarClick();
		visibleState();
	}

	@Override
	protected void clearScreen() throws java.sql.SQLException {
	}

	@Override
	protected int getTop() {
		if (onCadAtendimentoWindow) {
			return TOP;
		} else {
			return super.getTop();
		}
	}

	@Override
	public void visibleState() throws SQLException {
		super.visibleState();
		liberadoEdicao();
		btExcluir.setVisible(liberadoEdicao);
		btEditar.setVisible(liberadoEdicao);
		btFinalizarAtendimento.setEnabled(true);
		btSalvar.setVisible(false);
		barTopContainer.setVisible(!onCadAtendimentoWindow);
		barBottomContainer.setVisible(!onCadAtendimentoWindow);
		AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) getDomain();
	    if (!LavenderePdaConfig.isPermiteGerirListaSacCliente() || ValueUtil.valueEquals(atendimentoAtiv.getDsStatusAtendimento(atendimentoAtiv.cdStatusAtendimento), Messages.ATENDIMENTO_CONCLUIDO)) {
	    	btExcluir.setVisible(false);
			btEditar.setVisible(false);
			btSalvar.setVisible(false);
			btFinalizarAtendimento.setVisible(false);
	    } else {
	    	if (LavenderePdaConfig.isPermiteEncerrarOcorrencia()) {			
	    		btFinalizarAtendimento.setVisible(liberadoEdicao && isEditing());
	    	}
	    	if (LavenderePdaConfig.isPermiteGerirListaSacCliente()) {
	    		btExcluir.setVisible(liberadoEdicao);
	    		btExcluir.setEnabled(liberadoEdicao);
	    		btEditar.setVisible(liberadoEdicao);
	    		btEditar.setEnabled(liberadoEdicao);
	    	} 
	    }
	    
	  	if (ValueUtil.valueEquals(atendimentoAtiv.flOrigem, "P")) {
	  		btFinalizarAtendimento.setVisible(isEditing() && (LavenderePdaConfig.isPermiteEncerrarOcorrencia() || ValueUtil.valueEquals(atendimentoAtiv.cdUsuarioAtendimento, SessionLavenderePda.usuarioPdaRep.cdRepresentante)));
    	} else {
    		btFinalizarAtendimento.setVisible(isEditing() && (LavenderePdaConfig.isPermiteEncerrarOcorrencia() || ValueUtil.valueEquals(atendimentoAtiv.cdUsuarioAtendimento, SessionLavenderePda.usuarioPdaRep.cdUsuario)));
    	}
	}
	
	private void btEditarClick() {
		edDescricao.setEditable(true);
		btSalvar.setEnabled(true);
		btSalvar.setVisible(true);
		btExcluir.setVisible(false);
		btEditar.setVisible(false);
		btFinalizarAtendimento.setVisible(false);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btSalvar.setEnabled(enabled);

	}

	@Override
	protected void refreshComponents() throws SQLException {
		setEnabled(false);
		edDescricao.setEnabled(true);
		edDescricao.setEditable(false);
		btVoltar.setEnabled(true);
	}
	
	public void liberadoEdicao() throws SQLException {
		AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) getDomain();
		liberadoEdicao = !LavenderePdaConfig.naoPermiteExcluirAlterarSAC && !ValueUtil.valueEquals(atendimentoAtiv.getDsStatusAtendimento(atendimentoAtiv.cdStatusAtendimento), Messages.ATENDIMENTO_CONCLUIDO);
	}
	
	@Override
	protected void beforeSave() throws SQLException {
		super.beforeSave();
		AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) getDomain();
		atendimentoAtiv.dtAlteracao = new Date();
		atendimentoAtiv.hrAlteracao = TimeUtil.getCurrentTimeHHMM();
	}

}
