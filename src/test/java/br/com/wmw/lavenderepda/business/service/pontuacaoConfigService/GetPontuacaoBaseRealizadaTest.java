package br.com.wmw.lavenderepda.business.service.pontuacaoConfigService;

import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GetPontuacaoBaseRealizadaTest {

	public PontuacaoConfigService service;

	public GetPontuacaoBaseRealizadaTest() {
		service = PontuacaoConfigService.getInstance();
		LavenderePdaConfig.nuCasasDecimaisPontuacao = 2;
	}

	@Test
	public void deveRetornarNuloQuandoNaoApresentaBaseERealizada() {
		String vlBaseRealizada = service.getPontuacaoBaseRealizada(0d, 0d, false, false);
		assertNull(vlBaseRealizada);
	}

	@Test
	public void deveRetornarSomenteBaseQuandoNaoApresentaRealizada() {
		String vlBaseRealizada = service.getPontuacaoBaseRealizada(94d, 53.555589d, true, false);
		assertEquals(vlBaseRealizada, "53,56");
	}

	@Test
	public void deveRetornarSomenteRealizadaQuandoNaoApresentaBase() {
		String vlBaseRealizada = service.getPontuacaoBaseRealizada(94d, 53.555589d, false, true);
		assertEquals(vlBaseRealizada, "94,00");
	}

	@Test
	public void deveRetornarBaseERealizada() {
		String vlBaseRealizada = service.getPontuacaoBaseRealizada(53.555589d, 53.555589d, true, true);
		assertEquals(vlBaseRealizada, "53,56" + Messages.PONTUACAO_PEDIDO_SEPARADOR + "53,56");
	}

}
