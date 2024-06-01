package br.com.wmw.lavenderepda.business.service.pontuacaoConfigService;

import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaDias;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaPreco;
import br.com.wmw.lavenderepda.business.domain.PontuacaoProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.PontuacaoProdutoService;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetVlPesoPontuacaoProdutoTest {

	public PontuacaoConfigService service;

	public GetVlPesoPontuacaoProdutoTest() {
		service = PontuacaoConfigService.getInstance();
		LavenderePdaConfig.nuCasasDecimaisPontuacao = 2;
	}

	@Test
	public void deveRetornarUmQuandoPontuacaoConfigNula() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		Produto produto = new Produto();
		produto.vlLitro = 1;
		itemPedido.setProduto(produto);
		assertEquals(1d, PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, null));
	}

	@Test
	public void deveRetornarUmQuandoPontuacaoConfigVazia() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		Produto produto = new Produto();
		produto.vlLitro = 1;
		itemPedido.setProduto(produto);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		assertEquals(1d, PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig));
	}

	@Test
	public void deveRetornarUmQuandoPontuacaoProdutoVazia() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		Produto produto = new Produto();
		produto.vlLitro = 1;
		itemPedido.setProduto(produto);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		assertEquals(1d, PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig));
	}

	@Test
	public void deveRetornarCincoQuandoPesoForCinco() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		Produto produto = new Produto();
		produto.vlLitro = 1;
		itemPedido.setProduto(produto);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao = 5d;
		assertEquals(5d, PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig));
	}

	@Test
	public void deveRetornarQuatroQuandoVlLitroDoisEPesoDois() throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setQtItemFisico(1);
		Produto produto = new Produto();
		produto.vlLitro = 2;
		itemPedido.setProduto(produto);
		PontuacaoConfig pontuacaoConfig = new PontuacaoConfig();
		pontuacaoConfig.pontuacaoProduto = new PontuacaoProduto();
		pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao = 2d;
		assertEquals(4d, PontuacaoProdutoService.getInstance().getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig));
	}

}
