package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FreteCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwComboBoxListWindow;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListFreteDetalhesWindow extends LavendereWmwComboBoxListWindow {

	public FreteConfig freteConfig;
	public FreteCalculo selectedFreteCalculo;
	public Vector listFreteCalculoFormaFrete;
	
    public ListFreteDetalhesWindow(FreteConfig freteConfig) throws SQLException {
        super(freteConfig.nmTransportadoraDsFreteConfig);
        constructorListContainer();
        singleClickOn = false;
        
        this.freteConfig = freteConfig;
        setDefaultRect();
    }

    public void constructorListContainer() {
    	configListContainer("NUORDEMCALCULO");
    	listContainer = new GridListContainer(4, 2);
    	listContainer.setColPosition(1, RIGHT);
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColsSort(new String[][] {{"Sequência", "NUORDEMCALCULO"}});
    	listContainer.setColTotalizerRight(3, "Valor Total");
    	listContainer.btResize.setVisible(false);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return null;
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	return listFreteCalculoFormaFrete = this.freteConfig.listFreteCalculoFormaFrete != null ? this.freteConfig.listFreteCalculoFormaFrete : new Vector();
    }
    
	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		FreteCalculo freteCalculo = (FreteCalculo) domain;
		return new String[] {
			StringUtil.getStringValue(freteCalculo.freteEvento),
			ValueUtil.VALOR_NI,
			ValueUtil.VALOR_NI,
			StringUtil.getStringValueToInterface(freteCalculo.valorCalculado),
		};
	}

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer);
        listContainer.setRect(LEFT, getTop(), FILL, FILL);
    }

    //@Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	return new FreteCalculo();
    }

    @Override
    public void singleClickInList() throws SQLException {
    	searchSelectedRowKeyInList();
    	UiUtil.showInfoMessage("Click: " + selectedFreteCalculo.getRowKey());
    }
    
    private void searchSelectedRowKeyInList() throws SQLException {
    	String selectedRowKey = getSelectedRowKey();
    	int size = listFreteCalculoFormaFrete.size();
    	for (int i = 0; i < size; i++) {
    		FreteCalculo freteConfigListItem = (FreteCalculo) listFreteCalculoFormaFrete.items[i];
    		if (freteConfigListItem.getRowKey().equalsIgnoreCase(selectedRowKey)) {
    			selectedFreteCalculo = freteConfigListItem;
    			break;
    		}
    	}
		
	}

	//@Override
    protected void onFormEvent(Event event) throws SQLException {}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}
	
	@Override
	protected BaseDomain getDomainFilter() {
		return new FreteConfig();
	}

}