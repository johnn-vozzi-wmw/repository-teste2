package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoPerdaDbxDao;

public class MotivoPerdaService extends CrudService {

    private static MotivoPerdaService instance;
    
    private MotivoPerdaService() { }
    
    public static MotivoPerdaService getInstance() {
        if (instance == null) {
            instance = new MotivoPerdaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return MotivoPerdaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

}