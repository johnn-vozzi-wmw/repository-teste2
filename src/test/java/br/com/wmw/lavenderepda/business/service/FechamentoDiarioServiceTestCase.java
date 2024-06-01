package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totalcross.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class FechamentoDiarioServiceTestCase {

	public FechamentoDiarioService service;
	@BeforeEach
	public void before() throws InterruptedException {
		service = FechamentoDiarioService.getInstance();
	}

	@AfterEach
	public void After(){
		service = null;
		LavenderePdaConfig.usaConfigFechamentoDiarioVendas = null;
	}

	private void configurePrm(String usa, String bloqueiaFechamentoPedidoNaoTransmitido, String liberaFechamentoDiarioComSenha, String permiteInformarVeiculoFechamento, String obrigaInformarVeiculoFechamento, String usaRelatorioEstoqueProduto, String usaTipoLancamentoDinamico, String consideraValorPedidoAtualRetornado, String nuDiasConsideraValorPedidoAtualRetornadoPagamentos) {
		JSONObject jsonPrm = new JSONObject();
		jsonPrm.put("usa", usa);
		jsonPrm.put("bloqueiaFechamentoPedidoNaoTransmitido", bloqueiaFechamentoPedidoNaoTransmitido);
		jsonPrm.put("liberaFechamentoDiarioComSenha", liberaFechamentoDiarioComSenha);
		jsonPrm.put("permiteInformarVeiculoFechamento", permiteInformarVeiculoFechamento);
		jsonPrm.put("obrigaInformarVeiculoFechamento", obrigaInformarVeiculoFechamento);
		jsonPrm.put("usaRelatorioEstoqueProduto", usaRelatorioEstoqueProduto);
		jsonPrm.put("usaTipoLancamentoDinamico", usaTipoLancamentoDinamico);
		jsonPrm.put("consideraValorPedidoAtualRetornado", consideraValorPedidoAtualRetornado);
		jsonPrm.put("nuDiasConsideraValorPedidoAtualRetornadoPagamentos", nuDiasConsideraValorPedidoAtualRetornadoPagamentos);
		LavenderePdaConfig.usaConfigFechamentoDiarioVendas = jsonPrm.toString();
	}

	@Test
	public void deveRetornarNuloSeParametroDesligado() {
		configurePrm(ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, "0");
		assertNull(service.getDataConsideraValorPedidoAtualRetornadoPagamentos(null));
	}

	@Test
	public void deveRetornarNuloSeParametroSeNuDiasConsideraValorPedidoAtualRetornadoPagamentosNao() {
		configurePrm(ValueUtil.VALOR_SIM, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO);
		assertNull(service.getDataConsideraValorPedidoAtualRetornadoPagamentos(DateUtil.getDateValue(10, 4, 2021)));
	}

	@Test
	public void deveRetornarNuloSeParametroSeNuDiasConsideraValorPedidoAtualRetornadoPagamentosSim() {
		configurePrm(ValueUtil.VALOR_SIM, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_SIM);
		assertNull(service.getDataConsideraValorPedidoAtualRetornadoPagamentos(DateUtil.getDateValue(10, 4, 2021)));
	}

	@Test
	public void deveRetornarNuloSeNuDiasConsideraValorPedidoAtualRetornadoPagamentos0() {
		configurePrm(ValueUtil.VALOR_SIM, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_SIM, "0");
		assertNull(service.getDataConsideraValorPedidoAtualRetornadoPagamentos(DateUtil.getDateValue(10, 4, 2021)));
	}

	@Test
	public void deveRetornarDia9SeNuDiasConsideraValorPedidoAtualRetornadoPagamentos1() {
		configurePrm(ValueUtil.VALOR_SIM, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_SIM, "1");
		assertEquals(service.getDataConsideraValorPedidoAtualRetornadoPagamentos(DateUtil.getDateValue(10, 4, 2021)), DateUtil.getDateValue(9, 4, 2021));
	}

	@Test
	public void deveRetornarDia5SeNuDiasConsideraValorPedidoAtualRetornadoPagamentos5() {
		configurePrm(ValueUtil.VALOR_SIM, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_NAO, ValueUtil.VALOR_SIM, "5");
		assertEquals(service.getDataConsideraValorPedidoAtualRetornadoPagamentos(DateUtil.getDateValue(10, 4, 2021)), DateUtil.getDateValue(5, 4, 2021));
	}

}
