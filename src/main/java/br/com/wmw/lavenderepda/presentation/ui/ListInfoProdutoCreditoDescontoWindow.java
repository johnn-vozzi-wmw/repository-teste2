package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwComboBoxListWindow;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListInfoProdutoCreditoDescontoWindow extends LavendereWmwComboBoxListWindow {

	private static int TABPANEL_GERACAO = 0;
	private static int TABPANEL_CONSUMO = 1;
	
	private ScrollTabbedContainer tabs;
	protected GridListContainer listContainerGeracao;
	public String cdProduto;
	public String cdTabelaPreco;
	private Pedido pedido;
	
    public ListInfoProdutoCreditoDescontoWindow(Pedido pedido, String cdTabelaPreco) {
        super(Messages.PRODUTOCREDITODESCONTO_TITULO);
        tabs = new ScrollTabbedContainer(new String[]{Messages.PRODUTOCREDITODESCONTO_CREDITO, Messages.PRODUTOCREDITODESCONTO_DESCONTO});
        configListContainer("CDPRODUTO");
        listContainer = new GridListContainer(4, 2);
        listContainer.setColPosition(3, RIGHT);
        listContainer.setColsSort(new String[][] {{"Código", "CDPRODUTO"}, {"Descrição", "DSPRODUTO"}});
        listContainer.atributteSortSelected = sortAtributte;
        listContainer.sortAsc = sortAsc;
        listContainerGeracao = new GridListContainer(4, 2);
        listContainerGeracao.setColsSort(new String[][] {{"Código", "CDPRODUTO"}, {"Descrição", "DSPRODUTO"}});
        listContainerGeracao.atributteSortSelected = sortAtributte;
        listContainerGeracao.sortAsc = sortAsc;
        listContainerGeracao.setColPosition(3, RIGHT);
        this.cdTabelaPreco = cdTabelaPreco;
        this.pedido = pedido;
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
    	Vector domainList = ProdutoCreditoDescService.getInstance().findAllByExample(domain);
    	Vector produtoCreditoDescList = new Vector();
    	if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
			domainList = GrupoCliPermProdService.getInstance().restringeProdutoCreditoDescByGrupoCliPermProd(domainList);
		}
    	if (ValueUtil.isNotEmpty(domainList)) {
    		for (int i = 0; i < domainList.size(); i++) {
    			ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domainList.items[i];
    			Produto produto = ProdutoService.getInstance().getProduto(produtoCreditoDesconto.cdProduto);
    			if (produto.cdEmpresa != null) {
	    			produtoCreditoDesconto.dsProduto = produto.toString();
	    			if (!LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() || !DescPromocionalService.getInstance().isProdutoPossuiValorNoGrupoDescPromo(pedido, produto, cdTabelaPreco) || !ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(produtoCreditoDesconto.flTipoCadastroProduto)) {
	    				if (produtoCreditoDesconto.isVigente()) {
							produtoCreditoDescList.addElement(produtoCreditoDesconto);
	    				}
	    			}
    			}
			}
    	}
    	if ("DSPRODUTO".equals(domain.sortAtributte)) {
			SortUtil.qsortString(produtoCreditoDescList.items, 0, produtoCreditoDescList.size() - 1, ValueUtil.VALOR_SIM.equals(domain.sortAsc));
		}
    	return produtoCreditoDescList;
    }
    
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
        String[] item = {
        	StringUtil.getStringValue(produtoCreditoDesconto.dsProduto),
       		"",
            Messages.PRODUTOCREDITODESCONTO_QTD_ITEM + StringUtil.getStringValueToInterface(produtoCreditoDesconto.qtItem),
            Messages.PRODUTOCREDITODESCONTO_VL_UNITARIO + StringUtil.getStringValueToInterface(ProdutoCreditoDescService.getInstance().getVlItemIndicesCliECondComercial(produtoCreditoDesconto, pedido))};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() {
    	if (tabs.getActiveTab() == TABPANEL_CONSUMO) {
    		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
    		return c.id;
    	} else {
    		BaseListContainer.Item c = (BaseListContainer.Item)listContainerGeracao.getSelectedItem();
    		return c.id;
    	}
    }

    
    //--------------------------------------------------------------

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, tabs, LEFT, TOP, FILL, FILL);
        UiUtil.add(getFirstContainer(), listContainerGeracao, LEFT, TOP, FILL, FILL);
        UiUtil.add(getSecondContainer(), listContainer, LEFT, TOP, FILL, FILL);
        tabs.setActiveTab(TABPANEL_GERACAO);
        listProdGeracao();
    }

    
    private Container getFirstContainer() {
    	return tabs.getContainer(TABPANEL_GERACAO);
	}

	private Container getSecondContainer() {
		return tabs.getContainer(TABPANEL_CONSUMO);
	}
    
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainerGeracao != null) && (event.target == listContainerGeracao.popupMenuOrdenacao)) {
					if (listContainerGeracao.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listContainerGeracao.reloadSortSettings();
						sortAtributte = listContainerGeracao.atributteSortSelected;
						sortAsc = StringUtil.getStringValue(listContainerGeracao.sortAsc);
						beforeOrder();
						try {
							listProdGeracao();
						} catch (SQLException e) {
						}
					}
				}
				break;
			}
		}
	}
	
    //@Override
    protected void onFormEvent(Event event) {
    }
    
    public void listProdGeracao() throws java.sql.SQLException {
		if (listContainerGeracao != null) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			int listSize = 0;
			Vector domainList = null;
			try {
				listContainerGeracao.removeAllContainers();
				//---
				domainList = getProdutoGeracaoList();
				listSize = domainList.size();
				Container[] all = new Container[listSize];
				//--
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
				        all[i] = c = new BaseListContainer.Item(listContainerGeracao.getLayout());
				        domain = (BaseDomain)domainList.items[i];
				        c.id = domain.getRowKey();
				        c.setItens(getItemProdGeracao(domain));
				        c.setToolTip(getToolTip(domain));
				        c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend);
				        setPropertiesInRowList(c, domain);
				        domain = null;
					}
					listContainerGeracao.addContainers(all);
				}
			} finally {
				domainList = null;
				msg.unpop();
			}
		}
    }
    
    private Vector getProdutoGeracaoList() throws SQLException {
    	ProdutoCreditoDesc domainFilter = new ProdutoCreditoDesc();
		domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		domainFilter.flTipoCadastroProduto = ProdutoCreditoDesc.FLTIPOCADASTRO_QTD;
		domainFilter.sortAtributte = getDomainFilterSortable().sortAtributte;
		domainFilter.sortAsc = listContainerGeracao.sortAsc;
		Vector domainList = ProdutoCreditoDescService.getInstance().findAllByExample(domainFilter);
		Vector domainListFinal = new Vector();
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
			domainList = GrupoCliPermProdService.getInstance().restringeProdutoCreditoDescByGrupoCliPermProd(domainList);
		}
		if (ValueUtil.isNotEmpty(domainList)) {
    		for (int i = 0; i < domainList.size(); i++) {
    			ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domainList.items[i];
    			if (produtoCreditoDesconto.isVigente()) {
    				Produto produto = ProdutoService.getInstance().getProduto(produtoCreditoDesconto.cdProduto);
    				if (produto.cdEmpresa != null) {
	    				ItemPedido itemPedidoComDescPromo = new ItemPedido();
						itemPedidoComDescPromo.cdEmpresa = produtoCreditoDesconto.cdEmpresa;
						itemPedidoComDescPromo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
						itemPedidoComDescPromo.cdProduto = produtoCreditoDesconto.cdProduto;
						itemPedidoComDescPromo.cdTabelaPreco = cdTabelaPreco;
						itemPedidoComDescPromo.cdCliente = SessionLavenderePda.getCliente().cdCliente;
						itemPedidoComDescPromo.setProduto(produto);
						itemPedidoComDescPromo.pedido = pedido;
						itemPedidoComDescPromo.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						itemPedidoComDescPromo.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						itemPedidoComDescPromo.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
						if (!isPrecoNormalMaiorPrecoComDescPromo(itemPedidoComDescPromo)) {
							produtoCreditoDesconto.dsProduto = produto.toString();
							domainListFinal.addElement(produtoCreditoDesconto);
						}
    				}
    			}
			}
    	}
    	if ("DSPRODUTO".equals(getDomainFilterSortable().sortAtributte)) {
			SortUtil.qsortString(domainListFinal.items, 0, domainListFinal.size() - 1, ValueUtil.VALOR_SIM.equals(getDomainFilterSortable().sortAsc));
		}
    	return domainListFinal;
    }
    
    public boolean isPrecoNormalMaiorPrecoComDescPromo(final ItemPedido itemPedidoComDescPromo) throws SQLException {
    	DescPromocional descPromocional = itemPedidoComDescPromo.getDescPromocional();
    	if (descPromocional != null && !descPromocional.equals(new DescPromocional())) {
			ItemPedido itemPedidoNormal = (ItemPedido) itemPedidoComDescPromo.clone();
			itemPedidoNormal.qtdCreditoDesc = 0;
			itemPedidoNormal.flTipoCadastroItem = null;
			itemPedidoNormal.cdProdutoCreditoDesc = null;
			itemPedidoNormal.descPromocional = null; 
			itemPedidoNormal.bloqueiaAplicaDescPromocional = true;
			PedidoService.getInstance().resetDadosItemPedido(itemPedidoNormal.pedido, itemPedidoNormal);
			PedidoService.getInstance().resetDadosItemPedido(itemPedidoNormal.pedido, itemPedidoComDescPromo);
			return itemPedidoNormal.vlItemPedido > itemPedidoComDescPromo.vlItemPedido;
		}
		return false;
	}

	//@Override
    protected String[] getItemProdGeracao(Object domain) throws SQLException {
        ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) domain;
        Produto produto = ProdutoService.getInstance().getProduto(produtoCreditoDesconto.cdProduto);
        String[] item = {
    		produto.toString(),
       		"",
            Messages.PRODUTOCREDITODESCONTO_QTD_ITEM + StringUtil.getStringValueToInterface(produtoCreditoDesconto.qtItem),
			Messages.PRODUTOCREDITODESCONTO_VL_UNIT + " " + StringUtil.getStringValueToInterface(ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false))};
        return item;
    }
}
