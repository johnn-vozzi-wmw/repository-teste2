package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.test.method.MethodTestUtils;
import totalcross.util.Vector;

public class ItemPedidoBonifCfgServiceTestCase {

	public ItemPedidoBonifCfgService service;
	
	public ItemPedidoBonifCfgServiceTestCase() {
		service = ItemPedidoBonifCfgService.getInstance();
	}
	
	@Test
	public void deveBonificarTodoItemETerSaldoDeTrintaNaBonificacao() throws SQLException {
		ItemPedido itemPedido = createItemPedidoBonificacao(120);
		BonifCfg bonifCfg = createBonifCfg(true);
		ItemPedidoBonifCfg itemPedidoBonif = createItemPedidoBonifCfg(itemPedido, bonifCfg);
		MethodTestUtils.invokePrivateMethod(ItemPedidoBonifCfgService.getInstance(), "setQtBonificacaoConsumoBonifCfgFaixaQtde", itemPedido, itemPedidoBonif);
		
		assertEquals(90, itemPedidoBonif.qtBonificacao, "Deve retornar 90, pois o item de 100 unidades que já cobriu 10 qtds na regra anterior, faz com que nessa regra o consumo seja de 90 pra inteirar os 100" );
		assertEquals(100, itemPedido.qtBonificadoBonifCfg, "Deve retornar 100 pois ao passar pelo método, o item do pedido em questão teve toda sua qtItemFisico coberto pelo saldo disponivel da BonifCfg 1");
		assertEquals(30, itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.get("1;1;"), "Deve retornar 30 pois com o saldo de 120 inicial da bonificação, foi preciso retirar 90 para cobrir a qtd faltante para bonificacao do item");
		
	}
	
	@Test
	public void deveBonificarParcialItemEZerarSaldoNaBonificacao() throws SQLException {
		ItemPedido itemPedido = createItemPedidoBonificacao(40);
		BonifCfg bonifCfg = createBonifCfg(true);
		ItemPedidoBonifCfg itemPedidoBonif = createItemPedidoBonifCfg(itemPedido, bonifCfg);
		MethodTestUtils.invokePrivateMethod(ItemPedidoBonifCfgService.getInstance(), "setQtBonificacaoConsumoBonifCfgFaixaQtde", itemPedido, itemPedidoBonif);
		
		assertEquals(40, itemPedidoBonif.qtBonificacao, "Deve retornar 40, é o saldo total disponível para poder bonificar os (100-10) 90 que faltavam" );
		assertEquals(50, itemPedido.qtBonificadoBonifCfg, "Deve retornar 50 (10+40), pois foi a quantidade total de saldo disponível para cobrir a quantidade do Item");
		assertEquals(0, itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.get("1;1;"), "Deve retornar 0 pois consumiu todo o saldo da politica de bonificação 1 do pedido");
		
	}
	
	@Test
	public void deveRetornarQuatrocentosCinquentaSobreValorDeVenda() throws SQLException {
		ItemPedido itemPedido = createItemPedidoSimples("V");
		BonifCfg bonifCfg = createBonifCfg(false);
		ItemPedidoBonifCfg itemPedidoBonif = createItemPedidoBonifCfg(itemPedido, bonifCfg);
		double result = MethodTestUtils.invokePrivateMethod(ItemPedidoBonifCfgService.getInstance(), "getVlBonificacaoCalculado", itemPedido, itemPedidoBonif, bonifCfg);
		assertEquals(450, result, "Deve retornar 450, segundo a regra dos 15% da Política de Bonificação");
	}
	
	@Test
	public void deveRetornarDoisMilSobreValorDeVenda() throws SQLException {
		ItemPedido itemPedido = createItemPedidoSimples("B");
		BonifCfg bonifCfg = createBonifCfg(false);
		ItemPedidoBonifCfg itemPedidoBonif = createItemPedidoBonifCfg(itemPedido, bonifCfg);
		double result = MethodTestUtils.invokePrivateMethod(ItemPedidoBonifCfgService.getInstance(), "getVlBonificacaoCalculado", itemPedido, itemPedidoBonif, bonifCfg);
		assertEquals(2000, result, "Deve retornar 2000, dos 3000 do valor do item menos os 1000 já bonificado");
	}
	
