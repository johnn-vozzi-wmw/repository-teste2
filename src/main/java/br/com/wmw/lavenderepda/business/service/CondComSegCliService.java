package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondComSegCliPdbxDao;

public class CondComSegCliService extends CrudService {

    private static CondComSegCliService instance;
    
    private CondComSegCliService() {
        //--
    }
    
    public static CondComSegCliService getInstance() {
        if (instance == null) {
            instance = new CondComSegCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondComSegCliPdbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}