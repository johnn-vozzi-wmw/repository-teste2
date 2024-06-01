package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.builder.PedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.*;
import br.com.wmw.test.method.MethodTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totalcross.util.Vector;

public class VerbaServiceTest  {

	ItemPedido itemPedido;
	Pedido pedido;
	Vector descontoGrupoList;
	
	@BeforeEach
	public void setUp() throws Exception {
		itemPedido = new ItemPedidoBuilder("1", "1", "1").comTabelaPreco().build();
		pedido = new PedidoBuilder(itemPedido).build();
		itemPedido.pedido = pedido;
		itemPedido.setProduto(new Produto());
		itemPedido.setProdutoUnidade(new ProdutoUnidade());
		itemPedido.vlUnidadePadrao = 2.00;
		
		DescontoGrupo descontoGrupo1 = new DescontoGrupo();
		descontoGrupo1.vlPctDesconto = 8;
		descontoGrupo1.qtItem = 5;
		
		DescontoGrupo descontoGrupo2 = new DescontoGrupo();
		descontoGrupo2.vlPctDesconto = 12;
		descontoGrupo2.qtItem = 10;
		
		descontoGrupoList = new Vector();
		descontoGrupoList.addElement(descontoGrupo2);
		descontoGrupoList.addElement(descontoGrupo1);
	}

	@AfterEach
	public void tearDown() throws Exception {
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		LavenderePdaConfig.usaUnidadeAlternativa = false;
	}

	@Test
	public void testGetVlBaseVerba() throws SQLException {
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = true;
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		
		itemPedido.setQtItemFisico(2);
		assertEquals(2.0, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(3);
		assertEquals(2.0, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(5);
		assertEquals(1.84, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(6);
		assertEquals(1.84, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(9);
		assertEquals(1.84, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(10);
		assertEquals(1.76, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(27);
		assertEquals(1.76, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);

		
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		
		itemPedido.setQtItemFisico(2);
		assertEquals(2.0, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(itemPedido.vlUnidadePadrao, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(3);
		assertEquals(22.3, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(22.3, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(5);
		assertEquals(18.40, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(20.0, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(6);
		assertEquals(18.40, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(20.0, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(9);
		assertEquals(21.62, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(23.5, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(10);
		assertEquals(15.84, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(18.0, itemPedido, descontoGrupoList), 0.00001);
		//--
		itemPedido.setQtItemFisico(27);
		assertEquals(15.84, VerbaService.getInstance().getVlBaseFlexBaseadoFaixaDescontoGrupo(18.0, itemPedido, descontoGrupoList), 0.00001);
		//--
	}

	@Test
	public void deveRetornarTrueSeDataVigenciaVazia() {
		VerbaSaldo verbaSaldo = new VerbaSaldo();
		boolean result = MethodTestUtils.invokePrivateMethod(VerbaSaldoService.getInstance(), "isDataVigenciaVazia", verbaSaldo);
		assertTrue(result);
	}
	
	@Test
	public void deveAplicarVerbaBonifCfgCorretamente() {
		itemPedido.setQtItemFisico(5d);
		itemPedido.vlItemPedido = 10d;
		ItemPedidoBonifCfg itemBonif = new ItemPedidoBonifCfg(itemPedido);
		itemBonif.qtBonificacao = 4d;
		VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedido, itemBonif);
		assertEquals(-10d, itemPedido.vlVerbaItem);
		//--
		itemPedido.vlTotalItemPedido = 10d;
		itemBonif.qtBonificacao = 0d;
		itemBonif.vlBonificacao = 3d;
		VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedido, itemBonif);
		assertEquals(-7d, itemPedido.vlVerbaItem);
		//--
		itemPedido.setQtItemFisico(4d);
		itemPedido.vlItemPedido = 10d;
		itemBonif.qtBonificacao = 5d;
		VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedido, itemBonif);
		assertEquals(0d, Math.abs(itemPedido.vlVerbaItem));
		//--
		itemPedido.vlTotalItemPedido = 10d;
		itemBonif.qtBonificacao = 0d;
		itemBonif.vlBonificacao = 15d;
		VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedido, itemBonif);
		assertEquals(0d, Math.abs(itemPedido.vlVerbaItem));
	}

}
