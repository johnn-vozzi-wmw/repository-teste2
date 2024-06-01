package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListAvisoProdutoCreditoDescontoWindow extends WmwListWindow {

	private Pedido pedido;
	private LabelValue lbInfoProduto;
	private ButtonPopup btFecharPedido;
	public boolean fecharPedido;
	private String cdTabelaPreco;
	
    public ListAvisoProdutoCreditoDescontoWindow(Pedido pedido, String cdTabelaPreco) {
        super(Messages.PRODUTOCREDITODESCONTO_TITULO_LISTA_FECHAMENTO_PEDIDO);
        singleClickOn = false;
        this.pedido = pedido;
        listContainer = new GridListContainer(4, 2);
        listContainer.setBarTopSimple();
        listContainer.setColPosition(3, RIGHT);
        lbInfoProduto = new LabelValue(Messages.PRODUTOCREDITODESCONTO_FECHAMENTO_PEDIDO, CENTER);
        btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
        btFechar.setText(Messages.BOTAO_CANCELAR);
        this.cdTabelaPreco = cdTabelaPreco;
        setDefaultRect();
    }
    
    //@Override
    protected CrudService getCrudService() {
        return ProdutoCreditoDescService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
    	ItemPedido domainFilter = new ItemPedido();
		return domainFilter;
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
        return pedido.itemPedidoCredDescList;
    }
    
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        Produto produto = ProdutoService.getInstance().getProduto(itemPedido.cdProduto);
        String[] item = {
    		produto.toString(),
    		"",
    		MessageUtil.getMessage(Messages.PRODUTOCREDITODESCONTO_QTD_GERAR_CRED, itemPedido.qtdItemGerarCred),
			Messages.PRODUTOCREDITODESCONTO_VL_UNIT + " " + StringUtil.getStringValueToInterface(ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false))};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

   
    @Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	ItemPedido itemPedido = (ItemPedido) domain;
    	return ProdutoService.getInstance().getProduto(itemPedido.cdProduto).toString();
    }
    
    //--------------------------------------------------------------
    
    public void screenResized() {
		super.screenResized();
		lbInfoProduto.setText(MessageUtil.quebraLinhas(lbInfoProduto.getText(), width - 20));
		lbInfoProduto.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}
    
    //@Override
    protected void onFormStart() {
    	lbInfoProduto.setText(MessageUtil.quebraLinhas(lbInfoProduto.getText(), width - 20));
    	UiUtil.add(this, lbInfoProduto, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
    	addButtonPopup(btFecharPedido);
    	addButtonPopup(btFechar);
    }

    @Override
    public void onEvent(Event event) {
    	super.onEvent(event);
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFecharPedido) {
					fecharPedido = true;
					try {
						fecharWindow();
					} catch (SQLException e) {
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
