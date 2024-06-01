package br.com.wmw.lavenderepda.business.service;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoAgendaDbxDao;


public class MotivoAgendaService extends CrudService {

    private static MotivoAgendaService instance;
    
    private MotivoAgendaService() {
        //--
    }
    
    public static MotivoAgendaService getInstance() {
        if (instance == null) {
            instance = new MotivoAgendaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotivoAgendaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    
}