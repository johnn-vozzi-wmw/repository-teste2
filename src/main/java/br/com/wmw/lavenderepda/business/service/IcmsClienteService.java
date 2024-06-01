package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.IcmsClienteDbxDao;

public class IcmsClienteService extends CrudService {

    private static IcmsClienteService instance;
    
    private IcmsClienteService() {
        //--
    }
    
    public static IcmsClienteService getInstance() {
        if (instance == null) {
            instance = new IcmsClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return IcmsClienteDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

}