package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Licao;
import br.com.wmw.lavenderepda.business.service.LicaoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListTreinamentoForm extends LavendereCrudListForm {
	

	public ListTreinamentoForm() throws SQLException {
		super("Informações do treinamento");
		constructorListContainer();
		setBaseCrudCadForm(new CadTreinamentoForm());
		singleClickOn = true;
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(1, 1);
		configListContainer(ListContainerConfig.getDefautSortColumn(getConfigClassName()));
		listContainer.setColsSort(new String[][] {{Messages.LABEL_LICAO_TITULO, "TITULO"}});
	}
	
	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = new Licao();
		licao.titulo = edFiltro.getValue();
		return new Licao();
	}
	
	@Override
	protected void filtrarClick() throws SQLException {
		// TODO Auto-generated method stub
		list();
	}
	
	
	
	@Override
	protected CrudService getCrudService() throws SQLException {
		// TODO Auto-generated method stub
		return LicaoService.getInstance();
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, edFiltro, LEFT + 10, getTop(), FILL - 10);
		UiUtil.add(this, listContainer, LEFT, AFTER, FILL, FILL - barBottomContainer.getHeight());
		UiUtil.add(barBottomContainer, btNovo, 5);
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = (Licao) domain;
		Vector item = new Vector();
		item.addElement(licao.titulo);
		return (String[]) item.toObjectArray();
	}
	
	@Override
	public void setItems(String[][] items) {
		// TODO Auto-generated method stub
		super.setItems(items);
	}
	

	@Override
	public void onFormClose() throws SQLException {
		// TODO Auto-generated method stub
		super.onFormClose();
	}
	
}
