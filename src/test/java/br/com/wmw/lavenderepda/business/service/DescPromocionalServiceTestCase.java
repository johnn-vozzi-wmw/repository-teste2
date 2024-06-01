package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import org.junit.jupiter.api.Test;
import totalcross.util.Hashtable;

import java.sql.SQLException;

public class DescPromocionalServiceTestCase  {
	
	private static final String CDCLIENTE_BASE = "10";
	private static final String CDPRODUTO_BASE = "500";

	@Test
	public void testCalcDescPromocionalUnidadeAlternativa() {
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
		produtoUnidade.nuConversaoUnidade = 3;
		
		DescPromocional descPromocional = getNewDescPromocional();
		DescPromocionalService.getInstance().calcDescPromocionalUnidadeAlternativa(descPromocional, produtoUnidade);
		assertEquals(2, descPromocional.qtItem);
		//--
		produtoUnidade = new ProdutoUnidade();
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
		produtoUnidade.nuConversaoUnidade = 10;
		
		descPromocional = getNewDescPromocional();
		DescPromocionalService.getInstance().calcDescPromocionalUnidadeAlternativa(descPromocional, produtoUnidade);
		assertEquals(1, descPromocional.qtItem);
		//--
		produtoUnidade = new ProdutoUnidade();
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
		produtoUnidade.nuConversaoUnidade = 1;
		
		descPromocional = getNewDescPromocional();
		DescPromocionalService.getInstance().calcDescPromocionalUnidadeAlternativa(descPromocional, produtoUnidade);
		assertEquals(5, descPromocional.qtItem);
		//--
		produtoUnidade = new ProdutoUnidade();
		produtoUnidade.nuConversaoUnidade = 3;
		
		descPromocional = getNewDescPromocional();
		DescPromocionalService.getInstance().calcDescPromocionalUnidadeAlternativa(descPromocional, produtoUnidade);
		assertEquals(15, descPromocional.qtItem);
		//--
		produtoUnidade = new ProdutoUnidade();
		produtoUnidade.nuConversaoUnidade = 10;
		
		descPromocional = getNewDescPromocional();
		DescPromocionalService.getInstance().calcDescPromocionalUnidadeAlternativa(descPromocional, produtoUnidade);
		assertEquals(50, descPromocional.qtItem);
		//--
		produtoUnidade = new ProdutoUnidade();
		produtoUnidade.nuConversaoUnidade = 1;
		
		descPromocional = getNewDescPromocional();
		DescPromocionalService.getInstance().calcDescPromocionalUnidadeAlternativa(descPromocional, produtoUnidade);
		assertEquals(5, descPromocional.qtItem);
		
	}
	
	private DescPromocional getNewDescPromocional() {
		DescPromocional descPromocional = new DescPromocional();
		descPromocional.qtItem = 5;
		descPromocional.vlPctDescontoProduto = 3;
		return descPromocional;
	}

	@Test
	public void testLoadDescPromocionalComQtdSemRegrasAdicionais() throws SQLException {
		Hashtable descPromoHash = new Hashtable(0);
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "100";
		Pedido pedido = new Pedido();
		pedido.cdCondicaoComercial = "5";
		itemPedido.cdTabelaPreco = "1";
		itemPedido.pedido = pedido;
		
		DescPromocional descPromocional = new DescPromocional();
		descPromocional.cdEmpresa = "1";
		descPromocional.cdRepresentante = "100";
		descPromocional.cdCliente = "10";
		descPromocional.cdProduto = "500";
		descPromocional.cdGrupoDescCli = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.cdGrupoDescProd = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.qtItem = 2;
		descPromocional.cdTabelaPreco = "2";
		descPromocional.cdCondicaoComercial = "5";
		descPromocional.cdCondicaoPagamento = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.cdTipoFrete = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.cdTipoPedido = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.cdLocalEstoque = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.cdUf = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional.cdLocal = "0";
		descPromoHash.put(descPromocional.getRowKey(), descPromocional);
		DescPromocionalService.getInstance().descPromoPorProdutoHash = null;
		DescPromocionalService.getInstance().loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, CDCLIENTE_BASE, CDPRODUTO_BASE, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, descPromoHash);
		assertEquals(0, itemPedido.descPromocionalComQtdList.size());
		
