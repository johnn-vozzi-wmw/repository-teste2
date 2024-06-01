package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescPromocionalDbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class DescPromocionalService extends CrudService {

	protected Hashtable descPromoPorProdutoHash;
	private StringBuffer hashKeyBuffer;
	private String lastCdEmpresa;
    private static DescPromocionalService instance;
    
    private DescPromocionalService() {
        //--
    }
    
    public static DescPromocionalService getInstance() {
        if (instance == null) {
            instance = new DescPromocionalService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescPromocionalDbxDao.getInstance();
    }

    @Override
	public void validate(BaseDomain arg0) { /**/ }
	
	public void loadDescPromocional(ItemPedido itemPedido) throws SQLException {
		loadDescPromocional(itemPedido, itemPedido.getProduto());
	}
	
	public boolean loadDescPromocional(ItemPedido itemPedido, ProdutoBase produto) throws SQLException {
		if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
			return loadDescPromocionalRegraInterpolacao(itemPedido, produto);
		}
		if (itemPedido.possuiDescMaxProdCli()) {
			itemPedido.descPromocionalComQtdList = new Vector(0);
			itemPedido.descPromocional = new DescPromocional();
			return false;
		}
		String cdLocal = ValueUtil.isNotEmpty(itemPedido.cdLocal) ? itemPedido.cdLocal : DescPromocional.CD_CHAVE_VAZIO;
		DescPromocional descPromocionalFilter = new DescPromocional();
		descPromocionalFilter.cdEmpresa = itemPedido.cdEmpresa;
		descPromocionalFilter.cdRepresentante = itemPedido.cdRepresentante;
		descPromocionalFilter.dtInicial = DateUtil.getCurrentDate();
		descPromocionalFilter.dtFinal = DateUtil.getCurrentDate();
		descPromocionalFilter.sortAtributte = DescPromocional.NOME_COLUNA_QTITEM;
		descPromocionalFilter.sortAsc = ValueUtil.VALOR_SIM;
		Hashtable descPromoHash = findAllByExampleInInternalCache(descPromocionalFilter);
		
		if (descPromoHash.size() != 0) {
			Cliente cliente = itemPedido.pedido.getCliente();
			//-- 1 - Desconto Promocional por Cliente e Produto
			if (cliente != null && produto != null) {
				loadDescPromocional(itemPedido, cliente.cdCliente, produto.cdProduto, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 2 - Desconto Promocional por Cliente e Grupo de Desconto de Produto
			if (cliente != null && produto != null && ValueUtil.isNotEmpty(produto.cdGrupoDescProd)) {
				loadDescPromocional(itemPedido, cliente.cdCliente, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, produto.cdGrupoDescProd, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 3 - Desconto Promocional por Grupo de Desconto de Cliente e Produto
			if (cliente != null && ValueUtil.isNotEmpty(cliente.cdGrupoDescCli) && produto != null) {
				loadDescPromocional(itemPedido, DescPromocional.CD_CHAVE_VAZIO, produto.cdProduto, cliente.cdGrupoDescCli, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 4 - Desconto Promocional por Grupo de Desconto de Cliente e Grupo de Desconto de Produto
			if (cliente != null && ValueUtil.isNotEmpty(cliente.cdGrupoDescCli) && produto != null && ValueUtil.isNotEmpty(produto.cdGrupoDescProd)) {
				loadDescPromocional(itemPedido, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, cliente.cdGrupoDescCli, produto.cdGrupoDescProd, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 5 - Desconto Promocional por Cliente
			if (cliente != null) {
				loadDescPromocional(itemPedido, cliente.cdCliente, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 6 - Desconto Promocional por Grupo de Desconto de Cliente
			if (cliente != null && ValueUtil.isNotEmpty(cliente.cdGrupoDescCli)) {
				loadDescPromocional(itemPedido, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, cliente.cdGrupoDescCli, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 7 - Desconto Promocional por Produto
			if (produto != null) {
				loadDescPromocional(itemPedido, DescPromocional.CD_CHAVE_VAZIO, produto.cdProduto, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
			//-- 8 - Desconto Promocional por Grupo de Desconto de Produto
			if (produto != null && ValueUtil.isNotEmpty(produto.cdGrupoDescProd)) {
				loadDescPromocional(itemPedido, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, produto.cdGrupoDescProd, cdLocal, descPromoHash);
				if (isDescPromocionalEncontrado(itemPedido)) {
					return true;
				}
			}
		}
		itemPedido.descPromocionalComQtdList = new Vector(0);
		itemPedido.descPromocional = new DescPromocional();
		return false;
	}

	private boolean loadDescPromocionalRegraInterpolacao(ItemPedido itemPedido, ProdutoBase produto) throws SQLException {
		if (itemPedido == null) return false;
		Cliente cliente = itemPedido.pedido.getCliente();
		if (cliente == null || produto == null) return semDescPromocional(itemPedido);
		Vector listDescPromocionalMelhorGrupo = DescPromocionalDbxDao.getInstance().findDescPromocionalPrior(getDescPromocionalFilter(itemPedido, cliente));
		if (ValueUtil.isEmpty(listDescPromocionalMelhorGrupo)) return semDescPromocional(itemPedido);
		DescPromocional descPromocionalFilter = (DescPromocional) listDescPromocionalMelhorGrupo.items[0];
		if (ValueUtil.isEmpty(itemPedido.pedido.itemPedidoList)) {
			PedidoService.getInstance().findItemPedidoList(itemPedido.pedido);
		}
		
		double qtTotalItemGrupo = getQtItemGrupoDescTotalPedido(itemPedido, descPromocionalFilter);
		DescPromocional descPromocionalSelecionado = selecionaDescPromocionalQtd(listDescPromocionalMelhorGrupo, qtTotalItemGrupo);
		trocaDescPromoItensEMarcaParaRecalcular(itemPedido, descPromocionalSelecionado);
		
		selecionaDescPromocional(descPromocionalSelecionado, itemPedido);
		itemPedido.setProdutoBase(produto);
		return isDescPromocionalEncontrado(itemPedido);
	}

	private boolean semDescPromocional(ItemPedido itemPedido) {
		itemPedido.descPromocional = new DescPromocional();
		return false;
	}

	private void trocaDescPromoItensEMarcaParaRecalcular(ItemPedido itemPedido, DescPromocional descPromocionalSelecionado) {
		if (ValueUtil.isEmpty(itemPedido.pedido.itemPedidoList)) return;
		int sizeItem = itemPedido.pedido.itemPedidoList.size();
		for (int i = 0; i < sizeItem; i++) {
			ItemPedido itemPedidoListItem = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
			if (itemPedido.equals(itemPedidoListItem)) {
				selecionaDescPromocional(descPromocionalSelecionado, itemPedidoListItem);
				continue;
			}
			if (descPromocionalSelecionado.equals(itemPedidoListItem.descPromocional) || !ValueUtil.valueEquals(itemPedidoListItem.cdGrupoDescCli, descPromocionalSelecionado.cdGrupoDescCli) || !ValueUtil.valueEquals(itemPedidoListItem.cdGrupoDescProd, descPromocionalSelecionado.cdGrupoDescProd)) continue;
			selecionaDescPromocional(descPromocionalSelecionado, itemPedidoListItem);
		}
	}

	private void selecionaDescPromocional(DescPromocional descPromocionalSelecionado, ItemPedido itemPedidoListItem) {
		itemPedidoListItem.descPromocional = descPromocionalSelecionado;
		itemPedidoListItem.cdGrupoDescCli = descPromocionalSelecionado.cdGrupoDescCli;
		itemPedidoListItem.cdGrupoDescProd = descPromocionalSelecionado.cdGrupoDescProd;
		itemPedidoListItem.vlPctDescontoAuto = descPromocionalSelecionado.vlPctDescontoProduto;
		itemPedidoListItem.vlPctDescontoAuto = ValueUtil.getDoubleValueTruncated(itemPedidoListItem.descPromocional.vlPctDescontoProduto, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
		itemPedidoListItem.recalculaDescPromo = true;
	}

	private DescPromocional selecionaDescPromocionalQtd(Vector listDescPromocionalMelhorGrupo, double qtTotalItemGrupo) {
		DescPromocional descPromocionalSelecionado = null;
		int size = listDescPromocionalMelhorGrupo.size();
		for (int i = 0; i < size; i++) {
			DescPromocional descPromocional = (DescPromocional) listDescPromocionalMelhorGrupo.items[i];
			if (descPromocional.qtItem > qtTotalItemGrupo) break;
			if (descPromocionalSelecionado != null && descPromocionalSelecionado.dtInicial.isAfter(descPromocional.dtInicial)) continue;
			descPromocionalSelecionado = descPromocional;
		}
		return (descPromocionalSelecionado != null) ? descPromocionalSelecionado : new DescPromocional();
	}

	private double getQtItemGrupoDescTotalPedido(ItemPedido itemPedido, DescPromocional descPromocionalFilter) throws SQLException {
		final boolean usaDescPromocionalRegraInterpolacaoUnidadeProduto = LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoUnidadeProduto();
		double qtItemGrupo = getQtItemTotal(itemPedido, descPromocionalFilter, 0d, usaDescPromocionalRegraInterpolacaoUnidadeProduto);
		if (ValueUtil.isEmpty(itemPedido.pedido.itemPedidoList)) return qtItemGrupo;
		int size = itemPedido.pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoListItem = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
			if (itemPedido.equals(itemPedidoListItem) || !ValueUtil.valueEquals(itemPedidoListItem.cdGrupoDescCli, descPromocionalFilter.cdGrupoDescCli) || !ValueUtil.valueEquals(itemPedidoListItem.cdGrupoDescProd, descPromocionalFilter.cdGrupoDescProd)) continue;
			qtItemGrupo = getQtItemTotal(itemPedidoListItem, descPromocionalFilter, qtItemGrupo, usaDescPromocionalRegraInterpolacaoUnidadeProduto);
		}
		return qtItemGrupo;
	}

	private double getQtItemTotal(ItemPedido itemPedido, DescPromocional descPromocionalFilter, double qtItemGrupo, final boolean usaDescPromocionalRegraInterpolacaoUnidadeProduto) throws SQLException {
		if (!usaDescPromocionalRegraInterpolacaoUnidadeProduto || ValueUtil.isEmpty(descPromocionalFilter.cdUnidade) || ValueUtil.valueEquals(descPromocionalFilter.cdUnidade, itemPedido.cdUnidade)) {
			qtItemGrupo += itemPedido.getQtItemLista();
		} else if (usaDescPromocionalRegraInterpolacaoUnidadeProduto) {
			itemPedido.cdUnidadeDescPromocionalFilter = descPromocionalFilter.cdUnidade;
			qtItemGrupo = getQtItemUnAlternativa(itemPedido, qtItemGrupo);
			itemPedido.cdUnidadeDescPromocionalFilter = null;
		}
		return qtItemGrupo;
	}

	private double getQtItemUnAlternativa(ItemPedido itemPedido, double qtItemGrupo) throws SQLException {
		double qtTotalItemUnidade;
		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade(true);
		if (produtoUnidade.isMultiplica()) {
			qtTotalItemUnidade = itemPedido.getQtItemLista() * produtoUnidade.nuConversaoUnidade;
		} else {
			qtTotalItemUnidade = itemPedido.getQtItemLista() / produtoUnidade.nuConversaoUnidade;
		}
		qtItemGrupo += qtTotalItemUnidade;
		return qtItemGrupo;
	}

	private DescPromocional getDescPromocionalFilter(final ItemPedido itemPedido, final Cliente cliente) {
		DescPromocional descPromocionalFilter = new DescPromocional();
		descPromocionalFilter.cdEmpresa = itemPedido.cdEmpresa;
		descPromocionalFilter.cdRepresentante = itemPedido.cdRepresentante;
		descPromocionalFilter.cdCliente = cliente.cdCliente;
		descPromocionalFilter.cdProduto = itemPedido.cdProduto;
		descPromocionalFilter.cdTipoPedido = itemPedido.pedido.cdTipoPedido;
		descPromocionalFilter.dtInicial = DateUtil.getCurrentDate();
		descPromocionalFilter.dtFinal = DateUtil.getCurrentDate();
		return descPromocionalFilter;
	}

	public boolean isDescPromocionalEncontrado(ItemPedido itemPedido) {
		boolean descPromocionalEncontrado = (itemPedido.descPromocional != null && !(itemPedido.descPromocional.equals(new DescPromocional()))) || (itemPedido.descPromocionalComQtdList != null && itemPedido.descPromocionalComQtdList.size() > 0);
		if (itemPedido.getProdutoBase() != null) {
			itemPedido.getProdutoBase().possuiDescPromocional = descPromocionalEncontrado;
		}
		return descPromocionalEncontrado; 
	}
	
	private void loadDescPromocional(ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal, Hashtable descPromoHash) throws SQLException {
		if (LavenderePdaConfig.habilitaRegrasAdicionaisDescPromocional()) {
			buscaDescPromocionalComRegrasAdicionais(itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal, descPromoHash);
		} else {
			buscaDescPromocionalSemRegrasAdicionais(itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal, descPromoHash);
		}
		lastCdEmpresa = itemPedido.cdEmpresa;
	}
	
	private void populaDescPromocionalFilterComDadosComuns(DescPromocional descPromocionalFilter, ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal) {
		descPromocionalFilter.cdEmpresa = itemPedido.cdEmpresa;
		descPromocionalFilter.cdRepresentante = itemPedido.cdRepresentante;
		descPromocionalFilter.cdCliente = cdCliente;
		descPromocionalFilter.cdProduto = cdProduto;
		descPromocionalFilter.cdGrupoDescCli = cdGrupoDescCli;
		descPromocionalFilter.cdGrupoDescProd = cdGrupoDescProd;
		descPromocionalFilter.qtItem = ValueUtil.getIntegerValue(DescPromocional.CD_CHAVE_VAZIO);
		descPromocionalFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		descPromocionalFilter.cdLocal = cdLocal;
		descPromocionalFilter.cdCondicaoComercial = getCdCondicaoComercial(itemPedido.pedido);
	}
	
	private DescPromocional getDescPromocionalFilterComRegrasAdicionais(ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal) throws SQLException {
		DescPromocional descPromocionalFilter = new DescPromocional();
		populaDescPromocionalFilterComDadosComuns(descPromocionalFilter, itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal);
		descPromocionalFilter.cdUf = getCdUf(itemPedido.pedido);
		descPromocionalFilter.cdTipoPedido = getCdTipoPedido(itemPedido.pedido);
		descPromocionalFilter.cdCondicaoPagamento = getCdCondicaoPagamento(itemPedido.pedido);
		descPromocionalFilter.cdTipoFrete = getCdTipoFrete(itemPedido.pedido);
		descPromocionalFilter.cdLocalEstoque = getCdLocalEstoque(itemPedido.pedido);
		return descPromocionalFilter;
	}
	
	private DescPromocional getDescPromocionalFilterSemRegrasAdicionais(ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal) {
		DescPromocional descPromocionalFilter = new DescPromocional();
		populaDescPromocionalFilterComDadosComuns(descPromocionalFilter, itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal);
		descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdUf = DescPromocional.CD_CHAVE_VAZIO;
		return descPromocionalFilter;
	}
	
	private void buscaDescPromocionalComTodasEntidadesZero(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
	}
	
	private void buscaDescPromocionalPorEstado(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
	}
	
	private void buscaDescPromocionalPorTipoPedido(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorCondicaoComercial(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorCondicaoPagamentoBeforeTabelaPreco(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
			descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorCondicaoPagamentoAfterTabelaPreco(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorTabelaPrecoBeforeCondicaoPagamento(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
			descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorTabelaPrecoAfterCondicaoPagamento(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			} else {
				descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			}
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
				descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			} else {
				descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
				descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			}
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			} else {
				descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			}
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoECondComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorTipoFrete(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
			descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
			descPromocionalFilter.cdTipoFrete = descPromocionalFilterOld.cdTipoFrete;
			descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaDescPromocionalPorLocalEstoque(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		buscaComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
			descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaComCombinacoesDeEstadoTipoPedidoECondComercial(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		buscaCombinacaoComEstadoZero(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
	}
	
	private void buscaCombinacaoComEstadoZero(ItemPedido itemPedido, Hashtable descPromoHash, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdUf = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
	}
	
	private void buscaDescPromocionalComRegrasAdicionais(ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal, Hashtable descPromoHash) throws SQLException {
		DescPromocional descPromocionalFilter = getDescPromocionalFilterComRegrasAdicionais(itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal);
		DescPromocional descPromocionalFilterOld = (DescPromocional) descPromocionalFilter.clone();
		itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		buscaDescPromocionalPorLocalEstoque(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		buscaDescPromocionalPorTipoFrete(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
			buscaDescPromocionalPorTabelaPrecoBeforeCondicaoPagamento(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
			buscaDescPromocionalPorCondicaoPagamentoAfterTabelaPreco(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		} else {
			buscaDescPromocionalPorCondicaoPagamentoBeforeTabelaPreco(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
			buscaDescPromocionalPorTabelaPrecoAfterCondicaoPagamento(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		}
		buscaDescPromocionalPorCondicaoComercial(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		buscaDescPromocionalPorTipoPedido(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		buscaDescPromocionalPorEstado(itemPedido, descPromoHash, descPromocionalFilter, descPromocionalFilterOld);
		buscaDescPromocionalComTodasEntidadesZero(itemPedido, descPromoHash, descPromocionalFilter);
		if (itemPedido.descPromocional == null) {
			itemPedido.descPromocional = new DescPromocional();
		}
		if (LavenderePdaConfig.usaDescontoQtdeNoGrupoDescPromocional) {
			loadDescPromocionalComQtdComRegrasAdicionais(itemPedido, descPromocionalFilterOld, descPromoHash);
		}
	}
	
	private void buscaDescPromocionalSemRegrasAdicionais(ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal, Hashtable descPromoHash) throws SQLException {
		DescPromocional descPromocionalFilter = getDescPromocionalFilterSemRegrasAdicionais(itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal);
		itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoComercial = getCdCondicaoComercial(itemPedido.pedido);
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		if (itemPedido.descPromocional == null) {
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			itemPedido.descPromocional = (DescPromocional) descPromoHash.get(descPromocionalFilter.getRowKey());
		}
		if (itemPedido.descPromocional == null) {
			itemPedido.descPromocional = new DescPromocional();
		}
		if (LavenderePdaConfig.usaDescontoQtdeNoGrupoDescPromocional) {
			loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, cdLocal, descPromoHash);
		}
	}
	
	private String getCdCondicaoComercial(Pedido pedido) {
		if (pedido != null && ValueUtil.isNotEmpty(pedido.cdCondicaoComercial)) {
			return pedido.cdCondicaoComercial;
		} else {
			return CondicaoComercial.CDCONDICAOCOMERCIAL_VALOR_PADRAO;
		}
	}
	
	private String getCdUf (Pedido pedido) throws SQLException {
		if (pedido != null && pedido.getCliente() != null && ValueUtil.isNotEmpty(pedido.getCliente().cdEstadoComercial)) {
			return pedido.getCliente().cdEstadoComercial;
		} else {
			return DescPromocional.CD_CHAVE_VAZIO;
		}
	}
	
	private String getCdTipoPedido(Pedido pedido) {
		if (pedido != null && ValueUtil.isNotEmpty(pedido.cdTipoPedido)) {
			return pedido.cdTipoPedido;
		} else {
			return DescPromocional.CD_CHAVE_VAZIO;
		}
	}
	
	private String getCdTipoFrete(Pedido pedido) {
		if (pedido != null && ValueUtil.isNotEmpty(pedido.cdTipoFrete)) {
			return pedido.cdTipoFrete;
		} else {
			return DescPromocional.CD_CHAVE_VAZIO;
		}
	}
	
	private String getCdCondicaoPagamento(Pedido pedido) {
		if (pedido != null && ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento)) {
			return pedido.cdCondicaoPagamento;
		} else {
			return DescPromocional.CD_CHAVE_VAZIO;
		}
	}
	
	private String getCdLocalEstoque(Pedido pedido) throws SQLException {
		return pedido != null ? pedido.getCdLocalEstoque() : DescPromocional.CD_CHAVE_VAZIO;
	}
	
	public void loadDescPromocionalComQtdSemRegrasAdicionais(ItemPedido itemPedido, String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdLocal, Hashtable descPromoHash) throws SQLException {
		initDescPromoPorProdutoHash(descPromoHash, itemPedido.cdEmpresa);
		Vector descPromocionalHashValues = getDescQtdFromPartialHash(itemPedido, cdProduto);
		int size  = descPromocionalHashValues.size();
		SortUtil.qsortInt(descPromocionalHashValues.items, 0, size - 1, true);
        Vector descPromocionalList = new Vector();
        DescPromocional descPromocional;
        boolean encontrouDescPromocionalComTabelaPrecoECondicaoComercial = false;
        boolean encontrouDescPromocionalComTabelaPreco = false;
        boolean encontrouDescPromocionalComCondicaoComercial = false;
        boolean encontrouDescPromocionalTodasTabelasECondicoes = false;
        for (int i = 0; i < size; i++) {
        	descPromocional = (DescPromocional) descPromocionalHashValues.items[i];
        	if (isDescPromocionalComQtdeEncontradoSemRegrasAdicionais(cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, itemPedido.cdTabelaPreco, itemPedido.pedido.cdCondicaoComercial, cdLocal, descPromocional)) { 
        		if (encontrouDescPromocionalComTabelaPreco || encontrouDescPromocionalComCondicaoComercial || encontrouDescPromocionalTodasTabelasECondicoes) {
        			removeDescPromocionalIguaisList(descPromocionalList);
				}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouDescPromocionalComTabelaPrecoECondicaoComercial = true;
        	}
        	if (!encontrouDescPromocionalComTabelaPrecoECondicaoComercial && 
        			isDescPromocionalComQtdeEncontradoSemRegrasAdicionais(cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, itemPedido.cdTabelaPreco, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromocional)) { 
        		if (encontrouDescPromocionalComCondicaoComercial || encontrouDescPromocionalTodasTabelasECondicoes) {
        			removeDescPromocionalIguaisList(descPromocionalList);
				}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouDescPromocionalComTabelaPreco = true;
        	}
        	if (!encontrouDescPromocionalComTabelaPrecoECondicaoComercial && !encontrouDescPromocionalComTabelaPreco && 
        			isDescPromocionalComQtdeEncontradoSemRegrasAdicionais(cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, DescPromocional.CD_CHAVE_VAZIO, itemPedido.pedido.cdCondicaoComercial, cdLocal, descPromocional)) { 
        		if (encontrouDescPromocionalTodasTabelasECondicoes) {
        			removeDescPromocionalIguaisList(descPromocionalList);
				}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouDescPromocionalComCondicaoComercial = true;
        	}
        	if (!encontrouDescPromocionalComTabelaPrecoECondicaoComercial && !encontrouDescPromocionalComTabelaPreco && !encontrouDescPromocionalComCondicaoComercial && 
        			isDescPromocionalComQtdeEncontradoSemRegrasAdicionais(cdCliente, cdProduto, cdGrupoDescCli, cdGrupoDescProd, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, cdLocal, descPromocional)) { 
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouDescPromocionalTodasTabelasECondicoes = true;
        	}
        }
        itemPedido.descPromocionalComQtdList = getDescPromocionalQtItemUnidadeAlternativaCalculadoList(itemPedido, descPromocionalList);
	}
	
	public void loadDescPromocionalComQtdComRegrasAdicionais(ItemPedido itemPedido, DescPromocional descPromocionalFilter, Hashtable descPromoHash) throws SQLException {
		initDescPromoPorProdutoHash(descPromoHash, itemPedido.cdEmpresa);
		Vector descPromocionalHashValues = getDescQtdFromPartialHash(itemPedido, descPromocionalFilter.cdProduto);
        Vector descPromocionalList = new Vector();
        int size  = descPromocionalHashValues.size();
        DescPromocional descPromocional;
        DescPromocional descPromocionalFilterOld = (DescPromocional) descPromocionalFilter.clone();
        boolean encontrouComTodasEntidadesEspecificas = false;
        boolean encontrouComCombinacoesLocalEstoqueEspecifico = false;
        boolean encontrouComCombinacoesTipoFreteEspecifico = false;
        boolean encontrouComCombinacoesCondPagtoEspecifico = false;
        boolean encontrouComCombinacoesTabelaPrecoEspecifico = false;
        boolean encontrouComCombinacoesCondComercialEspecifico = false;
        boolean encontrouComCombinacoesTipoPedidoEspecifico = false;
        boolean encontrouComCombinacoesEstadoEspecifico = false;
        boolean encontrouComTodasEntidadesNaoEspecificas = false;
        for (int i = 0; i < size; i++) {
        	descPromocional = (DescPromocional) descPromocionalHashValues.items[i];
        	if (isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional)) {
        		if (encontrouComCombinacoesLocalEstoqueEspecifico || encontrouComCombinacoesTipoFreteEspecifico || encontrouComCombinacoesCondPagtoEspecifico || encontrouComCombinacoesTabelaPrecoEspecifico ||
        				encontrouComCombinacoesCondComercialEspecifico || encontrouComCombinacoesTipoPedidoEspecifico || encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
        			removeDescPromocionalIguaisList(descPromocionalList);
        		}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComTodasEntidadesEspecificas = true;
        	}
        	if (!encontrouComTodasEntidadesEspecificas && isDescPromocionalComQtdeEncontradoComCombinacoesLocalEstoque(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
        		if (encontrouComCombinacoesTipoFreteEspecifico || encontrouComCombinacoesCondPagtoEspecifico || encontrouComCombinacoesTabelaPrecoEspecifico || encontrouComCombinacoesCondComercialEspecifico 
        				|| encontrouComCombinacoesTipoPedidoEspecifico || encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
        			removeDescPromocionalIguaisList(descPromocionalList);
        		}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComCombinacoesLocalEstoqueEspecifico = true;
        	}
        	if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico 
        			&& isDescPromocionalComQtdeEncontradoComCombinacoesTipoFrete(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
        		if (encontrouComCombinacoesCondPagtoEspecifico || encontrouComCombinacoesTabelaPrecoEspecifico || encontrouComCombinacoesCondComercialEspecifico 
        				|| encontrouComCombinacoesTipoPedidoEspecifico || encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
        			removeDescPromocionalIguaisList(descPromocionalList);
        		}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComCombinacoesTipoFreteEspecifico = true;
        	}
        	if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
        		if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico
            			&& isDescPromocionalComQtdeEncontradoComCombinacoesTabPrecoBeforeCondPagto(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
            		if (encontrouComCombinacoesCondPagtoEspecifico || encontrouComCombinacoesCondComercialEspecifico || encontrouComCombinacoesTipoPedidoEspecifico 
            				|| encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
            			removeDescPromocionalIguaisList(descPromocionalList);
            		}
            		descPromocionalList.addElement(descPromocional.clone());
            		encontrouComCombinacoesTabelaPrecoEspecifico = true;
            	}
        		if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico && !encontrouComCombinacoesTabelaPrecoEspecifico
            			&& isDescPromocionalComQtdeEncontradoComCombinacoesCondPagtoAfterTabPreco(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
            		if (encontrouComCombinacoesCondComercialEspecifico || encontrouComCombinacoesTipoPedidoEspecifico || encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
            			removeDescPromocionalIguaisList(descPromocionalList);
            		}
            		descPromocionalList.addElement(descPromocional.clone());
            		encontrouComCombinacoesCondPagtoEspecifico = true;
            	}
        	} else {
        		if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico 
            			&& isDescPromocionalComQtdeEncontradoComCombinacoesCondPagtoBeforeTabPreco(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
            		if (encontrouComCombinacoesTabelaPrecoEspecifico || encontrouComCombinacoesCondComercialEspecifico || encontrouComCombinacoesTipoPedidoEspecifico 
            				|| encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
            			removeDescPromocionalIguaisList(descPromocionalList);
            		}
            		descPromocionalList.addElement(descPromocional.clone());
            		encontrouComCombinacoesCondPagtoEspecifico = true;
            	}
        		if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico && !encontrouComCombinacoesCondPagtoEspecifico
            			&& isDescPromocionalComQtdeEncontradoComCombinacoesTabPrecoAfterCondPagto(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
            		if (encontrouComCombinacoesCondComercialEspecifico || encontrouComCombinacoesTipoPedidoEspecifico || encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
            			removeDescPromocionalIguaisList(descPromocionalList);
            		}
            		descPromocionalList.addElement(descPromocional.clone());
            		encontrouComCombinacoesTabelaPrecoEspecifico = true;
            	}
        	}
        	if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico && !encontrouComCombinacoesCondPagtoEspecifico 
        			&& !encontrouComCombinacoesTabelaPrecoEspecifico && isDescPromocionalComQtdeEncontradoComCombinacoesCondComercial(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
        		if (encontrouComCombinacoesTipoPedidoEspecifico || encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
        			removeDescPromocionalIguaisList(descPromocionalList);
        		}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComCombinacoesCondComercialEspecifico = true;
        	}
        	if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico && !encontrouComCombinacoesCondPagtoEspecifico 
        			&& !encontrouComCombinacoesTabelaPrecoEspecifico && !encontrouComCombinacoesCondComercialEspecifico
        			&& isDescPromocionalComQtdeEncontradoComCombinacoesTipoPedido(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
        		if (encontrouComCombinacoesEstadoEspecifico || encontrouComTodasEntidadesNaoEspecificas) {
        			removeDescPromocionalIguaisList(descPromocionalList);
        		}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComCombinacoesTipoPedidoEspecifico = true;
        	}
        	if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico && !encontrouComCombinacoesCondPagtoEspecifico 
        			 && !encontrouComCombinacoesTabelaPrecoEspecifico && !encontrouComCombinacoesCondComercialEspecifico && !encontrouComCombinacoesTipoPedidoEspecifico 
        			 && isDescPromocionalComQtdeEncontradoComCombinacoesEstado(descPromocionalFilter, descPromocionalFilterOld, descPromocional)) {
        		if (encontrouComTodasEntidadesNaoEspecificas) {
        			removeDescPromocionalIguaisList(descPromocionalList);
        		}
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComCombinacoesEstadoEspecifico = true;
        	}
        	if (!encontrouComTodasEntidadesEspecificas && !encontrouComCombinacoesLocalEstoqueEspecifico && !encontrouComCombinacoesTipoFreteEspecifico && !encontrouComCombinacoesCondPagtoEspecifico 
        			&& !encontrouComCombinacoesTabelaPrecoEspecifico && !encontrouComCombinacoesCondComercialEspecifico && !encontrouComCombinacoesTipoPedidoEspecifico 
        			&& !encontrouComCombinacoesEstadoEspecifico && isDescPromocionalComQtdeEncontradoComEntidadesZero(descPromocionalFilter, descPromocional)) {
        		descPromocionalList.addElement(descPromocional.clone());
        		encontrouComTodasEntidadesNaoEspecificas = true;
        	}
        }
       	itemPedido.descPromocionalComQtdList = getDescPromocionalQtItemUnidadeAlternativaCalculadoList(itemPedido, descPromocionalList);
	}

	private void initDescPromoPorProdutoHash(Hashtable descPromoHash, String cdEmpresa) {
		if (descPromoPorProdutoHash != null && ValueUtil.valueEquals(cdEmpresa, lastCdEmpresa)) return;
		Vector descPromoList = descPromoHash.getValues();
		descPromoPorProdutoHash = new Hashtable(32);
		int size = descPromoList.size();
		for (int i = 0; i < size; i++) {
			DescPromocional descPromocional = (DescPromocional) descPromoList.items[i];
			String key = getDescPromoPorProdutoHashKey(descPromocional.cdEmpresa, descPromocional.cdRepresentante, descPromocional.cdProduto);
			Hashtable list = (Hashtable) descPromoPorProdutoHash.get(key);
			if (list == null) {
				list = new Hashtable(8);
			}
			list.put(descPromocional.getRowKey(), descPromocional);
			descPromoPorProdutoHash.put(key, list);
		}
	}

	private Vector getDescQtdFromPartialHash(ItemPedido itemPedido, String cdProduto) {
		Hashtable descPromoProdutoHash = (Hashtable) descPromoPorProdutoHash.get(getDescPromoPorProdutoHashKey(itemPedido.cdEmpresa, itemPedido.cdRepresentante, cdProduto));
		if (descPromoProdutoHash == null) {
			return new Vector(1);
		}
		return descPromoProdutoHash.getValues();
	}
	
	private String getDescPromoPorProdutoHashKey(final String cdEmp, final String cdRep, final String cdProduto) {
		if (hashKeyBuffer == null) hashKeyBuffer = new StringBuffer(16);
		hashKeyBuffer.setLength(0);
		return hashKeyBuffer.append(cdEmp).append(";").append(cdRep).append(";").append(cdProduto).toString();
	}

	private void removeDescPromocionalIguaisList(Vector descPromocionalList) {
		int size = descPromocionalList.size();
		for (int i = 0; i < size; i++) {
			DescPromocional descPromocional = (DescPromocional) descPromocionalList.items[i];
			boolean encontrado = false;
			for (int j = 0; j < size; j++) {
				DescPromocional descPromocional2 = (DescPromocional) descPromocionalList.items[j];
				if (!ValueUtil.valueEquals(descPromocional, descPromocional2) && ValueUtil.valueEquals(descPromocional.qtItem, descPromocional2.qtItem) && ValueUtil.valueEquals(descPromocional.vlPctDescontoProduto, descPromocional2.vlPctDescontoProduto)) {
					encontrado = true;
					break;
				}
			}
			if (encontrado) {
				descPromocionalList.removeElementAt(i);
				i--;
				size--;
			}
		}
	}

	private boolean isDescPromocionalComQtdeEncontradoSemRegrasAdicionais(String cdCliente, String cdProduto, String cdGrupoDescCli, String cdGrupoDescProd, String cdTabelaPreco, String cdCondicaoComercial, String cdLocal, DescPromocional descPromocional) {
		return descPromocional.cdCliente.equals(cdCliente) && descPromocional.cdProduto.equals(cdProduto) && descPromocional.cdGrupoDescCli.equals(cdGrupoDescCli) && descPromocional.cdGrupoDescProd.equals(cdGrupoDescProd) && descPromocional.cdTabelaPreco.equals(cdTabelaPreco) && descPromocional.cdCondicaoComercial.equals(cdCondicaoComercial)
				&& descPromocional.cdUf.equals(DescPromocional.CD_CHAVE_VAZIO) && descPromocional.cdTipoPedido.equals(DescPromocional.CD_CHAVE_VAZIO) && descPromocional.cdCondicaoPagamento.equals(DescPromocional.CD_CHAVE_VAZIO) && descPromocional.cdTipoFrete.equals(DescPromocional.CD_CHAVE_VAZIO) && descPromocional.cdLocalEstoque.equals(DescPromocional.CD_CHAVE_VAZIO) && descPromocional.qtItem > 0 && descPromocional.cdLocal.equals(cdLocal);
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(DescPromocional descPromocionalFilter, DescPromocional descPromocional) {
		return descPromocional.cdCliente.equals(descPromocionalFilter.cdCliente) && descPromocional.cdProduto.equals(descPromocionalFilter.cdProduto) && descPromocional.cdGrupoDescCli.equals(descPromocionalFilter.cdGrupoDescCli) && descPromocional.cdGrupoDescProd.equals(descPromocionalFilter.cdGrupoDescProd) && descPromocional.cdTabelaPreco.equals(descPromocionalFilter.cdTabelaPreco) && descPromocional.cdCondicaoComercial.equals(descPromocionalFilter.cdCondicaoComercial)
				&& descPromocional.cdUf.equals(descPromocionalFilter.cdUf) && descPromocional.cdTipoPedido.equals(descPromocionalFilter.cdTipoPedido) && descPromocional.cdCondicaoPagamento.equals(descPromocionalFilter.cdCondicaoPagamento) && descPromocional.cdTipoFrete.equals(descPromocionalFilter.cdTipoFrete) && descPromocional.cdLocalEstoque.equals(descPromocionalFilter.cdLocalEstoque) && descPromocional.qtItem > 0 && descPromocional.cdLocal.equals(descPromocionalFilter.cdLocal);
	}
	
	private boolean encontrouComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(DescPromocional descPromocional, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		boolean isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			} else {
				descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			}
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
				descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			} else {
				descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
				descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			}
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			if (LavenderePdaConfig.usaTabelaPrecoPorCondicaoPagamento) {
				descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			} else {
				descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			}
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(DescPromocional descPromocional, DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld) {
		boolean isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean encontrouCombinacaoComEstadoZero(DescPromocional descPromocional, DescPromocional descPromocionalFilter) {
		descPromocionalFilter.cdUf = DescPromocional.CD_CHAVE_VAZIO;
		return isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesLocalEstoque(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		boolean isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
			descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
			descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesTipoFrete(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
		descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
		descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
		descPromocionalFilter.cdTipoFrete = descPromocionalFilterOld.cdTipoFrete;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoCondComercialTabelaPrecoECondPagamento(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesTabPrecoBeforeCondPagto(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
		descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
		descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesCondPagtoAfterTabPreco(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
		descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
		descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesCondPagtoBeforeTabPreco(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
		descPromocionalFilter.cdCondicaoPagamento = descPromocionalFilterOld.cdCondicaoPagamento;
		descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
			descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
			descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesTabPrecoAfterCondPagto(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
		descPromocionalFilter.cdTabelaPreco = descPromocionalFilterOld.cdTabelaPreco;
		descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouComCombinacoesDeEstadoTipoPedidoECondComercial(descPromocional, descPromocionalFilter, descPromocionalFilterOld);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesCondComercial(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = descPromocionalFilterOld.cdCondicaoComercial;
		descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		}
		if (!isDescPromocionalEncontrado) {
			descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
			descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
			isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		}
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesTipoPedido(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = descPromocionalFilterOld.cdTipoPedido;
		descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		boolean isDescPromocionalEncontrado = isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
		if (!isDescPromocionalEncontrado) {
			isDescPromocionalEncontrado = encontrouCombinacaoComEstadoZero(descPromocional, descPromocionalFilter);
		}
		return isDescPromocionalEncontrado;
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComCombinacoesEstado(DescPromocional descPromocionalFilter, DescPromocional descPromocionalFilterOld, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = descPromocionalFilterOld.cdUf;
		descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		return isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
	}
	
	private boolean isDescPromocionalComQtdeEncontradoComEntidadesZero(DescPromocional descPromocionalFilter, DescPromocional descPromocional) {
		descPromocionalFilter.cdUf = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocionalFilter.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		return isDescPromocionalComQtdeEncontradoComRegrasAdicicionais(descPromocionalFilter, descPromocional);
	}

	public double findVlFinalProdutoDescPromocional(ItemPedido itemPedido, double vlBaseCalculoDescPromo) throws SQLException {
		double vlFinalProduto = 0;
		if (!itemPedido.permiteAplicarDesconto()) {
			return 0;
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			vlBaseCalculoDescPromo = itemPedido.getItemTabelaPreco().getVlBasePrimario(itemPedido.pedido);
		}
		if (vlBaseCalculoDescPromo == 0) {
			return vlFinalProduto;
		}
		DescPromocional descPromocional = itemPedido.getDescPromocional();
		if (descPromocional != null && !itemPedido.possuiDescMaxProdCli()) {
			if (ValueUtil.isNotEmpty(descPromocional.cdEmpresa) && itemPedido.retiraDescItem) {
				itemPedido.vlPctDesconto = 0;
				itemPedido.flPrecoLiberadoSenha = ValueUtil.VALOR_NAO;
				itemPedido.qtItemMinAfterLibPreco = 0d;
				itemPedido.vlItemMinAfterLibPreco = 0d;
				itemPedido.qtItemAfterLibPreco = 0d;
				itemPedido.vlItemAfterLibPreco = 0d;
			}
			if (descPromocional.vlProdutoFinal != 0 && descPromocional.qtItem == 0) {
				vlFinalProduto = descPromocional.vlProdutoFinal;
				if (LavenderePdaConfig.usaUnidadeAlternativa) {
					vlFinalProduto = calculaVlUnidadeAlternativaDescPromocional(itemPedido, vlFinalProduto);
				}
				if (LavenderePdaConfig.usaCondicaoComercialPedido) {
					vlFinalProduto *= aplicaIndiceCondicaoComercial(itemPedido);
				}
				return vlFinalProduto;
			} else if (descPromocional.vlDescontoProduto != 0) {
				vlFinalProduto = vlBaseCalculoDescPromo - descPromocional.vlDescontoProduto;
				return vlFinalProduto <= 0 ? -1 : vlFinalProduto;
			} else if (descPromocional.vlPctDescontoProduto != 0) {
				if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
					vlBaseCalculoDescPromo -= itemPedido.vlReducaoOptanteSimples;
				}
				vlFinalProduto = vlBaseCalculoDescPromo - (descPromocional.vlPctDescontoProduto / 100 * vlBaseCalculoDescPromo);
				return vlFinalProduto <= 0 ? -1 : vlFinalProduto;
			} else if (descPromocional.qtItem != 0) {
				vlFinalProduto = vlBaseCalculoDescPromo;
				return vlFinalProduto <= 0 ? -1 : vlFinalProduto;
			}
		}
		return vlFinalProduto;
	}

	private double aplicaIndiceCondicaoComercial(ItemPedido item) throws SQLException {
		if (ItemPedidoService.getInstance().ignoraIndiceFinanceiroCondComercialTabPrecoOrOProdProm(item)) {
			return 1;
		}
		if (LavenderePdaConfig.isAplicaIndiceCondicaoComercialNoPedido()) {
			return 1;
		}
		
		CondicaoComercial filter = new CondicaoComercial();
		filter.cdCliente = item.pedido.cdCliente;
		filter.cdEmpresa = item.cdEmpresa;
		filter.cdRepresentante = item.cdRepresentante;
		filter.cdCondicaoComercial  = item.pedido.cdCondicaoComercial;
		double value = CondicaoComercialService.getInstance().findIndiceCondicaoComercial(filter);
		return value == 0 ? 1 : value;
	}
	
	public double calculaVlUnidadeAlternativaDescPromocional(ItemPedido itemPedido, double vlFinalProduto) throws SQLException {
		double newVlBaseItemPedido = vlFinalProduto;
		ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
		double currentNuConversaoUnidade = itemPedido.nuConversaoUnidade;
		if (produtoUnidade != null) {
			itemPedido.nuConversaoUnidade = 1;
			newVlBaseItemPedido = ItemPedidoService.getInstance().calculaVlUnidadeAlternativa(itemPedido, produtoUnidade, vlFinalProduto, false);
			itemPedido.nuConversaoUnidade = currentNuConversaoUnidade;
		}
		return newVlBaseItemPedido;
	}
	
	private Vector getDescPromocionalQtItemUnidadeAlternativaCalculadoList(ItemPedido itemPedido, Vector descPromocionalList) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (produtoUnidade != null) {
				for (int i = 0; i < descPromocionalList.size(); i++) {
					calcDescPromocionalUnidadeAlternativa((DescPromocional) descPromocionalList.items[i], produtoUnidade);
				}
				return getDescPromocionalListSemFaixasDuplicadas(descPromocionalList);
			}
		}
		return descPromocionalList;
	}
	
	protected Vector getDescPromocionalListSemFaixasDuplicadas(Vector descPromocionalList) {
		Vector descPromocionalFinalList = new Vector();
		if (ValueUtil.isNotEmpty(descPromocionalList)) {
			for (int i = 0; i < descPromocionalList.size(); i++) {
				DescPromocional descPromocional = (DescPromocional) descPromocionalList.items[i];
				boolean inseridoVetorFinal = false;
				for (int j = 0; j < descPromocionalFinalList.size(); j++) {
					DescPromocional descQuantidadeTemp = (DescPromocional) descPromocionalFinalList.items[j];
					if (descPromocional.qtItem == descQuantidadeTemp.qtItem) {
						if (descPromocional.vlPctDescontoProduto > descQuantidadeTemp.vlPctDescontoProduto) {
							descPromocionalFinalList.removeElement(descQuantidadeTemp);
						} else {
							inseridoVetorFinal = true;
						}
					}
				}
				if (!inseridoVetorFinal) {
					descPromocionalFinalList.addElement(descPromocional);
				}
			}
		}
		return descPromocionalFinalList;
	}
	
	protected void calcDescPromocionalUnidadeAlternativa(DescPromocional descPromocional, ProdutoUnidade produtoUnidade) {
		double qtItem;
		double precisaoDecimal;
		if (produtoUnidade.isMultiplica()) {
			qtItem = descPromocional.qtItem / produtoUnidade.nuConversaoUnidade;
		} else {
			qtItem = descPromocional.qtItem * produtoUnidade.nuConversaoUnidade;
		}
		descPromocional.qtItem = ValueUtil.getIntegerValue(ValueUtil.round(qtItem));
		precisaoDecimal = ValueUtil.round(qtItem) - ValueUtil.getIntegerValue(ValueUtil.round(qtItem));
		if (ValueUtil.round(precisaoDecimal) > 0) {
			descPromocional.qtItem += 1;
		}
	}
	
	public void loadMaiorFaixaDescPromocionalPorQuantidadeItemPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido == null || !isItemPedidoPossuiDescPromocionalPorQtde(itemPedido)) return;
		int size = itemPedido.descPromocionalComQtdList.size();

		// qsortInt para ordenar a lista pelo qtItem, pois o mesmo deve estar ordenado do menor(qtItem) para maior(qtItem).
		SortUtil.qsortInt(itemPedido.descPromocionalComQtdList.items, 0, size - 1, true);

		itemPedido.qtItemPedidoMinimo = ((DescPromocional) itemPedido.descPromocionalComQtdList.items[0]).qtItem;
		DescPromocional descPromocional;
		for (int i = size - 1; i >= 0; i--) {
			descPromocional = (DescPromocional) itemPedido.descPromocionalComQtdList.items[i];
			if (itemPedido.getQtItemFisico() >= descPromocional.qtItem) {
				itemPedido.descPromocional = descPromocional;
				return;
			}
		}
		itemPedido.descPromocional = new DescPromocional();
	}
	
	public boolean isItemPedidoPossuiDescPromocional(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && (!itemPedido.getDescPromocional().equals(new DescPromocional()));
	}
	
	public boolean isItemPedidoPossuiDescPromocionalComRentabilidade(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && (!itemPedido.getDescPromocional().equals(new DescPromocional())) 
				&& itemPedido.getDescPromocional().vlPctRentabilidade != 0d;
	}

	public boolean isProdutoPossuiValorNoGrupoDescPromo(Pedido pedido, ProdutoBase produto, String cdTabelaPreco) throws SQLException {
		if (produto == null) {
			return false;
		}
		if (produto.descPromocionalCarregado) {
			return produto.possuiDescPromocional;
		}
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.cdProduto = produto.cdProduto;
		itemPedido.cdTabelaPreco = ValueUtil.isEmpty(pedido.cdTabelaPreco) ? cdTabelaPreco : pedido.cdTabelaPreco;
		itemPedido.setPedido(pedido);
		return loadDescPromocional(itemPedido, produto);
	}

	public boolean isItemPedidoPossuiDescPromocionalPorQtde(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && LavenderePdaConfig.usaDescontoQtdeNoGrupoDescPromocional && (itemPedido.getDescPromocionalComQtdList().size() > 0);
	}
	
	public Vector findAllByExampleCombo(BaseDomain domain) throws java.sql.SQLException {
		return DescPromocionalDbxDao.getInstance().findAllByExampleCombo(domain);
	}

	public void clearDescPromoPorProdutoHash() {
    	descPromoPorProdutoHash = null;
	}

}
