package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.builder.PedidoBuilder;
import br.com.wmw.lavenderepda.business.builder.ProdutoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.test.method.MethodTestUtils;
import totalcross.io.IOException;
import totalcross.json.JSONObject;
import totalcross.util.Vector;
import totalcross.xml.SyntaxException;

public class ItemPedidoServiceTestCase  {
	
	ItemPedido itemPedido;
	double vlPctMaxAcrescimo;
	double vlMaxAcrescimo;

	@Test
	public void testAplicaMaiorDescontoAutomaticoItemPedido() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "1";
		Pedido pedido = new Pedido("TBLVPPEDIDO");
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		Cliente cliente = new Cliente();
		CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
		condicaoPagamento.cdEmpresa = "1";
		itemPedido.setItemTabelaPreco(itemTabelaPreco);
		pedido.setCliente(cliente);
		pedido.setCondicaoPagamento(condicaoPagamento);
		LavenderePdaConfig.aplicaDescontoPromocionalAutomaticoItemTabPreco = true;
		LavenderePdaConfig.configIndiceFinanceiroClienteVlItemPedido = new JSONObject("{\"usa\":\"S\", \"nuCasasDecimaisCalcIndicFinanceiroCliente\":\"2\"}");
		LavenderePdaConfig.configIndiceFinanceiroCondPagto = new JSONObject("{\"aplicaIndiceVlItemPedido\":\"S\", \"exibeDescontoAcrescimoIndice\":\"N\", \"usaIndiceCondPagtoClienteConformePrazoMedio\":\"N\" }"); 

		//variável itemTabelaPreco.vlPctDescPromocional como maior desconto
		itemTabelaPreco.vlPctDescPromocional = 10;
		cliente.vlIndiceFinanceiro = 1.10;
		condicaoPagamento.vlIndiceFinanceiro = 0.95;

		//dominio 1
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "1";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 2
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "2";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 3
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "3";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 4
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "4";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//variável cliente.vlIndiceFinanceiro como maior desconto
		itemTabelaPreco.vlPctDescPromocional = 2;
		cliente.vlIndiceFinanceiro = 0.70;
		condicaoPagamento.vlIndiceFinanceiro = 0.90;

