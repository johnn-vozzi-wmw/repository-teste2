package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import org.junit.jupiter.api.Test;

public class CondicaoNegociacaoServiceTestCase  {

	@Test
	public void testeGetQtConsumidaDeEstoque() {
		LavenderePdaConfig.usaEstoqueInternoParcialmenteLocalEstoqueCondNeg = true;
		CondicaoNegociacaoService condicaoNegociacaoService = CondicaoNegociacaoService.getInstance();
		CondicaoNegociacao condicaoNegociacao = new CondicaoNegociacao();
		condicaoNegociacao.vlPctEstoque = 0;
		//--
		assertEquals(0, condicaoNegociacaoService.getQtConsumidaDeEstoque(0, condicaoNegociacao));
		//--
		assertEquals(0, condicaoNegociacaoService.getQtConsumidaDeEstoque(40, condicaoNegociacao));
		//--
		assertEquals(0, condicaoNegociacaoService.getQtConsumidaDeEstoque(40, condicaoNegociacao));
		//--
		condicaoNegociacao.vlPctEstoque = 20;
		assertEquals(8, condicaoNegociacaoService.getQtConsumidaDeEstoque(40, condicaoNegociacao));
		//--
		try {
			condicaoNegociacaoService.getQtConsumidaDeEstoque(2, condicaoNegociacao);
		} catch (Exception ex) {
			assertEquals(condicaoNegociacaoService.getQtSugeridaParaNegociacao2(1, 20), "5");
		}
		try {
			condicaoNegociacaoService.getQtConsumidaDeEstoque(8, condicaoNegociacao);
		} catch (Exception ex) {
			assertEquals(condicaoNegociacaoService.getQtSugeridaParaNegociacao2(2, 20), "5");
		}
	}

}
