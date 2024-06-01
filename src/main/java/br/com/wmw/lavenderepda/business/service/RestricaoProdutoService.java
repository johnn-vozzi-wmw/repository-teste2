package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RestricaoProdutoDbxDao;

public class RestricaoProdutoService extends CrudService {

	private static RestricaoProdutoService instance;

	public static RestricaoProdutoService getInstance() { return instance == null ? instance = new RestricaoProdutoService() : instance; }

	@Override public void validate(BaseDomain baseDomain) {}
	@Override protected CrudDao getCrudDao() { return RestricaoProdutoDbxDao.getInstance(); }

}