		//dominio 1
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "1";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(7.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(7.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 2
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "2";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(7.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(7.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 3
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "3";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 4
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "4";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(7.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(7.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//variável condicaoPagamento.vlIndiceFinanceiro como maior desconto
		itemTabelaPreco.vlPctDescPromocional = 2;
		cliente.vlIndiceFinanceiro = 0.95;
		condicaoPagamento.vlIndiceFinanceiro = 0.8;

		//dominio 1
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "1";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(8.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(8.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 2
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "2";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(8.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(8.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 3
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "3";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(8.000000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(8.000000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 4
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "4";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//variáveis zeradas
		itemPedido.vlBaseItemPedido = 0;
		itemPedido.vlUnidadePadrao = 0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "S";
		ItemPedidoService.getInstance().aplicaMaiorDescontoAutomaticoItemPedido(itemPedido, pedido);
		assertEquals(0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(0, itemPedido.vlUnidadePadrao, 0);
	}

	@Test
	public void testApplyIndiceFinanceiroCliente() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido("TBLVPPEDIDO");
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		Cliente cliente = new Cliente();
		CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
		itemPedido.setItemTabelaPreco(itemTabelaPreco);
		pedido.setCliente(cliente);
		pedido.setCondicaoPagamento(condicaoPagamento);
		cliente.vlIndiceFinanceiro = 0.95;
		LavenderePdaConfig.configIndiceFinanceiroClienteVlItemPedido = new JSONObject("{\"usa\":\"S\", \"nuCasasDecimaisCalcIndicFinanceiroCliente\":\"2\"}");

		//dominio 1
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "1";
		ItemPedidoService.getInstance().applyIndiceFinanceiroCliente(itemPedido, pedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

		//dominio 2
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "2";
		ItemPedidoService.getInstance().applyIndiceFinanceiroCliente(itemPedido, pedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

		//dominio 3
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "3";
		ItemPedidoService.getInstance().applyIndiceFinanceiroCliente(itemPedido, pedido);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 4
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "4";
		ItemPedidoService.getInstance().applyIndiceFinanceiroCliente(itemPedido, pedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

		//vlIndiceFinanceiro zerado
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "3";
		LavenderePdaConfig.configIndiceFinanceiroClienteVlItemPedido = new JSONObject("{\"usa\":\"N\", \"nuCasasDecimaisCalcIndicFinanceiroCliente\":\"2\"}");
		ItemPedidoService.getInstance().applyIndiceFinanceiroCliente(itemPedido, pedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

	}

	@Test
	public void testAplicaDescontoPromocionalAutomaticoItemTabelaPreco() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemPedido.setItemTabelaPreco(itemTabelaPreco);
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "1";
		itemTabelaPreco.vlPctDescPromocional = 5;
		LavenderePdaConfig.aplicaDescontoPromocionalAutomaticoItemTabPreco = true;

		//dominio 1
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "1";
		ItemPedidoService.getInstance().aplicaDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

		//dominio 2
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "2";
		ItemPedidoService.getInstance().aplicaDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlBaseItemPedido), 0);
		assertEquals(ValueUtil.round(9.500000001), ValueUtil.round(itemPedido.vlUnidadePadrao), 0);

		//dominio 3
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "3";
		ItemPedidoService.getInstance().aplicaDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

		//dominio 4
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "4";
		ItemPedidoService.getInstance().aplicaDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

		//vlPctDescPromocional zerado
		itemPedido.vlBaseItemPedido = 10.0;
		itemPedido.vlUnidadePadrao = 10.0;
		LavenderePdaConfig.aplicaMaiorDescontoNoItemPedido = "2";
		LavenderePdaConfig.aplicaDescontoPromocionalAutomaticoItemTabPreco = false;
		ItemPedidoService.getInstance().aplicaDescontoPromocionalAutomaticoItemTabelaPreco(itemPedido);
		assertEquals(10.0, itemPedido.vlBaseItemPedido, 0);
		assertEquals(10.0, itemPedido.vlUnidadePadrao, 0);

	}

	@Test
	public void testValidaPesoMaximoItemPedido() throws SQLException {
		Empresa empresa1 = new Empresa();
		Pedido pedido1 = new Pedido();
		ItemPedido itemPedido1 = new ItemPedido();
		CondicaoPagamento condPagto1 = new CondicaoPagamento();
		condPagto1.cdEmpresa = "1";
		pedido1.setCondicaoPagamento(condPagto1);
		pedido1.setEmpresa(empresa1);
		itemPedido1.pedido = pedido1;
		pedido1.qtPeso = 12;
		
		//--Controle por empresa com peso dentro do permitido
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = false;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = true;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 8;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado");
		}
		
		//--Controle por empresa com peso acima do permitido
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = false;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = true;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 9;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
			fail("Peso do pedido da empresa atual acima do permitido");
		} catch (ValidationException e) {
			//OK
		}
		
		//--Controle por empresa com peso máximo zerado
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = false;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = true;
		empresa1.vlMaxPeso = 0;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 15;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado");
		}
		
		//--Controle por condição de pagamento com peso dentro do permitido
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = true;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = false;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 3;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado");
		}
		
		//--Controle por condição de pagamento com peso acima do permitido
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = true;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = false;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 8;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
			fail("Peso do pedido da condição de pagamento atual acima do permitido");
		} catch (ValidationException e) {
			//OK
		}
		
		//--Controle por condição de pagamento com peso máximo zerado
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = true;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = false;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 0;
		itemPedido1.qtPeso = 15;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado");
		}

		//--Controle por condição de pagamento e por empresa com peso dentro do limite
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = true;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = true;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 3;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado");
		}
		
		//--Controle por condição de pagamento e por empresa com peso acima do limite
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = true;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = true;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 4;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
			fail("Peso do pedido da condição de pagamento atual acima do permitido");
		} catch (ValidationException e) {
			//OK
		}
		
		//--Controle por condição de pagamento e por empresa com peso acima do limite
		LavenderePdaConfig.usaControlePesoPedidoPorCondPagto = true;
		LavenderePdaConfig.usaControlePesoPedidoPorEmpresa = true;
		empresa1.vlMaxPeso = 20;
		condPagto1.vlMaxPeso = 15;
		itemPedido1.qtPeso = 4;
		try {
			PedidoService.getInstance().validaPesoMaximoPedido(pedido1, itemPedido1);
			fail("Peso do pedido da condição de pagamento atual acima do permitido");
		} catch (ValidationException e) {
			//OK
		}
	}

	@Test
	public void testValidaAcrescimoMaximoPermitido() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "1";
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemPedido.setItemTabelaPreco(itemTabelaPreco);
		itemPedido.vlBaseEmbalagemElementar = 10;
		itemPedido.vlBaseItemPedido = 20;
		itemTabelaPreco.vlMaxAcrescimo = 20;

		//--Sem unidade alternativa com valor de venda do item dentro do limite de acréscimo
		itemPedido.vlItemPedido = 40;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		try {
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
		} catch (ValidationException ex) {
			fail("Excedeu o limite de acréscimo");
		}

		//--Sem unidade alternativa com valor de venda do item acima do limite de acréscimo
		itemPedido.vlItemPedido = 41;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		try {
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
			fail("Excedeu o limite de acréscimo");
		} catch (ValidationException ex) {
			//ok
		}
		
		//--Parametro usaDescontoAcrescimoMaximoEmValor desligado.
		itemTabelaPreco.vlPctMaxAcrescimo = 10;
		itemPedido.vlItemPedido = 22;
		itemPedido.vlPctAcrescimo = 10;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "N";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = true;
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = true;
		try {
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
		} catch (ValidationException ex) {
			fail("Excedeu o limite de acréscimo");
		}
		
		itemTabelaPreco.vlPctMaxAcrescimo = 10;
		itemPedido.vlItemPedido = 23;
		itemPedido.vlPctAcrescimo = 15;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "N";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = true;
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = true;
		try {
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
			fail("Excedeu o limite de acréscimo");
		} catch (ValidationException ex) {
			//ok
		}

		//--Com unidade alternativa, acréscimo de 20 e valor de venda da embalagem elementar dentro do limite de acréscimo.
		itemPedido.vlEmbalagemElementar = 31;
		itemPedido.vlPctAcrescimo = 0;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = true;
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = true;
		try {
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
			fail("Excedeu o limite de acréscimo");
		} catch (ValidationException ex) {
			//ok
		}

		//--Campo acréscimo negativo.
		itemPedido.vlPctAcrescimo = -5;
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		try {
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
			fail("Valor de acréscimo negativo");
		} catch (ValidationException ex) {
			//ok
		}
	}

	@Test
	public void testCalculaValorPisECofins() throws SQLException {
		TributacaoConfig tributacaoConfig = new TributacaoConfig();
		ItemPedido itemPedido = new ItemPedido();
		Tributacao tributacao = new Tributacao();
		Produto produto = new Produto();
		
		//Teste com o parâmetro desligado
		LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado = false;
		tributacao.vlPctPis = 3.00;
		tributacao.vlMinPis = 0.15;
		tributacao.vlPctCofins = 3.00;
		tributacao.vlMinCofins = 0.15;
		produto.vlPctPis = 2.25;
		produto.vlMinIpi = 0.30;
		itemPedido.vlTotalItemPedido = 5.50;
		itemPedido.vlTotalItemPedidoFrete = 0.50;
		itemPedido.setProduto(produto);
		tributacaoConfig.flTipoCalculoPisCofins = "3";
		tributacaoConfig.flFreteBasePisCofins = ValueUtil.VALOR_NAO;
		ItemPedidoService.getInstance().calculaValorPisECofins(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.18, itemPedido.vlTotalPisItem, 2);
		assertEquals(0.18, itemPedido.vlTotalCofinsItem, 2);
		
		//Teste com o parâmetro ligado e flTipoCalculoPisCofins= "3"
		LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado = true;
		itemPedido.vlPis = 0;
		itemPedido.vlCofins = 0;
		ItemPedidoService.getInstance().calculaValorPisECofins(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.0, itemPedido.vlTotalPisItem);
		assertEquals(0.0, itemPedido.vlTotalCofinsItem);
		
		//Teste com flTipoCalculoPisCofins = "1"
		tributacaoConfig.flTipoCalculoPisCofins = "1";
		ItemPedidoService.getInstance().calculaValorPisECofins(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.165, itemPedido.vlTotalPisItem, 2);
		assertEquals(0.165, itemPedido.vlTotalCofinsItem, 2);
		//Teste com flFreteBasePisCofins = "S" 
		tributacaoConfig.flFreteBasePisCofins = ValueUtil.VALOR_SIM;
		ItemPedidoService.getInstance().calculaValorPisECofins(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.18, itemPedido.vlTotalPisItem, 2);
		assertEquals(0.18, itemPedido.vlTotalCofinsItem, 2);
	}

	@Test
	public void testGetQtItensDiferentesInseridosNoPedido() {
		ItemPedidoService itemPedidoService = ItemPedidoService.getInstance();
		Vector itemPedidoList = new Vector();
		ItemPedido itemPedido1 = new ItemPedido();
		itemPedido1.cdProduto = "1";
		ItemPedido itemPedido2 = new ItemPedido();
		itemPedido2.cdProduto = "2";
		ItemPedido itemPedido3 = new ItemPedido();
		itemPedido3.cdProduto = "3";
		itemPedidoList.addElement(itemPedido1);
		itemPedidoList.addElement(itemPedido2);
		itemPedidoList.addElement(itemPedido3);
		//--
		LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido = false;
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido = false;
		assertEquals(3, itemPedidoService.getQtItensDiferentesInseridosNoPedido(itemPedidoList));
		//--
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		assertEquals(3, itemPedidoService.getQtItensDiferentesInseridosNoPedido(itemPedidoList));
		//--
		LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido = true;
		LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido = true;
		assertEquals(3, itemPedidoService.getQtItensDiferentesInseridosNoPedido(itemPedidoList));
		//--
		ItemPedido itemPedido4 = new ItemPedido();
		itemPedido4.cdProduto = "3";
		itemPedidoList.addElement(itemPedido4);
		assertEquals(3, itemPedidoService.getQtItensDiferentesInseridosNoPedido(itemPedidoList));
		//--
		ItemPedido itemPedido5 = new ItemPedido();
		itemPedido5.cdProduto = "4";
		itemPedidoList.addElement(itemPedido5);
		assertEquals(4, itemPedidoService.getQtItensDiferentesInseridosNoPedido(itemPedidoList));
		//--
		ItemPedido itemPedido6 = new ItemPedido();
		itemPedido6.cdProduto = "4";
		itemPedidoList.addElement(itemPedido6);
		assertEquals(4, itemPedidoService.getQtItensDiferentesInseridosNoPedido(itemPedidoList));
	}

	@Test
	public void testRetiraDescontosDoItemPedido() {
		ItemPedidoService itemPedidoService = ItemPedidoService.getInstance();
		Pedido pedido = new Pedido();
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.vlBaseItemPedido = 100;
		itemPedido.setQtItemFisico(1);
		//--
		itemPedidoService.retiraDescontosDoItemPedido(pedido, itemPedido);
		assertEquals(ValueUtil.round(100), itemPedido.vlTotalItemPedido);
		//--
		itemPedido.vlPctDesconto = 10;
		itemPedidoService.retiraDescontosDoItemPedido(pedido, itemPedido);
		assertEquals(ValueUtil.round(90), itemPedido.vlTotalItemPedido);
		//--
		itemPedido.vlPctDesconto = 10;
		pedido.vlPctDescProgressivo = 10;
		itemPedidoService.retiraDescontosDoItemPedido(pedido, itemPedido);
		assertEquals(ValueUtil.round(100), itemPedido.vlTotalItemPedido);
		//--
		LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido = true;
		itemPedido.vlPctDesconto = 0;
		itemPedidoService.retiraDescontosDoItemPedido(pedido, itemPedido);
		assertEquals(ValueUtil.round(100), itemPedido.vlTotalItemPedido);
		//--
		itemPedido.vlPctDesconto = 10;
		pedido.vlPctDescProgressivoMix = 10;
		itemPedidoService.retiraDescontosDoItemPedido(pedido, itemPedido);
		assertEquals(ValueUtil.round(100), itemPedido.vlTotalItemPedido);
	}

	@Test
	public void testCalculoRentabilidadeItem() throws SQLException {
		LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado = false;
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		
		Produto produto = new ProdutoBuilder("1", "1", "1").build();
		produto.vlPrecoCusto = 8;
		produto.dsProduto = "1";
		ItemPedido itemPedido = new ItemPedidoBuilder("1", "1", "1", "P").comTabelaPreco().build();
		Pedido pedido = new PedidoBuilder(itemPedido).build();
		Empresa empresa = new Empresa();
		empresa.cdEmpresa = "1";
		empresa.vlPctCofins = 2;
		empresa.vlPctPis = 1.5;
		empresa.vlPctIrpj = 3;
		pedido.setEmpresa(empresa);
		itemPedido.setPedido(pedido);
		itemPedido.setProduto(produto);
		itemPedido.vlItemPedido = 10;
		itemPedido.vlSt = 1;
		itemPedido.setQtItemFisico(1);
		//--
		LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido = 5;
		ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, pedido);
		assertEquals(27.27, itemPedido.vlRentabilidade, 0.001);
		//--
		// Dominio 5 guarda a porcentagem na vlRentabilidade ao invés do valor, não deve sofrer alteração com maior quantidade de itens
		LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido = 5;
		itemPedido.setQtItemFisico(10);
		ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, pedido);
		assertEquals(27.27, itemPedido.vlRentabilidade, 0.001);
		//--
		itemPedido.getItemTabelaPreco().vlBase = 24.533;
		itemPedido.vlTotalItemPedido = 31.10;
		LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido = 6;
		itemPedido.setQtItemFisico(1);
		ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, pedido);
		assertEquals(26.77, itemPedido.vlRentabilidade, 0.001);
		//--
		itemPedido.getItemTabelaPreco().vlBase = 0;
		itemPedido.vlTotalItemPedido = 0;
		itemPedido.setQtItemFisico(10);
		//--
		itemPedido.setQtItemFisico(1);
		LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido = 3;
		ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, pedido);
		assertEquals(9.32, itemPedido.vlRentabilidade, 0.001);
		//--
		LavenderePdaConfig.formulaCalculoRentabilidadeNoPedido = 1;
		itemPedido.setQtItemFisico(1);
		itemPedido.getItemTabelaPreco().vlBase = 10;
		itemPedido.vlTotalItemPedido = 11;
		ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, pedido);
		assertEquals(1, itemPedido.vlRentabilidade, 0.001);
	}

	@Test
	public void testGetPesoItemPedido() throws SQLException {
		//Teste com propriedade forumlaCalculo = 1
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.configCalculoPesoPedido = "{\"formulaCalculo\":\"1\", \"mostraVPesoCapaPedido\":\"N\", \"mostraVPesoListaItens\": \"N\"}";
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produtoUnidade.unidade = new Unidade();
		produtoUnidade.unidade.cdUnidade = "UN";
		ItemPedidoService itemPedidoService = ItemPedidoService.getInstance();
		ItemPedido itemPedido = new ItemPedido();
		Produto produto = new Produto();
		produto.dsProduto = "1";
		produto.qtPeso  = 2.0;
		itemPedido.setQtItemFisico(1.0);

		//Teste multiplicação
		produtoUnidade.flDivideMultiplica = "M";
		itemPedido.setProdutoUnidade(produtoUnidade);
		itemPedido.setProduto(produto);
		itemPedido.nuConversaoUnidade = 12.0;
		itemPedido.setQtItemFisico(1.0);
		assertEquals(24.0,itemPedidoService.getPesoItemPedido(itemPedido), 2);

		//Teste divisão
		produtoUnidade.flDivideMultiplica = "D";
		itemPedido.nuConversaoUnidade = 2.0;
		itemPedido.setQtItemFisico(6.0);
		assertEquals(6.0,itemPedidoService.getPesoItemPedido(itemPedido),2);

		//Teste com paramêtro de conversão de unidade desligado
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		itemPedido.setQtItemFisico(6.0);
		assertEquals(12.0,itemPedidoService.getPesoItemPedido(itemPedido),2);

		//Teste com qtPeso zerado e com paramêtro de conversão de unidade desligado
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		produto.qtPeso  = 0.0;
		assertEquals(6.0,itemPedidoService.getPesoItemPedido(itemPedido),2);

		//Teste com divisão por zero
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		produtoUnidade.flDivideMultiplica = "D";
		itemPedido.nuConversaoUnidade = 0.0;
		itemPedido.setQtItemFisico(1.0);
		produto.qtPeso  = 2.0;
		assertEquals(2.0,itemPedidoService.getPesoItemPedido(itemPedido),2);

		//Teste com multiplicação por zero
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		produtoUnidade.flDivideMultiplica = "M";
		itemPedido.nuConversaoUnidade = 0.0;
		itemPedido.setQtItemFisico(1.0);
		produto.qtPeso  = 2.0;
		assertEquals(2.0,itemPedidoService.getPesoItemPedido(itemPedido),2);
		
		//Teste com propriedade forumlaCalculo = 2
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.configCalculoPesoPedido = "{\"formulaCalculo\":\"2\", \"mostraVPesoCapaPedido\":\"N\", \"mostraVPesoListaItens\": \"N\"}";
		produtoUnidade.unidade.flCalculaPesoGramatura = "S";
		produtoUnidade.unidade.cdUnidade = "UN";
		itemPedidoService = ItemPedidoService.getInstance();
		produto.qtPeso = 2.0;
		produto.vlGramatura = 1.0;
		itemPedido.setProduto(produto);
		itemPedido.setQtItemFisico(6.0);
		itemPedido.vlLargura = 150.0;
		itemPedido.vlComprimento = 500.0;
		assertEquals(0.9, itemPedidoService.getPesoItemPedido(itemPedido), 2);
		
		//Teste com flCalculaPesoGramatura desligado
		produtoUnidade.unidade.flCalculaPesoGramatura = "N";
		assertEquals(6.0, itemPedidoService.getPesoItemPedido(itemPedido), 2);
		
		//Teste com gramatura zero
		produtoUnidade.unidade.flCalculaPesoGramatura = "S";
		produto.vlGramatura = 0.0;
		assertEquals(0.0, itemPedidoService.getPesoItemPedido(itemPedido), 2);
		
		//Teste com qtPeso zerado e com paramêtro de conversão de unidade desligado
		LavenderePdaConfig.usaUnidadeAlternativa = false;
		produto.qtPeso = 0.0;
		assertEquals(6.0, itemPedidoService.getPesoItemPedido(itemPedido), 2);
	}

	@Test
	public void testValidateDescontoMaximoPermitido() throws IOException, SyntaxException, SQLException {
		Pedido pedido = new Pedido();
		pedido.nuPedido = "1";
		pedido.cdEmpresa ="1";
		pedido.cdRepresentante = "1";
		Cliente cliente = new Cliente();
		cliente.cdCliente = "1";
		pedido.cdCliente = "1";
		pedido.setCliente(cliente);
		//--
		ItemPedido itemPedido1 = new ItemPedido();
		itemPedido1.pedido = pedido;
		itemPedido1.cdEmpresa = "1";
		itemPedido1.cdRepresentante = "1";
		itemPedido1.cdProduto = "1";
		itemPedido1.setQtItemFisico(1);
		itemPedido1.vlBaseItemPedido = 12;
		itemPedido1.vlItemPedido = 10.2;
		itemPedido1.vlPctDesconto = 15;
		itemPedido1.vlVerbaItem = -1;
		itemPedido1.vlBaseFlex = 11.2;
		Produto produto = new Produto();
		produto.cdProduto = "1";
		produto.dsProduto = "1";
		itemPedido1.setProduto(produto);
		ItemTabelaPreco itemTabelaPreco1 = new ItemTabelaPreco();
		itemTabelaPreco1.vlPctMaxDesconto = 10;
		
		itemTabelaPreco1.vlBase = 11.2;
		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.vlPctMaxDescAdicionalItem = 8;
		itemTabelaPreco1.setTabelaPreco(tabelaPreco);
		itemPedido1.setItemTabelaPreco(itemTabelaPreco1);
		DescQuantidade descQuantidade = new DescQuantidade();
		descQuantidade.qtItem = 1;
		descQuantidade.vlPctDesconto = 12;
		itemPedido1.descQuantidade = descQuantidade;
		itemPedido1.vlDescontoCCP = 0.25;

		//-- Desc:15% - MáxPermitido:10% (Nenhum parametro de consistencia ligado)
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "1";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.aplicaDescontoCCPAposInserirItem = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		
		//-- Desc:15% - MáxPermitido:10%
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "N";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		
		//-- Desc:15% - MáxPermitido:10% 
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.aplicaDescontoCCPAposInserirItem = true;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		
		//-- Desc:15% - MáxPermitido:10% 
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.aplicaDescontoCCPAposInserirItem = true;
		itemPedido1.descQuantidade = null;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		
		//-- Desc:15% - MáxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - MáxPermitido:10%+(8% adicional)
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo ");
		}
		//-- Desc:15% - MáxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - MáxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = true;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - MáxPermitido:10%+(8% adicional)
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = true;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		itemPedido1.descQuantidade = descQuantidade;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1); 
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - DescQtde:0% - MaxPermitido:15%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = true;
		itemPedido1.descQuantidade = null;
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 15;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:20% - DescQtde:0% - MaxPermitido:15%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = true;
		itemPedido1.vlItemPedido = 9.6;
		itemPedido1.vlPctDesconto = 20;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:10% - DescQtde:0% - MaxPermitido:15%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "N";
		itemPedido1.descQuantidade = null;
		itemPedido1.vlItemPedido = 10.8;
		itemPedido1.vlPctDesconto = 10;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:20% - DescQtde:0% - MaxPermitido:15%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "N";
		itemPedido1.descQuantidade = null;
		itemPedido1.vlItemPedido = 9.6;
		itemPedido1.vlPctDesconto = 20;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - DescQtde:0% - MaxPermitido:15%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		itemPedido1.descQuantidade = null;
		itemPedido1.vlItemPedido = 10.2;
		itemPedido1.vlPctDesconto = 15;
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 15;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - DescQtde:0% - MaxPermitido:15%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		itemPedido1.descQuantidade = null;
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 15;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:15% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		itemPedido1.descQuantidade = descQuantidade;
		itemPedido1.descQuantidade.vlPctDesconto = 15;
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 10;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:17% - DescQtde:15% - MaxPermitido:20%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "1";
		itemPedido1.descQuantidade = descQuantidade;
		itemPedido1.descQuantidade.vlPctDesconto = 15;
		itemPedido1.vlPctDesconto = 17;
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 20;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:17% - DescQtde:15% - MaxPermitido:20%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "2";
		itemPedido1.descQuantidade = descQuantidade;
		itemPedido1.descQuantidade.vlPctDesconto = 15;
		itemPedido1.vlPctDesconto = 17;
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 20;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:15% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "1";
		itemPedido1.getItemTabelaPreco().vlPctMaxDesconto = 10;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - DescQtde:12%+(8% adicional) - MaxPermitido:10%+(8% adicional)
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		itemPedido1.descQuantidade.vlPctDesconto = 12;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = true;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - DescQtde:12% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = true;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "S";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = true;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = true;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = true;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = true;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			assertNotNull(e.getMessage());
		}
		//-- Desc:15% - DescQtde:12%+(8% adicional) - MaxPermitido:10%+(8% adicional)
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = true;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12%+(8% adicional) - MaxPermitido:10%+(8% adicional)
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12%+(8% adicional) - MaxPermitido:10%+(8% adicional)
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = true;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = true;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		//-- Desc:15% - DescQtde:12% - MaxPermitido:10%
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		pedido.flPrecoLiberadoSenha = ValueUtil.VALOR_SIM;
		itemPedido1.ignoraValidacaoDesconto = true;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (Exception e) {
			fail("Não deveria ter validado o % desconto máximo");
		}

		//-- Desc:155% - MáxPermitido:10%
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "1";
		itemPedido1.vlPctDesconto = 155;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado, pois o % é maior que 100%");
		} catch (Exception e) {
			assertNotNull(e.getMessage());
		}
		//-- Valor base item = 12 - Valor item venda = 10.2 - MáxDescPermitido:2 (Parametro usaDescontoAcrescimoMaximoEmValor ligado)
		itemPedido1.vlPctDesconto = 1.5;
		itemTabelaPreco1.vlMaxDesconto = 2;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "2";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		itemPedido1.ignoraValidacaoDesconto = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "1";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.aplicaDescontoCCPAposInserirItem = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
		} catch (ValidationException e) {
			fail("Não deveria ter validado o % desconto máximo");
		}
		
		//-- Valor base item = 12 - Valor item venda = 10.2  - MáxDescPermitido:1 (Parametro usaDescontoAcrescimoMaximoEmValor ligado)
		itemTabelaPreco1.vlMaxDesconto = 1;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "2";
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		itemPedido1.ignoraValidacaoDesconto = false;
		LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo = "1";
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.permiteDescAdicionalPorTabPreco = false;
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = false;
		LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex = false;
		LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco = false;
		LavenderePdaConfig.usaDescontoMaximoBaseadoNoVlBaseItemPedidoQuandoUsaFlex = false;
		LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco = false;
		LavenderePdaConfig.aplicaDescontoCCPAposInserirItem = false;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido1);
			fail("Deveria ter validado o % desconto máximo");
		} catch (ValidationException e) {
			//ok
		}
	}
	
	@BeforeEach
	protected void setUp() throws Exception {
		itemPedido = new ItemPedidoBuilder("1", "1", "1", "P").build();
		itemPedido.vlBaseItemPedido = 10;
		itemPedido.vlItemPedido = 11;
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemTabelaPreco.cdEmpresa = itemPedido.cdEmpresa;
		itemTabelaPreco.cdRepresentante = itemPedido.cdRepresentante;
		itemTabelaPreco.cdTabelaPreco = "1";
		itemTabelaPreco.vlReducaoOptanteSimples = 0.1;
		Produto produto = new ProdutoBuilder(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdProduto).build();
		itemPedido.setItemTabelaPreco(itemTabelaPreco);
		itemPedido.setProduto(produto);
		Cliente cliente = new Cliente();
		cliente.flOptanteSimples = ValueUtil.VALOR_SIM;
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		itemPedido.setPedido(pedido);
	}
	
	@AfterEach
	protected void tearDown() throws Exception {
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "N";
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "N";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples = false;
		LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem = false;
		LavenderePdaConfig.usaDescontosEmCascataManuaisNaCapaPedidoPorItem =  "N";
	}

	@Test
	public void testNaoDeveConsistirAcrescimoMaximo() throws SQLException {
		vlPctMaxAcrescimo = 0;
		vlMaxAcrescimo = 0;
		LavenderePdaConfig.naoConsisteDescontoAcrescimoMaximo = "1";
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
		} catch (ValidationException e) { 
			fail("Não deveria ter validado o acréscimo");
		}
	}

	@Test
	public void testDevePermitirPctAcrescimoMaximo() throws SQLException {
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "10";
		vlPctMaxAcrescimo = 10;
		vlMaxAcrescimo = 0;
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
		} catch (ValidationException e) { 
			fail("Deveria ter liberado o % acréscimo máximo");
		}
	}

	@Test
	public void testNaoDevePermitirAcrescimoMaximoComReducaoOptanteSimples() throws SQLException {
		LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples = true;
		LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem = true;
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "10";
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		vlPctMaxAcrescimo = 10;
		vlMaxAcrescimo = 0;
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
			fail("Deveria ter bloqueado o acréscimo");
		} catch (ValidationException e) { }
	}

	@Test
	public void testNaoDevePermitirAcrescimoMaximo() throws SQLException {
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "10";
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		vlPctMaxAcrescimo = 8;
		vlMaxAcrescimo = 0;
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
			fail("Deveria ter bloqueado o acréscimo");
		} catch (ValidationException e) { }
	}

	@Test
	public void testNaoDevePermitirValorAcrescimoMaximo() throws SQLException {
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "10";
		vlPctMaxAcrescimo = 11;
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
			fail("Deveria ter bloqueado o acréscimo");
		} catch (ValidationException e) { }
	}

	@Test
	public void testDevePermitirValorAcrescimoMaximo() throws SQLException {
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "10";
		LavenderePdaConfig.usaDescontoAcrescimoMaximoEmValor = "3";
		vlPctMaxAcrescimo = 0;
		vlMaxAcrescimo = 1;
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
		} catch (ValidationException e) { 
			fail("Deveria ter liberado o % acréscimo máximo");
		}
	}

	@Test
	public void testDevePermitirPctAcrescimoMaximoEmbalagemElementar() throws SQLException {
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "10";
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = true;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = true;
		itemPedido.vlBaseEmbalagemElementar = 10;
		itemPedido.vlEmbalagemElementar = 11;
		vlPctMaxAcrescimo = 10;
		vlMaxAcrescimo = 0;
		try {
			ItemPedidoService.getInstance().consisteAcrescimoMaximo(itemPedido, vlPctMaxAcrescimo, vlMaxAcrescimo, false);
		} catch (ValidationException e) { 
			fail("Deveria ter liberado o % acréscimo máximo");
		}
	}

	@Test
	public void testValidaPctAcrescimoNegativo() {
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "3";
		itemPedido.vlPctAcrescimo = 2;
		try {
			ItemPedidoService.getInstance().validaPctAcrescimoNegativo(itemPedido);
		} catch (ValidationException e) { 
			fail("Deveria ter liberado o % acréscimo");
		}
		itemPedido.vlPctAcrescimo = -2;
		try {
			ItemPedidoService.getInstance().validaPctAcrescimoNegativo(itemPedido);
			fail("Deveria ter bloqueado o % acréscimo");
		} catch (ValidationException e) { }
	}

	@Test
	public void testAplicaDescontoCascataManual() {
		LavenderePdaConfig.usaDescontosEmCascataManuaisNaCapaPedidoPorItem = "S";
		Pedido pedido = itemPedido.pedido;
		pedido.vlPctDescCliente = 7;
		pedido.vlPctDescontoCondicao = 5.5;
		pedido.vlPctDescFrete = 4.358;
		itemPedido.vlPctDesconto = 10;
		//--
		ItemPedidoService.getInstance().aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(itemPedido, pedido);
		assertEquals(ValueUtil.round(7.57), ValueUtil.round(itemPedido.vlItemPedido), 0.001);
		//--
		LavenderePdaConfig.usaDescontosEmCascataManuaisNaCapaPedidoPorItem = "2";
		ItemPedidoService.getInstance().aplicaDescontosEmCascataManuaisNaCapaPedidoPorItem(itemPedido, pedido);
		assertEquals(ValueUtil.round(7.56), ValueUtil.round(itemPedido.vlItemPedido), 0.001);
	}

	@Test
	public void testGetVlPctMaxDescPorRedutorCliente() {
		ItemTabelaPreco itemTabPreco = new ItemTabelaPreco();
		Produto produto = new Produto();
		double vlPctMaxDescontoCli = 0;
		double vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(0), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 12;
		itemTabPreco.vlPctMaxDesconto = 10;
		vlPctMaxDescontoCli = 2;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(20), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 10;
		vlPctMaxDescontoCli = 2;
		itemTabPreco.vlPctMaxDesconto = 10;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(18), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 0;
		vlPctMaxDescontoCli = 2;
		itemTabPreco.vlPctMaxDesconto =10;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(8), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 0;
		vlPctMaxDescontoCli = 0;
		itemTabPreco.vlPctMaxDesconto = 10;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(10), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 10;
		vlPctMaxDescontoCli = 0;
		itemTabPreco.vlPctMaxDesconto = 10;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(20), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 10;
		vlPctMaxDescontoCli = 2;
		itemTabPreco.vlPctMaxDesconto = 0;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(8), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 0;
		vlPctMaxDescontoCli = 2;
		itemTabPreco.vlPctMaxDesconto =0;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(0), vlPctMaxDesconto);
		//--
		itemTabPreco.vlPctDescValorBase = 102;
		vlPctMaxDescontoCli = 0;
		itemTabPreco.vlPctMaxDesconto = 0;
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescPorRedutorCliente(itemTabPreco, vlPctMaxDescontoCli, produto);
		assertEquals(new Double(100), vlPctMaxDesconto);
	}

	@Test
	public void testGetVlBaseFlexPorRedutorCliente() {
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemTabelaPreco.vlUnitario = 0;
		itemTabelaPreco.vlPctDescValorBase = 0;
		double vlIndiceFinanceiroCondPagto = 0;
		double vlPctMaxDescontoCli = 0;
		double vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(0), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 1;
		itemTabelaPreco.vlPctDescValorBase = 0;
		vlIndiceFinanceiroCondPagto = 0;
		vlPctMaxDescontoCli = 0;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(1), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 1;
		itemTabelaPreco.vlPctDescValorBase = 0;
		vlIndiceFinanceiroCondPagto = 1;
		vlPctMaxDescontoCli = 0;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(1), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 43.80;
		itemTabelaPreco.vlPctDescValorBase = 12;
		vlIndiceFinanceiroCondPagto = 1;
		vlPctMaxDescontoCli = 2;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(39.42), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 43.80;
		itemTabelaPreco.vlPctDescValorBase = 12;
		vlIndiceFinanceiroCondPagto = 1;
		vlPctMaxDescontoCli = 0;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(38.54), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 43.80;
		itemTabelaPreco.vlPctDescValorBase = 12;
		vlIndiceFinanceiroCondPagto = 0;
		vlPctMaxDescontoCli = 0;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(38.54), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 43.80;
		itemTabelaPreco.vlPctDescValorBase = 12;
		vlIndiceFinanceiroCondPagto = 0;
		vlPctMaxDescontoCli = 12;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(43.80), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 50;
		itemTabelaPreco.vlPctDescValorBase = 0;
		vlIndiceFinanceiroCondPagto = 0;
		vlPctMaxDescontoCli = 12;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(50), vlBaseFlex);
		//--
		itemTabelaPreco.vlUnitario = 50;
		itemTabelaPreco.vlPctDescValorBase = 2;
		vlIndiceFinanceiroCondPagto = 0;
		vlPctMaxDescontoCli = 12;
		vlBaseFlex = itemTabelaPreco.getVlBaseFlexPorRedutorCliente(vlIndiceFinanceiroCondPagto, vlPctMaxDescontoCli);
		assertEquals(new Double(50), vlBaseFlex);
	}

	@Test
	public void testMantemPrecoNegociadoReplicacaoPedido() {
		
		Pedido pedido = new Pedido();
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.pedido = pedido;
		pedido.itemPedidoPrecoNegociadoList = new Vector();
		
		//Teste 1
		itemPedido.vlNegociado = 8;
		itemPedido.vlPctDesconto = 20;
		LavenderePdaConfig.mantemPrecoNegociadoReplicacaoPedido = false;
		ItemPedidoService.getInstance().mantemPrecoNegociadoReplicacaoPedido(pedido, itemPedido, false);
		assertEquals(20.0, itemPedido.vlPctDesconto);
		assertEquals(0.0, itemPedido.vlPctAcrescimo);
		
		LavenderePdaConfig.mantemPrecoNegociadoReplicacaoPedido = true;
		LavenderePdaConfig.permiteDescontoAcrescimoItemPedido = "9";
		LavenderePdaConfig.nuCasasDecimais = 2;
		
		//Teste 2
		itemPedido.vlBaseItemPedido = 12;
		ItemPedidoService.getInstance().mantemPrecoNegociadoReplicacaoPedido(pedido, itemPedido, true);
		assertEquals(33.3, ValueUtil.round(itemPedido.vlPctDesconto));
		assertEquals(0.0, itemPedido.vlPctAcrescimo);
		
		//Teste 3
		itemPedido.vlNegociado = 7;
		ItemPedidoService.getInstance().mantemPrecoNegociadoReplicacaoPedido(pedido, itemPedido, true);
		assertEquals(41.63, ValueUtil.round(itemPedido.vlPctDesconto));
		assertEquals(0.0, itemPedido.vlPctAcrescimo);
		
	}

	@Test
	public void testDeveRetornarVerdadeiroQtBonificadaDentroPercentualMax() throws SQLException {

		String configBonificacaoItemPedido = "{ \"usa\":\"S\", \"consomeVerba\":\"N\", \"permiteBonificarQualquerProduto\":\"N\", \"percMaxQuantidadeBonificada\":\"50\", \"percMaxValorPedidoBonificado\":\"10\", \"validaPercMaxValorPedidoBonificadoNoFechamento\":\"S\" }";
		LavenderePdaConfig.configBonificacaoItemPedido = new JSONObject(configBonificacaoItemPedido);
		Pedido pedido = new Pedido();

		ItemPedido itemPedidoVenda = new ItemPedido();
		itemPedidoVenda.pedido = pedido;
		itemPedidoVenda.cdProduto = "1";
		itemPedidoVenda.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedidoVenda.setQtItemFisico(30d);

		ItemPedido itemPedidoBonificado = new ItemPedido();
		itemPedidoBonificado.pedido = pedido;
		itemPedidoBonificado.cdProduto = "1";
		itemPedidoBonificado.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		itemPedidoBonificado.setQtItemFisico(itemPedidoVenda.getQtItemFisico() * (LavenderePdaConfig.getPercMaxQuantidadeBonificada() / 100));

		itemPedido.pedido.itemPedidoList.addElement(itemPedidoVenda);
		itemPedido.pedido.itemPedidoList.addElement(itemPedidoBonificado);

		assertTrue(ItemPedidoService.getInstance().validaQtItemBonificado(itemPedidoBonificado));
	}
	
	@Test
	public void deveAplicarJurosSimplesNoValorBaseConformeQtDiasMedioPagamento() throws SQLException {
		LavenderePdaConfig.aplicaDeflatorCondPagtoValorTotalPedido = "4";
		ItemPedido itemPedido = createNewItemPedidoDeflatorCondPagto(Produto.FLFORMULACALCULO_JUROSSIMPLES, 21, 2.5);
		CondicaoPagamento condicaoPagto = createNewCondicaoPagtoDeflator(21);
		
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(320.51, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Simples, têm-se = 315 * (1 + 21 * 2.5 / 30 / 100) = 320.51 ");
		
		itemPedido.vlBaseItemPedido = 315;
		condicaoPagto.qtDiasMediosPagamento = 45;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(326.81, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Simples, têm-se = 315 * (1 + 45 * 2.5 / 30 / 100) = 326.81 ");
		
		itemPedido.vlBaseItemPedido = 315;
		condicaoPagto.qtDiasMediosPagamento = 37;
		itemPedido.getProduto().flFormulaCalculoDeflator = null;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(324.71, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Simples, têm-se = 315 * (1 + 37 * 2.5 / 30 / 100) = 324.71 ");
		
		itemPedido.vlBaseItemPedido = 122;
		condicaoPagto.qtDiasMediosPagamento = 48;
		itemPedido.getProduto().vlPctJurosMensal = 4;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(129.81, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Simples, têm-se = 122 * (1 + 48 * 4 / 30 / 100) = 129.81");
	}
	
	@Test
	public void deveAplicarJurosCompostoNoValorBaseConformeFormula() throws SQLException {
		LavenderePdaConfig.aplicaDeflatorCondPagtoValorTotalPedido = "4";
		ItemPedido itemPedido = createNewItemPedidoDeflatorCondPagto(Produto.FLFORMULACALCULO_JUROSCOMPOSTO, 30, 3);
		CondicaoPagamento condicaoPagto = createNewCondicaoPagtoDeflator(21);
		
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(315, itemPedido.vlBaseItemPedido, "Dias médio de pagamento é menor que a carencia da empresa, portanto não sofre alteração no valor base");
		
		condicaoPagto.qtDiasMediosPagamento = 45;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(319.76, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Composto, têm-se = 315 * (1 + 3 / 30 / 100) ^ (45 - 30) = 319.76");
		
		itemPedido.vlBaseItemPedido = 315;
		condicaoPagto.qtDiasMediosPagamento = 37;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(317.21, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Composto, têm-se = 315 * (1 + 3 / 30 / 100) ^ (37 - 30) = 317.21");

		itemPedido.vlBaseItemPedido = 298;
		condicaoPagto.qtDiasMediosPagamento = 42;
		itemPedido.getProduto().vlPctJurosMensal = 4;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(302.80, itemPedido.vlBaseItemPedido, "Ao substituir os valores na formula de Juros Composto, têm-se = 298 * (1 + 4 / 30 / 100) ^ (42 - 30) = 302.80");
		
		LavenderePdaConfig.aplicaDeflatorCondPagtoValorTotalPedido = "N";
		itemPedido.vlBaseItemPedido = 315;
		ItemPedidoService.getInstance().aplicaDeflatorCondPagtoItemPedido(itemPedido, condicaoPagto);
		assertEquals(315, itemPedido.vlBaseItemPedido, "Não deve alterar o valor base caso o parametro nao esteja ativado");
	}
	
	@Test
	public void deveCalcularDescontoManualConsiderandoPctDescontoDaFaixaQtde() {
		LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido = true;
		LavenderePdaConfig.nuCasasDecimais = 2;
		ItemPedidoService itemPedidoService = ItemPedidoService.getInstance();
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.descQuantidade = new DescQuantidade();
		itemPedido.descQuantidade.vlPctDesconto = itemPedido.vlPctFaixaDescQtd = 4.76;
		itemPedido.vlBaseItemPedido = 39.90;
		itemPedido.vlItemPedido = 35;
		double vlPctDescontoManual = itemPedidoService.getVlPctDescontoSemDescQtdFromItemPedido(itemPedido);
		
		assertEquals(7.52, vlPctDescontoManual, "Ao editar o valor do item para R$35,00, o sistema precisa calcular o desconto proporcional em cima do valor do item pós aplicação do descQtd. (aplicaDescQtdItemPedido) 39.90 * (1 - (4.76 + 7.52) / 100)) = 35 (vlItemPedido).");
		
		itemPedido.vlItemPedido = 38;
		vlPctDescontoManual = itemPedidoService.getVlPctDescontoSemDescQtdFromItemPedido(itemPedido);
		assertEquals(0, vlPctDescontoManual, "O valor informado é o valor final aplicado o descQtde, logo não tem desconto manual.");
		
		itemPedido.vlPctDesconto = 96;
		ValidationException validationException = assertThrows(ValidationException.class, () -> itemPedidoService.validaDescontoMaximoPermitido(itemPedido));
		String expectedMessage = MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ACUMULA_ULTRAPASSADO, new String[] { StringUtil.getStringValueToInterface(itemPedido.vlPctFaixaDescQtd), StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto), StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto + itemPedido.vlPctFaixaDescQtd)});
		
		assertEquals(expectedMessage, validationException.getMessage());
	}
	
	@Test
	public void deveValidarAQuantidadeMaxDoItemBonificacaoOuTrocaEmRelacaoAoPercentualMaximoPermitidoDoPedidoOriginal() {
		ItemPedidoService itemPedidoService = ItemPedidoService.getInstance();
		LavenderePdaConfig.usaQtdItemPedidoInteiro = ValueUtil.VALOR_NAO;
		Pedido pedido = getPedidoComItens();
		ItemPedido itemPedido = (ItemPedido) ((ItemPedido)pedido.itemPedidoList.elementAt(1)).clone();
		itemPedido.qtItemFisico = 20;
		
		ValidationException ex = assertThrows(ValidationException.class, () -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoQtTotalVendido", pedido, itemPedido, 55, 105);
		});
		String expectedMessage = MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_ACIMA_PERMITIDO_BONIFICACAOTROCA, 
				new String[] {StringUtil.getStringValueToInterface(57.75), MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_TOTAL_BONIFICACAOTROCA, StringUtil.getStringValueToInterface(60.0))});
		assertEquals(expectedMessage, ex.getMessage(), "A soma dos itens do pedido atual é de 84 (8 + 20 + 24 + 32), porém o terceiro item é desconsiderado, portanto 60 itens válidos. Onde foi simulado uma edição do segundo item de 16 qtd para 20."
				+ "O limite de bonificação/troca desse pedido é de 55% de 105 qtd do pedido original, ou seja um limite de 57.75 qtd");
		
		//--
		LavenderePdaConfig.usaQtdItemPedidoInteiro = ValueUtil.VALOR_SIM;
		ItemPedido itemPedido2 = (ItemPedido) ((ItemPedido)pedido.itemPedidoList.elementAt(3)).clone();
		pedido.itemPedidoList.removeElementAt(3);
		ex = assertThrows(ValidationException.class, () -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoQtTotalVendido", pedido, itemPedido2, 100, 55);
		});
		expectedMessage = MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_ACIMA_PERMITIDO_BONIFICACAOTROCA, 
				new String[] {StringUtil.getStringValueToInterface(55), MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITENS_RESTANTE_BONIFICACAOTROCA, StringUtil.getStringValueToInterface(31))});
		assertEquals(expectedMessage, ex.getMessage(), "A soma dos itens do pedido atual é de 48 (8 + 16 + 24). O terceiro item é desconsiderado, portanto 24 itens válidos. Onde foi simulado uma inclusão de um item de 32 qtd"
				+ "O limite de bonificação/troca desse pedido é de 100% de 55 qtd do pedido original, ou seja um limite de 55 qtd");
		
		//--
		assertDoesNotThrow(() -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoQtTotalVendido", pedido, itemPedido2, 72, 80);
		}, "Não deve gerar exception pois a quantidade bonificada não ultrapassou o limite de quantidade em relação ao pedido original.");
		
		//--
		pedido.onFechamentoPedido = true;
		ex = assertThrows(ValidationException.class, () -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoQtTotalVendido", pedido, null, 45, 50);
		});
		//--
		assertEquals(Messages.ITEMPEDIDO_MSG_QTITENS_ACIMA_PERMITIDO_BONIFICACAOTROCA_FECHARPEDIDO, ex.getMessage(), "A soma dos itens do pedido atual é de 48 (8 + 16 + 24). O terceiro item é desconsiderado, portanto 24 itens válidos."
				+ "O limite de bonificação/troca desse pedido é de 45% de 50 qtd do pedido original, ou seja um limite de 22 qtd");
	}
	
	@Test
	public void deveValidarAQuantidadeMaxDoItemBonificacaoOuTrocaEmRelacaoAoPercentualMaximoPermitidoDoItemOriginal() {
		ItemPedidoService itemPedidoService = ItemPedidoService.getInstance();
		LavenderePdaConfig.usaQtdItemPedidoInteiro = ValueUtil.VALOR_NAO;
		Pedido pedido = new Pedido();
		
		ItemPedido itemPedidoOriginal = new ItemPedido();
		itemPedidoOriginal.qtItemFisico = 88;
		itemPedidoOriginal.nuPedido = "ERP";
		itemPedidoOriginal.flOrigemPedido = "E";
		itemPedidoOriginal.flTipoItemPedido = "V";
		
		ValidationException ex = assertThrows(ValidationException.class, () -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoItemOriginal", pedido, itemPedidoOriginal, 55, 50);
		});
		String expectedMessage = MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_QTITEM_ACIMA_PERMITIDO_BONIFICACAOTROCA, 48);
		assertEquals(expectedMessage, ex.getMessage(), "O mesmo item no pedido original foi vendido à 88 unidades, onde 55% desse valor é permitido realizar a bonificação/troca."
				+ "Logo o limite de 48 unidades foi ultrapassado, informado 50 unidades no item de venda atual");
		
		//--
		assertDoesNotThrow(() -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoItemOriginal", pedido, itemPedidoOriginal, 55, 48);
		}, "Não deve gerar Exception pois foi bonificado/trocado item dentro do limite em relação ao item original");
		
		//--
		pedido.onFechamentoPedido = true;
		ex = assertThrows(ValidationException.class, () -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoItemOriginal", pedido, itemPedidoOriginal, 100, 89);
		});
		assertEquals(Messages.ITEMPEDIDO_MSG_QTITEM_ACIMA_PERMITIDO_BONIFICACAOTROCA_FECHARPEDIDO, ex.getMessage(), "O mesmo item no pedido original foi vendido à 88 unidades, onde 100% desse valor é permitido realizar a bonificação/troca."
				+ "Logo o limite de 88 unidades foi ultrapassado, informado 89 unidades no item de venda atual");
		
		//--
		assertDoesNotThrow(() -> {
			MethodTestUtils.invokePrivateMethod(itemPedidoService, "validaQtItemFisicoMaxBonifTrocaEmRelacaoItemOriginal", pedido, null, 55, 48);
		}, "Não deve gerar Exception pois não foi informado o item original");
		
	}
	
	private Pedido getPedidoComItens() {
		Pedido pedido = new Pedido();
		for (int i = 1; i <= 4; i++) {
			ItemPedido itemPedido = new ItemPedido();
			itemPedido.cdEmpresa = "1";
			itemPedido.cdRepresentante = "10";
			itemPedido.nuPedido = "WWW";
			itemPedido.flOrigemPedido = "P";
			itemPedido.nuSeqProduto = 1;
			itemPedido.flTipoItemPedido = "B";
			itemPedido.cdProduto = i + "";
			itemPedido.qtItemFisico = i * 8;
			itemPedido.flQuantidadeLiberada = i == 3 ? ValueUtil.VALOR_SIM : null;
			itemPedido.pedido = pedido;
			pedido.itemPedidoList.addElement(itemPedido);
		}
		return pedido;
	}
	
	private ItemPedido createNewItemPedidoDeflatorCondPagto(String flFormulaCalculo, int qtDiasCarenciaJuros, double vlPctJurosMensal) {
		Produto produto = new Produto();
		produto.dsProduto = "1";
		produto.flFormulaCalculoDeflator = flFormulaCalculo;
		produto.qtDiasCarenciaJuros = qtDiasCarenciaJuros;
		produto.vlPctJurosMensal = vlPctJurosMensal;
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.vlBaseItemPedido = 315;
		itemPedido.setProduto(produto);
		return itemPedido;
	}
	
	private CondicaoPagamento createNewCondicaoPagtoDeflator(int qtDiasMedioPagamento) {
		CondicaoPagamento condPagto = new CondicaoPagamento();
		condPagto.cdCondicaoPagamento = "1";
		condPagto.dsCondicaoPagamento = "W";
		condPagto.cdEmpresa = "1";
		condPagto.qtDiasMediosPagamento = qtDiasMedioPagamento;
		
		return condPagto;
	}

	private ItemPedido getItemPedidoComOrdemCompra(int qt, String cdProduto, String nuOrdemCompraCliente, int nuSeqOrdemCompraCliente) {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdProduto = cdProduto;
		itemPedido.qtItemFisico = qt;
		itemPedido.nuOrdemCompraCliente = nuOrdemCompraCliente;
		itemPedido.nuSeqOrdemCompraCliente = nuSeqOrdemCompraCliente;
		Produto produto = new Produto();
		produto.cdProduto = cdProduto;
		produto.dsProduto = "Produto Teste";
		itemPedido.setProduto(produto);
		return itemPedido;
	}

	@Test
	public void deveValidarNuSeqEOrdemCompraClienteDuplicado() {
		Exception e = assertThrows(ValidationException.class, () -> {
			LavenderePdaConfig.configNuOrdemCompraClienteNoPedido = "{\"usa\":\"2\", \"usaNuOrdemCompraENuSeqClienteItemPedido\":\"S\"}";
			Pedido pedido = new Pedido();
			pedido.itemPedidoList.addElement(getItemPedidoComOrdemCompra(1, "1", "1", 1));
			MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "validateNuOrdemCompraAndNuSequencialOrdemCompra", getItemPedidoComOrdemCompra(1, "2", "1", 1), pedido, false);
		}, "Uma ValidationException era esperada.");
		assertEquals("O produto 1 - Produto Teste já foi adicionado ao pedido com o mesmo sequencial e ordem de compra. Ordem: 1 e Sequencial: 1", e.getMessage(), "Mensagem de erro não correspondente.");
	}

	@Test
	public void deveValidarProdutoDuplicadoMesmaOrdemCompra() {
		Exception e = assertThrows(ValidationException.class, () -> {
			LavenderePdaConfig.configNuOrdemCompraClienteNoPedido = "{\"usa\":\"2\", \"usaNuOrdemCompraENuSeqClienteItemPedido\":\"S\"}";
			Pedido pedido = new Pedido();
			pedido.itemPedidoList.addElement(getItemPedidoComOrdemCompra(1, "1", "1", 1));
			MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "validateNuOrdemCompraAndNuSequencialOrdemCompra", getItemPedidoComOrdemCompra(1, "1", "1", 2), pedido, false);
		}, "Uma ValidationException era esperada.");
		assertEquals("O produto 1 - Produto Teste já existe no pedido com esta ordem de compra (1). É necessário adicionar uma ordem de compra diferente", e.getMessage(), "Mensagem de erro não correspondente.");
	}

	@Test
	public void deveValidarNuSeqDuplicadoSemOrdemCompraInformada() {
		Exception e = assertThrows(ValidationException.class, () -> {
			LavenderePdaConfig.configNuOrdemCompraClienteNoPedido = "{\"usa\":\"2\", \"usaNuOrdemCompraENuSeqClienteItemPedido\":\"S\"}";
			Pedido pedido = new Pedido();
			pedido.itemPedidoList.addElement(getItemPedidoComOrdemCompra(1, "1", null, 1));
			MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "validateNuOrdemCompraAndNuSequencialOrdemCompra", getItemPedidoComOrdemCompra(1, "2", null, 1), pedido, false);
		}, "Uma ValidationException era esperada.");
		assertEquals("O Produto 1 - Produto Teste já foi adiconado ao pedido com o mesmo sequencial e sem ordem de compra informada. Informe uma ordem de compra ou altere o sequencial do item", e.getMessage(), "Mensagem de erro não correspondente.");
	}

	@Test
	public void deveValidarNuOrdemCompraAndNuSequencialOrdemCompraSemExceptions() {
		LavenderePdaConfig.configNuOrdemCompraClienteNoPedido = "{\"usa\":\"2\", \"usaNuOrdemCompraENuSeqClienteItemPedido\":\"S\"}";
		Pedido pedido = new Pedido();
		pedido.itemPedidoList.addElement(getItemPedidoComOrdemCompra(1, "1", null, 1));
		MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "validateNuOrdemCompraAndNuSequencialOrdemCompra", getItemPedidoComOrdemCompra(1, "1", "2", 1), pedido, false);
		assertTrue(true);
	}
	
	@Test
	public void deveRetornarNuConversaoUnidadePuDoItemQuandoValorDiferenteDeZeroEStatusPedidoDiferenteDeAberto() {
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
		itemPedido.pedido = pedido;
		itemPedido.nuConversaoUnidadePu = 2;
		assertEquals(itemPedido.nuConversaoUnidadePu, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getNuConversaoUnidade", itemPedido, null));
	}
	
	@Test
	public void deveRetornarNuConversaoUnidadeDoProdutoUnidadeQuandoStatusPedidoIgualAberto() {
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		itemPedido.pedido = pedido;
		produtoUnidade.nuConversaoUnidade = 1;
		assertEquals(produtoUnidade.nuConversaoUnidade, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getNuConversaoUnidade", itemPedido, produtoUnidade));
	}
	
	@Test
	public void deveRetornarNuConversaoUnidadeDoProdutoUnidadeQuandoNuConversaoUnidadePuItemIgualAZero() {
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
		itemPedido.pedido = pedido;
		itemPedido.nuConversaoUnidadePu = 0;
		produtoUnidade.nuConversaoUnidade = 4;
		assertEquals(produtoUnidade.nuConversaoUnidade, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getNuConversaoUnidade", itemPedido, produtoUnidade));
	}
	
	@Test
	public void deveRetornarUmQuandoNuConversaoUnidadeDoProdutoUnidadeIgualAZero() {
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
		itemPedido.pedido = pedido;
		itemPedido.nuConversaoUnidadePu = 0;
		produtoUnidade.nuConversaoUnidade = 0;
		assertEquals(1d, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getNuConversaoUnidade", itemPedido, produtoUnidade));
	}
	
	@Test
	public void deveRetornarFlDivideMultiplicaPuDoItemQuandoValorDiferenteDeNuloEStatusPedidoDiferenteDeAberto() {
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
		itemPedido.pedido = pedido;
		itemPedido.flDivideMultiplicaPu = ProdutoUnidade.FL_MULTIPLICA;
		assertEquals(itemPedido.flDivideMultiplicaPu, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getFlDivideMultiplica", itemPedido, null));
	}
	
	@Test
	public void deveRetornarFlDivideMultiplicaDoProdutoUnidadeQuandoStatusPedidoIgualAberto() {
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		itemPedido.pedido = pedido;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_DIVIDE;
		assertEquals(produtoUnidade.flDivideMultiplica, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getFlDivideMultiplica", itemPedido, produtoUnidade));
	}
	
	@Test
	public void deveRetornarFlDivideMultiplicaDoProdutoUnidadeQuandoFlDivideMultiplicaPuDoItemNulo() {
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoTransmitido;
		itemPedido.pedido = pedido;
		itemPedido.flDivideMultiplicaPu = null;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_DIVIDE;
		assertEquals(produtoUnidade.flDivideMultiplica, MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "getFlDivideMultiplica", itemPedido, produtoUnidade));
	}
	
	@Test
	public void deveRetornarNuConversaoUnidadesMedidaQuandoNuConversaoUnidadeIgualAUm() {
		double nuConversaoUnidade = 1;
		double nuConversaoUnidadeMedida = 2;
		assertEquals(nuConversaoUnidadeMedida, ItemPedidoService.getInstance().getQtdEmbalagemSelecionada(ProdutoUnidade.FL_MULTIPLICA, nuConversaoUnidadeMedida, nuConversaoUnidade));
	}
	
	@Test
	public void deveRetornarNuConversaoUnidadesMedidaQuandoNuConversaoUnidadeIgualAZeroEFlDivideMultiplicaIgualADivide() {
		double nuConversaoUnidade = 0;
		double nuConversaoUnidadeMedida = 6;
		assertEquals(nuConversaoUnidadeMedida, ItemPedidoService.getInstance().getQtdEmbalagemSelecionada(ProdutoUnidade.FL_DIVIDE, nuConversaoUnidadeMedida, nuConversaoUnidade));
	}
	
	@Test
	public void deveRetornarMultiplicacaoQuandoNuConversaoUnidadeDiferenteDeUmEFlDivideMultiplicaIgualAMultiplica() {
		double nuConversaoUnidade = 3;
		double nuConversaoUnidadeMedida = 4;
		assertEquals(ValueUtil.round(nuConversaoUnidadeMedida * nuConversaoUnidade), ItemPedidoService.getInstance().getQtdEmbalagemSelecionada(ProdutoUnidade.FL_MULTIPLICA, nuConversaoUnidadeMedida, nuConversaoUnidade));
	}
	
	@Test
	public void deveRetornarDivisaoQuandoNuConversaoUnidadeDiferenteDeUmEZeroEFlDivideMultiplicaIgualADivide() {
		double nuConversaoUnidade = 3;
		double nuConversaoUnidadeMedida = 5;
		assertEquals(ValueUtil.round(nuConversaoUnidadeMedida / nuConversaoUnidade), ItemPedidoService.getInstance().getQtdEmbalagemSelecionada(ProdutoUnidade.FL_DIVIDE, nuConversaoUnidadeMedida, nuConversaoUnidade));
	}
	
	@Test
	public void devePopularQtEmbalagemElementarDoItemComNuConversaoUnidadeQuandoNuConversaoUnidadesMedidaIgualAUmEFlDivideMultiplicaIgualADivide() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		Produto produto = new Produto();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		itemPedido.pedido = pedido;
		produto.nuConversaoUnidadesMedida = 1;
		itemPedido.setProduto(produto);
		produtoUnidade.nuConversaoUnidade = 3.5;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_DIVIDE;
		ItemPedidoService.getInstance().aplicaQtdElementarItemPedido(itemPedido, produtoUnidade);
		assertEquals(produtoUnidade.nuConversaoUnidade, itemPedido.qtEmbalagemElementar);
	}
	
	@Test
	public void devePopularVlEmbalagemElementarDoItemQuandoFlDivideMultiplicaIgualAMultiplica() throws SQLException {
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		Produto produto = new Produto();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		itemPedido.pedido = pedido;
		produto.nuConversaoUnidadesMedida = 2;
		itemPedido.setProduto(produto);
		produtoUnidade.nuConversaoUnidade = 3.5;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
		double vlBaseItemPedido = 14.85d;
		double qtEmbalagemElementar = ValueUtil.round(produto.nuConversaoUnidadesMedida * produtoUnidade.nuConversaoUnidade);
		double vlEmbalagemElementar = ValueUtil.round(vlBaseItemPedido * produtoUnidade.nuConversaoUnidade);
		MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "aplicaVlEmbalagemElementarItemPedido", produtoUnidade, vlBaseItemPedido, itemPedido);
		assertEquals(ValueUtil.round(vlEmbalagemElementar / qtEmbalagemElementar), itemPedido.vlEmbalagemElementar);
	}
	
	@Test
	public void devePopularVlEmbalagemElementarDoItemQuandoFlDivideMultiplicaIgualADivide() throws SQLException {
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Pedido pedido = new Pedido();
		Produto produto = new Produto();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
		itemPedido.pedido = pedido;
		produto.nuConversaoUnidadesMedida = 2;
		itemPedido.setProduto(produto);
		produtoUnidade.nuConversaoUnidade = 3.5;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_DIVIDE;
		double vlBaseItemPedido = 14.85d;
		double qtEmbalagemElementar = ValueUtil.round(produto.nuConversaoUnidadesMedida / produtoUnidade.nuConversaoUnidade);
		double vlEmbalagemElementar = ValueUtil.round(vlBaseItemPedido / produtoUnidade.nuConversaoUnidade);
		MethodTestUtils.invokePrivateMethod(ItemPedidoService.getInstance(), "aplicaVlEmbalagemElementarItemPedido", produtoUnidade, vlBaseItemPedido, itemPedido);
		assertEquals(ValueUtil.round(vlEmbalagemElementar / qtEmbalagemElementar), itemPedido.vlEmbalagemElementar);
	}
	
	@Test
	public void deveRetornarMultiplicacaoComVlEmbalagemElementarQuandoFlDivideMultiplicaIgualAMultiplica() throws SQLException {
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Produto produto = new Produto();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produto.nuConversaoUnidadesMedida = 2;
		itemPedido.setProduto(produto);
		produtoUnidade.nuConversaoUnidade = 3.5;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
		itemPedido.setProdutoUnidade(produtoUnidade);
		double vlEmbalagemElementar = 35.23d;
		double qtEmbalagemElementar = produto.nuConversaoUnidadesMedida * produtoUnidade.nuConversaoUnidade;
		assertEquals(vlEmbalagemElementar * qtEmbalagemElementar, ItemPedidoService.getInstance().calculaVlItemByVlElementar(itemPedido, vlEmbalagemElementar));
	}
	
	@Test
	public void deveRetornarDivisaoComVlEmbalagemElementarQuandoFlDivideMultiplicaIgualADivide() throws SQLException {
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Produto produto = new Produto();
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produto.nuConversaoUnidadesMedida = 2;
		itemPedido.setProduto(produto);
		produtoUnidade.nuConversaoUnidade = 3.5;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_DIVIDE;
		itemPedido.setProdutoUnidade(produtoUnidade);
		double vlEmbalagemElementar = 35.23d;
		double qtEmbalagemElementar = produto.nuConversaoUnidadesMedida / produtoUnidade.nuConversaoUnidade;
		assertEquals(vlEmbalagemElementar * qtEmbalagemElementar, ItemPedidoService.getInstance().calculaVlItemByVlElementar(itemPedido, vlEmbalagemElementar));
	}
	
	@Test
	public void deveRetornarMultiplicacaoDoVlEmbalagemElementarComNuConversaoUnidadesMedidaDoProdutoQuandoProdutoUnidadeNulo() throws SQLException {
		LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco = false;
		ItemPedido itemPedido = new ItemPedido();
		Produto produto = new Produto();
		produto.nuConversaoUnidadesMedida = 2;
		itemPedido.setProduto(produto);
		itemPedido.setProdutoUnidade(null);
		double vlEmbalagemElementar = 35.23d;
		assertEquals(vlEmbalagemElementar * produto.nuConversaoUnidadesMedida, ItemPedidoService.getInstance().calculaVlItemByVlElementar(itemPedido, vlEmbalagemElementar));
	}

}