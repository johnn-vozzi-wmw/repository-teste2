package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelInadimpRepPdbxDao;

public class RelInadimpRepService extends CrudService {

    private static RelInadimpRepService instance;

    private RelInadimpRepService() {
        //--
    }

    public static RelInadimpRepService getInstance() {
        if (instance == null) {
            instance = new RelInadimpRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RelInadimpRepPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}