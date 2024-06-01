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
import br.com.wmw.lavenderepda.business.domain.Cultura;
import br.com.wmw.lavenderepda.business.service.CulturaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ListCulturaWindow extends LavendereWmwListWindow {
	
	public Cultura cultura;
	protected ButtonPopup btLimpar;
	protected EditFiltro edFiltro;
	private String cdProduto;
	public boolean clear;
	private boolean isObrigaCulturaPraga;
	
	public ListCulturaWindow(String cdProduto, boolean isObrigaCulturaPraga) {
		super(Messages.CULTURA_TITULO_WINDOW);
		constructorListContainer();
		btLimpar = new ButtonPopup("  " + FrameworkMessages.BOTAO_LIMPAR + "  ");
		edFiltro = new EditFiltro("999999999", 50);
		this.cdProduto = cdProduto;
		singleClickOn = true;
		this.isObrigaCulturaPraga = isObrigaCulturaPraga;
		
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return CulturaService.getInstance().findAllCulturaByAplicacaoProduto(domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Cultura cultura = (Cultura) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(cultura));
		item.addElement(StringUtil.getStringValue(cultura.dsNomeCientCultura));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return CulturaService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		Cultura cultura = new Cultura();
		cultura.cdProduto = cdProduto;
		cultura.dsFiltro = edFiltro.getText();
		return cultura;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.LABEL_CDCULTURA), edFiltro, getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		if (!isObrigaCulturaPraga) {
			addButtonPopup(btLimpar);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case EditIconEvent.PRESSED:
			if (event.target == edFiltro) {
				list();
			}
			break;
		case ControlEvent.PRESSED:
			if (event.target == btLimpar) {
				btLimparClick();
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

	private void btLimparClick() {
		clear = true;
		unpop();
	}
	
	private void constructorListContainer() {
    	configListContainer("CDCULTURA");
		listContainer = new GridListContainer(2, 1);
		String[][] matriz = new String[2][2];
		matriz[0][0] = Messages.CULTURA_LABEL_CODIGO;
		matriz[0][1] = "CDCULTURA";
		matriz[1][0] = Messages.CULTURA_LABEL_DESCRICAO;
		matriz[1][1] = "DSCULTURA";
		listContainer.setColsSort(matriz);
		for (int i = 0; i < 2; i++) {
			listContainer.setColPosition(i, LEFT);
		}
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
	}
	
	public void singleClickInList() throws SQLException {
		Cultura culturaFilter = (Cultura) getCrudService().findByRowKey(getSelectedRowKey());
		if (culturaFilter != null) {
			cultura = culturaFilter;
			unpop();
		}
	}
	
}
