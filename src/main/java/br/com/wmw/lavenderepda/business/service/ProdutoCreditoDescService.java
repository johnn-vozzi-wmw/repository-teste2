package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoCreditoDescDbxDao;
import totalcross.util.Vector;

public class ProdutoCreditoDescService extends CrudService {

    private static ProdutoCreditoDescService instance = null;
    
    private ProdutoCreditoDescService() {
        //--
    }
    
    public static ProdutoCreditoDescService getInstance() {
        if (instance == null) {
            instance = new ProdutoCreditoDescService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoCreditoDescDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public int getQtCreditosAplicar(Pedido pedido, ItemPedido itemPedido, int qtdCredito) throws SQLException {
		if (qtdCredito == 0) {
			throw new ValidationException(Messages.PRODUTOCREDITODESCONTO_QTCREDITOS_NAO_INFORMADA);
		}
		if (qtdCredito > pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido + itemPedido.qtdCreditoDesc) {
			throw new ValidationException(Messages.PRODUTOCREDITODESCONTO_QTCREDITOS_INSUFICIENTES);
		}
		ProdutoCreditoDesc produtoCreditoDesconto = getProdutoCreditoDescontoParaAplicacao(itemPedido.cdEmpresa, itemPedido.cdProduto, ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO);
		if (produtoCreditoDesconto != null) {
			double qtItem = itemPedido.getQtItemFisico();
			if (LavenderePdaConfig.usaUnidadeAlternativa) { 
				ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
				if (produtoUnidade != null) {
					double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
					qtItem = qtItem * nuConversaoUnidade;
				}
			}
			if (produtoCreditoDesconto.qtItem > 0) {
				if (qtItem > qtdCredito * produtoCreditoDesconto.qtItem) {
					throw new ValidationException(Messages.PRODUTOCREDITODESCONTO_QTCREDITOS_MENOR_NECESSARIO);
				}
				itemPedido.cdProdutoCreditoDesc = produtoCreditoDesconto.cdProdutoCreditoDesc;
				itemPedido.produtoCreditoDesc = produtoCreditoDesconto;
				return (int) Math.ceil(qtItem / produtoCreditoDesconto.qtItem);
			}
		}
		return 0;
	}
	
	public int getQtCreditosNecessario(ItemPedido itemPedido) throws SQLException {
		ProdutoCreditoDesc produtoCreditoDesconto = getProdutoCreditoDescontoParaAplicacao(itemPedido.cdEmpresa, itemPedido.cdProduto, ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO);
		if (produtoCreditoDesconto != null) {
			double qtItem = itemPedido.getQtItemFisico();
			if (LavenderePdaConfig.usaUnidadeAlternativa) { 
				ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
				if (produtoUnidade != null) {
					double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
					qtItem = qtItem * nuConversaoUnidade;
				}
			}
			if (produtoCreditoDesconto.qtItem > 0) {
				return (int) Math.ceil(qtItem / produtoCreditoDesconto.qtItem);
			}
		}
		return 0;
	}

	public ProdutoCreditoDesc getProdutoCreditoDescontoParaAplicacao(String cdEmpresa, String cdProduto, String flTipoCadastro) throws SQLException {
		ProdutoCreditoDesc produtoCreditoDescontoFilter = new ProdutoCreditoDesc();
		produtoCreditoDescontoFilter.cdEmpresa = cdEmpresa;
		produtoCreditoDescontoFilter.cdProduto = cdProduto;
		produtoCreditoDescontoFilter.flTipoCadastroProduto = flTipoCadastro;
		Vector prodCredDescList = findAllByExample(produtoCreditoDescontoFilter);
		if (ValueUtil.isNotEmpty(prodCredDescList)) {
			int size = prodCredDescList.size();
			for (int i = 0; i < size; i++) {
				ProdutoCreditoDesc produtoCreditoDesconto = (ProdutoCreditoDesc) prodCredDescList.items[i];
				if (produtoCreditoDesconto.isVigente()) {
					return produtoCreditoDesconto;
				}
			}
		}
		return null;
	}
	
	public ProdutoCreditoDesc getProdutoCreditoDescontoDoItem(String cdEmpresa, String cdProduto, String cdProdutoCreditoDesc) throws SQLException {
		ProdutoCreditoDesc produtoCreditoDescontoFilter = new ProdutoCreditoDesc();
		produtoCreditoDescontoFilter.cdEmpresa = cdEmpresa;
		produtoCreditoDescontoFilter.cdProduto = cdProduto;
		produtoCreditoDescontoFilter.cdProdutoCreditoDesc = cdProdutoCreditoDesc;
		return (ProdutoCreditoDesc) findByRowKey(produtoCreditoDescontoFilter.getRowKey());
	}

	public void retiraCreditoDescontoItem(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		pedido.qtdCreditoDescontoConsumido -= itemPedido.qtdCreditoDesc;
		itemPedido.qtdCreditoDesc = 0;
		itemPedido.flTipoCadastroItem = null;
		itemPedido.cdProdutoCreditoDesc = null;
		PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
	}

	public void geraCreditoInsercaoItemSeNecessario(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isItemVendaNormal() && !isPrecoNormalMaiorPrecoComDescPromo(itemPedido)) {
			ProdutoCreditoDesc produtoCreditoDesconto = getProdutoCreditoDescontoParaAplicacao(itemPedido.cdEmpresa, itemPedido.cdProduto, ProdutoCreditoDesc.FLTIPOCADASTRO_QTD);
			if (produtoCreditoDesconto != null) {
				double qtItem = itemPedido.getQtItemFisico();
				if (LavenderePdaConfig.usaUnidadeAlternativa) { 
					ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
					if (produtoUnidade != null) {
						double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
						qtItem = qtItem * nuConversaoUnidade;
					}
				}
				if (produtoCreditoDesconto.qtItem > 0 && itemPedido.vlPctDesconto == 0) {
					int qtCreditoItem = (int) (qtItem / produtoCreditoDesconto.qtItem);
					if (qtCreditoItem > 0) {
						itemPedido.flTipoCadastroItem = ProdutoCreditoDesc.FLTIPOCADASTRO_QTD;
						itemPedido.qtdCreditoDesc = qtCreditoItem;
						itemPedido.cdProdutoCreditoDesc = produtoCreditoDesconto.cdProdutoCreditoDesc;
						itemPedido.produtoCreditoDesc = produtoCreditoDesconto;
						itemPedido.pedido.qtdCreditoDescontoGerado += qtCreditoItem;
					}
				}
			}
		}
	}

	public boolean isPrecoNormalMaiorPrecoComDescPromo(final ItemPedido itemPedidoComDescPromo) throws SQLException {
		if (itemPedidoComDescPromo.getDescPromocional() != null && !itemPedidoComDescPromo.getDescPromocional().equals(new DescPromocional())) {
			ItemPedido itemPedidoNormal = (ItemPedido) itemPedidoComDescPromo.clone();
			itemPedidoNormal.qtdCreditoDesc = 0;
			itemPedidoNormal.flTipoCadastroItem = null;
			itemPedidoNormal.cdProdutoCreditoDesc = null;
			itemPedidoNormal.descPromocional = null; 
			itemPedidoNormal.bloqueiaAplicaDescPromocional = true;
			PedidoService.getInstance().resetDadosItemPedido(itemPedidoNormal.pedido, itemPedidoNormal);
			return itemPedidoNormal.vlItemPedido > itemPedidoComDescPromo.vlItemPedido;
		}
		return false;
	}

	public void atualizaQtdCreditoPedido(ItemPedido itemPedido) throws SQLException {
		if (!itemPedido.isItemVendaNormal() || isPrecoNormalMaiorPrecoComDescPromo(itemPedido) || ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
			return;
		}
		final int qtdCreditoDescOld = itemPedido.qtdCreditoDesc;
		final String flTipoCadastroItemOld = itemPedido.flTipoCadastroItem;
		final String cdProdutoCreditoDescOld = itemPedido.cdProdutoCreditoDesc;
		itemPedido.flTipoCadastroItem = itemPedido.cdProdutoCreditoDesc = null;
		itemPedido.qtdCreditoDesc = 0; 
		ProdutoCreditoDesc produtoCreditoDesconto = getProdutoCreditoDescontoParaAplicacao(itemPedido.cdEmpresa, itemPedido.cdProduto, ProdutoCreditoDesc.FLTIPOCADASTRO_QTD);
		Pedido pedido = itemPedido.pedido;
		if (produtoCreditoDesconto != null) {
			double qtItem = itemPedido.getQtItemFisico();
			if (produtoCreditoDesconto.qtItem > 0 && itemPedido.vlPctDesconto == 0) {
				if (LavenderePdaConfig.usaUnidadeAlternativa) { 
					ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
					if (produtoUnidade != null) {
						double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
						qtItem *= nuConversaoUnidade;
					}
				}
				int qtCreditoItem = (int) (qtItem / produtoCreditoDesconto.qtItem);
				if (qtCreditoItem > 0) {
					itemPedido.qtdCreditoDesc = qtCreditoItem;
					itemPedido.flTipoCadastroItem = ProdutoCreditoDesc.FLTIPOCADASTRO_QTD;
					itemPedido.cdProdutoCreditoDesc = produtoCreditoDesconto.cdProdutoCreditoDesc;
				}
			}
			boolean houveMudancaCreditoItem = itemPedido.qtdCreditoDesc != qtdCreditoDescOld;
			double creditosDisponiveisPedido = pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido;
			if (houveMudancaCreditoItem && pedido.qtdCreditoDescontoConsumido > 0 && creditosDisponiveisPedido - qtdCreditoDescOld + itemPedido.qtdCreditoDesc < 0) {
				itemPedido.qtdCreditoDesc = qtdCreditoDescOld;
				itemPedido.flTipoCadastroItem = flTipoCadastroItemOld;
				itemPedido.cdProdutoCreditoDesc = cdProdutoCreditoDescOld;
				throw new ValidationException(Messages.PRODUTOCREDITODESCONTO_ERRO_ATUALIZAR_ITEM);
			}
		}
		if (itemPedido.qtdCreditoDesc > 0 || qtdCreditoDescOld > 0) {
			pedido.qtdCreditoDescontoGerado += itemPedido.qtdCreditoDesc - qtdCreditoDescOld;
		}
	}

	public void deleteItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		if (ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(itemPedido.flTipoCadastroItem)) {
			if (itemPedido.qtdCreditoDesc > pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido && pedido.qtdCreditoDescontoConsumido > 0) {
				throw new ValidationException(Messages.PRODUTOCREDITODESCONTO_ERRO_EXCLUIR_ITEM);
			} else {
				pedido.qtdCreditoDescontoGerado -= itemPedido.qtdCreditoDesc;
			}
		}
	}

