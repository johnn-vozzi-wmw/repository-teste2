package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.FornecedorService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListFornecedoresNaoPositivadosWindow extends WmwListWindow {
	
	private Pedido pedido;
	private ButtonPopup btFecharPedido;
	private ButtonPopup btContinuar;
	public boolean isValidateFechamentoPedido;
	public boolean isContinuarClick;
	public boolean isFechandoPedido;
	private LabelValue lbMensagem;
	
	public ListFornecedoresNaoPositivadosWindow(Pedido pedido) {
		this(pedido, false);
	}
	
	public ListFornecedoresNaoPositivadosWindow(Pedido pedido, boolean isFechandoPedido) {
		super(Messages.FORNECEDOR_NAO_POSITIVADOS);
		this.pedido = pedido;
    	this.isFechandoPedido = isFechandoPedido;
    	lbMensagem = new LabelValue();
		listContainer = new GridListContainer(1, 1);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    	btFechar.setText(FrameworkMessages.BOTAO_VOLTAR);
    	if (isFechandoPedido) {
        	btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
    	} else {
        	btContinuar = new ButtonPopup(Messages.LIST_FORNECEDORES_NAO_POSITIVADOS_BOTAO_CONTINUAR);
    	}
    	isValidateFechamentoPedido = false;
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return PedidoService.getInstance().getFornecedoresNaoPositivadosList(pedido);
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		Fornecedor fornecedor = (Fornecedor) domain;
		String[] item = {StringUtil.getStringValue(fornecedor.nmFantasia)};
		return item;
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return FornecedorService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		Fornecedor domainFilter = new Fornecedor();
		return domainFilter;
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, lbMensagem, getLeft(), getTop(), FILL, PREFERRED + 20);
		lbMensagem.setMultipleLinesText(Messages.FORNECEDOR_MSG_CONFIRMACAO);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		if (isFechandoPedido) {
			addButtonPopup(btFecharPedido);
		} else {
			addButtonPopup(btContinuar);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}
	
	@Override
	public void onEvent(Event event) {
	    try {
			super.onEvent(event);
	    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
				if (event.target == btFecharPedido) {
					isValidateFechamentoPedido = true;
					unpop();
				}
				if (event.target == btContinuar) {
					isContinuarClick = true;
					unpop();
				}
				break;
			}
	    	default: break;
	    	}
    	} catch (Throwable ee) {
    		ExceptionUtil.handle(ee);
    	}
	}

}
