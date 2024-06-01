package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;

public class ListSolAutorizacaoWindow extends WmwWindow {

	public ListSolAutorizacaoForm listSolAutorizacaoForm;
	public Pedido pedido;
	public ItemPedido itemPedido;

	public ListSolAutorizacaoWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		super(itemPedido != null ? Messages.SOL_AUTORIZACAO_ITEM_PEDIDO : Messages.SOL_AUTORIZACAO_PEDIDO);
		listSolAutorizacaoForm = new ListSolAutorizacaoForm(pedido, itemPedido, false);
		listSolAutorizacaoForm.setID("listSolAutorizacaoForm");
		scrollable = false;
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, listSolAutorizacaoForm, LEFT, getTop(), FILL, FILL - footerH);
		listSolAutorizacaoForm.showRequestedFocus();
	}
	
}