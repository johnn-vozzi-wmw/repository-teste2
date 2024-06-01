package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;

public class ListBonifCfgWindow extends WmwWindow {
	
	ListBonifCfgPedidoForm listBonifCfgPedidoForm;

	public ListBonifCfgWindow(Pedido pedido) throws SQLException {
		super(Messages.TITULO_POLITICA_BONIFICACAO);
		listBonifCfgPedidoForm = new ListBonifCfgPedidoForm(pedido);
		setDefaultRect();
	}
	
	public ListBonifCfgWindow(Cliente cliente) throws SQLException {
		super(Messages.TITULO_POLITICA_BONIFICACAO);
		listBonifCfgPedidoForm = new ListBonifCfgPedidoForm(cliente);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listBonifCfgPedidoForm, LEFT, getTop(), FILL, FILL);
	}
	
}
