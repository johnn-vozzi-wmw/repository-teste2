package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListSacPedidoClienteWindow extends WmwListWindow {
	
	private Sac sac;
	public Vector listPedidoCliente;
	public String nuPedido;

    public ListSacPedidoClienteWindow(Sac sacSelecionado) throws SQLException {
        super(Messages.PEDIDO_LABEL_PEDIDOS);
        singleClickOn = true;
        listContainer = new GridListContainer(6, 2);
        listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
        this.sac = sacSelecionado;
        sortAsc = "N, N";
        sortAtributte = "DTEMISSAO";
        listPedidoCliente = new Vector();
        setDefaultRect();
    }
    
    //@Override
    protected CrudService getCrudService() throws SQLException {
        return PedidoService.getInstance(); 
    }
    
    @Override
    protected BaseDomain getDomainFilter() {
    	Pedido domainFilter = new Pedido();
    	domainFilter.cdCliente = sac.cdCliente;
    	domainFilter.cdEmpresa = sac.cdEmpresa;
    	domainFilter.cdRepresentante = sac.cdRepresentante;
    	return domainFilter;
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Pedido pedido = (Pedido) domain;
    	pedido.cdEmpresa = sac.cdEmpresa;
    	pedido.cdRepresentante = sac.cdRepresentante;
    	pedido.cdCliente = sac.cdCliente;
		pedido.addJoinTipoPedidoOnCadastroSac = true;
        return listPedidoCliente = getCrudService().findAllByExample(pedido);
    }
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	Pedido pedido = (Pedido)domain;
		return new String[]{
				StringUtil.getStringValue(pedido.nuPedido),
				StringUtil.getStringValue(" - " + pedido.dtEmissao),
				StringUtil.getStringValue(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(pedido.vlTotalPedido)),
				StringUtil.getStringValue(getDsTipoPedido(pedido)),
				StringUtil.getStringValue(getDsStatusPedido(pedido)),
				StringUtil.getStringValue(" - " + pedido.cdCliente),
		};
    }

	private String getDsTipoPedido(Pedido pedido) {
		if (!LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao() && ValueUtil.isNotEmpty(pedido.tipoPedido.dsTipoPedido)) {
			return " - " + pedido.tipoPedido.dsTipoPedido;
		}
		return "";
	}

	private String getDsStatusPedido(Pedido pedido) {
		return StringUtil.getStringValue(pedido.statusPedidoPda.dsStatusPedido);
	}

	//@Override
    protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop() + HEIGHT_GAP, FILL, FILL);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
    
    @Override
    public void singleClickInList() throws SQLException {
    	String[] rowKeyPedido = getSelectedRowKey().split(";");
   		nuPedido = rowKeyPedido[2];
   		btFecharClick();
    }
    
	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.domain;
	}
	
	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

}