	public void loadCreditosPedido(Pedido pedido) throws SQLException {
		if (pedido.isPedidoVenda()) {
			pedido.qtdCreditoDescontoGerado = 0;
			pedido.qtdCreditoDescontoConsumido = 0;
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(itemPedido.flTipoCadastroItem)) {
					pedido.qtdCreditoDescontoGerado += itemPedido.qtdCreditoDesc;
				} else if (ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
					pedido.qtdCreditoDescontoConsumido += itemPedido.qtdCreditoDesc;
				}
			}
		}
	}

//	public boolean validaAlteracaoQtdItemComCredito(ItemPedido itemPedido, double qtItemNew) throws SQLException {
//		ProdutoCreditoDesc produtoCreditoDesconto = itemPedido.getProdutoCreditoDesc();
//		if (produtoCreditoDesconto != null && ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(produtoCreditoDesconto.flTipoCadastroProduto) && itemPedido.qtItemFisico != qtItemNew) {
//			if (LavenderePdaConfig.usaUnidadeAlternativa) { 
//				ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
//				if (produtoUnidade != null) {
//					double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
//					qtItemNew = qtItemNew * nuConversaoUnidade;
//				}
//			}
//			if (produtoCreditoDesconto.qtItem > 0) {
//				if (qtItemNew > itemPedido.qtdCreditoDesc * produtoCreditoDesconto.qtItem) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	public boolean validaTrocaUnidadeAlternativaItemComDesconto(ItemPedido itemPedido, String cdUnidade) throws SQLException {
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produtoUnidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoUnidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoUnidade.cdUnidade = cdUnidade;
		produtoUnidade.cdProduto = itemPedido.cdProduto;
		produtoUnidade.cdItemGrade1 = LavenderePdaConfig.usaGradeProduto1() ? itemPedido.cdItemGrade1 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		produtoUnidade = (ProdutoUnidade) ProdutoUnidadeService.getInstance().findByRowKey(produtoUnidade.getRowKey());
		double qtItemNew = itemPedido.getQtItemFisico();
		double qtItemOld = itemPedido.getQtItemFisico();
		if (produtoUnidade != null) {
			qtItemNew = itemPedido.getQtItemFisico() * produtoUnidade.nuConversaoUnidade;
		}
		ProdutoUnidade produtoUni = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
		if (produtoUni != null) {
			qtItemOld = qtItemOld * produtoUni.nuConversaoUnidade;
		}
		ProdutoCreditoDesc produtoCreditoDesconto = itemPedido.getProdutoCreditoDesc();
		if (qtItemNew != produtoCreditoDesconto.qtItem * itemPedido.qtdCreditoDesc) {
			return false;
		}
		return true;
	}

