package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BonifCfgClienteDbxDao;

public class BonifCfgClienteService extends CrudService {

    private static BonifCfgClienteService instance;
    
    private BonifCfgClienteService() {
        //--
    }
    
    public static BonifCfgClienteService getInstance() {
        if (instance == null) {
            instance = new BonifCfgClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return BonifCfgClienteDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

}