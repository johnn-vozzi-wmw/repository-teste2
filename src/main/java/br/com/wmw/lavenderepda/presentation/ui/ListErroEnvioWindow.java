package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ErroEnvio;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ErroEnvioService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListErroEnvioWindow extends WmwListWindow {

	private BaseContainer prevContainer;
	private ButtonPopup btSync;
	
    public ListErroEnvioWindow(BaseContainer prevContainer) throws SQLException {
        super(Messages.ERROENVIO_TITULO);
        this.prevContainer = prevContainer;
        listContainer = new GridListContainer(3, 1);
        listContainer.setBarTopSimple();
        singleClickOn = true;
        btSync = new ButtonPopup(Messages.ERROENVIO_LABEL_SYNC);
        setDefaultRect();
    }
    
//    @Override
    protected CrudService getCrudService() {
        return ErroEnvioService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		ErroEnvio domainFilter = new ErroEnvio();
		return domainFilter;
	}
    
    @Override
    public void detalhesClick() throws SQLException {
		SessionLavenderePda.cdTabelaPreco = "";
		try {
			UiUtil.showProcessingMessage();
			ErroEnvio domain = (ErroEnvio) getSelectedDomain();
			Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(domain.dsChave);
			if (pedido == null) {
				UiUtil.showErrorMessage(Messages.PEDIDO_MSG_PEDIDO_NAO_ENCONTRADO);
				return;
			}
			CadPedidoForm cadPedido = new CadPedidoForm();
			cadPedido.edit(pedido);
			fecharWindow();
			prevContainer.show(cadPedido);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
    }
    
    @Override
    protected void btFecharClick() throws SQLException {
    	super.btFecharClick();
    }
    
//  @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
        return getCrudService().findAll();
    }
    
    @Override
    protected String[] getItem(Object domain) {
        ErroEnvio erroEnvio = (ErroEnvio) domain;
        String[] item = {
                Messages.ERROENVIO_LABEL_NMENTIDADE + ": " + erroEnvio.nmEntidade.substring(5),
                Messages.ERROENVIO_LABEL_DSCHAVE + ": " + StringUtil.getStringValue(erroEnvio.dsChave),
                StringUtil.getStringValue(erroEnvio.dsErro)
                };
        return item;
    }
    
    @Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	return ((ErroEnvio) domain).dsErro;
    }

    @Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }
   
    @Override
    protected void onFormStart() {
    	addButtonPopup(btSync);
    	addButtonPopup(btFechar);
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
    }

    @Override
    public void onEvent(Event event) {
    	super.onEvent(event);
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSync) {
					try {
						MainLavenderePda.getInstance().showSincronizacaoForm();
						fecharWindow();
					} catch (SQLException e) {
						UiUtil.showErrorMessage(e);
					}
				}
			}
		break;
    	}
    }

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
}
