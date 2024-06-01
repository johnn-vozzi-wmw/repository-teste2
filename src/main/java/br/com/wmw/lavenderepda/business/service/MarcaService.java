package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MarcaDbxDao;

public class MarcaService extends CrudService {

    private static MarcaService instance;
    
    private MarcaService() {
        //--
    }
    
    public static MarcaService getInstance() {
        if (instance == null) {
            instance = new MarcaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return MarcaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

}