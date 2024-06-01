package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteCodService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoClienteCodWindow extends WmwWindow {
	
	private ListProdutoClienteCodForm listProdutoClienteCodForm;
	protected ButtonPopup btNovoAndFecharPedido;
	public boolean openCadProdutoClienteCodByNovoCodigo;
	private boolean fechamentoPedido;
	public boolean fecharPedido;
	private Cliente cliente;
	private Pedido pedido;
	
	public ListProdutoClienteCodWindow(Cliente cliente) throws SQLException {
		this(cliente, null);
	}
	
	public ListProdutoClienteCodWindow(Cliente cliente, Pedido pedido) throws SQLException {
		super(Messages.CAD_PROD_CLI_COD_ITENS);
		this.cliente = cliente;
		this.pedido = pedido;
		fechamentoPedido = pedido != null;
		listProdutoClienteCodForm = new ListProdutoClienteCodForm(cliente, pedido);
		btNovoAndFecharPedido = new ButtonPopup(fechamentoPedido ? Messages.BOTAO_FECHAR_PEDIDO: FrameworkMessages.BOTAO_NOVO);
		btFechar.setText(fechamentoPedido && !pedido.isPedidoAberto() ? FrameworkMessages.BOTAO_FECHAR : FrameworkMessages.BOTAO_CANCELAR);
		setDefaultRect();
	}
	
	public void initUI() {
		addScBase();
		UiUtil.add(this, cFundoFooter, LEFT, BOTTOM, FILL, footerH);
		UiUtil.add(this, listProdutoClienteCodForm, LEFT, getTop(), FILL, FILL);
		addButtonsPopUp();
	}

	private void addButtonsPopUp() {
		if (!(fechamentoPedido && !pedido.isPedidoAberto())) {
			addButtonPopup(btNovoAndFecharPedido);
		}
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btNovoAndFecharPedido) {
				if (fechamentoPedido) {
					btFecharPedidoClick();
				} else {
					btNovoClick();
				}
			}
			break;
		}
	}

	private void btFecharPedidoClick() throws SQLException {
		Vector produtoClienteCodPedidoList = ProdutoClienteCodService.getInstance().findAllProdutoClienteCodFromFechamentoPedido(listProdutoClienteCodForm.getDomainFilter());
		int size = produtoClienteCodPedidoList.size();
		for (int i = 0; i < size; i++) {
			ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) produtoClienteCodPedidoList.items[i];
			if (!produtoClienteCod.isPossuiCodigoProdutoCliente()) {
				UiUtil.showErrorMessage(Messages.CAD_PROD_CLI_COD_VALIDACAO_CODIGOS);
				return;
			}
		}
		ItemPedidoService.getInstance().updateCdProdutoClienteItensPedido(pedido.cdCliente, new ItemPedido(pedido));
		atualizaDomainItensPedidoAposUpdate();
		fecharPedido = true;
		unpop();
	}

	private void atualizaDomainItensPedidoAposUpdate() throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido)pedido.itemPedidoList.items[i];
			item.cdProdutoCliente = ItemPedidoService.getInstance().findColumnByRowKey(item.getRowKey(), "CDPRODUTOCLIENTE");
		}
	}

	private void btNovoClick() throws SQLException {
		CadProdutoClienteCodForm cadProdutoClienteCodForm = new CadProdutoClienteCodForm(cliente);
		CadProdutoClienteCodFormWindow cadProdutoClienteCodWindow = new CadProdutoClienteCodFormWindow(cadProdutoClienteCodForm);
		cadProdutoClienteCodForm.setCadProdutoClienteCodFormWindow(cadProdutoClienteCodWindow);
		cadProdutoClienteCodWindow.showWindow();
		listProdutoClienteCodForm.list();
	}

}
