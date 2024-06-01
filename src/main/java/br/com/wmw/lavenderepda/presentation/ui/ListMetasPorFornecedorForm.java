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
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorForn;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.MetasPorFornService;
import br.com.wmw.lavenderepda.presentation.ui.combo.PeriodoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListMetasPorFornecedorForm extends BaseCrudListForm {

	private PeriodoComboBox cbPeriodo;
	private RepresentanteSupervComboBox cbRepresentante;
	private ButtonOptions bmOpcoes;

    public ListMetasPorFornecedorForm() throws SQLException {
        super(Messages.METASPORFORNECEDOR_NOME_ENTIDADE);
        setBaseCrudCadForm(new CadMetasPorFornForm());
        singleClickOn = true;
        //--
        cbPeriodo = new PeriodoComboBox(PeriodoComboBox.PERIODOMETASFORNECEDOR);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        bmOpcoes = new ButtonOptions();
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetasPorFornService.getInstance();
    }

    //@Override
    protected String getDataSource() throws SQLException {
        return MetasPorFornService.getInstance().getMetasFornecedorList(cbPeriodo.getValue(), cbRepresentante.getValue());
    }

    public String[][] getItems(String[][] items) {
    	boolean detalhesAdicionais = LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas;
    	String[][] itensFinal = new String[items.length][detalhesAdicionais ? 9 : 5];
    	for (int i = 0; i < items.length; i++) {
    		itensFinal[i][0] = items[i][0];
    		itensFinal[i][1] = items[i][1];
    		itensFinal[i][2] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][2]));
    		itensFinal[i][3] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][3]));
    		itensFinal[i][4] = StringUtil.getStringValueToInterface((ValueUtil.getDoubleValue(items[i][2]) > 0) ? (ValueUtil.getDoubleValue(items[i][3]) * 100) / ValueUtil.getDoubleValue(items[i][2]) : 0);
    		if (detalhesAdicionais) {
    			itensFinal[i][5] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][4]));
    			itensFinal[i][6] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][5]));
    			itensFinal[i][7] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][6]));
    			itensFinal[i][8] = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(items[i][7]));
    		}
    	}
    	items = null;
    	return itensFinal;
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new MetasPorForn();
    }

    protected Vector getDomainList() throws java.sql.SQLException {
    	String cdRep = cbRepresentante.getValue();
    	if (!SessionLavenderePda.isUsuarioSupervisor()) {
    		cdRep = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
        return MetasPorFornService.getInstance().findMetasFornecedorList(cbPeriodo.getValue(), cdRep);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        MetasPorForn metasPorFornecedor = (MetasPorForn) domain;
        //--
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	String[] item = {
  	            StringUtil.getStringValue(metasPorFornecedor.rowKey),
  	            StringUtil.getStringValue(metasPorFornecedor.toString()),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.vlMeta),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.vlRealizado),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.getPctRealizadoMeta()),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.qtUnidadeMeta),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.qtCaixaMeta),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.qtMixProdutoMeta),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.qtMixClienteMeta)};
      	    return item;
        } else {
    	  	String[] item = {
  	            StringUtil.getStringValue(metasPorFornecedor.rowKey),
  	            StringUtil.getStringValue(metasPorFornecedor.toString()),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.vlMeta),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.vlRealizado),
  	            StringUtil.getStringValueToInterface(metasPorFornecedor.getPctRealizadoMeta())};
      	    return item;
        }
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

  //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
            cbRepresentante.setSelectedIndex(0);
            if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
            	cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
            }
    	} else {
        	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_PERIODO), cbPeriodo, getLeft(), AFTER + HEIGHT_GAP);
        //--
    	int[] widths = getWidths();
        //--
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
	        GridColDefinition[] gridColDefiniton = {
                new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
                new GridColDefinition(Messages.FORNECEDOR_NOME_ENTIDADE, widths[0] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_VLMETA, widths[1] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_VLRESULTADO, widths[2] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_PCTREALIZADO, widths[3] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_UNIDADE, widths[4] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_QTCAIXA, widths[5] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_MIXPRODUTO, widths[6] + WIDTH_GAP_BIG, LEFT),
                new GridColDefinition(Messages.METAS_MIXCLIENTE, widths[7] + WIDTH_GAP_BIG, LEFT)};
	        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        } else {
        	GridColDefinition[] gridColDefiniton = {
    			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
    			new GridColDefinition(Messages.FORNECEDOR_NOME_ENTIDADE, widths[0] + WIDTH_GAP_BIG, LEFT),
    			new GridColDefinition(Messages.METAS_VLMETA, widths[1] + WIDTH_GAP_BIG, LEFT),
    			new GridColDefinition(Messages.METAS_VLRESULTADO, widths[2] + WIDTH_GAP_BIG, LEFT),
    			new GridColDefinition(Messages.METAS_PCTREALIZADO, widths[3] + WIDTH_GAP_BIG, LEFT)};
        	gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        }
        gridEdit.disableSort = true;
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
    }
    
    private int[] getWidths() throws SQLException {
    	int[] widths = new int[8];
    	int[] biggerWidths = new int[8];
    	Vector listMetasForn = getDomainList();
    	int size = listMetasForn.size();
    	if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {    		
    		for(int i = 0; i < size; i++) {
    			MetasPorForn metasPorForn = (MetasPorForn) listMetasForn.items[i];
    			widths[0] = fm.stringWidth(StringUtil.getStringValue(metasPorForn.toString()));
    			biggerWidths[0] = widths[0] > biggerWidths[0] ? widths[0] : biggerWidths[0];
    			widths[1] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.vlMeta));
    			biggerWidths[1] = widths[1] > biggerWidths[1] ? widths[1] : biggerWidths[1];
    			widths[2] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.vlRealizado));
    			biggerWidths[2] = widths[2] > biggerWidths[2] ? widths[2] : biggerWidths[2];
    			widths[3] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.getPctRealizadoMeta()));
    			biggerWidths[3] = widths[3] > biggerWidths[3] ? widths[3] : biggerWidths[3];
    			widths[4] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.qtUnidadeMeta));
    			biggerWidths[4] = widths[4] > biggerWidths[4] ? widths[4] : biggerWidths[4];
    			widths[5] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.qtCaixaMeta));
    			biggerWidths[5] = widths[5] > biggerWidths[5] ? widths[5] : biggerWidths[5];
    			widths[6] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.qtMixProdutoMeta));
    			biggerWidths[6] = widths[6] > biggerWidths[6] ? widths[6] : biggerWidths[6];
    			widths[7] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.qtMixClienteMeta));
    			biggerWidths[7] = widths[7] > biggerWidths[7] ? widths[7] : biggerWidths[7];
    		}
    	} else {
    		for(int i = 0; i < size; i++) {
    			MetasPorForn metasPorForn = (MetasPorForn) listMetasForn.items[i];
    			widths[0] = fm.stringWidth(StringUtil.getStringValue(metasPorForn.toString()));
    			biggerWidths[0] = widths[0] > biggerWidths[0] ? widths[0] : biggerWidths[0];
    			widths[1] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.vlMeta));
    			biggerWidths[1] = widths[1] > biggerWidths[1] ? widths[1] : biggerWidths[1];
    			widths[2] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.vlRealizado));
    			biggerWidths[2] = widths[2] > biggerWidths[2] ? widths[2] : biggerWidths[2];
    			widths[3] = fm.stringWidth(StringUtil.getStringValueToInterface(metasPorForn.getPctRealizadoMeta()));
    			biggerWidths[3] = widths[3] > biggerWidths[3] ? widths[3] : biggerWidths[3];
    		}    		
    	}
    	return biggerWidths;
    }

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	bmOpcoes.addItem(FrameworkMessages.BOTAO_DETALHES);
    	bmOpcoes.addItem(Messages.MENU_UTILITARIO_CALCULADORA);
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

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if ((event.target == cbPeriodo) || (event.target == cbRepresentante)) {
					if ((event.target == cbRepresentante) && (ValueUtil.isNotEmpty(cbRepresentante.getValue()))) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
						cbPeriodo.reload(PeriodoComboBox.PERIODOMETASFORNECEDOR);
					}
					list();
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS : {
				if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(Messages.MENU_UTILITARIO_CALCULADORA)) {
						(new Calculator()).popup();
					} else if (bmOpcoes.selectedItem.equals(FrameworkMessages.BOTAO_DETALHES)) {
						detalhesClick();
					}
				}
			}
    	}
    }

}
