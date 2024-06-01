package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FuncaoConfigDbxDao;

public class FuncaoConfigService extends CrudService {

    private static FuncaoConfigService instance;
    
    private FuncaoConfigService() {
        //--
    }
    
    public static FuncaoConfigService getInstance() {
        if (instance == null) {
            instance = new FuncaoConfigService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FuncaoConfigDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

}