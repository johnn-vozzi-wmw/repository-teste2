package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.MetasPorProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.PeriodoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListMetasPorProdutoForm extends BaseCrudListForm {

	private PeriodoComboBox cbPeriodo;
	private RepresentanteSupervComboBox cbRepresentante;
	private boolean flListInicialized;
	private ButtonOptions bmOpcoes;
	private LabelTotalizador lbTotalMeta;
	private LabelTotalizador lbTotalRealizado;
	protected SessionTotalizerContainer sessaoTotalizadores;
	private static String MENU_UTILITARIO_CALCULADORA = "";

    public ListMetasPorProdutoForm() throws SQLException {
        super(Messages.METASPORPRODUTO_TITULO_CADASTRO);
        singleClickOn = true;
        setBaseCrudCadForm(new CadMetasPorProdutoForm());
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        cbPeriodo = new PeriodoComboBox(PeriodoComboBox.PERIODOMETASPRODUTO);
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
        edFiltro.autoSelect = true;
        flListInicialized = false;
		sessaoTotalizadores = new SessionTotalizerContainer();
		lbTotalRealizado = new LabelTotalizador("999999999,99");
		lbTotalMeta = new LabelTotalizador("999999999,99");
		bmOpcoes = new ButtonOptions();
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetasPorProdutoService.getInstance();
    }

    public String[][] getItems(String[][] items) {
    	boolean detalhesAdicionais = LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado();
    	String[][] itensFinal = new String[items.length][detalhesAdicionais ? 11 : 5];
    	for (int i = 0; i < items.length; i++) {
    		itensFinal[i][0] = items[i][0];
    		itensFinal[i][1] = items[i][1] + "-" + items[i][2];
    		itensFinal[i][2] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][3]));
    		itensFinal[i][3] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][4]));
    		itensFinal[i][4] = StringUtil.getStringValueToInterface((ValueUtil.getDoubleValue(items[i][3]) > 0) ? (ValueUtil.getDoubleValue(items[i][4]) * 100) / ValueUtil.getDoubleValue(items[i][3]) : 0);
    		if (detalhesAdicionais) {
    			itensFinal[i][5] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][5]));
    			itensFinal[i][6] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][6]));
    			itensFinal[i][7] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][7]));
    			itensFinal[i][8] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][8]));
    			itensFinal[i][9] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][9]));
    			itensFinal[i][10] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][10]));
    		}
    	}
    	items = null;
    	return itensFinal;
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new MetasPorProduto();
    }

    protected Vector getDomainList() throws java.sql.SQLException {
    	String filtro = (edFiltro.getValue()).toUpperCase();
    	String cdRep = cbRepresentante.getValue();
    	if (!SessionLavenderePda.isUsuarioSupervisor()) {
    		cdRep = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
    	Vector result = MetasPorProdutoService.getInstance().getMetasProduto(filtro, cdRep, cbPeriodo.getValue());
    	return result;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	MetasPorProduto metasPorProduto = (MetasPorProduto) domain;
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
        	Vector item = new Vector();
        	String[] colunas = LavenderePdaConfig.getMostraDetalhesAdicionaisRelMetasPorProduto();
			item.addElement(metasPorProduto.rowKey);
			item.addElement(metasPorProduto.toString());
			item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.vlMeta));
			item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.vlRealizado));
			item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.getPctRealizadoMeta()));
        	for (int i = 0; i < colunas.length; i++) {
        		switch (i) {
					case 0:
						if (!ValueUtil.isEmpty(colunas[i]) && !".".equals(colunas[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.qtUnidadeMeta));
						}
						break;
					case 1:
						if (!ValueUtil.isEmpty(colunas[i]) && !".".equals(colunas[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.qtUnidadeRealizado));
						}
						break;
					case 2:
						if (!ValueUtil.isEmpty(colunas[i]) && !".".equals(colunas[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.qtCaixaMeta));
						}
						break;
					case 3:
						if (!ValueUtil.isEmpty(colunas[i]) && !".".equals(colunas[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.qtCaixaRealizado));
						}
						break;
					case 4:
						if (!ValueUtil.isEmpty(colunas[i]) && !".".equals(colunas[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.qtMixClienteMeta));
						}
						break;
					case 5:
						if (!ValueUtil.isEmpty(colunas[i]) && !".".equals(colunas[i])) {
							item.addElement(StringUtil.getStringValueToInterface(metasPorProduto.qtMixClienteRealizado));
						}
						break;
        		}
        	}
        	return (String[])item.toObjectArray();
        } else {
        	String[] item = {
        			StringUtil.getStringValue(metasPorProduto.rowKey),
        			StringUtil.getStringValue(metasPorProduto.toString()),
        			StringUtil.getStringValueToInterface(metasPorProduto.vlMeta),
        			StringUtil.getStringValueToInterface(metasPorProduto.vlRealizado),
        			StringUtil.getStringValueToInterface(metasPorProduto.getPctRealizadoMeta())};
        	return item;
        }
    }


    private void updateTotalizadores() throws SQLException {
		MetasPorProduto metasPorProdutoFilter = new MetasPorProduto();
		metasPorProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			metasPorProdutoFilter.cdRepresentante = cbRepresentante.getValue();
		} else {
			metasPorProdutoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		if (!ValueUtil.isEmpty(cbPeriodo.getValue())) {
			metasPorProdutoFilter.dsPeriodo = cbPeriodo.getValue();
		}
    	if (!ValueUtil.isEmpty(edFiltro.getValue())) {
    		metasPorProdutoFilter.findDescProduto = true;
    		metasPorProdutoFilter.cdProdutoLike = "%" + edFiltro.getValue() + "%";
    	}
    	double vlMeta = getCrudService().sumByExample(metasPorProdutoFilter, "vlMeta");
    	double vlRealizado = getCrudService().sumByExample(metasPorProdutoFilter, "vlRealizado");
    	//--
		lbTotalMeta.setValue(Messages.METAS_TTMETA + " " + vlMeta);
		lbTotalRealizado.setValue(Messages.METAS_TTRESULTADO + " " + vlRealizado);
    }


    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    public void initUI() {
    	if ((LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == 0) && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto != ValorParametro.PARAM_INT_VALOR_ZERO)) {
    		flListInicialized = true;
    	}
    	super.initUI();
    }

    //@Override
    protected void onFormStart() throws SQLException {
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight() - HEIGHT_GAP, FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, lbTotalMeta, LEFT + UiUtil.getTotalizerGap(), TOP + HEIGHT_GAP_BIG);
		UiUtil.add(sessaoTotalizadores, lbTotalRealizado, RIGHT - UiUtil.getTotalizerGap(), SAME);
        //--
        if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
    	} else {
        	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_PERIODO), cbPeriodo, getLeft(), AFTER + HEIGHT_GAP);
    	
		UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP);
		edFiltro.setText("");
        //--
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
        	int ww = fm.stringWidth("www");
        	gridEdit = UiUtil.createGridEdit(gridColDefinitionDetalhesAdicionais(ww));
        } else {
        	int ww = fm.stringWidth("www");
        	GridColDefinition[] gridColDefiniton = {
                new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
    			new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, ww * 5, LEFT),
    			new GridColDefinition(Messages.METAS_VLMETA, ww * 3, RIGHT),
    			new GridColDefinition(Messages.METAS_VLRESULTADO, ww * 3, RIGHT),
    			new GridColDefinition(Messages.METAS_PCTREALIZADO, ww * 2, RIGHT)};
        	gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        }
		UiUtil.add(this, gridEdit, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - (HEIGHT_GAP + barBottomContainer.getHeight() + lbTotalMeta.getHeight()));
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
    }

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	bmOpcoes.addItem(FrameworkMessages.BOTAO_DETALHES);
    	MENU_UTILITARIO_CALCULADORA = Messages.MENU_UTILITARIO_CALCULADORA;
    	bmOpcoes.addItem(MENU_UTILITARIO_CALCULADORA);
    }

    public void loadDefaultFilters() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		cbRepresentante.setSelectedIndex(0);
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    		}
    	}
        cbPeriodo.setSelectedIndex(0);
    }

    private GridColDefinition[] gridColDefinitionDetalhesAdicionais(int ww) {
    	//Recupera as colunas que serão mostradas no relatório
    	String[] colunas = LavenderePdaConfig.getMostraDetalhesAdicionaisRelMetasPorProduto();
    	Vector gridColDefinitonTemp = new Vector();
    	gridColDefinitonTemp.addElement(new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT));
    	gridColDefinitonTemp.addElement(new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, ww * 3, LEFT));
    	gridColDefinitonTemp.addElement(new GridColDefinition(Messages.METAS_VLMETA, (ww * 2) + (ww / 5), RIGHT));
    	gridColDefinitonTemp.addElement(new GridColDefinition(Messages.METAS_VLRESULTADO, (ww * 2) + (ww / 5), RIGHT));
    	gridColDefinitonTemp.addElement(new GridColDefinition(Messages.METAS_PCTREALIZADO, ww, RIGHT));
    	for (int i = 0; i < colunas.length; i++) {
    		if (ValueUtil.isNotEmpty(colunas[i]) && !".".equals(colunas[i])) {
    			gridColDefinitonTemp.addElement(new GridColDefinition(colunas[i], ww * 2, LEFT));
    	}
    	}
    	//Converte o Vector populado em uma lista de colunas da grid
    	GridColDefinition[] gridColDefiniton = new GridColDefinition[gridColDefinitonTemp.size()];
    	for (int pos = 0; pos < gridColDefinitonTemp.size(); pos++) {
    		gridColDefiniton[pos] = (GridColDefinition) gridColDefinitonTemp.items[pos];
    	}
    	return gridColDefiniton;
    }

    public void list() throws SQLException {
    	if (flListInicialized) {
        	super.list();
        	updateTotalizadores();
		}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		case ControlEvent.PRESSED: {
    			if (event.target == cbRepresentante) {
    				if ((event.target == cbRepresentante) && (ValueUtil.isNotEmpty(cbRepresentante.getValue()))) {
    					SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
    					cbPeriodo.reload(PeriodoComboBox.PERIODOMETASPRODUTO);
    				}
    				filtrarClick();
    			} else  if (event.target == cbPeriodo) {
    				filtrarClick();
    			}
    			break;
    		}
    		case ButtonOptionsEvent.OPTION_PRESS : {
    			if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(MENU_UTILITARIO_CALCULADORA)) {
						(new Calculator()).popup();
					} else if (bmOpcoes.selectedItem.equals(FrameworkMessages.BOTAO_DETALHES)) {
						detalhesClick();
					}
				}
    		}
    	}
    }
    
    protected void filtrarClick() throws SQLException {
		if (validateFiltro()) {
			flListInicialized = true;
			list();
			if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
				edFiltro.setValue("");
			}
        	edFiltro.requestFocus();
		}
    }

    private boolean validateFiltro() {
    	String filtro = edFiltro.getValue();
    	if (LavenderePdaConfig.usaPesquisaInicioString && edFiltro.getValue().startsWith("*")) {
    		filtro = edFiltro.getValue().substring(1);
    	}
    	if ((filtro == null) || (filtro.length() < LavenderePdaConfig.qtMinimaCaracteresFiltroProduto)) {
    		UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
    		return false;
    	}
    	return true;
	}

}
