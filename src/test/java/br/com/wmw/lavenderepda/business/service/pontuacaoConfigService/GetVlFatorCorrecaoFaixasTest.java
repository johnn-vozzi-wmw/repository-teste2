package br.com.wmw.lavenderepda.business.service.pontuacaoConfigService;

import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaDias;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaPreco;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetVlFatorCorrecaoFaixasTest {

	public PontuacaoConfigService service;

	public GetVlFatorCorrecaoFaixasTest() {
		service = PontuacaoConfigService.getInstance();
		LavenderePdaConfig.nuCasasDecimaisPontuacao = 2;
	}

	@Test
	public void deveRetornarUmQuandoPontuacaoConfigNula() {
		assertEquals(1d, service.getVlFatorCorrecaoFaixas(null));
	}

	@Test
	public void deveRetornarUmQuandoPontuacaoConfigVazia() {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		assertEquals(1d, service.getVlFatorCorrecaoFaixas(pontuacaoConfig));
	}

	@Test
	public void deveRetornarZeroQuandoPontuacaoFaixaDiaCondicaoPagtoVazia() {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto = new PontuacaoFaixaDias();
		assertEquals(0d, service.getVlFatorCorrecaoFaixas(pontuacaoConfig));
	}

	@Test
	public void deveRetornarZeroQuandoPontuacaoFaixaPrecoVazia() {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
		assertEquals(0d, service.getVlFatorCorrecaoFaixas(pontuacaoConfig));
	}

	@Test
	public void deveRetornarZeroQuandoFatorPontuacaoFaixaPrecoZero() {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto = new PontuacaoFaixaDias();
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.vlFatorCorrecao = 2;
		assertEquals(0d, service.getVlFatorCorrecaoFaixas(pontuacaoConfig));
	}

	@Test
	public void deveRetornarDoisQuandoFatorFaixaPrecoUmEFatorFaixaDiaDois() {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto = new PontuacaoFaixaDias();
		pontuacaoConfig.pontuacaoFaixaPreco.vlFatorCorrecao = 1;
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.vlFatorCorrecao = 2;
		assertEquals(2d, service.getVlFatorCorrecaoFaixas(pontuacaoConfig));
	}

	@Test
	public void deveRetornarQuatroQuandoFatorFaixaPrecoDoisEFatorFaixaDiaDois() {
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto = new PontuacaoFaixaDias();
		pontuacaoConfig.pontuacaoFaixaPreco.vlFatorCorrecao = 2;
		pontuacaoConfig.pontuacaoFaixaDiaCondicaoPagto.vlFatorCorrecao = 2;
		assertEquals(4d, service.getVlFatorCorrecaoFaixas(pontuacaoConfig));
	}

}
