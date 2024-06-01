package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemComboAgrSimilar;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemComboAgrSimilarDao;

import java.sql.SQLException;

public class ItemComboAgrSimilarService extends CrudService {
	
	private static ItemComboAgrSimilarService instance;

	@Override
	public void validate(BaseDomain domain) throws SQLException {}

	@Override
	protected CrudDao getCrudDao() { return ItemComboAgrSimilarDao.getInstance(); }
	
	public static ItemComboAgrSimilarService getInstance() {
		return instance == null ? instance = new ItemComboAgrSimilarService() : instance;
	}

	public ItemComboAgrSimilar getItemComboAgrSimilarFilterByItemCombo(ItemCombo itemCombo) {
		if (itemCombo == null) return null;
		ItemComboAgrSimilar itemComboAgrSimilar = new ItemComboAgrSimilar();
		itemComboAgrSimilar.cdEmpresa = itemCombo.cdEmpresa;
		itemComboAgrSimilar.cdRepresentante = itemCombo.cdRepresentante;
		itemComboAgrSimilar.cdCombo = itemCombo.cdCombo;
		itemComboAgrSimilar.cdProduto = itemCombo.cdProduto;
		itemComboAgrSimilar.cdTabelaPreco = itemCombo.cdTabelaPreco;
		itemComboAgrSimilar.flTipoItemCombo = itemCombo.flTipoItemCombo;
		return itemComboAgrSimilar;
	}
}
