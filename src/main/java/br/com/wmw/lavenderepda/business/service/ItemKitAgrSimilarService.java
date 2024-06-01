package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemComboAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemKitAgrSimilar;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemComboAgrSimilarDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemKitAgrSimilarDao;

import java.sql.SQLException;

public class ItemKitAgrSimilarService extends CrudService {
	
	private static ItemKitAgrSimilarService instance;

	@Override
	public void validate(BaseDomain domain) throws SQLException {}

	@Override
	protected CrudDao getCrudDao() { return ItemKitAgrSimilarDao.getInstance(); }
	
	public static ItemKitAgrSimilarService getInstance() {
		return instance == null ? instance = new ItemKitAgrSimilarService() : instance;
	}

	public ItemKitAgrSimilar getItemKitAgrSimilarFilterByItemKit(ItemKit itemKit) {
		if (itemKit == null) return null;
		ItemKitAgrSimilar itemKitAgrSimilar = new ItemKitAgrSimilar();
		itemKitAgrSimilar.cdEmpresa = itemKit.cdEmpresa;
		itemKitAgrSimilar.cdRepresentante = itemKit.cdRepresentante;
		itemKitAgrSimilar.cdKit = itemKit.cdKit;
		itemKitAgrSimilar.cdProduto = itemKit.cdProduto;
		itemKitAgrSimilar.cdTabelaPreco = itemKit.cdTabelaPreco;
		itemKitAgrSimilar.flBonificado = itemKit.flBonificado;
		return itemKitAgrSimilar;
	}
}
