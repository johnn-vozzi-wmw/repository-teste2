package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CentroCusto;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import br.com.wmw.lavenderepda.business.domain.PlataformaVenda;
import br.com.wmw.lavenderepda.business.service.CentroCustoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaAtuaService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CentroCustoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PlatVendaMetaPeridComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PlataformaVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListPlanejarVendaMetaForm extends LavendereCrudListForm {
	
	private final boolean usuarioSupervisor = SessionLavenderePda.isUsuarioSupervisor();
	
	private PlatVendaMetaPeridComboBox cbPeriodo;
	private CentroCustoComboBox cbCentroCusto;
	private PlataformaVendaComboBox cbPlataformaVenda;
	private RepresentanteSupervComboBox cbRepresentante;
	private LabelName lbPeriodo;
	private LabelName lbCentroCusto;
	private LabelName lbPlataformaVenda;
	private LabelName lbRepresentante;
	private CheckBoolean ckSomenteNaoPlanejado;
	protected PlatVendaMeta platVendaMeta;
	private BaseButton btFiltroAvancado;
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lbTotalContratada;
	private LabelTotalizador lbTotalPlanejRep;
	private LabelTotalizador lbTotalPlanejSup;
	private LabelTotalizador lbTotalRegistros;
	protected Map<String, Vector> hashCachedList;
	private boolean aplicouFiltro;
	protected boolean savedFromCad;
	private boolean isColorEncerradoApplied;
	private boolean isColorPlanejadoApplied;
	private double vlTotalContratada;
	private double vlTotalPlanejRep;
	private double vlTotalPlanejSup;
	private int vlTotalRegistros;

	public ListPlanejarVendaMetaForm() throws SQLException {
		super(Messages.MENU_PLANEJAR_META);
		singleClickOn = true;
		cbPeriodo = new PlatVendaMetaPeridComboBox();
		cbCentroCusto = new CentroCustoComboBox(BaseComboBox.DefaultItemType_ALL);
		cbPlataformaVenda = new PlataformaVendaComboBox(BaseComboBox.DefaultItemType_ALL);
		platVendaMeta = new PlatVendaMeta();
		lbPeriodo = new LabelName(Messages.METAS_PERIODO);
		lbCentroCusto = new LabelName(Messages.PLANEJAMENTOMETAVENDA_LABEL_CENTRO_DE_CUSTO);
		lbPlataformaVenda = new LabelName(Messages.PLANEJAMENTOMETAVENDA_LABEL_PLATAFORMA_DE_VENDA);
		lbRepresentante = new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE);
		ckSomenteNaoPlanejado = new CheckBoolean(Messages.LIST_PLANEJARVENDAMETA_SOMENTENAOPLANEJADOS_LABEL);
		cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_ALL);
		constructorListContainer();
		configureCombos();
		btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		sessaoTotalizadores = new SessionTotalizerContainer();
		lbTotalContratada = new LabelTotalizador("9999.99");
		lbTotalPlanejRep = new LabelTotalizador("9999.99");
		lbTotalPlanejSup = new LabelTotalizador("9999.99");
		lbTotalRegistros = new LabelTotalizador("9999.99");
		aplicouFiltro = true;
		hashCachedList = new HashMap<String, Vector>();
	}
	
    private void configureCombos() throws SQLException {
    	cbPeriodoChange();
    	if (usuarioSupervisor) {    		
    		cbCentroCusto.carregaCentroCusto(true, null);
    	} else {
    		cbCentroCusto.carregaCentroCusto(true);
    	}
		cbCentroCustoChange();
		cbRepresentante.setSelectedIndex(0);
		cbRepresentanteChange();
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		listContainer = new GridListContainer(8, 2);
		configListContainer("CDCLIENTE");
		listContainer.setColsSort(new String[][] {
			{Messages.CLIENTE_NOME_ENTIDADE, "CDCLIENTE"},
			{Messages.PLANEJAMENTOMETAVENDA_LABEL_CENTRO_DE_CUSTO, "CDCENTROCUSTO"},
			{Messages.PLANEJAMENTOMETAVENDA_LABEL_PLATAFORMA_DE_VENDA, "CDPLATAFORMAVENDA"}
		});
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(5, RIGHT);
    	listContainer.setColPosition(7, RIGHT);
    	listContainer.setTotalizerQtdeVisible(false);
		ScrollPosition.AUTO_HIDE = true;
    }

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return platVendaMeta;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PlatVendaMetaService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (usuarioSupervisor) {			
			UiUtil.add(this, lbRepresentante, cbRepresentante, getLeft(), getNextY());
		}
		UiUtil.add(this, lbPeriodo, cbPeriodo, getLeft(), getNextY());		
		UiUtil.add(this, lbCentroCusto, cbCentroCusto, getLeft(), getNextY());
		UiUtil.add(this, lbPlataformaVenda, cbPlataformaVenda, getLeft(), getNextY(), getWFill() - UiUtil.getControlPreferredHeight()); 
		UiUtil.add(this, btFiltroAvancado, getRight(), SAME, UiUtil.getControlPreferredHeight());
		UiUtil.add(this, ckSomenteNaoPlanejado, getLeft(), getNextY());
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight() * 2 + WIDTH_GAP_BIG);
		UiUtil.add(sessaoTotalizadores, lbTotalRegistros, LEFT + listContainer.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
		UiUtil.add(sessaoTotalizadores, lbTotalContratada, RIGHT - listContainer.getTotalizerGap(), SAME, PREFERRED, PREFERRED);
		UiUtil.add(sessaoTotalizadores, lbTotalPlanejRep, LEFT + listContainer.getTotalizerGap(), AFTER + WIDTH_GAP, PREFERRED, PREFERRED);
		UiUtil.add(sessaoTotalizadores, lbTotalPlanejSup, RIGHT - listContainer.getTotalizerGap(), SAME, PREFERRED, PREFERRED);
		sessaoTotalizadores.resizeHeight();
		sessaoTotalizadores.resetSetPositions();
		sessaoTotalizadores.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, ckSomenteNaoPlanejado.getY2(), FILL, FIT - 1, sessaoTotalizadores);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentante) {
					cbRepresentanteChange();
					list();
				} else if (event.target == cbPeriodo) {
					cbPeriodoChange();
					list();
				} else if (event.target == cbCentroCusto) {
					cbCentroCustoChange();
					list();
				} else if (event.target == cbPlataformaVenda) {
					cbPlataformaVendaChange();
					list();
				} else if (event.target == ckSomenteNaoPlanejado) {
					aplicouFiltro = true;
					list();
				} else if (event.target == btFiltroAvancado) {
	    			btFiltroAvancadoClick();
	    		}
				break;
			}
		}
	}

	private void btFiltroAvancadoClick() throws SQLException {
		FiltroPlanejarVendaMetaAvancadoWindow filtroPlataformaVendasAvancadoWindow = new FiltroPlanejarVendaMetaAvancadoWindow(this);
		filtroPlataformaVendasAvancadoWindow.popup();
		if (filtroPlataformaVendasAvancadoWindow.aplicouFiltros) {
			aplicouFiltro = true;
			list();
		}
	}
	
	private void cbRepresentanteChange() throws SQLException {
		platVendaMeta.cdRepresentante = cbRepresentante.getValue();
		cbRepresentante.configureSession();
		aplicouFiltro = true;
	}
	
	private void cbPeriodoChange() {
		platVendaMeta.cdPeriodo = cbPeriodo.getValue();
		aplicouFiltro = true;
	}

	private void cbCentroCustoChange() throws SQLException {
		platVendaMeta.cdCentroCusto = cbCentroCusto.getValue();
		cbPlataformaVenda.carregaPlataformas(cbCentroCusto.getValue(), null, usuarioSupervisor ? cbRepresentante.getValue() : SessionLavenderePda.getCdRepresentanteFiltroDados(getClass()));
		cbPlataformaVendaChange();
		aplicouFiltro = true;
	}

    private void cbPlataformaVendaChange() {
    	platVendaMeta.cdPlataformaVenda = cbPlataformaVenda.getValue();
    	aplicouFiltro = true;
    }
    
	@Override
    protected void filtrarClick() throws SQLException {
    	list();
    }
    
	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		if (savedFromCad) {
			list();
			savedFromCad = false;
		}
	}
	
    @Override
    protected String[] getItem(Object domain) throws SQLException {
        PlatVendaMeta platVendaMeta = (PlatVendaMeta) domain;
		String[] item = {
			StringUtil.getStringValue(platVendaMeta.cdCliente),
            " - " + ClienteService.getInstance().getNmRazaoSocial(
	            		platVendaMeta.cdEmpresa, 
	            		platVendaMeta.cdRepresentante, 
	            		platVendaMeta.cdCliente
            		),
            getCentroCustoItem(platVendaMeta),
            usuarioSupervisor ? StringUtil.getStringValue(platVendaMeta.cdRepresentante) : "",
            getPlataformaVendaItem(platVendaMeta),
            cbPeriodo.getDsPeriodo(),
            getEnderecoItem(platVendaMeta),
            platVendaMeta.dsStatus
        };
        return item;
    }
    
    private String getCentroCustoItem(PlatVendaMeta platVendaMeta) throws SQLException {
    	CentroCusto centroCusto = CentroCustoService.getInstance().findCentroCusto(platVendaMeta.cdEmpresa, platVendaMeta.cdRepresentante, platVendaMeta.cdCentroCusto);
    	return centroCusto.cdCentroCusto != null ? centroCusto.toString() : "";
    }
    
    private String getPlataformaVendaItem(PlatVendaMeta platVendaMeta) throws SQLException {
    	PlataformaVenda plataformaVenda = PlataformaVendaService.getInstance().findPlataformaVenda(platVendaMeta.cdEmpresa, platVendaMeta.cdPlataformaVenda);
    	return plataformaVenda.cdPlataformaVenda != null ? plataformaVenda.toString() : "";
    }
    
    private String getEnderecoItem(PlatVendaMeta platVendaMeta) throws SQLException {
    	Cliente cliente = ClienteService.getInstance().getCliente(platVendaMeta.cdEmpresa, platVendaMeta.cdRepresentante, platVendaMeta.cdCliente);
    	if (cliente.cdCliente == null) return "";
    	return cliente.dsCidadeComercial + " - " + (ValueUtil.isNotEmpty(cliente.dsEstadoComercial) ? cliente.dsEstadoComercial : cliente.cdEstadoComercial) + " - " + cliente.dsBairroComercial;
    }
    
    private String getStatusMetaEPopulaHash(PlatVendaMeta platVendaMeta) throws SQLException {
    	Vector listPlatVendaMetaPeriodo = new Vector();
    	if (aplicouFiltro) {
    		listPlatVendaMetaPeriodo = PlatVendaMetaService.getInstance().getAllByPeriodo(platVendaMeta, false);
    	} else {
    		listPlatVendaMetaPeriodo = hashCachedList.get(platVendaMeta.getRowKey());
    	}
    	
    	String dsStatus = Messages.REQUISICAOSERV_DSSTATUSPENDENTE;
    	int size = listPlatVendaMetaPeriodo.size();
    	PlatVendaMeta platVendaMetaRef;
    	
    	for (int i = 0; i < size; i++) {
    		platVendaMetaRef = (PlatVendaMeta) listPlatVendaMetaPeriodo.items[i];
    		if (aplicouFiltro) PlatVendaMetaAtuaService.getInstance().setPropertiesByAtuaIfExists(platVendaMetaRef);
    		if (ValueUtil.valueEquals(platVendaMetaRef.flEncerrado, ValueUtil.VALOR_SIM)) {
    			dsStatus = Messages.PLANEJAMENTOMETAVENDA_STATUS_PLANEJAMENTO_ENCERRADO;
    		} else if (ValueUtil.valueEquals(platVendaMetaRef.flPlanejado, ValueUtil.VALOR_SIM)) {
    			dsStatus = Messages.PLANEJAMENTOMETAVENDA_STATUS_PLANEJADO_EM_ANALISE;
    		} else if (platVendaMetaRef.vlMetaPlanejadaRep != 0) {
    			dsStatus = Messages.PLANEJAMENTOMETAVENDA_LABEL_EMPLANEJAMENTO;
    		}
    		populaTotalizadores(platVendaMetaRef);
    	}
    	if (aplicouFiltro) hashCachedList.put(platVendaMeta.getRowKey(), listPlatVendaMetaPeriodo);
    	return dsStatus;
    }
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	if (aplicouFiltro) hashCachedList.clear();
    	PlatVendaMeta platVendaMetaRef = (PlatVendaMeta) domain;
    	platVendaMetaRef.cdRepresentante = !usuarioSupervisor ? SessionLavenderePda.getCdRepresentanteFiltroDados(getClass()) : cbRepresentante.getValue();
    	Vector listPlatVendaMeta = PlatVendaMetaService.getInstance().getAllByPeriodo(platVendaMetaRef, true);
    	listPlatVendaMeta = populaHashEFiltraDsStatus(listPlatVendaMeta);
    	aplicouFiltro = false;
    	return listPlatVendaMeta;
    }
    
    private Vector populaHashEFiltraDsStatus(Vector list) throws SQLException {
		int size = list.size();
		vlTotalRegistros = size;
		Vector newList = new Vector();
		boolean checked = ckSomenteNaoPlanejado.isChecked();
		PlatVendaMeta platVendaMeta;
		
    	for (int i = 0; i < size; i++) {
    		platVendaMeta = (PlatVendaMeta) list.items[i];
    		platVendaMeta.dsStatus = getStatusMetaEPopulaHash(platVendaMeta);
			if (checked && ValueUtil.valueEquals(platVendaMeta.dsStatus, Messages.REQUISICAOSERV_DSSTATUSPENDENTE)) {
    			newList.addElement(platVendaMeta);
    		}
    	}
    	updateTotalizadores();
    	
		return checked ? newList : list;
	}

	@Override
    protected BaseDomain getDomain(BaseDomain domain) {
    	return super.getDomain(domain);
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	String itemRowKey = listContainer.getSelectedId();
    	PlatVendaMeta platVendaMeta = (PlatVendaMeta) PlatVendaMetaService.getInstance().findByRowKey(itemRowKey);
    	PlatVendaMetaAtuaService.getInstance().setPropertiesByAtuaIfExists(platVendaMeta);
    	platVendaMeta.cdPeriodo = this.platVendaMeta.cdPeriodo;
    	platVendaMeta.cdCliente = ((BaseListContainer.Item) listContainer.getSelectedItem()).items[0];
    	if (isColorEncerradoApplied) {
    		platVendaMeta.flEncerrado = ValueUtil.VALOR_SIM;
    		platVendaMeta.flPlanejado = ValueUtil.VALOR_SIM;
    	} else if (isColorPlanejadoApplied) {
    		platVendaMeta.flPlanejado = ValueUtil.VALOR_SIM;
    	}
    	setBaseCrudCadForm(new CadPlatVendaMetaForm(platVendaMeta, this));
    	super.detalhesClick();
    }
    
    @Override
    protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
    	PlatVendaMeta platVendaMeta = (PlatVendaMeta) domain;
    	isColorEncerradoApplied = false;
    	isColorPlanejadoApplied = false;
    	if (ValueUtil.valueEquals(platVendaMeta.dsStatus, Messages.PLANEJAMENTOMETAVENDA_STATUS_PLANEJADO_EM_ANALISE)) {
    		containerItem.setBackColor(LavendereColorUtil.COR_FUNDO_LISTA_PLANEJAR_METAS_PLANEJADO);
    		isColorPlanejadoApplied = true;
    		return;
    	}
    	if (ValueUtil.valueEquals(platVendaMeta.dsStatus, Messages.PLANEJAMENTOMETAVENDA_LABEL_EMPLANEJAMENTO)) {
    		containerItem.setBackColor(LavendereColorUtil.COR_FUNDO_LISTA_PLANEJAR_METAS_EMPLANEJAMENTO);
    		return;
    	}
    	if (ValueUtil.valueEquals(platVendaMeta.dsStatus, Messages.PLANEJAMENTOMETAVENDA_STATUS_PLANEJAMENTO_ENCERRADO)) {
    		containerItem.setBackColor(LavendereColorUtil.COR_FUNDO_LISTA_PLANEJAR_METAS_ENCERRADO);
    		isColorEncerradoApplied = true;
    		return;
    	}
    }
    
    private void populaTotalizadores(PlatVendaMeta platVendaMeta) {
    	vlTotalContratada += platVendaMeta.vlMetaContratada;
		vlTotalPlanejRep += platVendaMeta.vlMetaPlanejadaRep;
		vlTotalPlanejSup += platVendaMeta.vlMetaPlanejadaSup;
    }
    
    private void updateTotalizadores() throws SQLException {
    	lbTotalContratada.setValue(Messages.LIST_PLANEJAMENTOMETAVENDA_TOTAL_CONTRATADA + " " +  StringUtil.getStringValueToInterface(vlTotalContratada));
    	lbTotalPlanejRep.setValue(Messages.LIST_PLANEJAMENTOMETAVENDA_TOTAL_PLANEJ_REP + " " +  StringUtil.getStringValueToInterface(vlTotalPlanejRep));
    	lbTotalPlanejSup.setValue(Messages.LIST_PLANEJAMENTOMETAVENDA_TOTAL_PLANEJ_SUP + " " +  StringUtil.getStringValueToInterface(vlTotalPlanejSup));
    	lbTotalRegistros.setValue(StringUtil.getStringValueToInterface(vlTotalRegistros) + " " + Messages.LIST_PLANEJAMENTOMETAVENDA_REGISTROS);
    	sessaoTotalizadores.reposition();
    	vlTotalContratada = 0.d;
    	vlTotalPlanejRep = 0.d;
    	vlTotalPlanejSup = 0.d;
    }
    
}
