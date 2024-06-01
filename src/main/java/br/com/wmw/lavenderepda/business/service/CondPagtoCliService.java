package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoCliPdbxDao;

public class CondPagtoCliService extends CrudService {

    private static CondPagtoCliService instance;

    private CondPagtoCliService() {
      //--
    }

    public static CondPagtoCliService getInstance() {
        if (instance == null) {
            instance = new CondPagtoCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondPagtoCliPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}