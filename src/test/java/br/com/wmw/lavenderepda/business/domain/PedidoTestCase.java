package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.DateUtil;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import org.junit.jupiter.api.Test;
import totalcross.util.Vector;

public class PedidoTestCase  {

	@Test
	public void testIsAtingiuPesoMinimo() throws SQLException {
		LavenderePdaConfig.configCalculoPesoPedido = "{\"formulaCalculo\":\"1\", \"mostraVPesoCapaPedido\":\"N\", \"mostraVPesoListaItens\": \"N\"}";
		Pedido pedido = new Pedido("");
		pedido.tipoFrete = new TipoFrete();
		pedido.getTipoFrete().vlPesoMinimo = 10;
		pedido.qtPeso = 5;
		assertEquals(false, pedido.isAtingiuPesoMinimo());
		//
		pedido.qtPeso = 12;
		assertEquals(true, pedido.isAtingiuPesoMinimo());
	}

	@Test
	public void testIsPercentualDevolucaoClienteUltrapassada() throws SQLException {
		Pedido pedido = new Pedido();
		pedido.setCliente(new Cliente());
		//--
		pedido.getCliente().vlPctDevolucaoConsig = 0;
		pedido.vlPedidoOriginal = 0;
		assertTrue(pedido.isPercentualDevolucaoClienteUltrapassada(0));
		//--
		pedido.getCliente().vlPctDevolucaoConsig = 10;
		assertFalse(pedido.isPercentualDevolucaoClienteUltrapassada(0));
		//--
		pedido.vlPedidoOriginal = 100;
		assertFalse(pedido.isPercentualDevolucaoClienteUltrapassada(10));
		//--
		assertTrue(pedido.isPercentualDevolucaoClienteUltrapassada(20));
	}

	@Test
	public void testIsPedidoConsignadoVencido() {
		Pedido pedido = new Pedido();
		//--
		LavenderePdaConfig.nuDiasValidadePedidoEmConsignacao = "N";
		assertFalse(pedido.isPedidoConsignadoVencido());
		//--
		LavenderePdaConfig.nuDiasValidadePedidoEmConsignacao = "S";
		assertFalse(pedido.isPedidoConsignadoVencido());
		//--
		pedido.dtConsignacao = DateUtil.getCurrentDate();
		assertFalse(pedido.isPedidoConsignadoVencido());
		//--
		DateUtil.decDay(pedido.dtConsignacao, 181);
		assertTrue(pedido.isPedidoConsignadoVencido());
		//--
		LavenderePdaConfig.nuDiasValidadePedidoEmConsignacao = "10";
		pedido.dtConsignacao = DateUtil.getCurrentDate();
		DateUtil.decDay(pedido.dtConsignacao, 10);
		assertFalse(pedido.isPedidoConsignadoVencido());
		//--
		DateUtil.decDay(pedido.dtConsignacao, 1);
		assertTrue(pedido.isPedidoConsignadoVencido());
		
	}

	@Test
	public void testAgrSimilaresShouldNotBeNull() {
		Pedido pedido = new Pedido();
		assertNotNull(pedido.getItemPedidoAgrSimilares());
	}

	@Test
	public void testAgrSimilaresListInstanceShoudBeTheSame() {
		Pedido pedido = new Pedido();
		Vector firstCall = pedido.getItemPedidoAgrSimilares(),
			   secondCall = pedido.getItemPedidoAgrSimilares();
		
		assertSame(firstCall, secondCall);
	}
	
}
