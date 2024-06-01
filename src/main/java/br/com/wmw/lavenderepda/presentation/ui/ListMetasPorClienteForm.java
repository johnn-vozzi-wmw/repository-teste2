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
import br.com.wmw.lavenderepda.business.domain.MetasPorCliente;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.MetasPorClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.PeriodoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListMetasPorClienteForm extends BaseCrudListForm {

	private PeriodoComboBox cbPeriodo;
	private RepresentanteSupervComboBox cbRepresentante;
	private ButtonOptions bmOpcoes;

    public ListMetasPorClienteForm() throws SQLException {
        super(Messages.METASPORCLIENTE_NOME_ENTIDADE);
        setBaseCrudCadForm(new CadMetasPorClienteForm());
        singleClickOn = true;
        cbPeriodo = new PeriodoComboBox(PeriodoComboBox.PERIODOMETASCLIENTE);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        bmOpcoes = new ButtonOptions();
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetasPorClienteService.getInstance();
    }

    protected String getDataSource() throws SQLException {
    	return MetasPorClienteService.getInstance().getMetasClienteList(cbPeriodo.getValue(), cbRepresentante.getValue());
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new MetasPorCliente();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
    	Vector list = new Vector();
    	String cdRep = cbRepresentante.getValue();
    	if (!SessionLavenderePda.isUsuarioSupervisor()) {
    		cdRep = SessionLavenderePda.getRepresentante().cdRepresentante;
    	}
        list = MetasPorClienteService.getInstance().findMetasClienteList(cbPeriodo.getValue(), cdRep);
        return list;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	MetasPorCliente metasPorCliente = (MetasPorCliente) domain;
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	  String[] item = {
      	            StringUtil.getStringValue(metasPorCliente.rowKey),
      	            StringUtil.getStringValue(metasPorCliente.cdCliente),
      	            StringUtil.getStringValue(metasPorCliente.dsCliente),
      	            StringUtil.getStringValueToInterface(metasPorCliente.vlMeta),
      	            StringUtil.getStringValueToInterface(metasPorCliente.vlRealizado),
      	            StringUtil.getStringValueToInterface(metasPorCliente.getPctRealizadoMeta()),
      	            StringUtil.getStringValueToInterface(metasPorCliente.qtUnidadeMeta),
      	            StringUtil.getStringValueToInterface(metasPorCliente.qtCaixaMeta),
      	            StringUtil.getStringValueToInterface(metasPorCliente.qtMixProdutoMeta)};
      	    return item;
        } else {
        	  String[] item = {
      	            StringUtil.getStringValue(metasPorCliente.rowKey),
      	            StringUtil.getStringValue(metasPorCliente.cdCliente),
      	            StringUtil.getStringValue(metasPorCliente.dsCliente),
      	            StringUtil.getStringValueToInterface(metasPorCliente.vlMeta),
      	            StringUtil.getStringValueToInterface(metasPorCliente.vlRealizado),
      	            StringUtil.getStringValueToInterface(metasPorCliente.getPctRealizadoMeta())};
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
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	} else {
        	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_PERIODO), cbPeriodo, getLeft(), AFTER + HEIGHT_GAP);
        //--
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
	        int ww = fm.stringWidth("ww");
	        GridColDefinition[] gridColDefiniton = {
	                new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
	                new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, ww * 3, LEFT),
                new GridColDefinition(Messages.CLIENTE_NOME_ENTIDADE, ww * 8, LEFT),
	                new GridColDefinition(Messages.METAS_VLMETA, ww * 4, LEFT),
	                new GridColDefinition(Messages.METAS_VLRESULTADO, ww * 4, LEFT),
	                new GridColDefinition(Messages.METAS_PCTREALIZADO, ww * 3, LEFT),
	                new GridColDefinition(Messages.METAS_UNIDADE, ww * 3, LEFT),
	                new GridColDefinition(Messages.METAS_QTCAIXA, ww * 3, LEFT),
	                new GridColDefinition(Messages.METAS_MIXPRODUTO, ww * 3, LEFT)};
	        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        } else {
        	int ww = fm.stringWidth("ww");
        	GridColDefinition[] gridColDefiniton = {
        			new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
	                new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, ww * 3, LEFT),
    			new GridColDefinition(Messages.CLIENTE_NOME_ENTIDADE, ww * 8, LEFT),
        			new GridColDefinition(Messages.METAS_VLMETA, ww * 4, LEFT),
        			new GridColDefinition(Messages.METAS_VLRESULTADO, ww * 4, LEFT),
        			new GridColDefinition(Messages.METAS_PCTREALIZADO, ww * 3, LEFT)};
        	gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        }
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
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

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	bmOpcoes.addItem(FrameworkMessages.BOTAO_DETALHES);
    	bmOpcoes.addItem(Messages.MENU_UTILITARIO_CALCULADORA);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if ((event.target == cbPeriodo) || (event.target == cbRepresentante)) {
					if ((event.target == cbRepresentante) && (ValueUtil.isNotEmpty(cbRepresentante.getValue()))) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
						cbPeriodo.reload(PeriodoComboBox.PERIODOMETASCLIENTE);
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
