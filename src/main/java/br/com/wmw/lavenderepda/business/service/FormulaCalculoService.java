package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FormulaCalculoDbxDao;

public class FormulaCalculoService extends CrudService {

    private static FormulaCalculoService instance;
    
    private FormulaCalculoService() { }
    
    public static FormulaCalculoService getInstance() {
        if (instance == null) {
            instance = new FormulaCalculoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FormulaCalculoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

}