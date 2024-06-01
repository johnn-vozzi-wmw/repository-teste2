package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoCreditoDescontoWindow extends WmwListWindow {

	private Pedido pedido;
	public String cdProduto;
	public String cdTabelaPreco;
	private LabelName lbCreditoDisponivel;
	private LabelValue lvCreditoDisponivel;
	
    public ListProdutoCreditoDescontoWindow(Pedido pedido, String cdTabelaPreco) {
        super(Messages.PRODUTOCREDITODESCONTO_TITULO_LISTA);
        singleClickOn = true;
        this.pedido = pedido;
        this.cdTabelaPreco = cdTabelaPreco;
        listContainer = new GridListContainer(4, 2);
        listContainer.setColPosition(3, RIGHT);
        listContainer.setBarTopSimple();
        lbCreditoDisponivel = new LabelName(Messages.PRODUTOCREDITODESCONTO_DISPONIVEIS);
		lvCreditoDisponivel = new LabelValue(StringUtil.getStringValueToInterface(pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido));
        setDefaultRect();
    }
    
    //@Override
    protected CrudService getCrudService() {
        return ProdutoCreditoDescService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		ProdutoCreditoDesc domainFilter = new ProdutoCreditoDesc();
		domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		domainFilter.flTipoCadastroProduto = ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO;
		return domainFilter;
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Vector domainList = new Vector();
    	Vector produtoCreditoDescList = new Vector();
    	if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
    		domainList = ProdutoCreditoDescService.getInstance().findAllByItemPedidoList(pedido, domain);
    		if (LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
    			for (int i = 0; i < domainList.size(); i++) {
    				ProdutoCreditoDesc produtoCreditoDesc = (ProdutoCreditoDesc) domainList.items[i];
    				Produto produto = ProdutoService.getInstance().getProduto(produtoCreditoDesc.cdProduto);
    				if (produto.cdEmpresa != null && !DescPromocionalService.getInstance().isProdutoPossuiValorNoGrupoDescPromo(pedido, produto, cdTabelaPreco)) {
    					produtoCreditoDescList.addElement(produtoCreditoDesc);
    				}
    			}
    			return produtoCreditoDescList;
    		} else {
    			return domainList;
    		}
    	}
        return produtoCreditoDescList;
    }
    
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
        Produto produto = ProdutoService.getInstance().getProduto(produtoCreditoDesconto.cdProduto);
        String[] item = {
        	StringUtil.getStringValue(produto.toString()),
       		"",
            Messages.PRODUTOCREDITODESCONTO_QTD_ITEM + StringUtil.getStringValueToInterface(produtoCreditoDesconto.qtItem),
            Messages.PRODUTOCREDITODESCONTO_VL_UNITARIO + StringUtil.getStringValueToInterface(ProdutoCreditoDescService.getInstance().getVlItemIndicesCliECondComercial(produtoCreditoDesconto, pedido)),
            StringUtil.getStringValue(produtoCreditoDesconto.itemPedidoRowkey)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

   
    @Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
    	return ProdutoService.getInstance().getProduto(produtoCreditoDesconto.cdProduto).toString();
    }
    
    //--------------------------------------------------------------

    @Override
    public void singleClickInList() throws SQLException {
    	super.singleClickInList();
    	cdProduto = ((BaseListContainer.Item)listContainer.getSelectedItem()).id;
    	fecharWindow();
    }
    
    @Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
    	ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
    	super.setPropertiesInRowList(c, domain);
    	c.id = produtoCreditoDesconto.cdProduto;
    }
    
    //@Override
    protected void onFormStart() {
    	UiUtil.add(this, lbCreditoDisponivel, lvCreditoDisponivel, getLeft(), getNextY());
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
    }

    //@Override
    protected void onFormEvent(Event event) {
    }
    

}
