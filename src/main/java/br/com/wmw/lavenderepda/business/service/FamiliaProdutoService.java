package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FamiliaProdutoDbxDao;

public class FamiliaProdutoService extends CrudService {

    private static FamiliaProdutoService instance;
    
    private FamiliaProdutoService() {
        //--
    }
    
    public static FamiliaProdutoService getInstance() {
        if (instance == null) {
            instance = new FamiliaProdutoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FamiliaProdutoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

}