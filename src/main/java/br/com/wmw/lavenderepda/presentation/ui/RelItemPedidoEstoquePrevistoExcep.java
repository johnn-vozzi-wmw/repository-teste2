package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import totalcross.ui.Control;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelItemPedidoEstoquePrevistoExcep extends WmwListWindow {

	private Vector domainList;
	
	public RelItemPedidoEstoquePrevistoExcep(Vector domainList) {
		super(Messages.TITULO_ITENS_SEM_EST_CONT);
		this.domainList = domainList;
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return domainList;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		String[] itemErro = new String[1];
		itemErro[0] = (String) domain;
		return itemErro;
	}



	@Override
	protected void onFormStart() throws SQLException {
		int width = getWidthColumnGrid();
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.TITULO_ITENS, width + WIDTH_GAP_BIG, Control.LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton);
		UiUtil.add(this, grid);
		grid.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	private int getWidthColumnGrid() {
		int size = domainList.size();
		int width = 0;
		int biggerWidth = 0;
		for(int i = 0; i < size; i++) {
			String dsProdutoErro = (String)domainList.items[i];
			width = fm.stringWidth(StringUtil.getStringValue(dsProdutoErro));
			biggerWidth = width > biggerWidth ? width : biggerWidth;
		}
		return biggerWidth;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {return null;}
	@Override
	protected CrudService getCrudService() throws SQLException {return null;}
	@Override
	protected BaseDomain getDomainFilter() {return new Estoque();}
	@Override
	protected void onFormEvent(Event event) throws SQLException {}
}
