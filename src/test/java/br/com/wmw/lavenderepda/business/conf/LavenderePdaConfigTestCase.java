package br.com.wmw.lavenderepda.business.conf;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LavenderePdaConfigTestCase  {

	@Test
	public void testIsOrdemCamposTelaItemPedidoValida() {
		//Null
		assertFalse(LavenderePdaConfig.isOrdemCamposValida(null));
		//vazio sem espaços
		assertFalse(LavenderePdaConfig.isOrdemCamposValida(""));
		//vazio com espaços
		assertFalse(LavenderePdaConfig.isOrdemCamposValida("  "));
		//Começando com consoantes
		assertFalse(LavenderePdaConfig.isOrdemCamposValida("abc"));
		//Começanco com caracteres especiais
		assertFalse(LavenderePdaConfig.isOrdemCamposValida(";"));
		assertFalse(LavenderePdaConfig.isOrdemCamposValida(";45;45;12"));
		assertFalse(LavenderePdaConfig.isOrdemCamposValida("#4;87(:"));
		//Começando com zero (0;11;9;10)
		assertFalse(LavenderePdaConfig.isOrdemCamposValida("0;11;9;10"));
		//configuração válida 12;45;78
		assertTrue(LavenderePdaConfig.isOrdemCamposValida("12;45;78"));
		//Começanco com caracteres especiais
		assertTrue(LavenderePdaConfig.isOrdemCamposValida("4444;"));
	}
	
	public void testGetHoraLimiteParaEnvioPedidos() {
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = null;
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidos());
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "";
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidos());
		//-- 
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "14:50";
		assertEquals("14:50", LavenderePdaConfig.getHoraLimiteParaEnvioPedidos());
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "14:00|15:00|";
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(DateUtil.getDateValue(14, 06, 2016)));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "|15:00|17:00|15:00|||";
		assertEquals("17:00", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(DateUtil.getDateValue(14, 06, 2016)));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "|15:00";
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(DateUtil.getDateValue(14, 06, 2016)));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "14:00|15:00|";
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(null));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "||||||";
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(DateUtil.getDateValue(14, 06, 2016)));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "||||";
		assertEquals("", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(DateUtil.getDateValue(14, 06, 2016)));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "12:00|13:00|14:00|15:00|16:00|17:00|18:00";
		assertEquals("14:00", LavenderePdaConfig.getHoraLimiteParaEnvioPedidosByDiaSemana(DateUtil.getDateValue(14, 06, 2016)));
	}
	
	public void testGetHoraLimiteParaEnvioPedidosList() {
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = null;
		List<String> horaLimiteList = LavenderePdaConfig.getHoraLimiteParaEnvioPedidosList();
		assertTrue(horaLimiteList.contains(""));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "";
		horaLimiteList = LavenderePdaConfig.getHoraLimiteParaEnvioPedidosList();
		assertTrue(horaLimiteList.contains(""));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "14:50";
		horaLimiteList = LavenderePdaConfig.getHoraLimiteParaEnvioPedidosList();
		assertTrue(horaLimiteList.contains("14:50"));
		//--
		LavenderePdaConfig.horaLimiteParaEnvioPedidos = "14:50-15:00";
		horaLimiteList = LavenderePdaConfig.getHoraLimiteParaEnvioPedidosList();
		assertTrue(horaLimiteList.contains("14:50") && horaLimiteList.contains("15:00"));
		//--
		
	}
	
	public void testIsUsaPesquisaMercadoProdutosConcorrentesValor() {
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = null;
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "N";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "asda";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "2";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1,2";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1,2,3";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "2,1";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1;2";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = ";null";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor());
	}
	
	public void testIsUsaPesquisaMercadoProdutosConcorrentesGondola() {
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = null;
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "N";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "asda";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "2";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1,2";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1,2,3";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "2,1";
		assertEquals(true, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = "1;2";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
		//--
		LavenderePdaConfig.usaPesquisaMercadoProdutosConcorrentes = ";null";
		assertEquals(false, LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola());
	}
	
	public void testIsUsaAvisoClienteSemPesquisaMercadoTipoValor() {
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = null;
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,N";
		assertEquals(false, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,1";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,2";
		assertEquals(false, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,1,D,T";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,2,D,T";
		assertEquals(false, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor());
	}
	
	public void testIsUsaAvisoClienteSemPesquisaMercadoTipoGondola() {
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = null;
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,N";
		assertEquals(false, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,1";
		assertEquals(false, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,2";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,1,D,T";
		assertEquals(false, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
		//--
		LavenderePdaConfig.usaAvisoClienteSemPesquisaMercado = "5,S,2,D,T";
		assertEquals(true, LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola());
	}
	
	public void testIsUsaTotalizadoresEspecificosListaCliente() {
		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "1;2";
		assertTrue(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "1";
		assertTrue(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "2";
		assertTrue(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "2;1";
		assertTrue(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "2;";
		assertTrue(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "1;";
		assertTrue(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertTrue(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "asdasd;asd,asd.";
		assertFalse(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = ";;;;;";
		assertFalse(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "null";
		assertFalse(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = "";
		assertFalse(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());

		LavenderePdaConfig.usaTotalizadoresEspecificosListaCliente = null;
		assertFalse(LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtrasado());
		assertFalse(LavenderePdaConfig.isUsaTotalizadorClienteAtendido());
	}
	
	public void testUsaMultiplasSugestoesProdutoFechamentoPedido() {
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "N";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "S";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "1";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "3";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "1,3";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,1";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "S;N";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "S;S";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "N;1";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "N;2";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "N;3";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "S;1,3";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "N;2,1";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = null;
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda());
	}
	
	public void testQtLimiteItensMultiplasSugestoesListaGeral() {
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "1,2,3";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "9,2,4";
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", true));
		assertEquals(2, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", true));
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "1,3;3,2";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "9,3,4";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", true));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", true));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "N;2,3,1";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "9,3,4";
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", true));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", true));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = null;
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "N";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", true));
		//--
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "1,2,3";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "9,2,4";
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", false));
		assertEquals(2, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", false));
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "3,2";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "9,3,4";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", false));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", false));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "3,2";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "S";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", false));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", false));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensMultiplasSugestoesListaGeral = "N";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "1", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "2", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, "3", false));
		//--
	}
	
	public void testQtLimiteItensMultiplasSugestoesListaEspecifica() {
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "1,2,3";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "9,2,4";
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(2, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "3,2";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "9,3,4";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "9,3,4";
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = null;
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "N";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "S";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "2,1";
		assertEquals(2, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido = "2,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "S";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", true));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", true));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", true));
		//--
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "1,2,3";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "9,2,4";
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(2, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "3,2";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "9,3,4";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "9,3,4";
		assertEquals(4, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(9, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(3, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = null;
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2,3,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "N";
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(-1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "S";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "2,1";
		assertEquals(2, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(1, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2,1";
		LavenderePdaConfig.qtLimiteItensSugestaoListasEspecificas = "S";
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "1", false));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "2", false));
		assertEquals(0, LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, "3", false));
	}
	
	public void testUsaMultiplasSugestoesProdutoInicioPedido() {
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "1,2,3";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "1,3";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "1";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "N";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "S";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "1";
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "2";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "3";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertTrue(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = "";
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
		//--
		LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido = null;
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas());
		assertFalse(LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda());
	}
	
	public void testGetNuDiasPermanenciaFechamentoDiario() {
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "N";
		assertEquals(180, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "S";
		assertEquals(180, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "X";
		assertEquals(180, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "X1";
		assertEquals(180, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "0";
		assertEquals(0, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "2";
		assertEquals(2, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "20";
		assertEquals(20, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
		
		LavenderePdaConfig.nuDiasPermanenciaFechamentoDiario = "300";
		assertEquals(300, LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario());
	}

	
	public void testLoadVlMinimoPedido() {
		LavenderePdaConfig.loadVlMinimoPedido("100");
		assertEquals(new Double(100), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido("100;");
		assertEquals(new Double(100), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido("100;100");
		assertEquals(new Double(100), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido("100;200");
		assertEquals(new Double(200), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido(";200");
		assertEquals(new Double(200), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido(";N");
		assertEquals(new Double(0), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido("");
		assertEquals(new Double(0), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido(";");
		assertEquals(new Double(0), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido(null);
		assertEquals(new Double(0), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido("asd");
		assertEquals(new Double(0), LavenderePdaConfig.valorMinimoParaPedido);
		LavenderePdaConfig.loadVlMinimoPedido("1;3;4;5");
		assertEquals(new Double(3), LavenderePdaConfig.valorMinimoParaPedido);
	}
	
	public void testMetodosAuxiliaresJson() {
		String vlParametro = null;
		int cdParametro = 0;
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "usa", "N", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "N", cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo2", "S", cdParametro));

		vlParametro = "";
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "usa", "N", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "N", cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo2", "S", cdParametro));
		
		vlParametro = "{ \"usa\":\"N\", \"atributo1\":\"N\", \"atributo2\":\"N\" }";
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "usa", "N", cdParametro));
		assertFalse(LavenderePdaConfig.isAtributoJsonLigado(vlParametro, "usa", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "N", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo2", "N", cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "S", cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", null, cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, null, "N", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "", "N", cdParametro));
		
		vlParametro = "{ \"usa\":\"S\", \"atributo1\":\"50\", \"atributo2\":\"\" }";
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "usa", "S", cdParametro));
		assertTrue(LavenderePdaConfig.isAtributoJsonLigado(vlParametro, "usa", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "50", cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "49", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo2", "", cdParametro));
		assertFalse(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo2", "N", cdParametro));
		
		assertEquals("50", LavenderePdaConfig.getValorAtributoJson(vlParametro, "atributo1", cdParametro));
		assertEquals(50, ValueUtil.getIntegerValue(LavenderePdaConfig.getValorAtributoJson(vlParametro, "atributo1", cdParametro)));

		vlParametro = "{ \"usa\":\"";
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "usa", "N", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo1", "N", cdParametro));
		assertTrue(LavenderePdaConfig.comparaValorAtributoJson(vlParametro, "atributo2", "N", cdParametro));
		
	}

	public void testFechamentoDiario() {
		LavenderePdaConfig.usaConfigFechamentoDiarioVendas = "{ \"usa\":\"S\", \"bloqueiaFechamentoPedidoNaoTransmitido\":\"S\", \"liberaFechamentoDiarioComSenha\":\"S\", \"permiteInformarVeiculoFechamento\":\"S\", \"obrigaInformarVeiculoFechamento\":\"S\", \"usaRelatorioEstoqueProduto\":\"S\", \"usaTipoLancamentoDinamico\":\"S\", \"consideraValorPedidoAtualRetornado\":\"S\", \"nuDiasConsideraValorPedidoAtualRetornadoPagamentos\":\"5\"}";
		assertTrue(LavenderePdaConfig.usaConfigFechamentoDiarioVendas());
		assertTrue(LavenderePdaConfig.isBloqueiaFechamentoDiarioPedidosNaoTransmitidos());
		assertTrue(LavenderePdaConfig.isLiberaFechamentoDiarioComSenha());
		assertTrue(LavenderePdaConfig.usaPermiteInformarVeiculoFechamento());
		assertTrue(LavenderePdaConfig.usaObrigaInformarVeiculoFechamento());
		assertTrue(LavenderePdaConfig.usaRelatorioEstoqueProduto());
		assertTrue(LavenderePdaConfig.usaTipoLancamentoDinamico());
		assertTrue(LavenderePdaConfig.consideraValorPedidoAtualRetornado());
		assertEquals(LavenderePdaConfig.nuDiasConsideraValorPedidoAtualRetornadoPagamentos(), 5);
		LavenderePdaConfig.usaConfigFechamentoDiarioVendas = "{ \"usa\":\"N\", \"bloqueiaFechamentoPedidoNaoTransmitido\":\"S\", \"liberaFechamentoDiarioComSenha\":\"S\", \"permiteInformarVeiculoFechamento\":\"S\", \"obrigaInformarVeiculoFechamento\":\"S\", \"usaRelatorioEstoqueProduto\":\"S\", \"usaTipoLancamentoDinamico\":\"S\", \"consideraValorPedidoAtualRetornado\":\"S\", \"nuDiasConsideraValorPedidoAtualRetornadoPagamentos\":\"0\"}";
		assertFalse(LavenderePdaConfig.usaConfigFechamentoDiarioVendas());
		assertFalse(LavenderePdaConfig.isBloqueiaFechamentoDiarioPedidosNaoTransmitidos());
		assertFalse(LavenderePdaConfig.isLiberaFechamentoDiarioComSenha());
		assertFalse(LavenderePdaConfig.usaPermiteInformarVeiculoFechamento());
		assertFalse(LavenderePdaConfig.usaObrigaInformarVeiculoFechamento());
		assertFalse(LavenderePdaConfig.usaRelatorioEstoqueProduto());
		assertFalse(LavenderePdaConfig.usaTipoLancamentoDinamico());
		assertFalse(LavenderePdaConfig.consideraValorPedidoAtualRetornado());
		assertEquals(LavenderePdaConfig.nuDiasConsideraValorPedidoAtualRetornadoPagamentos(), 0);
		LavenderePdaConfig.usaConfigFechamentoDiarioVendas = "{ \"usa\":\"S\", \"bloqueiaFechamentoPedidoNaoTransmitido\":\"N\", \"liberaFechamentoDiarioComSenha\":\"N\", \"permiteInformarVeiculoFechamento\":\"N\", \"obrigaInformarVeiculoFechamento\":\"N\", \"usaRelatorioEstoqueProduto\":\"N\", \"usaTipoLancamentoDinamico\":\"N\", \"consideraValorPedidoAtualRetornado\":\"N\", \"nuDiasConsideraValorPedidoAtualRetornadoPagamentos\":\"0\"}";
		assertTrue(LavenderePdaConfig.usaConfigFechamentoDiarioVendas());
		assertFalse(LavenderePdaConfig.isBloqueiaFechamentoDiarioPedidosNaoTransmitidos());
		assertFalse(LavenderePdaConfig.isLiberaFechamentoDiarioComSenha());
		assertFalse(LavenderePdaConfig.usaPermiteInformarVeiculoFechamento());
		assertFalse(LavenderePdaConfig.usaObrigaInformarVeiculoFechamento());
		assertFalse(LavenderePdaConfig.usaRelatorioEstoqueProduto());
		assertFalse(LavenderePdaConfig.usaTipoLancamentoDinamico());
		assertFalse(LavenderePdaConfig.consideraValorPedidoAtualRetornado());
		assertEquals(LavenderePdaConfig.nuDiasConsideraValorPedidoAtualRetornadoPagamentos(), 0);
	}

}
