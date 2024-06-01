package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ColecaoDbxDao;

public class ColecaoService extends CrudService {

    private static ColecaoService instance;
    
    private ColecaoService() {
        //--
    }
    
    public static ColecaoService getInstance() {
        if (instance == null) {
            instance = new ColecaoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ColecaoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {    }

}