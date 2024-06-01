package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConjuntoDbxDao;

public class ConjuntoService extends CrudService {

    private static ConjuntoService instance;
    
    private ConjuntoService() {
        //--
    }
    
    public static ConjuntoService getInstance() {
        if (instance == null) {
            instance = new ConjuntoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ConjuntoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

}