package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Praga;
import br.com.wmw.lavenderepda.business.service.PragaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ListPragaWindow extends LavendereWmwListWindow {
	
	protected ButtonPopup btLimpar;
	protected EditFiltro edFiltro;
	private String cdProduto;
	private String cdCultura;
	public Praga praga;
	public boolean clear;
	private boolean isObrigaCulturaPraga;

	public ListPragaWindow(String cdProduto, String cdCultura, boolean isObrigaCulturaPraga) {
		super(Messages.PRAGA_TITULO_WINDOW);
		btLimpar = new ButtonPopup("  " + FrameworkMessages.BOTAO_LIMPAR + "  ");
		edFiltro = new EditFiltro("999999999", 50);
		this.cdProduto = cdProduto;
		this.cdCultura = cdCultura;
		constructorListContainer();
		singleClickOn = true;
		this.isObrigaCulturaPraga = isObrigaCulturaPraga;
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return PragaService.getInstance().findAllPragaByAplicacaoProduto(domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Praga praga = (Praga) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(praga));
		item.addElement(StringUtil.getStringValue(praga.dsPraga));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PragaService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		Praga praga = new Praga();
		praga.cdProduto = cdProduto;
		praga.cdCultura = cdCultura;
		praga.dsFiltro = edFiltro.getText();
		return praga;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.LABEL_CDPRAGA), edFiltro, getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		if (!isObrigaCulturaPraga) {
			addButtonPopup(btLimpar);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case EditIconEvent.PRESSED: {
			if (event.target == edFiltro) {
				list();
			}
			break;
		}
		case ControlEvent.PRESSED:
			if (event.target == btLimpar) {
				clear = true;
				unpop();
			}
			break;
		case KeyEvent.SPECIAL_KEY_PRESS: 
			if (event.target == edFiltro && ((KeyEvent)event).isActionKey()) {
				list();
				if (listContainer != null && listContainer.size() == 0) {
					edFiltro.requestFocus();
				}
			} 
			break;
		}
	}
	
	@Override
	public void singleClickInList() throws SQLException {
		Praga pragaFilter = (Praga) getCrudService().findByRowKey(getSelectedRowKey());
		if (pragaFilter != null) {
			praga = pragaFilter;
			unpop();
		}
	}
	
	private void constructorListContainer() {
    	configListContainer("CDPRAGA");
		listContainer = new GridListContainer(2, 1);
		String[][] matriz = new String[2][2];
		matriz[0][0] = Messages.PRAGA_LABEL_CODIGO;
		matriz[0][1] = "CDPRAGA";
		matriz[1][0] = Messages.PRAGA_LABEL_DESCRICAO;
		matriz[1][1] = "DSNOMECIENTPRAGA";
		listContainer.setColsSort(matriz);
		for (int i = 0; i < 2; i++) {
			listContainer.setColPosition(i, LEFT);
		}
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
	}

}
