package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ColunaTabelaPrecoDao;

public class ColunaTabelaPrecoService extends CrudService {

	private static ColunaTabelaPrecoService instance;
	
	public static ColunaTabelaPrecoService getInstance() {
		if (instance == null) {
			instance = new ColunaTabelaPrecoService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return ColunaTabelaPrecoDao.getInstance();
	}

}
