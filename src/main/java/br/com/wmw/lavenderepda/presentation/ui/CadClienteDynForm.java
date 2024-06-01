package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ClassUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteSocio;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.StatusCliente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ClienteSocioService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.business.service.NotificacaoPdaService;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class CadClienteDynForm extends BaseLavendereCrudPersonCadForm {

	private static int TAB_CLIENTE_SOCIO;
	
    private LabelValue edStatus;
    private LabelValue edDtVencimentoAlvara;
    private ButtonGroupBoolean btRecebeEmail;
    private ButtonGroupBoolean btRecebeSMS;
    private GridListContainer listContainer;
    private Container container;
    private Control lbMarcadores;
    private int iconSize;
    
    public CadClienteDynForm() {
        super(Messages.CLIENTE_SUBTITULO_DADOS);
        edStatus = new LabelValue("");
        edDtVencimentoAlvara = new LabelValue("");
        container = new Container();
        setReadOnly();
        if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail || LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
        	btSalvar.setVisible(true);
        	if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
        		btRecebeEmail = new ButtonGroupBoolean();
        	}
        	if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
        		btRecebeSMS = new ButtonGroupBoolean();
        	}
        }
        if (LavenderePdaConfig.apresentaListaSociosClienteNosDetalhes) {
        	constructorListContainer();
        }
        iconSize = edStatus.getPreferredHeight();
    }
    
    private void constructorListContainer() {
    	listContainer = new GridListContainer(5, 2);
    	listContainer.setColsSort(new String[][]{{Messages.CLIENTESOCIO_NOME_RAZAOSOCIAL, "NMRAZAOSOCIAL"}, {Messages.NOVOCLIENTE_CNPJ, "NUCNPJ"},
    		{Messages.CLIENTESOCIO_DATA_ENTRADA, "DTENTRADASOCIO"}, {Messages.CLIENTESOCIO_PERCENTUAL_PARTICIPACAO, "VLPCTPARTICIPACAO"}});
    	listContainer.setColPosition(3, RIGHT);
    }

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    protected String getDsTable() throws SQLException {
    	return Cliente.TABLE_NAME;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ClienteService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Cliente();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
    	if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail && ValueUtil.isNotEmpty(btRecebeEmail.getValue())) {
    		Cliente cliente = (Cliente) getDomain();
        	cliente.flRecebeEmail = btRecebeEmail.getValue();
			NotificacaoPdaService.getInstance().createNotificacaoPdaReferenteAlteracaoFlRecebeEmail(cliente.cdEmpresa, cliente.cdCliente, cliente.flRecebeEmail);
    	}
    	if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente && ValueUtil.isNotEmpty(btRecebeSMS.getValue())) {
    		Cliente cliente = (Cliente) getDomain();
        	cliente.flRecebeSMS = btRecebeSMS.getValue();
			NotificacaoPdaService.getInstance().createNotificacaoPdaReferenteAlteracaoFlRecebeSMS(cliente.cdEmpresa, cliente.cdCliente, cliente.flRecebeSMS);
    	}
        return super.screenToDomain();
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	Cliente cliente = (Cliente) domain;
    	super.domainToScreen(domain);
    	edStatus.setValue(ValueUtil.isNotEmpty(cliente.getFlStatusCliente()) ? StatusCliente.getDsStatusCliente(cliente.getFlStatusCliente()) : cliente.getFlStatusCliente());
        controlColorEdStatus(cliente);
        if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado()) {
        	if (ValueUtil.isEmpty(cliente.dtVencimentoAlvara)) {
        		edDtVencimentoAlvara.setForeColor(ColorUtil.softRed);
        		edDtVencimentoAlvara.setValue(Messages.PEDIDO_LABEL_STATUSALVARA_NAO);
        	} else if (cliente.isAlvaraVigente()) {
        		edDtVencimentoAlvara.setForeColor(ColorUtil.componentsForeColor);
        		edDtVencimentoAlvara.setValue(cliente.dtVencimentoAlvara.toString());
        	} else {
        		edDtVencimentoAlvara.setForeColor(ColorUtil.softRed);
        		edDtVencimentoAlvara.setValue(cliente.dtVencimentoAlvara.toString());
        	}
        }
        if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
        	Cliente cli = (Cliente) ClienteService.getInstance().findByRowKey(cliente.getRowKey());
        	btRecebeEmail.setValue(cli.flRecebeEmail);
        }
        if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
        	Cliente cli = (Cliente) ClienteService.getInstance().findByRowKey(cliente.getRowKey());
        	btRecebeSMS.setValue(cli.flRecebeSMS);
        }
    }

	private void controlColorEdStatus(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.grifaClienteBloqueado && (StatusCliente.STATUSCLIENTE_CDBLOQUEADO.equals(cliente.getFlStatusCliente()))) {
			edStatus.setForeColor(ColorUtil.softRed);
		} else {
			edStatus.setForeColor(ColorUtil.componentsForeColor);
		}
	}

    //@Override
    protected void clearScreen() throws SQLException {
    	super.clearScreen();
        edStatus.setText("");
        edDtVencimentoAlvara.setText("");
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	super.onFormEvent(event);
    	if (event.type == ControlEvent.WINDOW_CLOSED) {
    		if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
				if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
					listContainer.reloadSortSettings();
					saveListConfig();
					listClienteSocio();
				}
			}
    	}
    }
    
    protected String getConfigClassName() {
		return ClassUtil.getSimpleName(ClienteSocio.class);
	}
    
    private ConfigInterno getDomainConfig() {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
		configInterno.vlChave = getConfigClassName();
		configInterno.vlConfigInterno = listContainer.atributteSortSelected + ConfigInterno.defaultSeparatorInfoValue + StringUtil.getStringValue(listContainer.sortAsc);
		return configInterno;
	}
    
    private void saveListConfig() throws SQLException {
    	ConfigInterno configInteno = getDomainConfig();
    	ListContainerConfig.listasConfig.put(configInteno.vlChave, StringUtil.split(configInteno.vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
    	if (ConfigInternoService.getInstance().findByRowKey(configInteno.getRowKey()) == null) {
    		ConfigInternoService.getInstance().insert(configInteno);
    	} else {
    		ConfigInternoService.getInstance().update(configInteno);
    	}
    }
    
    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	if (LavenderePdaConfig.apresentaListaSociosClienteNosDetalhes) {
    		setSortAttributeFromConfInterno();
    		listContainer.atualizaTamanhoComponentesBarraSuperior();
    		listClienteSocio();
    	}
    }
    
    @Override
    protected void addTabsFixas(Vector tableTitles) throws SQLException {
    	if (LavenderePdaConfig.apresentaListaSociosClienteNosDetalhes) {
    		int index = tableTitles.indexOf(Messages.CONTATO_NOME_ENTIDADE);
    		index = index < 0 ? tableTitles.indexOf(Messages.CONTATO_NOME_ENTIDADE.toLowerCase()) : index;
    		if (index >= 0) {
    			tableTitles.insertElementAt(Messages.CLIENTE_ABA_SOCIOS, (TAB_CLIENTE_SOCIO = index + 1));
    		} else {
    			tableTitles.addElement(Messages.CLIENTE_ABA_SOCIOS);
    			TAB_CLIENTE_SOCIO = tableTitles.size() - 1;
    		}
    	}
    }

    @Override
    protected void refreshComponents() throws SQLException {
    	super.refreshComponents();
    	container.removeAll();
    	clearMarcadores();
    	adicionaMarcadoresTela();
    	repositionChildren();
    }
    
    @Override
	protected void addComponentesFixosFim() throws SQLException {
		Container cPrincipal = getContainerPrincipal();
		if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado()) {
			UiUtil.add(cPrincipal, new LabelName(Messages.CLIENTE_LABEL_DTVENCIMENTOALVARA), edDtVencimentoAlvara, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (!LavenderePdaConfig.usaCamposDinamicosMontagemDetalhesCliente) {
			UiUtil.add(cPrincipal, new LabelName(Messages.CLIENTE_LABEL_STATUS), edStatus, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
			UiUtil.add(cPrincipal, new LabelName(Messages.CLIENTE_LABEL_RECEBE_EMAIL), btRecebeEmail, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
			UiUtil.add(cPrincipal, new LabelName(Messages.CLIENTE_LABEL_RECEBE_SMS), btRecebeSMS, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.apresentaListaSociosClienteNosDetalhes) {
			Container c = tabDinamica.getContainer(TAB_CLIENTE_SOCIO);
			UiUtil.add(c, listContainer, LEFT, TOP , FILL, FILL);
		}
	}

	private void adicionaMarcadoresTela() throws SQLException {
		if (!LavenderePdaConfig.apresentaIndicadoreDetelhesCliente()) return;
		
		Container cPrincipal = getContainerPrincipal();
		UiUtil.add(cPrincipal, lbMarcadores = new LabelName(Messages.CLIENTE_MARCADORES), getLeft(), AFTER + HEIGHT_GAP);
		lbMarcadores.appId = 1;
		Vector marcadores = MarcadorService.getInstance().buscaMarcadoresPorCliente(((Cliente) getDomain()));
		for (int i = 0; i < marcadores.size(); i++) {
			Marcador marcador = (Marcador) marcadores.items[i];
			LabelValue label = new LabelValue(marcador.dsMarcador != null ? marcador.dsMarcador : "");
			label.appId = 1;
			if (marcador.imMarcadorAtivo == null) {
				UiUtil.add(cPrincipal, label, LEFT + WIDTH_GAP_BIG, AFTER);
				continue;
			}
			try {
				Image image = UiUtil.getImage(marcador.imMarcadorAtivo);
				image = UiUtil.getSmoothScaledImage(image, iconSize, iconSize);
				ImageControl img = new ImageControl(image);
				img.appId = 1;
				label.split(cPrincipal.getWidth() - img.getWidth() - (WIDTH_GAP_BIG * 5));
				UiUtil.add(cPrincipal, img, getLeft(), AFTER + HEIGHT_GAP);
			} catch (ApplicationException ex) {
				ExceptionUtil.handle(ex);
			}
			UiUtil.add(cPrincipal, label, AFTER + WIDTH_GAP_BIG, SAME);
		}
	}

	private String[] getItem(BaseDomain domain) {
		ClienteSocio clienteSocio = (ClienteSocio) domain;
		String[] item = new String[] {
		StringUtil.getStringValue(clienteSocio.nmRazaoSocial), "", MessageUtil.getMessage(Messages.CLIENTESOCIO_CPF_CNPJ_LIST, StringUtil.getStringValue(clienteSocio.nuCnpj)),
		MessageUtil.getMessage(Messages.CLIENTESOCIO_DATA_ENTRADA_LIST, StringUtil.getStringValue(clienteSocio.dtEntradaSocio)),
		MessageUtil.getMessage(Messages.CLIENTESOCIO_PERCENTUAL_PARTICIPACAO_LIST , StringUtil.getStringValueToInterface(clienteSocio.vlPctParticipacao))
		};
		return item;
	}
	
	public void listClienteSocio() throws SQLException {
		if (listContainer != null) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			int listSize = 0;
			Vector domainList = null;
			try {
				listContainer.removeAllContainers();
				listContainer.uncheckAll();
				domainList = getClienteSocioList();
				listSize = domainList.size();
				Container[] all = new Container[listSize];
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
						if (i % 250 == 0)
							VmUtil.executeGarbageCollector();
						all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
						domain = (BaseDomain) domainList.items[i];
						c.id = domain.getRowKey();
						c.setItens(getItem(domain));
						c.setToolTip(((ClienteSocio)domain).nmRazaoSocial);
					}
					listContainer.addContainers(all);
				}
			} finally {
				domainList = null;
				msg.unpop();
			}
		}
	}
	
	private ClienteSocio getClienteSocioFilterSortable() throws SQLException {
		ClienteSocio clienteSocio = new ClienteSocio();
		Cliente cliente = (Cliente)getDomain();
		clienteSocio.cdEmpresa = cliente.cdEmpresa;
		clienteSocio.cdCliente = cliente.cdCliente;
		clienteSocio.cdRepresentante = cliente.cdRepresentante;
		clienteSocio.sortAtributte = listContainer.atributteSortSelected;
		clienteSocio.sortAsc = listContainer.sortAsc;
		return clienteSocio;
	}
	
	private Vector getClienteSocioList() throws SQLException {
		return ClienteSocioService.getInstance().findAllByExample(getClienteSocioFilterSortable());
	}
	
	private void setSortAttributeFromConfInterno() throws SQLException {
		ConfigInterno configInterno = (ConfigInterno)ConfigInternoService.getInstance().findByPrimaryKey(getDomainConfig());
		if (configInterno != null && ValueUtil.isNotEmpty(configInterno.vlConfigInterno)) {
			String[] vlConfigInterno = configInterno.vlConfigInterno.split(String.valueOf(ConfigInterno.defaultSeparatorInfoValue));
			listContainer.atributteSortSelected = vlConfigInterno[0];
			listContainer.sortAsc = vlConfigInterno[1];
		} else {
			listContainer.atributteSortSelected = "NMRAZAOSOCIAL";
			listContainer.sortAsc = ValueUtil.VALOR_SIM;
		}
	}
	
	private void clearMarcadores() {
		Container cPrincipal = getContainerPrincipal();
		Control next = null;
		for (Control control = lbMarcadores; control != null;) {
			next = control.getNext();
			if (control.appId != 1) {
				break;
			}
			cPrincipal.remove(control);
			control = next;
		}
		lbMarcadores = null;
	}
	
}