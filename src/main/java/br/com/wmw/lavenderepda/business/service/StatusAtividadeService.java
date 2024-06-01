package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusAtividadeDao;

public class StatusAtividadeService extends CrudService {

    private static StatusAtividadeService instance = null;
    
    private StatusAtividadeService() {
        //--
    }
    
    public static StatusAtividadeService getInstance() {
        if (instance == null) {
            instance = new StatusAtividadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return StatusAtividadeDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { }

}