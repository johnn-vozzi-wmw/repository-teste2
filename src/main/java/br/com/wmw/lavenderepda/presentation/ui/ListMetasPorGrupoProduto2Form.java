package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorGrupoProduto;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import br.com.wmw.lavenderepda.business.service.MetasPorGrupoProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListMetasPorGrupoProduto2Form extends BaseCrudListForm {

	private static final String INITIAL_VALUE_TOTALIZADOR = "999999999,9999999";
	private LabelValue lbPeriodo;
	private RepresentanteSupervComboBox cbRepresentante;
	private ButtonOptions bmOpcoes;
	private String[] colunasGrid = LavenderePdaConfig.getMostraDetalhesAdicionaisRelMetasPorGrupoProduto1();
	protected SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lbTotalMeta;
	private LabelTotalizador lbTotalRealizado;
	private LabelTotalizador lbTotalQtUnidadeMeta;
	private LabelTotalizador lbTotalQtUnidadeRealizado;
	private LabelTotalizador lbTotalQtCaixaMeta;
	private LabelTotalizador lbTotalQtCaixaRealizado;
	private LabelTotalizador lbTotalMixClienteMeta;
	private LabelTotalizador lbTotalMixClienteRealizado;
	public  boolean totalizadoresMetaVisible;
	public boolean totalizadoresQtUnidadeVisible;
	public boolean totalizadoresQtMixClienteVisible;
	public boolean totalizadoresQtCaixaVisible;

	private double vlTtMeta;
	private double vlTtRealizado;
	private double qtTtUnidadeMeta;
	private double qtTtUnidadeRealizado;
	private double qtTtCaixaMeta;
	private double qtTtCaixaRealizado;
	private double qtTtMixClienteMeta;
	private double qtTtMixClienteRealizado;
	private String cdGrupoProduto1;
	private String dsPeriodo;

    public ListMetasPorGrupoProduto2Form(String cdGrupoProduto1, String dsPeriodo) throws SQLException {
        super(Messages.MENU_OPCAO_METAS_GRUPO_PRODUTO);
        singleClickOn = true;
        setBaseCrudCadForm(new CadMetasPorGrupoProduto1Form(2, false));
        this.cdGrupoProduto1 = cdGrupoProduto1;
        this.dsPeriodo = dsPeriodo;
        cbRepresentante = new RepresentanteSupervComboBox(RepresentanteSupervComboBox.DefaultItemType_ALL);
        lbPeriodo = new LabelValue(dsPeriodo);
        bmOpcoes = new ButtonOptions();
		setTotalizadoresVisible();
		sessaoTotalizadores = new SessionTotalizerContainer();
		if (totalizadoresMetaVisible) {
			lbTotalMeta = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[0] + INITIAL_VALUE_TOTALIZADOR);
			lbTotalRealizado = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[1] + INITIAL_VALUE_TOTALIZADOR, RIGHT);
		}
		if (totalizadoresQtUnidadeVisible) {
			lbTotalQtUnidadeMeta = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[3] + INITIAL_VALUE_TOTALIZADOR);
			lbTotalQtUnidadeRealizado = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[4] + INITIAL_VALUE_TOTALIZADOR, RIGHT);
		}
		if (totalizadoresQtCaixaVisible) {
			lbTotalQtCaixaMeta = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[6] + INITIAL_VALUE_TOTALIZADOR);
			lbTotalQtCaixaRealizado = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[7] + INITIAL_VALUE_TOTALIZADOR, RIGHT);
		}
		if (totalizadoresQtMixClienteVisible) {
			lbTotalMixClienteMeta = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[9] + INITIAL_VALUE_TOTALIZADOR);
			lbTotalMixClienteRealizado = new LabelTotalizador(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[10] + INITIAL_VALUE_TOTALIZADOR, RIGHT);
		}
	}

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetasPorGrupoProdutoService.getInstance();
    }

    protected Vector getDomainList() throws java.sql.SQLException {
    	MetasPorGrupoProduto metasPorGrupoProduto1Filter = new MetasPorGrupoProduto();
    	metasPorGrupoProduto1Filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
   		metasPorGrupoProduto1Filter.cdRepresentante = getCdRepresentante();
		metasPorGrupoProduto1Filter.dsPeriodo = dsPeriodo;
		metasPorGrupoProduto1Filter.cdGrupoProduto1 = cdGrupoProduto1;
		return MetasPorGrupoProdutoService.getInstance().findAllMetasByGrupoProduto(metasPorGrupoProduto1Filter, 2);
    }


    protected BaseDomain getDomainFilter() throws SQLException {
    	return new MetasPorGrupoProduto();
    }

    private String getCdRepresentante() {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (cbRepresentante.getSelectedIndex() == 0) {
    			return "";
    		} else {
    			return cbRepresentante.getValue();
    		}
    	} else {
    		return SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	MetasPorGrupoProduto metasPorGrupoProduto1 = (MetasPorGrupoProduto) domain;
    	Vector item = new Vector();
    	item.addElement(metasPorGrupoProduto1.cdGrupoProduto2);
    	if (ValueUtil.isEmpty(metasPorGrupoProduto1.dsGrupoProduto2) && !LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd) {
    		item.addElement(GrupoProduto2Service.getInstance().getDsGrupoProduto(metasPorGrupoProduto1.cdGrupoProduto1, metasPorGrupoProduto1.cdGrupoProduto2));
    	} else {
    		if (metasPorGrupoProduto1.dsGrupoProduto2 == null || metasPorGrupoProduto1.dsGrupoProduto2.equalsIgnoreCase(metasPorGrupoProduto1.cdGrupoProduto2)) {
    			item.addElement("[" + metasPorGrupoProduto1.cdGrupoProduto2 + "]");
    		} else {
    			item.addElement(metasPorGrupoProduto1.dsGrupoProduto2 + " [" + metasPorGrupoProduto1.cdGrupoProduto2 + "]");
    		}
    	}
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado()) {
        	for (int i = 0; i < colunasGrid.length; i++) {
        		switch (i) {
					case 0:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.vlMeta));
							vlTtMeta += metasPorGrupoProduto1.vlMeta;
						}
						break;
					case 1:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.vlRealizado));
							vlTtRealizado += metasPorGrupoProduto1.vlRealizado;
						}
						break;
					case 2:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.getPctRealizadoMeta()));
						}
						break;
					case 3:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.qtUnidadeMeta));
							qtTtUnidadeMeta += metasPorGrupoProduto1.qtUnidadeMeta;
						}
						break;
					case 4:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.qtUnidadeRealizado));
							qtTtUnidadeRealizado += metasPorGrupoProduto1.qtUnidadeRealizado;
						}
						break;
					case 5:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.getPctRealizadoQtUnidade()));
						}
						break;
					case 6:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.qtCaixaMeta));
							qtTtCaixaMeta += metasPorGrupoProduto1.qtCaixaMeta;
						}
						break;
					case 7:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.qtCaixaRealizado));
							qtTtCaixaRealizado += metasPorGrupoProduto1.qtCaixaRealizado;
						}
						break;
					case 8:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.getPctRealizadoQtCaixa()));
						}
						break;
					case 9:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.qtMixClienteMeta));
							qtTtMixClienteMeta += metasPorGrupoProduto1.qtMixClienteMeta;
						}
						break;
					case 10:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.qtMixClienteRealizado));
							qtTtMixClienteRealizado += metasPorGrupoProduto1.qtMixClienteRealizado;
						}
						break;
					case 11:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.getPctRealizadoQtMixCliente()));
						}
						break;
					case 12:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(metasPorGrupoProduto1.dtInicial)));
						}
						break;		
					case 13:
						if (!ValueUtil.isEmpty(colunasGrid[i])) {
							item.addElement(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(metasPorGrupoProduto1.dtFinal)));
						}
						break;	
	        	}
        	}
        	return (String[])item.toObjectArray();
        } else {
        	item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.vlMeta));
        	vlTtMeta += metasPorGrupoProduto1.vlMeta;
			item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.vlRealizado));
			vlTtRealizado += metasPorGrupoProduto1.vlRealizado;
			item.addElement(StringUtil.getStringValueToInterface(metasPorGrupoProduto1.getPctRealizadoMeta()));
			item.addElement(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(metasPorGrupoProduto1.dtInicial)));
			item.addElement(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(metasPorGrupoProduto1.dtFinal)));			
        	return (String[])item.toObjectArray();
        }
    }

	private void updateTotalizadores() {
		if (totalizadoresMetaVisible) {
			lbTotalMeta.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[0] + " " + StringUtil.getStringValueToInterface(vlTtMeta));
			lbTotalRealizado.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[1] + " " + StringUtil.getStringValueToInterface(vlTtRealizado));
		}
		if (totalizadoresQtUnidadeVisible) {
			lbTotalQtUnidadeMeta.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[3] + " " + StringUtil.getStringValueToInterface(qtTtUnidadeMeta));
			lbTotalQtUnidadeRealizado.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[4] + " " + qtTtUnidadeRealizado);
		}
		if (totalizadoresQtCaixaVisible) {
			lbTotalQtCaixaMeta.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[6] + " " + StringUtil.getStringValueToInterface(qtTtCaixaMeta));
			lbTotalQtCaixaRealizado.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[7] + " " + StringUtil.getStringValueToInterface(qtTtCaixaRealizado));
		}
		if (totalizadoresQtMixClienteVisible) {
			lbTotalMixClienteMeta.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[9] + " " + StringUtil.getStringValueToInterface(qtTtMixClienteMeta));
			lbTotalMixClienteRealizado.setValue(Messages.LABEL_TOTAL_ABREVIADO + colunasGrid[10] + " " + StringUtil.getStringValueToInterface(qtTtMixClienteRealizado));
		}
	}

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
		if (totalizadoresMetaVisible || totalizadoresQtUnidadeVisible || totalizadoresQtMixClienteVisible || totalizadoresQtCaixaVisible) {
			UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, getHeigthSessaoTotalizadores());
		}
		if (totalizadoresQtMixClienteVisible) {
			UiUtil.add(sessaoTotalizadores, lbTotalMixClienteMeta, LEFT + UiUtil.getTotalizerGap(), getNextY());
			UiUtil.add(sessaoTotalizadores, lbTotalMixClienteRealizado, RIGHT - UiUtil.getTotalizerGap(), SAME);
		}
		if (totalizadoresQtCaixaVisible) {
			UiUtil.add(sessaoTotalizadores, lbTotalQtCaixaMeta, LEFT + UiUtil.getTotalizerGap(), getNextY());
			UiUtil.add(sessaoTotalizadores, lbTotalQtCaixaRealizado, RIGHT - UiUtil.getTotalizerGap(), SAME);
		}
		if (totalizadoresQtUnidadeVisible) {
			UiUtil.add(sessaoTotalizadores, lbTotalQtUnidadeMeta, LEFT + UiUtil.getTotalizerGap(), getNextY());
			UiUtil.add(sessaoTotalizadores, lbTotalQtUnidadeRealizado, RIGHT - UiUtil.getTotalizerGap(), SAME);
		}
		if (totalizadoresMetaVisible) {
			UiUtil.add(sessaoTotalizadores, lbTotalMeta, LEFT + UiUtil.getTotalizerGap(), getNextY());
			UiUtil.add(sessaoTotalizadores, lbTotalRealizado, RIGHT - UiUtil.getTotalizerGap(), SAME);
		}
        if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getTop() + HEIGHT_GAP);
    	} else {
        	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getTop(), PREFERRED);
    	}
        //--
    	UiUtil.add(this, new LabelName(Messages.METAS_META), lbPeriodo, getLeft(), getNextY(), PREFERRED);
        UiUtil.add(this, new LabelName(GrupoProduto1Service.getInstance().getLabelGrupoProduto1()), new LabelValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(cdGrupoProduto1)), getLeft(), getNextY(), PREFERRED);
        //--
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado()) {
        	int ww = fm.stringWidth("www");
        	gridEdit = UiUtil.createGridEdit(gridColDefinitionDetalhesAdicionais(ww));
        } else {
        	int ww = fm.stringWidth("www");
        	GridColDefinition[] gridColDefiniton = {
                new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
    			new GridColDefinition(GrupoProduto2Service.getInstance().getLabelGrupoProduto2(), ww * 5, LEFT),
    			new GridColDefinition(Messages.METAS_VLMETA, ww * 4, RIGHT),
    			new GridColDefinition(Messages.METAS_VLRESULTADO, ww * 4, RIGHT),
    			new GridColDefinition(Messages.METAS_PCTREALIZADO, ww * 2, RIGHT),
        		new GridColDefinition(Messages.METAS_DTINICIAL, ww * 3, RIGHT),
        		new GridColDefinition(Messages.METAS_DTFINAL, ww * 3, RIGHT)};
        	gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        }
		UiUtil.add(this, gridEdit, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight() - sessaoTotalizadores.getHeight());
		addItensOnButtonMenu();
		UiUtil.add(barBottomContainer, bmOpcoes, 3);
		sessaoTotalizadores.resizeHeight();
		sessaoTotalizadores.reposition();
    }

    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	list();
    }

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	bmOpcoes.addItem(Messages.MENU_UTILITARIO_CALCULADORA);
    }

    public void loadDefaultFilters() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		cbRepresentante.setSelectedIndex(0);
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    		}
    	}
    }

    private GridColDefinition[] gridColDefinitionDetalhesAdicionais(int ww) {
    	Vector gridColDefinitonTemp = new Vector();
    	gridColDefinitonTemp.addElement(new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT));
    	gridColDefinitonTemp.addElement(new GridColDefinition(GrupoProduto2Service.getInstance().getLabelGrupoProduto2(), ww * 4, LEFT));
    	for (int i = 0; i < colunasGrid.length; i++) {
			gridColDefinitonTemp.addElement(new GridColDefinition(ValueUtil.isNotEmpty(colunasGrid[i]) && !".".equals(colunasGrid[i]) ? colunasGrid[i] : FrameworkMessages.CAMPO_ID, ww * 3, RIGHT));
    	}
    	//Converte o Vector populado em uma lista de colunas da grid
    	GridColDefinition[] gridColDefiniton = new GridColDefinition[gridColDefinitonTemp.size()];
    	for (int pos = 0; pos < gridColDefinitonTemp.size(); pos++) {
    		gridColDefiniton[pos] = (GridColDefinition) gridColDefinitonTemp.items[pos];
    	}
    	return gridColDefiniton;
    }

    private void resetTotalizadores() {
    	vlTtMeta = 0;
    	vlTtRealizado = 0;
    	qtTtUnidadeMeta = 0;
    	qtTtUnidadeRealizado = 0;
    	qtTtCaixaMeta = 0;
    	qtTtCaixaRealizado = 0;
    	qtTtMixClienteMeta = 0;
    	qtTtMixClienteRealizado = 0;
    }

    public void list() throws SQLException {
    	resetTotalizadores();
    	super.list();
    	updateTotalizadores();
    }

    //@Override
    public void detalhesClick() throws SQLException {
    	String cdGrupoProdutoSelected = getSelectedRowKey();
    	MetasPorGrupoProduto metasPorGrupoProduto = new MetasPorGrupoProduto();
    	metasPorGrupoProduto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	metasPorGrupoProduto.cdRepresentante = getCdRepresentante();
    	metasPorGrupoProduto.dsPeriodo = dsPeriodo;
    	metasPorGrupoProduto.cdGrupoProduto1 = cdGrupoProduto1;
    	metasPorGrupoProduto.cdGrupoProduto2 = cdGrupoProdutoSelected;
    	metasPorGrupoProduto.cdGrupoProduto3 = MetasPorGrupoProduto.CDGRUPOPRODUTO_NULO;
    	metasPorGrupoProduto.cdProduto = MetasPorGrupoProduto.CDGRUPOPRODUTO_NULO;
    	if (ValueUtil.isEmpty(getCdRepresentante())) {
			Vector list = MetasPorGrupoProdutoService.getInstance().findAllMetasByGrupoProduto(metasPorGrupoProduto, 2);
			if (list.size() > 0) {
				metasPorGrupoProduto = (MetasPorGrupoProduto)list.items[0];
				metasPorGrupoProduto.dsPeriodo = dsPeriodo;
				getBaseCrudCadForm().edit(metasPorGrupoProduto);
				show(getBaseCrudCadForm());
				return;
			}
		} else {
	    	metasPorGrupoProduto = (MetasPorGrupoProduto)MetasPorGrupoProdutoService.getInstance().findByRowKey(metasPorGrupoProduto.getRowKey());
			if (metasPorGrupoProduto != null) {
				getBaseCrudCadForm().edit(metasPorGrupoProduto);
				show(getBaseCrudCadForm());
				return;
			}
		}
    	if (LavenderePdaConfig.isNivelGrupoProd3()) {
	    	ListMetasPorGrupoProduto3Form listMetasPorGrupoProduto2Form = new ListMetasPorGrupoProduto3Form(cdGrupoProduto1, cdGrupoProdutoSelected, dsPeriodo);
	    	show(listMetasPorGrupoProduto2Form);
	    } else {
			ListMetasPorGrupoProduto4Form listMetasPorGrupoProduto4Form = new ListMetasPorGrupoProduto4Form(cdGrupoProduto1, cdGrupoProdutoSelected, MetasPorGrupoProduto.CDGRUPOPRODUTO_NULO, dsPeriodo);
	    	show(listMetasPorGrupoProduto4Form);
		}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		case ControlEvent.PRESSED: {
    			if (event.target == cbRepresentante) {
    				if (event.target == cbRepresentante) {
    					SessionLavenderePda.setRepresentante(null);
    					if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
    						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
    					}
    				}
					list();
    			}
    			break;
    		}
    		case ButtonOptionsEvent.OPTION_PRESS : {
    			if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(Messages.MENU_UTILITARIO_CALCULADORA)) {
						(new Calculator()).popup();
					}
				}
    			break;
    		}
    	}
    }

    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    		} else {
    			cbRepresentante.setSelectedIndex(0);
    			SessionLavenderePda.setRepresentante(null);
    		}
    	}
    	list();
    }

	public void setTotalizadoresVisible() {
		totalizadoresMetaVisible = !LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado() || (ValueUtil.isNotEmpty(colunasGrid[0]) && !".".equals(colunasGrid[0])) || (ValueUtil.isNotEmpty(colunasGrid[1]) && !".".equals(colunasGrid[1]));
		totalizadoresQtUnidadeVisible = (ValueUtil.isNotEmpty(colunasGrid[3]) && !".".equals(colunasGrid[3])) || (ValueUtil.isNotEmpty(colunasGrid[4]) && !".".equals(colunasGrid[4]));
		totalizadoresQtCaixaVisible = (ValueUtil.isNotEmpty(colunasGrid[6]) && !".".equals(colunasGrid[6])) || (ValueUtil.isNotEmpty(colunasGrid[7]) && !".".equals(colunasGrid[7]));
		totalizadoresQtMixClienteVisible = (ValueUtil.isNotEmpty(colunasGrid[9]) && !".".equals(colunasGrid[9])) || (ValueUtil.isNotEmpty(colunasGrid[10]) && !".".equals(colunasGrid[10]));
	}

	public int getHeigthSessaoTotalizadores() {
		int heigthSessaoTotalizadores = totalizadoresMetaVisible ? UiUtil.getLabelPreferredHeight() : 0;
		heigthSessaoTotalizadores += totalizadoresQtUnidadeVisible ? UiUtil.getLabelPreferredHeight() : 0;
		heigthSessaoTotalizadores += totalizadoresQtCaixaVisible ? UiUtil.getLabelPreferredHeight() : 0;
		heigthSessaoTotalizadores += totalizadoresQtMixClienteVisible ? UiUtil.getLabelPreferredHeight() : 0;
		return heigthSessaoTotalizadores;
	}

}
