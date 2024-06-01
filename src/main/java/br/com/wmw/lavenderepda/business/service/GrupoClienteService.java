package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoClienteDbxDao;

public class GrupoClienteService extends CrudService {

    private static GrupoClienteService instance;
    
    private GrupoClienteService() {
        //--
    }
    
    public static GrupoClienteService getInstance() {
        if (instance == null) {
            instance = new GrupoClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return GrupoClienteDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

}