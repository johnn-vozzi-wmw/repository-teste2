package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
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
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaGrupo;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto3Service;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaGrupoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaProdService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoSugestaoGrupoComQtdeWindow extends LavendereWmwListWindow {

	private LabelName lbGrupoProduto1;
	private LabelName lbGrupoProduto2;
	private LabelName lbGrupoProduto3;
    private LabelValue lbDsGrupoProduto1;
    private LabelValue lbDsGrupoProduto2;
    private LabelValue lbDsGrupoProduto3;
    private LabelValue lbMixProduto;
    private LabelValue lbQtdeProduto;
	public Pedido pedido;
	public SugestaoVendaGrupo sugestaoVendaGrupo;
	public SugestaoVenda sugestaoVenda;
	public Produto selectedProduto;
	private boolean isFromMultiplasSugestoesWindow;

	public ListProdutoSugestaoGrupoComQtdeWindow(Pedido pedido, SugestaoVendaGrupo sugestaoVendaGrupo, SugestaoVenda sugestaoVenda) throws SQLException {
		this(pedido, sugestaoVendaGrupo, sugestaoVenda, false);
	}

	public ListProdutoSugestaoGrupoComQtdeWindow(Pedido pedido, SugestaoVendaGrupo sugestaoVendaGrupo, SugestaoVenda sugestaoVenda, boolean isFromMultiplasSugestoesWindow) throws SQLException {
		super(Messages.SUGESTAO_ESCOLHA_PRODUTO);
		this.pedido = pedido;
		this.sugestaoVenda = sugestaoVenda;
		this.sugestaoVendaGrupo = sugestaoVendaGrupo;
		this.isFromMultiplasSugestoesWindow = isFromMultiplasSugestoesWindow;
		singleClickOn = true;
        lbGrupoProduto1 = new LabelName(GrupoProduto1Service.getInstance().getLabelGrupoProduto1(), RIGHT);
        lbGrupoProduto2 = new LabelName(GrupoProduto2Service.getInstance().getLabelGrupoProduto2(), RIGHT);
        lbGrupoProduto3 = new LabelName(GrupoProduto3Service.getInstance().getLabelGrupoProduto3(), RIGHT);
        lbDsGrupoProduto1 = new LabelValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(sugestaoVendaGrupo.cdGrupoProduto1));
        lbDsGrupoProduto2 = new LabelValue(GrupoProduto2Service.getInstance().getDsGrupoProduto(sugestaoVendaGrupo.cdGrupoProduto1, sugestaoVendaGrupo.cdGrupoProduto2));
        lbDsGrupoProduto3 = new LabelValue(GrupoProduto3Service.getInstance().getDsGrupoProduto(sugestaoVendaGrupo.cdGrupoProduto1, sugestaoVendaGrupo.cdGrupoProduto2, sugestaoVendaGrupo.cdGrupoProduto3));
        lbMixProduto = new LabelValue(Messages.MIX + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtMixVendida) + "/" + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtMixProdutosVenda));
        lbQtdeProduto = new LabelValue(Messages.UNIDADES + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtVendida) + "/" + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtUnidadesVenda));
        sortAtributte = "CDPRODUTO";
        sortAsc = ValueUtil.VALOR_SIM;
        constructorListContainer();
		setRect(13);
	}


	protected CrudService getCrudService() throws java.sql.SQLException {
		return SugestaoVendaGrupoService.getInstance();
	}

    private void constructorListContainer() {
    	int itemCount = 2; 
    	itemCount += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
    	itemCount += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
    	itemCount += (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || (LavenderePdaConfig.isMostraDescricaoReferencia() && !LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto())) ? 2 : 0;
		listContainer = new GridListContainer(itemCount, 2);
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			listContainer.setColPosition(itemCount - 1, RIGHT);
		}
		configListContainer(LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? "CDPRODUTO" : "DSPRODUTO");
		listContainer.setColsSort(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO"}, {Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO"}});
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
    }

    protected BaseDomain getDomainFilter() {
    	return new Produto();
    }

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		Produto produtoExample = (Produto)domain;
		produtoExample.cdEmpresa = pedido.cdEmpresa;
		produtoExample.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoExample.cdGrupoProduto1 = sugestaoVendaGrupo.cdGrupoProduto1;
		produtoExample.cdGrupoProduto2 = GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto2) ? "" : sugestaoVendaGrupo.cdGrupoProduto2;
		produtoExample.cdGrupoProduto3 = GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto3) ? "" : sugestaoVendaGrupo.cdGrupoProduto3;
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			produtoExample.flExcecaoProduto = ValueUtil.isNotEmpty(pedido.getTipoPedido().flExcecaoProduto) ? pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
			produtoExample.cdTipoPedidoFilter = pedido.cdTipoPedido;
		}
		Vector produtoList = ProdutoService.getInstance().findAllByExampleSummary(produtoExample);
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
			produtoList = GrupoCliPermProdService.getInstance().restringeProdutosByGrupoCliPermProd(SessionLavenderePda.getCliente().cdGrupoPermProd, produtoList);
		}
		SugestaoVendaProdService.getInstance().removeProdutosSemPreco(produtoList, pedido);
		Vector listFinal = new Vector();
		if (ValueUtil.isNotEmpty(produtoList)) {
			forSugestoes: for (int i = 0; i < produtoList.size(); i++) {
				Produto produto = (Produto)produtoList.items[i];
				for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
					if (produto.cdProduto.equals(((ItemPedido)pedido.itemPedidoList.items[j]).cdProduto)) {
						continue forSugestoes;
					}
				}
				produto.qtEstoqueProduto = EstoqueService.getInstance().getEstoqueProduto(produto);
				listFinal.addElement(produto);
			}
		}
		return listFinal;
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		Produto produto = (Produto)domain;
		int sizeVector = LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
		sizeVector += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
		Vector itens = new Vector(sizeVector);
		String dsReferencia = StringUtil.getStringValue(LavenderePdaConfig.isMostraDescricaoReferencia() ? "[" + ProdutoService.getInstance().getDsReferencia(produto.cdProduto) + "]" : "");
		dsReferencia = StringUtil.getStringValue(ValueUtil.isNotEmpty(dsReferencia) && LavenderePdaConfig.isMostraDescricaoReferenciaAposDsProduto() ? " - " + dsReferencia : dsReferencia);
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades) {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement(dsReferencia + " - " + ProdutoService.getInstance().getProduto(produto.cdProduto).toString());
			} else {
				itens.addElement(produto.toString() + dsReferencia);
			}
			itens.addElement("");
		} else {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement(dsReferencia + " - " + produto.cdProduto + " - ");
			} else {
				itens.addElement(produto.cdProduto + " - ");
				itens.addElement(StringUtil.getStringValue(produto.dsProduto) + dsReferencia);
			}
		}
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsPrincipioAtivo), (int)(width * 0.6), listContainer.getFontSubItens()));
			itens.addElement("");
		} 
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
			itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(getPrecoProduto(produto)));
		}
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			itens.addElement(EstoqueService.getInstance().getEstoqueToString(produto.qtEstoqueProduto) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
		}
		return (String[]) itens.toObjectArray();
	}

    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    private Produto getSelectedProduto() throws SQLException {
    	return (Produto) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId());
    }
    
    private double getPrecoProduto(Produto produto) throws SQLException {
    	if (ValueUtil.isEmpty(produto.cdProduto)) return 0;
		
		return ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, ValueUtil.isNotEmpty(pedido.cdTabelaPreco) ? pedido.cdTabelaPreco : pedido.getCliente().cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false);
	}

	protected void onFormStart() {
		int widthMaiorLabel = getWidthMaiorLabelGrupo();
		UiUtil.add(this, lbGrupoProduto1, LEFT + WIDTH_GAP, TOP + HEIGHT_GAP, widthMaiorLabel, PREFERRED);
		UiUtil.add(this, lbDsGrupoProduto1, AFTER + WIDTH_GAP_BIG, SAME, FILL, PREFERRED);
		if (!GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto2)) {
			UiUtil.add(this, lbGrupoProduto2, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, widthMaiorLabel, PREFERRED);
			UiUtil.add(this, lbDsGrupoProduto2, AFTER + WIDTH_GAP_BIG, SAME, FILL, PREFERRED);
		}
		if (!GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto3)) {
			UiUtil.add(this, lbGrupoProduto3, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, widthMaiorLabel, PREFERRED);
			UiUtil.add(this, lbDsGrupoProduto3, AFTER + WIDTH_GAP_BIG, SAME, FILL, PREFERRED);
		}
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - UiUtil.getLabelPreferredHeight());
		if (!isFromMultiplasSugestoesWindow) {
			UiUtil.add(this, lbMixProduto, LEFT + WIDTH_GAP, BOTTOM, PREFERRED);
			UiUtil.add(this, lbQtdeProduto, RIGHT - WIDTH_GAP, BOTTOM, PREFERRED);
		}
	}
	
	//@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		if (isFromMultiplasSugestoesWindow) {
			// PRODUTOS SEM ESTOQUE EM VERMELHO
			if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !((Produto) domain).isIgnoraValidacao()) {
				if (((Produto)domain).qtEstoqueProduto <= 0) {
					c.setBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK);
				}
			}
			ProdutoBase produtoBase = (ProdutoBase) domain;
			//MOSTRA PRODUTO PROMOCIONAL DESTACADO
			if (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido() && (!LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional || !ProdutoService.getInstance().isProdutoHaveDescPromocional(produtoBase, pedido.cdTabelaPreco))) {
				if (destacaProdutoPromocionalItemTabPreco(c, produtoBase)) {
					c.setBackColor(LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK);
				}
			}
			//MOSTRA PRODUTO COM DESCONTO PROMOCIONAL DESTACADO
			if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido()) {
				if (destacaProdutoDescPromocional(c, produtoBase)) {
					c.setBackColor(LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK);
				}
			}
		}
		if (getPrecoProduto((Produto)domain) == 0) {
			c.setBackColor(LavendereColorUtil.COR_GRID_PRODUTO_SEM_PRECO_SUGESTAO_VENDA);
		}
	}

	private int getWidthMaiorLabelGrupo() {
		int widthFinal = 10;
		if (lbGrupoProduto1.getPreferredWidth() > widthFinal) {
			widthFinal = lbGrupoProduto1.getPreferredWidth();
		}
		if (!GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto2) && lbGrupoProduto2.getPreferredWidth() > widthFinal) {
			widthFinal = lbGrupoProduto2.getPreferredWidth();
		}
		if (!GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(sugestaoVendaGrupo.cdGrupoProduto3) && lbGrupoProduto3.getPreferredWidth() > widthFinal) {
			widthFinal = lbGrupoProduto3.getPreferredWidth();
		}
		return widthFinal;
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				break;
			}
		}
	}

	public void detalhesClick() throws SQLException {
		selectedProduto = getSelectedProduto();
		unpop();
	}

	//@Override
	protected void onUnpop() { }
	
	private boolean destacaProdutoPromocionalItemTabPreco(Item c, ProdutoBase produtoBase) throws SQLException {
		if (produtoBase instanceof ProdutoTabPreco) {
			String dsTabPrecoPromoList = ProdutoTabPrecoService.getInstance().getProdutoTabPrecoColumn(produtoBase.cdProduto, "dsTabPrecoPromoList");
			String[] tabelasPromocionais = StringUtil.split(dsTabPrecoPromoList, '|');
			Vector vector = new Vector(tabelasPromocionais);
			int index = vector.indexOf(pedido.cdTabelaPreco);
			return (index != -1) || (ValueUtil.isEmpty(pedido.cdTabelaPreco) && (tabelasPromocionais.length > 0));
		} else {
			return produtoBase.isProdutoPromocao();
		}
	}
	
	private boolean destacaProdutoDescPromocional(Item c, ProdutoBase produto) throws SQLException {
		String dsTabPrecoDescPromocionalList = ProdutoTabPrecoService.getInstance().getProdutoTabPrecoColumn(produto.cdProduto, "dsTabPrecoDescPromocionalList");
		String[] tabelasPromocionais = StringUtil.split(dsTabPrecoDescPromocionalList, '|');
		Vector vector = new Vector(tabelasPromocionais);
		int index = vector.indexOf(pedido.cdTabelaPreco);
		return (index != -1) || (ValueUtil.isEmpty(pedido.cdTabelaPreco) && (tabelasPromocionais.length > 0));
	}

}
