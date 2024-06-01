package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.chart.BaseColumnChart;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCli;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.MetaVendaCliService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListMetaVendaCliForm extends LavendereCrudListForm {
	
	private Container containerTabs;
	private LabelName lbReppresentante;
	private LabelName lbCliente;
	private LabelName lbMeta;
	private RepresentanteSupervComboBox cbRepresentante;
	private ButtonOptions bmOpcoes;
	private PushButtonGroupBase btGraficoLista;
	private BaseColumnChart columnChart;
	private String cdClienteFilter;
	private String dsMetaFilter;
	private ButtonAction btTodosClientes;
	private EditFiltro edFiltroCli;
	private EditFiltro edFiltroMeta;
	public Button btResize;
	public boolean flListInicialized;
	private boolean topFilters;
	private BarContainer barTopListContainer;
	private String MENU_UTILITARIO_CALCULADORA = "";
	private final String SORT_ATRIBUTTE_DEFAULT = "DTINICIALVIGENCIA";
	private final String COL_CDMETAVENDACLI = "CDMETAVENDACLI";
	private final String COL_VLMETA = "vlMeta";
	private final String COL_VLREALIZADO = "vlRealizado";
	private final String COL_PCTRESTANTE = "pctRestante";
	private final String COL_DTINICIALVIGENCIA = "dtInicialVigencia";
	private final String COL_DTFINALVIGENCIA = "dtFinalVigencia";
	public static final String LABEL_TODOS = Messages.LABEL_TODOS;

	public ListMetaVendaCliForm() throws SQLException {
		super(Messages.META_VENDA_CLIENTE);
		singleClickOn = true;
		lbReppresentante = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
		cbRepresentante = new RepresentanteSupervComboBox(RepresentanteSupervComboBox.DefaultItemType_ALL);
        cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
        if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
        	cbRepresentante.setSelectedIndex(0);
        }
		flListInicialized = false;
		sortAtributte = SORT_ATRIBUTTE_DEFAULT;
		sortAsc = ValueUtil.VALOR_SIM;
		edFiltroCli = new EditFiltro("999999999", 10);
		edFiltroMeta = new EditFiltro("999999999", 10);
		lbCliente = new LabelName(Messages.META_VENDA_LABEL_CLIENTE);
		lbMeta = new LabelName(Messages.META_VENDA_DESCRICAO);
		btTodosClientes = new ButtonAction(Messages.BOTAO_REL_COMPLETO, UiUtil.getColorfulImage("images/list.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		bmOpcoes = new ButtonOptions();
		
		barTopListContainer = new BarContainer();
		containerTabs = new BaseScrollContainer(false, false);
		btResize = new Button("");
		btResize.setBorder(BORDER_NONE);
		btResize.transparentBackground = true;
		
		btGraficoLista = new PushButtonGroupBase(getTableButtonNames(), true, 1, -1, 10, 1, true, PushButtonGroup.NORMAL);
		btGraficoLista.setFont(UiUtil.defaultFontSmall);
		columnChart = new BaseColumnChart(Messages.META_VENDA_TITULO_GRAFICO, new String[] { "" });
		columnChart.showCategories = false;
		constructorListContainer();
	}

	private Image getBtResizeImage(int imageHeigth) {
		Image resizeImg = UiUtil.getImage("images/resize.png");
		resizeImg.applyColor2(ColorUtil.baseForeColorSystem);
		return UiUtil.getSmoothScaledImage(resizeImg, imageHeigth, imageHeigth);
	}

	private String[] getTableButtonNames() {
		String[] btTabNames = new String[2];
		btTabNames[0] = Messages.META_VENDA_LABEL_BT_TAB_LISTA;
		btTabNames[1] = Messages.META_VENDA_LABEL_BT_TAB_GRAFICO;
		return btTabNames;
	}
	
	protected BaseDomain getDomainFilter() throws SQLException {
		return new MetaVendaCli();
	}

	protected CrudService getCrudService() throws SQLException {
		return MetaVendaCliService.getInstance();
	}

	private String getCdRepresentante() {
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			return SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		if (cbRepresentante.getSelectedIndex() == 0) {
			return "";
		}
		return cbRepresentante.getValue();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		MetaVendaCli metaVendaCliFilter = getMetaVendaCliFilter(domain);
		Vector listMetaVendaCliCached = MetaVendaCliService.getInstance().findAllMetaVendaCli(metaVendaCliFilter);
		int size = listMetaVendaCliCached.size();
		
		double vlTotalMeta = 0;
		double vlTotalRealizado = 0;
		for (int i = 0; i < size; i++) {
			metaVendaCliFilter = (MetaVendaCli) listMetaVendaCliCached.items[i];
			MetaVendaCliService.getInstance().calculaVlMetaVlRealizadoDaMetaVendaCli(metaVendaCliFilter);
			vlTotalMeta += metaVendaCliFilter.vlMeta;
			vlTotalRealizado += metaVendaCliFilter.vlRealizado;
		}
		metaVendaCliFilter.sortAtributte = sortAtributte;
		metaVendaCliFilter.sortAsc = sortAsc;
		
		createGraficoMeta(vlTotalMeta, vlTotalRealizado);
		configureSortMetaVendaCli(metaVendaCliFilter, listMetaVendaCliCached);
		return listMetaVendaCliCached;
	}

	private void configureSortMetaVendaCli(MetaVendaCli metaVendaCliFilter, Vector list) {
		MetaVendaCli.sortAttr = metaVendaCliFilter.sortAtributte;
		if (COL_PCTRESTANTE.equals(metaVendaCliFilter.sortAtributte) || COL_CDMETAVENDACLI.equals(metaVendaCliFilter.sortAtributte) || COL_VLMETA.equals(metaVendaCliFilter.sortAtributte) || COL_VLREALIZADO.equals(metaVendaCliFilter.sortAtributte)) {
			SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
		} else {
			list.qsort();
		}
		//Ordenação desc
		if (metaVendaCliFilter.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			list.reverse();
		}
	}

	private MetaVendaCli getMetaVendaCliFilter(BaseDomain domain) {
		MetaVendaCli metaVendaCliFilter = (MetaVendaCli) domain;
		metaVendaCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		metaVendaCliFilter.cdRepresentante = getCdRepresentante();
		metaVendaCliFilter.cdCliente = cdClienteFilter;
		metaVendaCliFilter.dsMetaVendaCli = dsMetaFilter;
		metaVendaCliFilter.cdMetaVendaCli = dsMetaFilter;
		metaVendaCliFilter.sortAtributte = null;
		return metaVendaCliFilter;
	}
	
	private void constructorListContainer() {
		configListContainer(COL_CDMETAVENDACLI);
		listContainer = new GridListContainer(getListSize(), 2);
		getSortFields();
		listContainer.setColPosition(5, RIGHT);
		if(LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) {
			listContainer.setColPosition(7, RIGHT);
		}
	}

	private void getSortFields() {
		if(!LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) {
			listContainer.setColsSort(new String[][] {
				{ Messages.META_VENDA_LABEL_DTINICIALVIGENCIA, COL_DTINICIALVIGENCIA },
				{ Messages.META_VENDA_LABEL_DTFINALVIGENCIA, COL_DTFINALVIGENCIA },
				{ Messages.META_VENDA_LABEL_CODIGO, COL_CDMETAVENDACLI }, 
				{ Messages.META_VENDA_LABEL_META, COL_VLMETA }, 
				{ "Descrição", "DSMETAVENDACLI" },
				{ Messages.META_VENDA_LABEL_VALOR_REALIZADO, COL_VLREALIZADO } 
			});
		} else {
			listContainer.setColsSort(new String[][] {
				{ Messages.META_VENDA_LABEL_DTINICIALVIGENCIA, COL_DTINICIALVIGENCIA },
				{ Messages.META_VENDA_LABEL_DTFINALVIGENCIA, COL_DTFINALVIGENCIA },
				{ Messages.META_VENDA_LABEL_CODIGO, COL_CDMETAVENDACLI }, 
				{ "Descrição", "DSMETAVENDACLI" },
				{ Messages.META_VENDA_LABEL_META, COL_VLMETA }, 
				{ Messages.META_VENDA_LABEL_VALOR_REALIZADO, COL_VLREALIZADO },
				{ Messages.META_VENDA_LABEL_PCT_RESTANTE, COL_PCTRESTANTE }
			});			
		}
	}

	private int getListSize() {
		return (!LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) ? 6 : 8;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		MetaVendaCli metaVendaCli = (MetaVendaCli) domain;
		String[] item = new String[getListSize()];
		item[0] = StringUtil.getStringValue(metaVendaCli.cdMetaVendaCli);
		item[1] = " - " + StringUtil.getStringValue(metaVendaCli.dsMetaVendaCli);
		item[2] = "";
		item[3] = metaVendaCli.cliente.toString();
		item[4] = Messages.META_VENDA_LABEL_META + " " + StringUtil.getStringValueToInterface(metaVendaCli.vlMeta);
		item[5] = Messages.META_VENDA_LABEL_VALOR_REALIZADO + " (" + StringUtil.getStringValueToInterface(metaVendaCli.getPctRealizadoMeta()) + "%)";
		if(LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) {
			item[6] = Messages.META_VENDA_LABEL_VL_ATINGIDO + " " + StringUtil.getStringValueToInterface(metaVendaCli.vlRealizado);
			item[7] = Messages.META_VENDA_LABEL_PCT_RESTANTE + " (" + StringUtil.getStringValueToInterface(metaVendaCli.getPctRestanteMeta()) + "%)";
		}
		return item;
	}
	
	@Override
	protected void setPropertiesInRowList(Item currentItem, BaseDomain domain) throws SQLException {
		MetaVendaCli metaVendaCli = (MetaVendaCli)domain;
		if(LavenderePdaConfig.grifaMetaNaoAtingida && metaVendaCli.isMetaNaoAtingida()) {
			listContainer.setContainerBackColor(currentItem, LavendereColorUtil.COR_FUNDO_META_NAO_ATINGIDA);
		}
	}

	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	protected void onFormStart() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			UiUtil.add(this, lbReppresentante, cbRepresentante, getLeft(), getNextY());
		} else {
			cbRepresentante.setVisible(false);
		}
		
		UiUtil.add(this, lbCliente, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, edFiltroCli, getLeft(), AFTER, FIT);
		
		UiUtil.add(this, lbMeta, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, edFiltroMeta, getLeft(), AFTER);
		
		UiUtil.add(this, btGraficoLista, CENTER, getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
		
		UiUtil.add(this, barTopListContainer, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getListContainerBarTopHeight());
		UiUtil.add(this, listContainer, LEFT, SAME, FILL, FILL - barBottomContainer.getHeight());
		int iconsDefaultWidthHeight = (int)(((double)barTopListContainer.getHeight() / 5) * 4);
		btResize.setImage(getBtResizeImage(iconsDefaultWidthHeight));
		UiUtil.add(barTopListContainer, btResize, RIGHT - barTopListContainer.getHeight() / 4, CENTER, iconsDefaultWidthHeight, iconsDefaultWidthHeight);
		
		addItensOnButtonMenu();

		UiUtil.add(barBottomContainer, btTodosClientes, 1);
		UiUtil.add(barBottomContainer, bmOpcoes, 3);
		UiUtil.add(this, columnChart, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - (barBottomContainer.getHeight() + HEIGHT_GAP), barTopListContainer);
		
		loadConfigsPadrao();
	}

	private void createGraficoMeta(double vlMeta, double vlRealizado) {
		columnChart.series.removeAllElements();
		columnChart.series.addElement(new Series(Messages.META_VENDA_LABEL_META, new double[] { vlMeta }, Color.getRGB(79, 148, 205))); //AZUL
		columnChart.series.addElement(new Series(Messages.META_VENDA_LABEL_VALOR_REALIZADO, new double[] { vlRealizado }, Color.getRGB(162, 205, 90))); //VERDE
		double maxValue = vlMeta > vlRealizado ? vlMeta : vlRealizado;
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
	}

	private void addItensOnButtonMenu() {
		bmOpcoes.removeAll();
		MENU_UTILITARIO_CALCULADORA = Messages.MENU_UTILITARIO_CALCULADORA;
		bmOpcoes.addItem(MENU_UTILITARIO_CALCULADORA);
	}
	
	@Override
	public void reposition() {
		super.reposition();
		if(topFilters){
			disableFieldValues();
		} else {
			enableFieldsValues();
		}
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btGraficoLista) {
					visibleState();
				} else if (event.target == cbRepresentante) {
					resetFiltroMetaIfNull();
					if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep) cbRepresentante.getSelectedItem()).representante);
					}
				} else if (event.target == btTodosClientes) {
					String cdRepresentante = getCdRepresentante();
					if(ValueUtil.isNotEmpty(cdRepresentante)) {
						show(new ListMetaVendaCliTodosForm(cdRepresentante));
					} else {
						UiUtil.showWarnMessage(Messages.META_VENDA_WARN_NO_REP);
					}
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
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltroMeta) {
					if (ValueUtil.isEmpty(edFiltroCli.getText())) {
						cdClienteFilter = null;
					}
					resetFiltroMetaIfNull();
					if (ValueUtil.isNotEmpty(cdClienteFilter) || ValueUtil.isNotEmpty(edFiltroMeta.getValue())) {
						dsMetaFilter = edFiltroMeta.getValue();
						flListInicialized = true;
						list();
					} else {
						UiUtil.showWarnMessage(Messages.META_VENDA_WARN_REP_CLI);
					}
				} else if (event.target == edFiltroCli) {
					showListCliente();
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(MENU_UTILITARIO_CALCULADORA)) {
						(new Calculator()).popup();
					}
				}
				break;
			}
		}
	}

	private void disableFieldValues() {
		barTopListContainer.setRect(LEFT, TOP + barTopContainer.getY2() + 1, FILL, UiUtil.getListContainerBarTopHeight());
		columnChart.setRect(LEFT, barTopListContainer.getY2() + HEIGHT_GAP_BIG, FILL, FILL - (barBottomContainer.getHeight() + 4));
		setVisibleState(false);
	}

	private void enableFieldsValues() {
		barTopListContainer.setRect(LEFT, btGraficoLista.getY2() + HEIGHT_GAP, FILL, UiUtil.getListContainerBarTopHeight());
		columnChart.setRect(LEFT, barTopListContainer.getY2() + HEIGHT_GAP_BIG, FILL, FILL - (barBottomContainer.getHeight() + 4));
		setVisibleState(true);
	}
	
	private void setVisibleState(boolean state) {
		lbReppresentante.setVisible(state);
		cbRepresentante.setVisible(state && SessionLavenderePda.isUsuarioSupervisor());
		lbCliente.setVisible(state);
		lbMeta.setVisible(state);
		edFiltroCli.setVisible(state);
		edFiltroMeta.setVisible(state);
		btGraficoLista.setVisible(state);
		containerTabs.setVisible(state);
	}

	private void setEdFiltroCli(String dsCliente) {
		edFiltroCli.setText(dsCliente);
	}

	public void showListCliente() throws SQLException {
		ListClienteWindow listCliente = new ListClienteWindow();
		listCliente.popup();
		if (listCliente.cliente != null) {
			setEdFiltroCli(listCliente.cliente.getDescription());
			cdClienteFilter = listCliente.cliente.cdCliente;
			flListInicialized = true;
			resetFiltroMetaIfNull();
			list();
		} else {
			cdClienteFilter = null;
		}
	}

	private void resetFiltroMetaIfNull() {
		if(edFiltroMeta == null || ValueUtil.isEmpty(edFiltroMeta.getValue())) {
			dsMetaFilter = ValueUtil.VALOR_NI;
		}
	}

	public void detalhesClick() throws SQLException {
		MetaVendaCli metaVendacli = (MetaVendaCli) getSelectedDomain();
		if(metaVendacli != null) {
			metaVendacli.vlMeta = ValueUtil.getDoubleValue(listContainer.getValueFromContainer(listContainer.getSelectedIndex(), 6));
			metaVendacli.vlRealizado = ValueUtil.getDoubleValue(listContainer.getValueFromContainer(listContainer.getSelectedIndex(), 7));
		}
		show(new ListMetaVendaCliGrupoForm(metaVendacli));
	}

	private void loadConfigsPadrao() throws SQLException {
		try {
			String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configsPadraoMetaVendaCli);
			if (vlConfigInterno != null) {
				String[] values = StringUtil.split(vlConfigInterno, UiUtil.defaultSeparatorValue);
				btGraficoLista.setSelectedIndex(ValueUtil.getIntegerValue(values[1]));
				configureDefaultSort(values);
			}
		} catch (ValidationException valEx) {
			 ExceptionUtil.handle(valEx);
		}
	}

	private void configureDefaultSort(String[] values) {
		if(values.length > 2) {
			if (values[2].isEmpty()) {
				sortAsc = ValueUtil.VALOR_SIM;
			}
			sortAsc = StringUtil.getStringValue(values[2]);
		}
		if(values.length > 3) {
			if (values[3].isEmpty()) {
				sortAtributte = COL_DTINICIALVIGENCIA;
			}
			sortAtributte = StringUtil.getStringValue(values[3]);
		}
		listContainer.sortAsc = sortAsc;
		listContainer.atributteSortSelected = sortAtributte;
	}

	public void visibleState() {
		super.visibleState();
		boolean showingChart = btGraficoLista.getSelectedIndex() == 1;
		columnChart.setVisible(showingChart);
		barTopListContainer.setVisible(showingChart);
		listContainer.setVisible(!showingChart);
	}

	public void initUI() {
		flListInicialized = false;
		super.initUI();
	}

	public void list() throws SQLException {
		if (flListInicialized) {
			super.list();
			listContainer.setFocus();
		} else {
			createGraficoMeta(0d, 0d);
		}
	}

	public void onFormClose() throws SQLException {
		super.onFormClose();
		String cdRepresentante = getCdRepresentante();
		cdRepresentante = ValueUtil.isEmpty(cdRepresentante) ? ValueUtil.VALOR_ZERO : cdRepresentante;
		ConfigInternoService.getInstance().salvaInfosPadraoMetaVendaCli(cdRepresentante, btGraficoLista.getSelectedIndex(), sortAsc, sortAtributte);
	}
	
}