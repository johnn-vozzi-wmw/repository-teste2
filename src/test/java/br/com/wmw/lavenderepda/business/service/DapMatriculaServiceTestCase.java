package br.com.wmw.lavenderepda.business.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import totalcross.util.Date;

public class DapMatriculaServiceTestCase   {

	@Test
	public void testeMatriculaValida() {
		Date dtValidade = new Date();
		dtValidade.advance(5);
		assertFalse(DapMatriculaService.getInstance().isMatriculaInvalida(dtValidade));
	}

	@Test
	public void testeMatriculaInvalida() {
		Date dtValidade = new Date();
		dtValidade.advance(-5);
		assertTrue(DapMatriculaService.getInstance().isMatriculaInvalida(dtValidade));
	}

	@Test
	public void testeMatriculaValidaVencimentoHoje() {
		Date dtValidade = new Date();
		assertFalse(DapMatriculaService.getInstance().isMatriculaInvalida(dtValidade));
	}

}
