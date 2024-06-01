package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.chart.BaseLineChart;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.NumberChooser;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.GraficoLinhasConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaAcomp;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.VisitaAcompService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.Container;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.chart.Chart;
import totalcross.ui.chart.PieChart;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListVisitaAcompForm extends BaseUIForm {

	private PushButtonGroupBase btGraficoLista;
	private PieChart pie;
	private BaseLineChart burnUp;
	private Container containerRelatorio;
	private BaseButton btGraficoConfig;
	private SessionContainer containerDatahoje;
	private SessionContainer containerPeriodo;
	private RepresentanteSupervComboBox cbRepresentante;

	private int colorTotalAgendas = Color.getRGB(79, 148, 205);
	private int colorRealizadas = Color.ORANGE;
	private int colorPositivadas = Color.getRGB(162, 205, 90);
	private int colorNaoVisitados = Color.getRGB(79, 148, 205);
	private int colorNegativadas = Color.getRGB(209, 98, 88);
	private int colorPrevisao = Color.getRGB(110, 110, 110);

	private LabelName lbTotalDeAgendas;
	private LabelName lbPositivadas;
	private LabelName lbNegativadas;
	private LabelName lbAVisitar;

	private LabelValue lbTitle;
	private LabelValue lbDtHoje;
	private LabelValue lbSemAgenda;
	private LabelValue lbVisitasDoMes;
	private LabelValue lbVlTotalDeAgendas;
	private LabelValue lbVlPositivadas;
	private LabelValue lbVlNegativadas;
	private LabelValue lbVlAVisitar;

	private int qtTotalDeAgendas;
	private int qtPositivadas;
	private int qtNegativadas;
	private int qtAVisitar;
	double qtPrevisaoRealizadas;
	double qtPrevisaoPositivadas;
	double qtPrevisaoNegativadas;
	private Date dtUltimoDiaUtil;

	private boolean showLineRealizadas = true;
	private boolean showLineTotalDeAgendas = true;
	private boolean showLinePositivadas = true;
	private boolean showLineNegativadas = true;
	private boolean showLinesPrevisao = true;
	private boolean showPoints = true;
	private boolean topFilters;
	
	private int pointSize = 2;
	private int lineSize = 1;

	private BarContainer barTopListContainer;
	private ButtonAction btResize;
	
	
	public ListVisitaAcompForm() throws SQLException {
		super(Messages.VISITA_TITULO_ACOMPANHAMENTO);
		containerRelatorio = new Container();
		containerRelatorio.setBackColor(getBackColor());
		containerPeriodo = new SessionContainer();
		containerDatahoje = new SessionContainer();
		btGraficoConfig = new BaseButton("", UiUtil.getColorfulImage("images/config.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
		dtUltimoDiaUtil = DateUtil.getCurrentDate();
    	dtUltimoDiaUtil.advance(-1);
		lbSemAgenda = new LabelValue(Messages.VISITA_LABEL_SEM_AGENDA);
		lbVisitasDoMes = new LabelValue(Messages.VISITA_LABEL_RESUMO_DO_MES);
		lbTotalDeAgendas = new LabelName(Messages.VISITA_LABEL_TOTAL_DE_AGENDAS);
		lbPositivadas = new LabelName(Messages.VISITA_LABEL_POSITIVADOS);
		lbNegativadas = new LabelName(Messages.VISITA_LABEL_NEGATIVADOS);
		lbAVisitar = new LabelName(Messages.VISITA_LABEL_A_VISITAR);
        lbTitle = new LabelValue("999999999");
        lbDtHoje = new LabelValue("999999999");
		lbVlTotalDeAgendas = new LabelValue();
		lbVlPositivadas = new LabelValue();
		lbVlNegativadas = new LabelValue();
		lbVlAVisitar = new LabelValue();
		btGraficoLista = new PushButtonGroupBase(new String[]{Messages.META_RELATORIO, Messages.VISITA_BOTAO_GRAFICO_PIZZA, Messages.VISITA_BOTAO_GRAFICO_BURNUP}, true, 1, -1, WIDTH_GAP_BIG, 1, false, PushButtonGroup.NORMAL);
		btGraficoLista.setFont(UiUtil.defaultFontSmall);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_ALL);
        cbRepresentante.setSelectedIndex(0);
        if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
        	cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
        }
        barTopListContainer = new BarContainer(ColorUtil.sessionContainerBackColor);
		btResize = new ButtonAction("");
		btResize.setBorder(BORDER_NONE);
		btResize.transparentBackground = true;   
		barBottomContainer.setVisible(false);
    }

    //@Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return VisitaAcompService.getInstance();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
        return getCrudService().findAll();
    }

    private String getCdRepresentante() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (cbRepresentante.getSelectedIndex() == 0) {
    			return "";
    		} else {
    			return cbRepresentante.getValue();
    		}
    	} else {
    		return SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	}
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	}
    	UiUtil.add(this, btGraficoLista, CENTER, getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
    	btGraficoLista.setSelectedIndex(0);
    	
    	UiUtil.add(this, barTopListContainer, LEFT, getNextY(), FILL, UiUtil.getListContainerBarTopHeight());
		int iconsDefaultWidthHeight = (int)(((double)barTopListContainer.getHeight() / 5) * 4);
		btResize.setImage(getBtResizeImage(iconsDefaultWidthHeight));
		UiUtil.add(barTopListContainer, btResize, RIGHT - barTopListContainer.getHeight() / 4, CENTER, iconsDefaultWidthHeight, iconsDefaultWidthHeight);    	
    	
    	UiUtil.add(this, containerPeriodo, LEFT, AFTER, FILL, UiUtil.getLabelPreferredHeight() + HEIGHT_GAP_BIG);
    	UiUtil.add(this, containerDatahoje, LEFT, BOTTOM, FILL, containerPeriodo.getHeight());
    	UiUtil.add(containerDatahoje, btGraficoConfig, RIGHT - WIDTH_GAP, CENTER, containerDatahoje.getHeight() - HEIGHT_GAP, containerDatahoje.getHeight() - HEIGHT_GAP);
    	//--
    	loadGraficoConfig();
    	carregaData();
    	createAddGraficoPizza();
    	createAddGraficoBurnUp();
    	createAddRelatorioToScreen();
    	loadDadosToScreen();
    }

	private Image getBtResizeImage(int imageHeigth) {
		Image resizeImg = UiUtil.getImage("images/resize.png");
		resizeImg.applyColor2(ColorUtil.baseForeColorSystem);
		return UiUtil.getSmoothScaledImage(resizeImg, imageHeigth, imageHeigth);
	}       
    
    public void mostraMensagemSemAgenda() {
		UiUtil.add(this, lbSemAgenda, CENTER, CENTER);
		visibleState();
    }

    public void loadDadosToScreen() throws SQLException {
    	qtTotalDeAgendas = AgendaVisitaService.getInstance().getQtAgendasDiasUteisNoMes(SessionLavenderePda.cdEmpresa, getCdRepresentante());
    	if (qtTotalDeAgendas == 0) {
    		mostraMensagemSemAgenda();
    	} else {
    		dadosToScreen();
    	}
    }

    public void carregaData() {
    	String dataInicial = DateUtil.formatDateDDMMYYYY(DateUtil.getDateValue(1, dtUltimoDiaUtil.getMonth(), dtUltimoDiaUtil.getYear()));
    	String dataFinal = DateUtil.formatDateDDMMYYYY(DateUtil.getLastDayOfMonth(dtUltimoDiaUtil));
    	lbTitle.setValue(dataInicial + " - " + dataFinal);
    	lbDtHoje.setValue(dtUltimoDiaUtil + " - " + DateUtil.getNuDiaUtilMes(dtUltimoDiaUtil)+"° Dia Útil");
    	UiUtil.add(containerPeriodo, lbTitle, CENTER, CENTER);
    	UiUtil.add(containerDatahoje, lbDtHoje, CENTER, CENTER);
    }

    private void addElementsGraficoPizza() {
    	if (pie.series.size() > 0) {
    		pie.series.removeAllElements();
    	}
    	pie.series.addElement(new Series(Messages.VISITA_LABEL_A_VISITAR, new double[] {qtAVisitar}, colorNaoVisitados));
    	pie.series.addElement(new Series(Messages.VISITA_LABEL_POSITIVADOS, new double[] {qtPositivadas}, colorPositivadas));
    	pie.series.addElement(new Series(Messages.VISITA_LABEL_NEGATIVADOS, new double[] {qtNegativadas}, colorNegativadas));
    }

    private double getValorMaxBurnUp() {
    	double max = 0;
    	double maxValue = 0;
    	//--
    	qtPrevisaoRealizadas = qtPrevisaoPositivadas + qtPrevisaoNegativadas;
    	int visitasRealizadas = qtPositivadas + qtNegativadas;
    	if (showLinesPrevisao) {
    		if (showLineTotalDeAgendas && (qtTotalDeAgendas > maxValue)) {
    			maxValue = qtTotalDeAgendas;
    		}
    		if (showLineRealizadas && (qtPrevisaoRealizadas > maxValue)) {
    			maxValue = qtPrevisaoRealizadas + visitasRealizadas;
    		}
    		if (showLinePositivadas && (qtPrevisaoPositivadas > maxValue)) {
    			maxValue = qtPrevisaoPositivadas + qtPositivadas;
    		}
    		if (showLineNegativadas && (qtPrevisaoNegativadas > maxValue)) {
    			maxValue = qtPrevisaoNegativadas + qtNegativadas;
    		}
    	} else {
	    	if (showLineTotalDeAgendas) {
	    		maxValue = qtTotalDeAgendas;
	    	}
	    	if (showLineRealizadas && (visitasRealizadas > maxValue)) {
	    		maxValue = visitasRealizadas;
	    	}
	    	if (showLinePositivadas && (qtPositivadas > maxValue)) {
	    		maxValue = qtPositivadas;
	    	}
	    	if (showLineNegativadas && (qtNegativadas > maxValue)) {
	    		maxValue = qtNegativadas;
	    	}
    	}
		max = ValueUtil.getIntegerValue((maxValue/10));
		if (max < (maxValue/10)) {
			max ++;
			max *= 10;
		} else {
			max *= 10;
		}
		if (max == 0) {
			max = 10;
		}
		return max;
    }

    private void addElementsGraficoBurnUp() throws SQLException {
    	if (burnUp.series.size() > 0) {
    		burnUp.series.removeAllElements();
    	}
    	VisitaAcomp visitaAcompFilter = new VisitaAcomp();
    	visitaAcompFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	visitaAcompFilter.cdRepresentante = getCdRepresentante();
		AgendaVisita agendaFilter = new AgendaVisita();
		agendaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaFilter.cdRepresentante = getCdRepresentante();
		Vector visitaAcompList = VisitaAcompService.getInstance().findAllByExample(visitaAcompFilter);
		Vector agendaListTemp = AgendaVisitaService.getInstance().findAllByExample(agendaFilter);
    	Vector datasPeriodo = DateUtil.getDiasUteisMes(dtUltimoDiaUtil);
    	int nuDiasPeriodo = datasPeriodo.size();
    	int qtVisitasRealizadas = 0;
    	double[] vlVisitasRealizadas = new double[nuDiasPeriodo+1];
    	double[] vlTotalAgendas = new double[nuDiasPeriodo+1];
    	double[] vlPositivos = new double[nuDiasPeriodo+1];
    	double[] vlNegativos = new double[nuDiasPeriodo+1];
    	double[] vlPrevisaoRealizadas = new double[nuDiasPeriodo+1];
    	double[] vlPrevisaoPositivadas = new double[nuDiasPeriodo+1];
    	double[] vlPrevisaoNegativadas = new double[nuDiasPeriodo+1];
    	vlVisitasRealizadas[0] = 0;
    	vlTotalAgendas[0] = 0;
    	vlPositivos[0] = 0;
    	vlNegativos[0] = 0;
    	vlPrevisaoRealizadas[0] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    	vlPrevisaoPositivadas[0] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    	vlPrevisaoNegativadas[0] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    	qtPositivadas = 0;
    	qtNegativadas = 0;
    	qtPrevisaoRealizadas = 0;
    	qtPrevisaoPositivadas = 0;
    	qtPrevisaoNegativadas = 0;
    	Date dtRegistro;
    	for (int i = 1; i < vlVisitasRealizadas.length; i++) {
    		dtRegistro = (Date)datasPeriodo.items[i-1];
    		vlTotalAgendas[i] = vlTotalAgendas[i-1] + AgendaVisitaService.getInstance().getQtAgendasNoDia(dtRegistro, agendaListTemp);
    		if (ValueUtil.isEmpty(getCdRepresentante())) {
    			int sizeVisitasAcomp = visitaAcompList.size();
    			boolean encontrouVisitasAcomp = false;
    			for (int j = 0; j < sizeVisitasAcomp; j++) {
    				VisitaAcomp visitaAcomp = (VisitaAcomp)visitaAcompList.items[j];
    				if (visitaAcomp.dtRegistro.equals(dtRegistro)) {
    					qtPositivadas += visitaAcomp.qtVisitasPositivas;
        				qtNegativadas += visitaAcomp.qtVisitasNegativas;
        				vlPositivos[i] = qtPositivadas;
        				vlNegativos[i] = qtNegativadas;
        				qtVisitasRealizadas = qtPositivadas + qtNegativadas;
        				vlVisitasRealizadas[i] = qtVisitasRealizadas;
        				encontrouVisitasAcomp = true;
    				}
				}
    			if (!encontrouVisitasAcomp) {
    				vlNegativos[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    				vlPositivos[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    				vlVisitasRealizadas[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    			}
    		} else {
    			visitaAcompFilter.dtRegistro = dtRegistro;
    			int indexVisita = visitaAcompList.indexOf(visitaAcompFilter);
    			if (indexVisita != -1) {
    				VisitaAcomp visitaAcomp = (VisitaAcomp) visitaAcompList.items[indexVisita];
    				qtPositivadas += visitaAcomp.qtVisitasPositivas;
    				qtNegativadas += visitaAcomp.qtVisitasNegativas;
    				vlPositivos[i] = qtPositivadas;
    				vlNegativos[i] = qtNegativadas;
    				qtVisitasRealizadas = qtPositivadas + qtNegativadas;
    				vlVisitasRealizadas[i] = qtVisitasRealizadas;
    			} else {
    				vlPositivos[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    				vlNegativos[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    				vlVisitasRealizadas[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    			}
    		}
    		if (DateUtil.getCurrentDate().equals(dtRegistro) && (vlVisitasRealizadas[i] == BaseLineChart.VALOR_PADRAO_PONTO_OCULTO)) {
    			Visita visitaHojeFilter = new Visita();
    			visitaHojeFilter.dtVisita = dtRegistro;
    			Vector visitaHojeList = VisitaService.getInstance().findAllByExample(visitaHojeFilter);
    			qtPrevisaoRealizadas = visitaHojeList.size();
    			for (int j = 0; j < qtPrevisaoRealizadas; j++) {
    				Visita visita = (Visita) visitaHojeList.items[j];
    				if (Visita.FL_VISITA_POSITIVADA.equals(visita.flVisitaPositivada)) {
    					qtPrevisaoPositivadas++;
    				} else if (Visita.FL_VISITA_NAOPOSITIVADA.equals(visita.flVisitaPositivada)) {
    					qtPrevisaoNegativadas++;
    				}
    			}
				vlPrevisaoPositivadas[i-1] = qtPositivadas;
				vlPrevisaoPositivadas[i] = qtPositivadas + qtPrevisaoPositivadas;
				vlPrevisaoNegativadas[i-1] = qtNegativadas;
				vlPrevisaoNegativadas[i] = qtNegativadas + qtPrevisaoNegativadas;
				vlPrevisaoRealizadas[i-1] = qtVisitasRealizadas;
				vlPrevisaoRealizadas[i] = qtVisitasRealizadas + qtPrevisaoRealizadas;
    		} else {
    			vlPrevisaoPositivadas[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    			vlPrevisaoNegativadas[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    			vlPrevisaoRealizadas[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    		}
    		dtRegistro.advance(1);
		}
    	burnUp.setYAxis(0, getValorMaxBurnUp(), 10);
    	if (showLineTotalDeAgendas) {
    		burnUp.series.addElement(new Series(Messages.VISITA_LABEL_TOTAL_DE_AGENDAS, vlTotalAgendas, colorTotalAgendas));
    	}
    	if (showLineRealizadas) {
    		burnUp.series.addElement(new Series(Messages.VISITA_LABEL_REALIZADAS, vlVisitasRealizadas, colorRealizadas));
    	}
    	if (showLinePositivadas) {
    		burnUp.series.addElement(new Series(Messages.VISITA_LABEL_POSITIVADOS, vlPositivos, colorPositivadas));
    	}
    	if (showLineNegativadas) {
    		burnUp.series.addElement(new Series(Messages.VISITA_LABEL_NEGATIVADOS, vlNegativos, colorNegativadas));
    	}
    	if (showLinesPrevisao) {
    		if (showLineRealizadas) {
    			burnUp.series.addElement(new Series(Messages.VISITA_LABEL_PREVISAO, vlPrevisaoRealizadas, colorPrevisao));
    		}
    		if (showLinePositivadas) {
    			burnUp.series.addElement(new Series(Messages.VISITA_LABEL_PREVISAO, vlPrevisaoPositivadas, colorPrevisao));
    		}
    		if (showLineNegativadas) {
    			burnUp.series.addElement(new Series(Messages.VISITA_LABEL_PREVISAO, vlPrevisaoNegativadas, colorPrevisao));
    		}
    	}
    }

    public void dadosToScreen() throws SQLException {
		double[] somatorios = VisitaAcompService.getInstance().sumVisitasPositivasAndNegativasDoMes(getCdRepresentante(), SessionLavenderePda.cdEmpresa);
    	qtPositivadas = ValueUtil.getIntegerValue(somatorios[0]);
    	qtNegativadas = ValueUtil.getIntegerValue(somatorios[1]);
    	qtAVisitar = qtTotalDeAgendas - (qtPositivadas + qtNegativadas);
    	qtAVisitar = qtAVisitar >= 0 ? qtAVisitar : 0;
    	//--
    	lbVlTotalDeAgendas.setValue(qtTotalDeAgendas);
		lbVlPositivadas.setValue(qtPositivadas);
    	lbVlAVisitar.setValue(qtAVisitar);
    	lbVlNegativadas.setValue(qtNegativadas);
    	addElementsGraficoPizza();
    	addElementsGraficoBurnUp();
    	visibleState();
    }

    private void createAddRelatorioToScreen() {
    	UiUtil.add(this, containerRelatorio, LEFT, containerPeriodo.getY2() + HEIGHT_GAP, FILL, FILL);
    	UiUtil.add(containerRelatorio, lbVisitasDoMes, CENTER, TOP, PREFERRED);
    	UiUtil.add(containerRelatorio, lbTotalDeAgendas, BaseUIForm.CENTEREDLABEL, AFTER, PREFERRED);
    	UiUtil.add(containerRelatorio, lbVlTotalDeAgendas, AFTER + WIDTH_GAP_BIG, SAME + WIDTH_GAP, PREFERRED, PREFERRED);
    	UiUtil.add(containerRelatorio, lbAVisitar,  BaseUIForm.CENTEREDLABEL, AFTER, PREFERRED);
    	UiUtil.add(containerRelatorio, lbVlAVisitar, AFTER + WIDTH_GAP_BIG, SAME + WIDTH_GAP, PREFERRED, PREFERRED);
    	UiUtil.add(containerRelatorio, lbPositivadas, BaseUIForm.CENTEREDLABEL, AFTER, PREFERRED);
    	UiUtil.add(containerRelatorio, lbVlPositivadas, AFTER + WIDTH_GAP_BIG, SAME + WIDTH_GAP, PREFERRED, PREFERRED);
    	UiUtil.add(containerRelatorio, lbNegativadas,  BaseUIForm.CENTEREDLABEL, AFTER, PREFERRED);
    	UiUtil.add(containerRelatorio, lbVlNegativadas, AFTER + WIDTH_GAP_BIG, SAME + WIDTH_GAP, PREFERRED, PREFERRED);
    }

    private void loadGraficoConfig() throws SQLException {
		ConfigInterno configInternoFilter = new ConfigInterno();
		configInternoFilter.cdConfigInterno = ConfigInterno.configLineGraphProperties;
		configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInternoFilter.vlChave = ConfigInterno.VLCHAVEDEFAULT;
		configInternoFilter = (ConfigInterno) ConfigInternoService.getInstance().findByRowKey(configInternoFilter.getRowKey());
		if (configInternoFilter != null) {
			GraficoLinhasConfig graficoLinhasConfig = new GraficoLinhasConfig();
			graficoLinhasConfig.splitPairValueAndSetAtributes(configInternoFilter.vlConfigInterno);
			showPoints = graficoLinhasConfig.showPoints;
			pointSize = graficoLinhasConfig.pointSize;
			lineSize = graficoLinhasConfig.lineSize;
		}
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdConfigInterno = ConfigInterno.configGraphLinesVisitaAcompanhamento;
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.vlChave = ConfigInterno.VLCHAVEDEFAULT;
		configInterno = (ConfigInterno) ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey());
		if (configInterno != null) {
			VisitaAcomp visita = new VisitaAcomp();
			visita.splitPairValueAndSetAtributes(configInterno.vlConfigInterno);
			showLineTotalDeAgendas = visita.showLineTotalDeAgendas;
			showLineRealizadas = visita.showLineRealizadas;
			showLinePositivadas = visita.showLinePositivadas;
			showLineNegativadas = visita.showLineNegativadas;
			showLinesPrevisao = visita.showLinesPrevisao;
		}
    }

    private void createAddGraficoPizza() {
    	if (pie != null) {
    		remove(pie);
    	}
    	pie = new PieChart();
    	pie.showTitle = true;
    	pie.showLegend = true;
    	pie.yDecimalPlaces = 0;
    	pie.type = Chart.IS_3D;
    	pie.showCategories = true;
    	pie.legendPosition = BOTTOM;
    	pie.legendValueSuffix = "%";
    	pie.setFont(UiUtil.fontVerySmall);
    	pie.setTitle(Messages.VISITA_TITULO_GRAFICO_PIZZA);
    	UiUtil.add(this, pie, LEFT, containerPeriodo.getY2()+HEIGHT_GAP, FILL, FILL - (HEIGHT_GAP + containerPeriodo.getHeight()));
    }

    private void createAddGraficoBurnUp() {
    	Vector datasPeriodo = DateUtil.getDiasUteisMes(dtUltimoDiaUtil);
    	int diasPeriodo = datasPeriodo.size();
    	//--
    	String[] dias = new String[diasPeriodo+1];
    	dias[0] = "";
    	for (int i = 1; i < dias.length; i++) {
    		Date dtBase = (Date)datasPeriodo.items[i-1];
    		dias[i] = StringUtil.getStringValue(dtBase.getDay()+"/"+dtBase.getMonth());
		}
    	if (burnUp != null) {
    		remove(burnUp);
    	}
    	burnUp = new BaseLineChart(Messages.VISITA_TITULO_GRAFICO_BURNUP, dias);
    	burnUp.yDecimalPlaces = 0;
    	burnUp.pointR = pointSize;
    	burnUp.showPoints = showPoints;
    	burnUp.lineThickness = lineSize;
    	UiUtil.add(this, burnUp,  LEFT, containerPeriodo.getY2()+HEIGHT_GAP, FILL, FILL - (HEIGHT_GAP + containerPeriodo.getHeight()));
    }

    public void visibleState() {
    	boolean flRepTemAgendas = qtTotalDeAgendas > 0;
    	lbSemAgenda.setVisible(!flRepTemAgendas);
    	if (btGraficoLista.getSelectedIndex() == 0) {
    		pie.setVisible(false);
    		burnUp.setVisible(false);
    		btGraficoConfig.setVisible(false);
    		containerRelatorio.setVisible(flRepTemAgendas);
    	} else if (btGraficoLista.getSelectedIndex() == 1) {
    		pie.setVisible(flRepTemAgendas);
    		burnUp.setVisible(false);
    		btGraficoConfig.setVisible(false);
    		containerRelatorio.setVisible(false);
    	} else if (btGraficoLista.getSelectedIndex() == 2) {
	    	pie.setVisible(false);
	    	burnUp.setVisible(flRepTemAgendas);
	    	btGraficoConfig.setVisible(flRepTemAgendas);
	    	containerRelatorio.setVisible(false);
    	}
    }

    public class ChooseLinesGraphWindow extends WmwWindow {

    	private CheckBoolean ckTotalDeAgendas;
    	private CheckBoolean ckRealizadas;
    	private CheckBoolean ckPositivas;
    	private CheckBoolean ckNegativas;
    	private CheckBoolean ckPrevisoes;
    	// Configs do grafico
    	private ButtonGroupBoolean btShowPoint;
    	private NumberChooser ncLine;
    	private NumberChooser ncPoint;
    	private ScrollTabbedContainer tab;
    	private int TAB_LINHAS = 0;
    	private int TAB_GRAFICO = 1;

		public ChooseLinesGraphWindow(String title) {
			super(title);
			btFechar.setText(FrameworkMessages.BOTAO_OK);
			ckTotalDeAgendas = new CheckBoolean(Messages.VISITA_LABEL_TOTAL_DE_AGENDAS);
			ckTotalDeAgendas.checkColor = colorTotalAgendas;
			ckRealizadas = new CheckBoolean(Messages.VISITA_LABEL_REALIZADAS);
			ckRealizadas.checkColor = colorRealizadas;
			ckPositivas = new CheckBoolean(Messages.VISITA_LABEL_POSITIVADOS);
			ckPositivas.checkColor = colorPositivadas;
			ckNegativas = new CheckBoolean(Messages.VISITA_LABEL_NEGATIVADOS);
			ckNegativas.checkColor = colorNegativadas;
			ckPrevisoes = new CheckBoolean(Messages.VISITA_LABEL_PREVISAO);
			ckPrevisoes.checkColor = colorPrevisao;
			btShowPoint = new ButtonGroupBoolean();
			ncLine = new NumberChooser(false, 10, 1);
			ncPoint = new NumberChooser(false, 10, 1);
			tab = new ScrollTabbedContainer(new String[] {Messages.META_CONFIG_LINHAS, Messages.META_CONFIG_GRAFICO});
			makeUnmovable();
			setRect(20, 25);
		}

		public void initUI() {
			super.initUI();
			UiUtil.add(this, tab, TOP , footerH);
			Container tabPanel = tab.getContainer(TAB_LINHAS);
			UiUtil.add(tabPanel, ckTotalDeAgendas, LEFT + WIDTH_GAP, TOP + HEIGHT_GAP);
			UiUtil.add(tabPanel, ckRealizadas, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, ckPositivas, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, ckNegativas, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, ckPrevisoes, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			tabPanel = tab.getContainer(TAB_GRAFICO);
			UiUtil.add(tabPanel, new LabelName(Messages.META_CONFIG_GRAFICO_SHOW_POINTS), btShowPoint, getLeft(), TOP);
			UiUtil.add(tabPanel, new LabelName(Messages.META_CONFIG_GRAFICO_SIZE_POINTS), getLeft() , AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, ncPoint, getLeft(), AFTER, UiUtil.getControlPreferredHeight() * 3, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.META_CONFIG_GRAFICO_SIZE_LINE), getLeft() , AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, ncLine, getLeft(), AFTER, UiUtil.getControlPreferredHeight() * 3, UiUtil.getControlPreferredHeight());
			//--
			ckRealizadas.setChecked(showLineRealizadas);
			ckTotalDeAgendas.setChecked(showLineTotalDeAgendas);
			ckPositivas.setChecked(showLinePositivadas);
			ckNegativas.setChecked(showLineNegativadas);
			ckPrevisoes.setChecked(showLinesPrevisao);
			btShowPoint.setValueBoolean(showPoints);
			ncPoint.setValue(pointSize);
			ncLine.setValue(lineSize);
		}

		private void saveGraphProperties() throws SQLException {
			GraficoLinhasConfig graficoLinhasConfig = new GraficoLinhasConfig();
			graficoLinhasConfig.showPoints = showPoints;
			graficoLinhasConfig.pointSize = pointSize;
			graficoLinhasConfig.lineSize = lineSize;
			ConfigInternoService.getInstance().addValue(ConfigInterno.configLineGraphProperties, graficoLinhasConfig.prepareValues());
			VisitaAcomp visita = new VisitaAcomp();
			visita.showLineTotalDeAgendas = showLineTotalDeAgendas;
			visita.showLineRealizadas = showLineRealizadas;
			visita.showLinePositivadas = showLinePositivadas;
			visita.showLineNegativadas = showLineNegativadas;
			visita.showLinesPrevisao = showLinesPrevisao;
			ConfigInternoService.getInstance().addValue(ConfigInterno.configGraphLinesVisitaAcompanhamento, visita.prepareValues());
		}

		protected void btFecharClick() throws SQLException {
			showLineRealizadas = ckRealizadas.isChecked();
			showLineTotalDeAgendas = ckTotalDeAgendas.isChecked();
			showLinePositivadas = ckPositivas.isChecked();
			showLineNegativadas = ckNegativas.isChecked();
			showLinesPrevisao = ckPrevisoes.isChecked();
			showPoints = btShowPoint.getValueBoolean();
			pointSize = ncPoint.getValue();
			lineSize = ncLine.getValue();
			if (showLineTotalDeAgendas || showLineRealizadas || showLinePositivadas || showLineNegativadas) {
				super.btFecharClick();
				saveGraphProperties();
		    	createAddGraficoBurnUp();
		    	addElementsGraficoBurnUp();
		    	reloadVisibity();
    		} else {
    			throw new ValidationException(Messages.VISITA_MSG_AVISA_SEM_LINHAS_VISIVEIS);
    		}
		}
    }

    protected void reloadVisibity() {
    	visibleState();
    }

	//@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
				if (event.target == btGraficoLista) {
					visibleState();
	 			} else if (event.target == btGraficoConfig) {
					ChooseLinesGraphWindow newWindow = new ChooseLinesGraphWindow("Legenda");
					newWindow.popup();
				} else if (event.target == cbRepresentante) {
					if (ValueUtil.isNotEmpty(cbRepresentante.getValue()) && ValueUtil.isNotEmpty(getCdRepresentante())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
					} else {
						SessionLavenderePda.setRepresentante(null);
					}
					loadDadosToScreen();
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
 		}
    }
    
	private void disableFieldValues() {
		barTopListContainer.setRect(LEFT, TOP + barTopContainer.getY2() + 1, FILL, UiUtil.getListContainerBarTopHeight());
		containerPeriodo.setRect(LEFT, barTopListContainer.getY2(), FILL, KEEP);
		containerRelatorio.setRect(LEFT, containerPeriodo.getY2() + HEIGHT_GAP, FILL, FILL);
		burnUp.setRect(LEFT, containerPeriodo.getY() + UiUtil.getListContainerBarTopHeight(), FILL, FILL - UiUtil.getListContainerBarTopHeight());
		pie.setRect(LEFT, barTopListContainer.getY2() + UiUtil.getListContainerBarTopHeight(), FILL, FILL - UiUtil.getListContainerBarTopHeight());
		btGraficoLista.setVisible(false);
		cbRepresentante.setVisible(false);
	}  
	
	private void enableFieldsValues() {
		barTopListContainer.setRect(LEFT, btGraficoLista.getY2() + HEIGHT_GAP, FILL, UiUtil.getListContainerBarTopHeight());
		containerPeriodo.setRect(LEFT, barTopListContainer.getY2(), FILL, KEEP);
		containerRelatorio.setRect(LEFT, containerPeriodo.getY2() + HEIGHT_GAP , FILL, FILL);
		burnUp.setRect(LEFT, containerPeriodo.getY() + UiUtil.getListContainerBarTopHeight(), FILL  , FILL -  UiUtil.getListContainerBarTopHeight());
		pie.setRect(LEFT, barTopListContainer.getY2()+ UiUtil.getListContainerBarTopHeight(), FILL, FILL -  UiUtil.getListContainerBarTopHeight());
		btGraficoLista.setVisible(true);
		cbRepresentante.setVisible(true);
	}	

	@Override
	public void reposition() {
		super.reposition();
		if(topFilters) {
			disableFieldValues();
		} else {
			enableFieldsValues();
		}
		createAddRelatorioToScreen();
	}
}