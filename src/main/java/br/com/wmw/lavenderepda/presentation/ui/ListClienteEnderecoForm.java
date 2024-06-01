package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.service.ClienteEndAtuaService;
import br.com.wmw.lavenderepda.business.service.ClienteEnderecoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListClienteEnderecoForm extends LavendereCrudListForm {
	
	private static final int TABPANEL_ENDERECO_PRINCIPAL = 0;
	private static final int TABPANEL_OUTROS_ENDERECOS = 1;
	private static final int TABPANEL_ATUALIZACOES_ENDERECOS = 2;
	
	private Cliente cliente;
	private SessionContainer containerEndPrincipal;
	private LabelName lbDsLogradouroComercial;
	private LabelValue lvDsLogradouroComercial;
	private LabelName lbCepBairro;
	private LabelValue lvCepBairro;
	private LabelName lbCidadeUf;
	private LabelValue lvCidadeUf;
	private LabelName lbComplemento;
	private LabelValue lvComplemento;
	private ScrollTabbedContainer tabs;
	private GridListContainer listContainerClienteEndAtua;
	public String sortAtributteListSecundaria = null;
	public String sortAscListSecundaria = ValueUtil.VALOR_SIM;
	
    public ListClienteEnderecoForm(Cliente cliente) throws SQLException {
        super(Messages.CLIENTEENDERECO_TITULO_LISTA);
        this.cliente = cliente;
        setBaseCrudCadForm(new CadClienteEnderecoForm(true, false));
        singleClickOn = true;
        listResizeable = false;
        listContainer = new GridListContainer(5, 2);
        configListContainer(ClienteEndereco.NMCOLUNA_CDENDERECO);
        listContainer.setColsSort(new String[][] {{Messages.CLIENTEENDERECO_LABEL_CDENDERECO, ClienteEndereco.NMCOLUNA_CDENDERECO}, {Messages.CLIENTEENDERECO_LABEL_DSLOGRADOURO, ClienteEndereco.NMCOLUNA_DSLOGRADOURO}, {Messages.CLIENTEENDERECO_LABEL_DSBAIRRO, ClienteEndereco.NMCOLUNA_DSBAIRRO}, {Messages.CLIENTEENDERECO_LABEL_DSCIDADE, ClienteEndereco.NMCOLUNA_DSCIDADE}, {Messages.CLIENTEENDERECO_LABEL_DSESTADO, ClienteEndereco.NMCOLUNA_DSESTADO}});
        listContainer.setColPosition(1, RIGHT);
        listContainer.setColPosition(3, RIGHT);
        containerEndPrincipal = new SessionContainer();
        containerEndPrincipal.setBackColor(ColorUtil.formsBackColor);
        lbDsLogradouroComercial = new LabelName(Messages.CLIENTE_LABEL_DSLOGRADOUROCOMERCIAL);
        lvDsLogradouroComercial = new LabelValue();
        lbCepBairro = new LabelName(Messages.CLIENTE_LABEL_CEPBAIRRO);
        lvCepBairro = new LabelValue();
        lbCidadeUf = new LabelName(Messages.CLIENTE_LABEL_CIDADEUF);
        lvCidadeUf = new LabelValue();
        lbComplemento = new LabelName(Messages.CLIENTE_LABEL_COMPLEMENTO);
        lvComplemento = new LabelValue();
        Vector abas = new Vector();
        abas.addElement(Messages.CLIENTEENDERECO_LABEL_ENDERECO_PRINCIPAL);
        abas.addElement(Messages.CLIENTEENDERECO_LABEL_OUTROS_ENDERECOS);
        listContainerClienteEndAtua = new GridListContainer(6, 2);
        listContainerClienteEndAtua.setColsSort(new String[][] {{Messages.CLIENTEENDERECO_LABEL_CDENDERECO, ClienteEndereco.NMCOLUNA_CDENDERECO}, {Messages.CLIENTEENDERECO_LABEL_DSLOGRADOURO, ClienteEndereco.NMCOLUNA_DSLOGRADOURO}, {Messages.CLIENTEENDERECO_LABEL_DSBAIRRO, ClienteEndereco.NMCOLUNA_DSBAIRRO}, {Messages.CLIENTEENDERECO_LABEL_DSCIDADE, ClienteEndereco.NMCOLUNA_DSCIDADE}, {Messages.CLIENTEENDERECO_LABEL_DSESTADO, ClienteEndereco.NMCOLUNA_DSESTADO}});
        listContainerClienteEndAtua.atributteSortSelected = ClienteEndereco.NMCOLUNA_CDENDERECO;
        listContainerClienteEndAtua.sortAsc = sortAscListSecundaria;
        listContainerClienteEndAtua.setColPosition(1, RIGHT);
        listContainerClienteEndAtua.setColPosition(3, RIGHT);
        listContainerClienteEndAtua.setColPosition(5, RIGHT);
        if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente()) {
        	abas.addElement(Messages.CLIENTEENDERECO_LABEL_ATUALIZACOES_ENDERECOS);
        }
        tabs = new ScrollTabbedContainer((String[]) abas.toObjectArray());
        loadDadosEndCliente();
    }
    
    @Override
    protected CrudService getCrudService() throws SQLException {
        return ClienteEnderecoService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() throws SQLException {
		ClienteEndereco domainFilter = new ClienteEndereco();
		domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		domainFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		domainFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		return domainFilter;
	}
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
        return getCrudService().findAllByExampleDyn(domain);
    }
    
    private Vector getDomainListClienteEndAtua() throws SQLException {
    	ClienteEndAtua clienteEndAtuaFilter = (ClienteEndAtua) getDomainFilterSortableClienteEndAtua();
    	clienteEndAtuaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	clienteEndAtuaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	clienteEndAtuaFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	return ClienteEndAtuaService.getInstance().findAllByExample(clienteEndAtuaFilter);
    }
    
    private BaseDomain getDomainFilterSortableClienteEndAtua() {
    	ClienteEndAtua clienteEndAtuaFilter = new ClienteEndAtua();
    	clienteEndAtuaFilter.sortAtributte = sortAtributteListSecundaria;
    	clienteEndAtuaFilter.sortAsc = sortAscListSecundaria;
    	return clienteEndAtuaFilter;
    }
    
    @Override
    protected String[] getItem(Object domain) throws SQLException {
    	ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
        return new String[] {
                StringUtil.getStringValue(clienteEndereco.dsLogradouro),
                StringUtil.getStringValue(clienteEndereco.dsBairro),
                StringUtil.getStringValue(clienteEndereco.dsCidade),
                StringUtil.getStringValue(clienteEndereco.dsEstado),
        	""};
    }
    
    private String[] getItemClienteEndAtua(Object domain) {
    	ClienteEndAtua clienteEndAtua = (ClienteEndAtua) domain;
    	return new String[] {StringUtil.getStringValue(clienteEndAtua.dsLogradouro),
    			StringUtil.getStringValue(clienteEndAtua.dsBairro), 
    			StringUtil.getStringValue(clienteEndAtua.dsCidade), 
    			StringUtil.getStringValue(clienteEndAtua.dsEstado), 
    			StringUtil.getStringValue(clienteEndAtua.dtAtualizacao),
    			StringUtil.getStringValue(clienteEndAtua.getTipoAtualizacao())};
    }

    @Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }
    
    @Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	return getCrudService().findByRowKeyDyn(getSelectedRowKey());
    }
   
    //--------------------------------------------------------------

    @Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, tabs, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
    	Container contEnderecoPrincipal = tabs.getContainer(TABPANEL_ENDERECO_PRINCIPAL);
    	UiUtil.add(contEnderecoPrincipal, containerEndPrincipal, getLeft(), AFTER, FILL, FILL);
    	UiUtil.add(containerEndPrincipal, lbDsLogradouroComercial, lvDsLogradouroComercial, getLeft(), TOP);
    	UiUtil.add(containerEndPrincipal, lbComplemento, lvComplemento, SAME, AFTER);    	
    	UiUtil.add(containerEndPrincipal, lbCepBairro, lvCepBairro, SAME, AFTER);
    	UiUtil.add(containerEndPrincipal, lbCidadeUf, lvCidadeUf, SAME, AFTER);
    	containerEndPrincipal.setRect(KEEP, KEEP, KEEP, getHeighContainerEndPrincipal());
    	Container contOutrosEnderecos = tabs.getContainer(TABPANEL_OUTROS_ENDERECOS);
    	UiUtil.add(contOutrosEnderecos, listContainer, LEFT, AFTER, FILL, FILL);
    	if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente()) {
    		Container contAtualizacoesEnderecos = tabs.getContainer(TABPANEL_ATUALIZACOES_ENDERECOS);
    		UiUtil.add(contAtualizacoesEnderecos, listContainerClienteEndAtua, LEFT, AFTER, FILL, FILL);
    		UiUtil.add(barBottomContainer, btNovo, 5);
    	} 
    }
    
    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    	case PenEvent.PEN_UP: {
    		if (event.target instanceof BaseListContainer.Item && singleClickOn && listContainerClienteEndAtua.isEventoClickUnicoDisparado()) {
    			listContainerClienteEndAtua.setEventoClickUnicoDisparado(false);
    			CadClienteEndAtuaDynForm cadClienteEndAtuaDynForm = new CadClienteEndAtuaDynForm();
    			ClienteEndAtua clienteEndAtua = (ClienteEndAtua) ClienteEndAtuaService.getInstance().findByRowKeyDyn(listContainerClienteEndAtua.getSelectedId());
    			cadClienteEndAtuaDynForm.edit(clienteEndAtua);
    			show(cadClienteEndAtuaDynForm);
    		}
    		break;
    	}
    	case ControlEvent.WINDOW_CLOSED: {
			if (listContainerClienteEndAtua != null && event.target == listContainerClienteEndAtua.popupMenuOrdenacao) {
				if (listContainerClienteEndAtua.popupMenuOrdenacao.getSelectedIndex() != -1) {
					listContainerClienteEndAtua.reloadSortSettings();
					sortAtributteListSecundaria = listContainerClienteEndAtua.atributteSortSelected;
					sortAscListSecundaria = StringUtil.getStringValue(listContainerClienteEndAtua.sortAsc);
					beforeOrder();
					list();
				}
			}
			break;
		}
    	}
    }

    private void loadDadosEndCliente() throws SQLException {
    	if (this.cliente == null) {
    		return;
    	}
    	Cliente cliente = (Cliente) ClienteService.getInstance().findByRowKeyDyn(this.cliente.getRowKey());
    	String nuLogradouroComercial = ValueUtil.isEmpty((String)cliente.getHashValuesDinamicos().get("NULOGRADOUROCOMERCIAL")) ? "" : StringUtil.getStringValue(", " + cliente.getHashValuesDinamicos().get("NULOGRADOUROCOMERCIAL"));
    	String dsEstadoComercial = ValueUtil.isNotEmpty(StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSESTADOCOMERCIAL"))) ? StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSESTADOCOMERCIAL")) : StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("CDESTADOCOMERCIAL"));
    	lvDsLogradouroComercial.setValue(StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSLOGRADOUROCOMERCIAL")) + StringUtil.getStringValue(nuLogradouroComercial));
    	lvCepBairro.setValue(StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCEPCOMERCIAL")) + " / " + StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSBAIRROCOMERCIAL")));
    	lvCidadeUf.setValue(StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCIDADECOMERCIAL")) + " / " + dsEstadoComercial);
    	lvComplemento.setValue(StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCOMPLEMENTOCOMERCIAL")));
    }
    
    private int getHeighContainerEndPrincipal() {
    	return (lbDsLogradouroComercial.getHeight() * 4) + (lvDsLogradouroComercial.getHeight() * 4);
    }
    
    @Override
    protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
    	Image imgComercial = UiUtil.getImage("images/comercial.png");
    	imgComercial.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	Image imgEntrega = UiUtil.getImage("images/entrega.png");
    	imgEntrega.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	Image imgEntregaPadrao = UiUtil.getImage("images/entregaCobrancaPadrao.png");
    	imgEntregaPadrao.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	Image imgCobranca = UiUtil.getImage("images/cobranca.png");
    	imgCobranca.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	Image imgCobrancaPadrao = UiUtil.getImage("images/entregaCobrancaPadrao.png");
    	imgCobrancaPadrao.applyColor2(Color.brighter(ColorUtil.componentsForeColor, 130));
    	ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
    	if (clienteEndereco.isComercial()) {
    		imgComercial.applyColor2(ColorUtil.baseForeColorSystem);
    	}
    	if (clienteEndereco.isEntrega()) {
    		imgEntrega.applyColor2(ColorUtil.baseForeColorSystem);
    	}
    	if (clienteEndereco.isEntregaPadrao()) {
    		imgEntregaPadrao.applyColor2(ColorUtil.baseForeColorSystem);
    	}
    	if (clienteEndereco.isCobranca()) {
    		imgCobranca.applyColor2(Color.darker(Color.ORANGE, 80));
    	}
    	if (clienteEndereco.isCobrancaPadrao()) {
    		imgCobrancaPadrao.applyColor2(Color.darker(Color.ORANGE, 80));
    	}
    	return new Image[] {imgEntregaPadrao, imgEntrega, imgCobranca, imgCobrancaPadrao, imgComercial};
    }
    
    @Override
    public void list() throws SQLException {
    	super.list();
    	if (LavenderePdaConfig.isPermiteGerenciarEnderecosCliente()) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			int listSize = 0;
			Vector domainList = null;
			try {
				listContainerClienteEndAtua.removeAllContainers();
				//--
				domainList = getDomainListClienteEndAtua();
				listSize = domainList.size();
				Container[] all = new Container[listSize];
				//--
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
				        all[i] = c = new BaseListContainer.Item(listContainerClienteEndAtua.getLayout());
				        domain = (BaseDomain)domainList.items[i];
				        c.id = domain.getRowKey();
				        c.setItens(getItemClienteEndAtua(domain));
				        c.setToolTip(getToolTip(domain));
				        domain = null;
					}
					listContainerClienteEndAtua.addContainers(all);
				}
			} finally {
				domainList = null;
				msg.unpop();
			}
		}
    }
    
}
