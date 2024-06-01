package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemComboAgrSimilar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ItemComboAgrSimilarServiceTestCase {

	public ItemComboAgrSimilarService service;

	public ItemComboAgrSimilarServiceTestCase() {
		service = ItemComboAgrSimilarService.getInstance();
	}

	@Test
	public void testGetItemComboAgrSimilarFilterByItemCombo() {
		ItemComboAgrSimilar itemComboAgrSimilar = service.getItemComboAgrSimilarFilterByItemCombo(null);
		assertNull(itemComboAgrSimilar);

		ItemCombo itemCombo = new ItemCombo();
		itemComboAgrSimilar = service.getItemComboAgrSimilarFilterByItemCombo(itemCombo);
		assertNotNull(itemComboAgrSimilar);
		assertNull(itemComboAgrSimilar.cdEmpresa);

		itemCombo.cdEmpresa = "1";
		itemCombo.cdRepresentante = "2";
		itemCombo.cdCombo = "3";
		itemCombo.cdProduto = "4";
		itemCombo.cdTabelaPreco = "5";
		itemCombo.flTipoItemCombo = ItemCombo.TIPOITEMCOMBO_PRIMARIO;
		itemComboAgrSimilar = service.getItemComboAgrSimilarFilterByItemCombo(itemCombo);
		assertNotNull(itemComboAgrSimilar);
		assertNotNull(itemComboAgrSimilar.cdEmpresa);
		assertNotNull(itemComboAgrSimilar.cdRepresentante);
		assertNotNull(itemComboAgrSimilar.cdCombo);
		assertNotNull(itemComboAgrSimilar.cdProduto);
		assertNotNull(itemComboAgrSimilar.cdTabelaPreco);
		assertNotNull(itemComboAgrSimilar.flTipoItemCombo);
	}

}
