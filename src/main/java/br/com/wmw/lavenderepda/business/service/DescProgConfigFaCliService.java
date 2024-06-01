package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgConfigFaCliDbxDao;

public class DescProgConfigFaCliService extends CrudService {

    private static DescProgConfigFaCliService instance;

    private DescProgConfigFaCliService() {}
	public static DescProgConfigFaCliService getInstance() { return instance == null ? instance = new DescProgConfigFaCliService() : instance; }

	@Override protected CrudDao getCrudDao() { return DescProgConfigFaCliDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

}