package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.KitClienteDao;

public class KitClienteService extends CrudService {
	
	private static KitClienteService instance;
	
	public static KitClienteService getInstance() {
		if (instance == null) {
			instance = new KitClienteService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return KitClienteDao.getInstance();
	}

}
