package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlataformaVendaCliFinDbxDao;

public class PlataformaVendaCliFinService extends CrudService {

    private static PlataformaVendaCliFinService instance;
    
    private PlataformaVendaCliFinService() { }
    
    public static PlataformaVendaCliFinService getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaCliFinService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PlataformaVendaCliFinDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
}