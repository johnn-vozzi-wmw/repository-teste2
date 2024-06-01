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
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoPed;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeDbxDao;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class ItemNfeService extends CrudService {

    private static ItemNfeService instance;
    
    private ItemNfeService() {
        //--
    }
    
    public static ItemNfeService getInstance() {
        if (instance == null) {
            instance = new ItemNfeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemNfeDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public Vector getItemNfeList(final Nfe nfe) throws SQLException {
    	if (nfe != null) {
			ItemNfe itemNfeFilter = getItemNfeFilter(nfe);
			return findAllByExample(itemNfeFilter);
    	}
    	return null;
    }

	private ItemNfe getItemNfeFilter(final Nfe nfe) {
		ItemNfe itemNfeFilter = new ItemNfe();
		itemNfeFilter.cdEmpresa = nfe.cdEmpresa;
		itemNfeFilter.cdRepresentante = nfe.cdRepresentante;
		itemNfeFilter.nuPedido = nfe.nuPedido;
		itemNfeFilter.flOrigemPedido = nfe.flOrigemPedido;
		return itemNfeFilter;
	}
    
    public ItemPedidoGrade getItemPedidoGradeByItemNfe(ItemNfe itemNfe) {
    	if (itemNfe != null) {
    		ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
    		itemPedidoGrade.cdEmpresa = itemNfe.cdEmpresa;
    		itemPedidoGrade.cdRepresentante = itemNfe.cdRepresentante;
    		itemPedidoGrade.flOrigemPedido = itemNfe.flOrigemPedido;
    		itemPedidoGrade.nuPedido = itemNfe.nuPedido;
    		itemPedidoGrade.cdProduto = itemNfe.cdProduto;
    		itemPedidoGrade.flTipoItemPedido = itemNfe.flTipoItemPedido;
    		itemPedidoGrade.nuSeqProduto = itemNfe.nuSeqProduto;
    		itemPedidoGrade.cdItemGrade1 = itemNfe.cdItemGrade1;
    		itemPedidoGrade.cdItemGrade2 = itemNfe.cdItemGrade2;
    		itemPedidoGrade.cdItemGrade3 = itemNfe.cdItemGrade3;
    		return itemPedidoGrade;
    	}
    	return null;
    }
    
    public boolean isItemNfeGrade(final ItemNfe itemNfe) {
    	if (itemNfe != null) {
    		return !ValueUtil.VALOR_ZERO.equals(itemNfe.cdItemGrade1) || !ValueUtil.VALOR_ZERO.equals(itemNfe.cdItemGrade2) || !ValueUtil.VALOR_ZERO.equals(itemNfe.cdItemGrade3); 
    	}
    	return false;
    }
    
    public void geraItensNfe(Pedido pedido, Nfe nfe) throws SQLException {
    	int size = pedido.itemPedidoList.size();
    	double vlTotalProdutoItens = 0d;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.pedido = pedido;
			Produto produto = itemPedido.getProduto();
			ItemNfe itemNfe = new ItemNfe();
			TributacaoConfig config = itemPedido.getTributacaoConfigItem();
			itemNfe.cdEmpresa = nfe.cdEmpresa;
			itemNfe.cdRepresentante = nfe.cdRepresentante;
			itemNfe.nuPedido = pedido.nuPedido;
			itemNfe.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			itemNfe.cdProduto = itemPedido.cdProduto;
			itemNfe.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			itemNfe.nuSeqProduto = itemPedido.nuSeqProduto;
			itemNfe.cdItemGrade1 = itemNfe.cdItemGrade2 = itemNfe.cdItemGrade3 = "0";
			itemNfe.cdClassificFiscal = produto.cdClassificFiscal;
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
				if (config != null
						&& !ValueUtil.isEmpty(config.cdClassificFiscal)
						&& !config.cdClassificFiscal.equals("0")){
					itemNfe.cdClassificFiscal = config.cdClassificFiscal;
				}
			}
			itemNfe.cdUnidade = itemPedido.cdUnidade; 
			itemNfe.qtItemFisico = itemPedido.getQtItemFisico();
			itemNfe.vlBaseItemTabelaPreco = itemPedido.vlBaseItemTabelaPreco;
			itemNfe.vlItemPedido = itemPedido.vlItemPedido;
			itemNfe.vlTotalItemPedido = itemPedido.vlTotalItemPedido;
			itemNfe.flCigarro = produto.flCigarro;
			itemNfe.vlTotalIcmsItem = itemPedido.vlTotalIcmsItem;
			itemNfe.vlTotalStItem = itemPedido.vlTotalStItem;
			if ((LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && !itemNfe.isCigarro()) || !LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros) {
				nfe.vlTotalIcms += itemNfe.vlTotalIcmsItem;
				nfe.vlTotalSt += itemNfe.vlTotalStItem;
			}
			nfe.vlTotalProdutosNfe += itemPedido.vlTotalItemPedido;
			nfe.vlTotalSeguro += itemPedido.getVlTotalSeguro();
			itemNfe.dsNcmProduto = produto.dsNcmProduto;
			itemNfe.vlTotalBaseStItem = itemPedido.vlTotalBaseStItem;
			itemNfe.vlTotalBaseIcmsItem = itemPedido.vlTotalBaseIcmsItem == 0 && config != null && !config.isIndEscala() ? itemPedido.vlTotalItemPedido : itemPedido.vlTotalBaseIcmsItem;
			itemNfe.vlDespesaAcessoria = itemPedido.vlDespesaAcessoria;
			itemNfe.nuCfopProduto = getNuCfopProduto(produto, pedido.cdTipoPedido);
			itemNfe.qtPeso = itemPedido.qtPeso;
			itemNfe.dsCestProduto = produto.dsCestProduto;
			itemNfe.qtMultiploEmbalagem = ValueUtil.round(produto.nuMultiploEmbalagem * itemNfe.qtItemFisico);
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && config != null) {
				if (config.isCalculaIcms() && itemPedido.getQtItemFisico() > 0) {
					Tributacao tributacao = itemPedido.getTributacaoItem();
					double vlPctIcms = tributacao != null  && tributacao.vlPctIcms > 0 ? tributacao.vlPctIcms : itemPedido.getProduto().vlPctIcms;
					itemNfe.vlPctIcms = vlPctIcms;
				}
				if ((config.isCalculaPisCofins() || config.isCalculaEDebitaPisCofins()) && itemPedido.getQtItemFisico() > 0) {
					Tributacao tributacao = itemPedido.getTributacaoItem();
					double vlPctPis = tributacao != null  && tributacao.vlPctPis > 0 ? tributacao.vlPctPis : itemPedido.getProduto().vlPctPis;
					double vlPctCofins = tributacao != null && tributacao.vlPctCofins > 0 ? tributacao.vlPctCofins : itemPedido.getProduto().vlPctCofins;
					itemNfe.vlPctPis = vlPctPis;
					itemNfe.vlPctCofins = vlPctCofins;
				}
			}
			Tributacao tributacao = itemPedido.getTributacaoItem();
			itemNfe.cdOrigemMercadoria = produto.cdOrigemMercadoria;
			itemNfe.vlPctReducaoBaseIcms = tributacao != null ? tributacao.vlPctReducaoBaseIcms : 0;
			itemNfe.vlPctReducaoIcms = tributacao != null ? tributacao.vlPctReducaoIcms : 0;
			itemNfe.vlPctFecopRecolher = tributacao != null ? tributacao.vlPctFecopRecolher : 0;
			itemNfe.cdBeneficioFiscal = tributacao != null ? tributacao.cdBeneficioFiscal : null;
			vlTotalProdutoItens += itemNfe.vlTotalItemPedido;
			insert(itemNfe);
		}
		if (vlTotalProdutoItens != nfe.vlTotalProdutosNfe) {
			throw new ValidationException(Messages.NFE_ERRO_SOMA_ITENS_DIFERENTE_TOTAL_NOTA);
		}		
    }

	private String getNuCfopProduto(Produto produto, String cdTipoPedido) throws SQLException {
		ProdutoTipoPed produtoTipoPed = ProdutoTipoPedService.getInstance().getProdutoTipoPed(cdTipoPedido, produto.cdProduto);
		return produtoTipoPed != null && produtoTipoPed.nuCfopProduto != null ? produtoTipoPed.nuCfopProduto : produto.nuCfopProduto;
	}

	public void excluiuItensNfe(Nfe nfe) throws SQLException {
		deleteAllByExample(getItemNfeFilter(nfe));
	}
	
	public String findRSItemNfe(ItemNfe itemNfe) throws SQLException {
		ItemNfe itemNfeFilter = new ItemNfe();
		itemNfeFilter.cdEmpresa = itemNfe.cdEmpresa;
		itemNfeFilter.cdRepresentante = itemNfe.cdRepresentante;
		itemNfeFilter.flOrigemPedido = itemNfe.flOrigemPedido;
		itemNfeFilter.nuPedido = itemNfe.nuPedido;
		itemNfeFilter.comJoinExampleRS = false;
		return ItemNfeDbxDao.getInstance().findAllByExampleSql(itemNfeFilter);
	}

	public void atualizaItemNfeAposEnvioServidor() throws SQLException {
		ItemNfe itemNfeFilter = new ItemNfe();
		itemNfeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemNfeFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		itemNfeFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		itemNfeFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		itemNfeFilter.filtraPorNaoEnviadoServidor = true;
		ItemNfeDbxDao.getInstance().updateItemNfeTransmitidaComSucesso(itemNfeFilter);
		
	}

}