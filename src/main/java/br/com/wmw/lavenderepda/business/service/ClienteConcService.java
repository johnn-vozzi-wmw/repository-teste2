package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteConcDbxDao;

public class ClienteConcService extends CrudService {

    private static ClienteConcService instance;
    
    private ClienteConcService() {
        //--
    }
    
    public static ClienteConcService getInstance() {
        if (instance == null) {
            instance = new ClienteConcService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteConcDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}