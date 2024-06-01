package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseGrid;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.NotaFiscalService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;


public class ListNotaFiscalForm extends LavendereCrudListForm {

	private Pedido pedido;
	private boolean fromWindow;

	public ListNotaFiscalForm(Pedido pedido, boolean fromWindow) {
		super(Messages.MENU_OPCAO_PEDIDO);
		this.pedido = pedido;
		this.fromWindow = fromWindow;
		constructorListContainer();
	}

	private void constructorListContainer() {
		configListContainer("DTEMISSAO");
		listContainer = new GridListContainer(4, 2);
		listContainer.resizeable = false;
		listContainer.btResize.setVisible(false);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColsSort(new String[][]{{Messages.PEDIDO_LABEL_LISTNOTALFISCAL_NUNOTAFISCAL, "NUNOTAFISCAL"} , {Messages.PEDIDO_LABEL_LISTNOTAFISCAL_DTEMISSAO, "DTEMISSAO"}});
		singleClickOn = this.fromWindow;
	}

	protected String[] getItem(Object domain) {
		NotaFiscal notaFiscal = (NotaFiscal) domain;
		return new String[] {
				StringUtil.getStringValue(notaFiscal.cdSerie) + "/" + StringUtil.getStringValue(notaFiscal.nuNotaFiscal),
				StringUtil.getStringValue(notaFiscal.dtEmissao),
				"",
				Messages.MOEDA + " " + StringUtil.getStringValueToInterface(notaFiscal.vlNotaFiscal)};
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException { return new NotaFiscal(pedido.cdEmpresa, pedido.cdRepresentante, pedido.nuPedido); }

	@Override
	protected CrudService getCrudService() throws SQLException { return NotaFiscalService.getInstance(); }

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case GridEvent.SELECTED_EVENT: {
				if (event.target instanceof BaseGrid) {
					listContainer.setEventoClickUnicoDisparado(true);
				}
			}
		}
	}

	@Override
	public void detalhesClick() throws SQLException {
		if (this.fromWindow) {
			NotaFiscal notaFiscal = (NotaFiscal) NotaFiscalService.getInstance().findByRowKeyDyn(getSelectedRowKey());
			new CadNotaFiscalDynWindow(notaFiscal).popup();
		} else {
			super.detalhesClick();
		}
	}

}