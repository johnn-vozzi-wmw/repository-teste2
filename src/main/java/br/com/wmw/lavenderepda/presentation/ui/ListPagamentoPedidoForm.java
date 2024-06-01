package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListPagamentoPedidoForm extends BaseCrudListForm {

	protected Pedido pedido;
	public double vlPedidoAberto;
	private CadPedidoForm cadPedidoForm;
	private ButtonAction btNovoPagamento;
	
    public ListPagamentoPedidoForm(Pedido pedido) {
        super(Messages.PAGAMENTOPEDIDO_TITULO_CADASTRO);
        this.pedido = pedido;
        singleClickOn = true;
        newGridListContainer();
        btNovoPagamento = new ButtonAction(Messages.PAGAMENTOPEDIDO_BT_NOVO_PAGAMENTO, "images/add.png");
    }

	private void newGridListContainer() {
		int qtdColunas = 9;
		int posicaoColuna = 8;
		int posicaoColunaTotalizadorDireita = 7;
		if (LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPagamentoPedido()) {
			qtdColunas = 6;
			posicaoColuna = 5;
			posicaoColunaTotalizadorDireita = 4;
		}
		listContainer = new GridListContainer(qtdColunas, 3);
		listContainer.setColPosition(posicaoColuna, RIGHT);
        listContainer.setColTotalizerRight(posicaoColunaTotalizadorDireita, Messages.PAGAMENTOPEDIDO_LABEL_VLTOTAL);
        listContainer.setBarTopSimple();
	}
    
    protected CrudService getCrudService() throws SQLException {
        return PagamentoPedidoService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() throws SQLException {
    	PagamentoPedido pagamentoPedidoFilter = new PagamentoPedido();
    	pagamentoPedidoFilter.cdEmpresa = pedido.cdEmpresa;
    	pagamentoPedidoFilter.cdRepresentante = pedido.cdRepresentante;
    	pagamentoPedidoFilter.flOrigemPedido = pedido.flOrigemPedido;
    	pagamentoPedidoFilter.nuPedido = pedido.nuPedido;
		return pagamentoPedidoFilter;
	}
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	if (ValueUtil.isNotEmpty(pedido.nuPedido)) {
    		final Vector domainList = super.getDomainList();
			pedido.pagamentoPedidoList = domainList;
			return domainList;
    	}
    	return new Vector();
    }
    
    @Override
    public void visibleState() {
    	btNovo.setVisible(false);
    	btNovoPagamento.setVisible(cadPedidoForm.isEditing() && cadPedidoForm.isEnabled() || pedido.isPedidoConsignado());
    }
    
    @Override
    protected void initCabecalhoRodape() throws SQLException {
    	UiUtil.add(this, barBottomContainer, LEFT, BOTTOM, FILL, UiUtil.getBottomBarPreferredHeight());
    }
    
    protected String[] getItem(Object domain) throws SQLException {
        PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
        return LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPagamentoPedido() ? getItemSemCondicaoPagamento(pagamentoPedido) : getItemComCondicaoPagamento(pagamentoPedido);
    }
    
    private String[] getItemSemCondicaoPagamento(PagamentoPedido pagamentoPedido) throws SQLException {
		String[] item = {
	        StringUtil.getStringValue(TipoPagamentoService.getInstance().getTipoPagamento(pagamentoPedido.cdTipoPagamento)),
	        "",
	        "",
	        Messages.PRODUTO_LABEL_RS, 
	        StringUtil.getStringValueToInterface(pagamentoPedido.vlPagamentoPedido - pagamentoPedido.vlDesconto),
	        StringUtil.getStringValueToInterface(getVlPctDesconto(pagamentoPedido)) + Messages.PAGAMENTOPEDIDO_LABEL_PCT_DESC
	        };
		return item;
	}

    private double getVlPctDesconto(PagamentoPedido pagamentoPedido) {
		return pagamentoPedido.vlPagamentoPedido > 0 ? (pagamentoPedido.vlDesconto * 100) / pagamentoPedido.vlPagamentoPedido : 0d;
	}
    
    private String[] getItemComCondicaoPagamento(PagamentoPedido pagamentoPedido) throws SQLException {
    	String[] item = {
    			StringUtil.getStringValue(TipoPagamentoService.getInstance().getTipoPagamento(pagamentoPedido.cdTipoPagamento)),
    			"",
    			"",
    			StringUtil.getStringValue(CondicaoPagamentoService.getInstance().getCondicaoPagamento(pagamentoPedido.cdCondicaoPagamento)),
    			"",
    			"",
    			Messages.PRODUTO_LABEL_RS, 
    			StringUtil.getStringValueToInterface(pagamentoPedido.vlPagamentoPedido - pagamentoPedido.vlDesconto),
    			LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido ? "" : StringUtil.getStringValueToInterface((pagamentoPedido.vlDesconto * 100) / pagamentoPedido.vlPagamentoPedido) + Messages.PAGAMENTOPEDIDO_LABEL_PCT_DESC
    	};
    	return item;
    }
	
	
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }
   
    @Override
    public void list() throws SQLException {
    	super.list();
    	cadPedidoForm.atualizaVlPedidoAberto();
    }

    protected void onFormStart() throws SQLException {
    	listContainer.resizeable = false;
		UiUtil.add(barBottomContainer, btNovoPagamento, 1); 
        UiUtil.add(this, listContainer, LEFT, getTop() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
        
    }

    @Override
    public void novoClick() throws SQLException {
    	CadPagamentoPedidoWindow cadPagamentoPedidoWindow = new CadPagamentoPedidoWindow(pedido, vlPedidoAberto);
    	cadPagamentoPedidoWindow.setListPagamentoPedidoForm(this);
    	cadPagamentoPedidoWindow.setDefaultRect();
    	cadPagamentoPedidoWindow.add();
    	cadPagamentoPedidoWindow.popup();
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	PagamentoPedido pagamentoPedido = (PagamentoPedido)getSelectedDomain();
    	CadPagamentoPedidoWindow cadPagamentoPedidoWindow = new CadPagamentoPedidoWindow(pedido, vlPedidoAberto, pagamentoPedido.cdTipoPagamento);
    	cadPagamentoPedidoWindow.setListPagamentoPedidoForm(this);
    	cadPagamentoPedidoWindow.setEnabled(cadPedidoForm.isEditing() && cadPedidoForm.isEnabled() || pedido.isPedidoConsignado());
    	cadPagamentoPedidoWindow.setDefaultRect();
    	cadPagamentoPedidoWindow.edit(pagamentoPedido);
    	cadPagamentoPedidoWindow.popup();
    }
    
    protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btNovoPagamento) {
				novoClick();
			}
			break;
		}
    }


	public void setCadPedidoForm(CadPedidoForm cadPedidoForm) {
		this.cadPedidoForm = cadPedidoForm;
	}

}
