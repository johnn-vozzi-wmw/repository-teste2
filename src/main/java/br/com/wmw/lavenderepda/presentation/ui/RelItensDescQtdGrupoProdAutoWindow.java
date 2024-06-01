package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;

public class RelItensDescQtdGrupoProdAutoWindow extends WmwWindow {

	private ListItemPedidoForm listItemPedidoForm;

	public RelItensDescQtdGrupoProdAutoWindow(String title) {
		super(title);
	}

	public RelItensDescQtdGrupoProdAutoWindow(CadPedidoForm cadPedidoForm, Pedido pedido, String title) throws SQLException {
		super(title);
		listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByDescontoGrupoAuto(true);
		listItemPedidoForm.inWindowMode = true;
		listItemPedidoForm.getListContainer().setColTotalizerRight(3, Messages.ITEMPEDIDO_LABEL_VLTOTALITENSPEDIDOS);
		//--
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, listItemPedidoForm, LEFT, getTop(), FILL, FILL);
	}

	public void fecharWindow() throws SQLException {
		listItemPedidoForm.onFormClose();
		super.fecharWindow();
	}

}
