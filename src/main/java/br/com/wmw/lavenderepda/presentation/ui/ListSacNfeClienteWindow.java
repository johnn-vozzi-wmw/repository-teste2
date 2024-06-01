package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.NfeService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListSacNfeClienteWindow extends WmwListWindow {
	
	private Sac sac;
	public Vector listNfeCliente;
	public int nuNfe;
	public String dsSerie;

    public ListSacNfeClienteWindow(Sac sacSelecionado) throws SQLException {
        super(Messages.NOTA_LABEL_NOTAS);
        singleClickOn = true;
        listContainer = new GridListContainer(5, 3);
        listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
        this.sac = sacSelecionado;
        sortAsc = "N, N";
        sortAtributte = "DTEMISSAO";
        listNfeCliente = new Vector();
        setDefaultRect();
    }
    
    //@Override
    protected CrudService getCrudService() throws SQLException {
        return NfeService.getInstance(); 
    }
    
    @Override
    protected BaseDomain getDomainFilter() {
    	Nfe domainFilter = new Nfe();
    	domainFilter.cdCliente = sac.cdCliente;
    	domainFilter.cdEmpresa = sac.cdEmpresa;
    	domainFilter.cdRepresentante = sac.cdRepresentante;
    	return domainFilter;
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Nfe nfe = (Nfe) domain;
    	nfe.cdEmpresa = sac.cdEmpresa;
    	nfe.cdRepresentante = sac.cdRepresentante;
    	nfe.cdCliente = sac.cdCliente;
        return listNfeCliente = getCrudService().findAllByExample(nfe);
    }
    
    //@Override
	protected String[] getItem(Object domain) throws SQLException {
		Nfe nfe = (Nfe) domain;
		return new String[] { 

				StringUtil.getStringValue("" + nfe.nuNfe),
				StringUtil.getStringValue(" - " + nfe.dsSerieNfe),
				StringUtil.getStringValue(" - " + nfe.dtEmissao),
				StringUtil.getStringValue("" + nfe.nuPedido),
				StringUtil.getStringValue(" - " + Messages.MOEDA + " " + StringUtil.getStringValueToInterface(nfe.vlTotalNfe)),
				};
	}

	//@Override
    protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop() + HEIGHT_GAP, FILL, FILL);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
    
    @Override
    public void singleClickInList() throws SQLException {
    	Nfe domain = (Nfe) getCrudService().findByRowKey(getSelectedRowKey());
   		nuNfe = domain.nuNfe;
   		dsSerie = domain.dsSerieNfe;
   		btFecharClick();
    }
    
	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.domain;
	}
	
	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

}
