package br.com.wmw.lavenderepda.business.service;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculaEmbalagemTest  {

	@Test
	public void testEmbalagensCompletas() {
		
		double[] qtdEmbalagem = new double[]{1000d, 100d, 10d};
		
		double qtItemFisico = 9999;
		Map<String, Integer> testMap = CalculaEmbalagensService.getInstance().getQtdsPorTamanhoEmbalagemMap(qtdEmbalagem, qtItemFisico, true);
		assertTrue(testMap.get("menor") > 0 || testMap.get("maior") > 0);
		qtItemFisico = 10990;
		testMap = CalculaEmbalagensService.getInstance().getQtdsPorTamanhoEmbalagemMap(qtdEmbalagem, qtItemFisico, false);
		assertTrue(testMap.get("menor") == null || testMap.get("maior") == null);
		qtItemFisico = 9990;
		testMap = CalculaEmbalagensService.getInstance().getQtdsPorTamanhoEmbalagemMap(qtdEmbalagem, qtItemFisico, false);
		assertTrue(testMap.get("menor") == null || testMap.get("maior") == null);
		
		qtdEmbalagem = new double[]{30d, 20d, 10d};
		
		qtItemFisico = 9999;
		testMap = CalculaEmbalagensService.getInstance().getQtdsPorTamanhoEmbalagemMap(qtdEmbalagem, qtItemFisico, true);
		assertTrue(testMap.get("menor") > 0 || testMap.get("maior") > 0);
		qtItemFisico = 10990;
		testMap = CalculaEmbalagensService.getInstance().getQtdsPorTamanhoEmbalagemMap(qtdEmbalagem, qtItemFisico, false);
		assertTrue(testMap.get("menor") == null || testMap.get("maior") == null);
		qtItemFisico = 9990;
		testMap = CalculaEmbalagensService.getInstance().getQtdsPorTamanhoEmbalagemMap(qtdEmbalagem, qtItemFisico, false);
		assertTrue(testMap.get("menor") == null || testMap.get("maior") == null);
		
	}
	
}
