package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.service.FreteConfigService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoFreteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TransportadoraComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwComboBoxListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class ListFreteWindow extends LavendereWmwComboBoxListWindow {

	public Vector listFreteConfig;
	public FreteConfig selectedFretedConfig;
	private TransportadoraComboBox cbTransportadora;
	private TipoFreteComboBox cbTipoFrete;
	public Pedido pedido;
	private Pedido pedidoFiltro;
	private ButtonPopup btFreteManual;
	public boolean freteAplicado = false;
	private boolean firstOppening = true;
	private boolean fromCadManual;
	
	public ListFreteWindow(Pedido pedido) throws SQLException {
		this(pedido, false);
	}
    public ListFreteWindow(Pedido pedido, boolean fromCadManual) throws SQLException {
        super(Messages.FRETE_NOME_ENTIDADE);
        constructorListContainer();
        singleClickOn = true;
        
        this.pedido = pedido;
        this.pedidoFiltro = (Pedido) pedido.clone();
        btFreteManual = new ButtonPopup(Messages.FRETE_PERSONALIZADO_MANUAL);
        this.fromCadManual = fromCadManual;
        createFiltersComponents();
        controlFreteManual();
        setDefaultRect();
    }

    private void createFiltersComponents() throws SQLException {
		cbTransportadora = new TransportadoraComboBox();
		cbTipoFrete = new TipoFreteComboBox();
		cbTransportadora.defaultItemType = TransportadoraComboBox.DefaultItemType_ALL;
		cbTipoFrete.defaultItemType = TipoFreteComboBox.DefaultItemType_ALL;
		carregarCombos();
	}
    
    @Override
    public void initUI() {
    	super.initUI();
    	if (LavenderePdaConfig.permitirFreteManual() && !fromCadManual) {
    		cFundoFooter.add(btFreteManual);
    		ajustaTamanhoBotoes();
    	}
    }

	public void constructorListContainer() {
    	configListContainer(FreteConfig.CAMPOFILTROPRECO);
    	listContainer = new GridListContainer(4, 2);
    	
    	listContainer.setColPosition(1, RIGHT);
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColsSort(new String[][] {{Messages.FRETE_NOME_TRANSPORTADORA, "NMTRANSPORTADORA"},{Messages.FRETE_PERSONALIZADO_MANUAL_PRECO, FreteConfig.CAMPOFILTROPRECO}});
    	listContainer.btResize.setVisible(false);
    }
	
	private void carregarCombos() throws SQLException {
		cbTransportadora.carregaTransportadoras(pedido, LavenderePdaConfig.usaCalculoFretePersonalizado());
		cbTipoFrete.loadTipoFrete(pedido);
		
		if (LavenderePdaConfig.usaFiltroTodosPadrao() && ValueUtil.isEmpty(pedido.cdTransportadora)) {
			cbTransportadora.setSelectedIndex(0);
			cbTipoFrete.setSelectedIndex(0);
		} else {
			cbTransportadora.setValue(pedido.cdTransportadora);
			cbTipoFrete.setValue(pedido.cdTipoFrete, pedido.getCliente() != null ? pedido.getCliente().cdEstadoComercial : TipoFrete.CD_ESTADO_PADRAO);
		}
	}

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return null;
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	if (domain.sortAtributte == null) {
    		domain.sortAtributte = FreteConfig.CAMPOFILTROPRECO;
    		domain.sortAsc = ValueUtil.VALOR_NAO;
    	}
    	if (!controleDeFiltro()) return new Vector();
    	listFreteConfig = FreteConfigService.getInstance().buscaFreteConfigListByPedido(pedidoFiltro, domain);
    	return listFreteConfig;
    }

	private boolean controleDeFiltro() throws SQLException {
		if (firstOppening && emptyFilter()) return false;
		firstOppening = false;
		if (firstOppening && !LavenderePdaConfig.usaFiltroTodosPadrao()) return false;
    	if (firstOppening && LavenderePdaConfig.usaFiltroTodosPadrao()) {
    		pedidoFiltro.cdTipoFrete = null;
    		pedidoFiltro.cdTransportadora = null;
    	}
		return true;
	}

	private boolean emptyFilter() {
		return cbTransportadora.getSelectedIndex() < 0 && cbTipoFrete.getSelectedIndex() < 0;
	}
    
	private void controlFreteManual() throws SQLException {
		btFreteManual.setVisible(LavenderePdaConfig.permitirFreteManual());
		ajustaTamanhoBotoes();
		repaintNow();			
	}
	
	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		FreteConfig freteConfig = (FreteConfig)domain;
		Transportadora transportadora = TransportadoraService.getInstance().getTransportadora(freteConfig.cdTransportadora);
		freteConfig.nmTransportadoraDsFreteConfig = StringUtil.getStringValue(transportadora != null ? transportadora.nmTransportadora + " (" + freteConfig.dsFreteConfig + ")" : freteConfig.dsFreteConfig);
		freteConfig.vlPrecoFreteCalculado = freteConfig.vlPrecoFreteCalculado != null ? freteConfig.vlPrecoFreteCalculado : BigDecimal.ZERO;
		//--
		return new String[] {
				freteConfig.nmTransportadoraDsFreteConfig,
				ValueUtil.VALOR_NI,
				ValueUtil.VALOR_NI,
				StringUtil.getStringValueToInterface(freteConfig.vlPrecoFreteCalculado),
			};
	}

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, new LabelName(Messages.TRANSPORTADORA), cbTransportadora, getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.TIPOFRETE_LABEL_TIPOFRETE), cbTipoFrete, getLeft(), getNextY());
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
    }

    @Override
    public void singleClickInList() throws SQLException {
    	searchSelectedRowKeyInList();
    	boolean apresentaBotaoVerDetalhes = LavenderePdaConfig.exibeDetalhesCalculoFretePersonalizado();
    	String[] buttons = apresentaBotaoVerDetalhes ? new String[] {Messages.BOTAO_CANCELAR, Messages.FRETE_PERSONALIZADO_MANUAL_VER_DETALHES, Messages.FRETE_PERSONALIZADO_MANUAL_APLICAR_FRETE} : new String[] {Messages.BOTAO_CANCELAR, Messages.FRETE_PERSONALIZADO_MANUAL_APLICAR_FRETE};
		String message = MessageUtil.getMessage(Messages.FRETE_PERSONALIZADO_MANUAL_CONFIRMACAO_MSG, new String[] {selectedFretedConfig.nmTransportadoraDsFreteConfig, StringUtil.getStringValueToInterface(selectedFretedConfig.vlPrecoFreteCalculado)});
		int result = UiUtil.showConfirmMessage(selectedFretedConfig.nmTransportadoraDsFreteConfig, message, buttons);
		if (result == 0) {
			return;
		} else if (result == 1 && apresentaBotaoVerDetalhes) {
			new ListFreteDetalhesWindow(selectedFretedConfig).popup();
		} else if (result == 1 && !apresentaBotaoVerDetalhes) {
			aplicaFretePedido();
		} else {
			aplicaFretePedido();
		}
    }
    
    private void aplicaFretePedido() {
    	pedido.cdFreteConfig = selectedFretedConfig.cdFreteConfig;
    	pedido.cdTransportadora = selectedFretedConfig.cdTransportadora;
    	pedido.cdTipoFrete = selectedFretedConfig.cdTipoFrete;
    	pedido.vlFrete = ValueUtil.getDoubleValue(selectedFretedConfig.vlPrecoFreteCalculado);
    	pedido.freteConfig = selectedFretedConfig;
		freteAplicado = true;
    	unpop();
	}

	private void searchSelectedRowKeyInList() throws SQLException {
    	String selectedRowKey = getSelectedRowKey();
    	int size = listFreteConfig.size();
    	for (int i = 0; i < size; i++) {
    		FreteConfig freteConfigListItem = (FreteConfig) listFreteConfig.items[i];
    		if (freteConfigListItem.getRowKey().equalsIgnoreCase(selectedRowKey)) {
    			selectedFretedConfig = freteConfigListItem;
    			break;
    		}
    	}
	}

	//@Override
    protected void onFormEvent(Event event) throws SQLException { 
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbTransportadora) {
					cbTransportadoraChange();
				}
				if (event.target == cbTipoFrete) {
					cbTipoFreteChange();
				}
				if (event.target == btFreteManual) {
					exibirCadFreteManual();
				}
			}
    	}
    }

	private void exibirCadFreteManual() throws SQLException {
		CadFreteManualForm cadFrete = new CadFreteManualForm(pedido, true);
		cadFrete.popup();
		if (cadFrete.adicionadoFreteManual) {
			freteAplicado = true;
			unpop();
		}
	}

	private void cbTipoFreteChange() throws SQLException {
		pedidoFiltro.cdTipoFrete = cbTipoFrete.getValue();
		list();
	}

	private void cbTransportadoraChange() throws SQLException {
		pedidoFiltro.cdTransportadora = cbTransportadora.getValue();
		list();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new FreteConfig();
	}
	
}