package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculaVlBaseIcmsRetidoCalcRetidoTestCase  {

	private ItemPedido itemPedido = new ItemPedido();
	private Tributacao tributacao = new Tributacao();
	private double vlBaseIcmsRetidoCalcRetido;

	@BeforeEach
	public void beforeTests() {
		itemPedido.vlBaseItemPedido = 10.0;
		vlBaseIcmsRetidoCalcRetido = itemPedido.vlBaseItemPedido;
	}

	@Test
	public void testCalculaVlBaseIcmsRetidoCalcRetidoComAcrescimoDesconto() {
		beforeTests();
		tributacao.flBaseIcmsRetidoComDesconto = "3";
		
		itemPedido.vlPctDesconto = 25;
		itemPedido.vlPctAcrescimo = 0;
		assertEquals(7.5, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlPctAcrescimo = 20;
		assertEquals(12, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlBaseItemPedido = 15;
		vlBaseIcmsRetidoCalcRetido = itemPedido.vlBaseItemPedido;
		itemPedido.vlPctDesconto = 35;
		itemPedido.vlPctAcrescimo = 0;
		assertEquals(9.75, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlPctAcrescimo = 35;
		assertEquals(20.25, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlPctAcrescimo = 0;
		assertEquals(15, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
	}

	@Test
	public void testCalculaVlBaseIcmsRetidoCalcRetidoComAcrescimo() {
		beforeTests();
		tributacao.flBaseIcmsRetidoComDesconto = "2";

		itemPedido.vlPctAcrescimo = 6.0;
		assertEquals(10.6, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);

		itemPedido.vlPctAcrescimo = 20;
		assertEquals(12, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
	}

	@Test
	public void testCalculaVlBaseIcmsRetidoCalcRetidoComDesconto() {
		beforeTests();
		
		itemPedido.vlPctDesconto = 5;
		tributacao.flBaseIcmsRetidoComDesconto = "S";
		assertEquals(9.5, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		tributacao.flBaseIcmsRetidoComDesconto = "1";
		assertEquals(9.5, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlPctDesconto = 25;
		tributacao.flBaseIcmsRetidoComDesconto = "S";
		assertEquals(7.5, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		tributacao.flBaseIcmsRetidoComDesconto = "1";
		assertEquals(7.5, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
	}

	@Test
	public void testCalculaVlBaseIcmsRetidoCalcRetidoComFlagDesligadaOuInvalida() {
		beforeTests();
		tributacao.flBaseIcmsRetidoComDesconto = "N";
		
		itemPedido.vlPctAcrescimo = 6.0;
		assertEquals(10, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlPctAcrescimo = 20;
		assertEquals(10, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		tributacao.flBaseIcmsRetidoComDesconto = "0";
		
		itemPedido.vlPctAcrescimo = 6.0;
		assertEquals(10, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
		
		itemPedido.vlPctAcrescimo = 20;
		assertEquals(10, STService.getInstance().calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido), 0.00001);
	}

}
