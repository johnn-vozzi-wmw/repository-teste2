package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoLinhaPdbxDao;

public class CondPagtoLinhaService extends CrudService {

    private static CondPagtoLinhaService instance;

    private CondPagtoLinhaService() {
        //--
    }

    public static CondPagtoLinhaService getInstance() {
        if (instance == null) {
            instance = new CondPagtoLinhaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondPagtoLinhaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}