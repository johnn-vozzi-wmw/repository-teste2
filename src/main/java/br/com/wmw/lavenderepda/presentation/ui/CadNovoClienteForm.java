package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.CampoDinamicoComboBox;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditInscricaoEstadual;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditNumberMask;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.EditTextMask;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilterDyn;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.enums.GroupTypeFile;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.ContatoPdaService;
import br.com.wmw.lavenderepda.business.service.ContatoService;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.business.service.NovoCliEnderecoService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PesquisaAppService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.DuplicatedCnpjException;
import br.com.wmw.lavenderepda.business.validation.FotoNovoClienteException;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPessoaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderNovoClienteWindow;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.util.CepUtil;
import br.com.wmw.lavenderepda.util.JSONUtil;
import br.com.wmw.lavenderepda.util.LavendereFileChooserBoxUtil;
import totalcross.json.JSONObject;
import totalcross.sql.Types;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class CadNovoClienteForm extends BaseLavendereCrudPersonCadForm {

	private static int TABPANEL_NOVOCLIENTE;
	private static int TABPANEL_NOVOCLIENDERECO;
	
    private EditText edNuCnpj;
    private EditText edNuCpf;
    private EditText edRepresentante;
    private TipoPessoaComboBox cbTipoPessoa;
    public RepresentanteSupervComboBox cbRepresentantesSupervisor;
	private ButtonAction btNovoPedido;
	private ButtonAction btNovaPesquisaMercado;
	private ButtonAction btCadastrarCoordenada;
	private ButtonAction btFoto;
	private ButtonAction btNovoEndereco;
	private ButtonAction btAnexarDoc;
	private ButtonAction btPesquisa;
	private ButtonAction btContato;
	public ButtonOptions btOpcoes;
	public CadNovoClienteWindow cadNovoClienteFilterWindow;
	private boolean onPedidoSemCliente;
	public boolean onWindow;
	private Pedido pedido;
	private Cliente clienteProspects;
	private int nuSequenciaAgendaClienteProspect;
	private GridListContainer listGridNovoCliEndereco;
	public String sortAtributteListSecundaria = null;
	public String sortAscListSecundaria = ValueUtil.VALOR_SIM;
	private int nuParametrosAtivosNaTela;
	private int posicao;
	private boolean fromAnaliseNovoCli;
	public Vector contatosCadastrados;
	private boolean cepEventValueChanged;

    public CadNovoClienteForm(boolean onPedidoSemCliente, Pedido pedido, Cliente cliente) throws SQLException {
        super(Messages.NOVOCLIENTE_NOME_ENTIDADE);
        if (LavenderePdaConfig.isUsaSistemaIdiomaIngles()) {
        	edNuCnpj = new EditText("", 100).setID("edNuCnpj");
            edNuCpf = new EditText("", 100).setID("edNuCpf");
        } else {
        	String mascaraCnpj = "##.###.###/####-##";
        	String mascaraCpf = "###.###.###-##";
        	if (LavenderePdaConfig.isUsaSistemaIdiomaEspanhol()) {
        		mascaraCnpj = "########-#";
        		mascaraCpf = "#######";
        	}
        	edNuCnpj = new EditNumberMask(mascaraCnpj).setID("edNuCnpj");
        	edNuCpf = new EditNumberMask(mascaraCpf).setID("edNuCpf");
        }
        if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpj()) {
        	edNuCnpj.setRightIcon("images/reload.png");
        }
        cbTipoPessoa = new TipoPessoaComboBox().setID("cbTipoPessoa");
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        	edRepresentante = new EditText("@@@@@@@@@@", 10);
        	edRepresentante.setEditable(false);
        }
        if (!LavenderePdaConfig.isUsaCadastroNovoClientePessoaFisicaJuridica()) {
        	cbTipoPessoa.setEditable(false);
        }
        btNovoPedido = new ButtonAction(Messages.BOTAO_NOVO_PEDIDO, "images/novopedido.png");
        btNovaPesquisaMercado = new ButtonAction(Messages.PESQUISAMERCADO_NOME_ENTIDADE_ABREV, "images/pesquisaMercado.png");
        btCadastrarCoordenada = new ButtonAction(Messages.CAD_COORD_AREV, "images/gps.png");
        this.onPedidoSemCliente = onPedidoSemCliente;
        this.pedido = pedido;
        this.clienteProspects = cliente;
        this.nuSequenciaAgendaClienteProspect = cliente != null ? cliente.nuSequenciaVisitaCliente : 0;
        btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
        btAnexarDoc = new ButtonAction(Messages.BOTAO_ANEXAR_DOC, "images/anexo.png");
        listGridNovoCliEndereco = new GridListContainer(4, 2);
		listGridNovoCliEndereco.setID("listContainer");
        listGridNovoCliEndereco.setColsSort(new String[][] {{Messages.NOVOCLIENDERECO_LABEL_LOGRADOURO, NovoCliEndereco.NMCOLUNA_DSLOGRADOURO}, {Messages.NOVOCLIENDERECO_LABEL_BAIRRO, NovoCliEndereco.NMCOLUNA_DSBAIRRO}, {Messages.NOVOCLIENDERECO_LABEL_CIDADE, NovoCliEndereco.NMCOLUNA_DSCIDADE}, {Messages.NOVOCLIENDERECO_LABEL_UF, NovoCliEndereco.NMCOLUNA_CDUF}});
        listGridNovoCliEndereco.atributteSortSelected = NovoCliEndereco.NMCOLUNA_DSLOGRADOURO;
        listGridNovoCliEndereco.sortAsc = sortAscListSecundaria;
        listGridNovoCliEndereco.setColPosition(1, RIGHT);
        listGridNovoCliEndereco.setColPosition(3, RIGHT);
        btNovoEndereco = new ButtonAction(Messages.NOVOCLIENTE_BOTAO_NOVO_ENDERECO, "images/endereco.png");
        btPesquisa = new ButtonAction(Messages.BOTAO_PESQUISA, "images/pesquisacliente.png");
        btOpcoes = new ButtonOptions();
        if (LavenderePdaConfig.isUsaContatosNovoCliente()) {
        	btContato = new ButtonAction(Messages.NOVOCLIENTE_BOTAO_NOVO_CONTATO, "images/contato.png");
		}
    }

	@Override
    protected void addTabsFixas(Vector tableTitles) throws SQLException {
    	super.addTabsFixas(tableTitles);
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		int indexNovoCliente = tableTitles.indexOf(Messages.NOVOCLIENTE_ABA_CADASTRO);
    		if (indexNovoCliente != -1) {
    			tableTitles.removeElement(Messages.NOVOCLIENTE_ABA_CADASTRO);
    		}
    		if (tableTitles.size() == 0) {
    			TABPANEL_NOVOCLIENTE = 0;
    			tableTitles.insertElementAt(Messages.NOVOCLIENTE_ABA_CADASTRO, TABPANEL_NOVOCLIENTE);
    		}
    		int indexNovoCliEndereco = tableTitles.indexOf(Messages.NOVOCLIENTE_ABA_ENDERECO);
    		if (indexNovoCliEndereco != -1) {
    			tableTitles.removeElement(Messages.NOVOCLIENTE_ABA_ENDERECO);
    		}
    		TABPANEL_NOVOCLIENDERECO = tableTitles.size();
    		tableTitles.insertElementAt(Messages.NOVOCLIENTE_ABA_ENDERECO, TABPANEL_NOVOCLIENDERECO);
    	}
    }
    
    public CadNovoClienteForm() throws SQLException {
    	this(false, null, null);
    }

    public CadNovoClienteForm(boolean onWindow) throws SQLException {
    	this(false, null, null);
    	this.onWindow = onWindow;
    }

    protected String getDsTable() throws SQLException {
    	return NovoCliente.TABLE_NAME;
    }
    //-----------------------------------------------

    @Override
    public String getEntityDescription() {
    	return title;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return NovoClienteService.getInstance();
    }

    private NovoCliente getNovoCliente() throws SQLException {
    	return (NovoCliente) getDomain();
    }

    @Override
    protected BaseDomain createDomain() throws SQLException {
        return new NovoCliente();
    }
    
    @Override
    public void add() throws java.sql.SQLException {
    	super.add();
        NovoCliente novocli = getNovoCliente();
        novocli.cdEmpresa = SessionLavenderePda.cdEmpresa;
        novocli.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentantesSupervisor.getValue() : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
        novocli.flOrigemNovoCliente = OrigemPedido.FLORIGEMPEDIDO_PDA;
        novocli.cdNovoCliente = getCrudService().generateIdGlobal();
        novocli.dtCadastro = DateUtil.getCurrentDate();
        novocli.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
        novocli.flEmAnalise = LavenderePdaConfig.marcaNovoClienteParaAnalise ? ValueUtil.VALOR_SIM : null;
        if (clienteProspects != null) {
        	novocli.cdClienteOriginal = clienteProspects.getCdCliente();
        	novocli.oldCdRepresentante = clienteProspects.cdRepresentante;
        }
    }
    
    @Override
    public void edit(BaseDomain domain) throws SQLException {
    	super.edit(domain);
    	NovoCliente novoCliente =  (NovoCliente) domain;
    	if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
    		novoCliente.setFotoNovoClienteList(FotoNovoClienteService.getInstance().findAllFotoNovoClienteByNovoCliente(novoCliente));
    	}
    }

    @Override
    protected BaseDomain screenToDomain() throws SQLException {
    	NovoCliente novocli = (NovoCliente) getDomain();
    	novocli.flTipoPessoa = cbTipoPessoa.getValue();
    	if (cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_FISICA)) {
    		Campo campoIE = NovoClienteService.getInstance().getCampoIE();
    		if (campoIE != null) {
    			EditInscricaoEstadual editIE = (EditInscricaoEstadual) hashComponentes.get(campoIE.nmCampo);
    			editIE.setTipoPessoa(cbTipoPessoa.getValue());
			}
			
		}
    	super.screenToDomain();
    	novocli.flTipoPessoa = cbTipoPessoa.getValue();
    	novocli.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentantesSupervisor.getValue() : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	if (Messages.TIPOPESSOA_FLAG_FISICA.equals(novocli.flTipoPessoa)) {
    		novocli.nuCnpj = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? edNuCpf.getValue() : edNuCpf.getText();
    	} else {
    		novocli.nuCnpj = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? edNuCnpj.getValue() : edNuCnpj.getText();
    	}
    	novocli.qtContato = getQtContato(novocli.nuCnpj);
        return novocli;
    }

    private int getQtContato(String nuCnpj) throws SQLException {
    	if (ValueUtil.isEmpty(nuCnpj)) return 0;
    	Vector vector = ContatoService.getInstance().findAllContatoByNovoCliente(nuCnpj);
    	int qtContatoFind = vector.size();
    	if (!ValueUtil.isEmpty(contatosCadastrados)) {
    		return contatosCadastrados.size();
    	} else {
    		return qtContatoFind;
    	}
    }

	@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	super.domainToScreen(domain);
        NovoCliente novocli = (NovoCliente) domain;
        cbTipoPessoa.setValue(novocli.flTipoPessoa);
        cbTipoPessoaClick();
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor.setValue(novocli.cdRepresentante);
        	edRepresentante.setText(SessionLavenderePda.getRepresentante().nmRepresentante + " - " + "[" + SessionLavenderePda.getRepresentante().cdRepresentante + "]");
        }
        String nuCnpjCpf = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? novocli.nuCnpj : ValueUtil.getValidNumbers(novocli.nuCnpj);
        if (ValueUtil.isNotEmpty(nuCnpjCpf)) {
	    	if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
	    		edNuCpf.setValue(nuCnpjCpf);
	    	} else {
	    		edNuCnpj.setValue(nuCnpjCpf);
	    	}
	    	novocli.oldNuCnpj = nuCnpjCpf;
        }
        setaValorDeCidadeComboRelacionada(novocli);
        carregaNovoCliEndereco(false);
    }

	protected void setaValorDeCidadeComboRelacionada(NovoCliente novocli) throws SQLException {
		Cep cep = new Cep();
    	Control edCDCidade = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDCIDADECOMERCIAL);
    	Control edDSCidade = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL);
    	String cdUf = (String) novocli.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_CDUFCOMERCIAL);
    	if (edCDCidade instanceof CampoDinamicoComboBox) {
    		cep.cdCidade = (String) novocli.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_CDCIDADECOMERCIAL);
    		setaValorComboCdCidadeRelacionada(cep, true, edCDCidade, cdUf);
    	}
    	if (edDSCidade instanceof CampoDinamicoComboBox) {
    		cep.cdCidade = (String) novocli.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL);
    		setaValorComboDsCidadeRelacionada(cep, true, edDSCidade, cdUf);
    	}
	}

    @Override
    protected void clearScreen() throws java.sql.SQLException {
    	super.clearScreen();
        edNuCnpj.setValue("");
    	edNuCpf.setValue("");
    	if (LavenderePdaConfig.isUsaCadastroNovoClientePessoaFisicaJuridica() || LavenderePdaConfig.isUsaCadastroNovoClienteApenasPessoaJuridica()) {
    		cbTipoPessoa.setValue(!LavenderePdaConfig.isUsaCadastroNovoClienteApenasPessoaJuridica() && LavenderePdaConfig.usaTipoPessoaFisicaComoPadraoCadastroNovoCliente ? Messages.TIPOPESSOA_FLAG_FISICA : Messages.TIPOPESSOA_FLAG_JURIDICA);
			edNuCnpj.setVisible(cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_JURIDICA));
			edNuCpf.setVisible(cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_FISICA));
    	} else if (LavenderePdaConfig.isUsaCadastroNovoClientePessoaFisicaJuridica() || LavenderePdaConfig.isUsaCadastroNovoClienteApenasPessoaFisica()) {
    		cbTipoPessoa.setValue(Messages.TIPOPESSOA_FLAG_FISICA);
    		edNuCnpj.setVisible(cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_JURIDICA));
    		edNuCpf.setVisible(cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_FISICA));
    	}
    	if (clienteProspects != null) {
    		clienteProspects = (Cliente) ClienteService.getInstance().findByRowKeyDyn(clienteProspects.getRowKey());
    		clienteProspects.flTipoCadastro = Cliente.TIPO_PROSPECTS;
    		NovoClienteService.getInstance().geraNovoClienteAPartirDeUmClienteProspects((NovoCliente) getDomain(), clienteProspects);
    		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    			NovoCliEnderecoService.getInstance().copiaEnderecosProspectsParaNovoCliente((NovoCliente) getDomain(), clienteProspects);
			}
    		domainToScreen(getDomain());
    	} else if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		listGridNovoCliEndereco.removeAllContainers();
    	}
    }
	@Override
    public void setEnabled(boolean enabled) {
        try {
	    	super.setEnabled(enabled);
	        edNuCnpj.setEditable(!isEditing());
	        edNuCpf.setEditable(!isEditing());
	        if (LavenderePdaConfig.isUsaCadastroNovoClientePessoaFisicaJuridica()) {
	        	cbTipoPessoa.setEditable(!isEditing());
	        }
        } catch (Throwable ee) {
        	ExceptionUtil.handle(ee);
        }
    }

    private boolean haPedidosNovoCliente() throws SQLException {
        Pedido pedidoFilter = new Pedido();
        pedidoFilter.cdEmpresa = getNovoCliente().cdEmpresa;
        pedidoFilter.cdRepresentante = getNovoCliente().cdRepresentante;
        pedidoFilter.cdCliente = getNovoCliente().nuCnpj;
        Vector pedList = PedidoService.getInstance().findAllByExampleSummary(pedidoFilter);
        return (pedList != null) && (pedList.size() > 0);
    }

    private boolean isExistePesquisaMercadoNovoCliente() throws SQLException {
    	PesquisaMercado pesquisaMercado = new PesquisaMercado();
    	pesquisaMercado.cdEmpresa = getNovoCliente().cdEmpresa;
    	pesquisaMercado.cdRepresentante = getNovoCliente().cdRepresentante;
    	pesquisaMercado.cdCliente = getNovoCliente().nuCnpj;
        Vector pesquisaMercadoList = PesquisaMercadoService.getInstance().findAllByExample(pesquisaMercado);
        return (pesquisaMercadoList != null) && (pesquisaMercadoList.size() > 0);
    }

    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	if (!isEditing() && clienteProspects == null) {
    		refreshCombosDinamicas();
    	}
    	NovoCliente novoCliente = getNovoCliente();
		if (novoCliente.isPendente() && LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb && LavenderePdaConfig.usaConfirmacaoCPFCNPJIgualNovoCliente() && 
    			UiUtil.showWarnConfirmYesNoMessage(Messages.NOVOCLIENTE_CPFCNPJ_IGUAL_CLIENTE)) {
    		novoCliente.flStatusCadastro = NovoCliente.FLSTATUSCADASTRO_CONFIRMADO;
    		NovoClienteService.getInstance().update(novoCliente);
    	}
    	setEnabled(!isEditing() || novoCliente.isAlteradoPalm() || LavenderePdaConfig.isValidaCadastroDuasEtapas() && novoCliente.isPrimeiraEtapa());
    	tabDinamica.reposition();
    }
    
    @Override
    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
    	NovoCliente novoCliente = getNovoCliente();
    	setEnabled(!isEditing() || novoCliente.isAlteradoPalm() || LavenderePdaConfig.isValidaCadastroDuasEtapas() && novoCliente.isPrimeiraEtapa());
    	visibleState();
    }

    /* Reorganiza botões conforme parâmetros ativos na tela e se está editando ou não.
     * Caso tenha mais que 4 parâmetros ativos na tela, habilita o botão de opções, e
     * insere documento anexo, coordenadas, pesquisa de mercado, caso estejam ativos.
    */
    @Override
    protected void refreshComponents() throws SQLException {
    	super.refreshComponents();
    	barBottomContainer.removeAll();
    	UiUtil.add(barBottomContainer, btExcluir, 1);
    	posicao = 5;
    	checkButtonParameters();
    	boolean isEnviadoServidor = getNovoCliente().isEnviadoServidor();
    	if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
    		UiUtil.add(barBottomContainer, btFoto, posicao);
    		posicao--;
    	}
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente() && !isEnviadoServidor) {
    		UiUtil.add(barBottomContainer, btNovoEndereco, posicao);
    		posicao--;
    	}
    	if (nuParametrosAtivosNaTela > 4) {
    		if (posicao == 3) {
				posicao--;
			}

    		if (isPermitePedidoNovoCliente()) {
        		UiUtil.add(barBottomContainer, btNovoPedido, posicao);
        		posicao--;
        	}
    		if (!isEnviadoServidor){
				addItensOnButtonMenu();
			}
    	} else {
    		if (LavenderePdaConfig.usaSelecaoDocAnexoNovoCli() && !isEnviadoServidor) {
    			UiUtil.add(barBottomContainer, btAnexarDoc, posicao);
    			posicao--;
    		}
			if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente() && !isEnviadoServidor) {
				UiUtil.add(barBottomContainer, btCadastrarCoordenada, posicao);
				posicao--;
			}
    		if (LavenderePdaConfig.permitePesquisaMercadoNovoCliente && isEditing() && !isEnviadoServidor) {
				UiUtil.add(barBottomContainer, btNovaPesquisaMercado, posicao);
				posicao--;
    		}
    		if (isPermitePedidoNovoCliente()) {
        		UiUtil.add(barBottomContainer, btNovoPedido, posicao);
        		posicao--;
        	}
    		if (LavenderePdaConfig.isUsaPesquisaNovoCliente() && !isEnviadoServidor) {
    			UiUtil.add(barBottomContainer, btPesquisa, posicao);
				posicao--;
    		}
			if (LavenderePdaConfig.isUsaContatosNovoCliente()) {
				UiUtil.add(barBottomContainer, btContato, posicao);
				posicao--;
			}
    	}
    }

	private boolean isPermitePedidoNovoCliente() throws SQLException {
		return isEditing() && ((LavenderePdaConfig.isPermitePedidoApenasNovoClienteNaoEnviadoServidor() && !getNovoCliente().isEnviadoServidor()) || (LavenderePdaConfig.isIgnoraBloqueioPedidoNovoCliente() || LavenderePdaConfig.isPermitePedidoParaNovoClienteApenasPelaTelaDetalhes()));
	}
    
    /* Adiciona o botão de opções a tela.
     * Caso a barra ainda tenha uma posição disponível, adiciona uma das
     * opções neste espaço.
     */
    private void addItensOnButtonMenu() throws SQLException {
		btOpcoes.removeAll();
		UiUtil.add(barBottomContainer, btOpcoes, 3);
		if (posicao > 1) {
			if (posicao == 3) {
				posicao--;
			}
			if (LavenderePdaConfig.usaSelecaoDocAnexoNovoCli()) {
				UiUtil.add(barBottomContainer, btAnexarDoc, posicao);
				addItensOnButtonMenu(Messages.PESQUISAMERCADO_NOME_ENTIDADE, Messages.CAD_COORD_TITULO, Messages.BOTAO_PESQUISA);
				posicao--;
			} else if (LavenderePdaConfig.permitePesquisaMercadoNovoCliente && isEditing()) {
				UiUtil.add(barBottomContainer, btNovaPesquisaMercado, posicao);
				addItensOnButtonMenu(Messages.CAD_COORD_TITULO, Messages.BOTAO_PESQUISA);
				posicao--;
			} else if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente()) {
				UiUtil.add(barBottomContainer, btCadastrarCoordenada, posicao);
				addItensOnButtonMenu(Messages.BOTAO_PESQUISA);
				posicao--;
			} else if (LavenderePdaConfig.isUsaPesquisaNovoCliente()) {
				UiUtil.add(barBottomContainer, btPesquisa, posicao);
				posicao--;
			}
			if (LavenderePdaConfig.isUsaContatosNovoCliente() && posicao > 1) {
				if (posicao == 3){
					posicao--;
				}
				UiUtil.add(barBottomContainer, btContato, posicao);
				posicao--;
			} else {
				addItensOnButtonMenu(Messages.CONTATO_NOME_CONTATOS);
			}
		} else {
			addItensOnButtonMenu(Messages.PESQUISAMERCADO_NOME_ENTIDADE, Messages.BOTAO_ANEXAR_DOC, Messages.CAD_COORD_TITULO, Messages.BOTAO_PESQUISA, Messages.CONTATO_NOME_CONTATOS);
		}
    }
    
    private void addItensOnButtonMenu(String... params) throws SQLException {
    	String[] opcoes = params;
    	int size = opcoes.length;
    	for (int i = 0; i < size; i++) {
			String menuOption = opcoes[i];
			if (Messages.BOTAO_ANEXAR_DOC.equals(menuOption) && LavenderePdaConfig.usaSelecaoDocAnexoNovoCli()) {
				btOpcoes.addItem(Messages.BOTAO_ANEXAR_DOC);
			} else if (Messages.PESQUISAMERCADO_NOME_ENTIDADE.equals(menuOption) && LavenderePdaConfig.permitePesquisaMercadoNovoCliente && isEditing()) {
				btOpcoes.addItem(Messages.PESQUISAMERCADO_NOME_ENTIDADE);
			} else if (Messages.CAD_COORD_TITULO.equals(menuOption) && LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente()) {
				btOpcoes.addItem(Messages.CAD_COORD_TITULO);
			} else if (Messages.BOTAO_PESQUISA.equals(menuOption) && LavenderePdaConfig.isUsaPesquisaNovoCliente()) {
				btOpcoes.addItem(Messages.BOTAO_PESQUISA);
			} else if (Messages.CONTATO_NOME_CONTATOS.equals(menuOption) && LavenderePdaConfig.isUsaContatosNovoCliente()) {
				btOpcoes.addItem(Messages.CONTATO_NOME_CONTATOS);
			}
		}
    }
    
    /*
     * Verifica os parâmetros ativos, e se eles influenciam na visibilidade do componente,
     * na tela em utilização.
     */
    private void checkButtonParameters() throws SQLException {
    	nuParametrosAtivosNaTela = 0;
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente() && !getNovoCliente().isEnviadoServidor()) {
    		nuParametrosAtivosNaTela++;
    	}
    	if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
    		nuParametrosAtivosNaTela++;
    	}
    	if (isPermitePedidoNovoCliente()) {
    		nuParametrosAtivosNaTela++;
    	}
    	if (LavenderePdaConfig.permitePesquisaMercadoNovoCliente && isEditing() && !getNovoCliente().isEnviadoServidor()) {
    		nuParametrosAtivosNaTela++;
    	}
    	if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente() && !getNovoCliente().isEnviadoServidor()) {
    		nuParametrosAtivosNaTela++;
    	}
    	if (LavenderePdaConfig.usaSelecaoDocAnexoNovoCli() && !getNovoCliente().isEnviadoServidor()) {
    		nuParametrosAtivosNaTela++;
    	}
    	if (LavenderePdaConfig.isUsaPesquisaNovoCliente() && !getNovoCliente().isEnviadoServidor()) {
    		nuParametrosAtivosNaTela++;
    	}
		if (LavenderePdaConfig.isUsaContatosNovoCliente()) {
			nuParametrosAtivosNaTela++;
		}
    }

    @Override
    protected void addComponentesFixosInicio() throws SQLException {
    	int yPos = TOP + HEIGHT_GAP;
		int indexNovoCliente = tabDinamica.getIndexTab(Messages.NOVOCLIENTE_ABA_CADASTRO); 
		Container container = indexNovoCliente != -1 ? tabDinamica.getContainer(TABPANEL_NOVOCLIENDERECO) : getContainerPrincipal();
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			UiUtil.add(container, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), edRepresentante, getLeft(), yPos);
			yPos = AFTER + HEIGHT_GAP;
		}
		if (!LavenderePdaConfig.isUsaSistemaIdiomaIngles() || LavenderePdaConfig.isUsaCadastroNovoClientePessoaFisicaJuridica()) {
			UiUtil.add(container, new LabelName(Messages.NOVOCLIENTE_LABEL_TIPOPESSOA), cbTipoPessoa, getLeft(), yPos);
		}
		UiUtil.add(container, new LabelName(Messages.NOVOCLIENTE_LABEL_NUCNPJ), edNuCnpj, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(container, edNuCpf, SAME, SAME);
        if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
        	UiUtil.add(tabDinamica.getContainer(TABPANEL_NOVOCLIENDERECO), listGridNovoCliEndereco, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
        }
    }

	@Override
	protected int getTop() {
 		if (onPedidoSemCliente) {
			return TOP;
		}
		return super.getTop();
	}
    
	@Override
    protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btNovoPedido) {
					btNovoPedidoClick(getNovoCliente());
				} else if (event.target == btCadastrarCoordenada) {
					btCadCoordenadaClick(getNovoCliente());
				} else if (event.target == btFoto) {
					btFotoClick();
				} else if (event.target == btAnexarDoc) {
					btAnexarDocClick();
				} else if (event.target == btNovoEndereco) {
					btNovoEnderecoClick();
				} else if (event.target == btContato) {
					btContatoClick(getNovoCliente());
				} else if (event.target == cbTipoPessoa) {
					cbTipoPessoaClick();
				} else if (event.target == btNovaPesquisaMercado) {
					btNovaPesquisaMercadoClick(getNovoCliente());
				} else if (event.target == btPesquisa) {
					btPesquisaClick(getNovoCliente());
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listGridNovoCliEndereco.isEventoClickUnicoDisparado())) {
					editaEnderecoNovoCliente();
				} 
				break;
			}
			case ControlEvent.WINDOW_CLOSED: {
				if ((listGridNovoCliEndereco != null) && (event.target == listGridNovoCliEndereco.popupMenuOrdenacao)) {
					if (listGridNovoCliEndereco.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listGridNovoCliEndereco.reloadSortSettings();
						sortAtributteListSecundaria = listGridNovoCliEndereco.atributteSortSelected;
						sortAscListSecundaria = StringUtil.getStringValue(listGridNovoCliEndereco.sortAsc);
						carregaNovoCliEndereco(false);
					}
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS:
			case KeyEvent.KEY_PRESS: {
				if (event.target == edNuCnpj) {
					edNuCnpj.geraEventoValueChange();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL)) {
					try {
						edDsCepFocusOut();
						cepEventValueChanged = true;
					} catch (Throwable e) {
						ExceptionUtil.handle(e);
					}
				} else if (event.target == edNuCnpj) {
					if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpj() && !Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
						edNuCnpj.enabledIconEvent = true;
					}
				} else {
					for (int i = 0; i < getNmFormatoCepAutoList().size(); i++) {
						String nmFormatoCepAuto = getNmFormatoCepAutoList().get(i);
						if (event.target == hashComponentes.get(nmFormatoCepAuto)) {
							try {
								edDsCepAutoFocusOut(nmFormatoCepAuto, getDsColunasCepList().get(i));
							} catch (Throwable e) {
								ExceptionUtil.handle(e);
							}
						}
					}
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == btOpcoes) {
					if (btOpcoes.selectedItem.equals(Messages.PESQUISAMERCADO_NOME_ENTIDADE)) {
						btNovaPesquisaMercadoClick(getNovoCliente());
					} else if (btOpcoes.selectedItem.equals(Messages.CAD_COORD_TITULO)){
						btCadCoordenadaClick(getNovoCliente());
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_ANEXAR_DOC)){
						btAnexarDocClick();
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_PESQUISA)) {
						btPesquisaClick(getNovoCliente());
					} else if (btOpcoes.selectedItem.equals(Messages.CONTATO_NOME_CONTATOS)) {
						btContatoClick(getNovoCliente());
					}
				}
			}
			case ControlEvent.FOCUS_OUT: {
				if (event.target == hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL) && !cepEventValueChanged) {
					try {
						edDsCepFocusOut();
					} catch (Throwable e) {
						ExceptionUtil.handle(e);
					}
				} else if ((event.target == edNuCpf || event.target == edNuCnpj) && (ValueUtil.isNotEmpty(edNuCnpj.getValue()) || ValueUtil.isNotEmpty(edNuCpf.getValue()))) {
					validaCpfCnpj((EditText) event.target);
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edNuCnpj) {
					btPreencheCamposAutoClick();
				}
				break;
			}
    	}
    }

	private boolean isRepresentanteSelecionadoValido() throws SQLException {
    	if (ValueUtil.isEmpty(getNovoCliente().cdRepresentante)) {
			UiUtil.showErrorMessage(Messages.MENUREPRESENTANTE_MSG_SELECIONEANTESACAO);
			return false;
		}
		return true;
	}

	private void btAnexarDocClick() throws SQLException {
    	if (!isRepresentanteSelecionadoValido()) {
    		return;
    	}
		try {
			LavendereFileChooserBoxUtil fileChooserBoxUtil = new LavendereFileChooserBoxUtil(GroupTypeFile.ALL, DocumentoAnexo.NM_ENTIDADE_NOVOCLIENTE, getNovoCliente(), false);
			fileChooserBoxUtil.showListDocumentoAnexo();
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}
    
	private void btFotoClick() throws SQLException {
    	if (!isRepresentanteSelecionadoValido()) {
    		return;
    	}
		ImageSliderNovoClienteWindow imageSliderNovoClienteWindow = new ImageSliderNovoClienteWindow((NovoCliente) getDomain(), false, fromAnaliseNovoCli);
		imageSliderNovoClienteWindow.popup();
	}

	private void cbTipoPessoaClick() {
		edNuCpf.setValue("");
		edNuCnpj.setValue("");
		if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
			edNuCpf.setVisible(true);
			edNuCnpj.setVisible(false);
    	} else {
    		edNuCnpj.setVisible(true);
	        edNuCpf.setVisible(false);
    	}
    }

	public boolean showRepresentanteCombo() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			cbRepresentantesSupervisor.setValue(null);
			cbRepresentantesSupervisor.popup();
			getNovoCliente().cdRepresentante = cbRepresentantesSupervisor.getValue();
			cbRepresentantesSupervisor.configureSession();
			edRepresentante.setText(SessionLavenderePda.getRepresentante().nmRepresentante + " - " + "[" + SessionLavenderePda.getRepresentante().cdRepresentante + "]");
			return cbRepresentantesSupervisor.getSelectedItem() != null;
		}
		return true;
	}

    private void btNovoPedidoClick(NovoCliente novoCli) throws SQLException {
    	if (!isRepresentanteSelecionadoValido()) {
    		return;
    	}
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			if (!novoCli.isEnviadoServidor()) {
				save();
			}
			if (!novoCli.isAlteradoPalm() && !LavenderePdaConfig.isIgnoraBloqueioPedidoNovoCliente() && !LavenderePdaConfig.isPermitePedidoParaNovoClienteApenasPelaTelaDetalhes()) {
				UiUtil.showErrorMessage(Messages.NOVOCLIENTE_MSG_PEDIDO_BLOQUEADO);
				return;
			}

			Cliente clienteOficialByNovoCliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli);
			if (clienteOficialByNovoCliente == null) {
				ClienteService.getInstance().insertClienteByNovoCliente(novoCli);
				clienteOficialByNovoCliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli);
			}
			if (clienteOficialByNovoCliente == null) {
				UiUtil.showErrorMessage(Messages.ERRO_NOVO_CLIENTE_NAO_LOCALIZADO);
				return;
			}
			SessionLavenderePda.setCliente(clienteOficialByNovoCliente);


			if (!ConexaoPdaService.getInstance().validateEnvioRecebimentoDadosObrigatorio()) {
				return;
			}
			CadClienteMenuForm cadClienteMenuForm = new CadClienteMenuForm();
			cadClienteMenuForm.btNovoPedidoClick();
		} finally {
			mb.unpop();
		}
	}

    private void btPesquisaClick(NovoCliente novoCli) throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			if (!novoCli.isEnviadoServidor()) {
				save();
			}
			if (!isEditing()) {
				edit(getDomain());
			}
			NovoCliente novoCliente = getNovoCliente();
			Cliente clienteFilter = new Cliente();
			clienteFilter.cdEmpresa = novoCliente.cdEmpresa;
			clienteFilter.cdRepresentante = novoCliente.cdRepresentante;
			clienteFilter.cdCliente = novoCliente.nuCnpj;
			clienteFilter = (Cliente)ClienteService.getInstance().findByPrimaryKey(clienteFilter);
			if (clienteFilter != null){
				show(new ListPesquisaClienteForm(false, clienteFilter));
			}
		} finally {
			mb.unpop();
		}
    }

    public void btNovaPesquisaMercadoClick(NovoCliente novoCli) throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
			if (!novoCli.isEnviadoServidor()) {
				save();
			}
    		if (!novoCli.isAlteradoPalm()) {
    			UiUtil.showErrorMessage(Messages.NOVOCLIENTE_MSG_PESQUISA_MERCADO_BLOQUEADO);
    			return;
    		}
    		SessionLavenderePda.setCliente(NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli));
    		if (!ConexaoPdaService.getInstance().validateEnvioRecebimentoDadosObrigatorio()) {
    			return;
    		}
    		CadClienteMenuForm cadClienteMenuForm = new CadClienteMenuForm();
    		cadClienteMenuForm.btPesquisaMercadoClick();
    	} finally {
    		mb.unpop();
    	}
    }
    
    private void btCadCoordenadaClick(NovoCliente novoCli) throws SQLException {
    	if (!isRepresentanteSelecionadoValido()) {
    		return;
    	}
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		if (!novoCli.isEnviadoServidor()) {
    			save();
    		}
    		if (!isEditing()) {
    			edit(getDomain());
    		}
    		if (!novoCli.isAlteradoPalm()) {
    			UiUtil.showErrorMessage(Messages.NOVOCLIENTE_MSG_CADCOORDENADA_BLOQUEADO);
    			return;
    		}
    		SessionLavenderePda.setCliente(NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli));
    		if (!ConexaoPdaService.getInstance().validateEnvioRecebimentoDadosObrigatorio()) {
    			return;
    		}
    		CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(SessionLavenderePda.getCliente(), true, false);
    		cadCoordenadasGeograficasWindow.popup();
    		if (cadCoordenadasGeograficasWindow.cadastrouCoordenada) {
	    		novoCli.cdLatitude = cadCoordenadasGeograficasWindow.cliente.cdLatitude;
	    		novoCli.cdLongitude = cadCoordenadasGeograficasWindow.cliente.cdLongitude;
				NovoClienteService.getInstance().updateColumn(novoCli.getRowKey(), "cdLatitude", cadCoordenadasGeograficasWindow.cliente.cdLatitude, Types.DECIMAL);
				NovoClienteService.getInstance().updateColumn(novoCli.getRowKey(), "cdLongitude", cadCoordenadasGeograficasWindow.cliente.cdLongitude, Types.DECIMAL);
    		}
    	} finally {
    		mb.unpop();
    	}
	}
    
    private void btNovoEnderecoClick() throws SQLException {
    	if (!isRepresentanteSelecionadoValido()) {
    		return;
    	}
    	CadNovoCliEnderecoWindow cadNovoCliEnderecoWindow = new CadNovoCliEnderecoWindow(getNovoCliente(), null);
    	cadNovoCliEnderecoWindow.popup();
    	tabDinamica.setActiveTab(TABPANEL_NOVOCLIENDERECO);
    	carregaNovoCliEndereco(true);
	}

	private void btContatoClick(NovoCliente novoCli) throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			screenToDomain();
			novoCli.validaQtContato = false;
			NovoClienteService.getInstance().validate(novoCli);

			Cliente cliente = getClientePrimaryKeyNovoCliente(novoCli);
			if (cliente == null) {
				return;
			}
			SessionLavenderePda.setCliente(cliente);

			ListContatoForm listContatoForm = new ListContatoForm(this);
			if (!ValueUtil.isEmpty(contatosCadastrados)) {
				listContatoForm.contatosCadastradoSemMemoria = contatosCadastrados;
			}
			show(listContatoForm);
		} finally {
			mb.unpop();
		}

	}

	private Cliente getClientePrimaryKeyNovoCliente(NovoCliente novoCli) {
		Cliente cliente = new Cliente();
		cliente.cdCliente = cliente.nuCnpj = novoCli.nuCnpj;
		cliente.cdRepresentante = novoCli.cdRepresentante;
		cliente.cdEmpresa = novoCli.cdEmpresa;
		cliente.nmRazaoSocial = ((EditText) hashComponentes.get("NMRAZAOSOCIAL")).getValue();
		return cliente;
	}

    private void editaEnderecoNovoCliente() throws SQLException {
		NovoCliEndereco novoCliEndereco = NovoCliEnderecoService.getInstance().localizaNovoCliEnderecoNaLista(getNovoCliente().novoCliEnderecoList, listGridNovoCliEndereco.getSelectedId());
		if (fromAnaliseNovoCli) {
			novoCliEndereco.flTipoAlteracao = NovoCliEndereco.FLTIPOALTERACAO_ALTERADO;
		}
		CadNovoCliEnderecoWindow cadNovoCliEnderecoWindow = new CadNovoCliEnderecoWindow(getNovoCliente(), novoCliEndereco, fromAnaliseNovoCli);
		cadNovoCliEnderecoWindow.popup();
		carregaNovoCliEndereco(true);
	}
    
    public void setCadNovoClienteForm(CadNovoClienteWindow cadNovoClienteWindow) {
    	this.cadNovoClienteFilterWindow = cadNovoClienteWindow;
    }
    
    @Override
    protected void insertOrUpdate(BaseDomain domain) throws java.sql.SQLException {
    	try {
    		super.insertOrUpdate(domain);
    	} catch (FotoNovoClienteException ex) {
    		UiUtil.showWarnMessage(ex.getMessage());
    		btFotoClick();
    		insertOrUpdate(domain);
    	}
    }
    
    @Override
    protected void insert(BaseDomain domain) throws SQLException {
    	NovoCliente novoCliente = (NovoCliente)domain;
    	novoCliente.cdUsuarioCriacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	super.insert(domain);
    }

    @Override
    protected void afterSave() throws java.sql.SQLException {
    	super.afterSave();
    	if (clienteProspects != null) {
			VisitaService.getInstance().insertVisitaPositivadaParaClienteProspects(getNovoCliente().cdClienteOriginal, nuSequenciaAgendaClienteProspect);
    		if (prevContainer instanceof CadClienteMenuForm) {
    			CadClienteMenuForm cadClienteMenuForm = (CadClienteMenuForm) prevContainer;
    			ListClienteForm listClienteForm =  (ListClienteForm) cadClienteMenuForm.getBaseCrudListForm();
    			listClienteForm.loadDefaultFilters();
    			EditFiltro edFiltro = (EditFiltro) listClienteForm.getComponentToFocus();
				Cliente clienteByNovoCliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(getNovoCliente());
    			if (edFiltro != null) {
    				if(clienteByNovoCliente != null) {
    					edFiltro.setText(clienteByNovoCliente.nuCnpj);
    				}
					Cliente clienteListFilter = (Cliente) listClienteForm.getDomainFilter();
					clienteListFilter.flTipoCadastro = null;
					listClienteForm.filtrarClick();
					edFiltro.setText(ValueUtil.VALOR_NI);
    			}
    			((CadClienteMenuForm) prevContainer).voltarClick();
    		}
    	} else if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
    		VisitaService.getInstance().geraVisitaNovoCliente(getNovoCliente());
    	}
    }
    
    private boolean isNecessarioQtMinFotosNovoCliente() throws SQLException {
    	return LavenderePdaConfig.isUsaCadastroFotoNovoCliente() && LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente > 0 && qtdFotoNovoCliente() < LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente;
    }
    
    private int qtdFotoNovoCliente() throws SQLException {
    	return FotoNovoClienteService.getInstance().qtdFotoNovoCliente((NovoCliente) getDomain());
    }

    @Override
    protected void onSave() throws java.sql.SQLException {
    	if (!"2".equals(LavenderePdaConfig.getUsaCadastroFotoNovoCliente()) && !isNecessarioQtMinFotosNovoCliente()) {
    		save();
    		NovoCliente novoCli = (NovoCliente) getDomain();
        	if (LavenderePdaConfig.obrigaCadCoordenadasGeograficasNovoCliente && !ValueUtil.VALOR_SIM.equals(novoCli.flCadCoordenadaLiberado) && LavenderePdaConfig.isCadastroCoordenadasGeograficasNovoClienteAutomatico() && novoCli.cdLatitude == 0d && novoCli.cdLongitude == 0d) {
        		return;
        	}
    	} else {
    		save();
    	}
    	close();
    }
    
    
    @Override
    protected void voltarClick() throws SQLException {
    	NovoCliente novoCli = (NovoCliente) getDomain();
    	if (!novoCli.isEnviadoServidor() && !UiUtil.showConfirmYesNoMessage(Messages.NOVOCLIENTE_VOLTARCLICK)) {
    		return;
    	}
    	if (isEditing() && LavenderePdaConfig.obrigaCadCoordenadasGeograficasNovoCliente && !ValueUtil.VALOR_SIM.equals(novoCli.flCadCoordenadaLiberado) && LavenderePdaConfig.isCadastroCoordenadasGeograficasNovoClienteAutomatico() && novoCli.cdLatitude == 0d && novoCli.cdLongitude == 0d) {
    		if (!novoCli.isEnviadoServidor() && UiUtil.showConfirmYesNoMessage(Messages.NOVOCLIENTE_CADCOORD_OBRIGATORIO)) {
    			cadastraCoordenadaNovoCliente();
    		}
    	}
    	FotoNovoClienteService.getInstance().cancelaAlteracoesFotosFisicamente(getNovoCliente());
    	if (!isEditing() && LavenderePdaConfig.usaSelecaoDocAnexoNovoCli()) {
    		DocumentoAnexoService.getInstance().deleteDocAnexo(DocumentoAnexo.NM_ENTIDADE_NOVOCLIENTE, ((NovoCliente) getDomain()).getRowKey());
    	}
    	contatosCadastrados = null;
    	super.voltarClick();
    }

    
    
    @Override
    protected void salvarClick() throws SQLException {
		if (!UiUtil.showConfirmYesNoMessage(Messages.NOVOCLIENTE_CADASTRO_CONFIMACAO)) {
			return;
		}
		NovoCliente novoCli = (NovoCliente) getDomain();
    	if (!novoCli.isConfirmado()) {
		    if (!isEditing()) {
			    String nuCnpj;
			    if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
				    nuCnpj = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? edNuCpf.getValue() : edNuCpf.getText();
			    } else {
				    nuCnpj = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? edNuCnpj.getValue() : edNuCnpj.getText();
			    }
			    try {
    				if (NovoClienteService.getInstance().validaCpfCnpjDuplicado(nuCnpj, novoCli.oldNuCnpj, false, novoCli.flTipoCadastro)) {
    					return;
    				}
    			} catch (DuplicatedCnpjException e) {
    				if (LavenderePdaConfig.usaConfirmacaoCPFCNPJIgualNovoCliente()) {
					    if (!UiUtil.showWarnConfirmYesNoMessage(Messages.NOVOCLIENTE_CPFCNPJ_IGUAL_CLIENTE)) {
						    return;
					    } else if (LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb) {
						    novoCli.flStatusCadastro = NovoCliente.FLSTATUSCADASTRO_CONFIRMADO;
					    }
    				} else {
    					throw new ValidationException(Messages.NOVOCLIENTE_ERRO_CPFCNPJ_REPETIDO);
    				}
    			} catch (Throwable e) {
    				ExceptionUtil.handle(e);
    			}
    		}
    	}
    	if (onPedidoSemCliente) {
    		save();
    		Cliente cliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli);
    		cliente.cdTributacaoCliente = pedido.getCliente().cdTributacaoCliente;
    		cliente.cdTributacaoCliente2 = pedido.getCliente().cdTributacaoCliente2;
    		SessionLavenderePda.setCliente(cliente);
    		Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
    		pedido.setCliente(SessionLavenderePda.getCliente());
    		pedido.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    		if (visita != null &&  Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(visita.cdCliente)) {
    			VisitaService.getInstance().atualizaClienteDaVisita(visita);
    		}
    	} else if (onWindow) {
    		save();
    		Cliente cliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli);
    		SessionLavenderePda.setCliente(cliente);
    	} else {
    		super.salvarClick();
    	}
    	NovoCliEnderecoService.getInstance().insereEnderecoNovoCliente(getNovoCliente());
    	cadastraCoordenadaNovoCliente();
    	if (fromAnaliseNovoCli) {
    		((CadNovoClienteAnaForm) prevContainer).edit(getDomain());
    	}
    	if (!ValueUtil.isEmpty(contatosCadastrados)) {
    		int size = contatosCadastrados.size();
    		for (int i = 0; i < size; i++) {
				ContatoPdaService.getInstance().insert((Contato) contatosCadastrados.items[i]);
			}
    		contatosCadastrados = null;
		}
    }
    
    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	NovoCliente novoCliente = getNovoCliente();
        btOpcoes.setVisible(isEditing() && novoCliente.isAlteradoPalm());
        btSalvar.setVisible(novoCliente.isAlteradoPalm() || !isEditing() || LavenderePdaConfig.isValidaCadastroDuasEtapas() && novoCliente.isPrimeiraEtapa());
        btExcluir.setVisible(isEditing() && novoCliente.isAlteradoPalm() && !haPedidosNovoCliente() && !fromAnaliseNovoCli && !isExistePesquisaMercadoNovoCliente());
    }

    private boolean cadastraCoordenadaNovoCliente() throws SQLException {
    	NovoCliente novoCli = (NovoCliente) getDomain();
		if (!ValueUtil.VALOR_SIM.equals(novoCli.flCadCoordenadaLiberado) && LavenderePdaConfig.isCadastroCoordenadasGeograficasNovoClienteAutomatico() && novoCli.cdLatitude == 0d && novoCli.cdLongitude == 0d) {
			if (!isEditing()) {
				edit(novoCli);
			}
			Cliente cliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCli);
    		SessionLavenderePda.setCliente(cliente);
			if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
	    		cliente.nuCnpj = edNuCpf.getValue();
	    	}else {
				if (cliente == null) {
					novoCli.nuCnpj = edNuCnpj.getValue();
				} else {
					cliente.nuCnpj = edNuCnpj.getValue();
				}
			}
			if (cliente != null) {
				CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(cliente, false, true);
				cadCoordenadasGeograficasWindow.popup();
				if (!cadCoordenadasGeograficasWindow.cadastrouCoordenada && LavenderePdaConfig.obrigaCadCoordenadasGeograficasNovoCliente) {
					return false;
				}
				if (cadCoordenadasGeograficasWindow.liberadoPorSenha) {
					novoCli.flCadCoordenadaLiberado = ValueUtil.VALOR_SIM;
				}
				novoCli.cdLatitude = cliente.cdLatitude;
				novoCli.cdLongitude = cliente.cdLongitude;
				update(novoCli);
				ClienteService.getInstance().update(cliente);
			}
		}
		return true;
	}

	@Override
    protected void beforeSave() throws SQLException {
    	NovoCliente novoCli = getNovoCliente();
    	NovoCliEnderecoService.getInstance().validateNovoClienteEndereco(novoCli);
    	novoCli.validaQtContato = true;
    	super.beforeSave();
    }

	@Override
	protected boolean delete() throws SQLException {
		BaseDomain domain = screenToDomain();
		if (LavenderePdaConfig.isUsaPesquisaNovoCliente()) {
			Vector pesquisaAppList = PesquisaAppService.getInstance().findAllByNovoCliente((NovoCliente)domain);
			if (pesquisaAppList.size() > 0) {
				if (UiUtil.showConfirmYesNoMessage(Messages.NOVO_CLIENTE_ERRO_EXCLUSAO_PESQUISA) ) {
					PesquisaRespAppService.getInstance().excluiPesquisasNovoCliente(pesquisaAppList);
				} else {
					return false;
				}
			} else if (!UiUtil.showConfirmDeleteMessage(getEntityDescription())) {
				return false;
			}
		} else if (!UiUtil.showConfirmDeleteMessage(getEntityDescription())) {
			return false;
		}
		super.delete(domain);
		FotoNovoClienteService.getInstance().excluiFotosNovoCliente((NovoCliente) domain);
		VisitaService.getInstance().deleteVisitaByNovoCliente((NovoCliente) domain);
		DocumentoAnexoService.getInstance().deleteDocAnexo(DocumentoAnexo.NM_ENTIDADE_NOVOCLIENTE, ((NovoCliente) getDomain()).getRowKey());
		NovoCliEnderecoService.getInstance().deleteRegistrosNovoCliEndereco((NovoCliente) domain);
		if (prevContainer instanceof CadClienteMenuForm) {
			CadClienteMenuForm cadClienteMenuForm = (CadClienteMenuForm) prevContainer;
			cadClienteMenuForm.btTornarCliente.setVisible(true);
		}
		return true;
	}

	private void carregaNovoCliEndereco(boolean isCarregaEndApenasMemoria) throws SQLException {
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		Vector domainList = null;
    		int listSize = 0;
	    	listGridNovoCliEndereco.removeAllContainers();
			if (isCarregaEndApenasMemoria) {
				domainList = ((NovoCliente) getDomain()).novoCliEnderecoList;
			} else {
				domainList = getDomainListNovoCliEndereco();
			}
	    	listSize = domainList.size();
			Container[] all = new Container[listSize];
			if (listSize > 0) {
				BaseListContainer.Item c;
				BaseDomain domain;
				for (int i = 0; i < listSize; i++) {
			        all[i] = c = new BaseListContainer.Item(listGridNovoCliEndereco.getLayout());
			        domain = (BaseDomain)domainList.items[i];
			        c.id = domain.getRowKey();
					c.setID(c.id);
			        c.setItens(getItem(domain));
			        domain = null;
				}
				listGridNovoCliEndereco.addContainers(all);
			}
    	}
    }
    
    private String[] getItem(Object domain) {
    	NovoCliEndereco novoCliEndereco = (NovoCliEndereco) domain;
        String[] item = {
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("DSLOGRADOURO")),
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("DSBAIRRO")),
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("DSCIDADE")),
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("CDUF"))};
        return item;
    }
    
    private BaseDomain getDomainFilterSortableNovoCliEndereco() {
    	NovoCliEndereco novoCliEndereco = new NovoCliEndereco();
    	novoCliEndereco.sortAtributte = sortAtributteListSecundaria;
    	novoCliEndereco.sortAsc = sortAscListSecundaria;
    	return novoCliEndereco;
    }
    
    private Vector getDomainListNovoCliEndereco() throws SQLException {
    	NovoCliente domain = (NovoCliente) getDomain();
    	NovoCliEndereco novoCliEnderecoFilter = (NovoCliEndereco) getDomainFilterSortableNovoCliEndereco();
    	if (ValueUtil.isEmpty(domain.novoCliEnderecoList)) {
    		novoCliEnderecoFilter.cdEmpresa = domain.cdEmpresa;
    		novoCliEnderecoFilter.cdRepresentante = domain.cdRepresentante;
    		novoCliEnderecoFilter.cdNovoCliente = domain.cdNovoCliente;
    		novoCliEnderecoFilter.flOrigemNovoCliente = domain.flOrigemNovoCliente;
    		domain.novoCliEnderecoList = NovoCliEnderecoService.getInstance().findAllByExampleDyn(novoCliEnderecoFilter);
    	}
		NovoCliEndereco.sortAttr = novoCliEnderecoFilter.sortAtributte;
		domain.novoCliEnderecoList.qsort();
		if (novoCliEnderecoFilter.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			domain.novoCliEnderecoList.reverse();
   		}
		return domain.novoCliEnderecoList;
    }
    
	private void edDsCepFocusOut() throws Exception {
		EditText edCep = (EditText) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL);
		if (edCep == null || ValueUtil.isEmpty(edCep.getValue())) return;

		Cep cep = CepUtil.cepOffLine(edCep.getValue());
		boolean cepOffLine = true;
		if (cep == null && LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			cep = new Cep(edCep.getValue());
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}

		Control edLogradouro = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSLOGRADOUROCOMERCIAL);
		Control edBairro = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSBAIRROCOMERCIAL);
		Control edDSCidade = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL);
		Control edCDCidade = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDCIDADECOMERCIAL);
		Control cbUf = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDUFCOMERCIAL );
		Control cbEstado = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL );
		Control edDsTipoLogradouro = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSTIPOLOGRADOUROCOMERCIAL);
		if (cep != null){
			setaValoresNosCamposDinamicos(cep, cepOffLine, edLogradouro, edBairro, edDSCidade, edCDCidade, cbUf, cbEstado, edDsTipoLogradouro);
		}
	}

	private void setaValoresNosCamposDinamicos(Cep cep, boolean cepOffline, Control edLogradouro, Control edBairro, Control edDSCidade, Control edCDCidade, Control cbUf, Control cbEstado, Control edDsTipoLogradouro) throws SQLException {
		if (edLogradouro instanceof EditText) {
			String cdLogradouro = cepOffline ? LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro) : cep.dsLogradouro;
			if (ValueUtil.isNotEmpty(cdLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edLogradouro).setValue(cdLogradouro);
			}
		} else if (edLogradouro instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(cep.cdLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) edLogradouro).setValue(cep.cdLogradouro);
		}
		if (edDsTipoLogradouro instanceof EditText) {
			String dsLogradouro = cepOffline ? LogradouroService.getInstance().getDsTipoLogradouro(cep.cdLogradouro) : cep.dsLogradouro;
			if (ValueUtil.isNotEmpty(dsLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edDsTipoLogradouro).setValue(dsLogradouro);
			}
		}
		if (edBairro instanceof EditText) {
			String dsBairro = cepOffline ? BairroService.getInstance().getDsBairro(cep.cdBairro) : cep.dsBairro;
			if (ValueUtil.isNotEmpty(dsBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edBairro).setValue(dsBairro);
			}
		} else if (edBairro instanceof CampoDinamicoComboBox) {
			String cdBairro = cepOffline ? cep.cdBairro : cep.dsBairro;
			if (ValueUtil.isNotEmpty(cdBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((CampoDinamicoComboBox) edBairro).setValue(cdBairro);
			}
		}
		String dsUf = cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf;
		if (cbUf instanceof EditText && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((EditText) cbUf).setValue(dsUf);
		} else if (cbUf instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) cbUf).setValue(dsUf);
		}
		if (cbEstado instanceof EditText && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((EditText) cbEstado).setValue(dsUf);
		} else if (cbEstado instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) cbEstado).setValue(dsUf);
		}
		if (edDSCidade instanceof EditText) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edDSCidade).setValue(dsCidade);
			}
		} else if (edDSCidade instanceof CampoDinamicoComboBox) {
			setaValorComboDsCidadeRelacionada(cep, cepOffline, edDSCidade, dsUf);
		} else if (edDSCidade instanceof PopUpSearchFilterDyn) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((PopUpSearchFilterDyn) edDSCidade).setValue(dsCidade);
			}
		}
		if (edCDCidade instanceof EditText) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep){
				((EditText) edCDCidade).setValue(dsCidade);
			}
		} else if (edCDCidade instanceof CampoDinamicoComboBox) {
			setaValorComboCdCidadeRelacionada(cep, cepOffline, edCDCidade, dsUf);
		} else if (edCDCidade instanceof PopUpSearchFilterDyn) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((PopUpSearchFilterDyn) edCDCidade).setValue(dsCidade);
			}
		}
	}

	protected void setaValorComboCdCidadeRelacionada(Cep cep, boolean cepOffline, Control edCDCidade, String dsUf) throws SQLException {
		((CampoDinamicoComboBox) edCDCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
		String cdCidade = cepOffline ? cep.cdCidade : cep.dsCidade;
		if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			((CampoDinamicoComboBox) edCDCidade).setValue(cdCidade);
		}
		if (((CampoDinamicoComboBox) edCDCidade).getSelectedIndex() == -1) {
			Cidade cidade = new Cidade();
			cidade.nmCidade = StringUtil.changeStringAccented(cdCidade).toUpperCase();
			Vector list = CidadeService.getInstance().findAllByExample(cidade);
			if (list.size() > 0) {
				cidade = (Cidade) list.items[0];
				if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
					((CampoDinamicoComboBox) edCDCidade).setValue(cidade.cdCidade);
				}
			}
		}
	}

	protected void setaValorComboDsCidadeRelacionada(Cep cep, boolean cepOffline, Control edDSCidade, String dsUf) throws SQLException {
		String cdCidade = cepOffline ? cep.cdCidade : cep.dsCidade;
		((CampoDinamicoComboBox) edDSCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
		if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			((CampoDinamicoComboBox) edDSCidade).setValue(cdCidade);
		}
	}

	private void edDsCepAutoFocusOut(String nmFormatoCepAuto, List<String> dsColunasCepList) throws Exception {
		EditText edCepAuto = (EditText) hashComponentes.get(nmFormatoCepAuto);
		if (edCepAuto == null || ValueUtil.isEmpty(edCepAuto.getValue())) return;
		Cep cep = CepUtil.cepOffLine(edCepAuto.getValue());
		boolean cepOffLine = true;
		if (cep == null) {
			cep = new Cep(edCepAuto.getValue());
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}
		setaValoresNosCampos(dsColunasCepList, cep, cepOffLine);
	}

	private void setaValoresNosCampos(List<String> dsColunasCepList, Cep cep, boolean cepOffline) throws SQLException {
		for (int i = 0; i <= 4; i++) {
			if (!isExisteColunaCepNaTabela(dsColunasCepList, i)) continue;

			Control edCampo = (Control) hashComponentes.get(dsColunasCepList.get(i));
			switch (i) {
			case 0:
				if (edCampo instanceof EditText) {
					((EditText) edCampo).setValue(cepOffline ? LogradouroService.getInstance().getDsTipoLogradouro(cep.cdLogradouro) : cep.dsLogradouro );
				}
				break;
			case 1:
				if (edCampo instanceof EditText) {
					((EditText) edCampo).setValue(cepOffline ? LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro) : cep.dsLogradouro);
				} else if (edCampo instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) edCampo).setValue(cep.cdLogradouro);
				}
				break;
			case 2:
				if (edCampo instanceof EditText) {
					((EditText) edCampo).setValue(cepOffline ? BairroService.getInstance().getDsBairro(cep.cdBairro) : cep.dsBairro);
				} else if (edCampo instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) edCampo).setValue(cepOffline ? cep.cdBairro : cep.dsBairro);
				}
				break;
			case 3:
				if (edCampo instanceof EditText) {
					((EditText) edCampo).setValue(cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade);
				} else if (edCampo instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) edCampo).setValue(cepOffline ? cep.cdCidade : cep.dsCidade);
				} else if (edCampo instanceof PopUpSearchFilterDyn) {
					((PopUpSearchFilterDyn) edCampo).setValue(cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade);
				}
				break;
			case 4:
				if (edCampo instanceof EditText) {
					((EditText) edCampo).setValue(cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf);
				} else if (edCampo instanceof CampoDinamicoComboBox) {
					((CampoDinamicoComboBox) edCampo).setValue(cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf);
				}
				break;
			}
		}

	}
	
	private boolean isExisteColunaCepNaTabela(List<String> dsColunasCepList, int posicao) {
		try {
			String dsColunaCep = dsColunasCepList.get(posicao);
			if (ValueUtil.isNotEmpty(dsColunaCep)) {
				Vector camposList = getConfigPersonCadList();
				for (int i = 0; i < camposList.size(); i++) {
					Campo campo = (Campo) camposList.items[i];
					if (dsColunaCep.equals(campo.nmCampo.toUpperCase())) {
						return true;
					}
				}
			}
		} catch (Throwable ex) {
			return false;
		}
		return false;
	}

	private void btPreencheCamposAutoClick() {
		NovoClienteService.getInstance().validateCnpj(edNuCnpj.getValue());
		UiUtil.showProcessingMessage();
		try {
			JSONObject jsonResponse = SyncManager.consultaCnpjOnline(edNuCnpj.getValue());
			processaRetornoConsultaCnpj(jsonResponse, hashComponentes);
			edNuCnpj.enabledIconEvent = false;
		} catch (Exception ex) {
			UiUtil.showErrorMessage(ex);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}
	
	private void processaRetornoConsultaCnpj(JSONObject jsonResponse, Hashtable hashComponentes) throws SQLException {
		boolean changeCep = false;
		if (jsonResponse != null && hashComponentes != null) {
			Vector camposList = getConfigPersonCadList();
			int camposSize = camposList.size();
			StringBuilder camposComErro = new StringBuilder();
			String vir = "";
			String inscricaoEstadualValue = "";
			EditInscricaoEstadual editInscricaoEstadual = null;
			for (int i = 0; i < camposSize; i++) {
				Campo campo = (Campo) camposList.items[i];
				if (ValueUtil.isNotEmpty(campo.dsTagJson) && campo.isVisivelCad()) {
					Control control = (Control) hashComponentes.get(campo.nmCampo);
					control.clear();
					if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio()) {
						control.setEnabled(true);
					}
					try {
						String value = null;
						if (campo.dsTagJson.contains(".") || campo.dsTagJson.contains("_")) {
							value = JSONUtil.percorreTags(campo.dsTagJson, jsonResponse);
						} else {
							value = jsonResponse.getString(campo.dsTagJson);
						}
						if (control instanceof EditInscricaoEstadual) {
							inscricaoEstadualValue = value;
							editInscricaoEstadual = (EditInscricaoEstadual) control;
						}
						if (value == null || ValueUtil.valueEquals(value,"null")) {
							value = null;
							camposComErro.append(vir).append(campo.dsLabel);
							vir = ", ";
						}
						if (campo.nmCampo.contains("CEP")) {
							changeCep = true;
							edDsCepFocusOut();
						}
						setValueCampoDinamicoJsonTag(control, campo, value);
					} catch (Throwable ex) {
						camposComErro.append(vir).append(campo.dsLabel);
						vir = ", ";
						ExceptionUtil.handle(ex);
					}
				}
			}
			if (ValueUtil.isNotEmpty(camposComErro.toString())) {
				String msgCamposComErro = MessageUtil.getMessage(Messages.NOVOCLIENTE_CONSULTA_WEBSERVICE_ALERTA_NAO_PREENCHIDOS, camposComErro.toString());
				VmUtil.debug(msgCamposComErro);
				VmUtil.debug(jsonResponse.toString());
				if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjNotificaFalhas()) {
					UiUtil.showWarnMessage(msgCamposComErro);
				}
			}
			if (changeCep) {
				try {
					edDsCepFocusOut();
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
			if (editInscricaoEstadual != null) {
				editInscricaoEstadual.setValue(inscricaoEstadualValue);
				if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio()) {
					editInscricaoEstadual.setEnabled(false);
				}
			}
		}
	}
	
	private void setValueCampoDinamicoJsonTag(Control control, Campo campo, String value) {
		if (value != null) {
			if (control instanceof EditTextMask) {
				value = StringUtil.delete(StringUtil.delete(value, '-'),'.');
				if (campo.isDinamico() && value.length() > campo.getNuMaxLen()) {
					value = value.substring(0, campo.getNuMaxLen());
				}
				EditTextMask editMaskControl = (EditTextMask) control;
				editMaskControl.setValue(editMaskControl.clearMask(value));
			} else if (control instanceof EditText) {
				if (campo.isDinamico() && value.length() > campo.getNuMaxLen()) {
					value = value.substring(0, campo.getNuMaxLen());
				}
				((EditText) control).setValue(value);
			} else if (control instanceof EditDate) {
				((EditDate)control).setValue(DateUtil.getDateValue(value));
			} else if (control instanceof EditNumberInt) {
				((EditNumberInt)control).setValue(ValueUtil.getIntegerValue(value));
			} else if (control instanceof EditNumberFrac) {
				((EditNumberFrac)control).setValue(ValueUtil.getDoubleValue(value));
			} else if (control instanceof CampoDinamicoComboBox) {
				((CampoDinamicoComboBox)control).setValue(value);
			} else if (control instanceof LabelValue) {
				((LabelValue) control).setValue(value);
			} else if (control instanceof EditInscricaoEstadual) {
				((EditInscricaoEstadual) control).setValue(value);
			}
			if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio()) {
				control.setEnabled(false);
			}
		}
	}
	
	@Override
	protected void addBarButtons() {
		if (!onWindow) {
			super.addBarButtons();
		}
	}

	@Override
	protected void beforeAdd() throws SQLException {
		if (!showRepresentanteCombo()) {
			throw new ValidationException(Messages.MENUREPRESENTANTE_MSG_SELECIONEANTESACAO);
		}
	}

	private void validaCpfCnpj(EditText ed) throws SQLException {
		if (LavenderePdaConfig.isUsaSistemaIdiomaIngles()) {
			return;
		}
		String cpjCnpj = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? ed.getValue() : ed.getText();
		NovoCliente novoCli = (NovoCliente) getDomain();
		boolean validaCpfCnpjWeb = LavenderePdaConfig.usaValidacaoCPFCNPJBaseWeb;
		try {
			NovoClienteService.getInstance().validaCpfCnpjDuplicado(cpjCnpj, novoCli.oldNuCnpj, false, novoCli.flTipoCadastro);
		} catch (DuplicatedCnpjException e) {
			if (LavenderePdaConfig.usaConfirmacaoCPFCNPJIgualNovoCliente()) {
				if (!UiUtil.showWarnConfirmYesNoMessage(Messages.NOVOCLIENTE_CPFCNPJ_IGUAL_CLIENTE)) {
					if (validaCpfCnpjWeb) {
						getNovoCliente().flStatusCadastro = NovoCliente.FLSTATUSCADASTRO_PENDENTE;
					}
				} else if (validaCpfCnpjWeb) {
					getNovoCliente().flStatusCadastro = NovoCliente.FLSTATUSCADASTRO_CONFIRMADO;
				}
			} else {
				throw new ValidationException(Messages.NOVOCLIENTE_ERRO_CPFCNPJ_REPETIDO);
			}
		} catch (Throwable e) {
			getNovoCliente().flStatusCadastro = ValueUtil.VALOR_NI;
		}
	}

	public void setFromAnaliseNovoCli(boolean fromAnaliseNovoCli) {
		this.fromAnaliseNovoCli = fromAnaliseNovoCli;
	}
	
	
}
