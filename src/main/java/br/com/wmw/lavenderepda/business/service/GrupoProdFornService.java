package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProdFornDao;

public class GrupoProdFornService extends CrudService {

    private static GrupoProdFornService instance = null;
    
    private GrupoProdFornService() {
        //--
    }
    
    public static GrupoProdFornService getInstance() {
        if (instance == null) {
            instance = new GrupoProdFornService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return GrupoProdFornDao.getInstance();
    }
 
    public void validate(BaseDomain domain) {  }
    
}