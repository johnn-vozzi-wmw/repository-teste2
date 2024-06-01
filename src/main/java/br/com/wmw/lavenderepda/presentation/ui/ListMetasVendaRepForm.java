package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.chart.BaseColumnChart;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Meta;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.MetaAcompanhamentoService;
import br.com.wmw.lavenderepda.business.service.MetaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListMetasVendaRepForm extends LavendereCrudListForm {

	private LabelValue lbQtPedidos;
	private LabelName lbPedidos;
	private LabelName lbTtVendas;
	private LabelValue lbTotalVendas;
	private LabelName lbRep;
	private RepresentanteSupervComboBox cbRepresentante;
	private String cdRepresentante;
	private ButtonOptions bmOpcoes;
	private BaseColumnChart columnChart;
	private PushButtonGroupBase btGraficoLista;

	private String MENU_UTILITARIO_CALCULADORA = "";
	private String labelRealVendas = Messages.PRODUTIVIDADE_LABEL_VLREALIZADOVENDAS;
	private String labelMetasVendas = Messages.METAS_VLMETA;
	private int qtPedidosRealizadoTotal = 0;
	private double vlRealizadoMetaTotal = 0;
	private boolean topFilters;
	
	private BarContainer barTopListContainer;
	public ButtonAction btResize;
	
    public ListMetasVendaRepForm(String cdRepresentante) throws SQLException {
        super(Messages.METAS_VENDAREP_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadMetasVendaForm());
        singleClickOn = true;
        this.cdRepresentante = cdRepresentante;
        lbRep = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_ALL);
        cbRepresentante.setValue(this.cdRepresentante);
        lbQtPedidos = new LabelValue("9999999999");
        lbTotalVendas = new LabelValue("999999999,999");
        lbTtVendas = new LabelName(Messages.PRODUTIVIDADE_LABEL_TOTALVENDAS);
        lbPedidos = new LabelName(Messages.METAS_LABEL_PEDIDOS);
        btGraficoLista = new PushButtonGroupBase(getTableButtonNames(), true, 1, -1, 10, 1, true, PushButtonGroup.NORMAL);
        btGraficoLista.setFont(UiUtil.defaultFontSmall);
        bmOpcoes = new ButtonOptions();
		btResize = new ButtonAction("");
		btResize.setBorder(BORDER_NONE);
		btResize.transparentBackground = true;   
		barTopListContainer = new BarContainer();
        constructorListContainer();
    }
    
	private String[] getTableButtonNames() {
		String[] btTabNames = new String[2];
		btTabNames[0] = Messages.META_VENDA_LABEL_BT_TAB_LISTA;
		btTabNames[1] = Messages.META_VENDA_LABEL_BT_TAB_GRAFICO;
		return btTabNames;
	}

    private void constructorListContainer() {
    	configListContainer("NUSEQUENCIA");
    	listContainer = new GridListContainer(18 , 6);
    	listContainer.setColsSort(new String[][]{
    			{Messages.METAS_PERIODO,"DSPERIODO"},
    			{labelRealVendas,"VLREALIZADOVENDAS"},
    			{labelMetasVendas,"VLMETAVENDAS"}
    	});
    	//linha1
    	listContainer.setColPosition(4, RIGHT);
    	listContainer.setColPosition(5, BEFORE);
    	//linha2
    	listContainer.setColPosition(11, RIGHT);
    	//linha 3
    	listContainer.setColPosition(16, RIGHT);
    	listContainer.setColPosition(17, BEFORE);
    	
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetaService.getInstance();
    }

    private String getCdRepresentante() {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (cbRepresentante.getSelectedIndex() == 0) {
    			return "";
    		} else {
    			return cbRepresentante.getValue();
    		}
    	} else {
    		return cdRepresentante;
    	}
    }

    private void updateTotalizadores() throws SQLException {
    	int qtPedidosTotal = 0;
    	double vlTotalPedidos = 0d;
    	if (LavenderePdaConfig.usaSistemaModoOffLine) {
    		qtPedidosTotal = qtPedidosRealizadoTotal;
    		vlTotalPedidos = vlRealizadoMetaTotal;
    	} else {
    		Meta metaFilter = new Meta();
    		metaFilter.cdRepresentante = getCdRepresentante();
    		metaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		//--
    		qtPedidosTotal = ValueUtil.getIntegerValue(getCrudService().sumByExample(metaFilter, "qtPedidos"));
    		vlTotalPedidos = 0.0;
    		if (LavenderePdaConfig.usaSistemaModoOffLine) {
    			vlTotalPedidos = MetaAcompanhamentoService.getInstance().getVlRealizadoMeta(getCdRepresentante());
    		} else {
    			vlTotalPedidos = getCrudService().sumByExample(metaFilter, "vlRealizadoVendas");
    		}
    	}
		lbQtPedidos.setValue(qtPedidosTotal);
		lbTotalVendas.setValue(vlTotalPedidos);
		
		lbTotalVendas.reposition();
		lbTtVendas.reposition();
		lbQtPedidos.reposition();
		lbPedidos.reposition();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new Meta();
    }

    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	if (ValueUtil.isEmpty(getCdRepresentante())) {
    		return MetaService.getInstance().findAllMetasGroupByPeriodo(SessionLavenderePda.cdEmpresa);
    	} else {
    		Meta metaFilter = (Meta) domain;
    		metaFilter.cdRepresentante = getCdRepresentante();
    		metaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		Vector list = getCrudService().findAllByExample(metaFilter);
    		return list;
    	}
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	String labelPedidos = Messages.METAS_LABEL_PEDIDOS + "  ";
        Meta meta = (Meta) domain;
        if (LavenderePdaConfig.usaSistemaModoOffLine) {
    		MetaAcompanhamentoService.getInstance().getMetaAcompanhamentoList(meta);
    		MetaService.getInstance().recalculaMeta(meta , DateUtil.getCurrentDate());
    		vlRealizadoMetaTotal += meta.vlRealizadoVendas;
    		qtPedidosRealizadoTotal += meta.qtPedidos;
        }
        	String[] item = {
        			//linha 1
        			/*0*/ StringUtil.getStringValue(meta.dsPeriodo),
        			/*1*/ " ",
        			/*2*/ " ",
        			/*3*/ " ",
        			/*4*/ " " + labelPedidos,
        			/*5*/ StringUtil.getStringValueToInterface(meta.qtPedidos),
        			//linha 2
        			/*6*/ labelMetasVendas,
        			/*7*/ " ",
        			/*8*/ " ",
        			/*9*/ " ",
        			/*10*/ " ",
        			/*11*/ labelRealVendas,
        			//linha 3
        			/*12*/ StringUtil.getStringValueToInterface(meta.vlMetaVendas),
        			/*13*/ " ",
        			/*14*/ " ",
        			/*15*/ " ",
        			/*16*/ " (" + StringUtil.getStringValueToInterface(meta.getPctRealizadoMeta()) + "%)",
        			/*17*/ StringUtil.getStringValueToInterface(meta.vlRealizadoVendas)
        			};
        	return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, lbRep, cbRepresentante, getLeft(), getTop() + HEIGHT_GAP);
    		UiUtil.add(this, btGraficoLista, CENTER, AFTER + WIDTH_GAP_BIG, PREFERRED, UiUtil.getControlPreferredHeight());
    	}else {
    		UiUtil.add(this, btGraficoLista, CENTER, getTop() + WIDTH_GAP_BIG, PREFERRED, UiUtil.getControlPreferredHeight());
    	}
    	UiUtil.add(this, barTopListContainer, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getListContainerBarTopHeight());
		UiUtil.add(this, listContainer, LEFT, SAME, FILL, FILL - barBottomContainer.getHeight());
		
		int iconsDefaultWidthHeight = (int)(((double)barTopListContainer.getHeight() / 5) * 4);
		btResize.setImage(getBtResizeImage(iconsDefaultWidthHeight));
		UiUtil.add(barTopListContainer, btResize, RIGHT - barTopListContainer.getHeight() / 4, CENTER, iconsDefaultWidthHeight, iconsDefaultWidthHeight);		

		UiUtil.add(this, lbTotalVendas,RIGHT - HEIGHT_GAP, BOTTOM - barBottomContainer.getHeight());
		UiUtil.add(this, lbTtVendas, BEFORE, SAME);
		UiUtil.add(this, lbQtPedidos, BEFORE - HEIGHT_GAP_BIG, SAME);
		UiUtil.add(this, lbPedidos, BEFORE, SAME);
      	
        createGraficoMeta();
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
    	visibleState();
    }

    private void createGraficoMeta() throws SQLException {
    	Vector metasVendaList;
    	if (ValueUtil.isEmpty(getCdRepresentante())) {
    		metasVendaList = MetaService.getInstance().findAllMetasGroupByPeriodo(SessionLavenderePda.cdEmpresa);
    	} else {
    		Meta metaFilter = new Meta();
    		metaFilter.cdRepresentante = getCdRepresentante();
    		metaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		metasVendaList =  getCrudService().findAllByExample(metaFilter);
    	}
    	int size = metasVendaList.size();
    	Vector xGroups = new Vector(size);
    	for (int i = 0; i < size; i++) {
    		xGroups.addElement(((Meta)metasVendaList.items[i]).dsPeriodo);
    	}
    	String[] xLabels = new String[]{Messages.METAS_PERIODO};
    	if (xGroups.size() > 0) {
    		xLabels = (String[])xGroups.toObjectArray();
    	}
    	if (columnChart != null) {
    		remove(columnChart);
    	}
        columnChart = new BaseColumnChart(Messages.META_VENDA_TITULO_GRAFICO_PERIODO, xLabels);
        //--
        double[] vlMeta = new double[metasVendaList.size()];
        double[] vlRealizado = new double[metasVendaList.size()];
        double maxValue = 10;
    	for (int i = 0; i < metasVendaList.size(); i++) {
    		Meta meta = (Meta)metasVendaList.items[i];
            if (LavenderePdaConfig.usaSistemaModoOffLine) {
        		MetaAcompanhamentoService.getInstance().getMetaAcompanhamentoList(meta);
        		MetaService.getInstance().recalculaMeta(meta , DateUtil.getCurrentDate());
            }
    		vlMeta[i] = meta.vlMetaVendas;
    		vlRealizado[i] = meta.vlRealizadoVendas;
    		if (maxValue < meta.vlMetaVendas) {
    			maxValue = meta.vlMetaVendas;
    		}
    		if (maxValue < meta.vlRealizadoVendas) {
    			maxValue = meta.vlRealizadoVendas;
    		}
    	}
    	if (vlMeta.length == 0) {
    		vlMeta = new double[]{0};
    	}
    	if (vlRealizado.length == 0) {
    		vlRealizado = new double[]{0};
    	}
    	columnChart.series.addElement(new Series(Messages.META_VENDA_LABEL_VLMETA, vlMeta, Color.getRGB(79, 148, 205))); //AZUL
    	columnChart.series.addElement(new Series(Messages.META_VENDA_LABEL_VLREALIZADO, vlRealizado, Color.getRGB(162, 205, 90))); //VERDE
    	// Arredonda os valores de intervalos da grid
		double result = maxValue / 8;
		int nuConversaoUnidades = 1;
		if (result < 100) {
			nuConversaoUnidades = 10;
		} else if (result < 1000) {
			nuConversaoUnidades = 100;
		} else if (result < 10000) {
			nuConversaoUnidades = 1000;
		} else if (result < 100000) {
			nuConversaoUnidades = 10000;
		} else if (result < 1000000) {
			nuConversaoUnidades = 100000;
		}
		int newValue = ValueUtil.getIntegerValue(result / nuConversaoUnidades) * nuConversaoUnidades;
		newValue *= 8;
		maxValue = newValue > maxValue ? newValue : maxValue;
		if (maxValue > 0) {
			columnChart.setYAxis(0, ValueUtil.getIntegerValue(maxValue), 8);
		}
		UiUtil.add(this, columnChart, LEFT, barTopListContainer.getY2(), FILL, FILL - (barBottomContainer.getHeight()));    	
        metasVendaList = null;
    }

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	MENU_UTILITARIO_CALCULADORA = Messages.MENU_UTILITARIO_CALCULADORA;
    	bmOpcoes.addItem(MENU_UTILITARIO_CALCULADORA);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		case ControlEvent.PRESSED: {
    			if (event.target == btGraficoLista) {
     				visibleState();
     			} else if (event.target == cbRepresentante) {
					if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
					}
					createGraficoMeta();
					list();
					visibleState();
				} else if (event.target == btResize) {
					if(!topFilters) {
						disableFieldValues();
					} else {
						enableFieldsValues();
					}
					topFilters = !topFilters;
				}
    			break;
    		}
    		case ButtonOptionsEvent.OPTION_PRESS : {
    			if (event.target == bmOpcoes) {
 					if (bmOpcoes.selectedItem.equals(MENU_UTILITARIO_CALCULADORA)) {
 						(new Calculator()).popup();
 					}
     			}
    			break;
    		}
    	}
    }

    public void detalhesClick() throws SQLException {
    	if (ValueUtil.isNotEmpty(getCdRepresentante())) {
    		super.detalhesClick();
    	}
    }

    public void visibleState() {
    	super.visibleState();
    	boolean showingChart = btGraficoLista.getSelectedIndex() == 1;
    	columnChart.setVisible(showingChart);
    	listContainer.setVisible(!showingChart);
    	lbPedidos.setVisible(!showingChart);
        lbQtPedidos.setVisible(!showingChart);
        lbTtVendas.setVisible(!showingChart);
        lbTotalVendas.setVisible(!showingChart);
        barTopListContainer.setVisible(showingChart);
        cbRepresentante.setVisible(SessionLavenderePda.isUsuarioSupervisor());
    }

    public void list() throws SQLException {
    	super.list();
    	updateTotalizadores();
    }

	private void disableFieldValues() {
		barTopListContainer.setRect(LEFT, TOP + barTopContainer.getY2() + 1, FILL, UiUtil.getListContainerBarTopHeight());
		columnChart.setRect(LEFT, barTopListContainer.getY2() + HEIGHT_GAP_BIG, FILL, FILL - (barBottomContainer.getHeight() + 4));
		setVisibleState(false);
	}

	private void enableFieldsValues() {
		barTopListContainer.setRect(LEFT, btGraficoLista.getY2() + HEIGHT_GAP + 1, FILL, UiUtil.getListContainerBarTopHeight());
		columnChart.setRect(LEFT, barTopListContainer.getY2() + HEIGHT_GAP_BIG, FILL, FILL - (barBottomContainer.getHeight() + 4));
		setVisibleState(true);
	}    
	
	private void setVisibleState(boolean state) {
		lbRep.setVisible(state);
		cbRepresentante.setVisible(state);
		btGraficoLista.setVisible(state);
	}		
	
	private Image getBtResizeImage(int imageHeigth) {
		Image resizeImg = UiUtil.getImage("images/resize.png");
		resizeImg.applyColor2(ColorUtil.baseForeColorSystem);
		return UiUtil.getSmoothScaledImage(resizeImg, imageHeigth, imageHeigth);
	}		
	
	@Override
	public void reposition() {
		super.reposition();
		if(topFilters) {
			disableFieldValues();
		} else {
			enableFieldsValues();
		}
	}	
}