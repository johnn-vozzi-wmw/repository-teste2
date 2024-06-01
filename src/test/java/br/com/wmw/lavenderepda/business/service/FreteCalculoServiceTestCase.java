package br.com.wmw.lavenderepda.business.service;

import java.util.HashMap;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FreteBaseCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import org.junit.jupiter.api.Test;
import totalcross.sys.InvalidNumberException;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class FreteCalculoServiceTestCase  {
	
	public final FreteCalculoService freteCalculoService;
	
	public FreteCalculoServiceTestCase() {
		freteCalculoService = FreteCalculoService.getInstance();
	}

	@Test
	public void testGetValorCalculadoOrValorMinimo() {
		BigDecimal valorCalculado = null;
		BigDecimal valorMinimo = null;
		BigDecimal resultado = null;
		resultado = freteCalculoService.getValorCalculadoOrValorMinimo(valorMinimo, valorCalculado);
		assertEquals(BigDecimal.ZERO, resultado);
		
		valorCalculado = BigDecimal.ONE;
		valorMinimo = BigDecimal.ONE;
		resultado = freteCalculoService.getValorCalculadoOrValorMinimo(valorMinimo, valorCalculado);
		assertEquals(BigDecimal.ONE, resultado);
		
		valorCalculado = BigDecimal.TEN;
		valorMinimo = BigDecimal.ONE;
		resultado = freteCalculoService.getValorCalculadoOrValorMinimo(valorMinimo, valorCalculado);
		assertEquals(BigDecimal.TEN, resultado);
		
		valorCalculado = BigDecimal.ZERO;
		valorMinimo = BigDecimal.TEN;
		resultado = freteCalculoService.getValorCalculadoOrValorMinimo(valorMinimo, valorCalculado);
		assertEquals(BigDecimal.TEN, resultado);
	}

	@Test
	public void testAplicaIndiceTaxaValor() throws InvalidNumberException {
		BigDecimal valor = null;
		BigDecimal vlIndiceTaxa = null;
		String flTipoTaxa = null;
		BigDecimal resultado = null;
		
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, BigDecimal.ZERO);
		
		valor = BigDecimal.ONE;
		flTipoTaxa = "AS";
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, valor);
		
		valor = BigDecimal.ONE;
		vlIndiceTaxa = BigDecimal.ONE;
		flTipoTaxa = "AS";
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, valor);
		
		valor = BigDecimal.ONE.setScale(7);
		vlIndiceTaxa = BigDecimal.ONE;
		flTipoTaxa = FreteCalculo.TIPO_TAXA_INDICE;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), valor);
		
		valor = BigDecimal.ONE.setScale(7);
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_INDICE;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("0.5",7));
		
		valor = BigDecimal.ONE.setScale(7);
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.3");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_INDICE;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("0.3",7));
		
		valor = BigDecimal.ONE.setScale(7);
		vlIndiceTaxa = ValueUtil.toBigDecimal("1.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_INDICE;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("1.5",7));
		
		valor = ValueUtil.toBigDecimal("5");
		vlIndiceTaxa = ValueUtil.toBigDecimal("1.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_INDICE;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("7.5",7));
		
		valor = ValueUtil.toBigDecimal("5");
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_INDICE;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("2.5",7));
		
		valor = BigDecimal.ONE.setScale(7);
		vlIndiceTaxa = BigDecimal.ONE.setScale(7);
		flTipoTaxa = FreteCalculo.TIPO_TAXA_PERCENTUAL;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa.multiply(ValueUtil.toBigDecimal(100)), flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), valor);
		
		valor = BigDecimal.ONE;
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_PERCENTUAL;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa.multiply(ValueUtil.toBigDecimal(100)), flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("0.5",7));
		
		valor = BigDecimal.ONE;
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.3");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_PERCENTUAL;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa.multiply(ValueUtil.toBigDecimal(100)), flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("0.3",7));
		
		valor = BigDecimal.ONE;
		vlIndiceTaxa = ValueUtil.toBigDecimal("1.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_PERCENTUAL;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa.multiply(ValueUtil.toBigDecimal(100)), flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("1.5",7));
		
		valor = ValueUtil.toBigDecimal("5");
		vlIndiceTaxa = ValueUtil.toBigDecimal("1.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_PERCENTUAL;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa.multiply(ValueUtil.toBigDecimal(100)), flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("7.5",7));
		
		valor = ValueUtil.toBigDecimal("5");
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_PERCENTUAL;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa.multiply(ValueUtil.toBigDecimal(100)), flTipoTaxa);
		assertEquals(resultado.setScale(7,BigDecimal.ROUND_HALF_UP), ValueUtil.toBigDecimal("2.5",7));
		
		valor = BigDecimal.ONE;
		vlIndiceTaxa = BigDecimal.ONE;
		flTipoTaxa = FreteCalculo.TIPO_TAXA_FIXO;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, ValueUtil.toBigDecimal("2"));
		
		valor = ValueUtil.toBigDecimal("5");
		vlIndiceTaxa = ValueUtil.toBigDecimal("0.5");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_FIXO;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, ValueUtil.toBigDecimal("5.5"));
		
		valor = ValueUtil.toBigDecimal("5");
		vlIndiceTaxa = ValueUtil.toBigDecimal("0");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_FIXO;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, ValueUtil.toBigDecimal("5"));
		
		valor = ValueUtil.toBigDecimal("0");
		vlIndiceTaxa = ValueUtil.toBigDecimal("0");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_FIXO;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, ValueUtil.toBigDecimal("0"));
		
		valor = ValueUtil.toBigDecimal("0");
		vlIndiceTaxa = ValueUtil.toBigDecimal("-1");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_FIXO;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, ValueUtil.toBigDecimal("-1"));
		
		valor = ValueUtil.toBigDecimal("3");
		vlIndiceTaxa = ValueUtil.toBigDecimal("2");
		flTipoTaxa = FreteCalculo.TIPO_TAXA_FIXO;
		resultado = freteCalculoService.aplicaIndiceTaxaValor(valor, vlIndiceTaxa, flTipoTaxa);
		assertEquals(resultado, ValueUtil.toBigDecimal("5"));
	}

	@Test
	public void testCalculaValorSomatorioFreteBaseCalculo() {
		HashMap<Integer, FreteCalculo> hashValoresCalculados = null;
		FreteCalculo freteCalculo = null;
		BigDecimal resultado = null;
		
		resultado = freteCalculoService.calculaValorSomatorioFreteBaseCalculo(hashValoresCalculados, freteCalculo, null);
		assertEquals(resultado, BigDecimal.ZERO);
		
		// Sem indíce e sem valor minimo
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("32.00")));
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00"))); 
		freteCalculo = createFreteCalculo(3, null, 0d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null);
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaValorSomatorioFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal(0)).setScale(7, BigDecimal.ROUND_HALF_UP);
		assertEquals(resultado, ValueUtil.toBigDecimal("76.0000000")); // Maior valor encontrado 44
		
		// Sem indíce e com valor minimo de 80.40
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("32.00")));
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00")));
		freteCalculo = createFreteCalculo(3, null, 0d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null);
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaValorSomatorioFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal("80.40")).setScale(7, BigDecimal.ROUND_HALF_UP); // Valor minimo de 80.40
		assertEquals(resultado, ValueUtil.toBigDecimal("80.4000000")); // aplica-se o 80.40. pois o maior valor 76 não atingiu o 80.40
		
		// Com indíce e sem valor minimo
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("100.00")));
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00")));
		freteCalculo = createFreteCalculo(3, FreteCalculo.TIPO_TAXA_PERCENTUAL, 5d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null); // 5% do somatorio
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaValorSomatorioFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal("0")).setScale(7, BigDecimal.ROUND_HALF_UP);
		assertEquals(resultado, ValueUtil.toBigDecimal("7.2000000")); // resulta-se 5% de 144
		
		// Com indíce e com valor minimo de 10
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("100.00")));
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00")));
		freteCalculo = createFreteCalculo(3, FreteCalculo.TIPO_TAXA_PERCENTUAL, 5d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null); // 5% do somatorio
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaValorSomatorioFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal("10.0000000")).setScale(7, BigDecimal.ROUND_HALF_UP);
		assertEquals(resultado, ValueUtil.toBigDecimal("10.0000000")); // resulta-se 5% de 144 porém como 7.2 é menor que 10, retorna-se o minimo = 10
	}

	@Test
	public void testCalculaMaiorValorFreteBaseCalculo() {
		HashMap<Integer, FreteCalculo> hashValoresCalculados = null;
		FreteCalculo freteCalculo = null;
		BigDecimal resultado = null;
		
		resultado = freteCalculoService.calculaMaiorValorFreteBaseCalculo(hashValoresCalculados, freteCalculo, null);
		assertEquals(resultado, BigDecimal.ZERO);
		
		// Sem indíce e sem valor minimo
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("32.00")));
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00"))); // Maior valor
		freteCalculo = createFreteCalculo(3, null, 0d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null);
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaMaiorValorFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal(0)).setScale(7, BigDecimal.ROUND_HALF_UP);
		assertEquals(resultado, ValueUtil.toBigDecimal("44.0000000")); // Maior valor encontrado 44
		
		// Sem indíce e com valor minimo de 57.40
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("32.00")));
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00"))); // Maior valor
		freteCalculo = createFreteCalculo(3, null, 0d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null);
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaMaiorValorFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal("57.40")).setScale(7, BigDecimal.ROUND_HALF_UP); // Valor minimo de 57.40
		assertEquals(resultado, ValueUtil.toBigDecimal("57.4000000")); // aplica-se o 57.40. pois o maior valor 44 não atingiu o 57.40
		
		// Com indíce e sem valor minimo
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("100.00"))); // Maior valor
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00")));
		freteCalculo = createFreteCalculo(3, FreteCalculo.TIPO_TAXA_PERCENTUAL, 5d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null); // 5% do maior valor encontrado
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaMaiorValorFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal("0")).setScale(7, BigDecimal.ROUND_HALF_UP);
		assertEquals(resultado, ValueUtil.toBigDecimal("5.0000000")); // resulta-se 5% do 100 (maior valor)
		
		// Com indíce e com valor minimo de 10
		hashValoresCalculados = new HashMap<Integer, FreteCalculo>();
		hashValoresCalculados.put(1, createFreteCalculo(1, FreteCalculo.TIPO_TAXA_FIXO, 32.00, ValueUtil.VALOR_NAO, 32d, 0, null, 0, ValueUtil.toBigDecimal("100.00"))); // Maior valor
		hashValoresCalculados.put(2, createFreteCalculo(2, FreteCalculo.TIPO_TAXA_FIXO, 44.00, ValueUtil.VALOR_NAO, 44d, 0, null, 0, ValueUtil.toBigDecimal("44.00")));
		freteCalculo = createFreteCalculo(3, FreteCalculo.TIPO_TAXA_PERCENTUAL, 5d, ValueUtil.VALOR_SIM, 0, 0d, null, 0d, null); // 5% do maior valor encontrado
		freteCalculo.freteConfig = createFreteConfig("1", ValueUtil.VALOR_SIM, ValueUtil.VALOR_SIM, FreteConfig.FL_TIPO_TAB_FRETE_CEP, null, 0, 99999999);
		freteCalculo.listFreteBaseCalculo = new Vector();
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 1));
		freteCalculo.listFreteBaseCalculo.addElement(createFreteBaseCalculo("1", 3, 2));
		resultado = freteCalculoService.calculaMaiorValorFreteBaseCalculo(hashValoresCalculados, freteCalculo, ValueUtil.toBigDecimal("10.0000000")).setScale(7, BigDecimal.ROUND_HALF_UP);
		assertEquals(resultado, ValueUtil.toBigDecimal("10.0000000")); // resulta-se 5% do 100 (maior valor) porém como 5 é menor que 10, retorna-se o minimo = 10
	}
	
	//--------------------------------------------------------------------------------------------
	//-- Criação dos objetos para testes
	//--------------------------------------------------------------------------------------------
	
	private FreteBaseCalculo createFreteBaseCalculo(final String cdTransportadora, final int nuSeqCalculo, final int nuSeqCalculoBC) {
		FreteBaseCalculo freteBaseCalculo = new FreteBaseCalculo();
		freteBaseCalculo.cdEmpresa = freteBaseCalculo.cdRepresentante = freteBaseCalculo.cdFreteConfig = "1";
		freteBaseCalculo.cdTransportadora = cdTransportadora;
		freteBaseCalculo.cdFreteCalculo = nuSeqCalculo;
		freteBaseCalculo.cdFreteCalculoBC = nuSeqCalculoBC;
		return freteBaseCalculo;
	}

	private FreteCalculo createFreteCalculo(final int nuSeqCalculo, final String flTipoTaxa, final double vlTaxa, final String flFormaPrecoFrete, final double vlMinimo, final double vlFaixaBc, final String fltipoTaxaExcedenteFaixaBC, final double vlTaxaExcedenteFaixaBC, final BigDecimal valorCalculado) {
		FreteCalculo freteCalculo;
		freteCalculo = new FreteCalculo();
		freteCalculo.cdEmpresa = freteCalculo.cdRepresentante = freteCalculo.cdFreteConfig = freteCalculo.cdFreteEvento = freteCalculo.cdTransportadora = "1";
		freteCalculo.nuOrdemCalculo = nuSeqCalculo;
		freteCalculo.flTipoTaxa = flTipoTaxa;
		freteCalculo.vlTaxa = vlTaxa;
		freteCalculo.flFormaPrecoFrete = flFormaPrecoFrete;
		freteCalculo.vlMinimo = vlMinimo;
		freteCalculo.vlFaixaBC = vlFaixaBc;
		freteCalculo.flTipoTaxaExcedendeFaixaBC = fltipoTaxaExcedenteFaixaBC;
		freteCalculo.vlTaxaExcedenteFaixaBC = vlTaxaExcedenteFaixaBC;
		freteCalculo.valorCalculado = valorCalculado;
		return freteCalculo;
	}

	private FreteConfig createFreteConfig(final String cdFreteConfig, final String flCif, final String flFob, final String flTipoTabFrete, final String cdRegiao, final int nuCepInicio, final int nuCepFim) {
		FreteConfig freteConfig = new FreteConfig("1", "1", "1");
		freteConfig.cdFreteConfig = cdFreteConfig;
		freteConfig.flCif = flCif;
		freteConfig.flFob = flFob;
		freteConfig.flTipoTabFrete = flTipoTabFrete;
		freteConfig.cdRegiao = cdRegiao;
		freteConfig.nuCepInicio = nuCepInicio;
		freteConfig.nuCepFim = nuCepFim;
		freteConfig.vlPrecoFreteCalculado = BigDecimal.ZERO;
		freteConfig.listFreteCalculo = new Vector();
		freteConfig.listFreteCalculoFormaFrete = new Vector();
		return freteConfig;
	}
	
}