//	public void validaTrocaUnidadeAlternativaItemGerouCredito(ItemPedido itemPedido, String cdUnidade) throws SQLException {
//		double qtItem = itemPedido.qtItemFisico;
//		double qtItemOld = itemPedido.qtItemFisico;
//		ProdutoUnidade produtoUnidadeNovo = new ProdutoUnidade();
//		produtoUnidadeNovo.cdEmpresa = SessionLavenderePda.cdEmpresa;
//		produtoUnidadeNovo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
//		produtoUnidadeNovo.cdUnidade = cdUnidade;
//		produtoUnidadeNovo.cdProduto = itemPedido.cdProduto;
//		produtoUnidadeNovo.cdItemGrade1 = LavenderePdaConfig.usaGradeProduto == 1 ? itemPedido.cdItemGrade1 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
//		produtoUnidadeNovo = (ProdutoUnidade) ProdutoUnidadeService.getInstance().findByRowKey(produtoUnidadeNovo.getRowKey());
//		if (produtoUnidadeNovo != null) {
//			qtItem = itemPedido.qtItemFisico * produtoUnidadeNovo.nuConversaoUnidade;
//		}
//		//--
//		ProdutoCreditoDesc produtoCreditoDesconto = itemPedido.getProdutoCreditoDesc();
//		ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
//		if (produtoUnidade != null) {
//			double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
//			qtItemOld = qtItemOld * nuConversaoUnidade;
//		}
//		if (produtoCreditoDesconto.qtItem > 0) {
//			int qtCreditoItem = (int) (qtItem / produtoCreditoDesconto.qtItem);
//			int qtCreditoItemOld = (int) (qtItemOld / produtoCreditoDesconto.qtItem);
//			if (qtCreditoItem < qtCreditoItemOld) {
//				if (qtCreditoItemOld - qtCreditoItem > itemPedido.pedido.qtdCreditoDescontoGerado - itemPedido.pedido.qtdCreditoDescontoConsumido && itemPedido.pedido.qtdCreditoDescontoConsumido > 0) {
//					throw new ValidationException(Messages.PRODUTOCREDITODESCONTO_ERRO_ATUALIZAR_ITEM);
//				}
//			}
//		}
//	}

	public boolean validaProdutoSemVigencia(Pedido pedido) throws SQLException {
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPed = (ItemPedido) pedido.itemPedidoList.items[i];
			if (ValueUtil.isNotEmpty(itemPed.flTipoCadastroItem)) {
				ProdutoCreditoDesc pcd = itemPed.getProdutoCreditoDesc();
				if (pcd == null || !pcd.isVigente()) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector findAllByItemPedidoList(Pedido pedido, BaseDomain domain) throws SQLException {
		return ProdutoCreditoDescDbxDao.getInstance().findAllByItemPedidoList(pedido, domain);
	}

	public void loadItemPedidoAvisaCreditoDescontoList(Pedido pedido) throws SQLException {
		pedido.itemPedidoCredDescList = new Vector();
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			ProdutoCreditoDesc produtoCreditoDesconto = itemPedido.getProdutoCreditoDesc();
			if (produtoCreditoDesconto != null && ProdutoCreditoDesc.FLTIPOCADASTRO_QTD.equals(produtoCreditoDesconto.flTipoCadastroProduto) && produtoCreditoDesconto.qtItem > 0) {
				double qtItemPedido = itemPedido.getQtItemFisico();
				if (LavenderePdaConfig.usaUnidadeAlternativa) {
					ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
					if (produtoUnidade != null) {
						double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
						qtItemPedido = qtItemPedido * nuConversaoUnidade;
					}
				}
				double result = ValueUtil.round(qtItemPedido / produtoCreditoDesconto.qtItem);
				int intPart = (int) result; 
				if (result - intPart >= ValueUtil.round(LavenderePdaConfig.vlPctAvisaCreditoDesconto / 100)) {
					double partFrac = result - intPart;
					itemPedido.qtdItemGerarCred = (int) Math.ceil((1 - partFrac) * produtoCreditoDesconto.qtItem);
					pedido.itemPedidoCredDescList.addElement(itemPedido);
				}
			}
		}
	}
	
	public double getVlItemIndicesCliECondComercial(ProdutoCreditoDesc produtoCreditoDesc, Pedido pedido) throws SQLException {
		double vlFinal = produtoCreditoDesc.vlUnitario;
		if (pedido != null) {
			if (LavenderePdaConfig.usaCondicaoComercialPedido && pedido.getCondicaoComercial() != null) {
				vlFinal = vlFinal * pedido.getCondicaoComercial().getVlIndiceFinanceiro();
			}
			if (LavenderePdaConfig.isIndiceFinanceiroClienteVlItemPedido() && pedido.getCliente() != null) {
				int nuCasasDecimaisCalcIndicFinanceiroCliente = LavenderePdaConfig.getNuCasasDecimaisCalcIndicFinanceiroCliente();
				double vlIndiceFinanceiro = ValueUtil.round(pedido.getCliente().getVlIndiceFinanceiro());
				if (nuCasasDecimaisCalcIndicFinanceiroCliente > 0) {
					vlIndiceFinanceiro = ValueUtil.round(vlIndiceFinanceiro, nuCasasDecimaisCalcIndicFinanceiroCliente);
				}
				vlFinal = vlFinal * vlIndiceFinanceiro;
			}
		}
		return vlFinal;
    }
}