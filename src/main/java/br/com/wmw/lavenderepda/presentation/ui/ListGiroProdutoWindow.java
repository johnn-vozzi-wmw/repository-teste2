package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class ListGiroProdutoWindow extends WmwWindow {

	public ListGiroProdutoForm listGiroProdutoForm;
	public boolean hasGiroProduto;
	public boolean onFechamentoPedido;
	public boolean fecharPedido;
	private ButtonPopup btFecharPedido;

	public ListGiroProdutoWindow(CadItemPedidoForm cadItemPedidoForm, CadPedidoForm cadPedidoForm, Pedido pedido, boolean fromBtNovoItemOrFechamentoPed, boolean fromMenuCatalogo, boolean onFechamentoPedido) throws SQLException {
		super(Messages.GIROPRODUTO_NOME_ENTIDADE);
		this.onFechamentoPedido = onFechamentoPedido;
		listGiroProdutoForm = new ListGiroProdutoForm(true, this, cadPedidoForm, pedido, fromBtNovoItemOrFechamentoPed, fromMenuCatalogo, onFechamentoPedido);
		listGiroProdutoForm.setCadItemPedidoForm(cadItemPedidoForm);
		hasGiroProduto = listGiroProdutoForm.hasGiroProduto;
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PED);
		scrollable = false;
		setDefaultRect();
	}
	
	public void initUI() {
		super.initUI();
		UiUtil.add(this, listGiroProdutoForm , LEFT , getTop() , FILL , FILL - footerH);
		listGiroProdutoForm.visibleState();
		if (onFechamentoPedido) {
			addButtonPopup(btFecharPedido);
			addButtonPopup(btFechar);
		}
	}
	
	public void close() throws SQLException {
		super.fecharWindow();
	}
	
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFecharPedido) {
					fecharPedido = true;
					fecharWindow();
				}
			}
		}
	}
}
