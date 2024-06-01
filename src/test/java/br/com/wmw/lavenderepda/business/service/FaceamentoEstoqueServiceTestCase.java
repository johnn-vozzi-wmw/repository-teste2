package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FaceamentoEstoqueServiceTestCase {
	
	private static final int QUANTIDADE_ESTOQUE_CINCO = 5;
	private static final int QUANTIDADE_ESTOQUE_DEZ = 10;
	private static final int PONTO_DE_EQUILIBRIO_CINCO = 5;
	private static final int PONTO_DE_EQUILIBRIO_DEZ = 10;
	private static final double FATOR_FACEAMENTO = 2.75;
	private static final int PRECISAO_ZERO = 0;
	private static final int PRECISAO_DUAS_CASAS = 5;

	@Test
	public void sugestaoDeveSerTrezeComSetentaECinco() {
		final FaceamentoEstoqueService faceamentoEstoqueService = FaceamentoEstoqueService.getInstance();
		double resultado = faceamentoEstoqueService.calculaSugestaoVenda(QUANTIDADE_ESTOQUE_CINCO, FATOR_FACEAMENTO, PONTO_DE_EQUILIBRIO_DEZ, PRECISAO_DUAS_CASAS);
		assertEquals(13.75, resultado);
	}
	
	@Test
	public void sugestaoDeveSerQuatorze() {
		final FaceamentoEstoqueService faceamentoEstoqueService = FaceamentoEstoqueService.getInstance();
		double resultado = faceamentoEstoqueService.calculaSugestaoVenda(QUANTIDADE_ESTOQUE_CINCO, FATOR_FACEAMENTO, PONTO_DE_EQUILIBRIO_DEZ, PRECISAO_ZERO);
		assertEquals(14.0, resultado);
	}
	
	@Test
	public void sugestaoDeveSerZero() {
		final FaceamentoEstoqueService faceamentoEstoqueService = FaceamentoEstoqueService.getInstance();
		double resultado = faceamentoEstoqueService.calculaSugestaoVenda(QUANTIDADE_ESTOQUE_DEZ, FATOR_FACEAMENTO, PONTO_DE_EQUILIBRIO_CINCO, PRECISAO_ZERO);
		assertEquals(0.0, resultado);
	}
	
}
