package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgFamiliaDbxDao;

public class DescProgFamiliaService extends CrudService {

    private static DescProgFamiliaService instance;

    private DescProgFamiliaService() {}
	public static DescProgFamiliaService getInstance() { return instance == null ? instance = new DescProgFamiliaService() : instance; }

	@Override protected CrudDao getCrudDao() { return DescProgFamiliaDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

}