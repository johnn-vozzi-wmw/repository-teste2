package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.lavenderepda.business.domain.StatusOrcamento;
import org.junit.jupiter.api.Test;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabPrecoClasseProd;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.validation.ValidationValorMaxPedidoException;
import br.com.wmw.test.method.MethodTestUtils;
import totalcross.sys.Settings;
import totalcross.util.Vector;


public class PedidoServiceTestCase  {

	@Test
	public void testFalhaValorMaximoPedido()  {
		assertThrows(ValidationValorMaxPedidoException.class, ()->{
			Pedido pedido = new Pedido();
			CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
			pedido.vlTotalPedido = 2000d;
			pedido.vlTtPedidoComSt = 2000d;
			condicaoPagamento.qtMaxValor = 1000d;
			condicaoPagamento.cdEmpresa = "1";
			pedido.setCondicaoPagamento(condicaoPagamento);
			try {
				PedidoService.getInstance().validateValorMaximoPedido(pedido);
			} catch (SQLException e) {
				fail(e.getMessage());
			}
		});
	}

	@Test
	public void testSucessoValorMaximoPedido() {
		Pedido pedido = new Pedido();
		CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
		pedido.vlTotalPedido = 2000d;
		pedido.vlTtPedidoComSt = 2000d;
		condicaoPagamento.qtMaxValor = 3000d;
		pedido.setCondicaoPagamento(condicaoPagamento);
		try {
			PedidoService.getInstance().validateValorMaximoPedido(pedido);
		} catch (SQLException e) {
			fail(e.getMessage());
		}
	}

	private void  prepareSimilaresForTest(Pedido pedido, Produto produtoSimilar, Produto produtoNormal) {
		ItemPedido itemSimilar = new ItemPedido();
		itemSimilar.cdProduto = produtoSimilar.cdProduto = "1";
		ItemPedido itemNormal = new ItemPedido();
		itemNormal.cdProduto = produtoNormal.cdProduto = "2";
		pedido.getItemPedidoAgrSimilares().removeAllElements();
		pedido.getItemPedidoAgrSimilares().addElement(itemSimilar);
		pedido.itemPedidoList = new Vector(new Object[] {itemNormal});
	}

	@Test
	public void testDeveEncontrarProdutoSimilarComSolicitacaoAutorizacao() {
		Pedido pedido = new Pedido();
		Produto produtoSimilar = new Produto();
		Produto produtoNormal = new Produto();
		prepareSimilaresForTest(pedido, produtoSimilar, produtoNormal);
		LavenderePdaConfig.configSolicitaAutorizacao = "{\"usa\":\"S\"}";
		LavenderePdaConfig.usaAgrupadorSimilaridadeProduto = true;
		assertTrue(PedidoService.getInstance().isProdutoPresenteItemPedido(produtoSimilar, pedido));
	}

	@Test
	public void testNaoDeveEncontrarProdutoSimilarSemSolicitacaoAutorizacao() {
		Pedido pedido = new Pedido();
		Produto produtoSimilar = new Produto();
		Produto produtoNormal = new Produto();
		prepareSimilaresForTest(pedido, produtoSimilar, produtoNormal);
		LavenderePdaConfig.configSolicitaAutorizacao = "{\"usa\":\"N\"}";
		LavenderePdaConfig.usaAgrupadorSimilaridadeProduto = false;
		assertFalse(PedidoService.getInstance().isProdutoPresenteItemPedido(produtoSimilar, pedido));
	}

	@Test
	public void testDeveEncontrarProdutoNormalComSolicitacaoAutorizacao() {
		System.out.println(Settings.platform);
		Pedido pedido = new Pedido();
		Produto produtoSimilar = new Produto();
		Produto produtoNormal = new Produto();
		prepareSimilaresForTest(pedido, produtoSimilar, produtoNormal);
		LavenderePdaConfig.configSolicitaAutorizacao = "{\"usa\":\"S\"}";
		LavenderePdaConfig.usaAgrupadorSimilaridadeProduto = true;
		assertTrue(PedidoService.getInstance().isProdutoPresenteItemPedido(produtoNormal, pedido));
	}

	@Test
	public void testDeveEncontrarProdutoNormalSemSolicitacaoAutorizacao() {
		Pedido pedido = new Pedido();
		Produto produtoSimilar = new Produto();
		Produto produtoNormal = new Produto();
		prepareSimilaresForTest(pedido, produtoSimilar, produtoNormal);
		LavenderePdaConfig.configSolicitaAutorizacao = "{\"usa\":\"N\"}";
		LavenderePdaConfig.usaAgrupadorSimilaridadeProduto = false;
		assertTrue(PedidoService.getInstance().isProdutoPresenteItemPedido(produtoNormal, pedido));
	}

