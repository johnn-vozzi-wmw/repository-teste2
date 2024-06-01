package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import org.junit.jupiter.api.Test;

public class IpiServiceTestCase  {

	@Test
	public void testCalculaIpiItemPedidoPersonalizado() throws SQLException {
		TributacaoConfig tributacaoConfig = new TributacaoConfig();
		ItemPedido itemPedido = new ItemPedido();
		Tributacao tributacao = new Tributacao();
		Cliente cliente = new Cliente();
		cliente.flDebitaPisCofinsZonaFranca = ValueUtil.VALOR_NAO;
		Produto produto = new Produto();
		produto.vlPctIpi = 6.00;
		produto.vlMinIpi = 0.42;
		LavenderePdaConfig.nuCasasDecimaisVlStVlIpi = 2;
		
		//Teste com flCalculaIpi = 'N'. 
		tributacaoConfig.flCalculaIpi = ValueUtil.VALOR_NAO;
		tributacaoConfig.flIpiBaseIcms = ValueUtil.VALOR_NAO;
		tributacaoConfig.flTipoCalculoPisCofins = "1";
		tributacao.vlPctIpi = 4.00;
		tributacao.vlMinIpi = 0.10;
		itemPedido.vlTotalItemPedido = 5.50;
		itemPedido.vlTotalItemPedidoFrete = 1.50;
		itemPedido.vlPis = 0.50;
		itemPedido.vlCofins = 0.50;
		itemPedido.pedido = new Pedido();
		itemPedido.pedido.setCliente(cliente);
		itemPedido.setProduto(produto);
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.0, itemPedido.vlIpiItem);
		
		//Teste com flCalculaIpi = 'S'.
		tributacaoConfig.flCalculaIpi = ValueUtil.VALOR_SIM;
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.22, itemPedido.vlIpiItem, 2);
		
		//Teste com flCalculaIpi = 'S' e valor de frete na base do Ipi . 
		tributacaoConfig.flFreteBaseIpi  = ValueUtil.VALOR_SIM;;
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.28, itemPedido.vlIpiItem, 2);
		
		//Teste com flCalculaIpi = 'S' e (valor de frete + valor de Pis + valor de Cofins na base do Ipi.
		tributacaoConfig.flTipoCalculoPisCofins = "2";
		cliente.flDebitaPisCofinsZonaFranca = ValueUtil.VALOR_SIM;
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.32, itemPedido.vlIpiItem, 2);
		
		//Teste com o vlMinIpi maior que o vlIpiItem calculado da Tributação
		tributacao.vlMinIpi = 0.35;
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, tributacao);
		assertEquals(0.35, itemPedido.vlIpiItem, 2);
		
		//Teste utilizando o vlPctIpi e vlMinIpi do Produto
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, null);
		assertEquals(0.48, itemPedido.vlIpiItem, 2);
		
		//Teste com o vlMinIpi maior que o vlIpiItem calculado do Produto
		produto.vlMinIpi = 0.56;
		IpiService.getInstance().calculaIpiItemPedidoPersonalizado(itemPedido, tributacaoConfig, null);
		assertEquals(0.56, itemPedido.vlIpiItem, 2);
		
		
		
		
		
		
	}

}