		DescPromocional descPromocional2 = (DescPromocional) descPromocional.clone();
		descPromocional2.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional2.qtItem = 4;
		descPromoHash.put(descPromocional2.getRowKey(), descPromocional2);
		DescPromocionalService.getInstance().descPromoPorProdutoHash = null;
		DescPromocionalService.getInstance().loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, CDCLIENTE_BASE, CDPRODUTO_BASE, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, descPromoHash);
		assertEquals(0, itemPedido.descPromocionalComQtdList.size());
		
		DescPromocional descPromocional3 = (DescPromocional) descPromocional.clone();
		descPromocional3.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional3.cdCondicaoComercial = "5";
		descPromocional3.qtItem = 5;
		descPromoHash.put(descPromocional3.getRowKey(), descPromocional3);
		DescPromocionalService.getInstance().descPromoPorProdutoHash = null;
		DescPromocionalService.getInstance().loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, CDCLIENTE_BASE, CDPRODUTO_BASE, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, descPromoHash);
		assertEquals(descPromocional3, itemPedido.descPromocionalComQtdList.items[0]);

		DescPromocional descPromocional4 = (DescPromocional) descPromocional.clone();
		descPromocional4.cdTabelaPreco = "1";
		descPromocional4.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional4.qtItem = 6;
		descPromoHash.put(descPromocional4.getRowKey(), descPromocional4);
		DescPromocionalService.getInstance().descPromoPorProdutoHash = null;
		DescPromocionalService.getInstance().loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, CDCLIENTE_BASE, CDPRODUTO_BASE, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, descPromoHash);
		assertEquals(descPromocional3, itemPedido.descPromocionalComQtdList.items[0]);
		assertEquals(descPromocional4, itemPedido.descPromocionalComQtdList.items[1]);

		DescPromocional descPromocional5 = (DescPromocional) descPromocional.clone();
		descPromocional5.cdTabelaPreco = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional5.cdCondicaoComercial = DescPromocional.CD_CHAVE_VAZIO;
		descPromocional5.qtItem = 7;
		descPromoHash.put(descPromocional5.getRowKey(), descPromocional5);
		DescPromocionalService.getInstance().descPromoPorProdutoHash = null;
		DescPromocionalService.getInstance().loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, CDCLIENTE_BASE, CDPRODUTO_BASE, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, descPromoHash);
		assertEquals(descPromocional3, itemPedido.descPromocionalComQtdList.items[0]);
		assertEquals(descPromocional4, itemPedido.descPromocionalComQtdList.items[1]);

		DescPromocional descPromocional6 = (DescPromocional) descPromocional.clone();
		descPromocional6.cdTabelaPreco = "1";
		descPromocional6.cdCondicaoComercial = "5";
		descPromocional6.qtItem = 8;
		descPromoHash.put(descPromocional6.getRowKey(), descPromocional6);
		DescPromocionalService.getInstance().descPromoPorProdutoHash = null;
		DescPromocionalService.getInstance().loadDescPromocionalComQtdSemRegrasAdicionais(itemPedido, CDCLIENTE_BASE, CDPRODUTO_BASE, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, DescPromocional.CD_CHAVE_VAZIO, descPromoHash);
		assertEquals(descPromocional3, itemPedido.descPromocionalComQtdList.items[0]);
		assertEquals(descPromocional4, itemPedido.descPromocionalComQtdList.items[1]);
		assertEquals(descPromocional6, itemPedido.descPromocionalComQtdList.items[2]);

	}

}