	@Test
	public void testFalhaValidaQtMinimaProdutoPorCondPagamentoEQtMixProdutoNoFechamento() {
		Exception e = assertThrows(ValidationException.class, ()->{
			LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamento = "2";
			LavenderePdaConfig.formaValidacao = "2";
			Pedido pedido = getPedidoBase();
			MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "validaQtMinimaProdutoPorCondPagamentoEQtMixProduto" , pedido);
		}, "Uma ValidationException era esperada." );
		assertEquals( "A quantidade mínima de 2 produtos distintos, para a Condição de Pagamento selecionada, não foi atingida." , e.getMessage() , "Mensagem de erro não correspondente.");
	}

	@Test
	public void testSucessoValidaQtMinimaProdutoPorCondPagamentoEQtMixProdutoNoFechamento() {
		LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamento = "2";
		LavenderePdaConfig.formaValidacao = "2";
		Pedido pedido = getPedidoBase();
		addItens(pedido);
		addItens(pedido);
		MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "validaQtMinimaProdutoPorCondPagamentoEQtMixProduto", pedido);
	}

	@Test
	public void testFalhaValidaQtMinimaProdutoPorCondPagamentoQtMinProdutoFechamento() {
		Exception e = assertThrows(ValidationException.class, ()->{
			LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamento = "1";
			LavenderePdaConfig.formaValidacao = "2";
			Pedido pedido = getPedidoBase();
			addItens(pedido);
			MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "validaQtMinimaProdutoPorCondPagamentoQtMinProduto" , pedido);
		}, "Uma ValidationException era esperada." );
		assertEquals( "A quantidade mínima de 4 produtos, para a Condição de Pagamento selecionada, não foi atingida." , e.getMessage() , "Mensagem de erro não correspondente.");
	}

	@Test
	public void testSucessoValidaQtMinimaProdutoPorCondPagamentoQtMinProdutoFechamento() {
		LavenderePdaConfig.usaQtdeMinimaProdutoPorCondPagamento = "1";
		LavenderePdaConfig.formaValidacao = "2";
		Pedido pedido = getPedidoBase();
		addItens(pedido , 2);
		addItens(pedido , 2);
		MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "validaQtMinimaProdutoPorCondPagamentoQtMinProduto", pedido);

	}

	private void addItens(Pedido pedido ) {
		addItens(pedido , 1);
	}

	private void addItens(Pedido pedido , int qt) {
		ItemPedido itemPedido =  new ItemPedido();
		itemPedido.qtItemFisico = qt;
		itemPedido.setPedido(pedido);
		pedido.itemPedidoList.addElement(itemPedido);
	}

	private Pedido getPedidoBase() {
		Pedido pedido = new Pedido();
		CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
		condicaoPagamento.qtMinMixProduto = 2;
		condicaoPagamento.setQtMinProduto(4);
		condicaoPagamento.cdEmpresa = "1";
		pedido.setCondicaoPagamento(condicaoPagamento);
		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.cdTabelaPreco = "1";
		pedido.cdTabelaPreco = "1";
		pedido.setTabelaPreco(tabelaPreco);
		return pedido;
	}

	@Test
	public void testFalhaValidaQtMinimaProdutoPedidoPorClasse() {
		Exception e = assertThrows(ValidationException.class, () -> {
			LavenderePdaConfig.permiteTabPrecoItemDiferentePedido = "{\"usa\":\"S\"}";
			String cdClasse = "3";
			double qtItensClasse = 3;
			TabPrecoClasseProd tabPrecoClasseProd = new TabPrecoClasseProd("1", "1", "2", cdClasse);
			tabPrecoClasseProd.qtMinPedido = 10;
			Map<String, Double> qtItensClasseHash = new HashMap<>();
			qtItensClasseHash.put(cdClasse, qtItensClasse);
			MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "validateQtMinProdutoClasse", qtItensClasseHash, cdClasse, tabPrecoClasseProd.qtMinPedido);
		}, "Uma ValidationException era esperada.");
		assertEquals("A quantidade de produtos da classe 3 está abaixo do mínimo de 10,00 itens. Atual: 3,00 itens.", e.getMessage(), "Mensagem de erro não correspondente.");
	}

	@Test
	public void testSucessoValidaQtMinimaProdutoPedidoPorClasse() {
		String cdClasse = "3";
		double qtItensClasse = 6;
		TabPrecoClasseProd tabPrecoClasseProd = new TabPrecoClasseProd("1", "1", "2", cdClasse);
		tabPrecoClasseProd.qtMinPedido = 5;
		Map<String, Double> qtItensClasseHash = new HashMap<>();
		qtItensClasseHash.put(cdClasse, qtItensClasse);
		MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "validateQtMinProdutoClasse", qtItensClasseHash, cdClasse, tabPrecoClasseProd.qtMinPedido);
	}
	
	@Test
	public void testValorRetornadoIgualAZeroCasoFlagFlIgnoraVlMinParcelaCondPagtoHabilitada() {
		Pedido pedido = getPedidoPopuladoComCondPagtoETipoPedido(200.00, ValueUtil.VALOR_SIM);
		
		assertEquals(0.00, MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "getQtMinValorParcelaCondPagto", pedido));
	}
	
	@Test
	public void testValorRetornadoIgualQtMinValorParcelaCondPagtoCasoFlagFlIgnoraVlMinParcelaCondPagtoDesabilitadaOuNula() {
		Pedido pedido = getPedidoPopuladoComCondPagtoETipoPedido(200.00, ValueUtil.VALOR_NAO);
		
		assertEquals(200.00, MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "getQtMinValorParcelaCondPagto", pedido));
		
		pedido = getPedidoPopuladoComCondPagtoETipoPedido(200.00, null);
		
		assertEquals(200.00, MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "getQtMinValorParcelaCondPagto", pedido));
	}
	
	@Test
	public void testValorRetornadoIgualQtMinValorParcelaCondPagtoCasoTipoPedidoNulo() {
		Pedido pedido = getPedidoPopuladoComCondPagtoETipoPedido(200.00, ValueUtil.VALOR_NAO);
		pedido.setTipoPedido(null);
		
		assertEquals(200.00, MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "getQtMinValorParcelaCondPagto", pedido));
		
		pedido = getPedidoPopuladoComCondPagtoETipoPedido(200.00, ValueUtil.VALOR_SIM);
		pedido.setTipoPedido(null);
		
		assertEquals(200.00, MethodTestUtils.invokePrivateMethod(PedidoService.getInstance(), "getQtMinValorParcelaCondPagto", pedido));
	}
	
	public Pedido getPedidoPopuladoComCondPagtoETipoPedido(Double qtMinValorParcela, String flIgnoraVlMinParcelaCondPagto) {
		Pedido pedido = new Pedido();
		TipoPedido tipoPedido = new TipoPedido();
		CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
		
		condicaoPagamento.cdEmpresa = "1";
		condicaoPagamento.qtMinValorParcela = qtMinValorParcela;
		tipoPedido.flIgnoraVlMinParcelaCondPagto = flIgnoraVlMinParcelaCondPagto;
		
		pedido.setCondicaoPagamento(condicaoPagamento);
		pedido.setTipoPedido(tipoPedido);
		
		return pedido;
	}
	
	@Test
	public void testPermiteAlterarCondicaoComercialPedidoComItensQuandoParametroConfigurado() {
		LavenderePdaConfig.permiteAlterarCondicaoComercialPedido = true;
		LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento = false;
		assertTrue(PedidoService.getInstance().isPermiteAlterarCondicaoComercialPedido(new Pedido()));
	}

	@Test
	public void testNaoPermiteAlterarCondicaoComercialPedidoComItensQuandoParametroNaoConfigurado() {
		LavenderePdaConfig.permiteAlterarCondicaoComercialPedido = false;
		LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento = false;
		assertFalse(PedidoService.getInstance().isPermiteAlterarCondicaoComercialPedido(new Pedido()));
	}

	@Test
	public void testPermiteAlterarCondicaoComercialPedidoComItensQuandoParametroNaoConfiguradoEStatusOrcamentoPermiteAlterar() {
		Pedido pedido = new Pedido();
		StatusOrcamento statusOrcamento = new StatusOrcamento();
		statusOrcamento.flPermiteAlterarCondComercial = ValueUtil.VALOR_SIM;
		pedido.statusOrcamento = statusOrcamento;
		LavenderePdaConfig.permiteAlterarCondicaoComercialPedido = false;
		LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento = true;
		assertTrue(PedidoService.getInstance().isPermiteAlterarCondicaoComercialPedido(pedido));
	}

	@Test
	public void testNaoPermiteAlterarCondicaoComercialPedidoComItensQuandoParametroNaoConfiguradoEStatusOrcamentoNaoPermiteAlterar() {
		Pedido pedido = new Pedido();
		StatusOrcamento statusOrcamento = new StatusOrcamento();
		statusOrcamento.flPermiteAlterarCondComercial = ValueUtil.VALOR_NAO;
		pedido.statusOrcamento = statusOrcamento;
		LavenderePdaConfig.permiteAlterarCondicaoComercialPedido = false;
		LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento = true;
		assertFalse(PedidoService.getInstance().isPermiteAlterarCondicaoComercialPedido(pedido));
	}

}
