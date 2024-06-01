package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContaCorrenteCliDbxDao;

public class ContaCorrenteCliService extends CrudService {

    private static ContaCorrenteCliService instance;

    private ContaCorrenteCliService() {
    }

    public static ContaCorrenteCliService getInstance() {
        if (instance == null) {
            instance = new ContaCorrenteCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContaCorrenteCliDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}