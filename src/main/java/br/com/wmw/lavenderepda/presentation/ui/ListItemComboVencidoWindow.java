package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemComboService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItemComboVencidoWindow extends WmwListWindow {
	
	private Pedido pedido;
	private ButtonPopup btExcluir;

	public ListItemComboVencidoWindow(Pedido pedido) {
		super(Messages.LIST_COMBO_VENCIDA_TITULO);
		this.pedido = pedido;
		constructorListContainer();
		btExcluir = new ButtonPopup(FrameworkMessages.BOTAO_EXCLUIR);
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(3, RIGHT);
		listContainer.resizeable = false;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return ItemComboService.getInstance().findItemComboVencidoList(pedido);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemCombo itemCombo = (ItemCombo) domain;
		return new String[] {itemCombo.cdProduto + " - ", 
							 itemCombo.dsProduto,
							 itemCombo.combo.toString(),
							 getDsTipoItemCombo(itemCombo.flTipoItemCombo)};
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemComboService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemCombo();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
		addButtonPopup(btExcluir);
		addBtFechar();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btExcluir) {
				btExcluirClick();
			}
			break;
		}

	}
	
	private String getDsTipoItemCombo(String flTipoItemCombo) {
		if (ItemCombo.TIPOITEMCOMBO_SECUNDARIO.equals(flTipoItemCombo)) {
			return Messages.ITEMCOMBO_SECUNDARIO;
		}
		return Messages.ITEMCOMBO_PRIMARIO;
	}
	
	private void btExcluirClick() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		try {
			ItemPedidoService.getInstance().deleteItensComboVencidos(pedido, getDomainList(null));
			UiUtil.showSucessMessage(Messages.MSG_COMBO_VENCIDA_EXCLUIDA);
			unpop();
		} finally {
			msg.unpop();
		}
	}

}
