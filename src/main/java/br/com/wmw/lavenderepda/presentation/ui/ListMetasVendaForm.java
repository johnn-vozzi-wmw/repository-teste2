package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Meta;
import br.com.wmw.lavenderepda.business.service.MetaAcompanhamentoService;
import br.com.wmw.lavenderepda.business.service.MetaService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaPdbxDao;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListMetasVendaForm extends BaseCrudListForm {

	private LabelValue lbQtPedidos;
	private LabelValue lbTotalVendas;
	private ButtonOptions bmOpcoes;
	private int qtPedidosRealizadoTotal = 0;
	private double vlRealizadoMetaTotal = 0;

    public ListMetasVendaForm() throws SQLException {
        super(Messages.METAS_VENDA_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadMetasVendaForm());
        singleClickOn = true;
        lbQtPedidos = new LabelValue("99999999");
        bmOpcoes = new ButtonOptions();
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetaService.getInstance();
    }

    public void list() throws SQLException {
    	super.list();
    	updateTotalizadores();
    }

    private void updateTotalizadores() throws SQLException {
    	int qtPedidosTotal = 0;
    	double vlTotalPedidos = 0d;
    	if (LavenderePdaConfig.usaSistemaModoOffLine) {
    		qtPedidosTotal = qtPedidosRealizadoTotal;
    		vlTotalPedidos = vlRealizadoMetaTotal;
    	} else {
    		Meta metaFilter = new Meta();
    		metaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		qtPedidosTotal = ValueUtil.getIntegerValue(getCrudService().sumByExample(metaFilter, "qtPedidos"));
    		vlTotalPedidos = getCrudService().sumByExample(metaFilter, "vlRealizadoVendas");
    	}
    	lbQtPedidos.setValue(qtPedidosTotal);
    	lbQtPedidos.setVisible(true);
    	lbTotalVendas.setValue(vlTotalPedidos);
    	lbTotalVendas.setVisible(true);
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new Meta();
    }

    protected Vector getDomainList() throws java.sql.SQLException {
    	Vector list = MetaPdbxDao.getInstance().findAllMetasGroupByRepresentante(SessionLavenderePda.cdEmpresa);
        return list;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        Meta meta = (Meta) domain;
        if (LavenderePdaConfig.usaSistemaModoOffLine) {
    		MetaAcompanhamentoService.getInstance().getMetaAcompanhamentoList(meta);
    		MetaService.getInstance().recalculaMetaListSup(meta , DateUtil.getCurrentDate());
    		vlRealizadoMetaTotal += meta.vlRealizadoVendas;
    		qtPedidosRealizadoTotal += meta.qtPedidos;
        }
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	String[] item = {
        			StringUtil.getStringValue(meta.cdRepresentante),
        			StringUtil.getStringValue(meta.nmRepresentante),
        			StringUtil.getStringValueToInterface(meta.qtPedidos),
        			StringUtil.getStringValueToInterface(meta.vlMetaVendas),
        			StringUtil.getStringValueToInterface(meta.vlRealizadoVendas),
        			StringUtil.getStringValueToInterface(meta.getPctRealizadoMeta()),
      	            StringUtil.getStringValueToInterface(meta.qtUnidadeMeta),
      	            StringUtil.getStringValueToInterface(meta.qtCaixaMeta),
      	            StringUtil.getStringValueToInterface(meta.qtMixProdutoMeta),
      	            StringUtil.getStringValueToInterface(meta.qtMixClienteMeta)};
        	return item;
        } else {
        	String[] item = {
        			StringUtil.getStringValue(meta.cdRepresentante),
        			StringUtil.getStringValue(meta.nmRepresentante),
        			StringUtil.getStringValueToInterface(meta.qtPedidos),
        			StringUtil.getStringValueToInterface(meta.vlMetaVendas),
        			StringUtil.getStringValueToInterface(meta.vlRealizadoVendas),
        			StringUtil.getStringValueToInterface(meta.getPctRealizadoMeta())};
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
    	String labelPedidos = Messages.PEDIDO_LABEL_PEDIDOS;
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
        	int ww = fm.stringWidth("www");
        	GridColDefinition[] gridColDefiniton = {
    			new GridColDefinition(Messages.REPRESENTANTE_LABEL_CDREPRESENTANTE, ww, LEFT),
    			new GridColDefinition(Messages.REPRESENTANTE_NOME_ENTIDADE, ww * 4, LEFT),
    			new GridColDefinition(labelPedidos, ww * 2, RIGHT),
    			new GridColDefinition(Messages.METAS_VLMETA, ww * 3, RIGHT),
    			new GridColDefinition(Messages.PRODUTIVIDADE_LABEL_VLREALIZADOVENDAS, ww * 3, RIGHT),
    			new GridColDefinition(Messages.METAS_PCTREALIZADO, ww * 2, RIGHT),
                new GridColDefinition(Messages.METAS_UNIDADE, ww, LEFT),
                new GridColDefinition(Messages.METAS_QTCAIXA, ww, LEFT),
                new GridColDefinition(Messages.METAS_MIXPRODUTO, ww, LEFT),
                new GridColDefinition(Messages.METAS_MIXCLIENTE, ww, LEFT)};
        	gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        } else {
        	int ww = fm.stringWidth("www");
        	GridColDefinition[] gridColDefiniton = {
    			new GridColDefinition(Messages.REPRESENTANTE_LABEL_CDREPRESENTANTE, ww, LEFT),
    			new GridColDefinition(Messages.REPRESENTANTE_NOME_ENTIDADE, ww * 4, LEFT),
    			new GridColDefinition(labelPedidos, ww * 2, RIGHT),
    			new GridColDefinition(Messages.METAS_VLMETA, ww * 3, RIGHT),
    			new GridColDefinition(Messages.PRODUTIVIDADE_LABEL_VLREALIZADOVENDAS, ww * 3, RIGHT),
    			new GridColDefinition(Messages.METAS_PCTREALIZADO, ww * 2, RIGHT)};
        	gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        }
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, getTop(), FILL, FILL - (barBottomContainer.getHeight() + lbQtPedidos.getPreferredHeight()));
        UiUtil.add(this, new LabelName(labelPedidos), LEFT + WIDTH_GAP, AFTER, PREFERRED, UiUtil.getLabelPreferredHeight());
        UiUtil.add(this, lbQtPedidos, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
        UiUtil.add(this, lbTotalVendas = new LabelValue("999999999"), RIGHT - WIDTH_GAP, SAME, PREFERRED, PREFERRED);
        UiUtil.add(this, new LabelName(Messages.PRODUTIVIDADE_LABEL_TOTALVENDAS), BEFORE - WIDTH_GAP_BIG, SAME, PREFERRED, SAME);
        lbQtPedidos.setVisible(false);
        lbTotalVendas.setVisible(false);
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
    }

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	bmOpcoes.addItem(Messages.MENU_UTILITARIO_CALCULADORA);
    }

    //@Override
    public void detalhesClick() throws SQLException {
		if (gridEdit.getSelectedIndex() >= 0) {
			String cdRepresentante = gridEdit.getSelectedItem()[0];
			show(new ListMetasVendaRepForm(cdRepresentante));
		}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
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

}
