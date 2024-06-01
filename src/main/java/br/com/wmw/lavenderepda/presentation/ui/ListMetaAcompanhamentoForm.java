package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.chart.BaseLineChart;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
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
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Meta;
import br.com.wmw.lavenderepda.business.domain.MetaAcompanhamento;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.MetaAcompanhamentoService;
import br.com.wmw.lavenderepda.business.service.MetaService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.Container;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.ScrollContainer;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListMetaAcompanhamentoForm extends BaseUIForm {

	private Meta meta;
	private ScrollContainer sContainer;
	private SessionContainer containerDatahoje, containerPeriodo;
	private PushButtonGroupBase btGrafico;
	private BaseButton btGraficoConfig;
	private BaseLineChart lineChart;
	private LabelValue lbTitle;
	private LabelValue lbDtHoje;
	private LabelValue lbNenhumaMeta;
	private LabelName lbMetaVenda;
	private LabelName lbMetaDia;
	private LabelName lbMetaFlex;
	private LabelName lbMetaFlexDia;
	private LabelName lbVendaAcumulada;
	private LabelName lbVendaProporcional;
	private LabelName lbFlexUtilizado;
	private LabelName lbFlexSaldo;
	private LabelName lbFlexProporcional;
	private LabelValue lbPercVenda;
	private LabelValue lbPlanejado;
	private LabelValue lbRealizado;
	private LabelValue lbFlexUtilizadoValue;
	private LabelValue lbPercFlex;
	private LabelValue lbMetaVendaValue;
	private LabelValue lbMetaVendaDiaValue;
	private LabelValue lbMetaFlexValue;
	private LabelValue lbMetaFlexDiaValue;
	private LabelValue lbMetaRealizadoValue;
	private LabelValue lbFlexSaldoValue;
	private RepresentanteSupervComboBox cbRepresentante;

	private boolean showLineMetaVenda = true;
	private boolean showLineRealVenda = true;
	private boolean showLineMetaFlex = true;
	private boolean showLineRealFlex = true;
	private boolean showLinePrevisaoVenda = true;
	private boolean showLinePrevisaoFlex = true;
	private boolean showPoints = true;
	private int pointSize = 2;
	private int lineSize = 1;
	private int colorLineMetaVenda = Color.getRGB(162, 205, 90);
	private int colorLineMetaRealizado = Color.getRGB(79, 148, 205);
	private int colorLineFlexMeta = Color.getRGB(209, 98, 88);
	private int colorLineFlexRealizado = Color.ORANGE;
	private int colorLinePrevisaoVenda = Color.getRGB(110, 110, 110);
	private int colorLinePrevisaoFlex = Color.getRGB(40, 40, 40);

    public ListMetaAcompanhamentoForm() throws SQLException {
        super(Messages.META_GERENCIAMENTOVENDAS_TITULO);
        btGrafico = new PushButtonGroupBase(new String[]{Messages.META_RELATORIO, Messages.META_GRAFICO}, true, 1, -1, 1, 1, true, PushButtonGroup.NORMAL);
        btGrafico.setFont(UiUtil.defaultFontSmall);
		containerPeriodo = new SessionContainer();
		containerPeriodo.setBackColor(ColorUtil.componentsBackColor);
		containerDatahoje = new SessionContainer();
		containerDatahoje.setBackColor(ColorUtil.componentsBackColor);
        sContainer = new BaseScrollContainer(false, true);
    	sContainer.setBackColor(getBackColor());
        lbTitle = new LabelValue("999999999999");
        lbDtHoje = new LabelValue("999999999999");
        lbPercVenda = new LabelValue("999999999999");
        lbFlexUtilizadoValue = new LabelValue("999999999999");
        lbPercFlex = new LabelValue("999999999999");
        lbMetaVendaValue = new LabelValue("999999999999");
        lbMetaVendaDiaValue = new LabelValue("999999999999");
        lbMetaFlexValue = new LabelValue("999999999999");
        lbMetaFlexDiaValue = new LabelValue("999999999999");
        lbMetaRealizadoValue = new LabelValue("999999999999");
        lbFlexSaldoValue = new LabelValue("999999999999");
        lbPlanejado = new LabelValue(Messages.META_PLANEJADO);
        lbRealizado = new LabelValue(Messages.META_REALIZADO);
        lbNenhumaMeta = new LabelValue(Messages.META_MSG_NENHUMA_META_ENCONTRADA);
        lbMetaVenda = new LabelName(Messages.META_VENDA_MES);
        lbMetaDia = new LabelName(Messages.META_VENDA_DIA);
        lbMetaFlex = new LabelName(Messages.META_FLEX_MES);
        lbMetaFlexDia = new LabelName(Messages.META_FLEX_DIA);
        lbVendaAcumulada = new LabelName(Messages.META_VENDA_ACUMULADA);
        lbVendaProporcional = new LabelName(Messages.META_VENDA_PROPORCIONAL);
        lbFlexUtilizado = new LabelName(Messages.META_FLEX_UTILIZADO);
        lbFlexSaldo = new LabelName(Messages.META_SALDO_FLEX);
        lbFlexProporcional = new LabelName(Messages.META_FLEX_PROPORCIONAL);
        btGraficoConfig = new BaseButton(UiUtil.getColorfulImage("images/config.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
        btGraficoConfig.useBorder = false;
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        configure();
    }

    private void configure() throws SQLException {
    	cbRepresentante.setSelectedIndex(0);
    	if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
        	cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
        }
    	loadMeta();
    }

    private void loadMeta() throws SQLException {
    	Meta metaFilter = new Meta();
    	metaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metaFilter.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentante.getValue() : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	metaFilter.dtInicial = DateUtil.getCurrentDate();
    	metaFilter.dtFinal = DateUtil.getCurrentDate();
    	Vector metas = MetaService.getInstance().filtraMetaParaRelatorioMetaAcompanhamento(MetaService.getInstance().findAllByExample(metaFilter) , DateUtil.getCurrentDate());
    	if (metas.size() > 0) {
    		meta = (Meta)metas.items[0];
    		MetaAcompanhamentoService.getInstance().getMetaAcompanhamentoList(meta);
    		meta.metaFlexAtual = VerbaSaldoService.getInstance().getVerbaSaldoErpVingenciaAtual(meta.cdRepresentante);
    		meta.dtUltimoDia = DateUtil.getCurrentDate();
    		DateUtil.decDay(meta.dtUltimoDia, 1);
    	} else {
    		meta = null;
    	}
    	metas = null;
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	}
    	UiUtil.add(this, containerPeriodo, LEFT, getNextY(), FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
    	UiUtil.add(this, sContainer, LEFT, AFTER, FILL, FILL - barBottomContainer.getHeight() - containerPeriodo.getHeight());
    	UiUtil.add(this, containerDatahoje, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, containerPeriodo.getHeight());
    	UiUtil.add(containerPeriodo, lbTitle, CENTER, CENTER);
    	UiUtil.add(this, lbNenhumaMeta, CENTER, CENTER);
    	UiUtil.add(containerDatahoje, lbDtHoje, CENTER, CENTER);
    	UiUtil.add(containerDatahoje, btGraficoConfig, RIGHT - (WIDTH_GAP * 2), CENTER, containerDatahoje.getHeight() - 4, containerDatahoje.getHeight() - 4);
    	UiUtil.add(barBottomContainer, btGrafico, CENTER, CENTER);
   		metaToScreen();
   		addRelatorioToScreen();
    	btGrafico.setSelectedIndex(0);
    	visibleState();
    }

    private void addRelatorioToScreen() {
		UiUtil.add(sContainer, lbPlanejado, LEFT + WIDTH_GAP_BIG, TOP);
		UiUtil.add(sContainer, lbMetaVenda, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbMetaVendaValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbMetaDia, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbMetaVendaDiaValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbMetaFlex, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbMetaFlexValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbMetaFlexDia, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbMetaFlexDiaValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbRealizado, LEFT + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lbVendaAcumulada, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbMetaRealizadoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbVendaProporcional, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbPercVenda, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbFlexUtilizado, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbFlexUtilizadoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbFlexSaldo, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbFlexSaldoValue, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(sContainer, lbFlexProporcional, BaseUIForm.CENTEREDLABEL, AFTER);
		UiUtil.add(sContainer, lbPercFlex, AFTER + WIDTH_GAP_BIG, SAME);
    }

    private void metaToScreen() throws SQLException {
    	if (meta != null) {
    		MetaService.getInstance().recalculaMeta(meta , DateUtil.getCurrentDate());
    		lbTitle.setText(StringUtil.getStringValue(meta.dtInicial) + " à " + StringUtil.getStringValue(meta.dtFinal));
    		lbDtHoje.setText(meta.dtUltimoDia + " - " +  meta.getNuDiaUtil(meta.dtUltimoDia) + "° Dia Útil");
    		int nuDiasUteisPeriodo = meta.getNuDiasUteisPeriodo();
    		if (nuDiasUteisPeriodo == 0)  {
    			nuDiasUteisPeriodo = 1;
    		}
    		lbMetaVendaValue.setValue(meta.vlMetaVendas);
    		lbMetaVendaDiaValue.setValue(meta.vlMetaVendas / nuDiasUteisPeriodo);
    		lbMetaRealizadoValue.setValue(meta.vlRealizadoVendas);
    		if (meta.vlMetaVendas != 0) {
	    		double vlPrecVendaProporcional = meta.getPctProporcionalVenda();
	    		lbPercVenda.setValue(StringUtil.getStringValueToInterface(vlPrecVendaProporcional) + "%");
	    		if (vlPrecVendaProporcional < 0) {
	    			lbPercVenda.setForeColor(ColorUtil.softRed);
	    		} else {
	    			lbPercVenda.setForeColor(ColorUtil.componentsForeColor);
	    		}
    		} else {
    			lbPercVenda.setText("");
    		}
    		if (meta.metaFlexAtual != null) {
    			lbMetaFlexValue.setValue(meta.metaFlexAtual.vlSaldoInicial);
    			lbMetaFlexDiaValue.setValue(meta.metaFlexAtual.vlSaldoInicial / nuDiasUteisPeriodo);
    			double vlFlexutilizado = meta.metaFlexAtual.vlSaldoInicial - meta.metaFlexAtual.vlSaldo;
    			lbFlexUtilizadoValue.setValue(vlFlexutilizado);
    			if (vlFlexutilizado > meta.getVlMetaFlexUltimoDiaUtil()) {
    				lbFlexUtilizadoValue.setForeColor(ColorUtil.softRed);
    			} else {
    				lbFlexUtilizadoValue.setForeColor(ColorUtil.componentsForeColor);
        		}
    			lbFlexSaldoValue.setValue(meta.metaFlexAtual.vlSaldo);
    			double vlPercFlexProporcional = meta.getVlProporcionalFlex();
    			lbPercFlex.setValue(vlPercFlexProporcional);
    			if (vlPercFlexProporcional < 0) {
    				lbPercFlex.setForeColor(ColorUtil.softRed);
    			} else {
    				lbPercFlex.setForeColor(ColorUtil.componentsForeColor);
        		}
    		} else {
        		lbMetaFlexValue.setText("");
        		lbMetaFlexDiaValue.setText("");
        		lbFlexUtilizadoValue.setText("");
        		lbFlexSaldoValue.setText("");
        		lbPercFlex.setText("");
    		}
//    		addRelatorioToScreen(); //Readiciona na tela pra recalcular o tamanho dos labels novamente
    		loadGraficoConfig();
    		createAddGrafico();
    	} else {
    		lbTitle.setText("");
    		lbDtHoje.setText("");
    		lbMetaVendaValue.setText("");
    		lbMetaVendaDiaValue.setText("");
    		lbMetaFlexValue.setText("");
    		lbMetaFlexDiaValue.setText("");
    		lbMetaRealizadoValue.setText("");
    		lbPercVenda.setText("");
    		lbFlexUtilizadoValue.setText("");
    		lbFlexSaldoValue.setText("");
    		lbPercFlex.setText("");
    	}
		reposition();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btGrafico) {
				visibleState();
			} else if (event.target == btGraficoConfig) {
				ChooseLinesGraphWindow newWindow = new ChooseLinesGraphWindow("Legenda");
				newWindow.popup();
			} else if ((event.target == cbRepresentante) && (ValueUtil.isNotEmpty(cbRepresentante.getValue()))) {
				SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
				loadMeta();
				metaToScreen();
				visibleState();
			}
			break;
		}
    }

    private void visibleState() {
    	boolean flRepTemMeta = meta != null;
    	boolean visible = btGrafico.getSelectedIndex() == 0;
    	sContainer.setVisible(visible && flRepTemMeta);
    	btGrafico.setVisible(flRepTemMeta);
    	lbNenhumaMeta.setVisible(!flRepTemMeta);
    	btGraficoConfig.setVisible(!visible && flRepTemMeta);
    	if (lineChart != null) {
    		lineChart.setVisible(!visible && flRepTemMeta);
    	}
    }

    private void loadGraficoConfig() throws SQLException {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdConfigInterno = ConfigInterno.configLineGraphProperties;
		configInterno.vlChave = ConfigInterno.VLCHAVEDEFAULT;
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno = (ConfigInterno) ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey());
		if (configInterno != null) {
			GraficoLinhasConfig graficoLinhasConfig = new GraficoLinhasConfig();
			graficoLinhasConfig.splitPairValueAndSetAtributes(configInterno.vlConfigInterno);
			showPoints = graficoLinhasConfig.showPoints;
			pointSize = graficoLinhasConfig.pointSize;
			lineSize = graficoLinhasConfig.lineSize;
		} else {
			configInterno = new ConfigInterno();
			configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		}
		configInterno.cdConfigInterno = ConfigInterno.configGraphLinesMetaAcompanhamento;
		configInterno.vlChave = ConfigInterno.VLCHAVEDEFAULT;
		configInterno = (ConfigInterno) ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey());
		if (configInterno != null) {
			MetaAcompanhamento meta = new MetaAcompanhamento();
			meta.splitPairValueAndSetAtributes(configInterno.vlConfigInterno);
			showLineMetaVenda = meta.showLineMetaVenda;
			showLineRealVenda = meta.showLineRealVenda;
			showLineMetaFlex = meta.showLineMetaFlex;
			showLineRealFlex = meta.showLineRealFlex;
			showLinePrevisaoVenda = meta.showLinePrevisaoVenda;
			showLinePrevisaoFlex = meta.showLinePrevisaoFlex;
		}
    }

    private void createAddGrafico() throws SQLException {
    	Vector datasMetaDiaria = meta.getDiasUteisPeriodo();
    	boolean possuiDiasPeriodo = false;
    	int diasPeriodo = 0;
    	if (ValueUtil.isNotEmpty(datasMetaDiaria)) {
    		possuiDiasPeriodo = true;
    		diasPeriodo = datasMetaDiaria.size();
    	} else {
    		datasMetaDiaria = new Vector();
    		datasMetaDiaria.addElement(DateUtil.getCurrentDate());
    		diasPeriodo = 1;
    	}
    	double vlMetaDia = 0;
    	double vlMetaFlex = 0;
    	double vlMetaFlexDia = 0;
    	if (possuiDiasPeriodo) {
    		vlMetaDia = meta.vlMetaVendas / diasPeriodo;
    		if (meta.metaFlexAtual != null) {
    			vlMetaFlex = meta.metaFlexAtual.vlSaldoInicial;
    			vlMetaFlexDia = meta.metaFlexAtual.vlSaldoInicial / diasPeriodo;
    		}
    	}
    	double sumMetaRealizadoValues = 0;
    	double sumMetaFlexRealizadoValues = 0;
    	double[] metaVendaValues = new double[diasPeriodo + 1];
    	double[] metaFlexValues = new double[diasPeriodo + 1];
    	double[] metaRealizadoValues = new double[diasPeriodo + 1];
    	double[] metaFlexRealizadoValues = new double[diasPeriodo + 1];
    	double[] previsaoFlex = new double[diasPeriodo + 1];
    	double[] previsaoVenda = new double[diasPeriodo + 1];
    	String[] periodos = new String[diasPeriodo + 1];
    	Date dtBase;
    	double maxValue = vlMetaFlex > 0 ? vlMetaFlex : 10;
    	MetaAcompanhamento metaAcompFilter;
    	metaVendaValues[0] = 0;
    	metaRealizadoValues[0] = 0;
    	metaFlexValues[0] = vlMetaFlex;
    	metaFlexRealizadoValues[0] = vlMetaFlex;
    	previsaoFlex[0] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
		previsaoVenda[0] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    	periodos[0] = "";
    	for (int i = 1; i < periodos.length; i++) {
    		dtBase = (Date)datasMetaDiaria.items[i - 1];
    		periodos[i] = StringUtil.getStringValue(dtBase.getDay() + "/" + dtBase.getMonth());
    		metaVendaValues[i] = vlMetaDia * i;
    		metaFlexValues[i] = vlMetaFlex - (vlMetaFlexDia * i);
    		metaAcompFilter = new MetaAcompanhamento();
    		metaAcompFilter.cdEmpresa = meta.cdEmpresa;
    		metaAcompFilter.cdRepresentante = meta.cdRepresentante;
    		metaAcompFilter.dsPeriodo = meta.dsPeriodo;
    		metaAcompFilter.dtRegistro = dtBase;
    		int indexMetaAcomp = this.meta.metaAcompanhamentoList.indexOf(metaAcompFilter);
    		if (indexMetaAcomp != -1) {
    			metaAcompFilter = (MetaAcompanhamento)this.meta.metaAcompanhamentoList.items[indexMetaAcomp];
    			//--
    			sumMetaFlexRealizadoValues += metaAcompFilter.vlRealizadoFlex;
    			metaFlexRealizadoValues[i] = vlMetaFlex + sumMetaFlexRealizadoValues;
    			//--
    			sumMetaRealizadoValues += metaAcompFilter.vlRealizadoMeta;
    			metaRealizadoValues[i] = sumMetaRealizadoValues;
    		} else if (!dtBase.isAfter(meta.dtUltimoDia)) {
    			metaFlexRealizadoValues[i] = vlMetaFlex + sumMetaFlexRealizadoValues;
    			metaRealizadoValues[i] = sumMetaRealizadoValues;
    		} else {
    			metaRealizadoValues[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    			metaFlexRealizadoValues[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    		}
    		//Previsão
    		if (DateUtil.getCurrentDate().equals(dtBase) && (metaRealizadoValues[i] == BaseLineChart.VALOR_PADRAO_PONTO_OCULTO)) {
    			previsaoFlex[i - 1] = vlMetaFlex + sumMetaFlexRealizadoValues;
    			previsaoVenda[i - 1] = sumMetaRealizadoValues;

    			Pedido pedidoExample = new Pedido();
    			pedidoExample.cdEmpresa = meta.cdEmpresa;
    			pedidoExample.cdRepresentante = meta.cdRepresentante;
    			pedidoExample.dtEmissao = dtBase;
    			previsaoVenda[i] = sumMetaRealizadoValues + PedidoService.getInstance().sumByExample(pedidoExample, "VLTOTALPEDIDO");
    			previsaoFlex[i] = vlMetaFlex + sumMetaFlexRealizadoValues + PedidoService.getInstance().sumByExample(pedidoExample, "VLVERBAPEDIDO");
    		} else {
    			previsaoFlex[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    			previsaoVenda[i] = BaseLineChart.VALOR_PADRAO_PONTO_OCULTO;
    		}
    		// Maior valor para o eixo Y do gráfico
    		if (showLineMetaVenda && (maxValue < metaVendaValues[i])) {
    			maxValue = metaVendaValues[i];
    		}
    		if (showLineRealVenda && (maxValue < metaRealizadoValues[i])) {
    			maxValue = metaRealizadoValues[i];
    		}
    		if (showLineMetaFlex && (maxValue < metaFlexValues[i])) {
    			maxValue = metaFlexValues[i];
    		}
    		if (showLineRealFlex && (maxValue < metaFlexRealizadoValues[i])) {
    			maxValue = metaFlexRealizadoValues[i];
    		}
		}
    	if (lineChart != null) {
    		remove(lineChart);
    	}
    	lineChart = new BaseLineChart("", periodos);
    	lineChart.pointR = pointSize;
    	lineChart.showPoints = showPoints;
    	lineChart.setYAxis(0, maxValue, 8);
    	lineChart.lineThickness = lineSize;
    	if (showLineMetaVenda) {
    		lineChart.series.addElement(new Series(Messages.META_VENDA_GRAFICO, metaVendaValues, colorLineMetaVenda));  //VERDE
    	}
    	if (showLineRealVenda) {
    		lineChart.series.addElement(new Series(Messages.META_VENDA_REALIZADO_GRAFICO, metaRealizadoValues, colorLineMetaRealizado));  //AZUL
    	}
    	if (showLineMetaFlex && (meta.metaFlexAtual != null)) {
    		lineChart.series.addElement(new Series(Messages.META_FLEX_GRAFICO, metaFlexValues, colorLineFlexMeta)); //VERMELHO
    	}
    	if (showLineRealFlex && (meta.metaFlexAtual != null)) {
    		lineChart.series.addElement(new Series(Messages.META_FLEX_REALIZADO_GRAFICO, metaFlexRealizadoValues, colorLineFlexRealizado)); //LARANJA
    	}
    	if (showLinePrevisaoVenda && showLineRealVenda) {
    		lineChart.series.addElement(new Series(Messages.META_PREVISAO_VENDA, previsaoVenda, colorLinePrevisaoVenda)); //Cinza
    	}
    	if (showLinePrevisaoFlex && showLineRealFlex && (meta.metaFlexAtual != null)) {
    		lineChart.series.addElement(new Series(Messages.META_PREVISAO_FLEX, previsaoFlex, colorLinePrevisaoFlex)); //Cinza
    	}
    	UiUtil.add(this, lineChart, LEFT, containerPeriodo.getY2() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight() - containerPeriodo.getHeight());
    }

    public class ChooseLinesGraphWindow extends WmwWindow {

    	private CheckBoolean ckLineMeta;
    	private CheckBoolean ckLineVendas;
    	private CheckBoolean ckLineFlexMeta;
    	private CheckBoolean ckLineFlexReal;
    	private CheckBoolean ckLinePrevisaoFlex;
    	private CheckBoolean ckLinePrevisaoVenda;
    	private ButtonGroupBoolean btShowPoint;
    	private NumberChooser ncLine;
    	private NumberChooser ncPoint;

    	private ScrollTabbedContainer tab;

    	private int TAB_LINHAS = 0;
    	private int TAB_GRAFICO = 1;

		public ChooseLinesGraphWindow(String title) {
			super(title);
			btFechar.setText(FrameworkMessages.BOTAO_OK);
			ckLineFlexMeta = new CheckBoolean(Messages.META_FLEX_GRAFICO);
			ckLineFlexReal = new CheckBoolean(Messages.META_FLEX_REALIZADO_GRAFICO);
			ckLinePrevisaoFlex = new CheckBoolean(Messages.META_PREVISAO_FLEX);
			ckLinePrevisaoVenda = new CheckBoolean(Messages.META_PREVISAO_VENDA);
			btShowPoint = new ButtonGroupBoolean();
			ncLine = new NumberChooser(false, 10, 1);
			ncPoint = new NumberChooser(false, 10, 1);
			tab = new ScrollTabbedContainer(new String[] {Messages.META_CONFIG_LINHAS, Messages.META_CONFIG_GRAFICO});
			makeUnmovable();
			setRect(20, 25);
		}

		public void initUI() {
			super.initUI();
			UiUtil.add(this, tab, TOP + HEIGHT_GAP_BIG , footerH);
			Container tabPanel = tab.getContainer(TAB_LINHAS);
			UiUtil.add(tabPanel, ckLineMeta = new CheckBoolean(Messages.META_VENDA_GRAFICO), LEFT + WIDTH_GAP, TOP + HEIGHT_GAP);
			UiUtil.add(tabPanel, ckLineVendas = new CheckBoolean(Messages.META_VENDA_REALIZADO_GRAFICO), LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			UiUtil.add(tabPanel, ckLinePrevisaoVenda, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			if (meta.metaFlexAtual != null) {
				UiUtil.add(tabPanel, ckLineFlexMeta, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
				UiUtil.add(tabPanel, ckLineFlexReal, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
				UiUtil.add(tabPanel, ckLinePrevisaoFlex, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP);
			}
			tabPanel = tab.getContainer(TAB_GRAFICO);
			UiUtil.add(tabPanel, new LabelName(Messages.META_CONFIG_GRAFICO_SHOW_POINTS), btShowPoint, getLeft(), TOP);
			UiUtil.add(tabPanel, new LabelName(Messages.META_CONFIG_GRAFICO_SIZE_POINTS), getLeft() , AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, ncPoint, getLeft(), AFTER, UiUtil.getControlPreferredHeight() * 3, UiUtil.getControlPreferredHeight());
			UiUtil.add(tabPanel, new LabelName(Messages.META_CONFIG_GRAFICO_SIZE_LINE), getLeft() , AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
			UiUtil.add(tabPanel, ncLine, getLeft(), AFTER, UiUtil.getControlPreferredHeight() * 3, UiUtil.getControlPreferredHeight());
			//--
			ckLineMeta.setChecked(showLineMetaVenda);
			ckLineVendas.setChecked(showLineRealVenda);
			ckLineFlexMeta.setChecked(showLineMetaFlex);
			ckLineFlexReal.setChecked(showLineRealFlex);
			ckLinePrevisaoVenda.setChecked(showLinePrevisaoVenda);
			ckLinePrevisaoFlex.setChecked(showLinePrevisaoFlex);
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
			MetaAcompanhamento meta = new MetaAcompanhamento();
			meta.showLineMetaVenda = showLineMetaVenda;
			meta.showLineRealVenda = showLineRealVenda;
			meta.showLineMetaFlex = showLineMetaFlex;
			meta.showLineRealFlex = showLineRealFlex;
			meta.showLinePrevisaoFlex = showLinePrevisaoFlex;
			meta.showLinePrevisaoVenda = showLinePrevisaoVenda;
			ConfigInternoService.getInstance().addValue(ConfigInterno.configGraphLinesMetaAcompanhamento, meta.prepareValues());
		}

		protected void btFecharClick() throws SQLException {
			super.btFecharClick();
			showLineMetaVenda = ckLineMeta.isChecked();
			showLineRealVenda = ckLineVendas.isChecked();
			showLineMetaFlex = ckLineFlexMeta.isChecked();
			showLineRealFlex = ckLineFlexReal.isChecked();
			showLinePrevisaoFlex = ckLinePrevisaoFlex.isChecked();
			showLinePrevisaoVenda = ckLinePrevisaoVenda.isChecked();
			showPoints = btShowPoint.getValueBoolean();
			pointSize = ncPoint.getValue();
			lineSize = ncLine.getValue();
			saveGraphProperties();
			createAddGrafico();
		}
    }

}