package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoSimilarService;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoSimilarWindow extends LavendereWmwListWindow {

	private LabelName lbEstoque;
	private LabelName lbPreco;
	private LabelValue lvDsEstoque;
	private LabelValue lvDsPreco;
	private LabelValue lvDsProduto;
	public Produto produto;
	public boolean inRelatorioMode;
	private CadItemPedidoForm cadItemPedidoForm;
	private Vector produtoSimilarList;
	private boolean isSelectedItem = false;
	private String cdTabelaPreco;
	private TabelaPrecoComboBox cbTabelaPreco;
	private UnidadeFederalComboBox cbUf;
	private BaseScrollContainer produtoContainer;
	private SessionContainer containerDadosProduto;
	private Pedido pedido;
	private ItemPedido itemPedido;

	public ListProdutoSimilarWindow(Produto produto, boolean inRelatorioMode, String cdTabelaPreco, String uf) throws SQLException {
		this(produto, inRelatorioMode, cdTabelaPreco, uf, null);
	}

	public ListProdutoSimilarWindow(Produto produto, boolean inRelatorioMode, String cdTabelaPreco, String uf, ItemPedido itemPedido) throws SQLException {
		super(Messages.PRODUTO_LABEL_PRODUTOS_SIMILARES);
		this.produto = produto;
		this.inRelatorioMode = inRelatorioMode;
		this.itemPedido = itemPedido;
		pedido = itemPedido != null ? itemPedido.pedido : null;
		lbPreco = new LabelName(Messages.PRODUTO_LABEL_PRECO);
		cbUf = new UnidadeFederalComboBox();
		if (LavenderePdaConfig.usaPrecoPorUf) {
			cbUf.carregaUf();
			cbUf.setSelectedIndex(0);
			cbUf.setValue(uf);
		}
		cbTabelaPreco = new TabelaPrecoComboBox();
		if (inRelatorioMode) {
			cbTabelaPreco.loadForListProduto();
			cbTabelaPreco.setSelectedIndex(0);
			this.cdTabelaPreco = cbTabelaPreco.getValue();
		} else {
			this.cdTabelaPreco = cdTabelaPreco;
		}
		singleClickOn = !inRelatorioMode;
		produtoContainer = new BaseScrollContainer(true, false);
		produtoContainer.setBackForeColors(ColorUtil.sessionContainerBackColor, ColorUtil.sessionContainerForeColor);
		containerDadosProduto = new SessionContainer();
		lvDsProduto = new LabelValue(produtoToString(this.produto, null, " - "));
		lvDsProduto.setForeColor(ColorUtil.sessionContainerForeColor);
		if (!LavenderePdaConfig.isOcultaEstoque()) {
			lbEstoque = new LabelName(Messages.ESTOQUE_NOME_ENTIDADE);
			final double qtEstoque = EstoqueService.getInstance().getQtEstoque(produto.cdProduto, getPedidoLocalEstoque());
			final String dsEstoque = EstoqueService.getInstance().getEstoqueToString(qtEstoque);
			lvDsEstoque = new LabelValue(dsEstoque);
		}
		lvDsPreco = new LabelValue(findPrecoProduto(this.produto));
		constructorListContainer(getItemCount());
		setDefaultRect();
		canDrag = true;
		scrollable = false;
	}

	private int getItemCount() {
		int itemCount = 2;
		itemCount += ((LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() && !inRelatorioMode) || (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() && inRelatorioMode)) && !LavenderePdaConfig.isOcultaEstoque() ? 1 : 0;
		itemCount += !LavenderePdaConfig.isOcultaInfoPreco()
				&& (LavenderePdaConfig.mostraColunaPrecoNaListaDeProdutos.equals(ValueUtil.VALOR_SIM)
						|| (("2".equals(LavenderePdaConfig.mostraColunaPrecoNaListaDeProdutos) && !inRelatorioMode))
						|| (("1".equals(LavenderePdaConfig.mostraColunaPrecoNaListaDeProdutos) && inRelatorioMode))) ? 1 : 0;
		itemCount += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
		itemCount += LavenderePdaConfig.mostraPrecoUnidadeItem ? 1 : 0;
		return itemCount;
	}

	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ProdutoSimilarService.getInstance();
	}

    private void constructorListContainer(int itemCount) {
		listContainer = new GridListContainer(itemCount, 2, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
		if (itemCount > 2) {
			listContainer.setColPosition(2, LEFT);
		}
		if (itemCount > 3) {
			listContainer.setColPosition(3, RIGHT);
		}
    	listContainer.setUseSortMenu(false);
    	listContainer.resizeable = true;

    }

    @Override
	protected BaseDomain getDomainFilter() {
    	return new ProdutoSimilar();
    }

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		ProdutoSimilar produtoSimilarFilter = new ProdutoSimilar();
		produtoSimilarFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoSimilarFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoSimilar.class);
		produtoSimilarFilter.cdProduto = produto.cdProduto;
		produtoSimilarFilter.cdAgrupProdSimilar = produto.cdAgrupProdSimilar;
		produtoSimilarList = new Vector();
		if (LavenderePdaConfig.usaAgrupadorProdutoSimilar && ValueUtil.isEmpty(produtoSimilarFilter.cdAgrupProdSimilar)) {
			return produtoSimilarList;
		}
		if (produto.cdProduto != null) {
			produtoSimilarList = getProdutoSimilarValido(produtoSimilarFilter);
			produtoSimilarList.qsort();
		}
		if (!LavenderePdaConfig.usaAgrupadorProdutoSimilar && pedido != null && ValueUtil.isNotEmpty(produtoSimilarList)) {
			if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
				Cliente clienteFilter = pedido.getCliente();
				clienteFilter.cdRepresentante = produtoSimilarFilter.cdRepresentante;
				produtoSimilarList = RestricaoService.getInstance().filtraProdutosPorRestricao(produtoSimilarList, clienteFilter, null, 1);
			}
			if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
				ProdutoService.getInstance().filtraSugestaoVendaPorProdutoClienteExclusivo(pedido, produtoSimilarList);
			}
			if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
				ProdutoService.getInstance().filtraSugestaoVendaPorClienteProdutoExclusivo(pedido, produtoSimilarList);
			}
			if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
				ProdutoService.getInstance().filtraSugestaoVendaPorProdutoCondPagtoExclusivo(pedido, produtoSimilarList);
			}
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			produtoSimilarList = ItemPedidoAgrSimilarService.getInstance().filtraProdutosSimilaresInseridosAgrupadorSimilaridade(produtoSimilarList, pedido);
		}
		return produtoSimilarList;
	}

	private Vector getProdutoSimilarValido(ProdutoSimilar produtoSimilarFilter) throws SQLException {
		Vector finalList;
		if (LavenderePdaConfig.usaAgrupadorProdutoSimilar) {
			produtoSimilarFilter.cdClienteFilter =  inRelatorioMode ? null : SessionLavenderePda.getCliente().cdCliente;
			finalList = ProdutoService.getInstance().findAllProdutoSimilarByAgrupador(produtoSimilarFilter, pedido);
		} else {
			finalList = ProdutoSimilarService.getInstance().findAllByExample(produtoSimilarFilter);
		}
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && SessionLavenderePda.getCliente() != null && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd) && !inRelatorioMode) {
			finalList = GrupoCliPermProdService.getInstance().restringeProdutoSimilarByGrupoCliPermProd(finalList);
		}
		if (ValueUtil.isNotEmpty(finalList)) {
			for (ProdutoSimilar produtoSimilar : VectorUtil.iterateOver(finalList, ProdutoSimilar.class)) {
				if (isValido(produtoSimilar)) {
					produtoSimilarList.addElement(produtoSimilar);
				}
			}
		}
		return produtoSimilarList;
	}
	
	private boolean isValido(ProdutoSimilar produtoSimilar) throws SQLException {
		return (LavenderePdaConfig.usaProdutoSimilarProdutoInexistente || isProdutoComPreco(produtoSimilar)) && (LavenderePdaConfig.usaAgrupadorProdutoSimilar || isRestrito());
	}

	private boolean isRestrito() throws SQLException {
		return RestricaoService.getInstance().isProdutoRestrito(produto.cdProduto, inRelatorioMode ? null : SessionLavenderePda.getCliente().cdCliente, null, 1) == null;
	}

	private boolean isProdutoComPreco(ProdutoSimilar produtoSimilar) throws SQLException {
		return produtoSimilar.getProduto() != null && (inRelatorioMode || findPrecoProdutoDouble(produtoSimilar.getProduto()) > 0);
	}

	private StringBuilder infos = new StringBuilder();

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ProdutoSimilar produtoSimilar = (ProdutoSimilar)domain;
		final String separador = " - ";
		String preco = separador;
		infos.setLength(0);
		Vector itens = new Vector(0);
		infos.append(produtoToString(null, produtoSimilar, separador));
		itens.addElement(infos.toString());
		infos.setLength(0);
		itens.addElement("");
		boolean mostraEstoque = ((LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() && !inRelatorioMode) || (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido() && inRelatorioMode)) && !LavenderePdaConfig.isOcultaEstoque();
		if (mostraEstoque || LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
			produtoSimilar.qtEstoque = EstoqueService.getInstance().getQtEstoque(produtoSimilar.cdProdutoSimilar, getPedidoLocalEstoque());
			if (mostraEstoque) {
				infos.append(Messages.ESTOQUE_NOME_ENTIDADE).append(" ");
				infos.append(produtoSimilar.getProduto() == null ? separador : EstoqueService.getInstance().getEstoqueToString(produtoSimilar.qtEstoque));
				itens.addElement(infos.toString());
				infos.setLength(0);
			}
		}
		double precoSimilar = -1;
		if (!LavenderePdaConfig.isOcultaInfoPreco()
				&& (LavenderePdaConfig.mostraColunaPrecoNaListaDeProdutos.equals(ValueUtil.VALOR_SIM))
					|| (("2".equals(LavenderePdaConfig.mostraColunaPrecoNaListaDeProdutos) && !inRelatorioMode))
					|| (("1".equals(LavenderePdaConfig.mostraColunaPrecoNaListaDeProdutos) && inRelatorioMode))) {
			if (produtoSimilar.getProduto() != null) {
				precoSimilar = findPrecoProdutoDouble(produtoSimilar.getProduto());
				if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
					itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_RS, StringUtil.getStringValueToInterface(precoSimilar)));
				} else {
					preco = formataPrecoProduto(precoSimilar);
					infos.append(lbPreco.getValue() + preco);
					itens.addElement(infos.toString());
					infos.setLength(0);
				}
			} else {
				infos.append(lbPreco.getValue() + preco);
				itens.addElement(infos.toString());
				infos.setLength(0);
			}
		}
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			infos.append(Messages.PRODUTO_LABEL_PRINCIPIOATIVO).append(": ").append(StringUtil.getStringValue(produtoSimilar.getProduto() != null ? produtoSimilar.getProduto().dsPrincipioAtivo : ""));
			itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(infos.toString()), (int)(width * 0.6), listContainer.getFontSubItens()));
			infos.setLength(0);
		}
		if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
			double nuConversaoUnidadesMedida = produtoSimilar.getProduto() != null ? produtoSimilar.getProduto().nuConversaoUnidadesMedida : 1;
			if (nuConversaoUnidadesMedida == 0) {
				nuConversaoUnidadesMedida = 1;
			}
			if (precoSimilar == -1) {
				precoSimilar = findPrecoProdutoDouble(produtoSimilar.getProduto());
			}
			itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_UNID_RS, StringUtil.getStringValueToInterface(precoSimilar / nuConversaoUnidadesMedida)));
		}
		return (String[]) itens.toObjectArray();
	}

	private String produtoToString(Produto produto, ProdutoSimilar produtoSimilar, final String separador) throws SQLException {
		StringBuffer strb = new StringBuffer();
		if (!LavenderePdaConfig.ocultaColunaCdProduto) {
			if (produtoSimilar != null) {
				strb.append(produtoSimilar.cdProdutoSimilar + separador).append(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produtoSimilar.getProduto()));
			} else {
				strb.append(produto.cdProduto + separador).append(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto));
			}
		} else {
			if (produtoSimilar != null) {
				strb.append(StringUtil.getStringValue(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produtoSimilar.getProduto())));
			} else {
				strb.append(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto));
			}
		}
		return strb.toString();
	}

    @Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    @Override
	protected String getToolTip(BaseDomain domain) throws java.sql.SQLException {
    	return ProdutoService.getInstance().getDescricaoProdutoComReferencia(((ProdutoSimilar)domain).getProduto());
    }

    @Override
	protected void onFormStart() {
    	UiUtil.add(this, produtoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		UiUtil.add(produtoContainer, lvDsProduto, getLeft(), getNextY(), getWFill(), PREFERRED);
    	if (!LavenderePdaConfig.isOcultaEstoque() || !inRelatorioMode || !LavenderePdaConfig.isOcultaInfoPreco()) {
    		UiUtil.add(this, containerDadosProduto, LEFT, AFTER, FILL, UiUtil.getControlPreferredHeight() + HEIGHT_GAP_BIG);
    	}
    	int pos = getLeft();
    	if (!LavenderePdaConfig.isOcultaEstoque()) {
    		UiUtil.add(containerDadosProduto, lbEstoque, getLeft(), CENTER);
			UiUtil.add(containerDadosProduto, lvDsEstoque, AFTER + WIDTH_GAP_BIG, SAME);
			pos = getRight();
		}
    	if (!inRelatorioMode || !LavenderePdaConfig.isOcultaInfoPreco()) {
    		UiUtil.add(containerDadosProduto, pos == getRight() ? lvDsPreco : lbPreco, pos, CENTER);
    		UiUtil.add(containerDadosProduto, pos == getRight() ? lbPreco   : lvDsPreco, pos == getRight() ? BEFORE - WIDTH_GAP_BIG : AFTER + WIDTH_GAP_BIG, SAME);
    	}
    	if (inRelatorioMode) {
			UiUtil.add(this, new LabelName(Messages.TABELA_PRECO), cbTabelaPreco, getLeft(), getNextY());
    		if (LavenderePdaConfig.usaPrecoPorUf) {
				UiUtil.add(this, new LabelName(Messages.UF), cbUf, SAME, getNextY());
    		}
    	}
		UiUtil.add(this, listContainer, LEFT, inRelatorioMode ? getNextY() : AFTER, FILL, FILL);
    	if (listContainer.getHeight() <= UiUtil.getControlPreferredHeight() * 5) {
    		listContainer.resetSetPositions();
    		listContainer.setRect(LEFT, AFTER, FILL, UiUtil.getControlPreferredHeight() * 8, containerDadosProduto);
    		listContainer.reposition();
    	}
    }
    
    protected String findPrecoProduto(Produto prod) throws SQLException {
		return formataPrecoProduto(findPrecoProdutoDouble(prod));
    }
    
    protected String formataPrecoProduto(double valor) throws SQLException {
		String preco = " - ";
		 if (valor != 0) {
			 preco = "  " + Messages.MOEDA + " " + StringUtil.getStringValueToInterface(valor);
		 }
    	return preco;
    }
    
	protected double findPrecoProdutoDouble(Produto prod) throws SQLException {
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			if (prod.itemTabelaPreco == null || !ValueUtil.valueEquals(cdTabelaPreco, produto.itemTabelaPreco.cdTabelaPreco)) {
				if (itemPedido != null && LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
					prod.itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoPrazoPagtoPreco(cdTabelaPreco, prod.cdProduto, cbUf.getValue(), itemPedido.cdPrazoPagtoPreco);
				} else {
					prod.itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cdTabelaPreco, prod.cdProduto, cbUf.getValue());
				}
			}
			if (LavenderePdaConfig.realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido && prod.itemTabelaPreco != null && pedido != null) {
				return ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, prod, cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false);
			} else if (prod.itemTabelaPreco != null && prod.itemTabelaPreco.vlUnitario > 0) {
				double precoProduto = prod.itemTabelaPreco.vlUnitario;
				if (LavenderePdaConfig.usaIndiceFinanceiroSupRep && pedido == null) {
					precoProduto = ItemTabelaPrecoService.getInstance().aplicaIndiceFinanceiroSupRep(prod.vlBrutoItem);
				}
				return precoProduto;
			}
		}
		return 0;
	}
	
	private String getPedidoLocalEstoque() throws SQLException {
		return pedido == null ? Estoque.CD_LOCAL_ESTOQUE_PADRAO : pedido.getCdLocalEstoque();
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

    private Produto getSelectedProduto() throws SQLException {
		if (!LavenderePdaConfig.usaAgrupadorProdutoSimilar) {
			ProdutoSimilar produtoSimilar = (ProdutoSimilar) ProdutoSimilarService.getInstance().findByRowKey(listContainer.getSelectedId());
			return produtoSimilar.getProduto();
		}
		ProdutoSimilar produtoSimilarFilter = new ProdutoSimilar();
	    produtoSimilarFilter.rowKey = listContainer.getSelectedId();
	    return ProdutoService.getInstance().getProduto(produtoSimilarFilter.getCdProdutoSimilarFromRowKey());
    }

	@Override
	public void detalhesClick() throws SQLException {
		if (!inRelatorioMode && pedido!=null && pedido.isPedidoAberto()) {
			Produto selectedProduto = getSelectedProduto();
			if (selectedProduto != null) {
				if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, selectedProduto)) {
					UiUtil.showWarnMessage(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
					return;
				}
				isSelectedItem = true;
	    		cadItemPedidoForm.setProdutoSelecionado(selectedProduto);
	    		cadItemPedidoForm.fromSimilar = true;
	    		cadItemPedidoForm.add();
	    		fecharWindow();
	    		if (cadItemPedidoForm.inVendaUnitariaMode) {
	    			cadItemPedidoForm.fromWindowProdutoSimilar = true;
			    	cadItemPedidoForm.gridClick();
			    	cadItemPedidoForm.setFocusInQtde();
			    } else {
			    	cadItemPedidoForm.fromWindowProdutoSimilar = true;
			    	cadItemPedidoForm.gridClickAndRepaintScreen();
			    	cadItemPedidoForm.fromWindowProdutoSimilar = false;
			    }
			}
		}
	}

	@Override
	protected void fecharWindow() throws SQLException {
		if (cadItemPedidoForm != null) {
			cadItemPedidoForm.listProdutoSimilarInicialized = false;
		}
		super.fecharWindow();
		if (cadItemPedidoForm != null) {
			cadItemPedidoForm.setFocusInQtde();
		}
	}

	@Override
	public void popup() {
		if (cadItemPedidoForm != null) {
			cadItemPedidoForm.listProdutoSimilarInicialized = true;
		}
		if (ValueUtil.isEmpty(produtoSimilarList) && inRelatorioMode) {
			UiUtil.showInfoMessage(Messages.PRODUTO_MSG_NAO_POSSUI_PRODUTOS_SIMILARES);
		} else {
			super.popup();
		}
	}

	public boolean possuiProdutoSimilares() {
		return ValueUtil.isNotEmpty(produtoSimilarList);
	}

	public boolean isSelectedItem() {
		return isSelectedItem;
	}

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) {
		ProdutoSimilar produtoSimilar = (ProdutoSimilar)domain;
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
			if (produtoSimilar.qtEstoque <= 0) {
				c.setBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK);
			}
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbTabelaPreco) {
					cbTabelaPrecoChange();
				} else if (event.target == cbUf) {
					cbUfChange();
				}
				break;

			default:
				break;
		}
	}

	private void cbTabelaPrecoChange() throws SQLException {
		cdTabelaPreco = cbTabelaPreco.getValue();
		lvDsPreco.setValue(findPrecoProduto(produto));
		list();
		reposition();
	}

	private void cbUfChange() throws SQLException {
		lvDsPreco.setValue(findPrecoProduto(produto));
		list();
		reposition();
	}

	@Override
	public void screenResized() {
		super.screenResized();
		reposition();
		listMaximized = false;
	}

}
