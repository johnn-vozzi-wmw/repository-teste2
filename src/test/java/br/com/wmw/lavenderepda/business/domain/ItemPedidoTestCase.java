package br.com.wmw.lavenderepda.business.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoTestCase  {

	@Test
	public void testGetVlEfetivoTotal() {
		ItemPedido itemPedido = new ItemPedido();
		double qtEstoque = 0;
		itemPedido.vlItemPedido = 20;
		itemPedido.setQtItemFisico(10);
		//qtEstoque maior que a quantidade de itens vendida
		qtEstoque = 20;
		assertEquals(200, itemPedido.getVlEfetivoTotal(qtEstoque), 2);
		//qtEstoque igual a quantidade de itens vendida
		qtEstoque = 10;
		assertEquals(200, itemPedido.getVlEfetivoTotal(qtEstoque), 2);
		//qtEstoque menor que a quantidade de itens vendida
		qtEstoque = 5;
		assertEquals(100, itemPedido.getVlEfetivoTotal(qtEstoque), 2);
		//qtEstoque 0
		qtEstoque = 0;
		assertEquals(0, itemPedido.getVlEfetivoTotal(qtEstoque), 2);
		//vlItemPedido 0 e qtEstoque 10
		qtEstoque = 10;
		itemPedido.vlItemPedido = 0;
		assertEquals(0, itemPedido.getVlEfetivoTotal(qtEstoque), 2);
		itemPedido.vlItemPedido = 20;
		//qtItemFisico 0 e qtEstoque 10
		qtEstoque = 10;
		itemPedido.setQtItemFisico(0);
		assertEquals(0, itemPedido.getVlEfetivoTotal(qtEstoque), 2);
		itemPedido.setQtItemFisico(10);
	}

}
