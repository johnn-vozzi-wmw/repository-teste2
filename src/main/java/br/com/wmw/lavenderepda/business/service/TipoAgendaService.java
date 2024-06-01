package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoAgendaDbxDao;

public class TipoAgendaService extends CrudService {

    private static TipoAgendaService instance;
    
    private TipoAgendaService() {
        //--
    }
    
    public static TipoAgendaService getInstance() {
        if (instance == null) {
            instance = new TipoAgendaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoAgendaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}