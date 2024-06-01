package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import totalcross.util.Vector;

public class TributosService {

	private static TributosService instance;

	private TributosService() {

	}

	public static TributosService getInstance() {
		if (instance == null) {
			instance = new TributosService();
		}
		return instance;
	}
	
	public void calculaVlTotalPedidoComTributos(Pedido pedido) {
		pedido.vlTtPedidoComTributos = getVlTotalPedidoComTributos(pedido);
	}

	public double getVlTotalPedidoComTributos(final Pedido pedido) {
		if (pedido == null) return 0;
		
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.mostraValorBruto) {
			return getVlTotalItemPedidoPersonalizado(pedido);
		}
		
		return pedido.vlTotalPedido + getVlTotalTributosPedido(pedido);
	}
	
	private double getVlTotalTributosPedido(Pedido pedido) {
		if (pedido == null) return 0;
		
		double vlTotalTributos = 0;
		int size = pedido.itemPedidoList.size();
		ItemPedido item;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido) pedido.itemPedidoList.items[i];
			vlTotalTributos += LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? item.getVlTributos() : item.getVlTotalTributos();
		}
		return vlTotalTributos;
	}
	
	private double getVlTotalItemPedidoPersonalizado(final Pedido pedido) {
		if (pedido == null) return 0;
		
		double vlTotalTributos = 0;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido)pedido.itemPedidoList.items[i];
			vlTotalTributos += item.getVlTotalItemPedidoTributosFreteSeguro();
		}
		return vlTotalTributos;
	}
	
	public void calculaVlTotalPedidoComTributosEDeducoes(Pedido pedido) throws SQLException {
		double vlTotalTributoEDeducoesComFrete = getVlTotalTributoEDeducoesComFrete(pedido);
		if (ValueUtil.valueEquals(vlTotalTributoEDeducoesComFrete, 0d)) return;
		
		pedido.vlFinalPedidoDescTribFrete = vlTotalTributoEDeducoesComFrete;
	}
	
	public double getVlTotalTributoEDeducoesComFrete(Pedido pedido) throws SQLException {
		double vlTributosComVlAdicionais = 0;
 		double vlDeducoes = 0;
 		Vector list = ValueUtil.isEmpty(pedido.itemPedidoList) ? pedido.itemPedidoListCalculoTributacao : pedido.itemPedidoList;
		if (list == null) return 0d;

		for (int i = 0; i < list.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) list.items[i];
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				vlDeducoes += itemPedido.getVlDeducoes();
				vlTributosComVlAdicionais += itemPedido.getVlTributos();
				continue;
			}
			double vlTributosDespesasAcessoria = itemPedido.getVlTributos() + itemPedido.vlDespesaAcessoria;
			vlTributosComVlAdicionais += vlTributosDespesasAcessoria * itemPedido.getQtItemFisico();
			vlDeducoes += itemPedido.getVlDeducoes() * itemPedido.getQtItemFisico();

		}
 		double vlFrete = !LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? pedido.vlFrete : 0;
		return pedido.vlTotalPedido + vlFrete + vlTributosComVlAdicionais - vlDeducoes;
	}

}
