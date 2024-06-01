package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilter;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Regiao;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.TransportadoraReg;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.RegiaoService;
import br.com.wmw.lavenderepda.business.service.TipoFreteService;
import br.com.wmw.lavenderepda.business.service.TransportadoraRegService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RegiaoDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwComboBoxListWindow;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListTransportadoraRegWindow extends LavendereWmwComboBoxListWindow {

	private Pedido pedido;
	private ButtonPopup btSelecionar;
	private int indexTranspPreferencial;
	private int indexList;
	private boolean transpPreferencialCif;
	private boolean filtroAllRegiao;
	private boolean filtroCdRegiao;
	private String cdRegiao;
	public boolean selecionouTransportadora;
	private Vector transportadoraRegDisponiveisList;
	private EditFiltro edFiltro;
	private PopUpSearchFilter popUpFiltro;
	private Transportadora transportadoraRelacionada;
	
    public ListTransportadoraRegWindow(Pedido pedido) {
        super(Messages.TRANSPORTADORAREG_TITULO_LIST);
        this.pedido = pedido;
        edFiltro = new EditFiltro("999999999", 50);
        listContainer = new GridListContainer(5, 2);
        if (!LavenderePdaConfig.isOcultaComboTransportadoraPorRegiao()) {
        	popUpFiltro = new PopUpSearchFilter(Messages.TRANSPORTADORAREG_TITULO_FILTROPOPUP, edFiltro, new Regiao(), RegiaoDbxDao.getInstance(), true, Regiao.COLUMN_CDREGIAO, Regiao.COLUMN_NMREGIAO);
        }
        listContainer.setColPosition(1, RIGHT);
        listContainer.setColPosition(3, RIGHT);
        if (!LavenderePdaConfig.usaCalculoTransportadoraMaisRentavelPedido) {
        	listContainer.setColsSort(new String[][]{{Messages.TRANSPORTADORAREG_LABEL_CDTRANSPORTADORA, "CDTRANSPORTADORA"}, {Messages.DESCRICAO, "NMTRANSPORTADORA"}});
        	configListContainer("NMTRANSPORTADORA");
        	listContainer.atributteSortSelected = sortAtributte;
        	listContainer.sortAsc = sortAsc;
        }
        btSelecionar = new ButtonPopup(Messages.BOTAO_SELECIONAR);
        btFechar.setText(Messages.BOTAO_CANCELAR);
        try {
			edFiltro.setText(RegiaoService.getInstance().getRegiao(pedido.getCliente().cdRegiao).toString());
			cdRegiao = pedido.getCliente().cdRegiao;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
        setDefaultRect();
    }
    
    @Override
    protected CrudService getCrudService() {
        return TransportadoraRegService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		TransportadoraReg transportadoraRegFilter = new TransportadoraReg();
		transportadoraRegFilter.cdEmpresa = pedido.cdEmpresa;
		transportadoraRegFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TransportadoraReg.class);
		if (!LavenderePdaConfig.isOcultaComboTransportadoraPorRegiao()) {
			if (filtroAllRegiao) {
				if (!(edFiltro.getValue() == null)) {
					cdRegiao = edFiltro.getValue();
				}
			} else if (filtroCdRegiao && ValueUtil.isNotEmpty(popUpFiltro.getValue())) {
				cdRegiao = popUpFiltro.getValue();
			}
		}
		transportadoraRegFilter.cdRegiao = cdRegiao;
		transportadoraRegFilter.vlMinPedido = pedido.vlTotalPedido;
		transportadoraRegFilter.vlMaxPedido = pedido.vlTotalPedido;
		transportadoraRegFilter.vlTotalPedidoFilter = pedido.vlTotalPedido;
		transportadoraRegFilter.tipoFreteFilter = new TipoFrete();
		transportadoraRegFilter.tipoFreteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		transportadoraRegFilter.tipoFreteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoFrete.class);
		try {
			transportadoraRegFilter.tipoFreteFilter.cdUf = LavenderePdaConfig.usaTipoFretePorEstado ? pedido.getCliente().cdEstadoComercial : TipoFrete.CD_ESTADO_PADRAO;
			transportadoraRegFilter.ignoraVlMinMaxFrete = LavenderePdaConfig.usaPedidoComplementarMesmaConfPedidoOriginal && pedido.isPedidoComplementar();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		if (filtroAllRegiao || filtroCdRegiao) {
    		String dsFiltro = edFiltro.getText();
    		transportadoraRegFilter.regiaoFilter = new Regiao();
    		if (filtroAllRegiao) {
    			transportadoraRegFilter.cdRegiao = null;
    			transportadoraRegFilter.regiaoFilter.cdRegiao = dsFiltro;
    			transportadoraRegFilter.regiaoFilter.nmRegiao = dsFiltro;
    		} else {
    			transportadoraRegFilter.regiaoFilter.cdRegiao = cdRegiao;
    		}
    	}
		if (LavenderePdaConfig.usaCalculoTransportadoraMaisRentavelPedido) {
			transportadoraRegFilter.ordenaPorFreteCifVlFrete = true;
		}
		return transportadoraRegFilter;
	}
    
    private void filtrarClick() {
    	filtroAllRegiao = true;
		try {
			if (!edFiltro.getText().trim().isEmpty()) {
	    		atualizaGrid();
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		filtroAllRegiao = false;
    }
    
    private void filtrarClickPopUp() {
    	filtroCdRegiao = true;
		try {
			atualizaGrid();
			if (ValueUtil.isNotEmpty(cdRegiao)) {
				edFiltro.setText(RegiaoService.getInstance().getRegiao(cdRegiao).toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		filtroCdRegiao = false;
    }
    
    private void atualizaGrid() throws SQLException {
    	clearGrid();
    	list();
    }
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	TransportadoraReg transpRegFilter = (TransportadoraReg) domain;
    	selecionouTransportadora = false;
    	transpPreferencialCif = false;
    	indexTranspPreferencial = -1;
    	indexList = 0;
    	transportadoraRegDisponiveisList = new Vector();
    	if (ValueUtil.isEmpty(transpRegFilter.cdRegiao) && !filtroAllRegiao) {
    		return transportadoraRegDisponiveisList;
    	}
    	transportadoraRegDisponiveisList = getCrudService().findAllByExample(transpRegFilter);
        edFiltro.requestFocus();
        return transportadoraRegDisponiveisList;
    }

	public boolean isTranspRegPreferencialCliente(TransportadoraReg transportadoraReg) throws SQLException {
		if (LavenderePdaConfig.usaTransportadoraPreferencialCliente) {
			boolean isTranspRegCifPadrao = pedido.getCliente().cdTransportadoraCif != null && transportadoraReg.tipoFrete.isTipoFreteCif() && pedido.getCliente().cdTransportadoraCif.equals(transportadoraReg.cdTransportadora);
			boolean isTranspRegFobPadrao = pedido.getCliente().cdTransportadoraFob != null && transportadoraReg.tipoFrete.isTipoFreteFob() && pedido.getCliente().cdTransportadoraFob.equals(transportadoraReg.cdTransportadora);
			if (isTranspRegCifPadrao || isTranspRegFobPadrao) {
				transpPreferencialCif = isTranspRegCifPadrao;
				return true;
			} 
		}
		return false;
	}
    
    @Override
    protected String[] getItem(Object domain) throws SQLException {
        TransportadoraReg transportadoraReg = (TransportadoraReg) domain;
        String[] item = {
        	transportadoraReg.transportadora.toString(),
        	Messages.TRANSPORTADORAREG_LABEL_TTRS + (transportadoraReg.transportadora.isFlSomaFrete() ? StringUtil.getStringValueToInterface(pedido.vlTotalPedido + transportadoraReg.vlFrete) : StringUtil.getStringValueToInterface(pedido.vlTotalPedido)),
        	transportadoraReg.tipoFrete.toString(),
        	transportadoraReg.transportadora.isFlMostraFrete() ? Messages.TRANSPORTADORAREG_LABEL_RS + StringUtil.getStringValueToInterface(transportadoraReg.vlFrete) : "",
        	"Região: " + transportadoraReg.regiao.toString()};
        return item;
    }

	@Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c != null ? c.id : null;
    }

    @Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
    	super.setPropertiesInRowList(c, domain);
    	TransportadoraReg transportadoraReg = (TransportadoraReg) domain;
    	if (indexTranspPreferencial == -1 && !transpPreferencialCif) {
    		if (isTranspRegPreferencialCliente(transportadoraReg)) {
    			indexTranspPreferencial = indexList;
    		}
    	}
    	if (LavenderePdaConfig.usaDestaqueTransportadoraCIFNaLista && TipoFreteService.getInstance().isTipoFreteCif(transportadoraReg, pedido.getCliente().cdEstadoComercial)) {
    		if (transportadoraReg.isPossuiContrato()) {
    			c.setBackColor(LavendereColorUtil.COR_TRANSPORTADORA_TIPOFRETE_CIF_CONTRATO);
    		} else {
    			c.setBackColor(LavendereColorUtil.COR_TRANSPORTADORA_TIPOFRETE_CIF);
    		}
    	}
    	indexList++;
    }
   
    //--------------------------------------------------------------

    
    @Override
    protected void onFormStart() {
    	if (!LavenderePdaConfig.isOcultaComboTransportadoraPorRegiao()) {
    		UiUtil.add(this, new LabelName(Messages.TRANSPORTADORAREG_LABEL_SELECIONE_REGIAO), popUpFiltro, getLeft(), getNextY(), getWFill(), UiUtil.getControlPreferredHeight());
    	}
    	UiUtil.add(this, new LabelValue(Messages.TRANSPORTADORAREG_LABEL_SELECIONE_TRANSPORTADORA), getLeft(), getNextY() + HEIGHT_GAP);
        UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, FILL);
    }

    @Override
    public void initUI() {
    	super.initUI();
    	selectTransportadora();
    }
    
    private void selectTransportadora() {
    	if (listContainer.size() > 0) {
    		if (transpPreferencialCif && indexTranspPreferencial != -1) {
    			listContainer.setSelectedIndex(indexTranspPreferencial);
    		} else {
    			TransportadoraReg transp = (TransportadoraReg) transportadoraRegDisponiveisList.elementAt(0);
    			if (LavenderePdaConfig.usaCalculoTransportadoraMaisRentavelPedido && transp.tipoFrete.isTipoFreteCif()) {
    				listContainer.setSelectedIndex(0);
    			} else if (indexTranspPreferencial != -1) {
    				listContainer.setSelectedIndex(indexTranspPreferencial);
    			}
    		}
    	}
	}
    
    @Override
    protected void onPopup() {
    	super.onPopup();
    	try {
			setIconSelected();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
    }

	private void setIconSelected() throws SQLException {
		for (int i = 0; i < listContainer.size(); i++) {
			Container container = listContainer.getContainer(i);
			BaseListContainer.Item c = (BaseListContainer.Item) container;
			if (c.id.equals(getSelectedRowKey())) {
				TransportadoraReg transpRegSelected = (TransportadoraReg) getSelectedDomain(); 
				setDomainAtual(transpRegSelected);
				c.setIconsLegend(getIconsLegend(transpRegSelected), resizeIconsLegend);
			} else {
				c.setIconsLegend(new Image[]{}, resizeIconsLegend);
			}
		}
	}

    @Override
	protected void addButtons() {
		addButtonPopup(btSelecionar);
		super.addButtons();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSelecionar) {
					btSelecionarClick();
					fecharWindow();
				} else if (popUpFiltro != null && event.target == popUpFiltro.btFiltrar) {
					filtrarClickPopUp();
				}
				break;
			}
			case PenEvent.PEN_UP: {
		    	if (event.target instanceof BaseListContainer.Item) {
		    		setIconSelected();
		    	}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				KeyEvent ke = (KeyEvent)event;
				if (ke.key == SpecialKeys.ENTER && event.target == edFiltro) {
					filtrarClick();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == edFiltro) {
					filtrarClick();
				}
				break;
			}
			default: break;
		}
	}
	
	private void btSelecionarClick() throws SQLException {
		String rowKey = getSelectedRowKey(); 
		if (getSelectedRowKey() == null) {
			throw new ValidationException(Messages.TRANSPORTADORAREG_ERRO_NENHUMA_TRANSP_SELECIONADA);
		}
		TransportadoraReg transportadoraReg = new TransportadoraReg();
		transportadoraReg.setRowkey(rowKey);
		transportadoraReg = (TransportadoraReg) transportadoraRegDisponiveisList.elementAt(transportadoraRegDisponiveisList.indexOf(transportadoraReg));
		if (transportadoraReg != null) {
			Transportadora transportadora = TransportadoraService.getInstance().getTransportadora(transportadoraReg.cdTransportadora);
			validaTransportadora(transportadora);
			transportadoraReg.transportadora = transportadora;
			pedido.transportadoraReg = transportadoraReg;
			selecionouTransportadora = true;
		}
	}
	
	public void validaTransportadora(Transportadora transportadoraSelecionada) throws SQLException {
		if (LavenderePdaConfig.usaPedidoComplementarMesmaConfPedidoOriginal && pedido.isPedidoComplementar() && ValueUtil.isNotEmpty(pedido.nuPedidoComplementado)) {
			Pedido pedidoRelacionado = (Pedido) PedidoService.getInstance().findPedidoComplementarByPedidoInPdaAndErp(pedido);
			if (transportadoraRelacionada == null) {
		        transportadoraRelacionada = pedidoRelacionado.getTransportadora();
			}
			if (transportadoraRelacionada == null) {
				transportadoraRelacionada = new Transportadora();
				transportadoraRelacionada.cdTransportadora = pedidoRelacionado.cdTransportadora;
			}
			TransportadoraRegService.getInstance().validaTransportadoras(transportadoraRelacionada, transportadoraSelecionada);
		}
	}
	
}