package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoChurnDao;

public class MotivoChurnService extends CrudService {

    private static MotivoChurnService instance = null;
    
    private MotivoChurnService() { }
    
    public static MotivoChurnService getInstance() {
        if (instance == null) {
            instance = new MotivoChurnService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotivoChurnDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { }

}