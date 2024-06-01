package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.ResizeListEvent;
import br.com.wmw.framework.presentation.ui.event.TabContainerEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.RedeCliente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.RedeService;
import br.com.wmw.lavenderepda.business.service.TituloFinanceiroService;
import br.com.wmw.lavenderepda.business.validation.ClienteInadimplenteException;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadFichaFinanceiraDynForm extends BaseLavendereCrudPersonCadForm {

	private int TABPANEL_FINANCEIRO;
	private int TABPANEL_TITULOS;
	private int TABPANEL_REDE;
	private int TABPANEL_NOTADEVOLUCAO;

	private LabelValue lbDtMaiorAtraso;
    private LabelValue lbVlSaldoCliente;
    private LabelValue lbVlLimiteCredito;
    private LabelValue lbVlSaldoConsignacaoCliente;
    private LabelValue lbVlLimiteConsignacaoCliente;
    private LabelValue lbVlTotalAtraso;
    private LabelValue lbQtAtrasado;
    private LabelValue lbVlTotalAberto;
    private LabelValue lbQtAberto;
    protected BaseToolTip toolTipQtAtrasado;
    protected BaseToolTip toolTipQtAberto;
    protected BaseToolTip toolTipDtMaiorAtraso;
    protected BaseToolTip toolTipVlSaldoCliente;
    protected BaseToolTip toolTipVlLimiteCredito;
    protected BaseToolTip toolTipVlSaldoConsignacaoCliente;
    protected BaseToolTip toolTipVlLimiteConsignacaoCliente;
    protected BaseToolTip toolTipVlTotalAtraso;
    protected BaseToolTip toolTipVlTotalAberto;
    private LabelContainer containerDadosCli;
    private ButtonAction btAprovarNfDevolucao;
    private ButtonAction btReprovarNfDevolucao;
    private ListNfDevolucaoForm listNfDevolucaoform;
    
    public boolean isAcessoPopUpMotivo = false;
    public boolean setCbStatusTituloNaoPago = false;
    
    private boolean listMaximized = false;
    
    private final TipoPagamentoComboBox cbTipoPagamento;
	public ClienteInadimplenteException clienteInadiplenteException;
	
	public CadFichaFinanceiraDynForm(boolean setCbStatusTituloNaoPago) {
		this();
		this.setCbStatusTituloNaoPago = setCbStatusTituloNaoPago;
	}

    public CadFichaFinanceiraDynForm() {
        super(Messages.FICHAFINANCEIRA_NOME_ENTIDADE);
        lbVlSaldoCliente = new LabelValue();
        lbVlLimiteCredito = new LabelValue();
        lbVlSaldoConsignacaoCliente = new LabelValue();
        lbVlLimiteConsignacaoCliente = new LabelValue();
        lbVlTotalAtraso = new LabelValue("9999999,99");
        lbQtAtrasado = new LabelValue();
        lbVlTotalAberto = new LabelValue("9999999,99");
        lbQtAberto = new LabelValue();
        toolTipQtAtrasado = new BaseToolTip(lbQtAtrasado, Messages.FICHAFINANCEIRA_TOOLTIP_QTATRASADO);
        toolTipQtAberto = new BaseToolTip(lbQtAberto, Messages.FICHAFINANCEIRA_TOOLTIP_QTABERTO);
        lbDtMaiorAtraso = new LabelValue();
        toolTipDtMaiorAtraso = new BaseToolTip(lbDtMaiorAtraso, Messages.FICHAFINANCEIRA_TOOLTIP_DT_MAIOR_ATRASO);
        toolTipVlSaldoCliente = new BaseToolTip(lbVlSaldoCliente, Messages.FICHAFINANCEIRA_TOOLTIP_VL_SALDO_CLIENTE);
        toolTipVlLimiteCredito = new BaseToolTip(lbVlLimiteCredito, Messages.FICHAFINANCEIRA_TOOLTIP_VL_LIMITE_CREDITO);
        toolTipVlSaldoConsignacaoCliente = new BaseToolTip(lbVlSaldoConsignacaoCliente, Messages.FICHAFINANCEIRA_TOOLTIP_VL_SALDO_CONSIGNACAO_CLIENTE);
        toolTipVlLimiteConsignacaoCliente = new BaseToolTip(lbVlLimiteConsignacaoCliente, Messages.FICHAFINANCEIRA_TOOLTIP_VL_LIMITE_CONSIGNACAO_CLIENTE);
        toolTipVlTotalAtraso = new BaseToolTip(lbVlTotalAtraso, Messages.FICHAFINANCEIRA_TOOLTIP_VL_TOTAL_ATRASO);
        toolTipVlTotalAberto = new BaseToolTip(lbVlTotalAberto, Messages.FICHAFINANCEIRA_TOOLTIP_VL_TOTAL_ABERTO);
        containerDadosCli = new LabelContainer(SessionLavenderePda.getCliente().toString());
        cbTipoPagamento = new TipoPagamentoComboBox(true);
        setReadOnly();
        if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira) {
        	btAprovarNfDevolucao = new ButtonAction(Messages.NFDEVOLUCAO_APROVAR, "images/ok.png");
        	btReprovarNfDevolucao = new ButtonAction(Messages.NFDEVOLUCAO_REPROVAR, "images/cancel.png");
        	btAprovarNfDevolucao.setVisible(false);
        	btReprovarNfDevolucao.setVisible(false);
			
		}
    }

	protected String getDsTable() throws SQLException {
		return FichaFinanceira.TABLE_NAME;
	}

    //-----------------------------------------------

    //@Override
	protected String getEntityDescription() {
    	return title;
    }

    //@Override
	protected CrudService getCrudService() throws SQLException {
        return FichaFinanceiraService.getInstance();
    }

    //@Override
	protected BaseDomain createDomain() throws SQLException {
        return new FichaFinanceira();
    }

    //@Override
	protected BaseDomain screenToDomain() throws SQLException {
        return getDomain();
    }

    //@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
        FichaFinanceira fichaFinanceira = (FichaFinanceira) domain;
        super.domainToScreen(domain);
        //--
        
        double vlTotalPedidos = 0;
        Rede rede = null;
        
        if (LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes) {
        	if (LavenderePdaConfig.usaLimiteCreditoRedeCompartilhadoEmpresas) {
        		rede = RedeService.getInstance().findRedeByClienteIgnoraEmpresa(SessionLavenderePda.getCliente());
        	} else {
        		rede = RedeService.getInstance().findRedeByCliente(SessionLavenderePda.getCliente());
        	}
        }
        if (rede != null) {
        	vlTotalPedidos = FichaFinanceiraService.getInstance().getVlSaldoCliente(SessionLavenderePda.getCliente(), rede);
        	lbVlLimiteCredito.setValue(rede.vlLimiteCredito);
		} else {
			vlTotalPedidos = FichaFinanceiraService.getInstance().getVlSaldoCliente(SessionLavenderePda.getCliente(), rede);
			lbVlLimiteCredito.setValue(SessionLavenderePda.getCliente().vlLimiteCredito);
		}
        if (vlTotalPedidos < 0 && !(LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes && LavenderePdaConfig.exibeSaldoNegativo)) {
			vlTotalPedidos = 0;
		}
        
        lbVlSaldoCliente.setValue(vlTotalPedidos);
        if (vlTotalPedidos <= 0) {
        	lbVlSaldoCliente.setForeColor(ColorUtil.softRed);
        } else {
        	lbVlSaldoCliente.setForeColor(ColorUtil.componentsForeColor);
        }
        if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
        	vlTotalPedidos = FichaFinanceiraService.getInstance().getVlSaldoConsignadoCliente(null, SessionLavenderePda.getCliente(), false);
        	vlTotalPedidos = vlTotalPedidos >= 0 ? vlTotalPedidos : 0;
        	lbVlSaldoConsignacaoCliente.setValue(vlTotalPedidos);
        	lbVlSaldoConsignacaoCliente.setForeColor(vlTotalPedidos == 0 ? ColorUtil.softRed : ColorUtil.componentsForeColor);
        	lbVlLimiteConsignacaoCliente.setValue(SessionLavenderePda.getCliente().vlLimiteCreditoConsig);
        }
        
        //--
        if (LavenderePdaConfig.usaFiltroTituloFinanceiroPorTipoPagamento) {
        	cbTipoPagamento.carregaTipoPagamentos();
        	cbTipoPagamento.setSelectedIndex(0);
        	String tipoPagamentoSelecionado = cbTipoPagamento.getValue();
            ListTituloFinanceiroForm tabPanelTitulos = new ListTituloFinanceiroForm(fichaFinanceira);
    		tabPanelTitulos.cdTipoPagamento = tipoPagamentoSelecionado;
    		tabDinamica.setContainer(TABPANEL_TITULOS, tabPanelTitulos);
        } else {  
            ListTituloFinanceiroForm listTituloFinanceiroForm = new ListTituloFinanceiroForm(fichaFinanceira);
            tabDinamica.setContainer(TABPANEL_TITULOS, listTituloFinanceiroForm);
        }
        
        lbVlTotalAtraso.setValue(fichaFinanceira.vlTotalAtraso);
        lbQtAtrasado.setValue("(" + StringUtil.getStringValueToInterface(fichaFinanceira.qtAtrasado) + ")");
        if (fichaFinanceira.vlTotalAtraso > 0) {
        	lbVlTotalAtraso.setForeColor(ColorUtil.softRed);
        } else {
        	lbVlTotalAtraso.setForeColor(ColorUtil.componentsForeColor);
        }
        
        lbVlTotalAberto.setValue(fichaFinanceira.vlTotalAberto);
        lbQtAberto.setValue("(" + StringUtil.getStringValueToInterface(fichaFinanceira.qtAberto) + ")");
        lbDtMaiorAtraso.setValue(fichaFinanceira.dtTituloMaisAtrasado);
        
        if (LavenderePdaConfig.apresentaListaClienteRedeTituloFinanceiro) {
        	tabDinamica.setContainer(TABPANEL_REDE, new ListRedeClienteForm(getRedeClienteFilter(fichaFinanceira)));
        }
        
        if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira) {
        	listNfDevolucaoform = new ListNfDevolucaoForm(getNotaDevolucaoFilter(fichaFinanceira), false);
        	tabDinamica.setContainer(TABPANEL_NOTADEVOLUCAO, listNfDevolucaoform);
			
		}
    }

    private NfDevolucao getNotaDevolucaoFilter(FichaFinanceira fichaFinanceira) {
    	NfDevolucao notaDevolucao = new NfDevolucao();
    	notaDevolucao.cdCliente = fichaFinanceira.cdCliente;
    	return notaDevolucao;
    	
    }
    
    private RedeCliente getRedeClienteFilter(FichaFinanceira fichaFinanceira) throws SQLException {
    	RedeCliente redeClienteFilter = new RedeCliente();
    	Rede rede = SessionLavenderePda.getCliente().getRede();
    	if (rede != null) {
    		redeClienteFilter.cdRede = rede.cdRede;
    	}
		return redeClienteFilter;
	}

	//@Override
	protected void clearScreen() throws SQLException {
    	super.clearScreen();
        lbVlSaldoCliente.setText("");
        lbVlSaldoConsignacaoCliente.setText("");
        lbVlTotalAtraso.setText("");
        lbQtAtrasado.setText("");
        lbVlTotalAberto.setText("");
        lbQtAberto.setText("");
        lbDtMaiorAtraso.setText("");
    }

	protected void addCabecalho() throws SQLException {
    	UiUtil.add(this, containerDadosCli, LEFT, getNextY(), FILL, LabelContainer.getStaticHeight());
    	if (LavenderePdaConfig.usaFiltroTituloFinanceiroPorTipoPagamento) {
    		UiUtil.add(this, new LabelName(Messages.TIPOPAGTO_LABEL_TIPOPAGTO), cbTipoPagamento, getLeft(), getNextY());
    	}
    }
	
	@Override
	protected void addTabDinamica() {
		if (hashTabs.size() > 1) {
			UiUtil.add(scrollable ? scBase : this, tabDinamica, getNextY(), getBottomTab(), ColorUtil.formsBackColor);
		} else {
			UiUtil.add(this, scrollContainer, LEFT, getNextY(), FILL, FILL - getBottomTab());
			scrollContainer.setBackColor(ColorUtil.formsBackColor);
		}
	}

    @Override
    protected void addTabsFixas(Vector tableTitles) throws SQLException {
    	String fichaFinanceiraLabelTabPanelFinan = Messages.FICHAFINANCEIRA_LABEL_TABPANELFINAN;
    	int indexFinanceiro = tableTitles.indexOf(fichaFinanceiraLabelTabPanelFinan);
    	if (indexFinanceiro == -1) {
    		tableTitles.addElement(fichaFinanceiraLabelTabPanelFinan);
    		TABPANEL_FINANCEIRO = tableTitles.size() - 1;
    	} else {
    		TABPANEL_FINANCEIRO = indexFinanceiro;
    	}
    	//--
    	String TITULOFINANCEIRO_NOME_ENTIDADE = Messages.TITULOFINANCEIRO_NOME_ENTIDADE;
    	int indexTitulos = tableTitles.indexOf(TITULOFINANCEIRO_NOME_ENTIDADE);
    	if (indexTitulos == -1) {
    		tableTitles.addElement(TITULOFINANCEIRO_NOME_ENTIDADE);
    		TABPANEL_TITULOS = tableTitles.size() - 1;
    	} else {
    		TABPANEL_TITULOS = indexTitulos;
    	}
    	//--
    	if (LavenderePdaConfig.apresentaListaClienteRedeTituloFinanceiro) {
	    	String REDE_LABEL_TABPANELFINAN = Messages.REDE_LABEL_TABPANELFINAN;
	    	int indexTabRede = tableTitles.indexOf(REDE_LABEL_TABPANELFINAN);
	    	if (indexTabRede == -1) {
	    		tableTitles.addElement(REDE_LABEL_TABPANELFINAN);
	    		TABPANEL_REDE = tableTitles.size() - 1;
	    	} else {
	    		TABPANEL_REDE = indexTabRede;
	    	}
    	}
    	if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira) {
			int indexNotaDevolucao = tableTitles.indexOf(Messages.NFDEVOLUCAO_NOME_ENTIDADE);
			if (indexNotaDevolucao == -1) {
				tableTitles.addElement(Messages.NFDEVOLUCAO_NOME_ENTIDADE);
				TABPANEL_NOTADEVOLUCAO = tableTitles.size() - 1;
			} else {
				TABPANEL_NOTADEVOLUCAO = indexNotaDevolucao;
			}
		}
    }

	@Override
	public void reposition() {
		super.reposition();
		if (lbVlTotalAberto != null && !LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("1")) {
			lbVlTotalAberto.setRect(lbVlTotalAberto.getX(), lbVlTotalAberto.getY(), fm.stringWidth(lbVlTotalAberto.getText()), lbVlTotalAberto.getHeight());
			lbQtAberto.setRect(lbVlTotalAberto.getX2() + WIDTH_GAP, lbQtAberto.getY(), lbQtAberto.getWidth(), lbQtAberto.getHeight());
		}
		if (lbVlTotalAtraso != null && !LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("2")) {
			lbVlTotalAtraso.setRect(lbVlTotalAtraso.getX(), lbVlTotalAtraso.getY(), fm.stringWidth(lbVlTotalAtraso.getText()), lbVlTotalAtraso.getHeight());
			lbQtAtrasado.setRect(lbVlTotalAtraso.getX2() + WIDTH_GAP, lbQtAtrasado.getY(), lbQtAtrasado.getWidth(), lbQtAtrasado.getHeight());
		}
	}

	protected void addComponentesFixosInicio() throws SQLException {
		Container tabPanel = tabDinamica.getContainer(TABPANEL_FINANCEIRO);
		//--
		if (!LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("1")) {
			UiUtil.add(tabPanel, new LabelName(Messages.FICHAFINANCEIRA_LABEL_VLTOTALABERTO), getLeft(), getNextY());
			UiUtil.add(tabPanel, lbVlTotalAberto, getLeft(), getNextY());
	        UiUtil.add(tabPanel, lbQtAberto, AFTER + WIDTH_GAP_BIG, SAME);
		}
        //--
		if (!LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("2")) {
	        UiUtil.add(tabPanel, new LabelName(Messages.FICHAFINANCEIRA_LABEL_VLTOTALATRASO), getLeft(), getNextY());
	        UiUtil.add(tabPanel, lbVlTotalAtraso, getLeft(), getNextY());
	        UiUtil.add(tabPanel, lbQtAtrasado, AFTER + WIDTH_GAP_BIG, SAME);
		}
        //--
        if (LavenderePdaConfig.exibeTituloMaisAtrasadoNaFichaFinanceira) {
        	UiUtil.add(tabPanel, new LabelName(Messages.FICHAFINANCEIRA_LABEL_TITULO_MAIS_ATRASADO), lbDtMaiorAtraso, getLeft(), getNextY());
        }
    	if (!LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("3")) {
	        UiUtil.add(tabPanel, new LabelName(Messages.CLIENTE_LABEL_VLLIMITECREDITO), lbVlLimiteCredito, getLeft(), getNextY());
    	}
    	if (!LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("4")) {
	        UiUtil.add(tabPanel, new LabelName(Messages.FICHAFINANCEIRA_LABEL_VLSALDOCLIENTE), lbVlSaldoCliente, getLeft(), getNextY());
    	}
    	if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
    		if (!LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("5")) {
    	        UiUtil.add(tabPanel, new LabelName(Messages.CLIENTE_LABEL_VLLIMITECONSIGNACAO), lbVlLimiteConsignacaoCliente, getLeft(), getNextY());
    		}
    		if (!LavenderePdaConfig.ocultaCamposCalculadosFichaFinanceira.contains("6")) {
    	        UiUtil.add(tabPanel, new LabelName(Messages.FICHAFINANCEIRA_LABEL_VLSALDOCONSIGNACAOCLIENTE), lbVlSaldoConsignacaoCliente, getLeft(), getNextY());
        	}
    	}
    }

    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
		barBottomContainer.setVisible(true);
    }	
    
    public void setActiveTabTitulosItem() {
    	tabDinamica.setActiveTab(TABPANEL_TITULOS);
    }
    
    public void setActiveTabRede() {
    	tabDinamica.setActiveTab(TABPANEL_REDE);
    }
    
    @Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
		case ResizeListEvent.RESIZE_PRESS:
			try {
				((ListTituloFinanceiroForm) tabDinamica.getActiveContainer()).onEvent(event);
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
			break;
		case ControlEvent.PRESSED:
			if (event.target == btAprovarNfDevolucao) {
				btAprovarNfDevolucao();
			} else if (event.target == btReprovarNfDevolucao) {
				btReprovarNfDevolucao();
			}
			break;
		case TabContainerEvent.PUSHGROUP_TAB_PRESSED: {
			ScrollTabbedContainer scrollTabbedContainer = (ScrollTabbedContainer) event.target;
			if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira) {
				String activeTab = scrollTabbedContainer.activeTabTitle;
				if (ValueUtil.valueEquals(Messages.NFDEVOLUCAO_NOME_ENTIDADE, activeTab.trim())) {
					btAprovarNfDevolucao.setVisible(true);
					btReprovarNfDevolucao.setVisible(true);
				} else {
					btAprovarNfDevolucao.setVisible(false);
					btReprovarNfDevolucao.setVisible(false);
				}
			}
			break;
		}
		}
	}
	
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbTipoPagamento) {
					cbTipoPagamentoChange();
				}
				break;
			}
			}

		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void cbTipoPagamentoChange() throws SQLException {
		String tipoPagamentoSelecionado = cbTipoPagamento.getValue();
		ListTituloFinanceiroForm tabPanelTitulos = (ListTituloFinanceiroForm) tabDinamica.getContainer(TABPANEL_TITULOS);
		tabPanelTitulos.cdTipoPagamento = tipoPagamentoSelecionado;
		
		tabPanelTitulos.list();
	}

