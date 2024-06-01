package br.com.wmw.lavenderepda.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import br.com.wmw.framework.business.domain.DomainUtil;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.builder.PedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class PedidoConsignacaoServiceTest  {
	
	private ItemPedido itemPedido;
	private StringBuffer dsObservacao;
	private HashMap<String, ItemPedido> itemPedidoHash;
	private List<Pedido> pedidoList;

	@BeforeEach
	public void setUp() throws Exception {
		LavenderePdaConfig.cdStatusPedidoConsignado = "5";
		LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao = true;
		LavenderePdaConfig.usaAgrupamentoPedidoEmConsignacao = true;
		//--
		dsObservacao = new StringBuffer(64);
		itemPedidoHash = new HashMap<String, ItemPedido>();
		pedidoList = new ArrayList<Pedido>();
		//--
		Pedido pedido1 = new PedidoBuilder("1", "1", "1", "P").build();
		pedido1.cdCliente = "1";
		pedido1.cdStatusPedido  = "5";
		pedido1.dtConsignacao = DateUtil.getCurrentDate();
		pedido1.itemPedidoList = new Vector();
		pedido1.getHashValuesDinamicos().put(Pedido.NMCOLUNA_DSOBSERVACAO, "obs pedido 1");
		for (int i = 0; i < 3; i++) {
			itemPedido = new ItemPedidoBuilder("1", "1", "1", "P").build();
			itemPedido.nuPedido = "1";
			itemPedido.cdProduto = (i + 1) + "";
			itemPedido.flTipoItemPedido = "V";
			itemPedido.setQtItemFisico((i+1) * 5);
			itemPedido.vlItemPedido = i == 1 ? 9 : 10 * (i+1);
			itemPedido.pedido = pedido1;
			pedido1.itemPedidoList.addElement(itemPedido);
		}
		//--
		Pedido pedido2 = new PedidoBuilder("1", "1", "2", "P").build();
		pedido2.cdCliente = "1";
		pedido2.cdStatusPedido  = "5";
		pedido2.dtConsignacao = DateUtil.getCurrentDate();
		pedido2.itemPedidoList = new Vector();
		pedido2.setHashValuesDinamicos(new Hashtable(1));
		for (int i = 0; i < 3; i++) {
			itemPedido = new ItemPedidoBuilder("1", "1", "1", "P").build();
			itemPedido.nuPedido = "2";
			itemPedido.cdProduto = (i + 2) + "";
			itemPedido.flTipoItemPedido = "V";
			itemPedido.setQtItemFisico((i+1) * 5);
			itemPedido.vlItemPedido = 10 * (i+1);
			if (i + 2 == 4) {
				PedidoConsignacao pedCon = new PedidoConsignacao();
				pedCon.copyPropertiesfromItem(itemPedido);
				pedCon.copyPropertiesfromPedido(pedido2);
				pedCon.nuSeqConsignacao = 50;
				pedCon.flTipoRegistro = PedidoConsignacao.TIPO_REGISTRO_DEVOLUCAO; 
				pedCon.qtItemFisico = 3;
				pedCon.vlTotalItemPedido = ValueUtil.round(pedCon.qtItemFisico * itemPedido.vlItemPedido);
				pedCon.vlPctDesconto = 0;
				pedCon.vlPctAcrescimo = 0;
				pedido2.pedidoConsignacaoDevolucaoList = new Vector(1);
				pedido2.pedidoConsignacaoDevolucaoList.addElement(pedCon);
			}
			itemPedido.pedido = pedido2;
			pedido2.itemPedidoList.addElement(itemPedido);
		}
		//--
		pedidoList.add(pedido1);
		pedidoList.add(pedido2);
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testMontaItensDoPedidoConsignacaoAgrupado() {
		PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		//--
		assertEquals(itemPedidoHash.size(), 4, 1E-8);
		assertEquals("obs pedido 1; ", dsObservacao.toString());
		//--
		List<ItemPedido> itemPedidoList = new ArrayList<ItemPedido>();
		for (ItemPedido item : itemPedidoHash.values()) {
			itemPedidoList.add(item);
		}
		for (int i = 0; i < 4; i++) {
			ItemPedido item = itemPedidoList.get(i);
			assertEquals((i + 1) + "", item.cdProduto);
			if ("1".equals(item.cdProduto)) {
				assertEquals(5, item.getQtItemFisico(), 1E-8);
				assertEquals(10, item.vlItemPedido, 1E-8);
			} else if ("2".equals(item.cdProduto)) {
				assertEquals(15, item.getQtItemFisico(), 1E-8);
				assertEquals(9, item.vlItemPedido, 1E-8);
			} else if ("3".equals(item.cdProduto)) {
				assertEquals(25, item.getQtItemFisico(), 1E-8);
				assertEquals(20, item.vlItemPedido, 1E-8);
			} else if ("4".equals(item.cdProduto)) {
				assertEquals(15, item.getQtItemFisico(), 1E-8);
				assertEquals(30, item.vlItemPedido, 1E-8);
				assertEquals(3, item.qtDevolvida, 1E-8);
			}
		}
	}
	
	@Test
	public void testAgrupamentoPedidosStatusDosPedidos() {
		LavenderePdaConfig.cdStatusPedidoConsignado = "8";
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_STATUS_INVALIDO, e.getMessage());
		}
		LavenderePdaConfig.cdStatusPedidoConsignado = "5";
	}
	
	@Test
	public void testAgrupamentoPedidosDataVencimento() {
		Pedido pedido = pedidoList.get(0);
		pedido.dtConsignacao = DateUtil.getCurrentDate();
		pedido.dtConsignacao.advance(-50);
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals("Erro ao agrupar pedidos. A consignação do pedido 1 está vencida!", e.getMessage());
		}
	}
	
	@Test
	public void testAgrupamentoValidacoesCaracteristicasPedidos() {
		Pedido pedido = pedidoList.get(0);
		//--
		pedido.cdCliente = "123";
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_CLIENTE, e.getMessage());
		}
		pedido.cdCliente = "1";
		//--
		pedido.cdTipoPedido = "1";
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_TIPO_PEDIDO, e.getMessage());
		}
		pedido.cdTipoPedido = null;
		//--
		pedido.cdTipoPagamento = "1";
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_TIPO_PAGAMENTO, e.getMessage());
		}
		pedido.cdTipoPagamento = null;
		//--
		pedido.cdCondicaoComercial = "1";
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_CONDICAO_COMERCIAL, e.getMessage());
		}
		pedido.cdCondicaoComercial = null;
		//--
		pedido.cdCondicaoPagamento = "1";
		try {
			PedidoConsignacaoService.getInstance().montaItensDoPedidoConsignacaoAgrupado(pedidoList, null, dsObservacao, itemPedidoHash);
		} catch (ValidationException e) {
			assertEquals(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_CONDICAO_PAGAMENTO, e.getMessage());
		}
		pedido.cdCondicaoPagamento = null;
	}

}
