package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescQuantidadeClienteDao;

public class DescQuantidadeClienteService extends CrudService {
	
	private static DescQuantidadeClienteService instance;
	
	public static DescQuantidadeClienteService getInstance() {
		if (instance == null) {
			instance = new DescQuantidadeClienteService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return DescQuantidadeClienteDao.getInstance();
	}

}
