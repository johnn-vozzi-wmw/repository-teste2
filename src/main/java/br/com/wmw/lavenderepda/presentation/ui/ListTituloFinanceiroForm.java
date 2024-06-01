package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.service.TituloFinanceiroService;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusTituloFinanceiroComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListTituloFinanceiroForm extends LavendereCrudListForm {

	private FichaFinanceira fichaFinanceira;
	private String nuTitulo = "";
	private String dtVencimento = "";
	private String vlPago = "";
	private String nuNf = "";
	private String vlNf = "";
	private String dtPagamento = "";
	protected String cdTipoPagamento;
	private EditDate edDtVencimentoInicial;
	private EditDate edDtVencimentoFinal;
	private EditDate edDtPagamentoInicial;
	private EditDate edDtPagamentoFinal;
	private SessionContainer containerFiltros;
	private StatusTituloFinanceiroComboBox cbStatusTituloFinanceiro;
	private Date[] lastDateFilterPagamento = new Date[2];
	private Date[] lastDateFilterVencimento = new Date[2];
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lbTotalPago;
	private LabelTotalizador lbTotalAberto;

	public ListTituloFinanceiroForm(FichaFinanceira masterDomain, boolean setStatusTituloNaoPago) throws SQLException {
		this(masterDomain);
		if (setStatusTituloNaoPago) cbStatusTituloFinanceiro.setSelectedItem(Messages.COMBOBOX_TITULOFINANCEIRO_NAO_PAGO);
	}
	
	public ListTituloFinanceiroForm(FichaFinanceira masterDomain) throws SQLException {
        super(Messages.TITULOFINANCEIRO_NOME_ENTIDADE);
        setBaseCrudCadForm(new CadTituloFinanceiroDynForm());
        singleClickOn = true;
    	this.fichaFinanceira = masterDomain;
		containerFiltros = new SessionContainer();
		containerFiltros.setBackColor(ColorUtil.baseBackColorSystem);
        edDtVencimentoInicial = new EditDate();
        edDtVencimentoInicial.setEditable(true);
        edDtVencimentoFinal = new EditDate();
        edDtVencimentoFinal.setEditable(true);
        edDtPagamentoInicial = new EditDate();
        edDtPagamentoInicial.setEditable(true);
        edDtPagamentoFinal = new EditDate();
        edDtPagamentoFinal.setEditable(true);
        cbStatusTituloFinanceiro = new StatusTituloFinanceiroComboBox();
        constructorListContainer();
		lbTotalPago = new LabelTotalizador("9999.99");
		lbTotalAberto = new LabelTotalizador("9999.99");
		sessaoTotalizadores = new SessionTotalizerContainer();
    	lastDateFilterPagamento[0] = edDtPagamentoInicial.getValue();
    	lastDateFilterPagamento[1] = edDtPagamentoFinal.getValue();
    	lastDateFilterVencimento[0] = edDtVencimentoInicial.getValue();
    	lastDateFilterVencimento[1] = edDtVencimentoFinal.getValue();
		loadMessages();
    }

	private void constructorListContainer() {
		configListContainer("NUTITULO");
		//--
		listContainer = new GridListContainer(9, 3);
		listContainer.setColPosition(5, RIGHT);
		listContainer.setColPosition(8, RIGHT);
		listContainer.setColsSort(new String[][]{{Messages.TITULOFINANCEIRO_NOME_ENTIDADE, "NUTITULO"}, {Messages.TITULOFINANCEIRO_LABEL_DTVENCIMENTO, "DTVENCIMENTO"}, {Messages.TITULOFINANCEIRO_LABEL_DTPAGAMENTO, "DTPAGAMENTO"}});
		listResizeable = isListResizeable();
	}
	
	private boolean isListResizeable() {
		return LavenderePdaConfig.filtroPeriodoVencimentoConfigTituloFinanceiro()
				|| LavenderePdaConfig.filtroPeriodoPagamentoConfigTituloFinanceiro()
				|| LavenderePdaConfig.filtroTituloPagoConfigTituloFinanceiro();
	}

    @Override
    protected CrudService getCrudService() throws SQLException {
        return TituloFinanceiroService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new TituloFinanceiro();
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Vector list = new Vector();
    	if (fichaFinanceira != null) {
    		TituloFinanceiro.sortAttr = domain.sortAtributte;
    		list = fichaFinanceira.getTitulos(domain.sortAtributte, domain.sortAsc, cdTipoPagamento, edDtPagamentoInicial.getValue(), edDtPagamentoFinal.getValue(), 
    				edDtVencimentoInicial.getValue(), edDtVencimentoFinal.getValue(), cbStatusTituloFinanceiro.getSelectedItem() != null ? cbStatusTituloFinanceiro.getSelectedItem().toString() : null);
    	} 
        return list;
    }

    @Override
    protected String[] getItem(Object domain) throws SQLException {
        TituloFinanceiro tituloFinanceiro = (TituloFinanceiro) domain;
        String[] item = {
        		nuTitulo + StringUtil.getStringValue(tituloFinanceiro.nuTitulo),
                " - " + Messages.PRODUTO_LABEL_RS,
                StringUtil.getStringValueToInterface(tituloFinanceiro.vlTitulo),
                vlPago + Messages.PRODUTO_LABEL_RS,
                StringUtil.getStringValueToInterface(tituloFinanceiro.vlPago),
                ValueUtil.isNotEmpty(tituloFinanceiro.dtPagamento) ? dtPagamento + StringUtil.getStringValue(tituloFinanceiro.dtPagamento) : Messages.COMBOBOX_TITULOFINANCEIRO_NAO_PAGO,
                nuNf + StringUtil.getStringValue(tituloFinanceiro.nuNf),
                " - " + vlNf + Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(tituloFinanceiro.vlNf),
                dtVencimento + StringUtil.getStringValue(tituloFinanceiro.dtVencimento)};
        return item;
    }

    @Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    @Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		if (((TituloFinanceiro)domain).dtVencimento.isBefore(DateUtil.getCurrentDate()) && ValueUtil.isEmpty(((TituloFinanceiro)domain).dtPagamento)) {
			c.setBackColor(LavendereColorUtil.COR_CLIENTE_ATRASADO_BLOQUEADO_BACK);
		}
    }

    private void loadMessages() {
    	nuTitulo = Messages.TITULOFINANCEIRO_NOME_ENTIDADE + " ";
        dtVencimento = Messages.TITULOFINANCEIRO_LABEL_DTVENCIMENTO + " ";
        vlPago = Messages.TITULOFINANCEIRO_LABEL_VLPAGO + " ";
        nuNf = Messages.TITULOFINANCEIRO_LABEL_NUNF + " ";
        vlNf = Messages.TITULOFINANCEIRO_LABEL_VLNF + " ";
        dtPagamento = Messages.TITULOFINANCEIRO_LABEL_DTPAGAMENTO + " ";
    }
    
    private Integer qtdComponenteFiltroTela() {
    	Integer qtdFiltro = 0;
    	if (LavenderePdaConfig.filtroPeriodoVencimentoConfigTituloFinanceiro()) qtdFiltro++;
    	if (LavenderePdaConfig.filtroPeriodoPagamentoConfigTituloFinanceiro()) qtdFiltro++;
    	if (LavenderePdaConfig.filtroTituloPagoConfigTituloFinanceiro()) qtdFiltro++;
    	return UiUtil.getControlPreferredHeight() * qtdFiltro + UiUtil.getLabelPreferredHeight() * qtdFiltro + HEIGHT_GAP * qtdFiltro;
    }
    
    //@Override
    protected void onFormStart() throws SQLException {
    	int gap = listContainer.getTotalizerGap();
    	UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight());
    	UiUtil.add(sessaoTotalizadores, lbTotalAberto, LEFT + gap, SAME, PREFERRED, PREFERRED);
    	UiUtil.add(sessaoTotalizadores, lbTotalPago, RIGHT - gap, SAME, PREFERRED, PREFERRED);
    	sessaoTotalizadores.resizeHeight();
    	sessaoTotalizadores.resetSetPositions();
    	sessaoTotalizadores.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
    	UiUtil.add(this, containerFiltros, LEFT, TOP, FILL, qtdComponenteFiltroTela());
    	if (LavenderePdaConfig.filtroPeriodoPagamentoConfigTituloFinanceiro()) {
			UiUtil.add(containerFiltros, new LabelName(Messages.TITULOFINANCEIRO_LABEL_PERIODO_DATA_PAGAMENTO), edDtPagamentoInicial, getLeft(), AFTER + HEIGHT_GAP);
	    	UiUtil.add(containerFiltros, edDtPagamentoFinal, AFTER + WIDTH_GAP_BIG , SAME);    	   
    	}
    	if (LavenderePdaConfig.filtroPeriodoVencimentoConfigTituloFinanceiro()) {
			UiUtil.add(containerFiltros, new LabelName(Messages.TITULOFINANCEIRO_LABEL_PERIODO_DATA_VENCIMENTO), edDtVencimentoInicial, getLeft(), AFTER + HEIGHT_GAP);
	    	UiUtil.add(containerFiltros, edDtVencimentoFinal, AFTER + WIDTH_GAP_BIG , SAME);
    	}    	
    	if (LavenderePdaConfig.filtroTituloPagoConfigTituloFinanceiro()){
			UiUtil.add(containerFiltros, new LabelName(Messages.TITULOFINANCEIRO_LABEL_STATUS_TITULO), cbStatusTituloFinanceiro, getLeft(), AFTER + HEIGHT_GAP);
    	}
        UiUtil.add(this, listContainer, LEFT, AFTER, FILL, FILL - sessaoTotalizadores.getHeight());
    }
    
    public void visibleState() {
    	super.visibleState();
		barTopContainer.setVisible(false);
		barBottomContainer.setVisible(false);
    }

	public BaseDomain getSelectedDomain() throws SQLException {
		return getCrudService().findByRowKey(getSelectedRowKey());
	}

	@Override
	public void detalhesClick() throws SQLException {
		if (listContainer != null) {
			BaseDomain domain = getCrudService().findByRowKeyDyn(getSelectedRowKey());
			domain.rowKey = getSelectedRowKey();
	        setBaseCrudCadForm(new CadTituloFinanceiroDynForm());
			getBaseCrudCadForm().edit(domain);
			show(getBaseCrudCadForm());
		} else {
			int indexSelected = gridEdit.getSelectedIndex();
			if (indexSelected >= 0) {
				BaseDomain domain = getCrudService().findByRowKeyDyn(getSelectedRowKey());
				getBaseCrudCadForm().edit(domain);
				show(getBaseCrudCadForm());
			} else {
				UiUtil.showGridEmptySelectionMessage("");
			}
		}
	}
	
	//@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		case ControlEvent.PRESSED:
    			if (event.target instanceof CalendarWmw) {
					validateFilterData(edDtPagamentoInicial.getValue(), edDtPagamentoFinal.getValue());	
					validateFilterData(edDtVencimentoInicial.getValue(), edDtVencimentoFinal.getValue());	
    		    	lastDateFilterPagamento[0] = edDtPagamentoInicial.getValue();
    		    	lastDateFilterPagamento[1] = edDtPagamentoFinal.getValue();
    		    	lastDateFilterVencimento[0] = edDtVencimentoInicial.getValue();
    		    	lastDateFilterVencimento[1] = edDtVencimentoFinal.getValue(); 
    		    	list();	
    			}
    			if (event.target instanceof StatusTituloFinanceiroComboBox) {
    				list();
    			}
    		break;
    		case ValueChangeEvent.VALUE_CHANGE: 
    			list();
    		break;
    	}
    }

    public void validateFilterData(Date dateInitial, Date dateFinal) {
    	if (!ValueUtil.isEmpty(dateInitial) && !ValueUtil.isEmpty(dateFinal)) {
    		if (dateInitial.isAfter(dateFinal)) {
    			String[] param = {StringUtil.getStringValue(dateFinal), StringUtil.getStringValue(dateInitial)};
    			edDtPagamentoInicial.setText(StringUtil.getStringValue(lastDateFilterPagamento[0]));
    			edDtPagamentoFinal.setText(StringUtil.getStringValue(lastDateFilterPagamento[1]));
    			edDtVencimentoInicial.setText(StringUtil.getStringValue(lastDateFilterVencimento[0]));
    			edDtVencimentoFinal.setText(StringUtil.getStringValue(lastDateFilterVencimento[1]));
                throw new ValidationException(MessageUtil.getMessage(Messages.TITULOFINANCEIRO_MSG_DATA_INICIAL_MAIOR, param));
    		}
    	}
    }
    
    @Override
    protected void resizeListToFullScreen() {
		listContainer.setRect(0, TOP, FILL, FILL - sessaoTotalizadores.getHeight());
		listContainer.initUI();
		listContainer.repaintContainers();    	
    }
 
    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	updateTotalizadores(domainList);
    }
    
    private void updateTotalizadores(Vector list) {
    	int size = list.size();
    	double vlTotalPagos = 0.d;
    	double vlTotalAberto = 0.d;
    	if (size > 0) {
    		TituloFinanceiro tituloFinanceiro;
    		for (int i = 0; i < size; i++) {
    			tituloFinanceiro = (TituloFinanceiro) list.items[i];
    			vlTotalPagos += tituloFinanceiro.vlPago;
    			if (ValueUtil.isEmpty(tituloFinanceiro.dtPagamento)) vlTotalAberto += tituloFinanceiro.vlTitulo - tituloFinanceiro.vlPago;
    		}
    	}
    	lbTotalAberto.setValue(Messages.TITULOFINANCEIRO_LABEL_TOTALTITULOS + " " + StringUtil.getStringValueToInterface(vlTotalAberto));
    	lbTotalPago.setValue(Messages.TITULOFINANCEIRO_LABEL_TOTALTITULOS_PAGO + " " + StringUtil.getStringValueToInterface(vlTotalPagos));
    	sessaoTotalizadores.reposition();
    }
    
	public void carregaLista() throws SQLException {
		getDomainList(getDomainFilterSortable());
		
	}
}