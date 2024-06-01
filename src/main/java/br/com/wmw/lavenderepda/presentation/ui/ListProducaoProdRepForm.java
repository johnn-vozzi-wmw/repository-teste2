package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ProducaoProd;
import br.com.wmw.lavenderepda.business.domain.ProducaoProdRep;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ProducaoProdRepService;
import br.com.wmw.lavenderepda.business.service.ProducaoProdService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.PeriodoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProducaoProdRepForm extends BaseCrudListForm {

	private static int TABPANEL_DISPONIVEL = 0;
	private static int TABPANEL_RESERVADO = 1;
	
	private static final String COLUNA_DISPONIVEL = "3";
	
	private RepresentanteSupervComboBox cbRepresentante;
	private PeriodoComboBox cbPeriodo;
	private ButtonAction btRateio;
	private ScrollTabbedContainer tabs;
	private BaseGridEdit gridReservado;
	private Vector producaoProdRepList;
	private double qtTtReserva;
	private double qtTtRealizado;
	private double qtTtRestante;
	private LabelTotalizador lbTtReserva;
	private LabelTotalizador lbTtRealizado;
	private LabelTotalizador lbTtRestante;
	private EditNumberFrac edControlGrid;
	
    public ListProducaoProdRepForm(boolean usuarioSupervisor) throws SQLException {
        super(Messages.PRODUCAOPRODREP_TITULO_CADASTRO);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_SELECT_ONE_ITEM);
        cbPeriodo = new PeriodoComboBox(PeriodoComboBox.PERIODOPRODUCAOPRODREP);
        btRateio = new ButtonAction(Messages.PRODUCAOPRODREP_LABEL_RESERVAR, "images/add.png");
        tabs = new ScrollTabbedContainer(new String[]{Messages.PRODUCAOPRODREP_LABEL_TAB_DISPONIVEL, Messages.PRODUCAOPRODREP_LABEL_TAB_RESERVADO});
        producaoProdRepList = new Vector();
        btRateio.setVisible((LavenderePdaConfig.isUsaRateioProducaoPorRepresentante() || LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao()) && ProducaoProdService.getInstance().isPermiteRealizarRateio(cbPeriodo.getValue()));
        lbTtReserva = new LabelTotalizador(Messages.LABEL_TT_RESERVA);  
        lbTtRealizado = new LabelTotalizador(Messages.LABEL_TT_REALIZADO);
        lbTtRestante = new LabelTotalizador(Messages.LABEL_TT_RESTANTE);
        lbTtReserva.setVisible(false); 
		lbTtRealizado.setVisible(false);
		lbTtRestante.setVisible(false);
        qtTtReserva = 0;
        qtTtRealizado = 0;
        qtTtRestante = 0;
        edControlGrid = new EditNumberFrac("00000", 9, 0);
    }
    
    
    @Override
    protected CrudService getCrudService() {
        return ProducaoProdService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		ProducaoProd domainFilter = new ProducaoProd();
		domainFilter.dsProdutoFilter = "%" + edFiltro.getText() + "%";
		String dsPeriodo = cbPeriodo.getValue();
		domainFilter.dtInicial = DateUtil.getDateValue((StringUtil.split(dsPeriodo, '-'))[0].trim());
		domainFilter.dtFinal = DateUtil.getDateValue((StringUtil.split(dsPeriodo, '-'))[1].trim());
		return domainFilter;
	}
    
    @Override
    protected Vector getDomainList() throws SQLException {
    	qtTtReserva = 0;
        qtTtRealizado = 0;
        qtTtRestante = 0;
    	Vector domainList = new Vector();
    	producaoProdRepList = new Vector();
    	if (ValueUtil.isNotEmpty(cbPeriodo.getValue())) {
    		domainList = super.getDomainList(getDomainFilter());
    		domainList = removeProducaoRateada(domainList);
    	} else {
    		return domainList;
    	}
    	carregaGridReservado();
    	lbTtReserva.setValue(Messages.LABEL_TT_RESERVA + " " + StringUtil.getStringValueToInterface(qtTtReserva));
        lbTtRealizado.setValue(Messages.LABEL_TT_REALIZADO + " " + StringUtil.getStringValueToInterface(qtTtRealizado));
        lbTtRestante.setValue(Messages.LABEL_TT_RESTANTE + " " + StringUtil.getStringValueToInterface(qtTtRestante));
        barBottomContainer.reposition();
    	return domainList;
    }
    
    private Vector removeProducaoRateada(Vector domainList) throws SQLException {
		int size = domainList.size();
		for (int i = 0; i < size; i++) {
			ProducaoProd producaoProd = (ProducaoProd) domainList.items[i];
			if (producaoProd != null) {
				ProducaoProdRep producaoProdRep = ProducaoProdRepService.getInstance().findByProducaoProd(producaoProd, getCdRepresentante());
				if (producaoProdRep != null) {
					producaoProd.qtdReserva = producaoProdRep.qtdRateioProducao;
					producaoProdRepList.addElement(producaoProd);
				}
			}
		}
		if (LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao()) {
			if (!ProducaoProdService.getInstance().isPermiteRealizarRateio(cbPeriodo.getValue())) {
				domainList.removeAllElements();
				return domainList;
			}
			return domainList;
		}
		VectorUtil.removeObjectsByList(domainList, producaoProdRepList);
    	return domainList;
	}

	@Override
    protected String[] getItem(Object domain) {
        ProducaoProd producaoProd = (ProducaoProd) domain;
        String[] item = {
                StringUtil.getStringValue(producaoProd.rowKey),
                StringUtil.getStringValue(producaoProd.toString()),
                StringUtil.getStringValueToInterface(producaoProd.qtdProducaoProd),
                StringUtil.getStringValueToInterface(producaoProd.qtdDisponivel),
                LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao() ? "" : StringUtil.getStringValueToInterface(producaoProd.qtdReserva, ValueUtil.doublePrecisionInterface)};
        return item;
    }
	
	@Override
	public void list() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && cbRepresentante.isValueSelectedEmpty()) {
			clearGrid();
			return;
		}
		super.list();
		destacaLinhasProdutoReservado();
	}
	
    @Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(barBottomContainer, btRateio, RIGHT - WIDTH_GAP * 2, CENTER);
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	} else {
        	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_PERIODO), cbPeriodo, getLeft(), AFTER + HEIGHT_GAP);
    	UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP);
    	UiUtil.add(this, tabs, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - getBottomTab());
    	//-
    	GridColDefinition[] gridColDefiniton1 = createGridDisponivel();
        gridEdit = UiUtil.createGridEdit(gridColDefiniton1);
        UiUtil.add(getFirstContainer(), gridEdit, LEFT, TOP, FILL, FILL);
    	//--
        GridColDefinition[] gridColDefiniton2 = createGridReservado();
        gridReservado = UiUtil.createGridEdit(gridColDefiniton2);
        int totalizerGap = UiUtil.getTotalizerGap();
        UiUtil.add(barBottomContainer, lbTtReserva, LEFT + totalizerGap, TOP + HEIGHT_GAP, PREFERRED, PREFERRED);
        UiUtil.add(barBottomContainer, lbTtRealizado, RIGHT - totalizerGap, TOP + HEIGHT_GAP, PREFERRED, PREFERRED);
        UiUtil.add(barBottomContainer, lbTtRestante, LEFT + totalizerGap, BOTTOM - HEIGHT_GAP, PREFERRED, PREFERRED);
        UiUtil.add(getSecondContainer(), gridReservado, LEFT, TOP, FILL, FILL);
        //--
        gridSettings();
    }

	private GridColDefinition[] createGridDisponivel() {
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_PRODUTO, -40, LEFT),
            new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_PLANEJADO, -20, LEFT),
            new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_TAB_DISPONIVEL, -20, LEFT),
            new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_RESERVA, -20, LEFT)};
		return gridColDefiniton;
	}

	private GridColDefinition[] createGridReservado() {
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_PRODUTO, -40, LEFT),
			new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_RESERVA, -20, LEFT),
			new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_REALIZADO, -20, LEFT),
			new GridColDefinition(Messages.PRODUCAOPRODREP_LABEL_RESTANTE, -20, LEFT)};
		return gridColDefiniton;
	}
    
    protected int getBottomTab() {
		return barBottomContainer.isVisible() ? barBottomContainer.getHeight() : 0;
	}
    
    @Override
    public void onFormShow() throws SQLException {
		super.onFormShow();
		tabs.setActiveTab(TABPANEL_DISPONIVEL);
		tabs.requestFocus();
	}
    
    private Container getFirstContainer() {
		return tabs.getContainer(TABPANEL_DISPONIVEL);
	}

	private BaseScrollContainer getSecondContainer() {
		return (BaseScrollContainer)tabs.getContainer(TABPANEL_RESERVADO);
	}

    private void gridSettings() {
    	if (LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao() || LavenderePdaConfig.isUsaRateioProducaoPorRepresentante()) {
    		edControlGrid = gridEdit.setColumnEditableDoubleNoCalc(4, true, 9, ValueUtil.doublePrecisionInterface, 0);
			edControlGrid.autoSelect = true;
			edControlGrid.enableTeclado = !LavenderePdaConfig.usaTecladoFixoTelaItemPedido;
			gridEdit.setGridControllable();
			gridEdit.disableSort = true;
			gridEdit.gridController.colDisableList.addElement(COLUNA_DISPONIVEL);
    	}
	}
    
    @Override
    protected void filtrarClick() throws SQLException {
		if (ValueUtil.isNotEmpty(cbPeriodo.getValue())) {
			list();
		} else {
			UiUtil.showErrorMessage(Messages.PRODUCAOPRODREP_VALIDACAO_PERIODO_NAOSELECIONADO);
		}
    }
    
    private void destacaLinhasProdutoReservado() throws SQLException {
    	if (LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao()) {
    		int size = gridEdit.size();
    		for (int i = 0; i < size; i++) {
    			String[] rowGrid = gridEdit.getItem(i);
    			ProducaoProd producaoProd = (ProducaoProd) ProducaoProdService.getInstance().findByRowKey(rowGrid[0]);
    			ProducaoProdRep producaoProdRep = ProducaoProdRepService.getInstance().findByProducaoProd(producaoProd, getCdRepresentante());
    			if (producaoProdRep != null) {
    				gridEdit.gridController.setRowForeColor(LavendereColorUtil.COR_FONTE_PRODUTO_JA_RATEADO, i);
    			}
    		}
    	}
    }
    
    private void reservarProduto() throws SQLException {
    	int size = gridEdit.size();
    	Vector producaoProdList = new Vector();
    	for (int i = 0; i < size; i++) {
    		String[] rowGrid = gridEdit.getItem(i);
    		ProducaoProd producaoProd = (ProducaoProd) ProducaoProdService.getInstance().findByRowKey(rowGrid[0]);
    		producaoProd.qtdReserva = (int) ValueUtil.getDoubleValue(rowGrid[4].replace(".", ""));
    		producaoProd.cdRepresentante = getCdRepresentante();
    		if (producaoProd.qtdReserva != 0) {
    			if (producaoProd.qtdReserva > producaoProd.qtdProducaoProd) {
    				Produto produto = ProdutoService.getInstance().getProduto(producaoProd.cdProduto);
    				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PRODUCAOPRODREP_MSG_ERRO_RATEIO_MARIO_QTPRODUCAO, new Object[]{StringUtil.getStringValueToInterface(producaoProd.qtdReserva), produto.toString(), StringUtil.getStringValueToInterface(producaoProd.qtdProducaoProd)}));
    				return;
    			}
    			producaoProdList.addElement(producaoProd);
    		}
    	}
    	if (ValueUtil.isNotEmpty(producaoProdList)) {
    		Vector producaoProdErro = ProducaoProdService.getInstance().geraRateioProducaoProd(producaoProdList);
    		if (ValueUtil.isNotEmpty(producaoProdErro)) {
    			RelNotificacaoItemWindow relInsercaoForm = new RelNotificacaoItemWindow(producaoProdErro, false);
    			relInsercaoForm.popup();
    		} else {
    			UiUtil.showInfoMessage(Messages.PRODUCAOPRODREP_MSG_RATEIO_SUCESSO);
    		}
    		destacaLinhasProdutoReservado();
    		list();
    	} else {
    		UiUtil.showErrorMessage(Messages.PRODUCAOPRODREP_MSG_VALIDACAO_NENHUM_PROD_RATEIO);
    	}
    }
    
    private void carregaGridReservado() throws SQLException {
    	LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = null;
		String[][] gridItems;
		try {
			gridReservado.setItems(null);
			//---
			domainList =  producaoProdRepList;
			listSize = domainList.size();
			//--
			if (listSize > 0) {
				String[] item = getItemGridReservado(domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItemGridReservado(domainList.items[i]);
				}
				item = null;
				//--
				gridReservado.setItems(gridItems);
			}
		} finally {
			gridItems = null;
			domainList = null;
			msg.unpop();
		}
    }
    
    protected String[] getItemGridReservado(Object domain) throws SQLException {
        ProducaoProd producaoProd = (ProducaoProd) domain;
        producaoProd.cdRepresentante = getCdRepresentante();
        ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.nuMaxTentativas = 1;
		LavendereWeb2Tc lwWeb2Tc = new LavendereWeb2Tc(ps);
		String newEstoque = null;
		String newQtdReserva = null;
		Estoque estoque = new Estoque();
		try {
			newEstoque = lwWeb2Tc.getEstoqueServidorAndUpdatePda(SessionLavenderePda.cdEmpresa, getCdRepresentante(), producaoProd.cdProduto, null);
			estoque = EstoqueService.getInstance().getEstoque(producaoProd.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
			if (newEstoque != null) {
				estoque.qtEstoque = ValueUtil.getDoubleValue(newEstoque);
			}
			newQtdReserva = lwWeb2Tc.getProducaoProdRep(SessionLavenderePda.cdEmpresa, getCdRepresentante(), producaoProd.cdProduto, producaoProd.dtInicial.toString().replaceAll("/", "-"), producaoProd.dtFinal.toString().replaceAll("/", "-"));
			if (newQtdReserva != null) {
				producaoProd.qtdReserva = ValueUtil.getDoubleValue(newQtdReserva);
				atualizarProducaoProdRep(producaoProd);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
        boolean periodoVigente = ProducaoProdService.getInstance().isPeriodoVigente(DateUtil.getCurrentDate(), cbPeriodo.getValue());
        qtTtReserva += producaoProd.qtdReserva;
        qtTtRealizado += periodoVigente ? producaoProd.qtdReserva - estoque.qtEstoque : 0;
        qtTtRestante += periodoVigente ? estoque.qtEstoque : producaoProd.qtdReserva;
        String[] item = {
                StringUtil.getStringValue(producaoProd.rowKey),
                StringUtil.getStringValue(producaoProd.toString()),
                StringUtil.getStringValueToInterface(producaoProd.qtdReserva),
                StringUtil.getStringValueToInterface(periodoVigente ? producaoProd.qtdReserva - estoque.qtEstoque : 0),
                StringUtil.getStringValueToInterface(periodoVigente ? estoque.qtEstoque : producaoProd.qtdReserva)
        };
        return item;
    }


	private void atualizarProducaoProdRep(ProducaoProd producaoProd) throws SQLException {
		ProducaoProdRep pprep = new ProducaoProdRep();
		pprep.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pprep.cdRepresentante = getCdRepresentante();
		pprep.cdProduto = producaoProd.cdProduto;
		pprep.dtInicial = producaoProd.dtInicial;
		pprep.dtFinal = producaoProd.dtFinal;
		pprep.qtdRateioProducao = producaoProd.qtdReserva;
		ProducaoProdRepService.getInstance().update(pprep);
	}
    
    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbPeriodo) {
					list();
					if (ProducaoProdService.getInstance().isPermiteRealizarRateio(cbPeriodo.getValue())) {
						if (LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao()) {
							btRateio.setVisible(tabs.getActiveTab() == TABPANEL_DISPONIVEL);
							gridEdit.setColumnEditableDoubleNoCalc(4, true, 9, ValueUtil.doublePrecisionInterface, 0);
						} else {
							btRateio.setVisible(LavenderePdaConfig.isUsaRateioProducaoPorRepresentante() && tabs.getActiveTab() == TABPANEL_DISPONIVEL);
							gridEdit.setColumnEditableDoubleNoCalc(4, LavenderePdaConfig.isUsaRateioProducaoPorRepresentante(), 9, ValueUtil.doublePrecisionInterface, 0);
						}
					} else {
						btRateio.setVisible(false);
						gridEdit.setColumnEditableDoubleNoCalc(4, false, 9, ValueUtil.doublePrecisionInterface, 0);
					}
				} else if (event.target == btRateio) {
					reservarProduto();
				} else if (event.target == tabs && tabs.getActiveTab() == TABPANEL_DISPONIVEL) {
					btRateio.setVisible((LavenderePdaConfig.isUsaRateioProducaoPorRepresentante() || LavenderePdaConfig.isUsaRateioProducaoPorRepresentantePermiteEdicao()) && ProducaoProdService.getInstance().isPermiteRealizarRateio(cbPeriodo.getValue()));
					lbTtReserva.setVisible(false); 
					lbTtRealizado.setVisible(false);
					lbTtRestante.setVisible(false);
				} else if (event.target == tabs && tabs.getActiveTab() == TABPANEL_RESERVADO) {
					btRateio.setVisible(false);
					lbTtReserva.setVisible(true); 
					lbTtRealizado.setVisible(true);
					lbTtRestante.setVisible(true);
				} else if (event.target == cbRepresentante) {
					cbRepresentante.setValue(getCdRepresentante());
					cbPeriodo.reload(PeriodoComboBox.PERIODOPRODUCAOPRODREP);
					list();
				}
				break;
			}
    	}
    }
    
	@Override
    public void loadDefaultFilters() throws SQLException {
    	super.loadDefaultFilters();
    	cbRepresentante.setSelectedIndex(0);
    }
    
    private String getCdRepresentante() {
    	return SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentante.getValue() : SessionLavenderePda.getRepresentante().cdRepresentante;
    }

}