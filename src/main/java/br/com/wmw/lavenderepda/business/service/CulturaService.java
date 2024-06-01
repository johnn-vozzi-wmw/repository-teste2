package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CulturaDbxDao;
import totalcross.util.Vector;

public class CulturaService extends CrudService {
	
	public static CulturaService instance;
	
	public static CulturaService getInstance() {
		if (instance == null) {
			instance = new CulturaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return CulturaDbxDao.getInstance();
	}
	
	public Vector findAllCulturaByAplicacaoProduto(BaseDomain domain) throws SQLException {
		return CulturaDbxDao.getInstance().findAllCulturaByAplicacaoProduto(domain);
	}

}
