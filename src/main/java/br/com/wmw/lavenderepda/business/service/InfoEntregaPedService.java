package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.InfoEntregaPedPdbxDao;

public class InfoEntregaPedService extends CrudService {

    private static InfoEntregaPedService instance;
    
    private InfoEntregaPedService() {
        //--
    }
    
    public static InfoEntregaPedService getInstance() {
        if (instance == null) {
            instance = new InfoEntregaPedService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return InfoEntregaPedPdbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}