	@Test
	public void deveProsseguirValidateItemPedidoBonifCfg() {
		ItemPedidoBonifCfg itemPedBon = new ItemPedidoBonifCfg(createItemPedido(false));
		itemPedBon.cdBonifCfg = "1";
		boolean gerouException = false;
		try {
			service.validate(itemPedBon);
		} catch (Exception e) {
			gerouException = true;
		}
		assertFalse(gerouException);
	}
	
	@Test
	public void naoDeveProsseguirValidateItemPedidoBonifCfgCasoHouverAtributoNulo() {
		List<ItemPedidoBonifCfg> itemPedBonList = new ArrayList<>();
		ItemPedido itemPedido = createItemPedido(false);
		ItemPedidoBonifCfg itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		itemPedBon.flTipoRegistro = "C";
		itemPedBon.cdEmpresa = null;
		itemPedBonList.add(itemPedBon);
		
		itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		itemPedBon.flTipoRegistro = "C";
		itemPedBon.cdRepresentante = null;
		itemPedBonList.add(itemPedBon);
		
		itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		itemPedBon.flTipoRegistro = "C";
		itemPedBon.flOrigemPedido = null;
		itemPedBonList.add(itemPedBon);
		
		itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		itemPedBon.flTipoRegistro = "C";
		itemPedBon.cdProduto = null;
		itemPedBonList.add(itemPedBon);
		
		itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		itemPedBon.flTipoRegistro = "C";
		itemPedBon.cdBonifCfg = null;
		itemPedBonList.add(itemPedBon);
		
		int exceptionCount = 0;
		for (ItemPedidoBonifCfg itemPedBonif : itemPedBonList) {
			try {
				service.validate(itemPedBonif);
			} catch (ValidationException e) {
				exceptionCount++;
			} catch (SQLException e) {
				exceptionCount += 100;
			}
		}
		assertEquals(exceptionCount, itemPedBonList.size());
	}
	
	@Test
	public void deveTerAtributosIguais() {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "2";
		itemPedido.flOrigemPedido = "P";
		itemPedido.nuPedido = "3";
		itemPedido.flTipoItemPedido = "V";
		itemPedido.cdProduto = "4";
		itemPedido.nuSeqProduto = 5;
		
		ItemPedidoBonifCfg itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		itemPedBon.flTipoRegistro = "C";
		assertEquals(itemPedido.getPrimaryKey() + ";", itemPedBon.getPrimaryKeyItemPedido());
	}
	
	@Test
	public void deveRetonarValorSessentaAoCalcularBonifCfgSobreItem() {
		ItemPedido itemPedido = createItemPedido(false);
		ItemPedidoBonifCfg itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		BonifCfg bonifCfg = new BonifCfg();
		bonifCfg.vlPctSobreVenda = 30;
		itemPedido.vlItemPedido = 100;
		itemPedido.setQtItemFisico(2);
		itemPedBon.flTipoRegistro = "C";
		double resultado = MethodTestUtils.invokePrivateMethod(service, "getVlBonificacaoCalculado", itemPedido, itemPedBon, bonifCfg);
		assertEquals(60d, resultado);
	}
	
	@Test
	public void deveRetonarValorQuarentaAoCalcularBonifCfgSobreItem() {
		ItemPedido itemPedido = createItemPedido(true);
		ItemPedidoBonifCfg itemPedBon = new ItemPedidoBonifCfg(itemPedido);
		BonifCfg bonifCfg = new BonifCfg();
		bonifCfg.vlPctSobreVenda = 10;
		itemPedido.vlItemPedido = 20;
		itemPedido.vlBonificado = 40;
		itemPedido.setQtItemFisico(4);
		itemPedBon.flTipoRegistro = "D";
		double resultado = MethodTestUtils.invokePrivateMethod(service, "getVlBonificacaoCalculado", itemPedido, itemPedBon, bonifCfg);
		assertEquals(40d, resultado);
	}
	
