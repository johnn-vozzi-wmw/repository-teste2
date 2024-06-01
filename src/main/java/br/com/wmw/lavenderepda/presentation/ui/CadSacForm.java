package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.EditFoneMask;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ContatoCrm;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.domain.SubTipoSac;
import br.com.wmw.lavenderepda.business.domain.TipoSac;
import br.com.wmw.lavenderepda.business.service.AtendimentoAtivService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ContatoCrmService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.SacService;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ContatoCrmComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.SubTipoSacComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoSacComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadSacForm extends BaseCrudCadForm {
	
	private int TABPANEL_DETALHES = 0;

    private LabelContainer listDetailContainer;
    private ScrollTabbedContainer scTabContainer;
    private TipoSacComboBox cbTipoSac;
    private SubTipoSacComboBox cbSubTipoSac;
    private LabelValue lbNmCliente;
    private LabelValue lbDtCadastro;
    private LabelValue lbDtConclusao;
    private LabelValue lbNmEmpresa;
    private LabelName lbSubTipoSac;
    private LabelName lbNuPedido;
    private LabelName lbNota;
    private LabelValue lbFone;
    private LabelValue lbNmUsuarioSac;
    private EditText edNuPedido;
    private EditText edNota;
    private EditMemo edDescricao;
	public CadSacWindow cadSacWindow;
	public boolean onSacWindow;
	private ButtonAction btFinalizarOcorrencia;
	private ButtonAction btEditar;
	private ButtonOptions bmOpcoes;
	private ButtonAction btAdicionarAtividade;
	private ButtonAction btAdicionarProduto;
	private boolean liberadoEdicao;
	private boolean emEdicao;
	private ContatoCrmComboBox cbContato;
	private BaseButton btCadContato;
    private LabelValue lbFoneContato;
    private LabelValue lbEmailContato;

	private String dsSerieNota;

    
    public CadSacForm(boolean onSacWindow) throws SQLException  {
        super(Messages.SAC_TITULO);
        this.onSacWindow = onSacWindow;
        lbNmCliente = new LabelValue("");
        lbSubTipoSac = new LabelName(Messages.SAC_LABEL_SUBTIPO_SAC);
        lbNuPedido = new LabelName(Messages.SAC_LABEL_PEDIDO + "*");
        lbNota = new LabelName(Messages.SAC_LABEL_NOTA_FISCAL + "*");
        edNuPedido = new EditText("@@@@@@@@@@", 20);
        edNota = new EditText("@@@@@@@@@@", 20);
        lbNmUsuarioSac = new LabelValue(SessionLavenderePda.getRepresentante().nmRepresentante);
        lbDtCadastro = new LabelValue("");
        lbDtConclusao = new LabelValue("");
		bmOpcoes = new ButtonOptions();
		bmOpcoes.setID("bmOpcoes");
       	cbTipoSac = new TipoSacComboBox();
       	cbTipoSac.setSelectedIndex(0);
       	cbSubTipoSac = new SubTipoSacComboBox();
		lbNmEmpresa = new LabelValue(EmpresaService.getInstance().getEmpresa(SessionLavenderePda.cdEmpresa).nmEmpresa);
		liberadoEdicao();
        lbFone = new LabelValue(EditFoneMask.mask);
        edDescricao = new EditMemo("@@@", 4, 2, 255).setID("edObs");
        listDetailContainer = new LabelContainer("");
        btFinalizarOcorrencia = new ButtonAction(Messages.BUTTON_LABEL_ENCERRAR, "images/ok.png", true);
        btEditar = new ButtonAction(Messages.LABEL_BOTAO_EDITAR, "images/editar.png");
        btAdicionarAtividade = new ButtonAction(Messages.LABEL_BOTAO_ADD_ATIVIDADE, "images/add.png");
        btAdicionarProduto = new ButtonAction(Messages.LABEL_BOTAO_ADD_PRODUTO, "images/add.png");
        Vector abas = new Vector();
        abas.addElement(Messages.SAC_TAB_DETALHES); 
        if (LavenderePdaConfig.usaOcorrenciaSACComWorkflow) {
        	abas.addElement(Messages.SAC_TAB_ATIVIDADES_ANDAMENTO); 
        	abas.addElement(Messages.SAC_TAB_ATIVIDADES_A_FAZER); 
        	setReadOnly();
		} else {
			abas.addElement(Messages.SAC_TAB_HISTORICO_ATIVIDADES); 
		}
        
        if (LavenderePdaConfig.usaRelacionamentoProdutosSAC) {
        	abas.addElement(Messages.SAC_TAB_PRODUTOS_RELACIONADOS); 
        }
        scTabContainer = new ScrollTabbedContainer((String[]) abas.toObjectArray());
        scTabContainer.allSameWidth = true;
        cbContato = new ContatoCrmComboBox(Messages.SAC_LABEL_CONTATO);
        btCadContato = new BaseButton(UiUtil.getColorfulImage("images/add.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
        lbFoneContato = new LabelValue();
        lbEmailContato = new LabelValue();
        
    }
    
	@Override
    public String getEntityDescription() {
    	return Messages.SAC_TITULO;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
    	return SacService.getInstance();
    }
    
	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		clearScreen();
		Sac sac = (Sac)domain;
		montaCliente(sac);
		cbTipoSac.setValue(sac.cdTipoSac);
		sac.tipoSac = new TipoSac();
		sac.tipoSac.cdTipoSac = sac.cdTipoSac;
		sac.tipoSac.subTipoSac = new SubTipoSac();
		sac.tipoSac.subTipoSac.cdSubTipoSac = sac.cdSubTipoSac;
		if (ValueUtil.isNotEmpty(sac.cdTipoSac)) {
			cbSubTipoSac.cdTipoSac = sac.cdTipoSac;
			cbSubTipoSac.carregaSubTiposSac();
			
		}
		cbSubTipoSac.setValue(sac.tipoSac);
		cbTipoSac.setEnabled(!isEditing());
		edDescricao.setEditable(!isEditing());
		cbContato.setValue(sac.cdContato, sac.cdCliente);
		sac.contatoCrm = new ContatoCrm();
		sac.contatoCrm.cdContato = sac.cdContato;
		cbContato.setEnabled(!isEditing());
		btCadContato.setEnabled(!isEditing());
		btCadContato.setVisible(!isEditing());
        edDescricao.setText(StringUtil.getStringValue(sac.dsSac));
        liberaNotaPedido();
        
	}

	private void montaCliente(Sac sac) throws SQLException {
		Cliente cliente =  ClienteService.getInstance().getCliente(sac.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(Cliente.class), sac.cdCliente);
		listDetailContainer.setDescricao(Messages.SAC_PROTOCOLO + " - " + (sac.cdSac != null ? sac.cdSac : Messages.LABEL_NOVO_SAC));
		lbNmCliente.setValue(StringUtil.getStringValue(cliente.nmRazaoSocial));
		edNuPedido.setValue(StringUtil.getStringValue(sac.nuPedido));
		edNuPedido.setRightIcon("images/maisfiltros.png");
		edNota.setValue(StringUtil.getStringValue(sac.nuNotaFiscal));
		edNota.setRightIcon("images/maisfiltros.png");
		lbNmUsuarioSac.setValue(StringUtil.getStringValue(UsuarioWebService.getInstance().getNmUsuario(sac.cdUsuarioSac)));
		if (ValueUtil.valueEquals(lbNmUsuarioSac.getValue(), "")) {
			lbNmUsuarioSac.setValue(RepresentanteService.getInstance().findColumnByRowKey(sac.cdUsuarioSac + ";", "NMREPRESENTANTE"));
		}
		lbDtCadastro.setValue(StringUtil.getStringValue(sac.dtCadastro) + " - " + StringUtil.getStringValue(sac.hrCadastro));
		lbDtConclusao.setValue(StringUtil.getStringValue(sac.dtConclusao) + " - " + StringUtil.getStringValue(sac.hrConclusao));
		lbNmEmpresa.setValue(StringUtil.getStringValue(EmpresaService.getInstance().getEmpresaName(sac.cdEmpresa)));
		lbFone.setValue(StringUtil.getStringValue(cliente.nuFone));
		if (!LavenderePdaConfig.ocultaContatoSAC) {
			ContatoCrm contato = ContatoCrmService.getInstance().getContatoBySac(sac);
			if (contato != null) {				
				lbFoneContato.setValue(contato.nuFone);
				lbEmailContato.setValue(contato.dsEmail);
			}
		}
		
	}

	@Override
	protected void onFormStart() throws SQLException {
		int top = onSacWindow ? getTop() : super.getTop();
		UiUtil.add(this, listDetailContainer, LEFT, top, FILL, LabelContainer.getStaticHeight());
		UiUtil.add(this, scTabContainer, LEFT, getNextY(), FILL, onSacWindow ? FILL :  FILL - UiUtil.getBottomBarPreferredHeight());
		bmOpcoes.removeAll();
		if (LavenderePdaConfig.usaRelacionamentoProdutosSAC) {
			UiUtil.add(barBottomContainer, btAdicionarProduto, 2);
		}

		UiUtil.add(barBottomContainer, btFinalizarOcorrencia, 5);
		UiUtil.add(barBottomContainer, btEditar, 4);
		UiUtil.add(barBottomContainer, btAdicionarAtividade, 3);

		Container tabPanel = scTabContainer.getContainer(TABPANEL_DETALHES);
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_CADASTRO), lbDtCadastro, getLeft(), TOP);
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_CONCLUSAO), lbDtConclusao, getLeft(), getNextY());
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_ATENDENTE), lbNmUsuarioSac, getLeft(),getNextY());
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_EMPRESA), lbNmEmpresa, getLeft(), getNextY());
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_CLIENTE), lbNmCliente, getLeft(), getNextY());
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_FONE), lbFone, getLeft(), getNextY());
		if (!LavenderePdaConfig.ocultaContatoSAC) {
			UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_CONTATO), cbContato, getLeft(), getNextY(), FILL - 55);
			UiUtil.add(tabPanel, btCadContato, RIGHT - 10, SAME, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_FONE_CONTATO), lbFoneContato, getLeft(), getNextY());
			UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_EMAIL_CONTATO), lbEmailContato, getLeft(), getNextY());
		}
		
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_TIPO_SAC + "*"), cbTipoSac, getLeft(), getNextY());
		UiUtil.add(tabPanel, lbSubTipoSac, cbSubTipoSac, getLeft(), getNextY());
		UiUtil.add(tabPanel, new LabelName(Messages.SAC_LABEL_DESCRICAO + "*"), edDescricao, getLeft(), getNextY());
		Sac sac = getSac();
		if (sac.tipoSac == null) {
			sac.tipoSac = (TipoSac) cbTipoSac.getSelectedItem();
			sac.tipoSac = new TipoSac();
			sac.tipoSac.cdTipoSac = sac.cdTipoSac;
			sac.tipoSac.subTipoSac = new SubTipoSac();
			sac.tipoSac.subTipoSac.cdSubTipoSac = sac.cdTipoSac;
		}
		if (!LavenderePdaConfig.ocultaContatoSAC && sac.contatoCrm != null) {
			sac.contatoCrm = (ContatoCrm) cbContato.getSelectedItem();
			sac.contatoCrm = new ContatoCrm();
			sac.contatoCrm.cdContato = sac.cdContato;
		}
		tabPanel = scTabContainer.getContainer(TABPANEL_DETALHES);
		if (ValueUtil.valueEquals(sac.tipoSac.flRelacionarPedido, "O")) {
			lbNuPedido.setValue(Messages.SAC_LABEL_PEDIDO + "*");
			UiUtil.add(tabPanel, lbNuPedido, edNuPedido, getLeft(), getNextY());
		} else {
			lbNuPedido.setValue(Messages.SAC_LABEL_PEDIDO);
			UiUtil.add(tabPanel, lbNuPedido, edNuPedido, getLeft(), getNextY());
		}
		if (ValueUtil.valueEquals(sac.tipoSac.flRelacionarNotaFiscal, "O")) {
			lbNota.setValue(Messages.SAC_LABEL_NOTA_FISCAL + "*");
			UiUtil.add(tabPanel, lbNota, edNota, getLeft(), getNextY());
		} else {
			lbNota.setValue(Messages.SAC_LABEL_NOTA_FISCAL);
			UiUtil.add(tabPanel, lbNota, edNota, getLeft(), getNextY());
		}
		ListAtendimentoAtivForm listAtendimentoAtivForm = new ListAtendimentoAtivForm(getSac(), onSacWindow);
		scTabContainer.setContainer(1, listAtendimentoAtivForm);
		if (LavenderePdaConfig.usaOcorrenciaSACComWorkflow) {
			scTabContainer.setContainer(2, new ListTipoSacAtendimentoForm(getSac(), listAtendimentoAtivForm.listAtendimentoAtiv));
		}
	}
	
	@Override
	protected int getTop() {
		if (onSacWindow) {
			return TOP;
		} else {
			return super.getTop() + HEIGHT_GAP + LabelContainer.getStaticHeight();
		}
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		Sac sac = getSac();
	    sac.nuPedido = edNuPedido.getValue().trim();
	    sac.nuNotaFiscal = edNota.getValue().trim();
		sac.contatoCrm = (ContatoCrm) cbContato.getSelectedItem();
		if (cbContato.getSelectedItem() != null) {
			sac.cdContato = sac.contatoCrm.cdContato;
			sac.nmContato = sac.contatoCrm.nmContato;
			sac.nuFoneContato = sac.contatoCrm.nuFone;
			sac.emailContato = sac.contatoCrm.dsEmail;
			lbFoneContato.setValue(sac.contatoCrm.nuFone);
			lbEmailContato.setValue(sac.contatoCrm.dsEmail);
		} else {
			sac.contatoCrm = new ContatoCrm();
		}
	    sac.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    sac.dsSac = edDescricao.getValue().trim();
	    sac.tipoSac = (TipoSac) cbTipoSac.getSelectedItem();
	    if (sac.tipoSac != null) {
	    	sac.cdTipoSac = sac.tipoSac.cdTipoSac;
	    }
		if (cbSubTipoSac.getSelectedIndex() == 0) {
			sac.cdSubTipoSac = null;
			if (sac.tipoSac != null) {
				sac.tipoSac.subTipoSac = null;
			}
		} else {
			if (sac.tipoSac != null) {
				sac.tipoSac.subTipoSac = (SubTipoSac) cbSubTipoSac.getSelectedItem();
				if (sac.tipoSac.subTipoSac != null) {
					sac.cdSubTipoSac = sac.tipoSac.subTipoSac.cdSubTipoSac;
				}
			}
		}
		return sac;
	}
	
	@Override
	protected void clearScreen() throws java.sql.SQLException {
        edNuPedido.setValue("");
        edNota.setValue("");
        lbDtCadastro.setValue("");
        lbDtConclusao.setValue("");
       	cbTipoSac.clear();
       	cbSubTipoSac.clear();
       	cbSubTipoSac.setEnabled(false);
		lbNmEmpresa = new LabelValue(EmpresaService.getInstance().getEmpresa(SessionLavenderePda.cdEmpresa).nmEmpresa);
        edDescricao.setValue("");
        cbContato.clear();
        lbFoneContato.setValue(""); 
        lbEmailContato.setValue("");
	}
	
	@Override
	protected BaseDomain createDomain() throws SQLException {
		Sac sac = new Sac();
		sac.cdSac = getCrudService().generateIdGlobal();
		sac.cdCliente = SessionLavenderePda.getCdCliente();
		sac.cdEmpresa = SessionLavenderePda.cdEmpresa;
		sac.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(getClass());
		sac.cdUsuarioSac = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		sac.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		sac.cdStatusSac = Sac.CDSTATUSSAC_NAO_INICIADO;
		sac.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		sac.flOrigem = "P";
		sac.contatoCrm = new ContatoCrm();
		return sac;
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == cbTipoSac) {
				cbTipoSacClick();
			} else if (event.target == btFinalizarOcorrencia) {
				btFinalizarOcorrenciaClick();
			} else if (event.target == btEditar) {
				btEditarClick();
			} else if (event.target == btAdicionarAtividade) {
				btInserirAtividadeClick();
			} else if (event.target == btAdicionarProduto) {
				show(new ListProdutoForm());
			} else if (event.target == cbContato) {
				cbContatoClick();
			} else if (event.target == btCadContato) {
				btCadContatoClick();
			}
			break;
		}
		case EditIconEvent.PRESSED: {
			if (event.target == edNuPedido) {
				btListSacPedidoClick(event.target);
			} else if (event.target == edNota) {
				btListSacNfeClick(event.target);
			}
			break;
		}
		}

	}

	private void btCadContatoClick() throws SQLException {
		screenToDomain();
		new CadSacContatoWindow(getSac()).popup();
		cbContato.loadContatoCrm();
		if (getSac().contatoCrm != null) {
			cbContato.setValue(getSac().contatoCrm.cdContato, null);
		}
		cbContato.setEditable(true);
		btCadContato.setEnabled(true);
		btCadContato.setVisible(true);
	}

	private void cbContatoClick() throws SQLException {
		ContatoCrm contatoCrm = (ContatoCrm) cbContato.getSelectedItem();
		if (cbContato.getSelectedItem() != null) {
			lbFoneContato.setValue(contatoCrm.nuFone);
			lbEmailContato.setValue(contatoCrm.dsEmail);
		}
	}

	private void btFinalizarOcorrenciaClick() throws SQLException {
		Sac sac = getSac();
        if (AtendimentoAtivService.getInstance().possuiAtendimentoAberto(getSac())) {
			throw new ValidationException(MessageUtil.getMessage(Messages.SAC_ERRO_FINALIZACAO_ATENDIMENTOS_ABERTO, Messages.SAC_ERRO_FINALIZACAO));
        } else if (!AtendimentoAtivService.getInstance().possuiAtendimentoAtiv(getSac())) {
			throw new ValidationException(MessageUtil.getMessage(Messages.SAC_ERRO_FINALIZACAO, Messages.SAC_ERRO_FINALIZACAO));
        } else {
        	sac.cdStatusSac = Sac.CDSTATUSSAC_CONCLUIDO;
        	sac.dtConclusao = new Date();
        	sac.hrConclusao = TimeUtil.getCurrentTimeHHMM();
        	super.salvarClick();
        }
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		if (emEdicao) { 
			boolean cancelarEdicao = UiUtil.showConfirmYesNoMessage(Messages.MSG_SAIR_CANCELAR_SEM_SALVAR); 
			if (cancelarEdicao && ValueUtil.isNotEmpty(getSac().dtCadastro)) {
				emEdicao = false;
				clearScreen();
				btEditar.setVisible(true);
				btExcluir.setVisible(true);
				btFinalizarOcorrencia.setVisible(true);
				btSalvar.setVisible(false);
				domainToScreen(getSac());
				visibleState();
			} else if (cancelarEdicao && ValueUtil.isEmpty(getSac().dtCadastro)) {
				super.voltarClick();
			} else if (!cancelarEdicao) {
				return;
			}
		} else {
			super.voltarClick();
		}
		scTabContainer.setActiveTab(0);

	}

	private void btInserirAtividadeClick() throws SQLException {
		new CadSacAtividadeWindow(getSac()).popup();
		visibleState();
		scTabContainer.setActiveTab(1);
	}

	@Override
	public void visibleState() throws SQLException {
		super.visibleState();
		scTabContainer.setActiveTab(0);
		liberadoEdicao();
		emEdicao = false;
		barBottomContainer.setVisible(!onSacWindow);
		ListAtendimentoAtivForm listAtendimentoAtivForm = new ListAtendimentoAtivForm(getSac(), onSacWindow);
	    scTabContainer.setContainer(1, listAtendimentoAtivForm);
	    if (LavenderePdaConfig.usaOcorrenciaSACComWorkflow) {
  		    scTabContainer.setContainer(2, new ListTipoSacAtendimentoForm(getSac(), listAtendimentoAtivForm.listAtendimentoAtiv));
	    }
	    if (!LavenderePdaConfig.isPermiteGerirListaSacCliente() || ValueUtil.valueEquals(getSac().cdStatusSac, Sac.CDSTATUSSAC_CONCLUIDO)) {
	    	btExcluir.setVisible(false);
			btFinalizarOcorrencia.setVisible(false);
			btEditar.setVisible(false);
			btSalvar.setVisible(false);
			btAdicionarAtividade.setVisible(false);
			btAdicionarProduto.setVisible(false);
			btCadContato.setVisible(false);
	    } else {
	    	if (LavenderePdaConfig.isPermiteGerirListaSacCliente()) {
	    		btExcluir.setVisible(liberadoEdicao && ValueUtil.isNotEmpty(getSac().dtCadastro));
	    		btEditar.setVisible(liberadoEdicao && ValueUtil.isNotEmpty(getSac().dtCadastro));
	    		btSalvar.setVisible(ValueUtil.isEmpty(getSac().dtCadastro));
	    	} 
	    	if (LavenderePdaConfig.usaOcorrenciaSACComWorkflow) {
	    		btAdicionarAtividade.setVisible(false);
	    		btAdicionarProduto.setVisible(false);
	    	} else {
	    		btAdicionarAtividade.setVisible(ValueUtil.isNotEmpty(getSac().dtCadastro));
	    		btAdicionarProduto.setVisible(ValueUtil.isNotEmpty(getSac().dtCadastro));
	    	}
	    	
	    	if (ValueUtil.valueEquals(getSac().flOrigem, "P")) {
	    		btFinalizarOcorrencia.setVisible(isEditing()
	    				&& (LavenderePdaConfig.isPermiteEncerrarOcorrencia() 
	    						|| ValueUtil.valueEquals(getSac().cdUsuarioSac, SessionLavenderePda.usuarioPdaRep.cdRepresentante)));
	    	} else {
	    		btFinalizarOcorrencia.setVisible(isEditing()
	    				&& (LavenderePdaConfig.isPermiteEncerrarOcorrencia() 
	    				|| ValueUtil.valueEquals(getSac().cdUsuarioSac, SessionLavenderePda.usuarioPdaRep.cdUsuario)));
	    	}
	    	
	    }
	}

	private void btEditarClick() throws SQLException {
		setEnabled(true);
		btEditar.setVisible(false);
		btSalvar.setVisible(true);
		btExcluir.setVisible(false);
		btFinalizarOcorrencia.setVisible(false);
		btAdicionarAtividade.setVisible(false);
		btAdicionarProduto.setVisible(false);
		cbTipoSac.setEnabled(true);
		if (cbSubTipoSac.size() > 1) {
			cbSubTipoSac.setEnabled(true);
		}
		edDescricao.setEnabled(true);
		edDescricao.setEditable(isEditing());
		emEdicao = true;
		cbContato.setEnabled(true);
		btCadContato.setEnabled(true);
		btCadContato.setVisible(true);
		liberaNotaPedido();
	}
	
	private void cbTipoSacClick() throws SQLException {
		TipoSac tpSac = new TipoSac();
		if (cbTipoSac.getSelectedItem() != null) {
			lbSubTipoSac.setValue(Messages.SAC_LABEL_SUBTIPO_SAC);
			tpSac = (TipoSac) cbTipoSac.getSelectedItem();
			tpSac.cdTipoSac = tpSac.cdTipoSac;
			cbSubTipoSac.cdTipoSac = tpSac.cdTipoSac;
			cbSubTipoSac.carregaSubTiposSac();
			if (cbSubTipoSac.size() > 1) {
				cbSubTipoSac.setSelectedIndex(0);
				cbSubTipoSac.setEnabled(true);
				lbSubTipoSac.setValue(Messages.SAC_LABEL_SUBTIPO_SAC + "*");
			}
			
			if (ValueUtil.valueEquals(tpSac.flRelacionarNotaFiscal, ValueUtil.VALOR_NAO)) {
				edNota.setValue("");
			}
			if (ValueUtil.valueEquals(tpSac.flRelacionarPedido, ValueUtil.VALOR_NAO)) {
				edNuPedido.setValue("");
			}

		}  else {
			cbSubTipoSac.setEnabled(false);
		}
		
		if (ValueUtil.valueEquals(tpSac.flRelacionarPedido, "O")) {
			lbNuPedido.setValue(Messages.SAC_LABEL_PEDIDO + "*");
		} else {
			lbNuPedido.setValue(Messages.SAC_LABEL_PEDIDO);
		}
		if (ValueUtil.valueEquals(tpSac.flRelacionarNotaFiscal, "O")) {
			lbNota.setValue(Messages.SAC_LABEL_NOTA_FISCAL + "*");
		} else {
			lbNota.setValue(Messages.SAC_LABEL_NOTA_FISCAL);
		}
		
		liberaNotaPedido();

	}
	
	public void liberaNotaPedido() throws SQLException {
		TipoSac tpSac = null;
		if (cbTipoSac.getSelectedItem() != null) {
			tpSac = (TipoSac) cbTipoSac.getSelectedItem();
		}
		if (tpSac != null) {
			if (tpSac.flRelacionarNotaFiscal != null) {
		    	if (!ValueUtil.valueEquals(tpSac.flRelacionarNotaFiscal, ValueUtil.VALOR_NAO)
		    			&& ((ValueUtil.isNotEmpty(getSac().dtCadastro) && emEdicao) || !isEditing())) {
		    		edNota.setEnabled(true);
		    	} else {
		    		edNota.setEnabled(false);
		    		
		    	}
			}
			if (tpSac.flRelacionarPedido != null) {
		    	if (!ValueUtil.valueEquals(tpSac.flRelacionarPedido, ValueUtil.VALOR_NAO) 
		    			&& ((ValueUtil.isNotEmpty(getSac().dtCadastro) && emEdicao) || !isEditing())) {
		    		edNuPedido.setEnabled(true);
		    	} else {
		    		edNuPedido.setEnabled(false);
		    	}
			}
    	} else {
    		edNota.setValue("");
    		edNota.setEnabled(false);
    		edNuPedido.setValue("");
    		edNuPedido.setEnabled(false);
		}
	}
	
	private void btListSacPedidoClick(Object target) throws SQLException {
		ListSacPedidoClienteWindow listSacPedidoClienteWindow = new ListSacPedidoClienteWindow(getSac());
		listSacPedidoClienteWindow.popup();
		if (listSacPedidoClienteWindow.nuPedido != null) {
			edNuPedido.setValue(listSacPedidoClienteWindow.nuPedido);		
		}
		
	}

	private void btListSacNfeClick(Object target) throws SQLException {
		ListSacNfeClienteWindow listSacNfeClienteWindow = new ListSacNfeClienteWindow(getSac());
		listSacNfeClienteWindow.popup();
		if (listSacNfeClienteWindow.nuNfe != 0) {
			edNota.setValue(StringUtil.getStringValue(listSacNfeClienteWindow.nuNfe));
			dsSerieNota = listSacNfeClienteWindow.dsSerie;
		}
	}

	public Sac getSac() throws SQLException {
		return (Sac) getDomain() ;
	}

	@Override
	public void add() throws SQLException {
		super.add();
		emEdicao = true;
		createDomain();
		domainToScreen(getSac());
		btEditar.setVisible(false);
		btExcluir.setVisible(false);
		btFinalizarOcorrencia.setVisible(false);
		btSalvar.setVisible(true);
		
	}

	public void liberadoEdicao() throws SQLException {
		liberadoEdicao = !LavenderePdaConfig.naoPermiteExcluirAlterarSAC		 
				&& !ValueUtil.valueEquals(getSac().cdStatusSac, Sac.CDSTATUSSAC_CONCLUIDO);
	}
	
	@Override
	protected void onSave() throws SQLException {
		if (ValueUtil.valueEquals(getSac().cdStatusSac, Sac.CDSTATUSSAC_CONCLUIDO)) {
			super.onSave();
			super.list();
		} else {
			try {
				save();
				
			} catch (Exception e) {
				Sac sac = getSac();
				sac.dtCadastro = null;
				sac.hrCadastro = null;
				throw e;
			}
			edit(getSac());
			liberaNotaPedido();
		}
	}

	@Override
	protected void excluirClick() throws SQLException {
		Sac sac = getSac();
        if (AtendimentoAtivService.getInstance().possuiAtendimentoAtiv(getSac())) {
        	AtendimentoAtiv atendimentoAtivFilter = new AtendimentoAtiv();
    		atendimentoAtivFilter.cdEmpresa = sac.cdEmpresa;
    		atendimentoAtivFilter.cdRepresentante = sac.cdRepresentante;
    		atendimentoAtivFilter.cdCliente = sac.cdCliente;
    		atendimentoAtivFilter.cdSac = sac.cdSac;
    		AtendimentoAtivService.getInstance().deleteAllByExample(atendimentoAtivFilter);
        }
		super.excluirClick();
	}
	
	@Override
	protected void beforeSave() throws SQLException {
		super.beforeSave();
		Sac sac = getSac();
		if (ValueUtil.isEmpty(getSac().dtCadastro)) {
			sac.dtCadastro = new Date();
			sac.hrCadastro = TimeUtil.getCurrentTimeHHMM();
		}
		sac.dtAlteracao = new Date();
		sac.hrAlteracao = TimeUtil.getCurrentTimeHHMM();
		if (isEditing()) {
			sac.flTipoAlteracao = "A";
		} else {
			sac.flTipoAlteracao = "I";
		}
	}
	
	
}
