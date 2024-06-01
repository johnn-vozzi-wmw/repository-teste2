package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CatalogoExterno;
import br.com.wmw.lavenderepda.business.service.CatalogoExternoService;
import totalcross.sys.Vm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CatalogoExternoWindow extends WmwListWindow {

	private static final String SYSTEM_VIEWER = "viewer";

	public CatalogoExternoWindow() {
		super(Messages.CATALOGO_EXTERNO);
		singleClickOn = true;
		listContainer = new GridListContainer(1, 1);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return CatalogoExternoService.getInstance().findAllCatalogosBaixados();
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		CatalogoExterno catalogoExterno = (CatalogoExterno) domain;
		String[] item = {StringUtil.getStringValue(catalogoExterno.toString())};
		return item;
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return CatalogoExternoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		CatalogoExterno domainFilter = new CatalogoExterno();
		return domainFilter;
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, getTop() + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

	@Override
	public void singleClickInList() throws SQLException {
		super.singleClickInList();
		if (listContainer.getSelectedIndex() != -1) {
			CatalogoExterno catalogoExterno = (CatalogoExterno) getCrudService().findByRowKey(listContainer.getSelectedId());
			if (catalogoExterno != null) {
				abreArquivoSelecionado(catalogoExterno.nmArquivo);
				fecharWindow();
			}
		}
	}

	private void abreArquivoSelecionado(String nmArquivo) {
		String filePath = CatalogoExterno.getPathCatalogoExterno() + nmArquivo;
		if (VmUtil.isSimulador()) {
			filePath =  "file:///" + filePath.replace(" ", "%20");
		}
		Vm.exec(SYSTEM_VIEWER, filePath, 0, true);
	}

}
