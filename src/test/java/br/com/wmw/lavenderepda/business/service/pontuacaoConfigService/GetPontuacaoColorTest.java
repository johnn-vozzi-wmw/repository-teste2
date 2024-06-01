package br.com.wmw.lavenderepda.business.service.pontuacaoConfigService;

import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetPontuacaoColorTest {

	public PontuacaoConfigService service;

	public GetPontuacaoColorTest() {
		service = PontuacaoConfigService.getInstance();
		LavenderePdaConfig.nuCasasDecimaisPontuacao = 2;
	}

	@Test
	public void deveRetornarCorPadraoQuandoBaseRealizadoZero() throws SQLException {
		int color = service.getPontuacaoColor(0d, 0d, false, false, false);
		assertEquals(ColorUtil.componentsForeColor, color);
	}

	@Test
	public void deveRetornarCorPadraoQuandoNaoApresentaRealizado() throws SQLException {
		int color = service.getPontuacaoColor(0d, 2d, true, false, false);
		assertEquals(ColorUtil.componentsForeColor, color);
	}

	@Test
	public void deveRetornarCorNegativaQuandoBaseMaiorRealizado() throws SQLException {
		int color = service.getPontuacaoColor(0d, 2d, false, false, false);
		assertEquals(LavendereColorUtil.COR_EXTRATO_VALOR_PONTUACAO_NEGATIVO, color);
	}

	@Test
	public void deveRetornarCorPositivaQuandoRealizadoIgualBase() throws SQLException {
		int color = service.getPontuacaoColor(2d, 2d, false, false, false);
		assertEquals(LavendereColorUtil.COR_CAPA_VALOR_PONTUACAO_POSITIVO, color);
	}

	@Test
	public void deveRetornarCorPositivaQuandoRealizadoMaiorQueBase() throws SQLException {
		int color = service.getPontuacaoColor(4d, 2d, false, false, true);
		assertEquals(LavendereColorUtil.COR_EXTRATO_VALOR_PONTUACAO_POSITIVO, color);
	}

}
