package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServResp;
import br.com.wmw.lavenderepda.business.service.RequisicaoServRespService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListRequisicaoServRespForm extends LavendereCrudListForm  {
	
	private RequisicaoServ requisicaoServ;

	public ListRequisicaoServRespForm(RequisicaoServ requisicaoServ) {
		super(" ");
		this.requisicaoServ = requisicaoServ;
		constructorListContainer();
		singleClickOn = true;
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return requisicaoServ;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return RequisicaoServRespService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
	
	@Override
	protected Vector getDomainList(BaseDomain requisicaoServ) throws SQLException {
		return RequisicaoServRespService.getInstance().findRespostasRequisicao(requisicaoServ);
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		RequisicaoServResp requisicaoServResp = (RequisicaoServResp) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(requisicaoServResp.cdRequisicaoServResp));
		item.addElement(StringUtil.getStringValue(requisicaoServResp.motivoServ.getDsStatusRequisicao()));
		item.addElement(StringUtil.getStringValue(requisicaoServResp.motivoServ.toString()));
		item.addElement(StringUtil.getStringValue(requisicaoServResp.dtRequisicaoServResp) + " - " + StringUtil.getStringValue(requisicaoServResp.hrRequisicaoServResp).substring(0, 5));
		return (String[]) item.toObjectArray();
	}
	
    protected void constructorListContainer() {
    	configListContainer("CDREQUISICAOSERVRESP");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(0, LEFT);
		listContainer.setColPosition(2, LEFT);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setBarTopSimple();
    }
    
    @Override
    public void singleClickInList() throws SQLException {
    	CadRequisicaoServRespWindow cadRequisicaoResp = new CadRequisicaoServRespWindow((RequisicaoServResp)getSelectedDomain());
    	cadRequisicaoResp.popup();
    }

}
