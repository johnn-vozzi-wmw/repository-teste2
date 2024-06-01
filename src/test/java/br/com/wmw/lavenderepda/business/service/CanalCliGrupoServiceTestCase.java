package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import org.junit.jupiter.api.Test;

public class CanalCliGrupoServiceTestCase  {
	@Test
	public void testCalculaDescontoCanaleContratoItemPedido() {
		Cliente cliente = new Cliente();
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.vlBaseItemTabelaPreco = 100;
		double descMaxCanal = 0;
		double descCanalEContrato = 0;
		LavenderePdaConfig.vlPctLimiteSomaCanalContratoDecisaoCalculo = 20.000000001;
		LavenderePdaConfig.vlPctLimiteContratoDecisaoCalculoDesc = 8.000000001;
		
		//exemplo aplicando a formula com 20% de canal e 5% de contrato
		cliente.vlPctContratoCli = 5;
		descMaxCanal = 20;
		descCanalEContrato = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, descMaxCanal, cliente.vlPctContratoCli);
		assertEquals(16, descCanalEContrato, 0.00001);
		
		
		//exemplo aplicando a formula com 11,11% de canal e 6% de contrato
		cliente.vlPctContratoCli = 6;
		descMaxCanal = 11.11;
		descCanalEContrato = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, descMaxCanal, cliente.vlPctContratoCli);
		assertEquals(11.11, descCanalEContrato, 0.00001);
		
		
		//exemplo aplicando a formula com 17% de canal e 8% de contrato
		cliente.vlPctContratoCli = 8;
		descMaxCanal = 17;
		descCanalEContrato = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, descMaxCanal, cliente.vlPctContratoCli);
		assertEquals(17, descCanalEContrato, 0.00001);
		
		
		//exemplo aplicando a formula com 11,11% de canal e 10% de contrato
		cliente.vlPctContratoCli = 10;
		descMaxCanal = 11.11;
		descCanalEContrato = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, descMaxCanal, cliente.vlPctContratoCli);
		assertEquals(11.11, descCanalEContrato, 0.00001);
		
		//exemplo aplicando a formula com 20% de canal e 00% de contrato
		cliente.vlPctContratoCli = 0;
		descMaxCanal = 20;
		descCanalEContrato = CanalCliGrupoService.getInstance().calculaDescontoMaximoCanalEContratoCliente(itemPedido, descMaxCanal, cliente.vlPctContratoCli);
		assertEquals(20, descCanalEContrato, 0.00001);
		
	}

}
