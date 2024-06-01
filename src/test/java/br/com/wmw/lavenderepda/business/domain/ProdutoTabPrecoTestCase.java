package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTabPrecoTestCase  {

	@Test
	public void testIsVendido() {
		ProdutoTabPreco produtoTabPreco = new ProdutoTabPreco();
		assertFalse(produtoTabPreco.isVendido());
		//--
		produtoTabPreco.flVendido = ValueUtil.VALOR_NAO;
		assertFalse(produtoTabPreco.isVendido());
		//--
		produtoTabPreco.flVendido = ValueUtil.VALOR_SIM;
	    assertTrue(produtoTabPreco.isVendido());
	    //--
	    produtoTabPreco.flVendido = OrigemPedido.FLORIGEMPEDIDO_PDA;
	    assertTrue(produtoTabPreco.isVendido());
		
	}

}
