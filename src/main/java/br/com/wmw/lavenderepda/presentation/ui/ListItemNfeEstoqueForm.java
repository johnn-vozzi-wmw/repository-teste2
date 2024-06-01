package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemNfeEstoque;
import br.com.wmw.lavenderepda.business.domain.NfeEstoque;
import br.com.wmw.lavenderepda.business.service.ItemNfeEstoqueService;
import totalcross.ui.event.Event;

public class ListItemNfeEstoqueForm extends BaseCrudListForm {

	private NfeEstoque nfeEstoque;
	
	public ListItemNfeEstoqueForm(NfeEstoque nfeEstoque) {
		super(Messages.REMESSAESTOQUE_ITENS);
		listContainer = new GridListContainer(2, 1);
		this.nfeEstoque = nfeEstoque;
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		ItemNfeEstoque itemNfeEstoque = new ItemNfeEstoque();
		itemNfeEstoque.cdEmpresa = nfeEstoque.cdEmpresa;
		itemNfeEstoque.cdRepresentante = nfeEstoque.cdRepresentante;
		itemNfeEstoque.nuNotaRemessa = nfeEstoque.nuNotaRemessa;
		itemNfeEstoque.nuSerieRemessa = nfeEstoque.nuSerieRemessa;
		return itemNfeEstoque;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemNfeEstoqueService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemNfeEstoque itemNfeEstoque = (ItemNfeEstoque) domain;
		return new String[] {StringUtil.getStringValue(itemNfeEstoque), 
				Messages.PRODUTO_INFO_LABEL_QTD + " " + StringUtil.getStringValueToInterface(itemNfeEstoque.qtItem)};
	}
}
