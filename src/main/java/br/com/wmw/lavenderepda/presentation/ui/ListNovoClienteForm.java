package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListNovoClienteForm extends LavendereCrudListForm {
	
	private boolean onPedidoSemCliente;
	public ListNovoClienteWindow novoClienteFilterWindow;
	private RepresentanteSupervComboBox cbRepresentantesSupervisor;
	private ButtonAction btNovoPedido;
	private ButtonAction btNovaPesquisaMercado;
	private ButtonAction btCadastrarCoordenada;
	private Pedido pedido;
	private boolean onPesquisaMercSemCli;
	private boolean onCadCoordenadaSemCli;
	
	public ListNovoClienteForm(boolean onPedidoSemCliente, Pedido pedido) throws SQLException {
        super(Messages.NOVOCLIENTE_NOME_ENTIDADE);
        setBaseCrudCadForm(new CadNovoClienteForm());
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor = new RepresentanteSupervComboBox();
        }
        btNovoPedido = new ButtonAction(Messages.BOTAO_NOVO_PEDIDO, "images/novopedido.png");
        btNovaPesquisaMercado = new ButtonAction(Messages.PESQUISAMERCADO_NOME_ENTIDADE_ABREV, "images/pesquisaMercado.png");
        btCadastrarCoordenada = new ButtonAction(Messages.CAD_COORD_AREV, "images/gps.png");
        singleClickOn = true;
        constructorListContainer();
		this.onPedidoSemCliente = onPedidoSemCliente;
		this.pedido = pedido;
    }

    public ListNovoClienteForm() throws SQLException {
       this(false, null);
    }

    public ListNovoClienteForm(String cdTipoCadastro) throws SQLException {
    	this(false, null);
    	if (ListNovoClienteWindow.PESQUISAMERCSEMCLI.equals(cdTipoCadastro)) {
    		this.onPesquisaMercSemCli = true;
    	} else if (ListNovoClienteWindow.CADCOORDENADASEMCLI.equals(cdTipoCadastro)) {
    		this.onCadCoordenadaSemCli = true;
    	}
    }
    
    //@Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	StringBuffer tooltip = new StringBuffer();
    	NovoCliente novoCliente = (NovoCliente) domain;
    	if (LavenderePdaConfig.novoClienteExibeNomeFantasia() && LavenderePdaConfig.novoClienteExibeRazaoSocial()) {
    		tooltip.append(StringUtil.getStringValue(novoCliente.nmFantasia)).append(" - ");
    		tooltip.append(StringUtil.getStringValue(novoCliente.nmRazaoSocial));
    	} else if (LavenderePdaConfig.novoClienteExibeNomeFantasia()) {
    		tooltip.append(StringUtil.getStringValue(novoCliente.nmFantasia));
    	} else if (LavenderePdaConfig.novoClienteExibeRazaoSocial()) {
    		tooltip.append(StringUtil.getStringValue(novoCliente.nmFantasia));  		
    	}
    	
    	return tooltip.toString();
    }


	//@Override
    protected CrudService getCrudService() throws SQLException {
        return NovoClienteService.getInstance();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
    	NovoCliente novoClienteFilter = new NovoCliente();
    	novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
   		carregaRepresentante(novoClienteFilter);
   		novoClienteFilter.filtraNaoOcultos = LavenderePdaConfig.marcaNovoClienteParaAnalise;
    	return getCrudService().findAllByExample(novoClienteFilter);
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	NovoCliente novoClienteFilter = (NovoCliente) domain;
    	novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
   		carregaRepresentante(novoClienteFilter);
    	novoClienteFilter.filtraNaoOcultos = LavenderePdaConfig.marcaNovoClienteParaAnalise;
    	return super.getDomainList(novoClienteFilter);
    }

	public void loadDefaultFilters() throws SQLException {
        if (SessionLavenderePda.isUsuarioSupervisor()) {
    		cbRepresentantesSupervisor.setSelectedIndex(0);
        	if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentantesSupervisor.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    			if (ValueUtil.isEmpty(cbRepresentantesSupervisor.getValue())) {
    				cbRepresentantesSupervisor.setSelectedIndex(0);
    			}
    		}
        }
        list();
    }
	
	private void carregaRepresentante(NovoCliente novoClienteFilter) {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
   			novoClienteFilter.cdRepresentante = cbRepresentantesSupervisor.getValue();
   		} else {
   			novoClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
   		}
	}
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	NovoCliente novoCliente = (NovoCliente) domain;
    	Vector item = new Vector();
    	if (LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores) {
    		item.addElement(StringUtil.getStringValue(getCpfCnpjFormatado(novoCliente)));
    	} else {
    		item.addElement(StringUtil.getStringValue(novoCliente.nuCnpj));    	
    	}
    	 
    	item.addElement(LavenderePdaConfig.novoClienteExibeRazaoSocial() ? "  -  " + StringUtil.getStringValue(novoCliente.nmRazaoSocial) : "");

    	item.addElement(LavenderePdaConfig.novoClienteExibeNomeFantasia() ? StringUtil.getStringValue(novoCliente.nmFantasia) : "");
    	
    	item.addElement(LavenderePdaConfig.novoClienteExibeDataCadastro() ? StringUtil.getStringValue(novoCliente.dtCadastro) : "");
    	
    	return (String[]) item.toObjectArray();
    }
    
    private String getCpfCnpjFormatado(NovoCliente novoCliente) {
    	if (novoCliente != null) {
    		if (LavenderePdaConfig.isUsaSistemaIdiomaIngles()) {
    			return novoCliente.nuCnpj;
    		} else if (Messages.TIPOPESSOA_FLAG_JURIDICA.equals(novoCliente.flTipoPessoa)) {
    			return LavenderePdaConfig.isUsaSistemaIdiomaEspanhol() ? getRucFormatado(novoCliente.nuCnpj) : getCnpjFormatado(novoCliente.nuCnpj);
    		} else {
    			return LavenderePdaConfig.isUsaSistemaIdiomaEspanhol() ? novoCliente.nuCnpj : getCpfFormatado(novoCliente.nuCnpj);
    		}
    	}
    	return "";
    }
    
    private String getRucFormatado(String nuRuc) {
    	StringBuffer rucFormatado = new StringBuffer();
    	if (ValueUtil.isEmpty(nuRuc)) {
    		return rucFormatado.toString();
    	}
    	rucFormatado.append(nuRuc.substring(0, 7));
    	rucFormatado.append("-");
    	rucFormatado.append(nuRuc.substring(8));
		return rucFormatado.toString();
	}
 
	private String getCpfFormatado(String cpf) {
    	StringBuffer cpfFormatado = new StringBuffer();
    	if (ValueUtil.isNotEmpty(cpf)) {
	    	cpfFormatado.append(cpf.substring(0, 3));
	    	cpfFormatado.append(".");
	    	cpfFormatado.append(cpf.substring(3, 6));
	    	cpfFormatado.append(".");
	    	cpfFormatado.append(cpf.substring(6, 9));
	    	cpfFormatado.append("-");
	    	cpfFormatado.append(cpf.substring(9));
    	}
    	return cpfFormatado.toString();
    }
    
    private String getCnpjFormatado(String cnpj) {
    	StringBuffer cnpjFormatado = new StringBuffer();
    	if (ValueUtil.isNotEmpty(cnpj)) {
    		cnpjFormatado.append(cnpj.substring(0, 2));
    		cnpjFormatado.append(".");
    		cnpjFormatado.append(cnpj.substring(2, 5));
    		cnpjFormatado.append(".");
    		cnpjFormatado.append(cnpj.substring(5, 8));
    		cnpjFormatado.append("/");
    		cnpjFormatado.append(cnpj.substring(8, 12));
    		cnpjFormatado.append("-");
    		cnpjFormatado.append(cnpj.substring(12));
    	}
    	return cnpjFormatado.toString();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new NovoCliente();
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
    }
    
    @Override
    public void visibleState() {
    	super.visibleState();
    	btNovoPedido.setVisible(LavenderePdaConfig.isPermiteInformarNovoClienteFechamentoPedido() && !onPedidoSemCliente);
    }
    
    @Override
    protected void onFormStart() throws SQLException {
    	int yPos = getTop();
    	if (!onPedidoSemCliente && !onPesquisaMercSemCli && !onCadCoordenadaSemCli) {
    		int pos = 1;
    		if (LavenderePdaConfig.isUsaCadastroNovoCliente()) {
    			if (!LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
    				UiUtil.add(barBottomContainer, btNovo, 5);
    			}
    			if (!LavenderePdaConfig.isApresentaEnderecoNovoCliente()) {
	    			UiUtil.add(barBottomContainer, btNovoPedido, pos);
	    			pos++;
    			}
    		}
    		if (LavenderePdaConfig.permitePesquisaMercadoNovoCliente && !(LavenderePdaConfig.obrigaCadastroFotoProdutoNaPesquisaDeMercado() || LavenderePdaConfig.obrigaCadastroCoordenadaNaPesquisaDeMercado())) {
    			UiUtil.add(barBottomContainer, btNovaPesquisaMercado, pos);
    			pos++;
    		}
    		if (LavenderePdaConfig.isShowBtCadastroCoordenadasGeograficasNovoCliente()) {
    			UiUtil.add(barBottomContainer, btCadastrarCoordenada, pos);
    		}
    	}
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentantesSupervisor, getLeft(), yPos + HEIGHT_GAP);
    		yPos = AFTER + HEIGHT_GAP;
    	}
    	UiUtil.add(this, listContainer, LEFT, yPos, FILL, FILL - (onPedidoSemCliente || onPesquisaMercSemCli || onCadCoordenadaSemCli ? 0 : barBottomContainer.getHeight()));
    	if (onPedidoSemCliente || onPesquisaMercSemCli || onCadCoordenadaSemCli) {
    		barTopContainer.setVisible(false);
    		barBottomContainer.setVisible(false);
    	}
    }
    
    private void constructorListContainer() {
    	ScrollPosition.AUTO_HIDE = false;
    	configListContainer("NUCNPJ");
    	montarGrid();
    	ScrollPosition.AUTO_HIDE = true;
    }

    private void montarGrid() {
		if(LavenderePdaConfig.novoClienteExibeNomeFantasia() 
				||	LavenderePdaConfig.novoClienteExibeDataCadastro()) {
	    	listContainer = new GridListContainer(4, 2);
			listContainer.setColPosition(1, FILL);
			listContainer.setColPosition(3, RIGHT);
		}
		else {
	    	listContainer = new GridListContainer(2, 2);
			listContainer.setColPosition(1, RIGHT);
		}
		listContainer.resizeable = false;
    	listContainer.btResize.setVisible(false);
    	listContainer.setColsSort(getSort());
		
	}

	private String[][] getSort() {
		List<String[]> itens = new ArrayList<>();

		itens.add(new String[] {Messages.NOVOCLIENTE_LABEL_NUCNPJ, "NUCNPJ"});
		
		if(LavenderePdaConfig.novoClienteExibeRazaoSocial())
			itens.add(new String[] {Messages.NOVOCLIENTE_LABEL_RSOCIAL, "NMRAZAOSOCIAL"});
		
		if(LavenderePdaConfig.novoClienteExibeNomeFantasia())
			itens.add(new String[] {Messages.NOVOCLIENTE_LABEL_NMFANTASIA, "NMFANTASIA"});
		
		if(LavenderePdaConfig.novoClienteExibeDataCadastro())
			itens.add(new String[] {Messages.NOVOCLIENTE_LABEL_DTCADASTRO, "DTCADASTRO"});
		
		String[][] sort = new String[itens.size()][];
		for (int i = 0; i < itens.size(); i++) {
			sort[i] = itens.get(i);
		}
		
		return sort;
	}

	@Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	return getCrudService().findByRowKeyDyn(getSelectedRowKey());
    }

    public void setNovoClienteForm(ListNovoClienteWindow listNovoClienteWindow) {
    	this.novoClienteFilterWindow = listNovoClienteWindow;
    }
    
    protected int getTop() {
    	if (onPedidoSemCliente || onPesquisaMercSemCli || onCadCoordenadaSemCli) {
    		return TOP;
    	} else {
    		return super.getTop();
    	}
    }
    
    protected int getBottom() {
    	if (onPedidoSemCliente || onPesquisaMercSemCli || onCadCoordenadaSemCli) {
    		return FILL - HEIGHT_GAP_BIG;
    	} else {
    		return FILL - barBottomContainer.getHeight() - HEIGHT_GAP;
    	}
    }
    
    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btNovoPedido) {
	    			btNovoPedidoClick();
	    		} else if (event.target == btCadastrarCoordenada) {
	    			btCadastrarCoordenadaClick();
	    		} else if (event.target == btNovaPesquisaMercado) {
	    			btNovaPesquisaMercadoClick();
	    		} else if (event.target == cbRepresentantesSupervisor) {
	    			cbRepresentanteClick();
	    		}
	    		break;
	    	}
    	}
    }
    
    private void cbRepresentanteClick() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		if (cbRepresentantesSupervisor.getSelectedItem() != null) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentantesSupervisor.getSelectedItem()).representante);
		}
    	try {
    		list();
    	} finally {
    		mb.unpop();
    	}
	}

	private void btCadastrarCoordenadaClick() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			Cliente clienteDefault = ClienteService.getInstance().getClienteDefault(Cliente.CD_CLIENTE_DEFAULT_PARA_CADASTRO_COORDENADA, Cliente.NM_CLIENTE_DEFAULT);
			SessionLavenderePda.setCliente(clienteDefault);
			CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(SessionLavenderePda.getCliente(), true, false);
    		cadCoordenadasGeograficasWindow.popup();
    		list();
		} finally {
			mb.unpop();
		}
	}

	private void btNovaPesquisaMercadoClick() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			Cliente clienteDefault = ClienteService.getInstance().getClienteDefault(Cliente.CD_CLIENTE_DEFAULT_PARA_NOVA_PESQUISA_MERCADO, Cliente.NM_CLIENTE_DEFAULT);
			SessionLavenderePda.setCliente(clienteDefault);
			CadClienteMenuForm cadClienteMenuForm = new CadClienteMenuForm();
			cadClienteMenuForm.btPesquisaMercadoClick();
		} finally {
			mb.unpop();
		}
	}

	private void btNovoPedidoClick() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			Cliente clienteDefault = ClienteService.getInstance().getClienteDefault(Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO, Cliente.NM_CLIENTE_DEFAULT);
			SessionLavenderePda.setCliente(clienteDefault);
			CadClienteMenuForm cadClienteMenuForm = new CadClienteMenuForm();
			cadClienteMenuForm.btNovoPedidoClick();
		} finally {
			mb.unpop();
		}
	}
    
    //@Override
    public void onFormExibition() throws SQLException {
    	loadDefaultFilters();
    	list();
    	super.onFormExibition();
    }
    
    public void singleClickInList() throws SQLException {
    	if (onPedidoSemCliente) {
    		Cliente cliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente((NovoCliente) getSelectedDomain());
    		SessionLavenderePda.setCliente(cliente);
    		if (cliente.cdCliente.equals(cliente.nuCnpj)) {
				NovoCliente novoCliente = NovoClienteService.getInstance().getNovoClienteByCliente(cliente);
				if (!novoCliente.isAlteradoPalm() && !LavenderePdaConfig.isIgnoraBloqueioPedidoNovoCliente()) {
					UiUtil.showErrorMessage(Messages.NOVOCLIENTE_MSG_PEDIDO_BLOQUEADO);
					return;
				}
			}
    		Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
    		pedido.setCliente(SessionLavenderePda.getCliente());
    		pedido.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    		if (visita != null &&  Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(visita.cdCliente)) {
    			VisitaService.getInstance().atualizaClienteDaVisita(visita);
    		}
    		novoClienteFilterWindow.unpop();
    	} else if (onPesquisaMercSemCli || onCadCoordenadaSemCli) {
    		Cliente cliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente((NovoCliente) getSelectedDomain());
    		NovoClienteService.getInstance().validaNovoClienteConfirmadoErp(cliente, onPesquisaMercSemCli);
    		SessionLavenderePda.setCliente(cliente);
    		novoClienteFilterWindow.unpop();
		} else {
			NovoCliente novoClienteFilter = (NovoCliente) getSelectedDomain();
			SessionLavenderePda.setRepresentante(novoClienteFilter.cdRepresentante, RepresentanteService.getInstance().getDescription(novoClienteFilter.cdRepresentante));
			super.singleClickInList();
		}
    }
    
}
