package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SacDao;

public class SacProdutoAutService extends CrudService {

    private static SacProdutoAutService instance;
    
    private SacProdutoAutService() {
        //--
    }
    
    public static SacProdutoAutService getInstance() {
        if (instance == null) {
            instance = new SacProdutoAutService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SacDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
}