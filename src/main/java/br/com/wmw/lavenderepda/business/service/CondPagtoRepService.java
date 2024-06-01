package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoRepPdbxDao;

public class CondPagtoRepService extends CrudService {
	private static CondPagtoRepService instance;
	
	public CondPagtoRepService() {
	}

	public static CondPagtoRepService getInstance() {
		if (instance == null) {
			instance = new CondPagtoRepService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return CondPagtoRepPdbxDao.getInstance();
	}
}