	@Test
	public void saldoBonificacaoDeveCobrirTotalmenteERestarQuatorzeDeSaldo() {
		BonifCfg bonifCfg = createBonifCfg(true);
		ItemPedido itemPedido = createItemPedido(true, "10", 10);
		ItemPedidoBonifCfg itemPedBonif = createItemPedidoBonifCfg(itemPedido, bonifCfg);
		Pedido pedido = createPedido();
		pedido.qtConsumirBonifCfgFaixaQtde = new HashMap<>();
		pedido.qtConsumirBonifCfgFaixaQtde.put(bonifCfg.getRowKey(), 20d);
		itemPedido.qtBonificadoBonifCfg = 4;
		itemPedido.pedido = pedido;
		
		MethodTestUtils.invokePrivateMethod(service, "setQtBonificacaoConsumoBonifCfgFaixaQtde", itemPedido, itemPedBonif);
		
		assertEquals(10, itemPedido.qtBonificadoBonifCfg, "Como havia saldo para consumo, o item tem esse valor cheio em relação a qtItemFisico, qtBonificadoBonifCfg = qtItemFisico => Item totalmente coberto pelo saldo de bonificação");
		assertEquals(6, itemPedBonif.qtBonificacao, "O itemPedido ja havia sido contemplado parcialmente com 4 de bonificação e restavam apenas 6 para completar os 10 da quantidade do item");
		assertEquals(14, pedido.qtConsumirBonifCfgFaixaQtde.get(bonifCfg.getRowKey()), "Como restavam apenas 6 para contemplar totalmente o item e havia saldo nessa regra, foi consumido 6 dos 20 que havia de saldo");
	}
	
	@Test
	public void saldoBonificacaoDeveCobrirParcialmenteQuantidadeDoItemENaoRestarSaldo() {
		BonifCfg bonifCfg = createBonifCfg(true);
		ItemPedido itemPedido = createItemPedido(true, "10", 20);
		ItemPedidoBonifCfg itemPedBonif = createItemPedidoBonifCfg(itemPedido, bonifCfg);
		Pedido pedido = createPedido();
		pedido.qtConsumirBonifCfgFaixaQtde = new HashMap<>();
		pedido.qtConsumirBonifCfgFaixaQtde.put(bonifCfg.getRowKey(), 15d);
		itemPedido.qtBonificadoBonifCfg = 4;
		itemPedido.pedido = pedido;
		
		MethodTestUtils.invokePrivateMethod(service, "setQtBonificacaoConsumoBonifCfgFaixaQtde", itemPedido, itemPedBonif);
		
		assertEquals(19, itemPedido.qtBonificadoBonifCfg, "Como não havia saldo para consumir na politica de bonificação, o item tem esse valor menor que o qtItemFisico, pois foi até onde o saldo conseguiu cobrir");
		assertEquals(15, itemPedBonif.qtBonificacao, "Restavam 16 unidades para cobrir o item, mas como havia somente 15 de saldo, esses 15 foram possíveis bonificar usando o saldo de bonificação");
		assertEquals(0, pedido.qtConsumirBonifCfgFaixaQtde.get(bonifCfg.getRowKey()), "Como precisavam de 16 unidades para cobrir o item e havia somente 15 de saldo, foi debitado 15 dos 15 que haviam. O saldo nunca será negativo.");
	}
	
	@Test
	public void deveValidarNovaQtdItemPedidoEmRelacaoQtBrindeEQtBonificacaoAuto() {
		ItemPedido itemPedido = createItemPedido(true);
		double newQtItemFisico = 20;
		Double[] qtTotalMinPermitido = new Double[] {13d, 11d};
		
		assertThrows(ValidationException.class, () -> {
			service.validaEdicaoQtItemFisicoItemBonificadoAutomaticamente(itemPedido, newQtItemFisico, qtTotalMinPermitido);
		}, "Deve gerar exception quando o valor comparado for menor que a soma do minimo de brinde e bonificações automáticas");
		
		qtTotalMinPermitido[0] = null;
		assertDoesNotThrow(() -> {
			service.validaEdicaoQtItemFisicoItemBonificadoAutomaticamente(itemPedido, newQtItemFisico, qtTotalMinPermitido);
		}, "Não deve gerar exception quando o valor comparado for maior que a soma do minimo de brinde e bonificações automáticas");
		
		qtTotalMinPermitido[0] = 22d;
		qtTotalMinPermitido[1] = null;
		assertThrows(ValidationException.class, () -> {
			service.validaEdicaoQtItemFisicoItemBonificadoAutomaticamente(itemPedido, newQtItemFisico, qtTotalMinPermitido);
		}, "Deve gerar exception quando o valor comparado for menor que a soma do minimo de brinde e bonificações automáticas");
	}
	
	
	private ItemPedido createItemPedido(boolean isBonificacao) {
		return createItemPedido(isBonificacao, "4", 6);
	}
	
