package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.chart.BaseColumnChart;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.IndicadorWmw;
import br.com.wmw.lavenderepda.business.domain.ValorIndicadorWmw;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorWmwService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ApuracaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.IndicadorWmwComboBox;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListIndicadorProdutividadeInternoForm extends BaseLavendereCrudPersonCadForm {

	private static int TABPANEL_APURACAO = 0;
	private static int TABPANEL_GRAFICO = 1;
	
	private SessionContainer containerFiltros;
	private IndicadorWmwComboBox cbIndicadorWmw;
	private ApuracaoComboBox cbApuracao;
	private LabelValue lvMeta;
	private LabelValue lvApurado;
	private LabelValue lvPctRealizado;
	private LabelValue lvDtApuracao;
	private LabelValue lvHrApuracao;
	private BaseColumnChart columnChart;
	private ValorIndicadorWmw valorIndicadorWmw;
	
	public ListIndicadorProdutividadeInternoForm() throws SQLException {
		super(Messages.LABEL_INDICADOR_PROD_INTERNO);
		containerFiltros = new SessionContainer();
		containerFiltros.setBackColor(ColorUtil.componentsBackColorDark);
		cbIndicadorWmw = new IndicadorWmwComboBox(IndicadorWmw.CD_TIPO_APURACAO_2);
		cbApuracao = new ApuracaoComboBox();
		lvMeta = new LabelValue();
		lvApurado = new LabelValue();
		lvPctRealizado = new LabelValue();
		lvDtApuracao = new LabelValue();
		lvHrApuracao = new LabelValue();
		setReadOnly();
		if (ValueUtil.isNotEmpty(cbIndicadorWmw.getValue())) {
			cbIndicadorWmwChange();
		}
		loadValorIndicadorWmw(false);
	}   
	
	@Override
	protected String getDsTable() throws SQLException {
		return ValorIndicadorWmw.TABLE_NAME;
	}

	@Override
    protected BaseDomain createDomain() {
    	return  valorIndicadorWmw;
    }

    @Override
    public String getEntityDescription() {
    	return title;
    }

	@Override
    protected CrudService getCrudService() {
    	return ValorIndicadorWmwService.getInstance();
    }
	
	private void cbIndicadorWmwChange() throws SQLException  {
		cbApuracao.loadApuracao(cbIndicadorWmw.getValue());
	}

	private void loadValorIndicadorWmw(boolean isCbChange) throws SQLException {
		valorIndicadorWmw = ValorIndicadorWmwService.getInstance().findValorIndicador(cbIndicadorWmw.getValue(), cbApuracao.getValue());
		edit(valorIndicadorWmw);
		if (isCbChange) {
			remontaTela();
			reposition();
		}
	}

	@Override
    protected void addTabsFixas(Vector tableTitles) throws SQLException {
    	super.addTabsFixas(tableTitles);
    	if (tableTitles.indexOf(Messages.LABEL_APURACAO) == -1) {
    		tableTitles.insertElementAt(Messages.LABEL_APURACAO, TABPANEL_APURACAO);
    	}
    	tableTitles.insertElementAt(Messages.LABEL_GRAFICO, TABPANEL_GRAFICO);
    }
    
    @Override
    protected void addCabecalho() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_LABEL_INDICADOR), cbIndicadorWmw, getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.LABEL_APURACAO), cbApuracao, getLeft(), AFTER + HEIGHT_GAP);
    }
	
    @Override
	protected void addComponentesFixosInicio() throws SQLException {
    	if (!LavenderePdaConfig.isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
    		Container tabApuracao = tabDinamica.getContainer(TABPANEL_APURACAO);
    		addCampo(tabApuracao, Messages.LABEL_META, lvMeta);
			addCampo(tabApuracao, Messages.LABEL_APURADO, lvApurado);
			addCampo(tabApuracao, Messages.LABEL_PCT_REALIZADO, lvPctRealizado);
			addCampo(tabApuracao, Messages.LABEL_DT_APURACAO, lvDtApuracao);
			addCampo(tabApuracao, Messages.LABEL_HR_APURACAO, lvHrApuracao);
    	}
		createGraficoMeta();
	}
    
    private void addCampo(Container container, String label, Control ctr) {
    	addCampo(container, label, ctr, CENTEREDLABEL, HEIGHT_GAP_BIG, -1);
    }
    
    private void addCampo(Container container, String label, Control ctr, int position, int gap, int labelNameColor) {
    	LabelName labelName = new LabelName(label);
    	if (labelNameColor != -1) {
    		labelName.setForeColor(labelNameColor);
    	}
    	UiUtil.add(container, labelName, position, AFTER + gap);
		UiUtil.add(container, ctr, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
    }
    
    private void createGraficoMeta() throws SQLException {
        if (columnChart != null) {
    		remove(columnChart);
    	}
        columnChart = new BaseColumnChart("", new String[] {""});
        double maxValueGrafico = 0;
    	if (LavenderePdaConfig.isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
    		maxValueGrafico = addCamposDinamicosGrafico();
	    } else {
	    	double[] vlMeta = new double[1];
	        double[] vlRealizado = new double[1];
	        vlMeta[0] = valorIndicadorWmw.vlMeta;
	        vlRealizado[0] = valorIndicadorWmw.vlIndicador;
	    	columnChart.series.addElement(new Series(Messages.META_VENDA_LABEL_VLMETA, vlMeta, Color.getRGB(79, 148, 205))); //AZUL
        	columnChart.series.addElement(new Series(Messages.META_VENDA_LABEL_VLREALIZADO, vlRealizado, Color.getRGB(162, 205, 90))); //VERDE
        	maxValueGrafico = valorIndicadorWmw.vlMeta > valorIndicadorWmw.vlIndicador ? valorIndicadorWmw.vlMeta : valorIndicadorWmw.vlIndicador;
	    }
        double result = maxValueGrafico / 8;
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
		maxValueGrafico = newValue > maxValueGrafico ? newValue : maxValueGrafico;
		if (maxValueGrafico > 0) {
			columnChart.setYAxis(0, ValueUtil.getIntegerValue(maxValueGrafico), 8);
		}
		Container tabPanel = tabDinamica.getContainer(TABPANEL_GRAFICO);
		UiUtil.add(tabPanel, columnChart, LEFT, TOP, FILL, FILL - barBottomContainer.getHeight());
    }
    
    private double addCamposDinamicosGrafico() throws SQLException {
    	Campo configCad;
		int size = getConfigPersonCadList().size();
		List<String> campos = LavenderePdaConfig.getDefineCampoDinamicoGrafico();
    	String[] corRgbCampos = LavenderePdaConfig.getDefineCorRGBCampoDinamicoGrafico();
    	int r = 0;
    	int g = 0;
    	int b = 0;
    	double maxValue = 0;
    	for (int i = 0; i < size; i++) {
    		configCad = (Campo) getConfigPersonCadList().items[i];
        	if (!configCad.isVisivelCad() || ignoreField(configCad.nmCampo)) {
                continue;
            }
        	String nmCampoEntidade = configCad.nmCampo.toUpperCase();
        	int index = campos.indexOf(nmCampoEntidade);
        	if (index != -1) {
        		String strValue = StringUtil.getStringValue(valorIndicadorWmw.getHashValuesDinamicos().get(nmCampoEntidade));
	        	double value = ValueUtil.getDoubleValue(strValue);
	        	maxValue = Math.max(maxValue, value);
	        	String dslabel = configCad.dsLabel;
    			if (index < corRgbCampos.length) {
	        		String[] corRgb = StringUtil.getStringValue(corRgbCampos[index]).split(":\\s?");
	        		r = Integer.parseInt(corRgb[0]);
	        		g = Integer.parseInt(corRgb[1]);
	        		b = Integer.parseInt(corRgb[2]);
	        	}
    			if (isMostraCampo(strValue)) {
    				columnChart.series.addElement(new Series(dslabel, new double[] {value}, Color.getRGB(r, g, b)));
    			}
    		}
        }
    	return maxValue;
    }
	
	@Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	setEnabled(false);
    }
	
	@Override
	public void domainToScreen(BaseDomain domain) throws SQLException {
		super.domainToScreen(domain);
		if (!LavenderePdaConfig.isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
			lvMeta.setValue(valorIndicadorWmw.vlMeta);       
			lvApurado.setValue(valorIndicadorWmw.vlIndicador);
			if (valorIndicadorWmw.vlMeta > 0) {
				lvPctRealizado.setValue(ValueUtil.round((valorIndicadorWmw.vlIndicador * 100) / valorIndicadorWmw.vlMeta));
			} else {
				lvPctRealizado.setValue(0);
			}
			lvDtApuracao.setValue(valorIndicadorWmw.dtApuracao);
			lvHrApuracao.setValue(valorIndicadorWmw.hrApuracao);
		}
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: 
			if (event.target == cbIndicadorWmw) {
				cbIndicadorWmwChange();			
			}
			if (event.target == cbApuracao || event.target == cbIndicadorWmw) {
				loadValorIndicadorWmw(true);
				createGraficoMeta();
			}
			break;
		}
	}
	
	@Override
	protected void addTabDinamica() {
        if (hashTabs.size() > 1) {
            UiUtil.add(this, tabDinamica, AFTER + HEIGHT_GAP + 3, getBottomTab(), ColorUtil.formsBackColor);
        } else {
            UiUtil.add(this, scrollContainer, LEFT, getTop(), FILL, FILL - getBottomTab());
            scrollContainer.setBackColor(ColorUtil.formsBackColor);
        }
    }
	
	@Override
	 protected void addCampoPersonLabel(String label, Container tabContainerAtual, Control ctr, Campo configCad) {
		String nmColunaEntidade = configCad.nmCampo.toUpperCase();
		String labelValue =  StringUtil.getStringValue(valorIndicadorWmw.getHashValuesDinamicos().get(nmColunaEntidade));
		if (isMostraCampo(labelValue)) {
			if (LavenderePdaConfig.isGraficoIndicadorSubtitulo(nmColunaEntidade)) {
				addCampo(tabContainerAtual, label, ctr, CENTER, HEIGHT_GAP_BIG, ColorUtil.componentsForeColor);
			} else {
				addCampo(tabContainerAtual, label, ctr, CENTEREDLABEL, HEIGHT_GAP, -1);
			}
		}
    }
	
	@Override
	protected Vector getConfigPersonCadList() throws SQLException {
		if (LavenderePdaConfig.isUsaConfigCampoDinamicoIndicadorProdutividadeInterno()) {
			return super.getConfigPersonCadList();
		}
		return new Vector();
	}
	
	private boolean isMostraCampo(String labelValue) {
		return !ValueUtil.valueEquals(labelValue, ValueUtil.VALOR_NAO);
	}

}
