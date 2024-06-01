package br.com.wmw.lavenderepda.business.service;


import br.com.wmw.lavenderepda.business.domain.CatalogoParams;
import org.junit.jupiter.api.Test;
import totalcross.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogoProdutoServiceTestCase {
	CatalogoProdutoService catalogoService;

	@Test
	public void deveRetornarTrueSeJsonEstiverCorreto() {
		CatalogoParams catalogoParams = new CatalogoParams();
		catalogoParams.cdEmpresa="1";
		catalogoParams.cdRepresentante="123";
		catalogoParams.cdLocalEstoque="1;2";
		catalogoParams.cdTabelaPreco="12";
		catalogoParams.dsFiltroTexto="filtro";
		catalogoParams.estoqueDisponivelList="1;2;3;";
		catalogoParams.semPreco="N";
		catalogoParams.grupoProdutoList="2;4";
		catalogoParams.grupoDestaqueList="4;";
		JSONObject jsonEsperado = new JSONObject("{\"dsFiltroTexto\":\"filtro\",\"cdLocalEstoque\":\"1;2\",\"cdTabelaPreco\":\"12\",\"cdEmpresa\":\"1\",\"grupoDestaqueList\":\"4;\",\"cdRepresentante\":\"123\",\"grupoProdutoList\":\"2;4\",\"semPreco\":\"N\",\"estoqueDisponivelList\":\"1;2;3;\"}");
		JSONObject jsonRetornado = CatalogoProdutoService.getInstance().getCatalogoProdutoJson(catalogoParams);
		assertEquals(jsonEsperado.get("cdEmpresa"), jsonRetornado.get("cdEmpresa"));
		assertEquals(jsonEsperado.get("cdRepresentante"), jsonRetornado.get("cdRepresentante"));
		assertEquals(jsonEsperado.get("cdLocalEstoque"), jsonRetornado.get("cdLocalEstoque"));
		assertEquals(jsonEsperado.get("cdTabelaPreco"), jsonRetornado.get("cdTabelaPreco"));
		assertEquals(jsonEsperado.get("dsFiltroTexto"), jsonRetornado.get("dsFiltroTexto"));
		assertEquals(jsonEsperado.get("estoqueDisponivelList"), jsonRetornado.get("estoqueDisponivelList"));
		assertEquals(jsonEsperado.get("semPreco"), jsonRetornado.get("semPreco"));
		assertEquals(jsonEsperado.get("grupoProdutoList"), jsonRetornado.get("grupoProdutoList"));
		assertEquals(jsonEsperado.get("grupoDestaqueList"), jsonRetornado.get("grupoDestaqueList"));
	}

}
