package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GiroProdutoPdbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class GiroProdutoService extends CrudService {

    private static GiroProdutoService instance;

    private GiroProdutoService() {
        //--
    }

    public static GiroProdutoService getInstance() {
        if (instance == null) {
            instance = new GiroProdutoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return GiroProdutoPdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException {
    	/**/
    }

    public Vector findAllProdutosGiroProdutoByCliente(Cliente cliente) throws SQLException {
    	return findAllProdutosGiroProdutoByClienteAndPedido(cliente, null, false);
    }
    
	public Vector findAllProdutosGiroProdutoByClienteAndPedido(final Cliente cliente, final Pedido pedido, boolean filterByCdItemGradeDefault) throws SQLException {
		return findAllByExampleSummary(getGiroProdutoFilterByClienteAndPedido(cliente, pedido, filterByCdItemGradeDefault));
	}
	
	public GiroProduto getGiroProdutoFilterByClienteAndPedido(final Cliente cliente, final Pedido pedido, boolean filterByCdItemGradeDefault) throws SQLException {
		GiroProduto giroProdutoFilter = new GiroProduto();
		giroProdutoFilter.cdEmpresa = cliente.cdEmpresa;
		giroProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GiroProduto.class);
		giroProdutoFilter.cdCliente = cliente.cdCliente;
		giroProdutoFilter.produtoFilter = new Produto();
		giroProdutoFilter.produtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		giroProdutoFilter.produtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(cliente.cdGrupoPermProd)) {
			giroProdutoFilter.produtoFilter.cdGrupoPermProd = cliente.cdGrupoPermProd;
		}
		if (filterByCdItemGradeDefault) {
			giroProdutoFilter.cdItemGrade1 = GiroProduto.CD_ITEMGRADE_DEFAULT;
			giroProdutoFilter.cdItemGrade2 = GiroProduto.CD_ITEMGRADE_DEFAULT;
			giroProdutoFilter.cdItemGrade3 = GiroProduto.CD_ITEMGRADE_DEFAULT;
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			giroProdutoFilter.produtoClienteFilter = ProdutoClienteService.getInstance().getProdutoClienteFilter(cliente.cdCliente);
		}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
			giroProdutoFilter.clienteProdutoFilter = ClienteProdutoService.getInstance().getClienteProdutoFilter(cliente.cdCliente);
		}
		if (pedido != null) {
	    	giroProdutoFilter.itemPedidoFilter = new ItemPedido();
	    	giroProdutoFilter.itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
	    	giroProdutoFilter.itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
	    	giroProdutoFilter.itemPedidoFilter.nuPedidoListFilter = new String[] {pedido.nuPedido};
	    	if (LavenderePdaConfig.usaApenasGiroProdutoPorTabelaPrecoPedido) {
	    		giroProdutoFilter.cdTabelaPrecoList = new String[] {pedido.cdTabelaPreco, TabelaPreco.CDTABELAPRECO_VALOR_ZERO};
	    	}
	    	if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
	    		TipoPedido tipoPedido = pedido.getTipoPedido();
	    		giroProdutoFilter.produtoFilter.flExcecaoProduto = ValueUtil.isNotEmpty(tipoPedido.flExcecaoProduto) ? tipoPedido.flExcecaoProduto : ValueUtil.VALOR_NAO;
	    		giroProdutoFilter.produtoFilter.cdTipoPedidoFilter = pedido.cdTipoPedido;
			}
	    	if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
	    		giroProdutoFilter.produtoCondPagtoFilter = ProdutoCondPagtoService.getInstance().getProdutoCondPagtoFilter(pedido.cdCondicaoPagamento);
	    	}
    	}
		return giroProdutoFilter;
	}
	
	public int findCountProdutosGiroProduto(Cliente cliente) throws SQLException {
		GiroProduto giroProdutoFilter = new GiroProduto();
		giroProdutoFilter.cdEmpresa = cliente.cdEmpresa;
		giroProdutoFilter.cdRepresentante = cliente.cdRepresentante;
		giroProdutoFilter.cdCliente = cliente.cdCliente;
		return countByExample(giroProdutoFilter);
	}
	
	public Pedido criaPedidoListaGiroProduto(Cliente cliente) throws SQLException{
		if (findCountProdutosGiroProduto(cliente) > 0) {
			Pedido pedido = PedidoService.getInstance().createNewPedido(cliente);
			PedidoService.getInstance().insert(pedido);
			pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(pedido.getRowKey());
			pedido.setSugestaoPedidoGiroProduto(true);
			return pedido;
		} else {
			return new Pedido();
		}
	}

	public Pedido criaPedidoBaseadoGiroProduto(Cliente cliente, Vector giroProdutoList, boolean isFromGiroProduto) throws SQLException {
		int size = giroProdutoList.size();
		Hashtable giroHash = new Hashtable(size);
		for (int i = 0; i < size; i++) {
			GiroProduto giroProduto = (GiroProduto) giroProdutoList.items[i];
			if (giroHash.get(giroProduto.cdProduto) != null) {
				((Vector) giroHash.get(giroProduto.cdProduto)).addElement(giroProduto);
			} else {
				Vector giroList = new Vector();
				giroList.addElement(giroProduto);
				giroHash.put(giroProduto.cdProduto, giroList);
			}
		}
		//--
		Pedido pedido = PedidoService.getInstance().createNewPedido(cliente);
		if (isFromGiroProduto && pedido == null) {
			return null;
		}
		PedidoService.getInstance().insert(pedido);
		pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(pedido.getRowKey());
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(pedido);
		}
		Vector keys = giroHash.getKeys();
		int keysSize = keys.size();
		Vector itemPedidoList = new Vector(keysSize);
		for (int i = 0; i < keysSize; i++) {
			Vector giroList = (Vector) giroHash.get(keys.items[i]);
			int giroSize = giroList.size();
			double qtCompra = 0d;
			GiroProduto giroProduto = (GiroProduto) giroList.items[0];
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			if (LavenderePdaConfig.isConfigGradeProduto() && giroSize > 1) {
				for (int j = 0; j < giroSize; j++) {
					GiroProduto giroProdutoGrade = (GiroProduto) giroList.items[j];
					if (!ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(giroProdutoGrade.cdItemGrade1)) {
						qtCompra += giroProdutoGrade.qtCompra;
						if (!ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(giroProduto.cdItemGrade2)) {
							createItemPedidoGradeByGiroProduto(pedido, giroProdutoGrade, itemPedido, j);
						}
						itemPedido.cdItemGrade1 = giroProdutoGrade.cdItemGrade1;
					}
				}
			} else {
				qtCompra = giroProduto.qtCompra;
				if (LavenderePdaConfig.isConfigGradeProduto() && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(giroProduto.cdItemGrade1)) {
					if (!ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(giroProduto.cdItemGrade2)) {
						createItemPedidoGradeByGiroProduto(pedido, giroProduto, itemPedido, 1);
					}
					itemPedido.cdItemGrade1 = giroProduto.cdItemGrade1;
				}
			}
			itemPedido.pedido = pedido;
			itemPedido.giroProduto = giroProduto;
			itemPedido.cdEmpresa = giroProduto.cdEmpresa;
			itemPedido.cdRepresentante = giroProduto.cdRepresentante;
			itemPedido.flOrigemPedido = pedido.flOrigemPedido;
			itemPedido.nuPedido = pedido.nuPedido;
			itemPedido.cdProduto = giroProduto.cdProduto;
			itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
				itemPedido.qtItemDesejado = qtCompra;
				double qtEstoqueDisponivel = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, Estoque.CD_LOCAL_ESTOQUE_PADRAO);
				itemPedido.setQtItemFisico(qtCompra > qtEstoqueDisponivel ? qtEstoqueDisponivel : qtCompra);
			} else {
				itemPedido.setQtItemFisico(qtCompra);
			}
			itemPedido.vlItemPedido = giroProduto.vlUnitario;
			itemPedido.cdTabelaPreco = LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && LavenderePdaConfig.ocultaTabelaPrecoPedido ? giroProduto.cdTabelaPreco : pedido.cdTabelaPreco;
			itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
			itemPedido.flTipoCadastroItem = null;
			itemPedido.qtdCreditoDesc = 0;
			itemPedido.cdProdutoCreditoDesc = null;
			//--
			if (pedido.getCliente() != null) {
				itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
			}
			itemPedido.cdUnidade = itemPedido.getProduto().cdUnidade;
			if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
				itemPedido.flOrigemEscolhaItemPedido = ItemPedido.FLORIGEMESCOLHA_GIROPRODUTO;
			}
			itemPedidoList.addElement(itemPedido);
		}
		pedido.showMessageLimiteCredito = false;
		PedidoService.getInstance().insertItemPedidoByItemPedidoList(pedido, itemPedidoList, Pedido.SUGESTAO_PEDIDO_BASEADO_GIRO);
		return pedido;	
	}
	
	private void createItemPedidoGradeByGiroProduto(Pedido pedido, GiroProduto giroProduto, ItemPedido itemPedido, int nuSeqProduto) {
		ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
		itemPedidoGrade.cdEmpresa = giroProduto.cdEmpresa;
		itemPedidoGrade.cdRepresentante = giroProduto.cdRepresentante;
		itemPedidoGrade.cdItemGrade1 = giroProduto.cdItemGrade1;
		itemPedidoGrade.cdItemGrade2 = giroProduto.cdItemGrade2;
		itemPedidoGrade.cdItemGrade3 = giroProduto.cdItemGrade3;
		itemPedidoGrade.cdProduto = giroProduto.cdProduto;
		itemPedidoGrade.flOrigemPedido = pedido.flOrigemPedido;
		itemPedidoGrade.nuPedido = pedido.nuPedido;
		itemPedidoGrade.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedidoGrade.nuSeqProduto = nuSeqProduto;
		itemPedidoGrade.qtItemFisico = giroProduto.qtCompra;
		itemPedido.itemPedidoGradeList.addElement(itemPedidoGrade);
	}
	
	public boolean validateUnidadeItemPedidoByGiro(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, itemPedido.cdUnidade, pedido.cdTipoPedido)) {
			Vector produtoUnidadeList = ProdutoUnidadeService.getInstance().findAllByProduto(itemPedido, itemPedido.getProduto(), pedido.cdTabelaPreco);
			int sizePList = produtoUnidadeList.size();
			for (int j = 0; j < sizePList; j++) {
				ProdutoUnidade produtoUnidade = (ProdutoUnidade)produtoUnidadeList.items[j];
				itemPedido.setProdutoUnidade(produtoUnidade);
				itemPedido.cdUnidade = produtoUnidade.cdUnidade;
				if (!RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, itemPedido.cdUnidade, pedido.cdTipoPedido)) {
					itemPedido.vlItemPedido = ProdutoUnidadeService.getInstance().calculateUnidadeAlternativa(pedido, produtoUnidade, itemPedido.getItemTabelaPreco(), itemPedido.vlItemPedido, itemPedido.getProduto());
					PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
					return true;
				}
			}
		}
		return false;
	}

	public double loadVlUltPrecoProdutoGiroProduto(GiroProduto giroProduto) throws SQLException {
		if (giroProduto.cdProduto == null || giroProduto.cdCliente == null || giroProduto.pedidoAtual == null) return 0d;
		if (giroProduto.itemPedidoUltPreco != null) return giroProduto.itemPedidoUltPreco.vlItemPedido;
		giroProduto.itemPedidoUltPreco = ItemPedidoService.getInstance().findItemPedidoUltimoPedidoCliente(giroProduto.cdCliente, giroProduto.cdProduto, giroProduto.pedidoAtual.nuPedido);
		return giroProduto.itemPedidoUltPreco.vlItemPedido;
	}

	public void loadProdutoGiroProduto(GiroProduto giroProduto) throws SQLException {
		if (giroProduto.produto == null) {
			giroProduto.produto = ProdutoService.getInstance().getProduto(giroProduto.cdEmpresa, giroProduto.cdRepresentante, giroProduto.cdProduto);
		}
	}

	public void loadGradesGiroProduto(GiroProduto giroProduto) throws SQLException {
		if (!LavenderePdaConfig.isConfigGradeProduto() || LavenderePdaConfig.usaGradeProduto5() || giroProduto.produtoGrade != null) return;
    	giroProduto.produtoGrade = ProdutoGradeService.getInstance().getProdutoGradeByGiroProduto(giroProduto);
    	if (giroProduto.produtoGrade == null) return;
		giroProduto.itemGrade1 = ItemGradeService.getInstance().getItemGrade(giroProduto.produtoGrade.cdTipoItemGrade1, giroProduto.produtoGrade.cdItemGrade1);
		giroProduto.itemGrade2 = ItemGradeService.getInstance().getItemGrade(giroProduto.produtoGrade.cdTipoItemGrade2, giroProduto.produtoGrade.cdItemGrade2);
		if (LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) {
			giroProduto.itemGrade3 = ItemGradeService.getInstance().getItemGrade(giroProduto.produtoGrade.cdTipoItemGrade3, giroProduto.produtoGrade.cdItemGrade3);
		}
	}

	public String getMessageColunaRelGiroProduto(int pos) {
		String valorAtributo = null;
		switch (pos) {
		case 0:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaUltPreco();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_ULTPRECO : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 1:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaMedia();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_QTMEDIASEMANAL : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 2:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaMaiorCompra();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_QTMAIORCOMPRA : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 3:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaQtdMedia();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_QTMEDIA : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 4:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaVlUnit();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_VLUNIT : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 5:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaDia();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_DSDIA : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 6:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaGrade1();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_ITEMGRADE1 : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 7:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaGrade2();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_ITEMGRADE2 : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 8:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaGrade3();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_ITEMGRADE3 : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		case 9:
			valorAtributo = LavenderePdaConfig.relGiroProdutoUsaObservacao();
			return ValueUtil.VALOR_SIM.equalsIgnoreCase(valorAtributo) ? Messages.GIROPRODUTO_LABEL_OBSERVACAO : !ValueUtil.VALOR_NAO.equalsIgnoreCase(valorAtributo) ? valorAtributo : FrameworkMessages.CAMPO_ID;
		default:
			return FrameworkMessages.CAMPO_ID;
		}
	}

	public GiroProduto findGiroProdutoByProduto(Produto produto, Pedido pedido) throws SQLException {
		GiroProduto giroProdutoFilter = new GiroProduto();
		giroProdutoFilter.cdEmpresa = pedido.cdEmpresa;
		giroProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GiroProduto.class);
		giroProdutoFilter.cdCliente = pedido.cdCliente;
		giroProdutoFilter.cdProduto = produto.cdProduto;
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			giroProdutoFilter.cdItemGrade1 = produto.cdItemGrade1;
			giroProdutoFilter.cdItemGrade2 = produto.cdItemGrade2;
			giroProdutoFilter.cdItemGrade3 = produto.cdItemGrade3;
		} else {
			giroProdutoFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			giroProdutoFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			giroProdutoFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		}
		return (GiroProduto) findByPrimaryKey(giroProdutoFilter);
	}
}
