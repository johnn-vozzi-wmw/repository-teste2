package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto5Dao;

public class GrupoProduto5Service extends CrudService {
	
	private static GrupoProduto5Service instance;
	
	public static GrupoProduto5Service getInstance() {
		if (instance == null) {
			instance =  new GrupoProduto5Service();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return GrupoProduto5Dao.getInstance();
	}

}
