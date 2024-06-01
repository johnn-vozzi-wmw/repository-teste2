package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.Pedido;

public class ListItemPedidoBonifCfgWindow extends WmwWindow {
	
	ListItemPedidoBonifCfgForm listItemPedidoBonifCfgForm;

	public ListItemPedidoBonifCfgWindow(Pedido pedido, BonifCfg bonifCfg) {
		super(Messages.ITEMPEDIDOBONIFCFG_NOME_LISTA);
		listItemPedidoBonifCfgForm = new ListItemPedidoBonifCfgForm(pedido, bonifCfg);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listItemPedidoBonifCfgForm, LEFT, getTop(), FILL, FILL);
	}
}
