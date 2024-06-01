package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.builder.ItemPedidoBuilder;
import br.com.wmw.lavenderepda.business.builder.UsuarioRelRepBuilder;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import org.junit.jupiter.api.Test;

public class UsuarioRelRepServiceTest  {

	@Test
	public void testDescontoPermitidoParaUsuarioSelecionado() throws SQLException {
		UsuarioRelRepService usuarioRelRepService = UsuarioRelRepService.getInstance();
		
		UsuarioRelRep usuarioRelRep = new UsuarioRelRepBuilder("1", "1", "1").comVlPctMaxAcrescimo(15).comVlPctMaxDesconto(10).build();
		
		ItemPedido itemPedido = new ItemPedidoBuilder("1", "1", "1", "1").build();
		itemPedido.vlBaseItemPedido = 10;
		
		
		double vlItemInformado = 10;
		assertTrue(usuarioRelRepService.isDescontoPermitidoParaUsuarioSelecionado(usuarioRelRep, itemPedido, vlItemInformado));
		
		vlItemInformado = 9;
		assertTrue(usuarioRelRepService.isDescontoPermitidoParaUsuarioSelecionado(usuarioRelRep, itemPedido, vlItemInformado));
		
		vlItemInformado = 8.9;
		assertFalse(usuarioRelRepService.isDescontoPermitidoParaUsuarioSelecionado(usuarioRelRep, itemPedido, vlItemInformado));
		
		vlItemInformado = 11.5;
		assertTrue(usuarioRelRepService.isDescontoPermitidoParaUsuarioSelecionado(usuarioRelRep, itemPedido, vlItemInformado));
		
		vlItemInformado = 11.6;
		assertFalse(usuarioRelRepService.isDescontoPermitidoParaUsuarioSelecionado(usuarioRelRep, itemPedido, vlItemInformado));
		
	}


}
