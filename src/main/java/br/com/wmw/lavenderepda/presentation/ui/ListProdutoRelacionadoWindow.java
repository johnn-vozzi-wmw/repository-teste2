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
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoRelacionado;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.ConversaoUnidadeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoRelacionadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoRelacionadoWindow extends WmwListWindow {

	private LabelValue lbDescricao;
	private String dsDescription;
	public CadPedidoForm cadPedidoForm;
	private Pedido pedido;
	private boolean fromCadItemPedido;
	private ButtonPopup btLiberarSenha;
	private ButtonPopup btInserirRelacionados;

    public ListProdutoRelacionadoWindow(String dsDescription, Pedido pedido, boolean fromCadItemPedido) {
        super(Messages.PRODUTO_RELACIONADO_TITULO);
        this.pedido = pedido;
        this.dsDescription = StringUtil.getStringValue(dsDescription);
        this.fromCadItemPedido = fromCadItemPedido;
        singleClickOn = !LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado;
        lbDescricao = new LabelValue(this.dsDescription, CENTER);
        constructorListContainer();
        btLiberarSenha = new ButtonPopup(Messages.MENU_SISTEMA_GERAR_SENHA);
        btInserirRelacionados = new ButtonPopup(Messages.BOTAO_INSERIR);
        setDefaultRect();
    }

	//@Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return ProdutoRelacionadoService.getInstance();
    }

    protected BaseDomain getDomainFilter() {
		ProdutoRelacionado domainFilter = new ProdutoRelacionado();
		return domainFilter;
	}

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	return pedido.prodRelacionadosNaoContempladosList;
    }

    private Produto getSelectedProduto() throws SQLException {
    	return (Produto) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId());
    }

    private void constructorListContainer() {
		listContainer = new GridListContainer(3, 1);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }

    protected String getToolTip(BaseDomain domain) throws SQLException {
    	Produto produto = (Produto) domain;
    	return produto + "\n\n" + Messages.PRODUTO_RELACIONADO_RELACIONADO_A + ": " + produto.getProdutoRelacionado();

    }

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
        Produto produto = (Produto) domain;
        String dsUnidade = (LavenderePdaConfig.usaConversaoUnidadesMedida) ? produto.dsUnidadeFaturamento : produto.dsUnidadeFisica;
        double qtProdutoFaltante = produto.qtFaltanteProdutoRelacionado;
        if (LavenderePdaConfig.usaConversaoUnidadesMedida && !LavenderePdaConfig.validaVendaRelacionadaUnidadeFaturamento) {
        	qtProdutoFaltante = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(pedido.getCliente(), produto, qtProdutoFaltante);
        }
        String[] item = {
        		StringUtil.getStringValue(produto),
    			StringUtil.getStringValue(Messages.PRODUTO_RELACIONADO_QTD_FALTANTE + ": " + StringUtil.getStringValue(ValueUtil.getIntegerValueRoundUp(qtProdutoFaltante))) + " - " + StringUtil.getStringValue(dsUnidade),
    			StringUtil.getStringValue(Messages.PRODUTO_RELACIONADO_RELACIONADO_A + ": " + produto.getProdutoRelacionado().toString())};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    public void screenResized() {
		super.screenResized();
		lbDescricao.setText(MessageUtil.quebraLinhas(dsDescription, width - 20));
		lbDescricao.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

    //@Override
    protected void onFormStart() {
    	lbDescricao.setText(MessageUtil.quebraLinhas(dsDescription, width - 20));
    	UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
    }
    
    @Override
    protected void addButtons() {
    	if (LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado) {
    		addButtonPopup(btInserirRelacionados);
    	}
    	if (LavenderePdaConfig.usaSenhaVendaRelacionada) {
    		addButtonPopup(btLiberarSenha);
    	}
    	super.addButtons();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btLiberarSenha) {
					int size = pedido.prodRelacionadosNaoContempladosList.size();
					Vector cdProdutoList = new Vector();
					for (int i = 0; i < size; i++) {
						Produto produto = (Produto) pedido.prodRelacionadosNaoContempladosList.items[i];
						if (!cdProdutoList.contains(produto.cdProdutoPrincipal)) {
							cdProdutoList.addElement(produto.cdProdutoPrincipal);
						}
					}
					AdmSenhaDinamicaWindow admSenhaDinamicaForm = new AdmSenhaDinamicaWindow();
					admSenhaDinamicaForm.setMensagem(Messages.PRODUTO_RELACIONADO_VENDA_RELACIONADA);
					admSenhaDinamicaForm.setCdProdutoList(cdProdutoList);
					admSenhaDinamicaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_PRODUTO_RELACIONADO);
					if (admSenhaDinamicaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
						ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, admSenhaDinamicaForm.cdProdutoLiberado);
						if (itemPedido != null) {
							itemPedido.pedido = pedido;
							itemPedido.flLiberadoVendaRelacionada = ValueUtil.VALOR_SIM;
							PedidoService.getInstance().updateItemPedido(pedido, itemPedido);
							ProdutoRelacionadoService.getInstance().loadProdutosRelacionadosNaoContemplados(pedido);
							if (ValueUtil.isEmpty(pedido.prodRelacionadosNaoContempladosList)) {
								fecharWindow();
							} else {
								list();
							}
						} else {
							UiUtil.showWarnMessage(Messages.PRODUTO_RELACIONADO_MSG_PRODUTO_NAO_ENCONTRADO);
						}
					}
				} else if (event.target == btInserirRelacionados) {
					btInserirItensRelacionadosClick();
				}
				break;
			}
    	}
    }


    public void singleClickInList() throws SQLException {
    	super.singleClickInList();
    	CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
		Produto produto = getSelectedProduto();
		if (produto != null) {
			ItemPedido itemPedido = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, produto.cdProduto);
			//--
			cadItemPedidoForm.fromProdutoRelacionadoWindow = true;
			cadItemPedidoForm.fromProdutoRelacionadoWindowOnFechamento = !fromCadItemPedido;
			if (itemPedido == null) {
				cadItemPedidoForm.add();
				if (!fromCadItemPedido) {
					MainLavenderePda.getInstance().show(cadItemPedidoForm);
				}
				cadItemPedidoForm.produtoSelecionado = produto;
				cadItemPedidoForm.gridClickAndRepaintScreen(!cadItemPedidoForm.inVendaUnitariaMode);
			} else {
				cadItemPedidoForm.edit(itemPedido);
				if (!fromCadItemPedido) {
					MainLavenderePda.getInstance().show(cadItemPedidoForm);
				} else {
					cadItemPedidoForm.onFormShow();
				}
				cadItemPedidoForm.produtoSelecionado = produto;
			}
			//--
			fecharWindow();
			if (cadItemPedidoForm.inVendaUnitariaMode) {
				cadPedidoForm.inItemNegotiationProdutosRelacionados = true;
				cadItemPedidoForm.setFocusInQtde();
			} else {
				stopShowProdutoRelacionadoWindw();
			}
		}
    }

    protected void btFecharClick() throws SQLException {
		stopShowProdutoRelacionadoWindw();
		super.btFecharClick();
    }

    private void stopShowProdutoRelacionadoWindw() throws SQLException {
    	cadPedidoForm.inItemNegotiationProdutosRelacionados = false;
    	CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
    	cadItemPedidoForm.fromProdutoRelacionadoWindow = false;
    	cadItemPedidoForm.fromProdutoRelacionadoWindowOnFechamento = false;
    }

    private void btInserirItensRelacionadosClick() throws SQLException {
    	Vector produtoList = getDomainList(getDomainFilter());
    	if (ValueUtil.isNotEmpty(produtoList)) {
    		for (int i = 0; i < produtoList.size(); i++) {
    			Produto produto = (Produto) produtoList.items[i];
    			ItemPedido itemPedidoPrincipal = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, produto.cdProdutoPrincipal);
    			ItemPedido itemPedidoRelacionado = ItemPedidoService.getInstance().getItemPedidoByCdProduto(pedido, produto.cdProduto);
    			if (itemPedidoRelacionado == null) {
    				itemPedidoRelacionado = ItemPedidoService.getInstance().createNewItemPedidoRelacionadoByItemPedidoPrincipal(itemPedidoPrincipal, produto, pedido);
    				insereItemPedidoRelacionadoEAtualizaValores(itemPedidoRelacionado);
    				atualizaListaItensPedido(produto);
    			} else {
    				if (LavenderePdaConfig.validaVendaRelacionadaUnidadeFaturamento && LavenderePdaConfig.usaConversaoUnidadesMedida) {
    					produto.qtFaltanteProdutoRelacionado += itemPedidoRelacionado.qtItemFaturamento;
    				} else {
    					produto.qtFaltanteProdutoRelacionado += itemPedidoRelacionado.getQtItemFisico();
    				}
    				PedidoService.getInstance().deleteItemPedido(pedido, itemPedidoRelacionado);
    				itemPedidoRelacionado = ItemPedidoService.getInstance().createNewItemPedidoRelacionadoByItemPedidoPrincipal(itemPedidoPrincipal, produto, pedido);
    				insereItemPedidoRelacionadoEAtualizaValores(itemPedidoRelacionado);
    				atualizaListaItensPedido(produto);
    			}
			}
    		fecharWindow();
    	}
    }

    private void insereItemPedidoRelacionadoEAtualizaValores(ItemPedido itemPedidoRelacionado) throws SQLException {
    	ItemPedidoService.getInstance().calculate(itemPedidoRelacionado, pedido);
    	ItemPedidoService.getInstance().validate(itemPedidoRelacionado);
		CadItemPedidoForm.validateItemPedidoUI(itemPedidoRelacionado, pedido);
		ItemPedidoService.getInstance().insert(itemPedidoRelacionado);
		pedido.itemPedidoList.addElement(itemPedidoRelacionado);
		PedidoService.getInstance().calculate(pedido);

    }

    private void atualizaListaItensPedido(final Produto produto) throws SQLException {
    	if (MainLavenderePda.getInstance().getActualForm() instanceof CadItemPedidoForm) {
			CadItemPedidoForm cadItemPedidoForm = (CadItemPedidoForm)MainLavenderePda.getInstance().getActualForm();
			cadItemPedidoForm.list();
			cadItemPedidoForm.atualizaProdutoNaGrid(produto, true);
		}
    }

}
