package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFreteTabPreco;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import org.junit.jupiter.api.Test;

public class TipoFreteTabPrecoServiceTestCase  {

	@Test
	public void testCalculateFreteItemPedidoByTipoFreteTabPreco() throws SQLException{
		TipoFreteTabPreco tipoFreteTabPreco = new TipoFreteTabPreco();
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		TipoPedido tipoPedido = new TipoPedido();
		tipoPedido.flIgnoraCalculoFrete = "N";
		pedido.setTipoPedido(tipoPedido);
		
		tipoFreteTabPreco.vlPctFrete = 10;
		itemPedido.vlItemPedido = 100;
		itemPedido.setQtItemFisico(1);
		itemPedido.pedido = pedido;
		TipoFreteTabPrecoService.getInstance().calculateFreteItemPedidoByTipoFreteTabPrecoEPeso(tipoFreteTabPreco, itemPedido);
		assertEquals(10.00, itemPedido.vlItemPedidoFrete, 2);
	
		//teste com tipoFreteTabPreco nulo
		tipoFreteTabPreco = null;
		itemPedido.vlItemPedido = 100;
		TipoFreteTabPrecoService.getInstance().calculateFreteItemPedidoByTipoFreteTabPrecoEPeso(tipoFreteTabPreco, itemPedido);
		assertEquals(0.0, itemPedido.vlItemPedidoFrete,2);
	}
}
