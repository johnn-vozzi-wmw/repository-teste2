package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ClienteEnderecoService;
import br.com.wmw.lavenderepda.business.service.NovoCliEnderecoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ClienteEnderecoComboBoxMultiLine;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwComboBoxListWindow;
import totalcross.sys.SpecialKeys;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListClienteEnderecoWindow extends LavendereWmwComboBoxListWindow {
	
	public String cdClienteEntrega;
	public boolean indicaClienteEntrega;
	public boolean isEnderecoCobranca;
	public ButtonPopup btLimpar;
	public ClienteEnderecoComboBoxMultiLine clienteEnderecoComboBoxMultiLine;
	public Pedido pedido;
	
	public ListClienteEnderecoWindow() throws SQLException {
		this(null, false, null);
	}
	
	public ListClienteEnderecoWindow(ClienteEnderecoComboBoxMultiLine clienteEnderecoComboBoxMultiLine, boolean isEnderecoCobranca) {
		this(false, null, isEnderecoCobranca, null);
		this.clienteEnderecoComboBoxMultiLine = clienteEnderecoComboBoxMultiLine;
	}
	
	public ListClienteEnderecoWindow(ClienteEnderecoComboBoxMultiLine clienteEnderecoComboBoxMultiLine, boolean indicaClienteEntrega, String cdClienteEntrega) {
		this(indicaClienteEntrega, cdClienteEntrega, false, null);
		this.clienteEnderecoComboBoxMultiLine = clienteEnderecoComboBoxMultiLine;
	}

	public ListClienteEnderecoWindow(ClienteEnderecoComboBoxMultiLine clienteEnderecoComboBoxMultiLine, boolean indicaClienteEntrega, String cdClienteEntrega, Pedido pedido) {
		this(indicaClienteEntrega, cdClienteEntrega, false, pedido);
		this.clienteEnderecoComboBoxMultiLine = clienteEnderecoComboBoxMultiLine;
	}
	
	public ListClienteEnderecoWindow(boolean indicaClienteEntrega, String cdClienteEntrega, boolean isEnderecoCobranca, Pedido pedido) {
		super(Messages.CLIENTEENDERECO_SELECIONE_ENDERECO);
		this.indicaClienteEntrega = indicaClienteEntrega;
		this.cdClienteEntrega = cdClienteEntrega;
		this.isEnderecoCobranca = isEnderecoCobranca;
		if (pedido != null) {
			setBaseCadWindow(new CadClienteEnderecoWindow(pedido.isPedidoNovoCliente()));
		} else {
			setBaseCadWindow(new CadClienteEnderecoWindow(false));
		}
		this.pedido = pedido;
        this.singleClickOn = true;
        listContainer = new GridListContainer(5, 2);
        configListContainer("DSLOGRADOURO");
        listContainer.setColsSort(new String[][] {{Messages.CLIENTEENDERECO_LABEL_DSLOGRADOURO, "DSLOGRADOURO"}, {Messages.CLIENTEENDERECO_LABEL_DSBAIRRO, "DSBAIRRO"}, {Messages.CLIENTEENDERECO_LABEL_DSCIDADE, "DSCIDADE"}, {Messages.CLIENTEENDERECO_LABEL_DSESTADO, "DSESTADO"}});
        listContainer.setColPosition(1, RIGHT);
        listContainer.setColPosition(3, RIGHT);
        listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
        setDefaultRect();
	}


	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		Vector listClienteEndereco = getCrudService().findAllByExample(domain);
		if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega()) {
			if (listClienteEndereco.size() > 0) {
				listClienteEndereco = ClienteEnderecoService.getInstance().buscaEnderecoAtualizado(listClienteEndereco);
			} else {
				listClienteEndereco.addElement(domain);
				listClienteEndereco = ClienteEnderecoService.getInstance().buscaEnderecoAtualizado(listClienteEndereco);
			}
		}
		if (pedido != null && pedido.isPedidoNovoCliente() && LavenderePdaConfig.isApresentaEnderecoNovoCliente()) {
			listClienteEndereco = new Vector();
			NovoCliEnderecoService.getInstance().buscaEnderecoNovoClienteList(pedido, listClienteEndereco);
		}
		return listClienteEndereco;
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
        return new String[] {
                StringUtil.getStringValue(clienteEndereco.dsLogradouro),
                StringUtil.getStringValue(clienteEndereco.dsBairro),
                StringUtil.getStringValue(clienteEndereco.dsCidade),
                StringUtil.getStringValue(clienteEndereco.dsEstado), 
        	""};
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c != null ? c.id : "";
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ClienteEnderecoService.getInstance();
	}
	
	@Override
	protected void addButtons() {
		super.addButtons();
		addButtonPopup(btLimpar);
	}
	
	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		return super.getSelectedDomain2();
	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btFechar) {
				btFecharClick();
			} else if (event.target == btLimpar) {
				btLimparClick();
			}
			break;
		}
		case KeyEvent.SPECIAL_KEY_PRESS: {
	        if (((KeyEvent)event).key == SpecialKeys.ESCAPE) {
	        	btFecharClick();
			}
	        break;
		}
		}
	}
	
	protected void btLimparClick() throws SQLException {
		clienteEnderecoComboBoxMultiLine.cleanDomainSelected();
		fecharWindow();
		closedByBtFechar = true;
		listContainer.setSelectedIndex(-1);
		
	}
	
	@Override
	protected BaseDomain getDomainFilter() {
		ClienteEndereco domainFilter = new ClienteEndereco();
		domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		domainFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		if (indicaClienteEntrega) {
			domainFilter.cdCliente = ValueUtil.isNotEmpty(cdClienteEntrega) ? cdClienteEntrega : ValueUtil.VALOR_NI;
		} else {
			domainFilter.cdCliente = SessionLavenderePda.getCliente() != null ? SessionLavenderePda.getCliente().cdCliente : ValueUtil.VALOR_NI;
		}
		if (isEnderecoCobranca) {
			domainFilter.flCobranca = ValueUtil.VALOR_SIM;
		} else {			
			domainFilter.flEntrega = ValueUtil.VALOR_SIM;
		}
		return domainFilter;
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL);
	}

	@Override
	public void singleClickInList() throws SQLException {
		if (LavenderePdaConfig.usaMultiplosEnderecosCliente() && (LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() == 2 || (isEnderecoCobranca && LavenderePdaConfig.getConfigUsaEnderecoCobrancaPedido() == 2))) {
			super.singleClickInList();
		} else {
			detalhesClick();
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

	@Override
	protected Image[] getIconsLegend(BaseDomain domain) {
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
	public void updateCurrentRecord(BaseDomain domain) throws java.sql.SQLException {
		list();
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
}
