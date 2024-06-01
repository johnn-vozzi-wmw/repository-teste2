package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import org.junit.jupiter.api.Test;

public class FecopTestCase  {
	
	//Validação do Cálculo de Fecop (Fundo de pobreza - PRM303)
	@Test
	public void testCalculaValorFecopARecolher() {
		//usaArredondamentoIndividualCalculoComposto desligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = false;
		double vlBaseCalculoRetido = 1.96738;
		Tributacao tributacao = new Tributacao();
		tributacao.flAplicaFecopST = ValueUtil.VALOR_SIM;
		tributacao.flBaseIcmsRetidoComRepasse = ValueUtil.VALOR_NAO;
		tributacao.vlPctRepasseIcms = 0;
		tributacao.vlPctFecop = 19.0;
		tributacao.vlPctFecopRecolher = 19.0;
		double valorIcms = 0.63345;
		double vlFrete = 1.50102;
		double margemAgregadaPctIcms = 50.8;
		double aliquotaRetido = 19.0;
		double vlIpi = 0.58124;
		double vlFecopARecolher = FecopService.getInstance().calculaValorFecopARecolher(vlBaseCalculoRetido, tributacao, valorIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpi, true);
		assertEquals(0.244187284, vlFecopARecolher, 6);
		//usaArredondamentoIndividualCalculoComposto ligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;
		vlFecopARecolher = FecopService.getInstance().calculaValorFecopARecolher(vlBaseCalculoRetido, tributacao, valorIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpi, true);
		assertEquals(0.24, vlFecopARecolher, 2);
	}

	@Test
	public void testCalculaValorFecopARecolherTipo1() {
		//usaArredondamentoIndividualCalculoComposto desligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = false;

		Tributacao tributacao = new Tributacao();
		tributacao.flBaseIcmsRetidoComRepasse = ValueUtil.VALOR_NAO;
		tributacao.vlPctRepasseIcms = 0;
		tributacao.vlPctFecop = 0.0;
		tributacao.vlPctFecopRecolher = 2.0;
		tributacao.vlPctIcms = 0.0;

		double vlBaseCalculoRetido = 58.78;
		double valorIcms = 7.053;
		double vlFrete = 0;
		double margemAgregadaPctIcms = 92.02;
		double aliquotaRetido = 18.0;
		double vlIpi = 3.820;

		double vlFecopARecolher = FecopService.getInstance().calculaValorFecop1(vlBaseCalculoRetido, tributacao, valorIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpi);

		assertEquals(0.279, vlFecopARecolher, 3);

		//usaArredondamentoIndividualCalculoComposto ligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;

		vlFecopARecolher = FecopService.getInstance().calculaValorFecop1(vlBaseCalculoRetido, tributacao, valorIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpi);

		assertEquals(0.28, vlFecopARecolher, 2);
	}

	@Test
	public void testCalculaValorFecopARecolherTipo2() {
		//usaArredondamentoIndividualCalculoComposto desligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = false;

		Tributacao tributacao = new Tributacao();
		tributacao.flBaseIcmsRetidoComRepasse = ValueUtil.VALOR_NAO;
		tributacao.vlPctRepasseIcms = 0;
		tributacao.vlPctFecop = 0.0;
		tributacao.vlPctFecopRecolher = 2.0;
		tributacao.vlPctIcms = 0.0;

		double vlBaseCalculoRetido = 58.78;
		double vlFrete = 0;
		double margemAgregadaPctIcms = 92.02;
		double vlIpi = 3.820;
		double vlFecopARecolher = FecopService.getInstance().calculaValorFecop2(vlBaseCalculoRetido, tributacao, vlFrete, margemAgregadaPctIcms, vlIpi);

		assertEquals(2.333, vlFecopARecolher, 3);

		//usaArredondamentoIndividualCalculoComposto ligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;

		vlFecopARecolher = FecopService.getInstance().calculaValorFecop2(vlBaseCalculoRetido, tributacao, vlFrete, margemAgregadaPctIcms, vlIpi);

		assertEquals(2.33, vlFecopARecolher, 2);
	}

	@Test
	public void testCalculaValorFecopARecolherTipo3() {
		//usaArredondamentoIndividualCalculoComposto desligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = false;

		Tributacao tributacao = new Tributacao();
		tributacao.vlPctFecopRecolher = 2.0;

		double vlBaseIcmsRetidoCalcRetido = 58.78;
		double vlFrete = 0;
		double vlIpi = 3.820;

		double vlFecopARecolher = FecopService.getInstance().calculaValorFecop3(vlBaseIcmsRetidoCalcRetido, tributacao, vlFrete, vlIpi);

		assertEquals(1.252, vlFecopARecolher, 3);

		//usaArredondamentoIndividualCalculoComposto ligado
		LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao = true;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;

		vlFecopARecolher = FecopService.getInstance().calculaValorFecop3(vlBaseIcmsRetidoCalcRetido, tributacao, vlFrete, vlIpi);

		assertEquals(1.25, vlFecopARecolher, 2);
	}

}
