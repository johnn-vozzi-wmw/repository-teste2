package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Produto;
import org.junit.jupiter.api.Test;

public class ConversaoUnidadeServiceTest  {

	@Test
	public void testConverteQtUnidadeFisicaToQtUnidadeFaturamento() {
		Cliente cliente = new Cliente();
		Produto produto = new Produto();
		produto.nuConversaoUnidadesMedida = 5;
	    produto.nuConversaoUMCreditoAntecipado = 10;
		double valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 25);
		assertEquals(5.0, valor, 0.00001);

		//teste de número decimal
		produto.nuConversaoUnidadesMedida = 2.5;
		valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 10);
		assertEquals(4.0, valor, 0.00001);

		//teste de variável zerada
		produto.nuConversaoUnidadesMedida = 0;
		valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 10);
		assertEquals(10.0, valor, 0.00001);

		//teste de variável arredondada
		produto.nuConversaoUnidadesMedida = 2;
		valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 6.4);
		assertEquals(3.2, valor, 0.00001);

		//teste de resultado menor que 1 e maior que 0
		produto.nuConversaoUnidadesMedida = 15;
		valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 10);
		assertEquals(0.6666666666666666, valor, 0.00001);

		//usaFatorCUMEspecialClienteCreditoAntecipado e isCreditoAntecipado
		LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado = true;
		cliente.flCreditoAntecipado = ValueUtil.VALOR_SIM;
		valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 25);
		assertEquals(2.5, valor, 0.00001);

		//usaFatorCUMEspecialClienteCreditoAntecipado e !isCreditoAntecipado
		LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado = true;
		cliente.flCreditoAntecipado = ValueUtil.VALOR_NAO;
		produto.nuConversaoUnidadesMedida = 5;
		valor = ConversaoUnidadeService.getInstance().converteQtUnidadeFisicaToQtUnidadeFaturamento(cliente, produto, 25);
		assertEquals(5.0, valor, 0.00001);
	}
	@Test
	public void testIsConversaoUnidadeCompleta() {
		double nuconversao = 0;
		double qtItem = 4;
		assertFalse(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));

		nuconversao = 1;
		qtItem = 4;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));

		nuconversao = 3;
		qtItem = 4;
		assertFalse(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));

		nuconversao = 3;
		qtItem = 66;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 1;
		qtItem = 444444444;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 15;
		qtItem = 150;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 15;
		qtItem = 100;
		assertFalse(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = -15;
		qtItem = 100;
		assertFalse(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 1.5;
		qtItem = 3;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 1.5;
		qtItem = 0;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 3;
		qtItem = 1234644;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		nuconversao = 1.4;
		qtItem = 2.8;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
		
		ValueUtil.doublePrecision = 4;
		nuconversao = 4.3354;
		qtItem = 86708;
		assertTrue(ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuconversao, qtItem));
	
	}
}
