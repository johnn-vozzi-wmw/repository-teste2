package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaProdService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.SugestaoVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Convert;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoSugestaoSemQtdeWindow extends WmwListWindow {

	private LabelName lbDescricao;
	private LabelValue lbMsgFechamento;
	public Pedido pedido;
	private CadItemPedidoForm cadItemPedidoForm;
	public boolean onFechamentoPedido;
	public boolean onAcessoManual;
	private ButtonPopup btCancelar;
	private ButtonPopup btFecharPedido;
	private SugestaoVendaComboBox cbSugestaoVenda;
	private SessionContainer scItensInserids;
	private LabelTotalizador lbItensInseridos;
	private LabelTotalizador lbPctItensInseridos;
	private boolean permiteFecharPedido = true;
	private Vector estoqueCache;

	public ListProdutoSugestaoSemQtdeWindow(Pedido pedido, boolean onFechamentoPedido) throws SQLException {
		super(Messages.PEDIDO_LABEL_SUGESTAO_VENDAS);
		this.pedido = pedido;
		this.onFechamentoPedido = onFechamentoPedido;
		singleClickOn = true;
		lbDescricao = new LabelName(Messages.SUGESTAO_VENDAS_DESCRICAO);
		cbSugestaoVenda = new SugestaoVendaComboBox(this.pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, onFechamentoPedido);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
		lbMsgFechamento = new LabelValue("", CENTER);
		scItensInserids = new SessionContainer();
		scItensInserids.setBackColor(ColorUtil.componentsBackColor);
		lbItensInseridos = new LabelTotalizador(Messages.SUGESTAO_ITENS_INSERIDOS);
		lbPctItensInseridos = new LabelTotalizador("");
		estoqueCache = new Vector();
        constructorListContainer();
        loadComboSugestaoVendasAndSelectTodos();
		setDefaultRect();
	}


	protected CrudService getCrudService() throws java.sql.SQLException {
		return SugestaoVendaProdService.getInstance();
	}

    private void constructorListContainer() {
    	int itemCount = 3;
    	itemCount += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
        itemCount += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
    	itemCount += LavenderePdaConfig.mostraColunaMarcaNaSugestaoDeProdutos() ? 1 : 0;
    	itemCount += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
		listContainer = new GridListContainer(itemCount, 2);
    	setPositionsListContainer(itemCount);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }


	private void setPositionsListContainer(int itemCount) {
		for (int i = itemCount-1; i <= itemCount; i--) {
    		if (i % 2 > 0) {
    			listContainer.setColPosition(i, RIGHT);
    		} else if (i == 0) {
    			break;
    		}
    	}
	}

    public void loadComboSugestaoVendasAndSelectTodos() throws SQLException {
    	cbSugestaoVenda.load(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, onFechamentoPedido);
    	cbSugestaoVenda.setSelectedIndex(0);
    }

    protected BaseDomain getDomainFilter() {
    	return new SugestaoVendaProd();
    }

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		Vector sugestaoVendaProdList = new Vector(0);
		if (isComboAllSelected()) {
			sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdSemQtdPendenteNoPedido(cbSugestaoVenda.getSugestoesVendaSemQtde(), pedido);
		} else {
			sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdSemQtdPendenteNoPedido((SugestaoVenda)cbSugestaoVenda.getSelectedItem(), pedido);
		}
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
			sugestaoVendaProdList = GrupoCliPermProdService.getInstance().restringeSugestaoVendaProdByGrupoCliPermProd(sugestaoVendaProdList);
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			sugestaoVendaProdList = ProdutoService.getInstance().filtraSugestaoVendaPorTipoPedido(pedido, sugestaoVendaProdList);
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			ProdutoService.getInstance().filtraSugestaoVendaPorProdutoClienteExclusivo(pedido, sugestaoVendaProdList);
		}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
			ProdutoService.getInstance().filtraSugestaoVendaPorClienteProdutoExclusivo(pedido, sugestaoVendaProdList);
		}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
			ProdutoService.getInstance().filtraSugestaoVendaPorProdutoCondPagtoExclusivo(pedido, sugestaoVendaProdList);
		}
		SugestaoVendaProdService.getInstance().adicionaPrecoNaSugestaoVendaProduto(sugestaoVendaProdList, pedido);
		SortUtil.qsortInt(sugestaoVendaProdList.items, 0, sugestaoVendaProdList.size()-1, false);
		return sugestaoVendaProdList;
	}

	public void list(Pedido pedido) throws SQLException {
		this.pedido = pedido;
		list();
	}

	public void list() throws java.sql.SQLException {
		super.list();
		lbPctItensInseridos.setValue(calculaPctItensInseridos());
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
    	if (domain.getClass() == SugestaoVendaProd.class) {
    		SugestaoVendaProd prod = (SugestaoVendaProd) domain;
        	return  prod.dsTooltip;
    	}else {
    		return super.getToolTip(domain);
    	}
    }
	
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd)domain;
		int sizeVector = 3;
		sizeVector += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
		sizeVector += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
		sizeVector += LavenderePdaConfig.mostraColunaMarcaNaSugestaoDeProdutos() ? 1 : 0;
		sizeVector += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
		Vector itens = new Vector(sizeVector);
		String dsReferencia = LavenderePdaConfig.isMostraDescricaoReferencia() ? "[" + ProdutoService.getInstance().getDsReferencia(sugestaoVendaProd.cdProduto) + "]" : "";
		dsReferencia = ValueUtil.isNotEmpty(dsReferencia) && LavenderePdaConfig.isMostraDescricaoReferenciaAposDsProduto() ? " - " + dsReferencia : dsReferencia;
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades) {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement(dsReferencia + " - " + ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto).toString());
			} else {
				itens.addElement(ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto).toString() + dsReferencia);
			}
		} else {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement(dsReferencia + " - " + sugestaoVendaProd.cdProduto + " - ");
			} else { 
				itens.addElement(sugestaoVendaProd.cdProduto + " - "+ProdutoService.getInstance().getDsProduto(sugestaoVendaProd.cdProduto) + dsReferencia);
			}
		}
		itens.addElement("");
		String dsSugestaoVenda = SugestaoVendaService.getInstance().getDsSugestaoVenda(sugestaoVendaProd.cdSugestaoVenda);
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(ProdutoService.getInstance().getDsPrincipioAtivo(sugestaoVendaProd.cdProduto)), (int)(width * 0.6), listContainer.getFontSubItens()));
    	}
		itens.addElement(dsSugestaoVenda);
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			if (sugestaoVendaProd.produto != null) {
				if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
					itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValue(sugestaoVendaProd.vlPrecoProduto));
				}
				if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
					itens.addElement(EstoqueService.getInstance().getEstoqueToString(getEstoqueProduto(sugestaoVendaProd.produto)) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
				}
			}
		}
		if (LavenderePdaConfig.mostraColunaMarcaNaSugestaoDeProdutos()) {
			itens.addElement((ValueUtil.isNotEmpty(sugestaoVendaProd.produto.dsMarca)) ? sugestaoVendaProd.produto.dsMarca : "");
		}
		sugestaoVendaProd.dsTooltip = itens.elementAt(0).toString();
		return (String[]) itens.toObjectArray();
	}
	
	//@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) domain;
		// PRODUTOS SEM ESTOQUE EM VERMELHO
		if (sugestaoVendaProd.vlPrecoProduto == 0) {
			c.setBackColor(LavendereColorUtil.COR_GRID_PRODUTO_SEM_PRECO_SUGESTAO_VENDA);
		}
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
			if (getEstoqueProduto(sugestaoVendaProd.produto) <= 0) {
				c.setBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK);
			}
		}
	}

    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    private Produto getSelectedProduto() throws SQLException {
    	SugestaoVendaProd produto = (SugestaoVendaProd) SugestaoVendaProdService.getInstance().findByRowKey(listContainer.getSelectedId());
    	return ProdutoService.getInstance().getProduto(produto.cdProduto);
    }
    
    private double getEstoqueProduto(Produto produto) throws SQLException {
		for (int i = 0; i < estoqueCache.size(); i++) {
			Estoque est = (Estoque) estoqueCache.items[i];
			if (ValueUtil.isNotEmpty(produto.cdProduto) && produto.cdProduto.equals(est.cdProduto)) {
				return est.qtEstoque;
			}
		}
		Estoque estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
		if (ValueUtil.isEmpty(estoque.cdProduto)) {
			Estoque estoqueVazio = new Estoque();
			estoqueVazio.cdProduto = produto.cdProduto;
			estoqueVazio.qtEstoque = 0;
			estoqueCache.addElement(estoqueVazio);
		} else {
			estoqueCache.addElement(estoque);
		}
		return estoque.qtEstoque;
	}
    
	protected void onFormStart() {
		int yTop = getTop() + HEIGHT_GAP;
		if (onFechamentoPedido) {
			UiUtil.add(this, lbMsgFechamento, LEFT, yTop);
			yTop = AFTER + HEIGHT_GAP;
		}
		UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, yTop);
		UiUtil.add(this, cbSugestaoVenda, AFTER + WIDTH_GAP_BIG, SAME);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - scItensInserids.getPreferredHeight() + HEIGHT_GAP);
		UiUtil.add(this, scItensInserids, LEFT, BOTTOM + 1, FILL, PREFERRED);
		UiUtil.add(scItensInserids, lbItensInseridos, LEFT + UiUtil.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
		UiUtil.add(scItensInserids, lbPctItensInseridos, AFTER + HEIGHT_GAP, SAME, FILL, FILL);
		if (onFechamentoPedido) {
			addButtonPopup(btFecharPedido);
			addButtonPopup(btCancelar);
		}
		ajustaComponents();
	}
	
	public void reposition() {
		super.reposition();
		ajustaComponents();
	}

	private void ajustaComponents() {
		int yTop = getTop() + HEIGHT_GAP;
		if (onFechamentoPedido) {
			lbMsgFechamento.setText(Convert.insertLineBreak(width - 6, lbMsgFechamento.fm, Messages.SUGESTAO_MSG_FECHAMENTO_PEDIDO));
			lbMsgFechamento.setRect(CENTER, yTop, width, PREFERRED);
			yTop = AFTER + HEIGHT_GAP;
		}
		lbDescricao.setRect(LEFT + WIDTH_GAP, yTop, PREFERRED, UiUtil.getControlPreferredHeight());
		cbSugestaoVenda.setRect(AFTER + WIDTH_GAP_BIG, SAME, FILL - 2, UiUtil.getControlPreferredHeight());
		listContainer.reposition();
		listContainer.setRect(LEFT, cbSugestaoVenda.getY2() + HEIGHT_GAP, FILL, FILL);
	}

	protected void addBtFechar() {
		if (!onFechamentoPedido) {
			super.addBtFechar();
		}
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					fecharWindow();
				} else if (event.target == cbSugestaoVenda) {
					list();
				} else if (event.target == btFecharPedido) {
					permiteFecharPedido = true;
					if (SugestaoVendaService.getInstance().isHasSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true, true)) {
						if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
							permiteFecharPedido = PedidoUiUtil.showPopupLiberacaoSenhaSugestaoVendaObrigatoria(pedido, cbSugestaoVenda.getSugestoesVendaSemQtde(), SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, false);
						} else {
							throw new ValidationException(Messages.VALIDACAO_FECHAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA);
						}
					}
					if (permiteFecharPedido) {
						fecharWindow();
						if (MainLavenderePda.getInstance().getActualForm() instanceof CadPedidoForm) {
							CadPedidoForm cadPedidoForm = (CadPedidoForm)MainLavenderePda.getInstance().getActualForm();
							cadPedidoForm.getPedido().ignoraValidacaoSugestaoProdutosSemQtde = true;
							cadPedidoForm.showMessageConfirmClosePedido = false;
							cadPedidoForm.fecharPedido();
						}
					}
				}
				break;
			}
		}
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	public void detalhesClick() throws SQLException {
		Produto produto = getSelectedProduto();
		if (ValueUtil.isEmpty(produto.dsProduto)) {
			UiUtil.showErrorMessage(Messages.SUGESTAO_MSG_PRODUTO_NAO_ENCONTRADO);
			return;
		}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, produto)) {
			UiUtil.showWarnMessage(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
			return;
		}
		cadItemPedidoForm.produtoSelecionado = produto;
		unpop();
		cadItemPedidoForm.gridClickAndRepaintScreen();
		cadItemPedidoForm.fromWindowSugestaoProduto = true;
		cadItemPedidoForm.setFocusInQtde();
	}

	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		if (onFechamentoPedido) {
			cadItemPedidoForm.voltarClick();
		}
		if ((!LavenderePdaConfig.isUsaRamoAtividadeSugestaoComQtdMinima() || pedido.getCliente().isPossuiRamoAtividade()) && !onAcessoManual && !onFechamentoPedido) {
			ListProdutoSugestaoComQtdeWindow listProdutoSugestaoComQtdeWindow = cadItemPedidoForm.getListProdutoSugestaoComQtdeWindow(false, false);
			if (listProdutoSugestaoComQtdeWindow.hasSugestaoVenda()) {
				listProdutoSugestaoComQtdeWindow.popup();
			}
		}
	}

	public boolean hasSugestaoVenda() {
		return listContainer != null && listContainer.size() > 0;
	}

	private String calculaPctItensInseridos() throws SQLException {
		double pctInseridos = 0;
		Vector sugestaoProdutosList = new Vector();
		if (isComboAllSelected()) {
			sugestaoProdutosList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdBySugestaoVendaList(cbSugestaoVenda.getSugestoesVendaSemQtde());
		} else {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) (SugestaoVenda) cbSugestaoVenda.getSelectedItem();
			sugestaoProdutosList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdBySugestaoVenda(sugestaoVenda, pedido);
		}
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
			sugestaoProdutosList = GrupoCliPermProdService.getInstance().restringeSugestaoVendaProdByGrupoCliPermProd(sugestaoProdutosList);
		}
		double totalProdutos = sugestaoProdutosList.size();
		double produtosInseridos = totalProdutos - listContainer.size();
		if (totalProdutos > 0) {
			pctInseridos = (produtosInseridos / totalProdutos) * 100;
		}
		return StringUtil.getStringValue(ValueUtil.getIntegerValue(produtosInseridos)) + "/" + ValueUtil.getIntegerValue(totalProdutos) + " (" + StringUtil.getStringValueToInterface(pctInseridos, 2) + "%)";
	}

    public boolean isComboAllSelected() {
    	return cbSugestaoVenda.getSelectedIndex() == 0;
    }

	//@Override
	protected void onUnpop() {}

}
