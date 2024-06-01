package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import org.junit.jupiter.api.Test;

public class TipoFreteServiceTestCase  {

	@Test
	public void testCalculateFreteItemPedido(){
		TipoFrete tipoFrete = new TipoFrete();
		ItemPedido itemPedido = new ItemPedido();
		tipoFrete.vlPctFrete = 10;
		itemPedido.vlItemPedido = 80;
		TipoFreteService.getInstance().calculateFreteItemPedido(tipoFrete, itemPedido);
		assertTrue(8.0 == itemPedido.vlItemPedidoFrete);
	}

}