	private ItemPedido createItemPedido(boolean isBonificacao, String cdProduto, double qtItemFisico) {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "2";
		itemPedido.flOrigemPedido = "P";
		itemPedido.nuPedido = "3";
		itemPedido.flTipoItemPedido = isBonificacao ? TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO : TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.cdProduto = cdProduto;
		itemPedido.nuSeqProduto = 5;
		itemPedido.qtItemFisico = qtItemFisico;
		Produto produto = new Produto();
		produto.cdProduto = cdProduto;
		produto.dsProduto = "Produto WMW";
		itemPedido.setProduto(produto);
		return itemPedido;
	}
	
	private ItemPedido createItemPedidoSimples(String flTipoItemPedido) {
		Pedido pedido = new Pedido();
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "1";
		itemPedido.nuPedido = "1";
		itemPedido.flOrigemPedido = "P";
		itemPedido.flTipoItemPedido = flTipoItemPedido;
		itemPedido.cdProduto = "1";
		itemPedido.nuSeqProduto = 1;
		itemPedido.setQtItemFisico(100);
		itemPedido.pedido = pedido;
		itemPedido.vlItemPedido = 30;
		itemPedido.vlBonificado = 1000;
		
		return itemPedido;
	}
	
	private ItemPedido createItemPedidoBonificacao(double saldoBonificaoBonifCfg) {
		Pedido pedido = new Pedido();
		pedido.qtConsumirBonifCfgFaixaQtde = new HashMap<String, Double>();
		pedido.qtConsumirBonifCfgFaixaQtde.put("1;1;", saldoBonificaoBonifCfg);
		
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "1";
		itemPedido.nuPedido = "1";
		itemPedido.flOrigemPedido = "P";
		itemPedido.flTipoItemPedido = "B";
		itemPedido.cdProduto = "1";
		itemPedido.nuSeqProduto = 1;
		itemPedido.setQtItemFisico(100);
		itemPedido.qtBonificadoBonifCfg = 10;
		itemPedido.pedido = pedido;
		itemPedido.vlItemPedido = 30;
		
		return itemPedido;
	}
	
	private BonifCfg createBonifCfg(boolean isTipoRegraQtd) {
		BonifCfg bonifCfg = new BonifCfg();
		bonifCfg.cdEmpresa = "1";
		bonifCfg.cdBonifCfg = "1";
		bonifCfg.cdTipoRegraBonificacao = isTipoRegraQtd ? BonifCfg.CDTIPOREGRA_QTDE : BonifCfg.CDTIPOREGRA_VALOR;
		bonifCfg.vlPctSobreVenda = isTipoRegraQtd ? 0 : 15;
		bonifCfg.flOpcional = ValueUtil.VALOR_NAO;
		
		return bonifCfg;
	}
	
	private ItemPedidoBonifCfg createItemPedidoBonifCfg(ItemPedido itemPedido, BonifCfg bonifCfg) {
		ItemPedidoBonifCfg itemPedidoBonifCfg = new ItemPedidoBonifCfg(itemPedido);
		itemPedidoBonifCfg.flTipoRegistro = itemPedido.isItemBonificacao() ? ItemPedidoBonifCfg.FLTIPOREGISTRO_DEBITO : ItemPedidoBonifCfg.FLTIPOREGISTRO_CREDITO;
		itemPedidoBonifCfg.cdBonifCfg = bonifCfg.cdBonifCfg;
		
		return itemPedidoBonifCfg;
	}
	
	private Pedido createPedido() {
		Pedido pedido = new Pedido();
		pedido.cdEmpresa = "1";
		pedido.cdRepresentante = "2";
		pedido.flOrigemPedido = "P";
		pedido.nuPedido = "3";
		pedido.setItemBrindeBonifCfgList(new Vector(0));
		
		return pedido;
	}
}
