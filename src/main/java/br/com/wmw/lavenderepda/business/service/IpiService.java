package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;

public class IpiService {

	private static IpiService instance;

	private IpiService() {

	}

	public static IpiService getInstance() {
	    if (instance == null) {
	        instance = new IpiService();
	    }
	    return instance;
	}
	
	public double getVlTotalIpiPedido(Pedido pedido) {
		double vlTotalIpi = 0;
		if (pedido != null) {
			int size = pedido.itemPedidoList.size();
			ItemPedido item;
			for (int i = 0; i < size; i++) {
				item = (ItemPedido)pedido.itemPedidoList.items[i];
				vlTotalIpi += item.getVlTotalIpi();
			}
		}
		return vlTotalIpi;
	}

	public double getVlTotalPedidoComIpi(final Pedido pedido) {
		double vlTotalItensComIpi = 0;
		if (pedido != null) {
			return pedido.vlTotalPedido + getVlTotalIpiPedido(pedido);
		}
		return vlTotalItensComIpi;
	}

	public void calculaVlTotalPedidoComIpi(Pedido pedido) {
		pedido.vlTtPedidoComIpi = getVlTotalPedidoComIpi(pedido);
	}
	
	protected void calculaIpiItemPedidoPersonalizado(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao) throws SQLException {
		if (tributacaoConfig != null && tributacaoConfig.isCalculaIpi() && itemPedido.getQtItemFisico() != 0) {
			double vlPctIpi = tributacao != null && tributacao.vlPctIpi != 0 ? tributacao.vlPctIpi : itemPedido.getProduto().vlPctIpi;
			double vlMinIpi = tributacao != null && tributacao.vlMinIpi != 0 ? tributacao.vlMinIpi : itemPedido.getProduto().vlMinIpi;
			vlMinIpi = vlMinIpi * itemPedido.getQtItemFisico();
			double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
			boolean aplicaIndiceCondPagtoVlBaseIPI = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseIPI();
			double vlTotalItemPedidoComSeguro = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseIPI, vlIndiceCondPagto);
			double vlBaseIpi = tributacaoConfig.isAplicaValorFreteNaBaseIpi() ? (vlTotalItemPedidoComSeguro + itemPedido.getVlTotalItemPedidoFrete()) : vlTotalItemPedidoComSeguro;
			if (tributacaoConfig.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
				vlBaseIpi += itemPedido.vlTotalPisItem + itemPedido.vlTotalCofinsItem;
			}
			itemPedido.vlTotalIpiItem = ValueUtil.round(vlBaseIpi * vlPctIpi / 100, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			itemPedido.vlTotalIpiItem =  itemPedido.vlTotalIpiItem < vlMinIpi ? vlMinIpi : itemPedido.vlTotalIpiItem;
			if (LavenderePdaConfig.permiteAlterarValorItemComIPI && itemPedido.vlBaseItemIpi == 0) {
				itemPedido.vlItemIpi = ValueUtil.round(itemPedido.vlItemPedido + itemPedido.vlTotalIpiItem / itemPedido.getQtItemFisico(), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
				itemPedido.vlBaseItemIpi = ValueUtil.round(itemPedido.vlBaseItemPedido + itemPedido.vlTotalIpiItem / itemPedido.getQtItemFisico(), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			}
		} else {
			itemPedido.vlTotalIpiItem = 0;
			itemPedido.vlItemIpi = 0;
			itemPedido.vlBaseItemIpi = 0;
		}
	}
	
	protected void calculaIpiItemPedidoNormal(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProduto().vlIpi != 0) {
			itemPedido.vlIpiItem = itemPedido.getProduto().vlIpi;
		} else if (itemPedido.getProduto().vlPctIpi != 0) {
			double vlFrete = ("2".equals(LavenderePdaConfig.calculaIpiItemPedido) && !LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) ? itemPedido.vlItemPedidoFrete : 0;
			itemPedido.vlIpiItem = ValueUtil.round((itemPedido.vlItemPedido + vlFrete) * itemPedido.getProduto().vlPctIpi / 100, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		} else {
			itemPedido.vlIpiItem = 0;
		}
		itemPedido.vlIpiItem = itemPedido.vlIpiItem < itemPedido.getProduto().vlMinIpi ? itemPedido.getProduto().vlMinIpi : itemPedido.vlIpiItem;
	}

}
