package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelInadimpCliPdbxDao;

public class RelInadimpCliService extends CrudService {

    private static RelInadimpCliService instance;

    private RelInadimpCliService() {
        //--
    }

    public static RelInadimpCliService getInstance() {
        if (instance == null) {
            instance = new RelInadimpCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RelInadimpCliPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}