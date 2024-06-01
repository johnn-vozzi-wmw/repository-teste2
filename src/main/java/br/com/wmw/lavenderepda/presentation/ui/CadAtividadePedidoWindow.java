package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtividadePedido;

public class CadAtividadePedidoWindow extends WmwWindow {
	
	public CadAtividadePedidoForm cadAtividadePedidoForm;

	public CadAtividadePedidoWindow(AtividadePedido atividadePedido) throws SQLException {
		super(Messages.ATIVIDADEPEDIDO_DETALHES);
		cadAtividadePedidoForm = new CadAtividadePedidoForm(atividadePedido);
		cadAtividadePedidoForm.edit(atividadePedido);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadAtividadePedidoForm , LEFT , TOP , FILL , FILL);
	}
	
}
