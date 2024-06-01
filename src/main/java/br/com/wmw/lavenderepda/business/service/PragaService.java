package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PragaDbxDao;
import totalcross.util.Vector;

public class PragaService extends CrudService {
	
	public static PragaService instance;
	
	public static PragaService getInstance() {
		if (instance == null) {
			instance = new PragaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return PragaDbxDao.getInstance();
	}
	
	public Vector findAllPragaByAplicacaoProduto(BaseDomain domain) throws SQLException {
		return PragaDbxDao.getInstance().findAllPragaByAplicacaoProduto(domain);
	}

}
