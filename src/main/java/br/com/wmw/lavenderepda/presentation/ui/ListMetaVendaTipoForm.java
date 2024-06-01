package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.chart.BaseColumnChart;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Familia;
import br.com.wmw.lavenderepda.business.domain.MetaVenda;
import br.com.wmw.lavenderepda.business.domain.MetaVendaRep;
import br.com.wmw.lavenderepda.business.domain.MetaVendaTipo;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.FamiliaService;
import br.com.wmw.lavenderepda.business.service.MetaVendaRepService;
import br.com.wmw.lavenderepda.business.service.MetaVendaService;
import br.com.wmw.lavenderepda.business.service.MetaVendaTipoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.FamiliaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MetaVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.Container;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListMetaVendaTipoForm extends BaseCrudListForm {

	private int corPositivo = Color.darker(ColorUtil.softGreen);
	private int corNegativo = Color.darker(ColorUtil.softRed);

	private LabelName lbMetaVenda;
	private LabelName lbFamilia;
	private LabelName lbVariavelMeta;
	private LabelName lbPlanejadoHoje;
	private LabelName lbPctPlanejadoHoje;
	private LabelName lbRealizadoHoje;
	private LabelName lbPctRealizadoHoje;
	private LabelName lbVariacaoHoje;
	private LabelName lbPctVariacaoHoje;
	private LabelValue lbVariavelMetaValue;
	private LabelValue lbPlanejadoHojeValue;
	private LabelValue lbPctPlanejadoHojeValue;
	private LabelValue lbRealizadoHojeValue;
	private LabelValue lbPctRealizadoHojeValue;
	private LabelValue lbVariacaoHojeValue;
	private LabelValue lbPctVariacaoHojeValue;
	private LabelValue lbPlanejado;
	private LabelValue lbRealizado;
	private LabelValue lbVariacao;
	private LabelName lbRep;
	private RepresentanteSupervComboBox cbRepresentante;
	private MetaVendaComboBox cbMetaVenda;
	private FamiliaComboBox cbFamilia;
	private ScrollTabbedContainer scTabContainer;
	private BaseColumnChart columnChart;
	public boolean showComboFamilia;
	public int lastActiveTab = 0;

    public ListMetaVendaTipoForm() throws SQLException {
        super(Messages.META_VENDA_TITULO_TELA);
        lbMetaVenda = new LabelName(Messages.META_VENDA_META);
        lbFamilia = new LabelName(Messages.META_VENDA_FAMILIA);
        lbVariavelMeta = new LabelName(Messages.META_VENDA_VARIAVEL);
        lbVariavelMetaValue = new LabelValue("-");
        lbRep = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_ALL);
        cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
        if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
        	cbRepresentante.setSelectedIndex(0);
        }
        cbMetaVenda = new MetaVendaComboBox(getCdRepresentante());
        cbMetaVenda.setSelectedIndex(0);
        cbFamilia = new FamiliaComboBox(lbFamilia.getValue());
        lbPlanejado = new LabelValue(Messages.META_VENDA_PLANEJADO, RIGHT);
        lbPlanejadoHoje = new LabelName(Messages.META_VENDA_META_HOJE);
        lbPctPlanejadoHoje = new LabelName(Messages.META_VENDA_PERC_META_HOJE);
        lbPlanejadoHojeValue = new LabelValue("-");
        lbPctPlanejadoHojeValue = new LabelValue("-");
        lbRealizado = new LabelValue(Messages.META_VENDA_REALIZADO, RIGHT);
        lbRealizadoHoje = new LabelName(Messages.META_VENDA_REALIZADO);
        lbPctRealizadoHoje = new LabelName(Messages.META_VENDA_PERC_REALIZADO);
        lbRealizadoHojeValue = new LabelValue("-");
        lbPctRealizadoHojeValue = new LabelValue("-");
        lbVariacao = new LabelValue(Messages.META_VENDA_VARIACAO, RIGHT);
        lbVariacaoHoje = new LabelName(Messages.META_VENDA_VARIACAO);
        lbPctVariacaoHoje = new LabelName(Messages.META_VENDA_PERC_VARIACAO);
        lbVariacaoHojeValue = new LabelValue("-");
        lbPctVariacaoHojeValue = new LabelValue("-");
		columnChart = new BaseColumnChart("", new String[]{"*"});
		columnChart.showCategories = false;
        scTabContainer = new ScrollTabbedContainer(new String[]{Messages.META_VENDA_PAGE_PAINEL, Messages.META_VENDA_PAGE_GRAFICO, Messages.META_VENDA_PAGE_LISTA});
        scTabContainer.allSameWidth = true;
        if (!loadConfigsPadraoMetaVenda()) {
        	loadDadosMetaSelecionada();
        }
		cbFamiliaClick();
    }

    private boolean loadConfigsPadraoMetaVenda() {
		try {
			ConfigInterno configInternoFilter = new ConfigInterno();
			configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			configInternoFilter.cdConfigInterno = ConfigInterno.configsPadraoMetaVenda;
			configInternoFilter.vlChave = ConfigInterno.VLCHAVEDEFAULT;
			configInternoFilter = (ConfigInterno)ConfigInternoService.getInstance().findByRowKey(configInternoFilter.getRowKey());
			if (configInternoFilter != null) {
				String[] values = StringUtil.split(configInternoFilter.vlConfigInterno, UiUtil.defaultSeparatorValue);
				lastActiveTab = ValueUtil.getIntegerValue(values[3]);
				cbRepresentante.setValue(values[2]);
				if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
					cbRepresentante.setSelectedIndex(0);
				}
				cbMetaVenda.setValue(values[0]);
				if (ValueUtil.isEmpty(cbMetaVenda.getValue())) {
					cbMetaVenda.setSelectedIndex(0);
				}
				loadDadosMetaSelecionada();
				cbFamilia.setValue(values[1]);
				if (ValueUtil.isEmpty(cbFamilia.getValue())) {
					cbFamilia.setSelectedIndex(0);
				}
				cbFamiliaClick();
				return true;
			}
		} catch (Throwable e) {
			//--
		}
		return false;
	}

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetaVendaTipoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
		MetaVendaTipo domainFilter = new MetaVendaTipo();
		return domainFilter;
	}

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
    	if (ValueUtil.isEmpty(cbMetaVenda.getValue())) {
    		return new Vector(0);
    	}
		MetaVendaRep metaVendaRep = new MetaVendaRep();
		metaVendaRep.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			metaVendaRep.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		}
		metaVendaRep.cdRepresentante = getCdRepresentante();
		metaVendaRep.cdMetaVenda = cbMetaVenda.getValue();
		metaVendaRep.vlChaveTipo = cbFamilia.getValue();
		Vector metaVendaRepTempList = MetaVendaRepService.getInstance().findAllByExample(metaVendaRep);
		Vector metaVendaRepFinalList = new Vector(0);
		for (int i = 0; i < metaVendaRepTempList.size(); i++) {
			metaVendaRep = (MetaVendaRep)metaVendaRepTempList.items[i];
			if (metaVendaRep.vlMetaVenda > 0) {
				metaVendaRepFinalList.addElement(metaVendaRep);
			}
		}
		return metaVendaRepFinalList;
    }

    private String getCdRepresentante() {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
   			return cbRepresentante.getValue();
    	} else {
    		return SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	}
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	MetaVenda metaVenda = (MetaVenda)cbMetaVenda.getSelectedItem();
    	MetaVendaRep metaVendaRep = (MetaVendaRep) domain;
    	double vlMetaHoje = metaVendaRep.vlMetaVenda / metaVenda.getNuDiasUteisPeriodo() * metaVenda.getNuDiaUtilHoje();
		double pctMetaHoje = (metaVendaRep.vlMetaVenda > 0) ? (vlMetaHoje * 100) / metaVendaRep.vlMetaVenda : 0;
		double pctRealizadoHoje = (metaVendaRep.vlMetaVenda > 0) ? (metaVendaRep.vlRealizadoVenda * 100) / metaVendaRep.vlMetaVenda : 0;
        String[] item = {
                StringUtil.getStringValue(FamiliaService.getInstance().getDsFamilia(metaVendaRep.vlChaveTipo)),
                StringUtil.getStringValue(RepresentanteService.getInstance().getDescriptionWithId(metaVendaRep.cdRepresentante)),
                StringUtil.getStringValueToInterface(metaVendaRep.vlMetaVenda),
                StringUtil.getStringValueToInterface(vlMetaHoje),
                StringUtil.getStringValueToInterface(pctMetaHoje),
                StringUtil.getStringValueToInterface(metaVendaRep.vlRealizadoVenda),
                StringUtil.getStringValueToInterface(pctRealizadoHoje),
                StringUtil.getStringValueToInterface(metaVendaRep.vlRealizadoVenda - vlMetaHoje),
                StringUtil.getStringValueToInterface(pctRealizadoHoje - pctMetaHoje)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    private String loadDadosMetaSelecionada() throws SQLException {
    	MetaVenda metaVenda = (MetaVenda)cbMetaVenda.getSelectedItem();
    	if (metaVenda != null) {
    		lbVariavelMeta.setValue(MetaVendaService.getInstance().getDsVarivavelMeta(metaVenda));
    		cbFamilia.load(metaVenda.cdMetaVenda);
    		cbFamilia.setSelectedIndex(0);
    		if (MetaVenda.CDTIPOMETA_GERAL.equals(metaVenda.cdTipoMetaVenda)) {
    			showComboFamilia = false;
    			return MetaVenda.CDTIPOMETA_GERAL;
    		} else if (MetaVenda.CDTIPOMETA_FAMILIA_PRODUTOS.equals(metaVenda.cdTipoMetaVenda)) {
    			showComboFamilia = true;
    			return MetaVenda.CDTIPOMETA_FAMILIA_PRODUTOS;
	    	} else {
	    		cbFamilia.removeAll();
	    	}
    	}
    	return "0";
    }

    private void cbFamiliaClick() throws SQLException {
    	MetaVenda metaVenda = (MetaVenda)cbMetaVenda.getSelectedItem();
    	if (metaVenda != null) {
    		Vector metaVendaRepList = getDomainList();
    		double vlMetaVenda = 0;
    		double vlMetaRealizado = 0;
    		for (int i = 0; i < metaVendaRepList.size(); i++) {
    			MetaVendaRep metaVendaRep = (MetaVendaRep)metaVendaRepList.items[i];
				vlMetaVenda += metaVendaRep.vlMetaVenda;
				vlMetaRealizado += metaVendaRep.vlRealizadoVenda;
    		}
    		lbVariavelMetaValue.setValue(vlMetaVenda);
    		//--
    		double vlMetaHoje = vlMetaVenda / metaVenda.getNuDiasUteisPeriodo() * metaVenda.getNuDiaUtilHoje();
    		double pctMetaHoje = (vlMetaVenda > 0) ? (vlMetaHoje * 100) / vlMetaVenda : 0;
    		lbPlanejadoHojeValue.setValue(vlMetaHoje);
    		lbPctPlanejadoHojeValue.setValue(pctMetaHoje);
    		//--
    		double pctRealizadoHoje = (vlMetaVenda > 0) ? (vlMetaRealizado * 100) / vlMetaVenda : 0;
    		lbRealizadoHojeValue.setValue(vlMetaRealizado);
    		lbPctRealizadoHojeValue.setValue(pctRealizadoHoje);
    		lbVariacaoHojeValue.setValue(vlMetaRealizado - vlMetaHoje);
    		lbPctVariacaoHojeValue.setValue(pctRealizadoHoje - pctMetaHoje);
    		if (vlMetaRealizado - vlMetaHoje >= 0) {
    			lbVariacaoHojeValue.setForeColor(corPositivo);
    			lbPctVariacaoHojeValue.setForeColor(corPositivo);
    		} else {
    			lbVariacaoHojeValue.setForeColor(corNegativo);
    			lbPctVariacaoHojeValue.setForeColor(corNegativo);
    		}
    		createGraficoMeta(vlMetaVenda, vlMetaHoje, vlMetaRealizado);
    	} else {
    		lbVariavelMetaValue.setValue("");
    		lbPlanejadoHojeValue.setValue("");
    		lbPctPlanejadoHojeValue.setValue("");
    		lbRealizadoHojeValue.setValue("");
    		lbPctRealizadoHojeValue.setValue("");
    		lbVariacaoHojeValue.setValue("");
    		lbPctVariacaoHojeValue.setValue("");
    		columnChart.series.removeAllElements();
    	}
    }

    private void createGraficoMeta(double vlMetaVenda, double vlMetaHoje, double vlMetaRealizado) {
        columnChart.series.removeAllElements();
    	columnChart.series.addElement(new Series(Messages.META_VENDA_TOTAL, new double[]{vlMetaVenda}, Color.getRGB(9, 48, 80))); //AZUL FORTE
    	columnChart.series.addElement(new Series(Messages.META_VENDA_META_HOJE, new double[]{vlMetaHoje}, Color.getRGB(79, 148, 205))); //AZUL CLARO
    	columnChart.series.addElement(new Series(Messages.META_VENDA_REALIZADO, new double[]{vlMetaRealizado}, vlMetaRealizado >= vlMetaHoje ? corPositivo : corNegativo)); //VERDE ou VERMELHO
    	// Arredonda os valores de intervalos da grid
    	double maxValue = vlMetaVenda > vlMetaRealizado ? vlMetaVenda : vlMetaRealizado;
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

    //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, lbRep, cbRepresentante, getLeft(), getTop() + HEIGHT_GAP);
    		UiUtil.add(this, lbMetaVenda, cbMetaVenda, getLeft(), AFTER + HEIGHT_GAP);
    	} else {
    		UiUtil.add(this, lbMetaVenda, cbMetaVenda, getLeft(), getTop() + HEIGHT_GAP);
    	}
    	if (showComboFamilia) {
    		UiUtil.add(this, lbFamilia, cbFamilia, getLeft(), AFTER + HEIGHT_GAP);
    	} else {
    		remove(lbFamilia);
    		remove(cbFamilia);
    	}
    	UiUtil.add(this, scTabContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
    	//--
    	Container container = scTabContainer.getContainer(0);
    	UiUtil.add(container, lbPlanejado, CENTER, TOP + HEIGHT_GAP);
    	UiUtil.add(container, lbVariavelMeta, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbVariavelMetaValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	UiUtil.add(container, lbPlanejadoHoje, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbPlanejadoHojeValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	UiUtil.add(container, lbPctPlanejadoHoje, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbPctPlanejadoHojeValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	UiUtil.add(container, lbRealizado, CENTER, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbRealizadoHoje, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbRealizadoHojeValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	UiUtil.add(container, lbPctRealizadoHoje, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbPctRealizadoHojeValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	UiUtil.add(container, lbVariacao, CENTER, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbVariacaoHoje, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbVariacaoHojeValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	UiUtil.add(container, lbPctVariacaoHoje, CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(container, lbPctVariacaoHojeValue, AFTER + WIDTH_GAP_BIG, SAME, FILL);
    	container = scTabContainer.getContainer(1);
        UiUtil.add(container, columnChart, LEFT, TOP + HEIGHT_GAP, FILL, FILL - HEIGHT_GAP * 5);
        container = scTabContainer.getContainer(2);
        int oneChar = fm.charWidth('A');
        GridColDefinition[] gridColDefiniton = {
    		new GridColDefinition(showComboFamilia ? Messages.META_VENDA_FAMILIA : FrameworkMessages.CAMPO_ID, oneChar * 18, LEFT),
    		new GridColDefinition(Messages.REPRESENTANTE_NOME_ENTIDADE, SessionLavenderePda.isUsuarioSupervisor() ? oneChar * 18 : 0, LEFT),
            new GridColDefinition(Messages.META_VENDA_TOTAL, oneChar * 9, RIGHT),
            new GridColDefinition(Messages.META_VENDA_META_HOJE, oneChar * 9, RIGHT),
            new GridColDefinition(Messages.META_VENDA_PERC_META_HOJE, oneChar * 5, RIGHT),
            new GridColDefinition(Messages.META_VENDA_REALIZADO, oneChar * 9, RIGHT),
            new GridColDefinition(Messages.META_VENDA_PERC_REALIZADO, oneChar * 5, RIGHT),
            new GridColDefinition(Messages.META_VENDA_VARIACAO, oneChar * 9, RIGHT),
            new GridColDefinition(Messages.META_VENDA_PERC_VARIACAO, oneChar * 5, RIGHT)
        };
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        gridEdit.setGridControllable();
        UiUtil.add(container, gridEdit, LEFT, TOP + HEIGHT_GAP, FILL, FILL);
        scTabContainer.setActiveTab(0);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentante) {
					if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
					}
					cbFamiliaClick();
					list();
				} else if (event.target == cbMetaVenda) {
					loadDadosMetaSelecionada();
					cbFamiliaClick();
					//--
					lastActiveTab = scTabContainer.getActiveTab();
					scTabContainer.setActiveTab(0);
					onFormStart();
					list();
					repaintNow();
				} else if (event.target == cbFamilia) {
					cbFamiliaClick();
					list();
				}
				break;
			}
    	}
    }

	public void list() throws SQLException {
    	super.list();
    	gridEdit.qsort(ValueUtil.isEmpty(cbFamilia.getValue()) ? 0 : 1);
    	for (int i = 0; i < gridEdit.size(); i++) {
    		if (ValueUtil.getDoubleValue(gridEdit.getItem(i)[7]) < 0) {
    			gridEdit.gridController.setCelForeColor(corNegativo, i, 7);
    			gridEdit.gridController.setCelForeColor(corNegativo, i, 8);
    		} else {
    			gridEdit.gridController.setCelForeColor(corPositivo, i, 7);
    			gridEdit.gridController.setCelForeColor(corPositivo, i, 8);
    		}
		}
    }

    public void onFormClose() throws SQLException {
    	super.onFormClose();
    	ConfigInternoService.getInstance().salvaInfosPadraoMetaVenda(cbMetaVenda.getValue(), cbFamilia.getValue(), cbRepresentante.getValue(), scTabContainer.getActiveTab());
    }

    public boolean hasMetaVendaAbaixoPlanejadoHoje() throws SQLException {
    	Vector listFamilias;
    	MetaVenda metaVenda;
    	String cdFamilia;
    	for (int i = 0; i < cbMetaVenda.size(); i++) {
    		metaVenda = (MetaVenda)cbMetaVenda.getItemAt(i);
    		listFamilias = FamiliaService.getInstance().findAllFamiliasByMetaVenda(metaVenda.cdMetaVenda);
    		for (int j = 0; j < listFamilias.size(); j++) {
    			cdFamilia = ((Familia)listFamilias.items[j]).cdFamilia;
        		MetaVendaRep metaVendaRep = new MetaVendaRep();
        		metaVendaRep.cdEmpresa = SessionLavenderePda.cdEmpresa;
        		if (SessionLavenderePda.isUsuarioSupervisor()) {
        			metaVendaRep.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
        		} else {
        			metaVendaRep.cdRepresentante = getCdRepresentante();
        		}
        		metaVendaRep.cdMetaVenda = metaVenda.cdMetaVenda;
        		metaVendaRep.vlChaveTipo = cdFamilia;
        		Vector metaVendaRepList = MetaVendaRepService.getInstance().findAllByExample(metaVendaRep);
        		double vlMetaVenda = 0;
        		double vlMetaRealizado = 0;
        		for (int x = 0; x < metaVendaRepList.size(); x++) {
        			metaVendaRep = (MetaVendaRep)metaVendaRepList.items[x];
        			vlMetaVenda += metaVendaRep.vlMetaVenda;
        			vlMetaRealizado += metaVendaRep.vlRealizadoVenda;
        		}
        		double vlMetaHoje = vlMetaVenda / metaVenda.getNuDiasUteisPeriodo() * metaVenda.getNuDiaUtilHoje();
        		if (vlMetaRealizado - vlMetaHoje < 0) {
        			return true;
        		}
			}
		}
    	return false;
    }


}
