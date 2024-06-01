package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgFaixaDbxDao;

public class DescProgFaixaService extends CrudService {

    private static DescProgFaixaService instance;

    private DescProgFaixaService() {}
	public static DescProgFaixaService getInstance() { return instance == null ? instance = new DescProgFaixaService() : instance; }

	@Override protected CrudDao getCrudDao() { return DescProgFaixaDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

}