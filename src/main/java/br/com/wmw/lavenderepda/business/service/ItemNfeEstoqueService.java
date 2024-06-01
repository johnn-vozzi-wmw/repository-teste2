package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfeEstoqueDao;

public class ItemNfeEstoqueService extends CrudService {
	
	private static ItemNfeEstoqueService instance;
	
	public static ItemNfeEstoqueService getInstance() {
		if (instance == null) {
			instance = new ItemNfeEstoqueService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return ItemNfeEstoqueDao.getInstance();
	}

}
