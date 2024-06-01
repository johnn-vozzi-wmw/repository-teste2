package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioSupervisorDbxDao;

public class UsuarioSupervisorService extends CrudService {

    private static UsuarioSupervisorService instance;
    
    private UsuarioSupervisorService() {
        //--
    }
    
    public static UsuarioSupervisorService getInstance() {
        if (instance == null) {
            instance = new UsuarioSupervisorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioSupervisorDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}