package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgProduto;
import br.com.wmw.lavenderepda.business.service.BonifCfgService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListBonifCfgProdutoForm extends LavendereCrudListForm {

	private final Vector bonifCfgProdutoList;
	public BonifCfgProduto bonifCfgProdutoSelecionado;

	public ListBonifCfgProdutoForm(Vector bonifCfgProdutoList) {
		super(Messages.TITULO_POLITICAS_BONIFICACAO_PRODUTOS);
		this.bonifCfgProdutoList = bonifCfgProdutoList;
		constructorListContainer();
	}

	private void constructorListContainer() {
		GridListContainer gridListContainer = new GridListContainer(2, 1);
		gridListContainer.setColPosition(1, LEFT);
		gridListContainer.setUseSortMenu(false);
		gridListContainer.setBarTopSimple();
		listContainer = gridListContainer;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return this.bonifCfgProdutoList;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		BonifCfgProduto bonifCfgProduto =  (BonifCfgProduto) domain;
		return new String[]{
				StringUtil.getStringValue(bonifCfgProduto.cdProduto), bonifCfgProduto.dsProduto
		};
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return BonifCfgService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new BonifCfg();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case PenEvent.PEN_UP: {
				if (event.target instanceof BaseListContainer.Item && ((BaseListContainer.Item) event.target).layout == listContainer.getLayout()) {
					BaseListContainer.Item c = (Item) listContainer.getSelectedItem();
					bonifCfgProdutoSelecionado = (BonifCfgProduto) c.domain;
				}
			}
			break;
		}
	}

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		c.domain = domain;
	}

}
