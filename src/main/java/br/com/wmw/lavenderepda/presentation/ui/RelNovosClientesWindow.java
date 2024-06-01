package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelNovosClientesWindow extends WmwListWindow {

	private LabelValue lbDescricao1;
	private LabelValue lbDescricao2;
	private Vector domainList;

	public RelNovosClientesWindow() throws SQLException {
		super(Messages.NOVOCLIENTE_NOVOS_CLIENTES);
		lbDescricao1 = new LabelValue(Messages.NOVOCLIENTE_MSG_NOVOCADASTRO_PART1, CENTER);
		lbDescricao2 = new LabelValue(Messages.NOVOCLIENTE_MSG_NOVOCADASTRO_PART2, CENTER);
		listContainer = new GridListContainer(2, 1);
		listContainer.setBarTopSimple();
		domainList = NovoClienteService.getInstance().deleteNovoClientePdaByClienteErp();
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return domainList;
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		Cliente cliente = (Cliente) domain;
		Vector item = new Vector(2);
		item.addElement(cliente.toString());
		item.addElement(StringUtil.getStringValue(cliente.nuCnpj));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return NovoClienteService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new NovoCliEndereco();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, lbDescricao1, CENTER + WIDTH_GAP, TOP);
		UiUtil.add(this, lbDescricao2, CENTER + WIDTH_GAP, AFTER);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
	
	public void tryPopup() {
		if (ValueUtil.isNotEmpty(domainList)) {
			popup();
		}
	}

}
