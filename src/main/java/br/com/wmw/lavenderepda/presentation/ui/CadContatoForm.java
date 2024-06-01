package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.service.ContatoErpService;
import br.com.wmw.lavenderepda.business.service.ContatoPdaService;
import br.com.wmw.lavenderepda.business.service.ContatoService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

import java.sql.SQLException;

public class CadContatoForm extends BaseLavendereCrudPersonCadForm {

	private LabelContainer clienteContainer;
    private boolean isContatoERP;
    private ButtonAction btAlterar;

    private boolean inAniversarioCad;
    private boolean alterando = false;
    private boolean realizouAlteracao;

	public Vector contatosCadastrados;
	public ListContatoForm listContatoForm;

    public CadContatoForm() {
		this(false, null);
	}

    public CadContatoForm(boolean inAniversarioCad, ListContatoForm listContatoForm) {
        super(Messages.CONTATO_NOME_ENTIDADE);
        this.inAniversarioCad = inAniversarioCad;
        this.listContatoForm = listContatoForm;
        clienteContainer = new LabelContainer("");
        btAlterar = new ButtonAction(Messages.BOTAO_HABILITAR_ALTERAR, "images/reorganizar.png");
		contatosCadastrados = new Vector();
    }

    //-----------------------------------------------

    protected String getDsTable() throws SQLException {
    	return Contato.TABLE_NAME;
    }

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
    	if (isContatoERP) {
            return ContatoErpService.getInstance();
    	} else {
            return ContatoPdaService.getInstance();
    	}
    }

    //@Override
    public void add() throws SQLException {
    	super.add();
    	ContatoPda contato = (ContatoPda)getDomain();
    	contato.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	contato.cdContato = getCrudService().generateIdGlobal();
    	contato.cdRegistro = ContatoPda.CD_REGISTRO_DEFAULT;
    	contato.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	contato.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	contato.flOrigemContato = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	contato.flAcaoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		isContatoERP = false;
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new ContatoPda();
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	super.domainToScreen(domain);
    	verificaTipoContato(domain);
        clienteContainer.setDescricao(SessionLavenderePda.getCliente().toString());
    }

    private void verificaTipoContato(BaseDomain domain) {
		isContatoERP = false;
		internalSetEnabled(!BaseDomain.FLTIPOALTERACAO_ORIGINAL.equals(domain.flTipoAlteracao), false);
    	if (domain instanceof ContatoErp) {
    		isContatoERP = true;
    		internalSetEnabled(true, false);
    	}
    }

    //@Override
    protected void clearScreen() throws SQLException {
    	super.clearScreen();
        clienteContainer.setDescricao(SessionLavenderePda.getCliente().toString());
    }

    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
        btSalvar.setEnabled(enabled);
		try {
			setVisible();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

    public void setVisible() throws SQLException {
    	btExcluir.setVisible(isEnabled() && isPermiteInserirContatoNovoCliente() && !(isContatoERP && !LavenderePdaConfig.isHabilitaExclusaoModuloContatoCliente()) && isEditing() && !inAniversarioCad && SessionLavenderePda.getCliente().isNovoCliente());
    	btAlterar.setVisible(isEnabled() && isPermiteInserirContatoNovoCliente() && !(isContatoERP && !LavenderePdaConfig.isHabilitaEdicaoModuloContatoCliente()) && isEditing() && !inAniversarioCad);
    	btSalvar.setVisible(isEnabled() && isPermiteInserirContatoNovoCliente() && !(isContatoERP && !LavenderePdaConfig.isHabilitaEdicaoModuloContatoCliente()) && !inAniversarioCad);
    }

    private boolean isPermiteInserirContatoNovoCliente() throws SQLException {
    	Cliente cliente = SessionLavenderePda.getCliente();
    	NovoCliente novoCli = NovoClienteService.getInstance().getNovoClienteByCliente(cliente);
    	return (novoCli != null && ((LavenderePdaConfig.permiteContatosNovoClienteTransmitido() && novoCli.isEnviadoServidor()) || !novoCli.isEnviadoServidor())) || (novoCli == null && cliente.isNovoCliente()) || !cliente.isNovoCliente();
	}

    //-----------------------------------------------

    //@Override
    public void onFormShow() throws SQLException {
    	setVisible();
		setEnabled(!isEditing());
		alterando = false;
    	super.onFormShow();
    }

	//@Override
    protected void onFormStart() throws SQLException {
    	super.onFormStart();
        UiUtil.add(barBottomContainer, btAlterar , 5);
    	UiUtil.add(this, clienteContainer, LEFT, super.getTop(), FILL, LabelContainer.getStaticHeight());
    }

    protected int getTop() {
    	return super.getTop() + LabelContainer.getStaticHeight();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	super.onFormEvent(event);
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btAlterar) {
					if (alterando) {
	    				if (realizouAlteracao) {
	    					executaFluxoAlteracao();
	    				} else {
	    					changeBotaoAlterarDesfazer(Messages.BOTAO_HABILITAR_ALTERAR);
	    				}
	    			} else {
	    				changeBotaoAlterarDesfazer(Messages.BOTAO_HABILITAR_DESFAZER);
	    			}
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE : {
	    		realizouAlteracao = true;
	    		break;
	    	}
    	}
    }


    @Override
    protected void voltarClick() throws SQLException {
    	changeBotaoAlterarDesfazer(Messages.BOTAO_HABILITAR_ALTERAR);
    	super.voltarClick();
    }

    //@Override
    protected void salvarClick() throws SQLException {
    	if (!SessionLavenderePda.getCliente().isNovoCliente()) {
			if (UiUtil.showConfirmYesNoMessage(Messages.CONTATO_MSG_CONFIRMACAO_SALVAR)) {
				super.salvarClick();
				changeBotaoAlterarDesfazer(Messages.BOTAO_HABILITAR_ALTERAR);
			}
		} else {
			super.salvarClick();
			changeBotaoAlterarDesfazer(Messages.BOTAO_HABILITAR_ALTERAR);
		}
    }

    protected void save() throws SQLException {
    	BaseDomain domain = screenToDomain();
    	if (!isContatoERP && SessionLavenderePda.getCliente().isNovoCliente()) {
    		ContatoPda contatoPda = (ContatoPda)domain;
    		if (listContatoForm.isNovoClienteSalvo()) {
    			contatoPda.flContatoNovoCliente = ValueUtil.VALOR_SIM;
    			insertOrUpdate(domain);
    		} else {
    			contatoPda.flContatoNovoCliente = ValueUtil.VALOR_SIM;
    			salvaContatoNovoClienteEmMemoria(contatoPda);
    		}
    	} else {
    		insertOrUpdate(domain);
    	}

    	if (isEditing() && !isContatoERP) {
    		if (SessionLavenderePda.getCliente().isNovoCliente() && listContatoForm != null && !listContatoForm.isNovoClienteSalvo()) {
    			updateCurrentRecordInList(listContatoForm.getSelectedDomainContatoEmMemoria());
    		} else {
    			updateCurrentRecordInList(getCrudService().findByRowKey(domain.getRowKey()));
    		}
    	} else {
    		list();
    	}
    }

	private void salvaContatoNovoClienteEmMemoria(ContatoPda domain) throws SQLException {
		ContatoPdaService.getInstance().validate(domain);
		if (contatosCadastrados.contains(domain) && isEditing()) {
			atualizaDadosContato(domain);
		} else {
			if (ValueUtil.isNotEmpty(listContatoForm.contatosCadastradoSemMemoria)) {
				contatosCadastrados = listContatoForm.contatosCadastradoSemMemoria;
			}
			contatosCadastrados.addElement((Contato) domain);
		}
	}

    public void atualizaDadosContato(Contato contato) {
		contato.nmContato = StringUtil.getStringValue(contato.getHashValuesDinamicos().get("NMCONTATO"));
		contato.dsEmail = StringUtil.getStringValue(contato.getHashValuesDinamicos().get("DSEMAIL"));
		contato.nuFone = StringUtil.getStringValue(contato.getHashValuesDinamicos().get("NUFONE"));
	}

	public Vector getContatosCadastrados() {
		return contatosCadastrados;
	}

    private void executaFluxoAlteracao() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.CONTATO_MSG_ALERTA_ALTERACOES_NAO_SALVAS)) {
			changeBotaoAlterarDesfazer(Messages.BOTAO_HABILITAR_ALTERAR);
			domainToScreen(getDomain());
			repaint();
		}
	}

    private void changeBotaoAlterarDesfazer(String labelBt) {
		setEnabled(!btSalvar.isEnabled() && isEnabled());
		alterando = !alterando;
		btAlterar.setText(labelBt);
		realizouAlteracao = false;
	}

	@Override
	protected boolean delete() throws SQLException {
		boolean result = UiUtil.showConfirmDeleteMessage(getEntityDescription());
		if (result && SessionLavenderePda.getCliente().isNovoCliente()) {
			Vector qtContato;
			if (ValueUtil.isEmpty(contatosCadastrados)) {
				contatosCadastrados = listContatoForm.contatosCadastradoSemMemoria;
			}
			if (listContatoForm.isNovoClienteSalvo()) {
				qtContato = ContatoService.getInstance().findAllContatoByCliente(SessionLavenderePda.getCliente().cdCliente);
			} else {
				qtContato = contatosCadastrados;
			}
			if (qtContato.size() <= LavenderePdaConfig.qtMinContatosNovoCliente()) {
				throw new ValidationException(MessageUtil.getMessage(Messages.NOVOCLIENTE_MSG_QTMINCONTATO_CADASTRADO, LavenderePdaConfig.qtMinContatosNovoCliente()));
			}
			if (!listContatoForm.isNovoClienteSalvo()) {
				boolean delete = deleteContatoEmMemoria((Contato) screenToDomain());
				listContatoForm.contatosCadastradoSemMemoria = contatosCadastrados;
				return delete;
			}
			delete(screenToDomain());
		}
		return result;
	}

	public boolean deleteContatoEmMemoria(Contato screenToDomain) {
		return contatosCadastrados.removeElement(screenToDomain);
	}

}