//	@Override
	protected void voltarClick() throws SQLException {
		super.voltarClick();
		if (LavenderePdaConfig.isUsaMotivoAtrasoClienteAtrasado() && isAcessoPopUpMotivo) {
			close();
			CadClienteMenuForm cadClienteMenuForm = new CadClienteMenuForm();
			int qtDiasAtrasoCliente = TituloFinanceiroService.getInstance().getDiasAtrasoCliente(SessionLavenderePda.getCliente());
			String[] params = ClienteService.getInstance().getClienteInadimplementeParams(SessionLavenderePda.getCliente(), qtDiasAtrasoCliente);
			cadClienteMenuForm.showPopUpMotivoClienteAtrasado(params, clienteInadiplenteException);
		}
	}

	@Override
	protected void addComponentesFixosFim() throws SQLException {
		super.addComponentesFixosFim();
		if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira) {
			UiUtil.add(barBottomContainer, btAprovarNfDevolucao, 5);
			UiUtil.add(barBottomContainer, btReprovarNfDevolucao, 1);
			
		}
	}
	
	private void btAprovarNfDevolucao() throws SQLException {
		listNfDevolucaoform.btAprovarClick();
		
	}
	
	private void btReprovarNfDevolucao() throws SQLException {
		listNfDevolucaoform.btReprovarClick();
		
	}
}
