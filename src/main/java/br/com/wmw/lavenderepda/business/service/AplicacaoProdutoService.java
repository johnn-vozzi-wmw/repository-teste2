package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AplicacaoProdutoDbxDao;

public class AplicacaoProdutoService extends CrudService {
	
	public static AplicacaoProdutoService instance;
	
	public static AplicacaoProdutoService getInstance() {
		if (instance == null) {
			instance = new AplicacaoProdutoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return AplicacaoProdutoDbxDao.getInstance();
	}

}
