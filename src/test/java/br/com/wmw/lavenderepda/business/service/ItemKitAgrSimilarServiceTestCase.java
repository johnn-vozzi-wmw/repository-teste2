package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemKitAgrSimilar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ItemKitAgrSimilarServiceTestCase {

	public ItemKitAgrSimilarService service;

	public ItemKitAgrSimilarServiceTestCase() {
		service = ItemKitAgrSimilarService.getInstance();
	}

	@Test
	public void testGetItemKitAgrSimilarFilterByItemKit() {
		ItemKitAgrSimilar itemKitAgrSimilar = service.getItemKitAgrSimilarFilterByItemKit(null);
		assertNull(itemKitAgrSimilar);

		ItemKit itemKit = new ItemKit();
		itemKitAgrSimilar = service.getItemKitAgrSimilarFilterByItemKit(itemKit);
		assertNotNull(itemKitAgrSimilar);
		assertNull(itemKitAgrSimilar.cdEmpresa);

		itemKit.cdEmpresa = "1";
		itemKit.cdRepresentante = "2";
		itemKit.cdKit = "3";
		itemKit.cdProduto = "4";
		itemKit.cdTabelaPreco = "5";
		itemKitAgrSimilar = service.getItemKitAgrSimilarFilterByItemKit(itemKit);
		assertNotNull(itemKitAgrSimilar);
		assertNotNull(itemKitAgrSimilar.cdEmpresa);
		assertNotNull(itemKitAgrSimilar.cdRepresentante);
		assertNotNull(itemKitAgrSimilar.cdKit);
		assertNotNull(itemKitAgrSimilar.cdProduto);
		assertNotNull(itemKitAgrSimilar.cdTabelaPreco);
	}

}
