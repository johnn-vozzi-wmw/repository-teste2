package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RestricaoClienteDbxDao;

public class RestricaoClienteService extends CrudService {

	private static RestricaoClienteService instance;

	public static RestricaoClienteService getInstance() { return instance == null ? instance = new RestricaoClienteService() : instance; }

	@Override public void validate(BaseDomain baseDomain) {}
	@Override protected CrudDao getCrudDao() { return RestricaoClienteDbxDao.getInstance(); }

}