package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondComClienteDao;

public class CondComClienteService extends CrudService {

    private static CondComClienteService instance;
    
    public static CondComClienteService getInstance() {
        if (instance == null) {
            instance = new CondComClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondComClienteDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain arg0) {
	}
 

}