package br.com.wmw.lavenderepda.business.service.pontuacaoConfigService;

import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoProduto;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GetPontuacaoBaseItemTest {

	public PontuacaoConfigService service;

	public GetPontuacaoBaseItemTest() {
		service = PontuacaoConfigService.getInstance();
		LavenderePdaConfig.nuCasasDecimaisPontuacao = 2;
	}

	@Test
	public void deveRetornarZeroQuandoParametrosNulos() throws SQLException {
		assertEquals(0d, service.getPontuacaoBaseItem(null, null));
	}

	@Test
	public void deveRetornarZeroQuandoItemPedidoNulo() throws SQLException {
		assertEquals(0d, service.getPontuacaoBaseItem(null, new PontuacaoConfig()));
	}

	@Test
	public void deveRetornarZeroQuandoPontuacaoConfigNula() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		assertEquals(0d, service.getPontuacaoBaseItem(itemPedido, null));
	}

	@Test
	public void deveRetornarUmQuandoPontuacaoProdutoNula() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		assertEquals(1d, service.getPontuacaoBaseItem(itemPedido, pontuacaoConfig));
	}

	@Test
	public void deveRetornarUmQuandoNaoHouverPesoProduto() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		assertEquals(1d, service.getPontuacaoBaseItem(itemPedido, pontuacaoConfig));
	}

	@Test
	public void deveMultiplicarQuantidadePorPesoInteiro() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(3d);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao = 40d;
		assertEquals(120d, service.getPontuacaoBaseItem(itemPedido, pontuacaoConfig));
	}

	@Test
	public void deveMultiplicarQuantidadePorPesoFracionado() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(3d);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao = 3.33d;
		assertEquals(9.99, service.getPontuacaoBaseItem(itemPedido, pontuacaoConfig));
	}

}
