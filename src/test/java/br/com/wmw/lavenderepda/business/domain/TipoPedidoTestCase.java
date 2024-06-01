package br.com.wmw.lavenderepda.business.domain;



import br.com.wmw.framework.util.ValueUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TipoPedidoTestCase  {
	
	@Test
	public void testFlGeraAtendimento() {
		TipoPedido tiPedido = new TipoPedido();
		assertTrue(tiPedido.isGeraAtendimento());
		
		tiPedido.flGeraAtendimento = ValueUtil.VALOR_SIM;
		assertTrue(tiPedido.isGeraAtendimento());

		tiPedido.flGeraAtendimento = ValueUtil.VALOR_NAO;
		assertFalse(tiPedido.isGeraAtendimento());
		
		tiPedido.flGeraAtendimento = ValueUtil.VALOR_NI;
		assertTrue(tiPedido.isGeraAtendimento());

		tiPedido.flGeraAtendimento = "P";
		assertTrue(tiPedido.isGeraAtendimento());
	}

}
