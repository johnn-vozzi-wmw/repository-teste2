package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SaldoVendaRepDbxDao;

public class SaldoVendaRepService extends CrudService {

    private static SaldoVendaRepService instance;
    
    private SaldoVendaRepService() {
        //--
    }
    
    public static SaldoVendaRepService getInstance() {
        if (instance == null) {
            instance = new SaldoVendaRepService();
        }
        return instance;
    }
    
    @Override
    protected CrudDao getCrudDao() {
        return SaldoVendaRepDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {}

}