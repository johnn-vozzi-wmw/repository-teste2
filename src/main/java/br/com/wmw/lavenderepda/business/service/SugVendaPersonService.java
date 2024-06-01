package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugVendaPersonDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugVendaPersonProdDbxDao;
import totalcross.util.Vector;

public class SugVendaPersonService extends CrudService {
	
	private static SugVendaPersonService instance;
	
	public static SugVendaPersonService getInstance() {
		if (instance == null) {
			instance = new SugVendaPersonService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return SugVendaPersonDbxDao.getInstance();
	}
	
	public Vector findProdutosSugVendaPerson(Pedido pedido, Vector listaAtual) throws SQLException {
		int limiteProdutos = LavenderePdaConfig.qtLimiteProdutosSug;
		int qtInseridos = ItemPedidoService.getInstance().countItensSugInseridos(pedido);
		if (LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
			limiteProdutos -= qtInseridos;
		}
		ItemPedido.sortAttr = ProdutoBase.sortAttr = SugVendaPerson.getParamSortAttr();
		boolean asc = !SugVendaPerson.ORDEMDESC.equals(LavenderePdaConfig.sentidoOrdenacaoListaSugPerson);
		String dsOrdenacao = getDsOrdencaoSugPerson();
		dsOrdenacao += asc ? " ASC" : " DESC";
		String cdTabelaPrecoFilter = LavenderePdaConfig.ocultaTabelaPrecoPedido ? pedido.getCliente().cdTabelaPreco : pedido.cdTabelaPreco;
		Vector produtos = SugVendaPersonProdDbxDao.getInstance().getProdutosSugestoesPerson(pedido, null, pedido.getCliente().cdGrupoPermProd, cdTabelaPrecoFilter, dsOrdenacao);
		HashMap<String, Produto> produtosSelecionadosHashMap = new HashMap<>(limiteProdutos);
		List<Produto> produtosSelecionadosList = new ArrayList<>(limiteProdutos);
		int size = produtos.size();
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto)produtos.items[i];
			if (produtosSelecionadosHashMap.get(produto.cdProduto) == null) {
				produtosSelecionadosHashMap.put(produto.cdProduto, produto);
				produtosSelecionadosList.add(produto);
			}
			if (produtosSelecionadosHashMap.size() >= limiteProdutos) {
				break;
			}
		}
		Produto produtoSelecionado;
		HashMap<String, String> cdSugVendaPersonHashMap = new HashMap<>();
		String cdSugVendaPersonTemp;
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto)produtos.items[i];
			if ((produtoSelecionado = produtosSelecionadosHashMap.get(produto.cdProduto)) != null) {
				cdSugVendaPersonTemp = produto.cdSugVendaPerson;
				produtoSelecionado.getCodsSugVendaPerson().put(cdSugVendaPersonTemp, cdSugVendaPersonTemp);
				cdSugVendaPersonHashMap.put(cdSugVendaPersonTemp, cdSugVendaPersonTemp);
			}
		}
		SugVendaPerson filter = new SugVendaPerson();
		filter.cdSugsFilter = new ArrayList<>(cdSugVendaPersonHashMap.keySet());
		Vector sugVendaPersonList = (cdSugVendaPersonHashMap == null || cdSugVendaPersonHashMap.isEmpty()) ? new Vector() : findAllByExample(filter);
		size = sugVendaPersonList.size();
		ArrayList<SugVendaPerson> sugestoes = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			sugestoes.add((SugVendaPerson) sugVendaPersonList.items[i]);
		}
		pedido.sugVendaPersonList = sugestoes;
		Vector itensPedido = new Vector(limiteProdutos);
		HashMap<String, ItemPedido> itens = new HashMap<>(limiteProdutos);
		if (LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson && listaAtual != null) {
			int size2 = listaAtual.size();
			ItemPedido itemPedido;
			for (int i = 0; i < size2; i++) {
				itemPedido = (ItemPedido)listaAtual.items[i];
				itens.put(itemPedido.cdProduto, itemPedido);
			}
		}
		ItemPedido itemPedido;
		for (Produto produto : produtosSelecionadosList) {
			Vector produtoUnidadeList = getProdutoUnidadeListSorted(produto, pedido);
			String cdUnidadeAnterior = null;
			ProdutoUnidade unAlternativa = (ProdutoUnidade) produtoUnidadeList.items[0];
			String cdUnidade = unAlternativa != null ? unAlternativa.cdUnidade : null;
			if (!itens.isEmpty() && itens.containsKey(produto.cdProduto)) {
				itemPedido = itens.get(produto.cdProduto);
				cdUnidadeAnterior = itemPedido.cdUnidade;
				ItemTabelaPrecoService.getInstance().loadCdUnidadeItem(pedido, produto, cdUnidade, itemPedido);
			} else {
				itemPedido = ItemTabelaPrecoService.getInstance().getItemPedidoComValores(pedido, produto, pedido.cdTabPrecoFilterSug, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), cdUnidade, false);
			}
			itemPedido.unidadesListSugPerson = produtoUnidadeList;
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			itemPedido.flSugVendaPerson = ValueUtil.VALOR_SIM;
			itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			Produto produtoItemPed = itemPedido.getProduto();
			produtoItemPed.cdGrupoProduto1 = produto.cdGrupoProduto1;
			produtoItemPed.cdGrupoProduto2 = produto.cdGrupoProduto2;
			produtoItemPed.cdGrupoProduto3 = produto.cdGrupoProduto3;
			produtoItemPed.cdGrupoProduto4 = produto.cdGrupoProduto4;
			produtoItemPed.cdGrupoProduto5 = produto.cdGrupoProduto5;
			produtoItemPed.vlMedioHistorico = produto.vlMedioHistorico;
			produtoItemPed.qtdMediaHistorico = produto.qtdMediaHistorico;
			produtoItemPed.dsProduto = produto.dsProduto;
			produtoItemPed.dsPrincipioAtivo = produto.dsPrincipioAtivo;
			produtoItemPed.cdsSugVendaPerson = new ArrayList<>(produto.codsSugVendaPerson.keySet());
			produtoItemPed.dsReferencia = produto.dsReferencia;
			produtoItemPed.flUtilizaVerba = produto.flUtilizaVerba;
			if (LavenderePdaConfig.isMostraFotoProdutoListSugPerson()) {
				produtoItemPed.fotoProduto = produto.fotoProduto;
			}
			if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto) {
				produtoItemPed.nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
			}
			if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
				produtoItemPed.nuConversaoUMCreditoAntecipado = produto.nuConversaoUMCreditoAntecipado;
			}
			double qtEstoque = produto.qtEstoqueProduto;
			if (ValueUtil.isNotEmpty(cdUnidadeAnterior) && !ValueUtil.valueEquals(cdUnidadeAnterior, itemPedido.cdUnidade)) {
				PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
			}
			qtEstoque = getEstoqueToGridUnidadeAlternativa(itemPedido, produtoItemPed, qtEstoque, pedido);
			produtoItemPed.qtEstoqueProduto = qtEstoque;
			itensPedido.addElement(itemPedido);
		}
		return itensPedido;
	}
	
	private double getEstoqueToGridUnidadeAlternativa(ItemPedido itemPedido, Produto produto, double qtEstoque, Pedido pedido) throws SQLException {
		ItemPedido itemPedidoClone = (ItemPedido)itemPedido.clone();
		itemPedidoClone.cdTabelaPreco = pedido.cdTabelaPreco;
		itemPedidoClone.setProduto(produto);
		return EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedidoClone.getItemTabelaPreco(), itemPedidoClone.getProdutoUnidade(), qtEstoque);
	}
	
	public double getEstoqueToGridUnidadeAlternativa(ItemPedido itemPedido, ProdutoBase produtoBase, double qtEstoque, Pedido pedido) throws SQLException {
		ItemPedido itemPedidoClone = (ItemPedido)itemPedido.clone();
		itemPedidoClone.cdTabelaPreco = pedido.cdTabelaPreco;
		Produto produto = new Produto();
		produto.cdEmpresa = produtoBase.cdEmpresa;
		produto.cdRepresentante = produto.cdRepresentante;
		produto.cdProduto = produtoBase.cdProduto;
		produto.cdUnidade = produtoBase.cdUnidade;
		itemPedidoClone.setProduto(produto);
		return EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedidoClone.getItemTabelaPreco(), itemPedidoClone.getProdutoUnidade(), qtEstoque);
	}
	
	private String getDsOrdencaoSugPerson() {
		String ordemCol = LavenderePdaConfig.dsOrdenacaoListaSugPerson;
		if (ordemCol.equals(SugVendaPerson.ORDEMCDPRODUTO)) {
			return "produto.CDPRODUTO";
		} else if (ordemCol.equals(SugVendaPerson.ORDEMDSPRODUTO)) {
			return "produto.DSPRODUTO";
		} else if (ordemCol.equals(SugVendaPerson.ORDEMQTESTOQUE) && LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			return "QTDESTOQUE";
		} else if (ordemCol.equals(SugVendaPerson.ORDEMVLHIST) && LavenderePdaConfig.dsInformacoesComplementaresListaSugestaoVendaPerson.indexOf(String.valueOf(SugVendaPerson.CAMPO_VLHIS)) != -1) {
			return "giroProduto.VLMEDIOHISTORICO";
		} else if (ordemCol.equals(SugVendaPerson.ORDEMQTDHIST) && LavenderePdaConfig.dsInformacoesComplementaresListaSugestaoVendaPerson.indexOf(String.valueOf(SugVendaPerson.CAMPO_QTDHIS)) != -1) {
			return "giroProduto.QTDMEDIAHISTORICO";
		} else {
			return "produto.CDPRODUTO";
		}
	}
	
	public HashMap<String, Produto> getProdutosSelecionadosMap(Pedido pedido) throws SQLException {
		int limiteProdutos = LavenderePdaConfig.qtLimiteProdutosSug;
		boolean asc = !SugVendaPerson.ORDEMDESC.equals(LavenderePdaConfig.sentidoOrdenacaoListaSugPerson);
		String dsOrdenacao = getDsOrdencaoSugPerson();
		dsOrdenacao += asc ? " ASC" : " DESC";
		String cdTabelaPrecoFilter = LavenderePdaConfig.ocultaTabelaPrecoPedido ? pedido.getCliente().cdTabelaPreco : pedido.cdTabelaPreco;
		Vector produtos = SugVendaPersonProdDbxDao.getInstance().getProdutosSugestoesPerson(pedido, null, pedido.getCliente().cdGrupoPermProd, cdTabelaPrecoFilter, dsOrdenacao);
		HashMap<String, Produto> produtosSelecionadosHashMap = new HashMap<>(limiteProdutos);
		List<Produto> produtosSelecionadosList = new ArrayList<>(limiteProdutos);
		int size = produtos.size();
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto)produtos.items[i];
			if (produtosSelecionadosHashMap.get(produto.cdProduto) == null) {
				produtosSelecionadosHashMap.put(produto.cdProduto, produto);
				produtosSelecionadosList.add(produto);
			}
			if (produtosSelecionadosHashMap.size() >= limiteProdutos) {
				break;
			}
		}
		return produtosSelecionadosHashMap;
	}
	
	public Vector getProdutoUnidadeListSorted(Produto produto, Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.cdProduto = produto.cdProduto;
		itemPedido.pedido = pedido;
		itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
		Vector unidades = ProdutoUnidadeService.getInstance().findAllByProduto(itemPedido, produto, pedido.cdTabelaPreco);
		SortUtil.qsortDouble(unidades.items, 0, unidades.size() - 1, true);
		return unidades;
	}
	
}
