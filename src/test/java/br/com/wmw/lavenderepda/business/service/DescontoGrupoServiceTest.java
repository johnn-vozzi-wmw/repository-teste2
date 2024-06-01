package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totalcross.util.Vector;

public class DescontoGrupoServiceTest  {

	ItemPedido itemPedido;

	@BeforeEach
	public void setUp() throws Exception {
		itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = "1";
		itemPedido.cdRepresentante = "1";
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemPedido.setItemTabelaPreco(itemTabelaPreco);
		itemPedido.vlBaseEmbalagemElementar = 3.02;
		itemPedido.vlUnidadePadrao = 3.02;
		itemPedido.vlBaseItemPedido = 36.24;
		itemPedido.vlPctDesconto = 10;
		
		Produto produto = new Produto();
		produto.cdProduto = "1";
		produto.cdGrupoProduto1 = "1";
		produto.dsProduto = "1";
		itemPedido.setProduto(produto);
		
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produtoUnidade.nuConversaoUnidade = 12;
		produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
		itemPedido.setProdutoUnidade(produtoUnidade);

	}

	@AfterEach
	public void tearDown() throws Exception {
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = false;
		LavenderePdaConfig.usaUnidadeAlternativa = false;
	}

	@Test
	public void testGetVlDescVlBaseDescGrupo() throws SQLException {
		itemPedido.vlPctDesconto = 10;
		assertEquals(32.62, DescontoGrupoService.getInstance().getVlDescVlBaseItemPedido(itemPedido, itemPedido.vlPctDesconto), 0.00001);

		itemPedido.vlPctDesconto = 12;
		assertEquals(31.89, DescontoGrupoService.getInstance().getVlDescVlBaseItemPedido(itemPedido, itemPedido.vlPctDesconto), 0.00001);
		
	}

	@Test
	public void testGetVlDescVlBaseDescGrupoVlElementar() throws SQLException {
		LavenderePdaConfig.permiteAlterarVlItemNaUnidadeElementar = true;
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		
		assertEquals(32.64, DescontoGrupoService.getInstance().getVlDescVlBaseItemPedido(itemPedido, itemPedido.vlPctDesconto), 0.00001);
		
		itemPedido.vlBaseEmbalagemElementar = 3.02;
		itemPedido.vlPctDesconto = 12;
		
		assertEquals(31.92, DescontoGrupoService.getInstance().getVlDescVlBaseItemPedido(itemPedido, itemPedido.vlPctDesconto), 0.00001);
	}

	@Test
	public void testGetQtdItensAdicionadosGrupo() throws SQLException {
		Vector itemPedidoList = new Vector();
		itemPedido.setQtItemFisico(5);
		itemPedidoList.addElement(itemPedido);
		itemPedidoList.addElement(itemPedido);
		
		assertEquals(10.00, DescontoGrupoService.getInstance().getQtdItensAdicionadosGrupo("1", itemPedidoList), 0.00001);
	}

	@Test
	public void testGetQtdItensAdicionadosGrupoUnidadeAlternativa() throws SQLException {
		LavenderePdaConfig.usaUnidadeAlternativa = true;
		Vector itemPedidoList = new Vector();
		itemPedido.setQtItemFisico(5);
		itemPedidoList.addElement(itemPedido);
		itemPedidoList.addElement(itemPedido);
		
		assertEquals(120.00, DescontoGrupoService.getInstance().getQtdItensAdicionadosGrupo("1", itemPedidoList), 0.00001);
	}

}
