package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteServiceTestCase  {

	@Test
	public void testSucessoDescontosBloqueados() {
		Cliente cliente = new Cliente();
		cliente.dsDescontoBloqueadoList = "1|2|3";

		assertTrue(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_MANUAL));
		assertTrue(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL));
		assertTrue(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_QNT_ITEM));

	}

	@Test
	public void testSucessoDescontosBloqueadosApenasManual() {
		Cliente cliente = new Cliente();
		cliente.dsDescontoBloqueadoList = "1";

		assertTrue(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_MANUAL));
		assertFalse(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL));
		assertFalse(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_QNT_ITEM));

	}

	@Test
	public void testFalhaDescontosBloqueados() {
		Cliente cliente = new Cliente();
		cliente.dsDescontoBloqueadoList = "4";

		assertFalse(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_MANUAL));
		assertFalse(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_PROMOCIONAL));
		assertFalse(ClienteService.getInstance().isDescontoBloqueado(cliente, Cliente.DESCONTO_BLOQUEADO_QNT_ITEM));

	}

	@Test
	public void testSucessoAcescimoBloqueados() {
		Cliente cliente = new Cliente();
		cliente.dsAcrescimoBloqueadoList = "1";

		assertTrue(ClienteService.getInstance().isAcrescimoBloqueado(cliente, Cliente.ACRESCIMO_BLOQUEADO_MANUAL));

	}

	@Test
	public void testFalhaAcrescimoBloqueados() {
		Cliente cliente = new Cliente();
		cliente.dsAcrescimoBloqueadoList = "4";

		assertFalse(ClienteService.getInstance().isAcrescimoBloqueado(cliente, Cliente.ACRESCIMO_BLOQUEADO_MANUAL));

	}
	
	@Test
	public void naoDevePermitirProdutoControladoClienteSemAlvara() {
		Cliente cliente = new Cliente();
		cliente.dtVencimentoAlvara = null;
		LavenderePdaConfig.configProdutoControlado = "{\"bloqueiaClienteSemAlvara\":\"S\", \"bloqueiaClienteSemLicenca\":\"N\"}";
		ItemPedido itemPedido = createItemPedidoComProdutoControlado();
		ClienteService clienteService = ClienteService.getInstance();
		ValidationException validationException = assertThrows(ValidationException.class, () -> clienteService.validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, cliente));
		String expectedMessage = MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_CONTROLADO, Messages.PEDIDO_LABEL_STATUSALVARA_NAO);
		assertEquals(expectedMessage, validationException.getMessage());
	}
	
	@Test
	public void naoDevePermitirProdutoControladoClienteAlvaraVencido() {
		Cliente cliente = new Cliente();
		cliente.dtVencimentoAlvara = DateUtil.toDate("2021-01-01");
		LavenderePdaConfig.configProdutoControlado = "{\"bloqueiaClienteSemAlvara\":\"S\", \"bloqueiaClienteSemLicenca\":\"N\"}";
		ItemPedido itemPedido = createItemPedidoComProdutoControlado();
		ClienteService clienteService = ClienteService.getInstance();
		ValidationException validationException = assertThrows(ValidationException.class, () -> clienteService.validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, cliente));
		String expectedMessage = MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_CONTROLADO, cliente.dtVencimentoAlvara.toString());
		assertEquals(expectedMessage, validationException.getMessage());
	}
	
	@Test
	public void naoDevePermitirProdutoControladoClienteSemLicenca() {
		Cliente cliente = new Cliente();
		cliente.nuLicencaProdutoControlado = null;
		LavenderePdaConfig.configProdutoControlado = "{\"bloqueiaClienteSemAlvara\":\"N\", \"bloqueiaClienteSemLicenca\":\"S\"}";
		ItemPedido itemPedido = createItemPedidoComProdutoControlado();
		ClienteService clienteService = ClienteService.getInstance();
		ValidationException validationException = assertThrows(ValidationException.class, () -> {
			clienteService.validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, cliente);
		});
		String expectedMessage = Messages.CLIENTE_MSG_SEM_LICENCA_PRODUTO_CONTROLADO;
		assertEquals(expectedMessage, validationException.getMessage());
	}
	
	@Test
	public void devePermitirProdutoNaoControladoClienteSemLicencaSemAlvara(){
		Cliente cliente = new Cliente();
		cliente.nuLicencaProdutoControlado = null;
		cliente.dtVencimentoAlvara = null;
		LavenderePdaConfig.configProdutoControlado = "{\"bloqueiaClienteSemAlvara\":\"S\", \"bloqueiaClienteSemLicenca\":\"S\"}";
		ItemPedido itemPedido = createItemPedidoComProduto();
		ClienteService clienteService = ClienteService.getInstance();
		assertDoesNotThrow(() -> clienteService.validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, cliente));
	}
	
	@Test
	public void devePermitirProdutoControladoClienteComLicenca(){
		Cliente cliente = new Cliente();
		cliente.nuLicencaProdutoControlado = "W";
		cliente.dtVencimentoAlvara = null;
		LavenderePdaConfig.configProdutoControlado = "{\"bloqueiaClienteSemAlvara\":\"N\", \"bloqueiaClienteSemLicenca\":\"S\"}";
		ItemPedido itemPedido = createItemPedidoComProdutoControlado();
		ClienteService clienteService = ClienteService.getInstance();
		assertDoesNotThrow(() -> clienteService.validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, cliente));
	}
	
	@Test
	public void devePermitirProdutoControladoClienteComAlvara(){
		Cliente cliente = new Cliente();
		cliente.nuLicencaProdutoControlado = null;
		cliente.dtVencimentoAlvara = DateUtil.getCurrentDate();
		LavenderePdaConfig.configProdutoControlado = "{\"bloqueiaClienteSemAlvara\":\"S\", \"bloqueiaClienteSemLicenca\":\"N\"}";
		ItemPedido itemPedido = createItemPedidoComProdutoControlado();
		ClienteService clienteService = ClienteService.getInstance();
		assertDoesNotThrow(() -> clienteService.validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, cliente));
	}
	
	private ItemPedido createItemPedidoComProdutoControlado() {
		Produto prod = new Produto();
		prod.cdProduto="1";
		prod.dsProduto = "1";
		prod.flProdutoControlado = ValueUtil.VALOR_SIM;
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.pedido = new Pedido();
		itemPedido.cdProduto = "1";
		itemPedido.setProduto(prod);
		return itemPedido;
	}
	
	private ItemPedido createItemPedidoComProduto() {
		Produto prod = new Produto();
		prod.cdProduto = "1";
		prod.dsProduto = "1";
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.pedido = new Pedido();
		itemPedido.cdProduto = "1";
		itemPedido.setProduto(prod);
		return itemPedido;
	}
	
}
