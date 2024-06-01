package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashSet;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;

import org.junit.jupiter.api.Test;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;

public class SenhaDinamicaServiceTestCase  {

	@Test
	public void testGetSenhaCorretaLiberacaoSqlExecutor() {
		int result;

		//Teste valores inválidos
		result = SenhaDinamicaService.getSenhaCorreta(0, 0, 0, 0);
		assertEquals(0, result);

		// Número 1 = 0 , Número 2 = 120, Semente = 15000, Usuário = 1
		result = SenhaDinamicaService.getSenhaCorreta(0, 120, 15000, 1);
		assertEquals(0, result);

		// Número 1 = 100, Número 2 = 120, Semente = 15000, Usuário = 1
		result = SenhaDinamicaService.getSenhaCorreta(100, 120, 15000, 1);
		assertEquals(7500, result);

		// Número 1 = 1500, Número 2 = 1245, Semente = 15000, Usuário = 40
		result = SenhaDinamicaService.getSenhaCorreta(1500, 1245, 15000, 40);
		assertEquals(7582, result);

		// Número 1 = 2548, Número 2 = 9574, Semente = 15000, Usuário = 3
		result = SenhaDinamicaService.getSenhaCorreta(2548, 9574, 15000, 3);
		assertEquals(8314, result);

		// Número 1 = 2548, Número 2 = 9574, Semente = 15000, Usuário = T001
		result = SenhaDinamicaService.getSenhaCorreta(2548, 9574, 15000, ValueUtil.getIntegerValue("T001"));
		assertEquals(8313, result);

		// Número 1 = 2548, Número 2 = 9574, Semente = 15000, Usuário = DEVADONI
		result = SenhaDinamicaService.getSenhaCorreta(2548, 9574, 15000, ValueUtil.getIntegerValue("DEVADONI"));
		assertEquals(8313, result);

		// Número 1 = 2548, Número 2 = 9574, Semente = 15000, Usuário = DEVADONI
		SenhaDinamicaService.forceString = true;
		result = SenhaDinamicaService.getSenhaCorreta(2548, 9574, 15000, SenhaDinamicaService.getValueGeracaoSenha("DEVADONI"));
		assertEquals(218968242, result);
		SenhaDinamicaService.forceString = false;
	}

	@Test
	public void testSenhaCorretaDataUsuario() throws SQLException {
		int fator1 = 1234;
		int fator2 = 5678;
		int semente = 5000;
		Date data = null;
		try {
			data = new Date(10, 10, 2016);
		} catch (InvalidDateException e) {
			e.printStackTrace();
		}
		String cdUsuario = "11002";

		LavenderePdaConfig.liberaSenhaDiaEntregaPedido = "S";
		int senhaData = SenhaDinamicaService.getSenhaCorreta(fator1, fator2, semente, data, cdUsuario);
		assertEquals(29093, senhaData);

		LavenderePdaConfig.liberaSenhaDiaEntregaPedido = "2";
		int senhaDataUsuario = SenhaDinamicaService.getSenhaCorreta(fator1, fator2, semente, data, cdUsuario);
		assertNotSame(40090, senhaDataUsuario);

		LavenderePdaConfig.liberaSenhaDiaEntregaPedido = "2";
		senhaDataUsuario = SenhaDinamicaService.getSenhaCorreta(fator1, fator2, semente, data, cdUsuario);
		assertEquals(40095, senhaDataUsuario);

		//---

		int chaveSemente = SenhaDinamica.SENHA_LIBERACAO_DIA_ENTREGA_PEDIDO;

		LavenderePdaConfig.sementeSenhaDiaEntregaPedido = 5000;
		try {
			SenhaDinamicaService.validateSenha(fator1, fator2, "", "", 0, 0, senhaDataUsuario, chaveSemente, "", "", "", "", 0, "", cdUsuario, "", data, "", "");
		} catch (ValidationException e) {
			fail("Senha deveria estar correta");
		}

		cdUsuario = "11001";
		try {
			SenhaDinamicaService.validateSenha(fator1, fator2, "", "", 0, 0, senhaDataUsuario, chaveSemente, "", "", "", "", 0, "", cdUsuario, "", data, "", "a");
			fail("Senha deveria estar incorreta");
		} catch (ValidationException e) {
		}

		LavenderePdaConfig.liberaSenhaDiaEntregaPedido = "S";
		try {
			SenhaDinamicaService.validateSenha(fator1, fator2, "", "", 0, 0, senhaData, chaveSemente, "", "", "", "", 0, "", cdUsuario, "", data, "", "0");
		} catch (ValidationException e) {
			fail("Usuário não deveria ter interferido na fórmula");
		}

		LavenderePdaConfig.liberaSenhaDiaEntregaPedido = "N";
		LavenderePdaConfig.sementeSenhaDiaEntregaPedido = 0;
	}

	@Test
	public void testdeveTestarSenha() {
		HashSet hashSet = new HashSet();
		int cont = 0;
		for (double i = 0; i <= 100d; i = i + 0.01) {
			cont++;
			int senha = SenhaDinamicaService.getSenhaCorreta(
					28,
					8282,
					15000,
					"22",
					"1012",
					2,
					i,
					null
			);
			hashSet.add(senha);

		}

		assertEquals(cont, hashSet.size());

	}
	
	@Test 
	public void testGeracaoSenhaDesbloqueioSistemaPorTempo() {
//		fórmula (((fator1 * fator2) / semente) + semente + cdUsuarioInt) + (tempoLiberacaoSegundos / 60);
//		cdUsuario = WMW = 58519 (valor int)
		int result;
		String cdUsuario = "WMW";
		
		result = SenhaDinamicaService.getSenha(500, 100, 15000, cdUsuario, "01:30");
		assertEquals(73612, result);
		//
		result = SenhaDinamicaService.getSenha(8000, 2000, 15000, cdUsuario, "00:30");
		assertEquals(74615, result);
		//
		//horario inválido, deve lançar exceção
		try {
			result = SenhaDinamicaService.getSenha(1000, 500, 1500, cdUsuario, "00:01");
			assertEquals(1, 0);
		} catch (Exception e) {
			assertEquals(0, 0);
		}
		//horario inválido, deve lançar exceção
		try {
			result = SenhaDinamicaService.getSenha(1000, 500, 1500, cdUsuario, "0030");
			assertEquals(1, 0);
		} catch (Exception e) {
			assertEquals(0, 0);
		}
	}

}
