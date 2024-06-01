package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPagtoTabPrecoDbxDao;

public class TipoPagtoTabPrecoService extends CrudService {
	
	public static TipoPagtoTabPrecoService instance;
	
	public static TipoPagtoTabPrecoService getInstance() {
		if (instance == null) {
			instance = new TipoPagtoTabPrecoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return TipoPagtoTabPrecoDbxDao.getInstance();
	}

}
